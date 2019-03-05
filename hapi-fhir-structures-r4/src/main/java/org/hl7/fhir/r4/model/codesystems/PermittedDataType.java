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

public enum PermittedDataType {

        /**
         * A measured amount.
         */
        QUANTITY, 
        /**
         * A coded concept from a reference terminology and/or text.
         */
        CODEABLECONCEPT, 
        /**
         * A sequence of Unicode characters.
         */
        STRING, 
        /**
         * true or false.
         */
        BOOLEAN, 
        /**
         * A signed integer.
         */
        INTEGER, 
        /**
         * A set of values bounded by low and high.
         */
        RANGE, 
        /**
         * A ratio of two Quantity values - a numerator and a denominator.
         */
        RATIO, 
        /**
         * A series of measurements taken by a device.
         */
        SAMPLEDDATA, 
        /**
         * A time during the day, in the format hh:mm:ss.
         */
        TIME, 
        /**
         * A date, date-time or partial date (e.g. just year or year + month) as used in human communication.
         */
        DATETIME, 
        /**
         * A time range defined by start and end date/time.
         */
        PERIOD, 
        /**
         * added to help the parsers
         */
        NULL;
        public static PermittedDataType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("Quantity".equals(codeString))
          return QUANTITY;
        if ("CodeableConcept".equals(codeString))
          return CODEABLECONCEPT;
        if ("string".equals(codeString))
          return STRING;
        if ("boolean".equals(codeString))
          return BOOLEAN;
        if ("integer".equals(codeString))
          return INTEGER;
        if ("Range".equals(codeString))
          return RANGE;
        if ("Ratio".equals(codeString))
          return RATIO;
        if ("SampledData".equals(codeString))
          return SAMPLEDDATA;
        if ("time".equals(codeString))
          return TIME;
        if ("dateTime".equals(codeString))
          return DATETIME;
        if ("Period".equals(codeString))
          return PERIOD;
        throw new FHIRException("Unknown PermittedDataType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case QUANTITY: return "Quantity";
            case CODEABLECONCEPT: return "CodeableConcept";
            case STRING: return "string";
            case BOOLEAN: return "boolean";
            case INTEGER: return "integer";
            case RANGE: return "Range";
            case RATIO: return "Ratio";
            case SAMPLEDDATA: return "SampledData";
            case TIME: return "time";
            case DATETIME: return "dateTime";
            case PERIOD: return "Period";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/permitted-data-type";
        }
        public String getDefinition() {
          switch (this) {
            case QUANTITY: return "A measured amount.";
            case CODEABLECONCEPT: return "A coded concept from a reference terminology and/or text.";
            case STRING: return "A sequence of Unicode characters.";
            case BOOLEAN: return "true or false.";
            case INTEGER: return "A signed integer.";
            case RANGE: return "A set of values bounded by low and high.";
            case RATIO: return "A ratio of two Quantity values - a numerator and a denominator.";
            case SAMPLEDDATA: return "A series of measurements taken by a device.";
            case TIME: return "A time during the day, in the format hh:mm:ss.";
            case DATETIME: return "A date, date-time or partial date (e.g. just year or year + month) as used in human communication.";
            case PERIOD: return "A time range defined by start and end date/time.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case QUANTITY: return "Quantity";
            case CODEABLECONCEPT: return "CodeableConcept";
            case STRING: return "string";
            case BOOLEAN: return "boolean";
            case INTEGER: return "integer";
            case RANGE: return "Range";
            case RATIO: return "Ratio";
            case SAMPLEDDATA: return "SampledData";
            case TIME: return "time";
            case DATETIME: return "dateTime";
            case PERIOD: return "Period";
            default: return "?";
          }
    }


}

