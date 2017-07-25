package ${basePackage}.service.${moduleName}.impl;

import ${basePackage}.service.${moduleName}.dto.${entityName}DTO;
import ${basePackage}.data.${moduleName}.entity.${entityName};
import ${basePackage}.data.${moduleName}.I${entityName}Dao;
import ${basePackage}.service.${moduleName}.I${entityName}Service;
import org.springframework.stereotype.Component;
<#if !pk.fullJavaType?starts_with("java.lang")>
import {pk.fullJavaType};
</#if>
import com.github.pasp.context.service.BaseService;

@Component("${entityName?uncap_first}Service")
public class ${entityName}ServiceImpl extends BaseService<${entityName}, ${pk.javaType}, ${entityName}DTO> implements I${entityName}Service {

	public I${entityName}Dao getDao() {
		return (I${entityName}Dao) super.getDao();
	}
	
}