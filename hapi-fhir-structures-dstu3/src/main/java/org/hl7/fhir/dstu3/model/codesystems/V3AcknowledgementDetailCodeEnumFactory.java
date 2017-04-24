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

public class V3AcknowledgementDetailCodeEnumFactory implements EnumFactory<V3AcknowledgementDetailCode> {

  public V3AcknowledgementDetailCode fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("_AcknowledgementDetailNotSupportedCode".equals(codeString))
      return V3AcknowledgementDetailCode._ACKNOWLEDGEMENTDETAILNOTSUPPORTEDCODE;
    if ("NS200".equals(codeString))
      return V3AcknowledgementDetailCode.NS200;
    if ("NS202".equals(codeString))
      return V3AcknowledgementDetailCode.NS202;
    if ("NS203".equals(codeString))
      return V3AcknowledgementDetailCode.NS203;
    if ("NS250".equals(codeString))
      return V3AcknowledgementDetailCode.NS250;
    if ("NS260".equals(codeString))
      return V3AcknowledgementDetailCode.NS260;
    if ("NS261".equals(codeString))
      return V3AcknowledgementDetailCode.NS261;
    if ("INTERR".equals(codeString))
      return V3AcknowledgementDetailCode.INTERR;
    if ("NOSTORE".equals(codeString))
      return V3AcknowledgementDetailCode.NOSTORE;
    if ("RTEDEST".equals(codeString))
      return V3AcknowledgementDetailCode.RTEDEST;
    if ("RTUDEST".equals(codeString))
      return V3AcknowledgementDetailCode.RTUDEST;
    if ("RTWDEST".equals(codeString))
      return V3AcknowledgementDetailCode.RTWDEST;
    if ("SYN".equals(codeString))
      return V3AcknowledgementDetailCode.SYN;
    if ("SYN102".equals(codeString))
      return V3AcknowledgementDetailCode.SYN102;
    if ("SYN105".equals(codeString))
      return V3AcknowledgementDetailCode.SYN105;
    if ("SYN100".equals(codeString))
      return V3AcknowledgementDetailCode.SYN100;
    if ("SYN101".equals(codeString))
      return V3AcknowledgementDetailCode.SYN101;
    if ("SYN114".equals(codeString))
      return V3AcknowledgementDetailCode.SYN114;
    if ("SYN106".equals(codeString))
      return V3AcknowledgementDetailCode.SYN106;
    if ("SYN103".equals(codeString))
      return V3AcknowledgementDetailCode.SYN103;
    if ("SYN104".equals(codeString))
      return V3AcknowledgementDetailCode.SYN104;
    if ("SYN107".equals(codeString))
      return V3AcknowledgementDetailCode.SYN107;
    if ("SYN108".equals(codeString))
      return V3AcknowledgementDetailCode.SYN108;
    if ("SYN110".equals(codeString))
      return V3AcknowledgementDetailCode.SYN110;
    if ("SYN112".equals(codeString))
      return V3AcknowledgementDetailCode.SYN112;
    if ("SYN109".equals(codeString))
      return V3AcknowledgementDetailCode.SYN109;
    if ("SYN111".equals(codeString))
      return V3AcknowledgementDetailCode.SYN111;
    if ("SYN113".equals(codeString))
      return V3AcknowledgementDetailCode.SYN113;
    throw new IllegalArgumentException("Unknown V3AcknowledgementDetailCode code '"+codeString+"'");
  }

  public String toCode(V3AcknowledgementDetailCode code) {
    if (code == V3AcknowledgementDetailCode._ACKNOWLEDGEMENTDETAILNOTSUPPORTEDCODE)
      return "_AcknowledgementDetailNotSupportedCode";
    if (code == V3AcknowledgementDetailCode.NS200)
      return "NS200";
    if (code == V3AcknowledgementDetailCode.NS202)
      return "NS202";
    if (code == V3AcknowledgementDetailCode.NS203)
      return "NS203";
    if (code == V3AcknowledgementDetailCode.NS250)
      return "NS250";
    if (code == V3AcknowledgementDetailCode.NS260)
      return "NS260";
    if (code == V3AcknowledgementDetailCode.NS261)
      return "NS261";
    if (code == V3AcknowledgementDetailCode.INTERR)
      return "INTERR";
    if (code == V3AcknowledgementDetailCode.NOSTORE)
      return "NOSTORE";
    if (code == V3AcknowledgementDetailCode.RTEDEST)
      return "RTEDEST";
    if (code == V3AcknowledgementDetailCode.RTUDEST)
      return "RTUDEST";
    if (code == V3AcknowledgementDetailCode.RTWDEST)
      return "RTWDEST";
    if (code == V3AcknowledgementDetailCode.SYN)
      return "SYN";
    if (code == V3AcknowledgementDetailCode.SYN102)
      return "SYN102";
    if (code == V3AcknowledgementDetailCode.SYN105)
      return "SYN105";
    if (code == V3AcknowledgementDetailCode.SYN100)
      return "SYN100";
    if (code == V3AcknowledgementDetailCode.SYN101)
      return "SYN101";
    if (code == V3AcknowledgementDetailCode.SYN114)
      return "SYN114";
    if (code == V3AcknowledgementDetailCode.SYN106)
      return "SYN106";
    if (code == V3AcknowledgementDetailCode.SYN103)
      return "SYN103";
    if (code == V3AcknowledgementDetailCode.SYN104)
      return "SYN104";
    if (code == V3AcknowledgementDetailCode.SYN107)
      return "SYN107";
    if (code == V3AcknowledgementDetailCode.SYN108)
      return "SYN108";
    if (code == V3AcknowledgementDetailCode.SYN110)
      return "SYN110";
    if (code == V3AcknowledgementDetailCode.SYN112)
      return "SYN112";
    if (code == V3AcknowledgementDetailCode.SYN109)
      return "SYN109";
    if (code == V3AcknowledgementDetailCode.SYN111)
      return "SYN111";
    if (code == V3AcknowledgementDetailCode.SYN113)
      return "SYN113";
    return "?";
  }

    public String toSystem(V3AcknowledgementDetailCode code) {
      return code.getSystem();
      }

}

