package org.hl7.fhir.common.hapi.validation.support;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValidationSupportContext;
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
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.defaultString;
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
	public ValueSetExpansionOutcome expandValueSet(ValidationSupportContext theValidationSupportContext, ValueSetExpansionOptions theExpansionOptions, IBaseResource theValueSetToExpand) {

		org.hl7.fhir.r5.model.ValueSet expansionR5 = expandValueSetToCanonical(theValidationSupportContext, theValueSetToExpand, null, null);
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

	private org.hl7.fhir.r5.model.ValueSet expandValueSetToCanonical(ValidationSupportContext theValidationSupportContext, IBaseResource theValueSetToExpand, @Nullable String theWantSystem, @Nullable String theWantCode) {
		org.hl7.fhir.r5.model.ValueSet expansionR5;
		switch (myCtx.getVersion().getVersion()) {
			case DSTU2:
			case DSTU2_HL7ORG: {
				expansionR5 = expandValueSetDstu2Hl7Org(theValidationSupportContext, (ValueSet) theValueSetToExpand, theWantSystem, theWantCode);
				break;
			}
			case DSTU3: {
				expansionR5 = expandValueSetDstu3(theValidationSupportContext, (org.hl7.fhir.dstu3.model.ValueSet) theValueSetToExpand, theWantSystem, theWantCode);
				break;
			}
			case R4: {
				expansionR5 = expandValueSetR4(theValidationSupportContext, (org.hl7.fhir.r4.model.ValueSet) theValueSetToExpand, theWantSystem, theWantCode);
				break;
			}
			case R5: {
				expansionR5 = expandValueSetR5(theValidationSupportContext, (org.hl7.fhir.r5.model.ValueSet) theValueSetToExpand);
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
	public CodeValidationResult
	validateCodeInValueSet(ValidationSupportContext theValidationSupportContext, ConceptValidationOptions theOptions, String theCodeSystem, String theCode, String theDisplay, @Nonnull IBaseResource theValueSet) {
	org.hl7.fhir.r5.model.ValueSet expansion = expandValueSetToCanonical(theValidationSupportContext, theValueSet, theCodeSystem, theCode);
		if (expansion == null) {
			return null;
		}
		return validateCodeInExpandedValueSet(theValidationSupportContext, theOptions, theCodeSystem, theCode, expansion);
	}


	@Override
	public CodeValidationResult validateCode(ValidationSupportContext theValidationSupportContext, ConceptValidationOptions theOptions, String theCodeSystem, String theCode, String theDisplay, String theValueSetUrl) {
		IBaseResource vs;
		if (isNotBlank(theValueSetUrl)) {
			vs = theValidationSupportContext.getRootValidationSupport().fetchValueSet(theValueSetUrl);
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

		ValueSetExpansionOutcome valueSetExpansionOutcome = expandValueSet(theValidationSupportContext, null, vs);
		if (valueSetExpansionOutcome == null) {
			return null;
		}

		IBaseResource expansion = valueSetExpansionOutcome.getValueSet();

		return validateCodeInExpandedValueSet(theValidationSupportContext, theOptions, theCodeSystem, theCode, expansion);

	}

	private CodeValidationResult validateCodeInExpandedValueSet(ValidationSupportContext theValidationSupportContext, ConceptValidationOptions theOptions, String theCodeSystem, String theCode, IBaseResource theExpansion) {
		assert theExpansion != null;

		boolean caseSensitive = true;
		IBaseResource system = null;
		if (!theOptions.isInferSystem() && isNotBlank(theCodeSystem)) {
			system = theValidationSupportContext.getRootValidationSupport().fetchCodeSystem(theCodeSystem);
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
		String codeSystemContentMode = null;
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
					codeSystemContentMode = systemDstu3.getContentElement().getValueAsString();
					break;
				}
				case R4: {
					org.hl7.fhir.r4.model.CodeSystem systemR4 = (org.hl7.fhir.r4.model.CodeSystem) system;
					caseSensitive = systemR4.getCaseSensitive();
					codeSystemName = systemR4.getName();
					codeSystemVersion = systemR4.getVersion();
					codeSystemContentMode = systemR4.getContentElement().getValueAsString();
					break;
				}
				case R5: {
					CodeSystem systemR5 = (CodeSystem) system;
					caseSensitive = systemR5.getCaseSensitive();
					codeSystemName = systemR5.getName();
					codeSystemVersion = systemR5.getVersion();
					codeSystemContentMode = systemR5.getContentElement().getValueAsString();
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
				codeMatches = defaultString(theCode).equals(nextExpansionCode.getCode());
			} else {
				codeMatches = defaultString(theCode).equalsIgnoreCase(nextExpansionCode.getCode());
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

		ValidationMessage.IssueSeverity severity;
		String message;
		if ("fragment".equals(codeSystemContentMode)) {
			severity = ValidationMessage.IssueSeverity.WARNING;
			message = "Unknown code in fragment CodeSystem '" + (isNotBlank(theCodeSystem) ? theCodeSystem + "#" : "") + theCode + "'";
		} else {
			severity = ValidationMessage.IssueSeverity.ERROR;
			message = "Unknown code '" + (isNotBlank(theCodeSystem) ? theCodeSystem + "#" : "") + theCode + "'";
		}

		return new CodeValidationResult()
			.setSeverityCode(severity.toCode())
			.setMessage(message);
	}

	@Override
	public LookupCodeResult lookupCode(ValidationSupportContext theValidationSupportContext, String theSystem, String theCode) {
		return validateCode(theValidationSupportContext, new ConceptValidationOptions(), theSystem, theCode, null, null).asLookupCodeResult(theSystem, theCode);
	}

	@Nullable
	private org.hl7.fhir.r5.model.ValueSet expandValueSetDstu2Hl7Org(ValidationSupportContext theValidationSupportContext, ValueSet theInput, @Nullable String theWantSystem, @Nullable String theWantCode) {
		Function<String, CodeSystem> codeSystemLoader = t -> {
			org.hl7.fhir.dstu2.model.ValueSet codeSystem = (org.hl7.fhir.dstu2.model.ValueSet) theValidationSupportContext.getRootValidationSupport().fetchCodeSystem(t);
			CodeSystem retVal = new CodeSystem();
			addCodesDstu2Hl7Org(codeSystem.getCodeSystem().getConcept(), retVal.getConcept());
			return retVal;
		};
		Function<String, org.hl7.fhir.r5.model.ValueSet> valueSetLoader = t -> {
			org.hl7.fhir.dstu2.model.ValueSet valueSet = (org.hl7.fhir.dstu2.model.ValueSet) theValidationSupportContext.getRootValidationSupport().fetchValueSet(t);
			return ValueSet10_50.convertValueSet(valueSet);
		};

		org.hl7.fhir.r5.model.ValueSet input = ValueSet10_50.convertValueSet(theInput);
		org.hl7.fhir.r5.model.ValueSet output = expandValueSetR5(theValidationSupportContext, input, codeSystemLoader, valueSetLoader, theWantSystem, theWantCode);
		return (output);
	}


	@Override
	public boolean isCodeSystemSupported(ValidationSupportContext theValidationSupportContext, String theSystem) {
		if (isBlank(theSystem)) {
			return false;
		}

		IBaseResource cs = theValidationSupportContext.getRootValidationSupport().fetchCodeSystem(theSystem);

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
	public boolean isValueSetSupported(ValidationSupportContext theValidationSupportContext, String theValueSetUrl) {
		return isNotBlank(theValueSetUrl) && theValidationSupportContext.getRootValidationSupport().fetchValueSet(theValueSetUrl) != null;
	}


	private void addCodesDstu2Hl7Org(List<ValueSet.ConceptDefinitionComponent> theSourceList, List<CodeSystem.ConceptDefinitionComponent> theTargetList) {
		for (ValueSet.ConceptDefinitionComponent nextSource : theSourceList) {
			CodeSystem.ConceptDefinitionComponent targetConcept = new CodeSystem.ConceptDefinitionComponent().setCode(nextSource.getCode()).setDisplay(nextSource.getDisplay());
			theTargetList.add(targetConcept);
			addCodesDstu2Hl7Org(nextSource.getConcept(), targetConcept.getConcept());
		}
	}

	@Nullable
	private org.hl7.fhir.r5.model.ValueSet expandValueSetDstu3(ValidationSupportContext theValidationSupportContext, org.hl7.fhir.dstu3.model.ValueSet theInput, @Nullable String theWantSystem, @Nullable String theWantCode) {
		Function<String, org.hl7.fhir.r5.model.CodeSystem> codeSystemLoader = t -> {
			org.hl7.fhir.dstu3.model.CodeSystem codeSystem = (org.hl7.fhir.dstu3.model.CodeSystem) theValidationSupportContext.getRootValidationSupport().fetchCodeSystem(t);
			return CodeSystem30_50.convertCodeSystem(codeSystem);
		};
		Function<String, org.hl7.fhir.r5.model.ValueSet> valueSetLoader = t -> {
			org.hl7.fhir.dstu3.model.ValueSet valueSet = (org.hl7.fhir.dstu3.model.ValueSet) theValidationSupportContext.getRootValidationSupport().fetchValueSet(t);
			return ValueSet30_50.convertValueSet(valueSet);
		};

		org.hl7.fhir.r5.model.ValueSet input = ValueSet30_50.convertValueSet(theInput);
		org.hl7.fhir.r5.model.ValueSet output = expandValueSetR5(theValidationSupportContext, input, codeSystemLoader, valueSetLoader, theWantSystem, theWantCode);
		return (output);
	}

	@Nullable
	private org.hl7.fhir.r5.model.ValueSet expandValueSetR4(ValidationSupportContext theValidationSupportContext, org.hl7.fhir.r4.model.ValueSet theInput, @Nullable String theWantSystem, @Nullable String theWantCode) {
		Function<String, org.hl7.fhir.r5.model.CodeSystem> codeSystemLoader = t -> {
			org.hl7.fhir.r4.model.CodeSystem codeSystem = (org.hl7.fhir.r4.model.CodeSystem) theValidationSupportContext.getRootValidationSupport().fetchCodeSystem(t);
			return CodeSystem40_50.convertCodeSystem(codeSystem);
		};
		Function<String, org.hl7.fhir.r5.model.ValueSet> valueSetLoader = t -> {
			org.hl7.fhir.r4.model.ValueSet valueSet = (org.hl7.fhir.r4.model.ValueSet) theValidationSupportContext.getRootValidationSupport().fetchValueSet(t);
			return ValueSet40_50.convertValueSet(valueSet);
		};

		org.hl7.fhir.r5.model.ValueSet input = ValueSet40_50.convertValueSet(theInput);
		org.hl7.fhir.r5.model.ValueSet output = expandValueSetR5(theValidationSupportContext, input, codeSystemLoader, valueSetLoader, theWantSystem, theWantCode);
		return (output);
	}

	@Nullable
	private org.hl7.fhir.r5.model.ValueSet expandValueSetR5(ValidationSupportContext theValidationSupportContext, org.hl7.fhir.r5.model.ValueSet theInput) {
		Function<String, org.hl7.fhir.r5.model.CodeSystem> codeSystemLoader = t -> (org.hl7.fhir.r5.model.CodeSystem) theValidationSupportContext.getRootValidationSupport().fetchCodeSystem(t);
		Function<String, org.hl7.fhir.r5.model.ValueSet> valueSetLoader = t -> (org.hl7.fhir.r5.model.ValueSet) theValidationSupportContext.getRootValidationSupport().fetchValueSet(t);

		return expandValueSetR5(theValidationSupportContext, theInput, codeSystemLoader, valueSetLoader, null, null);
	}

	@Nullable
	private org.hl7.fhir.r5.model.ValueSet expandValueSetR5(ValidationSupportContext theValidationSupportContext, org.hl7.fhir.r5.model.ValueSet theInput, Function<String, CodeSystem> theCodeSystemLoader, Function<String, org.hl7.fhir.r5.model.ValueSet> theValueSetLoader, @Nullable String theWantSystem, @Nullable String theWantCode) {
		Set<VersionIndependentConcept> concepts = new HashSet<>();

		try {
			expandValueSetR5IncludeOrExclude(theValidationSupportContext, concepts, theCodeSystemLoader, theValueSetLoader, theInput.getCompose().getInclude(), true, theWantSystem, theWantCode);
			expandValueSetR5IncludeOrExclude(theValidationSupportContext, concepts, theCodeSystemLoader, theValueSetLoader, theInput.getCompose().getExclude(), false, theWantSystem, theWantCode);
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

	private void expandValueSetR5IncludeOrExclude(ValidationSupportContext theValidationSupportContext, Set<VersionIndependentConcept> theConcepts, Function<String, CodeSystem> theCodeSystemLoader, Function<String, org.hl7.fhir.r5.model.ValueSet> theValueSetLoader, List<org.hl7.fhir.r5.model.ValueSet.ConceptSetComponent> theComposeList, boolean theComposeListIsInclude, @Nullable String theWantSystem, @Nullable String theWantCode) throws ExpansionCouldNotBeCompletedInternallyException {
		for (org.hl7.fhir.r5.model.ValueSet.ConceptSetComponent nextInclude : theComposeList) {

			List<VersionIndependentConcept> nextCodeList = new ArrayList<>();
			String system = nextInclude.getSystem();
			if (isNotBlank(system)) {

				if (theWantSystem != null && !theWantSystem.equals(system)) {
					continue;
				}

				CodeSystem codeSystem = theCodeSystemLoader.apply(system);

				Set<String> wantCodes;
				if (nextInclude.getConcept().isEmpty()) {
					wantCodes = null;
				} else {
					wantCodes = nextInclude
						.getConcept()
						.stream().map(t -> t.getCode()).collect(Collectors.toSet());
				}

				boolean ableToHandleCode = false;
				if (codeSystem == null || codeSystem.getContent() == CodeSystem.CodeSystemContentMode.NOTPRESENT) {

					if (theWantCode != null) {
						if (theValidationSupportContext.getRootValidationSupport().isCodeSystemSupported(theValidationSupportContext, system)) {
							LookupCodeResult lookup = theValidationSupportContext.getRootValidationSupport().lookupCode(theValidationSupportContext, system, theWantCode);
							if (lookup != null && lookup.isFound()) {
								CodeSystem.ConceptDefinitionComponent conceptDefinition = new CodeSystem.ConceptDefinitionComponent()
									.addConcept()
									.setCode(theWantCode)
									.setDisplay(lookup.getCodeDisplay());
								List<CodeSystem.ConceptDefinitionComponent> codesList = Collections.singletonList(conceptDefinition);
								addCodes(system, codesList, nextCodeList, wantCodes);
								ableToHandleCode = true;
							}
						} else if (theComposeListIsInclude) {

							/*
							 * If we're doing an expansion specifically looking for a single code, that means we're validating that code.
							 * In the case where we have a ValueSet that explicitly enumerates a collection of codes
							 * (via ValueSet.compose.include.code) in a code system that is unknown we'll assume the code is valid
							 * even iof we can't find the CodeSystem. This is a compromise obviously, since it would be ideal for
							 * CodeSystems to always be known, but realistically there are always going to be CodeSystems that
							 * can't be supplied because of copyright issues, or because they are grammar based. Allowing a VS to
							 * enumerate a set of good codes for them is a nice compromise there.
							 */
							for (org.hl7.fhir.r5.model.ValueSet.ConceptSetComponent next : theComposeList) {
								if (Objects.equals(next.getSystem(), theWantSystem)) {
									Optional<org.hl7.fhir.r5.model.ValueSet.ConceptReferenceComponent> matchingEnumeratedConcept = next.getConcept().stream().filter(t -> Objects.equals(t.getCode(), theWantCode)).findFirst();
									if (matchingEnumeratedConcept.isPresent()) {
										CodeSystem.ConceptDefinitionComponent conceptDefinition = new CodeSystem.ConceptDefinitionComponent()
											.addConcept()
											.setCode(theWantCode)
											.setDisplay(matchingEnumeratedConcept.get().getDisplay());
										List<CodeSystem.ConceptDefinitionComponent> codesList = Collections.singletonList(conceptDefinition);
										addCodes(system, codesList, nextCodeList, wantCodes);
										ableToHandleCode = true;break;
									}
								}
							}

						}
					}

				} else {
					ableToHandleCode = true;
				}

				if (!ableToHandleCode) {
					throw new ExpansionCouldNotBeCompletedInternallyException();
				}

				if (codeSystem != null && codeSystem.getContent() != CodeSystem.CodeSystemContentMode.NOTPRESENT) {
					addCodes(system, codeSystem.getConcept(), nextCodeList, wantCodes);
				}

			}

			for (CanonicalType nextValueSetInclude : nextInclude.getValueSet()) {
				org.hl7.fhir.r5.model.ValueSet vs = theValueSetLoader.apply(nextValueSetInclude.getValueAsString());
				if (vs != null) {
					org.hl7.fhir.r5.model.ValueSet subExpansion = expandValueSetR5(theValidationSupportContext, vs, theCodeSystemLoader, theValueSetLoader, theWantSystem, theWantCode);
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
