/*-
 * #%L
 * HAPI FHIR - Clinical Reasoning
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
package ca.uhn.fhir.cr.config;

import ca.uhn.fhir.cr.r4.measure.CareGapsOperationProvider;
import ca.uhn.fhir.cr.r4.measure.CareGapsService;
import ca.uhn.fhir.cr.r4.measure.ISubmitDataService;
import ca.uhn.fhir.cr.r4.measure.MeasureOperationsProvider;
import ca.uhn.fhir.cr.r4.measure.MeasureService;
import ca.uhn.fhir.cr.r4.measure.SubmitDataProvider;
import ca.uhn.fhir.cr.r4.measure.SubmitDataService;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;

import java.util.concurrent.Executor;
import java.util.function.Function;

@Configuration
@Import(BaseClinicalReasoningConfig.class)
public class CrR4Config {

	@Bean
	public Function<RequestDetails, MeasureService> r4MeasureServiceFactory(ApplicationContext theApplicationContext) {
		return r -> {
			var ms = theApplicationContext.getBean(MeasureService.class);
			ms.setRequestDetails(r);
			return ms;
		};
	}

	@Bean
	@Scope("prototype")
	public MeasureService r4measureService() {
		return new MeasureService();
	}

	@Bean
	public MeasureOperationsProvider r4measureOperationsProvider() {
		return new MeasureOperationsProvider();
	}

	@Bean
	public Function<RequestDetails, CareGapsService> r4CareGapsServiceFactory(Function<RequestDetails, MeasureService> theR4MeasureServiceFactory,
																									  CrProperties theCrProperties,
																									  DaoRegistry theDaoRegistry, Executor cqlExecutor) {
		return r -> {
			var ms = theR4MeasureServiceFactory.apply(r);
			var cs = new CareGapsService(theCrProperties, ms, theDaoRegistry, cqlExecutor, r);
			return cs;
		};
	}

	@Bean
	public CareGapsOperationProvider r4CareGapsProvider(Function<RequestDetails, CareGapsService> theCareGapsServiceFunction){
		return new CareGapsOperationProvider(theCareGapsServiceFunction);
	}

	@Bean
	public ISubmitDataService r4SubmitDataService(DaoRegistry theDaoRegistry){
		return requestDetails -> new SubmitDataService(theDaoRegistry, requestDetails);
	}

	@Bean
	public SubmitDataProvider r4SubmitDataProvider(ISubmitDataService theSubmitDataService){
		return new SubmitDataProvider(theSubmitDataService);
	}

}
