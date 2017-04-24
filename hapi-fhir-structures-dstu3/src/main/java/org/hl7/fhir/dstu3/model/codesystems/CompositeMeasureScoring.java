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

public enum CompositeMeasureScoring {

        /**
         * Opportunity scoring combines the scores from component measures by combining the numerators and denominators for each component
         */
        OPPORTUNITY, 
        /**
         * All-or-nothing scoring includes an individual in the numerator of the composite measure if they are in the numerators of all of the component measures in which they are in the denominator
         */
        ALLORNOTHING, 
        /**
         * Linear scoring gives an individual a score based on the number of numerators in which they appear
         */
        LINEAR, 
        /**
         * Weighted scoring gives an individual a score based on a weighted factor for each component numerator in which they appear
         */
        WEIGHTED, 
        /**
         * added to help the parsers
         */
        NULL;
        public static CompositeMeasureScoring fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("opportunity".equals(codeString))
          return OPPORTUNITY;
        if ("all-or-nothing".equals(codeString))
          return ALLORNOTHING;
        if ("linear".equals(codeString))
          return LINEAR;
        if ("weighted".equals(codeString))
          return WEIGHTED;
        throw new FHIRException("Unknown CompositeMeasureScoring code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case OPPORTUNITY: return "opportunity";
            case ALLORNOTHING: return "all-or-nothing";
            case LINEAR: return "linear";
            case WEIGHTED: return "weighted";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/composite-measure-scoring";
        }
        public String getDefinition() {
          switch (this) {
            case OPPORTUNITY: return "Opportunity scoring combines the scores from component measures by combining the numerators and denominators for each component";
            case ALLORNOTHING: return "All-or-nothing scoring includes an individual in the numerator of the composite measure if they are in the numerators of all of the component measures in which they are in the denominator";
            case LINEAR: return "Linear scoring gives an individual a score based on the number of numerators in which they appear";
            case WEIGHTED: return "Weighted scoring gives an individual a score based on a weighted factor for each component numerator in which they appear";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case OPPORTUNITY: return "Opportunity";
            case ALLORNOTHING: return "All-or-nothing";
            case LINEAR: return "Linear";
            case WEIGHTED: return "Weighted";
            default: return "?";
          }
    }


}

