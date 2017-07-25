<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<groupId>${groupId}</groupId>
	<artifactId>${artifactId}</artifactId>
	<version>${version}</version>
	<packaging>pom</packaging>
	<modules>
		<module>${artifactId}-common</module>
		<module>${artifactId}-data</module>
		<module>${artifactId}-service</module>
		<module>${artifactId}-control</module>
		<module>${artifactId}-web</module>
	</modules>

	<properties>
		<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
		<java.source.version>1.7</java.source.version>
		<java.target.version>1.7</java.target.version>
		<junit.version>4.11</junit.version>
		<jackson.version>1.8.1</jackson.version>
		<spring.version>4.1.6.RELEASE</spring.version>
		<pasp.version>${paspVersion}</pasp.version>
		<mysql.version>5.1.32</mysql.version>
		<logback.version>1.1.6</logback.version>
		<log4j.over.slf4j.version>1.1.6</log4j.over.slf4j.version>
	</properties>

	<#noparse>
	<dependencyManagement>
		<dependencies>
			<dependency>
				<groupId>com.github.pasp</groupId>
				<artifactId>pasp-core</artifactId>
				<version>${pasp.version}</version>
			</dependency>

			<dependency>
				<groupId>com.github.pasp</groupId>
				<artifactId>pasp-data</artifactId>
				<version>${pasp.version}</version>
			</dependency>

			<dependency>
				<groupId>com.github.pasp</groupId>
				<artifactId>pasp-context</artifactId>
				<version>${pasp.version}</version>
			</dependency>
			
			<dependency>
				<groupId>com.github.pasp</groupId>
				<artifactId>pasp-web</artifactId>
				<version>${pasp.version}</version>
			</dependency>
		</dependencies>
	</dependencyManagement>

	<dependencies>
		<dependency>
			<groupId>junit</groupId>
			<artifactId>junit</artifactId>
			<version>${junit.version}</version>
			<scope>test</scope>
		</dependency>

		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-test</artifactId>
			<version>${spring.version}</version>
			<scope>test</scope>
		</dependency>
	</dependencies>
	</#noparse>
	
	<repositories>

		<repository>
			<id>nexus-ctgae</id>
			<name>local private nexus</name>
			<url>http://132.122.1.152:8081/nexus/content/groups/public</url>
			<releases>
				<enabled>true</enabled>
				<updatePolicy>always</updatePolicy>
			</releases>
			<snapshots>
				<enabled>true</enabled>
				<updatePolicy>always</updatePolicy>
			</snapshots>
		</repository>

	</repositories>

	<#noparse>
	<build>
		<plugins>
			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-compiler-plugin</artifactId>
				<version>3.1</version>
				<configuration>
					<source>${java.source.version}</source>
					<target>${java.source.version}</target>
					<showWarnings>true</showWarnings>
					<encoding>UTF-8</encoding>
				</configuration>
			</plugin>
		</plugins>
	</build>
	</#noparse>
</project>