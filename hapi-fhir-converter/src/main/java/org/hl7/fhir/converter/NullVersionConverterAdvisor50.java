package org.hl7.fhir.converter;

/*
 * #%L
 * HAPI FHIR - Converter
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import org.hl7.fhir.convertors.VersionConvertorAdvisor50;
import org.hl7.fhir.dstu2.model.Resource;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.r5.model.Bundle;
import org.hl7.fhir.r5.model.CodeSystem;
import org.hl7.fhir.r5.model.ValueSet;

import java.util.IdentityHashMap;

public class NullVersionConverterAdvisor50 implements VersionConvertorAdvisor50 {

	private IdentityHashMap<ValueSet, CodeSystem> myCodeSystems = new IdentityHashMap<>();

	@Override
	public boolean ignoreEntry(Bundle.BundleEntryComponent src) {
		return false;
	}

	@Override
	public Resource convertR2(org.hl7.fhir.r5.model.Resource resource) throws FHIRException {
		return null;
	}

	@Override
	public org.hl7.fhir.dstu2016may.model.Resource convertR2016May(org.hl7.fhir.r5.model.Resource resource) throws FHIRException {
		return null;
	}

	@Override
	public org.hl7.fhir.dstu3.model.Resource convertR3(org.hl7.fhir.r5.model.Resource resource) throws FHIRException {
		return null;
	}

	@Override
	public org.hl7.fhir.r4.model.Resource convertR4(org.hl7.fhir.r5.model.Resource resource) throws FHIRException {
		return null;
	}

	@Override
	public void handleCodeSystem(CodeSystem tgtcs, ValueSet source) throws FHIRException {
		myCodeSystems.put(source, tgtcs);
	}

	@Override
	public CodeSystem getCodeSystem(ValueSet src) throws FHIRException {
		return myCodeSystems.get(src);
	}
}
