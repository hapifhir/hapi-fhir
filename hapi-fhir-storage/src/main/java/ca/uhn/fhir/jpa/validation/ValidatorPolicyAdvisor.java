/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.jpa.validation;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.r5.elementmodel.Element;
import org.hl7.fhir.r5.model.ElementDefinition;
import org.hl7.fhir.r5.model.StructureDefinition;
import org.hl7.fhir.r5.model.ValueSet;
import org.hl7.fhir.r5.utils.validation.IMessagingServices;
import org.hl7.fhir.r5.utils.validation.IResourceValidator;
import org.hl7.fhir.r5.utils.validation.IValidationPolicyAdvisor;
import org.hl7.fhir.r5.utils.validation.constants.BindingKind;
import org.hl7.fhir.r5.utils.validation.constants.ContainedReferenceValidationPolicy;
import org.hl7.fhir.r5.utils.validation.constants.ReferenceValidationPolicy;
import org.hl7.fhir.utilities.validation.ValidationMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

public class ValidatorPolicyAdvisor implements IValidationPolicyAdvisor {

	private static final Logger ourLog = LoggerFactory.getLogger(ValidatorPolicyAdvisor.class);

	@Autowired
	private ValidationSettings myValidationSettings;

	@Autowired
	private FhirContext myFhirContext;

	@Override
	public ReferenceValidationPolicy policyForReference(
			IResourceValidator validator, Object appContext, String path, String url) {
		int slashIdx = url.indexOf("/");
		if (slashIdx > 0 && myFhirContext.getResourceTypes().contains(url.substring(0, slashIdx))) {
			return myValidationSettings.getLocalReferenceValidationDefaultPolicy();
		}

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
	public List<StructureDefinition> getImpliedProfilesForResource(
			IResourceValidator validator,
			Object appContext,
			String stackPath,
			ElementDefinition definition,
			StructureDefinition structure,
			Element resource,
			boolean valid,
			IMessagingServices msgServices,
			List<ValidationMessage> messages) {
		return Arrays.asList();
	}
}
