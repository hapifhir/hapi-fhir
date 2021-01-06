package ca.uhn.fhir.jpa.search.lastn;

/*-
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

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

public class ElasticsearchRestClientFactory {


	private static String determineScheme(String theHostname) {
		int schemeIdx = theHostname.indexOf("://");
		if (schemeIdx > 0) {
			return theHostname.substring(0, schemeIdx);
		} else {
			return "http";
		}
	}

	private static String stripHostOfScheme(String theHostname) {
		int schemeIdx = theHostname.indexOf("://");
		if (schemeIdx > 0) {
			return theHostname.substring(schemeIdx + 3);
		} else {
			return theHostname;
		}
	}

    static public RestHighLevelClient createElasticsearchHighLevelRestClient(String theHostname, int thePort, String theUsername, String thePassword) {
        final CredentialsProvider credentialsProvider =
                new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials(theUsername, thePassword));
        RestClientBuilder clientBuilder = RestClient.builder(
                new HttpHost(stripHostOfScheme(theHostname), thePort, determineScheme(theHostname)))
                .setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder
                        .setDefaultCredentialsProvider(credentialsProvider));

        Header[] defaultHeaders = new Header[]{new BasicHeader("Content-Type", "application/json")};
        clientBuilder.setDefaultHeaders(defaultHeaders);

        return new RestHighLevelClient(clientBuilder);

    }
}
