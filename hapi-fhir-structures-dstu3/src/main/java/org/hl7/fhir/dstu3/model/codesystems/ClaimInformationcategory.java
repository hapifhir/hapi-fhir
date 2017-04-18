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

public enum ClaimInformationcategory {

        /**
         * Codes conveying additional situation and condition information.
         */
        INFO, 
        /**
         * Discharge status and discharge to locations.
         */
        DISCHARGE, 
        /**
         * Period, start or end dates of aspects of the Condition.
         */
        ONSET, 
        /**
         * Nature and date of the related event eg. Last exam, service, Xray etc.
         */
        RELATED, 
        /**
         * Insurance policy exceptions.
         */
        EXCEPTION, 
        /**
         * Materials being forwarded, eg. Models, molds, images, documents.
         */
        MATERIAL, 
        /**
         * Materials attached such as images, documents and resources.
         */
        ATTACHMENT, 
        /**
         * Teeth which are missing for any reason, for example: prior extraction, never developed.
         */
        MISSINGTOOTH, 
        /**
         * The type of prosthesis and date of supply if a previously supplied prosthesis.
         */
        PROSTHESIS, 
        /**
         * Other information identified by the type.system.
         */
        OTHER, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ClaimInformationcategory fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("info".equals(codeString))
          return INFO;
        if ("discharge".equals(codeString))
          return DISCHARGE;
        if ("onset".equals(codeString))
          return ONSET;
        if ("related".equals(codeString))
          return RELATED;
        if ("exception".equals(codeString))
          return EXCEPTION;
        if ("material".equals(codeString))
          return MATERIAL;
        if ("attachment".equals(codeString))
          return ATTACHMENT;
        if ("missingtooth".equals(codeString))
          return MISSINGTOOTH;
        if ("prosthesis".equals(codeString))
          return PROSTHESIS;
        if ("other".equals(codeString))
          return OTHER;
        throw new FHIRException("Unknown ClaimInformationcategory code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case INFO: return "info";
            case DISCHARGE: return "discharge";
            case ONSET: return "onset";
            case RELATED: return "related";
            case EXCEPTION: return "exception";
            case MATERIAL: return "material";
            case ATTACHMENT: return "attachment";
            case MISSINGTOOTH: return "missingtooth";
            case PROSTHESIS: return "prosthesis";
            case OTHER: return "other";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/claiminformationcategory";
        }
        public String getDefinition() {
          switch (this) {
            case INFO: return "Codes conveying additional situation and condition information.";
            case DISCHARGE: return "Discharge status and discharge to locations.";
            case ONSET: return "Period, start or end dates of aspects of the Condition.";
            case RELATED: return "Nature and date of the related event eg. Last exam, service, Xray etc.";
            case EXCEPTION: return "Insurance policy exceptions.";
            case MATERIAL: return "Materials being forwarded, eg. Models, molds, images, documents.";
            case ATTACHMENT: return "Materials attached such as images, documents and resources.";
            case MISSINGTOOTH: return "Teeth which are missing for any reason, for example: prior extraction, never developed.";
            case PROSTHESIS: return "The type of prosthesis and date of supply if a previously supplied prosthesis.";
            case OTHER: return "Other information identified by the type.system.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case INFO: return "Information";
            case DISCHARGE: return "Discharge";
            case ONSET: return "Onset";
            case RELATED: return "Related Services";
            case EXCEPTION: return "Exception";
            case MATERIAL: return "Materials Forwarded";
            case ATTACHMENT: return "Attachment";
            case MISSINGTOOTH: return "Missing Tooth";
            case PROSTHESIS: return "Prosthesis";
            case OTHER: return "Other";
            default: return "?";
          }
    }


}

