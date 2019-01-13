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

public class HspcPractitionerPrivilegeEnumFactory implements EnumFactory<HspcPractitionerPrivilege> {

  public HspcPractitionerPrivilege fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("e6310141-5308-4062-a176-0a03197fb489".equals(codeString))
      return HspcPractitionerPrivilege.E631014153084062A1760A03197FB489;
    if ("0a9a9b65-4f9f-42fa-ade8-0d2592bde220".equals(codeString))
      return HspcPractitionerPrivilege._0A9A9B654F9F42FAADE80D2592BDE220;
    if ("77f1b390-a433-4708-ad3b-070e358868f4".equals(codeString))
      return HspcPractitionerPrivilege._77F1B390A4334708AD3B070E358868F4;
    if ("5750552b-25d4-4eb8-86f9-8233756449f0".equals(codeString))
      return HspcPractitionerPrivilege._5750552B25D44EB886F98233756449F0;
    if ("45fc874f-d787-43f2-a2aa-5954446b163d".equals(codeString))
      return HspcPractitionerPrivilege._45FC874FD78743F2A2AA5954446B163D;
    throw new IllegalArgumentException("Unknown HspcPractitionerPrivilege code '"+codeString+"'");
  }

  public String toCode(HspcPractitionerPrivilege code) {
    if (code == HspcPractitionerPrivilege.E631014153084062A1760A03197FB489)
      return "e6310141-5308-4062-a176-0a03197fb489";
    if (code == HspcPractitionerPrivilege._0A9A9B654F9F42FAADE80D2592BDE220)
      return "0a9a9b65-4f9f-42fa-ade8-0d2592bde220";
    if (code == HspcPractitionerPrivilege._77F1B390A4334708AD3B070E358868F4)
      return "77f1b390-a433-4708-ad3b-070e358868f4";
    if (code == HspcPractitionerPrivilege._5750552B25D44EB886F98233756449F0)
      return "5750552b-25d4-4eb8-86f9-8233756449f0";
    if (code == HspcPractitionerPrivilege._45FC874FD78743F2A2AA5954446B163D)
      return "45fc874f-d787-43f2-a2aa-5954446b163d";
    return "?";
  }

    public String toSystem(HspcPractitionerPrivilege code) {
      return code.getSystem();
      }

}

