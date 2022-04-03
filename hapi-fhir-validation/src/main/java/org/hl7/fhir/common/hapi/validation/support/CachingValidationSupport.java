package org.hl7.fhir.common.hapi.validation.support;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.TranslateConceptResults;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.context.support.ValueSetExpansionOptions;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.apache.commons.lang3.time.DateUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.StringUtils.defaultIfBlank;
import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@SuppressWarnings("unchecked")
public class CachingValidationSupport extends BaseValidationSupportWrapper implements IValidationSupport {

	private static final Logger ourLog = LoggerFactory.getLogger(CachingValidationSupport.class);
	public static final ValueSetExpansionOptions EMPTY_EXPANSION_OPTIONS = new ValueSetExpansionOptions();

	private final Cache<String, Object> myCache;
	private final Cache<String, Object> myValidateCodeCache;
	private final Cache<TranslateCodeRequest, Object> myTranslateCodeCache;
	private final Cache<String, Object> myLookupCodeCache;
	private final ThreadPoolExecutor myBackgroundExecutor;
	private final Map<Object, Object> myNonExpiringCache;
	private final Cache<String, Object> myExpandValueSetCache;

	/**
	 * Constuctor with default timeouts
	 *
	 * @param theWrap The validation support module to wrap
	 */
	public CachingValidationSupport(IValidationSupport theWrap) {
		this(theWrap, CacheTimeouts.defaultValues());
	}

	/**
	 * Constructor with configurable timeouts
	 *
	 * @param theWrap          The validation support module to wrap
	 * @param theCacheTimeouts The timeouts to use
	 */
	public CachingValidationSupport(IValidationSupport theWrap, CacheTimeouts theCacheTimeouts) {
		super(theWrap.getFhirContext(), theWrap);
		myExpandValueSetCache = Caffeine
			.newBuilder()
			.expireAfterWrite(theCacheTimeouts.getExpandValueSetMillis(), TimeUnit.MILLISECONDS)
			.maximumSize(100)
			.build();
		myValidateCodeCache = Caffeine
			.newBuilder()
			.expireAfterWrite(theCacheTimeouts.getValidateCodeMillis(), TimeUnit.MILLISECONDS)
			.maximumSize(5000)
			.build();
		myLookupCodeCache = Caffeine
			.newBuilder()
			.expireAfterWrite(theCacheTimeouts.getLookupCodeMillis(), TimeUnit.MILLISECONDS)
			.maximumSize(5000)
			.build();
		myTranslateCodeCache = Caffeine
			.newBuilder()
			.expireAfterWrite(theCacheTimeouts.getTranslateCodeMillis(), TimeUnit.MILLISECONDS)
			.maximumSize(5000)
			.build();
		myCache = Caffeine
			.newBuilder()
			.expireAfterWrite(theCacheTimeouts.getMiscMillis(), TimeUnit.MILLISECONDS)
			.maximumSize(5000)
			.build();
		myNonExpiringCache = Collections.synchronizedMap(new HashMap<>());

		LinkedBlockingQueue<Runnable> executorQueue = new LinkedBlockingQueue<>(1000);
		BasicThreadFactory threadFactory = new BasicThreadFactory.Builder()
			.namingPattern("CachingValidationSupport-%d")
			.daemon(false)
			.priority(Thread.NORM_PRIORITY)
			.build();
		myBackgroundExecutor = new ThreadPoolExecutor(
			1,
			1,
			0L,
			TimeUnit.MILLISECONDS,
			executorQueue,
			threadFactory,
			new ThreadPoolExecutor.DiscardPolicy());

	}

	@Override
	public List<IBaseResource> fetchAllConformanceResources() {
		String key = "fetchAllConformanceResources";
		return loadFromCacheWithAsyncRefresh(myCache, key, t -> super.fetchAllConformanceResources());
	}

	@Override
	public <T extends IBaseResource> List<T> fetchAllStructureDefinitions() {
		String key = "fetchAllStructureDefinitions";
		return loadFromCacheWithAsyncRefresh(myCache, key, t -> super.fetchAllStructureDefinitions());
	}

	@Override
	public <T extends IBaseResource> List<T> fetchAllNonBaseStructureDefinitions() {
		String key = "fetchAllNonBaseStructureDefinitions";
		return loadFromCacheWithAsyncRefresh(myCache, key, t -> super.fetchAllNonBaseStructureDefinitions());
	}

	@Override
	public IBaseResource fetchCodeSystem(String theSystem) {
		return loadFromCache(myCache, "fetchCodeSystem " + theSystem, t -> super.fetchCodeSystem(theSystem));
	}

	@Override
	public IBaseResource fetchValueSet(String theUri) {
		return loadFromCache(myCache, "fetchValueSet " + theUri, t -> super.fetchValueSet(theUri));
	}

	@Override
	public IBaseResource fetchStructureDefinition(String theUrl) {
		return loadFromCache(myCache, "fetchStructureDefinition " + theUrl, t -> super.fetchStructureDefinition(theUrl));
	}

	@Override
	public <T extends IBaseResource> T fetchResource(@Nullable Class<T> theClass, String theUri) {
		return loadFromCache(myCache, "fetchResource " + theClass + " " + theUri,
			t -> super.fetchResource(theClass, theUri));
	}

	@Override
	public boolean isCodeSystemSupported(ValidationSupportContext theValidationSupportContext, String theSystem) {
		String key = "isCodeSystemSupported " + theSystem;
		Boolean retVal = loadFromCacheReentrantSafe(myCache, key, t -> super.isCodeSystemSupported(theValidationSupportContext, theSystem));
		assert retVal != null;
		return retVal;
	}

	@Override
	public ValueSetExpansionOutcome expandValueSet(ValidationSupportContext theValidationSupportContext, ValueSetExpansionOptions theExpansionOptions, @Nonnull IBaseResource theValueSetToExpand) {
		if (!theValueSetToExpand.getIdElement().hasIdPart()) {
			return super.expandValueSet(theValidationSupportContext, theExpansionOptions, theValueSetToExpand);
		}

		ValueSetExpansionOptions expansionOptions = defaultIfNull(theExpansionOptions, EMPTY_EXPANSION_OPTIONS);
		String key = "expandValueSet " +
			theValueSetToExpand.getIdElement().getValue() + " " +
			expansionOptions.isIncludeHierarchy() + " " +
			expansionOptions.getFilter() + " " +
			expansionOptions.getOffset() + " " +
			expansionOptions.getCount();
		return loadFromCache(myExpandValueSetCache, key, t -> super.expandValueSet(theValidationSupportContext, theExpansionOptions, theValueSetToExpand));
	}

	@Override
	public CodeValidationResult validateCode(@Nonnull ValidationSupportContext theValidationSupportContext, @Nonnull ConceptValidationOptions theOptions, String theCodeSystem, String theCode, String theDisplay, String theValueSetUrl) {
		String key = "validateCode " + theCodeSystem + " " + theCode + " " + defaultIfBlank(theValueSetUrl, "NO_VS");
		return loadFromCache(myValidateCodeCache, key, t -> super.validateCode(theValidationSupportContext, theOptions, theCodeSystem, theCode, theDisplay, theValueSetUrl));
	}

	@Override
	public LookupCodeResult lookupCode(ValidationSupportContext theValidationSupportContext, String theSystem, String theCode, String theDisplayLanguage) {
		String key = "lookupCode " + theSystem + " " + theCode + " " + defaultIfBlank(theDisplayLanguage, "NO_LANG");
		return loadFromCache(myLookupCodeCache, key, t -> super.lookupCode(theValidationSupportContext, theSystem, theCode, theDisplayLanguage));
	}

	@Override
	public IValidationSupport.CodeValidationResult validateCodeInValueSet(ValidationSupportContext theValidationSupportContext, ConceptValidationOptions theValidationOptions, String theCodeSystem, String theCode, String theDisplay, @Nonnull IBaseResource theValueSet) {

		BaseRuntimeChildDefinition urlChild = myCtx.getResourceDefinition(theValueSet).getChildByName("url");
		Optional<String> valueSetUrl = urlChild.getAccessor().getValues(theValueSet).stream().map(t -> ((IPrimitiveType<?>) t).getValueAsString()).filter(t -> isNotBlank(t)).findFirst();
		if (valueSetUrl.isPresent()) {
			String key = "validateCodeInValueSet " + theValidationOptions.toString() + " " + defaultString(theCodeSystem, "(null)") + " " + defaultString(theCode, "(null)") + " " + defaultString(theDisplay, "(null)") + " " + valueSetUrl.get();
			return loadFromCache(myValidateCodeCache, key, t -> super.validateCodeInValueSet(theValidationSupportContext, theValidationOptions, theCodeSystem, theCode, theDisplay, theValueSet));
		}

		return super.validateCodeInValueSet(theValidationSupportContext, theValidationOptions, theCodeSystem, theCode, theDisplay, theValueSet);
	}

	@Override
	public TranslateConceptResults translateConcept(TranslateCodeRequest theRequest) {
		return loadFromCache(myTranslateCodeCache, theRequest, k -> super.translateConcept(theRequest));
	}

	@SuppressWarnings("OptionalAssignedToNull")
	@Nullable
	private <S, T> T loadFromCache(Cache<S, Object> theCache, S theKey, Function<S, T> theLoader) {
		ourLog.trace("Fetching from cache: {}", theKey);

		Function<S, Optional<T>> loaderWrapper = key -> Optional.ofNullable(theLoader.apply(theKey));
		Optional<T> result = (Optional<T>) theCache.get(theKey, loaderWrapper);
		assert result != null;

		return result.orElse(null);
	}

	/**
	 * The Caffeine cache uses ConcurrentHashMap which is not reentrant, so if we get unlucky and the hashtable
	 * needs to grow at the same time as we are in a reentrant cache lookup, the thread will deadlock.  Use this
	 * method in place of loadFromCache in situations where a cache lookup calls another cache lookup within its lambda
	 */
	@Nullable
	private <S, T> T loadFromCacheReentrantSafe(Cache<S, Object> theCache, S theKey, Function<S, T> theLoader) {
		ourLog.trace("Reentrant fetch from cache: {}", theKey);

		Optional<T> result = (Optional<T>) theCache.getIfPresent(theKey);
		if (result != null && result.isPresent()) {
			return result.get();
		}
		T value = theLoader.apply(theKey);
		assert value != null;

		theCache.put(theKey, Optional.of(value));

		return value;
	}

	private <S, T> T loadFromCacheWithAsyncRefresh(Cache<S, Object> theCache, S theKey, Function<S, T> theLoader) {
		T retVal = (T) theCache.getIfPresent(theKey);
		if (retVal == null) {
			retVal = (T) myNonExpiringCache.get(theKey);
			if (retVal != null) {

				Runnable loaderTask = () -> {
					T loadedItem = loadFromCache(theCache, theKey, theLoader);
					myNonExpiringCache.put(theKey, loadedItem);
				};
				myBackgroundExecutor.execute(loaderTask);

				return retVal;
			}
		}

		retVal = loadFromCache(theCache, theKey, theLoader);
		myNonExpiringCache.put(theKey, retVal);
		return retVal;
	}


	@Override
	public void invalidateCaches() {
		myExpandValueSetCache.invalidateAll();
		myLookupCodeCache.invalidateAll();
		myCache.invalidateAll();
		myValidateCodeCache.invalidateAll();
		myNonExpiringCache.clear();
	}

	/**
	 * @since 5.4.0
	 */
	public static class CacheTimeouts {

		private long myTranslateCodeMillis;
		private long myLookupCodeMillis;
		private long myValidateCodeMillis;
		private long myMiscMillis;
		private long myExpandValueSetMillis;

		public long getExpandValueSetMillis() {
			return myExpandValueSetMillis;
		}

		public CacheTimeouts setExpandValueSetMillis(long theExpandValueSetMillis) {
			myExpandValueSetMillis = theExpandValueSetMillis;
			return this;
		}

		public long getTranslateCodeMillis() {
			return myTranslateCodeMillis;
		}

		public CacheTimeouts setTranslateCodeMillis(long theTranslateCodeMillis) {
			myTranslateCodeMillis = theTranslateCodeMillis;
			return this;
		}

		public long getLookupCodeMillis() {
			return myLookupCodeMillis;
		}

		public CacheTimeouts setLookupCodeMillis(long theLookupCodeMillis) {
			myLookupCodeMillis = theLookupCodeMillis;
			return this;
		}

		public long getValidateCodeMillis() {
			return myValidateCodeMillis;
		}

		public CacheTimeouts setValidateCodeMillis(long theValidateCodeMillis) {
			myValidateCodeMillis = theValidateCodeMillis;
			return this;
		}

		public long getMiscMillis() {
			return myMiscMillis;
		}

		public CacheTimeouts setMiscMillis(long theMiscMillis) {
			myMiscMillis = theMiscMillis;
			return this;
		}

		public static CacheTimeouts defaultValues() {
			return new CacheTimeouts()
				.setLookupCodeMillis(10 * DateUtils.MILLIS_PER_MINUTE)
				.setExpandValueSetMillis(1 * DateUtils.MILLIS_PER_MINUTE)
				.setTranslateCodeMillis(10 * DateUtils.MILLIS_PER_MINUTE)
				.setValidateCodeMillis(10 * DateUtils.MILLIS_PER_MINUTE)
				.setMiscMillis(10 * DateUtils.MILLIS_PER_MINUTE);
		}
	}
}
