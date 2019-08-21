package org.hl7.fhir.r4.hapi.validation;

import ca.uhn.fhir.context.FhirContext;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.hapi.ctx.IValidationSupport;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.r4.model.ValueSet;
import org.hl7.fhir.r4.terminologies.ValueSetExpander;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@SuppressWarnings("unchecked")
public class CachingValidationSupport implements IValidationSupport {

	private final IValidationSupport myWrap;
	private final Cache<String, Object> myCache;

	public CachingValidationSupport(IValidationSupport theWrap) {
		myWrap = theWrap;
		myCache = Caffeine
			.newBuilder()
			.expireAfterWrite(60, TimeUnit.SECONDS)
			.maximumSize(5000)
			.build();
	}

	@Override
	public ValueSetExpander.ValueSetExpansionOutcome expandValueSet(FhirContext theContext, ValueSet.ConceptSetComponent theInclude) {
		return myWrap.expandValueSet(theContext, theInclude);
	}

	@Override
	public List<IBaseResource> fetchAllConformanceResources(FhirContext theContext) {
		String key = "fetchAllConformanceResources";
		return loadFromCache(key, t -> myWrap.fetchAllConformanceResources(theContext));
	}

	@Override
	public List<StructureDefinition> fetchAllStructureDefinitions(FhirContext theContext) {
		String key = "fetchAllStructureDefinitions";
		return loadFromCache(key, t -> myWrap.fetchAllStructureDefinitions(theContext));
	}

	@Override
	public CodeSystem fetchCodeSystem(FhirContext theContext, String uri) {
		return myWrap.fetchCodeSystem(theContext, uri);
	}

	@Override
	public ValueSet fetchValueSet(FhirContext theContext, String uri) {
		return myWrap.fetchValueSet(theContext, uri);
	}

	@Override
	public <T extends IBaseResource> T fetchResource(FhirContext theContext, Class<T> theClass, String theUri) {
		return loadFromCache("fetchResource " + theClass.getName() + " " + theUri,
			t -> myWrap.fetchResource(theContext, theClass, theUri));
	}

	@Override
	public StructureDefinition fetchStructureDefinition(FhirContext theCtx, String theUrl) {
		return myWrap.fetchStructureDefinition(theCtx, theUrl);
	}

	@Override
	public boolean isCodeSystemSupported(FhirContext theContext, String theSystem) {
		String key = "isCodeSystemSupported " + theSystem;
		return loadFromCache(key, t -> myWrap.isCodeSystemSupported(theContext, theSystem));
	}

	@Override
	public StructureDefinition generateSnapshot(StructureDefinition theInput, String theUrl, String theWebUrl, String theProfileName) {
		return myWrap.generateSnapshot(theInput, theUrl, theWebUrl, theProfileName);
	}

	@Override
	public CodeValidationResult validateCode(FhirContext theContext, String theCodeSystem, String theCode, String theDisplay) {
		String key = "validateCode " + theCodeSystem + " " + theCode;
		return loadFromCache(key, t -> myWrap.validateCode(theContext, theCodeSystem, theCode, theDisplay));
	}

	@Override
	public LookupCodeResult lookupCode(FhirContext theContext, String theSystem, String theCode) {
		String key = "lookupCode " + theSystem + " " + theCode;
		return loadFromCache(key, t -> myWrap.lookupCode(theContext, theSystem, theCode));
	}

	@Nullable
	private <T> T loadFromCache(String theKey, Function<String, T> theLoader) {
		Function<String, Optional<T>> loaderWrapper = key -> Optional.ofNullable(theLoader.apply(theKey));
		Optional<T> result = (Optional<T>) myCache.get(theKey, loaderWrapper);
		return result.orElse(null);
	}

	public void flushCaches() {
		myCache.invalidateAll();
	}
}
