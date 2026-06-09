package ca.uhn.fhir.jpa.batch2.jobs.term.icd;

import ca.uhn.fhir.batch2.api.AttachmentDetails;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.BaseImportTerminologyFileStep;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyJobParameters;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyMetadataAttachmentJson;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyFileSetJson;
import org.hl7.fhir.r4.model.CodeSystem;
import org.jetbrains.annotations.UnknownNullability;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
public abstract class BaseIcdTest {

	@Mock
	protected StepExecutionDetails<ImportTerminologyJobParameters, TerminologyFileSetJson> myStepExecutionDetails;
	@Mock
	protected ImportTerminologyMetadataAttachmentJson myJobMetadata;
	@Mock
	protected AttachmentDetails myAttachment;
	@Mock
	protected ImportTerminologyJobParameters myJobParameters;
	@Mock
	protected TerminologyFileSetJson myData;

	protected final CodeSystem myCodeSystemToPopulate = new CodeSystem();
	protected final BaseImportTerminologyFileStep.MyBaseContext myContext = new BaseImportTerminologyFileStep.MyBaseContext();


	protected List<CodeSystem.ConceptDefinitionComponent> getRootConcepts(Collection<CodeSystem.ConceptDefinitionComponent> theConcepts) {
		Set<String> nonRootConcepts = new HashSet<>();
		for (CodeSystem.ConceptDefinitionComponent concept : theConcepts) {
			for (CodeSystem.ConceptDefinitionComponent child : concept.getConcept()) {
				nonRootConcepts.add(child.getCode());
			}
		}

		List<CodeSystem.ConceptDefinitionComponent> rootConcepts = new ArrayList<>();
		for (CodeSystem.ConceptDefinitionComponent concept : theConcepts) {
			if (nonRootConcepts.contains(concept.getCode())) {
				continue;
			}
			rootConcepts.add(concept);
		}

		return rootConcepts;
	}


	protected String toTree(Collection<CodeSystem.ConceptDefinitionComponent> theConcepts) {
		StringBuilder buffer = new StringBuilder();
		for (CodeSystem.ConceptDefinitionComponent concept : getRootConcepts(theConcepts)) {
			toTree(concept, 0, buffer);
		}
		return buffer.toString();
	}

	private void toTree(CodeSystem.@UnknownNullability ConceptDefinitionComponent concept, int indent, StringBuilder buffer) {
		buffer.append("-".repeat(indent));
		buffer.append(concept.getCode());
		String display = concept.getDisplay();
		if (display != null) {
			buffer.append(" \"").append(display).append("\"");
		}
		buffer.append("\n");
		indent++;
		for (CodeSystem.ConceptDefinitionComponent childCode : concept.getConcept()) {
			toTree(childCode, indent, buffer);
		}
	}

}
