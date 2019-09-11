package org.hl7.fhir.r5.hapi.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.hapi.ctx.IValidationSupport;
import org.hl7.fhir.r5.model.CodeSystem;
import org.hl7.fhir.r5.model.StructureDefinition;
import org.hl7.fhir.r5.model.ValueSet;
import org.hl7.fhir.r5.model.ValueSet.ConceptSetComponent;
import org.hl7.fhir.r5.terminologies.ValueSetExpander;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class ValidationSupportChain implements IValidationSupport {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ValidationSupportChain.class);

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
				myChain.add(next);
			}
		}
	}

	public void addValidationSupport(IValidationSupport theValidationSupport) {
		myChain.add(theValidationSupport);
	}

	@Override
	public ValueSetExpander.ValueSetExpansionOutcome expandValueSet(FhirContext theCtx, ConceptSetComponent theInclude) {
		for (IValidationSupport next : myChain) {
			if (next.isCodeSystemSupported(theCtx, theInclude.getSystem())) {
				ValueSetExpander.ValueSetExpansionOutcome expansion = next.expandValueSet(theCtx, theInclude);
				if (expansion != null) {
					return expansion;
				}
			}
		}

		throw new InvalidRequestException("unable to find code system " + theInclude.getSystem());
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
	public CodeSystem fetchCodeSystem(FhirContext theCtx, String theSystem) {
		for (IValidationSupport next : myChain) {
			if (next.isCodeSystemSupported(theCtx, theSystem)) {
				CodeSystem retVal = next.fetchCodeSystem(theCtx, theSystem);
				if (retVal != null) {
					return retVal;
				}
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
	public StructureDefinition generateSnapshot(StructureDefinition theInput, String theUrl, String theWebUrl, String theProfileName) {
		StructureDefinition outcome = null;
		for (IValidationSupport next : myChain) {
			outcome = next.generateSnapshot(theInput, theUrl, theWebUrl, theProfileName);
			if (outcome != null) {
				break;
			}
		}
		return outcome;
	}

	@Override
	public CodeValidationResult validateCode(FhirContext theCtx, String theCodeSystem, String theCode, String theDisplay) {

		ourLog.debug("Validating code {} in chain with {} items", theCode, myChain.size());

		for (IValidationSupport next : myChain) {
			if (next.isCodeSystemSupported(theCtx, theCodeSystem)) {
				CodeValidationResult result = next.validateCode(theCtx, theCodeSystem, theCode, theDisplay);
				if (result != null) {
					ourLog.debug("Chain item {} returned outcome {}", next, result.isOk());
					return result;
				}
			} else {
				ourLog.debug("Chain item {} does not support code system {}", next, theCodeSystem);
			}
		}
		return myChain.get(0).validateCode(theCtx, theCodeSystem, theCode, theDisplay);
	}

	@Override
	public LookupCodeResult lookupCode(FhirContext theContext, String theSystem, String theCode) {
		for (IValidationSupport next : myChain) {
			if (next.isCodeSystemSupported(theContext, theSystem)) {
				return next.lookupCode(theContext, theSystem, theCode);
			}
		}
		return null;
	}

	@Override
	public List<StructureDefinition> fetchAllStructureDefinitions(FhirContext theContext) {
		ArrayList<StructureDefinition> retVal = new ArrayList<>();
		Set<String> urls = new HashSet<>();
		for (IValidationSupport nextSupport : myChain) {
			List<StructureDefinition> list = nextSupport.fetchAllStructureDefinitions(theContext);
			if (list != null) {
				for (StructureDefinition next : list) {
					if (isBlank(next.getUrl()) || urls.add(next.getUrl())) {
						retVal.add(next);
					}
				}
			}
		}
		return retVal;
	}

}
