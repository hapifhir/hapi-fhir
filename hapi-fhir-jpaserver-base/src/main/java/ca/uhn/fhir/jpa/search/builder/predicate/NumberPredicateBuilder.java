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
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.dao.LegacySearchBuilder;
import ca.uhn.fhir.jpa.dao.predicate.SearchFilterParser;
import ca.uhn.fhir.jpa.dao.predicate.SearchFuzzUtil;
import ca.uhn.fhir.jpa.search.builder.sql.SearchQueryBuilder;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.ComboCondition;
import com.healthmarketscience.sqlbuilder.Condition;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.math.MathContext;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

public class NumberPredicateBuilder extends BaseSearchParamPredicateBuilder {

	private static final Logger ourLog = LoggerFactory.getLogger(NumberPredicateBuilder.class);
	private final DbColumn myColumnValue;
	@Autowired
	private FhirContext myFhirContext;

	/**
	 * Constructor
	 */
	public NumberPredicateBuilder(SearchQueryBuilder theSearchSqlBuilder) {
		super(theSearchSqlBuilder, theSearchSqlBuilder.addTable("HFJ_SPIDX_NUMBER"));

		myColumnValue = getTable().addColumn("SP_VALUE");
	}

	public Condition createPredicateNumeric(String theResourceName, String theParamName, SearchFilterParser.CompareOperation theOperation, BigDecimal theValue, RequestPartitionId theRequestPartitionId, IQueryParameterType theActualParam) {
		Condition numericPredicate = createPredicateNumeric(this, theOperation, theValue, myColumnValue, "invalidNumberPrefix", myFhirContext, theActualParam);
		return combineWithHashIdentityPredicate(theResourceName, theParamName, numericPredicate);
	}

	public DbColumn getColumnValue() {
		return myColumnValue;
	}


	static Condition createPredicateNumeric(BaseSearchParamPredicateBuilder theIndexTable, SearchFilterParser.CompareOperation theOperation, BigDecimal theValue, DbColumn theColumn, String theInvalidValueKey, FhirContext theFhirContext, IQueryParameterType theActualParam) {
		Condition num;

		// Per discussions with Grahame Grieve and James Agnew on 11/13/19, modified logic for EQUAL and NOT_EQUAL operators below so as to
		//   use exact value matching.  The "fuzz amount" matching is still used with the APPROXIMATE operator.
		SearchFilterParser.CompareOperation operation = defaultIfNull(theOperation, SearchFilterParser.CompareOperation.eq);
		switch (operation) {
			case gt:
				num = BinaryCondition.greaterThan(theColumn, theIndexTable.generatePlaceholder(theValue));
				break;
			case ge:
				num = BinaryCondition.greaterThanOrEq(theColumn, theIndexTable.generatePlaceholder(theValue));
				break;
			case lt:
				num = BinaryCondition.lessThan(theColumn, theIndexTable.generatePlaceholder(theValue));
				break;
			case le:
				num = BinaryCondition.lessThanOrEq(theColumn, theIndexTable.generatePlaceholder(theValue));
				break;
			case eq:
				num = BinaryCondition.equalTo(theColumn, theIndexTable.generatePlaceholder(theValue));
				break;
			case ne:
				num = BinaryCondition.notEqualTo(theColumn, theIndexTable.generatePlaceholder(theValue));
				break;
			case ap:
				BigDecimal mul = SearchFuzzUtil.calculateFuzzAmount(ParamPrefixEnum.APPROXIMATE, theValue);
				BigDecimal low = theValue.subtract(mul, MathContext.DECIMAL64);
				BigDecimal high = theValue.add(mul, MathContext.DECIMAL64);
				Condition lowPred = BinaryCondition.greaterThanOrEq(theColumn, theIndexTable.generatePlaceholder(low));
				Condition highPred = BinaryCondition.lessThanOrEq(theColumn, theIndexTable.generatePlaceholder(high));
				num = ComboCondition.and(lowPred, highPred);
				ourLog.trace("Searching for {} <= val <= {}", low, high);
				break;
			default:
				String paramValue = theActualParam.getValueAsQueryToken(theFhirContext);
				String msg = theIndexTable.getFhirContext().getLocalizer().getMessage(LegacySearchBuilder.class, theInvalidValueKey, operation, paramValue);
				throw new InvalidRequestException(Msg.code(1235) + msg);
		}

		return num;
	}
}
