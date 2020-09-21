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
import ca.uhn.fhir.jpa.dao.search.sql.QuantityIndexTable;
import ca.uhn.fhir.jpa.model.entity.ResourceLink;
import ca.uhn.fhir.model.api.IQueryParameterType;
import com.healthmarketscience.sqlbuilder.ComboCondition;
import com.healthmarketscience.sqlbuilder.Condition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import java.util.ArrayList;
import java.util.List;

@Component
@Scope("prototype")
public
class PredicateBuilderQuantity2 extends BasePredicateBuilder2 implements IPredicateBuilder2 {

	PredicateBuilderQuantity2(SearchBuilder2 theSearchBuilder) {
		super(theSearchBuilder);
	}

	@Override
	public Condition addPredicate(String theResourceName,
											RuntimeSearchParam theSearchParam,
											List<? extends IQueryParameterType> theList,
											SearchFilterParser.CompareOperation theOperation,
											From<?, ResourceLink> theLinkJoin,
											RequestPartitionId theRequestPartitionId) {

		QuantityIndexTable join = getSqlBuilder().addQuantity();

		if (theList.get(0).getMissing() != null) {
			addPredicateParamMissingForNonReference(theResourceName, theSearchParam.getName(), theList.get(0).getMissing(), join, theRequestPartitionId);
			return null;
		}

		addPartitionIdPredicate(theRequestPartitionId, join, null);

		List<Condition> codePredicates = new ArrayList<>();
		for (IQueryParameterType nextOr : theList) {
			Condition singleCode = createPredicateQuantity(nextOr, theResourceName, theSearchParam.getName(), myCriteriaBuilder, join, theOperation, theRequestPartitionId);
			codePredicates.add(singleCode);
		}

		Condition retVal = ComboCondition.or(codePredicates.toArray(new Condition[0]));

		getSqlBuilder().addPredicate(retVal);

		return retVal;
	}

	public Condition createPredicateQuantity(IQueryParameterType theParam,
														  String theResourceName,
														  String theParamName,
														  CriteriaBuilder theBuilder,
														  QuantityIndexTable theFrom,
														  SearchFilterParser.CompareOperation theOperation,
														  RequestPartitionId theRequestPartitionId) {
		return theFrom.createPredicateQuantity(theParam,
			theResourceName,
			theParamName,
			theBuilder,
			theFrom,
			theOperation,
			theRequestPartitionId);
	}



}
