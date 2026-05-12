package ca.uhn.fhir.jpa.batch2.jobs.term.loinc;

import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoCodeSystem;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.BaseExpandDistributionIntoFilesStep;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.hl7.fhir.r4.model.CodeSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;

import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_CODESYSTEM_VERSION;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_LINGUISTIC_VARIANTS_FILE;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_LINGUISTIC_VARIANTS_FILE_DEFAULT;
import static org.apache.commons.lang3.ObjectUtils.getIfNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trim;

public class ImportLoincStep1ExpandDistributionIntoFilesStep
	extends BaseExpandDistributionIntoFilesStep<LoincJobImportParameters, ImportLoincFileSetJson> {
	private static final Logger ourLog = LoggerFactory.getLogger(ImportLoincStep1ExpandDistributionIntoFilesStep.class);

	@Autowired
	private IFhirResourceDaoCodeSystem<CodeSystem> myCodeSystemDao;

	@Autowired
	private ITermCodeSystemStorageSvc myTermCodeSystemStorageSvc;

	@Override
	protected ImportLoincFileSetJson newTerminologyFileSetJson() {
		return new ImportLoincFileSetJson();
	}

	@Override
	protected void handleSynchronous(
		StepExecutionDetails<LoincJobImportParameters, VoidModel> theStepExecutionDetails,
		String theFileName,
		byte[] theBytes,
		LoincJobImportParameters theJobParameters,
		ImportLoincFileSetJson theFileSet) {
		super.handleSynchronous(theStepExecutionDetails, theFileName, theBytes, theJobParameters, theFileSet);

		String linguisticVariantsFilename = theJobParameters.getProperties().getProperty(LOINC_LINGUISTIC_VARIANTS_FILE.getCode(), LOINC_LINGUISTIC_VARIANTS_FILE_DEFAULT.getCode());

		if (theFileName.endsWith("loinc.xml")) {
			// FIXME: add test to ensure we fail if the ZIP has multiple loinc.xml
			// FIXME: add test to ensure we fail if the ZIP has no loinc.xml
			handleLoincXml(theStepExecutionDetails, theBytes, theJobParameters, theFileSet);
		} else if (theFileName.endsWith(linguisticVariantsFilename)) {
			handleLoincLinguisticVariants(theBytes, theFileSet);
		}

	}

	private void handleLoincLinguisticVariants(byte[] theBytes, ImportLoincFileSetJson theFileSet) {
		ourLog.info("Processing 'LinguisticVariants.csv' file");

		String csvString = new String(theBytes, StandardCharsets.UTF_8);
		try {
			CSVParser csvParser = newLoincCsvParser(new StringReader(csvString));
			for (CSVRecord record : csvParser.getRecords()) {
				String id = trim(record.get("ID"));
				String isoLanguage = trim(record.get("ISO_LANGUAGE"));
				String isoCountry = trim(record.get("ISO_COUNTRY"));
				String languageName = trim(record.get("LANGUAGE_NAME"));

				if (isNotBlank(id) && isNotBlank(isoLanguage) && isNotBlank(isoCountry) && isNotBlank(languageName)) {
					ImportLoincFileSetJson.LinguisticVariantJson linguisticVariant = new ImportLoincFileSetJson.LinguisticVariantJson(id, isoLanguage, isoCountry, languageName);
					theFileSet.getLinguisticVariants().add(linguisticVariant);
				}
			}

		} catch (IOException e) {
			// FIXME: add code
			throw new JobExecutionFailedException(Msg.code(1) + e.getMessage(), e);
		}

	}

	private void handleLoincXml(StepExecutionDetails<LoincJobImportParameters, VoidModel> theStepExecutionDetails, byte[] theBytes, LoincJobImportParameters theJobParameters, ImportLoincFileSetJson theFileSet) {
		ourLog.info("Processing 'loinc.xml' file");

		String loincCodeSystemXml = new String(theBytes, StandardCharsets.UTF_8);
		theFileSet.setLoincCodeSystemXml(loincCodeSystemXml);

		CodeSystem cs = theFileSet.getLoincCodeSystem();
		if (isNotBlank(cs.getVersion())) {
			// FIXME: add test
			throw new JobExecutionFailedException(
				Msg.code(876) + "'loinc.xml' file must not have a version defined. To define a version use '"
					+ LOINC_CODESYSTEM_VERSION.getCode() + "' property of 'loincupload.properties' file");
		}

		if (!"http://loinc.org".equals(cs.getUrl())) {
			// FIXME: add code
			throw new JobExecutionFailedException(
				Msg.code(1) + "'loinc.xml' file must have URL of 'http://loinc.org'. Found: " + cs.getUrl());
		}

		String codeSystemVersionId =
			theJobParameters.getProperties().getProperty(LOINC_CODESYSTEM_VERSION.getCode());
		if (isNotBlank(codeSystemVersionId)) {
			cs.setId(getIfNull(cs.getId(), "loinc") + "-" + codeSystemVersionId);
			cs.setVersion(codeSystemVersionId);
		} else {
			// FIXME: should we allow null versions? probably not
			cs.setId(getIfNull(cs.getId(), "loinc"));
		}

		// TODO: DM 2019-09-13 - Manually add EXTERNAL_COPYRIGHT_NOTICE property until Regenstrief adds this to
		// loinc.xml
		if (cs.getProperty().stream().noneMatch(t -> "EXTERNAL_COPYRIGHT_NOTICE".equals(t.getCode()))) {
			cs.addProperty().setCode("EXTERNAL_COPYRIGHT_NOTICE").setType(CodeSystem.PropertyType.STRING);
		}

		theFileSet.setLoincCodeSystem(cs);

		// Create the CodeSystem resource
		SystemRequestDetails srd = theStepExecutionDetails.newSystemRequestDetails();
		myCodeSystemDao.update(cs, srd);

		ITermCodeSystemStorageSvc.StartStagingCodeSystemVersionResponse response =
			myTermCodeSystemStorageSvc.startStagingCodeSystemVersion(cs.getUrl(), cs.getVersion());
		theFileSet.setCodeSystemStagingVersionId(response.stagingVersionId());
	}
}
