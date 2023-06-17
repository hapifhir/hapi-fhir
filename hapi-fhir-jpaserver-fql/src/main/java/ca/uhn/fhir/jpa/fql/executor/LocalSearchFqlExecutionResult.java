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

import ca.uhn.fhir.fhirpath.IFhirPath;
import ca.uhn.fhir.jpa.fql.parser.FqlStatement;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LocalSearchFqlExecutionResult implements IFqlExecutionResult {

	private final IBundleProvider mySearchResult;
	private final IFhirPath myFhirPath;
	private final Integer myLimit;
	private final FqlStatement myStatement;
	private int myTotalRowsFetched = 0;
	private int myNextSearchResultRow;
	private int myNextBatchRow = 0;
	private List<IBaseResource> myNextBatch;
	private IBaseResource myNextResource;
	private boolean myExhausted = false;
	private int myNextResourceSearchRow;

	public LocalSearchFqlExecutionResult(FqlStatement theStatement, IBundleProvider theSearchResult, IFhirPath theFhirPath, Integer theLimit, int theInitialOffset) {
		myStatement = theStatement;
		mySearchResult = theSearchResult;
		myFhirPath = theFhirPath;
		myLimit = theLimit;
		myNextSearchResultRow = theInitialOffset;
	}


	@Override
	public List<String> getColumnNames() {
		return
			myStatement
				.getSelectClauses()
				.stream()
				.map(FqlStatement.SelectClause::getAlias)
				.collect(Collectors.toUnmodifiableList());
	}

	@Override
	public List<FqlDataTypeEnum> getColumnTypes() {
		/*
		 * For now we assume that all datatypes returned by select clauses are all
		 * strings. It'd be nice to do something more nuanced here, but it's hard to
		 * determine datatypes from a fhirpath expression
		 */
		ArrayList<FqlDataTypeEnum> retVal = new ArrayList<>();
		for (int i = 0; i < myStatement.getSelectClauses().size(); i++) {
			retVal.add(FqlDataTypeEnum.STRING);
		}
		return retVal;
	}

	@Override
	public boolean hasNext() {
		fetchNextResource();
		return myNextResource != null;
	}

	private void fetchNextResource() {
		while (myNextResource == null && !myExhausted) {
			if (myNextBatch == null) {
				myNextBatch = mySearchResult.getResources(myNextSearchResultRow, myNextSearchResultRow + FqlExecutor.BATCH_SIZE);
				myNextBatchRow = 0;
				myNextSearchResultRow += FqlExecutor.BATCH_SIZE;
			}
			if (myNextBatch.isEmpty()) {
				myExhausted = true;
			} else if (myNextBatch.size() > myNextBatchRow) {
				myNextResource = myNextBatch.get(myNextBatchRow);
				myNextResourceSearchRow = (myNextSearchResultRow - FqlExecutor.BATCH_SIZE) + myNextBatchRow;
				myNextBatchRow++;
			} else {
				myNextBatch = null;
			}

			if (myNextResource != null && !myStatement.getWhereClauses().isEmpty()) {
				for (FqlStatement.WhereClause nextWhereClause : myStatement.getWhereClauses()) {

					List<IBase> values = myFhirPath.evaluate(myNextResource, nextWhereClause.getLeft(), IBase.class);
					boolean haveMatch = false;
					for (IBase nextValue : values) {
						for (String nextRight : nextWhereClause.getRight()) {
							String expression = "$this = " + nextRight;
							IPrimitiveType outcome = myFhirPath
								.evaluateFirst(nextValue, expression, IPrimitiveType.class)
								.orElseThrow(IllegalStateException::new);
							Boolean value = (Boolean) outcome.getValue();
							haveMatch = value;
							if (haveMatch) {
								break;
							}
						}
						if (haveMatch) {
							break;
						}
					}

					if (!haveMatch) {
						myNextResource = null;
						break;
					}

				}

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
		for (FqlStatement.SelectClause nextColumn : myStatement.getSelectClauses()) {
			List<IPrimitiveType> nextPrimitive = myFhirPath.evaluate(myNextResource, nextColumn.getClause(), IPrimitiveType.class);
			String value = null;
			if (!nextPrimitive.isEmpty()) {
				IPrimitiveType primitive = nextPrimitive.get(0);
				if (primitive != null) {
					value = primitive.getValueAsString();
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
	public FqlStatement getStatement() {
		return myStatement;
	}


}
