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

public enum ImmunizationOrigin {

        /**
         * The data for the immunization event originated with another provider.
         */
        PROVIDER, 
        /**
         * The data for the immunization event originated with a written record for the patient.
         */
        RECORD, 
        /**
         * The data for the immunization event originated from the recollection of the patient or parent/guardian of the patient.
         */
        RECALL, 
        /**
         * The data for the immunization event originated with a school record for the patient.
         */
        SCHOOL, 
        /**
         * The data for the immunization event originated with an immunization information system (IIS) or registry operating within the jurisdiction.
         */
        JURISDICTION, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ImmunizationOrigin fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("provider".equals(codeString))
          return PROVIDER;
        if ("record".equals(codeString))
          return RECORD;
        if ("recall".equals(codeString))
          return RECALL;
        if ("school".equals(codeString))
          return SCHOOL;
        if ("jurisdiction".equals(codeString))
          return JURISDICTION;
        throw new FHIRException("Unknown ImmunizationOrigin code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PROVIDER: return "provider";
            case RECORD: return "record";
            case RECALL: return "recall";
            case SCHOOL: return "school";
            case JURISDICTION: return "jurisdiction";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://terminology.hl7.org/CodeSystem/immunization-origin";
        }
        public String getDefinition() {
          switch (this) {
            case PROVIDER: return "The data for the immunization event originated with another provider.";
            case RECORD: return "The data for the immunization event originated with a written record for the patient.";
            case RECALL: return "The data for the immunization event originated from the recollection of the patient or parent/guardian of the patient.";
            case SCHOOL: return "The data for the immunization event originated with a school record for the patient.";
            case JURISDICTION: return "The data for the immunization event originated with an immunization information system (IIS) or registry operating within the jurisdiction.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PROVIDER: return "Other Provider";
            case RECORD: return "Written Record";
            case RECALL: return "Parent/Guardian/Patient Recall";
            case SCHOOL: return "School Record";
            case JURISDICTION: return "Jurisdictional IIS";
            default: return "?";
          }
    }


}

