package com.github.pasp.context;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * <p>
 * AppContext，提供访问spring容器的静态方法
 * </p>
 * 注意：需要在spring容器初始化完成后才能使用
 * 
 * @author xiongkw
 *
 */
public class AppContext implements ApplicationContextAware {
	private static ApplicationContext context;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		AppContext.context = applicationContext;
	}

	public static ApplicationContext getContext() {
		return AppContext.context;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getBean(final String beanId) {
		return (T) AppContext.context.getBean(beanId);
	}

	public static boolean containsBean(final String beanId) {
		return AppContext.context.containsBean(beanId);
	}

	@SuppressWarnings("unchecked")
	public static <T> T getBean(final String beanId, Object... args) {
		return (T) AppContext.context.getBean(beanId, args);
	}

	public static <T> T getBean(Class<T> clazz) {
		return AppContext.context.getBean(clazz);
	}

}
