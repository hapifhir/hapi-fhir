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

public class HspcOrganizationOrganizationtypeEnumFactory implements EnumFactory<HspcOrganizationOrganizationtype> {

  public HspcOrganizationOrganizationtype fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("526758010".equals(codeString))
      return HspcOrganizationOrganizationtype._526758010;
    if ("526758011".equals(codeString))
      return HspcOrganizationOrganizationtype._526758011;
    if ("526758012".equals(codeString))
      return HspcOrganizationOrganizationtype._526758012;
    if ("526758013".equals(codeString))
      return HspcOrganizationOrganizationtype._526758013;
    if ("526758014".equals(codeString))
      return HspcOrganizationOrganizationtype._526758014;
    if ("526758015".equals(codeString))
      return HspcOrganizationOrganizationtype._526758015;
    if ("526758016".equals(codeString))
      return HspcOrganizationOrganizationtype._526758016;
    if ("526758017".equals(codeString))
      return HspcOrganizationOrganizationtype._526758017;
    if ("526758018".equals(codeString))
      return HspcOrganizationOrganizationtype._526758018;
    if ("526758019".equals(codeString))
      return HspcOrganizationOrganizationtype._526758019;
    if ("526758020".equals(codeString))
      return HspcOrganizationOrganizationtype._526758020;
    if ("526758021".equals(codeString))
      return HspcOrganizationOrganizationtype._526758021;
    if ("526758022".equals(codeString))
      return HspcOrganizationOrganizationtype._526758022;
    if ("526758023".equals(codeString))
      return HspcOrganizationOrganizationtype._526758023;
    if ("526758024".equals(codeString))
      return HspcOrganizationOrganizationtype._526758024;
    throw new IllegalArgumentException("Unknown HspcOrganizationOrganizationtype code '"+codeString+"'");
  }

  public String toCode(HspcOrganizationOrganizationtype code) {
    if (code == HspcOrganizationOrganizationtype._526758010)
      return "526758010";
    if (code == HspcOrganizationOrganizationtype._526758011)
      return "526758011";
    if (code == HspcOrganizationOrganizationtype._526758012)
      return "526758012";
    if (code == HspcOrganizationOrganizationtype._526758013)
      return "526758013";
    if (code == HspcOrganizationOrganizationtype._526758014)
      return "526758014";
    if (code == HspcOrganizationOrganizationtype._526758015)
      return "526758015";
    if (code == HspcOrganizationOrganizationtype._526758016)
      return "526758016";
    if (code == HspcOrganizationOrganizationtype._526758017)
      return "526758017";
    if (code == HspcOrganizationOrganizationtype._526758018)
      return "526758018";
    if (code == HspcOrganizationOrganizationtype._526758019)
      return "526758019";
    if (code == HspcOrganizationOrganizationtype._526758020)
      return "526758020";
    if (code == HspcOrganizationOrganizationtype._526758021)
      return "526758021";
    if (code == HspcOrganizationOrganizationtype._526758022)
      return "526758022";
    if (code == HspcOrganizationOrganizationtype._526758023)
      return "526758023";
    if (code == HspcOrganizationOrganizationtype._526758024)
      return "526758024";
    return "?";
  }

    public String toSystem(HspcOrganizationOrganizationtype code) {
      return code.getSystem();
      }

}

