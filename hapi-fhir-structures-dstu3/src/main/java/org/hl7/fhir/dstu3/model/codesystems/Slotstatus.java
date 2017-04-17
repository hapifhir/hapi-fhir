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

public enum Slotstatus {

        /**
         * Indicates that the time interval is busy because one  or more events have been scheduled for that interval.
         */
        BUSY, 
        /**
         * Indicates that the time interval is free for scheduling.
         */
        FREE, 
        /**
         * Indicates that the time interval is busy and that the interval can not be scheduled.
         */
        BUSYUNAVAILABLE, 
        /**
         * Indicates that the time interval is busy because one or more events have been tentatively scheduled for that interval.
         */
        BUSYTENTATIVE, 
        /**
         * This instance should not have been part of this patient's medical record.
         */
        ENTEREDINERROR, 
        /**
         * added to help the parsers
         */
        NULL;
        public static Slotstatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("busy".equals(codeString))
          return BUSY;
        if ("free".equals(codeString))
          return FREE;
        if ("busy-unavailable".equals(codeString))
          return BUSYUNAVAILABLE;
        if ("busy-tentative".equals(codeString))
          return BUSYTENTATIVE;
        if ("entered-in-error".equals(codeString))
          return ENTEREDINERROR;
        throw new FHIRException("Unknown Slotstatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case BUSY: return "busy";
            case FREE: return "free";
            case BUSYUNAVAILABLE: return "busy-unavailable";
            case BUSYTENTATIVE: return "busy-tentative";
            case ENTEREDINERROR: return "entered-in-error";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/slotstatus";
        }
        public String getDefinition() {
          switch (this) {
            case BUSY: return "Indicates that the time interval is busy because one  or more events have been scheduled for that interval.";
            case FREE: return "Indicates that the time interval is free for scheduling.";
            case BUSYUNAVAILABLE: return "Indicates that the time interval is busy and that the interval can not be scheduled.";
            case BUSYTENTATIVE: return "Indicates that the time interval is busy because one or more events have been tentatively scheduled for that interval.";
            case ENTEREDINERROR: return "This instance should not have been part of this patient's medical record.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case BUSY: return "Busy";
            case FREE: return "Free";
            case BUSYUNAVAILABLE: return "Busy (Unavailable)";
            case BUSYTENTATIVE: return "Busy (Tentative)";
            case ENTEREDINERROR: return "Entered in error";
            default: return "?";
          }
    }


}

