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

// Generated on Sun, May 6, 2018 17:51-0400 for FHIR v3.4.0


import org.hl7.fhir.exceptions.FHIRException;

public enum ProductStorageScale {

        /**
         * Fahrenheit temperature scale
         */
        FARENHEIT, 
        /**
         * Celsius or centigrade temperature scale
         */
        CELSIUS, 
        /**
         * Kelvin absolute thermodynamic temperature scale
         */
        KELVIN, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ProductStorageScale fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("farenheit".equals(codeString))
          return FARENHEIT;
        if ("celsius".equals(codeString))
          return CELSIUS;
        if ("kelvin".equals(codeString))
          return KELVIN;
        throw new FHIRException("Unknown ProductStorageScale code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case FARENHEIT: return "farenheit";
            case CELSIUS: return "celsius";
            case KELVIN: return "kelvin";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/product-storage-scale";
        }
        public String getDefinition() {
          switch (this) {
            case FARENHEIT: return "Fahrenheit temperature scale";
            case CELSIUS: return "Celsius or centigrade temperature scale";
            case KELVIN: return "Kelvin absolute thermodynamic temperature scale";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case FARENHEIT: return "Fahrenheit";
            case CELSIUS: return "Celsius";
            case KELVIN: return "Kelvin";
            default: return "?";
          }
    }


}

