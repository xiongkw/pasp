package com.github.pasp.context.weave;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class WeaveConfigParser {
	private static final String CONF_PASP_WEAVE_XML = "/conf/pasp-weave.xml";
	private static Logger logger = LoggerFactory.getLogger(WeaveConfigParser.class);
	private static WeaveConfig config;

	static {
		readConfig();
	}

	private static void readConfig() {
		InputStream in = null;
		try {
			in = WeaveConfigParser.class.getResourceAsStream(CONF_PASP_WEAVE_XML);
			JAXBContext context = JAXBContext.newInstance(WeaveConfig.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			config = (WeaveConfig) unmarshaller.unmarshal(in);
		} catch (JAXBException e) {
			logger.warn("Parse weave config failed!", e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
		}
	}
	
	public static WeaveConfig getConfig(){
		return config;
	}
	
}
