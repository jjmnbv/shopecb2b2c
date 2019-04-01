package net.shopec.restful;

/**
 * 响应结果生成工具
 */
public class ResultUtils<T> {
	
	private static final String DEFAULT_SUCCESS_MESSAGE = "SUCCESS";

	public static <T> Result<T> success() {
		return new Result<T>().setCode(Code.SUCCESS).setMessage(DEFAULT_SUCCESS_MESSAGE);
	}

	public static <T> Result<T> success(T data) {
		return new Result<T>().setCode(Code.SUCCESS).setMessage(DEFAULT_SUCCESS_MESSAGE).setData(data);
	}

	public static <T> Result<T> fail(String message) {
		return new Result<T>().setCode(Code.FAIL).setMessage(message);
	}
	
}
