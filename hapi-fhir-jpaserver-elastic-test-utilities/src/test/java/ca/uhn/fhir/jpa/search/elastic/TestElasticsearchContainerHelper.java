package ca.uhn.fhir.jpa.search.elastic;

import org.testcontainers.elasticsearch.ElasticsearchContainer;

import java.time.Duration;

import static java.time.temporal.ChronoUnit.SECONDS;

public class TestElasticsearchContainerHelper {


	public static final String ELASTICSEARCH_VERSION = "7.16.3";
	public static final String ELASTICSEARCH_IMAGE = "docker.elastic.co/elasticsearch/elasticsearch:" + ELASTICSEARCH_VERSION;

	public static ElasticsearchContainer getEmbeddedElasticSearch() {

		return new ElasticsearchContainer(ELASTICSEARCH_IMAGE)
			.withStartupTimeout(Duration.of(300, SECONDS));
	}

}
