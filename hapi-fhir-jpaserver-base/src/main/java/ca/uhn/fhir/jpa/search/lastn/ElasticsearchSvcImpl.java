/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.search.lastn;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.dao.TolerantJsonParser;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.util.CodeSystemHash;
import ca.uhn.fhir.jpa.search.lastn.json.CodeJson;
import ca.uhn.fhir.jpa.search.lastn.json.ObservationJson;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.util.LastNParameterHelper;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.Result;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.indices.ExistsRequest;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.util.NamedValue;
import co.elastic.clients.util.ObjectBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.Nullable;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class ElasticsearchSvcImpl implements IElasticsearchSvc {

	private static final Logger ourLog = LoggerFactory.getLogger(ElasticsearchSvcImpl.class);

	// Index Constants
	public static final String OBSERVATION_INDEX = "observation_index";
	public static final String OBSERVATION_CODE_INDEX = "code_index";
	public static final String OBSERVATION_DOCUMENT_TYPE =
			"ca.uhn.fhir.jpa.model.entity.ObservationIndexedSearchParamLastNEntity";
	public static final String CODE_DOCUMENT_TYPE =
			"ca.uhn.fhir.jpa.model.entity.ObservationIndexedCodeCodeableConceptEntity";
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

	private final ElasticsearchClient myRestHighLevelClient;

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Autowired
	private PartitionSettings myPartitionSettings;

	@Autowired
	private FhirContext myContext;

	// This constructor used to inject a dummy partitionsettings in test.
	public ElasticsearchSvcImpl(
			PartitionSettings thePartitionSetings,
			String theProtocol,
			String theHostname,
			@Nullable String theUsername,
			@Nullable String thePassword) {
		this(theProtocol, theHostname, theUsername, thePassword);
		this.myPartitionSettings = thePartitionSetings;
	}

	public ElasticsearchSvcImpl(
			String theProtocol, String theHostname, @Nullable String theUsername, @Nullable String thePassword) {

		myRestHighLevelClient = ElasticsearchRestClientFactory.createElasticsearchHighLevelRestClient(
				theProtocol, theHostname, theUsername, thePassword);

		try {
			createObservationIndexIfMissing();
			createObservationCodeIndexIfMissing();
		} catch (IOException theE) {
			throw new RuntimeException(Msg.code(1175) + "Failed to create document index", theE);
		}
	}

	private String getIndexSchema(String theSchemaFileName) throws IOException {
		InputStreamReader input =
				new InputStreamReader(ElasticsearchSvcImpl.class.getResourceAsStream(theSchemaFileName));
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
		return myRestHighLevelClient
				.indices()
				.create(cir -> cir.index(theIndexName).withJson(new StringReader(theMapping)))
				.acknowledged();
	}

	private boolean indexExists(String theIndexName) throws IOException {
		ExistsRequest request = new ExistsRequest.Builder().index(theIndexName).build();
		return myRestHighLevelClient.indices().exists(request).value();
	}

	@Override
	public List<String> executeLastN(
			SearchParameterMap theSearchParameterMap, FhirContext theFhirContext, Integer theMaxResultsToFetch) {
		Validate.isTrue(
				!myPartitionSettings.isPartitioningEnabled(),
				"$lastn is not currently supported on partitioned servers");

		String[] topHitsInclude = {OBSERVATION_IDENTIFIER_FIELD_NAME};
		return buildAndExecuteSearch(
				theSearchParameterMap,
				theFhirContext,
				topHitsInclude,
				ObservationJson::getIdentifier,
				theMaxResultsToFetch);
	}

	private <T> List<T> buildAndExecuteSearch(
			SearchParameterMap theSearchParameterMap,
			FhirContext theFhirContext,
			String[] topHitsInclude,
			Function<ObservationJson, T> setValue,
			Integer theMaxResultsToFetch) {
		String patientParamName = LastNParameterHelper.getPatientParamName(theFhirContext);
		String subjectParamName = LastNParameterHelper.getSubjectParamName(theFhirContext);
		List<T> searchResults = new ArrayList<>();
		if (theSearchParameterMap.containsKey(patientParamName)
				|| theSearchParameterMap.containsKey(subjectParamName)) {
			for (String subject :
					getSubjectReferenceCriteria(patientParamName, subjectParamName, theSearchParameterMap)) {
				if (theMaxResultsToFetch != null && searchResults.size() >= theMaxResultsToFetch) {
					break;
				}
				SearchRequest myLastNRequest = buildObservationsSearchRequest(
						subject,
						theSearchParameterMap,
						theFhirContext,
						OBSERVATION_SUBJECT_FIELD_NAME,
						createObservationSubjectAggregationBuilder(
								getMaxParameter(theSearchParameterMap), topHitsInclude));
				ourLog.debug("ElasticSearch query: {}", myLastNRequest.source().toString());
				try {
					SearchResponse<ObservationJson> lastnResponse = executeSearchRequest(myLastNRequest);
					searchResults.addAll(buildObservationList(
							lastnResponse, setValue, theSearchParameterMap, theFhirContext, theMaxResultsToFetch));
				} catch (IOException theE) {
					throw new InvalidRequestException(Msg.code(1178) + "Unable to execute LastN request", theE);
				}
			}
		} else {
			SearchRequest myLastNRequest = buildObservationsSearchRequest(
					null,
					theSearchParameterMap,
					theFhirContext,
					GROUP_BY_CODE,
					createObservationCodeAggregationBuilder(getMaxParameter(theSearchParameterMap), topHitsInclude));
			ourLog.debug("ElasticSearch query: {}", myLastNRequest.source().toString());
			try {
				SearchResponse<ObservationJson> lastnResponse = executeSearchRequest(myLastNRequest);
				searchResults.addAll(buildObservationList(
						lastnResponse, setValue, theSearchParameterMap, theFhirContext, theMaxResultsToFetch));
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

	private List<String> getSubjectReferenceCriteria(
			String thePatientParamName, String theSubjectParamName, SearchParameterMap theSearchParameterMap) {
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
				throw new IllegalArgumentException(
						Msg.code(1180) + "Invalid token type (expecting ReferenceParam): " + nextOr.getClass());
			}
		}
		return referenceList;
	}

	private Function<Aggregation.Builder, ObjectBuilder<Aggregation>> createObservationSubjectAggregationBuilder(
			Integer theMaxNumberObservationsPerCode, String[] theTopHitsInclude) {

		Function<Aggregation.Builder, ObjectBuilder<Aggregation>> retVal = ab -> {
			return ab.terms(t -> t.field(OBSERVATION_SUBJECT_FIELD_NAME))
					.aggregations(
							GROUP_BY_CODE,
							createObservationCodeAggregationBuilder(
									theMaxNumberObservationsPerCode, theTopHitsInclude));
		};
		return retVal;

		//		CompositeValuesSourceBuilder<?> subjectValuesBuilder =
		//				new TermsValuesSourceBuilder(OBSERVATION_SUBJECT_FIELD_NAME).field(OBSERVATION_SUBJECT_FIELD_NAME);
		//		List<CompositeValuesSourceBuilder<?>> compositeAggSubjectSources = new ArrayList<>();
		//		compositeAggSubjectSources.add(subjectValuesBuilder);
		//		CompositeAggregationBuilder compositeAggregationSubjectBuilder =
		//				new CompositeAggregationBuilder(GROUP_BY_SUBJECT, compositeAggSubjectSources);
		//		compositeAggregationSubjectBuilder.subAggregation(
		//				createObservationCodeAggregationBuilder(theMaxNumberObservationsPerCode, theTopHitsInclude));
		//		compositeAggregationSubjectBuilder.size(10000);
		//
		//		return compositeAggregationSubjectBuilder;
	}

	private Function<Aggregation.Builder, ObjectBuilder<Aggregation>> createObservationCodeAggregationBuilder(
			int theMaxNumberObservationsPerCode, String[] theTopHitsInclude) {
		Function<Aggregation.Builder, ObjectBuilder<Aggregation>> retVal = ab -> {
			NamedValue<co.elastic.clients.elasticsearch._types.SortOrder> order = null;
			return ab.terms(t -> t.field(OBSERVATION_CODEVALUE_FIELD_NAME).order(order));
		};

		//		TermsAggregationBuilder observationCodeCodeAggregationBuilder =
		//				new TermsAggregationBuilder(GROUP_BY_CODE).field(OBSERVATION_CODEVALUE_FIELD_NAME);
		//		observationCodeCodeAggregationBuilder.order(BucketOrder.key(true));
		//		// Top Hits Aggregation
		//		observationCodeCodeAggregationBuilder.subAggregation(AggregationBuilders.topHits(MOST_RECENT_EFFECTIVE)
		//				.sort(OBSERVATION_EFFECTIVEDTM_FIELD_NAME, SortOrder.DESC)
		//				.fetchSource(theTopHitsInclude, null)
		//				.size(theMaxNumberObservationsPerCode));
		//		observationCodeCodeAggregationBuilder.size(10000);
		//		TermsAggregationBuilder observationCodeSystemAggregationBuilder =
		//				new TermsAggregationBuilder(GROUP_BY_SYSTEM).field(OBSERVATION_CODESYSTEM_FIELD_NAME);
		//		observationCodeSystemAggregationBuilder.order(BucketOrder.key(true));
		//		observationCodeSystemAggregationBuilder.subAggregation(observationCodeCodeAggregationBuilder);
		//		//		return observationCodeSystemAggregationBuilder;

		return retVal;
	}

	private SearchResponse executeSearchRequest(SearchRequest searchRequest) throws IOException {
		return myRestHighLevelClient.search(searchRequest, Object.class);
	}

	private <T> List<T> buildObservationList(
			SearchResponse<ObservationJson> theSearchResponse,
			Function<ObservationJson, T> setValue,
			SearchParameterMap theSearchParameterMap,
			FhirContext theFhirContext,
			Integer theMaxResultsToFetch)
			throws IOException {
		List<T> theObservationList = new ArrayList<>();
		if (theSearchParameterMap.containsKey(LastNParameterHelper.getPatientParamName(theFhirContext))
				|| theSearchParameterMap.containsKey(LastNParameterHelper.getSubjectParamName(theFhirContext))) {
			//			for (ParsedComposite.ParsedBucket subjectBucket : getSubjectBuckets(theSearchResponse)) {
			//				if (theMaxResultsToFetch != null && theObservationList.size() >= theMaxResultsToFetch) {
			//					break;
			//				}
			//				for (Terms.Bucket observationCodeBucket : getObservationCodeBuckets(subjectBucket)) {
			//					if (theMaxResultsToFetch != null && theObservationList.size() >= theMaxResultsToFetch) {
			//						break;
			//					}
			//					for (SearchHit lastNMatch : getLastNMatches(observationCodeBucket)) {
			//						if (theMaxResultsToFetch != null && theObservationList.size() >= theMaxResultsToFetch) {
			//							break;
			//						}
			//						String indexedObservation = lastNMatch.getSourceAsString();
			//						ObservationJson observationJson =
			//								objectMapper.readValue(indexedObservation, ObservationJson.class);
			//						theObservationList.add(setValue.apply(observationJson));
			//					}
			//				}
			//			}
		} else {
			//			for (Terms.Bucket observationCodeBucket : getObservationCodeBuckets(theSearchResponse)) {
			//				if (theMaxResultsToFetch != null && theObservationList.size() >= theMaxResultsToFetch) {
			//					break;
			//				}
			//				for (SearchHit lastNMatch : getLastNMatches(observationCodeBucket)) {
			//					if (theMaxResultsToFetch != null && theObservationList.size() >= theMaxResultsToFetch) {
			//						break;
			//					}
			//					String indexedObservation = lastNMatch.getSourceAsString();
			//					ObservationJson observationJson = objectMapper.readValue(indexedObservation, ObservationJson.class);
			//					theObservationList.add(setValue.apply(observationJson));
			//				}
			//			}
		}

		return theObservationList;
	}

	//	private List<ParsedComposite.ParsedBucket> getSubjectBuckets(SearchResponse<ObservationJson> theSearchResponse) {
	//		Map<String, Aggregate> responseAggregations = theSearchResponse.aggregations();
	//		Aggregate aggregatedSubjects = responseAggregations.get(GROUP_BY_SUBJECT);
	//		throw new InternalErrorException("foo");
	//	}
	//
	//	private List<? extends Terms.Bucket> getObservationCodeBuckets(SearchResponse theSearchResponse) {
	//		throw new InternalErrorException("blah");
	//		//		Aggregations responseAggregations = theSearchResponse.getAggregations();
	//		//		return getObservationCodeBuckets(responseAggregations);
	//	}
	//
	//	private List<? extends Terms.Bucket> getObservationCodeBuckets(ParsedComposite.ParsedBucket theSubjectBucket) {
	//		Aggregations observationCodeSystemAggregations = theSubjectBucket.getAggregations();
	//		return getObservationCodeBuckets(observationCodeSystemAggregations);
	//	}
	//
	//	private List<? extends Terms.Bucket> getObservationCodeBuckets(Aggregations theObservationCodeSystemAggregations)
	// {
	//		List<Terms.Bucket> retVal = new ArrayList<>();
	//		ParsedTerms aggregatedObservationCodeSystems = theObservationCodeSystemAggregations.get(GROUP_BY_SYSTEM);
	//		for (Terms.Bucket observationCodeSystem : aggregatedObservationCodeSystems.getBuckets()) {
	//			Aggregations observationCodeCodeAggregations = observationCodeSystem.getAggregations();
	//			ParsedTerms aggregatedObservationCodeCodes = observationCodeCodeAggregations.get(GROUP_BY_CODE);
	//			retVal.addAll(aggregatedObservationCodeCodes.getBuckets());
	//		}
	//		return retVal;
	//	}
	//
	//	private SearchHit[] getLastNMatches(Terms.Bucket theObservationCodeBucket) {
	//		Aggregations topHitObservationCodes = theObservationCodeBucket.getAggregations();
	//		ParsedTopHits parsedTopHits = topHitObservationCodes.get(MOST_RECENT_EFFECTIVE);
	//		return parsedTopHits.getHits().getHits();
	//	}

	private SearchRequest buildObservationsSearchRequest(
			@Nullable String theSubjectParam,
			SearchParameterMap theSearchParameterMap,
			FhirContext theFhirContext,
			String theAggregationKey,
			Function<Aggregation.Builder, ObjectBuilder<Aggregation>> theAggregationBuilder) {

		Function<BoolQuery.Builder, ObjectBuilder<BoolQuery>> bqb = boolQueryBuilder -> {
			Function<Query.Builder, ObjectBuilder<Query>> qbbm;
			if (theSubjectParam != null) {
				qbbm = i -> {
					i.term(TermQuery.of(
							tq -> tq.field(OBSERVATION_SUBJECT_FIELD_NAME).field(theSubjectParam)));
					return i;
				};
			} else {
				qbbm = i -> {
					i.matchAll(t -> t);
					return i;
				};
			}

			boolQueryBuilder.must(qbbm);
			addCategoriesCriteria(boolQueryBuilder, theSearchParameterMap, theFhirContext);
			addObservationCodeCriteria(boolQueryBuilder, theSearchParameterMap, theFhirContext);
			addDateCriteria(boolQueryBuilder, theSearchParameterMap, theFhirContext);
			return boolQueryBuilder;
		};
		return SearchRequest.of(sr -> sr.index(OBSERVATION_INDEX)
				.query(qb -> qb.bool(bqb))
				.size(0)
				.aggregations(theAggregationKey, theAggregationBuilder));
	}

	private Boolean searchParamsHaveLastNCriteria(
			SearchParameterMap theSearchParameterMap, FhirContext theFhirContext) {
		return theSearchParameterMap != null
				&& (theSearchParameterMap.containsKey(LastNParameterHelper.getPatientParamName(theFhirContext))
						|| theSearchParameterMap.containsKey(LastNParameterHelper.getSubjectParamName(theFhirContext))
						|| theSearchParameterMap.containsKey(LastNParameterHelper.getCategoryParamName(theFhirContext))
						|| theSearchParameterMap.containsKey(LastNParameterHelper.getCodeParamName(theFhirContext)));
	}

	private void addCategoriesCriteria(
			BoolQuery.Builder theBoolQueryBuilder,
			SearchParameterMap theSearchParameterMap,
			FhirContext theFhirContext) {
		String categoryParamName = LastNParameterHelper.getCategoryParamName(theFhirContext);
		if (theSearchParameterMap.containsKey(categoryParamName)) {
			ArrayList<FieldValue> codeSystemHashList = new ArrayList<>();
			ArrayList<FieldValue> codeOnlyList = new ArrayList<>();
			ArrayList<FieldValue> systemOnlyList = new ArrayList<>();
			ArrayList<FieldValue> textOnlyList = new ArrayList<>();
			List<List<IQueryParameterType>> andOrParams = theSearchParameterMap.get(categoryParamName);
			for (List<? extends IQueryParameterType> nextAnd : andOrParams) {
				codeSystemHashList.addAll(getCodingCodeSystemValues(nextAnd));
				codeOnlyList.addAll(getCodingCodeOnlyValues(nextAnd));
				systemOnlyList.addAll(getCodingSystemOnlyValues(nextAnd));
				textOnlyList.addAll(getCodingTextOnlyValues(nextAnd));
			}
			if (!codeSystemHashList.isEmpty()) {
				theBoolQueryBuilder.must(mf -> mf.terms(tb ->
						tb.field(OBSERVATION_CATEGORYHASH_FIELD_NAME).terms(tbf -> tbf.value(codeSystemHashList))));
			}
			if (!codeOnlyList.isEmpty()) {
				theBoolQueryBuilder.must(mf -> mf.terms(
						tb -> tb.field(OBSERVATION_CATEGORYVALUE_FIELD_NAME).terms(tbf -> tbf.value(codeOnlyList))));
			}
			if (!systemOnlyList.isEmpty()) {
				theBoolQueryBuilder.must(mf -> mf.terms(
						tb -> tb.field(OBSERVATION_CATEGORYSYSTEM_FIELD_NAME).terms(tbf -> tbf.value(systemOnlyList))));
			}
			if (!textOnlyList.isEmpty()) {
				theBoolQueryBuilder.must(mf -> {
					for (FieldValue textOnlyParam : textOnlyList) {
						mf.bool(bq -> bq.should(sq ->
										sq.matchPhrasePrefix(mfp -> mfp.field(OBSERVATION_CATEGORYDISPLAY_FIELD_NAME)
												.query(textOnlyParam.stringValue())))
								.should(sq -> sq.matchPhrasePrefix(mfp -> mfp.field(OBSERVATION_CATEGORYTEXT_FIELD_NAME)
										.query(textOnlyParam.stringValue()))));
					}
					return mf;
				});
			}
		}
	}

	private List<FieldValue> getCodingCodeSystemValues(List<? extends IQueryParameterType> codeParams) {
		ArrayList<FieldValue> codeSystemHashList = new ArrayList<>();
		for (IQueryParameterType nextOr : codeParams) {
			if (nextOr instanceof TokenParam) {
				TokenParam ref = (TokenParam) nextOr;
				if (ref.getSystem() != null && ref.getValue() != null) {
					codeSystemHashList.add(FieldValue.of(
							String.valueOf(CodeSystemHash.hashCodeSystem(ref.getSystem(), ref.getValue()))));
				}
			} else {
				throw new IllegalArgumentException(
						Msg.code(1181) + "Invalid token type (expecting TokenParam): " + nextOr.getClass());
			}
		}
		return codeSystemHashList;
	}

	private List<FieldValue> getCodingCodeOnlyValues(List<? extends IQueryParameterType> codeParams) {
		ArrayList<FieldValue> codeOnlyList = new ArrayList<>();
		for (IQueryParameterType nextOr : codeParams) {

			if (nextOr instanceof TokenParam) {
				TokenParam ref = (TokenParam) nextOr;
				if (ref.getValue() != null && ref.getSystem() == null && !ref.isText()) {
					codeOnlyList.add(FieldValue.of(ref.getValue()));
				}
			} else {
				throw new IllegalArgumentException(
						Msg.code(1182) + "Invalid token type (expecting TokenParam): " + nextOr.getClass());
			}
		}
		return codeOnlyList;
	}

	private List<FieldValue> getCodingSystemOnlyValues(List<? extends IQueryParameterType> codeParams) {
		ArrayList<FieldValue> systemOnlyList = new ArrayList<>();
		for (IQueryParameterType nextOr : codeParams) {

			if (nextOr instanceof TokenParam) {
				TokenParam ref = (TokenParam) nextOr;
				if (ref.getValue() == null && ref.getSystem() != null) {
					systemOnlyList.add(FieldValue.of(ref.getSystem()));
				}
			} else {
				throw new IllegalArgumentException(
						Msg.code(1183) + "Invalid token type (expecting TokenParam): " + nextOr.getClass());
			}
		}
		return systemOnlyList;
	}

	private List<FieldValue> getCodingTextOnlyValues(List<? extends IQueryParameterType> codeParams) {
		ArrayList<FieldValue> textOnlyList = new ArrayList<>();
		for (IQueryParameterType nextOr : codeParams) {

			if (nextOr instanceof TokenParam) {
				TokenParam ref = (TokenParam) nextOr;
				if (ref.isText() && ref.getValue() != null) {
					textOnlyList.add(FieldValue.of(ref.getValue()));
				}
			} else {
				throw new IllegalArgumentException(
						Msg.code(1184) + "Invalid token type (expecting TokenParam): " + nextOr.getClass());
			}
		}
		return textOnlyList;
	}

	private void addObservationCodeCriteria(
			BoolQuery.Builder theBoolQueryBuilder,
			SearchParameterMap theSearchParameterMap,
			FhirContext theFhirContext) {
		String codeParamName = LastNParameterHelper.getCodeParamName(theFhirContext);
		if (theSearchParameterMap.containsKey(codeParamName)) {
			ArrayList<FieldValue> codeSystemHashList = new ArrayList<>();
			ArrayList<FieldValue> codeOnlyList = new ArrayList<>();
			ArrayList<FieldValue> systemOnlyList = new ArrayList<>();
			ArrayList<FieldValue> textOnlyList = new ArrayList<>();
			List<List<IQueryParameterType>> andOrParams = theSearchParameterMap.get(codeParamName);
			for (List<? extends IQueryParameterType> nextAnd : andOrParams) {
				codeSystemHashList.addAll(getCodingCodeSystemValues(nextAnd));
				codeOnlyList.addAll(getCodingCodeOnlyValues(nextAnd));
				systemOnlyList.addAll(getCodingSystemOnlyValues(nextAnd));
				textOnlyList.addAll(getCodingTextOnlyValues(nextAnd));
			}
			if (!codeSystemHashList.isEmpty()) {
				theBoolQueryBuilder.must(mf -> mf.terms(
						tb -> tb.field(OBSERVATION_CODEHASH_FIELD_NAME).terms(tbf -> tbf.value(codeSystemHashList))));
			}
			if (!codeOnlyList.isEmpty()) {
				theBoolQueryBuilder.must(mf -> mf.terms(
						tb -> tb.field(OBSERVATION_CODEVALUE_FIELD_NAME).terms(tbf -> tbf.value(codeOnlyList))));
			}
			if (!systemOnlyList.isEmpty()) {
				theBoolQueryBuilder.must(mf -> mf.terms(
						tb -> tb.field(OBSERVATION_CODESYSTEM_FIELD_NAME).terms(tbf -> tbf.value(systemOnlyList))));
			}
			if (!textOnlyList.isEmpty()) {
				theBoolQueryBuilder.must(mf -> {
					for (FieldValue textOnlyParam : textOnlyList) {
						mf.bool(bq -> bq.should(
										sq -> sq.matchPhrasePrefix(mfp -> mfp.field(OBSERVATION_CODEDISPLAY_FIELD_NAME)
												.query(textOnlyParam.stringValue())))
								.should(sq -> sq.matchPhrasePrefix(mfp -> mfp.field(OBSERVATION_CODE_TEXT_FIELD_NAME)
										.query(textOnlyParam.stringValue()))));
					}
					return mf;
				});
			}
		}
	}

	private void addDateCriteria(
			BoolQuery.Builder theBoolQueryBuilder,
			SearchParameterMap theSearchParameterMap,
			FhirContext theFhirContext) {
		String dateParamName = LastNParameterHelper.getEffectiveParamName(theFhirContext);
		if (theSearchParameterMap.containsKey(dateParamName)) {
			List<List<IQueryParameterType>> andOrParams = theSearchParameterMap.get(dateParamName);
			for (List<? extends IQueryParameterType> nextAnd : andOrParams) {
				theBoolQueryBuilder.must(mb -> mb.bool(bqb -> {
					for (IQueryParameterType nextOr : nextAnd) {
						if (nextOr instanceof DateParam) {
							DateParam myDate = (DateParam) nextOr;
							createDateCriteria(myDate, bqb);
						}
					}
					return bqb;
				}));
			}
		}
	}

	private void createDateCriteria(DateParam theDate, BoolQuery.Builder theBoolQueryBuilder) {
		Long dateInstant = theDate.getValue().getTime();

		ParamPrefixEnum prefix = theDate.getPrefix();
		if (prefix == ParamPrefixEnum.GREATERTHAN || prefix == ParamPrefixEnum.STARTS_AFTER) {
			theBoolQueryBuilder.should(s ->
					s.range(r -> r.field(OBSERVATION_EFFECTIVEDTM_FIELD_NAME).gt(JsonData.of(dateInstant))));
		} else if (prefix == ParamPrefixEnum.LESSTHAN || prefix == ParamPrefixEnum.ENDS_BEFORE) {
			theBoolQueryBuilder.should(s ->
					s.range(r -> r.field(OBSERVATION_EFFECTIVEDTM_FIELD_NAME).lt(JsonData.of(dateInstant))));
		} else if (prefix == ParamPrefixEnum.LESSTHAN_OR_EQUALS) {
			theBoolQueryBuilder.should(s ->
					s.range(r -> r.field(OBSERVATION_EFFECTIVEDTM_FIELD_NAME).lte(JsonData.of(dateInstant))));
		} else if (prefix == ParamPrefixEnum.GREATERTHAN_OR_EQUALS) {
			theBoolQueryBuilder.should(s ->
					s.range(r -> r.field(OBSERVATION_EFFECTIVEDTM_FIELD_NAME).gte(JsonData.of(dateInstant))));
		} else {
			theBoolQueryBuilder.should(s ->
					s.match(m -> m.field(OBSERVATION_EFFECTIVEDTM_FIELD_NAME).query(dateInstant)));
		}
	}

	@VisibleForTesting
	public List<ObservationJson> executeLastNWithAllFieldsForTest(
			SearchParameterMap theSearchParameterMap, FhirContext theFhirContext) {
		return buildAndExecuteSearch(theSearchParameterMap, theFhirContext, null, t -> t, 100);
	}

	@VisibleForTesting
	List<CodeJson> queryAllIndexedObservationCodesForTest() throws IOException {
		return myRestHighLevelClient
				.search(
						sb -> sb.index(OBSERVATION_CODE_INDEX)
								.query(qb -> qb.matchAll(mab -> mab))
								.size(1000),
						CodeJson.class)
				.hits()
				.hits()
				.stream()
				.map(t -> t.source())
				.collect(Collectors.toList());
	}

	@Override
	public ObservationJson getObservationDocument(String theDocumentID) {
		if (theDocumentID == null) {
			throw new InvalidRequestException(
					Msg.code(1185) + "Require non-null document ID for observation document query");
		}
		SearchRequest theSearchRequest = buildSingleObservationSearchRequest(theDocumentID);
		ObservationJson observationDocumentJson = null;
		try {
			SearchResponse<ObservationJson> observationDocumentResponse =
					myRestHighLevelClient.search(theSearchRequest, ObservationJson.class);
			List<Hit<ObservationJson>> observationDocumentHits =
					observationDocumentResponse.hits().hits();
			if (!observationDocumentHits.isEmpty()) {
				// There should be no more than one hit for the identifier
				observationDocumentJson = observationDocumentHits.get(0).source();
			}

		} catch (IOException theE) {
			throw new InvalidRequestException(
					Msg.code(1186) + "Unable to execute observation document query for ID " + theDocumentID, theE);
		}

		return observationDocumentJson;
	}

	private SearchRequest buildSingleObservationSearchRequest(String theObservationIdentifier) {
		return SearchRequest.of(srb -> srb.query(qb -> qb.bool(bb -> bb.must(mb -> mb.term(
						tb -> tb.field(OBSERVATION_IDENTIFIER_FIELD_NAME).value(theObservationIdentifier)))))
				.size(1));
	}

	@Override
	public CodeJson getObservationCodeDocument(String theCodeSystemHash, String theText) {
		if (theCodeSystemHash == null && theText == null) {
			throw new InvalidRequestException(Msg.code(1187)
					+ "Require a non-null code system hash value or display value for observation code document query");
		}
		SearchRequest theSearchRequest = buildSingleObservationCodeSearchRequest(theCodeSystemHash, theText);
		CodeJson observationCodeDocumentJson = null;
		try {
			SearchResponse<CodeJson> observationCodeDocumentResponse =
					myRestHighLevelClient.search(theSearchRequest, CodeJson.class);
			List<Hit<CodeJson>> observationCodeDocumentHits =
					observationCodeDocumentResponse.hits().hits();
			if (!observationCodeDocumentHits.isEmpty()) {
				// There should be no more than one hit for the code lookup.
				observationCodeDocumentJson = observationCodeDocumentHits.get(0).source();
			}

		} catch (IOException theE) {
			throw new InvalidRequestException(
					Msg.code(1188) + "Unable to execute observation code document query hash code or display", theE);
		}

		return observationCodeDocumentJson;
	}

	private SearchRequest buildSingleObservationCodeSearchRequest(String theCodeSystemHash, String theText) {
		Function<Query.Builder, ObjectBuilder<Query>> qb = i -> {
			if (theCodeSystemHash != null) {
				i.term(tq -> tq.field(CODE_HASH).value(theCodeSystemHash));
			} else {
				i.matchPhrase(mp -> mp.field(CODE_TEXT).query(theText));
			}
			return i;
		};

		return SearchRequest.of(i -> i.index(OBSERVATION_CODE_INDEX).size(1).query(q -> q.bool(qb2 -> qb2.must(qb))));
	}

	@Override
	public Boolean createOrUpdateObservationIndex(String theDocumentId, ObservationJson theObservationDocument) {
		try {
			IndexRequest<ObservationJson> request = IndexRequest.of(
					i -> i.index(OBSERVATION_INDEX).id(theDocumentId).document(theObservationDocument));
			IndexResponse indexResponse = myRestHighLevelClient.index(request);

			Result result = indexResponse.result();
			return (result == Result.Created || result == Result.Updated);
		} catch (IOException e) {
			throw new InvalidRequestException(
					Msg.code(1189) + "Unable to persist Observation document " + theDocumentId);
		}
	}

	@Override
	public Boolean createOrUpdateObservationCodeIndex(
			String theCodeableConceptID, CodeJson theObservationCodeDocument) {
		try {
			IndexRequest<CodeJson> request = IndexRequest.of(i ->
					i.index(OBSERVATION_CODE_INDEX).id(theCodeableConceptID).document(theObservationCodeDocument));
			IndexResponse indexResponse = myRestHighLevelClient.index(request);

			Result result = indexResponse.result();
			return (result == Result.Created || result == Result.Updated);
		} catch (IOException theE) {
			throw new InvalidRequestException(
					Msg.code(1190) + "Unable to persist Observation Code document " + theCodeableConceptID);
		}
	}

	@Override
	public void close() throws IOException {
		// nothing
	}

	@Override
	public List<IBaseResource> getObservationResources(Collection<? extends IResourcePersistentId> thePids) {
		SearchRequest searchRequest = buildObservationResourceSearchRequest(thePids);
		try {
			SearchResponse<ObservationJson> observationDocumentResponse =
					myRestHighLevelClient.search(searchRequest, ObservationJson.class);
			List<Hit<ObservationJson>> observationDocumentHits =
					observationDocumentResponse.hits().hits();
			IParser parser = TolerantJsonParser.createWithLenientErrorHandling(myContext, null);
			Class<? extends IBaseResource> resourceType =
					myContext.getResourceDefinition(OBSERVATION_RESOURCE_NAME).getImplementingClass();
			/**
			 * @see ca.uhn.fhir.jpa.dao.BaseHapiFhirDao#toResource(Class, IBaseResourceEntity, Collection, boolean) for
			 * details about parsing raw json to BaseResource
			 */
			return observationDocumentHits.stream()
					.map(Hit::source)
					.map(observationJson -> parser.parseResource(resourceType, observationJson.getResource()))
					.collect(Collectors.toList());
		} catch (IOException theE) {
			throw new InvalidRequestException(
					Msg.code(2003) + "Unable to execute observation document query for provided IDs " + thePids, theE);
		}
	}

	//	private ObservationJson parseObservationJson(SearchHit theSearchHit) {
	//		try {
	//			return objectMapper.readValue(theSearchHit.getSourceAsString(), ObservationJson.class);
	//		} catch (JsonProcessingException exp) {
	//			throw new InvalidRequestException(Msg.code(2004) + "Unable to parse the observation resource json", exp);
	//		}
	//	}

	private SearchRequest buildObservationResourceSearchRequest(Collection<? extends IResourcePersistentId> thePids) {
		List<FieldValue> values = thePids.stream()
				.map(Object::toString)
				.map(v -> FieldValue.of(v))
				.collect(Collectors.toList());

		return SearchRequest.of(sr -> sr.index(OBSERVATION_INDEX)
				.query(qb -> qb.bool(bb -> bb.must(bbm -> {
					bbm.terms(terms ->
							terms.field(OBSERVATION_IDENTIFIER_FIELD_NAME).terms(termsb -> termsb.value(values)));
					return bbm;
				})))
				.size(thePids.size()));
	}

	@Override
	public void deleteObservationDocument(String theDocumentId) {
		try {
			myRestHighLevelClient.deleteByQuery(qb -> qb.query(ob ->
					ob.term(tb -> tb.field(OBSERVATION_IDENTIFIER_FIELD_NAME).value(theDocumentId))));
		} catch (IOException theE) {
			throw new InvalidRequestException(Msg.code(1191) + "Unable to delete Observation " + theDocumentId);
		}
	}

	@VisibleForTesting
	public void deleteAllDocumentsForTest(String theIndexName) throws IOException {
		myRestHighLevelClient.deleteByQuery(ob -> ob.index(theIndexName).query(fn -> fn.matchAll(mab -> mab)));
	}

	@VisibleForTesting
	public void refreshIndex(String theIndexName) throws IOException {
		myRestHighLevelClient.indices().refresh(fn -> fn.index(theIndexName));
	}
}
