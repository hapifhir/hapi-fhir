/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.search.elastic;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.search.HapiHSearchAnalysisConfigurers;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.search.backend.elasticsearch.aws.cfg.ElasticsearchAwsBackendSettings;
import org.hibernate.search.backend.elasticsearch.aws.cfg.ElasticsearchAwsCredentialsTypeNames;
import org.hibernate.search.backend.elasticsearch.cfg.ElasticsearchBackendSettings;
import org.hibernate.search.backend.elasticsearch.cfg.ElasticsearchIndexSettings;
import org.hibernate.search.backend.elasticsearch.index.IndexStatus;
import org.hibernate.search.engine.cfg.BackendSettings;
import org.hibernate.search.mapper.orm.automaticindexing.session.AutomaticIndexingSynchronizationStrategyNames;
import org.hibernate.search.mapper.orm.cfg.HibernateOrmMapperSettings;
import org.hibernate.search.mapper.orm.schema.management.SchemaManagementStrategyName;
import org.slf4j.Logger;

import java.util.Properties;

import static org.hibernate.search.backend.elasticsearch.cfg.ElasticsearchBackendSettings.Defaults.SCROLL_TIMEOUT;
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

	public String getAwsRegion() {
		return myAwsRegion;
	}

	private String myAwsRegion;
	private long myIndexManagementWaitTimeoutMillis = 10000L;
	private long myScrollTimeoutSecs = SCROLL_TIMEOUT;
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
		theProperties.put(
				BackendSettings.backendKey(ElasticsearchIndexSettings.ANALYSIS_CONFIGURER),
				HapiHSearchAnalysisConfigurers.HapiElasticsearchAnalysisConfigurer.class.getName());
		theProperties.put(BackendSettings.backendKey(ElasticsearchBackendSettings.HOSTS), myHosts);
		theProperties.put(BackendSettings.backendKey(ElasticsearchBackendSettings.PROTOCOL), myProtocol);

		if (StringUtils.isNotBlank(myUsername)) {
			theProperties.put(BackendSettings.backendKey(ElasticsearchBackendSettings.USERNAME), myUsername);
		}
		if (StringUtils.isNotBlank(myPassword)) {
			theProperties.put(BackendSettings.backendKey(ElasticsearchBackendSettings.PASSWORD), myPassword);
		}
		theProperties.put(
				HibernateOrmMapperSettings.SCHEMA_MANAGEMENT_STRATEGY,
				myIndexSchemaManagementStrategy.externalRepresentation());
		theProperties.put(
				BackendSettings.backendKey(
						ElasticsearchIndexSettings.SCHEMA_MANAGEMENT_MINIMAL_REQUIRED_STATUS_WAIT_TIMEOUT),
				Long.toString(myIndexManagementWaitTimeoutMillis));
		theProperties.put(
				BackendSettings.backendKey(ElasticsearchIndexSettings.SCHEMA_MANAGEMENT_MINIMAL_REQUIRED_STATUS),
				myRequiredIndexStatus.externalRepresentation());
		// Need the mapping to be dynamic because of terminology indexes.
		theProperties.put(BackendSettings.backendKey(ElasticsearchIndexSettings.DYNAMIC_MAPPING), "true");
		// Only for unit tests
		theProperties.put(HibernateOrmMapperSettings.AUTOMATIC_INDEXING_SYNCHRONIZATION_STRATEGY, myDebugSyncStrategy);
		theProperties.put(
				BackendSettings.backendKey(ElasticsearchBackendSettings.LOG_JSON_PRETTY_PRINTING),
				Boolean.toString(myDebugPrettyPrintJsonLog));
		theProperties.put(
				BackendSettings.backendKey(ElasticsearchBackendSettings.SCROLL_TIMEOUT),
				Long.toString(myScrollTimeoutSecs));

		// This tells elasticsearch to use our custom index naming strategy.
		theProperties.put(
				BackendSettings.backendKey(ElasticsearchBackendSettings.LAYOUT_STRATEGY),
				IndexNamePrefixLayoutStrategy.class.getName());

		// This tells hibernate search to use this custom file for creating index settings. We use this to add a custom
		// max_ngram_diff
		theProperties.put(
				BackendSettings.backendKey(ElasticsearchIndexSettings.SCHEMA_MANAGEMENT_SETTINGS_FILE),
				"ca/uhn/fhir/jpa/elastic/index-settings.json");

		if (!StringUtils.isBlank(myAwsRegion)) {
			theProperties.put(BackendSettings.backendKey(ElasticsearchAwsBackendSettings.REGION), myAwsRegion);
			theProperties.put(BackendSettings.backendKey(ElasticsearchAwsBackendSettings.SIGNING_ENABLED), true);
			if (!StringUtils.isBlank(myUsername) && !StringUtils.isBlank(myPassword)) {
				theProperties.put(
						BackendSettings.backendKey(ElasticsearchAwsBackendSettings.CREDENTIALS_TYPE),
						ElasticsearchAwsCredentialsTypeNames.STATIC);
				theProperties.put(
						BackendSettings.backendKey(ElasticsearchAwsBackendSettings.CREDENTIALS_ACCESS_KEY_ID),
						myUsername);
				theProperties.put(
						BackendSettings.backendKey(ElasticsearchAwsBackendSettings.CREDENTIALS_SECRET_ACCESS_KEY),
						myPassword);
			} else {
				// Default to Standard IAM Auth provider if username or password is absent.
				theProperties.put(
						BackendSettings.backendKey(ElasticsearchAwsBackendSettings.CREDENTIALS_TYPE),
						ElasticsearchAwsCredentialsTypeNames.DEFAULT);
			}
		}
	}

	public ElasticsearchHibernatePropertiesBuilder setRequiredIndexStatus(IndexStatus theRequiredIndexStatus) {
		myRequiredIndexStatus = theRequiredIndexStatus;
		return this;
	}

	public ElasticsearchHibernatePropertiesBuilder setHosts(String hosts) {
		if (hosts.contains("://")) {
			throw new ConfigurationException(
					Msg.code(2139)
							+ "Elasticsearch URLs cannot include a protocol, that is a separate property. Remove http:// or https:// from this URL.");
		}
		myHosts = hosts;
		return this;
	}

	public ElasticsearchHibernatePropertiesBuilder setProtocol(String theProtocol) {
		myProtocol = theProtocol;
		return this;
	}

	public ElasticsearchHibernatePropertiesBuilder setIndexSchemaManagementStrategy(
			SchemaManagementStrategyName theIndexSchemaManagementStrategy) {
		myIndexSchemaManagementStrategy = theIndexSchemaManagementStrategy;
		return this;
	}

	public ElasticsearchHibernatePropertiesBuilder setIndexManagementWaitTimeoutMillis(
			long theIndexManagementWaitTimeoutMillis) {
		myIndexManagementWaitTimeoutMillis = theIndexManagementWaitTimeoutMillis;
		return this;
	}

	public ElasticsearchHibernatePropertiesBuilder setScrollTimeoutSecs(long theScrollTimeoutSecs) {
		myScrollTimeoutSecs = theScrollTimeoutSecs;
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
	 * If this is set to `true`, the AWS region will be used to configure the AWS client. Additionally, this will trigger
	 * HibernateSearch to attempt to use IAM Authentication. If the username and password are set in addition to the region,
	 * then the username and password will be used as the AWS_ACCESS_KEY_ID and AWS_SECRET_KEY_ID for a static credentials file for IAM.
	 *
	 * @param theAwsRegion The String version of the region, e.g. `us-east-2`.
	 * @return This builder.
	 */
	public ElasticsearchHibernatePropertiesBuilder setAwsRegion(String theAwsRegion) {
		myAwsRegion = theAwsRegion;
		return this;
	}
}
