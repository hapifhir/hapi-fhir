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
package ca.uhn.fhir.jpa.search.builder.predicate;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.search.builder.sql.SearchQueryBuilder;
import ca.uhn.fhir.jpa.util.QueryParameterUtils;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.param.UriParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.MethodNotAllowedException;
import ca.uhn.fhir.util.StringUtil;
import ca.uhn.fhir.util.UrlUtil;
import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.Condition;
import com.healthmarketscience.sqlbuilder.FunctionCall;
import com.healthmarketscience.sqlbuilder.UnaryCondition;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static ca.uhn.fhir.jpa.search.builder.predicate.StringPredicateBuilder.createLeftAndRightMatchLikeExpression;
import static ca.uhn.fhir.jpa.search.builder.predicate.StringPredicateBuilder.createLeftMatchLikeExpression;

public class SourcePredicateBuilder extends BaseJoiningPredicateBuilder {

	private static final Logger ourLog = LoggerFactory.getLogger(SourcePredicateBuilder.class);
	private final DbColumn myColumnSourceUri;
	private final DbColumn myColumnRequestId;
	private final DbColumn myResourceIdColumn;

	/**
	 * Constructor
	 */
	public SourcePredicateBuilder(SearchQueryBuilder theSearchSqlBuilder) {
		super(theSearchSqlBuilder, theSearchSqlBuilder.addTable("HFJ_RES_VER_PROV"));

		myResourceIdColumn = getTable().addColumn("RES_PID");
		myColumnSourceUri = getTable().addColumn("SOURCE_URI");
		myColumnRequestId = getTable().addColumn("REQUEST_ID");
	}

	@Override
	public DbColumn getResourceIdColumn() {
		return myResourceIdColumn;
	}

	public Condition createPredicateSourceUri(String theSourceUri) {
		return BinaryCondition.equalTo(myColumnSourceUri, generatePlaceholder(theSourceUri));
	}

	public Condition createPredicateMissingSourceUri() {
		return UnaryCondition.isNull(myColumnSourceUri);
	}

	public Condition createPredicateSourceUriWithModifiers(
			IQueryParameterType theQueryParameter, JpaStorageSettings theStorageSetting, String theSourceUri) {
		if (theQueryParameter.getMissing() != null && !theQueryParameter.getMissing()) {
			return UnaryCondition.isNotNull(myColumnSourceUri);
		} else if (theQueryParameter instanceof UriParam && theQueryParameter.getQueryParameterQualifier() != null) {
			UriParam uriParam = (UriParam) theQueryParameter;
			switch (uriParam.getQualifier()) {
				case ABOVE:
					return createPredicateSourceAbove(theSourceUri);
				case BELOW:
					return createPredicateSourceBelow(theSourceUri);
				case CONTAINS:
					return createPredicateSourceContains(theStorageSetting, theSourceUri);
				default:
					throw new InvalidRequestException(Msg.code(2418)
							+ String.format(
									"Unsupported qualifier specified, qualifier=%s",
									theQueryParameter.getQueryParameterQualifier()));
			}
		} else {
			return createPredicateSourceUri(theSourceUri);
		}
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
			throw new MethodNotAllowedException(Msg.code(2417) + ":contains modifier is disabled on this server");
		}
	}

	public Condition createPredicateRequestId(String theRequestId) {
		return BinaryCondition.equalTo(myColumnRequestId, generatePlaceholder(theRequestId));
	}
}
