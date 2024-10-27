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
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.svc.IGoldenResourceSearchSvc;
import ca.uhn.fhir.jpa.mdm.broker.MdmMessageHandler;
import ca.uhn.fhir.jpa.mdm.broker.MdmMessageKeySvc;
import ca.uhn.fhir.jpa.mdm.broker.MdmQueueConsumerLoader;
import ca.uhn.fhir.jpa.mdm.dao.MdmLinkDaoSvc;
import ca.uhn.fhir.jpa.mdm.svc.BlockRuleEvaluationSvcImpl;
import ca.uhn.fhir.jpa.mdm.svc.GoldenResourceMergerSvcImpl;
import ca.uhn.fhir.jpa.mdm.svc.GoldenResourceSearchSvcImpl;
import ca.uhn.fhir.jpa.mdm.svc.IMdmModelConverterSvc;
import ca.uhn.fhir.jpa.mdm.svc.MdmControllerSvcImpl;
import ca.uhn.fhir.jpa.mdm.svc.MdmEidUpdateService;
import ca.uhn.fhir.jpa.mdm.svc.MdmLinkCreateSvcImpl;
import ca.uhn.fhir.jpa.mdm.svc.MdmLinkQuerySvcImplSvc;
import ca.uhn.fhir.jpa.mdm.svc.MdmLinkSvcImpl;
import ca.uhn.fhir.jpa.mdm.svc.MdmLinkUpdaterSvcImpl;
import ca.uhn.fhir.jpa.mdm.svc.MdmMatchFinderSvcImpl;
import ca.uhn.fhir.jpa.mdm.svc.MdmMatchLinkSvc;
import ca.uhn.fhir.jpa.mdm.svc.MdmModelConverterSvcImpl;
import ca.uhn.fhir.jpa.mdm.svc.MdmResourceDaoSvcImpl;
import ca.uhn.fhir.jpa.mdm.svc.MdmResourceFilteringSvc;
import ca.uhn.fhir.jpa.mdm.svc.candidate.CandidateSearcher;
import ca.uhn.fhir.jpa.mdm.svc.candidate.FindCandidateByEidSvc;
import ca.uhn.fhir.jpa.mdm.svc.candidate.FindCandidateByExampleSvc;
import ca.uhn.fhir.jpa.mdm.svc.candidate.FindCandidateByLinkSvc;
import ca.uhn.fhir.jpa.mdm.svc.candidate.MdmCandidateSearchCriteriaBuilderSvc;
import ca.uhn.fhir.jpa.mdm.svc.candidate.MdmCandidateSearchSvc;
import ca.uhn.fhir.jpa.mdm.svc.candidate.MdmGoldenResourceFindingSvc;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelFactory;
import ca.uhn.fhir.mdm.api.IGoldenResourceMergerSvc;
import ca.uhn.fhir.mdm.api.IMdmControllerSvc;
import ca.uhn.fhir.mdm.api.IMdmLinkCreateSvc;
import ca.uhn.fhir.mdm.api.IMdmLinkQuerySvc;
import ca.uhn.fhir.mdm.api.IMdmLinkSvc;
import ca.uhn.fhir.mdm.api.IMdmLinkUpdaterSvc;
import ca.uhn.fhir.mdm.api.IMdmMatchFinderSvc;
import ca.uhn.fhir.mdm.api.IMdmResourceDaoSvc;
import ca.uhn.fhir.mdm.api.IMdmSettings;
import ca.uhn.fhir.mdm.batch2.MdmBatch2Config;
import ca.uhn.fhir.mdm.blocklist.svc.IBlockListRuleProvider;
import ca.uhn.fhir.mdm.blocklist.svc.IBlockRuleEvaluationSvc;
import ca.uhn.fhir.mdm.dao.IMdmLinkImplFactory;
import ca.uhn.fhir.mdm.dao.MdmLinkFactory;
import ca.uhn.fhir.mdm.interceptor.IMdmStorageInterceptor;
import ca.uhn.fhir.mdm.interceptor.MdmStorageInterceptor;
import ca.uhn.fhir.mdm.log.Logs;
import ca.uhn.fhir.mdm.provider.MdmControllerHelper;
import ca.uhn.fhir.mdm.provider.MdmProviderLoader;
import ca.uhn.fhir.mdm.svc.MdmSearchParamSvc;
import ca.uhn.fhir.mdm.util.EIDHelper;
import ca.uhn.fhir.mdm.util.MdmPartitionHelper;
import ca.uhn.fhir.mdm.util.MessageHelper;
import ca.uhn.fhir.validation.IResourceLoader;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({MdmCommonConfig.class, MdmSurvivorshipConfig.class, MdmBatch2Config.class})
public class MdmConsumerConfig {

	private static final Logger ourLog = Logs.getMdmTroubleshootingLog();

	@Bean
	IMdmStorageInterceptor mdmStorageInterceptor() {
		return new MdmStorageInterceptor();
	}

	@Bean
	MdmQueueConsumerLoader mdmQueueConsumerLoader(
			IChannelFactory theChannelFactory, IMdmSettings theMdmSettings, MdmMessageHandler theMdmMessageHandler) {
		return new MdmQueueConsumerLoader(theChannelFactory, theMdmSettings, theMdmMessageHandler);
	}

	@Bean
	MdmMessageHandler mdmMessageHandler() {
		return new MdmMessageHandler();
	}

	@Bean
	MdmMessageKeySvc mdmMessageKeySvc() {
		return new MdmMessageKeySvc();
	}

	@Bean
	MdmMatchLinkSvc mdmMatchLinkSvc() {
		return new MdmMatchLinkSvc();
	}

	@Bean
	IBlockRuleEvaluationSvc blockRuleEvaluationSvc(
			@Autowired FhirContext theContext, @Autowired(required = false) IBlockListRuleProvider theProvider) {
		return new BlockRuleEvaluationSvcImpl(theContext, theProvider);
	}

	@Bean
	MdmEidUpdateService eidUpdateService() {
		return new MdmEidUpdateService();
	}

	@Bean
	IMdmResourceDaoSvc mdmResourceDaoSvc() {
		return new MdmResourceDaoSvcImpl();
	}

	@Bean
	IMdmLinkSvc mdmLinkSvc() {
		return new MdmLinkSvcImpl();
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
	IMdmMatchFinderSvc mdmMatchFinderSvc() {
		return new MdmMatchFinderSvcImpl();
	}

	@Bean
	IGoldenResourceMergerSvc mdmGoldenResourceMergerSvc() {
		return new GoldenResourceMergerSvcImpl();
	}

	@Bean
	IMdmLinkQuerySvc mdmLinkQuerySvc() {
		return new MdmLinkQuerySvcImplSvc();
	}

	@Bean
	IMdmModelConverterSvc mdmModelConverterSvc() {
		return new MdmModelConverterSvcImpl();
	}

	@Bean
	MdmCandidateSearchSvc mdmCandidateSearchSvc() {
		return new MdmCandidateSearchSvc();
	}

	@Bean
	CandidateSearcher candidateSearcher(
			DaoRegistry theDaoRegistry, IMdmSettings theMdmSettings, MdmSearchParamSvc theMdmSearchParamSvc) {
		return new CandidateSearcher(theDaoRegistry, theMdmSettings, theMdmSearchParamSvc);
	}

	@Bean
	MdmCandidateSearchCriteriaBuilderSvc mdmCriteriaBuilderSvc() {
		return new MdmCandidateSearchCriteriaBuilderSvc();
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
	MdmLinkFactory mdmLinkFactory(IMdmSettings theMdmSettings, IMdmLinkImplFactory theMdmLinkImplFactory) {
		return new MdmLinkFactory(theMdmSettings, theMdmLinkImplFactory);
	}

	@Bean
	IMdmLinkUpdaterSvc mdmLinkUpdaterSvc() {
		return new MdmLinkUpdaterSvcImpl();
	}

	@Bean
	IMdmLinkCreateSvc mdmLinkCreateSvc() {
		return new MdmLinkCreateSvcImpl();
	}

	@Bean
	MdmLoader mdmLoader() {
		return new MdmLoader();
	}

	@Bean
	MdmResourceFilteringSvc mdmResourceFilteringSvc() {
		return new MdmResourceFilteringSvc();
	}

	@Bean
	MdmControllerHelper mdmProviderHelper(
			FhirContext theFhirContext,
			IResourceLoader theResourceLoader,
			IMdmSettings theMdmSettings,
			IMdmMatchFinderSvc theMdmMatchFinderSvc,
			MessageHelper messageHelper,
			IRequestPartitionHelperSvc partitionHelperSvc) {
		return new MdmControllerHelper(
				theFhirContext,
				theResourceLoader,
				theMdmMatchFinderSvc,
				theMdmSettings,
				messageHelper,
				partitionHelperSvc);
	}

	@Bean
	IMdmControllerSvc mdmControllerSvc() {
		return new MdmControllerSvcImpl();
	}

	@Bean
	MdmPartitionHelper mdmPartitionHelper(MessageHelper theMessageHelper, IMdmSettings theMdmSettings) {
		return new MdmPartitionHelper(theMessageHelper, theMdmSettings);
	}

	@Bean
	public IGoldenResourceSearchSvc goldenResourceSearchSvc() {
		return new GoldenResourceSearchSvcImpl();
	}
}
