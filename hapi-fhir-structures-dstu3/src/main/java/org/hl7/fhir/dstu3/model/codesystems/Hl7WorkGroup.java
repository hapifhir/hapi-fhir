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

public enum Hl7WorkGroup {

        /**
         * Community Based Collaborative Care (http://www.hl7.org/Special/committees/cbcc/index.cfm)
         */
        CBCC, 
        /**
         * Clinical Decision Support (http://www.hl7.org/Special/committees/dss/index.cfm)
         */
        CDS, 
        /**
         * Clinical Quality Information (http://www.hl7.org/Special/committees/cqi/index.cfm)
         */
        CQI, 
        /**
         * Clinical Genomics (http://www.hl7.org/Special/committees/clingenomics/index.cfm)
         */
        CG, 
        /**
         * Health Care Devices (http://www.hl7.org/Special/committees/healthcaredevices/index.cfm)
         */
        DEV, 
        /**
         * Electronic Health Records (http://www.hl7.org/special/committees/ehr/index.cfm)
         */
        EHR, 
        /**
         * FHIR Infrastructure (http://www.hl7.org/Special/committees/fiwg/index.cfm)
         */
        FHIR, 
        /**
         * Financial Management (http://www.hl7.org/Special/committees/fm/index.cfm)
         */
        FM, 
        /**
         * Health Standards Integration (http://www.hl7.org/Special/committees/hsi/index.cfm)
         */
        HSI, 
        /**
         * Imaging Integration (http://www.hl7.org/Special/committees/imagemgt/index.cfm)
         */
        II, 
        /**
         * Infrastructure And Messaging (http://www.hl7.org/special/committees/inm/index.cfm)
         */
        INM, 
        /**
         * Implementable Technology Specifications (http://www.hl7.org/special/committees/xml/index.cfm)
         */
        ITS, 
        /**
         * Orders and Observations (http://www.hl7.org/Special/committees/orders/index.cfm)
         */
        OO, 
        /**
         * Patient Administration (http://www.hl7.org/Special/committees/pafm/index.cfm)
         */
        PA, 
        /**
         * Patient Care (http://www.hl7.org/Special/committees/patientcare/index.cfm)
         */
        PC, 
        /**
         * Public Health and Emergency Response (http://www.hl7.org/Special/committees/pher/index.cfm)
         */
        PHER, 
        /**
         * Pharmacy (http://www.hl7.org/Special/committees/medication/index.cfm)
         */
        PHX, 
        /**
         * Regulated Clinical Research Information Management (http://www.hl7.org/Special/committees/rcrim/index.cfm)
         */
        RCRIM, 
        /**
         * Structured Documents (http://www.hl7.org/Special/committees/structure/index.cfm)
         */
        SD, 
        /**
         * Security (http://www.hl7.org/Special/committees/secure/index.cfm)
         */
        SEC, 
        /**
         * US Realm Taskforce (http://wiki.hl7.org/index.php?title=US_Realm_Task_Force)
         */
        US, 
        /**
         * Vocabulary (http://www.hl7.org/Special/committees/Vocab/index.cfm)
         */
        VOCAB, 
        /**
         * Application Implementation and Design (http://www.hl7.org/Special/committees/java/index.cfm)
         */
        AID, 
        /**
         * added to help the parsers
         */
        NULL;
        public static Hl7WorkGroup fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("cbcc".equals(codeString))
          return CBCC;
        if ("cds".equals(codeString))
          return CDS;
        if ("cqi".equals(codeString))
          return CQI;
        if ("cg".equals(codeString))
          return CG;
        if ("dev".equals(codeString))
          return DEV;
        if ("ehr".equals(codeString))
          return EHR;
        if ("fhir".equals(codeString))
          return FHIR;
        if ("fm".equals(codeString))
          return FM;
        if ("hsi".equals(codeString))
          return HSI;
        if ("ii".equals(codeString))
          return II;
        if ("inm".equals(codeString))
          return INM;
        if ("its".equals(codeString))
          return ITS;
        if ("oo".equals(codeString))
          return OO;
        if ("pa".equals(codeString))
          return PA;
        if ("pc".equals(codeString))
          return PC;
        if ("pher".equals(codeString))
          return PHER;
        if ("phx".equals(codeString))
          return PHX;
        if ("rcrim".equals(codeString))
          return RCRIM;
        if ("sd".equals(codeString))
          return SD;
        if ("sec".equals(codeString))
          return SEC;
        if ("us".equals(codeString))
          return US;
        if ("vocab".equals(codeString))
          return VOCAB;
        if ("aid".equals(codeString))
          return AID;
        throw new FHIRException("Unknown Hl7WorkGroup code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case CBCC: return "cbcc";
            case CDS: return "cds";
            case CQI: return "cqi";
            case CG: return "cg";
            case DEV: return "dev";
            case EHR: return "ehr";
            case FHIR: return "fhir";
            case FM: return "fm";
            case HSI: return "hsi";
            case II: return "ii";
            case INM: return "inm";
            case ITS: return "its";
            case OO: return "oo";
            case PA: return "pa";
            case PC: return "pc";
            case PHER: return "pher";
            case PHX: return "phx";
            case RCRIM: return "rcrim";
            case SD: return "sd";
            case SEC: return "sec";
            case US: return "us";
            case VOCAB: return "vocab";
            case AID: return "aid";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/hl7-work-group";
        }
        public String getDefinition() {
          switch (this) {
            case CBCC: return "Community Based Collaborative Care (http://www.hl7.org/Special/committees/cbcc/index.cfm)";
            case CDS: return "Clinical Decision Support (http://www.hl7.org/Special/committees/dss/index.cfm)";
            case CQI: return "Clinical Quality Information (http://www.hl7.org/Special/committees/cqi/index.cfm)";
            case CG: return "Clinical Genomics (http://www.hl7.org/Special/committees/clingenomics/index.cfm)";
            case DEV: return "Health Care Devices (http://www.hl7.org/Special/committees/healthcaredevices/index.cfm)";
            case EHR: return "Electronic Health Records (http://www.hl7.org/special/committees/ehr/index.cfm)";
            case FHIR: return "FHIR Infrastructure (http://www.hl7.org/Special/committees/fiwg/index.cfm)";
            case FM: return "Financial Management (http://www.hl7.org/Special/committees/fm/index.cfm)";
            case HSI: return "Health Standards Integration (http://www.hl7.org/Special/committees/hsi/index.cfm)";
            case II: return "Imaging Integration (http://www.hl7.org/Special/committees/imagemgt/index.cfm)";
            case INM: return "Infrastructure And Messaging (http://www.hl7.org/special/committees/inm/index.cfm)";
            case ITS: return "Implementable Technology Specifications (http://www.hl7.org/special/committees/xml/index.cfm)";
            case OO: return "Orders and Observations (http://www.hl7.org/Special/committees/orders/index.cfm)";
            case PA: return "Patient Administration (http://www.hl7.org/Special/committees/pafm/index.cfm)";
            case PC: return "Patient Care (http://www.hl7.org/Special/committees/patientcare/index.cfm)";
            case PHER: return "Public Health and Emergency Response (http://www.hl7.org/Special/committees/pher/index.cfm)";
            case PHX: return "Pharmacy (http://www.hl7.org/Special/committees/medication/index.cfm)";
            case RCRIM: return "Regulated Clinical Research Information Management (http://www.hl7.org/Special/committees/rcrim/index.cfm)";
            case SD: return "Structured Documents (http://www.hl7.org/Special/committees/structure/index.cfm)";
            case SEC: return "Security (http://www.hl7.org/Special/committees/secure/index.cfm)";
            case US: return "US Realm Taskforce (http://wiki.hl7.org/index.php?title=US_Realm_Task_Force)";
            case VOCAB: return "Vocabulary (http://www.hl7.org/Special/committees/Vocab/index.cfm)";
            case AID: return "Application Implementation and Design (http://www.hl7.org/Special/committees/java/index.cfm)";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case CBCC: return "Community Based Collaborative Care";
            case CDS: return "Clinical Decision Support";
            case CQI: return "Clinical Quality Information";
            case CG: return "Clinical Genomics";
            case DEV: return "Health Care Devices";
            case EHR: return "Electronic Health Records";
            case FHIR: return "FHIR Infrastructure";
            case FM: return "Financial Management";
            case HSI: return "Health Standards Integration";
            case II: return "Imaging Integration";
            case INM: return "Infrastructure And Messaging";
            case ITS: return "Implementable Technology Specifications";
            case OO: return "Orders and Observations";
            case PA: return "Patient Administration";
            case PC: return "Patient Care";
            case PHER: return "Public Health and Emergency Response";
            case PHX: return "Pharmacy";
            case RCRIM: return "Regulated Clinical Research Information Management";
            case SD: return "Structured Documents";
            case SEC: return "Security";
            case US: return "US Realm Taskforce";
            case VOCAB: return "Vocabulary";
            case AID: return "Application Implementation and Design";
            default: return "?";
          }
    }


}

