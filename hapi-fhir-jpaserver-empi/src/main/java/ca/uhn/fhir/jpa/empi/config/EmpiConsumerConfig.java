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
import ca.uhn.fhir.empi.api.IEmpiControllerSvc;
import ca.uhn.fhir.empi.api.IEmpiExpungeSvc;
import ca.uhn.fhir.empi.api.IEmpiLinkQuerySvc;
import ca.uhn.fhir.empi.api.IEmpiLinkSvc;
import ca.uhn.fhir.empi.api.IEmpiLinkUpdaterSvc;
import ca.uhn.fhir.empi.api.IEmpiMatchFinderSvc;
import ca.uhn.fhir.empi.api.IGoldenResourceMergerSvc;
import ca.uhn.fhir.empi.api.IEmpiSettings;
import ca.uhn.fhir.empi.log.Logs;
import ca.uhn.fhir.empi.provider.EmpiControllerHelper;
import ca.uhn.fhir.empi.provider.EmpiProviderLoader;
import ca.uhn.fhir.empi.rules.config.EmpiRuleValidator;
import ca.uhn.fhir.empi.rules.svc.EmpiResourceMatcherSvc;
import ca.uhn.fhir.empi.util.EIDHelper;
import ca.uhn.fhir.empi.util.MessageHelper;
import ca.uhn.fhir.empi.util.PersonHelper;
import ca.uhn.fhir.jpa.dao.empi.EmpiLinkDeleteSvc;
import ca.uhn.fhir.jpa.empi.broker.EmpiMessageHandler;
import ca.uhn.fhir.jpa.empi.broker.EmpiQueueConsumerLoader;
import ca.uhn.fhir.jpa.empi.dao.EmpiLinkDaoSvc;
import ca.uhn.fhir.jpa.empi.dao.EmpiLinkFactory;
import ca.uhn.fhir.jpa.empi.interceptor.EmpiStorageInterceptor;
import ca.uhn.fhir.jpa.empi.interceptor.IEmpiStorageInterceptor;
import ca.uhn.fhir.jpa.empi.svc.EmpiClearSvcImpl;
import ca.uhn.fhir.jpa.empi.svc.EmpiControllerSvcImpl;
import ca.uhn.fhir.jpa.empi.svc.EmpiEidUpdateService;
import ca.uhn.fhir.jpa.empi.svc.EmpiLinkQuerySvcImpl;
import ca.uhn.fhir.jpa.empi.svc.EmpiLinkSvcImpl;
import ca.uhn.fhir.jpa.empi.svc.EmpiLinkUpdaterSvcImpl;
import ca.uhn.fhir.jpa.empi.svc.EmpiMatchFinderSvcImpl;
import ca.uhn.fhir.jpa.empi.svc.EmpiMatchLinkSvc;
import ca.uhn.fhir.jpa.empi.svc.EmpiPersonDeletingSvc;
import ca.uhn.fhir.jpa.empi.svc.GoldenResourceMergerSvcImpl;
import ca.uhn.fhir.jpa.empi.svc.EmpiResourceDaoSvc;
import ca.uhn.fhir.jpa.empi.svc.EmpiResourceFilteringSvc;
import ca.uhn.fhir.jpa.empi.svc.candidate.EmpiCandidateSearchCriteriaBuilderSvc;
import ca.uhn.fhir.jpa.empi.svc.candidate.EmpiCandidateSearchSvc;
import ca.uhn.fhir.jpa.empi.svc.candidate.EmpiSourceResourceFindingSvc;
import ca.uhn.fhir.jpa.empi.svc.candidate.FindCandidateByEidSvc;
import ca.uhn.fhir.jpa.empi.svc.candidate.FindCandidateByLinkSvc;
import ca.uhn.fhir.jpa.empi.svc.candidate.FindCandidateByScoreSvc;
import ca.uhn.fhir.rest.server.util.ISearchParamRetriever;
import ca.uhn.fhir.validation.IResourceLoader;
import org.slf4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmpiConsumerConfig {
	private static final Logger ourLog = Logs.getEmpiTroubleshootingLog();

	@Bean
	IEmpiStorageInterceptor empiStorageInterceptor() {
		return new EmpiStorageInterceptor();
	}

	@Bean
	EmpiQueueConsumerLoader empiQueueConsumerLoader() {
		return new EmpiQueueConsumerLoader();
	}

	@Bean
	EmpiMessageHandler empiMessageHandler() {
		return new EmpiMessageHandler();
	}

	@Bean
	EmpiMatchLinkSvc empiMatchLinkSvc() {
		return new EmpiMatchLinkSvc();
	}

	@Bean
	EmpiEidUpdateService eidUpdateService() {
		return new EmpiEidUpdateService();
	}

	@Bean
	EmpiResourceDaoSvc empiResourceDaoSvc() {
		return new EmpiResourceDaoSvc();
	}

	@Bean
	IEmpiLinkSvc empiLinkSvc() {
		return new EmpiLinkSvcImpl();
	}

	@Bean
	PersonHelper personHelper(FhirContext theFhirContext) {
		return new PersonHelper(theFhirContext);
	}

	@Bean
	MessageHelper messageHelper(IEmpiSettings theEmpiSettings, FhirContext theFhirContext) {
		return new MessageHelper(theEmpiSettings, theFhirContext);
	}

	@Bean
	EmpiSubscriptionLoader empiSubscriptionLoader() {
		return new EmpiSubscriptionLoader();
	}

	@Bean
	EmpiSearchParameterLoader empiSearchParameterLoader() {
		return new EmpiSearchParameterLoader();
	}

	@Bean
	EmpiSourceResourceFindingSvc empiPersonFindingSvc() {
		return new EmpiSourceResourceFindingSvc();
	}

	@Bean
	FindCandidateByEidSvc findCandidateByEidSvc() {
		return new FindCandidateByEidSvc();
	}

	@Bean
	FindCandidateByLinkSvc findCandidateByLinkSvc() {
		return new FindCandidateByLinkSvc();
	}

	@Bean
	FindCandidateByScoreSvc findCandidateByScoreSvc() {
		return new FindCandidateByScoreSvc();
	}

	@Bean
	EmpiProviderLoader empiProviderLoader() {
		return new EmpiProviderLoader();
	}

	@Bean
	EmpiRuleValidator empiRuleValidator(FhirContext theFhirContext, ISearchParamRetriever theSearchParamRetriever) {
		return new EmpiRuleValidator(theFhirContext, theSearchParamRetriever);
	}

	@Bean
	IEmpiMatchFinderSvc empiMatchFinderSvc() {
		return new EmpiMatchFinderSvcImpl();
	}

	@Bean
	IGoldenResourceMergerSvc empiPersonMergerSvc() {
		return new GoldenResourceMergerSvcImpl();
	}


	@Bean
	IEmpiLinkQuerySvc empiLinkQuerySvc() {
		return new EmpiLinkQuerySvcImpl();
	}

	@Bean
	IEmpiExpungeSvc empiResetSvc(EmpiLinkDaoSvc theEmpiLinkDaoSvc, EmpiPersonDeletingSvc theEmpiPersonDeletingSvcImpl) {
		return new EmpiClearSvcImpl(theEmpiLinkDaoSvc, theEmpiPersonDeletingSvcImpl);
	}

	@Bean
	EmpiCandidateSearchSvc empiCandidateSearchSvc() {
		return new EmpiCandidateSearchSvc();
	}

	@Bean
	EmpiCandidateSearchCriteriaBuilderSvc empiCriteriaBuilderSvc() {
		return new EmpiCandidateSearchCriteriaBuilderSvc();
	}

	@Bean
	EmpiResourceMatcherSvc empiResourceComparatorSvc(FhirContext theFhirContext, IEmpiSettings theEmpiConfig) {
		return new EmpiResourceMatcherSvc(theFhirContext, theEmpiConfig);
	}

	@Bean
	EIDHelper eidHelper(FhirContext theFhirContext, IEmpiSettings theEmpiConfig) {
		return new EIDHelper(theFhirContext, theEmpiConfig);
	}

	@Bean
	EmpiLinkDaoSvc empiLinkDaoSvc() {
		return new EmpiLinkDaoSvc();
	}

	@Bean
	EmpiLinkFactory empiLinkFactory(IEmpiSettings theEmpiSettings) {
		return new EmpiLinkFactory(theEmpiSettings);
	}

	@Bean
	IEmpiLinkUpdaterSvc manualLinkUpdaterSvc() {
		return new EmpiLinkUpdaterSvcImpl();
	}

	@Bean
	EmpiLoader empiLoader() {
		return new EmpiLoader();
	}

	@Bean
	EmpiLinkDeleteSvc empiLinkDeleteSvc() {
		return new EmpiLinkDeleteSvc();
	}

	@Bean
	EmpiResourceFilteringSvc empiResourceFilteringSvc() {
		return new EmpiResourceFilteringSvc();
	}

	@Bean
	EmpiControllerHelper empiProviderHelper(FhirContext theFhirContext, IResourceLoader theResourceLoader, IEmpiSettings theEmpiSettings, MessageHelper messageHelper) {
		return new EmpiControllerHelper(theFhirContext, theResourceLoader, theEmpiSettings, messageHelper);
	}

	@Bean
	IEmpiControllerSvc empiControllerSvc() {
		return new EmpiControllerSvcImpl();
	}
}
