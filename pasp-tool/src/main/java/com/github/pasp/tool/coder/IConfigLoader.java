package com.github.pasp.tool.coder;

import java.io.FileNotFoundException;

import com.github.pasp.tool.coder.config.Config;

public interface IConfigLoader {
	
	Config load(String path) throws FileNotFoundException;
	
}
