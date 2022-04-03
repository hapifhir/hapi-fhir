package org.hl7.fhir.common.hapi.validation.support;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.TranslateConceptResults;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.context.support.ValueSetExpansionOptions;
import ca.uhn.fhir.i18n.Msg;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

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
	public TranslateConceptResults translateConcept(TranslateCodeRequest theRequest) {
		TranslateConceptResults retVal = null;
		for (IValidationSupport next : myChain) {
			TranslateConceptResults translations = next.translateConcept(theRequest);
			if (translations != null) {
				if (retVal == null) {
					retVal = new TranslateConceptResults();
				}

				if (retVal.getMessage() == null) {
					retVal.setMessage(translations.getMessage());
				}

				if (translations.getResult() && !retVal.getResult()) {
					retVal.setResult(translations.getResult());
					retVal.setMessage(translations.getMessage());
				}

				retVal.getResults().addAll(translations.getResults());
			}
		}
		return retVal;
	}

	@Override
	public void invalidateCaches() {
		for (IValidationSupport next : myChain) {
			next.invalidateCaches();
		}
	}

	@Override
	public boolean isValueSetSupported(ValidationSupportContext theValidationSupportContext, String theValueSetUrl) {
		for (IValidationSupport next : myChain) {
			boolean retVal = next.isValueSetSupported(theValidationSupportContext, theValueSetUrl);
			if (retVal) {
				return true;
			}
		}
		return false;
	}

	@Override
	public IBaseResource generateSnapshot(ValidationSupportContext theValidationSupportContext, IBaseResource theInput, String theUrl, String theWebUrl, String theProfileName) {
		for (IValidationSupport next : myChain) {
			IBaseResource retVal = next.generateSnapshot(theValidationSupportContext, theInput, theUrl, theWebUrl, theProfileName);
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
			throw new ConfigurationException(Msg.code(708) + message);
		}

		FhirContext existingFhirContext = getFhirContext();
		if (existingFhirContext != null) {
			FhirVersionEnum newVersion = theValidationSupport.getFhirContext().getVersion().getVersion();
			FhirVersionEnum existingVersion = existingFhirContext.getVersion().getVersion();
			if (!existingVersion.equals(newVersion)) {
				String message = "Trying to add validation support of version " + newVersion + " to chain with " + myChain.size() + " entries of version " + existingVersion;
				throw new ConfigurationException(Msg.code(709) + message);
			}
		}

		myChain.add(theIndex, theValidationSupport);
	}

	/**
	 * Removes an item from the chain. Note that this method is mostly intended for testing. Removing items from the chain while validation is
	 * actually occurring is not an expected use case for this class.
	 */
	public void removeValidationSupport(IValidationSupport theValidationSupport) {
		myChain.remove(theValidationSupport);
	}

	@Override
	public ValueSetExpansionOutcome expandValueSet(ValidationSupportContext theValidationSupportContext, ValueSetExpansionOptions theExpansionOptions, @Nonnull IBaseResource theValueSetToExpand) {
		for (IValidationSupport next : myChain) {
			// TODO: test if code system is supported?
			ValueSetExpansionOutcome expanded = next.expandValueSet(theValidationSupportContext, theExpansionOptions, theValueSetToExpand);
			if (expanded != null) {
				return expanded;
			}
		}
		return null;
	}

	@Override
	public boolean isRemoteTerminologyServiceConfigured() {
		if (myChain != null) {
			Optional<IValidationSupport> remoteTerminologyService = myChain.stream().filter(RemoteTerminologyServiceValidationSupport.class::isInstance).findFirst();
			if (remoteTerminologyService.isPresent()) {
				return true;
			}
		}
		return false;
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
		return doFetchStructureDefinitions(t->t.fetchAllStructureDefinitions());
	}

	@Override
	public List<IBaseResource> fetchAllNonBaseStructureDefinitions() {
		return doFetchStructureDefinitions(t->t.fetchAllNonBaseStructureDefinitions());
	}

	private List<IBaseResource> doFetchStructureDefinitions(Function<IValidationSupport, List<IBaseResource>> theFunction) {
		ArrayList<IBaseResource> retVal = new ArrayList<>();
		Set<String> urls = new HashSet<>();
		for (IValidationSupport nextSupport : myChain) {
			List<IBaseResource> allStructureDefinitions = theFunction.apply(nextSupport);
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
	public boolean isCodeSystemSupported(ValidationSupportContext theValidationSupportContext, String theSystem) {
		for (IValidationSupport next : myChain) {
			if (next.isCodeSystemSupported(theValidationSupportContext, theSystem)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public CodeValidationResult validateCode(@Nonnull ValidationSupportContext theValidationSupportContext, @Nonnull ConceptValidationOptions theOptions, String theCodeSystem, String theCode, String theDisplay, String theValueSetUrl) {
		for (IValidationSupport next : myChain) {
			if ((isBlank(theValueSetUrl) && next.isCodeSystemSupported(theValidationSupportContext, theCodeSystem)) || (isNotBlank(theValueSetUrl) && next.isValueSetSupported(theValidationSupportContext, theValueSetUrl))) {
				CodeValidationResult retVal = next.validateCode(theValidationSupportContext, theOptions, theCodeSystem, theCode, theDisplay, theValueSetUrl);
				if (retVal != null) {
					return retVal;
				}
			}
		}
		return null;
	}

	@Override
	public CodeValidationResult validateCodeInValueSet(ValidationSupportContext theValidationSupportContext, ConceptValidationOptions theOptions, String theCodeSystem, String theCode, String theDisplay, @Nonnull IBaseResource theValueSet) {
		for (IValidationSupport next : myChain) {
			String url = CommonCodeSystemsTerminologyService.getValueSetUrl(theValueSet);
			if (isBlank(url) || next.isValueSetSupported(theValidationSupportContext, url)) {
				CodeValidationResult retVal = next.validateCodeInValueSet(theValidationSupportContext, theOptions, theCodeSystem, theCode, theDisplay, theValueSet);
				if (retVal != null) {
					return retVal;
				}
			}
		}
		return null;
	}

	@Override
	public LookupCodeResult lookupCode(ValidationSupportContext theValidationSupportContext, String theSystem, String theCode, String theDisplayLanguage) {
		for (IValidationSupport next : myChain) {
			if (next.isCodeSystemSupported(theValidationSupportContext, theSystem)) {
				return next.lookupCode(theValidationSupportContext, theSystem, theCode, theDisplayLanguage);
			}
		}
		return null;
	}
}
