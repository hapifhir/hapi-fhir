package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.rest.api.Constants;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.blankString;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServletRequestTracingTest {

	MockHttpServletRequest myRequest = new MockHttpServletRequest();
	String myRequestIdResult;

	void run() {
		myRequestIdResult = ServletRequestTracing.getOrGenerateRequestId(myRequest);
	}

	@Test
	public void emptyRequestGetsGeneratedId() {
		// no setup

		run();

		// verify
		assertThat("id generated", myRequestIdResult, not(blankString()));
		assertEquals(myRequest.getAttribute(ServletRequestTracing.ATTRIBUTE_REQUEST_ID),myRequestIdResult);
	}

	@Test
	public void requestWithCallerHapiIdUsesThat() {
		// setup
		myRequest.addHeader(Constants.HEADER_REQUEST_ID, "a_request_id");

		run();

		// verify
		assertEquals("a_request_id", myRequestIdResult);
	}

	@Test
	public void duplicateCallsKeepsSameId() {
		// no headers

		myRequestIdResult = ServletRequestTracing.getOrGenerateRequestId(myRequest);

		String secondResult = ServletRequestTracing.getOrGenerateRequestId(myRequest);

		// verify
		assertThat("id generated", secondResult, not(blankString()));
		assertEquals(myRequestIdResult, secondResult);
	}

}
