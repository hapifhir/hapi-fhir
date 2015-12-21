package org.hl7.fhir.dstu21.model.valuesets;

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

// Generated on Sun, Dec 20, 2015 20:55-0500 for FHIR v1.2.0


import org.hl7.fhir.dstu21.model.EnumFactory;

public class BenefitSubcategoryEnumFactory implements EnumFactory<BenefitSubcategory> {

  public BenefitSubcategory fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("oral-basic".equals(codeString))
      return BenefitSubcategory.ORALBASIC;
    if ("oral-major".equals(codeString))
      return BenefitSubcategory.ORALMAJOR;
    if ("oral-ortho".equals(codeString))
      return BenefitSubcategory.ORALORTHO;
    if ("vision-exam".equals(codeString))
      return BenefitSubcategory.VISIONEXAM;
    if ("vision-glasses".equals(codeString))
      return BenefitSubcategory.VISIONGLASSES;
    if ("vision-contacts".equals(codeString))
      return BenefitSubcategory.VISIONCONTACTS;
    if ("medical-primarycare".equals(codeString))
      return BenefitSubcategory.MEDICALPRIMARYCARE;
    if ("pharmacy-dispense".equals(codeString))
      return BenefitSubcategory.PHARMACYDISPENSE;
    throw new IllegalArgumentException("Unknown BenefitSubcategory code '"+codeString+"'");
  }

  public String toCode(BenefitSubcategory code) {
    if (code == BenefitSubcategory.ORALBASIC)
      return "oral-basic";
    if (code == BenefitSubcategory.ORALMAJOR)
      return "oral-major";
    if (code == BenefitSubcategory.ORALORTHO)
      return "oral-ortho";
    if (code == BenefitSubcategory.VISIONEXAM)
      return "vision-exam";
    if (code == BenefitSubcategory.VISIONGLASSES)
      return "vision-glasses";
    if (code == BenefitSubcategory.VISIONCONTACTS)
      return "vision-contacts";
    if (code == BenefitSubcategory.MEDICALPRIMARYCARE)
      return "medical-primarycare";
    if (code == BenefitSubcategory.PHARMACYDISPENSE)
      return "pharmacy-dispense";
    return "?";
  }

    public String toSystem(BenefitSubcategory code) {
      return code.getSystem();
      }

}

