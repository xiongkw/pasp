<!DOCTYPE web-app PUBLIC
 "-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN"
 "http://java.sun.com/dtd/web-app_2_3.dtd" >
<web-app>
 	<display-name>${artifactId}</display-name>

	<listener>
		<listener-class>com.github.pasp.web.AppContextLoader</listener-class>
	</listener>
	
	<servlet>
		<servlet-name>springServlet</servlet-name>
		<servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
		<init-param>
			<param-name>contextConfigLocation</param-name>
			<param-value>classpath*:conf/spring/spring-mvc.xml</param-value>
		</init-param>
		<load-on-startup>1</load-on-startup>
	</servlet>

	<servlet-mapping>
		<servlet-name>springServlet</servlet-name>
		<url-pattern>/</url-pattern>
	</servlet-mapping>
 
</web-app>