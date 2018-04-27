package ca.uhn.fhir.jpa.term;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.BaseHapiFhirDao;
import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.dao.IFhirResourceDaoCodeSystem;
import ca.uhn.fhir.jpa.dao.data.*;
import ca.uhn.fhir.jpa.dao.r4.TranslationQuery;
import ca.uhn.fhir.jpa.dao.r4.TranslationRequest;
import ca.uhn.fhir.jpa.entity.*;
import ca.uhn.fhir.jpa.entity.TermConceptParentChildLink.RelationshipTypeEnum;
import ca.uhn.fhir.jpa.util.ScrollableResultsIterator;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.util.ObjectUtil;
import ca.uhn.fhir.util.StopWatch;
import ca.uhn.fhir.util.ValidateUtil;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Stopwatch;
import com.google.common.collect.ArrayListMultimap;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.lucene.search.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.query.dsl.BooleanJunction;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.ValueSet;
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

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public abstract class BaseHapiTerminologySvcImpl implements IHapiTerminologySvc {
	public static final int DEFAULT_FETCH_SIZE = 250;

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseHapiTerminologySvcImpl.class);
	private static final Object PLACEHOLDER_OBJECT = new Object();
	private static boolean ourForceSaveDeferredAlwaysForUnitTest;
	private static boolean ourLastResultsFromTranslationCache; // For testing.
	private static boolean ourLastResultsFromTranslationWithReverseCache; // For testing.
	@Autowired
	protected ITermCodeSystemDao myCodeSystemDao;
	@Autowired
	protected ITermConceptDao myConceptDao;
	@Autowired
	protected ITermConceptMapDao myConceptMapDao;
	@Autowired
	protected ITermConceptMapGroupDao myConceptMapGroupDao;
	@Autowired
	protected ITermConceptMapGroupElementDao myConceptMapGroupElementDao;
	@Autowired
	protected ITermConceptMapGroupElementTargetDao myConceptMapGroupElementTargetDao;
	@Autowired
	protected ITermConceptPropertyDao myConceptPropertyDao;
	@Autowired
	protected ITermConceptDesignationDao myConceptDesignationDao;
	@Autowired
	protected FhirContext myContext;
	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	protected EntityManager myEntityManager;
	private ArrayListMultimap<Long, Long> myChildToParentPidCache;
	@Autowired
	private ITermCodeSystemVersionDao myCodeSystemVersionDao;
	private List<TermConceptParentChildLink> myConceptLinksToSaveLater = new ArrayList<>();
	@Autowired
	private ITermConceptParentChildLinkDao myConceptParentChildLinkDao;
	private List<TermConcept> myDeferredConcepts = Collections.synchronizedList(new ArrayList<>());
	private List<ValueSet> myDeferredValueSets = Collections.synchronizedList(new ArrayList<>());
	private List<ConceptMap> myDeferredConceptMaps = Collections.synchronizedList(new ArrayList<>());
	@Autowired
	private DaoConfig myDaoConfig;
	private long myNextReindexPass;
	private boolean myProcessDeferred = true;
	@Autowired
	private PlatformTransactionManager myTransactionMgr;
	@Autowired(required = false)
	private IFhirResourceDaoCodeSystem<?, ?, ?> myCodeSystemResourceDao;

	private Cache<TranslationQuery, List<TermConceptMapGroupElementTarget>> myTranslationCache;
	private Cache<TranslationQuery, List<TermConceptMapGroupElement>> myTranslationWithReverseCache;

	private int myFetchSize = DEFAULT_FETCH_SIZE;

	private void addCodeIfNotAlreadyAdded(String theCodeSystem, ValueSet.ValueSetExpansionComponent theExpansionComponent, Set<String> theAddedCodes, TermConcept theConcept) {
		if (theAddedCodes.add(theConcept.getCode())) {
			ValueSet.ValueSetExpansionContainsComponent contains = theExpansionComponent.addContains();
			contains.setCode(theConcept.getCode());
			contains.setSystem(theCodeSystem);
			contains.setDisplay(theConcept.getDisplay());
			for (TermConceptDesignation nextDesignation : theConcept.getDesignations()) {
				contains
					.addDesignation()
					.setValue(nextDesignation.getValue())
					.getUse()
					.setSystem(nextDesignation.getUseSystem())
					.setCode(nextDesignation.getUseCode())
					.setDisplay(nextDesignation.getUseDisplay());
			}
		}
	}

	private void addConceptsToList(ValueSet.ValueSetExpansionComponent theExpansionComponent, Set<String> theAddedCodes, String theSystem, List<CodeSystem.ConceptDefinitionComponent> theConcept) {
		for (CodeSystem.ConceptDefinitionComponent next : theConcept) {
			if (!theAddedCodes.contains(next.getCode())) {
				theAddedCodes.add(next.getCode());
				ValueSet.ValueSetExpansionContainsComponent contains = theExpansionComponent.addContains();
				contains.setCode(next.getCode());
				contains.setSystem(theSystem);
				contains.setDisplay(next.getDisplay());
			}
			addConceptsToList(theExpansionComponent, theAddedCodes, theSystem, next.getConcept());
		}
	}

	private void addDisplayFilterExact(QueryBuilder qb, BooleanJunction<?> bool, ValueSet.ConceptSetFilterComponent nextFilter) {
		bool.must(qb.phrase().onField("myDisplay").sentence(nextFilter.getValue()).createQuery());
	}

	private void addDisplayFilterInexact(QueryBuilder qb, BooleanJunction<?> bool, ValueSet.ConceptSetFilterComponent nextFilter) {
		Query textQuery = qb
			.phrase()
			.withSlop(2)
			.onField("myDisplay").boostedTo(4.0f)
			.andField("myDisplayEdgeNGram").boostedTo(2.0f)
			// .andField("myDisplayNGram").boostedTo(1.0f)
			// .andField("myDisplayPhonetic").boostedTo(0.5f)
			.sentence(nextFilter.getValue().toLowerCase()).createQuery();
		bool.must(textQuery);
	}

	private boolean addToSet(Set<TermConcept> theSetToPopulate, TermConcept theConcept) {
		boolean retVal = theSetToPopulate.add(theConcept);
		if (retVal) {
			if (theSetToPopulate.size() >= myDaoConfig.getMaximumExpansionSize()) {
				String msg = myContext.getLocalizer().getMessage(BaseHapiTerminologySvcImpl.class, "expansionTooLarge", myDaoConfig.getMaximumExpansionSize());
				throw new InvalidRequestException(msg);
			}
		}
		return retVal;
	}

	@PostConstruct
	public void buildTranslationCaches() {
		myTranslationCache =
			Caffeine.newBuilder()
				.maximumSize(10000)
				.expireAfterWrite(1, TimeUnit.HOURS)
				.build();

		myTranslationWithReverseCache =
			Caffeine.newBuilder()
				.maximumSize(10000)
				.expireAfterWrite(1, TimeUnit.HOURS)
				.build();
	}

	protected abstract IIdType createOrUpdateCodeSystem(CodeSystem theCodeSystemResource);

	protected abstract void createOrUpdateConceptMap(ConceptMap theNextConceptMap);

	abstract void createOrUpdateValueSet(ValueSet theValueSet);

	@Override
	public void deleteCodeSystem(TermCodeSystem theCodeSystem) {
		ourLog.info(" * Deleting code system {}", theCodeSystem.getPid());

		myEntityManager.flush();
		TermCodeSystem cs = myCodeSystemDao.findOne(theCodeSystem.getPid());
		cs.setCurrentVersion(null);
		myCodeSystemDao.save(cs);
		myCodeSystemDao.flush();

		int i = 0;
		for (TermCodeSystemVersion next : myCodeSystemVersionDao.findByCodeSystemResource(theCodeSystem.getPid())) {
			myConceptParentChildLinkDao.deleteByCodeSystemVersion(next.getPid());
			for (TermConcept nextConcept : myConceptDao.findByCodeSystemVersion(next.getPid())) {
				myConceptPropertyDao.delete(nextConcept.getProperties());
				myConceptDesignationDao.delete(nextConcept.getDesignations());
				myConceptDao.delete(nextConcept);
			}
			if (next.getCodeSystem().getCurrentVersion() == next) {
				next.getCodeSystem().setCurrentVersion(null);
				myCodeSystemDao.save(next.getCodeSystem());
			}
			myCodeSystemVersionDao.delete(next);

			if (i++ % 1000 == 0) {
				myEntityManager.flush();
			}
		}
		myCodeSystemVersionDao.deleteForCodeSystem(theCodeSystem);
		myCodeSystemDao.delete(theCodeSystem);

		myEntityManager.flush();
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

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ValueSet expandValueSet(ValueSet theValueSetToExpand) {
		ValueSet.ValueSetExpansionComponent expansionComponent = new ValueSet.ValueSetExpansionComponent();
		Set<String> addedCodes = new HashSet<>();
		boolean haveIncludeCriteria = false;

		for (ValueSet.ConceptSetComponent include : theValueSetToExpand.getCompose().getInclude()) {
			String system = include.getSystem();
			if (isNotBlank(system)) {
				ourLog.info("Starting expansion around code system: {}", system);

				TermCodeSystem cs = myCodeSystemDao.findByCodeSystemUri(system);
				if (cs != null) {
					TermCodeSystemVersion csv = cs.getCurrentVersion();

					/*
					 * Include Concepts
					 */
					for (ValueSet.ConceptReferenceComponent next : include.getConcept()) {
						String nextCode = next.getCode();
						if (isNotBlank(nextCode) && !addedCodes.contains(nextCode)) {
							haveIncludeCriteria = true;
							TermConcept code = findCode(system, nextCode);
							if (code != null) {
								addCodeIfNotAlreadyAdded(system, expansionComponent, addedCodes, code);
							}
						}
					}

					/*
					 * Filters
					 */

					if (include.getFilter().size() > 0) {
						haveIncludeCriteria = true;

						FullTextEntityManager em = org.hibernate.search.jpa.Search.getFullTextEntityManager(myEntityManager);
						QueryBuilder qb = em.getSearchFactory().buildQueryBuilder().forEntity(TermConcept.class).get();
						BooleanJunction<?> bool = qb.bool();

						bool.must(qb.keyword().onField("myCodeSystemVersionPid").matching(csv.getPid()).createQuery());

						for (ValueSet.ConceptSetFilterComponent nextFilter : include.getFilter()) {
							if (isBlank(nextFilter.getValue()) && nextFilter.getOp() == null && isBlank(nextFilter.getProperty())) {
								continue;
							}

							if (isBlank(nextFilter.getValue()) || nextFilter.getOp() == null || isBlank(nextFilter.getProperty())) {
								throw new InvalidRequestException("Invalid filter, must have fields populated: property op value");
							}


							if (nextFilter.getProperty().equals("display:exact") && nextFilter.getOp() == ValueSet.FilterOperator.EQUAL) {
								addDisplayFilterExact(qb, bool, nextFilter);
							} else if ("display".equals(nextFilter.getProperty()) && nextFilter.getOp() == ValueSet.FilterOperator.EQUAL) {
								if (nextFilter.getValue().trim().contains(" ")) {
									addDisplayFilterExact(qb, bool, nextFilter);
								} else {
									addDisplayFilterInexact(qb, bool, nextFilter);
								}
							} else if ((nextFilter.getProperty().equals("concept") || nextFilter.getProperty().equals("code")) && nextFilter.getOp() == ValueSet.FilterOperator.ISA) {

								TermConcept code = findCode(system, nextFilter.getValue());
								if (code == null) {
									throw new InvalidRequestException("Invalid filter criteria - code does not exist: {" + system + "}" + nextFilter.getValue());
								}

								ourLog.info(" * Filtering on codes with a parent of {}/{}/{}", code.getId(), code.getCode(), code.getDisplay());
								bool.must(qb.keyword().onField("myParentPids").matching("" + code.getId()).createQuery());

							} else {

								bool.must(qb.phrase().onField("myProperties").sentence(nextFilter.getProperty() + "=" + nextFilter.getValue()).createQuery());

							}
						}

						Query luceneQuery = bool.createQuery();
						FullTextQuery jpaQuery = em.createFullTextQuery(luceneQuery, TermConcept.class);
						jpaQuery.setMaxResults(1000);

						StopWatch sw = new StopWatch();

						@SuppressWarnings("unchecked")
						List<TermConcept> result = jpaQuery.getResultList();

						ourLog.info("Expansion completed in {}ms", sw.getMillis());

						for (TermConcept nextConcept : result) {
							addCodeIfNotAlreadyAdded(system, expansionComponent, addedCodes, nextConcept);
						}

						expansionComponent.setTotal(jpaQuery.getResultSize());
					}

					if (!haveIncludeCriteria) {
						List<TermConcept> allCodes = findCodes(system);
						for (TermConcept nextConcept : allCodes) {
							addCodeIfNotAlreadyAdded(system, expansionComponent, addedCodes, nextConcept);
						}
					}

				} else {
					// No codesystem matching the URL found in the database

					CodeSystem codeSystemFromContext = getCodeSystemFromContext(system);
					if (codeSystemFromContext == null) {
						throw new InvalidRequestException("Unknown code system: " + system);
					}

					if (include.getConcept().isEmpty() == false) {
						for (ValueSet.ConceptReferenceComponent next : include.getConcept()) {
							String nextCode = next.getCode();
							if (isNotBlank(nextCode) && !addedCodes.contains(nextCode)) {
								CodeSystem.ConceptDefinitionComponent code = findCode(codeSystemFromContext.getConcept(), nextCode);
								if (code != null) {
									addedCodes.add(nextCode);
									ValueSet.ValueSetExpansionContainsComponent contains = expansionComponent.addContains();
									contains.setCode(nextCode);
									contains.setSystem(system);
									contains.setDisplay(code.getDisplay());
								}
							}
						}
					} else {
						List<CodeSystem.ConceptDefinitionComponent> concept = codeSystemFromContext.getConcept();
						addConceptsToList(expansionComponent, addedCodes, system, concept);
					}

				}
			}
		}

		ValueSet valueSet = new ValueSet();
		valueSet.setExpansion(expansionComponent);
		return valueSet;
	}

	protected List<VersionIndependentConcept> expandValueSetAndReturnVersionIndependentConcepts(org.hl7.fhir.r4.model.ValueSet theValueSetToExpandR4) {
		org.hl7.fhir.r4.model.ValueSet.ValueSetExpansionComponent expandedR4 = expandValueSet(theValueSetToExpandR4).getExpansion();

		ArrayList<VersionIndependentConcept> retVal = new ArrayList<>();
		for (org.hl7.fhir.r4.model.ValueSet.ValueSetExpansionContainsComponent nextContains : expandedR4.getContains()) {
			retVal.add(
				new VersionIndependentConcept()
					.setSystem(nextContains.getSystem())
					.setCode(nextContains.getCode()));
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

	private TermConcept fetchLoadedCode(Long theCodeSystemResourcePid, String theCode) {
		TermCodeSystemVersion codeSystem = myCodeSystemVersionDao.findCurrentVersionForCodeSystemResourcePid(theCodeSystemResourcePid);
		return myConceptDao.findByCodeSystemAndCode(codeSystem, theCode);
	}

	private void fetchParents(TermConcept theConcept, Set<TermConcept> theSetToPopulate) {
		for (TermConceptParentChildLink nextChildLink : theConcept.getParents()) {
			TermConcept nextChild = nextChildLink.getParent();
			if (addToSet(theSetToPopulate, nextChild)) {
				fetchParents(nextChild, theSetToPopulate);
			}
		}
	}

	private CodeSystem.ConceptDefinitionComponent findCode(List<CodeSystem.ConceptDefinitionComponent> theConcepts, String theCode) {
		for (CodeSystem.ConceptDefinitionComponent next : theConcepts) {
			if (theCode.equals(next.getCode())) {
				return next;
			}
			findCode(next.getConcept(), theCode);
		}
		return null;
	}

	@Override
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
		StopWatch stopwatch = new StopWatch();

		TermConcept concept = fetchLoadedCode(theCodeSystemResourcePid, theCode);
		if (concept == null) {
			return Collections.emptySet();
		}

		Set<TermConcept> retVal = new HashSet<>();
		retVal.add(concept);

		fetchParents(concept, retVal);

		ourLog.info("Fetched {} codes above code {} in {}ms", retVal.size(), theCode, stopwatch.getMillis());
		return retVal;
	}

	@Override
	public List<VersionIndependentConcept> findCodesAbove(String theSystem, String theCode) {
		TermCodeSystem cs = getCodeSystem(theSystem);
		if (cs == null) {
			return findCodesAboveUsingBuiltInSystems(theSystem, theCode);
		}
		TermCodeSystemVersion csv = cs.getCurrentVersion();

		Set<TermConcept> codes = findCodesAbove(cs.getResource().getId(), csv.getPid(), theCode);
		return toVersionIndependentConcepts(theSystem, codes);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public Set<TermConcept> findCodesBelow(Long theCodeSystemResourcePid, Long theCodeSystemVersionPid, String theCode) {
		Stopwatch stopwatch = Stopwatch.createStarted();

		TermConcept concept = fetchLoadedCode(theCodeSystemResourcePid, theCode);
		if (concept == null) {
			return Collections.emptySet();
		}

		Set<TermConcept> retVal = new HashSet<>();
		retVal.add(concept);

		fetchChildren(concept, retVal);

		ourLog.info("Fetched {} codes below code {} in {}ms", retVal.size(), theCode, stopwatch.elapsed(TimeUnit.MILLISECONDS));
		return retVal;
	}

	@Override
	public List<VersionIndependentConcept> findCodesBelow(String theSystem, String theCode) {
		TermCodeSystem cs = getCodeSystem(theSystem);
		if (cs == null) {
			return findCodesBelowUsingBuiltInSystems(theSystem, theCode);
		}
		TermCodeSystemVersion csv = cs.getCurrentVersion();

		Set<TermConcept> codes = findCodesBelow(cs.getResource().getId(), csv.getPid(), theCode);
		return toVersionIndependentConcepts(theSystem, codes);
	}

	private TermCodeSystemVersion findCurrentCodeSystemVersionForSystem(String theCodeSystem) {
		TermCodeSystem cs = getCodeSystem(theCodeSystem);
		if (cs == null || cs.getCurrentVersion() == null) {
			return null;
		}
		return cs.getCurrentVersion();
	}

	private TermCodeSystem getCodeSystem(String theSystem) {
		return myCodeSystemDao.findByCodeSystemUri(theSystem);
	}

	protected abstract CodeSystem getCodeSystemFromContext(String theSystem);

	private void persistChildren(TermConcept theConcept, TermCodeSystemVersion theCodeSystem, IdentityHashMap<TermConcept, Object> theConceptsStack, int theTotalConcepts) {
		if (theConceptsStack.put(theConcept, PLACEHOLDER_OBJECT) != null) {
			return;
		}

		if (theConceptsStack.size() == 1 || theConceptsStack.size() % 10000 == 0) {
			float pct = (float) theConceptsStack.size() / (float) theTotalConcepts;
			ourLog.info("Have processed {}/{} concepts ({}%)", theConceptsStack.size(), theTotalConcepts, (int) (pct * 100.0f));
		}

		theConcept.setCodeSystemVersion(theCodeSystem);
		theConcept.setIndexStatus(BaseHapiFhirDao.INDEX_STATUS_INDEXED);

		if (theConceptsStack.size() <= myDaoConfig.getDeferIndexingForCodesystemsOfSize()) {
			saveConcept(theConcept);
		} else {
			myDeferredConcepts.add(theConcept);
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
		if (theNext.getCodeSystemVersion() != null) {
			return;
		}
		theNext.setCodeSystemVersion(theCodeSystemVersion);
		for (TermConceptParentChildLink next : theNext.getChildren()) {
			populateVersion(next.getChild(), theCodeSystemVersion);
		}
	}

	private void processDeferredConceptMaps() {
		int count = Math.min(myDeferredConceptMaps.size(), 20);
		for (ConceptMap nextConceptMap : new ArrayList<>(myDeferredConceptMaps.subList(0, count))) {
			ourLog.info("Creating ConceptMap: {}", nextConceptMap.getId());
			createOrUpdateConceptMap(nextConceptMap);
			myDeferredConceptMaps.remove(nextConceptMap);
		}
		ourLog.info("Saved {} deferred ConceptMap resources, have {} remaining", count, myDeferredConceptMaps.size());
	}

	private void processDeferredConcepts() {
		int codeCount = 0, relCount = 0;
		StopWatch stopwatch = new StopWatch();

		int count = Math.min(myDaoConfig.getDeferIndexingForCodesystemsOfSize(), myDeferredConcepts.size());
		ourLog.info("Saving {} deferred concepts...", count);
		while (codeCount < count && myDeferredConcepts.size() > 0) {
			TermConcept next = myDeferredConcepts.remove(0);
			codeCount += saveConcept(next);
		}

		if (codeCount > 0) {
			ourLog.info("Saved {} deferred concepts ({} codes remain and {} relationships remain) in {}ms ({}ms / code)",
				codeCount, myDeferredConcepts.size(), myConceptLinksToSaveLater.size(), stopwatch.getMillis(), stopwatch.getMillisPerOperation(codeCount));
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
			ourLog.info("Saved {} deferred relationships ({} remain) in {}ms ({}ms / code)",
				relCount, myConceptLinksToSaveLater.size(), stopwatch.getMillis(), stopwatch.getMillisPerOperation(codeCount));
		}

		if ((myDeferredConcepts.size() + myConceptLinksToSaveLater.size()) == 0) {
			ourLog.info("All deferred concepts and relationships have now been synchronized to the database");
		}
	}

	private void processDeferredValueSets() {
		int count = Math.min(myDeferredValueSets.size(), 20);
		for (ValueSet nextValueSet : new ArrayList<>(myDeferredValueSets.subList(0, count))) {
			ourLog.info("Creating ValueSet: {}", nextValueSet.getId());
			createOrUpdateValueSet(nextValueSet);
			myDeferredValueSets.remove(nextValueSet);
		}
		ourLog.info("Saved {} deferred ValueSet resources, have {} remaining", count, myDeferredValueSets.size());
	}

	private void processReindexing() {
		if (System.currentTimeMillis() < myNextReindexPass && !ourForceSaveDeferredAlwaysForUnitTest) {
			return;
		}

		TransactionTemplate tt = new TransactionTemplate(myTransactionMgr);
		tt.setPropagationBehavior(TransactionTemplate.PROPAGATION_REQUIRES_NEW);
		tt.execute(new TransactionCallbackWithoutResult() {
			private void createParentsString(StringBuilder theParentsBuilder, Long theConceptPid) {
				Validate.notNull(theConceptPid, "theConceptPid must not be null");
				List<Long> parents = myChildToParentPidCache.get(theConceptPid);
				if (parents.contains(-1L)) {
					return;
				} else if (parents.isEmpty()) {
					Collection<Long> parentLinks = myConceptParentChildLinkDao.findAllWithChild(theConceptPid);
					if (parentLinks.isEmpty()) {
						myChildToParentPidCache.put(theConceptPid, -1L);
						ourLog.info("Found {} parent concepts of concept {} (cache has {})", 0, theConceptPid, myChildToParentPidCache.size());
						return;
					} else {
						for (Long next : parentLinks) {
							myChildToParentPidCache.put(theConceptPid, next);
						}
						int parentCount = myChildToParentPidCache.get(theConceptPid).size();
						ourLog.info("Found {} parent concepts of concept {} (cache has {})", parentCount, theConceptPid, myChildToParentPidCache.size());
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

			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theArg0) {
				int maxResult = 1000;
				Page<TermConcept> concepts = myConceptDao.findResourcesRequiringReindexing(new PageRequest(0, maxResult));
				if (concepts.hasContent() == false) {
					if (myChildToParentPidCache != null) {
						ourLog.info("Clearing parent concept cache");
						myNextReindexPass = System.currentTimeMillis() + DateUtils.MILLIS_PER_MINUTE;
						myChildToParentPidCache = null;
					}
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

				ourLog.info("Indexed {} / {} concepts in {}ms - Avg {}ms / resource", count, concepts.getContent().size(), stopwatch.getMillis(), stopwatch.getMillisPerOperation(count));
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

	private void saveConceptLink(TermConceptParentChildLink next) {
		if (next.getId() == null) {
			myConceptParentChildLinkDao.save(next);
		}
	}

	@Scheduled(fixedRate = 5000)
	@Transactional(propagation = Propagation.NEVER)
	@Override
	public synchronized void saveDeferred() {
		if (!myProcessDeferred) {
			return;
		} else if (myDeferredConcepts.isEmpty() && myConceptLinksToSaveLater.isEmpty()) {
			processReindexing();
			return;
		}

		TransactionTemplate tt = new TransactionTemplate(myTransactionMgr);
		tt.setPropagationBehavior(TransactionTemplate.PROPAGATION_REQUIRES_NEW);
		tt.execute(t -> {
			processDeferredConcepts();
			return null;
		});

		if (myDeferredValueSets.size() > 0) {
			tt.execute(t -> {
				processDeferredValueSets();
				return null;
			});
		}
		if (myDeferredConceptMaps.size() > 0) {
			tt.execute(t -> {
				processDeferredConceptMaps();
				return null;
			});
		}

	}

	@Override
	public void setProcessDeferred(boolean theProcessDeferred) {
		myProcessDeferred = theProcessDeferred;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void storeNewCodeSystemVersion(Long theCodeSystemResourcePid, String theSystemUri, String theSystemName, TermCodeSystemVersion theCodeSystemVersion) {
		ourLog.info("Storing code system");

		ValidateUtil.isTrueOrThrowInvalidRequest(theCodeSystemVersion.getResource() != null, "No resource supplied");
		ValidateUtil.isNotBlankOrThrowInvalidRequest(theSystemUri, "No system URI supplied");

		// Grab the existing versions so we can delete them later
		List<TermCodeSystemVersion> existing = myCodeSystemVersionDao.findByCodeSystemResource(theCodeSystemResourcePid);

//		verifyNoDuplicates(theCodeSystemVersion.getConcepts(), new HashSet<String>());

		/*
		 * For now we always delete old versions.. At some point it would be nice to allow configuration to keep old versions
		 */

		ourLog.info("Deleting old code system versions");
		for (TermCodeSystemVersion next : existing) {
			ourLog.info(" * Deleting code system version {}", next.getPid());
			myConceptParentChildLinkDao.deleteByCodeSystemVersion(next.getPid());
			for (TermConcept nextConcept : myConceptDao.findByCodeSystemVersion(next.getPid())) {
				myConceptPropertyDao.delete(nextConcept.getProperties());
				myConceptDao.delete(nextConcept);
			}
		}

		ourLog.info("Flushing...");

		myConceptParentChildLinkDao.flush();
		myConceptPropertyDao.flush();
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
			codeSystem.setName(theSystemName);
			myCodeSystemDao.save(codeSystem);
		} else {
			if (!ObjectUtil.equals(codeSystem.getResource().getId(), theCodeSystemVersion.getResource().getId())) {
				String msg = myContext.getLocalizer().getMessage(BaseHapiTerminologySvcImpl.class, "cannotCreateDuplicateCodeSystemUri", theSystemUri,
					codeSystem.getResource().getIdDt().toUnqualifiedVersionless().getValue());
				throw new UnprocessableEntityException(msg);
			}
		}
		theCodeSystemVersion.setCodeSystem(codeSystem);

		ourLog.info("Validating all codes in CodeSystem for storage (this can take some time for large sets)");

		// Validate the code system
		ArrayList<String> conceptsStack = new ArrayList<>();
		IdentityHashMap<TermConcept, Object> allConcepts = new IdentityHashMap<>();
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

		if (myDeferredConcepts.size() > 0 || myConceptLinksToSaveLater.size() > 0) {
			ourLog.info("Note that some concept saving was deferred - still have {} concepts and {} relationships", myDeferredConcepts.size(), myConceptLinksToSaveLater.size());
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void storeNewCodeSystemVersion(CodeSystem theCodeSystemResource, TermCodeSystemVersion theCodeSystemVersion, RequestDetails theRequestDetails, List<ValueSet> theValueSets, List<ConceptMap> theConceptMaps) {
		Validate.notBlank(theCodeSystemResource.getUrl(), "theCodeSystemResource must have a URL");

		IIdType csId = createOrUpdateCodeSystem(theCodeSystemResource);

		ResourceTable resource = (ResourceTable) myCodeSystemResourceDao.readEntity(csId);
		Long codeSystemResourcePid = resource.getId();

		ourLog.info("CodeSystem resource has ID: {}", csId.getValue());

		theCodeSystemVersion.setResource(resource);
		storeNewCodeSystemVersion(codeSystemResourcePid, theCodeSystemResource.getUrl(), theCodeSystemResource.getName(), theCodeSystemVersion);

		myDeferredConceptMaps.addAll(theConceptMaps);
		myDeferredValueSets.addAll(theValueSets);
	}

	@Override
	@Transactional
	public void storeTermConceptMapAndChildren(ResourceTable theResourceTable, ConceptMap theConceptMap) {
		ourLog.info("Storing TermConceptMap...");

		ValidateUtil.isTrueOrThrowInvalidRequest(theResourceTable != null, "No resource supplied.");
		ValidateUtil.isNotBlankOrThrowInvalidRequest(theConceptMap.getUrl(), "No URL supplied.");

		TermConceptMap termConceptMap = new TermConceptMap();
		termConceptMap.setResource(theResourceTable);
		termConceptMap.setUrl(theConceptMap.getUrl());

		// Get existing entity so it can be deleted.
		Optional<TermConceptMap> optionalExistingTermConceptMapById = myConceptMapDao.findTermConceptMapByResourcePid(termConceptMap.getResourcePid());

		/*
		 * For now we always delete old versions. At some point, it would be nice to allow configuration to keep old versions.
		 */

		if (optionalExistingTermConceptMapById.isPresent()) {
			Long id = optionalExistingTermConceptMapById.get().getId();
			ourLog.info("Deleting existing TermConceptMap {} and its children...", id);
			myConceptMapGroupElementTargetDao.deleteTermConceptMapGroupElementTargetById(id);
			myConceptMapGroupElementDao.deleteTermConceptMapGroupElementById(id);
			myConceptMapGroupDao.deleteTermConceptMapGroupById(id);
			myConceptMapDao.deleteTermConceptMapById(id);
			ourLog.info("Done deleting existing TermConceptMap {} and its children.", id);

			ourLog.info("Flushing...");
			myConceptMapGroupElementTargetDao.flush();
			myConceptMapGroupElementDao.flush();
			myConceptMapGroupDao.flush();
			myConceptMapDao.flush();
			ourLog.info("Done flushing.");
		}

		/*
		 * Do the upload.
		 */
		String conceptMapUrl = termConceptMap.getUrl();
		Optional<TermConceptMap> optionalExistingTermConceptMapByUrl = myConceptMapDao.findTermConceptMapByUrl(conceptMapUrl);
		if (!optionalExistingTermConceptMapByUrl.isPresent()) {
			try {
				String source = theConceptMap.getSourceUriType().getValueAsString();
				if (isNotBlank(source)) {
					termConceptMap.setSource(source);
				}
				String target = theConceptMap.getTargetUriType().getValueAsString();
				if (isNotBlank(target)) {
					termConceptMap.setTarget(target);
				}
			} catch (FHIRException fe) {
				throw new InternalErrorException(fe);
			}
			myConceptMapDao.save(termConceptMap);

			if (theConceptMap.hasGroup()) {
				TermConceptMapGroup termConceptMapGroup;
				for (ConceptMap.ConceptMapGroupComponent group : theConceptMap.getGroup()) {
					termConceptMapGroup = new TermConceptMapGroup();
					termConceptMapGroup.setConceptMap(termConceptMap);
					termConceptMapGroup.setSource(group.getSource());
					termConceptMapGroup.setSourceVersion(group.getSourceVersion());
					termConceptMapGroup.setTarget(group.getTarget());
					termConceptMapGroup.setTargetVersion(group.getTargetVersion());
					myConceptMapGroupDao.save(termConceptMapGroup);

					if (group.hasElement()) {
						TermConceptMapGroupElement termConceptMapGroupElement;
						for (ConceptMap.SourceElementComponent element : group.getElement()) {
							termConceptMapGroupElement = new TermConceptMapGroupElement();
							termConceptMapGroupElement.setConceptMapGroup(termConceptMapGroup);
							termConceptMapGroupElement.setCode(element.getCode());
							termConceptMapGroupElement.setDisplay(element.getDisplay());
							myConceptMapGroupElementDao.save(termConceptMapGroupElement);

							if (element.hasTarget()) {
								TermConceptMapGroupElementTarget termConceptMapGroupElementTarget;
								for (ConceptMap.TargetElementComponent target : element.getTarget()) {
									termConceptMapGroupElementTarget = new TermConceptMapGroupElementTarget();
									termConceptMapGroupElementTarget.setConceptMapGroupElement(termConceptMapGroupElement);
									termConceptMapGroupElementTarget.setCode(target.getCode());
									termConceptMapGroupElementTarget.setDisplay(target.getDisplay());
									termConceptMapGroupElementTarget.setEquivalence(target.getEquivalence());
									myConceptMapGroupElementTargetDao.saveAndFlush(termConceptMapGroupElementTarget);
								}
							}
						}
					}
				}
			}
		} else {
			TermConceptMap existingTermConceptMap = optionalExistingTermConceptMapByUrl.get();

			String msg = myContext.getLocalizer().getMessage(
				BaseHapiTerminologySvcImpl.class,
				"cannotCreateDuplicateConceptMapUrl",
				conceptMapUrl,
				existingTermConceptMap.getResourcePid());

			throw new UnprocessableEntityException(msg);
		}

		ourLog.info("Done storing TermConceptMap.");
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

	@Override
	public List<TermConceptMapGroupElementTarget> translate(TranslationRequest theTranslationRequest) {
		List<TermConceptMapGroupElementTarget> retVal = new ArrayList<>();

		CriteriaBuilder criteriaBuilder = myEntityManager.getCriteriaBuilder();
		CriteriaQuery<TermConceptMapGroupElementTarget> query = criteriaBuilder.createQuery(TermConceptMapGroupElementTarget.class);
		Root<TermConceptMapGroupElementTarget> root = query.from(TermConceptMapGroupElementTarget.class);

		Join<TermConceptMapGroupElementTarget, TermConceptMapGroupElement> elementJoin = root.join("myConceptMapGroupElement");
		Join<TermConceptMapGroupElement, TermConceptMapGroup> groupJoin = elementJoin.join("myConceptMapGroup");
		Join<TermConceptMapGroup, TermConceptMap> conceptMapJoin = groupJoin.join("myConceptMap");

		List<TranslationQuery> translationQueries = theTranslationRequest.getTranslationQueries();
		List<TermConceptMapGroupElementTarget> cachedTargets;
		ArrayList<Predicate> predicates;
		Coding coding;
		for (TranslationQuery translationQuery : translationQueries) {
			cachedTargets = myTranslationCache.getIfPresent(translationQuery);
			if (cachedTargets == null) {
				final List<TermConceptMapGroupElementTarget> targets = new ArrayList<>();

				predicates = new ArrayList<>();

				coding = translationQuery.getCoding();
				if (coding.hasCode()) {
					predicates.add(criteriaBuilder.equal(elementJoin.get("myCode"), coding.getCode()));
				} else {
					throw new InvalidRequestException("A code must be provided for translation to occur.");
				}

				if (coding.hasSystem()) {
					predicates.add(criteriaBuilder.equal(groupJoin.get("mySource"), coding.getSystem()));
				}

				if (coding.hasVersion()) {
					predicates.add(criteriaBuilder.equal(groupJoin.get("mySourceVersion"), coding.getVersion()));
				}

				if (translationQuery.hasTargetSystem()) {
					predicates.add(criteriaBuilder.equal(groupJoin.get("myTarget"), translationQuery.getTargetSystem().getValueAsString()));
				}

				if (translationQuery.hasSource()) {
					predicates.add(criteriaBuilder.equal(conceptMapJoin.get("mySource"), translationQuery.getSource().getValueAsString()));
				}

				if (translationQuery.hasTarget()) {
					predicates.add(criteriaBuilder.equal(conceptMapJoin.get("myTarget"), translationQuery.getTarget().getValueAsString()));
				}

				if (translationQuery.hasResourceId()) {
					predicates.add(criteriaBuilder.equal(conceptMapJoin.get("myResourcePid"), translationQuery.getResourceId()));
				}

				Predicate outerPredicate = criteriaBuilder.and(predicates.toArray(new Predicate[0]));
				query.where(outerPredicate);

				// Use scrollable results.
				final TypedQuery<TermConceptMapGroupElementTarget> typedQuery = myEntityManager.createQuery(query.select(root));
				org.hibernate.query.Query<TermConceptMapGroupElementTarget> hibernateQuery = (org.hibernate.query.Query<TermConceptMapGroupElementTarget>) typedQuery;
				hibernateQuery.setFetchSize(myFetchSize);
				ScrollableResults scrollableResults = hibernateQuery.scroll(ScrollMode.FORWARD_ONLY);
				Iterator<TermConceptMapGroupElementTarget> scrollableResultsIterator = new ScrollableResultsIterator<>(scrollableResults);

				while (scrollableResultsIterator.hasNext()) {
					targets.add(scrollableResultsIterator.next());
				}

				ourLastResultsFromTranslationCache = false; // For testing.
				myTranslationCache.get(translationQuery, k -> targets);
				retVal.addAll(targets);
			} else {
				ourLastResultsFromTranslationCache = true; // For testing.
				retVal.addAll(cachedTargets);
			}
		}

		return retVal;
	}

	@Override
	public List<TermConceptMapGroupElement> translateWithReverse(TranslationRequest theTranslationRequest) {
		List<TermConceptMapGroupElement> retVal = new ArrayList<>();

		CriteriaBuilder criteriaBuilder = myEntityManager.getCriteriaBuilder();
		CriteriaQuery<TermConceptMapGroupElement> query = criteriaBuilder.createQuery(TermConceptMapGroupElement.class);
		Root<TermConceptMapGroupElement> root = query.from(TermConceptMapGroupElement.class);

		Join<TermConceptMapGroupElement, TermConceptMapGroupElementTarget> targetJoin = root.join("myConceptMapGroupElementTargets");
		Join<TermConceptMapGroupElement, TermConceptMapGroup> groupJoin = root.join("myConceptMapGroup");
		Join<TermConceptMapGroup, TermConceptMap> conceptMapJoin = groupJoin.join("myConceptMap");

		List<TranslationQuery> translationQueries = theTranslationRequest.getTranslationQueries();
		List<TermConceptMapGroupElement> cachedElements;
		ArrayList<Predicate> predicates;
		Coding coding;
		for (TranslationQuery translationQuery : translationQueries) {
			cachedElements = myTranslationWithReverseCache.getIfPresent(translationQuery);
			if (cachedElements == null) {
				final List<TermConceptMapGroupElement> elements = new ArrayList<>();

				predicates = new ArrayList<>();

				coding = translationQuery.getCoding();
				if (coding.hasCode()) {
					predicates.add(criteriaBuilder.equal(targetJoin.get("myCode"), coding.getCode()));
				} else {
					throw new InvalidRequestException("A code must be provided for translation to occur.");
				}

				if (coding.hasSystem()) {
					predicates.add(criteriaBuilder.equal(groupJoin.get("myTarget"), coding.getSystem()));
				}

				if (coding.hasVersion()) {
					predicates.add(criteriaBuilder.equal(groupJoin.get("myTargetVersion"), coding.getVersion()));
				}

				if (translationQuery.hasTargetSystem()) {
					predicates.add(criteriaBuilder.equal(groupJoin.get("mySource"), translationQuery.getTargetSystem().getValueAsString()));
				}

				if (translationQuery.hasSource()) {
					predicates.add(criteriaBuilder.equal(conceptMapJoin.get("myTarget"), translationQuery.getSource().getValueAsString()));
				}

				if (translationQuery.hasTarget()) {
					predicates.add(criteriaBuilder.equal(conceptMapJoin.get("mySource"), translationQuery.getTarget().getValueAsString()));
				}

				if (translationQuery.hasResourceId()) {
					predicates.add(criteriaBuilder.equal(conceptMapJoin.get("myResourcePid"), translationQuery.getResourceId()));
				}

				Predicate outerPredicate = criteriaBuilder.and(predicates.toArray(new Predicate[0]));
				query.where(outerPredicate);

				// Use scrollable results.
				final TypedQuery<TermConceptMapGroupElement> typedQuery = myEntityManager.createQuery(query.select(root));
				org.hibernate.query.Query<TermConceptMapGroupElement> hibernateQuery = (org.hibernate.query.Query<TermConceptMapGroupElement>) typedQuery;
				hibernateQuery.setFetchSize(myFetchSize);
				ScrollableResults scrollableResults = hibernateQuery.scroll(ScrollMode.FORWARD_ONLY);
				Iterator<TermConceptMapGroupElement> scrollableResultsIterator = new ScrollableResultsIterator<>(scrollableResults);

				while (scrollableResultsIterator.hasNext()) {
					elements.add(scrollableResultsIterator.next());
				}

				ourLastResultsFromTranslationWithReverseCache = false; // For testing.
				myTranslationWithReverseCache.get(translationQuery, k -> elements);
				retVal.addAll(elements);
			} else {
				ourLastResultsFromTranslationWithReverseCache = true; // For testing.
				retVal.addAll(cachedElements);
			}
		}

		return retVal;
	}

	private int validateConceptForStorage(TermConcept theConcept, TermCodeSystemVersion theCodeSystem, ArrayList<String> theConceptsStack,
													  IdentityHashMap<TermConcept, Object> theAllConcepts) {
		ValidateUtil.isTrueOrThrowInvalidRequest(theConcept.getCodeSystemVersion() != null, "CodesystemValue is null");
		ValidateUtil.isTrueOrThrowInvalidRequest(theConcept.getCodeSystemVersion() == theCodeSystem, "CodeSystems are not equal");
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

	private void verifyNoDuplicates(Collection<TermConcept> theConcepts, Set<String> theCodes) {
		for (TermConcept next : theConcepts) {
			if (!theCodes.add(next.getCode())) {
				throw new InvalidRequestException("Duplicate code " + next.getCode() + " found in codesystem after checking " + theCodes.size() + " codes");
			}
			verifyNoDuplicates(next.getChildren().stream().map(TermConceptParentChildLink::getChild).collect(Collectors.toList()), theCodes);
		}
	}

	/**
	 * This method is present only for unit tests, do not call from client code
	 */
	@VisibleForTesting
	static void clearOurLastResultsFromTranslationCache() {
		ourLastResultsFromTranslationCache = false;
	}

	/**
	 * This method is present only for unit tests, do not call from client code
	 */
	@VisibleForTesting
	static void clearOurLastResultsFromTranslationWithReverseCache() {
		ourLastResultsFromTranslationWithReverseCache = false;
	}

	/**
	 * This method is present only for unit tests, do not call from client code
	 */
	@VisibleForTesting
	void clearTranslationCache() {
		myTranslationCache.invalidateAll();
	}

	/**
	 * This method is present only for unit tests, do not call from client code
	 */
	@VisibleForTesting()
	void clearTranslationWithReverseCache() {
		myTranslationWithReverseCache.invalidateAll();
	}

	/**
	 * This method is present only for unit tests, do not call from client code
	 */
	@VisibleForTesting
	static boolean isOurLastResultsFromTranslationCache() {
		return ourLastResultsFromTranslationCache;
	}

	/**
	 * This method is present only for unit tests, do not call from client code
	 */
	@VisibleForTesting
	static boolean isOurLastResultsFromTranslationWithReverseCache() {
		return ourLastResultsFromTranslationWithReverseCache;
	}

	/**
	 * This method is present only for unit tests, do not call from client code
	 */
	@VisibleForTesting
	public static void setForceSaveDeferredAlwaysForUnitTest(boolean theForceSaveDeferredAlwaysForUnitTest) {
		ourForceSaveDeferredAlwaysForUnitTest = theForceSaveDeferredAlwaysForUnitTest;
	}
}
