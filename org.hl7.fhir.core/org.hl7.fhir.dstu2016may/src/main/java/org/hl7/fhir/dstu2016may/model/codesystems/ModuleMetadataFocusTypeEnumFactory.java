package org.hl7.fhir.dstu2016may.model.codesystems;

/*-
 * #%L
 * org.hl7.fhir.dstu2016may
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


/*
  Copyright (c) 2011+, HL7, Inc.
  All rights reserved.
  
  Redistribution and use in source and binary forms, with or without modification, 
  are permitted provided that the following conditions are met:
  
   * Redistributions of source code must retain the above copyright notice, this 
     list of conditions and the following disclaimer.
   * Redistributions in binary form must reproduce the above copyright notice, 
     this list of conditions and the following disclaimer in the documentation 
     and/or other materials provided with the distribution.
   * Neither the name of HL7 nor the names of its contributors may be used to 
     endorse or promote products derived from this software without specific 
     prior written permission.
  
  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
  IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, 
  INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT 
  NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR 
  PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
  WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
  ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
  POSSIBILITY OF SUCH DAMAGE.
  
*/

// Generated on Sun, May 8, 2016 03:05+1000 for FHIR v1.4.0


import org.hl7.fhir.dstu2016may.model.EnumFactory;

public class ModuleMetadataFocusTypeEnumFactory implements EnumFactory<ModuleMetadataFocusType> {

  public ModuleMetadataFocusType fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("patient-gender".equals(codeString))
      return ModuleMetadataFocusType.PATIENTGENDER;
    if ("patient-age-group".equals(codeString))
      return ModuleMetadataFocusType.PATIENTAGEGROUP;
    if ("clinical-focus".equals(codeString))
      return ModuleMetadataFocusType.CLINICALFOCUS;
    if ("target-user".equals(codeString))
      return ModuleMetadataFocusType.TARGETUSER;
    if ("workflow-setting".equals(codeString))
      return ModuleMetadataFocusType.WORKFLOWSETTING;
    if ("workflow-task".equals(codeString))
      return ModuleMetadataFocusType.WORKFLOWTASK;
    if ("clinical-venue".equals(codeString))
      return ModuleMetadataFocusType.CLINICALVENUE;
    if ("jurisdiction".equals(codeString))
      return ModuleMetadataFocusType.JURISDICTION;
    throw new IllegalArgumentException("Unknown ModuleMetadataFocusType code '"+codeString+"'");
  }

  public String toCode(ModuleMetadataFocusType code) {
    if (code == ModuleMetadataFocusType.PATIENTGENDER)
      return "patient-gender";
    if (code == ModuleMetadataFocusType.PATIENTAGEGROUP)
      return "patient-age-group";
    if (code == ModuleMetadataFocusType.CLINICALFOCUS)
      return "clinical-focus";
    if (code == ModuleMetadataFocusType.TARGETUSER)
      return "target-user";
    if (code == ModuleMetadataFocusType.WORKFLOWSETTING)
      return "workflow-setting";
    if (code == ModuleMetadataFocusType.WORKFLOWTASK)
      return "workflow-task";
    if (code == ModuleMetadataFocusType.CLINICALVENUE)
      return "clinical-venue";
    if (code == ModuleMetadataFocusType.JURISDICTION)
      return "jurisdiction";
    return "?";
  }

    public String toSystem(ModuleMetadataFocusType code) {
      return code.getSystem();
      }

}

