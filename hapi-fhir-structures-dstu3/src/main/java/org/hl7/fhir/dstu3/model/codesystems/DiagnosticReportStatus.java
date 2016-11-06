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

// Generated on Sat, Nov 5, 2016 08:41-0400 for FHIR v1.7.0


import org.hl7.fhir.exceptions.FHIRException;

public enum DiagnosticReportStatus {

        /**
         * The existence of the report is registered, but there is nothing yet available.
         */
        REGISTERED, 
        /**
         * This is a partial (e.g. initial, interim or preliminary) report: data in the report may be incomplete or unverified.
         */
        PARTIAL, 
        /**
         * The report is complete and verified by an authorized person.
         */
        FINAL, 
        /**
         * The report has been modified subsequent to being Final, and is complete and verified by an authorized person
         */
        CORRECTED, 
        /**
         * The report has been modified subsequent to being Final, and is complete and verified by an authorized person. New content has been added, but existing content hasn't changed.
         */
        APPENDED, 
        /**
         * The report is unavailable because the measurement was not started or not completed (also sometimes called "aborted").
         */
        CANCELLED, 
        /**
         * The report has been withdrawn following a previous final release.
         */
        ENTEREDINERROR, 
        /**
         * added to help the parsers
         */
        NULL;
        public static DiagnosticReportStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("registered".equals(codeString))
          return REGISTERED;
        if ("partial".equals(codeString))
          return PARTIAL;
        if ("final".equals(codeString))
          return FINAL;
        if ("corrected".equals(codeString))
          return CORRECTED;
        if ("appended".equals(codeString))
          return APPENDED;
        if ("cancelled".equals(codeString))
          return CANCELLED;
        if ("entered-in-error".equals(codeString))
          return ENTEREDINERROR;
        throw new FHIRException("Unknown DiagnosticReportStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case REGISTERED: return "registered";
            case PARTIAL: return "partial";
            case FINAL: return "final";
            case CORRECTED: return "corrected";
            case APPENDED: return "appended";
            case CANCELLED: return "cancelled";
            case ENTEREDINERROR: return "entered-in-error";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/diagnostic-report-status";
        }
        public String getDefinition() {
          switch (this) {
            case REGISTERED: return "The existence of the report is registered, but there is nothing yet available.";
            case PARTIAL: return "This is a partial (e.g. initial, interim or preliminary) report: data in the report may be incomplete or unverified.";
            case FINAL: return "The report is complete and verified by an authorized person.";
            case CORRECTED: return "The report has been modified subsequent to being Final, and is complete and verified by an authorized person";
            case APPENDED: return "The report has been modified subsequent to being Final, and is complete and verified by an authorized person. New content has been added, but existing content hasn't changed.";
            case CANCELLED: return "The report is unavailable because the measurement was not started or not completed (also sometimes called \"aborted\").";
            case ENTEREDINERROR: return "The report has been withdrawn following a previous final release.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case REGISTERED: return "Registered";
            case PARTIAL: return "Partial";
            case FINAL: return "Final";
            case CORRECTED: return "Corrected";
            case APPENDED: return "Appended";
            case CANCELLED: return "Cancelled";
            case ENTEREDINERROR: return "Entered in Error";
            default: return "?";
          }
    }


}

