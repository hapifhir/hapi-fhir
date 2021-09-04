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
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.fhirpath.IFhirPath;
import ca.uhn.fhir.jpa.dao.data.IForcedIdDao;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.search.ExtendedLuceneIndexData;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.extractor.ISearchParamExtractor;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.TokenParamModifier;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.search.engine.search.predicate.dsl.BooleanPredicateClausesStep;
import org.hibernate.search.engine.search.predicate.dsl.SearchPredicateFactory;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
	private ISearchParamExtractor mySearchParamExtractor;
	@Autowired
	private FhirContext myFhirContext;


	private Boolean ourDisabled;

	/**
	 * Constructor
	 */
	public FulltextSearchSvcImpl() {
		super();
	}

	public ExtendedLuceneIndexData extractLuceneIndexData(FhirContext theContext, IBaseResource theResource) {
		ExtendedLuceneIndexData retVal = new ExtendedLuceneIndexData();

		RuntimeResourceDefinition resourceDefinition = theContext.getResourceDefinition(theResource);
		IFhirPath iFhirPath = theContext.newFhirPath();

		resourceDefinition.getSearchParams().stream()
			.map(sp->new LuceneRuntimeSearchParam(sp, iFhirPath, mySearchParamExtractor))
			.forEach(lsp -> {
				lsp.extractLuceneIndexData(theResource, retVal);
			});
		return retVal;
	}

	private void addTextSearch(SearchPredicateFactory f, BooleanPredicateClausesStep<?> b, List<List<IQueryParameterType>> theTerms, String theFieldName) {
		if (theTerms == null) {
			return;
		}
		for (List<? extends IQueryParameterType> nextAnd : theTerms) {
			Set<String> terms = extractOrStringParams(nextAnd);
			if (terms.size() == 1) {
				b.must(f.phrase()
					.field(theFieldName)
					.boost(4.0f)
					.matching(terms.iterator().next().toLowerCase())
					.slop(2));
			} else if (terms.size() > 1) {
				String joinedTerms = StringUtils.join(terms, ' ');
				b.must(f.match().field(theFieldName).matching(joinedTerms));
			} else {
				ourLog.debug("No Terms found in query parameter {}", nextAnd);
			}
		}
	}

	@Nonnull
	private Set<String> extractOrStringParams(List<? extends IQueryParameterType> nextAnd) {
		Set<String> terms = new HashSet<>();
		for (IQueryParameterType nextOr : nextAnd) {
			String nextValueTrimmed;
			if (nextOr instanceof StringParam) {
				StringParam nextOrString = (StringParam) nextOr;
				nextValueTrimmed = StringUtils.defaultString(nextOrString.getValue()).trim();
			} else if (nextOr instanceof TokenParam) {
				TokenParam nextOrToken = (TokenParam) nextOr;
				nextValueTrimmed = nextOrToken.getValue();
			} else {
				throw new IllegalArgumentException("Unsupported full-text param type: " + nextOr.getClass());
			}
			if (isNotBlank(nextValueTrimmed)) {
				terms.add(nextValueTrimmed);
			}
		}
		return terms;
	}

	private List<ResourcePersistentId> doSearch(String theResourceName, SearchParameterMap theParams, ResourcePersistentId theReferencingPid) {

		SearchSession session = Search.session(myEntityManager);
		List<List<IQueryParameterType>> contentAndTerms = theParams.remove(Constants.PARAM_CONTENT);
		List<List<IQueryParameterType>> textAndTerms = theParams.remove(Constants.PARAM_TEXT);
		// wipmb make this query builder dynamic on SPs too.

		/***
		 * {
		 *   "myId": 1
		 *   "myNarrativeText" : 'adsasdjkaldjalkdjalkdjalkdjs",
		 *   // should be indexed, not stored
		 *     "text-code" : "Our observation Glucose Moles volume in Blood"
		 *     "text-clinicalCode" : "Our observation Glucose Moles volume in Blood"
		 *     "text-identifier" :
		 *     "text-component-value-concept": " a a s d d  g v",
		 *     _index: {
		 *
		 *     }
		 *   resource: {
		 *   	type: "Observation"
		 *   	"code": {
		 *     "coding": [
		 *       {
		 *         "system": "http://loinc.org",
		 *         "code": "15074-8",
		 *         "display": "Glucose [Moles/volume] in Blood"
		 *       }
		 *     ],
		 *     "text", "Our observation"
		 *   },
		 *   }
		 * }
		 */

		// TODO generic version
//		List<IQueryParameterType> textParameters = theParams.entrySet().stream()
//			.flatMap(andList -> andList.getValue().stream())
//			.flatMap(Collection::stream)
//			.filter(param -> PARAMQUALIFIER_TOKEN_TEXT.equals(param.getQueryParameterQualifier()))
//			.collect(Collectors.toList());
//		for (IQueryParameterType testParameter : textParameters) {
//			theParams.removeByNameAndQualifier(testParameter.getValueAsQueryToken(), testParameter.getQueryParameterQualifier());
//		}
		//DSTU doesn't support fhirpath, so fall back to old style lookup.

		List<List<IQueryParameterType>> tokenTextAndTerms;
		List<List<IQueryParameterType>> identifierText;
		if (myFhirContext.getVersion().getVersion().isEqualOrNewerThan(FhirVersionEnum.DSTU3)) {
			tokenTextAndTerms = theParams.removeByNameAndQualifier("code", TokenParamModifier.TEXT);
			identifierText = theParams.removeByNameAndQualifier("identifier", TokenParamModifier.TEXT);
		} else {
			tokenTextAndTerms = new ArrayList<>();
			identifierText = new ArrayList<>();
		}

		List<Long> longPids = session.search(ResourceTable.class)
			//Selects are replacements for projection and convert more cleanly than the old implementation.
			.select(
				f -> f.field("myId", Long.class)
			)
			.where(
				f -> f.bool(b -> {
					/*
					 * Handle _content parameter (resource body content)
					 */
					addTextSearch(f, b, contentAndTerms, "myContentText");
					/*
					 * Handle _text parameter (resource narrative content)
					 */
					addTextSearch(f, b, textAndTerms, "myNarrativeText");

					/**
					 * Handle :text qualifier on Tokens
					 */
					addTextSearch(f, b, tokenTextAndTerms, "text-" + "code");
					addTextSearch(f, b, identifierText, "text-" + "identifier");

//					addTextSearch(f, b, codingAndTerms, "myCodingDisplayText");
//					addTextSearch(f, b, codeableConceptAndTerms, "myCodeableConceptText");
//					addTextSearch(f, b, identifierTypeTerms, "myIdentifierTypeText");

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
			.map(pid -> new ResourcePersistentId(pid))
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
