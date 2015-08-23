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

// Generated on Sat, Aug 22, 2015 23:00-0400 for FHIR v0.5.0


import org.hl7.fhir.instance.model.EnumFactory;

public class ObjectLifecycleEnumFactory implements EnumFactory<ObjectLifecycle> {

  public ObjectLifecycle fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("1".equals(codeString))
      return ObjectLifecycle._1;
    if ("2".equals(codeString))
      return ObjectLifecycle._2;
    if ("3".equals(codeString))
      return ObjectLifecycle._3;
    if ("4".equals(codeString))
      return ObjectLifecycle._4;
    if ("5".equals(codeString))
      return ObjectLifecycle._5;
    if ("6".equals(codeString))
      return ObjectLifecycle._6;
    if ("7".equals(codeString))
      return ObjectLifecycle._7;
    if ("8".equals(codeString))
      return ObjectLifecycle._8;
    if ("9".equals(codeString))
      return ObjectLifecycle._9;
    if ("10".equals(codeString))
      return ObjectLifecycle._10;
    if ("11".equals(codeString))
      return ObjectLifecycle._11;
    if ("12".equals(codeString))
      return ObjectLifecycle._12;
    if ("13".equals(codeString))
      return ObjectLifecycle._13;
    if ("14".equals(codeString))
      return ObjectLifecycle._14;
    if ("15".equals(codeString))
      return ObjectLifecycle._15;
    throw new IllegalArgumentException("Unknown ObjectLifecycle code '"+codeString+"'");
  }

  public String toCode(ObjectLifecycle code) {
    if (code == ObjectLifecycle._1)
      return "1";
    if (code == ObjectLifecycle._2)
      return "2";
    if (code == ObjectLifecycle._3)
      return "3";
    if (code == ObjectLifecycle._4)
      return "4";
    if (code == ObjectLifecycle._5)
      return "5";
    if (code == ObjectLifecycle._6)
      return "6";
    if (code == ObjectLifecycle._7)
      return "7";
    if (code == ObjectLifecycle._8)
      return "8";
    if (code == ObjectLifecycle._9)
      return "9";
    if (code == ObjectLifecycle._10)
      return "10";
    if (code == ObjectLifecycle._11)
      return "11";
    if (code == ObjectLifecycle._12)
      return "12";
    if (code == ObjectLifecycle._13)
      return "13";
    if (code == ObjectLifecycle._14)
      return "14";
    if (code == ObjectLifecycle._15)
      return "15";
    return "?";
  }


}

