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
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.Result;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.indices.ExistsRequest;
import co.elastic.clients.util.ObjectBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.annotations.VisibleForTesting;
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
