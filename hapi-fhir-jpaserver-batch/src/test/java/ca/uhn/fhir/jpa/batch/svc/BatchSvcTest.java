package ca.uhn.fhir.jpa.batch.svc;

import ca.uhn.fhir.jpa.batch.BaseBatchR4Test;
import org.junit.Test;

public class BatchSvcTest extends BaseBatchR4Test {

	@Test
	public void testApplicationContextLoads(){
		myDummyService.test();
	}
}
