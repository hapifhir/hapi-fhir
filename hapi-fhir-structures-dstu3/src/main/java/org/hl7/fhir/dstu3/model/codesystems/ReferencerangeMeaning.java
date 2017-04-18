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

public enum ReferencerangeMeaning {

        /**
         * General types of reference range.
         */
        TYPE, 
        /**
         * Based on 95th percentile for the relevant control population.
         */
        NORMAL, 
        /**
         * The range that is recommended by a relevant professional body.
         */
        RECOMMENDED, 
        /**
         * The range at which treatment would/should be considered.
         */
        TREATMENT, 
        /**
         * The optimal range for best therapeutic outcomes.
         */
        THERAPEUTIC, 
        /**
         * The optimal range for best therapeutic outcomes for a specimen taken immediately before administration.
         */
        PRE, 
        /**
         * The optimal range for best therapeutic outcomes for a specimen taken immediately after administration.
         */
        POST, 
        /**
         * Endocrine related states that change the expected value.
         */
        ENDOCRINE, 
        /**
         * An expected range in an individual prior to puberty.
         */
        PREPUBERTY, 
        /**
         * An expected range in an individual during the follicular stage of the cycle.
         */
        FOLLICULAR, 
        /**
         * An expected range in an individual during the follicular stage of the cycle.
         */
        MIDCYCLE, 
        /**
         * An expected range in an individual during the luteal stage of the cycle.
         */
        LUTEAL, 
        /**
         * An expected range in an individual post-menopause.
         */
        POSTMEOPAUSAL, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ReferencerangeMeaning fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("type".equals(codeString))
          return TYPE;
        if ("normal".equals(codeString))
          return NORMAL;
        if ("recommended".equals(codeString))
          return RECOMMENDED;
        if ("treatment".equals(codeString))
          return TREATMENT;
        if ("therapeutic".equals(codeString))
          return THERAPEUTIC;
        if ("pre".equals(codeString))
          return PRE;
        if ("post".equals(codeString))
          return POST;
        if ("endocrine".equals(codeString))
          return ENDOCRINE;
        if ("pre-puberty".equals(codeString))
          return PREPUBERTY;
        if ("follicular".equals(codeString))
          return FOLLICULAR;
        if ("midcycle".equals(codeString))
          return MIDCYCLE;
        if ("luteal".equals(codeString))
          return LUTEAL;
        if ("postmeopausal".equals(codeString))
          return POSTMEOPAUSAL;
        throw new FHIRException("Unknown ReferencerangeMeaning code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case TYPE: return "type";
            case NORMAL: return "normal";
            case RECOMMENDED: return "recommended";
            case TREATMENT: return "treatment";
            case THERAPEUTIC: return "therapeutic";
            case PRE: return "pre";
            case POST: return "post";
            case ENDOCRINE: return "endocrine";
            case PREPUBERTY: return "pre-puberty";
            case FOLLICULAR: return "follicular";
            case MIDCYCLE: return "midcycle";
            case LUTEAL: return "luteal";
            case POSTMEOPAUSAL: return "postmeopausal";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/referencerange-meaning";
        }
        public String getDefinition() {
          switch (this) {
            case TYPE: return "General types of reference range.";
            case NORMAL: return "Based on 95th percentile for the relevant control population.";
            case RECOMMENDED: return "The range that is recommended by a relevant professional body.";
            case TREATMENT: return "The range at which treatment would/should be considered.";
            case THERAPEUTIC: return "The optimal range for best therapeutic outcomes.";
            case PRE: return "The optimal range for best therapeutic outcomes for a specimen taken immediately before administration.";
            case POST: return "The optimal range for best therapeutic outcomes for a specimen taken immediately after administration.";
            case ENDOCRINE: return "Endocrine related states that change the expected value.";
            case PREPUBERTY: return "An expected range in an individual prior to puberty.";
            case FOLLICULAR: return "An expected range in an individual during the follicular stage of the cycle.";
            case MIDCYCLE: return "An expected range in an individual during the follicular stage of the cycle.";
            case LUTEAL: return "An expected range in an individual during the luteal stage of the cycle.";
            case POSTMEOPAUSAL: return "An expected range in an individual post-menopause.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case TYPE: return "Type";
            case NORMAL: return "Normal Range";
            case RECOMMENDED: return "Recommended Range";
            case TREATMENT: return "Treatment Range";
            case THERAPEUTIC: return "Therapeutic Desired Level";
            case PRE: return "Pre Therapeutic Desired Level";
            case POST: return "Post Therapeutic Desired Level";
            case ENDOCRINE: return "Endocrine";
            case PREPUBERTY: return "Pre-Puberty";
            case FOLLICULAR: return "Follicular Stage";
            case MIDCYCLE: return "MidCycle";
            case LUTEAL: return "Luteal";
            case POSTMEOPAUSAL: return "Post-Menopause";
            default: return "?";
          }
    }


}

