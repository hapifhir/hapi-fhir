package ca.uhn.fhir.jpa.interceptor;

import ca.uhn.fhir.jpa.model.interceptor.api.Hook;
import ca.uhn.fhir.jpa.model.interceptor.api.Interceptor;
import ca.uhn.fhir.jpa.model.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.model.search.SearchRuntimeDetails;
import ca.uhn.fhir.util.LogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Interceptor(manualRegistration = true)
public class PerformanceTracingLoggingInterceptor {
	private static final Logger ourLog = LoggerFactory.getLogger(PerformanceTracingLoggingInterceptor.class);
	private final Logger myLog;
	private final LogUtil.Level myLevel;

	/**
	 * Constructor that logs to this class with a level of INFO
	 */
	public PerformanceTracingLoggingInterceptor() {
		this(ourLog, LogUtil.Level.INFO);
	}

	/**
	 * Constructor that logs to a custom logger and level
	 */
	public PerformanceTracingLoggingInterceptor(Logger theLog, LogUtil.Level theLevel) {
		myLog = theLog;
		myLevel = theLevel;
	}

	@Hook(value = Pointcut.PERFTRACE_SEARCH_FIRST_RESULT_LOADED)
	public void searchFirstResultLoaded(SearchRuntimeDetails theOutcome) {
		log("Initial query result returned in {} for query {}", theOutcome.getQueryStopwatch(), theOutcome.getSearchUuid());
	}

	@Hook(value = Pointcut.PERFTRACE_SEARCH_SELECT_COMPLETE)
	public void searchSelectComplete(SearchRuntimeDetails theOutcome) {
		log("Query found {} matches in {} for query {}", theOutcome.getFoundMatchesCount(), theOutcome.getQueryStopwatch(), theOutcome.getSearchUuid());
	}

	private void log(String theMessage, Object... theArgs) {
		LogUtil.log(myLog, myLevel, theMessage, theArgs);
	}

}
