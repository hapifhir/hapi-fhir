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

public class AdjudicationEnumFactory implements EnumFactory<Adjudication> {

  public Adjudication fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("total".equals(codeString))
      return Adjudication.TOTAL;
    if ("copay".equals(codeString))
      return Adjudication.COPAY;
    if ("eligible".equals(codeString))
      return Adjudication.ELIGIBLE;
    if ("deductible".equals(codeString))
      return Adjudication.DEDUCTIBLE;
    if ("eligpercent".equals(codeString))
      return Adjudication.ELIGPERCENT;
    if ("tax".equals(codeString))
      return Adjudication.TAX;
    if ("benefit".equals(codeString))
      return Adjudication.BENEFIT;
    throw new IllegalArgumentException("Unknown Adjudication code '"+codeString+"'");
  }

  public String toCode(Adjudication code) {
    if (code == Adjudication.TOTAL)
      return "total";
    if (code == Adjudication.COPAY)
      return "copay";
    if (code == Adjudication.ELIGIBLE)
      return "eligible";
    if (code == Adjudication.DEDUCTIBLE)
      return "deductible";
    if (code == Adjudication.ELIGPERCENT)
      return "eligpercent";
    if (code == Adjudication.TAX)
      return "tax";
    if (code == Adjudication.BENEFIT)
      return "benefit";
    return "?";
  }

    public String toSystem(Adjudication code) {
      return code.getSystem();
      }

}

