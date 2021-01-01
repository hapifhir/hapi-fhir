package ca.uhn.fhir.jpa.search.builder.predicate;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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
import ca.uhn.fhir.jpa.search.builder.QueryStack;
import ca.uhn.fhir.jpa.model.entity.BaseResourceIndexedSearchParam;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamQuantity;
import ca.uhn.fhir.jpa.search.builder.sql.SearchQueryBuilder;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.base.composite.BaseQuantityDt;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import ca.uhn.fhir.rest.param.QuantityParam;
import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.ComboCondition;
import com.healthmarketscience.sqlbuilder.Condition;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.criteria.CriteriaBuilder;
import java.math.BigDecimal;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.StringUtils.isBlank;

public class QuantityPredicateBuilder extends BaseSearchParamPredicateBuilder {

	private final DbColumn myColumnHashIdentitySystemUnits;
	private final DbColumn myColumnHashIdentityUnits;
	private final DbColumn myColumnValue;
	@Autowired
	private FhirContext myFhirContext;

	/**
	 * Constructor
	 */
	public QuantityPredicateBuilder(SearchQueryBuilder theSearchSqlBuilder) {
		super(theSearchSqlBuilder, theSearchSqlBuilder.addTable("HFJ_SPIDX_QUANTITY"));

		myColumnHashIdentitySystemUnits = getTable().addColumn("HASH_IDENTITY_SYS_UNITS");
		myColumnHashIdentityUnits = getTable().addColumn("HASH_IDENTITY_AND_UNITS");
		myColumnValue = getTable().addColumn("SP_VALUE");
	}


	public Condition createPredicateQuantity(IQueryParameterType theParam, String theResourceName, String theParamName, CriteriaBuilder theBuilder, QuantityPredicateBuilder theFrom, SearchFilterParser.CompareOperation theOperation, RequestPartitionId theRequestPartitionId) {

		String systemValue;
		String unitsValue;
		ParamPrefixEnum cmpValue;
		BigDecimal valueValue;

		if (theParam instanceof BaseQuantityDt) {
			BaseQuantityDt param = (BaseQuantityDt) theParam;
			systemValue = param.getSystemElement().getValueAsString();
			unitsValue = param.getUnitsElement().getValueAsString();
			cmpValue = ParamPrefixEnum.forValue(param.getComparatorElement().getValueAsString());
			valueValue = param.getValueElement().getValue();
		} else if (theParam instanceof QuantityParam) {
			QuantityParam param = (QuantityParam) theParam;
			systemValue = param.getSystem();
			unitsValue = param.getUnits();
			cmpValue = param.getPrefix();
			valueValue = param.getValue();
		} else {
			throw new IllegalArgumentException("Invalid quantity type: " + theParam.getClass());
		}

		Condition hashPredicate;
		if (!isBlank(systemValue) && !isBlank(unitsValue)) {
			long hash = ResourceIndexedSearchParamQuantity.calculateHashSystemAndUnits(getPartitionSettings(), theRequestPartitionId, theResourceName, theParamName, systemValue, unitsValue);
			hashPredicate = BinaryCondition.equalTo(myColumnHashIdentitySystemUnits, generatePlaceholder(hash));
		} else if (!isBlank(unitsValue)) {
			long hash = ResourceIndexedSearchParamQuantity.calculateHashUnits(getPartitionSettings(), theRequestPartitionId, theResourceName, theParamName, unitsValue);
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
