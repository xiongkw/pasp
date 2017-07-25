<?xml version="1.0" encoding="UTF-8"?>
<sqlList>
    <!--
    <sql id="$get">
        SELECT
        <#if fields??>
        <#list fields as field>
        ${field.columnName}<#if field_has_next>, </#if>
        </#list>
        </#if>
        FROM
        ${tableName}
        WHERE
        ${pk.columnName} = ?
    </sql>
    -->
    <!--
	<sql id="$select">
        SELECT
        <#if fields??>
        <#list fields as field>
        ${field.columnName}<#if field_has_next>, </#if>
        </#list>
        </#if>
        FROM
        ${tableName}
        LIMIT ?,?
    </sql>
    -->
    <!--
    <sql id="$insert">
        INSERT INTO
        ${tableName}(
        <#if fields??>
        <#list fields as field>
        ${field.columnName}<#if field_has_next>, </#if>
        </#list>
        </#if>
        )
        VALUES(<#if fields??><#list fields as field>?<#if field_has_next>, </#if></#list></#if>)

    </sql>
    -->
    <!--
    <sql id="$update">
        UPDATE ${tableName}
        SET
        <#if fields??>
        <#list fields as field>
        ${field.columnName} = ?<#if field_has_next>, </#if>
        </#list>
        </#if>
        WHERE
        ${pk.columnName} = ?

    </sql>
    -->
    <!--
    <sql id="$delete">
        DELETE FROM ${tableName} WHERE ${pk.columnName} = ?
    </sql>
    -->
</sqlList>