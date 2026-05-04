package ca.uhn.fhir.jpa.batch2.jobs.term.loinc;

import ca.uhn.fhir.batch2.api.JobExecutionFailedException;import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoCodeSystem;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.r4.model.CodeSystem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;

import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_CODESYSTEM_VERSION;
import static org.assertj.core.api.Assertions.assertThatThrownBy;import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.nullable;import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Much of the functionality in {@link ImportLoincStep1ExpandDistributionIntoFilesStep} is tested in
 * {@link BaseExpandDistributionIntoFilesStepTest}
 *
 */
@ExtendWith(MockitoExtension.class)
class ImportLoincStep1ExpandDistributionIntoFilesStepTest {

	private final FhirContext myFhirContext = FhirContext.forR4Cached();
	@InjectMocks
	private final ImportLoincStep1ExpandDistributionIntoFilesStep myStep = new ImportLoincStep1ExpandDistributionIntoFilesStep();
	@Mock
	private IFhirResourceDaoCodeSystem<CodeSystem> myCodeSystemDao;
	@Mock
	private ITermCodeSystemStorageSvc myTermCodeSystemStorageSvc;
	@Mock
	private StepExecutionDetails<LoincJobImportParameters, VoidModel> myStepExecutionDetails;
	@Captor
	private ArgumentCaptor<CodeSystem> myCodeSystemCaptor;

	@Test
	void testHandleSynchronous_NoIdAndNoVersionSpecified() {
		// Setup
		when(myTermCodeSystemStorageSvc.startStagingCodeSystemVersion(any(), any())).thenReturn(new ITermCodeSystemStorageSvc.StartStagingCodeSystemVersionResponse("a-b-c-d"));

		// Test
		CodeSystem cs = new CodeSystem();
		cs.setUrl("http://loinc.org");
		ImportLoincFileSetJson fileSet = new ImportLoincFileSetJson();
		myStep.handleSynchronous(myStepExecutionDetails, "loinc.xml", toBytes(cs), new LoincJobImportParameters(), fileSet);

		// Verify
		assertNotNull(fileSet.getLoincCodeSystem());
		assertEquals("loinc", fileSet.getLoincCodeSystem().getIdElement().getIdPart());
		assertNull(fileSet.getLoincCodeSystem().getVersion());
		assertEquals("EXTERNAL_COPYRIGHT_NOTICE", fileSet.getLoincCodeSystem().getProperty().get(0).getCode());
		assertEquals(CodeSystem.PropertyType.STRING, fileSet.getLoincCodeSystem().getProperty().get(0).getType());

		verify(myCodeSystemDao, times(1)).update(myCodeSystemCaptor.capture(), nullable(RequestDetails.class));
		assertSame(fileSet.getLoincCodeSystem(), myCodeSystemCaptor.getValue());
	}

	@Test
	void testHandleSynchronous_IdAndVersionSpecified() {
		// Setup
		CodeSystem cs = new CodeSystem();
		cs.setUrl("http://loinc.org");
		cs.setId("loinc-cs");

		LoincJobImportParameters jobParameters = new LoincJobImportParameters();
		jobParameters.setProperties(LOINC_CODESYSTEM_VERSION.getCode() + "=1.234");

		when(myTermCodeSystemStorageSvc.startStagingCodeSystemVersion(any(), any())).thenReturn(new ITermCodeSystemStorageSvc.StartStagingCodeSystemVersionResponse("a-b-c-d"));

		// Test
		ImportLoincFileSetJson fileSet = new ImportLoincFileSetJson();
		myStep.handleSynchronous(myStepExecutionDetails, "loinc.xml", toBytes(cs), jobParameters, fileSet);

		// Verify
		assertNotNull(fileSet.getLoincCodeSystem());
		assertEquals("loinc-cs-1.234", fileSet.getLoincCodeSystem().getIdElement().getIdPart());
		assertEquals("1.234", fileSet.getLoincCodeSystem().getVersion());
		assertEquals("EXTERNAL_COPYRIGHT_NOTICE", fileSet.getLoincCodeSystem().getProperty().get(0).getCode());
		assertEquals(CodeSystem.PropertyType.STRING, fileSet.getLoincCodeSystem().getProperty().get(0).getType());

		verify(myCodeSystemDao, times(1)).update(myCodeSystemCaptor.capture(), nullable(RequestDetails.class));
		assertSame(fileSet.getLoincCodeSystem(), myCodeSystemCaptor.getValue());

		verify(myTermCodeSystemStorageSvc, times(1)).startStagingCodeSystemVersion(eq("http://loinc.org"), eq("1.234"));
	}

	@Test
	void testHandleSynchronous_VersionSpecifiedInCodeSystem() {
		CodeSystem cs = new CodeSystem();
		cs.setId("loinc-cs");
		cs.setVersion("1.234");
		cs.setUrl("http://loinc.org");

		// Test
		LoincJobImportParameters jobParameters = new LoincJobImportParameters();
		ImportLoincFileSetJson fileSet = new ImportLoincFileSetJson();
		assertThatThrownBy(() -> myStep.handleSynchronous(myStepExecutionDetails, "loinc.xml", toBytes(cs), jobParameters, fileSet))
			.isInstanceOf(JobExecutionFailedException.class)
			.hasMessageContaining("HAPI-0876: 'loinc.xml' file must not have a version defined. To define a version use 'loinc.codesystem.version' property");
	}

	private byte[] toBytes(CodeSystem theCs) {
		return FhirContext.forR4Cached().newXmlParser().encodeResourceToString(theCs).getBytes(StandardCharsets.UTF_8);
	}

}
