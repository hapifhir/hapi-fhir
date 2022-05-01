package ca.uhn.fhir.jpa.search.lastn;

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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.TolerantJsonParser;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.IBaseResourceEntity;
import ca.uhn.fhir.jpa.model.util.CodeSystemHash;
import ca.uhn.fhir.jpa.search.lastn.json.CodeJson;
import ca.uhn.fhir.jpa.search.lastn.json.ObservationJson;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.util.LastNParameterHelper;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.Validate;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.refresh.RefreshRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.composite.CompositeAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.composite.CompositeValuesSourceBuilder;
import org.elasticsearch.search.aggregations.bucket.composite.ParsedComposite;
import org.elasticsearch.search.aggregations.bucket.composite.TermsValuesSourceBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.ParsedTopHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.xcontent.XContentType;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class ElasticsearchSvcImpl implements IElasticsearchSvc {

	private static final Logger ourLog = LoggerFactory.getLogger(ElasticsearchSvcImpl.class);

	// Index Constants
	public static final String OBSERVATION_INDEX = "observation_index";
	public static final String OBSERVATION_CODE_INDEX = "code_index";
	public static final String OBSERVATION_DOCUMENT_TYPE = "ca.uhn.fhir.jpa.model.entity.ObservationIndexedSearchParamLastNEntity";
	public static final String CODE_DOCUMENT_TYPE = "ca.uhn.fhir.jpa.model.entity.ObservationIndexedCodeCodeableConceptEntity";
	public static final String OBSERVATION_INDEX_SCHEMA_FILE = "ObservationIndexSchema.json";
	public static final String OBSERVATION_CODE_INDEX_SCHEMA_FILE = "ObservationCodeIndexSchema.json";

	// Aggregation Constants
	private static final String GROUP_BY_SUBJECT = "group_by_subject";
	private static final String GROUP_BY_SYSTEM = "group_by_system";
	private static final String GROUP_BY_CODE = "group_by_code";
	private static final String MOST_RECENT_EFFECTIVE = "most_recent_effective";

	// Observation index document element names
	private static final String OBSERVATION_IDENTIFIER_FIELD_NAME = "identifier";
	private static final String OBSERVATION_SUBJECT_FIELD_NAME = "subject";
	private static final String OBSERVATION_CODEVALUE_FIELD_NAME = "codeconceptcodingcode";
	private static final String OBSERVATION_CODESYSTEM_FIELD_NAME = "codeconceptcodingsystem";
	private static final String OBSERVATION_CODEHASH_FIELD_NAME = "codeconceptcodingcode_system_hash";
	private static final String OBSERVATION_CODEDISPLAY_FIELD_NAME = "codeconceptcodingdisplay";
	private static final String OBSERVATION_CODE_TEXT_FIELD_NAME = "codeconcepttext";
	private static final String OBSERVATION_EFFECTIVEDTM_FIELD_NAME = "effectivedtm";
	private static final String OBSERVATION_CATEGORYHASH_FIELD_NAME = "categoryconceptcodingcode_system_hash";
	private static final String OBSERVATION_CATEGORYVALUE_FIELD_NAME = "categoryconceptcodingcode";
	private static final String OBSERVATION_CATEGORYSYSTEM_FIELD_NAME = "categoryconceptcodingsystem";
	private static final String OBSERVATION_CATEGORYDISPLAY_FIELD_NAME = "categoryconceptcodingdisplay";
	private static final String OBSERVATION_CATEGORYTEXT_FIELD_NAME = "categoryconcepttext";

	// Code index document element names
	private static final String CODE_HASH = "codingcode_system_hash";
	private static final String CODE_TEXT = "text";

	private static final String OBSERVATION_RESOURCE_NAME = "Observation";

	private final RestHighLevelClient myRestHighLevelClient;

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Autowired
	private PartitionSettings myPartitionSettings;

	@Autowired
	private FhirContext myContext;

	//This constructor used to inject a dummy partitionsettings in test.
	public ElasticsearchSvcImpl(PartitionSettings thePartitionSetings, String theProtocol, String theHostname, @Nullable String theUsername, @Nullable String thePassword) {
		this(theProtocol, theHostname, theUsername, thePassword);
		this.myPartitionSettings = thePartitionSetings;
	}

	public ElasticsearchSvcImpl(String theProtocol, String theHostname, @Nullable String theUsername, @Nullable String thePassword) {
		myRestHighLevelClient = ElasticsearchRestClientFactory.createElasticsearchHighLevelRestClient(theProtocol, theHostname, theUsername, thePassword);

		try {
			createObservationIndexIfMissing();
			createObservationCodeIndexIfMissing();
		} catch (IOException theE) {
			throw new RuntimeException(Msg.code(1175) + "Failed to create document index", theE);
		}
	}

	private String getIndexSchema(String theSchemaFileName) throws IOException {
		InputStreamReader input = new InputStreamReader(ElasticsearchSvcImpl.class.getResourceAsStream(theSchemaFileName));
		BufferedReader reader = new BufferedReader(input);
		StringBuilder sb = new StringBuilder();
		String str;
		while ((str = reader.readLine()) != null) {
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
			throw new RuntimeException(Msg.code(1176) + "Failed to create observation index");
		}
	}

	private void createObservationCodeIndexIfMissing() throws IOException {
		if (indexExists(OBSERVATION_CODE_INDEX)) {
			return;
		}
		String observationCodeMapping = getIndexSchema(OBSERVATION_CODE_INDEX_SCHEMA_FILE);
		if (!createIndex(OBSERVATION_CODE_INDEX, observationCodeMapping)) {
			throw new RuntimeException(Msg.code(1177) + "Failed to create observation code index");
		}

	}

	private boolean createIndex(String theIndexName, String theMapping) throws IOException {
		CreateIndexRequest request = new CreateIndexRequest(theIndexName);
		request.source(theMapping, XContentType.JSON);
		CreateIndexResponse createIndexResponse = myRestHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
		return createIndexResponse.isAcknowledged();

	}

	private boolean indexExists(String theIndexName) throws IOException {
		GetIndexRequest request = new GetIndexRequest(theIndexName);
		return myRestHighLevelClient.indices().exists(request, RequestOptions.DEFAULT);
	}

	@Override
	public List<String> executeLastN(SearchParameterMap theSearchParameterMap, FhirContext theFhirContext, Integer theMaxResultsToFetch) {
		Validate.isTrue(!myPartitionSettings.isPartitioningEnabled(), "$lastn is not currently supported on partitioned servers");

		String[] topHitsInclude = {OBSERVATION_IDENTIFIER_FIELD_NAME};
		return buildAndExecuteSearch(theSearchParameterMap, theFhirContext, topHitsInclude,
			ObservationJson::getIdentifier, theMaxResultsToFetch);
	}

	private <T> List<T> buildAndExecuteSearch(SearchParameterMap theSearchParameterMap, FhirContext theFhirContext,
															String[] topHitsInclude, Function<ObservationJson, T> setValue, Integer theMaxResultsToFetch) {
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
				ourLog.debug("ElasticSearch query: {}", myLastNRequest.source().toString());
				try {
					SearchResponse lastnResponse = executeSearchRequest(myLastNRequest);
					searchResults.addAll(buildObservationList(lastnResponse, setValue, theSearchParameterMap, theFhirContext,
						theMaxResultsToFetch));
				} catch (IOException theE) {
					throw new InvalidRequestException(Msg.code(1178) + "Unable to execute LastN request", theE);
				}
			}
		} else {
			SearchRequest myLastNRequest = buildObservationsSearchRequest(theSearchParameterMap, theFhirContext,
				createObservationCodeAggregationBuilder(getMaxParameter(theSearchParameterMap), topHitsInclude));
			ourLog.debug("ElasticSearch query: {}", myLastNRequest.source().toString());
			try {
				SearchResponse lastnResponse = executeSearchRequest(myLastNRequest);
				searchResults.addAll(buildObservationList(lastnResponse, setValue, theSearchParameterMap, theFhirContext,
					theMaxResultsToFetch));
			} catch (IOException theE) {
				throw new InvalidRequestException(Msg.code(1179) + "Unable to execute LastN request", theE);
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
				throw new IllegalArgumentException(Msg.code(1180) + "Invalid token type (expecting ReferenceParam): " + nextOr.getClass());
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
		TermsAggregationBuilder observationCodeCodeAggregationBuilder = new TermsAggregationBuilder(GROUP_BY_CODE).field(OBSERVATION_CODEVALUE_FIELD_NAME);
		observationCodeCodeAggregationBuilder.order(BucketOrder.key(true));
		// Top Hits Aggregation
		observationCodeCodeAggregationBuilder.subAggregation(AggregationBuilders.topHits(MOST_RECENT_EFFECTIVE)
			.sort(OBSERVATION_EFFECTIVEDTM_FIELD_NAME, SortOrder.DESC)
			.fetchSource(theTopHitsInclude, null).size(theMaxNumberObservationsPerCode));
		observationCodeCodeAggregationBuilder.size(10000);
		TermsAggregationBuilder observationCodeSystemAggregationBuilder = new TermsAggregationBuilder(GROUP_BY_SYSTEM).field(OBSERVATION_CODESYSTEM_FIELD_NAME);
		observationCodeSystemAggregationBuilder.order(BucketOrder.key(true));
		observationCodeSystemAggregationBuilder.subAggregation(observationCodeCodeAggregationBuilder);
		return observationCodeSystemAggregationBuilder;
	}

	private SearchResponse executeSearchRequest(SearchRequest searchRequest) throws IOException {
		return myRestHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
	}

	private <T> List<T> buildObservationList(SearchResponse theSearchResponse, Function<ObservationJson, T> setValue,
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
		ParsedTerms aggregatedObservationCodeSystems = theObservationCodeSystemAggregations.get(GROUP_BY_SYSTEM);
		for (Terms.Bucket observationCodeSystem : aggregatedObservationCodeSystems.getBuckets()) {
			Aggregations observationCodeCodeAggregations = observationCodeSystem.getAggregations();
			ParsedTerms aggregatedObservationCodeCodes = observationCodeCodeAggregations.get(GROUP_BY_CODE);
			retVal.addAll(aggregatedObservationCodeCodes.getBuckets());
		}
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
				throw new IllegalArgumentException(Msg.code(1181) + "Invalid token type (expecting TokenParam): " + nextOr.getClass());
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
				throw new IllegalArgumentException(Msg.code(1182) + "Invalid token type (expecting TokenParam): " + nextOr.getClass());
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
				throw new IllegalArgumentException(Msg.code(1183) + "Invalid token type (expecting TokenParam): " + nextOr.getClass());
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
				throw new IllegalArgumentException(Msg.code(1184) + "Invalid token type (expecting TokenParam): " + nextOr.getClass());
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
		return buildAndExecuteSearch(theSearchParameterMap, theFhirContext, null, t -> t, 100);
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
			throw new InvalidRequestException(Msg.code(1185) + "Require non-null document ID for observation document query");
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
			throw new InvalidRequestException(Msg.code(1186) + "Unable to execute observation document query for ID " + theDocumentID, theE);
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

	@Override
	public CodeJson getObservationCodeDocument(String theCodeSystemHash, String theText) {
		if (theCodeSystemHash == null && theText == null) {
			throw new InvalidRequestException(Msg.code(1187) + "Require a non-null code system hash value or display value for observation code document query");
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
			throw new InvalidRequestException(Msg.code(1188) + "Unable to execute observation code document query hash code or display", theE);
		}

		return observationCodeDocumentJson;
	}

	private SearchRequest buildSingleObservationCodeSearchRequest(String theCodeSystemHash, String theText) {
		SearchRequest searchRequest = new SearchRequest(OBSERVATION_CODE_INDEX);
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		if (theCodeSystemHash != null) {
			boolQueryBuilder.must(QueryBuilders.termQuery(CODE_HASH, theCodeSystemHash));
		} else {
			boolQueryBuilder.must(QueryBuilders.matchPhraseQuery(CODE_TEXT, theText));
		}

		searchSourceBuilder.query(boolQueryBuilder);
		searchSourceBuilder.size(1);

		searchRequest.source(searchSourceBuilder);

		return searchRequest;
	}

	@Override
	public Boolean createOrUpdateObservationIndex(String theDocumentId, ObservationJson theObservationDocument) {
		try {
			String documentToIndex = objectMapper.writeValueAsString(theObservationDocument);
			return performIndex(OBSERVATION_INDEX, theDocumentId, documentToIndex, ElasticsearchSvcImpl.OBSERVATION_DOCUMENT_TYPE);
		} catch (IOException theE) {
			throw new InvalidRequestException(Msg.code(1189) + "Unable to persist Observation document " + theDocumentId);
		}
	}

	@Override
	public Boolean createOrUpdateObservationCodeIndex(String theCodeableConceptID, CodeJson theObservationCodeDocument) {
		try {
			String documentToIndex = objectMapper.writeValueAsString(theObservationCodeDocument);
			return performIndex(OBSERVATION_CODE_INDEX, theCodeableConceptID, documentToIndex, ElasticsearchSvcImpl.CODE_DOCUMENT_TYPE);
		} catch (IOException theE) {
			throw new InvalidRequestException(Msg.code(1190) + "Unable to persist Observation Code document " + theCodeableConceptID);
		}
	}

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
	public List<IBaseResource> getObservationResources(Collection<ResourcePersistentId> thePids) {
		SearchRequest searchRequest = buildObservationResourceSearchRequest(thePids);
		try {
			SearchResponse observationDocumentResponse = executeSearchRequest(searchRequest);
			SearchHit[] observationDocumentHits = observationDocumentResponse.getHits().getHits();
			IParser parser = TolerantJsonParser.createWithLenientErrorHandling(myContext, null);
			Class<? extends IBaseResource> resourceType = myContext.getResourceDefinition(OBSERVATION_RESOURCE_NAME).getImplementingClass();
			/**
			 * @see ca.uhn.fhir.jpa.dao.BaseHapiFhirDao#toResource(Class, IBaseResourceEntity, Collection, boolean) for
			 * details about parsing raw json to BaseResource
			 */
			return Arrays.stream(observationDocumentHits)
				.map(this::parseObservationJson)
				.map(observationJson -> parser.parseResource(resourceType, observationJson.getResource()))
				.collect(Collectors.toList());
		} catch (IOException theE) {
			throw new InvalidRequestException(Msg.code(2003) + "Unable to execute observation document query for provided IDs " + thePids, theE);
		}
	}

	private ObservationJson parseObservationJson(SearchHit theSearchHit) {
		try {
			return objectMapper.readValue(theSearchHit.getSourceAsString(), ObservationJson.class);
		} catch (JsonProcessingException exp) {
			throw new InvalidRequestException(Msg.code(2004) + "Unable to parse the observation resource json", exp);
		}
	}

	private SearchRequest buildObservationResourceSearchRequest(Collection<ResourcePersistentId> thePids) {
		SearchRequest searchRequest = new SearchRequest(OBSERVATION_INDEX);
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		// Query
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		List<String> pidParams = thePids.stream().map(Object::toString).collect(Collectors.toList());
		boolQueryBuilder.must(QueryBuilders.termsQuery(OBSERVATION_IDENTIFIER_FIELD_NAME, pidParams));
		searchSourceBuilder.query(boolQueryBuilder);
		searchSourceBuilder.size(thePids.size());
		searchRequest.source(searchSourceBuilder);
		return searchRequest;
	}


	private IndexRequest createIndexRequest(String theIndexName, String theDocumentId, String theObservationDocument, String theDocumentType) {
		IndexRequest request = new IndexRequest(theIndexName);
		request.id(theDocumentId);
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
			throw new InvalidRequestException(Msg.code(1191) + "Unable to delete Observation " + theDocumentId);
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
