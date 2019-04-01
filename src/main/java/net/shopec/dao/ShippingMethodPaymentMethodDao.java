package net.shopec.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import net.shopec.entity.ShippingMethodPaymentMethod;

/**
 * <p>
 * 配送方式支付方式中间表 Mapper 接口
 * </p>
 *
 * @author 江南红衣
 * @since 2018-11-26
 */
public interface ShippingMethodPaymentMethodDao extends BaseDao<ShippingMethodPaymentMethod> {

    /**
     * 批量保存
     * @param shippingMethodPaymentMethods
     * @return
     */
    int batchSave(@Param("shippingMethodPaymentMethods")List<ShippingMethodPaymentMethod> shippingMethodPaymentMethods);

}
