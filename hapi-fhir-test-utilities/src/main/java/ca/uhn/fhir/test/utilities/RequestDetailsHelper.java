package ca.uhn.fhir.test.utilities;

import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import org.springframework.mock.web.MockHttpServletRequest;

public class RequestDetailsHelper {
	public static RequestDetails newServletRequestDetails() {
		ServletRequestDetails retval = new ServletRequestDetails();
		retval.setServletRequest(new MockHttpServletRequest());
		retval.setServer(new RestfulServer());
		return retval;
	}
}
