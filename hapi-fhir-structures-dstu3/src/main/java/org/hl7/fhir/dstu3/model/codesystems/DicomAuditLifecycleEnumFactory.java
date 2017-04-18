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

public class DicomAuditLifecycleEnumFactory implements EnumFactory<DicomAuditLifecycle> {

  public DicomAuditLifecycle fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("1".equals(codeString))
      return DicomAuditLifecycle._1;
    if ("2".equals(codeString))
      return DicomAuditLifecycle._2;
    if ("3".equals(codeString))
      return DicomAuditLifecycle._3;
    if ("4".equals(codeString))
      return DicomAuditLifecycle._4;
    if ("5".equals(codeString))
      return DicomAuditLifecycle._5;
    if ("6".equals(codeString))
      return DicomAuditLifecycle._6;
    if ("7".equals(codeString))
      return DicomAuditLifecycle._7;
    if ("8".equals(codeString))
      return DicomAuditLifecycle._8;
    if ("9".equals(codeString))
      return DicomAuditLifecycle._9;
    if ("10".equals(codeString))
      return DicomAuditLifecycle._10;
    if ("11".equals(codeString))
      return DicomAuditLifecycle._11;
    if ("12".equals(codeString))
      return DicomAuditLifecycle._12;
    if ("13".equals(codeString))
      return DicomAuditLifecycle._13;
    if ("14".equals(codeString))
      return DicomAuditLifecycle._14;
    if ("15".equals(codeString))
      return DicomAuditLifecycle._15;
    throw new IllegalArgumentException("Unknown DicomAuditLifecycle code '"+codeString+"'");
  }

  public String toCode(DicomAuditLifecycle code) {
    if (code == DicomAuditLifecycle._1)
      return "1";
    if (code == DicomAuditLifecycle._2)
      return "2";
    if (code == DicomAuditLifecycle._3)
      return "3";
    if (code == DicomAuditLifecycle._4)
      return "4";
    if (code == DicomAuditLifecycle._5)
      return "5";
    if (code == DicomAuditLifecycle._6)
      return "6";
    if (code == DicomAuditLifecycle._7)
      return "7";
    if (code == DicomAuditLifecycle._8)
      return "8";
    if (code == DicomAuditLifecycle._9)
      return "9";
    if (code == DicomAuditLifecycle._10)
      return "10";
    if (code == DicomAuditLifecycle._11)
      return "11";
    if (code == DicomAuditLifecycle._12)
      return "12";
    if (code == DicomAuditLifecycle._13)
      return "13";
    if (code == DicomAuditLifecycle._14)
      return "14";
    if (code == DicomAuditLifecycle._15)
      return "15";
    return "?";
  }

    public String toSystem(DicomAuditLifecycle code) {
      return code.getSystem();
      }

}

