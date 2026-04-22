/*-
 * #%L
 * HAPI FHIR JPA Server Test Utilities
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.test.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.elasticsearch.ElasticsearchContainer;

import java.time.Duration;

import static java.time.temporal.ChronoUnit.MINUTES;
import static java.util.Objects.requireNonNull;

public class TestElasticsearchContainerHelper {

	private static final Logger ourLog = LoggerFactory.getLogger(TestElasticsearchContainerHelper.class);


	public static final String ELASTICSEARCH_VERSION = "7.17.25";
	public static final String ELASTICSEARCH_IMAGE = "docker.elastic.co/elasticsearch/elasticsearch:" + ELASTICSEARCH_VERSION;

	public static ElasticsearchContainer getEmbeddedElasticSearch() {

		ElasticsearchContainer elasticsearchContainer = new ElasticsearchContainer(ELASTICSEARCH_IMAGE)
			// the default is 4GB which is too much for our little tests
			.withEnv("ES_JAVA_OPTS", "-Xms512m -Xmx512m")
			// turn off security warnings
			.withEnv("xpack.security.enabled", "false")
			// turn off machine learning (we don't need it in tests anyways)
			.withEnv("xpack.ml.enabled", "false")
			// we have some slow runners sometimes.
			.withStartupTimeout(Duration.of(4, MINUTES))
			.withCreateContainerCmdModifier(c -> requireNonNull(c.getHostConfig()).withMemory(1_000_000_000L))
			.withLogConsumer(new Slf4jLogConsumer(ourLog));

		return elasticsearchContainer;
	}

}
