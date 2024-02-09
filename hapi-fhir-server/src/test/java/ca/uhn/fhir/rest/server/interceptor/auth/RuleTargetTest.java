package ca.uhn.fhir.rest.server.interceptor.auth;

import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class RuleTargetTest {

	@Test
	void setSearchParams() {
		RuleTarget target = new RuleTarget();
		RequestDetails requestDetails = new ServletRequestDetails();
		requestDetails.addParameter("subject:mdm", new String[]{"Patient/123"});
		requestDetails.addParameter("performer", new String[]{"Practioner/456"});
		target.setSearchParams(requestDetails);

		Map<String, String[]> storedParams = target.getSearchParams();
		assertThat(storedParams.keySet()).hasSize(2);
		assertThat(storedParams.get("subject")[0]).isEqualTo("Patient/123");
		assertThat(storedParams.get("performer")[0]).isEqualTo("Practioner/456");
	}
}
