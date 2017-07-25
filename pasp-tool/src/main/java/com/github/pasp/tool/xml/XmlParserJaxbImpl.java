package com.github.pasp.tool.xml;

import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlParserJaxbImpl implements IXmlParser {
	private static Logger logger = LoggerFactory.getLogger(XmlParserJaxbImpl.class);

	private Unmarshaller unmarshaller;

	public XmlParserJaxbImpl(Class<?>... classes) {
		try {
			JAXBContext context = JAXBContext.newInstance(classes);
			unmarshaller = context.createUnmarshaller();
		} catch (JAXBException e) {
			logger.error("Init Config parser failed!", e);
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T parse(InputStream in, Class<T> cl) {
		try {
			return (T) unmarshaller.unmarshal(in);
		} catch (JAXBException e) {
			logger.error("Parse config failed!", e);
			return null;
		}
	}
}
