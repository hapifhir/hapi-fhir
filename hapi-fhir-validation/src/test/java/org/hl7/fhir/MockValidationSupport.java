package org.hl7.fhir;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.hl7.fhir.common.hapi.validation.support.InMemoryTerminologyServerValidationSupport;
import org.hl7.fhir.dstu3.model.CodeSystem;
import org.hl7.fhir.dstu3.model.ValueSet;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MockValidationSupport implements IValidationSupport {
	private static final Logger ourLog = LoggerFactory.getLogger(MockValidationSupport.class);
	private final IValidationSupport myDefaultValidationSupport;
	private final FhirContext myFhirContext;
	private final ArrayList<String> myValidConcepts = new ArrayList<>();
	private final Set<String> myValidSystems = new HashSet<>();
	private final Set<String> myValidSystemsNotReturningIssues = new HashSet<>();
	private final Map<String, ValueSet.ValueSetExpansionComponent> mySupportedCodeSystemsForExpansion = new HashMap<>();
	private HashMap<String, IBaseResource> myStructureDefinitions = new HashMap<>();
	private HashMap<String, IBaseResource> myCodeSystems = new HashMap<>();
	private HashMap<String, IBaseResource> myValueSets = new HashMap<>();
	private HashMap<String, IBaseResource> myQuestionnaires = new HashMap<>();
	private Set<String> mySupportedValueSets = new HashSet<>();
	private int myCountValidateCodeInValueSet;
	private int myCountValidateCode;

	public MockValidationSupport(FhirContext theFhirContext) {
		myFhirContext = theFhirContext;
		myDefaultValidationSupport = myFhirContext.getValidationSupport();
	}

	@Override
	public FhirContext getFhirContext() {
		return myFhirContext;
	}

	public void addValidConcept(String theSystem, String theCode) {
		addValidConcept(theSystem, theCode, true);
	}

	public void addValidConcept(String theSystem, String theCode, boolean theShouldSystemReturnIssuesForInvalidCode) {
		if (theShouldSystemReturnIssuesForInvalidCode) {
			myValidSystems.add(theSystem);
		} else {
			myValidSystemsNotReturningIssues.add(theSystem);
		}
		myValidConcepts.add(theSystem + "___" + theCode);
	}

	@Override
	public boolean isCodeSystemSupported(ValidationSupportContext theValidationSupportContext, String theSystem) {
		boolean retVal = myValidSystems.contains(theSystem);
		ourLog.debug("isCodeSystemSupported({}) : {}", theSystem, retVal);
		if (retVal == false) {
			retVal = myCodeSystems.containsKey(theSystem);
		}
		return retVal;
	}

	@Override
	public boolean isValueSetSupported(ValidationSupportContext theValidationSupportContext, String theValueSetUrl) {
		boolean retVal = false;
		if (mySupportedValueSets.contains(theValueSetUrl)) {
			retVal = true;
		}
		if (myValueSets.containsKey(theValueSetUrl)) {
			retVal = true;
		}
		ourLog.info("isValueSetSupported({}) : {}", theValueSetUrl, retVal);
		return retVal;
	}

	@Nullable
	@Override
	public IBaseResource fetchValueSet(String theValueSetUrl) {
		return myValueSets.get(theValueSetUrl);
	}

	//	@Nullable
//	@Override
//	public ValueSetExpansionOutcome expandValueSet(ValidationSupportContext theValidationSupportContext, @Nullable ValueSetExpansionOptions theExpansionOptions, @Nonnull IBaseResource theValueSetToExpand) {
//		ValueSet arg = (ValueSet) theValueSetToExpand;
//		ValueSet.ValueSetExpansionComponent retVal = mySupportedCodeSystemsForExpansion.get(arg.getCompose().getIncludeFirstRep().getSystem());
//		if (retVal == null) {
//			ValueSet expandedVs = (ValueSet) myDefaultValidationSupport.expandValueSet(new ValidationSupportContext(myDefaultValidationSupport), null, arg).getValueSet();
//			retVal = expandedVs.getExpansion();
//		}
//		return new ValueSetExpansionOutcome(retVal);
//
//	}


	@Nullable
	@Override
	public CodeValidationResult validateCode(ValidationSupportContext theValidationSupportContext, ConceptValidationOptions theOptions, String theCodeSystem, String theCode, String theDisplay, String theValueSetUrl) {
		myCountValidateCode++;
		CodeValidationResult retVal = doValidateCode(theOptions, theCodeSystem, theCode, theDisplay, theValueSetUrl);
		ourLog.debug("validateCode({}, {}, {}, {}) : {}", theCodeSystem, theCode, theDisplay, theValueSetUrl, retVal);
		return retVal;
	}

	@Nullable
	@Override
	public CodeValidationResult validateCodeInValueSet(ValidationSupportContext theValidationSupportContext, ConceptValidationOptions theOptions, String theCodeSystem, String theCode, String theDisplay, @Nonnull IBaseResource theValueSet) {
		myCountValidateCodeInValueSet++;
		CodeValidationResult retVal = doValidateCode(theOptions, theCodeSystem, theCode, theDisplay, null);
		ourLog.debug("validateCode({}, {}, {}, {}) : {}", theCodeSystem, theCode, theDisplay, null, retVal);
		return retVal;
	}

	@Nullable
	private CodeValidationResult doValidateCode(ConceptValidationOptions theOptions, String theCodeSystem, String theCode, String theDisplay, String theValueSetUrl) {
		CodeValidationResult retVal;
		if (myValidConcepts.contains(theCodeSystem + "___" + theCode)) {
			retVal = new CodeValidationResult().setCode(theCode);
		} else if (myValidSystems.contains(theCodeSystem)) {
			final String message = "Unknown code (for '" + theCodeSystem + "#" + theCode + "')";
			retVal = new CodeValidationResult().setSeverity(IssueSeverity.ERROR).setMessage(message).setIssues(Collections.singletonList(new CodeValidationIssue(message, IssueSeverity.ERROR, CodeValidationIssueCode.CODE_INVALID, CodeValidationIssueCoding.INVALID_CODE)));
		} else if (myValidSystemsNotReturningIssues.contains(theCodeSystem)) {
			final String message = "Unknown code (for '" + theCodeSystem + "#" + theCode + "')";
			retVal = new CodeValidationResult().setSeverity(IssueSeverity.ERROR).setMessage(message);
		} else if (myCodeSystems.containsKey(theCodeSystem)) {
			InMemoryTerminologyServerValidationSupport inMemory = new InMemoryTerminologyServerValidationSupport(myFhirContext);
			ValidationSupportContext nestedCtx = new ValidationSupportContext(this);
			retVal = inMemory.validateCode(nestedCtx, theOptions, theCodeSystem, theCode, theDisplay, theValueSetUrl);
		} else {
			retVal = null;
		}
		return retVal;
	}

	@Nullable
	@Override
	public IBaseResource fetchCodeSystem(String theSystem) {
		return myCodeSystems.get(theSystem);
	}

	@Nullable
	@Override
	public IBaseResource fetchStructureDefinition(String theUrl) {
		return myStructureDefinitions.get(theUrl);
	}

	@Nullable
	@Override
	public <T extends IBaseResource> List<T> fetchAllStructureDefinitions() {
		return (List<T>) new ArrayList<>(myStructureDefinitions.values());
	}

	@Nullable
	@Override
	public <T extends IBaseResource> T fetchResource(@Nullable Class<T> theClass, String theUri) {
		IBaseResource retVal = null;

		String type = theClass != null ? myFhirContext.getResourceType(theClass) : null;

		if (retVal == null && ("StructureDefinition".equals(type) || type == null)) {
			retVal = myStructureDefinitions.get(theUri);
		}
		if (retVal == null && ("ValueSet".equals(type) || type == null)) {
			retVal = myValueSets.get(theUri);
		}
		if (retVal == null && ("CodeSystem".equals(type) || type == null)) {
			retVal = myCodeSystems.get(theUri);
		}
		if (retVal == null && ("Questionnaire".equals(type) || type == null)) {
			retVal = myQuestionnaires.get(theUri);
		}

		return (T) retVal;
	}

	public void addValueSet(String theUrl, IBaseResource theValueSet) {
		myValueSets.put(theUrl, theValueSet);
	}

	public void addStructureDefinition(String theUrl, IBaseResource theSd) {
		myStructureDefinitions.put(theUrl, theSd);
	}

	public void addCodeSystem(String theUrl, CodeSystem theCs) {
		myCodeSystems.put(theUrl, theCs);
	}

	public void addQuestionnaire(String theUrl, IBaseResource theQuestionnaire) {
		myQuestionnaires.put(theUrl, theQuestionnaire);
	}

	public void addValidValueSet(String theSupportedValueSet) {
		mySupportedValueSets.add(theSupportedValueSet);
	}

	public int countValidateCodeInValueSet() {
		return myCountValidateCodeInValueSet;
	}

	public int countValidateCode() {
		return myCountValidateCode;
	}
}

