package net.shopec.plugin.discountPromotion;

import org.springframework.stereotype.Component;

import net.shopec.entity.Promotion;
import net.shopec.plugin.PromotionPlugin;

/**
 * Plugin - 折扣促销
 * 
 */
@Component("discountPromotionPlugin")
public class DiscountPromotionPlugin extends PromotionPlugin {

	/**
	 * ID
	 */
	public static final String ID = "discountPromotionPlugin";

	@Override
	public String getName() {
		return "折扣促销";
	}

	@Override
	public String getVersion() {
		return "1.0";
	}

	@Override
	public String getAuthor() {
		return "SHOPEC";
	}

	@Override
	public String getInstallUrl() {
		return "discount_promotion/install";
	}

	@Override
	public String getUninstallUrl() {
		return "discount_promotion/uninstall";
	}

	@Override
	public String getSettingUrl() {
		return "discount_promotion/setting";
	}

	@Override
	public String getPriceExpression(Promotion promotion, Boolean useAmountPromotion, Boolean useNumberPromotion) {
		if (promotion.getDiscount() == null) {
			return "";
		}
		if (promotion.getDiscount() < 1) {
			return "price*" + String.valueOf(promotion.getDiscount());
		} else {
			return "price-" + String.valueOf(promotion.getDiscount());
		}
	}

}