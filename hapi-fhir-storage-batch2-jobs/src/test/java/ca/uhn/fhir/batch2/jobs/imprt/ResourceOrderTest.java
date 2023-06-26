package ca.uhn.fhir.batch2.jobs.imprt;

import ca.uhn.fhir.context.FhirContext;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.lessThan;

class ResourceOrderTest {

	private static final Logger ourLog = LoggerFactory.getLogger(ResourceOrderTest.class);

	@Test
	public void testResourceOrder() {
		List<String> r4Order = ResourceOrderUtil.getResourceOrder(FhirContext.forR4Cached());
		ourLog.info("R4 Order: {}", r4Order);

		assertThat(r4Order.indexOf("Patient"), lessThan(r4Order.indexOf("Observation")));
		assertThat(r4Order.indexOf("Practitioner"), lessThan(r4Order.indexOf("Patient")));
	}

}
