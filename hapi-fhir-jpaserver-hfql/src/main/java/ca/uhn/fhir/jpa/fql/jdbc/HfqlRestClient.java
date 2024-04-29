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

import ca.uhn.fhir.jpa.fql.executor.IHfqlExecutionResult;
import ca.uhn.fhir.jpa.fql.util.HfqlConstants;
import ca.uhn.fhir.rest.client.impl.HttpBasicAuthInterceptor;
import ca.uhn.fhir.util.IoUtil;
import jakarta.annotation.Nonnull;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.lang3.Validate;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.IntegerType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.StringType;

import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import static ca.uhn.fhir.jpa.fql.util.HfqlConstants.DEFAULT_FETCH_SIZE;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * This is the HTTP/REST client used by the JDBC driver to talk to the FHIR server.
 * We don't use the HAPI FHIR REST client even though we're talking to a HAPI FHIR
 * REST server because the operation we're calling returns CSV data instead of
 * FHIR data. Instead, we just use the Apache HTTPClient.
 * <p>
 * Ideally in the future I'd like to explore using JDK primitives instead of even
 * using the Apache client or HAPI FHIR in order to reduce the dependencies required
 * in the JDBC driver, but that can be a problem for the future.
 */
public class HfqlRestClient {
	public static final CSVFormat CSV_FORMAT = CSVFormat.DEFAULT.withRecordSeparator('\n');
	private final String myBaseUrl;
	private final CloseableHttpClient myClient;

	public HfqlRestClient(String theBaseUrl, String theUsername, String thePassword) {
		myBaseUrl = theBaseUrl;

		PoolingHttpClientConnectionManager connectionManager =
				new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
		connectionManager.setMaxTotal(99);
		connectionManager.setDefaultMaxPerRoute(99);
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create()
				.setConnectionManager(connectionManager)
				.setMaxConnPerRoute(99);
		if (isNotBlank(theUsername) && isNotBlank(thePassword)) {
			httpClientBuilder.addInterceptorLast(new HttpBasicAuthInterceptor(theUsername, thePassword));
		}
		myClient = httpClientBuilder.build();
	}

	@Nonnull
	public static Parameters newQueryRequestParameters(String sql, Integer limit, int fetchSize) {
		Parameters input = new Parameters();
		input.addParameter(HfqlConstants.PARAM_ACTION, new CodeType(HfqlConstants.PARAM_ACTION_SEARCH));
		input.addParameter(HfqlConstants.PARAM_QUERY, new StringType(sql));
		if (limit != null) {
			input.addParameter(HfqlConstants.PARAM_LIMIT, new IntegerType(limit));
		}
		input.addParameter(HfqlConstants.PARAM_FETCH_SIZE, new IntegerType(fetchSize));
		return input;
	}

	public IHfqlExecutionResult execute(
			Parameters theRequestParameters, boolean theSupportsContinuations, Integer theFetchSize)
			throws SQLException {
		Integer fetchSize = theFetchSize;
		fetchSize = defaultIfNull(fetchSize, DEFAULT_FETCH_SIZE);
		Validate.isTrue(fetchSize > 0, "theFetchSize must be a positive integer, got: %s", fetchSize);
		return new RemoteHfqlExecutionResult(
				theRequestParameters, myBaseUrl, myClient, fetchSize, theSupportsContinuations);
	}

	public void close() {
		IoUtil.closeQuietly(myClient);
	}
}
