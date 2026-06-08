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
import ca.uhn.fhir.jpa.model.entity.ResourceSystemEntity;
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

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Predicate builder for the compressed token index tables:
 * <ul>
 *   <li>{@code HFJ_SPIDX2_TOKEN_COMMON_RES} + {@code HFJ_SPIDX2_TOKEN_COMMON} (Mode.COMMON)</li>
 *   <li>{@code HFJ_SPIDX2_TOKEN_IDENTIFIER} (Mode.IDENTIFIER)</li>
 * </ul>
 *
 * @see ca.uhn.fhir.jpa.model.entity.TokenIndexStrategy
 */
public class CompressedTokenPredicateBuilder extends BaseTokenPredicateBuilder {

	private final TokenIndexMode myIndexMode;
	private final DbColumn myColumnResId;

	// COMMON mode: primary table is HFJ_SPIDX2_TOKEN_COMMON_RES
	private DbColumn myColumnCommonHashSysAndValue;

	// IDENTIFIER mode: primary table is HFJ_SPIDX2_TOKEN_IDENTIFIER
	private DbColumn myColumnIdentifierHashIdentity;
	private DbColumn myColumnIdentifierHashValue;
	private DbColumn myColumnIdentifierTypeHashSysAndValue;
	private DbColumn myColumnIdentifierSystemUrlId;

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
			myColumnIdentifierSystemUrlId = getTable().addColumn("SP_SYSTEM_URL_ID");
		}
	}

	@Override
	public DbColumn getResourceIdColumn() {
		return myColumnResId;
	}

	@Override
	protected Condition buildNeHashIdentityCondition(
			RequestPartitionId theRequestPartitionId, String theResourceName, String theParamName) {
		long hashIdentity = BaseResourceIndexedSearchParam.calculateHashIdentity(
				getPartitionSettings(), theRequestPartitionId, theResourceName, theParamName);
		mySearchParamIdentityCacheSvc.findOrCreateSearchParamIdentity(hashIdentity, theResourceName, theParamName);

		if (myIndexMode == TokenIndexMode.IDENTIFIER) {
			return BinaryCondition.equalTo(myColumnIdentifierHashIdentity, generatePlaceholder(hashIdentity));
		}
		// COMMON mode: HASH_IDENTITY is in HFJ_SPIDX2_TOKEN_COMMON, requires subquery from HFJ_SPIDX2_TOKEN_COMMON_RES
		return buildCommonSubquery("HASH_IDENTITY", hashIdentity);
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

	@Override
	protected Condition createPredicateOrList(
			String theResourceType,
			String theSearchParamName,
			List<FhirVersionIndependentConcept> theCodes,
			boolean theWantEquals) {

		Condition[] conditions = new Condition[theCodes.size()];
		for (int i = 0; i < theCodes.size(); i++) {
			FhirVersionIndependentConcept token = theCodes.get(i);
			if (myIndexMode == TokenIndexMode.COMMON) {
				conditions[i] = buildCommonCondition(theResourceType, theSearchParamName, token, theWantEquals);
			} else {
				conditions[i] = buildIdentifierCondition(theResourceType, theSearchParamName, token, theWantEquals);
			}
		}

		if (conditions.length == 1) {
			return conditions[0];
		}
		return theWantEquals
				? QueryParameterUtils.toOrPredicate(conditions)
				: QueryParameterUtils.toAndPredicate(conditions);
	}

	/**
	 * Builds the predicate for one token in COMMON mode. A system+value token matches
	 * {@code HASH_SYS_AND_VALUE} directly on {@code HFJ_SPIDX2_TOKEN_COMMON_RES}; a value-only or system-only
	 * token is resolved with a subquery into {@code HFJ_SPIDX2_TOKEN_COMMON}.
	 */
	private Condition buildCommonCondition(
			String theResourceType,
			String theSearchParamName,
			FhirVersionIndependentConcept theToken,
			boolean theWantEquals) {

		String system = theToken.getSystem();
		String code = theToken.getCode();

		if (system == null) {
			// value only: subquery into TOKEN_COMMON by HASH_VALUE
			long hash = ResourceIndexedSearchParamToken.calculateHashValue(
					getPartitionSettings(), getRequestPartitionId(), theResourceType, theSearchParamName, code);
			Condition subquery = buildCommonSubquery("HASH_VALUE", hash);
			return theWantEquals ? subquery : new NotCondition(subquery);
		}

		if (isBlank(code)) {
			// system only: subquery into TOKEN_COMMON by HASH_IDENTITY + SYSTEM_ID
			long systemId = ResourceSystemEntity.calculatePid(system);
			long hashIdentity = BaseResourceIndexedSearchParam.calculateHashIdentity(
					getPartitionSettings(), getRequestPartitionId(), theResourceType, theSearchParamName);
			Condition subquery = buildCommonSystemOnlySubquery(hashIdentity, systemId);
			return theWantEquals ? subquery : new NotCondition(subquery);
		}

		// system + value: direct HASH_SYS_AND_VALUE lookup on TOKEN_COMMON_RES
		long hash = ResourceIndexedSearchParamToken.calculateHashSystemAndValue(
				getPartitionSettings(), getRequestPartitionId(), theResourceType, theSearchParamName, system, code);
		return equalsPredicate(theWantEquals, myColumnCommonHashSysAndValue, hash);
	}

	/**
	 * Builds a predicate for COMMON mode:
	 * HASH_SYS_AND_VALUE IN (SELECT HASH_SYS_AND_VALUE FROM HFJ_SPIDX2_TOKEN_COMMON WHERE theHashColumnName = ?).
	 * theHashColumnName is HASH_VALUE for a value-only search, or HASH_IDENTITY for the not-equals (ne) identity scoping.
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

	/**
	 * Builds a system-only predicate for COMMON mode:
	 * HASH_SYS_AND_VALUE IN (SELECT HASH_SYS_AND_VALUE FROM HFJ_SPIDX2_TOKEN_COMMON WHERE HASH_IDENTITY = ? AND SYSTEM_ID = ?).
	 */
	private InCondition buildCommonSystemOnlySubquery(long theHashIdentity, long theSystemId) {
		DbTable commonTable = getSearchQueryBuilder().addTable("HFJ_SPIDX2_TOKEN_COMMON");
		DbColumn hashSysAndValueCol = commonTable.addColumn("HASH_SYS_AND_VALUE");
		DbColumn hashIdentityCol = commonTable.addColumn("HASH_IDENTITY");
		DbColumn systemIdCol = commonTable.addColumn("SYSTEM_ID");

		SelectQuery subquery = new SelectQuery();
		subquery.addColumns(hashSysAndValueCol);
		subquery.addFromTable(commonTable);
		subquery.addCondition(ComboCondition.and(
				BinaryCondition.equalTo(hashIdentityCol, generatePlaceholder(theHashIdentity)),
				BinaryCondition.equalTo(systemIdCol, generatePlaceholder(theSystemId))));

		return new InCondition(myColumnCommonHashSysAndValue, subquery);
	}

	private Condition buildIdentifierCondition(
			String theResourceType,
			String theSearchParamName,
			FhirVersionIndependentConcept theToken,
			boolean theWantEquals) {

		String system = theToken.getSystem();
		String code = theToken.getCode();

		// for :of-type search parameters we use TYPE_HASH_SYS_AND_VALUE column for lookup
		if (theSearchParamName.endsWith(Constants.PARAMQUALIFIER_TOKEN_OF_TYPE) && system != null && isNotBlank(code)) {
			long hash = ResourceIndexedSearchParamToken.calculateHashSystemAndValue(
					getPartitionSettings(), getRequestPartitionId(), theResourceType, theSearchParamName, system, code);
			return equalsPredicate(theWantEquals, myColumnIdentifierTypeHashSysAndValue, hash);
		}

		// HASH_IDENTITY (resource type + param) added only for equality searches; :not/ne gets it from the
		// caller (buildNeHashIdentityCondition), and value searches already imply it via HASH_VALUE.
		Condition hashIdentityCondition = null;
		if (theWantEquals) {
			long hashIdentity = BaseResourceIndexedSearchParam.calculateHashIdentity(
					getPartitionSettings(), getRequestPartitionId(), theResourceType, theSearchParamName);
			hashIdentityCondition =
					BinaryCondition.equalTo(myColumnIdentifierHashIdentity, generatePlaceholder(hashIdentity));
		}

		// build the system and value predicates
		Condition systemCondition = systemPredicate(theWantEquals, system);
		Condition valueCondition = null;
		if (isNotBlank(code)) {
			long hashValue = ResourceIndexedSearchParamToken.calculateHashValue(
					getPartitionSettings(), getRequestPartitionId(), theResourceType, theSearchParamName, code);
			valueCondition = equalsPredicate(theWantEquals, myColumnIdentifierHashValue, hashValue);
		}

		// equals - system AND value must match; :not/ne - the inverse, system OR value <>
		Condition tokenCondition = theWantEquals
				? QueryParameterUtils.toAndPredicate(systemCondition, valueCondition)
				: QueryParameterUtils.toOrPredicate(systemCondition, valueCondition);

		return QueryParameterUtils.toAndPredicate(hashIdentityCondition, tokenCondition);
	}

	/**
	 * Builds the {@code SP_SYSTEM_URL_ID} predicate for a token's system, or {@code null} when there is no system
	 * constraint.
	 */
	private Condition systemPredicate(boolean theWantEquals, String theSystem) {
		// null system = no system constraint (value-only search)
		if (theSystem == null) {
			return null;
		}

		// empty system ("identifier=|value") is indexed as NULL, so match on NULL/NOT NULL
		if (isBlank(theSystem)) {
			return theWantEquals
					? UnaryCondition.isNull(myColumnIdentifierSystemUrlId)
					: UnaryCondition.isNotNull(myColumnIdentifierSystemUrlId);
		}

		long systemId = ResourceSystemEntity.calculatePid(theSystem);
		if (theWantEquals) {
			// equals on a concrete system: SP_SYSTEM_URL_ID must equal this system's id
			return BinaryCondition.equalTo(myColumnIdentifierSystemUrlId, generatePlaceholder(systemId));
		}

		// concrete system, :not/ne: match any row that isn't this system (including rows with no system)
		return QueryParameterUtils.toOrPredicate(
				equalsPredicate(false, myColumnIdentifierSystemUrlId, systemId),
				UnaryCondition.isNull(myColumnIdentifierSystemUrlId));
	}

	/**
	 * Builds {@code theColumn = ?} when {@code theWantEquals} is {@code true}, otherwise {@code theColumn <> ?}.
	 */
	private Condition equalsPredicate(boolean theWantEquals, DbColumn theColumn, long theValue) {
		return theWantEquals
				? BinaryCondition.equalTo(theColumn, generatePlaceholder(theValue))
				: BinaryCondition.notEqualTo(theColumn, generatePlaceholder(theValue));
	}
}
