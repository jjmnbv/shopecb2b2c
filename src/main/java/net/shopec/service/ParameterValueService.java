package net.shopec.service;

import java.util.List;

import net.shopec.entity.ParameterValue;

/**
 * Service - 参数值
 * 
 */
public interface ParameterValueService {

	/**
	 * 参数值过滤
	 * 
	 * @param parameterValues
	 *            参数值
	 */
	void filter(List<ParameterValue> parameterValues);

}