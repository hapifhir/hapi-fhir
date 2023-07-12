/*-
 * #%L
 * HAPI FHIR JPA Server - HFQL Driver
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

import ca.uhn.fhir.jpa.fql.executor.IHfqlExecutionResult;
import ca.uhn.fhir.rest.client.impl.HttpBasicAuthInterceptor;
import ca.uhn.fhir.util.IoUtil;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.lang3.Validate;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.hl7.fhir.r4.model.Parameters;

import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import static ca.uhn.fhir.jpa.fql.util.HfqlConstants.DEFAULT_FETCH_SIZE;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class HfqlRestClient {
	public static final CSVFormat CSV_FORMAT = CSVFormat.DEFAULT
		.withRecordSeparator('\n');
	private final String myBaseUrl;
	private final String myUsername;
	private final String myPassword;
	private final CloseableHttpClient myClient;

	public HfqlRestClient(String theBaseUrl, String theUsername, String thePassword) {
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

	public IHfqlExecutionResult execute(Parameters theRequestParameters, boolean theSupportsContinuations, Integer theFetchSize) throws SQLException {
		Integer fetchSize = theFetchSize;
		fetchSize = defaultIfNull(fetchSize, DEFAULT_FETCH_SIZE);
		Validate.isTrue(fetchSize > 0, "theFetchSize must be a positive integer, got: %s", fetchSize);
		return new RemoteHfqlExecutionResult(theRequestParameters, myBaseUrl, myClient, fetchSize, theSupportsContinuations);
	}

	public void close() {
		IoUtil.closeQuietly(myClient);
	}
}
