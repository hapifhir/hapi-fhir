package org.hl7.fhir.common.hapi.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IContextValidationSupport;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.hl7.fhir.instance.model.api.IBaseResource;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static org.apache.commons.lang3.StringUtils.defaultIfBlank;

@SuppressWarnings("unchecked")
public class CachingValidationSupport implements IContextValidationSupport {

	private final IContextValidationSupport myWrap;
	private final Cache<String, Object> myCache;

	public CachingValidationSupport(IContextValidationSupport theWrap) {
		myWrap = theWrap;
		myCache = Caffeine
			.newBuilder()
			.expireAfterWrite(60, TimeUnit.SECONDS)
			.maximumSize(5000)
			.build();
	}

	@Override
	public IContextValidationSupport.ValueSetExpansionOutcome expandValueSet(IContextValidationSupport theRootValidationSupport, FhirContext theContext, IBaseResource theInclude) {
		return myWrap.expandValueSet(theRootValidationSupport, theContext, theInclude);
	}

	@Override
	public List<IBaseResource> fetchAllConformanceResources(FhirContext theContext) {
		String key = "fetchAllConformanceResources";
		return loadFromCache(key, t -> myWrap.fetchAllConformanceResources(theContext));
	}

	@Override
	public<T extends IBaseResource> List<T> fetchAllStructureDefinitions(FhirContext theContext, Class<T> theStructureDefinitionType) {
		String key = "fetchAllStructureDefinitions";
		return loadFromCache(key, t -> myWrap.fetchAllStructureDefinitions(theContext, theStructureDefinitionType));
	}

	@Override
	public <T extends IBaseResource> T fetchCodeSystem(FhirContext theContext, String theSystem, Class<T> theCodeSystemType) {
		return myWrap.fetchCodeSystem(theContext, theSystem, theCodeSystemType);
	}

	@Override
	public IBaseResource fetchValueSet(FhirContext theContext, String theUri) {
		return myWrap.fetchValueSet(theContext, theUri);
	}

	@Override
	public <T extends IBaseResource> T fetchResource(FhirContext theContext, Class<T> theClass, String theUri) {
		return loadFromCache("fetchResource " + theClass.getName() + " " + theUri,
			t -> myWrap.fetchResource(theContext, theClass, theUri));
	}

	@Override
	public IBaseResource fetchStructureDefinition(FhirContext theCtx, String theUrl) {
		return myWrap.fetchStructureDefinition(theCtx, theUrl);
	}

	@Override
	public boolean isCodeSystemSupported(FhirContext theContext, String theSystem) {
		String key = "isCodeSystemSupported " + theSystem;
		Boolean retVal = loadFromCache(key, t -> myWrap.isCodeSystemSupported(theContext, theSystem));
		assert retVal != null;
		return retVal;
	}

	@Override
	public IBaseResource generateSnapshot(IContextValidationSupport theRootValidationSupport, IBaseResource theInput, String theUrl, String theWebUrl, String theProfileName) {
		return myWrap.generateSnapshot(theRootValidationSupport, theInput, theUrl, theWebUrl, theProfileName);
	}

	@Override
	public CodeValidationResult validateCode(IContextValidationSupport theRootValidationSupport, FhirContext theContext, String theCodeSystem, String theCode, String theDisplay, String theValueSetUrl) {
		String key = "validateCode " + theCodeSystem + " " + theCode + " " + defaultIfBlank(theValueSetUrl, "NO_VS");
		return loadFromCache(key, t -> myWrap.validateCode(theRootValidationSupport, theContext, theCodeSystem, theCode, theDisplay, theValueSetUrl));
	}

	@Override
	public CodeValidationResult validateCodeInValueSet(FhirContext theContext, String theCodeSystem, String theCode, String theDisplay, @Nonnull IBaseResource theValueSet) {
		return myWrap.validateCodeInValueSet(theContext, theCodeSystem, theCode, theDisplay, theValueSet);
	}

	@Override
	public LookupCodeResult lookupCode(IContextValidationSupport theRootValidationSupport, FhirContext theContext, String theSystem, String theCode) {
		String key = "lookupCode " + theSystem + " " + theCode;
		return loadFromCache(key, t -> myWrap.lookupCode(theRootValidationSupport, theContext, theSystem, theCode));
	}

	@SuppressWarnings("OptionalAssignedToNull")
	@Nullable
	private <T> T loadFromCache(String theKey, Function<String, T> theLoader) {
		Function<String, Optional<T>> loaderWrapper = key -> Optional.ofNullable(theLoader.apply(theKey));
		Optional<T> result = (Optional<T>) myCache.get(theKey, loaderWrapper);
		assert result != null;
		return result.orElse(null);
	}

	public void flushCaches() {
		myCache.invalidateAll();
	}
}
