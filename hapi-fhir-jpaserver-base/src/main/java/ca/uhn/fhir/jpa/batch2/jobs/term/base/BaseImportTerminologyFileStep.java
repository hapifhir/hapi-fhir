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
package ca.uhn.fhir.jpa.batch2.jobs.term.base;

import ca.uhn.fhir.batch2.api.*;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.LookupCodeRequest;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.batch2.jobs.term.loinc.BaseImportLoincStep;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.term.UploadStatistics;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import ca.uhn.fhir.system.HapiSystemProperties;
import ca.uhn.fhir.util.StopWatch;
import ca.uhn.hapi.converters.canonical.VersionCanonicalizer;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.SetMultimap;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.time.DateUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import java.util.*;

import static ca.uhn.fhir.jpa.batch2.jobs.term.loinc.ImportLoincJobAppCtx.STEP_ID_FINALIZE_IMPORT;
import static ca.uhn.fhir.util.TestUtil.sleepAtLeast;
import static org.apache.commons.lang3.StringUtils.*;
import static org.hl7.fhir.common.hapi.validation.support.ValidationConstants.LOINC_GENERIC_CODE_SYSTEM_URL;

public abstract class BaseImportTerminologyFileStep<
				PT extends ImportTerminologyJobParameters, CT extends BaseImportTerminologyFileCsvStep.MyBaseContext>
		extends BaseImportTerminologyStep
		implements ITerminologyImportFileHandlerStep<PT, TerminologyFileSetJson, TerminologyFileSetJson> {

	private static final Logger ourLog = LoggerFactory.getLogger(BaseImportTerminologyFileStep.class);

	@Autowired
	protected FhirContext myFhirContext;

	@Autowired
	protected VersionCanonicalizer myVersionCanonicalizer;

	@Autowired
	protected IJobPersistence myJobPersistence;

	@Autowired
	private ITermCodeSystemStorageSvc myTermCodeSystemStorageSvc;

	@Autowired
	private IHapiTransactionService myTransactionService;

	@Autowired
	private IValidationSupport myValidationSupport;

	@Autowired
	private DaoRegistry myDaoRegistry;

	@Override
	public boolean mustFindFile() {
		return true;
	}

	@Nonnull
	@Override
	public RunOutcome run(
			@Nonnull StepExecutionDetails<PT, TerminologyFileSetJson> theStepExecutionDetails,
			@Nonnull IJobDataSink<TerminologyFileSetJson> theDataSink)
			throws JobExecutionFailedException {

		CT codeExtractionContext = newContextObject(theStepExecutionDetails);

		ImportTerminologyMetadataAttachmentJson jobMetadata =
				getJobMetadata(theStepExecutionDetails.getInstance().getInstanceId());

		return run(theStepExecutionDetails, theDataSink, jobMetadata, codeExtractionContext);
	}

	@Nonnull
	protected RunOutcome run(
			@Nonnull StepExecutionDetails<PT, TerminologyFileSetJson> theStepExecutionDetails,
			@Nonnull IJobDataSink<TerminologyFileSetJson> theDataSink,
			ImportTerminologyMetadataAttachmentJson theJobMetadata,
			CT theContext) {
		String jobInstanceId = theStepExecutionDetails.getInstance().getInstanceId();

		TerminologyFileSetJson theData = theStepExecutionDetails.getData();
		PT jobParameters = theStepExecutionDetails.getParameters();

		CodeSystem codeSystemToPopulate = new CodeSystem();
		codeSystemToPopulate.setUrl(theJobMetadata.getCodeSystem().getUrl());
		codeSystemToPopulate.setVersion(theJobMetadata.getCodeSystemStagingVersionId());

		String attachmentId = theData.getAttachmentId();
		String sourceFilename = theData.getSourceFilename();
		if (isNotBlank(attachmentId)) {

			AttachmentDetails attachment = myJobPersistence.fetchAttachmentById(jobInstanceId, attachmentId);
			processAttachment(
					theStepExecutionDetails,
					theJobMetadata,
					theContext,
					attachment,
					jobParameters,
					codeSystemToPopulate,
					theData,
					sourceFilename);

			syncToDb(theJobMetadata, theContext, codeSystemToPopulate, theStepExecutionDetails);
		}

		if (!theData.getStepIdToRecordsAdded().isEmpty()
				|| !theData.getResourcesToActivate().isEmpty()) {
			TerminologyFileSetJson counterWorkChunk = new TerminologyFileSetJson();
			counterWorkChunk.getStepIdToRecordsAdded().putAll(theData.getStepIdToRecordsAdded());
			counterWorkChunk.getResourcesToActivate().addAll(theData.getResourcesToActivate());
			theDataSink.acceptForFutureStep(STEP_ID_FINALIZE_IMPORT, counterWorkChunk);
		}

		return RunOutcome.SUCCESS;
	}

	protected abstract void processAttachment(
			@Nonnull StepExecutionDetails<PT, TerminologyFileSetJson> theStepExecutionDetails,
			ImportTerminologyMetadataAttachmentJson theJobMetadata,
			CT theContext,
			AttachmentDetails attachment,
			PT jobParameters,
			CodeSystem codeSystemToPopulate,
			TerminologyFileSetJson theData,
			String sourceFilename);

	protected <T> T executeInNewTransactionWithRetry(
			Callable<T> theFunction, StepExecutionDetails<PT, TerminologyFileSetJson> theStepExecutionDetails) {
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
					ourLog.atError()
							.setMessage("Failed to saver terminology due to version conflict after {} retries: {}")
							.addArgument(retryCount)
							.addArgument(e.getMessage())
							.log();
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

	protected abstract CT newContextObject(StepExecutionDetails<PT, TerminologyFileSetJson> theStepExecutionDetails);

	protected TerminologyFileSetJson.RecordsAddedCounter getRecordsAddedCounter(
			StepExecutionDetails<PT, TerminologyFileSetJson> theStepExecutionDetails) {

		TerminologyFileSetJson data = theStepExecutionDetails.getData();
		String currentStepId = theStepExecutionDetails.getCurrentStepId();
		return data.getRecordsAddedCounter(currentStepId);
	}

	@Nullable
	protected IValidationSupport.LookupCodeResult lookupPreExistingConcept(
			ImportTerminologyMetadataAttachmentJson theJobMetadata, String propertyCodeValue) {
		String version = theJobMetadata.getCodeSystemStagingVersionId();
		LookupCodeRequest request =
				new LookupCodeRequest(LOINC_GENERIC_CODE_SYSTEM_URL + "|" + version, propertyCodeValue);
		return myValidationSupport.lookupCode(new ValidationSupportContext(myValidationSupport), request);
	}

	@Nonnull
	protected CodeSystem.ConceptDefinitionComponent getOrAddConcept(MyBaseContext theContext, String theCode) {

		CodeSystem.ConceptDefinitionComponent code =
				theContext.getCodeToConcept().get(theCode);
		if (code == null) {
			code = new CodeSystem.ConceptDefinitionComponent();
			code.setCode(theCode);
			theContext.getCodeToConcept().put(theCode, code);
		}
		return code;
	}

	protected void getOrAddParentChildHierarchy(MyBaseContext theContext, String theParentCode, String theChildCode) {
		if (isNotBlank(theParentCode) && isNotBlank(theChildCode)) {
			Map<String, CodeSystem.ConceptDefinitionComponent> codeToConceptMap = theContext.getCodeToConcept();
			CodeSystem.ConceptDefinitionComponent parentConcept =
					codeToConceptMap.computeIfAbsent(theParentCode, this::newConcept);
			CodeSystem.ConceptDefinitionComponent childConcept =
					codeToConceptMap.computeIfAbsent(theChildCode, this::newConcept);

			if (parentConcept.getConcept().stream().noneMatch(t -> t.getCode().equals(theChildCode))) {
				parentConcept.addConcept(childConcept);
			}
		}
	}

	private CodeSystem.ConceptDefinitionComponent newConcept(String theCode) {
		CodeSystem.ConceptDefinitionComponent retVal = new CodeSystem.ConceptDefinitionComponent();
		retVal.setCode(theCode);
		return retVal;
	}

	protected void addConceptMapEntry(MyBaseContext theContext, ConceptMapping theMapping) {
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
			StepExecutionDetails<PT, TerminologyFileSetJson> theStepExecutionDetails,
			ImportTerminologyMetadataAttachmentJson theJobMetadata,
			TerminologyFileSetJson theData,
			MyBaseContext theContext,
			String theValueSetId,
			String theValueSetUri,
			String theValueSetName,
			String theVersionPropertyName) {

		String version;
		String codeSystemVersion = theJobMetadata.getCodeSystem().getVersion();
		assert isNotBlank(codeSystemVersion);

		Properties jobProperties = ImportTerminologyUtil.getJobProperties(myJobPersistence, theStepExecutionDetails);
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
			vs.setPublisher(BaseImportLoincStep.REGENSTRIEF_INSTITUTE_INC);
			vs.addContact()
					.setName(BaseImportLoincStep.REGENSTRIEF_INSTITUTE_INC)
					.addTelecom()
					.setSystem(ContactPoint.ContactPointSystem.URL)
					.setValue(BaseImportLoincStep.LOINC_WEBSITE_URL);
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

	protected void addCodeAsIncludeToValueSet(
			ValueSet theVs, String theCodeSystemUrl, String theCode, String theDisplayName) {
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
			StepExecutionDetails<PT, TerminologyFileSetJson> theStepExecutionDetails) {

		syncConceptsToDb(theStepExecutionDetails, theCodeExtractionContext, theCodeSystemToPopulate);
		syncValueSetsToDb(theCodeExtractionContext, theStepExecutionDetails);
		syncConceptMapsToDb(theJobMetadata, theCodeExtractionContext, theStepExecutionDetails);
	}

	private void syncConceptsToDb(
			@Nonnull StepExecutionDetails<PT, TerminologyFileSetJson> theStepExecutionDetails,
			CT theCodeExtractionContext,
			CodeSystem codeSystemToPopulate) {
		if (!theCodeExtractionContext.getCodeToConcept().isEmpty()) {

			populateConceptsIntoCodeSystem(theCodeExtractionContext.getCodeToConcept(), codeSystemToPopulate);

			StopWatch sw = new StopWatch();

			Callable<UploadStatistics> uploader = () -> {
				IBaseResource codeSystemToPopulateNonCanonical =
						myVersionCanonicalizer.codeSystemFromCanonical(codeSystemToPopulate);
				return myTermCodeSystemStorageSvc.uploadCodeSystemConcepts(codeSystemToPopulateNonCanonical);
			};
			UploadStatistics uploadStatistics = executeInNewTransactionWithRetry(uploader, theStepExecutionDetails);

			ourLog.info(
					"Imported {} concept entries including {} root concept entries for storage in {}. Outcome: {}",
					theCodeExtractionContext.getCodeToConcept().size(),
					codeSystemToPopulate.getConcept().size(),
					sw,
					uploadStatistics);

			TerminologyFileSetJson.RecordsAddedCounter recordsAddedCounter =
					getRecordsAddedCounter(theStepExecutionDetails);
			recordsAddedCounter.incrementConceptsAdded(uploadStatistics.getAddedConceptCount());
			recordsAddedCounter.incrementConceptLinksAdded(uploadStatistics.getAddedConceptLinkCount());
			recordsAddedCounter.incrementPropertiesAdded(uploadStatistics.getAddedPropertyCount());
			recordsAddedCounter.incrementDesignationsAdded(uploadStatistics.getAddedDesignationCount());
		}
	}

	private static void populateConceptsIntoCodeSystem(
			Map<String, CodeSystem.ConceptDefinitionComponent> codeToConcept, CodeSystem codeSystemToPopulate) {
		// Figure out which concepts are at the very top of the hierarchy (i.e. not children of any other concept)
		// and put them at the root of the CodeSystem to upload

		Set<String> childCodes = new HashSet<>();

		for (CodeSystem.ConceptDefinitionComponent concept : codeToConcept.values()) {
			for (CodeSystem.ConceptDefinitionComponent childConcept : concept.getConcept()) {
				childCodes.add(childConcept.getCode());
			}
		}

		// If there is a circular hierarchy chain, we want to still add the concepts. This isn't valid
		// but it can happen. The terminology service will handle this gracefully.

		IdentityHashMap<CodeSystem.ConceptDefinitionComponent, CodeSystem.ConceptDefinitionComponent> added = new IdentityHashMap<>();

		for (CodeSystem.ConceptDefinitionComponent concept : codeToConcept.values()) {
			if (!childCodes.contains(concept.getCode())) {
				codeSystemToPopulate.addConcept(concept);
				added.put(concept, concept);
				addChildrenToIdentityMap(added, concept);
			}
		}

		for (CodeSystem.ConceptDefinitionComponent concept : codeToConcept.values()) {
			if (!added.containsKey(concept)) {
				codeSystemToPopulate.addConcept(concept);
			}
		}

	}

	private static void addChildrenToIdentityMap(IdentityHashMap<CodeSystem.ConceptDefinitionComponent, CodeSystem.ConceptDefinitionComponent> theAdded, CodeSystem.ConceptDefinitionComponent theConcept) {
		for (CodeSystem.ConceptDefinitionComponent childConcept : theConcept.getConcept()) {
			if (theAdded.put(childConcept, childConcept) == null) {
				addChildrenToIdentityMap(theAdded, childConcept);
			}
		}
	}

	private void syncConceptMapsToDb(
			ImportTerminologyMetadataAttachmentJson theJobMetadata,
			MyBaseContext theCodeExtractionContext,
			StepExecutionDetails<PT, TerminologyFileSetJson> theStepExecutionDetails) {
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
			StepExecutionDetails<PT, TerminologyFileSetJson> theStepExecutionDetails,
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
			conceptMap.setPublisher(BaseImportLoincStep.REGENSTRIEF_INSTITUTE_INC);
			conceptMap
					.addContact()
					.setName(BaseImportLoincStep.REGENSTRIEF_INSTITUTE_INC)
					.addTelecom()
					.setSystem(ContactPoint.ContactPointSystem.URL)
					.setValue(BaseImportLoincStep.LOINC_WEBSITE_URL);

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
			MyBaseContext theCodeExtractionContext,
			StepExecutionDetails<PT, TerminologyFileSetJson> theStepExecutionDetails) {
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
			StepExecutionDetails<PT, TerminologyFileSetJson> theStepExecutionDetails,
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

	public static class ConceptMapping {

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

		public ConceptMapping setConceptMapId(String theConceptMapId) {
			myConceptMapId = theConceptMapId;
			return this;
		}

		String getConceptMapName() {
			return myConceptMapName;
		}

		public ConceptMapping setConceptMapName(String theConceptMapName) {
			myConceptMapName = theConceptMapName;
			return this;
		}

		String getConceptMapUri() {
			return myConceptMapUri;
		}

		public ConceptMapping setConceptMapUri(String theConceptMapUri) {
			myConceptMapUri = theConceptMapUri;
			return this;
		}

		String getConceptMapVersion() {
			return myConceptMapVersion;
		}

		public ConceptMapping setConceptMapVersion(String theConceptMapVersion) {
			myConceptMapVersion = theConceptMapVersion;
			return this;
		}

		String getCopyright() {
			return myCopyright;
		}

		public ConceptMapping setCopyright(String theCopyright) {
			myCopyright = theCopyright;
			return this;
		}

		Enumerations.ConceptMapEquivalence getEquivalence() {
			return myEquivalence;
		}

		public ConceptMapping setEquivalence(Enumerations.ConceptMapEquivalence theEquivalence) {
			myEquivalence = theEquivalence;
			return this;
		}

		String getSourceCode() {
			return mySourceCode;
		}

		public ConceptMapping setSourceCode(String theSourceCode) {
			mySourceCode = theSourceCode;
			return this;
		}

		String getSourceCodeSystem() {
			return mySourceCodeSystem;
		}

		public ConceptMapping setSourceCodeSystem(String theSourceCodeSystem) {
			mySourceCodeSystem = theSourceCodeSystem;
			return this;
		}

		String getSourceCodeSystemVersion() {
			return mySourceCodeSystemVersion;
		}

		public ConceptMapping setSourceCodeSystemVersion(String theSourceCodeSystemVersion) {
			mySourceCodeSystemVersion = theSourceCodeSystemVersion;
			return this;
		}

		String getSourceDisplay() {
			return mySourceDisplay;
		}

		public ConceptMapping setSourceDisplay(String theSourceDisplay) {
			mySourceDisplay = theSourceDisplay;
			return this;
		}

		String getTargetCode() {
			return myTargetCode;
		}

		public ConceptMapping setTargetCode(String theTargetCode) {
			myTargetCode = theTargetCode;
			return this;
		}

		String getTargetCodeSystem() {
			return myTargetCodeSystem;
		}

		public ConceptMapping setTargetCodeSystem(String theTargetCodeSystem) {
			myTargetCodeSystem = theTargetCodeSystem;
			return this;
		}

		String getTargetCodeSystemVersion() {
			return myTargetCodeSystemVersion;
		}

		public ConceptMapping setTargetCodeSystemVersion(String theTargetCodeSystemVersion) {
			myTargetCodeSystemVersion = theTargetCodeSystemVersion;
			return this;
		}

		String getTargetDisplay() {
			return myTargetDisplay;
		}

		public ConceptMapping setTargetDisplay(String theTargetDisplay) {
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

	public static class MyBaseContext {

		private final Map<String, ValueSet> myIdToValueSet = new HashMap<>();
		private final SetMultimap<String, ConceptMapping> myIdToConceptMappings =
				MultimapBuilder.hashKeys().linkedHashSetValues().build();
		private final Map<String, CodeSystem.ConceptDefinitionComponent> myCodeToConcept = new LinkedHashMap<>();
		private Map<String, CodeSystem.PropertyType> myPropertyNameToType;

		public MyBaseContext() {}

		public Map<String, CodeSystem.ConceptDefinitionComponent> getCodeToConcept() {
			return myCodeToConcept;
		}

		public SetMultimap<String, ConceptMapping> getIdToConceptMappings() {
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
