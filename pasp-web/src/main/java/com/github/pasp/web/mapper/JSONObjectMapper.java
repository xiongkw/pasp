package com.github.pasp.web.mapper;

import com.github.pasp.core.IObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONObjectMapper implements IObjectMapper {
	private static Logger logger = LoggerFactory.getLogger(JSONObjectMapper.class);

	@Override
	public <T> T mapper(String content, Class<T> cl) {
		if (content == null || content.length() == 0) {
			return null;
		}
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(content, cl);
		} catch (Exception e) {
			logger.warn("Parse dto with content: {} and class: {} failed!", content, cl, e);
			return null;
		}
	}

}
