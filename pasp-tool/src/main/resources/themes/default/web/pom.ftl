<?xml version="1.0"?>
<project
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd"
	xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
	<modelVersion>4.0.0</modelVersion>
	<parent>
		<groupId>${groupId}</groupId>
		<artifactId>${artifactId}</artifactId>
		<version>${version}</version>
	</parent>
	<artifactId>${artifactId}-web</artifactId>
	<name>${artifactId}-web</name>
	<packaging>war</packaging>
	
	<dependencies>
		
		<dependency>
			<groupId>${groupId}</groupId>
			<artifactId>${artifactId}-control</artifactId>
			<version>${version}</version>
		</dependency>
		
		<#noparse>
		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-web</artifactId>
			<version>${spring.version}</version>
		</dependency>

		<dependency>
			<groupId>ch.qos.logback</groupId>
			<artifactId>logback-classic</artifactId>
			<version>${logback.version}</version>
		</dependency>

		<dependency>
			<groupId>org.slf4j</groupId>
			<artifactId>log4j-over-slf4j</artifactId>
			<version>${log4j.over.slf4j.version}</version>
		</dependency>

		</#noparse>
	</dependencies>
	
	<build>
		<finalName>${artifactId}-web</finalName>
	</build>
</project>