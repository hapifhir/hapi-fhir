package z;

import org.junit.AfterClass;
import org.junit.Ignore;
import org.junit.Test;

import ca.uhn.fhir.util.TestUtil;

public class TestTest {

	@Test
	@Ignore
	public void testId() throws InterruptedException {
		Thread.sleep(1000000);
	}
	

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}


	
}
