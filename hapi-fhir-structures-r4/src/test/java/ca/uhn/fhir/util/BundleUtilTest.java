package ca.uhn.fhir.util;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.r4.model.Bundle;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;

public class BundleUtilTest {

	private static FhirContext ourCtx = FhirContext.forR4();

	@Test
	public void testGetLink() {
		Bundle b = new Bundle();
		b.getLinkOrCreate("prev").setUrl("http://bar");
		b.getLinkOrCreate("next").setUrl("http://foo");
		Assert.assertEquals("http://foo", BundleUtil.getLinkUrlOfType(ourCtx, b, "next"));
	}

	@Test
	public void testGetLinkDoesntExist() {
		Bundle b = new Bundle();
		b.getLinkOrCreate("prev").setUrl("http://bar");
		Assert.assertEquals(null, BundleUtil.getLinkUrlOfType(ourCtx, b, "next"));
	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
