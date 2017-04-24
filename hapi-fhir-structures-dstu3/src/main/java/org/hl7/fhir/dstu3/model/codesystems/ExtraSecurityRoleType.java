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

public enum ExtraSecurityRoleType {

        /**
         * An entity providing authorization services to enable the electronic sharing of health-related information based on resource owner's preapproved permissions. For example, an UMA Authorization Server[UMA]
         */
        AUTHSERVER, 
        /**
         * An entity that collects information over which the data subject may have certain rights under policy or law to control that information's management and distribution by data collectors, including the right to access, retrieve, distribute, or delete that information. 
         */
        DATACOLLECTOR, 
        /**
         * An entity that processes collected information over which the data subject may have certain rights under policy or law to control that information's management and distribution by data processors, including the right to access, retrieve, distribute, or delete that information.
         */
        DATAPROCESSOR, 
        /**
         * A person whose personal information is collected or processed, and who may have certain rights under policy or law to control that information's management and distribution by data collectors or processors, including the right to access, retrieve, distribute, or delete that information.
         */
        DATASUBJECT, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ExtraSecurityRoleType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("authserver".equals(codeString))
          return AUTHSERVER;
        if ("datacollector".equals(codeString))
          return DATACOLLECTOR;
        if ("dataprocessor".equals(codeString))
          return DATAPROCESSOR;
        if ("datasubject".equals(codeString))
          return DATASUBJECT;
        throw new FHIRException("Unknown ExtraSecurityRoleType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case AUTHSERVER: return "authserver";
            case DATACOLLECTOR: return "datacollector";
            case DATAPROCESSOR: return "dataprocessor";
            case DATASUBJECT: return "datasubject";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/extra-security-role-type";
        }
        public String getDefinition() {
          switch (this) {
            case AUTHSERVER: return "An entity providing authorization services to enable the electronic sharing of health-related information based on resource owner's preapproved permissions. For example, an UMA Authorization Server[UMA]";
            case DATACOLLECTOR: return "An entity that collects information over which the data subject may have certain rights under policy or law to control that information's management and distribution by data collectors, including the right to access, retrieve, distribute, or delete that information. ";
            case DATAPROCESSOR: return "An entity that processes collected information over which the data subject may have certain rights under policy or law to control that information's management and distribution by data processors, including the right to access, retrieve, distribute, or delete that information.";
            case DATASUBJECT: return "A person whose personal information is collected or processed, and who may have certain rights under policy or law to control that information's management and distribution by data collectors or processors, including the right to access, retrieve, distribute, or delete that information.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case AUTHSERVER: return "authorization server";
            case DATACOLLECTOR: return "data collector";
            case DATAPROCESSOR: return "data processor";
            case DATASUBJECT: return "data subject";
            default: return "?";
          }
    }


}

