package org.hl7.fhir.common.hapi.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IContextValidationSupport;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.util.VersionIndependentConcept;
import org.hl7.fhir.dstu3.model.ExpansionProfile;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.ValueSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public abstract class BaseStaticResourceValidationSupport implements IContextValidationSupport {

	@Override
	public ValueSetExpansionOutcome expandValueSet(IContextValidationSupport theRootValidationSupport, FhirContext theContext, IBaseResource theValueSet) {

		IBaseResource expansion;
		switch (theContext.getVersion().getVersion()) {
			case DSTU3: {
				org.hl7.fhir.dstu3.terminologies.ValueSetExpanderSimple expander = new org.hl7.fhir.dstu3.terminologies.ValueSetExpanderSimple(new org.hl7.fhir.dstu3.hapi.ctx.HapiWorkerContext(theContext, theRootValidationSupport), null);
				expansion = expander.expand((org.hl7.fhir.dstu3.model.ValueSet) theValueSet, new ExpansionProfile()).getValueset();
				break;
			}
			case R4: {
				org.hl7.fhir.r4.terminologies.ValueSetExpanderSimple expander = new org.hl7.fhir.r4.terminologies.ValueSetExpanderSimple(new org.hl7.fhir.r4.hapi.ctx.HapiWorkerContext(theContext, theRootValidationSupport));
				expansion = expander.expand((org.hl7.fhir.r4.model.ValueSet) theValueSet, new org.hl7.fhir.r4.model.Parameters()).getValueset();
				break;
			}
			case R5: {
				org.hl7.fhir.r5.terminologies.ValueSetExpanderSimple expander = new org.hl7.fhir.r5.terminologies.ValueSetExpanderSimple(new org.hl7.fhir.r5.hapi.ctx.HapiWorkerContext(theContext, theRootValidationSupport));
				expansion = expander.expand((org.hl7.fhir.r5.model.ValueSet) theValueSet, new org.hl7.fhir.r5.model.Parameters()).getValueset();
				break;
			}
			case DSTU2:
			case DSTU2_1:
			case DSTU2_HL7ORG:
			default:
				throw new IllegalArgumentException("Can not handle version: " + theContext.getVersion().getVersion());
		}

		return new ValueSetExpansionOutcome(expansion, null);
	}

	@Override
	public CodeValidationResult validateCode(IContextValidationSupport theRootValidationSupport, FhirContext theContext, String theCodeSystem, String theCode, String theDisplay, String theValueSetUrl) {
		IBaseResource vs;
		if (isNotBlank(theValueSetUrl)) {
			vs = fetchValueSet(theContext, theValueSetUrl);
		} else {
			switch (theContext.getVersion().getVersion()) {
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
				case DSTU2_HL7ORG:
				default:
					throw new IllegalArgumentException("Can not handle version: " + theContext.getVersion().getVersion());
			}
		}

		if (vs == null) {
			return null;
		}


		IBaseResource expansion = expandValueSet(theRootValidationSupport, theContext, vs).getValueSet();

		List<VersionIndependentConcept> codes;
		switch (theContext.getVersion().getVersion()) {
			case DSTU3: {
				org.hl7.fhir.dstu3.model.ValueSet expansionVs = (org.hl7.fhir.dstu3.model.ValueSet) expansion;
				List<org.hl7.fhir.dstu3.model.ValueSet.ValueSetExpansionContainsComponent> contains = expansionVs.getExpansion().getContains();
				codes = contains.stream().map(t -> new VersionIndependentConcept(t.getSystem(), t.getCode())).collect(Collectors.toList());
				break;
			}
			case R4: {
				org.hl7.fhir.r4.model.ValueSet expansionVs = (org.hl7.fhir.r4.model.ValueSet) expansion;
				List<ValueSet.ValueSetExpansionContainsComponent> contains = expansionVs.getExpansion().getContains();
				codes = contains.stream().map(t -> new VersionIndependentConcept(t.getSystem(), t.getCode())).collect(Collectors.toList());
				break;
			}
			case R5: {
				org.hl7.fhir.r5.model.ValueSet expansionVs = (org.hl7.fhir.r5.model.ValueSet) expansion;
				List<org.hl7.fhir.r5.model.ValueSet.ValueSetExpansionContainsComponent> contains = expansionVs.getExpansion().getContains();
				codes = contains.stream().map(t -> new VersionIndependentConcept(t.getSystem(), t.getCode())).collect(Collectors.toList());
				break;
			}
			case DSTU2:
			case DSTU2_1:
			case DSTU2_HL7ORG:
			default:
				throw new IllegalArgumentException("Can not handle version: " + theContext.getVersion().getVersion());
		}

		for (VersionIndependentConcept nextExpansionCode : codes) {

			if (theCode.equals(nextExpansionCode.getCode())) {
				if (Constants.codeSystemNotNeeded(theCodeSystem) || nextExpansionCode.getSystem().equals(theCodeSystem)) {
					return new CodeValidationResult(new CodeSystem.ConceptDefinitionComponent(new CodeType(theCode)));
				}
			}
		}

		return null;
	}

	@Override
	public LookupCodeResult lookupCode(IContextValidationSupport theRootValidationSupport, FhirContext theContext, String theSystem, String theCode) {
		return validateCode(theRootValidationSupport, theContext, theSystem, theCode, null, null).asLookupCodeResult(theSystem, theCode);
	}


	static <T extends IBaseResource> ArrayList<T> toList(Map<String, IBaseResource> theMap, Class<T> theType) {
		ArrayList<T> retVal = new ArrayList<>(theMap.size());
		for (IBaseResource next : theMap.values()) {
			retVal.add(theType.cast(next));
		}
		return retVal;
	}

}
