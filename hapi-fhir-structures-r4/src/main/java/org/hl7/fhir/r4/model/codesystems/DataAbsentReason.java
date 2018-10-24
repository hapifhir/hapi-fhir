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

// Generated on Thu, Sep 13, 2018 09:04-0400 for FHIR v3.5.0


import org.hl7.fhir.exceptions.FHIRException;

public enum DataAbsentReason {

        /**
         * The value is expected to exist but is not known.
         */
        UNKNOWN, 
        /**
         * The source was asked but does not know the value.
         */
        ASKEDUNKNOWN, 
        /**
         * There is reason to expect (from the workflow) that the value may become known.
         */
        TEMPUNKNOWN, 
        /**
         * The workflow didn't lead to this value being known.
         */
        NOTASKED, 
        /**
         * The source was asked but declined to answer.
         */
        ASKEDDECLINED, 
        /**
         * The information is not available due to security, privacy or related reasons.
         */
        MASKED, 
        /**
         * There is no proper value for this element (e.g. last menstrual period for a male).
         */
        NOTAPPLICABLE, 
        /**
         * The source system wasn't capable of supporting this element.
         */
        UNSUPPORTED, 
        /**
         * The content of the data is represented in the resource narrative.
         */
        ASTEXT, 
        /**
         * Some system or workflow process error means that the information is not available.
         */
        ERROR, 
        /**
         * The numeric value is undefined or unrepresentable due to a floating point processing error.
         */
        NOTANUMBER, 
        /**
         * The numeric value is excessively low and unrepresentable due to a floating point processing error.
         */
        NEGATIVEINFINITY, 
        /**
         * The numeric value is excessively high and unrepresentable due to a floating point processing error.
         */
        POSITIVEINFINITY, 
        /**
         * The value is not available because the observation procedure (test, etc.) was not performed.
         */
        NOTPERFORMED, 
        /**
         * The value is not permitted in this context (e.g. due to profiles, or the base data types).
         */
        NOTPERMITTED, 
        /**
         * added to help the parsers
         */
        NULL;
        public static DataAbsentReason fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("unknown".equals(codeString))
          return UNKNOWN;
        if ("asked-unknown".equals(codeString))
          return ASKEDUNKNOWN;
        if ("temp-unknown".equals(codeString))
          return TEMPUNKNOWN;
        if ("not-asked".equals(codeString))
          return NOTASKED;
        if ("asked-declined".equals(codeString))
          return ASKEDDECLINED;
        if ("masked".equals(codeString))
          return MASKED;
        if ("not-applicable".equals(codeString))
          return NOTAPPLICABLE;
        if ("unsupported".equals(codeString))
          return UNSUPPORTED;
        if ("as-text".equals(codeString))
          return ASTEXT;
        if ("error".equals(codeString))
          return ERROR;
        if ("not-a-number".equals(codeString))
          return NOTANUMBER;
        if ("negative-infinity".equals(codeString))
          return NEGATIVEINFINITY;
        if ("positive-infinity".equals(codeString))
          return POSITIVEINFINITY;
        if ("not-performed".equals(codeString))
          return NOTPERFORMED;
        if ("not-permitted".equals(codeString))
          return NOTPERMITTED;
        throw new FHIRException("Unknown DataAbsentReason code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case UNKNOWN: return "unknown";
            case ASKEDUNKNOWN: return "asked-unknown";
            case TEMPUNKNOWN: return "temp-unknown";
            case NOTASKED: return "not-asked";
            case ASKEDDECLINED: return "asked-declined";
            case MASKED: return "masked";
            case NOTAPPLICABLE: return "not-applicable";
            case UNSUPPORTED: return "unsupported";
            case ASTEXT: return "as-text";
            case ERROR: return "error";
            case NOTANUMBER: return "not-a-number";
            case NEGATIVEINFINITY: return "negative-infinity";
            case POSITIVEINFINITY: return "positive-infinity";
            case NOTPERFORMED: return "not-performed";
            case NOTPERMITTED: return "not-permitted";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://terminology.hl7.org/CodeSystem/data-absent-reason";
        }
        public String getDefinition() {
          switch (this) {
            case UNKNOWN: return "The value is expected to exist but is not known.";
            case ASKEDUNKNOWN: return "The source was asked but does not know the value.";
            case TEMPUNKNOWN: return "There is reason to expect (from the workflow) that the value may become known.";
            case NOTASKED: return "The workflow didn't lead to this value being known.";
            case ASKEDDECLINED: return "The source was asked but declined to answer.";
            case MASKED: return "The information is not available due to security, privacy or related reasons.";
            case NOTAPPLICABLE: return "There is no proper value for this element (e.g. last menstrual period for a male).";
            case UNSUPPORTED: return "The source system wasn't capable of supporting this element.";
            case ASTEXT: return "The content of the data is represented in the resource narrative.";
            case ERROR: return "Some system or workflow process error means that the information is not available.";
            case NOTANUMBER: return "The numeric value is undefined or unrepresentable due to a floating point processing error.";
            case NEGATIVEINFINITY: return "The numeric value is excessively low and unrepresentable due to a floating point processing error.";
            case POSITIVEINFINITY: return "The numeric value is excessively high and unrepresentable due to a floating point processing error.";
            case NOTPERFORMED: return "The value is not available because the observation procedure (test, etc.) was not performed.";
            case NOTPERMITTED: return "The value is not permitted in this context (e.g. due to profiles, or the base data types).";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case UNKNOWN: return "Unknown";
            case ASKEDUNKNOWN: return "Asked But Unknown";
            case TEMPUNKNOWN: return "Temporarily Unknown";
            case NOTASKED: return "Not Asked";
            case ASKEDDECLINED: return "Asked But Declined";
            case MASKED: return "Masked";
            case NOTAPPLICABLE: return "Not Applicable";
            case UNSUPPORTED: return "Unsupported";
            case ASTEXT: return "As Text";
            case ERROR: return "Error";
            case NOTANUMBER: return "Not a Number (NaN)";
            case NEGATIVEINFINITY: return "Negative Infinity (NINF)";
            case POSITIVEINFINITY: return "Positive Infinity (PINF)";
            case NOTPERFORMED: return "Not Performed";
            case NOTPERMITTED: return "Not Permitted";
            default: return "?";
          }
    }


}

