package ca.uhn.fhir.jpa.dao.search;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.dao.predicate.SearchFilterParser;
import ca.uhn.fhir.jpa.dao.search.sql.NumberIndexTable;
import ca.uhn.fhir.jpa.model.entity.ResourceLink;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.param.NumberParam;
import com.healthmarketscience.sqlbuilder.ComboCondition;
import com.healthmarketscience.sqlbuilder.Condition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.From;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
@Scope("prototype")
public
class PredicateBuilderNumber2 extends BasePredicateBuilder2 implements IPredicateBuilder2 {
	private static final Logger ourLog = LoggerFactory.getLogger(PredicateBuilderNumber2.class);

	public PredicateBuilderNumber2(SearchBuilder2 theSearchBuilder) {
		super(theSearchBuilder);
	}

	@Override
	public Condition addPredicate(String theResourceName,
											RuntimeSearchParam theSearchParam,
											List<? extends IQueryParameterType> theList,
											SearchFilterParser.CompareOperation theOperation,
											From<?, ResourceLink> theLinkJoin, RequestPartitionId theRequestPartitionId) {

		NumberIndexTable join = getSqlBuilder().addNumberSelector();

		if (theList.get(0).getMissing() != null) {
			addPredicateParamMissingForNonReference(theResourceName, theSearchParam.getName(), theList.get(0).getMissing(), join, theRequestPartitionId);
			return null;
		}

		addPartitionIdPredicate(theRequestPartitionId, join, null);

		List<Condition> codePredicates = new ArrayList<>();
		for (IQueryParameterType nextOr : theList) {

			if (nextOr instanceof NumberParam) {
				NumberParam param = (NumberParam) nextOr;

				BigDecimal value = param.getValue();
				if (value == null) {
					continue;
				}

				Condition predicateNumeric = join.createPredicateNumeric(theResourceName, theSearchParam.getName(), join, myCriteriaBuilder, nextOr, theOperation, value, null, null, theRequestPartitionId);
				Condition predicateOuter = combineParamIndexPredicateWithParamNamePredicate(theResourceName, theSearchParam.getName(), join, predicateNumeric, theRequestPartitionId);
				codePredicates.add(predicateOuter);

			} else {
				throw new IllegalArgumentException("Invalid token type: " + nextOr.getClass());
			}

		}

		Condition predicate = ComboCondition.or(codePredicates.toArray(new Condition[0]));
		getSqlBuilder().addPredicate(predicate);
		return predicate;
	}
}
