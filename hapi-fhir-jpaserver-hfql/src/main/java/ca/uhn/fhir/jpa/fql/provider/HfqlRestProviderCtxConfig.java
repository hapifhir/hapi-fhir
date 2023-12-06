/*-
 * #%L
 * HAPI FHIR JPA Server - HFQL Driver
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.fql.provider;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.fql.executor.HfqlExecutor;
import ca.uhn.fhir.jpa.fql.executor.IHfqlExecutor;
import ca.uhn.fhir.rest.server.IPagingProvider;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class HfqlRestProviderCtxConfig {

	@Bean
	@Lazy
	public IHfqlExecutor fqlExecutor(
			@Autowired FhirContext myFhirContext,
			@Autowired IPagingProvider myPagingProvider,
			@Autowired ISearchParamRegistry mySearchParamRegistry,
			@Autowired DaoRegistry myDaoRegistry) {
		return new HfqlExecutor(myFhirContext, myPagingProvider, mySearchParamRegistry, myDaoRegistry);
	}

	@Bean
	@Lazy
	public HfqlRestProvider fqlRestProvider() {
		return new HfqlRestProvider();
	}
}
