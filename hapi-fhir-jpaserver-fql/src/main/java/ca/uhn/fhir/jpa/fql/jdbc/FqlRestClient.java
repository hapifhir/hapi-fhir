package ca.uhn.fhir.jpa.fql.jdbc;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.fql.executor.IFqlResult;
import ca.uhn.fhir.jpa.fql.provider.FqlRestProvider;
import ca.uhn.fhir.jpa.fql.util.FqlConstants;
import ca.uhn.fhir.rest.client.apache.ResourceEntity;
import ca.uhn.fhir.rest.client.impl.HttpBasicAuthInterceptor;
import ca.uhn.fhir.util.IoUtil;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
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

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class FqlRestClient {
	public static final CSVFormat CSV_FORMAT = CSVFormat.DEFAULT;
	private final String myBaseUrl;
	private final String myUsername;
	private final String myPassword;
	private final CloseableHttpClient myClient;

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

	public IFqlResult execute(String theSql, Integer theLimit) throws SQLException {
		return new FqlResult(theSql, theLimit);
	}

	private class FqlResult implements IFqlResult {
		private CloseableHttpResponse myRequest;
		private final InputStreamReader myReader;
		private final Iterator<CSVRecord> myIterator;
		private final List<String> myColumnNames = new ArrayList<>();

		public FqlResult(String theSql, Integer theLimit) throws SQLException {
			HttpPost post = new HttpPost(myBaseUrl + "/" + FqlConstants.FQL_EXECUTE);
			Parameters input = new Parameters();
			input.addParameter(FqlRestProvider.PARAM_QUERY, new StringType(theSql));
			if (theLimit != null) {
				input.addParameter(FqlRestProvider.PARAM_LIMIT, new IntegerType(theLimit));
			}
			post.setEntity(new ResourceEntity(FhirContext.forR4Cached(), input));
			try {
				myRequest = myClient.execute(post);
				myReader = new InputStreamReader(myRequest.getEntity().getContent(), StandardCharsets.UTF_8);
				CSVParser csvParser = new CSVParser(myReader, CSV_FORMAT);
				myIterator = csvParser.iterator();

				CSVRecord nextRecord = myIterator.next();
				for (String next : nextRecord) {
					myColumnNames.add(next);
				}

			} catch (IOException theE) {
				throw new SQLException(theE);
			}
		}

		@Override
		public List<String> getColumnNames() {
			return myColumnNames;
		}

		@Override
		public boolean hasNext() {
			return myIterator.hasNext();
		}

		@Override
		public List<String> getNextRowAsStrings() {
			List<String> retVal = new ArrayList<>();
			for (String next : myIterator.next()) {
				retVal.add(next);
			}
			return retVal;
		}

		@Override
		public boolean isClosed() {
			return myRequest == null;
		}

		@Override
		public void close() {
			IoUtil.closeQuietly(myRequest);
			myRequest = null;
		}
	}
}
