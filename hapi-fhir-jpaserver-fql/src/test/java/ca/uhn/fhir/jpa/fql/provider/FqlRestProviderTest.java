package ca.uhn.fhir.jpa.fql.provider;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.fql.executor.IFqlExecutor;
import ca.uhn.fhir.jpa.fql.executor.IFqlExecutionResult;
import ca.uhn.fhir.jpa.fql.parser.FqlStatement;
import ca.uhn.fhir.jpa.fql.util.FqlConstants;
import ca.uhn.fhir.rest.client.apache.ResourceEntity;
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.JsonUtil;
import ca.uhn.fhir.util.VersionUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.hl7.fhir.r4.model.CodeType;
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
	private IFqlExecutionResult myMockFqlResult;
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
	public void testExecuteInitialSearch() throws IOException {
		// Setup
		FqlStatement statement = createFakeStatement();
		when(myFqlExecutor.executeInitialSearch(any(), any(), any())).thenReturn(myMockFqlResult);
		when(myMockFqlResult.getStatement()).thenReturn(statement);
		when(myMockFqlResult.getColumnNames()).thenReturn(List.of("name.family", "name.given"));
		when(myMockFqlResult.getColumnTypes()).thenReturn(List.of(IFqlExecutionResult.DataTypeEnum.STRING, IFqlExecutionResult.DataTypeEnum.STRING));
		when(myMockFqlResult.hasNext()).thenReturn(true, true, false);
		when(myMockFqlResult.getNextRow()).thenReturn(
			new IFqlExecutionResult.Row(0, List.of("Simpson", "Homer")),
			new IFqlExecutionResult.Row(3, List.of("Simpson", "Marge"))
		);
		when(myMockFqlResult.getSearchId()).thenReturn("my-search-id");
		when(myMockFqlResult.getLimit()).thenReturn(999);

		String select = "from Patient select foo";
		Parameters request = new Parameters();
		request.addParameter(FqlRestProvider.PARAM_ACTION, new CodeType(FqlRestProvider.PARAM_ACTION_SEARCH));
		request.addParameter(FqlRestProvider.PARAM_QUERY, new StringType(select));
		request.addParameter(FqlRestProvider.PARAM_LIMIT, new IntegerType(100));
		HttpPost fetch = new HttpPost(myServer.getBaseUrl() + "/" + FqlConstants.FQL_EXECUTE);
		fetch.setEntity(new ResourceEntity(ourCtx, request));

		// Test
		try (CloseableHttpResponse response = myHttpClient.execute(fetch)) {

			// Verify
			String outcome = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			String expected = """
				1,HAPI FHIR THE-VERSION
				my-search-id,999,"{""selectClauses"":[{""clause"":""name.family"",""alias"":""name.family""}],""fromResourceName"":""Patient""}"
				"",name.family,name.given
				"",STRING,STRING
				0,Simpson,Homer
				3,Simpson,Marge
				""".replace("THE-VERSION", VersionUtil.getVersion());
			assertEquals(expected.trim(), outcome.trim());
			assertEquals(200, response.getStatusLine().getStatusCode());
			assertThat(response.getEntity().getContentType().getValue(), startsWith("text/csv;"));

			verify(myFqlExecutor, times(1)).executeInitialSearch(myStatementCaptor.capture(), myLimitCaptor.capture(), notNull());
			assertEquals(select, myStatementCaptor.getValue());
			assertEquals(100, myLimitCaptor.getValue());
		}
	}


	@Test
	public void testExecuteContinuation() throws IOException {
		// Setup
		when(myFqlExecutor.executeContinuation(any(), any(), anyInt(), isNull(), any())).thenReturn(myMockFqlResult);
		when(myMockFqlResult.hasNext()).thenReturn(true, true, false);
		when(myMockFqlResult.getNextRow()).thenReturn(
			new IFqlExecutionResult.Row(4, List.of("Simpson", "Homer")),
			new IFqlExecutionResult.Row(6, List.of("Simpson", "Marge"))
		);
		when(myMockFqlResult.getSearchId()).thenReturn("my-search-id");
		when(myMockFqlResult.getLimit()).thenReturn(-1);

		String continuation = "the-continuation-id";
		Parameters request = new Parameters();
		request.addParameter(FqlRestProvider.PARAM_ACTION, new CodeType(FqlRestProvider.PARAM_ACTION_SEARCH_CONTINUATION));
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
				1,HAPI FHIR THE-VERSION
				my-search-id,-1,
				""
				""
				4,Simpson,Homer
				6,Simpson,Marge
				""".replace("THE-VERSION", VersionUtil.getVersion());
			assertEquals(expected.trim(), outcome.trim());
			assertEquals(200, response.getStatusLine().getStatusCode());
			assertThat(response.getEntity().getContentType().getValue(), startsWith("text/csv;"));

			verify(myFqlExecutor, times(1)).executeContinuation(any(), myStatementCaptor.capture(), myOffsetCaptor.capture(), myLimitCaptor.capture(), notNull());
			assertEquals(continuation, myStatementCaptor.getValue());
			assertEquals(null, myLimitCaptor.getValue());
			assertEquals(99, myOffsetCaptor.getValue());
		}
	}


	@Test
	public void testIntrospectTables() throws IOException {
		// Setup
		when(myFqlExecutor.introspectTables()).thenReturn(myMockFqlResult);
		when(myMockFqlResult.hasNext()).thenReturn(true, true, false);
		when(myMockFqlResult.getColumnNames()).thenReturn(List.of("TABLE_NAME"));
		when(myMockFqlResult.getColumnTypes()).thenReturn(List.of(IFqlExecutionResult.DataTypeEnum.STRING));
		when(myMockFqlResult.getNextRow()).thenReturn(
			new IFqlExecutionResult.Row(0, List.of("Account")),
			new IFqlExecutionResult.Row(6, List.of("Patient"))
		);
		when(myMockFqlResult.getSearchId()).thenReturn(null);
		when(myMockFqlResult.getLimit()).thenReturn(-1);

		Parameters request = new Parameters();
		request.addParameter(FqlRestProvider.PARAM_ACTION, new CodeType(FqlRestProvider.PARAM_ACTION_INTROSPECT_TABLES));
		HttpPost fetch = new HttpPost(myServer.getBaseUrl() + "/" + FqlConstants.FQL_EXECUTE);
		fetch.setEntity(new ResourceEntity(ourCtx, request));

		// Test
		try (CloseableHttpResponse response = myHttpClient.execute(fetch)) {

			// Verify
			String outcome = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			String expected = """
				1,HAPI FHIR THE-VERSION
				,-1,
				"",TABLE_NAME
				"",STRING
				0,Account
				6,Patient
				""".replace("THE-VERSION", VersionUtil.getVersion());
			assertEquals(expected.trim(), outcome.trim());
			assertEquals(200, response.getStatusLine().getStatusCode());
			assertThat(response.getEntity().getContentType().getValue(), startsWith("text/csv;"));
		}

	}


	@Test
	public void testIntrospectColumns() throws IOException {
		// Setup
		when(myFqlExecutor.introspectColumns(eq("FOO"),eq("BAR"))).thenReturn(myMockFqlResult);
		when(myMockFqlResult.hasNext()).thenReturn(true, true, false);
		when(myMockFqlResult.getColumnNames()).thenReturn(List.of("COLUMN_NAME"));
		when(myMockFqlResult.getColumnTypes()).thenReturn(List.of(IFqlExecutionResult.DataTypeEnum.STRING));
		when(myMockFqlResult.getNextRow()).thenReturn(
			new IFqlExecutionResult.Row(0, List.of("FOO")),
			new IFqlExecutionResult.Row(6, List.of("BAR"))
		);
		when(myMockFqlResult.getSearchId()).thenReturn(null);
		when(myMockFqlResult.getLimit()).thenReturn(-1);

		Parameters request = new Parameters();
		request.addParameter(FqlRestProvider.PARAM_ACTION, new CodeType(FqlRestProvider.PARAM_ACTION_INTROSPECT_COLUMNS));
		request.addParameter(FqlRestProvider.PARAM_INTROSPECT_TABLE_NAME, new StringType("FOO"));
		request.addParameter(FqlRestProvider.PARAM_INTROSPECT_COLUMN_NAME, new StringType("BAR"));
		HttpPost fetch = new HttpPost(myServer.getBaseUrl() + "/" + FqlConstants.FQL_EXECUTE);
		fetch.setEntity(new ResourceEntity(ourCtx, request));

		// Test
		try (CloseableHttpResponse response = myHttpClient.execute(fetch)) {

			// Verify
			String outcome = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			String expected = """
				1,HAPI FHIR THE-VERSION
				,-1,
				"",COLUMN_NAME
				"",STRING
				0,FOO
				6,BAR
				""".replace("THE-VERSION", VersionUtil.getVersion());
			assertEquals(expected.trim(), outcome.trim());
			assertEquals(200, response.getStatusLine().getStatusCode());
			assertThat(response.getEntity().getContentType().getValue(), startsWith("text/csv;"));
		}

	}

}
