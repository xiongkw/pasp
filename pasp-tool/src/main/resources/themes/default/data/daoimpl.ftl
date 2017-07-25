package ${basePackage}.data.${moduleName}.impl;

import ${basePackage}.data.${moduleName}.I${entityName}Dao;
import ${basePackage}.data.${moduleName}.entity.${entityName};
import org.springframework.stereotype.Repository;
<#if !pk.fullJavaType?starts_with("java.lang")>
import {pk.fullJavaType};
</#if>
import com.github.pasp.data.dao.BaseDao;

@Repository("${entityName?uncap_first}Dao")
public class ${entityName}DaoImpl extends BaseDao<${entityName}, ${pk.javaType}> implements I${entityName}Dao {

}