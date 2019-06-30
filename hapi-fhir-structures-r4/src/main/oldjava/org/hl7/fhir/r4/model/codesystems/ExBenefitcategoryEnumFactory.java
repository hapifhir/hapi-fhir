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

public class ExBenefitcategoryEnumFactory implements EnumFactory<ExBenefitcategory> {

  public ExBenefitcategory fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("1".equals(codeString))
      return ExBenefitcategory._1;
    if ("2".equals(codeString))
      return ExBenefitcategory._2;
    if ("3".equals(codeString))
      return ExBenefitcategory._3;
    if ("4".equals(codeString))
      return ExBenefitcategory._4;
    if ("5".equals(codeString))
      return ExBenefitcategory._5;
    if ("14".equals(codeString))
      return ExBenefitcategory._14;
    if ("23".equals(codeString))
      return ExBenefitcategory._23;
    if ("24".equals(codeString))
      return ExBenefitcategory._24;
    if ("25".equals(codeString))
      return ExBenefitcategory._25;
    if ("26".equals(codeString))
      return ExBenefitcategory._26;
    if ("27".equals(codeString))
      return ExBenefitcategory._27;
    if ("28".equals(codeString))
      return ExBenefitcategory._28;
    if ("30".equals(codeString))
      return ExBenefitcategory._30;
    if ("35".equals(codeString))
      return ExBenefitcategory._35;
    if ("36".equals(codeString))
      return ExBenefitcategory._36;
    if ("37".equals(codeString))
      return ExBenefitcategory._37;
    if ("49".equals(codeString))
      return ExBenefitcategory._49;
    if ("55".equals(codeString))
      return ExBenefitcategory._55;
    if ("56".equals(codeString))
      return ExBenefitcategory._56;
    if ("61".equals(codeString))
      return ExBenefitcategory._61;
    if ("62".equals(codeString))
      return ExBenefitcategory._62;
    if ("63".equals(codeString))
      return ExBenefitcategory._63;
    if ("69".equals(codeString))
      return ExBenefitcategory._69;
    if ("76".equals(codeString))
      return ExBenefitcategory._76;
    if ("F1".equals(codeString))
      return ExBenefitcategory.F1;
    if ("F3".equals(codeString))
      return ExBenefitcategory.F3;
    if ("F4".equals(codeString))
      return ExBenefitcategory.F4;
    if ("F6".equals(codeString))
      return ExBenefitcategory.F6;
    throw new IllegalArgumentException("Unknown ExBenefitcategory code '"+codeString+"'");
  }

  public String toCode(ExBenefitcategory code) {
    if (code == ExBenefitcategory._1)
      return "1";
    if (code == ExBenefitcategory._2)
      return "2";
    if (code == ExBenefitcategory._3)
      return "3";
    if (code == ExBenefitcategory._4)
      return "4";
    if (code == ExBenefitcategory._5)
      return "5";
    if (code == ExBenefitcategory._14)
      return "14";
    if (code == ExBenefitcategory._23)
      return "23";
    if (code == ExBenefitcategory._24)
      return "24";
    if (code == ExBenefitcategory._25)
      return "25";
    if (code == ExBenefitcategory._26)
      return "26";
    if (code == ExBenefitcategory._27)
      return "27";
    if (code == ExBenefitcategory._28)
      return "28";
    if (code == ExBenefitcategory._30)
      return "30";
    if (code == ExBenefitcategory._35)
      return "35";
    if (code == ExBenefitcategory._36)
      return "36";
    if (code == ExBenefitcategory._37)
      return "37";
    if (code == ExBenefitcategory._49)
      return "49";
    if (code == ExBenefitcategory._55)
      return "55";
    if (code == ExBenefitcategory._56)
      return "56";
    if (code == ExBenefitcategory._61)
      return "61";
    if (code == ExBenefitcategory._62)
      return "62";
    if (code == ExBenefitcategory._63)
      return "63";
    if (code == ExBenefitcategory._69)
      return "69";
    if (code == ExBenefitcategory._76)
      return "76";
    if (code == ExBenefitcategory.F1)
      return "F1";
    if (code == ExBenefitcategory.F3)
      return "F3";
    if (code == ExBenefitcategory.F4)
      return "F4";
    if (code == ExBenefitcategory.F6)
      return "F6";
    return "?";
  }

    public String toSystem(ExBenefitcategory code) {
      return code.getSystem();
      }

}

