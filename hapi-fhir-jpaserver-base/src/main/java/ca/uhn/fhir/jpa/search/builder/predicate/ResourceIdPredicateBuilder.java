/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.search.builder.predicate;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.api.svc.ResolveIdentityMode;
import ca.uhn.fhir.jpa.dao.predicate.SearchFilterParser;
import ca.uhn.fhir.jpa.model.cross.IResourceLookup;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.search.builder.sql.ColumnTupleObject;
import ca.uhn.fhir.jpa.search.builder.sql.JpaPidValueTuples;
import ca.uhn.fhir.jpa.search.builder.sql.SearchQueryBuilder;
import ca.uhn.fhir.jpa.util.QueryParameterUtils;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.TokenParamModifier;
import com.healthmarketscience.sqlbuilder.Condition;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class ResourceIdPredicateBuilder extends BasePredicateBuilder {

	@Autowired
	private IIdHelperService<JpaPid> myIdHelperService;

	/**
	 * Constructor
	 */
	public ResourceIdPredicateBuilder(SearchQueryBuilder theSearchSqlBuilder) {
		super(theSearchSqlBuilder);
	}

	@Nullable
	public Condition createPredicateResourceId(
			@Nullable DbColumn[] theSourceJoinColumn,
			String theResourceName,
			List<List<IQueryParameterType>> theValues,
			SearchFilterParser.CompareOperation theOperation,
			RequestPartitionId theRequestPartitionId) {

		Set<JpaPid> allOrPids = null;
		SearchFilterParser.CompareOperation defaultOperation = SearchFilterParser.CompareOperation.eq;

		for (List<? extends IQueryParameterType> nextValue : theValues) {
			Set<IIdType> ids = new LinkedHashSet<>();
			boolean haveValue = false;
			for (IQueryParameterType next : nextValue) {
				String value = next.getValueAsQueryToken(getFhirContext());
				if (value != null && value.startsWith("|")) {
					value = value.substring(1);
				}

				if (isNotBlank(value)) {
					haveValue = true;
					if (!value.contains("/")) {
						value = theResourceName + "/" + value;
					}
					IIdType id = getFhirContext().getVersion().newIdType();
					id.setValue(value);
					ids.add(id);
				}

				if (next instanceof TokenParam) {
					if (((TokenParam) next).getModifier() == TokenParamModifier.NOT) {
						defaultOperation = SearchFilterParser.CompareOperation.ne;
					}
				}
			}

			Set<JpaPid> orPids = new HashSet<>();

			// We're joining this to a query that will explicitly ask for non-deleted,
			// so we really only want the PID and can safely cache (even if a previously
			// deleted status was cached, since it might now be undeleted)
			Map<IIdType, IResourceLookup<JpaPid>> resolvedPids = myIdHelperService.resolveResourceIdentities(
					theRequestPartitionId,
					ids,
					ResolveIdentityMode.includeDeleted().cacheOk());
			for (IResourceLookup<JpaPid> lookup : resolvedPids.values()) {
				orPids.add(lookup.getPersistentId());
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
			assert operation == SearchFilterParser.CompareOperation.eq
					|| operation == SearchFilterParser.CompareOperation.ne;

			if (theSourceJoinColumn == null) {
				BaseJoiningPredicateBuilder queryRootTable = super.getOrCreateQueryRootTable(true);
				Condition predicate;
				switch (operation) {
					default:
					case eq:
						predicate = queryRootTable.createPredicateResourceIds(false, allOrPids);
						break;
					case ne:
						predicate = queryRootTable.createPredicateResourceIds(true, allOrPids);
						break;
				}
				predicate = queryRootTable.combineWithRequestPartitionIdPredicate(theRequestPartitionId, predicate);
				return predicate;
			} else {
				if (getSearchQueryBuilder().isIncludePartitionIdInJoins()) {
					ColumnTupleObject left = new ColumnTupleObject(theSourceJoinColumn);
					JpaPidValueTuples right = JpaPidValueTuples.from(getSearchQueryBuilder(), allOrPids);
					return QueryParameterUtils.toInPredicate(
							left, right, operation == SearchFilterParser.CompareOperation.ne);
				} else {
					DbColumn resIdColumn = getResourceIdColumn(theSourceJoinColumn);
					List<Long> resourceIds = JpaPid.toLongList(allOrPids);
					return QueryParameterUtils.toEqualToOrInPredicate(
							resIdColumn,
							generatePlaceholders(resourceIds),
							operation == SearchFilterParser.CompareOperation.ne);
				}
			}
		}

		return null;
	}

	/**
	 * This method takes 1-2 columns and returns the last one. This is useful where the input is an array of
	 * join columns for SQL Search expressions. In partition key mode, there are 2 columns (partition id and resource id).
	 * In non partition key mode, only the resource id column is used.
	 */
	@Nullable
	public static DbColumn getResourceIdColumn(@Nullable DbColumn[] theJoinColumns) {
		DbColumn resIdColumn;
		if (theJoinColumns == null) {
			return null;
		} else if (theJoinColumns.length == 1) {
			resIdColumn = theJoinColumns[0];
		} else {
			assert theJoinColumns.length == 2;
			resIdColumn = theJoinColumns[1];
		}
		return resIdColumn;
	}
}
