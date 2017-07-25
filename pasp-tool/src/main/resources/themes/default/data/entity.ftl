package ${basePackage}.data.${moduleName}.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
<#list importList as i>
import ${i};
</#list>
<#list entityImportList as i>
import ${i};
</#list>
import com.github.pasp.core.Entity;

@Table(name = "${tableName}")
public class ${entityName} extends Entity<${pk.javaType}> {
	private static final long serialVersionUID = 1L;
	
	<#if pk??>
	/**
	 * ${pk.comment!""}
	 */
	@Id
	@Column(name = "${pk.columnName}")
	private ${pk.javaType} ${pk.name};
	
	</#if>
	<#if fields??>
	<#list fields as field>
	/**
	 * ${field.comment!""}
	 */
	@Column(name = "${field.columnName}")
	<#if field.versionId>
	@Version
	</#if>
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
	<#if pk.name!="id">
	public ${pk.javaType} getId(){
		return this.${pk.name};
	}
	
	public void setId(${pk.javaType} ${pk.name}){
		this.${pk.name} = ${pk.name};
	}
	</#if>
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