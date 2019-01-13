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

public enum QicorePatientMilitaryService {

        /**
         * The military status is not indicated
         */
        NOTINDICATED, 
        /**
         * The subject has no history of military service
         */
        NOMILITARYSERVICE, 
        /**
         * The subject is has served in the military but is no longer active
         */
        VETERAN, 
        /**
         * The subject is not a reserve member and is currently engaged in full-time military activity
         */
        ACTIVEDUTY, 
        /**
         * The subject is a reserve member and is currently engaged in full-time military activity
         */
        ACTIVERESERVE, 
        /**
         * The subject is a reserve member and is not currently engaged in full-time military activity
         */
        INACTIVERESERVE, 
        /**
         * added to help the parsers
         */
        NULL;
        public static QicorePatientMilitaryService fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("not-indicated".equals(codeString))
          return NOTINDICATED;
        if ("no-military-service".equals(codeString))
          return NOMILITARYSERVICE;
        if ("veteran".equals(codeString))
          return VETERAN;
        if ("active-duty".equals(codeString))
          return ACTIVEDUTY;
        if ("active-reserve".equals(codeString))
          return ACTIVERESERVE;
        if ("inactive-reserve".equals(codeString))
          return INACTIVERESERVE;
        throw new FHIRException("Unknown QicorePatientMilitaryService code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case NOTINDICATED: return "not-indicated";
            case NOMILITARYSERVICE: return "no-military-service";
            case VETERAN: return "veteran";
            case ACTIVEDUTY: return "active-duty";
            case ACTIVERESERVE: return "active-reserve";
            case INACTIVERESERVE: return "inactive-reserve";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/qicore-military-service";
        }
        public String getDefinition() {
          switch (this) {
            case NOTINDICATED: return "The military status is not indicated";
            case NOMILITARYSERVICE: return "The subject has no history of military service";
            case VETERAN: return "The subject is has served in the military but is no longer active";
            case ACTIVEDUTY: return "The subject is not a reserve member and is currently engaged in full-time military activity";
            case ACTIVERESERVE: return "The subject is a reserve member and is currently engaged in full-time military activity";
            case INACTIVERESERVE: return "The subject is a reserve member and is not currently engaged in full-time military activity";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case NOTINDICATED: return "Not Indicated";
            case NOMILITARYSERVICE: return "No Military Service";
            case VETERAN: return "Veteran";
            case ACTIVEDUTY: return "Active Duty";
            case ACTIVERESERVE: return "Active Reserve";
            case INACTIVERESERVE: return "Inactive Reserve";
            default: return "?";
          }
    }


}

