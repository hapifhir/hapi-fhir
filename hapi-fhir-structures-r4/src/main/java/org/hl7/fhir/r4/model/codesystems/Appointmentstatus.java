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

public enum Appointmentstatus {

        /**
         * None of the participant(s) have finalized their acceptance of the appointment request, and the start/end time might not be set yet.
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
         * The patient/patients has/have arrived and is/are waiting to be seen.
         */
        ARRIVED, 
        /**
         * The planning stages of the appointment are now complete, the encounter resource will exist and will track further status changes. Note that an encounter may exist before the appointment status is fulfilled for many reasons.
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
         * When checked in, all pre-encounter administrative work is complete, and the encounter may begin. (where multiple patients are involved, they are all present).
         */
        CHECKEDIN, 
        /**
         * The appointment has been placed on a waitlist, to be scheduled/confirmed in the future when a slot/service is available.
A specific time might or might not be pre-allocated.
         */
        WAITLIST, 
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
        if ("checked-in".equals(codeString))
          return CHECKEDIN;
        if ("waitlist".equals(codeString))
          return WAITLIST;
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
            case CHECKEDIN: return "checked-in";
            case WAITLIST: return "waitlist";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/appointmentstatus";
        }
        public String getDefinition() {
          switch (this) {
            case PROPOSED: return "None of the participant(s) have finalized their acceptance of the appointment request, and the start/end time might not be set yet.";
            case PENDING: return "Some or all of the participant(s) have not finalized their acceptance of the appointment request.";
            case BOOKED: return "All participant(s) have been considered and the appointment is confirmed to go ahead at the date/times specified.";
            case ARRIVED: return "The patient/patients has/have arrived and is/are waiting to be seen.";
            case FULFILLED: return "The planning stages of the appointment are now complete, the encounter resource will exist and will track further status changes. Note that an encounter may exist before the appointment status is fulfilled for many reasons.";
            case CANCELLED: return "The appointment has been cancelled.";
            case NOSHOW: return "Some or all of the participant(s) have not/did not appear for the appointment (usually the patient).";
            case ENTEREDINERROR: return "This instance should not have been part of this patient's medical record.";
            case CHECKEDIN: return "When checked in, all pre-encounter administrative work is complete, and the encounter may begin. (where multiple patients are involved, they are all present).";
            case WAITLIST: return "The appointment has been placed on a waitlist, to be scheduled/confirmed in the future when a slot/service is available.\nA specific time might or might not be pre-allocated.";
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
            case CHECKEDIN: return "Checked In";
            case WAITLIST: return "Waitlisted";
            default: return "?";
          }
    }


}

