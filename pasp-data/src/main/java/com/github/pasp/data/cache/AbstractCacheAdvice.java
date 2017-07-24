package com.github.pasp.data.cache;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.github.pasp.core.ICache;
import com.github.pasp.core.ICacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.ClassUtils;

abstract class AbstractCacheAdvice implements InitializingBean, BeanFactoryAware {
	private static Logger logger = LoggerFactory.getLogger(AbstractCacheAdvice.class);

	@Autowired
	private ParameterNameDiscoverer discoverer;

	@Autowired(required = false)
	@Qualifier("pasp-cacheManager")
	protected ICacheManager cacheManager;

	private ExpressionParser parser = new SpelExpressionParser();

	private Map<Method, ExprContext> exprCache = new ConcurrentHashMap<Method, ExprContext>(1000);

	@Value("${pasp.cache}")
	private boolean useCache;

	private BeanFactory beanFactory;

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}
	@Override
	public void afterPropertiesSet() throws Exception {
		if (!this.useCache) {
			this.cacheManager = null;
			return;
		}
		if (this.cacheManager == null) {
			this.cacheManager = beanFactory.getBean(ICacheManager.class);
			logger.debug("No bean named 'pasp-cacheManager' found, Use default: {}", this.cacheManager.getClass());
		}
	}

	protected ICache getCache(String name) {
		if (cacheManager != null) {
			return cacheManager.getCache(name);
		}
		return null;
	}

	protected Object getCacheKey(String key, Method method, Object[] arguments, Object root) {
		if (!exprCache.containsKey(method)) {
			Expression expr = parser.parseExpression(key);
			String[] parameterNames = getParameterNames(method, AopUtils.getTargetClass(root));
			exprCache.put(method, new ExprContext(expr, parameterNames));
		}
		ExprContext exprContext = exprCache.get(method);
		StandardEvaluationContext context = new StandardEvaluationContext(root);
		String[] argNames = exprContext.getArgNames();
		if (argNames != null) {
			for (int i = 0; i < argNames.length; i++) {
				context.setVariable(argNames[i], arguments[i]);
			}
		}
		return exprContext.getExpr().getValue(context);
	}

	private String[] getParameterNames(Method method, Class<?> targetClass) {
		return this.discoverer.getParameterNames(getOriginalMethod(method, targetClass));
	}

	public <T extends Annotation> T getMethodAnnotation(Method method, Class<?> targetClass, Class<T> annoClass) {
		Method m = getOriginalMethod(method, targetClass);
		T anno = m.getAnnotation(annoClass);
		if (anno != null) {
			return anno;
		}
		return method.getAnnotation(annoClass);
	}

	protected Method getOriginalMethod(Method method, Class<?> targetClass) {
		// The method may be on an interface, but we need attributes from the
		// target class.
		// If the target class is null, the method will be unchanged.
		Method specificMethod = ClassUtils.getMostSpecificMethod(method, targetClass);
		// If we are dealing with method with generic parameters, find the
		// original method.
		return BridgeMethodResolver.findBridgedMethod(specificMethod);
	}

	class ExprContext {
		private Expression expr;
		private String[] argNames;

		public ExprContext(Expression expr, String[] argNames) {
			super();
			this.expr = expr;
			this.argNames = argNames;
		}

		public Expression getExpr() {
			return expr;
		}

		public void setExpr(Expression expr) {
			this.expr = expr;
		}

		public String[] getArgNames() {
			return argNames;
		}

		public void setArgNames(String[] argNames) {
			this.argNames = argNames;
		}

	}
}
