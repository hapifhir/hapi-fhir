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

public enum ObservationRelationshiptypes {

        /**
         * This observation is a group observation (e.g. a battery, a panel of tests, a set of vital sign measurements) that includes the target as a member of the group.
         */
        HASMEMBER, 
        /**
         * The target resource (Observation or QuestionnaireResponse) is part of the information from which this observation value is derived. (e.g. calculated anion gap, Apgar score)  NOTE:  "derived-from" is the only logical choice when referencing QuestionnaireResponse.
         */
        DERIVEDFROM, 
        /**
         * This observation follows the target observation (e.g. timed tests such as Glucose Tolerance Test).
         */
        SEQUELTO, 
        /**
         * This observation replaces a previous observation (i.e. a revised value). The target observation is now obsolete.
         */
        REPLACES, 
        /**
         * The value of the target observation qualifies (refines) the semantics of the source observation (e.g. a lipemia measure target from a plasma measure).
         */
        QUALIFIEDBY, 
        /**
         * The value of the target observation interferes (degrades quality, or prevents valid observation) with the semantics of the source observation (e.g. a hemolysis measure target from a plasma potassium measure, which has no value).
         */
        INTERFEREDBY, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ObservationRelationshiptypes fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("has-member".equals(codeString))
          return HASMEMBER;
        if ("derived-from".equals(codeString))
          return DERIVEDFROM;
        if ("sequel-to".equals(codeString))
          return SEQUELTO;
        if ("replaces".equals(codeString))
          return REPLACES;
        if ("qualified-by".equals(codeString))
          return QUALIFIEDBY;
        if ("interfered-by".equals(codeString))
          return INTERFEREDBY;
        throw new FHIRException("Unknown ObservationRelationshiptypes code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case HASMEMBER: return "has-member";
            case DERIVEDFROM: return "derived-from";
            case SEQUELTO: return "sequel-to";
            case REPLACES: return "replaces";
            case QUALIFIEDBY: return "qualified-by";
            case INTERFEREDBY: return "interfered-by";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/observation-relationshiptypes";
        }
        public String getDefinition() {
          switch (this) {
            case HASMEMBER: return "This observation is a group observation (e.g. a battery, a panel of tests, a set of vital sign measurements) that includes the target as a member of the group.";
            case DERIVEDFROM: return "The target resource (Observation or QuestionnaireResponse) is part of the information from which this observation value is derived. (e.g. calculated anion gap, Apgar score)  NOTE:  \"derived-from\" is the only logical choice when referencing QuestionnaireResponse.";
            case SEQUELTO: return "This observation follows the target observation (e.g. timed tests such as Glucose Tolerance Test).";
            case REPLACES: return "This observation replaces a previous observation (i.e. a revised value). The target observation is now obsolete.";
            case QUALIFIEDBY: return "The value of the target observation qualifies (refines) the semantics of the source observation (e.g. a lipemia measure target from a plasma measure).";
            case INTERFEREDBY: return "The value of the target observation interferes (degrades quality, or prevents valid observation) with the semantics of the source observation (e.g. a hemolysis measure target from a plasma potassium measure, which has no value).";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case HASMEMBER: return "Has Member";
            case DERIVEDFROM: return "Derived From";
            case SEQUELTO: return "Sequel To";
            case REPLACES: return "Replaces";
            case QUALIFIEDBY: return "Qualified By";
            case INTERFEREDBY: return "Interfered By";
            default: return "?";
          }
    }


}

