package com.github.pasp.data.dao;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.github.pasp.data.entity.User;

public class EntityInfoParserTest {

	private EntityInfoParser parser = new EntityInfoParser();

	@Test
	public void testParse() {
		EntityInfo info = parser.parse(User.class);
		assertEquals("T_USER", info.getTableName());

		ColumnInfo pk = info.getPrimaryKey();
		assertNotNull(pk);
		assertEquals("USER_ID", pk.getColumnName());
		assertEquals("userId", pk.getFieldName());
		assertTrue(pk.isPrimaryKey());
		assertFalse(pk.isSharding());
		assertFalse(pk.isSharding());
		assertEquals(Long.class, pk.getType());

		ColumnInfo versionColumn = info.getVersionColumn();
		assertNotNull(versionColumn);
		assertEquals("LOCK", versionColumn.getColumnName());
		assertTrue(versionColumn.isVersioning());

		List<ColumnInfo> columnList = info.getColumnList();
		assertTrue(columnList.size() == 9);
		
		assertEquals(
				"INSERT INTO T_USER(USER_ID, USER_NAME, GENDER, AGE, IMAGE, REMARK, LOCK, LAST_UPDATE, REGION) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
				info.getInsertSql());
		assertEquals(
				"UPDATE T_USER SET USER_NAME=?, GENDER=?, AGE=?, IMAGE=?, REMARK=?, LOCK=(LOCK+1), LAST_UPDATE=?, REGION=? WHERE USER_ID=? AND LOCK=?",
				info.getUpdateByPrimaryKeySql());
		assertEquals(
				"SELECT USER_ID, USER_NAME, GENDER, AGE, IMAGE, REMARK, LOCK, LAST_UPDATE, REGION FROM T_USER WHERE USER_ID=?",
				info.getSelectByPrimaryKeySql());
		assertEquals("DELETE FROM T_USER WHERE USER_ID=?", info.getDeleteByPrimaryKeySql());

		assertEquals("DELETE FROM T_USER WHERE USER_ID=? AND LOCK=?", info.getDeleteByPrimaryKeySqlWithVersion());
	}
}
