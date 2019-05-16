package ca.uhn.fhir.jpa.interceptor;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.model.search.SearchRuntimeDetails;
import ca.uhn.fhir.util.LogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

@Interceptor()
public class PerformanceTracingLoggingInterceptor {
	private static final Logger ourLog = LoggerFactory.getLogger(PerformanceTracingLoggingInterceptor.class);
	private final Logger myLog;
	private final Level myLevel;

	/**
	 * Constructor that logs to this class with a level of INFO
	 */
	public PerformanceTracingLoggingInterceptor() {
		this(ourLog, Level.INFO);
	}

	/**
	 * Constructor that logs to a custom logger and level
	 */
	public PerformanceTracingLoggingInterceptor(Logger theLog, Level theLevel) {
		myLog = theLog;
		myLevel = theLevel;
	}

	@Hook(value = Pointcut.JPA_PERFTRACE_SEARCH_FIRST_RESULT_LOADED)
	public void searchFirstResultLoaded(SearchRuntimeDetails theOutcome) {
		log("Initial query result returned in {} for query {}", theOutcome.getQueryStopwatch(), theOutcome.getSearchUuid());
	}

	@Hook(value = Pointcut.JPA_PERFTRACE_SEARCH_SELECT_COMPLETE)
	public void searchSelectComplete(SearchRuntimeDetails theOutcome) {
		log("Query found {} matches in {} for query {}", theOutcome.getFoundMatchesCount(), theOutcome.getQueryStopwatch(), theOutcome.getSearchUuid());
	}

	@Hook(value = Pointcut.JPA_PERFTRACE_SEARCH_COMPLETE)
	public void searchComplete(SearchRuntimeDetails theOutcome) {
		log("Query {} is complete in {} - Found {} matches", theOutcome.getSearchUuid(), theOutcome.getQueryStopwatch(), theOutcome.getFoundMatchesCount());
	}

	@Hook(value = Pointcut.JPA_PERFTRACE_SEARCH_PASS_COMPLETE)
	public void searchPassComplete(SearchRuntimeDetails theOutcome) {
		log("Query {} pass complete and set to status {} in {} - Found {} matches", theOutcome.getSearchUuid(), theOutcome.getSearchStatus(), theOutcome.getQueryStopwatch(), theOutcome.getFoundMatchesCount());
	}

	@Hook(value = Pointcut.JPA_PERFTRACE_SEARCH_FAILED)
	public void searchFailed(SearchRuntimeDetails theOutcome) {
		log("Query {} failed in {} - Found {} matches", theOutcome.getSearchUuid(), theOutcome.getQueryStopwatch(), theOutcome.getFoundMatchesCount());
	}

	private void log(String theMessage, Object... theArgs) {
		LogUtil.log(myLog, myLevel, theMessage, theArgs);
	}

}
