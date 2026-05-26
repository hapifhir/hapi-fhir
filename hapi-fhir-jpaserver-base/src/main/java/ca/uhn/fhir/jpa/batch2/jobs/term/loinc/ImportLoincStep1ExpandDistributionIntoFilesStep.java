package ca.uhn.fhir.jpa.batch2.jobs.term.loinc;

import ca.uhn.fhir.batch2.api.AttachmentContentTypeEnum;
import ca.uhn.fhir.batch2.api.AttachmentDetails;
import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.BaseExpandDistributionIntoFilesStep;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyMetadataAttachmentJson;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyFileSetJson;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc;
import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.util.JsonUtil;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.ValueSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static ca.uhn.fhir.jpa.batch2.jobs.term.loinc.ImportLoincJobAppCtx.STEP_ID_CHUNK_CONCEPTS_FOR_CLOSURE_GENERATION;
import static ca.uhn.fhir.jpa.batch2.jobs.term.loinc.ImportLoincJobAppCtx.STEP_ID_FINALIZE_IMPORT;
import static ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc.MAKE_LOADING_VERSION_CURRENT;
import static org.hl7.fhir.common.hapi.validation.support.ValidationConstants.LOINC_ALL_VALUESET_ID;
import static org.hl7.fhir.common.hapi.validation.support.ValidationConstants.LOINC_GENERIC_VALUESET_URL;

public class ImportLoincStep1ExpandDistributionIntoFilesStep
		extends BaseExpandDistributionIntoFilesStep<
				ImportLoincJobParameters, ImportLoincStep1ExpandDistributionIntoFilesStep.MyContext> {
	private static final Logger ourLog = LoggerFactory.getLogger(ImportLoincStep1ExpandDistributionIntoFilesStep.class);

	@Autowired
	private DaoRegistry myDaoRegistry;

	@Autowired
	private ITermCodeSystemStorageSvc myTermCodeSystemStorageSvc;

	@Override
	protected MyContext newContextObject() {
		return new MyContext();
	}

	@Override
	protected void handleSynchronous(
			StepExecutionDetails<ImportLoincJobParameters, VoidModel> theStepExecutionDetails,
			IJobDataSink<TerminologyFileSetJson> theDataSink,
			MyContext theContext,
			String theFileName,
			byte[] theBytes,
			ImportLoincJobParameters theJobParameters,
			TerminologyFileSetJson theFileSet) {
		super.handleSynchronous(
				theStepExecutionDetails, theDataSink, theContext, theFileName, theBytes, theJobParameters, theFileSet);

		if (theFileName.endsWith("loinc.xml")) {
			theContext.incrementLoincXmlCount();
			handleLoincXml(theStepExecutionDetails, theDataSink, theBytes, theJobParameters);
		}
	}

	private void handleLoincXml(
			StepExecutionDetails<ImportLoincJobParameters, VoidModel> theStepExecutionDetails,
			IJobDataSink<TerminologyFileSetJson> theDataSink,
			byte[] theBytes,
			ImportLoincJobParameters theJobParameters) {
		ourLog.info("Processing 'loinc.xml' file");

		ImportTerminologyMetadataAttachmentJson jobMetadataAttachment = new ImportTerminologyMetadataAttachmentJson();

		String loincCodeSystemXml = new String(theBytes, StandardCharsets.UTF_8);
		jobMetadataAttachment.setCodeSystemXml(loincCodeSystemXml);

		CodeSystem cs = jobMetadataAttachment.getCodeSystem();
		if (!"http://loinc.org".equals(cs.getUrl())) {
			throw new JobExecutionFailedException(
					Msg.code(876) + "'loinc.xml' file must have URL of 'http://loinc.org'. Found: " + cs.getUrl());
		}

		String codeSystemVersionId = theJobParameters.getVersionId();
		assert codeSystemVersionId != null;

		cs.setId("loinc" + "-" + codeSystemVersionId);
		cs.setVersion(codeSystemVersionId);

		// TODO: DM 2019-09-13 - Manually add EXTERNAL_COPYRIGHT_NOTICE property until Regenstrief adds this to
		// loinc.xml
		if (cs.getProperty().stream().noneMatch(t -> "EXTERNAL_COPYRIGHT_NOTICE".equals(t.getCode()))) {
			cs.addProperty().setCode("EXTERNAL_COPYRIGHT_NOTICE").setType(CodeSystem.PropertyType.STRING);
		}

		jobMetadataAttachment.setCodeSystem(cs);

		// Create the CodeSystem resource
		SystemRequestDetails srd = theStepExecutionDetails.newSystemRequestDetails();
		srd.getUserData().put(MAKE_LOADING_VERSION_CURRENT, Boolean.FALSE);
		IFhirResourceDao codeSystemDao = myDaoRegistry.getResourceDao("CodeSystem");
		codeSystemDao.update(myVersionCanonicalizer.codeSystemFromCanonical(cs), srd);

		// Create the "all loinc" ValueSet
		ValueSet valueSet = getValueSetLoincAll(cs.getVersion(), cs.getCopyright());
		IFhirResourceDao valueSetDao = myDaoRegistry.getResourceDao("ValueSet");
		valueSetDao.update(myVersionCanonicalizer.valueSetFromCanonical(valueSet), srd);

		ITermCodeSystemStorageSvc.StartStagingCodeSystemVersionResponse response =
				myTermCodeSystemStorageSvc.startStagingCodeSystemVersion(cs.getUrl(), cs.getVersion());
		jobMetadataAttachment.setCodeSystemStagingVersionId(response.stagingVersionId());

		String instanceId = theStepExecutionDetails.getInstance().getInstanceId();
		AttachmentDetails attachmentRequest = new AttachmentDetails(
				new ByteArrayInputStream(
						JsonUtil.serialize(jobMetadataAttachment).getBytes(StandardCharsets.UTF_8)),
				AttachmentContentTypeEnum.JSON,
				ImportTerminologyMetadataAttachmentJson.ATTACHMENT_FILENAME);
		myJobPersistence.storeNewAttachment(instanceId, attachmentRequest);

		TerminologyFileSetJson fileSet = new TerminologyFileSetJson();
		fileSet.addResourceToActivate("ValueSet/" + valueSet.getIdElement().getIdPart());
		theDataSink.acceptForFutureStep(STEP_ID_FINALIZE_IMPORT, fileSet);

		// Send a single chunk to trigger the first closure generation step
		fileSet = new TerminologyFileSetJson();
		theDataSink.acceptForFutureStep(STEP_ID_CHUNK_CONCEPTS_FOR_CLOSURE_GENERATION, fileSet);
	}

	private ValueSet getValueSetLoincAll(String theLoincVersion, String theCopyrightStatement) {
		ValueSet retVal = new ValueSet();

		retVal.setId(LOINC_ALL_VALUESET_ID + "-" + theLoincVersion);
		retVal.setUrl(LOINC_GENERIC_VALUESET_URL);
		retVal.setVersion(theLoincVersion);
		retVal.setName("All LOINC codes");
		retVal.setStatus(Enumerations.PublicationStatus.DRAFT);
		retVal.setDate(new Date());
		retVal.setPublisher("Regenstrief Institute, Inc.");
		retVal.setDescription("A value set that includes all LOINC codes");
		retVal.setCopyright(theCopyrightStatement);
		retVal.getCompose().addInclude().setSystem(ITermLoaderSvc.LOINC_URI).setVersion(theLoincVersion);

		return retVal;
	}

	@Override
	protected void afterCompletion(MyContext theContext) {
		super.afterCompletion(theContext);

		if (theContext.getLoincXmlCount() == 0) {
			throw new JobExecutionFailedException(Msg.code(2950) + "No 'loinc.xml' file found in ZIP");
		}
		if (theContext.getLoincXmlCount() > 1) {
			throw new JobExecutionFailedException(Msg.code(2951) + "Multiple 'loinc.xml' file found in ZIP");
		}
	}

	public static class MyContext {

		private int myLoincXmlCount;

		public void incrementLoincXmlCount() {
			myLoincXmlCount++;
		}

		public int getLoincXmlCount() {
			return myLoincXmlCount;
		}
	}
}
