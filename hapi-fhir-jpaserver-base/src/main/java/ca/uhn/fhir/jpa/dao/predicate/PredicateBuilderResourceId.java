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

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.dao.LegacySearchBuilder;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.r4.model.IdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Component
@Scope("prototype")
public class PredicateBuilderResourceId extends BasePredicateBuilder {
	private static final Logger ourLog = LoggerFactory.getLogger(PredicateBuilderResourceId.class);

	@Autowired
	IIdHelperService myIdHelperService;

	public PredicateBuilderResourceId(LegacySearchBuilder theSearchBuilder) {
		super(theSearchBuilder);
	}

	@Nullable
	Predicate addPredicateResourceId(List<List<IQueryParameterType>> theValues, String theResourceName, SearchFilterParser.CompareOperation theOperation, RequestPartitionId theRequestPartitionId) {

		Predicate nextPredicate = createPredicate(theResourceName, theValues, theOperation, theRequestPartitionId);

		if (nextPredicate != null) {
			myQueryStack.addPredicate(nextPredicate);
			return nextPredicate;
		}

		return null;
	}

	@Nullable
	private Predicate createPredicate(String theResourceName, List<List<IQueryParameterType>> theValues, SearchFilterParser.CompareOperation theOperation, RequestPartitionId theRequestPartitionId) {
		Predicate nextPredicate = null;

		Set<ResourcePersistentId> allOrPids = null;

		for (List<? extends IQueryParameterType> nextValue : theValues) {
			Set<ResourcePersistentId> orPids = new HashSet<>();
			boolean haveValue = false;
			for (IQueryParameterType next : nextValue) {
				String value = next.getValueAsQueryToken(myContext);
				if (value != null && value.startsWith("|")) {
					value = value.substring(1);
				}

				IdType valueAsId = new IdType(value);
				if (isNotBlank(value)) {
					haveValue = true;
					try {
						ResourcePersistentId pid = myIdHelperService.resolveResourcePersistentIds(theRequestPartitionId, theResourceName, valueAsId.getIdPart());
						orPids.add(pid);
					} catch (ResourceNotFoundException e) {
						// This is not an error in a search, it just results in no matchesFhirResourceDaoR4InterceptorTest
						ourLog.debug("Resource ID {} was requested but does not exist", valueAsId.getIdPart());
					}
				}
			}
			if (haveValue) {
				if (allOrPids == null) {
					allOrPids = orPids;
				} else {
					allOrPids.retainAll(orPids);
				}

			}
		}

		if (allOrPids != null && allOrPids.isEmpty()) {

			// This will never match
			nextPredicate = myCriteriaBuilder.equal(myQueryStack.getResourcePidColumn(), -1);

		} else if (allOrPids != null) {

			SearchFilterParser.CompareOperation operation = defaultIfNull(theOperation, SearchFilterParser.CompareOperation.eq);
			assert operation == SearchFilterParser.CompareOperation.eq || operation == SearchFilterParser.CompareOperation.ne;
			List<Predicate> codePredicates = new ArrayList<>();
			switch (operation) {
				default:
				case eq:
					codePredicates.add(myQueryStack.getResourcePidColumn().in(ResourcePersistentId.toLongList(allOrPids)));
					nextPredicate = myCriteriaBuilder.and(toArray(codePredicates));
					break;
				case ne:
					codePredicates.add(myQueryStack.getResourcePidColumn().in(ResourcePersistentId.toLongList(allOrPids)).not());
					nextPredicate = myCriteriaBuilder.and(toArray(codePredicates));
					break;
			}

		}

		return nextPredicate;
	}

}
