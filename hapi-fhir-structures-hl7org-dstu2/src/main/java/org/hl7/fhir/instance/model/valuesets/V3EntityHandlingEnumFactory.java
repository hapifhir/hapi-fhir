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

// Generated on Sat, Aug 22, 2015 23:00-0400 for FHIR v0.5.0


import org.hl7.fhir.instance.model.EnumFactory;

public class V3EntityHandlingEnumFactory implements EnumFactory<V3EntityHandling> {

  public V3EntityHandling fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("AMB".equals(codeString))
      return V3EntityHandling.AMB;
    if ("C37".equals(codeString))
      return V3EntityHandling.C37;
    if ("CAMB".equals(codeString))
      return V3EntityHandling.CAMB;
    if ("CATM".equals(codeString))
      return V3EntityHandling.CATM;
    if ("CFRZ".equals(codeString))
      return V3EntityHandling.CFRZ;
    if ("CREF".equals(codeString))
      return V3EntityHandling.CREF;
    if ("DFRZ".equals(codeString))
      return V3EntityHandling.DFRZ;
    if ("DRY".equals(codeString))
      return V3EntityHandling.DRY;
    if ("FRZ".equals(codeString))
      return V3EntityHandling.FRZ;
    if ("MTLF".equals(codeString))
      return V3EntityHandling.MTLF;
    if ("NTR".equals(codeString))
      return V3EntityHandling.NTR;
    if ("PRTL".equals(codeString))
      return V3EntityHandling.PRTL;
    if ("PSA".equals(codeString))
      return V3EntityHandling.PSA;
    if ("PSO".equals(codeString))
      return V3EntityHandling.PSO;
    if ("REF".equals(codeString))
      return V3EntityHandling.REF;
    if ("SBU".equals(codeString))
      return V3EntityHandling.SBU;
    if ("UFRZ".equals(codeString))
      return V3EntityHandling.UFRZ;
    if ("UPR".equals(codeString))
      return V3EntityHandling.UPR;
    throw new IllegalArgumentException("Unknown V3EntityHandling code '"+codeString+"'");
  }

  public String toCode(V3EntityHandling code) {
    if (code == V3EntityHandling.AMB)
      return "AMB";
    if (code == V3EntityHandling.C37)
      return "C37";
    if (code == V3EntityHandling.CAMB)
      return "CAMB";
    if (code == V3EntityHandling.CATM)
      return "CATM";
    if (code == V3EntityHandling.CFRZ)
      return "CFRZ";
    if (code == V3EntityHandling.CREF)
      return "CREF";
    if (code == V3EntityHandling.DFRZ)
      return "DFRZ";
    if (code == V3EntityHandling.DRY)
      return "DRY";
    if (code == V3EntityHandling.FRZ)
      return "FRZ";
    if (code == V3EntityHandling.MTLF)
      return "MTLF";
    if (code == V3EntityHandling.NTR)
      return "NTR";
    if (code == V3EntityHandling.PRTL)
      return "PRTL";
    if (code == V3EntityHandling.PSA)
      return "PSA";
    if (code == V3EntityHandling.PSO)
      return "PSO";
    if (code == V3EntityHandling.REF)
      return "REF";
    if (code == V3EntityHandling.SBU)
      return "SBU";
    if (code == V3EntityHandling.UFRZ)
      return "UFRZ";
    if (code == V3EntityHandling.UPR)
      return "UPR";
    return "?";
  }


}

