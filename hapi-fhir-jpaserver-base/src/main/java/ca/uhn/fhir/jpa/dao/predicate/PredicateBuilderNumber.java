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
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamNumber;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.param.NumberParam;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

@Component
@Scope("prototype")
public class PredicateBuilderNumber extends BasePredicateBuilder implements IPredicateBuilder {
	private static final Logger ourLog = LoggerFactory.getLogger(PredicateBuilderNumber.class);

	public PredicateBuilderNumber(LegacySearchBuilder theSearchBuilder) {
		super(theSearchBuilder);
	}

	@Override
	public Predicate addPredicate(String theResourceName,
											RuntimeSearchParam theSearchParam,
											List<? extends IQueryParameterType> theList,
											SearchFilterParser.CompareOperation operation,
											RequestPartitionId theRequestPartitionId) {

		From<?, ResourceIndexedSearchParamNumber> join = myQueryStack.createJoin(SearchBuilderJoinEnum.NUMBER, theSearchParam.getName());

		if (theList.get(0).getMissing() != null) {
			addPredicateParamMissingForNonReference(theResourceName, theSearchParam.getName(), theList.get(0).getMissing(), join, theRequestPartitionId);
			return null;
		}

		List<Predicate> codePredicates = new ArrayList<>();
		addPartitionIdPredicate(theRequestPartitionId, join, codePredicates);

		for (IQueryParameterType nextOr : theList) {

			if (nextOr instanceof NumberParam) {
				NumberParam param = (NumberParam) nextOr;

				BigDecimal value = param.getValue();
				if (value == null) {
					continue;
				}

				final Expression<BigDecimal> fromObj = join.get("myValue");
				ParamPrefixEnum prefix = defaultIfNull(param.getPrefix(), ParamPrefixEnum.EQUAL);
				if (operation == SearchFilterParser.CompareOperation.ne) {
					prefix = ParamPrefixEnum.NOT_EQUAL;
				} else if (operation == SearchFilterParser.CompareOperation.lt) {
					prefix = ParamPrefixEnum.LESSTHAN;
				} else if (operation == SearchFilterParser.CompareOperation.le) {
					prefix = ParamPrefixEnum.LESSTHAN_OR_EQUALS;
				} else if (operation == SearchFilterParser.CompareOperation.gt) {
					prefix = ParamPrefixEnum.GREATERTHAN;
				} else if (operation == SearchFilterParser.CompareOperation.ge) {
					prefix = ParamPrefixEnum.GREATERTHAN_OR_EQUALS;
				} else if (operation == SearchFilterParser.CompareOperation.eq) {
					prefix = ParamPrefixEnum.EQUAL;
				} else if (operation != null) {
					throw new IllegalArgumentException(Msg.code(999) + "Invalid operator specified for number type");
				}


				String invalidMessageName = "invalidNumberPrefix";

				Predicate predicateNumeric = createPredicateNumeric(theResourceName, theSearchParam.getName(), join, myCriteriaBuilder, nextOr, prefix, value, fromObj, invalidMessageName, theRequestPartitionId);
				Predicate predicateOuter = combineParamIndexPredicateWithParamNamePredicate(theResourceName, theSearchParam.getName(), join, predicateNumeric, theRequestPartitionId);
				codePredicates.add(predicateOuter);

			} else {
				throw new IllegalArgumentException(Msg.code(1000) + "Invalid token type: " + nextOr.getClass());
			}

		}

		Predicate predicate = myCriteriaBuilder.or(toArray(codePredicates));
		myQueryStack.addPredicateWithImplicitTypeSelection(predicate);
		return predicate;
	}
}
