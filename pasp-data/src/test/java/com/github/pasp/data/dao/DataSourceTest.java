package com.github.pasp.data.dao;

import javax.annotation.Resource;

import com.github.pasp.data.api.IRoleDao;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.druid.pool.DruidDataSource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:conf/spring/pasp-data.spring.xml",
		"classpath*:conf/spring/pasp-data-test-datasource.spring.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class DataSourceTest {
	
	@Resource(name="roleDao")
	private IRoleDao roleDao;
	
	@Resource(name="dsRoleDao")
	private IRoleDao dsRoleDao;
	
	@SuppressWarnings("rawtypes")
	@Test
	public void test(){
		BaseDao d1 = (BaseDao) roleDao;
		BaseDao d2 = (BaseDao) dsRoleDao;
		Assert.assertNotEquals(d1, d2);
		
		JdbcTemplate j1 = d1.getJdbcTemplate();
		JdbcTemplate j2 = d2.getJdbcTemplate();
		Assert.assertNotEquals(j1, j2);
		DruidDataSource ds1 = (DruidDataSource) j1.getDataSource();
		DruidDataSource ds2 = (DruidDataSource) j2.getDataSource();
		Assert.assertNotEquals(ds1, ds2);
		
		Assert.assertNotEquals(ds1.getRawJdbcUrl(),ds2.getRawJdbcUrl());
	}

}
