package net.shopec.service.impl;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.baomidou.mybatisplus.mapper.EntityWrapper;

import net.shopec.Filter;
import net.shopec.Order;
import net.shopec.Page;
import net.shopec.Pageable;
import net.shopec.dao.MemberDao;
import net.shopec.dao.OrderDao;
import net.shopec.dao.ProductDao;
import net.shopec.dao.ReviewDao;
import net.shopec.entity.Member;
import net.shopec.entity.Product;
import net.shopec.entity.Review;
import net.shopec.entity.Store;
import net.shopec.service.ReviewService;

/**
 * Service - 评论
 * 
 */
@Service
public class ReviewServiceImpl extends BaseServiceImpl<Review> implements ReviewService {

	@Inject
	private ReviewDao reviewDao;
	@Inject
	private MemberDao memberDao;
	@Inject
	private ProductDao productDao;
	@Inject
	private OrderDao orderDao;

	@Transactional(readOnly = true)
	public List<Review> findList(Member member, Product product, Review.Type type, Boolean isShow, Integer count, List<Filter> filters, List<Order> orders) {
		EntityWrapper<Review> wrapper = toWrapper(null, count, filters, orders);
		return reviewDao.findList(wrapper, member, product, type, isShow);
	}

	@Transactional(readOnly = true)
	@Cacheable(value = "review", condition = "#useCache")
	public List<Review> findList(Long memberId, Long productId, Review.Type type, Boolean isShow, Integer count, List<Filter> filters, List<Order> orders, boolean useCache) {
		Member member = memberDao.find(memberId);
		if (memberId != null && member == null) {
			return Collections.emptyList();
		}
		Product product = productDao.find(productId);
		if (productId != null && product == null) {
			return Collections.emptyList();
		}
		EntityWrapper<Review> wrapper = toWrapper(null, count, filters, orders);
		return reviewDao.findList(wrapper, member, product, type, isShow);
	}

	@Transactional(readOnly = true)
	public Page<Review> findPage(Member member, Product product, Store store, Review.Type type, Boolean isShow, Pageable pageable) {
		com.baomidou.mybatisplus.plugins.Page<Review> plusPage = getPlusPage(pageable);
		plusPage.setRecords(reviewDao.findPage(plusPage, getWrapper(pageable), member, product, store, type, isShow));
		return super.findPage(plusPage, pageable);
	}

	@Transactional(readOnly = true)
	public Long count(Member member, Product product, Review.Type type, Boolean isShow) {
		return reviewDao.count(member, product, type, isShow);
	}

	@Transactional(readOnly = true)
	public boolean hasPermission(Member member, Product product) {
		Assert.notNull(member, "notNull");
		Assert.notNull(product, "notNull");

		long reviewCount = reviewDao.count(member, product, null, null);
		long orderCount = orderDao.count(null, net.shopec.entity.Order.Status.completed, null, member, product, null, null, null, null, null, null);
		return orderCount > reviewCount;
	}

	@Override
	@Transactional
	@CacheEvict(value = "review", allEntries = true)
	public Review save(Review review) {
		Assert.notNull(review, "notNull");

		Review pReview = super.save(review);
		Product product = pReview.getProduct();
		if (product != null) {
			long totalScore = reviewDao.calculateTotalScore(product);
			long scoreCount = reviewDao.calculateScoreCount(product);
			product.setTotalScore(totalScore);
			product.setScoreCount(scoreCount);
			productDao.update(product);
		}
		return pReview;
	}

	@Override
	@Transactional
	@CacheEvict(value = "review", allEntries = true)
	public Review update(Review review) {
		Assert.notNull(review, "notNull");

		Review pReview = super.update(review);
		Product product = pReview.getProduct();
		if (product != null) {
			long totalScore = reviewDao.calculateTotalScore(product);
			long scoreCount = reviewDao.calculateScoreCount(product);
			product.setTotalScore(totalScore);
			product.setScoreCount(scoreCount);
			productDao.update(product);
		}
		return pReview;
	}

	@Override
	@Transactional
	@CacheEvict(value = "review", allEntries = true)
	public Review update(Review review, String... ignoreProperties) {
		return super.update(review, ignoreProperties);
	}

	@Override
	@Transactional
	@CacheEvict(value = "review", allEntries = true)
	public void delete(Long id) {
		super.delete(id);
	}

	@Override
	@Transactional
	@CacheEvict(value = "review", allEntries = true)
	public void delete(Long... ids) {
		super.delete(ids);
	}

	@Override
	@Transactional
	@CacheEvict(value = "review", allEntries = true)
	public void delete(Review review) {
		if (review != null) {
			super.delete(review);
			Product product = review.getProduct();
			if (product != null) {
				//reviewDao.flush();
				long totalScore = reviewDao.calculateTotalScore(product);
				long scoreCount = reviewDao.calculateScoreCount(product);
				product.setTotalScore(totalScore);
				product.setScoreCount(scoreCount);
				productDao.updateById(product);
			}
		}
	}

	@CacheEvict(value = "review", allEntries = true)
	public void reply(Review review, Review replyReview) {
		if (review == null || replyReview == null) {
			return;
		}

		replyReview.setIsShow(true);
		replyReview.setProduct(review.getProduct());
		replyReview.setForReview(review);
		replyReview.setStore(review.getStore());
		replyReview.setScore(review.getScore());
		replyReview.setMember(review.getMember());
		super.save(replyReview);
	}

}