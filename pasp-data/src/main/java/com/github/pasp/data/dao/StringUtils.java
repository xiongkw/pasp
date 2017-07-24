package com.github.pasp.data.dao;

class StringUtils {
	static boolean equals(String s1, String s2) {
		return s1 == null ? s2 == null : s1.equals(s2);
	}
	
	static boolean isEmpty(Object str) {
		return (str == null || "".equals(str));
	}
	
	static boolean isNotEmpty(CharSequence cs) {
        return !StringUtils.isEmpty(cs);
    }
	
	static String trim(String str) {
        return str == null ? null : str.trim();
    }
}
