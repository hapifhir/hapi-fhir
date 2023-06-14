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

import ca.uhn.fhir.context.BaseRuntimeChildDatatypeDefinition;
import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimePrimitiveDatatypeDefinition;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.fhirpath.IFhirPath;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.fql.parser.FqlParser;
import ca.uhn.fhir.jpa.fql.parser.FqlStatement;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.util.JpaParamUtil;
import ca.uhn.fhir.model.api.IQueryParameterAnd;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.QualifiedParamList;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.DateOrListParam;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.QualifierDetails;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.server.IPagingProvider;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.fhir.util.UrlUtil;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.springframework.beans.factory.annotation.Autowired;
import org.thymeleaf.util.StringUtils;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class FqlExecutor implements IFqlExecutor {
	private static final int BATCH_SIZE = 1000;
	public static final String[] EMPTY_STRING_ARRAY = new String[0];

	@Autowired
	private DaoRegistry myDaoRegistry;
	@Autowired
	private FhirContext myFhirContext;
	@Autowired
	private IPagingProvider myPagingProvider;
	@Autowired
	private ISearchParamRegistry mySearchParamRegistry;

	/**
	 * Constructor
	 */
	public FqlExecutor() {
		super();
	}

	@Override
	public FqlResult executeInitialSearch(String theStatement, Integer theLimit, RequestDetails theRequestDetails) {
		FqlParser parser = new FqlParser(myFhirContext, theStatement);
		FqlStatement statement = parser.parse();
		IFhirResourceDao dao = myDaoRegistry.getResourceDao(statement.getFromResourceName());
		if (dao == null) {
			throw new DataFormatException("Unknown or unsupported FROM type: " + statement.getFromResourceName());
		}

		massageSelectColumnNames(statement);

		SearchParameterMap map = new SearchParameterMap();
		IBundleProvider outcome = dao.search(map, theRequestDetails);

		IFhirPath fhirPath = myFhirContext.newFhirPath();

		List<FqlStatement.WhereClause> searchClauses = statement.getSearchClauses();
		for (FqlStatement.WhereClause nextSearchClause : searchClauses) {
			if (nextSearchClause.getLeft().equals(Constants.PARAM_ID)) {
				map.add(Constants.PARAM_ID, new TokenOrListParam(null, nextSearchClause.getRightAsStrings().toArray(EMPTY_STRING_ARRAY)));
			} else if (nextSearchClause.getLeft().equals(Constants.PARAM_LASTUPDATED)) {
				DateOrListParam param = new DateOrListParam();
				for (String nextValue : nextSearchClause.getRightAsStrings()) {
					param.addOr(new DateParam(nextValue));
				}
				map.add(Constants.PARAM_LASTUPDATED, param);
			} else if (nextSearchClause.getLeft().startsWith("_")) {
				throw newInvalidRequestExceptionUnknownSearchParameter(nextSearchClause);
			} else {

				String paramName = nextSearchClause.getLeft();
				QualifierDetails qualifiedParamName = QualifierDetails.extractQualifiersFromParameterName(paramName);

				RuntimeSearchParam searchParam = mySearchParamRegistry.getActiveSearchParam(statement.getFromResourceName(), qualifiedParamName.getParamName());
				if (searchParam == null) {
					throw newInvalidRequestExceptionUnknownSearchParameter(nextSearchClause);
				}

				QualifiedParamList values = new QualifiedParamList();
				values.setQualifier(qualifiedParamName.getWholeQualifier());
				values.addAll(nextSearchClause.getRightAsStrings());
				IQueryParameterAnd<?> andParam = JpaParamUtil.parseQueryParams(myFhirContext, searchParam.getParamType(), paramName, List.of(values));
				map.add(qualifiedParamName.getParamName(), andParam);

			}
		}

		return new FqlResult(statement, outcome, fhirPath, theLimit, 0);
	}

	private void massageSelectColumnNames(FqlStatement theFqlStatement) {

		List<FqlStatement.SelectClause> selectClauses = theFqlStatement.getSelectClauses();
		for (int i = 0; i < selectClauses.size(); i++) {
			if ("*".equals(selectClauses.get(i).getClause())) {
				resolveAndReplaceStarInSelectClauseAtIndex(theFqlStatement, selectClauses, i);
			}
		}

	}

	private void resolveAndReplaceStarInSelectClauseAtIndex(FqlStatement theFqlStatement, List<FqlStatement.SelectClause> theSelectClauses, int theIndex) {
		RuntimeResourceDefinition def = myFhirContext.getResourceDefinition(theFqlStatement.getFromResourceName());
		TreeSet<String> allLeafPaths = new TreeSet<>(Comparator.reverseOrder());
		findLeafPaths(def, allLeafPaths, new ArrayList<>());

		theSelectClauses.remove(theIndex);
		allLeafPaths.forEach(t-> theSelectClauses.add(theIndex, new FqlStatement.SelectClause(t)));
	}

	private void findLeafPaths(BaseRuntimeElementCompositeDefinition<?> theCompositeDefinition, TreeSet<String> theAllLeafPaths, List<String> theCurrentPath) {
		for (BaseRuntimeChildDefinition nextChild : theCompositeDefinition.getChildren()) {
			for (String nextChildName : nextChild.getValidChildNames()) {
				if (theCurrentPath.contains(nextChildName)) {
					continue;
				}
				if (nextChildName.equals("extension") || nextChildName.equals("modifierExtension")) {
					continue;
				}
				if (nextChildName.equals("id") && theCurrentPath.size() > 0) {
					continue;
				}

				theCurrentPath.add(nextChildName);

				BaseRuntimeElementDefinition<?> childDef = nextChild.getChildByName(nextChildName);
				if (childDef instanceof BaseRuntimeElementCompositeDefinition) {
					if (theCurrentPath.size() < 2) {
						findLeafPaths((BaseRuntimeElementCompositeDefinition<?>) childDef, theAllLeafPaths, theCurrentPath);
					}
				} else if (childDef instanceof RuntimePrimitiveDatatypeDefinition) {
					theAllLeafPaths.add(StringUtils.join(theCurrentPath, "."));
				}

				theCurrentPath.remove(theCurrentPath.size() - 1);
			}
		}
	}

	@Nonnull
	private static InvalidRequestException newInvalidRequestExceptionUnknownSearchParameter(FqlStatement.WhereClause nextSearchClause) {
		return new InvalidRequestException("Unknown/unsupported search parameter: " + UrlUtil.sanitizeUrlPart(nextSearchClause.getLeft()));
	}

	@Override
	public IFqlResult executeContinuation(FqlStatement theStatement, String theSearchId, int theStartingOffset, Integer theLimit, RequestDetails theRequestDetails) {
		IBundleProvider resultList = myPagingProvider.retrieveResultList(theRequestDetails, theSearchId);
		IFhirPath fhirPath = myFhirContext.newFhirPath();
		return new FqlResult(theStatement, resultList, fhirPath, theLimit, theStartingOffset);
	}


	private static class FqlResult implements IFqlResult {

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

		public FqlResult(FqlStatement theStatement, IBundleProvider theSearchResult, IFhirPath theFhirPath, Integer theLimit, int theInitialOffset) {
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
		public boolean hasNext() {
			fetchNextResource();
			return myNextResource != null;
		}

		private void fetchNextResource() {
			while (myNextResource == null && !myExhausted) {
				if (myNextBatch == null) {
					myNextBatch = mySearchResult.getResources(myNextSearchResultRow, myNextSearchResultRow + BATCH_SIZE);
					myNextBatchRow = 0;
					myNextSearchResultRow += BATCH_SIZE;
				}
				if (myNextBatch.isEmpty()) {
					myExhausted = true;
				} else if (myNextBatch.size() > myNextBatchRow) {
					myNextResource = myNextBatch.get(myNextBatchRow);
					myNextResourceSearchRow = (myNextSearchResultRow - BATCH_SIZE) + myNextBatchRow;
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
								haveMatch |= value;
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

			List<String> values = new ArrayList<>();
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

}
