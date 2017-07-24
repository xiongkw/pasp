package com.github.pasp.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Pasp {
	private static Properties props;

	static {
		InputStream in = null;
		try {
			in = Pasp.class.getResourceAsStream("/conf/pasp.properties");
			props = new Properties();
			props.load(in);
		} catch (IOException e) {
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
		}
	}
	
	public static String getProperty(String name){
		return props.getProperty(name);
	}
}
