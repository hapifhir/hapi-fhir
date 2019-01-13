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

public enum HspcOrganizationOrganizationtype {

        /**
         * A place where outpatients are provided medical treatments or advices.
         */
        _526758010, 
        /**
         * A center where patients with cancer are cared and treated.
         */
        _526758011, 
        /**
         * A place where dental services are provided.
         */
        _526758012, 
        /**
         * A center where patients are provided imaging services.
         */
        _526758013, 
        /**
         * A center where patients with kidney diseases are provided dialysis services.
         */
        _526758014, 
        /**
         * A place where people of different ages gain an education.
         */
        _526758015, 
        /**
         * A government organization set up for a specific purpose such as the management of resources, financial oversight of industries or national security issues.
         */
        _526758016, 
        /**
         * An organization which provides assistant health care for people at their homes.
         */
        _526758017, 
        /**
         * An institution where the sick or injuried people are provided medical and surgical treatments and nursing cares.
         */
        _526758018, 
        /**
         * A network or group of hospitals that work together to coordinate and deliver a broad spectrum of services to their community.
         */
        _526758019, 
        /**
         * A financial institution that sells insurance.
         */
        _526758020, 
        /**
         * A laboratory where tests are done on clinical specimens in order to get information about the health of a patient as pertaining to the diagnosis, treatment, and prevention of disease.
         */
        _526758021, 
        /**
         * A facility providing skilled, intermediate or custodial nursing care.
         */
        _526758022, 
        /**
         * A store or a place in the hospital where drugs and medicines are dispensed and sold.
         */
        _526758023, 
        /**
         * A center where outpatients are provided surgical services.
         */
        _526758024, 
        /**
         * added to help the parsers
         */
        NULL;
        public static HspcOrganizationOrganizationtype fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("526758010".equals(codeString))
          return _526758010;
        if ("526758011".equals(codeString))
          return _526758011;
        if ("526758012".equals(codeString))
          return _526758012;
        if ("526758013".equals(codeString))
          return _526758013;
        if ("526758014".equals(codeString))
          return _526758014;
        if ("526758015".equals(codeString))
          return _526758015;
        if ("526758016".equals(codeString))
          return _526758016;
        if ("526758017".equals(codeString))
          return _526758017;
        if ("526758018".equals(codeString))
          return _526758018;
        if ("526758019".equals(codeString))
          return _526758019;
        if ("526758020".equals(codeString))
          return _526758020;
        if ("526758021".equals(codeString))
          return _526758021;
        if ("526758022".equals(codeString))
          return _526758022;
        if ("526758023".equals(codeString))
          return _526758023;
        if ("526758024".equals(codeString))
          return _526758024;
        throw new FHIRException("Unknown HspcOrganizationOrganizationtype code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case _526758010: return "526758010";
            case _526758011: return "526758011";
            case _526758012: return "526758012";
            case _526758013: return "526758013";
            case _526758014: return "526758014";
            case _526758015: return "526758015";
            case _526758016: return "526758016";
            case _526758017: return "526758017";
            case _526758018: return "526758018";
            case _526758019: return "526758019";
            case _526758020: return "526758020";
            case _526758021: return "526758021";
            case _526758022: return "526758022";
            case _526758023: return "526758023";
            case _526758024: return "526758024";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7,org/fhir/organization-hspc-organizationtype";
        }
        public String getDefinition() {
          switch (this) {
            case _526758010: return "A place where outpatients are provided medical treatments or advices.";
            case _526758011: return "A center where patients with cancer are cared and treated.";
            case _526758012: return "A place where dental services are provided.";
            case _526758013: return "A center where patients are provided imaging services.";
            case _526758014: return "A center where patients with kidney diseases are provided dialysis services.";
            case _526758015: return "A place where people of different ages gain an education.";
            case _526758016: return "A government organization set up for a specific purpose such as the management of resources, financial oversight of industries or national security issues.";
            case _526758017: return "An organization which provides assistant health care for people at their homes.";
            case _526758018: return "An institution where the sick or injuried people are provided medical and surgical treatments and nursing cares.";
            case _526758019: return "A network or group of hospitals that work together to coordinate and deliver a broad spectrum of services to their community.";
            case _526758020: return "A financial institution that sells insurance.";
            case _526758021: return "A laboratory where tests are done on clinical specimens in order to get information about the health of a patient as pertaining to the diagnosis, treatment, and prevention of disease.";
            case _526758022: return "A facility providing skilled, intermediate or custodial nursing care.";
            case _526758023: return "A store or a place in the hospital where drugs and medicines are dispensed and sold.";
            case _526758024: return "A center where outpatients are provided surgical services.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case _526758010: return "Clinic";
            case _526758011: return "Cancer Center";
            case _526758012: return "Dental Office";
            case _526758013: return "Diagnostic Imaging Center";
            case _526758014: return "Dialysis Center";
            case _526758015: return "Educational Institute";
            case _526758016: return "Federal Agency";
            case _526758017: return "Home Health";
            case _526758018: return "Hospital";
            case _526758019: return "Hospital Network";
            case _526758020: return "Insurance Company";
            case _526758021: return "Medical Laboratory";
            case _526758022: return "Nursing Care Facility";
            case _526758023: return "Pharmacy";
            case _526758024: return "Surgical Center";
            default: return "?";
          }
    }


}

