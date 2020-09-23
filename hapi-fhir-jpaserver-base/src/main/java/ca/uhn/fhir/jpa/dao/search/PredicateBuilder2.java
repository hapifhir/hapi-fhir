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
import ca.uhn.fhir.rest.api.server.RequestDetails;
import com.healthmarketscience.sqlbuilder.Condition;

import javax.persistence.criteria.From;
import java.util.List;

public class PredicateBuilder2 {

	public PredicateBuilder2(SearchBuilder2 theSearchBuilder) {
	}



//	void addLinkPredicateString(String theResourceName, RuntimeSearchParam theSearchParam, List<? extends IQueryParameterType> theNextAnd, From<?, ResourceLink> theLinkJoin, RequestPartitionId theRequestPartitionId) {
//		addLinkPredicateString(theResourceName, theSearchParam, theNextAnd, SearchFilterParser.CompareOperation.sw, theLinkJoin, theRequestPartitionId);
//	}
//

//	void addPredicateTag(List<List<IQueryParameterType>> theAndOrParams, String theParamName, RequestPartitionId theRequestPartitionId) {
//		myPredicateBuilderTag.addPredicateTag(theAndOrParams, theParamName, theRequestPartitionId);
//	}


//	public void searchForIdsWithAndOr(String theResourceName, String theNextParamName, List<List<IQueryParameterType>> theAndOrParams, RequestDetails theRequest, RequestPartitionId theRequestPartitionId) {
//		myPredicateBuilderReference.searchForIdsWithAndOr(theResourceName, theNextParamName, theAndOrParams, theRequest, theRequestPartitionId);
//	}


	public Condition addLinkPredicate(String theResourceName, RuntimeSearchParam theParamDef, List<IQueryParameterType> theOrValues, SearchFilterParser.CompareOperation theOperation, From<?, ResourceLink> theLinkJoin, RequestPartitionId theRequestPartitionId) {
		// FIXME: move
		throw new UnsupportedOperationException();
//
//		switch (theParamDef.getParamType()) {
//			case DATE:
//				return addLinkPredicateDate(theResourceName, theParamDef, theOrValues, theOperation, theLinkJoin, theRequestPartitionId);
//			case NUMBER:
//				return addLinkPredicateNumber(theResourceName, theParamDef, theOrValues, theOperation, theLinkJoin, theRequestPartitionId);
//			case QUANTITY:
//				return addLinkPredicateQuantity(theResourceName, theParamDef, theOrValues, theOperation, theLinkJoin, theRequestPartitionId);
//			case STRING:
//				return addLinkPredicateString(theResourceName, theParamDef, theOrValues, theOperation, theLinkJoin, theRequestPartitionId);
//			case TOKEN:
//					if ("Location.position".equals(theParamDef.getPath())) {
//						return addLinkPredicateCoords(theResourceName, theParamDef, theOrValues, theLinkJoin, theRequestPartitionId);
//					} else {
//						return addLinkPredicateToken(theResourceName, theParamDef, theOrValues, theOperation, theLinkJoin, theRequestPartitionId);
//					}
//			case URI:
//				return addLinkPredicateUri(theResourceName, theParamDef, theOrValues, theOperation, theLinkJoin, theRequestPartitionId);
//			default:
//				throw new UnsupportedOperationException("Chain search on type " + theParamDef.getParamType() +
//					" is not supported: " + theResourceName + "." + theParamDef.getName());
//		}
	}

	// FIXME: remove this class

//	public Condition addPredicateReference(String theResourceName, String theParamName, List<? extends IQueryParameterType> theSingletonList, SearchFilterParser.CompareOperation theOperation, RequestDetails theRequest, RequestPartitionId theRequestPartitionId) {
//		return myPredicateBuilderReference.addPredicate(theResourceName, theParamName, theSingletonList, theOperation, theRequest, theRequestPartitionId, theOperation);
//	}
//
}
