package com.github.pasp.tool.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;

public class BeanUtils {
	public static void copyProperties(Map<String, Object> map, Object obj) throws Exception {
		if (obj == null) {
			return;
		}
		BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		for (PropertyDescriptor property : propertyDescriptors) {
			String key = property.getName();
			if (key.equals("class")) {
				continue;
			}
			Method getter = property.getReadMethod();
			Object value = getter.invoke(obj);
			map.put(key, value);
		}
	}
	
}
