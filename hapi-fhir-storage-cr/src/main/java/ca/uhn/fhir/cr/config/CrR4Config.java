package ca.uhn.fhir.cr.config;

/*-
 * #%L
 * HAPI FHIR - Clinical Reasoning
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.cr.r4.provider.MeasureOperationsProvider;
import ca.uhn.fhir.cr.r4.service.MeasureService;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;

import java.util.function.Function;

@Configuration
public class CrR4Config extends BaseCrConfig {

	@Bean
	public Function<RequestDetails, MeasureService> r4MeasureServiceFactory() {
		return r -> {
			var ms = r4measureService();
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
	public MeasureOperationsProvider r4measureOperationsProvider() { return new MeasureOperationsProvider();}
}
