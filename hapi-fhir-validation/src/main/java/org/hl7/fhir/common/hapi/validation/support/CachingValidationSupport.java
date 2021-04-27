package org.hl7.fhir.common.hapi.validation.support;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.TranslateConceptResults;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.apache.commons.lang3.time.DateUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static org.apache.commons.lang3.StringUtils.defaultIfBlank;
import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@SuppressWarnings("unchecked")
public class CachingValidationSupport extends BaseValidationSupportWrapper implements IValidationSupport {

	private static final Logger ourLog = LoggerFactory.getLogger(CachingValidationSupport.class);
	private final Cache<String, Object> myCache;
	private final Cache<String, Object> myValidateCodeCache;
	private final Cache<TranslateCodeRequest, Object> myTranslateCodeCache;
	private final Cache<String, Object> myLookupCodeCache;

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
	}

	@Override
	public List<IBaseResource> fetchAllConformanceResources() {
		String key = "fetchAllConformanceResources";
		return loadFromCache(myCache, key, t -> super.fetchAllConformanceResources());
	}

	@Override
	public <T extends IBaseResource> List<T> fetchAllStructureDefinitions() {
		String key = "fetchAllStructureDefinitions";
		return loadFromCache(myCache, key, t -> super.fetchAllStructureDefinitions());
	}

	@Override
	public <T extends IBaseResource> List<T> fetchAllNonBaseStructureDefinitions() {
		String key = "fetchAllNonBaseStructureDefinitions";
		return loadFromCache(myCache, key, t -> super.fetchAllNonBaseStructureDefinitions());
	}

	@Override
	public <T extends IBaseResource> T fetchResource(@Nullable Class<T> theClass, String theUri) {
		return loadFromCache(myCache, "fetchResource " + theClass + " " + theUri,
			t -> super.fetchResource(theClass, theUri));
	}

	@Override
	public boolean isCodeSystemSupported(ValidationSupportContext theValidationSupportContext, String theSystem) {
		String key = "isCodeSystemSupported " + theSystem;
		Boolean retVal = loadFromCache(myCache, key, t -> super.isCodeSystemSupported(theValidationSupportContext, theSystem));
		assert retVal != null;
		return retVal;
	}

	@Override
	public CodeValidationResult validateCode(ValidationSupportContext theValidationSupportContext, ConceptValidationOptions theOptions, String theCodeSystem, String theCode, String theDisplay, String theValueSetUrl) {
		String key = "validateCode " + theCodeSystem + " " + theCode + " " + defaultIfBlank(theValueSetUrl, "NO_VS");
		return loadFromCache(myValidateCodeCache, key, t -> super.validateCode(theValidationSupportContext, theOptions, theCodeSystem, theCode, theDisplay, theValueSetUrl));
	}

	@Override
	public LookupCodeResult lookupCode(ValidationSupportContext theValidationSupportContext, String theSystem, String theCode) {
		String key = "lookupCode " + theSystem + " " + theCode;
		return loadFromCache(myLookupCodeCache, key, t -> super.lookupCode(theValidationSupportContext, theSystem, theCode));
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

	@Override
	public void invalidateCaches() {
		myLookupCodeCache.invalidateAll();
		myCache.invalidateAll();
		myValidateCodeCache.invalidateAll();
	}

	/**
	 * @since 5.4.0
	 */
	public static class CacheTimeouts {

		private long myTranslateCodeMillis;
		private long myLookupCodeMillis;
		private long myValidateCodeMillis;
		private long myMiscMillis;

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
				.setTranslateCodeMillis(10 * DateUtils.MILLIS_PER_MINUTE)
				.setValidateCodeMillis(10 * DateUtils.MILLIS_PER_MINUTE)
				.setMiscMillis(10 * DateUtils.MILLIS_PER_MINUTE);
		}
	}
}
