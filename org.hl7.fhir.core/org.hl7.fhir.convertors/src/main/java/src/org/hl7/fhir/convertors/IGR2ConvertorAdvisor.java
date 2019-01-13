package org.hl7.fhir.convertors;

/*-
 * #%L
 * org.hl7.fhir.convertors
 * %%
 * Copyright (C) 2014 - 2019 Health Level 7
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


import org.hl7.fhir.dstu2.model.Resource;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.ValueSet;

public class IGR2ConvertorAdvisor implements VersionConvertorAdvisor40 {

  @Override
  public boolean ignoreEntry(BundleEntryComponent src) {
    return false;
  }

  @Override
  public Resource convertR2(org.hl7.fhir.r4.model.Resource resource) throws FHIRException {
    return null;
  }

  @Override
  public org.hl7.fhir.dstu2016may.model.Resource convertR2016May(org.hl7.fhir.r4.model.Resource resource) throws FHIRException {
    return null;
  }

  @Override
  public org.hl7.fhir.dstu3.model.Resource convertR3(org.hl7.fhir.r4.model.Resource resource) throws FHIRException {
    return null;
  }

  @Override
  public void handleCodeSystem(CodeSystem cs, ValueSet vs) {
    cs.setId(vs.getId());
  }

  @Override
  public CodeSystem getCodeSystem(ValueSet src) {
    return null;
  }

}
