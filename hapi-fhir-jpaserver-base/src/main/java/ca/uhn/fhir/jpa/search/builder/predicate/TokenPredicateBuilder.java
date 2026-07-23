/*-
 * #%L
 * HAPI FHIR JPA Server
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
package ca.uhn.fhir.jpa.search.builder.predicate;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamToken;
import ca.uhn.fhir.jpa.search.builder.sql.SearchQueryBuilder;
import ca.uhn.fhir.jpa.util.QueryParameterUtils;
import ca.uhn.fhir.util.FhirVersionIndependentConcept;
import com.google.common.annotations.VisibleForTesting;
import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.Condition;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;

import java.util.Arrays;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class TokenPredicateBuilder extends BaseTokenPredicateBuilder {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(TokenPredicateBuilder.class);

	private final DbColumn myColumnResId;
	private final DbColumn myColumnHashSystemAndValue;
	private final DbColumn myColumnHashSystem;
	private final DbColumn myColumnHashValue;
	private final DbColumn myColumnSystem;
	private final DbColumn myColumnValue;
	private final DbColumn myColumnHashIdentity;

	/**
	 * Constructor
	 */
	public TokenPredicateBuilder(SearchQueryBuilder theSearchSqlBuilder) {
		super(theSearchSqlBuilder, theSearchSqlBuilder.addTable("HFJ_SPIDX_TOKEN"));
		myColumnResId = getTable().addColumn("RES_ID");
		myColumnHashIdentity = getTable().addColumn("HASH_IDENTITY");
		myColumnHashSystem = getTable().addColumn("HASH_SYS");
		myColumnHashSystemAndValue = getTable().addColumn("HASH_SYS_AND_VALUE");
		myColumnHashValue = getTable().addColumn("HASH_VALUE");
		myColumnSystem = getTable().addColumn("SP_SYSTEM");
		myColumnValue = getTable().addColumn("SP_VALUE");
	}

	@Override
	public DbColumn getColumnHashIdentity() {
		return myColumnHashIdentity;
	}

	@VisibleForTesting
	public void setStorageSettingsForUnitTest(JpaStorageSettings theStorageSettings) {
		myStorageSettings = theStorageSettings;
	}

	@Override
	public DbColumn getResourceIdColumn() {
		return myColumnResId;
	}

	public DbColumn getColumnSystem() {
		return myColumnSystem;
	}

	public DbColumn getColumnValue() {
		return myColumnValue;
	}

	@Override
	protected Condition buildOptionalHashIdentityForEquals(
			RequestPartitionId theRequestPartitionId, String theResourceName, String theParamName) {
		if (myStorageSettings.isIncludeHashIdentityForTokenSearches()) {
			return createHashIdentityPredicate(theRequestPartitionId, theResourceName, theParamName);
		}
		return null;
	}

	@Override
	protected Condition createPredicateOrList(
			String theResourceType,
			String theSearchParamName,
			List<FhirVersionIndependentConcept> theCodes,
			boolean theWantEquals) {
		Condition[] conditions = new Condition[theCodes.size()];

		Long[] hashes = new Long[theCodes.size()];
		DbColumn[] columns = new DbColumn[theCodes.size()];
		boolean haveMultipleColumns = false;
		for (int i = 0; i < conditions.length; i++) {

			FhirVersionIndependentConcept nextToken = theCodes.get(i);
			long hash;
			DbColumn column;
			if (nextToken.getSystem() == null) {
				hash = ResourceIndexedSearchParamToken.calculateHashValue(
						getPartitionSettings(),
						getRequestPartitionId(),
						theResourceType,
						theSearchParamName,
						nextToken.getCode());
				column = myColumnHashValue;
			} else if (isBlank(nextToken.getCode())) {
				hash = ResourceIndexedSearchParamToken.calculateHashSystem(
						getPartitionSettings(),
						getRequestPartitionId(),
						theResourceType,
						theSearchParamName,
						nextToken.getSystem());
				column = myColumnHashSystem;
			} else {
				hash = ResourceIndexedSearchParamToken.calculateHashSystemAndValue(
						getPartitionSettings(),
						getRequestPartitionId(),
						theResourceType,
						theSearchParamName,
						nextToken.getSystem(),
						nextToken.getCode());
				column = myColumnHashSystemAndValue;
			}
			hashes[i] = hash;
			columns[i] = column;
			if (i > 0 && columns[0] != columns[i]) {
				haveMultipleColumns = true;
			}
		}

		if (!haveMultipleColumns && conditions.length > 1) {
			List<Long> values = Arrays.asList(hashes);
			return QueryParameterUtils.toEqualToOrInPredicate(columns[0], generatePlaceholders(values), !theWantEquals);
		}

		for (int i = 0; i < conditions.length; i++) {
			String valuePlaceholder = generatePlaceholder(hashes[i]);
			if (theWantEquals) {
				conditions[i] = BinaryCondition.equalTo(columns[i], valuePlaceholder);
			} else {
				conditions[i] = BinaryCondition.notEqualTo(columns[i], valuePlaceholder);
			}
		}
		if (conditions.length > 1) {
			if (theWantEquals) {
				return QueryParameterUtils.toOrPredicate(conditions);
			} else {
				return QueryParameterUtils.toAndPredicate(conditions);
			}
		} else {
			return conditions[0];
		}
	}
}
