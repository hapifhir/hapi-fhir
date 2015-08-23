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

public class V3CalendarCycleEnumFactory implements EnumFactory<V3CalendarCycle> {

  public V3CalendarCycle fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("_CalendarCycleOneLetter".equals(codeString))
      return V3CalendarCycle._CALENDARCYCLEONELETTER;
    if ("CW".equals(codeString))
      return V3CalendarCycle.CW;
    if ("CY".equals(codeString))
      return V3CalendarCycle.CY;
    if ("D".equals(codeString))
      return V3CalendarCycle.D;
    if ("DW".equals(codeString))
      return V3CalendarCycle.DW;
    if ("H".equals(codeString))
      return V3CalendarCycle.H;
    if ("M".equals(codeString))
      return V3CalendarCycle.M;
    if ("N".equals(codeString))
      return V3CalendarCycle.N;
    if ("S".equals(codeString))
      return V3CalendarCycle.S;
    if ("_CalendarCycleTwoLetter".equals(codeString))
      return V3CalendarCycle._CALENDARCYCLETWOLETTER;
    if ("CD".equals(codeString))
      return V3CalendarCycle.CD;
    if ("CH".equals(codeString))
      return V3CalendarCycle.CH;
    if ("CM".equals(codeString))
      return V3CalendarCycle.CM;
    if ("CN".equals(codeString))
      return V3CalendarCycle.CN;
    if ("CS".equals(codeString))
      return V3CalendarCycle.CS;
    if ("DY".equals(codeString))
      return V3CalendarCycle.DY;
    if ("WY".equals(codeString))
      return V3CalendarCycle.WY;
    if ("WM".equals(codeString))
      return V3CalendarCycle.WM;
    throw new IllegalArgumentException("Unknown V3CalendarCycle code '"+codeString+"'");
  }

  public String toCode(V3CalendarCycle code) {
    if (code == V3CalendarCycle._CALENDARCYCLEONELETTER)
      return "_CalendarCycleOneLetter";
    if (code == V3CalendarCycle.CW)
      return "CW";
    if (code == V3CalendarCycle.CY)
      return "CY";
    if (code == V3CalendarCycle.D)
      return "D";
    if (code == V3CalendarCycle.DW)
      return "DW";
    if (code == V3CalendarCycle.H)
      return "H";
    if (code == V3CalendarCycle.M)
      return "M";
    if (code == V3CalendarCycle.N)
      return "N";
    if (code == V3CalendarCycle.S)
      return "S";
    if (code == V3CalendarCycle._CALENDARCYCLETWOLETTER)
      return "_CalendarCycleTwoLetter";
    if (code == V3CalendarCycle.CD)
      return "CD";
    if (code == V3CalendarCycle.CH)
      return "CH";
    if (code == V3CalendarCycle.CM)
      return "CM";
    if (code == V3CalendarCycle.CN)
      return "CN";
    if (code == V3CalendarCycle.CS)
      return "CS";
    if (code == V3CalendarCycle.DY)
      return "DY";
    if (code == V3CalendarCycle.WY)
      return "WY";
    if (code == V3CalendarCycle.WM)
      return "WM";
    return "?";
  }


}

