package ca.uhn.fhir.jpa.batch2.jobs.term.custom;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.BaseExpandDistributionIntoFilesStep;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyJobParameters;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyMetadataAttachmentJson;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyConstants;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyFileSetJson;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.hl7.fhir.r4.model.CodeSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class ImportCustomTerminologyStep1ExpandDistributionIntoFilesStep
	extends BaseExpandDistributionIntoFilesStep<ImportTerminologyJobParameters, ImportCustomTerminologyStep1ExpandDistributionIntoFilesStep.MyContext> {
	private static final Logger ourLog = LoggerFactory.getLogger(ImportCustomTerminologyStep1ExpandDistributionIntoFilesStep.class);

	private final FhirContext myCanonicalFhirContext = FhirContext.forR4Cached();

	@Override
	protected MyContext newContextObject() {
		return new MyContext();
	}

	@Nonnull
	@Override
	protected String getDistributionFileName() {
		return TerminologyConstants.FILENAME_CUSTOM_DISTRIBUTION_FILE;
	}

	@Nullable
	@Override
	protected String getCodeSystemIdRoot() {
		return null;
	}

	@Override
	protected void massageCodeSystem(CodeSystem theCodeSystem, MyContext theContext) {
		super.massageCodeSystem(theCodeSystem, theContext);

		CodeSystem codeSystem = theContext.getCodeSystem();
		if (codeSystem == null) {
			// FIXME: add test
			throw new JobExecutionFailedException(Msg.code(1) + "No CodeSystem resource was supplied in the custom terminology distribution file.");
		}

		// FIXME: throw error if no ID, and add test
		ourLog.info("CodeSystem will be stored with ID: CodeSystem/{}", codeSystem.getIdElement().getIdPart());
		theCodeSystem.setId(codeSystem.getIdElement().getIdPart());

		theCodeSystem.setName(codeSystem.getName());
		theCodeSystem.setStatus(codeSystem.getStatus());
		theCodeSystem.setHierarchyMeaning(codeSystem.getHierarchyMeaning());
		theCodeSystem.setDescription(codeSystem.getDescription());
		theCodeSystem.setTitle(codeSystem.getTitle());
		theCodeSystem.setCaseSensitiveElement(codeSystem.getCaseSensitiveElement());
		theCodeSystem.setCopyright(codeSystem.getCopyright());
		theCodeSystem.setCompositional(codeSystem.getCompositional());
		theCodeSystem.setContact(codeSystem.getContact());
		theCodeSystem.setDateElement(codeSystem.getDateElement());
		theCodeSystem.setExperimentalElement(codeSystem.getExperimentalElement());
		theCodeSystem.setIdentifier(codeSystem.getIdentifier());
		theCodeSystem.setPublisher(codeSystem.getPublisher());
		theCodeSystem.setPurpose(codeSystem.getPurpose());
		theCodeSystem.setSupplements(codeSystem.getSupplements());
	}

	@Override
	protected void handleSynchronous(StepExecutionDetails<ImportTerminologyJobParameters, VoidModel> theStepExecutionDetails, IJobDataSink<TerminologyFileSetJson> theDataSink, MyContext theContext, String theFileName, byte[] theBytes, ImportTerminologyJobParameters theJobParameters, TerminologyFileSetJson theFileSet, ImportTerminologyMetadataAttachmentJson theJobMetadataAttachment) {
		super.handleSynchronous(theStepExecutionDetails, theDataSink, theContext, theFileName, theBytes, theJobParameters, theFileSet, theJobMetadataAttachment);

		CodeSystem codeSystem = null;
		if (theFileName.endsWith(TerminologyConstants.CUSTOM_CODESYSTEM_JSON)) {
			codeSystem = myCanonicalFhirContext.newJsonParser().parseResource(CodeSystem.class, new String(theBytes, StandardCharsets.UTF_8));
		} else if (theFileName.endsWith(TerminologyConstants.CUSTOM_CODESYSTEM_XML)) {
			codeSystem = myCanonicalFhirContext.newXmlParser().parseResource(CodeSystem.class, new String(theBytes, StandardCharsets.UTF_8));
		}

		if (codeSystem != null) {

			String expectedUrl = theJobParameters.getUrl();
			if (!Objects.equals(codeSystem.getUrl(), expectedUrl)) {
				// FIXME: add code
				throw new JobExecutionFailedException(Msg.code(1) + "CodeSystem resources has unexpected URL: " + codeSystem.getUrl() + ". Expected: " + expectedUrl);
			}

			if ( theContext.getCodeSystem() != null) {
				// FIXME: add code
				throw new JobExecutionFailedException(Msg.code(1) + "Multiple CodeSystem resources were supplied in the custom terminology distribution file.");
			}
			theContext.setCodeSystem(codeSystem);
		}
	}


	protected static class MyContext {

		private CodeSystem myCodeSystem;

		public CodeSystem getCodeSystem() {
			return myCodeSystem;
		}

		public void setCodeSystem(CodeSystem theCodeSystem) {
			myCodeSystem = theCodeSystem;
		}

	}

}
