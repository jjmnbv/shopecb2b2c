package net.shopec.restful;

import com.alibaba.fastjson.JSON;

/**
 * 统一API响应结果封装
 */
public class Result<T> {
	/**
	 * 代码
	 */
	private int code;

	/**
	 * 消息
	 */
	private String message;

	/**
	 * 数据
	 */
	private T data;

	public Result<T> setCode(Code resultCode) {
		this.code = resultCode.code();
		return this;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public Result<T> setMessage(String message) {
		this.message = message;
		return this;
	}

	public T getData() {
		return data;
	}

	public Result<T> setData(T data) {
		this.data = data;
		return this;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
}
