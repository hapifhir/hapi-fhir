/*-
 * #%L
 * HAPI FHIR JPA Server - Master Data Management
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
package ca.uhn.fhir.jpa.mdm.config;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.nickname.INicknameSvc;
import ca.uhn.fhir.mdm.api.IMdmSettings;
import ca.uhn.fhir.mdm.interceptor.MdmSearchExpandingInterceptor;
import ca.uhn.fhir.mdm.rules.config.MdmRuleValidator;
import ca.uhn.fhir.mdm.rules.matcher.IMatcherFactory;
import ca.uhn.fhir.mdm.rules.matcher.MdmMatcherFactory;
import ca.uhn.fhir.mdm.rules.svc.MdmResourceMatcherSvc;
import ca.uhn.fhir.mdm.svc.MdmLinkDeleteSvc;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class MdmCommonConfig {
	@Bean
	MdmRuleValidator mdmRuleValidator(FhirContext theFhirContext, ISearchParamRegistry theSearchParamRetriever) {
		return new MdmRuleValidator(theFhirContext, theSearchParamRetriever);
	}

	@Bean
	@Lazy
	public MdmSearchExpandingInterceptor mdmSearchExpandingInterceptor() {
		return new MdmSearchExpandingInterceptor();
	}

	@Bean
	MdmLinkDeleteSvc mdmLinkDeleteSvc() {
		return new MdmLinkDeleteSvc();
	}

	@Bean
	@Lazy
	MdmResourceMatcherSvc mdmResourceComparatorSvc(
			FhirContext theFhirContext, IMatcherFactory theIMatcherFactory, IMdmSettings theMdmSettings) {
		return new MdmResourceMatcherSvc(theFhirContext, theIMatcherFactory, theMdmSettings);
	}

	@Bean
	@Lazy
	public IMatcherFactory matcherFactory(
			FhirContext theFhirContext, IMdmSettings theSettings, INicknameSvc theNicknameSvc) {
		return new MdmMatcherFactory(theFhirContext, theSettings, theNicknameSvc);
	}
}
