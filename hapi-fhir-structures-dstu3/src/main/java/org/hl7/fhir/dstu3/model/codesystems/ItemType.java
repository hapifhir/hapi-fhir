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

public enum ItemType {

        /**
         * An item with no direct answer but should have at least one child item.
         */
        GROUP, 
        /**
         * Text for display that will not capture an answer or have child items.
         */
        DISPLAY, 
        /**
         * An item that defines a specific answer to be captured, and may have child items.
(the answer provided in the QuestionnaireResponse should be of the defined datatype)
         */
        QUESTION, 
        /**
         * Question with a yes/no answer (valueBoolean)
         */
        BOOLEAN, 
        /**
         * Question with is a real number answer (valueDecimal)
         */
        DECIMAL, 
        /**
         * Question with an integer answer (valueInteger)
         */
        INTEGER, 
        /**
         * Question with a date answer (valueDate)
         */
        DATE, 
        /**
         * Question with a date and time answer (valueDateTime)
         */
        DATETIME, 
        /**
         * Question with a time (hour:minute:second) answer independent of date. (valueTime)
         */
        TIME, 
        /**
         * Question with a short (few words to short sentence) free-text entry answer (valueString)
         */
        STRING, 
        /**
         * Question with a long (potentially multi-paragraph) free-text entry answer (valueString)
         */
        TEXT, 
        /**
         * Question with a URL (website, FTP site, etc.) answer (valueUri)
         */
        URL, 
        /**
         * Question with a Coding drawn from a list of options (specified in either the option property, or via the valueset referenced in the options property) as an answer (valueCoding)
         */
        CHOICE, 
        /**
         * Answer is a Coding drawn from a list of options (as with the choice type) or a free-text entry in a string (valueCoding or valueString)
         */
        OPENCHOICE, 
        /**
         * Question with binary content such as a image, PDF, etc. as an answer (valueAttachment)
         */
        ATTACHMENT, 
        /**
         * Question with a reference to another resource (practitioner, organization, etc.) as an answer (valueReference)
         */
        REFERENCE, 
        /**
         * Question with a combination of a numeric value and unit, potentially with a comparator (<, >, etc.) as an answer. (valueQuantity)
There is an extension 'http://hl7.org/fhir/StructureDefinition/questionnaire-unit' that can be used to define what unit whould be captured (or the a unit that has a ucum conversion from the provided unit)
         */
        QUANTITY, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ItemType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("group".equals(codeString))
          return GROUP;
        if ("display".equals(codeString))
          return DISPLAY;
        if ("question".equals(codeString))
          return QUESTION;
        if ("boolean".equals(codeString))
          return BOOLEAN;
        if ("decimal".equals(codeString))
          return DECIMAL;
        if ("integer".equals(codeString))
          return INTEGER;
        if ("date".equals(codeString))
          return DATE;
        if ("dateTime".equals(codeString))
          return DATETIME;
        if ("time".equals(codeString))
          return TIME;
        if ("string".equals(codeString))
          return STRING;
        if ("text".equals(codeString))
          return TEXT;
        if ("url".equals(codeString))
          return URL;
        if ("choice".equals(codeString))
          return CHOICE;
        if ("open-choice".equals(codeString))
          return OPENCHOICE;
        if ("attachment".equals(codeString))
          return ATTACHMENT;
        if ("reference".equals(codeString))
          return REFERENCE;
        if ("quantity".equals(codeString))
          return QUANTITY;
        throw new FHIRException("Unknown ItemType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case GROUP: return "group";
            case DISPLAY: return "display";
            case QUESTION: return "question";
            case BOOLEAN: return "boolean";
            case DECIMAL: return "decimal";
            case INTEGER: return "integer";
            case DATE: return "date";
            case DATETIME: return "dateTime";
            case TIME: return "time";
            case STRING: return "string";
            case TEXT: return "text";
            case URL: return "url";
            case CHOICE: return "choice";
            case OPENCHOICE: return "open-choice";
            case ATTACHMENT: return "attachment";
            case REFERENCE: return "reference";
            case QUANTITY: return "quantity";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/item-type";
        }
        public String getDefinition() {
          switch (this) {
            case GROUP: return "An item with no direct answer but should have at least one child item.";
            case DISPLAY: return "Text for display that will not capture an answer or have child items.";
            case QUESTION: return "An item that defines a specific answer to be captured, and may have child items.\n(the answer provided in the QuestionnaireResponse should be of the defined datatype)";
            case BOOLEAN: return "Question with a yes/no answer (valueBoolean)";
            case DECIMAL: return "Question with is a real number answer (valueDecimal)";
            case INTEGER: return "Question with an integer answer (valueInteger)";
            case DATE: return "Question with a date answer (valueDate)";
            case DATETIME: return "Question with a date and time answer (valueDateTime)";
            case TIME: return "Question with a time (hour:minute:second) answer independent of date. (valueTime)";
            case STRING: return "Question with a short (few words to short sentence) free-text entry answer (valueString)";
            case TEXT: return "Question with a long (potentially multi-paragraph) free-text entry answer (valueString)";
            case URL: return "Question with a URL (website, FTP site, etc.) answer (valueUri)";
            case CHOICE: return "Question with a Coding drawn from a list of options (specified in either the option property, or via the valueset referenced in the options property) as an answer (valueCoding)";
            case OPENCHOICE: return "Answer is a Coding drawn from a list of options (as with the choice type) or a free-text entry in a string (valueCoding or valueString)";
            case ATTACHMENT: return "Question with binary content such as a image, PDF, etc. as an answer (valueAttachment)";
            case REFERENCE: return "Question with a reference to another resource (practitioner, organization, etc.) as an answer (valueReference)";
            case QUANTITY: return "Question with a combination of a numeric value and unit, potentially with a comparator (<, >, etc.) as an answer. (valueQuantity)\nThere is an extension 'http://hl7.org/fhir/StructureDefinition/questionnaire-unit' that can be used to define what unit whould be captured (or the a unit that has a ucum conversion from the provided unit)";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case GROUP: return "Group";
            case DISPLAY: return "Display";
            case QUESTION: return "Question";
            case BOOLEAN: return "Boolean";
            case DECIMAL: return "Decimal";
            case INTEGER: return "Integer";
            case DATE: return "Date";
            case DATETIME: return "Date Time";
            case TIME: return "Time";
            case STRING: return "String";
            case TEXT: return "Text";
            case URL: return "Url";
            case CHOICE: return "Choice";
            case OPENCHOICE: return "Open Choice";
            case ATTACHMENT: return "Attachment";
            case REFERENCE: return "Reference";
            case QUANTITY: return "Quantity";
            default: return "?";
          }
    }


}

