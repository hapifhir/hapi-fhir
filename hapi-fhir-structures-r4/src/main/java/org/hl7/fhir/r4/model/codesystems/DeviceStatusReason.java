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

public enum DeviceStatusReason {

        /**
         * The device is off.
         */
        ONLINE, 
        /**
         * The device is paused.
         */
        PAUSED, 
        /**
         * The device is ready but not actively operating.
         */
        STANDBY, 
        /**
         * The device is offline.
         */
        OFFLINE, 
        /**
         * The device is not ready.
         */
        NOTREADY, 
        /**
         * The device transducer is disconnected.
         */
        TRANSDUCDISCON, 
        /**
         * The device hardware is disconnected.
         */
        HWDISCON, 
        /**
         * The device is off.
         */
        OFF, 
        /**
         * added to help the parsers
         */
        NULL;
        public static DeviceStatusReason fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("online".equals(codeString))
          return ONLINE;
        if ("paused".equals(codeString))
          return PAUSED;
        if ("standby".equals(codeString))
          return STANDBY;
        if ("offline".equals(codeString))
          return OFFLINE;
        if ("not-ready".equals(codeString))
          return NOTREADY;
        if ("transduc-discon".equals(codeString))
          return TRANSDUCDISCON;
        if ("hw-discon".equals(codeString))
          return HWDISCON;
        if ("off".equals(codeString))
          return OFF;
        throw new FHIRException("Unknown DeviceStatusReason code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ONLINE: return "online";
            case PAUSED: return "paused";
            case STANDBY: return "standby";
            case OFFLINE: return "offline";
            case NOTREADY: return "not-ready";
            case TRANSDUCDISCON: return "transduc-discon";
            case HWDISCON: return "hw-discon";
            case OFF: return "off";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://terminology.hl7.org/CodeSystem/device-status-reason";
        }
        public String getDefinition() {
          switch (this) {
            case ONLINE: return "The device is off.";
            case PAUSED: return "The device is paused.";
            case STANDBY: return "The device is ready but not actively operating.";
            case OFFLINE: return "The device is offline.";
            case NOTREADY: return "The device is not ready.";
            case TRANSDUCDISCON: return "The device transducer is disconnected.";
            case HWDISCON: return "The device hardware is disconnected.";
            case OFF: return "The device is off.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ONLINE: return "Online";
            case PAUSED: return "Paused";
            case STANDBY: return "Standby";
            case OFFLINE: return "Offline";
            case NOTREADY: return "Not Ready";
            case TRANSDUCDISCON: return "Transducer Disconnected";
            case HWDISCON: return "Hardware Disconnected";
            case OFF: return "Off";
            default: return "?";
          }
    }


}

