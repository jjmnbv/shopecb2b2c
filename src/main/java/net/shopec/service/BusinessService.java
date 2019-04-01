package net.shopec.service;

import java.math.BigDecimal;
import java.util.List;

import net.shopec.Page;
import net.shopec.Pageable;
import net.shopec.entity.Business;
import net.shopec.entity.BusinessDepositLog;
import net.shopec.security.AuthenticationProvider;

import com.baomidou.mybatisplus.service.IService;

/**
 * Service - 商家
 * 
 */
public interface BusinessService extends IService<Business>, AuthenticationProvider {

	/**
	 * 查找实体对象
	 * 
	 * @param id
	 *            ID
	 * @return 实体对象，若不存在则返回null
	 */
	Business find(Long id);
	
	/**
	 * 查找所有实体对象集合
	 * 
	 * @return 所有实体对象集合
	 */
	List<Business> findAll();
	
	/**
	 * 查找实体对象分页
	 * 
	 * @param pageable
	 *            分页信息
	 * @return 实体对象分页
	 */
	Page<Business> findPage(Pageable pageable);
	
	/**
	 * 判断用户名是否存在
	 * 
	 * @param username
	 *            用户名(忽略大小写)
	 * @return 用户名是否存在
	 */
	boolean usernameExists(String username);

	/**
	 * 更新实体对象
	 * 
	 * @param entity
	 *            实体对象
	 * @return 实体对象
	 */
	int update(Business business);

	/**
	 * 更新实体对象
	 * 
	 * @param entity
	 *            实体对象
	 * @param ignoreProperties
	 *            忽略属性
	 * @return 实体对象
	 */
	int update(Business business, String... ignoreProperties);
	
	/**
	 * 保存实体对象
	 * 
	 * @param entity
	 *            实体对象
	 * @return 实体对象
	 */
	Business save(Business business);
	
	/**
	 * 删除实体对象
	 * 
	 * @param ids
	 *            ID
	 */
	void delete(Long... ids);
	
	/**
	 * 根据用户名查找商家
	 * 
	 * @param username
	 *            用户名(忽略大小写)
	 * @return 商家，若不存在则返回null
	 */
	Business findByUsername(String username);

	/**
	 * 判断E-mail是否存在
	 * 
	 * @param email
	 *            E-mail(忽略大小写)
	 * @return E-mail是否存在
	 */
	boolean emailExists(String email);

	/**
	 * 判断E-mail是否唯一
	 * 
	 * @param id
	 *            ID
	 * @param email
	 *            E-mail(忽略大小写)
	 * @return E-mail是否唯一
	 */
	boolean emailUnique(Long id, String email);

	/**
	 * 根据E-mail查找商家
	 * 
	 * @param email
	 *            E-mail(忽略大小写)
	 * @return 商家，若不存在则返回null
	 */
	Business findByEmail(String email);

	/**
	 * 判断手机是否存在
	 * 
	 * @param mobile
	 *            手机(忽略大小写)
	 * @return 手机是否存在
	 */
	boolean mobileExists(String mobile);

	/**
	 * 判断手机是否唯一
	 * 
	 * @param id
	 *            ID
	 * @param mobile
	 *            手机(忽略大小写)
	 * @return 手机是否唯一
	 */
	boolean mobileUnique(Long id, String mobile);

	/**
	 * 通过名称查找商家
	 * 
	 * @param keyword
	 *            关键词
	 * @param count
	 *            数量
	 * @return 商家
	 */
	List<Business> search(String keyword, Integer count);

	/**
	 * 根据手机查找商家
	 * 
	 * @param mobile
	 *            手机(忽略大小写)
	 * @return 商家，若不存在则返回null
	 */
	Business findByMobile(String mobile);

	/**
	 * 增加余额
	 * 
	 * @param business
	 *            商家
	 * @param amount
	 *            值
	 * @param type
	 *            类型
	 * @param memo
	 *            备注
	 */
	void addBalance(Business business, BigDecimal amount, BusinessDepositLog.Type type, String memo);

	/**
	 * 增加冻结金额
	 * 
	 * @param business
	 *            商家
	 * @param amount
	 *            值
	 */
	void addFrozenFund(Business business, BigDecimal amount);

}