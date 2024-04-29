/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
import ca.uhn.fhir.jpa.search.lastn.json.ObservationJson;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.indices.ExistsRequest;
import com.google.common.annotations.VisibleForTesting;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ElasticsearchSvcImpl implements IElasticsearchSvc {

	// Index Constants
	public static final String OBSERVATION_INDEX = "observation_index";
	public static final String OBSERVATION_CODE_INDEX = "code_index";
	public static final String OBSERVATION_INDEX_SCHEMA_FILE = "ObservationIndexSchema.json";
	public static final String OBSERVATION_CODE_INDEX_SCHEMA_FILE = "ObservationCodeIndexSchema.json";

	// Aggregation Constants

	// Observation index document element names
	private static final String OBSERVATION_IDENTIFIER_FIELD_NAME = "identifier";

	// Code index document element names
	private static final String CODE_HASH = "codingcode_system_hash";
	private static final String CODE_TEXT = "text";

	private static final String OBSERVATION_RESOURCE_NAME = "Observation";

	private final ElasticsearchClient myRestHighLevelClient;

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

	@VisibleForTesting
	public void refreshIndex(String theIndexName) throws IOException {
		myRestHighLevelClient.indices().refresh(fn -> fn.index(theIndexName));
	}
}
