/**
 * 
 */
package com.github.pasp.core;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 缓存注解
 * <p>
 * 标注在dao方法上，对该方法增加缓存功能<br>
 * 
 * @author xiongkw
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UseCache {
	/**
	 * 缓存名称
	 * <p>
	 * 指定缓存名，默认为当前Dao的实体类全名
	 * </p>
	 * 
	 * @return
	 */
	String value() default "";

	/**
	 * 缓存key
	 * <p>
	 * 指定缓存对象key，支持Spel表达式，不指定则采用默认缓存处理逻辑
	 * </p>
	 * 
	 * @return
	 */
	String key() default "";
}
