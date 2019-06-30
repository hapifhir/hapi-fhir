package org.hl7.fhir.r4.model.codesystems;

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

// Generated on Thu, Dec 27, 2018 10:06-0500 for FHIR v4.0.0


import org.hl7.fhir.r4.model.EnumFactory;

public class GenderIdentityEnumFactory implements EnumFactory<GenderIdentity> {

  public GenderIdentity fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("transgender-female".equals(codeString))
      return GenderIdentity.TRANSGENDERFEMALE;
    if ("transgender-male".equals(codeString))
      return GenderIdentity.TRANSGENDERMALE;
    if ("non-binary".equals(codeString))
      return GenderIdentity.NONBINARY;
    if ("male".equals(codeString))
      return GenderIdentity.MALE;
    if ("female".equals(codeString))
      return GenderIdentity.FEMALE;
    if ("other".equals(codeString))
      return GenderIdentity.OTHER;
    if ("non-disclose".equals(codeString))
      return GenderIdentity.NONDISCLOSE;
    throw new IllegalArgumentException("Unknown GenderIdentity code '"+codeString+"'");
  }

  public String toCode(GenderIdentity code) {
    if (code == GenderIdentity.TRANSGENDERFEMALE)
      return "transgender-female";
    if (code == GenderIdentity.TRANSGENDERMALE)
      return "transgender-male";
    if (code == GenderIdentity.NONBINARY)
      return "non-binary";
    if (code == GenderIdentity.MALE)
      return "male";
    if (code == GenderIdentity.FEMALE)
      return "female";
    if (code == GenderIdentity.OTHER)
      return "other";
    if (code == GenderIdentity.NONDISCLOSE)
      return "non-disclose";
    return "?";
  }

    public String toSystem(GenderIdentity code) {
      return code.getSystem();
      }

}

