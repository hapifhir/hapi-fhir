package ca.uhn.fhir.jpa.batch2.jobs.term.custom;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.BaseExpandDistributionIntoFilesStep;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyJobParameters;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyMetadataAttachmentJson;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyConstants;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyFileSetJson;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.UriParam;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.r4.model.CodeSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.function.Supplier;

public class ImportCustomTerminologyStep1ExpandDistributionIntoFilesStep
		extends BaseExpandDistributionIntoFilesStep<
				ImportTerminologyJobParameters, ImportCustomTerminologyStep1ExpandDistributionIntoFilesStep.MyContext> {
	private static final Logger ourLog =
			LoggerFactory.getLogger(ImportCustomTerminologyStep1ExpandDistributionIntoFilesStep.class);

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
	protected boolean isIndividualFileAttachmentsSupported() {
		return true;
	}

	@Override
	protected void massageCodeSystem(
			CodeSystem theCodeSystem,
			MyContext theContext,
			StepExecutionDetails<ImportTerminologyJobParameters, VoidModel> theStepExecutionDetails) {
		super.massageCodeSystem(theCodeSystem, theContext, theStepExecutionDetails);

		CodeSystem codeSystem = theContext.getCodeSystem();
		if (codeSystem == null) {

			// These should never happen since we validate
			ImportTerminologyJobParameters jobParameters = theStepExecutionDetails.getParameters();
			Validate.notBlank(jobParameters.getUrl(), "No URL specified in job parameters");
			Validate.notBlank(jobParameters.getVersionId(), "No version specified in job parameters");

			SearchParameterMap map = SearchParameterMap.newSynchronous().setLoadSynchronousUpTo(2);
			map.add(CodeSystem.SP_URL, new UriParam(jobParameters.getUrl()));
			map.add(CodeSystem.SP_VERSION, new TokenParam(jobParameters.getVersionId()));

			IFhirResourceDao dao = myDaoRegistry.getResourceDao("CodeSystem");
			IBundleProvider results = dao.search(map, theStepExecutionDetails.newSystemRequestDetails());
			if (results.isEmpty()) {
				throw new JobExecutionFailedException(Msg.code(2964)
						+ "No CodeSystem resource was supplied in the custom terminology distribution file, and no CodeSystem resource was found in the database with URL["
						+ jobParameters.getUrl() + "] and version[" + jobParameters.getVersionId() + "]");
			}

			// There can't be more than one CodeSystem with the same URL and version
			// because the DAO enforces it
			Validate.isTrue(
					results.size() == 1,
					"Expected exactly one CodeSystem resource with URL[%s] and version[%s]",
					jobParameters.getUrl(),
					jobParameters.getVersionId());

			codeSystem = (CodeSystem) results.getResources(0, 1).get(0);
			ourLog.info(
					"Found existing CodeSystem resource with URL[{}] and version[{}]: {}",
					jobParameters.getUrl(),
					jobParameters.getVersionId(),
					codeSystem.getId());
		}

		if (!codeSystem.getIdElement().hasIdPart()) {
			throw new JobExecutionFailedException(
					Msg.code(2963) + "CodeSystem resource supplied with the job must have an ID");
		}

		ourLog.info(
				"CodeSystem will be stored with ID: CodeSystem/{}",
				codeSystem.getIdElement().getIdPart());
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
	protected void handleSynchronous(
			StepExecutionDetails<ImportTerminologyJobParameters, VoidModel> theStepExecutionDetails,
			IJobDataSink<TerminologyFileSetJson> theDataSink,
			MyContext theContext,
			String theFileName,
			Supplier<InputStream> theInputStreamSupplier,
			ImportTerminologyJobParameters theJobParameters,
			ImportTerminologyMetadataAttachmentJson theJobMetadataAttachment)
			throws IOException {
		super.handleSynchronous(
				theStepExecutionDetails,
				theDataSink,
				theContext,
				theFileName,
				theInputStreamSupplier,
				theJobParameters,
				theJobMetadataAttachment);

		CodeSystem codeSystem = null;
		if (theFileName.endsWith(TerminologyConstants.CUSTOM_CODESYSTEM_JSON)) {
			codeSystem = myCanonicalFhirContext
					.newJsonParser()
					.parseResource(
							CodeSystem.class,
							new InputStreamReader(theInputStreamSupplier.get(), StandardCharsets.UTF_8));
		} else if (theFileName.endsWith(TerminologyConstants.CUSTOM_CODESYSTEM_XML)) {
			codeSystem = myCanonicalFhirContext
					.newXmlParser()
					.parseResource(
							CodeSystem.class,
							new InputStreamReader(theInputStreamSupplier.get(), StandardCharsets.UTF_8));
		}

		if (codeSystem != null) {

			String expectedUrl = theJobParameters.getUrl();
			if (!Objects.equals(codeSystem.getUrl(), expectedUrl)) {
				throw new JobExecutionFailedException(Msg.code(2965) + "CodeSystem resources has unexpected URL: "
						+ codeSystem.getUrl() + ". Expected: " + expectedUrl);
			}

			if (theContext.getCodeSystem() != null) {
				throw new JobExecutionFailedException(Msg.code(2966)
						+ "Multiple CodeSystem resources were supplied in the custom terminology distribution file.");
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
