package ca.uhn.fhir.jpa.fql.provider;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.fql.executor.IFqlExecutor;
import ca.uhn.fhir.jpa.fql.executor.IFqlResult;
import ca.uhn.fhir.jpa.fql.jdbc.FqlRestClient;
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

import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.in;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.contains;

@SuppressWarnings("unchecked")
@ExtendWith(MockitoExtension.class)
public class FqlRestProviderTest {

	private static final FhirContext ourCtx = FhirContext.forR4Cached();

	@Mock
	private IFqlExecutor myFqlExecutor;
	@Mock
	private IFqlResult myMockFqlResult;
	@InjectMocks
	private FqlRestProvider myProvider = new FqlRestProvider();

	private HeaderCaptureInterceptor myHeaderCaptureInterceptor = new HeaderCaptureInterceptor();

	@RegisterExtension
	private RestfulServerExtension myServer = new RestfulServerExtension(ourCtx)
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
		when(myFqlExecutor.execute(any(), any(), any())).thenReturn(myMockFqlResult);
		when(myMockFqlResult.getColumnNames()).thenReturn(List.of("name.family", "name.given"));
		when(myMockFqlResult.hasNext()).thenReturn(true, true, false);
		when(myMockFqlResult.getNextRowAsStrings()).thenReturn(
			List.of("Simpson", "Homer"),
			List.of("Simpson", "Marge")
		);

		String sql = "from Patient select name.family, name.given";
		String username = "some-username";
		String password = "some-password";

		FqlRestClient client = new FqlRestClient(myServer.getBaseUrl(), username, password);
		IFqlResult result = client.execute(sql, 123);
		assertThat(result.getColumnNames(), contains("name.family", "name.given"));
		assertTrue(result.hasNext());
		assertThat(result.getNextRowAsStrings(), contains("Simpson", "Homer"));
		assertTrue(result.hasNext());
		assertThat(result.getNextRowAsStrings(), contains("Simpson", "Marge"));
		assertFalse(result.hasNext());

		verify(myFqlExecutor, times(1)).execute(myStatementCaptor.capture(), myLimitCaptor.capture(), myRequestDetailsCaptor.capture());
		assertEquals(sql, myStatementCaptor.getValue());
		String expectedAuthHeader = Constants.HEADER_AUTHORIZATION_VALPREFIX_BASIC + Base64Utils.encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));
		String actual = myHeaderCaptureInterceptor.getCapturedHeaders().get(0).get(Constants.HEADER_AUTHORIZATION).get(0);
		assertEquals(expectedAuthHeader, actual);
		assertEquals(123, myLimitCaptor.getValue().intValue());
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
