package net.shopec.service.impl;

import java.util.List;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.shopec.Page;
import net.shopec.Pageable;
import net.shopec.dao.ProductNotifyDao;
import net.shopec.entity.Member;
import net.shopec.entity.ProductNotify;
import net.shopec.entity.Sku;
import net.shopec.entity.Store;
import net.shopec.service.MailService;
import net.shopec.service.ProductNotifyService;

/**
 * Service - 到货通知
 * 
 */
@Service
public class ProductNotifyServiceImpl extends BaseServiceImpl<ProductNotify> implements ProductNotifyService {

	@Inject
	private ProductNotifyDao productNotifyDao;
	@Inject
	private MailService mailService;

	@Transactional(readOnly = true)
	public boolean exists(Sku sku, String email) {
		return productNotifyDao.exists(sku, email);
	}

	@Transactional(readOnly = true)
	public Page<ProductNotify> findPage(Store store, Member member, Boolean isMarketable, Boolean isOutOfStock, Boolean hasSent, Pageable pageable) {
		com.baomidou.mybatisplus.plugins.Page<ProductNotify> plusPage = getPlusPage(pageable);
		plusPage.setRecords(productNotifyDao.findPage(plusPage, getWrapper(pageable), store, member, isMarketable, isOutOfStock, hasSent));
		return super.findPage(plusPage, pageable);
	}

	@Transactional(readOnly = true)
	public Long count(Member member, Boolean isMarketable, Boolean isOutOfStock, Boolean hasSent) {
		return productNotifyDao.count(member, isMarketable, isOutOfStock, hasSent);
	}

	public int send(List<ProductNotify> productNotifies) {
		if (CollectionUtils.isEmpty(productNotifies)) {
			return 0;
		}

		int count = 0;
		for (ProductNotify productNotify : productNotifies) {
			if (productNotify != null && StringUtils.isNotEmpty(productNotify.getEmail())) {
				mailService.sendProductNotifyMail(productNotify);
				productNotify.setHasSent(true);
				productNotifyDao.update(productNotify);
				count++;
			}
		}
		return count;
	}

	@Override
	public ProductNotify save(ProductNotify productNotify) {
		return super.save(productNotify);
	}
}