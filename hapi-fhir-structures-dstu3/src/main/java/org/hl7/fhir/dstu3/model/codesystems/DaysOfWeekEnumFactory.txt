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

public class DaysOfWeekEnumFactory implements EnumFactory<DaysOfWeek> {

  public DaysOfWeek fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("mon".equals(codeString))
      return DaysOfWeek.MON;
    if ("tue".equals(codeString))
      return DaysOfWeek.TUE;
    if ("wed".equals(codeString))
      return DaysOfWeek.WED;
    if ("thu".equals(codeString))
      return DaysOfWeek.THU;
    if ("fri".equals(codeString))
      return DaysOfWeek.FRI;
    if ("sat".equals(codeString))
      return DaysOfWeek.SAT;
    if ("sun".equals(codeString))
      return DaysOfWeek.SUN;
    throw new IllegalArgumentException("Unknown DaysOfWeek code '"+codeString+"'");
  }

  public String toCode(DaysOfWeek code) {
    if (code == DaysOfWeek.MON)
      return "mon";
    if (code == DaysOfWeek.TUE)
      return "tue";
    if (code == DaysOfWeek.WED)
      return "wed";
    if (code == DaysOfWeek.THU)
      return "thu";
    if (code == DaysOfWeek.FRI)
      return "fri";
    if (code == DaysOfWeek.SAT)
      return "sat";
    if (code == DaysOfWeek.SUN)
      return "sun";
    return "?";
  }

    public String toSystem(DaysOfWeek code) {
      return code.getSystem();
      }

}

