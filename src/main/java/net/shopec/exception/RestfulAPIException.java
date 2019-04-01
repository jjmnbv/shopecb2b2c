package net.shopec.exception;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.alibaba.fastjson.JSON;

import net.shopec.restful.Result;
import net.shopec.restful.Code;

public class RestfulAPIException implements HandlerExceptionResolver {
	private final Logger logger = LoggerFactory.getLogger(RestfulAPIException.class);

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) {
		Result<?> result = new Result<>();
		if (e instanceof ServiceException) {
			result.setCode(Code.FAIL).setMessage(e.getMessage());
			logger.info(e.getMessage());
		} else if (e instanceof NoHandlerFoundException) {
			result.setCode(Code.NOT_FOUND).setMessage("接口 [" + request.getRequestURI() + "] 不存在");
		} else if (e instanceof ServletException) {
			result.setCode(Code.FAIL).setMessage(e.getMessage());
		} else {
			result.setCode(Code.INTERNAL_SERVER_ERROR).setMessage("接口 [" + request.getRequestURI() + "] 内部错误，请联系管理员");
			String message;
			if (handler instanceof HandlerMethod) {
				HandlerMethod handlerMethod = (HandlerMethod) handler;
				message = String.format("接口 [%s] 出现异常，方法：%s.%s，异常摘要：%s", request.getRequestURI(), handlerMethod.getBean().getClass().getName(), handlerMethod.getMethod().getName(), e.getMessage());
			} else {
				message = e.getMessage();
			}
			logger.error(message, e);
		}
		responseResult(response, result);
		return new ModelAndView();
	}

	private void responseResult(HttpServletResponse response, Result<?> result) {
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Content-type", "application/json;charset=UTF-8");
		response.setStatus(200);
		try {
			response.getWriter().write(JSON.toJSONString(result));
		} catch (IOException ex) {
			logger.error(ex.getMessage());
		}
	}
	
}
