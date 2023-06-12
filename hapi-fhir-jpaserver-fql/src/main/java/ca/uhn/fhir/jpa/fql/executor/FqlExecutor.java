package ca.uhn.fhir.jpa.fql.executor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.fhirpath.IFhirPath;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.fql.parser.FqlParser;
import ca.uhn.fhir.jpa.fql.parser.FqlStatement;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.IPagingProvider;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FqlExecutor implements IFqlExecutor {
	private static final int BATCH_SIZE = 1000;

	@Autowired
	private DaoRegistry myDaoRegistry;
	@Autowired
	private FhirContext myFhirContext;
	@Autowired
	private IPagingProvider myPagingProvider;

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

		SearchParameterMap map = new SearchParameterMap();
		IBundleProvider outcome = dao.search(map, theRequestDetails);

		List<FqlStatement.SelectClause> selectClauses = statement.getSelectClauses();
		List<FqlStatement.WhereClause> whereClauses = statement.getWhereClauses();
		IFhirPath fhirPath = myFhirContext.newFhirPath();

		Integer limit = theLimit;
		if (limit == null) {
			limit = 1000;
		}
		if (whereClauses.isEmpty() && limit != null) {
			map.setLoadSynchronousUpTo(limit);
		}

		List<FqlStatement.WhereClause> searchClauses = statement.getSearchClauses();
		if (!searchClauses.isEmpty()) {
			// FIXME: implement
		}

		return new FqlResult(statement, selectClauses, whereClauses, outcome, fhirPath, limit);
	}

	@Override
	public IFqlResult executeContinuation(FqlStatement theStatement, String theSearchId, int theStartingOffset, Integer theLimit, RequestDetails theRequestDetails) {
		myPagingProvider.retrieveResultList(theRequestDetails, theSearchId);
		return null;
	}


	private static class FqlResult implements IFqlResult {

		private final IBundleProvider mySearchResult;
		private final IFhirPath myFhirPath;
		private final Integer myLimit;
		private final FqlStatement myStatement;
		private int myTotalRowsFetched = 0;
		private int myNextSearchResultRow = 0;
		private int myNextBatchRow = 0;
		private List<IBaseResource> myNextBatch;
		private IBaseResource myNextResource;
		private boolean myExhausted = false;
		private int myNextResourceSearchRow;

		public FqlResult(FqlStatement theStatement, List<FqlStatement.SelectClause> theSelectClauses, List<FqlStatement.WhereClause> theWhereClauses, IBundleProvider theSearchResult, IFhirPath theFhirPath, Integer theLimit) {
			myStatement = theStatement;
			mySearchResult = theSearchResult;
			myFhirPath = theFhirPath;
			myLimit = theLimit;
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
