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

public enum V3Hl7CMETAttribution {

        /**
         * Description: Provides sufficient information to allow the object identified to be contacted. This is likely to have the content of identified and confirmable plus telephone number.
         */
        CONTACT, 
        /**
         * Description: This variant is a proper subset of universal and is intended to provide sufficient information to identify the object(s) modeled by the CMET. This variant is only suitable for use within TIGHTLY COUPLED SYSTEMS ONLY. This variant provides ONLY the ID (and code where applicable) and Name. Other variants may not be substituted at runtime.
         */
        IDENTIFIED, 
        /**
         * Description: This extends the identified variant by adding just sufficient additional information to allow the identity of object modeled to be confirmed by a number of corroborating items of data; for instance a patient's date of birth and current address. However, specific contact information, such as telephone number, are not viewed as confirming information.
         */
        IDENTIFIEDCONFIRMABLE, 
        /**
         * Description: Generally the same information content as "contactable" but using new "informational" CMETs as dependant CMETs. This flavor allows expression of the CMET when non-focal class information is not known.
         */
        IDENTIFIEDINFORMATIONAL, 
        /**
         * Description: Generally the same information content as "contactable", but with required (not mandatory) ids on entry point class. This flavor allows expression of the CMET even when mandatory information is not known.
         */
        INFORMATIONAL, 
        /**
         * Description: Provides more than identified, but not as much as universal. There are not expected to be many of these.
         */
        MINIMAL, 
        /**
         * Description: This variant includes all attributes and associations present in the R-MIM. Any of non-mandatory and non-required attributes and/or associations may be present or absent, as permitted in the cardinality constraints.
         */
        UNIVERSAL, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3Hl7CMETAttribution fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("contact".equals(codeString))
          return CONTACT;
        if ("identified".equals(codeString))
          return IDENTIFIED;
        if ("identified-confirmable".equals(codeString))
          return IDENTIFIEDCONFIRMABLE;
        if ("identified-informational".equals(codeString))
          return IDENTIFIEDINFORMATIONAL;
        if ("informational".equals(codeString))
          return INFORMATIONAL;
        if ("minimal".equals(codeString))
          return MINIMAL;
        if ("universal".equals(codeString))
          return UNIVERSAL;
        throw new FHIRException("Unknown V3Hl7CMETAttribution code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case CONTACT: return "contact";
            case IDENTIFIED: return "identified";
            case IDENTIFIEDCONFIRMABLE: return "identified-confirmable";
            case IDENTIFIEDINFORMATIONAL: return "identified-informational";
            case INFORMATIONAL: return "informational";
            case MINIMAL: return "minimal";
            case UNIVERSAL: return "universal";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://terminology.hl7.org/CodeSystem/v3-hl7CMETAttribution";
        }
        public String getDefinition() {
          switch (this) {
            case CONTACT: return "Description: Provides sufficient information to allow the object identified to be contacted. This is likely to have the content of identified and confirmable plus telephone number.";
            case IDENTIFIED: return "Description: This variant is a proper subset of universal and is intended to provide sufficient information to identify the object(s) modeled by the CMET. This variant is only suitable for use within TIGHTLY COUPLED SYSTEMS ONLY. This variant provides ONLY the ID (and code where applicable) and Name. Other variants may not be substituted at runtime.";
            case IDENTIFIEDCONFIRMABLE: return "Description: This extends the identified variant by adding just sufficient additional information to allow the identity of object modeled to be confirmed by a number of corroborating items of data; for instance a patient's date of birth and current address. However, specific contact information, such as telephone number, are not viewed as confirming information.";
            case IDENTIFIEDINFORMATIONAL: return "Description: Generally the same information content as \"contactable\" but using new \"informational\" CMETs as dependant CMETs. This flavor allows expression of the CMET when non-focal class information is not known.";
            case INFORMATIONAL: return "Description: Generally the same information content as \"contactable\", but with required (not mandatory) ids on entry point class. This flavor allows expression of the CMET even when mandatory information is not known.";
            case MINIMAL: return "Description: Provides more than identified, but not as much as universal. There are not expected to be many of these.";
            case UNIVERSAL: return "Description: This variant includes all attributes and associations present in the R-MIM. Any of non-mandatory and non-required attributes and/or associations may be present or absent, as permitted in the cardinality constraints.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case CONTACT: return "contact";
            case IDENTIFIED: return "identified";
            case IDENTIFIEDCONFIRMABLE: return "identified-confirmable";
            case IDENTIFIEDINFORMATIONAL: return "identified-informational";
            case INFORMATIONAL: return "informational";
            case MINIMAL: return "minimal";
            case UNIVERSAL: return "universal";
            default: return "?";
          }
    }


}

