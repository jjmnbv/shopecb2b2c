package net.shopec.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import net.shopec.entity.SitemapIndex;
import net.shopec.entity.SitemapUrl;
import net.shopec.service.SitemapIndexService;
import net.shopec.service.SitemapUrlService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * Service - Sitemap索引
 * 
 */
@Service
public class SitemapIndexServiceImpl implements SitemapIndexService {

	@Inject
	private SitemapUrlService sitemapUrlService;

	@Transactional(readOnly = true)
	public List<SitemapIndex> generate(SitemapUrl.Type type, int maxSitemapUrlSize) {
		Assert.notNull(type, "notNull");
		Assert.state(maxSitemapUrlSize >= 0, "state");

		List<SitemapIndex> sitemapIndexs = new ArrayList<>();
		Long sitemapUrlSize = sitemapUrlService.count(type);
		for (int i = 0; i < Math.ceil((double) sitemapUrlSize / (double) maxSitemapUrlSize); i++) {
			SitemapIndex sitemapIndex = new SitemapIndex();
			sitemapIndex.setType(type);
			sitemapIndex.setIndex(i);
			sitemapIndexs.add(sitemapIndex);
		}
		return sitemapIndexs;
	}

}