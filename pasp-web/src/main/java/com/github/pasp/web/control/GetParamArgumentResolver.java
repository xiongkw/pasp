package com.github.pasp.web.control;

import com.github.pasp.core.IObjectMapper;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.github.pasp.web.JSONParam;

public class GetParamArgumentResolver implements HandlerMethodArgumentResolver {
	private IObjectMapper objectMapper;

	public void setObjectMapper(IObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(JSONParam.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		String parameterName = parameter.getParameterName();
		String value = webRequest.getParameter(parameterName);
		return objectMapper.mapper(value, parameter.getParameterType());
	}

}
