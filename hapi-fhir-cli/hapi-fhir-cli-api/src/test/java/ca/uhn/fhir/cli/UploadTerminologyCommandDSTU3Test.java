package ca.uhn.fhir.cli;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.dstu3.model.CodeSystem;
import org.hl7.fhir.dstu3.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.FileWriter;
import java.io.IOException;

@ExtendWith(MockitoExtension.class)
public class UploadTerminologyCommandDSTU3Test extends BaseUploadTerminologyCommandTest {
	final String FHIR_VERSION = "dstu3";
	private FhirContext myCtx = FhirContext.forDstu3();

	@BeforeEach
	public void beforeEach() throws Exception {
		writeConceptAndHierarchyFiles();
		super.beforeEach(myCtx);
	}

	@Test
	public void testDeltaAdd() throws IOException {
		super.testDeltaAdd(FHIR_VERSION);
	}

	@Test
	public void testDeltaAddUsingCodeSystemResource() throws IOException {
		try (FileWriter w = new FileWriter(myCodeSystemFile, false)) {
			CodeSystem cs = new CodeSystem();
			cs.addConcept().setCode("CODE").setDisplay("Display");
			myCtx.newJsonParser().encodeResourceToWriter(cs, w);
		}
		super.testDeltaAddUsingCodeSystemResource(FHIR_VERSION);
	}

	@Test
	public void testDeltaAddInvalidResource() throws IOException {
		try (FileWriter w = new FileWriter(myCodeSystemFile, false)) {
			Patient patient = new Patient();
			patient.setActive(true);
			myCtx.newJsonParser().encodeResourceToWriter(patient, w);
		}
		super.testDeltaAddInvalidResource(FHIR_VERSION);
	}

	@Test
	public void testDeltaAddInvalidFileType() throws IOException {
		super.testDeltaAddInvalidFileType(FHIR_VERSION);
	}

	@Test
	public void testDeltaAddUsingCompressedFile() throws IOException {
		super.testDeltaAddUsingCompressedFile(FHIR_VERSION);
	}

	@Test
	public void testDeltaAddInvalidFileName() throws IOException {
		super.testDeltaAddInvalidFileName(FHIR_VERSION);
	}

	@Test
	public void testDeltaRemove() throws IOException {
		super.testDeltaRemove(FHIR_VERSION);
	}

	@Test
	public void testSnapshot() throws IOException {
		super.testSnapshot(FHIR_VERSION);
	}

	@Test
	public void testPropertiesFile() throws IOException {
		super.testPropertiesFile(FHIR_VERSION);
	}

	/**
	 * When transferring large files, we use a local file to store the binary instead of
	 * using HTTP to transfer a giant base 64 encoded attachment. Hopefully we can
	 * replace this with a bulk data import at some point when that gets implemented.
	 */
	@Test
	public void testSnapshotLargeFile() throws IOException {
		super.testSnapshotLargeFile(FHIR_VERSION);
	}

	@Test
	public void testAddICD10UsingCompressedFile() throws IOException {
		super.testUploadICD10UsingCompressedFile(FHIR_VERSION);
	}
}
