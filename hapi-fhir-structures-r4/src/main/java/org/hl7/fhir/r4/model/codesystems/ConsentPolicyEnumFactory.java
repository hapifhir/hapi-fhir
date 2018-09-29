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

public class ConsentPolicyEnumFactory implements EnumFactory<ConsentPolicy> {

  public ConsentPolicy fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("cric".equals(codeString))
      return ConsentPolicy.CRIC;
    if ("illinois-minor-procedure".equals(codeString))
      return ConsentPolicy.ILLINOISMINORPROCEDURE;
    if ("hipaa-auth".equals(codeString))
      return ConsentPolicy.HIPAAAUTH;
    if ("hipaa-npp".equals(codeString))
      return ConsentPolicy.HIPAANPP;
    if ("hipaa-restrictions".equals(codeString))
      return ConsentPolicy.HIPAARESTRICTIONS;
    if ("hipaa-research".equals(codeString))
      return ConsentPolicy.HIPAARESEARCH;
    if ("hipaa-self-pay".equals(codeString))
      return ConsentPolicy.HIPAASELFPAY;
    if ("mdhhs-5515".equals(codeString))
      return ConsentPolicy.MDHHS5515;
    if ("nyssipp".equals(codeString))
      return ConsentPolicy.NYSSIPP;
    if ("va-10-0484".equals(codeString))
      return ConsentPolicy.VA100484;
    if ("va-10-0485".equals(codeString))
      return ConsentPolicy.VA100485;
    if ("va-10-5345".equals(codeString))
      return ConsentPolicy.VA105345;
    if ("va-10-5345a".equals(codeString))
      return ConsentPolicy.VA105345A;
    if ("va-10-5345a-mhv".equals(codeString))
      return ConsentPolicy.VA105345AMHV;
    if ("va-10-10116".equals(codeString))
      return ConsentPolicy.VA1010116;
    if ("va-21-4142".equals(codeString))
      return ConsentPolicy.VA214142;
    if ("ssa-827".equals(codeString))
      return ConsentPolicy.SSA827;
    if ("dch-3927".equals(codeString))
      return ConsentPolicy.DCH3927;
    if ("squaxin".equals(codeString))
      return ConsentPolicy.SQUAXIN;
    if ("nl-lsp".equals(codeString))
      return ConsentPolicy.NLLSP;
    if ("at-elga".equals(codeString))
      return ConsentPolicy.ATELGA;
    if ("nih-hipaa".equals(codeString))
      return ConsentPolicy.NIHHIPAA;
    if ("nci".equals(codeString))
      return ConsentPolicy.NCI;
    if ("nih-grdr".equals(codeString))
      return ConsentPolicy.NIHGRDR;
    if ("nih-527".equals(codeString))
      return ConsentPolicy.NIH527;
    if ("ga4gh".equals(codeString))
      return ConsentPolicy.GA4GH;
    throw new IllegalArgumentException("Unknown ConsentPolicy code '"+codeString+"'");
  }

  public String toCode(ConsentPolicy code) {
    if (code == ConsentPolicy.CRIC)
      return "cric";
    if (code == ConsentPolicy.ILLINOISMINORPROCEDURE)
      return "illinois-minor-procedure";
    if (code == ConsentPolicy.HIPAAAUTH)
      return "hipaa-auth";
    if (code == ConsentPolicy.HIPAANPP)
      return "hipaa-npp";
    if (code == ConsentPolicy.HIPAARESTRICTIONS)
      return "hipaa-restrictions";
    if (code == ConsentPolicy.HIPAARESEARCH)
      return "hipaa-research";
    if (code == ConsentPolicy.HIPAASELFPAY)
      return "hipaa-self-pay";
    if (code == ConsentPolicy.MDHHS5515)
      return "mdhhs-5515";
    if (code == ConsentPolicy.NYSSIPP)
      return "nyssipp";
    if (code == ConsentPolicy.VA100484)
      return "va-10-0484";
    if (code == ConsentPolicy.VA100485)
      return "va-10-0485";
    if (code == ConsentPolicy.VA105345)
      return "va-10-5345";
    if (code == ConsentPolicy.VA105345A)
      return "va-10-5345a";
    if (code == ConsentPolicy.VA105345AMHV)
      return "va-10-5345a-mhv";
    if (code == ConsentPolicy.VA1010116)
      return "va-10-10116";
    if (code == ConsentPolicy.VA214142)
      return "va-21-4142";
    if (code == ConsentPolicy.SSA827)
      return "ssa-827";
    if (code == ConsentPolicy.DCH3927)
      return "dch-3927";
    if (code == ConsentPolicy.SQUAXIN)
      return "squaxin";
    if (code == ConsentPolicy.NLLSP)
      return "nl-lsp";
    if (code == ConsentPolicy.ATELGA)
      return "at-elga";
    if (code == ConsentPolicy.NIHHIPAA)
      return "nih-hipaa";
    if (code == ConsentPolicy.NCI)
      return "nci";
    if (code == ConsentPolicy.NIHGRDR)
      return "nih-grdr";
    if (code == ConsentPolicy.NIH527)
      return "nih-527";
    if (code == ConsentPolicy.GA4GH)
      return "ga4gh";
    return "?";
  }

    public String toSystem(ConsentPolicy code) {
      return code.getSystem();
      }

}

