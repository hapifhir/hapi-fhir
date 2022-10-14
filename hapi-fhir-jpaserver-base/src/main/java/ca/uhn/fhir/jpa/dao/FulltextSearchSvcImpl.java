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
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.dao.search.ExtendedHSearchClauseBuilder;
import ca.uhn.fhir.jpa.dao.search.ExtendedHSearchIndexExtractor;
import ca.uhn.fhir.jpa.dao.search.ExtendedHSearchResourceProjection;
import ca.uhn.fhir.jpa.dao.search.ExtendedHSearchSearchBuilder;
import ca.uhn.fhir.jpa.dao.search.IHSearchSortHelper;
import ca.uhn.fhir.jpa.dao.search.LastNOperation;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.search.ExtendedHSearchIndexData;
import ca.uhn.fhir.jpa.search.autocomplete.ValueSetAutocompleteOptions;
import ca.uhn.fhir.jpa.search.autocomplete.ValueSetAutocompleteSearch;
import ca.uhn.fhir.jpa.search.builder.ISearchQueryExecutor;
import ca.uhn.fhir.jpa.search.builder.SearchQueryExecutors;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.extractor.ISearchParamExtractor;
import ca.uhn.fhir.jpa.searchparam.extractor.ResourceIndexedSearchParams;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.fhir.rest.server.util.ResourceSearchParams;
import com.google.common.collect.Ordering;
import org.hibernate.search.backend.elasticsearch.ElasticsearchExtension;
import org.hibernate.search.engine.search.predicate.dsl.PredicateFinalStep;
import org.hibernate.search.engine.search.predicate.dsl.SearchPredicateFactory;
import org.hibernate.search.engine.search.projection.dsl.CompositeProjectionOptionsStep;
import org.hibernate.search.engine.search.projection.dsl.SearchProjectionFactory;
import org.hibernate.search.engine.search.query.dsl.SearchQueryOptionsStep;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.common.EntityReference;
import org.hibernate.search.mapper.orm.search.loading.dsl.SearchLoadingOptionsStep;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.hibernate.search.mapper.orm.work.SearchIndexingPlan;
import org.hibernate.search.util.common.SearchException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Nonnull;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static ca.uhn.fhir.rest.server.BasePagingProvider.DEFAULT_MAX_PAGE_SIZE;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class FulltextSearchSvcImpl implements IFulltextSearchSvc {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FulltextSearchSvcImpl.class);
	private static final int DEFAULT_MAX_NON_PAGED_SIZE = 500;

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
	@Autowired
	ISearchParamExtractor mySearchParamExtractor;
	@Autowired
	IIdHelperService myIdHelperService;

	@Autowired
	ModelConfig myModelConfig;

	@Autowired
	private IHSearchSortHelper myExtendedFulltextSortHelper;

	final private ExtendedHSearchSearchBuilder myAdvancedIndexQueryBuilder = new ExtendedHSearchSearchBuilder();

	@Autowired(required = false)
	private IHSearchEventListener myHSearchEventListener;

	private Boolean ourDisabled;

	/**
	 * Constructor
	 */
	public FulltextSearchSvcImpl() {
		super();
	}

	public ExtendedHSearchIndexData extractLuceneIndexData(IBaseResource theResource, ResourceIndexedSearchParams theNewParams) {
		String resourceType = myFhirContext.getResourceType(theResource);
		ResourceSearchParams activeSearchParams = mySearchParamRegistry.getActiveSearchParams(resourceType);
		ExtendedHSearchIndexExtractor extractor = new ExtendedHSearchIndexExtractor(
			myDaoConfig, myFhirContext, activeSearchParams, mySearchParamExtractor, myModelConfig);
		return extractor.extract(theResource,theNewParams);
	}

	@Override
	public boolean supportsSomeOf(SearchParameterMap myParams) {

		// keep this in sync with the guts of doSearch
		boolean requiresHibernateSearchAccess = myParams.containsKey(Constants.PARAM_CONTENT) || myParams.containsKey(Constants.PARAM_TEXT) || myParams.isLastN();

		requiresHibernateSearchAccess |= myDaoConfig.isAdvancedHSearchIndexing() && myAdvancedIndexQueryBuilder.isSupportsSomeOf(myParams);

		return requiresHibernateSearchAccess;
	}

	@Override
	public void reindex(ResourceTable theEntity) {
		validateHibernateSearchIsEnabled();

		SearchIndexingPlan plan = getSearchSession().indexingPlan();
		plan.addOrUpdate(theEntity);
	}

	@Override
	public ISearchQueryExecutor searchNotScrolled(String theResourceName, SearchParameterMap theParams, Integer theMaxResultsToFetch) {
		validateHibernateSearchIsEnabled();

		return doSearch(theResourceName, theParams, null, theMaxResultsToFetch);
	}


	// keep this in sync with supportsSomeOf();
	private ISearchQueryExecutor doSearch(String theResourceType, SearchParameterMap theParams,
				ResourcePersistentId theReferencingPid, Integer theMaxResultsToFetch) {

		int offset = theParams.getOffset() == null ? 0 : theParams.getOffset();
		int count = getMaxFetchSize(theParams, theMaxResultsToFetch);

		// perform an offset search instead of a scroll one, which doesn't allow for offset
		List<Long> queryFetchResult = getSearchQueryOptionsStep(theResourceType, theParams, theReferencingPid).fetchHits(offset, count);

		// indicate param was already processed, otherwise queries DB to process it
		theParams.setOffset(null);
		return SearchQueryExecutors.from(queryFetchResult);
	}


	private int getMaxFetchSize(SearchParameterMap theParams, Integer theMax) {
		if (theMax != null) {
			return theMax;
		}

		// todo mb we should really pass this in.
		if (theParams.getCount() != null) {
			return theParams.getCount();
		}

		return DEFAULT_MAX_NON_PAGED_SIZE;
	}


	private SearchQueryOptionsStep<?, Long, SearchLoadingOptionsStep, ?, ?> getSearchQueryOptionsStep(
			String theResourceType, SearchParameterMap theParams, ResourcePersistentId theReferencingPid) {

		dispatchEvent(IHSearchEventListener.HSearchEventType.SEARCH);
		var query= getSearchSession().search(ResourceTable.class)
			// The document id is the PK which is pid.  We use this instead of _myId to avoid fetching the doc body.
			.select(
				// adapt the String docRef.id() to the Long that it really is.
				f -> f.composite(
					docRef -> Long.valueOf(docRef.id()),
					f.documentReference())
			)
			.where(
				f -> buildWhereClause(f, theResourceType, theParams, theReferencingPid)
			);

		if (theParams.getSort() != null) {
			query.sort(
				f -> myExtendedFulltextSortHelper.getSortClauses(f, theParams.getSort(), theResourceType) );

			// indicate parameter was processed
			theParams.setSort(null);
		}

		return query;
	}


	private PredicateFinalStep buildWhereClause(SearchPredicateFactory f, String theResourceType,
															  SearchParameterMap theParams, ResourcePersistentId theReferencingPid) {
		return f.bool(b -> {
			ExtendedHSearchClauseBuilder builder = new ExtendedHSearchClauseBuilder(myFhirContext, myModelConfig, b, f);

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
			 * Posterity:
			 * We do not want the HAPI-FHIR dao's to process the
			 * _text parameter, so we remove it from the map here
			 */
			List<List<IQueryParameterType>> textAndTerms = theParams.remove(Constants.PARAM_TEXT);
			builder.addStringTextSearch(Constants.PARAM_TEXT, textAndTerms);

			if (theReferencingPid != null) {
				b.must(f.match().field("myResourceLinksField").matching(theReferencingPid.toString()));
			}

			if (isNotBlank(theResourceType)) {
				builder.addResourceTypeClause(theResourceType);
			}

			/*
			 * Handle other supported parameters
			 */
			if (myDaoConfig.isAdvancedHSearchIndexing() && theParams.getEverythingMode() == null) {
				myAdvancedIndexQueryBuilder.addAndConsumeAdvancedQueryClauses(builder, theResourceType, theParams, mySearchParamRegistry);
			}
			//DROP EARLY HERE IF BOOL IS EMPTY?
		});
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
	public List<ResourcePersistentId> everything(String theResourceName, SearchParameterMap theParams, ResourcePersistentId theReferencingPid) {
		validateHibernateSearchIsEnabled();

		// todo mb what about max results here?
		List<ResourcePersistentId> retVal = toList(doSearch(null, theParams, theReferencingPid, 10_000), 10_000);
		if (theReferencingPid != null) {
			retVal.add(theReferencingPid);
		}
		return retVal;
	}

	private void validateHibernateSearchIsEnabled() {
		if (isDisabled()) {
			throw new UnsupportedOperationException(Msg.code(2137) + "Hibernate search is not enabled!");
		}
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
		validateHibernateSearchIsEnabled();
		return toList(doSearch(theResourceName, theParams, null, DEFAULT_MAX_NON_PAGED_SIZE), DEFAULT_MAX_NON_PAGED_SIZE);
	}

	/**
	 * Adapt our async interface to the legacy concrete List
	 */
	private List<ResourcePersistentId> toList(ISearchQueryExecutor theSearchResultStream, long theMaxSize) {
		return StreamSupport.stream(Spliterators.spliteratorUnknownSize(theSearchResultStream, 0), false)
			.map(ResourcePersistentId::new)
			.limit(theMaxSize)
			.collect(Collectors.toList());
	}

	@Transactional()
	@Override
	public IBaseResource tokenAutocompleteValueSetSearch(ValueSetAutocompleteOptions theOptions) {
		validateHibernateSearchIsEnabled();
		ensureElastic();

		ValueSetAutocompleteSearch autocomplete = new ValueSetAutocompleteSearch(myFhirContext, myModelConfig, getSearchSession());

		dispatchEvent(IHSearchEventListener.HSearchEventType.SEARCH);
		return autocomplete.search(theOptions);
	}

	/**
	 * Throws an error if configured with Lucene.
	 *
	 * Some features only work with Elasticsearch.
	 * Lastn and the autocomplete search use nested aggregations which are Elasticsearch-only
	 */
	private void ensureElastic() {
		try {
			getSearchSession().scope( ResourceTable.class )
				.aggregation()
				.extension(ElasticsearchExtension.get());
		} catch (SearchException e) {
			// unsupported.  we are probably running Lucene.
			throw new IllegalStateException(Msg.code(2070) + "This operation requires Elasticsearch.  Lucene is not supported.");
		}

	}

	@Override
	public List<ResourcePersistentId> lastN(SearchParameterMap theParams, Integer theMaximumResults) {
		ensureElastic();
		dispatchEvent(IHSearchEventListener.HSearchEventType.SEARCH);
		List<Long> pidList = new LastNOperation(getSearchSession(), myFhirContext, myModelConfig, mySearchParamRegistry)
			.executeLastN(theParams, theMaximumResults);
		return convertLongsToResourcePersistentIds(pidList);
	}

	@Override
	public List<IBaseResource> getResources(Collection<Long> thePids) {
		if (thePids.isEmpty()) {
			return Collections.emptyList();
		}

		SearchSession session = getSearchSession();
		dispatchEvent(IHSearchEventListener.HSearchEventType.SEARCH);
		List<ExtendedHSearchResourceProjection> rawResourceDataList = session.search(ResourceTable.class)
			.select(
				this::buildResourceSelectClause
			)
			.where(
				f -> f.id().matchingAny(thePids) // matches '_id' from resource index
			).fetchAllHits();

		// order resource projections as per thePids
		ArrayList<Long> pidList = new ArrayList<>(thePids);
		List<ExtendedHSearchResourceProjection> orderedAsPidsResourceDataList = rawResourceDataList.stream()
			.sorted( Ordering.explicit(pidList).onResultOf(ExtendedHSearchResourceProjection::getPid) ).collect( Collectors.toList() );

		return resourceProjectionsToResources(orderedAsPidsResourceDataList);
	}


	@Nonnull
	private List<IBaseResource> resourceProjectionsToResources(List<ExtendedHSearchResourceProjection> theResourceDataList) {
		IParser parser = myFhirContext.newJsonParser();
		return theResourceDataList.stream()
			.map(p -> p.toResource(parser))
			.collect(Collectors.toList());
	}


	private CompositeProjectionOptionsStep<?, ExtendedHSearchResourceProjection> buildResourceSelectClause(
							SearchProjectionFactory<EntityReference, ResourceTable> f) {
		return f.composite(
			ExtendedHSearchResourceProjection::new,
			f.field("myId", Long.class),
			f.field("myForcedId", String.class),
			f.field("myRawResource", String.class));
	}


	@Override
	public long count(String theResourceName, SearchParameterMap theParams) {
		SearchQueryOptionsStep<?, Long, SearchLoadingOptionsStep, ?, ?> queryOptionsStep =
			getSearchQueryOptionsStep(theResourceName, theParams, null);

		return queryOptionsStep.fetchTotalHitCount();
	}


	@Override
	@Transactional(readOnly = true)
	public List<IBaseResource> searchForResources(String theResourceType, SearchParameterMap theParams) {
		int offset = 0;
		int limit = theParams.getCount() == null ? DEFAULT_MAX_PAGE_SIZE : theParams.getCount();

		if (theParams.getOffset() != null && theParams.getOffset() != 0) {
			offset = theParams.getOffset();
			// indicate param was already processed, otherwise queries DB to process it
			theParams.setOffset(null);
		}

		dispatchEvent(IHSearchEventListener.HSearchEventType.SEARCH);

		var query = getSearchSession().search(ResourceTable.class)
				.select(this::buildResourceSelectClause)
				.where(f -> buildWhereClause(f, theResourceType, theParams, null));

		if (theParams.getSort() != null) {
			query.sort(
				f -> myExtendedFulltextSortHelper.getSortClauses(f, theParams.getSort(), theResourceType) );
		}

		List<ExtendedHSearchResourceProjection> extendedLuceneResourceProjections = query.fetchHits(offset, limit);

		return resourceProjectionsToResources(extendedLuceneResourceProjections);
	}


	@Override
	public boolean supportsAllOf(SearchParameterMap theParams) {
		return myAdvancedIndexQueryBuilder.isSupportsAllOf(theParams);
	}


	private void dispatchEvent(IHSearchEventListener.HSearchEventType theEventType) {
		if (myHSearchEventListener != null) {
			myHSearchEventListener.hsearchEvent(theEventType);
		}
	}


	@Override
	public void deleteIndexedDocumentsByTypeAndId(Class theClazz, List<Object> theGivenIds) {
		SearchSession session = Search.session(myEntityManager);
		SearchIndexingPlan indexingPlan = session.indexingPlan();
		for (Object givenId : theGivenIds) {
			indexingPlan.purge(theClazz, givenId, null);
		}
		indexingPlan.process();
		indexingPlan.execute();
	}
}
