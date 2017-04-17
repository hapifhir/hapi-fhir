package org.hl7.fhir.dstu3.model.codesystems;

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

// Generated on Mon, Apr 17, 2017 17:38-0400 for FHIR v3.0.1


import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3EntityNameUseR2EnumFactory implements EnumFactory<V3EntityNameUseR2> {

  public V3EntityNameUseR2 fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("Assumed".equals(codeString))
      return V3EntityNameUseR2.ASSUMED;
    if ("A".equals(codeString))
      return V3EntityNameUseR2.A;
    if ("ANON".equals(codeString))
      return V3EntityNameUseR2.ANON;
    if ("I".equals(codeString))
      return V3EntityNameUseR2.I;
    if ("P".equals(codeString))
      return V3EntityNameUseR2.P;
    if ("R".equals(codeString))
      return V3EntityNameUseR2.R;
    if ("C".equals(codeString))
      return V3EntityNameUseR2.C;
    if ("M".equals(codeString))
      return V3EntityNameUseR2.M;
    if ("NameRepresentationUse".equals(codeString))
      return V3EntityNameUseR2.NAMEREPRESENTATIONUSE;
    if ("ABC".equals(codeString))
      return V3EntityNameUseR2.ABC;
    if ("IDE".equals(codeString))
      return V3EntityNameUseR2.IDE;
    if ("SYL".equals(codeString))
      return V3EntityNameUseR2.SYL;
    if ("OLD".equals(codeString))
      return V3EntityNameUseR2.OLD;
    if ("DN".equals(codeString))
      return V3EntityNameUseR2.DN;
    if ("OR".equals(codeString))
      return V3EntityNameUseR2.OR;
    if ("PHON".equals(codeString))
      return V3EntityNameUseR2.PHON;
    if ("SRCH".equals(codeString))
      return V3EntityNameUseR2.SRCH;
    if ("T".equals(codeString))
      return V3EntityNameUseR2.T;
    throw new IllegalArgumentException("Unknown V3EntityNameUseR2 code '"+codeString+"'");
  }

  public String toCode(V3EntityNameUseR2 code) {
    if (code == V3EntityNameUseR2.ASSUMED)
      return "Assumed";
    if (code == V3EntityNameUseR2.A)
      return "A";
    if (code == V3EntityNameUseR2.ANON)
      return "ANON";
    if (code == V3EntityNameUseR2.I)
      return "I";
    if (code == V3EntityNameUseR2.P)
      return "P";
    if (code == V3EntityNameUseR2.R)
      return "R";
    if (code == V3EntityNameUseR2.C)
      return "C";
    if (code == V3EntityNameUseR2.M)
      return "M";
    if (code == V3EntityNameUseR2.NAMEREPRESENTATIONUSE)
      return "NameRepresentationUse";
    if (code == V3EntityNameUseR2.ABC)
      return "ABC";
    if (code == V3EntityNameUseR2.IDE)
      return "IDE";
    if (code == V3EntityNameUseR2.SYL)
      return "SYL";
    if (code == V3EntityNameUseR2.OLD)
      return "OLD";
    if (code == V3EntityNameUseR2.DN)
      return "DN";
    if (code == V3EntityNameUseR2.OR)
      return "OR";
    if (code == V3EntityNameUseR2.PHON)
      return "PHON";
    if (code == V3EntityNameUseR2.SRCH)
      return "SRCH";
    if (code == V3EntityNameUseR2.T)
      return "T";
    return "?";
  }

    public String toSystem(V3EntityNameUseR2 code) {
      return code.getSystem();
      }

}

