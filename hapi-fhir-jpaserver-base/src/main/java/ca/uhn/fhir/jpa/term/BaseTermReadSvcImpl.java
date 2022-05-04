package ca.uhn.fhir.jpa.term;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IDao;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoCodeSystem;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.config.HibernatePropertiesProvider;
import ca.uhn.fhir.jpa.config.util.ConnectionPoolInfoProvider;
import ca.uhn.fhir.jpa.config.util.IConnectionPoolInfoProvider;
import ca.uhn.fhir.jpa.dao.IFulltextSearchSvc;
import ca.uhn.fhir.jpa.dao.data.ITermCodeSystemDao;
import ca.uhn.fhir.jpa.dao.data.ITermCodeSystemVersionDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptDesignationDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptPropertyDao;
import ca.uhn.fhir.jpa.dao.data.ITermValueSetConceptDao;
import ca.uhn.fhir.jpa.dao.data.ITermValueSetConceptDesignationDao;
import ca.uhn.fhir.jpa.dao.data.ITermValueSetConceptViewDao;
import ca.uhn.fhir.jpa.dao.data.ITermValueSetConceptViewOracleDao;
import ca.uhn.fhir.jpa.dao.data.ITermValueSetDao;
import ca.uhn.fhir.jpa.entity.ITermValueSetConceptView;
import ca.uhn.fhir.jpa.entity.TermCodeSystem;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.entity.TermConceptDesignation;
import ca.uhn.fhir.jpa.entity.TermConceptParentChildLink;
import ca.uhn.fhir.jpa.entity.TermConceptParentChildLink.RelationshipTypeEnum;
import ca.uhn.fhir.jpa.entity.TermConceptProperty;
import ca.uhn.fhir.jpa.entity.TermConceptPropertyTypeEnum;
import ca.uhn.fhir.jpa.entity.TermValueSet;
import ca.uhn.fhir.jpa.entity.TermValueSetConcept;
import ca.uhn.fhir.jpa.entity.TermValueSetPreExpansionStatusEnum;
import ca.uhn.fhir.jpa.model.entity.ForcedId;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.sched.HapiJob;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.model.sched.ScheduledJobDefinition;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.search.ElasticsearchNestedQueryBuilderUtil;
import ca.uhn.fhir.jpa.search.builder.SearchBuilder;
import ca.uhn.fhir.jpa.term.api.ITermDeferredStorageSvc;
import ca.uhn.fhir.jpa.term.api.ITermReadSvc;
import ca.uhn.fhir.jpa.term.api.ReindexTerminologyResult;
import ca.uhn.fhir.jpa.term.ex.ExpansionTooCostlyException;
import ca.uhn.fhir.jpa.util.LogicUtil;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
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
import com.google.gson.JsonObject;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.RegexpQuery;
import org.apache.lucene.search.TermQuery;
import org.hibernate.CacheMode;
import org.hibernate.search.backend.elasticsearch.ElasticsearchExtension;
import org.hibernate.search.backend.lucene.LuceneExtension;
import org.hibernate.search.engine.search.predicate.dsl.BooleanPredicateClausesStep;
import org.hibernate.search.engine.search.predicate.dsl.PredicateFinalStep;
import org.hibernate.search.engine.search.predicate.dsl.SearchPredicateFactory;
import org.hibernate.search.engine.search.query.SearchQuery;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.common.EntityReference;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.hibernate.search.mapper.pojo.massindexing.impl.PojoMassIndexingLoggingMonitor;
import org.hl7.fhir.common.hapi.validation.support.CommonCodeSystemsTerminologyService;
import org.hl7.fhir.common.hapi.validation.support.InMemoryTerminologyServerValidationSupport;
import org.hl7.fhir.convertors.advisors.impl.BaseAdvisor_40_50;
import org.hl7.fhir.convertors.context.ConversionContext40_50;
import org.hl7.fhir.convertors.conv40_50.VersionConvertor_40_50;
import org.hl7.fhir.convertors.conv40_50.resources40_50.ValueSet40_50;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.instance.model.api.IBaseDatatype;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.CanonicalType;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.DomainResource;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.InstantType;
import org.hl7.fhir.r4.model.IntegerType;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.ValueSet;
import org.hl7.fhir.r4.model.codesystems.ConceptSubsumptionOutcome;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
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
import org.springframework.util.CollectionUtils;
import org.springframework.util.comparator.Comparators;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static ca.uhn.fhir.jpa.term.api.ITermLoaderSvc.LOINC_URI;
import static java.lang.String.join;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNoneBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.lowerCase;
import static org.apache.commons.lang3.StringUtils.startsWithIgnoreCase;
import static org.hl7.fhir.common.hapi.validation.support.ValidationConstants.LOINC_LOW;

public abstract class BaseTermReadSvcImpl implements ITermReadSvc {
	public static final int DEFAULT_FETCH_SIZE = 250;
	private static final int SINGLE_FETCH_SIZE = 1;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseTermReadSvcImpl.class);
	private static final ValueSetExpansionOptions DEFAULT_EXPANSION_OPTIONS = new ValueSetExpansionOptions();
	private static final TermCodeSystemVersion NO_CURRENT_VERSION = new TermCodeSystemVersion().setId(-1L);
	private static Runnable myInvokeOnNextCallForUnitTest;
	private static boolean ourForceDisableHibernateSearchForUnitTest;

	private static final String IDX_PROPERTIES = "myProperties";
	private static final String IDX_PROP_KEY = IDX_PROPERTIES + ".myKey";
	private static final String IDX_PROP_VALUE_STRING = IDX_PROPERTIES + ".myValueString";
	private static final String IDX_PROP_DISPLAY_STRING = IDX_PROPERTIES + ".myDisplayString";

	public static final int DEFAULT_MASS_INDEXER_OBJECT_LOADING_THREADS = 2;
	// doesn't seem to be much gain by using more threads than this value
	public static final int MAX_MASS_INDEXER_OBJECT_LOADING_THREADS = 6;

	private boolean myPreExpandingValueSets = false;

	private final Cache<String, TermCodeSystemVersion> myCodeSystemCurrentVersionCache = Caffeine.newBuilder().expireAfterWrite(1, TimeUnit.MINUTES).build();
	@Autowired
	protected DaoRegistry myDaoRegistry;
	@Autowired
	protected ITermCodeSystemDao myCodeSystemDao;
	@Autowired
	protected ITermConceptDao myConceptDao;
	@Autowired
	protected ITermConceptPropertyDao myConceptPropertyDao;
	@Autowired
	protected ITermConceptDesignationDao myConceptDesignationDao;
	@Autowired
	protected ITermValueSetDao myTermValueSetDao;
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
	private TransactionTemplate myTxTemplate;
	@Autowired
	private PlatformTransactionManager myTransactionManager;
	@Autowired(required = false)
	private IFulltextSearchSvc myFulltextSearchSvc;
	@Autowired
	private PlatformTransactionManager myTxManager;
	@Autowired
	private ITermConceptDao myTermConceptDao;
	@Autowired
	private ITermValueSetConceptViewDao myTermValueSetConceptViewDao;
	@Autowired
	private ITermValueSetConceptViewOracleDao myTermValueSetConceptViewOracleDao;
	@Autowired
	private ISchedulerService mySchedulerService;
	@Autowired(required = false)
	private ITermDeferredStorageSvc myDeferredStorageSvc;
	@Autowired
	private IIdHelperService myIdHelperService;

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

	private boolean addCodeIfNotAlreadyAdded(@Nullable ValueSetExpansionOptions theExpansionOptions, IValueSetConceptAccumulator theValueSetCodeAccumulator, Set<String> theAddedCodes, TermConcept theConcept, boolean theAdd, String theValueSetIncludeVersion) {
		String codeSystem = theConcept.getCodeSystemVersion().getCodeSystem().getCodeSystemUri();
		String codeSystemVersion = theConcept.getCodeSystemVersion().getCodeSystemVersionId();
		String code = theConcept.getCode();
		String display = theConcept.getDisplay();
		Long sourceConceptPid = theConcept.getId();
		String directParentPids = "";

		if (theExpansionOptions != null && theExpansionOptions.isIncludeHierarchy()) {
			directParentPids = theConcept
				.getParents()
				.stream()
				.map(t -> t.getParent().getId().toString())
				.collect(joining(" "));
		}

		Collection<TermConceptDesignation> designations = theConcept.getDesignations();
		if (StringUtils.isNotEmpty(theValueSetIncludeVersion)) {
			return addCodeIfNotAlreadyAdded(theValueSetCodeAccumulator, theAddedCodes, designations, theAdd, codeSystem + "|" + theValueSetIncludeVersion, code, display, sourceConceptPid, directParentPids, codeSystemVersion);
		} else {
			return addCodeIfNotAlreadyAdded(theValueSetCodeAccumulator, theAddedCodes, designations, theAdd, codeSystem, code, display, sourceConceptPid, directParentPids, codeSystemVersion);
		}
	}

	private boolean addCodeIfNotAlreadyAdded(IValueSetConceptAccumulator theValueSetCodeAccumulator, Set<String> theAddedCodes, boolean theAdd, String theCodeSystem, String theCodeSystemVersion, String theCode, String theDisplay, Long theSourceConceptPid, String theSourceConceptDirectParentPids, Collection<TermConceptDesignation> theDesignations) {
		if (StringUtils.isNotEmpty(theCodeSystemVersion)) {
			if (isNoneBlank(theCodeSystem, theCode)) {
				if (theAdd && theAddedCodes.add(theCodeSystem + "|" + theCode)) {
					theValueSetCodeAccumulator.includeConceptWithDesignations(theCodeSystem + "|" + theCodeSystemVersion, theCode, theDisplay, theDesignations, theSourceConceptPid, theSourceConceptDirectParentPids, theCodeSystemVersion);
					return true;
				}

				if (!theAdd && theAddedCodes.remove(theCodeSystem + "|" + theCode)) {
					theValueSetCodeAccumulator.excludeConcept(theCodeSystem + "|" + theCodeSystemVersion, theCode);
					return true;
				}
			}
		} else {
			if (theAdd && theAddedCodes.add(theCodeSystem + "|" + theCode)) {
				theValueSetCodeAccumulator.includeConceptWithDesignations(theCodeSystem, theCode, theDisplay, theDesignations, theSourceConceptPid, theSourceConceptDirectParentPids, theCodeSystemVersion);
				return true;
			}

			if (!theAdd && theAddedCodes.remove(theCodeSystem + "|" + theCode)) {
				theValueSetCodeAccumulator.excludeConcept(theCodeSystem, theCode);
				return true;
			}
		}

		return false;
	}

	private boolean addCodeIfNotAlreadyAdded(IValueSetConceptAccumulator theValueSetCodeAccumulator, Set<String> theAddedCodes, Collection<TermConceptDesignation> theDesignations, boolean theAdd, String theCodeSystem, String theCode, String theDisplay, Long theSourceConceptPid, String theSourceConceptDirectParentPids, String theSystemVersion) {
		if (isNoneBlank(theCodeSystem, theCode)) {
			if (theAdd && theAddedCodes.add(theCodeSystem + "|" + theCode)) {
				theValueSetCodeAccumulator.includeConceptWithDesignations(theCodeSystem, theCode, theDisplay, theDesignations, theSourceConceptPid, theSourceConceptDirectParentPids, theSystemVersion);
				return true;
			}

			if (!theAdd && theAddedCodes.remove(theCodeSystem + "|" + theCode)) {
				theValueSetCodeAccumulator.excludeConcept(theCodeSystem, theCode);
				return true;
			}
		}

		return false;
	}

	private boolean addToSet(Set<TermConcept> theSetToPopulate, TermConcept theConcept) {
		boolean retVal = theSetToPopulate.add(theConcept);
		if (retVal) {
			if (theSetToPopulate.size() >= myDaoConfig.getMaximumExpansionSize()) {
				String msg = myContext.getLocalizer().getMessage(BaseTermReadSvcImpl.class, "expansionTooLarge", myDaoConfig.getMaximumExpansionSize());
				throw new ExpansionTooCostlyException(Msg.code(885) + msg);
			}
		}
		return retVal;
	}

	/**
	 * This method is present only for unit tests, do not call from client code
	 */
	@VisibleForTesting
	public void clearCaches() {
		myCodeSystemCurrentVersionCache.invalidateAll();
	}


	public void deleteValueSetForResource(ResourceTable theResourceTable) {
		// Get existing entity so it can be deleted.
		Optional<TermValueSet> optionalExistingTermValueSetById = myTermValueSetDao.findByResourcePid(theResourceTable.getId());

		if (optionalExistingTermValueSetById.isPresent()) {
			TermValueSet existingTermValueSet = optionalExistingTermValueSetById.get();

			ourLog.info("Deleting existing TermValueSet[{}] and its children...", existingTermValueSet.getId());
			deletePreCalculatedValueSetContents(existingTermValueSet);
			myTermValueSetDao.deleteById(existingTermValueSet.getId());
			ourLog.info("Done deleting existing TermValueSet[{}] and its children.", existingTermValueSet.getId());
		}
	}

	private void deletePreCalculatedValueSetContents(TermValueSet theValueSet) {
		myValueSetConceptDesignationDao.deleteByTermValueSetId(theValueSet.getId());
		myValueSetConceptDao.deleteByTermValueSetId(theValueSet.getId());
	}

	@Override
	@Transactional
	public void deleteValueSetAndChildren(ResourceTable theResourceTable) {
		deleteValueSetForResource(theResourceTable);
	}


	@Override
	@Transactional
	public List<FhirVersionIndependentConcept> expandValueSetIntoConceptList(@Nullable ValueSetExpansionOptions theExpansionOptions, @Nonnull String theValueSetCanonicalUrl) {
		// TODO: DM 2019-09-10 - This is problematic because an incorrect URL that matches ValueSet.id will not be found in the terminology tables but will yield a ValueSet here. Depending on the ValueSet, the expansion may time-out.

		ValueSet expanded = expandValueSet(theExpansionOptions, theValueSetCanonicalUrl);

		ArrayList<FhirVersionIndependentConcept> retVal = new ArrayList<>();
		for (ValueSet.ValueSetExpansionContainsComponent nextContains : expanded.getExpansion().getContains()) {
			retVal.add(new FhirVersionIndependentConcept(nextContains.getSystem(), nextContains.getCode(), nextContains.getDisplay(), nextContains.getVersion()));
		}
		return retVal;
	}

	@Override
	@Transactional
	public ValueSet expandValueSet(@Nullable ValueSetExpansionOptions theExpansionOptions, @Nonnull String theValueSetCanonicalUrl) {
		ValueSet valueSet = fetchCanonicalValueSetFromCompleteContext(theValueSetCanonicalUrl);
		if (valueSet == null) {
			throw new ResourceNotFoundException(Msg.code(886) + "Unknown ValueSet: " + UrlUtil.escapeUrlParam(theValueSetCanonicalUrl));
		}

		return expandValueSet(theExpansionOptions, valueSet);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ValueSet expandValueSet(@Nullable ValueSetExpansionOptions theExpansionOptions, @Nonnull ValueSet theValueSetToExpand) {
		String filter = null;
		if (theExpansionOptions != null) {
			filter = theExpansionOptions.getFilter();
		}
		return expandValueSet(theExpansionOptions, theValueSetToExpand, ExpansionFilter.fromFilterString(filter));
	}

	private ValueSet expandValueSet(@Nullable ValueSetExpansionOptions theExpansionOptions, ValueSet theValueSetToExpand, ExpansionFilter theFilter) {
		ValidateUtil.isNotNullOrThrowUnprocessableEntity(theValueSetToExpand, "ValueSet to expand can not be null");

		ValueSetExpansionOptions expansionOptions = provideExpansionOptions(theExpansionOptions);
		int offset = expansionOptions.getOffset();
		int count = expansionOptions.getCount();

		ValueSetExpansionComponentWithConceptAccumulator accumulator = new ValueSetExpansionComponentWithConceptAccumulator(myContext, count, expansionOptions.isIncludeHierarchy());
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
		valueSet.setUrl(theValueSetToExpand.getUrl());
		valueSet.setId(theValueSetToExpand.getId());
		valueSet.setStatus(Enumerations.PublicationStatus.ACTIVE);
		valueSet.setCompose(theValueSetToExpand.getCompose());
		valueSet.setExpansion(accumulator);

		for (String next : accumulator.getMessages()) {
			valueSet.getMeta().addExtension()
				.setUrl(HapiExtensions.EXT_VALUESET_EXPANSION_MESSAGE)
				.setValue(new StringType(next));
		}

		if (expansionOptions.isIncludeHierarchy()) {
			accumulator.applyHierarchy();
		}

		return valueSet;
	}

	private void expandValueSetIntoAccumulator(ValueSet theValueSetToExpand, ValueSetExpansionOptions theExpansionOptions, IValueSetConceptAccumulator theAccumulator, ExpansionFilter theFilter, boolean theAdd) {
		Optional<TermValueSet> optionalTermValueSet;
		if (theValueSetToExpand.hasUrl()) {
			if (theValueSetToExpand.hasVersion()) {
				optionalTermValueSet = myTermValueSetDao.findTermValueSetByUrlAndVersion(theValueSetToExpand.getUrl(), theValueSetToExpand.getVersion());
			} else {
				optionalTermValueSet = findCurrentTermValueSet(theValueSetToExpand.getUrl());
			}
		} else {
			optionalTermValueSet = Optional.empty();
		}

		/*
		 * ValueSet doesn't exist in pre-expansion database, so perform in-memory expansion
		 */
		if (!optionalTermValueSet.isPresent()) {
			ourLog.debug("ValueSet is not present in terminology tables. Will perform in-memory expansion without parameters. {}", getValueSetInfo(theValueSetToExpand));
			String msg = myContext.getLocalizer().getMessage(BaseTermReadSvcImpl.class, "valueSetExpandedUsingInMemoryExpansion", getValueSetInfo(theValueSetToExpand));
			theAccumulator.addMessage(msg);
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
		String expansionTimestamp = toHumanReadableExpansionTimestamp(termValueSet);
		String msg = myContext.getLocalizer().getMessage(BaseTermReadSvcImpl.class, "valueSetExpandedUsingPreExpansion", expansionTimestamp);
		theAccumulator.addMessage(msg);
		expandConcepts(theAccumulator, termValueSet, theFilter, theAdd, isOracleDialect());
	}

	@Nonnull
	private String toHumanReadableExpansionTimestamp(TermValueSet termValueSet) {
		String expansionTimestamp = "(unknown)";
		if (termValueSet.getExpansionTimestamp() != null) {
			String timeElapsed = StopWatch.formatMillis(System.currentTimeMillis() - termValueSet.getExpansionTimestamp().getTime());
			expansionTimestamp = new InstantType(termValueSet.getExpansionTimestamp()).getValueAsString() + " (" + timeElapsed + " ago)";
		}
		return expansionTimestamp;
	}

	private boolean isOracleDialect() {
		return myHibernatePropertiesProvider.getDialect() instanceof org.hibernate.dialect.Oracle12cDialect;
	}

	private void expandConcepts(IValueSetConceptAccumulator theAccumulator, TermValueSet theTermValueSet, ExpansionFilter theFilter, boolean theAdd, boolean theOracle) {
		// NOTE: if you modifiy the logic here, look to `expandConceptsOracle` and see if your new code applies to its copy pasted sibling
		Integer offset = theAccumulator.getSkipCountRemaining();
		offset = ObjectUtils.defaultIfNull(offset, 0);
		offset = Math.min(offset, theTermValueSet.getTotalConcepts().intValue());

		Integer count = theAccumulator.getCapacityRemaining();
		count = defaultIfNull(count, myDaoConfig.getMaximumExpansionSize());

		int conceptsExpanded = 0;
		int designationsExpanded = 0;
		int toIndex = offset + count;

		Collection<? extends ITermValueSetConceptView> conceptViews;
		boolean wasFilteredResult = false;
		String filterDisplayValue = null;
		if (!theFilter.getFilters().isEmpty() && JpaConstants.VALUESET_FILTER_DISPLAY.equals(theFilter.getFilters().get(0).getProperty()) && theFilter.getFilters().get(0).getOp() == ValueSet.FilterOperator.EQUAL) {
			filterDisplayValue = lowerCase(theFilter.getFilters().get(0).getValue().replace("%", "[%]"));
			String displayValue = "%" + lowerCase(filterDisplayValue) + "%";
			if (theOracle) {
				conceptViews = myTermValueSetConceptViewOracleDao.findByTermValueSetId(theTermValueSet.getId(), displayValue);
			} else {
				conceptViews = myTermValueSetConceptViewDao.findByTermValueSetId(theTermValueSet.getId(), displayValue);
			}
			wasFilteredResult = true;
		} else {
			// TODO JA HS: I'm pretty sure we are overfetching here.  test says offset 3, count 4, but we are fetching index 3 -> 10 here, grabbing 7 concepts.
			//Specifically this test testExpandInline_IncludePreExpandedValueSetByUri_FilterOnDisplay_LeftMatch_SelectRange
			if (theOracle) {
				conceptViews = myTermValueSetConceptViewOracleDao.findByTermValueSetId(offset, toIndex, theTermValueSet.getId());
			} else {
				conceptViews = myTermValueSetConceptViewDao.findByTermValueSetId(offset, toIndex, theTermValueSet.getId());
			}
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
		Map<Long, Long> pidToSourcePid = new HashMap<>();
		Map<Long, String> pidToSourceDirectParentPids = new HashMap<>();

		for (ITermValueSetConceptView conceptView : conceptViews) {

			String system = conceptView.getConceptSystemUrl();
			String code = conceptView.getConceptCode();
			String display = conceptView.getConceptDisplay();
			String systemVersion = conceptView.getConceptSystemVersion();

			//-- this is quick solution, may need to revisit
			if (!applyFilter(display, filterDisplayValue)) {
				continue;}

			Long conceptPid = conceptView.getConceptPid();
			if (!pidToConcept.containsKey(conceptPid)) {
				FhirVersionIndependentConcept concept = new FhirVersionIndependentConcept(system, code, display, systemVersion);
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

			if (theAccumulator.isTrackingHierarchy()) {
				pidToSourcePid.put(conceptPid, conceptView.getSourceConceptPid());
				pidToSourceDirectParentPids.put(conceptPid, conceptView.getSourceConceptDirectParentPids());
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
			String systemVersion = concept.getSystemVersion();

			if (theAdd) {
				if (theAccumulator.getCapacityRemaining() != null) {
					if (theAccumulator.getCapacityRemaining() == 0) {
						break;
					}
				}

				Long sourceConceptPid = pidToSourcePid.get(nextPid);
				String sourceConceptDirectParentPids = pidToSourceDirectParentPids.get(nextPid);
				theAccumulator.includeConceptWithDesignations(system, code, display, designations, sourceConceptPid, sourceConceptDirectParentPids, systemVersion);
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

	public boolean applyFilter(final String theDisplay, final String theFilterDisplay) {

		//-- safety check only, no need to apply filter
		if (theDisplay == null || theFilterDisplay == null)
			return true;

		// -- sentence case
		if (startsWithIgnoreCase(theDisplay, theFilterDisplay))
			return true;

		//-- token case
		return startsWithByWordBoundaries(theDisplay, theFilterDisplay);
	}

	private boolean startsWithByWordBoundaries(String theDisplay, String theFilterDisplay) {
		// return true only e.g. the input is 'Body height', theFilterDisplay is "he", or 'bo'
		StringTokenizer tok = new StringTokenizer(theDisplay);
		List<String> tokens = new ArrayList<>();
		while (tok.hasMoreTokens()) {
			String token = tok.nextToken();
			if (startsWithIgnoreCase(token, theFilterDisplay))
				return true;
			tokens.add(token);
		}

		// Allow to search by the end of the phrase.  E.g.  "working proficiency" will match "Limited working proficiency"
		for (int start = 0; start <= tokens.size() - 1; ++start) {
			for (int end = start + 1; end <= tokens.size(); ++end) {
				String sublist = String.join(" ", tokens.subList(start, end));
				if (startsWithIgnoreCase(sublist, theFilterDisplay))
					return true;
			}
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
				throw new InvalidRequestException(Msg.code(887) + msg);
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

				return expandValueSetHandleIncludeOrExcludeUsingDatabase(theExpansionOptions, theValueSetCodeAccumulator, theAddedCodes, theIncludeOrExclude, theAdd, theQueryIndex, theExpansionFilter, system, cs);

			} else {

				if (theIncludeOrExclude.getConcept().size() > 0 && theExpansionFilter.hasCode()) {
					if (defaultString(theIncludeOrExclude.getSystem()).equals(theExpansionFilter.getSystem())) {
						if (theIncludeOrExclude.getConcept().stream().noneMatch(t -> t.getCode().equals(theExpansionFilter.getCode()))) {
							return false;
						}
					}
				}

				Consumer<FhirVersionIndependentConcept> consumer = c -> {
					addOrRemoveCode(theValueSetCodeAccumulator, theAddedCodes, theAdd, system, c.getCode(), c.getDisplay(), c.getSystemVersion());
				};

				try {
					ConversionContext40_50.INSTANCE.init(new VersionConvertor_40_50(new BaseAdvisor_40_50()), "ValueSet");
					org.hl7.fhir.r5.model.ValueSet.ConceptSetComponent includeOrExclude = ValueSet40_50.convertConceptSetComponent(theIncludeOrExclude);
					new InMemoryTerminologyServerValidationSupport(myContext).expandValueSetIncludeOrExclude(new ValidationSupportContext(provideValidationSupport()), consumer, includeOrExclude);
				} catch (InMemoryTerminologyServerValidationSupport.ExpansionCouldNotBeCompletedInternallyException e) {
					if (!theExpansionOptions.isFailOnMissingCodeSystem() && e.getFailureType() == InMemoryTerminologyServerValidationSupport.FailureType.UNKNOWN_CODE_SYSTEM) {
						return false;
					}
					throw new InternalErrorException(Msg.code(888) + e);
				} finally {
					ConversionContext40_50.INSTANCE.close("ValueSet");
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
					throw new ResourceNotFoundException(Msg.code(889) + "Unknown ValueSet: " + UrlUtil.escapeUrlParam(valueSetUrl));
				}

				expandValueSetIntoAccumulator(valueSet, theExpansionOptions, theValueSetCodeAccumulator, subExpansionFilter, theAdd);

			}

			return false;

		} else {
			throw new InvalidRequestException(Msg.code(890) + "ValueSet contains " + (theAdd ? "include" : "exclude") + " criteria with no system defined");
		}


	}

	private boolean isHibernateSearchEnabled() {
		return myFulltextSearchSvc != null && !ourForceDisableHibernateSearchForUnitTest;
	}

	@Nonnull
	private Boolean expandValueSetHandleIncludeOrExcludeUsingDatabase(ValueSetExpansionOptions theExpansionOptions, IValueSetConceptAccumulator theValueSetCodeAccumulator, Set<String> theAddedCodes, ValueSet.ConceptSetComponent theIncludeOrExclude, boolean theAdd, int theQueryIndex, @Nonnull ExpansionFilter theExpansionFilter, String theSystem, TermCodeSystem theCs) {
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

		List<String> codes = theIncludeOrExclude
			.getConcept()
			.stream()
			.filter(Objects::nonNull)
			.map(ValueSet.ConceptReferenceComponent::getCode)
			.filter(StringUtils::isNotBlank)
			.collect(Collectors.toList());

		Optional<PredicateFinalStep> expansionStepOpt = buildExpansionPredicate(codes, predicate);
		final PredicateFinalStep finishedQuery = expansionStepOpt.isPresent()
			? predicate.bool().must(step).must(expansionStepOpt.get()) : step;

		/*
		 * DM 2019-08-21 - Processing slows after any ValueSets with many codes explicitly identified. This might
		 * be due to the dark arts that is memory management. Will monitor but not do anything about this right now.
		 */

		//BooleanQuery.setMaxClauseCount(SearchBuilder.getMaximumPageSize());
		//TODO GGG HS looks like we can't set max clause count, but it can be set server side.
		//BooleanQuery.setMaxClauseCount(10000);
		// JM 2-22-02-15 - Hopefully increasing maxClauseCount should be not needed anymore

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

		ourLog.debug("Beginning batch expansion for {} with max results per batch: {}", (theAdd ? "inclusion" : "exclusion"), maxResultsPerBatch);

		StopWatch swForBatch = new StopWatch();
		AtomicInteger countForBatch = new AtomicInteger(0);

		SearchQuery<EntityReference> termConceptsQuery = searchSession
			.search(TermConcept.class)
			.selectEntityReference()
			.where(f -> finishedQuery)
			.toQuery();

		ourLog.trace("About to query: {}", termConceptsQuery.queryString());
		List<EntityReference> termConceptRefs = termConceptsQuery.fetchHits(theQueryIndex * maxResultsPerBatch, maxResultsPerBatch);
		List<Long> pids = termConceptRefs
			.stream()
			.map(t -> (Long) t.id())
			.collect(Collectors.toList());

		List<TermConcept> termConcepts = myTermConceptDao.fetchConceptsAndDesignationsByPid(pids);

		// If the include section had multiple codes, return the codes in the same order
		if (codes.size() > 1) {
			termConcepts = new ArrayList<>(termConcepts);
			Map<String, Integer> codeToIndex = new HashMap<>(codes.size());
			for (int i = 0; i < codes.size(); i++) {
				codeToIndex.put(codes.get(i), i);
			}
			termConcepts.sort(((o1, o2) -> {
				Integer idx1 = codeToIndex.get(o1.getCode());
				Integer idx2 = codeToIndex.get(o2.getCode());
				return Comparators.nullsHigh().compare(idx1, idx2);
			}));
		}

		int resultsInBatch = termConcepts.size();
		int firstResult = theQueryIndex * maxResultsPerBatch;// TODO GGG HS we lose the ability to check the index of the first result, so just best-guessing it here.
		int delta = 0;
		for (TermConcept concept : termConcepts) {
			count.incrementAndGet();
			countForBatch.incrementAndGet();
			if (theAdd && expansionStepOpt.isPresent()) {
				ValueSet.ConceptReferenceComponent theIncludeConcept = getMatchedConceptIncludedInValueSet(theIncludeOrExclude, concept);
				if (theIncludeConcept != null && isNotBlank(theIncludeConcept.getDisplay())) {
					concept.setDisplay(theIncludeConcept.getDisplay());
				}
			}
			boolean added = addCodeIfNotAlreadyAdded(theExpansionOptions, theValueSetCodeAccumulator, theAddedCodes, concept, theAdd, includeOrExcludeVersion);
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

	private ValueSet.ConceptReferenceComponent getMatchedConceptIncludedInValueSet(ValueSet.ConceptSetComponent theIncludeOrExclude, TermConcept concept) {
		return theIncludeOrExclude
			.getConcept()
			.stream().filter(includedConcept -> includedConcept.getCode().equalsIgnoreCase(concept.getCode()))
			.findFirst()
			.orElse(null);
	}

	/**
	 * Helper method which builds a predicate for the expansion
	 */
	private Optional<PredicateFinalStep> buildExpansionPredicate(List<String> theCodes, SearchPredicateFactory thePredicate) {
		if (CollectionUtils.isEmpty(theCodes)) { return  Optional.empty(); }

		if (theCodes.size() < BooleanQuery.getMaxClauseCount()) {
			return Optional.of(thePredicate.simpleQueryString()
				.field( "myCode" ).matching( String.join(" | ", theCodes)) );
		}
		
		// Number of codes is larger than maxClauseCount, so we split the query in several clauses

		// partition codes in lists of BooleanQuery.getMaxClauseCount() size
		List<List<String>> listOfLists = ListUtils.partition(theCodes, BooleanQuery.getMaxClauseCount());

		PredicateFinalStep step = thePredicate.bool(b -> {
			b.minimumShouldMatchNumber(1);
			for (List<String> codeList : listOfLists) {
				b.should(p -> p.simpleQueryString().field("myCode").matching(String.join(" | ", codeList)));
			}
		});

		return Optional.of(step);
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

	private void addOrRemoveCode(IValueSetConceptAccumulator theValueSetCodeAccumulator, Set<String> theAddedCodes, boolean theAdd, String theSystem, String theCode, String theDisplay, String theSystemVersion) {
		if (theAdd && theAddedCodes.add(theSystem + "|" + theCode)) {
			theValueSetCodeAccumulator.includeConcept(theSystem, theCode, theDisplay, null, null, theSystemVersion);
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
			throw new InvalidRequestException(Msg.code(891) + "Invalid filter, must have fields populated: property op value");
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
				handleFilterLoincAncestor(theCodeSystemIdentifier, theF, theB, theFilter);
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
				if (theFilter.getOp() == ValueSet.FilterOperator.REGEX) {
					handleFilterRegex(theF, theB, theFilter);
				} else {
					handleFilterPropertyDefault(theF, theB, theFilter);
				}
				break;
		}
	}

	private void handleFilterPropertyDefault(SearchPredicateFactory theF,
			BooleanPredicateClausesStep<?> theB, ValueSet.ConceptSetFilterComponent theFilter) {

		theB.must(getPropertyNameValueNestedPredicate(theF, theFilter.getProperty(), theFilter.getValue()));
	}


	private void handleFilterRegex(SearchPredicateFactory theF, BooleanPredicateClausesStep<?> theB, ValueSet.ConceptSetFilterComponent theFilter) {
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

		if (isFullTextSetToUseElastic()) {
			ElasticsearchNestedQueryBuilderUtil nestedQueryBuildUtil = new ElasticsearchNestedQueryBuilderUtil(
				"myProperties", "myKey", theFilter.getProperty(),
				"myValueString", value);

			JsonObject nestedQueryJO =  nestedQueryBuildUtil.toGson();

			ourLog.debug("Build nested Elasticsearch query: {}", nestedQueryJO);
			theB.must(theF.extension(ElasticsearchExtension.get()).fromJson(nestedQueryJO));
			return;

		}

		// native Lucene configured
		Query termPropKeyQuery = new TermQuery(new Term(IDX_PROP_KEY, theFilter.getProperty()));
		Query regexpValueQuery = new RegexpQuery(new Term(IDX_PROP_VALUE_STRING, value));

		theB.must(theF.nested().objectField("myProperties").nest(
			theF.bool()
				.must(theF.extension(LuceneExtension.get()).fromLuceneQuery(termPropKeyQuery))
				.must(theF.extension(LuceneExtension.get()).fromLuceneQuery(regexpValueQuery))
		));
	}


	private void handleFilterLoincCopyright(SearchPredicateFactory theF, BooleanPredicateClausesStep<?> theB, ValueSet.ConceptSetFilterComponent theFilter) {
		if (theFilter.getOp() == ValueSet.FilterOperator.EQUAL) {

			String copyrightFilterValue = defaultString(theFilter.getValue()).toLowerCase();
			switch (copyrightFilterValue) {
				case "3rdparty":
					logFilteringValueOnProperty(theFilter.getValue(), theFilter.getProperty());
					addFilterLoincCopyright3rdParty(theF, theB, theFilter);
					break;
				case "loinc":
					logFilteringValueOnProperty(theFilter.getValue(), theFilter.getProperty());
					addFilterLoincCopyrightLoinc(theF, theB, theFilter);
					break;
				default:
					throwInvalidRequestForValueOnProperty(theFilter.getValue(), theFilter.getProperty());
			}

		} else {
			throwInvalidRequestForOpOnProperty(theFilter.getOp(), theFilter.getProperty());
		}
	}

	private void addFilterLoincCopyrightLoinc(SearchPredicateFactory theF,
				BooleanPredicateClausesStep<?> theB, ValueSet.ConceptSetFilterComponent theFilter) {

		theB.mustNot(theF.match().field(IDX_PROP_KEY).matching("EXTERNAL_COPYRIGHT_NOTICE"));
	}


	private void addFilterLoincCopyright3rdParty(SearchPredicateFactory thePredicateFactory,
				BooleanPredicateClausesStep<?> theBooleanClause, ValueSet.ConceptSetFilterComponent theFilter) {
		//TODO GGG HS These used to be Term term = new Term(TermConceptPropertyBinder.CONCEPT_FIELD_PROPERTY_PREFIX + "EXTERNAL_COPYRIGHT_NOTICE", ".*");, which was lucene-specific.
		//TODO GGG HS ask diederik if this is equivalent.
		//This old .* regex is the same as an existence check on a field, which I've implemented here.
//		theBooleanClause.must(thePredicateFactory.exists().field("EXTERNAL_COPYRIGHT_NOTICE"));

		theBooleanClause.must(thePredicateFactory.match().field(IDX_PROP_KEY).matching("EXTERNAL_COPYRIGHT_NOTICE"));
	}

	@SuppressWarnings("EnumSwitchStatementWhichMissesCases")
	private void handleFilterLoincAncestor(String theSystem, SearchPredicateFactory f, BooleanPredicateClausesStep<?> b, ValueSet.ConceptSetFilterComponent theFilter) {
		switch (theFilter.getOp()) {
			case EQUAL:
				addLoincFilterAncestorEqual(theSystem, f, b, theFilter);
				break;
			case IN:
				addLoincFilterAncestorIn(theSystem, f, b, theFilter);
				break;
			default:
				throw new InvalidRequestException(Msg.code(892) + "Don't know how to handle op=" + theFilter.getOp() + " on property " + theFilter.getProperty());
		}

	}

	private void addLoincFilterAncestorEqual(String theSystem, SearchPredicateFactory f, BooleanPredicateClausesStep<?> b, ValueSet.ConceptSetFilterComponent theFilter) {
		long parentPid = getAncestorCodePid(theSystem, theFilter.getProperty(), theFilter.getValue());
		b.must(f.match().field("myParentPids").matching(String.valueOf(parentPid)));
	}


	private void addLoincFilterAncestorIn(String theSystem, SearchPredicateFactory f, BooleanPredicateClausesStep<?> b, ValueSet.ConceptSetFilterComponent theFilter) {
		String[] values = theFilter.getValue().split(",");
		List<Long> ancestorCodePidList = new ArrayList<>();
		for (String value : values) {
			ancestorCodePidList.add(getAncestorCodePid(theSystem, theFilter.getProperty(), value));
		}

		b.must(f.bool(innerB -> ancestorCodePidList.forEach(
			ancestorPid -> innerB.should(f.match().field("myParentPids").matching(String.valueOf(ancestorPid)))
		)));
	}


	@SuppressWarnings("EnumSwitchStatementWhichMissesCases")
	private void handleFilterLoincParentChild(SearchPredicateFactory f, BooleanPredicateClausesStep<?> b, ValueSet.ConceptSetFilterComponent theFilter) {
		switch (theFilter.getOp()) {
			case EQUAL:
				addLoincFilterParentChildEqual(f, b, theFilter.getProperty(), theFilter.getValue());
				break;
			case IN:
				addLoincFilterParentChildIn(f, b, theFilter);
				break;
			default:
				throw new InvalidRequestException(Msg.code(893) + "Don't know how to handle op=" + theFilter.getOp() + " on property " + theFilter.getProperty());
		}
	}

	private void addLoincFilterParentChildIn(SearchPredicateFactory f, BooleanPredicateClausesStep<?> b, ValueSet.ConceptSetFilterComponent theFilter) {
		String[] values = theFilter.getValue().split(",");
		b.minimumShouldMatchNumber(1);
		for (String value : values) {
			logFilteringValueOnProperty(value, theFilter.getProperty());
			b.should(getPropertyNameValueNestedPredicate(f, theFilter.getProperty(), value));
		}
	}

	private void addLoincFilterParentChildEqual(SearchPredicateFactory f, BooleanPredicateClausesStep<?> b, String theProperty, String theValue) {
		logFilteringValueOnProperty(theValue, theProperty);
		b.must(getPropertyNameValueNestedPredicate(f, theProperty, theValue));
	}

	/**
	 * A nested predicate is required for both predicates to be applied to same property, otherwise if properties
	 * propAA:valueAA and propBB:valueBB are defined, a search for propAA:valueBB would be a match
	 * @see "https://docs.jboss.org/hibernate/search/6.0/reference/en-US/html_single/#search-dsl-predicate-nested"
	 */
	private PredicateFinalStep getPropertyNameValueNestedPredicate(SearchPredicateFactory f, String theProperty, String theValue) {
		return f.nested().objectField(IDX_PROPERTIES).nest(
			f.bool()
				.must(f.match().field(IDX_PROP_KEY).matching(theProperty))
				.must(f.match().field(IDX_PROP_VALUE_STRING).field(IDX_PROP_DISPLAY_STRING).matching(theValue))
		);
	}


	private void handleFilterConceptAndCode(String theSystem, SearchPredicateFactory f, BooleanPredicateClausesStep<?> b, ValueSet.ConceptSetFilterComponent theFilter) {
		TermConcept code = findCodeForFilterCriteria(theSystem, theFilter);

		if (theFilter.getOp() == ValueSet.FilterOperator.ISA) {
			ourLog.debug(" * Filtering on codes with a parent of {}/{}/{}", code.getId(), code.getCode(), code.getDisplay());

			b.must(f.match().field("myParentPids").matching("" + code.getId()));
		} else {
			throwInvalidFilter(theFilter, "");
		}
	}

	@Nonnull
	private TermConcept findCodeForFilterCriteria(String theSystem, ValueSet.ConceptSetFilterComponent theFilter) {
		return findCode(theSystem, theFilter.getValue())
			.orElseThrow(() -> new InvalidRequestException(Msg.code(2071) + "Invalid filter criteria - code does not exist: {" + Constants.codeSystemWithDefaultDescription(theSystem) + "}" + theFilter.getValue()));
	}

	private void throwInvalidFilter(ValueSet.ConceptSetFilterComponent theFilter, String theErrorSuffix) {
		throw new InvalidRequestException(Msg.code(894) + "Don't know how to handle op=" + theFilter.getOp() + " on property " + theFilter.getProperty() + theErrorSuffix);
	}

	private void isCodeSystemLoincOrThrowInvalidRequestException(String theSystemIdentifier, String theProperty) {
		String systemUrl = getUrlFromIdentifier(theSystemIdentifier);
		if (!isCodeSystemLoinc(systemUrl)) {
			throw new InvalidRequestException(Msg.code(895) + "Invalid filter, property " + theProperty + " is LOINC-specific and cannot be used with system: " + systemUrl);
		}
	}

	private boolean isCodeSystemLoinc(String theSystem) {
		return LOINC_URI.equals(theSystem);
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

	private long getAncestorCodePid(String theSystem, String theProperty, String theValue) {
		TermConcept code = findCode(theSystem, theValue)
			.orElseThrow(() -> new InvalidRequestException("Invalid filter criteria - code does not exist: {" + Constants.codeSystemWithDefaultDescription(theSystem) + "}" + theValue));

		logFilteringValueOnProperty(theValue, theProperty);
		return code.getId();
	}

	@SuppressWarnings("EnumSwitchStatementWhichMissesCases")
	private void handleFilterLoincDescendant(String theSystem, SearchPredicateFactory f, BooleanPredicateClausesStep<?> b, ValueSet.ConceptSetFilterComponent theFilter) {
		switch (theFilter.getOp()) {
			case EQUAL:
				addLoincFilterDescendantEqual(theSystem, f, b, theFilter);
				break;
			case IN:
				addLoincFilterDescendantIn(theSystem, f, b, theFilter);
				break;
			default:
				throw new InvalidRequestException(Msg.code(896) + "Don't know how to handle op=" + theFilter.getOp() + " on property " + theFilter.getProperty());
		}
	}


	private void addLoincFilterDescendantEqual(String theSystem, SearchPredicateFactory f,
			BooleanPredicateClausesStep<?> b, ValueSet.ConceptSetFilterComponent theFilter) {

		List<Long> parentPids = getCodeParentPids(theSystem, theFilter.getProperty(), theFilter.getValue());
		if (parentPids.isEmpty()) {
			// Can't return empty must, because it wil match according to other predicates.
			// Some day there will be a 'matchNone' predicate (https://discourse.hibernate.org/t/fail-fast-predicate/6062)
			b.mustNot( f.matchAll() );
			return;
		}

		b.must(f.bool(innerB -> {
			innerB.minimumShouldMatchNumber(1);
			parentPids.forEach(pid -> innerB.should(f.match().field("myId").matching(pid)));
		}));

	}

	/**
	 * We are looking for codes which have codes indicated in theFilter.getValue() as descendants.
	 * Strategy is to find codes which have their pId(s) in the list of the parentId(s) of all the TermConcept(s)
	 * representing the codes in theFilter.getValue()
	 */
	private void addLoincFilterDescendantIn(String theSystem, SearchPredicateFactory f,
			BooleanPredicateClausesStep<?> b, ValueSet.ConceptSetFilterComponent theFilter) {

		String[] values = theFilter.getValue().split(",");
		if (values.length == 0) {
			throw new InvalidRequestException(Msg.code(2062) + "Invalid filter criteria - no codes specified");
		}

		List<Long> descendantCodePidList = getMultipleCodeParentPids(theSystem, theFilter.getProperty(), values);

		b.must(f.bool(innerB -> descendantCodePidList.forEach(
			pId -> innerB.should(f.match().field("myId").matching(pId))
		)));
	}


	/**
	 * Returns the list of parentId(s) of the TermConcept representing theValue as a code
	 */
	private List<Long> getCodeParentPids(String theSystem, String theProperty, String theValue) {
		TermConcept code = findCode(theSystem, theValue)
			.orElseThrow(() -> new InvalidRequestException("Invalid filter criteria - code does not exist: {" +
				Constants.codeSystemWithDefaultDescription(theSystem) + "}" + theValue));

		String[] parentPids = code.getParentPidsAsString().split(" ");
		List<Long> retVal = Arrays.stream(parentPids)
			.filter( pid -> !StringUtils.equals(pid, "NONE") )
			.map(Long::parseLong)
			.collect(Collectors.toList());
		logFilteringValueOnProperty(theValue, theProperty);
		return retVal;
	}


	/**
	 * Returns the list of parentId(s) of the TermConcept representing theValue as a code
	 */
	private List<Long> getMultipleCodeParentPids(String theSystem, String theProperty, String[] theValues) {
		List<String> valuesList = Arrays.asList(theValues);
		List<TermConcept> termConcepts = findCodes(theSystem, valuesList);
		if (valuesList.size() != termConcepts.size()) {
			String exMsg = getTermConceptsFetchExceptionMsg(termConcepts, valuesList);
			throw new InvalidRequestException(Msg.code(2064) + "Invalid filter criteria - {" +
				Constants.codeSystemWithDefaultDescription(theSystem) + "}: " + exMsg);
		}

		List<Long> retVal = termConcepts.stream()
			.flatMap(tc -> Arrays.stream(tc.getParentPidsAsString().split(" ")))
			.filter( pid -> !StringUtils.equals(pid, "NONE") )
			.map(Long::parseLong)
			.collect(Collectors.toList());

		logFilteringValueOnProperties(valuesList, theProperty);

		return retVal;
	}

	/**
	 * Generate message indicating for which of theValues a TermConcept was not found
	 */
	private String getTermConceptsFetchExceptionMsg(List<TermConcept> theTermConcepts, List<String> theValues) {
		// case: more TermConcept(s) retrieved than codes queried
		if (theTermConcepts.size() > theValues.size()) {
			return "Invalid filter criteria - More TermConcepts were found than indicated codes. Queried codes: [" +
				join(",", theValues + "]; Obtained TermConcept IDs, codes: [" +
					theTermConcepts.stream().map(tc -> tc.getId() + ", " + tc.getCode())
						.collect(joining("; "))+ "]");
		}

		// case: less TermConcept(s) retrieved than codes queried
		Set<String> matchedCodes = theTermConcepts.stream().map(TermConcept::getCode).collect(toSet());
		List<String> notMatchedValues = theValues.stream()
			.filter(v -> ! matchedCodes.contains (v)) .collect(toList());

		return "Invalid filter criteria - No TermConcept(s) were found for the requested codes: [" +
			join(",", notMatchedValues + "]");
	}


	private void logFilteringValueOnProperty(String theValue, String theProperty) {
		ourLog.debug(" * Filtering with value={} on property {}", theValue, theProperty);
	}

	private void logFilteringValueOnProperties(List<String> theValues, String theProperty) {
		ourLog.debug(" * Filtering with values={} on property {}", String.join(", ", theValues), theProperty);
	}

	private void throwInvalidRequestForOpOnProperty(ValueSet.FilterOperator theOp, String theProperty) {
		throw new InvalidRequestException(Msg.code(897) + "Don't know how to handle op=" + theOp + " on property " + theProperty);
	}

	private void throwInvalidRequestForValueOnProperty(String theValue, String theProperty) {
		throw new InvalidRequestException(Msg.code(898) + "Don't know how to handle value=" + theValue + " on property " + theProperty);
	}

	private void expandWithoutHibernateSearch(IValueSetConceptAccumulator theValueSetCodeAccumulator, TermCodeSystemVersion theVersion, Set<String> theAddedCodes, ValueSet.ConceptSetComponent theInclude, String theSystem, boolean theAdd) {
		ourLog.trace("Hibernate search is not enabled");

		if (theValueSetCodeAccumulator instanceof ValueSetExpansionComponentWithConceptAccumulator) {
			Validate.isTrue(((ValueSetExpansionComponentWithConceptAccumulator) theValueSetCodeAccumulator).getParameter().isEmpty(), "Can not expand ValueSet with parameters - Hibernate Search is not enabled on this server.");
		}

		Validate.isTrue(isNotBlank(theSystem), "Can not expand ValueSet without explicit system - Hibernate Search is not enabled on this server.");

		for (ValueSet.ConceptSetFilterComponent nextFilter : theInclude.getFilter()) {
			boolean handled = false;
			switch (nextFilter.getProperty()) {
				case "concept":
				case "code":
					if (nextFilter.getOp() == ValueSet.FilterOperator.ISA) {
						theValueSetCodeAccumulator.addMessage("Processing IS-A filter in database - Note that Hibernate Search is not enabled on this server, so this operation can be inefficient.");
						TermConcept code = findCodeForFilterCriteria(theSystem, nextFilter);
						addConceptAndChildren(theValueSetCodeAccumulator, theAddedCodes, theInclude, theSystem, theAdd, code);
						handled = true;
					}
					break;
			}

			if (!handled) {
				throwInvalidFilter(nextFilter, " - Note that Hibernate Search is disabled on this server so not all ValueSet expansion funtionality is available.");
			}
		}

		if (theInclude.getConcept().isEmpty()) {

			Collection<TermConcept> concepts = myConceptDao.fetchConceptsAndDesignationsByVersionPid(theVersion.getPid());
			for (TermConcept next : concepts) {
				addCodeIfNotAlreadyAdded(theValueSetCodeAccumulator, theAddedCodes, theAdd, theSystem, theInclude.getVersion(), next.getCode(), next.getDisplay(), next.getId(), next.getParentPidsAsString(), next.getDesignations());
			}
		}

		for (ValueSet.ConceptReferenceComponent next : theInclude.getConcept()) {
			if (!theSystem.equals(theInclude.getSystem()) && isNotBlank(theSystem)) {
				continue;
			}
			Collection<TermConceptDesignation> designations = next
				.getDesignation()
				.stream()
				.map(t->new TermConceptDesignation()
					.setValue(t.getValue())
					.setLanguage(t.getLanguage())
					.setUseCode(t.getUse().getCode())
					.setUseSystem(t.getUse().getSystem())
					.setUseDisplay(t.getUse().getDisplay())
				)
				.collect(Collectors.toList());
			addCodeIfNotAlreadyAdded(theValueSetCodeAccumulator, theAddedCodes, theAdd, theSystem, theInclude.getVersion(), next.getCode(), next.getDisplay(), null, null, designations);
		}


	}

	private void addConceptAndChildren(IValueSetConceptAccumulator theValueSetCodeAccumulator, Set<String> theAddedCodes, ValueSet.ConceptSetComponent theInclude, String theSystem, boolean theAdd, TermConcept theConcept) {
		for (TermConcept nextChild : theConcept.getChildCodes()) {
			boolean added = addCodeIfNotAlreadyAdded(theValueSetCodeAccumulator, theAddedCodes, theAdd, theSystem, theInclude.getVersion(), nextChild.getCode(), nextChild.getDisplay(), nextChild.getId(), nextChild.getParentPidsAsString(), nextChild.getDesignations());
			if (added) {
				addConceptAndChildren(theValueSetCodeAccumulator, theAddedCodes, theInclude, theSystem, theAdd, nextChild);
			}
		}
	}

	@Override
	@Transactional
	public String invalidatePreCalculatedExpansion(IIdType theValueSetId, RequestDetails theRequestDetails) {
		IBaseResource valueSet = myDaoRegistry.getResourceDao("ValueSet").read(theValueSetId, theRequestDetails);
		ValueSet canonicalValueSet = toCanonicalValueSet(valueSet);
		Optional<TermValueSet> optionalTermValueSet = fetchValueSetEntity(canonicalValueSet);
		if (!optionalTermValueSet.isPresent()) {
			return myContext.getLocalizer().getMessage(BaseTermReadSvcImpl.class, "valueSetNotFoundInTerminologyDatabase", theValueSetId);
		}

		ourLog.info("Invalidating pre-calculated expansion on ValueSet {} / {}", theValueSetId, canonicalValueSet.getUrl());

		TermValueSet termValueSet = optionalTermValueSet.get();
		if (termValueSet.getExpansionStatus() == TermValueSetPreExpansionStatusEnum.NOT_EXPANDED) {
			return myContext.getLocalizer().getMessage(BaseTermReadSvcImpl.class, "valueSetCantInvalidateNotYetPrecalculated", termValueSet.getUrl(), termValueSet.getExpansionStatus());
		}

		Long totalConcepts = termValueSet.getTotalConcepts();

		deletePreCalculatedValueSetContents(termValueSet);

		termValueSet.setExpansionStatus(TermValueSetPreExpansionStatusEnum.NOT_EXPANDED);
		termValueSet.setExpansionTimestamp(null);
		myTermValueSetDao.save(termValueSet);
		return myContext.getLocalizer().getMessage(BaseTermReadSvcImpl.class, "valueSetPreExpansionInvalidated", termValueSet.getUrl(), totalConcepts);
	}

	@Override
	@Transactional
	public boolean isValueSetPreExpandedForCodeValidation(ValueSet theValueSet) {
		Optional<TermValueSet> optionalTermValueSet = fetchValueSetEntity(theValueSet);

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

	private Optional<TermValueSet> fetchValueSetEntity(ValueSet theValueSet) {
		ResourcePersistentId valueSetResourcePid = getValueSetResourcePersistentId(theValueSet);
		Optional<TermValueSet> optionalTermValueSet = myTermValueSetDao.findByResourcePid(valueSetResourcePid.getIdAsLong());
		return optionalTermValueSet;
	}

	private ResourcePersistentId getValueSetResourcePersistentId(ValueSet theValueSet) {
		ResourcePersistentId valueSetResourcePid = myIdHelperService.resolveResourcePersistentIds(RequestPartitionId.allPartitions(), theValueSet.getIdElement().getResourceType(), theValueSet.getIdElement().getIdPart());
		return valueSetResourcePid;
	}

	protected IValidationSupport.CodeValidationResult validateCodeIsInPreExpandedValueSet(
		ConceptValidationOptions theValidationOptions,
		ValueSet theValueSet, String theSystem, String theCode, String theDisplay, Coding theCoding, CodeableConcept theCodeableConcept) {
		assert TransactionSynchronizationManager.isSynchronizationActive();

		ValidateUtil.isNotNullOrThrowUnprocessableEntity(theValueSet.hasId(), "ValueSet.id is required");
		ResourcePersistentId valueSetResourcePid = getValueSetResourcePersistentId(theValueSet);


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

		TermValueSet valueSetEntity = myTermValueSetDao.findByResourcePid(valueSetResourcePid.getIdAsLong()).orElseThrow(() -> new IllegalStateException());
		String timingDescription = toHumanReadableExpansionTimestamp(valueSetEntity);
		String msg = myContext.getLocalizer().getMessage(BaseTermReadSvcImpl.class, "validationPerformedAgainstPreExpansion", timingDescription);

		if (theValidationOptions.isValidateDisplay() && concepts.size() > 0) {
			String systemVersion = null;
			for (TermValueSetConcept concept : concepts) {
				systemVersion = concept.getSystemVersion();
				if (isBlank(theDisplay) || isBlank(concept.getDisplay()) || theDisplay.equals(concept.getDisplay())) {
					return new IValidationSupport.CodeValidationResult()
						.setCode(concept.getCode())
						.setDisplay(concept.getDisplay())
						.setCodeSystemVersion(concept.getSystemVersion())
						.setMessage(msg);
				}
			}

			return createFailureCodeValidationResult(theSystem, theCode, systemVersion, " - Concept Display \"" + theDisplay + "\" does not match expected \"" + concepts.get(0).getDisplay() + "\". " + msg).setDisplay(concepts.get(0).getDisplay());
		}

		if (!concepts.isEmpty()) {
			return new IValidationSupport.CodeValidationResult()
				.setCode(concepts.get(0).getCode())
				.setDisplay(concepts.get(0).getDisplay())
				.setCodeSystemVersion(concepts.get(0).getSystemVersion())
				.setMessage(msg);
		}
		
		// Ok, we failed
		List<TermValueSetConcept> outcome = myValueSetConceptDao.findByTermValueSetIdSystemOnly(Pageable.ofSize(1), valueSetEntity.getId(), theSystem);
		String append;
		if (outcome.size() == 0) {
			append = " - No codes in ValueSet belong to CodeSystem with URL " + theSystem;
		} else {
			append = " - Unknown code " + theSystem + "#" + theCode + ". " + msg;
		}

		return createFailureCodeValidationResult(theSystem, theCode, null, append);
	}

	private CodeValidationResult createFailureCodeValidationResult(String theSystem, String theCode, String theCodeSystemVersion, String theAppend) {
		return new CodeValidationResult()
			.setSeverity(IssueSeverity.ERROR)
			.setCodeSystemVersion(theCodeSystemVersion)
			.setMessage("Unable to validate code " + theSystem + "#" + theCode + theAppend);
	}

	private List<TermValueSetConcept> findByValueSetResourcePidSystemAndCode(ResourcePersistentId theResourcePid, String theSystem, String theCode) {
		assert TransactionSynchronizationManager.isSynchronizationActive();

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


	@Transactional(propagation = Propagation.MANDATORY)
	public List<TermConcept> findCodes(String theCodeSystem, List<String> theCodeList) {
		TermCodeSystemVersion csv = getCurrentCodeSystemVersion(theCodeSystem);
		if (csv == null) { return Collections.emptyList(); }

		return myConceptDao.findByCodeSystemAndCodeList(csv, theCodeList);
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
		scheduleJob();
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
	public synchronized void preExpandDeferredValueSetsToTerminologyTables() {
		if (!myDaoConfig.isEnableTaskPreExpandValueSets()) {
			return;
		}
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
				termValueSet.setTotalConcepts(0L);
				termValueSet.setTotalConceptDesignations(0L);
				termValueSet.setExpansionStatus(TermValueSetPreExpansionStatusEnum.EXPANSION_IN_PROGRESS);
				return myTermValueSetDao.saveAndFlush(termValueSet);
			});
			if (valueSetToExpand == null) {
				return;
			}

			// We have a ValueSet to pre-expand.
			setPreExpandingValueSets(true);
			try {
				ValueSet valueSet = txTemplate.execute(t -> {
					TermValueSet refreshedValueSetToExpand = myTermValueSetDao.findById(valueSetToExpand.getId()).orElseThrow(() -> new IllegalStateException("Unknown VS ID: " + valueSetToExpand.getId()));
					return getValueSetFromResourceTable(refreshedValueSetToExpand.getResource());
				});
				assert valueSet != null;

				ValueSetConceptAccumulator accumulator = new ValueSetConceptAccumulator(valueSetToExpand, myTermValueSetDao, myValueSetConceptDao, myValueSetConceptDesignationDao);
				ValueSetExpansionOptions options = new ValueSetExpansionOptions();
				options.setIncludeHierarchy(true);
				expandValueSet(options, valueSet, accumulator);

				// We are done with this ValueSet.
				txTemplate.executeWithoutResult(t -> {
					valueSetToExpand.setExpansionStatus(TermValueSetPreExpansionStatusEnum.EXPANDED);
					valueSetToExpand.setExpansionTimestamp(new Date());
					myTermValueSetDao.saveAndFlush(valueSetToExpand);

				});

				ourLog.info("Pre-expanded ValueSet[{}] with URL[{}] - Saved {} concepts in {}", valueSet.getId(), valueSet.getUrl(), accumulator.getConceptsSaved(), sw);

			} catch (Exception e) {
				ourLog.error("Failed to pre-expand ValueSet: " + e.getMessage(), e);
				txTemplate.executeWithoutResult(t -> {
					valueSetToExpand.setExpansionStatus(TermValueSetPreExpansionStatusEnum.FAILED_TO_EXPAND);
					myTermValueSetDao.saveAndFlush(valueSetToExpand);

				});

			} finally {
				setPreExpandingValueSets(false);
			}
		}
	}

	private synchronized void setPreExpandingValueSets(boolean thePreExpandingValueSets) {
		myPreExpandingValueSets = thePreExpandingValueSets;
	}

	private synchronized boolean isPreExpandingValueSets() {
		return myPreExpandingValueSets;
	}

	@Override
	@Transactional
	public CodeValidationResult validateCode(ConceptValidationOptions theOptions, IIdType theValueSetId, String theValueSetIdentifier, String theCodeSystemIdentifierToValidate, String theCodeToValidate, String theDisplayToValidate, IBaseDatatype theCodingToValidate, IBaseDatatype theCodeableConceptToValidate) {

		CodeableConcept codeableConcept = toCanonicalCodeableConcept(theCodeableConceptToValidate);
		boolean haveCodeableConcept = codeableConcept != null && codeableConcept.getCoding().size() > 0;

		Coding canonicalCodingToValidate = toCanonicalCoding(theCodingToValidate);
		boolean haveCoding = canonicalCodingToValidate != null && canonicalCodingToValidate.isEmpty() == false;

		boolean haveCode = theCodeToValidate != null && theCodeToValidate.isEmpty() == false;

		if (!haveCodeableConcept && !haveCoding && !haveCode) {
			throw new InvalidRequestException(Msg.code(899) + "No code, coding, or codeableConcept provided to validate");
		}
		if (!LogicUtil.multiXor(haveCodeableConcept, haveCoding, haveCode)) {
			throw new InvalidRequestException(Msg.code(900) + "$validate-code can only validate (system AND code) OR (coding) OR (codeableConcept)");
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
			throw new InvalidRequestException(Msg.code(901) + "Either ValueSet ID or ValueSet identifier or system and code must be provided. Unable to validate.");
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
		Slice<TermValueSet> page = myTermValueSetDao.findByExpansionStatus(PageRequest.of(0, 1), TermValueSetPreExpansionStatusEnum.NOT_EXPANDED);

		if (!page.getContent().isEmpty()) {
			retVal = Optional.of(page.getContent().get(0));
		}

		return retVal;
	}

	@Override
	@Transactional
	public void storeTermValueSet(ResourceTable theResourceTable, ValueSet theValueSet) {

		ValidateUtil.isTrueOrThrowInvalidRequest(theResourceTable != null, "No resource supplied");
		if (isPlaceholder(theValueSet)) {
			ourLog.info("Not storing TermValueSet for placeholder {}", theValueSet.getIdElement().toVersionless().getValueAsString());
			return;
		}

		ValidateUtil.isNotBlankOrThrowUnprocessableEntity(theValueSet.getUrl(), "ValueSet has no value for ValueSet.url");
		ourLog.info("Storing TermValueSet for {}", theValueSet.getIdElement().toVersionless().getValueAsString());

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
			optionalExistingTermValueSetByUrl = myTermValueSetDao.findTermValueSetByUrlAndVersion(url, version);
		} else {
			optionalExistingTermValueSetByUrl = myTermValueSetDao.findTermValueSetByUrlAndNullVersion(url);
		}
		if (!optionalExistingTermValueSetByUrl.isPresent()) {

			myTermValueSetDao.save(termValueSet);

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
			throw new UnprocessableEntityException(Msg.code(902) + msg);
		}
	}

	@Override
	@Transactional
	public IFhirResourceDaoCodeSystem.SubsumesResult subsumes(IPrimitiveType<String> theCodeA, IPrimitiveType<String> theCodeB,
																				 IPrimitiveType<String> theSystem, IBaseCoding theCodingA, IBaseCoding theCodingB) {
		FhirVersionIndependentConcept conceptA = toConcept(theCodeA, theSystem, theCodingA);
		FhirVersionIndependentConcept conceptB = toConcept(theCodeB, theSystem, theCodingB);

		if (!StringUtils.equals(conceptA.getSystem(), conceptB.getSystem())) {
			throw new InvalidRequestException(Msg.code(903) + "Unable to test subsumption across different code systems");
		}

		if (!StringUtils.equals(conceptA.getSystemVersion(), conceptB.getSystemVersion())) {
			throw new InvalidRequestException(Msg.code(904) + "Unable to test subsumption across different code system versions");
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

	protected IValidationSupport.LookupCodeResult lookupCode(String theSystem, String theCode, String theDisplayLanguage) {
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
					// filter out the designation based on displayLanguage if any
					if (isDisplayLanguageMatch(theDisplayLanguage, next.getLanguage())) {
						IValidationSupport.ConceptDesignation designation = new IValidationSupport.ConceptDesignation();
						designation.setLanguage(next.getLanguage());
						designation.setUseSystem(next.getUseSystem());
						designation.setUseCode(next.getUseCode());
						designation.setUseDisplay(next.getUseDisplay());
						designation.setValue(next.getValue());
						result.getDesignations().add(designation);
					}
				}

				for (TermConceptProperty next : code.getProperties()) {
					if (next.getType() == TermConceptPropertyTypeEnum.CODING) {
						IValidationSupport.CodingConceptProperty property = new IValidationSupport.CodingConceptProperty(next.getKey(), next.getCodeSystem(), next.getValue(), next.getDisplay());
						result.getProperties().add(property);
					} else if (next.getType() == TermConceptPropertyTypeEnum.STRING) {
						IValidationSupport.StringConceptProperty property = new IValidationSupport.StringConceptProperty(next.getKey(), next.getValue());
						result.getProperties().add(property);
					} else {
						throw new InternalErrorException(Msg.code(905) + "Unknown type: " + next.getType());
					}
				}

				return result;

			} else {
				return new LookupCodeResult()
					.setFound(false);
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
	public IValidationSupport.CodeValidationResult validateCode(@Nonnull ValidationSupportContext theValidationSupportContext, @Nonnull ConceptValidationOptions theOptions, String theCodeSystem, String theCode, String theDisplay, String theValueSetUrl) {
		//TODO GGG TRY TO JUST AUTO_PASS HERE AND SEE WHAT HAPPENS.
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
				return createFailureCodeValidationResult(theCodeSystem, theCode, code.getSystemVersion(), " - Concept Display \"" + code.getDisplay() + "\" does not match expected \"" + code.getDisplay() + "\"").setDisplay(code.getDisplay());
			}
		}

		return createFailureCodeValidationResult(theCodeSystem, theCode, null, " - Code can not be found in CodeSystem");
	}

	IValidationSupport.CodeValidationResult validateCodeInValueSet(ValidationSupportContext theValidationSupportContext, ConceptValidationOptions theValidationOptions, String theValueSetUrl, String theCodeSystem, String theCode, String theDisplay) {
		IBaseResource valueSet = theValidationSupportContext.getRootValidationSupport().fetchValueSet(theValueSetUrl);
		CodeValidationResult retVal = null;

		// If we don't have a PID, this came from some source other than the JPA
		// database, so we don't need to check if it's pre-expanded or not
		if (valueSet instanceof IAnyResource) {
			Long pid = IDao.RESOURCE_PID.get((IAnyResource) valueSet);
			if (pid != null) {
				TransactionTemplate txTemplate = new TransactionTemplate(myTxManager);
				retVal = txTemplate.execute(tx -> {
					if (isValueSetPreExpandedForCodeValidation(valueSet)) {
						return validateCodeIsInPreExpandedValueSet(theValidationOptions, valueSet, theCodeSystem, theCode, theDisplay, null, null);
					} else {
						return null;
					}
				});
			}
		}

		if (retVal == null) {
			if (valueSet != null) {
				retVal = new InMemoryTerminologyServerValidationSupport(myContext).validateCodeInValueSet(theValidationSupportContext, theValidationOptions, theCodeSystem, theCode, theDisplay, valueSet);
			} else {
				String append = " - Unable to locate ValueSet[" + theValueSetUrl + "]";
				retVal = createFailureCodeValidationResult(theCodeSystem, theCode, null, append);
			}
		}

		// Check if someone is accidentally using a VS url where it should be a CS URL
		if (retVal != null && retVal.getCode() == null && theCodeSystem != null) {
			if (isValueSetSupported(theValidationSupportContext, theCodeSystem)) {
				if (!isCodeSystemSupported(theValidationSupportContext, theCodeSystem)) {
					String newMessage = "Unable to validate code " + theCodeSystem + "#" + theCode + " - Supplied system URL is a ValueSet URL and not a CodeSystem URL, check if it is correct: " + theCodeSystem;
					retVal.setMessage(newMessage);
				}
			}
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

	@Nonnull
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
			throw new InvalidRequestException(Msg.code(906) + "No code, coding, or codeableConcept provided to validate.");
		}
		if (!LogicUtil.multiXor(haveCodeableConcept, haveCoding, haveCode)) {
			throw new InvalidRequestException(Msg.code(907) + "$validate-code can only validate (code) OR (coding) OR (codeableConcept)");
		}

		boolean haveIdentifierParam = isNotBlank(theCodeSystemUrl);
		String codeSystemUrl;
		if (theCodeSystemId != null) {
			IBaseResource codeSystem = myDaoRegistry.getResourceDao("CodeSystem").read(theCodeSystemId);
			codeSystemUrl = CommonCodeSystemsTerminologyService.getCodeSystemUrl(codeSystem);
		} else if (haveIdentifierParam) {
			codeSystemUrl = theCodeSystemUrl;
		} else {
			throw new InvalidRequestException(Msg.code(908) + "Either CodeSystem ID or CodeSystem identifier must be provided. Unable to validate.");
		}


		String code = theCode;
		String display = theDisplay;

		if (haveCodeableConcept) {
			for (int i = 0; i < codeableConcept.getCoding().size(); i++) {
				Coding nextCoding = codeableConcept.getCoding().get(i);
				if (nextCoding.hasSystem()) {
					if (!codeSystemUrl.equalsIgnoreCase(nextCoding.getSystem())) {
						throw new InvalidRequestException(Msg.code(909) + "Coding.system '" + nextCoding.getSystem() + "' does not equal with CodeSystem.url '" + theCodeSystemUrl + "'. Unable to validate.");
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
					throw new InvalidRequestException(Msg.code(910) + "Coding.system '" + coding.getSystem() + "' does not equal with CodeSystem.url '" + theCodeSystemUrl + "'. Unable to validate.");
				}
				codeSystemUrl = coding.getSystem();
			}
			code = coding.getCode();
			display = coding.getDisplay();
		}

		return codeSystemValidateCode(codeSystemUrl, theVersion, code, display);
	}

	/**
	 * When the search is for unversioned loinc system it uses the forcedId to obtain the current
	 * version, as it is not necessarily the last  one anymore.
	 * For other cases it keeps on considering the last uploaded as the current
	 */
	@Override
	public Optional<TermValueSet> findCurrentTermValueSet(String theUrl) {
		if (TermReadSvcUtil.isLoincUnversionedValueSet(theUrl)) {
			Optional<String> vsIdOpt = TermReadSvcUtil.getValueSetId(theUrl);
			if (!vsIdOpt.isPresent()) {
				return Optional.empty();
			}

			return myTermValueSetDao.findTermValueSetByForcedId(vsIdOpt.get());
		}

		List<TermValueSet> termValueSetList = myTermValueSetDao.findTermValueSetByUrl(Pageable.ofSize(1), theUrl);
		if (termValueSetList.isEmpty()) {
			return Optional.empty();
		}

		return Optional.of(termValueSetList.get(0));
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

		if (isNoneBlank(theCodeSystemUrl)) {
			predicates.add(criteriaBuilder.equal(systemJoin.get("myCodeSystemUri"), theCodeSystemUrl));
		}

		// for loinc CodeSystem last version is not necessarily the current anymore, so if no version is present
		// we need to query for the current, which is that which version is null
		if (isNoneBlank(theCodeSystemVersion)) {
			predicates.add(criteriaBuilder.equal(systemVersionJoin.get("myCodeSystemVersionId"), theCodeSystemVersion));
		} else {
			if (theCodeSystemUrl.toLowerCase(Locale.ROOT).contains(LOINC_LOW)) {
				predicates.add(criteriaBuilder.isNull(systemVersionJoin.get("myCodeSystemVersionId")));
			} else {
				query.orderBy(criteriaBuilder.desc(root.get("myUpdated")));
			}
		}

		Predicate outerPredicate = criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		query.where(outerPredicate);

		final TypedQuery<TermConcept> typedQuery = myEntityManager.createQuery(query.select(root));
		org.hibernate.query.Query<TermConcept> hibernateQuery = (org.hibernate.query.Query<TermConcept>) typedQuery;
		hibernateQuery.setFetchSize(SINGLE_FETCH_SIZE);
		List<TermConcept> resultsList = hibernateQuery.getResultList();

		if (!resultsList.isEmpty()) {
			TermConcept concept = resultsList.get(0);

			if (isNotBlank(theDisplay) && !theDisplay.equals(concept.getDisplay())) {
				String message = "Concept Display \"" + theDisplay + "\" does not match expected \"" + concept.getDisplay() + "\" for CodeSystem: " + theCodeSystemUrl;
				return createFailureCodeValidationResult(theCodeSystemUrl, theCode, theCodeSystemVersion, message);
			}

			return new CodeValidationResult().setCode(concept.getCode()).setDisplay(concept.getDisplay());
		}

		return createFailureCodeValidationResult(theCodeSystemUrl, theCode, theCodeSystemVersion, " - Code is not found in CodeSystem: " + theCodeSystemUrl);
	}

	@Override
	public Optional<IBaseResource> readCodeSystemByForcedId(String theForcedId) {
		@SuppressWarnings("unchecked")
		List<ResourceTable> resultList = (List<ResourceTable>) myEntityManager.createQuery(
			"select f.myResource from ForcedId f " +
				"where f.myResourceType = 'CodeSystem' and f.myForcedId = '" + theForcedId + "'").getResultList();
		if (resultList.isEmpty()) return Optional.empty();

		if (resultList.size() > 1)
			throw new NonUniqueResultException(Msg.code(911) + "More than one CodeSystem is pointed by forcedId: " + theForcedId + ". Was constraint "
				+ ForcedId.IDX_FORCEDID_TYPE_FID + " removed?");

		IFhirResourceDao<CodeSystem> csDao = myDaoRegistry.getResourceDao("CodeSystem");
		IBaseResource cs = csDao.toResource(resultList.get(0), false);
		return Optional.of(cs);
	}


	private static final int SECONDS_IN_MINUTE = 60;
	private static final int INDEXED_ROOTS_LOGGING_COUNT = 50_000;


	@Transactional
	@Override
	public ReindexTerminologyResult reindexTerminology() throws InterruptedException {
		if (myFulltextSearchSvc == null) {
			return ReindexTerminologyResult.SEARCH_SVC_DISABLED;
		}

		if (isBatchTerminologyTasksRunning()) {
			return ReindexTerminologyResult.OTHER_BATCH_TERMINOLOGY_TASKS_RUNNING;
		}

		// disallow pre-expanding ValueSets while reindexing
		myDeferredStorageSvc.setProcessDeferred(false);

		int objectLoadingThreadNumber = calculateObjectLoadingThreadNumber();
		ourLog.info("Using {} threads to load objects", objectLoadingThreadNumber);

		try {
			SearchSession searchSession = getSearchSession();
			searchSession
				.massIndexer( TermConcept.class )
				.dropAndCreateSchemaOnStart( true )
				.purgeAllOnStart( false )
				.batchSizeToLoadObjects( 100 )
				.cacheMode( CacheMode.IGNORE )
				.threadsToLoadObjects( 6 )
				.transactionTimeout( 60 * SECONDS_IN_MINUTE )
				.monitor( new PojoMassIndexingLoggingMonitor(INDEXED_ROOTS_LOGGING_COUNT) )
				.startAndWait();
		} finally {
			myDeferredStorageSvc.setProcessDeferred(true);
		}

		return ReindexTerminologyResult.SUCCESS;
	}


	@VisibleForTesting
	boolean isBatchTerminologyTasksRunning() {
		return isNotSafeToPreExpandValueSets() || isPreExpandingValueSets();
	}


	@VisibleForTesting
	int calculateObjectLoadingThreadNumber() {
		IConnectionPoolInfoProvider connectionPoolInfoProvider =
			new ConnectionPoolInfoProvider(myHibernatePropertiesProvider.getDataSource());
		Optional<Integer> maxConnectionsOpt = connectionPoolInfoProvider.getTotalConnectionSize();
		if ( ! maxConnectionsOpt.isPresent() ) {
			return DEFAULT_MASS_INDEXER_OBJECT_LOADING_THREADS;
		}

		int maxConnections = maxConnectionsOpt.get();
		int usableThreads = maxConnections < 6 ? 1 : maxConnections - 5;
		int objectThreads = Math.min(usableThreads, MAX_MASS_INDEXER_OBJECT_LOADING_THREADS);
		ourLog.debug("Data source connection pool has {} connections allocated, so reindexing will use {} object " +
			"loading threads (each using a connection)", maxConnections, objectThreads);
		return objectThreads;
	}


	@VisibleForTesting
	SearchSession getSearchSession() {
		return Search.session( myEntityManager );
	}


	@VisibleForTesting
	public static void setForceDisableHibernateSearchForUnitTest(boolean theForceDisableHibernateSearchForUnitTest) {
		ourForceDisableHibernateSearchForUnitTest = theForceDisableHibernateSearchForUnitTest;
	}

	static boolean isPlaceholder(DomainResource theResource) {
		boolean retVal = false;
		Extension extension = theResource.getExtensionByUrl(HapiExtensions.EXT_RESOURCE_PLACEHOLDER);
		if (extension != null && extension.hasValue() && extension.getValue() instanceof BooleanType) {
			retVal = ((BooleanType) extension.getValue()).booleanValue();
		}
		return retVal;
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

	static boolean isDisplayLanguageMatch(String theReqLang, String theStoredLang) {
		// NOTE: return the designation when one of then is not specified.
		if (theReqLang == null || theStoredLang == null)
			return true;

		return theReqLang.equalsIgnoreCase(theStoredLang);
	}

	public static class Job implements HapiJob {
		@Autowired
		private ITermReadSvc myTerminologySvc;

		@Override
		public void execute(JobExecutionContext theContext) {
			myTerminologySvc.preExpandDeferredValueSetsToTerminologyTables();
		}
	}

}
