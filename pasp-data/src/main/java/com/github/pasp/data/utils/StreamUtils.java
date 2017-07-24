package com.github.pasp.data.utils;

import java.io.Closeable;
import java.io.IOException;

public class StreamUtils {
	
	public static void closeQuietly(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (IOException e) {
			}
		}
	}
	
}
