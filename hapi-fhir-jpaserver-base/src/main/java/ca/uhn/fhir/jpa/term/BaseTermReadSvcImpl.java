package ca.uhn.fhir.jpa.term;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.context.support.ValueSetExpansionOptions;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IDao;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoCodeSystem;
import ca.uhn.fhir.jpa.api.model.TranslationQuery;
import ca.uhn.fhir.jpa.api.model.TranslationRequest;
import ca.uhn.fhir.jpa.config.HibernatePropertiesProvider;
import ca.uhn.fhir.jpa.dao.IFulltextSearchSvc;
import ca.uhn.fhir.jpa.dao.data.ITermCodeSystemDao;
import ca.uhn.fhir.jpa.dao.data.ITermCodeSystemVersionDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptDesignationDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptMapDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptMapGroupDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptMapGroupElementDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptMapGroupElementTargetDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptPropertyDao;
import ca.uhn.fhir.jpa.dao.data.ITermValueSetConceptDao;
import ca.uhn.fhir.jpa.dao.data.ITermValueSetConceptDesignationDao;
import ca.uhn.fhir.jpa.dao.data.ITermValueSetConceptViewDao;
import ca.uhn.fhir.jpa.dao.data.ITermValueSetDao;
import ca.uhn.fhir.jpa.entity.TermCodeSystem;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.entity.TermConceptDesignation;
import ca.uhn.fhir.jpa.entity.TermConceptMap;
import ca.uhn.fhir.jpa.entity.TermConceptMapGroup;
import ca.uhn.fhir.jpa.entity.TermConceptMapGroupElement;
import ca.uhn.fhir.jpa.entity.TermConceptMapGroupElementTarget;
import ca.uhn.fhir.jpa.entity.TermConceptParentChildLink;
import ca.uhn.fhir.jpa.entity.TermConceptParentChildLink.RelationshipTypeEnum;
import ca.uhn.fhir.jpa.entity.TermConceptProperty;
import ca.uhn.fhir.jpa.entity.TermConceptPropertyBinder;
import ca.uhn.fhir.jpa.entity.TermConceptPropertyTypeEnum;
import ca.uhn.fhir.jpa.entity.TermValueSet;
import ca.uhn.fhir.jpa.entity.TermValueSetConcept;
import ca.uhn.fhir.jpa.entity.TermValueSetConceptView;
import ca.uhn.fhir.jpa.entity.TermValueSetPreExpansionStatusEnum;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.sched.HapiJob;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.model.sched.ScheduledJobDefinition;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.search.builder.SearchBuilder;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc;
import ca.uhn.fhir.jpa.term.api.ITermDeferredStorageSvc;
import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;
import ca.uhn.fhir.jpa.term.api.ITermReadSvc;
import ca.uhn.fhir.jpa.term.ex.ExpansionTooCostlyException;
import ca.uhn.fhir.jpa.util.LogicUtil;
import ca.uhn.fhir.jpa.util.ScrollableResultsIterator;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.util.CoverageIgnore;
import ca.uhn.fhir.util.FhirVersionIndependentConcept;
import ca.uhn.fhir.util.HapiExtensions;
import ca.uhn.fhir.util.StopWatch;
import ca.uhn.fhir.util.UrlUtil;
import ca.uhn.fhir.util.ValidateUtil;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Stopwatch;
import com.google.common.collect.ArrayListMultimap;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.RegexpQuery;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.search.backend.elasticsearch.ElasticsearchExtension;
import org.hibernate.search.backend.lucene.LuceneExtension;
import org.hibernate.search.engine.search.predicate.dsl.BooleanPredicateClausesStep;
import org.hibernate.search.engine.search.predicate.dsl.PredicateFinalStep;
import org.hibernate.search.engine.search.predicate.dsl.SearchPredicateFactory;
import org.hibernate.search.engine.search.query.SearchQuery;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.hl7.fhir.common.hapi.validation.support.CommonCodeSystemsTerminologyService;
import org.hl7.fhir.common.hapi.validation.support.InMemoryTerminologyServerValidationSupport;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.instance.model.api.IBaseDatatype;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.CanonicalType;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.IntegerType;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.ValueSet;
import org.hl7.fhir.r4.model.codesystems.ConceptSubsumptionOutcome;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.NoRollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.support.TransactionSynchronizationManager;
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
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNoneBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.lowerCase;

public abstract class BaseTermReadSvcImpl implements ITermReadSvc {
	public static final int DEFAULT_FETCH_SIZE = 250;
	private static final int SINGLE_FETCH_SIZE = 1;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseTermReadSvcImpl.class);
	private static final ValueSetExpansionOptions DEFAULT_EXPANSION_OPTIONS = new ValueSetExpansionOptions();
	private static final TermCodeSystemVersion NO_CURRENT_VERSION = new TermCodeSystemVersion().setId(-1L);
	private static boolean ourLastResultsFromTranslationCache; // For testing.
	private static boolean ourLastResultsFromTranslationWithReverseCache; // For testing.
	private static Runnable myInvokeOnNextCallForUnitTest;
	private final int myFetchSize = DEFAULT_FETCH_SIZE;
	private final Cache<String, TermCodeSystemVersion> myCodeSystemCurrentVersionCache = Caffeine.newBuilder().expireAfterWrite(1, TimeUnit.MINUTES).build();
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

	//We need this bean so we can tell which mode hibernate search is running in.
	@Autowired
	private HibernatePropertiesProvider myHibernatePropertiesProvider;

	private boolean isFullTextSetToUseElastic() {
		return "elasticsearch".equalsIgnoreCase(myHibernatePropertiesProvider.getHibernateSearchBackend());
	}

	@Override
	public boolean isCodeSystemSupported(ValidationSupportContext theValidationSupportContext, String theSystem) {
		TermCodeSystemVersion cs = getCurrentCodeSystemVersion(theSystem);
		return cs != null;
	}

	private boolean addCodeIfNotAlreadyAdded(IValueSetConceptAccumulator theValueSetCodeAccumulator, Set<String> theAddedCodes, TermConcept theConcept, boolean theAdd, String theValueSetIncludeVersion) {
		String codeSystem = theConcept.getCodeSystemVersion().getCodeSystem().getCodeSystemUri();
		String code = theConcept.getCode();
		String display = theConcept.getDisplay();
		Collection<TermConceptDesignation> designations = theConcept.getDesignations();
		if (StringUtils.isNotEmpty(theValueSetIncludeVersion)) {
			return addCodeIfNotAlreadyAdded(theValueSetCodeAccumulator, theAddedCodes, designations, theAdd, codeSystem + "|" + theValueSetIncludeVersion, code, display);
		} else {
			return addCodeIfNotAlreadyAdded(theValueSetCodeAccumulator, theAddedCodes, designations, theAdd, codeSystem, code, display);
		}
	}

	private void addCodeIfNotAlreadyAdded(IValueSetConceptAccumulator theValueSetCodeAccumulator, Set<String> theAddedCodes, boolean theAdd, String theCodeSystem, String theCodeSystemVersion, String theCode, String theDisplay) {
		if (StringUtils.isNotEmpty(theCodeSystemVersion)) {
			if (isNoneBlank(theCodeSystem, theCode)) {
				if (theAdd && theAddedCodes.add(theCodeSystem + "|" + theCode)) {
					theValueSetCodeAccumulator.includeConceptWithDesignations(theCodeSystem + "|" + theCodeSystemVersion, theCode, theDisplay, null);
				}

				if (!theAdd && theAddedCodes.remove(theCodeSystem + "|" + theCode)) {
					theValueSetCodeAccumulator.excludeConcept(theCodeSystem + "|" + theCodeSystemVersion, theCode);
				}
			}
		} else {
			if (theAdd && theAddedCodes.add(theCodeSystem + "|" + theCode)) {
				theValueSetCodeAccumulator.includeConceptWithDesignations(theCodeSystem, theCode, theDisplay, null);
			}

			if (!theAdd && theAddedCodes.remove(theCodeSystem + "|" + theCode)) {
				theValueSetCodeAccumulator.excludeConcept(theCodeSystem, theCode);
			}
		}
	}

	private boolean addCodeIfNotAlreadyAdded(IValueSetConceptAccumulator theValueSetCodeAccumulator, Set<String> theAddedCodes, Collection<TermConceptDesignation> theDesignations, boolean theAdd, String theCodeSystem, String theCode, String theDisplay) {
		if (isNoneBlank(theCodeSystem, theCode)) {
			if (theAdd && theAddedCodes.add(theCodeSystem + "|" + theCode)) {
				theValueSetCodeAccumulator.includeConceptWithDesignations(theCodeSystem, theCode, theDisplay, theDesignations);
				return true;
			}

			if (!theAdd && theAddedCodes.remove(theCodeSystem + "|" + theCode)) {
				theValueSetCodeAccumulator.excludeConcept(theCodeSystem, theCode);
				return true;
			}
		}

		return false;
	}

	private void addConceptsToList(IValueSetConceptAccumulator theValueSetCodeAccumulator, Set<String> theAddedCodes, String theSystem, List<CodeSystem.ConceptDefinitionComponent> theConcept, boolean theAdd, @Nonnull ExpansionFilter theExpansionFilter) {
		for (CodeSystem.ConceptDefinitionComponent next : theConcept) {
			if (isNoneBlank(theSystem, next.getCode())) {
				if (!theExpansionFilter.hasCode() || theExpansionFilter.getCode().equals(next.getCode())) {
					addOrRemoveCode(theValueSetCodeAccumulator, theAddedCodes, theAdd, theSystem, next.getCode(), next.getDisplay());
				}
			}
			addConceptsToList(theValueSetCodeAccumulator, theAddedCodes, theSystem, next.getConcept(), theAdd, theExpansionFilter);
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
	public void clearCaches() {
		myTranslationCache.invalidateAll();
		myTranslationWithReverseCache.invalidateAll();
		myCodeSystemCurrentVersionCache.invalidateAll();
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

	public void deleteValueSetForResource(ResourceTable theResourceTable) {
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
		deleteValueSetForResource(theResourceTable);
	}


	@Override
	@Transactional
	public List<FhirVersionIndependentConcept> expandValueSetIntoConceptList(@Nullable ValueSetExpansionOptions theExpansionOptions, @Nonnull String theValueSetCanonicalUrl) {
		String expansionFilter = null;
		// TODO: DM 2019-09-10 - This is problematic because an incorrect URL that matches ValueSet.id will not be found in the terminology tables but will yield a ValueSet here. Depending on the ValueSet, the expansion may time-out.

		ValueSet expanded = expandValueSet(theExpansionOptions, theValueSetCanonicalUrl, expansionFilter);

		ArrayList<FhirVersionIndependentConcept> retVal = new ArrayList<>();
		for (ValueSet.ValueSetExpansionContainsComponent nextContains : expanded.getExpansion().getContains()) {
			retVal.add(new FhirVersionIndependentConcept(nextContains.getSystem(), nextContains.getCode(), nextContains.getDisplay(), nextContains.getVersion()));
		}
		return retVal;
	}

	@Override
	@Transactional
	public ValueSet expandValueSet(@Nullable ValueSetExpansionOptions theExpansionOptions, @Nonnull String theValueSetCanonicalUrl, @Nullable String theExpansionFilter) {
		ValueSet valueSet = fetchCanonicalValueSetFromCompleteContext(theValueSetCanonicalUrl);
		if (valueSet == null) {
			throw new ResourceNotFoundException("Unknown ValueSet: " + UrlUtil.escapeUrlParam(theValueSetCanonicalUrl));
		}

		return expandValueSet(theExpansionOptions, valueSet, theExpansionFilter);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ValueSet expandValueSet(@Nullable ValueSetExpansionOptions theExpansionOptions, @Nonnull ValueSet theValueSetToExpand) {
		return expandValueSet(theExpansionOptions, theValueSetToExpand, (String) null);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ValueSet expandValueSet(@Nullable ValueSetExpansionOptions theExpansionOptions, @Nonnull ValueSet theValueSetToExpand, @Nullable String theFilter) {
		return expandValueSet(theExpansionOptions, theValueSetToExpand, ExpansionFilter.fromFilterString(theFilter));
	}

	private ValueSet expandValueSet(@Nullable ValueSetExpansionOptions theExpansionOptions, ValueSet theValueSetToExpand, ExpansionFilter theFilter) {
		ValidateUtil.isNotNullOrThrowUnprocessableEntity(theValueSetToExpand, "ValueSet to expand can not be null");

		ValueSetExpansionOptions expansionOptions = provideExpansionOptions(theExpansionOptions);
		int offset = expansionOptions.getOffset();
		int count = expansionOptions.getCount();

		ValueSetExpansionComponentWithConceptAccumulator accumulator = new ValueSetExpansionComponentWithConceptAccumulator(myContext, count);
		accumulator.setHardExpansionMaximumSize(myDaoConfig.getMaximumExpansionSize());
		accumulator.setSkipCountRemaining(offset);
		accumulator.setIdentifier(UUID.randomUUID().toString());
		accumulator.setTimestamp(new Date());
		accumulator.setOffset(offset);

		if (theExpansionOptions != null && isHibernateSearchEnabled()) {
			accumulator.addParameter().setName("offset").setValue(new IntegerType(offset));
			accumulator.addParameter().setName("count").setValue(new IntegerType(count));
		}


		expandValueSetIntoAccumulator(theValueSetToExpand, theExpansionOptions, accumulator, theFilter, true);

		if (accumulator.getTotalConcepts() != null) {
			accumulator.setTotal(accumulator.getTotalConcepts());
		}

		ValueSet valueSet = new ValueSet();
		valueSet.setStatus(Enumerations.PublicationStatus.ACTIVE);
		valueSet.setCompose(theValueSetToExpand.getCompose());
		valueSet.setExpansion(accumulator);

		for (String next : accumulator.getMessages()) {
			valueSet.getMeta().addExtension()
				.setUrl(HapiExtensions.EXT_VALUESET_EXPANSION_MESSAGE)
				.setValue(new StringType(next));
		}
		return valueSet;
	}

	private void expandValueSetIntoAccumulator(ValueSet theValueSetToExpand, ValueSetExpansionOptions theExpansionOptions, IValueSetConceptAccumulator theAccumulator, ExpansionFilter theFilter, boolean theAdd) {
		Optional<TermValueSet> optionalTermValueSet;
		if (theValueSetToExpand.hasUrl()) {
			if (theValueSetToExpand.hasVersion()) {
				optionalTermValueSet = myValueSetDao.findTermValueSetByUrlAndVersion(theValueSetToExpand.getUrl(), theValueSetToExpand.getVersion());
			} else {
				List<TermValueSet> termValueSets = myValueSetDao.findTermValueSetByUrl(PageRequest.of(0, 1), theValueSetToExpand.getUrl());
				if (termValueSets.size() > 0) {
					optionalTermValueSet = Optional.of(termValueSets.get(0));
				} else {
					optionalTermValueSet = Optional.empty();
				}
			}
		} else {
			optionalTermValueSet = Optional.empty();
		}

		/*
		 * ValueSet doesn't exist in pre-expansion database, so perform in-memory expansion
		 */
		if (!optionalTermValueSet.isPresent()) {
			ourLog.debug("ValueSet is not present in terminology tables. Will perform in-memory expansion without parameters. {}", getValueSetInfo(theValueSetToExpand));
			expandValueSet(theExpansionOptions, theValueSetToExpand, theAccumulator, theFilter);
			return;
		}

		/*
		 * ValueSet exists in pre-expansion database, but pre-expansion is not yet complete so perform in-memory expansion
		 */
		TermValueSet termValueSet = optionalTermValueSet.get();
		if (termValueSet.getExpansionStatus() != TermValueSetPreExpansionStatusEnum.EXPANDED) {
			String msg = myContext.getLocalizer().getMessage(BaseTermReadSvcImpl.class, "valueSetNotYetExpanded", getValueSetInfo(theValueSetToExpand), termValueSet.getExpansionStatus().name(), termValueSet.getExpansionStatus().getDescription());
			theAccumulator.addMessage(msg);
			expandValueSet(theExpansionOptions, theValueSetToExpand, theAccumulator, theFilter);
			return;
		}

		/*
		 * ValueSet is pre-expanded in database so let's use that
		 */
		expandConcepts(theAccumulator, termValueSet, theFilter, theAdd);
	}


	private void expandConcepts(IValueSetConceptAccumulator theAccumulator, TermValueSet theTermValueSet, ExpansionFilter theFilter, boolean theAdd) {
		Integer offset = theAccumulator.getSkipCountRemaining();
		offset = ObjectUtils.defaultIfNull(offset, 0);
		offset = Math.min(offset, theTermValueSet.getTotalConcepts().intValue());

		Integer count = theAccumulator.getCapacityRemaining();
		count = defaultIfNull(count, myDaoConfig.getMaximumExpansionSize());

		int conceptsExpanded = 0;
		int designationsExpanded = 0;
		int toIndex = offset + count;

		Collection<TermValueSetConceptView> conceptViews;
		boolean wasFilteredResult = false;
		String filterDisplayValue = null;
		if (!theFilter.getFilters().isEmpty() && JpaConstants.VALUESET_FILTER_DISPLAY.equals(theFilter.getFilters().get(0).getProperty()) && theFilter.getFilters().get(0).getOp() == ValueSet.FilterOperator.EQUAL) {
			filterDisplayValue = lowerCase(theFilter.getFilters().get(0).getValue().replace("%", "[%]"));
			String displayValue = "%" + lowerCase(filterDisplayValue) + "%";
			conceptViews = myTermValueSetConceptViewDao.findByTermValueSetId(theTermValueSet.getId(), displayValue);
			wasFilteredResult = true;
		} else {
			// TODO JA HS: I'm pretty sure we are overfetching here.  test says offset 3, count 4, but we are fetching index 3 -> 10 here, grabbing 7 concepts.
			//Specifically this test testExpandInline_IncludePreExpandedValueSetByUri_FilterOnDisplay_LeftMatch_SelectRange
			conceptViews = myTermValueSetConceptViewDao.findByTermValueSetId(offset, toIndex, theTermValueSet.getId());
			theAccumulator.consumeSkipCount(offset);
			if (theAdd) {
				theAccumulator.incrementOrDecrementTotalConcepts(true, theTermValueSet.getTotalConcepts().intValue());
			}
		}

		if (conceptViews.isEmpty()) {
			logConceptsExpanded("No concepts to expand. ", theTermValueSet, conceptsExpanded);
			return;
		}

		Map<Long, FhirVersionIndependentConcept> pidToConcept = new LinkedHashMap<>();
		ArrayListMultimap<Long, TermConceptDesignation> pidToDesignations = ArrayListMultimap.create();

		for (TermValueSetConceptView conceptView : conceptViews) {

			String system = conceptView.getConceptSystemUrl();
			String code = conceptView.getConceptCode();
			String display = conceptView.getConceptDisplay();

			//-- this is quick solution, may need to revisit
			if (!applyFilter(display, filterDisplayValue))
				continue;
				
			Long conceptPid = conceptView.getConceptPid();
			if (!pidToConcept.containsKey(conceptPid)) {
				FhirVersionIndependentConcept concept = new FhirVersionIndependentConcept(system, code, display);
				pidToConcept.put(conceptPid, concept);
			}

			// TODO: DM 2019-08-17 - Implement includeDesignations parameter for $expand operation to designations optional.
			if (conceptView.getDesignationPid() != null) {
				TermConceptDesignation designation = new TermConceptDesignation();
				designation.setUseSystem(conceptView.getDesignationUseSystem());
				designation.setUseCode(conceptView.getDesignationUseCode());
				designation.setUseDisplay(conceptView.getDesignationUseDisplay());
				designation.setValue(conceptView.getDesignationVal());
				designation.setLanguage(conceptView.getDesignationLang());
				pidToDesignations.put(conceptPid, designation);

				if (++designationsExpanded % 250 == 0) {
					logDesignationsExpanded("Expansion of designations in progress. ", theTermValueSet, designationsExpanded);
				}
			}

			if (++conceptsExpanded % 250 == 0) {
				logConceptsExpanded("Expansion of concepts in progress. ", theTermValueSet, conceptsExpanded);
			}
		}

		for (Long nextPid : pidToConcept.keySet()) {
			FhirVersionIndependentConcept concept = pidToConcept.get(nextPid);
			List<TermConceptDesignation> designations = pidToDesignations.get(nextPid);
			String system = concept.getSystem();
			String code = concept.getCode();
			String display = concept.getDisplay();

			if (theAdd) {
				if (theAccumulator.getCapacityRemaining() != null) {
					if (theAccumulator.getCapacityRemaining() == 0) {
						break;
					}
				}

				theAccumulator.includeConceptWithDesignations(system, code, display, designations);
			} else {
				boolean removed = theAccumulator.excludeConcept(system, code);
				if (removed) {
					theAccumulator.incrementOrDecrementTotalConcepts(false, 1);
				}
			}
		}

		if (wasFilteredResult && theAdd) {
			theAccumulator.incrementOrDecrementTotalConcepts(true, pidToConcept.size());
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

	public boolean applyFilter(final String theInput, final String thePrefixToken) {
		
		//-- safety check only, no need to apply filter
		if (theInput == null || thePrefixToken == null)
			return true;

		// -- sentence case
		if (org.apache.commons.lang3.StringUtils.startsWithIgnoreCase(theInput, thePrefixToken))
			return true;
		
		//-- token case
		// return true only e.g. the input is 'Body height', thePrefixToken is "he", or 'bo'
		StringTokenizer tok = new StringTokenizer(theInput);		
		while (tok.hasMoreTokens()) {
			if (org.apache.commons.lang3.StringUtils.startsWithIgnoreCase(tok.nextToken(), thePrefixToken))
				return true;
		}
		
		return false;
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void expandValueSet(ValueSetExpansionOptions theExpansionOptions, ValueSet theValueSetToExpand, IValueSetConceptAccumulator theValueSetCodeAccumulator) {
		expandValueSet(theExpansionOptions, theValueSetToExpand, theValueSetCodeAccumulator, ExpansionFilter.NO_FILTER);
	}

	@SuppressWarnings("ConstantConditions")
	private void expandValueSet(ValueSetExpansionOptions theExpansionOptions, ValueSet theValueSetToExpand, IValueSetConceptAccumulator theValueSetCodeAccumulator, @Nonnull ExpansionFilter theExpansionFilter) {
		Set<String> addedCodes = new HashSet<>();

		StopWatch sw = new StopWatch();
		String valueSetInfo = getValueSetInfo(theValueSetToExpand);
		ourLog.debug("Working with {}", valueSetInfo);

		// Offset can't be combined with excludes
		Integer skipCountRemaining = theValueSetCodeAccumulator.getSkipCountRemaining();
		if (skipCountRemaining != null && skipCountRemaining > 0) {
			if (theValueSetToExpand.getCompose().getExclude().size() > 0) {
				String msg = myContext.getLocalizer().getMessage(BaseTermReadSvcImpl.class, "valueSetNotYetExpanded_OffsetNotAllowed", valueSetInfo);
				throw new InvalidRequestException(msg);
			}
		}

		// Handle includes
		ourLog.debug("Handling includes");
		for (ValueSet.ConceptSetComponent include : theValueSetToExpand.getCompose().getInclude()) {
			for (int i = 0; ; i++) {
				int queryIndex = i;
				Boolean shouldContinue = executeInNewTransactionIfNeeded(() -> {
					boolean add = true;
					return expandValueSetHandleIncludeOrExclude(theExpansionOptions, theValueSetCodeAccumulator, addedCodes, include, add, queryIndex, theExpansionFilter);
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
				int queryIndex = i;
				Boolean shouldContinue = executeInNewTransactionIfNeeded(() -> {
					boolean add = false;
					ExpansionFilter expansionFilter = ExpansionFilter.NO_FILTER;
					return expandValueSetHandleIncludeOrExclude(theExpansionOptions, theValueSetCodeAccumulator, addedCodes, exclude, add, queryIndex, expansionFilter);
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

	/**
	 * Execute in a new transaction only if we aren't already in one. We do this because in some cases
	 * when performing a VS expansion we throw an {@link ExpansionTooCostlyException} and we don't want
	 * this to cause the TX to be marked a rollback prematurely.
	 */
	private <T> T executeInNewTransactionIfNeeded(Supplier<T> theAction) {
		if (TransactionSynchronizationManager.isSynchronizationActive()) {
			return theAction.get();
		}
		return myTxTemplate.execute(t -> theAction.get());
	}

	private String getValueSetInfo(ValueSet theValueSet) {
		StringBuilder sb = new StringBuilder();
		boolean isIdentified = false;
		if (theValueSet.hasUrl()) {
			isIdentified = true;
			sb
				.append("ValueSet.url[")
				.append(theValueSet.getUrl())
				.append("]");
		} else if (theValueSet.hasId()) {
			isIdentified = true;
			sb
				.append("ValueSet.id[")
				.append(theValueSet.getId())
				.append("]");
		}

		if (!isIdentified) {
			sb.append("Unidentified ValueSet");
		}

		return sb.toString();
	}

	/**
	 * @return Returns true if there are potentially more results to process.
	 */
	private Boolean expandValueSetHandleIncludeOrExclude(@Nullable ValueSetExpansionOptions theExpansionOptions, IValueSetConceptAccumulator theValueSetCodeAccumulator, Set<String> theAddedCodes, ValueSet.ConceptSetComponent theIncludeOrExclude, boolean theAdd, int theQueryIndex, @Nonnull ExpansionFilter theExpansionFilter) {

		String system = theIncludeOrExclude.getSystem();
		boolean hasSystem = isNotBlank(system);
		boolean hasValueSet = theIncludeOrExclude.getValueSet().size() > 0;

		if (hasSystem) {

			if (theExpansionFilter.hasCode() && theExpansionFilter.getSystem() != null && !system.equals(theExpansionFilter.getSystem())) {
				return false;
			}

			ourLog.debug("Starting {} expansion around CodeSystem: {}", (theAdd ? "inclusion" : "exclusion"), system);

			TermCodeSystem cs = myCodeSystemDao.findByCodeSystemUri(system);
			if (cs != null) {

				return expandValueSetHandleIncludeOrExcludeUsingDatabase(theValueSetCodeAccumulator, theAddedCodes, theIncludeOrExclude, theAdd, theQueryIndex, theExpansionFilter, system, cs);

			} else {

				if (theIncludeOrExclude.getConcept().size() > 0 && theExpansionFilter.hasCode()) {
					if (defaultString(theIncludeOrExclude.getSystem()).equals(theExpansionFilter.getSystem())) {
						if (theIncludeOrExclude.getConcept().stream().noneMatch(t -> t.getCode().equals(theExpansionFilter.getCode()))) {
							return false;
						}
					}
				}

				// No CodeSystem matching the URL found in the database.
				CodeSystem codeSystemFromContext = fetchCanonicalCodeSystemFromCompleteContext(system);
				if (codeSystemFromContext == null) {

					// This is a last ditch effort.. We don't have a CodeSystem resource for the desired CS, and we don't have
					// anything at all in the database that matches it. So let's try asking the validation support context
					// just in case there is a registered service that knows how to handle this. This can happen, for example,
					// if someone creates a valueset that includes UCUM codes, since we don't have a CodeSystem resource for those
					// but CommonCodeSystemsTerminologyService can validate individual codes.
					List<FhirVersionIndependentConcept> includedConcepts = null;
					if (theExpansionFilter.hasCode()) {
						includedConcepts = new ArrayList<>();
						includedConcepts.add(theExpansionFilter.toFhirVersionIndependentConcept());
					} else if (!theIncludeOrExclude.getConcept().isEmpty()) {
						includedConcepts = theIncludeOrExclude
							.getConcept()
							.stream()
							.map(t -> new FhirVersionIndependentConcept(theIncludeOrExclude.getSystem(), t.getCode()))
							.collect(Collectors.toList());
					}

					if (includedConcepts != null) {
						int foundCount = 0;
						for (FhirVersionIndependentConcept next : includedConcepts) {
							String nextSystem = next.getSystem();
							if (nextSystem == null) {
								nextSystem = system;
							}

							LookupCodeResult lookup = myValidationSupport.lookupCode(new ValidationSupportContext(provideValidationSupport()), nextSystem, next.getCode());
							if (lookup != null && lookup.isFound()) {
								addOrRemoveCode(theValueSetCodeAccumulator, theAddedCodes, theAdd, nextSystem, next.getCode(), lookup.getCodeDisplay());
								foundCount++;
							}
						}

						if (foundCount == includedConcepts.size()) {
							return false;
							// ELSE, we'll continue below and throw an exception
						}
					}

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
						if (!theExpansionFilter.hasCode() || theExpansionFilter.getCode().equals(nextCode)) {
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
					addConceptsToList(theValueSetCodeAccumulator, theAddedCodes, system, concept, theAdd, theExpansionFilter);
				}

				return false;
			}

		} else if (hasValueSet) {

			for (CanonicalType nextValueSet : theIncludeOrExclude.getValueSet()) {
				String valueSetUrl = nextValueSet.getValueAsString();
				ourLog.debug("Starting {} expansion around ValueSet: {}", (theAdd ? "inclusion" : "exclusion"), valueSetUrl);

				ExpansionFilter subExpansionFilter = new ExpansionFilter(theExpansionFilter, theIncludeOrExclude.getFilter(), theValueSetCodeAccumulator.getCapacityRemaining());

				// TODO: DM 2019-09-10 - This is problematic because an incorrect URL that matches ValueSet.id will not be found in the terminology tables but will yield a ValueSet here. Depending on the ValueSet, the expansion may time-out.

				ValueSet valueSet = fetchCanonicalValueSetFromCompleteContext(valueSetUrl);
				if (valueSet == null) {
					throw new ResourceNotFoundException("Unknown ValueSet: " + UrlUtil.escapeUrlParam(valueSetUrl));
				}

				expandValueSetIntoAccumulator(valueSet, theExpansionOptions, theValueSetCodeAccumulator, subExpansionFilter, theAdd);

			}

			return false;

		} else {
			throw new InvalidRequestException("ValueSet contains " + (theAdd ? "include" : "exclude") + " criteria with no system defined");
		}


	}

	private boolean isHibernateSearchEnabled() {
		return myFulltextSearchSvc != null;
	}

	@Nonnull
	private Boolean expandValueSetHandleIncludeOrExcludeUsingDatabase(IValueSetConceptAccumulator theValueSetCodeAccumulator, Set<String> theAddedCodes, ValueSet.ConceptSetComponent theIncludeOrExclude, boolean theAdd, int theQueryIndex, @Nonnull ExpansionFilter theExpansionFilter, String theSystem, TermCodeSystem theCs) {
		String includeOrExcludeVersion = theIncludeOrExclude.getVersion();
		TermCodeSystemVersion csv;
		if (isEmpty(includeOrExcludeVersion)) {
			csv = theCs.getCurrentVersion();
		} else {
			csv = myCodeSystemVersionDao.findByCodeSystemPidAndVersion(theCs.getPid(), includeOrExcludeVersion);
		}

		SearchSession searchSession = Search.session(myEntityManager);
		/*
		 * If FullText searching is not enabled, we can handle only basic expansions
		 * since we're going to do it without the database.
		 */
		if (!isHibernateSearchEnabled()) {
			expandWithoutHibernateSearch(theValueSetCodeAccumulator, csv, theAddedCodes, theIncludeOrExclude, theSystem, theAdd);
			return false;
		}

		/*
		 * Ok, let's use hibernate search to build the expansion
		 */
		//Manually building a predicate since we need to throw it around.
		SearchPredicateFactory predicate = searchSession.scope(TermConcept.class).predicate();

		//Build the top-level expansion on filters.
		PredicateFinalStep step = predicate.bool(b -> {
			b.must(predicate.match().field("myCodeSystemVersionPid").matching(csv.getPid()));

			if (theExpansionFilter.hasCode()) {
				b.must(predicate.match().field("myCode").matching(theExpansionFilter.getCode()));
			}

			String codeSystemUrlAndVersion = buildCodeSystemUrlAndVersion(theSystem, includeOrExcludeVersion);
			for (ValueSet.ConceptSetFilterComponent nextFilter : theIncludeOrExclude.getFilter()) {
				handleFilter(codeSystemUrlAndVersion, predicate, b, nextFilter);
			}
			for (ValueSet.ConceptSetFilterComponent nextFilter : theExpansionFilter.getFilters()) {
				handleFilter(codeSystemUrlAndVersion, predicate, b, nextFilter);
			}
		});

		PredicateFinalStep expansionStep = buildExpansionPredicate(theIncludeOrExclude, predicate);
		final PredicateFinalStep finishedQuery;
		if (expansionStep == null) {
			finishedQuery = step;
		} else {
			finishedQuery = predicate.bool().must(step).must(expansionStep);
		}

		/*
		 * DM 2019-08-21 - Processing slows after any ValueSets with many codes explicitly identified. This might
		 * be due to the dark arts that is memory management. Will monitor but not do anything about this right now.
		 */
		//BooleanQuery.setMaxClauseCount(SearchBuilder.getMaximumPageSize());
		//TODO GGG HS looks like we can't set max clause count, but it can be set server side.
		//BooleanQuery.setMaxClauseCount(10000);

		StopWatch sw = new StopWatch();
		AtomicInteger count = new AtomicInteger(0);

		int maxResultsPerBatch = SearchBuilder.getMaximumPageSize();

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

//		jpaQuery.setMaxResults(maxResultsPerBatch);
//		jpaQuery.setFirstResult(theQueryIndex * maxResultsPerBatch);

		ourLog.debug("Beginning batch expansion for {} with max results per batch: {}", (theAdd ? "inclusion" : "exclusion"), maxResultsPerBatch);

		StopWatch swForBatch = new StopWatch();
		AtomicInteger countForBatch = new AtomicInteger(0);

		SearchQuery<TermConcept> termConceptsQuery = searchSession.search(TermConcept.class)
			.where(f -> finishedQuery).toQuery();

		System.out.println("About to query:" +  termConceptsQuery.queryString());
		List<TermConcept> termConcepts = termConceptsQuery.fetchHits(theQueryIndex * maxResultsPerBatch, maxResultsPerBatch);


		int resultsInBatch = termConcepts.size();
		int firstResult = theQueryIndex * maxResultsPerBatch;// TODO GGG HS we lose the ability to check the index of the first result, so just best-guessing it here.
		int delta = 0;
		for (TermConcept concept: termConcepts) {
			count.incrementAndGet();
			countForBatch.incrementAndGet();
			boolean added = addCodeIfNotAlreadyAdded(theValueSetCodeAccumulator, theAddedCodes, concept, theAdd, includeOrExcludeVersion);
			if (added) {
				delta++;
			}
		}

		ourLog.debug("Batch expansion for {} with starting index of {} produced {} results in {}ms", (theAdd ? "inclusion" : "exclusion"), firstResult, countForBatch, swForBatch.getMillis());
		theValueSetCodeAccumulator.incrementOrDecrementTotalConcepts(theAdd, delta);

		if (resultsInBatch < maxResultsPerBatch) {
			ourLog.debug("Expansion for {} produced {} results in {}ms", (theAdd ? "inclusion" : "exclusion"), count, sw.getMillis());
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Helper method which builds a predicate for the expansion
	 */
	private PredicateFinalStep buildExpansionPredicate(ValueSet.ConceptSetComponent theTheIncludeOrExclude, SearchPredicateFactory thePredicate) {
		PredicateFinalStep expansionStep;
		/*
		 * Include/Exclude Concepts
		 */
		List<Term> codes = theTheIncludeOrExclude
			.getConcept()
			.stream()
			.filter(Objects::nonNull)
			.map(ValueSet.ConceptReferenceComponent::getCode)
			.filter(StringUtils::isNotBlank)
			.map(t -> new Term("myCode", t))
			.collect(Collectors.toList());

		if (codes.size() > 0) {
			expansionStep = thePredicate.bool(b -> {
				b.minimumShouldMatchNumber(1);
				for (Term code : codes) {
					b.should(thePredicate.match().field(code.field()).matching(code.text()));
				}
			});
			return expansionStep;
		} else {
			return null;
		}
	}

	private String buildCodeSystemUrlAndVersion(String theSystem, String theIncludeOrExcludeVersion) {
		String codeSystemUrlAndVersion;
		if (theIncludeOrExcludeVersion != null) {
			codeSystemUrlAndVersion = theSystem + "|" + theIncludeOrExcludeVersion;
		} else {
			codeSystemUrlAndVersion = theSystem;
		}
		return codeSystemUrlAndVersion;
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

	private void handleFilter(String theCodeSystemIdentifier, SearchPredicateFactory theF, BooleanPredicateClausesStep<?> theB, ValueSet.ConceptSetFilterComponent theFilter) {
		if (isBlank(theFilter.getValue()) && theFilter.getOp() == null && isBlank(theFilter.getProperty())) {
			return;
		}

		if (isBlank(theFilter.getValue()) || theFilter.getOp() == null || isBlank(theFilter.getProperty())) {
			throw new InvalidRequestException("Invalid filter, must have fields populated: property op value");
		}

		switch (theFilter.getProperty()) {
			case "display:exact":
			case "display":
				handleFilterDisplay(theF, theB, theFilter);
				break;
			case "concept":
			case "code":
				handleFilterConceptAndCode(theCodeSystemIdentifier, theF, theB, theFilter);
				break;
			case "parent":
			case "child":
				isCodeSystemLoincOrThrowInvalidRequestException(theCodeSystemIdentifier, theFilter.getProperty());
				handleFilterLoincParentChild(theF, theB, theFilter);
				break;
			case "ancestor":
				isCodeSystemLoincOrThrowInvalidRequestException(theCodeSystemIdentifier, theFilter.getProperty());
				handleFilterLoincAncestor2(theCodeSystemIdentifier, theF, theB, theFilter);
				break;
			case "descendant":
				isCodeSystemLoincOrThrowInvalidRequestException(theCodeSystemIdentifier, theFilter.getProperty());
				handleFilterLoincDescendant(theCodeSystemIdentifier, theF, theB, theFilter);
				break;
			case "copyright":
				isCodeSystemLoincOrThrowInvalidRequestException(theCodeSystemIdentifier, theFilter.getProperty());
				handleFilterLoincCopyright(theF, theB, theFilter);
				break;
			default:
				handleFilterRegex(theF, theB, theFilter);
				break;
		}
	}

	private void handleFilterRegex(SearchPredicateFactory theF, BooleanPredicateClausesStep<?> theB, ValueSet.ConceptSetFilterComponent theFilter) {
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

			Term term = new Term(TermConceptPropertyBinder.CONCEPT_FIELD_PROPERTY_PREFIX + theFilter.getProperty(), value);

			if (isFullTextSetToUseElastic()) {
				String regexpQuery = "{'regexp':{'" + term.field() + "':{'value':'" + term.text() + "'}}}";
				ourLog.debug("Build Elasticsearch Regexp Query: {}", regexpQuery);
				theB.must(theF.extension(ElasticsearchExtension.get()).fromJson(regexpQuery));
			} else {
				RegexpQuery query = new RegexpQuery(term);
				theB.must(theF.extension(LuceneExtension.get()).fromLuceneQuery(query));
			}
		} else {
			String value = theFilter.getValue();
			Term term = new Term(TermConceptPropertyBinder.CONCEPT_FIELD_PROPERTY_PREFIX + theFilter.getProperty(), value);
			theB.must(theF.match().field(term.field()).matching(term.text()));

		}
	}

	private void handleFilterLoincCopyright(SearchPredicateFactory theF, BooleanPredicateClausesStep<?> theB, ValueSet.ConceptSetFilterComponent theFilter) {
		if (theFilter.getOp() == ValueSet.FilterOperator.EQUAL) {

			String copyrightFilterValue = defaultString(theFilter.getValue()).toLowerCase();
			switch (copyrightFilterValue) {
				case "3rdparty":
					logFilteringValueOnProperty(theFilter.getValue(), theFilter.getProperty());
					addFilterLoincCopyright3rdParty(theF, theB);
					break;
				case "loinc":
					logFilteringValueOnProperty(theFilter.getValue(), theFilter.getProperty());
					addFilterLoincCopyrightLoinc(theF, theB);
					break;
				default:
					throwInvalidRequestForValueOnProperty(theFilter.getValue(), theFilter.getProperty());
			}

		} else {
			throwInvalidRequestForOpOnProperty(theFilter.getOp(), theFilter.getProperty());
		}
	}

	private void addFilterLoincCopyrightLoinc(SearchPredicateFactory thePredicateFactory, BooleanPredicateClausesStep<?> theBooleanClause) {
		theBooleanClause.mustNot(thePredicateFactory.exists().field(TermConceptPropertyBinder.CONCEPT_FIELD_PROPERTY_PREFIX + "EXTERNAL_COPYRIGHT_NOTICE"));
	}

	private void addFilterLoincCopyright3rdParty(SearchPredicateFactory thePredicateFactory, BooleanPredicateClausesStep<?> theBooleanClause) {
		//TODO GGG HS These used to be Term term = new Term(TermConceptPropertyBinder.CONCEPT_FIELD_PROPERTY_PREFIX + "EXTERNAL_COPYRIGHT_NOTICE", ".*");, which was lucene-specific.
		//TODO GGG HS ask diederik if this is equivalent.
		//This old .* regex is the same as an existence check on a field, which I've implemented here.
		theBooleanClause.must(thePredicateFactory.exists().field(TermConceptPropertyBinder.CONCEPT_FIELD_PROPERTY_PREFIX + "EXTERNAL_COPYRIGHT_NOTICE"));
	}

	private void handleFilterLoincAncestor2(String theSystem, SearchPredicateFactory f, BooleanPredicateClausesStep<?> b, ValueSet.ConceptSetFilterComponent theFilter) {
		switch (theFilter.getOp()) {
			case EQUAL:
				addLoincFilterAncestorEqual(theSystem, f, b, theFilter);
				break;
			case IN:
				addLoincFilterAncestorIn(theSystem, f, b, theFilter);
				break;
			default:
				throw new InvalidRequestException("Don't know how to handle op=" + theFilter.getOp() + " on property " + theFilter.getProperty());
		}

	}

	private void addLoincFilterAncestorEqual(String theSystem, SearchPredicateFactory f, BooleanPredicateClausesStep<?> b, ValueSet.ConceptSetFilterComponent theFilter) {
		addLoincFilterAncestorEqual(theSystem, f, b, theFilter.getProperty(), theFilter.getValue());
	}

	private void addLoincFilterAncestorEqual(String theSystem, SearchPredicateFactory f, BooleanPredicateClausesStep<?> b, String theProperty, String theValue) {
		List<Term> terms = getAncestorTerms(theSystem, theProperty, theValue);
		b.must(f.bool(innerB -> terms.forEach(term -> innerB.should(f.match().field(term.field()).matching(term.text())))));
	}

	private void addLoincFilterAncestorIn(String theSystem, SearchPredicateFactory f, BooleanPredicateClausesStep<?> b, ValueSet.ConceptSetFilterComponent theFilter) {
		String[] values = theFilter.getValue().split(",");
		List<Term> terms = new ArrayList<>();
		for (String value : values) {
			terms.addAll(getAncestorTerms(theSystem, theFilter.getProperty(), value));
		}
		b.must(f.bool(innerB -> terms.forEach(term -> innerB.should(f.match().field(term.field()).matching(term.text())))));

	}

	private void handleFilterLoincParentChild(SearchPredicateFactory f, BooleanPredicateClausesStep<?> b, ValueSet.ConceptSetFilterComponent theFilter) {
		switch (theFilter.getOp()) {
			case EQUAL:
				addLoincFilterParentChildEqual(f, b, theFilter.getProperty(), theFilter.getValue());
				break;
			case IN:
				addLoincFilterParentChildIn(f, b, theFilter);
				break;
			default:
				throw new InvalidRequestException("Don't know how to handle op=" + theFilter.getOp() + " on property " + theFilter.getProperty());
		}
	}

	private void addLoincFilterParentChildIn(SearchPredicateFactory f, BooleanPredicateClausesStep<?> b, ValueSet.ConceptSetFilterComponent theFilter) {
		String[] values = theFilter.getValue().split(",");
		List<Term> terms = new ArrayList<>();
		for (String value : values) {
			logFilteringValueOnProperty(value, theFilter.getProperty());
			terms.add(getPropertyTerm(theFilter.getProperty(), value));
		}

		//TODO GGG HS: Not sure if this is the right equivalent...seems to be no equivalent to `TermsQuery` in HS6.
		//Far as I'm aware, this is a single element of a MUST portion of a bool, which itself should contain a list of OR'ed options, e.g.
		// shape == round && color == (green || blue)
		b.must(f.bool(innerB -> terms.forEach(term -> innerB.should(f.match().field(term.field()).matching(term.text())))));
	}

	private void addLoincFilterParentChildEqual(SearchPredicateFactory f, BooleanPredicateClausesStep<?> b, String theProperty, String theValue) {
		logFilteringValueOnProperty(theValue, theProperty);
		//TODO GGG HS: Not sure if this is the right equivalent...seems to be no equivalent to `TermsQuery` in HS6.
		//b.must(new TermsQuery(getPropertyTerm(theProperty, theValue)));
		//According to the DSL migration reference (https://docs.jboss.org/hibernate/search/6.0/migration/html_single/#queries-reference),
		//Since this property is handled with a specific analyzer, I'm not sure a terms match here is actually correct. The analyzer is literally just a whitespace tokenizer here.

		b.must(f.match().field(TermConceptPropertyBinder.CONCEPT_FIELD_PROPERTY_PREFIX + theProperty).matching(theValue));
	}

	private void handleFilterConceptAndCode(String theSystem, SearchPredicateFactory f, BooleanPredicateClausesStep<?> b, ValueSet.ConceptSetFilterComponent theFilter) {


		TermConcept code = findCode(theSystem, theFilter.getValue())
			.orElseThrow(() -> new InvalidRequestException("Invalid filter criteria - code does not exist: {" + Constants.codeSystemWithDefaultDescription(theSystem) + "}" + theFilter.getValue()));

		if (theFilter.getOp() == ValueSet.FilterOperator.ISA) {
			ourLog.debug(" * Filtering on codes with a parent of {}/{}/{}", code.getId(), code.getCode(), code.getDisplay());

			b.must(f.match().field("myParentPids").matching("" + code.getId()));
		} else {
			throw new InvalidRequestException("Don't know how to handle op=" + theFilter.getOp() + " on property " + theFilter.getProperty());
		}
	}


	private void isCodeSystemLoincOrThrowInvalidRequestException(String theSystemIdentifier, String theProperty) {
		String systemUrl = getUrlFromIdentifier(theSystemIdentifier);
		if (!isCodeSystemLoinc(systemUrl)) {
			throw new InvalidRequestException("Invalid filter, property " + theProperty + " is LOINC-specific and cannot be used with system: " + systemUrl);
		}
	}

	private boolean isCodeSystemLoinc(String theSystem) {
		return ITermLoaderSvc.LOINC_URI.equals(theSystem);
	}

	private void handleFilterDisplay(SearchPredicateFactory f, BooleanPredicateClausesStep<?> b, ValueSet.ConceptSetFilterComponent theFilter) {
		if (theFilter.getProperty().equals("display:exact") && theFilter.getOp() == ValueSet.FilterOperator.EQUAL) {
			addDisplayFilterExact(f, b, theFilter);
		} else if (theFilter.getProperty().equals("display") && theFilter.getOp() == ValueSet.FilterOperator.EQUAL) {
			if (theFilter.getValue().trim().contains(" ")) {
				addDisplayFilterExact(f, b, theFilter);
			} else {
				addDisplayFilterInexact(f, b, theFilter);
			}
		}
	}

	private void addDisplayFilterExact(SearchPredicateFactory f, BooleanPredicateClausesStep<?> bool, ValueSet.ConceptSetFilterComponent nextFilter) {
		bool.must(f.phrase().field("myDisplay").matching(nextFilter.getValue()));
	}



	private void addDisplayFilterInexact(SearchPredicateFactory f, BooleanPredicateClausesStep<?> bool, ValueSet.ConceptSetFilterComponent nextFilter) {
		bool.must(f.phrase()
			.field("myDisplay").boost(4.0f)
			.field("myDisplayWordEdgeNGram").boost(1.0f)
			.field("myDisplayEdgeNGram").boost(1.0f)
			.matching(nextFilter.getValue().toLowerCase())
			.slop(2)
		);
	}

	private Term getPropertyTerm(String theProperty, String theValue) {
		return new Term(TermConceptPropertyBinder.CONCEPT_FIELD_PROPERTY_PREFIX + theProperty, theValue);
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
	private void handleFilterLoincDescendant(String theSystem, SearchPredicateFactory f, BooleanPredicateClausesStep<?> b, ValueSet.ConceptSetFilterComponent theFilter) {
		switch (theFilter.getOp()) {
			case EQUAL:
				addLoincFilterDescendantEqual(theSystem, f, b, theFilter);
				break;
			case IN:
				addLoincFilterDescendantIn(theSystem, f,b , theFilter);
				break;
			default:
				throw new InvalidRequestException("Don't know how to handle op=" + theFilter.getOp() + " on property " + theFilter.getProperty());
		}
	}


	private void addLoincFilterDescendantEqual(String theSystem, SearchPredicateFactory f, BooleanPredicateClausesStep<?> b, ValueSet.ConceptSetFilterComponent theFilter) {
		addLoincFilterDescendantEqual(theSystem, f, b, theFilter.getProperty(), theFilter.getValue());
	}

	private void addLoincFilterDescendantIn(String theSystem, SearchPredicateFactory f, BooleanPredicateClausesStep<?> b, ValueSet.ConceptSetFilterComponent theFilter) {
		String[] values = theFilter.getValue().split(",");
		List<Term> terms = new ArrayList<>();
		for (String value : values) {
			terms.addAll(getDescendantTerms(theSystem, theFilter.getProperty(), value));
		}
		searchByParentPids(f, b, terms);
	}

	private void addLoincFilterDescendantEqual(String theSystem, SearchPredicateFactory f, BooleanPredicateClausesStep<?> b, String theProperty, String theValue) {
		List<Term> terms = getDescendantTerms(theSystem, theProperty, theValue);
		searchByParentPids(f, b, terms);
	}

	private void searchByParentPids(SearchPredicateFactory f, BooleanPredicateClausesStep<?> b, List<Term> theTerms) {
		List<Long> parentPids = convertTermsToParentPids(theTerms);
		b.must(f.bool(innerB -> {
			parentPids.forEach(pid -> innerB.should(f.match().field(theTerms.get(0).field()).matching(pid)));
		}));
	}

	private List<Long> convertTermsToParentPids(List<Term> theTerms) {
		return theTerms.stream().map(Term::text).map(Long::valueOf).collect(Collectors.toList());
	}


	private List<Term> getDescendantTerms(String theSystem, String theProperty, String theValue) {
		List<Term> retVal = new ArrayList<>();

		TermConcept code = findCode(theSystem, theValue)
			.orElseThrow(() -> new InvalidRequestException("Invalid filter criteria - code does not exist: {" + Constants.codeSystemWithDefaultDescription(theSystem) + "}" + theValue));

		String[] parentPids = code.getParentPidsAsString().split(" ");
		for (String parentPid : parentPids) {
			if (!StringUtils.equals(parentPid, "NONE")) {
				retVal.add(new Term("myId", parentPid));
			}
		}
		logFilteringValueOnProperty(theValue, theProperty);

		return retVal;
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

	private void expandWithoutHibernateSearch(IValueSetConceptAccumulator theValueSetCodeAccumulator, TermCodeSystemVersion theVersion, Set<String> theAddedCodes, ValueSet.ConceptSetComponent theInclude, String theSystem, boolean theAdd) {
		ourLog.trace("Hibernate search is not enabled");

		if (theValueSetCodeAccumulator instanceof ValueSetExpansionComponentWithConceptAccumulator) {
			Validate.isTrue(((ValueSetExpansionComponentWithConceptAccumulator) theValueSetCodeAccumulator).getParameter().isEmpty(), "Can not expand ValueSet with parameters - Hibernate Search is not enabled on this server.");
		}

		Validate.isTrue(theInclude.getFilter().isEmpty(), "Can not expand ValueSet with filters - Hibernate Search is not enabled on this server.");
		Validate.isTrue(isNotBlank(theSystem), "Can not expand ValueSet without explicit system - Hibernate Search is not enabled on this server.");


		if (theInclude.getConcept().isEmpty()) {
			for (TermConcept next : theVersion.getConcepts()) {
				addCodeIfNotAlreadyAdded(theValueSetCodeAccumulator, theAddedCodes, theAdd, theSystem, theInclude.getVersion(), next.getCode(), next.getDisplay());
			}
		}

		for (ValueSet.ConceptReferenceComponent next : theInclude.getConcept()) {
			if (!theSystem.equals(theInclude.getSystem()) && isNotBlank(theSystem)) {
				continue;
			}
			addCodeIfNotAlreadyAdded(theValueSetCodeAccumulator, theAddedCodes, theAdd, theSystem, theInclude.getVersion(), next.getCode(), next.getDisplay());
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

	protected IValidationSupport.CodeValidationResult validateCodeIsInPreExpandedValueSet(
		ConceptValidationOptions theValidationOptions,
		ValueSet theValueSet, String theSystem, String theCode, String theDisplay, Coding theCoding, CodeableConcept theCodeableConcept) {

		ValidateUtil.isNotNullOrThrowUnprocessableEntity(theValueSet.hasId(), "ValueSet.id is required");
		ResourcePersistentId valueSetResourcePid = myConceptStorageSvc.getValueSetResourcePid(theValueSet.getIdElement());

		List<TermValueSetConcept> concepts = new ArrayList<>();
		if (isNotBlank(theCode)) {
			if (theValidationOptions.isInferSystem()) {
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
		} else {
			return null;
		}

		if (theValidationOptions.isValidateDisplay() && concepts.size() > 0) {
			for (TermValueSetConcept concept : concepts) {
				if (isBlank(theDisplay) || isBlank(concept.getDisplay()) || theDisplay.equals(concept.getDisplay())) {
					return new IValidationSupport.CodeValidationResult()
						.setCode(concept.getCode())
						.setDisplay(concept.getDisplay());
				}
			}

			return createFailureCodeValidationResult(theSystem, theCode, " - Concept Display \"" + theDisplay + "\" does not match expected \"" + concepts.get(0).getDisplay() + "\"").setDisplay(concepts.get(0).getDisplay());
		}

		if (!concepts.isEmpty()) {
			return new IValidationSupport.CodeValidationResult()
				.setCode(concepts.get(0).getCode())
				.setDisplay(concepts.get(0).getDisplay());
		}

		return createFailureCodeValidationResult(theSystem, theCode);
	}

	private CodeValidationResult createFailureCodeValidationResult(String theSystem, String theCode) {
		String append = "";
		return createFailureCodeValidationResult(theSystem, theCode, append);
	}

	private CodeValidationResult createFailureCodeValidationResult(String theSystem, String theCode, String theAppend) {
		return new CodeValidationResult()
			.setSeverity(IssueSeverity.ERROR)
			.setMessage("Unknown code {" + theSystem + "}" + theCode + theAppend);
	}

	private List<TermValueSetConcept> findByValueSetResourcePidSystemAndCode(ResourcePersistentId theResourcePid, String theSystem, String theCode) {
		List<TermValueSetConcept> retVal = new ArrayList<>();
		Optional<TermValueSetConcept> optionalTermValueSetConcept;
		int versionIndex = theSystem.indexOf("|");
		if (versionIndex >= 0) {
			String systemUrl = theSystem.substring(0, versionIndex);
			String systemVersion = theSystem.substring(versionIndex + 1);
			optionalTermValueSetConcept = myValueSetConceptDao.findByValueSetResourcePidSystemAndCodeWithVersion(theResourcePid.getIdAsLong(), systemUrl, systemVersion, theCode);
		} else {
			optionalTermValueSetConcept = myValueSetConceptDao.findByValueSetResourcePidSystemAndCode(theResourcePid.getIdAsLong(), theSystem, theCode);
		}
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
			TermCodeSystemVersion csv = getCurrentCodeSystemVersion(theCodeSystem);
			if (csv == null) {
				return Optional.empty();
			}
			return myConceptDao.findByCodeSystemAndCode(csv, theCode);
		});
	}

	@Nullable
	private TermCodeSystemVersion getCurrentCodeSystemVersion(String theCodeSystemIdentifier) {
		String version = getVersionFromIdentifier(theCodeSystemIdentifier);
		TermCodeSystemVersion retVal = myCodeSystemCurrentVersionCache.get(theCodeSystemIdentifier, t -> myTxTemplate.execute(tx -> {
			TermCodeSystemVersion csv = null;
			TermCodeSystem cs = myCodeSystemDao.findByCodeSystemUri(getUrlFromIdentifier(theCodeSystemIdentifier));
			if (cs != null) {
				if (version != null) {
					csv = myCodeSystemVersionDao.findByCodeSystemPidAndVersion(cs.getPid(), version);
				} else if (cs.getCurrentVersion() != null) {
					csv = cs.getCurrentVersion();
				}
			}
			if (csv != null) {
				return csv;
			} else {
				return NO_CURRENT_VERSION;
			}
		}));
		if (retVal == NO_CURRENT_VERSION) {
			return null;
		}
		return retVal;
	}

	private String getVersionFromIdentifier(String theUri) {
		String retVal = null;
		if (StringUtils.isNotEmpty((theUri))) {
			int versionSeparator = theUri.lastIndexOf('|');
			if (versionSeparator != -1) {
				retVal = theUri.substring(versionSeparator + 1);
			}
		}
		return retVal;
	}

	private String getUrlFromIdentifier(String theUri) {
		String retVal = theUri;
		if (StringUtils.isNotEmpty((theUri))) {
			int versionSeparator = theUri.lastIndexOf('|');
			if (versionSeparator != -1) {
				retVal = theUri.substring(0, versionSeparator);
			}
		}
		return retVal;
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
	public List<FhirVersionIndependentConcept> findCodesAbove(String theSystem, String theCode) {
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
	public List<FhirVersionIndependentConcept> findCodesBelow(String theSystem, String theCode) {
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
		termConceptMap.setVersion(theConceptMap.getVersion());

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

		if (source == null && theConceptMap.hasSourceCanonicalType()) {
			source = theConceptMap.getSourceCanonicalType().getValueAsString();
		}
		if (target == null && theConceptMap.hasTargetCanonicalType()) {
			target = theConceptMap.getTargetCanonicalType().getValueAsString();
		}

		/*
		 * For now we always delete old versions. At some point, it would be nice to allow configuration to keep old versions.
		 */
		deleteConceptMap(theResourceTable);

		/*
		 * Do the upload.
		 */
		String conceptMapUrl = termConceptMap.getUrl();
		String conceptMapVersion = termConceptMap.getVersion();
		Optional<TermConceptMap> optionalExistingTermConceptMapByUrl;
		if (isBlank(conceptMapVersion)) {
			optionalExistingTermConceptMapByUrl = myConceptMapDao.findTermConceptMapByUrlAndNullVersion(conceptMapUrl);
		} else {
			optionalExistingTermConceptMapByUrl = myConceptMapDao.findTermConceptMapByUrlAndVersion(conceptMapUrl, conceptMapVersion);
		}
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

					String groupSource = group.getSource();
					if (isBlank(groupSource)) {
						groupSource = source;
					}
					if (isBlank(groupSource)) {
						throw new UnprocessableEntityException("ConceptMap[url='" + theConceptMap.getUrl() + "'] contains at least one group without a value in ConceptMap.group.source");
					}

					String groupTarget = group.getTarget();
					if (isBlank(groupTarget)) {
						groupTarget = target;
					}
					if (isBlank(groupTarget)) {
						throw new UnprocessableEntityException("ConceptMap[url='" + theConceptMap.getUrl() + "'] contains at least one group without a value in ConceptMap.group.target");
					}

					termConceptMapGroup = new TermConceptMapGroup();
					termConceptMapGroup.setConceptMap(termConceptMap);
					termConceptMapGroup.setSource(groupSource);
					termConceptMapGroup.setSourceVersion(group.getSourceVersion());
					termConceptMapGroup.setTarget(groupTarget);
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
									if (isBlank(elementTarget.getCode())) {
										continue;
									}
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

			if (isBlank(conceptMapVersion)) {
				String msg = myContext.getLocalizer().getMessage(
					BaseTermReadSvcImpl.class,
					"cannotCreateDuplicateConceptMapUrl",
					conceptMapUrl,
					existingTermConceptMap.getResource().getIdDt().toUnqualifiedVersionless().getValue());
				throw new UnprocessableEntityException(msg);

			} else {
				String msg = myContext.getLocalizer().getMessage(
					BaseTermReadSvcImpl.class,
					"cannotCreateDuplicateConceptMapUrlAndVersion",
					conceptMapUrl, conceptMapVersion,
					existingTermConceptMap.getResource().getIdDt().toUnqualifiedVersionless().getValue());
				throw new UnprocessableEntityException(msg);
			}
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
			StopWatch sw = new StopWatch();
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
					TermValueSet refreshedValueSetToExpand = myValueSetDao.findById(valueSetToExpand.getId()).orElseThrow(() -> new IllegalStateException("Unknown VS ID: " + valueSetToExpand.getId()));
					return getValueSetFromResourceTable(refreshedValueSetToExpand.getResource());
				});
				assert valueSet != null;

				ValueSetConceptAccumulator accumulator = new ValueSetConceptAccumulator(valueSetToExpand, myValueSetDao, myValueSetConceptDao, myValueSetConceptDesignationDao);
				expandValueSet(null, valueSet, accumulator);

				// We are done with this ValueSet.
				txTemplate.execute(t -> {
					valueSetToExpand.setExpansionStatus(TermValueSetPreExpansionStatusEnum.EXPANDED);
					myValueSetDao.saveAndFlush(valueSetToExpand);
					return null;
				});

				ourLog.info("Pre-expanded ValueSet[{}] with URL[{}] - Saved {} concepts in {}", valueSet.getId(), valueSet.getUrl(), accumulator.getConceptsSaved(), sw.toString());

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

	@Override
	public CodeValidationResult validateCode(ConceptValidationOptions theOptions, IIdType theValueSetId, String theValueSetIdentifier, String theCodeSystemIdentifierToValidate, String theCodeToValidate, String theDisplayToValidate, IBaseDatatype theCodingToValidate, IBaseDatatype theCodeableConceptToValidate) {

		CodeableConcept codeableConcept = toCanonicalCodeableConcept(theCodeableConceptToValidate);
		boolean haveCodeableConcept = codeableConcept != null && codeableConcept.getCoding().size() > 0;

		Coding canonicalCodingToValidate = toCanonicalCoding(theCodingToValidate);
		boolean haveCoding = canonicalCodingToValidate != null && canonicalCodingToValidate.isEmpty() == false;

		boolean haveCode = theCodeToValidate != null && theCodeToValidate.isEmpty() == false;

		if (!haveCodeableConcept && !haveCoding && !haveCode) {
			throw new InvalidRequestException("No code, coding, or codeableConcept provided to validate");
		}
		if (!LogicUtil.multiXor(haveCodeableConcept, haveCoding, haveCode)) {
			throw new InvalidRequestException("$validate-code can only validate (system AND code) OR (coding) OR (codeableConcept)");
		}

		boolean haveIdentifierParam = isNotBlank(theValueSetIdentifier);
		String valueSetIdentifier;
		if (theValueSetId != null) {
			IBaseResource valueSet = myDaoRegistry.getResourceDao("ValueSet").read(theValueSetId);
			StringBuilder valueSetIdentifierBuilder = new StringBuilder(CommonCodeSystemsTerminologyService.getValueSetUrl(valueSet));
			String valueSetVersion = CommonCodeSystemsTerminologyService.getValueSetVersion(valueSet);
			if (valueSetVersion != null) {
				valueSetIdentifierBuilder.append("|").append(valueSetVersion);
			}
			valueSetIdentifier = valueSetIdentifierBuilder.toString();

		} else if (haveIdentifierParam) {
			valueSetIdentifier = theValueSetIdentifier;
		} else {
			throw new InvalidRequestException("Either ValueSet ID or ValueSet identifier or system and code must be provided. Unable to validate.");
		}

		ValidationSupportContext validationContext = new ValidationSupportContext(provideValidationSupport());

		String codeValueToValidate = theCodeToValidate;
		String codeSystemIdentifierValueToValidate = theCodeSystemIdentifierToValidate;
		String codeDisplayValueToValidate = theDisplayToValidate;

		if (haveCodeableConcept) {
			for (int i = 0; i < codeableConcept.getCoding().size(); i++) {
				Coding nextCoding = codeableConcept.getCoding().get(i);
				String codeSystemIdentifier;
				if (nextCoding.hasVersion()) {
					codeSystemIdentifier = nextCoding.getSystem() + "|" + nextCoding.getVersion();
				} else {
					codeSystemIdentifier = nextCoding.getSystem();
				}
				CodeValidationResult nextValidation = validateCode(validationContext, theOptions, codeSystemIdentifier, nextCoding.getCode(), nextCoding.getDisplay(), valueSetIdentifier);
				if (nextValidation.isOk() || i == codeableConcept.getCoding().size() - 1) {
					return nextValidation;
				}
			}
		} else if (haveCoding) {
			if (canonicalCodingToValidate.hasVersion()) {
				codeSystemIdentifierValueToValidate = canonicalCodingToValidate.getSystem() + "|" + canonicalCodingToValidate.getVersion();
			} else {
				codeSystemIdentifierValueToValidate = canonicalCodingToValidate.getSystem();
			}
			codeValueToValidate = canonicalCodingToValidate.getCode();
			codeDisplayValueToValidate = canonicalCodingToValidate.getDisplay();
		}

		return validateCode(validationContext, theOptions, codeSystemIdentifierValueToValidate, codeValueToValidate, codeDisplayValueToValidate, valueSetIdentifier);
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

		/*
		 * Get CodeSystem and validate CodeSystemVersion
		 */
		TermValueSet termValueSet = new TermValueSet();
		termValueSet.setResource(theResourceTable);
		termValueSet.setUrl(theValueSet.getUrl());
		termValueSet.setVersion(theValueSet.getVersion());
		termValueSet.setName(theValueSet.hasName() ? theValueSet.getName() : null);

		// Delete version being replaced
		deleteValueSetForResource(theResourceTable);

		/*
		 * Do the upload.
		 */
		String url = termValueSet.getUrl();
		String version = termValueSet.getVersion();
		Optional<TermValueSet> optionalExistingTermValueSetByUrl;
		if (version != null) {
			optionalExistingTermValueSetByUrl = myValueSetDao.findTermValueSetByUrlAndVersion(url, version);
		} else {
			optionalExistingTermValueSetByUrl = myValueSetDao.findTermValueSetByUrlAndNullVersion(url);
		}
		if (!optionalExistingTermValueSetByUrl.isPresent()) {

			myValueSetDao.save(termValueSet);

		} else {
			TermValueSet existingTermValueSet = optionalExistingTermValueSetByUrl.get();
			String msg;
			if (version != null) {
				msg = myContext.getLocalizer().getMessage(
					BaseTermReadSvcImpl.class,
					"cannotCreateDuplicateValueSetUrlAndVersion",
					url, version, existingTermValueSet.getResource().getIdDt().toUnqualifiedVersionless().getValue());
			} else {
				msg = myContext.getLocalizer().getMessage(
					BaseTermReadSvcImpl.class,
					"cannotCreateDuplicateValueSetUrl",
					url, existingTermValueSet.getResource().getIdDt().toUnqualifiedVersionless().getValue());
			}
			throw new UnprocessableEntityException(msg);
		}
	}


	@Override
	@Transactional
	public IFhirResourceDaoCodeSystem.SubsumesResult subsumes(IPrimitiveType<String> theCodeA, IPrimitiveType<String> theCodeB,
																				 IPrimitiveType<String> theSystem, IBaseCoding theCodingA, IBaseCoding theCodingB) {
		FhirVersionIndependentConcept conceptA = toConcept(theCodeA, theSystem, theCodingA);
		FhirVersionIndependentConcept conceptB = toConcept(theCodeB, theSystem, theCodingB);

		if (!StringUtils.equals(conceptA.getSystem(), conceptB.getSystem())) {
			throw new InvalidRequestException("Unable to test subsumption across different code systems");
		}

		if (!StringUtils.equals(conceptA.getSystemVersion(), conceptB.getSystemVersion())) {
			throw new InvalidRequestException("Unable to test subsumption across different code system versions");
		}

		String codeASystemIdentifier;
		if (StringUtils.isNotEmpty(conceptA.getSystemVersion())) {
			codeASystemIdentifier = conceptA.getSystem() + "|" + conceptA.getSystemVersion();
		} else {
			codeASystemIdentifier = conceptA.getSystem();
		}
		TermConcept codeA = findCode(codeASystemIdentifier, conceptA.getCode())
			.orElseThrow(() -> new InvalidRequestException("Unknown code: " + conceptA));

		String codeBSystemIdentifier;
		if (StringUtils.isNotEmpty(conceptB.getSystemVersion())) {
			codeBSystemIdentifier = conceptB.getSystem() + "|" + conceptB.getSystemVersion();
		} else {
			codeBSystemIdentifier = conceptB.getSystem();
		}
		TermConcept codeB = findCode(codeBSystemIdentifier, conceptB.getCode())
			.orElseThrow(() -> new InvalidRequestException("Unknown code: " + conceptB));

		SearchSession searchSession = Search.session(myEntityManager);

		ConceptSubsumptionOutcome subsumes;
		subsumes = testForSubsumption(searchSession, codeA, codeB, ConceptSubsumptionOutcome.SUBSUMES);
		if (subsumes == null) {
			subsumes = testForSubsumption(searchSession, codeB, codeA, ConceptSubsumptionOutcome.SUBSUMEDBY);
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

	@Nullable
	private ConceptSubsumptionOutcome testForSubsumption(SearchSession theSearchSession, TermConcept theLeft, TermConcept theRight, ConceptSubsumptionOutcome theOutput) {
		List<TermConcept> fetch = theSearchSession.search(TermConcept.class)
			.where(f -> f.bool()
				.must(f.match().field("myId").matching(theRight.getId()))
				.must(f.match().field("myParentPids").matching(Long.toString(theLeft.getId())))
			).fetchHits(1);

		if (fetch.size() > 0) {
			return theOutput;
		} else {
			return null;
		}
	}


	private ArrayList<FhirVersionIndependentConcept> toVersionIndependentConcepts(String theSystem, Set<TermConcept> codes) {
		ArrayList<FhirVersionIndependentConcept> retVal = new ArrayList<>(codes.size());
		for (TermConcept next : codes) {
			retVal.add(new FhirVersionIndependentConcept(theSystem, next.getCode()));
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

		//-- get the latest ConceptMapVersion if theTranslationRequest has ConceptMap url but no ConceptMap version
		String latestConceptMapVersion = null;
		if (theTranslationRequest.hasUrl() && !theTranslationRequest.hasConceptMapVersion())
			latestConceptMapVersion = getLatestConceptMapVersion(theTranslationRequest);

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

				if (translationQuery.hasUrl()) {
					predicates.add(criteriaBuilder.equal(conceptMapJoin.get("myUrl"), translationQuery.getUrl().getValueAsString()));
					if (translationQuery.hasConceptMapVersion()) {
						// both url and conceptMapVersion
						predicates.add(criteriaBuilder.equal(conceptMapJoin.get("myVersion"), translationQuery.getConceptMapVersion().getValueAsString()));
					} else {
						if (StringUtils.isNotBlank(latestConceptMapVersion)) {
							// only url and use latestConceptMapVersion
							predicates.add(criteriaBuilder.equal(conceptMapJoin.get("myVersion"), latestConceptMapVersion));
						} else {
							predicates.add(criteriaBuilder.isNull(conceptMapJoin.get("myVersion")));
						}
					}
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

		//-- get the latest ConceptMapVersion if theTranslationRequest has ConceptMap url but no ConceptMap version
		String latestConceptMapVersion = null;
		if (theTranslationRequest.hasUrl() && !theTranslationRequest.hasConceptMapVersion())
			latestConceptMapVersion = getLatestConceptMapVersion(theTranslationRequest);

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

				if (translationQuery.hasUrl()) {
					predicates.add(criteriaBuilder.equal(conceptMapJoin.get("myUrl"), translationQuery.getUrl().getValueAsString()));
					if (translationQuery.hasConceptMapVersion()) {
						// both url and conceptMapVersion
						predicates.add(criteriaBuilder.equal(conceptMapJoin.get("myVersion"), translationQuery.getConceptMapVersion().getValueAsString()));
					} else {
						if (StringUtils.isNotBlank(latestConceptMapVersion)) {
							// only url and use latestConceptMapVersion
							predicates.add(criteriaBuilder.equal(conceptMapJoin.get("myVersion"), latestConceptMapVersion));
						} else {
							predicates.add(criteriaBuilder.isNull(conceptMapJoin.get("myVersion")));
						}
					}
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
						// TODO: The invocation of the size() below does not seem to be necessary but for some reason, removing it causes tests in TerminologySvcImplR4Test to fail.
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

	// Special case for the translate operation with url and without
	// conceptMapVersion, find the latest conecptMapVersion
	private String getLatestConceptMapVersion(TranslationRequest theTranslationRequest) {

		Pageable page = PageRequest.of(0, 1);
		List<TermConceptMap> theConceptMapList = myConceptMapDao.getTermConceptMapEntitiesByUrlOrderByMostRecentUpdate(page,
			theTranslationRequest.getUrl().asStringValue());
		if (!theConceptMapList.isEmpty()) {
			return theConceptMapList.get(0).getVersion();
		}

		return null;
	}

	@Override
	@Transactional
	public CodeValidationResult validateCodeInValueSet(ValidationSupportContext theValidationSupportContext, ConceptValidationOptions theOptions, String theCodeSystem, String theCode, String theDisplay, @Nonnull IBaseResource theValueSet) {
		invokeRunnableForUnitTest();

		IPrimitiveType<?> urlPrimitive = myContext.newTerser().getSingleValueOrNull(theValueSet, "url", IPrimitiveType.class);
		String url = urlPrimitive.getValueAsString();
		if (isNotBlank(url)) {
			return validateCode(theValidationSupportContext, theOptions, theCodeSystem, theCode, theDisplay, url);
		}
		return null;
	}

	@CoverageIgnore
	@Override
	public IValidationSupport.CodeValidationResult validateCode(ValidationSupportContext theValidationSupportContext, ConceptValidationOptions theOptions, String theCodeSystem, String theCode, String theDisplay, String theValueSetUrl) {
		invokeRunnableForUnitTest();

		if (isNotBlank(theValueSetUrl)) {
			return validateCodeInValueSet(theValidationSupportContext, theOptions, theValueSetUrl, theCodeSystem, theCode, theDisplay);
		}

		TransactionTemplate txTemplate = new TransactionTemplate(myTransactionManager);
		txTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		Optional<FhirVersionIndependentConcept> codeOpt = txTemplate.execute(t -> findCode(theCodeSystem, theCode).map(c -> new FhirVersionIndependentConcept(theCodeSystem, c.getCode())));

		if (codeOpt != null && codeOpt.isPresent()) {
			FhirVersionIndependentConcept code = codeOpt.get();
			if (!theOptions.isValidateDisplay() || (isNotBlank(code.getDisplay()) && isNotBlank(theDisplay) && code.getDisplay().equals(theDisplay))) {
				return new CodeValidationResult()
					.setCode(code.getCode())
					.setDisplay(code.getDisplay());
			} else {
				return createFailureCodeValidationResult(theCodeSystem, theCode, " - Concept Display \"" + code.getDisplay() + "\" does not match expected \"" + code.getDisplay() + "\"").setDisplay(code.getDisplay());
			}
		}

		return createFailureCodeValidationResult(theCodeSystem, theCode);
	}


	IValidationSupport.CodeValidationResult validateCodeInValueSet(ValidationSupportContext theValidationSupportContext, ConceptValidationOptions theValidationOptions, String theValueSetUrl, String theCodeSystem, String theCode, String theDisplay) {
		IBaseResource valueSet = theValidationSupportContext.getRootValidationSupport().fetchValueSet(theValueSetUrl);

		// If we don't have a PID, this came from some source other than the JPA
		// database, so we don't need to check if it's pre-expanded or not
		if (valueSet instanceof IAnyResource) {
			Long pid = IDao.RESOURCE_PID.get((IAnyResource) valueSet);
			if (pid != null) {
				if (isValueSetPreExpandedForCodeValidation(valueSet)) {
					return validateCodeIsInPreExpandedValueSet(theValidationOptions, valueSet, theCodeSystem, theCode, null, null, null);
				}
			}
		}

		CodeValidationResult retVal;
		if (valueSet != null) {
			retVal = new InMemoryTerminologyServerValidationSupport(myContext).validateCodeInValueSet(theValidationSupportContext, theValidationOptions, theCodeSystem, theCode, theDisplay, valueSet);
		} else {
			String append = " - Unable to locate ValueSet[" + theValueSetUrl + "]";
			retVal = createFailureCodeValidationResult(theCodeSystem, theCode, append);
		}

		if (retVal == null) {
			String append = " - Unable to expand ValueSet[" + theValueSetUrl + "]";
			retVal = createFailureCodeValidationResult(theCodeSystem, theCode, append);
		}

		return retVal;

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

	private void findCodesAbove(CodeSystem theSystem, String theSystemString, String theCode, List<FhirVersionIndependentConcept> theListToPopulate) {
		List<CodeSystem.ConceptDefinitionComponent> conceptList = theSystem.getConcept();
		for (CodeSystem.ConceptDefinitionComponent next : conceptList) {
			addTreeIfItContainsCode(theSystemString, next, theCode, theListToPopulate);
		}
	}

	@Override
	public List<FhirVersionIndependentConcept> findCodesAboveUsingBuiltInSystems(String theSystem, String theCode) {
		ArrayList<FhirVersionIndependentConcept> retVal = new ArrayList<>();
		CodeSystem system = fetchCanonicalCodeSystemFromCompleteContext(theSystem);
		if (system != null) {
			findCodesAbove(system, theSystem, theCode, retVal);
		}
		return retVal;
	}

	private void findCodesBelow(CodeSystem theSystem, String theSystemString, String theCode, List<FhirVersionIndependentConcept> theListToPopulate) {
		List<CodeSystem.ConceptDefinitionComponent> conceptList = theSystem.getConcept();
		findCodesBelow(theSystemString, theCode, theListToPopulate, conceptList);
	}

	private void findCodesBelow(String theSystemString, String theCode, List<FhirVersionIndependentConcept> theListToPopulate, List<CodeSystem.ConceptDefinitionComponent> conceptList) {
		for (CodeSystem.ConceptDefinitionComponent next : conceptList) {
			if (theCode.equals(next.getCode())) {
				addAllChildren(theSystemString, next, theListToPopulate);
			} else {
				findCodesBelow(theSystemString, theCode, theListToPopulate, next.getConcept());
			}
		}
	}

	@Override
	public List<FhirVersionIndependentConcept> findCodesBelowUsingBuiltInSystems(String theSystem, String theCode) {
		ArrayList<FhirVersionIndependentConcept> retVal = new ArrayList<>();
		CodeSystem system = fetchCanonicalCodeSystemFromCompleteContext(theSystem);
		if (system != null) {
			findCodesBelow(system, theSystem, theCode, retVal);
		}
		return retVal;
	}

	private void addAllChildren(String theSystemString, CodeSystem.ConceptDefinitionComponent theCode, List<FhirVersionIndependentConcept> theListToPopulate) {
		if (isNotBlank(theCode.getCode())) {
			theListToPopulate.add(new FhirVersionIndependentConcept(theSystemString, theCode.getCode()));
		}
		for (CodeSystem.ConceptDefinitionComponent nextChild : theCode.getConcept()) {
			addAllChildren(theSystemString, nextChild, theListToPopulate);
		}
	}

	private boolean addTreeIfItContainsCode(String theSystemString, CodeSystem.ConceptDefinitionComponent theNext, String theCode, List<FhirVersionIndependentConcept> theListToPopulate) {
		boolean foundCodeInChild = false;
		for (CodeSystem.ConceptDefinitionComponent nextChild : theNext.getConcept()) {
			foundCodeInChild |= addTreeIfItContainsCode(theSystemString, nextChild, theCode, theListToPopulate);
		}

		if (theCode.equals(theNext.getCode()) || foundCodeInChild) {
			theListToPopulate.add(new FhirVersionIndependentConcept(theSystemString, theNext.getCode()));
			return true;
		}

		return false;
	}

	@Nullable
	protected abstract Coding toCanonicalCoding(@Nullable IBaseDatatype theCoding);

	@Nullable
	protected abstract Coding toCanonicalCoding(@Nullable IBaseCoding theCoding);

	@Nullable
	protected abstract CodeableConcept toCanonicalCodeableConcept(@Nullable IBaseDatatype theCodeableConcept);

	@NotNull
	private FhirVersionIndependentConcept toConcept(IPrimitiveType<String> theCodeType, IPrimitiveType<String> theCodeSystemIdentifierType, IBaseCoding theCodingType) {
		String code = theCodeType != null ? theCodeType.getValueAsString() : null;
		String system = theCodeSystemIdentifierType != null ? getUrlFromIdentifier(theCodeSystemIdentifierType.getValueAsString()) : null;
		String systemVersion = theCodeSystemIdentifierType != null ? getVersionFromIdentifier(theCodeSystemIdentifierType.getValueAsString()) : null;
		if (theCodingType != null) {
			Coding canonicalizedCoding = toCanonicalCoding(theCodingType);
			assert canonicalizedCoding != null; // Shouldn't be null, since theCodingType isn't
			code = canonicalizedCoding.getCode();
			system = canonicalizedCoding.getSystem();
			systemVersion = canonicalizedCoding.getVersion();
		}
		return new FhirVersionIndependentConcept(system, code, null, systemVersion);
	}

	@Override
	@Transactional
	public CodeValidationResult codeSystemValidateCode(IIdType theCodeSystemId, String theCodeSystemUrl, String theVersion, String theCode, String theDisplay, IBaseDatatype theCoding, IBaseDatatype theCodeableConcept) {

		CodeableConcept codeableConcept = toCanonicalCodeableConcept(theCodeableConcept);
		boolean haveCodeableConcept = codeableConcept != null && codeableConcept.getCoding().size() > 0;

		Coding coding = toCanonicalCoding(theCoding);
		boolean haveCoding = coding != null && coding.isEmpty() == false;

		boolean haveCode = theCode != null && theCode.isEmpty() == false;

		if (!haveCodeableConcept && !haveCoding && !haveCode) {
			throw new InvalidRequestException("No code, coding, or codeableConcept provided to validate.");
		}
		if (!LogicUtil.multiXor(haveCodeableConcept, haveCoding, haveCode)) {
			throw new InvalidRequestException("$validate-code can only validate (code) OR (coding) OR (codeableConcept)");
		}

		boolean haveIdentifierParam = isNotBlank(theCodeSystemUrl);
		String codeSystemUrl;
		if (theCodeSystemId != null) {
			IBaseResource codeSystem = myDaoRegistry.getResourceDao("CodeSystem").read(theCodeSystemId);
			codeSystemUrl = CommonCodeSystemsTerminologyService.getCodeSystemUrl(codeSystem);
		} else if (haveIdentifierParam) {
			codeSystemUrl = theCodeSystemUrl;
		} else {
			throw new InvalidRequestException("Either CodeSystem ID or CodeSystem identifier must be provided. Unable to validate.");
		}


		String code = theCode;
		String display = theDisplay;

		if (haveCodeableConcept) {
			for (int i = 0; i < codeableConcept.getCoding().size(); i++) {
				Coding nextCoding = codeableConcept.getCoding().get(i);
				if (nextCoding.hasSystem()) {
					if (!codeSystemUrl.equalsIgnoreCase(nextCoding.getSystem())) {
						throw new InvalidRequestException("Coding.system '" + nextCoding.getSystem() + "' does not equal with CodeSystem.url '" + theCodeSystemUrl + "'. Unable to validate.");
					}
					codeSystemUrl = nextCoding.getSystem();
				}
				code = nextCoding.getCode();
				display = nextCoding.getDisplay();
				CodeValidationResult nextValidation = codeSystemValidateCode(codeSystemUrl, theVersion, code, display);
				if (nextValidation.isOk() || i == codeableConcept.getCoding().size() - 1) {
					return nextValidation;
				}
			}
		} else if (haveCoding) {
			if (coding.hasSystem()) {
				if (!codeSystemUrl.equalsIgnoreCase(coding.getSystem())) {
					throw new InvalidRequestException("Coding.system '" + coding.getSystem() + "' does not equal with CodeSystem.url '" + theCodeSystemUrl + "'. Unable to validate.");
				}
				codeSystemUrl = coding.getSystem();
			}
			code = coding.getCode();
			display = coding.getDisplay();
		}

		return codeSystemValidateCode(codeSystemUrl, theVersion, code, display);
	}

	@SuppressWarnings("unchecked")
	private CodeValidationResult codeSystemValidateCode(String theCodeSystemUrl, String theCodeSystemVersion, String theCode, String theDisplay) {

		CriteriaBuilder criteriaBuilder = myEntityManager.getCriteriaBuilder();
		CriteriaQuery<TermConcept> query = criteriaBuilder.createQuery(TermConcept.class);
		Root<TermConcept> root = query.from(TermConcept.class);

		Fetch<TermCodeSystemVersion, TermConcept> systemVersionFetch = root.fetch("myCodeSystem", JoinType.INNER);
		Join<TermCodeSystemVersion, TermConcept> systemVersionJoin = (Join<TermCodeSystemVersion, TermConcept>) systemVersionFetch;
		Fetch<TermCodeSystem, TermCodeSystemVersion> systemFetch = systemVersionFetch.fetch("myCodeSystem", JoinType.INNER);
		Join<TermCodeSystem, TermCodeSystemVersion> systemJoin = (Join<TermCodeSystem, TermCodeSystemVersion>) systemFetch;

		ArrayList<Predicate> predicates = new ArrayList<>();

		if (isNotBlank(theCode)) {
			predicates.add(criteriaBuilder.equal(root.get("myCode"), theCode));
		}

		if (isNotBlank(theDisplay)) {
			predicates.add(criteriaBuilder.equal(root.get("myDisplay"), theDisplay));
		}

		if (isNoneBlank(theCodeSystemUrl)) {
			predicates.add(criteriaBuilder.equal(systemJoin.get("myCodeSystemUri"), theCodeSystemUrl));
		}

		if (isNoneBlank(theCodeSystemVersion)) {
			predicates.add(criteriaBuilder.equal(systemVersionJoin.get("myCodeSystemVersionId"), theCodeSystemVersion));
		} else {
			query.orderBy(criteriaBuilder.desc(root.get("myUpdated")));
		}

		Predicate outerPredicate = criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		query.where(outerPredicate);

		final TypedQuery<TermConcept> typedQuery = myEntityManager.createQuery(query.select(root));
		org.hibernate.query.Query<TermConcept> hibernateQuery = (org.hibernate.query.Query<TermConcept>) typedQuery;
		hibernateQuery.setFetchSize(SINGLE_FETCH_SIZE);
		List<TermConcept> resultsList = hibernateQuery.getResultList();

		if (!resultsList.isEmpty()) {
			TermConcept concept = resultsList.get(0);
			return new CodeValidationResult().setCode(concept.getCode()).setDisplay(concept.getDisplay());
		}

		if (isBlank(theDisplay))
			return createFailureCodeValidationResult(theCodeSystemUrl, theCode);
		else
			return createFailureCodeValidationResult(theCodeSystemUrl, theCode, " - Concept Display : " + theDisplay);
	}

	public static class Job implements HapiJob {
		@Autowired
		private ITermReadSvc myTerminologySvc;

		@Override
		public void execute(JobExecutionContext theContext) {
			myTerminologySvc.preExpandDeferredValueSetsToTerminologyTables();
		}
	}

	/**
	 * This is only used for unit tests to test failure conditions
	 */
	static void invokeRunnableForUnitTest() {
		if (myInvokeOnNextCallForUnitTest != null) {
			Runnable invokeOnNextCallForUnitTest = myInvokeOnNextCallForUnitTest;
			myInvokeOnNextCallForUnitTest = null;
			invokeOnNextCallForUnitTest.run();
		}
	}

	@VisibleForTesting
	public static void setInvokeOnNextCallForUnitTest(Runnable theInvokeOnNextCallForUnitTest) {
		myInvokeOnNextCallForUnitTest = theInvokeOnNextCallForUnitTest;
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
