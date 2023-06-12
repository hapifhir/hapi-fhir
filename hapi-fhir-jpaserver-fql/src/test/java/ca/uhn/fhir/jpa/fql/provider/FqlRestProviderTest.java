package ca.uhn.fhir.jpa.fql.provider;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.fql.executor.IFqlExecutor;
import ca.uhn.fhir.jpa.fql.executor.IFqlResult;
import ca.uhn.fhir.jpa.fql.parser.FqlStatement;
import ca.uhn.fhir.jpa.fql.util.FqlConstants;
import ca.uhn.fhir.rest.client.apache.ResourceEntity;
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.JsonUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.hl7.fhir.r4.model.IntegerType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static ca.uhn.fhir.jpa.fql.jdbc.FqlRestClientTest.createFakeStatement;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FqlRestProviderTest {

	private static final FhirContext ourCtx = FhirContext.forR4Cached();
	@RegisterExtension
	public HttpClientExtension myHttpClient = new HttpClientExtension();
	@Mock
	private IFqlExecutor myFqlExecutor;
	@Mock
	private IFqlResult myMockFqlResult;
	@InjectMocks
	private FqlRestProvider myProvider = new FqlRestProvider();
	@RegisterExtension
	public RestfulServerExtension myServer = new RestfulServerExtension(ourCtx)
		.registerProvider(myProvider);
	@Captor
	private ArgumentCaptor<String> myStatementCaptor;
	@Captor
	private ArgumentCaptor<Integer> myLimitCaptor;
	@Captor
	private ArgumentCaptor<Integer> myOffsetCaptor;


	@Test
	public void testProviderFetchInitial() throws IOException {
		// Setup
		FqlStatement statement = createFakeStatement();
		when(myFqlExecutor.executeInitialSearch(any(), any(), any())).thenReturn(myMockFqlResult);
		when(myMockFqlResult.getStatement()).thenReturn(statement);
		when(myMockFqlResult.getColumnNames()).thenReturn(List.of("name.family", "name.given"));
		when(myMockFqlResult.hasNext()).thenReturn(true, true, false);
		when(myMockFqlResult.getNextRow()).thenReturn(
			new IFqlResult.Row(0, List.of("Simpson", "Homer")),
			new IFqlResult.Row(3, List.of("Simpson", "Marge"))
		);
		when(myMockFqlResult.getSearchId()).thenReturn("my-search-id");
		when(myMockFqlResult.getLimit()).thenReturn(999);

		String select = "from Patient select foo";
		Parameters request = new Parameters();
		request.addParameter(FqlRestProvider.PARAM_QUERY, new StringType(select));
		request.addParameter(FqlRestProvider.PARAM_LIMIT, new IntegerType(100));
		HttpPost fetch = new HttpPost(myServer.getBaseUrl() + "/" + FqlConstants.FQL_EXECUTE);
		fetch.setEntity(new ResourceEntity(ourCtx, request));

		// Test
		try (CloseableHttpResponse response = myHttpClient.execute(fetch)) {

			// Verify
			String outcome = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			String expected = """
				1
				my-search-id,999,"{""selectClauses"":[{""clause"":""name.family"",""alias"":""name.family""}],""whereClauses"":[],""searchClauses"":[],""fromResourceName"":""Patient""}"
				"",name.family,name.given
				0,Simpson,Homer
				3,Simpson,Marge
				""";
			assertEquals(expected.trim(), outcome.trim());
			assertEquals(200, response.getStatusLine().getStatusCode());
			assertThat(response.getEntity().getContentType().getValue(), startsWith("text/csv;"));

			verify(myFqlExecutor, times(1)).executeInitialSearch(myStatementCaptor.capture(), myLimitCaptor.capture(), notNull());
			assertEquals(select, myStatementCaptor.getValue());
			assertEquals(100, myLimitCaptor.getValue());
		}
	}


	@Test
	public void testProviderFetchContinuation() throws IOException {
		// Setup
		when(myFqlExecutor.executeContinuation(any(), any(), anyInt(), isNull(), any())).thenReturn(myMockFqlResult);
		when(myMockFqlResult.hasNext()).thenReturn(true, true, false);
		when(myMockFqlResult.getNextRow()).thenReturn(
			new IFqlResult.Row(4, List.of("Simpson", "Homer")),
			new IFqlResult.Row(6, List.of("Simpson", "Marge"))
		);
		when(myMockFqlResult.getSearchId()).thenReturn("my-search-id");
		when(myMockFqlResult.getLimit()).thenReturn(-1);

		String continuation = "the-continuation-id";
		Parameters request = new Parameters();
		request.addParameter(FqlRestProvider.PARAM_CONTINUATION, new StringType(continuation));
		request.addParameter(FqlRestProvider.PARAM_STATEMENT, new StringType(JsonUtil.serialize(createFakeStatement())));
		request.addParameter(FqlRestProvider.PARAM_OFFSET, new IntegerType(99));
		HttpPost fetch = new HttpPost(myServer.getBaseUrl() + "/" + FqlConstants.FQL_EXECUTE);
		fetch.setEntity(new ResourceEntity(ourCtx, request));

		// Test
		try (CloseableHttpResponse response = myHttpClient.execute(fetch)) {

			// Verify
			String outcome = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			String expected = """
				1
				my-search-id,-1,
				""
				4,Simpson,Homer
				6,Simpson,Marge
				""";
			assertEquals(expected.trim(), outcome.trim());
			assertEquals(200, response.getStatusLine().getStatusCode());
			assertThat(response.getEntity().getContentType().getValue(), startsWith("text/csv;"));

			verify(myFqlExecutor, times(1)).executeContinuation(any(), myStatementCaptor.capture(), myOffsetCaptor.capture(), myLimitCaptor.capture(), notNull());
			assertEquals(continuation, myStatementCaptor.getValue());
			assertEquals(null, myLimitCaptor.getValue());
			assertEquals(99, myOffsetCaptor.getValue());
		}
	}

}
