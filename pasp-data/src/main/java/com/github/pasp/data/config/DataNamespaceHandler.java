package com.github.pasp.data.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class DataNamespaceHandler extends NamespaceHandlerSupport {

	@Override
	public void init() {
		registerBeanDefinitionParser("dataSource", new DatasourceBeanDefinitionParser());
	}

}
