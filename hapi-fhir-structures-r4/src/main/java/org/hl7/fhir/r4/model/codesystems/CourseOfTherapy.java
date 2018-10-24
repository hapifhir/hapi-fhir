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

// Generated on Thu, Sep 13, 2018 09:04-0400 for FHIR v3.5.0


import org.hl7.fhir.exceptions.FHIRException;

public enum CourseOfTherapy {

        /**
         * A medication which is expected to be continued beyond the present order and which the patient should be assumed to be taking unless explicitly stopped.
         */
        CONTINUOUS, 
        /**
         * A medication which the patient is only expected to consume for the duration of the current order and which is not expected to be renewed.
         */
        ACUTE, 
        /**
         * A medication which is expected to be used on a part time basis at certain times of the year.
         */
        SEASONAL, 
        /**
         * added to help the parsers
         */
        NULL;
        public static CourseOfTherapy fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("continuous".equals(codeString))
          return CONTINUOUS;
        if ("acute".equals(codeString))
          return ACUTE;
        if ("seasonal".equals(codeString))
          return SEASONAL;
        throw new FHIRException("Unknown CourseOfTherapy code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case CONTINUOUS: return "continuous";
            case ACUTE: return "acute";
            case SEASONAL: return "seasonal";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://terminology.hl7.org/CodeSystem/course-of-therapy";
        }
        public String getDefinition() {
          switch (this) {
            case CONTINUOUS: return "A medication which is expected to be continued beyond the present order and which the patient should be assumed to be taking unless explicitly stopped.";
            case ACUTE: return "A medication which the patient is only expected to consume for the duration of the current order and which is not expected to be renewed.";
            case SEASONAL: return "A medication which is expected to be used on a part time basis at certain times of the year.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case CONTINUOUS: return "Continuous long term therapy";
            case ACUTE: return "Short course (acute) therapy";
            case SEASONAL: return "Seasonal";
            default: return "?";
          }
    }


}

