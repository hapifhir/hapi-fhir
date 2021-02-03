package ca.uhn.fhir.jpa.search.lastn;

import org.junit.jupiter.api.Test;

import java.io.IOException;

public class ElasticsearchSvcImplTest {

	/**
	 * No exception should be thrown if the client closes before it's ever used
	 */
	@Test
	public void testClose() throws IOException {
		ElasticsearchSvcImpl svc = new ElasticsearchSvcImpl("localhost", 0, "user", "pass");
		svc.close();
	}

}
