package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.config.ElasticsearchWithPrefixConfig;
import ca.uhn.fhir.jpa.search.lastn.ElasticsearchRestClientFactory;
import ca.uhn.fhir.test.utilities.docker.RequiresDocker;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.elasticsearch.ElasticsearchContainer;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

@RequiresDocker
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ElasticsearchWithPrefixConfig.class})
public class ElasticsearchPrefixTest {

	@Autowired
	ElasticsearchContainer elasticsearchContainer;

	public static String ELASTIC_PREFIX = "hapi-fhir";
	@Test
	public void test() throws IOException {
		//Given
		RestHighLevelClient elasticsearchHighLevelRestClient = ElasticsearchRestClientFactory.createElasticsearchHighLevelRestClient(
			"http", elasticsearchContainer.getHost() + ":" + elasticsearchContainer.getMappedPort(9200), "", "");

		//When
		RestClient lowLevelClient = elasticsearchHighLevelRestClient.getLowLevelClient();
		Response get = lowLevelClient.performRequest(new Request("GET", "/_cat/indices"));
		String catIndexes = EntityUtils.toString(get.getEntity());

		//Then
		assertThat(catIndexes, containsString(ELASTIC_PREFIX + "-resourcetable-000001"));
		assertThat(catIndexes, containsString(ELASTIC_PREFIX + "-termconcept-000001"));

	}

}
