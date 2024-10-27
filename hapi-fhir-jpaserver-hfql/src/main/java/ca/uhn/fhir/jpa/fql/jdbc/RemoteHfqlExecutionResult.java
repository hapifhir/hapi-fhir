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
package ca.uhn.fhir.jpa.fql.jdbc;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.fql.executor.IHfqlExecutionResult;
import ca.uhn.fhir.jpa.fql.parser.HfqlStatement;
import ca.uhn.fhir.jpa.fql.util.HfqlConstants;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.client.apache.ResourceEntity;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.IoUtil;
import ca.uhn.fhir.util.JsonUtil;
import ca.uhn.fhir.util.ValidateUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.Validate;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.Binary;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.DecimalType;
import org.hl7.fhir.r4.model.IntegerType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.StringType;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static ca.uhn.fhir.jpa.fql.util.HfqlConstants.PROTOCOL_VERSION;
import static org.apache.commons.lang3.StringUtils.defaultIfBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * This implementation of {@link IHfqlExecutionResult} is intended to be used within
 * a remote client (ie a JDBC driver). It executes a call to a FHIR server, executing
 * the {@link ca.uhn.fhir.jpa.fql.provider.HfqlRestProvider#executeFql(IPrimitiveType, IPrimitiveType, IPrimitiveType, IPrimitiveType, IPrimitiveType, IPrimitiveType, IPrimitiveType, IPrimitiveType, IPrimitiveType, RequestDetails, HttpServletResponse)}
 * operation, parses the response and returns it.
 *
 * @see IHfqlExecutionResult for more information about the purpose of this class
 */
public class RemoteHfqlExecutionResult implements IHfqlExecutionResult {
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
	private HfqlStatement myStatement;

	public RemoteHfqlExecutionResult(
			Parameters theRequestParameters,
			String theBaseUrl,
			CloseableHttpClient theClient,
			int theFetchSize,
			boolean theSupportsContinuations)
			throws SQLException {
		myBaseUrl = theBaseUrl;
		myClient = theClient;
		myFetchSize = theFetchSize;
		mySupportsContinuations = theSupportsContinuations;

		HttpPost post = new HttpPost(myBaseUrl + "/" + HfqlConstants.HFQL_EXECUTE);
		post.setEntity(new ResourceEntity(FhirContext.forR4Cached(), theRequestParameters));
		try {
			myRequest = myClient.execute(post);
			validateResponse();
			myReader = new InputStreamReader(myRequest.getEntity().getContent(), StandardCharsets.UTF_8);
			CSVParser csvParser = new CSVParser(myReader, HfqlRestClient.CSV_FORMAT);
			myIterator = csvParser.iterator();
			readHeaderRows(true);
		} catch (IOException e) {
			throw new SQLException(Msg.code(2400) + e.getMessage(), e);
		}
	}

	public RemoteHfqlExecutionResult(Parameters theRequestParameters, IGenericClient theClient) throws IOException {
		myBaseUrl = null;
		myClient = null;
		myFetchSize = 100;
		mySupportsContinuations = false;
		Binary response = theClient
				.operation()
				.onServer()
				.named(HfqlConstants.HFQL_EXECUTE)
				.withParameters(theRequestParameters)
				.returnResourceType(Binary.class)
				.execute();
		String contentType = defaultIfBlank(response.getContentType(), "");
		if (contentType.contains(";")) {
			contentType = contentType.substring(0, contentType.indexOf(';'));
		}
		contentType = contentType.trim();
		Validate.isTrue(Constants.CT_TEXT_CSV.equals(contentType), "Unexpected content-type: %s", contentType);

		myReader = new InputStreamReader(new ByteArrayInputStream(response.getContent()), StandardCharsets.UTF_8);
		CSVParser csvParser = new CSVParser(myReader, HfqlRestClient.CSV_FORMAT);
		myIterator = csvParser.iterator();
		readHeaderRows(true);
	}

	private void validateResponse() {
		Validate.isTrue(
				myRequest.getStatusLine().getStatusCode() == 200,
				"Server returned wrong status: %d",
				myRequest.getStatusLine().getStatusCode());
	}

	private void readHeaderRows(boolean theFirstPage) {
		// Protocol version
		CSVRecord protocolVersionRow = myIterator.next();
		String protocolVersion = protocolVersionRow.get(0);
		ValidateUtil.isTrueOrThrowInvalidRequest(
				PROTOCOL_VERSION.equals(protocolVersion),
				"Wrong protocol version, expected %s but got %s",
				PROTOCOL_VERSION,
				protocolVersion);

		// Search ID, Limit, Parsed Statement
		CSVRecord searchIdRow = myIterator.next();
		mySearchId = searchIdRow.get(0);
		myLimit = Integer.parseInt(searchIdRow.get(1));
		String statementJsonString = searchIdRow.get(2);
		if (theFirstPage && isNotBlank(statementJsonString)) {
			myStatement = JsonUtil.deserialize(statementJsonString, HfqlStatement.class);
		}
		myCurrentFetchCount = 0;
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
				switch (myStatement.getSelectClauses().get(i).getDataType()) {
					case STRING:
					case JSON:
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
						DateType dateType = new DateType();
						dateType.setValueAsString(existingValue);
						newValue = dateType.getValue();
						break;
					case TIMESTAMP:
						DateTimeType dateTimeType = new DateTimeType();
						dateTimeType.setValueAsString(existingValue);
						newValue = dateTimeType.getValue();
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
		HttpPost post = new HttpPost(myBaseUrl + "/" + HfqlConstants.HFQL_EXECUTE);
		Parameters input = new Parameters();
		input.addParameter(HfqlConstants.PARAM_ACTION, new CodeType(HfqlConstants.PARAM_ACTION_SEARCH_CONTINUATION));
		input.addParameter(HfqlConstants.PARAM_CONTINUATION, new StringType(mySearchId));
		input.addParameter(HfqlConstants.PARAM_OFFSET, new IntegerType(myLastRowNumber + 1));
		input.addParameter(HfqlConstants.PARAM_LIMIT, new IntegerType(myLimit));
		input.addParameter(HfqlConstants.PARAM_FETCH_SIZE, new IntegerType(myFetchSize));
		input.addParameter(HfqlConstants.PARAM_STATEMENT, new StringType(JsonUtil.serialize(myStatement, false)));
		post.setEntity(new ResourceEntity(FhirContext.forR4Cached(), input));
		try {
			myRequest = myClient.execute(post);
			validateResponse();
			myReader = new InputStreamReader(myRequest.getEntity().getContent(), StandardCharsets.UTF_8);
			CSVParser csvParser = new CSVParser(myReader, HfqlRestClient.CSV_FORMAT);
			myIterator = csvParser.iterator();
			readHeaderRows(false);
		} catch (IOException e) {
			throw new InternalErrorException(Msg.code(2399) + e.getMessage(), e);
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
	public HfqlStatement getStatement() {
		return myStatement;
	}
}
