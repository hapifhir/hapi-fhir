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

public class ConsentCategoryEnumFactory implements EnumFactory<ConsentCategory> {

  public ConsentCategory fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("42-CFR-2".equals(codeString))
      return ConsentCategory._42CFR2;
    if ("ACD".equals(codeString))
      return ConsentCategory.ACD;
    if ("CRIC".equals(codeString))
      return ConsentCategory.CRIC;
    if ("DNR".equals(codeString))
      return ConsentCategory.DNR;
    if ("EMRGONLY".equals(codeString))
      return ConsentCategory.EMRGONLY;
    if ("Illinois-Minor-Procedure".equals(codeString))
      return ConsentCategory.ILLINOISMINORPROCEDURE;
    if ("HCD".equals(codeString))
      return ConsentCategory.HCD;
    if ("HIPAA-Auth".equals(codeString))
      return ConsentCategory.HIPAAAUTH;
    if ("HIPAA-NPP".equals(codeString))
      return ConsentCategory.HIPAANPP;
    if ("HIPAA-Restrictions".equals(codeString))
      return ConsentCategory.HIPAARESTRICTIONS;
    if ("HIPAA-Research".equals(codeString))
      return ConsentCategory.HIPAARESEARCH;
    if ("HIPAA-Self-Pay".equals(codeString))
      return ConsentCategory.HIPAASELFPAY;
    if ("MDHHS-5515".equals(codeString))
      return ConsentCategory.MDHHS5515;
    if ("NYSSIPP".equals(codeString))
      return ConsentCategory.NYSSIPP;
    if ("NPP".equals(codeString))
      return ConsentCategory.NPP;
    if ("POLST".equals(codeString))
      return ConsentCategory.POLST;
    if ("RESEARCH".equals(codeString))
      return ConsentCategory.RESEARCH;
    if ("RSDID".equals(codeString))
      return ConsentCategory.RSDID;
    if ("RSREID".equals(codeString))
      return ConsentCategory.RSREID;
    if ("SSA-827".equals(codeString))
      return ConsentCategory.SSA827;
    if ("VA-10-0484".equals(codeString))
      return ConsentCategory.VA100484;
    if ("VA-10-0485".equals(codeString))
      return ConsentCategory.VA100485;
    if ("VA-10-5345".equals(codeString))
      return ConsentCategory.VA105345;
    if ("VA-10-5345a".equals(codeString))
      return ConsentCategory.VA105345A;
    if ("VA-10-5345a-MHV".equals(codeString))
      return ConsentCategory.VA105345AMHV;
    if ("VA-10-10116".equals(codeString))
      return ConsentCategory.VA1010116;
    if ("VA-21-4142".equals(codeString))
      return ConsentCategory.VA214142;
    throw new IllegalArgumentException("Unknown ConsentCategory code '"+codeString+"'");
  }

  public String toCode(ConsentCategory code) {
    if (code == ConsentCategory._42CFR2)
      return "42-CFR-2";
    if (code == ConsentCategory.ACD)
      return "ACD";
    if (code == ConsentCategory.CRIC)
      return "CRIC";
    if (code == ConsentCategory.DNR)
      return "DNR";
    if (code == ConsentCategory.EMRGONLY)
      return "EMRGONLY";
    if (code == ConsentCategory.ILLINOISMINORPROCEDURE)
      return "Illinois-Minor-Procedure";
    if (code == ConsentCategory.HCD)
      return "HCD";
    if (code == ConsentCategory.HIPAAAUTH)
      return "HIPAA-Auth";
    if (code == ConsentCategory.HIPAANPP)
      return "HIPAA-NPP";
    if (code == ConsentCategory.HIPAARESTRICTIONS)
      return "HIPAA-Restrictions";
    if (code == ConsentCategory.HIPAARESEARCH)
      return "HIPAA-Research";
    if (code == ConsentCategory.HIPAASELFPAY)
      return "HIPAA-Self-Pay";
    if (code == ConsentCategory.MDHHS5515)
      return "MDHHS-5515";
    if (code == ConsentCategory.NYSSIPP)
      return "NYSSIPP";
    if (code == ConsentCategory.NPP)
      return "NPP";
    if (code == ConsentCategory.POLST)
      return "POLST";
    if (code == ConsentCategory.RESEARCH)
      return "RESEARCH";
    if (code == ConsentCategory.RSDID)
      return "RSDID";
    if (code == ConsentCategory.RSREID)
      return "RSREID";
    if (code == ConsentCategory.SSA827)
      return "SSA-827";
    if (code == ConsentCategory.VA100484)
      return "VA-10-0484";
    if (code == ConsentCategory.VA100485)
      return "VA-10-0485";
    if (code == ConsentCategory.VA105345)
      return "VA-10-5345";
    if (code == ConsentCategory.VA105345A)
      return "VA-10-5345a";
    if (code == ConsentCategory.VA105345AMHV)
      return "VA-10-5345a-MHV";
    if (code == ConsentCategory.VA1010116)
      return "VA-10-10116";
    if (code == ConsentCategory.VA214142)
      return "VA-21-4142";
    return "?";
  }

    public String toSystem(ConsentCategory code) {
      return code.getSystem();
      }

}

