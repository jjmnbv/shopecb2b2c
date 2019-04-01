package net.shopec.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Transient;

import org.apache.commons.codec.digest.DigestUtils;

import com.baomidou.mybatisplus.annotations.TableField;

/**
 * Entity - 管理员
 * 
 */
public class Admin extends User {

	private static final long serialVersionUID = -4000007477538426L;

	
	/**
	 * 用户名
	 */
	private String username;

	/**
	 * 密码
	 */
	@TableField(exist = false)
	private String password;

	/**
	 * 加密密码
	 */
	private String encodedPassword;

	/**
	 * E-mail
	 */
	private String email;

	/**
	 * 姓名
	 */
	private String name;

	/**
	 * 部门
	 */
	private String department;

	/**
	 * 角色
	 */
	@TableField(exist=false)
	private Set<Role> roles = new HashSet<>();

	/**
	 * 获取用户名
	 * 
	 * @return 用户名
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * 设置用户名
	 * 
	 * @param username
	 *            用户名
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * 获取密码
	 * 
	 * @return 密码
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * 设置密码
	 * 
	 * @param password
	 *            密码
	 */
	public void setPassword(String password) {
		this.password = password;
		if (password != null) {
			setEncodedPassword(DigestUtils.md5Hex(password));
		}
	}

	/**
	 * 获取加密密码
	 * 
	 * @return 加密密码
	 */
	public String getEncodedPassword() {
		return encodedPassword;
	}

	/**
	 * 设置加密密码
	 * 
	 * @param encodedPassword
	 *            加密密码
	 */
	public void setEncodedPassword(String encodedPassword) {
		this.encodedPassword = encodedPassword;
	}

	/**
	 * 获取E-mail
	 * 
	 * @return E-mail
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * 设置E-mail
	 * 
	 * @param email
	 *            E-mail
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * 获取姓名
	 * 
	 * @return 姓名
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置姓名
	 * 
	 * @param name
	 *            姓名
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取部门
	 * 
	 * @return 部门
	 */
	public String getDepartment() {
		return department;
	}

	/**
	 * 设置部门
	 * 
	 * @param department
	 *            部门
	 */
	public void setDepartment(String department) {
		this.department = department;
	}

	/**
	 * 获取角色
	 * 
	 * @return 角色
	 */
	public Set<Role> getRoles() {
		return roles;
	}

	/**
	 * 设置角色
	 * 
	 * @param roles
	 *            角色
	 */
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	@Override
	@Transient
	public String getDisplayName() {
		return getUsername();
	}

	@Override
	@Transient
	public Object getPrincipal() {
		return getUsername();
	}

	@Override
	@Transient
	public Object getCredentials() {
		return getPassword();
	}

	@Override
	@Transient
	public boolean isValidCredentials(Object credentials) {
		return credentials != null && DigestUtils.md5Hex(credentials instanceof char[] ? new String((char[]) credentials) : credentials.toString()).equals(getEncodedPassword());
	}

}