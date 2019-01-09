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

public enum MedicationdispenseStatus {

        /**
         * The core event has not started yet, but some staging activities have begun (e.g. initial compounding or packaging of medication). Preparation stages may be tracked for billing purposes.
         */
        PREPARATION, 
        /**
         * The dispensed product is ready for pickup.
         */
        INPROGRESS, 
        /**
         * The dispensed product was not and will never be picked up by the patient.
         */
        CANCELLED, 
        /**
         * The dispense process is paused while waiting for an external event to reactivate the dispense.  For example, new stock has arrived or the prescriber has called.
         */
        ONHOLD, 
        /**
         * The dispensed product has been picked up.
         */
        COMPLETED, 
        /**
         * The dispense was entered in error and therefore nullified.
         */
        ENTEREDINERROR, 
        /**
         * Actions implied by the dispense have been permanently halted, before all of them occurred.
         */
        STOPPED, 
        /**
         * The dispense was declined and not performed.
         */
        DECLINED, 
        /**
         * The authoring system does not know which of the status values applies for this medication dispense.  Note: this concept is not to be used for other - one of the listed statuses is presumed to apply, it's just now known which one.
         */
        UNKNOWN, 
        /**
         * added to help the parsers
         */
        NULL;
        public static MedicationdispenseStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("preparation".equals(codeString))
          return PREPARATION;
        if ("in-progress".equals(codeString))
          return INPROGRESS;
        if ("cancelled".equals(codeString))
          return CANCELLED;
        if ("on-hold".equals(codeString))
          return ONHOLD;
        if ("completed".equals(codeString))
          return COMPLETED;
        if ("entered-in-error".equals(codeString))
          return ENTEREDINERROR;
        if ("stopped".equals(codeString))
          return STOPPED;
        if ("declined".equals(codeString))
          return DECLINED;
        if ("unknown".equals(codeString))
          return UNKNOWN;
        throw new FHIRException("Unknown MedicationdispenseStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PREPARATION: return "preparation";
            case INPROGRESS: return "in-progress";
            case CANCELLED: return "cancelled";
            case ONHOLD: return "on-hold";
            case COMPLETED: return "completed";
            case ENTEREDINERROR: return "entered-in-error";
            case STOPPED: return "stopped";
            case DECLINED: return "declined";
            case UNKNOWN: return "unknown";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://terminology.hl7.org/CodeSystem/medicationdispense-status";
        }
        public String getDefinition() {
          switch (this) {
            case PREPARATION: return "The core event has not started yet, but some staging activities have begun (e.g. initial compounding or packaging of medication). Preparation stages may be tracked for billing purposes.";
            case INPROGRESS: return "The dispensed product is ready for pickup.";
            case CANCELLED: return "The dispensed product was not and will never be picked up by the patient.";
            case ONHOLD: return "The dispense process is paused while waiting for an external event to reactivate the dispense.  For example, new stock has arrived or the prescriber has called.";
            case COMPLETED: return "The dispensed product has been picked up.";
            case ENTEREDINERROR: return "The dispense was entered in error and therefore nullified.";
            case STOPPED: return "Actions implied by the dispense have been permanently halted, before all of them occurred.";
            case DECLINED: return "The dispense was declined and not performed.";
            case UNKNOWN: return "The authoring system does not know which of the status values applies for this medication dispense.  Note: this concept is not to be used for other - one of the listed statuses is presumed to apply, it's just now known which one.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PREPARATION: return "Preparation";
            case INPROGRESS: return "In Progress";
            case CANCELLED: return "Cancelled";
            case ONHOLD: return "On Hold";
            case COMPLETED: return "Completed";
            case ENTEREDINERROR: return "Entered in Error";
            case STOPPED: return "Stopped";
            case DECLINED: return "Declined";
            case UNKNOWN: return "Unknown";
            default: return "?";
          }
    }


}

