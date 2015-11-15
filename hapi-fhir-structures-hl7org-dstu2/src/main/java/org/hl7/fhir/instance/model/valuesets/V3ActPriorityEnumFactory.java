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

public class V3ActPriorityEnumFactory implements EnumFactory<V3ActPriority> {

  public V3ActPriority fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("A".equals(codeString))
      return V3ActPriority.A;
    if ("CR".equals(codeString))
      return V3ActPriority.CR;
    if ("CS".equals(codeString))
      return V3ActPriority.CS;
    if ("CSP".equals(codeString))
      return V3ActPriority.CSP;
    if ("CSR".equals(codeString))
      return V3ActPriority.CSR;
    if ("EL".equals(codeString))
      return V3ActPriority.EL;
    if ("EM".equals(codeString))
      return V3ActPriority.EM;
    if ("P".equals(codeString))
      return V3ActPriority.P;
    if ("PRN".equals(codeString))
      return V3ActPriority.PRN;
    if ("R".equals(codeString))
      return V3ActPriority.R;
    if ("RR".equals(codeString))
      return V3ActPriority.RR;
    if ("S".equals(codeString))
      return V3ActPriority.S;
    if ("T".equals(codeString))
      return V3ActPriority.T;
    if ("UD".equals(codeString))
      return V3ActPriority.UD;
    if ("UR".equals(codeString))
      return V3ActPriority.UR;
    throw new IllegalArgumentException("Unknown V3ActPriority code '"+codeString+"'");
  }

  public String toCode(V3ActPriority code) {
    if (code == V3ActPriority.A)
      return "A";
    if (code == V3ActPriority.CR)
      return "CR";
    if (code == V3ActPriority.CS)
      return "CS";
    if (code == V3ActPriority.CSP)
      return "CSP";
    if (code == V3ActPriority.CSR)
      return "CSR";
    if (code == V3ActPriority.EL)
      return "EL";
    if (code == V3ActPriority.EM)
      return "EM";
    if (code == V3ActPriority.P)
      return "P";
    if (code == V3ActPriority.PRN)
      return "PRN";
    if (code == V3ActPriority.R)
      return "R";
    if (code == V3ActPriority.RR)
      return "RR";
    if (code == V3ActPriority.S)
      return "S";
    if (code == V3ActPriority.T)
      return "T";
    if (code == V3ActPriority.UD)
      return "UD";
    if (code == V3ActPriority.UR)
      return "UR";
    return "?";
  }


}

