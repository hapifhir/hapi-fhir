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

public class CertaintySubcomponentTypeEnumFactory implements EnumFactory<CertaintySubcomponentType> {

  public CertaintySubcomponentType fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("RiskOfBias".equals(codeString))
      return CertaintySubcomponentType.RISKOFBIAS;
    if ("Inconsistency".equals(codeString))
      return CertaintySubcomponentType.INCONSISTENCY;
    if ("Indirectness".equals(codeString))
      return CertaintySubcomponentType.INDIRECTNESS;
    if ("Imprecision".equals(codeString))
      return CertaintySubcomponentType.IMPRECISION;
    if ("PublicationBias".equals(codeString))
      return CertaintySubcomponentType.PUBLICATIONBIAS;
    if ("DoseResponseGradient".equals(codeString))
      return CertaintySubcomponentType.DOSERESPONSEGRADIENT;
    if ("PlausibleConfounding".equals(codeString))
      return CertaintySubcomponentType.PLAUSIBLECONFOUNDING;
    if ("LargeEffect".equals(codeString))
      return CertaintySubcomponentType.LARGEEFFECT;
    throw new IllegalArgumentException("Unknown CertaintySubcomponentType code '"+codeString+"'");
  }

  public String toCode(CertaintySubcomponentType code) {
    if (code == CertaintySubcomponentType.RISKOFBIAS)
      return "RiskOfBias";
    if (code == CertaintySubcomponentType.INCONSISTENCY)
      return "Inconsistency";
    if (code == CertaintySubcomponentType.INDIRECTNESS)
      return "Indirectness";
    if (code == CertaintySubcomponentType.IMPRECISION)
      return "Imprecision";
    if (code == CertaintySubcomponentType.PUBLICATIONBIAS)
      return "PublicationBias";
    if (code == CertaintySubcomponentType.DOSERESPONSEGRADIENT)
      return "DoseResponseGradient";
    if (code == CertaintySubcomponentType.PLAUSIBLECONFOUNDING)
      return "PlausibleConfounding";
    if (code == CertaintySubcomponentType.LARGEEFFECT)
      return "LargeEffect";
    return "?";
  }

    public String toSystem(CertaintySubcomponentType code) {
      return code.getSystem();
      }

}

