package net.shopec.service.impl;

import java.util.Date;
import java.util.UUID;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.baomidou.mybatisplus.toolkit.IdWorker;

import net.shopec.dao.CartDao;
import net.shopec.dao.CartItemDao;
import net.shopec.entity.Cart;
import net.shopec.entity.CartItem;
import net.shopec.entity.Member;
import net.shopec.entity.Sku;
import net.shopec.event.CartAddedEvent;
import net.shopec.event.CartClearedEvent;
import net.shopec.event.CartMergedEvent;
import net.shopec.event.CartModifiedEvent;
import net.shopec.event.CartRemovedEvent;
import net.shopec.service.CartService;
import net.shopec.service.UserService;
import net.shopec.util.WebUtils;

/**
 * Service - 购物车
 * 
 */
@Service
public class CartServiceImpl extends BaseServiceImpl<Cart> implements CartService {

	@Inject
	private ApplicationEventPublisher applicationEventPublisher;
	@Inject
	private CartDao cartDao;
	@Inject
	private CartItemDao cartItemDao;
	@Inject
	private UserService userService;

	@Transactional(readOnly = true)
	public Cart getCurrent() {
		Member currentUser = userService.getCurrent(Member.class);
		return currentUser != null ? currentUser.getCart() : getAnonymousCart();
	}

	public Cart create() {
		Member currentUser = userService.getCurrent(Member.class);
		if (currentUser != null && currentUser.getCart() != null) {
			return currentUser.getCart();
		}
		Cart cart = new Cart();
		if (currentUser != null) {
			cart.setMember(currentUser);
			currentUser.setCart(cart);
		}
		cart.setKey(DigestUtils.md5Hex(UUID.randomUUID() + RandomStringUtils.randomAlphabetic(30)));
		if (cart.getMember() == null) {
			cart.setExpire(DateUtils.addSeconds(new Date(), Cart.TIMEOUT));
		} else {
			cart.setExpire(null);
		}
		super.save(cart);
		return cart;
	}

	public void add(Cart cart, Sku sku, int quantity) {
		Assert.notNull(cart, "notNull");
		Assert.isTrue(!cart.isNew(), "isTrue");
		Assert.notNull(sku, "notNull");
		Assert.isTrue(!sku.isNew(), "isTrue");
		Assert.state(quantity > 0, "state");

		addInternal(cart, sku, quantity);

		applicationEventPublisher.publishEvent(new CartAddedEvent(this, cart, sku, quantity));
	}

	public void modify(Cart cart, Sku sku, int quantity) {
		Assert.notNull(cart, "notNull");
		Assert.isTrue(!cart.isNew(), "isTrue");
		Assert.notNull(sku, "notNull");
		Assert.isTrue(!sku.isNew(), "isTrue");
		Assert.isTrue(cart.contains(sku, null), "isTrue");
		Assert.state(quantity > 0, "state");

		if (CartItem.MAX_QUANTITY != null && quantity > CartItem.MAX_QUANTITY) {
			return;
		}

		CartItem cartItem = cart.getCartItem(sku, null);
		cartItem.setQuantity(quantity);
		cartItem.setLastModifiedDate(new Date());
		cartItemDao.update(cartItem);
		applicationEventPublisher.publishEvent(new CartModifiedEvent(this, cart, sku, quantity));
	}

	public void remove(Cart cart, Sku sku) {
		Assert.notNull(cart, "notNull");
		Assert.isTrue(!cart.isNew(), "isTrue");
		Assert.notNull(sku, "notNull");
		Assert.isTrue(!sku.isNew(), "isTrue");
		Assert.isTrue(cart.contains(sku, null), "isTrue");

		CartItem cartItem = cart.getCartItem(sku, null);
		cartItemDao.remove(cartItem);
		cart.remove(cartItem);

		applicationEventPublisher.publishEvent(new CartRemovedEvent(this, cart, sku));
	}

	public void clear(Cart cart) {
		Assert.notNull(cart, "notNull");
		Assert.isTrue(!cart.isNew(), "isTrue");

		for (CartItem cartItem : cart) {
			cartItemDao.remove(cartItem);
		}
		cart.clear();

		applicationEventPublisher.publishEvent(new CartClearedEvent(this, cart));
	}

	public void merge(Cart cart) {
		Assert.notNull(cart, "notNull");
		Assert.isTrue(!cart.isNew(), "isTrue");
		Assert.notNull(cart.getMember(), "notNull");

		Cart anonymousCart = getAnonymousCart();
		if (anonymousCart != null) {
			for (CartItem cartItem : anonymousCart) {
				Sku sku = cartItem.getSku();
				int quantity = cartItem.getQuantity();
				addInternal(cart, sku, quantity);
			}
			cartDao.remove(anonymousCart);
		}

		applicationEventPublisher.publishEvent(new CartMergedEvent(this, cart));
	}

	public void deleteExpired() {
		cartDao.deleteExpired();
	}

	/**
	 * 获取匿名购物车
	 * 
	 * @return 匿名购物车，若不存在则返回null
	 */
	private Cart getAnonymousCart() {
		HttpServletRequest request = WebUtils.getRequest();
		if (request == null) {
			return null;
		}
		String key = WebUtils.getCookie(request, Cart.KEY_COOKIE_NAME);
		Cart cart = StringUtils.isNotEmpty(key) ? cartDao.findByAttribute("cart_key", key) : null;
		return cart != null && cart.getMember() == null ? cart : null;
	}

	/**
	 * 添加购物车SKU
	 * 
	 * @param cart
	 *            购物车
	 * @param sku
	 *            SKU
	 * @param quantity
	 *            数量
	 */
	private void addInternal(Cart cart, Sku sku, int quantity) {
		Assert.notNull(cart, "notNull");
		Assert.isTrue(!cart.isNew(), "isTrue");
		Assert.notNull(sku, "notNull");
		Assert.isTrue(!sku.isNew(), "isTrue");
		Assert.state(quantity > 0, "notNull");

		if (cart.contains(sku, null)) {
			CartItem cartItem = cart.getCartItem(sku, null);
			if (CartItem.MAX_QUANTITY != null && cartItem.getQuantity() + quantity > CartItem.MAX_QUANTITY) {
				return;
			}
			cartItem.add(quantity);
			cartItem.setLastModifiedDate(new Date());
			cartItemDao.update(cartItem);
		} else {
			if (Cart.MAX_CART_ITEM_SIZE != null && cart.size() >= Cart.MAX_CART_ITEM_SIZE) {
				return;
			}
			if (CartItem.MAX_QUANTITY != null && quantity > CartItem.MAX_QUANTITY) {
				return;
			}
			CartItem cartItem = new CartItem();
			cartItem.setQuantity(quantity);
			cartItem.setSku(sku);
			cartItem.setCart(cart);
			cartItem.setId(IdWorker.getId());
			cartItem.setVersion(0L);
			cartItemDao.save(cartItem);
			cart.add(cartItem);
		}
	}

}