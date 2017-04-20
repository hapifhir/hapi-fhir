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

public enum MeasurePopulation {

        /**
         * The initial population refers to all patients or events to be evaluated by a quality measure involving patients who share a common set of specified characterstics. All patients or events counted (for example, as numerator, as denominator) are drawn from the initial population
         */
        INITIALPOPULATION, 
        /**
         * The upper portion of a fraction used to calculate a rate, proportion, or ratio. Also called the measure focus, it is the target process, condition, event, or outcome. Numerator criteria are the processes or outcomes expected for each patient, or event defined in the denominator. A numerator statement describes the clinical action that satisfies the conditions of the measure
         */
        NUMERATOR, 
        /**
         * Numerator exclusion criteria define patients or events to be removed from the numerator. Numerator exclusions are used in proportion and ratio measures to help narrow the numerator (for inverted measures)
         */
        NUMERATOREXCLUSION, 
        /**
         * The lower portion of a fraction used to calculate a rate, proportion, or ratio. The denominator can be the same as the initial population, or a subset of the initial population to further constrain the population for the purpose of the measure
         */
        DENOMINATOR, 
        /**
         * Denominator exclusion criteria define patients or events that should be removed from the denominator before determining if numerator criteria are met. Denominator exclusions are used in proportion and ratio measures to help narrow the denominator. For example, patients with bilateral lower extremity amputations would be listed as a denominator exclusion for a measure requiring foot exams
         */
        DENOMINATOREXCLUSION, 
        /**
         * Denominator exceptions are conditions that should remove a patient or event from the denominator of a measure only if the numerator criteria are not met. Denominator exception allows for adjustment of the calculated score for those providers with higher risk populations. Denominator exception criteria are only used in proportion measures
         */
        DENOMINATOREXCEPTION, 
        /**
         * Measure population criteria define the patients or events for which the individual observation for the measure should be taken. Measure populations are used for continuous variable measures rather than numerator and denominator criteria
         */
        MEASUREPOPULATION, 
        /**
         * Measure population criteria define the patients or events that should be removed from the measure population before determining the outcome of one or more continuous variables defined for the measure observation. Measure population exclusion criteria are used within continuous variable measures to help narrow the measure population
         */
        MEASUREPOPULATIONEXCLUSION, 
        /**
         * Defines the individual observation to be performed for each patient or event in the measure population. Measure observations for each case in the population are aggregated to determine the overall measure score for the population
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
            case INITIALPOPULATION: return "The initial population refers to all patients or events to be evaluated by a quality measure involving patients who share a common set of specified characterstics. All patients or events counted (for example, as numerator, as denominator) are drawn from the initial population";
            case NUMERATOR: return "The upper portion of a fraction used to calculate a rate, proportion, or ratio. Also called the measure focus, it is the target process, condition, event, or outcome. Numerator criteria are the processes or outcomes expected for each patient, or event defined in the denominator. A numerator statement describes the clinical action that satisfies the conditions of the measure";
            case NUMERATOREXCLUSION: return "Numerator exclusion criteria define patients or events to be removed from the numerator. Numerator exclusions are used in proportion and ratio measures to help narrow the numerator (for inverted measures)";
            case DENOMINATOR: return "The lower portion of a fraction used to calculate a rate, proportion, or ratio. The denominator can be the same as the initial population, or a subset of the initial population to further constrain the population for the purpose of the measure";
            case DENOMINATOREXCLUSION: return "Denominator exclusion criteria define patients or events that should be removed from the denominator before determining if numerator criteria are met. Denominator exclusions are used in proportion and ratio measures to help narrow the denominator. For example, patients with bilateral lower extremity amputations would be listed as a denominator exclusion for a measure requiring foot exams";
            case DENOMINATOREXCEPTION: return "Denominator exceptions are conditions that should remove a patient or event from the denominator of a measure only if the numerator criteria are not met. Denominator exception allows for adjustment of the calculated score for those providers with higher risk populations. Denominator exception criteria are only used in proportion measures";
            case MEASUREPOPULATION: return "Measure population criteria define the patients or events for which the individual observation for the measure should be taken. Measure populations are used for continuous variable measures rather than numerator and denominator criteria";
            case MEASUREPOPULATIONEXCLUSION: return "Measure population criteria define the patients or events that should be removed from the measure population before determining the outcome of one or more continuous variables defined for the measure observation. Measure population exclusion criteria are used within continuous variable measures to help narrow the measure population";
            case MEASUREOBSERVATION: return "Defines the individual observation to be performed for each patient or event in the measure population. Measure observations for each case in the population are aggregated to determine the overall measure score for the population";
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

