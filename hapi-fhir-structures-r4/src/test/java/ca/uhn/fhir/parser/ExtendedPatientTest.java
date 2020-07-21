package ca.uhn.fhir.parser;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

public class ExtendedPatientTest {

	@Test
	public void testBundleReferences() {

		FhirContext fhirContext = FhirContext.forR4();
		fhirContext.setDefaultTypeForProfile("http://acme.org//StructureDefinition/patient-with-eyes", ExtendedPatient.class);

		ExtendedPatient homer = new ExtendedPatient();
		homer.setId("homer");
		homer.addName().setFamily("Simpson").addGiven("Homer");

		ExtendedPatient marge = new ExtendedPatient();
		marge.setId("marge");
		marge.addName().setFamily("Simpson").addGiven("Marge");
		marge.setEyeColour(new CodeType("blue"));
		marge.getLink().add(new Patient.PatientLinkComponent()
			.setType(Patient.LinkType.REFER)
			.setOther(new Reference("Patient/homer")));

		Bundle bundle = new Bundle()
			.addEntry(new Bundle.BundleEntryComponent()
				.setFullUrl("http://acme.org/Patient/homer").setResource(homer)
				.setSearch(new Bundle.BundleEntrySearchComponent()
					.setMode(Bundle.SearchEntryMode.INCLUDE)))
			.addEntry(new Bundle.BundleEntryComponent()
				.setFullUrl("http://acme.org/Patient/marge").setResource(marge)
				.setSearch(new Bundle.BundleEntrySearchComponent()));

		IParser p = fhirContext.newXmlParser().setPrettyPrint(true);
		String encoded = p.encodeResourceToString(bundle);

		Bundle parsedBundle = p.parseResource(Bundle.class, encoded);

		ExtendedPatient parsedHomer = (ExtendedPatient) parsedBundle.getEntry().get(0).getResource();
		ExtendedPatient parsedMarge = (ExtendedPatient) parsedBundle.getEntry().get(1).getResource();

		IBaseResource referencedHomer = parsedMarge.getLinkFirstRep().getOther().getResource();
		assertNotNull(referencedHomer);
		assertSame(parsedHomer, referencedHomer);
	}

}
