package org.hl7.fhir.dstu3.hapi.validation;

import ca.uhn.fhir.context.FhirContext;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.hl7.fhir.dstu3.hapi.ctx.IValidationSupport;
import org.hl7.fhir.dstu3.model.CodeSystem;
import org.hl7.fhir.dstu3.model.StructureDefinition;
import org.hl7.fhir.dstu3.model.ValueSet;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@SuppressWarnings("unchecked")
public class CachingValidationSupport implements IValidationSupport {

	private static final Logger ourLog = LoggerFactory.getLogger(CachingValidationSupport.class);
	private final IValidationSupport myWrap;
	private final Cache<String, Object> myCache;

	public CachingValidationSupport(IValidationSupport theWrap) {
		myWrap = theWrap;
		myCache = Caffeine.newBuilder().expireAfterWrite(60, TimeUnit.SECONDS).build();
	}

	@Override
	public ValueSet.ValueSetExpansionComponent expandValueSet(FhirContext theContext, ValueSet.ConceptSetComponent theInclude) {
		return myWrap.expandValueSet(theContext, theInclude);
	}

	@Override
	public List<IBaseResource> fetchAllConformanceResources(FhirContext theContext) {
		return loadFromCache("fetchAllConformanceResources",
			t -> myWrap.fetchAllConformanceResources(theContext));
	}

	@Override
	public List<StructureDefinition> fetchAllStructureDefinitions(FhirContext theContext) {
		return loadFromCache("fetchAllStructureDefinitions",
			t -> myWrap.fetchAllStructureDefinitions(theContext));
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
		return myWrap.isCodeSystemSupported(theContext, theSystem);
	}

	@Override
	public CodeValidationResult validateCode(FhirContext theContext, String theCodeSystem, String theCode, String theDisplay) {
		return myWrap.validateCode(theContext, theCodeSystem, theCode, theDisplay);
	}

	@Override
	public LookupCodeResult lookupCode(FhirContext theContext, String theSystem, String theCode) {
		return myWrap.lookupCode(theContext, theSystem, theCode);
	}

	@Override
	public StructureDefinition generateSnapshot(StructureDefinition theInput, String theUrl, String theName) {
		return myWrap.generateSnapshot(theInput, theUrl, theName);
	}

	@Nullable
	private <T> T loadFromCache(String theKey, Function<String, T> theLoader) {
		ourLog.trace("Loading: {}", theKey);
		Function<String, Optional<T>> loaderWrapper = key -> {
			ourLog.trace("Loading {} from cache", theKey);
			return Optional.ofNullable(theLoader.apply(theKey));
		};
		Optional<T> result = (Optional<T>) myCache.get(theKey, loaderWrapper);
		return result.orElse(null);
	}

	public void flushCaches() {
		myCache.invalidateAll();
	}
}
