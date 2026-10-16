/*-
 * #%L
 * HAPI FHIR JPA Server - Master Data Management
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
import ca.uhn.fhir.mdm.interceptor.MdmReadVirtualizationInterceptor;
import ca.uhn.fhir.mdm.interceptor.MdmSearchExpandingInterceptor;
import ca.uhn.fhir.mdm.rules.config.MdmRuleValidator;
import ca.uhn.fhir.mdm.rules.matcher.IMatcherFactory;
import ca.uhn.fhir.mdm.rules.matcher.MdmMatcherFactory;
import ca.uhn.fhir.mdm.rules.similarity.ISimilarityFactory;
import ca.uhn.fhir.mdm.rules.similarity.MdmSimilarityFactory;
import ca.uhn.fhir.mdm.rules.svc.MdmResourceMatcherSvc;
import ca.uhn.fhir.mdm.svc.MdmLinkDeleteSvc;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;

@Configuration
public class MdmCommonConfig {
	/**
	 * The name of the {@link MdmResourceMatcherSvc} bean that scores with {@link IMdmSettings#getMatchOnlyMdmRules()}.
	 */
	public static final String MATCH_ONLY_MDM_RESOURCE_MATCHER_SVC = "matchOnlyMdmResourceMatcherSvc";

	@Bean
	MdmRuleValidator mdmRuleValidator(
			FhirContext theFhirContext,
			ISearchParamRegistry theSearchParamRetriever,
			IMatcherFactory theMatcherFactory,
			ISimilarityFactory theSimilarityFactory) {
		return new MdmRuleValidator(theFhirContext, theSearchParamRetriever, theMatcherFactory, theSimilarityFactory);
	}

	@Bean
	@Lazy
	public MdmSearchExpandingInterceptor mdmSearchExpandingInterceptor() {
		return new MdmSearchExpandingInterceptor();
	}

	@Bean
	@Lazy
	public MdmReadVirtualizationInterceptor<?> mdmReadVirtualizationInterceptor() {
		return new MdmReadVirtualizationInterceptor<>();
	}

	@Bean
	MdmLinkDeleteSvc mdmLinkDeleteSvc() {
		return new MdmLinkDeleteSvc();
	}

	@Bean
	@Lazy
	@Primary
	MdmResourceMatcherSvc mdmResourceComparatorSvc(
			FhirContext theFhirContext,
			IMatcherFactory theIMatcherFactory,
			ISimilarityFactory theSimilarityFactory,
			IMdmSettings theMdmSettings) {
		return new MdmResourceMatcherSvc(
				theFhirContext, theIMatcherFactory, theSimilarityFactory, theMdmSettings.getMdmRules());
	}

	@Bean(MATCH_ONLY_MDM_RESOURCE_MATCHER_SVC)
	@Lazy
	MdmResourceMatcherSvc matchOnlyMdmResourceMatcherSvc(
			FhirContext theFhirContext,
			IMatcherFactory theIMatcherFactory,
			ISimilarityFactory theSimilarityFactory,
			IMdmSettings theMdmSettings) {
		return new MdmResourceMatcherSvc(
				theFhirContext, theIMatcherFactory, theSimilarityFactory, theMdmSettings.getMatchOnlyMdmRules());
	}

	@Bean
	public IMatcherFactory matcherFactory(FhirContext theFhirContext, INicknameSvc theNicknameSvc) {
		return new MdmMatcherFactory(theFhirContext, theNicknameSvc);
	}

	@Bean
	public ISimilarityFactory similarityFactory() {
		return new MdmSimilarityFactory();
	}
}
