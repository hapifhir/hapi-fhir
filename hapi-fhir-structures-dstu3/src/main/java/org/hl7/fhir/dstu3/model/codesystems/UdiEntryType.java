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

public enum UdiEntryType {

        /**
         * A Barcode scanner captured the data from the device label
         */
        BARCODE, 
        /**
         * An RFID chip reader captured the data from the device label
         */
        RFID, 
        /**
         * The data was read from the label by a person and manually entered. (e.g.  via a keyboard)
         */
        MANUAL, 
        /**
         * The data originated from a patient's implant card and read by an operator.
         */
        CARD, 
        /**
         * The data originated from a patient source and not directly scanned or read from a label or card.
         */
        SELFREPORTED, 
        /**
         * The method of data capture has not been determined
         */
        UNKNOWN, 
        /**
         * added to help the parsers
         */
        NULL;
        public static UdiEntryType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("barcode".equals(codeString))
          return BARCODE;
        if ("rfid".equals(codeString))
          return RFID;
        if ("manual".equals(codeString))
          return MANUAL;
        if ("card".equals(codeString))
          return CARD;
        if ("self-reported".equals(codeString))
          return SELFREPORTED;
        if ("unknown".equals(codeString))
          return UNKNOWN;
        throw new FHIRException("Unknown UdiEntryType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case BARCODE: return "barcode";
            case RFID: return "rfid";
            case MANUAL: return "manual";
            case CARD: return "card";
            case SELFREPORTED: return "self-reported";
            case UNKNOWN: return "unknown";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/udi-entry-type";
        }
        public String getDefinition() {
          switch (this) {
            case BARCODE: return "A Barcode scanner captured the data from the device label";
            case RFID: return "An RFID chip reader captured the data from the device label";
            case MANUAL: return "The data was read from the label by a person and manually entered. (e.g.  via a keyboard)";
            case CARD: return "The data originated from a patient's implant card and read by an operator.";
            case SELFREPORTED: return "The data originated from a patient source and not directly scanned or read from a label or card.";
            case UNKNOWN: return "The method of data capture has not been determined";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case BARCODE: return "BarCode";
            case RFID: return "RFID";
            case MANUAL: return "Manual";
            case CARD: return "Card";
            case SELFREPORTED: return "Self Reported";
            case UNKNOWN: return "Unknown";
            default: return "?";
          }
    }


}

