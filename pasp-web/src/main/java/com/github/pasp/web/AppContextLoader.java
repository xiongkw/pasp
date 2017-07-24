package com.github.pasp.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.ConfigurableWebEnvironment;
import org.springframework.web.context.ContextLoaderListener;

import javax.servlet.ServletContext;

public class AppContextLoader extends ContextLoaderListener {

	private static final String DEFAULT_CONFIG_PATTERN = "classpath*:conf/spring/*.spring.xml";

	private static Logger logger = LoggerFactory.getLogger(AppContextLoader.class);

	protected void configureAndRefreshWebApplicationContext(ConfigurableWebApplicationContext wac, ServletContext sc) {
		if (ObjectUtils.identityToString(wac).equals(wac.getId())) {
			// The application context id is still set to its original default
			// value
			// -> assign a more useful id based on available information
			String idParam = sc.getInitParameter(CONTEXT_ID_PARAM);
			if (idParam != null) {
				wac.setId(idParam);
			} else {
				// Generate default id...
				wac.setId(ConfigurableWebApplicationContext.APPLICATION_CONTEXT_ID_PREFIX
						+ ObjectUtils.getDisplayString(sc.getContextPath()));
			}
		}

		wac.setServletContext(sc);
		String configLocationParam = sc.getInitParameter(CONFIG_LOCATION_PARAM);
		if (configLocationParam != null) {
			wac.setConfigLocation(configLocationParam);
		} else {
			// 增加默认pasp *.spring.xml逻辑
			logger.debug("No config location pattern found with servlet context param: {}, use pasp default: {}",
					CONFIG_LOCATION_PARAM, DEFAULT_CONFIG_PATTERN);
			wac.setConfigLocation(DEFAULT_CONFIG_PATTERN);
		}

		// The wac environment's #initPropertySources will be called in any case
		// when the context
		// is refreshed; do it eagerly here to ensure servlet property sources
		// are in place for
		// use in any post-processing or initialization that occurs below prior
		// to #refresh
		ConfigurableEnvironment env = wac.getEnvironment();
		if (env instanceof ConfigurableWebEnvironment) {
			((ConfigurableWebEnvironment) env).initPropertySources(sc, null);
		}

		customizeContext(sc, wac);
		wac.refresh();
	}

}
