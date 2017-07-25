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
	<artifactId>${artifactId}-control</artifactId>
	<name>${artifactId}-control</name>
	
	<dependencies>
		<dependency>
			<groupId>com.github.pasp</groupId>
			<artifactId>pasp-web</artifactId>
		</dependency>
		
		<dependency>
			<groupId>${groupId}</groupId>
			<artifactId>${artifactId}-service</artifactId>
			<version>${version}</version>
		</dependency>

		<dependency>
			<groupId>${groupId}</groupId>
			<artifactId>${artifactId}-common</artifactId>
			<version>${version}</version>
		</dependency>
		
	</dependencies>
</project>