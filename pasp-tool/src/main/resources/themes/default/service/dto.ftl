package ${basePackage}.service.${moduleName}.dto;

import java.io.Serializable;
<#list importList as i>
import ${i};
</#list>

import com.github.pasp.core.DTO;

public class ${entityName}DTO extends DTO implements Serializable{
	
	private static final long serialVersionUID = 1L;

	<#if pk??>
	/**
	 * ${pk.comment!""}
	 */
	private ${pk.javaType} ${pk.name};
	</#if>
	
	<#if fields??>
	<#list fields as field>
	/**
	 * ${field.comment!""}
	 */
	private ${field.javaType} ${field.name};
	
	</#list>
	</#if>
	<#if pk??>
	public ${pk.javaType} get${pk.name?cap_first}(){
		return this.${pk.name};
	}
	
	public void set${pk.name?cap_first}(${pk.javaType} ${pk.name}){
		this.${pk.name} = ${pk.name};
	}
	
	</#if>
	<#if fields??>
	<#list fields as field>
	public ${field.javaType} get${field.name?cap_first}(){
		return this.${field.name};
	}
	
	public void set${field.name?cap_first}(${field.javaType} ${field.name}){
		this.${field.name} = ${field.name};
	}
	
	</#list>
	</#if>
}
