package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.jpa.util.JpaConstants;
import ca.uhn.fhir.rest.server.exceptions.NotImplementedOperationException;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.r4.model.MessageHeader;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleType;
import org.hl7.fhir.r4.model.Composition;
import org.hl7.fhir.r4.model.Parameters;
import org.junit.AfterClass;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;

public class ResourceProviderR4BundleTest extends BaseResourceProviderR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResourceProviderR4BundleTest.class);

	/**
	 * See #401
	 */
	@Test
	public void testBundlePreservesFullUrl() {

		Bundle bundle = new Bundle();
		bundle.setType(BundleType.DOCUMENT);

		Composition composition = new Composition();
		composition.setTitle("Visit Summary");
		bundle.addEntry().setFullUrl("http://foo").setResource(composition);

		IIdType id = ourClient.create().resource(bundle).execute().getId();

		Bundle retBundle = ourClient.read().resource(Bundle.class).withId(id).execute();

    ourLog.info(myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(retBundle));

		assertEquals("http://foo", bundle.getEntry().get(0).getFullUrl());
	}

	@Test
	public void testProcessMessage() {

		Bundle bundle = new Bundle();
		bundle.setType(BundleType.MESSAGE);

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
