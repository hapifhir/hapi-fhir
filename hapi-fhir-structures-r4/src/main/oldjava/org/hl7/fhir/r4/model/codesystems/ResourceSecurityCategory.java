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

public enum ResourceSecurityCategory {

        /**
         * These resources tend to not contain any individual data, or business sensitive data. Most often these Resources will be available for anonymous access, meaning there is no access control based on the user or system requesting. However these Resources do tend to contain important information that must be authenticated back to the source publishing them, and protected from integrity failures in communication. For this reason server authenticated https (TLS) is recommended to provide authentication of the server and integrity protection in transit. This is normal web-server use of https.
         */
        ANONYMOUS, 
        /**
         * These Resources tend to not contain any individual data, but do have data that describe business or service sensitive data. The use of the term Business is not intended to only mean an incorporated business, but rather the more broad concept of an organization, location, or other group that is not identifable as individuals. Often these resources will require some for of client authentication to assure that only authorized access is given. The client access control may be to individuals, or may be to system identity. For this purpose possible client authentication methods such as: mutual-authenticated-TLS, APIKey, App signed JWT, or App OAuth client-id JWT For example: a App that uses a Business protected Provider Directory to determine other business endpoint details.
         */
        BUSINESS, 
        /**
         * These Resources do NOT contain Patient data, but do contain individual information about other participants. These other individuals are Practitioners, PractionerRole, CareTeam, or other users. These identities are needed to enable the practice of healthcare. These identities are identities under general privacy regulations, and thus must consider Privacy risk. Often access to these other identities are covered by business relationships. For this purpose access to these Resources will tend to be Role specific using methods such as RBAC or ABAC.
         */
        INDIVIDUAL, 
        /**
         * These Resources make up the bulk of FHIR and therefore are the most commonly understood. These Resources contain highly sesitive health information, or are closely linked to highly sensitive health information. These Resources will often use the security labels to differentiate various confidentiality levels within this broad group of Patient Sensitive data. Access to these Resources often requires a declared Purpose Of Use. Access to these Resources is often controlled by a Privacy Consent.
         */
        PATIENT, 
        /**
         * Some Resources can be used for a wide scope of use-cases that span very sensitive to very non-sensitive. These Resources do not fall into any of the above classifications, as their sensitivity is highly variable. These Resources will need special handling. These Resources often contain metadata that describes the content in a way that can be used for Access Control decisions.
         */
        NOTCLASSIFIED, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ResourceSecurityCategory fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("anonymous".equals(codeString))
          return ANONYMOUS;
        if ("business".equals(codeString))
          return BUSINESS;
        if ("individual".equals(codeString))
          return INDIVIDUAL;
        if ("patient".equals(codeString))
          return PATIENT;
        if ("not-classified".equals(codeString))
          return NOTCLASSIFIED;
        throw new FHIRException("Unknown ResourceSecurityCategory code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ANONYMOUS: return "anonymous";
            case BUSINESS: return "business";
            case INDIVIDUAL: return "individual";
            case PATIENT: return "patient";
            case NOTCLASSIFIED: return "not-classified";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://terminology.hl7.org/CodeSystem/resource-security-category";
        }
        public String getDefinition() {
          switch (this) {
            case ANONYMOUS: return "These resources tend to not contain any individual data, or business sensitive data. Most often these Resources will be available for anonymous access, meaning there is no access control based on the user or system requesting. However these Resources do tend to contain important information that must be authenticated back to the source publishing them, and protected from integrity failures in communication. For this reason server authenticated https (TLS) is recommended to provide authentication of the server and integrity protection in transit. This is normal web-server use of https.";
            case BUSINESS: return "These Resources tend to not contain any individual data, but do have data that describe business or service sensitive data. The use of the term Business is not intended to only mean an incorporated business, but rather the more broad concept of an organization, location, or other group that is not identifable as individuals. Often these resources will require some for of client authentication to assure that only authorized access is given. The client access control may be to individuals, or may be to system identity. For this purpose possible client authentication methods such as: mutual-authenticated-TLS, APIKey, App signed JWT, or App OAuth client-id JWT For example: a App that uses a Business protected Provider Directory to determine other business endpoint details.";
            case INDIVIDUAL: return "These Resources do NOT contain Patient data, but do contain individual information about other participants. These other individuals are Practitioners, PractionerRole, CareTeam, or other users. These identities are needed to enable the practice of healthcare. These identities are identities under general privacy regulations, and thus must consider Privacy risk. Often access to these other identities are covered by business relationships. For this purpose access to these Resources will tend to be Role specific using methods such as RBAC or ABAC.";
            case PATIENT: return "These Resources make up the bulk of FHIR and therefore are the most commonly understood. These Resources contain highly sesitive health information, or are closely linked to highly sensitive health information. These Resources will often use the security labels to differentiate various confidentiality levels within this broad group of Patient Sensitive data. Access to these Resources often requires a declared Purpose Of Use. Access to these Resources is often controlled by a Privacy Consent.";
            case NOTCLASSIFIED: return "Some Resources can be used for a wide scope of use-cases that span very sensitive to very non-sensitive. These Resources do not fall into any of the above classifications, as their sensitivity is highly variable. These Resources will need special handling. These Resources often contain metadata that describes the content in a way that can be used for Access Control decisions.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ANONYMOUS: return "Anonymous READ Access Resource";
            case BUSINESS: return "Business Sensitive Resource";
            case INDIVIDUAL: return "Individual Sensitive Resource";
            case PATIENT: return "Patient Sensitive";
            case NOTCLASSIFIED: return "Not classified";
            default: return "?";
          }
    }


}

