package ${basePackage}.service.${moduleName};

import ${basePackage}.service.${moduleName}.dto.${entityName}DTO;
import ${basePackage}.data.${moduleName}.entity.${entityName};
<#if !pk.fullJavaType?starts_with("java.lang")>
import {pk.fullJavaType};
</#if>
import com.github.pasp.core.IBaseService;

public interface I${entityName}Service extends IBaseService<${entityName}, ${pk.javaType}, ${entityName}DTO> {

}