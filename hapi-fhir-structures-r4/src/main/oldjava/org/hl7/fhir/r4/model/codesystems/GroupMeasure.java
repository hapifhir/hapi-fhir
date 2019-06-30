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

public enum GroupMeasure {

        /**
         * Aggregated using Mean of participant values.
         */
        MEAN, 
        /**
         * Aggregated using Median of participant values.
         */
        MEDIAN, 
        /**
         * Aggregated using Mean of study mean values.
         */
        MEANOFMEAN, 
        /**
         * Aggregated using Mean of study median values.
         */
        MEANOFMEDIAN, 
        /**
         * Aggregated using Median of study mean values.
         */
        MEDIANOFMEAN, 
        /**
         * Aggregated using Median of study median values.
         */
        MEDIANOFMEDIAN, 
        /**
         * added to help the parsers
         */
        NULL;
        public static GroupMeasure fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("mean".equals(codeString))
          return MEAN;
        if ("median".equals(codeString))
          return MEDIAN;
        if ("mean-of-mean".equals(codeString))
          return MEANOFMEAN;
        if ("mean-of-median".equals(codeString))
          return MEANOFMEDIAN;
        if ("median-of-mean".equals(codeString))
          return MEDIANOFMEAN;
        if ("median-of-median".equals(codeString))
          return MEDIANOFMEDIAN;
        throw new FHIRException("Unknown GroupMeasure code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case MEAN: return "mean";
            case MEDIAN: return "median";
            case MEANOFMEAN: return "mean-of-mean";
            case MEANOFMEDIAN: return "mean-of-median";
            case MEDIANOFMEAN: return "median-of-mean";
            case MEDIANOFMEDIAN: return "median-of-median";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/group-measure";
        }
        public String getDefinition() {
          switch (this) {
            case MEAN: return "Aggregated using Mean of participant values.";
            case MEDIAN: return "Aggregated using Median of participant values.";
            case MEANOFMEAN: return "Aggregated using Mean of study mean values.";
            case MEANOFMEDIAN: return "Aggregated using Mean of study median values.";
            case MEDIANOFMEAN: return "Aggregated using Median of study mean values.";
            case MEDIANOFMEDIAN: return "Aggregated using Median of study median values.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case MEAN: return "Mean";
            case MEDIAN: return "Median";
            case MEANOFMEAN: return "Mean of Study Means";
            case MEANOFMEDIAN: return "Mean of Study Medins";
            case MEDIANOFMEAN: return "Median of Study Means";
            case MEDIANOFMEDIAN: return "Median of Study Medians";
            default: return "?";
          }
    }


}

