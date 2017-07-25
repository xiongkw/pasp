package com.github.pasp.tool.coder.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import com.github.pasp.tool.coder.IConfigLoader;
import com.github.pasp.tool.xml.IXmlParser;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.pasp.tool.coder.config.Config;
import com.github.pasp.tool.xml.XmlParserJaxbImpl;

public class ConfigLoader implements IConfigLoader {
	private static Logger logger = LoggerFactory.getLogger(ConfigLoader.class);
	private IXmlParser parser = new XmlParserJaxbImpl(Config.class);
	private String userdir;

	public ConfigLoader(String userdir) {
		this.userdir = userdir;
	}

	@Override
	public Config load(String path) throws FileNotFoundException {
		String relativePath = userdir + "/" + path;
		File file = new File(relativePath);
		if (!file.exists()) {
			file = new File(path);
			if (!file.exists()) {
				throw new FileNotFoundException();
			}
		}
		logger.debug("加载配置文件:{}", path);
		InputStream in = null;
		try {
			in = new FileInputStream(file);
			return parser.parse(in, Config.class);
		} finally {
			IOUtils.closeQuietly(in);
		}
	}

}
