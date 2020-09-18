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
import ca.uhn.fhir.jpa.model.entity.ResourceLink;
import ca.uhn.fhir.model.api.IQueryParameterType;
import com.healthmarketscience.sqlbuilder.Condition;

import javax.annotation.Nullable;
import javax.persistence.criteria.From;
import java.util.List;

public interface IPredicateBuilder2 {
	@Nullable
	Condition addPredicate(String theResourceName,
								  RuntimeSearchParam theSearchParam,
								  List<? extends IQueryParameterType> theList,
								  SearchFilterParser.CompareOperation theOperation,
								  From<?, ResourceLink> theLinkJoin, RequestPartitionId theRequestPartitionId);
}
