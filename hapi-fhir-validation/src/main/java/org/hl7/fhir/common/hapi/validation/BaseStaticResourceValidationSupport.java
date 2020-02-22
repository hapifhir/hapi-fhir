package org.hl7.fhir.common.hapi.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IContextValidationSupport;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.util.VersionIndependentConcept;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.utilities.validation.ValidationMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public abstract class BaseStaticResourceValidationSupport extends BaseValidationSupport implements IContextValidationSupport {

	/**
	 * Constructor
	 */
	protected BaseStaticResourceValidationSupport(FhirContext theFhirContext) {
		super(theFhirContext);
	}

	@Override
	public ValueSetExpansionOutcome expandValueSet(IContextValidationSupport theRootValidationSupport, IBaseResource theValueSetToExpand) {

		IBaseResource expansion;
		switch (myCtx.getVersion().getVersion()) {
			case DSTU2_HL7ORG: {
				org.hl7.fhir.dstu2.terminologies.ValueSetExpanderSimple expander = new org.hl7.fhir.dstu2.terminologies.ValueSetExpanderSimple(new org.hl7.fhir.instance.hapi.validation.HapiWorkerContext(myCtx, theRootValidationSupport), null);
				expansion = expander.expand((org.hl7.fhir.dstu2.model.ValueSet) theValueSetToExpand).getValueset();
				break;
			}
			case DSTU3: {
				org.hl7.fhir.dstu3.terminologies.ValueSetExpanderSimple expander = new org.hl7.fhir.dstu3.terminologies.ValueSetExpanderSimple(new org.hl7.fhir.dstu3.hapi.ctx.HapiWorkerContext(myCtx, theRootValidationSupport), null);
				expansion = expander.expand((org.hl7.fhir.dstu3.model.ValueSet) theValueSetToExpand, null).getValueset();
				break;
			}
			case R4: {
				org.hl7.fhir.r4.terminologies.ValueSetExpanderSimple expander = new org.hl7.fhir.r4.terminologies.ValueSetExpanderSimple(new org.hl7.fhir.r4.hapi.ctx.HapiWorkerContext(myCtx, theRootValidationSupport));
				expansion = expander.expand((org.hl7.fhir.r4.model.ValueSet) theValueSetToExpand, new org.hl7.fhir.r4.model.Parameters()).getValueset();
				break;
			}
			case R5: {
				org.hl7.fhir.r5.terminologies.ValueSetExpanderSimple expander = new org.hl7.fhir.r5.terminologies.ValueSetExpanderSimple(new org.hl7.fhir.r5.hapi.ctx.HapiWorkerContext(myCtx, theRootValidationSupport));
				expansion = expander.expand((org.hl7.fhir.r5.model.ValueSet) theValueSetToExpand, new org.hl7.fhir.r5.model.Parameters()).getValueset();
				break;
			}
			case DSTU2:
			case DSTU2_1:
			default:
				throw new IllegalArgumentException("Can not handle version: " + myCtx.getVersion().getVersion());
		}

		return new ValueSetExpansionOutcome(expansion, null);
	}

	@Override
	public CodeValidationResult validateCode(IContextValidationSupport theRootValidationSupport, String theCodeSystem, String theCode, String theDisplay, String theValueSetUrl) {
		IBaseResource vs;
		if (isNotBlank(theValueSetUrl)) {
			vs = fetchValueSet(theValueSetUrl);
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

		if (vs == null) {
			return null;
		}


		IBaseResource expansion = expandValueSet(theRootValidationSupport, vs).getValueSet();
		IBaseResource system = fetchCodeSystem(theCodeSystem);
		boolean caseSensitive;

		List<VersionIndependentConcept> codes = new ArrayList<>();
		switch (myCtx.getVersion().getVersion()) {
			case DSTU2_HL7ORG: {
				org.hl7.fhir.dstu2.model.ValueSet expansionVs = (org.hl7.fhir.dstu2.model.ValueSet) expansion;
				List<org.hl7.fhir.dstu2.model.ValueSet.ValueSetExpansionContainsComponent> contains = expansionVs.getExpansion().getContains();
				flattenAndConvertCodesDstu2(contains, codes);
				caseSensitive = true;
				break;
			}
			case DSTU3: {
				org.hl7.fhir.dstu3.model.ValueSet expansionVs = (org.hl7.fhir.dstu3.model.ValueSet) expansion;
				List<org.hl7.fhir.dstu3.model.ValueSet.ValueSetExpansionContainsComponent> contains = expansionVs.getExpansion().getContains();
				flattenAndConvertCodesDstu3(contains, codes);
				caseSensitive = system == null || ((org.hl7.fhir.dstu3.model.CodeSystem) system).getCaseSensitive();
				break;
			}
			case R4: {
				org.hl7.fhir.r4.model.ValueSet expansionVs = (org.hl7.fhir.r4.model.ValueSet) expansion;
				List<org.hl7.fhir.r4.model.ValueSet.ValueSetExpansionContainsComponent> contains = expansionVs.getExpansion().getContains();
				flattenAndConvertCodesR4(contains, codes);
				caseSensitive = system == null || ((org.hl7.fhir.r4.model.CodeSystem) system).getCaseSensitive();
				break;
			}
			case R5: {
				org.hl7.fhir.r5.model.ValueSet expansionVs = (org.hl7.fhir.r5.model.ValueSet) expansion;
				List<org.hl7.fhir.r5.model.ValueSet.ValueSetExpansionContainsComponent> contains = expansionVs.getExpansion().getContains();
				flattenAndConvertCodesR5(contains, codes);
				caseSensitive = system == null || ((org.hl7.fhir.r5.model.CodeSystem) system).getCaseSensitive();
				break;
			}
			case DSTU2:
			case DSTU2_1:
			default:
				throw new IllegalArgumentException("Can not handle version: " + myCtx.getVersion().getVersion());
		}

		for (VersionIndependentConcept nextExpansionCode : codes) {

			boolean codeMatches;
			if (caseSensitive) {
				codeMatches = theCode.equals(nextExpansionCode.getCode());
			} else {
				codeMatches = theCode.equalsIgnoreCase(nextExpansionCode.getCode());
			}
			if (codeMatches) {
				if (Constants.codeSystemNotNeeded(theCodeSystem) || nextExpansionCode.getSystem().equals(theCodeSystem)) {
					return new CodeValidationResult()
						.setCode(theCode);
				}
			}
		}

		if (system != null) {
			return new CodeValidationResult()
				.setSeverity(ValidationMessage.IssueSeverity.WARNING.toCode())
				.setMessage("Unknown code for '" + theCodeSystem + "#" + theCode + "'");
		}

		return null;
	}

	@Override
	public LookupCodeResult lookupCode(IContextValidationSupport theRootValidationSupport, String theSystem, String theCode) {
		return validateCode(theRootValidationSupport, theSystem, theCode, null, null).asLookupCodeResult(theSystem, theCode);
	}

	private static void flattenAndConvertCodesDstu2(List<org.hl7.fhir.dstu2.model.ValueSet.ValueSetExpansionContainsComponent> theInput, List<VersionIndependentConcept> theVersionIndependentConcepts) {
		for (org.hl7.fhir.dstu2.model.ValueSet.ValueSetExpansionContainsComponent next : theInput) {
			theVersionIndependentConcepts.add(new VersionIndependentConcept(next.getSystem(), next.getCode()));
			flattenAndConvertCodesDstu2(next.getContains(), theVersionIndependentConcepts);
		}
	}

	private static void flattenAndConvertCodesDstu3(List<org.hl7.fhir.dstu3.model.ValueSet.ValueSetExpansionContainsComponent> theInput, List<VersionIndependentConcept> theVersionIndependentConcepts) {
		for (org.hl7.fhir.dstu3.model.ValueSet.ValueSetExpansionContainsComponent next : theInput) {
			theVersionIndependentConcepts.add(new VersionIndependentConcept(next.getSystem(), next.getCode()));
			flattenAndConvertCodesDstu3(next.getContains(), theVersionIndependentConcepts);
		}
	}

	private static void flattenAndConvertCodesR4(List<org.hl7.fhir.r4.model.ValueSet.ValueSetExpansionContainsComponent> theInput, List<VersionIndependentConcept> theVersionIndependentConcepts) {
		for (org.hl7.fhir.r4.model.ValueSet.ValueSetExpansionContainsComponent next : theInput) {
			theVersionIndependentConcepts.add(new VersionIndependentConcept(next.getSystem(), next.getCode()));
			flattenAndConvertCodesR4(next.getContains(), theVersionIndependentConcepts);
		}
	}

	private static void flattenAndConvertCodesR5(List<org.hl7.fhir.r5.model.ValueSet.ValueSetExpansionContainsComponent> theInput, List<VersionIndependentConcept> theVersionIndependentConcepts) {
		for (org.hl7.fhir.r5.model.ValueSet.ValueSetExpansionContainsComponent next : theInput) {
			theVersionIndependentConcepts.add(new VersionIndependentConcept(next.getSystem(), next.getCode()));
			flattenAndConvertCodesR5(next.getContains(), theVersionIndependentConcepts);
		}
	}

	@SuppressWarnings("unchecked")
	static <T extends IBaseResource> List<T> toList(Map<String, IBaseResource> theMap) {
		ArrayList<IBaseResource> retVal = new ArrayList<>(theMap.values());
		return (List<T>) Collections.unmodifiableList(retVal);
	}

}
