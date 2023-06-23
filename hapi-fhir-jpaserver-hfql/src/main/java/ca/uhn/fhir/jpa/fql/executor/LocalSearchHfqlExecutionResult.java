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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.fhirpath.IFhirPath;
import ca.uhn.fhir.jpa.fql.parser.HfqlStatement;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class LocalSearchHfqlExecutionResult implements IHfqlExecutionResult {

	private final IBundleProvider mySearchResult;
	private final IFhirPath myFhirPath;
	private final Integer myLimit;
	private final HfqlStatement myStatement;
	private final List<String> myColumnNames;
	private final List<HfqlDataTypeEnum> myColumnDataTypes;
	private final Predicate<IBaseResource> myWhereClausePredicate;
	private final IParser myParser;
	private int myTotalRowsFetched = 0;
	private int myNextSearchResultRow;
	private int myNextBatchRow = 0;
	private List<IBaseResource> myNextBatch;
	private IBaseResource myNextResource;
	private boolean myExhausted = false;
	private int myNextResourceSearchRow;

	public LocalSearchHfqlExecutionResult(HfqlStatement theStatement, IBundleProvider theSearchResult, IFhirPath theFhirPath, Integer theLimit, int theInitialOffset, List<HfqlDataTypeEnum> theColumnDataTypes, Predicate<IBaseResource> theWhereClausePredicate, FhirContext theFhirContext) {
		myStatement = theStatement;
		mySearchResult = theSearchResult;
		myFhirPath = theFhirPath;
		myLimit = theLimit;
		myNextSearchResultRow = theInitialOffset;
		myColumnDataTypes = theColumnDataTypes;
		myWhereClausePredicate = theWhereClausePredicate;
		myColumnNames = myStatement
			.getSelectClauses()
			.stream()
			.map(HfqlStatement.SelectClause::getAlias)
			.collect(Collectors.toUnmodifiableList());
		myParser = theFhirContext.newJsonParser();
	}


	@Override
	public List<String> getColumnNames() {
		return myColumnNames;
	}

	@Override
	public List<HfqlDataTypeEnum> getColumnTypes() {
		return myColumnDataTypes;
	}

	@Override
	public boolean hasNext() {
		fetchNextResource();
		return myNextResource != null;
	}

	private void fetchNextResource() {
		while (myNextResource == null && !myExhausted) {
			if (myNextBatch == null) {
				myNextBatch = mySearchResult.getResources(myNextSearchResultRow, myNextSearchResultRow + HfqlExecutor.BATCH_SIZE);
				myNextBatchRow = 0;
				myNextSearchResultRow += HfqlExecutor.BATCH_SIZE;
			}
			if (myNextBatch.isEmpty()) {
				myExhausted = true;
			} else if (myNextBatch.size() > myNextBatchRow) {
				myNextResource = myNextBatch.get(myNextBatchRow);
				myNextResourceSearchRow = (myNextSearchResultRow - HfqlExecutor.BATCH_SIZE) + myNextBatchRow;
				myNextBatchRow++;
			} else {
				myNextBatch = null;
			}

			if (myNextResource != null && !myWhereClausePredicate.test(myNextResource)) {
				myNextResource = null;
			}
		}

		if (myNextResource != null) {
			myTotalRowsFetched++;
			if (myLimit != null && myTotalRowsFetched >= myLimit) {
				myExhausted = true;
			}
		}
	}

	@Override
	public Row getNextRow() {
		fetchNextResource();
		Validate.isTrue(myNextResource != null, "No more results");

		List<Object> values = new ArrayList<>();
		for (HfqlStatement.SelectClause nextColumn : myStatement.getSelectClauses()) {
			String clause = nextColumn.getClause();
			List<IBase> columnValues = myFhirPath.evaluate(myNextResource, clause, IBase.class);
			String value = null;
			if (!columnValues.isEmpty()) {
				IBase firstColumnValue = columnValues.get(0);
				if (firstColumnValue instanceof IIdType) {
					value = ((IIdType) firstColumnValue).getIdPart();
				} else if (firstColumnValue != null) {
					value = myParser.encodeToString(firstColumnValue);
				}
			}
			values.add(value);
		}

		myNextResource = null;
		return new Row(myNextResourceSearchRow, values);
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
		return mySearchResult.getUuid();
	}

	@Override
	public int getLimit() {
		return myLimit != null ? myLimit : -1;
	}

	@Override
	public HfqlStatement getStatement() {
		return myStatement;
	}


}
