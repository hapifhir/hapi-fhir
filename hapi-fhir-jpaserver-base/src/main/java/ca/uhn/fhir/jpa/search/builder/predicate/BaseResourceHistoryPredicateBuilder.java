/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.search.builder.sql.SearchQueryBuilder;
import ca.uhn.fhir.jpa.util.QueryParameterUtils;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.param.UriParam;
import ca.uhn.fhir.rest.server.exceptions.MethodNotAllowedException;
import ca.uhn.fhir.util.StringUtil;
import ca.uhn.fhir.util.UrlUtil;
import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.Condition;
import com.healthmarketscience.sqlbuilder.FunctionCall;
import com.healthmarketscience.sqlbuilder.UnaryCondition;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbTable;

import java.util.List;

import static ca.uhn.fhir.jpa.search.builder.predicate.StringPredicateBuilder.createLeftAndRightMatchLikeExpression;
import static ca.uhn.fhir.jpa.search.builder.predicate.StringPredicateBuilder.createLeftMatchLikeExpression;

public abstract class BaseResourceHistoryPredicateBuilder extends BaseJoiningPredicateBuilder
		implements ISourcePredicateBuilder {
	protected DbColumn myColumnSourceUri;
	protected DbColumn myColumnRequestId;
	protected DbColumn myResourceIdColumn;

	public BaseResourceHistoryPredicateBuilder(
			SearchQueryBuilder theSearchSqlBuilder, DbTable theTable, String theResourceIdColumn) {
		super(theSearchSqlBuilder, theTable);
		myColumnSourceUri = getTable().addColumn("SOURCE_URI");
		myColumnRequestId = getTable().addColumn("REQUEST_ID");
		myResourceIdColumn = getTable().addColumn(theResourceIdColumn);
	}

	@Override
	public DbColumn getResourceIdColumn() {
		return myResourceIdColumn;
	}

	@Override
	public Condition createPredicateSourceUri(String theSourceUri) {
		return combineWithRequestPartitionIdPredicate(
				getRequestPartitionId(), BinaryCondition.equalTo(myColumnSourceUri, generatePlaceholder(theSourceUri)));
	}

	@Override
	public Condition createPredicateMissingSourceUri() {
		return combineWithRequestPartitionIdPredicate(
				getRequestPartitionId(), UnaryCondition.isNull(myColumnSourceUri));
	}

	@Override
	public Condition createPredicateSourceUriWithModifiers(
			IQueryParameterType theQueryParameter, JpaStorageSettings theStorageSetting, String theSourceUri) {

		if (theQueryParameter.getMissing() != null && !theQueryParameter.getMissing()) {
			UnaryCondition condition = UnaryCondition.isNotNull(myColumnSourceUri);
			return combineWithRequestPartitionIdPredicate(getRequestPartitionId(), condition);
		}

		if (theQueryParameter instanceof UriParam uriParam && theQueryParameter.getQueryParameterQualifier() != null) {
			Condition condition =
					switch (uriParam.getQualifier()) {
						case ABOVE -> createPredicateSourceAbove(theSourceUri);
						case BELOW -> createPredicateSourceBelow(theSourceUri);
						case CONTAINS -> createPredicateSourceContains(theStorageSetting, theSourceUri);
					};
			return combineWithRequestPartitionIdPredicate(getRequestPartitionId(), condition);
		}

		return createPredicateSourceUri(theSourceUri);
	}

	private Condition createPredicateSourceAbove(String theSourceUri) {
		List<String> aboveUriCandidates = UrlUtil.getAboveUriCandidates(theSourceUri);
		List<String> aboveUriPlaceholders = generatePlaceholders(aboveUriCandidates);
		return QueryParameterUtils.toEqualToOrInPredicate(myColumnSourceUri, aboveUriPlaceholders);
	}

	private Condition createPredicateSourceBelow(String theSourceUri) {
		String belowLikeExpression = createLeftMatchLikeExpression(theSourceUri);
		return BinaryCondition.like(myColumnSourceUri, generatePlaceholder(belowLikeExpression));
	}

	private Condition createPredicateSourceContains(JpaStorageSettings theStorageSetting, String theSourceUri) {
		if (theStorageSetting.isAllowContainsSearches()) {
			FunctionCall upperFunction = new FunctionCall("UPPER");
			upperFunction.addCustomParams(myColumnSourceUri);
			String normalizedString = StringUtil.normalizeStringForSearchIndexing(theSourceUri);
			String containsLikeExpression = createLeftAndRightMatchLikeExpression(normalizedString);
			return BinaryCondition.like(upperFunction, generatePlaceholder(containsLikeExpression));
		} else {
			throw new MethodNotAllowedException(
					Msg.code(2768) + ":contains modifier is disabled on this server");
		}
	}

	protected abstract int getContainsModifierDisabledCode();

	@Override
	public Condition createPredicateRequestId(String theRequestId) {
		return combineWithRequestPartitionIdPredicate(
				getRequestPartitionId(), BinaryCondition.equalTo(myColumnRequestId, generatePlaceholder(theRequestId)));
	}
}
