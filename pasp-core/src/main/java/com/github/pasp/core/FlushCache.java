package com.github.pasp.core;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 缓存更新注解
 * <p>
 * 标注在dao方法上，用于在方法调用成功返回后更新缓存(异常时不会执行)<br>
 * 
 * </p>
 * 
 * @author xiongkw
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FlushCache {

	/**
	 * 缓存名称
	 * <p>
	 * 指定需要清空的缓存名，可以指定多个，默认为当前Dao的实体类全名
	 * </p>
	 * 
	 * @return
	 */
	String[] value() default {};

	/**
	 * 缓存key
	 * <p>
	 * 指定需要清除的对象key，支持Spel表达式，不指定则清除当前缓存中所有对象
	 * </p>
	 * 
	 * @return
	 */
	String key() default "";
}
