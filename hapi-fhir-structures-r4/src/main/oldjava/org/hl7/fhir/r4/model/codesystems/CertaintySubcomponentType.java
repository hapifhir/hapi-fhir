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

public enum CertaintySubcomponentType {

        /**
         * methodologic concerns reducing internal validity.
         */
        RISKOFBIAS, 
        /**
         * concerns that findings are not similar enough to support certainty.
         */
        INCONSISTENCY, 
        /**
         * concerns reducing external validity.
         */
        INDIRECTNESS, 
        /**
         * High quality evidence.
         */
        IMPRECISION, 
        /**
         * likelihood that what is published misrepresents what is available to publish.
         */
        PUBLICATIONBIAS, 
        /**
         * higher certainty due to dose response relationship.
         */
        DOSERESPONSEGRADIENT, 
        /**
         * higher certainty due to risk of bias in opposite direction.
         */
        PLAUSIBLECONFOUNDING, 
        /**
         * higher certainty due to large effect size.
         */
        LARGEEFFECT, 
        /**
         * added to help the parsers
         */
        NULL;
        public static CertaintySubcomponentType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("RiskOfBias".equals(codeString))
          return RISKOFBIAS;
        if ("Inconsistency".equals(codeString))
          return INCONSISTENCY;
        if ("Indirectness".equals(codeString))
          return INDIRECTNESS;
        if ("Imprecision".equals(codeString))
          return IMPRECISION;
        if ("PublicationBias".equals(codeString))
          return PUBLICATIONBIAS;
        if ("DoseResponseGradient".equals(codeString))
          return DOSERESPONSEGRADIENT;
        if ("PlausibleConfounding".equals(codeString))
          return PLAUSIBLECONFOUNDING;
        if ("LargeEffect".equals(codeString))
          return LARGEEFFECT;
        throw new FHIRException("Unknown CertaintySubcomponentType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case RISKOFBIAS: return "RiskOfBias";
            case INCONSISTENCY: return "Inconsistency";
            case INDIRECTNESS: return "Indirectness";
            case IMPRECISION: return "Imprecision";
            case PUBLICATIONBIAS: return "PublicationBias";
            case DOSERESPONSEGRADIENT: return "DoseResponseGradient";
            case PLAUSIBLECONFOUNDING: return "PlausibleConfounding";
            case LARGEEFFECT: return "LargeEffect";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://terminology.hl7.org/CodeSystem/certainty-subcomponent-type";
        }
        public String getDefinition() {
          switch (this) {
            case RISKOFBIAS: return "methodologic concerns reducing internal validity.";
            case INCONSISTENCY: return "concerns that findings are not similar enough to support certainty.";
            case INDIRECTNESS: return "concerns reducing external validity.";
            case IMPRECISION: return "High quality evidence.";
            case PUBLICATIONBIAS: return "likelihood that what is published misrepresents what is available to publish.";
            case DOSERESPONSEGRADIENT: return "higher certainty due to dose response relationship.";
            case PLAUSIBLECONFOUNDING: return "higher certainty due to risk of bias in opposite direction.";
            case LARGEEFFECT: return "higher certainty due to large effect size.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case RISKOFBIAS: return "Risk of bias";
            case INCONSISTENCY: return "Inconsistency";
            case INDIRECTNESS: return "Indirectness";
            case IMPRECISION: return "Imprecision";
            case PUBLICATIONBIAS: return "Publication bias";
            case DOSERESPONSEGRADIENT: return "Dose response gradient";
            case PLAUSIBLECONFOUNDING: return "Plausible confounding";
            case LARGEEFFECT: return "Large effect";
            default: return "?";
          }
    }


}

