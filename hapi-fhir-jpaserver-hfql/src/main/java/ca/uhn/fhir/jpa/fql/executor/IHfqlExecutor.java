/*-
 * #%L
 * HAPI FHIR JPA Server - HFQL Driver
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
package ca.uhn.fhir.jpa.fql.executor;

import ca.uhn.fhir.jpa.fql.parser.HfqlStatement;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import jakarta.annotation.Nullable;

public interface IHfqlExecutor {

	/**
	 * Execute a FQL query and return the first page of data
	 *
	 * @param theStatement      The FQL statement to execute
	 * @param theLimit          The maximum number of records to retrieve
	 * @param theRequestDetails The request details associated with the request
	 * @return Returns a {@link IHfqlExecutionResult result object}. Note that the returned object is not thread safe.
	 */
	IHfqlExecutionResult executeInitialSearch(String theStatement, Integer theLimit, RequestDetails theRequestDetails);

	/**
	 * Load a subsequent page of data from a search initiated by a call to {@link #executeInitialSearch(String, Integer, RequestDetails)}.
	 *
	 * @param theStatement      The parsed statement from the initial search. Available through a call to {@link IHfqlExecutionResult#getStatement()}.
	 * @param theSearchId       The search ID from the initial search. Available through a call to {@link IHfqlExecutionResult#getSearchId()}.
	 * @param theLimit          The maximum number of results to return (across all pages)
	 * @param theRequestDetails The request details associated with the request
	 * @param theStartingOffset The row offset count for the first result to return. This should be set to one higher than the last value returned by {@link IHfqlExecutionResult.Row#getRowOffset()}.
	 */
	IHfqlExecutionResult executeContinuation(
			HfqlStatement theStatement,
			String theSearchId,
			int theStartingOffset,
			Integer theLimit,
			RequestDetails theRequestDetails);

	/**
	 * Provides a list of "tables", which are actually resource types, in order to
	 * support the JCBC {@link java.sql.DatabaseMetaData#getTables(String, String, String, String[])}
	 * query.
	 */
	IHfqlExecutionResult introspectTables();

	/**
	 * Provides a list of "columns", which are actually selected valid FHIRPath expressions
	 * that can be selected on a resource
	 *
	 * @param theTableName The table name or null
	 * @param theColumnName The column name or null
	 */
	IHfqlExecutionResult introspectColumns(@Nullable String theTableName, @Nullable String theColumnName);
}
