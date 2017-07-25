package com.github.pasp.tool.coder.impl;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.pasp.tool.coder.ISourceStorer;

public class SourceStorer implements ISourceStorer {
	private static Logger logger = LoggerFactory.getLogger(SourceStorer.class);

	private String dir;

	public SourceStorer(String dir) {
		super();
		this.dir = dir;
		if (!this.dir.endsWith("/")) {
			this.dir += "/";
		}
	}

	@Override
	public void store(String content, String name) {
		try {
			File file = tryToCreateFile(dir + name);
			FileUtils.writeStringToFile(file, content, "UTF-8");
		} catch (IOException e) {
			logger.warn("Store code {} failed!", name, e);
		}
	}

	private File tryToCreateFile(String path) throws IOException {
		File file = new File(path);
		if (!file.exists()) {
			file.getParentFile().mkdirs();
			file.createNewFile();
		}
		return file;
	}
}
