package org.hl7.fhir.dstu2016may.hapi.validation;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.dstu2016may.model.CodeSystem;
import org.hl7.fhir.dstu2016may.model.StructureDefinition;
import org.hl7.fhir.dstu2016may.model.ValueSet;
import org.hl7.fhir.dstu2016may.model.ValueSet.ConceptSetComponent;
import org.hl7.fhir.dstu2016may.model.ValueSet.ValueSetExpansionComponent;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class ValidationSupportChain implements IValidationSupport {

	private List<IValidationSupport> myChain;

	/**
	 * Constructor
	 */
	public ValidationSupportChain() {
		myChain = new ArrayList<IValidationSupport>();
	}

	/**
	 * Constructor
	 */
	public ValidationSupportChain(IValidationSupport... theValidationSupportModules) {
		this();
		for (IValidationSupport next : theValidationSupportModules) {
			if (next != null) {
				myChain.add(next);
			}
		}
	}

	public void addValidationSupport(IValidationSupport theValidationSupport) {
		myChain.add(theValidationSupport);
	}

	@Override
	public ValueSetExpansionComponent expandValueSet(FhirContext theCtx, ConceptSetComponent theInclude) {
		for (IValidationSupport next : myChain) {
			if (next.isCodeSystemSupported(theCtx, theInclude.getSystem())) {
				return next.expandValueSet(theCtx, theInclude);
			}
		}
		return myChain.get(0).expandValueSet(theCtx, theInclude);
	}

	@Override
	public List<IBaseResource> fetchAllConformanceResources(FhirContext theContext) {
		List<IBaseResource> retVal = new ArrayList<>();
		for (IValidationSupport next : myChain) {
			List<IBaseResource> candidates = next.fetchAllConformanceResources(theContext);
			if (candidates != null) {
				retVal.addAll(candidates);
			}
		}
		return retVal;
	}

	@Override
	public List<StructureDefinition> fetchAllStructureDefinitions(FhirContext theContext) {
		ArrayList<StructureDefinition> retVal = new ArrayList<StructureDefinition>();
		Set<String> urls = new HashSet<String>();
		for (IValidationSupport nextSupport : myChain) {
			for (StructureDefinition next : nextSupport.fetchAllStructureDefinitions(theContext)) {
				if (isBlank(next.getUrl()) || urls.add(next.getUrl())) {
					retVal.add(next);
				}
			}
		}
		return retVal;
	}

	@Override
	public CodeSystem fetchCodeSystem(FhirContext theCtx, String uri) {
		for (IValidationSupport next : myChain) {
			CodeSystem retVal = next.fetchCodeSystem(theCtx, uri);
			if (retVal != null) {
				return retVal;
			}
		}
		return null;
	}

	@Override
	public ValueSet fetchValueSet(FhirContext theCtx, String uri) {
		for (IValidationSupport next : myChain) {
			ValueSet retVal = next.fetchValueSet(theCtx, uri);
			if (retVal != null) {
				return retVal;
			}
		}
		return null;
	}


	@Override
	public <T extends IBaseResource> T fetchResource(FhirContext theContext, Class<T> theClass, String theUri) {
		for (IValidationSupport next : myChain) {
			T retVal = next.fetchResource(theContext, theClass, theUri);
			if (retVal != null) {
				return retVal;
			}
		}
		return null;
	}

	@Override
	public StructureDefinition fetchStructureDefinition(FhirContext theCtx, String theUrl) {
		for (IValidationSupport next : myChain) {
			StructureDefinition retVal = next.fetchStructureDefinition(theCtx, theUrl);
			if (retVal != null) {
				return retVal;
			}
		}
		return null;
	}

	@Override
	public boolean isCodeSystemSupported(FhirContext theCtx, String theSystem) {
		for (IValidationSupport next : myChain) {
			if (next.isCodeSystemSupported(theCtx, theSystem)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public CodeValidationResult validateCode(FhirContext theCtx, String theCodeSystem, String theCode, String theDisplay) {
		for (IValidationSupport next : myChain) {
			if (next.isCodeSystemSupported(theCtx, theCodeSystem)) {
				return next.validateCode(theCtx, theCodeSystem, theCode, theDisplay);
			}
		}
		return myChain.get(0).validateCode(theCtx, theCodeSystem, theCode, theDisplay);
	}

}
