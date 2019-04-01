package net.shopec.service.impl;

import org.springframework.stereotype.Service;

import net.shopec.entity.Specification;
import net.shopec.service.SpecificationService;

/**
 * Service - 规格
 * 
 */
@Service
public class SpecificationServiceImpl extends BaseServiceImpl<Specification> implements SpecificationService {

	@Override
	public Specification save(Specification specification) {
		return super.save(specification);
	}
	
	@Override
	public Specification update(Specification specification, String... ignoreProperties) {
		return super.update(specification, ignoreProperties);
	}
	
}