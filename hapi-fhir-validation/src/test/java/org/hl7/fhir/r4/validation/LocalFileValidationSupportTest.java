package org.hl7.fhir.r4.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.fhirpath.BaseValidationTestWithInlineMocks;
import org.hl7.fhir.common.hapi.validation.support.LocalFileValidationSupport;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class LocalFileValidationSupportTest extends BaseValidationTestWithInlineMocks {

	private FhirContext myFhirCtx = FhirContext.forR4();
	private static final Logger ourLog = LoggerFactory.getLogger(LocalFileValidationSupportTest.class);

	@Test
	public void getStructureDefinition_shouldReturnLocalFile_whenLocalFileProvided() throws IOException {
		String patientProfile = LocalFileValidationSupportTest.class.getResource("/PatientIn-Profile.json").getFile();

		LocalFileValidationSupport localFileValidationSupport = new LocalFileValidationSupport(myFhirCtx);
		localFileValidationSupport.loadFile(patientProfile);
		StructureDefinition structureDefinition = (StructureDefinition) localFileValidationSupport.fetchStructureDefinition("https://www.medizininformatik-initiative.de/fhir/core/modul-person/StructureDefinition/Patient");

		assertEquals(structureDefinition.getName(), "Profile_MII_Patient_PatientIn");
	}

}
