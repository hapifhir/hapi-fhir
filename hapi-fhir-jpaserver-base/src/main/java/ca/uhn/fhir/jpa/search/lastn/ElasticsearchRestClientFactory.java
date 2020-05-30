package ca.uhn.fhir.jpa.search.lastn;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.message.BasicHeader;
import org.shadehapi.elasticsearch.client.RestClient;
import org.shadehapi.elasticsearch.client.RestClientBuilder;
import org.shadehapi.elasticsearch.client.RestHighLevelClient;

public class ElasticsearchRestClientFactory {

    static public RestHighLevelClient createElasticsearchHighLevelRestClient(String theHostname, int thePort, String theUsername, String thePassword) {
        final CredentialsProvider credentialsProvider =
                new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials(theUsername, thePassword));

        RestClientBuilder clientBuilder = RestClient.builder(
                new HttpHost(theHostname, thePort))
                .setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder
                        .setDefaultCredentialsProvider(credentialsProvider));

        Header[] defaultHeaders = new Header[]{new BasicHeader("Content-Type", "application/json")};
        clientBuilder.setDefaultHeaders(defaultHeaders);

        return new RestHighLevelClient(clientBuilder);

    }
}
