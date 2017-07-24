package com.github.pasp.data.dao;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import org.junit.Test;

import com.github.pasp.data.Sql;
import com.github.pasp.data.entity.User;

public class SqlBuilderImplTest {
	private SqlBuilderImpl builder = new SqlBuilderImpl();
	private EntityInfoParser parser = new EntityInfoParser();

	@Test
	public void testBuildBatchDeleteByPrimaryKeySql() {
		EntityInfo info = parser.parse(User.class);
		ArrayList<User> list = new ArrayList<User>();
		User user1 = new User();
		user1.setId(1L);
		user1.setRegion(1L);
		list.add(user1);

		User user2 = new User();
		user2.setId(2L);
		user2.setRegion(2L);
		list.add(user2);

		Sql sql = builder.buildBatchDeleteByPrimaryKeySql(list, info);
		assertEquals("DELETE FROM T_USER WHERE USER_ID=?", sql.getStatement());
		List<Object[]> listArgs = sql.getListArgs();
		assertNotNull(listArgs);
		assertTrue(listArgs.size() == 2);
	}

	@Test
	public void testBuildBatchInsertSql() {
		EntityInfo info = parser.parse(User.class);
		ArrayList<User> list = new ArrayList<User>();
		User user1 = new User();
		user1.setId(1L);
		user1.setRegion(1L);
		list.add(user1);

		User user2 = new User();
		user2.setId(2L);
		user2.setRegion(2L);
		list.add(user2);

		Sql sql = builder.buildBatchInsertSql(list, info);
		assertEquals(
				"INSERT INTO T_USER(USER_ID, USER_NAME, GENDER, AGE, IMAGE, REMARK, LOCK, LAST_UPDATE, REGION) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
				sql.getStatement());
		List<Object[]> listArgs = sql.getListArgs();
		assertNotNull(listArgs);
		assertTrue(listArgs.size() == 2);
	}

	@Test
	public void testBuildBatchUpdateByPrimaryKeySql() {
		EntityInfo info = parser.parse(User.class);
		ArrayList<User> list = new ArrayList<User>();
		User user1 = new User();
		user1.setId(1L);
		user1.setRegion(1L);
		user1.setLock(1L);
		list.add(user1);

		User user2 = new User();
		user2.setId(2L);
		user2.setRegion(2L);
		user2.setLock(1L);
		list.add(user2);

		Sql sql = builder.buildBatchUpdateByPrimaryKeySql(list, info);
		assertEquals(
				"UPDATE T_USER SET USER_NAME=?, GENDER=?, AGE=?, IMAGE=?, REMARK=?, LOCK=(LOCK+1), LAST_UPDATE=?, REGION=? WHERE USER_ID=? AND LOCK=?",
				sql.getStatement());
		List<Object[]> listArgs = sql.getListArgs();
		assertNotNull(listArgs);
		assertTrue(listArgs.size() == 2);
	}

	@Test
	public void testBuildCountByExampleSql() {
		EntityInfo info = parser.parse(User.class);
		User user = new User();
		user.setAge(30);
		user.setGender("1");
		user.setUserName("Tom");
		Sql sql = builder.buildCountByExampleSql(user, info);
		assertEquals("SELECT COUNT(1) FROM T_USER WHERE USER_NAME=? AND GENDER=? AND AGE=?", sql.getStatement());
		assertArrayEquals(new Object[] { "Tom", "1", 30 }, sql.getArgs());
	}

	@Test
	public void testBuildDeleteByExampleSql() {
		EntityInfo info = parser.parse(User.class);
		User user = new User();
		user.setAge(30);
		user.setGender("1");
		user.setUserName("Tom");
		Sql sql = builder.buildDeleteByExampleSql(user, info);
		assertEquals("DELETE FROM T_USER WHERE USER_NAME=? AND GENDER=? AND AGE=?", sql.getStatement());
		assertArrayEquals(new Object[] { "Tom", "1", 30 }, sql.getArgs());
	}

	@Test
	public void testBuildDeleteByPrimaryKeySql() {
		EntityInfo info = parser.parse(User.class);
		User user = new User();
		user.setUserId(1L);
		user.setAge(30);
		user.setGender("1");
		user.setUserName("Tom");
		user.setRegion(1L);
		user.setLock(1L);
		Sql sql = builder.buildDeleteByPrimaryKeySql(user, info);
		assertEquals("DELETE FROM T_USER WHERE USER_ID=? AND LOCK=?", sql.getStatement());
		assertArrayEquals(new Object[] { 1L, 1L }, sql.getArgs());
	}

	@Test
	public void testBuildInsertSql() {
		EntityInfo info = parser.parse(User.class);
		User user = new User();
		user.setAge(30);
		user.setGender("1");
		user.setUserName("Tom");
		user.setRegion(1L);
		Sql sql = builder.buildInsertSql(user, info);
		assertEquals(
				"INSERT INTO T_USER(USER_ID, USER_NAME, GENDER, AGE, IMAGE, REMARK, LOCK, LAST_UPDATE, REGION) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
				sql.getStatement());
		assertArrayEquals(new Object[] { null, "Tom", "1", 30, null, null, null, null, 1L }, sql.getArgs());
	}

	@Test
	public void testBuildSelectByExampleSql() {
		EntityInfo info = parser.parse(User.class);
		User user = new User();
		user.setAge(30);
		user.setGender("1");
		user.setUserName("Tom");
		Sql sql = builder.buildSelectByExampleSql(user, info);
		assertEquals(
				"SELECT USER_ID, USER_NAME, GENDER, AGE, IMAGE, REMARK, LOCK, LAST_UPDATE, REGION FROM T_USER WHERE USER_NAME=? AND GENDER=? AND AGE=?",
				sql.getStatement());
		assertArrayEquals(new Object[] { "Tom", "1", 30, }, sql.getArgs());
	}

	@Test
	public void testBuildSelectByPrimaryKeySql() {
		EntityInfo info = parser.parse(User.class);
		User user = new User();
		user.setUserId(1L);
		Sql sql = builder.buildSelectByPrimaryKeySql(user, info);
		assertEquals(
				"SELECT USER_ID, USER_NAME, GENDER, AGE, IMAGE, REMARK, LOCK, LAST_UPDATE, REGION FROM T_USER WHERE USER_ID=?",
				sql.getStatement());
		assertArrayEquals(new Object[] { 1L }, sql.getArgs());
	}

	@Test
	public void testBuildUpdateByExampleSql() {
		EntityInfo info = parser.parse(User.class);
		User user = new User();
		user.setAge(30);
		user.setGender("1");
		user.setUserName("Tom");
		user.setRegion(1L);
		User example = new User();
		example.setGender("0");
		example.setAge(25);
		Sql sql = builder.buildUpdateByExampleSql(user, example, info);
		assertEquals(
				"UPDATE T_USER SET USER_NAME=?, GENDER=?, AGE=?, IMAGE=?, REMARK=?, LOCK=(LOCK+1), LAST_UPDATE=?, REGION=? WHERE GENDER=? AND AGE=?",
				sql.getStatement());
		assertArrayEquals(new Object[] { "Tom", "1", 30, null, null, null, 1L, "0", 25 }, sql.getArgs());
	}

	@Test
	public void testBuildUpdateByPrimaryKeySql() {
		EntityInfo info = parser.parse(User.class);
		User user = new User();
		user.setUserId(1L);
		user.setAge(30);
		user.setGender("1");
		user.setUserName("Tom");
		user.setRegion(1L);
		user.setLock(1L);
		Sql sql = builder.buildUpdateByPrimaryKeySql(user, info);
		assertEquals(
				"UPDATE T_USER SET USER_NAME=?, GENDER=?, AGE=?, IMAGE=?, REMARK=?, LOCK=(LOCK+1), LAST_UPDATE=?, REGION=? WHERE USER_ID=? AND LOCK=?",
				sql.getStatement());
		assertArrayEquals(new Object[] { "Tom", "1", 30, null, null, null, 1L, 1L, 1L }, sql.getArgs());
	}

	@Test
	public void testBuildUpdateSelectiveByExampleSql() {
		EntityInfo info = parser.parse(User.class);
		User user = new User();
		user.setAge(30);
		user.setGender("1");
		user.setUserName("Tom");
		User example = new User();
		example.setGender("0");
		example.setAge(25);
		Sql sql = builder.buildUpdateSelectiveByExampleSql(user, example, info);
		assertEquals(
				"UPDATE T_USER SET USER_NAME=?, GENDER=?, AGE=?, LOCK=(LOCK+1) WHERE GENDER=? AND AGE=?",
				sql.getStatement());
		assertArrayEquals(new Object[] { "Tom", "1", 30, "0", 25 }, sql.getArgs());
	}

	@Test
	public void testBuildUpdateSelectiveByPrimaryKeySql() {
		EntityInfo info = parser.parse(User.class);
		User user = new User();
		user.setUserId(1L);
		user.setAge(30);
		user.setGender("1");
		user.setUserName("Tom");
		user.setRegion(1L);
		user.setLock(1L);
		Sql sql = builder.buildUpdateSelectiveByPrimaryKeySql(user, info);
		assertEquals(
				"UPDATE T_USER SET USER_NAME=?, GENDER=?, AGE=?, LOCK=(LOCK+1), REGION=? WHERE USER_ID=? AND LOCK=?",
				sql.getStatement());
		assertArrayEquals(new Object[] { "Tom", "1", 30, 1L, 1L, 1L }, sql.getArgs());
	}
}
