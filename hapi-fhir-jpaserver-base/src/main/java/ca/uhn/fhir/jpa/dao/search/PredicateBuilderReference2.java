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

import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.dao.index.IdHelperService;
import ca.uhn.fhir.jpa.dao.predicate.SearchFilterParser;
import ca.uhn.fhir.jpa.dao.search.sql.ReferenceIndexTable;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamRegistry;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import com.healthmarketscience.sqlbuilder.Condition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope("prototype")
class PredicateBuilderReference2 extends BasePredicateBuilder2 {
	private static final Logger ourLog = LoggerFactory.getLogger(PredicateBuilderReference2.class);
	private final PredicateBuilder2 myPredicateBuilder;
	@Autowired
	IdHelperService myIdHelperService;
	@Autowired
	ISearchParamRegistry mySearchParamRegistry;
	@Autowired
	MatchUrlService myMatchUrlService;
	@Autowired
	DaoRegistry myDaoRegistry;
	@Autowired
	PartitionSettings myPartitionSettings;
	@Autowired
	private IInterceptorBroadcaster myInterceptorBroadcaster;

	PredicateBuilderReference2(SearchBuilder2 theSearchBuilder, PredicateBuilder2 thePredicateBuilder) {
		super(theSearchBuilder);
		myPredicateBuilder = thePredicateBuilder;
	}

	/**
	 * Add reference predicate to the current search
	 */

	public Condition addPredicate(String theResourceName,
											String theParamName,
											List<? extends IQueryParameterType> theList,
											SearchFilterParser.CompareOperation operation,
											RequestDetails theRequest,
											RequestPartitionId theRequestPartitionId,
											SearchFilterParser.CompareOperation theOperation) {

		// This just to ensure the chain has been split correctly
		assert theParamName.contains(".") == false;

		if ((operation != null) &&
			(operation != SearchFilterParser.CompareOperation.eq) &&
			(operation != SearchFilterParser.CompareOperation.ne)) {
			throw new InvalidRequestException("Invalid operator specified for reference predicate.  Supported operators for reference predicate are \"eq\" and \"ne\".");
		}

		if (theList.get(0).getMissing() != null) {
			addPredicateParamMissingForReference(theResourceName, theParamName, theList.get(0).getMissing(), theRequestPartitionId);
			return null;
		}

		ReferenceIndexTable join = getSqlBuilder().addReferenceSelector();
		return join.addPredicate(theRequest, theParamName, theList, theOperation, theRequestPartitionId);
	}


	// FIXME: remove
	public Object createResourceLinkPathPredicate(String theTargetResourceType, String theParamReference) {
		throw new UnsupportedOperationException();
	}
}
