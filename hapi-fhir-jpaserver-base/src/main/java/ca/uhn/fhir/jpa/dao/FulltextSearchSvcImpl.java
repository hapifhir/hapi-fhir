package ca.uhn.fhir.jpa.dao;

/*
 * #%L
 * HAPI FHIR JPA Server
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
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.dao.data.IForcedIdDao;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.search.ExtendedLuceneIndexData;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.extractor.ResourceIndexedSearchParams;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.param.QuantityParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.TokenParamModifier;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class FulltextSearchSvcImpl implements IFulltextSearchSvc {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FulltextSearchSvcImpl.class);
	public static final String EMPTY_MODIFIER = "";
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


	private Boolean ourDisabled;

	/**
	 * Constructor
	 */
	public FulltextSearchSvcImpl() {
		super();
	}

	public ExtendedLuceneIndexData extractLuceneIndexData(FhirContext theContext, IBaseResource theResource, ResourceIndexedSearchParams theNewParams) {
		ExtendedLuceneIndexData retVal = new ExtendedLuceneIndexData(myFhirContext);

		// wip mb - add string params to indexing.
		// wipmb weird - theNewParams seem to have some doubles.
		theNewParams.myStringParams.stream()
				.forEach(param -> {
					retVal.addStringIndexData(param.getParamName(), param.getValueExact());
				});
		theNewParams.myTokenParams.stream()
				.forEach(param -> {
					retVal.addTokenIndexData(param.getParamName(), param.getSystem(), param.getValue());
				});

		return retVal;
	}

	@Override
	public boolean supportsSomeOf(SearchParameterMap myParams) {
		// keep this in sync with the guts of doSearch
		boolean requiresHibernateSearchAccess = myParams.containsKey(Constants.PARAM_CONTENT) || myParams.containsKey(Constants.PARAM_TEXT) || myParams.isLastN();

		requiresHibernateSearchAccess |= myParams.entrySet().stream()
			.flatMap(andList -> andList.getValue().stream())
			.flatMap(Collection::stream)
			// wipmb to extend to string params.
			.anyMatch(param -> {
				String modifier = StringUtils.defaultString(param.getQueryParameterQualifier(), EMPTY_MODIFIER);
				if (param instanceof TokenParam) {
					switch (modifier) {
						case Constants.PARAMQUALIFIER_TOKEN_TEXT:
						case "":
							// we support plain token and token:text
							return true;
						default:
							return false;
					}
				} else if (param instanceof StringParam) {
					switch (modifier) {
						// we support string:text, string:contains, string:exact, and unmodified string.
						case Constants.PARAMQUALIFIER_TOKEN_TEXT:
						case Constants.PARAMQUALIFIER_STRING_EXACT:
						case Constants.PARAMQUALIFIER_STRING_CONTAINS:
						case EMPTY_MODIFIER:
							return true;
						default:
							return false;
					}
				} else if (param instanceof QuantityParam) {
					// wip next up
					return false;
				} else if (param instanceof ReferenceParam) {
					// wip next up
					return false;
				} else {
					return false;
				}
			});

		return requiresHibernateSearchAccess;
	}


	private List<ResourcePersistentId> doSearch(String theResourceName, SearchParameterMap theParams, ResourcePersistentId theReferencingPid) {
		// keep this in sync with supportsSomeOf();
		SearchSession session = Search.session(myEntityManager);

		List<Long> longPids = session.search(ResourceTable.class)
			//Selects are replacements for projection and convert more cleanly than the old implementation.
			.select(
				f -> f.field("myId", Long.class)
			)
			.where(
				f -> f.bool(b -> {
					HibernateSearchQueryBuilder builder = new HibernateSearchQueryBuilder(myFhirContext, b, f);

					/*
					 * Handle _content parameter (resource body content)
					 */
					List<List<IQueryParameterType>> contentAndTerms = theParams.remove(Constants.PARAM_CONTENT);
					builder.addStringTextSearch(Constants.PARAM_CONTENT, contentAndTerms);
					/*
					 * Handle _text parameter (resource narrative content)
					 */
					List<List<IQueryParameterType>> textAndTerms = theParams.remove(Constants.PARAM_TEXT);
					builder.addStringTextSearch(Constants.PARAM_TEXT, textAndTerms);

					/*
					 * Handle other supported parameters
					 */
					// wip mb the query guts

					// copy the keys to avoid concurrent modification error
					for(String nextParam: Lists.newArrayList(theParams.keySet())) {
						RuntimeSearchParam activeParam = mySearchParamRegistry.getActiveSearchParam(theResourceName, nextParam);
						if (activeParam == null) {
							// ignore magic params like
							continue;
						}
						switch (activeParam.getParamType()) {
							case TOKEN:
								List<List<IQueryParameterType>> tokenTextAndOrTerms = theParams.removeByNameAndModifier(nextParam, Constants.PARAMQUALIFIER_TOKEN_TEXT);
								builder.addStringTextSearch(nextParam, tokenTextAndOrTerms);

								List<List<IQueryParameterType>> tokenUnmodifiedAndOrTerms = theParams.removeByNameUnmodified(nextParam);
								builder.addTokenUnmodifiedSearch(nextParam, tokenUnmodifiedAndOrTerms);

								break;
							case STRING:
								List<List<IQueryParameterType>> stringTextAndOrTerms = theParams.removeByNameAndModifier(nextParam, Constants.PARAMQUALIFIER_TOKEN_TEXT);
								builder.addStringTextSearch(nextParam, stringTextAndOrTerms);

								List<List<IQueryParameterType>> stringExactAndOrTerms = theParams.removeByNameAndModifier(nextParam, Constants.PARAMQUALIFIER_STRING_EXACT);
								builder.addStringExactSearch(nextParam, stringExactAndOrTerms);

								List<List<IQueryParameterType>> stringContainsAndOrTerms = theParams.removeByNameAndModifier(nextParam, Constants.PARAMQUALIFIER_STRING_CONTAINS);
								builder.addStringContainsSearch(nextParam, stringContainsAndOrTerms);

								List<List<IQueryParameterType>> stringAndOrTerms = theParams.removeByNameUnmodified(nextParam);
								builder.addStringUnmodifiedSearch(nextParam, stringAndOrTerms);
								break;

							case QUANTITY:
								// wip next up
								break;

							case REFERENCE:
								// wip for gary
								break;

								// wip mb add the rest.
							default:
								// ignore unsupported param types/modifiers.  They will be processed up in SearchBuilder.
						}
					}

					if (theReferencingPid != null) {
						b.must(f.match().field("myResourceLinksField").matching(theReferencingPid.toString()));
					}

					//DROP EARLY HERE IF BOOL IS EMPTY?

					if (isNotBlank(theResourceName)) {
						b.must(f.match().field("myResourceType").matching(theResourceName));
					}
				})
			).fetchAllHits();

		return convertLongsToResourcePersistentIds(longPids);
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
					SearchSession searchSession = Search.session(myEntityManager);
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

}
