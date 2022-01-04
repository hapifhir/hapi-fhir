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
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.search.backend.elasticsearch.index.layout.IndexLayoutStrategy;
import org.hibernate.search.backend.elasticsearch.logging.impl.Log;
import org.hibernate.search.util.common.logging.impl.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class instructs hibernate search on how to create index names for indexed entities.
 * In our case, we use this class to add an optional prefix to all indices which are created, which can be controlled via
 * {@link DaoConfig#setElasticSearchIndexPrefix(String)}.
 */
@Service
public class IndexNamePrefixLayoutStrategy implements IndexLayoutStrategy {

	@Autowired
	private DaoConfig myDaoConfig;

	static final Log log = LoggerFactory.make(Log.class, MethodHandles.lookup());
	public static final String NAME = "prefix";
	public static final Pattern UNIQUE_KEY_EXTRACTION_PATTERN = Pattern.compile("(.*)-\\d{6}");

	public String createInitialElasticsearchIndexName(String hibernateSearchIndexName) {
		return addPrefixIfNecessary(hibernateSearchIndexName + "-000001");
	}

	public String createWriteAlias(String hibernateSearchIndexName) {
		return addPrefixIfNecessary(hibernateSearchIndexName +"-write");
	}

	public String createReadAlias(String hibernateSearchIndexName) {
		return addPrefixIfNecessary(hibernateSearchIndexName + "-read");
	}

	private String addPrefixIfNecessary(String theCandidateName) {
		validateDaoConfigIsPresent();
		if (!StringUtils.isBlank(myDaoConfig.getElasticSearchIndexPrefix())) {
			return myDaoConfig.getElasticSearchIndexPrefix() + "-" + theCandidateName;
		} else {
			return theCandidateName;
		}
	}

	public String extractUniqueKeyFromHibernateSearchIndexName(String hibernateSearchIndexName) {
		return hibernateSearchIndexName;
	}

	public String extractUniqueKeyFromElasticsearchIndexName(String elasticsearchIndexName) {
		Matcher matcher = UNIQUE_KEY_EXTRACTION_PATTERN.matcher(elasticsearchIndexName);
		if (!matcher.matches()) {
			throw log.invalidIndexPrimaryName(elasticsearchIndexName, UNIQUE_KEY_EXTRACTION_PATTERN);
		} else {
			String candidateUniqueKey= matcher.group(1);
			return removePrefixIfNecessary(candidateUniqueKey);
		}
	}

	private String removePrefixIfNecessary(String theCandidateUniqueKey) {
		validateDaoConfigIsPresent();
		if (!StringUtils.isBlank(myDaoConfig.getElasticSearchIndexPrefix())) {
			return theCandidateUniqueKey.replace(myDaoConfig.getElasticSearchIndexPrefix() + "-", "");
		} else {
			return theCandidateUniqueKey;
		}
	}
	private void validateDaoConfigIsPresent() {
		if (myDaoConfig == null) {
			throw new ConfigurationException(Msg.code(1168) + "While attempting to boot HAPI FHIR, the Hibernate Search bootstrapper failed to find the DaoConfig. This probably means Hibernate Search has been recently upgraded, or somebody modified HapiFhirLocalContainerEntityManagerFactoryBean.");
		}
	}
}
