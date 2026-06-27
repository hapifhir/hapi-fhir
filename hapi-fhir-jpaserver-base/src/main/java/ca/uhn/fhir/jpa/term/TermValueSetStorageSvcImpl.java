package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyConstants;
import ca.uhn.fhir.jpa.batch2.jobs.term.valueset.preexpand.PreExpandValueSetJobAppCtx;
import ca.uhn.fhir.jpa.batch2.jobs.term.valueset.preexpand.PreExpandValueSetParameters;
import ca.uhn.fhir.jpa.dao.data.ITermValueSetConceptDao;
import ca.uhn.fhir.jpa.dao.data.ITermValueSetConceptDesignationDao;
import ca.uhn.fhir.jpa.dao.data.ITermValueSetConceptParentChildLinkDao;
import ca.uhn.fhir.jpa.dao.data.ITermValueSetDao;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.entity.TermValueSet;
import ca.uhn.fhir.jpa.entity.TermValueSetConcept;
import ca.uhn.fhir.jpa.entity.TermValueSetConceptDesignation;
import ca.uhn.fhir.jpa.entity.TermValueSetConceptParentChildLink;
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
import ca.uhn.hapi.converters.canonical.VersionCanonicalizer;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.SetMultimap;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
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
	private ITermValueSetConceptParentChildLinkDao myTermValueSetConceptParentChildLinkDao;

	@Autowired
	private IHapiTransactionService myTxService;

	@Autowired
	private DaoRegistry myDaoRegistry;

	@Autowired
	private VersionCanonicalizer myVersionCanonicalizer;

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

	@Override
	public UploadStatistics addConceptsToExpansion(@Nonnull ValueSet theDelta, int theStartingOrder) {
		StopWatch sw = new StopWatch();
		UploadStatistics statistics = new UploadStatistics();
		String url = theDelta.getUrl();
		String version = theDelta.getVersion();

		myTxService.withSystemRequestOnDefaultPartition().execute(() -> {
			// FIXME: split the stuff below into methods
			FlattenedValueSet flattenedValueSet = flattenValueSet(theDelta);
			IntCounter order = new IntCounter(theStartingOrder);
			Map<SystemAndCode, TermValueSetConcept> codeToStorageConcept = new HashMap<>();

			TermValueSet termValueSet = fetchTermValueSet(url, version);

			statistics.setTarget(termValueSet.getResource().getIdDt());

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

					boolean shouldSave = false;
					TermValueSetConcept storageConcept = codeToExistingConcept.get(conceptToAdd.getCode());
					if (storageConcept == null) {
						storageConcept = new TermValueSetConcept();
						storageConcept.setCode(conceptToAdd.getCode());
						storageConcept.setDisplay(conceptToAdd.getDisplay());
						storageConcept.setSystem(conceptToAdd.getSystem());
						storageConcept.setSystemVersion(conceptToAdd.getVersion());
						storageConcept.setValueSet(termValueSet);

						while (existingOrders.contains(order.get())) {
							order.increment();
						}
						storageConcept.setOrder(order.getAndIncrement());

						statistics.incrementConceptsAddedCount();

						ourLog.atInfo() // FIXME: make debug
								.setMessage("Added Concept[{}] to ValueSet[{}] with order: {}")
								.addArgument(storageConcept.getCode())
								.addArgument(termValueSet.getUrl())
								.addArgument(storageConcept.getOrder())
								.log();
						shouldSave = true;
					}

					if (isNotBlank(conceptToAdd.getDisplay())
							&& !conceptToAdd.getDisplay().equals(storageConcept.getDisplay())) {
						storageConcept.setDisplay(conceptToAdd.getDisplay());
						shouldSave = true;
					}

					Extension sourceConceptPidExtension =
							conceptToAdd.getExtensionByUrl(TerminologyConstants.EXTENSION_SOURCE_CONCEPT_PID);
					if (sourceConceptPidExtension != null) {
						Long sourceConceptPid = ((DecimalType) sourceConceptPidExtension.getValue())
								.getValue()
								.longValue();
						if (!sourceConceptPid.equals(storageConcept.getSourceConceptPid())) {
							storageConcept.setSourceConceptPid(sourceConceptPid);
							shouldSave = true;
						}
					}

					String directParentPids = conceptToAdd.getExtensionString(
							TerminologyConstants.EXTENSION_SOURCE_CONCEPT_DIRECT_PARENT_PIDS);
					if (isNotBlank(directParentPids)
							&& !directParentPids.equals(storageConcept.getSourceConceptDirectParentPids())) {
						storageConcept.setSourceConceptDirectParentPids(directParentPids);
						shouldSave = true;
					}

					if (shouldSave) {
						myTermValueSetConceptDao.save(storageConcept);
					}

					SystemAndCode systemAndCode = new SystemAndCode(
							conceptToAdd.getSystem(), conceptToAdd.getVersion(), conceptToAdd.getCode());
					codeToStorageConcept.put(systemAndCode, storageConcept);

					if (!conceptToAdd.getDesignation().isEmpty()) {
						Set<LanguageAndDesignation> existingDesignations = storageConcept.getDesignations().stream()
								.map(t -> new LanguageAndDesignation(
										t.getLanguage(), t.getValue(), t.getUseSystem(), t.getUseCode()))
								.collect(Collectors.toSet());
						for (ValueSet.ConceptReferenceDesignationComponent designationToAdd :
								conceptToAdd.getDesignation()) {
							if (!existingDesignations.contains(new LanguageAndDesignation(
									designationToAdd.getLanguage(),
									designationToAdd.getValue(),
									designationToAdd.getUse().getSystem(),
									designationToAdd.getUse().getCode()))) {

								TermValueSetConceptDesignation designation = new TermValueSetConceptDesignation();
								designation.setPartitionId(storageConcept.getPartitionId());
								designation.setConcept(storageConcept);
								designation.setValueSet(termValueSet);
								designation.setLanguage(designationToAdd.getLanguage());
								designation.setValue(designationToAdd.getValue());
								designation.setUseSystem(
										designationToAdd.getUse().getSystem());
								designation.setUseCode(designationToAdd.getUse().getCode());
								designation.setUseDisplay(
										designationToAdd.getUse().getDisplay());

								statistics.incrementDesignationsAddedCount();
								myTermValueSetConceptDesignationDao.save(designation);
							}
						}
					}
				}
			}

			for (Map.Entry<SystemAndCode, SystemAndCode> entry :
					flattenedValueSet.childCodeToParentCodes().entries()) {
				SystemAndCode childSystemAndCode = entry.getKey();
				SystemAndCode parentSystemAndCode = entry.getValue();
				TermValueSetConcept childConcept = codeToStorageConcept.get(childSystemAndCode);
				TermValueSetConcept parentConcept = codeToStorageConcept.get(parentSystemAndCode);

				// Sanity check - These should never fail since we always create the parents
				// in the block above, and you can't pass a child into this method without
				// also passing in its parent
				Validate.notNull(childConcept, "Failed to find concept: %s", childSystemAndCode);
				Validate.notNull(parentConcept, "Failed to find concept: %s", parentSystemAndCode);

				boolean shouldAdd = childConcept.getParentConcepts().stream()
						.map(c -> new SystemAndCode(c.getSystem(), c.getSystemVersion(), c.getCode()))
						.noneMatch(t -> t.equals(parentSystemAndCode));
				if (shouldAdd) {

					TermValueSetConceptParentChildLink.TermValueSetConceptParentChildLinkPk pk =
							new TermValueSetConceptParentChildLink.TermValueSetConceptParentChildLinkPk();
					pk.setPartitionId(termValueSet.getPartitionedId().getPartitionIdValue());
					pk.setParentPid(parentConcept.getId());
					pk.setChildPid(childConcept.getId());

					TermValueSetConceptParentChildLink linkToAdd = new TermValueSetConceptParentChildLink();
					linkToAdd.setId(pk);
					linkToAdd.setValueSet(termValueSet);
					myTermValueSetConceptParentChildLinkDao.save(linkToAdd);

					parentConcept.getChildren().add(linkToAdd);
					childConcept.getParents().add(linkToAdd);

					statistics.incrementConceptLinksAddedCount();
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
		StopWatch sw = new StopWatch();

		String versionToDelete = myTxService
				.withSystemRequestOnDefaultPartition()
				.execute(() -> {
					TermValueSet stagingValueSet = fetchTermValueSet(theValueSetUrl, theStagingVersionId);
					if (stagingValueSet.getIntendedVersionId() == null) {
						// FIXME: add code
						throw new InvalidRequestException(Msg.code(1) + "ValueSet URL[" + theValueSetUrl + "] Version["
								+ theStagingVersionId + "] is not a staging version");
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
				// FIXME: add test and code
				throw new InvalidRequestException(Msg.code(1) + "Cannot drop staging version of ValueSet[url=" + theUrl
						+ ", version=" + theVersion + "] because it is not a staging version");
			}

			deleteTermValueSet(valueSet);
		});
	}

	@Nonnull
	private Map<String, TermValueSetConcept> fetchExistingConcepts(
			UrlUtil.CanonicalUrlParts system, FlattenedValueSet flattenedValueSet, TermValueSet termValueSet) {
		Collection<String> codes = flattenedValueSet.systemToCodes().get(system);

		Map<String, TermValueSetConcept> codeToExistingConcept = QueryChunker.chunk(codes.stream())
				.flatMap(t ->
						myTermValueSetConceptDao.findByCodesForTermValueSet(termValueSet, system.url(), t).stream())
				.collect(Collectors.toMap(TermValueSetConcept::getCode, Function.identity()));
		return codeToExistingConcept;
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
		for (TermValueSetConceptParentChildLink parent : theConceptToDelete.getParents()) {
			theStatistics.incrementConceptLinksRemovedCount();
			myTermValueSetConceptParentChildLinkDao.delete(parent);
			parent.getParent().getChildren().remove(parent);
		}
		for (TermValueSetConceptParentChildLink child : theConceptToDelete.getChildren()) {
			theStatistics.incrementConceptLinksRemovedCount();
			myTermValueSetConceptParentChildLinkDao.delete(child);
			child.getChild().getParents().remove(child);

			// Recurse
			deleteConceptAndChildren(child.getChild(), theStatistics);
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
			// FIXME: add code and test
			throw new ResourceNotFoundException(Msg.code(1) + "No ValueSet found with URL[" + theUrl + "] and Version["
					+ getIfNull(theVersion, "(none)") + "]");
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
					// FIXME: add code
					throw new InvalidRequestException(
							Msg.code(1) + "ValueSet contains a code with no system: " + UrlUtil.sanitizeUrlPart(code));
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
		} else {
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
		Optional<TermValueSet> deletedTrmValueSet = deleteValueSetForResource(theResourceTable);

		/*
		 * Do the upload.
		 */
		String url = termValueSet.getUrl();
		String version = termValueSet.getVersion();
		Optional<TermValueSet> optionalExistingTermValueSetByUrl;

		if (deletedTrmValueSet.isPresent()
				&& Objects.equals(deletedTrmValueSet.get().getUrl(), url)
				&& Objects.equals(deletedTrmValueSet.get().getVersion(), version)) {
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

	private Optional<TermValueSet> deleteValueSetForResource(ResourceTable theResourceTable) {
		HapiTransactionService.requireTransaction();

		// Get existing entity so it can be deleted.
		Optional<TermValueSet> optionalExistingTermValueSetById =
				myTermValueSetDao.findByResourcePid(theResourceTable.getId());

		if (optionalExistingTermValueSetById.isPresent()) {
			TermValueSet existingTermValueSet = optionalExistingTermValueSetById.get();

			deleteTermValueSet(existingTermValueSet);
		}

		return optionalExistingTermValueSetById;
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

	@Override
	public String invalidatePreCalculatedExpansion(IIdType theValueSetId, RequestDetails theRequestDetails) {
		return myTxService.withSystemRequestOnDefaultPartition().execute(() -> {
			IBaseResource valueSet = myDaoRegistry.getResourceDao("ValueSet").read(theValueSetId, theRequestDetails);
			org.hl7.fhir.r4.model.ValueSet canonicalValueSet = myVersionCanonicalizer.valueSetToCanonical(valueSet);
			Optional<TermValueSet> optionalTermValueSet =
					fetchTermValueSetOpt(canonicalValueSet.getUrl(), canonicalValueSet.getVersion());
			if (optionalTermValueSet.isEmpty()) {
				return myContext
						.getLocalizer()
						.getMessage(TermReadSvcImpl.class, "valueSetNotFoundInTerminologyDatabase", theValueSetId);
			}

			ourLog.info(
					"Invalidating pre-calculated expansion on ValueSet {} / {}",
					theValueSetId,
					canonicalValueSet.getUrl());

			TermValueSet termValueSet = optionalTermValueSet.get();
			if (termValueSet.getExpansionStatus() == TermValueSetPreExpansionStatusEnum.NOT_EXPANDED) {
				return myContext
						.getLocalizer()
						.getMessage(
								TermReadSvcImpl.class,
								"valueSetCantInvalidateNotYetPrecalculated",
								termValueSet.getUrl(),
								termValueSet.getExpansionStatus());
			}

			Long totalConcepts = termValueSet.getTotalConcepts();

			deletePreCalculatedValueSetContents(termValueSet);

			termValueSet.setExpansionStatus(TermValueSetPreExpansionStatusEnum.NOT_EXPANDED);
			termValueSet.setExpansionTimestamp(null);

			assert termValueSet.getId() != null;
			myEntityManager.merge(termValueSet);

			afterValueSetExpansionStatusChange();

			return myContext
					.getLocalizer()
					.getMessage(
							TermReadSvcImpl.class,
							"valueSetPreExpansionInvalidated",
							termValueSet.getUrl(),
							totalConcepts);
		});
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
		myTermValueSetConceptParentChildLinkDao.deleteByTermValueSetId(theValueSet);
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
