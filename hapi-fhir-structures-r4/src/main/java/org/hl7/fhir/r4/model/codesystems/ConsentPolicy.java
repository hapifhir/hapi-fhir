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

// Generated on Thu, Dec 27, 2018 10:06-0500 for FHIR v4.0.0


import org.hl7.fhir.exceptions.FHIRException;

public enum ConsentPolicy {

        /**
         * 45 CFR part 46 §46.116 General requirements for informed consent; and §46.117 Documentation of informed consent. https://www.gpo.gov/fdsys/pkg/FR-2017-01-19/pdf/2017-01058.pdf
         */
        CRIC, 
        /**
         * The consent to the performance of a medical or surgical procedure by a physician licensed to practice medicine and surgery, a licensed advanced practice nurse, or a licensed physician assistant executed by a married person who is a minor, by a parent who is a minor, by a pregnant woman who is a minor, or by any person 18 years of age or older, is not voidable because of such minority, and, for such purpose, a married person who is a minor, a parent who is a minor, a pregnant woman who is a minor, or any person 18 years of age or older, is deemed to have the same legal capacity to act and has the same powers and obligations as has a person of legal age. Consent by Minors to Medical Procedures Act. (410 ILCS 210/0.01) (from Ch. 111, par. 4500) Sec. 0.01. Short title. This Act may be cited as the Consent by Minors to Medical Procedures Act. (Source: P.A. 86-1324.) http://www.ilga.gov/legislation/ilcs/ilcs3.asp?ActID=1539&ChapterID=35
         */
        ILLINOISMINORPROCEDURE, 
        /**
         * HIPAA 45 CFR Section 164.508 Uses and disclosures for which an authorization is required. (a) Standard: Authorizations for uses and disclosures. (1) Authorization required: General rule. Except as otherwise permitted or required by this subchapter, a covered entity SHALL not use or disclose protected health information without an authorization that is valid under this section. When a covered entity obtains or receives a valid authorization for its use or disclosure of protected health information, such use or disclosure must be consistent with such authorization. Usage Note: Authorizations governed under this regulation meet the definition of an opt in class of consent directive.
         */
        HIPAAAUTH, 
        /**
         * 164.520  Notice of privacy practices for protected health information. (1) Right to notice. Except as provided by paragraph (a)(2) or (3) of this section, an individual has a right to adequate notice of the uses and disclosures of protected health information that may be made by the covered entity, and of the individual's rights and the covered entity's legal duties with respect to protected health information. Usage Note: Restrictions governed under this regulation meet the definition of an implied with an opportunity to dissent class of consent directive.
         */
        HIPAANPP, 
        /**
         * HIPAA 45 CFR 164.510 - Uses and disclosures requiring an opportunity for the individual to agree or to object. A covered entity may use or disclose protected health information, provided that the individual is informed in advance of the use or disclosure and has the opportunity to agree to or prohibit or restrict the use or disclosure, in accordance with the applicable requirements of this section. The covered entity may orally inform the individual of and obtain the individual's oral agreement or objection to a use or disclosure permitted by this section. Usage Note: Restrictions governed under this regulation meet the definition of an opt out with exception class of consent directive.
         */
        HIPAARESTRICTIONS, 
        /**
         * HIPAA 45 CFR 164.508 - Uses and disclosures for which an authorization is required. (a) Standard: Authorizations for uses and disclosures. (3) Compound authorizations. An authorization for use or disclosure of protected health information SHALL NOT be combined with any other document to create a compound authorization, except as follows: (i) An authorization for the use or disclosure of protected health information for a research study may be combined with any other type of written permission for the same or another research study. This exception includes combining an authorization for the use or disclosure of protected health information for a research study with another authorization for the same research study, with an authorization for the creation or maintenance of a research database or repository, or with a consent to participate in research. Where a covered health care provider has conditioned the provision of research-related treatment on the provision of one of the authorizations, as permitted under paragraph (b)(4)(i) of this section, any compound authorization created under this paragraph must clearly differentiate between the conditioned and unconditioned components and provide the individual with an opportunity to opt in to the research activities described in the unconditioned authorization. Usage Notes: See HHS http://www.hhs.gov/hipaa/for-professionals/special-topics/research/index.html and OCR http://www.hhs.gov/hipaa/for-professionals/special-topics/research/index.html
         */
        HIPAARESEARCH, 
        /**
         * HIPAA 45 CFR 164.522(a) Right To Request a Restriction of Uses and Disclosures. (vi) A covered entity must agree to the request of an individual to restrict disclosure of protected health information about the individual to a health plan if: (A) The disclosure is for the purpose of carrying out payment or health care operations and is not otherwise required by law; and (B) The protected health information pertains solely to a health care item or service for which the individual, or person other than the health plan on behalf of the individual, has paid the covered entity in full. Usage Note: Restrictions governed under this regulation meet the definition of an opt out with exception class of consent directive. Opt out is limited to disclosures to a payer for payment and operations purpose of use. See HL7 HIPAA Self-Pay code in ActPrivacyLaw (2.16.840.1.113883.1.11.20426).
         */
        HIPAASELFPAY, 
        /**
         * On January 1, 2015, the Michigan Department of Health and Human Services (MDHHS) released a standard consent form for the sharing of health information specific to behavioral health and substance use treatment in accordance with Public Act 129 of 2014. In Michigan, while providers are not required to use this new standard form (MDHHS-5515), they are required to accept it. Note: Form is available at http://www.michigan.gov/documents/mdhhs/Consent_to_Share_Behavioral_Health_Information_for_Care_Coordination_Purposes_548835_7.docx For more information see http://www.michigan.gov/documents/mdhhs/Behavioral_Health_Consent_Form_Background_Information_548864_7.pdf
         */
        MDHHS5515, 
        /**
         * The New York State Surgical and Invasive Procedure Protocol (NYSSIPP) applies to all operative and invasive procedures including endoscopy, general surgery or interventional radiology. Other procedures that involve puncture or incision of the skin, or insertion of an instrument or foreign material into the body are within the scope of the protocol. This protocol also applies to those anesthesia procedures either prior to a surgical procedure or independent of a surgical procedure such as spinal facet blocks. Example: Certain 'minor' procedures such as venipuncture, peripheral IV placement, insertion of nasogastric tube and foley catheter insertion are not within the scope of the protocol. From http://www.health.ny.gov/professionals/protocols_and_guidelines/surgical_and_invasive_procedure/nyssipp_faq.htm Note: HHC 100B-1 Form is available at http://www.downstate.edu/emergency_medicine/documents/Consent_CT_with_contrast.pdf
         */
        NYSSIPP, 
        /**
         * VA Form 10-0484 Revocation for Release of Individually-Identifiable Health Information enables a veteran to revoke authorization for the VA to release specified copies of individually-identifiable health information with the non-VA health care provider organizations participating in the eHealth Exchange and partnering with VA. Comment: Opt-in Consent Directive with status = rescinded (aka 'revoked'). Note: Form is available at http://www.va.gov/vaforms/medical/pdf/vha-10-0484-fill.pdf
         */
        VA100484, 
        /**
         * VA Form 10-0485 Request for and Authorization to Release Protected Health Information to eHealth Exchange enables a veteran to request and authorize a VA health care facility to release protected health information (PHI) for treatment purposes only to the communities that are participating in the eHealth Exchange, VLER Directive, and other Health Information Exchanges with who VA has an agreement. This information may consist of the diagnosis of Sickle Cell Anemia, the treatment of or referral for Drug Abuse, treatment of or referral for Alcohol Abuse or the treatment of or testing for infection with Human Immunodeficiency Virus. This authorization covers the diagnoses that I may have upon signing of the authorization and the diagnoses that I may acquire in the future including those protected by 38 U.S.C. 7332. Comment: Opt-in Consent Directive. Note: Form is available at http://www.va.gov/vaforms/medical/pdf/10-0485-fill.pdf
         */
        VA100485, 
        /**
         * VA Form 10-5345 Request for and Authorization to Release Medical Records or Health Information enables a veteran to request and authorize the VA to release specified copies of protected health information (PHI), such as hospital summary or outpatient treatment notes, which may include information about conditions governed under Title 38 Section 7332 (drug abuse, alcoholism or alcohol abuse, testing for or infection with HIV, and sickle cell anemia). Comment: Opt-in Consent Directive. Note: Form is available at http://www.va.gov/vaforms/medical/pdf/vha-10-5345-fill.pdf
         */
        VA105345, 
        /**
         * VA Form 10-5345a Individuals' Request for a Copy of Their Own Health Information enables a veteran to request and authorize the VA to release specified copies of protected health information (PHI), such as hospital summary or outpatient treatment notes. Note: Form is available at http://www.va.gov/vaforms/medical/pdf/vha-10-5345a-fill.pdf
         */
        VA105345A, 
        /**
         * VA Form 10-5345a-MHV Individual's Request for a Copy of their own health information from MyHealtheVet enables a veteran to receive a copy of all available personal health information to be delivered through the veteran's My HealtheVet account. Note: Form is available at http://www.va.gov/vaforms/medical/pdf/vha-10-5345a-MHV-fill.pdf
         */
        VA105345AMHV, 
        /**
         * VA Form 10-10116 Revocation of Authorization for Use and Release of Individually Identifiable Health Information for Veterans Health Administration Research. Comment: Opt-in with Restriction Consent Directive with status = 'completed'. Note: Form is available at http://www.northerncalifornia.va.gov/northerncalifornia/services/rnd/docs/vha-10-10116.pdf 
         */
        VA1010116, 
        /**
         * VA Form 21-4142 (Authorization and Consent to Release Information to the Department of Veterans Affairs (VA) enables a veteran to authorize the US Veterans Administration [VA] to request veteran's health information from non-VA providers. Aka VA Compensation Application Note: Form is available at http://www.vba.va.gov/pubs/forms/VBA-21-4142-ARE.pdf . For additional information regarding VA Form 21-4142, refer to the following website: www.benefits.va.gov/compensation/consent_privateproviders
         */
        VA214142, 
        /**
         * SA Form SSA-827 (Authorization to Disclose Information to the Social Security Administration (SSA)). Form is available at https://www.socialsecurity.gov/forms/ssa-827-inst-sp.pdf
         */
        SSA827, 
        /**
         * Michigan DCH-3927 Consent to Share Behavioral Health Information for Care Coordination Purposes, which combines 42 CFR Part 2 and Michigan Mental Health Code, Act 258 of 1974. Form is available at http://www.michigan.gov/documents/mdch/DCH-3927_Consent_to_Share_Health_Information_477005_7.docx
         */
        DCH3927, 
        /**
         * Squaxin Indian HIPAA and 42 CFR Part 2 Consent for Release and Exchange of Confidential Information, which permits consenter to select healthcare record type and types of treatment purposes.  This consent requires disclosers and recipients to comply with 42 C.F.R. Part 2, and HIPAA 45 C.F.R. parts 160 and 164. It includes patient notice of the refrain policy not to disclose without consent, and revocation rights. https://www.ncsacw.samhsa.gov/files/SI_ConsentForReleaseAndExchange.PDF
         */
        SQUAXIN, 
        /**
         * LSP (National Exchange Point) requires that providers, hospitals and pharmacy obtain explicit permission [opt-in] from healthcare consumers to submit and retrieve all or only some of a subject of care’s health information collected by the LSP for purpose of treatment, which can be revoked.  Without permission, a provider cannot access LSP information even in an emergency. The LSP provides healthcare consumers with accountings of disclosures. https://www.vzvz.nl/uploaded/FILES/htmlcontent/Formulieren/TOESTEMMINGSFORMULIER.pdf, https://www.ikgeeftoestemming.nl/en, https://www.ikgeeftoestemming.nl/en/registration/find-healthcare-provider
         */
        NLLSP, 
        /**
         * Pursuant to Sec. 2 no. 9 Health Telematics Act 2012, ELGA Health Data ( “ELGA-Gesundheitsdaten”) = Medical documents. Austria opted for an opt-out approach. This means that a person is by default ‘ELGA participant’ unless he/she objects. ELGA participants have the following options: General opt out: No participation in ELGA, Partial opt-out: No participation in a particular ELGA application, e.g. eMedication and Case-specific opt-out: No participation in ELGA only regarding a particular case/treatment. There is the possibility to opt-in again. ELGA participants can also exclude the access of a particular ELGA healthcare provider to a particular piece of or all of their ELGA data. http://ec.europa.eu/health/ehealth/docs/laws_austria_en.pdf
         */
        ATELGA, 
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
        public static ConsentPolicy fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("cric".equals(codeString))
          return CRIC;
        if ("illinois-minor-procedure".equals(codeString))
          return ILLINOISMINORPROCEDURE;
        if ("hipaa-auth".equals(codeString))
          return HIPAAAUTH;
        if ("hipaa-npp".equals(codeString))
          return HIPAANPP;
        if ("hipaa-restrictions".equals(codeString))
          return HIPAARESTRICTIONS;
        if ("hipaa-research".equals(codeString))
          return HIPAARESEARCH;
        if ("hipaa-self-pay".equals(codeString))
          return HIPAASELFPAY;
        if ("mdhhs-5515".equals(codeString))
          return MDHHS5515;
        if ("nyssipp".equals(codeString))
          return NYSSIPP;
        if ("va-10-0484".equals(codeString))
          return VA100484;
        if ("va-10-0485".equals(codeString))
          return VA100485;
        if ("va-10-5345".equals(codeString))
          return VA105345;
        if ("va-10-5345a".equals(codeString))
          return VA105345A;
        if ("va-10-5345a-mhv".equals(codeString))
          return VA105345AMHV;
        if ("va-10-10116".equals(codeString))
          return VA1010116;
        if ("va-21-4142".equals(codeString))
          return VA214142;
        if ("ssa-827".equals(codeString))
          return SSA827;
        if ("dch-3927".equals(codeString))
          return DCH3927;
        if ("squaxin".equals(codeString))
          return SQUAXIN;
        if ("nl-lsp".equals(codeString))
          return NLLSP;
        if ("at-elga".equals(codeString))
          return ATELGA;
        if ("nih-hipaa".equals(codeString))
          return NIHHIPAA;
        if ("nci".equals(codeString))
          return NCI;
        if ("nih-grdr".equals(codeString))
          return NIHGRDR;
        if ("nih-527".equals(codeString))
          return NIH527;
        if ("ga4gh".equals(codeString))
          return GA4GH;
        throw new FHIRException("Unknown ConsentPolicy code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case CRIC: return "cric";
            case ILLINOISMINORPROCEDURE: return "illinois-minor-procedure";
            case HIPAAAUTH: return "hipaa-auth";
            case HIPAANPP: return "hipaa-npp";
            case HIPAARESTRICTIONS: return "hipaa-restrictions";
            case HIPAARESEARCH: return "hipaa-research";
            case HIPAASELFPAY: return "hipaa-self-pay";
            case MDHHS5515: return "mdhhs-5515";
            case NYSSIPP: return "nyssipp";
            case VA100484: return "va-10-0484";
            case VA100485: return "va-10-0485";
            case VA105345: return "va-10-5345";
            case VA105345A: return "va-10-5345a";
            case VA105345AMHV: return "va-10-5345a-mhv";
            case VA1010116: return "va-10-10116";
            case VA214142: return "va-21-4142";
            case SSA827: return "ssa-827";
            case DCH3927: return "dch-3927";
            case SQUAXIN: return "squaxin";
            case NLLSP: return "nl-lsp";
            case ATELGA: return "at-elga";
            case NIHHIPAA: return "nih-hipaa";
            case NCI: return "nci";
            case NIHGRDR: return "nih-grdr";
            case NIH527: return "nih-527";
            case GA4GH: return "ga4gh";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://terminology.hl7.org/CodeSystem/consentpolicycodes";
        }
        public String getDefinition() {
          switch (this) {
            case CRIC: return "45 CFR part 46 §46.116 General requirements for informed consent; and §46.117 Documentation of informed consent. https://www.gpo.gov/fdsys/pkg/FR-2017-01-19/pdf/2017-01058.pdf";
            case ILLINOISMINORPROCEDURE: return "The consent to the performance of a medical or surgical procedure by a physician licensed to practice medicine and surgery, a licensed advanced practice nurse, or a licensed physician assistant executed by a married person who is a minor, by a parent who is a minor, by a pregnant woman who is a minor, or by any person 18 years of age or older, is not voidable because of such minority, and, for such purpose, a married person who is a minor, a parent who is a minor, a pregnant woman who is a minor, or any person 18 years of age or older, is deemed to have the same legal capacity to act and has the same powers and obligations as has a person of legal age. Consent by Minors to Medical Procedures Act. (410 ILCS 210/0.01) (from Ch. 111, par. 4500) Sec. 0.01. Short title. This Act may be cited as the Consent by Minors to Medical Procedures Act. (Source: P.A. 86-1324.) http://www.ilga.gov/legislation/ilcs/ilcs3.asp?ActID=1539&ChapterID=35";
            case HIPAAAUTH: return "HIPAA 45 CFR Section 164.508 Uses and disclosures for which an authorization is required. (a) Standard: Authorizations for uses and disclosures. (1) Authorization required: General rule. Except as otherwise permitted or required by this subchapter, a covered entity SHALL not use or disclose protected health information without an authorization that is valid under this section. When a covered entity obtains or receives a valid authorization for its use or disclosure of protected health information, such use or disclosure must be consistent with such authorization. Usage Note: Authorizations governed under this regulation meet the definition of an opt in class of consent directive.";
            case HIPAANPP: return "164.520  Notice of privacy practices for protected health information. (1) Right to notice. Except as provided by paragraph (a)(2) or (3) of this section, an individual has a right to adequate notice of the uses and disclosures of protected health information that may be made by the covered entity, and of the individual's rights and the covered entity's legal duties with respect to protected health information. Usage Note: Restrictions governed under this regulation meet the definition of an implied with an opportunity to dissent class of consent directive.";
            case HIPAARESTRICTIONS: return "HIPAA 45 CFR 164.510 - Uses and disclosures requiring an opportunity for the individual to agree or to object. A covered entity may use or disclose protected health information, provided that the individual is informed in advance of the use or disclosure and has the opportunity to agree to or prohibit or restrict the use or disclosure, in accordance with the applicable requirements of this section. The covered entity may orally inform the individual of and obtain the individual's oral agreement or objection to a use or disclosure permitted by this section. Usage Note: Restrictions governed under this regulation meet the definition of an opt out with exception class of consent directive.";
            case HIPAARESEARCH: return "HIPAA 45 CFR 164.508 - Uses and disclosures for which an authorization is required. (a) Standard: Authorizations for uses and disclosures. (3) Compound authorizations. An authorization for use or disclosure of protected health information SHALL NOT be combined with any other document to create a compound authorization, except as follows: (i) An authorization for the use or disclosure of protected health information for a research study may be combined with any other type of written permission for the same or another research study. This exception includes combining an authorization for the use or disclosure of protected health information for a research study with another authorization for the same research study, with an authorization for the creation or maintenance of a research database or repository, or with a consent to participate in research. Where a covered health care provider has conditioned the provision of research-related treatment on the provision of one of the authorizations, as permitted under paragraph (b)(4)(i) of this section, any compound authorization created under this paragraph must clearly differentiate between the conditioned and unconditioned components and provide the individual with an opportunity to opt in to the research activities described in the unconditioned authorization. Usage Notes: See HHS http://www.hhs.gov/hipaa/for-professionals/special-topics/research/index.html and OCR http://www.hhs.gov/hipaa/for-professionals/special-topics/research/index.html";
            case HIPAASELFPAY: return "HIPAA 45 CFR 164.522(a) Right To Request a Restriction of Uses and Disclosures. (vi) A covered entity must agree to the request of an individual to restrict disclosure of protected health information about the individual to a health plan if: (A) The disclosure is for the purpose of carrying out payment or health care operations and is not otherwise required by law; and (B) The protected health information pertains solely to a health care item or service for which the individual, or person other than the health plan on behalf of the individual, has paid the covered entity in full. Usage Note: Restrictions governed under this regulation meet the definition of an opt out with exception class of consent directive. Opt out is limited to disclosures to a payer for payment and operations purpose of use. See HL7 HIPAA Self-Pay code in ActPrivacyLaw (2.16.840.1.113883.1.11.20426).";
            case MDHHS5515: return "On January 1, 2015, the Michigan Department of Health and Human Services (MDHHS) released a standard consent form for the sharing of health information specific to behavioral health and substance use treatment in accordance with Public Act 129 of 2014. In Michigan, while providers are not required to use this new standard form (MDHHS-5515), they are required to accept it. Note: Form is available at http://www.michigan.gov/documents/mdhhs/Consent_to_Share_Behavioral_Health_Information_for_Care_Coordination_Purposes_548835_7.docx For more information see http://www.michigan.gov/documents/mdhhs/Behavioral_Health_Consent_Form_Background_Information_548864_7.pdf";
            case NYSSIPP: return "The New York State Surgical and Invasive Procedure Protocol (NYSSIPP) applies to all operative and invasive procedures including endoscopy, general surgery or interventional radiology. Other procedures that involve puncture or incision of the skin, or insertion of an instrument or foreign material into the body are within the scope of the protocol. This protocol also applies to those anesthesia procedures either prior to a surgical procedure or independent of a surgical procedure such as spinal facet blocks. Example: Certain 'minor' procedures such as venipuncture, peripheral IV placement, insertion of nasogastric tube and foley catheter insertion are not within the scope of the protocol. From http://www.health.ny.gov/professionals/protocols_and_guidelines/surgical_and_invasive_procedure/nyssipp_faq.htm Note: HHC 100B-1 Form is available at http://www.downstate.edu/emergency_medicine/documents/Consent_CT_with_contrast.pdf";
            case VA100484: return "VA Form 10-0484 Revocation for Release of Individually-Identifiable Health Information enables a veteran to revoke authorization for the VA to release specified copies of individually-identifiable health information with the non-VA health care provider organizations participating in the eHealth Exchange and partnering with VA. Comment: Opt-in Consent Directive with status = rescinded (aka 'revoked'). Note: Form is available at http://www.va.gov/vaforms/medical/pdf/vha-10-0484-fill.pdf";
            case VA100485: return "VA Form 10-0485 Request for and Authorization to Release Protected Health Information to eHealth Exchange enables a veteran to request and authorize a VA health care facility to release protected health information (PHI) for treatment purposes only to the communities that are participating in the eHealth Exchange, VLER Directive, and other Health Information Exchanges with who VA has an agreement. This information may consist of the diagnosis of Sickle Cell Anemia, the treatment of or referral for Drug Abuse, treatment of or referral for Alcohol Abuse or the treatment of or testing for infection with Human Immunodeficiency Virus. This authorization covers the diagnoses that I may have upon signing of the authorization and the diagnoses that I may acquire in the future including those protected by 38 U.S.C. 7332. Comment: Opt-in Consent Directive. Note: Form is available at http://www.va.gov/vaforms/medical/pdf/10-0485-fill.pdf";
            case VA105345: return "VA Form 10-5345 Request for and Authorization to Release Medical Records or Health Information enables a veteran to request and authorize the VA to release specified copies of protected health information (PHI), such as hospital summary or outpatient treatment notes, which may include information about conditions governed under Title 38 Section 7332 (drug abuse, alcoholism or alcohol abuse, testing for or infection with HIV, and sickle cell anemia). Comment: Opt-in Consent Directive. Note: Form is available at http://www.va.gov/vaforms/medical/pdf/vha-10-5345-fill.pdf";
            case VA105345A: return "VA Form 10-5345a Individuals' Request for a Copy of Their Own Health Information enables a veteran to request and authorize the VA to release specified copies of protected health information (PHI), such as hospital summary or outpatient treatment notes. Note: Form is available at http://www.va.gov/vaforms/medical/pdf/vha-10-5345a-fill.pdf";
            case VA105345AMHV: return "VA Form 10-5345a-MHV Individual's Request for a Copy of their own health information from MyHealtheVet enables a veteran to receive a copy of all available personal health information to be delivered through the veteran's My HealtheVet account. Note: Form is available at http://www.va.gov/vaforms/medical/pdf/vha-10-5345a-MHV-fill.pdf";
            case VA1010116: return "VA Form 10-10116 Revocation of Authorization for Use and Release of Individually Identifiable Health Information for Veterans Health Administration Research. Comment: Opt-in with Restriction Consent Directive with status = 'completed'. Note: Form is available at http://www.northerncalifornia.va.gov/northerncalifornia/services/rnd/docs/vha-10-10116.pdf ";
            case VA214142: return "VA Form 21-4142 (Authorization and Consent to Release Information to the Department of Veterans Affairs (VA) enables a veteran to authorize the US Veterans Administration [VA] to request veteran's health information from non-VA providers. Aka VA Compensation Application Note: Form is available at http://www.vba.va.gov/pubs/forms/VBA-21-4142-ARE.pdf . For additional information regarding VA Form 21-4142, refer to the following website: www.benefits.va.gov/compensation/consent_privateproviders";
            case SSA827: return "SA Form SSA-827 (Authorization to Disclose Information to the Social Security Administration (SSA)). Form is available at https://www.socialsecurity.gov/forms/ssa-827-inst-sp.pdf";
            case DCH3927: return "Michigan DCH-3927 Consent to Share Behavioral Health Information for Care Coordination Purposes, which combines 42 CFR Part 2 and Michigan Mental Health Code, Act 258 of 1974. Form is available at http://www.michigan.gov/documents/mdch/DCH-3927_Consent_to_Share_Health_Information_477005_7.docx";
            case SQUAXIN: return "Squaxin Indian HIPAA and 42 CFR Part 2 Consent for Release and Exchange of Confidential Information, which permits consenter to select healthcare record type and types of treatment purposes.  This consent requires disclosers and recipients to comply with 42 C.F.R. Part 2, and HIPAA 45 C.F.R. parts 160 and 164. It includes patient notice of the refrain policy not to disclose without consent, and revocation rights. https://www.ncsacw.samhsa.gov/files/SI_ConsentForReleaseAndExchange.PDF";
            case NLLSP: return "LSP (National Exchange Point) requires that providers, hospitals and pharmacy obtain explicit permission [opt-in] from healthcare consumers to submit and retrieve all or only some of a subject of care’s health information collected by the LSP for purpose of treatment, which can be revoked.  Without permission, a provider cannot access LSP information even in an emergency. The LSP provides healthcare consumers with accountings of disclosures. https://www.vzvz.nl/uploaded/FILES/htmlcontent/Formulieren/TOESTEMMINGSFORMULIER.pdf, https://www.ikgeeftoestemming.nl/en, https://www.ikgeeftoestemming.nl/en/registration/find-healthcare-provider";
            case ATELGA: return "Pursuant to Sec. 2 no. 9 Health Telematics Act 2012, ELGA Health Data ( “ELGA-Gesundheitsdaten”) = Medical documents. Austria opted for an opt-out approach. This means that a person is by default ‘ELGA participant’ unless he/she objects. ELGA participants have the following options: General opt out: No participation in ELGA, Partial opt-out: No participation in a particular ELGA application, e.g. eMedication and Case-specific opt-out: No participation in ELGA only regarding a particular case/treatment. There is the possibility to opt-in again. ELGA participants can also exclude the access of a particular ELGA healthcare provider to a particular piece of or all of their ELGA data. http://ec.europa.eu/health/ehealth/docs/laws_austria_en.pdf";
            case NIHHIPAA: return "Guidance and template form https://privacyruleandresearch.nih.gov/pdf/authorization.pdf";
            case NCI: return "see http://ctep.cancer.gov/protocolDevelopment/docs/Informed_Consent_Template.docx";
            case NIHGRDR: return "Global Rare Disease Patient Registry and Data Repository (GRDR) consent is an agreement of a healthcare consumer to permit collection, access, use and disclosure of de-identified rare disease information and collection of bio-specimens, medical information, family history and other related information from patients to permit the registry collection of health and genetic information, and specimens for pseudonymized disclosure for research purpose of use. https://rarediseases.info.nih.gov/files/informed_consent_template.pdf";
            case NIH527: return "NIH Authorization for the Release of Medical Information is a patient’s consent for the National Institutes of Health Clinical Center to release medical information to care providers, which can be revoked. Note: Consent Form available @ http://cc.nih.gov/participate/_pdf/NIH-527.pdf";
            case GA4GH: return "Global Alliance for Genomic Health Data Sharing Consent Form is an example of the GA4GH Population origins and ancestry research consent form. Consenters agree to permitting a specified research project to collect ancestry and genetic information in controlled-access databases, and to allow other researchers to use deidentified information from those databases. http://www.commonaccord.org/index.php?action=doc&file=Wx/org/genomicsandhealth/REWG/Demo/Roberta_Robinson_US";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case CRIC: return "Common Rule Informed Consent";
            case ILLINOISMINORPROCEDURE: return "Illinois Consent by Minors to Medical Procedures";
            case HIPAAAUTH: return "HIPAA Authorization";
            case HIPAANPP: return "HIPAA Notice of Privacy Practices";
            case HIPAARESTRICTIONS: return "HIPAA Restrictions";
            case HIPAARESEARCH: return "HIPAA Research Authorization";
            case HIPAASELFPAY: return "HIPAA Self-Pay Restriction";
            case MDHHS5515: return "Michigan MDHHS-5515 Consent to Share Behavioral Health Information for Care Coordination Purposes";
            case NYSSIPP: return "New York State Surgical and Invasive Procedure Protocol";
            case VA100484: return "VA Form 10-0484";
            case VA100485: return "VA Form 10-0485";
            case VA105345: return "VA Form 10-5345";
            case VA105345A: return "VA Form 10-5345a";
            case VA105345AMHV: return "VA Form 10-5345a-MHV";
            case VA1010116: return "VA Form 10-10-10116";
            case VA214142: return "VA Form 21-4142";
            case SSA827: return "SSA Authorization to Disclose";
            case DCH3927: return "Michigan behavior and mental health consent";
            case SQUAXIN: return "Squaxin Indian behavioral health and HIPAA consent";
            case NLLSP: return "NL LSP Permission";
            case ATELGA: return "AT ELGA Opt-in Consent";
            case NIHHIPAA: return "HHS NIH HIPAA Research Authorization";
            case NCI: return "NCI Cancer Clinical Trial consent";
            case NIHGRDR: return "NIH Global Rare Disease Patient Registry and Data Repository consent";
            case NIH527: return "NIH Authorization for the Release of Medical Information";
            case GA4GH: return "Population origins and ancestry research consent";
            default: return "?";
          }
    }


}

