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

public class BenefitSubcategoryEnumFactory implements EnumFactory<BenefitSubcategory> {

  public BenefitSubcategory fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("1".equals(codeString))
      return BenefitSubcategory._1;
    if ("2".equals(codeString))
      return BenefitSubcategory._2;
    if ("3".equals(codeString))
      return BenefitSubcategory._3;
    if ("4".equals(codeString))
      return BenefitSubcategory._4;
    if ("5".equals(codeString))
      return BenefitSubcategory._5;
    if ("14".equals(codeString))
      return BenefitSubcategory._14;
    if ("23".equals(codeString))
      return BenefitSubcategory._23;
    if ("24".equals(codeString))
      return BenefitSubcategory._24;
    if ("25".equals(codeString))
      return BenefitSubcategory._25;
    if ("26".equals(codeString))
      return BenefitSubcategory._26;
    if ("27".equals(codeString))
      return BenefitSubcategory._27;
    if ("28".equals(codeString))
      return BenefitSubcategory._28;
    if ("30".equals(codeString))
      return BenefitSubcategory._30;
    if ("35".equals(codeString))
      return BenefitSubcategory._35;
    if ("36".equals(codeString))
      return BenefitSubcategory._36;
    if ("37".equals(codeString))
      return BenefitSubcategory._37;
    if ("49".equals(codeString))
      return BenefitSubcategory._49;
    if ("55".equals(codeString))
      return BenefitSubcategory._55;
    if ("56".equals(codeString))
      return BenefitSubcategory._56;
    if ("61".equals(codeString))
      return BenefitSubcategory._61;
    if ("62".equals(codeString))
      return BenefitSubcategory._62;
    if ("63".equals(codeString))
      return BenefitSubcategory._63;
    if ("69".equals(codeString))
      return BenefitSubcategory._69;
    if ("76".equals(codeString))
      return BenefitSubcategory._76;
    if ("F1".equals(codeString))
      return BenefitSubcategory.F1;
    if ("F3".equals(codeString))
      return BenefitSubcategory.F3;
    if ("F4".equals(codeString))
      return BenefitSubcategory.F4;
    if ("F6".equals(codeString))
      return BenefitSubcategory.F6;
    throw new IllegalArgumentException("Unknown BenefitSubcategory code '"+codeString+"'");
  }

  public String toCode(BenefitSubcategory code) {
    if (code == BenefitSubcategory._1)
      return "1";
    if (code == BenefitSubcategory._2)
      return "2";
    if (code == BenefitSubcategory._3)
      return "3";
    if (code == BenefitSubcategory._4)
      return "4";
    if (code == BenefitSubcategory._5)
      return "5";
    if (code == BenefitSubcategory._14)
      return "14";
    if (code == BenefitSubcategory._23)
      return "23";
    if (code == BenefitSubcategory._24)
      return "24";
    if (code == BenefitSubcategory._25)
      return "25";
    if (code == BenefitSubcategory._26)
      return "26";
    if (code == BenefitSubcategory._27)
      return "27";
    if (code == BenefitSubcategory._28)
      return "28";
    if (code == BenefitSubcategory._30)
      return "30";
    if (code == BenefitSubcategory._35)
      return "35";
    if (code == BenefitSubcategory._36)
      return "36";
    if (code == BenefitSubcategory._37)
      return "37";
    if (code == BenefitSubcategory._49)
      return "49";
    if (code == BenefitSubcategory._55)
      return "55";
    if (code == BenefitSubcategory._56)
      return "56";
    if (code == BenefitSubcategory._61)
      return "61";
    if (code == BenefitSubcategory._62)
      return "62";
    if (code == BenefitSubcategory._63)
      return "63";
    if (code == BenefitSubcategory._69)
      return "69";
    if (code == BenefitSubcategory._76)
      return "76";
    if (code == BenefitSubcategory.F1)
      return "F1";
    if (code == BenefitSubcategory.F3)
      return "F3";
    if (code == BenefitSubcategory.F4)
      return "F4";
    if (code == BenefitSubcategory.F6)
      return "F6";
    return "?";
  }

    public String toSystem(BenefitSubcategory code) {
      return code.getSystem();
      }

}

