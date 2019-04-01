package net.shopec.service.impl;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import net.shopec.Setting;
import net.shopec.dao.PaymentTransactionDao;
import net.shopec.entity.Business;
import net.shopec.entity.BusinessDepositLog;
import net.shopec.entity.Member;
import net.shopec.entity.MemberDepositLog;
import net.shopec.entity.Order;
import net.shopec.entity.OrderPayment;
import net.shopec.entity.PaymentItem;
import net.shopec.entity.PaymentMethod;
import net.shopec.entity.PaymentTransaction;
import net.shopec.entity.PaymentTransaction.LineItem;
import net.shopec.entity.PlatformSvc;
import net.shopec.entity.PromotionPluginSvc;
import net.shopec.entity.Sn;
import net.shopec.entity.Store;
import net.shopec.entity.Svc;
import net.shopec.entity.User;
import net.shopec.plugin.PaymentPlugin;
import net.shopec.plugin.discountPromotion.DiscountPromotionPlugin;
import net.shopec.plugin.fullReductionPromotion.FullReductionPromotionPlugin;
import net.shopec.service.BusinessService;
import net.shopec.service.MemberService;
import net.shopec.service.OrderService;
import net.shopec.service.PaymentTransactionService;
import net.shopec.service.ProductService;
import net.shopec.service.SnService;
import net.shopec.service.StoreService;
import net.shopec.service.SvcService;
import net.shopec.service.UserService;
import net.shopec.util.SystemUtils;

/**
 * Service - 支付事务
 * 
 */
@Service
public class PaymentTransactionServiceImpl extends BaseServiceImpl<PaymentTransaction> implements PaymentTransactionService {

	@Inject
	private PaymentTransactionDao paymentTransactionDao;
	@Inject
	private SnService snService;
	@Inject
	private ProductService productService;
	@Inject
	private OrderService orderService;
	@Inject
	private UserService userService;
	@Inject
	private MemberService memberService;
	@Inject
	private BusinessService businessService;
	@Inject
	private StoreService storeService;
	@Inject
	private SvcService svcService;

	@Transactional(readOnly = true)
	public PaymentTransaction findBySn(String sn) {
		return paymentTransactionDao.findByAttribute("sn", StringUtils.lowerCase(sn));
	}

	public PaymentTransaction generate(PaymentTransaction.LineItem lineItem, PaymentPlugin paymentPlugin) {
		Assert.notNull(lineItem, "notNull");
		Assert.notNull(paymentPlugin, "notNull");
		Assert.notNull(lineItem.getAmount(), "notNull");
		Assert.notNull(lineItem.getType(), "notNull");
		Assert.notNull(lineItem.getTarget(), "notNull");

		BigDecimal amount = paymentPlugin.calculateAmount(lineItem.getAmount());
		BigDecimal fee = paymentPlugin.calculateFee(lineItem.getAmount());
		PaymentTransaction paymentTransaction = paymentTransactionDao.findAvailable(lineItem, paymentPlugin, amount, fee);
		if (paymentTransaction == null) {
			paymentTransaction = new PaymentTransaction();
			paymentTransaction.setSn(snService.generate(Sn.Type.paymentTransaction));
			paymentTransaction.setType(lineItem.getType());
			paymentTransaction.setAmount(paymentPlugin.calculateAmount(lineItem.getAmount()));
			paymentTransaction.setFee(paymentPlugin.calculateFee(lineItem.getAmount()));
			paymentTransaction.setIsSuccess(false);
			paymentTransaction.setExpire(DateUtils.addSeconds(new Date(), paymentPlugin.getTimeout()));
			paymentTransaction.setParent(null);
			paymentTransaction.setChildren(null);
			paymentTransaction.setTarget(lineItem.getTarget());
			paymentTransaction.setPaymentPlugin(paymentPlugin);
			super.save(paymentTransaction);
		}
		return paymentTransaction;
	}

	public PaymentTransaction generateParent(Collection<PaymentTransaction.LineItem> lineItems, PaymentPlugin paymentPlugin) {
		Assert.notEmpty(lineItems, "notEmpty");
		Assert.notNull(paymentPlugin, "notNull");
		Assert.isTrue(lineItems.size() > 1, "isTrue");

		PaymentTransaction parentPaymentTransaction = paymentTransactionDao.findAvailableParent(lineItems, paymentPlugin);
		if (parentPaymentTransaction == null) {
			BigDecimal amount = BigDecimal.ZERO;
			for (PaymentTransaction.LineItem lineItem : lineItems) {
				Assert.notNull(lineItem, "notNull");
				Assert.notNull(lineItem.getAmount(), "notNull");
				Assert.notNull(lineItem.getType(), "notNull");
				Assert.notNull(lineItem.getTarget(), "notNull");

				amount = amount.add(lineItem.getAmount());
			}

			parentPaymentTransaction = new PaymentTransaction();
			parentPaymentTransaction.setSn(snService.generate(Sn.Type.paymentTransaction));
			parentPaymentTransaction.setType(null);
			parentPaymentTransaction.setAmount(paymentPlugin.calculateAmount(amount));
			parentPaymentTransaction.setFee(paymentPlugin.calculateFee(amount));
			parentPaymentTransaction.setIsSuccess(false);
			parentPaymentTransaction.setExpire(DateUtils.addSeconds(new Date(), paymentPlugin.getTimeout()));
			parentPaymentTransaction.setParent(null);
			parentPaymentTransaction.setChildren(null);
			parentPaymentTransaction.setTarget(null);
			parentPaymentTransaction.setPaymentPlugin(paymentPlugin);
			super.save(parentPaymentTransaction);
			for (PaymentTransaction.LineItem lineItem : lineItems) {
				Assert.notNull(lineItem, "notNull");
				Assert.notNull(lineItem.getAmount(), "notNull");
				Assert.notNull(lineItem.getType(), "notNull");
				Assert.notNull(lineItem.getTarget(), "notNull");

				PaymentTransaction paymentTransaction = new PaymentTransaction();
				paymentTransaction.setSn(snService.generate(Sn.Type.paymentTransaction));
				paymentTransaction.setType(lineItem.getType());
				paymentTransaction.setAmount(paymentPlugin.calculateAmount(lineItem.getAmount()));
				paymentTransaction.setFee(paymentPlugin.calculateFee(lineItem.getAmount()));
				paymentTransaction.setIsSuccess(null);
				paymentTransaction.setExpire(null);
				paymentTransaction.setChildren(null);
				paymentTransaction.setTarget(lineItem.getTarget());
				paymentTransaction.setPaymentPlugin(null);
				paymentTransaction.setParent(parentPaymentTransaction);
				super.save(paymentTransaction);
			}
		}
		return parentPaymentTransaction;
	}

	public void handle(PaymentTransaction paymentTransaction) {
		Assert.notNull(paymentTransaction, "notNull");

		if (BooleanUtils.isNotFalse(paymentTransaction.getIsSuccess())) {
			return;
		}

		Set<PaymentTransaction> paymentTransactions = new HashSet<>();
		Set<PaymentTransaction> childrenList = paymentTransaction.getChildren();
		if (CollectionUtils.isNotEmpty(childrenList)) {
			paymentTransaction.setIsSuccess(true);
			paymentTransactions = childrenList;
		} else {
			paymentTransactions.add(paymentTransaction);
		}

		for (PaymentTransaction transaction : paymentTransactions) {
			Svc svc = transaction.getSvc();
			Store store = transaction.getStore();
			//User user = transaction.getUser();
			Business business = transaction.getBusiness();
			Member member = transaction.getMember();
			BigDecimal effectiveAmount = transaction.getEffectiveAmount();

			Assert.notNull(transaction.getType(), "notNull");
			switch (transaction.getType()) {
			case ORDER_PAYMENT:
				Order order = transaction.getOrder();
				if (order != null && Order.Status.pendingPayment.equals(order.getStatus())) {
					OrderPayment orderPayment = new OrderPayment();
					orderPayment.setMethod(OrderPayment.Method.online);
					orderPayment.setPaymentMethod(transaction.getPaymentPluginName());
					orderPayment.setAmount(transaction.getAmount());
					orderPayment.setFee(transaction.getFee());
					orderPayment.setOrder(order);
					orderService.payment(order, orderPayment);
				}
				break;
			case SVC_PAYMENT:
				if (svc == null || svc.getStore() == null) {
					break;
				}
				store = svc.getStore();

				Integer durationDays = svc.getDurationDays();
				if (svc instanceof PlatformSvc) {
					storeService.addEndDays(store, durationDays);
					if (Store.Status.approved.equals(store.getStatus()) && !store.hasExpired() && store.getBailPayable().compareTo(BigDecimal.ZERO) == 0) {
						store.setStatus(Store.Status.success);
					} else {
						productService.refreshActive(store);
					}
				} else if (svc instanceof PromotionPluginSvc) {
					String promotionPluginId = ((PromotionPluginSvc) svc).getPromotionPluginId();
					switch (promotionPluginId) {
					case DiscountPromotionPlugin.ID:
						storeService.addDiscountPromotionEndDays(store, durationDays);
						break;
					case FullReductionPromotionPlugin.ID:
						storeService.addFullReductionPromotionEndDays(store, durationDays);
						break;
					}
				}
				break;
			case DEPOSIT_RECHARGE:
				if (member != null && member instanceof Member) {
					memberService.addBalance(member, effectiveAmount, MemberDepositLog.Type.recharge, null);
				} else if (business != null && business instanceof Business) {
					businessService.addBalance(business, effectiveAmount, BusinessDepositLog.Type.recharge, null);
				}
				break;
			case BAIL_PAYMENT:
				if (store == null) {
					break;
				}

				storeService.addBailPaid(store, effectiveAmount);
				if (Store.Status.approved.equals(store.getStatus()) && !store.hasExpired() && store.getBailPayable().compareTo(BigDecimal.ZERO) == 0) {
					store.setStatus(Store.Status.success);
				} else {
					productService.refreshActive(store);
				}
				break;
			}
			transaction.setIsSuccess(true);
			paymentTransactionDao.update(transaction);
		}
	}

	public LineItem generate(PaymentItem paymentItem) {
		if (paymentItem == null || paymentItem.getType() == null) {
			return null;
		}
		Setting setting = SystemUtils.getSetting();
		User user = userService.getCurrent();
		switch (paymentItem.getType()) {
		case ORDER_PAYMENT:
			Member member = (Member) user;
			if (member == null) {
				return null;
			}
			Order order = orderService.findBySn(paymentItem.getOrderSn());
			if (order == null || !member.equals(order.getMember()) || !orderService.acquireLock(order, member)) {
				return null;
			}
			if (order.getPaymentMethod() == null || !PaymentMethod.Method.online.equals(order.getPaymentMethod().getMethod())) {
				return null;
			}
			if (order.getAmountPayable().compareTo(BigDecimal.ZERO) <= 0) {
				return null;
			}
			return new PaymentTransaction.OrderLineItem(order);
		case SVC_PAYMENT:
			Svc svc = svcService.findBySn(paymentItem.getSvcSn());
			if (svc == null) {
				return null;
			}
			if (svc.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
				return null;
			}
			return new PaymentTransaction.SvcLineItem(svc);
		case DEPOSIT_RECHARGE:
			if (user == null) {
				return null;
			}
			if (paymentItem.getAmount() == null || paymentItem.getAmount().compareTo(BigDecimal.ZERO) <= 0 || paymentItem.getAmount().precision() > 15 || paymentItem.getAmount().scale() > setting.getPriceScale()) {
				return null;
			}
			if (user instanceof Member) {
				return new PaymentTransaction.DepositRechargerLineItem(user, paymentItem.getAmount());
			} else if (user instanceof Business) {
				return new PaymentTransaction.DepositRechargerLineItem(user, paymentItem.getAmount());
			} else {
				return null;
			}
		case BAIL_PAYMENT:
			Store store = storeService.getCurrent();
			if (store == null) {
				return null;
			}
			if (paymentItem.getAmount() == null || paymentItem.getAmount().compareTo(BigDecimal.ZERO) <= 0 || paymentItem.getAmount().precision() > 15 || paymentItem.getAmount().scale() > setting.getPriceScale()) {
				return null;
			}
			return new PaymentTransaction.BailPaymentLineItem(store, paymentItem.getAmount());
		}
		return null;
	}

	@Override
	@Transactional
	public PaymentTransaction save(PaymentTransaction paymentTransaction) {
		Assert.notNull(paymentTransaction, "notNull");
		paymentTransaction.setSn(snService.generate(Sn.Type.paymentTransaction));
		
		return super.save(paymentTransaction);
	}

}