package ca.uhn.fhir.rest.server.interceptor.auth;

import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RuleTargetTest {

	@Test
	void setSearchParams() {
		RuleTarget target = new RuleTarget();
		RequestDetails requestDetails = new ServletRequestDetails();
		requestDetails.addParameter("subject:mdm", new String[]{"Patient/123"});
		requestDetails.addParameter("performer", new String[]{"Practioner/456"});
		target.setSearchParams(requestDetails);

		Map<String, String[]> storedParams = target.getSearchParams();
		assertThat(storedParams.keySet(), hasSize(2));
		assertEquals("Patient/123", storedParams.get("subject")[0]);
		assertEquals("Practioner/456", storedParams.get("performer")[0]);
	}
}
