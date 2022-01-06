package org.hl7.fhir.converter;

/*
 * #%L
 * HAPI FHIR - Converter
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import org.hl7.fhir.convertors.advisors.impl.BaseAdvisor_10_30;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.dstu3.model.CodeSystem;
import org.hl7.fhir.dstu3.model.ValueSet;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.r5.model.FhirPublication;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class NullVersionConverterAdvisor10_30 extends BaseAdvisor_10_30 {


	@Nullable
	@Override
	public CodeSystem getCodeSystem(@Nullable ValueSet theValueSet) throws FHIRException {
		return null;
	}

	@Override
	public void handleCodeSystem(@Nullable CodeSystem theCodeSystem, @Nullable ValueSet theValueSet) throws FHIRException {
		// nothing
	}

	@Override
	public boolean ignoreEntry(@Nullable BundleEntryComponent theBundleEntryComponent, @Nonnull FhirPublication theFhirPublication) {
		return false;
	}
}
