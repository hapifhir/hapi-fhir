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
import ca.uhn.fhir.jpa.fql.executor.IFqlExecutionResult;
import ca.uhn.fhir.jpa.fql.parser.FqlStatement;
import ca.uhn.fhir.jpa.fql.provider.FqlRestProvider;
import ca.uhn.fhir.jpa.fql.util.FqlConstants;
import ca.uhn.fhir.rest.client.apache.ResourceEntity;
import ca.uhn.fhir.rest.client.impl.HttpBasicAuthInterceptor;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.IoUtil;
import ca.uhn.fhir.util.JsonUtil;
import ca.uhn.fhir.util.ValidateUtil;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.Validate;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
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
import java.util.concurrent.TimeUnit;

import static ca.uhn.fhir.jpa.fql.provider.FqlRestProvider.PROTOCOL_VERSION;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class FqlRestClient {
	public static final CSVFormat CSV_FORMAT = CSVFormat.DEFAULT
		.withRecordSeparator('\n');
	private final String myBaseUrl;
	private final String myUsername;
	private final String myPassword;
	private final CloseableHttpClient myClient;
	private int myFetchSize = 1000;

	public FqlRestClient(String theBaseUrl, String theUsername, String thePassword) {
		myBaseUrl = theBaseUrl;
		myUsername = theUsername;
		myPassword = thePassword;

		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
		connectionManager.setMaxTotal(99);
		connectionManager.setDefaultMaxPerRoute(99);
		HttpClientBuilder httpClientBuilder = HttpClientBuilder
			.create()
			.setConnectionManager(connectionManager)
			.setMaxConnPerRoute(99);
		if (isNotBlank(myUsername) && isNotBlank(myPassword)) {
			httpClientBuilder.addInterceptorLast(new HttpBasicAuthInterceptor(myUsername, myPassword));
		}
		myClient = httpClientBuilder.build();

	}

	/**
	 * Sets the number of results to fetch in a single page
	 *
	 * @param theFetchSize Must be a positive integer
	 */
	public void setFetchSize(int theFetchSize) {
		Validate.isTrue(theFetchSize > 0, "theFetchSize must be a positive integer");
		myFetchSize = theFetchSize;
	}

	public IFqlExecutionResult execute(String theSql, Integer theLimit) throws SQLException {
		return new FqlExecutionResult(theSql, theLimit);
	}

	private class FqlExecutionResult implements IFqlExecutionResult {
		private final List<String> myColumnNames = new ArrayList<>();
		private String mySearchId;
		private int myLimit;
		private InputStreamReader myReader;
		private Iterator<CSVRecord> myIterator;
		private int myCurrentFetchCount;
		private CloseableHttpResponse myRequest;
		private int myLastRowNumber;
		private boolean myExhausted;
		private FqlStatement myStatement;

		public FqlExecutionResult(String theSql, Integer theLimit) throws SQLException {
			HttpPost post = new HttpPost(myBaseUrl + "/" + FqlConstants.FQL_EXECUTE);
			Parameters input = new Parameters();
			input.addParameter(FqlRestProvider.PARAM_QUERY, new StringType(theSql));
			if (theLimit != null) {
				input.addParameter(FqlRestProvider.PARAM_LIMIT, new IntegerType(theLimit));
			}
			input.addParameter(FqlRestProvider.PARAM_FETCH_SIZE, new IntegerType(myFetchSize));
			post.setEntity(new ResourceEntity(FhirContext.forR4Cached(), input));
			try {
				myRequest = myClient.execute(post);
				validateResponse();
				myReader = new InputStreamReader(myRequest.getEntity().getContent(), StandardCharsets.UTF_8);
				CSVParser csvParser = new CSVParser(myReader, CSV_FORMAT);
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
			if (theFirstPage) {
				myStatement = JsonUtil.deserialize(searchIdRow.get(2), FqlStatement.class);
			}
			myCurrentFetchCount = 0;

			// Column Headers
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
		}

		@Override
		public List<String> getColumnNames() {
			return myColumnNames;
		}

		@Override
		public boolean hasNext() {
			if (myExhausted) {
				return false;
			}

			boolean hasNext = myIterator.hasNext();
			if (!hasNext) {
				close();

				HttpPost post = new HttpPost(myBaseUrl + "/" + FqlConstants.FQL_EXECUTE);
				Parameters input = new Parameters();
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
					CSVParser csvParser = new CSVParser(myReader, CSV_FORMAT);
					myIterator = csvParser.iterator();
					readHeaderRows(false);
				} catch (IOException e) {
					throw new InternalErrorException(e);
				}
				hasNext = myIterator.hasNext();
			}

			if (!hasNext && myCurrentFetchCount < myFetchSize) {
				myExhausted = true;
			}
			return hasNext;
		}

		@Override
		public Row getNextRow() {
			Validate.isTrue(!myExhausted, "Search is exhausted. This is a bug.");

			List<String> columnValues = new ArrayList<>();
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
			return new Row(myLastRowNumber, columnValues);
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
}
