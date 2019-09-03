package ca.uhn.fhir.jpa.term;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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
import ca.uhn.fhir.context.support.IContextValidationSupport;
import ca.uhn.fhir.jpa.dao.*;
import ca.uhn.fhir.jpa.dao.IFhirResourceDaoValueSet.ValidateCodeResult;
import ca.uhn.fhir.jpa.dao.data.*;
import ca.uhn.fhir.jpa.entity.*;
import ca.uhn.fhir.jpa.entity.TermConceptParentChildLink.RelationshipTypeEnum;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
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
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.lucene.index.Term;
import org.apache.lucene.queries.TermsQuery;
import org.apache.lucene.search.*;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.query.dsl.BooleanJunction;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.*;
import org.hl7.fhir.r4.model.codesystems.ConceptSubsumptionOutcome;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.*;

public abstract class BaseHapiTerminologySvcImpl implements IHapiTerminologySvc, ApplicationContextAware {
	public static final int DEFAULT_FETCH_SIZE = 250;

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseHapiTerminologySvcImpl.class);
	private static final Object PLACEHOLDER_OBJECT = new Object();
	private static boolean ourForceSaveDeferredAlwaysForUnitTest;
	private static boolean ourLastResultsFromTranslationCache; // For testing.
	private static boolean ourLastResultsFromTranslationWithReverseCache; // For testing.
	@Autowired
	protected DaoRegistry myDaoRegistry;
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
	protected ITermValueSetDao myValueSetDao;
	@Autowired
	protected ITermValueSetConceptDao myValueSetConceptDao;
	@Autowired
	protected ITermValueSetConceptDesignationDao myValueSetConceptDesignationDao;
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
	private IFhirResourceDaoCodeSystem<?, ?, ?> myCodeSystemResourceDao;
	private IFhirResourceDaoValueSet<?, ?, ?> myValueSetResourceDao;
	private Cache<TranslationQuery, List<TermConceptMapGroupElementTarget>> myTranslationCache;
	private Cache<TranslationQuery, List<TermConceptMapGroupElement>> myTranslationWithReverseCache;
	private int myFetchSize = DEFAULT_FETCH_SIZE;
	private ApplicationContext myApplicationContext;
	private TransactionTemplate myTxTemplate;
	@Autowired
	private PlatformTransactionManager myTransactionManager;
	@Autowired(required = false)
	private IFulltextSearchSvc myFulltextSearchSvc;
	@Autowired
	private PlatformTransactionManager myTxManager;

	private void addCodeIfNotAlreadyAdded(IValueSetConceptAccumulator theValueSetCodeAccumulator, Set<String> theAddedCodes, TermConcept theConcept, boolean theAdd, AtomicInteger theCodeCounter) {
		String codeSystem = theConcept.getCodeSystemVersion().getCodeSystem().getCodeSystemUri();
		String code = theConcept.getCode();
		String display = theConcept.getDisplay();
		Collection<TermConceptDesignation> designations = theConcept.getDesignations();
		addCodeIfNotAlreadyAdded(theValueSetCodeAccumulator, theAddedCodes, designations, theAdd, theCodeCounter, codeSystem, code, display);
	}

	private void addCodeIfNotAlreadyAdded(IValueSetConceptAccumulator theValueSetCodeAccumulator, Set<String> theAddedCodes, Collection<TermConceptDesignation> theDesignations, boolean theAdd, AtomicInteger theCodeCounter, String theCodeSystem, String theCode, String theDisplay) {
		if (isNoneBlank(theCodeSystem, theCode)) {
			if (theAdd && theAddedCodes.add(theCodeSystem + "|" + theCode)) {
				theValueSetCodeAccumulator.includeConceptWithDesignations(theCodeSystem, theCode, theDisplay, theDesignations);
				theCodeCounter.incrementAndGet();
			}

			if (!theAdd && theAddedCodes.remove(theCodeSystem + "|" + theCode)) {
				theValueSetCodeAccumulator.excludeConcept(theCodeSystem, theCode);
				theCodeCounter.decrementAndGet();
			}
		}
	}

	private void addConceptsToList(IValueSetConceptAccumulator theValueSetCodeAccumulator, Set<String> theAddedCodes, String theSystem, List<CodeSystem.ConceptDefinitionComponent> theConcept, boolean theAdd) {
		for (CodeSystem.ConceptDefinitionComponent next : theConcept) {
			if (isNoneBlank(theSystem, next.getCode())) {
				if (theAdd && theAddedCodes.add(theSystem + "|" + next.getCode())) {
					theValueSetCodeAccumulator.includeConcept(theSystem, next.getCode(), next.getDisplay());
				}
				if (!theAdd && theAddedCodes.remove(theSystem + "|" + next.getCode())) {
					theValueSetCodeAccumulator.excludeConcept(theSystem, next.getCode());
				}
			}
			addConceptsToList(theValueSetCodeAccumulator, theAddedCodes, theSystem, next.getConcept(), theAdd);
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
		Long timeout = myDaoConfig.getTranslationCachesExpireAfterWriteInMinutes();

		myTranslationCache =
			Caffeine.newBuilder()
				.maximumSize(10000)
				.expireAfterWrite(timeout, TimeUnit.MINUTES)
				.build();

		myTranslationWithReverseCache =
			Caffeine.newBuilder()
				.maximumSize(10000)
				.expireAfterWrite(timeout, TimeUnit.MINUTES)
				.build();
	}

	/**
	 * This method is present only for unit tests, do not call from client code
	 */
	@VisibleForTesting
	public void clearDeferred() {
		myDeferredValueSets.clear();
		myDeferredConceptMaps.clear();
		myDeferredConcepts.clear();
	}

	/**
	 * This method is present only for unit tests, do not call from client code
	 */
	@VisibleForTesting
	public void clearTranslationCache() {
		myTranslationCache.invalidateAll();
	}

	/**
	 * This method is present only for unit tests, do not call from client code
	 */
	@VisibleForTesting()
	public void clearTranslationWithReverseCache() {
		myTranslationWithReverseCache.invalidateAll();
	}

	protected abstract IIdType createOrUpdateCodeSystem(CodeSystem theCodeSystemResource);

	protected void validateCodeSystemForStorage(CodeSystem theCodeSystemResource) {
		ValidateUtil.isNotBlankOrThrowUnprocessableEntity(theCodeSystemResource.getUrl(), "Can not store a CodeSystem without a valid URL");
	}

	protected abstract void createOrUpdateConceptMap(ConceptMap theNextConceptMap);

	abstract void createOrUpdateValueSet(ValueSet theValueSet);

	@Override
	public void deleteCodeSystem(TermCodeSystem theCodeSystem) {
		ourLog.info(" * Deleting code system {}", theCodeSystem.getPid());

		myEntityManager.flush();
		TermCodeSystem cs = myCodeSystemDao.findById(theCodeSystem.getPid()).orElseThrow(IllegalStateException::new);
		cs.setCurrentVersion(null);
		myCodeSystemDao.save(cs);
		myCodeSystemDao.flush();

		int i = 0;
		for (TermCodeSystemVersion next : myCodeSystemVersionDao.findByCodeSystemResource(theCodeSystem.getPid())) {
			deleteCodeSystemVersion(next.getPid());
		}
		myCodeSystemVersionDao.deleteForCodeSystem(theCodeSystem);
		myCodeSystemDao.delete(theCodeSystem);

		myEntityManager.flush();
	}

	public void deleteCodeSystemVersion(final Long theCodeSystemVersionPid) {
		ourLog.info(" * Deleting code system version {}", theCodeSystemVersionPid);

		PageRequest page1000 = PageRequest.of(0, 1000);

		// Parent/Child links
		{
			String descriptor = "parent/child links";
			Supplier<Slice<TermConceptParentChildLink>> loader = () -> myConceptParentChildLinkDao.findByCodeSystemVersion(page1000, theCodeSystemVersionPid);
			Supplier<Integer> counter = () -> myConceptParentChildLinkDao.countByCodeSystemVersion(theCodeSystemVersionPid);
			doDelete(descriptor, loader, counter, myConceptParentChildLinkDao);
		}

		// Properties
		{
			String descriptor = "concept properties";
			Supplier<Slice<TermConceptProperty>> loader = () -> myConceptPropertyDao.findByCodeSystemVersion(page1000, theCodeSystemVersionPid);
			Supplier<Integer> counter = () -> myConceptPropertyDao.countByCodeSystemVersion(theCodeSystemVersionPid);
			doDelete(descriptor, loader, counter, myConceptPropertyDao);
		}

		// Designations
		{
			String descriptor = "concept designations";
			Supplier<Slice<TermConceptDesignation>> loader = () -> myConceptDesignationDao.findByCodeSystemVersion(page1000, theCodeSystemVersionPid);
			Supplier<Integer> counter = () -> myConceptDesignationDao.countByCodeSystemVersion(theCodeSystemVersionPid);
			doDelete(descriptor, loader, counter, myConceptDesignationDao);
		}

		// Concepts
		{
			String descriptor = "concepts";
			// For some reason, concepts are much slower to delete, so use a smaller batch size
			PageRequest page100 = PageRequest.of(0, 100);
			Supplier<Slice<TermConcept>> loader = () -> myConceptDao.findByCodeSystemVersion(page100, theCodeSystemVersionPid);
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

	public void deleteConceptMap(ResourceTable theResourceTable) {
		// Get existing entity so it can be deleted.
		Optional<TermConceptMap> optionalExistingTermConceptMapById = myConceptMapDao.findTermConceptMapByResourcePid(theResourceTable.getId());

		if (optionalExistingTermConceptMapById.isPresent()) {
			TermConceptMap existingTermConceptMap = optionalExistingTermConceptMapById.get();

			ourLog.info("Deleting existing TermConceptMap[{}] and its children...", existingTermConceptMap.getId());
			for (TermConceptMapGroup group : existingTermConceptMap.getConceptMapGroups()) {

				for (TermConceptMapGroupElement element : group.getConceptMapGroupElements()) {

					for (TermConceptMapGroupElementTarget target : element.getConceptMapGroupElementTargets()) {

						myConceptMapGroupElementTargetDao.deleteTermConceptMapGroupElementTargetById(target.getId());
					}

					myConceptMapGroupElementDao.deleteTermConceptMapGroupElementById(element.getId());
				}

				myConceptMapGroupDao.deleteTermConceptMapGroupById(group.getId());
			}

			myConceptMapDao.deleteTermConceptMapById(existingTermConceptMap.getId());
			ourLog.info("Done deleting existing TermConceptMap[{}] and its children.", existingTermConceptMap.getId());

			ourLog.info("Flushing...");
			myConceptMapGroupElementTargetDao.flush();
			myConceptMapGroupElementDao.flush();
			myConceptMapGroupDao.flush();
			myConceptMapDao.flush();
			ourLog.info("Done flushing.");
		}
	}

	@Override
	@Transactional
	public void deleteConceptMapAndChildren(ResourceTable theResourceTable) {
		deleteConceptMap(theResourceTable);
	}

	public void deleteValueSet(ResourceTable theResourceTable) {
		// Get existing entity so it can be deleted.
		Optional<TermValueSet> optionalExistingTermValueSetById = myValueSetDao.findByResourcePid(theResourceTable.getId());

		if (optionalExistingTermValueSetById.isPresent()) {
			TermValueSet existingTermValueSet = optionalExistingTermValueSetById.get();

			ourLog.info("Deleting existing TermValueSet[{}] and its children...", existingTermValueSet.getId());
			myValueSetConceptDesignationDao.deleteByTermValueSetId(existingTermValueSet.getId());
			myValueSetConceptDao.deleteByTermValueSetId(existingTermValueSet.getId());
			myValueSetDao.deleteByTermValueSetId(existingTermValueSet.getId());
			ourLog.info("Done deleting existing TermValueSet[{}] and its children.", existingTermValueSet.getId());

			ourLog.info("Flushing...");
			myValueSetConceptDesignationDao.flush();
			myValueSetConceptDao.flush();
			myValueSetDao.flush();
			ourLog.info("Done flushing.");
		}
	}

	@Override
	@Transactional
	public void deleteValueSetAndChildren(ResourceTable theResourceTable) {
		deleteValueSet(theResourceTable);
	}

	private <T> void doDelete(String theDescriptor, Supplier<Slice<T>> theLoader, Supplier<Integer> theCounter, JpaRepository<T, ?> theDao) {
		int count;
		ourLog.info(" * Deleting {}", theDescriptor);
		int totalCount = theCounter.get();
		StopWatch sw = new StopWatch();
		count = 0;
		while (true) {
			Slice<T> link = theLoader.get();
			if (!link.hasContent()) {
				break;
			}

			TransactionTemplate txTemplate = new TransactionTemplate(myTransactionManager);
			txTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
			txTemplate.execute(t -> {
				theDao.deleteAll(link);
				return null;
			});

			count += link.getNumberOfElements();
			ourLog.info(" * {} {} deleted - {}/sec - ETA: {}", count, theDescriptor, sw.formatThroughput(count, TimeUnit.SECONDS), sw.getEstimatedTimeRemaining(count, totalCount));
		}
		theDao.flush();
	}

	private int ensureParentsSaved(Collection<TermConceptParentChildLink> theParents) {
		ourLog.trace("Checking {} parents", theParents.size());
		int retVal = 0;

		for (TermConceptParentChildLink nextLink : theParents) {
			if (nextLink.getRelationshipType() == RelationshipTypeEnum.ISA) {
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

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ValueSet expandValueSet(ValueSet theValueSetToExpand) {

		ValueSetExpansionComponentWithConceptAccumulator expansionComponent = new ValueSetExpansionComponentWithConceptAccumulator();
		expansionComponent.setIdentifier(UUID.randomUUID().toString());
		expansionComponent.setTimestamp(new Date());

		AtomicInteger codeCounter = new AtomicInteger(0);

		expandValueSet(theValueSetToExpand, expansionComponent, codeCounter);

		expansionComponent.setTotal(codeCounter.get());

		ValueSet valueSet = new ValueSet();
		valueSet.setStatus(Enumerations.PublicationStatus.ACTIVE);
		valueSet.setCompose(theValueSetToExpand.getCompose());
		valueSet.setExpansion(expansionComponent);
		return valueSet;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ValueSet expandValueSet(ValueSet theValueSetToExpand, int theOffset, int theCount) {
		ValidateUtil.isNotNullOrThrowUnprocessableEntity(theValueSetToExpand, "ValueSet to expand can not be null");

		Optional<TermValueSet> optionalTermValueSet;
		if (theValueSetToExpand.hasId()) {
			Long valueSetResourcePid = getValueSetResourcePid(theValueSetToExpand.getIdElement());
			optionalTermValueSet = myValueSetDao.findByResourcePid(valueSetResourcePid);
		} else if (theValueSetToExpand.hasUrl()) {
			optionalTermValueSet = myValueSetDao.findByUrl(theValueSetToExpand.getUrl());
		} else {
			throw new UnprocessableEntityException("ValueSet to be expanded must provide either ValueSet.id or ValueSet.url");
		}

		if (!optionalTermValueSet.isPresent()) {
			ourLog.warn("ValueSet is not present in terminology tables. Will perform in-memory expansion without parameters. Will schedule this ValueSet for pre-expansion. {}", getValueSetInfo(theValueSetToExpand));
			myDeferredValueSets.add(theValueSetToExpand);
			return expandValueSet(theValueSetToExpand); // In-memory expansion.
		}

		TermValueSet termValueSet = optionalTermValueSet.get();

		if (termValueSet.getExpansionStatus() != TermValueSetPreExpansionStatusEnum.EXPANDED) {
			ourLog.warn("{} is present in terminology tables but not ready for persistence-backed invocation of operation $expand. Will perform in-memory expansion without parameters. Current status: {} | {}",
				getValueSetInfo(theValueSetToExpand), termValueSet.getExpansionStatus().name(), termValueSet.getExpansionStatus().getDescription());
			return expandValueSet(theValueSetToExpand); // In-memory expansion.
		}

		ValueSet.ValueSetExpansionComponent expansionComponent = new ValueSet.ValueSetExpansionComponent();
		expansionComponent.setIdentifier(UUID.randomUUID().toString());
		expansionComponent.setTimestamp(new Date());

		populateExpansionComponent(expansionComponent, termValueSet, theOffset, theCount);

		ValueSet valueSet = new ValueSet();
		valueSet.setStatus(Enumerations.PublicationStatus.ACTIVE);
		valueSet.setCompose(theValueSetToExpand.getCompose());
		valueSet.setExpansion(expansionComponent);
		return valueSet;
	}

	private void populateExpansionComponent(ValueSet.ValueSetExpansionComponent theExpansionComponent, TermValueSet theTermValueSet, int theOffset, int theCount) {
		int total = myValueSetConceptDao.countByTermValueSetId(theTermValueSet.getId());
		theExpansionComponent.setTotal(total);
		theExpansionComponent.setOffset(theOffset);
		theExpansionComponent.addParameter().setName("offset").setValue(new IntegerType(theOffset));
		theExpansionComponent.addParameter().setName("count").setValue(new IntegerType(theCount));

		if (theCount == 0 || total == 0) {
			return;
		}

		expandConcepts(theExpansionComponent, theTermValueSet, theOffset, theCount);
	}

	private void expandConcepts(ValueSet.ValueSetExpansionComponent theExpansionComponent, TermValueSet theTermValueSet, int theOffset, int theCount) {
		int conceptsExpanded = 0;
		for (int i = theOffset; i < (theOffset + theCount); i++) {
			final int page = i;
			Supplier<Slice<TermValueSetConcept>> loader = () -> myValueSetConceptDao.findByTermValueSetId(PageRequest.of(page, 1), theTermValueSet.getId());

			Slice<TermValueSetConcept> slice = loader.get();
			if (!slice.hasContent()) {
				break;
			}

			for (TermValueSetConcept concept : slice.getContent()) {
				ValueSet.ValueSetExpansionContainsComponent containsComponent = theExpansionComponent.addContains();
				containsComponent.setSystem(concept.getSystem());
				containsComponent.setCode(concept.getCode());
				containsComponent.setDisplay(concept.getDisplay());

				// TODO: DM 2019-08-17 - Implement includeDesignations parameter for $expand operation to make this optional.
				expandDesignations(theTermValueSet, concept, containsComponent);

				if (++conceptsExpanded % 250 == 0) {
					ourLog.info("Have expanded {} concepts in ValueSet[{}]", conceptsExpanded, theTermValueSet.getUrl());
				}
			}

			if (!slice.hasNext()) {
				break;
			}
		}

		if (conceptsExpanded > 0) {
			ourLog.info("Have expanded {} concepts in ValueSet[{}]", conceptsExpanded, theTermValueSet.getUrl());
		}
	}

	private void expandDesignations(TermValueSet theValueSet, TermValueSetConcept theConcept, ValueSet.ValueSetExpansionContainsComponent theContainsComponent) {
		int designationsExpanded = 0;
		int index = 0;
		while (true) {
			final int page = index++;
			Supplier<Slice<TermValueSetConceptDesignation>> loader = () -> myValueSetConceptDesignationDao.findByTermValueSetConceptId(PageRequest.of(page, 1000), theConcept.getId());

			Slice<TermValueSetConceptDesignation> slice = loader.get();
			if (!slice.hasContent()) {
				break;
			}

			for (TermValueSetConceptDesignation designation : slice.getContent()) {
				ValueSet.ConceptReferenceDesignationComponent designationComponent = theContainsComponent.addDesignation();
				designationComponent.setLanguage(designation.getLanguage());
				designationComponent.setUse(new Coding(
					designation.getUseSystem(),
					designation.getUseCode(),
					designation.getUseDisplay()));
				designationComponent.setValue(designation.getValue());

				if (++designationsExpanded % 250 == 0) {
					ourLog.info("Have expanded {} designations for Concept[{}|{}] in ValueSet[{}]", designationsExpanded, theConcept.getSystem(), theConcept.getCode(), theValueSet.getUrl());
				}
			}

			if (!slice.hasNext()) {
				break;
			}
		}

		if (designationsExpanded > 0) {
			ourLog.info("Have expanded {} designations for Concept[{}|{}] in ValueSet[{}]", designationsExpanded, theConcept.getSystem(), theConcept.getCode(), theValueSet.getUrl());
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void expandValueSet(ValueSet theValueSetToExpand, IValueSetConceptAccumulator theValueSetCodeAccumulator) {
		expandValueSet(theValueSetToExpand, theValueSetCodeAccumulator, new AtomicInteger(0));
	}

	@SuppressWarnings("ConstantConditions")
	private void expandValueSet(ValueSet theValueSetToExpand, IValueSetConceptAccumulator theValueSetCodeAccumulator, AtomicInteger theCodeCounter) {
		Set<String> addedCodes = new HashSet<>();

		StopWatch sw = new StopWatch();
		String valueSetInfo = getValueSetInfo(theValueSetToExpand);
		ourLog.info("Working with {}", valueSetInfo);

		// Handle includes
		ourLog.debug("Handling includes");
		for (ValueSet.ConceptSetComponent include : theValueSetToExpand.getCompose().getInclude()) {
			for (int i = 0; ; i++) {
				int finalI = i;
				Boolean shouldContinue = myTxTemplate.execute(t -> {
					boolean add = true;
					return expandValueSetHandleIncludeOrExclude(theValueSetCodeAccumulator, addedCodes, include, add, theCodeCounter, finalI);
				});
				if (!shouldContinue) {
					break;
				}
			}
		}

		// Handle excludes
		ourLog.debug("Handling excludes");
		for (ValueSet.ConceptSetComponent exclude : theValueSetToExpand.getCompose().getExclude()) {
			for (int i = 0; ; i++) {
				int finalI = i;
				Boolean shouldContinue = myTxTemplate.execute(t -> {
					boolean add = false;
					return expandValueSetHandleIncludeOrExclude(theValueSetCodeAccumulator, addedCodes, exclude, add, theCodeCounter, finalI);
				});
				if (!shouldContinue) {
					break;
				}
			}
		}

		ourLog.info("Done working with {} in {}ms", valueSetInfo, sw.getMillis());
	}

	private String getValueSetInfo(ValueSet theValueSet) {
		StringBuilder sb = new StringBuilder();
		boolean isIdentified = false;
		sb
			.append("ValueSet:");
		if (theValueSet.hasId()) {
			isIdentified = true;
			sb
				.append(" ValueSet.id[")
				.append(theValueSet.getId())
				.append("]");
		}
		if (theValueSet.hasUrl()) {
			isIdentified = true;
			sb
				.append(" ValueSet.url[")
				.append(theValueSet.getUrl())
				.append("]");
		}
		if (theValueSet.hasIdentifier()) {
			isIdentified = true;
			sb
				.append(" ValueSet.identifier[")
				.append(theValueSet.getIdentifierFirstRep().getSystem())
				.append("|")
				.append(theValueSet.getIdentifierFirstRep().getValue())
				.append("]");
		}

		if (!isIdentified) {
			sb.append(" None of ValueSet.id, ValueSet.url, and ValueSet.identifier are provided.");
		}

		return sb.toString();
	}

	protected List<VersionIndependentConcept> expandValueSetAndReturnVersionIndependentConcepts(org.hl7.fhir.r4.model.ValueSet theValueSetToExpandR4) {
		org.hl7.fhir.r4.model.ValueSet.ValueSetExpansionComponent expandedR4 = expandValueSet(theValueSetToExpandR4).getExpansion();

		ArrayList<VersionIndependentConcept> retVal = new ArrayList<>();
		for (org.hl7.fhir.r4.model.ValueSet.ValueSetExpansionContainsComponent nextContains : expandedR4.getContains()) {
			retVal.add(
				new VersionIndependentConcept(nextContains.getSystem(), nextContains.getCode()));
		}
		return retVal;
	}

	/**
	 * @return Returns true if there are potentially more results to process.
	 */
	private Boolean expandValueSetHandleIncludeOrExclude(IValueSetConceptAccumulator theValueSetCodeAccumulator, Set<String> theAddedCodes, ValueSet.ConceptSetComponent theInclude, boolean theAdd, AtomicInteger theCodeCounter, int theQueryIndex) {

		String system = theInclude.getSystem();
		boolean hasSystem = isNotBlank(system);
		boolean hasValueSet = theInclude.getValueSet().size() > 0;

		if (hasSystem) {
			ourLog.info("Starting {} expansion around CodeSystem: {}", (theAdd ? "inclusion" : "exclusion"), system);

			TermCodeSystem cs = myCodeSystemDao.findByCodeSystemUri(system);
			if (cs != null) {

				TermCodeSystemVersion csv = cs.getCurrentVersion();
				FullTextEntityManager em = org.hibernate.search.jpa.Search.getFullTextEntityManager(myEntityManager);

				/*
				 * If FullText searching is not enabled, we can handle only basic expansions
				 * since we're going to do it without the database.
				 */
				if (myFulltextSearchSvc == null) {
					expandWithoutHibernateSearch(theValueSetCodeAccumulator, theAddedCodes, theInclude, system, theAdd, theCodeCounter);
					return false;
				}

				/*
				 * Ok, let's use hibernate search to build the expansion
				 */
				QueryBuilder qb = em.getSearchFactory().buildQueryBuilder().forEntity(TermConcept.class).get();
				BooleanJunction<?> bool = qb.bool();

				bool.must(qb.keyword().onField("myCodeSystemVersionPid").matching(csv.getPid()).createQuery());

				/*
				 * Filters
				 */

				if (theInclude.getFilter().size() > 0) {

					for (ValueSet.ConceptSetFilterComponent nextFilter : theInclude.getFilter()) {
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
						} else if (nextFilter.getProperty().equals("concept") || nextFilter.getProperty().equals("code")) {

							TermConcept code = findCode(system, nextFilter.getValue())
								.orElseThrow(() -> new InvalidRequestException("Invalid filter criteria - code does not exist: {" + system + "}" + nextFilter.getValue()));

							if (nextFilter.getOp() == ValueSet.FilterOperator.ISA) {
								ourLog.info(" * Filtering on codes with a parent of {}/{}/{}", code.getId(), code.getCode(), code.getDisplay());
								bool.must(qb.keyword().onField("myParentPids").matching("" + code.getId()).createQuery());
							} else {
								throw new InvalidRequestException("Don't know how to handle op=" + nextFilter.getOp() + " on property " + nextFilter.getProperty());
							}

						} else {

							if (nextFilter.getOp() == ValueSet.FilterOperator.REGEX) {

								/*
								 * We treat the regex filter as a match on the regex
								 * anywhere in the property string. The spec does not
								 * say whether or not this is the right behaviour, but
								 * there are examples that seem to suggest that it is.
								 */
								String value = nextFilter.getValue();
								if (value.endsWith("$")) {
									value = value.substring(0, value.length() - 1);
								} else if (!value.endsWith(".*")) {
									value = value + ".*";
								}
								if (!value.startsWith("^") && !value.startsWith(".*")) {
									value = ".*" + value;
								} else if (value.startsWith("^")) {
									value = value.substring(1);
								}

								Term term = new Term(TermConceptPropertyFieldBridge.CONCEPT_FIELD_PROPERTY_PREFIX + nextFilter.getProperty(), value);
								RegexpQuery query = new RegexpQuery(term);
								bool.must(query);

							} else {

								String value = nextFilter.getValue();
								Term term = new Term(TermConceptPropertyFieldBridge.CONCEPT_FIELD_PROPERTY_PREFIX + nextFilter.getProperty(), value);
								bool.must(new TermsQuery(term));

							}

						}
					}

				}

				Query luceneQuery = bool.createQuery();

				/*
				 * Include Concepts
				 */

				List<Term> codes = theInclude
					.getConcept()
					.stream()
					.filter(Objects::nonNull)
					.map(ValueSet.ConceptReferenceComponent::getCode)
					.filter(StringUtils::isNotBlank)
					.map(t -> new Term("myCode", t))
					.collect(Collectors.toList());
				if (codes.size() > 0) {
					MultiPhraseQuery query = new MultiPhraseQuery();
					query.add(codes.toArray(new Term[0]));
					luceneQuery = new BooleanQuery.Builder()
						.add(luceneQuery, BooleanClause.Occur.MUST)
						.add(query, BooleanClause.Occur.MUST)
						.build();
				}

				/*
				 * Execute the query
				 */

				FullTextQuery jpaQuery = em.createFullTextQuery(luceneQuery, TermConcept.class);
				/*
				 * DM 2019-08-21 - Processing slows after any ValueSets with many codes explicitly identified. This might
				 * be due to the dark arts that is memory management. Will monitor but not do anything about this right now.
				 */
				BooleanQuery.setMaxClauseCount(10000);

				StopWatch sw = new StopWatch();
				AtomicInteger count = new AtomicInteger(0);

				int maxResultsPerBatch = 10000;
				jpaQuery.setMaxResults(maxResultsPerBatch);
				jpaQuery.setFirstResult(theQueryIndex * maxResultsPerBatch);

				ourLog.info("Beginning batch expansion for {} with max results per batch: {}", (theAdd ? "inclusion" : "exclusion"), maxResultsPerBatch);

				StopWatch swForBatch = new StopWatch();
				AtomicInteger countForBatch = new AtomicInteger(0);

				List resultList = jpaQuery.getResultList();
				int resultsInBatch = resultList.size();
				int firstResult = jpaQuery.getFirstResult();
				for (Object next : resultList) {
					count.incrementAndGet();
					countForBatch.incrementAndGet();
					TermConcept concept = (TermConcept) next;
					addCodeIfNotAlreadyAdded(theValueSetCodeAccumulator, theAddedCodes, concept, theAdd, theCodeCounter);
				}

				ourLog.info("Batch expansion for {} with starting index of {} produced {} results in {}ms", (theAdd ? "inclusion" : "exclusion"), firstResult, countForBatch, swForBatch.getMillis());

				if (resultsInBatch < maxResultsPerBatch) {
					ourLog.info("Expansion for {} produced {} results in {}ms", (theAdd ? "inclusion" : "exclusion"), count, sw.getMillis());
					return false;
				} else {
					return true;
				}

			} else {
				// No codesystem matching the URL found in the database

				CodeSystem codeSystemFromContext = getCodeSystemFromContext(system);
				if (codeSystemFromContext == null) {
					throw new InvalidRequestException("Unknown code system: " + system);
				}

				if (!theInclude.getConcept().isEmpty()) {
					for (ValueSet.ConceptReferenceComponent next : theInclude.getConcept()) {
						String nextCode = next.getCode();
						if (isNoneBlank(system, nextCode) && !theAddedCodes.contains(system + "|" + nextCode)) {
							CodeSystem.ConceptDefinitionComponent code = findCode(codeSystemFromContext.getConcept(), nextCode);
							if (code != null) {
								if (theAdd && theAddedCodes.add(system + "|" + nextCode)) {
									theValueSetCodeAccumulator.includeConcept(system, nextCode, code.getDisplay());
								}
								if (!theAdd && theAddedCodes.remove(system + "|" + nextCode)) {
									theValueSetCodeAccumulator.excludeConcept(system, nextCode);
								}
							}
						}
					}
				} else {
					List<CodeSystem.ConceptDefinitionComponent> concept = codeSystemFromContext.getConcept();
					addConceptsToList(theValueSetCodeAccumulator, theAddedCodes, system, concept, theAdd);
				}

				return false;
			}
		} else if (hasValueSet) {

			for (CanonicalType nextValueSet : theInclude.getValueSet()) {
				ourLog.info("Starting {} expansion around ValueSet: {}", (theAdd ? "inclusion" : "exclusion"), nextValueSet.getValueAsString());

				List<VersionIndependentConcept> expanded = expandValueSet(nextValueSet.getValueAsString());
				for (VersionIndependentConcept nextConcept : expanded) {
					if (theAdd) {
						TermCodeSystem codeSystem = myCodeSystemDao.findByCodeSystemUri(nextConcept.getSystem());
						myConceptDao
							.findByCodeSystemAndCode(codeSystem.getCurrentVersion(), nextConcept.getCode())
							.ifPresent(concept ->
								addCodeIfNotAlreadyAdded(theValueSetCodeAccumulator, theAddedCodes, concept, theAdd, theCodeCounter)
							);

					}
					if (isNoneBlank(nextConcept.getSystem(), nextConcept.getCode()) && !theAdd && theAddedCodes.remove(nextConcept.getSystem() + "|" + nextConcept.getCode())) {
						theValueSetCodeAccumulator.excludeConcept(nextConcept.getSystem(), nextConcept.getCode());
					}
				}

			}

			return false;

		} else {
			throw new InvalidRequestException("ValueSet contains " + (theAdd ? "include" : "exclude") + " criteria with no system defined");
		}


	}

	private void expandWithoutHibernateSearch(IValueSetConceptAccumulator theValueSetCodeAccumulator, Set<String> theAddedCodes, ValueSet.ConceptSetComponent theInclude, String theSystem, boolean theAdd, AtomicInteger theCodeCounter) {
		ourLog.trace("Hibernate search is not enabled");
		if (theValueSetCodeAccumulator instanceof ValueSetExpansionComponentWithConceptAccumulator) {
			Validate.isTrue(((ValueSetExpansionComponentWithConceptAccumulator) theValueSetCodeAccumulator).getParameter().isEmpty(), "Can not expand ValueSet with parameters - Hibernate Search is not enabled on this server.");
		}
		Validate.isTrue(theInclude.getFilter().isEmpty(), "Can not expand ValueSet with filters - Hibernate Search is not enabled on this server.");
		Validate.isTrue(isNotBlank(theSystem), "Can not expand ValueSet without explicit system - Hibernate Search is not enabled on this server.");

		for (ValueSet.ConceptReferenceComponent next : theInclude.getConcept()) {
			if (!theSystem.equals(theInclude.getSystem())) {
				continue;
			}
			addCodeIfNotAlreadyAdded(theValueSetCodeAccumulator, theAddedCodes, null, theAdd, theCodeCounter, theSystem, next.getCode(), next.getDisplay());
		}
	}

	@Override
	public boolean isValueSetPreExpandedForCodeValidation(ValueSet theValueSet) {
		Long valueSetResourcePid = getValueSetResourcePid(theValueSet.getIdElement());
		Optional<TermValueSet> optionalTermValueSet = myValueSetDao.findByResourcePid(valueSetResourcePid);

		if (!optionalTermValueSet.isPresent()) {
			ourLog.warn("ValueSet is not present in terminology tables. Will perform in-memory code validation. Will schedule this ValueSet for pre-expansion. {}", getValueSetInfo(theValueSet));
			myDeferredValueSets.add(theValueSet);
			return false;
		}

		TermValueSet termValueSet = optionalTermValueSet.get();

		if (termValueSet.getExpansionStatus() != TermValueSetPreExpansionStatusEnum.EXPANDED) {
			ourLog.warn("{} is present in terminology tables but not ready for persistence-backed invocation of operation $validation-code. Will perform in-memory code validation. Current status: {} | {}",
				getValueSetInfo(theValueSet), termValueSet.getExpansionStatus().name(), termValueSet.getExpansionStatus().getDescription());
			return false;
		}

		return true;
	}

	protected ValidateCodeResult validateCodeIsInPreExpandedValueSet(
		ValueSet theValueSet, String theSystem, String theCode, String theDisplay, Coding theCoding, CodeableConcept theCodeableConcept) {

		ValidateUtil.isNotNullOrThrowUnprocessableEntity(theValueSet.hasId(), "ValueSet.id is required");
		Long valueSetResourcePid = getValueSetResourcePid(theValueSet.getIdElement());

		List<TermValueSetConcept> concepts = new ArrayList<>();
		if (isNotBlank(theCode)) {
			if (isNotBlank(theSystem)) {
				concepts.addAll(findByValueSetResourcePidSystemAndCode(valueSetResourcePid, theSystem, theCode));
			} else {
				concepts.addAll(myValueSetConceptDao.findByValueSetResourcePidAndCode(valueSetResourcePid, theCode));
			}
		} else if (theCoding != null) {
			if (theCoding.hasSystem() && theCoding.hasCode()) {
				concepts.addAll(findByValueSetResourcePidSystemAndCode(valueSetResourcePid, theCoding.getSystem(), theCoding.getCode()));
			}
		} else if (theCodeableConcept != null){
			for (Coding coding : theCodeableConcept.getCoding()) {
				if (coding.hasSystem() && coding.hasCode()) {
					concepts.addAll(findByValueSetResourcePidSystemAndCode(valueSetResourcePid, coding.getSystem(), coding.getCode()));
					if (!concepts.isEmpty()) {
						break;
					}
				}
			}
		}

		for (TermValueSetConcept concept : concepts) {
			if (isNotBlank(theDisplay) && theDisplay.equals(concept.getDisplay())) {
				return new ValidateCodeResult(true, "Validation succeeded", concept.getDisplay());
			}
		}

		if (!concepts.isEmpty()) {
			return new ValidateCodeResult(true, "Validation succeeded", concepts.get(0).getDisplay());
		}

		return null;
	}

	private List<TermValueSetConcept> findByValueSetResourcePidSystemAndCode(Long theResourcePid, String theSystem, String theCode) {
		List<TermValueSetConcept> retVal = new ArrayList<>();
		Optional<TermValueSetConcept> optionalTermValueSetConcept = myValueSetConceptDao.findByValueSetResourcePidSystemAndCode(theResourcePid, theSystem, theCode);
		if (optionalTermValueSetConcept.isPresent()) {
			retVal.add(optionalTermValueSetConcept.get());
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

	private Optional<TermConcept> fetchLoadedCode(Long theCodeSystemResourcePid, String theCode) {
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
			CodeSystem.ConceptDefinitionComponent val = findCode(next.getConcept(), theCode);
			if (val != null) {
				return val;
			}
		}
		return null;
	}

	@Override
	public Optional<TermConcept> findCode(String theCodeSystem, String theCode) {
		/*
		 * Loading concepts without a transaction causes issues later on some
		 * platforms (e.g. PSQL) so this transactiontemplate is here to make
		 * sure that we always call this with an open transaction
		 */
		TransactionTemplate txTemplate = new TransactionTemplate(myTransactionManager);
		txTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_MANDATORY);
		return txTemplate.execute(t -> {
			TermCodeSystemVersion csv = findCurrentCodeSystemVersionForSystem(theCodeSystem);
			return myConceptDao.findByCodeSystemAndCode(csv, theCode);
		});
	}

	@Override
	public List<TermConcept> findCodes(String theSystem) {
		return myConceptDao.findByCodeSystemVersion(findCurrentCodeSystemVersionForSystem(theSystem));
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public Set<TermConcept> findCodesAbove(Long theCodeSystemResourcePid, Long theCodeSystemVersionPid, String theCode) {
		StopWatch stopwatch = new StopWatch();

		Optional<TermConcept> concept = fetchLoadedCode(theCodeSystemResourcePid, theCode);
		if (!concept.isPresent()) {
			return Collections.emptySet();
		}

		Set<TermConcept> retVal = new HashSet<>();
		retVal.add(concept.get());

		fetchParents(concept.get(), retVal);

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

		Optional<TermConcept> concept = fetchLoadedCode(theCodeSystemResourcePid, theCode);
		if (!concept.isPresent()) {
			return Collections.emptySet();
		}

		Set<TermConcept> retVal = new HashSet<>();
		retVal.add(concept.get());

		fetchChildren(concept.get(), retVal);

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

	private Long getCodeSystemResourcePid(IIdType theIdType) {
		return getCodeSystemResourcePid(theIdType, null);
	}

	private Long getCodeSystemResourcePid(IIdType theIdType, RequestDetails theRequestDetails) {
		return getResourcePid(myCodeSystemResourceDao, theIdType, theRequestDetails);
	}

	private Long getValueSetResourcePid(IIdType theIdType) {
		return getValueSetResourcePid(theIdType, null);
	}

	private Long getValueSetResourcePid(IIdType theIdType, RequestDetails theRequestDetails) {
		return getResourcePid(myValueSetResourceDao, theIdType, theRequestDetails);
	}

	private Long getResourcePid(IFhirResourceDao<? extends IBaseResource> theResourceDao, IIdType theIdType, RequestDetails theRequestDetails) {
		ResourceTable resourceTable = (ResourceTable) theResourceDao.readEntity(theIdType, theRequestDetails);
		return resourceTable.getId();
	}

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

				if (!myConceptDao.findById(next.getChild().getId()).isPresent() || !myConceptDao.findById(next.getParent().getId()).isPresent()) {
					ourLog.warn("Not inserting link from child {} to parent {} because it appears to have been deleted", next.getParent().getCode(), next.getChild().getCode());
					continue;
				}

				saveConceptLink(next);
				relCount++;
			}
		}

		if (relCount > 0) {
			ourLog.info("Saved {} deferred relationships ({} remain) in {}ms ({}ms / entry)",
				relCount, myConceptLinksToSaveLater.size(), stopwatch.getMillis(), stopwatch.getMillisPerOperation(relCount));
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
		tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
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
				Page<TermConcept> concepts = myConceptDao.findResourcesRequiringReindexing(PageRequest.of(0, maxResult));
				if (!concepts.hasContent()) {
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

					if (isBlank(nextConcept.getParentPidsAsString())) {
						StringBuilder parentsBuilder = new StringBuilder();
						createParentsString(parentsBuilder, nextConcept.getId());
						nextConcept.setParentPids(parentsBuilder.toString());
					}

					saveConcept(nextConcept);
					count++;
				}

				ourLog.info("Indexed {} / {} concepts in {}ms - Avg {}ms / resource", count, concepts.getContent().size(), stopwatch.getMillis(), stopwatch.getMillisPerOperation(count));
			}
		});

	}

	/**
	 * Returns the number of saved concepts
	 */
	private int saveOrUpdateConcept(TermConcept theConcept) {

		TermCodeSystemVersion csv = theConcept.getCodeSystemVersion();
		Optional<TermConcept> existing = myConceptDao.findByCodeSystemAndCode(csv, theConcept.getCode());
		if (existing.isPresent()) {
			TermConcept existingConcept = existing.get();
			boolean haveChanges = false;
			if (!StringUtils.equals(existingConcept.getDisplay(), theConcept.getDisplay())) {
				existingConcept.setDisplay(theConcept.getDisplay());
				haveChanges = true;
			}

			if (!haveChanges) {
				return 0;
			}

			myConceptDao.save(existingConcept);
			return 1;

		} else {
			return saveConcept(theConcept);
		}

	}

	/**
	 * Returns the number of saved concepts
	 */
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

	private void saveConceptLink(TermConceptParentChildLink next) {
		if (next.getId() == null) {
			myConceptParentChildLinkDao.save(next);
		}
	}

	@Scheduled(fixedRate = 5000)
	@Transactional(propagation = Propagation.NEVER)
	@Override
	public synchronized void saveDeferred() {
		if (isProcessDeferredPaused()) {
			return;
		} else if (isNoDeferredConceptsAndNoConceptLinksToSaveLater()) {
			processReindexing();
		}

		TransactionTemplate tt = new TransactionTemplate(myTransactionMgr);
		tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		if (isDeferredConceptsOrConceptLinksToSaveLater()) {
			tt.execute(t -> {
				processDeferredConcepts();
				return null;
			});
		}

		if (isDeferredValueSets()) {
			tt.execute(t -> {
				processDeferredValueSets();
				return null;
			});
		}
		if (isDeferredConceptMaps()) {
			tt.execute(t -> {
				processDeferredConceptMaps();
				return null;
			});
		}

	}

	private boolean isProcessDeferredPaused() {
		return !myProcessDeferred;
	}

	private boolean isNoDeferredConceptsAndNoConceptLinksToSaveLater() {
		return isNoDeferredConcepts() && isNoConceptLinksToSaveLater();
	}

	private boolean isDeferredConceptsOrConceptLinksToSaveLater() {
		return isDeferredConcepts() || isConceptLinksToSaveLater();
	}

	private boolean isDeferredConcepts() {
		return !myDeferredConcepts.isEmpty();
	}

	private boolean isNoDeferredConcepts() {
		return myDeferredConcepts.isEmpty();
	}

	private boolean isConceptLinksToSaveLater() {
		return !myConceptLinksToSaveLater.isEmpty();
	}

	private boolean isNoConceptLinksToSaveLater() {
		return myConceptLinksToSaveLater.isEmpty();
	}

	private boolean isDeferredValueSets() {
		return !myDeferredValueSets.isEmpty();
	}

	private boolean isDeferredConceptMaps() {
		return !myDeferredConceptMaps.isEmpty();
	}

	@Override
	public void setApplicationContext(ApplicationContext theApplicationContext) throws BeansException {
		myApplicationContext = theApplicationContext;
	}

	@Override
	public void setProcessDeferred(boolean theProcessDeferred) {
		myProcessDeferred = theProcessDeferred;
	}

	@PostConstruct
	public void start() {
		myCodeSystemResourceDao = myApplicationContext.getBean(IFhirResourceDaoCodeSystem.class);
		myValueSetResourceDao = myApplicationContext.getBean(IFhirResourceDaoValueSet.class);
		myTxTemplate = new TransactionTemplate(myTransactionManager);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void storeNewCodeSystemVersion(Long theCodeSystemResourcePid, String theSystemUri, String theSystemName, String theSystemVersionId, TermCodeSystemVersion theCodeSystemVersion) {
		ourLog.info("Storing code system");

		ValidateUtil.isTrueOrThrowInvalidRequest(theCodeSystemVersion.getResource() != null, "No resource supplied");
		ValidateUtil.isNotBlankOrThrowInvalidRequest(theSystemUri, "No system URI supplied");

		// Grab the existing versions so we can delete them later
		List<TermCodeSystemVersion> existing = myCodeSystemVersionDao.findByCodeSystemResource(theCodeSystemResourcePid);

		/*
		 * For now we always delete old versions. At some point it would be nice to allow configuration to keep old versions.
		 */

		ourLog.info("Deleting old code system versions");
		for (TermCodeSystemVersion next : existing) {
			Long codeSystemVersionPid = next.getPid();
			deleteCodeSystemVersion(codeSystemVersionPid);
		}

		ourLog.info("Flushing...");
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
				String msg = myContext.getLocalizer().getMessage(BaseHapiTerminologySvcImpl.class, "cannotCreateDuplicateCodeSystemUrl", theSystemUri,
					codeSystem.getResource().getIdDt().toUnqualifiedVersionless().getValue());
				throw new UnprocessableEntityException(msg);
			}
		}
		theCodeSystemVersion.setCodeSystem(codeSystem);

		theCodeSystemVersion.setCodeSystemDisplayName(theSystemName);
		theCodeSystemVersion.setCodeSystemVersionId(theSystemVersionId);

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

		ourLog.info("Setting CodeSystemVersion[{}] on {} concepts...", codeSystem.getPid(), totalCodeCount);

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
	public IIdType storeNewCodeSystemVersion(CodeSystem theCodeSystemResource, TermCodeSystemVersion theCodeSystemVersion, RequestDetails theRequest, List<ValueSet> theValueSets, List<ConceptMap> theConceptMaps) {
		Validate.notBlank(theCodeSystemResource.getUrl(), "theCodeSystemResource must have a URL");

		IIdType csId = createOrUpdateCodeSystem(theCodeSystemResource);

		ResourceTable resource = (ResourceTable) myCodeSystemResourceDao.readEntity(csId, theRequest);
		Long codeSystemResourcePid = resource.getId();

		ourLog.info("CodeSystem resource has ID: {}", csId.getValue());

		populateCodeSystemVersionProperties(theCodeSystemVersion, theCodeSystemResource, resource);

		storeNewCodeSystemVersion(codeSystemResourcePid, theCodeSystemResource.getUrl(), theCodeSystemResource.getName(), theCodeSystemResource.getVersion(), theCodeSystemVersion);

		myDeferredConceptMaps.addAll(theConceptMaps);
		myDeferredValueSets.addAll(theValueSets);

		return csId;
	}

	private void populateCodeSystemVersionProperties(TermCodeSystemVersion theCodeSystemVersion, CodeSystem theCodeSystemResource, ResourceTable theResourceTable) {
		theCodeSystemVersion.setResource(theResourceTable);
		theCodeSystemVersion.setCodeSystemDisplayName(theCodeSystemResource.getName());
		theCodeSystemVersion.setCodeSystemVersionId(theCodeSystemResource.getVersion());
	}

	@Override
	public void storeNewCodeSystemVersionIfNeeded(CodeSystem theCodeSystem, ResourceTable theResourceEntity) {
		if (theCodeSystem != null && isNotBlank(theCodeSystem.getUrl())) {
			String codeSystemUrl = theCodeSystem.getUrl();
			if (theCodeSystem.getContent() == CodeSystem.CodeSystemContentMode.COMPLETE || theCodeSystem.getContent() == null || theCodeSystem.getContent() == CodeSystem.CodeSystemContentMode.NOTPRESENT) {
				ourLog.info("CodeSystem {} has a status of {}, going to store concepts in terminology tables", theResourceEntity.getIdDt().getValue(), theCodeSystem.getContentElement().getValueAsString());

				Long codeSystemResourcePid = getCodeSystemResourcePid(theCodeSystem.getIdElement());
				TermCodeSystemVersion persCs = myCodeSystemVersionDao.findCurrentVersionForCodeSystemResourcePid(codeSystemResourcePid);
				if (persCs != null) {
					ourLog.info("Code system version already exists in database");
				} else {

					persCs = new TermCodeSystemVersion();
					populateCodeSystemVersionProperties(persCs, theCodeSystem, theResourceEntity);

					persCs.getConcepts().addAll(toPersistedConcepts(theCodeSystem.getConcept(), persCs));
					ourLog.info("Code system has {} concepts", persCs.getConcepts().size());
					storeNewCodeSystemVersion(codeSystemResourcePid, codeSystemUrl, theCodeSystem.getName(), theCodeSystem.getVersion(), persCs);

				}

			}
		}
	}

	private List<TermConcept> toPersistedConcepts(List<CodeSystem.ConceptDefinitionComponent> theConcept, TermCodeSystemVersion theCodeSystemVersion) {
		ArrayList<TermConcept> retVal = new ArrayList<>();

		for (CodeSystem.ConceptDefinitionComponent next : theConcept) {
			if (isNotBlank(next.getCode())) {
				TermConcept termConcept = toTermConcept(next, theCodeSystemVersion);
				retVal.add(termConcept);
			}
		}

		return retVal;
	}

	@Nonnull
	private TermConcept toTermConcept(CodeSystem.ConceptDefinitionComponent theConceptDefinition, TermCodeSystemVersion theCodeSystemVersion) {
		TermConcept termConcept = new TermConcept();
		termConcept.setCode(theConceptDefinition.getCode());
		termConcept.setCodeSystemVersion(theCodeSystemVersion);
		termConcept.setDisplay(theConceptDefinition.getDisplay());
		termConcept.addChildren(toPersistedConcepts(theConceptDefinition.getConcept(), theCodeSystemVersion), RelationshipTypeEnum.ISA);

		for (CodeSystem.ConceptDefinitionDesignationComponent designationComponent : theConceptDefinition.getDesignation()) {
			if (isNotBlank(designationComponent.getValue())) {
				TermConceptDesignation designation = termConcept.addDesignation();
				designation.setLanguage(designationComponent.hasLanguage() ? designationComponent.getLanguage() : null);
				if (designationComponent.hasUse()) {
					designation.setUseSystem(designationComponent.getUse().hasSystem() ? designationComponent.getUse().getSystem() : null);
					designation.setUseCode(designationComponent.getUse().hasCode() ? designationComponent.getUse().getCode() : null);
					designation.setUseDisplay(designationComponent.getUse().hasDisplay() ? designationComponent.getUse().getDisplay() : null);
				}
				designation.setValue(designationComponent.getValue());
			}
		}

		for (CodeSystem.ConceptPropertyComponent next : theConceptDefinition.getProperty()) {
			TermConceptProperty property = new TermConceptProperty();

			property.setKey(next.getCode());
			property.setConcept(termConcept);
			property.setCodeSystemVersion(theCodeSystemVersion);

			if (next.getValue() instanceof StringType) {
				property.setType(TermConceptPropertyTypeEnum.STRING);
				property.setValue(next.getValueStringType().getValue());
			} else if (next.getValue() instanceof Coding) {
				Coding nextCoding = next.getValueCoding();
				property.setType(TermConceptPropertyTypeEnum.CODING);
				property.setCodeSystem(nextCoding.getSystem());
				property.setValue(nextCoding.getCode());
				property.setDisplay(nextCoding.getDisplay());
			} else if (next.getValue() != null) {
				// TODO: LOINC has properties of type BOOLEAN that we should handle
				ourLog.warn("Don't know how to handle properties of type: " + next.getValue().getClass());
				continue;
			}

			termConcept.getProperties().add(property);
		}
		return termConcept;
	}

	@Override
	@Transactional
	public void storeTermConceptMapAndChildren(ResourceTable theResourceTable, ConceptMap theConceptMap) {
		ourLog.info("Storing TermConceptMap {}", theConceptMap.getIdElement().getValue());

		ValidateUtil.isTrueOrThrowInvalidRequest(theResourceTable != null, "No resource supplied");
		ValidateUtil.isNotBlankOrThrowUnprocessableEntity(theConceptMap.getUrl(), "ConceptMap has no value for ConceptMap.url");

		TermConceptMap termConceptMap = new TermConceptMap();
		termConceptMap.setResource(theResourceTable);
		termConceptMap.setUrl(theConceptMap.getUrl());

		String source = theConceptMap.hasSourceUriType() ? theConceptMap.getSourceUriType().getValueAsString() : null;
		String target = theConceptMap.hasTargetUriType() ? theConceptMap.getTargetUriType().getValueAsString() : null;

		/*
		 * If this is a mapping between "resources" instead of purely between
		 * "concepts" (this is a weird concept that is technically possible, at least as of
		 * FHIR R4), don't try to store the mappings.
		 *
		 * See here for a description of what that is:
		 * http://hl7.org/fhir/conceptmap.html#bnr
		 */
		if ("StructureDefinition".equals(new IdType(source).getResourceType()) ||
			"StructureDefinition".equals(new IdType(target).getResourceType())) {
			return;
		}

		/*
		 * For now we always delete old versions. At some point, it would be nice to allow configuration to keep old versions.
		 */
		deleteConceptMap(theResourceTable);

		/*
		 * Do the upload.
		 */
		String conceptMapUrl = termConceptMap.getUrl();
		Optional<TermConceptMap> optionalExistingTermConceptMapByUrl = myConceptMapDao.findTermConceptMapByUrl(conceptMapUrl);
		if (!optionalExistingTermConceptMapByUrl.isPresent()) {
			try {
				if (isNotBlank(source)) {
					termConceptMap.setSource(source);
				}
				if (isNotBlank(target)) {
					termConceptMap.setTarget(target);
				}
			} catch (FHIRException fe) {
				throw new InternalErrorException(fe);
			}
			myConceptMapDao.save(termConceptMap);
			int codesSaved = 0;

			if (theConceptMap.hasGroup()) {
				TermConceptMapGroup termConceptMapGroup;
				for (ConceptMap.ConceptMapGroupComponent group : theConceptMap.getGroup()) {
					if (isBlank(group.getSource())) {
						throw new UnprocessableEntityException("ConceptMap[url='" + theConceptMap.getUrl() + "'] contains at least one group without a value in ConceptMap.group.source");
					}
					if (isBlank(group.getTarget())) {
						throw new UnprocessableEntityException("ConceptMap[url='" + theConceptMap.getUrl() + "'] contains at least one group without a value in ConceptMap.group.target");
					}
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
							if (isBlank(element.getCode())) {
								// FIXME: JA - send this to an interceptor message so it can be output
								continue;
							}
							termConceptMapGroupElement = new TermConceptMapGroupElement();
							termConceptMapGroupElement.setConceptMapGroup(termConceptMapGroup);
							termConceptMapGroupElement.setCode(element.getCode());
							termConceptMapGroupElement.setDisplay(element.getDisplay());
							myConceptMapGroupElementDao.save(termConceptMapGroupElement);

							if (element.hasTarget()) {
								TermConceptMapGroupElementTarget termConceptMapGroupElementTarget;
								for (ConceptMap.TargetElementComponent elementTarget : element.getTarget()) {
									termConceptMapGroupElementTarget = new TermConceptMapGroupElementTarget();
									termConceptMapGroupElementTarget.setConceptMapGroupElement(termConceptMapGroupElement);
									termConceptMapGroupElementTarget.setCode(elementTarget.getCode());
									termConceptMapGroupElementTarget.setDisplay(elementTarget.getDisplay());
									termConceptMapGroupElementTarget.setEquivalence(elementTarget.getEquivalence());
									myConceptMapGroupElementTargetDao.save(termConceptMapGroupElementTarget);

									if (codesSaved++ % 250 == 0) {
										ourLog.info("Have saved {} codes in ConceptMap", codesSaved);
										myConceptMapGroupElementTargetDao.flush();
									}
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
				existingTermConceptMap.getResource().getIdDt().toUnqualifiedVersionless().getValue());

			throw new UnprocessableEntityException(msg);
		}

		ourLog.info("Done storing TermConceptMap.");
	}

	@Scheduled(fixedDelay = 600000) // 10 minutes.
	@Override
	public synchronized void preExpandDeferredValueSetsToTerminologyTables() {
		if (isNotSafeToPreExpandValueSets()) {
			ourLog.info("Skipping scheduled pre-expansion of ValueSets while deferred entities are being loaded.");
			return;
		}
		TransactionTemplate txTemplate = new TransactionTemplate(myTxManager);

		while (true) {
			TermValueSet valueSetToExpand = txTemplate.execute(t -> {
				Optional<TermValueSet> optionalTermValueSet = getNextTermValueSetNotExpanded();
				if (!optionalTermValueSet.isPresent()) {
					return null;
				}

				TermValueSet termValueSet = optionalTermValueSet.get();
				termValueSet.setExpansionStatus(TermValueSetPreExpansionStatusEnum.EXPANSION_IN_PROGRESS);
				return myValueSetDao.saveAndFlush(termValueSet);
			});
			if (valueSetToExpand == null) {
				return;
			}

			// We have a ValueSet to pre-expand.
			try {
				ValueSet valueSet = txTemplate.execute(t -> {
					TermValueSet refreshedValueSetToExpand = myValueSetDao.findById(valueSetToExpand.getId()).get();
					return getValueSetFromResourceTable(refreshedValueSetToExpand.getResource());
				});
				expandValueSet(valueSet, new ValueSetConceptAccumulator(valueSetToExpand, myValueSetConceptDao, myValueSetConceptDesignationDao));

				// We are done with this ValueSet.
				txTemplate.execute(t -> {
					valueSetToExpand.setExpansionStatus(TermValueSetPreExpansionStatusEnum.EXPANDED);
					myValueSetDao.saveAndFlush(valueSetToExpand);
					return null;
				});

			} catch (Exception e) {
				ourLog.error("Failed to pre-expand ValueSet: " + e.getMessage(), e);
				txTemplate.execute(t -> {
					valueSetToExpand.setExpansionStatus(TermValueSetPreExpansionStatusEnum.FAILED_TO_EXPAND);
					myValueSetDao.saveAndFlush(valueSetToExpand);
					return null;
				});
			}
		}
	}

	private boolean isNotSafeToPreExpandValueSets() {
		return !isSafeToPreExpandValueSets();
	}

	private boolean isSafeToPreExpandValueSets() {
		if (isProcessDeferredPaused()) {
			return false;
		}

		if (isDeferredConcepts()) {
			return false;
		}

		if (isConceptLinksToSaveLater()) {
			return false;
		}

		if (isDeferredValueSets()) {
			return false;
		}

		if (isDeferredConceptMaps()) {
			return false;
		}

		return true;
	}

	protected abstract ValueSet getValueSetFromResourceTable(ResourceTable theResourceTable);

	private Optional<TermValueSet> getNextTermValueSetNotExpanded() {
		Optional<TermValueSet> retVal = Optional.empty();
		Slice<TermValueSet> page = myValueSetDao.findByExpansionStatus(PageRequest.of(0, 1), TermValueSetPreExpansionStatusEnum.NOT_EXPANDED);

		if (!page.getContent().isEmpty()) {
			retVal = Optional.of(page.getContent().get(0));
		}

		return retVal;
	}

	@Override
	@Transactional
	public void storeTermValueSet(ResourceTable theResourceTable, ValueSet theValueSet) {
		ourLog.info("Storing TermValueSet {}", theValueSet.getIdElement().getValue());

		ValidateUtil.isTrueOrThrowInvalidRequest(theResourceTable != null, "No resource supplied");
		ValidateUtil.isNotBlankOrThrowUnprocessableEntity(theValueSet.getUrl(), "ValueSet has no value for ValueSet.url");

		TermValueSet termValueSet = new TermValueSet();
		termValueSet.setResource(theResourceTable);
		termValueSet.setUrl(theValueSet.getUrl());
		termValueSet.setName(theValueSet.hasName() ? theValueSet.getName() : null);

		// We delete old versions; we don't support versioned ValueSets.
		deleteValueSet(theResourceTable);

		/*
		 * Do the upload.
		 */
		String url = termValueSet.getUrl();
		Optional<TermValueSet> optionalExistingTermValueSetByUrl = myValueSetDao.findByUrl(url);
		if (!optionalExistingTermValueSetByUrl.isPresent()) {

			myValueSetDao.save(termValueSet);

		} else {
			TermValueSet existingTermValueSet = optionalExistingTermValueSetByUrl.get();

			String msg = myContext.getLocalizer().getMessage(
				BaseHapiTerminologySvcImpl.class,
				"cannotCreateDuplicateValueSetUrl",
				url,
				existingTermValueSet.getResource().getIdDt().toUnqualifiedVersionless().getValue());

			throw new UnprocessableEntityException(msg);
		}

		ourLog.info("Done storing TermValueSet.");
	}

	@Override
	@Transactional
	public IFhirResourceDaoCodeSystem.SubsumesResult subsumes(IPrimitiveType<String> theCodeA, IPrimitiveType<String> theCodeB, IPrimitiveType<String> theSystem, IBaseCoding theCodingA, IBaseCoding theCodingB) {
		VersionIndependentConcept conceptA = toConcept(theCodeA, theSystem, theCodingA);
		VersionIndependentConcept conceptB = toConcept(theCodeB, theSystem, theCodingB);

		if (!StringUtils.equals(conceptA.getSystem(), conceptB.getSystem())) {
			throw new InvalidRequestException("Unable to test subsumption across different code systems");
		}

		TermConcept codeA = findCode(conceptA.getSystem(), conceptA.getCode())
			.orElseThrow(() -> new InvalidRequestException("Unknown code: " + conceptA));

		TermConcept codeB = findCode(conceptB.getSystem(), conceptB.getCode())
			.orElseThrow(() -> new InvalidRequestException("Unknown code: " + conceptB));

		FullTextEntityManager em = org.hibernate.search.jpa.Search.getFullTextEntityManager(myEntityManager);

		ConceptSubsumptionOutcome subsumes;
		subsumes = testForSubsumption(em, codeA, codeB, ConceptSubsumptionOutcome.SUBSUMES);
		if (subsumes == null) {
			subsumes = testForSubsumption(em, codeB, codeA, ConceptSubsumptionOutcome.SUBSUMEDBY);
		}
		if (subsumes == null) {
			subsumes = ConceptSubsumptionOutcome.NOTSUBSUMED;
		}

		return new IFhirResourceDaoCodeSystem.SubsumesResult(subsumes);
	}

	@Transactional
	@Override
	public AtomicInteger applyDeltaCodesystemsAdd(String theSystem, @Nullable String theParent, CodeSystem theValue) {
		TermCodeSystem cs = getCodeSystem(theSystem);
		if (cs == null) {
			List<CodeSystem.ConceptDefinitionComponent> codes = theValue.getConcept();
			theValue.setConcept(null);
			createOrUpdateCodeSystem(theValue);
			cs = getCodeSystem(theSystem);
			theValue.setConcept(codes);
		}

		TermCodeSystemVersion csv = cs.getCurrentVersion();

		AtomicInteger addedCodeCounter = new AtomicInteger(0);

		TermConcept parentCode = null;
		if (isNotBlank(theParent)) {
			parentCode = myConceptDao
				.findByCodeSystemAndCode(csv, theParent)
				.orElseThrow(() -> new InvalidRequestException("Unknown code [" + theSystem + "|" + theParent + "]"));
		}

		List<TermConcept> concepts = new ArrayList<>();
		for (CodeSystem.ConceptDefinitionComponent next : theValue.getConcept()) {
			TermConcept concept = toTermConcept(next, csv);
			if (parentCode != null) {
				parentCode.addChild(concept, RelationshipTypeEnum.ISA);
			}
			concepts.add(concept);
		}

		// The first pass just saves any concepts that were added to the
		// root of the CodeSystem
		List<TermConceptParentChildLink> links = new ArrayList<>();
		for (TermConcept next : concepts) {
			int addedCount = saveOrUpdateConcept(next);
			addedCodeCounter.addAndGet(addedCount);
			extractLinksFromConceptAndChildren(next, links);
		}

		// This second pass saves any child concepts
		for (TermConceptParentChildLink next : links) {
			next.setCodeSystem(csv);
			int addedCount = saveOrUpdateConcept(next.getChild());
			addedCodeCounter.addAndGet(addedCount);
			myConceptParentChildLinkDao.save(next);
		}

		return addedCodeCounter;
	}

	@Transactional
	@Override
	public AtomicInteger applyDeltaCodesystemsRemove(String theSystem, CodeSystem theValue) {
		TermCodeSystem cs = getCodeSystem(theSystem);
		if (cs == null) {
			throw new InvalidRequestException("Unknown code system: " + theSystem);
		}

		AtomicInteger removeCounter = new AtomicInteger(0);

		for (CodeSystem.ConceptDefinitionComponent next : theValue.getConcept()) {
			Optional<TermConcept> conceptOpt = findCode(theSystem, next.getCode());
			if (conceptOpt.isPresent()) {
				TermConcept concept = conceptOpt.get();
				deleteConceptChildrenAndConcept(concept, removeCounter);
			}
		}

		return removeCounter;
	}

	private void deleteConceptChildrenAndConcept(TermConcept theConcept, AtomicInteger theRemoveCounter) {
		for (TermConceptParentChildLink nextChildLink : theConcept.getChildren()) {
			deleteConceptChildrenAndConcept(nextChildLink.getChild(), theRemoveCounter);
			myConceptParentChildLinkDao.delete(nextChildLink);
		}

		myConceptDesignationDao.deleteAll(theConcept.getDesignations());
		myConceptPropertyDao.deleteAll(theConcept.getProperties());
		myConceptDao.delete(theConcept);
		theRemoveCounter.incrementAndGet();
	}

	protected IContextValidationSupport.LookupCodeResult lookupCode(FhirContext theContext, String theSystem, String theCode) {
		TransactionTemplate txTemplate = new TransactionTemplate(myTransactionManager);
		return txTemplate.execute(t -> {
			Optional<TermConcept> codeOpt = findCode(theSystem, theCode);
			if (codeOpt.isPresent()) {
				TermConcept code = codeOpt.get();

				IContextValidationSupport.LookupCodeResult result = new IContextValidationSupport.LookupCodeResult();
				result.setCodeSystemDisplayName(code.getCodeSystemVersion().getCodeSystemDisplayName());
				result.setCodeSystemVersion(code.getCodeSystemVersion().getCodeSystemVersionId());
				result.setSearchedForSystem(theSystem);
				result.setSearchedForCode(theCode);
				result.setFound(true);
				result.setCodeDisplay(code.getDisplay());

				for (TermConceptDesignation next : code.getDesignations()) {
					IContextValidationSupport.ConceptDesignation designation = new IContextValidationSupport.ConceptDesignation();
					designation.setLanguage(next.getLanguage());
					designation.setUseSystem(next.getUseSystem());
					designation.setUseCode(next.getUseCode());
					designation.setUseDisplay(next.getUseDisplay());
					designation.setValue(next.getValue());
					result.getDesignations().add(designation);
				}

				for (TermConceptProperty next : code.getProperties()) {
					if (next.getType() == TermConceptPropertyTypeEnum.CODING) {
						IContextValidationSupport.CodingConceptProperty property = new IContextValidationSupport.CodingConceptProperty(next.getKey(), next.getCodeSystem(), next.getValue(), next.getDisplay());
						result.getProperties().add(property);
					} else if (next.getType() == TermConceptPropertyTypeEnum.STRING) {
						IContextValidationSupport.StringConceptProperty property = new IContextValidationSupport.StringConceptProperty(next.getKey(), next.getValue());
						result.getProperties().add(property);
					} else {
						throw new InternalErrorException("Unknown type: " + next.getType());
					}
				}

				return result;

			} else {
				return null;
			}
		});
	}

	private @Nullable
	ConceptSubsumptionOutcome testForSubsumption(FullTextEntityManager theEntityManager, TermConcept theLeft, TermConcept theRight, ConceptSubsumptionOutcome theOutput) {
		QueryBuilder qb = theEntityManager.getSearchFactory().buildQueryBuilder().forEntity(TermConcept.class).get();
		BooleanJunction<?> bool = qb.bool();
		bool.must(qb.keyword().onField("myId").matching(Long.toString(theLeft.getId())).createQuery());
		bool.must(qb.keyword().onField("myParentPids").matching(Long.toString(theRight.getId())).createQuery());
		Query luceneQuery = bool.createQuery();
		FullTextQuery jpaQuery = theEntityManager.createFullTextQuery(luceneQuery, TermConcept.class);
		jpaQuery.setMaxResults(1);
		if (jpaQuery.getResultList().size() > 0) {
			return theOutput;
		}
		return null;
	}

	@Override
	public boolean supportsSystem(String theSystem) {
		TermCodeSystem cs = getCodeSystem(theSystem);
		return cs != null;
	}

	private ArrayList<VersionIndependentConcept> toVersionIndependentConcepts(String theSystem, Set<TermConcept> codes) {
		ArrayList<VersionIndependentConcept> retVal = new ArrayList<>(codes.size());
		for (TermConcept next : codes) {
			retVal.add(new VersionIndependentConcept(theSystem, next.getCode()));
		}
		return retVal;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
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
				try (ScrollableResultsIterator<TermConceptMapGroupElementTarget> scrollableResultsIterator = new ScrollableResultsIterator<>(scrollableResults)) {

					while (scrollableResultsIterator.hasNext()) {
						targets.add(scrollableResultsIterator.next());
					}

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
	@Transactional(propagation = Propagation.REQUIRED)
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
				String targetCode;
				String targetCodeSystem = null;
				if (coding.hasCode()) {
					predicates.add(criteriaBuilder.equal(targetJoin.get("myCode"), coding.getCode()));
					targetCode = coding.getCode();
				} else {
					throw new InvalidRequestException("A code must be provided for translation to occur.");
				}

				if (coding.hasSystem()) {
					predicates.add(criteriaBuilder.equal(groupJoin.get("myTarget"), coding.getSystem()));
					targetCodeSystem = coding.getSystem();
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
				try (ScrollableResultsIterator<TermConceptMapGroupElement> scrollableResultsIterator = new ScrollableResultsIterator<>(scrollableResults)) {

					while (scrollableResultsIterator.hasNext()) {
						TermConceptMapGroupElement nextElement = scrollableResultsIterator.next();
						nextElement.getConceptMapGroupElementTargets().size();
						myEntityManager.detach(nextElement);

						if (isNotBlank(targetCode) && isNotBlank(targetCodeSystem)) {
							for (Iterator<TermConceptMapGroupElementTarget> iter = nextElement.getConceptMapGroupElementTargets().iterator(); iter.hasNext(); ) {
								TermConceptMapGroupElementTarget next = iter.next();
								if (targetCodeSystem.equals(next.getSystem())) {
									if (targetCode.equals(next.getCode())) {
										continue;
									}
								}

								iter.remove();
							}
						}

						elements.add(nextElement);
					}

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

	private static void extractLinksFromConceptAndChildren(TermConcept theConcept, List<TermConceptParentChildLink> theLinks) {
		theLinks.addAll(theConcept.getParents());
		for (TermConceptParentChildLink child : theConcept.getChildren()) {
			extractLinksFromConceptAndChildren(child.getChild(), theLinks);
		}
	}

	@NotNull
	private static VersionIndependentConcept toConcept(IPrimitiveType<String> theCodeType, IPrimitiveType<String> theSystemType, IBaseCoding theCodingType) {
		String code = theCodeType != null ? theCodeType.getValueAsString() : null;
		String system = theSystemType != null ? theSystemType.getValueAsString() : null;
		if (theCodingType != null) {
			code = theCodingType.getCode();
			system = theCodingType.getSystem();
		}
		return new VersionIndependentConcept(system, code);
	}

	/**
	 * This method is present only for unit tests, do not call from client code
	 */
	@VisibleForTesting
	public static void clearOurLastResultsFromTranslationCache() {
		ourLastResultsFromTranslationCache = false;
	}

	/**
	 * This method is present only for unit tests, do not call from client code
	 */
	@VisibleForTesting
	public static void clearOurLastResultsFromTranslationWithReverseCache() {
		ourLastResultsFromTranslationWithReverseCache = false;
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
