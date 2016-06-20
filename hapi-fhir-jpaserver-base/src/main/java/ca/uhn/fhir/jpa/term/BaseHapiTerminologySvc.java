package ca.uhn.fhir.jpa.term;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2016 University Health Network
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
import java.util.Collections;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Stopwatch;

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
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.util.ObjectUtil;
import ca.uhn.fhir.util.ValidateUtil;

public abstract class BaseHapiTerminologySvc implements IHapiTerminologySvc {
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
	
	private boolean myProcessDeferred = true;

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
			return Collections.emptyList();
		}
		TermCodeSystemVersion csv = cs.getCurrentVersion();

		Set<TermConcept> codes = findCodesAbove(cs.getResource().getId(), csv.getResourceVersionId(), theCode);
		ArrayList<VersionIndependentConcept> retVal = toVersionIndependentConcepts(theSystem, codes);
		return retVal;
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
			return Collections.emptyList();
		}
		TermCodeSystemVersion csv = cs.getCurrentVersion();

		Set<TermConcept> codes = findCodesBelow(cs.getResource().getId(), csv.getResourceVersionId(), theCode);
		ArrayList<VersionIndependentConcept> retVal = toVersionIndependentConcepts(theSystem, codes);
		return retVal;
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
	
	private void parentPids(TermConcept theNextConcept, Set<Long> theParentPids) {
		for (TermConceptParentChildLink nextParentLink : theNextConcept.getParents()){
			TermConcept parent = nextParentLink.getParent();
			if (parent != null && theParentPids.add(parent.getId())) {
				parentPids(parent, theParentPids);
			}
		}
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

		Set<Long> parentPids = new HashSet<Long>();
		parentPids(theConcept, parentPids);
		theConcept.setParentPids(parentPids);

		if (theConceptsStack.size() <= myDaoConfig.getDeferIndexingForCodesystemsOfSize()) {
			myConceptDao.save(theConcept);
		} else {
			myConceptsToSaveLater.add(theConcept);
		}
		
		for (TermConceptParentChildLink next : theConcept.getChildren()) {
			persistChildren(next.getChild(), theCodeSystem, theConceptsStack, theTotalConcepts);
		}

		for (TermConceptParentChildLink next : theConcept.getChildren()) {
			if (theConceptsStack.size() <= myDaoConfig.getDeferIndexingForCodesystemsOfSize()) {
				myConceptParentChildLinkDao.save(next);
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

	@Scheduled(fixedRate=5000)
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public synchronized void saveDeferred() {
		if (!myProcessDeferred || ((myConceptsToSaveLater.isEmpty() && myConceptLinksToSaveLater.isEmpty()))) {
			return;
		}
		
		int codeCount = 0, relCount = 0;
		
		int count = Math.min(myDaoConfig.getDeferIndexingForCodesystemsOfSize(), myConceptsToSaveLater.size());
		ourLog.info("Saving {} deferred concepts...", count);
		while (codeCount < count && myConceptsToSaveLater.size() > 0) {
			TermConcept next = myConceptsToSaveLater.remove(0);
			myConceptDao.save(next);
			codeCount++;
		}

		if (codeCount == 0) {
			count = Math.min(myDaoConfig.getDeferIndexingForCodesystemsOfSize(), myConceptLinksToSaveLater.size());
			ourLog.info("Saving {} deferred concept relationships...", count);
			while (relCount < count && myConceptLinksToSaveLater.size() > 0) {
				TermConceptParentChildLink next = myConceptLinksToSaveLater.remove(0);
				myConceptParentChildLinkDao.save(next);
				relCount++;
			}
		}
		
		ourLog.info("Saved {} deferred concepts ({} remain) and {} deferred relationships ({} remain)", new Object[] {codeCount, myConceptsToSaveLater.size(), relCount, myConceptLinksToSaveLater.size()});
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

		ourLog.info("Saving version");

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

		/*
		 * For now we always delete old versions.. At some point it would be nice to allow configuration to keep old versions
		 */

		ourLog.info("Deleting old code system versions");
		for (TermCodeSystemVersion next : existing) {
			ourLog.info(" * Deleting code system version {}", next.getPid());
			myConceptParentChildLinkDao.deleteByCodeSystemVersion(next.getPid());
			myConceptDao.deleteByCodeSystemVersion(next.getPid());
		}

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

}
