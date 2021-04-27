# CORS (Cross-Origin Resource Sharing)
			
If you are intending to support JavaScript clients in your server application, you will generally need to enable Cross Origin Resource Sharing (CORS). There are a number of ways of supporting this, so two are shown here: 

* An approach using a HAPI FHIR Server Interceptor (Requires SpringFramework)
* An approach using a servlet Filter (Container Specific)

<a name="cors_interceptor"/>

# HAPI FHIR CORS Interceptor

The HAPI FHIR server framework includes an interceptor that can be used to provide CORS functionality on your server. This mechanism is nice because it relies purely on Java configuration (no messing around with web.xml files). HAPI's interceptor is a thin wrapper around Spring Framework's CorsProcessor class, so it requires Spring to be present on your classpath.

Spring is generally unlikely to conflict with other libraries so it is usually safe to add it to your classpath, but it is a fairly large library so if size is a concern you might opt to use a filter instead.

* [CorsInterceptor JavaDoc](/apidocs/hapi-fhir-server/ca/uhn/fhir/rest/server/interceptor/CorsInterceptor.html)
* [CorsInterceptor Source](https://github.com/hapifhir/hapi-fhir/blob/master/hapi-fhir-server/src/main/java/ca/uhn/fhir/rest/server/interceptor/CorsInterceptor.java)

The following steps outline how to enable HAPI's CorsInterceptor:

Add the following dependency to your POM. Note the exclusion of commons-logging, as we are using SLF4j without commons-logging in most of our examples. If your application uses commons-logging you don't need to exclude that dependency.

```xml
<dependency>
	<groupId>org.springframework</groupId>
	<artifactId>spring-web</artifactId>
	<version>${spring_version}</version>
	<exclusions>
		<exclusion>
			<artifactId>commons-logging</artifactId>
			<groupId>commons-logging</groupId>
		</exclusion>
	</exclusions>
</dependency>
```

In your server's initialization method, create and register a CorsInterceptor:

```java
{{snippet:classpath:/ca/uhn/hapi/fhir/docs/ServletExamples.java|corsInterceptor}}
``` 

# CORS Servlet Filter

<p class="doc_info_bubble">
Note that in previous revisions of HAPI FHIR documentation we recommended using the <a href="https://github.com/ebay/cors-filter">eBay CORS Filter</a>, but as of 2016 the eBay filter is no longer being maintained and contains known bugs. We now recommend against using this filter. 
</p>

The following examples show how to use the Apache Tomcat CorsFilter to enable CORS support. The filter being used (`org.apache.catalina.filters.CorsFilter`) is bundled with Apache Tomcat so if you are deploying to that server you can use the filter.

Other containers have similar filters you can use, so consult the documentation for the given container you are using for more information. (If you have an example for configuring a different CORS filter, please send it our way! Examples are always useful!)

In your web.xml file (within the WEB-INF directory in your WAR file), the following filter definition adds the CORS filter, including support for the X-FHIR-Starter header defined by SMART Platforms.

```xml
<filter>
	<filter-name>CORS Filter</filter-name>
	<filter-class>org.apache.catalina.filters.CorsFilter</filter-class>
	<init-param>
		<description>A comma separated list of allowed origins. Note: An '*' cannot be used for an allowed origin when using credentials.</description>
		<param-name>cors.allowed.origins</param-name>
		<param-value>*</param-value>
	</init-param>
	<init-param>
		<description>A comma separated list of HTTP verbs, using which a CORS request can be made.</description>
		<param-name>cors.allowed.methods</param-name>
		<param-value>GET,POST,PUT,DELETE,OPTIONS</param-value>
	</init-param>
	<init-param>
		<description>A comma separated list of allowed headers when making a non simple CORS request.</description>
		<param-name>cors.allowed.headers</param-name>
		<param-value>X-FHIR-Starter,Origin,Accept,X-Requested-With,Content-Type,Access-Control-Request-Method,Access-Control-Request-Headers,Authorization</param-value>
	</init-param>
	<init-param>
		<description>A comma separated list non-standard response headers that will be exposed to XHR2 object.</description>
		<param-name>cors.exposed.headers</param-name>
		<param-value>Location,Content-Location</param-value>
	</init-param>
	<init-param>
		<description>A flag that suggests if CORS is supported with cookies</description>
		<param-name>cors.support.credentials</param-name>
		<param-value>true</param-value>
	</init-param>
	<init-param>
		<description>A flag to control logging</description>
		<param-name>cors.logging.enabled</param-name>
		<param-value>true</param-value>
	</init-param>
	<init-param>
		<description>Indicates how long (in seconds) the results of a preflight request can be cached in a preflight result cache.</description>
		<param-name>cors.preflight.maxage</param-name>
		<param-value>300</param-value>
	</init-param>
</filter>
<filter-mapping>
	<filter-name>CORS Filter</filter-name>
	<url-pattern>/*</url-pattern>
</filter-mapping>
```


