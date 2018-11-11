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

public class EventTimingEnumFactory implements EnumFactory<EventTiming> {

  public EventTiming fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("MORN".equals(codeString))
      return EventTiming.MORN;
    if ("MORN.early".equals(codeString))
      return EventTiming.MORN_EARLY;
    if ("MORN.late".equals(codeString))
      return EventTiming.MORN_LATE;
    if ("NOON".equals(codeString))
      return EventTiming.NOON;
    if ("AFT".equals(codeString))
      return EventTiming.AFT;
    if ("AFT.early".equals(codeString))
      return EventTiming.AFT_EARLY;
    if ("AFT.late".equals(codeString))
      return EventTiming.AFT_LATE;
    if ("EVE".equals(codeString))
      return EventTiming.EVE;
    if ("EVE.early".equals(codeString))
      return EventTiming.EVE_EARLY;
    if ("EVE.late".equals(codeString))
      return EventTiming.EVE_LATE;
    if ("NIGHT".equals(codeString))
      return EventTiming.NIGHT;
    if ("PHS".equals(codeString))
      return EventTiming.PHS;
    throw new IllegalArgumentException("Unknown EventTiming code '"+codeString+"'");
  }

  public String toCode(EventTiming code) {
    if (code == EventTiming.MORN)
      return "MORN";
    if (code == EventTiming.MORN_EARLY)
      return "MORN.early";
    if (code == EventTiming.MORN_LATE)
      return "MORN.late";
    if (code == EventTiming.NOON)
      return "NOON";
    if (code == EventTiming.AFT)
      return "AFT";
    if (code == EventTiming.AFT_EARLY)
      return "AFT.early";
    if (code == EventTiming.AFT_LATE)
      return "AFT.late";
    if (code == EventTiming.EVE)
      return "EVE";
    if (code == EventTiming.EVE_EARLY)
      return "EVE.early";
    if (code == EventTiming.EVE_LATE)
      return "EVE.late";
    if (code == EventTiming.NIGHT)
      return "NIGHT";
    if (code == EventTiming.PHS)
      return "PHS";
    return "?";
  }

    public String toSystem(EventTiming code) {
      return code.getSystem();
      }

}

