/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.searchparam.submit.interceptor;

import ca.uhn.fhir.IHapiBootOrder;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;

public class SearchParamSubmitInterceptorLoader {

	private static final Logger ourLog = LoggerFactory.getLogger(SearchParamSubmitInterceptorLoader.class);

	private SearchParamValidatingInterceptor mySearchParamValidatingInterceptor;

	private IInterceptorService myInterceptorRegistry;

	@EventListener(classes = {ContextRefreshedEvent.class})
	@Order(IHapiBootOrder.REGISTER_INTERCEPTORS)
	public void start() {
		ourLog.info("Registering SearchParamValidatingInterceptor interceptor");
		myInterceptorRegistry.registerInterceptor(mySearchParamValidatingInterceptor);
	}

	@Autowired
	public void setSearchParamValidatingInterceptor(
			SearchParamValidatingInterceptor theSearchParamValidatingInterceptor) {
		mySearchParamValidatingInterceptor = theSearchParamValidatingInterceptor;
	}

	@Autowired
	public void setInterceptorRegistry(IInterceptorService theInterceptorRegistry) {
		myInterceptorRegistry = theInterceptorRegistry;
	}

	protected SearchParamValidatingInterceptor getSearchParamValidatingInterceptor() {
		return mySearchParamValidatingInterceptor;
	}
}
