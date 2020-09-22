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

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.dao.index.IdHelperService;
import ca.uhn.fhir.jpa.dao.predicate.SearchFilterParser;
import ca.uhn.fhir.jpa.dao.search.sql.BaseIndexTable;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import com.healthmarketscience.sqlbuilder.Condition;
import org.hl7.fhir.r4.model.IdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Component
@Scope("prototype")
class PredicateBuilderResourceId2 extends BasePredicateBuilder2 {
	private static final Logger ourLog = LoggerFactory.getLogger(PredicateBuilderResourceId2.class);

	@Autowired
	IdHelperService myIdHelperService;

	PredicateBuilderResourceId2(SearchBuilder2 theSearchBuilder) {
		super(theSearchBuilder);
	}

	@Nullable
	Condition addPredicateResourceId(List<List<IQueryParameterType>> theValues, String theResourceName, SearchFilterParser.CompareOperation theOperation, RequestPartitionId theRequestPartitionId) {

		Condition predicate = createPredicate(theResourceName, theValues, theOperation, theRequestPartitionId);

		if (predicate != null) {
			getSqlBuilder().addPredicate(predicate);
		}

		return predicate;
	}

	@Nullable
	private Condition createPredicate(String theResourceName, List<List<IQueryParameterType>> theValues, SearchFilterParser.CompareOperation theOperation, RequestPartitionId theRequestPartitionId) {

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
						// This is not an error in a search, it just results in no matches
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

			getSqlBuilder().setMatchNothing();

		} else if (allOrPids != null) {

			SearchFilterParser.CompareOperation operation = defaultIfNull(theOperation, SearchFilterParser.CompareOperation.eq);
			assert operation == SearchFilterParser.CompareOperation.eq || operation == SearchFilterParser.CompareOperation.ne;

			BaseIndexTable queryRootTable = getSqlBuilder().getOrCreateQueryRootTable();

			switch (operation) {
				default:
				case eq:
					return queryRootTable.createPredicateResourceIds(false, ResourcePersistentId.toLongList(allOrPids));
				case ne:
					return queryRootTable.createPredicateResourceIds(true, ResourcePersistentId.toLongList(allOrPids));
			}

		}

		return null;
	}

}
