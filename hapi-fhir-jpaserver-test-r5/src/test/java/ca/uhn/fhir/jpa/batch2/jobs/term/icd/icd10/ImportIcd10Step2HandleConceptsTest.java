package ca.uhn.fhir.jpa.batch2.jobs.term.icd.icd10;

import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.BaseImportTerminologyFileStep;
import ca.uhn.fhir.jpa.batch2.jobs.term.icd.BaseIcdTest;
import ca.uhn.fhir.util.ClasspathUtil;
import org.hl7.fhir.r4.model.CodeSystem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class ImportIcd10Step2HandleConceptsTest extends BaseIcdTest {

	@InjectMocks
	private ImportIcd10Step2HandleConcepts mySvc;

	@Test
	public void testLoadIcd10Cm() {
		when(myAttachment.getInputStream()).thenReturn(ClasspathUtil.loadResourceAsStream("icd/icd102019en.xml"));

		CodeSystem codeSystemToPopulate = new CodeSystem();
		BaseImportTerminologyFileStep.MyBaseContext context = new BaseImportTerminologyFileStep.MyBaseContext();

		String sourceFilename = null;
		mySvc.processAttachment(myStepExecutionDetails, myJobMetadata, context, myAttachment, myJobParameters, codeSystemToPopulate, myData, sourceFilename);

		// Verify
		String tree = toTree(context.getCodeToConcept().values());
		String expected = """
			I "Certain infectious and parasitic diseases"
			-A00-A09 "Intestinal infectious diseases"
			--A00 "Cholera"
			---A00.0 "Cholera due to Vibrio cholerae 01, biovar cholerae"
			---A00.1 "Cholera due to Vibrio cholerae 01, biovar eltor"
			---A00.9 "Cholera, unspecified"
			--A01 "Typhoid and paratyphoid fevers"
			---A01.0 "Typhoid fever"
			---A01.1 "Paratyphoid fever A"
			---A01.2 "Paratyphoid fever B"
			---A01.3 "Paratyphoid fever C"
			---A01.4 "Paratyphoid fever, unspecified"
			--A02 "Other salmonella infections"
			""";
		assertEquals(expected, tree);
	}

	@Test
	public void testInvalidXmlDocument() {
		// This is the ICD-10-CM document, not the ICD-10 document
		when(myAttachment.getInputStream()).thenReturn(ClasspathUtil.loadResourceAsStream("icd/icd10cm_tabular_2021.xml"));

		assertThatThrownBy(()->mySvc.processAttachment(myStepExecutionDetails, myJobMetadata, myContext, myAttachment, myJobParameters, myCodeSystemToPopulate, myData, null))
			.isInstanceOf(JobExecutionFailedException.class)
			.hasMessageContaining("Unexpected root node in ICD-10 document: ICD10CM.tabular");
	}

}
