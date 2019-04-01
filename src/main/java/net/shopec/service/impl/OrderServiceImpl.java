package net.shopec.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.toolkit.IdWorker;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.shopec.Filter;
import net.shopec.Page;
import net.shopec.Pageable;
import net.shopec.Setting;
import net.shopec.dao.CartDao;
import net.shopec.dao.OrderDao;
import net.shopec.dao.OrderItemDao;
import net.shopec.dao.OrderLogDao;
import net.shopec.dao.OrderPaymentDao;
import net.shopec.dao.OrderRefundsDao;
import net.shopec.dao.OrderReturnsDao;
import net.shopec.dao.OrderReturnsItemDao;
import net.shopec.dao.OrderShippingDao;
import net.shopec.dao.OrderShippingItemDao;
import net.shopec.entity.BusinessDepositLog;
import net.shopec.entity.Cart;
import net.shopec.entity.CartItem;
import net.shopec.entity.Coupon;
import net.shopec.entity.CouponCode;
import net.shopec.entity.Invoice;
import net.shopec.entity.Member;
import net.shopec.entity.MemberDepositLog;
import net.shopec.entity.Order;
import net.shopec.entity.OrderItem;
import net.shopec.entity.OrderLog;
import net.shopec.entity.OrderPayment;
import net.shopec.entity.OrderRefunds;
import net.shopec.entity.OrderReturns;
import net.shopec.entity.OrderReturnsItem;
import net.shopec.entity.OrderShipping;
import net.shopec.entity.OrderShippingItem;
import net.shopec.entity.PaymentMethod;
import net.shopec.entity.PointLog;
import net.shopec.entity.Product;
import net.shopec.entity.Receiver;
import net.shopec.entity.ShippingMethod;
import net.shopec.entity.Sku;
import net.shopec.entity.Sn;
import net.shopec.entity.StockLog;
import net.shopec.entity.Store;
import net.shopec.entity.User;
import net.shopec.service.BusinessService;
import net.shopec.service.CouponCodeService;
import net.shopec.service.MemberService;
import net.shopec.service.OrderService;
import net.shopec.service.ProductService;
import net.shopec.service.ShippingMethodService;
import net.shopec.service.SkuService;
import net.shopec.service.SnService;
import net.shopec.service.UserService;
import net.shopec.util.SystemUtils;

/**
 * Service - 订单
 * 
 */
@Service
public class OrderServiceImpl extends BaseServiceImpl<Order> implements OrderService {

	@Inject
	private CacheManager cacheManager;
	@Inject
	private OrderDao orderDao;
	@Inject
	private OrderItemDao orderItemDao;
	@Inject
	private OrderLogDao orderLogDao;
	@Inject
	private CartDao cartDao;
	@Inject
	private SnService snService;
	@Inject
	private OrderPaymentDao orderPaymentDao;
	@Inject
	private OrderRefundsDao orderRefundsDao;
	@Inject
	private OrderShippingDao orderShippingDao;
	@Inject
	private OrderShippingItemDao orderShippingItemDao;
	@Inject
	private OrderReturnsDao orderReturnsDao;
	@Inject
	private OrderReturnsItemDao orderReturnsItemDao;
	@Inject
	private UserService userService;
	@Inject
	private MemberService memberService;
	@Inject
	private BusinessService businessService;
	@Inject
	private CouponCodeService couponCodeService;
	@Inject
	private ProductService productService;
	@Inject
	private SkuService skuService;
	@Inject
	private ShippingMethodService shippingMethodService;
//	@Inject
//	private MailService mailService;
//	@Inject
//	private SmsService smsService;

	@Override
	public Order find(Long id) {
		return orderDao.find(id);
	}
	
	@Transactional(readOnly = true)
	public Order findBySn(String sn) {
		return orderDao.findByAttribute("sn", StringUtils.lowerCase(sn));
	}

	@Transactional(readOnly = true)
	public List<Order> findList(Order.Type type, Order.Status status, Store store, Member member, Product product, Boolean isPendingReceive, Boolean isPendingRefunds, Boolean isUseCouponCode, Boolean isExchangePoint, Boolean isAllocatedStock, Boolean hasExpired, Integer count, List<Filter> filters,
			List<net.shopec.Order> orders) {
		EntityWrapper<Order> wrapper = toWrapper(null, count, filters, orders);
		return orderDao.findList(wrapper, type, status, store, member, product, isPendingReceive, isPendingRefunds, isUseCouponCode, isExchangePoint, isAllocatedStock, hasExpired);
	}

	@Transactional(readOnly = true)
	public Page<Order> findPage(Order.Type type, Order.Status status, Store store, Member member, Product product, Boolean isPendingReceive, Boolean isPendingRefunds, Boolean isUseCouponCode, Boolean isExchangePoint, Boolean isAllocatedStock, Boolean hasExpired, Pageable pageable,String beginDate,String endDate) {
		com.baomidou.mybatisplus.plugins.Page<Order> plusPage = getPlusPage(pageable);
		plusPage.setRecords(orderDao.findPage(plusPage, getWrapper(pageable), type, status, store, member, product, isPendingReceive, isPendingRefunds, isUseCouponCode, isExchangePoint, isAllocatedStock, hasExpired,beginDate,endDate));
		return super.findPage(plusPage, pageable);
	}

	@Transactional(readOnly = true)
	public Long count(Order.Type type, Order.Status status, Store store, Member member, Product product, Boolean isPendingReceive, Boolean isPendingRefunds, Boolean isUseCouponCode, Boolean isExchangePoint, Boolean isAllocatedStock, Boolean hasExpired) {
		return orderDao.count(type, status, store, member, product, isPendingReceive, isPendingRefunds, isUseCouponCode, isExchangePoint, isAllocatedStock, hasExpired);
	}

	@Transactional(readOnly = true)
	public BigDecimal calculateTax(BigDecimal price, BigDecimal promotionDiscount, BigDecimal couponDiscount, BigDecimal offsetAmount) {
		Assert.notNull(price, "notNull");
		Assert.state(price.compareTo(BigDecimal.ZERO) >= 0, "state");
		Assert.state(promotionDiscount == null || promotionDiscount.compareTo(BigDecimal.ZERO) >= 0, "state");
		Assert.state(couponDiscount == null || couponDiscount.compareTo(BigDecimal.ZERO) >= 0, "state");

		Setting setting = SystemUtils.getSetting();
		if (!setting.getIsTaxPriceEnabled()) {
			return BigDecimal.ZERO;
		}
		BigDecimal amount = price;
		if (promotionDiscount != null) {
			amount = amount.subtract(promotionDiscount);
		}
		if (couponDiscount != null) {
			amount = amount.subtract(couponDiscount);
		}
		if (offsetAmount != null) {
			amount = amount.add(offsetAmount);
		}
		BigDecimal tax = amount.multiply(new BigDecimal(String.valueOf(setting.getTaxRate())));
		return tax.compareTo(BigDecimal.ZERO) >= 0 ? setting.setScale(tax) : BigDecimal.ZERO;
	}

	@Transactional(readOnly = true)
	public BigDecimal calculateTax(Order order) {
		Assert.notNull(order, "notNull");

		if (order.getInvoice() == null) {
			return BigDecimal.ZERO;
		}
		return calculateTax(order.getPrice(), order.getPromotionDiscount(), order.getCouponDiscount(), order.getOffsetAmount());
	}

	@Transactional(readOnly = true)
	public BigDecimal calculateAmount(BigDecimal price, BigDecimal fee, BigDecimal freight, BigDecimal tax, BigDecimal promotionDiscount, BigDecimal couponDiscount, BigDecimal offsetAmount) {
		Assert.notNull(price, "notNull");
		Assert.state(price.compareTo(BigDecimal.ZERO) >= 0, "state");
		Assert.state(fee == null || fee.compareTo(BigDecimal.ZERO) >= 0, "state");
		Assert.state(freight == null || freight.compareTo(BigDecimal.ZERO) >= 0, "state");
		Assert.state(tax == null || tax.compareTo(BigDecimal.ZERO) >= 0, "state");
		Assert.state(promotionDiscount == null || promotionDiscount.compareTo(BigDecimal.ZERO) >= 0, "state");
		Assert.state(couponDiscount == null || couponDiscount.compareTo(BigDecimal.ZERO) >= 0, "state");

		Setting setting = SystemUtils.getSetting();
		BigDecimal amount = price;
		if (fee != null) {
			amount = amount.add(fee);
		}
		if (freight != null) {
			amount = amount.add(freight);
		}
		if (tax != null) {
			amount = amount.add(tax);
		}
		if (promotionDiscount != null) {
			amount = amount.subtract(promotionDiscount);
		}
		if (couponDiscount != null) {
			amount = amount.subtract(couponDiscount);
		}
		if (offsetAmount != null) {
			amount = amount.add(offsetAmount);
		}
		return amount.compareTo(BigDecimal.ZERO) >= 0 ? setting.setScale(amount) : BigDecimal.ZERO;
	}

	@Transactional(readOnly = true)
	public BigDecimal calculateAmount(Order order) {
		Assert.notNull(order, "NotNull");

		return calculateAmount(order.getPrice(), order.getFee(), order.getFreight(), order.getTax(), order.getPromotionDiscount(), order.getCouponDiscount(), order.getOffsetAmount());
	}

	@Transactional(readOnly = true)
	public boolean acquireLock(Order order, User user) {
		Assert.notNull(order, "notNull");
		Assert.isTrue(!order.isNew(), "isTrue");
		Assert.notNull(user, "notNull");
		Assert.isTrue(!user.isNew(), "isTrue");

		Long orderId = order.getId();
		Ehcache cache = cacheManager.getEhcache(Order.ORDER_LOCK_CACHE_NAME);
		cache.acquireWriteLockOnKey(orderId);
		try {
			Element element = cache.get(orderId);
			if (element != null && !user.getId().equals(element.getObjectValue())) {
				return false;
			}
			cache.put(new Element(orderId, user.getId()));
		} finally {
			cache.releaseWriteLockOnKey(orderId);
		}
		return true;
	}

	@Transactional(readOnly = true)
	public boolean acquireLock(Order order) {
		User currentUser = userService.getCurrent();
		if (currentUser == null) {
			return false;
		}
		return acquireLock(order, currentUser);
	}

	@Transactional(readOnly = true)
	public void releaseLock(Order order) {
		Assert.notNull(order, "notNull");
		Assert.isTrue(!order.isNew(), "isTrue");

		Ehcache cache = cacheManager.getEhcache(Order.ORDER_LOCK_CACHE_NAME);
		cache.remove(order.getId());
	}

	public void undoExpiredUseCouponCode() {
		while (true) {
			EntityWrapper<Order> wrapper = toWrapper(null, 100, null, null);
			List<Order> orders = orderDao.findList(wrapper, null, null, null, null, null, null, null, true, null, null, true);
			if (CollectionUtils.isNotEmpty(orders)) {
				for (Order order : orders) {
					undoUseCouponCode(order);
				}
			}
			if (orders.size() < 100) {
				break;
			}
		}
	}

	public void undoExpiredExchangePoint() {
		while (true) {
			EntityWrapper<Order> wrapper = toWrapper(null, 100, null, null);
			List<Order> orders = orderDao.findList(wrapper, null, null, null, null, null, null, null, null, true, null, true);
			if (CollectionUtils.isNotEmpty(orders)) {
				for (Order order : orders) {
					undoExchangePoint(order);
				}
			}
			if (orders.size() < 100) {
				break;
			}
		}
	}

	public void releaseExpiredAllocatedStock() {
		while (true) {
			EntityWrapper<Order> wrapper = toWrapper(null, 100, null, null);
			List<Order> orders = orderDao.findList(wrapper, null, null, null, null, null, null, null, null, null, true, true);
			if (CollectionUtils.isNotEmpty(orders)) {
				for (Order order : orders) {
					releaseAllocatedStock(order);
				}
			}
			if (orders.size() < 100) {
				break;
			}
		}
	}

	@Transactional(readOnly = true)
	public List<Order> generate(Order.Type type, Cart cart, Receiver receiver, PaymentMethod paymentMethod, ShippingMethod shippingMethod, CouponCode couponCode, Invoice invoice, BigDecimal balance, String memo) {
		Assert.notNull(type, "notNull");
		Assert.notNull(cart, "notNull");
		Assert.notNull(cart.getMember(), "notNull");
		Assert.notNull(cart.getStores(), "notNull");
		Assert.state(!cart.isEmpty(), "state");

		Setting setting = SystemUtils.getSetting();
		Member member = cart.getMember();
		BigDecimal price = BigDecimal.ZERO;
		BigDecimal discount = BigDecimal.ZERO;
		Long effectiveRewardPoint = 0L;
		BigDecimal couponDiscount = BigDecimal.ZERO;

		List<Store> stores = cart.getStores();
		List<Order> orders = new ArrayList<>();
		for (Store store : stores) {
			price = cart.getPrice(store);
			discount = cart.getDiscount(store);
			effectiveRewardPoint = cart.getEffectiveRewardPoint(store);
			couponDiscount = couponCode != null && cart.isCouponAllowed(store) && cart.isValid(couponCode, store) ? cart.getEffectivePrice(store).subtract(couponCode.getCoupon().calculatePrice(cart.getEffectivePrice(store), cart.getSkuQuantity(store))) : BigDecimal.ZERO;
			Order order = new Order();
			order.setType(type);
			order.setPrice(price);
			order.setFee(BigDecimal.ZERO);
			order.setPromotionDiscount(discount);
			order.setOffsetAmount(BigDecimal.ZERO);
			order.setRefundAmount(BigDecimal.ZERO);
			order.setRewardPoint(effectiveRewardPoint);
			order.setExchangePoint(cart.getExchangePoint(store));
			order.setWeight(cart.getTotalWeight(store));
			order.setQuantity(cart.getTotalQuantity(store));
			order.setShippedQuantity(0);
			order.setReturnedQuantity(0);
			order.setMemo(memo);
			order.setIsUseCouponCode(false);
			order.setIsExchangePoint(false);
			order.setIsAllocatedStock(false);
			order.setInvoice(setting.getIsInvoiceEnabled() ? invoice : null);
			order.setPaymentMethod(paymentMethod);
			order.setMember(member);
			order.setStore(store);
			order.setPromotionNames(new ArrayList<>(cart.getPromotionNames(store)));
			order.setCoupons(new ArrayList<>(cart.getCoupons(store)));

			if (shippingMethod != null && shippingMethod.isSupported(paymentMethod) && cart.getIsDelivery(store)) {
				order.setFreight(!cart.isFreeShipping(store) ? shippingMethodService.calculateFreight(shippingMethod, store, receiver, cart.getNeedDeliveryTotalWeight(store)) : BigDecimal.ZERO);
				order.setShippingMethod(shippingMethod);
			} else {
				order.setFreight(BigDecimal.ZERO);
				order.setShippingMethod(null);
			}

			if (couponCode != null && cart.isCouponAllowed(store) && cart.isValid(couponCode, store)) {
				order.setCouponDiscount(couponDiscount.compareTo(BigDecimal.ZERO) >= 0 ? couponDiscount : BigDecimal.ZERO);
				order.setCouponCode(couponCode);
			} else {
				order.setCouponDiscount(BigDecimal.ZERO);
				order.setCouponCode(null);
			}

			order.setTax(calculateTax(order));
			order.setAmount(calculateAmount(order));

			if (balance != null && balance.compareTo(BigDecimal.ZERO) > 0 && balance.compareTo(member.getBalance()) <= 0) {
				if (balance.compareTo(order.getAmount()) <= 0) {
					order.setAmountPaid(balance);
				} else {
					order.setAmountPaid(order.getAmount());
					balance = balance.subtract(order.getAmount());
				}
			} else {
				order.setAmountPaid(BigDecimal.ZERO);
			}

			if (cart.getIsDelivery(store) && receiver != null) {
				order.setConsignee(receiver.getConsignee());
				order.setAreaName(receiver.getAreaName());
				order.setAddress(receiver.getAddress());
				order.setZipCode(receiver.getZipCode());
				order.setPhone(receiver.getPhone());
				order.setArea(receiver.getArea());
			}

			List<OrderItem> orderItems = order.getOrderItems();
			for (CartItem cartItem : cart.getCartItems(store)) {
				Sku sku = cartItem.getSku();
				if (sku != null) {
					OrderItem orderItem = new OrderItem();
					orderItem.setSn(sku.getSn());
					orderItem.setName(sku.getName());
					orderItem.setType(sku.getType());
					orderItem.setPrice(cartItem.getPrice());
					orderItem.setWeight(sku.getWeight());
					orderItem.setIsDelivery(sku.getIsDelivery());
					orderItem.setThumbnail(sku.getThumbnail());
					orderItem.setQuantity(cartItem.getQuantity());
					orderItem.setShippedQuantity(0);
					orderItem.setReturnedQuantity(0);
					orderItem.setSku(cartItem.getSku());
					orderItem.setOrder(order);
					orderItem.setSpecifications(sku.getSpecifications());
					orderItems.add(orderItem);
				}
			}

			for (Sku gift : cart.getGifts(store)) {
				OrderItem orderItem = new OrderItem();
				orderItem.setSn(gift.getSn());
				orderItem.setName(gift.getName());
				orderItem.setType(gift.getType());
				orderItem.setPrice(BigDecimal.ZERO);
				orderItem.setWeight(gift.getWeight());
				orderItem.setIsDelivery(gift.getIsDelivery());
				orderItem.setThumbnail(gift.getThumbnail());
				orderItem.setQuantity(1);
				orderItem.setShippedQuantity(0);
				orderItem.setReturnedQuantity(0);
				orderItem.setSku(gift);
				orderItem.setOrder(order);
				orderItem.setSpecifications(gift.getSpecifications());
				orderItems.add(orderItem);
			}
			orders.add(order);
		}
		return orders;
	}

	@Transactional
	public List<Order> create(Order.Type type, Cart cart, Receiver receiver, PaymentMethod paymentMethod, ShippingMethod shippingMethod, CouponCode couponCode, Invoice invoice, BigDecimal balance, String memo) {
		Assert.notNull(type, "notNull");
		Assert.notNull(cart, "notNull");
		Assert.notNull(cart.getMember(), "notNull");
		Assert.state(!cart.isEmpty(), "state");
		if (cart.getIsDelivery(null)) {
			Assert.notNull(receiver, "notNull");
			Assert.notNull(shippingMethod, "notNull");
			Assert.state(shippingMethod.isSupported(paymentMethod), "state");
		} else {
			Assert.isNull(receiver, "notNull");
			Assert.isNull(shippingMethod, "notNull");
		}
		List<Store> stores = cart.getStores();
		List<Order> orders = new ArrayList<>();

		for (CartItem cartItem : cart.getCartItems()) {
			Sku sku = cartItem.getSku();
			if (sku == null || !sku.getIsMarketable() || cartItem.getQuantity() > sku.getAvailableStock()) {
				throw new IllegalArgumentException();
			}
		}

		for (Store store : stores) {
			for (Sku gift : cart.getGifts(store)) {
				if (!gift.getIsMarketable() || gift.getIsOutOfStock()) {
					throw new IllegalArgumentException();
				}
			}

			Setting setting = SystemUtils.getSetting();
			Member member = cart.getMember();

			Order order = new Order();
			order.setId(IdWorker.getId());
			order.setVersion(0L);
			order.setSn(snService.generate(Sn.Type.order));
			order.setType(type);
			order.setPrice(cart.getPrice(store));
			order.setFee(BigDecimal.ZERO);
			order.setFreight(cart.getIsDelivery(store) && !cart.isFreeShipping(store) ? shippingMethodService.calculateFreight(shippingMethod, store, receiver, cart.getNeedDeliveryTotalWeight(store)) : BigDecimal.ZERO);
			order.setPromotionDiscount(cart.getDiscount(store));
			order.setOffsetAmount(BigDecimal.ZERO);
			order.setAmountPaid(BigDecimal.ZERO);
			order.setRefundAmount(BigDecimal.ZERO);
			order.setRewardPoint(cart.getEffectiveRewardPoint(store));
			order.setExchangePoint(cart.getExchangePoint(store));
			order.setWeight(cart.getTotalWeight(store));
			order.setQuantity(cart.getTotalQuantity(store));
			order.setShippedQuantity(0);
			order.setReturnedQuantity(0);
			if (cart.getIsDelivery(store)) {
				order.setConsignee(receiver.getConsignee());
				order.setAreaName(receiver.getAreaName());
				order.setAddress(receiver.getAddress());
				order.setZipCode(receiver.getZipCode());
				order.setPhone(receiver.getPhone());
				order.setArea(receiver.getArea());
			}
			order.setMemo(memo);
			order.setIsUseCouponCode(false);
			order.setIsExchangePoint(false);
			order.setIsAllocatedStock(false);
			order.setInvoice(setting.getIsInvoiceEnabled() ? invoice : null);
			order.setShippingMethod(shippingMethod);
			order.setMember(member);
			order.setStore(store);
			order.setPromotionNames(cart.getPromotionNames(store));
			order.setCoupons(new ArrayList<>(cart.getCoupons(store)));

			if (couponCode != null && couponCode.getCoupon().getStore().equals(store)) {
				if (!cart.isCouponAllowed(store) || !cart.isValid(couponCode, store)) {
					throw new IllegalArgumentException();
				}
				BigDecimal couponDiscount = cart.getEffectivePrice(store).subtract(couponCode.getCoupon().calculatePrice(cart.getEffectivePrice(store), cart.getSkuQuantity(store)));
				order.setCouponDiscount(couponDiscount.compareTo(BigDecimal.ZERO) >= 0 ? couponDiscount : BigDecimal.ZERO);
				order.setCouponCode(couponCode);
				useCouponCode(order);
			} else {
				order.setCouponDiscount(BigDecimal.ZERO);
			}

			order.setTax(calculateTax(order));
			order.setAmount(calculateAmount(order));
			if (balance != null && (balance.compareTo(BigDecimal.ZERO) < 0 || balance.compareTo(member.getBalance()) > 0)) {
				throw new IllegalArgumentException();
			}
			BigDecimal amountPayable = balance != null ? order.getAmount().subtract(balance) : order.getAmount();
			if (amountPayable.compareTo(BigDecimal.ZERO) > 0) {
				if (paymentMethod == null) {
					throw new IllegalArgumentException();
				}
				order.setStatus(PaymentMethod.Type.deliveryAgainstPayment.equals(paymentMethod.getType()) ? Order.Status.pendingPayment : Order.Status.pendingReview);
				order.setPaymentMethod(paymentMethod);
				if (paymentMethod.getTimeout() != null && Order.Status.pendingPayment.equals(order.getStatus())) {
					order.setExpire(DateUtils.addMinutes(new Date(), paymentMethod.getTimeout()));
				}
			} else {
				order.setStatus(Order.Status.pendingReview);
				order.setPaymentMethod(null);
			}
			
			/**
			 * 持久化前处理
			 */
			order.setSn(StringUtils.lowerCase(order.getSn()));
			if (order.getArea() != null) {
				order.setAreaName(order.getArea().getFullName());
			}
			if (order.getPaymentMethod() != null) {
				order.setPaymentMethodName(order.getPaymentMethod().getName());
				order.setPaymentMethodType(order.getPaymentMethod().getType());
			}
			if (order.getShippingMethod() != null) {
				order.setShippingMethodName(order.getShippingMethod().getName());
			}
			
			List<OrderItem> orderItems = order.getOrderItems();
			for (CartItem cartItem : cart.getCartItems(store)) {
				Sku sku = cartItem.getSku();
				OrderItem orderItem = new OrderItem();
				orderItem.setId(IdWorker.getId());
				orderItem.setSn(sku.getSn());
				orderItem.setName(sku.getName());
				orderItem.setType(sku.getType());
				orderItem.setPrice(cartItem.getPrice());
				orderItem.setWeight(sku.getWeight());
				orderItem.setIsDelivery(sku.getIsDelivery());
				orderItem.setThumbnail(sku.getThumbnail());
				orderItem.setQuantity(cartItem.getQuantity());
				orderItem.setShippedQuantity(0);
				orderItem.setReturnedQuantity(0);
				orderItem.setCommissionTotals(sku.getCommission(store.getType()).multiply(new BigDecimal(cartItem.getQuantity())));
				orderItem.setSku(cartItem.getSku());
				orderItem.setOrder(order);
				orderItem.setSpecifications(sku.getSpecifications());
				orderItems.add(orderItem);
			}

			for (Sku gift : cart.getGifts(store)) {
				OrderItem orderItem = new OrderItem();
				orderItem.setId(IdWorker.getId());
				orderItem.setSn(gift.getSn());
				orderItem.setName(gift.getName());
				orderItem.setType(gift.getType());
				orderItem.setPrice(BigDecimal.ZERO);
				orderItem.setWeight(gift.getWeight());
				orderItem.setIsDelivery(gift.getIsDelivery());
				orderItem.setThumbnail(gift.getThumbnail());
				orderItem.setQuantity(1);
				orderItem.setShippedQuantity(0);
				orderItem.setReturnedQuantity(0);
				orderItem.setCommissionTotals(gift.getCommission(store.getType()).multiply(new BigDecimal("1")));
				orderItem.setSku(gift);
				orderItem.setOrder(order);
				orderItem.setSpecifications(gift.getSpecifications());
				orderItems.add(orderItem);
			}

			orderDao.save(order);
			orderItemDao.batchSave(orderItems);

			OrderLog orderLog = new OrderLog();
			orderLog.setId(IdWorker.getId());
			orderLog.setType(OrderLog.Type.create);
			orderLog.setOrder(order);
			orderLogDao.save(orderLog);

			exchangePoint(order);
			if (Setting.StockAllocationTime.order.equals(setting.getStockAllocationTime())
					|| (Setting.StockAllocationTime.payment.equals(setting.getStockAllocationTime()) && (order.getAmountPaid().compareTo(BigDecimal.ZERO) > 0 || order.getExchangePoint() > 0 || order.getAmountPayable().compareTo(BigDecimal.ZERO) <= 0))) {
				allocateStock(order);
			}

			if (balance != null && balance.compareTo(BigDecimal.ZERO) > 0) {
				OrderPayment orderPayment = new OrderPayment();
				orderPayment.setMethod(OrderPayment.Method.deposit);
				orderPayment.setFee(BigDecimal.ZERO);
				orderPayment.setOrder(order);
				if (balance.compareTo(order.getAmount()) >= 0) {
					balance = balance.subtract(order.getAmount());
					orderPayment.setAmount(order.getAmount());
				} else {
					orderPayment.setAmount(balance);
					balance = BigDecimal.ZERO;
				}
				payment(order, orderPayment);
			}

//			mailService.sendCreateOrderMail(order);
//			smsService.sendCreateOrderSms(order);
			orders.add(order);
		}

		if (!cart.isNew()) {
			cartDao.remove(cart);
		}
		return orders;
	}

	public void modify(Order order) {
		Assert.notNull(order, "notNull");
		Assert.isTrue(!order.isNew(), "isTrue");
		Assert.state(!order.hasExpired() && (Order.Status.pendingPayment.equals(order.getStatus()) || Order.Status.pendingReview.equals(order.getStatus())), "state");

		order.setAmount(calculateAmount(order));
		if (order.getAmountPayable().compareTo(BigDecimal.ZERO) <= 0) {
			order.setStatus(Order.Status.pendingReview);
			order.setExpire(null);
		} else {
			if (order.getPaymentMethod() != null && PaymentMethod.Type.deliveryAgainstPayment.equals(order.getPaymentMethod().getType())) {
				order.setStatus(Order.Status.pendingPayment);
			} else {
				order.setStatus(Order.Status.pendingReview);
				order.setExpire(null);
			}
		}
		
		/**
		 * 更新前处理
		 */
		if (order.getArea() != null) {
			order.setAreaName(order.getArea().getFullName());
		}
		if (order.getPaymentMethod() != null) {
			order.setPaymentMethodName(order.getPaymentMethod().getName());
			order.setPaymentMethodType(order.getPaymentMethod().getType());
		}
		if (order.getShippingMethod() != null) {
			order.setShippingMethodName(order.getShippingMethod().getName());
		}
		
		orderDao.update(order);

		OrderLog orderLog = new OrderLog();
		orderLog.setId(IdWorker.getId());
		orderLog.setType(OrderLog.Type.modify);
		orderLog.setOrder(order);
		orderLogDao.save(orderLog);

//		mailService.sendUpdateOrderMail(order);
//		smsService.sendUpdateOrderSms(order);
	}

	public void cancel(Order order) {
		Assert.notNull(order, "notNull");
		Assert.isTrue(!order.isNew(), "isTrue");
		Assert.state(!order.hasExpired() && (Order.Status.pendingPayment.equals(order.getStatus()) || Order.Status.pendingReview.equals(order.getStatus())), "state");

		order.setStatus(Order.Status.canceled);
		order.setExpire(null);
		orderDao.update(order);

		undoUseCouponCode(order);
		undoExchangePoint(order);
		releaseAllocatedStock(order);

		if (order.getRefundableAmount().compareTo(BigDecimal.ZERO) > 0) {
			businessService.addBalance(order.getStore().getBusiness(), order.getRefundableAmount(), BusinessDepositLog.Type.orderRefunds, null);
		}

		OrderLog orderLog = new OrderLog();
		orderLog.setId(IdWorker.getId());
		orderLog.setType(OrderLog.Type.cancel);
		orderLog.setOrder(order);
		orderLogDao.save(orderLog);

	}

	public void review(Order order, boolean passed) {
		Assert.notNull(order, "notNull");
		Assert.isTrue(!order.isNew(), "isTrue");
		Assert.state(!order.hasExpired() && Order.Status.pendingReview.equals(order.getStatus()), "state");

		if (passed) {
			order.setStatus(Order.Status.pendingShipment);
		} else {
			order.setStatus(Order.Status.denied);

			if (order.getRefundableAmount().compareTo(BigDecimal.ZERO) > 0) {
				businessService.addBalance(order.getStore().getBusiness(), order.getRefundableAmount(), BusinessDepositLog.Type.orderRefunds, null);
			}
			undoUseCouponCode(order);
			undoExchangePoint(order);
			releaseAllocatedStock(order);
		}
		orderDao.update(order);

		OrderLog orderLog = new OrderLog();
		orderLog.setId(IdWorker.getId());
		orderLog.setType(OrderLog.Type.review);
		orderLog.setOrder(order);
		orderLogDao.save(orderLog);

	}

	public void payment(Order order, OrderPayment orderPayment) {
		Assert.notNull(order, "notNull");
		Assert.isTrue(!order.isNew(), "isTrue");
		Assert.notNull(orderPayment, "notNull");
		Assert.isTrue(orderPayment.isNew(), "isTrue");
		Assert.notNull(orderPayment.getAmount(), "notNull");
		Assert.state(orderPayment.getAmount().compareTo(BigDecimal.ZERO) > 0, "state");

		orderPayment.setSn(snService.generate(Sn.Type.orderPayment));
		orderPayment.setOrder(order);
		orderPayment.setId(IdWorker.getId());
		orderPaymentDao.save(orderPayment);

		if (order.getMember() != null && OrderPayment.Method.deposit.equals(orderPayment.getMethod())) {
			memberService.addBalance(order.getMember(), orderPayment.getEffectiveAmount().negate(), MemberDepositLog.Type.orderPayment, null);
		}

		Setting setting = SystemUtils.getSetting();
		if (Setting.StockAllocationTime.payment.equals(setting.getStockAllocationTime())) {
			allocateStock(order);
		}

		order.setAmountPaid(order.getAmountPaid().add(orderPayment.getEffectiveAmount()));
		order.setFee(order.getFee().add(orderPayment.getFee()));
		if (!order.hasExpired() && Order.Status.pendingPayment.equals(order.getStatus()) && order.getAmountPayable().compareTo(BigDecimal.ZERO) <= 0) {
			order.setStatus(Order.Status.pendingReview);
			order.setExpire(null);
		}
		orderDao.update(order);

		OrderLog orderLog = new OrderLog();
		orderLog.setId(IdWorker.getId());
		orderLog.setType(OrderLog.Type.payment);
		orderLog.setOrder(order);
		orderLogDao.save(orderLog);
	}

	public void refunds(Order order, OrderRefunds orderRefunds) {
		Assert.notNull(order, "notNull");
		Assert.isTrue(!order.isNew(), "isTrue");
		Assert.state(order.getRefundableAmount().compareTo(BigDecimal.ZERO) > 0, "state");
		Assert.notNull(orderRefunds, "notNull");
		Assert.isTrue(orderRefunds.isNew(), "isTrue");
		Assert.notNull(orderRefunds.getAmount(), "notNull");
		Assert.state(orderRefunds.getAmount().compareTo(BigDecimal.ZERO) > 0 && orderRefunds.getAmount().compareTo(order.getRefundableAmount()) <= 0, "state");
		Assert.state(!OrderRefunds.Method.deposit.equals(orderRefunds.getMethod()) || order.getStore().getBusiness().getBalance().compareTo(orderRefunds.getAmount()) >= 0, "state");

		orderRefunds.setSn(snService.generate(Sn.Type.orderRefunds));
		orderRefunds.setOrder(order);
		orderRefunds.setId(IdWorker.getId());
		orderRefundsDao.save(orderRefunds);

		if (OrderRefunds.Method.deposit.equals(orderRefunds.getMethod())) {
			memberService.addBalance(order.getMember(), orderRefunds.getAmount(), MemberDepositLog.Type.orderRefunds, null);
			businessService.addBalance(order.getStore().getBusiness(), orderRefunds.getAmount().negate(), BusinessDepositLog.Type.orderRefunds, null);
		}

		order.setAmountPaid(order.getAmountPaid().subtract(orderRefunds.getAmount()));
		order.setRefundAmount(order.getRefundAmount().add(orderRefunds.getAmount()));
		orderDao.update(order);
		
		OrderLog orderLog = new OrderLog();
		orderLog.setId(IdWorker.getId());
		orderLog.setType(OrderLog.Type.refunds);
		orderLog.setOrder(order);
		orderLogDao.save(orderLog);
	}

	@Transactional
	public void shipping(Order order, OrderShipping orderShipping) {
		Assert.notNull(order, "notNull");
		Assert.isTrue(!order.isNew(), "isTrue");
		Assert.state(order.getShippableQuantity() > 0, "state");
		Assert.notNull(orderShipping, "notNull");
		Assert.isTrue(orderShipping.isNew(), "isTrue");
		Assert.notEmpty(orderShipping.getOrderShippingItems(), "notEmpty");

		orderShipping.setSn(snService.generate(Sn.Type.orderShipping));
		orderShipping.setOrder(order);
		orderShipping.setId(IdWorker.getId());
		orderShippingDao.save(orderShipping);
		
		for(OrderShippingItem orderShippingItem : orderShipping.getOrderShippingItems()) {
			orderShippingItem.setId(IdWorker.getId());
			orderShippingItem.setOrderShipping(orderShipping);
			orderShippingItemDao.save(orderShippingItem);
		}
		
		Setting setting = SystemUtils.getSetting();
		if (Setting.StockAllocationTime.ship.equals(setting.getStockAllocationTime())) {
			allocateStock(order);
		}

		for (OrderShippingItem orderShippingItem : orderShipping.getOrderShippingItems()) {
			OrderItem orderItem = order.getOrderItem(orderShippingItem.getSn());
			if (orderItem == null || orderShippingItem.getQuantity() > orderItem.getShippableQuantity()) {
				throw new IllegalArgumentException();
			}
			orderItem.setShippedQuantity(orderItem.getShippedQuantity() + orderShippingItem.getQuantity());
			orderItemDao.update(orderItem);
			Sku sku = orderShippingItem.getSku();
			if (sku != null) {
				if (orderShippingItem.getQuantity() > sku.getStock()) {
					throw new IllegalArgumentException();
				}
				skuService.addStock(sku, -orderShippingItem.getQuantity(), StockLog.Type.stockOut, null);
				if (BooleanUtils.isTrue(order.getIsAllocatedStock())) {
					skuService.addAllocatedStock(sku, -orderShippingItem.getQuantity());
				}
			}
		}

		order.setShippedQuantity(order.getShippedQuantity() + orderShipping.getQuantity());
		if (order.getShippedQuantity() >= order.getQuantity()) {
			order.setStatus(Order.Status.shipped);
			order.setIsAllocatedStock(false);
		}
		orderDao.update(order);
		
		OrderLog orderLog = new OrderLog();
		orderLog.setId(IdWorker.getId());
		orderLog.setType(OrderLog.Type.shipping);
		orderLog.setOrder(order);
		orderLogDao.save(orderLog);
	}

	@Transactional
	public void returns(Order order, OrderReturns orderReturns) {
		Assert.notNull(order, "notNull");
		Assert.isTrue(!order.isNew(), "isTrue");
		Assert.state(order.getReturnableQuantity() > 0, "state");
		Assert.notNull(orderReturns, "notNull");
		Assert.isTrue(orderReturns.isNew(), "isTrue");
		Assert.notEmpty(orderReturns.getOrderReturnsItems(), "notEmpty");

		orderReturns.setSn(snService.generate(Sn.Type.orderReturns));
		orderReturns.setOrder(order);
		orderReturns.setId(IdWorker.getId());
		orderReturnsDao.save(orderReturns);
		
		for (OrderReturnsItem orderReturnsItem : orderReturns.getOrderReturnsItems()) {
			orderReturnsItem.setId(IdWorker.getId());
			orderReturnsItem.setOrderReturns(orderReturns);
			orderReturnsItemDao.save(orderReturnsItem);
		}
		for (OrderReturnsItem orderReturnsItem : orderReturns.getOrderReturnsItems()) {
			OrderItem orderItem = order.getOrderItem(orderReturnsItem.getSn());
			if (orderItem == null || orderReturnsItem.getQuantity() > orderItem.getReturnableQuantity()) {
				throw new IllegalArgumentException();
			}
			orderItem.setReturnedQuantity(orderItem.getReturnedQuantity() + orderReturnsItem.getQuantity());
			orderItemDao.update(orderItem);
		}

		order.setReturnedQuantity(order.getReturnedQuantity() + orderReturns.getQuantity());
		orderDao.update(order);
		
		OrderLog orderLog = new OrderLog();
		orderLog.setId(IdWorker.getId());
		orderLog.setType(OrderLog.Type.returns);
		orderLog.setOrder(order);
		orderLogDao.save(orderLog);
	}

	public void receive(Order order) {
		Assert.notNull(order, "notNull");
		Assert.isTrue(!order.isNew(), "isTrue");
		Assert.state(!order.hasExpired() && Order.Status.shipped.equals(order.getStatus()), "state");

		order.setStatus(Order.Status.received);
		orderDao.update(order);
		
		OrderLog orderLog = new OrderLog();
		orderLog.setId(IdWorker.getId());
		orderLog.setType(OrderLog.Type.receive);
		orderLog.setOrder(order);
		orderLogDao.save(orderLog);

//		mailService.sendReceiveOrderMail(order);
//		smsService.sendReceiveOrderSms(order);
	}

	@CacheEvict(value = { "product", "productCategory" }, allEntries = true)
	public void complete(Order order) {
		Assert.notNull(order, "notNull");
		Assert.isTrue(!order.isNew(), "isTrue");
		Assert.state(!order.hasExpired() && Order.Status.received.equals(order.getStatus()), "state");

		Member member = order.getMember();
		if (order.getRewardPoint() > 0) {
			memberService.addPoint(member, order.getRewardPoint(), PointLog.Type.reward, null);
		}
		if (CollectionUtils.isNotEmpty(order.getCoupons())) {
			for (Coupon coupon : order.getCoupons()) {
				couponCodeService.generate(coupon, member);
			}
		}
		if (order.getAmountPaid().compareTo(BigDecimal.ZERO) > 0) {
			memberService.addAmount(member, order.getAmountPaid());
		}
		if (order.getSettlementAmount().compareTo(BigDecimal.ZERO) > 0) {
			businessService.addBalance(order.getStore().getBusiness(), order.getSettlementAmount(), BusinessDepositLog.Type.orderSettlement, null);
		}
		for (OrderItem orderItem : order.getOrderItems()) {
			Sku sku = orderItem.getSku();
			if (sku != null && sku.getProduct() != null) {
				productService.addSales(sku.getProduct(), orderItem.getQuantity());
			}
		}

		order.setStatus(Order.Status.completed);
		order.setCompleteDate(new Date());
		orderDao.update(order);

		OrderLog orderLog = new OrderLog();
		orderLog.setId(IdWorker.getId());
		orderLog.setType(OrderLog.Type.complete);
		orderLog.setOrder(order);
		orderLogDao.save(orderLog);

//		mailService.sendCompleteOrderMail(order);
//		smsService.sendCompleteOrderSms(order);
	}

	public void fail(Order order) {
		Assert.notNull(order, "notNull");
		Assert.isTrue(!order.isNew(), "isTrue");
		Assert.state(!order.hasExpired() && (Order.Status.pendingShipment.equals(order.getStatus()) || Order.Status.shipped.equals(order.getStatus()) || Order.Status.received.equals(order.getStatus())), "state");

		order.setStatus(Order.Status.failed);

		undoUseCouponCode(order);
		undoExchangePoint(order);
		releaseAllocatedStock(order);

		if (order.getRefundableAmount().compareTo(BigDecimal.ZERO) > 0) {
			businessService.addBalance(order.getStore().getBusiness(), order.getRefundableAmount(), BusinessDepositLog.Type.orderRefunds, null);
		}
		orderDao.update(order);
		
		OrderLog orderLog = new OrderLog();
		orderLog.setId(IdWorker.getId());
		orderLog.setType(OrderLog.Type.fail);
		orderLog.setOrder(order);
		orderLogDao.save(orderLog);
	}

	@Override
	@Transactional
	public void delete(Order order) {
		if (order != null && !Order.Status.completed.equals(order.getStatus())) {
			undoUseCouponCode(order);
			undoExchangePoint(order);
			releaseAllocatedStock(order);
		}

		super.delete(order);
	}

	/**
	 * 优惠码使用
	 * 
	 * @param order
	 *            订单
	 */
	private void useCouponCode(Order order) {
		if (order == null || BooleanUtils.isNotFalse(order.getIsUseCouponCode()) || order.getCouponCode() == null) {
			return;
		}
		CouponCode couponCode = order.getCouponCode();
		couponCode.setIsUsed(true);
		couponCode.setUsedDate(new Date());
		order.setIsUseCouponCode(true);
	}

	/**
	 * 优惠码使用撤销
	 * 
	 * @param order
	 *            订单
	 */
	private void undoUseCouponCode(Order order) {
		if (order == null || BooleanUtils.isNotTrue(order.getIsUseCouponCode()) || order.getCouponCode() == null) {
			return;
		}
		CouponCode couponCode = order.getCouponCode();
		couponCode.setIsUsed(false);
		couponCode.setUsedDate(null);
		couponCodeService.update(couponCode);
		order.setIsUseCouponCode(false);
		order.setCouponCode(null);
		orderDao.update(order);
	}

	/**
	 * 积分兑换
	 * 
	 * @param order
	 *            订单
	 */
	private void exchangePoint(Order order) {
		if (order == null || BooleanUtils.isNotFalse(order.getIsExchangePoint()) || order.getExchangePoint() <= 0 || order.getMember() == null) {
			return;
		}
		memberService.addPoint(order.getMember(), -order.getExchangePoint(), PointLog.Type.exchange, null);
		order.setIsExchangePoint(true);
	}

	/**
	 * 积分兑换撤销
	 * 
	 * @param order
	 *            订单
	 */
	private void undoExchangePoint(Order order) {
		if (order == null || BooleanUtils.isNotTrue(order.getIsExchangePoint()) || order.getExchangePoint() <= 0 || order.getMember() == null) {
			return;
		}
		memberService.addPoint(order.getMember(), order.getExchangePoint(), PointLog.Type.undoExchange, null);
		order.setIsExchangePoint(false);
		orderDao.update(order);
	}

	/**
	 * 分配库存
	 * 
	 * @param order
	 *            订单
	 */
	private void allocateStock(Order order) {
		if (order == null || BooleanUtils.isNotFalse(order.getIsAllocatedStock())) {
			return;
		}
		if (order.getOrderItems() != null) {
			for (OrderItem orderItem : order.getOrderItems()) {
				Sku sku = orderItem.getSku();
				if (sku != null) {
					skuService.addAllocatedStock(sku, orderItem.getQuantity() - orderItem.getShippedQuantity());
				}
			}
		}
		order.setIsAllocatedStock(true);
	}

	/**
	 * 释放已分配库存
	 * 
	 * @param order
	 *            订单
	 */
	private void releaseAllocatedStock(Order order) {
		if (order == null || BooleanUtils.isNotTrue(order.getIsAllocatedStock())) {
			return;
		}
		if (order.getOrderItems() != null) {
			for (OrderItem orderItem : order.getOrderItems()) {
				Sku sku = orderItem.getSku();
				if (sku != null) {
					skuService.addAllocatedStock(sku, -(orderItem.getQuantity() - orderItem.getShippedQuantity()));
				}
			}
		}
		order.setIsAllocatedStock(false);
		orderDao.update(order);
	}

	@Transactional(readOnly = true)
	public Long completeOrderCount(Store store, Date beginDate, Date endDate) {
		return orderDao.completeOrderCount(store, beginDate, endDate);
	}

	@Transactional(readOnly = true)
	public BigDecimal completeOrderAmount(Store store, Date beginDate, Date endDate) {
		return orderDao.completeOrderAmount(store, beginDate, endDate);
	}

	public void automaticReceive() {
		Date currentTime = new Date();
		while (true) {
			EntityWrapper<Order> wrapper = toWrapper(null, 100, null, null);
			List<Order> orders = orderDao.findList(wrapper, null, Order.Status.shipped, null, null, null, null, null, null, null, null, false);
			if (CollectionUtils.isNotEmpty(orders)) {
				for (Order order : orders) {
					OrderShipping orderShipping = orderShippingDao.findLast(order);
					Date automaticReceiveTime = DateUtils.addDays(orderShipping.getCreatedDate(), SystemUtils.getSetting().getAutomaticReceiveTime());
					if (automaticReceiveTime.compareTo(currentTime) < 0) {
						order.setStatus(Order.Status.received);
						orderDao.update(order);
					}
				}
			}
			if (orders.size() < 100) {
				break;
			}
		}
	}

}