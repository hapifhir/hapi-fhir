package org.hl7.fhir.common.hapi.validation.support;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValueSetExpansionOptions;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import javax.annotation.Nonnull;
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
		myChain = new ArrayList<>();
	}

	/**
	 * Constructor
	 */
	public ValidationSupportChain(IValidationSupport... theValidationSupportModules) {
		this();
		for (IValidationSupport next : theValidationSupportModules) {
			if (next != null) {
				addValidationSupport(next);
			}
		}
	}

	@Override
	public void invalidateCaches() {
		for (IValidationSupport next : myChain) {
			next.invalidateCaches();
		}
	}

	@Override
	public boolean isValueSetSupported(IValidationSupport theRootValidationSupport, String theValueSetUrl) {
		for (IValidationSupport next : myChain) {
			boolean retVal = next.isValueSetSupported(theRootValidationSupport, theValueSetUrl);
			if (retVal) {
				return true;
			}
		}
		return false;
	}

	@Override
	public IBaseResource generateSnapshot(IValidationSupport theRootValidationSupport, IBaseResource theInput, String theUrl, String theWebUrl, String theProfileName) {
		for (IValidationSupport next : myChain) {
			IBaseResource retVal = next.generateSnapshot(theRootValidationSupport, theInput, theUrl, theWebUrl, theProfileName);
			if (retVal != null) {
				return retVal;
			}
		}
		return null;
	}

	@Override
	public FhirContext getFhirContext() {
		if (myChain.size() == 0) {
			return null;
		}
		return myChain.get(0).getFhirContext();
	}

	/**
	 * Add a validation support module to the chain.
	 * <p>
	 * Note that this method is not thread-safe. All validation support modules should be added prior to use.
	 * </p>
	 *
	 * @param theValidationSupport The validation support. Must not be null, and must have a {@link #getFhirContext() FhirContext} that is configured for the same FHIR version as other entries in the chain.
	 */
	public void addValidationSupport(IValidationSupport theValidationSupport) {
		int index = myChain.size();
		addValidationSupport(index, theValidationSupport);
	}

	/**
	 * Add a validation support module to the chain at the given index.
	 * <p>
	 * Note that this method is not thread-safe. All validation support modules should be added prior to use.
	 * </p>
	 *
	 * @param theIndex             The index to add to
	 * @param theValidationSupport The validation support. Must not be null, and must have a {@link #getFhirContext() FhirContext} that is configured for the same FHIR version as other entries in the chain.
	 */
	public void addValidationSupport(int theIndex, IValidationSupport theValidationSupport) {
		Validate.notNull(theValidationSupport, "theValidationSupport must not be null");

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

		myChain.add(theIndex, theValidationSupport);
	}

	@Override
	public ValueSetExpansionOutcome expandValueSet(IValidationSupport theRootValidationSupport, ValueSetExpansionOptions theExpansionOptions, IBaseResource theValueSetToExpand) {
		for (IValidationSupport next : myChain) {
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
		for (IValidationSupport next : myChain) {
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
		for (IValidationSupport nextSupport : myChain) {
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
		for (IValidationSupport next : myChain) {
			IBaseResource retVal = next.fetchCodeSystem(theSystem);
			if (retVal != null) {
				return retVal;
			}
		}
		return null;
	}

	@Override
	public IBaseResource fetchValueSet(String theUrl) {
		for (IValidationSupport next : myChain) {
			IBaseResource retVal = next.fetchValueSet(theUrl);
			if (retVal != null) {
				return retVal;
			}
		}
		return null;
	}


	@Override
	public <T extends IBaseResource> T fetchResource(Class<T> theClass, String theUri) {
		for (IValidationSupport next : myChain) {
			T retVal = next.fetchResource(theClass, theUri);
			if (retVal != null) {
				return retVal;
			}
		}
		return null;
	}

	@Override
	public IBaseResource fetchStructureDefinition(String theUrl) {
		for (IValidationSupport next : myChain) {
			IBaseResource retVal = next.fetchStructureDefinition(theUrl);
			if (retVal != null) {
				return retVal;
			}
		}
		return null;
	}

	@Override
	public boolean isCodeSystemSupported(IValidationSupport theRootValidationSupport, String theSystem) {
		for (IValidationSupport next : myChain) {
			if (next.isCodeSystemSupported(theRootValidationSupport, theSystem)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public CodeValidationResult validateCode(IValidationSupport theRootValidationSupport, ConceptValidationOptions theOptions, String theCodeSystem, String theCode, String theDisplay, String theValueSetUrl) {
		for (IValidationSupport next : myChain) {
			if (theOptions.isInferSystem() || (theCodeSystem != null && next.isCodeSystemSupported(theRootValidationSupport, theCodeSystem))) {
				CodeValidationResult retVal = next.validateCode(theRootValidationSupport, theOptions, theCodeSystem, theCode, theDisplay, theValueSetUrl);
				if (retVal != null) {
					return retVal;
				}
			}
		}
		return null;
	}

	@Override
	public CodeValidationResult validateCodeInValueSet(IValidationSupport theRootValidationSupport, ConceptValidationOptions theOptions, String theCodeSystem, String theCode, String theDisplay, @Nonnull IBaseResource theValueSet) {
		for (IValidationSupport next : myChain) {
			CodeValidationResult retVal = next.validateCodeInValueSet(theRootValidationSupport, theOptions, theCodeSystem, theCode, theDisplay, theValueSet);
			if (retVal != null) {
				return retVal;
			}
		}
		return null;
	}

	@Override
	public LookupCodeResult lookupCode(IValidationSupport theRootValidationSupport, String theSystem, String theCode) {
		for (IValidationSupport next : myChain) {
			if (next.isCodeSystemSupported(theRootValidationSupport, theSystem)) {
				return next.lookupCode(theRootValidationSupport, theSystem, theCode);
			}
		}
		return null;
	}


}
