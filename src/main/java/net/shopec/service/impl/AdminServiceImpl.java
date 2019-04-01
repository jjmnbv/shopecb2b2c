package net.shopec.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.IdWorker;

import net.shopec.Page;
import net.shopec.Pageable;
import net.shopec.dao.AdminDao;
import net.shopec.dao.AdminRoleDao;
import net.shopec.dao.UserDao;
import net.shopec.entity.Admin;
import net.shopec.entity.AdminRole;
import net.shopec.entity.Role;
import net.shopec.entity.User;
import net.shopec.service.AdminService;

@Service
public class AdminServiceImpl extends ServiceImpl<AdminDao, Admin> implements AdminService {

	@Inject
	private AdminDao adminDao;
	@Inject
	private UserDao userDao;
	@Inject
	private AdminRoleDao adminRoleDao;
	
	@Override
	public Admin find(Long id) {
		return adminDao.find(id);
	}
	
	@Transactional(readOnly = true)
	public Admin getUser(Object principal) {
		Assert.notNull(principal, "notNull");
		Assert.isInstanceOf(String.class, principal);

		return findByUsername((String) principal);
	}

	@Transactional(readOnly = true)
	public Set<String> getPermissions(User user) {
		Assert.notNull(user, "notNull");
		Assert.isInstanceOf(Admin.class, user);

		Admin admin = adminDao.find(user.getId());
		Set<String> result = new HashSet<>();
		if (admin != null && admin.getRoles() != null) {
			for (Role role : admin.getRoles()) {
				if (role.getPermissions() != null) {
					result.addAll(role.getPermissions());
				}
			}
		}
		return result;
	}

	@Transactional(readOnly = true)
	public boolean supports(Class<?> userClass) {
		return userClass != null && Admin.class.isAssignableFrom(userClass);
	}

	@Transactional(readOnly = true)
	public boolean usernameExists(String username) {
		return adminDao.exists("username", StringUtils.lowerCase(username));
	}

	@Transactional(readOnly = true)
	public Admin findByUsername(String username) {
		return adminDao.findByAttribute("username", StringUtils.lowerCase(username));
	}

	@Transactional(readOnly = true)
	public boolean emailExists(String email) {
		return adminDao.exists("email", StringUtils.lowerCase(email));
	}

	@Transactional(readOnly = true)
	public boolean emailUnique(Long id, String email) {
		if (id != null) {
			return !adminDao.unique(id, "email", StringUtils.lowerCase(email));
		} else {
			return !adminDao.exists("email", StringUtils.lowerCase(email));
		}
	}

	@Transactional(readOnly = true)
	public Admin findByEmail(String email) {
		return adminDao.findByAttribute("email", StringUtils.lowerCase(email));
	}

	@Override
	@Transactional
	@CacheEvict(value = "authorization", allEntries = true)
	public Admin save(Admin admin) {
		admin.setId(IdWorker.getId());
		admin.setUsername(StringUtils.lowerCase(admin.getUsername()));
		admin.setEmail(StringUtils.lowerCase(admin.getEmail()));
		userDao.save(admin);
		adminDao.save(admin);
		
		setAdminRole(admin);
		return admin;
	}
	
	@Override
	@Transactional
	@CacheEvict(value = "authorization", allEntries = true)
	public int update(Admin admin) {
		return adminDao.update(admin);
	}

	@Override
	@Transactional
	@CacheEvict(value = "authorization", allEntries = true)
	public int update(Admin admin, String... ignoreProperties) {
		Assert.notNull(admin, "Admin Not Null");
		Assert.isTrue(!admin.isNew(), "Admin is True");

		Admin persistant = adminDao.find(admin.getId());
		if (persistant != null) {
			BeanUtils.copyProperties(admin, persistant, ignoreProperties);
		}
		admin.setEmail(StringUtils.lowerCase(admin.getEmail()));
		setAdminRole(admin);
		return update(persistant);
	}
	
	@Override
	@Transactional
	@CacheEvict(value = "authorization", allEntries = true)
	public void delete(Long id) {
		adminDao.deleteById(id);
		userDao.deleteById(id);
	}

	@Override
	@Transactional
	@CacheEvict(value = "authorization", allEntries = true)
	public void delete(Long... ids) {
		if (ids != null) {
			for (Long id : ids) {
				EntityWrapper<AdminRole> wrapper = new EntityWrapper<AdminRole>();
				wrapper.where("admins_id = {0}", id);
				adminRoleDao.delete(wrapper);
				delete(id);
			}
		}
	}
	
	@Override
	@Transactional(readOnly = true)
	public Page<Admin> findPage(Pageable pageable) {
		EntityWrapper<Admin> wrapper = new EntityWrapper<Admin>();
		// 搜索属性、搜索值
		String searchProperty = pageable.getSearchProperty();
		String searchValue = pageable.getSearchValue();
		if (StringUtils.isNotEmpty(searchProperty) && StringUtils.isNotEmpty(searchValue)) {
			wrapper.like(searchProperty, searchValue);
		}
		com.baomidou.mybatisplus.plugins.Page<Admin> plusPage = selectPage(new com.baomidou.mybatisplus.plugins.Page<Admin>(pageable.getPageNumber(), pageable.getPageSize()), wrapper);
        Page<Admin> page = new Page<Admin>(plusPage.getRecords(), plusPage.getTotal(), pageable);
		return page;
	}

	@Override
	@Transactional(readOnly = true)
	public long count() {
		return adminDao.selectCount(null);
	}

	/**
	 * 设置管理员角色中间表
	 * 
	 */
	private void setAdminRole(Admin admin) {
		EntityWrapper<AdminRole> wrapper = new EntityWrapper<AdminRole>();
		wrapper.where("admins_id = {0}", admin.getId());
		adminRoleDao.delete(wrapper);
		List<AdminRole> adminRoles = new ArrayList<>();
		for (Role role : admin.getRoles()) {
			AdminRole adminRole = new AdminRole();
			adminRole.setAdminsId(admin.getId());
			adminRole.setRolesId(role.getId());
			adminRoles.add(adminRole);
		}
		if (CollectionUtils.isNotEmpty(adminRoles)) {
			adminRoleDao.batchSave(adminRoles);
		}
	}

}
