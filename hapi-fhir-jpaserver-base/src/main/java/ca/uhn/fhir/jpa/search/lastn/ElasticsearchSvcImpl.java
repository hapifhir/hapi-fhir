package ca.uhn.fhir.jpa.search.lastn;

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.jpa.search.lastn.json.CodeJson;
import ca.uhn.fhir.jpa.search.lastn.json.ObservationJson;
import ca.uhn.fhir.jpa.search.lastn.util.CodeSystemHash;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.annotations.VisibleForTesting;
import org.shadehapi.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.shadehapi.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.shadehapi.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.shadehapi.elasticsearch.action.DocWriteResponse;
import org.shadehapi.elasticsearch.action.index.IndexRequest;
import org.shadehapi.elasticsearch.action.index.IndexResponse;
import org.shadehapi.elasticsearch.action.search.SearchRequest;
import org.shadehapi.elasticsearch.action.search.SearchResponse;
import org.shadehapi.elasticsearch.client.RequestOptions;
import org.shadehapi.elasticsearch.client.RestHighLevelClient;
import org.shadehapi.elasticsearch.common.xcontent.XContentType;
import org.shadehapi.elasticsearch.index.query.BoolQueryBuilder;
import org.shadehapi.elasticsearch.index.query.QueryBuilders;
import org.shadehapi.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.shadehapi.elasticsearch.search.SearchHit;
import org.shadehapi.elasticsearch.search.SearchHits;
import org.shadehapi.elasticsearch.search.aggregations.AggregationBuilder;
import org.shadehapi.elasticsearch.search.aggregations.AggregationBuilders;
import org.shadehapi.elasticsearch.search.aggregations.Aggregations;
import org.shadehapi.elasticsearch.search.aggregations.bucket.composite.*;
import org.shadehapi.elasticsearch.search.aggregations.bucket.terms.ParsedTerms;
import org.shadehapi.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.shadehapi.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.shadehapi.elasticsearch.search.aggregations.metrics.tophits.ParsedTopHits;
import org.shadehapi.elasticsearch.search.aggregations.support.ValueType;
import org.shadehapi.elasticsearch.search.builder.SearchSourceBuilder;
import org.shadehapi.elasticsearch.search.sort.SortOrder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class ElasticsearchSvcImpl implements IElasticsearchSvc {

	private final RestHighLevelClient myRestHighLevelClient;

	private final ObjectMapper objectMapper = new ObjectMapper();

	private final String GROUP_BY_SUBJECT = "group_by_subject";
	private final String GROUP_BY_CODE = "group_by_code";
	private final String OBSERVATION_IDENTIFIER_FIELD_NAME = "identifier";


	public ElasticsearchSvcImpl(String theHostname, int thePort, String theUsername, String thePassword) {
		myRestHighLevelClient = ElasticsearchRestClientFactory.createElasticsearchHighLevelRestClient(theHostname, thePort, theUsername, thePassword);

		try {
			createObservationIndexIfMissing();
			createCodeIndexIfMissing();
		} catch (IOException theE) {
			throw new RuntimeException("Failed to create document index", theE);
		}
	}

	private void createObservationIndexIfMissing() throws IOException {
		if (indexExists(IndexConstants.OBSERVATION_INDEX)) {
			return;
		}
		String observationMapping = "{\n" +
			"  \"mappings\" : {\n" +
			"    \"ca.uhn.fhir.jpa.dao.lastn.entity.ObservationIndexedSearchParamLastNEntity\" : {\n" +
			"      \"properties\" : {\n" +
			"        \"codeconceptid\" : {\n" +
			"          \"type\" : \"keyword\",\n" +
			"          \"norms\" : true\n" +
			"        },\n" +
			"        \"codeconcepttext\" : {\n" +
			"          \"type\" : \"text\"\n" +
			"        },\n" +
			"        \"codeconceptcodingcode\" : {\n" +
			"          \"type\" : \"keyword\"\n" +
			"        },\n" +
			"        \"codeconceptcodingsystem\" : {\n" +
			"          \"type\" : \"keyword\"\n" +
			"        },\n" +
			"        \"codeconceptcodingcode_system_hash\" : {\n" +
			"          \"type\" : \"keyword\"\n" +
			"        },\n" +
			"        \"codeconceptcodingdisplay\" : {\n" +
			"          \"type\" : \"text\"\n" +
			"        },\n" +
			"        \"categoryconcepttext\" : {\n" +
			"          \"type\" : \"text\"\n" +
			"        },\n" +
			"        \"categoryconceptcodingcode\" : {\n" +
			"          \"type\" : \"keyword\"\n" +
			"        },\n" +
			"        \"categoryconceptcodingsystem\" : {\n" +
			"          \"type\" : \"keyword\"\n" +
			"        },\n" +
			"        \"categoryconceptcodingcode_system_hash\" : {\n" +
			"          \"type\" : \"keyword\"\n" +
			"        },\n" +
			"        \"categoryconceptcodingdisplay\" : {\n" +
			"          \"type\" : \"text\"\n" +
			"        },\n" +
			"        \"effectivedtm\" : {\n" +
			"          \"type\" : \"date\"\n" +
			"        },\n" +
			"        \"identifier\" : {\n" +
			"          \"type\" : \"keyword\",\n" +
			"          \"store\" : true\n" +
			"        },\n" +
			"        \"subject\" : {\n" +
			"          \"type\" : \"keyword\"\n" +
			"        }\n" +
			"      }\n" +
			"    }\n" +
			"  }\n" +
			"}\n";
		if (!createIndex(IndexConstants.OBSERVATION_INDEX, observationMapping)) {
			throw new RuntimeException("Failed to create observation index");
		}

	}

	private void createCodeIndexIfMissing() throws IOException {
		if (indexExists(IndexConstants.CODE_INDEX)) {
			return;
		}
		String codeMapping = "{\n" +
			"  \"mappings\" : {\n" +
			"    \"ca.uhn.fhir.jpa.dao.lastn.entity.ObservationIndexedCodeCodeableConceptEntity\" : {\n" +
			"      \"properties\" : {\n" +
			"        \"codeable_concept_id\" : {\n" +
			"          \"type\" : \"keyword\",\n" +
			"          \"store\" : true\n" +
			"        },\n" +
			"        \"codeable_concept_text\" : {\n" +
			"          \"type\" : \"text\"\n" +
			"        },\n" +
			"        \"codingcode\" : {\n" +
			"          \"type\" : \"keyword\"\n" +
			"        },\n" +
			"        \"codingcode_system_hash\" : {\n" +
			"          \"type\" : \"keyword\"\n" +
			"        },\n" +
			"        \"codingdisplay\" : {\n" +
			"          \"type\" : \"text\"\n" +
			"        },\n" +
			"        \"codingsystem\" : {\n" +
			"          \"type\" : \"keyword\"\n" +
			"        }\n" +
			"      }\n" +
			"    }\n" +
			"  }\n" +
			"}\n";
		if (!createIndex(IndexConstants.CODE_INDEX, codeMapping)) {
			throw new RuntimeException("Failed to create code index");
		}

	}

	private boolean createIndex(String theIndexName, String theMapping) throws IOException {
		CreateIndexRequest request = new CreateIndexRequest(theIndexName);
		request.source(theMapping, XContentType.JSON);
		CreateIndexResponse createIndexResponse = myRestHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
		return createIndexResponse.isAcknowledged();

	}

	@VisibleForTesting
	boolean performIndex(String theIndexName, String theDocumentId, String theIndexDocument, String theDocumentType) throws IOException {
		IndexResponse indexResponse = myRestHighLevelClient.index(createIndexRequest(theIndexName, theDocumentId, theIndexDocument, theDocumentType),
			RequestOptions.DEFAULT);

		return (indexResponse.getResult() == DocWriteResponse.Result.CREATED) || (indexResponse.getResult() == DocWriteResponse.Result.UPDATED);
	}

	private IndexRequest createIndexRequest(String theIndexName, String theDocumentId, String theObservationDocument, String theDocumentType) {
		IndexRequest request = new IndexRequest(theIndexName);
		request.id(theDocumentId);
		request.type(theDocumentType);

		request.source(theObservationDocument, XContentType.JSON);
		return request;
	}

	private boolean indexExists(String theIndexName) throws IOException {
		GetIndexRequest request = new GetIndexRequest();
		request.indices(theIndexName);
		return myRestHighLevelClient.indices().exists(request, RequestOptions.DEFAULT);
	}

	@Override
	// TODO: Should eliminate dependency on SearchParameterMap in API.
	public List<String> executeLastN(SearchParameterMap theSearchParameterMap, Integer theMaxObservationsPerCode, Integer theMaxResultsToFetch) {
		String[] topHitsInclude = {OBSERVATION_IDENTIFIER_FIELD_NAME};
		try {
			List<SearchResponse> responses = buildAndExecuteSearch(theSearchParameterMap, theMaxObservationsPerCode, topHitsInclude);
			List<String> observationIds = new ArrayList<>();
			for (SearchResponse response : responses) {
//				observationIds.addAll(buildObservationIdList(response));
				Integer maxResultsToAdd = theMaxResultsToFetch - observationIds.size();
				observationIds.addAll(buildObservationList(response, t -> t.getIdentifier(), theSearchParameterMap, maxResultsToAdd));
			}
			return observationIds;
		} catch (IOException theE) {
			throw new InvalidRequestException("Unable to execute LastN request", theE);
		}
	}

	private List<SearchResponse> buildAndExecuteSearch(SearchParameterMap theSearchParameterMap, Integer theMaxObservationsPerCode,
																		String[] topHitsInclude) {
		List<SearchResponse> responses = new ArrayList<>();
		if (theSearchParameterMap.containsKey(IndexConstants.PATIENT_SEARCH_PARAM) || theSearchParameterMap.containsKey(IndexConstants.SUBJECT_SEARCH_PARAM)) {
			ArrayList<String> subjectReferenceCriteria = new ArrayList<>();
			List<List<IQueryParameterType>> patientParams = new ArrayList<>();
			if (theSearchParameterMap.get(IndexConstants.PATIENT_SEARCH_PARAM) != null) {
				patientParams.addAll(theSearchParameterMap.get(IndexConstants.PATIENT_SEARCH_PARAM));
			}
			if (theSearchParameterMap.get(IndexConstants.SUBJECT_SEARCH_PARAM) != null) {
				patientParams.addAll(theSearchParameterMap.get(IndexConstants.SUBJECT_SEARCH_PARAM));
			}
			for (List<? extends IQueryParameterType> nextSubjectList : patientParams) {
				subjectReferenceCriteria.addAll(getReferenceValues(nextSubjectList));
			}
			for (String subject : subjectReferenceCriteria) {
				SearchRequest myLastNRequest = buildObservationsSearchRequest(subject, theSearchParameterMap, createCompositeAggregationBuilder(theMaxObservationsPerCode, topHitsInclude));
				try {
					SearchResponse lastnResponse = executeSearchRequest(myLastNRequest);
					responses.add(lastnResponse);
				} catch (IOException theE) {
					throw new InvalidRequestException("Unable to execute LastN request", theE);
				}
			}
		} else {
			SearchRequest myLastNRequest = buildObservationsSearchRequest(theSearchParameterMap, createObservationCodeAggregationBuilder(theMaxObservationsPerCode, topHitsInclude));
			try {
				SearchResponse lastnResponse = executeSearchRequest(myLastNRequest);
				responses.add(lastnResponse);
			} catch (IOException theE) {
				throw new InvalidRequestException("Unable to execute LastN request", theE);
			}

		}
		return responses;
	}

	@VisibleForTesting
	// TODO: Should eliminate dependency on SearchParameterMap in API.
	List<ObservationJson> executeLastNWithAllFields(SearchParameterMap theSearchParameterMap, Integer theMaxObservationsPerCode, Integer theMaxResultsToFetch) {
		try {
			List<SearchResponse> responses = buildAndExecuteSearch(theSearchParameterMap, theMaxObservationsPerCode, null);
			List<ObservationJson> observationDocuments = new ArrayList<>();
			for (SearchResponse response : responses) {
				observationDocuments.addAll(buildObservationList(response, t -> t, theSearchParameterMap, theMaxResultsToFetch));
			}
			return observationDocuments;
		} catch (IOException theE) {
			throw new InvalidRequestException("Unable to execute LastN request", theE);
		}
	}

	@VisibleForTesting
	List<CodeJson> queryAllIndexedObservationCodes(int theMaxResultSetSize) throws IOException {
		SearchRequest codeSearchRequest = buildObservationCodesSearchRequest(theMaxResultSetSize);
		SearchResponse codeSearchResponse = executeSearchRequest(codeSearchRequest);
		return buildCodeResult(codeSearchResponse);
	}

	private SearchRequest buildObservationCodesSearchRequest(int theMaxResultSetSize) {
		SearchRequest searchRequest = new SearchRequest(IndexConstants.CODE_INDEX);
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		// Query
		searchSourceBuilder.query(QueryBuilders.matchAllQuery());
		searchSourceBuilder.size(theMaxResultSetSize);
		searchRequest.source(searchSourceBuilder);
		return searchRequest;
	}

	private CompositeAggregationBuilder createCompositeAggregationBuilder(int theMaxNumberObservationsPerCode, String[] theTopHitsInclude) {
		CompositeValuesSourceBuilder<?> subjectValuesBuilder = new TermsValuesSourceBuilder("subject").field("subject");
		List<CompositeValuesSourceBuilder<?>> compositeAggSubjectSources = new ArrayList();
		compositeAggSubjectSources.add(subjectValuesBuilder);
		CompositeAggregationBuilder compositeAggregationSubjectBuilder = new CompositeAggregationBuilder(GROUP_BY_SUBJECT, compositeAggSubjectSources);
		compositeAggregationSubjectBuilder.subAggregation(createObservationCodeAggregationBuilder(theMaxNumberObservationsPerCode, theTopHitsInclude));
		compositeAggregationSubjectBuilder.size(10000);

		return compositeAggregationSubjectBuilder;
	}

	private TermsAggregationBuilder createObservationCodeAggregationBuilder(int theMaxNumberObservationsPerCode, String[] theTopHitsInclude) {
		TermsAggregationBuilder observationCodeAggregationBuilder = new TermsAggregationBuilder(GROUP_BY_CODE, ValueType.STRING).field("codeconceptid");
		// Top Hits Aggregation
		observationCodeAggregationBuilder.subAggregation(AggregationBuilders.topHits("most_recent_effective")
			.sort("effectivedtm", SortOrder.DESC)
			.fetchSource(theTopHitsInclude, null).size(theMaxNumberObservationsPerCode));
		observationCodeAggregationBuilder.size(10000);
		return observationCodeAggregationBuilder;
	}

	public SearchResponse executeSearchRequest(SearchRequest searchRequest) throws IOException {
		return myRestHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
	}

	private List<String> buildObservationIdList(SearchResponse theSearchResponse) throws IOException {
		List<String> theObservationList = new ArrayList<>();
		for (ParsedComposite.ParsedBucket subjectBucket : getSubjectBuckets(theSearchResponse)) {
			for (Terms.Bucket observationCodeBucket : getObservationCodeBuckets(subjectBucket)) {
				for (SearchHit lastNMatch : getLastNMatches(observationCodeBucket)) {
					String indexedObservation = lastNMatch.getSourceAsString();
					ObservationJson observationJson = objectMapper.readValue(indexedObservation, ObservationJson.class);
					theObservationList.add(observationJson.getIdentifier());
				}
			}
		}
		return theObservationList;
	}

	private List<ObservationJson> buildObservationDocumentList(SearchResponse theSearchResponse) throws IOException {
		List<ObservationJson> theObservationList = new ArrayList<>();
		for (ParsedComposite.ParsedBucket subjectBucket : getSubjectBuckets(theSearchResponse)) {
			for (Terms.Bucket observationCodeBucket : getObservationCodeBuckets(subjectBucket)) {
				for (SearchHit lastNMatch : getLastNMatches(observationCodeBucket)) {
					String indexedObservation = lastNMatch.getSourceAsString();
					ObservationJson observationJson = objectMapper.readValue(indexedObservation, ObservationJson.class);
					theObservationList.add(observationJson);
				}
			}
		}
		return theObservationList;
	}

	private <T> List<T> buildObservationList(SearchResponse theSearchResponse, Function<ObservationJson,T> setValue,
														  SearchParameterMap theSearchParameterMap, Integer theMaxResultsToFetch) throws IOException {
		List<T> theObservationList = new ArrayList<>();
		if (theSearchParameterMap.containsKey(IndexConstants.PATIENT_SEARCH_PARAM) || theSearchParameterMap.containsKey(IndexConstants.SUBJECT_SEARCH_PARAM)) {
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
		ParsedTerms aggregatedObservationCodes = responseAggregations.get(GROUP_BY_CODE);
		return aggregatedObservationCodes.getBuckets();
	}

	private List<? extends Terms.Bucket> getObservationCodeBuckets(ParsedComposite.ParsedBucket theSubjectBucket) {
		Aggregations observationCodeAggregations = theSubjectBucket.getAggregations();
		ParsedTerms aggregatedObservationCodes = observationCodeAggregations.get(GROUP_BY_CODE);
		return aggregatedObservationCodes.getBuckets();
	}

	private SearchHit[] getLastNMatches(Terms.Bucket theObservationCodeBucket) {
		Aggregations topHitObservationCodes = theObservationCodeBucket.getAggregations();
		ParsedTopHits parsedTopHits = topHitObservationCodes.get("most_recent_effective");
		return parsedTopHits.getHits().getHits();
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

	private SearchRequest buildObservationsSearchRequest(SearchParameterMap theSearchParameterMap, AggregationBuilder theAggregationBuilder) {
		SearchRequest searchRequest = new SearchRequest(IndexConstants.OBSERVATION_INDEX);
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		// Query
		if (!searchParamsHaveLastNCriteria(theSearchParameterMap)) {
			searchSourceBuilder.query(QueryBuilders.matchAllQuery());
		} else {
			BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
			addSubjectsCriteria(boolQueryBuilder, theSearchParameterMap);
			addCategoriesCriteria(boolQueryBuilder, theSearchParameterMap);
			addObservationCodeCriteria(boolQueryBuilder, theSearchParameterMap);
			searchSourceBuilder.query(boolQueryBuilder);
		}
		searchSourceBuilder.size(0);

		// Aggregation by order codes
		searchSourceBuilder.aggregation(theAggregationBuilder);
		searchRequest.source(searchSourceBuilder);

		return searchRequest;
	}

	private SearchRequest buildObservationsSearchRequest(String theSubjectParam, SearchParameterMap theSearchParameterMap, AggregationBuilder theAggregationBuilder) {
		SearchRequest searchRequest = new SearchRequest(IndexConstants.OBSERVATION_INDEX);
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		// Query
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		boolQueryBuilder.must(QueryBuilders.termQuery("subject", theSubjectParam));
		addCategoriesCriteria(boolQueryBuilder, theSearchParameterMap);
		addObservationCodeCriteria(boolQueryBuilder, theSearchParameterMap);
		searchSourceBuilder.query(boolQueryBuilder);
		searchSourceBuilder.size(0);

		// Aggregation by order codes
		searchSourceBuilder.aggregation(theAggregationBuilder);
		searchRequest.source(searchSourceBuilder);

		return searchRequest;
	}

	private Boolean searchParamsHaveLastNCriteria(SearchParameterMap theSearchParameterMap) {
		return theSearchParameterMap != null &&
			(theSearchParameterMap.containsKey(IndexConstants.PATIENT_SEARCH_PARAM) || theSearchParameterMap.containsKey(IndexConstants.SUBJECT_SEARCH_PARAM) ||
				theSearchParameterMap.containsKey(IndexConstants.CATEGORY_SEARCH_PARAM) || theSearchParameterMap.containsKey(IndexConstants.CODE_SEARCH_PARAM));
	}

	private void addSubjectsCriteria(BoolQueryBuilder theBoolQueryBuilder, SearchParameterMap theSearchParameterMap) {
		if (theSearchParameterMap.containsKey(IndexConstants.PATIENT_SEARCH_PARAM) || theSearchParameterMap.containsKey(IndexConstants.SUBJECT_SEARCH_PARAM)) {
			ArrayList<String> subjectReferenceCriteria = new ArrayList<>();
			List<List<IQueryParameterType>> andOrParams = new ArrayList<>();
			if (theSearchParameterMap.get(IndexConstants.PATIENT_SEARCH_PARAM) != null) {
				andOrParams.addAll(theSearchParameterMap.get(IndexConstants.PATIENT_SEARCH_PARAM));
			}
			if (theSearchParameterMap.get(IndexConstants.SUBJECT_SEARCH_PARAM) != null) {
				andOrParams.addAll(theSearchParameterMap.get(IndexConstants.SUBJECT_SEARCH_PARAM));
			}
			for (List<? extends IQueryParameterType> nextAnd : andOrParams) {
				subjectReferenceCriteria.addAll(getReferenceValues(nextAnd));
			}
			if (subjectReferenceCriteria.size() > 0) {
				theBoolQueryBuilder.must(QueryBuilders.termsQuery("subject", subjectReferenceCriteria));
			}
		}

	}

	private List<String> getReferenceValues(List<? extends IQueryParameterType> referenceParams) {
		ArrayList<String> referenceList = new ArrayList<>();

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

	private void addCategoriesCriteria(BoolQueryBuilder theBoolQueryBuilder, SearchParameterMap theSearchParameterMap) {
		if (theSearchParameterMap.containsKey(IndexConstants.CATEGORY_SEARCH_PARAM)) {
			ArrayList<String> codeSystemHashList = new ArrayList<>();
			ArrayList<String> codeOnlyList = new ArrayList<>();
			ArrayList<String> systemOnlyList = new ArrayList<>();
			ArrayList<String> textOnlyList = new ArrayList<>();
			List<List<IQueryParameterType>> andOrParams = theSearchParameterMap.get(IndexConstants.CATEGORY_SEARCH_PARAM);
			for (List<? extends IQueryParameterType> nextAnd : andOrParams) {
				codeSystemHashList.addAll(getCodingCodeSystemValues(nextAnd));
				codeOnlyList.addAll(getCodingCodeOnlyValues(nextAnd));
				systemOnlyList.addAll(getCodingSystemOnlyValues(nextAnd));
				textOnlyList.addAll(getCodingTextOnlyValues(nextAnd));
			}
			if (codeSystemHashList.size() > 0) {
				theBoolQueryBuilder.must(QueryBuilders.termsQuery("categoryconceptcodingcode_system_hash", codeSystemHashList));
			}
			if (codeOnlyList.size() > 0) {
				theBoolQueryBuilder.must(QueryBuilders.termsQuery("categoryconceptcodingcode", codeOnlyList));
			}
			if (systemOnlyList.size() > 0) {
				theBoolQueryBuilder.must(QueryBuilders.termsQuery("categoryconceptcodingsystem", systemOnlyList));
			}
			if (textOnlyList.size() > 0) {
				BoolQueryBuilder myTextBoolQueryBuilder = QueryBuilders.boolQuery();
				for (String textOnlyParam : textOnlyList) {
					myTextBoolQueryBuilder.should(QueryBuilders.matchPhraseQuery("categoryconceptcodingdisplay", textOnlyParam));
					myTextBoolQueryBuilder.should(QueryBuilders.matchPhraseQuery("categoryconcepttext", textOnlyParam));
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

	private void addObservationCodeCriteria(BoolQueryBuilder theBoolQueryBuilder, SearchParameterMap theSearchParameterMap) {
		if (theSearchParameterMap.containsKey(IndexConstants.CODE_SEARCH_PARAM)) {
			ArrayList<String> codeSystemHashList = new ArrayList<>();
			ArrayList<String> codeOnlyList = new ArrayList<>();
			ArrayList<String> systemOnlyList = new ArrayList<>();
			ArrayList<String> textOnlyList = new ArrayList<>();
			List<List<IQueryParameterType>> andOrParams = theSearchParameterMap.get(IndexConstants.CODE_SEARCH_PARAM);
			for (List<? extends IQueryParameterType> nextAnd : andOrParams) {
				codeSystemHashList.addAll(getCodingCodeSystemValues(nextAnd));
				codeOnlyList.addAll(getCodingCodeOnlyValues(nextAnd));
				systemOnlyList.addAll(getCodingSystemOnlyValues(nextAnd));
				textOnlyList.addAll(getCodingTextOnlyValues(nextAnd));
			}
			if (codeSystemHashList.size() > 0) {
				theBoolQueryBuilder.must(QueryBuilders.termsQuery("codeconceptcodingcode_system_hash", codeSystemHashList));
			}
			if (codeOnlyList.size() > 0) {
				theBoolQueryBuilder.must(QueryBuilders.termsQuery("codeconceptcodingcode", codeOnlyList));
			}
			if (systemOnlyList.size() > 0) {
				theBoolQueryBuilder.must(QueryBuilders.termsQuery("codeconceptcodingsystem", systemOnlyList));
			}
			if (textOnlyList.size() > 0) {
				BoolQueryBuilder myTextBoolQueryBuilder = QueryBuilders.boolQuery();
				for (String textOnlyParam : textOnlyList) {
					myTextBoolQueryBuilder.should(QueryBuilders.matchPhraseQuery("codeconceptcodingdisplay", textOnlyParam));
					myTextBoolQueryBuilder.should(QueryBuilders.matchPhraseQuery("codeconcepttext", textOnlyParam));
				}
				theBoolQueryBuilder.must(myTextBoolQueryBuilder);
			}
		}

	}

	@VisibleForTesting
	void deleteAllDocuments(String theIndexName) throws IOException {
		DeleteByQueryRequest deleteByQueryRequest = new DeleteByQueryRequest(theIndexName);
		deleteByQueryRequest.setQuery(QueryBuilders.matchAllQuery());
		myRestHighLevelClient.deleteByQuery(deleteByQueryRequest, RequestOptions.DEFAULT);
	}

/*	public void deleteObservationIndex(String theObservationIdentifier) throws IOException {
		DeleteByQueryRequest deleteByQueryRequest = new DeleteByQueryRequest(IndexConstants.OBSERVATION_DOCUMENT_TYPE);
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		boolQueryBuilder.must(QueryBuilders.termsQuery(OBSERVATION_IDENTIFIER_FIELD_NAME, theObservationIdentifier));
		deleteByQueryRequest.setQuery(boolQueryBuilder);
		myRestHighLevelClient.deleteByQuery(deleteByQueryRequest, RequestOptions.DEFAULT);
	}
 */
}
