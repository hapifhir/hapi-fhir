package org.hl7.fhir.common.hapi.validation.validator;

import org.hl7.fhir.r5.elementmodel.Element;
import org.hl7.fhir.r5.model.ElementDefinition;
import org.hl7.fhir.r5.model.StructureDefinition;
import org.hl7.fhir.r5.model.ValueSet;
import org.hl7.fhir.r5.utils.validation.IResourceValidator;
import org.hl7.fhir.r5.utils.validation.IValidationPolicyAdvisor;
import org.hl7.fhir.r5.utils.validation.constants.BindingKind;
import org.hl7.fhir.r5.utils.validation.constants.ContainedReferenceValidationPolicy;
import org.hl7.fhir.r5.utils.validation.constants.ReferenceValidationPolicy;

import java.util.EnumSet;
import java.util.List;

/**
 * Implementation of the base {@link IValidationPolicyAdvisor}. This is used as the default for all validation operations
 * done within the core libraries, as without a default, it will ignore some validation operations.
 */
public class FhirDefaultPolicyAdvisor implements IValidationPolicyAdvisor {

	@Override
	public ReferenceValidationPolicy policyForReference(
			IResourceValidator validator, Object appContext, String path, String url) {
		return ReferenceValidationPolicy.IGNORE;
	}

	@Override
	public EnumSet<ResourceValidationAction> policyForResource(
			IResourceValidator validator, Object appContext, StructureDefinition type, String path) {
		return EnumSet.allOf(ResourceValidationAction.class);
	}

	@Override
	public EnumSet<ElementValidationAction> policyForElement(
			IResourceValidator validator,
			Object appContext,
			StructureDefinition structure,
			ElementDefinition element,
			String path) {
		return EnumSet.allOf(ElementValidationAction.class);
	}

	@Override
	public ContainedReferenceValidationPolicy policyForContained(
			IResourceValidator validator,
			Object appContext,
			StructureDefinition structure,
			ElementDefinition element,
			String containerType,
			String containerId,
			Element.SpecialElement containingResourceType,
			String path,
			String url) {
		return ContainedReferenceValidationPolicy.CHECK_VALID;
	}

	@Override
	public EnumSet<CodedContentValidationAction> policyForCodedContent(
			IResourceValidator validator,
			Object appContext,
			String stackPath,
			ElementDefinition definition,
			StructureDefinition structure,
			BindingKind kind,
			AdditionalBindingPurpose purpose,
			ValueSet valueSet,
			List<String> systems) {
		return EnumSet.allOf(CodedContentValidationAction.class);
	}
}
