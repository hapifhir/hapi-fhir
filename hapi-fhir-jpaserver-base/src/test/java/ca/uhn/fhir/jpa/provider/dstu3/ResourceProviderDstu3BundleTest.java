package ca.uhn.fhir.jpa.provider.dstu3;

import static org.junit.Assert.*;

import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Bundle.BundleType;
import org.hl7.fhir.dstu3.model.Composition;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.AfterClass;
import org.junit.Test;

import ca.uhn.fhir.util.TestUtil;

public class ResourceProviderDstu3BundleTest extends BaseResourceProviderDstu3Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResourceProviderDstu3BundleTest.class);

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}


	/**
	 * See #401
	 */
	@Test
	public void testBundlePreservesFullUrl() throws Exception {
		
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


}
