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

// Generated on Sat, Nov 5, 2016 08:41-0400 for FHIR v1.7.0


import org.hl7.fhir.exceptions.FHIRException;

public enum ConsentCategory {

        /**
         * null
         */
        CAT1, 
        /**
         * Any instructions, written or given verbally by a patient to a health care provider in anticipation of potential need for medical treatment
         */
        ADVANCEDIRECTIVE, 
        /**
         * RWJ funded toolkit has several international example consent forms, and excellent overview of issues around medical informed consent
         */
        CAT2, 
        /**
         * Informed consent is the process of communication between a patient and physician that results in the patient’s authorization or agreement to undergo a specific medical intervention [AMA 1998]. For both ethical and legal reasons, patients must be given enough information to be fully informed before deciding to undergo a major treatment, and this informed consent must be documented in writing.
         */
        MEDICALCONSENT, 
        /**
         * null
         */
        CAT3, 
        /**
         * HIPAA 45 CFR Section 164.508 Uses and disclosures for which an authorization is required. (a) Standard: Authorizations for uses and disclosures. (1) Authorization required: General rule. Except as otherwise permitted or required by this subchapter, a covered entity may not use or disclose protected health information without an authorization that is valid under this section. When a covered entity obtains or receives a valid authorization for its use or disclosure of protected health information, such use or disclosure must be consistent with such authorization. Usage Note: Authorizations governed under this regulation meet the definition of an opt in class of consent directive.
         */
        HIPAA, 
        /**
         * SA Form SSA-827 (Authorization to Disclose Information to the Social Security Administration (SSA)). Form is available at https://www.socialsecurity.gov/forms/ssa-827-inst-sp.pdf
         */
        SSA827, 
        /**
         * May include federal and state jurisdictional privacy laws
         */
        CAT4, 
        /**
         * Michigan DCH-3927 Consent to Share Behavioral Health Information for Care Coordination Purposes, which combines 42 CFR Part 2 and Michigan Mental Health Code, Act 258 of 1974. Form is available at http://www.michigan.gov/documents/mdch/DCH-3927_Consent_to_Share_Health_Information_477005_7.docx
         */
        DCH3927, 
        /**
         * Squaxin Indian HIPAA and 42 CFR Part 2 Consent for Release and Exchange of Confidential Information, which permits consenter to select healthcare record type and types of treatment purposes.  This consent requires disclosers and recipients to comply with 42 C.F.R. Part 2, and HIPAA 45 C.F.R. parts 160 and 164. It includes patient notice of the refrain policy not to disclose without consent, and revocation rights. https://www.ncsacw.samhsa.gov/files/SI_ConsentForReleaseAndExchange.PDF
         */
        SQUAXIN, 
        /**
         * null
         */
        CAT5, 
        /**
         * LSP (National Exchange Point) requires that providers, hospitals and pharmacy obtain explicit permission [opt-in] from healthcare consumers to submit and retrieve all or only some of a subject of care’s health information collected by the LSP for purpose of treatment, which can be revoked.  Without permission, a provider cannot access LSP information even in an emergency. The LSP provides healthcare consumers with accountings of disclosures. https://www.vzvz.nl/uploaded/FILES/htmlcontent/Formulieren/TOESTEMMINGSFORMULIER.pdf, https://www.ikgeeftoestemming.nl/en, https://www.ikgeeftoestemming.nl/en/registration/find-healthcare-provider
         */
        NLLSP, 
        /**
         * Pursuant to Sec. 2 no. 9 Health Telematics Act 2012, ELGA Health Data ( “ELGA-Gesundheitsdaten”) = Medical documents. Austria opted for an opt-out approach. This means that a person is by default ‘ELGA participant’ unless he/she objects. ELGA participants have the following options: General opt out: No participation in ELGA, Partial opt-out: No participation in a particular ELGA application, e.g. eMedication and Case-specific opt-out: No participation in ELGA only regarding a particular case/treatment. There is the possibility to opt-in again. ELGA participants can also exclude the access of a particular ELGA healthcare provider to a particular piece of or all of their ELGA data. http://ec.europa.eu/health/ehealth/docs/laws_austria_en.pdf
         */
        ATELGA, 
        /**
         * null
         */
        CAT6, 
        /**
         * Guidance and template form https://privacyruleandresearch.nih.gov/pdf/authorization.pdf
         */
        NIHHIPAA, 
        /**
         * see http://ctep.cancer.gov/protocolDevelopment/docs/Informed_Consent_Template.docx
         */
        NCI, 
        /**
         * Global Rare Disease Patient Registry and Data Repository (GRDR) consent is an agreement of a healthcare consumer to permit collection, access, use and disclosure of de-identified rare disease information and collection of bio-specimens, medical information, family history and other related information from patients to permit the registry collection of health and genetic information, and specimens for pseudonymized disclosure for research purpose of use. https://rarediseases.info.nih.gov/files/informed_consent_template.pdf
         */
        NIHGRDR, 
        /**
         * VA Form 10-10116 Revocation of Authorization for Use and Release of Individually Identifiable Health Information for Veterans Health Administration Research. Note: VA Form 10-10116 is available @ http://www.northerncalifornia.va.gov/northerncalifornia/services/rnd/docs/vha-10-10116.pdf
         */
        VA1010116, 
        /**
         * NIH Authorization for the Release of Medical Information is a patient’s consent for the National Institutes of Health Clinical Center to release medical information to care providers, which can be revoked. Note: Consent Form available @ http://cc.nih.gov/participate/_pdf/NIH-527.pdf
         */
        NIH527, 
        /**
         * Global Alliance for Genomic Health Data Sharing Consent Form is an example of the GA4GH Population origins and ancestry research consent form. Consenters agree to permitting a specified research project to collect ancestry and genetic information in controlled-access databases, and to allow other researchers to use deidentified information from those databases. http://www.commonaccord.org/index.php?action=doc&file=Wx/org/genomicsandhealth/REWG/Demo/Roberta_Robinson_US
         */
        GA4GH, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ConsentCategory fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("cat1".equals(codeString))
          return CAT1;
        if ("advance-directive".equals(codeString))
          return ADVANCEDIRECTIVE;
        if ("cat2".equals(codeString))
          return CAT2;
        if ("medical-consent".equals(codeString))
          return MEDICALCONSENT;
        if ("cat3".equals(codeString))
          return CAT3;
        if ("hipaa".equals(codeString))
          return HIPAA;
        if ("SSA-827".equals(codeString))
          return SSA827;
        if ("cat4".equals(codeString))
          return CAT4;
        if ("DCH-3927".equals(codeString))
          return DCH3927;
        if ("squaxin".equals(codeString))
          return SQUAXIN;
        if ("cat5".equals(codeString))
          return CAT5;
        if ("nl-lsp".equals(codeString))
          return NLLSP;
        if ("at-elga".equals(codeString))
          return ATELGA;
        if ("cat6".equals(codeString))
          return CAT6;
        if ("nih-hipaa".equals(codeString))
          return NIHHIPAA;
        if ("nci".equals(codeString))
          return NCI;
        if ("nih-grdr".equals(codeString))
          return NIHGRDR;
        if ("va-10-10116".equals(codeString))
          return VA1010116;
        if ("nih-527".equals(codeString))
          return NIH527;
        if ("ga4gh".equals(codeString))
          return GA4GH;
        throw new FHIRException("Unknown ConsentCategory code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case CAT1: return "cat1";
            case ADVANCEDIRECTIVE: return "advance-directive";
            case CAT2: return "cat2";
            case MEDICALCONSENT: return "medical-consent";
            case CAT3: return "cat3";
            case HIPAA: return "hipaa";
            case SSA827: return "SSA-827";
            case CAT4: return "cat4";
            case DCH3927: return "DCH-3927";
            case SQUAXIN: return "squaxin";
            case CAT5: return "cat5";
            case NLLSP: return "nl-lsp";
            case ATELGA: return "at-elga";
            case CAT6: return "cat6";
            case NIHHIPAA: return "nih-hipaa";
            case NCI: return "nci";
            case NIHGRDR: return "nih-grdr";
            case VA1010116: return "va-10-10116";
            case NIH527: return "nih-527";
            case GA4GH: return "ga4gh";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/consentcategorycodes";
        }
        public String getDefinition() {
          switch (this) {
            case CAT1: return "";
            case ADVANCEDIRECTIVE: return "Any instructions, written or given verbally by a patient to a health care provider in anticipation of potential need for medical treatment";
            case CAT2: return "RWJ funded toolkit has several international example consent forms, and excellent overview of issues around medical informed consent";
            case MEDICALCONSENT: return "Informed consent is the process of communication between a patient and physician that results in the patient’s authorization or agreement to undergo a specific medical intervention [AMA 1998]. For both ethical and legal reasons, patients must be given enough information to be fully informed before deciding to undergo a major treatment, and this informed consent must be documented in writing.";
            case CAT3: return "";
            case HIPAA: return "HIPAA 45 CFR Section 164.508 Uses and disclosures for which an authorization is required. (a) Standard: Authorizations for uses and disclosures. (1) Authorization required: General rule. Except as otherwise permitted or required by this subchapter, a covered entity may not use or disclose protected health information without an authorization that is valid under this section. When a covered entity obtains or receives a valid authorization for its use or disclosure of protected health information, such use or disclosure must be consistent with such authorization. Usage Note: Authorizations governed under this regulation meet the definition of an opt in class of consent directive.";
            case SSA827: return "SA Form SSA-827 (Authorization to Disclose Information to the Social Security Administration (SSA)). Form is available at https://www.socialsecurity.gov/forms/ssa-827-inst-sp.pdf";
            case CAT4: return "May include federal and state jurisdictional privacy laws";
            case DCH3927: return "Michigan DCH-3927 Consent to Share Behavioral Health Information for Care Coordination Purposes, which combines 42 CFR Part 2 and Michigan Mental Health Code, Act 258 of 1974. Form is available at http://www.michigan.gov/documents/mdch/DCH-3927_Consent_to_Share_Health_Information_477005_7.docx";
            case SQUAXIN: return "Squaxin Indian HIPAA and 42 CFR Part 2 Consent for Release and Exchange of Confidential Information, which permits consenter to select healthcare record type and types of treatment purposes.  This consent requires disclosers and recipients to comply with 42 C.F.R. Part 2, and HIPAA 45 C.F.R. parts 160 and 164. It includes patient notice of the refrain policy not to disclose without consent, and revocation rights. https://www.ncsacw.samhsa.gov/files/SI_ConsentForReleaseAndExchange.PDF";
            case CAT5: return "";
            case NLLSP: return "LSP (National Exchange Point) requires that providers, hospitals and pharmacy obtain explicit permission [opt-in] from healthcare consumers to submit and retrieve all or only some of a subject of care’s health information collected by the LSP for purpose of treatment, which can be revoked.  Without permission, a provider cannot access LSP information even in an emergency. The LSP provides healthcare consumers with accountings of disclosures. https://www.vzvz.nl/uploaded/FILES/htmlcontent/Formulieren/TOESTEMMINGSFORMULIER.pdf, https://www.ikgeeftoestemming.nl/en, https://www.ikgeeftoestemming.nl/en/registration/find-healthcare-provider";
            case ATELGA: return "Pursuant to Sec. 2 no. 9 Health Telematics Act 2012, ELGA Health Data ( “ELGA-Gesundheitsdaten”) = Medical documents. Austria opted for an opt-out approach. This means that a person is by default ‘ELGA participant’ unless he/she objects. ELGA participants have the following options: General opt out: No participation in ELGA, Partial opt-out: No participation in a particular ELGA application, e.g. eMedication and Case-specific opt-out: No participation in ELGA only regarding a particular case/treatment. There is the possibility to opt-in again. ELGA participants can also exclude the access of a particular ELGA healthcare provider to a particular piece of or all of their ELGA data. http://ec.europa.eu/health/ehealth/docs/laws_austria_en.pdf";
            case CAT6: return "";
            case NIHHIPAA: return "Guidance and template form https://privacyruleandresearch.nih.gov/pdf/authorization.pdf";
            case NCI: return "see http://ctep.cancer.gov/protocolDevelopment/docs/Informed_Consent_Template.docx";
            case NIHGRDR: return "Global Rare Disease Patient Registry and Data Repository (GRDR) consent is an agreement of a healthcare consumer to permit collection, access, use and disclosure of de-identified rare disease information and collection of bio-specimens, medical information, family history and other related information from patients to permit the registry collection of health and genetic information, and specimens for pseudonymized disclosure for research purpose of use. https://rarediseases.info.nih.gov/files/informed_consent_template.pdf";
            case VA1010116: return "VA Form 10-10116 Revocation of Authorization for Use and Release of Individually Identifiable Health Information for Veterans Health Administration Research. Note: VA Form 10-10116 is available @ http://www.northerncalifornia.va.gov/northerncalifornia/services/rnd/docs/vha-10-10116.pdf";
            case NIH527: return "NIH Authorization for the Release of Medical Information is a patient’s consent for the National Institutes of Health Clinical Center to release medical information to care providers, which can be revoked. Note: Consent Form available @ http://cc.nih.gov/participate/_pdf/NIH-527.pdf";
            case GA4GH: return "Global Alliance for Genomic Health Data Sharing Consent Form is an example of the GA4GH Population origins and ancestry research consent form. Consenters agree to permitting a specified research project to collect ancestry and genetic information in controlled-access databases, and to allow other researchers to use deidentified information from those databases. http://www.commonaccord.org/index.php?action=doc&file=Wx/org/genomicsandhealth/REWG/Demo/Roberta_Robinson_US";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case CAT1: return "Advance Directive Consent examples";
            case ADVANCEDIRECTIVE: return "Advance Directive";
            case CAT2: return "Medical/Procedure Informed Consent";
            case MEDICALCONSENT: return "Medical Consent";
            case CAT3: return "Example of US jurisdictional [federal and state] privacy consent";
            case HIPAA: return "HIPAA Authorization";
            case SSA827: return "SSA Authorization to Disclose";
            case CAT4: return "US “Mixed” state HIE consent types";
            case DCH3927: return "Michigan behavior and mental health consent";
            case SQUAXIN: return "Squaxin Indian behavioral health and HIPAA consent";
            case CAT5: return "Example international health information exchange consent type";
            case NLLSP: return "NL LSP Permission";
            case ATELGA: return "AT ELGA Opt-in Consent";
            case CAT6: return "Examples of US Research Consent Types";
            case NIHHIPAA: return "HHS NIH HIPAA Research Authorization";
            case NCI: return "NCI Cancer Clinical Trial consent";
            case NIHGRDR: return "NIH Global Rare Disease Patient Registry and Data Repository consent";
            case VA1010116: return "VA Form 10-10116";
            case NIH527: return "NIH Authorization for the Release of Medical Information";
            case GA4GH: return "Population origins and ancestry research consent";
            default: return "?";
          }
    }


}

