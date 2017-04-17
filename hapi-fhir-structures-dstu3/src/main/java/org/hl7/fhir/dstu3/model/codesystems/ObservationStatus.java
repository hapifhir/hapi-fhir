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


import org.hl7.fhir.exceptions.FHIRException;

public enum ObservationStatus {

        /**
         * The existence of the observation is registered, but there is no result yet available.
         */
        REGISTERED, 
        /**
         * This is an initial or interim observation: data may be incomplete or unverified.
         */
        PRELIMINARY, 
        /**
         * The observation is complete.
         */
        FINAL, 
        /**
         * Subsequent to being Final, the observation has been modified subsequent.  This includes updates/new information and corrections.
         */
        AMENDED, 
        /**
         * Subsequent to being Final, the observation has been modified to correct an error in the test result.
         */
        CORRECTED, 
        /**
         * The observation is unavailable because the measurement was not started or not completed (also sometimes called "aborted").
         */
        CANCELLED, 
        /**
         * The observation has been withdrawn following previous final release.  This electronic record should never have existed, though it is possible that real-world decisions were based on it. (If real-world activity has occurred, the status should be "cancelled" rather than "entered-in-error".)
         */
        ENTEREDINERROR, 
        /**
         * The authoring system does not know which of the status values currently applies for this request. Note: This concept is not to be used for "other" - one of the listed statuses is presumed to apply, but the authoring system does not know which.
         */
        UNKNOWN, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ObservationStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("registered".equals(codeString))
          return REGISTERED;
        if ("preliminary".equals(codeString))
          return PRELIMINARY;
        if ("final".equals(codeString))
          return FINAL;
        if ("amended".equals(codeString))
          return AMENDED;
        if ("corrected".equals(codeString))
          return CORRECTED;
        if ("cancelled".equals(codeString))
          return CANCELLED;
        if ("entered-in-error".equals(codeString))
          return ENTEREDINERROR;
        if ("unknown".equals(codeString))
          return UNKNOWN;
        throw new FHIRException("Unknown ObservationStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case REGISTERED: return "registered";
            case PRELIMINARY: return "preliminary";
            case FINAL: return "final";
            case AMENDED: return "amended";
            case CORRECTED: return "corrected";
            case CANCELLED: return "cancelled";
            case ENTEREDINERROR: return "entered-in-error";
            case UNKNOWN: return "unknown";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/observation-status";
        }
        public String getDefinition() {
          switch (this) {
            case REGISTERED: return "The existence of the observation is registered, but there is no result yet available.";
            case PRELIMINARY: return "This is an initial or interim observation: data may be incomplete or unverified.";
            case FINAL: return "The observation is complete.";
            case AMENDED: return "Subsequent to being Final, the observation has been modified subsequent.  This includes updates/new information and corrections.";
            case CORRECTED: return "Subsequent to being Final, the observation has been modified to correct an error in the test result.";
            case CANCELLED: return "The observation is unavailable because the measurement was not started or not completed (also sometimes called \"aborted\").";
            case ENTEREDINERROR: return "The observation has been withdrawn following previous final release.  This electronic record should never have existed, though it is possible that real-world decisions were based on it. (If real-world activity has occurred, the status should be \"cancelled\" rather than \"entered-in-error\".)";
            case UNKNOWN: return "The authoring system does not know which of the status values currently applies for this request. Note: This concept is not to be used for \"other\" - one of the listed statuses is presumed to apply, but the authoring system does not know which.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case REGISTERED: return "Registered";
            case PRELIMINARY: return "Preliminary";
            case FINAL: return "Final";
            case AMENDED: return "Amended";
            case CORRECTED: return "Corrected";
            case CANCELLED: return "Cancelled";
            case ENTEREDINERROR: return "Entered in Error";
            case UNKNOWN: return "Unknown";
            default: return "?";
          }
    }


}

