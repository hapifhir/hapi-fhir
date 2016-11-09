package ca.uhn.fhirtest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor;
import ca.uhn.fhir.rest.server.interceptor.LoggingInterceptor;
import ca.uhn.fhirtest.interceptor.AnalyticsInterceptor;
import ca.uhn.fhirtest.joke.HolyFooCowInterceptor;

@Configuration
public class CommonConfig {

	/**
	 * Do some fancy logging to create a nice access log that has details about each incoming request.
	 */
	@Bean
	public IServerInterceptor accessLoggingInterceptor() {
		LoggingInterceptor retVal = new LoggingInterceptor();
		retVal.setLoggerName("fhirtest.access");
		retVal.setMessageFormat(
				"Path[${servletPath}] Source[${requestHeader.x-forwarded-for}] Operation[${operationType} ${operationName} ${idOrResourceName}] UA[${requestHeader.user-agent}] Params[${requestParameters}] ResponseEncoding[${responseEncodingNoDefault}]");
		retVal.setLogExceptions(true);
		retVal.setErrorMessageFormat("ERROR - ${requestVerb} ${requestUrl}");
		return retVal;
	}

	/**
	 * This interceptor pings Google Analytics with usage data for the server
	 */
	@Bean
	public IServerInterceptor analyticsInterceptor() {
		AnalyticsInterceptor retVal = new AnalyticsInterceptor();
		retVal.setAnalyticsTid("UA-1395874-6");
		return retVal;
	}
	
	/**
	 * Do some fancy logging to create a nice access log that has details about each incoming request.
	 */
	@Bean
	public IServerInterceptor requestLoggingInterceptor() {
		LoggingInterceptor retVal = new LoggingInterceptor();
		retVal.setLoggerName("fhirtest.request");
		retVal.setMessageFormat("${requestVerb} ${servletPath} -\n${requestBodyFhir}");
		retVal.setLogExceptions(false);
		return retVal;
	}
	
	/**
	 * This is a joke
	 * 
	 * https://chat.fhir.org/#narrow/stream/implementers/topic/Unsupported.20search.20parameters
	 */
	@Bean
	public IServerInterceptor holyFooCowInterceptor() {
		return new HolyFooCowInterceptor();
	}

}
