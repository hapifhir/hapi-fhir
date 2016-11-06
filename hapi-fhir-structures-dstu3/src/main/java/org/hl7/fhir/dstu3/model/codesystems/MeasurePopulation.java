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

// Generated on Sat, Nov 5, 2016 08:41-0400 for FHIR v1.7.0


import org.hl7.fhir.exceptions.FHIRException;

public enum MeasurePopulation {

        /**
         * The initial population for the measure
         */
        INITIALPOPULATION, 
        /**
         * The numerator for the measure
         */
        NUMERATOR, 
        /**
         * The numerator exclusion for the measure
         */
        NUMERATOREXCLUSION, 
        /**
         * The denominator for the measure
         */
        DENOMINATOR, 
        /**
         * The denominator exclusion for the measure
         */
        DENOMINATOREXCLUSION, 
        /**
         * The denominator exception for the measure
         */
        DENOMINATOREXCEPTION, 
        /**
         * The measure population for the measure
         */
        MEASUREPOPULATION, 
        /**
         * The measure population exclusion for the measure
         */
        MEASUREPOPULATIONEXCLUSION, 
        /**
         * The measure observation for the measure
         */
        MEASUREOBSERVATION, 
        /**
         * added to help the parsers
         */
        NULL;
        public static MeasurePopulation fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("initial-population".equals(codeString))
          return INITIALPOPULATION;
        if ("numerator".equals(codeString))
          return NUMERATOR;
        if ("numerator-exclusion".equals(codeString))
          return NUMERATOREXCLUSION;
        if ("denominator".equals(codeString))
          return DENOMINATOR;
        if ("denominator-exclusion".equals(codeString))
          return DENOMINATOREXCLUSION;
        if ("denominator-exception".equals(codeString))
          return DENOMINATOREXCEPTION;
        if ("measure-population".equals(codeString))
          return MEASUREPOPULATION;
        if ("measure-population-exclusion".equals(codeString))
          return MEASUREPOPULATIONEXCLUSION;
        if ("measure-observation".equals(codeString))
          return MEASUREOBSERVATION;
        throw new FHIRException("Unknown MeasurePopulation code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case INITIALPOPULATION: return "initial-population";
            case NUMERATOR: return "numerator";
            case NUMERATOREXCLUSION: return "numerator-exclusion";
            case DENOMINATOR: return "denominator";
            case DENOMINATOREXCLUSION: return "denominator-exclusion";
            case DENOMINATOREXCEPTION: return "denominator-exception";
            case MEASUREPOPULATION: return "measure-population";
            case MEASUREPOPULATIONEXCLUSION: return "measure-population-exclusion";
            case MEASUREOBSERVATION: return "measure-observation";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/measure-population";
        }
        public String getDefinition() {
          switch (this) {
            case INITIALPOPULATION: return "The initial population for the measure";
            case NUMERATOR: return "The numerator for the measure";
            case NUMERATOREXCLUSION: return "The numerator exclusion for the measure";
            case DENOMINATOR: return "The denominator for the measure";
            case DENOMINATOREXCLUSION: return "The denominator exclusion for the measure";
            case DENOMINATOREXCEPTION: return "The denominator exception for the measure";
            case MEASUREPOPULATION: return "The measure population for the measure";
            case MEASUREPOPULATIONEXCLUSION: return "The measure population exclusion for the measure";
            case MEASUREOBSERVATION: return "The measure observation for the measure";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case INITIALPOPULATION: return "Initial Population";
            case NUMERATOR: return "Numerator";
            case NUMERATOREXCLUSION: return "Numerator Exclusion";
            case DENOMINATOR: return "Denominator";
            case DENOMINATOREXCLUSION: return "Denominator Exclusion";
            case DENOMINATOREXCEPTION: return "Denominator Exception";
            case MEASUREPOPULATION: return "Measure Population";
            case MEASUREPOPULATIONEXCLUSION: return "Measure Population Exclusion";
            case MEASUREOBSERVATION: return "Measure Observation";
            default: return "?";
          }
    }


}

