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

public enum SynthesisType {

        /**
         * A meta-analysis of the summary data of estimates from individual studies or data sets.
         */
        STDMA, 
        /**
         * A meta-analysis of the individual participant data from individual studies or data sets.
         */
        IPDMA, 
        /**
         * An indirect meta-analysis derived from 2 or more direct comparisons in a network meta-analysis.
         */
        INDIRECTNMA, 
        /**
         * An composite meta-analysis derived from direct comparisons and indirect comparisons in a network meta-analysis.
         */
        COMBINEDNMA, 
        /**
         * A range of results across a body of evidence.
         */
        RANGE, 
        /**
         * An approach describing a body of evidence by categorically classifying individual studies (eg 3 studies showed beneft and 2 studied found no effect).
         */
        CLASSIFICATION, 
        /**
         * added to help the parsers
         */
        NULL;
        public static SynthesisType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("std-MA".equals(codeString))
          return STDMA;
        if ("IPD-MA".equals(codeString))
          return IPDMA;
        if ("indirect-NMA".equals(codeString))
          return INDIRECTNMA;
        if ("combined-NMA".equals(codeString))
          return COMBINEDNMA;
        if ("range".equals(codeString))
          return RANGE;
        if ("classification".equals(codeString))
          return CLASSIFICATION;
        throw new FHIRException("Unknown SynthesisType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case STDMA: return "std-MA";
            case IPDMA: return "IPD-MA";
            case INDIRECTNMA: return "indirect-NMA";
            case COMBINEDNMA: return "combined-NMA";
            case RANGE: return "range";
            case CLASSIFICATION: return "classification";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://terminology.hl7.org/CodeSystem/synthesis-type";
        }
        public String getDefinition() {
          switch (this) {
            case STDMA: return "A meta-analysis of the summary data of estimates from individual studies or data sets.";
            case IPDMA: return "A meta-analysis of the individual participant data from individual studies or data sets.";
            case INDIRECTNMA: return "An indirect meta-analysis derived from 2 or more direct comparisons in a network meta-analysis.";
            case COMBINEDNMA: return "An composite meta-analysis derived from direct comparisons and indirect comparisons in a network meta-analysis.";
            case RANGE: return "A range of results across a body of evidence.";
            case CLASSIFICATION: return "An approach describing a body of evidence by categorically classifying individual studies (eg 3 studies showed beneft and 2 studied found no effect).";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case STDMA: return "summary data meta-analysis";
            case IPDMA: return "individual patient data meta-analysis";
            case INDIRECTNMA: return "indirect network meta-analysis";
            case COMBINEDNMA: return "combined direct plus indirect network meta-analysis";
            case RANGE: return "range of results";
            case CLASSIFICATION: return "classifcation of results";
            default: return "?";
          }
    }


}

