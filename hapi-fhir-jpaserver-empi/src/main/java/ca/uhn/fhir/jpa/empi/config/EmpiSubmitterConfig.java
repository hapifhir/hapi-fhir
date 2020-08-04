package ca.uhn.fhir.jpa.empi.config;

/*-
 * #%L
 * HAPI FHIR JPA Server - Enterprise Master Patient Index
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.empi.api.IEmpiBatchSvc;
import ca.uhn.fhir.empi.api.IEmpiChannelSubmitterSvc;
import ca.uhn.fhir.empi.rules.config.EmpiRuleValidator;
import ca.uhn.fhir.jpa.dao.empi.EmpiLinkDeleteSvc;
import ca.uhn.fhir.jpa.empi.interceptor.EmpiSubmitterInterceptorLoader;
import ca.uhn.fhir.jpa.empi.svc.EmpiBatchSvcImpl;
import ca.uhn.fhir.jpa.empi.svc.EmpiChannelSubmitterSvcImpl;
import ca.uhn.fhir.jpa.empi.svc.EmpiPersonDeletingSvc;
import ca.uhn.fhir.jpa.empi.svc.EmpiSearchParamSvc;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class EmpiSubmitterConfig {

	@Bean
	EmpiSubmitterInterceptorLoader empiSubmitterInterceptorLoader() {
		return new EmpiSubmitterInterceptorLoader();
	}

	@Bean
	EmpiSearchParamSvc empiSearchParamSvc() {
		return new EmpiSearchParamSvc();
	}

	@Bean
	EmpiRuleValidator empiRuleValidator(FhirContext theFhirContext) {
		return new EmpiRuleValidator(theFhirContext, empiSearchParamSvc());
	}

	@Bean
	EmpiLinkDeleteSvc empiLinkDeleteSvc() {
		return new EmpiLinkDeleteSvc();
	}

	@Bean
	EmpiPersonDeletingSvc empiPersonDeletingSvc() {
		return new EmpiPersonDeletingSvc();
	}

	@Bean
	@Lazy
	IEmpiChannelSubmitterSvc empiChannelSubmitterSvc(FhirContext theFhirContext, IChannelFactory theChannelFactory) {
		return new EmpiChannelSubmitterSvcImpl(theFhirContext, theChannelFactory);
	}

	@Bean
	IEmpiBatchSvc empiBatchService() {
		return new EmpiBatchSvcImpl();
	}
}
