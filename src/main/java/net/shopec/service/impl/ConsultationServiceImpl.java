package net.shopec.service.impl;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;

import net.shopec.Filter;
import net.shopec.Order;
import net.shopec.Page;
import net.shopec.Pageable;
import net.shopec.dao.ConsultationDao;
import net.shopec.dao.MemberDao;
import net.shopec.dao.ProductDao;
import net.shopec.entity.Consultation;
import net.shopec.entity.Member;
import net.shopec.entity.Product;
import net.shopec.entity.Store;
import net.shopec.service.ConsultationService;

/**
 * Service - 咨询
 * 
 */
@Service
public class ConsultationServiceImpl extends BaseServiceImpl<Consultation> implements ConsultationService {

	@Inject
	private ConsultationDao consultationDao;
	@Inject
	private MemberDao memberDao;
	@Inject
	private ProductDao productDao;

	
	@Transactional(readOnly = true)
	public List<Consultation> findList(Member member, Product product, Boolean isShow, Integer count, List<Filter> filters, List<Order> orders) {
		EntityWrapper<Consultation> wrapper = toWrapper(null, count, filters, orders);
		return consultationDao.findList(wrapper, member, product, isShow);
	}

	@Transactional(readOnly = true)
	@Cacheable(value = "consultation", condition = "#useCache")
	public List<Consultation> findList(Long memberId, Long productId, Boolean isShow, Integer count, List<Filter> filters, List<Order> orders, boolean useCache) {
		Member member = memberDao.find(memberId);
		if (memberId != null && member == null) {
			return Collections.emptyList();
		}
		Product product = productDao.find(productId);
		if (productId != null && product == null) {
			return Collections.emptyList();
		}
		EntityWrapper<Consultation> wrapper = toWrapper(null, count, filters, orders);
		return consultationDao.findList(wrapper, member, product, isShow);
	}

	@Transactional(readOnly = true)
	public Page<Consultation> findPage(Member member, Product product, Store store, Boolean isShow, Pageable pageable) {
		com.baomidou.mybatisplus.plugins.Page<Consultation> plusPage = getPlusPage(pageable);
		plusPage.setRecords(consultationDao.findPage(plusPage, getWrapper(pageable), member, product, store, isShow));
		return super.findPage(plusPage, pageable);
	}

	@Transactional(readOnly = true)
	public Long count(Member member, Product product, Boolean isShow) {
		return consultationDao.count(member, product, isShow);
	}

	@CacheEvict(value = "consultation", allEntries = true)
	public void reply(Consultation consultation, Consultation replyConsultation) {
		if (consultation == null || replyConsultation == null) {
			return;
		}
		consultation.setIsShow(true);

		replyConsultation.setIsShow(true);
		replyConsultation.setProduct(consultation.getProduct());
		replyConsultation.setForConsultation(consultation);
		replyConsultation.setStore(consultation.getStore());
		super.save(replyConsultation);
	}

	@Override
	@Transactional
	@CacheEvict(value = "consultation", allEntries = true)
	public Consultation save(Consultation consultation) {
		return super.save(consultation);
	}

	@Override
	@Transactional
	@CacheEvict(value = "consultation", allEntries = true)
	public Consultation update(Consultation consultation) {
		return super.update(consultation);
	}

	@Override
	@Transactional
	@CacheEvict(value = "consultation", allEntries = true)
	public Consultation update(Consultation consultation, String... ignoreProperties) {
		return super.update(consultation, ignoreProperties);
	}

	@Override
	@Transactional
	@CacheEvict(value = "consultation", allEntries = true)
	public void delete(Long id) {
		super.delete(id);
	}

	@Override
	@Transactional
	@CacheEvict(value = "consultation", allEntries = true)
	public void delete(Long... ids) {
		super.delete(ids);
	}

	@Override
	@Transactional
	@CacheEvict(value = "consultation", allEntries = true)
	public void delete(Consultation consultation) {
		super.delete(consultation);
	}

}