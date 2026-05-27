/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.jpa.batch2.jobs.term.loinc;

import ca.uhn.fhir.batch2.api.AttachmentDetails;
import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.LookupCodeRequest;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.BaseImportTerminologyStep;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ITerminologyImportFileHandlerStep;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyMetadataAttachmentJson;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyFileSetJson;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.term.UploadStatistics;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import ca.uhn.fhir.system.HapiSystemProperties;
import ca.uhn.hapi.converters.canonical.VersionCanonicalizer;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.SetMultimap;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.time.DateUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.ContactPoint;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.PrimitiveType;
import org.hl7.fhir.r4.model.ValueSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static ca.uhn.fhir.jpa.batch2.jobs.term.base.BaseExpandDistributionIntoFilesStep.newLoincCsvParser;
import static ca.uhn.fhir.jpa.batch2.jobs.term.loinc.ImportLoincJobAppCtx.STEP_ID_FINALIZE_IMPORT;
import static ca.uhn.fhir.util.TestUtil.sleepAtLeast;
import static org.apache.commons.lang3.StringUtils.defaultIfBlank;
import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.hl7.fhir.common.hapi.validation.support.ValidationConstants.LOINC_GENERIC_CODE_SYSTEM_URL;

public abstract class BaseImportLoincStep<CT extends BaseImportLoincStep.MyBaseContext>
		extends BaseImportTerminologyStep
		implements ITerminologyImportFileHandlerStep<
				ImportLoincJobParameters, TerminologyFileSetJson, TerminologyFileSetJson> {
	/**
	 * This is <b>NOT</b> the LOINC CodeSystem URI! It is just
	 * the website URL to LOINC.
	 */
	public static final String LOINC_WEBSITE_URL = "https://loinc.org";

	public static final String REGENSTRIEF_INSTITUTE_INC = "Regenstrief Institute, Inc.";
	public static final String LOINC_IEEE_CM_ID = "loinc-to-ieee-11073-10101";
	public static final String LOINC_IEEE_CM_URI = "http://loinc.org/cm/loinc-to-ieee-11073-10101";
	public static final String LOINC_IEEE_CM_NAME = "LOINC/IEEE Device Code Mappings";
	public static final String CM_COPYRIGHT =
			"The LOINC/IEEE Medical Device Code Mapping Table contains content from IEEE (http://ieee.org), copyright © 2017 IEEE.";
	public static final String CM_RSNA_COPYRIGHT =
			"The LOINC/RSNA Radiology Playbook and the LOINC Part File contain content from RadLex® (http://rsna.org/RadLex.aspx), copyright © 2005-2017, The Radiological Society of North America, Inc., available at no cost under the license at http://www.rsna.org/uploadedFiles/RSNA/Content/Informatics/RadLex_License_Agreement_and_Terms_of_Use_V2_Final.pdf.";
	public static final String LOINC_SCT_PART_MAP_ID = "loinc-parts-to-snomed-ct";
	public static final String LOINC_SCT_PART_MAP_URI = "http://loinc.org/cm/loinc-parts-to-snomed-ct";
	public static final String LOINC_TERM_TO_RPID_PART_MAP_ID = "loinc-to-radlex";
	public static final String LOINC_TERM_TO_RPID_PART_MAP_URI = "http://loinc.org/cm/loinc-to-radlex";
	public static final String LOINC_TERM_TO_RPID_PART_MAP_NAME = "LOINC Terms to RadLex RPIDs";
	public static final String LOINC_PART_TO_RID_PART_MAP_ID = "loinc-parts-to-radlex";
	public static final String LOINC_PART_TO_RID_PART_MAP_URI = "http://loinc.org/cm/loinc-parts-to-radlex";
	public static final String LOINC_PART_TO_RID_PART_MAP_NAME = "LOINC Parts to RadLex RIDs";
	public static final String LOINC_SCT_PART_MAP_NAME = "LOINC Part Map to SNOMED CT";
	public static final String LOINC_RXNORM_PART_MAP_ID = "loinc-parts-to-rxnorm";
	public static final String LOINC_RXNORM_PART_MAP_URI = "http://loinc.org/cm/loinc-parts-to-rxnorm";
	public static final String LOINC_RXNORM_PART_MAP_NAME = "LOINC Part Map to RxNORM";
	public static final String LOINC_PUBCHEM_PART_MAP_ID = "loinc-parts-to-pubchem";
	public static final String LOINC_PUBCHEM_PART_MAP_URI = "http://loinc.org/cm/loinc-parts-to-pubchem";
	public static final String LOINC_PUBCHEM_PART_MAP_NAME = "LOINC Part Map to PubChem";
	public static final String CM_SCT_COPYRIGHT =
			"The LOINC Part File, LOINC/SNOMED CT Expression Association and Map Sets File, RELMA database and associated search index files include SNOMED Clinical Terms (SNOMED CT®) which is used by permission of the International Health Terminology Standards Development Organisation (IHTSDO) under license. All rights are reserved. SNOMED CT® was originally created by The College of American Pathologists. “SNOMED” and “SNOMED CT” are registered trademarks of the IHTSDO. Use of SNOMED CT content is subject to the terms and conditions set forth in the SNOMED CT Affiliate License Agreement.  It is the responsibility of those implementing this product to ensure they are appropriately licensed and for more information on the license, including how to register as an Affiliate Licensee, please refer to http://www.snomed.org/snomed-ct/get-snomed-ct or info@snomed.org. Under the terms of the Affiliate License, use of SNOMED CT in countries that are not IHTSDO Members is subject to reporting and fee payment obligations. However, IHTSDO agrees to waive the requirements to report and pay fees for use of SNOMED CT content included in the LOINC Part Mapping and LOINC Term Associations for purposes that support or enable more effective use of LOINC. This material includes content from the US Edition to SNOMED CT, which is developed and maintained by the U.S. National Library of Medicine and is available to authorized UMLS Metathesaurus Licensees from the UTS Downloads site at https://uts.nlm.nih.gov.";
	public static final String RSNA_CODES_VS_ID = "loinc-rsna-radiology-playbook";
	public static final String RSNA_CODES_VS_URI = "http://loinc.org/vs/loinc-rsna-radiology-playbook";
	public static final String RSNA_CODES_VS_NAME = "LOINC/RSNA Radiology Playbook";
	public static final String RID_CS_URI = "http://www.radlex.org";
	/**
	 * About these being the same - Per Dan Vreeman:
	 * We had some discussion about this, and both
	 * RIDs (RadLex clinical terms) and RPIDs (Radlex Playbook Ids)
	 * belong to the same "code system" since they will never collide.
	 * The codesystem uri is "http://www.radlex.org". FYI, that's
	 * now listed on the FHIR page:
	 * https://www.hl7.org/fhir/terminologies-systems.html
	 * -ja
	 */
	public static final String RPID_CS_URI = RID_CS_URI;

	public static final String DOCUMENT_ONTOLOGY_CODES_VS_ID = "loinc-document-ontology";
	public static final String DOCUMENT_ONTOLOGY_CODES_VS_URI = "http://loinc.org/vs/loinc-document-ontology";
	public static final String DOCUMENT_ONTOLOGY_CODES_VS_NAME = "LOINC Document Ontology Codes";
	public static final String ASK_AT_ORDER_ENTRY_PROP_NAME = "AskAtOrderEntry";
	public static final String ASSOCIATED_OBSERVATIONS_PROP_NAME = "AssociatedObservations";
	private static final Logger ourLog = LoggerFactory.getLogger(BaseImportLoincStep.class);

	@Autowired
	protected FhirContext myFhirContext;

	@Autowired
	private IJobPersistence myJobPersistence;

	@Autowired
	private ITermCodeSystemStorageSvc myTermCodeSystemStorageSvc;

	@Autowired
	private IHapiTransactionService myTransactionService;

	@Autowired
	private IValidationSupport myValidationSupport;

	@Autowired
	private DaoRegistry myDaoRegistry;

	@Autowired
	protected VersionCanonicalizer myVersionCanonicalizer;

	@Nonnull
	@Override
	public Optional<FileHandlingInstructions> canHandleFile(
			StepExecutionDetails<ImportLoincJobParameters, VoidModel> theStepExecutionDetails,
			ImportLoincJobParameters theJobParameters,
			String theFileName) {

		Properties jobProperties = getJobProperties(theStepExecutionDetails);

		for (LoincFileNameSpecification loincFileNameSpecification : getFilesToProcess(theStepExecutionDetails)) {
			if (loincFileNameSpecification.matchFileName(jobProperties, theFileName)) {
				return Optional.of(new FileHandlingInstructions(loincFileNameSpecification.fileHandlingType()));
			}
		}

		return Optional.empty();
	}

	protected Properties getJobProperties(StepExecutionDetails<ImportLoincJobParameters, ?> theStepExecutionDetails) {
		ImportLoincJobParameters jobParameters = theStepExecutionDetails.getParameters();
		Properties retVal = jobParameters.getJobProperties();
		if (retVal == null) {
			String instanceId = theStepExecutionDetails.getInstance().getInstanceId();
			retVal = new Properties();
			try {
				String filename = LoincUploadPropertiesEnum.LOINC_UPLOAD_PROPERTIES_FILE.getCode();
				AttachmentDetails attachment = myJobPersistence.fetchAttachmentByFilename(instanceId, filename);
				retVal.load(attachment.getInputStream());
			} catch (ResourceNotFoundException | IOException e) {
				// no properties file was provided
			}

			jobParameters.setJobProperties(retVal);
		}
		return retVal;
	}

	@Nonnull
	@Override
	public RunOutcome run(
			@Nonnull StepExecutionDetails<ImportLoincJobParameters, TerminologyFileSetJson> theStepExecutionDetails,
			@Nonnull IJobDataSink<TerminologyFileSetJson> theDataSink)
			throws JobExecutionFailedException {
		TerminologyFileSetJson data = theStepExecutionDetails.getData();
		String jobInstanceId = theStepExecutionDetails.getInstance().getInstanceId();
		ImportLoincJobParameters jobParameters = theStepExecutionDetails.getParameters();

		CT codeExtractionContext = newContextObject(theStepExecutionDetails);

		ImportTerminologyMetadataAttachmentJson jobMetadata = getJobMetadata(jobInstanceId);

		CodeSystem codeSystemToPopulate = new CodeSystem();
		codeSystemToPopulate.setUrl(jobMetadata.getCodeSystem().getUrl());
		codeSystemToPopulate.setVersion(jobMetadata.getCodeSystemStagingVersionId());

		String attachmentId = data.getAttachmentId();
		String sourceFilename = data.getSourceFilename();
		if (isNotBlank(attachmentId)) {

			AttachmentDetails attachment = myJobPersistence.fetchAttachmentById(jobInstanceId, attachmentId);
			try (InputStream inputStream = attachment.getInputStream()) {
				InputStreamReader reader = new InputStreamReader(
						BOMInputStream.builder().setInputStream(inputStream).get(), StandardCharsets.UTF_8);
				CSVParser csvReader = newLoincCsvParser(reader);
				for (CSVRecord record : csvReader.getRecords()) {
					handleRecord(
							theStepExecutionDetails,
							jobMetadata,
							jobParameters,
							codeExtractionContext,
							record,
							codeSystemToPopulate,
							data,
							sourceFilename);
				}

			} catch (IOException e) {
				throw new JobExecutionFailedException(
						Msg.code(2941) + "Failed to read file attachment: " + e.getMessage(), e);
			}

			syncToDb(jobMetadata, codeExtractionContext, codeSystemToPopulate, theStepExecutionDetails);
		}

		if (!data.getStepIdToRecordsAdded().isEmpty()
				|| !data.getResourcesToActivate().isEmpty()) {
			TerminologyFileSetJson counterWorkChunk = new TerminologyFileSetJson();
			counterWorkChunk.getStepIdToRecordsAdded().putAll(data.getStepIdToRecordsAdded());
			counterWorkChunk.getResourcesToActivate().addAll(data.getResourcesToActivate());
			theDataSink.acceptForFutureStep(STEP_ID_FINALIZE_IMPORT, counterWorkChunk);
		}

		return RunOutcome.SUCCESS;
	}

	protected <T> T executeInNewTransactionWithRetry(
			Callable<T> theFunction,
			StepExecutionDetails<ImportLoincJobParameters, TerminologyFileSetJson> theStepExecutionDetails) {
		int retryCount = 0;
		while (true) {
			try {
				return myTransactionService
						.withSystemRequestOnDefaultPartition()
						.execute(theFunction);
			} catch (ResourceVersionConflictException e) {
				retryCount++;
				int maxRetries = 10;
				if (retryCount > maxRetries) {
					throw e;
				}
				ourLog.atWarn()
						.setMessage("Failed to save terminology for step {}, retry {}/{} in 5 seconds: {}")
						.addArgument(theStepExecutionDetails.getCurrentStepId())
						.addArgument(retryCount)
						.addArgument(maxRetries)
						.addArgument(e.getMessage())
						.log();

				long sleepTime = 5 * DateUtils.MILLIS_PER_SECOND;
				if (HapiSystemProperties.isUnitTestModeEnabled()) {
					sleepTime = 10;
				}

				sleepAtLeast(sleepTime);
			}
		}
	}

	protected abstract CT newContextObject(
			StepExecutionDetails<ImportLoincJobParameters, TerminologyFileSetJson> theStepExecutionDetails);

	@Nonnull
	protected abstract List<LoincFileNameSpecification> getFilesToProcess(
			StepExecutionDetails<ImportLoincJobParameters, ?> theStepExecutionDetails);

	protected abstract void handleRecord(
			StepExecutionDetails<ImportLoincJobParameters, TerminologyFileSetJson> theStepExecutionDetails,
			ImportTerminologyMetadataAttachmentJson theJobMetadata,
			ImportLoincJobParameters theJobParameters,
			CT theContext,
			CSVRecord theRecord,
			CodeSystem theCodeSystemToPopulate,
			TerminologyFileSetJson theData,
			String theSourceFilename);

	protected TerminologyFileSetJson.RecordsAddedCounter getRecordsAddedCounter(
			StepExecutionDetails<ImportLoincJobParameters, TerminologyFileSetJson> theStepExecutionDetails) {

		TerminologyFileSetJson data = theStepExecutionDetails.getData();
		String currentStepId = theStepExecutionDetails.getCurrentStepId();
		return data.getRecordsAddedCounter(currentStepId);
	}

	@Nullable
	IValidationSupport.LookupCodeResult lookupPreExistingConcept(
			ImportTerminologyMetadataAttachmentJson theJobMetadata, String propertyCodeValue) {
		String version = theJobMetadata.getCodeSystemStagingVersionId();
		LookupCodeRequest request =
				new LookupCodeRequest(LOINC_GENERIC_CODE_SYSTEM_URL + "|" + version, propertyCodeValue);
		return myValidationSupport.lookupCode(new ValidationSupportContext(myValidationSupport), request);
	}

	@Nonnull
	protected CodeSystem.ConceptDefinitionComponent getOrAddConcept(
			CT theContext, CodeSystem theCodeSystemToPopulate, String theCode) {
		CodeSystem.ConceptDefinitionComponent loincCode;
		loincCode = theContext.getCodeToConcept().get(theCode);
		if (loincCode == null) {
			loincCode = theCodeSystemToPopulate.addConcept();
			loincCode.setCode(theCode);
			theContext.getCodeToConcept().put(theCode, loincCode);
		}
		return loincCode;
	}

	protected void addConceptMapEntry(CT theContext, ConceptMapping theMapping) {
		Validate.notBlank(theMapping.getConceptMapId(), "ConceptMap ID must not be blank");

		if (isBlank(theMapping.getSourceCode())) {
			return;
		}
		if (isBlank(theMapping.getTargetCode())) {
			return;
		}

		theContext.getIdToConceptMappings().put(theMapping.getConceptMapId(), theMapping);
	}

	protected ValueSet getOrAddValueSet(
			StepExecutionDetails<ImportLoincJobParameters, TerminologyFileSetJson> theStepExecutionDetails,
			ImportTerminologyMetadataAttachmentJson theJobMetadata,
			TerminologyFileSetJson theData,
			CT theContext,
			String theValueSetId,
			String theValueSetUri,
			String theValueSetName,
			String theVersionPropertyName) {

		String version;
		String codeSystemVersion = theJobMetadata.getCodeSystem().getVersion();
		assert isNotBlank(codeSystemVersion);

		Properties jobProperties = getJobProperties(theStepExecutionDetails);
		if (isNotBlank(theVersionPropertyName) && isNotBlank(jobProperties.getProperty(theVersionPropertyName))) {
			version = jobProperties.getProperty(theVersionPropertyName) + "-" + codeSystemVersion;
		} else {
			version = codeSystemVersion;
		}

		ValueSet vs;
		String valueSetId = theValueSetId + "-" + codeSystemVersion;

		if (!theContext.getIdToValueSet().containsKey(valueSetId)) {
			vs = new ValueSet();
			vs.setUrl(theValueSetUri);
			vs.setId(valueSetId);
			vs.setVersion(version);
			vs.setStatus(Enumerations.PublicationStatus.DRAFT);
			vs.setPublisher(REGENSTRIEF_INSTITUTE_INC);
			vs.addContact()
					.setName(REGENSTRIEF_INSTITUTE_INC)
					.addTelecom()
					.setSystem(ContactPoint.ContactPointSystem.URL)
					.setValue(LOINC_WEBSITE_URL);
			vs.setCopyright(theJobMetadata.getCodeSystem().getCopyright());
			theContext.getIdToValueSet().put(valueSetId, vs);
			theData.addResourceToActivate("ValueSet/" + valueSetId);
		} else {
			vs = theContext.getIdToValueSet().get(valueSetId);
		}

		if (isBlank(vs.getName()) && isNotBlank(theValueSetName)) {
			vs.setName(theValueSetName);
		}

		return vs;
	}

	void addCodeAsIncludeToValueSet(ValueSet theVs, String theCodeSystemUrl, String theCode, String theDisplayName) {
		ValueSet.ConceptSetComponent include = null;
		for (ValueSet.ConceptSetComponent next : theVs.getCompose().getInclude()) {
			if (next.getSystem().equals(theCodeSystemUrl)) {
				include = next;
				break;
			}
		}
		if (include == null) {
			include = theVs.getCompose().addInclude();
			include.setSystem(theCodeSystemUrl);
			if (StringUtils.isNotBlank(theVs.getVersion())) {
				include.setVersion(theVs.getVersion());
			}
		}

		boolean found = false;
		for (ValueSet.ConceptReferenceComponent next : include.getConcept()) {
			if (next.getCode().equals(theCode)) {
				found = true;
			}
		}
		if (!found) {
			include.addConcept().setCode(theCode).setDisplay(theDisplayName);
		}
	}

	/**
	 * Invoked after all CSV rows have been processed but before the CodeSystem is submitted for storage.
	 * Subclasses may override, but they should call this super-method too.
	 */
	protected void syncToDb(
			ImportTerminologyMetadataAttachmentJson theJobMetadata,
			CT theCodeExtractionContext,
			CodeSystem theCodeSystemToPopulate,
			StepExecutionDetails<ImportLoincJobParameters, TerminologyFileSetJson> theStepExecutionDetails) {

		syncConceptsToDb(theStepExecutionDetails, theCodeSystemToPopulate);
		syncValueSetsToDb(theCodeExtractionContext, theStepExecutionDetails);
		syncConceptMapsToDb(theJobMetadata, theCodeExtractionContext, theStepExecutionDetails);
	}

	private void syncConceptsToDb(
			@Nonnull StepExecutionDetails<ImportLoincJobParameters, TerminologyFileSetJson> theStepExecutionDetails,
			CodeSystem codeSystemToPopulate) {
		if (codeSystemToPopulate.hasConcept()) {

			int conceptCount = codeSystemToPopulate.getConcept().size();
			ourLog.atInfo()
					.setMessage("Storing {} concepts")
					.addArgument(conceptCount)
					.log();

			Callable<UploadStatistics> uploader = () -> {
				IBaseResource codeSystemToPopulateNonCanonical =
						myVersionCanonicalizer.codeSystemFromCanonical(codeSystemToPopulate);
				return myTermCodeSystemStorageSvc.uploadCodeSystemConcepts(codeSystemToPopulateNonCanonical);
			};
			UploadStatistics uploadStatistics = executeInNewTransactionWithRetry(uploader, theStepExecutionDetails);

			TerminologyFileSetJson.RecordsAddedCounter recordsAddedCounter =
					getRecordsAddedCounter(theStepExecutionDetails);
			recordsAddedCounter.incrementConceptsAdded(uploadStatistics.getAddedConceptCount());
			recordsAddedCounter.incrementConceptLinksAdded(uploadStatistics.getAddedConceptLinkCount());
			recordsAddedCounter.incrementPropertiesAdded(uploadStatistics.getAddedPropertyCount());
			recordsAddedCounter.incrementDesignationsAdded(uploadStatistics.getAddedDesignationCount());
		}
	}

	private void syncConceptMapsToDb(
			ImportTerminologyMetadataAttachmentJson theJobMetadata,
			CT theCodeExtractionContext,
			StepExecutionDetails<ImportLoincJobParameters, TerminologyFileSetJson> theStepExecutionDetails) {
		IFhirResourceDao conceptMapDao = myDaoRegistry.getResourceDao("ConceptMap");
		for (Map.Entry<String, Collection<ConceptMapping>> entry :
				theCodeExtractionContext.getIdToConceptMappings().asMap().entrySet()) {

			String conceptMapId = entry.getKey();
			Collection<ConceptMapping> mappings = entry.getValue();

			executeInNewTransactionWithRetry(
					() -> {
						syncConceptMapToDb(
								theJobMetadata, theStepExecutionDetails, conceptMapId, conceptMapDao, mappings);
						return null;
					},
					theStepExecutionDetails);
		}
	}

	private void syncConceptMapToDb(
			ImportTerminologyMetadataAttachmentJson theJobMetadata,
			StepExecutionDetails<ImportLoincJobParameters, TerminologyFileSetJson> theStepExecutionDetails,
			String conceptMapId,
			IFhirResourceDao conceptMapDao,
			Collection<ConceptMapping> mappings) {
		ourLog.info("Checking for existence of ConceptMap: {}", conceptMapId);

		ConceptMap conceptMap;
		try {
			SystemRequestDetails requestDetails = theStepExecutionDetails.newSystemRequestDetails();

			IIdType existingId = myFhirContext.getVersion().newIdType(conceptMapId);
			IBaseResource conceptMapNonCanonical = conceptMapDao.read(existingId, requestDetails);
			conceptMap = myVersionCanonicalizer.conceptMapToCanonical(conceptMapNonCanonical);
			ourLog.info("Found existing ConceptMap: {}", conceptMapId);
			assert conceptMap != null : "Reading ConceptMap " + conceptMapId + " returned null";

		} catch (ResourceNotFoundException | ResourceGoneException e) {
			ConceptMapping firstMapping = mappings.iterator().next();

			ourLog.info("Creating new ConceptMap: {}", conceptMapId);
			getRecordsAddedCounter(theStepExecutionDetails).incrementConceptMapsAdded(1);

			conceptMap = new ConceptMap();
			conceptMap.setUrl(firstMapping.getConceptMapUri());
			conceptMap.setName(firstMapping.getConceptMapName());
			conceptMap.setVersion(firstMapping.getConceptMapVersion());
			conceptMap.setPublisher(REGENSTRIEF_INSTITUTE_INC);
			conceptMap
					.addContact()
					.setName(REGENSTRIEF_INSTITUTE_INC)
					.addTelecom()
					.setSystem(ContactPoint.ContactPointSystem.URL)
					.setValue(LOINC_WEBSITE_URL);

			String copyright = firstMapping.getCopyright();
			if (!copyright.contains("LOINC")) {
				String loincCopyrightStatement = theJobMetadata.getCodeSystem().getCopyright();
				copyright = loincCopyrightStatement + (loincCopyrightStatement.endsWith(".") ? " " : ". ") + copyright;
			}
			conceptMap.setCopyright(copyright);
		}

		int addedMappings = 0;
		int skippedMappings = 0;
		for (ConceptMapping nextMapping : mappings) {

			ConceptMap.SourceElementComponent source = null;
			ConceptMap.ConceptMapGroupComponent group = null;

			for (ConceptMap.ConceptMapGroupComponent next : conceptMap.getGroup()) {
				if (next.getSource().equals(nextMapping.getSourceCodeSystem())) {
					if (next.getTarget().equals(nextMapping.getTargetCodeSystem())) {
						if (!defaultString(nextMapping.getTargetCodeSystemVersion())
								.equals(defaultString(next.getTargetVersion()))) {
							continue;
						}
						group = next;
						break;
					}
				}
			}
			if (group == null) {
				group = conceptMap.addGroup();
				group.setSource(nextMapping.getSourceCodeSystem());
				group.setSourceVersion(nextMapping.getSourceCodeSystemVersion());
				group.setTarget(nextMapping.getTargetCodeSystem());
				group.setTargetVersion(defaultIfBlank(nextMapping.getTargetCodeSystemVersion(), null));
			}

			for (ConceptMap.SourceElementComponent next : group.getElement()) {
				if (next.getCode().equals(nextMapping.getSourceCode())) {
					source = next;
				}
			}
			if (source == null) {
				source = group.addElement();
				source.setCode(nextMapping.getSourceCode());
				source.setDisplay(nextMapping.getSourceDisplay());
			}

			boolean found = false;
			for (ConceptMap.TargetElementComponent next : source.getTarget()) {
				if (next.getCode().equals(nextMapping.getTargetCode())) {
					found = true;
				}
			}
			if (!found) {
				source.addTarget()
						.setCode(nextMapping.getTargetCode())
						.setDisplay(nextMapping.getTargetDisplay())
						.setEquivalence(nextMapping.getEquivalence());
				addedMappings++;
			} else {
				skippedMappings++;
				ourLog.atDebug()
						.setMessage("Not going to add a mapping from [{}/{}] to [{}/{}] because one already exists")
						.addArgument(nextMapping.getSourceCodeSystem())
						.addArgument(nextMapping.getSourceCode())
						.addArgument(nextMapping.getTargetCodeSystem())
						.addArgument(nextMapping.getTargetCode())
						.log();
			}
		}

		if (addedMappings > 0) {

			SystemRequestDetails requestDetails = theStepExecutionDetails.newSystemRequestDetails();
			IBaseResource nonCanonicalConceptMap = myVersionCanonicalizer.conceptMapFromCanonical(conceptMap);
			if (conceptMap.getId() == null) {
				/*
				 * Create but with an assigned ID. We do this as a create instead of an update
				 * in order to avoid the possibility of a race condition where a new ConceptMap
				 * is created by another thread while we are trying to also create it here, since
				 * this would result in us overwriting the other thread's ConceptMap.
				 */
				nonCanonicalConceptMap.setId(conceptMapId);
				nonCanonicalConceptMap.setUserData(JpaConstants.RESOURCE_ID_SERVER_ASSIGNED_VALUE, conceptMapId);
				conceptMapDao.create(nonCanonicalConceptMap, requestDetails);
			} else {
				conceptMapDao.update(nonCanonicalConceptMap, requestDetails);
			}

			getRecordsAddedCounter(theStepExecutionDetails).incrementConceptMapMappingsAdded(addedMappings);
		}

		ourLog.atInfo()
				.setMessage("Adding {} mappings and skipped {} pre-existing mappings to LOINC ConceptMap {}")
				.addArgument(addedMappings)
				.addArgument(skippedMappings)
				.addArgument(conceptMap.getId())
				.log();
	}

	private void syncValueSetsToDb(
			CT theCodeExtractionContext,
			StepExecutionDetails<ImportLoincJobParameters, TerminologyFileSetJson> theStepExecutionDetails) {
		if (!theCodeExtractionContext.getIdToValueSet().isEmpty()) {
			IFhirResourceDao valueSetDao = myDaoRegistry.getResourceDao("ValueSet");
			for (ValueSet valueSet : theCodeExtractionContext.getIdToValueSet().values()) {
				executeInNewTransactionWithRetry(
						() -> {
							syncValueSetToDb(theStepExecutionDetails, valueSet, valueSetDao);
							return null;
						},
						theStepExecutionDetails);
			}
		}
	}

	private void syncValueSetToDb(
			StepExecutionDetails<ImportLoincJobParameters, TerminologyFileSetJson> theStepExecutionDetails,
			ValueSet valueSet,
			IFhirResourceDao valueSetDao) {
		try {
			SystemRequestDetails requestDetails = theStepExecutionDetails.newSystemRequestDetails();
			IIdType existingId =
					myFhirContext.getVersion().newIdType(valueSet.getIdElement().getIdPart());
			IBaseResource valueSetNonCanonical = valueSetDao.read(existingId, requestDetails);
			ValueSet existing = myVersionCanonicalizer.valueSetToCanonical(valueSetNonCanonical);
			assert existing != null : "Reading ValueSet " + valueSet.getId() + " returned null";

			/*
			 * A ValueSet already exists with the given ID, so we'll merge the contents
			 * of our ValueSet into it and save it.
			 */

			int addedCodes = 0;

			for (ValueSet.ConceptSetComponent sourceInclude :
					valueSet.getCompose().getInclude()) {
				ValueSet.ConceptSetComponent targetInclude = findOrAddMatchingConceptSetComponent(
						existing.getCompose().getInclude(), sourceInclude);

				// Add codes
				Set<String> existingCodes = targetInclude.getConcept().stream()
						.map(ValueSet.ConceptReferenceComponent::getCode)
						.collect(Collectors.toSet());
				for (ValueSet.ConceptReferenceComponent toAdd : sourceInclude.getConcept()) {
					if (!existingCodes.contains(toAdd.getCode())) {
						existing.getCompose().getIncludeFirstRep().addConcept(toAdd);
						addedCodes++;
					}
				}
			}

			if (isNotBlank(valueSet.getName())) {
				getRecordsAddedCounter(theStepExecutionDetails).incrementOtherChanges(1);
				existing.setName(valueSet.getName());
			}

			ourLog.atInfo()
					.setMessage("Updating existing LOINC ValueSet {} to add {} codes")
					.addArgument(valueSet.getId())
					.addArgument(addedCodes)
					.log();

			requestDetails = theStepExecutionDetails.newSystemRequestDetails();
			valueSetDao.update(myVersionCanonicalizer.valueSetFromCanonical(existing), requestDetails);

			getRecordsAddedCounter(theStepExecutionDetails).incrementValueSetCodesAdded(addedCodes);

		} catch (ResourceNotFoundException | ResourceGoneException e) {

			/*
			 * Ok, we didn't find an existing ValueSet with the given ID, so we'll
			 * store the ValueSet as a new one.
			 */
			getRecordsAddedCounter(theStepExecutionDetails).incrementValueSetsAdded(1);

			int codeCount = 0;
			if (valueSet.hasCompose()
					&& valueSet.getCompose().hasInclude()
					&& valueSet.getCompose().getIncludeFirstRep().hasConcept()) {
				codeCount = Math.toIntExact(
						valueSet.getCompose().getIncludeFirstRep().getConcept().size());
			}

			ourLog.atInfo()
					.setMessage("Creating new LOINC ValueSet {} with {} code inclusions")
					.addArgument(valueSet.getId())
					.addArgument(codeCount)
					.log();
			SystemRequestDetails requestDetails = theStepExecutionDetails.newSystemRequestDetails();

			/*
			 * Create but with an assigned ID. We do this as a create instead of an update
			 * in order to avoid the possibility of a race condition where a new ValueSet
			 * is created by another thread while we are trying to also create it here, since
			 * this would result in us overwriting the other thread's ValueSet.
			 */
			IBaseResource nonCanonicalValueSet = myVersionCanonicalizer.valueSetFromCanonical(valueSet);
			nonCanonicalValueSet.setUserData(
					JpaConstants.RESOURCE_ID_SERVER_ASSIGNED_VALUE,
					valueSet.getIdElement().getIdPart());
			valueSetDao.create(nonCanonicalValueSet, requestDetails);

			getRecordsAddedCounter(theStepExecutionDetails).incrementValueSetCodesAdded(codeCount);
		}
	}

	private ValueSet.ConceptSetComponent findOrAddMatchingConceptSetComponent(
			List<ValueSet.ConceptSetComponent> theTargetList, ValueSet.ConceptSetComponent theSetToFind) {
		ConceptSetComponentIdentity toFind = new ConceptSetComponentIdentity(theSetToFind);
		for (ValueSet.ConceptSetComponent next : theTargetList) {
			ConceptSetComponentIdentity nextIdentity = new ConceptSetComponentIdentity(next);
			if (toFind.equals(nextIdentity)) {
				return next;
			}
		}

		// Not found
		ValueSet.ConceptSetComponent newSet = new ValueSet.ConceptSetComponent();
		theTargetList.add(newSet);

		newSet.setSystem(theSetToFind.getSystem());
		newSet.setValueSet(theSetToFind.getValueSet());
		return newSet;
	}

	private record ConceptSetComponentIdentity(String system, Set<String> valueSets) {
		public ConceptSetComponentIdentity(ValueSet.ConceptSetComponent theSetToFind) {
			this(
					theSetToFind.getSystem(),
					theSetToFind.getValueSet().stream()
							.map(PrimitiveType::getValue)
							.filter(StringUtils::isNotBlank)
							.collect(Collectors.toSet()));
		}
	}

	protected static class ConceptMapping {

		private String myCopyright;
		private String myConceptMapId;
		private String myConceptMapUri;
		private String myConceptMapVersion;
		private String myConceptMapName;
		private String mySourceCodeSystem;
		private String mySourceCodeSystemVersion;
		private String mySourceCode;
		private String mySourceDisplay;
		private String myTargetCodeSystem;
		private String myTargetCode;
		private String myTargetDisplay;
		private Enumerations.ConceptMapEquivalence myEquivalence;
		private String myTargetCodeSystemVersion;

		String getConceptMapId() {
			return myConceptMapId;
		}

		ConceptMapping setConceptMapId(String theConceptMapId) {
			myConceptMapId = theConceptMapId;
			return this;
		}

		String getConceptMapName() {
			return myConceptMapName;
		}

		ConceptMapping setConceptMapName(String theConceptMapName) {
			myConceptMapName = theConceptMapName;
			return this;
		}

		String getConceptMapUri() {
			return myConceptMapUri;
		}

		ConceptMapping setConceptMapUri(String theConceptMapUri) {
			myConceptMapUri = theConceptMapUri;
			return this;
		}

		String getConceptMapVersion() {
			return myConceptMapVersion;
		}

		ConceptMapping setConceptMapVersion(String theConceptMapVersion) {
			myConceptMapVersion = theConceptMapVersion;
			return this;
		}

		String getCopyright() {
			return myCopyright;
		}

		ConceptMapping setCopyright(String theCopyright) {
			myCopyright = theCopyright;
			return this;
		}

		Enumerations.ConceptMapEquivalence getEquivalence() {
			return myEquivalence;
		}

		ConceptMapping setEquivalence(Enumerations.ConceptMapEquivalence theEquivalence) {
			myEquivalence = theEquivalence;
			return this;
		}

		String getSourceCode() {
			return mySourceCode;
		}

		ConceptMapping setSourceCode(String theSourceCode) {
			mySourceCode = theSourceCode;
			return this;
		}

		String getSourceCodeSystem() {
			return mySourceCodeSystem;
		}

		ConceptMapping setSourceCodeSystem(String theSourceCodeSystem) {
			mySourceCodeSystem = theSourceCodeSystem;
			return this;
		}

		String getSourceCodeSystemVersion() {
			return mySourceCodeSystemVersion;
		}

		ConceptMapping setSourceCodeSystemVersion(String theSourceCodeSystemVersion) {
			mySourceCodeSystemVersion = theSourceCodeSystemVersion;
			return this;
		}

		String getSourceDisplay() {
			return mySourceDisplay;
		}

		ConceptMapping setSourceDisplay(String theSourceDisplay) {
			mySourceDisplay = theSourceDisplay;
			return this;
		}

		String getTargetCode() {
			return myTargetCode;
		}

		ConceptMapping setTargetCode(String theTargetCode) {
			myTargetCode = theTargetCode;
			return this;
		}

		String getTargetCodeSystem() {
			return myTargetCodeSystem;
		}

		ConceptMapping setTargetCodeSystem(String theTargetCodeSystem) {
			myTargetCodeSystem = theTargetCodeSystem;
			return this;
		}

		String getTargetCodeSystemVersion() {
			return myTargetCodeSystemVersion;
		}

		ConceptMapping setTargetCodeSystemVersion(String theTargetCodeSystemVersion) {
			myTargetCodeSystemVersion = theTargetCodeSystemVersion;
			return this;
		}

		String getTargetDisplay() {
			return myTargetDisplay;
		}

		ConceptMapping setTargetDisplay(String theTargetDisplay) {
			myTargetDisplay = theTargetDisplay;
			return this;
		}

		@Override
		public boolean equals(Object theO) {
			if (!(theO instanceof ConceptMapping that)) {
				return false;
			}
			return Objects.equals(myCopyright, that.myCopyright)
					&& Objects.equals(myConceptMapId, that.myConceptMapId)
					&& Objects.equals(myConceptMapUri, that.myConceptMapUri)
					&& Objects.equals(myConceptMapVersion, that.myConceptMapVersion)
					&& Objects.equals(myConceptMapName, that.myConceptMapName)
					&& Objects.equals(mySourceCodeSystem, that.mySourceCodeSystem)
					&& Objects.equals(mySourceCodeSystemVersion, that.mySourceCodeSystemVersion)
					&& Objects.equals(mySourceCode, that.mySourceCode)
					&& Objects.equals(mySourceDisplay, that.mySourceDisplay)
					&& Objects.equals(myTargetCodeSystem, that.myTargetCodeSystem)
					&& Objects.equals(myTargetCode, that.myTargetCode)
					&& Objects.equals(myTargetDisplay, that.myTargetDisplay)
					&& myEquivalence == that.myEquivalence
					&& Objects.equals(myTargetCodeSystemVersion, that.myTargetCodeSystemVersion);
		}

		@Override
		public int hashCode() {
			return Objects.hash(
					myCopyright,
					myConceptMapId,
					myConceptMapUri,
					myConceptMapVersion,
					myConceptMapName,
					mySourceCodeSystem,
					mySourceCodeSystemVersion,
					mySourceCode,
					mySourceDisplay,
					myTargetCodeSystem,
					myTargetCode,
					myTargetDisplay,
					myEquivalence,
					myTargetCodeSystemVersion);
		}
	}

	protected record LoincFileNameSpecification(
			FileHandlingType fileHandlingType,
			LoincUploadPropertiesEnum propertyName,
			List<LoincUploadPropertiesEnum> defaultValues,
			Predicate<String> fileNameTester) {

		protected LoincFileNameSpecification(
				FileHandlingType theFileHandlingType,
				LoincUploadPropertiesEnum thePropertyName,
				LoincUploadPropertiesEnum... theDefaultValue) {
			this(theFileHandlingType, thePropertyName, Arrays.asList(theDefaultValue), null);
		}

		protected LoincFileNameSpecification(
				FileHandlingType theFileHandlingType, Predicate<String> theFileNameTester) {
			this(theFileHandlingType, null, null, theFileNameTester);
		}

		public boolean matchFileName(Properties theJobProperties, String theFileName) {
			boolean matches = false;
			if (propertyName() != null) {
				String propertyName = propertyName().getCode();
				String fileName = theJobProperties.getProperty(propertyName, null);
				if (isNotBlank(fileName)) {
					matches = theFileName.endsWith(fileName);
				} else {
					for (LoincUploadPropertiesEnum nextDefault : defaultValues()) {
						matches |= theFileName.endsWith(nextDefault.getCode());
					}
				}
			} else if (this.fileNameTester() != null) {
				matches = this.fileNameTester().test(theFileName);
			}
			return matches;
		}
	}

	protected static class MyBaseContext {

		private final Map<String, ValueSet> myIdToValueSet = new HashMap<>();
		private final SetMultimap<String, BaseImportLoincStep.ConceptMapping> myIdToConceptMappings =
				MultimapBuilder.hashKeys().linkedHashSetValues().build();
		private final Map<String, CodeSystem.ConceptDefinitionComponent> myCodeToConcept = new HashMap<>();
		private Map<String, CodeSystem.PropertyType> myPropertyNameToType;

		public MyBaseContext() {}

		public Map<String, CodeSystem.ConceptDefinitionComponent> getCodeToConcept() {
			return myCodeToConcept;
		}

		public SetMultimap<String, BaseImportLoincStep.ConceptMapping> getIdToConceptMappings() {
			return myIdToConceptMappings;
		}

		public Map<String, ValueSet> getIdToValueSet() {
			return myIdToValueSet;
		}

		public Map<String, CodeSystem.PropertyType> getPropertyNameToType(
				ImportTerminologyMetadataAttachmentJson theJobMetadata) {
			if (myPropertyNameToType != null) {
				return myPropertyNameToType;
			}
			Map<String, CodeSystem.PropertyType> propertyNameToType = new HashMap<>();
			for (CodeSystem.PropertyComponent nextProperty :
					theJobMetadata.getCodeSystem().getProperty()) {
				String nextPropertyCode = nextProperty.getCode();
				CodeSystem.PropertyType nextPropertyType = nextProperty.getType();
				if (isNotBlank(nextPropertyCode)) {
					propertyNameToType.put(nextPropertyCode, nextPropertyType);
				}
			}
			myPropertyNameToType = propertyNameToType;
			return propertyNameToType;
		}
	}
}
