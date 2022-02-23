package ca.uhn.fhir.jpa.search.builder.predicate;

/*
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.dao.predicate.SearchFilterParser;
import ca.uhn.fhir.jpa.model.entity.BaseResourceIndexedSearchParam;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamBaseQuantity;
import ca.uhn.fhir.jpa.search.builder.QueryStack;
import ca.uhn.fhir.jpa.search.builder.sql.SearchQueryBuilder;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.base.composite.BaseQuantityDt;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import ca.uhn.fhir.rest.param.QuantityParam;
import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.ComboCondition;
import com.healthmarketscience.sqlbuilder.Condition;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbTable;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.criteria.CriteriaBuilder;
import java.math.BigDecimal;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.StringUtils.isBlank;


public abstract class QuantityBasePredicateBuilder extends BaseSearchParamPredicateBuilder {

	protected DbColumn myColumnHashIdentitySystemUnits;
	protected DbColumn myColumnHashIdentityUnits;
	protected DbColumn myColumnValue;

	@Autowired
	private FhirContext myFhirContext;

	/**
	 * Constructor
	 */
	public QuantityBasePredicateBuilder(SearchQueryBuilder theSearchSqlBuilder, DbTable theTable) {
		super(theSearchSqlBuilder, theTable);
	}

	public Condition createPredicateQuantity(QuantityParam theParam, String theResourceName, String theParamName, CriteriaBuilder theBuilder, QuantityBasePredicateBuilder theFrom, SearchFilterParser.CompareOperation theOperation, RequestPartitionId theRequestPartitionId) {

		String systemValue = theParam.getSystem();
		String unitsValue = theParam.getUnits();
		ParamPrefixEnum cmpValue = theParam.getPrefix();
		BigDecimal valueValue = theParam.getValue();

		Condition hashPredicate;
		if (!isBlank(systemValue) && !isBlank(unitsValue)) {
			long hash = ResourceIndexedSearchParamBaseQuantity.calculateHashSystemAndUnits(getPartitionSettings(), theRequestPartitionId, theResourceName, theParamName, systemValue, unitsValue);
			hashPredicate = BinaryCondition.equalTo(myColumnHashIdentitySystemUnits, generatePlaceholder(hash));
		} else if (!isBlank(unitsValue)) {
			long hash = ResourceIndexedSearchParamBaseQuantity.calculateHashUnits(getPartitionSettings(), theRequestPartitionId, theResourceName, theParamName, unitsValue);
			hashPredicate = BinaryCondition.equalTo(myColumnHashIdentityUnits, generatePlaceholder(hash));
		} else {
			long hash = BaseResourceIndexedSearchParam.calculateHashIdentity(getPartitionSettings(), theRequestPartitionId, theResourceName, theParamName);
			hashPredicate = BinaryCondition.equalTo(getColumnHashIdentity(), generatePlaceholder(hash));
		}

		SearchFilterParser.CompareOperation operation = theOperation;
		if (operation == null && cmpValue != null) {
			operation = QueryStack.toOperation(cmpValue);
		}
		operation = defaultIfNull(operation, SearchFilterParser.CompareOperation.eq);
		Condition numericPredicate = NumberPredicateBuilder.createPredicateNumeric(this, operation, valueValue, myColumnValue, "invalidQuantityPrefix", myFhirContext, theParam);

		return ComboCondition.and(hashPredicate, numericPredicate);
	}

	public DbColumn getColumnValue() {
		return myColumnValue;
	}

}
