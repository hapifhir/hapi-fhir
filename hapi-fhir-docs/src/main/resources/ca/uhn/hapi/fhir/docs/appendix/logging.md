# Logging

Java has an abundance of logging frameworks, none of which are perfect. Many libraries depend on one or more of these frameworks but also have dependencies who depend on a different one. These dependencies can cause conflicts and be very irritating to solve.

## Quick Start: Using Logback

If you don't want to spend much time worrying about logging, it's probably easiest to just include the [Logback](http://logback.qos.ch/) JAR along with your application.

Logback is a powerful and flexible framework. To configure it, simply include a "logback.xml" file on your classpath. The following contents may be placed in this file to simply log at a suitable level to the console:

```xml
<configuration scan="true" scanPeriod="30 seconds">

	<appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
		<filter class="ch.qos.logback.classic.filter.ThresholdFilter">
			<level>INFO</level>
		</filter>
		<encoder>
			<pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} [%file:%line] %msg%n</pattern>
		</encoder>
	</appender>

	<root level="INFO">
		<appender-ref ref="STDOUT" />
	</root>

</configuration>
```

For more detail on how logging can be configured, see the following section.

# Configuring HAPI's Logging - SLF4j
				
<img src="/hapi-fhir/docs/images/hapi-fhir-logging.svg"  width="723" height="273" alt="Logging arch diagram" align="right"/>
			
HAPI uses [SLF4j](http://www.slf4j.org/) for all internal logging. SLF4j is a *logging facade* framework, meaning that it doesn't actually handle log output (i.e. it isn't actually writing log lines to disk) but rather it is able to delegate that task to any of a number of underlying frameworks (e.g. log4j, logback, JDK logging, etc.)

This means that in order to successfully log anything, you will need to 
add two (or three) dependency JARs to your application:

* **slf4j-api-vXX.jar**: This is the SLF4j API and is necessary for HAPI to function
* An actual logging implementation, as well as its SLF4j binding. For example:
   * The recommended logging framework to use is Logback. Logback is absolutely not necessary for HAPI to function correctly, but it has a number of nice features and is a good default choice. To use logback, you would include `logback-vXX.jar`.
   * If you wanted to use log4j you would include `log4j-vXX.jar` as well as `slf4j-log4j-vXX.jar`. Log4j is a mature framework that is very widely used.
   * If you wanted to use JDK logging (aka java.util.Logging) you would include `slf4j-jdk14-vXX.jar`. JDK logging is included with Java but is not particularly full featured compared to many other frameworks.
    
## Commons-Logging

<img src="/hapi-fhir/docs/images/hapi-fhir-logging-complete.svg" width="614" height="153" alt="Logging arch diagram" align="right"/>

Note that HAPI's client uses Apache HttpComponents Client internally, and that library uses Apache Commons Logging as a logging facade. The recommended approach to using HAPI is to not include any commons-logging JAR in your application, but rather to include a copy of jcl-over-slf4j-vXX.jar. This JAR will simulate commons-logging, but will redirect its logging statements to the same target as SLF4j has been configured to.   

The diagram at the right shows the chain of command for logging under this scheme.

Note that some popular libraries (e.g. Spring Framework) also use commons-logging for logging. As such they may include a commons-logging JAR automatically as a transitive dependency in Maven. If you are using jcl-over-slf4j and it isn't working correctly, it is often worth checking the list of JARs included in your application to see whether commons-logging has also been added. It can then be specifically excluded in Maven.
 
<br clear="all"/>

# Client Payload Logging

To enable detailed logging of client requests and responses (what URL is being requested, what headers and payload are being received, etc.), an interceptor may be added to the client which logs each transaction. See [Logging Requests and Responses](/docs/interceptors/built_in_client_interceptors.html#logging_interceptor) for more information.

# Server Request Logging

To enable detailed logging of server requests and responses, an interceptor may be added to the server which logs each transaction. See [Logging Interceptor](/docs/interceptors/built_in_server_interceptors.html#logging_interceptor) for more information.

# Hibernate SQL Log Filtering

Hibernate logs SQL statements from a single class, which makes hard to obtain SQL logs only for a specific feature, as logging includes all background processes.

Hibernate SQL log filtering feature allows you to filter Hibernate SQL logging by adding filters in a  `hibernate-sql-log-filters.txt` file and adding it to the classpath.

Hibernate SQL log filtering and its filter-refreshing task activate when `org.hibernate.SQL` logging is set to `debug` level or higher, and deactivate when logging is set to `info` level or lower. 

The feature doesn't affect performance in any way while inactive, also shutting down background filter-refreshing process. It can affect performance when active, so the feature is intended only as a  debugging tool.  

The filter lines must start with one of: 
1. `stack: ` to filter log entries produced by code which stack trace has a line which starts with filter string, 
2. `sw: ` to filter log entries which start with filter string, or
3. `frag: ` to filter log entries which contain filter string 

A sample file is provided with filter lines commented out. These filter lines, once uncommented, filter out most background processes logging. 


