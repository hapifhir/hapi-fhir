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

public enum Appointmentstatus {

        /**
         * None of the participant(s) have finalized their acceptance of the appointment request, and the start/end time may not be set yet.
         */
        PROPOSED, 
        /**
         * Some or all of the participant(s) have not finalized their acceptance of the appointment request.
         */
        PENDING, 
        /**
         * All participant(s) have been considered and the appointment is confirmed to go ahead at the date/times specified.
         */
        BOOKED, 
        /**
         * Some of the patients have arrived.
         */
        ARRIVED, 
        /**
         * This appointment has completed and may have resulted in an encounter.
         */
        FULFILLED, 
        /**
         * The appointment has been cancelled.
         */
        CANCELLED, 
        /**
         * Some or all of the participant(s) have not/did not appear for the appointment (usually the patient).
         */
        NOSHOW, 
        /**
         * This instance should not have been part of this patient's medical record.
         */
        ENTEREDINERROR, 
        /**
         * added to help the parsers
         */
        NULL;
        public static Appointmentstatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("proposed".equals(codeString))
          return PROPOSED;
        if ("pending".equals(codeString))
          return PENDING;
        if ("booked".equals(codeString))
          return BOOKED;
        if ("arrived".equals(codeString))
          return ARRIVED;
        if ("fulfilled".equals(codeString))
          return FULFILLED;
        if ("cancelled".equals(codeString))
          return CANCELLED;
        if ("noshow".equals(codeString))
          return NOSHOW;
        if ("entered-in-error".equals(codeString))
          return ENTEREDINERROR;
        throw new FHIRException("Unknown Appointmentstatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PROPOSED: return "proposed";
            case PENDING: return "pending";
            case BOOKED: return "booked";
            case ARRIVED: return "arrived";
            case FULFILLED: return "fulfilled";
            case CANCELLED: return "cancelled";
            case NOSHOW: return "noshow";
            case ENTEREDINERROR: return "entered-in-error";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/appointmentstatus";
        }
        public String getDefinition() {
          switch (this) {
            case PROPOSED: return "None of the participant(s) have finalized their acceptance of the appointment request, and the start/end time may not be set yet.";
            case PENDING: return "Some or all of the participant(s) have not finalized their acceptance of the appointment request.";
            case BOOKED: return "All participant(s) have been considered and the appointment is confirmed to go ahead at the date/times specified.";
            case ARRIVED: return "Some of the patients have arrived.";
            case FULFILLED: return "This appointment has completed and may have resulted in an encounter.";
            case CANCELLED: return "The appointment has been cancelled.";
            case NOSHOW: return "Some or all of the participant(s) have not/did not appear for the appointment (usually the patient).";
            case ENTEREDINERROR: return "This instance should not have been part of this patient's medical record.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PROPOSED: return "Proposed";
            case PENDING: return "Pending";
            case BOOKED: return "Booked";
            case ARRIVED: return "Arrived";
            case FULFILLED: return "Fulfilled";
            case CANCELLED: return "Cancelled";
            case NOSHOW: return "No Show";
            case ENTEREDINERROR: return "Entered in error";
            default: return "?";
          }
    }


}

