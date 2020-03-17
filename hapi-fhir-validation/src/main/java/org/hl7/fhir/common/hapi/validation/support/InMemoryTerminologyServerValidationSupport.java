package org.hl7.fhir.common.hapi.validation.support;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValueSetExpansionOptions;
import ca.uhn.fhir.util.VersionIndependentConcept;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.convertors.conv10_50.ValueSet10_50;
import org.hl7.fhir.convertors.conv30_50.CodeSystem30_50;
import org.hl7.fhir.convertors.conv30_50.ValueSet30_50;
import org.hl7.fhir.convertors.conv40_50.CodeSystem40_50;
import org.hl7.fhir.convertors.conv40_50.ValueSet40_50;
import org.hl7.fhir.dstu2.model.ValueSet;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r5.model.CanonicalType;
import org.hl7.fhir.r5.model.CodeSystem;
import org.hl7.fhir.utilities.validation.ValidationMessage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * This class is a basic in-memory terminology service, designed to expand ValueSets and validate codes
 * completely in-memory. It is suitable for runtime validation purposes where no dedicated terminology
 * service exists (either an internal one such as the HAPI FHIR JPA terminology service, or an
 * external term service API)
 */
public class InMemoryTerminologyServerValidationSupport implements IValidationSupport {
	private final FhirContext myCtx;

	public InMemoryTerminologyServerValidationSupport(FhirContext theCtx) {
		Validate.notNull(theCtx, "theCtx must not be null");
		myCtx = theCtx;
	}

	@Override
	public FhirContext getFhirContext() {
		return myCtx;
	}

	@Override
	public ValueSetExpansionOutcome expandValueSet(IValidationSupport theRootValidationSupport, ValueSetExpansionOptions theExpansionOptions, IBaseResource theValueSetToExpand) {

		org.hl7.fhir.r5.model.ValueSet expansionR5 = expandValueSetToCanonical(theRootValidationSupport, theValueSetToExpand);
		if (expansionR5 == null) {
			return null;
		}

		IBaseResource expansion;
		switch (myCtx.getVersion().getVersion()) {
			case DSTU2_HL7ORG: {
				expansion = ValueSet10_50.convertValueSet(expansionR5);
				break;
			}
			case DSTU3: {
				expansion = ValueSet30_50.convertValueSet(expansionR5);
				break;
			}
			case R4: {
				expansion = ValueSet40_50.convertValueSet(expansionR5);
				break;
			}
			case R5: {
				expansion = expansionR5;
				break;
			}
			case DSTU2:
			case DSTU2_1:
			default:
				throw new IllegalArgumentException("Can not handle version: " + myCtx.getVersion().getVersion());
		}

		return new ValueSetExpansionOutcome(expansion, null);
	}

	private org.hl7.fhir.r5.model.ValueSet expandValueSetToCanonical(IValidationSupport theRootValidationSupport, IBaseResource theValueSetToExpand) {
		org.hl7.fhir.r5.model.ValueSet expansionR5;
		switch (myCtx.getVersion().getVersion()) {
			case DSTU2:
			case DSTU2_HL7ORG: {
				expansionR5 = expandValueSetDstu2Hl7Org(theRootValidationSupport, (ValueSet) theValueSetToExpand);
				break;
			}
			case DSTU3: {
				expansionR5 = expandValueSetDstu3(theRootValidationSupport, (org.hl7.fhir.dstu3.model.ValueSet) theValueSetToExpand);
				break;
			}
			case R4: {
				expansionR5 = expandValueSetR4(theRootValidationSupport, (org.hl7.fhir.r4.model.ValueSet) theValueSetToExpand);
				break;
			}
			case R5: {
				expansionR5 = expandValueSetR5(theRootValidationSupport, (org.hl7.fhir.r5.model.ValueSet) theValueSetToExpand);
				break;
			}
			case DSTU2_1:
			default:
				throw new IllegalArgumentException("Can not handle version: " + myCtx.getVersion().getVersion());
		}

		if (expansionR5 == null) {
			return null;
		}
		return expansionR5;
	}

	@Override
	public CodeValidationResult validateCodeInValueSet(IValidationSupport theRootValidationSupport, ConceptValidationOptions theOptions, String theCodeSystem, String theCode, String theDisplay, @Nonnull IBaseResource theValueSet) {
	org.hl7.fhir.r5.model.ValueSet expansion = expandValueSetToCanonical(theRootValidationSupport, theValueSet);
		if (expansion == null) {
			return null;
		}
		return validateCodeInExpandedValueSet(theRootValidationSupport, theOptions, theCodeSystem, theCode, expansion);
	}


	@Override
	public CodeValidationResult validateCode(IValidationSupport theRootValidationSupport, ConceptValidationOptions theOptions, String theCodeSystem, String theCode, String theDisplay, String theValueSetUrl) {
		IBaseResource vs;
		if (isNotBlank(theValueSetUrl)) {
			vs = theRootValidationSupport.fetchValueSet(theValueSetUrl);
			if (vs == null) {
				return null;
			}
		} else {
			switch (myCtx.getVersion().getVersion()) {
				case DSTU2_HL7ORG:
					vs = new org.hl7.fhir.dstu2.model.ValueSet()
						.setCompose(new org.hl7.fhir.dstu2.model.ValueSet.ValueSetComposeComponent()
							.addInclude(new org.hl7.fhir.dstu2.model.ValueSet.ConceptSetComponent().setSystem(theCodeSystem)));
					break;
				case DSTU3:
					vs = new org.hl7.fhir.dstu3.model.ValueSet()
						.setCompose(new org.hl7.fhir.dstu3.model.ValueSet.ValueSetComposeComponent()
							.addInclude(new org.hl7.fhir.dstu3.model.ValueSet.ConceptSetComponent().setSystem(theCodeSystem)));
					break;
				case R4:
					vs = new org.hl7.fhir.r4.model.ValueSet()
						.setCompose(new org.hl7.fhir.r4.model.ValueSet.ValueSetComposeComponent()
							.addInclude(new org.hl7.fhir.r4.model.ValueSet.ConceptSetComponent().setSystem(theCodeSystem)));
					break;
				case R5:
					vs = new org.hl7.fhir.r5.model.ValueSet()
						.setCompose(new org.hl7.fhir.r5.model.ValueSet.ValueSetComposeComponent()
							.addInclude(new org.hl7.fhir.r5.model.ValueSet.ConceptSetComponent().setSystem(theCodeSystem)));
					break;
				case DSTU2:
				case DSTU2_1:
				default:
					throw new IllegalArgumentException("Can not handle version: " + myCtx.getVersion().getVersion());
			}
		}

		ValueSetExpansionOutcome valueSetExpansionOutcome = expandValueSet(theRootValidationSupport, null, vs);
		if (valueSetExpansionOutcome == null) {
			return null;
		}

		IBaseResource expansion = valueSetExpansionOutcome.getValueSet();

		return validateCodeInExpandedValueSet(theRootValidationSupport, theOptions, theCodeSystem, theCode, expansion);

	}

	private CodeValidationResult validateCodeInExpandedValueSet(IValidationSupport theRootValidationSupport, ConceptValidationOptions theOptions, String theCodeSystem, String theCode, IBaseResource theExpansion) {
		assert theExpansion != null;

		boolean caseSensitive = true;
		IBaseResource system = null;
		if (!theOptions.isInferSystem() && isNotBlank(theCodeSystem)) {
			system = theRootValidationSupport.fetchCodeSystem(theCodeSystem);
			if (system == null) {
				return null;
			}
		}

		List<VersionIndependentConcept> codes = new ArrayList<>();
		switch (theExpansion.getStructureFhirVersionEnum()) {
			case DSTU2_HL7ORG: {
				ValueSet expansionVs = (ValueSet) theExpansion;
				List<ValueSet.ValueSetExpansionContainsComponent> contains = expansionVs.getExpansion().getContains();
				flattenAndConvertCodesDstu2(contains, codes);
				break;
			}
			case DSTU3: {
				org.hl7.fhir.dstu3.model.ValueSet expansionVs = (org.hl7.fhir.dstu3.model.ValueSet) theExpansion;
				List<org.hl7.fhir.dstu3.model.ValueSet.ValueSetExpansionContainsComponent> contains = expansionVs.getExpansion().getContains();
				flattenAndConvertCodesDstu3(contains, codes);
				break;
			}
			case R4: {
				org.hl7.fhir.r4.model.ValueSet expansionVs = (org.hl7.fhir.r4.model.ValueSet) theExpansion;
				List<org.hl7.fhir.r4.model.ValueSet.ValueSetExpansionContainsComponent> contains = expansionVs.getExpansion().getContains();
				flattenAndConvertCodesR4(contains, codes);
				break;
			}
			case R5: {
				org.hl7.fhir.r5.model.ValueSet expansionVs = (org.hl7.fhir.r5.model.ValueSet) theExpansion;
				List<org.hl7.fhir.r5.model.ValueSet.ValueSetExpansionContainsComponent> contains = expansionVs.getExpansion().getContains();
				flattenAndConvertCodesR5(contains, codes);
				break;
			}
			case DSTU2:
			case DSTU2_1:
			default:
				throw new IllegalArgumentException("Can not handle version: " + myCtx.getVersion().getVersion());
		}

		String codeSystemName = null;
		String codeSystemVersion = null;
		if (system != null) {
			switch (system.getStructureFhirVersionEnum()) {
				case DSTU2_HL7ORG: {
					caseSensitive = true;
					break;
				}
				case DSTU3: {
					org.hl7.fhir.dstu3.model.CodeSystem systemDstu3 = (org.hl7.fhir.dstu3.model.CodeSystem) system;
					caseSensitive = systemDstu3.getCaseSensitive();
					codeSystemName = systemDstu3.getName();
					codeSystemVersion = systemDstu3.getVersion();
					break;
				}
				case R4: {
					org.hl7.fhir.r4.model.CodeSystem systemR4 = (org.hl7.fhir.r4.model.CodeSystem) system;
					caseSensitive = systemR4.getCaseSensitive();
					codeSystemName = systemR4.getName();
					codeSystemVersion = systemR4.getVersion();
					break;
				}
				case R5: {
					CodeSystem systemR5 = (CodeSystem) system;
					caseSensitive = systemR5.getCaseSensitive();
					codeSystemName = systemR5.getName();
					codeSystemVersion = systemR5.getVersion();
					break;
				}
				case DSTU2:
				case DSTU2_1:
				default:
					throw new IllegalArgumentException("Can not handle version: " + myCtx.getVersion().getVersion());
			}
		}

		for (VersionIndependentConcept nextExpansionCode : codes) {

			boolean codeMatches;
			if (caseSensitive) {
				codeMatches = theCode.equals(nextExpansionCode.getCode());
			} else {
				codeMatches = theCode.equalsIgnoreCase(nextExpansionCode.getCode());
			}
			if (codeMatches) {
				if (theOptions.isInferSystem() || nextExpansionCode.getSystem().equals(theCodeSystem)) {
					return new CodeValidationResult()
						.setCode(theCode)
						.setDisplay(nextExpansionCode.getDisplay())
						.setCodeSystemName(codeSystemName)
						.setCodeSystemVersion(codeSystemVersion);
				}
			}
		}

		ValidationMessage.IssueSeverity severity = ValidationMessage.IssueSeverity.ERROR;

		String message = "Unknown code '" + (isNotBlank(theCodeSystem) ? theCodeSystem + "#" : "") + theCode + "'";
		return new CodeValidationResult()
			.setSeverityCode(severity.toCode())
			.setMessage(message);
	}

	@Override
	public LookupCodeResult lookupCode(IValidationSupport theRootValidationSupport, String theSystem, String theCode) {
		return validateCode(theRootValidationSupport, new ConceptValidationOptions(), theSystem, theCode, null, null).asLookupCodeResult(theSystem, theCode);
	}

	@Nullable
	private org.hl7.fhir.r5.model.ValueSet expandValueSetDstu2Hl7Org(IValidationSupport theRootValidationSupport, ValueSet theInput) {
		Function<String, CodeSystem> codeSystemLoader = t -> {
			org.hl7.fhir.dstu2.model.ValueSet codeSystem = (org.hl7.fhir.dstu2.model.ValueSet) theRootValidationSupport.fetchCodeSystem(t);
			CodeSystem retVal = new CodeSystem();
			addCodesDstu2Hl7Org(codeSystem.getCodeSystem().getConcept(), retVal.getConcept());
			return retVal;
		};
		Function<String, org.hl7.fhir.r5.model.ValueSet> valueSetLoader = t -> {
			org.hl7.fhir.dstu2.model.ValueSet valueSet = (org.hl7.fhir.dstu2.model.ValueSet) theRootValidationSupport.fetchValueSet(t);
			return ValueSet10_50.convertValueSet(valueSet);
		};

		org.hl7.fhir.r5.model.ValueSet input = ValueSet10_50.convertValueSet(theInput);
		org.hl7.fhir.r5.model.ValueSet output = expandValueSetR5(input, codeSystemLoader, valueSetLoader);
		return (output);
	}


	@Override
	public boolean isCodeSystemSupported(IValidationSupport theRootValidationSupport, String theSystem) {
		if (isBlank(theSystem)) {
			return false;
		}

		IBaseResource cs = theRootValidationSupport.fetchCodeSystem(theSystem);

		if (!myCtx.getVersion().getVersion().isEqualOrNewerThan(FhirVersionEnum.DSTU2_1)) {
			return cs != null;
		}

		if (cs != null) {
			IPrimitiveType<?> content = getFhirContext().newTerser().getSingleValueOrNull(cs, "content", IPrimitiveType.class);
			if (!"not-present".equals(content.getValueAsString())) {
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean isValueSetSupported(IValidationSupport theRootValidationSupport, String theValueSetUrl) {
		return isNotBlank(theValueSetUrl) && theRootValidationSupport.fetchValueSet(theValueSetUrl) != null;
	}


	private void addCodesDstu2Hl7Org(List<ValueSet.ConceptDefinitionComponent> theSourceList, List<CodeSystem.ConceptDefinitionComponent> theTargetList) {
		for (ValueSet.ConceptDefinitionComponent nextSource : theSourceList) {
			CodeSystem.ConceptDefinitionComponent targetConcept = new CodeSystem.ConceptDefinitionComponent().setCode(nextSource.getCode()).setDisplay(nextSource.getDisplay());
			theTargetList.add(targetConcept);
			addCodesDstu2Hl7Org(nextSource.getConcept(), targetConcept.getConcept());
		}
	}

	@Nullable
	private org.hl7.fhir.r5.model.ValueSet expandValueSetDstu3(IValidationSupport theRootValidationSupport, org.hl7.fhir.dstu3.model.ValueSet theInput) {
		Function<String, org.hl7.fhir.r5.model.CodeSystem> codeSystemLoader = t -> {
			org.hl7.fhir.dstu3.model.CodeSystem codeSystem = (org.hl7.fhir.dstu3.model.CodeSystem) theRootValidationSupport.fetchCodeSystem(t);
			return CodeSystem30_50.convertCodeSystem(codeSystem);
		};
		Function<String, org.hl7.fhir.r5.model.ValueSet> valueSetLoader = t -> {
			org.hl7.fhir.dstu3.model.ValueSet valueSet = (org.hl7.fhir.dstu3.model.ValueSet) theRootValidationSupport.fetchValueSet(t);
			return ValueSet30_50.convertValueSet(valueSet);
		};

		org.hl7.fhir.r5.model.ValueSet input = ValueSet30_50.convertValueSet(theInput);
		org.hl7.fhir.r5.model.ValueSet output = expandValueSetR5(input, codeSystemLoader, valueSetLoader);
		return (output);
	}

	@Nullable
	private org.hl7.fhir.r5.model.ValueSet expandValueSetR4(IValidationSupport theRootValidationSupport, org.hl7.fhir.r4.model.ValueSet theInput) {
		Function<String, org.hl7.fhir.r5.model.CodeSystem> codeSystemLoader = t -> {
			org.hl7.fhir.r4.model.CodeSystem codeSystem = (org.hl7.fhir.r4.model.CodeSystem) theRootValidationSupport.fetchCodeSystem(t);
			return CodeSystem40_50.convertCodeSystem(codeSystem);
		};
		Function<String, org.hl7.fhir.r5.model.ValueSet> valueSetLoader = t -> {
			org.hl7.fhir.r4.model.ValueSet valueSet = (org.hl7.fhir.r4.model.ValueSet) theRootValidationSupport.fetchValueSet(t);
			return ValueSet40_50.convertValueSet(valueSet);
		};

		org.hl7.fhir.r5.model.ValueSet input = ValueSet40_50.convertValueSet(theInput);
		org.hl7.fhir.r5.model.ValueSet output = expandValueSetR5(input, codeSystemLoader, valueSetLoader);
		return (output);
	}

	@Nullable
	private org.hl7.fhir.r5.model.ValueSet expandValueSetR5(IValidationSupport theRootValidationSupport, org.hl7.fhir.r5.model.ValueSet theInput) {
		Function<String, org.hl7.fhir.r5.model.CodeSystem> codeSystemLoader = t -> (org.hl7.fhir.r5.model.CodeSystem) theRootValidationSupport.fetchCodeSystem(t);
		Function<String, org.hl7.fhir.r5.model.ValueSet> valueSetLoader = t -> (org.hl7.fhir.r5.model.ValueSet) theRootValidationSupport.fetchValueSet(t);

		return expandValueSetR5(theInput, codeSystemLoader, valueSetLoader);
	}

	@Nullable
	private org.hl7.fhir.r5.model.ValueSet expandValueSetR5(org.hl7.fhir.r5.model.ValueSet theInput, Function<String, CodeSystem> theCodeSystemLoader, Function<String, org.hl7.fhir.r5.model.ValueSet> theValueSetLoader) {
		Set<VersionIndependentConcept> concepts = new HashSet<>();

		try {
			expandValueSetR5IncludeOrExclude(concepts, theCodeSystemLoader, theValueSetLoader, theInput.getCompose().getInclude(), true);
			expandValueSetR5IncludeOrExclude(concepts, theCodeSystemLoader, theValueSetLoader, theInput.getCompose().getExclude(), false);
		} catch (ExpansionCouldNotBeCompletedInternallyException e) {
			return null;
		}

		org.hl7.fhir.r5.model.ValueSet retVal = new org.hl7.fhir.r5.model.ValueSet();
		for (VersionIndependentConcept next : concepts) {
			org.hl7.fhir.r5.model.ValueSet.ValueSetExpansionContainsComponent contains = retVal.getExpansion().addContains();
			contains.setSystem(next.getSystem());
			contains.setCode(next.getCode());
			contains.setDisplay(next.getDisplay());
		}

		return retVal;
	}

	private void expandValueSetR5IncludeOrExclude(Set<VersionIndependentConcept> theConcepts, Function<String, CodeSystem> theCodeSystemLoader, Function<String, org.hl7.fhir.r5.model.ValueSet> theValueSetLoader, List<org.hl7.fhir.r5.model.ValueSet.ConceptSetComponent> theComposeList, boolean theComposeListIsInclude) throws ExpansionCouldNotBeCompletedInternallyException {
		for (org.hl7.fhir.r5.model.ValueSet.ConceptSetComponent nextInclude : theComposeList) {

			List<VersionIndependentConcept> nextCodeList = new ArrayList<>();
			String system = nextInclude.getSystem();
			if (isNotBlank(system)) {
				CodeSystem codeSystem = theCodeSystemLoader.apply(system);
				if (codeSystem == null) {
					throw new ExpansionCouldNotBeCompletedInternallyException();
				}
				if (codeSystem.getContent() == CodeSystem.CodeSystemContentMode.NOTPRESENT) {
					throw new ExpansionCouldNotBeCompletedInternallyException();
				}

				Set<String> wantCodes;
				if (nextInclude.getConcept().isEmpty()) {
					wantCodes = null;
				} else {
					wantCodes = nextInclude.getConcept().stream().map(t -> t.getCode()).collect(Collectors.toSet());
				}

				addCodes(system, codeSystem.getConcept(), nextCodeList, wantCodes);
			}

			for (CanonicalType nextValueSetInclude : nextInclude.getValueSet()) {
				org.hl7.fhir.r5.model.ValueSet vs = theValueSetLoader.apply(nextValueSetInclude.getValueAsString());
				if (vs != null) {
					org.hl7.fhir.r5.model.ValueSet subExpansion = expandValueSetR5(vs, theCodeSystemLoader, theValueSetLoader);
					if (subExpansion == null) {
						throw new ExpansionCouldNotBeCompletedInternallyException();
					}
					for (org.hl7.fhir.r5.model.ValueSet.ValueSetExpansionContainsComponent next : subExpansion.getExpansion().getContains()) {
						nextCodeList.add(new VersionIndependentConcept(next.getSystem(), next.getCode(), next.getDisplay()));
					}
				}
			}

			if (theComposeListIsInclude) {
				theConcepts.addAll(nextCodeList);
			} else {
				theConcepts.removeAll(nextCodeList);
			}

		}

	}

	private void addCodes(String theSystem, List<CodeSystem.ConceptDefinitionComponent> theSource, List<VersionIndependentConcept> theTarget, Set<String> theCodeFilter) {
		for (CodeSystem.ConceptDefinitionComponent next : theSource) {
			if (isNotBlank(next.getCode())) {
				if (theCodeFilter == null || theCodeFilter.contains(next.getCode())) {
					theTarget.add(new VersionIndependentConcept(theSystem, next.getCode(), next.getDisplay()));
				}
			}
			addCodes(theSystem, next.getConcept(), theTarget, theCodeFilter);
		}
	}

	private static class ExpansionCouldNotBeCompletedInternallyException extends Exception {

	}

	private static void flattenAndConvertCodesDstu2(List<org.hl7.fhir.dstu2.model.ValueSet.ValueSetExpansionContainsComponent> theInput, List<VersionIndependentConcept> theVersionIndependentConcepts) {
		for (org.hl7.fhir.dstu2.model.ValueSet.ValueSetExpansionContainsComponent next : theInput) {
			theVersionIndependentConcepts.add(new VersionIndependentConcept(next.getSystem(), next.getCode(), next.getDisplay()));
			flattenAndConvertCodesDstu2(next.getContains(), theVersionIndependentConcepts);
		}
	}

	private static void flattenAndConvertCodesDstu3(List<org.hl7.fhir.dstu3.model.ValueSet.ValueSetExpansionContainsComponent> theInput, List<VersionIndependentConcept> theVersionIndependentConcepts) {
		for (org.hl7.fhir.dstu3.model.ValueSet.ValueSetExpansionContainsComponent next : theInput) {
			theVersionIndependentConcepts.add(new VersionIndependentConcept(next.getSystem(), next.getCode(), next.getDisplay()));
			flattenAndConvertCodesDstu3(next.getContains(), theVersionIndependentConcepts);
		}
	}

	private static void flattenAndConvertCodesR4(List<org.hl7.fhir.r4.model.ValueSet.ValueSetExpansionContainsComponent> theInput, List<VersionIndependentConcept> theVersionIndependentConcepts) {
		for (org.hl7.fhir.r4.model.ValueSet.ValueSetExpansionContainsComponent next : theInput) {
			theVersionIndependentConcepts.add(new VersionIndependentConcept(next.getSystem(), next.getCode(), next.getDisplay()));
			flattenAndConvertCodesR4(next.getContains(), theVersionIndependentConcepts);
		}
	}

	private static void flattenAndConvertCodesR5(List<org.hl7.fhir.r5.model.ValueSet.ValueSetExpansionContainsComponent> theInput, List<VersionIndependentConcept> theVersionIndependentConcepts) {
		for (org.hl7.fhir.r5.model.ValueSet.ValueSetExpansionContainsComponent next : theInput) {
			theVersionIndependentConcepts.add(new VersionIndependentConcept(next.getSystem(), next.getCode(), next.getDisplay()));
			flattenAndConvertCodesR5(next.getContains(), theVersionIndependentConcepts);
		}
	}

}
