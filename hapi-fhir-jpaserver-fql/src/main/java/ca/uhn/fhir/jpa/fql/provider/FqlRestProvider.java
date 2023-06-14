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
package ca.uhn.fhir.jpa.fql.provider;

import ca.uhn.fhir.jpa.fql.executor.IFqlExecutor;
import ca.uhn.fhir.jpa.fql.executor.IFqlResult;
import ca.uhn.fhir.jpa.fql.parser.FqlStatement;
import ca.uhn.fhir.jpa.fql.util.FqlConstants;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.util.DatatypeUtil;
import ca.uhn.fhir.util.JsonUtil;
import ca.uhn.fhir.util.ValidateUtil;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nullable;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;

import static ca.uhn.fhir.jpa.fql.jdbc.FqlRestClient.CSV_FORMAT;
import static ca.uhn.fhir.rest.api.Constants.CHARSET_UTF8_CTSUFFIX;
import static ca.uhn.fhir.rest.api.Constants.CT_TEXT_CSV;
import static ca.uhn.fhir.util.DatatypeUtil.toStringValue;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class FqlRestProvider {

	public static final String PARAM_QUERY = "query";
	public static final String PARAM_STATEMENT = "statement";
	public static final String PARAM_CONTINUATION = "continuation";
	public static final String PARAM_LIMIT = "limit";
	public static final String PARAM_OFFSET = "offset";
	public static final String PARAM_FETCH_SIZE = "fetchSize";
	public static final String PROTOCOL_VERSION = "1";
	private static final int MIN_FETCH_SIZE = 1;
	private static final int MAX_FETCH_SIZE = 10000;

	@Autowired
	private IFqlExecutor myFqlExecutor;

	/**
	 * Constructor
	 */
	public FqlRestProvider() {
		super();
	}

	@Operation(name = FqlConstants.FQL_EXECUTE, manualResponse = true)
	public void executeFql(
		@OperationParam(name = PARAM_QUERY, typeName = "string", min = 0, max = 1) IPrimitiveType<String> theQuery,
		@OperationParam(name = PARAM_STATEMENT, typeName = "string", min = 0, max = 1) IPrimitiveType<String> theStatement,
		@OperationParam(name = PARAM_CONTINUATION, typeName = "string", min = 0, max = 1) IPrimitiveType<String> theContinuation,
		@OperationParam(name = PARAM_LIMIT, typeName = "integer", min = 0, max = 1) IPrimitiveType<Integer> theLimit,
		@OperationParam(name = PARAM_OFFSET, typeName = "integer", min = 0, max = 1) IPrimitiveType<Integer> theOffset,
		@OperationParam(name = PARAM_FETCH_SIZE, typeName = "integer", min = 0, max = 1) IPrimitiveType<Integer> theFetchSize,
		RequestDetails theRequestDetails,
		HttpServletResponse theServletResponse
	) throws IOException {
		int fetchSize = parseFetchSize(theFetchSize);
		Integer limit = parseLimit(theLimit);

		String query = toStringValue(theQuery);
		String continuation = toStringValue(theContinuation);
		ValidateUtil.isTrueOrThrowInvalidRequest(isNotBlank(query) ^ isNotBlank(continuation), "Must have either a query or a continuation");

		if (isNotBlank(query)) {
			IFqlResult outcome = myFqlExecutor.executeInitialSearch(query, limit, theRequestDetails);
			streamResponseCsv(theServletResponse, fetchSize, outcome, true, outcome.getStatement());
		} else {
			ValidateUtil.isTrueOrThrowInvalidRequest(theOffset != null && theOffset.hasValue(), "No offset supplied");
			int startingOffset = theOffset.getValue();

			String statement = DatatypeUtil.toStringValue(theStatement);
			ValidateUtil.isNotBlankOrThrowIllegalArgument(statement, "No statement provided");
			FqlStatement statementJson = JsonUtil.deserialize(statement, FqlStatement.class);

			IFqlResult outcome = myFqlExecutor.executeContinuation(statementJson, continuation, startingOffset, limit, theRequestDetails);
			streamResponseCsv(theServletResponse, fetchSize, outcome, false, outcome.getStatement());
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
		ValidateUtil.isTrueOrThrowInvalidRequest(fetchSize >= MIN_FETCH_SIZE && fetchSize <= MAX_FETCH_SIZE, "Fetch size must be between %d and %d", MIN_FETCH_SIZE, MAX_FETCH_SIZE);
		return fetchSize;
	}

	private static void streamResponseCsv(HttpServletResponse theServletResponse, int theFetchSize, IFqlResult theResult, boolean theInitialPage, FqlStatement theStatement) throws IOException {
		theServletResponse.setStatus(200);
		theServletResponse.setContentType(CT_TEXT_CSV + CHARSET_UTF8_CTSUFFIX);
		try (ServletOutputStream outputStream = theServletResponse.getOutputStream()) {
			Appendable out = new OutputStreamWriter(outputStream);
			CSVPrinter csvWriter = new CSVPrinter(out, CSV_FORMAT);
			csvWriter.printRecords();

			// Protocol version
			csvWriter.printRecord(PROTOCOL_VERSION);

			// Search ID, Limit, Parsed FQL Statement
			String searchId = theResult.getSearchId();
			Validate.notBlank(searchId, "Search returned a blank search ID");
			String parsedFqlStatement = "";
			if (theInitialPage) {
				Validate.notNull(theStatement, "No statement provided");
				parsedFqlStatement = JsonUtil.serialize(theStatement, false);
			}
			csvWriter.printRecord(searchId, theResult.getLimit(), parsedFqlStatement);

			// First row is the column names
			if (theInitialPage) {
				csvWriter.print("");
				csvWriter.printRecord(theResult.getColumnNames());
			} else {
				csvWriter.printRecord("");
			}

			// Print the rows
			int recordCount = 0;
			while (recordCount++ < theFetchSize && theResult.hasNext()) {
				IFqlResult.Row nextRow = theResult.getNextRow();
				csvWriter.print(nextRow.searchRowNumber());
				csvWriter.printRecord(nextRow.values());
			}
			csvWriter.close(true);
		}
	}


}
