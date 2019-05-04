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

// Generated on Thu, Dec 27, 2018 10:06-0500 for FHIR v4.0.0


import org.hl7.fhir.r4.model.EnumFactory;

public class HistoryAbsentReasonEnumFactory implements EnumFactory<HistoryAbsentReason> {

  public HistoryAbsentReason fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("subject-unknown".equals(codeString))
      return HistoryAbsentReason.SUBJECTUNKNOWN;
    if ("withheld".equals(codeString))
      return HistoryAbsentReason.WITHHELD;
    if ("unable-to-obtain".equals(codeString))
      return HistoryAbsentReason.UNABLETOOBTAIN;
    if ("deferred".equals(codeString))
      return HistoryAbsentReason.DEFERRED;
    throw new IllegalArgumentException("Unknown HistoryAbsentReason code '"+codeString+"'");
  }

  public String toCode(HistoryAbsentReason code) {
    if (code == HistoryAbsentReason.SUBJECTUNKNOWN)
      return "subject-unknown";
    if (code == HistoryAbsentReason.WITHHELD)
      return "withheld";
    if (code == HistoryAbsentReason.UNABLETOOBTAIN)
      return "unable-to-obtain";
    if (code == HistoryAbsentReason.DEFERRED)
      return "deferred";
    return "?";
  }

    public String toSystem(HistoryAbsentReason code) {
      return code.getSystem();
      }

}

