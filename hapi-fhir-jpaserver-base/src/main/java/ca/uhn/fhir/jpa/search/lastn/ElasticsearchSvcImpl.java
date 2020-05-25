package ca.uhn.fhir.jpa.search.lastn;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.util.LastNParameterHelper;
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
import java.util.TreeSet;
import java.util.function.Function;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class ElasticsearchSvcImpl implements IElasticsearchSvc {

	public static final String OBSERVATION_INDEX = "observation_index";
	public static final String CODE_INDEX = "code_index";
	public static final String OBSERVATION_DOCUMENT_TYPE = "ca.uhn.fhir.jpa.model.entity.ObservationIndexedSearchParamLastNEntity";
	public static final String CODE_DOCUMENT_TYPE = "ca.uhn.fhir.jpa.model.entity.ObservationIndexedCodeCodeableConceptEntity";

	private final RestHighLevelClient myRestHighLevelClient;

	private final ObjectMapper objectMapper = new ObjectMapper();

	private final String GROUP_BY_SUBJECT = "group_by_subject";
	private final String GROUP_BY_SYSTEM = "group_by_system";
	private final String GROUP_BY_CODE = "group_by_code";


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
		if (indexExists(OBSERVATION_INDEX)) {
			return;
		}
		String observationMapping = "{\n" +
			"  \"mappings\" : {\n" +
			"    \"ca.uhn.fhir.jpa.model.entity.ObservationIndexedSearchParamLastNEntity\" : {\n" +
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
		if (!createIndex(OBSERVATION_INDEX, observationMapping)) {
			throw new RuntimeException("Failed to create observation index");
		}
	}

	private void createCodeIndexIfMissing() throws IOException {
		if (indexExists(CODE_INDEX)) {
			return;
		}
		String codeMapping = "{\n" +
			"  \"mappings\" : {\n" +
			"    \"ca.uhn.fhir.jpa.model.entity.ObservationIndexedCodeCodeableConceptEntity\" : {\n" +
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
		if (!createIndex(CODE_INDEX, codeMapping)) {
			throw new RuntimeException("Failed to create code index");
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
		String OBSERVATION_IDENTIFIER_FIELD_NAME = "identifier";
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
					createObservationSubjectAggregationBuilder(theSearchParameterMap.getLastNMax(), topHitsInclude));
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
				createObservationCodeAggregationBuilder(theSearchParameterMap.getLastNMax(), topHitsInclude));
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

	private TreeSet<String> getReferenceValues(List<? extends IQueryParameterType> referenceParams) {
		TreeSet<String> referenceList = new TreeSet<>();

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

	private CompositeAggregationBuilder createObservationSubjectAggregationBuilder(int theMaxNumberObservationsPerCode, String[] theTopHitsInclude) {
		CompositeValuesSourceBuilder<?> subjectValuesBuilder = new TermsValuesSourceBuilder("subject").field("subject");
		List<CompositeValuesSourceBuilder<?>> compositeAggSubjectSources = new ArrayList();
		compositeAggSubjectSources.add(subjectValuesBuilder);
		CompositeAggregationBuilder compositeAggregationSubjectBuilder = new CompositeAggregationBuilder(GROUP_BY_SUBJECT, compositeAggSubjectSources);
		compositeAggregationSubjectBuilder.subAggregation(createObservationCodeAggregationBuilder(theMaxNumberObservationsPerCode, theTopHitsInclude));
		compositeAggregationSubjectBuilder.size(10000);

		return compositeAggregationSubjectBuilder;
	}

	private TermsAggregationBuilder createObservationCodeAggregationBuilder(int theMaxNumberObservationsPerCode, String[] theTopHitsInclude) {
		TermsAggregationBuilder observationCodeCodeAggregationBuilder = new TermsAggregationBuilder(GROUP_BY_CODE, ValueType.STRING).field("codeconceptcodingcode");
		// Top Hits Aggregation
		observationCodeCodeAggregationBuilder.subAggregation(AggregationBuilders.topHits("most_recent_effective")
			.sort("effectivedtm", SortOrder.DESC)
			.fetchSource(theTopHitsInclude, null).size(theMaxNumberObservationsPerCode));
		observationCodeCodeAggregationBuilder.size(10000);
		TermsAggregationBuilder observationCodeSystemAggregationBuilder = new TermsAggregationBuilder(GROUP_BY_SYSTEM, ValueType.STRING).field("codeconceptcodingsystem");
		observationCodeSystemAggregationBuilder.subAggregation(observationCodeCodeAggregationBuilder);
		return observationCodeSystemAggregationBuilder;
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
		ParsedTerms aggregatedObservationCodeSystems = theObservationCodeSystemAggregations.get(GROUP_BY_SYSTEM);
		for(Terms.Bucket observationCodeSystem : aggregatedObservationCodeSystems.getBuckets()) {
			Aggregations observationCodeCodeAggregations = observationCodeSystem.getAggregations();
			ParsedTerms aggregatedObservationCodeCodes = observationCodeCodeAggregations.get(GROUP_BY_CODE);
			retVal.addAll(aggregatedObservationCodeCodes.getBuckets());
		}
		return retVal;
	}

	private SearchHit[] getLastNMatches(Terms.Bucket theObservationCodeBucket) {
		Aggregations topHitObservationCodes = theObservationCodeBucket.getAggregations();
		ParsedTopHits parsedTopHits = topHitObservationCodes.get("most_recent_effective");
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
		boolQueryBuilder.must(QueryBuilders.termQuery("subject", theSubjectParam));
		addCategoriesCriteria(boolQueryBuilder, theSearchParameterMap, theFhirContext);
		addObservationCodeCriteria(boolQueryBuilder, theSearchParameterMap, theFhirContext);
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
	List<ObservationJson> executeLastNWithAllFields(SearchParameterMap theSearchParameterMap, FhirContext theFhirContext) {
		return buildAndExecuteSearch(theSearchParameterMap, theFhirContext, null, t -> t, 100);
	}

	@VisibleForTesting
	List<CodeJson> queryAllIndexedObservationCodes() throws IOException {
		SearchRequest codeSearchRequest = new SearchRequest(CODE_INDEX);
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

	@VisibleForTesting
	void deleteAllDocuments(String theIndexName) throws IOException {
		DeleteByQueryRequest deleteByQueryRequest = new DeleteByQueryRequest(theIndexName);
		deleteByQueryRequest.setQuery(QueryBuilders.matchAllQuery());
		myRestHighLevelClient.deleteByQuery(deleteByQueryRequest, RequestOptions.DEFAULT);
	}

}
