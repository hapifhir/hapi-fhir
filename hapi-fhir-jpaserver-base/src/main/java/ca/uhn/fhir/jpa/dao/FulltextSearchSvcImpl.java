package ca.uhn.fhir.jpa.dao;

/*
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.dao.data.IForcedIdDao;
import ca.uhn.fhir.jpa.dao.search.ExtendedLuceneClauseBuilder;
import ca.uhn.fhir.jpa.dao.search.ExtendedLuceneIndexExtractor;
import ca.uhn.fhir.jpa.dao.search.ExtendedLuceneSearchBuilder;
import ca.uhn.fhir.jpa.dao.search.LastNOperation;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.search.ExtendedLuceneIndexData;
import ca.uhn.fhir.jpa.search.autocomplete.ValueSetAutocompleteOptions;
import ca.uhn.fhir.jpa.search.autocomplete.ValueSetAutocompleteSearch;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.extractor.ResourceIndexedSearchParams;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.hibernate.search.mapper.orm.work.SearchIndexingPlan;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Nonnull;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class FulltextSearchSvcImpl implements IFulltextSearchSvc {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FulltextSearchSvcImpl.class);
	@Autowired
	protected IForcedIdDao myForcedIdDao;
	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	private EntityManager myEntityManager;
	@Autowired
	private PlatformTransactionManager myTxManager;
	@Autowired
	private FhirContext myFhirContext;
	@Autowired
	private ISearchParamRegistry mySearchParamRegistry;
	@Autowired
	private DaoConfig myDaoConfig;
	final private ExtendedLuceneSearchBuilder myAdvancedIndexQueryBuilder = new ExtendedLuceneSearchBuilder();

	private Boolean ourDisabled;

	/**
	 * Constructor
	 */
	public FulltextSearchSvcImpl() {
		super();
	}

	public ExtendedLuceneIndexData extractLuceneIndexData(IBaseResource theResource, ResourceIndexedSearchParams theNewParams) {
		String resourceType = myFhirContext.getResourceType(theResource);
		Map<String, RuntimeSearchParam> activeSearchParams = mySearchParamRegistry.getActiveSearchParams(resourceType);
		ExtendedLuceneIndexExtractor extractor = new ExtendedLuceneIndexExtractor(myFhirContext, activeSearchParams);
		return extractor.extract(theNewParams);
	}

	@Override
	public boolean supportsSomeOf(SearchParameterMap myParams) {
		// keep this in sync with the guts of doSearch
		boolean requiresHibernateSearchAccess = myParams.containsKey(Constants.PARAM_CONTENT) || myParams.containsKey(Constants.PARAM_TEXT) || myParams.isLastN();

		requiresHibernateSearchAccess |= myDaoConfig.isAdvancedLuceneIndexing() && myAdvancedIndexQueryBuilder.isSupportsSomeOf(myParams);

		return requiresHibernateSearchAccess;
	}

	@Override
	public void reindex(ResourceTable theEntity) {
		SearchIndexingPlan plan = getSearchSession().indexingPlan();
		plan.addOrUpdate(theEntity);
	}

	private List<ResourcePersistentId> doSearch(String theResourceType, SearchParameterMap theParams, ResourcePersistentId theReferencingPid) {
		// keep this in sync with supportsSomeOf();
		SearchSession session = getSearchSession();

		List<Long> longPids = session.search(ResourceTable.class)
			// Selects are replacements for projection and convert more cleanly than the old implementation.
			.select(
				f -> f.field("myId", Long.class)
			)
			.where(
				f -> f.bool(b -> {
					ExtendedLuceneClauseBuilder builder = new ExtendedLuceneClauseBuilder(myFhirContext, b, f);

					/*
					 * Handle _content parameter (resource body content)
					 *
					 * Posterity:
					 * We do not want the HAPI-FHIR dao's to process the
					 * _content parameter, so we remove it from the map here
					 */
					List<List<IQueryParameterType>> contentAndTerms = theParams.remove(Constants.PARAM_CONTENT);
					builder.addStringTextSearch(Constants.PARAM_CONTENT, contentAndTerms);

					/*
					 * Handle _text parameter (resource narrative content)
					 *
					 * Positerity:
					 * We do not want the HAPI-FHIR dao's to process the
					 * _text parameter, so we remove it from the map here
					 */
					List<List<IQueryParameterType>> textAndTerms = theParams.remove(Constants.PARAM_TEXT);
					builder.addStringTextSearch(Constants.PARAM_TEXT, textAndTerms);

					if (theReferencingPid != null) {
						b.must(f.match().field("myResourceLinksField").matching(theReferencingPid.toString()));
					}

					if (isNotBlank(theResourceType)) {
						b.must(f.match().field("myResourceType").matching(theResourceType));
					}

					/*
					 * Handle other supported parameters
					 */
					if (myDaoConfig.isAdvancedLuceneIndexing()) {
						myAdvancedIndexQueryBuilder.addAndConsumeAdvancedQueryClauses(builder, theResourceType, theParams, mySearchParamRegistry);
					}
					//DROP EARLY HERE IF BOOL IS EMPTY?

				})
			).fetchAllHits();

		return convertLongsToResourcePersistentIds(longPids);
	}

	@Nonnull
	private SearchSession getSearchSession() {
		return Search.session(myEntityManager);
	}

	private List<ResourcePersistentId> convertLongsToResourcePersistentIds(List<Long> theLongPids) {
		return theLongPids.stream()
			.map(ResourcePersistentId::new)
			.collect(Collectors.toList());
	}

	@Override
	public List<ResourcePersistentId> everything(String theResourceName, SearchParameterMap theParams, RequestDetails theRequest) {

		ResourcePersistentId pid = null;
		if (theParams.get(IAnyResource.SP_RES_ID) != null) {
			String idParamValue;
			IQueryParameterType idParam = theParams.get(IAnyResource.SP_RES_ID).get(0).get(0);
			if (idParam instanceof TokenParam) {
				TokenParam idParm = (TokenParam) idParam;
				idParamValue = idParm.getValue();
			} else {
				StringParam idParm = (StringParam) idParam;
				idParamValue = idParm.getValue();
			}
//			pid = myIdHelperService.translateForcedIdToPid_(theResourceName, idParamValue, theRequest);
		}

		ResourcePersistentId referencingPid = pid;
		List<ResourcePersistentId> retVal = doSearch(null, theParams, referencingPid);
		if (referencingPid != null) {
			retVal.add(referencingPid);
		}
		return retVal;
	}

	@Override
	public boolean isDisabled() {
		Boolean retVal = ourDisabled;

		if (retVal == null) {
			retVal = new TransactionTemplate(myTxManager).execute(t -> {
				try {
					SearchSession searchSession = getSearchSession();
					searchSession.search(ResourceTable.class);
					return Boolean.FALSE;
				} catch (Exception e) {
					ourLog.trace("FullText test failed", e);
					ourLog.debug("Hibernate Search (Lucene) appears to be disabled on this server, fulltext will be disabled");
					return Boolean.TRUE;
				}
			});
			ourDisabled = retVal;
		}

		assert retVal != null;
		return retVal;
	}

	@Transactional()
	@Override
	public List<ResourcePersistentId> search(String theResourceName, SearchParameterMap theParams) {
		return doSearch(theResourceName, theParams, null);
	}

	@Transactional()
	@Override
	public IBaseResource tokenAutocompleteValueSetSearch(ValueSetAutocompleteOptions theOptions) {

		ValueSetAutocompleteSearch autocomplete = new ValueSetAutocompleteSearch(myFhirContext, getSearchSession());

		return autocomplete.search(theOptions);
	}
	@Override
	public List<ResourcePersistentId> lastN(SearchParameterMap theParams, Integer theMaximumResults) {
		List<Long> pidList = new LastNOperation(getSearchSession(), myFhirContext, mySearchParamRegistry)
			.executeLastN(theParams, theMaximumResults);
		return convertLongsToResourcePersistentIds(pidList);
	}

}
