package org.hl7.fhir.dstu2016may.model.codesystems;

/*-
 * #%L
 * org.hl7.fhir.dstu2016may
 * %%
 * Copyright (C) 2014 - 2019 Health Level 7
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


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

// Generated on Sun, May 8, 2016 03:05+1000 for FHIR v1.4.0


import org.hl7.fhir.exceptions.FHIRException;

public enum QicoreConditionCriticality {

        /**
         * The criticality of the condition is not specified
         */
        UNSPECIFIED, 
        /**
         * The condition is expected to resolve on its own
         */
        SELFRESOLVING, 
        /**
         * The condition is considered to be controllable
         */
        CONTROLLABLE, 
        /**
         * The condition may result in partial or full loss of function or capacity
         */
        FUNCTIONALLOSS, 
        /**
         * The condition is considered to be life-threatening
         */
        LIFETHREATENING, 
        /**
         * The condition requires hospitalization
         */
        REQUIRESHOSPITALIZATION, 
        /**
         * added to help the parsers
         */
        NULL;
        public static QicoreConditionCriticality fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("unspecified".equals(codeString))
          return UNSPECIFIED;
        if ("self-resolving".equals(codeString))
          return SELFRESOLVING;
        if ("controllable".equals(codeString))
          return CONTROLLABLE;
        if ("functional-loss".equals(codeString))
          return FUNCTIONALLOSS;
        if ("life-threatening".equals(codeString))
          return LIFETHREATENING;
        if ("requires-hospitalization".equals(codeString))
          return REQUIRESHOSPITALIZATION;
        throw new FHIRException("Unknown QicoreConditionCriticality code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case UNSPECIFIED: return "unspecified";
            case SELFRESOLVING: return "self-resolving";
            case CONTROLLABLE: return "controllable";
            case FUNCTIONALLOSS: return "functional-loss";
            case LIFETHREATENING: return "life-threatening";
            case REQUIRESHOSPITALIZATION: return "requires-hospitalization";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/qicore-condition-criticality";
        }
        public String getDefinition() {
          switch (this) {
            case UNSPECIFIED: return "The criticality of the condition is not specified";
            case SELFRESOLVING: return "The condition is expected to resolve on its own";
            case CONTROLLABLE: return "The condition is considered to be controllable";
            case FUNCTIONALLOSS: return "The condition may result in partial or full loss of function or capacity";
            case LIFETHREATENING: return "The condition is considered to be life-threatening";
            case REQUIRESHOSPITALIZATION: return "The condition requires hospitalization";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case UNSPECIFIED: return "Not Specified";
            case SELFRESOLVING: return "Expected to Self-Resolve";
            case CONTROLLABLE: return "Controllable";
            case FUNCTIONALLOSS: return "Potential loss of function or capacity";
            case LIFETHREATENING: return "Life Threatening";
            case REQUIRESHOSPITALIZATION: return "Requires Hospitalization";
            default: return "?";
          }
    }


}

