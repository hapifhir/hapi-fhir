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
package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyConstants;
import ca.uhn.fhir.jpa.batch2.jobs.term.valueset.preexpand.PreExpandValueSetJobAppCtx;
import ca.uhn.fhir.jpa.batch2.jobs.term.valueset.preexpand.PreExpandValueSetParameters;
import ca.uhn.fhir.jpa.dao.data.ITermValueSetConceptDao;
import ca.uhn.fhir.jpa.dao.data.ITermValueSetConceptDesignationDao;
import ca.uhn.fhir.jpa.dao.data.ITermValueSetDao;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.entity.TermValueSet;
import ca.uhn.fhir.jpa.entity.TermValueSetConcept;
import ca.uhn.fhir.jpa.entity.TermValueSetConceptDesignation;
import ca.uhn.fhir.jpa.entity.TermValueSetPreExpansionStatusEnum;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.term.api.ITermReadSvc;
import ca.uhn.fhir.jpa.term.api.ITermValueSetStorageSvc;
import ca.uhn.fhir.jpa.util.QueryChunker;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.util.IntCounter;
import ca.uhn.fhir.util.StopWatch;
import ca.uhn.fhir.util.UrlUtil;
import ca.uhn.fhir.util.ValidateUtil;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.SetMultimap;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import org.hl7.fhir.r4.model.DecimalType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.ValueSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import static ca.uhn.fhir.jpa.term.TermReadSvcImpl.isPlaceholder;
import static org.apache.commons.lang3.ObjectUtils.getIfNull;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class TermValueSetStorageSvcImpl implements ITermValueSetStorageSvc {
	public static final String INTENDED_VERSION_ID_NULL = "(null)";
	private static final Logger ourLog = LoggerFactory.getLogger(TermValueSetStorageSvcImpl.class);

	@Autowired
	private JpaStorageSettings myStorageSettings;

	@Autowired
	private FhirContext myContext;

	@Autowired
	private ITermReadSvc myTermReadSvc;

	@Autowired
	private EntityManager myEntityManager;

	@Autowired
	private ITermValueSetDao myTermValueSetDao;

	@Autowired
	private ITermValueSetConceptDao myTermValueSetConceptDao;

	@Autowired
	private ITermValueSetConceptDesignationDao myTermValueSetConceptDesignationDao;

	@Autowired
	private IHapiTransactionService myTxService;

	@Autowired
	private ApplicationContext myApplicationContext;

	@Autowired
	private IJobCoordinator myJobCoordinator;

	private IValidationSupport myValidationSupport;

	@Nonnull
	@Override
	public String startStagingVersion(@Nonnull String theUrl, @Nullable String theVersion) {
		return myTxService.withSystemRequestOnDefaultPartition().execute(() -> {
			TermValueSet valueSet = fetchTermValueSet(theUrl, theVersion);
			valueSet.setExpansionStatus(TermValueSetPreExpansionStatusEnum.EXPANSION_IN_PROGRESS);
			myTermValueSetDao.save(valueSet);

			String stagingVersionId = UUID.randomUUID().toString();

			TermValueSet stagingValueSet = new TermValueSet();
			stagingValueSet.setPartitionId(valueSet.getPartitionId());
			stagingValueSet.setExpansionStatus(TermValueSetPreExpansionStatusEnum.EXPANSION_IN_PROGRESS);
			stagingValueSet.setUrl(valueSet.getUrl());
			stagingValueSet.setVersion(stagingVersionId);
			stagingValueSet.setName(valueSet.getName());
			stagingValueSet.setIntendedVersionId(getIfNull(theVersion, INTENDED_VERSION_ID_NULL));
			stagingValueSet.setResource(valueSet.getResource());
			myTermValueSetDao.save(stagingValueSet);

			ourLog.atInfo()
					.setMessage("Starting staging version for ValueSet Url[{}] Version[{}] with staging version: {}")
					.addArgument(valueSet.getUrl())
					.addArgument(valueSet.getVersion())
					.addArgument(stagingVersionId)
					.log();

			return stagingVersionId;
		});
	}

	@Nonnull
	@Override
	public UploadStatistics addConceptsToExpansion(@Nonnull ValueSet theDelta, int theStartingOrder) {
		StopWatch sw = new StopWatch();
		UploadStatistics statistics = new UploadStatistics();
		String url = theDelta.getUrl();
		String version = theDelta.getVersion();

		myTxService.withSystemRequestOnDefaultPartition().execute(() -> {
			FlattenedValueSet flattenedValueSet = flattenValueSet(theDelta);
			IntCounter order = new IntCounter(theStartingOrder);
			Map<SystemAndCode, TermValueSetConcept> codeToStorageConcept = new HashMap<>();

			TermValueSet termValueSet = fetchTermValueSet(url, version);

			statistics.setTarget(termValueSet.getResource().getIdDt());

			/*
			 * Concepts
			 */
			for (UrlUtil.CanonicalUrlParts system :
					flattenedValueSet.systemToCodes().keySet()) {

				Map<String, TermValueSetConcept> codeToExistingConcept =
						fetchExistingConcepts(system, flattenedValueSet, termValueSet);
				Set<Integer> existingOrders = codeToExistingConcept.values().stream()
						.map(TermValueSetConcept::getOrder)
						.collect(Collectors.toSet());

				Set<ValueSet.ValueSetExpansionContainsComponent> conceptsToAdd =
						flattenedValueSet.systemToConcepts().get(system);
				for (ValueSet.ValueSetExpansionContainsComponent conceptToAdd : conceptsToAdd) {
					storeConcept(
							conceptToAdd,
							termValueSet,
							order,
							existingOrders,
							statistics,
							codeToExistingConcept,
							codeToStorageConcept);
				}
			}
		});

		updateValueSetStatisticsWithPessimisticLock(url, version, statistics);

		ourLog.atInfo()
				.setMessage("Added to ValueSet URL[{}] Version[{}] in {}: {}")
				.addArgument(url)
				.addArgument(version)
				.addArgument(sw)
				.addArgument(statistics)
				.log();

		return statistics;
	}

	private void storeConcept(
			ValueSet.ValueSetExpansionContainsComponent theConceptToAdd,
			TermValueSet theTargetTermValueSet,
			IntCounter theOrderCounter,
			Set<Integer> theExistingOrderMap,
			UploadStatistics theStatisticsToPopulate,
			Map<String, TermValueSetConcept> theCodeToExistingConceptMap,
			Map<SystemAndCode, TermValueSetConcept> theCodeToStorageConceptMap) {
		boolean shouldSave = false;
		TermValueSetConcept storageConcept = theCodeToExistingConceptMap.get(theConceptToAdd.getCode());
		if (storageConcept == null) {
			storageConcept = new TermValueSetConcept();
			storageConcept.setCode(theConceptToAdd.getCode());
			storageConcept.setDisplay(theConceptToAdd.getDisplay());
			storageConcept.setSystem(theConceptToAdd.getSystem());
			storageConcept.setSystemVersion(theConceptToAdd.getVersion());
			storageConcept.setValueSet(theTargetTermValueSet);

			while (theExistingOrderMap.contains(theOrderCounter.get())) {
				theOrderCounter.increment();
			}
			storageConcept.setOrder(theOrderCounter.getAndIncrement());

			theStatisticsToPopulate.incrementConceptsAddedCount();

			ourLog.atTrace()
					.setMessage("Added Concept[{}] to ValueSet[{}] with order: {}")
					.addArgument(storageConcept.getCode())
					.addArgument(theTargetTermValueSet.getUrl())
					.addArgument(storageConcept.getOrder())
					.log();
			shouldSave = true;
		}

		if (isNotBlank(theConceptToAdd.getDisplay())
				&& !theConceptToAdd.getDisplay().equals(storageConcept.getDisplay())) {
			storageConcept.setDisplay(theConceptToAdd.getDisplay());
			shouldSave = true;
		}

		Extension sourceConceptPidExtension =
				theConceptToAdd.getExtensionByUrl(TerminologyConstants.EXTENSION_SOURCE_CONCEPT_PID);
		if (sourceConceptPidExtension != null) {
			Long sourceConceptPid = ((DecimalType) sourceConceptPidExtension.getValue())
					.getValue()
					.longValue();
			if (!sourceConceptPid.equals(storageConcept.getSourceConceptPid())) {
				storageConcept.setSourceConceptPid(sourceConceptPid);
				shouldSave = true;
			}
		}

		String directParentPids =
				theConceptToAdd.getExtensionString(TerminologyConstants.EXTENSION_SOURCE_CONCEPT_DIRECT_PARENT_PIDS);
		if (isNotBlank(directParentPids)
				&& !directParentPids.equals(storageConcept.getSourceConceptDirectParentPids())) {
			storageConcept.setSourceConceptDirectParentPids(directParentPids);
			shouldSave = true;
		}

		if (shouldSave) {
			myTermValueSetConceptDao.save(storageConcept);
		}

		SystemAndCode systemAndCode =
				new SystemAndCode(theConceptToAdd.getSystem(), theConceptToAdd.getVersion(), theConceptToAdd.getCode());
		theCodeToStorageConceptMap.put(systemAndCode, storageConcept);

		if (!theConceptToAdd.getDesignation().isEmpty()) {
			Set<LanguageAndDesignation> existingDesignations = storageConcept.getDesignations().stream()
					.map(t ->
							new LanguageAndDesignation(t.getLanguage(), t.getValue(), t.getUseSystem(), t.getUseCode()))
					.collect(Collectors.toSet());
			for (ValueSet.ConceptReferenceDesignationComponent designationToAdd : theConceptToAdd.getDesignation()) {
				if (!existingDesignations.contains(new LanguageAndDesignation(
						designationToAdd.getLanguage(),
						designationToAdd.getValue(),
						designationToAdd.getUse().getSystem(),
						designationToAdd.getUse().getCode()))) {

					TermValueSetConceptDesignation designation = new TermValueSetConceptDesignation();
					designation.setPartitionId(storageConcept.getPartitionId());
					designation.setConcept(storageConcept);
					designation.setValueSet(theTargetTermValueSet);
					designation.setLanguage(designationToAdd.getLanguage());
					designation.setValue(designationToAdd.getValue());
					designation.setUseSystem(designationToAdd.getUse().getSystem());
					designation.setUseCode(designationToAdd.getUse().getCode());
					designation.setUseDisplay(designationToAdd.getUse().getDisplay());

					theStatisticsToPopulate.incrementDesignationsAddedCount();
					myTermValueSetConceptDesignationDao.save(designation);
				}
			}
		}
	}

	@Nonnull
	@Override
	public UploadStatistics removeConceptsFromExpansion(ValueSet theDelta) {
		StopWatch sw = new StopWatch();
		UploadStatistics statistics = new UploadStatistics();

		String url = theDelta.getUrl();
		String version = theDelta.getVersion();

		myTxService.withSystemRequestOnDefaultPartition().execute(() -> {
			TermValueSet termValueSet = fetchTermValueSet(url, version);
			FlattenedValueSet flattenedValueSet = flattenValueSet(theDelta);
			statistics.setTarget(termValueSet.getResource().getIdDt());

			for (UrlUtil.CanonicalUrlParts system :
					flattenedValueSet.systemToCodes().keySet()) {

				Map<String, TermValueSetConcept> codeToExistingConcept =
						fetchExistingConcepts(system, flattenedValueSet, termValueSet);
				for (TermValueSetConcept next : codeToExistingConcept.values()) {
					deleteConceptAndChildren(next, statistics);
				}
			}
		});

		updateValueSetStatisticsWithPessimisticLock(url, version, statistics);

		ourLog.atInfo()
				.setMessage("Removed from ValueSet URL[{}] Version[{}] in {}: {}")
				.addArgument(url)
				.addArgument(version)
				.addArgument(sw)
				.addArgument(statistics)
				.log();

		return statistics;
	}

	@Override
	public void activateStagingVersion(String theValueSetUrl, String theStagingVersionId) {
		String versionToDelete = myTxService
				.withSystemRequestOnDefaultPartition()
				.execute(() -> {
					TermValueSet stagingValueSet = fetchTermValueSet(theValueSetUrl, theStagingVersionId);
					if (stagingValueSet.getIntendedVersionId() == null) {
						throw new InvalidRequestException(Msg.code(2988) + "ValueSet URL[" + theValueSetUrl
								+ "] Version[" + theStagingVersionId + "] is not a staging version");
					}

					String intendedVersion = stagingValueSet.getIntendedVersionId();
					if (intendedVersion.equals(INTENDED_VERSION_ID_NULL)) {
						intendedVersion = null;
					}

					TermValueSet currentValueSet = fetchTermValueSet(theValueSetUrl, intendedVersion);
					String temporaryVersion = currentValueSet.getVersion() + "_" + UUID.randomUUID();
					currentValueSet.setVersion(temporaryVersion);
					myTermValueSetDao.saveAndFlush(currentValueSet);

					stagingValueSet.setExpansionTimestamp(new Date());
					stagingValueSet.setExpansionStatus(TermValueSetPreExpansionStatusEnum.EXPANDED);
					stagingValueSet.setVersion(intendedVersion);
					myTermValueSetDao.saveAndFlush(stagingValueSet);

					return temporaryVersion;
				});

		myTxService.withSystemRequestOnDefaultPartition().execute(() -> {
			ourLog.atInfo()
					.setMessage("Deleting former version of ValueSet[url={}, version={}]")
					.addArgument(theValueSetUrl)
					.addArgument(versionToDelete)
					.log();

			TermValueSet valueSet = fetchTermValueSet(theValueSetUrl, versionToDelete);
			deleteTermValueSet(valueSet);
		});
	}

	@Override
	public void dropStagingVersion(String theUrl, String theVersion) {
		myTxService.withSystemRequestOnDefaultPartition().execute(() -> {
			TermValueSet valueSet = fetchTermValueSet(theUrl, theVersion);
			if (valueSet.getIntendedVersionId() == null) {
				throw new InvalidRequestException(Msg.code(2989) + "Cannot drop staging version of ValueSet[url="
						+ theUrl + ", version=" + theVersion + "] because it is not a staging version");
			}

			deleteTermValueSet(valueSet);
		});
	}

	@Nonnull
	private Map<String, TermValueSetConcept> fetchExistingConcepts(
			UrlUtil.CanonicalUrlParts system, FlattenedValueSet flattenedValueSet, TermValueSet termValueSet) {
		Collection<String> codes = flattenedValueSet.systemToCodes().get(system);

		return QueryChunker.chunk(codes.stream())
				.flatMap(t ->
						myTermValueSetConceptDao.findByCodesForTermValueSet(termValueSet, system.url(), t).stream())
				.collect(Collectors.toMap(TermValueSetConcept::getCode, Function.identity()));
	}

	private void updateValueSetStatisticsWithPessimisticLock(String url, String version, UploadStatistics statistics) {
		myTxService.withSystemRequestOnDefaultPartition().execute(() -> {
			TermValueSet termValueSet = fetchTermValueSet(url, version);
			myEntityManager.lock(termValueSet, LockModeType.PESSIMISTIC_WRITE);

			termValueSet.setTotalConcepts(termValueSet.getTotalConcepts() + statistics.getAddedConceptCount());
			termValueSet.setTotalConcepts(termValueSet.getTotalConcepts() - statistics.getRemovedConceptCount());
			termValueSet.setTotalConceptDesignations(
					termValueSet.getTotalConceptDesignations() + statistics.getAddedDesignationCount());
			termValueSet.setTotalConceptDesignations(
					termValueSet.getTotalConceptDesignations() - statistics.getRemovedDesignationCount());
			myTermValueSetDao.save(termValueSet);
		});
	}

	private void deleteConceptAndChildren(TermValueSetConcept theConceptToDelete, UploadStatistics theStatistics) {
		for (TermValueSetConceptDesignation designation : theConceptToDelete.getDesignations()) {
			theStatistics.incrementDesignationsRemovedCount();
			myTermValueSetConceptDesignationDao.delete(designation);
		}

		myTermValueSetConceptDao.delete(theConceptToDelete);
		theStatistics.incrementConceptsRemovedCount();
	}

	private FlattenedValueSet flattenValueSet(@Nonnull ValueSet theValueSet) {
		SetMultimap<UrlUtil.CanonicalUrlParts, String> systemToCodes =
				MultimapBuilder.linkedHashKeys().linkedHashSetValues().build();
		SetMultimap<UrlUtil.CanonicalUrlParts, ValueSet.ValueSetExpansionContainsComponent> systemToConcepts =
				MultimapBuilder.linkedHashKeys().linkedHashSetValues().build();
		SetMultimap<SystemAndCode, SystemAndCode> childCodeToParentCodes =
				MultimapBuilder.linkedHashKeys().linkedHashSetValues().build();

		List<ValueSet.ValueSetExpansionContainsComponent> containsList =
				theValueSet.getExpansion().getContains();
		flattenValueSetInto(containsList, null, systemToCodes, systemToConcepts, childCodeToParentCodes);

		return new FlattenedValueSet(systemToCodes, systemToConcepts, childCodeToParentCodes);
	}

	@Nonnull
	private TermValueSet fetchTermValueSet(String theUrl, @Nullable String theVersion) {
		Optional<TermValueSet> valueSetOpt = fetchTermValueSetOpt(theUrl, theVersion);

		if (valueSetOpt.isEmpty()) {
			throw new ResourceNotFoundException(Msg.code(2990) + "No ValueSet found with URL[" + theUrl
					+ "] and Version[" + getIfNull(theVersion, "(none)") + "]");
		}

		return valueSetOpt.get();
	}

	private Optional<TermValueSet> fetchTermValueSetOpt(String theUrl, @Nullable String theVersion) {
		HapiTransactionService.requireTransaction();

		Optional<TermValueSet> valueSetOpt;
		if (isBlank(theVersion)) {
			valueSetOpt = myTermValueSetDao.findTermValueSetByUrlAndNullVersion(theUrl);
		} else {
			valueSetOpt = myTermValueSetDao.findTermValueSetByUrlAndVersion(theUrl, theVersion);
		}
		return valueSetOpt;
	}

	private static void flattenValueSetInto(
			List<ValueSet.ValueSetExpansionContainsComponent> theSourceConcepts,
			SystemAndCode theSourceParentConcept,
			SetMultimap<UrlUtil.CanonicalUrlParts, String> theTargetSystemToCodes,
			SetMultimap<UrlUtil.CanonicalUrlParts, ValueSet.ValueSetExpansionContainsComponent>
					theTargetSystemToConcepts,
			SetMultimap<SystemAndCode, SystemAndCode> theTargetChildCodeToParentCodes) {
		for (ValueSet.ValueSetExpansionContainsComponent contains : theSourceConcepts) {
			String code = contains.getCode();
			if (isNotBlank(code)) {
				String systemUrl = contains.getSystem();
				if (isBlank(systemUrl)) {
					throw new InvalidRequestException(Msg.code(2991) + "ValueSet contains a code with no system: "
							+ UrlUtil.sanitizeUrlPart(code));
				}

				String systemVersion = contains.getVersion();
				UrlUtil.CanonicalUrlParts system = UrlUtil.parseCanonicalUrl(systemUrl, systemVersion);
				theTargetSystemToCodes.put(system, code);
				theTargetSystemToConcepts.put(system, contains);

				SystemAndCode systemAndCode = new SystemAndCode(system, code);
				if (theSourceParentConcept != null) {
					theTargetChildCodeToParentCodes.put(systemAndCode, theSourceParentConcept);
				}

				// Recurse
				flattenValueSetInto(
						contains.getContains(),
						systemAndCode,
						theTargetSystemToCodes,
						theTargetSystemToConcepts,
						theTargetChildCodeToParentCodes);
			}
		}
	}

	@Override
	public void deleteValueSetAndChildren(ResourceTable theResourceTable) {
		myTxService.withSystemRequestOnDefaultPartition().execute(() -> {
			deleteValueSetForResource(theResourceTable);
			myTermReadSvc.invalidateValueSetCaches();
		});
	}

	@Override
	public void storeTermValueSet(
			RequestDetails theRequestDetails,
			ResourceTable theResourceTable,
			org.hl7.fhir.r4.model.ValueSet theValueSet) {
		HapiTransactionService.requireTransaction();

		// If we're in a transaction, we need to flush now so that we can correctly detect
		// duplicates if there are multiple ValueSets in the same TX with the same URL
		// (which is an error, but we need to catch it). It'd be better to catch this by
		// inspecting the URLs in the bundle or something, since flushing hurts performance
		// but it's not expected that loading valuesets is going to be a huge high frequency
		// thing so it probably doesn't matter
		myEntityManager.flush();

		ValidateUtil.isTrueOrThrowInvalidRequest(theResourceTable != null, "No resource supplied");
		if (isPlaceholder(theValueSet)) {
			ourLog.info(
					"Not storing TermValueSet for placeholder {}",
					theValueSet.getIdElement().toVersionless().getValueAsString());
			return;
		}

		ValidateUtil.isNotBlankOrThrowUnprocessableEntity(
				theValueSet.getUrl(), "ValueSet has no value for ValueSet.url");
		ourLog.info(
				"Storing TermValueSet for {}",
				theValueSet.getIdElement().toVersionless().getValueAsString());

		/*
		 * Get CodeSystem and validate CodeSystemVersion
		 */
		TermValueSet termValueSet = new TermValueSet();
		termValueSet.setResource(theResourceTable);
		termValueSet.setUrl(theValueSet.getUrl());
		termValueSet.setVersion(theValueSet.getVersion());
		termValueSet.setName(theValueSet.hasName() ? theValueSet.getName() : null);

		if (theValueSet.getStatus() != null && theValueSet.getStatus() != Enumerations.PublicationStatus.ACTIVE) {
			termValueSet.setExpansionStatus(TermValueSetPreExpansionStatusEnum.NOT_ACTIVE);
		} else if (myStorageSettings.isPreExpandValueSets() && myStorageSettings.isEnableTaskPreExpandValueSets()) {
			/*
			 * If we're saving an active ValueSet, automatically start a batch job
			 * to precalculate the expansion. We register this after the transaction
			 * commits so that we don't create a job for a failed ValueSet.
			 */
			TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
				@Override
				public void afterCommit() {
					JobInstanceStartRequest startRequest = new JobInstanceStartRequest();
					startRequest.setJobDefinitionId(PreExpandValueSetJobAppCtx.JOB_ID_PRE_EXPAND_VALUESET);

					PreExpandValueSetParameters parameters = new PreExpandValueSetParameters();
					parameters.setUrl(theValueSet.getUrl());
					parameters.setVersion(theValueSet.getVersion());
					startRequest.setParameters(parameters);

					ourLog.info(
							"Submitting pre-expansion job for ValueSet[url={}, version={}]",
							parameters.getUrl(),
							parameters.getVersion());
					myJobCoordinator.startInstance(theRequestDetails, startRequest);
				}
			});
		}

		// Delete version being replaced
		List<TermValueSet> deletedTrmValueSets = deleteValueSetForResource(theResourceTable);

		/*
		 * Do the upload.
		 */
		String url = termValueSet.getUrl();
		String version = termValueSet.getVersion();
		Optional<TermValueSet> optionalExistingTermValueSetByUrl;

		if (deletedTrmValueSets.stream()
				.anyMatch(t -> Objects.equals(t.getUrl(), url) && Objects.equals(t.getVersion(), version))) {
			// If we just deleted the valueset marker, we don't need to check if it exists
			// in the database
			optionalExistingTermValueSetByUrl = Optional.empty();
		} else {
			optionalExistingTermValueSetByUrl = fetchTermValueSetOpt(url, version);
		}

		if (optionalExistingTermValueSetByUrl.isEmpty()) {

			myEntityManager.persist(termValueSet);

		} else {
			TermValueSet existingTermValueSet = optionalExistingTermValueSetByUrl.get();
			String msg;
			if (version != null) {
				msg = myContext
						.getLocalizer()
						.getMessage(
								TermReadSvcImpl.class,
								"cannotCreateDuplicateValueSetUrlAndVersion",
								url,
								version,
								existingTermValueSet
										.getResource()
										.getIdDt()
										.toUnqualifiedVersionless()
										.getValue());
			} else {
				msg = myContext
						.getLocalizer()
						.getMessage(
								TermReadSvcImpl.class,
								"cannotCreateDuplicateValueSetUrl",
								url,
								existingTermValueSet
										.getResource()
										.getIdDt()
										.toUnqualifiedVersionless()
										.getValue());
			}
			throw new UnprocessableEntityException(Msg.code(902) + msg);
		}
		myTermReadSvc.invalidateValueSetCaches();
	}

	private List<TermValueSet> deleteValueSetForResource(ResourceTable theResourceTable) {
		HapiTransactionService.requireTransaction();

		// Get existing entity so it can be deleted.
		List<TermValueSet> termValueSets = myTermValueSetDao.findByResourcePid(theResourceTable.getId());

		termValueSets.forEach(this::deleteTermValueSet);

		return termValueSets;
	}

	private void deleteTermValueSet(TermValueSet theTermValueSet) {
		HapiTransactionService.requireTransaction();

		ourLog.info("Deleting existing TermValueSet[{}] and its children...", theTermValueSet.getId());
		deletePreCalculatedValueSetContents(theTermValueSet);
		myTermValueSetDao.deleteById(theTermValueSet.getPartitionedId());

		/*
		 * If we're updating an existing ValueSet within a transaction, we need to make
		 * sure to manually flush now since otherwise we'll try to create a new
		 * TermValueSet entity and fail with a constraint error on the URL, since
		 * this one won't be deleted yet
		 */
		myTermValueSetDao.flush();

		ourLog.info("Done deleting existing TermValueSet[{}] and its children.", theTermValueSet.getId());
	}

	// Generated by claude-sonnet-4-6
	@Override
	public int invalidatePreCalculatedExpansionOfValueSetsContainingCodeSystem(String theCodeSystemUrl) {
		return myTxService.withSystemRequestOnDefaultPartition().execute(() -> {
			List<TermValueSet> affectedValueSets = myTermValueSetDao.findExpandedByCodeSystemUrl(
					theCodeSystemUrl,
					List.of(
							TermValueSetPreExpansionStatusEnum.EXPANDED,
							TermValueSetPreExpansionStatusEnum.EXPANSION_IN_PROGRESS));

			if (affectedValueSets.isEmpty()) {
				return 0;
			}

			for (TermValueSet termValueSet : affectedValueSets) {
				ourLog.info(
						"Invalidating pre-calculated expansion of ValueSet {} due to update of CodeSystem {}",
						termValueSet.getUrl(),
						theCodeSystemUrl);
				deletePreCalculatedValueSetContents(termValueSet);
				termValueSet.setExpansionStatus(TermValueSetPreExpansionStatusEnum.NOT_EXPANDED);
				termValueSet.setExpansionTimestamp(null);
				myEntityManager.merge(termValueSet);
			}

			afterValueSetExpansionStatusChange();

			return affectedValueSets.size();
		});
	}

	@Override
	public void markValueSetAsFailedToExpand(String theUrl, String theVersion) {
		myTxService.withSystemRequestOnDefaultPartition().execute(() -> fetchTermValueSetOpt(theUrl, theVersion)
				.ifPresent(theValueSet -> {
					theValueSet.setExpansionStatus(TermValueSetPreExpansionStatusEnum.FAILED_TO_EXPAND);
					myEntityManager.merge(theValueSet);
				}));
	}

	/*
	 * If a ValueSet has just finished pre-expanding, let's flush the caches. This is
	 * kind of a blunt tool, but it should ensure that users don't get unpredictable
	 * results while they test changes, which is probably a worthwhile sacrifice
	 */
	private void afterValueSetExpansionStatusChange() {
		provideValidationSupport().invalidateCaches();
	}

	private void deletePreCalculatedValueSetContents(TermValueSet theValueSet) {
		myTermValueSetConceptDesignationDao.deleteByTermValueSetId(theValueSet);
		myTermValueSetConceptDao.deleteByTermValueSetId(theValueSet);
	}

	@Nonnull
	protected IValidationSupport provideValidationSupport() {
		IValidationSupport validationSupport = myValidationSupport;
		if (validationSupport == null) {
			validationSupport = myApplicationContext.getBean(IValidationSupport.class);
			myValidationSupport = validationSupport;
		}
		return validationSupport;
	}

	private record LanguageAndDesignation(String language, String designation, String useSystem, String useCode) {}

	private record FlattenedValueSet(
			SetMultimap<UrlUtil.CanonicalUrlParts, String> systemToCodes,
			SetMultimap<UrlUtil.CanonicalUrlParts, ValueSet.ValueSetExpansionContainsComponent> systemToConcepts,
			SetMultimap<SystemAndCode, SystemAndCode> childCodeToParentCodes) {}
}
