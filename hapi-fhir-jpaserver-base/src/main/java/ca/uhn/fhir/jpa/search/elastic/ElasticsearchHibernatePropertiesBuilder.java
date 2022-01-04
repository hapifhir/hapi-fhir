package ca.uhn.fhir.jpa.search.elastic;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.jpa.search.lastn.ElasticsearchRestClientFactory;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.PutIndexTemplateRequest;
import org.elasticsearch.common.settings.Settings;
import org.hibernate.search.backend.elasticsearch.index.IndexStatus;
import org.hibernate.search.engine.cfg.BackendSettings;
import org.hibernate.search.mapper.orm.automaticindexing.session.AutomaticIndexingSynchronizationStrategyNames;
import org.hibernate.search.mapper.orm.cfg.HibernateOrmMapperSettings;
import org.hibernate.search.backend.elasticsearch.cfg.ElasticsearchBackendSettings;
import org.hibernate.search.backend.elasticsearch.cfg.ElasticsearchIndexSettings;
import org.hibernate.search.mapper.orm.schema.management.SchemaManagementStrategyName;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * This class is used to inject appropriate properties into a hibernate
 * Properties object being used to create an entitymanager for a HAPI
 * FHIR JPA server. This class also injects a starter template into the ES cluster.
 */
public class ElasticsearchHibernatePropertiesBuilder {
	private static final Logger ourLog = getLogger(ElasticsearchHibernatePropertiesBuilder.class);


	private IndexStatus myRequiredIndexStatus = IndexStatus.YELLOW;
	private SchemaManagementStrategyName myIndexSchemaManagementStrategy = SchemaManagementStrategyName.CREATE;

	private String myHosts;
	private String myUsername;
	private String myPassword;
	private long myIndexManagementWaitTimeoutMillis = 10000L;
	private String myDebugSyncStrategy = AutomaticIndexingSynchronizationStrategyNames.ASYNC;
	private boolean myDebugPrettyPrintJsonLog = false;
	private String myProtocol;

	public ElasticsearchHibernatePropertiesBuilder setUsername(String theUsername) {
		myUsername = theUsername;
		return this;
	}

	public ElasticsearchHibernatePropertiesBuilder setPassword(String thePassword) {
		myPassword = thePassword;
		return this;
	}

	public void apply(Properties theProperties) {

		// the below properties are used for ElasticSearch integration
		theProperties.put(BackendSettings.backendKey(BackendSettings.TYPE), "elasticsearch");
		theProperties.put(BackendSettings.backendKey(ElasticsearchIndexSettings.ANALYSIS_CONFIGURER), HapiElasticsearchAnalysisConfigurer.class.getName());
		theProperties.put(BackendSettings.backendKey(ElasticsearchBackendSettings.HOSTS), myHosts);
		theProperties.put(BackendSettings.backendKey(ElasticsearchBackendSettings.PROTOCOL), myProtocol);

		if (StringUtils.isNotBlank(myUsername)) {
			theProperties.put(BackendSettings.backendKey(ElasticsearchBackendSettings.USERNAME), myUsername);
		}
		if (StringUtils.isNotBlank(myPassword)) {
			theProperties.put(BackendSettings.backendKey(ElasticsearchBackendSettings.PASSWORD), myPassword);
		}
		theProperties.put(HibernateOrmMapperSettings.SCHEMA_MANAGEMENT_STRATEGY, myIndexSchemaManagementStrategy.externalRepresentation());
		theProperties.put(BackendSettings.backendKey(ElasticsearchIndexSettings.SCHEMA_MANAGEMENT_MINIMAL_REQUIRED_STATUS_WAIT_TIMEOUT), Long.toString(myIndexManagementWaitTimeoutMillis));
		theProperties.put(BackendSettings.backendKey(ElasticsearchIndexSettings.SCHEMA_MANAGEMENT_MINIMAL_REQUIRED_STATUS), myRequiredIndexStatus.externalRepresentation());
		// Need the mapping to be dynamic because of terminology indexes.
		theProperties.put(BackendSettings.backendKey(ElasticsearchIndexSettings.DYNAMIC_MAPPING), "true");
		// Only for unit tests
		theProperties.put(HibernateOrmMapperSettings.AUTOMATIC_INDEXING_SYNCHRONIZATION_STRATEGY, myDebugSyncStrategy);
		theProperties.put(BackendSettings.backendKey(ElasticsearchBackendSettings.LOG_JSON_PRETTY_PRINTING), Boolean.toString(myDebugPrettyPrintJsonLog));

		//This tells elasticsearch to use our custom index naming strategy.
		theProperties.put(BackendSettings.backendKey(ElasticsearchBackendSettings.LAYOUT_STRATEGY), IndexNamePrefixLayoutStrategy.class.getName());

		injectStartupTemplate(myProtocol, myHosts, myUsername, myPassword);
	}

	public ElasticsearchHibernatePropertiesBuilder setRequiredIndexStatus(IndexStatus theRequiredIndexStatus) {
		myRequiredIndexStatus = theRequiredIndexStatus;
		return this;
	}

	public ElasticsearchHibernatePropertiesBuilder setHosts(String hosts) {
		myHosts = hosts;
		return this;
	}

	public ElasticsearchHibernatePropertiesBuilder setProtocol(String theProtocol) {
		myProtocol = theProtocol;
		return this;
	}

	public ElasticsearchHibernatePropertiesBuilder setIndexSchemaManagementStrategy(SchemaManagementStrategyName theIndexSchemaManagementStrategy) {
		myIndexSchemaManagementStrategy = theIndexSchemaManagementStrategy;
		return this;
	}

	public ElasticsearchHibernatePropertiesBuilder setIndexManagementWaitTimeoutMillis(long theIndexManagementWaitTimeoutMillis) {
		myIndexManagementWaitTimeoutMillis = theIndexManagementWaitTimeoutMillis;
		return this;
	}

	public ElasticsearchHibernatePropertiesBuilder setDebugIndexSyncStrategy(String theSyncStrategy) {
		myDebugSyncStrategy = theSyncStrategy;
		return this;
	}


	public ElasticsearchHibernatePropertiesBuilder setDebugPrettyPrintJsonLog(boolean theDebugPrettyPrintJsonLog) {
		myDebugPrettyPrintJsonLog = theDebugPrettyPrintJsonLog;
		return this;
	}

	/**
	 * At startup time, injects a template into the elasticsearch cluster, which is needed for handling large ngram diffs.
	 * TODO GGG HS: In HS6.1, we should have a native way of performing index settings manipulation at bootstrap time, so this should
	 * eventually be removed in favour of whatever solution they come up with.
	 */
	void injectStartupTemplate(String theProtocol, String theHosts, @Nullable String theUsername, @Nullable String thePassword) {
		PutIndexTemplateRequest ngramTemplate = new PutIndexTemplateRequest("ngram-template")
			.patterns(Arrays.asList("*resourcetable-*", "*termconcept-*"))
			.settings(Settings.builder().put("index.max_ngram_diff", 50));

		try {
			RestHighLevelClient elasticsearchHighLevelRestClient = ElasticsearchRestClientFactory.createElasticsearchHighLevelRestClient(theProtocol, theHosts, theUsername, thePassword);
			ourLog.info("Adding starter template for large ngram diffs");
			AcknowledgedResponse acknowledgedResponse = elasticsearchHighLevelRestClient.indices().putTemplate(ngramTemplate, RequestOptions.DEFAULT);
			assert acknowledgedResponse.isAcknowledged();
		} catch (IOException theE) {
			theE.printStackTrace();
			throw new ConfigurationException(Msg.code(1169) + "Couldn't connect to the elasticsearch server to create necessary templates. Ensure the Elasticsearch user has permissions to create templates.");
		}
	}
}
