package ca.uhn.fhir.jpa.search.builder.predicate;

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
import ca.uhn.fhir.jpa.dao.index.IdHelperService;
import ca.uhn.fhir.jpa.dao.predicate.SearchFilterParser;
import ca.uhn.fhir.jpa.search.builder.QueryStack;
import ca.uhn.fhir.jpa.search.builder.sql.SearchQueryBuilder;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.TokenParamModifier;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import com.healthmarketscience.sqlbuilder.Condition;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;
import org.hl7.fhir.r4.model.IdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class ResourceIdPredicateBuilder extends BasePredicateBuilder {
	private static final Logger ourLog = LoggerFactory.getLogger(ResourceIdPredicateBuilder.class);

	@Autowired
	private IIdHelperService myIdHelperService;

	/**
	 * Constructor
	 */
	public ResourceIdPredicateBuilder(SearchQueryBuilder theSearchSqlBuilder) {
		super(theSearchSqlBuilder);
	}


	@Nullable
	public Condition createPredicateResourceId(@Nullable DbColumn theSourceJoinColumn, String theResourceName, List<List<IQueryParameterType>> theValues, SearchFilterParser.CompareOperation theOperation, RequestPartitionId theRequestPartitionId) {

		Set<ResourcePersistentId> allOrPids = null;
		SearchFilterParser.CompareOperation defaultOperation = SearchFilterParser.CompareOperation.eq;

		boolean allIdsAreForcedIds = true;
		for (List<? extends IQueryParameterType> nextValue : theValues) {
			Set<ResourcePersistentId> orPids = new HashSet<>();
			boolean haveValue = false;
			for (IQueryParameterType next : nextValue) {
				String value = next.getValueAsQueryToken(getFhirContext());
				if (value != null && value.startsWith("|")) {
					value = value.substring(1);
				}

				IdType valueAsId = new IdType(value);
				if (isNotBlank(value)) {
					if (!myIdHelperService.idRequiresForcedId(valueAsId.getIdPart()) && allIdsAreForcedIds) {
						allIdsAreForcedIds = false;
					}
					haveValue = true;
					try {
						ResourcePersistentId pid = myIdHelperService.resolveResourcePersistentIds(theRequestPartitionId, theResourceName, valueAsId.getIdPart());
						orPids.add(pid);
					} catch (ResourceNotFoundException e) {
						// This is not an error in a search, it just results in no matches
						ourLog.debug("Resource ID {} was requested but does not exist", valueAsId.getIdPart());
					}
				}

				if (next instanceof TokenParam) {
					if (((TokenParam) next).getModifier() == TokenParamModifier.NOT) {
						defaultOperation = SearchFilterParser.CompareOperation.ne;
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

			setMatchNothing();

		} else if (allOrPids != null) {

			SearchFilterParser.CompareOperation operation = defaultIfNull(theOperation, defaultOperation);
			assert operation == SearchFilterParser.CompareOperation.eq || operation == SearchFilterParser.CompareOperation.ne;

			List<Long> resourceIds = ResourcePersistentId.toLongList(allOrPids);
			if (theSourceJoinColumn == null) {
				BaseJoiningPredicateBuilder queryRootTable = super.getOrCreateQueryRootTable(!allIdsAreForcedIds);
				Condition predicate;
				switch (operation) {
					default:
					case eq:
						predicate = queryRootTable.createPredicateResourceIds(false, resourceIds);
						return queryRootTable.combineWithRequestPartitionIdPredicate(theRequestPartitionId, predicate);
					case ne:
						predicate = queryRootTable.createPredicateResourceIds(true, resourceIds);
						return queryRootTable.combineWithRequestPartitionIdPredicate(theRequestPartitionId, predicate);
				}
			} else {
				return QueryStack.toEqualToOrInPredicate(theSourceJoinColumn, generatePlaceholders(resourceIds), operation == SearchFilterParser.CompareOperation.ne);
			}

		}

		return null;
	}


}
