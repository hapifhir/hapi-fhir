package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.rest.api.Constants;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;

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
		assertThat(myRequestIdResult).as("id generated").isNotBlank();
		assertThat(myRequestIdResult).isEqualTo(myRequest.getAttribute(ServletRequestTracing.ATTRIBUTE_REQUEST_ID));
	}

	@Test
	public void requestWithCallerHapiIdUsesThat() {
		// setup
		myRequest.addHeader(Constants.HEADER_REQUEST_ID, "a_request_id");

		run();

		// verify
		assertThat(myRequestIdResult).isEqualTo("a_request_id");
	}

	@Test
	public void duplicateCallsKeepsSameId() {
		// no headers

		myRequestIdResult = ServletRequestTracing.getOrGenerateRequestId(myRequest);

		String secondResult = ServletRequestTracing.getOrGenerateRequestId(myRequest);

		// verify
		assertThat(secondResult).as("id generated").isNotBlank();
		assertThat(secondResult).isEqualTo(myRequestIdResult);
	}

}
