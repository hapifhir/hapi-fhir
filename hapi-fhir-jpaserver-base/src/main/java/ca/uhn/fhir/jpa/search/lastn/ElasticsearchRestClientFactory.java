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

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.i18n.Msg;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.Node;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ElasticsearchRestClientFactory {

	public static ElasticsearchClient createElasticsearchHighLevelRestClient(
			String protocol, String hosts, @Nullable String theUsername, @Nullable String thePassword) {

		if (hosts.contains("://")) {
			throw new ConfigurationException(
					Msg.code(1173)
							+ "Elasticsearch URLs cannot include a protocol, that is a separate property. Remove http:// or https:// from this URL.");
		}
		String[] hostArray = hosts.split(",");
		List<Node> clientNodes = Arrays.stream(hostArray)
				.map(String::trim)
				.filter(s -> s.contains(":"))
				.map(h -> {
					int colonIndex = h.indexOf(":");
					String host = h.substring(0, colonIndex);
					int port = Integer.parseInt(h.substring(colonIndex + 1));
					return new Node(new HttpHost(host, port, protocol));
				})
				.collect(Collectors.toList());
		if (hostArray.length != clientNodes.size()) {
			throw new ConfigurationException(
					Msg.code(1174)
							+ "Elasticsearch URLs have to contain ':' as a host:port separator. Example: localhost:9200,localhost:9201,localhost:9202");
		}

		RestClientBuilder clientBuilder = RestClient.builder(clientNodes.toArray(new Node[0]));
		if (StringUtils.isNotBlank(theUsername) && StringUtils.isNotBlank(thePassword)) {
			final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
			credentialsProvider.setCredentials(
					AuthScope.ANY, new UsernamePasswordCredentials(theUsername, thePassword));
			clientBuilder.setHttpClientConfigCallback(
					httpClientBuilder -> httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider));
		}

		Header[] defaultHeaders = new Header[] {new BasicHeader("Content-Type", "application/json")};
		clientBuilder.setDefaultHeaders(defaultHeaders);

		RestClient restClient = clientBuilder.build();

		// Create the transport with a Jackson mapper
		ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());

		// And create the API client
		return new ElasticsearchClient(transport);
	}
}
