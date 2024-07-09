package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.config.ElasticsearchWithPrefixConfig;
import ca.uhn.fhir.jpa.search.lastn.ElasticsearchRestClientFactory;
import ca.uhn.fhir.test.utilities.docker.RequiresDocker;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.cat.IndicesResponse;
import co.elastic.clients.elasticsearch.cat.indices.IndicesRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.elasticsearch.ElasticsearchContainer;

import java.io.IOException;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
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
		IndicesResponse indicesResponse = elasticsearchHighLevelRestClient
			.cat()
			.indices();

		String catIndexes = indicesResponse.valueBody().stream().map(IndicesRecord::index).collect(Collectors.joining(","));

		//Then
		assertThat(catIndexes).contains(ELASTIC_PREFIX + "-resourcetable-000001");
		assertThat(catIndexes).contains(ELASTIC_PREFIX + "-termconcept-000001");

	}

}
