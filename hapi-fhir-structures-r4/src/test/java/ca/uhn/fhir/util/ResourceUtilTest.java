package ca.uhn.fhir.util;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;

public class ResourceUtilTest {

	@Test
	public void testRemoveNarrative() {
		Bundle bundle = new Bundle();

		Patient patient = new Patient();
		patient.getText().getDiv().setValue("<div>help im a bug</div>");
		bundle.addEntry().setResource(patient);

		Bundle embeddedBundle = new Bundle();
		embeddedBundle.setType(Bundle.BundleType.COLLECTION);
		bundle.addEntry().setResource(embeddedBundle);

		ResourceUtil.removeNarrative(FhirContext.forR4(), bundle);

		assertNull(((Patient) bundle.getEntry().get(0).getResource()).getText().getDiv().getValueAsString());
	}

}
