package org.hl7.fhir.instance.hapi.validation;

import ca.uhn.fhir.context.FhirContext;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.hl7.fhir.instance.model.StructureDefinition;
import org.hl7.fhir.instance.model.ValueSet;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.List;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unchecked")
public class CachingValidationSupport implements IValidationSupport {

	private final IValidationSupport myWrap;
	private final Cache<String, Object> myCache;

	public CachingValidationSupport(IValidationSupport theWrap) {
		myWrap = theWrap;
		myCache = Caffeine.newBuilder().expireAfterWrite(60, TimeUnit.SECONDS).build();
	}


	@Override
	public List<StructureDefinition> allStructures() {
		return (List<StructureDefinition>) myCache.get("fetchAllStructureDefinitions",
			t -> myWrap.allStructures());
	}

	@Override
	public ValueSet.ValueSetExpansionComponent expandValueSet(FhirContext theContext, ValueSet.ConceptSetComponent theInclude) {
		return myWrap.expandValueSet(theContext, theInclude);
	}

	@Override
	public ValueSet fetchCodeSystem(FhirContext theContext, String theSystem) {
		return myWrap.fetchCodeSystem(theContext, theSystem);
	}

	@Override
	public <T extends IBaseResource> T fetchResource(FhirContext theContext, Class<T> theClass, String theUri) {
		return myWrap.fetchResource(theContext, theClass, theUri);
	}

	@Override
	public boolean isCodeSystemSupported(FhirContext theContext, String theSystem) {
		return myWrap.isCodeSystemSupported(theContext, theSystem);
	}

	@Override
	public CodeValidationResult validateCode(FhirContext theContext, String theCodeSystem, String theCode, String theDisplay) {
		return myWrap.validateCode(theContext, theCodeSystem, theCode, theDisplay);
	}

}
