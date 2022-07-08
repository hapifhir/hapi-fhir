package org.hl7.fhir.common.hapi.validation.support;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.context.support.ValueSetExpansionOptions;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.util.FhirVersionIndependentConcept;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.convertors.advisors.impl.BaseAdvisor_10_50;
import org.hl7.fhir.convertors.advisors.impl.BaseAdvisor_30_50;
import org.hl7.fhir.convertors.advisors.impl.BaseAdvisor_40_50;
import org.hl7.fhir.convertors.factory.VersionConvertorFactory_10_50;
import org.hl7.fhir.convertors.factory.VersionConvertorFactory_30_50;
import org.hl7.fhir.convertors.factory.VersionConvertorFactory_40_50;
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
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.contains;
import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.substringAfter;
import static org.apache.commons.lang3.StringUtils.substringBefore;

/**
 * This class is a basic in-memory terminology service, designed to expand ValueSets and validate codes
 * completely in-memory. It is suitable for runtime validation purposes where no dedicated terminology
 * service exists (either an internal one such as the HAPI FHIR JPA terminology service, or an
 * external term service API)
 */
public class InMemoryTerminologyServerValidationSupport implements IValidationSupport {
	private static final String OUR_PIPE_CHARACTER = "|";

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
	public ValueSetExpansionOutcome expandValueSet(ValidationSupportContext theValidationSupportContext, ValueSetExpansionOptions theExpansionOptions, @Nonnull IBaseResource theValueSetToExpand) {
		return expandValueSet(theValidationSupportContext, theExpansionOptions, theValueSetToExpand, null, null);
	}

	private ValueSetExpansionOutcome expandValueSet(ValidationSupportContext theValidationSupportContext, ValueSetExpansionOptions theExpansionOptions, IBaseResource theValueSetToExpand, String theWantSystemAndVersion, String theWantCode) {
		org.hl7.fhir.r5.model.ValueSet expansionR5;
		try {
			expansionR5 = expandValueSetToCanonical(theValidationSupportContext, theValueSetToExpand, theWantSystemAndVersion, theWantCode);
		} catch (ExpansionCouldNotBeCompletedInternallyException e) {
			return new ValueSetExpansionOutcome(e.getMessage());
		}
		if (expansionR5 == null) {
			return null;
		}

		IBaseResource expansion;
		switch (myCtx.getVersion().getVersion()) {
			case DSTU2_HL7ORG: {
				expansion = VersionConvertorFactory_10_50.convertResource(expansionR5, new BaseAdvisor_10_50(false));
				break;
			}
			case DSTU3: {
				expansion = VersionConvertorFactory_30_50.convertResource(expansionR5, new BaseAdvisor_30_50(false));
				break;
			}
			case R4: {
				expansion = VersionConvertorFactory_40_50.convertResource(expansionR5, new BaseAdvisor_40_50(false));
				break;
			}
			case R5: {
				expansion = expansionR5;
				break;
			}
			case DSTU2:
			case DSTU2_1:
			default:
				throw new IllegalArgumentException(Msg.code(697) + "Can not handle version: " + myCtx.getVersion().getVersion());
		}

		return new ValueSetExpansionOutcome(expansion);
	}

	private org.hl7.fhir.r5.model.ValueSet expandValueSetToCanonical(ValidationSupportContext theValidationSupportContext, IBaseResource theValueSetToExpand, @Nullable String theWantSystemUrlAndVersion, @Nullable String theWantCode) throws ExpansionCouldNotBeCompletedInternallyException {
		org.hl7.fhir.r5.model.ValueSet expansionR5;
		switch (theValueSetToExpand.getStructureFhirVersionEnum()) {
			case DSTU2: {
				expansionR5 = expandValueSetDstu2(theValidationSupportContext, (ca.uhn.fhir.model.dstu2.resource.ValueSet) theValueSetToExpand, theWantSystemUrlAndVersion, theWantCode);
				break;
			}
			case DSTU2_HL7ORG: {
				expansionR5 = expandValueSetDstu2Hl7Org(theValidationSupportContext, (ValueSet) theValueSetToExpand, theWantSystemUrlAndVersion, theWantCode);
				break;
			}
			case DSTU3: {
				expansionR5 = expandValueSetDstu3(theValidationSupportContext, (org.hl7.fhir.dstu3.model.ValueSet) theValueSetToExpand, theWantSystemUrlAndVersion, theWantCode);
				break;
			}
			case R4: {
				expansionR5 = expandValueSetR4(theValidationSupportContext, (org.hl7.fhir.r4.model.ValueSet) theValueSetToExpand, theWantSystemUrlAndVersion, theWantCode);
				break;
			}
			case R5: {
				expansionR5 = expandValueSetR5(theValidationSupportContext, (org.hl7.fhir.r5.model.ValueSet) theValueSetToExpand, theWantSystemUrlAndVersion, theWantCode);
				break;
			}
			case DSTU2_1:
			default:
				throw new IllegalArgumentException(Msg.code(698) + "Can not handle version: " + myCtx.getVersion().getVersion());
		}

		return expansionR5;
	}

	@Override
	public CodeValidationResult validateCodeInValueSet(ValidationSupportContext theValidationSupportContext, ConceptValidationOptions theOptions, String theCodeSystemUrlAndVersion, String theCode, String theDisplay, @Nonnull IBaseResource theValueSet) {
		org.hl7.fhir.r5.model.ValueSet expansion;
		String vsUrl = CommonCodeSystemsTerminologyService.getValueSetUrl(theValueSet);
		try {
			expansion = expandValueSetToCanonical(theValidationSupportContext, theValueSet, theCodeSystemUrlAndVersion, theCode);
		} catch (ExpansionCouldNotBeCompletedInternallyException e) {
			CodeValidationResult codeValidationResult = new CodeValidationResult();
			codeValidationResult.setSeverityCode("error");

			String msg = "Failed to expand ValueSet '" + vsUrl + "' (in-memory). Could not validate code " + theCodeSystemUrlAndVersion + "#" + theCode;
			if (e.getMessage() != null) {
				msg += ". Error was: " + e.getMessage();
			}

			codeValidationResult.setMessage(msg);
			return codeValidationResult;
		}

		if (expansion == null) {
			return null;
		}

		return validateCodeInExpandedValueSet(theValidationSupportContext, theOptions, theCodeSystemUrlAndVersion, theCode, theDisplay, expansion, vsUrl);
	}


	@Override
	@Nullable
	public CodeValidationResult validateCode(@Nonnull ValidationSupportContext theValidationSupportContext, @Nonnull ConceptValidationOptions theOptions, String theCodeSystem, String theCode, String theDisplay, String theValueSetUrl) {
		IBaseResource vs;
		if (isNotBlank(theValueSetUrl)) {
			vs = theValidationSupportContext.getRootValidationSupport().fetchValueSet(theValueSetUrl);
			if (vs == null) {
				return null;
			}
		} else {
			String codeSystemUrl;
			String codeSystemVersion = null;
			int codeSystemVersionIndex = theCodeSystem.indexOf("|");
			if (codeSystemVersionIndex > -1) {
				codeSystemUrl = theCodeSystem.substring(0, codeSystemVersionIndex);
				codeSystemVersion = theCodeSystem.substring(codeSystemVersionIndex + 1);
			} else {
				codeSystemUrl = theCodeSystem;
			}
			switch (myCtx.getVersion().getVersion()) {
				case DSTU2_HL7ORG:
					vs = new org.hl7.fhir.dstu2.model.ValueSet()
						.setCompose(new org.hl7.fhir.dstu2.model.ValueSet.ValueSetComposeComponent()
							.addInclude(new org.hl7.fhir.dstu2.model.ValueSet.ConceptSetComponent().setSystem(theCodeSystem)));
					break;
				case DSTU3:
					if (codeSystemVersion != null) {
						vs = new org.hl7.fhir.dstu3.model.ValueSet()
							.setCompose(new org.hl7.fhir.dstu3.model.ValueSet.ValueSetComposeComponent()
								.addInclude(new org.hl7.fhir.dstu3.model.ValueSet.ConceptSetComponent().setSystem(codeSystemUrl).setVersion(codeSystemVersion)));
					} else {
						vs = new org.hl7.fhir.dstu3.model.ValueSet()
							.setCompose(new org.hl7.fhir.dstu3.model.ValueSet.ValueSetComposeComponent()
								.addInclude(new org.hl7.fhir.dstu3.model.ValueSet.ConceptSetComponent().setSystem(theCodeSystem)));
					}
					break;
				case R4:
					if (codeSystemVersion != null) {
						vs = new org.hl7.fhir.r4.model.ValueSet()
							.setCompose(new org.hl7.fhir.r4.model.ValueSet.ValueSetComposeComponent()
								.addInclude(new org.hl7.fhir.r4.model.ValueSet.ConceptSetComponent().setSystem(codeSystemUrl).setVersion(codeSystemVersion)));
					} else {
						vs = new org.hl7.fhir.r4.model.ValueSet()
							.setCompose(new org.hl7.fhir.r4.model.ValueSet.ValueSetComposeComponent()
								.addInclude(new org.hl7.fhir.r4.model.ValueSet.ConceptSetComponent().setSystem(theCodeSystem)));
					}
					break;
				case R5:
					if (codeSystemVersion != null) {
						vs = new org.hl7.fhir.r5.model.ValueSet()
							.setCompose(new org.hl7.fhir.r5.model.ValueSet.ValueSetComposeComponent()
								.addInclude(new org.hl7.fhir.r5.model.ValueSet.ConceptSetComponent().setSystem(codeSystemUrl).setVersion(codeSystemVersion)));
					} else {
						vs = new org.hl7.fhir.r5.model.ValueSet()
							.setCompose(new org.hl7.fhir.r5.model.ValueSet.ValueSetComposeComponent()
								.addInclude(new org.hl7.fhir.r5.model.ValueSet.ConceptSetComponent().setSystem(theCodeSystem)));
					}
					break;
				case DSTU2:
				case DSTU2_1:
				default:
					throw new IllegalArgumentException(Msg.code(699) + "Can not handle version: " + myCtx.getVersion().getVersion());
			}
		}

		ValueSetExpansionOutcome valueSetExpansionOutcome = expandValueSet(theValidationSupportContext, null, vs, theCodeSystem, theCode);
		if (valueSetExpansionOutcome == null) {
			return null;
		}

		if (valueSetExpansionOutcome.getError() != null) {
			return new CodeValidationResult()
				.setSeverity(IssueSeverity.ERROR)
				.setMessage(valueSetExpansionOutcome.getError());
		}

		IBaseResource expansion = valueSetExpansionOutcome.getValueSet();
		return validateCodeInExpandedValueSet(theValidationSupportContext, theOptions, theCodeSystem, theCode, theDisplay, expansion, theValueSetUrl);
	}

	private CodeValidationResult validateCodeInExpandedValueSet(ValidationSupportContext theValidationSupportContext, ConceptValidationOptions theOptions, String theCodeSystemUrlAndVersionToValidate, String theCodeToValidate, String theDisplayToValidate, IBaseResource theExpansion, String theValueSetUrl) {
		assert theExpansion != null;

		boolean caseSensitive = true;
		IBaseResource codeSystemToValidateResource = null;
		if (!theOptions.isInferSystem() && isNotBlank(theCodeSystemUrlAndVersionToValidate)) {
			codeSystemToValidateResource = theValidationSupportContext.getRootValidationSupport().fetchCodeSystem(theCodeSystemUrlAndVersionToValidate);
		}

		List<FhirVersionIndependentConcept> codes = new ArrayList<>();
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
				throw new IllegalArgumentException(Msg.code(700) + "Can not handle version: " + myCtx.getVersion().getVersion());
		}

		String codeSystemResourceName = null;
		String codeSystemResourceVersion = null;
		String codeSystemResourceContentMode = null;
		if (codeSystemToValidateResource != null) {
			switch (codeSystemToValidateResource.getStructureFhirVersionEnum()) {
				case DSTU2_HL7ORG: {
					caseSensitive = true;
					break;
				}
				case DSTU3: {
					org.hl7.fhir.dstu3.model.CodeSystem systemDstu3 = (org.hl7.fhir.dstu3.model.CodeSystem) codeSystemToValidateResource;
					caseSensitive = systemDstu3.getCaseSensitive();
					codeSystemResourceName = systemDstu3.getName();
					codeSystemResourceVersion = systemDstu3.getVersion();
					codeSystemResourceContentMode = systemDstu3.getContentElement().getValueAsString();
					break;
				}
				case R4: {
					org.hl7.fhir.r4.model.CodeSystem systemR4 = (org.hl7.fhir.r4.model.CodeSystem) codeSystemToValidateResource;
					caseSensitive = systemR4.getCaseSensitive();
					codeSystemResourceName = systemR4.getName();
					codeSystemResourceVersion = systemR4.getVersion();
					codeSystemResourceContentMode = systemR4.getContentElement().getValueAsString();
					break;
				}
				case R5: {
					CodeSystem systemR5 = (CodeSystem) codeSystemToValidateResource;
					caseSensitive = systemR5.getCaseSensitive();
					codeSystemResourceName = systemR5.getName();
					codeSystemResourceVersion = systemR5.getVersion();
					codeSystemResourceContentMode = systemR5.getContentElement().getValueAsString();
					break;
				}
				case DSTU2:
				case DSTU2_1:
				default:
					throw new IllegalArgumentException(Msg.code(701) + "Can not handle version: " + myCtx.getVersion().getVersion());
			}
		}

		String codeSystemUrlToValidate = null;
		String codeSystemVersionToValidate = null;
		if (theCodeSystemUrlAndVersionToValidate != null) {
			int versionIndex = theCodeSystemUrlAndVersionToValidate.indexOf("|");
			if (versionIndex > -1) {
				codeSystemUrlToValidate = theCodeSystemUrlAndVersionToValidate.substring(0, versionIndex);
				codeSystemVersionToValidate = theCodeSystemUrlAndVersionToValidate.substring(versionIndex + 1);
			} else {
				codeSystemUrlToValidate = theCodeSystemUrlAndVersionToValidate;
			}
		}
		for (FhirVersionIndependentConcept nextExpansionCode : codes) {

			boolean codeMatches;
			if (caseSensitive) {
				codeMatches = defaultString(theCodeToValidate).equals(nextExpansionCode.getCode());
			} else {
				codeMatches = defaultString(theCodeToValidate).equalsIgnoreCase(nextExpansionCode.getCode());
			}
			if (codeMatches) {
				if (theOptions.isInferSystem() || (nextExpansionCode.getSystem().equals(codeSystemUrlToValidate) && (codeSystemVersionToValidate == null || codeSystemVersionToValidate.equals(nextExpansionCode.getSystemVersion())))) {
					String csVersion = codeSystemResourceVersion;
					if (isNotBlank(nextExpansionCode.getSystemVersion())) {
						csVersion = nextExpansionCode.getSystemVersion();
					}
					if (!theOptions.isValidateDisplay() || (isBlank(nextExpansionCode.getDisplay()) || isBlank(theDisplayToValidate) || nextExpansionCode.getDisplay().equals(theDisplayToValidate))) {
						CodeValidationResult codeValidationResult = new CodeValidationResult()
							.setCode(theCodeToValidate)
							.setDisplay(nextExpansionCode.getDisplay())
							.setCodeSystemName(codeSystemResourceName)
							.setCodeSystemVersion(csVersion);
						if (isNotBlank(theValueSetUrl)) {
							codeValidationResult.setMessage("Code was validated against in-memory expansion of ValueSet: " + theValueSetUrl);
						}
						return codeValidationResult;
					} else {
						String message = "Concept Display \"" + theDisplayToValidate + "\" does not match expected \"" + nextExpansionCode.getDisplay() + "\"";
						if (isNotBlank(theValueSetUrl)) {
							message += " for in-memory expansion of ValueSet: " + theValueSetUrl;
						}
						return new CodeValidationResult()
							.setSeverity(IssueSeverity.ERROR)
							.setDisplay(nextExpansionCode.getDisplay())
							.setMessage(message)
							.setCodeSystemName(codeSystemResourceName)
							.setCodeSystemVersion(csVersion);
					}
				}
			}
		}

		ValidationMessage.IssueSeverity severity;
		String message;
		if ("fragment".equals(codeSystemResourceContentMode)) {
			severity = ValidationMessage.IssueSeverity.WARNING;
			message = "Unknown code in fragment CodeSystem '" + (isNotBlank(theCodeSystemUrlAndVersionToValidate) ? theCodeSystemUrlAndVersionToValidate + "#" : "") + theCodeToValidate + "'";
		} else {
			severity = ValidationMessage.IssueSeverity.ERROR;
			message = "Unknown code '" + (isNotBlank(theCodeSystemUrlAndVersionToValidate) ? theCodeSystemUrlAndVersionToValidate + "#" : "") + theCodeToValidate + "'";
		}
		if (isNotBlank(theValueSetUrl)) {
			message += " for in-memory expansion of ValueSet '" + theValueSetUrl + "'";
		}

		return new CodeValidationResult()
			.setSeverityCode(severity.toCode())
			.setMessage(message);
	}

	@Override
	public LookupCodeResult lookupCode(ValidationSupportContext theValidationSupportContext, String theSystem, String theCode, String theDisplayLanguage) {
		CodeValidationResult codeValidationResult = validateCode(theValidationSupportContext, new ConceptValidationOptions(), theSystem, theCode, null, null);
		if (codeValidationResult == null) {
			return null;
		}
		return codeValidationResult.asLookupCodeResult(theSystem, theCode);
	}

	@Nullable
	private org.hl7.fhir.r5.model.ValueSet expandValueSetDstu2Hl7Org(ValidationSupportContext theValidationSupportContext, ValueSet theInput, @Nullable String theWantSystemUrlAndVersion, @Nullable String theWantCode) throws ExpansionCouldNotBeCompletedInternallyException {
		org.hl7.fhir.r5.model.ValueSet input = (org.hl7.fhir.r5.model.ValueSet) VersionConvertorFactory_10_50.convertResource(theInput, new BaseAdvisor_10_50(false));
		return (expandValueSetR5(theValidationSupportContext, input, theWantSystemUrlAndVersion, theWantCode));
	}

	@Nullable
	private org.hl7.fhir.r5.model.ValueSet expandValueSetDstu2(ValidationSupportContext theValidationSupportContext, ca.uhn.fhir.model.dstu2.resource.ValueSet theInput, @Nullable String theWantSystemUrlAndVersion, @Nullable String theWantCode) throws ExpansionCouldNotBeCompletedInternallyException {
		IParser parserRi = FhirContext.forCached(FhirVersionEnum.DSTU2_HL7ORG).newJsonParser();
		IParser parserHapi = FhirContext.forDstu2Cached().newJsonParser();

		org.hl7.fhir.dstu2.model.ValueSet valueSetRi = parserRi.parseResource(org.hl7.fhir.dstu2.model.ValueSet.class, parserHapi.encodeResourceToString(theInput));
		org.hl7.fhir.r5.model.ValueSet input = (org.hl7.fhir.r5.model.ValueSet) VersionConvertorFactory_10_50.convertResource(valueSetRi, new BaseAdvisor_10_50(false));
		return (expandValueSetR5(theValidationSupportContext, input, theWantSystemUrlAndVersion, theWantCode));
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
			return !"not-present".equals(content.getValueAsString());
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

	private void addCodesDstu2(List<ca.uhn.fhir.model.dstu2.resource.ValueSet.CodeSystemConcept> theSourceList, List<CodeSystem.ConceptDefinitionComponent> theTargetList) {
		for (ca.uhn.fhir.model.dstu2.resource.ValueSet.CodeSystemConcept nextSource : theSourceList) {
			CodeSystem.ConceptDefinitionComponent targetConcept = new CodeSystem.ConceptDefinitionComponent().setCode(nextSource.getCode()).setDisplay(nextSource.getDisplay());
			theTargetList.add(targetConcept);
			addCodesDstu2(nextSource.getConcept(), targetConcept.getConcept());
		}
	}

	@Nullable
	private org.hl7.fhir.r5.model.ValueSet expandValueSetDstu3(ValidationSupportContext theValidationSupportContext, org.hl7.fhir.dstu3.model.ValueSet theInput, @Nullable String theWantSystemUrlAndVersion, @Nullable String theWantCode) throws ExpansionCouldNotBeCompletedInternallyException {
		org.hl7.fhir.r5.model.ValueSet input = (org.hl7.fhir.r5.model.ValueSet) VersionConvertorFactory_30_50.convertResource(theInput, new BaseAdvisor_30_50(false));
		return (expandValueSetR5(theValidationSupportContext, input, theWantSystemUrlAndVersion, theWantCode));
	}

	@Nullable
	private org.hl7.fhir.r5.model.ValueSet expandValueSetR4(ValidationSupportContext theValidationSupportContext, org.hl7.fhir.r4.model.ValueSet theInput, @Nullable String theWantSystemUrlAndVersion, @Nullable String theWantCode) throws ExpansionCouldNotBeCompletedInternallyException {
		org.hl7.fhir.r5.model.ValueSet input = (org.hl7.fhir.r5.model.ValueSet) VersionConvertorFactory_40_50.convertResource(theInput, new BaseAdvisor_40_50(false));
		return expandValueSetR5(theValidationSupportContext, input, theWantSystemUrlAndVersion, theWantCode);
	}

	@Nullable
	private org.hl7.fhir.r5.model.ValueSet expandValueSetR5(ValidationSupportContext theValidationSupportContext, org.hl7.fhir.r5.model.ValueSet theInput) throws ExpansionCouldNotBeCompletedInternallyException {
		return expandValueSetR5(theValidationSupportContext, theInput, null, null);
	}

	@Nullable
	private org.hl7.fhir.r5.model.ValueSet expandValueSetR5(ValidationSupportContext theValidationSupportContext, org.hl7.fhir.r5.model.ValueSet theInput, @Nullable String theWantSystemUrlAndVersion, @Nullable String theWantCode) throws ExpansionCouldNotBeCompletedInternallyException {
		Set<FhirVersionIndependentConcept> concepts = new HashSet<>();

		expandValueSetR5IncludeOrExcludes(theValidationSupportContext, concepts, theInput.getCompose().getInclude(), true, theWantSystemUrlAndVersion, theWantCode);
		expandValueSetR5IncludeOrExcludes(theValidationSupportContext, concepts, theInput.getCompose().getExclude(), false, theWantSystemUrlAndVersion, theWantCode);

		org.hl7.fhir.r5.model.ValueSet retVal = new org.hl7.fhir.r5.model.ValueSet();
		for (FhirVersionIndependentConcept next : concepts) {
			org.hl7.fhir.r5.model.ValueSet.ValueSetExpansionContainsComponent contains = retVal.getExpansion().addContains();
			contains.setSystem(next.getSystem());
			contains.setCode(next.getCode());
			contains.setDisplay(next.getDisplay());
			contains.setVersion(next.getSystemVersion());
		}

		return retVal;
	}

	/**
	 * Use with caution - this is not a stable API
	 *
	 * @since 5.6.0
	 */
	public void expandValueSetIncludeOrExclude(ValidationSupportContext theValidationSupportContext, Consumer<FhirVersionIndependentConcept> theConsumer, org.hl7.fhir.r5.model.ValueSet.ConceptSetComponent theIncludeOrExclude) throws ExpansionCouldNotBeCompletedInternallyException {
		expandValueSetR5IncludeOrExclude(theValidationSupportContext, theConsumer, null, null, theIncludeOrExclude);
	}


	private void expandValueSetR5IncludeOrExcludes(ValidationSupportContext theValidationSupportContext, Set<FhirVersionIndependentConcept> theConcepts, List<org.hl7.fhir.r5.model.ValueSet.ConceptSetComponent> theComposeList, boolean theComposeListIsInclude, @Nullable String theWantSystemUrlAndVersion, @Nullable String theWantCode) throws ExpansionCouldNotBeCompletedInternallyException {
		Consumer<FhirVersionIndependentConcept> consumer = c -> {
			if (theComposeListIsInclude) {
				theConcepts.add(c);
			} else {
				theConcepts.remove(c);
			}
		};
		expandValueSetR5IncludeOrExcludes(theValidationSupportContext, consumer, theComposeList, theWantSystemUrlAndVersion, theWantCode);
	}


	private void expandValueSetR5IncludeOrExcludes(ValidationSupportContext theValidationSupportContext, Consumer<FhirVersionIndependentConcept> theConsumer, List<org.hl7.fhir.r5.model.ValueSet.ConceptSetComponent> theComposeList, @Nullable String theWantSystemUrlAndVersion, @Nullable String theWantCode) throws ExpansionCouldNotBeCompletedInternallyException {
		ExpansionCouldNotBeCompletedInternallyException caughtException = null;
		for (org.hl7.fhir.r5.model.ValueSet.ConceptSetComponent nextInclude : theComposeList) {
			try {
				boolean outcome = expandValueSetR5IncludeOrExclude(theValidationSupportContext, theConsumer, theWantSystemUrlAndVersion, theWantCode, nextInclude);
				if (isNotBlank(theWantCode)) {
					if (outcome) {
						return;
					}
				}
			} catch (ExpansionCouldNotBeCompletedInternallyException e) {
				if (isBlank(theWantCode)) {
					throw e;
				} else {
					caughtException = e;
				}
			}
		}
		if (caughtException != null) {
			throw caughtException;
		}
	}

	/**
	 * Returns <code>true</code> if at least one code was added
	 */
	private boolean expandValueSetR5IncludeOrExclude(ValidationSupportContext theValidationSupportContext, Consumer<FhirVersionIndependentConcept> theConsumer, @Nullable String theWantSystemUrlAndVersion, @Nullable String theWantCode, org.hl7.fhir.r5.model.ValueSet.ConceptSetComponent theInclude) throws ExpansionCouldNotBeCompletedInternallyException {

		String wantSystemUrl = null;
		String wantSystemVersion = null;

		if (theWantSystemUrlAndVersion != null) {
			int versionIndex = theWantSystemUrlAndVersion.indexOf(OUR_PIPE_CHARACTER);
			if (versionIndex > -1) {
				wantSystemUrl = theWantSystemUrlAndVersion.substring(0, versionIndex);
				wantSystemVersion = theWantSystemUrlAndVersion.substring(versionIndex + 1);
			} else {
				wantSystemUrl = theWantSystemUrlAndVersion;
			}
		}

		String includeOrExcludeConceptSystemUrl = theInclude.getSystem();
		String includeOrExcludeConceptSystemVersion = theInclude.getVersion();

		Function<String, CodeSystem> codeSystemLoader = newCodeSystemLoader(theValidationSupportContext);
		Function<String, org.hl7.fhir.r5.model.ValueSet> valueSetLoader = newValueSetLoader(theValidationSupportContext);

		List<FhirVersionIndependentConcept> nextCodeList = new ArrayList<>();
		CodeSystem includeOrExcludeSystemResource = null;

		if (isNotBlank(includeOrExcludeConceptSystemUrl)) {

			includeOrExcludeConceptSystemVersion = optionallyPopulateVersionFromUrl(includeOrExcludeConceptSystemUrl, includeOrExcludeConceptSystemVersion);
			includeOrExcludeConceptSystemUrl = substringBefore(includeOrExcludeConceptSystemUrl, OUR_PIPE_CHARACTER);

			if (wantSystemUrl != null && !wantSystemUrl.equals(includeOrExcludeConceptSystemUrl)) {
				return false;
			}

			if (wantSystemVersion != null && !wantSystemVersion.equals(includeOrExcludeConceptSystemVersion)) {
				return false;
			}

			String loadedCodeSystemUrl;
			if (includeOrExcludeConceptSystemVersion != null) {
				loadedCodeSystemUrl = includeOrExcludeConceptSystemUrl + OUR_PIPE_CHARACTER + includeOrExcludeConceptSystemVersion;
			} else {
				loadedCodeSystemUrl = includeOrExcludeConceptSystemUrl;
			}

			includeOrExcludeSystemResource = codeSystemLoader.apply(loadedCodeSystemUrl);

			Set<String> wantCodes;
			if (theInclude.getConcept().isEmpty()) {
				wantCodes = null;
			} else {
				wantCodes = theInclude
					.getConcept()
					.stream().map(t -> t.getCode()).collect(Collectors.toSet());
			}

			boolean ableToHandleCode = false;
			String failureMessage = null;
			FailureType failureType = FailureType.OTHER;

			if (includeOrExcludeSystemResource == null || includeOrExcludeSystemResource.getContent() == CodeSystem.CodeSystemContentMode.NOTPRESENT) {

				if (theWantCode != null) {
					if (theValidationSupportContext.getRootValidationSupport().isCodeSystemSupported(theValidationSupportContext, includeOrExcludeConceptSystemUrl)) {
						LookupCodeResult lookup = theValidationSupportContext.getRootValidationSupport().lookupCode(theValidationSupportContext, includeOrExcludeConceptSystemUrl, theWantCode, null);
						if (lookup != null) {
							ableToHandleCode = true;
							if (lookup.isFound()) {
								CodeSystem.ConceptDefinitionComponent conceptDefinition = new CodeSystem.ConceptDefinitionComponent()
									.addConcept()
									.setCode(theWantCode)
									.setDisplay(lookup.getCodeDisplay());
								List<CodeSystem.ConceptDefinitionComponent> codesList = Collections.singletonList(conceptDefinition);
								addCodes(includeOrExcludeConceptSystemUrl, includeOrExcludeConceptSystemVersion, codesList, nextCodeList, wantCodes);
							}
						}
					} else {

						/*
						 * If we're doing an expansion specifically looking for a single code, that means we're validating that code.
						 * In the case where we have a ValueSet that explicitly enumerates a collection of codes
						 * (via ValueSet.compose.include.code) in a code system that is unknown we'll assume the code is valid
						 * even if we can't find the CodeSystem. This is a compromise obviously, since it would be ideal for
						 * CodeSystems to always be known, but realistically there are always going to be CodeSystems that
						 * can't be supplied because of copyright issues, or because they are grammar based. Allowing a VS to
						 * enumerate a set of good codes for them is a nice compromise there.
						 */
						if (Objects.equals(theInclude.getSystem(), theWantSystemUrlAndVersion)) {
							Optional<org.hl7.fhir.r5.model.ValueSet.ConceptReferenceComponent> matchingEnumeratedConcept = theInclude.getConcept().stream().filter(t -> Objects.equals(t.getCode(), theWantCode)).findFirst();

							// If the ValueSet.compose.include has no individual concepts in it, and
							// we can't find the actual referenced CodeSystem, we have no choice
							// but to fail
							if (!theInclude.getConcept().isEmpty()) {
								ableToHandleCode = true;
							} else {
								failureMessage = getFailureMessageForMissingOrUnusableCodeSystem(includeOrExcludeSystemResource, loadedCodeSystemUrl);
							}

							if (matchingEnumeratedConcept.isPresent()) {
								CodeSystem.ConceptDefinitionComponent conceptDefinition = new CodeSystem.ConceptDefinitionComponent()
									.addConcept()
									.setCode(theWantCode)
									.setDisplay(matchingEnumeratedConcept.get().getDisplay());
								List<CodeSystem.ConceptDefinitionComponent> codesList = Collections.singletonList(conceptDefinition);
								addCodes(includeOrExcludeConceptSystemUrl, includeOrExcludeConceptSystemVersion, codesList, nextCodeList, wantCodes);
							}
						}

					}
				} else {
					if (isNotBlank(theInclude.getSystem()) && !theInclude.getConcept().isEmpty() && theInclude.getFilter().isEmpty() && theInclude.getValueSet().isEmpty()) {
						theInclude
							.getConcept()
							.stream()
							.map(t -> new FhirVersionIndependentConcept(theInclude.getSystem(), t.getCode(), t.getDisplay(), theInclude.getVersion()))
							.forEach(t -> nextCodeList.add(t));
						ableToHandleCode = true;
					}

					if (!ableToHandleCode) {
						failureMessage = getFailureMessageForMissingOrUnusableCodeSystem(includeOrExcludeSystemResource, loadedCodeSystemUrl);
					}
				}

			} else {
				ableToHandleCode = true;
			}

			if (!ableToHandleCode) {
				if (includeOrExcludeSystemResource == null && failureMessage == null) {
					failureMessage = getFailureMessageForMissingOrUnusableCodeSystem(includeOrExcludeSystemResource, loadedCodeSystemUrl);
				}

				if (includeOrExcludeSystemResource == null) {
					failureType = FailureType.UNKNOWN_CODE_SYSTEM;
				}

				throw new ExpansionCouldNotBeCompletedInternallyException(Msg.code(702) + failureMessage, failureType);
			}

			if (includeOrExcludeSystemResource != null && includeOrExcludeSystemResource.getContent() != CodeSystem.CodeSystemContentMode.NOTPRESENT) {
				addCodes(includeOrExcludeConceptSystemUrl, includeOrExcludeConceptSystemVersion, includeOrExcludeSystemResource.getConcept(), nextCodeList, wantCodes);
			}

		}

		for (CanonicalType nextValueSetInclude : theInclude.getValueSet()) {
			org.hl7.fhir.r5.model.ValueSet vs = valueSetLoader.apply(nextValueSetInclude.getValueAsString());
			if (vs != null) {
				org.hl7.fhir.r5.model.ValueSet subExpansion = expandValueSetR5(theValidationSupportContext, vs, theWantSystemUrlAndVersion, theWantCode);
				if (subExpansion == null) {
					throw new ExpansionCouldNotBeCompletedInternallyException(Msg.code(703) + "Failed to expand ValueSet: " + nextValueSetInclude.getValueAsString(), FailureType.OTHER);
				}
				for (org.hl7.fhir.r5.model.ValueSet.ValueSetExpansionContainsComponent next : subExpansion.getExpansion().getContains()) {
					nextCodeList.add(new FhirVersionIndependentConcept(next.getSystem(), next.getCode(), next.getDisplay(), next.getVersion()));
				}
			}
		}

		boolean retVal = false;

		for (FhirVersionIndependentConcept next : nextCodeList) {
			if (includeOrExcludeSystemResource != null && theWantCode != null) {
				boolean matches;
				if (includeOrExcludeSystemResource.getCaseSensitive()) {
					matches = theWantCode.equals(next.getCode());
				} else {
					matches = theWantCode.equalsIgnoreCase(next.getCode());
				}
				if (!matches) {
					continue;
				}
			}

			theConsumer.accept(next);
			retVal = true;
		}

		return retVal;
	}

	private Function<String, org.hl7.fhir.r5.model.ValueSet> newValueSetLoader(ValidationSupportContext theValidationSupportContext) {
		switch (myCtx.getVersion().getVersion()) {
			case DSTU2:
			case DSTU2_HL7ORG:
				return t -> {
					IBaseResource vs = theValidationSupportContext.getRootValidationSupport().fetchValueSet(t);
					if (vs instanceof ca.uhn.fhir.model.dstu2.resource.ValueSet) {
						IParser parserRi = FhirContext.forCached(FhirVersionEnum.DSTU2_HL7ORG).newJsonParser();
						IParser parserHapi = FhirContext.forDstu2Cached().newJsonParser();
						ca.uhn.fhir.model.dstu2.resource.ValueSet valueSet = (ca.uhn.fhir.model.dstu2.resource.ValueSet) vs;
						org.hl7.fhir.dstu2.model.ValueSet valueSetRi = parserRi.parseResource(org.hl7.fhir.dstu2.model.ValueSet.class, parserHapi.encodeResourceToString(valueSet));
						return (org.hl7.fhir.r5.model.ValueSet) VersionConvertorFactory_10_50.convertResource(valueSetRi, new BaseAdvisor_10_50(false));
					} else {
						org.hl7.fhir.dstu2.model.ValueSet valueSet = (org.hl7.fhir.dstu2.model.ValueSet) theValidationSupportContext.getRootValidationSupport().fetchValueSet(t);
						return (org.hl7.fhir.r5.model.ValueSet) VersionConvertorFactory_10_50.convertResource(valueSet, new BaseAdvisor_10_50(false));
					}
				};
			case DSTU3:
				return t -> {
					org.hl7.fhir.dstu3.model.ValueSet valueSet = (org.hl7.fhir.dstu3.model.ValueSet) theValidationSupportContext.getRootValidationSupport().fetchValueSet(t);
					return (org.hl7.fhir.r5.model.ValueSet) VersionConvertorFactory_30_50.convertResource(valueSet, new BaseAdvisor_30_50(false));
				};
			case R4:
				return t -> {
					org.hl7.fhir.r4.model.ValueSet valueSet = (org.hl7.fhir.r4.model.ValueSet) theValidationSupportContext.getRootValidationSupport().fetchValueSet(t);
					return (org.hl7.fhir.r5.model.ValueSet) VersionConvertorFactory_40_50.convertResource(valueSet, new BaseAdvisor_40_50(false));
				};
			default:
			case DSTU2_1:
			case R5:
				return t -> (org.hl7.fhir.r5.model.ValueSet) theValidationSupportContext.getRootValidationSupport().fetchValueSet(t);
		}
	}

	private Function<String, CodeSystem> newCodeSystemLoader(ValidationSupportContext theValidationSupportContext) {
		switch (myCtx.getVersion().getVersion()) {
			case DSTU2:
			case DSTU2_HL7ORG:
				return t -> {
					IBaseResource codeSystem = theValidationSupportContext.getRootValidationSupport().fetchCodeSystem(t);
					CodeSystem retVal = null;
					if (codeSystem != null) {
						retVal = new CodeSystem();
						if (codeSystem instanceof ca.uhn.fhir.model.dstu2.resource.ValueSet) {
							ca.uhn.fhir.model.dstu2.resource.ValueSet codeSystemCasted = (ca.uhn.fhir.model.dstu2.resource.ValueSet) codeSystem;
							retVal.setUrl(codeSystemCasted.getUrl());
							addCodesDstu2(codeSystemCasted.getCodeSystem().getConcept(), retVal.getConcept());
						} else {
							org.hl7.fhir.dstu2.model.ValueSet codeSystemCasted = (org.hl7.fhir.dstu2.model.ValueSet) codeSystem;
							retVal.setUrl(codeSystemCasted.getUrl());
							addCodesDstu2Hl7Org(codeSystemCasted.getCodeSystem().getConcept(), retVal.getConcept());
						}
					}
					return retVal;
				};
			case DSTU3:
				return t -> {
					org.hl7.fhir.dstu3.model.CodeSystem codeSystem = (org.hl7.fhir.dstu3.model.CodeSystem) theValidationSupportContext.getRootValidationSupport().fetchCodeSystem(t);
					return (CodeSystem) VersionConvertorFactory_30_50.convertResource(codeSystem, new BaseAdvisor_30_50(false));
				};
			case R4:
				return t -> {
					org.hl7.fhir.r4.model.CodeSystem codeSystem = (org.hl7.fhir.r4.model.CodeSystem) theValidationSupportContext.getRootValidationSupport().fetchCodeSystem(t);
					return (CodeSystem) VersionConvertorFactory_40_50.convertResource(codeSystem, new BaseAdvisor_40_50(false));
				};
			case DSTU2_1:
			case R5:
			default:
				return t -> (org.hl7.fhir.r5.model.CodeSystem) theValidationSupportContext.getRootValidationSupport().fetchCodeSystem(t);

		}
	}

	private String getFailureMessageForMissingOrUnusableCodeSystem(CodeSystem includeOrExcludeSystemResource, String loadedCodeSystemUrl) {
		String failureMessage;
		if (includeOrExcludeSystemResource == null) {
			failureMessage = "Unable to expand ValueSet because CodeSystem could not be found: " + loadedCodeSystemUrl;
		} else {
			assert includeOrExcludeSystemResource.getContent() == CodeSystem.CodeSystemContentMode.NOTPRESENT;
			failureMessage = "Unable to expand ValueSet because CodeSystem has CodeSystem.content=not-present but contents were not found: " + loadedCodeSystemUrl;
		}
		return failureMessage;
	}

	private void addCodes(String theCodeSystemUrl, String theCodeSystemVersion, List<CodeSystem.ConceptDefinitionComponent> theSource, List<FhirVersionIndependentConcept> theTarget, Set<String> theCodeFilter) {
		for (CodeSystem.ConceptDefinitionComponent next : theSource) {
			if (isNotBlank(next.getCode())) {
				if (theCodeFilter == null || theCodeFilter.contains(next.getCode())) {
					theTarget.add(new FhirVersionIndependentConcept(theCodeSystemUrl, next.getCode(), next.getDisplay(), theCodeSystemVersion));
				}
			}
			addCodes(theCodeSystemUrl, theCodeSystemVersion, next.getConcept(), theTarget, theCodeFilter);
		}
	}

	private String optionallyPopulateVersionFromUrl(String theSystemUrl, String theVersion) {
		if(contains(theSystemUrl, OUR_PIPE_CHARACTER) && isBlank(theVersion)){
			theVersion = substringAfter(theSystemUrl, OUR_PIPE_CHARACTER);
		}
		return theVersion;
	}

	public enum FailureType {

		UNKNOWN_CODE_SYSTEM,
		OTHER

	}

	public static class ExpansionCouldNotBeCompletedInternallyException extends Exception {

		private static final long serialVersionUID = -2226561628771483085L;
		private final FailureType myFailureType;

		public ExpansionCouldNotBeCompletedInternallyException(String theMessage, FailureType theFailureType) {
			super(theMessage);
			myFailureType = theFailureType;
		}

		public FailureType getFailureType() {
			return myFailureType;
		}
	}

	private static void flattenAndConvertCodesDstu2(List<org.hl7.fhir.dstu2.model.ValueSet.ValueSetExpansionContainsComponent> theInput, List<FhirVersionIndependentConcept> theFhirVersionIndependentConcepts) {
		for (org.hl7.fhir.dstu2.model.ValueSet.ValueSetExpansionContainsComponent next : theInput) {
			theFhirVersionIndependentConcepts.add(new FhirVersionIndependentConcept(next.getSystem(), next.getCode(), next.getDisplay()));
			flattenAndConvertCodesDstu2(next.getContains(), theFhirVersionIndependentConcepts);
		}
	}

	private static void flattenAndConvertCodesDstu3(List<org.hl7.fhir.dstu3.model.ValueSet.ValueSetExpansionContainsComponent> theInput, List<FhirVersionIndependentConcept> theFhirVersionIndependentConcepts) {
		for (org.hl7.fhir.dstu3.model.ValueSet.ValueSetExpansionContainsComponent next : theInput) {
			theFhirVersionIndependentConcepts.add(new FhirVersionIndependentConcept(next.getSystem(), next.getCode(), next.getDisplay(), next.getVersion()));
			flattenAndConvertCodesDstu3(next.getContains(), theFhirVersionIndependentConcepts);
		}
	}

	private static void flattenAndConvertCodesR4(List<org.hl7.fhir.r4.model.ValueSet.ValueSetExpansionContainsComponent> theInput, List<FhirVersionIndependentConcept> theFhirVersionIndependentConcepts) {
		for (org.hl7.fhir.r4.model.ValueSet.ValueSetExpansionContainsComponent next : theInput) {
			theFhirVersionIndependentConcepts.add(new FhirVersionIndependentConcept(next.getSystem(), next.getCode(), next.getDisplay(), next.getVersion()));
			flattenAndConvertCodesR4(next.getContains(), theFhirVersionIndependentConcepts);
		}
	}

	private static void flattenAndConvertCodesR5(List<org.hl7.fhir.r5.model.ValueSet.ValueSetExpansionContainsComponent> theInput, List<FhirVersionIndependentConcept> theFhirVersionIndependentConcepts) {
		for (org.hl7.fhir.r5.model.ValueSet.ValueSetExpansionContainsComponent next : theInput) {
			theFhirVersionIndependentConcepts.add(new FhirVersionIndependentConcept(next.getSystem(), next.getCode(), next.getDisplay(), next.getVersion()));
			flattenAndConvertCodesR5(next.getContains(), theFhirVersionIndependentConcepts);
		}
	}

}
