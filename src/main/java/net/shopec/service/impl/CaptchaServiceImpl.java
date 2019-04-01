package net.shopec.service.impl;

import java.awt.image.BufferedImage;

import javax.inject.Inject;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.shopec.service.CaptchaService;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.google.code.kaptcha.Producer;

/**
 * Service - 验证码
 * 
 */
@Service
public class CaptchaServiceImpl implements CaptchaService {

	/**
	 * "验证码"缓存名称
	 */
	private static final String CAPTCHA_CACHE_NAME = "captcha";

	@Inject
	private Producer captchaProducer;
	@Inject
	private CacheManager cacheManager;

	public BufferedImage createImage(String captchaId) {
		Assert.hasText(captchaId, "hasText");

		String captcha = captchaProducer.createText();
		Ehcache cache = cacheManager.getEhcache(CAPTCHA_CACHE_NAME);
		cache.put(new Element(captchaId, captcha));
		return captchaProducer.createImage(captcha);
	}

	public boolean isValid(String captchaId, String captcha) {
		if (StringUtils.isEmpty(captchaId) || StringUtils.isEmpty(captcha)) {
			return false;
		}

		Ehcache cache = cacheManager.getEhcache(CAPTCHA_CACHE_NAME);
		Element element = cache.get(captchaId);
		if (element != null) {
			String value = (String) element.getObjectValue();
			cache.remove(captchaId);
			return StringUtils.equalsIgnoreCase(captcha, value);
		}
		return false;
	}

}