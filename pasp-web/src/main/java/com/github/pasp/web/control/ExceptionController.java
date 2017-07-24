package com.github.pasp.web.control;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.github.pasp.core.exception.CodedException;
import com.github.pasp.web.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * <p>
 * 异常处理controller<br>
 * 配合CodedException使用，实现国际化的统一异常处理
 * </p>
 * 
 * @author xiongkw
 *
 */
public class ExceptionController {
	private static final String UNKNOWN_ERROR = "Unknown Error";

	private static Logger logger = LoggerFactory.getLogger(ExceptionController.class);

	@Resource(name = "pasp-web-messageSource")
	private MessageSource messageSource;

	@ExceptionHandler
	@ResponseBody
	public Response onException(Exception e, HttpServletRequest request) {
		logger.error("Request exception!", e);
		if (e instanceof CodedException) {
			CodedException ex = (CodedException) e;
			String code = ex.getCode();
			Object[] args = ex.getArgs();
			String message = messageSource.getMessage(code, args, UNKNOWN_ERROR, request.getLocale());
			return new Response(code, message);
		}
		return new Response(HttpStatus.INTERNAL_SERVER_ERROR.toString(), UNKNOWN_ERROR);
	}
}
