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

public enum SmartCapabilities {

        /**
         * support for SMART’s EHR Launch mode.
         */
        LAUNCHEHR, 
        /**
         * support for SMART’s Standalone Launch mode.
         */
        LAUNCHSTANDALONE, 
        /**
         * support for SMART’s public client profile (no client authentication).
         */
        CLIENTPUBLIC, 
        /**
         * support for SMART’s confidential client profile (symmetric client secret authentication).
         */
        CLIENTCONFIDENTIALSYMMETRIC, 
        /**
         * support for SMART’s OpenID Connect profile.
         */
        SSOOPENIDCONNECT, 
        /**
         * support for “need patient banner” launch context (conveyed via need_patient_banner token parameter).
         */
        CONTEXTPASSTHROUGHBANNER, 
        /**
         * support for “SMART style URL” launch context (conveyed via smart_style_url token parameter).
         */
        CONTEXTPASSTHROUGHSTYLE, 
        /**
         * support for patient-level launch context (requested by launch/patient scope, conveyed via patient token parameter).
         */
        CONTEXTEHRPATIENT, 
        /**
         * support for encounter-level launch context (requested by launch/encounter scope, conveyed via encounter token parameter).
         */
        CONTEXTEHRENCOUNTER, 
        /**
         * support for patient-level launch context (requested by launch/patient scope, conveyed via patient token parameter).
         */
        CONTEXTSTANDALONEPATIENT, 
        /**
         * support for encounter-level launch context (requested by launch/encounter scope, conveyed via encounter token parameter).
         */
        CONTEXTSTANDALONEENCOUNTER, 
        /**
         * support for refresh tokens (requested by offline_access scope).
         */
        PERMISSIONOFFLINE, 
        /**
         * support for patient-level scopes (e.g. patient/Observation.read).
         */
        PERMISSIONPATIENT, 
        /**
         * support for user-level scopes (e.g. user/Appointment.read).
         */
        PERMISSIONUSER, 
        /**
         * added to help the parsers
         */
        NULL;
        public static SmartCapabilities fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("launch-ehr".equals(codeString))
          return LAUNCHEHR;
        if ("launch-standalone".equals(codeString))
          return LAUNCHSTANDALONE;
        if ("client-public".equals(codeString))
          return CLIENTPUBLIC;
        if ("client-confidential-symmetric".equals(codeString))
          return CLIENTCONFIDENTIALSYMMETRIC;
        if ("sso-openid-connect".equals(codeString))
          return SSOOPENIDCONNECT;
        if ("context-passthrough-banner".equals(codeString))
          return CONTEXTPASSTHROUGHBANNER;
        if ("context-passthrough-style".equals(codeString))
          return CONTEXTPASSTHROUGHSTYLE;
        if ("context-ehr-patient".equals(codeString))
          return CONTEXTEHRPATIENT;
        if ("context-ehr-encounter".equals(codeString))
          return CONTEXTEHRENCOUNTER;
        if ("context-standalone-patient".equals(codeString))
          return CONTEXTSTANDALONEPATIENT;
        if ("context-standalone-encounter".equals(codeString))
          return CONTEXTSTANDALONEENCOUNTER;
        if ("permission-offline".equals(codeString))
          return PERMISSIONOFFLINE;
        if ("permission-patient".equals(codeString))
          return PERMISSIONPATIENT;
        if ("permission-user".equals(codeString))
          return PERMISSIONUSER;
        throw new FHIRException("Unknown SmartCapabilities code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case LAUNCHEHR: return "launch-ehr";
            case LAUNCHSTANDALONE: return "launch-standalone";
            case CLIENTPUBLIC: return "client-public";
            case CLIENTCONFIDENTIALSYMMETRIC: return "client-confidential-symmetric";
            case SSOOPENIDCONNECT: return "sso-openid-connect";
            case CONTEXTPASSTHROUGHBANNER: return "context-passthrough-banner";
            case CONTEXTPASSTHROUGHSTYLE: return "context-passthrough-style";
            case CONTEXTEHRPATIENT: return "context-ehr-patient";
            case CONTEXTEHRENCOUNTER: return "context-ehr-encounter";
            case CONTEXTSTANDALONEPATIENT: return "context-standalone-patient";
            case CONTEXTSTANDALONEENCOUNTER: return "context-standalone-encounter";
            case PERMISSIONOFFLINE: return "permission-offline";
            case PERMISSIONPATIENT: return "permission-patient";
            case PERMISSIONUSER: return "permission-user";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://terminology.hl7.org/CodeSystem/smart-capabilities";
        }
        public String getDefinition() {
          switch (this) {
            case LAUNCHEHR: return "support for SMART’s EHR Launch mode.";
            case LAUNCHSTANDALONE: return "support for SMART’s Standalone Launch mode.";
            case CLIENTPUBLIC: return "support for SMART’s public client profile (no client authentication).";
            case CLIENTCONFIDENTIALSYMMETRIC: return "support for SMART’s confidential client profile (symmetric client secret authentication).";
            case SSOOPENIDCONNECT: return "support for SMART’s OpenID Connect profile.";
            case CONTEXTPASSTHROUGHBANNER: return "support for “need patient banner” launch context (conveyed via need_patient_banner token parameter).";
            case CONTEXTPASSTHROUGHSTYLE: return "support for “SMART style URL” launch context (conveyed via smart_style_url token parameter).";
            case CONTEXTEHRPATIENT: return "support for patient-level launch context (requested by launch/patient scope, conveyed via patient token parameter).";
            case CONTEXTEHRENCOUNTER: return "support for encounter-level launch context (requested by launch/encounter scope, conveyed via encounter token parameter).";
            case CONTEXTSTANDALONEPATIENT: return "support for patient-level launch context (requested by launch/patient scope, conveyed via patient token parameter).";
            case CONTEXTSTANDALONEENCOUNTER: return "support for encounter-level launch context (requested by launch/encounter scope, conveyed via encounter token parameter).";
            case PERMISSIONOFFLINE: return "support for refresh tokens (requested by offline_access scope).";
            case PERMISSIONPATIENT: return "support for patient-level scopes (e.g. patient/Observation.read).";
            case PERMISSIONUSER: return "support for user-level scopes (e.g. user/Appointment.read).";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case LAUNCHEHR: return "EHR Launch Mode";
            case LAUNCHSTANDALONE: return "Standalone Launch Mode";
            case CLIENTPUBLIC: return "Public Client Profile";
            case CLIENTCONFIDENTIALSYMMETRIC: return "Confidential Client Profile";
            case SSOOPENIDCONNECT: return "Supports OpenID Connect";
            case CONTEXTPASSTHROUGHBANNER: return "Allows \"Need Patient Banner\"";
            case CONTEXTPASSTHROUGHSTYLE: return "Allows \"Smart Style Style\"";
            case CONTEXTEHRPATIENT: return "Allows \"Patient Level Launch Context (EHR)\"";
            case CONTEXTEHRENCOUNTER: return "Allows \"Encounter Level Launch Context (EHR)\"";
            case CONTEXTSTANDALONEPATIENT: return "Allows \"Patient Level Launch Context (STANDALONE)\"";
            case CONTEXTSTANDALONEENCOUNTER: return "Allows \"Encounter Level Launch Context (STANDALONE)\"";
            case PERMISSIONOFFLINE: return "Supports Refresh Token";
            case PERMISSIONPATIENT: return "Supports Patient Level Scopes";
            case PERMISSIONUSER: return "Supports User Level Scopes";
            default: return "?";
          }
    }


}

