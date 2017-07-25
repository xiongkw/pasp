package ${basePackage}.data.${moduleName};

import ${basePackage}.data.${moduleName}.entity.${entityName};
<#if !pk.fullJavaType?starts_with("java.lang")>
import {pk.fullJavaType};
</#if>
import com.github.pasp.core.IBaseDao;

public interface I${entityName}Dao extends IBaseDao<${entityName}, ${pk.javaType}> {

}