package org.hl7.fhir.common.hapi.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IContextValidationSupport;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class ValidationSupportChain implements IContextValidationSupport {

	private List<IContextValidationSupport> myChain;

	/**
	 * Constructor
	 */
	public ValidationSupportChain() {
		myChain = new ArrayList<>();
	}

	/**
	 * Constructor
	 */
	public ValidationSupportChain(IContextValidationSupport... theValidationSupportModules) {
		this();
		for (IContextValidationSupport next : theValidationSupportModules) {
			if (next != null) {
				myChain.add(next);
			}
		}
	}

	@Override
	public CodeValidationResult validateCodeInValueSet(FhirContext theContext, String theCodeSystem, String theCode, String theDisplay, @Nonnull IBaseResource theValueSet) {
		for (IContextValidationSupport next : myChain) {
			CodeValidationResult retVal = next.validateCodeInValueSet(theContext, theCodeSystem, theCode, theDisplay, theValueSet);
			if (retVal != null) {
				return retVal;
			}
		}
		return null;
	}

	@Override
	public boolean isValueSetSupported(FhirContext theContext, String theValueSetUrl) {
		for (IContextValidationSupport next : myChain) {
			boolean retVal = next.isValueSetSupported(theContext, theValueSetUrl);
			if (retVal) {
				return true;
			}
		}
		return false;
	}

	@Override
	public IBaseResource generateSnapshot(IContextValidationSupport theRootValidationSupport, IBaseResource theInput, String theUrl, String theWebUrl, String theProfileName) {
		for (IContextValidationSupport next : myChain) {
			IBaseResource retVal = next.generateSnapshot(theRootValidationSupport, theInput, theUrl, theWebUrl, theProfileName);
			if (retVal != null) {
				return retVal;
			}
		}
		return null;
	}

	public void addValidationSupport(IContextValidationSupport theValidationSupport) {
		myChain.add(theValidationSupport);
	}

	@Override
	public ValueSetExpansionOutcome expandValueSet(IContextValidationSupport theRootValidationSupport, FhirContext theContext, IBaseResource theInclude) {
		for (IContextValidationSupport next : myChain) {
			// TODO: test if code system is supported?
			ValueSetExpansionOutcome expanded = next.expandValueSet(theRootValidationSupport, theContext, theInclude);
			if (expanded != null) {
				return expanded;
			}
		}
		return null;
	}

	@Override
	public List<IBaseResource> fetchAllConformanceResources(FhirContext theContext) {
		List<IBaseResource> retVal = new ArrayList<>();
		for (IContextValidationSupport next : myChain) {
			List<IBaseResource> candidates = next.fetchAllConformanceResources(theContext);
			if (candidates != null) {
				retVal.addAll(candidates);
			}
		}
		return retVal;
	}

	@Override
	public <T extends IBaseResource> List<T> fetchAllStructureDefinitions(FhirContext theContext, Class<T> theStructureDefinitionType) {
		ArrayList<T> retVal = new ArrayList<>();
		Set<String> urls = new HashSet<>();
		for (IContextValidationSupport nextSupport : myChain) {
			for (IBaseResource next : nextSupport.fetchAllStructureDefinitions(theContext, theStructureDefinitionType)) {

				IPrimitiveType<?> urlType = theContext.newTerser().getSingleValueOrNull(next, "url", IPrimitiveType.class);
				if (urlType == null || isBlank(urlType.getValueAsString()) || urls.add(urlType.getValueAsString())) {
					retVal.add(theStructureDefinitionType.cast(next));
				}
			}
		}
		return retVal;
	}

	@Override
	public <T extends IBaseResource> T fetchCodeSystem(FhirContext theContext, String theSystem, Class<T> theCodeSystemType) {
		for (IContextValidationSupport next : myChain) {
			T retVal = next.fetchCodeSystem(theContext, theSystem, theCodeSystemType);
			if (retVal != null) {
				return retVal;
			}
		}
		return null;
	}

	@Override
	public IBaseResource fetchValueSet(FhirContext theCtx, String theUrl) {
		for (IContextValidationSupport next : myChain) {
			IBaseResource retVal = next.fetchValueSet(theCtx, theUrl);
			if (retVal != null) {
				return retVal;
			}
		}
		return null;
	}


	@Override
	public <T extends IBaseResource> T fetchResource(FhirContext theContext, Class<T> theClass, String theUri) {
		for (IContextValidationSupport next : myChain) {
			T retVal = next.fetchResource(theContext, theClass, theUri);
			if (retVal != null) {
				return retVal;
			}
		}
		return null;
	}

	@Override
	public IBaseResource fetchStructureDefinition(FhirContext theCtx, String theUrl) {
		for (IContextValidationSupport next : myChain) {
			IBaseResource retVal = next.fetchStructureDefinition(theCtx, theUrl);
			if (retVal != null) {
				return retVal;
			}
		}
		return null;
	}

	@Override
	public boolean isCodeSystemSupported(FhirContext theCtx, String theSystem) {
		for (IContextValidationSupport next : myChain) {
			if (next.isCodeSystemSupported(theCtx, theSystem)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public CodeValidationResult validateCode(IContextValidationSupport theRootValidationSupport, FhirContext theCtx, String theCodeSystem, String theCode, String theDisplay, String theValueSetUrl) {
		for (IContextValidationSupport next : myChain) {
			if (theCodeSystem != null && next.isCodeSystemSupported(theCtx, theCodeSystem)) {
				return next.validateCode(theRootValidationSupport, theCtx, theCodeSystem, theCode, theDisplay, theValueSetUrl);
			}
		}
		return myChain.get(0).validateCode(theRootValidationSupport, theCtx, theCodeSystem, theCode, theDisplay, theValueSetUrl);
	}

	@Override
	public LookupCodeResult lookupCode(IContextValidationSupport theRootValidationSupport, FhirContext theContext, String theSystem, String theCode) {
		for (IContextValidationSupport next : myChain) {
			if (next.isCodeSystemSupported(theContext, theSystem)) {
				return next.lookupCode(theRootValidationSupport, theContext, theSystem, theCode);
			}
		}
		return null;
	}


}
