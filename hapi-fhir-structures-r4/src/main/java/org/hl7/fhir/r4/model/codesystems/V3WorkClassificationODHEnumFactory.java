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

// Generated on Thu, Sep 13, 2018 09:04-0400 for FHIR v3.5.0


import org.hl7.fhir.r4.model.EnumFactory;

public class V3WorkClassificationODHEnumFactory implements EnumFactory<V3WorkClassificationODH> {

  public V3WorkClassificationODH fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("PWAF".equals(codeString))
      return V3WorkClassificationODH.PWAF;
    if ("PWFG".equals(codeString))
      return V3WorkClassificationODH.PWFG;
    if ("PWLG".equals(codeString))
      return V3WorkClassificationODH.PWLG;
    if ("PWNSE".equals(codeString))
      return V3WorkClassificationODH.PWNSE;
    if ("PWSE".equals(codeString))
      return V3WorkClassificationODH.PWSE;
    if ("PWSG".equals(codeString))
      return V3WorkClassificationODH.PWSG;
    if ("UWNSE".equals(codeString))
      return V3WorkClassificationODH.UWNSE;
    if ("UWSE".equals(codeString))
      return V3WorkClassificationODH.UWSE;
    if ("VW".equals(codeString))
      return V3WorkClassificationODH.VW;
    throw new IllegalArgumentException("Unknown V3WorkClassificationODH code '"+codeString+"'");
  }

  public String toCode(V3WorkClassificationODH code) {
    if (code == V3WorkClassificationODH.PWAF)
      return "PWAF";
    if (code == V3WorkClassificationODH.PWFG)
      return "PWFG";
    if (code == V3WorkClassificationODH.PWLG)
      return "PWLG";
    if (code == V3WorkClassificationODH.PWNSE)
      return "PWNSE";
    if (code == V3WorkClassificationODH.PWSE)
      return "PWSE";
    if (code == V3WorkClassificationODH.PWSG)
      return "PWSG";
    if (code == V3WorkClassificationODH.UWNSE)
      return "UWNSE";
    if (code == V3WorkClassificationODH.UWSE)
      return "UWSE";
    if (code == V3WorkClassificationODH.VW)
      return "VW";
    return "?";
  }

    public String toSystem(V3WorkClassificationODH code) {
      return code.getSystem();
      }

}

