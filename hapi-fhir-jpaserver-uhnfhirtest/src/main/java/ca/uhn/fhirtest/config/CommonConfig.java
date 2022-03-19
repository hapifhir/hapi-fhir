package ca.uhn.fhirtest.config;

import ca.uhn.fhir.batch2.jobs.config.Batch2JobsConfig;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.jpa.batch2.JpaBatch2Config;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.subscription.channel.config.SubscriptionChannelConfig;
import ca.uhn.fhir.jpa.subscription.match.config.SubscriptionProcessorConfig;
import ca.uhn.fhir.jpa.subscription.match.config.WebsocketDispatcherConfig;
import ca.uhn.fhir.jpa.subscription.submit.config.SubscriptionSubmitterConfig;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor;
import ca.uhn.fhir.rest.server.interceptor.LoggingInterceptor;
import ca.uhn.fhirtest.interceptor.AnalyticsInterceptor;
import ca.uhn.fhirtest.joke.HolyFooCowInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
	WebsocketDispatcherConfig.class,
	SubscriptionChannelConfig.class,
	SubscriptionProcessorConfig.class,
	SubscriptionSubmitterConfig.class,
	JpaBatch2Config.class,
	Batch2JobsConfig.class
})
public class CommonConfig {

	/**
	 * Do some fancy logging to create a nice access log that has details about each incoming request.
	 *
	 * @return
	 */
	@Bean
	public LoggingInterceptor accessLoggingInterceptor() {
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
	 * This is a joke
	 * <p>
	 * https://chat.fhir.org/#narrow/stream/implementers/topic/Unsupported.20search.20parameters
	 */
	@Bean
	public IServerInterceptor holyFooCowInterceptor() {
		return new HolyFooCowInterceptor();
	}

	/**
	 * Do some fancy logging to create a nice access log that has details about each incoming request.
	 */
	@Bean
	public LoggingInterceptor requestLoggingInterceptor() {
		LoggingInterceptor retVal = new LoggingInterceptor();
		retVal.setLoggerName("fhirtest.request");
		retVal.setMessageFormat("${requestVerb} ${servletPath} -\n${requestBodyFhir}");
		retVal.setLogExceptions(false);
		return retVal;
	}

	@Bean
	public PartitionSettings partitionSettings() {
		return new PartitionSettings();
	}

	@Bean
	public SqlCaptureInterceptor sqlCaptureInterceptor(IInterceptorService theInterceptorService) {
		SqlCaptureInterceptor retVal = new SqlCaptureInterceptor();
		theInterceptorService.registerInterceptor(retVal);
		return retVal;
	}

	public static boolean isLocalTestMode() {
		return "true".equalsIgnoreCase(System.getProperty("testmode.local"));
	}

}
