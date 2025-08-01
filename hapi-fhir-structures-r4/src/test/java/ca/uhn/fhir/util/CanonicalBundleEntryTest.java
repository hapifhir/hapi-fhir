package ca.uhn.fhir.util;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.r4.model.Bundle;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class CanonicalBundleEntryTest {
	private static final Logger ourLog = LoggerFactory.getLogger(CanonicalBundleEntryTest.class);
	public static final FhirContext ourFhirContext = FhirContext.forR4Cached();
	@Test
	public void testMapping() {
		ourLog.debug("Starting testMapping");
		Bundle bundle = ClasspathUtil.loadResource(ourFhirContext, Bundle.class, "transaction-bundle.json");
		List<CanonicalBundleEntry> canonicalEntries = BundleUtil.toListOfCanonicalBundleEntries(ourFhirContext, bundle);
		Bundle newBundle = new Bundle();
		newBundle.setType(bundle.getType());
		newBundle.setId(bundle.getId());
		newBundle.setMeta(bundle.getMeta());
		canonicalEntries.forEach(entry -> {
			newBundle.addEntry(entry.toBundleEntry(ourFhirContext, Bundle.BundleEntryComponent.class));
		});

		String originalBundleString = ourFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle);
		String newBundleString = ourFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(newBundle);
		assertThat(newBundleString).isEqualTo(originalBundleString);
	}

}
