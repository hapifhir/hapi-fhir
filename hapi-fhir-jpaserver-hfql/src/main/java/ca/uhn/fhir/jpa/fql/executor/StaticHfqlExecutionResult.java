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
import jakarta.annotation.Nullable;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @see IHfqlExecutionResult for information about the purpose of this class
 */
public class StaticHfqlExecutionResult implements IHfqlExecutionResult {
	private final String mySearchId;
	private final Iterator<List<Object>> myRowsIterator;
	private int myNextRowOffset;
	private HfqlStatement myStatement;

	/**
	 * Constructor for an empty result
	 *
	 * @param theSearchId The search ID associated with this result
	 */
	public StaticHfqlExecutionResult(@Nullable String theSearchId) {
		this(theSearchId, new HfqlStatement(), Collections.emptyList());
	}

	/**
	 * Constructor for an empty result
	 *
	 * @param theSearchId The search ID associated with this result
	 */
	public StaticHfqlExecutionResult(
			@Nullable String theSearchId,
			List<String> theColumnNames,
			List<HfqlDataTypeEnum> theDataTypes,
			List<List<Object>> theRows) {
		this(theSearchId, toStatement(theColumnNames, theDataTypes), theRows);
	}

	private static HfqlStatement toStatement(List<String> theColumnNames, List<HfqlDataTypeEnum> theDataTypes) {
		assert theColumnNames.size() == theDataTypes.size();

		HfqlStatement retVal = new HfqlStatement();
		for (int i = 0; i < theColumnNames.size(); i++) {
			retVal.addSelectClause(theColumnNames.get(i))
					.setAlias(theColumnNames.get(i))
					.setDataType(theDataTypes.get(i));
		}
		return retVal;
	}

	/**
	 * Constructor
	 */
	public StaticHfqlExecutionResult(
			@Nullable String theSearchId, HfqlStatement theStatement, List<List<Object>> theRows) {
		mySearchId = theSearchId;
		myStatement = theStatement;
		myRowsIterator = theRows.iterator();
		myNextRowOffset = 0;
	}

	@Override
	public boolean hasNext() {
		return myRowsIterator.hasNext();
	}

	@Override
	public Row getNextRow() {
		return new Row(myNextRowOffset++, myRowsIterator.next());
	}

	@Override
	public boolean isClosed() {
		return false;
	}

	@Override
	public void close() {
		// ignore
	}

	@Override
	public String getSearchId() {
		return mySearchId;
	}

	@Override
	public int getLimit() {
		return 0;
	}

	@Override
	public HfqlStatement getStatement() {
		return myStatement;
	}

	public static IHfqlExecutionResult withError(String theErrorMessage) {
		StaticHfqlExecutionResult retVal = new StaticHfqlExecutionResult(
				null, List.of("Error"), List.of(HfqlDataTypeEnum.STRING), List.of(List.of(theErrorMessage)));
		retVal.myNextRowOffset = IHfqlExecutionResult.ROW_OFFSET_ERROR;
		return retVal;
	}
}
