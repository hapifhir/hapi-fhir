package org.hl7.fhir.common.hapi.validation;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.IContextValidationSupport;
import ca.uhn.fhir.context.support.ValueSetExpansionOptions;
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
				addValidationSupport(next);
			}
		}
	}

	@Override
	public void invalidateCaches() {
		for (IContextValidationSupport next : myChain) {
			next.invalidateCaches();
		}
	}

	@Override
	public CodeValidationResult validateCodeInValueSet(IContextValidationSupport theRootValidationSupport, ConceptValidationOptions theOptions, String theCodeSystem, String theCode, String theDisplay, @Nonnull IBaseResource theValueSet) {
		for (IContextValidationSupport next : myChain) {
			CodeValidationResult retVal = next.validateCodeInValueSet(theRootValidationSupport, theOptions, theCodeSystem, theCode, theDisplay, theValueSet);
			if (retVal != null) {
				return retVal;
			}
		}
		return null;
	}

	@Override
	public boolean isValueSetSupported(IContextValidationSupport theRootValidationSupport, String theValueSetUrl) {
		for (IContextValidationSupport next : myChain) {
			boolean retVal = next.isValueSetSupported(theRootValidationSupport, theValueSetUrl);
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

	@Override
	public FhirContext getFhirContext() {
		if (myChain.size() == 0){
			return null;
		}
		return myChain.get(0).getFhirContext();
	}

	public void addValidationSupport(IContextValidationSupport theValidationSupport) {
		if (theValidationSupport.getFhirContext() == null) {
			String message = "Can not add validation support: getFhirContext() returns null";
			throw new ConfigurationException(message);
		}

		FhirContext existingFhirContext = getFhirContext();
		if (existingFhirContext != null) {
			FhirVersionEnum newVersion = theValidationSupport.getFhirContext().getVersion().getVersion();
			FhirVersionEnum existingVersion = existingFhirContext.getVersion().getVersion();
			if (!existingVersion.equals(newVersion)) {
				String message = "Trying to add validation support of version " + newVersion + " to chain with " + myChain.size() + " entries of version " + existingVersion;
				throw new ConfigurationException(message);
			}
		}
		myChain.add(theValidationSupport);
	}

	@Override
	public ValueSetExpansionOutcome expandValueSet(IContextValidationSupport theRootValidationSupport, ValueSetExpansionOptions theExpansionOptions, IBaseResource theValueSetToExpand) {
		for (IContextValidationSupport next : myChain) {
			// TODO: test if code system is supported?
			ValueSetExpansionOutcome expanded = next.expandValueSet(theRootValidationSupport, null, theValueSetToExpand);
			if (expanded != null) {
				return expanded;
			}
		}
		return null;
	}

	@Override
	public List<IBaseResource> fetchAllConformanceResources() {
		List<IBaseResource> retVal = new ArrayList<>();
		for (IContextValidationSupport next : myChain) {
			List<IBaseResource> candidates = next.fetchAllConformanceResources();
			if (candidates != null) {
				retVal.addAll(candidates);
			}
		}
		return retVal;
	}

	@Override
	public List<IBaseResource> fetchAllStructureDefinitions() {
		ArrayList<IBaseResource> retVal = new ArrayList<>();
		Set<String> urls = new HashSet<>();
		for (IContextValidationSupport nextSupport : myChain) {
			List<IBaseResource> allStructureDefinitions = nextSupport.fetchAllStructureDefinitions();
			if (allStructureDefinitions != null) {
				for (IBaseResource next : allStructureDefinitions) {

					IPrimitiveType<?> urlType = getFhirContext().newTerser().getSingleValueOrNull(next, "url", IPrimitiveType.class);
					if (urlType == null || isBlank(urlType.getValueAsString()) || urls.add(urlType.getValueAsString())) {
						retVal.add(next);
					}
				}
			}
		}
		return retVal;
	}

	@Override
	public IBaseResource fetchCodeSystem(String theSystem) {
		for (IContextValidationSupport next : myChain) {
			IBaseResource retVal = next.fetchCodeSystem(theSystem);
			if (retVal != null) {
				return retVal;
			}
		}
		return null;
	}

	@Override
	public IBaseResource fetchValueSet(String theUrl) {
		for (IContextValidationSupport next : myChain) {
			IBaseResource retVal = next.fetchValueSet(theUrl);
			if (retVal != null) {
				return retVal;
			}
		}
		return null;
	}


	@Override
	public <T extends IBaseResource> T fetchResource(Class<T> theClass, String theUri) {
		for (IContextValidationSupport next : myChain) {
			T retVal = next.fetchResource(theClass, theUri);
			if (retVal != null) {
				return retVal;
			}
		}
		return null;
	}

	@Override
	public IBaseResource fetchStructureDefinition(String theUrl) {
		for (IContextValidationSupport next : myChain) {
			IBaseResource retVal = next.fetchStructureDefinition(theUrl);
			if (retVal != null) {
				return retVal;
			}
		}
		return null;
	}

	@Override
	public boolean isCodeSystemSupported(IContextValidationSupport theRootValidationSupport, String theSystem) {
		for (IContextValidationSupport next : myChain) {
			if (next.isCodeSystemSupported(theRootValidationSupport, theSystem)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public CodeValidationResult validateCode(IContextValidationSupport theRootValidationSupport, ConceptValidationOptions theOptions, String theCodeSystem, String theCode, String theDisplay, String theValueSetUrl) {
		for (IContextValidationSupport next : myChain) {
			if (theOptions.isInferSystem() || (theCodeSystem != null && next.isCodeSystemSupported(theRootValidationSupport, theCodeSystem))) {
				CodeValidationResult retVal = next.validateCode(theRootValidationSupport, theOptions, theCodeSystem, theCode, theDisplay, theValueSetUrl);
				if (retVal != null) {
					return retVal;
				}
			}
		}
		return null; // myChain.get(0).validateCode(theRootValidationSupport, theOptions, theCodeSystem, theCode, theDisplay, theValueSetUrl);
	}

	@Override
	public LookupCodeResult lookupCode(IContextValidationSupport theRootValidationSupport, String theSystem, String theCode) {
		for (IContextValidationSupport next : myChain) {
			if (next.isCodeSystemSupported(theRootValidationSupport, theSystem)) {
				return next.lookupCode(theRootValidationSupport, theSystem, theCode);
			}
		}
		return null;
	}


}
