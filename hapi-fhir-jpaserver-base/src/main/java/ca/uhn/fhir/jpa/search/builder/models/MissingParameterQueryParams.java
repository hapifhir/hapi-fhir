/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.search.builder.models;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.search.builder.sql.SearchQueryBuilder;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;

import java.security.InvalidParameterException;
import java.util.List;

public class MissingParameterQueryParams {
	/**
	 * The sql builder
	 */
	private final SearchQueryBuilder mySqlBuilder;

	/**
	 * The parameter type
	 */
	private final RestSearchParameterTypeEnum myParamType;

	/**
	 * The list of query parameter types (only needed for validation)
	 */
	private final List<? extends IQueryParameterType> myQueryParameterTypes;

	/**
	 * The missing boolean value from :missing=true/false
	 */
	private final boolean myIsMissing;

	/**
	 * The name of the parameter.
	 */
	private final String myParamName;

	/**
	 * The resource type
	 */
	private final String myResourceType;

	/**
	 * The column on which to join.
	 */
	private final DbColumn mySourceJoinColumn;

	/**
	 * The partition id
	 */
	private final RequestPartitionId myRequestPartitionId;

	public MissingParameterQueryParams(
			SearchQueryBuilder theSqlBuilder,
			RestSearchParameterTypeEnum theParamType,
			List<? extends IQueryParameterType> theList,
			String theParamName,
			String theResourceType,
			DbColumn theSourceJoinColumn,
			RequestPartitionId theRequestPartitionId) {
		mySqlBuilder = theSqlBuilder;
		myParamType = theParamType;
		myQueryParameterTypes = theList;
		if (theList.isEmpty()) {
			// this will never happen
			throw new InvalidParameterException(Msg.code(2140) + " Invalid search parameter list. Cannot be empty!");
		}
		myIsMissing = theList.get(0).getMissing();
		myParamName = theParamName;
		myResourceType = theResourceType;
		mySourceJoinColumn = theSourceJoinColumn;
		myRequestPartitionId = theRequestPartitionId;
	}

	public SearchQueryBuilder getSqlBuilder() {
		return mySqlBuilder;
	}

	public RestSearchParameterTypeEnum getParamType() {
		return myParamType;
	}

	public List<? extends IQueryParameterType> getQueryParameterTypes() {
		return myQueryParameterTypes;
	}

	public boolean isMissing() {
		return myIsMissing;
	}

	public String getParamName() {
		return myParamName;
	}

	public String getResourceType() {
		return myResourceType;
	}

	public DbColumn getSourceJoinColumn() {
		return mySourceJoinColumn;
	}

	public ca.uhn.fhir.interceptor.model.RequestPartitionId getRequestPartitionId() {
		return myRequestPartitionId;
	}
}
