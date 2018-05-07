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

// Generated on Tue, Sep 26, 2017 07:05-0400 for FHIR v3.1.0


import org.hl7.fhir.exceptions.FHIRException;

public enum ImmunizationRecommendationDateCriterion {

        /**
         * Date the next dose is considered due.
         */
        DUE, 
        /**
         * At the recommended date.
         */
        RECOMMENDED, 
        /**
         * As early as possible.
         */
        EARLIEST, 
        /**
         * Date the next dose is considered overdue.
         */
        OVERDUE, 
        /**
         * The latest date the next dose is to be given.
         */
        LATEST, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ImmunizationRecommendationDateCriterion fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("due".equals(codeString))
          return DUE;
        if ("recommended".equals(codeString))
          return RECOMMENDED;
        if ("earliest".equals(codeString))
          return EARLIEST;
        if ("overdue".equals(codeString))
          return OVERDUE;
        if ("latest".equals(codeString))
          return LATEST;
        throw new FHIRException("Unknown ImmunizationRecommendationDateCriterion code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case DUE: return "due";
            case RECOMMENDED: return "recommended";
            case EARLIEST: return "earliest";
            case OVERDUE: return "overdue";
            case LATEST: return "latest";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/immunization-recommendation-date-criterion";
        }
        public String getDefinition() {
          switch (this) {
            case DUE: return "Date the next dose is considered due.";
            case RECOMMENDED: return "At the recommended date.";
            case EARLIEST: return "As early as possible.";
            case OVERDUE: return "Date the next dose is considered overdue.";
            case LATEST: return "The latest date the next dose is to be given.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case DUE: return "Due";
            case RECOMMENDED: return "Recommended";
            case EARLIEST: return "Earliest Date";
            case OVERDUE: return "Past Due Date";
            case LATEST: return "Latest";
            default: return "?";
          }
    }


}

