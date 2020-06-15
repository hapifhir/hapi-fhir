package ca.uhn.fhir.jpa.search.lastn;

/*-
 * #%L
 * HAPI FHIR JPA Server
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
import ca.uhn.fhir.jpa.model.util.CodeSystemHash;
import ca.uhn.fhir.jpa.search.lastn.json.CodeJson;
import ca.uhn.fhir.jpa.search.lastn.json.ObservationJson;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.util.LastNParameterHelper;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.annotations.VisibleForTesting;
import org.shadehapi.elasticsearch.action.DocWriteResponse;
import org.shadehapi.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.shadehapi.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.shadehapi.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.shadehapi.elasticsearch.action.admin.indices.refresh.RefreshRequest;
import org.shadehapi.elasticsearch.action.index.IndexRequest;
import org.shadehapi.elasticsearch.action.index.IndexResponse;
import org.shadehapi.elasticsearch.action.search.SearchRequest;
import org.shadehapi.elasticsearch.action.search.SearchResponse;
import org.shadehapi.elasticsearch.client.RequestOptions;
import org.shadehapi.elasticsearch.client.RestHighLevelClient;
import org.shadehapi.elasticsearch.common.xcontent.XContentType;
import org.shadehapi.elasticsearch.index.query.BoolQueryBuilder;
import org.shadehapi.elasticsearch.index.query.MatchQueryBuilder;
import org.shadehapi.elasticsearch.index.query.QueryBuilders;
import org.shadehapi.elasticsearch.index.query.RangeQueryBuilder;
import org.shadehapi.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.shadehapi.elasticsearch.search.SearchHit;
import org.shadehapi.elasticsearch.search.SearchHits;
import org.shadehapi.elasticsearch.search.aggregations.AggregationBuilder;
import org.shadehapi.elasticsearch.search.aggregations.AggregationBuilders;
import org.shadehapi.elasticsearch.search.aggregations.Aggregations;
import org.shadehapi.elasticsearch.search.aggregations.BucketOrder;
import org.shadehapi.elasticsearch.search.aggregations.bucket.composite.CompositeAggregationBuilder;
import org.shadehapi.elasticsearch.search.aggregations.bucket.composite.CompositeValuesSourceBuilder;
import org.shadehapi.elasticsearch.search.aggregations.bucket.composite.ParsedComposite;
import org.shadehapi.elasticsearch.search.aggregations.bucket.composite.TermsValuesSourceBuilder;
import org.shadehapi.elasticsearch.search.aggregations.bucket.terms.ParsedTerms;
import org.shadehapi.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.shadehapi.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.shadehapi.elasticsearch.search.aggregations.metrics.tophits.ParsedTopHits;
import org.shadehapi.elasticsearch.search.aggregations.support.ValueType;
import org.shadehapi.elasticsearch.search.builder.SearchSourceBuilder;
import org.shadehapi.elasticsearch.search.sort.SortOrder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class ElasticsearchSvcImpl implements IElasticsearchSvc {

	// Index Constants
	public static final String OBSERVATION_INDEX = "observation_index";
	public static final String OBSERVATION_CODE_INDEX = "code_index";
	public static final String OBSERVATION_DOCUMENT_TYPE = "ca.uhn.fhir.jpa.model.entity.ObservationIndexedSearchParamLastNEntity";
	public static final String CODE_DOCUMENT_TYPE = "ca.uhn.fhir.jpa.model.entity.ObservationIndexedCodeCodeableConceptEntity";
	public static final String OBSERVATION_INDEX_SCHEMA_FILE = "ObservationIndexSchema.json";
	public static final String OBSERVATION_CODE_INDEX_SCHEMA_FILE = "ObservationCodeIndexSchema.json";
	final static String INITIAL_OBSERVATION_CODE_ID = "observation_code_id_not_defined";

	// Aggregation Constants
	private final String GROUP_BY_SUBJECT = "group_by_subject";
//	private final String GROUP_BY_SYSTEM = "group_by_system";
	private final String GROUP_BY_CODE_ID = "group_by_code_id";
	private final String MOST_RECENT_EFFECTIVE = "most_recent_effective";

	// Observation index document element names
	private final String OBSERVATION_IDENTIFIER_FIELD_NAME = "identifier";
	private final String OBSERVATION_SUBJECT_FIELD_NAME = "subject";
	private final String OBSERVATION_CODEVALUE_FIELD_NAME = "codeconceptcodingcode";
	private final String OBSERVATION_CODESYSTEM_FIELD_NAME = "codeconceptcodingsystem";
	private final String OBSERVATION_CODEHASH_FIELD_NAME = "codeconceptcodingcode_system_hash";
	private final String OBSERVATION_CODEDISPLAY_FIELD_NAME = "codeconceptcodingdisplay";
	private final String OBSERVATION_CODE_TEXT_FIELD_NAME = "codeconcepttext";
	private final String OBSERVATION_EFFECTIVEDTM_FIELD_NAME = "effectivedtm";
	private final String OBSERVATION_CATEGORYHASH_FIELD_NAME = "categoryconceptcodingcode_system_hash";
	private final String OBSERVATION_CATEGORYVALUE_FIELD_NAME = "categoryconceptcodingcode";
	private final String OBSERVATION_CATEGORYSYSTEM_FIELD_NAME = "categoryconceptcodingsystem";
	private final String OBSERVATION_CATEGORYDISPLAY_FIELD_NAME = "categoryconceptcodingdisplay";
	private final String OBSERVATION_CATEGORYTEXT_FIELD_NAME = "categoryconcepttext";
	private final String OBSERVATION_CODE_IDENTIFIER_FIELD_NAME = "codeconceptid";

	// Code index document element names
	private final String CODE_HASH_FIELD_NAME = "codingcode_system_hash";
	private final String CODE_TEXT_FIELD_NAME = "text";
	private final String CODE_IDENTIFIER_FIELD_NAME = "codeable_concept_id";

	private final RestHighLevelClient myRestHighLevelClient;

	private final ObjectMapper objectMapper = new ObjectMapper();

	public ElasticsearchSvcImpl(String theHostname, int thePort, String theUsername, String thePassword) {
		myRestHighLevelClient = ElasticsearchRestClientFactory.createElasticsearchHighLevelRestClient(theHostname, thePort, theUsername, thePassword);

		try {
			createObservationIndexIfMissing();
			createObservationCodeIndexIfMissing();
		} catch (IOException theE) {
			throw new RuntimeException("Failed to create document index", theE);
		}
	}

	private String getIndexSchema(String theSchemaFileName) throws IOException {
		InputStreamReader input = new InputStreamReader(ElasticsearchSvcImpl.class.getResourceAsStream(theSchemaFileName));
		BufferedReader reader = new BufferedReader(input);
		StringBuilder sb = new StringBuilder();
		String str;
		while((str = reader.readLine())!= null){
			sb.append(str);
		}

		return sb.toString();
	}

	private void createObservationIndexIfMissing() throws IOException {
		if (indexExists(OBSERVATION_INDEX)) {
			return;
		}
		String observationMapping = getIndexSchema(OBSERVATION_INDEX_SCHEMA_FILE);
		if (!createIndex(OBSERVATION_INDEX, observationMapping)) {
			throw new RuntimeException("Failed to create observation index");
		}
	}

	private void createObservationCodeIndexIfMissing() throws IOException {
		if (indexExists(OBSERVATION_CODE_INDEX)) {
			return;
		}
		String observationCodeMapping = getIndexSchema(OBSERVATION_CODE_INDEX_SCHEMA_FILE);
		if (!createIndex(OBSERVATION_CODE_INDEX, observationCodeMapping)) {
			throw new RuntimeException("Failed to create observation code index");
		}

	}

	private boolean createIndex(String theIndexName, String theMapping) throws IOException {
		CreateIndexRequest request = new CreateIndexRequest(theIndexName);
		request.source(theMapping, XContentType.JSON);
		CreateIndexResponse createIndexResponse = myRestHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
		return createIndexResponse.isAcknowledged();

	}

	private boolean indexExists(String theIndexName) throws IOException {
		GetIndexRequest request = new GetIndexRequest();
		request.indices(theIndexName);
		return myRestHighLevelClient.indices().exists(request, RequestOptions.DEFAULT);
	}

	@Override
	public List<String> executeLastN(SearchParameterMap theSearchParameterMap, FhirContext theFhirContext, Integer theMaxResultsToFetch) {
		String[] topHitsInclude = {OBSERVATION_IDENTIFIER_FIELD_NAME};
		return buildAndExecuteSearch(theSearchParameterMap, theFhirContext, topHitsInclude,
			ObservationJson::getIdentifier, theMaxResultsToFetch);
	}

	private <T> List<T> buildAndExecuteSearch(SearchParameterMap theSearchParameterMap, FhirContext theFhirContext,
																		String[] topHitsInclude, Function<ObservationJson,T> setValue, Integer theMaxResultsToFetch) {
		String patientParamName = LastNParameterHelper.getPatientParamName(theFhirContext);
		String subjectParamName = LastNParameterHelper.getSubjectParamName(theFhirContext);
		List<T> searchResults = new ArrayList<>();
		if (theSearchParameterMap.containsKey(patientParamName)
			|| theSearchParameterMap.containsKey(subjectParamName)) {
			for (String subject : getSubjectReferenceCriteria(patientParamName, subjectParamName, theSearchParameterMap)) {
				if (theMaxResultsToFetch != null && searchResults.size() >= theMaxResultsToFetch) {
					break;
				}
				SearchRequest myLastNRequest = buildObservationsSearchRequest(subject, theSearchParameterMap, theFhirContext,
					createObservationSubjectAggregationBuilder(getMaxParameter(theSearchParameterMap), topHitsInclude));
				try {
					SearchResponse lastnResponse = executeSearchRequest(myLastNRequest);
					searchResults.addAll(buildObservationList(lastnResponse, setValue, theSearchParameterMap, theFhirContext,
						theMaxResultsToFetch));
				} catch (IOException theE) {
					throw new InvalidRequestException("Unable to execute LastN request", theE);
				}
			}
		} else {
			SearchRequest myLastNRequest = buildObservationsSearchRequest(theSearchParameterMap, theFhirContext,
				createObservationCodeAggregationBuilder(getMaxParameter(theSearchParameterMap), topHitsInclude));
			try {
				SearchResponse lastnResponse = executeSearchRequest(myLastNRequest);
				searchResults.addAll(buildObservationList(lastnResponse, setValue, theSearchParameterMap, theFhirContext,
					theMaxResultsToFetch));
			} catch (IOException theE) {
				throw new InvalidRequestException("Unable to execute LastN request", theE);
			}
		}
		return searchResults;
	}

	private int getMaxParameter(SearchParameterMap theSearchParameterMap) {
		if (theSearchParameterMap.getLastNMax() == null) {
			return 1;
		} else {
			return theSearchParameterMap.getLastNMax();
		}
	}

	private List<String> getSubjectReferenceCriteria(String thePatientParamName, String theSubjectParamName, SearchParameterMap theSearchParameterMap) {
		List<String> subjectReferenceCriteria = new ArrayList<>();

		List<List<IQueryParameterType>> patientParams = new ArrayList<>();
		if (theSearchParameterMap.get(thePatientParamName) != null) {
			patientParams.addAll(theSearchParameterMap.get(thePatientParamName));
		}
		if (theSearchParameterMap.get(theSubjectParamName) != null) {
			patientParams.addAll(theSearchParameterMap.get(theSubjectParamName));
		}
		for (List<? extends IQueryParameterType> nextSubjectList : patientParams) {
			subjectReferenceCriteria.addAll(getReferenceValues(nextSubjectList));
		}
		return subjectReferenceCriteria;
	}

	private List<String> getReferenceValues(List<? extends IQueryParameterType> referenceParams) {
		List<String> referenceList = new ArrayList<>();

		for (IQueryParameterType nextOr : referenceParams) {

			if (nextOr instanceof ReferenceParam) {
				ReferenceParam ref = (ReferenceParam) nextOr;
				if (isBlank(ref.getChain())) {
					referenceList.add(ref.getValue());
				}
			} else {
				throw new IllegalArgumentException("Invalid token type (expecting ReferenceParam): " + nextOr.getClass());
			}
		}
		return referenceList;
	}

	private CompositeAggregationBuilder createObservationSubjectAggregationBuilder(Integer theMaxNumberObservationsPerCode, String[] theTopHitsInclude) {
		CompositeValuesSourceBuilder<?> subjectValuesBuilder = new TermsValuesSourceBuilder(OBSERVATION_SUBJECT_FIELD_NAME).field(OBSERVATION_SUBJECT_FIELD_NAME);
		List<CompositeValuesSourceBuilder<?>> compositeAggSubjectSources = new ArrayList<>();
		compositeAggSubjectSources.add(subjectValuesBuilder);
		CompositeAggregationBuilder compositeAggregationSubjectBuilder = new CompositeAggregationBuilder(GROUP_BY_SUBJECT, compositeAggSubjectSources);
		compositeAggregationSubjectBuilder.subAggregation(createObservationCodeAggregationBuilder(theMaxNumberObservationsPerCode, theTopHitsInclude));
		compositeAggregationSubjectBuilder.size(10000);

		return compositeAggregationSubjectBuilder;
	}

	private TermsAggregationBuilder createObservationCodeAggregationBuilder(int theMaxNumberObservationsPerCode, String[] theTopHitsInclude) {
		TermsAggregationBuilder observationCodeAggregationBuilder = new TermsAggregationBuilder(GROUP_BY_CODE_ID, ValueType.STRING).field(OBSERVATION_CODE_IDENTIFIER_FIELD_NAME);
		observationCodeAggregationBuilder.order(BucketOrder.key(true));
		// Top Hits Aggregation
		observationCodeAggregationBuilder.subAggregation(AggregationBuilders.topHits(MOST_RECENT_EFFECTIVE)
			.sort(OBSERVATION_EFFECTIVEDTM_FIELD_NAME, SortOrder.DESC)
			.fetchSource(theTopHitsInclude, null).size(theMaxNumberObservationsPerCode));
		observationCodeAggregationBuilder.size(10000);

		return observationCodeAggregationBuilder;
	}

	private SearchResponse executeSearchRequest(SearchRequest searchRequest) throws IOException {
		return myRestHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
	}

	private <T> List<T> buildObservationList(SearchResponse theSearchResponse, Function<ObservationJson,T> setValue,
														  SearchParameterMap theSearchParameterMap, FhirContext theFhirContext,
														  Integer theMaxResultsToFetch) throws IOException {
		List<T> theObservationList = new ArrayList<>();
		if (theSearchParameterMap.containsKey(LastNParameterHelper.getPatientParamName(theFhirContext))
			|| theSearchParameterMap.containsKey(LastNParameterHelper.getSubjectParamName(theFhirContext))) {
			for (ParsedComposite.ParsedBucket subjectBucket : getSubjectBuckets(theSearchResponse)) {
				if (theMaxResultsToFetch != null && theObservationList.size() >= theMaxResultsToFetch) {
					break;
				}
				for (Terms.Bucket observationCodeBucket : getObservationCodeBuckets(subjectBucket)) {
					if (theMaxResultsToFetch != null && theObservationList.size() >= theMaxResultsToFetch) {
						break;
					}
					for (SearchHit lastNMatch : getLastNMatches(observationCodeBucket)) {
						if (theMaxResultsToFetch != null && theObservationList.size() >= theMaxResultsToFetch) {
							break;
						}
						String indexedObservation = lastNMatch.getSourceAsString();
						ObservationJson observationJson = objectMapper.readValue(indexedObservation, ObservationJson.class);
						theObservationList.add(setValue.apply(observationJson));
					}
				}
			}
		} else {
			for (Terms.Bucket observationCodeBucket : getObservationCodeBuckets(theSearchResponse)) {
				if (theMaxResultsToFetch != null && theObservationList.size() >= theMaxResultsToFetch) {
					break;
				}
				for (SearchHit lastNMatch : getLastNMatches(observationCodeBucket)) {
					if (theMaxResultsToFetch != null && theObservationList.size() >= theMaxResultsToFetch) {
						break;
					}
					String indexedObservation = lastNMatch.getSourceAsString();
					ObservationJson observationJson = objectMapper.readValue(indexedObservation, ObservationJson.class);
					theObservationList.add(setValue.apply(observationJson));
				}
			}
		}

		return theObservationList;
	}

	private List<ParsedComposite.ParsedBucket> getSubjectBuckets(SearchResponse theSearchResponse) {
		Aggregations responseAggregations = theSearchResponse.getAggregations();
		ParsedComposite aggregatedSubjects = responseAggregations.get(GROUP_BY_SUBJECT);
		return aggregatedSubjects.getBuckets();
	}

	private List<? extends Terms.Bucket> getObservationCodeBuckets(SearchResponse theSearchResponse) {
		Aggregations responseAggregations = theSearchResponse.getAggregations();
		return getObservationCodeBuckets(responseAggregations);
	}

	private List<? extends Terms.Bucket> getObservationCodeBuckets(ParsedComposite.ParsedBucket theSubjectBucket) {
		Aggregations observationCodeSystemAggregations = theSubjectBucket.getAggregations();
		return getObservationCodeBuckets(observationCodeSystemAggregations);
	}

	private List<? extends Terms.Bucket> getObservationCodeBuckets(Aggregations theObservationCodeSystemAggregations) {
		List<Terms.Bucket> retVal = new ArrayList<>();
//		ParsedTerms aggregatedObservationCodeSystems = theObservationCodeSystemAggregations.get(GROUP_BY_SYSTEM);
		ParsedTerms aggregatedObservationCode = theObservationCodeSystemAggregations.get(GROUP_BY_CODE_ID);
//		for(Terms.Bucket observationCodeSystem : aggregatedObservationCodeSystems.getBuckets()) {
//			Aggregations observationCodeCodeAggregations = observationCodeSystem.getAggregations();
//			ParsedTerms aggregatedObservationCodeCodes = observationCodeCodeAggregations.get(GROUP_BY_CODE);
//			retVal.addAll(aggregatedObservationCodeCodes.getBuckets());
//		}
		retVal.addAll(aggregatedObservationCode.getBuckets());
		return retVal;
	}

	private SearchHit[] getLastNMatches(Terms.Bucket theObservationCodeBucket) {
		Aggregations topHitObservationCodes = theObservationCodeBucket.getAggregations();
		ParsedTopHits parsedTopHits = topHitObservationCodes.get(MOST_RECENT_EFFECTIVE);
		return parsedTopHits.getHits().getHits();
	}

	private SearchRequest buildObservationsSearchRequest(SearchParameterMap theSearchParameterMap, FhirContext theFhirContext, AggregationBuilder theAggregationBuilder) {
		SearchRequest searchRequest = new SearchRequest(OBSERVATION_INDEX);
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		// Query
		if (!searchParamsHaveLastNCriteria(theSearchParameterMap, theFhirContext)) {
			searchSourceBuilder.query(QueryBuilders.matchAllQuery());
		} else {
			BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
			addCategoriesCriteria(boolQueryBuilder, theSearchParameterMap, theFhirContext);
			addObservationCodeCriteria(boolQueryBuilder, theSearchParameterMap, theFhirContext);
			addDateCriteria(boolQueryBuilder, theSearchParameterMap, theFhirContext);
			searchSourceBuilder.query(boolQueryBuilder);
		}
		searchSourceBuilder.size(0);

		// Aggregation by order codes
		searchSourceBuilder.aggregation(theAggregationBuilder);
		searchRequest.source(searchSourceBuilder);

		return searchRequest;
	}

	private SearchRequest buildObservationsSearchRequest(String theSubjectParam, SearchParameterMap theSearchParameterMap, FhirContext theFhirContext,
																		  AggregationBuilder theAggregationBuilder) {
		SearchRequest searchRequest = new SearchRequest(OBSERVATION_INDEX);
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		// Query
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		boolQueryBuilder.must(QueryBuilders.termQuery(OBSERVATION_SUBJECT_FIELD_NAME, theSubjectParam));
		addCategoriesCriteria(boolQueryBuilder, theSearchParameterMap, theFhirContext);
		addObservationCodeCriteria(boolQueryBuilder, theSearchParameterMap, theFhirContext);
		addDateCriteria(boolQueryBuilder, theSearchParameterMap, theFhirContext);
		searchSourceBuilder.query(boolQueryBuilder);
		searchSourceBuilder.size(0);

		// Aggregation by order codes
		searchSourceBuilder.aggregation(theAggregationBuilder);
		searchRequest.source(searchSourceBuilder);

		return searchRequest;
	}

	private Boolean searchParamsHaveLastNCriteria(SearchParameterMap theSearchParameterMap, FhirContext theFhirContext) {
		return theSearchParameterMap != null &&
			(theSearchParameterMap.containsKey(LastNParameterHelper.getPatientParamName(theFhirContext))
				|| theSearchParameterMap.containsKey(LastNParameterHelper.getSubjectParamName(theFhirContext))
				|| theSearchParameterMap.containsKey(LastNParameterHelper.getCategoryParamName(theFhirContext))
				|| theSearchParameterMap.containsKey(LastNParameterHelper.getCodeParamName(theFhirContext)));
	}

	private void addCategoriesCriteria(BoolQueryBuilder theBoolQueryBuilder, SearchParameterMap theSearchParameterMap, FhirContext theFhirContext) {
		String categoryParamName = LastNParameterHelper.getCategoryParamName(theFhirContext);
		if (theSearchParameterMap.containsKey(categoryParamName)) {
			ArrayList<String> codeSystemHashList = new ArrayList<>();
			ArrayList<String> codeOnlyList = new ArrayList<>();
			ArrayList<String> systemOnlyList = new ArrayList<>();
			ArrayList<String> textOnlyList = new ArrayList<>();
			List<List<IQueryParameterType>> andOrParams = theSearchParameterMap.get(categoryParamName);
			for (List<? extends IQueryParameterType> nextAnd : andOrParams) {
				codeSystemHashList.addAll(getCodingCodeSystemValues(nextAnd));
				codeOnlyList.addAll(getCodingCodeOnlyValues(nextAnd));
				systemOnlyList.addAll(getCodingSystemOnlyValues(nextAnd));
				textOnlyList.addAll(getCodingTextOnlyValues(nextAnd));
			}
			if (codeSystemHashList.size() > 0) {
				theBoolQueryBuilder.must(QueryBuilders.termsQuery(OBSERVATION_CATEGORYHASH_FIELD_NAME, codeSystemHashList));
			}
			if (codeOnlyList.size() > 0) {
				theBoolQueryBuilder.must(QueryBuilders.termsQuery(OBSERVATION_CATEGORYVALUE_FIELD_NAME, codeOnlyList));
			}
			if (systemOnlyList.size() > 0) {
				theBoolQueryBuilder.must(QueryBuilders.termsQuery(OBSERVATION_CATEGORYSYSTEM_FIELD_NAME, systemOnlyList));
			}
			if (textOnlyList.size() > 0) {
				BoolQueryBuilder myTextBoolQueryBuilder = QueryBuilders.boolQuery();
				for (String textOnlyParam : textOnlyList) {
					myTextBoolQueryBuilder.should(QueryBuilders.matchPhrasePrefixQuery(OBSERVATION_CATEGORYDISPLAY_FIELD_NAME, textOnlyParam));
					myTextBoolQueryBuilder.should(QueryBuilders.matchPhrasePrefixQuery(OBSERVATION_CATEGORYTEXT_FIELD_NAME, textOnlyParam));
				}
				theBoolQueryBuilder.must(myTextBoolQueryBuilder);
			}
		}

	}

	private List<String> getCodingCodeSystemValues(List<? extends IQueryParameterType> codeParams) {
		ArrayList<String> codeSystemHashList = new ArrayList<>();
		for (IQueryParameterType nextOr : codeParams) {
			if (nextOr instanceof TokenParam) {
				TokenParam ref = (TokenParam) nextOr;
				if (ref.getSystem() != null && ref.getValue() != null) {
					codeSystemHashList.add(String.valueOf(CodeSystemHash.hashCodeSystem(ref.getSystem(), ref.getValue())));
				}
			} else {
				throw new IllegalArgumentException("Invalid token type (expecting TokenParam): " + nextOr.getClass());
			}
		}
		return codeSystemHashList;
	}

	private List<String> getCodingCodeOnlyValues(List<? extends IQueryParameterType> codeParams) {
		ArrayList<String> codeOnlyList = new ArrayList<>();
		for (IQueryParameterType nextOr : codeParams) {

			if (nextOr instanceof TokenParam) {
				TokenParam ref = (TokenParam) nextOr;
				if (ref.getValue() != null && ref.getSystem() == null && !ref.isText()) {
					codeOnlyList.add(ref.getValue());
				}
			} else {
				throw new IllegalArgumentException("Invalid token type (expecting TokenParam): " + nextOr.getClass());
			}
		}
		return codeOnlyList;
	}

	private List<String> getCodingSystemOnlyValues(List<? extends IQueryParameterType> codeParams) {
		ArrayList<String> systemOnlyList = new ArrayList<>();
		for (IQueryParameterType nextOr : codeParams) {

			if (nextOr instanceof TokenParam) {
				TokenParam ref = (TokenParam) nextOr;
				if (ref.getValue() == null && ref.getSystem() != null) {
					systemOnlyList.add(ref.getSystem());
				}
			} else {
				throw new IllegalArgumentException("Invalid token type (expecting TokenParam): " + nextOr.getClass());
			}
		}
		return systemOnlyList;
	}

	private List<String> getCodingTextOnlyValues(List<? extends IQueryParameterType> codeParams) {
		ArrayList<String> textOnlyList = new ArrayList<>();
		for (IQueryParameterType nextOr : codeParams) {

			if (nextOr instanceof TokenParam) {
				TokenParam ref = (TokenParam) nextOr;
				if (ref.isText() && ref.getValue() != null) {
					textOnlyList.add(ref.getValue());
				}
			} else {
				throw new IllegalArgumentException("Invalid token type (expecting TokenParam): " + nextOr.getClass());
			}
		}
		return textOnlyList;
	}

	private void addObservationCodeCriteria(BoolQueryBuilder theBoolQueryBuilder, SearchParameterMap theSearchParameterMap, FhirContext theFhirContext) {
		String codeParamName = LastNParameterHelper.getCodeParamName(theFhirContext);
		if (theSearchParameterMap.containsKey(codeParamName)) {
			ArrayList<String> codeSystemHashList = new ArrayList<>();
			ArrayList<String> codeOnlyList = new ArrayList<>();
			ArrayList<String> systemOnlyList = new ArrayList<>();
			ArrayList<String> textOnlyList = new ArrayList<>();
			List<List<IQueryParameterType>> andOrParams = theSearchParameterMap.get(codeParamName);
			for (List<? extends IQueryParameterType> nextAnd : andOrParams) {
				codeSystemHashList.addAll(getCodingCodeSystemValues(nextAnd));
				codeOnlyList.addAll(getCodingCodeOnlyValues(nextAnd));
				systemOnlyList.addAll(getCodingSystemOnlyValues(nextAnd));
				textOnlyList.addAll(getCodingTextOnlyValues(nextAnd));
			}
			if (codeSystemHashList.size() > 0) {
				theBoolQueryBuilder.must(QueryBuilders.termsQuery(OBSERVATION_CODEHASH_FIELD_NAME, codeSystemHashList));
			}
			if (codeOnlyList.size() > 0) {
				theBoolQueryBuilder.must(QueryBuilders.termsQuery(OBSERVATION_CODEVALUE_FIELD_NAME, codeOnlyList));
			}
			if (systemOnlyList.size() > 0) {
				theBoolQueryBuilder.must(QueryBuilders.termsQuery(OBSERVATION_CODESYSTEM_FIELD_NAME, systemOnlyList));
			}
			if (textOnlyList.size() > 0) {
				BoolQueryBuilder myTextBoolQueryBuilder = QueryBuilders.boolQuery();
				for (String textOnlyParam : textOnlyList) {
					myTextBoolQueryBuilder.should(QueryBuilders.matchPhrasePrefixQuery(OBSERVATION_CODEDISPLAY_FIELD_NAME, textOnlyParam));
					myTextBoolQueryBuilder.should(QueryBuilders.matchPhrasePrefixQuery(OBSERVATION_CODE_TEXT_FIELD_NAME, textOnlyParam));
				}
				theBoolQueryBuilder.must(myTextBoolQueryBuilder);
			}
		}

	}

	private void addDateCriteria(BoolQueryBuilder theBoolQueryBuilder, SearchParameterMap theSearchParameterMap, FhirContext theFhirContext) {
		String dateParamName = LastNParameterHelper.getEffectiveParamName(theFhirContext);
		if (theSearchParameterMap.containsKey(dateParamName)) {
			List<List<IQueryParameterType>> andOrParams = theSearchParameterMap.get(dateParamName);
			for (List<? extends IQueryParameterType> nextAnd : andOrParams) {
				BoolQueryBuilder myDateBoolQueryBuilder = new BoolQueryBuilder();
				for (IQueryParameterType nextOr : nextAnd) {
					if (nextOr instanceof DateParam) {
						DateParam myDate = (DateParam) nextOr;
						createDateCriteria(myDate, myDateBoolQueryBuilder);
					}
				}
				theBoolQueryBuilder.must(myDateBoolQueryBuilder);
			}
		}
	}

	private void createDateCriteria(DateParam theDate, BoolQueryBuilder theBoolQueryBuilder) {
		Long dateInstant = theDate.getValue().getTime();
		RangeQueryBuilder myRangeQueryBuilder = new RangeQueryBuilder(OBSERVATION_EFFECTIVEDTM_FIELD_NAME);

		ParamPrefixEnum prefix = theDate.getPrefix();
		if (prefix == ParamPrefixEnum.GREATERTHAN || prefix == ParamPrefixEnum.STARTS_AFTER) {
			theBoolQueryBuilder.should(myRangeQueryBuilder.gt(dateInstant));
		} else if (prefix == ParamPrefixEnum.LESSTHAN || prefix == ParamPrefixEnum.ENDS_BEFORE) {
			theBoolQueryBuilder.should(myRangeQueryBuilder.lt(dateInstant));
		} else if (prefix == ParamPrefixEnum.LESSTHAN_OR_EQUALS) {
			theBoolQueryBuilder.should(myRangeQueryBuilder.lte(dateInstant));
		} else if (prefix == ParamPrefixEnum.GREATERTHAN_OR_EQUALS) {
			theBoolQueryBuilder.should(myRangeQueryBuilder.gte(dateInstant));
		} else {
			theBoolQueryBuilder.should(new MatchQueryBuilder(OBSERVATION_EFFECTIVEDTM_FIELD_NAME, dateInstant));
		}
	}

	@VisibleForTesting
	public List<ObservationJson> executeLastNWithAllFieldsForTest(SearchParameterMap theSearchParameterMap, FhirContext theFhirContext) {
		return buildAndExecuteSearch(theSearchParameterMap, theFhirContext, null, t -> t, 200);
	}

	@VisibleForTesting
	List<CodeJson> queryAllIndexedObservationCodesForTest() throws IOException {
		SearchRequest codeSearchRequest = new SearchRequest(OBSERVATION_CODE_INDEX);
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		// Query
		searchSourceBuilder.query(QueryBuilders.matchAllQuery());
		searchSourceBuilder.size(1000);
		codeSearchRequest.source(searchSourceBuilder);
		SearchResponse codeSearchResponse = executeSearchRequest(codeSearchRequest);
		return buildCodeResult(codeSearchResponse);
	}

	private List<CodeJson> buildCodeResult(SearchResponse theSearchResponse) throws JsonProcessingException {
		SearchHits codeHits = theSearchResponse.getHits();
		List<CodeJson> codes = new ArrayList<>();
		for (SearchHit codeHit : codeHits) {
			CodeJson code = objectMapper.readValue(codeHit.getSourceAsString(), CodeJson.class);
			codes.add(code);
		}
		return codes;
	}

	@Override
	public ObservationJson getObservationDocument(String theDocumentID) {
		if (theDocumentID == null) {
			throw new InvalidRequestException("Require non-null document ID for observation document query");
		}
		SearchRequest theSearchRequest = buildSingleObservationSearchRequest(theDocumentID);
		ObservationJson observationDocumentJson = null;
		try {
			SearchResponse observationDocumentResponse = executeSearchRequest(theSearchRequest);
			SearchHit[] observationDocumentHits = observationDocumentResponse.getHits().getHits();
			if (observationDocumentHits.length > 0) {
				// There should be no more than one hit for the identifier
				String observationDocument = observationDocumentHits[0].getSourceAsString();
				observationDocumentJson = objectMapper.readValue(observationDocument, ObservationJson.class);
			}

		} catch (IOException theE) {
			throw new InvalidRequestException("Unable to execute observation document query for ID " + theDocumentID, theE);
		}

		return observationDocumentJson;
	}

	private SearchRequest buildSingleObservationSearchRequest(String theObservationIdentifier) {
		SearchRequest searchRequest = new SearchRequest(OBSERVATION_INDEX);
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		boolQueryBuilder.must(QueryBuilders.termQuery(OBSERVATION_IDENTIFIER_FIELD_NAME, theObservationIdentifier));
		searchSourceBuilder.query(boolQueryBuilder);
		searchSourceBuilder.size(1);

		searchRequest.source(searchSourceBuilder);

		return searchRequest;
	}

/*	@Override
	public CodeJson getObservationCodeDocument(String theCodeSystemHash, String theText) {
		if(theCodeSystemHash == null && theText == null) {
			throw new InvalidRequestException("Require a non-null code system hash value or display value for observation code document query");
		}
		SearchRequest theSearchRequest = buildSingleObservationCodeSearchRequest(theCodeSystemHash, theText);
		CodeJson observationCodeDocumentJson = null;
		try {
			SearchResponse observationCodeDocumentResponse = executeSearchRequest(theSearchRequest);
			SearchHit[] observationCodeDocumentHits = observationCodeDocumentResponse.getHits().getHits();
			if (observationCodeDocumentHits.length > 0) {
				// There should be no more than one hit for the code lookup.
				String observationCodeDocument = observationCodeDocumentHits[0].getSourceAsString();
				observationCodeDocumentJson = objectMapper.readValue(observationCodeDocument, CodeJson.class);
			}

		} catch (IOException theE) {
			throw new InvalidRequestException("Unable to execute observation code document query hash code or display", theE);
		}

		return observationCodeDocumentJson;
	}

	private SearchRequest buildSingleObservationCodeSearchRequest(String theCodeSystemHash, String theText) {
		SearchRequest searchRequest = new SearchRequest(OBSERVATION_CODE_INDEX);
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		if (theCodeSystemHash != null) {
			boolQueryBuilder.must(QueryBuilders.termQuery(CODE_HASH_FIELD_NAME, theCodeSystemHash));
		} else {
			boolQueryBuilder.must(QueryBuilders.matchPhraseQuery(CODE_TEXT_FIELD_NAME, theText));
		}

		searchSourceBuilder.query(boolQueryBuilder);
		searchSourceBuilder.size(1);

		searchRequest.source(searchSourceBuilder);

		return searchRequest;
	}
*/
	private CodeJson getOrCreateObservationCodeDocument(ObservationJson theObservationJson) {
		if(theObservationJson.getCode_coding_code_system_hash() == null && theObservationJson.getCode_concept_text() == null) {
			throw new InvalidRequestException("Require a non-null code system hash value or display value for observation code document query");
		}
		Map<String, CodeJson> indexedCodeJsons;
		boolean unindexedCodings = false;
		CodeJson indexedCodeJson = null;
		try {
			indexedCodeJsons = new HashMap<>();
			if (theObservationJson.getCode_coding_code_system_hash() != null && !theObservationJson.getCode_coding_code_system_hash().isEmpty()) {
				// Process Codings
				for (String codeSystemHash : theObservationJson.getCode_coding_code_system_hash()) {
					SearchRequest theSearchRequest = buildSingleObservationCodeValueSearchRequest(codeSystemHash, CODE_HASH_FIELD_NAME);
					List<CodeJson> codeJsonsForCoding = getCodeJsonsFromSearchRequest(theSearchRequest);
					if (codeJsonsForCoding.isEmpty()) {
						unindexedCodings = true;
					} else {
						for(CodeJson codeJsonForCoding : codeJsonsForCoding) {
							indexedCodeJsons.put(codeJsonForCoding.getCodeableConceptId(), codeJsonForCoding);
						}
					}
				}
			} else {
				// Process Text
				SearchRequest theSearchRequest = buildSingleObservationCodeValueSearchRequest(theObservationJson.getCode_concept_text(), CODE_TEXT_FIELD_NAME);
				List<CodeJson> codeJsonsForCoding = getCodeJsonsFromSearchRequest(theSearchRequest);
				if (codeJsonsForCoding.isEmpty()) {
					unindexedCodings = true;
				} else {
					for(CodeJson codeJsonForCoding : codeJsonsForCoding) {
						indexedCodeJsons.put(codeJsonForCoding.getCodeableConceptId(), codeJsonForCoding);
					}
				}
			}
			if (indexedCodeJsons.size() == 1 && !unindexedCodings) {
				// Only one index found and it includes all codings.
				for(String codeId : indexedCodeJsons.keySet()) {
					indexedCodeJson = indexedCodeJsons.get(codeId);
				}
			} else {
				// Either no index found or multiple found. Either way, create a new index for code.
				indexedCodeJson = new CodeJson();
				indexedCodeJson.addCoding(theObservationJson.getCode_coding_system(), theObservationJson.getCode_coding_code(),
					theObservationJson.getCode_coding_display(), theObservationJson.getCode_coding_code_system_hash());
				String codeableConceptId = UUID.randomUUID().toString();
				indexedCodeJson.setCodeableConceptId(codeableConceptId);
				String codeJsonDecoded = objectMapper.writeValueAsString(indexedCodeJson);
				performIndex(OBSERVATION_CODE_INDEX, codeableConceptId, codeJsonDecoded, CODE_DOCUMENT_TYPE);
				if (indexedCodeJsons.size() > 1) {
					// Multiple indexes found. Replace these with the new index.
					mergeCodeIndexes(indexedCodeJsons, indexedCodeJson);
				}
			}
		} catch (IOException theE) {
			throw new InvalidRequestException("Unable to execute observation code document query hash code or display", theE);
		}

		return indexedCodeJson;
	}

	private List<CodeJson> getCodeJsonsFromSearchRequest(SearchRequest theSearchRequest) throws IOException {
		List<CodeJson> codeJsons = new ArrayList<>();
		SearchResponse observationCodeDocumentResponse = executeSearchRequest(theSearchRequest);
		if (observationCodeDocumentResponse.getHits()!=null) {
			SearchHit[] observationCodeDocumentHits = observationCodeDocumentResponse.getHits().getHits();
			for (SearchHit observationCodeDocumentHit : observationCodeDocumentHits) {
				String observationCodeDocument = observationCodeDocumentHit.getSourceAsString();
				codeJsons.add(objectMapper.readValue(observationCodeDocument, CodeJson.class));
			}
		}
		return codeJsons;
	}

	private SearchRequest buildSingleObservationCodeValueSearchRequest(String theSearchValue, String theSearchField) {
		SearchRequest searchRequest = new SearchRequest(OBSERVATION_CODE_INDEX);
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		boolQueryBuilder.must(QueryBuilders.matchPhraseQuery(theSearchField, theSearchValue));

		searchSourceBuilder.query(boolQueryBuilder);
		searchSourceBuilder.size(1);

		searchRequest.source(searchSourceBuilder);

		return searchRequest;
	}

	private void mergeCodeIndexes(Map<String, CodeJson> theOldCodeJsons, CodeJson theNewCodeJson) throws IOException {
		for (String codeId : theOldCodeJsons.keySet()) {
			List<String> observationIds = getObservationsForCodeId(codeId);
			for (String observationId : observationIds) {
				ObservationJson observationJson = getObservationDocument(observationId);
				observationJson.setCode_concept_id(theNewCodeJson.getCodeableConceptId());
				createOrUpdateObservationIndex(observationId, observationJson);
			}
			deleteObservationCodeDocument(codeId);
		}
	}

	private void deleteObservationCodeDocument(String theCodeDocumentId) {
		DeleteByQueryRequest deleteByQueryRequest = new DeleteByQueryRequest(OBSERVATION_CODE_INDEX);
		deleteByQueryRequest.setQuery(QueryBuilders.termQuery(CODE_IDENTIFIER_FIELD_NAME, theCodeDocumentId));
		try {
			myRestHighLevelClient.deleteByQuery(deleteByQueryRequest, RequestOptions.DEFAULT);
		} catch (IOException theE) {
			throw new InvalidRequestException("Unable to delete Observation Code" + theCodeDocumentId);
		}
	}

	@Override
	public Boolean createOrUpdateObservationIndex(String theDocumentId, ObservationJson theObservationDocument){
		// Ensure that we reset the Observation Code document ID.
		return createOrUpdateObservationIndexWithCode(theDocumentId, INITIAL_OBSERVATION_CODE_ID, theObservationDocument);
	}

	private Boolean createOrUpdateObservationIndexWithCode(String theDocumentId, String theCodeId, ObservationJson theObservationDocument) {
		try {
			theObservationDocument.setCode_concept_id(theCodeId);
			String documentToIndex = objectMapper.writeValueAsString(theObservationDocument);
			return performIndex(OBSERVATION_INDEX, theDocumentId, documentToIndex, ElasticsearchSvcImpl.OBSERVATION_DOCUMENT_TYPE);
		} catch (IOException theE) {
			throw new InvalidRequestException("Unable to persist Observation document " + theDocumentId);
		}
	}


/*	@Override
	public Boolean createOrUpdateObservationCodeIndex(String theCodeableConceptID, CodeJson theObservationCodeDocument) {
		try {
			String documentToIndex = objectMapper.writeValueAsString(theObservationCodeDocument);
			return performIndex(OBSERVATION_CODE_INDEX, theCodeableConceptID, documentToIndex, ElasticsearchSvcImpl.CODE_DOCUMENT_TYPE);
		} catch (IOException theE) {
			throw new InvalidRequestException("Unable to persist Observation Code document " + theCodeableConceptID);
		}
	}
*/
	private boolean performIndex(String theIndexName, String theDocumentId, String theIndexDocument, String theDocumentType) throws IOException {
		IndexResponse indexResponse = myRestHighLevelClient.index(createIndexRequest(theIndexName, theDocumentId, theIndexDocument, theDocumentType),
			RequestOptions.DEFAULT);

		return (indexResponse.getResult() == DocWriteResponse.Result.CREATED) || (indexResponse.getResult() == DocWriteResponse.Result.UPDATED);
	}

	@Override
	public void close() throws IOException {
		myRestHighLevelClient.close();
	}

	@Override
	public List<String> getObservationsNeedingCodeUpdate() {
		try {
			return getObservationsForCodeId(INITIAL_OBSERVATION_CODE_ID);
		} catch (IOException theE) {
			throw new InvalidRequestException("Unable to query index for Observations needing code update");
		}
	}

	private List<String> getObservationsForCodeId(String theCodeId) throws IOException {
		SearchRequest searchRequest = new SearchRequest(OBSERVATION_INDEX);
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		// Query
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		boolQueryBuilder.must(QueryBuilders.termsQuery(OBSERVATION_CODE_IDENTIFIER_FIELD_NAME, theCodeId));
		searchSourceBuilder.query(boolQueryBuilder);
		searchSourceBuilder.size(10000);

		searchRequest.source(searchSourceBuilder);
		SearchResponse observationDocumentResponse = executeSearchRequest(searchRequest);
		SearchHit[] observationDocumentHits = observationDocumentResponse.getHits().getHits();
		List<String> observationIdentifiers = new ArrayList<>();
		for (SearchHit observationDocumentHit : observationDocumentHits) {
			String observationDocument = observationDocumentHit.getSourceAsString();
			ObservationJson observationDocumentJson = objectMapper.readValue(observationDocument, ObservationJson.class);
			observationIdentifiers.add(observationDocumentJson.getIdentifier());
		}
		return observationIdentifiers;
	}

	@Override
	public void updateObservationCode(String theDocumentId) {
		ObservationJson observationJson = getObservationDocument(theDocumentId);
		CodeJson codeJson = getOrCreateObservationCodeDocument(observationJson);
		createOrUpdateObservationIndexWithCode(theDocumentId, codeJson.getCodeableConceptId(), observationJson);
	}

	private IndexRequest createIndexRequest(String theIndexName, String theDocumentId, String theObservationDocument, String theDocumentType) {
		IndexRequest request = new IndexRequest(theIndexName);
		request.id(theDocumentId);
		request.type(theDocumentType);

		request.source(theObservationDocument, XContentType.JSON);
		return request;
	}

	@Override
	public void deleteObservationDocument(String theDocumentId) {
		DeleteByQueryRequest deleteByQueryRequest = new DeleteByQueryRequest(OBSERVATION_INDEX);
		deleteByQueryRequest.setQuery(QueryBuilders.termQuery(OBSERVATION_IDENTIFIER_FIELD_NAME, theDocumentId));
		try {
			myRestHighLevelClient.deleteByQuery(deleteByQueryRequest, RequestOptions.DEFAULT);
		} catch (IOException theE) {
			throw new InvalidRequestException("Unable to delete Observation " + theDocumentId);
		}
	}

	@VisibleForTesting
	public void deleteAllDocumentsForTest(String theIndexName) throws IOException {
		DeleteByQueryRequest deleteByQueryRequest = new DeleteByQueryRequest(theIndexName);
		deleteByQueryRequest.setQuery(QueryBuilders.matchAllQuery());
		myRestHighLevelClient.deleteByQuery(deleteByQueryRequest, RequestOptions.DEFAULT);
	}

	@VisibleForTesting
	public void refreshIndex(String theIndexName) throws IOException {
		myRestHighLevelClient.indices().refresh(new RefreshRequest(theIndexName), RequestOptions.DEFAULT);
	}

}
