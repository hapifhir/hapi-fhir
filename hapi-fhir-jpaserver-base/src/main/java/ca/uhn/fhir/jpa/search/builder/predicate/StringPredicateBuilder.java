package ca.uhn.fhir.jpa.search.builder.predicate;

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
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.dao.predicate.SearchFilterParser;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamString;
import ca.uhn.fhir.jpa.search.builder.sql.SearchQueryBuilder;
import ca.uhn.fhir.jpa.search.builder.QueryStack;
import ca.uhn.fhir.model.api.IPrimitiveDatatype;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.MethodNotAllowedException;
import ca.uhn.fhir.util.StringUtil;
import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.ComboCondition;
import com.healthmarketscience.sqlbuilder.Condition;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;

public class StringPredicateBuilder extends BaseSearchParamPredicateBuilder {

	private final DbColumn myColumnResId;
	private final DbColumn myColumnValueExact;
	private final DbColumn myColumnValueNormalized;
	private final DbColumn myColumnHashNormPrefix;
	private final DbColumn myColumnHashIdentity;
	private final DbColumn myColumnHashExact;
	@Autowired
	private DaoConfig myDaoConfig;

	/**
	 * Constructor
	 */
	public StringPredicateBuilder(SearchQueryBuilder theSearchSqlBuilder) {
		super(theSearchSqlBuilder, theSearchSqlBuilder.addTable("HFJ_SPIDX_STRING"));
		myColumnResId = getTable().addColumn("RES_ID");
		myColumnValueExact = getTable().addColumn("SP_VALUE_EXACT");
		myColumnValueNormalized = getTable().addColumn("SP_VALUE_NORMALIZED");
		myColumnHashNormPrefix = getTable().addColumn("HASH_NORM_PREFIX");
		myColumnHashIdentity = getTable().addColumn("HASH_IDENTITY");
		myColumnHashExact = getTable().addColumn("HASH_EXACT");
	}

	public DbColumn getColumnValueNormalized() {
		return myColumnValueNormalized;
	}

	@Override
	public DbColumn getResourceIdColumn() {
		return myColumnResId;
	}

	public Condition createPredicateString(IQueryParameterType theParameter,
														String theResourceName,
														String theSpnamePrefix,
														RuntimeSearchParam theSearchParam,
														StringPredicateBuilder theFrom,
														SearchFilterParser.CompareOperation operation) {
		String rawSearchTerm;
		String paramName = QueryStack.getParamNameWithPrefix(theSpnamePrefix, theSearchParam.getName());
		
		if (theParameter instanceof TokenParam) {
			TokenParam id = (TokenParam) theParameter;
			if (!id.isText()) {
				throw new IllegalStateException(Msg.code(1257) + "Trying to process a text search on a non-text token parameter");
			}
			rawSearchTerm = id.getValue();
		} else if (theParameter instanceof StringParam) {
			StringParam id = (StringParam) theParameter;
			rawSearchTerm = id.getValue();
			if (id.isContains()) {
				if (!myDaoConfig.isAllowContainsSearches()) {
					throw new MethodNotAllowedException(Msg.code(1258) + ":contains modifier is disabled on this server");
				}
			} else {
				rawSearchTerm = theSearchParam.encode(rawSearchTerm);
			}
		} else if (theParameter instanceof IPrimitiveDatatype<?>) {
			IPrimitiveDatatype<?> id = (IPrimitiveDatatype<?>) theParameter;
			rawSearchTerm = id.getValueAsString();
		} else {
			throw new IllegalArgumentException(Msg.code(1259) + "Invalid token type: " + theParameter.getClass());
		}

		if (rawSearchTerm.length() > ResourceIndexedSearchParamString.MAX_LENGTH) {
			throw new InvalidRequestException(Msg.code(1260) + "Parameter[" + paramName + "] has length (" + rawSearchTerm.length() + ") that is longer than maximum allowed ("
				+ ResourceIndexedSearchParamString.MAX_LENGTH + "): " + rawSearchTerm);
		}

		boolean exactMatch = theParameter instanceof StringParam && ((StringParam) theParameter).isExact();
		if (exactMatch) {
			// Exact match
			return theFrom.createPredicateExact(theResourceName, paramName, rawSearchTerm);
		} else {
			// Normalized Match
			String normalizedString = StringUtil.normalizeStringForSearchIndexing(rawSearchTerm);
			String likeExpression;
			if ((theParameter instanceof StringParam) &&
				(((((StringParam) theParameter).isContains()) &&
					(myDaoConfig.isAllowContainsSearches())) ||
					(operation == SearchFilterParser.CompareOperation.co))) {
				likeExpression = createLeftAndRightMatchLikeExpression(normalizedString);
			} else if ((operation != SearchFilterParser.CompareOperation.ne) &&
				(operation != SearchFilterParser.CompareOperation.gt) &&
				(operation != SearchFilterParser.CompareOperation.lt) &&
				(operation != SearchFilterParser.CompareOperation.ge) &&
				(operation != SearchFilterParser.CompareOperation.le)) {
				if (operation == SearchFilterParser.CompareOperation.ew) {
					likeExpression = createRightMatchLikeExpression(normalizedString);
				} else {
					likeExpression = createLeftMatchLikeExpression(normalizedString);
				}
			} else {
				likeExpression = normalizedString;
			}

			Condition predicate;
			if ((operation == null) ||
				(operation == SearchFilterParser.CompareOperation.sw)) {
				predicate = theFrom.createPredicateNormalLike(theResourceName, paramName, normalizedString, likeExpression);
			} else if ((operation == SearchFilterParser.CompareOperation.ew) || (operation == SearchFilterParser.CompareOperation.co)) {
				predicate = theFrom.createPredicateLikeExpressionOnly(theResourceName, paramName, likeExpression, false);
			} else if (operation == SearchFilterParser.CompareOperation.eq) {
				predicate = theFrom.createPredicateNormal(theResourceName, paramName, normalizedString);
			} else if (operation == SearchFilterParser.CompareOperation.ne) {
				predicate = theFrom.createPredicateLikeExpressionOnly(theResourceName, paramName, likeExpression, true);
			} else if (operation == SearchFilterParser.CompareOperation.gt) {
				predicate = theFrom.createPredicateNormalGreaterThan(theResourceName, paramName, likeExpression);
			} else if (operation == SearchFilterParser.CompareOperation.ge) {
				predicate = theFrom.createPredicateNormalGreaterThanOrEqual(theResourceName, paramName, likeExpression);
			} else if (operation == SearchFilterParser.CompareOperation.lt) {
				predicate = theFrom.createPredicateNormalLessThan(theResourceName, paramName, likeExpression);
			} else if (operation == SearchFilterParser.CompareOperation.le) {
				predicate = theFrom.createPredicateNormalLessThanOrEqual(theResourceName, paramName, likeExpression);
			} else {
				throw new IllegalArgumentException(Msg.code(1261) + "Don't yet know how to handle operation " + operation + " on a string");
			}

			return predicate;
		}
	}

	@Nonnull
	public Condition createPredicateExact(String theResourceType, String theParamName, String theTheValueExact) {
		long hash = ResourceIndexedSearchParamString.calculateHashExact(getPartitionSettings(), getRequestPartitionId(), theResourceType, theParamName, theTheValueExact);
		String placeholderValue = generatePlaceholder(hash);
		return BinaryCondition.equalTo(myColumnHashExact, placeholderValue);
	}

	@Nonnull
	public Condition createPredicateNormalLike(String theResourceType, String theParamName, String theNormalizedString, String theLikeExpression) {
		Long hash = ResourceIndexedSearchParamString.calculateHashNormalized(getPartitionSettings(), getRequestPartitionId(), getModelConfig(), theResourceType, theParamName, theNormalizedString);
		Condition hashPredicate = BinaryCondition.equalTo(myColumnHashNormPrefix, generatePlaceholder(hash));
		Condition valuePredicate = BinaryCondition.like(myColumnValueNormalized, generatePlaceholder(theLikeExpression));
		return ComboCondition.and(hashPredicate, valuePredicate);
	}

	@Nonnull
	public Condition createPredicateNormal(String theResourceType, String theParamName, String theNormalizedString) {
		Long hash = ResourceIndexedSearchParamString.calculateHashNormalized(getPartitionSettings(), getRequestPartitionId(), getModelConfig(), theResourceType, theParamName, theNormalizedString);
		Condition hashPredicate = BinaryCondition.equalTo(myColumnHashNormPrefix, generatePlaceholder(hash));
		Condition valuePredicate = BinaryCondition.equalTo(myColumnValueNormalized, generatePlaceholder(theNormalizedString));
		return ComboCondition.and(hashPredicate, valuePredicate);
	}

	private Condition createPredicateNormalGreaterThanOrEqual(String theResourceType, String theParamName, String theNormalizedString) {
		Condition hashPredicate = createHashIdentityPredicate(theResourceType, theParamName);
		Condition valuePredicate = BinaryCondition.greaterThanOrEq(myColumnValueNormalized, generatePlaceholder(theNormalizedString));
		return ComboCondition.and(hashPredicate, valuePredicate);
	}

	private Condition createPredicateNormalGreaterThan(String theResourceType, String theParamName, String theNormalizedString) {
		Condition hashPredicate = createHashIdentityPredicate(theResourceType, theParamName);
		Condition valuePredicate = BinaryCondition.greaterThan(myColumnValueNormalized, generatePlaceholder(theNormalizedString));
		return ComboCondition.and(hashPredicate, valuePredicate);
	}

	private Condition createPredicateNormalLessThanOrEqual(String theResourceType, String theParamName, String theNormalizedString) {
		Condition hashPredicate = createHashIdentityPredicate(theResourceType, theParamName);
		Condition valuePredicate = BinaryCondition.lessThanOrEq(myColumnValueNormalized, generatePlaceholder(theNormalizedString));
		return ComboCondition.and(hashPredicate, valuePredicate);
	}

	private Condition createPredicateNormalLessThan(String theResourceType, String theParamName, String theNormalizedString) {
		Condition hashPredicate = createHashIdentityPredicate(theResourceType, theParamName);
		Condition valuePredicate = BinaryCondition.lessThan(myColumnValueNormalized, generatePlaceholder(theNormalizedString));
		return ComboCondition.and(hashPredicate, valuePredicate);
	}

	@Nonnull
	public Condition createPredicateLikeExpressionOnly(String theResourceType, String theParamName, String theLikeExpression, boolean theInverse) {
		long hashIdentity = ResourceIndexedSearchParamString.calculateHashIdentity(getPartitionSettings(), getRequestPartitionId(), theResourceType, theParamName);
		BinaryCondition identityPredicate = BinaryCondition.equalTo(myColumnHashIdentity, generatePlaceholder(hashIdentity));
		BinaryCondition likePredicate;
		if (theInverse) {
			likePredicate = BinaryCondition.notLike(myColumnValueNormalized, generatePlaceholder(theLikeExpression));
		} else {
			likePredicate = BinaryCondition.like(myColumnValueNormalized, generatePlaceholder(theLikeExpression));
		}
		return ComboCondition.and(identityPredicate, likePredicate);
	}

	public static String createLeftAndRightMatchLikeExpression(String likeExpression) {
		return "%" + likeExpression.replace("%", "\\%") + "%";
	}

	public static String createLeftMatchLikeExpression(String likeExpression) {
		return likeExpression.replace("%", "\\%") + "%";
	}

	public static String createRightMatchLikeExpression(String likeExpression) {
		return "%" + likeExpression.replace("%", "\\%");
	}


}
