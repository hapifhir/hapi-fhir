/*-
 * #%L
 * HAPI FHIR JPA Server - Firely Query Language
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class StaticHfqlExecutionResult implements IHfqlExecutionResult {
	private final String mySearchId;
	private final Iterator<List<Object>> myRowsIterator;
	private final List<HfqlDataTypeEnum> myDataTypes;
	private int myNextRowOffset;
	private List<String> myColumnNames;

	/**
	 * Constructor for an empty result
	 *
	 * @param theSearchId The search ID associated with this result
	 */
	public StaticHfqlExecutionResult(@Nullable String theSearchId) {
		this(theSearchId, Collections.emptyList(), Collections.emptyList(), Collections.emptyList());
	}

	/**
	 * Constructor for an empty result
	 *
	 * @param theSearchId The search ID associated with this result
	 */
	public StaticHfqlExecutionResult(@Nullable String theSearchId, List<String> theColumnNames, List<HfqlDataTypeEnum> theDataTypes, List<List<Object>> theRows) {
		mySearchId = theSearchId;
		myColumnNames = theColumnNames;
		myDataTypes = theDataTypes;
		myRowsIterator = theRows.iterator();
		myNextRowOffset = 0;

		for (List<Object> next : theRows) {
			assert next.size() == theColumnNames.size();
			assert next.size() == theDataTypes.size() : "Row has " + next.size() + " columns but datatypes has " + theDataTypes.size();
			for (int i = 0; i < next.size(); i++) {
				Object value = next.get(i);
				if (value != null) {
					switch (theDataTypes.get(i)) {
						case STRING:
							assert value instanceof String : "Column " + i + " has value of type " + value.getClass().getSimpleName() + " but expected " + theDataTypes.get(i) + ": " + value;
							break;
						case INTEGER:
							assert value instanceof Integer : "Column " + i + " has value of type " + value.getClass().getSimpleName() + " but expected " + theDataTypes.get(i) + ": " + value;
							break;
					}
				}
			}
		}
	}

	@Override
	public List<String> getColumnNames() {
		return myColumnNames;
	}

	@Override
	public List<HfqlDataTypeEnum> getColumnTypes() {
		return myDataTypes;
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
		return null;
	}

}
