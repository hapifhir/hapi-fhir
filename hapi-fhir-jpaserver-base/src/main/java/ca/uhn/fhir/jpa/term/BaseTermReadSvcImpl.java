package ca.uhn.fhir.jpa.term;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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
import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValueSetExpansionOptions;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IDao;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoCodeSystem;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoValueSet;
import ca.uhn.fhir.jpa.api.model.TranslationQuery;
import ca.uhn.fhir.jpa.api.model.TranslationRequest;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.dao.IFulltextSearchSvc;
import ca.uhn.fhir.jpa.dao.data.*;
import ca.uhn.fhir.jpa.entity.*;
import ca.uhn.fhir.jpa.entity.TermConceptParentChildLink.RelationshipTypeEnum;
import ca.uhn.fhir.jpa.model.cross.ResourcePersistentId;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.sched.HapiJob;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.model.sched.ScheduledJobDefinition;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc;
import ca.uhn.fhir.jpa.term.api.ITermDeferredStorageSvc;
import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;
import ca.uhn.fhir.jpa.term.api.ITermReadSvc;
import ca.uhn.fhir.jpa.term.ex.ExpansionTooCostlyException;
import ca.uhn.fhir.jpa.util.ScrollableResultsIterator;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.util.StopWatch;
import ca.uhn.fhir.util.UrlUtil;
import ca.uhn.fhir.util.ValidateUtil;
import ca.uhn.fhir.util.VersionIndependentConcept;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Stopwatch;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.lucene.index.Term;
import org.apache.lucene.queries.TermsQuery;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.RegexpQuery;
import org.apache.lucene.search.TermQuery;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.query.dsl.BooleanJunction;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.*;
import org.hl7.fhir.r4.model.codesystems.ConceptSubsumptionOutcome;
import org.hl7.fhir.utilities.validation.ValidationOptions;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.NoRollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNoneBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public abstract class BaseTermReadSvcImpl implements ITermReadSvc {
	public static final int DEFAULT_FETCH_SIZE = 250;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseTermReadSvcImpl.class);
	private static final ValueSetExpansionOptions DEFAULT_EXPANSION_OPTIONS = new ValueSetExpansionOptions();
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
	@Autowired
	private ITermCodeSystemVersionDao myCodeSystemVersionDao;
	@Autowired
	private DaoConfig myDaoConfig;
	private Cache<TranslationQuery, List<TermConceptMapGroupElementTarget>> myTranslationCache;
	private Cache<TranslationQuery, List<TermConceptMapGroupElement>> myTranslationWithReverseCache;
	private int myFetchSize = DEFAULT_FETCH_SIZE;
	private TransactionTemplate myTxTemplate;
	@Autowired
	private PlatformTransactionManager myTransactionManager;
	@Autowired(required = false)
	private IFulltextSearchSvc myFulltextSearchSvc;
	@Autowired
	private PlatformTransactionManager myTxManager;
	@Autowired
	private ITermValueSetConceptViewDao myTermValueSetConceptViewDao;
	@Autowired
	private ISchedulerService mySchedulerService;
	@Autowired(required = false)
	private ITermDeferredStorageSvc myDeferredStorageSvc;
	@Autowired(required = false)
	private ITermCodeSystemStorageSvc myConceptStorageSvc;
	@Autowired
	private ApplicationContext myApplicationContext;
	private volatile IValidationSupport myJpaValidationSupport;
	private volatile IValidationSupport myValidationSupport;

	@Override
	public boolean isCodeSystemSupported(IValidationSupport theRootValidationSupport, String theSystem) {
		return supportsSystem(theSystem);
	}

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

	private void addConceptsToList(IValueSetConceptAccumulator theValueSetCodeAccumulator, Set<String> theAddedCodes, String theSystem, List<CodeSystem.ConceptDefinitionComponent> theConcept, boolean theAdd, VersionIndependentConcept theWantConceptOrNull) {
		for (CodeSystem.ConceptDefinitionComponent next : theConcept) {
			if (isNoneBlank(theSystem, next.getCode())) {
				if (theWantConceptOrNull == null || theWantConceptOrNull.getCode().equals(next.getCode())) {
					addOrRemoveCode(theValueSetCodeAccumulator, theAddedCodes, theAdd, theSystem, next.getCode(), next.getDisplay());
				}
			}
			addConceptsToList(theValueSetCodeAccumulator, theAddedCodes, theSystem, next.getConcept(), theAdd, theWantConceptOrNull);
		}
	}

	private boolean addToSet(Set<TermConcept> theSetToPopulate, TermConcept theConcept) {
		boolean retVal = theSetToPopulate.add(theConcept);
		if (retVal) {
			if (theSetToPopulate.size() >= myDaoConfig.getMaximumExpansionSize()) {
				String msg = myContext.getLocalizer().getMessage(BaseTermReadSvcImpl.class, "expansionTooLarge", myDaoConfig.getMaximumExpansionSize());
				throw new ExpansionTooCostlyException(msg);
			}
		}
		return retVal;
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
			myValueSetDao.deleteById(existingTermValueSet.getId());
			ourLog.info("Done deleting existing TermValueSet[{}] and its children.", existingTermValueSet.getId());
		}
	}

	@Override
	@Transactional
	public void deleteValueSetAndChildren(ResourceTable theResourceTable) {
		deleteValueSet(theResourceTable);
	}

	private ValueSet expandValueSetInMemory(ValueSetExpansionOptions theExpansionOptions, ValueSet theValueSetToExpand, VersionIndependentConcept theWantConceptOrNull) {

		int maxCapacity = myDaoConfig.getMaximumExpansionSize();
		ValueSetExpansionComponentWithConceptAccumulator expansionComponent = new ValueSetExpansionComponentWithConceptAccumulator(myContext, maxCapacity);
		expansionComponent.setIdentifier(UUID.randomUUID().toString());
		expansionComponent.setTimestamp(new Date());

		AtomicInteger codeCounter = new AtomicInteger(0);

		expandValueSet(theExpansionOptions, theValueSetToExpand, expansionComponent, codeCounter, theWantConceptOrNull);

		expansionComponent.setTotal(codeCounter.get());

		ValueSet valueSet = new ValueSet();
		valueSet.setStatus(Enumerations.PublicationStatus.ACTIVE);
		valueSet.setCompose(theValueSetToExpand.getCompose());
		valueSet.setExpansion(expansionComponent);
		return valueSet;
	}

	@Override
	public List<VersionIndependentConcept> expandValueSet(ValueSetExpansionOptions theExpansionOptions, String theValueSet) {
		// TODO: DM 2019-09-10 - This is problematic because an incorrect URL that matches ValueSet.id will not be found in the terminology tables but will yield a ValueSet here. Depending on the ValueSet, the expansion may time-out.

		ValueSet valueSet = fetchCanonicalValueSetFromCompleteContext(theValueSet);
		if (valueSet == null) {
			throwInvalidValueSet(theValueSet);
		}

		return expandValueSetAndReturnVersionIndependentConcepts(theExpansionOptions, valueSet, null);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ValueSet expandValueSet(ValueSetExpansionOptions theExpansionOptions, ValueSet theValueSetToExpand) {
		ValidateUtil.isNotNullOrThrowUnprocessableEntity(theValueSetToExpand, "ValueSet to expand can not be null");

		Optional<TermValueSet> optionalTermValueSet;
		if (theValueSetToExpand.hasUrl()) {
			optionalTermValueSet = myValueSetDao.findByUrl(theValueSetToExpand.getUrl());
		} else {
			optionalTermValueSet = Optional.empty();
		}

		if (!optionalTermValueSet.isPresent()) {
			ourLog.debug("ValueSet is not present in terminology tables. Will perform in-memory expansion without parameters. {}", getValueSetInfo(theValueSetToExpand));
			return expandValueSetInMemory(theExpansionOptions, theValueSetToExpand, null); // In-memory expansion.
		}

		TermValueSet termValueSet = optionalTermValueSet.get();

		if (termValueSet.getExpansionStatus() != TermValueSetPreExpansionStatusEnum.EXPANDED) {
			ourLog.warn("{} is present in terminology tables but not ready for persistence-backed invocation of operation $expand. Will perform in-memory expansion without parameters. Current status: {} | {}",
				getValueSetInfo(theValueSetToExpand), termValueSet.getExpansionStatus().name(), termValueSet.getExpansionStatus().getDescription());
			return expandValueSetInMemory(theExpansionOptions, theValueSetToExpand, null); // In-memory expansion.
		}

		ValueSet.ValueSetExpansionComponent expansionComponent = new ValueSet.ValueSetExpansionComponent();
		expansionComponent.setIdentifier(UUID.randomUUID().toString());
		expansionComponent.setTimestamp(new Date());

		ValueSetExpansionOptions expansionOptions = provideExpansionOptions(theExpansionOptions);
		int offset = expansionOptions.getOffset();
		int count = expansionOptions.getCount();
		populateExpansionComponent(expansionComponent, termValueSet, offset, count);

		ValueSet valueSet = new ValueSet();
		valueSet.setStatus(Enumerations.PublicationStatus.ACTIVE);
		valueSet.setCompose(theValueSetToExpand.getCompose());
		valueSet.setExpansion(expansionComponent);
		return valueSet;
	}

	private void populateExpansionComponent(ValueSet.ValueSetExpansionComponent theExpansionComponent, TermValueSet theTermValueSet, int theOffset, int theCount) {
		int total = theTermValueSet.getTotalConcepts().intValue();
		theExpansionComponent.setTotal(total);
		theExpansionComponent.setOffset(theOffset);
		theExpansionComponent.addParameter().setName("offset").setValue(new IntegerType(theOffset));
		theExpansionComponent.addParameter().setName("count").setValue(new IntegerType(theCount));

		if (theCount == 0) {
			return;
		}

		expandConcepts(theExpansionComponent, theTermValueSet, theOffset, theCount);
	}

	private void expandConcepts(ValueSet.ValueSetExpansionComponent theExpansionComponent, TermValueSet theTermValueSet, int theOffset, int theCount) {
		int conceptsExpanded = 0;
		int designationsExpanded = 0;
		int toIndex = theOffset + theCount;
		Collection<TermValueSetConceptView> conceptViews = myTermValueSetConceptViewDao.findByTermValueSetId(theOffset, toIndex, theTermValueSet.getId());

		if (conceptViews.isEmpty()) {
			logConceptsExpanded("No concepts to expand. ", theTermValueSet, conceptsExpanded);
			return;
		}

		Map<Long, ValueSet.ValueSetExpansionContainsComponent> pidToConcept = new HashMap<>();
		for (TermValueSetConceptView conceptView : conceptViews) {

			Long conceptPid = conceptView.getConceptPid();
			ValueSet.ValueSetExpansionContainsComponent containsComponent;

			if (!pidToConcept.containsKey(conceptPid)) {
				containsComponent = theExpansionComponent.addContains();
				containsComponent.setSystem(conceptView.getConceptSystemUrl());
				containsComponent.setCode(conceptView.getConceptCode());
				containsComponent.setDisplay(conceptView.getConceptDisplay());
				pidToConcept.put(conceptPid, containsComponent);
			} else {
				containsComponent = pidToConcept.get(conceptPid);
			}

			// TODO: DM 2019-08-17 - Implement includeDesignations parameter for $expand operation to designations optional.
			if (conceptView.getDesignationPid() != null) {
				ValueSet.ConceptReferenceDesignationComponent designationComponent = containsComponent.addDesignation();
				designationComponent.setLanguage(conceptView.getDesignationLang());
				designationComponent.setUse(new Coding(
					conceptView.getDesignationUseSystem(),
					conceptView.getDesignationUseCode(),
					conceptView.getDesignationUseDisplay()));
				designationComponent.setValue(conceptView.getDesignationVal());

				if (++designationsExpanded % 250 == 0) {
					logDesignationsExpanded("Expansion of designations in progress. ", theTermValueSet, designationsExpanded);
				}
			}

			if (++conceptsExpanded % 250 == 0) {
				logConceptsExpanded("Expansion of concepts in progress. ", theTermValueSet, conceptsExpanded);
			}
		}

		logDesignationsExpanded("Finished expanding designations. ", theTermValueSet, designationsExpanded);
		logConceptsExpanded("Finished expanding concepts. ", theTermValueSet, conceptsExpanded);
	}

	private void logConceptsExpanded(String theLogDescriptionPrefix, TermValueSet theTermValueSet, int theConceptsExpanded) {
		if (theConceptsExpanded > 0) {
			ourLog.debug("{}Have expanded {} concepts in ValueSet[{}]", theLogDescriptionPrefix, theConceptsExpanded, theTermValueSet.getUrl());
		}
	}

	private void logDesignationsExpanded(String theLogDescriptionPrefix, TermValueSet theTermValueSet, int theDesignationsExpanded) {
		if (theDesignationsExpanded > 0) {
			ourLog.debug("{}Have expanded {} designations in ValueSet[{}]", theLogDescriptionPrefix, theDesignationsExpanded, theTermValueSet.getUrl());
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void expandValueSet(ValueSetExpansionOptions theExpansionOptions, ValueSet theValueSetToExpand, IValueSetConceptAccumulator theValueSetCodeAccumulator) {
		expandValueSet(theExpansionOptions, theValueSetToExpand, theValueSetCodeAccumulator, new AtomicInteger(0), null);
	}

	@SuppressWarnings("ConstantConditions")
	private void expandValueSet(ValueSetExpansionOptions theExpansionOptions, ValueSet theValueSetToExpand, IValueSetConceptAccumulator theValueSetCodeAccumulator, AtomicInteger theCodeCounter, VersionIndependentConcept theWantConceptOrNull) {
		Set<String> addedCodes = new HashSet<>();

		StopWatch sw = new StopWatch();
		String valueSetInfo = getValueSetInfo(theValueSetToExpand);
		ourLog.debug("Working with {}", valueSetInfo);

		// Handle includes
		ourLog.debug("Handling includes");
		for (ValueSet.ConceptSetComponent include : theValueSetToExpand.getCompose().getInclude()) {
			for (int i = 0; ; i++) {
				int queryIndex = i;
				Boolean shouldContinue = myTxTemplate.execute(t -> {
					boolean add = true;
					return expandValueSetHandleIncludeOrExclude(theExpansionOptions, theValueSetCodeAccumulator, addedCodes, include, add, theCodeCounter, queryIndex, theWantConceptOrNull);
				});
				if (!shouldContinue) {
					break;
				}
			}
		}

		// If the accumulator filled up, abort
		if (theValueSetCodeAccumulator.getCapacityRemaining() != null && theValueSetCodeAccumulator.getCapacityRemaining() <= 0) {
			String msg = myContext.getLocalizer().getMessage(BaseTermReadSvcImpl.class, "expansionTooLarge", myDaoConfig.getMaximumExpansionSize());
			throw new ExpansionTooCostlyException(msg);
		}

		// Handle excludes
		ourLog.debug("Handling excludes");
		for (ValueSet.ConceptSetComponent exclude : theValueSetToExpand.getCompose().getExclude()) {
			for (int i = 0; ; i++) {
				int queryIndex = i;
				Boolean shouldContinue = myTxTemplate.execute(t -> {
					boolean add = false;
					return expandValueSetHandleIncludeOrExclude(theExpansionOptions, theValueSetCodeAccumulator, addedCodes, exclude, add, theCodeCounter, queryIndex, null);
				});
				if (!shouldContinue) {
					break;
				}
			}
		}

		if (theValueSetCodeAccumulator instanceof ValueSetConceptAccumulator) {
			myTxTemplate.execute(t -> ((ValueSetConceptAccumulator) theValueSetCodeAccumulator).removeGapsFromConceptOrder());
		}

		ourLog.debug("Done working with {} in {}ms", valueSetInfo, sw.getMillis());
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

	protected List<VersionIndependentConcept> expandValueSetAndReturnVersionIndependentConcepts(ValueSetExpansionOptions theExpansionOptions, ValueSet theValueSetToExpandR4, VersionIndependentConcept theWantConceptOrNull) {
		org.hl7.fhir.r4.model.ValueSet.ValueSetExpansionComponent expandedR4 = expandValueSetInMemory(theExpansionOptions, theValueSetToExpandR4, theWantConceptOrNull).getExpansion();

		ArrayList<VersionIndependentConcept> retVal = new ArrayList<>();
		for (org.hl7.fhir.r4.model.ValueSet.ValueSetExpansionContainsComponent nextContains : expandedR4.getContains()) {
			retVal.add(new VersionIndependentConcept(nextContains.getSystem(), nextContains.getCode(), nextContains.getDisplay()));
		}
		return retVal;
	}

	/**
	 * @return Returns true if there are potentially more results to process.
	 */
	private Boolean expandValueSetHandleIncludeOrExclude(@Nullable ValueSetExpansionOptions theExpansionOptions, IValueSetConceptAccumulator theValueSetCodeAccumulator, Set<String> theAddedCodes, ValueSet.ConceptSetComponent theIncludeOrExclude, boolean theAdd, AtomicInteger theCodeCounter, int theQueryIndex, VersionIndependentConcept theWantConceptOrNull) {

		String system = theIncludeOrExclude.getSystem();
		boolean hasSystem = isNotBlank(system);
		boolean hasValueSet = theIncludeOrExclude.getValueSet().size() > 0;

		if (hasSystem) {

			if (theWantConceptOrNull != null && theWantConceptOrNull.getSystem() != null && !system.equals(theWantConceptOrNull.getSystem())) {
				return false;
			}

			ourLog.debug("Starting {} expansion around CodeSystem: {}", (theAdd ? "inclusion" : "exclusion"), system);

			TermCodeSystem cs = myCodeSystemDao.findByCodeSystemUri(system);
			if (cs != null) {

				TermCodeSystemVersion csv = cs.getCurrentVersion();
				FullTextEntityManager em = org.hibernate.search.jpa.Search.getFullTextEntityManager(myEntityManager);

				/*
				 * If FullText searching is not enabled, we can handle only basic expansions
				 * since we're going to do it without the database.
				 */
				if (myFulltextSearchSvc == null) {
					expandWithoutHibernateSearch(theValueSetCodeAccumulator, csv, theAddedCodes, theIncludeOrExclude, system, theAdd, theCodeCounter);
					return false;
				}

				/*
				 * Ok, let's use hibernate search to build the expansion
				 */
				QueryBuilder qb = em.getSearchFactory().buildQueryBuilder().forEntity(TermConcept.class).get();
				BooleanJunction<?> bool = qb.bool();

				bool.must(qb.keyword().onField("myCodeSystemVersionPid").matching(csv.getPid()).createQuery());

				if (theWantConceptOrNull != null) {
					bool.must(qb.keyword().onField("myCode").matching(theWantConceptOrNull.getCode()).createQuery());
				}

				/*
				 * Filters
				 */
				handleFilters(bool, system, qb, theIncludeOrExclude);

				Query luceneQuery = bool.createQuery();

				/*
				 * Include/Exclude Concepts
				 */
				List<Term> codes = theIncludeOrExclude
					.getConcept()
					.stream()
					.filter(Objects::nonNull)
					.map(ValueSet.ConceptReferenceComponent::getCode)
					.filter(StringUtils::isNotBlank)
					.map(t -> new Term("myCode", t))
					.collect(Collectors.toList());
				if (codes.size() > 0) {

					BooleanQuery.Builder builder = new BooleanQuery.Builder();
					builder.setMinimumNumberShouldMatch(1);
					for (Term nextCode : codes) {
						builder.add(new TermQuery(nextCode), BooleanClause.Occur.SHOULD);
					}

					luceneQuery = new BooleanQuery.Builder()
						.add(luceneQuery, BooleanClause.Occur.MUST)
						.add(builder.build(), BooleanClause.Occur.MUST)
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

				/*
				 * If the accumulator is bounded, we may reduce the size of the query to
				 * Lucene in order to be more efficient.
				 */
				if (theAdd) {
					Integer accumulatorCapacityRemaining = theValueSetCodeAccumulator.getCapacityRemaining();
					if (accumulatorCapacityRemaining != null) {
						maxResultsPerBatch = Math.min(maxResultsPerBatch, accumulatorCapacityRemaining + 1);
					}
					if (maxResultsPerBatch <= 0) {
						return false;
					}
				}

				jpaQuery.setMaxResults(maxResultsPerBatch);
				jpaQuery.setFirstResult(theQueryIndex * maxResultsPerBatch);

				ourLog.debug("Beginning batch expansion for {} with max results per batch: {}", (theAdd ? "inclusion" : "exclusion"), maxResultsPerBatch);

				StopWatch swForBatch = new StopWatch();
				AtomicInteger countForBatch = new AtomicInteger(0);

				List resultList = jpaQuery.getResultList();
				int resultsInBatch = resultList.size();
				int firstResult = jpaQuery.getFirstResult();
				for (Object next : resultList) {
					count.incrementAndGet();
					countForBatch.incrementAndGet();
					TermConcept concept = (TermConcept) next;
					try {
						addCodeIfNotAlreadyAdded(theValueSetCodeAccumulator, theAddedCodes, concept, theAdd, theCodeCounter);
					} catch (ExpansionTooCostlyException e) {
						return false;
					}
				}

				ourLog.debug("Batch expansion for {} with starting index of {} produced {} results in {}ms", (theAdd ? "inclusion" : "exclusion"), firstResult, countForBatch, swForBatch.getMillis());

				if (resultsInBatch < maxResultsPerBatch) {
					ourLog.debug("Expansion for {} produced {} results in {}ms", (theAdd ? "inclusion" : "exclusion"), count, sw.getMillis());
					return false;
				} else {
					return true;
				}

			} else {
				// No CodeSystem matching the URL found in the database.

				CodeSystem codeSystemFromContext = fetchCanonicalCodeSystemFromCompleteContext(system);
				if (codeSystemFromContext == null) {
					String msg = myContext.getLocalizer().getMessage(BaseTermReadSvcImpl.class, "expansionRefersToUnknownCs", system);
					if (provideExpansionOptions(theExpansionOptions).isFailOnMissingCodeSystem()) {
						throw new PreconditionFailedException(msg);
					} else {
						ourLog.warn(msg);
						theValueSetCodeAccumulator.addMessage(msg);
						return false;
					}
				}

				if (!theIncludeOrExclude.getConcept().isEmpty()) {
					for (ValueSet.ConceptReferenceComponent next : theIncludeOrExclude.getConcept()) {
						String nextCode = next.getCode();
						if (theWantConceptOrNull == null || theWantConceptOrNull.getCode().equals(nextCode)) {
							if (isNoneBlank(system, nextCode) && !theAddedCodes.contains(system + "|" + nextCode)) {

								CodeSystem.ConceptDefinitionComponent code = findCode(codeSystemFromContext.getConcept(), nextCode);
								if (code != null) {
									String display = code.getDisplay();
									addOrRemoveCode(theValueSetCodeAccumulator, theAddedCodes, theAdd, system, nextCode, display);
								}

							}
						}
					}
				} else {
					List<CodeSystem.ConceptDefinitionComponent> concept = codeSystemFromContext.getConcept();
					addConceptsToList(theValueSetCodeAccumulator, theAddedCodes, system, concept, theAdd, theWantConceptOrNull);
				}

				return false;
			}
		} else if (hasValueSet) {

			for (CanonicalType nextValueSet : theIncludeOrExclude.getValueSet()) {
				ourLog.debug("Starting {} expansion around ValueSet: {}", (theAdd ? "inclusion" : "exclusion"), nextValueSet.getValueAsString());

				List<VersionIndependentConcept> expanded = expandValueSet(theExpansionOptions, nextValueSet.getValueAsString());
				Map<String, TermCodeSystem> uriToCodeSystem = new HashMap<>();

				for (VersionIndependentConcept nextConcept : expanded) {
					if (theAdd) {

						if (!uriToCodeSystem.containsKey(nextConcept.getSystem())) {
							TermCodeSystem codeSystem = myCodeSystemDao.findByCodeSystemUri(nextConcept.getSystem());
							uriToCodeSystem.put(nextConcept.getSystem(), codeSystem);
						}

						TermCodeSystem codeSystem = uriToCodeSystem.get(nextConcept.getSystem());
						if (codeSystem != null) {
							myConceptDao
								.findByCodeSystemAndCode(codeSystem.getCurrentVersion(), nextConcept.getCode())
								.ifPresent(concept ->
									addCodeIfNotAlreadyAdded(theValueSetCodeAccumulator, theAddedCodes, concept, theAdd, theCodeCounter)
								);
						} else {
							// This will happen if we're expanding against a built-in (part of FHIR) ValueSet that
							// isn't actually in the database anywhere
							Collection<TermConceptDesignation> emptyCollection = Collections.emptyList();
							addCodeIfNotAlreadyAdded(theValueSetCodeAccumulator, theAddedCodes, emptyCollection, theAdd, theCodeCounter, nextConcept.getSystem(), nextConcept.getCode(), nextConcept.getDisplay());
						}
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

	private @Nonnull
	ValueSetExpansionOptions provideExpansionOptions(@Nullable ValueSetExpansionOptions theExpansionOptions) {
		if (theExpansionOptions != null) {
			return theExpansionOptions;
		} else {
			return DEFAULT_EXPANSION_OPTIONS;
		}
	}

	private void addOrRemoveCode(IValueSetConceptAccumulator theValueSetCodeAccumulator, Set<String> theAddedCodes, boolean theAdd, String theSystem, String theCode, String theDisplay) {
		if (theAdd && theAddedCodes.add(theSystem + "|" + theCode)) {
			theValueSetCodeAccumulator.includeConcept(theSystem, theCode, theDisplay);
		}
		if (!theAdd && theAddedCodes.remove(theSystem + "|" + theCode)) {
			theValueSetCodeAccumulator.excludeConcept(theSystem, theCode);
		}
	}

	private void handleFilters(BooleanJunction<?> theBool, String theSystem, QueryBuilder theQb, ValueSet.ConceptSetComponent theIncludeOrExclude) {
		if (theIncludeOrExclude.getFilter().size() > 0) {
			for (ValueSet.ConceptSetFilterComponent nextFilter : theIncludeOrExclude.getFilter()) {
				handleFilter(theSystem, theQb, theBool, nextFilter);
			}
		}
	}

	private void handleFilter(String theSystem, QueryBuilder theQb, BooleanJunction<?> theBool, ValueSet.ConceptSetFilterComponent theFilter) {
		if (isBlank(theFilter.getValue()) && theFilter.getOp() == null && isBlank(theFilter.getProperty())) {
			return;
		}

		if (isBlank(theFilter.getValue()) || theFilter.getOp() == null || isBlank(theFilter.getProperty())) {
			throw new InvalidRequestException("Invalid filter, must have fields populated: property op value");
		}

		switch (theFilter.getProperty()) {
			case "display:exact":
			case "display":
				handleFilterDisplay(theQb, theBool, theFilter);
				break;
			case "concept":
			case "code":
				handleFilterConceptAndCode(theSystem, theQb, theBool, theFilter);
				break;
			case "parent":
			case "child":
				isCodeSystemLoingOrThrowInvalidRequestException(theSystem, theFilter.getProperty());
				handleFilterLoincParentChild(theQb, theBool, theFilter);
				break;
			case "ancestor":
				isCodeSystemLoingOrThrowInvalidRequestException(theSystem, theFilter.getProperty());
				handleFilterLoincAncestor(theSystem, theQb, theBool, theFilter);
				break;
			case "descendant":
				isCodeSystemLoingOrThrowInvalidRequestException(theSystem, theFilter.getProperty());
				handleFilterLoincDescendant(theSystem, theQb, theBool, theFilter);
				break;
			case "copyright":
				isCodeSystemLoingOrThrowInvalidRequestException(theSystem, theFilter.getProperty());
				handleFilterLoincCopyright(theQb, theBool, theFilter);
				break;
			default:
				handleFilterRegex(theBool, theFilter);
				break;
		}
	}

	private boolean isCodeSystemLoingOrThrowInvalidRequestException(String theSystem, String theProperty) {
		if (!isCodeSystemLoinc(theSystem)) {
			throw new InvalidRequestException("Invalid filter, property " + theProperty + " is LOINC-specific and cannot be used with system: " + theSystem);
		}
		return true;
	}

	private boolean isCodeSystemLoinc(String theSystem) {
		return ITermLoaderSvc.LOINC_URI.equals(theSystem);
	}

	private void handleFilterDisplay(QueryBuilder theQb, BooleanJunction<?> theBool, ValueSet.ConceptSetFilterComponent theFilter) {
		if (theFilter.getProperty().equals("display:exact") && theFilter.getOp() == ValueSet.FilterOperator.EQUAL) {
			addDisplayFilterExact(theQb, theBool, theFilter);
		} else if (theFilter.getProperty().equals("display") && theFilter.getOp() == ValueSet.FilterOperator.EQUAL) {
			if (theFilter.getValue().trim().contains(" ")) {
				addDisplayFilterExact(theQb, theBool, theFilter);
			} else {
				addDisplayFilterInexact(theQb, theBool, theFilter);
			}
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

	private void handleFilterConceptAndCode(String theSystem, QueryBuilder theQb, BooleanJunction<?> theBool, ValueSet.ConceptSetFilterComponent theFilter) {
		TermConcept code = findCode(theSystem, theFilter.getValue())
			.orElseThrow(() -> new InvalidRequestException("Invalid filter criteria - code does not exist: {" + Constants.codeSystemWithDefaultDescription(theSystem) + "}" + theFilter.getValue()));

		if (theFilter.getOp() == ValueSet.FilterOperator.ISA) {
			ourLog.debug(" * Filtering on codes with a parent of {}/{}/{}", code.getId(), code.getCode(), code.getDisplay());
			theBool.must(theQb.keyword().onField("myParentPids").matching("" + code.getId()).createQuery());
		} else {
			throw new InvalidRequestException("Don't know how to handle op=" + theFilter.getOp() + " on property " + theFilter.getProperty());
		}
	}

	@SuppressWarnings("EnumSwitchStatementWhichMissesCases")
	private void handleFilterLoincParentChild(QueryBuilder theQb, BooleanJunction<?> theBool, ValueSet.ConceptSetFilterComponent theFilter) {
		switch (theFilter.getOp()) {
			case EQUAL:
				addLoincFilterParentChildEqual(theBool, theFilter.getProperty(), theFilter.getValue());
				break;
			case IN:
				addLoincFilterParentChildIn(theBool, theFilter);
				break;
			default:
				throw new InvalidRequestException("Don't know how to handle op=" + theFilter.getOp() + " on property " + theFilter.getProperty());
		}
	}

	private void addLoincFilterParentChildEqual(BooleanJunction<?> theBool, String theProperty, String theValue) {
		logFilteringValueOnProperty(theValue, theProperty);
		theBool.must(new TermsQuery(getPropertyTerm(theProperty, theValue)));
	}

	private void addLoincFilterParentChildIn(BooleanJunction<?> theBool, ValueSet.ConceptSetFilterComponent theFilter) {
		String[] values = theFilter.getValue().split(",");
		List<Term> terms = new ArrayList<>();
		for (String value : values) {
			logFilteringValueOnProperty(value, theFilter.getProperty());
			terms.add(getPropertyTerm(theFilter.getProperty(), value));
		}
		theBool.must(new TermsQuery(terms));
	}

	private Term getPropertyTerm(String theProperty, String theValue) {
		return new Term(TermConceptPropertyFieldBridge.CONCEPT_FIELD_PROPERTY_PREFIX + theProperty, theValue);
	}

	@SuppressWarnings("EnumSwitchStatementWhichMissesCases")
	private void handleFilterLoincAncestor(String theSystem, QueryBuilder theQb, BooleanJunction<?> theBool, ValueSet.ConceptSetFilterComponent theFilter) {
		switch (theFilter.getOp()) {
			case EQUAL:
				addLoincFilterAncestorEqual(theSystem, theQb, theBool, theFilter);
				break;
			case IN:
				addLoincFilterAncestorIn(theSystem, theQb, theBool, theFilter);
				break;
			default:
				throw new InvalidRequestException("Don't know how to handle op=" + theFilter.getOp() + " on property " + theFilter.getProperty());
		}
	}

	private void addLoincFilterAncestorEqual(String theSystem, QueryBuilder theQb, BooleanJunction<?> theBool, ValueSet.ConceptSetFilterComponent theFilter) {
		addLoincFilterAncestorEqual(theSystem, theQb, theBool, theFilter.getProperty(), theFilter.getValue());
	}

	private void addLoincFilterAncestorEqual(String theSystem, QueryBuilder theQb, BooleanJunction<?> theBool, String theProperty, String theValue) {
		List<Term> terms = getAncestorTerms(theSystem, theProperty, theValue);
		theBool.must(new TermsQuery(terms));
	}

	private void addLoincFilterAncestorIn(String theSystem, QueryBuilder theQb, BooleanJunction<?> theBool, ValueSet.ConceptSetFilterComponent theFilter) {
		String[] values = theFilter.getValue().split(",");
		List<Term> terms = new ArrayList<>();
		for (String value : values) {
			terms.addAll(getAncestorTerms(theSystem, theFilter.getProperty(), value));
		}
		theBool.must(new TermsQuery(terms));
	}

	private List<Term> getAncestorTerms(String theSystem, String theProperty, String theValue) {
		List<Term> retVal = new ArrayList<>();

		TermConcept code = findCode(theSystem, theValue)
			.orElseThrow(() -> new InvalidRequestException("Invalid filter criteria - code does not exist: {" + Constants.codeSystemWithDefaultDescription(theSystem) + "}" + theValue));

		retVal.add(new Term("myParentPids", "" + code.getId()));
		logFilteringValueOnProperty(theValue, theProperty);

		return retVal;
	}

	@SuppressWarnings("EnumSwitchStatementWhichMissesCases")
	private void handleFilterLoincDescendant(String theSystem, QueryBuilder theQb, BooleanJunction<?> theBool, ValueSet.ConceptSetFilterComponent theFilter) {
		switch (theFilter.getOp()) {
			case EQUAL:
				addLoincFilterDescendantEqual(theSystem, theBool, theFilter);
				break;
			case IN:
				addLoincFilterDescendantIn(theSystem, theQb, theBool, theFilter);
				break;
			default:
				throw new InvalidRequestException("Don't know how to handle op=" + theFilter.getOp() + " on property " + theFilter.getProperty());
		}
	}

	private void addLoincFilterDescendantEqual(String theSystem, BooleanJunction<?> theBool, ValueSet.ConceptSetFilterComponent theFilter) {
		addLoincFilterDescendantEqual(theSystem, theBool, theFilter.getProperty(), theFilter.getValue());
	}

	private void addLoincFilterDescendantEqual(String theSystem, BooleanJunction<?> theBool, String theProperty, String theValue) {
		List<Term> terms = getDescendantTerms(theSystem, theProperty, theValue);
		theBool.must(new TermsQuery(terms));
	}

	private void addLoincFilterDescendantIn(String theSystem, QueryBuilder theQb, BooleanJunction<?> theBool, ValueSet.ConceptSetFilterComponent theFilter) {
		String[] values = theFilter.getValue().split(",");
		List<Term> terms = new ArrayList<>();
		for (String value : values) {
			terms.addAll(getDescendantTerms(theSystem, theFilter.getProperty(), value));
		}
		theBool.must(new TermsQuery(terms));
	}

	private List<Term> getDescendantTerms(String theSystem, String theProperty, String theValue) {
		List<Term> retVal = new ArrayList<>();

		TermConcept code = findCode(theSystem, theValue)
			.orElseThrow(() -> new InvalidRequestException("Invalid filter criteria - code does not exist: {" + Constants.codeSystemWithDefaultDescription(theSystem) + "}" + theValue));

		String[] parentPids = code.getParentPidsAsString().split(" ");
		for (String parentPid : parentPids) {
			retVal.add(new Term("myId", parentPid));
		}
		logFilteringValueOnProperty(theValue, theProperty);

		return retVal;
	}

	private void handleFilterLoincCopyright(QueryBuilder theQb, BooleanJunction<?> theBool, ValueSet.ConceptSetFilterComponent theFilter) {
		if (theFilter.getOp() == ValueSet.FilterOperator.EQUAL) {

			String copyrightFilterValue = defaultString(theFilter.getValue()).toLowerCase();
			switch (copyrightFilterValue) {
				case "3rdparty":
					logFilteringValueOnProperty(theFilter.getValue(), theFilter.getProperty());
					addFilterLoincCopyright3rdParty(theBool);
					break;
				case "loinc":
					logFilteringValueOnProperty(theFilter.getValue(), theFilter.getProperty());
					addFilterLoincCopyrightLoinc(theBool);
					break;
				default:
					throwInvalidRequestForValueOnProperty(theFilter.getValue(), theFilter.getProperty());
			}

		} else {
			throwInvalidRequestForOpOnProperty(theFilter.getOp(), theFilter.getProperty());
		}
	}

	private void addFilterLoincCopyright3rdParty(BooleanJunction<?> theBool) {
		theBool.must(getRegexQueryForFilterLoincCopyright());
	}

	private void addFilterLoincCopyrightLoinc(BooleanJunction<?> theBool) {
		theBool.must(getRegexQueryForFilterLoincCopyright()).not();
	}

	private RegexpQuery getRegexQueryForFilterLoincCopyright() {
		Term term = new Term(TermConceptPropertyFieldBridge.CONCEPT_FIELD_PROPERTY_PREFIX + "EXTERNAL_COPYRIGHT_NOTICE", ".*");
		return new RegexpQuery(term);
	}

	private void logFilteringValueOnProperty(String theValue, String theProperty) {
		ourLog.debug(" * Filtering with value={} on property {}", theValue, theProperty);
	}

	private void throwInvalidRequestForOpOnProperty(ValueSet.FilterOperator theOp, String theProperty) {
		throw new InvalidRequestException("Don't know how to handle op=" + theOp + " on property " + theProperty);
	}

	private void throwInvalidRequestForValueOnProperty(String theValue, String theProperty) {
		throw new InvalidRequestException("Don't know how to handle value=" + theValue + " on property " + theProperty);
	}

	private void handleFilterRegex(BooleanJunction<?> theBool, ValueSet.ConceptSetFilterComponent theFilter) {
		if (theFilter.getOp() == ValueSet.FilterOperator.REGEX) {

			/*
			 * We treat the regex filter as a match on the regex
			 * anywhere in the property string. The spec does not
			 * say whether or not this is the right behaviour, but
			 * there are examples that seem to suggest that it is.
			 */
			String value = theFilter.getValue();
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

			Term term = new Term(TermConceptPropertyFieldBridge.CONCEPT_FIELD_PROPERTY_PREFIX + theFilter.getProperty(), value);
			RegexpQuery query = new RegexpQuery(term);
			theBool.must(query);

		} else {

			String value = theFilter.getValue();
			Term term = new Term(TermConceptPropertyFieldBridge.CONCEPT_FIELD_PROPERTY_PREFIX + theFilter.getProperty(), value);
			theBool.must(new TermsQuery(term));

		}
	}

	private void expandWithoutHibernateSearch(IValueSetConceptAccumulator theValueSetCodeAccumulator, TermCodeSystemVersion theVersion, Set<String> theAddedCodes, ValueSet.ConceptSetComponent theInclude, String theSystem, boolean theAdd, AtomicInteger theCodeCounter) {
		ourLog.trace("Hibernate search is not enabled");

		if (theValueSetCodeAccumulator instanceof ValueSetExpansionComponentWithConceptAccumulator) {
			Validate.isTrue(((ValueSetExpansionComponentWithConceptAccumulator) theValueSetCodeAccumulator).getParameter().isEmpty(), "Can not expand ValueSet with parameters - Hibernate Search is not enabled on this server.");
		}

		Validate.isTrue(theInclude.getFilter().isEmpty(), "Can not expand ValueSet with filters - Hibernate Search is not enabled on this server.");
		Validate.isTrue(isNotBlank(theSystem), "Can not expand ValueSet without explicit system - Hibernate Search is not enabled on this server.");


		if (theInclude.getConcept().isEmpty()) {
			for (TermConcept next : theVersion.getConcepts()) {
				addCodeIfNotAlreadyAdded(theValueSetCodeAccumulator, theAddedCodes, null, theAdd, theCodeCounter, theSystem, next.getCode(), next.getDisplay());
			}
		}

		for (ValueSet.ConceptReferenceComponent next : theInclude.getConcept()) {
			if (!theSystem.equals(theInclude.getSystem()) && isNotBlank(theSystem)) {
				continue;
			}
			addCodeIfNotAlreadyAdded(theValueSetCodeAccumulator, theAddedCodes, null, theAdd, theCodeCounter, theSystem, next.getCode(), next.getDisplay());
		}


	}

	@Override
	public boolean isValueSetPreExpandedForCodeValidation(ValueSet theValueSet) {
		ResourcePersistentId valueSetResourcePid = myConceptStorageSvc.getValueSetResourcePid(theValueSet.getIdElement());
		Optional<TermValueSet> optionalTermValueSet = myValueSetDao.findByResourcePid(valueSetResourcePid.getIdAsLong());

		if (!optionalTermValueSet.isPresent()) {
			ourLog.warn("ValueSet is not present in terminology tables. Will perform in-memory code validation. {}", getValueSetInfo(theValueSet));
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

	protected IFhirResourceDaoValueSet.ValidateCodeResult validateCodeIsInPreExpandedValueSet(
		ValidationOptions theValidationOptions,
		ValueSet theValueSet, String theSystem, String theCode, String theDisplay, Coding theCoding, CodeableConcept theCodeableConcept) {

		ValidateUtil.isNotNullOrThrowUnprocessableEntity(theValueSet.hasId(), "ValueSet.id is required");
		ResourcePersistentId valueSetResourcePid = myConceptStorageSvc.getValueSetResourcePid(theValueSet.getIdElement());

		List<TermValueSetConcept> concepts = new ArrayList<>();
		if (isNotBlank(theCode)) {
			if (theValidationOptions.isGuessSystem()) {
				concepts.addAll(myValueSetConceptDao.findByValueSetResourcePidAndCode(valueSetResourcePid.getIdAsLong(), theCode));
			} else if (isNotBlank(theSystem)) {
				concepts.addAll(findByValueSetResourcePidSystemAndCode(valueSetResourcePid, theSystem, theCode));
			}
		} else if (theCoding != null) {
			if (theCoding.hasSystem() && theCoding.hasCode()) {
				concepts.addAll(findByValueSetResourcePidSystemAndCode(valueSetResourcePid, theCoding.getSystem(), theCoding.getCode()));
			}
		} else if (theCodeableConcept != null) {
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
				return new IFhirResourceDaoValueSet.ValidateCodeResult(true, "Validation succeeded", concept.getDisplay());
			}
		}

		if (!concepts.isEmpty()) {
			return new IFhirResourceDaoValueSet.ValidateCodeResult(true, "Validation succeeded", concepts.get(0).getDisplay());
		}

		return null;
	}

	private List<TermValueSetConcept> findByValueSetResourcePidSystemAndCode(ResourcePersistentId theResourcePid, String theSystem, String theCode) {
		List<TermValueSetConcept> retVal = new ArrayList<>();
		Optional<TermValueSetConcept> optionalTermValueSetConcept = myValueSetConceptDao.findByValueSetResourcePidSystemAndCode(theResourcePid.getIdAsLong(), theSystem, theCode);
		optionalTermValueSetConcept.ifPresent(retVal::add);
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
			TermCodeSystemVersion csv = null;
			TermCodeSystem cs = myCodeSystemDao.findByCodeSystemUri(theCodeSystem);
			if (cs != null && cs.getCurrentVersion() != null) {
				csv = cs.getCurrentVersion();
			}
			return myConceptDao.findByCodeSystemAndCode(csv, theCode);
		});
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

		ourLog.debug("Fetched {} codes above code {} in {}ms", retVal.size(), theCode, stopwatch.getMillis());
		return retVal;
	}

	@Transactional
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

		ourLog.debug("Fetched {} codes below code {} in {}ms", retVal.size(), theCode, stopwatch.elapsed(TimeUnit.MILLISECONDS));
		return retVal;
	}

	@Transactional
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

	private TermCodeSystem getCodeSystem(String theSystem) {
		return myCodeSystemDao.findByCodeSystemUri(theSystem);
	}

	@PostConstruct
	public void start() {
		RuleBasedTransactionAttribute rules = new RuleBasedTransactionAttribute();
		rules.getRollbackRules().add(new NoRollbackRuleAttribute(ExpansionTooCostlyException.class));
		myTxTemplate = new TransactionTemplate(myTransactionManager, rules);
		buildTranslationCaches();
		scheduleJob();
	}

	private void buildTranslationCaches() {
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

	public void scheduleJob() {
		// TODO KHS what does this mean?
		// Register scheduled job to pre-expand ValueSets
		// In the future it would be great to make this a cluster-aware task somehow
		ScheduledJobDefinition vsJobDefinition = new ScheduledJobDefinition();
		vsJobDefinition.setId(getClass().getName());
		vsJobDefinition.setJobClass(Job.class);
		mySchedulerService.scheduleClusteredJob(10 * DateUtils.MILLIS_PER_MINUTE, vsJobDefinition);
	}

	@Override
	@Transactional
	public void storeTermConceptMapAndChildren(ResourceTable theResourceTable, ConceptMap theConceptMap) {
		ourLog.debug("Storing TermConceptMap for {}", theConceptMap.getIdElement().toVersionless().getValueAsString());

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
			termConceptMap = myConceptMapDao.save(termConceptMap);
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

									if (++codesSaved % 250 == 0) {
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
				BaseTermReadSvcImpl.class,
				"cannotCreateDuplicateConceptMapUrl",
				conceptMapUrl,
				existingTermConceptMap.getResource().getIdDt().toUnqualifiedVersionless().getValue());

			throw new UnprocessableEntityException(msg);
		}

		ourLog.info("Done storing TermConceptMap[{}] for {}", termConceptMap.getId(), theConceptMap.getIdElement().toVersionless().getValueAsString());
	}

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
				expandValueSet(null, valueSet, new ValueSetConceptAccumulator(valueSetToExpand, myValueSetDao, myValueSetConceptDao, myValueSetConceptDesignationDao));

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
		return myDeferredStorageSvc != null && !myDeferredStorageSvc.isStorageQueueEmpty();
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
		ourLog.info("Storing TermValueSet for {}", theValueSet.getIdElement().toVersionless().getValueAsString());

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

			termValueSet = myValueSetDao.save(termValueSet);

		} else {
			TermValueSet existingTermValueSet = optionalExistingTermValueSetByUrl.get();

			String msg = myContext.getLocalizer().getMessage(
				BaseTermReadSvcImpl.class,
				"cannotCreateDuplicateValueSetUrl",
				url,
				existingTermValueSet.getResource().getIdDt().toUnqualifiedVersionless().getValue());

			throw new UnprocessableEntityException(msg);
		}

		ourLog.info("Done storing TermValueSet[{}] for {}", termValueSet.getId(), theValueSet.getIdElement().toVersionless().getValueAsString());
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

	protected abstract ValueSet toCanonicalValueSet(IBaseResource theValueSet);

	protected IValidationSupport.LookupCodeResult lookupCode(String theSystem, String theCode) {
		TransactionTemplate txTemplate = new TransactionTemplate(myTransactionManager);
		return txTemplate.execute(t -> {
			Optional<TermConcept> codeOpt = findCode(theSystem, theCode);
			if (codeOpt.isPresent()) {
				TermConcept code = codeOpt.get();

				IValidationSupport.LookupCodeResult result = new IValidationSupport.LookupCodeResult();
				result.setCodeSystemDisplayName(code.getCodeSystemVersion().getCodeSystemDisplayName());
				result.setCodeSystemVersion(code.getCodeSystemVersion().getCodeSystemVersionId());
				result.setSearchedForSystem(theSystem);
				result.setSearchedForCode(theCode);
				result.setFound(true);
				result.setCodeDisplay(code.getDisplay());

				for (TermConceptDesignation next : code.getDesignations()) {
					IValidationSupport.ConceptDesignation designation = new IValidationSupport.ConceptDesignation();
					designation.setLanguage(next.getLanguage());
					designation.setUseSystem(next.getUseSystem());
					designation.setUseCode(next.getUseCode());
					designation.setUseDisplay(next.getUseDisplay());
					designation.setValue(next.getValue());
					result.getDesignations().add(designation);
				}

				for (TermConceptProperty next : code.getProperties()) {
					if (next.getType() == TermConceptPropertyTypeEnum.CODING) {
						IValidationSupport.CodingConceptProperty property = new IValidationSupport.CodingConceptProperty(next.getKey(), next.getCodeSystem(), next.getValue(), next.getDisplay());
						result.getProperties().add(property);
					} else if (next.getType() == TermConceptPropertyTypeEnum.STRING) {
						IValidationSupport.StringConceptProperty property = new IValidationSupport.StringConceptProperty(next.getKey(), next.getValue());
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
								if (StringUtils.equals(targetCodeSystem, next.getSystem())) {
									if (StringUtils.equals(targetCode, next.getCode())) {
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

	void throwInvalidValueSet(String theValueSet) {
		throw new ResourceNotFoundException("Unknown ValueSet: " + UrlUtil.escapeUrlParam(theValueSet));
	}

	@Override
	public CodeValidationResult validateCodeInValueSet(IValidationSupport theRootValidationSupport, ConceptValidationOptions theOptions, String theCodeSystem, String theCode, String theDisplay, @Nonnull IBaseResource theValueSet) {

		IPrimitiveType<?> urlPrimitive = myContext.newTerser().getSingleValueOrNull(theValueSet, "url", IPrimitiveType.class);
		String url = urlPrimitive.getValueAsString();
		if (isNotBlank(url)) {
			return validateCode(theRootValidationSupport, theOptions, theCodeSystem, theCode, theDisplay, url);
		}
		return null;
	}

	Optional<VersionIndependentConcept> validateCodeInValueSet(IValidationSupport theValidationSupport, ConceptValidationOptions theValidationOptions, String theValueSetUrl, String theCodeSystem, String theCode) {
		IBaseResource valueSet = theValidationSupport.fetchValueSet(theValueSetUrl);

		// If we don't have a PID, this came from some source other than the JPA
		// database, so we don't need to check if it's pre-expanded or not
		if (valueSet instanceof IAnyResource) {
			Long pid = IDao.RESOURCE_PID.get((IAnyResource) valueSet);
			if (pid != null) {
				if (isValueSetPreExpandedForCodeValidation(valueSet)) {
					IFhirResourceDaoValueSet.ValidateCodeResult outcome = validateCodeIsInPreExpandedValueSet(new ValidationOptions(), valueSet, theCodeSystem, theCode, null, null, null);
					if (outcome != null && outcome.isResult()) {
						return Optional.of(new VersionIndependentConcept(theCodeSystem, theCode));
					}
				}
			}
		}

		ValueSet canonicalValueSet = toCanonicalValueSet(valueSet);
		VersionIndependentConcept wantConcept = new VersionIndependentConcept(theCodeSystem, theCode);
		ValueSetExpansionOptions expansionOptions = new ValueSetExpansionOptions()
			.setFailOnMissingCodeSystem(false);

		List<VersionIndependentConcept> expansionOutcome = expandValueSetAndReturnVersionIndependentConcepts(expansionOptions, canonicalValueSet, wantConcept);
		return expansionOutcome
			.stream()
			.filter(t -> (theValidationOptions.isInferSystem() || t.getSystem().equals(theCodeSystem)) && t.getCode().equals(theCode))
			.findFirst();
	}

	@Override
	public IBaseResource fetchCodeSystem(String theSystem) {
		IValidationSupport jpaValidationSupport = provideJpaValidationSupport();
		return jpaValidationSupport.fetchCodeSystem(theSystem);
	}

	@Override
	public CodeSystem fetchCanonicalCodeSystemFromCompleteContext(String theSystem) {
		IValidationSupport validationSupport = provideValidationSupport();
		IBaseResource codeSystem = validationSupport.fetchCodeSystem(theSystem);
		if (codeSystem != null) {
			codeSystem = toCanonicalCodeSystem(codeSystem);
		}
		return (CodeSystem) codeSystem;
	}

	@Nonnull
	private IValidationSupport provideJpaValidationSupport() {
		IValidationSupport jpaValidationSupport = myJpaValidationSupport;
		if (jpaValidationSupport == null) {
			jpaValidationSupport = myApplicationContext.getBean("myJpaValidationSupport", IValidationSupport.class);
			myJpaValidationSupport = jpaValidationSupport;
		}
		return jpaValidationSupport;
	}

	@Nonnull
	private IValidationSupport provideValidationSupport() {
		IValidationSupport validationSupport = myValidationSupport;
		if (validationSupport == null) {
			validationSupport = myApplicationContext.getBean(IValidationSupport.class);
			myValidationSupport = validationSupport;
		}
		return validationSupport;
	}

	public ValueSet fetchCanonicalValueSetFromCompleteContext(String theSystem) {
		IValidationSupport validationSupport = provideValidationSupport();
		IBaseResource valueSet = validationSupport.fetchValueSet(theSystem);
		if (valueSet != null) {
			valueSet = toCanonicalValueSet(valueSet);
		}
		return (ValueSet) valueSet;
	}

	protected abstract CodeSystem toCanonicalCodeSystem(IBaseResource theCodeSystem);

	@Override
	public IBaseResource fetchValueSet(String theValueSetUrl) {
		return provideJpaValidationSupport().fetchValueSet(theValueSetUrl);
	}

	@Override
	public FhirContext getFhirContext() {
		return myContext;
	}

	private void findCodesAbove(CodeSystem theSystem, String theSystemString, String theCode, List<VersionIndependentConcept> theListToPopulate) {
		List<CodeSystem.ConceptDefinitionComponent> conceptList = theSystem.getConcept();
		for (CodeSystem.ConceptDefinitionComponent next : conceptList) {
			addTreeIfItContainsCode(theSystemString, next, theCode, theListToPopulate);
		}
	}

	@Override
	public List<VersionIndependentConcept> findCodesAboveUsingBuiltInSystems(String theSystem, String theCode) {
		ArrayList<VersionIndependentConcept> retVal = new ArrayList<>();
		CodeSystem system = (CodeSystem) fetchCanonicalCodeSystemFromCompleteContext(theSystem);
		if (system != null) {
			findCodesAbove(system, theSystem, theCode, retVal);
		}
		return retVal;
	}

	private void findCodesBelow(CodeSystem theSystem, String theSystemString, String theCode, List<VersionIndependentConcept> theListToPopulate) {
		List<CodeSystem.ConceptDefinitionComponent> conceptList = theSystem.getConcept();
		findCodesBelow(theSystemString, theCode, theListToPopulate, conceptList);
	}

	private void findCodesBelow(String theSystemString, String theCode, List<VersionIndependentConcept> theListToPopulate, List<CodeSystem.ConceptDefinitionComponent> conceptList) {
		for (CodeSystem.ConceptDefinitionComponent next : conceptList) {
			if (theCode.equals(next.getCode())) {
				addAllChildren(theSystemString, next, theListToPopulate);
			} else {
				findCodesBelow(theSystemString, theCode, theListToPopulate, next.getConcept());
			}
		}
	}

	@Override
	public List<VersionIndependentConcept> findCodesBelowUsingBuiltInSystems(String theSystem, String theCode) {
		ArrayList<VersionIndependentConcept> retVal = new ArrayList<>();
		CodeSystem system = (CodeSystem) fetchCanonicalCodeSystemFromCompleteContext(theSystem);
		if (system != null) {
			findCodesBelow(system, theSystem, theCode, retVal);
		}
		return retVal;
	}

	private void addAllChildren(String theSystemString, CodeSystem.ConceptDefinitionComponent theCode, List<VersionIndependentConcept> theListToPopulate) {
		if (isNotBlank(theCode.getCode())) {
			theListToPopulate.add(new VersionIndependentConcept(theSystemString, theCode.getCode()));
		}
		for (CodeSystem.ConceptDefinitionComponent nextChild : theCode.getConcept()) {
			addAllChildren(theSystemString, nextChild, theListToPopulate);
		}
	}

	private boolean addTreeIfItContainsCode(String theSystemString, CodeSystem.ConceptDefinitionComponent theNext, String theCode, List<VersionIndependentConcept> theListToPopulate) {
		boolean foundCodeInChild = false;
		for (CodeSystem.ConceptDefinitionComponent nextChild : theNext.getConcept()) {
			foundCodeInChild |= addTreeIfItContainsCode(theSystemString, nextChild, theCode, theListToPopulate);
		}

		if (theCode.equals(theNext.getCode()) || foundCodeInChild) {
			theListToPopulate.add(new VersionIndependentConcept(theSystemString, theNext.getCode()));
			return true;
		}

		return false;
	}

	public static class Job implements HapiJob {
		@Autowired
		private ITermReadSvc myTerminologySvc;

		@Override
		public void execute(JobExecutionContext theContext) {
			myTerminologySvc.preExpandDeferredValueSetsToTerminologyTables();
		}
	}

	static List<TermConcept> toPersistedConcepts(List<CodeSystem.ConceptDefinitionComponent> theConcept, TermCodeSystemVersion theCodeSystemVersion) {
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
	static TermConcept toTermConcept(CodeSystem.ConceptDefinitionComponent theConceptDefinition, TermCodeSystemVersion theCodeSystemVersion) {
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


}
