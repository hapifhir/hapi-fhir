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
import ca.uhn.fhir.jpa.model.entity.BaseResourceIndexedSearchParam;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamToken;
import ca.uhn.fhir.jpa.search.builder.models.MissingQueryParameterPredicateParams;
import ca.uhn.fhir.jpa.search.builder.models.TokenIndexMode;
import ca.uhn.fhir.jpa.search.builder.sql.SearchQueryBuilder;
import ca.uhn.fhir.jpa.util.QueryParameterUtils;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.util.FhirVersionIndependentConcept;
import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.ComboCondition;
import com.healthmarketscience.sqlbuilder.Condition;
import com.healthmarketscience.sqlbuilder.InCondition;
import com.healthmarketscience.sqlbuilder.NotCondition;
import com.healthmarketscience.sqlbuilder.SelectQuery;
import com.healthmarketscience.sqlbuilder.UnaryCondition;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Predicate builder for the compressed token index tables:
 * <ul>
 *   <li>{@code HFJ_SPIDX2_TOKEN_COMMON_RES} + {@code HFJ_SPIDX2_TOKEN_COMMON} (Mode.COMMON)</li>
 *   <li>{@code HFJ_SPIDX2_TOKEN_IDENTIFIER} (Mode.IDENTIFIER)</li>
 * </ul>
 *
 * @see ca.uhn.fhir.jpa.model.entity.TokenIndexStrategyEnum
 */
public class CompressedTokenPredicateBuilder extends BaseTokenPredicateBuilder {

	private static final Logger ourLog = LoggerFactory.getLogger(CompressedTokenPredicateBuilder.class);

	private final TokenIndexMode myIndexMode;
	private final DbColumn myColumnResId;

	// COMMON mode: primary table is HFJ_SPIDX2_TOKEN_COMMON_RES
	private DbColumn myColumnCommonHashSysAndValue;

	// IDENTIFIER mode: primary table is HFJ_SPIDX2_TOKEN_IDENTIFIER
	private DbColumn myColumnIdentifierHashIdentity;
	private DbColumn myColumnIdentifierHashValue;
	private DbColumn myColumnIdentifierTypeHashSysAndValue;

	public CompressedTokenPredicateBuilder(SearchQueryBuilder theSearchSqlBuilder, TokenIndexMode theMode) {
		super(theSearchSqlBuilder, theSearchSqlBuilder.addTable(theMode.getTableName()));
		myIndexMode = theMode;
		myColumnResId = getTable().addColumn("RES_ID");

		if (myIndexMode == TokenIndexMode.COMMON) {
			myColumnCommonHashSysAndValue = getTable().addColumn("HASH_SYS_AND_VALUE");
		} else {
			myColumnIdentifierHashIdentity = getTable().addColumn("HASH_IDENTITY");
			myColumnIdentifierHashValue = getTable().addColumn("HASH_VALUE");
			myColumnIdentifierTypeHashSysAndValue = getTable().addColumn("TYPE_HASH_SYS_AND_VALUE");
		}
	}

	@Override
	public DbColumn getResourceIdColumn() {
		return myColumnResId;
	}

	@Override
	protected Condition buildNeHashIdentityCondition(
			RequestPartitionId theRequestPartitionId, String theResourceName, String theParamName) {
		return buildHashIdentityPredicate(theRequestPartitionId, theResourceName, theParamName);
	}

	@Override
	protected Condition createPredicateOrList(
			String theResourceType,
			String theSearchParamName,
			List<FhirVersionIndependentConcept> theCodes,
			boolean theWantEquals) {

		Condition[] conditions = new Condition[theCodes.size()];
		for (int i = 0; i < theCodes.size(); i++) {
			conditions[i] = buildSingleCondition(theResourceType, theSearchParamName, theCodes.get(i), theWantEquals);
		}

		if (conditions.length == 1) {
			return conditions[0];
		}
		return theWantEquals
				? QueryParameterUtils.toOrPredicate(conditions)
				: QueryParameterUtils.toAndPredicate(conditions);
	}

	/**
	 * Implements {@code :missing=} via {@code NOT EXISTS} subquery.
	 */
	@Override
	public Condition createPredicateParamMissingValue(MissingQueryParameterPredicateParams theParams) {
		long hashIdentity = BaseResourceIndexedSearchParam.calculateHashIdentity(
				getPartitionSettings(),
				theParams.getRequestPartitionId(),
				theParams.getResourceTablePredicateBuilder().getResourceType(),
				theParams.getParamName());

		SelectQuery subquery = new SelectQuery();
		subquery.addCustomColumns(1);
		subquery.addFromTable(getTable());

		Condition resIdCondition = BinaryCondition.equalTo(
				myColumnResId, theParams.getResourceTablePredicateBuilder().getResourceIdColumn());

		Condition hashIdentityCondition;
		if (myIndexMode == TokenIndexMode.IDENTIFIER) {
			hashIdentityCondition =
					BinaryCondition.equalTo(myColumnIdentifierHashIdentity, generatePlaceholder(hashIdentity));
			subquery.addCondition(ComboCondition.and(resIdCondition, hashIdentityCondition));
		} else {
			// COMMON mode: join TOKEN_COMMON_RES with TOKEN_COMMON via HASH_SYS_AND_VALUE
			DbTable commonTable = getSearchQueryBuilder().addTable("HFJ_SPIDX2_TOKEN_COMMON");
			DbColumn commonHashSysAndValue = commonTable.addColumn("HASH_SYS_AND_VALUE");
			DbColumn commonHashIdentity = commonTable.addColumn("HASH_IDENTITY");
			subquery.addFromTable(commonTable);

			Condition joinCondition = BinaryCondition.equalTo(myColumnCommonHashSysAndValue, commonHashSysAndValue);
			hashIdentityCondition = BinaryCondition.equalTo(commonHashIdentity, generatePlaceholder(hashIdentity));
			subquery.addCondition(ComboCondition.and(resIdCondition, joinCondition, hashIdentityCondition));
		}

		Condition exists = UnaryCondition.exists(subquery);
		if (theParams.isMissing()) {
			exists = new NotCondition(exists);
		}
		return combineWithRequestPartitionIdPredicate(theParams.getRequestPartitionId(), exists);
	}

	/**
	 * Not supported — compressed tables have no {@code SP_MISSING} column.
	 * QueryStack routes to {@link #createPredicateParamMissingValue} instead.
	 */
	@Override
	public Condition createPredicateParamMissingForNonReference(
			String theResourceName, String theParamName, Boolean theMissing, RequestPartitionId theRequestPartitionId) {
		throw new IllegalStateException(
				"SP_MISSING is not supported with compressed token tables. Set IndexMissingFields = DISABLED.");
	}

	private Condition buildSingleCondition(
			String theResourceType,
			String theSearchParamName,
			FhirVersionIndependentConcept theToken,
			boolean theWantEquals) {
		if (myIndexMode == TokenIndexMode.COMMON) {
			return buildCommonCondition(theResourceType, theSearchParamName, theToken, theWantEquals);
		}
		return buildIdentifierCondition(theResourceType, theSearchParamName, theToken, theWantEquals);
	}

	private Condition buildCommonCondition(
			String theResourceType,
			String theSearchParamName,
			FhirVersionIndependentConcept theToken,
			boolean theWantEquals) {

		String system = theToken.getSystem();
		String code = theToken.getCode();

		if (code != null && system != null) {
			// system + value → direct HASH_SYS_AND_VALUE lookup on TOKEN_COMMON_RES (fast, single index hit)
			long hash = ResourceIndexedSearchParamToken.calculateHashSystemAndValue(
					getPartitionSettings(), getRequestPartitionId(), theResourceType, theSearchParamName, system, code);
			return theWantEquals
					? BinaryCondition.equalTo(myColumnCommonHashSysAndValue, generatePlaceholder(hash))
					: BinaryCondition.notEqualTo(myColumnCommonHashSysAndValue, generatePlaceholder(hash));
		}

		if (code != null) {
			// value only → subquery into TOKEN_COMMON by HASH_VALUE
			long hash = ResourceIndexedSearchParamToken.calculateHashValue(
					getPartitionSettings(), getRequestPartitionId(), theResourceType, theSearchParamName, code);
			Condition subquery = buildCommonSubquery("HASH_VALUE", hash);
			return theWantEquals ? subquery : new NotCondition(subquery);
		}

		// system only: no HASH_SYS column in compressed tables; caller should route elsewhere
		ourLog.warn(
				"System-only token search is not supported with compressed token tables for param [{}]; returning no results",
				theSearchParamName);
		setMatchNothing();
		return null;
	}

	private Condition buildIdentifierCondition(
			String theResourceType,
			String theSearchParamName,
			FhirVersionIndependentConcept theToken,
			boolean theWantEquals) {

		String system = theToken.getSystem();
		String code = theToken.getCode();

		// For :of-type searches with both system and code, use TYPE_HASH_SYS_AND_VALUE for direct lookup
		if (theSearchParamName.endsWith(Constants.PARAMQUALIFIER_TOKEN_OF_TYPE) && system != null && code != null) {
			long hash = ResourceIndexedSearchParamToken.calculateHashSystemAndValue(
					getPartitionSettings(), getRequestPartitionId(), theResourceType, theSearchParamName, system, code);
			return theWantEquals
					? BinaryCondition.equalTo(myColumnIdentifierTypeHashSysAndValue, generatePlaceholder(hash))
					: BinaryCondition.notEqualTo(myColumnIdentifierTypeHashSysAndValue, generatePlaceholder(hash));
		}

		long hashIdentity = BaseResourceIndexedSearchParam.calculateHashIdentity(
				getPartitionSettings(), getRequestPartitionId(), theResourceType, theSearchParamName);
		Condition identityCondition =
				BinaryCondition.equalTo(myColumnIdentifierHashIdentity, generatePlaceholder(hashIdentity));

		if (code == null) {
			return identityCondition;
		}

		long hashValue = ResourceIndexedSearchParamToken.calculateHashValue(
				getPartitionSettings(), getRequestPartitionId(), theResourceType, theSearchParamName, code);
		Condition valueCondition = theWantEquals
				? BinaryCondition.equalTo(myColumnIdentifierHashValue, generatePlaceholder(hashValue))
				: BinaryCondition.notEqualTo(myColumnIdentifierHashValue, generatePlaceholder(hashValue));

		// TODO: add SP_SYSTEM_URL_ID discrimination once IResourceIdentifierCacheSvc exposes a read-only lookup
		return QueryParameterUtils.toAndPredicate(identityCondition, valueCondition);
	}

	/**
	 * Builds a HASH_IDENTITY predicate appropriate for the current mode.
	 * Used for the ne (not-equal) compound predicate.
	 */
	private Condition buildHashIdentityPredicate(
			RequestPartitionId theRequestPartitionId, String theResourceName, String theParamName) {
		long hashIdentity = BaseResourceIndexedSearchParam.calculateHashIdentity(
				getPartitionSettings(), theRequestPartitionId, theResourceName, theParamName);

		if (myIndexMode == TokenIndexMode.IDENTIFIER) {
			return BinaryCondition.equalTo(myColumnIdentifierHashIdentity, generatePlaceholder(hashIdentity));
		}
		// COMMON mode: HASH_IDENTITY lives in TOKEN_COMMON, not TOKEN_COMMON_RES
		return buildCommonSubquery("HASH_IDENTITY", hashIdentity);
	}

	/**
	 * Builds: HASH_SYS_AND_VALUE IN (SELECT HASH_SYS_AND_VALUE FROM HFJ_SPIDX2_TOKEN_COMMON WHERE {@code theHashColumnName} = ?)
	 */
	private InCondition buildCommonSubquery(String theHashColumnName, long theHashValue) {
		DbTable commonTable = getSearchQueryBuilder().addTable("HFJ_SPIDX2_TOKEN_COMMON");
		DbColumn hashSysAndValueCol = commonTable.addColumn("HASH_SYS_AND_VALUE");
		DbColumn hashCol = commonTable.addColumn(theHashColumnName);

		SelectQuery subquery = new SelectQuery();
		subquery.addColumns(hashSysAndValueCol);
		subquery.addFromTable(commonTable);
		subquery.addCondition(BinaryCondition.equalTo(hashCol, generatePlaceholder(theHashValue)));

		return new InCondition(myColumnCommonHashSysAndValue, subquery);
	}
}
