package ca.uhn.fhir.jpa.fql.jdbc;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.fql.executor.HfqlDataTypeEnum;
import ca.uhn.fhir.jpa.fql.executor.StaticHfqlExecutionResult;
import ca.uhn.fhir.jpa.fql.executor.IHfqlExecutor;
import ca.uhn.fhir.jpa.fql.executor.IHfqlExecutionResult;
import ca.uhn.fhir.jpa.fql.parser.HfqlStatement;
import ca.uhn.fhir.jpa.fql.provider.HfqlRestProvider;
import ca.uhn.fhir.jpa.fql.util.HfqlConstants;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.IntegerType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.Base64Utils;

import javax.annotation.Nonnull;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.in;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HfqlRestClientTest {
	private static final FhirContext ourCtx = FhirContext.forR4Cached();
	private static final String USERNAME = "some-username";
	private static final String PASSWORD = "some-password";
	private final HeaderCaptureInterceptor myHeaderCaptureInterceptor = new HeaderCaptureInterceptor();
	@Mock
	private IHfqlExecutor myFqlExecutor;
	@Mock
	private IHfqlExecutionResult myMockFqlResult0;
	@Mock
	private IHfqlExecutionResult myMockFqlResult1;
	@InjectMocks
	private HfqlRestProvider myProvider = new HfqlRestProvider();
	@RegisterExtension
	public RestfulServerExtension myServer = new RestfulServerExtension(ourCtx)
		.registerProvider(myProvider)
		.registerInterceptor(myHeaderCaptureInterceptor);
	@Captor
	private ArgumentCaptor<String> myStatementCaptor;
	@Captor
	private ArgumentCaptor<RequestDetails> myRequestDetailsCaptor;
	@Captor
	private ArgumentCaptor<Integer> myLimitCaptor;
	private HfqlRestClient myClient;

	@BeforeEach
	public void beforeEach() {
		myHeaderCaptureInterceptor.clear();
		myClient = new HfqlRestClient(myServer.getBaseUrl(), USERNAME, PASSWORD);
	}

	@AfterEach
	public void afterEach() {
		myClient.close();
	}


	@Test
	public void testExecuteSearchAndContinuation() throws SQLException {
		String sql = "from Patient select name.family, name.given where name.family = 'Simpson'";
		String searchId = "my-search-id";
		HfqlStatement statement = createFakeStatement();
		when(myMockFqlResult0.getStatement()).thenReturn(statement);
		when(myMockFqlResult0.getColumnNames()).thenReturn(List.of("name.family", "name.given"));
		when(myMockFqlResult0.getColumnTypes()).thenReturn(List.of(HfqlDataTypeEnum.STRING, HfqlDataTypeEnum.STRING));
		when(myMockFqlResult0.hasNext()).thenReturn(true, true, true);
		when(myMockFqlResult0.getNextRow()).thenReturn(
			new IHfqlExecutionResult.Row(0, List.of("Simpson", "Homer")),
			new IHfqlExecutionResult.Row(3, List.of("Simpson", "Marge")),
			// Fetch size is 2 so this one shouldn't get returned in the first pass
			new IHfqlExecutionResult.Row(5, List.of("Simpson", "Maggie"))
		);
		when(myMockFqlResult0.getSearchId()).thenReturn(searchId);
		when(myMockFqlResult0.getLimit()).thenReturn(123);
		when(myFqlExecutor.executeInitialSearch(eq(sql), any(), any())).thenReturn(myMockFqlResult0);

		when(myMockFqlResult1.getStatement()).thenReturn(statement);
		when(myMockFqlResult1.hasNext()).thenReturn(true, true, false);
		when(myMockFqlResult1.getNextRow()).thenReturn(
			new IHfqlExecutionResult.Row(5, List.of("Simpson", "Maggie")),
			new IHfqlExecutionResult.Row(7, List.of("Simpson", "Lisa"))
		);
		when(myMockFqlResult1.getSearchId()).thenReturn(searchId);
		when(myMockFqlResult1.getLimit()).thenReturn(123);
		when(myFqlExecutor.executeContinuation(any(), eq(searchId), eq(4), eq(123), any())).thenReturn(myMockFqlResult1);
		when(myFqlExecutor.executeContinuation(any(), eq(searchId), eq(8), eq(123), any())).thenReturn(new StaticHfqlExecutionResult(searchId));

		myClient.setFetchSize(2);
		Parameters input = new Parameters();
		input.addParameter(HfqlConstants.PARAM_ACTION, new CodeType(HfqlConstants.PARAM_ACTION_SEARCH));
		input.addParameter(HfqlConstants.PARAM_QUERY, new StringType(sql));
		input.addParameter(HfqlConstants.PARAM_LIMIT, new IntegerType(123));
		input.addParameter(HfqlConstants.PARAM_FETCH_SIZE, new IntegerType(2));

		IHfqlExecutionResult result = myClient.execute(input, true);
		IHfqlExecutionResult.Row nextRow;
		assertThat(result.getColumnNames().toString(), result.getColumnNames(), contains("name.family", "name.given"));
		assertTrue(result.hasNext());
		nextRow = result.getNextRow();
		assertEquals(0, nextRow.getRowOffset());
		assertThat(nextRow.getRowValues(), contains("Simpson", "Homer"));
		assertTrue(result.hasNext());
		nextRow = result.getNextRow();
		assertEquals(3, nextRow.getRowOffset());
		assertThat(nextRow.getRowValues(), contains("Simpson", "Marge"));
		assertTrue(result.hasNext());
		nextRow = result.getNextRow();
		assertEquals(5, nextRow.getRowOffset());
		assertThat(nextRow.getRowValues(), contains("Simpson", "Maggie"));
		assertTrue(result.hasNext());
		nextRow = result.getNextRow();
		assertEquals(7, nextRow.getRowOffset());
		assertThat(nextRow.getRowValues(), contains("Simpson", "Lisa"));
		assertFalse(result.hasNext());

		verify(myFqlExecutor, times(1)).executeInitialSearch(myStatementCaptor.capture(), myLimitCaptor.capture(), myRequestDetailsCaptor.capture());
		assertEquals(sql, myStatementCaptor.getValue());
		String expectedAuthHeader = Constants.HEADER_AUTHORIZATION_VALPREFIX_BASIC + Base64Utils.encodeToString((USERNAME + ":" + PASSWORD).getBytes(StandardCharsets.UTF_8));


		String actual = myHeaderCaptureInterceptor.getCapturedHeaders().get(0).get(Constants.HEADER_AUTHORIZATION).get(0);
		assertEquals(expectedAuthHeader, actual);
		assertEquals(123, myLimitCaptor.getValue().intValue());
	}

	@Nonnull
	public static HfqlStatement createFakeStatement() {
		HfqlStatement statement = new HfqlStatement();
		statement.setFromResourceName("Patient");
		statement.addSelectClause("name.family");
		return statement;
	}

	@Interceptor
	public static class HeaderCaptureInterceptor {

		private final List<Map<String, List<String>>> myCapturedHeaders = new ArrayList<>();

		@Hook(Pointcut.SERVER_INCOMING_REQUEST_PRE_HANDLED)
		public void capture(ServletRequestDetails theServletRequestDetails) {
			myCapturedHeaders.add(theServletRequestDetails.getHeaders());
		}

		public void clear() {
			myCapturedHeaders.clear();
		}

		public List<Map<String, List<String>>> getCapturedHeaders() {
			return myCapturedHeaders;
		}

	}

}
