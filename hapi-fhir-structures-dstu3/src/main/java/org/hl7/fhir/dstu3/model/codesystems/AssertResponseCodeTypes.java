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

public enum AssertResponseCodeTypes {

        /**
         * Response code is 200.
         */
        OKAY, 
        /**
         * Response code is 201.
         */
        CREATED, 
        /**
         * Response code is 204.
         */
        NOCONTENT, 
        /**
         * Response code is 304.
         */
        NOTMODIFIED, 
        /**
         * Response code is 400.
         */
        BAD, 
        /**
         * Response code is 403.
         */
        FORBIDDEN, 
        /**
         * Response code is 404.
         */
        NOTFOUND, 
        /**
         * Response code is 405.
         */
        METHODNOTALLOWED, 
        /**
         * Response code is 409.
         */
        CONFLICT, 
        /**
         * Response code is 410.
         */
        GONE, 
        /**
         * Response code is 412.
         */
        PRECONDITIONFAILED, 
        /**
         * Response code is 422.
         */
        UNPROCESSABLE, 
        /**
         * added to help the parsers
         */
        NULL;
        public static AssertResponseCodeTypes fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("okay".equals(codeString))
          return OKAY;
        if ("created".equals(codeString))
          return CREATED;
        if ("noContent".equals(codeString))
          return NOCONTENT;
        if ("notModified".equals(codeString))
          return NOTMODIFIED;
        if ("bad".equals(codeString))
          return BAD;
        if ("forbidden".equals(codeString))
          return FORBIDDEN;
        if ("notFound".equals(codeString))
          return NOTFOUND;
        if ("methodNotAllowed".equals(codeString))
          return METHODNOTALLOWED;
        if ("conflict".equals(codeString))
          return CONFLICT;
        if ("gone".equals(codeString))
          return GONE;
        if ("preconditionFailed".equals(codeString))
          return PRECONDITIONFAILED;
        if ("unprocessable".equals(codeString))
          return UNPROCESSABLE;
        throw new FHIRException("Unknown AssertResponseCodeTypes code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case OKAY: return "okay";
            case CREATED: return "created";
            case NOCONTENT: return "noContent";
            case NOTMODIFIED: return "notModified";
            case BAD: return "bad";
            case FORBIDDEN: return "forbidden";
            case NOTFOUND: return "notFound";
            case METHODNOTALLOWED: return "methodNotAllowed";
            case CONFLICT: return "conflict";
            case GONE: return "gone";
            case PRECONDITIONFAILED: return "preconditionFailed";
            case UNPROCESSABLE: return "unprocessable";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/assert-response-code-types";
        }
        public String getDefinition() {
          switch (this) {
            case OKAY: return "Response code is 200.";
            case CREATED: return "Response code is 201.";
            case NOCONTENT: return "Response code is 204.";
            case NOTMODIFIED: return "Response code is 304.";
            case BAD: return "Response code is 400.";
            case FORBIDDEN: return "Response code is 403.";
            case NOTFOUND: return "Response code is 404.";
            case METHODNOTALLOWED: return "Response code is 405.";
            case CONFLICT: return "Response code is 409.";
            case GONE: return "Response code is 410.";
            case PRECONDITIONFAILED: return "Response code is 412.";
            case UNPROCESSABLE: return "Response code is 422.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case OKAY: return "okay";
            case CREATED: return "created";
            case NOCONTENT: return "noContent";
            case NOTMODIFIED: return "notModified";
            case BAD: return "bad";
            case FORBIDDEN: return "forbidden";
            case NOTFOUND: return "notFound";
            case METHODNOTALLOWED: return "methodNotAllowed";
            case CONFLICT: return "conflict";
            case GONE: return "gone";
            case PRECONDITIONFAILED: return "preconditionFailed";
            case UNPROCESSABLE: return "unprocessable";
            default: return "?";
          }
    }


}

