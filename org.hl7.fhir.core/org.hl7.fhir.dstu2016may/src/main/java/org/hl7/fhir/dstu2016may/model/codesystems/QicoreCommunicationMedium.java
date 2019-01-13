package org.hl7.fhir.dstu2016may.model.codesystems;

/*-
 * #%L
 * org.hl7.fhir.dstu2016may
 * %%
 * Copyright (C) 2014 - 2019 Health Level 7
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


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

// Generated on Sun, May 8, 2016 03:05+1000 for FHIR v1.4.0


import org.hl7.fhir.exceptions.FHIRException;

public enum QicoreCommunicationMedium {

        /**
         * The communication medium has not been specified
         */
        UNSPECIFIED, 
        /**
         * The message was communicated via telephone
         */
        TELEPHONE, 
        /**
         * The message was sent via a fax transmission
         */
        FAX, 
        /**
         * The message was communicated via a medical device
         */
        DEVICE, 
        /**
         * The message was communicated via a video call
         */
        VIDEO, 
        /**
         * The message was left on the recipient's voicemail system
         */
        VOICEMAIL, 
        /**
         * The message was sent via text message (SMS)
         */
        TEXT, 
        /**
         * The message was communicated via a social media platform
         */
        SOCIALMEDIA, 
        /**
         * The message was communicated in person
         */
        INPERSON, 
        /**
         * The message was posted via conventional mail
         */
        MAIL, 
        /**
         * The message was sent as an email
         */
        EMAIL, 
        /**
         * The message was communicated via a patient portal
         */
        PORTAL, 
        /**
         * added to help the parsers
         */
        NULL;
        public static QicoreCommunicationMedium fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("unspecified".equals(codeString))
          return UNSPECIFIED;
        if ("telephone".equals(codeString))
          return TELEPHONE;
        if ("fax".equals(codeString))
          return FAX;
        if ("device".equals(codeString))
          return DEVICE;
        if ("video".equals(codeString))
          return VIDEO;
        if ("voicemail".equals(codeString))
          return VOICEMAIL;
        if ("text".equals(codeString))
          return TEXT;
        if ("social-media".equals(codeString))
          return SOCIALMEDIA;
        if ("in-person".equals(codeString))
          return INPERSON;
        if ("mail".equals(codeString))
          return MAIL;
        if ("email".equals(codeString))
          return EMAIL;
        if ("portal".equals(codeString))
          return PORTAL;
        throw new FHIRException("Unknown QicoreCommunicationMedium code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case UNSPECIFIED: return "unspecified";
            case TELEPHONE: return "telephone";
            case FAX: return "fax";
            case DEVICE: return "device";
            case VIDEO: return "video";
            case VOICEMAIL: return "voicemail";
            case TEXT: return "text";
            case SOCIALMEDIA: return "social-media";
            case INPERSON: return "in-person";
            case MAIL: return "mail";
            case EMAIL: return "email";
            case PORTAL: return "portal";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/qicore-communication-medium";
        }
        public String getDefinition() {
          switch (this) {
            case UNSPECIFIED: return "The communication medium has not been specified";
            case TELEPHONE: return "The message was communicated via telephone";
            case FAX: return "The message was sent via a fax transmission";
            case DEVICE: return "The message was communicated via a medical device";
            case VIDEO: return "The message was communicated via a video call";
            case VOICEMAIL: return "The message was left on the recipient's voicemail system";
            case TEXT: return "The message was sent via text message (SMS)";
            case SOCIALMEDIA: return "The message was communicated via a social media platform";
            case INPERSON: return "The message was communicated in person";
            case MAIL: return "The message was posted via conventional mail";
            case EMAIL: return "The message was sent as an email";
            case PORTAL: return "The message was communicated via a patient portal";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case UNSPECIFIED: return "Not Specified";
            case TELEPHONE: return "Telephone";
            case FAX: return "Facsimile (fax)";
            case DEVICE: return "Via medical device";
            case VIDEO: return "Video call";
            case VOICEMAIL: return "Voice message (voicemail)";
            case TEXT: return "Text message (SMS)";
            case SOCIALMEDIA: return "Social media";
            case INPERSON: return "In person";
            case MAIL: return "Mail (conventional)";
            case EMAIL: return "Electronic mail (email)";
            case PORTAL: return "Patient portal";
            default: return "?";
          }
    }


}

