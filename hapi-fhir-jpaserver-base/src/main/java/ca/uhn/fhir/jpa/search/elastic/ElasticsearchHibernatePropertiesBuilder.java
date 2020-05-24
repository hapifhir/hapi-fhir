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
import org.hibernate.search.cfg.Environment;
import org.hibernate.search.elasticsearch.cfg.ElasticsearchEnvironment;
import org.hibernate.search.elasticsearch.cfg.ElasticsearchIndexStatus;
import org.hibernate.search.elasticsearch.cfg.IndexSchemaManagementStrategy;

import java.util.Properties;

/**
 * This class is used to inject appropriate properties into a hibernate
 * Properties object being used to create an entitymanager for a HAPI
 * FHIR JPA server.
 */
public class ElasticsearchHibernatePropertiesBuilder {

	private ElasticsearchIndexStatus myRequiredIndexStatus = ElasticsearchIndexStatus.YELLOW;
	private String myRestUrl;
	private String myUsername;
	private String myPassword;
	private IndexSchemaManagementStrategy myIndexSchemaManagementStrategy = IndexSchemaManagementStrategy.CREATE;
	private long myIndexManagementWaitTimeoutMillis = 10000L;
	private boolean myDebugRefreshAfterWrite = false;
	private boolean myDebugPrettyPrintJsonLog = false;

	public ElasticsearchHibernatePropertiesBuilder setUsername(String theUsername) {
		myUsername = theUsername;
		return this;
	}

	public ElasticsearchHibernatePropertiesBuilder setPassword(String thePassword) {
		myPassword = thePassword;
		return this;
	}

	public void apply(Properties theProperties) {

		// Don't use the Lucene properties as they conflict
		theProperties.remove("hibernate.search.model_mapping");

		// the below properties are used for ElasticSearch integration
		theProperties.put("hibernate.search.default." + Environment.INDEX_MANAGER_IMPL_NAME, "elasticsearch");
		theProperties.put("hibernate.search." + ElasticsearchEnvironment.ANALYSIS_DEFINITION_PROVIDER, ElasticsearchMappingProvider.class.getName());

		theProperties.put("hibernate.search.default.elasticsearch.host", myRestUrl);
		if (StringUtils.isNotBlank(myUsername)) {
			theProperties.put("hibernate.search.default.elasticsearch.username", myUsername);
		}
		if (StringUtils.isNotBlank(myPassword)) {
			theProperties.put("hibernate.search.default.elasticsearch.password", myPassword);
		}

		theProperties.put("hibernate.search.default." + ElasticsearchEnvironment.INDEX_SCHEMA_MANAGEMENT_STRATEGY, myIndexSchemaManagementStrategy.getExternalName());
		theProperties.put("hibernate.search.default." + ElasticsearchEnvironment.INDEX_MANAGEMENT_WAIT_TIMEOUT, Long.toString(myIndexManagementWaitTimeoutMillis));
		theProperties.put("hibernate.search.default." + ElasticsearchEnvironment.REQUIRED_INDEX_STATUS, myRequiredIndexStatus.getElasticsearchString());

		// Only for unit tests
		theProperties.put("hibernate.search.default." + ElasticsearchEnvironment.REFRESH_AFTER_WRITE, Boolean.toString(myDebugRefreshAfterWrite));
		theProperties.put("hibernate.search." + ElasticsearchEnvironment.LOG_JSON_PRETTY_PRINTING, Boolean.toString(myDebugPrettyPrintJsonLog));

	}

	public ElasticsearchHibernatePropertiesBuilder setRequiredIndexStatus(ElasticsearchIndexStatus theRequiredIndexStatus) {
		myRequiredIndexStatus = theRequiredIndexStatus;
		return this;
	}

	public ElasticsearchHibernatePropertiesBuilder setRestUrl(String theRestUrl) {
		myRestUrl = theRestUrl;
		return this;
	}

	public ElasticsearchHibernatePropertiesBuilder setIndexSchemaManagementStrategy(IndexSchemaManagementStrategy theIndexSchemaManagementStrategy) {
		myIndexSchemaManagementStrategy = theIndexSchemaManagementStrategy;
		return this;
	}

	public ElasticsearchHibernatePropertiesBuilder setIndexManagementWaitTimeoutMillis(long theIndexManagementWaitTimeoutMillis) {
		myIndexManagementWaitTimeoutMillis = theIndexManagementWaitTimeoutMillis;
		return this;
	}

	public ElasticsearchHibernatePropertiesBuilder setDebugRefreshAfterWrite(boolean theDebugRefreshAfterWrite) {
		myDebugRefreshAfterWrite = theDebugRefreshAfterWrite;
		return this;
	}

	public ElasticsearchHibernatePropertiesBuilder setDebugPrettyPrintJsonLog(boolean theDebugPrettyPrintJsonLog) {
		myDebugPrettyPrintJsonLog = theDebugPrettyPrintJsonLog;
		return this;
	}


}
