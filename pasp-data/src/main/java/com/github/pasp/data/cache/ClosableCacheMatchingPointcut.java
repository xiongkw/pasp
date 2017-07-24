package com.github.pasp.data.cache;

import java.io.Serializable;
import java.lang.annotation.Annotation;

import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.annotation.AnnotationMethodMatcher;

public class ClosableCacheMatchingPointcut implements Pointcut {

	private static final String STAT_ON = "on";

	private boolean cacheOn;

	private AnnotationMethodMatcher methodMatcher;

	public void setCacheOn(String cacheOn) {
		this.cacheOn = STAT_ON.equalsIgnoreCase(cacheOn);
	}

	@Override
	public ClassFilter getClassFilter() {
		if (cacheOn) {
			return ClassFilter.TRUE;
		}
		return NoneClassFilter.INSTANCE;
	}

	public void setMethodAnnotationType(Class<? extends Annotation> methodAnnotationType) {
		this.methodMatcher = new AnnotationMethodMatcher(methodAnnotationType);
	}

	@Override
	public MethodMatcher getMethodMatcher() {
		return methodMatcher;
	}

	static class NoneClassFilter implements ClassFilter, Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public static final NoneClassFilter INSTANCE = new NoneClassFilter();

		/**
		 * Enforce Singleton pattern.
		 */
		private NoneClassFilter() {
		}

		@Override
		public boolean matches(Class<?> clazz) {
			return false;
		}

		/**
		 * Required to support serialization. Replaces with canonical instance
		 * on deserialization, protecting Singleton pattern. Alternative to
		 * overriding {@code equals()}.
		 */
		private Object readResolve() {
			return INSTANCE;
		}

		@Override
		public String toString() {
			return "ClassFilter.NONE";
		}

	}
}
