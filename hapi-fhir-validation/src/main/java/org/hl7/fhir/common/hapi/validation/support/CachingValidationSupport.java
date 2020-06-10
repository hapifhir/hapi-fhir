package org.hl7.fhir.common.hapi.validation.support;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
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
	private final Cache<String, Object> myLookupCodeCache;


	public CachingValidationSupport(IValidationSupport theWrap) {
		super(theWrap.getFhirContext(), theWrap);
		myValidateCodeCache = Caffeine
			.newBuilder()
			.expireAfterWrite(10, TimeUnit.MINUTES)
			.maximumSize(5000)
			.build();
		myLookupCodeCache = Caffeine
			.newBuilder()
			.expireAfterWrite(10, TimeUnit.MINUTES)
			.maximumSize(5000)
			.build();
		myCache = Caffeine
			.newBuilder()
			.expireAfterWrite(10, TimeUnit.MINUTES)
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
	public <T extends IBaseResource> T fetchResource(Class<T> theClass, String theUri) {
		return loadFromCache(myCache, "fetchResource " + theClass.getName() + " " + theUri,
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

	@SuppressWarnings("OptionalAssignedToNull")
	@Nullable
	private <T> T loadFromCache(Cache theCache, String theKey, Function<String, T> theLoader) {
		ourLog.trace("Fetching from cache: {}", theKey);

		Function<String, Optional<T>> loaderWrapper = key -> Optional.ofNullable(theLoader.apply(theKey));
		Optional<T> result = (Optional<T>) theCache.get(theKey, loaderWrapper);
		assert result != null;

		return result.orElse(null);

	}

	@Override
	public IValidationSupport.CodeValidationResult validateCodeInValueSet(ValidationSupportContext theValidationSupportContext, ConceptValidationOptions theValidationOptions, String theCodeSystem, String theCode, String theDisplay, @Nonnull IBaseResource theValueSet) {

		BaseRuntimeChildDefinition urlChild = myCtx.getResourceDefinition(theValueSet).getChildByName("url");
		Optional<String> valueSetUrl = urlChild.getAccessor().getValues(theValueSet).stream().map(t -> ((IPrimitiveType<?>) t).getValueAsString()).filter(t->isNotBlank(t)).findFirst();
		if (valueSetUrl.isPresent()) {
			String key = "validateCodeInValueSet " + theValidationOptions.toString() + " " + defaultString(theCodeSystem, "(null)") + " " + defaultString(theCode, "(null)") + " " + defaultString(theDisplay, "(null)") + " " + valueSetUrl.get();
			return loadFromCache(myValidateCodeCache, key, t-> super.validateCodeInValueSet(theValidationSupportContext, theValidationOptions, theCodeSystem, theCode, theDisplay, theValueSet));
		}

		return super.validateCodeInValueSet(theValidationSupportContext, theValidationOptions, theCodeSystem, theCode, theDisplay, theValueSet);
	}

	@Override
	public void invalidateCaches() {
		myLookupCodeCache.invalidateAll();
		myCache.invalidateAll();
		myValidateCodeCache.invalidateAll();
	}
}
