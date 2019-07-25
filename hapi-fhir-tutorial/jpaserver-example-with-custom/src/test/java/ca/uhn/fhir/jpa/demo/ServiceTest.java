package ca.uhn.fhir.jpa.demo;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class ServiceTest {

	private static FileSystemXmlApplicationContext ourAppCtx;

	@BeforeClass
	public static void beforeClass() {
		ourAppCtx = new FileSystemXmlApplicationContext(
			"src/test/resources/test-hapi-fhir-server-database-config.xml",
			"src/main/webapp/WEB-INF/hapi-fhir-server-config.xml",
			"src/main/webapp/WEB-INF/non-fhir-services-config.xml"
			);
		ourAppCtx.start();
	}
	
	@Test
	public void testSomething() {
		
	}

	@AfterClass
	public static void afterClass() {
		ourAppCtx.stop();
	}
}

