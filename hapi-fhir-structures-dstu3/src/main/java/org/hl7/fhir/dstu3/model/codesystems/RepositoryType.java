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

public enum RepositoryType {

        /**
         * When URL is clicked, the resource can be seen directly (by webpage or by download link format)
         */
        DIRECTLINK, 
        /**
         * When the API method (e.g. [base_url]/[parameter]) related with the URL of the website is executed, the resource can be seen directly (usually in JSON or XML format)
         */
        OPENAPI, 
        /**
         * When logged into the website, the resource can be seen.
         */
        LOGIN, 
        /**
         * When logged in and  follow the API in the website related with URL, the resource can be seen.
         */
        OAUTH, 
        /**
         * Some other complicated or particular way to get resource from URL.
         */
        OTHER, 
        /**
         * added to help the parsers
         */
        NULL;
        public static RepositoryType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("directlink".equals(codeString))
          return DIRECTLINK;
        if ("openapi".equals(codeString))
          return OPENAPI;
        if ("login".equals(codeString))
          return LOGIN;
        if ("oauth".equals(codeString))
          return OAUTH;
        if ("other".equals(codeString))
          return OTHER;
        throw new FHIRException("Unknown RepositoryType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case DIRECTLINK: return "directlink";
            case OPENAPI: return "openapi";
            case LOGIN: return "login";
            case OAUTH: return "oauth";
            case OTHER: return "other";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/repository-type";
        }
        public String getDefinition() {
          switch (this) {
            case DIRECTLINK: return "When URL is clicked, the resource can be seen directly (by webpage or by download link format)";
            case OPENAPI: return "When the API method (e.g. [base_url]/[parameter]) related with the URL of the website is executed, the resource can be seen directly (usually in JSON or XML format)";
            case LOGIN: return "When logged into the website, the resource can be seen.";
            case OAUTH: return "When logged in and  follow the API in the website related with URL, the resource can be seen.";
            case OTHER: return "Some other complicated or particular way to get resource from URL.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case DIRECTLINK: return "Click and see";
            case OPENAPI: return "The URL is the RESTful or other kind of API that can access to the result.";
            case LOGIN: return "Result cannot be access unless an account is logged in";
            case OAUTH: return "Result need to be fetched with API and need LOGIN( or cookies are required when visiting the link of resource)";
            case OTHER: return "Some other complicated or particular way to get resource from URL.";
            default: return "?";
          }
    }


}

