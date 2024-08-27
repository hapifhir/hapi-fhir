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
package ca.uhn.fhir.jpa.fql.provider;

import ca.uhn.fhir.jpa.fql.executor.IHfqlExecutionResult;
import ca.uhn.fhir.jpa.fql.executor.IHfqlExecutor;
import ca.uhn.fhir.jpa.fql.parser.HfqlStatement;
import ca.uhn.fhir.jpa.fql.util.HfqlConstants;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.util.DatatypeUtil;
import ca.uhn.fhir.util.JsonUtil;
import ca.uhn.fhir.util.ValidateUtil;
import ca.uhn.fhir.util.VersionUtil;
import jakarta.annotation.Nullable;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.csv.CSVPrinter;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.OutputStreamWriter;

import static ca.uhn.fhir.jpa.fql.jdbc.HfqlRestClient.CSV_FORMAT;
import static ca.uhn.fhir.rest.api.Constants.CHARSET_UTF8_CTSUFFIX;
import static ca.uhn.fhir.rest.api.Constants.CT_TEXT_CSV;
import static ca.uhn.fhir.util.DatatypeUtil.toStringValue;

public class HfqlRestProvider {

	@Autowired
	private IHfqlExecutor myHfqlExecutor;

	/**
	 * Constructor
	 */
	public HfqlRestProvider() {
		this(null);
	}

	/**
	 * Constructor
	 */
	public HfqlRestProvider(IHfqlExecutor theHfqlExecutor) {
		myHfqlExecutor = theHfqlExecutor;
	}

	public IHfqlExecutor getHfqlExecutor() {
		return myHfqlExecutor;
	}

	public void setHfqlExecutor(IHfqlExecutor theHfqlExecutor) {
		myHfqlExecutor = theHfqlExecutor;
	}

	/**
	 * This function implements the <code>$hfql-execute</code> operation, which is
	 * the FHIR operation that the HFQL JDBC client uses to talk to the server. All
	 * communication between the client and the server goes through this operation. The
	 * response is not FHIR however: Responses from this operation are in CSV format using
	 * a custom CSV format that is understood by the client. See
	 * {@link #streamResponseCsv(HttpServletResponse, int, IHfqlExecutionResult, boolean, HfqlStatement)}
	 * to see how that format works.
	 */
	@Operation(name = HfqlConstants.HFQL_EXECUTE, manualResponse = true)
	public void executeFql(
			@OperationParam(name = HfqlConstants.PARAM_ACTION, typeName = "code", min = 0, max = 1)
					IPrimitiveType<String> theAction,
			@OperationParam(name = HfqlConstants.PARAM_QUERY, typeName = "string", min = 0, max = 1)
					IPrimitiveType<String> theQuery,
			@OperationParam(name = HfqlConstants.PARAM_STATEMENT, typeName = "string", min = 0, max = 1)
					IPrimitiveType<String> theStatement,
			@OperationParam(name = HfqlConstants.PARAM_CONTINUATION, typeName = "string", min = 0, max = 1)
					IPrimitiveType<String> theContinuation,
			@OperationParam(name = HfqlConstants.PARAM_LIMIT, typeName = "integer", min = 0, max = 1)
					IPrimitiveType<Integer> theLimit,
			@OperationParam(name = HfqlConstants.PARAM_OFFSET, typeName = "integer", min = 0, max = 1)
					IPrimitiveType<Integer> theOffset,
			@OperationParam(name = HfqlConstants.PARAM_FETCH_SIZE, typeName = "integer", min = 0, max = 1)
					IPrimitiveType<Integer> theFetchSize,
			@OperationParam(name = HfqlConstants.PARAM_INTROSPECT_TABLE_NAME, typeName = "string", min = 0, max = 1)
					IPrimitiveType<String> theIntrospectTableName,
			@OperationParam(name = HfqlConstants.PARAM_INTROSPECT_COLUMN_NAME, typeName = "string", min = 0, max = 1)
					IPrimitiveType<String> theIntrospectColumnName,
			RequestDetails theRequestDetails,
			HttpServletResponse theServletResponse)
			throws IOException {
		String action = toStringValue(theAction);

		int fetchSize = parseFetchSize(theFetchSize);
		Integer limit = parseLimit(theLimit);
		switch (action) {
			case HfqlConstants.PARAM_ACTION_SEARCH: {
				String query = toStringValue(theQuery);
				IHfqlExecutionResult outcome = getHfqlExecutor().executeInitialSearch(query, limit, theRequestDetails);
				streamResponseCsv(theServletResponse, fetchSize, outcome, true, outcome.getStatement());
				break;
			}
			case HfqlConstants.PARAM_ACTION_SEARCH_CONTINUATION: {
				String continuation = toStringValue(theContinuation);
				ValidateUtil.isTrueOrThrowInvalidRequest(
						theOffset != null && theOffset.hasValue(), "No offset supplied");
				@SuppressWarnings("java:S2259") // Sonar doesn't understand the above
				int startingOffset = theOffset.getValue();

				String statement = DatatypeUtil.toStringValue(theStatement);
				ValidateUtil.isNotBlankOrThrowIllegalArgument(statement, "No statement provided");
				HfqlStatement statementJson = JsonUtil.deserialize(statement, HfqlStatement.class);

				IHfqlExecutionResult outcome = myHfqlExecutor.executeContinuation(
						statementJson, continuation, startingOffset, limit, theRequestDetails);
				streamResponseCsv(theServletResponse, fetchSize, outcome, false, outcome.getStatement());
				break;
			}
			case HfqlConstants.PARAM_ACTION_INTROSPECT_TABLES: {
				IHfqlExecutionResult outcome = myHfqlExecutor.introspectTables();
				streamResponseCsv(theServletResponse, fetchSize, outcome, true, outcome.getStatement());
				break;
			}
			case HfqlConstants.PARAM_ACTION_INTROSPECT_COLUMNS: {
				String tableName = toStringValue(theIntrospectTableName);
				String columnName = toStringValue(theIntrospectColumnName);
				IHfqlExecutionResult outcome = myHfqlExecutor.introspectColumns(tableName, columnName);
				streamResponseCsv(theServletResponse, fetchSize, outcome, true, outcome.getStatement());
				break;
			}
			default:
				//noinspection DataFlowIssue
				ValidateUtil.isTrueOrThrowInvalidRequest(false, "Unrecognized action: %s", action);
		}
	}

	@Nullable
	private static Integer parseLimit(IPrimitiveType<Integer> theLimit) {
		Integer limit = null;
		if (theLimit != null) {
			limit = theLimit.getValue();
		}
		return limit;
	}

	private static int parseFetchSize(IPrimitiveType<Integer> theFetchSize) {
		int fetchSize = 1000;
		if (theFetchSize != null && theFetchSize.getValue() != null) {
			fetchSize = theFetchSize.getValue();
		}
		if (fetchSize == 0) {
			fetchSize = HfqlConstants.MAX_FETCH_SIZE;
		}
		ValidateUtil.isTrueOrThrowInvalidRequest(
				fetchSize >= HfqlConstants.MIN_FETCH_SIZE && fetchSize <= HfqlConstants.MAX_FETCH_SIZE,
				"Fetch size must be between %d and %d",
				HfqlConstants.MIN_FETCH_SIZE,
				HfqlConstants.MAX_FETCH_SIZE);
		return fetchSize;
	}

	private static void streamResponseCsv(
			HttpServletResponse theServletResponse,
			int theFetchSize,
			IHfqlExecutionResult theResult,
			boolean theInitialPage,
			HfqlStatement theStatement)
			throws IOException {
		theServletResponse.setStatus(200);
		theServletResponse.setContentType(CT_TEXT_CSV + CHARSET_UTF8_CTSUFFIX);
		try (ServletOutputStream outputStream = theServletResponse.getOutputStream()) {
			Appendable out = new OutputStreamWriter(outputStream);
			try (CSVPrinter csvWriter = new CSVPrinter(out, CSV_FORMAT)) {
				csvWriter.printRecords();

				// Protocol version
				csvWriter.printRecord(HfqlConstants.PROTOCOL_VERSION, "HAPI FHIR " + VersionUtil.getVersion());

				// Search ID, Limit, Parsed FQL Statement
				String searchId = theResult.getSearchId();
				String parsedFqlStatement = "";
				if (theInitialPage && theStatement != null) {
					parsedFqlStatement = JsonUtil.serialize(theStatement, false);
				}
				csvWriter.printRecord(searchId, theResult.getLimit(), parsedFqlStatement);

				// Print the rows
				int recordCount = 0;
				while (recordCount++ < theFetchSize && theResult.hasNext()) {
					IHfqlExecutionResult.Row nextRow = theResult.getNextRow();
					csvWriter.print(nextRow.getRowOffset());
					csvWriter.printRecord(nextRow.getRowValues());
				}
				csvWriter.flush();
			}
		}
	}
}
