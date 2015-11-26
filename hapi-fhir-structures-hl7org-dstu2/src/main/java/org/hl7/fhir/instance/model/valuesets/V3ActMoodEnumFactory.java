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

public class V3ActMoodEnumFactory implements EnumFactory<V3ActMood> {

  public V3ActMood fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("_ActMoodCompletionTrack".equals(codeString))
      return V3ActMood._ACTMOODCOMPLETIONTRACK;
    if ("_ActMoodPotential".equals(codeString))
      return V3ActMood._ACTMOODPOTENTIAL;
    if ("DEF".equals(codeString))
      return V3ActMood.DEF;
    if ("PERM".equals(codeString))
      return V3ActMood.PERM;
    if ("SLOT".equals(codeString))
      return V3ActMood.SLOT;
    if ("EVN".equals(codeString))
      return V3ActMood.EVN;
    if ("INT".equals(codeString))
      return V3ActMood.INT;
    if ("_ActMoodDesire".equals(codeString))
      return V3ActMood._ACTMOODDESIRE;
    if ("_ActMoodActRequest".equals(codeString))
      return V3ActMood._ACTMOODACTREQUEST;
    if ("ARQ".equals(codeString))
      return V3ActMood.ARQ;
    if ("PERMRQ".equals(codeString))
      return V3ActMood.PERMRQ;
    if ("RQO".equals(codeString))
      return V3ActMood.RQO;
    if ("PRP".equals(codeString))
      return V3ActMood.PRP;
    if ("RMD".equals(codeString))
      return V3ActMood.RMD;
    if ("PRMS".equals(codeString))
      return V3ActMood.PRMS;
    if ("APT".equals(codeString))
      return V3ActMood.APT;
    if ("_ActMoodPredicate".equals(codeString))
      return V3ActMood._ACTMOODPREDICATE;
    if ("CRT".equals(codeString))
      return V3ActMood.CRT;
    if ("EVN.CRT".equals(codeString))
      return V3ActMood.EVN_CRT;
    if ("GOL.CRT".equals(codeString))
      return V3ActMood.GOL_CRT;
    if ("INT.CRT".equals(codeString))
      return V3ActMood.INT_CRT;
    if ("PRMS.CRT".equals(codeString))
      return V3ActMood.PRMS_CRT;
    if ("RQO.CRT".equals(codeString))
      return V3ActMood.RQO_CRT;
    if ("RSK.CRT".equals(codeString))
      return V3ActMood.RSK_CRT;
    if ("EXPEC".equals(codeString))
      return V3ActMood.EXPEC;
    if ("GOL".equals(codeString))
      return V3ActMood.GOL;
    if ("RSK".equals(codeString))
      return V3ActMood.RSK;
    if ("OPT".equals(codeString))
      return V3ActMood.OPT;
    throw new IllegalArgumentException("Unknown V3ActMood code '"+codeString+"'");
  }

  public String toCode(V3ActMood code) {
    if (code == V3ActMood._ACTMOODCOMPLETIONTRACK)
      return "_ActMoodCompletionTrack";
    if (code == V3ActMood._ACTMOODPOTENTIAL)
      return "_ActMoodPotential";
    if (code == V3ActMood.DEF)
      return "DEF";
    if (code == V3ActMood.PERM)
      return "PERM";
    if (code == V3ActMood.SLOT)
      return "SLOT";
    if (code == V3ActMood.EVN)
      return "EVN";
    if (code == V3ActMood.INT)
      return "INT";
    if (code == V3ActMood._ACTMOODDESIRE)
      return "_ActMoodDesire";
    if (code == V3ActMood._ACTMOODACTREQUEST)
      return "_ActMoodActRequest";
    if (code == V3ActMood.ARQ)
      return "ARQ";
    if (code == V3ActMood.PERMRQ)
      return "PERMRQ";
    if (code == V3ActMood.RQO)
      return "RQO";
    if (code == V3ActMood.PRP)
      return "PRP";
    if (code == V3ActMood.RMD)
      return "RMD";
    if (code == V3ActMood.PRMS)
      return "PRMS";
    if (code == V3ActMood.APT)
      return "APT";
    if (code == V3ActMood._ACTMOODPREDICATE)
      return "_ActMoodPredicate";
    if (code == V3ActMood.CRT)
      return "CRT";
    if (code == V3ActMood.EVN_CRT)
      return "EVN.CRT";
    if (code == V3ActMood.GOL_CRT)
      return "GOL.CRT";
    if (code == V3ActMood.INT_CRT)
      return "INT.CRT";
    if (code == V3ActMood.PRMS_CRT)
      return "PRMS.CRT";
    if (code == V3ActMood.RQO_CRT)
      return "RQO.CRT";
    if (code == V3ActMood.RSK_CRT)
      return "RSK.CRT";
    if (code == V3ActMood.EXPEC)
      return "EXPEC";
    if (code == V3ActMood.GOL)
      return "GOL";
    if (code == V3ActMood.RSK)
      return "RSK";
    if (code == V3ActMood.OPT)
      return "OPT";
    return "?";
  }


}

