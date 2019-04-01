package net.shopec.service.impl;

import org.springframework.stereotype.Service;

import net.shopec.entity.Parameter;
import net.shopec.service.ParameterService;

/**
 * Service - 参数
 * 
 */
@Service
public class ParameterServiceImpl extends BaseServiceImpl<Parameter> implements ParameterService {
	
	@Override
	public Parameter save(Parameter parameter) {
		return super.save(parameter);
	}
	
	@Override
	public Parameter update(Parameter parameter, String... ignoreProperties) {
		return super.update(parameter);
	}
	
}