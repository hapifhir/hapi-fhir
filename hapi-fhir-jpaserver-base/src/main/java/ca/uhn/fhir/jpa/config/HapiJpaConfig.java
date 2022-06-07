package ca.uhn.fhir.jpa.config;

/*-
 * #%L
 * HAPI FHIR JPA Server
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

import ca.uhn.fhir.jpa.api.IDaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.batch.config.NonPersistedBatchConfigurer;
import ca.uhn.fhir.jpa.config.util.ResourceCountCacheUtil;
import ca.uhn.fhir.jpa.config.util.ValidationSupportConfigUtil;
import ca.uhn.fhir.jpa.dao.FulltextSearchSvcImpl;
import ca.uhn.fhir.jpa.dao.IFulltextSearchSvc;
import ca.uhn.fhir.jpa.provider.DaoRegistryResourceSupportedSvc;
import ca.uhn.fhir.jpa.search.DatabaseBackedPagingProvider;
import ca.uhn.fhir.jpa.search.IStaleSearchDeletingSvc;
import ca.uhn.fhir.jpa.search.StaleSearchDeletingSvcImpl;
import ca.uhn.fhir.jpa.util.ResourceCountCache;
import ca.uhn.fhir.jpa.validation.JpaValidationSupportChain;
import ca.uhn.fhir.rest.api.IResourceSupportedSvc;
import org.hl7.fhir.common.hapi.validation.support.CachingValidationSupport;
import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

@Configuration
@Import({JpaConfig.class})
public class HapiJpaConfig {
	@Bean
	public IFulltextSearchSvc fullTextSearchSvc() {
		return new FulltextSearchSvcImpl();
	}

	@Bean
	public IStaleSearchDeletingSvc staleSearchDeletingSvc() {
		return new StaleSearchDeletingSvcImpl();
	}

	@Primary
	@Bean
	public CachingValidationSupport validationSupportChain(JpaValidationSupportChain theJpaValidationSupportChain) {
		return ValidationSupportConfigUtil.newCachingValidationSupport(theJpaValidationSupportChain);
	}

	@Bean
	public BatchConfigurer batchConfigurer() {
		return new NonPersistedBatchConfigurer();
	}

	@Bean
	public DatabaseBackedPagingProvider databaseBackedPagingProvider() {
		return new DatabaseBackedPagingProvider();
	}

	@Bean
	public IResourceSupportedSvc resourceSupportedSvc(IDaoRegistry theDaoRegistry) {
		return new DaoRegistryResourceSupportedSvc(theDaoRegistry);
	}

	@Bean(name = "myResourceCountsCache")
	public ResourceCountCache resourceCountsCache(IFhirSystemDao<?, ?> theSystemDao) {
		return ResourceCountCacheUtil.newResourceCountCache(theSystemDao);
	}
}
