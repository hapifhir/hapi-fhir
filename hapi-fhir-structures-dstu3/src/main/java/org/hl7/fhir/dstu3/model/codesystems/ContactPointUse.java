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

public enum ContactPointUse {

        /**
         * A communication contact point at a home; attempted contacts for business purposes might intrude privacy and chances are one will contact family or other household members instead of the person one wishes to call. Typically used with urgent cases, or if no other contacts are available.
         */
        HOME, 
        /**
         * An office contact point. First choice for business related contacts during business hours.
         */
        WORK, 
        /**
         * A temporary contact point. The period can provide more detailed information.
         */
        TEMP, 
        /**
         * This contact point is no longer in use (or was never correct, but retained for records).
         */
        OLD, 
        /**
         * A telecommunication device that moves and stays with its owner. May have characteristics of all other use codes, suitable for urgent matters, not the first choice for routine business.
         */
        MOBILE, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ContactPointUse fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("home".equals(codeString))
          return HOME;
        if ("work".equals(codeString))
          return WORK;
        if ("temp".equals(codeString))
          return TEMP;
        if ("old".equals(codeString))
          return OLD;
        if ("mobile".equals(codeString))
          return MOBILE;
        throw new FHIRException("Unknown ContactPointUse code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case HOME: return "home";
            case WORK: return "work";
            case TEMP: return "temp";
            case OLD: return "old";
            case MOBILE: return "mobile";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/contact-point-use";
        }
        public String getDefinition() {
          switch (this) {
            case HOME: return "A communication contact point at a home; attempted contacts for business purposes might intrude privacy and chances are one will contact family or other household members instead of the person one wishes to call. Typically used with urgent cases, or if no other contacts are available.";
            case WORK: return "An office contact point. First choice for business related contacts during business hours.";
            case TEMP: return "A temporary contact point. The period can provide more detailed information.";
            case OLD: return "This contact point is no longer in use (or was never correct, but retained for records).";
            case MOBILE: return "A telecommunication device that moves and stays with its owner. May have characteristics of all other use codes, suitable for urgent matters, not the first choice for routine business.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case HOME: return "Home";
            case WORK: return "Work";
            case TEMP: return "Temp";
            case OLD: return "Old";
            case MOBILE: return "Mobile";
            default: return "?";
          }
    }


}

