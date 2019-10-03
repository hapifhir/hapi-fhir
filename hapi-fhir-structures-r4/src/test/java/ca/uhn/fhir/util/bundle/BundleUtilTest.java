package ca.uhn.fhir.util.bundle;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.util.TestUtil;
import ca.uhn.fhir.util.BundleUtil;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;
import org.junit.AfterClass;
import org.junit.Test;

import java.util.List;
import java.util.function.Consumer;

import static org.junit.Assert.assertEquals;

public class BundleUtilTest {

	private static FhirContext ourCtx = FhirContext.forR4();

	@Test
	public void testGetLink() {
		Bundle b = new Bundle();
		b.getLinkOrCreate("prev").setUrl("http://bar");
		b.getLinkOrCreate("next").setUrl("http://foo");
		assertEquals("http://foo", BundleUtil.getLinkUrlOfType(ourCtx, b, "next"));
	}

	@Test
	public void testGetLinkDoesntExist() {
		Bundle b = new Bundle();
		b.getLinkOrCreate("prev").setUrl("http://bar");
		assertEquals(null, BundleUtil.getLinkUrlOfType(ourCtx, b, "next"));
	}

	@Test
	public void testGetTotal() {
		Bundle b = new Bundle();
		b.setTotal(999);
		assertEquals(999, BundleUtil.getTotal(ourCtx, b).intValue());
	}

	@Test
	public void testGetTotalNull() {
		Bundle b = new Bundle();
		assertEquals(null, BundleUtil.getTotal(ourCtx, b));
	}

	@Test
	public void toListOfResourcesOfTypeTest() {
		Bundle bundle = new Bundle();
		for (int i = 0; i < 5; i++) {
			bundle.addEntry(new Bundle.BundleEntryComponent().setResource(new Patient()));
		}
		List<Patient> list = BundleUtil.toListOfResourcesOfType(ourCtx, bundle, Patient.class);
		assertEquals(5, list.size());
	}

	@Test
	public void testProcessEntries() {
		Bundle bundle = new Bundle();
		bundle.setType(Bundle.BundleType.TRANSACTION);
		bundle.addEntry().getRequest().setMethod(Bundle.HTTPVerb.GET).setUrl("Observation");
		Consumer<ModifiableBundleEntry> consumer = e -> e.setRequestUrl(ourCtx, e.getRequestUrl() + "?foo=bar");
		BundleUtil.processEntries(ourCtx, bundle, consumer);
		assertEquals("Observation?foo=bar", bundle.getEntryFirstRep().getRequest().getUrl());
	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
