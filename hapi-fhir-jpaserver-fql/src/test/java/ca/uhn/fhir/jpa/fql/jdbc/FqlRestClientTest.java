package ca.uhn.fhir.jpa.fql.jdbc;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.fql.executor.IFqlExecutor;
import ca.uhn.fhir.jpa.fql.executor.IFqlResult;
import ca.uhn.fhir.jpa.fql.parser.FqlStatement;
import ca.uhn.fhir.jpa.fql.provider.FqlRestProvider;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FqlRestClientTest {
	private static final FhirContext ourCtx = FhirContext.forR4Cached();
	private final HeaderCaptureInterceptor myHeaderCaptureInterceptor = new HeaderCaptureInterceptor();
	@Mock
	private IFqlExecutor myFqlExecutor;
	@Mock
	private IFqlResult myMockFqlResult0;
	@Mock
	private IFqlResult myMockFqlResult1;
	@InjectMocks
	private FqlRestProvider myProvider = new FqlRestProvider();
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

	@BeforeEach
	public void beforeEach() {
		myHeaderCaptureInterceptor.clear();
	}


	@Test
	public void testClient() throws SQLException {
		String sql = "from Patient select name.family, name.given where name.family = 'Simpson'";
		String searchId = "my-search-id";
		FqlStatement statement = createFakeStatement();
		when(myMockFqlResult0.getStatement()).thenReturn(statement);
		when(myMockFqlResult0.getColumnNames()).thenReturn(List.of("name.family", "name.given"));
		when(myMockFqlResult0.hasNext()).thenReturn(true, true, true);
		when(myMockFqlResult0.getNextRow()).thenReturn(
			new IFqlResult.Row(0, List.of("Simpson", "Homer")),
			new IFqlResult.Row(3, List.of("Simpson", "Marge")),
			// Fetch size is 2 so this one shouldn't get returned in the first pass
			new IFqlResult.Row(5, List.of("Simpson", "Maggie"))
		);
		when(myMockFqlResult0.getSearchId()).thenReturn(searchId);
		when(myMockFqlResult0.getLimit()).thenReturn(123);
		when(myFqlExecutor.executeInitialSearch(eq(sql), any(), any())).thenReturn(myMockFqlResult0);

		when(myMockFqlResult1.getStatement()).thenReturn(statement);
		when(myMockFqlResult1.hasNext()).thenReturn(true, true, false);
		when(myMockFqlResult1.getNextRow()).thenReturn(
			new IFqlResult.Row(5, List.of("Simpson", "Maggie")),
			new IFqlResult.Row(7, List.of("Simpson", "Lisa"))
		);
		when(myMockFqlResult1.getSearchId()).thenReturn(searchId);
		when(myMockFqlResult1.getLimit()).thenReturn(123);
		when(myFqlExecutor.executeContinuation(any(), eq(searchId), eq(4), eq(123), any())).thenReturn(myMockFqlResult1);
		when(myFqlExecutor.executeContinuation(any(), eq(searchId), eq(8), eq(123), any())).thenReturn(new EmptyFqlResult(searchId));

		String username = "some-username";
		String password = "some-password";

		FqlRestClient client = new FqlRestClient(myServer.getBaseUrl(), username, password);
		client.setFetchSize(2);
		IFqlResult result = client.execute(sql, 123);
		IFqlResult.Row nextRow;
		assertThat(result.getColumnNames().toString(), result.getColumnNames(), contains("name.family", "name.given"));
		assertTrue(result.hasNext());
		nextRow = result.getNextRow();
		assertEquals(0, nextRow.searchRowNumber());
		assertThat(nextRow.values(), contains("Simpson", "Homer"));
		assertTrue(result.hasNext());
		nextRow = result.getNextRow();
		assertEquals(3, nextRow.searchRowNumber());
		assertThat(nextRow.values(), contains("Simpson", "Marge"));
		assertTrue(result.hasNext());
		nextRow = result.getNextRow();
		assertEquals(5, nextRow.searchRowNumber());
		assertThat(nextRow.values(), contains("Simpson", "Maggie"));
		assertTrue(result.hasNext());
		nextRow = result.getNextRow();
		assertEquals(7, nextRow.searchRowNumber());
		assertThat(nextRow.values(), contains("Simpson", "Lisa"));
		assertFalse(result.hasNext());

		verify(myFqlExecutor, times(1)).executeInitialSearch(myStatementCaptor.capture(), myLimitCaptor.capture(), myRequestDetailsCaptor.capture());
		assertEquals(sql, myStatementCaptor.getValue());
		String expectedAuthHeader = Constants.HEADER_AUTHORIZATION_VALPREFIX_BASIC + Base64Utils.encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));


		String actual = myHeaderCaptureInterceptor.getCapturedHeaders().get(0).get(Constants.HEADER_AUTHORIZATION).get(0);
		assertEquals(expectedAuthHeader, actual);
		assertEquals(123, myLimitCaptor.getValue().intValue());
	}

	@Nonnull
	public static FqlStatement createFakeStatement() {
		FqlStatement statement = new FqlStatement();
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
