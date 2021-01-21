package ca.uhn.fhir.jpa.search.lastn.config;

import com.github.dockerjava.api.exception.InternalServerErrorException;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.elasticsearch.ElasticsearchContainer;

import java.time.Duration;

import static java.time.temporal.ChronoUnit.SECONDS;

public class TestElasticsearchContainerHelper {


	public static final String ELASTICSEARCH_VERSION = "7.10.2";
	public static final String ELASTICSEARCH_IMAGE = "docker.elastic.co/elasticsearch/elasticsearch:" + ELASTICSEARCH_VERSION;

	public static ElasticsearchContainer getEmbeddedElasticSearch() {

		return new ElasticsearchContainer(ELASTICSEARCH_IMAGE)
			.withStartupTimeout(Duration.of(300, SECONDS));
	}

}
