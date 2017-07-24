package com.github.pasp.data;

import java.util.List;

import com.github.pasp.core.Entity;
import com.github.pasp.data.dao.EntityInfo;

public interface ISqlBuilder {

	Sql buildInsertSql(Entity<?> entity, EntityInfo info);
	
	Sql buildSelectByPrimaryKeySql(Entity<?> entity, EntityInfo info);

	Sql buildUpdateByPrimaryKeySql(Entity<?> entity, EntityInfo info);

	Sql buildDeleteByPrimaryKeySql(Entity<?> entity, EntityInfo info);

	Sql buildUpdateSelectiveByPrimaryKeySql(Entity<?> entity, EntityInfo entityInfo);

	Sql buildSelectByExampleSql(Entity<?> entity, EntityInfo entityInfo);

	Sql buildCountByExampleSql(Entity<?> entity, EntityInfo entityInfo);

	Sql buildBatchInsertSql(List<?> entities, EntityInfo entityInfo);

	Sql buildBatchUpdateByPrimaryKeySql(List<?> entities, EntityInfo entityInfo);

	Sql buildBatchDeleteByPrimaryKeySql(List<?> entities, EntityInfo entityInfo);

	Sql buildDeleteByExampleSql(Entity<?> example, EntityInfo entityInfo);

	Sql buildUpdateSelectiveByExampleSql(Entity<?> entity,Entity<?> example, EntityInfo entityInfo);

	Sql buildUpdateByExampleSql(Entity<?> entity, Entity<?> example, EntityInfo entityInfo);

}
