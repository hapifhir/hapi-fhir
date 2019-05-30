package ca.uhn.fhir.util;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

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

	@Test
	public void testGetTotal() {
		Bundle b = new Bundle();
		b.setTotal(999);
		Assert.assertEquals(999, BundleUtil.getTotal(ourCtx, b).intValue());
	}

	@Test
	public void testGetTotalNull() {
		Bundle b = new Bundle();
		Assert.assertEquals(null, BundleUtil.getTotal(ourCtx, b));
	}

	@Test
	public void toListOfResourcesOfTypeTest() {
		Bundle bundle = new Bundle();
		for (int i = 0; i < 5; i++) {
			bundle.addEntry(new Bundle.BundleEntryComponent().setResource(new Patient()));
		}
		List<Patient> list = BundleUtil.toListOfResourcesOfType(ourCtx, bundle, Patient.class);
		Assert.assertEquals(5, list.size());
	}


	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
