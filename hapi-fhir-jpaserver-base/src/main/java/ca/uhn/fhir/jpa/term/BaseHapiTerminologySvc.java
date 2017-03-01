package ca.uhn.fhir.jpa.term;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Stopwatch;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimaps;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.BaseHapiFhirDao;
import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.dao.data.ITermCodeSystemDao;
import ca.uhn.fhir.jpa.dao.data.ITermCodeSystemVersionDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptParentChildLinkDao;
import ca.uhn.fhir.jpa.entity.TermCodeSystem;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.entity.TermConceptParentChildLink;
import ca.uhn.fhir.jpa.entity.TermConceptParentChildLink.RelationshipTypeEnum;
import ca.uhn.fhir.jpa.util.StopWatch;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.util.ObjectUtil;
import ca.uhn.fhir.util.ValidateUtil;

public abstract class BaseHapiTerminologySvc implements IHapiTerminologySvc {
	private static boolean ourForceSaveDeferredAlwaysForUnitTest;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseHapiTerminologySvc.class);
	private static final Object PLACEHOLDER_OBJECT = new Object();

	@Autowired
	protected ITermCodeSystemDao myCodeSystemDao;

	@Autowired
	private ITermCodeSystemVersionDao myCodeSystemVersionDao;

	@Autowired
	protected ITermConceptDao myConceptDao;

	private List<TermConceptParentChildLink> myConceptLinksToSaveLater = new ArrayList<TermConceptParentChildLink>();

	@Autowired
	private ITermConceptParentChildLinkDao myConceptParentChildLinkDao;

	private List<TermConcept> myConceptsToSaveLater = new ArrayList<TermConcept>();

	@Autowired
	protected FhirContext myContext;

	@Autowired
	private DaoConfig myDaoConfig;

	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	protected EntityManager myEntityManager;
	
	private long myNextReindexPass;
	private boolean myProcessDeferred = true;

	@Autowired
	private PlatformTransactionManager myTransactionMgr;

	private boolean addToSet(Set<TermConcept> theSetToPopulate, TermConcept theConcept) {
		boolean retVal = theSetToPopulate.add(theConcept);
		if (retVal) {
			if (theSetToPopulate.size() >= myDaoConfig.getMaximumExpansionSize()) {
				String msg = myContext.getLocalizer().getMessage(BaseHapiTerminologySvc.class, "expansionTooLarge", myDaoConfig.getMaximumExpansionSize());
				throw new InvalidRequestException(msg);
			}
		}
		return retVal;
	}

	private int ensureParentsSaved(Collection<TermConceptParentChildLink> theParents) {
		ourLog.trace("Checking {} parents", theParents.size());
		int retVal = 0;
		
		for (TermConceptParentChildLink nextLink : theParents) {
			if (nextLink.getRelationshipType() == RelationshipTypeEnum.ISA) {
				TermConcept nextParent = nextLink.getParent();
				retVal += ensureParentsSaved(nextParent.getParents());
				if (nextParent.getId() == null) {
					myConceptDao.saveAndFlush(nextParent);
					retVal++;
					ourLog.debug("Saved parent code {} and got id {}", nextParent.getCode(), nextParent.getId());
				}
			}
		}
		
		return retVal;
	}

	private void fetchChildren(TermConcept theConcept, Set<TermConcept> theSetToPopulate) {
		for (TermConceptParentChildLink nextChildLink : theConcept.getChildren()) {
			TermConcept nextChild = nextChildLink.getChild();
			if (addToSet(theSetToPopulate, nextChild)) {
				fetchChildren(nextChild, theSetToPopulate);
			}
		}
	}

	private TermConcept fetchLoadedCode(Long theCodeSystemResourcePid, Long theCodeSystemVersionPid, String theCode) {
		TermCodeSystemVersion codeSystem = myCodeSystemVersionDao.findByCodeSystemResourceAndVersion(theCodeSystemResourcePid, theCodeSystemVersionPid);
		TermConcept concept = myConceptDao.findByCodeSystemAndCode(codeSystem, theCode);
		return concept;
	}

	private void fetchParents(TermConcept theConcept, Set<TermConcept> theSetToPopulate) {
		for (TermConceptParentChildLink nextChildLink : theConcept.getParents()) {
			TermConcept nextChild = nextChildLink.getParent();
			if (addToSet(theSetToPopulate, nextChild)) {
				fetchParents(nextChild, theSetToPopulate);
			}
		}
	}

	public TermConcept findCode(String theCodeSystem, String theCode) {
		TermCodeSystemVersion csv = findCurrentCodeSystemVersionForSystem(theCodeSystem);

		return myConceptDao.findByCodeSystemAndCode(csv, theCode);
	}

	@Override
	public List<TermConcept> findCodes(String theSystem) {
		return myConceptDao.findByCodeSystemVersion(findCurrentCodeSystemVersionForSystem(theSystem));
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public Set<TermConcept> findCodesAbove(Long theCodeSystemResourcePid, Long theCodeSystemVersionPid, String theCode) {
		Stopwatch stopwatch = Stopwatch.createStarted();

		TermConcept concept = fetchLoadedCode(theCodeSystemResourcePid, theCodeSystemVersionPid, theCode);
		if (concept == null) {
			return Collections.emptySet();
		}

		Set<TermConcept> retVal = new HashSet<TermConcept>();
		retVal.add(concept);

		fetchParents(concept, retVal);

		ourLog.info("Fetched {} codes above code {} in {}ms", new Object[] { retVal.size(), theCode, stopwatch.elapsed(TimeUnit.MILLISECONDS) });
		return retVal;
	}

	@Override
	public List<VersionIndependentConcept> findCodesAbove(String theSystem, String theCode) {
		TermCodeSystem cs = getCodeSystem(theSystem);
		if (cs == null) {
			return findCodesAboveUsingBuiltInSystems(theSystem, theCode);
		}
		TermCodeSystemVersion csv = cs.getCurrentVersion();

		Set<TermConcept> codes = findCodesAbove(cs.getResource().getId(), csv.getResourceVersionId(), theCode);
		ArrayList<VersionIndependentConcept> retVal = toVersionIndependentConcepts(theSystem, codes);
		return retVal;
	}

	/**
	 * Subclasses may override
	 * @param theSystem The code system
	 * @param theCode The code
	 */
	protected List<VersionIndependentConcept> findCodesAboveUsingBuiltInSystems(String theSystem, String theCode) {
		return Collections.emptyList();
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public Set<TermConcept> findCodesBelow(Long theCodeSystemResourcePid, Long theCodeSystemVersionPid, String theCode) {
		Stopwatch stopwatch = Stopwatch.createStarted();

		TermConcept concept = fetchLoadedCode(theCodeSystemResourcePid, theCodeSystemVersionPid, theCode);
		if (concept == null) {
			return Collections.emptySet();
		}

		Set<TermConcept> retVal = new HashSet<TermConcept>();
		retVal.add(concept);

		fetchChildren(concept, retVal);

		ourLog.info("Fetched {} codes below code {} in {}ms", new Object[] { retVal.size(), theCode, stopwatch.elapsed(TimeUnit.MILLISECONDS) });
		return retVal;
	}

	@Override
	public List<VersionIndependentConcept> findCodesBelow(String theSystem, String theCode) {
		TermCodeSystem cs = getCodeSystem(theSystem);
		if (cs == null) {
			return findCodesBelowUsingBuiltInSystems(theSystem, theCode);
		}
		TermCodeSystemVersion csv = cs.getCurrentVersion();

		Set<TermConcept> codes = findCodesBelow(cs.getResource().getId(), csv.getResourceVersionId(), theCode);
		ArrayList<VersionIndependentConcept> retVal = toVersionIndependentConcepts(theSystem, codes);
		return retVal;
	}
	/**
	 * Subclasses may override
	 * @param theSystem The code system
	 * @param theCode The code
	 */
	protected List<VersionIndependentConcept> findCodesBelowUsingBuiltInSystems(String theSystem, String theCode) {
		return Collections.emptyList();
	}
	
	private TermCodeSystemVersion findCurrentCodeSystemVersionForSystem(String theCodeSystem) {
		TermCodeSystem cs = getCodeSystem(theCodeSystem);
		if (cs == null || cs.getCurrentVersion() == null) {
			return null;
		}
		TermCodeSystemVersion csv = cs.getCurrentVersion();
		return csv;
	}

	private TermCodeSystem getCodeSystem(String theSystem) {
		TermCodeSystem cs = myCodeSystemDao.findByCodeSystemUri(theSystem);
		return cs;
	}

	private void persistChildren(TermConcept theConcept, TermCodeSystemVersion theCodeSystem, IdentityHashMap<TermConcept, Object> theConceptsStack, int theTotalConcepts) {
		if (theConceptsStack.put(theConcept, PLACEHOLDER_OBJECT) != null) {
			return;
		}

		if (theConceptsStack.size() == 1 || theConceptsStack.size() % 10000 == 0) {
			float pct = (float) theConceptsStack.size() / (float) theTotalConcepts;
			ourLog.info("Have processed {}/{} concepts ({}%)", theConceptsStack.size(), theTotalConcepts, (int)( pct*100.0f));
		}

		theConcept.setCodeSystem(theCodeSystem);
		theConcept.setIndexStatus(BaseHapiFhirDao.INDEX_STATUS_INDEXED);

		if (theConceptsStack.size() <= myDaoConfig.getDeferIndexingForCodesystemsOfSize()) {
			saveConcept(theConcept);
		} else {
			myConceptsToSaveLater.add(theConcept);
		}
		
		for (TermConceptParentChildLink next : theConcept.getChildren()) {
			persistChildren(next.getChild(), theCodeSystem, theConceptsStack, theTotalConcepts);
		}

		for (TermConceptParentChildLink next : theConcept.getChildren()) {
			if (theConceptsStack.size() <= myDaoConfig.getDeferIndexingForCodesystemsOfSize()) {
				saveConceptLink(next);
			} else {
				myConceptLinksToSaveLater.add(next);
			}
		}
		
	}

	private void populateVersion(TermConcept theNext, TermCodeSystemVersion theCodeSystemVersion) {
		if (theNext.getCodeSystem() != null) {
			return;
		}
		theNext.setCodeSystem(theCodeSystemVersion);
		for (TermConceptParentChildLink next : theNext.getChildren()) {
			populateVersion(next.getChild(), theCodeSystemVersion);
		}
	}

	private ArrayListMultimap<Long, Long> myChildToParentPidCache;
	
	private void processReindexing() {
		if (System.currentTimeMillis() < myNextReindexPass && !ourForceSaveDeferredAlwaysForUnitTest) {
			return;
		}
		
		TransactionTemplate tt = new TransactionTemplate(myTransactionMgr);
		tt.setPropagationBehavior(TransactionTemplate.PROPAGATION_REQUIRES_NEW);
		tt.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theArg0) {
				int maxResult = 1000;
				Page<TermConcept> concepts = myConceptDao.findResourcesRequiringReindexing(new PageRequest(0, maxResult));
				if (concepts.hasContent() == false) {
					myNextReindexPass = System.currentTimeMillis() + DateUtils.MILLIS_PER_MINUTE;
					myChildToParentPidCache = null;
					return;
				}

				if (myChildToParentPidCache == null) {
					myChildToParentPidCache = ArrayListMultimap.create();
				}
				
				ourLog.info("Indexing {} / {} concepts", concepts.getContent().size(), concepts.getTotalElements());

				int count = 0;
				StopWatch stopwatch = new StopWatch();

				for (TermConcept nextConcept : concepts) {
					
					StringBuilder parentsBuilder = new StringBuilder();
					createParentsString(parentsBuilder, nextConcept.getId());
					nextConcept.setParentPids(parentsBuilder.toString());
					
					saveConcept(nextConcept);
					count++;
				}
				
				ourLog.info("Indexed {} / {} concepts in {}ms - Avg {}ms / resource", new Object[] { count, concepts.getContent().size(), stopwatch.getMillis(), stopwatch.getMillisPerOperation(count) });
			}

			private void createParentsString(StringBuilder theParentsBuilder, Long theConceptPid) {
				Validate.notNull(theConceptPid, "theConceptPid must not be null");
				List<Long> parents = myChildToParentPidCache.get(theConceptPid);
				if (parents.contains(-1L)) {
					return;
				} else if (parents.isEmpty()) {
					Collection<TermConceptParentChildLink> parentLinks = myConceptParentChildLinkDao.findAllWithChild(theConceptPid);
					if (parentLinks.isEmpty()) {
						myChildToParentPidCache.put(theConceptPid, -1L);
						return;
					} else {
						for (TermConceptParentChildLink next : parentLinks) {
							myChildToParentPidCache.put(theConceptPid, next.getParentPid());
						}
					}
				}
				
				
				for (Long nextParent : parents) {
					if (theParentsBuilder.length() > 0) {
						theParentsBuilder.append(' ');
					}
					theParentsBuilder.append(nextParent);
					createParentsString(theParentsBuilder, nextParent);
				}
			}
		});

	}

	private int saveConcept(TermConcept theConcept) {
		int retVal = 0;
	
		/*
		 * If the concept has an ID, we're reindexing, so there's no need to
		 * save parent concepts first (it's way too slow to do that)
		 */
		if (theConcept.getId() == null) {
			retVal += ensureParentsSaved(theConcept.getParents());
		}
		
		if (theConcept.getId() == null || theConcept.getIndexStatus() == null) {
			retVal++;
			theConcept.setIndexStatus(BaseHapiFhirDao.INDEX_STATUS_INDEXED);
			myConceptDao.save(theConcept);
		}
		
		ourLog.trace("Saved {} and got PID {}", theConcept.getCode(), theConcept.getId());
		return retVal;
	}
	
	private void saveConceptLink(TermConceptParentChildLink next) {
		if (next.getId() == null) {
			myConceptParentChildLinkDao.save(next);
		}
	}

	@Scheduled(fixedRate=5000)
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public synchronized void saveDeferred() {
		if (!myProcessDeferred) {
			return;
		} else if (myConceptsToSaveLater.isEmpty() && myConceptLinksToSaveLater.isEmpty()) {
			processReindexing();
			return;
		}
		
		int codeCount = 0, relCount = 0;
		StopWatch stopwatch = new StopWatch();
		
		int count = Math.min(myDaoConfig.getDeferIndexingForCodesystemsOfSize(), myConceptsToSaveLater.size());
		ourLog.info("Saving {} deferred concepts...", count);
		while (codeCount < count && myConceptsToSaveLater.size() > 0) {
			TermConcept next = myConceptsToSaveLater.remove(0);
			codeCount += saveConcept(next);
		}

		if (codeCount > 0) {
			ourLog.info("Saved {} deferred concepts ({} codes remain and {} relationships remain) in {}ms ({}ms / code)", new Object[] {codeCount, myConceptsToSaveLater.size(), myConceptLinksToSaveLater.size(), stopwatch.getMillis(), stopwatch.getMillisPerOperation(codeCount)});
		}
		
		if (codeCount == 0) {
			count = Math.min(myDaoConfig.getDeferIndexingForCodesystemsOfSize(), myConceptLinksToSaveLater.size());
			ourLog.info("Saving {} deferred concept relationships...", count);
			while (relCount < count && myConceptLinksToSaveLater.size() > 0) {
				TermConceptParentChildLink next = myConceptLinksToSaveLater.remove(0);
				
				if (myConceptDao.findOne(next.getChild().getId()) == null || myConceptDao.findOne(next.getParent().getId()) == null) {
					ourLog.warn("Not inserting link from child {} to parent {} because it appears to have been deleted", next.getParent().getCode(), next.getChild().getCode());
					continue;
				}
				
				saveConceptLink(next);
				relCount++;
			}
		}
		
		if (relCount > 0) {
			ourLog.info("Saved {} deferred relationships ({} remain) in {}ms ({}ms / code)", new Object[] {relCount, myConceptLinksToSaveLater.size(), stopwatch.getMillis(), stopwatch.getMillisPerOperation(codeCount)});
		}
		
		if ((myConceptsToSaveLater.size() + myConceptLinksToSaveLater.size()) == 0) {
			ourLog.info("All deferred concepts and relationships have now been synchronized to the database");
		}
	}
	
	@Override
	public void setProcessDeferred(boolean theProcessDeferred) {
		myProcessDeferred = theProcessDeferred;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void storeNewCodeSystemVersion(Long theCodeSystemResourcePid, String theSystemUri, TermCodeSystemVersion theCodeSystemVersion) {
		ourLog.info("Storing code system");

		ValidateUtil.isTrueOrThrowInvalidRequest(theCodeSystemVersion.getResource() != null, "No resource supplied");
		ValidateUtil.isNotBlankOrThrowInvalidRequest(theSystemUri, "No system URI supplied");

		// Grab the existing versions so we can delete them later
		List<TermCodeSystemVersion> existing = myCodeSystemVersionDao.findByCodeSystemResource(theCodeSystemResourcePid);

		/*
		 * For now we always delete old versions.. At some point it would be nice to allow configuration to keep old versions
		 */

		ourLog.info("Deleting old code system versions");
		for (TermCodeSystemVersion next : existing) {
			ourLog.info(" * Deleting code system version {}", next.getPid());
			myConceptParentChildLinkDao.deleteByCodeSystemVersion(next.getPid());
			myConceptDao.deleteByCodeSystemVersion(next.getPid());
		}

		ourLog.info("Flushing...");
		
		myConceptParentChildLinkDao.flush();
		myConceptDao.flush();

		ourLog.info("Done flushing");

		/*
		 * Do the upload
		 */

		TermCodeSystem codeSystem = getCodeSystem(theSystemUri);
		if (codeSystem == null) {
			codeSystem = myCodeSystemDao.findByResourcePid(theCodeSystemResourcePid);
			if (codeSystem == null) {
				codeSystem = new TermCodeSystem();
			}
			codeSystem.setResource(theCodeSystemVersion.getResource());
			codeSystem.setCodeSystemUri(theSystemUri);
			myCodeSystemDao.save(codeSystem);
		} else {
			if (!ObjectUtil.equals(codeSystem.getResource().getId(), theCodeSystemVersion.getResource().getId())) {
				String msg = myContext.getLocalizer().getMessage(BaseHapiTerminologySvc.class, "cannotCreateDuplicateCodeSystemUri", theSystemUri,
						codeSystem.getResource().getIdDt().toUnqualifiedVersionless().getValue());
				throw new UnprocessableEntityException(msg);
			}
		}

		ourLog.info("Validating all codes in CodeSystem for storage (this can take some time for large sets)");

		// Validate the code system
		ArrayList<String> conceptsStack = new ArrayList<String>();
		IdentityHashMap<TermConcept, Object> allConcepts = new IdentityHashMap<TermConcept, Object>();
		int totalCodeCount = 0;
		for (TermConcept next : theCodeSystemVersion.getConcepts()) {
			totalCodeCount += validateConceptForStorage(next, theCodeSystemVersion, conceptsStack, allConcepts);
		}

		ourLog.info("Saving version containing {} concepts", totalCodeCount);

		TermCodeSystemVersion codeSystemVersion = myCodeSystemVersionDao.saveAndFlush(theCodeSystemVersion);

		ourLog.info("Saving code system");

		codeSystem.setCurrentVersion(theCodeSystemVersion);
		codeSystem = myCodeSystemDao.saveAndFlush(codeSystem);

		ourLog.info("Setting codesystemversion on {} concepts...", totalCodeCount);

		for (TermConcept next : theCodeSystemVersion.getConcepts()) {
			populateVersion(next, codeSystemVersion);
		}

		ourLog.info("Saving {} concepts...", totalCodeCount);
		
		IdentityHashMap<TermConcept, Object> conceptsStack2 = new IdentityHashMap<TermConcept, Object>();
		for (TermConcept next : theCodeSystemVersion.getConcepts()) {
			persistChildren(next, codeSystemVersion, conceptsStack2, totalCodeCount);
		}

		ourLog.info("Done saving concepts, flushing to database");

		myConceptDao.flush();
		myConceptParentChildLinkDao.flush();

		ourLog.info("Done deleting old code system versions");
		
		if (myConceptsToSaveLater.size() > 0 || myConceptLinksToSaveLater.size() > 0) {
			ourLog.info("Note that some concept saving was deferred - still have {} concepts and {} relationships", myConceptsToSaveLater.size(), myConceptLinksToSaveLater.size());
		}
	}

	@Override
	public boolean supportsSystem(String theSystem) {
		TermCodeSystem cs = getCodeSystem(theSystem);
		return cs != null;
	}
	
	private ArrayList<VersionIndependentConcept> toVersionIndependentConcepts(String theSystem, Set<TermConcept> codes) {
		ArrayList<VersionIndependentConcept> retVal = new ArrayList<VersionIndependentConcept>(codes.size());
		for (TermConcept next : codes) {
			retVal.add(new VersionIndependentConcept(theSystem, next.getCode()));
		}
		return retVal;
	}

	private int validateConceptForStorage(TermConcept theConcept, TermCodeSystemVersion theCodeSystem, ArrayList<String> theConceptsStack,
			IdentityHashMap<TermConcept, Object> theAllConcepts) {
		ValidateUtil.isTrueOrThrowInvalidRequest(theConcept.getCodeSystem() != null, "CodesystemValue is null");
		ValidateUtil.isTrueOrThrowInvalidRequest(theConcept.getCodeSystem() == theCodeSystem, "CodeSystems are not equal");
		ValidateUtil.isNotBlankOrThrowInvalidRequest(theConcept.getCode(), "Codesystem contains a code with no code value");

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

	/**
	 * This method is present only for unit tests, do not call from client code
	 */
	@VisibleForTesting
	public static void setForceSaveDeferredAlwaysForUnitTest(boolean theForceSaveDeferredAlwaysForUnitTest) {
		ourForceSaveDeferredAlwaysForUnitTest = theForceSaveDeferredAlwaysForUnitTest;
	}

}
