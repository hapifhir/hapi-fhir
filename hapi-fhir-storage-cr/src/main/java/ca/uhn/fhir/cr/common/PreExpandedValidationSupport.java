/*-
 * #%L
 * HAPI FHIR - Clinical Reasoning
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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
package ca.uhn.fhir.cr.common;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.context.support.ValueSetExpansionOptions;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * This class provides an implementation of IValidationSupport
 * interface which is used for validation of terminology services.
 */
public class PreExpandedValidationSupport implements IValidationSupport {
	private final FhirContext myFhirContext;

	public PreExpandedValidationSupport(FhirContext theFhirContext) {
		this.myFhirContext = theFhirContext;
	}

	@Override
	public ValueSetExpansionOutcome expandValueSet(
			ValidationSupportContext theValidationSupportContext,
			@Nullable ValueSetExpansionOptions theExpansionOptions,
			@Nonnull IBaseResource theValueSetToExpand) {
		Validate.notNull(theValueSetToExpand, "theValueSetToExpand must not be null or blank");

		if (!getFhirContext()
				.getResourceDefinition("ValueSet")
				.getChildByName("expansion")
				.getAccessor()
				.getValues(theValueSetToExpand)
				.isEmpty()) {
			return new ValueSetExpansionOutcome(theValueSetToExpand);
		} else {
			return IValidationSupport.super.expandValueSet(
					theValidationSupportContext, theExpansionOptions, theValueSetToExpand);
		}
	}

	@Override
	public FhirContext getFhirContext() {
		return this.myFhirContext;
	}
}
