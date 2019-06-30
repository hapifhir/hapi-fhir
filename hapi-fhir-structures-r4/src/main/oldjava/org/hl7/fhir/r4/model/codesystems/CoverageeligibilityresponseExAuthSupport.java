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


import org.hl7.fhir.exceptions.FHIRException;

public enum CoverageeligibilityresponseExAuthSupport {

        /**
         * A request or authorization for laboratory diagnostic tests.
         */
        LABORDER, 
        /**
         * A report on laboratory diagnostic test(s).
         */
        LABREPORT, 
        /**
         * A request or authorization for diagnostic imaging.
         */
        DIAGNOSTICIMAGEORDER, 
        /**
         * A report on diagnostic image(s).
         */
        DIAGNOSTICIMAGEREPORT, 
        /**
         * A report from a licensed professional regarding the siutation, condition or proposed treatment.
         */
        PROFESSIONALREPORT, 
        /**
         * A formal accident report as would be filed with police or a simlar official body.
         */
        ACCIDENTREPORT, 
        /**
         * A physical model of the affected area.
         */
        MODEL, 
        /**
         * A photograph of the affected area.
         */
        PICTURE, 
        /**
         * added to help the parsers
         */
        NULL;
        public static CoverageeligibilityresponseExAuthSupport fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("laborder".equals(codeString))
          return LABORDER;
        if ("labreport".equals(codeString))
          return LABREPORT;
        if ("diagnosticimageorder".equals(codeString))
          return DIAGNOSTICIMAGEORDER;
        if ("diagnosticimagereport".equals(codeString))
          return DIAGNOSTICIMAGEREPORT;
        if ("professionalreport".equals(codeString))
          return PROFESSIONALREPORT;
        if ("accidentreport".equals(codeString))
          return ACCIDENTREPORT;
        if ("model".equals(codeString))
          return MODEL;
        if ("picture".equals(codeString))
          return PICTURE;
        throw new FHIRException("Unknown CoverageeligibilityresponseExAuthSupport code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case LABORDER: return "laborder";
            case LABREPORT: return "labreport";
            case DIAGNOSTICIMAGEORDER: return "diagnosticimageorder";
            case DIAGNOSTICIMAGEREPORT: return "diagnosticimagereport";
            case PROFESSIONALREPORT: return "professionalreport";
            case ACCIDENTREPORT: return "accidentreport";
            case MODEL: return "model";
            case PICTURE: return "picture";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://terminology.hl7.org/CodeSystem/coverageeligibilityresponse-ex-auth-support";
        }
        public String getDefinition() {
          switch (this) {
            case LABORDER: return "A request or authorization for laboratory diagnostic tests.";
            case LABREPORT: return "A report on laboratory diagnostic test(s).";
            case DIAGNOSTICIMAGEORDER: return "A request or authorization for diagnostic imaging.";
            case DIAGNOSTICIMAGEREPORT: return "A report on diagnostic image(s).";
            case PROFESSIONALREPORT: return "A report from a licensed professional regarding the siutation, condition or proposed treatment.";
            case ACCIDENTREPORT: return "A formal accident report as would be filed with police or a simlar official body.";
            case MODEL: return "A physical model of the affected area.";
            case PICTURE: return "A photograph of the affected area.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case LABORDER: return "Lab Order";
            case LABREPORT: return "Lab Report";
            case DIAGNOSTICIMAGEORDER: return "Diagnostic Image Order";
            case DIAGNOSTICIMAGEREPORT: return "Diagnostic Image Report";
            case PROFESSIONALREPORT: return "Professional Report";
            case ACCIDENTREPORT: return "Accident Report";
            case MODEL: return "Model";
            case PICTURE: return "Picture";
            default: return "?";
          }
    }


}

