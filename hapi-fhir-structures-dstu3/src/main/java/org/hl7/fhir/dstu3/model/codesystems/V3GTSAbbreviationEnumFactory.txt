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

public class V3GTSAbbreviationEnumFactory implements EnumFactory<V3GTSAbbreviation> {

  public V3GTSAbbreviation fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("AM".equals(codeString))
      return V3GTSAbbreviation.AM;
    if ("BID".equals(codeString))
      return V3GTSAbbreviation.BID;
    if ("JB".equals(codeString))
      return V3GTSAbbreviation.JB;
    if ("JE".equals(codeString))
      return V3GTSAbbreviation.JE;
    if ("JH".equals(codeString))
      return V3GTSAbbreviation.JH;
    if ("_GTSAbbreviationHolidaysChristianRoman".equals(codeString))
      return V3GTSAbbreviation._GTSABBREVIATIONHOLIDAYSCHRISTIANROMAN;
    if ("JHCHREAS".equals(codeString))
      return V3GTSAbbreviation.JHCHREAS;
    if ("JHCHRGFR".equals(codeString))
      return V3GTSAbbreviation.JHCHRGFR;
    if ("JHCHRNEW".equals(codeString))
      return V3GTSAbbreviation.JHCHRNEW;
    if ("JHCHRPEN".equals(codeString))
      return V3GTSAbbreviation.JHCHRPEN;
    if ("JHCHRXME".equals(codeString))
      return V3GTSAbbreviation.JHCHRXME;
    if ("JHCHRXMS".equals(codeString))
      return V3GTSAbbreviation.JHCHRXMS;
    if ("JHNNL".equals(codeString))
      return V3GTSAbbreviation.JHNNL;
    if ("JHNNLLD".equals(codeString))
      return V3GTSAbbreviation.JHNNLLD;
    if ("JHNNLQD".equals(codeString))
      return V3GTSAbbreviation.JHNNLQD;
    if ("JHNNLSK".equals(codeString))
      return V3GTSAbbreviation.JHNNLSK;
    if ("JHNUS".equals(codeString))
      return V3GTSAbbreviation.JHNUS;
    if ("JHNUSCLM".equals(codeString))
      return V3GTSAbbreviation.JHNUSCLM;
    if ("JHNUSIND".equals(codeString))
      return V3GTSAbbreviation.JHNUSIND;
    if ("JHNUSIND1".equals(codeString))
      return V3GTSAbbreviation.JHNUSIND1;
    if ("JHNUSIND5".equals(codeString))
      return V3GTSAbbreviation.JHNUSIND5;
    if ("JHNUSLBR".equals(codeString))
      return V3GTSAbbreviation.JHNUSLBR;
    if ("JHNUSMEM".equals(codeString))
      return V3GTSAbbreviation.JHNUSMEM;
    if ("JHNUSMEM5".equals(codeString))
      return V3GTSAbbreviation.JHNUSMEM5;
    if ("JHNUSMEM6".equals(codeString))
      return V3GTSAbbreviation.JHNUSMEM6;
    if ("JHNUSMLK".equals(codeString))
      return V3GTSAbbreviation.JHNUSMLK;
    if ("JHNUSPRE".equals(codeString))
      return V3GTSAbbreviation.JHNUSPRE;
    if ("JHNUSTKS".equals(codeString))
      return V3GTSAbbreviation.JHNUSTKS;
    if ("JHNUSTKS5".equals(codeString))
      return V3GTSAbbreviation.JHNUSTKS5;
    if ("JHNUSVET".equals(codeString))
      return V3GTSAbbreviation.JHNUSVET;
    if ("PM".equals(codeString))
      return V3GTSAbbreviation.PM;
    if ("Q4H".equals(codeString))
      return V3GTSAbbreviation.Q4H;
    if ("Q6H".equals(codeString))
      return V3GTSAbbreviation.Q6H;
    if ("QD".equals(codeString))
      return V3GTSAbbreviation.QD;
    if ("QID".equals(codeString))
      return V3GTSAbbreviation.QID;
    if ("QOD".equals(codeString))
      return V3GTSAbbreviation.QOD;
    if ("TID".equals(codeString))
      return V3GTSAbbreviation.TID;
    throw new IllegalArgumentException("Unknown V3GTSAbbreviation code '"+codeString+"'");
  }

  public String toCode(V3GTSAbbreviation code) {
    if (code == V3GTSAbbreviation.AM)
      return "AM";
    if (code == V3GTSAbbreviation.BID)
      return "BID";
    if (code == V3GTSAbbreviation.JB)
      return "JB";
    if (code == V3GTSAbbreviation.JE)
      return "JE";
    if (code == V3GTSAbbreviation.JH)
      return "JH";
    if (code == V3GTSAbbreviation._GTSABBREVIATIONHOLIDAYSCHRISTIANROMAN)
      return "_GTSAbbreviationHolidaysChristianRoman";
    if (code == V3GTSAbbreviation.JHCHREAS)
      return "JHCHREAS";
    if (code == V3GTSAbbreviation.JHCHRGFR)
      return "JHCHRGFR";
    if (code == V3GTSAbbreviation.JHCHRNEW)
      return "JHCHRNEW";
    if (code == V3GTSAbbreviation.JHCHRPEN)
      return "JHCHRPEN";
    if (code == V3GTSAbbreviation.JHCHRXME)
      return "JHCHRXME";
    if (code == V3GTSAbbreviation.JHCHRXMS)
      return "JHCHRXMS";
    if (code == V3GTSAbbreviation.JHNNL)
      return "JHNNL";
    if (code == V3GTSAbbreviation.JHNNLLD)
      return "JHNNLLD";
    if (code == V3GTSAbbreviation.JHNNLQD)
      return "JHNNLQD";
    if (code == V3GTSAbbreviation.JHNNLSK)
      return "JHNNLSK";
    if (code == V3GTSAbbreviation.JHNUS)
      return "JHNUS";
    if (code == V3GTSAbbreviation.JHNUSCLM)
      return "JHNUSCLM";
    if (code == V3GTSAbbreviation.JHNUSIND)
      return "JHNUSIND";
    if (code == V3GTSAbbreviation.JHNUSIND1)
      return "JHNUSIND1";
    if (code == V3GTSAbbreviation.JHNUSIND5)
      return "JHNUSIND5";
    if (code == V3GTSAbbreviation.JHNUSLBR)
      return "JHNUSLBR";
    if (code == V3GTSAbbreviation.JHNUSMEM)
      return "JHNUSMEM";
    if (code == V3GTSAbbreviation.JHNUSMEM5)
      return "JHNUSMEM5";
    if (code == V3GTSAbbreviation.JHNUSMEM6)
      return "JHNUSMEM6";
    if (code == V3GTSAbbreviation.JHNUSMLK)
      return "JHNUSMLK";
    if (code == V3GTSAbbreviation.JHNUSPRE)
      return "JHNUSPRE";
    if (code == V3GTSAbbreviation.JHNUSTKS)
      return "JHNUSTKS";
    if (code == V3GTSAbbreviation.JHNUSTKS5)
      return "JHNUSTKS5";
    if (code == V3GTSAbbreviation.JHNUSVET)
      return "JHNUSVET";
    if (code == V3GTSAbbreviation.PM)
      return "PM";
    if (code == V3GTSAbbreviation.Q4H)
      return "Q4H";
    if (code == V3GTSAbbreviation.Q6H)
      return "Q6H";
    if (code == V3GTSAbbreviation.QD)
      return "QD";
    if (code == V3GTSAbbreviation.QID)
      return "QID";
    if (code == V3GTSAbbreviation.QOD)
      return "QOD";
    if (code == V3GTSAbbreviation.TID)
      return "TID";
    return "?";
  }

    public String toSystem(V3GTSAbbreviation code) {
      return code.getSystem();
      }

}

