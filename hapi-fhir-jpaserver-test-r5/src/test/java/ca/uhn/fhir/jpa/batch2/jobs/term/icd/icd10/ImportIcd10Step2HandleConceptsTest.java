package ca.uhn.fhir.jpa.batch2.jobs.term.icd.icd10;

import ca.uhn.fhir.batch2.api.AttachmentDetails;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.BaseImportTerminologyFileStep;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyJobParameters;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyMetadataAttachmentJson;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyFileSetJson;
import ca.uhn.fhir.jpa.batch2.jobs.term.icd.BaseIcdTest;import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.entity.TermConceptProperty;
import ca.uhn.fhir.jpa.term.icd10.Icd10Loader;
import ca.uhn.fhir.util.ClasspathUtil;
import org.hl7.fhir.r4.model.CodeSystem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;import static org.mockito.Mockito.when;


// FIXME: move and rework, and then drop Icd10Loader
@ExtendWith(MockitoExtension.class)
public class ImportIcd10Step2HandleConceptsTest extends BaseIcdTest {

	@InjectMocks
	private ImportIcd10Step2HandleConcepts mySvc;

	@Mock
	StepExecutionDetails<ImportTerminologyJobParameters, TerminologyFileSetJson> stepExecutionDetails;
	@Mock
	ImportTerminologyMetadataAttachmentJson jobMetadata ;
	@Mock
	BaseImportTerminologyFileStep.MyBaseContext context ;
	@Mock
	AttachmentDetails attachment ;
	@Mock
	ImportTerminologyJobParameters jobParameters ;
	@Mock
	TerminologyFileSetJson data;


	@Test
	public void testLoadIcd10Cm() throws IOException, SAXException {
		when(attachment.getInputStream()).thenReturn(ClasspathUtil.loadResourceAsStream("icd/icd102019en.xml"));
		CodeSystem codeSystemToPopulate = new CodeSystem();

//		StringReader reader = new StringReader(ClasspathUtil.loadResource("icd/icd10-dummy-test-en.xml"));
//		TermCodeSystemVersion codeSystemVersion = new TermCodeSystemVersion();
//		CodeSystem codeSystem = new CodeSystem();
//		Icd10Loader loader = new Icd10Loader(codeSystem, codeSystemVersion);
//		loader.load(reader);

		String sourceFilename = null;
		mySvc.processAttachment(stepExecutionDetails, jobMetadata, context, attachment, jobParameters, codeSystemToPopulate, data, sourceFilename);

		// Verify
		List<String> rootCodes = super.listCodes(codeSystemToPopulate.getConcept());
		assertThat(rootCodes).containsExactly("I");

		CodeSystem.ConceptDefinitionComponent rootConcept = codeSystemToPopulate.getConcept().get(0);
		List<String> childCodes = super.listCodes(rootConcept.getConcept());
		assertThat(childCodes).containsExactly("I");


//		assertEquals("ICD-10-EN", codeSystem.getTitle());
//		assertEquals("International Statistical Classification of Diseases and Related Health Problems 10th Revision", codeSystem.getDescription());
//		assertEquals("2022-tree-expanded", codeSystemVersion.getCodeSystemVersionId());
//
//		List<TermConcept> rootConcepts = new ArrayList<>(codeSystemVersion.getConcepts());
//		assertThat(rootConcepts).hasSize(2);
//		TermConcept chapterA = rootConcepts.get(0);
//		assertEquals("A", chapterA.getCode());
//		assertEquals("Fruit", chapterA.getDisplay());
//		Collection<TermConceptProperty> properties = chapterA.getProperties();
//		assertThat(properties).hasSize(2);
//		assertEquals("Include fruit", chapterA.getStringProperty("inclusion"));
//		assertEquals("Things that are not fruit", chapterA.getStringProperty("exclusion"));
//
//		assertThat(toTree(rootConcepts)).isEqualTo("""
//						A "Fruit"
//						-A1-A3 "A1 to A3 type fruit"
//						--A1 "Apples"
//						--A2 "Pears"
//						--A3 "Bananas"
//						B "Trees"
//						-B1-B2 "A group of trees"
//						--B1 "Oak trees"
//						--B2 "Ash trees"
//                  """);
	}

	private String toTree(List<TermConcept> concepts) {
		StringBuilder buffer = new StringBuilder();
		for (TermConcept concept : concepts) {
			toTree(concept, 0, buffer);
		}
		return buffer.toString();
	}

	private void toTree(TermConcept concept, int indent, StringBuilder buffer) {
		buffer.append("-".repeat(indent));
		buffer.append(concept.getCode());
		String display = concept.getDisplay();
		if (display != null) {
			buffer.append(" \"").append(display).append("\"");
		}
		buffer.append("\n");
		indent++;
		for (TermConcept childCode : concept.getChildCodes()) {
			toTree(childCode, indent, buffer);
		}
	}
}
