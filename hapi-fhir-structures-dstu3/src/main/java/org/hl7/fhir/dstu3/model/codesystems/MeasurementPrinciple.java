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

public enum MeasurementPrinciple {

        /**
         * Measurement principle isn't in the list.
         */
        OTHER, 
        /**
         * Measurement is done using the chemical principle.
         */
        CHEMICAL, 
        /**
         * Measurement is done using the electrical principle.
         */
        ELECTRICAL, 
        /**
         * Measurement is done using the impedance principle.
         */
        IMPEDANCE, 
        /**
         * Measurement is done using the nuclear principle.
         */
        NUCLEAR, 
        /**
         * Measurement is done using the optical principle.
         */
        OPTICAL, 
        /**
         * Measurement is done using the thermal principle.
         */
        THERMAL, 
        /**
         * Measurement is done using the biological principle.
         */
        BIOLOGICAL, 
        /**
         * Measurement is done using the mechanical principle.
         */
        MECHANICAL, 
        /**
         * Measurement is done using the acoustical principle.
         */
        ACOUSTICAL, 
        /**
         * Measurement is done using the manual principle.
         */
        MANUAL, 
        /**
         * added to help the parsers
         */
        NULL;
        public static MeasurementPrinciple fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("other".equals(codeString))
          return OTHER;
        if ("chemical".equals(codeString))
          return CHEMICAL;
        if ("electrical".equals(codeString))
          return ELECTRICAL;
        if ("impedance".equals(codeString))
          return IMPEDANCE;
        if ("nuclear".equals(codeString))
          return NUCLEAR;
        if ("optical".equals(codeString))
          return OPTICAL;
        if ("thermal".equals(codeString))
          return THERMAL;
        if ("biological".equals(codeString))
          return BIOLOGICAL;
        if ("mechanical".equals(codeString))
          return MECHANICAL;
        if ("acoustical".equals(codeString))
          return ACOUSTICAL;
        if ("manual".equals(codeString))
          return MANUAL;
        throw new FHIRException("Unknown MeasurementPrinciple code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case OTHER: return "other";
            case CHEMICAL: return "chemical";
            case ELECTRICAL: return "electrical";
            case IMPEDANCE: return "impedance";
            case NUCLEAR: return "nuclear";
            case OPTICAL: return "optical";
            case THERMAL: return "thermal";
            case BIOLOGICAL: return "biological";
            case MECHANICAL: return "mechanical";
            case ACOUSTICAL: return "acoustical";
            case MANUAL: return "manual";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/measurement-principle";
        }
        public String getDefinition() {
          switch (this) {
            case OTHER: return "Measurement principle isn't in the list.";
            case CHEMICAL: return "Measurement is done using the chemical principle.";
            case ELECTRICAL: return "Measurement is done using the electrical principle.";
            case IMPEDANCE: return "Measurement is done using the impedance principle.";
            case NUCLEAR: return "Measurement is done using the nuclear principle.";
            case OPTICAL: return "Measurement is done using the optical principle.";
            case THERMAL: return "Measurement is done using the thermal principle.";
            case BIOLOGICAL: return "Measurement is done using the biological principle.";
            case MECHANICAL: return "Measurement is done using the mechanical principle.";
            case ACOUSTICAL: return "Measurement is done using the acoustical principle.";
            case MANUAL: return "Measurement is done using the manual principle.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case OTHER: return "MSP Other";
            case CHEMICAL: return "MSP Chemical";
            case ELECTRICAL: return "MSP Electrical";
            case IMPEDANCE: return "MSP Impedance";
            case NUCLEAR: return "MSP Nuclear";
            case OPTICAL: return "MSP Optical";
            case THERMAL: return "MSP Thermal";
            case BIOLOGICAL: return "MSP Biological";
            case MECHANICAL: return "MSP Mechanical";
            case ACOUSTICAL: return "MSP Acoustical";
            case MANUAL: return "MSP Manual";
            default: return "?";
          }
    }


}

