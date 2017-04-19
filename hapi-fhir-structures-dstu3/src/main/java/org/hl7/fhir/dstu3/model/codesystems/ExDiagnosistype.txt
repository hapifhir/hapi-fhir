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

public enum ExDiagnosistype {

        /**
         * The diagnosis given as the reason why the patient was admitted to the hospital.
         */
        ADMITTING, 
        /**
         * A diagnosis made on the basis of medical signs and patient-reported symptoms, rather than diagnostic tests.
         */
        CLINICAL, 
        /**
         * One of a set of the possible diagnoses that could be connected to the signs, symptoms, and lab findings.
         */
        DIFFERENTIAL, 
        /**
         * The diagnosis given when the patient is discharged from the hospital.
         */
        DISCHARGE, 
        /**
         * A diagnosis based significantly on laboratory reports or test results, rather than the physical examination of the patient.
         */
        LABORATORY, 
        /**
         * A diagnosis which identifies people's responses to situations in their lives, such as a readiness to change or a willingness to accept assistance.
         */
        NURSING, 
        /**
         * A diagnosis determined prior to birth.
         */
        PRENATAL, 
        /**
         * The single medical diagnosis that is most relevant to the patient's chief complaint or need for treatment.
         */
        PRINCIPAL, 
        /**
         * A diagnosis based primarily on the results from medical imaging studies.
         */
        RADIOLOGY, 
        /**
         * A diagnosis determined using telemedicine techniques.
         */
        REMOTE, 
        /**
         * The labeling of an illness in a specific historical event using modern knowledge, methods and disease classifications.
         */
        RETROSPECTIVE, 
        /**
         * A diagnosis determined by the patient.
         */
        SELF, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ExDiagnosistype fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("admitting".equals(codeString))
          return ADMITTING;
        if ("clinical".equals(codeString))
          return CLINICAL;
        if ("differential".equals(codeString))
          return DIFFERENTIAL;
        if ("discharge".equals(codeString))
          return DISCHARGE;
        if ("laboratory".equals(codeString))
          return LABORATORY;
        if ("nursing".equals(codeString))
          return NURSING;
        if ("prenatal".equals(codeString))
          return PRENATAL;
        if ("principal".equals(codeString))
          return PRINCIPAL;
        if ("radiology".equals(codeString))
          return RADIOLOGY;
        if ("remote".equals(codeString))
          return REMOTE;
        if ("retrospective".equals(codeString))
          return RETROSPECTIVE;
        if ("self".equals(codeString))
          return SELF;
        throw new FHIRException("Unknown ExDiagnosistype code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ADMITTING: return "admitting";
            case CLINICAL: return "clinical";
            case DIFFERENTIAL: return "differential";
            case DISCHARGE: return "discharge";
            case LABORATORY: return "laboratory";
            case NURSING: return "nursing";
            case PRENATAL: return "prenatal";
            case PRINCIPAL: return "principal";
            case RADIOLOGY: return "radiology";
            case REMOTE: return "remote";
            case RETROSPECTIVE: return "retrospective";
            case SELF: return "self";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/ex-diagnosistype";
        }
        public String getDefinition() {
          switch (this) {
            case ADMITTING: return "The diagnosis given as the reason why the patient was admitted to the hospital.";
            case CLINICAL: return "A diagnosis made on the basis of medical signs and patient-reported symptoms, rather than diagnostic tests.";
            case DIFFERENTIAL: return "One of a set of the possible diagnoses that could be connected to the signs, symptoms, and lab findings.";
            case DISCHARGE: return "The diagnosis given when the patient is discharged from the hospital.";
            case LABORATORY: return "A diagnosis based significantly on laboratory reports or test results, rather than the physical examination of the patient.";
            case NURSING: return "A diagnosis which identifies people's responses to situations in their lives, such as a readiness to change or a willingness to accept assistance.";
            case PRENATAL: return "A diagnosis determined prior to birth.";
            case PRINCIPAL: return "The single medical diagnosis that is most relevant to the patient's chief complaint or need for treatment.";
            case RADIOLOGY: return "A diagnosis based primarily on the results from medical imaging studies.";
            case REMOTE: return "A diagnosis determined using telemedicine techniques.";
            case RETROSPECTIVE: return "The labeling of an illness in a specific historical event using modern knowledge, methods and disease classifications.";
            case SELF: return "A diagnosis determined by the patient.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ADMITTING: return "Admitting Diagnosis";
            case CLINICAL: return "Clinical Diagnosis";
            case DIFFERENTIAL: return "Differential Diagnosis";
            case DISCHARGE: return "Discharge Diagnosis";
            case LABORATORY: return "Laboratory Diagnosis";
            case NURSING: return "Nursing Diagnosis";
            case PRENATAL: return "Prenatal Diagnosis";
            case PRINCIPAL: return "Principal Diagnosis";
            case RADIOLOGY: return "Radiology Diagnosis";
            case REMOTE: return "Remote Diagnosis";
            case RETROSPECTIVE: return "Retrospective Diagnosis";
            case SELF: return "Self Diagnosis";
            default: return "?";
          }
    }


}

