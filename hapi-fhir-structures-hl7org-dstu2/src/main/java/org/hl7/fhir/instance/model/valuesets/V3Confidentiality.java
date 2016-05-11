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


public enum V3Confidentiality {

        /**
         * A specializable code and its leaf codes used in Confidentiality value sets to value the Act.Confidentiality and Role.Confidentiality attribute in accordance with the definition for concept domain "Confidentiality".
         */
        _CONFIDENTIALITY, 
        /**
         * Definition: Privacy metadata indicating that the information has been de-identified, and there are mitigating circumstances that prevent re-identification, which minimize risk of harm from unauthorized disclosure.  The information requires protection to maintain low sensitivity.

                        
                           Examples: Includes anonymized, pseudonymized, or non-personally identifiable information such as HIPAA limited data sets.

                        
                           Map: No clear map to ISO 13606-4 Sensitivity Level (1) Care Management:   RECORD_COMPONENTs that might need to be accessed by a wide range of administrative staff to manage the subject of care's access to health services.

                        
                           Usage Note: This metadata indicates the receiver may have an obligation to comply with a data use agreement.
         */
        L, 
        /**
         * Definition: Privacy metadata indicating moderately sensitive information, which presents moderate risk of harm if disclosed without authorization.

                        
                           Examples: Includes allergies of non-sensitive nature used inform food service; health information a patient authorizes to be used for marketing, released to a bank for a health credit card or savings account; or information in personal health record systems that are not governed under health privacy laws.

                        
                           Map: Partial Map to ISO 13606-4 Sensitivity Level (2) Clinical Management:  Less sensitive RECORD_COMPONENTs that might need to be accessed by a wider range of personnel not all of whom are actively caring for the patient (e.g. radiology staff).

                        
                           Usage Note: This metadata indicates that the receiver may be obligated to comply with the receiver's terms of use or privacy policies.
         */
        M, 
        /**
         * Definition: Privacy metadata indicating that the information is typical, non-stigmatizing health information, which presents typical risk of harm if disclosed without authorization.

                        
                           Examples: In the US, this includes what HIPAA identifies as the minimum necessary protected health information (PHI) given a covered purpose of use (treatment, payment, or operations).  Includes typical, non-stigmatizing health information disclosed in an application for health, workers compensation, disability, or life insurance.

                        
                           Map: Partial Map to ISO 13606-4 Sensitivity Level (3) Clinical Care:   Default for normal clinical care access (i.e. most clinical staff directly caring for the patient should be able to access nearly all of the EHR).   Maps to normal confidentiality for treatment information but not to ancillary care, payment and operations.

                        
                           Usage Note: This metadata indicates that the receiver may be obligated to comply with applicable jurisdictional privacy law or disclosure authorization.
         */
        N, 
        /**
         * Privacy metadata indicating highly sensitive, potentially stigmatizing information, which presents a high risk to the information subject if disclosed without authorization. May be pre-empted by jurisdictional law, e.g. for public health reporting or emergency treatment.

                        
                           Examples: Includes information that is additionally protected such as sensitive conditions mental health, HIV, substance abuse, domestic violence, child abuse, genetic disease, and reproductive health; or sensitive demographic information such as a patient's standing as an employee or a celebrity. May be used to indicate proprietary or classified information that is not related to an individual, e.g. secret ingredients in a therapeutic substance; or the name of a manufacturer.

                        
                           Map: Partial Map to ISO 13606-4 Sensitivity Level (3) Clinical Care: Default for normal clinical care access (i.e. most clinical staff directly caring for the patient should be able to access nearly all of the EHR). Maps to normal confidentiality for treatment information but not to ancillary care, payment and operations..

                        
                           Usage Note: This metadata indicates that the receiver may be obligated to comply with applicable, prevailing (default) jurisdictional privacy law or disclosure authorization..
         */
        R, 
        /**
         * Definition: Privacy metadata indicating that the information is not classified as sensitive.

                        
                           Examples: Includes publicly available information, e.g. business name, phone, email or physical address.

                        
                           Usage Note: This metadata indicates that the receiver has no obligation to consider additional policies when making access control decisions.   Note that in some jurisdictions, personally identifiable information must be protected as confidential, so it would not be appropriate to assign a confidentiality code of "unrestricted"  to that information even if it is publicly available.
         */
        U, 
        /**
         * . Privacy metadata indicating that the information is extremely sensitive and likely stigmatizing health information that presents a very high risk if disclosed without authorization.  This information must be kept in the highest confidence.  

                        
                           Examples:  Includes information about a victim of abuse, patient requested information sensitivity, and taboo subjects relating to health status that must be discussed with the patient by an attending provider before sharing with the patient.  May also include information held under â€œlegal lockâ€? or attorney-client privilege

                        
                           Map:  This metadata indicates that the receiver may not disclose this information except as directed by the information custodian, who may be the information subject.

                        
                           Usage Note:  This metadata indicates that the receiver may not disclose this information except as directed by the information custodian, who may be the information subject.
         */
        V, 
        /**
         * Description: By accessing subject / role and relationship based  rights  (These concepts are mutually exclusive, one and only one is required for a valid confidentiality coding.)

                        
                           Deprecation Comment:Deprecated due to updated confidentiality codes under ActCode
         */
        _CONFIDENTIALITYBYACCESSKIND, 
        /**
         * Description: Since the service class can represent knowledge structures that may be considered a trade or business secret, there is sometimes (though rarely) the need to flag those items as of business level confidentiality.  However, no patient related information may ever be of this confidentiality level.

                        
                           Deprecation Comment: Replced by ActCode.B
         */
        B, 
        /**
         * Description: Only clinicians may see this item, billing and administration persons can not access this item without special permission.

                        
                           Deprecation Comment:Deprecated due to updated confidentiality codes under ActCode
         */
        D, 
        /**
         * Description: Access only to individual persons who are mentioned explicitly as actors of this service and whose actor type warrants that access (cf. to actor type code).

                        
                           Deprecation Comment:Deprecated due to updated confidentiality codes under ActCode
         */
        I, 
        /**
         * Description: By information type, only for service catalog entries (multiples allowed). Not to be used with actual patient data!

                        
                           Deprecation Comment:Deprecated due to updated confidentiality codes under ActCode
         */
        _CONFIDENTIALITYBYINFOTYPE, 
        /**
         * Description: Alcohol/drug-abuse related item

                        
                           Deprecation Comment:Replced by ActCode.ETH
         */
        ETH, 
        /**
         * Description: HIV and AIDS related item

                        
                           Deprecation Comment:Replced by ActCode.HIV
         */
        HIV, 
        /**
         * Description: Psychiatry related item

                        
                           Deprecation Comment:Replced by ActCode.PSY
         */
        PSY, 
        /**
         * Description: Sexual assault / domestic violence related item

                        
                           Deprecation Comment:Replced by ActCode.SDV
         */
        SDV, 
        /**
         * Description: Modifiers of role based access rights  (multiple allowed)

                        
                           Deprecation Comment:Deprecated due to updated confidentiality codes under ActCode
         */
        _CONFIDENTIALITYMODIFIERS, 
        /**
         * Description: Celebrities are people of public interest (VIP) including employees, whose information require special protection.

                        
                           Deprecation Comment:Replced by ActCode.CEL
         */
        C, 
        /**
         * Description: 
                        
Information for which the patient seeks heightened confidentiality. Sensitive information is not to be shared with family members.  Information reported by the patient about family members is sensitive by default. Flag can be set or cleared on patient's request.
                           Deprecation Comment:Deprecated due to updated confidentiality codes under ActCode
         */
        S, 
        /**
         * Description: Information not to be disclosed or discussed with patient except through physician assigned to patient in this case.  This is usually a temporary constraint only, example use is a new fatal diagnosis or finding, such as malignancy or HIV.

                        
                           Deprecation Note:Replced by ActCode.TBOO
         */
        T, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3Confidentiality fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("_Confidentiality".equals(codeString))
          return _CONFIDENTIALITY;
        if ("L".equals(codeString))
          return L;
        if ("M".equals(codeString))
          return M;
        if ("N".equals(codeString))
          return N;
        if ("R".equals(codeString))
          return R;
        if ("U".equals(codeString))
          return U;
        if ("V".equals(codeString))
          return V;
        if ("_ConfidentialityByAccessKind".equals(codeString))
          return _CONFIDENTIALITYBYACCESSKIND;
        if ("B".equals(codeString))
          return B;
        if ("D".equals(codeString))
          return D;
        if ("I".equals(codeString))
          return I;
        if ("_ConfidentialityByInfoType".equals(codeString))
          return _CONFIDENTIALITYBYINFOTYPE;
        if ("ETH".equals(codeString))
          return ETH;
        if ("HIV".equals(codeString))
          return HIV;
        if ("PSY".equals(codeString))
          return PSY;
        if ("SDV".equals(codeString))
          return SDV;
        if ("_ConfidentialityModifiers".equals(codeString))
          return _CONFIDENTIALITYMODIFIERS;
        if ("C".equals(codeString))
          return C;
        if ("S".equals(codeString))
          return S;
        if ("T".equals(codeString))
          return T;
        throw new Exception("Unknown V3Confidentiality code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case _CONFIDENTIALITY: return "_Confidentiality";
            case L: return "L";
            case M: return "M";
            case N: return "N";
            case R: return "R";
            case U: return "U";
            case V: return "V";
            case _CONFIDENTIALITYBYACCESSKIND: return "_ConfidentialityByAccessKind";
            case B: return "B";
            case D: return "D";
            case I: return "I";
            case _CONFIDENTIALITYBYINFOTYPE: return "_ConfidentialityByInfoType";
            case ETH: return "ETH";
            case HIV: return "HIV";
            case PSY: return "PSY";
            case SDV: return "SDV";
            case _CONFIDENTIALITYMODIFIERS: return "_ConfidentialityModifiers";
            case C: return "C";
            case S: return "S";
            case T: return "T";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/Confidentiality";
        }
        public String getDefinition() {
          switch (this) {
            case _CONFIDENTIALITY: return "A specializable code and its leaf codes used in Confidentiality value sets to value the Act.Confidentiality and Role.Confidentiality attribute in accordance with the definition for concept domain \"Confidentiality\".";
            case L: return "Definition: Privacy metadata indicating that the information has been de-identified, and there are mitigating circumstances that prevent re-identification, which minimize risk of harm from unauthorized disclosure.  The information requires protection to maintain low sensitivity.\r\n\n                        \n                           Examples: Includes anonymized, pseudonymized, or non-personally identifiable information such as HIPAA limited data sets.\r\n\n                        \n                           Map: No clear map to ISO 13606-4 Sensitivity Level (1) Care Management:   RECORD_COMPONENTs that might need to be accessed by a wide range of administrative staff to manage the subject of care's access to health services.\r\n\n                        \n                           Usage Note: This metadata indicates the receiver may have an obligation to comply with a data use agreement.";
            case M: return "Definition: Privacy metadata indicating moderately sensitive information, which presents moderate risk of harm if disclosed without authorization.\r\n\n                        \n                           Examples: Includes allergies of non-sensitive nature used inform food service; health information a patient authorizes to be used for marketing, released to a bank for a health credit card or savings account; or information in personal health record systems that are not governed under health privacy laws.\r\n\n                        \n                           Map: Partial Map to ISO 13606-4 Sensitivity Level (2) Clinical Management:  Less sensitive RECORD_COMPONENTs that might need to be accessed by a wider range of personnel not all of whom are actively caring for the patient (e.g. radiology staff).\r\n\n                        \n                           Usage Note: This metadata indicates that the receiver may be obligated to comply with the receiver's terms of use or privacy policies.";
            case N: return "Definition: Privacy metadata indicating that the information is typical, non-stigmatizing health information, which presents typical risk of harm if disclosed without authorization.\r\n\n                        \n                           Examples: In the US, this includes what HIPAA identifies as the minimum necessary protected health information (PHI) given a covered purpose of use (treatment, payment, or operations).  Includes typical, non-stigmatizing health information disclosed in an application for health, workers compensation, disability, or life insurance.\r\n\n                        \n                           Map: Partial Map to ISO 13606-4 Sensitivity Level (3) Clinical Care:   Default for normal clinical care access (i.e. most clinical staff directly caring for the patient should be able to access nearly all of the EHR).   Maps to normal confidentiality for treatment information but not to ancillary care, payment and operations.\r\n\n                        \n                           Usage Note: This metadata indicates that the receiver may be obligated to comply with applicable jurisdictional privacy law or disclosure authorization.";
            case R: return "Privacy metadata indicating highly sensitive, potentially stigmatizing information, which presents a high risk to the information subject if disclosed without authorization. May be pre-empted by jurisdictional law, e.g. for public health reporting or emergency treatment.\r\n\n                        \n                           Examples: Includes information that is additionally protected such as sensitive conditions mental health, HIV, substance abuse, domestic violence, child abuse, genetic disease, and reproductive health; or sensitive demographic information such as a patient's standing as an employee or a celebrity. May be used to indicate proprietary or classified information that is not related to an individual, e.g. secret ingredients in a therapeutic substance; or the name of a manufacturer.\r\n\n                        \n                           Map: Partial Map to ISO 13606-4 Sensitivity Level (3) Clinical Care: Default for normal clinical care access (i.e. most clinical staff directly caring for the patient should be able to access nearly all of the EHR). Maps to normal confidentiality for treatment information but not to ancillary care, payment and operations..\r\n\n                        \n                           Usage Note: This metadata indicates that the receiver may be obligated to comply with applicable, prevailing (default) jurisdictional privacy law or disclosure authorization..";
            case U: return "Definition: Privacy metadata indicating that the information is not classified as sensitive.\r\n\n                        \n                           Examples: Includes publicly available information, e.g. business name, phone, email or physical address.\r\n\n                        \n                           Usage Note: This metadata indicates that the receiver has no obligation to consider additional policies when making access control decisions.   Note that in some jurisdictions, personally identifiable information must be protected as confidential, so it would not be appropriate to assign a confidentiality code of \"unrestricted\"  to that information even if it is publicly available.";
            case V: return ". Privacy metadata indicating that the information is extremely sensitive and likely stigmatizing health information that presents a very high risk if disclosed without authorization.  This information must be kept in the highest confidence.  \r\n\n                        \n                           Examples:  Includes information about a victim of abuse, patient requested information sensitivity, and taboo subjects relating to health status that must be discussed with the patient by an attending provider before sharing with the patient.  May also include information held under â€œlegal lockâ€? or attorney-client privilege\r\n\n                        \n                           Map:  This metadata indicates that the receiver may not disclose this information except as directed by the information custodian, who may be the information subject.\r\n\n                        \n                           Usage Note:  This metadata indicates that the receiver may not disclose this information except as directed by the information custodian, who may be the information subject.";
            case _CONFIDENTIALITYBYACCESSKIND: return "Description: By accessing subject / role and relationship based  rights  (These concepts are mutually exclusive, one and only one is required for a valid confidentiality coding.)\r\n\n                        \n                           Deprecation Comment:Deprecated due to updated confidentiality codes under ActCode";
            case B: return "Description: Since the service class can represent knowledge structures that may be considered a trade or business secret, there is sometimes (though rarely) the need to flag those items as of business level confidentiality.  However, no patient related information may ever be of this confidentiality level.\r\n\n                        \n                           Deprecation Comment: Replced by ActCode.B";
            case D: return "Description: Only clinicians may see this item, billing and administration persons can not access this item without special permission.\r\n\n                        \n                           Deprecation Comment:Deprecated due to updated confidentiality codes under ActCode";
            case I: return "Description: Access only to individual persons who are mentioned explicitly as actors of this service and whose actor type warrants that access (cf. to actor type code).\r\n\n                        \n                           Deprecation Comment:Deprecated due to updated confidentiality codes under ActCode";
            case _CONFIDENTIALITYBYINFOTYPE: return "Description: By information type, only for service catalog entries (multiples allowed). Not to be used with actual patient data!\r\n\n                        \n                           Deprecation Comment:Deprecated due to updated confidentiality codes under ActCode";
            case ETH: return "Description: Alcohol/drug-abuse related item\r\n\n                        \n                           Deprecation Comment:Replced by ActCode.ETH";
            case HIV: return "Description: HIV and AIDS related item\r\n\n                        \n                           Deprecation Comment:Replced by ActCode.HIV";
            case PSY: return "Description: Psychiatry related item\r\n\n                        \n                           Deprecation Comment:Replced by ActCode.PSY";
            case SDV: return "Description: Sexual assault / domestic violence related item\r\n\n                        \n                           Deprecation Comment:Replced by ActCode.SDV";
            case _CONFIDENTIALITYMODIFIERS: return "Description: Modifiers of role based access rights  (multiple allowed)\r\n\n                        \n                           Deprecation Comment:Deprecated due to updated confidentiality codes under ActCode";
            case C: return "Description: Celebrities are people of public interest (VIP) including employees, whose information require special protection.\r\n\n                        \n                           Deprecation Comment:Replced by ActCode.CEL";
            case S: return "Description: \n                        \r\nInformation for which the patient seeks heightened confidentiality. Sensitive information is not to be shared with family members.  Information reported by the patient about family members is sensitive by default. Flag can be set or cleared on patient's request.\n                           Deprecation Comment:Deprecated due to updated confidentiality codes under ActCode";
            case T: return "Description: Information not to be disclosed or discussed with patient except through physician assigned to patient in this case.  This is usually a temporary constraint only, example use is a new fatal diagnosis or finding, such as malignancy or HIV.\r\n\n                        \n                           Deprecation Note:Replced by ActCode.TBOO";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case _CONFIDENTIALITY: return "Confidentiality";
            case L: return "low";
            case M: return "moderate";
            case N: return "normal";
            case R: return "restricted";
            case U: return "unrestricted";
            case V: return "very restricted";
            case _CONFIDENTIALITYBYACCESSKIND: return "ConfidentialityByAccessKind";
            case B: return "business";
            case D: return "clinician";
            case I: return "individual";
            case _CONFIDENTIALITYBYINFOTYPE: return "ConfidentialityByInfoType";
            case ETH: return "substance abuse related";
            case HIV: return "HIV related";
            case PSY: return "psychiatry relate";
            case SDV: return "sexual and domestic violence related";
            case _CONFIDENTIALITYMODIFIERS: return "ConfidentialityModifiers";
            case C: return "celebrity";
            case S: return "sensitive";
            case T: return "taboo";
            default: return "?";
          }
    }


}

