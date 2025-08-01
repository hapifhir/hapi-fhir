/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.jpa.config.r4;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.util.Batch2TaskHelper;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.jpa.api.IDaoRegistry;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.config.GeneratedDaoAndResourceProviderConfigR4;
import ca.uhn.fhir.jpa.config.JpaConfig;
import ca.uhn.fhir.jpa.dao.ITransactionProcessorVersionAdapter;
import ca.uhn.fhir.jpa.dao.r4.TransactionProcessorVersionAdapterR4;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.graphql.GraphQLProvider;
import ca.uhn.fhir.jpa.graphql.GraphQLProviderWithIntrospection;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.provider.IReplaceReferencesSvc;
import ca.uhn.fhir.jpa.provider.JpaSystemProvider;
import ca.uhn.fhir.jpa.provider.merge.MergeValidationService;
import ca.uhn.fhir.jpa.provider.merge.PatientMergeProvider;
import ca.uhn.fhir.jpa.provider.merge.ResourceMergeService;
import ca.uhn.fhir.jpa.provider.merge.ResourceUndoMergeService;
import ca.uhn.fhir.jpa.term.TermLoaderSvcImpl;
import ca.uhn.fhir.jpa.term.TermVersionAdapterSvcR4;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc;
import ca.uhn.fhir.jpa.term.api.ITermDeferredStorageSvc;
import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;
import ca.uhn.fhir.jpa.term.api.ITermVersionAdapterSvc;
import ca.uhn.fhir.merge.MergeProvenanceSvc;
import ca.uhn.fhir.replacereferences.PreviousResourceVersionRestorer;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Meta;
import org.hl7.fhir.utilities.graphql.IGraphQLStorageServices;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@Import({FhirContextR4Config.class, GeneratedDaoAndResourceProviderConfigR4.class, JpaConfig.class})
public class JpaR4Config {

	@Bean
	public ITermVersionAdapterSvc terminologyVersionAdapterSvc() {
		return new TermVersionAdapterSvcR4();
	}

	@Bean
	public ITransactionProcessorVersionAdapter transactionProcessorVersionFacade() {
		return new TransactionProcessorVersionAdapterR4();
	}

	@Bean(name = JpaConfig.GRAPHQL_PROVIDER_NAME)
	@Lazy
	public GraphQLProvider graphQLProvider(
			FhirContext theFhirContext,
			IGraphQLStorageServices theGraphqlStorageServices,
			IValidationSupport theValidationSupport,
			ISearchParamRegistry theSearchParamRegistry,
			IDaoRegistry theDaoRegistry) {
		return new GraphQLProviderWithIntrospection(
				theFhirContext,
				theValidationSupport,
				theGraphqlStorageServices,
				theSearchParamRegistry,
				theDaoRegistry);
	}

	@Bean(name = "mySystemDaoR4")
	public IFhirSystemDao<Bundle, Meta> systemDaoR4() {
		ca.uhn.fhir.jpa.dao.r4.FhirSystemDaoR4 retVal = new ca.uhn.fhir.jpa.dao.r4.FhirSystemDaoR4();
		return retVal;
	}

	@Bean(name = "mySystemProviderR4")
	public JpaSystemProvider<Bundle, Meta> systemProviderR4(FhirContext theFhirContext) {
		JpaSystemProvider<Bundle, Meta> retVal = new JpaSystemProvider<>();
		retVal.setContext(theFhirContext);
		retVal.setDao(systemDaoR4());
		return retVal;
	}

	@Bean
	public ITermLoaderSvc termLoaderService(
			ITermDeferredStorageSvc theDeferredStorageSvc, ITermCodeSystemStorageSvc theCodeSystemStorageSvc) {
		return new TermLoaderSvcImpl(theDeferredStorageSvc, theCodeSystemStorageSvc);
	}

	@Bean
	public MergeValidationService mergeValidationService(FhirContext theFhirContext, DaoRegistry theDaoRegistry) {
		return new MergeValidationService(theFhirContext, theDaoRegistry);
	}

	@Bean
	public MergeProvenanceSvc mergeProvenanceSvc(DaoRegistry theDaoRegistry) {
		return new MergeProvenanceSvc(theDaoRegistry);
	}

	@Bean
	public ResourceMergeService resourceMergeService(
			DaoRegistry theDaoRegistry,
			IReplaceReferencesSvc theReplaceReferencesSvc,
			HapiTransactionService theHapiTransactionService,
			IRequestPartitionHelperSvc theRequestPartitionHelperSvc,
			IJobCoordinator theJobCoordinator,
			Batch2TaskHelper theBatch2TaskHelper,
			JpaStorageSettings theStorageSettings,
			MergeValidationService theMergeValidationService,
			MergeProvenanceSvc theMergeProvenanceSvc) {

		return new ResourceMergeService(
				theStorageSettings,
				theDaoRegistry,
				theReplaceReferencesSvc,
				theHapiTransactionService,
				theRequestPartitionHelperSvc,
				theJobCoordinator,
				theBatch2TaskHelper,
				theMergeValidationService,
				theMergeProvenanceSvc);
	}

	@Bean
	public ResourceUndoMergeService resourceUndoMergeService(
			DaoRegistry theDaoRegistry,
			MergeProvenanceSvc theMergeProvenanceSvc,
			PreviousResourceVersionRestorer theResourceVersionRestorer,
			MergeValidationService theMergeValidationService,
			IRequestPartitionHelperSvc theRequestPartitionHelperSvc) {
		return new ResourceUndoMergeService(
				theDaoRegistry,
				theMergeProvenanceSvc,
				theResourceVersionRestorer,
				theMergeValidationService,
				theRequestPartitionHelperSvc);
	}

	@Bean
	public PatientMergeProvider patientMergeProvider(
			FhirContext theFhirContext,
			DaoRegistry theDaoRegistry,
			ResourceMergeService theResourceMergeService,
			ResourceUndoMergeService theResourceUndoMergeService,
			IInterceptorBroadcaster theInterceptorBroadcaster) {

		return new PatientMergeProvider(
				theFhirContext,
				theDaoRegistry,
				theResourceMergeService,
				theResourceUndoMergeService,
				theInterceptorBroadcaster);
	}
}
