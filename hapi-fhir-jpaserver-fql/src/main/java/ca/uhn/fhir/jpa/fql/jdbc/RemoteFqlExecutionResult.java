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
package ca.uhn.fhir.jpa.fql.jdbc;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.fql.executor.FqlDataTypeEnum;
import ca.uhn.fhir.jpa.fql.executor.IFqlExecutionResult;
import ca.uhn.fhir.jpa.fql.parser.FqlStatement;
import ca.uhn.fhir.jpa.fql.provider.FqlRestProvider;
import ca.uhn.fhir.jpa.fql.util.FqlConstants;
import ca.uhn.fhir.rest.client.apache.ResourceEntity;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.IoUtil;
import ca.uhn.fhir.util.JsonUtil;
import ca.uhn.fhir.util.ValidateUtil;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.Validate;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.DecimalType;
import org.hl7.fhir.r4.model.IntegerType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.StringType;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static ca.uhn.fhir.jpa.fql.provider.FqlRestProvider.PROTOCOL_VERSION;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class RemoteFqlExecutionResult implements IFqlExecutionResult {
	private final List<String> myColumnNames = new ArrayList<>();
	private final List<FqlDataTypeEnum> myColumnTypes = new ArrayList<>();
	private final boolean mySupportsContinuations;
	private final String myBaseUrl;
	private final CloseableHttpClient myClient;
	private final int myFetchSize;
	private String mySearchId;
	private int myLimit;
	private InputStreamReader myReader;
	private Iterator<CSVRecord> myIterator;
	private int myCurrentFetchCount;
	private CloseableHttpResponse myRequest;
	private int myLastRowNumber;
	private boolean myExhausted;
	private ca.uhn.fhir.jpa.fql.parser.FqlStatement myStatement;

	public RemoteFqlExecutionResult(Parameters theRequestParameters, String theBaseUrl, CloseableHttpClient theClient, int theFetchSize, boolean theSupportsContinuations) throws SQLException {
		myBaseUrl = theBaseUrl;
		myClient = theClient;
		myFetchSize = theFetchSize;
		mySupportsContinuations = theSupportsContinuations;

		HttpPost post = new HttpPost(myBaseUrl + "/" + FqlConstants.FQL_EXECUTE);
		post.setEntity(new ResourceEntity(FhirContext.forR4Cached(), theRequestParameters));
		try {
			myRequest = myClient.execute(post);
			validateResponse();
			myReader = new InputStreamReader(myRequest.getEntity().getContent(), StandardCharsets.UTF_8);
			CSVParser csvParser = new CSVParser(myReader, FqlRestClient.CSV_FORMAT);
			myIterator = csvParser.iterator();
			readHeaderRows(true);
		} catch (IOException e) {
			throw new SQLException(e);
		}
	}

	private void validateResponse() {
		Validate.isTrue(myRequest.getStatusLine().getStatusCode() == 200, "Server returned wrong status: %d", myRequest.getStatusLine().getStatusCode());
	}

	private void readHeaderRows(boolean theFirstPage) {
		// Protocol version
		CSVRecord protocolVersionRow = myIterator.next();
		String protocolVersion = protocolVersionRow.get(0);
		ValidateUtil.isTrueOrThrowInvalidRequest(PROTOCOL_VERSION.equals(protocolVersion), "Wrong protocol version, expected %s but got %s", PROTOCOL_VERSION, protocolVersion);

		// Search ID, Limit, Parsed Statement
		CSVRecord searchIdRow = myIterator.next();
		mySearchId = searchIdRow.get(0);
		myLimit = Integer.parseInt(searchIdRow.get(1));
		String statementJsonString = searchIdRow.get(2);
		if (theFirstPage && isNotBlank(statementJsonString)) {
			myStatement = JsonUtil.deserialize(statementJsonString, ca.uhn.fhir.jpa.fql.parser.FqlStatement.class);
		}
		myCurrentFetchCount = 0;

		// Column Names
		CSVRecord nextRecord = myIterator.next();
		if (myColumnNames.isEmpty()) {
			boolean first = true;
			for (String next : nextRecord) {
				if (first) {
					// First column in the CSV is the row number, not
					// an actual SQL result column
					first = false;
					continue;
				}
				myColumnNames.add(next);
			}
		}

		// Column Types
		nextRecord = myIterator.next();
		if (myColumnTypes.isEmpty()) {
			boolean first = true;
			for (String next : nextRecord) {
				if (first) {
					// First column in the CSV is the row number, not
					// an actual SQL result column
					first = false;
					continue;
				}
				myColumnTypes.add(FqlDataTypeEnum.valueOf(next));
			}
		}

	}

	@Override
	public List<String> getColumnNames() {
		return myColumnNames;
	}

	@Override
	public List<FqlDataTypeEnum> getColumnTypes() {
		return myColumnTypes;
	}

	@Override
	public boolean hasNext() {
		if (myExhausted) {
			return false;
		}

		boolean hasNext = myIterator.hasNext();
		if (!hasNext && myCurrentFetchCount < myFetchSize) {
			myExhausted = true;
			close();
		} else if (!hasNext) {
			close();
			if (mySupportsContinuations) {
				hasNext = executeContinuationSearch();
			}
		}

		return hasNext;
	}

	@Override
	public Row getNextRow() {
		Validate.isTrue(!myExhausted, "Search is exhausted. This is a bug.");

		List<Object> columnValues = new ArrayList<>();
		boolean first = true;
		CSVRecord nextRecord = myIterator.next();
		myCurrentFetchCount++;

		for (String next : nextRecord) {
			if (first) {
				first = false;
				myLastRowNumber = Integer.parseInt(next);
				continue;
			}
			columnValues.add(next);
		}

		for (int i = 0; i < columnValues.size(); i++) {
			String existingValue = (String) columnValues.get(i);
			if (isNotBlank(existingValue)) {
				Object newValue = null;
				switch (myColumnTypes.get(i)) {
					case STRING:
						// No action
						break;
					case TIME:
						// No action (we represent times as strings internally)
						break;
					case INTEGER:
						newValue = Integer.parseInt(existingValue);
						break;
					case BOOLEAN:
						newValue = Boolean.parseBoolean(existingValue);
						break;
					case DATE:
						newValue = new DateType(existingValue).getValue();
						break;
					case TIMESTAMP:
						newValue = new DateTimeType(existingValue).getValue();
						break;
					case LONGINT:
						newValue = Long.parseLong(existingValue);
						break;
					case DECIMAL:
						newValue = new DecimalType(existingValue).getValue();
						break;
				}
				if (newValue != null) {
					columnValues.set(i, newValue);
				}
			} else {
				columnValues.set(i, null);
			}
		}

		return new Row(myLastRowNumber, columnValues);
	}

	private boolean executeContinuationSearch() {
		boolean hasNext;
		HttpPost post = new HttpPost(myBaseUrl + "/" + FqlConstants.FQL_EXECUTE);
		Parameters input = new Parameters();
		input.addParameter(FqlRestProvider.PARAM_ACTION, new CodeType(FqlRestProvider.PARAM_ACTION_SEARCH_CONTINUATION));
		input.addParameter(FqlRestProvider.PARAM_CONTINUATION, new StringType(mySearchId));
		input.addParameter(FqlRestProvider.PARAM_OFFSET, new IntegerType(myLastRowNumber + 1));
		input.addParameter(FqlRestProvider.PARAM_LIMIT, new IntegerType(myLimit));
		input.addParameter(FqlRestProvider.PARAM_FETCH_SIZE, new IntegerType(myFetchSize));
		input.addParameter(FqlRestProvider.PARAM_STATEMENT, new StringType(JsonUtil.serialize(myStatement, false)));
		post.setEntity(new ResourceEntity(FhirContext.forR4Cached(), input));
		try {
			myRequest = myClient.execute(post);
			validateResponse();
			myReader = new InputStreamReader(myRequest.getEntity().getContent(), StandardCharsets.UTF_8);
			CSVParser csvParser = new CSVParser(myReader, FqlRestClient.CSV_FORMAT);
			myIterator = csvParser.iterator();
			readHeaderRows(false);
		} catch (IOException e) {
			throw new InternalErrorException(e);
		}
		hasNext = myIterator.hasNext();
		return hasNext;
	}

	@Override
	public boolean isClosed() {
		return myRequest == null;
	}

	@Override
	public void close() {
		IoUtil.closeQuietly(myReader);
		IoUtil.closeQuietly(myRequest);
		myRequest = null;
	}

	@Override
	public String getSearchId() {
		return mySearchId;
	}

	@Override
	public int getLimit() {
		return myLimit;
	}

	@Override
	public FqlStatement getStatement() {
		return myStatement;
	}
}
