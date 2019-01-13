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

public class HspcObservationWeightPrecondEnumFactory implements EnumFactory<HspcObservationWeightPrecond> {

  public HspcObservationWeightPrecond fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("84123".equals(codeString))
      return HspcObservationWeightPrecond._84123;
    if ("84124".equals(codeString))
      return HspcObservationWeightPrecond._84124;
    if ("84127".equals(codeString))
      return HspcObservationWeightPrecond._84127;
    if ("84128".equals(codeString))
      return HspcObservationWeightPrecond._84128;
    if ("84129".equals(codeString))
      return HspcObservationWeightPrecond._84129;
    if ("84130".equals(codeString))
      return HspcObservationWeightPrecond._84130;
    if ("84131".equals(codeString))
      return HspcObservationWeightPrecond._84131;
    if ("84132".equals(codeString))
      return HspcObservationWeightPrecond._84132;
    if ("84133".equals(codeString))
      return HspcObservationWeightPrecond._84133;
    if ("84134".equals(codeString))
      return HspcObservationWeightPrecond._84134;
    if ("84135".equals(codeString))
      return HspcObservationWeightPrecond._84135;
    if ("84136".equals(codeString))
      return HspcObservationWeightPrecond._84136;
    if ("84137".equals(codeString))
      return HspcObservationWeightPrecond._84137;
    throw new IllegalArgumentException("Unknown HspcObservationWeightPrecond code '"+codeString+"'");
  }

  public String toCode(HspcObservationWeightPrecond code) {
    if (code == HspcObservationWeightPrecond._84123)
      return "84123";
    if (code == HspcObservationWeightPrecond._84124)
      return "84124";
    if (code == HspcObservationWeightPrecond._84127)
      return "84127";
    if (code == HspcObservationWeightPrecond._84128)
      return "84128";
    if (code == HspcObservationWeightPrecond._84129)
      return "84129";
    if (code == HspcObservationWeightPrecond._84130)
      return "84130";
    if (code == HspcObservationWeightPrecond._84131)
      return "84131";
    if (code == HspcObservationWeightPrecond._84132)
      return "84132";
    if (code == HspcObservationWeightPrecond._84133)
      return "84133";
    if (code == HspcObservationWeightPrecond._84134)
      return "84134";
    if (code == HspcObservationWeightPrecond._84135)
      return "84135";
    if (code == HspcObservationWeightPrecond._84136)
      return "84136";
    if (code == HspcObservationWeightPrecond._84137)
      return "84137";
    return "?";
  }

    public String toSystem(HspcObservationWeightPrecond code) {
      return code.getSystem();
      }

}

