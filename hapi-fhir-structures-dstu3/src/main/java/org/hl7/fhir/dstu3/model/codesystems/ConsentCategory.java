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

public enum ConsentCategory {

        /**
         * Required elements in a written consent to a disclosure of information governed under 42 CFR Part 2. http://www.ecfr.gov/cgi-bin/text-idx?SID=69c4339acd2df9fab9dcbed15181917b&mc=true&node=pt42.1.2&rgn=div5
         */
        _42CFR2, 
        /**
         * Any instructions, written or given verbally by a patient to a health care provider in anticipation of potential need for medical treatment. [2005 Honor My Wishes]
         */
        ACD, 
        /**
         * 45 CFR part 46 Â§46.116 General requirements for informed consent; and Â§46.117 Documentation of informed consent. https://www.gpo.gov/fdsys/pkg/FR-2017-01-19/pdf/2017-01058.pdf
         */
        CRIC, 
        /**
         * A legal document, signed by both the patient and their provider, stating a desire not to have CPR initiated in case of a cardiac event. Note: This form was replaced in 2003 with the Physician Orders for Life-Sustaining Treatment [POLST].
         */
        DNR, 
        /**
         * Opt-in to disclosure of health information for emergency only consent directive. Comment: This general consent directive specifically limits disclosure of health information for purpose of emergency treatment. Additional parameters may further limit the disclosure to specific users, roles, duration, types of information, and impose uses obligations. [ActConsentDirective (2.16.840.1.113883.1.11.20425)]
         */
        EMRGONLY, 
        /**
         * The consent to the performance of a medical or surgical procedure by a physician licensed to practice medicine and surgery, a licensed advanced practice nurse, or a licensed physician assistant executed by a married person who is a minor, by a parent who is a minor, by a pregnant woman who is a minor, or by any person 18 years of age or older, is not voidable because of such minority, and, for such purpose, a married person who is a minor, a parent who is a minor, a pregnant woman who is a minor, or any person 18 years of age or older, is deemed to have the same legal capacity to act and has the same powers and obligations as has a person of legal age. Consent by Minors to Medical Procedures Act. (410 ILCS 210/0.01) (from Ch. 111, par. 4500) Sec. 0.01. Short title. This Act may be cited as the Consent by Minors to Medical Procedures Act. (Source: P.A. 86-1324.) http://www.ilga.gov/legislation/ilcs/ilcs3.asp?ActID=1539&ChapterID=35
         */
        ILLINOISMINORPROCEDURE, 
        /**
         * Patientâ€™s document telling patientâ€™s health care provider what the patient wants or does not want if the patient is diagnosed as being terminally ill and in a persistent vegetative state or in a permanently unconscious condition.[2005 Honor My Wishes]
         */
        HCD, 
        /**
         * HIPAA 45 CFR Section 164.508 Uses and disclosures for which an authorization is required. (a) Standard: Authorizations for uses and disclosures. (1) Authorization required: General rule. Except as otherwise permitted or required by this subchapter, a covered entity may not use or disclose protected health information without an authorization that is valid under this section. When a covered entity obtains or receives a valid authorization for its use or disclosure of protected health information, such use or disclosure must be consistent with such authorization. Usage Note: Authorizations governed under this regulation meet the definition of an opt in class of consent directive.
         */
        HIPAAAUTH, 
        /**
         * Â§ 164.520 â€” Notice of privacy practices for protected health information. (1) Right to notice. Except as provided by paragraph (a)(2) or (3) of this section, an individual has a right to adequate notice of the uses and disclosures of protected health information that may be made by the covered entity, and of the individual's rights and the covered entity's legal duties with respect to protected health information. Usage Note: Restrictions governed under this regulation meet the definition of an implied with an opportunity to dissent class of consent directive.
         */
        HIPAANPP, 
        /**
         * HIPAA 45 CFR Â§ 164.510 - Uses and disclosures requiring an opportunity for the individual to agree or to object. A covered entity may use or disclose protected health information, provided that the individual is informed in advance of the use or disclosure and has the opportunity to agree to or prohibit or restrict the use or disclosure, in accordance with the applicable requirements of this section. The covered entity may orally inform the individual of and obtain the individual's oral agreement or objection to a use or disclosure permitted by this section. Usage Note: Restrictions governed under this regulation meet the definition of an opt out with exception class of consent directive.
         */
        HIPAARESTRICTIONS, 
        /**
         * HIPAA 45 CFR Â§ 164.508 - Uses and disclosures for which an authorization is required. (a) Standard: Authorizations for uses and disclosures. (3) Compound authorizations. An authorization for use or disclosure of protected health information may not be combined with any other document to create a compound authorization, except as follows: (i) An authorization for the use or disclosure of protected health information for a research study may be combined with any other type of written permission for the same or another research study. This exception includes combining an authorization for the use or disclosure of protected health information for a research study with another authorization for the same research study, with an authorization for the creation or maintenance of a research database or repository, or with a consent to participate in research. Where a covered health care provider has conditioned the provision of research-related treatment on the provision of one of the authorizations, as permitted under paragraph (b)(4)(i) of this section, any compound authorization created under this paragraph must clearly differentiate between the conditioned and unconditioned components and provide the individual with an opportunity to opt in to the research activities described in the unconditioned authorization. Usage Notes: See HHS http://www.hhs.gov/hipaa/for-professionals/special-topics/research/index.html and OCR http://www.hhs.gov/hipaa/for-professionals/special-topics/research/index.html
         */
        HIPAARESEARCH, 
        /**
         * HIPAA 45 CFR Â§ 164.522(a)â€”Right To Request a Restriction of Uses and Disclosures. (vi) A covered entity must agree to the request of an individual to restrict disclosure of protected health information about the individual to a health plan if: (A) The disclosure is for the purpose of carrying out payment or health care operations and is not otherwise required by law; and (B) The protected health information pertains solely to a health care item or service for which the individual, or person other than the health plan on behalf of the individual, has paid the covered entity in full. Usage Note: Restrictions governed under this regulation meet the definition of an opt out with exception class of consent directive. Opt out is limited to disclosures to a payer for payment and operations purpose of use. See HL7 HIPAA Self-Pay code in ActPrivacyLaw (2.16.840.1.113883.1.11.20426).
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
         * Acknowledgement of custodian notice of privacy practices. Usage Notes: This type of consent directive acknowledges a custodian's notice of privacy practices including its permitted collection, access, use and disclosure of health information to users and for purposes of use specified. [ActConsentDirective (2.16.840.1.113883.1.11.20425)]
         */
        NPP, 
        /**
         * The Physician Order for Life-Sustaining Treatment form records a personâ€™s health care wishes for end of life emergency treatment and translates them into an order by the physician. It must be reviewed and signed by both the patient and the physician, Advanced Registered Nurse Practitioner or Physician Assistant. [2005 Honor My Wishes] Comment: Opt-in Consent Directive with restrictions.
         */
        POLST, 
        /**
         * Consent to have healthcare information in an electronic health record accessed for research purposes. [VALUE SET: ActConsentType (2.16.840.1.113883.1.11.19897)]
         */
        RESEARCH, 
        /**
         * Consent to have de-identified healthcare information in an electronic health record that is accessed for research purposes, but without consent to re-identify the information under any circumstance. [VALUE SET: ActConsentType (2.16.840.1.113883.1.11.19897)
         */
        RSDID, 
        /**
         * Consent to have de-identified healthcare information in an electronic health record that is accessed for research purposes re-identified under specific circumstances outlined in the consent. [VALUE SET: ActConsentType (2.16.840.1.113883.1.11.19897)]
         */
        RSREID, 
        /**
         * SSA Form SSA-827 (Authorization to Disclose Information to the Social Security Administration (SSA))and its affiliated State disability determination services use Form SSA-827, Authorization to Disclose Information to the Social Security Administration (SSA) to obtain medical and other information needed to determine whether or not a claimant is disabled. Comment: Opt-in Consent Directive. Note: Form is available at https://www.socialsecurity.gov/forms/ssa-827-inst-sp.pdf 
         */
        SSA827, 
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
         * VA Form 10-5345a-MHV Individualâ€™s Request for a Copy of their own health information from MyHealtheVet enables a veteran to receive a copy of all available personal health information to be delivered through the veteranâ€™s My HealtheVet account. Note: Form is available at http://www.va.gov/vaforms/medical/pdf/vha-10-5345a-MHV-fill.pdf
         */
        VA105345AMHV, 
        /**
         * VA Form 10-10116 Revocation of Authorization for Use and Release of Individually Identifiable Health Information for Veterans Health Administration Research. Comment: Opt-in with Restriction Consent Directive with status = 'completed'. Note: Form is available at http://www.northerncalifornia.va.gov/northerncalifornia/services/rnd/docs/vha-10-10116.pdf 
         */
        VA1010116, 
        /**
         * VA Form 21-4142 (Authorization and Consent to Release Information to the Department of Veterans Affairs (VA) enables a veteran to authorize the US Veterans Administration [VA] to request veteranâ€™s health information from non-VA providers. Aka VA Compensation Application Note: Form is available at http://www.vba.va.gov/pubs/forms/VBA-21-4142-ARE.pdf . For additional information regarding VA Form 21-4142, refer to the following website: www.benefits.va.gov/compensation/consent_privateproviders
         */
        VA214142, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ConsentCategory fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("42-CFR-2".equals(codeString))
          return _42CFR2;
        if ("ACD".equals(codeString))
          return ACD;
        if ("CRIC".equals(codeString))
          return CRIC;
        if ("DNR".equals(codeString))
          return DNR;
        if ("EMRGONLY".equals(codeString))
          return EMRGONLY;
        if ("Illinois-Minor-Procedure".equals(codeString))
          return ILLINOISMINORPROCEDURE;
        if ("HCD".equals(codeString))
          return HCD;
        if ("HIPAA-Auth".equals(codeString))
          return HIPAAAUTH;
        if ("HIPAA-NPP".equals(codeString))
          return HIPAANPP;
        if ("HIPAA-Restrictions".equals(codeString))
          return HIPAARESTRICTIONS;
        if ("HIPAA-Research".equals(codeString))
          return HIPAARESEARCH;
        if ("HIPAA-Self-Pay".equals(codeString))
          return HIPAASELFPAY;
        if ("MDHHS-5515".equals(codeString))
          return MDHHS5515;
        if ("NYSSIPP".equals(codeString))
          return NYSSIPP;
        if ("NPP".equals(codeString))
          return NPP;
        if ("POLST".equals(codeString))
          return POLST;
        if ("RESEARCH".equals(codeString))
          return RESEARCH;
        if ("RSDID".equals(codeString))
          return RSDID;
        if ("RSREID".equals(codeString))
          return RSREID;
        if ("SSA-827".equals(codeString))
          return SSA827;
        if ("VA-10-0484".equals(codeString))
          return VA100484;
        if ("VA-10-0485".equals(codeString))
          return VA100485;
        if ("VA-10-5345".equals(codeString))
          return VA105345;
        if ("VA-10-5345a".equals(codeString))
          return VA105345A;
        if ("VA-10-5345a-MHV".equals(codeString))
          return VA105345AMHV;
        if ("VA-10-10116".equals(codeString))
          return VA1010116;
        if ("VA-21-4142".equals(codeString))
          return VA214142;
        throw new FHIRException("Unknown ConsentCategory code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case _42CFR2: return "42-CFR-2";
            case ACD: return "ACD";
            case CRIC: return "CRIC";
            case DNR: return "DNR";
            case EMRGONLY: return "EMRGONLY";
            case ILLINOISMINORPROCEDURE: return "Illinois-Minor-Procedure";
            case HCD: return "HCD";
            case HIPAAAUTH: return "HIPAA-Auth";
            case HIPAANPP: return "HIPAA-NPP";
            case HIPAARESTRICTIONS: return "HIPAA-Restrictions";
            case HIPAARESEARCH: return "HIPAA-Research";
            case HIPAASELFPAY: return "HIPAA-Self-Pay";
            case MDHHS5515: return "MDHHS-5515";
            case NYSSIPP: return "NYSSIPP";
            case NPP: return "NPP";
            case POLST: return "POLST";
            case RESEARCH: return "RESEARCH";
            case RSDID: return "RSDID";
            case RSREID: return "RSREID";
            case SSA827: return "SSA-827";
            case VA100484: return "VA-10-0484";
            case VA100485: return "VA-10-0485";
            case VA105345: return "VA-10-5345";
            case VA105345A: return "VA-10-5345a";
            case VA105345AMHV: return "VA-10-5345a-MHV";
            case VA1010116: return "VA-10-10116";
            case VA214142: return "VA-21-4142";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/consentcategorycodes";
        }
        public String getDefinition() {
          switch (this) {
            case _42CFR2: return "Required elements in a written consent to a disclosure of information governed under 42 CFR Part 2. http://www.ecfr.gov/cgi-bin/text-idx?SID=69c4339acd2df9fab9dcbed15181917b&mc=true&node=pt42.1.2&rgn=div5";
            case ACD: return "Any instructions, written or given verbally by a patient to a health care provider in anticipation of potential need for medical treatment. [2005 Honor My Wishes]";
            case CRIC: return "45 CFR part 46 Â§46.116 General requirements for informed consent; and Â§46.117 Documentation of informed consent. https://www.gpo.gov/fdsys/pkg/FR-2017-01-19/pdf/2017-01058.pdf";
            case DNR: return "A legal document, signed by both the patient and their provider, stating a desire not to have CPR initiated in case of a cardiac event. Note: This form was replaced in 2003 with the Physician Orders for Life-Sustaining Treatment [POLST].";
            case EMRGONLY: return "Opt-in to disclosure of health information for emergency only consent directive. Comment: This general consent directive specifically limits disclosure of health information for purpose of emergency treatment. Additional parameters may further limit the disclosure to specific users, roles, duration, types of information, and impose uses obligations. [ActConsentDirective (2.16.840.1.113883.1.11.20425)]";
            case ILLINOISMINORPROCEDURE: return "The consent to the performance of a medical or surgical procedure by a physician licensed to practice medicine and surgery, a licensed advanced practice nurse, or a licensed physician assistant executed by a married person who is a minor, by a parent who is a minor, by a pregnant woman who is a minor, or by any person 18 years of age or older, is not voidable because of such minority, and, for such purpose, a married person who is a minor, a parent who is a minor, a pregnant woman who is a minor, or any person 18 years of age or older, is deemed to have the same legal capacity to act and has the same powers and obligations as has a person of legal age. Consent by Minors to Medical Procedures Act. (410 ILCS 210/0.01) (from Ch. 111, par. 4500) Sec. 0.01. Short title. This Act may be cited as the Consent by Minors to Medical Procedures Act. (Source: P.A. 86-1324.) http://www.ilga.gov/legislation/ilcs/ilcs3.asp?ActID=1539&ChapterID=35";
            case HCD: return "Patientâ€™s document telling patientâ€™s health care provider what the patient wants or does not want if the patient is diagnosed as being terminally ill and in a persistent vegetative state or in a permanently unconscious condition.[2005 Honor My Wishes]";
            case HIPAAAUTH: return "HIPAA 45 CFR Section 164.508 Uses and disclosures for which an authorization is required. (a) Standard: Authorizations for uses and disclosures. (1) Authorization required: General rule. Except as otherwise permitted or required by this subchapter, a covered entity may not use or disclose protected health information without an authorization that is valid under this section. When a covered entity obtains or receives a valid authorization for its use or disclosure of protected health information, such use or disclosure must be consistent with such authorization. Usage Note: Authorizations governed under this regulation meet the definition of an opt in class of consent directive.";
            case HIPAANPP: return "Â§ 164.520 â€” Notice of privacy practices for protected health information. (1) Right to notice. Except as provided by paragraph (a)(2) or (3) of this section, an individual has a right to adequate notice of the uses and disclosures of protected health information that may be made by the covered entity, and of the individual's rights and the covered entity's legal duties with respect to protected health information. Usage Note: Restrictions governed under this regulation meet the definition of an implied with an opportunity to dissent class of consent directive.";
            case HIPAARESTRICTIONS: return "HIPAA 45 CFR Â§ 164.510 - Uses and disclosures requiring an opportunity for the individual to agree or to object. A covered entity may use or disclose protected health information, provided that the individual is informed in advance of the use or disclosure and has the opportunity to agree to or prohibit or restrict the use or disclosure, in accordance with the applicable requirements of this section. The covered entity may orally inform the individual of and obtain the individual's oral agreement or objection to a use or disclosure permitted by this section. Usage Note: Restrictions governed under this regulation meet the definition of an opt out with exception class of consent directive.";
            case HIPAARESEARCH: return "HIPAA 45 CFR Â§ 164.508 - Uses and disclosures for which an authorization is required. (a) Standard: Authorizations for uses and disclosures. (3) Compound authorizations. An authorization for use or disclosure of protected health information may not be combined with any other document to create a compound authorization, except as follows: (i) An authorization for the use or disclosure of protected health information for a research study may be combined with any other type of written permission for the same or another research study. This exception includes combining an authorization for the use or disclosure of protected health information for a research study with another authorization for the same research study, with an authorization for the creation or maintenance of a research database or repository, or with a consent to participate in research. Where a covered health care provider has conditioned the provision of research-related treatment on the provision of one of the authorizations, as permitted under paragraph (b)(4)(i) of this section, any compound authorization created under this paragraph must clearly differentiate between the conditioned and unconditioned components and provide the individual with an opportunity to opt in to the research activities described in the unconditioned authorization. Usage Notes: See HHS http://www.hhs.gov/hipaa/for-professionals/special-topics/research/index.html and OCR http://www.hhs.gov/hipaa/for-professionals/special-topics/research/index.html";
            case HIPAASELFPAY: return "HIPAA 45 CFR Â§ 164.522(a)â€”Right To Request a Restriction of Uses and Disclosures. (vi) A covered entity must agree to the request of an individual to restrict disclosure of protected health information about the individual to a health plan if: (A) The disclosure is for the purpose of carrying out payment or health care operations and is not otherwise required by law; and (B) The protected health information pertains solely to a health care item or service for which the individual, or person other than the health plan on behalf of the individual, has paid the covered entity in full. Usage Note: Restrictions governed under this regulation meet the definition of an opt out with exception class of consent directive. Opt out is limited to disclosures to a payer for payment and operations purpose of use. See HL7 HIPAA Self-Pay code in ActPrivacyLaw (2.16.840.1.113883.1.11.20426).";
            case MDHHS5515: return "On January 1, 2015, the Michigan Department of Health and Human Services (MDHHS) released a standard consent form for the sharing of health information specific to behavioral health and substance use treatment in accordance with Public Act 129 of 2014. In Michigan, while providers are not required to use this new standard form (MDHHS-5515), they are required to accept it. Note: Form is available at http://www.michigan.gov/documents/mdhhs/Consent_to_Share_Behavioral_Health_Information_for_Care_Coordination_Purposes_548835_7.docx For more information see http://www.michigan.gov/documents/mdhhs/Behavioral_Health_Consent_Form_Background_Information_548864_7.pdf";
            case NYSSIPP: return "The New York State Surgical and Invasive Procedure Protocol (NYSSIPP) applies to all operative and invasive procedures including endoscopy, general surgery or interventional radiology. Other procedures that involve puncture or incision of the skin, or insertion of an instrument or foreign material into the body are within the scope of the protocol. This protocol also applies to those anesthesia procedures either prior to a surgical procedure or independent of a surgical procedure such as spinal facet blocks. Example: Certain 'minor' procedures such as venipuncture, peripheral IV placement, insertion of nasogastric tube and foley catheter insertion are not within the scope of the protocol. From http://www.health.ny.gov/professionals/protocols_and_guidelines/surgical_and_invasive_procedure/nyssipp_faq.htm Note: HHC 100B-1 Form is available at http://www.downstate.edu/emergency_medicine/documents/Consent_CT_with_contrast.pdf";
            case NPP: return "Acknowledgement of custodian notice of privacy practices. Usage Notes: This type of consent directive acknowledges a custodian's notice of privacy practices including its permitted collection, access, use and disclosure of health information to users and for purposes of use specified. [ActConsentDirective (2.16.840.1.113883.1.11.20425)]";
            case POLST: return "The Physician Order for Life-Sustaining Treatment form records a personâ€™s health care wishes for end of life emergency treatment and translates them into an order by the physician. It must be reviewed and signed by both the patient and the physician, Advanced Registered Nurse Practitioner or Physician Assistant. [2005 Honor My Wishes] Comment: Opt-in Consent Directive with restrictions.";
            case RESEARCH: return "Consent to have healthcare information in an electronic health record accessed for research purposes. [VALUE SET: ActConsentType (2.16.840.1.113883.1.11.19897)]";
            case RSDID: return "Consent to have de-identified healthcare information in an electronic health record that is accessed for research purposes, but without consent to re-identify the information under any circumstance. [VALUE SET: ActConsentType (2.16.840.1.113883.1.11.19897)";
            case RSREID: return "Consent to have de-identified healthcare information in an electronic health record that is accessed for research purposes re-identified under specific circumstances outlined in the consent. [VALUE SET: ActConsentType (2.16.840.1.113883.1.11.19897)]";
            case SSA827: return "SSA Form SSA-827 (Authorization to Disclose Information to the Social Security Administration (SSA))and its affiliated State disability determination services use Form SSA-827, Authorization to Disclose Information to the Social Security Administration (SSA) to obtain medical and other information needed to determine whether or not a claimant is disabled. Comment: Opt-in Consent Directive. Note: Form is available at https://www.socialsecurity.gov/forms/ssa-827-inst-sp.pdf ";
            case VA100484: return "VA Form 10-0484 Revocation for Release of Individually-Identifiable Health Information enables a veteran to revoke authorization for the VA to release specified copies of individually-identifiable health information with the non-VA health care provider organizations participating in the eHealth Exchange and partnering with VA. Comment: Opt-in Consent Directive with status = rescinded (aka 'revoked'). Note: Form is available at http://www.va.gov/vaforms/medical/pdf/vha-10-0484-fill.pdf";
            case VA100485: return "VA Form 10-0485 Request for and Authorization to Release Protected Health Information to eHealth Exchange enables a veteran to request and authorize a VA health care facility to release protected health information (PHI) for treatment purposes only to the communities that are participating in the eHealth Exchange, VLER Directive, and other Health Information Exchanges with who VA has an agreement. This information may consist of the diagnosis of Sickle Cell Anemia, the treatment of or referral for Drug Abuse, treatment of or referral for Alcohol Abuse or the treatment of or testing for infection with Human Immunodeficiency Virus. This authorization covers the diagnoses that I may have upon signing of the authorization and the diagnoses that I may acquire in the future including those protected by 38 U.S.C. 7332. Comment: Opt-in Consent Directive. Note: Form is available at http://www.va.gov/vaforms/medical/pdf/10-0485-fill.pdf";
            case VA105345: return "VA Form 10-5345 Request for and Authorization to Release Medical Records or Health Information enables a veteran to request and authorize the VA to release specified copies of protected health information (PHI), such as hospital summary or outpatient treatment notes, which may include information about conditions governed under Title 38 Section 7332 (drug abuse, alcoholism or alcohol abuse, testing for or infection with HIV, and sickle cell anemia). Comment: Opt-in Consent Directive. Note: Form is available at http://www.va.gov/vaforms/medical/pdf/vha-10-5345-fill.pdf";
            case VA105345A: return "VA Form 10-5345a Individuals' Request for a Copy of Their Own Health Information enables a veteran to request and authorize the VA to release specified copies of protected health information (PHI), such as hospital summary or outpatient treatment notes. Note: Form is available at http://www.va.gov/vaforms/medical/pdf/vha-10-5345a-fill.pdf";
            case VA105345AMHV: return "VA Form 10-5345a-MHV Individualâ€™s Request for a Copy of their own health information from MyHealtheVet enables a veteran to receive a copy of all available personal health information to be delivered through the veteranâ€™s My HealtheVet account. Note: Form is available at http://www.va.gov/vaforms/medical/pdf/vha-10-5345a-MHV-fill.pdf";
            case VA1010116: return "VA Form 10-10116 Revocation of Authorization for Use and Release of Individually Identifiable Health Information for Veterans Health Administration Research. Comment: Opt-in with Restriction Consent Directive with status = 'completed'. Note: Form is available at http://www.northerncalifornia.va.gov/northerncalifornia/services/rnd/docs/vha-10-10116.pdf ";
            case VA214142: return "VA Form 21-4142 (Authorization and Consent to Release Information to the Department of Veterans Affairs (VA) enables a veteran to authorize the US Veterans Administration [VA] to request veteranâ€™s health information from non-VA providers. Aka VA Compensation Application Note: Form is available at http://www.vba.va.gov/pubs/forms/VBA-21-4142-ARE.pdf . For additional information regarding VA Form 21-4142, refer to the following website: www.benefits.va.gov/compensation/consent_privateproviders";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case _42CFR2: return "42 CFR Part 2 Form of written consent";
            case ACD: return "advance directive";
            case CRIC: return "common rule informed consent";
            case DNR: return "do not resuscitate";
            case EMRGONLY: return "emergency only";
            case ILLINOISMINORPROCEDURE: return "Illinois Consent by Minors to Medical Procedures";
            case HCD: return "health care directive";
            case HIPAAAUTH: return "HIPAA Authorization";
            case HIPAANPP: return "HIPAA Notice of Privacy Practices";
            case HIPAARESTRICTIONS: return "HIPAA Restrictions";
            case HIPAARESEARCH: return "HIPAA Research Authorization";
            case HIPAASELFPAY: return "HIPAA Self-Pay Restriction";
            case MDHHS5515: return "Michigan MDHHS-5515 Consent to Share Behavioral Health Information for Care Coordination Purposes";
            case NYSSIPP: return "New York State Surgical and Invasive Procedure Protocol";
            case NPP: return "notice of privacy practices";
            case POLST: return "POLST";
            case RESEARCH: return "research information access";
            case RSDID: return "de-identified information access";
            case RSREID: return "re-identifiable information access";
            case SSA827: return "Form SSA-827";
            case VA100484: return "VA Form 10-0484";
            case VA100485: return "VA Form 10-0485";
            case VA105345: return "VA Form 10-5345";
            case VA105345A: return "VA Form 10-5345a";
            case VA105345AMHV: return "VA Form 10-5345a-MHV";
            case VA1010116: return "VA Form 10-10-10116";
            case VA214142: return "VA Form 21-4142";
            default: return "?";
          }
    }


}

