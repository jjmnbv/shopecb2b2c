package net.shopec.plugin.fullReductionPromotion;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import net.shopec.entity.Promotion;
import net.shopec.plugin.PromotionPlugin;

/**
 * Plugin - 满减促销
 * 
 */
@Component("fullReductionPromotionPlugin")
public class FullReductionPromotionPlugin extends PromotionPlugin {

	/**
	 * ID
	 */
	public static final String ID = "fullReductionPromotionPlugin";

	@Override
	public String getName() {
		return "满减促销";
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
		return "full_reduction_promotion/install";
	}

	@Override
	public String getUninstallUrl() {
		return "full_reduction_promotion/uninstall";
	}

	@Override
	public String getSettingUrl() {
		return "full_reduction_promotion/setting";
	}

	@Override
	public String getPriceExpression(Promotion promotion, Boolean useAmountPromotion, Boolean useNumberPromotion) {
		if (useAmountPromotion != null && useAmountPromotion) {
			BigDecimal conditionsAmoun = promotion.getConditionsAmount();
			BigDecimal creditAmount = promotion.getCreditAmount();
			if (conditionsAmoun != null && creditAmount != null && conditionsAmoun.compareTo(BigDecimal.ZERO) > 0 && creditAmount.compareTo(BigDecimal.ZERO) > 0) {
				return "price-((price/" + conditionsAmoun.toString() + ") as int) *" + creditAmount.toString();
			}
		} else if (useNumberPromotion != null && useNumberPromotion) {
			Integer conditionsNumber = promotion.getConditionsNumber();
			Integer creditNumber = promotion.getCreditNumber();
			if (conditionsNumber != null && creditNumber != null && conditionsNumber > 0 && creditNumber > 0) {
				return "price-(quantity.intdiv(" + conditionsNumber + "))*" + "(" + creditNumber + "*" + "(price/quantity)" + ")";
			}
		}
		return "";
	}

}