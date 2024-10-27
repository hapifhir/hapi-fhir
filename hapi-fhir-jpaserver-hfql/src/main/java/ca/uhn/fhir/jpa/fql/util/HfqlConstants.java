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
package ca.uhn.fhir.jpa.fql.util;

public class HfqlConstants {

	public static final String HFQL_EXECUTE = "$hfql-execute";
	public static final String PARAM_QUERY = "query";
	public static final String PARAM_STATEMENT = "statement";
	public static final String PARAM_CONTINUATION = "continuation";
	public static final String PARAM_LIMIT = "limit";
	public static final String PARAM_OFFSET = "offset";
	public static final String PARAM_FETCH_SIZE = "fetchSize";
	public static final String PROTOCOL_VERSION = "1";
	public static final String PARAM_ACTION = "action";
	public static final String PARAM_ACTION_SEARCH = "search";
	public static final String PARAM_ACTION_SEARCH_CONTINUATION = "searchContinuation";
	public static final String PARAM_ACTION_INTROSPECT_TABLES = "introspectTables";
	public static final String PARAM_ACTION_INTROSPECT_COLUMNS = "introspectColumns";
	public static final int MIN_FETCH_SIZE = 1;
	public static final int DEFAULT_FETCH_SIZE = 1000;
	public static final int MAX_FETCH_SIZE = 10000;
	public static final String PARAM_INTROSPECT_TABLE_NAME = "introspectTableName";
	public static final String PARAM_INTROSPECT_COLUMN_NAME = "introspectColumnName";
	/**
	 * This is the maximum number of results that can be sorted or grouped on
	 */
	public static final int ORDER_AND_GROUP_LIMIT = 10000;

	private HfqlConstants() {}
}
