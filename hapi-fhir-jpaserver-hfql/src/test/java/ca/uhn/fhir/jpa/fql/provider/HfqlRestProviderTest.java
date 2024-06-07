package ca.uhn.fhir.jpa.fql.provider;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.fql.executor.HfqlDataTypeEnum;
import ca.uhn.fhir.jpa.fql.executor.IHfqlExecutionResult;
import ca.uhn.fhir.jpa.fql.executor.IHfqlExecutor;
import ca.uhn.fhir.jpa.fql.parser.HfqlStatement;
import ca.uhn.fhir.jpa.fql.util.HfqlConstants;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static ca.uhn.fhir.jpa.fql.jdbc.HfqlRestClientTest.createFakeStatement;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.isNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HfqlRestProviderTest {

	private static final FhirContext ourCtx = FhirContext.forR4Cached();
	private static final Logger ourLog = LoggerFactory.getLogger(HfqlRestProviderTest.class);
	@RegisterExtension
	public HttpClientExtension myHttpClient = new HttpClientExtension();
	@Mock
	private IHfqlExecutor myFqlExecutor;
	@Mock
	private IHfqlExecutionResult myMockFqlResult;
	@InjectMocks
	private HfqlRestProvider myProvider = new HfqlRestProvider();
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
		HfqlStatement statement = createFakeStatement();
		when(myFqlExecutor.executeInitialSearch(any(), any(), any())).thenReturn(myMockFqlResult);
		when(myMockFqlResult.getStatement()).thenReturn(statement);
		when(myMockFqlResult.hasNext()).thenReturn(true, true, false);
		when(myMockFqlResult.getNextRow()).thenReturn(
			new IHfqlExecutionResult.Row(0, List.of("Simpson", "Homer")),
			new IHfqlExecutionResult.Row(3, List.of("Simpson", "Marge"))
		);
		when(myMockFqlResult.getSearchId()).thenReturn("my-search-id");
		when(myMockFqlResult.getLimit()).thenReturn(999);

		String select = "from Patient select foo";
		Parameters request = new Parameters();
		request.addParameter(HfqlConstants.PARAM_ACTION, new CodeType(HfqlConstants.PARAM_ACTION_SEARCH));
		request.addParameter(HfqlConstants.PARAM_QUERY, new StringType(select));
		request.addParameter(HfqlConstants.PARAM_LIMIT, new IntegerType(100));
		HttpPost fetch = new HttpPost(myServer.getBaseUrl() + "/" + HfqlConstants.HFQL_EXECUTE);
		fetch.setEntity(new ResourceEntity(ourCtx, request));

		// Test
		try (CloseableHttpResponse response = myHttpClient.execute(fetch)) {

			// Verify
			String outcome = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			String expected = """
				1,HAPI FHIR THE-VERSION
				my-search-id,999,"{""select"":[{""clause"":""name[0].family"",""alias"":""name[0].family"",""operator"":""SELECT"",""dataType"":""STRING""},{""clause"":""name[0].given[0]"",""alias"":""name[0].given[0]"",""operator"":""SELECT"",""dataType"":""STRING""}],""fromResourceName"":""Patient""}"
				0,Simpson,Homer
				3,Simpson,Marge
				""".replace("THE-VERSION", VersionUtil.getVersion());
			assertEquals(expected.trim(), outcome.trim());
			assertEquals(200, response.getStatusLine().getStatusCode());
			assertThat(response.getEntity().getContentType().getValue()).startsWith("text/csv;");

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
			new IHfqlExecutionResult.Row(4, List.of("Simpson", "Homer")),
			new IHfqlExecutionResult.Row(6, List.of("Simpson", "Marge"))
		);
		when(myMockFqlResult.getSearchId()).thenReturn("my-search-id");
		when(myMockFqlResult.getLimit()).thenReturn(-1);

		String continuation = "the-continuation-id";
		Parameters request = new Parameters();
		request.addParameter(HfqlConstants.PARAM_ACTION, new CodeType(HfqlConstants.PARAM_ACTION_SEARCH_CONTINUATION));
		request.addParameter(HfqlConstants.PARAM_CONTINUATION, new StringType(continuation));
		request.addParameter(HfqlConstants.PARAM_STATEMENT, new StringType(JsonUtil.serialize(createFakeStatement())));
		request.addParameter(HfqlConstants.PARAM_OFFSET, new IntegerType(99));
		ourLog.info("Request: {}", ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(request));
		HttpPost fetch = new HttpPost(myServer.getBaseUrl() + "/" + HfqlConstants.HFQL_EXECUTE);
		fetch.setEntity(new ResourceEntity(ourCtx, request));

		// Test
		try (CloseableHttpResponse response = myHttpClient.execute(fetch)) {

			// Verify
			String outcome = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			String expected = """
				1,HAPI FHIR THE-VERSION
				my-search-id,-1,
				4,Simpson,Homer
				6,Simpson,Marge
				""".replace("THE-VERSION", VersionUtil.getVersion());
			assertEquals(expected.trim(), outcome.trim());
			assertEquals(200, response.getStatusLine().getStatusCode());
			assertThat(response.getEntity().getContentType().getValue()).startsWith("text/csv;");

			verify(myFqlExecutor, times(1)).executeContinuation(any(), myStatementCaptor.capture(), myOffsetCaptor.capture(), myLimitCaptor.capture(), notNull());
			assertEquals(continuation, myStatementCaptor.getValue());
			assertNull(myLimitCaptor.getValue());
			assertEquals(99, myOffsetCaptor.getValue());
		}
	}


	@Test
	public void testIntrospectTables() throws IOException {
		// Setup
		when(myFqlExecutor.introspectTables()).thenReturn(myMockFqlResult);
		when(myMockFqlResult.hasNext()).thenReturn(true, true, false);
		HfqlStatement statement = new HfqlStatement();
		statement.addSelectClauseAndAlias("TABLE_NAME").setDataType(HfqlDataTypeEnum.STRING);
		when(myMockFqlResult.getStatement()).thenReturn(statement);
		when(myMockFqlResult.getNextRow()).thenReturn(
			new IHfqlExecutionResult.Row(0, List.of("Account")),
			new IHfqlExecutionResult.Row(6, List.of("Patient"))
		);
		when(myMockFqlResult.getSearchId()).thenReturn(null);
		when(myMockFqlResult.getLimit()).thenReturn(-1);

		Parameters request = new Parameters();
		request.addParameter(HfqlConstants.PARAM_ACTION, new CodeType(HfqlConstants.PARAM_ACTION_INTROSPECT_TABLES));
		HttpPost fetch = new HttpPost(myServer.getBaseUrl() + "/" + HfqlConstants.HFQL_EXECUTE);
		fetch.setEntity(new ResourceEntity(ourCtx, request));

		// Test
		try (CloseableHttpResponse response = myHttpClient.execute(fetch)) {

			// Verify
			String outcome = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			String expected = """
				1,HAPI FHIR THE-VERSION
				,-1,"{""select"":[{""clause"":""TABLE_NAME"",""alias"":""TABLE_NAME"",""operator"":""SELECT"",""dataType"":""STRING""}]}"
				0,Account
				6,Patient
				""".replace("THE-VERSION", VersionUtil.getVersion());
			assertEquals(expected.trim(), outcome.trim());
			assertEquals(200, response.getStatusLine().getStatusCode());
			assertThat(response.getEntity().getContentType().getValue()).startsWith("text/csv;");
		}

	}


	@Test
	public void testIntrospectColumns() throws IOException {
		// Setup
		when(myFqlExecutor.introspectColumns(eq("FOO"), eq("BAR"))).thenReturn(myMockFqlResult);
		when(myMockFqlResult.hasNext()).thenReturn(true, true, false);
		HfqlStatement statement = new HfqlStatement();
		statement.addSelectClauseAndAlias("COLUMN_NAME").setDataType(HfqlDataTypeEnum.STRING);
		when(myMockFqlResult.getStatement()).thenReturn(statement);
		when(myMockFqlResult.getNextRow()).thenReturn(
			new IHfqlExecutionResult.Row(0, List.of("FOO")),
			new IHfqlExecutionResult.Row(6, List.of("BAR"))
		);
		when(myMockFqlResult.getSearchId()).thenReturn(null);
		when(myMockFqlResult.getLimit()).thenReturn(-1);

		Parameters request = new Parameters();
		request.addParameter(HfqlConstants.PARAM_ACTION, new CodeType(HfqlConstants.PARAM_ACTION_INTROSPECT_COLUMNS));
		request.addParameter(HfqlConstants.PARAM_INTROSPECT_TABLE_NAME, new StringType("FOO"));
		request.addParameter(HfqlConstants.PARAM_INTROSPECT_COLUMN_NAME, new StringType("BAR"));
		HttpPost fetch = new HttpPost(myServer.getBaseUrl() + "/" + HfqlConstants.HFQL_EXECUTE);
		fetch.setEntity(new ResourceEntity(ourCtx, request));

		// Test
		try (CloseableHttpResponse response = myHttpClient.execute(fetch)) {

			// Verify
			String outcome = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			String expected = """
				1,HAPI FHIR THE-VERSION
				,-1,"{""select"":[{""clause"":""COLUMN_NAME"",""alias"":""COLUMN_NAME"",""operator"":""SELECT"",""dataType"":""STRING""}]}"
				0,FOO
				6,BAR
				""".replace("THE-VERSION", VersionUtil.getVersion());
			assertEquals(expected.trim(), outcome.trim());
			assertEquals(200, response.getStatusLine().getStatusCode());
			assertThat(response.getEntity().getContentType().getValue()).startsWith("text/csv;");
		}

	}

}
