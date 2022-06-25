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
import ca.uhn.fhir.jpa.dao.search.HSearchParamHelper;
import ca.uhn.fhir.jpa.dao.search.HSearchParamHelperNumber;
import ca.uhn.fhir.jpa.dao.search.HSearchParamHelperProviderImpl;
import ca.uhn.fhir.jpa.dao.search.HSearchParamHelperQuantity;
import ca.uhn.fhir.jpa.dao.search.HSearchParamHelperReference;
import ca.uhn.fhir.jpa.dao.search.HSearchParamHelperToken;
import ca.uhn.fhir.jpa.dao.search.HSearchParamHelperUri;
import ca.uhn.fhir.jpa.dao.search.IHSearchParamHelperProvider;
import ca.uhn.fhir.jpa.dao.search.IPrefixedNumberPredicateHelperImpl;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.dao.search.HSearchSortHelperImpl;
import ca.uhn.fhir.jpa.dao.search.IHSearchSortHelper;
import ca.uhn.fhir.jpa.provider.DaoRegistryResourceSupportedSvc;
import ca.uhn.fhir.jpa.search.DatabaseBackedPagingProvider;
import ca.uhn.fhir.jpa.search.IStaleSearchDeletingSvc;
import ca.uhn.fhir.jpa.search.StaleSearchDeletingSvcImpl;
import ca.uhn.fhir.jpa.util.ResourceCountCache;
import ca.uhn.fhir.jpa.validation.JpaValidationSupportChain;
import ca.uhn.fhir.rest.api.IResourceSupportedSvc;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import org.hl7.fhir.common.hapi.validation.support.CachingValidationSupport;
import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

import java.util.List;
import java.util.Map;

import static ca.uhn.fhir.jpa.model.search.HibernateSearchIndexWriter.IDX_STRING_LOWER;
import static ca.uhn.fhir.jpa.model.search.HibernateSearchIndexWriter.NESTED_SEARCH_PARAM_ROOT;
import static ca.uhn.fhir.jpa.model.search.HibernateSearchIndexWriter.QTY_PARAM_NAME;
import static ca.uhn.fhir.jpa.model.search.HibernateSearchIndexWriter.QTY_VALUE;
import static ca.uhn.fhir.jpa.model.search.HibernateSearchIndexWriter.QTY_VALUE_NORM;
import static ca.uhn.fhir.jpa.model.search.HibernateSearchIndexWriter.SEARCH_PARAM_ROOT;
import static ca.uhn.fhir.jpa.model.search.HibernateSearchIndexWriter.URI_VALUE;

@Configuration
@Import({JpaConfig.class})
public class HapiJpaConfig {

	@Autowired
	private ISearchParamRegistry mySearchParamRegistry;

	@Bean
	public IHSearchSortHelper extendedFulltextSortHelper() {
		return new HSearchSortHelperImpl(mySearchParamRegistry);
	}

	@Autowired
	private ModelConfig myModelConfig;

	@Bean
	public IPrefixedNumberPredicateHelperImpl prefixedNumberPredicateHelper() {
		return new IPrefixedNumberPredicateHelperImpl();
	}

	@Bean
	public IHSearchParamHelperProvider hSearchParamHelperProvider() {

		IPrefixedNumberPredicateHelperImpl prefixedNumberPredicateHelper = prefixedNumberPredicateHelper();

		// register specific parameter helpers in parent map
		HSearchParamHelper.registerChildHelper( new HSearchParamHelperToken() ) ;
		HSearchParamHelper.registerChildHelper( new HSearchParamHelperNumber( prefixedNumberPredicateHelper ) ) ;
		HSearchParamHelper.registerChildHelper( new HSearchParamHelperUri() ) ;
		HSearchParamHelper.registerChildHelper( new HSearchParamHelperReference() ) ;
		HSearchParamHelper.registerChildHelper( new HSearchParamHelperQuantity( prefixedNumberPredicateHelper, myModelConfig ) ) ;
//		HSearchParamHelper.registerChildHelper( new HSearchParamHelperDate() ) ;

		return new HSearchParamHelperProviderImpl();
	}

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
