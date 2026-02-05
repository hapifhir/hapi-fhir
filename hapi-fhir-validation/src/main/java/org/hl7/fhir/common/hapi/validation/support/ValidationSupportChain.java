package org.hl7.fhir.common.hapi.validation.support;

import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.LookupCodeRequest;
import ca.uhn.fhir.context.support.TranslateConceptResults;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.context.support.ValueSetExpansionOptions;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.sl.cache.Cache;
import ca.uhn.fhir.sl.cache.CacheFactory;
import ca.uhn.fhir.util.FhirTerser;
import ca.uhn.fhir.util.Logs;
import ca.uhn.fhir.util.StopWatch;
import ca.uhn.fhir.util.UrlUtil;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Strings;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.slf4j.Logger;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.StringUtils.defaultIfBlank;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * This validation support module has two primary purposes: It can be used to
 * chain multiple backing modules together, and it can optionally cache the
 * results.
 * <p>
 * The following chaining logic is used:
 * <ul>
 * <li>
 *     Calls to {@literal fetchAll...} methods such as {@link #fetchAllConformanceResources()}
 *     and {@link #fetchAllStructureDefinitions()} will call every method in the chain in
 *     order, and aggregate the results into a single list to return.
 * </li>
 * <li>
 *     Calls to fetch or validate codes, such as {@link #validateCode(ValidationSupportContext, ConceptValidationOptions, String, String, String, String)}
 *     and {@link #lookupCode(ValidationSupportContext, LookupCodeRequest)} will first test
 *     each module in the chain using the {@link #isCodeSystemSupported(ValidationSupportContext, String)}
 *     or {@link #isValueSetSupported(ValidationSupportContext, String)}
 *     methods (depending on whether a ValueSet URL is present in the method parameters)
 *     and will invoke any methods in the chain which return that they can handle the given
 *     CodeSystem/ValueSet URL. The first non-null value returned by a method in the chain
 *     that can support the URL will be returned to the caller.
 * </li>
 * <li>
 *     All other methods will invoke each method in the chain in order, and will stop processing and return
 *     immediately as soon as the first non-null value is returned.
 * </li>
 * </ul>
 * </p>
 * <p>
 * The following caching logic is used if caching is enabled using {@link CacheConfiguration}.
 * You can use {@link CacheConfiguration#disabled()} if you want to disable caching.
 * <ul>
 * <li>
 *     Calls to fetch StructureDefinitions including {@link #fetchAllStructureDefinitions()}
 *     and {@link #fetchStructureDefinition(String)} are cached in a non-expiring cache.
 *     This is because the {@link org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator}
 *     module makes assumptions that these objects will not change for the lifetime
 *     of the validator for performance reasons.
 * </li>
 * <li>
 *     Calls to all other {@literal fetchAll...} methods including
 *     {@link #fetchAllConformanceResources()} and {@link #fetchAllSearchParameters()}
 *     cache their results in an expiring cache, but will refresh that cache asynchronously.
 * </li>
 * <li>
 *     Results of {@link #generateSnapshot(ValidationSupportContext, IBaseResource, String, String, String)}
 *     are not cached, since this method is generally called in contexts where the results
 *     are cached.
 * </li>
 * <li>
 *     Results of all other methods are stored in an expiring cache.
 * </li>
 * </ul>
 * </p>
 * <p>
 * Note that caching functionality used to be provided by a separate provider
 * called {@literal CachingValidationSupport} but that functionality has been
 * moved into this class as of HAPI FHIR 8.0.0, because it is possible to
 * provide a more efficient chain when these functions are combined.
 * </p>
 */
public class ValidationSupportChain implements IValidationSupport {
	public static final ValueSetExpansionOptions EMPTY_EXPANSION_OPTIONS = new ValueSetExpansionOptions();
	static Logger ourLog = Logs.getTerminologyTroubleshootingLog();
	private final List<IValidationSupport> myChain = new CopyOnWriteArrayList<>();

	@Nullable
	private final Cache<BaseKey<?>, Object> myExpiringCache;

	@Nullable
	private final Map<BaseKey<?>, Object> myNonExpiringCache;

	/**
	 * See class documentation for an explanation of why this is separate
	 * and non-expiring. Note that this field is non-synchronized. If you
	 * access it, you should first wrap the call in
	 * <code>synchronized(myStructureDefinitionsByUrl)</code>.
	 */
	@Nonnull
	private final Map<String, IBaseResource> myStructureDefinitionsByUrl = new HashMap<>();
	/**
	 * See class documentation for an explanation of why this is separate
	 * and non-expiring. Note that this field is non-synchronized. If you
	 * access it, you should first wrap the call in
	 * <code>synchronized(myStructureDefinitionsByUrl)</code> (synchronize on
	 * the other field because both collections are expected to be modified
	 * at the same time).
	 */
	@Nonnull
	private final List<IBaseResource> myStructureDefinitionsAsList = new ArrayList<>();

	private final ThreadPoolExecutor myBackgroundExecutor;
	private final CacheConfiguration myCacheConfiguration;
	private boolean myEnabledValidationForCodingsLogicalAnd;
	private String myName = getClass().getSimpleName();
	private ValidationSupportChainMetrics myMetrics;
	private volatile boolean myHaveFetchedAllStructureDefinitions = false;

	/**
	 * Constructor which initializes the chain with no modules (modules
	 * must subsequently be registered using {@link #addValidationSupport(IValidationSupport)}).
	 * The cache will be enabled using {@link CacheConfiguration#defaultValues()}.
	 */
	public ValidationSupportChain() {
		/*
		 * Note, this constructor is called by default when
		 * FhirContext#getValidationSupport() is called, so it should
		 * provide sensible defaults.
		 */
		this(Collections.emptyList());
	}

	/**
	 * Constructor which initializes the chain with the given modules.
	 * The cache will be enabled using {@link CacheConfiguration#defaultValues()}.
	 */
	public ValidationSupportChain(IValidationSupport... theValidationSupportModules) {
		this(
				theValidationSupportModules != null
						? Arrays.asList(theValidationSupportModules)
						: Collections.emptyList());
	}

	/**
	 * Constructor which initializes the chain with the given modules.
	 * The cache will be enabled using {@link CacheConfiguration#defaultValues()}.
	 */
	public ValidationSupportChain(List<IValidationSupport> theValidationSupportModules) {
		this(CacheConfiguration.defaultValues(), theValidationSupportModules);
	}

	/**
	 * Constructor
	 *
	 * @param theCacheConfiguration       The caching configuration
	 * @param theValidationSupportModules The initial modules to add to the chain
	 */
	public ValidationSupportChain(
			@Nonnull CacheConfiguration theCacheConfiguration, IValidationSupport... theValidationSupportModules) {
		this(
				theCacheConfiguration,
				theValidationSupportModules != null
						? Arrays.asList(theValidationSupportModules)
						: Collections.emptyList());
	}

	/**
	 * Constructor
	 *
	 * @param theCacheConfiguration       The caching configuration
	 * @param theValidationSupportModules The initial modules to add to the chain
	 */
	public ValidationSupportChain(
			@Nonnull CacheConfiguration theCacheConfiguration,
			@Nonnull List<IValidationSupport> theValidationSupportModules) {

		Validate.notNull(theCacheConfiguration, "theCacheConfiguration must not be null");
		Validate.notNull(theValidationSupportModules, "theValidationSupportModules must not be null");

		myCacheConfiguration = theCacheConfiguration;
		if (theCacheConfiguration.getCacheSize() == 0 || theCacheConfiguration.getCacheTimeout() == 0) {
			myExpiringCache = null;
			myNonExpiringCache = null;
			myBackgroundExecutor = null;
		} else {
			myExpiringCache =
					CacheFactory.build(theCacheConfiguration.getCacheTimeout(), theCacheConfiguration.getCacheSize());
			myNonExpiringCache = Collections.synchronizedMap(new HashMap<>());

			LinkedBlockingQueue<Runnable> executorQueue = new LinkedBlockingQueue<>(1000);
			BasicThreadFactory threadFactory = new BasicThreadFactory.Builder()
					.namingPattern("CachingValidationSupport-%d")
					.daemon(false)
					.priority(Thread.NORM_PRIORITY)
					.build();

			// NOTE: We're not using ThreadPoolUtil here, because that class depends on Spring and
			// we want the validator infrastructure to not require spring dependencies.
			myBackgroundExecutor = new ThreadPoolExecutor(
					1,
					1,
					0L,
					TimeUnit.MILLISECONDS,
					executorQueue,
					threadFactory,
					new ThreadPoolExecutor.DiscardPolicy());
		}

		for (IValidationSupport next : theValidationSupportModules) {
			if (next != null) {
				addValidationSupport(next);
			}
		}
	}

	@Override
	public String getName() {
		return myName;
	}

	/**
	 * Sets a name for this chain. This name will be returned by
	 * {@link #getName()} and used by OpenTelemetry.
	 */
	public void setName(String theName) {
		Validate.notBlank(theName, "theName must not be blank");
		myName = theName;
	}

	@PostConstruct
	public void start() {
		if (myMetrics == null) {
			myMetrics = new ValidationSupportChainMetrics(this);
			myMetrics.start();
		}
	}

	@PreDestroy
	public void stop() {
		if (myMetrics != null) {
			myMetrics.stop();
			myMetrics = null;
		}
	}

	@Override
	public boolean isCodeableConceptValidationSuccessfulIfNotAllCodingsAreValid() {
		return myEnabledValidationForCodingsLogicalAnd;
	}

	/**
	 * When validating a CodeableConcept containing multiple codings, this method can be used to control whether
	 * the validator requires all codings in the CodeableConcept to be valid in order to consider the
	 * CodeableConcept valid.
	 * <p>
	 * See VersionSpecificWorkerContextWrapper#validateCode in hapi-fhir-validation, and the refer to the values below
	 * for the behaviour associated with each value.
	 * </p>
	 * <p>
	 *   <ul>
	 *     <li>If <code>false</code> (default setting) the validation for codings will return a positive result only if
	 *     ALL codings are valid.</li>
	 * 	   <li>If <code>true</code> the validation for codings will return a positive result if ANY codings are valid.
	 * 	   </li>
	 * 	  </ul>
	 * </p>
	 *
	 * @return true or false depending on the desired coding validation behaviour.
	 */
	public ValidationSupportChain setCodeableConceptValidationSuccessfulIfNotAllCodingsAreValid(
			boolean theEnabledValidationForCodingsLogicalAnd) {
		myEnabledValidationForCodingsLogicalAnd = theEnabledValidationForCodingsLogicalAnd;
		return this;
	}

	@Override
	public TranslateConceptResults translateConcept(TranslateCodeRequest theRequest) {
		TranslateConceptKey key = new TranslateConceptKey(theRequest);
		CacheValue<TranslateConceptResults> retVal = getFromCache(key);
		if (retVal == null) {

			/*
			 * The chain behaviour for this method is to call every element in the
			 * chain and aggregate the results (as opposed to just using the first
			 * module which provides a response).
			 */
			retVal = CacheValue.empty();

			TranslateConceptResults outcome = null;
			for (IValidationSupport next : myChain) {
				TranslateConceptResults translations = next.translateConcept(theRequest);
				if (translations != null) {
					if (outcome == null) {
						outcome = new TranslateConceptResults();
					}

					if (outcome.getMessage() == null) {
						outcome.setMessage(translations.getMessage());
					}

					if (translations.getResult() && !outcome.getResult()) {
						outcome.setResult(translations.getResult());
						outcome.setMessage(translations.getMessage());
					}

					if (!translations.isEmpty()) {
						ourLog.debug(
								"{} found {} concept translation{} for {}",
								next.getName(),
								translations.size(),
								translations.size() > 1 ? "s" : "",
								theRequest);
						outcome.getResults().addAll(translations.getResults());
					}
				}
			}

			if (outcome != null) {
				retVal = new CacheValue<>(outcome);
			}

			putInCache(key, retVal);
		}

		return retVal.getValue();
	}

	@Override
	public void invalidateCaches() {
		ourLog.debug("Invalidating caches in {} validation support modules", myChain.size());
		myHaveFetchedAllStructureDefinitions = false;
		for (IValidationSupport next : myChain) {
			next.invalidateCaches();
		}
		if (myNonExpiringCache != null) {
			myNonExpiringCache.clear();
		}
		if (myExpiringCache != null) {
			myExpiringCache.invalidateAll();
		}
		synchronized (myStructureDefinitionsByUrl) {
			myStructureDefinitionsByUrl.clear();
			myStructureDefinitionsAsList.clear();
		}
	}

	/**
	 * Invalidate the expiring cache, but not the permanent StructureDefinition cache
	 *
	 * @since 8.0.0
	 */
	public void invalidateExpiringCaches() {
		if (myExpiringCache != null) {
			myExpiringCache.invalidateAll();
		}
	}

	@Override
	public boolean isValueSetSupported(ValidationSupportContext theValidationSupportContext, String theValueSetUrl) {
		for (IValidationSupport next : myChain) {
			boolean retVal = isValueSetSupported(theValidationSupportContext, next, theValueSetUrl);
			if (retVal) {
				ourLog.debug("ValueSet {} found in {}", theValueSetUrl, next.getName());
				return true;
			}
		}
		return false;
	}

	private boolean isValueSetSupported(
			ValidationSupportContext theValidationSupportContext,
			IValidationSupport theValidationSupport,
			String theValueSetUrl) {
		IsValueSetSupportedKey key = new IsValueSetSupportedKey(theValidationSupport, theValueSetUrl);
		CacheValue<Boolean> value = getFromCache(key);
		if (value == null) {
			value = new CacheValue<>(
					theValidationSupport.isValueSetSupported(theValidationSupportContext, theValueSetUrl));
			putInCache(key, value);
		}
		return value.getValue();
	}

	@Override
	public IBaseResource generateSnapshot(
			ValidationSupportContext theValidationSupportContext,
			IBaseResource theInput,
			String theUrl,
			String theWebUrl,
			String theProfileName) {

		/*
		 * No caching for this method because we typically cache the results anyhow.
		 * If this ever changes, make sure to update the class javadocs and the
		 * HAPI FHIR documentation which indicate that this isn't cached.
		 */

		for (IValidationSupport next : myChain) {
			IBaseResource retVal =
					next.generateSnapshot(theValidationSupportContext, theInput, theUrl, theWebUrl, theProfileName);
			if (retVal != null) {
				ourLog.atDebug()
						.setMessage("Profile snapshot for {} generated by {}")
						.addArgument(theInput::getIdElement)
						.addArgument(next::getName)
						.log();
				return retVal;
			}
		}
		return null;
	}

	@Override
	public FhirContext getFhirContext() {
		if (myChain.isEmpty()) {
			return null;
		}
		return myChain.get(0).getFhirContext();
	}

	/**
	 * Add a validation support module to the chain.
	 * <p>
	 * Note that this method is not thread-safe. All validation support modules should be added prior to use.
	 * </p>
	 *
	 * @param theValidationSupport The validation support. Must not be null, and must have a {@link #getFhirContext() FhirContext} that is configured for the same FHIR version as other entries in the chain.
	 */
	public void addValidationSupport(IValidationSupport theValidationSupport) {
		int index = myChain.size();
		addValidationSupport(index, theValidationSupport);
	}

	/**
	 * Add a validation support module to the chain at the given index.
	 * <p>
	 * Note that this method is not thread-safe. All validation support modules should be added prior to use.
	 * </p>
	 *
	 * @param theIndex             The index to add to
	 * @param theValidationSupport The validation support. Must not be null, and must have a {@link #getFhirContext() FhirContext} that is configured for the same FHIR version as other entries in the chain.
	 */
	public void addValidationSupport(int theIndex, IValidationSupport theValidationSupport) {
		Validate.notNull(theValidationSupport, "theValidationSupport must not be null");
		invalidateCaches();

		if (theValidationSupport.getFhirContext() == null) {
			String message = "Can not add validation support: getFhirContext() returns null";
			throw new ConfigurationException(Msg.code(708) + message);
		}

		FhirContext existingFhirContext = getFhirContext();
		if (existingFhirContext != null) {
			FhirVersionEnum newVersion =
					theValidationSupport.getFhirContext().getVersion().getVersion();
			FhirVersionEnum existingVersion = existingFhirContext.getVersion().getVersion();
			if (!existingVersion.equals(newVersion)) {
				String message = "Trying to add validation support of version " + newVersion + " to chain with "
						+ myChain.size() + " entries of version " + existingVersion;
				throw new ConfigurationException(Msg.code(709) + message);
			}
		}

		myChain.add(theIndex, theValidationSupport);
	}

	/**
	 * Checks if the validation support chain is empty.
	 * This method is useful for subclasses that need to determine whether validators
	 * have already been added to the chain, particularly in scenarios where the same
	 * chain instance may be shared across multiple application contexts.
	 *
	 * @return true if the chain is empty, false otherwise
	 */
	protected boolean isChainEmpty() {
		return myChain.isEmpty();
	}

	/**
	 * Removes an item from the chain. Note that this method is mostly intended for testing. Removing items from the chain while validation is
	 * actually occurring is not an expected use case for this class.
	 */
	public void removeValidationSupport(IValidationSupport theValidationSupport) {
		invalidateCaches();
		myChain.remove(theValidationSupport);
	}

	@Nullable
	@Override
	public ValueSetExpansionOutcome expandValueSet(
			ValidationSupportContext theValidationSupportContext,
			@Nullable ValueSetExpansionOptions theExpansionOptions,
			@Nonnull String theValueSetUrlToExpand)
			throws ResourceNotFoundException {
		ValueSetExpansionOptions expansionOptions = defaultIfNull(theExpansionOptions, EMPTY_EXPANSION_OPTIONS);
		ExpandValueSetKey key = new ExpandValueSetKey(expansionOptions, null, theValueSetUrlToExpand);
		CacheValue<ValueSetExpansionOutcome> retVal = getFromCache(key);

		if (retVal == null) {
			retVal = CacheValue.empty();
			for (IValidationSupport next : myChain) {
				if (isValueSetSupported(theValidationSupportContext, next, theValueSetUrlToExpand)) {
					ValueSetExpansionOutcome expanded =
							next.expandValueSet(theValidationSupportContext, expansionOptions, theValueSetUrlToExpand);
					if (expanded != null) {
						ourLog.debug("ValueSet {} expanded by URL by {}", theValueSetUrlToExpand, next.getName());
						retVal = new CacheValue<>(expanded);
						break;
					}
				}
			}

			putInCache(key, retVal);
		}

		if (retVal.getValue() == null) {
			throw new ResourceNotFoundException(
					Msg.code(2788) + "Unknown ValueSet: " + UrlUtil.escapeUrlParam(theValueSetUrlToExpand));
		}

		return retVal.getValue();
	}

	@Override
	public ValueSetExpansionOutcome expandValueSet(
			ValidationSupportContext theValidationSupportContext,
			ValueSetExpansionOptions theExpansionOptions,
			@Nonnull IBaseResource theValueSetToExpand) {

		ValueSetExpansionOptions expansionOptions = defaultIfNull(theExpansionOptions, EMPTY_EXPANSION_OPTIONS);
		String id = theValueSetToExpand.getIdElement().getValue();
		ExpandValueSetKey key = null;
		CacheValue<ValueSetExpansionOutcome> retVal = null;
		if (isNotBlank(id)) {
			key = new ExpandValueSetKey(expansionOptions, id, null);
			retVal = getFromCache(key);
		}
		if (retVal == null) {
			retVal = CacheValue.empty();
			for (IValidationSupport next : myChain) {
				ValueSetExpansionOutcome expanded =
						next.expandValueSet(theValidationSupportContext, expansionOptions, theValueSetToExpand);
				if (expanded != null) {
					ourLog.debug("ValueSet {} expanded by {}", theValueSetToExpand.getIdElement(), next.getName());
					retVal = new CacheValue<>(expanded);
					break;
				}
			}

			if (key != null) {
				putInCache(key, retVal);
			}
		}

		return retVal.getValue();
	}

	@Override
	public boolean isRemoteTerminologyServiceConfigured() {
		return myChain.stream().anyMatch(RemoteTerminologyServiceValidationSupport.class::isInstance);
	}

	@Override
	public List<IBaseResource> fetchAllConformanceResources() {
		FetchAllKey key = new FetchAllKey(FetchAllKey.TypeEnum.ALL);
		Supplier<List<IBaseResource>> loader = () -> {
			List<IBaseResource> allCandidates = new ArrayList<>();
			for (IValidationSupport next : myChain) {
				List<IBaseResource> candidates = next.fetchAllConformanceResources();
				if (candidates != null) {
					allCandidates.addAll(candidates);
				}
			}
			return allCandidates;
		};

		return getFromCacheWithAsyncRefresh(key, loader);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Nonnull
	public List<IBaseResource> fetchAllStructureDefinitions() {
		if (!myHaveFetchedAllStructureDefinitions) {
			FhirTerser terser = getFhirContext().newTerser();
			List<IBaseResource> allStructureDefinitions =
					doFetchStructureDefinitions(IValidationSupport::fetchAllStructureDefinitions);
			if (myExpiringCache != null) {
				synchronized (myStructureDefinitionsByUrl) {
					for (IBaseResource structureDefinition : allStructureDefinitions) {
						String url = terser.getSinglePrimitiveValueOrNull(structureDefinition, "url");
						String version = terser.getSinglePrimitiveValueOrNull(structureDefinition, "version");

						// Most queries to the base structure definitions are versionless
						// And they shouldn't be overwritten with multiple versions anyway
						// (see JpaPersistedResourceValidationSupportChain#doFetchResource())
						// So we'll cache them without version.
						boolean shouldAppendVersionToUrl = !Strings.isNullOrEmpty(url)
								&& version != null
								&& !url.startsWith(URL_PREFIX_STRUCTURE_DEFINITION);
						if (shouldAppendVersionToUrl) {
							url = url + "|" + version;
						}

						url = defaultIfBlank(url, UUID.randomUUID().toString());
						if (myStructureDefinitionsByUrl.putIfAbsent(url, structureDefinition) == null) {
							myStructureDefinitionsAsList.add(structureDefinition);
						}
					}
				}
			}
			myHaveFetchedAllStructureDefinitions = true;
		}
		return Collections.unmodifiableList(new ArrayList<>(myStructureDefinitionsAsList));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<IBaseResource> fetchAllNonBaseStructureDefinitions() {
		FetchAllKey key = new FetchAllKey(FetchAllKey.TypeEnum.ALL_NON_BASE_STRUCTUREDEFINITIONS);
		Supplier<List<IBaseResource>> loader =
				() -> doFetchStructureDefinitions(IValidationSupport::fetchAllNonBaseStructureDefinitions);
		return getFromCacheWithAsyncRefresh(key, loader);
	}

	@SuppressWarnings("unchecked")
	@Nullable
	@Override
	public <T extends IBaseResource> List<T> fetchAllSearchParameters() {
		FetchAllKey key = new FetchAllKey(FetchAllKey.TypeEnum.ALL_SEARCHPARAMETERS);
		Supplier<List<IBaseResource>> loader =
				() -> doFetchStructureDefinitions(IValidationSupport::fetchAllSearchParameters);
		return (List<T>) getFromCacheWithAsyncRefresh(key, loader);
	}

	private List<IBaseResource> doFetchStructureDefinitions(
			Function<IValidationSupport, List<IBaseResource>> theFunction) {
		ArrayList<IBaseResource> retVal = new ArrayList<>();
		Set<String> urls = new HashSet<>();
		for (IValidationSupport nextSupport : myChain) {
			List<IBaseResource> allStructureDefinitions = theFunction.apply(nextSupport);
			if (allStructureDefinitions != null) {
				for (IBaseResource next : allStructureDefinitions) {

					IPrimitiveType<?> urlType =
							getFhirContext().newTerser().getSingleValueOrNull(next, "url", IPrimitiveType.class);
					if (urlType == null
							|| isBlank(urlType.getValueAsString())
							|| urls.add(urlType.getValueAsString())) {
						retVal.add(next);
					}
				}
			}
		}
		return retVal;
	}

	@Override
	public IBaseResource fetchCodeSystem(String theSystem) {
		Function<IValidationSupport, IBaseResource> invoker = v -> v.fetchCodeSystem(theSystem);
		ResourceByUrlKey<IBaseResource> key = new ResourceByUrlKey<>(ResourceByUrlKey.TypeEnum.CODESYSTEM, theSystem);
		return fetchValue(key, invoker, theSystem);
	}

	private <T> T fetchValue(ResourceByUrlKey<T> theKey, Function<IValidationSupport, T> theInvoker, String theUrl) {
		CacheValue<T> retVal = getFromCache(theKey);

		if (retVal == null) {
			retVal = CacheValue.empty();
			for (IValidationSupport next : myChain) {
				T outcome = theInvoker.apply(next);
				if (outcome != null) {
					ourLog.debug("{} {} with URL {} fetched by {}", theKey.myType, outcome, theUrl, next.getName());
					retVal = new CacheValue<>(outcome);
					break;
				}
			}
			putInCache(theKey, retVal);
		}

		return retVal.getValue();
	}

	@Override
	public IBaseResource fetchValueSet(String theUrl) {
		Function<IValidationSupport, IBaseResource> invoker = v -> v.fetchValueSet(theUrl);
		ResourceByUrlKey<IBaseResource> key = new ResourceByUrlKey<>(ResourceByUrlKey.TypeEnum.VALUESET, theUrl);
		return fetchValue(key, invoker, theUrl);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends IBaseResource> T fetchResource(Class<T> theClass, String theUri) {

		/*
		 * If we're looking for a common type with a dedicated fetch method, use that
		 * so that we can use a common cache location for lookups wanting a given
		 * URL on both methods (the validator will call both paths when looking for a
		 * specific URL so this improves cache efficiency).
		 */
		if (theClass != null) {
			BaseRuntimeElementDefinition<?> elementDefinition = getFhirContext().getElementDefinition(theClass);
			if (elementDefinition != null) {
				switch (elementDefinition.getName()) {
					case "ValueSet":
						return (T) fetchValueSet(theUri);
					case "CodeSystem":
						return (T) fetchCodeSystem(theUri);
					case "StructureDefinition":
						return (T) fetchStructureDefinition(theUri);
				}
			}
		}

		Function<IValidationSupport, T> invoker = v -> v.fetchResource(theClass, theUri);
		TypedResourceByUrlKey<T> key = new TypedResourceByUrlKey<>(theClass, theUri);
		return fetchValue(key, invoker, theUri);
	}

	@Override
	public byte[] fetchBinary(String theKey) {
		Function<IValidationSupport, byte[]> invoker = v -> v.fetchBinary(theKey);
		ResourceByUrlKey<byte[]> key = new ResourceByUrlKey<>(ResourceByUrlKey.TypeEnum.BINARY, theKey);
		return fetchValue(key, invoker, theKey);
	}

	@Override
	public IBaseResource fetchStructureDefinition(String theUrl) {
		synchronized (myStructureDefinitionsByUrl) {
			IBaseResource candidate = myStructureDefinitionsByUrl.get(theUrl);
			if (candidate == null) {
				Function<IValidationSupport, IBaseResource> invoker = v -> v.fetchStructureDefinition(theUrl);
				ResourceByUrlKey<IBaseResource> key =
						new ResourceByUrlKey<>(ResourceByUrlKey.TypeEnum.STRUCTUREDEFINITION, theUrl);
				candidate = fetchValue(key, invoker, theUrl);
				if (myExpiringCache != null) {
					if (candidate != null) {
						if (myStructureDefinitionsByUrl.putIfAbsent(theUrl, candidate) == null) {
							myStructureDefinitionsAsList.add(candidate);
						}
					}
				}
			}
			return candidate;
		}
	}

	@Override
	public boolean isCodeSystemSupported(ValidationSupportContext theValidationSupportContext, String theSystem) {
		for (IValidationSupport next : myChain) {
			if (isCodeSystemSupported(theValidationSupportContext, next, theSystem)) {
				if (ourLog.isDebugEnabled()) {
					ourLog.debug("CodeSystem with System {} is supported by {}", theSystem, next.getName());
				}
				return true;
			}
		}
		return false;
	}

	private boolean isCodeSystemSupported(
			ValidationSupportContext theValidationSupportContext,
			IValidationSupport theValidationSupport,
			String theCodeSystemUrl) {
		IsCodeSystemSupportedKey key = new IsCodeSystemSupportedKey(theValidationSupport, theCodeSystemUrl);
		CacheValue<Boolean> value = getFromCache(key);
		if (value == null) {
			value = new CacheValue<>(
					theValidationSupport.isCodeSystemSupported(theValidationSupportContext, theCodeSystemUrl));
			putInCache(key, value);
		}
		return value.getValue();
	}

	@Override
	public CodeValidationResult validateCode(
			@Nonnull ValidationSupportContext theValidationSupportContext,
			@Nonnull ConceptValidationOptions theOptions,
			String theCodeSystem,
			String theCode,
			String theDisplay,
			String theValueSetUrl) {

		ValidateCodeKey key = new ValidateCodeKey(theOptions, theCodeSystem, theCode, theDisplay, theValueSetUrl);
		CacheValue<CodeValidationResult> retVal = getFromCache(key);
		if (retVal == null) {
			retVal = CacheValue.empty();

			for (IValidationSupport next : myChain) {
				if ((isBlank(theValueSetUrl) && isCodeSystemSupported(theValidationSupportContext, next, theCodeSystem))
						|| (isNotBlank(theValueSetUrl)
								&& isValueSetSupported(theValidationSupportContext, next, theValueSetUrl))) {
					CodeValidationResult outcome = next.validateCode(
							theValidationSupportContext,
							theOptions,
							theCodeSystem,
							theCode,
							theDisplay,
							theValueSetUrl);
					if (outcome != null) {
						ourLog.debug(
								"Code {}|{} '{}' in ValueSet {} validated by {}",
								theCodeSystem,
								theCode,
								theDisplay,
								theValueSetUrl,
								next.getName());
						retVal = new CacheValue<>(outcome);
						break;
					}
				}
			}

			if (retVal.getValue() == null) {
				CodeValidationResult unknownCodeSystemResult =
						generateResultForUnknownCodeSystem(theCodeSystem, theCode);
				if (unknownCodeSystemResult != null) {
					retVal = new CacheValue<>(unknownCodeSystemResult);
				}
			}

			putInCache(key, retVal);
		}

		return retVal.getValue();
	}

	/**
	 * Generates a {@link CodeValidationResult} for an unknown CodeSystem.
	 * <p>
	 * If theCodeSystem is null, this method returns null.
	 * If a ValidationSupport in the chain can fetch the CodeSystem, it returns null
	 * Otherwise, it returns a CodeValidationResult with an error message. The message severity is set to ERROR by this function,
	 * but it might be overridden after
	 * - by the core validator, which has its own logic for determining the severity of an issue for an unknown CodeSystem,
	 *   which is based on the binding strength. See https://github.com/hapifhir/org.hl7.fhir.core/issues/2129
	 * - and by the {@link ca.uhn.fhir.rest.server.interceptor.validation.ValidationMessageUnknownCodeSystemProcessingInterceptor}
	 *   to a configured severity if it is registered.
	 * </p>
	 *
	 * This function was originally part of now deprecated UnknownCodeSystemWarningValidationSupport
	 * @param theCodeSystem The CodeSystem URL to validate
	 * @param theCode       The code to validate
	 * @return A CodeValidationResult indicating the error, or null if theCodeSystem is null
	 * or a validation support can fetch the code system.
	 */
	@Nullable
	private CodeValidationResult generateResultForUnknownCodeSystem(String theCodeSystem, String theCode) {

		if (theCodeSystem == null) {
			return null;
		}
		IBaseResource codeSystem = fetchCodeSystem(theCodeSystem);
		if (codeSystem != null) {
			return null;
		}

		CodeValidationResult result = new CodeValidationResult();
		result.setSeverity(IssueSeverity.ERROR);
		String message = "CodeSystem is unknown and can't be validated: %s for '%s#%s'"
				.formatted(theCodeSystem, theCodeSystem, theCode);
		result.setMessage(message);

		result.addIssue(new CodeValidationIssue(
				message, IssueSeverity.ERROR, CodeValidationIssueCode.NOT_FOUND, CodeValidationIssueCoding.NOT_FOUND));
		return result;
	}

	@Override
	public CodeValidationResult validateCodeInValueSet(
			ValidationSupportContext theValidationSupportContext,
			ConceptValidationOptions theOptions,
			String theCodeSystem,
			String theCode,
			String theDisplay,
			@Nonnull IBaseResource theValueSet) {
		String url = CommonCodeSystemsTerminologyService.getValueSetUrl(getFhirContext(), theValueSet);

		ValidateCodeKey key = null;
		CacheValue<CodeValidationResult> retVal = null;
		if (isNotBlank(url)) {
			key = new ValidateCodeKey(theOptions, theCodeSystem, theCode, theDisplay, url);
			retVal = getFromCache(key);
		}
		if (retVal != null) {
			return retVal.getValue();
		}

		retVal = CacheValue.empty();
		for (IValidationSupport next : myChain) {
			if (isBlank(url) || isValueSetSupported(theValidationSupportContext, next, url)) {
				CodeValidationResult outcome = next.validateCodeInValueSet(
						theValidationSupportContext, theOptions, theCodeSystem, theCode, theDisplay, theValueSet);
				if (outcome != null) {
					ourLog.debug(
							"Code {}|{} '{}' in ValueSet {} validated by {}",
							theCodeSystem,
							theCode,
							theDisplay,
							theValueSet.getIdElement(),
							next.getName());
					retVal = new CacheValue<>(outcome);
					break;
				}
			}
		}

		if (key != null) {
			putInCache(key, retVal);
		}

		return retVal.getValue();
	}

	@Override
	public LookupCodeResult lookupCode(
			ValidationSupportContext theValidationSupportContext, @Nonnull LookupCodeRequest theLookupCodeRequest) {

		LookupCodeKey key = new LookupCodeKey(theLookupCodeRequest);
		CacheValue<LookupCodeResult> retVal = getFromCache(key);
		if (retVal == null) {

			retVal = CacheValue.empty();
			for (IValidationSupport next : myChain) {
				final String system = theLookupCodeRequest.getSystem();
				final String code = theLookupCodeRequest.getCode();
				final String displayLanguage = theLookupCodeRequest.getDisplayLanguage();
				if (isCodeSystemSupported(theValidationSupportContext, next, system)) {
					LookupCodeResult lookupCodeResult =
							next.lookupCode(theValidationSupportContext, theLookupCodeRequest);
					if (lookupCodeResult == null) {
						/*
						This branch has been added as a fall-back mechanism for supporting lookupCode
						methods marked as deprecated in interface IValidationSupport.
						*/
						//noinspection deprecation
						lookupCodeResult = next.lookupCode(theValidationSupportContext, system, code, displayLanguage);
					}
					if (lookupCodeResult != null) {
						ourLog.debug(
								"Code {}|{}{} {} by {}",
								system,
								code,
								isBlank(displayLanguage) ? "" : " (" + theLookupCodeRequest.getDisplayLanguage() + ")",
								lookupCodeResult.isFound() ? "found" : "not found",
								next.getName());
						retVal = new CacheValue<>(lookupCodeResult);
						break;
					}
				}
			}

			putInCache(key, retVal);
		}

		return retVal.getValue();
	}

	/**
	 * Returns a view of the {@link IValidationSupport} modules within
	 * this chain. The returned collection is unmodifiable and will reflect
	 * changes to the underlying list.
	 *
	 * @since 8.0.0
	 */
	public List<IValidationSupport> getValidationSupports() {
		return Collections.unmodifiableList(myChain);
	}

	private <T> void putInCache(BaseKey<T> key, CacheValue<T> theValue) {
		if (myExpiringCache != null) {
			myExpiringCache.put(key, theValue);
		}
	}

	@SuppressWarnings("unchecked")
	private <T> CacheValue<T> getFromCache(BaseKey<T> key) {
		if (myExpiringCache != null) {
			return (CacheValue<T>) myExpiringCache.getIfPresent(key);
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	private List<IBaseResource> getFromCacheWithAsyncRefresh(
			FetchAllKey theKey, Supplier<List<IBaseResource>> theLoader) {
		if (myExpiringCache == null || myNonExpiringCache == null) {
			return theLoader.get();
		}

		CacheValue<List<IBaseResource>> retVal = getFromCache(theKey);
		if (retVal == null) {
			retVal = (CacheValue<List<IBaseResource>>) myNonExpiringCache.get(theKey);
			if (retVal != null) {
				Runnable loaderTask = () -> {
					List<IBaseResource> loadedItem = theLoader.get();
					CacheValue<List<IBaseResource>> value = new CacheValue<>(loadedItem);
					myNonExpiringCache.put(theKey, value);
					putInCache(theKey, value);
				};
				List<IBaseResource> returnValue = retVal.getValue();

				myBackgroundExecutor.execute(loaderTask);

				return returnValue;
			} else {
				// Avoid flooding the validation support modules tons of concurrent
				// requests for the same thing
				synchronized (this) {
					retVal = getFromCache(theKey);
					if (retVal == null) {
						StopWatch sw = new StopWatch();
						ourLog.info("Performing initial retrieval for non-expiring cache: {}", theKey);
						retVal = new CacheValue<>(theLoader.get());
						ourLog.info("Initial retrieval for non-expiring cache {} succeeded in {}", theKey, sw);
						myNonExpiringCache.put(theKey, retVal);
						putInCache(theKey, retVal);
					}
				}
			}
		}

		return retVal.getValue();
	}

	public void logCacheSizes() {
		String b = "Cache sizes:" + "\n * Expiring: "
				+ (myExpiringCache != null ? myExpiringCache.estimatedSize() : "(disabled)")
				+ "\n * Non-Expiring: "
				+ (myNonExpiringCache != null ? myNonExpiringCache.size() : "(disabled)");
		ourLog.info(b);
	}

	long getMetricExpiringCacheEntries() {
		if (myExpiringCache != null) {
			return myExpiringCache.estimatedSize();
		} else {
			return 0;
		}
	}

	int getMetricNonExpiringCacheEntries() {
		synchronized (myStructureDefinitionsByUrl) {
			int size = myNonExpiringCache != null ? myNonExpiringCache.size() : 0;
			return size + myStructureDefinitionsAsList.size();
		}
	}

	int getMetricExpiringCacheMaxSize() {
		return myCacheConfiguration.getCacheSize();
	}

	/**
	 * Clears all validation support modules from the chain.
	 * This method is intended for unit testing purposes only to allow
	 * rebuilding the chain with different configurations.
	 */
	@VisibleForTesting
	protected void clearChainForUnitTest() {
		myChain.clear();
	}

	/**
	 * @since 5.4.0
	 */
	public static class CacheConfiguration {

		private long myCacheTimeout;
		private int myCacheSize;

		/**
		 * Non-instantiable. Use the factory methods.
		 */
		private CacheConfiguration() {
			super();
		}

		public long getCacheTimeout() {
			return myCacheTimeout;
		}

		public CacheConfiguration setCacheTimeout(Duration theCacheTimeout) {
			Validate.isTrue(theCacheTimeout.toMillis() >= 0, "Cache timeout must not be negative");
			myCacheTimeout = theCacheTimeout.toMillis();
			return this;
		}

		public int getCacheSize() {
			return myCacheSize;
		}

		public CacheConfiguration setCacheSize(int theCacheSize) {
			Validate.isTrue(theCacheSize >= 0, "Cache size must not be negative");
			myCacheSize = theCacheSize;
			return this;
		}

		/**
		 * Creates a cache configuration with sensible default values:
		 * 10 minutes expiry, and 5000 cache entries.
		 */
		public static CacheConfiguration defaultValues() {
			return new CacheConfiguration()
					.setCacheTimeout(Duration.ofMinutes(10))
					.setCacheSize(5000);
		}

		public static CacheConfiguration disabled() {
			return new CacheConfiguration().setCacheSize(0).setCacheTimeout(Duration.ofMillis(0));
		}
	}

	/**
	 * @param <V> The value type associated with this key
	 */
	@SuppressWarnings("unused")
	abstract static class BaseKey<V> {

		@Override
		public abstract boolean equals(Object theO);

		@Override
		public abstract int hashCode();
	}

	static class ExpandValueSetKey extends BaseKey<ValueSetExpansionOutcome> {

		private final ValueSetExpansionOptions myOptions;
		private final String myId;
		private final String myUrl;
		private final int myHashCode;

		private ExpandValueSetKey(ValueSetExpansionOptions theOptions, String theId, String theUrl) {
			myOptions = theOptions;
			myId = theId;
			myUrl = theUrl;
			myHashCode = Objects.hash(myOptions, myId, myUrl);
		}

		@Override
		public boolean equals(Object theO) {
			if (this == theO) return true;
			if (!(theO instanceof ExpandValueSetKey)) return false;
			ExpandValueSetKey that = (ExpandValueSetKey) theO;
			return Objects.equals(myOptions, that.myOptions)
					&& Objects.equals(myId, that.myId)
					&& Objects.equals(myUrl, that.myUrl);
		}

		@Override
		public int hashCode() {
			return myHashCode;
		}
	}

	static class FetchAllKey extends BaseKey<List<IBaseResource>> {

		private final TypeEnum myType;
		private final int myHashCode;

		private FetchAllKey(TypeEnum theType) {
			myType = theType;
			myHashCode = Objects.hash(myType);
		}

		@Override
		public boolean equals(Object theO) {
			if (this == theO) return true;
			if (!(theO instanceof FetchAllKey)) return false;
			FetchAllKey that = (FetchAllKey) theO;
			return myType == that.myType;
		}

		@Override
		public int hashCode() {
			return myHashCode;
		}

		private enum TypeEnum {
			ALL,
			ALL_STRUCTUREDEFINITIONS,
			ALL_NON_BASE_STRUCTUREDEFINITIONS,
			ALL_SEARCHPARAMETERS
		}
	}

	static class ResourceByUrlKey<T> extends BaseKey<T> {

		private final TypeEnum myType;
		private final String myUrl;
		private final int myHashCode;

		private ResourceByUrlKey(TypeEnum theType, String theUrl) {
			this(theType, theUrl, Objects.hash("ResourceByUrl", theType, theUrl));
		}

		private ResourceByUrlKey(TypeEnum theType, String theUrl, int theHashCode) {
			myType = theType;
			myUrl = theUrl;
			myHashCode = theHashCode;
		}

		@Override
		public boolean equals(Object theO) {
			if (this == theO) return true;
			if (!(theO instanceof ResourceByUrlKey)) return false;
			ResourceByUrlKey<?> that = (ResourceByUrlKey<?>) theO;
			return myType == that.myType && Objects.equals(myUrl, that.myUrl);
		}

		@Override
		public int hashCode() {
			return myHashCode;
		}

		private enum TypeEnum {
			CODESYSTEM,
			VALUESET,
			RESOURCE,
			BINARY,
			STRUCTUREDEFINITION
		}
	}

	static class TypedResourceByUrlKey<T> extends ResourceByUrlKey<T> {

		private final Class<?> myType;

		private TypedResourceByUrlKey(Class<?> theType, String theUrl) {
			super(ResourceByUrlKey.TypeEnum.RESOURCE, theUrl, Objects.hash("TypedResourceByUrl", theType, theUrl));
			myType = theType;
		}

		@Override
		public boolean equals(Object theO) {
			if (this == theO) return true;
			if (!(theO instanceof TypedResourceByUrlKey)) return false;
			if (!super.equals(theO)) return false;
			TypedResourceByUrlKey<?> that = (TypedResourceByUrlKey<?>) theO;
			return Objects.equals(myType, that.myType);
		}

		@Override
		public int hashCode() {
			return Objects.hash(super.hashCode(), myType);
		}
	}

	static class IsValueSetSupportedKey extends BaseKey<Boolean> {

		private final String myValueSetUrl;
		private final IValidationSupport myValidationSupport;
		private final int myHashCode;

		private IsValueSetSupportedKey(IValidationSupport theValidationSupport, String theValueSetUrl) {
			myValidationSupport = theValidationSupport;
			myValueSetUrl = theValueSetUrl;
			myHashCode = Objects.hash("IsValueSetSupported", theValidationSupport, myValueSetUrl);
		}

		@Override
		public boolean equals(Object theO) {
			if (this == theO) return true;
			if (!(theO instanceof IsValueSetSupportedKey)) return false;
			IsValueSetSupportedKey that = (IsValueSetSupportedKey) theO;
			return myValidationSupport == that.myValidationSupport && Objects.equals(myValueSetUrl, that.myValueSetUrl);
		}

		@Override
		public int hashCode() {
			return myHashCode;
		}
	}

	static class IsCodeSystemSupportedKey extends BaseKey<Boolean> {

		private final String myCodeSystemUrl;
		private final IValidationSupport myValidationSupport;
		private final int myHashCode;

		private IsCodeSystemSupportedKey(IValidationSupport theValidationSupport, String theCodeSystemUrl) {
			myValidationSupport = theValidationSupport;
			myCodeSystemUrl = theCodeSystemUrl;
			myHashCode = Objects.hash("IsCodeSystemSupported", theValidationSupport, myCodeSystemUrl);
		}

		@Override
		public boolean equals(Object theO) {
			if (this == theO) return true;
			if (!(theO instanceof IsCodeSystemSupportedKey)) return false;
			IsCodeSystemSupportedKey that = (IsCodeSystemSupportedKey) theO;
			return myValidationSupport == that.myValidationSupport
					&& Objects.equals(myCodeSystemUrl, that.myCodeSystemUrl);
		}

		@Override
		public int hashCode() {
			return myHashCode;
		}
	}

	static class LookupCodeKey extends BaseKey<LookupCodeResult> {

		private final LookupCodeRequest myRequest;
		private final int myHashCode;

		private LookupCodeKey(LookupCodeRequest theRequest) {
			myRequest = theRequest;
			myHashCode = Objects.hash("LookupCode", myRequest);
		}

		@Override
		public boolean equals(Object theO) {
			if (this == theO) return true;
			if (!(theO instanceof LookupCodeKey)) return false;
			LookupCodeKey that = (LookupCodeKey) theO;
			return Objects.equals(myRequest, that.myRequest);
		}

		@Override
		public int hashCode() {
			return myHashCode;
		}
	}

	static class TranslateConceptKey extends BaseKey<TranslateConceptResults> {

		private final TranslateCodeRequest myRequest;
		private final int myHashCode;

		private TranslateConceptKey(TranslateCodeRequest theRequest) {
			myRequest = theRequest;
			myHashCode = Objects.hash("TranslateConcept", myRequest);
		}

		@Override
		public boolean equals(Object theO) {
			if (this == theO) return true;
			if (!(theO instanceof TranslateConceptKey)) return false;
			TranslateConceptKey that = (TranslateConceptKey) theO;
			return Objects.equals(myRequest, that.myRequest);
		}

		@Override
		public int hashCode() {
			return myHashCode;
		}
	}

	static class ValidateCodeKey extends BaseKey<CodeValidationResult> {
		private final String mySystem;
		private final String myCode;
		private final String myDisplay;
		private final String myValueSetUrl;
		private final int myHashCode;
		private final ConceptValidationOptions myOptions;

		private ValidateCodeKey(
				ConceptValidationOptions theOptions,
				String theSystem,
				String theCode,
				String theDisplay,
				String theValueSetUrl) {
			// copy ConceptValidationOptions because it is mutable
			myOptions = ConceptValidationOptions.copy(theOptions);
			mySystem = theSystem;
			myCode = theCode;
			myDisplay = theDisplay;
			myValueSetUrl = theValueSetUrl;
			myHashCode = Objects.hash("ValidateCodeKey", myOptions, mySystem, myCode, myDisplay, myValueSetUrl);
		}

		@Override
		public boolean equals(Object theO) {
			if (this == theO) return true;
			if (!(theO instanceof ValidateCodeKey)) return false;
			ValidateCodeKey that = (ValidateCodeKey) theO;
			return Objects.equals(myOptions, that.myOptions)
					&& Objects.equals(mySystem, that.mySystem)
					&& Objects.equals(myCode, that.myCode)
					&& Objects.equals(myDisplay, that.myDisplay)
					&& Objects.equals(myValueSetUrl, that.myValueSetUrl);
		}

		@Override
		public int hashCode() {
			return myHashCode;
		}

		@Override
		public String toString() {
			return "ValidateCodeKey{"
					+ "mySystem='" + mySystem + '\''
					+ ", myCode='" + myCode + '\''
					+ ", myDisplay='" + myDisplay + '\''
					+ ", myValueSetUrl='" + myValueSetUrl + '\''
					+ ", myHashCode=" + myHashCode + '\''
					+ ", myOptions=" + myOptions + '}';
		}
	}

	/**
	 * This class is basically the same thing as Optional, but is a distinct thing
	 * because we want to use it as a method parameter value, and compare instances of
	 * it with null. Both of these things generate warnings in various linters.
	 */
	private static class CacheValue<T> {

		private static final CacheValue<CodeValidationResult> EMPTY = new CacheValue<>(null);

		private final T myValue;

		private CacheValue(T theValue) {
			myValue = theValue;
		}

		public T getValue() {
			return myValue;
		}

		@SuppressWarnings("unchecked")
		public static <T> CacheValue<T> empty() {
			return (CacheValue<T>) EMPTY;
		}
	}
}
