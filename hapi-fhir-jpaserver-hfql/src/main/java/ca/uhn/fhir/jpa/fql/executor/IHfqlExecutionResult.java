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

import java.util.List;

/**
 * This interface represents a ResultSet returned by the HFQL query layer in
 * {@link IHfqlExecutor}. Think of it as roughly equivalent to the JDBC
 * {@link java.sql.ResultSet} except that it's the internal version of that.
 * <p>
 * There are several implementations of this interface:
 * <ul>
 * <li>
 *    {@link LocalSearchHfqlExecutionResult} - Implementation backed by a database search.
 *    This is used inside the HAPI FHIR server that is handling HFQL queries.
 * </li>
 * <li>
 *    {@link StaticHfqlExecutionResult} - Static implementation with fixed results. This is
 *    usually used to represent errors and failed queries inside the HAPI FHIR server.
 * </li>
 * <li>
 *    {@link ca.uhn.fhir.jpa.fql.jdbc.RemoteHfqlExecutionResult} - This is used inside the
 *    JDBC driver (ie. remote from the HAPI FHIR server) and holds results that have
 *    been received over the wire.
 * </li>
 * </ul>
 * </p>
 */
public interface IHfqlExecutionResult {

	int ROW_OFFSET_ERROR = -1;

	boolean hasNext();

	Row getNextRow();

	boolean isClosed();

	void close();

	String getSearchId();

	int getLimit();

	HfqlStatement getStatement();

	class Row {

		private final List<Object> myRowValues;
		private final int myRowOffset;

		public Row(int theRowOffset, List<Object> theRowValues) {
			myRowOffset = theRowOffset;
			myRowValues = theRowValues;
		}

		public int getRowOffset() {
			return myRowOffset;
		}

		public List<Object> getRowValues() {
			return myRowValues;
		}

		public Row toRowOffset(int theRowOffset) {
			return new Row(theRowOffset, myRowValues);
		}
	}
}
