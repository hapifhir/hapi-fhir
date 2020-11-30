package ca.uhn.fhir.jpa.search.elastic;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import org.apache.commons.lang3.StringUtils;
import org.hibernate.search.backend.elasticsearch.index.IndexStatus;
import org.hibernate.search.engine.cfg.BackendSettings;
import org.hibernate.search.mapper.orm.automaticindexing.session.AutomaticIndexingSynchronizationStrategyNames;
import org.hibernate.search.mapper.orm.cfg.HibernateOrmMapperSettings;
import org.hibernate.search.backend.elasticsearch.cfg.ElasticsearchBackendSettings;
import org.hibernate.search.backend.elasticsearch.cfg.ElasticsearchIndexSettings;
import org.hibernate.search.mapper.orm.schema.management.SchemaManagementStrategyName;

import java.util.Properties;

/**
 * This class is used to inject appropriate properties into a hibernate
 * Properties object being used to create an entitymanager for a HAPI
 * FHIR JPA server.
 */
public class ElasticsearchHibernatePropertiesBuilder {

	private IndexStatus myRequiredIndexStatus = IndexStatus.YELLOW.YELLOW;
	private SchemaManagementStrategyName myIndexSchemaManagementStrategy = SchemaManagementStrategyName.CREATE;

	private String myRestUrl;
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

		//theProperties.put("hibernate.search.default.elasticsearch.host", myRestUrl);
		theProperties.put(BackendSettings.backendKey(ElasticsearchBackendSettings.HOSTS), myRestUrl);
		theProperties.put(BackendSettings.backendKey(ElasticsearchBackendSettings.PROTOCOL), myProtocol);

		if (StringUtils.isNotBlank(myUsername)) {
			//theProperties.put("hibernate.search.default.elasticsearch.username", myUsername);
			theProperties.put(BackendSettings.backendKey(ElasticsearchBackendSettings.USERNAME), myUsername);
		}
		if (StringUtils.isNotBlank(myPassword)) {
			//theProperties.put("hibernate.search.default.elasticsearch.password", myPassword);
			theProperties.put(BackendSettings.backendKey(ElasticsearchBackendSettings.PASSWORD), myPassword);
		}

		//theProperties.put("hibernate.search.default." + ElasticsearchEnvironment.INDEX_SCHEMA_MANAGEMENT_STRATEGY, myIndexSchemaManagementStrategy.getExternalName());
		theProperties.put(HibernateOrmMapperSettings.SCHEMA_MANAGEMENT_STRATEGY, myIndexSchemaManagementStrategy.externalRepresentation());

		//theProperties.put("hibernate.search.default." + ElasticsearchEnvironment.INDEX_MANAGEMENT_WAIT_TIMEOUT, Long.toString(myIndexManagementWaitTimeoutMillis));
		theProperties.put(BackendSettings.backendKey(ElasticsearchIndexSettings.SCHEMA_MANAGEMENT_MINIMAL_REQUIRED_STATUS_WAIT_TIMEOUT), Long.toString(myIndexManagementWaitTimeoutMillis));

		//theProperties.put("hibernate.search.default." + ElasticsearchEnvironment.REQUIRED_INDEX_STATUS, myRequiredIndexStatus.getElasticsearchString());
		theProperties.put(BackendSettings.backendKey(ElasticsearchIndexSettings.SCHEMA_MANAGEMENT_MINIMAL_REQUIRED_STATUS), myRequiredIndexStatus.externalRepresentation());


		// Need the mapping to be dynamic because of terminology indexes.
		//theProperties.put("hibernate.search.default.elasticsearch.dynamic_mapping", "true");
		theProperties.put(BackendSettings.backendKey(ElasticsearchIndexSettings.DYNAMIC_MAPPING), "true");


		// Only for unit tests
		//theProperties.put("hibernate.search.default." + ElasticsearchEnvironment.REFRESH_AFTER_WRITE, Boolean.toString(myDebugRefreshAfterWrite));
		//Docs say that "read-sync" or "sync" is the same as the old RefreshAfterWrite
		theProperties.put(HibernateOrmMapperSettings.AUTOMATIC_INDEXING_SYNCHRONIZATION_STRATEGY, myDebugSyncStrategy);

		//theProperties.put("hibernate.search." + ElasticsearchEnvironment.LOG_JSON_PRETTY_PRINTING, Boolean.toString(myDebugPrettyPrintJsonLog));
		theProperties.put(BackendSettings.backendKey(ElasticsearchBackendSettings.LOG_JSON_PRETTY_PRINTING), Boolean.toString(myDebugPrettyPrintJsonLog));

	}

	public ElasticsearchHibernatePropertiesBuilder setRequiredIndexStatus(IndexStatus theRequiredIndexStatus) {
		myRequiredIndexStatus = theRequiredIndexStatus;
		return this;
	}

	public ElasticsearchHibernatePropertiesBuilder setRestUrl(String theRestUrl) {
		myRestUrl = theRestUrl;
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


}
