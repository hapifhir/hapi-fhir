/*-
 * #%L
 * HAPI-FHIR Storage Test Utilities
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.storage.test;

/**
 * Parameter for {@link CircularQueueCaptureQueriesListenerAssertions} methods
 *
 * @since 8.14.0
 */
public enum SqlCountTypeEnum {

	/// Count the number of parameter sets issued.
	/// For example, given a query that inserts to a table,
	/// if we execute `INSERT INTO my_table (col1, col2) VALUES (?, ?)` in a batch with 3 pairs of
	/// parameters (meaning 3 rows will be inserted), the count should be `3`.
	PARAMETER_SETS("ParamSets"),

	/// Count the number of statements issued, regardless of how many parameter sets are issued
	/// with the statement.
	/// For example, given a query that inserts to a table,
	/// if we execute `INSERT INTO my_table (col1, col2) VALUES (?, ?)` in a batch with 3 pairs of
	/// parameters (meaning 3 rows will be inserted), the count should be `1`.
	STATEMENTS("Statements");

	private final String myShortName;

	SqlCountTypeEnum(String theShortName) {
		myShortName = theShortName;
	}

	public String shortName() {
		return myShortName;
	}
}
