package ca.uhn.fhir.jpa.provider.dstu3;

import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.rest.server.exceptions.NotImplementedOperationException;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Bundle.BundleType;
import org.hl7.fhir.dstu3.model.Composition;
import org.hl7.fhir.dstu3.model.Parameters;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.AfterClass;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;

public class ResourceProviderDstu3BundleTest extends BaseResourceProviderDstu3Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResourceProviderDstu3BundleTest.class);

	@Test
	public void testProcessMessage() {

		Bundle bundle = new Bundle();
		bundle.setType(Bundle.BundleType.MESSAGE);

		Parameters parameters = new Parameters();
		parameters.addParameter()
			.setName("content")
			.setResource(bundle);
		try {
			ourClient.operation().onServer().named(JpaConstants.OPERATION_PROCESS_MESSAGE).withParameters(parameters).execute();
			fail();
		} catch (NotImplementedOperationException e) {
			assertThat(e.getMessage(), containsString("This operation is not yet implemented on this server"));
		}

	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}


}
