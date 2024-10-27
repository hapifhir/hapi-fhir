/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.LookupCodeRequest;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.context.support.ValueSetExpansionOptions;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IDao;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoCodeSystem;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.config.HibernatePropertiesProvider;
import ca.uhn.fhir.jpa.config.util.ConnectionPoolInfoProvider;
import ca.uhn.fhir.jpa.config.util.IConnectionPoolInfoProvider;
import ca.uhn.fhir.jpa.dao.IFulltextSearchSvc;
import ca.uhn.fhir.jpa.dao.IJpaStorageResourceParser;
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
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.sched.HapiJob;
import ca.uhn.fhir.jpa.model.sched.IHasScheduledJobs;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.model.sched.ScheduledJobDefinition;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.search.builder.SearchBuilder;
import ca.uhn.fhir.jpa.term.api.ITermDeferredStorageSvc;
import ca.uhn.fhir.jpa.term.api.ITermReadSvc;
import ca.uhn.fhir.jpa.term.api.ReindexTerminologyResult;
import ca.uhn.fhir.jpa.term.ex.ExpansionTooCostlyException;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.sl.cache.Cache;
import ca.uhn.fhir.sl.cache.CacheFactory;
import ca.uhn.fhir.util.CoverageIgnore;
import ca.uhn.fhir.util.FhirVersionIndependentConcept;
import ca.uhn.fhir.util.HapiExtensions;
import ca.uhn.fhir.util.StopWatch;
import ca.uhn.fhir.util.UrlUtil;
import ca.uhn.fhir.util.ValidateUtil;
import ca.uhn.hapi.converters.canonical.VersionCanonicalizer;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Stopwatch;
import com.google.common.collect.ArrayListMultimap;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceContextType;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.hibernate.CacheMode;
import org.hibernate.search.engine.search.predicate.dsl.BooleanPredicateClausesStep;
import org.hibernate.search.engine.search.predicate.dsl.PredicateFinalStep;
import org.hibernate.search.engine.search.predicate.dsl.SearchPredicateFactory;
import org.hibernate.search.engine.search.query.SearchQuery;
import org.hibernate.search.engine.search.query.SearchScroll;
import org.hibernate.search.engine.search.query.SearchScrollResult;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.common.EntityReference;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.hibernate.search.mapper.pojo.massindexing.impl.PojoMassIndexingLoggingMonitor;
import org.hl7.fhir.common.hapi.validation.support.CachingValidationSupport;
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
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.DecimalType;
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
import org.springframework.util.comparator.Comparators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static ca.uhn.fhir.jpa.entity.TermConceptPropertyBinder.CONCEPT_PROPERTY_PREFIX_NAME;
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

public class TermReadSvcImpl implements ITermReadSvc, IHasScheduledJobs {
	public static final int DEFAULT_FETCH_SIZE = 250;
	public static final int DEFAULT_MASS_INDEXER_OBJECT_LOADING_THREADS = 2;
	// doesn't seem to be much gain by using more threads than this value
	public static final int MAX_MASS_INDEXER_OBJECT_LOADING_THREADS = 6;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(TermReadSvcImpl.class);
	private static final ValueSetExpansionOptions DEFAULT_EXPANSION_OPTIONS = new ValueSetExpansionOptions();
	private static final TermCodeSystemVersionDetails NO_CURRENT_VERSION = new TermCodeSystemVersionDetails(-1L, null);
	private static final String OUR_PIPE_CHARACTER = "|";
	private static final int SECONDS_IN_MINUTE = 60;
	private static final int INDEXED_ROOTS_LOGGING_COUNT = 50_000;
	private static Runnable myInvokeOnNextCallForUnitTest;
	private static boolean ourForceDisableHibernateSearchForUnitTest;
	private final Cache<String, TermCodeSystemVersionDetails> myCodeSystemCurrentVersionCache =
			CacheFactory.build(TimeUnit.MINUTES.toMillis(1));

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

	private boolean myPreExpandingValueSets = false;

	@Autowired
	private ITermCodeSystemVersionDao myCodeSystemVersionDao;

	@Autowired
	private JpaStorageSettings myStorageSettings;

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

	@Autowired(required = false)
	private ITermDeferredStorageSvc myDeferredStorageSvc;

	@Autowired
	private IIdHelperService<JpaPid> myIdHelperService;

	@Autowired
	private ApplicationContext myApplicationContext;

	private volatile IValidationSupport myJpaValidationSupport;
	private volatile IValidationSupport myValidationSupport;
	// We need this bean so we can tell which mode hibernate search is running in.
	@Autowired
	private HibernatePropertiesProvider myHibernatePropertiesProvider;

	@Autowired
	private CachingValidationSupport myCachingValidationSupport;

	@Autowired
	private VersionCanonicalizer myVersionCanonicalizer;

	@Autowired
	private IJpaStorageResourceParser myJpaStorageResourceParser;

	@Autowired
	private InMemoryTerminologyServerValidationSupport myInMemoryTerminologyServerValidationSupport;

	@Autowired
	private ValueSetConceptAccumulatorFactory myValueSetConceptAccumulatorFactory;

	@Override
	public boolean isCodeSystemSupported(ValidationSupportContext theValidationSupportContext, String theSystem) {
		if (isBlank(theSystem)) {
			return false;
		}
		TermCodeSystemVersionDetails cs = getCurrentCodeSystemVersion(theSystem);
		return cs != null;
	}

	@Override
	public boolean isValueSetSupported(ValidationSupportContext theValidationSupportContext, String theValueSetUrl) {
		return fetchValueSet(theValueSetUrl) != null;
	}

	private boolean addCodeIfNotAlreadyAdded(
			@Nullable ValueSetExpansionOptions theExpansionOptions,
			IValueSetConceptAccumulator theValueSetCodeAccumulator,
			Set<String> theAddedCodes,
			TermConcept theConcept,
			boolean theAdd,
			String theValueSetIncludeVersion) {
		String codeSystem = theConcept.getCodeSystemVersion().getCodeSystem().getCodeSystemUri();
		String codeSystemVersion = theConcept.getCodeSystemVersion().getCodeSystemVersionId();
		String code = theConcept.getCode();
		String display = theConcept.getDisplay();
		Long sourceConceptPid = theConcept.getId();
		String directParentPids = "";

		if (theExpansionOptions != null && theExpansionOptions.isIncludeHierarchy()) {
			directParentPids = theConcept.getParents().stream()
					.map(t -> t.getParent().getId().toString())
					.collect(joining(" "));
		}

		Collection<TermConceptDesignation> designations = theConcept.getDesignations();
		if (StringUtils.isNotEmpty(theValueSetIncludeVersion)) {
			return addCodeIfNotAlreadyAdded(
					theValueSetCodeAccumulator,
					theAddedCodes,
					designations,
					theAdd,
					codeSystem + OUR_PIPE_CHARACTER + theValueSetIncludeVersion,
					code,
					display,
					sourceConceptPid,
					directParentPids,
					codeSystemVersion);
		} else {
			return addCodeIfNotAlreadyAdded(
					theValueSetCodeAccumulator,
					theAddedCodes,
					designations,
					theAdd,
					codeSystem,
					code,
					display,
					sourceConceptPid,
					directParentPids,
					codeSystemVersion);
		}
	}

	private boolean addCodeIfNotAlreadyAdded(
			IValueSetConceptAccumulator theValueSetCodeAccumulator,
			Set<String> theAddedCodes,
			boolean theAdd,
			String theCodeSystem,
			String theCodeSystemVersion,
			String theCode,
			String theDisplay,
			Long theSourceConceptPid,
			String theSourceConceptDirectParentPids,
			Collection<TermConceptDesignation> theDesignations) {
		if (StringUtils.isNotEmpty(theCodeSystemVersion)) {
			if (isNoneBlank(theCodeSystem, theCode)) {
				if (theAdd && theAddedCodes.add(theCodeSystem + OUR_PIPE_CHARACTER + theCode)) {
					theValueSetCodeAccumulator.includeConceptWithDesignations(
							theCodeSystem + OUR_PIPE_CHARACTER + theCodeSystemVersion,
							theCode,
							theDisplay,
							theDesignations,
							theSourceConceptPid,
							theSourceConceptDirectParentPids,
							theCodeSystemVersion);
					return true;
				}

				if (!theAdd && theAddedCodes.remove(theCodeSystem + OUR_PIPE_CHARACTER + theCode)) {
					theValueSetCodeAccumulator.excludeConcept(
							theCodeSystem + OUR_PIPE_CHARACTER + theCodeSystemVersion, theCode);
					return true;
				}
			}
		} else {
			if (theAdd && theAddedCodes.add(theCodeSystem + OUR_PIPE_CHARACTER + theCode)) {
				theValueSetCodeAccumulator.includeConceptWithDesignations(
						theCodeSystem,
						theCode,
						theDisplay,
						theDesignations,
						theSourceConceptPid,
						theSourceConceptDirectParentPids,
						theCodeSystemVersion);
				return true;
			}

			if (!theAdd && theAddedCodes.remove(theCodeSystem + OUR_PIPE_CHARACTER + theCode)) {
				theValueSetCodeAccumulator.excludeConcept(theCodeSystem, theCode);
				return true;
			}
		}

		return false;
	}

	private boolean addCodeIfNotAlreadyAdded(
			IValueSetConceptAccumulator theValueSetCodeAccumulator,
			Set<String> theAddedCodes,
			Collection<TermConceptDesignation> theDesignations,
			boolean theAdd,
			String theCodeSystem,
			String theCode,
			String theDisplay,
			Long theSourceConceptPid,
			String theSourceConceptDirectParentPids,
			String theSystemVersion) {
		if (isNoneBlank(theCodeSystem, theCode)) {
			if (theAdd && theAddedCodes.add(theCodeSystem + OUR_PIPE_CHARACTER + theCode)) {
				theValueSetCodeAccumulator.includeConceptWithDesignations(
						theCodeSystem,
						theCode,
						theDisplay,
						theDesignations,
						theSourceConceptPid,
						theSourceConceptDirectParentPids,
						theSystemVersion);
				return true;
			}

			if (!theAdd && theAddedCodes.remove(theCodeSystem + OUR_PIPE_CHARACTER + theCode)) {
				theValueSetCodeAccumulator.excludeConcept(theCodeSystem, theCode);
				return true;
			}
		}

		return false;
	}

	private boolean addToSet(Set<TermConcept> theSetToPopulate, TermConcept theConcept) {
		boolean retVal = theSetToPopulate.add(theConcept);
		if (retVal) {
			if (theSetToPopulate.size() >= myStorageSettings.getMaximumExpansionSize()) {
				String msg = myContext
						.getLocalizer()
						.getMessage(
								TermReadSvcImpl.class,
								"expansionTooLarge",
								myStorageSettings.getMaximumExpansionSize());
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
		Optional<TermValueSet> optionalExistingTermValueSetById =
				myTermValueSetDao.findByResourcePid(theResourceTable.getId());

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
	public List<FhirVersionIndependentConcept> expandValueSetIntoConceptList(
			@Nullable ValueSetExpansionOptions theExpansionOptions, @Nonnull String theValueSetCanonicalUrl) {
		// TODO: DM 2019-09-10 - This is problematic because an incorrect URL that matches ValueSet.id will not be found
		// in the terminology tables but will yield a ValueSet here. Depending on the ValueSet, the expansion may
		// time-out.

		ValueSet expanded = expandValueSet(theExpansionOptions, theValueSetCanonicalUrl);

		ArrayList<FhirVersionIndependentConcept> retVal = new ArrayList<>();
		for (ValueSet.ValueSetExpansionContainsComponent nextContains :
				expanded.getExpansion().getContains()) {
			retVal.add(new FhirVersionIndependentConcept(
					nextContains.getSystem(),
					nextContains.getCode(),
					nextContains.getDisplay(),
					nextContains.getVersion()));
		}
		return retVal;
	}

	@Override
	public ValueSet expandValueSet(
			@Nullable ValueSetExpansionOptions theExpansionOptions, @Nonnull String theValueSetCanonicalUrl) {
		ValueSet valueSet = fetchCanonicalValueSetFromCompleteContext(theValueSetCanonicalUrl);
		if (valueSet == null) {
			throw new ResourceNotFoundException(
					Msg.code(886) + "Unknown ValueSet: " + UrlUtil.escapeUrlParam(theValueSetCanonicalUrl));
		}

		return expandValueSet(theExpansionOptions, valueSet);
	}

	@Override
	public ValueSet expandValueSet(
			@Nullable ValueSetExpansionOptions theExpansionOptions, @Nonnull ValueSet theValueSetToExpand) {
		String filter = null;
		if (theExpansionOptions != null) {
			filter = theExpansionOptions.getFilter();
		}
		return doExpandValueSet(theExpansionOptions, theValueSetToExpand, ExpansionFilter.fromFilterString(filter));
	}

	private ValueSet doExpandValueSet(
			@Nullable ValueSetExpansionOptions theExpansionOptions,
			ValueSet theValueSetToExpand,
			ExpansionFilter theFilter) {
		Set<String> addedCodes = new HashSet<>();
		ValidateUtil.isNotNullOrThrowUnprocessableEntity(theValueSetToExpand, "ValueSet to expand can not be null");

		ValueSetExpansionOptions expansionOptions = provideExpansionOptions(theExpansionOptions);
		int offset = expansionOptions.getOffset();
		int count = expansionOptions.getCount();

		ValueSetExpansionComponentWithConceptAccumulator accumulator =
				new ValueSetExpansionComponentWithConceptAccumulator(
						myContext, count, expansionOptions.isIncludeHierarchy());
		accumulator.setHardExpansionMaximumSize(myStorageSettings.getMaximumExpansionSize());
		accumulator.setSkipCountRemaining(offset);
		accumulator.setIdentifier(UUID.randomUUID().toString());
		accumulator.setTimestamp(new Date());
		accumulator.setOffset(offset);

		if (theExpansionOptions != null && isHibernateSearchEnabled()) {
			accumulator.addParameter().setName("offset").setValue(new IntegerType(offset));
			accumulator.addParameter().setName("count").setValue(new IntegerType(count));
		}

		myTxTemplate.executeWithoutResult(tx -> expandValueSetIntoAccumulator(
				theValueSetToExpand, theExpansionOptions, accumulator, theFilter, true, addedCodes));

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
			valueSet.getMeta()
					.addExtension()
					.setUrl(HapiExtensions.EXT_VALUESET_EXPANSION_MESSAGE)
					.setValue(new StringType(next));
		}

		if (expansionOptions.isIncludeHierarchy()) {
			accumulator.applyHierarchy();
		}

		return valueSet;
	}

	private void expandValueSetIntoAccumulator(
			ValueSet theValueSetToExpand,
			ValueSetExpansionOptions theExpansionOptions,
			IValueSetConceptAccumulator theAccumulator,
			ExpansionFilter theFilter,
			boolean theAdd,
			Set<String> theAddedCodes) {
		Optional<TermValueSet> optionalTermValueSet;
		if (theValueSetToExpand.hasUrl()) {
			if (theValueSetToExpand.hasVersion()) {
				optionalTermValueSet = myTermValueSetDao.findTermValueSetByUrlAndVersion(
						theValueSetToExpand.getUrl(), theValueSetToExpand.getVersion());
			} else {
				optionalTermValueSet = findCurrentTermValueSet(theValueSetToExpand.getUrl());
			}
		} else {
			optionalTermValueSet = Optional.empty();
		}

		/*
		 * ValueSet doesn't exist in pre-expansion database, so perform in-memory expansion
		 */
		if (optionalTermValueSet.isEmpty()) {
			ourLog.debug(
					"ValueSet is not present in terminology tables. Will perform in-memory expansion without parameters. {}",
					getValueSetInfo(theValueSetToExpand));
			String msg = myContext
					.getLocalizer()
					.getMessage(
							TermReadSvcImpl.class,
							"valueSetExpandedUsingInMemoryExpansion",
							getValueSetInfo(theValueSetToExpand));
			theAccumulator.addMessage(msg);
			doExpandValueSet(theExpansionOptions, theValueSetToExpand, theAccumulator, theFilter, theAddedCodes);
			return;
		}

		/*
		 * ValueSet exists in pre-expansion database, but pre-expansion is not yet complete so perform in-memory expansion
		 */
		TermValueSet termValueSet = optionalTermValueSet.get();
		if (termValueSet.getExpansionStatus() != TermValueSetPreExpansionStatusEnum.EXPANDED) {
			String msg = myContext
					.getLocalizer()
					.getMessage(
							TermReadSvcImpl.class,
							"valueSetNotYetExpanded",
							getValueSetInfo(theValueSetToExpand),
							termValueSet.getExpansionStatus().name(),
							termValueSet.getExpansionStatus().getDescription());
			theAccumulator.addMessage(msg);
			doExpandValueSet(theExpansionOptions, theValueSetToExpand, theAccumulator, theFilter, theAddedCodes);
			return;
		}

		/*
		 * ValueSet is pre-expanded in database so let's use that
		 */
		String expansionTimestamp = toHumanReadableExpansionTimestamp(termValueSet);
		String msg = myContext
				.getLocalizer()
				.getMessage(TermReadSvcImpl.class, "valueSetExpandedUsingPreExpansion", expansionTimestamp);
		theAccumulator.addMessage(msg);
		expandConcepts(
				theExpansionOptions,
				theAccumulator,
				termValueSet,
				theFilter,
				theAdd,
				theAddedCodes,
				myHibernatePropertiesProvider.isOracleDialect());
	}

	@Nonnull
	private String toHumanReadableExpansionTimestamp(TermValueSet termValueSet) {
		String expansionTimestamp = "(unknown)";
		if (termValueSet.getExpansionTimestamp() != null) {
			String timeElapsed = StopWatch.formatMillis(System.currentTimeMillis()
					- termValueSet.getExpansionTimestamp().getTime());
			expansionTimestamp = new InstantType(termValueSet.getExpansionTimestamp()).getValueAsString() + " ("
					+ timeElapsed + " ago)";
		}
		return expansionTimestamp;
	}

	private void expandConcepts(
			ValueSetExpansionOptions theExpansionOptions,
			IValueSetConceptAccumulator theAccumulator,
			TermValueSet theTermValueSet,
			ExpansionFilter theFilter,
			boolean theAdd,
			Set<String> theAddedCodes,
			boolean theOracle) {
		// NOTE: if you modifiy the logic here, look to `expandConceptsOracle` and see if your new code applies to its
		// copy pasted sibling
		Integer offset = theAccumulator.getSkipCountRemaining();
		offset = ObjectUtils.defaultIfNull(offset, 0);
		offset = Math.min(offset, theTermValueSet.getTotalConcepts().intValue());

		Integer count = theAccumulator.getCapacityRemaining();
		count = defaultIfNull(count, myStorageSettings.getMaximumExpansionSize());

		int conceptsExpanded = 0;
		int designationsExpanded = 0;
		int toIndex = offset + count;

		Collection<? extends ITermValueSetConceptView> conceptViews;
		boolean wasFilteredResult = false;
		String filterDisplayValue = null;
		if (!theFilter.getFilters().isEmpty()
				&& JpaConstants.VALUESET_FILTER_DISPLAY.equals(
						theFilter.getFilters().get(0).getProperty())
				&& theFilter.getFilters().get(0).getOp() == ValueSet.FilterOperator.EQUAL) {
			filterDisplayValue =
					lowerCase(theFilter.getFilters().get(0).getValue().replace("%", "[%]"));
			String displayValue = "%" + lowerCase(filterDisplayValue) + "%";
			if (theOracle) {
				conceptViews =
						myTermValueSetConceptViewOracleDao.findByTermValueSetId(theTermValueSet.getId(), displayValue);
			} else {
				conceptViews = myTermValueSetConceptViewDao.findByTermValueSetId(theTermValueSet.getId(), displayValue);
			}
			wasFilteredResult = true;
		} else {
			if (theOracle) {
				conceptViews = myTermValueSetConceptViewOracleDao.findByTermValueSetId(
						offset, toIndex, theTermValueSet.getId());
			} else {
				conceptViews =
						myTermValueSetConceptViewDao.findByTermValueSetId(offset, toIndex, theTermValueSet.getId());
			}
			theAccumulator.consumeSkipCount(offset);
			if (theAdd) {
				theAccumulator.incrementOrDecrementTotalConcepts(
						true, theTermValueSet.getTotalConcepts().intValue());
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

			// -- this is quick solution, may need to revisit
			if (!applyFilter(display, filterDisplayValue)) {
				continue;
			}

			Long conceptPid = conceptView.getConceptPid();
			if (!pidToConcept.containsKey(conceptPid)) {
				FhirVersionIndependentConcept concept =
						new FhirVersionIndependentConcept(system, code, display, systemVersion);
				pidToConcept.put(conceptPid, concept);
			}

			// TODO: DM 2019-08-17 - Implement includeDesignations parameter for $expand operation to designations
			// optional.
			if (conceptView.getDesignationPid() != null) {
				TermConceptDesignation designation = new TermConceptDesignation();

				if (isValueSetDisplayLanguageMatch(theExpansionOptions, conceptView.getDesignationLang())) {
					designation.setUseSystem(conceptView.getDesignationUseSystem());
					designation.setUseCode(conceptView.getDesignationUseCode());
					designation.setUseDisplay(conceptView.getDesignationUseDisplay());
					designation.setValue(conceptView.getDesignationVal());
					designation.setLanguage(conceptView.getDesignationLang());
					pidToDesignations.put(conceptPid, designation);
				}

				if (++designationsExpanded % 250 == 0) {
					logDesignationsExpanded(
							"Expansion of designations in progress. ", theTermValueSet, designationsExpanded);
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
				if (theAddedCodes.add(system + OUR_PIPE_CHARACTER + code)) {
					theAccumulator.includeConceptWithDesignations(
							system,
							code,
							display,
							designations,
							sourceConceptPid,
							sourceConceptDirectParentPids,
							systemVersion);
					if (wasFilteredResult) {
						theAccumulator.incrementOrDecrementTotalConcepts(true, 1);
					}
				}
			} else {
				if (theAddedCodes.remove(system + OUR_PIPE_CHARACTER + code)) {
					theAccumulator.excludeConcept(system, code);
					theAccumulator.incrementOrDecrementTotalConcepts(false, 1);
				}
			}
		}

		logDesignationsExpanded("Finished expanding designations. ", theTermValueSet, designationsExpanded);
		logConceptsExpanded("Finished expanding concepts. ", theTermValueSet, conceptsExpanded);
	}

	private void logConceptsExpanded(
			String theLogDescriptionPrefix, TermValueSet theTermValueSet, int theConceptsExpanded) {
		if (theConceptsExpanded > 0) {
			ourLog.debug(
					"{}Have expanded {} concepts in ValueSet[{}]",
					theLogDescriptionPrefix,
					theConceptsExpanded,
					theTermValueSet.getUrl());
		}
	}

	private void logDesignationsExpanded(
			String theLogDescriptionPrefix, TermValueSet theTermValueSet, int theDesignationsExpanded) {
		if (theDesignationsExpanded > 0) {
			ourLog.debug(
					"{}Have expanded {} designations in ValueSet[{}]",
					theLogDescriptionPrefix,
					theDesignationsExpanded,
					theTermValueSet.getUrl());
		}
	}

	public boolean applyFilter(final String theDisplay, final String theFilterDisplay) {

		// -- safety check only, no need to apply filter
		if (theDisplay == null || theFilterDisplay == null) return true;

		// -- sentence case
		if (startsWithIgnoreCase(theDisplay, theFilterDisplay)) return true;

		// -- token case
		return startsWithByWordBoundaries(theDisplay, theFilterDisplay);
	}

	private boolean startsWithByWordBoundaries(String theDisplay, String theFilterDisplay) {
		// return true only e.g. the input is 'Body height', theFilterDisplay is "he", or 'bo'
		StringTokenizer tok = new StringTokenizer(theDisplay);
		List<String> tokens = new ArrayList<>();
		while (tok.hasMoreTokens()) {
			String token = tok.nextToken();
			if (startsWithIgnoreCase(token, theFilterDisplay)) return true;
			tokens.add(token);
		}

		// Allow to search by the end of the phrase.  E.g.  "working proficiency" will match "Limited working
		// proficiency"
		for (int start = 0; start <= tokens.size() - 1; ++start) {
			for (int end = start + 1; end <= tokens.size(); ++end) {
				String sublist = String.join(" ", tokens.subList(start, end));
				if (startsWithIgnoreCase(sublist, theFilterDisplay)) return true;
			}
		}
		return false;
	}

	@Override
	public void expandValueSet(
			ValueSetExpansionOptions theExpansionOptions,
			ValueSet theValueSetToExpand,
			IValueSetConceptAccumulator theValueSetCodeAccumulator) {
		Set<String> addedCodes = new HashSet<>();
		doExpandValueSet(
				theExpansionOptions,
				theValueSetToExpand,
				theValueSetCodeAccumulator,
				ExpansionFilter.NO_FILTER,
				addedCodes);
	}

	/**
	 * Note: Not transactional because specific calls within this method
	 * get executed in a transaction
	 */
	private void doExpandValueSet(
			ValueSetExpansionOptions theExpansionOptions,
			ValueSet theValueSetToExpand,
			IValueSetConceptAccumulator theValueSetCodeAccumulator,
			@Nonnull ExpansionFilter theExpansionFilter,
			Set<String> theAddedCodes) {

		StopWatch sw = new StopWatch();
		String valueSetInfo = getValueSetInfo(theValueSetToExpand);
		ourLog.debug("Working with {}", valueSetInfo);

		// Offset can't be combined with excludes
		Integer skipCountRemaining = theValueSetCodeAccumulator.getSkipCountRemaining();
		if (skipCountRemaining != null && skipCountRemaining > 0) {
			if (!theValueSetToExpand.getCompose().getExclude().isEmpty()) {
				String msg = myContext
						.getLocalizer()
						.getMessage(TermReadSvcImpl.class, "valueSetNotYetExpanded_OffsetNotAllowed", valueSetInfo);
				throw new InvalidRequestException(Msg.code(887) + msg);
			}
		}

		// Handle includes
		ourLog.debug("Handling includes");
		for (ValueSet.ConceptSetComponent include :
				theValueSetToExpand.getCompose().getInclude()) {
			myTxTemplate.executeWithoutResult(tx -> expandValueSetHandleIncludeOrExclude(
					theExpansionOptions, theValueSetCodeAccumulator, theAddedCodes, include, true, theExpansionFilter));
		}

		// Handle excludes
		ourLog.debug("Handling excludes");
		for (ValueSet.ConceptSetComponent exclude :
				theValueSetToExpand.getCompose().getExclude()) {
			myTxTemplate.executeWithoutResult(tx -> expandValueSetHandleIncludeOrExclude(
					theExpansionOptions,
					theValueSetCodeAccumulator,
					theAddedCodes,
					exclude,
					false,
					ExpansionFilter.NO_FILTER));
		}

		if (theValueSetCodeAccumulator instanceof ValueSetConceptAccumulator) {
			myTxTemplate.execute(
					t -> ((ValueSetConceptAccumulator) theValueSetCodeAccumulator).removeGapsFromConceptOrder());
		}

		ourLog.debug("Done working with {} in {}ms", valueSetInfo, sw.getMillis());
	}

	private String getValueSetInfo(ValueSet theValueSet) {
		StringBuilder sb = new StringBuilder();
		boolean isIdentified = false;
		if (theValueSet.hasUrl()) {
			isIdentified = true;
			sb.append("ValueSet.url[").append(theValueSet.getUrl()).append("]");
		} else if (theValueSet.hasId()) {
			isIdentified = true;
			sb.append("ValueSet.id[").append(theValueSet.getId()).append("]");
		}

		if (!isIdentified) {
			sb.append("Unidentified ValueSet");
		}

		return sb.toString();
	}

	/**
	 * Returns true if there are potentially more results to process.
	 */
	private void expandValueSetHandleIncludeOrExclude(
			@Nullable ValueSetExpansionOptions theExpansionOptions,
			IValueSetConceptAccumulator theValueSetCodeAccumulator,
			Set<String> theAddedCodes,
			ValueSet.ConceptSetComponent theIncludeOrExclude,
			boolean theAdd,
			@Nonnull ExpansionFilter theExpansionFilter) {

		String system = theIncludeOrExclude.getSystem();
		boolean hasSystem = isNotBlank(system);
		boolean hasValueSet = !theIncludeOrExclude.getValueSet().isEmpty();

		if (hasSystem) {

			if (theExpansionFilter.hasCode()
					&& theExpansionFilter.getSystem() != null
					&& !system.equals(theExpansionFilter.getSystem())) {
				return;
			}

			ourLog.debug("Starting {} expansion around CodeSystem: {}", (theAdd ? "inclusion" : "exclusion"), system);

			Optional<TermCodeSystemVersion> termCodeSystemVersion =
					optionalFindTermCodeSystemVersion(theIncludeOrExclude);
			if (termCodeSystemVersion.isPresent()) {

				expandValueSetHandleIncludeOrExcludeUsingDatabase(
						theExpansionOptions,
						theValueSetCodeAccumulator,
						theAddedCodes,
						theIncludeOrExclude,
						theAdd,
						theExpansionFilter,
						system,
						termCodeSystemVersion.get());

			} else {

				if (!theIncludeOrExclude.getConcept().isEmpty() && theExpansionFilter.hasCode()) {
					if (defaultString(theIncludeOrExclude.getSystem()).equals(theExpansionFilter.getSystem())) {
						if (theIncludeOrExclude.getConcept().stream()
								.noneMatch(t -> t.getCode().equals(theExpansionFilter.getCode()))) {
							return;
						}
					}
				}

				Consumer<FhirVersionIndependentConcept> consumer = c -> addOrRemoveCode(
						theValueSetCodeAccumulator,
						theAddedCodes,
						theAdd,
						system,
						c.getCode(),
						c.getDisplay(),
						c.getSystemVersion());

				try {
					ConversionContext40_50.INSTANCE.init(
							new VersionConvertor_40_50(new BaseAdvisor_40_50()), "ValueSet");
					org.hl7.fhir.r5.model.ValueSet.ConceptSetComponent includeOrExclude =
							ValueSet40_50.convertConceptSetComponent(theIncludeOrExclude);
					myInMemoryTerminologyServerValidationSupport.expandValueSetIncludeOrExclude(
							new ValidationSupportContext(provideValidationSupport()), consumer, includeOrExclude);
				} catch (InMemoryTerminologyServerValidationSupport.ExpansionCouldNotBeCompletedInternallyException e) {
					if (theExpansionOptions != null
							&& !theExpansionOptions.isFailOnMissingCodeSystem()
							// Code system is unknown, therefore NOT_FOUND
							&& e.getCodeValidationIssue().getCoding() == CodeValidationIssueCoding.NOT_FOUND) {
						return;
					}
					throw new InternalErrorException(Msg.code(888) + e);
				} finally {
					ConversionContext40_50.INSTANCE.close("ValueSet");
				}
			}

		} else if (hasValueSet) {

			for (CanonicalType nextValueSet : theIncludeOrExclude.getValueSet()) {
				String valueSetUrl = nextValueSet.getValueAsString();
				ourLog.debug(
						"Starting {} expansion around ValueSet: {}", (theAdd ? "inclusion" : "exclusion"), valueSetUrl);

				ExpansionFilter subExpansionFilter = new ExpansionFilter(
						theExpansionFilter,
						theIncludeOrExclude.getFilter(),
						theValueSetCodeAccumulator.getCapacityRemaining());

				// TODO: DM 2019-09-10 - This is problematic because an incorrect URL that matches ValueSet.id will not
				// be found in the terminology tables but will yield a ValueSet here. Depending on the ValueSet, the
				// expansion may time-out.

				ValueSet valueSet = fetchCanonicalValueSetFromCompleteContext(valueSetUrl);
				if (valueSet == null) {
					throw new ResourceNotFoundException(
							Msg.code(889) + "Unknown ValueSet: " + UrlUtil.escapeUrlParam(valueSetUrl));
				}

				expandValueSetIntoAccumulator(
						valueSet,
						theExpansionOptions,
						theValueSetCodeAccumulator,
						subExpansionFilter,
						theAdd,
						theAddedCodes);
			}

		} else {
			throw new InvalidRequestException(Msg.code(890) + "ValueSet contains " + (theAdd ? "include" : "exclude")
					+ " criteria with no system defined");
		}
	}

	private Optional<TermCodeSystemVersion> optionalFindTermCodeSystemVersion(
			ValueSet.ConceptSetComponent theIncludeOrExclude) {
		if (isEmpty(theIncludeOrExclude.getVersion())) {
			return Optional.ofNullable(myCodeSystemDao.findByCodeSystemUri(theIncludeOrExclude.getSystem()))
					.map(TermCodeSystem::getCurrentVersion);
		} else {
			return Optional.ofNullable(myCodeSystemVersionDao.findByCodeSystemUriAndVersion(
					theIncludeOrExclude.getSystem(), theIncludeOrExclude.getVersion()));
		}
	}

	private boolean isHibernateSearchEnabled() {
		return myFulltextSearchSvc != null && !ourForceDisableHibernateSearchForUnitTest;
	}

	private void expandValueSetHandleIncludeOrExcludeUsingDatabase(
			ValueSetExpansionOptions theExpansionOptions,
			IValueSetConceptAccumulator theValueSetCodeAccumulator,
			Set<String> theAddedCodes,
			ValueSet.ConceptSetComponent theIncludeOrExclude,
			boolean theAdd,
			@Nonnull ExpansionFilter theExpansionFilter,
			String theSystem,
			TermCodeSystemVersion theTermCodeSystemVersion) {

		StopWatch fullOperationSw = new StopWatch();
		String includeOrExcludeVersion = theIncludeOrExclude.getVersion();

		/*
		 * If FullText searching is not enabled, we can handle only basic expansions
		 * since we're going to do it without the database.
		 */
		if (!isHibernateSearchEnabled()) {
			expandWithoutHibernateSearch(
					theValueSetCodeAccumulator,
					theTermCodeSystemVersion,
					theAddedCodes,
					theIncludeOrExclude,
					theSystem,
					theAdd);
			return;
		}

		/*
		 * Ok, let's use hibernate search to build the expansion
		 */

		int count = 0;

		Optional<Integer> chunkSizeOpt = getScrollChunkSize(theAdd, theValueSetCodeAccumulator);
		if (chunkSizeOpt.isEmpty()) {
			return;
		}
		int chunkSize = chunkSizeOpt.get();

		/*
		 * Turn the filter into one or more Hibernate Search queries. Ideally we want it
		 * to be handled by a single query, but Lucene/ES don't like it when we exceed
		 * 1024 different terms in a single query. So if we have that many terms (which
		 * can happen if a ValueSet has a lot of explicitly enumerated codes that it's
		 * including) we split this into multiple searches. The method below builds these
		 * searches lazily, returning a Supplier that creates and executes the search
		 * when it's actually time to.
		 */
		SearchProperties searchProps = buildSearchScrolls(
				theTermCodeSystemVersion,
				theExpansionFilter,
				theSystem,
				theIncludeOrExclude,
				chunkSize,
				includeOrExcludeVersion);
		int accumulatedBatchesSoFar = 0;
		for (var next : searchProps.getSearchScroll()) {
			try (SearchScroll<EntityReference> scroll = next.get()) {
				ourLog.debug(
						"Beginning batch expansion for {} with max results per batch: {}",
						(theAdd ? "inclusion" : "exclusion"),
						chunkSize);
				for (SearchScrollResult<EntityReference> chunk = scroll.next();
						chunk.hasHits();
						chunk = scroll.next()) {
					int countForBatch = 0;

					List<Long> pids =
							chunk.hits().stream().map(t -> (Long) t.id()).collect(Collectors.toList());

					List<TermConcept> termConcepts = myTermConceptDao.fetchConceptsAndDesignationsByPid(pids);

					// If the include section had multiple codes, return the codes in the same order
					termConcepts = sortTermConcepts(searchProps, termConcepts);

					//	 int firstResult = theQueryIndex * maxResultsPerBatch;
					// TODO GGG HS we lose the ability to check the
					// index of the first result, so just best-guessing it here.
					int delta = 0;
					for (TermConcept concept : termConcepts) {
						count++;
						countForBatch++;
						if (theAdd && searchProps.hasIncludeOrExcludeCodes()) {
							ValueSet.ConceptReferenceComponent theIncludeConcept =
									getMatchedConceptIncludedInValueSet(theIncludeOrExclude, concept);
							if (theIncludeConcept != null && isNotBlank(theIncludeConcept.getDisplay())) {
								concept.setDisplay(theIncludeConcept.getDisplay());
							}
						}
						boolean added = addCodeIfNotAlreadyAdded(
								theExpansionOptions,
								theValueSetCodeAccumulator,
								theAddedCodes,
								concept,
								theAdd,
								includeOrExcludeVersion);
						if (added) {
							delta++;
						}
					}

					ourLog.debug(
							"Batch expansion scroll for {} with offset {} produced {} results in {}ms",
							(theAdd ? "inclusion" : "exclusion"),
							accumulatedBatchesSoFar,
							chunk.hits().size(),
							chunk.took().toMillis());

					theValueSetCodeAccumulator.incrementOrDecrementTotalConcepts(theAdd, delta);
					accumulatedBatchesSoFar += countForBatch;

					// keep session bounded
					myEntityManager.flush();
					myEntityManager.clear();
				}

				ourLog.debug(
						"Expansion for {} produced {} results in {}ms",
						(theAdd ? "inclusion" : "exclusion"),
						count,
						fullOperationSw.getMillis());
			}
		}
	}

	private List<TermConcept> sortTermConcepts(SearchProperties searchProps, List<TermConcept> termConcepts) {
		List<String> codes = searchProps.getIncludeOrExcludeCodes();
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
		return termConcepts;
	}

	private Optional<Integer> getScrollChunkSize(
			boolean theAdd, IValueSetConceptAccumulator theValueSetCodeAccumulator) {
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
		}
		return maxResultsPerBatch > 0 ? Optional.of(maxResultsPerBatch) : Optional.empty();
	}

	private SearchProperties buildSearchScrolls(
			TermCodeSystemVersion theTermCodeSystemVersion,
			ExpansionFilter theExpansionFilter,
			String theSystem,
			ValueSet.ConceptSetComponent theIncludeOrExclude,
			Integer theScrollChunkSize,
			String theIncludeOrExcludeVersion) {
		SearchSession searchSession = Search.session(myEntityManager);
		// Manually building a predicate since we need to throw it around.
		SearchPredicateFactory predicate =
				searchSession.scope(TermConcept.class).predicate();

		List<String> allCodes = theIncludeOrExclude.getConcept().stream()
				.filter(Objects::nonNull)
				.map(ValueSet.ConceptReferenceComponent::getCode)
				.filter(StringUtils::isNotBlank)
				.collect(Collectors.toList());
		SearchProperties returnProps = new SearchProperties();
		returnProps.setIncludeOrExcludeCodes(allCodes);

		/*
		 * Lucene/ES can't typically handle more than 1024 clauses per search, so if
		 * we have more than that number (e.g. because of a ValueSet that explicitly
		 * includes thousands of codes), we break this up into multiple searches.
		 */
		List<List<String>> partitionedCodes = ListUtils.partition(allCodes, IndexSearcher.getMaxClauseCount() - 10);
		if (partitionedCodes.isEmpty()) {
			partitionedCodes = List.of(List.of());
		}

		for (List<String> nextCodePartition : partitionedCodes) {
			Supplier<SearchScroll<EntityReference>> nextScroll = () -> {
				// Build the top-level expansion on filters.
				PredicateFinalStep step = predicate.bool(b -> {
					b.must(predicate
							.match()
							.field("myCodeSystemVersionPid")
							.matching(theTermCodeSystemVersion.getPid()));

					if (theExpansionFilter.hasCode()) {
						b.must(predicate.match().field("myCode").matching(theExpansionFilter.getCode()));
					}

					String codeSystemUrlAndVersion =
							buildCodeSystemUrlAndVersion(theSystem, theIncludeOrExcludeVersion);
					for (ValueSet.ConceptSetFilterComponent nextFilter : theIncludeOrExclude.getFilter()) {
						handleFilter(codeSystemUrlAndVersion, predicate, b, nextFilter);
					}
					for (ValueSet.ConceptSetFilterComponent nextFilter : theExpansionFilter.getFilters()) {
						handleFilter(codeSystemUrlAndVersion, predicate, b, nextFilter);
					}
				});

				// Add a selector on any explicitly enumerated codes in the VS component
				final PredicateFinalStep finishedQuery;
				if (nextCodePartition.isEmpty()) {
					finishedQuery = step;
				} else {
					PredicateFinalStep expansionStep = buildExpansionPredicate(nextCodePartition, predicate);
					finishedQuery = predicate.bool().must(step).must(expansionStep);
				}

				SearchQuery<EntityReference> termConceptsQuery = searchSession
						.search(TermConcept.class)
						.selectEntityReference()
						.where(f -> finishedQuery)
						.toQuery();

				return termConceptsQuery.scroll(theScrollChunkSize);
			};

			returnProps.addSearchScroll(nextScroll);
		}

		return returnProps;
	}

	private ValueSet.ConceptReferenceComponent getMatchedConceptIncludedInValueSet(
			ValueSet.ConceptSetComponent theIncludeOrExclude, TermConcept concept) {
		return theIncludeOrExclude.getConcept().stream()
				.filter(includedConcept -> includedConcept.getCode().equalsIgnoreCase(concept.getCode()))
				.findFirst()
				.orElse(null);
	}

	/**
	 * Helper method which builds a predicate for the expansion
	 */
	private PredicateFinalStep buildExpansionPredicate(List<String> theCodes, SearchPredicateFactory thePredicate) {
		assert !theCodes.isEmpty();
		return thePredicate.simpleQueryString().field("myCode").matching(String.join(" | ", theCodes));
	}

	private String buildCodeSystemUrlAndVersion(String theSystem, String theIncludeOrExcludeVersion) {
		String codeSystemUrlAndVersion;
		if (theIncludeOrExcludeVersion != null) {
			codeSystemUrlAndVersion = theSystem + OUR_PIPE_CHARACTER + theIncludeOrExcludeVersion;
		} else {
			codeSystemUrlAndVersion = theSystem;
		}
		return codeSystemUrlAndVersion;
	}

	private @Nonnull ValueSetExpansionOptions provideExpansionOptions(
			@Nullable ValueSetExpansionOptions theExpansionOptions) {
		return Objects.requireNonNullElse(theExpansionOptions, DEFAULT_EXPANSION_OPTIONS);
	}

	private void addOrRemoveCode(
			IValueSetConceptAccumulator theValueSetCodeAccumulator,
			Set<String> theAddedCodes,
			boolean theAdd,
			String theSystem,
			String theCode,
			String theDisplay,
			String theSystemVersion) {
		if (theAdd && theAddedCodes.add(theSystem + OUR_PIPE_CHARACTER + theCode)) {
			theValueSetCodeAccumulator.includeConcept(theSystem, theCode, theDisplay, null, null, theSystemVersion);
		}
		if (!theAdd && theAddedCodes.remove(theSystem + OUR_PIPE_CHARACTER + theCode)) {
			theValueSetCodeAccumulator.excludeConcept(theSystem, theCode);
		}
	}

	private void handleFilter(
			String theCodeSystemIdentifier,
			SearchPredicateFactory theF,
			BooleanPredicateClausesStep<?> theB,
			ValueSet.ConceptSetFilterComponent theFilter) {
		if (isBlank(theFilter.getValue()) && theFilter.getOp() == null && isBlank(theFilter.getProperty())) {
			return;
		}

		// if filter type is EXISTS, there's no reason to worry about the value (we won't set it anyways)
		if ((isBlank(theFilter.getValue()) && theFilter.getOp() != ValueSet.FilterOperator.EXISTS)
				|| theFilter.getOp() == null
				|| isBlank(theFilter.getProperty())) {
			throw new InvalidRequestException(
					Msg.code(891) + "Invalid filter, must have fields populated: property op value");
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

	private void handleFilterPropertyDefault(
			SearchPredicateFactory theF,
			BooleanPredicateClausesStep<?> theB,
			ValueSet.ConceptSetFilterComponent theFilter) {

		String value = theFilter.getValue();
		if (theFilter.getOp() == ValueSet.FilterOperator.EXISTS) {
			// EXISTS has no value and is thus handled differently
			Term term = new Term(CONCEPT_PROPERTY_PREFIX_NAME + theFilter.getProperty());
			theB.must(theF.exists().field(term.field()));
		} else {
			Term term = new Term(CONCEPT_PROPERTY_PREFIX_NAME + theFilter.getProperty(), value);
			switch (theFilter.getOp()) {
				case EQUAL:
					theB.must(theF.match().field(term.field()).matching(term.text()));
					break;
				case IN:
				case NOTIN:
					boolean isNotFilter = theFilter.getOp() == ValueSet.FilterOperator.NOTIN;
					// IN and NOTIN expect comma separated lists
					String[] values = term.text().split(",");
					Set<String> valueSet = new HashSet<>(Arrays.asList(values));
					if (isNotFilter) {
						theB.filter(theF.not(theF.terms().field(term.field()).matchingAny(valueSet)));
					} else {
						theB.filter(theF.terms().field(term.field()).matchingAny(valueSet));
					}
					break;
				case ISA:
				case ISNOTA:
				case DESCENDENTOF:
				case GENERALIZES:
				default:
					/*
					 * We do not need to handle REGEX, because that's handled in parent
					 * We also don't handle EXISTS because that's a separate area (with different term).
					 * We add a match-none filter because otherwise it matches everything (not desired).
					 */
					ourLog.error(
							"Unsupported property filter {}. This may affect expansion, but will not cause errors.",
							theFilter.getOp().getDisplay());
					theB.must(theF.matchNone());
					break;
			}
		}
	}

	private void handleFilterRegex(
			SearchPredicateFactory theF,
			BooleanPredicateClausesStep<?> theB,
			ValueSet.ConceptSetFilterComponent theFilter) {
		/*
		 * We treat the regex filter as a match on the regex
		 * anywhere in the property string. The spec does not
		 * say whether this is the right behaviour or not, but
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

		theB.must(theF.regexp()
				.field(CONCEPT_PROPERTY_PREFIX_NAME + theFilter.getProperty())
				.matching(value));
	}

	private void handleFilterLoincCopyright(
			SearchPredicateFactory theF,
			BooleanPredicateClausesStep<?> theB,
			ValueSet.ConceptSetFilterComponent theFilter) {

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

	private void addFilterLoincCopyrightLoinc(SearchPredicateFactory theF, BooleanPredicateClausesStep<?> theB) {
		theB.mustNot(theF.exists().field(CONCEPT_PROPERTY_PREFIX_NAME + "EXTERNAL_COPYRIGHT_NOTICE"));
	}

	private void addFilterLoincCopyright3rdParty(SearchPredicateFactory theF, BooleanPredicateClausesStep<?> theB) {
		theB.must(theF.exists().field(CONCEPT_PROPERTY_PREFIX_NAME + "EXTERNAL_COPYRIGHT_NOTICE"));
	}

	@SuppressWarnings("EnumSwitchStatementWhichMissesCases")
	private void handleFilterLoincAncestor(
			String theSystem,
			SearchPredicateFactory f,
			BooleanPredicateClausesStep<?> b,
			ValueSet.ConceptSetFilterComponent theFilter) {
		switch (theFilter.getOp()) {
			case EQUAL:
				addLoincFilterAncestorEqual(theSystem, f, b, theFilter);
				break;
			case IN:
				addLoincFilterAncestorIn(theSystem, f, b, theFilter);
				break;
			default:
				throw new InvalidRequestException(Msg.code(892) + "Don't know how to handle op=" + theFilter.getOp()
						+ " on property " + theFilter.getProperty());
		}
	}

	private void addLoincFilterAncestorEqual(
			String theSystem,
			SearchPredicateFactory f,
			BooleanPredicateClausesStep<?> b,
			ValueSet.ConceptSetFilterComponent theFilter) {
		addLoincFilterAncestorEqual(theSystem, f, b, theFilter.getProperty(), theFilter.getValue());
	}

	private void addLoincFilterAncestorEqual(
			String theSystem,
			SearchPredicateFactory f,
			BooleanPredicateClausesStep<?> b,
			String theProperty,
			String theValue) {
		List<Term> terms = getAncestorTerms(theSystem, theProperty, theValue);
		b.must(f.bool(innerB -> terms.forEach(
				term -> innerB.should(f.match().field(term.field()).matching(term.text())))));
	}

	private void addLoincFilterAncestorIn(
			String theSystem,
			SearchPredicateFactory f,
			BooleanPredicateClausesStep<?> b,
			ValueSet.ConceptSetFilterComponent theFilter) {
		String[] values = theFilter.getValue().split(",");
		List<Term> terms = new ArrayList<>();
		for (String value : values) {
			terms.addAll(getAncestorTerms(theSystem, theFilter.getProperty(), value));
		}
		b.must(f.bool(innerB -> terms.forEach(
				term -> innerB.should(f.match().field(term.field()).matching(term.text())))));
	}

	@SuppressWarnings("EnumSwitchStatementWhichMissesCases")
	private void handleFilterLoincParentChild(
			SearchPredicateFactory f, BooleanPredicateClausesStep<?> b, ValueSet.ConceptSetFilterComponent theFilter) {
		switch (theFilter.getOp()) {
			case EQUAL:
				addLoincFilterParentChildEqual(f, b, theFilter.getProperty(), theFilter.getValue());
				break;
			case IN:
				addLoincFilterParentChildIn(f, b, theFilter);
				break;
			default:
				throw new InvalidRequestException(Msg.code(893) + "Don't know how to handle op=" + theFilter.getOp()
						+ " on property " + theFilter.getProperty());
		}
	}

	private void addLoincFilterParentChildIn(
			SearchPredicateFactory f, BooleanPredicateClausesStep<?> b, ValueSet.ConceptSetFilterComponent theFilter) {
		String[] values = theFilter.getValue().split(",");
		List<Term> terms = new ArrayList<>();
		for (String value : values) {
			logFilteringValueOnProperty(value, theFilter.getProperty());
			terms.add(getPropertyTerm(theFilter.getProperty(), value));
		}

		b.must(f.bool(innerB -> terms.forEach(
				term -> innerB.should(f.match().field(term.field()).matching(term.text())))));
	}

	private void addLoincFilterParentChildEqual(
			SearchPredicateFactory f, BooleanPredicateClausesStep<?> b, String theProperty, String theValue) {
		logFilteringValueOnProperty(theValue, theProperty);
		b.must(f.match().field(CONCEPT_PROPERTY_PREFIX_NAME + theProperty).matching(theValue));
	}

	private void handleFilterConceptAndCode(
			String theSystem,
			SearchPredicateFactory f,
			BooleanPredicateClausesStep<?> b,
			ValueSet.ConceptSetFilterComponent theFilter) {
		TermConcept code = findCodeForFilterCriteriaCodeOrConcept(theSystem, theFilter);

		if (theFilter.getOp() == ValueSet.FilterOperator.ISA) {
			ourLog.debug(
					" * Filtering on specific code and codes with a parent of {}/{}/{}",
					code.getId(),
					code.getCode(),
					code.getDisplay());

			b.must(f.bool()
					.should(f.match().field("myParentPids").matching("" + code.getId()))
					.should(f.match().field("myId").matching(code.getId())));
		} else if (theFilter.getOp() == ValueSet.FilterOperator.DESCENDENTOF) {
			ourLog.debug(
					" * Filtering on codes with a parent of {}/{}/{}", code.getId(), code.getCode(), code.getDisplay());

			b.must(f.match().field("myParentPids").matching("" + code.getId()));
		} else {
			throwInvalidFilter(theFilter, "");
		}
	}

	@Nonnull
	private TermConcept findCodeForFilterCriteriaCodeOrConcept(
			String theSystem, ValueSet.ConceptSetFilterComponent theFilter) {
		return findCode(theSystem, theFilter.getValue())
				.orElseThrow(() ->
						new InvalidRequestException(Msg.code(2071) + "Invalid filter criteria - code does not exist: {"
								+ Constants.codeSystemWithDefaultDescription(theSystem) + "}" + theFilter.getValue()));
	}

	private void throwInvalidFilter(ValueSet.ConceptSetFilterComponent theFilter, String theErrorSuffix) {
		throw new InvalidRequestException(Msg.code(894) + "Don't know how to handle op=" + theFilter.getOp()
				+ " on property " + theFilter.getProperty() + theErrorSuffix);
	}

	private void isCodeSystemLoincOrThrowInvalidRequestException(String theSystemIdentifier, String theProperty) {
		String systemUrl = getUrlFromIdentifier(theSystemIdentifier);
		if (!isCodeSystemLoinc(systemUrl)) {
			throw new InvalidRequestException(Msg.code(895) + "Invalid filter, property " + theProperty
					+ " is LOINC-specific and cannot be used with system: " + systemUrl);
		}
	}

	private boolean isCodeSystemLoinc(String theSystem) {
		return LOINC_URI.equals(theSystem);
	}

	private void handleFilterDisplay(
			SearchPredicateFactory f, BooleanPredicateClausesStep<?> b, ValueSet.ConceptSetFilterComponent theFilter) {
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

	private void addDisplayFilterExact(
			SearchPredicateFactory f,
			BooleanPredicateClausesStep<?> bool,
			ValueSet.ConceptSetFilterComponent nextFilter) {
		bool.must(f.phrase().field("myDisplay").matching(nextFilter.getValue()));
	}

	private void addDisplayFilterInexact(
			SearchPredicateFactory f,
			BooleanPredicateClausesStep<?> bool,
			ValueSet.ConceptSetFilterComponent nextFilter) {
		bool.must(f.phrase()
				.field("myDisplay")
				.boost(4.0f)
				.field("myDisplayWordEdgeNGram")
				.boost(1.0f)
				.field("myDisplayEdgeNGram")
				.boost(1.0f)
				.matching(nextFilter.getValue().toLowerCase())
				.slop(2));
	}

	private Term getPropertyTerm(String theProperty, String theValue) {
		return new Term(CONCEPT_PROPERTY_PREFIX_NAME + theProperty, theValue);
	}

	private List<Term> getAncestorTerms(String theSystem, String theProperty, String theValue) {
		List<Term> retVal = new ArrayList<>();

		TermConcept code = findCode(theSystem, theValue)
				.orElseThrow(() -> new InvalidRequestException("Invalid filter criteria - code does not exist: {"
						+ Constants.codeSystemWithDefaultDescription(theSystem) + "}" + theValue));

		retVal.add(new Term("myParentPids", "" + code.getId()));
		logFilteringValueOnProperty(theValue, theProperty);

		return retVal;
	}

	@SuppressWarnings("EnumSwitchStatementWhichMissesCases")
	private void handleFilterLoincDescendant(
			String theSystem,
			SearchPredicateFactory f,
			BooleanPredicateClausesStep<?> b,
			ValueSet.ConceptSetFilterComponent theFilter) {
		switch (theFilter.getOp()) {
			case EQUAL:
				addLoincFilterDescendantEqual(theSystem, f, b, theFilter);
				break;
			case IN:
				addLoincFilterDescendantIn(theSystem, f, b, theFilter);
				break;
			default:
				throw new InvalidRequestException(Msg.code(896) + "Don't know how to handle op=" + theFilter.getOp()
						+ " on property " + theFilter.getProperty());
		}
	}

	private void addLoincFilterDescendantEqual(
			String theSystem,
			SearchPredicateFactory f,
			BooleanPredicateClausesStep<?> b,
			ValueSet.ConceptSetFilterComponent theFilter) {

		List<Long> parentPids = getCodeParentPids(theSystem, theFilter.getProperty(), theFilter.getValue());
		if (parentPids.isEmpty()) {
			// Can't return empty must, because it wil match according to other predicates.
			// Some day there will be a 'matchNone' predicate
			// (https://discourse.hibernate.org/t/fail-fast-predicate/6062)
			b.mustNot(f.matchAll());
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
	private void addLoincFilterDescendantIn(
			String theSystem,
			SearchPredicateFactory f,
			BooleanPredicateClausesStep<?> b,
			ValueSet.ConceptSetFilterComponent theFilter) {

		String[] values = theFilter.getValue().split(",");
		if (values.length == 0) {
			throw new InvalidRequestException(Msg.code(2062) + "Invalid filter criteria - no codes specified");
		}

		List<Long> descendantCodePidList = getMultipleCodeParentPids(theSystem, theFilter.getProperty(), values);

		b.must(f.bool(innerB -> descendantCodePidList.forEach(
				pId -> innerB.should(f.match().field("myId").matching(pId)))));
	}

	/**
	 * Returns the list of parentId(s) of the TermConcept representing theValue as a code
	 */
	private List<Long> getCodeParentPids(String theSystem, String theProperty, String theValue) {
		TermConcept code = findCode(theSystem, theValue)
				.orElseThrow(() -> new InvalidRequestException("Invalid filter criteria - code does not exist: {"
						+ Constants.codeSystemWithDefaultDescription(theSystem) + "}" + theValue));

		String[] parentPids = code.getParentPidsAsString().split(" ");
		List<Long> retVal = Arrays.stream(parentPids)
				.filter(pid -> !StringUtils.equals(pid, "NONE"))
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
			throw new InvalidRequestException(Msg.code(2064) + "Invalid filter criteria - {"
					+ Constants.codeSystemWithDefaultDescription(theSystem) + "}: " + exMsg);
		}

		List<Long> retVal = termConcepts.stream()
				.flatMap(tc -> Arrays.stream(tc.getParentPidsAsString().split(" ")))
				.filter(pid -> !StringUtils.equals(pid, "NONE"))
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
			return "Invalid filter criteria - More TermConcepts were found than indicated codes. Queried codes: ["
					+ join(
							",",
							theValues + "]; Obtained TermConcept IDs, codes: ["
									+ theTermConcepts.stream()
											.map(tc -> tc.getId() + ", " + tc.getCode())
											.collect(joining("; "))
									+ "]");
		}

		// case: less TermConcept(s) retrieved than codes queried
		Set<String> matchedCodes =
				theTermConcepts.stream().map(TermConcept::getCode).collect(toSet());
		List<String> notMatchedValues =
				theValues.stream().filter(v -> !matchedCodes.contains(v)).collect(toList());

		return "Invalid filter criteria - No TermConcept(s) were found for the requested codes: ["
				+ join(",", notMatchedValues + "]");
	}

	private void logFilteringValueOnProperty(String theValue, String theProperty) {
		ourLog.debug(" * Filtering with value={} on property {}", theValue, theProperty);
	}

	private void logFilteringValueOnProperties(List<String> theValues, String theProperty) {
		ourLog.debug(" * Filtering with values={} on property {}", String.join(", ", theValues), theProperty);
	}

	private void throwInvalidRequestForOpOnProperty(ValueSet.FilterOperator theOp, String theProperty) {
		throw new InvalidRequestException(
				Msg.code(897) + "Don't know how to handle op=" + theOp + " on property " + theProperty);
	}

	private void throwInvalidRequestForValueOnProperty(String theValue, String theProperty) {
		throw new InvalidRequestException(
				Msg.code(898) + "Don't know how to handle value=" + theValue + " on property " + theProperty);
	}

	private void expandWithoutHibernateSearch(
			IValueSetConceptAccumulator theValueSetCodeAccumulator,
			TermCodeSystemVersion theVersion,
			Set<String> theAddedCodes,
			ValueSet.ConceptSetComponent theInclude,
			String theSystem,
			boolean theAdd) {
		ourLog.trace("Hibernate search is not enabled");

		if (theValueSetCodeAccumulator instanceof ValueSetExpansionComponentWithConceptAccumulator) {
			Validate.isTrue(
					((ValueSetExpansionComponentWithConceptAccumulator) theValueSetCodeAccumulator)
							.getParameter()
							.isEmpty(),
					"Can not expand ValueSet with parameters - Hibernate Search is not enabled on this server.");
		}

		Validate.isTrue(
				isNotBlank(theSystem),
				"Can not expand ValueSet without explicit system - Hibernate Search is not enabled on this server.");

		for (ValueSet.ConceptSetFilterComponent nextFilter : theInclude.getFilter()) {
			boolean handled = false;
			switch (nextFilter.getProperty().toLowerCase()) {
				case "concept":
				case "code":
					if (nextFilter.getOp() == ValueSet.FilterOperator.ISA) {
						theValueSetCodeAccumulator.addMessage(
								"Processing IS-A filter in database - Note that Hibernate Search is not enabled on this server, so this operation can be inefficient.");
						TermConcept code = findCodeForFilterCriteriaCodeOrConcept(theSystem, nextFilter);
						addConceptAndChildren(
								theValueSetCodeAccumulator, theAddedCodes, theInclude, theSystem, theAdd, code);
						handled = true;
					}
					break;
				default:
					// TODO - we need to handle other properties (fields)
					// and other operations (not just is-a)
					// in some (preferably generic) way
					break;
			}

			if (!handled) {
				throwInvalidFilter(
						nextFilter,
						" - Note that Hibernate Search is disabled on this server so not all ValueSet expansion functionality is available.");
			}
		}

		if (theInclude.getFilter().isEmpty() && theInclude.getConcept().isEmpty()) {
			Collection<TermConcept> concepts =
					myConceptDao.fetchConceptsAndDesignationsByVersionPid(theVersion.getPid());
			for (TermConcept next : concepts) {
				addCodeIfNotAlreadyAdded(
						theValueSetCodeAccumulator,
						theAddedCodes,
						theAdd,
						theSystem,
						theInclude.getVersion(),
						next.getCode(),
						next.getDisplay(),
						next.getId(),
						next.getParentPidsAsString(),
						next.getDesignations());
			}
		}

		for (ValueSet.ConceptReferenceComponent next : theInclude.getConcept()) {
			if (!theSystem.equals(theInclude.getSystem()) && isNotBlank(theSystem)) {
				continue;
			}
			Collection<TermConceptDesignation> designations = next.getDesignation().stream()
					.map(t -> new TermConceptDesignation()
							.setValue(t.getValue())
							.setLanguage(t.getLanguage())
							.setUseCode(t.getUse().getCode())
							.setUseSystem(t.getUse().getSystem())
							.setUseDisplay(t.getUse().getDisplay()))
					.collect(Collectors.toList());
			addCodeIfNotAlreadyAdded(
					theValueSetCodeAccumulator,
					theAddedCodes,
					theAdd,
					theSystem,
					theInclude.getVersion(),
					next.getCode(),
					next.getDisplay(),
					null,
					null,
					designations);
		}
	}

	private void addConceptAndChildren(
			IValueSetConceptAccumulator theValueSetCodeAccumulator,
			Set<String> theAddedCodes,
			ValueSet.ConceptSetComponent theInclude,
			String theSystem,
			boolean theAdd,
			TermConcept theConcept) {
		for (TermConcept nextChild : theConcept.getChildCodes()) {
			boolean added = addCodeIfNotAlreadyAdded(
					theValueSetCodeAccumulator,
					theAddedCodes,
					theAdd,
					theSystem,
					theInclude.getVersion(),
					nextChild.getCode(),
					nextChild.getDisplay(),
					nextChild.getId(),
					nextChild.getParentPidsAsString(),
					nextChild.getDesignations());
			if (added) {
				addConceptAndChildren(
						theValueSetCodeAccumulator, theAddedCodes, theInclude, theSystem, theAdd, nextChild);
			}
		}
	}

	@Override
	@Transactional
	public String invalidatePreCalculatedExpansion(IIdType theValueSetId, RequestDetails theRequestDetails) {
		IBaseResource valueSet = myDaoRegistry.getResourceDao("ValueSet").read(theValueSetId, theRequestDetails);
		ValueSet canonicalValueSet = myVersionCanonicalizer.valueSetToCanonical(valueSet);
		Optional<TermValueSet> optionalTermValueSet = fetchValueSetEntity(canonicalValueSet);
		if (optionalTermValueSet.isEmpty()) {
			return myContext
					.getLocalizer()
					.getMessage(TermReadSvcImpl.class, "valueSetNotFoundInTerminologyDatabase", theValueSetId);
		}

		ourLog.info(
				"Invalidating pre-calculated expansion on ValueSet {} / {}", theValueSetId, canonicalValueSet.getUrl());

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
		myTermValueSetDao.save(termValueSet);

		afterValueSetExpansionStatusChange();

		return myContext
				.getLocalizer()
				.getMessage(
						TermReadSvcImpl.class, "valueSetPreExpansionInvalidated", termValueSet.getUrl(), totalConcepts);
	}

	@Override
	@Transactional
	public boolean isValueSetPreExpandedForCodeValidation(ValueSet theValueSet) {
		Optional<TermValueSet> optionalTermValueSet = fetchValueSetEntity(theValueSet);

		if (optionalTermValueSet.isEmpty()) {
			ourLog.warn(
					"ValueSet is not present in terminology tables. Will perform in-memory code validation. {}",
					getValueSetInfo(theValueSet));
			return false;
		}

		TermValueSet termValueSet = optionalTermValueSet.get();

		if (termValueSet.getExpansionStatus() != TermValueSetPreExpansionStatusEnum.EXPANDED) {
			ourLog.warn(
					"{} is present in terminology tables but not ready for persistence-backed invocation of operation $validation-code. Will perform in-memory code validation. Current status: {} | {}",
					getValueSetInfo(theValueSet),
					termValueSet.getExpansionStatus().name(),
					termValueSet.getExpansionStatus().getDescription());
			return false;
		}

		return true;
	}

	private Optional<TermValueSet> fetchValueSetEntity(ValueSet theValueSet) {
		JpaPid valueSetResourcePid = getValueSetResourcePersistentId(theValueSet);
		return myTermValueSetDao.findByResourcePid(valueSetResourcePid.getId());
	}

	private JpaPid getValueSetResourcePersistentId(ValueSet theValueSet) {
		return myIdHelperService.resolveResourcePersistentIds(
				RequestPartitionId.allPartitions(),
				theValueSet.getIdElement().getResourceType(),
				theValueSet.getIdElement().getIdPart());
	}

	protected IValidationSupport.CodeValidationResult validateCodeIsInPreExpandedValueSet(
			ConceptValidationOptions theValidationOptions,
			ValueSet theValueSet,
			String theSystem,
			String theCode,
			String theDisplay,
			Coding theCoding,
			CodeableConcept theCodeableConcept) {
		assert TransactionSynchronizationManager.isSynchronizationActive();

		ValidateUtil.isNotNullOrThrowUnprocessableEntity(theValueSet.hasId(), "ValueSet.id is required");
		JpaPid valueSetResourcePid = getValueSetResourcePersistentId(theValueSet);

		List<TermValueSetConcept> concepts = new ArrayList<>();
		if (isNotBlank(theCode)) {
			if (theValidationOptions.isInferSystem()) {
				concepts.addAll(
						myValueSetConceptDao.findByValueSetResourcePidAndCode(valueSetResourcePid.getId(), theCode));
			} else if (isNotBlank(theSystem)) {
				concepts.addAll(findByValueSetResourcePidSystemAndCode(valueSetResourcePid, theSystem, theCode));
			}
		} else if (theCoding != null) {
			if (theCoding.hasSystem() && theCoding.hasCode()) {
				concepts.addAll(findByValueSetResourcePidSystemAndCode(
						valueSetResourcePid, theCoding.getSystem(), theCoding.getCode()));
			}
		} else if (theCodeableConcept != null) {
			for (Coding coding : theCodeableConcept.getCoding()) {
				if (coding.hasSystem() && coding.hasCode()) {
					concepts.addAll(findByValueSetResourcePidSystemAndCode(
							valueSetResourcePid, coding.getSystem(), coding.getCode()));
					if (!concepts.isEmpty()) {
						break;
					}
				}
			}
		} else {
			return null;
		}

		TermValueSet valueSetEntity = myTermValueSetDao
				.findByResourcePid(valueSetResourcePid.getId())
				.orElseThrow(IllegalStateException::new);
		String timingDescription = toHumanReadableExpansionTimestamp(valueSetEntity);
		String preExpansionMessage = myContext
				.getLocalizer()
				.getMessage(TermReadSvcImpl.class, "validationPerformedAgainstPreExpansion", timingDescription);

		if (theValidationOptions.isValidateDisplay() && concepts.size() > 0) {
			String systemVersion = null;
			for (TermValueSetConcept concept : concepts) {
				systemVersion = concept.getSystemVersion();
				if (isBlank(theDisplay) || isBlank(concept.getDisplay()) || theDisplay.equals(concept.getDisplay())) {
					return new IValidationSupport.CodeValidationResult()
							.setCode(concept.getCode())
							.setDisplay(concept.getDisplay())
							.setCodeSystemVersion(concept.getSystemVersion())
							.setSourceDetails(preExpansionMessage);
				}
			}

			String expectedDisplay = concepts.get(0).getDisplay();
			return InMemoryTerminologyServerValidationSupport.createResultForDisplayMismatch(
					myContext,
					theCode,
					theDisplay,
					expectedDisplay,
					theSystem,
					systemVersion,
					myStorageSettings.getIssueSeverityForCodeDisplayMismatch());
		}

		if (!concepts.isEmpty()) {
			return new IValidationSupport.CodeValidationResult()
					.setCode(concepts.get(0).getCode())
					.setDisplay(concepts.get(0).getDisplay())
					.setCodeSystemVersion(concepts.get(0).getSystemVersion())
					.setMessage(preExpansionMessage);
		}

		// Ok, we failed
		List<TermValueSetConcept> outcome = myValueSetConceptDao.findByTermValueSetIdSystemOnly(
				Pageable.ofSize(1), valueSetEntity.getId(), theSystem);
		String append;
		if (outcome.size() == 0) {
			append = " - No codes in ValueSet belong to CodeSystem with URL " + theSystem;
		} else {
			String unknownCodeMessage = myContext
					.getLocalizer()
					.getMessage(TermReadSvcImpl.class, "unknownCodeInSystem", theSystem, theCode);
			append = " - " + unknownCodeMessage + ". " + preExpansionMessage;
		}

		return createFailureCodeValidationResult(theSystem, theCode, null, append);
	}

	private CodeValidationResult createFailureCodeValidationResult(
			String theSystem, String theCode, String theCodeSystemVersion, String theAppend) {
		String theMessage = "Unable to validate code " + theSystem + "#" + theCode + theAppend;
		return new CodeValidationResult()
				.setSeverity(IssueSeverity.ERROR)
				.setCodeSystemVersion(theCodeSystemVersion)
				.setMessage(theMessage)
				.addCodeValidationIssue(new CodeValidationIssue(
						theMessage,
						IssueSeverity.ERROR,
						CodeValidationIssueCode.CODE_INVALID,
						CodeValidationIssueCoding.INVALID_CODE));
	}

	private List<TermValueSetConcept> findByValueSetResourcePidSystemAndCode(
			JpaPid theResourcePid, String theSystem, String theCode) {
		assert TransactionSynchronizationManager.isSynchronizationActive();

		List<TermValueSetConcept> retVal = new ArrayList<>();
		Optional<TermValueSetConcept> optionalTermValueSetConcept;
		int versionIndex = theSystem.indexOf(OUR_PIPE_CHARACTER);
		if (versionIndex >= 0) {
			String systemUrl = theSystem.substring(0, versionIndex);
			String systemVersion = theSystem.substring(versionIndex + 1);
			optionalTermValueSetConcept = myValueSetConceptDao.findByValueSetResourcePidSystemAndCodeWithVersion(
					theResourcePid.getId(), systemUrl, systemVersion, theCode);
		} else {
			optionalTermValueSetConcept = myValueSetConceptDao.findByValueSetResourcePidSystemAndCode(
					theResourcePid.getId(), theSystem, theCode);
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
		TermCodeSystemVersion codeSystem =
				myCodeSystemVersionDao.findCurrentVersionForCodeSystemResourcePid(theCodeSystemResourcePid);
		return myConceptDao.findByCodeSystemAndCode(codeSystem.getPid(), theCode);
	}

	private void fetchParents(TermConcept theConcept, Set<TermConcept> theSetToPopulate) {
		for (TermConceptParentChildLink nextChildLink : theConcept.getParents()) {
			TermConcept nextChild = nextChildLink.getParent();
			if (addToSet(theSetToPopulate, nextChild)) {
				fetchParents(nextChild, theSetToPopulate);
			}
		}
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
		txTemplate.setReadOnly(true);

		return txTemplate.execute(t -> {
			TermCodeSystemVersionDetails csv = getCurrentCodeSystemVersion(theCodeSystem);
			if (csv == null) {
				return Optional.empty();
			}
			return myConceptDao.findByCodeSystemAndCode(csv.myPid, theCode);
		});
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public List<TermConcept> findCodes(String theCodeSystem, List<String> theCodeList) {
		TermCodeSystemVersionDetails csv = getCurrentCodeSystemVersion(theCodeSystem);
		if (csv == null) {
			return Collections.emptyList();
		}

		return myConceptDao.findByCodeSystemAndCodeList(csv.myPid, theCodeList);
	}

	@Nullable
	private TermCodeSystemVersionDetails getCurrentCodeSystemVersion(String theCodeSystemIdentifier) {
		String version = getVersionFromIdentifier(theCodeSystemIdentifier);
		TermCodeSystemVersionDetails retVal = myCodeSystemCurrentVersionCache.get(
				theCodeSystemIdentifier,
				t -> myTxTemplate.execute(tx -> {
					TermCodeSystemVersion csv = null;
					TermCodeSystem cs =
							myCodeSystemDao.findByCodeSystemUri(getUrlFromIdentifier(theCodeSystemIdentifier));
					if (cs != null) {
						if (version != null) {
							csv = myCodeSystemVersionDao.findByCodeSystemPidAndVersion(cs.getPid(), version);
						} else if (cs.getCurrentVersion() != null) {
							csv = cs.getCurrentVersion();
						}
					}
					if (csv != null) {
						return new TermCodeSystemVersionDetails(csv.getPid(), csv.getCodeSystemVersionId());
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
	public Set<TermConcept> findCodesAbove(
			Long theCodeSystemResourcePid, Long theCodeSystemVersionPid, String theCode) {
		StopWatch stopwatch = new StopWatch();

		Optional<TermConcept> concept = fetchLoadedCode(theCodeSystemResourcePid, theCode);
		if (concept.isEmpty()) {
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
	public Set<TermConcept> findCodesBelow(
			Long theCodeSystemResourcePid, Long theCodeSystemVersionPid, String theCode) {
		Stopwatch stopwatch = Stopwatch.createStarted();

		Optional<TermConcept> concept = fetchLoadedCode(theCodeSystemResourcePid, theCode);
		if (concept.isEmpty()) {
			return Collections.emptySet();
		}

		Set<TermConcept> retVal = new HashSet<>();
		retVal.add(concept.get());

		fetchChildren(concept.get(), retVal);

		ourLog.debug(
				"Fetched {} codes below code {} in {}ms",
				retVal.size(),
				theCode,
				stopwatch.elapsed(TimeUnit.MILLISECONDS));
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
	}

	@Override
	public void scheduleJobs(ISchedulerService theSchedulerService) {
		// Register scheduled job to pre-expand ValueSets
		// In the future it would be great to make this a cluster-aware task somehow
		ScheduledJobDefinition vsJobDefinition = new ScheduledJobDefinition();
		vsJobDefinition.setId(getClass().getName());
		vsJobDefinition.setJobClass(Job.class);
		theSchedulerService.scheduleClusteredJob(10 * DateUtils.MILLIS_PER_MINUTE, vsJobDefinition);
	}

	@Override
	public synchronized void preExpandDeferredValueSetsToTerminologyTables() {
		if (!myStorageSettings.isEnableTaskPreExpandValueSets()) {
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
				if (optionalTermValueSet.isEmpty()) {
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
					TermValueSet refreshedValueSetToExpand = myTermValueSetDao
							.findById(valueSetToExpand.getId())
							.orElseThrow(() -> new IllegalStateException("Unknown VS ID: " + valueSetToExpand.getId()));
					return getValueSetFromResourceTable(refreshedValueSetToExpand.getResource());
				});
				assert valueSet != null;

				ValueSetConceptAccumulator valueSetConceptAccumulator =
						myValueSetConceptAccumulatorFactory.create(valueSetToExpand);
				ValueSetExpansionOptions options = new ValueSetExpansionOptions();
				options.setIncludeHierarchy(true);
				expandValueSet(options, valueSet, valueSetConceptAccumulator);

				// We are done with this ValueSet.
				txTemplate.executeWithoutResult(t -> {
					valueSetToExpand.setExpansionStatus(TermValueSetPreExpansionStatusEnum.EXPANDED);
					valueSetToExpand.setExpansionTimestamp(new Date());
					myTermValueSetDao.saveAndFlush(valueSetToExpand);
				});

				afterValueSetExpansionStatusChange();

				ourLog.info(
						"Pre-expanded ValueSet[{}] with URL[{}] - Saved {} concepts in {}",
						valueSet.getId(),
						valueSet.getUrl(),
						valueSetConceptAccumulator.getConceptsSaved(),
						sw);

			} catch (Exception e) {
				ourLog.error(
						"Failed to pre-expand ValueSet with URL[{}]: {}", valueSetToExpand.getUrl(), e.getMessage(), e);
				txTemplate.executeWithoutResult(t -> {
					valueSetToExpand.setExpansionStatus(TermValueSetPreExpansionStatusEnum.FAILED_TO_EXPAND);
					myTermValueSetDao.saveAndFlush(valueSetToExpand);
				});

			} finally {
				setPreExpandingValueSets(false);
			}
		}
	}

	/*
	 * If a ValueSet has just finished pre-expanding, let's flush the caches. This is
	 * kind of a blunt tool, but it should ensure that users don't get unpredictable
	 * results while they test changes, which is probably a worthwhile sacrifice
	 */
	private void afterValueSetExpansionStatusChange() {
		// TODO: JA2 - Move this caching into the memorycacheservice, and only purge the
		// relevant individual cache
		myCachingValidationSupport.invalidateCaches();
	}

	private synchronized boolean isPreExpandingValueSets() {
		return myPreExpandingValueSets;
	}

	private synchronized void setPreExpandingValueSets(boolean thePreExpandingValueSets) {
		myPreExpandingValueSets = thePreExpandingValueSets;
	}

	private boolean isNotSafeToPreExpandValueSets() {
		return myDeferredStorageSvc != null && !myDeferredStorageSvc.isStorageQueueEmpty(true);
	}

	private Optional<TermValueSet> getNextTermValueSetNotExpanded() {
		Optional<TermValueSet> retVal = Optional.empty();
		Slice<TermValueSet> page = myTermValueSetDao.findByExpansionStatus(
				PageRequest.of(0, 1), TermValueSetPreExpansionStatusEnum.NOT_EXPANDED);

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
		if (optionalExistingTermValueSetByUrl.isEmpty()) {

			myTermValueSetDao.save(termValueSet);

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
	}

	@Override
	@Transactional
	public IFhirResourceDaoCodeSystem.SubsumesResult subsumes(
			IPrimitiveType<String> theCodeA,
			IPrimitiveType<String> theCodeB,
			IPrimitiveType<String> theSystem,
			IBaseCoding theCodingA,
			IBaseCoding theCodingB) {
		FhirVersionIndependentConcept conceptA = toConcept(theCodeA, theSystem, theCodingA);
		FhirVersionIndependentConcept conceptB = toConcept(theCodeB, theSystem, theCodingB);

		if (!StringUtils.equals(conceptA.getSystem(), conceptB.getSystem())) {
			throw new InvalidRequestException(
					Msg.code(903) + "Unable to test subsumption across different code systems");
		}

		if (!StringUtils.equals(conceptA.getSystemVersion(), conceptB.getSystemVersion())) {
			throw new InvalidRequestException(
					Msg.code(904) + "Unable to test subsumption across different code system versions");
		}

		String codeASystemIdentifier;
		if (StringUtils.isNotEmpty(conceptA.getSystemVersion())) {
			codeASystemIdentifier = conceptA.getSystem() + OUR_PIPE_CHARACTER + conceptA.getSystemVersion();
		} else {
			codeASystemIdentifier = conceptA.getSystem();
		}
		TermConcept codeA = findCode(codeASystemIdentifier, conceptA.getCode())
				.orElseThrow(() -> new InvalidRequestException("Unknown code: " + conceptA));

		String codeBSystemIdentifier;
		if (StringUtils.isNotEmpty(conceptB.getSystemVersion())) {
			codeBSystemIdentifier = conceptB.getSystem() + OUR_PIPE_CHARACTER + conceptB.getSystemVersion();
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

	@Override
	public IValidationSupport.LookupCodeResult lookupCode(
			ValidationSupportContext theValidationSupportContext, @Nonnull LookupCodeRequest theLookupCodeRequest) {
		TransactionTemplate txTemplate = new TransactionTemplate(myTransactionManager);
		return txTemplate.execute(t -> {
			final String theSystem = theLookupCodeRequest.getSystem();
			final String theCode = theLookupCodeRequest.getCode();
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
					if (isDisplayLanguageMatch(theLookupCodeRequest.getDisplayLanguage(), next.getLanguage())) {
						IValidationSupport.ConceptDesignation designation = new IValidationSupport.ConceptDesignation();
						designation.setLanguage(next.getLanguage());
						designation.setUseSystem(next.getUseSystem());
						designation.setUseCode(next.getUseCode());
						designation.setUseDisplay(next.getUseDisplay());
						designation.setValue(next.getValue());
						result.getDesignations().add(designation);
					}
				}

				final Collection<String> propertyNames = theLookupCodeRequest.getPropertyNames();
				for (TermConceptProperty next : code.getProperties()) {
					if (ObjectUtils.isNotEmpty(propertyNames) && !propertyNames.contains(next.getKey())) {
						continue;
					}
					if (next.getType() == TermConceptPropertyTypeEnum.CODING) {
						IValidationSupport.CodingConceptProperty property =
								new IValidationSupport.CodingConceptProperty(
										next.getKey(), next.getCodeSystem(), next.getValue(), next.getDisplay());
						result.getProperties().add(property);
					} else if (next.getType() == TermConceptPropertyTypeEnum.STRING) {
						IValidationSupport.StringConceptProperty property =
								new IValidationSupport.StringConceptProperty(next.getKey(), next.getValue());
						result.getProperties().add(property);
					} else {
						throw new InternalErrorException(Msg.code(905) + "Unknown type: " + next.getType());
					}
				}

				return result;

			} else {
				return new LookupCodeResult().setFound(false);
			}
		});
	}

	@Nullable
	private ConceptSubsumptionOutcome testForSubsumption(
			SearchSession theSearchSession,
			TermConcept theLeft,
			TermConcept theRight,
			ConceptSubsumptionOutcome theOutput) {
		List<TermConcept> fetch = theSearchSession
				.search(TermConcept.class)
				.where(f -> f.bool()
						.must(f.match().field("myId").matching(theRight.getId()))
						.must(f.match().field("myParentPids").matching(Long.toString(theLeft.getId()))))
				.fetchHits(1);

		if (fetch.size() > 0) {
			return theOutput;
		} else {
			return null;
		}
	}

	private ArrayList<FhirVersionIndependentConcept> toVersionIndependentConcepts(
			String theSystem, Set<TermConcept> codes) {
		ArrayList<FhirVersionIndependentConcept> retVal = new ArrayList<>(codes.size());
		for (TermConcept next : codes) {
			retVal.add(new FhirVersionIndependentConcept(theSystem, next.getCode()));
		}
		return retVal;
	}

	@Override
	@Transactional
	public CodeValidationResult validateCodeInValueSet(
			ValidationSupportContext theValidationSupportContext,
			ConceptValidationOptions theOptions,
			String theCodeSystem,
			String theCode,
			String theDisplay,
			@Nonnull IBaseResource theValueSet) {
		invokeRunnableForUnitTest();

		IPrimitiveType<?> urlPrimitive;
		if (theValueSet instanceof org.hl7.fhir.dstu2.model.ValueSet) {
			urlPrimitive = FhirContext.forDstu2Hl7OrgCached()
					.newTerser()
					.getSingleValueOrNull(theValueSet, "url", IPrimitiveType.class);
		} else {
			urlPrimitive = myContext.newTerser().getSingleValueOrNull(theValueSet, "url", IPrimitiveType.class);
		}
		String url = urlPrimitive.getValueAsString();
		if (isNotBlank(url)) {
			return validateCode(theValidationSupportContext, theOptions, theCodeSystem, theCode, theDisplay, url);
		}
		return null;
	}

	@CoverageIgnore
	@Override
	public IValidationSupport.CodeValidationResult validateCode(
			@Nonnull ValidationSupportContext theValidationSupportContext,
			@Nonnull ConceptValidationOptions theOptions,
			String theCodeSystemUrl,
			String theCode,
			String theDisplay,
			String theValueSetUrl) {
		// TODO GGG TRY TO JUST AUTO_PASS HERE AND SEE WHAT HAPPENS.
		invokeRunnableForUnitTest();
		theOptions.setValidateDisplay(isNotBlank(theDisplay));

		if (isNotBlank(theValueSetUrl)) {
			return validateCodeInValueSet(
					theValidationSupportContext, theOptions, theValueSetUrl, theCodeSystemUrl, theCode, theDisplay);
		}

		TransactionTemplate txTemplate = new TransactionTemplate(myTransactionManager);
		txTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		txTemplate.setReadOnly(true);
		Optional<FhirVersionIndependentConcept> codeOpt =
				txTemplate.execute(tx -> findCode(theCodeSystemUrl, theCode).map(c -> {
					String codeSystemVersionId = getCurrentCodeSystemVersion(theCodeSystemUrl).myCodeSystemVersionId;
					return new FhirVersionIndependentConcept(
							theCodeSystemUrl, c.getCode(), c.getDisplay(), codeSystemVersionId);
				}));

		if (codeOpt != null && codeOpt.isPresent()) {
			FhirVersionIndependentConcept code = codeOpt.get();
			if (!theOptions.isValidateDisplay()
					|| isBlank(code.getDisplay())
					|| isBlank(theDisplay)
					|| code.getDisplay().equals(theDisplay)) {
				return new CodeValidationResult().setCode(code.getCode()).setDisplay(code.getDisplay());
			} else {
				return InMemoryTerminologyServerValidationSupport.createResultForDisplayMismatch(
						myContext,
						theCode,
						theDisplay,
						code.getDisplay(),
						code.getSystem(),
						code.getSystemVersion(),
						myStorageSettings.getIssueSeverityForCodeDisplayMismatch());
			}
		}

		return createFailureCodeValidationResult(
				theCodeSystemUrl, theCode, null, createMessageAppendForCodeNotFoundInCodeSystem(theCodeSystemUrl));
	}

	IValidationSupport.CodeValidationResult validateCodeInValueSet(
			ValidationSupportContext theValidationSupportContext,
			ConceptValidationOptions theValidationOptions,
			String theValueSetUrl,
			String theCodeSystem,
			String theCode,
			String theDisplay) {
		IBaseResource valueSet =
				theValidationSupportContext.getRootValidationSupport().fetchValueSet(theValueSetUrl);
		CodeValidationResult retVal = null;

		// If we don't have a PID, this came from some source other than the JPA
		// database, so we don't need to check if it's pre-expanded or not
		if (valueSet instanceof IAnyResource) {
			Long pid = IDao.RESOURCE_PID.get((IAnyResource) valueSet);
			if (pid != null) {
				TransactionTemplate txTemplate = new TransactionTemplate(myTxManager);
				retVal = txTemplate.execute(tx -> {
					if (isValueSetPreExpandedForCodeValidation(valueSet)) {
						return validateCodeIsInPreExpandedValueSet(
								theValidationOptions, valueSet, theCodeSystem, theCode, theDisplay, null, null);
					} else {
						return null;
					}
				});
			}
		}

		if (retVal == null) {
			if (valueSet != null) {
				retVal = myInMemoryTerminologyServerValidationSupport.validateCodeInValueSet(
						theValidationSupportContext,
						theValidationOptions,
						theCodeSystem,
						theCode,
						theDisplay,
						valueSet);
			} else {
				String append = " - Unable to locate ValueSet[" + theValueSetUrl + "]";
				retVal = createFailureCodeValidationResult(theCodeSystem, theCode, null, append);
			}
		}

		// Check if someone is accidentally using a VS url where it should be a CS URL
		if (retVal != null
				&& retVal.getCode() == null
				&& theCodeSystem != null
				&& myContext.getVersion().getVersion().isNewerThan(FhirVersionEnum.DSTU2)) {
			if (isValueSetSupported(theValidationSupportContext, theCodeSystem)) {
				if (!isCodeSystemSupported(theValidationSupportContext, theCodeSystem)) {
					String newMessage = "Unable to validate code " + theCodeSystem + "#" + theCode
							+ " - Supplied system URL is a ValueSet URL and not a CodeSystem URL, check if it is correct: "
							+ theCodeSystem;
					retVal.setMessage(newMessage);
				}
			}
		}

		return retVal;
	}

	@Override
	public CodeSystem fetchCanonicalCodeSystemFromCompleteContext(String theSystem) {
		IValidationSupport validationSupport = provideValidationSupport();
		IBaseResource codeSystem = validationSupport.fetchCodeSystem(theSystem);
		if (codeSystem != null) {
			codeSystem = myVersionCanonicalizer.codeSystemToCanonical(codeSystem);
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
	protected IValidationSupport provideValidationSupport() {
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
			valueSet = myVersionCanonicalizer.valueSetToCanonical(valueSet);
		}
		return (ValueSet) valueSet;
	}

	@Override
	public IBaseResource fetchValueSet(String theValueSetUrl) {
		return provideJpaValidationSupport().fetchValueSet(theValueSetUrl);
	}

	@Override
	public FhirContext getFhirContext() {
		return myContext;
	}

	private void findCodesAbove(
			CodeSystem theSystem,
			String theSystemString,
			String theCode,
			List<FhirVersionIndependentConcept> theListToPopulate) {
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

	private void findCodesBelow(
			CodeSystem theSystem,
			String theSystemString,
			String theCode,
			List<FhirVersionIndependentConcept> theListToPopulate) {
		List<CodeSystem.ConceptDefinitionComponent> conceptList = theSystem.getConcept();
		findCodesBelow(theSystemString, theCode, theListToPopulate, conceptList);
	}

	private void findCodesBelow(
			String theSystemString,
			String theCode,
			List<FhirVersionIndependentConcept> theListToPopulate,
			List<CodeSystem.ConceptDefinitionComponent> conceptList) {
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

	private void addAllChildren(
			String theSystemString,
			CodeSystem.ConceptDefinitionComponent theCode,
			List<FhirVersionIndependentConcept> theListToPopulate) {
		if (isNotBlank(theCode.getCode())) {
			theListToPopulate.add(new FhirVersionIndependentConcept(theSystemString, theCode.getCode()));
		}
		for (CodeSystem.ConceptDefinitionComponent nextChild : theCode.getConcept()) {
			addAllChildren(theSystemString, nextChild, theListToPopulate);
		}
	}

	private boolean addTreeIfItContainsCode(
			String theSystemString,
			CodeSystem.ConceptDefinitionComponent theNext,
			String theCode,
			List<FhirVersionIndependentConcept> theListToPopulate) {
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

	@Nonnull
	private FhirVersionIndependentConcept toConcept(
			IPrimitiveType<String> theCodeType,
			IPrimitiveType<String> theCodeSystemIdentifierType,
			IBaseCoding theCodingType) {
		String code = theCodeType != null ? theCodeType.getValueAsString() : null;
		String system = theCodeSystemIdentifierType != null
				? getUrlFromIdentifier(theCodeSystemIdentifierType.getValueAsString())
				: null;
		String systemVersion = theCodeSystemIdentifierType != null
				? getVersionFromIdentifier(theCodeSystemIdentifierType.getValueAsString())
				: null;
		if (theCodingType != null) {
			Coding canonicalizedCoding = myVersionCanonicalizer.codingToCanonical(theCodingType);
			assert canonicalizedCoding != null; // Shouldn't be null, since theCodingType isn't
			code = canonicalizedCoding.getCode();
			system = canonicalizedCoding.getSystem();
			systemVersion = canonicalizedCoding.getVersion();
		}
		return new FhirVersionIndependentConcept(system, code, null, systemVersion);
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
			if (vsIdOpt.isEmpty()) {
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

	@Override
	public Optional<IBaseResource> readCodeSystemByForcedId(String theForcedId) {
		@SuppressWarnings("unchecked")
		List<ResourceTable> resultList = (List<ResourceTable>) myEntityManager
				.createQuery("select r from ResourceTable r "
						+ "where r.myResourceType = 'CodeSystem' and r.myFhirId = :fhirId")
				.setParameter("fhirId", theForcedId)
				.getResultList();
		if (resultList.isEmpty()) return Optional.empty();

		if (resultList.size() > 1)
			throw new NonUniqueResultException(Msg.code(911) + "More than one CodeSystem is pointed by forcedId: "
					+ theForcedId + ". Was constraint " + ResourceTable.IDX_RES_TYPE_FHIR_ID + " removed?");

		IFhirResourceDao<CodeSystem> csDao = myDaoRegistry.getResourceDao("CodeSystem");
		IBaseResource cs = myJpaStorageResourceParser.toResource(resultList.get(0), false);
		return Optional.of(cs);
	}

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
					.massIndexer(TermConcept.class)
					.dropAndCreateSchemaOnStart(true)
					.purgeAllOnStart(false)
					.batchSizeToLoadObjects(100)
					.cacheMode(CacheMode.IGNORE)
					.threadsToLoadObjects(6)
					.transactionTimeout(60 * SECONDS_IN_MINUTE)
					.monitor(new PojoMassIndexingLoggingMonitor(INDEXED_ROOTS_LOGGING_COUNT))
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
		if (maxConnectionsOpt.isEmpty()) {
			return DEFAULT_MASS_INDEXER_OBJECT_LOADING_THREADS;
		}

		int maxConnections = maxConnectionsOpt.get();
		int usableThreads = maxConnections < 6 ? 1 : maxConnections - 5;
		int objectThreads = Math.min(usableThreads, MAX_MASS_INDEXER_OBJECT_LOADING_THREADS);
		ourLog.debug(
				"Data source connection pool has {} connections allocated, so reindexing will use {} object "
						+ "loading threads (each using a connection)",
				maxConnections,
				objectThreads);
		return objectThreads;
	}

	@VisibleForTesting
	SearchSession getSearchSession() {
		return Search.session(myEntityManager);
	}

	@Override
	public ValueSetExpansionOutcome expandValueSet(
			ValidationSupportContext theValidationSupportContext,
			ValueSetExpansionOptions theExpansionOptions,
			@Nonnull IBaseResource theValueSetToExpand) {
		ValueSet canonicalInput = myVersionCanonicalizer.valueSetToCanonical(theValueSetToExpand);
		org.hl7.fhir.r4.model.ValueSet expandedR4 = expandValueSet(theExpansionOptions, canonicalInput);
		return new ValueSetExpansionOutcome(myVersionCanonicalizer.valueSetFromCanonical(expandedR4));
	}

	@Override
	public IBaseResource expandValueSet(ValueSetExpansionOptions theExpansionOptions, IBaseResource theInput) {
		org.hl7.fhir.r4.model.ValueSet valueSetToExpand = myVersionCanonicalizer.valueSetToCanonical(theInput);
		org.hl7.fhir.r4.model.ValueSet valueSetR4 = expandValueSet(theExpansionOptions, valueSetToExpand);
		return myVersionCanonicalizer.valueSetFromCanonical(valueSetR4);
	}

	@Override
	public void expandValueSet(
			ValueSetExpansionOptions theExpansionOptions,
			IBaseResource theValueSetToExpand,
			IValueSetConceptAccumulator theValueSetCodeAccumulator) {
		org.hl7.fhir.r4.model.ValueSet valueSetToExpand =
				myVersionCanonicalizer.valueSetToCanonical(theValueSetToExpand);
		expandValueSet(theExpansionOptions, valueSetToExpand, theValueSetCodeAccumulator);
	}

	private org.hl7.fhir.r4.model.ValueSet getValueSetFromResourceTable(ResourceTable theResourceTable) {
		Class<? extends IBaseResource> type =
				getFhirContext().getResourceDefinition("ValueSet").getImplementingClass();
		IBaseResource valueSet = myJpaStorageResourceParser.toResource(type, theResourceTable, null, false);
		return myVersionCanonicalizer.valueSetToCanonical(valueSet);
	}

	@Override
	public CodeValidationResult validateCodeIsInPreExpandedValueSet(
			ConceptValidationOptions theOptions,
			IBaseResource theValueSet,
			String theSystem,
			String theCode,
			String theDisplay,
			IBaseDatatype theCoding,
			IBaseDatatype theCodeableConcept) {
		ValidateUtil.isNotNullOrThrowUnprocessableEntity(theValueSet, "ValueSet must not be null");
		org.hl7.fhir.r4.model.ValueSet valueSetR4 = myVersionCanonicalizer.valueSetToCanonical(theValueSet);
		org.hl7.fhir.r4.model.Coding codingR4 = myVersionCanonicalizer.codingToCanonical((IBaseCoding) theCoding);
		org.hl7.fhir.r4.model.CodeableConcept codeableConcept =
				myVersionCanonicalizer.codeableConceptToCanonical(theCodeableConcept);

		return validateCodeIsInPreExpandedValueSet(
				theOptions, valueSetR4, theSystem, theCode, theDisplay, codingR4, codeableConcept);
	}

	@Override
	public boolean isValueSetPreExpandedForCodeValidation(IBaseResource theValueSet) {
		ValidateUtil.isNotNullOrThrowUnprocessableEntity(theValueSet, "ValueSet must not be null");
		org.hl7.fhir.r4.model.ValueSet valueSetR4 = myVersionCanonicalizer.valueSetToCanonical(theValueSet);
		return isValueSetPreExpandedForCodeValidation(valueSetR4);
	}

	private static class TermCodeSystemVersionDetails {

		private final long myPid;
		private final String myCodeSystemVersionId;

		public TermCodeSystemVersionDetails(long thePid, String theCodeSystemVersionId) {
			myPid = thePid;
			myCodeSystemVersionId = theCodeSystemVersionId;
		}
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
	 * Properties returned from method buildSearchScroll
	 */
	private static final class SearchProperties {
		private List<Supplier<SearchScroll<EntityReference>>> mySearchScroll = new ArrayList<>();
		private List<String> myIncludeOrExcludeCodes;

		public List<Supplier<SearchScroll<EntityReference>>> getSearchScroll() {
			return mySearchScroll;
		}

		public void addSearchScroll(Supplier<SearchScroll<EntityReference>> theSearchScrollSupplier) {
			mySearchScroll.add(theSearchScrollSupplier);
		}

		public List<String> getIncludeOrExcludeCodes() {
			return myIncludeOrExcludeCodes;
		}

		public void setIncludeOrExcludeCodes(List<String> theIncludeOrExcludeCodes) {
			myIncludeOrExcludeCodes = theIncludeOrExcludeCodes;
		}

		public boolean hasIncludeOrExcludeCodes() {
			return !myIncludeOrExcludeCodes.isEmpty();
		}
	}

	static boolean isValueSetDisplayLanguageMatch(ValueSetExpansionOptions theExpansionOptions, String theStoredLang) {
		if (theExpansionOptions == null) {
			return true;
		}

		if (theExpansionOptions.getTheDisplayLanguage() == null || theStoredLang == null) {
			return true;
		}

		return theExpansionOptions.getTheDisplayLanguage().equalsIgnoreCase(theStoredLang);
	}

	@Nonnull
	private static String createMessageAppendForCodeNotFoundInCodeSystem(String theCodeSystemUrl) {
		return " - Code is not found in CodeSystem: " + theCodeSystemUrl;
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

	static List<TermConcept> toPersistedConcepts(
			List<CodeSystem.ConceptDefinitionComponent> theConcept, TermCodeSystemVersion theCodeSystemVersion) {
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
	static TermConcept toTermConcept(
			CodeSystem.ConceptDefinitionComponent theConceptDefinition, TermCodeSystemVersion theCodeSystemVersion) {
		TermConcept termConcept = new TermConcept();
		termConcept.setCode(theConceptDefinition.getCode());
		termConcept.setCodeSystemVersion(theCodeSystemVersion);
		termConcept.setDisplay(theConceptDefinition.getDisplay());
		termConcept.addChildren(
				toPersistedConcepts(theConceptDefinition.getConcept(), theCodeSystemVersion), RelationshipTypeEnum.ISA);

		for (CodeSystem.ConceptDefinitionDesignationComponent designationComponent :
				theConceptDefinition.getDesignation()) {
			if (isNotBlank(designationComponent.getValue())) {
				TermConceptDesignation designation = termConcept.addDesignation();
				designation.setLanguage(designationComponent.hasLanguage() ? designationComponent.getLanguage() : null);
				if (designationComponent.hasUse()) {
					designation.setUseSystem(
							designationComponent.getUse().hasSystem()
									? designationComponent.getUse().getSystem()
									: null);
					designation.setUseCode(
							designationComponent.getUse().hasCode()
									? designationComponent.getUse().getCode()
									: null);
					designation.setUseDisplay(
							designationComponent.getUse().hasDisplay()
									? designationComponent.getUse().getDisplay()
									: null);
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
			} else if (next.getValue() instanceof BooleanType) {
				property.setType(TermConceptPropertyTypeEnum.BOOLEAN);
				property.setValue(((BooleanType) next.getValue()).getValueAsString());
			} else if (next.getValue() instanceof IntegerType) {
				property.setType(TermConceptPropertyTypeEnum.INTEGER);
				property.setValue(((IntegerType) next.getValue()).getValueAsString());
			} else if (next.getValue() instanceof DecimalType) {
				property.setType(TermConceptPropertyTypeEnum.DECIMAL);
				property.setValue(((DecimalType) next.getValue()).getValueAsString());
			} else if (next.getValue() instanceof DateTimeType) {
				// DateType is not supported because it's not
				// supported in CodeSystem.setValue
				property.setType(TermConceptPropertyTypeEnum.DATETIME);
				property.setValue(((DateTimeType) next.getValue()).getValueAsString());
			} else if (next.getValue() instanceof Coding) {
				Coding nextCoding = next.getValueCoding();
				property.setType(TermConceptPropertyTypeEnum.CODING);
				property.setCodeSystem(nextCoding.getSystem());
				property.setValue(nextCoding.getCode());
				property.setDisplay(nextCoding.getDisplay());
			} else if (next.getValue() != null) {
				ourLog.warn("Don't know how to handle properties of type: "
						+ next.getValue().getClass());
				continue;
			}

			termConcept.getProperties().add(property);
		}
		return termConcept;
	}

	static boolean isDisplayLanguageMatch(String theReqLang, String theStoredLang) {
		// NOTE: return the designation when one of then is not specified.
		if (theReqLang == null || theStoredLang == null) return true;

		return theReqLang.equalsIgnoreCase(theStoredLang);
	}
}
