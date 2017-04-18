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

public class DiagnosticReportStatusEnumFactory implements EnumFactory<DiagnosticReportStatus> {

  public DiagnosticReportStatus fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("registered".equals(codeString))
      return DiagnosticReportStatus.REGISTERED;
    if ("partial".equals(codeString))
      return DiagnosticReportStatus.PARTIAL;
    if ("preliminary".equals(codeString))
      return DiagnosticReportStatus.PRELIMINARY;
    if ("final".equals(codeString))
      return DiagnosticReportStatus.FINAL;
    if ("amended".equals(codeString))
      return DiagnosticReportStatus.AMENDED;
    if ("corrected".equals(codeString))
      return DiagnosticReportStatus.CORRECTED;
    if ("appended".equals(codeString))
      return DiagnosticReportStatus.APPENDED;
    if ("cancelled".equals(codeString))
      return DiagnosticReportStatus.CANCELLED;
    if ("entered-in-error".equals(codeString))
      return DiagnosticReportStatus.ENTEREDINERROR;
    if ("unknown".equals(codeString))
      return DiagnosticReportStatus.UNKNOWN;
    throw new IllegalArgumentException("Unknown DiagnosticReportStatus code '"+codeString+"'");
  }

  public String toCode(DiagnosticReportStatus code) {
    if (code == DiagnosticReportStatus.REGISTERED)
      return "registered";
    if (code == DiagnosticReportStatus.PARTIAL)
      return "partial";
    if (code == DiagnosticReportStatus.PRELIMINARY)
      return "preliminary";
    if (code == DiagnosticReportStatus.FINAL)
      return "final";
    if (code == DiagnosticReportStatus.AMENDED)
      return "amended";
    if (code == DiagnosticReportStatus.CORRECTED)
      return "corrected";
    if (code == DiagnosticReportStatus.APPENDED)
      return "appended";
    if (code == DiagnosticReportStatus.CANCELLED)
      return "cancelled";
    if (code == DiagnosticReportStatus.ENTEREDINERROR)
      return "entered-in-error";
    if (code == DiagnosticReportStatus.UNKNOWN)
      return "unknown";
    return "?";
  }

    public String toSystem(DiagnosticReportStatus code) {
      return code.getSystem();
      }

}

