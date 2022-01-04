package ca.uhn.fhir.jpa.dao.predicate;

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
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.dao.LegacySearchBuilder;
import ca.uhn.fhir.jpa.model.entity.BaseResourceIndexedSearchParam;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamQuantity;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.base.composite.BaseQuantityDt;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import ca.uhn.fhir.rest.param.QuantityParam;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Component
@Scope("prototype")
public class PredicateBuilderQuantity extends BasePredicateBuilder implements IPredicateBuilder {

	public PredicateBuilderQuantity(LegacySearchBuilder theSearchBuilder) {
		super(theSearchBuilder);
	}

	@Override
	public Predicate addPredicate(String theResourceName,
											RuntimeSearchParam theSearchParam,
											List<? extends IQueryParameterType> theList,
											SearchFilterParser.CompareOperation theOperation,
											RequestPartitionId theRequestPartitionId) {

		From<?, ResourceIndexedSearchParamQuantity> join = myQueryStack.createJoin(SearchBuilderJoinEnum.QUANTITY, theSearchParam.getName());

		if (theList.get(0).getMissing() != null) {
			addPredicateParamMissingForNonReference(theResourceName, theSearchParam.getName(), theList.get(0).getMissing(), join, theRequestPartitionId);
			return null;
		}

		List<Predicate> codePredicates = new ArrayList<>();
		addPartitionIdPredicate(theRequestPartitionId, join, codePredicates);

		for (IQueryParameterType nextOr : theList) {
			Predicate singleCode = createPredicateQuantity(nextOr, theResourceName, theSearchParam.getName(), myCriteriaBuilder, join, theOperation, theRequestPartitionId);
			codePredicates.add(singleCode);
		}

		Predicate retVal = myCriteriaBuilder.or(toArray(codePredicates));
		myQueryStack.addPredicateWithImplicitTypeSelection(retVal);
		return retVal;
	}

	public Predicate createPredicateQuantity(IQueryParameterType theParam,
														  String theResourceName,
														  String theParamName,
														  CriteriaBuilder theBuilder,
														  From<?, ResourceIndexedSearchParamQuantity> theFrom,
														  RequestPartitionId theRequestPartitionId) {
		return createPredicateQuantity(theParam,
			theResourceName,
			theParamName,
			theBuilder,
			theFrom,
			null,
                theRequestPartitionId);
	}

	private Predicate createPredicateQuantity(IQueryParameterType theParam,
															String theResourceName,
															String theParamName,
															CriteriaBuilder theBuilder,
															From<?, ResourceIndexedSearchParamQuantity> theFrom,
															SearchFilterParser.CompareOperation operation,
															RequestPartitionId theRequestPartitionId) {
		String systemValue;
		String unitsValue;
		ParamPrefixEnum cmpValue = null;
		BigDecimal valueValue;

		if (operation == SearchFilterParser.CompareOperation.ne) {
			cmpValue = ParamPrefixEnum.NOT_EQUAL;
		} else if (operation == SearchFilterParser.CompareOperation.lt) {
			cmpValue = ParamPrefixEnum.LESSTHAN;
		} else if (operation == SearchFilterParser.CompareOperation.le) {
			cmpValue = ParamPrefixEnum.LESSTHAN_OR_EQUALS;
		} else if (operation == SearchFilterParser.CompareOperation.gt) {
			cmpValue = ParamPrefixEnum.GREATERTHAN;
		} else if (operation == SearchFilterParser.CompareOperation.ge) {
			cmpValue = ParamPrefixEnum.GREATERTHAN_OR_EQUALS;
		} else if (operation == SearchFilterParser.CompareOperation.eq) {
			cmpValue = ParamPrefixEnum.EQUAL;
		} else if (operation != null) {
			throw new IllegalArgumentException(Msg.code(1045) + "Invalid operator specified for quantity type");
		}

		if (theParam instanceof BaseQuantityDt) {
			BaseQuantityDt param = (BaseQuantityDt) theParam;
			systemValue = param.getSystemElement().getValueAsString();
			unitsValue = param.getUnitsElement().getValueAsString();
			if (operation == null) {
				cmpValue = ParamPrefixEnum.forValue(param.getComparatorElement().getValueAsString());
			}
			valueValue = param.getValueElement().getValue();
		} else if (theParam instanceof QuantityParam) {
			QuantityParam param = (QuantityParam) theParam;
			systemValue = param.getSystem();
			unitsValue = param.getUnits();
			if (operation == null) {
				cmpValue = param.getPrefix();
			}
			valueValue = param.getValue();
		} else {
			throw new IllegalArgumentException(Msg.code(1046) + "Invalid quantity type: " + theParam.getClass());
		}

		if (myDontUseHashesForSearch) {
			Predicate system = null;
			if (!isBlank(systemValue)) {
				system = theBuilder.equal(theFrom.get("mySystem"), systemValue);
			}

			Predicate code = null;
			if (!isBlank(unitsValue)) {
				code = theBuilder.equal(theFrom.get("myUnits"), unitsValue);
			}

			cmpValue = defaultIfNull(cmpValue, ParamPrefixEnum.EQUAL);
			final Expression<BigDecimal> path = theFrom.get("myValue");
			String invalidMessageName = "invalidQuantityPrefix";

			Predicate num = createPredicateNumeric(theResourceName, null, theFrom, theBuilder, theParam, cmpValue, valueValue, path, invalidMessageName, theRequestPartitionId);

			Predicate singleCode;
			if (system == null && code == null) {
				singleCode = num;
			} else if (system == null) {
				singleCode = theBuilder.and(code, num);
			} else if (code == null) {
				singleCode = theBuilder.and(system, num);
			} else {
				singleCode = theBuilder.and(system, code, num);
			}

			return combineParamIndexPredicateWithParamNamePredicate(theResourceName, theParamName, theFrom, singleCode, theRequestPartitionId);
		}

		Predicate hashPredicate;
		if (!isBlank(systemValue) && !isBlank(unitsValue)) {
			long hash = ResourceIndexedSearchParamQuantity.calculateHashSystemAndUnits(getPartitionSettings(), theRequestPartitionId, theResourceName, theParamName, systemValue, unitsValue);
			hashPredicate = myCriteriaBuilder.equal(theFrom.get("myHashIdentitySystemAndUnits"), hash);
		} else if (!isBlank(unitsValue)) {
			long hash = ResourceIndexedSearchParamQuantity.calculateHashUnits(getPartitionSettings(), theRequestPartitionId, theResourceName, theParamName, unitsValue);
			hashPredicate = myCriteriaBuilder.equal(theFrom.get("myHashIdentityAndUnits"), hash);
		} else {
			long hash = BaseResourceIndexedSearchParam.calculateHashIdentity(getPartitionSettings(), theRequestPartitionId, theResourceName, theParamName);
			hashPredicate = myCriteriaBuilder.equal(theFrom.get("myHashIdentity"), hash);
		}

		cmpValue = defaultIfNull(cmpValue, ParamPrefixEnum.EQUAL);
		final Expression<BigDecimal> path = theFrom.get("myValue");
		String invalidMessageName = "invalidQuantityPrefix";

		Predicate numericPredicate = createPredicateNumeric(theResourceName, null, theFrom, theBuilder, theParam, cmpValue, valueValue, path, invalidMessageName, theRequestPartitionId);

		return theBuilder.and(hashPredicate, numericPredicate);
	}


}
