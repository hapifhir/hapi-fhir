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

// Generated on Sat, Mar 3, 2018 18:00-0500 for FHIR v3.2.0


import org.hl7.fhir.r4.model.EnumFactory;

public class V3SubstanceAdminSubstitutionEnumFactory implements EnumFactory<V3SubstanceAdminSubstitution> {

  public V3SubstanceAdminSubstitution fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("_ActSubstanceAdminSubstitutionCode".equals(codeString))
      return V3SubstanceAdminSubstitution._ACTSUBSTANCEADMINSUBSTITUTIONCODE;
    if ("E".equals(codeString))
      return V3SubstanceAdminSubstitution.E;
    if ("EC".equals(codeString))
      return V3SubstanceAdminSubstitution.EC;
    if ("BC".equals(codeString))
      return V3SubstanceAdminSubstitution.BC;
    if ("G".equals(codeString))
      return V3SubstanceAdminSubstitution.G;
    if ("TE".equals(codeString))
      return V3SubstanceAdminSubstitution.TE;
    if ("TB".equals(codeString))
      return V3SubstanceAdminSubstitution.TB;
    if ("TG".equals(codeString))
      return V3SubstanceAdminSubstitution.TG;
    if ("F".equals(codeString))
      return V3SubstanceAdminSubstitution.F;
    if ("N".equals(codeString))
      return V3SubstanceAdminSubstitution.N;
    throw new IllegalArgumentException("Unknown V3SubstanceAdminSubstitution code '"+codeString+"'");
  }

  public String toCode(V3SubstanceAdminSubstitution code) {
    if (code == V3SubstanceAdminSubstitution._ACTSUBSTANCEADMINSUBSTITUTIONCODE)
      return "_ActSubstanceAdminSubstitutionCode";
    if (code == V3SubstanceAdminSubstitution.E)
      return "E";
    if (code == V3SubstanceAdminSubstitution.EC)
      return "EC";
    if (code == V3SubstanceAdminSubstitution.BC)
      return "BC";
    if (code == V3SubstanceAdminSubstitution.G)
      return "G";
    if (code == V3SubstanceAdminSubstitution.TE)
      return "TE";
    if (code == V3SubstanceAdminSubstitution.TB)
      return "TB";
    if (code == V3SubstanceAdminSubstitution.TG)
      return "TG";
    if (code == V3SubstanceAdminSubstitution.F)
      return "F";
    if (code == V3SubstanceAdminSubstitution.N)
      return "N";
    return "?";
  }

    public String toSystem(V3SubstanceAdminSubstitution code) {
      return code.getSystem();
      }

}

