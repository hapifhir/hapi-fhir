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

public enum OperationalStatus {

        /**
         * The device is off.
         */
        OFF, 
        /**
         * The device is fully operational.
         */
        ON, 
        /**
         * The device is not ready.
         */
        NOTREADY, 
        /**
         * The device is ready but not actively operating.
         */
        STANDBY, 
        /**
         * The device transducer is diconnected.
         */
        TRANSDUCDISCON, 
        /**
         * The device hardware is disconnected.
         */
        HWDISCON, 
        /**
         * The device was entered in error.
         */
        ENTEREDINERROR, 
        /**
         * added to help the parsers
         */
        NULL;
        public static OperationalStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("off".equals(codeString))
          return OFF;
        if ("on".equals(codeString))
          return ON;
        if ("not-ready".equals(codeString))
          return NOTREADY;
        if ("standby".equals(codeString))
          return STANDBY;
        if ("transduc-discon".equals(codeString))
          return TRANSDUCDISCON;
        if ("hw-discon".equals(codeString))
          return HWDISCON;
        if ("entered-in-error".equals(codeString))
          return ENTEREDINERROR;
        throw new FHIRException("Unknown OperationalStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case OFF: return "off";
            case ON: return "on";
            case NOTREADY: return "not-ready";
            case STANDBY: return "standby";
            case TRANSDUCDISCON: return "transduc-discon";
            case HWDISCON: return "hw-discon";
            case ENTEREDINERROR: return "entered-in-error";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/operational-status";
        }
        public String getDefinition() {
          switch (this) {
            case OFF: return "The device is off.";
            case ON: return "The device is fully operational.";
            case NOTREADY: return "The device is not ready.";
            case STANDBY: return "The device is ready but not actively operating.";
            case TRANSDUCDISCON: return "The device transducer is diconnected.";
            case HWDISCON: return "The device hardware is disconnected.";
            case ENTEREDINERROR: return "The device was entered in error.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case OFF: return "Off";
            case ON: return "On";
            case NOTREADY: return "Not Ready";
            case STANDBY: return "Standby";
            case TRANSDUCDISCON: return "Transducer Diconnected";
            case HWDISCON: return "Hardware Disconnectd";
            case ENTEREDINERROR: return "Entered In Error";
            default: return "?";
          }
    }


}

