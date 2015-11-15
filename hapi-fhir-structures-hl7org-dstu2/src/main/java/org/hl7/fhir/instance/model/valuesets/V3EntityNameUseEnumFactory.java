package org.hl7.fhir.instance.model.valuesets;

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

// Generated on Wed, Nov 11, 2015 10:54-0500 for FHIR v1.0.2


import org.hl7.fhir.instance.model.EnumFactory;

public class V3EntityNameUseEnumFactory implements EnumFactory<V3EntityNameUse> {

  public V3EntityNameUse fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("_NameRepresentationUse".equals(codeString))
      return V3EntityNameUse._NAMEREPRESENTATIONUSE;
    if ("ABC".equals(codeString))
      return V3EntityNameUse.ABC;
    if ("IDE".equals(codeString))
      return V3EntityNameUse.IDE;
    if ("SYL".equals(codeString))
      return V3EntityNameUse.SYL;
    if ("ASGN".equals(codeString))
      return V3EntityNameUse.ASGN;
    if ("C".equals(codeString))
      return V3EntityNameUse.C;
    if ("I".equals(codeString))
      return V3EntityNameUse.I;
    if ("L".equals(codeString))
      return V3EntityNameUse.L;
    if ("OR".equals(codeString))
      return V3EntityNameUse.OR;
    if ("P".equals(codeString))
      return V3EntityNameUse.P;
    if ("A".equals(codeString))
      return V3EntityNameUse.A;
    if ("R".equals(codeString))
      return V3EntityNameUse.R;
    if ("SRCH".equals(codeString))
      return V3EntityNameUse.SRCH;
    if ("PHON".equals(codeString))
      return V3EntityNameUse.PHON;
    if ("SNDX".equals(codeString))
      return V3EntityNameUse.SNDX;
    throw new IllegalArgumentException("Unknown V3EntityNameUse code '"+codeString+"'");
  }

  public String toCode(V3EntityNameUse code) {
    if (code == V3EntityNameUse._NAMEREPRESENTATIONUSE)
      return "_NameRepresentationUse";
    if (code == V3EntityNameUse.ABC)
      return "ABC";
    if (code == V3EntityNameUse.IDE)
      return "IDE";
    if (code == V3EntityNameUse.SYL)
      return "SYL";
    if (code == V3EntityNameUse.ASGN)
      return "ASGN";
    if (code == V3EntityNameUse.C)
      return "C";
    if (code == V3EntityNameUse.I)
      return "I";
    if (code == V3EntityNameUse.L)
      return "L";
    if (code == V3EntityNameUse.OR)
      return "OR";
    if (code == V3EntityNameUse.P)
      return "P";
    if (code == V3EntityNameUse.A)
      return "A";
    if (code == V3EntityNameUse.R)
      return "R";
    if (code == V3EntityNameUse.SRCH)
      return "SRCH";
    if (code == V3EntityNameUse.PHON)
      return "PHON";
    if (code == V3EntityNameUse.SNDX)
      return "SNDX";
    return "?";
  }


}

