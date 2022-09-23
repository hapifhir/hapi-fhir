package org.hl7.fhir.r4.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.EncodingEnum;
import org.apache.commons.io.IOUtils;
import org.hl7.fhir.common.hapi.validation.support.LocalFileValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.SnapshotGeneratingValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class LocalFileValidationSupportTest {

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
