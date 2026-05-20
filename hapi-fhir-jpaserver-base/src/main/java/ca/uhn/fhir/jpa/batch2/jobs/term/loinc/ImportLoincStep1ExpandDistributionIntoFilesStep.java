package ca.uhn.fhir.jpa.batch2.jobs.term.loinc;

import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoCodeSystem;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.BaseExpandDistributionIntoFilesStep;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc;
import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.ValueSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Properties;

import static ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc.MAKE_LOADING_VERSION_CURRENT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_CODESYSTEM_VERSION;
import static org.apache.commons.lang3.ObjectUtils.getIfNull;
import static org.hl7.fhir.common.hapi.validation.support.ValidationConstants.LOINC_ALL_VALUESET_ID;
import static org.hl7.fhir.common.hapi.validation.support.ValidationConstants.LOINC_GENERIC_VALUESET_URL;

public class ImportLoincStep1ExpandDistributionIntoFilesStep
	extends BaseExpandDistributionIntoFilesStep<ImportLoincJobParameters, ImportLoincFileSetJson> {
	private static final Logger ourLog = LoggerFactory.getLogger(ImportLoincStep1ExpandDistributionIntoFilesStep.class);

	@Autowired
	private DaoRegistry myDaoRegistry;

	@Autowired
	private ITermCodeSystemStorageSvc myTermCodeSystemStorageSvc;

	@Override
	protected ImportLoincFileSetJson newTerminologyFileSetJson() {
		return new ImportLoincFileSetJson();
	}

	@Override
	protected void handleSynchronous(
		StepExecutionDetails<ImportLoincJobParameters, VoidModel> theStepExecutionDetails,
		String theFileName,
		byte[] theBytes,
		ImportLoincJobParameters theJobParameters,
		ImportLoincFileSetJson theFileSet) {
		super.handleSynchronous(theStepExecutionDetails, theFileName, theBytes, theJobParameters, theFileSet);

		if (theFileName.endsWith("loinc.xml")) {
			// FIXME: add test to ensure we fail if the ZIP has multiple loinc.xml
			// FIXME: add test to ensure we fail if the ZIP has no loinc.xml
			handleLoincXml(theStepExecutionDetails, theBytes, theJobParameters, theFileSet);
		}

	}

	private void handleLoincXml(StepExecutionDetails<ImportLoincJobParameters, VoidModel> theStepExecutionDetails, byte[] theBytes, ImportLoincJobParameters theJobParameters, ImportLoincFileSetJson theFileSet) {
		ourLog.info("Processing 'loinc.xml' file");

		String loincCodeSystemXml = new String(theBytes, StandardCharsets.UTF_8);
		theFileSet.setLoincCodeSystemXml(loincCodeSystemXml);

		CodeSystem cs = theFileSet.getLoincCodeSystem();
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

		theFileSet.setLoincCodeSystem(cs);

		// Create the CodeSystem resource
		SystemRequestDetails srd = theStepExecutionDetails.newSystemRequestDetails();
		srd.getUserData().put(MAKE_LOADING_VERSION_CURRENT, Boolean.FALSE);
		IFhirResourceDao codeSystemDao = myDaoRegistry.getResourceDao("CodeSystem");
		codeSystemDao.update(cs, srd);

		ValueSet valueSet = getValueSetLoincAll(cs.getVersion(), cs.getCopyright());
		IFhirResourceDao valueSetDao = myDaoRegistry.getResourceDao("ValueSet");
		valueSetDao.update(valueSet, srd);

		theFileSet.addResourceToActivate("ValueSet/" + valueSet.getIdElement().getIdPart());

		ITermCodeSystemStorageSvc.StartStagingCodeSystemVersionResponse response =
			myTermCodeSystemStorageSvc.startStagingCodeSystemVersion(cs.getUrl(), cs.getVersion());
		theFileSet.setCodeSystemStagingVersionId(response.stagingVersionId());
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
}
