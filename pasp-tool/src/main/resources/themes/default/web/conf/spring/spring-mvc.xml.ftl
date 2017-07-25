<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:p="http://www.springframework.org/schema/p"
	xmlns:context="http://www.springframework.org/schema/context"
	xmlns:mvc="http://www.springframework.org/schema/mvc"
	xsi:schemaLocation="
http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd 
http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd
http://www.springframework.org/schema/mvc 
http://www.springframework.org/schema/mvc/spring-mvc.xsd">


	<!-- 使Spring支持自动检测组件，如注解的Controller -->
	<context:component-scan base-package="${basePackage}"
		use-default-filters="false">
		<context:include-filter expression="org.springframework.stereotype.Controller"
			type="annotation" />
		<context:include-filter type="annotation"
			expression="org.springframework.web.bind.annotation.ControllerAdvice" />
		<context:exclude-filter expression="org.springframework.stereotype.Service"
			type="annotation" />
		<context:exclude-filter type="annotation"
			expression="org.springframework.stereotype.Repository" />
        <context:exclude-filter type="annotation"
			expression="org.springframework.stereotype.Component" />
	</context:component-scan>
	
	<bean class="com.github.pasp.web.control.ExceptionController"/>
	
	<!--RequestMappingHandlerAdapter -->
	<mvc:annotation-driven>
		<mvc:message-converters>
			<ref bean="stringHttpMessageConverter" />
			<ref bean="mappingJacksonHttpMessageConverter" />
		</mvc:message-converters>
		<mvc:argument-resolvers>
			<ref bean="jsonParamArgumentResolver"/>
		</mvc:argument-resolvers>
	</mvc:annotation-driven>
	
	<bean id="jsonParamArgumentResolver" class="com.github.pasp.web.control.GetParamArgumentResolver">
		<property name="objectMapper">
			<bean class="com.github.pasp.web.mapper.JSONObjectMapper" />
		</property>
	</bean>
	
	<!-- 将无法mapping到Controller的path交给default servlet handler处理 -->
	<mvc:default-servlet-handler />

	<!-- 默认的视图解析器 在上边的解析错误时使用 (默认使用html)- -->
	<bean id="viewResolver"
		class="org.springframework.web.servlet.view.InternalResourceViewResolver"
		p:prefix="/WEB-INF/" p:suffix=".html" />

	<!-- 处理JSON数据转换，为了处理返回的JSON数据的编码，默认是ISO-88859-1的，这里把它设置为UTF-8,解决有乱码的情况 -->
	<bean id="mappingJacksonHttpMessageConverter"
		class="org.springframework.http.converter.json.MappingJackson2HttpMessageConverter">
		<property name="supportedMediaTypes">
			<list>
				<value>text/html;charset=UTF-8</value>
			</list>
		</property>
	</bean>
	<bean id="stringHttpMessageConverter"
		class="org.springframework.http.converter.StringHttpMessageConverter">
		<property name="supportedMediaTypes">
			<list>
				<value>text/html;charset=UTF-8</value>
			</list>
		</property>
	</bean>

</beans>