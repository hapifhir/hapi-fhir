package ca.uhn.fhir.jpa.mdm.config;

/*-
 * #%L
 * HAPI FHIR JPA Server - Master Data Management
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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
import ca.uhn.fhir.mdm.api.IMdmControllerSvc;
import ca.uhn.fhir.mdm.api.IMdmExpungeSvc;
import ca.uhn.fhir.mdm.api.IMdmLinkQuerySvc;
import ca.uhn.fhir.mdm.api.IMdmLinkSvc;
import ca.uhn.fhir.mdm.api.IMdmLinkUpdaterSvc;
import ca.uhn.fhir.mdm.api.IMdmMatchFinderSvc;
import ca.uhn.fhir.mdm.api.IGoldenResourceMergerSvc;
import ca.uhn.fhir.mdm.api.IMdmSettings;
import ca.uhn.fhir.mdm.log.Logs;
import ca.uhn.fhir.mdm.provider.MdmControllerHelper;
import ca.uhn.fhir.mdm.provider.MdmProviderLoader;
import ca.uhn.fhir.mdm.rules.config.MdmRuleValidator;
import ca.uhn.fhir.mdm.rules.svc.MdmResourceMatcherSvc;
import ca.uhn.fhir.mdm.util.EIDHelper;
import ca.uhn.fhir.mdm.util.MessageHelper;
import ca.uhn.fhir.mdm.util.GoldenResourceHelper;
import ca.uhn.fhir.jpa.dao.mdm.MdmLinkDeleteSvc;
import ca.uhn.fhir.jpa.mdm.broker.MdmMessageHandler;
import ca.uhn.fhir.jpa.mdm.broker.MdmQueueConsumerLoader;
import ca.uhn.fhir.jpa.mdm.dao.MdmLinkDaoSvc;
import ca.uhn.fhir.jpa.mdm.dao.MdmLinkFactory;
import ca.uhn.fhir.jpa.mdm.interceptor.MdmStorageInterceptor;
import ca.uhn.fhir.jpa.mdm.interceptor.IMdmStorageInterceptor;
import ca.uhn.fhir.jpa.mdm.svc.MdmClearSvcImpl;
import ca.uhn.fhir.jpa.mdm.svc.MdmControllerSvcImpl;
import ca.uhn.fhir.jpa.mdm.svc.MdmEidUpdateService;
import ca.uhn.fhir.jpa.mdm.svc.MdmLinkQuerySvcImpl;
import ca.uhn.fhir.jpa.mdm.svc.MdmLinkSvcImpl;
import ca.uhn.fhir.jpa.mdm.svc.MdmLinkUpdaterSvcImpl;
import ca.uhn.fhir.jpa.mdm.svc.MdmMatchFinderSvcImpl;
import ca.uhn.fhir.jpa.mdm.svc.MdmMatchLinkSvc;
import ca.uhn.fhir.jpa.mdm.svc.MdmGoldenResourceDeletingSvc;
import ca.uhn.fhir.jpa.mdm.svc.GoldenResourceMergerSvcImpl;
import ca.uhn.fhir.jpa.mdm.svc.MdmResourceDaoSvc;
import ca.uhn.fhir.jpa.mdm.svc.MdmResourceFilteringSvc;
import ca.uhn.fhir.jpa.mdm.svc.candidate.MdmCandidateSearchCriteriaBuilderSvc;
import ca.uhn.fhir.jpa.mdm.svc.candidate.MdmCandidateSearchSvc;
import ca.uhn.fhir.jpa.mdm.svc.candidate.MdmGoldenResourceFindingSvc;
import ca.uhn.fhir.jpa.mdm.svc.candidate.FindCandidateByEidSvc;
import ca.uhn.fhir.jpa.mdm.svc.candidate.FindCandidateByLinkSvc;
import ca.uhn.fhir.jpa.mdm.svc.candidate.FindCandidateByExampleSvc;
import ca.uhn.fhir.rest.server.util.ISearchParamRetriever;
import ca.uhn.fhir.validation.IResourceLoader;
import org.slf4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MdmConsumerConfig {
	private static final Logger ourLog = Logs.getMdmTroubleshootingLog();

	@Bean
	IMdmStorageInterceptor mdmStorageInterceptor() {
		return new MdmStorageInterceptor();
	}

	@Bean
	MdmQueueConsumerLoader mdmQueueConsumerLoader() {
		return new MdmQueueConsumerLoader();
	}

	@Bean
	MdmMessageHandler mdmMessageHandler() {
		return new MdmMessageHandler();
	}

	@Bean
	MdmMatchLinkSvc mdmMatchLinkSvc() {
		return new MdmMatchLinkSvc();
	}

	@Bean
	MdmEidUpdateService eidUpdateService() {
		return new MdmEidUpdateService();
	}

	@Bean
	MdmResourceDaoSvc mdmResourceDaoSvc() {
		return new MdmResourceDaoSvc();
	}

	@Bean
	IMdmLinkSvc mdmLinkSvc() {
		return new MdmLinkSvcImpl();
	}

	@Bean
	GoldenResourceHelper goldenResourceHelper(FhirContext theFhirContext) {
		return new GoldenResourceHelper(theFhirContext);
	}

	@Bean
	MessageHelper messageHelper(IMdmSettings theMdmSettings, FhirContext theFhirContext) {
		return new MessageHelper(theMdmSettings, theFhirContext);
	}

	@Bean
	MdmSubscriptionLoader mdmSubscriptionLoader() {
		return new MdmSubscriptionLoader();
	}

	@Bean
	MdmGoldenResourceFindingSvc mdmGoldenResourceFindingSvc() {
		return new MdmGoldenResourceFindingSvc();
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
	FindCandidateByExampleSvc findCandidateByScoreSvc() {
		return new FindCandidateByExampleSvc();
	}

	@Bean
	MdmProviderLoader mdmProviderLoader() {
		return new MdmProviderLoader();
	}

	@Bean
	MdmRuleValidator mdmRuleValidator(FhirContext theFhirContext, ISearchParamRetriever theSearchParamRetriever) {
		return new MdmRuleValidator(theFhirContext, theSearchParamRetriever);
	}

	@Bean
	IMdmMatchFinderSvc mdmMatchFinderSvc() {
		return new MdmMatchFinderSvcImpl();
	}

	@Bean
	IGoldenResourceMergerSvc mdmGoldenResourceMergerSvc() {
		return new GoldenResourceMergerSvcImpl();
	}


	@Bean
	IMdmLinkQuerySvc mdmLinkQuerySvc() {
		return new MdmLinkQuerySvcImpl();
	}

	@Bean
	IMdmExpungeSvc mdmResetSvc(MdmLinkDaoSvc theMdmLinkDaoSvc, MdmGoldenResourceDeletingSvc theDeletingSvc, IMdmSettings theIMdmSettings) {
		return new MdmClearSvcImpl(theMdmLinkDaoSvc, theDeletingSvc, theIMdmSettings);
	}

	@Bean
	MdmCandidateSearchSvc mdmCandidateSearchSvc() {
		return new MdmCandidateSearchSvc();
	}

	@Bean
	MdmCandidateSearchCriteriaBuilderSvc mdmCriteriaBuilderSvc() {
		return new MdmCandidateSearchCriteriaBuilderSvc();
	}

	@Bean
	MdmResourceMatcherSvc mdmResourceComparatorSvc(FhirContext theFhirContext, IMdmSettings theMdmSettings) {
		return new MdmResourceMatcherSvc(theFhirContext, theMdmSettings);
	}

	@Bean
	EIDHelper eidHelper(FhirContext theFhirContext, IMdmSettings theMdmSettings) {
		return new EIDHelper(theFhirContext, theMdmSettings);
	}

	@Bean
	MdmLinkDaoSvc mdmLinkDaoSvc() {
		return new MdmLinkDaoSvc();
	}

	@Bean
	MdmLinkFactory mdmLinkFactory(IMdmSettings theMdmSettings) {
		return new MdmLinkFactory(theMdmSettings);
	}

	@Bean
	IMdmLinkUpdaterSvc mdmLinkUpdaterSvc() {
		return new MdmLinkUpdaterSvcImpl();
	}

	@Bean
	MdmLoader mdmLoader() {
		return new MdmLoader();
	}

	@Bean
	MdmLinkDeleteSvc mdmLinkDeleteSvc() {
		return new MdmLinkDeleteSvc();
	}

	@Bean
	MdmResourceFilteringSvc mdmResourceFilteringSvc() {
		return new MdmResourceFilteringSvc();
	}

	@Bean
	MdmControllerHelper mdmProviderHelper(FhirContext theFhirContext, IResourceLoader theResourceLoader, IMdmSettings theMdmSettings, MessageHelper messageHelper) {
		return new MdmControllerHelper(theFhirContext, theResourceLoader, theMdmSettings, messageHelper);
	}

	@Bean
	IMdmControllerSvc mdmControllerSvc() {
		return new MdmControllerSvcImpl();
	}
}
