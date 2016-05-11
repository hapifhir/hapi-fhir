package org.hl7.fhir.instance.model.valuesets;

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

// Generated on Wed, Nov 11, 2015 10:54-0500 for FHIR v1.0.2


public enum V3ActUSPrivacyLaw {

        /**
         * Definition: A jurisdictional mandate in the U.S. relating to privacy.

                        
                           Usage Note: ActPrivacyLaw codes may be associated with an Act or a Role to indicate the legal provision to which the assignment of an Act.confidentialityCode or Role.confidentialtyCode complies.  May be used to further specify rationale for assignment of other ActPrivacyPolicy codes in the US realm, e.g. ETH and 42CFRPart2 can be differentiated from ETH and Title38Part1.
         */
        _ACTUSPRIVACYLAW, 
        /**
         * 42 CFR Part 2 stipulates the right of an individual who has applied for or been given diagnosis or treatment for alcohol or drug abuse at a federally assisted program.

                        
                           Definition: Non-disclosure of health information relating to health care paid for by a federally assisted substance abuse program without patient consent.

                        
                           Usage Note: May be associated with an Act or a Role to indicate the legal provision to which the assignment of an Act.confidentialityCode or Role.confidentialityCode complies.
         */
        _42CFRPART2, 
        /**
         * U.S. Federal regulations governing the protection of human subjects in research (codified at Subpart A of 45 CFR part 46) that has been adopted by 15 U.S. Federal departments and agencies in an effort to promote uniformity, understanding, and compliance with human subject protections. Existing regulations governing the protection of human subjects in Food and Drug Administration (FDA)-regulated research (21 CFR parts 50, 56, 312, and 812) are separate from the Common Rule but include similar requirements.

                        
                           Definition: U.S. federal laws governing research-related privacy policies.

                        
                           Usage Note: May be associated with an Act or a Role to indicate the legal provision to which the assignment of an Act.confidentialityCode or Role.confidentialtyCode complies.
         */
        COMMONRULE, 
        /**
         * The U.S. Public Law 104-191 Health Insurance Portability and Accountability Act (HIPAA) Privacy Rule (45 CFR Part 164 Subpart E) permits access, use and disclosure of certain personal health information (PHI as defined under the law) for purposes of Treatment, Payment, and Operations, and requires that the provider ask that patients acknowledge the Provider's Notice of Privacy Practices as permitted conduct under the law.

                        
                           Definition: Notification of HIPAA Privacy Practices.

                        
                           Usage Note: May be associated with an Act or a Role to indicate the legal provision to which the assignment of an Act.confidentialityCode or Role.confidentialtyCode complies.
         */
        HIPAANOPP, 
        /**
         * The U.S. Public Law 104-191 Health Insurance Portability and Accountability Act (HIPAA) Privacy Rule (45 CFR Part 164 Section 164.508) requires authorization for certain uses and disclosure of psychotherapy notes.

                        
                           Definition: Authorization that must be obtained for disclosure of psychotherapy notes.

                        
                           Usage Note: May be associated with an Act or a Role to indicate the legal provision to which the assignment of an Act.confidentialityCode or Role.confidentialityCode complies.
         */
        HIPAAPSYNOTES, 
        /**
         * Section 13405(a) of the Health Information Technology for Economic and Clinical Health Act (HITECH) stipulates the right of an individual to have disclosures regarding certain health care items or services for which the individual pays out of pocket in full restricted from a health plan.

                        
                           Definition: Non-disclosure of health information to a health plan relating to health care items or services for which an individual pays out of pocket in full.

                        
                           Usage Note: May be associated with an Act or a Role to indicate the legal provision to which the assignment of an Act.confidentialityCode or Role.confidentialityCode complies.
         */
        HIPAASELFPAY, 
        /**
         * Title 38 Part 1-protected information may only be disclosed to a third party with the special written consent of the patient except where expressly authorized by 38 USC 7332. VA may disclose this information for specific purposes to: VA employees on a need to know basis - more restrictive than Privacy Act need to know; contractors who need the information in order to perform or fulfill the duties of the contract; and researchers who provide assurances that the information will not be identified in any report.  This information may also be disclosed without consent where patient lacks decision-making capacity; in a medical emergency for the purpose of treating a condition which poses an immediate threat to the health of any individual and which requires immediate medical intervention; for eye, tissue, or organ donation purposes; and disclosure of HIV information for public health purposes.

                        
                           Definition: Title 38 Part 1 - Â§1.462 Confidentiality restrictions.

                        
(a) General. The patient records to which Â§Â§1.460 through 1.499 of this part apply may be disclosed or used only as permitted by these regulations and may not otherwise be disclosed or used in any civil, criminal, administrative, or legislative proceedings conducted by any Federal, State, or local authority. Any disclosure made under these regulations must be limited to that information which is necessary to carry out the purpose of the disclosure. SUBCHAPTER III--PROTECTION OF PATIENT RIGHTS Sec. 7332. Confidentiality of certain medical records (a)(1) Records of the identity, diagnosis, prognosis, or treatment of any patient or subject which are maintained in connection with the performance of any program or activity (including education, training, treatment, rehabilitation, or research) relating to drug abuse, alcoholism or alcohol abuse, infection with the human immunodeficiency virus, or sickle cell anemia which is carried out by or for the Department under this title shall, except as provided in subsections (e) and (f), be confidential, and (section 5701 of this title to the contrary notwithstanding) such records may be disclosed only for the purposes and under the circumstances expressly authorized under subsection (b).

                        
                           Usage Note: May be associated with an Act or a Role to indicate the legal provision to which the assignment of an Act.confidentialityCode or Role.confidentialityCode complies.
         */
        TITLE38SECTION7332, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3ActUSPrivacyLaw fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("_ActUSPrivacyLaw".equals(codeString))
          return _ACTUSPRIVACYLAW;
        if ("42CFRPart2".equals(codeString))
          return _42CFRPART2;
        if ("CommonRule".equals(codeString))
          return COMMONRULE;
        if ("HIPAANOPP".equals(codeString))
          return HIPAANOPP;
        if ("HIPAAPsyNotes".equals(codeString))
          return HIPAAPSYNOTES;
        if ("HIPAASelfPay".equals(codeString))
          return HIPAASELFPAY;
        if ("Title38Section7332".equals(codeString))
          return TITLE38SECTION7332;
        throw new Exception("Unknown V3ActUSPrivacyLaw code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case _ACTUSPRIVACYLAW: return "_ActUSPrivacyLaw";
            case _42CFRPART2: return "42CFRPart2";
            case COMMONRULE: return "CommonRule";
            case HIPAANOPP: return "HIPAANOPP";
            case HIPAAPSYNOTES: return "HIPAAPsyNotes";
            case HIPAASELFPAY: return "HIPAASelfPay";
            case TITLE38SECTION7332: return "Title38Section7332";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/ActUSPrivacyLaw";
        }
        public String getDefinition() {
          switch (this) {
            case _ACTUSPRIVACYLAW: return "Definition: A jurisdictional mandate in the U.S. relating to privacy.\r\n\n                        \n                           Usage Note: ActPrivacyLaw codes may be associated with an Act or a Role to indicate the legal provision to which the assignment of an Act.confidentialityCode or Role.confidentialtyCode complies.  May be used to further specify rationale for assignment of other ActPrivacyPolicy codes in the US realm, e.g. ETH and 42CFRPart2 can be differentiated from ETH and Title38Part1.";
            case _42CFRPART2: return "42 CFR Part 2 stipulates the right of an individual who has applied for or been given diagnosis or treatment for alcohol or drug abuse at a federally assisted program.\r\n\n                        \n                           Definition: Non-disclosure of health information relating to health care paid for by a federally assisted substance abuse program without patient consent.\r\n\n                        \n                           Usage Note: May be associated with an Act or a Role to indicate the legal provision to which the assignment of an Act.confidentialityCode or Role.confidentialityCode complies.";
            case COMMONRULE: return "U.S. Federal regulations governing the protection of human subjects in research (codified at Subpart A of 45 CFR part 46) that has been adopted by 15 U.S. Federal departments and agencies in an effort to promote uniformity, understanding, and compliance with human subject protections. Existing regulations governing the protection of human subjects in Food and Drug Administration (FDA)-regulated research (21 CFR parts 50, 56, 312, and 812) are separate from the Common Rule but include similar requirements.\r\n\n                        \n                           Definition: U.S. federal laws governing research-related privacy policies.\r\n\n                        \n                           Usage Note: May be associated with an Act or a Role to indicate the legal provision to which the assignment of an Act.confidentialityCode or Role.confidentialtyCode complies.";
            case HIPAANOPP: return "The U.S. Public Law 104-191 Health Insurance Portability and Accountability Act (HIPAA) Privacy Rule (45 CFR Part 164 Subpart E) permits access, use and disclosure of certain personal health information (PHI as defined under the law) for purposes of Treatment, Payment, and Operations, and requires that the provider ask that patients acknowledge the Provider's Notice of Privacy Practices as permitted conduct under the law.\r\n\n                        \n                           Definition: Notification of HIPAA Privacy Practices.\r\n\n                        \n                           Usage Note: May be associated with an Act or a Role to indicate the legal provision to which the assignment of an Act.confidentialityCode or Role.confidentialtyCode complies.";
            case HIPAAPSYNOTES: return "The U.S. Public Law 104-191 Health Insurance Portability and Accountability Act (HIPAA) Privacy Rule (45 CFR Part 164 Section 164.508) requires authorization for certain uses and disclosure of psychotherapy notes.\r\n\n                        \n                           Definition: Authorization that must be obtained for disclosure of psychotherapy notes.\r\n\n                        \n                           Usage Note: May be associated with an Act or a Role to indicate the legal provision to which the assignment of an Act.confidentialityCode or Role.confidentialityCode complies.";
            case HIPAASELFPAY: return "Section 13405(a) of the Health Information Technology for Economic and Clinical Health Act (HITECH) stipulates the right of an individual to have disclosures regarding certain health care items or services for which the individual pays out of pocket in full restricted from a health plan.\r\n\n                        \n                           Definition: Non-disclosure of health information to a health plan relating to health care items or services for which an individual pays out of pocket in full.\r\n\n                        \n                           Usage Note: May be associated with an Act or a Role to indicate the legal provision to which the assignment of an Act.confidentialityCode or Role.confidentialityCode complies.";
            case TITLE38SECTION7332: return "Title 38 Part 1-protected information may only be disclosed to a third party with the special written consent of the patient except where expressly authorized by 38 USC 7332. VA may disclose this information for specific purposes to: VA employees on a need to know basis - more restrictive than Privacy Act need to know; contractors who need the information in order to perform or fulfill the duties of the contract; and researchers who provide assurances that the information will not be identified in any report.  This information may also be disclosed without consent where patient lacks decision-making capacity; in a medical emergency for the purpose of treating a condition which poses an immediate threat to the health of any individual and which requires immediate medical intervention; for eye, tissue, or organ donation purposes; and disclosure of HIV information for public health purposes.\r\n\n                        \n                           Definition: Title 38 Part 1 - Â§1.462 Confidentiality restrictions.\r\n\n                        \n(a) General. The patient records to which Â§Â§1.460 through 1.499 of this part apply may be disclosed or used only as permitted by these regulations and may not otherwise be disclosed or used in any civil, criminal, administrative, or legislative proceedings conducted by any Federal, State, or local authority. Any disclosure made under these regulations must be limited to that information which is necessary to carry out the purpose of the disclosure. SUBCHAPTER III--PROTECTION OF PATIENT RIGHTS Sec. 7332. Confidentiality of certain medical records (a)(1) Records of the identity, diagnosis, prognosis, or treatment of any patient or subject which are maintained in connection with the performance of any program or activity (including education, training, treatment, rehabilitation, or research) relating to drug abuse, alcoholism or alcohol abuse, infection with the human immunodeficiency virus, or sickle cell anemia which is carried out by or for the Department under this title shall, except as provided in subsections (e) and (f), be confidential, and (section 5701 of this title to the contrary notwithstanding) such records may be disclosed only for the purposes and under the circumstances expressly authorized under subsection (b).\r\n\n                        \n                           Usage Note: May be associated with an Act or a Role to indicate the legal provision to which the assignment of an Act.confidentialityCode or Role.confidentialityCode complies.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case _ACTUSPRIVACYLAW: return "ActUSPrivacyLaw";
            case _42CFRPART2: return "42 CFR Part2";
            case COMMONRULE: return "Common Rule";
            case HIPAANOPP: return "HIPAA notice of privacy practices";
            case HIPAAPSYNOTES: return "HIPAA psychotherapy notes";
            case HIPAASELFPAY: return "HIPAA self-pay";
            case TITLE38SECTION7332: return "Title 38 Section 7332";
            default: return "?";
          }
    }


}

