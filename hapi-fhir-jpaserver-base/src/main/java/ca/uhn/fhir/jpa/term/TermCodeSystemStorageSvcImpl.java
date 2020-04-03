package ca.uhn.fhir.jpa.term;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.jpa.dao.BaseHapiFhirDao;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.dao.IHapiJpaRepository;
import ca.uhn.fhir.jpa.dao.data.IResourceTableDao;
import ca.uhn.fhir.jpa.dao.data.ITermCodeSystemDao;
import ca.uhn.fhir.jpa.dao.data.ITermCodeSystemVersionDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptDesignationDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptParentChildLinkDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptPropertyDao;
import ca.uhn.fhir.jpa.dao.index.IdHelperService;
import ca.uhn.fhir.jpa.entity.TermCodeSystem;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.entity.TermConceptDesignation;
import ca.uhn.fhir.jpa.entity.TermConceptParentChildLink;
import ca.uhn.fhir.jpa.entity.TermConceptProperty;
import ca.uhn.fhir.jpa.model.cross.ResourcePersistentId;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc;
import ca.uhn.fhir.jpa.term.api.ITermDeferredStorageSvc;
import ca.uhn.fhir.jpa.term.api.ITermReadSvc;
import ca.uhn.fhir.jpa.term.api.ITermVersionAdapterSvc;
import ca.uhn.fhir.jpa.term.custom.CustomTerminologySet;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.util.ObjectUtil;
import ca.uhn.fhir.util.StopWatch;
import ca.uhn.fhir.util.ValidateUtil;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.ValueSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Nonnull;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class TermCodeSystemStorageSvcImpl implements ITermCodeSystemStorageSvc {
	private static final Logger ourLog = LoggerFactory.getLogger(TermCodeSystemStorageSvcImpl.class);
	private static final Object PLACEHOLDER_OBJECT = new Object();
	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	protected EntityManager myEntityManager;
	@Autowired
	protected ITermCodeSystemDao myCodeSystemDao;
	@Autowired
	protected ITermCodeSystemVersionDao myCodeSystemVersionDao;
	@Autowired
	protected ITermConceptDao myConceptDao;
	@Autowired
	protected ITermConceptPropertyDao myConceptPropertyDao;
	@Autowired
	protected ITermConceptDesignationDao myConceptDesignationDao;
	@Autowired
	protected IdHelperService myIdHelperService;
	@Autowired
	private PlatformTransactionManager myTransactionManager;
	@Autowired
	private ITermConceptParentChildLinkDao myConceptParentChildLinkDao;
	@Autowired
	private ITermVersionAdapterSvc myTerminologyVersionAdapterSvc;
	@Autowired
	private ITermDeferredStorageSvc myDeferredStorageSvc;
	@Autowired
	private FhirContext myContext;
	@Autowired
	private ITermReadSvc myTerminologySvc;
	@Autowired
	private DaoConfig myDaoConfig;
	@Autowired
	private IResourceTableDao myResourceTableDao;

	@Override
	public ResourcePersistentId getValueSetResourcePid(IIdType theIdType) {
		return myIdHelperService.resolveResourcePersistentIds(theIdType.getResourceType(), theIdType.getIdPart());
	}

	@Transactional
	@Override
	public UploadStatistics applyDeltaCodeSystemsAdd(String theSystem, CustomTerminologySet theAdditions) {
		ValidateUtil.isNotBlankOrThrowInvalidRequest(theSystem, "No system provided");
		validateDstu3OrNewer();
		theAdditions.validateNoCycleOrThrowInvalidRequest();

		TermCodeSystem cs = myCodeSystemDao.findByCodeSystemUri(theSystem);
		if (cs == null) {
			CodeSystem codeSystemResource = new CodeSystem();
			codeSystemResource.setUrl(theSystem);
			codeSystemResource.setContent(CodeSystem.CodeSystemContentMode.NOTPRESENT);
			myTerminologyVersionAdapterSvc.createOrUpdateCodeSystem(codeSystemResource);

			cs = myCodeSystemDao.findByCodeSystemUri(theSystem);
		}

		TermCodeSystemVersion csv = cs.getCurrentVersion();
		Validate.notNull(csv);

		CodeSystem codeSystem = myTerminologySvc.fetchCanonicalCodeSystemFromCompleteContext(theSystem);
		if (codeSystem.getContent() != CodeSystem.CodeSystemContentMode.NOTPRESENT) {
			throw new InvalidRequestException("CodeSystem with url[" + Constants.codeSystemWithDefaultDescription(theSystem) + "] can not apply a delta - wrong content mode: " + codeSystem.getContent());
		}

		Validate.notNull(cs);
		Validate.notNull(cs.getPid());

		IIdType codeSystemId = cs.getResource().getIdDt();

		UploadStatistics retVal = new UploadStatistics(codeSystemId);
		HashMap<String, TermConcept> codeToConcept = new HashMap<>();

		// Add root concepts
		for (TermConcept nextRootConcept : theAdditions.getRootConcepts()) {
			List<String> parentCodes = Collections.emptyList();
			addConceptInHierarchy(csv, parentCodes, nextRootConcept, retVal, codeToConcept, 0);
		}

		return retVal;
	}

	@Transactional
	@Override
	public UploadStatistics applyDeltaCodeSystemsRemove(String theSystem, CustomTerminologySet theValue) {
		ValidateUtil.isNotBlankOrThrowInvalidRequest(theSystem, "No system provided");
		validateDstu3OrNewer();

		TermCodeSystem cs = myCodeSystemDao.findByCodeSystemUri(theSystem);
		if (cs == null) {
			throw new InvalidRequestException("Unknown code system: " + theSystem);
		}

		AtomicInteger removeCounter = new AtomicInteger(0);

		for (TermConcept nextSuppliedConcept : theValue.getRootConcepts()) {
			Optional<TermConcept> conceptOpt = myTerminologySvc.findCode(theSystem, nextSuppliedConcept.getCode());
			if (conceptOpt.isPresent()) {
				TermConcept concept = conceptOpt.get();
				deleteConceptChildrenAndConcept(concept, removeCounter);
			}
		}

		IIdType target = cs.getResource().getIdDt();
		return new UploadStatistics(removeCounter.get(), target);
	}

	@Override
	public void deleteCodeSystem(TermCodeSystem theCodeSystem) {
		ourLog.info(" * Deleting code system {}", theCodeSystem.getPid());

		myEntityManager.flush();
		TermCodeSystem cs = myCodeSystemDao.findById(theCodeSystem.getPid()).orElseThrow(IllegalStateException::new);
		cs.setCurrentVersion(null);
		myCodeSystemDao.save(cs);
		myCodeSystemDao.flush();

		List<TermCodeSystemVersion> codeSystemVersions = myCodeSystemVersionDao.findByCodeSystemPid(theCodeSystem.getPid());
		for (TermCodeSystemVersion next : codeSystemVersions) {
			deleteCodeSystemVersion(next.getPid());
		}
		myCodeSystemVersionDao.deleteForCodeSystem(theCodeSystem);
		myCodeSystemDao.delete(theCodeSystem);

		myEntityManager.flush();
	}

	/**
	 * Returns the number of saved concepts
	 */
	@Override
	public int saveConcept(TermConcept theConcept) {
		int retVal = 0;

		/*
		 * If the concept has an ID, we're reindexing, so there's no need to
		 * save parent concepts first (it's way too slow to do that)
		 */
		if (theConcept.getId() == null) {
			boolean needToSaveParents = false;
			for (TermConceptParentChildLink next : theConcept.getParents()) {
				if (next.getParent().getId() == null) {
					needToSaveParents = true;
				}
			}
			if (needToSaveParents) {
				retVal += ensureParentsSaved(theConcept.getParents());
			}
		}

		if (theConcept.getId() == null || theConcept.getIndexStatus() == null) {
			retVal++;
			theConcept.setIndexStatus(BaseHapiFhirDao.INDEX_STATUS_INDEXED);
			theConcept.setUpdated(new Date());
			myConceptDao.save(theConcept);

			for (TermConceptProperty next : theConcept.getProperties()) {
				myConceptPropertyDao.save(next);
			}

			for (TermConceptDesignation next : theConcept.getDesignations()) {
				myConceptDesignationDao.save(next);
			}
		}

		ourLog.trace("Saved {} and got PID {}", theConcept.getCode(), theConcept.getId());
		return retVal;
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public void storeNewCodeSystemVersionIfNeeded(CodeSystem theCodeSystem, ResourceTable theResourceEntity) {
		if (theCodeSystem != null && isNotBlank(theCodeSystem.getUrl())) {
			String codeSystemUrl = theCodeSystem.getUrl();
			if (theCodeSystem.getContent() == CodeSystem.CodeSystemContentMode.COMPLETE || theCodeSystem.getContent() == null || theCodeSystem.getContent() == CodeSystem.CodeSystemContentMode.NOTPRESENT) {
				ourLog.info("CodeSystem {} has a status of {}, going to store concepts in terminology tables", theResourceEntity.getIdDt().getValue(), theCodeSystem.getContentElement().getValueAsString());

				ResourcePersistentId codeSystemResourcePid = getCodeSystemResourcePid(theCodeSystem.getIdElement());

				/*
				 * If this is a not-present codesystem, we don't want to store a new version if one
				 * already exists, since that will wipe out the existing concepts. We do create or update
				 * the TermCodeSystem table though, since that allows the DB to reject changes
				 * that would result in duplicate CodeSysten.url values.
				 */
				if (theCodeSystem.getContent() == CodeSystem.CodeSystemContentMode.NOTPRESENT) {
					TermCodeSystem codeSystem = myCodeSystemDao.findByCodeSystemUri(theCodeSystem.getUrl());
					if (codeSystem != null) {
						getOrCreateTermCodeSystem(codeSystemResourcePid, theCodeSystem.getUrl(), theCodeSystem.getUrl(), theResourceEntity);
						return;
					}
				}

				TermCodeSystemVersion persCs = new TermCodeSystemVersion();

				populateCodeSystemVersionProperties(persCs, theCodeSystem, theResourceEntity);

				persCs.getConcepts().addAll(BaseTermReadSvcImpl.toPersistedConcepts(theCodeSystem.getConcept(), persCs));
				ourLog.debug("Code system has {} concepts", persCs.getConcepts().size());
				storeNewCodeSystemVersion(codeSystemResourcePid, codeSystemUrl, theCodeSystem.getName(), theCodeSystem.getVersion(), persCs, theResourceEntity);
			}

		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public IIdType storeNewCodeSystemVersion(CodeSystem theCodeSystemResource, TermCodeSystemVersion theCodeSystemVersion, RequestDetails theRequest, List<ValueSet> theValueSets, List<ConceptMap> theConceptMaps) {
		Validate.notBlank(theCodeSystemResource.getUrl(), "theCodeSystemResource must have a URL");

		IIdType csId = myTerminologyVersionAdapterSvc.createOrUpdateCodeSystem(theCodeSystemResource);
		ResourcePersistentId codeSystemResourcePid = myIdHelperService.resolveResourcePersistentIds(csId.getResourceType(), csId.getIdPart());
		ResourceTable resource = myResourceTableDao.getOne(codeSystemResourcePid.getIdAsLong());

		ourLog.info("CodeSystem resource has ID: {}", csId.getValue());

		populateCodeSystemVersionProperties(theCodeSystemVersion, theCodeSystemResource, resource);

		storeNewCodeSystemVersion(codeSystemResourcePid, theCodeSystemResource.getUrl(), theCodeSystemResource.getName(), theCodeSystemResource.getVersion(), theCodeSystemVersion, resource);

		myDeferredStorageSvc.addConceptMapsToStorageQueue(theConceptMaps);
		myDeferredStorageSvc.addValueSetsToStorageQueue(theValueSets);

		return csId;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void storeNewCodeSystemVersion(ResourcePersistentId theCodeSystemResourcePid, String theSystemUri, String theSystemName, String theSystemVersionId, TermCodeSystemVersion theCodeSystemVersion, ResourceTable theCodeSystemResourceTable) {
		ourLog.debug("Storing code system");

		ValidateUtil.isTrueOrThrowInvalidRequest(theCodeSystemVersion.getResource() != null, "No resource supplied");
		ValidateUtil.isNotBlankOrThrowInvalidRequest(theSystemUri, "No system URI supplied");

		// Grab the existing versions so we can delete them later
		List<TermCodeSystemVersion> existing = myCodeSystemVersionDao.findByCodeSystemResourcePid(theCodeSystemResourcePid.getIdAsLong());

		/*
		 * For now we always delete old versions. At some point it would be nice to allow configuration to keep old versions.
		 */

		for (TermCodeSystemVersion next : existing) {
			ourLog.info("Deleting old code system version {}", next.getPid());
			Long codeSystemVersionPid = next.getPid();
			deleteCodeSystemVersion(codeSystemVersionPid);
		}

		ourLog.debug("Flushing...");
		myConceptDao.flush();
		ourLog.debug("Done flushing");

		/*
		 * Do the upload
		 */

		TermCodeSystem codeSystem = getOrCreateTermCodeSystem(theCodeSystemResourcePid, theSystemUri, theSystemName, theCodeSystemResourceTable);

		theCodeSystemVersion.setCodeSystem(codeSystem);

		theCodeSystemVersion.setCodeSystemDisplayName(theSystemName);
		theCodeSystemVersion.setCodeSystemVersionId(theSystemVersionId);

		ourLog.debug("Validating all codes in CodeSystem for storage (this can take some time for large sets)");

		// Validate the code system
		ArrayList<String> conceptsStack = new ArrayList<>();
		IdentityHashMap<TermConcept, Object> allConcepts = new IdentityHashMap<>();
		int totalCodeCount = 0;
		for (TermConcept next : theCodeSystemVersion.getConcepts()) {
			totalCodeCount += validateConceptForStorage(next, theCodeSystemVersion, conceptsStack, allConcepts);
		}

		ourLog.debug("Saving version containing {} concepts", totalCodeCount);

		TermCodeSystemVersion codeSystemVersion = myCodeSystemVersionDao.saveAndFlush(theCodeSystemVersion);

		ourLog.debug("Saving code system");

		codeSystem.setCurrentVersion(theCodeSystemVersion);
		codeSystem = myCodeSystemDao.saveAndFlush(codeSystem);

		ourLog.debug("Setting CodeSystemVersion[{}] on {} concepts...", codeSystem.getPid(), totalCodeCount);

		for (TermConcept next : theCodeSystemVersion.getConcepts()) {
			populateVersion(next, codeSystemVersion);
		}

		ourLog.debug("Saving {} concepts...", totalCodeCount);

		IdentityHashMap<TermConcept, Object> conceptsStack2 = new IdentityHashMap<>();
		for (TermConcept next : theCodeSystemVersion.getConcepts()) {
			persistChildren(next, codeSystemVersion, conceptsStack2, totalCodeCount);
		}

		ourLog.debug("Done saving concepts, flushing to database");

		myConceptDao.flush();
		myConceptParentChildLinkDao.flush();

		if (myDeferredStorageSvc.isStorageQueueEmpty() == false) {
			ourLog.info("Note that some concept saving has been deferred");
		}
	}

	private void deleteCodeSystemVersion(final Long theCodeSystemVersionPid) {
		ourLog.info(" * Deleting code system version {}", theCodeSystemVersionPid);

		PageRequest page1000 = PageRequest.of(0, 1000);

		// Parent/Child links
		{
			String descriptor = "parent/child links";
			Supplier<Slice<Long>> loader = () -> myConceptParentChildLinkDao.findIdsByCodeSystemVersion(page1000, theCodeSystemVersionPid);
			Supplier<Integer> counter = () -> myConceptParentChildLinkDao.countByCodeSystemVersion(theCodeSystemVersionPid);
			doDelete(descriptor, loader, counter, myConceptParentChildLinkDao);
		}

		// Properties
		{
			String descriptor = "concept properties";
			Supplier<Slice<Long>> loader = () -> myConceptPropertyDao.findIdsByCodeSystemVersion(page1000, theCodeSystemVersionPid);
			Supplier<Integer> counter = () -> myConceptPropertyDao.countByCodeSystemVersion(theCodeSystemVersionPid);
			doDelete(descriptor, loader, counter, myConceptPropertyDao);
		}

		// Designations
		{
			String descriptor = "concept designations";
			Supplier<Slice<Long>> loader = () -> myConceptDesignationDao.findIdsByCodeSystemVersion(page1000, theCodeSystemVersionPid);
			Supplier<Integer> counter = () -> myConceptDesignationDao.countByCodeSystemVersion(theCodeSystemVersionPid);
			doDelete(descriptor, loader, counter, myConceptDesignationDao);
		}

		// Concepts
		{
			String descriptor = "concepts";
			// For some reason, concepts are much slower to delete, so use a smaller batch size
			PageRequest page100 = PageRequest.of(0, 100);
			Supplier<Slice<Long>> loader = () -> myConceptDao.findIdsByCodeSystemVersion(page100, theCodeSystemVersionPid);
			Supplier<Integer> counter = () -> myConceptDao.countByCodeSystemVersion(theCodeSystemVersionPid);
			doDelete(descriptor, loader, counter, myConceptDao);
		}

		Optional<TermCodeSystem> codeSystemOpt = myCodeSystemDao.findWithCodeSystemVersionAsCurrentVersion(theCodeSystemVersionPid);
		if (codeSystemOpt.isPresent()) {
			TermCodeSystem codeSystem = codeSystemOpt.get();
			ourLog.info(" * Removing code system version {} as current version of code system {}", theCodeSystemVersionPid, codeSystem.getPid());
			codeSystem.setCurrentVersion(null);
			myCodeSystemDao.save(codeSystem);
		}

		ourLog.info(" * Deleting code system version");
		myCodeSystemVersionDao.deleteById(theCodeSystemVersionPid);

	}

	private void validateDstu3OrNewer() {
		Validate.isTrue(myContext.getVersion().getVersion().isEqualOrNewerThan(FhirVersionEnum.DSTU3), "Terminology operations only supported in DSTU3+ mode");
	}

	private void addConceptInHierarchy(TermCodeSystemVersion theCsv, Collection<String> theParentCodes, TermConcept theConceptToAdd, UploadStatistics theStatisticsTracker, Map<String, TermConcept> theCodeToConcept, int theSequence) {
		TermConcept conceptToAdd = theConceptToAdd;
		List<TermConceptParentChildLink> childrenToAdd = theConceptToAdd.getChildren();

		String nextCodeToAdd = conceptToAdd.getCode();
		String parentDescription = "(root concept)";

		ourLog.info("Saving concept {} with parent {}", theStatisticsTracker.getUpdatedConceptCount(), parentDescription);

		Optional<TermConcept> existingCodeOpt = myConceptDao.findByCodeSystemAndCode(theCsv, nextCodeToAdd);
		List<TermConceptParentChildLink> existingParentLinks;
		if (existingCodeOpt.isPresent()) {
			TermConcept existingCode = existingCodeOpt.get();
			existingCode.setIndexStatus(null);
			existingCode.setDisplay(conceptToAdd.getDisplay());
			conceptToAdd = existingCode;
			existingParentLinks = conceptToAdd.getParents();
		} else {
			existingParentLinks = Collections.emptyList();
		}

		Set<TermConcept> parentConceptsWeShouldLinkTo = new HashSet<>();
		for (String nextParentCode : theParentCodes) {

			// Don't add parent links that already exist for the code
			if (existingParentLinks.stream().anyMatch(t -> t.getParent().getCode().equals(nextParentCode))) {
				continue;
			}

			TermConcept nextParentOpt = theCodeToConcept.get(nextParentCode);
			if (nextParentOpt == null) {
				nextParentOpt = myConceptDao.findByCodeSystemAndCode(theCsv, nextParentCode).orElse(null);
			}
			if (nextParentOpt == null) {
				throw new InvalidRequestException("Unable to add code \"" + nextCodeToAdd + "\" to unknown parent: " + nextParentCode);
			}
			parentConceptsWeShouldLinkTo.add(nextParentOpt);
		}

		if (conceptToAdd.getSequence() == null) {
			conceptToAdd.setSequence(theSequence);
		}

		// Null out the hierarchy PIDs for this concept always. We do this because we're going to
		// force a reindex, and it'll be regenerated then
		conceptToAdd.setParentPids(null);
		conceptToAdd.setCodeSystemVersion(theCsv);

		if (theStatisticsTracker.getUpdatedConceptCount() <= myDaoConfig.getDeferIndexingForCodesystemsOfSize()) {
			saveConcept(conceptToAdd);
			Long nextConceptPid = conceptToAdd.getId();
			Validate.notNull(nextConceptPid);
		} else {
			myDeferredStorageSvc.addConceptToStorageQueue(conceptToAdd);
		}

		theCodeToConcept.put(conceptToAdd.getCode(), conceptToAdd);

		theStatisticsTracker.incrementUpdatedConceptCount();

		// Add link to new child to the parent
		for (TermConcept nextParentConcept : parentConceptsWeShouldLinkTo) {
			TermConceptParentChildLink parentLink = new TermConceptParentChildLink();
			parentLink.setParent(nextParentConcept);
			parentLink.setChild(conceptToAdd);
			parentLink.setCodeSystem(theCsv);
			parentLink.setRelationshipType(TermConceptParentChildLink.RelationshipTypeEnum.ISA);
			nextParentConcept.getChildren().add(parentLink);
			conceptToAdd.getParents().add(parentLink);
			ourLog.info("Saving parent/child link - Parent[{}] Child[{}]", parentLink.getParent().getCode(), parentLink.getChild().getCode());

			if (theStatisticsTracker.getUpdatedConceptCount() <= myDaoConfig.getDeferIndexingForCodesystemsOfSize()) {
				myConceptParentChildLinkDao.save(parentLink);
			} else {
				myDeferredStorageSvc.addConceptLinkToStorageQueue(parentLink);
			}

		}

		ourLog.trace("About to save parent-child links");

		// Save children recursively
		int childIndex = 0;
		for (TermConceptParentChildLink nextChildConceptLink : new ArrayList<>(childrenToAdd)) {

			TermConcept nextChild = nextChildConceptLink.getChild();

			for (int i = 0; i < nextChild.getParents().size(); i++) {
				if (nextChild.getParents().get(i).getId() == null) {
					String parentCode = nextChild.getParents().get(i).getParent().getCode();
					TermConcept parentConcept = theCodeToConcept.get(parentCode);
					if (parentConcept == null) {
						parentConcept = myConceptDao.findByCodeSystemAndCode(theCsv, parentCode).orElse(null);
					}
					if (parentConcept == null) {
						throw new IllegalArgumentException("Unknown parent code: " + parentCode);
					}

					nextChild.getParents().get(i).setParent(parentConcept);
				}
			}

			Collection<String> parentCodes = nextChild.getParents().stream().map(t -> t.getParent().getCode()).collect(Collectors.toList());
			addConceptInHierarchy(theCsv, parentCodes, nextChild, theStatisticsTracker, theCodeToConcept, childIndex);

			childIndex++;
		}

	}

	private ResourcePersistentId getCodeSystemResourcePid(IIdType theIdType) {
		return myIdHelperService.resolveResourcePersistentIds(theIdType.getResourceType(), theIdType.getIdPart());
	}

	private void persistChildren(TermConcept theConcept, TermCodeSystemVersion theCodeSystem, IdentityHashMap<TermConcept, Object> theConceptsStack, int theTotalConcepts) {
		if (theConceptsStack.put(theConcept, PLACEHOLDER_OBJECT) != null) {
			return;
		}

		if ((theConceptsStack.size() + 1) % 10000 == 0) {
			float pct = (float) theConceptsStack.size() / (float) theTotalConcepts;
			ourLog.info("Have processed {}/{} concepts ({}%)", theConceptsStack.size(), theTotalConcepts, (int) (pct * 100.0f));
		}

		theConcept.setCodeSystemVersion(theCodeSystem);
		theConcept.setIndexStatus(BaseHapiFhirDao.INDEX_STATUS_INDEXED);

		if (theConceptsStack.size() <= myDaoConfig.getDeferIndexingForCodesystemsOfSize()) {
			saveConcept(theConcept);
		} else {
			myDeferredStorageSvc.addConceptToStorageQueue(theConcept);
		}

		for (TermConceptParentChildLink next : theConcept.getChildren()) {
			persistChildren(next.getChild(), theCodeSystem, theConceptsStack, theTotalConcepts);
		}

		for (TermConceptParentChildLink next : theConcept.getChildren()) {
			if (theConceptsStack.size() <= myDaoConfig.getDeferIndexingForCodesystemsOfSize()) {
				saveConceptLink(next);
			} else {
				myDeferredStorageSvc.addConceptLinkToStorageQueue(next);
			}
		}

	}

	private void populateVersion(TermConcept theNext, TermCodeSystemVersion theCodeSystemVersion) {
		if (theNext.getCodeSystemVersion() != null) {
			return;
		}
		theNext.setCodeSystemVersion(theCodeSystemVersion);
		for (TermConceptParentChildLink next : theNext.getChildren()) {
			populateVersion(next.getChild(), theCodeSystemVersion);
		}
	}

	private void saveConceptLink(TermConceptParentChildLink next) {
		if (next.getId() == null) {
			myConceptParentChildLinkDao.save(next);
		}
	}

	private int ensureParentsSaved(Collection<TermConceptParentChildLink> theParents) {
		ourLog.trace("Checking {} parents", theParents.size());
		int retVal = 0;

		for (TermConceptParentChildLink nextLink : theParents) {
			if (nextLink.getRelationshipType() == TermConceptParentChildLink.RelationshipTypeEnum.ISA) {
				TermConcept nextParent = nextLink.getParent();
				retVal += ensureParentsSaved(nextParent.getParents());
				if (nextParent.getId() == null) {
					nextParent.setUpdated(new Date());
					myConceptDao.saveAndFlush(nextParent);
					retVal++;
					ourLog.debug("Saved parent code {} and got id {}", nextParent.getCode(), nextParent.getId());
				}
			}
		}

		return retVal;
	}

	@Nonnull
	private TermCodeSystem getOrCreateTermCodeSystem(ResourcePersistentId theCodeSystemResourcePid, String theSystemUri, String theSystemName, ResourceTable theCodeSystemResourceTable) {
		TermCodeSystem codeSystem = myCodeSystemDao.findByCodeSystemUri(theSystemUri);
		if (codeSystem == null) {
			codeSystem = myCodeSystemDao.findByResourcePid(theCodeSystemResourcePid.getIdAsLong());
			if (codeSystem == null) {
				codeSystem = new TermCodeSystem();
			}
			codeSystem.setResource(theCodeSystemResourceTable);
		} else {
			if (!ObjectUtil.equals(codeSystem.getResource().getId(), theCodeSystemResourceTable.getId())) {
				String msg = myContext.getLocalizer().getMessage(BaseTermReadSvcImpl.class, "cannotCreateDuplicateCodeSystemUrl", theSystemUri,
					codeSystem.getResource().getIdDt().toUnqualifiedVersionless().getValue());
				throw new UnprocessableEntityException(msg);
			}
		}

		codeSystem.setCodeSystemUri(theSystemUri);
		codeSystem.setName(theSystemName);
		codeSystem = myCodeSystemDao.save(codeSystem);
		return codeSystem;
	}

	private void populateCodeSystemVersionProperties(TermCodeSystemVersion theCodeSystemVersion, CodeSystem theCodeSystemResource, ResourceTable theResourceTable) {
		theCodeSystemVersion.setResource(theResourceTable);
		theCodeSystemVersion.setCodeSystemDisplayName(theCodeSystemResource.getName());
		theCodeSystemVersion.setCodeSystemVersionId(theCodeSystemResource.getVersion());
	}

	private void deleteConceptChildrenAndConcept(TermConcept theConcept, AtomicInteger theRemoveCounter) {
		for (TermConceptParentChildLink nextChildLink : theConcept.getChildren()) {
			deleteConceptChildrenAndConcept(nextChildLink.getChild(), theRemoveCounter);
		}

		myConceptParentChildLinkDao.deleteByConceptPid(theConcept.getId());

		myConceptDesignationDao.deleteAll(theConcept.getDesignations());
		myConceptPropertyDao.deleteAll(theConcept.getProperties());

		ourLog.info("Deleting concept {} - Code {}", theConcept.getId(), theConcept.getCode());
		myConceptDao.deleteByPid(theConcept.getId());
		theRemoveCounter.incrementAndGet();
	}


	private <T> void doDelete(String theDescriptor, Supplier<Slice<Long>> theLoader, Supplier<Integer> theCounter, IHapiJpaRepository<T> theDao) {
		int count;
		ourLog.info(" * Deleting {}", theDescriptor);
		int totalCount = theCounter.get();
		StopWatch sw = new StopWatch();
		count = 0;
		while (true) {
			Slice<Long> link = theLoader.get();
			if (!link.hasContent()) {
				break;
			}

			TransactionTemplate txTemplate = new TransactionTemplate(myTransactionManager);
			txTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
			txTemplate.execute(t -> {
				link.forEach(id -> theDao.deleteByPid(id));
				return null;
			});

			count += link.getNumberOfElements();
			ourLog.info(" * {} {} deleted - {}/sec - ETA: {}", count, theDescriptor, sw.formatThroughput(count, TimeUnit.SECONDS), sw.getEstimatedTimeRemaining(count, totalCount));
		}
		theDao.flush();
	}


	private int validateConceptForStorage(TermConcept theConcept, TermCodeSystemVersion theCodeSystem, ArrayList<String> theConceptsStack,
													  IdentityHashMap<TermConcept, Object> theAllConcepts) {
		ValidateUtil.isTrueOrThrowInvalidRequest(theConcept.getCodeSystemVersion() != null, "CodeSystemVersion is null");
		ValidateUtil.isTrueOrThrowInvalidRequest(theConcept.getCodeSystemVersion() == theCodeSystem, "CodeSystems are not equal");
		ValidateUtil.isNotBlankOrThrowInvalidRequest(theConcept.getCode(), "CodeSystem contains a code with no code value");

		if (theConceptsStack.contains(theConcept.getCode())) {
			throw new InvalidRequestException("CodeSystem contains circular reference around code " + theConcept.getCode());
		}
		theConceptsStack.add(theConcept.getCode());

		int retVal = 0;
		if (theAllConcepts.put(theConcept, theAllConcepts) == null) {
			if (theAllConcepts.size() % 1000 == 0) {
				ourLog.info("Have validated {} concepts", theAllConcepts.size());
			}
			retVal = 1;
		}

		for (TermConceptParentChildLink next : theConcept.getChildren()) {
			next.setCodeSystem(theCodeSystem);
			retVal += validateConceptForStorage(next.getChild(), theCodeSystem, theConceptsStack, theAllConcepts);
		}

		theConceptsStack.remove(theConceptsStack.size() - 1);

		return retVal;
	}


}
