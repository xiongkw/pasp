package com.github.pasp.data.config;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.w3c.dom.Element;

import com.alibaba.druid.pool.DruidDataSource;

public class DatasourceBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

	private static final String ATTR_DSN = "dsn";

	private static final String PROP_URL = "url";
	private static final String P_JDBC_URL = "${${dsn}.jdbc.url}";
	private static final String PROP_USERNAME = "username";
	private static final String P_JDBC_USERNAME = "${${dsn}.jdbc.username}";
	private static final String PROP_PASSWORD = "password";
	private static final String P_JDBC_PASSWORD = "${${dsn}.jdbc.password}";
	private static final String PROP_INITIAL_SIZE = "initialSize";
	private static final String P_JDBC_INITIAL_SIZE = "${${dsn}.jdbc.initialSize:1}";
	private static final String PROP_MIN_IDLE = "minIdle";
	private static final String P_JDBC_MIN_IDLE = "${${dsn}.jdbc.minIdle:1}";
	private static final String PROP_MAX_ACTIVE = "maxActive";
	private static final String P_JDBC_MAX_ACTIVE = "${${dsn}.jdbc.maxActive:300}";
	private static final String PROP_MAX_WAIT = "maxWait";
	private static final String P_JDBC_MAX_WAIT = "${${dsn}.jdbc.maxWait:60000}";
	private static final String PROP_TIME_BETWEEN_EVICTION_RUNS_MILLIS = "timeBetweenEvictionRunsMillis";
	private static final String P_JDBC_TIME_BETWEEN_EVICTION_RUNS_MILLIS = "${${dsn}.jdbc.timeBetweenEvictionRunsMillis:60000}";
	private static final String PROP_MIN_EVICTABLE_IDLE_TIME_MILLIS = "minEvictableIdleTimeMillis";
	private static final String P_JDBC_MIN_EVICTABLE_IDLE_TIME_MILLIS = "${${dsn}.jdbc.minEvictableIdleTimeMillis:300000}";
	private static final String PROP_VALIDATION_QUERY = "validationQuery";
	private static final String P_JDBC_VALIDATION_QUERY = "${${dsn}.jdbc.validationQuery:SELECT 'x'}";
	private static final String PROP_TEST_WHILE_IDLE = "testWhileIdle";
	private static final String P_JDBC_TEST_WHILE_IDLE = "${${dsn}.jdbc.testWhileIdle:true}";
	private static final String PROP_TEST_ON_BORROW = "testOnBorrow";
	private static final String P_JDBC_TEST_ON_BORROW = "${${dsn}.jdbc.testOnBorrow:false}";
	private static final String PROP_TEST_ON_RETURN = "testOnReturn";
	private static final String P_JDBC_TEST_ON_RETURN = "${${dsn}.jdbc.testOnReturn:false}";
	private static final String PROP_POOL_PREPARED_STATEMENTS = "poolPreparedStatements";
	private static final String P_JDBC_POOL_PREPARED_STATEMENTS = "${${dsn}.jdbc.poolPreparedStatements:true}";
	private static final String PROP_MAX_POOL_PREPARED_STATEMENT_PER_CONNECTION_SIZE = "maxPoolPreparedStatementPerConnectionSize";
	private static final String P_JDBC_MAX_POOL_PREPARED_STATEMENT_PER_CONNECTION_SIZE = "${${dsn}.jdbc.maxPoolPreparedStatementPerConnectionSize:200}";
	private static final String PROP_REMOVE_ABANDONED = "removeAbandoned";
	private static final String P_JDBC_REMOVE_ABANDONED = "${${dsn}.jdbc.removeAbandoned:true}";
	private static final String PROP_REMOVE_ABANDONED_TIMEOUT_MILLIS = "removeAbandonedTimeoutMillis";
	private static final String P_JDBC_REMOVE_ABANDONED_TIMEOUT_MILLIS = "${${dsn}.jdbc.removeAbandonedTimeoutMillis:1800000}";
	private static final String PROP_RESET_STAT_ENABLE = "resetStatEnable";
	private static final String P_JDBC_RESET_STAT_ENABLE = "${${dsn}.jdbc.resetStatEnable:false}";

	@Override
	protected void doParse(Element element, BeanDefinitionBuilder builder) {
		String dsn = element.getAttribute(ATTR_DSN);

		builder.addPropertyValue(PROP_URL, formatDsn(P_JDBC_URL, dsn));
		builder.addPropertyValue(PROP_USERNAME, formatDsn(P_JDBC_USERNAME, dsn));
		builder.addPropertyValue(PROP_PASSWORD, formatDsn(P_JDBC_PASSWORD, dsn));

		builder.addPropertyValue(PROP_INITIAL_SIZE, formatDsn(P_JDBC_INITIAL_SIZE, dsn));
		builder.addPropertyValue(PROP_MIN_IDLE, formatDsn(P_JDBC_MIN_IDLE, dsn));
		builder.addPropertyValue(PROP_MAX_ACTIVE, formatDsn(P_JDBC_MAX_ACTIVE, dsn));

		builder.addPropertyValue(PROP_MAX_WAIT, formatDsn(P_JDBC_MAX_WAIT, dsn));
		builder.addPropertyValue(PROP_TIME_BETWEEN_EVICTION_RUNS_MILLIS,
				formatDsn(P_JDBC_TIME_BETWEEN_EVICTION_RUNS_MILLIS, dsn));
		builder.addPropertyValue(PROP_MIN_EVICTABLE_IDLE_TIME_MILLIS,
				formatDsn(P_JDBC_MIN_EVICTABLE_IDLE_TIME_MILLIS, dsn));
		builder.addPropertyValue(PROP_VALIDATION_QUERY, formatDsn(P_JDBC_VALIDATION_QUERY, dsn));

		builder.addPropertyValue(PROP_TEST_WHILE_IDLE, formatDsn(P_JDBC_TEST_WHILE_IDLE, dsn));
		builder.addPropertyValue(PROP_TEST_ON_BORROW, formatDsn(P_JDBC_TEST_ON_BORROW, dsn));
		builder.addPropertyValue(PROP_TEST_ON_RETURN, formatDsn(P_JDBC_TEST_ON_RETURN, dsn));
		builder.addPropertyValue(PROP_POOL_PREPARED_STATEMENTS, formatDsn(P_JDBC_POOL_PREPARED_STATEMENTS, dsn));

		builder.addPropertyValue(PROP_MAX_POOL_PREPARED_STATEMENT_PER_CONNECTION_SIZE,
				formatDsn(P_JDBC_MAX_POOL_PREPARED_STATEMENT_PER_CONNECTION_SIZE, dsn));
		builder.addPropertyValue(PROP_REMOVE_ABANDONED, formatDsn(P_JDBC_REMOVE_ABANDONED, dsn));
		builder.addPropertyValue(PROP_REMOVE_ABANDONED_TIMEOUT_MILLIS,
				formatDsn(P_JDBC_REMOVE_ABANDONED_TIMEOUT_MILLIS, dsn));
		builder.addPropertyValue(PROP_RESET_STAT_ENABLE, formatDsn(P_JDBC_RESET_STAT_ENABLE, dsn));

	}

	private String formatDsn(String pattern, String dsn) {
		return pattern.replace("${dsn}", dsn);
	}

	@Override
	protected Class<?> getBeanClass(Element element) {
		return DruidDataSource.class;
	}

}
