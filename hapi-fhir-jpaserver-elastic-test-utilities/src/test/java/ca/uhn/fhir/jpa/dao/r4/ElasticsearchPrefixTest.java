package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.config.ElasticsearchWithPrefixConfig;
import ca.uhn.fhir.jpa.search.lastn.ElasticsearchRestClientFactory;
import ca.uhn.fhir.test.utilities.docker.RequiresDocker;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.indices.GetIndexResponse;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.elasticsearch.ElasticsearchContainer;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_CLASS;

@RequiresDocker
@ExtendWith(SpringExtension.class)
// we don't reuse this context, so discard it and release our elastic container.
@DirtiesContext(classMode = AFTER_CLASS)
@ContextConfiguration(classes = {ElasticsearchWithPrefixConfig.class})
public class ElasticsearchPrefixTest {

	@Autowired
	ElasticsearchContainer elasticsearchContainer;

	public static String ELASTIC_PREFIX = "hapi-fhir";
	@Test
	public void test() throws IOException {
		//Given
		ElasticsearchClient elasticsearchHighLevelRestClient = ElasticsearchRestClientFactory.createElasticsearchHighLevelRestClient(
			"http", elasticsearchContainer.getHost() + ":" + elasticsearchContainer.getMappedPort(9200), "", "");

		//When
		GetIndexResponse indicesResponse = elasticsearchHighLevelRestClient
			.indices()
			.get(i -> i);

		String catIndexes = indicesResponse.result().toString();

		//Then
		assertThat(catIndexes, containsString(ELASTIC_PREFIX + "-resourcetable-000001"));
		assertThat(catIndexes, containsString(ELASTIC_PREFIX + "-termconcept-000001"));

	}

}
