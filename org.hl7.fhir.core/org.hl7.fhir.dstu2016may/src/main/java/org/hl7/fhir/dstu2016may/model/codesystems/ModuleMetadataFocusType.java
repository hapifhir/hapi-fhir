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

public enum ModuleMetadataFocusType {

        /**
         * The gender of the patient. For this item type, use HL7 administrative gender codes (OID: 2.16.840.1.113883.1.11.1)
         */
        PATIENTGENDER, 
        /**
         * A patient demographic category for which this artifact is applicable. Allows specification of age groups using coded values originating from the MeSH Code system (OID: 2.16.840.1.113883.6.177). More specifically, only codes from the AgeGroupObservationValue value set are valid for this field  [2.16.840.1.113883.11.75]
         */
        PATIENTAGEGROUP, 
        /**
         * The clinical concept(s) addressed by the artifact.  For example, disease, diagnostic test interpretation, medication ordering. Please refer to the implementation guide on which code system and codes to use
         */
        CLINICALFOCUS, 
        /**
         * The user types to which an artifact is targeted.  For example, PCP, Patient, Cardiologist, Behavioral Professional, Oral Health Professional, Prescriber, etc... taken from the NUCC Health Care provider taxonomyCode system (OID: 2.16.840.1.113883.6.101)
         */
        TARGETUSER, 
        /**
         * The settings in which the artifact is intended for use.  For example, admission, pre-op, etc
         */
        WORKFLOWSETTING, 
        /**
         * The context for the clinical task(s) represented by this artifact. Can be any task context represented by the HL7 ActTaskCode value set (OID: 2.16.840.1.113883.1.11.19846). General categories include: order entry, patient documentation and patient information review
         */
        WORKFLOWTASK, 
        /**
         * The venue in which an artifact could be used.  For example, Outpatient, Inpatient, Home, Nursing home. The code value may originate from either the HL7 ActEncounter (OID: 2.16.840.1.113883.1.11.13955) or NUCC non-individual provider codes OID: 2.16.840.1.113883.1.11.19465
         */
        CLINICALVENUE, 
        /**
         * The jurisdiction in which the artifact is intended to be used
         */
        JURISDICTION, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ModuleMetadataFocusType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("patient-gender".equals(codeString))
          return PATIENTGENDER;
        if ("patient-age-group".equals(codeString))
          return PATIENTAGEGROUP;
        if ("clinical-focus".equals(codeString))
          return CLINICALFOCUS;
        if ("target-user".equals(codeString))
          return TARGETUSER;
        if ("workflow-setting".equals(codeString))
          return WORKFLOWSETTING;
        if ("workflow-task".equals(codeString))
          return WORKFLOWTASK;
        if ("clinical-venue".equals(codeString))
          return CLINICALVENUE;
        if ("jurisdiction".equals(codeString))
          return JURISDICTION;
        throw new FHIRException("Unknown ModuleMetadataFocusType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PATIENTGENDER: return "patient-gender";
            case PATIENTAGEGROUP: return "patient-age-group";
            case CLINICALFOCUS: return "clinical-focus";
            case TARGETUSER: return "target-user";
            case WORKFLOWSETTING: return "workflow-setting";
            case WORKFLOWTASK: return "workflow-task";
            case CLINICALVENUE: return "clinical-venue";
            case JURISDICTION: return "jurisdiction";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/module-metadata-focus-type";
        }
        public String getDefinition() {
          switch (this) {
            case PATIENTGENDER: return "The gender of the patient. For this item type, use HL7 administrative gender codes (OID: 2.16.840.1.113883.1.11.1)";
            case PATIENTAGEGROUP: return "A patient demographic category for which this artifact is applicable. Allows specification of age groups using coded values originating from the MeSH Code system (OID: 2.16.840.1.113883.6.177). More specifically, only codes from the AgeGroupObservationValue value set are valid for this field  [2.16.840.1.113883.11.75]";
            case CLINICALFOCUS: return "The clinical concept(s) addressed by the artifact.  For example, disease, diagnostic test interpretation, medication ordering. Please refer to the implementation guide on which code system and codes to use";
            case TARGETUSER: return "The user types to which an artifact is targeted.  For example, PCP, Patient, Cardiologist, Behavioral Professional, Oral Health Professional, Prescriber, etc... taken from the NUCC Health Care provider taxonomyCode system (OID: 2.16.840.1.113883.6.101)";
            case WORKFLOWSETTING: return "The settings in which the artifact is intended for use.  For example, admission, pre-op, etc";
            case WORKFLOWTASK: return "The context for the clinical task(s) represented by this artifact. Can be any task context represented by the HL7 ActTaskCode value set (OID: 2.16.840.1.113883.1.11.19846). General categories include: order entry, patient documentation and patient information review";
            case CLINICALVENUE: return "The venue in which an artifact could be used.  For example, Outpatient, Inpatient, Home, Nursing home. The code value may originate from either the HL7 ActEncounter (OID: 2.16.840.1.113883.1.11.13955) or NUCC non-individual provider codes OID: 2.16.840.1.113883.1.11.19465";
            case JURISDICTION: return "The jurisdiction in which the artifact is intended to be used";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PATIENTGENDER: return "Patient Gender";
            case PATIENTAGEGROUP: return "Patient Age Group";
            case CLINICALFOCUS: return "Clinical Focus";
            case TARGETUSER: return "Target User";
            case WORKFLOWSETTING: return "Workflow Setting";
            case WORKFLOWTASK: return "Workflow Task";
            case CLINICALVENUE: return "Clinical Venue";
            case JURISDICTION: return "Jurisdiction";
            default: return "?";
          }
    }


}

