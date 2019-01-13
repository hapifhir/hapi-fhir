package org.hl7.fhir.r4.validation;

/*-
 * #%L
 * org.hl7.fhir.validation
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


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeSystem.ConceptDefinitionComponent;
import org.hl7.fhir.utilities.validation.ValidationMessage;
import org.hl7.fhir.utilities.validation.ValidationMessage.IssueType;

public class CodeSystemValidator extends BaseValidator {

  public List<ValidationMessage> validate(CodeSystem cs, boolean forBuild) {
    List<ValidationMessage> errors = new ArrayList<ValidationMessage>();
    
    // this is an invariant on CodeSystem, but the invariant is wrong in R3, and doesn't work 
    checkCodesUnique(cs, errors);
    return errors;
  }

  private void checkCodesUnique(CodeSystem cs, List<ValidationMessage> errors) {
    Set<String> codes = new HashSet<String>();
    checkCodes(codes, cs.getConcept(), "CodeSystem["+cs.getId()+"]", errors);
  }

  private void checkCodes(Set<String> codes, List<ConceptDefinitionComponent> list, String path, List<ValidationMessage> errors) {
    for (ConceptDefinitionComponent cc : list) {
      String npath = path+".concept["+cc.getCode()+"]";
      rule(errors, IssueType.BUSINESSRULE, npath, !codes.contains(cc.getCode()), "Duplicate Code "+cc.getCode());
      codes.add(cc.getCode());
      checkCodes(codes, cc.getConcept(), npath, errors);
    }
  }


}
