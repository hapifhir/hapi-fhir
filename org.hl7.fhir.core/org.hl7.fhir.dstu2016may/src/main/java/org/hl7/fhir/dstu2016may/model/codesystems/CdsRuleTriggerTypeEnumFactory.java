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

public class CdsRuleTriggerTypeEnumFactory implements EnumFactory<CdsRuleTriggerType> {

  public CdsRuleTriggerType fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("named-event".equals(codeString))
      return CdsRuleTriggerType.NAMEDEVENT;
    if ("periodic".equals(codeString))
      return CdsRuleTriggerType.PERIODIC;
    if ("data-added".equals(codeString))
      return CdsRuleTriggerType.DATAADDED;
    if ("data-modified".equals(codeString))
      return CdsRuleTriggerType.DATAMODIFIED;
    if ("data-removed".equals(codeString))
      return CdsRuleTriggerType.DATAREMOVED;
    if ("data-accessed".equals(codeString))
      return CdsRuleTriggerType.DATAACCESSED;
    if ("data-access-ended".equals(codeString))
      return CdsRuleTriggerType.DATAACCESSENDED;
    throw new IllegalArgumentException("Unknown CdsRuleTriggerType code '"+codeString+"'");
  }

  public String toCode(CdsRuleTriggerType code) {
    if (code == CdsRuleTriggerType.NAMEDEVENT)
      return "named-event";
    if (code == CdsRuleTriggerType.PERIODIC)
      return "periodic";
    if (code == CdsRuleTriggerType.DATAADDED)
      return "data-added";
    if (code == CdsRuleTriggerType.DATAMODIFIED)
      return "data-modified";
    if (code == CdsRuleTriggerType.DATAREMOVED)
      return "data-removed";
    if (code == CdsRuleTriggerType.DATAACCESSED)
      return "data-accessed";
    if (code == CdsRuleTriggerType.DATAACCESSENDED)
      return "data-access-ended";
    return "?";
  }

    public String toSystem(CdsRuleTriggerType code) {
      return code.getSystem();
      }

}

