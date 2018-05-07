package org.hl7.fhir.convertors;

/*-
 * #%L
 * HAPI FHIR - Converter
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
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


import org.hl7.fhir.dstu3.model.CodeSystem;
import org.hl7.fhir.dstu3.model.ValueSet;
import org.hl7.fhir.exceptions.FHIRException;

public interface VersionConvertorAdvisor {
	boolean ignoreEntry(org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent src);

	// called ?
	org.hl7.fhir.instance.model.Resource convert(org.hl7.fhir.dstu3.model.Resource resource) throws FHIRException;

	// called when an r2 value set has a codeSystem in it
	void handleCodeSystem(CodeSystem tgtcs, ValueSet source);

	CodeSystem getCodeSystem(ValueSet src);
}
