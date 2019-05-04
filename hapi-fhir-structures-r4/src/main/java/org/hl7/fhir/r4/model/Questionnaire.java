package org.hl7.fhir.r4.model;

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

import java.util.*;

import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.r4.model.Enumerations.*;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ChildOrder;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Block;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.exceptions.FHIRException;
/**
 * A structured set of questions intended to guide the collection of answers from end-users. Questionnaires provide detailed control over order, presentation, phraseology and grouping to allow coherent, consistent data collection.
 */
@ResourceDef(name="Questionnaire", profile="http://hl7.org/fhir/StructureDefinition/Questionnaire")
@ChildOrder(names={"url", "identifier", "version", "name", "title", "derivedFrom", "status", "experimental", "subjectType", "date", "publisher", "contact", "description", "useContext", "jurisdiction", "purpose", "copyright", "approvalDate", "lastReviewDate", "effectivePeriod", "code", "item"})
public class Questionnaire extends MetadataResource {

    public enum QuestionnaireItemType {
        /**
         * An item with no direct answer but should have at least one child item.
         */
        GROUP, 
        /**
         * Text for display that will not capture an answer or have child items.
         */
        DISPLAY, 
        /**
         * An item that defines a specific answer to be captured, and which may have child items. (the answer provided in the QuestionnaireResponse should be of the defined datatype).
         */
        QUESTION, 
        /**
         * Question with a yes/no answer (valueBoolean).
         */
        BOOLEAN, 
        /**
         * Question with is a real number answer (valueDecimal).
         */
        DECIMAL, 
        /**
         * Question with an integer answer (valueInteger).
         */
        INTEGER, 
        /**
         * Question with a date answer (valueDate).
         */
        DATE, 
        /**
         * Question with a date and time answer (valueDateTime).
         */
        DATETIME, 
        /**
         * Question with a time (hour:minute:second) answer independent of date. (valueTime).
         */
        TIME, 
        /**
         * Question with a short (few words to short sentence) free-text entry answer (valueString).
         */
        STRING, 
        /**
         * Question with a long (potentially multi-paragraph) free-text entry answer (valueString).
         */
        TEXT, 
        /**
         * Question with a URL (website, FTP site, etc.) answer (valueUri).
         */
        URL, 
        /**
         * Question with a Coding drawn from a list of possible answers (specified in either the answerOption property, or via the valueset referenced in the answerValueSet property) as an answer (valueCoding).
         */
        CHOICE, 
        /**
         * Answer is a Coding drawn from a list of possible answers (as with the choice type) or a free-text entry in a string (valueCoding or valueString).
         */
        OPENCHOICE, 
        /**
         * Question with binary content such as an image, PDF, etc. as an answer (valueAttachment).
         */
        ATTACHMENT, 
        /**
         * Question with a reference to another resource (practitioner, organization, etc.) as an answer (valueReference).
         */
        REFERENCE, 
        /**
         * Question with a combination of a numeric value and unit, potentially with a comparator (<, >, etc.) as an answer. (valueQuantity) There is an extension 'http://hl7.org/fhir/StructureDefinition/questionnaire-unit' that can be used to define what unit should be captured (or the unit that has a ucum conversion from the provided unit).
         */
        QUANTITY, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static QuestionnaireItemType fromCode(String codeString) throws FHIRException {
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
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown QuestionnaireItemType code '"+codeString+"'");
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
          switch (this) {
            case GROUP: return "http://hl7.org/fhir/item-type";
            case DISPLAY: return "http://hl7.org/fhir/item-type";
            case QUESTION: return "http://hl7.org/fhir/item-type";
            case BOOLEAN: return "http://hl7.org/fhir/item-type";
            case DECIMAL: return "http://hl7.org/fhir/item-type";
            case INTEGER: return "http://hl7.org/fhir/item-type";
            case DATE: return "http://hl7.org/fhir/item-type";
            case DATETIME: return "http://hl7.org/fhir/item-type";
            case TIME: return "http://hl7.org/fhir/item-type";
            case STRING: return "http://hl7.org/fhir/item-type";
            case TEXT: return "http://hl7.org/fhir/item-type";
            case URL: return "http://hl7.org/fhir/item-type";
            case CHOICE: return "http://hl7.org/fhir/item-type";
            case OPENCHOICE: return "http://hl7.org/fhir/item-type";
            case ATTACHMENT: return "http://hl7.org/fhir/item-type";
            case REFERENCE: return "http://hl7.org/fhir/item-type";
            case QUANTITY: return "http://hl7.org/fhir/item-type";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case GROUP: return "An item with no direct answer but should have at least one child item.";
            case DISPLAY: return "Text for display that will not capture an answer or have child items.";
            case QUESTION: return "An item that defines a specific answer to be captured, and which may have child items. (the answer provided in the QuestionnaireResponse should be of the defined datatype).";
            case BOOLEAN: return "Question with a yes/no answer (valueBoolean).";
            case DECIMAL: return "Question with is a real number answer (valueDecimal).";
            case INTEGER: return "Question with an integer answer (valueInteger).";
            case DATE: return "Question with a date answer (valueDate).";
            case DATETIME: return "Question with a date and time answer (valueDateTime).";
            case TIME: return "Question with a time (hour:minute:second) answer independent of date. (valueTime).";
            case STRING: return "Question with a short (few words to short sentence) free-text entry answer (valueString).";
            case TEXT: return "Question with a long (potentially multi-paragraph) free-text entry answer (valueString).";
            case URL: return "Question with a URL (website, FTP site, etc.) answer (valueUri).";
            case CHOICE: return "Question with a Coding drawn from a list of possible answers (specified in either the answerOption property, or via the valueset referenced in the answerValueSet property) as an answer (valueCoding).";
            case OPENCHOICE: return "Answer is a Coding drawn from a list of possible answers (as with the choice type) or a free-text entry in a string (valueCoding or valueString).";
            case ATTACHMENT: return "Question with binary content such as an image, PDF, etc. as an answer (valueAttachment).";
            case REFERENCE: return "Question with a reference to another resource (practitioner, organization, etc.) as an answer (valueReference).";
            case QUANTITY: return "Question with a combination of a numeric value and unit, potentially with a comparator (<, >, etc.) as an answer. (valueQuantity) There is an extension 'http://hl7.org/fhir/StructureDefinition/questionnaire-unit' that can be used to define what unit should be captured (or the unit that has a ucum conversion from the provided unit).";
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

  public static class QuestionnaireItemTypeEnumFactory implements EnumFactory<QuestionnaireItemType> {
    public QuestionnaireItemType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("group".equals(codeString))
          return QuestionnaireItemType.GROUP;
        if ("display".equals(codeString))
          return QuestionnaireItemType.DISPLAY;
        if ("question".equals(codeString))
          return QuestionnaireItemType.QUESTION;
        if ("boolean".equals(codeString))
          return QuestionnaireItemType.BOOLEAN;
        if ("decimal".equals(codeString))
          return QuestionnaireItemType.DECIMAL;
        if ("integer".equals(codeString))
          return QuestionnaireItemType.INTEGER;
        if ("date".equals(codeString))
          return QuestionnaireItemType.DATE;
        if ("dateTime".equals(codeString))
          return QuestionnaireItemType.DATETIME;
        if ("time".equals(codeString))
          return QuestionnaireItemType.TIME;
        if ("string".equals(codeString))
          return QuestionnaireItemType.STRING;
        if ("text".equals(codeString))
          return QuestionnaireItemType.TEXT;
        if ("url".equals(codeString))
          return QuestionnaireItemType.URL;
        if ("choice".equals(codeString))
          return QuestionnaireItemType.CHOICE;
        if ("open-choice".equals(codeString))
          return QuestionnaireItemType.OPENCHOICE;
        if ("attachment".equals(codeString))
          return QuestionnaireItemType.ATTACHMENT;
        if ("reference".equals(codeString))
          return QuestionnaireItemType.REFERENCE;
        if ("quantity".equals(codeString))
          return QuestionnaireItemType.QUANTITY;
        throw new IllegalArgumentException("Unknown QuestionnaireItemType code '"+codeString+"'");
        }
        public Enumeration<QuestionnaireItemType> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<QuestionnaireItemType>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("group".equals(codeString))
          return new Enumeration<QuestionnaireItemType>(this, QuestionnaireItemType.GROUP);
        if ("display".equals(codeString))
          return new Enumeration<QuestionnaireItemType>(this, QuestionnaireItemType.DISPLAY);
        if ("question".equals(codeString))
          return new Enumeration<QuestionnaireItemType>(this, QuestionnaireItemType.QUESTION);
        if ("boolean".equals(codeString))
          return new Enumeration<QuestionnaireItemType>(this, QuestionnaireItemType.BOOLEAN);
        if ("decimal".equals(codeString))
          return new Enumeration<QuestionnaireItemType>(this, QuestionnaireItemType.DECIMAL);
        if ("integer".equals(codeString))
          return new Enumeration<QuestionnaireItemType>(this, QuestionnaireItemType.INTEGER);
        if ("date".equals(codeString))
          return new Enumeration<QuestionnaireItemType>(this, QuestionnaireItemType.DATE);
        if ("dateTime".equals(codeString))
          return new Enumeration<QuestionnaireItemType>(this, QuestionnaireItemType.DATETIME);
        if ("time".equals(codeString))
          return new Enumeration<QuestionnaireItemType>(this, QuestionnaireItemType.TIME);
        if ("string".equals(codeString))
          return new Enumeration<QuestionnaireItemType>(this, QuestionnaireItemType.STRING);
        if ("text".equals(codeString))
          return new Enumeration<QuestionnaireItemType>(this, QuestionnaireItemType.TEXT);
        if ("url".equals(codeString))
          return new Enumeration<QuestionnaireItemType>(this, QuestionnaireItemType.URL);
        if ("choice".equals(codeString))
          return new Enumeration<QuestionnaireItemType>(this, QuestionnaireItemType.CHOICE);
        if ("open-choice".equals(codeString))
          return new Enumeration<QuestionnaireItemType>(this, QuestionnaireItemType.OPENCHOICE);
        if ("attachment".equals(codeString))
          return new Enumeration<QuestionnaireItemType>(this, QuestionnaireItemType.ATTACHMENT);
        if ("reference".equals(codeString))
          return new Enumeration<QuestionnaireItemType>(this, QuestionnaireItemType.REFERENCE);
        if ("quantity".equals(codeString))
          return new Enumeration<QuestionnaireItemType>(this, QuestionnaireItemType.QUANTITY);
        throw new FHIRException("Unknown QuestionnaireItemType code '"+codeString+"'");
        }
    public String toCode(QuestionnaireItemType code) {
      if (code == QuestionnaireItemType.GROUP)
        return "group";
      if (code == QuestionnaireItemType.DISPLAY)
        return "display";
      if (code == QuestionnaireItemType.QUESTION)
        return "question";
      if (code == QuestionnaireItemType.BOOLEAN)
        return "boolean";
      if (code == QuestionnaireItemType.DECIMAL)
        return "decimal";
      if (code == QuestionnaireItemType.INTEGER)
        return "integer";
      if (code == QuestionnaireItemType.DATE)
        return "date";
      if (code == QuestionnaireItemType.DATETIME)
        return "dateTime";
      if (code == QuestionnaireItemType.TIME)
        return "time";
      if (code == QuestionnaireItemType.STRING)
        return "string";
      if (code == QuestionnaireItemType.TEXT)
        return "text";
      if (code == QuestionnaireItemType.URL)
        return "url";
      if (code == QuestionnaireItemType.CHOICE)
        return "choice";
      if (code == QuestionnaireItemType.OPENCHOICE)
        return "open-choice";
      if (code == QuestionnaireItemType.ATTACHMENT)
        return "attachment";
      if (code == QuestionnaireItemType.REFERENCE)
        return "reference";
      if (code == QuestionnaireItemType.QUANTITY)
        return "quantity";
      return "?";
      }
    public String toSystem(QuestionnaireItemType code) {
      return code.getSystem();
      }
    }

    public enum QuestionnaireItemOperator {
        /**
         * True if whether an answer exists is equal to the enableWhen answer (which must be a boolean).
         */
        EXISTS, 
        /**
         * True if whether at least one answer has a value that is equal to the enableWhen answer.
         */
        EQUAL, 
        /**
         * True if whether at least no answer has a value that is equal to the enableWhen answer.
         */
        NOT_EQUAL, 
        /**
         * True if whether at least no answer has a value that is greater than the enableWhen answer.
         */
        GREATER_THAN, 
        /**
         * True if whether at least no answer has a value that is less than the enableWhen answer.
         */
        LESS_THAN, 
        /**
         * True if whether at least no answer has a value that is greater or equal to the enableWhen answer.
         */
        GREATER_OR_EQUAL, 
        /**
         * True if whether at least no answer has a value that is less or equal to the enableWhen answer.
         */
        LESS_OR_EQUAL, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static QuestionnaireItemOperator fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("exists".equals(codeString))
          return EXISTS;
        if ("=".equals(codeString))
          return EQUAL;
        if ("!=".equals(codeString))
          return NOT_EQUAL;
        if (">".equals(codeString))
          return GREATER_THAN;
        if ("<".equals(codeString))
          return LESS_THAN;
        if (">=".equals(codeString))
          return GREATER_OR_EQUAL;
        if ("<=".equals(codeString))
          return LESS_OR_EQUAL;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown QuestionnaireItemOperator code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case EXISTS: return "exists";
            case EQUAL: return "=";
            case NOT_EQUAL: return "!=";
            case GREATER_THAN: return ">";
            case LESS_THAN: return "<";
            case GREATER_OR_EQUAL: return ">=";
            case LESS_OR_EQUAL: return "<=";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case EXISTS: return "http://hl7.org/fhir/questionnaire-enable-operator";
            case EQUAL: return "http://hl7.org/fhir/questionnaire-enable-operator";
            case NOT_EQUAL: return "http://hl7.org/fhir/questionnaire-enable-operator";
            case GREATER_THAN: return "http://hl7.org/fhir/questionnaire-enable-operator";
            case LESS_THAN: return "http://hl7.org/fhir/questionnaire-enable-operator";
            case GREATER_OR_EQUAL: return "http://hl7.org/fhir/questionnaire-enable-operator";
            case LESS_OR_EQUAL: return "http://hl7.org/fhir/questionnaire-enable-operator";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case EXISTS: return "True if whether an answer exists is equal to the enableWhen answer (which must be a boolean).";
            case EQUAL: return "True if whether at least one answer has a value that is equal to the enableWhen answer.";
            case NOT_EQUAL: return "True if whether at least no answer has a value that is equal to the enableWhen answer.";
            case GREATER_THAN: return "True if whether at least no answer has a value that is greater than the enableWhen answer.";
            case LESS_THAN: return "True if whether at least no answer has a value that is less than the enableWhen answer.";
            case GREATER_OR_EQUAL: return "True if whether at least no answer has a value that is greater or equal to the enableWhen answer.";
            case LESS_OR_EQUAL: return "True if whether at least no answer has a value that is less or equal to the enableWhen answer.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case EXISTS: return "Exists";
            case EQUAL: return "Equals";
            case NOT_EQUAL: return "Not Equals";
            case GREATER_THAN: return "Greater Than";
            case LESS_THAN: return "Less Than";
            case GREATER_OR_EQUAL: return "Greater or Equals";
            case LESS_OR_EQUAL: return "Less or Equals";
            default: return "?";
          }
        }
    }

  public static class QuestionnaireItemOperatorEnumFactory implements EnumFactory<QuestionnaireItemOperator> {
    public QuestionnaireItemOperator fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("exists".equals(codeString))
          return QuestionnaireItemOperator.EXISTS;
        if ("=".equals(codeString))
          return QuestionnaireItemOperator.EQUAL;
        if ("!=".equals(codeString))
          return QuestionnaireItemOperator.NOT_EQUAL;
        if (">".equals(codeString))
          return QuestionnaireItemOperator.GREATER_THAN;
        if ("<".equals(codeString))
          return QuestionnaireItemOperator.LESS_THAN;
        if (">=".equals(codeString))
          return QuestionnaireItemOperator.GREATER_OR_EQUAL;
        if ("<=".equals(codeString))
          return QuestionnaireItemOperator.LESS_OR_EQUAL;
        throw new IllegalArgumentException("Unknown QuestionnaireItemOperator code '"+codeString+"'");
        }
        public Enumeration<QuestionnaireItemOperator> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<QuestionnaireItemOperator>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("exists".equals(codeString))
          return new Enumeration<QuestionnaireItemOperator>(this, QuestionnaireItemOperator.EXISTS);
        if ("=".equals(codeString))
          return new Enumeration<QuestionnaireItemOperator>(this, QuestionnaireItemOperator.EQUAL);
        if ("!=".equals(codeString))
          return new Enumeration<QuestionnaireItemOperator>(this, QuestionnaireItemOperator.NOT_EQUAL);
        if (">".equals(codeString))
          return new Enumeration<QuestionnaireItemOperator>(this, QuestionnaireItemOperator.GREATER_THAN);
        if ("<".equals(codeString))
          return new Enumeration<QuestionnaireItemOperator>(this, QuestionnaireItemOperator.LESS_THAN);
        if (">=".equals(codeString))
          return new Enumeration<QuestionnaireItemOperator>(this, QuestionnaireItemOperator.GREATER_OR_EQUAL);
        if ("<=".equals(codeString))
          return new Enumeration<QuestionnaireItemOperator>(this, QuestionnaireItemOperator.LESS_OR_EQUAL);
        throw new FHIRException("Unknown QuestionnaireItemOperator code '"+codeString+"'");
        }
    public String toCode(QuestionnaireItemOperator code) {
      if (code == QuestionnaireItemOperator.EXISTS)
        return "exists";
      if (code == QuestionnaireItemOperator.EQUAL)
        return "=";
      if (code == QuestionnaireItemOperator.NOT_EQUAL)
        return "!=";
      if (code == QuestionnaireItemOperator.GREATER_THAN)
        return ">";
      if (code == QuestionnaireItemOperator.LESS_THAN)
        return "<";
      if (code == QuestionnaireItemOperator.GREATER_OR_EQUAL)
        return ">=";
      if (code == QuestionnaireItemOperator.LESS_OR_EQUAL)
        return "<=";
      return "?";
      }
    public String toSystem(QuestionnaireItemOperator code) {
      return code.getSystem();
      }
    }

    public enum EnableWhenBehavior {
        /**
         * Enable the question when all the enableWhen criteria are satisfied.
         */
        ALL, 
        /**
         * Enable the question when any of the enableWhen criteria are satisfied.
         */
        ANY, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static EnableWhenBehavior fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("all".equals(codeString))
          return ALL;
        if ("any".equals(codeString))
          return ANY;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown EnableWhenBehavior code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ALL: return "all";
            case ANY: return "any";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case ALL: return "http://hl7.org/fhir/questionnaire-enable-behavior";
            case ANY: return "http://hl7.org/fhir/questionnaire-enable-behavior";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case ALL: return "Enable the question when all the enableWhen criteria are satisfied.";
            case ANY: return "Enable the question when any of the enableWhen criteria are satisfied.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ALL: return "All";
            case ANY: return "Any";
            default: return "?";
          }
        }
    }

  public static class EnableWhenBehaviorEnumFactory implements EnumFactory<EnableWhenBehavior> {
    public EnableWhenBehavior fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("all".equals(codeString))
          return EnableWhenBehavior.ALL;
        if ("any".equals(codeString))
          return EnableWhenBehavior.ANY;
        throw new IllegalArgumentException("Unknown EnableWhenBehavior code '"+codeString+"'");
        }
        public Enumeration<EnableWhenBehavior> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<EnableWhenBehavior>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("all".equals(codeString))
          return new Enumeration<EnableWhenBehavior>(this, EnableWhenBehavior.ALL);
        if ("any".equals(codeString))
          return new Enumeration<EnableWhenBehavior>(this, EnableWhenBehavior.ANY);
        throw new FHIRException("Unknown EnableWhenBehavior code '"+codeString+"'");
        }
    public String toCode(EnableWhenBehavior code) {
      if (code == EnableWhenBehavior.ALL)
        return "all";
      if (code == EnableWhenBehavior.ANY)
        return "any";
      return "?";
      }
    public String toSystem(EnableWhenBehavior code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class QuestionnaireItemComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * An identifier that is unique within the Questionnaire allowing linkage to the equivalent item in a QuestionnaireResponse resource.
         */
        @Child(name = "linkId", type = {StringType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Unique id for item in questionnaire", formalDefinition="An identifier that is unique within the Questionnaire allowing linkage to the equivalent item in a QuestionnaireResponse resource." )
        protected StringType linkId;

        /**
         * This element is a URI that refers to an [[[ElementDefinition]]] that provides information about this item, including information that might otherwise be included in the instance of the Questionnaire resource. A detailed description of the construction of the URI is shown in Comments, below. If this element is present then the following element values MAY be derived from the Element Definition if the corresponding elements of this Questionnaire resource instance have no value:

* code (ElementDefinition.code) 
* type (ElementDefinition.type) 
* required (ElementDefinition.min) 
* repeats (ElementDefinition.max) 
* maxLength (ElementDefinition.maxLength) 
* answerValueSet (ElementDefinition.binding)
* options (ElementDefinition.binding).
         */
        @Child(name = "definition", type = {UriType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="ElementDefinition - details for the item", formalDefinition="This element is a URI that refers to an [[[ElementDefinition]]] that provides information about this item, including information that might otherwise be included in the instance of the Questionnaire resource. A detailed description of the construction of the URI is shown in Comments, below. If this element is present then the following element values MAY be derived from the Element Definition if the corresponding elements of this Questionnaire resource instance have no value:\n\n* code (ElementDefinition.code) \n* type (ElementDefinition.type) \n* required (ElementDefinition.min) \n* repeats (ElementDefinition.max) \n* maxLength (ElementDefinition.maxLength) \n* answerValueSet (ElementDefinition.binding)\n* options (ElementDefinition.binding)." )
        protected UriType definition;

        /**
         * A terminology code that corresponds to this group or question (e.g. a code from LOINC, which defines many questions and answers).
         */
        @Child(name = "code", type = {Coding.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Corresponding concept for this item in a terminology", formalDefinition="A terminology code that corresponds to this group or question (e.g. a code from LOINC, which defines many questions and answers)." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/questionnaire-questions")
        protected List<Coding> code;

        /**
         * A short label for a particular group, question or set of display text within the questionnaire used for reference by the individual completing the questionnaire.
         */
        @Child(name = "prefix", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="E.g. \"1(a)\", \"2.5.3\"", formalDefinition="A short label for a particular group, question or set of display text within the questionnaire used for reference by the individual completing the questionnaire." )
        protected StringType prefix;

        /**
         * The name of a section, the text of a question or text content for a display item.
         */
        @Child(name = "text", type = {StringType.class}, order=5, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Primary text for the item", formalDefinition="The name of a section, the text of a question or text content for a display item." )
        protected StringType text;

        /**
         * The type of questionnaire item this is - whether text for display, a grouping of other items or a particular type of data to be captured (string, integer, coded choice, etc.).
         */
        @Child(name = "type", type = {CodeType.class}, order=6, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="group | display | boolean | decimal | integer | date | dateTime +", formalDefinition="The type of questionnaire item this is - whether text for display, a grouping of other items or a particular type of data to be captured (string, integer, coded choice, etc.)." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/item-type")
        protected Enumeration<QuestionnaireItemType> type;

        /**
         * A constraint indicating that this item should only be enabled (displayed/allow answers to be captured) when the specified condition is true.
         */
        @Child(name = "enableWhen", type = {}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=true, summary=false)
        @Description(shortDefinition="Only allow data when", formalDefinition="A constraint indicating that this item should only be enabled (displayed/allow answers to be captured) when the specified condition is true." )
        protected List<QuestionnaireItemEnableWhenComponent> enableWhen;

        /**
         * Controls how multiple enableWhen values are interpreted -  whether all or any must be true.
         */
        @Child(name = "enableBehavior", type = {CodeType.class}, order=8, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="all | any", formalDefinition="Controls how multiple enableWhen values are interpreted -  whether all or any must be true." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/questionnaire-enable-behavior")
        protected Enumeration<EnableWhenBehavior> enableBehavior;

        /**
         * An indication, if true, that the item must be present in a "completed" QuestionnaireResponse.  If false, the item may be skipped when answering the questionnaire.
         */
        @Child(name = "required", type = {BooleanType.class}, order=9, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Whether the item must be included in data results", formalDefinition="An indication, if true, that the item must be present in a \"completed\" QuestionnaireResponse.  If false, the item may be skipped when answering the questionnaire." )
        protected BooleanType required;

        /**
         * An indication, if true, that the item may occur multiple times in the response, collecting multiple answers for questions or multiple sets of answers for groups.
         */
        @Child(name = "repeats", type = {BooleanType.class}, order=10, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Whether the item may repeat", formalDefinition="An indication, if true, that the item may occur multiple times in the response, collecting multiple answers for questions or multiple sets of answers for groups." )
        protected BooleanType repeats;

        /**
         * An indication, when true, that the value cannot be changed by a human respondent to the Questionnaire.
         */
        @Child(name = "readOnly", type = {BooleanType.class}, order=11, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Don't allow human editing", formalDefinition="An indication, when true, that the value cannot be changed by a human respondent to the Questionnaire." )
        protected BooleanType readOnly;

        /**
         * The maximum number of characters that are permitted in the answer to be considered a "valid" QuestionnaireResponse.
         */
        @Child(name = "maxLength", type = {IntegerType.class}, order=12, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="No more than this many characters", formalDefinition="The maximum number of characters that are permitted in the answer to be considered a \"valid\" QuestionnaireResponse." )
        protected IntegerType maxLength;

        /**
         * A reference to a value set containing a list of codes representing permitted answers for a "choice" or "open-choice" question.
         */
        @Child(name = "answerValueSet", type = {CanonicalType.class}, order=13, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Valueset containing permitted answers", formalDefinition="A reference to a value set containing a list of codes representing permitted answers for a \"choice\" or \"open-choice\" question." )
        protected CanonicalType answerValueSet;

        /**
         * One of the permitted answers for a "choice" or "open-choice" question.
         */
        @Child(name = "answerOption", type = {}, order=14, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Permitted answer", formalDefinition="One of the permitted answers for a \"choice\" or \"open-choice\" question." )
        protected List<QuestionnaireItemAnswerOptionComponent> answerOption;

        /**
         * One or more values that should be pre-populated in the answer when initially rendering the questionnaire for user input.
         */
        @Child(name = "initial", type = {}, order=15, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Initial value(s) when item is first rendered", formalDefinition="One or more values that should be pre-populated in the answer when initially rendering the questionnaire for user input." )
        protected List<QuestionnaireItemInitialComponent> initial;

        /**
         * Text, questions and other groups to be nested beneath a question or group.
         */
        @Child(name = "item", type = {QuestionnaireItemComponent.class}, order=16, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Nested questionnaire items", formalDefinition="Text, questions and other groups to be nested beneath a question or group." )
        protected List<QuestionnaireItemComponent> item;

        private static final long serialVersionUID = -1503380450L;

    /**
     * Constructor
     */
      public QuestionnaireItemComponent() {
        super();
      }

    /**
     * Constructor
     */
      public QuestionnaireItemComponent(StringType linkId, Enumeration<QuestionnaireItemType> type) {
        super();
        this.linkId = linkId;
        this.type = type;
      }

        /**
         * @return {@link #linkId} (An identifier that is unique within the Questionnaire allowing linkage to the equivalent item in a QuestionnaireResponse resource.). This is the underlying object with id, value and extensions. The accessor "getLinkId" gives direct access to the value
         */
        public StringType getLinkIdElement() { 
          if (this.linkId == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create QuestionnaireItemComponent.linkId");
            else if (Configuration.doAutoCreate())
              this.linkId = new StringType(); // bb
          return this.linkId;
        }

        public boolean hasLinkIdElement() { 
          return this.linkId != null && !this.linkId.isEmpty();
        }

        public boolean hasLinkId() { 
          return this.linkId != null && !this.linkId.isEmpty();
        }

        /**
         * @param value {@link #linkId} (An identifier that is unique within the Questionnaire allowing linkage to the equivalent item in a QuestionnaireResponse resource.). This is the underlying object with id, value and extensions. The accessor "getLinkId" gives direct access to the value
         */
        public QuestionnaireItemComponent setLinkIdElement(StringType value) { 
          this.linkId = value;
          return this;
        }

        /**
         * @return An identifier that is unique within the Questionnaire allowing linkage to the equivalent item in a QuestionnaireResponse resource.
         */
        public String getLinkId() { 
          return this.linkId == null ? null : this.linkId.getValue();
        }

        /**
         * @param value An identifier that is unique within the Questionnaire allowing linkage to the equivalent item in a QuestionnaireResponse resource.
         */
        public QuestionnaireItemComponent setLinkId(String value) { 
            if (this.linkId == null)
              this.linkId = new StringType();
            this.linkId.setValue(value);
          return this;
        }

        /**
         * @return {@link #definition} (This element is a URI that refers to an [[[ElementDefinition]]] that provides information about this item, including information that might otherwise be included in the instance of the Questionnaire resource. A detailed description of the construction of the URI is shown in Comments, below. If this element is present then the following element values MAY be derived from the Element Definition if the corresponding elements of this Questionnaire resource instance have no value:

* code (ElementDefinition.code) 
* type (ElementDefinition.type) 
* required (ElementDefinition.min) 
* repeats (ElementDefinition.max) 
* maxLength (ElementDefinition.maxLength) 
* answerValueSet (ElementDefinition.binding)
* options (ElementDefinition.binding).). This is the underlying object with id, value and extensions. The accessor "getDefinition" gives direct access to the value
         */
        public UriType getDefinitionElement() { 
          if (this.definition == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create QuestionnaireItemComponent.definition");
            else if (Configuration.doAutoCreate())
              this.definition = new UriType(); // bb
          return this.definition;
        }

        public boolean hasDefinitionElement() { 
          return this.definition != null && !this.definition.isEmpty();
        }

        public boolean hasDefinition() { 
          return this.definition != null && !this.definition.isEmpty();
        }

        /**
         * @param value {@link #definition} (This element is a URI that refers to an [[[ElementDefinition]]] that provides information about this item, including information that might otherwise be included in the instance of the Questionnaire resource. A detailed description of the construction of the URI is shown in Comments, below. If this element is present then the following element values MAY be derived from the Element Definition if the corresponding elements of this Questionnaire resource instance have no value:

* code (ElementDefinition.code) 
* type (ElementDefinition.type) 
* required (ElementDefinition.min) 
* repeats (ElementDefinition.max) 
* maxLength (ElementDefinition.maxLength) 
* answerValueSet (ElementDefinition.binding)
* options (ElementDefinition.binding).). This is the underlying object with id, value and extensions. The accessor "getDefinition" gives direct access to the value
         */
        public QuestionnaireItemComponent setDefinitionElement(UriType value) { 
          this.definition = value;
          return this;
        }

        /**
         * @return This element is a URI that refers to an [[[ElementDefinition]]] that provides information about this item, including information that might otherwise be included in the instance of the Questionnaire resource. A detailed description of the construction of the URI is shown in Comments, below. If this element is present then the following element values MAY be derived from the Element Definition if the corresponding elements of this Questionnaire resource instance have no value:

* code (ElementDefinition.code) 
* type (ElementDefinition.type) 
* required (ElementDefinition.min) 
* repeats (ElementDefinition.max) 
* maxLength (ElementDefinition.maxLength) 
* answerValueSet (ElementDefinition.binding)
* options (ElementDefinition.binding).
         */
        public String getDefinition() { 
          return this.definition == null ? null : this.definition.getValue();
        }

        /**
         * @param value This element is a URI that refers to an [[[ElementDefinition]]] that provides information about this item, including information that might otherwise be included in the instance of the Questionnaire resource. A detailed description of the construction of the URI is shown in Comments, below. If this element is present then the following element values MAY be derived from the Element Definition if the corresponding elements of this Questionnaire resource instance have no value:

* code (ElementDefinition.code) 
* type (ElementDefinition.type) 
* required (ElementDefinition.min) 
* repeats (ElementDefinition.max) 
* maxLength (ElementDefinition.maxLength) 
* answerValueSet (ElementDefinition.binding)
* options (ElementDefinition.binding).
         */
        public QuestionnaireItemComponent setDefinition(String value) { 
          if (Utilities.noString(value))
            this.definition = null;
          else {
            if (this.definition == null)
              this.definition = new UriType();
            this.definition.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #code} (A terminology code that corresponds to this group or question (e.g. a code from LOINC, which defines many questions and answers).)
         */
        public List<Coding> getCode() { 
          if (this.code == null)
            this.code = new ArrayList<Coding>();
          return this.code;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public QuestionnaireItemComponent setCode(List<Coding> theCode) { 
          this.code = theCode;
          return this;
        }

        public boolean hasCode() { 
          if (this.code == null)
            return false;
          for (Coding item : this.code)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public Coding addCode() { //3
          Coding t = new Coding();
          if (this.code == null)
            this.code = new ArrayList<Coding>();
          this.code.add(t);
          return t;
        }

        public QuestionnaireItemComponent addCode(Coding t) { //3
          if (t == null)
            return this;
          if (this.code == null)
            this.code = new ArrayList<Coding>();
          this.code.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #code}, creating it if it does not already exist
         */
        public Coding getCodeFirstRep() { 
          if (getCode().isEmpty()) {
            addCode();
          }
          return getCode().get(0);
        }

        /**
         * @return {@link #prefix} (A short label for a particular group, question or set of display text within the questionnaire used for reference by the individual completing the questionnaire.). This is the underlying object with id, value and extensions. The accessor "getPrefix" gives direct access to the value
         */
        public StringType getPrefixElement() { 
          if (this.prefix == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create QuestionnaireItemComponent.prefix");
            else if (Configuration.doAutoCreate())
              this.prefix = new StringType(); // bb
          return this.prefix;
        }

        public boolean hasPrefixElement() { 
          return this.prefix != null && !this.prefix.isEmpty();
        }

        public boolean hasPrefix() { 
          return this.prefix != null && !this.prefix.isEmpty();
        }

        /**
         * @param value {@link #prefix} (A short label for a particular group, question or set of display text within the questionnaire used for reference by the individual completing the questionnaire.). This is the underlying object with id, value and extensions. The accessor "getPrefix" gives direct access to the value
         */
        public QuestionnaireItemComponent setPrefixElement(StringType value) { 
          this.prefix = value;
          return this;
        }

        /**
         * @return A short label for a particular group, question or set of display text within the questionnaire used for reference by the individual completing the questionnaire.
         */
        public String getPrefix() { 
          return this.prefix == null ? null : this.prefix.getValue();
        }

        /**
         * @param value A short label for a particular group, question or set of display text within the questionnaire used for reference by the individual completing the questionnaire.
         */
        public QuestionnaireItemComponent setPrefix(String value) { 
          if (Utilities.noString(value))
            this.prefix = null;
          else {
            if (this.prefix == null)
              this.prefix = new StringType();
            this.prefix.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #text} (The name of a section, the text of a question or text content for a display item.). This is the underlying object with id, value and extensions. The accessor "getText" gives direct access to the value
         */
        public StringType getTextElement() { 
          if (this.text == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create QuestionnaireItemComponent.text");
            else if (Configuration.doAutoCreate())
              this.text = new StringType(); // bb
          return this.text;
        }

        public boolean hasTextElement() { 
          return this.text != null && !this.text.isEmpty();
        }

        public boolean hasText() { 
          return this.text != null && !this.text.isEmpty();
        }

        /**
         * @param value {@link #text} (The name of a section, the text of a question or text content for a display item.). This is the underlying object with id, value and extensions. The accessor "getText" gives direct access to the value
         */
        public QuestionnaireItemComponent setTextElement(StringType value) { 
          this.text = value;
          return this;
        }

        /**
         * @return The name of a section, the text of a question or text content for a display item.
         */
        public String getText() { 
          return this.text == null ? null : this.text.getValue();
        }

        /**
         * @param value The name of a section, the text of a question or text content for a display item.
         */
        public QuestionnaireItemComponent setText(String value) { 
          if (Utilities.noString(value))
            this.text = null;
          else {
            if (this.text == null)
              this.text = new StringType();
            this.text.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #type} (The type of questionnaire item this is - whether text for display, a grouping of other items or a particular type of data to be captured (string, integer, coded choice, etc.).). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public Enumeration<QuestionnaireItemType> getTypeElement() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create QuestionnaireItemComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new Enumeration<QuestionnaireItemType>(new QuestionnaireItemTypeEnumFactory()); // bb
          return this.type;
        }

        public boolean hasTypeElement() { 
          return this.type != null && !this.type.isEmpty();
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (The type of questionnaire item this is - whether text for display, a grouping of other items or a particular type of data to be captured (string, integer, coded choice, etc.).). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public QuestionnaireItemComponent setTypeElement(Enumeration<QuestionnaireItemType> value) { 
          this.type = value;
          return this;
        }

        /**
         * @return The type of questionnaire item this is - whether text for display, a grouping of other items or a particular type of data to be captured (string, integer, coded choice, etc.).
         */
        public QuestionnaireItemType getType() { 
          return this.type == null ? null : this.type.getValue();
        }

        /**
         * @param value The type of questionnaire item this is - whether text for display, a grouping of other items or a particular type of data to be captured (string, integer, coded choice, etc.).
         */
        public QuestionnaireItemComponent setType(QuestionnaireItemType value) { 
            if (this.type == null)
              this.type = new Enumeration<QuestionnaireItemType>(new QuestionnaireItemTypeEnumFactory());
            this.type.setValue(value);
          return this;
        }

        /**
         * @return {@link #enableWhen} (A constraint indicating that this item should only be enabled (displayed/allow answers to be captured) when the specified condition is true.)
         */
        public List<QuestionnaireItemEnableWhenComponent> getEnableWhen() { 
          if (this.enableWhen == null)
            this.enableWhen = new ArrayList<QuestionnaireItemEnableWhenComponent>();
          return this.enableWhen;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public QuestionnaireItemComponent setEnableWhen(List<QuestionnaireItemEnableWhenComponent> theEnableWhen) { 
          this.enableWhen = theEnableWhen;
          return this;
        }

        public boolean hasEnableWhen() { 
          if (this.enableWhen == null)
            return false;
          for (QuestionnaireItemEnableWhenComponent item : this.enableWhen)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public QuestionnaireItemEnableWhenComponent addEnableWhen() { //3
          QuestionnaireItemEnableWhenComponent t = new QuestionnaireItemEnableWhenComponent();
          if (this.enableWhen == null)
            this.enableWhen = new ArrayList<QuestionnaireItemEnableWhenComponent>();
          this.enableWhen.add(t);
          return t;
        }

        public QuestionnaireItemComponent addEnableWhen(QuestionnaireItemEnableWhenComponent t) { //3
          if (t == null)
            return this;
          if (this.enableWhen == null)
            this.enableWhen = new ArrayList<QuestionnaireItemEnableWhenComponent>();
          this.enableWhen.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #enableWhen}, creating it if it does not already exist
         */
        public QuestionnaireItemEnableWhenComponent getEnableWhenFirstRep() { 
          if (getEnableWhen().isEmpty()) {
            addEnableWhen();
          }
          return getEnableWhen().get(0);
        }

        /**
         * @return {@link #enableBehavior} (Controls how multiple enableWhen values are interpreted -  whether all or any must be true.). This is the underlying object with id, value and extensions. The accessor "getEnableBehavior" gives direct access to the value
         */
        public Enumeration<EnableWhenBehavior> getEnableBehaviorElement() { 
          if (this.enableBehavior == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create QuestionnaireItemComponent.enableBehavior");
            else if (Configuration.doAutoCreate())
              this.enableBehavior = new Enumeration<EnableWhenBehavior>(new EnableWhenBehaviorEnumFactory()); // bb
          return this.enableBehavior;
        }

        public boolean hasEnableBehaviorElement() { 
          return this.enableBehavior != null && !this.enableBehavior.isEmpty();
        }

        public boolean hasEnableBehavior() { 
          return this.enableBehavior != null && !this.enableBehavior.isEmpty();
        }

        /**
         * @param value {@link #enableBehavior} (Controls how multiple enableWhen values are interpreted -  whether all or any must be true.). This is the underlying object with id, value and extensions. The accessor "getEnableBehavior" gives direct access to the value
         */
        public QuestionnaireItemComponent setEnableBehaviorElement(Enumeration<EnableWhenBehavior> value) { 
          this.enableBehavior = value;
          return this;
        }

        /**
         * @return Controls how multiple enableWhen values are interpreted -  whether all or any must be true.
         */
        public EnableWhenBehavior getEnableBehavior() { 
          return this.enableBehavior == null ? null : this.enableBehavior.getValue();
        }

        /**
         * @param value Controls how multiple enableWhen values are interpreted -  whether all or any must be true.
         */
        public QuestionnaireItemComponent setEnableBehavior(EnableWhenBehavior value) { 
          if (value == null)
            this.enableBehavior = null;
          else {
            if (this.enableBehavior == null)
              this.enableBehavior = new Enumeration<EnableWhenBehavior>(new EnableWhenBehaviorEnumFactory());
            this.enableBehavior.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #required} (An indication, if true, that the item must be present in a "completed" QuestionnaireResponse.  If false, the item may be skipped when answering the questionnaire.). This is the underlying object with id, value and extensions. The accessor "getRequired" gives direct access to the value
         */
        public BooleanType getRequiredElement() { 
          if (this.required == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create QuestionnaireItemComponent.required");
            else if (Configuration.doAutoCreate())
              this.required = new BooleanType(); // bb
          return this.required;
        }

        public boolean hasRequiredElement() { 
          return this.required != null && !this.required.isEmpty();
        }

        public boolean hasRequired() { 
          return this.required != null && !this.required.isEmpty();
        }

        /**
         * @param value {@link #required} (An indication, if true, that the item must be present in a "completed" QuestionnaireResponse.  If false, the item may be skipped when answering the questionnaire.). This is the underlying object with id, value and extensions. The accessor "getRequired" gives direct access to the value
         */
        public QuestionnaireItemComponent setRequiredElement(BooleanType value) { 
          this.required = value;
          return this;
        }

        /**
         * @return An indication, if true, that the item must be present in a "completed" QuestionnaireResponse.  If false, the item may be skipped when answering the questionnaire.
         */
        public boolean getRequired() { 
          return this.required == null || this.required.isEmpty() ? false : this.required.getValue();
        }

        /**
         * @param value An indication, if true, that the item must be present in a "completed" QuestionnaireResponse.  If false, the item may be skipped when answering the questionnaire.
         */
        public QuestionnaireItemComponent setRequired(boolean value) { 
            if (this.required == null)
              this.required = new BooleanType();
            this.required.setValue(value);
          return this;
        }

        /**
         * @return {@link #repeats} (An indication, if true, that the item may occur multiple times in the response, collecting multiple answers for questions or multiple sets of answers for groups.). This is the underlying object with id, value and extensions. The accessor "getRepeats" gives direct access to the value
         */
        public BooleanType getRepeatsElement() { 
          if (this.repeats == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create QuestionnaireItemComponent.repeats");
            else if (Configuration.doAutoCreate())
              this.repeats = new BooleanType(); // bb
          return this.repeats;
        }

        public boolean hasRepeatsElement() { 
          return this.repeats != null && !this.repeats.isEmpty();
        }

        public boolean hasRepeats() { 
          return this.repeats != null && !this.repeats.isEmpty();
        }

        /**
         * @param value {@link #repeats} (An indication, if true, that the item may occur multiple times in the response, collecting multiple answers for questions or multiple sets of answers for groups.). This is the underlying object with id, value and extensions. The accessor "getRepeats" gives direct access to the value
         */
        public QuestionnaireItemComponent setRepeatsElement(BooleanType value) { 
          this.repeats = value;
          return this;
        }

        /**
         * @return An indication, if true, that the item may occur multiple times in the response, collecting multiple answers for questions or multiple sets of answers for groups.
         */
        public boolean getRepeats() { 
          return this.repeats == null || this.repeats.isEmpty() ? false : this.repeats.getValue();
        }

        /**
         * @param value An indication, if true, that the item may occur multiple times in the response, collecting multiple answers for questions or multiple sets of answers for groups.
         */
        public QuestionnaireItemComponent setRepeats(boolean value) { 
            if (this.repeats == null)
              this.repeats = new BooleanType();
            this.repeats.setValue(value);
          return this;
        }

        /**
         * @return {@link #readOnly} (An indication, when true, that the value cannot be changed by a human respondent to the Questionnaire.). This is the underlying object with id, value and extensions. The accessor "getReadOnly" gives direct access to the value
         */
        public BooleanType getReadOnlyElement() { 
          if (this.readOnly == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create QuestionnaireItemComponent.readOnly");
            else if (Configuration.doAutoCreate())
              this.readOnly = new BooleanType(); // bb
          return this.readOnly;
        }

        public boolean hasReadOnlyElement() { 
          return this.readOnly != null && !this.readOnly.isEmpty();
        }

        public boolean hasReadOnly() { 
          return this.readOnly != null && !this.readOnly.isEmpty();
        }

        /**
         * @param value {@link #readOnly} (An indication, when true, that the value cannot be changed by a human respondent to the Questionnaire.). This is the underlying object with id, value and extensions. The accessor "getReadOnly" gives direct access to the value
         */
        public QuestionnaireItemComponent setReadOnlyElement(BooleanType value) { 
          this.readOnly = value;
          return this;
        }

        /**
         * @return An indication, when true, that the value cannot be changed by a human respondent to the Questionnaire.
         */
        public boolean getReadOnly() { 
          return this.readOnly == null || this.readOnly.isEmpty() ? false : this.readOnly.getValue();
        }

        /**
         * @param value An indication, when true, that the value cannot be changed by a human respondent to the Questionnaire.
         */
        public QuestionnaireItemComponent setReadOnly(boolean value) { 
            if (this.readOnly == null)
              this.readOnly = new BooleanType();
            this.readOnly.setValue(value);
          return this;
        }

        /**
         * @return {@link #maxLength} (The maximum number of characters that are permitted in the answer to be considered a "valid" QuestionnaireResponse.). This is the underlying object with id, value and extensions. The accessor "getMaxLength" gives direct access to the value
         */
        public IntegerType getMaxLengthElement() { 
          if (this.maxLength == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create QuestionnaireItemComponent.maxLength");
            else if (Configuration.doAutoCreate())
              this.maxLength = new IntegerType(); // bb
          return this.maxLength;
        }

        public boolean hasMaxLengthElement() { 
          return this.maxLength != null && !this.maxLength.isEmpty();
        }

        public boolean hasMaxLength() { 
          return this.maxLength != null && !this.maxLength.isEmpty();
        }

        /**
         * @param value {@link #maxLength} (The maximum number of characters that are permitted in the answer to be considered a "valid" QuestionnaireResponse.). This is the underlying object with id, value and extensions. The accessor "getMaxLength" gives direct access to the value
         */
        public QuestionnaireItemComponent setMaxLengthElement(IntegerType value) { 
          this.maxLength = value;
          return this;
        }

        /**
         * @return The maximum number of characters that are permitted in the answer to be considered a "valid" QuestionnaireResponse.
         */
        public int getMaxLength() { 
          return this.maxLength == null || this.maxLength.isEmpty() ? 0 : this.maxLength.getValue();
        }

        /**
         * @param value The maximum number of characters that are permitted in the answer to be considered a "valid" QuestionnaireResponse.
         */
        public QuestionnaireItemComponent setMaxLength(int value) { 
            if (this.maxLength == null)
              this.maxLength = new IntegerType();
            this.maxLength.setValue(value);
          return this;
        }

        /**
         * @return {@link #answerValueSet} (A reference to a value set containing a list of codes representing permitted answers for a "choice" or "open-choice" question.). This is the underlying object with id, value and extensions. The accessor "getAnswerValueSet" gives direct access to the value
         */
        public CanonicalType getAnswerValueSetElement() { 
          if (this.answerValueSet == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create QuestionnaireItemComponent.answerValueSet");
            else if (Configuration.doAutoCreate())
              this.answerValueSet = new CanonicalType(); // bb
          return this.answerValueSet;
        }

        public boolean hasAnswerValueSetElement() { 
          return this.answerValueSet != null && !this.answerValueSet.isEmpty();
        }

        public boolean hasAnswerValueSet() { 
          return this.answerValueSet != null && !this.answerValueSet.isEmpty();
        }

        /**
         * @param value {@link #answerValueSet} (A reference to a value set containing a list of codes representing permitted answers for a "choice" or "open-choice" question.). This is the underlying object with id, value and extensions. The accessor "getAnswerValueSet" gives direct access to the value
         */
        public QuestionnaireItemComponent setAnswerValueSetElement(CanonicalType value) { 
          this.answerValueSet = value;
          return this;
        }

        /**
         * @return A reference to a value set containing a list of codes representing permitted answers for a "choice" or "open-choice" question.
         */
        public String getAnswerValueSet() { 
          return this.answerValueSet == null ? null : this.answerValueSet.getValue();
        }

        /**
         * @param value A reference to a value set containing a list of codes representing permitted answers for a "choice" or "open-choice" question.
         */
        public QuestionnaireItemComponent setAnswerValueSet(String value) { 
          if (Utilities.noString(value))
            this.answerValueSet = null;
          else {
            if (this.answerValueSet == null)
              this.answerValueSet = new CanonicalType();
            this.answerValueSet.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #answerOption} (One of the permitted answers for a "choice" or "open-choice" question.)
         */
        public List<QuestionnaireItemAnswerOptionComponent> getAnswerOption() { 
          if (this.answerOption == null)
            this.answerOption = new ArrayList<QuestionnaireItemAnswerOptionComponent>();
          return this.answerOption;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public QuestionnaireItemComponent setAnswerOption(List<QuestionnaireItemAnswerOptionComponent> theAnswerOption) { 
          this.answerOption = theAnswerOption;
          return this;
        }

        public boolean hasAnswerOption() { 
          if (this.answerOption == null)
            return false;
          for (QuestionnaireItemAnswerOptionComponent item : this.answerOption)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public QuestionnaireItemAnswerOptionComponent addAnswerOption() { //3
          QuestionnaireItemAnswerOptionComponent t = new QuestionnaireItemAnswerOptionComponent();
          if (this.answerOption == null)
            this.answerOption = new ArrayList<QuestionnaireItemAnswerOptionComponent>();
          this.answerOption.add(t);
          return t;
        }

        public QuestionnaireItemComponent addAnswerOption(QuestionnaireItemAnswerOptionComponent t) { //3
          if (t == null)
            return this;
          if (this.answerOption == null)
            this.answerOption = new ArrayList<QuestionnaireItemAnswerOptionComponent>();
          this.answerOption.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #answerOption}, creating it if it does not already exist
         */
        public QuestionnaireItemAnswerOptionComponent getAnswerOptionFirstRep() { 
          if (getAnswerOption().isEmpty()) {
            addAnswerOption();
          }
          return getAnswerOption().get(0);
        }

        /**
         * @return {@link #initial} (One or more values that should be pre-populated in the answer when initially rendering the questionnaire for user input.)
         */
        public List<QuestionnaireItemInitialComponent> getInitial() { 
          if (this.initial == null)
            this.initial = new ArrayList<QuestionnaireItemInitialComponent>();
          return this.initial;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public QuestionnaireItemComponent setInitial(List<QuestionnaireItemInitialComponent> theInitial) { 
          this.initial = theInitial;
          return this;
        }

        public boolean hasInitial() { 
          if (this.initial == null)
            return false;
          for (QuestionnaireItemInitialComponent item : this.initial)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public QuestionnaireItemInitialComponent addInitial() { //3
          QuestionnaireItemInitialComponent t = new QuestionnaireItemInitialComponent();
          if (this.initial == null)
            this.initial = new ArrayList<QuestionnaireItemInitialComponent>();
          this.initial.add(t);
          return t;
        }

        public QuestionnaireItemComponent addInitial(QuestionnaireItemInitialComponent t) { //3
          if (t == null)
            return this;
          if (this.initial == null)
            this.initial = new ArrayList<QuestionnaireItemInitialComponent>();
          this.initial.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #initial}, creating it if it does not already exist
         */
        public QuestionnaireItemInitialComponent getInitialFirstRep() { 
          if (getInitial().isEmpty()) {
            addInitial();
          }
          return getInitial().get(0);
        }

        /**
         * @return {@link #item} (Text, questions and other groups to be nested beneath a question or group.)
         */
        public List<QuestionnaireItemComponent> getItem() { 
          if (this.item == null)
            this.item = new ArrayList<QuestionnaireItemComponent>();
          return this.item;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public QuestionnaireItemComponent setItem(List<QuestionnaireItemComponent> theItem) { 
          this.item = theItem;
          return this;
        }

        public boolean hasItem() { 
          if (this.item == null)
            return false;
          for (QuestionnaireItemComponent item : this.item)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public QuestionnaireItemComponent addItem() { //3
          QuestionnaireItemComponent t = new QuestionnaireItemComponent();
          if (this.item == null)
            this.item = new ArrayList<QuestionnaireItemComponent>();
          this.item.add(t);
          return t;
        }

        public QuestionnaireItemComponent addItem(QuestionnaireItemComponent t) { //3
          if (t == null)
            return this;
          if (this.item == null)
            this.item = new ArrayList<QuestionnaireItemComponent>();
          this.item.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #item}, creating it if it does not already exist
         */
        public QuestionnaireItemComponent getItemFirstRep() { 
          if (getItem().isEmpty()) {
            addItem();
          }
          return getItem().get(0);
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("linkId", "string", "An identifier that is unique within the Questionnaire allowing linkage to the equivalent item in a QuestionnaireResponse resource.", 0, 1, linkId));
          children.add(new Property("definition", "uri", "This element is a URI that refers to an [[[ElementDefinition]]] that provides information about this item, including information that might otherwise be included in the instance of the Questionnaire resource. A detailed description of the construction of the URI is shown in Comments, below. If this element is present then the following element values MAY be derived from the Element Definition if the corresponding elements of this Questionnaire resource instance have no value:\n\n* code (ElementDefinition.code) \n* type (ElementDefinition.type) \n* required (ElementDefinition.min) \n* repeats (ElementDefinition.max) \n* maxLength (ElementDefinition.maxLength) \n* answerValueSet (ElementDefinition.binding)\n* options (ElementDefinition.binding).", 0, 1, definition));
          children.add(new Property("code", "Coding", "A terminology code that corresponds to this group or question (e.g. a code from LOINC, which defines many questions and answers).", 0, java.lang.Integer.MAX_VALUE, code));
          children.add(new Property("prefix", "string", "A short label for a particular group, question or set of display text within the questionnaire used for reference by the individual completing the questionnaire.", 0, 1, prefix));
          children.add(new Property("text", "string", "The name of a section, the text of a question or text content for a display item.", 0, 1, text));
          children.add(new Property("type", "code", "The type of questionnaire item this is - whether text for display, a grouping of other items or a particular type of data to be captured (string, integer, coded choice, etc.).", 0, 1, type));
          children.add(new Property("enableWhen", "", "A constraint indicating that this item should only be enabled (displayed/allow answers to be captured) when the specified condition is true.", 0, java.lang.Integer.MAX_VALUE, enableWhen));
          children.add(new Property("enableBehavior", "code", "Controls how multiple enableWhen values are interpreted -  whether all or any must be true.", 0, 1, enableBehavior));
          children.add(new Property("required", "boolean", "An indication, if true, that the item must be present in a \"completed\" QuestionnaireResponse.  If false, the item may be skipped when answering the questionnaire.", 0, 1, required));
          children.add(new Property("repeats", "boolean", "An indication, if true, that the item may occur multiple times in the response, collecting multiple answers for questions or multiple sets of answers for groups.", 0, 1, repeats));
          children.add(new Property("readOnly", "boolean", "An indication, when true, that the value cannot be changed by a human respondent to the Questionnaire.", 0, 1, readOnly));
          children.add(new Property("maxLength", "integer", "The maximum number of characters that are permitted in the answer to be considered a \"valid\" QuestionnaireResponse.", 0, 1, maxLength));
          children.add(new Property("answerValueSet", "canonical(ValueSet)", "A reference to a value set containing a list of codes representing permitted answers for a \"choice\" or \"open-choice\" question.", 0, 1, answerValueSet));
          children.add(new Property("answerOption", "", "One of the permitted answers for a \"choice\" or \"open-choice\" question.", 0, java.lang.Integer.MAX_VALUE, answerOption));
          children.add(new Property("initial", "", "One or more values that should be pre-populated in the answer when initially rendering the questionnaire for user input.", 0, java.lang.Integer.MAX_VALUE, initial));
          children.add(new Property("item", "@Questionnaire.item", "Text, questions and other groups to be nested beneath a question or group.", 0, java.lang.Integer.MAX_VALUE, item));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -1102667083: /*linkId*/  return new Property("linkId", "string", "An identifier that is unique within the Questionnaire allowing linkage to the equivalent item in a QuestionnaireResponse resource.", 0, 1, linkId);
          case -1014418093: /*definition*/  return new Property("definition", "uri", "This element is a URI that refers to an [[[ElementDefinition]]] that provides information about this item, including information that might otherwise be included in the instance of the Questionnaire resource. A detailed description of the construction of the URI is shown in Comments, below. If this element is present then the following element values MAY be derived from the Element Definition if the corresponding elements of this Questionnaire resource instance have no value:\n\n* code (ElementDefinition.code) \n* type (ElementDefinition.type) \n* required (ElementDefinition.min) \n* repeats (ElementDefinition.max) \n* maxLength (ElementDefinition.maxLength) \n* answerValueSet (ElementDefinition.binding)\n* options (ElementDefinition.binding).", 0, 1, definition);
          case 3059181: /*code*/  return new Property("code", "Coding", "A terminology code that corresponds to this group or question (e.g. a code from LOINC, which defines many questions and answers).", 0, java.lang.Integer.MAX_VALUE, code);
          case -980110702: /*prefix*/  return new Property("prefix", "string", "A short label for a particular group, question or set of display text within the questionnaire used for reference by the individual completing the questionnaire.", 0, 1, prefix);
          case 3556653: /*text*/  return new Property("text", "string", "The name of a section, the text of a question or text content for a display item.", 0, 1, text);
          case 3575610: /*type*/  return new Property("type", "code", "The type of questionnaire item this is - whether text for display, a grouping of other items or a particular type of data to be captured (string, integer, coded choice, etc.).", 0, 1, type);
          case 1893321565: /*enableWhen*/  return new Property("enableWhen", "", "A constraint indicating that this item should only be enabled (displayed/allow answers to be captured) when the specified condition is true.", 0, java.lang.Integer.MAX_VALUE, enableWhen);
          case 1854802165: /*enableBehavior*/  return new Property("enableBehavior", "code", "Controls how multiple enableWhen values are interpreted -  whether all or any must be true.", 0, 1, enableBehavior);
          case -393139297: /*required*/  return new Property("required", "boolean", "An indication, if true, that the item must be present in a \"completed\" QuestionnaireResponse.  If false, the item may be skipped when answering the questionnaire.", 0, 1, required);
          case 1094288952: /*repeats*/  return new Property("repeats", "boolean", "An indication, if true, that the item may occur multiple times in the response, collecting multiple answers for questions or multiple sets of answers for groups.", 0, 1, repeats);
          case -867683742: /*readOnly*/  return new Property("readOnly", "boolean", "An indication, when true, that the value cannot be changed by a human respondent to the Questionnaire.", 0, 1, readOnly);
          case -791400086: /*maxLength*/  return new Property("maxLength", "integer", "The maximum number of characters that are permitted in the answer to be considered a \"valid\" QuestionnaireResponse.", 0, 1, maxLength);
          case -743278833: /*answerValueSet*/  return new Property("answerValueSet", "canonical(ValueSet)", "A reference to a value set containing a list of codes representing permitted answers for a \"choice\" or \"open-choice\" question.", 0, 1, answerValueSet);
          case -1527878189: /*answerOption*/  return new Property("answerOption", "", "One of the permitted answers for a \"choice\" or \"open-choice\" question.", 0, java.lang.Integer.MAX_VALUE, answerOption);
          case 1948342084: /*initial*/  return new Property("initial", "", "One or more values that should be pre-populated in the answer when initially rendering the questionnaire for user input.", 0, java.lang.Integer.MAX_VALUE, initial);
          case 3242771: /*item*/  return new Property("item", "@Questionnaire.item", "Text, questions and other groups to be nested beneath a question or group.", 0, java.lang.Integer.MAX_VALUE, item);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1102667083: /*linkId*/ return this.linkId == null ? new Base[0] : new Base[] {this.linkId}; // StringType
        case -1014418093: /*definition*/ return this.definition == null ? new Base[0] : new Base[] {this.definition}; // UriType
        case 3059181: /*code*/ return this.code == null ? new Base[0] : this.code.toArray(new Base[this.code.size()]); // Coding
        case -980110702: /*prefix*/ return this.prefix == null ? new Base[0] : new Base[] {this.prefix}; // StringType
        case 3556653: /*text*/ return this.text == null ? new Base[0] : new Base[] {this.text}; // StringType
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // Enumeration<QuestionnaireItemType>
        case 1893321565: /*enableWhen*/ return this.enableWhen == null ? new Base[0] : this.enableWhen.toArray(new Base[this.enableWhen.size()]); // QuestionnaireItemEnableWhenComponent
        case 1854802165: /*enableBehavior*/ return this.enableBehavior == null ? new Base[0] : new Base[] {this.enableBehavior}; // Enumeration<EnableWhenBehavior>
        case -393139297: /*required*/ return this.required == null ? new Base[0] : new Base[] {this.required}; // BooleanType
        case 1094288952: /*repeats*/ return this.repeats == null ? new Base[0] : new Base[] {this.repeats}; // BooleanType
        case -867683742: /*readOnly*/ return this.readOnly == null ? new Base[0] : new Base[] {this.readOnly}; // BooleanType
        case -791400086: /*maxLength*/ return this.maxLength == null ? new Base[0] : new Base[] {this.maxLength}; // IntegerType
        case -743278833: /*answerValueSet*/ return this.answerValueSet == null ? new Base[0] : new Base[] {this.answerValueSet}; // CanonicalType
        case -1527878189: /*answerOption*/ return this.answerOption == null ? new Base[0] : this.answerOption.toArray(new Base[this.answerOption.size()]); // QuestionnaireItemAnswerOptionComponent
        case 1948342084: /*initial*/ return this.initial == null ? new Base[0] : this.initial.toArray(new Base[this.initial.size()]); // QuestionnaireItemInitialComponent
        case 3242771: /*item*/ return this.item == null ? new Base[0] : this.item.toArray(new Base[this.item.size()]); // QuestionnaireItemComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1102667083: // linkId
          this.linkId = castToString(value); // StringType
          return value;
        case -1014418093: // definition
          this.definition = castToUri(value); // UriType
          return value;
        case 3059181: // code
          this.getCode().add(castToCoding(value)); // Coding
          return value;
        case -980110702: // prefix
          this.prefix = castToString(value); // StringType
          return value;
        case 3556653: // text
          this.text = castToString(value); // StringType
          return value;
        case 3575610: // type
          value = new QuestionnaireItemTypeEnumFactory().fromType(castToCode(value));
          this.type = (Enumeration) value; // Enumeration<QuestionnaireItemType>
          return value;
        case 1893321565: // enableWhen
          this.getEnableWhen().add((QuestionnaireItemEnableWhenComponent) value); // QuestionnaireItemEnableWhenComponent
          return value;
        case 1854802165: // enableBehavior
          value = new EnableWhenBehaviorEnumFactory().fromType(castToCode(value));
          this.enableBehavior = (Enumeration) value; // Enumeration<EnableWhenBehavior>
          return value;
        case -393139297: // required
          this.required = castToBoolean(value); // BooleanType
          return value;
        case 1094288952: // repeats
          this.repeats = castToBoolean(value); // BooleanType
          return value;
        case -867683742: // readOnly
          this.readOnly = castToBoolean(value); // BooleanType
          return value;
        case -791400086: // maxLength
          this.maxLength = castToInteger(value); // IntegerType
          return value;
        case -743278833: // answerValueSet
          this.answerValueSet = castToCanonical(value); // CanonicalType
          return value;
        case -1527878189: // answerOption
          this.getAnswerOption().add((QuestionnaireItemAnswerOptionComponent) value); // QuestionnaireItemAnswerOptionComponent
          return value;
        case 1948342084: // initial
          this.getInitial().add((QuestionnaireItemInitialComponent) value); // QuestionnaireItemInitialComponent
          return value;
        case 3242771: // item
          this.getItem().add((QuestionnaireItemComponent) value); // QuestionnaireItemComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("linkId")) {
          this.linkId = castToString(value); // StringType
        } else if (name.equals("definition")) {
          this.definition = castToUri(value); // UriType
        } else if (name.equals("code")) {
          this.getCode().add(castToCoding(value));
        } else if (name.equals("prefix")) {
          this.prefix = castToString(value); // StringType
        } else if (name.equals("text")) {
          this.text = castToString(value); // StringType
        } else if (name.equals("type")) {
          value = new QuestionnaireItemTypeEnumFactory().fromType(castToCode(value));
          this.type = (Enumeration) value; // Enumeration<QuestionnaireItemType>
        } else if (name.equals("enableWhen")) {
          this.getEnableWhen().add((QuestionnaireItemEnableWhenComponent) value);
        } else if (name.equals("enableBehavior")) {
          value = new EnableWhenBehaviorEnumFactory().fromType(castToCode(value));
          this.enableBehavior = (Enumeration) value; // Enumeration<EnableWhenBehavior>
        } else if (name.equals("required")) {
          this.required = castToBoolean(value); // BooleanType
        } else if (name.equals("repeats")) {
          this.repeats = castToBoolean(value); // BooleanType
        } else if (name.equals("readOnly")) {
          this.readOnly = castToBoolean(value); // BooleanType
        } else if (name.equals("maxLength")) {
          this.maxLength = castToInteger(value); // IntegerType
        } else if (name.equals("answerValueSet")) {
          this.answerValueSet = castToCanonical(value); // CanonicalType
        } else if (name.equals("answerOption")) {
          this.getAnswerOption().add((QuestionnaireItemAnswerOptionComponent) value);
        } else if (name.equals("initial")) {
          this.getInitial().add((QuestionnaireItemInitialComponent) value);
        } else if (name.equals("item")) {
          this.getItem().add((QuestionnaireItemComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1102667083:  return getLinkIdElement();
        case -1014418093:  return getDefinitionElement();
        case 3059181:  return addCode(); 
        case -980110702:  return getPrefixElement();
        case 3556653:  return getTextElement();
        case 3575610:  return getTypeElement();
        case 1893321565:  return addEnableWhen(); 
        case 1854802165:  return getEnableBehaviorElement();
        case -393139297:  return getRequiredElement();
        case 1094288952:  return getRepeatsElement();
        case -867683742:  return getReadOnlyElement();
        case -791400086:  return getMaxLengthElement();
        case -743278833:  return getAnswerValueSetElement();
        case -1527878189:  return addAnswerOption(); 
        case 1948342084:  return addInitial(); 
        case 3242771:  return addItem(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1102667083: /*linkId*/ return new String[] {"string"};
        case -1014418093: /*definition*/ return new String[] {"uri"};
        case 3059181: /*code*/ return new String[] {"Coding"};
        case -980110702: /*prefix*/ return new String[] {"string"};
        case 3556653: /*text*/ return new String[] {"string"};
        case 3575610: /*type*/ return new String[] {"code"};
        case 1893321565: /*enableWhen*/ return new String[] {};
        case 1854802165: /*enableBehavior*/ return new String[] {"code"};
        case -393139297: /*required*/ return new String[] {"boolean"};
        case 1094288952: /*repeats*/ return new String[] {"boolean"};
        case -867683742: /*readOnly*/ return new String[] {"boolean"};
        case -791400086: /*maxLength*/ return new String[] {"integer"};
        case -743278833: /*answerValueSet*/ return new String[] {"canonical"};
        case -1527878189: /*answerOption*/ return new String[] {};
        case 1948342084: /*initial*/ return new String[] {};
        case 3242771: /*item*/ return new String[] {"@Questionnaire.item"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("linkId")) {
          throw new FHIRException("Cannot call addChild on a primitive type Questionnaire.linkId");
        }
        else if (name.equals("definition")) {
          throw new FHIRException("Cannot call addChild on a primitive type Questionnaire.definition");
        }
        else if (name.equals("code")) {
          return addCode();
        }
        else if (name.equals("prefix")) {
          throw new FHIRException("Cannot call addChild on a primitive type Questionnaire.prefix");
        }
        else if (name.equals("text")) {
          throw new FHIRException("Cannot call addChild on a primitive type Questionnaire.text");
        }
        else if (name.equals("type")) {
          throw new FHIRException("Cannot call addChild on a primitive type Questionnaire.type");
        }
        else if (name.equals("enableWhen")) {
          return addEnableWhen();
        }
        else if (name.equals("enableBehavior")) {
          throw new FHIRException("Cannot call addChild on a primitive type Questionnaire.enableBehavior");
        }
        else if (name.equals("required")) {
          throw new FHIRException("Cannot call addChild on a primitive type Questionnaire.required");
        }
        else if (name.equals("repeats")) {
          throw new FHIRException("Cannot call addChild on a primitive type Questionnaire.repeats");
        }
        else if (name.equals("readOnly")) {
          throw new FHIRException("Cannot call addChild on a primitive type Questionnaire.readOnly");
        }
        else if (name.equals("maxLength")) {
          throw new FHIRException("Cannot call addChild on a primitive type Questionnaire.maxLength");
        }
        else if (name.equals("answerValueSet")) {
          throw new FHIRException("Cannot call addChild on a primitive type Questionnaire.answerValueSet");
        }
        else if (name.equals("answerOption")) {
          return addAnswerOption();
        }
        else if (name.equals("initial")) {
          return addInitial();
        }
        else if (name.equals("item")) {
          return addItem();
        }
        else
          return super.addChild(name);
      }

      public QuestionnaireItemComponent copy() {
        QuestionnaireItemComponent dst = new QuestionnaireItemComponent();
        copyValues(dst);
        dst.linkId = linkId == null ? null : linkId.copy();
        dst.definition = definition == null ? null : definition.copy();
        if (code != null) {
          dst.code = new ArrayList<Coding>();
          for (Coding i : code)
            dst.code.add(i.copy());
        };
        dst.prefix = prefix == null ? null : prefix.copy();
        dst.text = text == null ? null : text.copy();
        dst.type = type == null ? null : type.copy();
        if (enableWhen != null) {
          dst.enableWhen = new ArrayList<QuestionnaireItemEnableWhenComponent>();
          for (QuestionnaireItemEnableWhenComponent i : enableWhen)
            dst.enableWhen.add(i.copy());
        };
        dst.enableBehavior = enableBehavior == null ? null : enableBehavior.copy();
        dst.required = required == null ? null : required.copy();
        dst.repeats = repeats == null ? null : repeats.copy();
        dst.readOnly = readOnly == null ? null : readOnly.copy();
        dst.maxLength = maxLength == null ? null : maxLength.copy();
        dst.answerValueSet = answerValueSet == null ? null : answerValueSet.copy();
        if (answerOption != null) {
          dst.answerOption = new ArrayList<QuestionnaireItemAnswerOptionComponent>();
          for (QuestionnaireItemAnswerOptionComponent i : answerOption)
            dst.answerOption.add(i.copy());
        };
        if (initial != null) {
          dst.initial = new ArrayList<QuestionnaireItemInitialComponent>();
          for (QuestionnaireItemInitialComponent i : initial)
            dst.initial.add(i.copy());
        };
        if (item != null) {
          dst.item = new ArrayList<QuestionnaireItemComponent>();
          for (QuestionnaireItemComponent i : item)
            dst.item.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof QuestionnaireItemComponent))
          return false;
        QuestionnaireItemComponent o = (QuestionnaireItemComponent) other_;
        return compareDeep(linkId, o.linkId, true) && compareDeep(definition, o.definition, true) && compareDeep(code, o.code, true)
           && compareDeep(prefix, o.prefix, true) && compareDeep(text, o.text, true) && compareDeep(type, o.type, true)
           && compareDeep(enableWhen, o.enableWhen, true) && compareDeep(enableBehavior, o.enableBehavior, true)
           && compareDeep(required, o.required, true) && compareDeep(repeats, o.repeats, true) && compareDeep(readOnly, o.readOnly, true)
           && compareDeep(maxLength, o.maxLength, true) && compareDeep(answerValueSet, o.answerValueSet, true)
           && compareDeep(answerOption, o.answerOption, true) && compareDeep(initial, o.initial, true) && compareDeep(item, o.item, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof QuestionnaireItemComponent))
          return false;
        QuestionnaireItemComponent o = (QuestionnaireItemComponent) other_;
        return compareValues(linkId, o.linkId, true) && compareValues(definition, o.definition, true) && compareValues(prefix, o.prefix, true)
           && compareValues(text, o.text, true) && compareValues(type, o.type, true) && compareValues(enableBehavior, o.enableBehavior, true)
           && compareValues(required, o.required, true) && compareValues(repeats, o.repeats, true) && compareValues(readOnly, o.readOnly, true)
           && compareValues(maxLength, o.maxLength, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(linkId, definition, code
          , prefix, text, type, enableWhen, enableBehavior, required, repeats, readOnly
          , maxLength, answerValueSet, answerOption, initial, item);
      }

  public String fhirType() {
    return "Questionnaire.item";

  }

  }

    @Block()
    public static class QuestionnaireItemEnableWhenComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The linkId for the question whose answer (or lack of answer) governs whether this item is enabled.
         */
        @Child(name = "question", type = {StringType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Question that determines whether item is enabled", formalDefinition="The linkId for the question whose answer (or lack of answer) governs whether this item is enabled." )
        protected StringType question;

        /**
         * Specifies the criteria by which the question is enabled.
         */
        @Child(name = "operator", type = {CodeType.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="exists | = | != | > | < | >= | <=", formalDefinition="Specifies the criteria by which the question is enabled." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/questionnaire-enable-operator")
        protected Enumeration<QuestionnaireItemOperator> operator;

        /**
         * A value that the referenced question is tested using the specified operator in order for the item to be enabled.
         */
        @Child(name = "answer", type = {BooleanType.class, DecimalType.class, IntegerType.class, DateType.class, DateTimeType.class, TimeType.class, StringType.class, Coding.class, Quantity.class, Reference.class}, order=3, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Value for question comparison based on operator", formalDefinition="A value that the referenced question is tested using the specified operator in order for the item to be enabled." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/questionnaire-answers")
        protected Type answer;

        private static final long serialVersionUID = -1815133868L;

    /**
     * Constructor
     */
      public QuestionnaireItemEnableWhenComponent() {
        super();
      }

    /**
     * Constructor
     */
      public QuestionnaireItemEnableWhenComponent(StringType question, Enumeration<QuestionnaireItemOperator> operator, Type answer) {
        super();
        this.question = question;
        this.operator = operator;
        this.answer = answer;
      }

        /**
         * @return {@link #question} (The linkId for the question whose answer (or lack of answer) governs whether this item is enabled.). This is the underlying object with id, value and extensions. The accessor "getQuestion" gives direct access to the value
         */
        public StringType getQuestionElement() { 
          if (this.question == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create QuestionnaireItemEnableWhenComponent.question");
            else if (Configuration.doAutoCreate())
              this.question = new StringType(); // bb
          return this.question;
        }

        public boolean hasQuestionElement() { 
          return this.question != null && !this.question.isEmpty();
        }

        public boolean hasQuestion() { 
          return this.question != null && !this.question.isEmpty();
        }

        /**
         * @param value {@link #question} (The linkId for the question whose answer (or lack of answer) governs whether this item is enabled.). This is the underlying object with id, value and extensions. The accessor "getQuestion" gives direct access to the value
         */
        public QuestionnaireItemEnableWhenComponent setQuestionElement(StringType value) { 
          this.question = value;
          return this;
        }

        /**
         * @return The linkId for the question whose answer (or lack of answer) governs whether this item is enabled.
         */
        public String getQuestion() { 
          return this.question == null ? null : this.question.getValue();
        }

        /**
         * @param value The linkId for the question whose answer (or lack of answer) governs whether this item is enabled.
         */
        public QuestionnaireItemEnableWhenComponent setQuestion(String value) { 
            if (this.question == null)
              this.question = new StringType();
            this.question.setValue(value);
          return this;
        }

        /**
         * @return {@link #operator} (Specifies the criteria by which the question is enabled.). This is the underlying object with id, value and extensions. The accessor "getOperator" gives direct access to the value
         */
        public Enumeration<QuestionnaireItemOperator> getOperatorElement() { 
          if (this.operator == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create QuestionnaireItemEnableWhenComponent.operator");
            else if (Configuration.doAutoCreate())
              this.operator = new Enumeration<QuestionnaireItemOperator>(new QuestionnaireItemOperatorEnumFactory()); // bb
          return this.operator;
        }

        public boolean hasOperatorElement() { 
          return this.operator != null && !this.operator.isEmpty();
        }

        public boolean hasOperator() { 
          return this.operator != null && !this.operator.isEmpty();
        }

        /**
         * @param value {@link #operator} (Specifies the criteria by which the question is enabled.). This is the underlying object with id, value and extensions. The accessor "getOperator" gives direct access to the value
         */
        public QuestionnaireItemEnableWhenComponent setOperatorElement(Enumeration<QuestionnaireItemOperator> value) { 
          this.operator = value;
          return this;
        }

        /**
         * @return Specifies the criteria by which the question is enabled.
         */
        public QuestionnaireItemOperator getOperator() { 
          return this.operator == null ? null : this.operator.getValue();
        }

        /**
         * @param value Specifies the criteria by which the question is enabled.
         */
        public QuestionnaireItemEnableWhenComponent setOperator(QuestionnaireItemOperator value) { 
            if (this.operator == null)
              this.operator = new Enumeration<QuestionnaireItemOperator>(new QuestionnaireItemOperatorEnumFactory());
            this.operator.setValue(value);
          return this;
        }

        /**
         * @return {@link #answer} (A value that the referenced question is tested using the specified operator in order for the item to be enabled.)
         */
        public Type getAnswer() { 
          return this.answer;
        }

        /**
         * @return {@link #answer} (A value that the referenced question is tested using the specified operator in order for the item to be enabled.)
         */
        public BooleanType getAnswerBooleanType() throws FHIRException { 
          if (this.answer == null)
            this.answer = new BooleanType();
          if (!(this.answer instanceof BooleanType))
            throw new FHIRException("Type mismatch: the type BooleanType was expected, but "+this.answer.getClass().getName()+" was encountered");
          return (BooleanType) this.answer;
        }

        public boolean hasAnswerBooleanType() { 
          return this != null && this.answer instanceof BooleanType;
        }

        /**
         * @return {@link #answer} (A value that the referenced question is tested using the specified operator in order for the item to be enabled.)
         */
        public DecimalType getAnswerDecimalType() throws FHIRException { 
          if (this.answer == null)
            this.answer = new DecimalType();
          if (!(this.answer instanceof DecimalType))
            throw new FHIRException("Type mismatch: the type DecimalType was expected, but "+this.answer.getClass().getName()+" was encountered");
          return (DecimalType) this.answer;
        }

        public boolean hasAnswerDecimalType() { 
          return this != null && this.answer instanceof DecimalType;
        }

        /**
         * @return {@link #answer} (A value that the referenced question is tested using the specified operator in order for the item to be enabled.)
         */
        public IntegerType getAnswerIntegerType() throws FHIRException { 
          if (this.answer == null)
            this.answer = new IntegerType();
          if (!(this.answer instanceof IntegerType))
            throw new FHIRException("Type mismatch: the type IntegerType was expected, but "+this.answer.getClass().getName()+" was encountered");
          return (IntegerType) this.answer;
        }

        public boolean hasAnswerIntegerType() { 
          return this != null && this.answer instanceof IntegerType;
        }

        /**
         * @return {@link #answer} (A value that the referenced question is tested using the specified operator in order for the item to be enabled.)
         */
        public DateType getAnswerDateType() throws FHIRException { 
          if (this.answer == null)
            this.answer = new DateType();
          if (!(this.answer instanceof DateType))
            throw new FHIRException("Type mismatch: the type DateType was expected, but "+this.answer.getClass().getName()+" was encountered");
          return (DateType) this.answer;
        }

        public boolean hasAnswerDateType() { 
          return this != null && this.answer instanceof DateType;
        }

        /**
         * @return {@link #answer} (A value that the referenced question is tested using the specified operator in order for the item to be enabled.)
         */
        public DateTimeType getAnswerDateTimeType() throws FHIRException { 
          if (this.answer == null)
            this.answer = new DateTimeType();
          if (!(this.answer instanceof DateTimeType))
            throw new FHIRException("Type mismatch: the type DateTimeType was expected, but "+this.answer.getClass().getName()+" was encountered");
          return (DateTimeType) this.answer;
        }

        public boolean hasAnswerDateTimeType() { 
          return this != null && this.answer instanceof DateTimeType;
        }

        /**
         * @return {@link #answer} (A value that the referenced question is tested using the specified operator in order for the item to be enabled.)
         */
        public TimeType getAnswerTimeType() throws FHIRException { 
          if (this.answer == null)
            this.answer = new TimeType();
          if (!(this.answer instanceof TimeType))
            throw new FHIRException("Type mismatch: the type TimeType was expected, but "+this.answer.getClass().getName()+" was encountered");
          return (TimeType) this.answer;
        }

        public boolean hasAnswerTimeType() { 
          return this != null && this.answer instanceof TimeType;
        }

        /**
         * @return {@link #answer} (A value that the referenced question is tested using the specified operator in order for the item to be enabled.)
         */
        public StringType getAnswerStringType() throws FHIRException { 
          if (this.answer == null)
            this.answer = new StringType();
          if (!(this.answer instanceof StringType))
            throw new FHIRException("Type mismatch: the type StringType was expected, but "+this.answer.getClass().getName()+" was encountered");
          return (StringType) this.answer;
        }

        public boolean hasAnswerStringType() { 
          return this != null && this.answer instanceof StringType;
        }

        /**
         * @return {@link #answer} (A value that the referenced question is tested using the specified operator in order for the item to be enabled.)
         */
        public Coding getAnswerCoding() throws FHIRException { 
          if (this.answer == null)
            this.answer = new Coding();
          if (!(this.answer instanceof Coding))
            throw new FHIRException("Type mismatch: the type Coding was expected, but "+this.answer.getClass().getName()+" was encountered");
          return (Coding) this.answer;
        }

        public boolean hasAnswerCoding() { 
          return this != null && this.answer instanceof Coding;
        }

        /**
         * @return {@link #answer} (A value that the referenced question is tested using the specified operator in order for the item to be enabled.)
         */
        public Quantity getAnswerQuantity() throws FHIRException { 
          if (this.answer == null)
            this.answer = new Quantity();
          if (!(this.answer instanceof Quantity))
            throw new FHIRException("Type mismatch: the type Quantity was expected, but "+this.answer.getClass().getName()+" was encountered");
          return (Quantity) this.answer;
        }

        public boolean hasAnswerQuantity() { 
          return this != null && this.answer instanceof Quantity;
        }

        /**
         * @return {@link #answer} (A value that the referenced question is tested using the specified operator in order for the item to be enabled.)
         */
        public Reference getAnswerReference() throws FHIRException { 
          if (this.answer == null)
            this.answer = new Reference();
          if (!(this.answer instanceof Reference))
            throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.answer.getClass().getName()+" was encountered");
          return (Reference) this.answer;
        }

        public boolean hasAnswerReference() { 
          return this != null && this.answer instanceof Reference;
        }

        public boolean hasAnswer() { 
          return this.answer != null && !this.answer.isEmpty();
        }

        /**
         * @param value {@link #answer} (A value that the referenced question is tested using the specified operator in order for the item to be enabled.)
         */
        public QuestionnaireItemEnableWhenComponent setAnswer(Type value) { 
          if (value != null && !(value instanceof BooleanType || value instanceof DecimalType || value instanceof IntegerType || value instanceof DateType || value instanceof DateTimeType || value instanceof TimeType || value instanceof StringType || value instanceof Coding || value instanceof Quantity || value instanceof Reference))
            throw new Error("Not the right type for Questionnaire.item.enableWhen.answer[x]: "+value.fhirType());
          this.answer = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("question", "string", "The linkId for the question whose answer (or lack of answer) governs whether this item is enabled.", 0, 1, question));
          children.add(new Property("operator", "code", "Specifies the criteria by which the question is enabled.", 0, 1, operator));
          children.add(new Property("answer[x]", "boolean|decimal|integer|date|dateTime|time|string|Coding|Quantity|Reference(Any)", "A value that the referenced question is tested using the specified operator in order for the item to be enabled.", 0, 1, answer));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -1165870106: /*question*/  return new Property("question", "string", "The linkId for the question whose answer (or lack of answer) governs whether this item is enabled.", 0, 1, question);
          case -500553564: /*operator*/  return new Property("operator", "code", "Specifies the criteria by which the question is enabled.", 0, 1, operator);
          case 1693524994: /*answer[x]*/  return new Property("answer[x]", "boolean|decimal|integer|date|dateTime|time|string|Coding|Quantity|Reference(Any)", "A value that the referenced question is tested using the specified operator in order for the item to be enabled.", 0, 1, answer);
          case -1412808770: /*answer*/  return new Property("answer[x]", "boolean|decimal|integer|date|dateTime|time|string|Coding|Quantity|Reference(Any)", "A value that the referenced question is tested using the specified operator in order for the item to be enabled.", 0, 1, answer);
          case 1194603146: /*answerBoolean*/  return new Property("answer[x]", "boolean|decimal|integer|date|dateTime|time|string|Coding|Quantity|Reference(Any)", "A value that the referenced question is tested using the specified operator in order for the item to be enabled.", 0, 1, answer);
          case -1622812237: /*answerDecimal*/  return new Property("answer[x]", "boolean|decimal|integer|date|dateTime|time|string|Coding|Quantity|Reference(Any)", "A value that the referenced question is tested using the specified operator in order for the item to be enabled.", 0, 1, answer);
          case -1207023712: /*answerInteger*/  return new Property("answer[x]", "boolean|decimal|integer|date|dateTime|time|string|Coding|Quantity|Reference(Any)", "A value that the referenced question is tested using the specified operator in order for the item to be enabled.", 0, 1, answer);
          case 958960780: /*answerDate*/  return new Property("answer[x]", "boolean|decimal|integer|date|dateTime|time|string|Coding|Quantity|Reference(Any)", "A value that the referenced question is tested using the specified operator in order for the item to be enabled.", 0, 1, answer);
          case -1835321991: /*answerDateTime*/  return new Property("answer[x]", "boolean|decimal|integer|date|dateTime|time|string|Coding|Quantity|Reference(Any)", "A value that the referenced question is tested using the specified operator in order for the item to be enabled.", 0, 1, answer);
          case 959444907: /*answerTime*/  return new Property("answer[x]", "boolean|decimal|integer|date|dateTime|time|string|Coding|Quantity|Reference(Any)", "A value that the referenced question is tested using the specified operator in order for the item to be enabled.", 0, 1, answer);
          case -1409727121: /*answerString*/  return new Property("answer[x]", "boolean|decimal|integer|date|dateTime|time|string|Coding|Quantity|Reference(Any)", "A value that the referenced question is tested using the specified operator in order for the item to be enabled.", 0, 1, answer);
          case -1872828216: /*answerCoding*/  return new Property("answer[x]", "boolean|decimal|integer|date|dateTime|time|string|Coding|Quantity|Reference(Any)", "A value that the referenced question is tested using the specified operator in order for the item to be enabled.", 0, 1, answer);
          case -618108311: /*answerQuantity*/  return new Property("answer[x]", "boolean|decimal|integer|date|dateTime|time|string|Coding|Quantity|Reference(Any)", "A value that the referenced question is tested using the specified operator in order for the item to be enabled.", 0, 1, answer);
          case -1726221011: /*answerReference*/  return new Property("answer[x]", "boolean|decimal|integer|date|dateTime|time|string|Coding|Quantity|Reference(Any)", "A value that the referenced question is tested using the specified operator in order for the item to be enabled.", 0, 1, answer);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1165870106: /*question*/ return this.question == null ? new Base[0] : new Base[] {this.question}; // StringType
        case -500553564: /*operator*/ return this.operator == null ? new Base[0] : new Base[] {this.operator}; // Enumeration<QuestionnaireItemOperator>
        case -1412808770: /*answer*/ return this.answer == null ? new Base[0] : new Base[] {this.answer}; // Type
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1165870106: // question
          this.question = castToString(value); // StringType
          return value;
        case -500553564: // operator
          value = new QuestionnaireItemOperatorEnumFactory().fromType(castToCode(value));
          this.operator = (Enumeration) value; // Enumeration<QuestionnaireItemOperator>
          return value;
        case -1412808770: // answer
          this.answer = castToType(value); // Type
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("question")) {
          this.question = castToString(value); // StringType
        } else if (name.equals("operator")) {
          value = new QuestionnaireItemOperatorEnumFactory().fromType(castToCode(value));
          this.operator = (Enumeration) value; // Enumeration<QuestionnaireItemOperator>
        } else if (name.equals("answer[x]")) {
          this.answer = castToType(value); // Type
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1165870106:  return getQuestionElement();
        case -500553564:  return getOperatorElement();
        case 1693524994:  return getAnswer(); 
        case -1412808770:  return getAnswer(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1165870106: /*question*/ return new String[] {"string"};
        case -500553564: /*operator*/ return new String[] {"code"};
        case -1412808770: /*answer*/ return new String[] {"boolean", "decimal", "integer", "date", "dateTime", "time", "string", "Coding", "Quantity", "Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("question")) {
          throw new FHIRException("Cannot call addChild on a primitive type Questionnaire.question");
        }
        else if (name.equals("operator")) {
          throw new FHIRException("Cannot call addChild on a primitive type Questionnaire.operator");
        }
        else if (name.equals("answerBoolean")) {
          this.answer = new BooleanType();
          return this.answer;
        }
        else if (name.equals("answerDecimal")) {
          this.answer = new DecimalType();
          return this.answer;
        }
        else if (name.equals("answerInteger")) {
          this.answer = new IntegerType();
          return this.answer;
        }
        else if (name.equals("answerDate")) {
          this.answer = new DateType();
          return this.answer;
        }
        else if (name.equals("answerDateTime")) {
          this.answer = new DateTimeType();
          return this.answer;
        }
        else if (name.equals("answerTime")) {
          this.answer = new TimeType();
          return this.answer;
        }
        else if (name.equals("answerString")) {
          this.answer = new StringType();
          return this.answer;
        }
        else if (name.equals("answerCoding")) {
          this.answer = new Coding();
          return this.answer;
        }
        else if (name.equals("answerQuantity")) {
          this.answer = new Quantity();
          return this.answer;
        }
        else if (name.equals("answerReference")) {
          this.answer = new Reference();
          return this.answer;
        }
        else
          return super.addChild(name);
      }

      public QuestionnaireItemEnableWhenComponent copy() {
        QuestionnaireItemEnableWhenComponent dst = new QuestionnaireItemEnableWhenComponent();
        copyValues(dst);
        dst.question = question == null ? null : question.copy();
        dst.operator = operator == null ? null : operator.copy();
        dst.answer = answer == null ? null : answer.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof QuestionnaireItemEnableWhenComponent))
          return false;
        QuestionnaireItemEnableWhenComponent o = (QuestionnaireItemEnableWhenComponent) other_;
        return compareDeep(question, o.question, true) && compareDeep(operator, o.operator, true) && compareDeep(answer, o.answer, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof QuestionnaireItemEnableWhenComponent))
          return false;
        QuestionnaireItemEnableWhenComponent o = (QuestionnaireItemEnableWhenComponent) other_;
        return compareValues(question, o.question, true) && compareValues(operator, o.operator, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(question, operator, answer
          );
      }

  public String fhirType() {
    return "Questionnaire.item.enableWhen";

  }

  }

    @Block()
    public static class QuestionnaireItemAnswerOptionComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A potential answer that's allowed as the answer to this question.
         */
        @Child(name = "value", type = {IntegerType.class, DateType.class, TimeType.class, StringType.class, Coding.class, Reference.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Answer value", formalDefinition="A potential answer that's allowed as the answer to this question." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/questionnaire-answers")
        protected Type value;

        /**
         * Indicates whether the answer value is selected when the list of possible answers is initially shown.
         */
        @Child(name = "initialSelected", type = {BooleanType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Whether option is selected by default", formalDefinition="Indicates whether the answer value is selected when the list of possible answers is initially shown." )
        protected BooleanType initialSelected;

        private static final long serialVersionUID = 1703686148L;

    /**
     * Constructor
     */
      public QuestionnaireItemAnswerOptionComponent() {
        super();
      }

    /**
     * Constructor
     */
      public QuestionnaireItemAnswerOptionComponent(Type value) {
        super();
        this.value = value;
      }

        /**
         * @return {@link #value} (A potential answer that's allowed as the answer to this question.)
         */
        public Type getValue() { 
          return this.value;
        }

        /**
         * @return {@link #value} (A potential answer that's allowed as the answer to this question.)
         */
        public IntegerType getValueIntegerType() throws FHIRException { 
          if (this.value == null)
            this.value = new IntegerType();
          if (!(this.value instanceof IntegerType))
            throw new FHIRException("Type mismatch: the type IntegerType was expected, but "+this.value.getClass().getName()+" was encountered");
          return (IntegerType) this.value;
        }

        public boolean hasValueIntegerType() { 
          return this != null && this.value instanceof IntegerType;
        }

        /**
         * @return {@link #value} (A potential answer that's allowed as the answer to this question.)
         */
        public DateType getValueDateType() throws FHIRException { 
          if (this.value == null)
            this.value = new DateType();
          if (!(this.value instanceof DateType))
            throw new FHIRException("Type mismatch: the type DateType was expected, but "+this.value.getClass().getName()+" was encountered");
          return (DateType) this.value;
        }

        public boolean hasValueDateType() { 
          return this != null && this.value instanceof DateType;
        }

        /**
         * @return {@link #value} (A potential answer that's allowed as the answer to this question.)
         */
        public TimeType getValueTimeType() throws FHIRException { 
          if (this.value == null)
            this.value = new TimeType();
          if (!(this.value instanceof TimeType))
            throw new FHIRException("Type mismatch: the type TimeType was expected, but "+this.value.getClass().getName()+" was encountered");
          return (TimeType) this.value;
        }

        public boolean hasValueTimeType() { 
          return this != null && this.value instanceof TimeType;
        }

        /**
         * @return {@link #value} (A potential answer that's allowed as the answer to this question.)
         */
        public StringType getValueStringType() throws FHIRException { 
          if (this.value == null)
            this.value = new StringType();
          if (!(this.value instanceof StringType))
            throw new FHIRException("Type mismatch: the type StringType was expected, but "+this.value.getClass().getName()+" was encountered");
          return (StringType) this.value;
        }

        public boolean hasValueStringType() { 
          return this != null && this.value instanceof StringType;
        }

        /**
         * @return {@link #value} (A potential answer that's allowed as the answer to this question.)
         */
        public Coding getValueCoding() throws FHIRException { 
          if (this.value == null)
            this.value = new Coding();
          if (!(this.value instanceof Coding))
            throw new FHIRException("Type mismatch: the type Coding was expected, but "+this.value.getClass().getName()+" was encountered");
          return (Coding) this.value;
        }

        public boolean hasValueCoding() { 
          return this != null && this.value instanceof Coding;
        }

        /**
         * @return {@link #value} (A potential answer that's allowed as the answer to this question.)
         */
        public Reference getValueReference() throws FHIRException { 
          if (this.value == null)
            this.value = new Reference();
          if (!(this.value instanceof Reference))
            throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.value.getClass().getName()+" was encountered");
          return (Reference) this.value;
        }

        public boolean hasValueReference() { 
          return this != null && this.value instanceof Reference;
        }

        public boolean hasValue() { 
          return this.value != null && !this.value.isEmpty();
        }

        /**
         * @param value {@link #value} (A potential answer that's allowed as the answer to this question.)
         */
        public QuestionnaireItemAnswerOptionComponent setValue(Type value) { 
          if (value != null && !(value instanceof IntegerType || value instanceof DateType || value instanceof TimeType || value instanceof StringType || value instanceof Coding || value instanceof Reference))
            throw new Error("Not the right type for Questionnaire.item.answerOption.value[x]: "+value.fhirType());
          this.value = value;
          return this;
        }

        /**
         * @return {@link #initialSelected} (Indicates whether the answer value is selected when the list of possible answers is initially shown.). This is the underlying object with id, value and extensions. The accessor "getInitialSelected" gives direct access to the value
         */
        public BooleanType getInitialSelectedElement() { 
          if (this.initialSelected == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create QuestionnaireItemAnswerOptionComponent.initialSelected");
            else if (Configuration.doAutoCreate())
              this.initialSelected = new BooleanType(); // bb
          return this.initialSelected;
        }

        public boolean hasInitialSelectedElement() { 
          return this.initialSelected != null && !this.initialSelected.isEmpty();
        }

        public boolean hasInitialSelected() { 
          return this.initialSelected != null && !this.initialSelected.isEmpty();
        }

        /**
         * @param value {@link #initialSelected} (Indicates whether the answer value is selected when the list of possible answers is initially shown.). This is the underlying object with id, value and extensions. The accessor "getInitialSelected" gives direct access to the value
         */
        public QuestionnaireItemAnswerOptionComponent setInitialSelectedElement(BooleanType value) { 
          this.initialSelected = value;
          return this;
        }

        /**
         * @return Indicates whether the answer value is selected when the list of possible answers is initially shown.
         */
        public boolean getInitialSelected() { 
          return this.initialSelected == null || this.initialSelected.isEmpty() ? false : this.initialSelected.getValue();
        }

        /**
         * @param value Indicates whether the answer value is selected when the list of possible answers is initially shown.
         */
        public QuestionnaireItemAnswerOptionComponent setInitialSelected(boolean value) { 
            if (this.initialSelected == null)
              this.initialSelected = new BooleanType();
            this.initialSelected.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("value[x]", "integer|date|time|string|Coding|Reference(Any)", "A potential answer that's allowed as the answer to this question.", 0, 1, value));
          children.add(new Property("initialSelected", "boolean", "Indicates whether the answer value is selected when the list of possible answers is initially shown.", 0, 1, initialSelected));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -1410166417: /*value[x]*/  return new Property("value[x]", "integer|date|time|string|Coding|Reference(Any)", "A potential answer that's allowed as the answer to this question.", 0, 1, value);
          case 111972721: /*value*/  return new Property("value[x]", "integer|date|time|string|Coding|Reference(Any)", "A potential answer that's allowed as the answer to this question.", 0, 1, value);
          case -1668204915: /*valueInteger*/  return new Property("value[x]", "integer|date|time|string|Coding|Reference(Any)", "A potential answer that's allowed as the answer to this question.", 0, 1, value);
          case -766192449: /*valueDate*/  return new Property("value[x]", "integer|date|time|string|Coding|Reference(Any)", "A potential answer that's allowed as the answer to this question.", 0, 1, value);
          case -765708322: /*valueTime*/  return new Property("value[x]", "integer|date|time|string|Coding|Reference(Any)", "A potential answer that's allowed as the answer to this question.", 0, 1, value);
          case -1424603934: /*valueString*/  return new Property("value[x]", "integer|date|time|string|Coding|Reference(Any)", "A potential answer that's allowed as the answer to this question.", 0, 1, value);
          case -1887705029: /*valueCoding*/  return new Property("value[x]", "integer|date|time|string|Coding|Reference(Any)", "A potential answer that's allowed as the answer to this question.", 0, 1, value);
          case 1755241690: /*valueReference*/  return new Property("value[x]", "integer|date|time|string|Coding|Reference(Any)", "A potential answer that's allowed as the answer to this question.", 0, 1, value);
          case -1310184961: /*initialSelected*/  return new Property("initialSelected", "boolean", "Indicates whether the answer value is selected when the list of possible answers is initially shown.", 0, 1, initialSelected);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 111972721: /*value*/ return this.value == null ? new Base[0] : new Base[] {this.value}; // Type
        case -1310184961: /*initialSelected*/ return this.initialSelected == null ? new Base[0] : new Base[] {this.initialSelected}; // BooleanType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 111972721: // value
          this.value = castToType(value); // Type
          return value;
        case -1310184961: // initialSelected
          this.initialSelected = castToBoolean(value); // BooleanType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("value[x]")) {
          this.value = castToType(value); // Type
        } else if (name.equals("initialSelected")) {
          this.initialSelected = castToBoolean(value); // BooleanType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1410166417:  return getValue(); 
        case 111972721:  return getValue(); 
        case -1310184961:  return getInitialSelectedElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 111972721: /*value*/ return new String[] {"integer", "date", "time", "string", "Coding", "Reference"};
        case -1310184961: /*initialSelected*/ return new String[] {"boolean"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("valueInteger")) {
          this.value = new IntegerType();
          return this.value;
        }
        else if (name.equals("valueDate")) {
          this.value = new DateType();
          return this.value;
        }
        else if (name.equals("valueTime")) {
          this.value = new TimeType();
          return this.value;
        }
        else if (name.equals("valueString")) {
          this.value = new StringType();
          return this.value;
        }
        else if (name.equals("valueCoding")) {
          this.value = new Coding();
          return this.value;
        }
        else if (name.equals("valueReference")) {
          this.value = new Reference();
          return this.value;
        }
        else if (name.equals("initialSelected")) {
          throw new FHIRException("Cannot call addChild on a primitive type Questionnaire.initialSelected");
        }
        else
          return super.addChild(name);
      }

      public QuestionnaireItemAnswerOptionComponent copy() {
        QuestionnaireItemAnswerOptionComponent dst = new QuestionnaireItemAnswerOptionComponent();
        copyValues(dst);
        dst.value = value == null ? null : value.copy();
        dst.initialSelected = initialSelected == null ? null : initialSelected.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof QuestionnaireItemAnswerOptionComponent))
          return false;
        QuestionnaireItemAnswerOptionComponent o = (QuestionnaireItemAnswerOptionComponent) other_;
        return compareDeep(value, o.value, true) && compareDeep(initialSelected, o.initialSelected, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof QuestionnaireItemAnswerOptionComponent))
          return false;
        QuestionnaireItemAnswerOptionComponent o = (QuestionnaireItemAnswerOptionComponent) other_;
        return compareValues(initialSelected, o.initialSelected, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(value, initialSelected);
      }

  public String fhirType() {
    return "Questionnaire.item.answerOption";

  }

  }

    @Block()
    public static class QuestionnaireItemInitialComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The actual value to for an initial answer.
         */
        @Child(name = "value", type = {BooleanType.class, DecimalType.class, IntegerType.class, DateType.class, DateTimeType.class, TimeType.class, StringType.class, UriType.class, Attachment.class, Coding.class, Quantity.class, Reference.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Actual value for initializing the question", formalDefinition="The actual value to for an initial answer." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/questionnaire-answers")
        protected Type value;

        private static final long serialVersionUID = -732981989L;

    /**
     * Constructor
     */
      public QuestionnaireItemInitialComponent() {
        super();
      }

    /**
     * Constructor
     */
      public QuestionnaireItemInitialComponent(Type value) {
        super();
        this.value = value;
      }

        /**
         * @return {@link #value} (The actual value to for an initial answer.)
         */
        public Type getValue() { 
          return this.value;
        }

        /**
         * @return {@link #value} (The actual value to for an initial answer.)
         */
        public BooleanType getValueBooleanType() throws FHIRException { 
          if (this.value == null)
            this.value = new BooleanType();
          if (!(this.value instanceof BooleanType))
            throw new FHIRException("Type mismatch: the type BooleanType was expected, but "+this.value.getClass().getName()+" was encountered");
          return (BooleanType) this.value;
        }

        public boolean hasValueBooleanType() { 
          return this != null && this.value instanceof BooleanType;
        }

        /**
         * @return {@link #value} (The actual value to for an initial answer.)
         */
        public DecimalType getValueDecimalType() throws FHIRException { 
          if (this.value == null)
            this.value = new DecimalType();
          if (!(this.value instanceof DecimalType))
            throw new FHIRException("Type mismatch: the type DecimalType was expected, but "+this.value.getClass().getName()+" was encountered");
          return (DecimalType) this.value;
        }

        public boolean hasValueDecimalType() { 
          return this != null && this.value instanceof DecimalType;
        }

        /**
         * @return {@link #value} (The actual value to for an initial answer.)
         */
        public IntegerType getValueIntegerType() throws FHIRException { 
          if (this.value == null)
            this.value = new IntegerType();
          if (!(this.value instanceof IntegerType))
            throw new FHIRException("Type mismatch: the type IntegerType was expected, but "+this.value.getClass().getName()+" was encountered");
          return (IntegerType) this.value;
        }

        public boolean hasValueIntegerType() { 
          return this != null && this.value instanceof IntegerType;
        }

        /**
         * @return {@link #value} (The actual value to for an initial answer.)
         */
        public DateType getValueDateType() throws FHIRException { 
          if (this.value == null)
            this.value = new DateType();
          if (!(this.value instanceof DateType))
            throw new FHIRException("Type mismatch: the type DateType was expected, but "+this.value.getClass().getName()+" was encountered");
          return (DateType) this.value;
        }

        public boolean hasValueDateType() { 
          return this != null && this.value instanceof DateType;
        }

        /**
         * @return {@link #value} (The actual value to for an initial answer.)
         */
        public DateTimeType getValueDateTimeType() throws FHIRException { 
          if (this.value == null)
            this.value = new DateTimeType();
          if (!(this.value instanceof DateTimeType))
            throw new FHIRException("Type mismatch: the type DateTimeType was expected, but "+this.value.getClass().getName()+" was encountered");
          return (DateTimeType) this.value;
        }

        public boolean hasValueDateTimeType() { 
          return this != null && this.value instanceof DateTimeType;
        }

        /**
         * @return {@link #value} (The actual value to for an initial answer.)
         */
        public TimeType getValueTimeType() throws FHIRException { 
          if (this.value == null)
            this.value = new TimeType();
          if (!(this.value instanceof TimeType))
            throw new FHIRException("Type mismatch: the type TimeType was expected, but "+this.value.getClass().getName()+" was encountered");
          return (TimeType) this.value;
        }

        public boolean hasValueTimeType() { 
          return this != null && this.value instanceof TimeType;
        }

        /**
         * @return {@link #value} (The actual value to for an initial answer.)
         */
        public StringType getValueStringType() throws FHIRException { 
          if (this.value == null)
            this.value = new StringType();
          if (!(this.value instanceof StringType))
            throw new FHIRException("Type mismatch: the type StringType was expected, but "+this.value.getClass().getName()+" was encountered");
          return (StringType) this.value;
        }

        public boolean hasValueStringType() { 
          return this != null && this.value instanceof StringType;
        }

        /**
         * @return {@link #value} (The actual value to for an initial answer.)
         */
        public UriType getValueUriType() throws FHIRException { 
          if (this.value == null)
            this.value = new UriType();
          if (!(this.value instanceof UriType))
            throw new FHIRException("Type mismatch: the type UriType was expected, but "+this.value.getClass().getName()+" was encountered");
          return (UriType) this.value;
        }

        public boolean hasValueUriType() { 
          return this != null && this.value instanceof UriType;
        }

        /**
         * @return {@link #value} (The actual value to for an initial answer.)
         */
        public Attachment getValueAttachment() throws FHIRException { 
          if (this.value == null)
            this.value = new Attachment();
          if (!(this.value instanceof Attachment))
            throw new FHIRException("Type mismatch: the type Attachment was expected, but "+this.value.getClass().getName()+" was encountered");
          return (Attachment) this.value;
        }

        public boolean hasValueAttachment() { 
          return this != null && this.value instanceof Attachment;
        }

        /**
         * @return {@link #value} (The actual value to for an initial answer.)
         */
        public Coding getValueCoding() throws FHIRException { 
          if (this.value == null)
            this.value = new Coding();
          if (!(this.value instanceof Coding))
            throw new FHIRException("Type mismatch: the type Coding was expected, but "+this.value.getClass().getName()+" was encountered");
          return (Coding) this.value;
        }

        public boolean hasValueCoding() { 
          return this != null && this.value instanceof Coding;
        }

        /**
         * @return {@link #value} (The actual value to for an initial answer.)
         */
        public Quantity getValueQuantity() throws FHIRException { 
          if (this.value == null)
            this.value = new Quantity();
          if (!(this.value instanceof Quantity))
            throw new FHIRException("Type mismatch: the type Quantity was expected, but "+this.value.getClass().getName()+" was encountered");
          return (Quantity) this.value;
        }

        public boolean hasValueQuantity() { 
          return this != null && this.value instanceof Quantity;
        }

        /**
         * @return {@link #value} (The actual value to for an initial answer.)
         */
        public Reference getValueReference() throws FHIRException { 
          if (this.value == null)
            this.value = new Reference();
          if (!(this.value instanceof Reference))
            throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.value.getClass().getName()+" was encountered");
          return (Reference) this.value;
        }

        public boolean hasValueReference() { 
          return this != null && this.value instanceof Reference;
        }

        public boolean hasValue() { 
          return this.value != null && !this.value.isEmpty();
        }

        /**
         * @param value {@link #value} (The actual value to for an initial answer.)
         */
        public QuestionnaireItemInitialComponent setValue(Type value) { 
          if (value != null && !(value instanceof BooleanType || value instanceof DecimalType || value instanceof IntegerType || value instanceof DateType || value instanceof DateTimeType || value instanceof TimeType || value instanceof StringType || value instanceof UriType || value instanceof Attachment || value instanceof Coding || value instanceof Quantity || value instanceof Reference))
            throw new Error("Not the right type for Questionnaire.item.initial.value[x]: "+value.fhirType());
          this.value = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("value[x]", "boolean|decimal|integer|date|dateTime|time|string|uri|Attachment|Coding|Quantity|Reference(Any)", "The actual value to for an initial answer.", 0, 1, value));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -1410166417: /*value[x]*/  return new Property("value[x]", "boolean|decimal|integer|date|dateTime|time|string|uri|Attachment|Coding|Quantity|Reference(Any)", "The actual value to for an initial answer.", 0, 1, value);
          case 111972721: /*value*/  return new Property("value[x]", "boolean|decimal|integer|date|dateTime|time|string|uri|Attachment|Coding|Quantity|Reference(Any)", "The actual value to for an initial answer.", 0, 1, value);
          case 733421943: /*valueBoolean*/  return new Property("value[x]", "boolean|decimal|integer|date|dateTime|time|string|uri|Attachment|Coding|Quantity|Reference(Any)", "The actual value to for an initial answer.", 0, 1, value);
          case -2083993440: /*valueDecimal*/  return new Property("value[x]", "boolean|decimal|integer|date|dateTime|time|string|uri|Attachment|Coding|Quantity|Reference(Any)", "The actual value to for an initial answer.", 0, 1, value);
          case -1668204915: /*valueInteger*/  return new Property("value[x]", "boolean|decimal|integer|date|dateTime|time|string|uri|Attachment|Coding|Quantity|Reference(Any)", "The actual value to for an initial answer.", 0, 1, value);
          case -766192449: /*valueDate*/  return new Property("value[x]", "boolean|decimal|integer|date|dateTime|time|string|uri|Attachment|Coding|Quantity|Reference(Any)", "The actual value to for an initial answer.", 0, 1, value);
          case 1047929900: /*valueDateTime*/  return new Property("value[x]", "boolean|decimal|integer|date|dateTime|time|string|uri|Attachment|Coding|Quantity|Reference(Any)", "The actual value to for an initial answer.", 0, 1, value);
          case -765708322: /*valueTime*/  return new Property("value[x]", "boolean|decimal|integer|date|dateTime|time|string|uri|Attachment|Coding|Quantity|Reference(Any)", "The actual value to for an initial answer.", 0, 1, value);
          case -1424603934: /*valueString*/  return new Property("value[x]", "boolean|decimal|integer|date|dateTime|time|string|uri|Attachment|Coding|Quantity|Reference(Any)", "The actual value to for an initial answer.", 0, 1, value);
          case -1410172357: /*valueUri*/  return new Property("value[x]", "boolean|decimal|integer|date|dateTime|time|string|uri|Attachment|Coding|Quantity|Reference(Any)", "The actual value to for an initial answer.", 0, 1, value);
          case -475566732: /*valueAttachment*/  return new Property("value[x]", "boolean|decimal|integer|date|dateTime|time|string|uri|Attachment|Coding|Quantity|Reference(Any)", "The actual value to for an initial answer.", 0, 1, value);
          case -1887705029: /*valueCoding*/  return new Property("value[x]", "boolean|decimal|integer|date|dateTime|time|string|uri|Attachment|Coding|Quantity|Reference(Any)", "The actual value to for an initial answer.", 0, 1, value);
          case -2029823716: /*valueQuantity*/  return new Property("value[x]", "boolean|decimal|integer|date|dateTime|time|string|uri|Attachment|Coding|Quantity|Reference(Any)", "The actual value to for an initial answer.", 0, 1, value);
          case 1755241690: /*valueReference*/  return new Property("value[x]", "boolean|decimal|integer|date|dateTime|time|string|uri|Attachment|Coding|Quantity|Reference(Any)", "The actual value to for an initial answer.", 0, 1, value);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 111972721: /*value*/ return this.value == null ? new Base[0] : new Base[] {this.value}; // Type
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 111972721: // value
          this.value = castToType(value); // Type
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("value[x]")) {
          this.value = castToType(value); // Type
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1410166417:  return getValue(); 
        case 111972721:  return getValue(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 111972721: /*value*/ return new String[] {"boolean", "decimal", "integer", "date", "dateTime", "time", "string", "uri", "Attachment", "Coding", "Quantity", "Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("valueBoolean")) {
          this.value = new BooleanType();
          return this.value;
        }
        else if (name.equals("valueDecimal")) {
          this.value = new DecimalType();
          return this.value;
        }
        else if (name.equals("valueInteger")) {
          this.value = new IntegerType();
          return this.value;
        }
        else if (name.equals("valueDate")) {
          this.value = new DateType();
          return this.value;
        }
        else if (name.equals("valueDateTime")) {
          this.value = new DateTimeType();
          return this.value;
        }
        else if (name.equals("valueTime")) {
          this.value = new TimeType();
          return this.value;
        }
        else if (name.equals("valueString")) {
          this.value = new StringType();
          return this.value;
        }
        else if (name.equals("valueUri")) {
          this.value = new UriType();
          return this.value;
        }
        else if (name.equals("valueAttachment")) {
          this.value = new Attachment();
          return this.value;
        }
        else if (name.equals("valueCoding")) {
          this.value = new Coding();
          return this.value;
        }
        else if (name.equals("valueQuantity")) {
          this.value = new Quantity();
          return this.value;
        }
        else if (name.equals("valueReference")) {
          this.value = new Reference();
          return this.value;
        }
        else
          return super.addChild(name);
      }

      public QuestionnaireItemInitialComponent copy() {
        QuestionnaireItemInitialComponent dst = new QuestionnaireItemInitialComponent();
        copyValues(dst);
        dst.value = value == null ? null : value.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof QuestionnaireItemInitialComponent))
          return false;
        QuestionnaireItemInitialComponent o = (QuestionnaireItemInitialComponent) other_;
        return compareDeep(value, o.value, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof QuestionnaireItemInitialComponent))
          return false;
        QuestionnaireItemInitialComponent o = (QuestionnaireItemInitialComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(value);
      }

  public String fhirType() {
    return "Questionnaire.item.initial";

  }

  }

    /**
     * A formal identifier that is used to identify this questionnaire when it is represented in other formats, or referenced in a specification, model, design or an instance.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Additional identifier for the questionnaire", formalDefinition="A formal identifier that is used to identify this questionnaire when it is represented in other formats, or referenced in a specification, model, design or an instance." )
    protected List<Identifier> identifier;

    /**
     * The URL of a Questionnaire that this Questionnaire is based on.
     */
    @Child(name = "derivedFrom", type = {CanonicalType.class}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Instantiates protocol or definition", formalDefinition="The URL of a Questionnaire that this Questionnaire is based on." )
    protected List<CanonicalType> derivedFrom;

    /**
     * The types of subjects that can be the subject of responses created for the questionnaire.
     */
    @Child(name = "subjectType", type = {CodeType.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Resource that can be subject of QuestionnaireResponse", formalDefinition="The types of subjects that can be the subject of responses created for the questionnaire." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/resource-types")
    protected List<CodeType> subjectType;

    /**
     * Explanation of why this questionnaire is needed and why it has been designed as it has.
     */
    @Child(name = "purpose", type = {MarkdownType.class}, order=3, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Why this questionnaire is defined", formalDefinition="Explanation of why this questionnaire is needed and why it has been designed as it has." )
    protected MarkdownType purpose;

    /**
     * A copyright statement relating to the questionnaire and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the questionnaire.
     */
    @Child(name = "copyright", type = {MarkdownType.class}, order=4, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Use and/or publishing restrictions", formalDefinition="A copyright statement relating to the questionnaire and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the questionnaire." )
    protected MarkdownType copyright;

    /**
     * The date on which the resource content was approved by the publisher. Approval happens once when the content is officially approved for usage.
     */
    @Child(name = "approvalDate", type = {DateType.class}, order=5, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="When the questionnaire was approved by publisher", formalDefinition="The date on which the resource content was approved by the publisher. Approval happens once when the content is officially approved for usage." )
    protected DateType approvalDate;

    /**
     * The date on which the resource content was last reviewed. Review happens periodically after approval but does not change the original approval date.
     */
    @Child(name = "lastReviewDate", type = {DateType.class}, order=6, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="When the questionnaire was last reviewed", formalDefinition="The date on which the resource content was last reviewed. Review happens periodically after approval but does not change the original approval date." )
    protected DateType lastReviewDate;

    /**
     * The period during which the questionnaire content was or is planned to be in active use.
     */
    @Child(name = "effectivePeriod", type = {Period.class}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When the questionnaire is expected to be used", formalDefinition="The period during which the questionnaire content was or is planned to be in active use." )
    protected Period effectivePeriod;

    /**
     * An identifier for this question or group of questions in a particular terminology such as LOINC.
     */
    @Child(name = "code", type = {Coding.class}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Concept that represents the overall questionnaire", formalDefinition="An identifier for this question or group of questions in a particular terminology such as LOINC." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/questionnaire-questions")
    protected List<Coding> code;

    /**
     * A particular question, question grouping or display text that is part of the questionnaire.
     */
    @Child(name = "item", type = {}, order=9, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Questions and sections within the Questionnaire", formalDefinition="A particular question, question grouping or display text that is part of the questionnaire." )
    protected List<QuestionnaireItemComponent> item;

    private static final long serialVersionUID = 1036031192L;

  /**
   * Constructor
   */
    public Questionnaire() {
      super();
    }

  /**
   * Constructor
   */
    public Questionnaire(Enumeration<PublicationStatus> status) {
      super();
      this.status = status;
    }

    /**
     * @return {@link #url} (An absolute URI that is used to identify this questionnaire when it is referenced in a specification, model, design or an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal address at which at which an authoritative instance of this questionnaire is (or will be) published. This URL can be the target of a canonical reference. It SHALL remain the same when the questionnaire is stored on different servers.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public UriType getUrlElement() { 
      if (this.url == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Questionnaire.url");
        else if (Configuration.doAutoCreate())
          this.url = new UriType(); // bb
      return this.url;
    }

    public boolean hasUrlElement() { 
      return this.url != null && !this.url.isEmpty();
    }

    public boolean hasUrl() { 
      return this.url != null && !this.url.isEmpty();
    }

    /**
     * @param value {@link #url} (An absolute URI that is used to identify this questionnaire when it is referenced in a specification, model, design or an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal address at which at which an authoritative instance of this questionnaire is (or will be) published. This URL can be the target of a canonical reference. It SHALL remain the same when the questionnaire is stored on different servers.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public Questionnaire setUrlElement(UriType value) { 
      this.url = value;
      return this;
    }

    /**
     * @return An absolute URI that is used to identify this questionnaire when it is referenced in a specification, model, design or an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal address at which at which an authoritative instance of this questionnaire is (or will be) published. This URL can be the target of a canonical reference. It SHALL remain the same when the questionnaire is stored on different servers.
     */
    public String getUrl() { 
      return this.url == null ? null : this.url.getValue();
    }

    /**
     * @param value An absolute URI that is used to identify this questionnaire when it is referenced in a specification, model, design or an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal address at which at which an authoritative instance of this questionnaire is (or will be) published. This URL can be the target of a canonical reference. It SHALL remain the same when the questionnaire is stored on different servers.
     */
    public Questionnaire setUrl(String value) { 
      if (Utilities.noString(value))
        this.url = null;
      else {
        if (this.url == null)
          this.url = new UriType();
        this.url.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #identifier} (A formal identifier that is used to identify this questionnaire when it is represented in other formats, or referenced in a specification, model, design or an instance.)
     */
    public List<Identifier> getIdentifier() { 
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      return this.identifier;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Questionnaire setIdentifier(List<Identifier> theIdentifier) { 
      this.identifier = theIdentifier;
      return this;
    }

    public boolean hasIdentifier() { 
      if (this.identifier == null)
        return false;
      for (Identifier item : this.identifier)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Identifier addIdentifier() { //3
      Identifier t = new Identifier();
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return t;
    }

    public Questionnaire addIdentifier(Identifier t) { //3
      if (t == null)
        return this;
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #identifier}, creating it if it does not already exist
     */
    public Identifier getIdentifierFirstRep() { 
      if (getIdentifier().isEmpty()) {
        addIdentifier();
      }
      return getIdentifier().get(0);
    }

    /**
     * @return {@link #version} (The identifier that is used to identify this version of the questionnaire when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the questionnaire author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
     */
    public StringType getVersionElement() { 
      if (this.version == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Questionnaire.version");
        else if (Configuration.doAutoCreate())
          this.version = new StringType(); // bb
      return this.version;
    }

    public boolean hasVersionElement() { 
      return this.version != null && !this.version.isEmpty();
    }

    public boolean hasVersion() { 
      return this.version != null && !this.version.isEmpty();
    }

    /**
     * @param value {@link #version} (The identifier that is used to identify this version of the questionnaire when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the questionnaire author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
     */
    public Questionnaire setVersionElement(StringType value) { 
      this.version = value;
      return this;
    }

    /**
     * @return The identifier that is used to identify this version of the questionnaire when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the questionnaire author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence.
     */
    public String getVersion() { 
      return this.version == null ? null : this.version.getValue();
    }

    /**
     * @param value The identifier that is used to identify this version of the questionnaire when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the questionnaire author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence.
     */
    public Questionnaire setVersion(String value) { 
      if (Utilities.noString(value))
        this.version = null;
      else {
        if (this.version == null)
          this.version = new StringType();
        this.version.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #name} (A natural language name identifying the questionnaire. This name should be usable as an identifier for the module by machine processing applications such as code generation.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public StringType getNameElement() { 
      if (this.name == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Questionnaire.name");
        else if (Configuration.doAutoCreate())
          this.name = new StringType(); // bb
      return this.name;
    }

    public boolean hasNameElement() { 
      return this.name != null && !this.name.isEmpty();
    }

    public boolean hasName() { 
      return this.name != null && !this.name.isEmpty();
    }

    /**
     * @param value {@link #name} (A natural language name identifying the questionnaire. This name should be usable as an identifier for the module by machine processing applications such as code generation.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public Questionnaire setNameElement(StringType value) { 
      this.name = value;
      return this;
    }

    /**
     * @return A natural language name identifying the questionnaire. This name should be usable as an identifier for the module by machine processing applications such as code generation.
     */
    public String getName() { 
      return this.name == null ? null : this.name.getValue();
    }

    /**
     * @param value A natural language name identifying the questionnaire. This name should be usable as an identifier for the module by machine processing applications such as code generation.
     */
    public Questionnaire setName(String value) { 
      if (Utilities.noString(value))
        this.name = null;
      else {
        if (this.name == null)
          this.name = new StringType();
        this.name.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #title} (A short, descriptive, user-friendly title for the questionnaire.). This is the underlying object with id, value and extensions. The accessor "getTitle" gives direct access to the value
     */
    public StringType getTitleElement() { 
      if (this.title == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Questionnaire.title");
        else if (Configuration.doAutoCreate())
          this.title = new StringType(); // bb
      return this.title;
    }

    public boolean hasTitleElement() { 
      return this.title != null && !this.title.isEmpty();
    }

    public boolean hasTitle() { 
      return this.title != null && !this.title.isEmpty();
    }

    /**
     * @param value {@link #title} (A short, descriptive, user-friendly title for the questionnaire.). This is the underlying object with id, value and extensions. The accessor "getTitle" gives direct access to the value
     */
    public Questionnaire setTitleElement(StringType value) { 
      this.title = value;
      return this;
    }

    /**
     * @return A short, descriptive, user-friendly title for the questionnaire.
     */
    public String getTitle() { 
      return this.title == null ? null : this.title.getValue();
    }

    /**
     * @param value A short, descriptive, user-friendly title for the questionnaire.
     */
    public Questionnaire setTitle(String value) { 
      if (Utilities.noString(value))
        this.title = null;
      else {
        if (this.title == null)
          this.title = new StringType();
        this.title.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #derivedFrom} (The URL of a Questionnaire that this Questionnaire is based on.)
     */
    public List<CanonicalType> getDerivedFrom() { 
      if (this.derivedFrom == null)
        this.derivedFrom = new ArrayList<CanonicalType>();
      return this.derivedFrom;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Questionnaire setDerivedFrom(List<CanonicalType> theDerivedFrom) { 
      this.derivedFrom = theDerivedFrom;
      return this;
    }

    public boolean hasDerivedFrom() { 
      if (this.derivedFrom == null)
        return false;
      for (CanonicalType item : this.derivedFrom)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #derivedFrom} (The URL of a Questionnaire that this Questionnaire is based on.)
     */
    public CanonicalType addDerivedFromElement() {//2 
      CanonicalType t = new CanonicalType();
      if (this.derivedFrom == null)
        this.derivedFrom = new ArrayList<CanonicalType>();
      this.derivedFrom.add(t);
      return t;
    }

    /**
     * @param value {@link #derivedFrom} (The URL of a Questionnaire that this Questionnaire is based on.)
     */
    public Questionnaire addDerivedFrom(String value) { //1
      CanonicalType t = new CanonicalType();
      t.setValue(value);
      if (this.derivedFrom == null)
        this.derivedFrom = new ArrayList<CanonicalType>();
      this.derivedFrom.add(t);
      return this;
    }

    /**
     * @param value {@link #derivedFrom} (The URL of a Questionnaire that this Questionnaire is based on.)
     */
    public boolean hasDerivedFrom(String value) { 
      if (this.derivedFrom == null)
        return false;
      for (CanonicalType v : this.derivedFrom)
        if (v.getValue().equals(value)) // canonical(Questionnaire)
          return true;
      return false;
    }

    /**
     * @return {@link #status} (The status of this questionnaire. Enables tracking the life-cycle of the content.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<PublicationStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Questionnaire.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<PublicationStatus>(new PublicationStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (The status of this questionnaire. Enables tracking the life-cycle of the content.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Questionnaire setStatusElement(Enumeration<PublicationStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The status of this questionnaire. Enables tracking the life-cycle of the content.
     */
    public PublicationStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The status of this questionnaire. Enables tracking the life-cycle of the content.
     */
    public Questionnaire setStatus(PublicationStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<PublicationStatus>(new PublicationStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #experimental} (A Boolean value to indicate that this questionnaire is authored for testing purposes (or education/evaluation/marketing) and is not intended to be used for genuine usage.). This is the underlying object with id, value and extensions. The accessor "getExperimental" gives direct access to the value
     */
    public BooleanType getExperimentalElement() { 
      if (this.experimental == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Questionnaire.experimental");
        else if (Configuration.doAutoCreate())
          this.experimental = new BooleanType(); // bb
      return this.experimental;
    }

    public boolean hasExperimentalElement() { 
      return this.experimental != null && !this.experimental.isEmpty();
    }

    public boolean hasExperimental() { 
      return this.experimental != null && !this.experimental.isEmpty();
    }

    /**
     * @param value {@link #experimental} (A Boolean value to indicate that this questionnaire is authored for testing purposes (or education/evaluation/marketing) and is not intended to be used for genuine usage.). This is the underlying object with id, value and extensions. The accessor "getExperimental" gives direct access to the value
     */
    public Questionnaire setExperimentalElement(BooleanType value) { 
      this.experimental = value;
      return this;
    }

    /**
     * @return A Boolean value to indicate that this questionnaire is authored for testing purposes (or education/evaluation/marketing) and is not intended to be used for genuine usage.
     */
    public boolean getExperimental() { 
      return this.experimental == null || this.experimental.isEmpty() ? false : this.experimental.getValue();
    }

    /**
     * @param value A Boolean value to indicate that this questionnaire is authored for testing purposes (or education/evaluation/marketing) and is not intended to be used for genuine usage.
     */
    public Questionnaire setExperimental(boolean value) { 
        if (this.experimental == null)
          this.experimental = new BooleanType();
        this.experimental.setValue(value);
      return this;
    }

    /**
     * @return {@link #subjectType} (The types of subjects that can be the subject of responses created for the questionnaire.)
     */
    public List<CodeType> getSubjectType() { 
      if (this.subjectType == null)
        this.subjectType = new ArrayList<CodeType>();
      return this.subjectType;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Questionnaire setSubjectType(List<CodeType> theSubjectType) { 
      this.subjectType = theSubjectType;
      return this;
    }

    public boolean hasSubjectType() { 
      if (this.subjectType == null)
        return false;
      for (CodeType item : this.subjectType)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #subjectType} (The types of subjects that can be the subject of responses created for the questionnaire.)
     */
    public CodeType addSubjectTypeElement() {//2 
      CodeType t = new CodeType();
      if (this.subjectType == null)
        this.subjectType = new ArrayList<CodeType>();
      this.subjectType.add(t);
      return t;
    }

    /**
     * @param value {@link #subjectType} (The types of subjects that can be the subject of responses created for the questionnaire.)
     */
    public Questionnaire addSubjectType(String value) { //1
      CodeType t = new CodeType();
      t.setValue(value);
      if (this.subjectType == null)
        this.subjectType = new ArrayList<CodeType>();
      this.subjectType.add(t);
      return this;
    }

    /**
     * @param value {@link #subjectType} (The types of subjects that can be the subject of responses created for the questionnaire.)
     */
    public boolean hasSubjectType(String value) { 
      if (this.subjectType == null)
        return false;
      for (CodeType v : this.subjectType)
        if (v.getValue().equals(value)) // code
          return true;
      return false;
    }

    /**
     * @return {@link #date} (The date  (and optionally time) when the questionnaire was published. The date must change when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the questionnaire changes.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public DateTimeType getDateElement() { 
      if (this.date == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Questionnaire.date");
        else if (Configuration.doAutoCreate())
          this.date = new DateTimeType(); // bb
      return this.date;
    }

    public boolean hasDateElement() { 
      return this.date != null && !this.date.isEmpty();
    }

    public boolean hasDate() { 
      return this.date != null && !this.date.isEmpty();
    }

    /**
     * @param value {@link #date} (The date  (and optionally time) when the questionnaire was published. The date must change when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the questionnaire changes.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public Questionnaire setDateElement(DateTimeType value) { 
      this.date = value;
      return this;
    }

    /**
     * @return The date  (and optionally time) when the questionnaire was published. The date must change when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the questionnaire changes.
     */
    public Date getDate() { 
      return this.date == null ? null : this.date.getValue();
    }

    /**
     * @param value The date  (and optionally time) when the questionnaire was published. The date must change when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the questionnaire changes.
     */
    public Questionnaire setDate(Date value) { 
      if (value == null)
        this.date = null;
      else {
        if (this.date == null)
          this.date = new DateTimeType();
        this.date.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #publisher} (The name of the organization or individual that published the questionnaire.). This is the underlying object with id, value and extensions. The accessor "getPublisher" gives direct access to the value
     */
    public StringType getPublisherElement() { 
      if (this.publisher == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Questionnaire.publisher");
        else if (Configuration.doAutoCreate())
          this.publisher = new StringType(); // bb
      return this.publisher;
    }

    public boolean hasPublisherElement() { 
      return this.publisher != null && !this.publisher.isEmpty();
    }

    public boolean hasPublisher() { 
      return this.publisher != null && !this.publisher.isEmpty();
    }

    /**
     * @param value {@link #publisher} (The name of the organization or individual that published the questionnaire.). This is the underlying object with id, value and extensions. The accessor "getPublisher" gives direct access to the value
     */
    public Questionnaire setPublisherElement(StringType value) { 
      this.publisher = value;
      return this;
    }

    /**
     * @return The name of the organization or individual that published the questionnaire.
     */
    public String getPublisher() { 
      return this.publisher == null ? null : this.publisher.getValue();
    }

    /**
     * @param value The name of the organization or individual that published the questionnaire.
     */
    public Questionnaire setPublisher(String value) { 
      if (Utilities.noString(value))
        this.publisher = null;
      else {
        if (this.publisher == null)
          this.publisher = new StringType();
        this.publisher.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #contact} (Contact details to assist a user in finding and communicating with the publisher.)
     */
    public List<ContactDetail> getContact() { 
      if (this.contact == null)
        this.contact = new ArrayList<ContactDetail>();
      return this.contact;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Questionnaire setContact(List<ContactDetail> theContact) { 
      this.contact = theContact;
      return this;
    }

    public boolean hasContact() { 
      if (this.contact == null)
        return false;
      for (ContactDetail item : this.contact)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public ContactDetail addContact() { //3
      ContactDetail t = new ContactDetail();
      if (this.contact == null)
        this.contact = new ArrayList<ContactDetail>();
      this.contact.add(t);
      return t;
    }

    public Questionnaire addContact(ContactDetail t) { //3
      if (t == null)
        return this;
      if (this.contact == null)
        this.contact = new ArrayList<ContactDetail>();
      this.contact.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #contact}, creating it if it does not already exist
     */
    public ContactDetail getContactFirstRep() { 
      if (getContact().isEmpty()) {
        addContact();
      }
      return getContact().get(0);
    }

    /**
     * @return {@link #description} (A free text natural language description of the questionnaire from a consumer's perspective.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public MarkdownType getDescriptionElement() { 
      if (this.description == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Questionnaire.description");
        else if (Configuration.doAutoCreate())
          this.description = new MarkdownType(); // bb
      return this.description;
    }

    public boolean hasDescriptionElement() { 
      return this.description != null && !this.description.isEmpty();
    }

    public boolean hasDescription() { 
      return this.description != null && !this.description.isEmpty();
    }

    /**
     * @param value {@link #description} (A free text natural language description of the questionnaire from a consumer's perspective.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public Questionnaire setDescriptionElement(MarkdownType value) { 
      this.description = value;
      return this;
    }

    /**
     * @return A free text natural language description of the questionnaire from a consumer's perspective.
     */
    public String getDescription() { 
      return this.description == null ? null : this.description.getValue();
    }

    /**
     * @param value A free text natural language description of the questionnaire from a consumer's perspective.
     */
    public Questionnaire setDescription(String value) { 
      if (value == null)
        this.description = null;
      else {
        if (this.description == null)
          this.description = new MarkdownType();
        this.description.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #useContext} (The content was developed with a focus and intent of supporting the contexts that are listed. These contexts may be general categories (gender, age, ...) or may be references to specific programs (insurance plans, studies, ...) and may be used to assist with indexing and searching for appropriate questionnaire instances.)
     */
    public List<UsageContext> getUseContext() { 
      if (this.useContext == null)
        this.useContext = new ArrayList<UsageContext>();
      return this.useContext;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Questionnaire setUseContext(List<UsageContext> theUseContext) { 
      this.useContext = theUseContext;
      return this;
    }

    public boolean hasUseContext() { 
      if (this.useContext == null)
        return false;
      for (UsageContext item : this.useContext)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public UsageContext addUseContext() { //3
      UsageContext t = new UsageContext();
      if (this.useContext == null)
        this.useContext = new ArrayList<UsageContext>();
      this.useContext.add(t);
      return t;
    }

    public Questionnaire addUseContext(UsageContext t) { //3
      if (t == null)
        return this;
      if (this.useContext == null)
        this.useContext = new ArrayList<UsageContext>();
      this.useContext.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #useContext}, creating it if it does not already exist
     */
    public UsageContext getUseContextFirstRep() { 
      if (getUseContext().isEmpty()) {
        addUseContext();
      }
      return getUseContext().get(0);
    }

    /**
     * @return {@link #jurisdiction} (A legal or geographic region in which the questionnaire is intended to be used.)
     */
    public List<CodeableConcept> getJurisdiction() { 
      if (this.jurisdiction == null)
        this.jurisdiction = new ArrayList<CodeableConcept>();
      return this.jurisdiction;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Questionnaire setJurisdiction(List<CodeableConcept> theJurisdiction) { 
      this.jurisdiction = theJurisdiction;
      return this;
    }

    public boolean hasJurisdiction() { 
      if (this.jurisdiction == null)
        return false;
      for (CodeableConcept item : this.jurisdiction)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addJurisdiction() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.jurisdiction == null)
        this.jurisdiction = new ArrayList<CodeableConcept>();
      this.jurisdiction.add(t);
      return t;
    }

    public Questionnaire addJurisdiction(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.jurisdiction == null)
        this.jurisdiction = new ArrayList<CodeableConcept>();
      this.jurisdiction.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #jurisdiction}, creating it if it does not already exist
     */
    public CodeableConcept getJurisdictionFirstRep() { 
      if (getJurisdiction().isEmpty()) {
        addJurisdiction();
      }
      return getJurisdiction().get(0);
    }

    /**
     * @return {@link #purpose} (Explanation of why this questionnaire is needed and why it has been designed as it has.). This is the underlying object with id, value and extensions. The accessor "getPurpose" gives direct access to the value
     */
    public MarkdownType getPurposeElement() { 
      if (this.purpose == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Questionnaire.purpose");
        else if (Configuration.doAutoCreate())
          this.purpose = new MarkdownType(); // bb
      return this.purpose;
    }

    public boolean hasPurposeElement() { 
      return this.purpose != null && !this.purpose.isEmpty();
    }

    public boolean hasPurpose() { 
      return this.purpose != null && !this.purpose.isEmpty();
    }

    /**
     * @param value {@link #purpose} (Explanation of why this questionnaire is needed and why it has been designed as it has.). This is the underlying object with id, value and extensions. The accessor "getPurpose" gives direct access to the value
     */
    public Questionnaire setPurposeElement(MarkdownType value) { 
      this.purpose = value;
      return this;
    }

    /**
     * @return Explanation of why this questionnaire is needed and why it has been designed as it has.
     */
    public String getPurpose() { 
      return this.purpose == null ? null : this.purpose.getValue();
    }

    /**
     * @param value Explanation of why this questionnaire is needed and why it has been designed as it has.
     */
    public Questionnaire setPurpose(String value) { 
      if (value == null)
        this.purpose = null;
      else {
        if (this.purpose == null)
          this.purpose = new MarkdownType();
        this.purpose.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #copyright} (A copyright statement relating to the questionnaire and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the questionnaire.). This is the underlying object with id, value and extensions. The accessor "getCopyright" gives direct access to the value
     */
    public MarkdownType getCopyrightElement() { 
      if (this.copyright == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Questionnaire.copyright");
        else if (Configuration.doAutoCreate())
          this.copyright = new MarkdownType(); // bb
      return this.copyright;
    }

    public boolean hasCopyrightElement() { 
      return this.copyright != null && !this.copyright.isEmpty();
    }

    public boolean hasCopyright() { 
      return this.copyright != null && !this.copyright.isEmpty();
    }

    /**
     * @param value {@link #copyright} (A copyright statement relating to the questionnaire and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the questionnaire.). This is the underlying object with id, value and extensions. The accessor "getCopyright" gives direct access to the value
     */
    public Questionnaire setCopyrightElement(MarkdownType value) { 
      this.copyright = value;
      return this;
    }

    /**
     * @return A copyright statement relating to the questionnaire and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the questionnaire.
     */
    public String getCopyright() { 
      return this.copyright == null ? null : this.copyright.getValue();
    }

    /**
     * @param value A copyright statement relating to the questionnaire and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the questionnaire.
     */
    public Questionnaire setCopyright(String value) { 
      if (value == null)
        this.copyright = null;
      else {
        if (this.copyright == null)
          this.copyright = new MarkdownType();
        this.copyright.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #approvalDate} (The date on which the resource content was approved by the publisher. Approval happens once when the content is officially approved for usage.). This is the underlying object with id, value and extensions. The accessor "getApprovalDate" gives direct access to the value
     */
    public DateType getApprovalDateElement() { 
      if (this.approvalDate == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Questionnaire.approvalDate");
        else if (Configuration.doAutoCreate())
          this.approvalDate = new DateType(); // bb
      return this.approvalDate;
    }

    public boolean hasApprovalDateElement() { 
      return this.approvalDate != null && !this.approvalDate.isEmpty();
    }

    public boolean hasApprovalDate() { 
      return this.approvalDate != null && !this.approvalDate.isEmpty();
    }

    /**
     * @param value {@link #approvalDate} (The date on which the resource content was approved by the publisher. Approval happens once when the content is officially approved for usage.). This is the underlying object with id, value and extensions. The accessor "getApprovalDate" gives direct access to the value
     */
    public Questionnaire setApprovalDateElement(DateType value) { 
      this.approvalDate = value;
      return this;
    }

    /**
     * @return The date on which the resource content was approved by the publisher. Approval happens once when the content is officially approved for usage.
     */
    public Date getApprovalDate() { 
      return this.approvalDate == null ? null : this.approvalDate.getValue();
    }

    /**
     * @param value The date on which the resource content was approved by the publisher. Approval happens once when the content is officially approved for usage.
     */
    public Questionnaire setApprovalDate(Date value) { 
      if (value == null)
        this.approvalDate = null;
      else {
        if (this.approvalDate == null)
          this.approvalDate = new DateType();
        this.approvalDate.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #lastReviewDate} (The date on which the resource content was last reviewed. Review happens periodically after approval but does not change the original approval date.). This is the underlying object with id, value and extensions. The accessor "getLastReviewDate" gives direct access to the value
     */
    public DateType getLastReviewDateElement() { 
      if (this.lastReviewDate == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Questionnaire.lastReviewDate");
        else if (Configuration.doAutoCreate())
          this.lastReviewDate = new DateType(); // bb
      return this.lastReviewDate;
    }

    public boolean hasLastReviewDateElement() { 
      return this.lastReviewDate != null && !this.lastReviewDate.isEmpty();
    }

    public boolean hasLastReviewDate() { 
      return this.lastReviewDate != null && !this.lastReviewDate.isEmpty();
    }

    /**
     * @param value {@link #lastReviewDate} (The date on which the resource content was last reviewed. Review happens periodically after approval but does not change the original approval date.). This is the underlying object with id, value and extensions. The accessor "getLastReviewDate" gives direct access to the value
     */
    public Questionnaire setLastReviewDateElement(DateType value) { 
      this.lastReviewDate = value;
      return this;
    }

    /**
     * @return The date on which the resource content was last reviewed. Review happens periodically after approval but does not change the original approval date.
     */
    public Date getLastReviewDate() { 
      return this.lastReviewDate == null ? null : this.lastReviewDate.getValue();
    }

    /**
     * @param value The date on which the resource content was last reviewed. Review happens periodically after approval but does not change the original approval date.
     */
    public Questionnaire setLastReviewDate(Date value) { 
      if (value == null)
        this.lastReviewDate = null;
      else {
        if (this.lastReviewDate == null)
          this.lastReviewDate = new DateType();
        this.lastReviewDate.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #effectivePeriod} (The period during which the questionnaire content was or is planned to be in active use.)
     */
    public Period getEffectivePeriod() { 
      if (this.effectivePeriod == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Questionnaire.effectivePeriod");
        else if (Configuration.doAutoCreate())
          this.effectivePeriod = new Period(); // cc
      return this.effectivePeriod;
    }

    public boolean hasEffectivePeriod() { 
      return this.effectivePeriod != null && !this.effectivePeriod.isEmpty();
    }

    /**
     * @param value {@link #effectivePeriod} (The period during which the questionnaire content was or is planned to be in active use.)
     */
    public Questionnaire setEffectivePeriod(Period value) { 
      this.effectivePeriod = value;
      return this;
    }

    /**
     * @return {@link #code} (An identifier for this question or group of questions in a particular terminology such as LOINC.)
     */
    public List<Coding> getCode() { 
      if (this.code == null)
        this.code = new ArrayList<Coding>();
      return this.code;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Questionnaire setCode(List<Coding> theCode) { 
      this.code = theCode;
      return this;
    }

    public boolean hasCode() { 
      if (this.code == null)
        return false;
      for (Coding item : this.code)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Coding addCode() { //3
      Coding t = new Coding();
      if (this.code == null)
        this.code = new ArrayList<Coding>();
      this.code.add(t);
      return t;
    }

    public Questionnaire addCode(Coding t) { //3
      if (t == null)
        return this;
      if (this.code == null)
        this.code = new ArrayList<Coding>();
      this.code.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #code}, creating it if it does not already exist
     */
    public Coding getCodeFirstRep() { 
      if (getCode().isEmpty()) {
        addCode();
      }
      return getCode().get(0);
    }

    /**
     * @return {@link #item} (A particular question, question grouping or display text that is part of the questionnaire.)
     */
    public List<QuestionnaireItemComponent> getItem() { 
      if (this.item == null)
        this.item = new ArrayList<QuestionnaireItemComponent>();
      return this.item;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Questionnaire setItem(List<QuestionnaireItemComponent> theItem) { 
      this.item = theItem;
      return this;
    }

    public boolean hasItem() { 
      if (this.item == null)
        return false;
      for (QuestionnaireItemComponent item : this.item)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public QuestionnaireItemComponent addItem() { //3
      QuestionnaireItemComponent t = new QuestionnaireItemComponent();
      if (this.item == null)
        this.item = new ArrayList<QuestionnaireItemComponent>();
      this.item.add(t);
      return t;
    }

    public Questionnaire addItem(QuestionnaireItemComponent t) { //3
      if (t == null)
        return this;
      if (this.item == null)
        this.item = new ArrayList<QuestionnaireItemComponent>();
      this.item.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #item}, creating it if it does not already exist
     */
    public QuestionnaireItemComponent getItemFirstRep() { 
      if (getItem().isEmpty()) {
        addItem();
      }
      return getItem().get(0);
    }

      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("url", "uri", "An absolute URI that is used to identify this questionnaire when it is referenced in a specification, model, design or an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal address at which at which an authoritative instance of this questionnaire is (or will be) published. This URL can be the target of a canonical reference. It SHALL remain the same when the questionnaire is stored on different servers.", 0, 1, url));
        children.add(new Property("identifier", "Identifier", "A formal identifier that is used to identify this questionnaire when it is represented in other formats, or referenced in a specification, model, design or an instance.", 0, java.lang.Integer.MAX_VALUE, identifier));
        children.add(new Property("version", "string", "The identifier that is used to identify this version of the questionnaire when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the questionnaire author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence.", 0, 1, version));
        children.add(new Property("name", "string", "A natural language name identifying the questionnaire. This name should be usable as an identifier for the module by machine processing applications such as code generation.", 0, 1, name));
        children.add(new Property("title", "string", "A short, descriptive, user-friendly title for the questionnaire.", 0, 1, title));
        children.add(new Property("derivedFrom", "canonical(Questionnaire)", "The URL of a Questionnaire that this Questionnaire is based on.", 0, java.lang.Integer.MAX_VALUE, derivedFrom));
        children.add(new Property("status", "code", "The status of this questionnaire. Enables tracking the life-cycle of the content.", 0, 1, status));
        children.add(new Property("experimental", "boolean", "A Boolean value to indicate that this questionnaire is authored for testing purposes (or education/evaluation/marketing) and is not intended to be used for genuine usage.", 0, 1, experimental));
        children.add(new Property("subjectType", "code", "The types of subjects that can be the subject of responses created for the questionnaire.", 0, java.lang.Integer.MAX_VALUE, subjectType));
        children.add(new Property("date", "dateTime", "The date  (and optionally time) when the questionnaire was published. The date must change when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the questionnaire changes.", 0, 1, date));
        children.add(new Property("publisher", "string", "The name of the organization or individual that published the questionnaire.", 0, 1, publisher));
        children.add(new Property("contact", "ContactDetail", "Contact details to assist a user in finding and communicating with the publisher.", 0, java.lang.Integer.MAX_VALUE, contact));
        children.add(new Property("description", "markdown", "A free text natural language description of the questionnaire from a consumer's perspective.", 0, 1, description));
        children.add(new Property("useContext", "UsageContext", "The content was developed with a focus and intent of supporting the contexts that are listed. These contexts may be general categories (gender, age, ...) or may be references to specific programs (insurance plans, studies, ...) and may be used to assist with indexing and searching for appropriate questionnaire instances.", 0, java.lang.Integer.MAX_VALUE, useContext));
        children.add(new Property("jurisdiction", "CodeableConcept", "A legal or geographic region in which the questionnaire is intended to be used.", 0, java.lang.Integer.MAX_VALUE, jurisdiction));
        children.add(new Property("purpose", "markdown", "Explanation of why this questionnaire is needed and why it has been designed as it has.", 0, 1, purpose));
        children.add(new Property("copyright", "markdown", "A copyright statement relating to the questionnaire and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the questionnaire.", 0, 1, copyright));
        children.add(new Property("approvalDate", "date", "The date on which the resource content was approved by the publisher. Approval happens once when the content is officially approved for usage.", 0, 1, approvalDate));
        children.add(new Property("lastReviewDate", "date", "The date on which the resource content was last reviewed. Review happens periodically after approval but does not change the original approval date.", 0, 1, lastReviewDate));
        children.add(new Property("effectivePeriod", "Period", "The period during which the questionnaire content was or is planned to be in active use.", 0, 1, effectivePeriod));
        children.add(new Property("code", "Coding", "An identifier for this question or group of questions in a particular terminology such as LOINC.", 0, java.lang.Integer.MAX_VALUE, code));
        children.add(new Property("item", "", "A particular question, question grouping or display text that is part of the questionnaire.", 0, java.lang.Integer.MAX_VALUE, item));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case 116079: /*url*/  return new Property("url", "uri", "An absolute URI that is used to identify this questionnaire when it is referenced in a specification, model, design or an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal address at which at which an authoritative instance of this questionnaire is (or will be) published. This URL can be the target of a canonical reference. It SHALL remain the same when the questionnaire is stored on different servers.", 0, 1, url);
        case -1618432855: /*identifier*/  return new Property("identifier", "Identifier", "A formal identifier that is used to identify this questionnaire when it is represented in other formats, or referenced in a specification, model, design or an instance.", 0, java.lang.Integer.MAX_VALUE, identifier);
        case 351608024: /*version*/  return new Property("version", "string", "The identifier that is used to identify this version of the questionnaire when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the questionnaire author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence.", 0, 1, version);
        case 3373707: /*name*/  return new Property("name", "string", "A natural language name identifying the questionnaire. This name should be usable as an identifier for the module by machine processing applications such as code generation.", 0, 1, name);
        case 110371416: /*title*/  return new Property("title", "string", "A short, descriptive, user-friendly title for the questionnaire.", 0, 1, title);
        case 1077922663: /*derivedFrom*/  return new Property("derivedFrom", "canonical(Questionnaire)", "The URL of a Questionnaire that this Questionnaire is based on.", 0, java.lang.Integer.MAX_VALUE, derivedFrom);
        case -892481550: /*status*/  return new Property("status", "code", "The status of this questionnaire. Enables tracking the life-cycle of the content.", 0, 1, status);
        case -404562712: /*experimental*/  return new Property("experimental", "boolean", "A Boolean value to indicate that this questionnaire is authored for testing purposes (or education/evaluation/marketing) and is not intended to be used for genuine usage.", 0, 1, experimental);
        case -603200890: /*subjectType*/  return new Property("subjectType", "code", "The types of subjects that can be the subject of responses created for the questionnaire.", 0, java.lang.Integer.MAX_VALUE, subjectType);
        case 3076014: /*date*/  return new Property("date", "dateTime", "The date  (and optionally time) when the questionnaire was published. The date must change when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the questionnaire changes.", 0, 1, date);
        case 1447404028: /*publisher*/  return new Property("publisher", "string", "The name of the organization or individual that published the questionnaire.", 0, 1, publisher);
        case 951526432: /*contact*/  return new Property("contact", "ContactDetail", "Contact details to assist a user in finding and communicating with the publisher.", 0, java.lang.Integer.MAX_VALUE, contact);
        case -1724546052: /*description*/  return new Property("description", "markdown", "A free text natural language description of the questionnaire from a consumer's perspective.", 0, 1, description);
        case -669707736: /*useContext*/  return new Property("useContext", "UsageContext", "The content was developed with a focus and intent of supporting the contexts that are listed. These contexts may be general categories (gender, age, ...) or may be references to specific programs (insurance plans, studies, ...) and may be used to assist with indexing and searching for appropriate questionnaire instances.", 0, java.lang.Integer.MAX_VALUE, useContext);
        case -507075711: /*jurisdiction*/  return new Property("jurisdiction", "CodeableConcept", "A legal or geographic region in which the questionnaire is intended to be used.", 0, java.lang.Integer.MAX_VALUE, jurisdiction);
        case -220463842: /*purpose*/  return new Property("purpose", "markdown", "Explanation of why this questionnaire is needed and why it has been designed as it has.", 0, 1, purpose);
        case 1522889671: /*copyright*/  return new Property("copyright", "markdown", "A copyright statement relating to the questionnaire and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the questionnaire.", 0, 1, copyright);
        case 223539345: /*approvalDate*/  return new Property("approvalDate", "date", "The date on which the resource content was approved by the publisher. Approval happens once when the content is officially approved for usage.", 0, 1, approvalDate);
        case -1687512484: /*lastReviewDate*/  return new Property("lastReviewDate", "date", "The date on which the resource content was last reviewed. Review happens periodically after approval but does not change the original approval date.", 0, 1, lastReviewDate);
        case -403934648: /*effectivePeriod*/  return new Property("effectivePeriod", "Period", "The period during which the questionnaire content was or is planned to be in active use.", 0, 1, effectivePeriod);
        case 3059181: /*code*/  return new Property("code", "Coding", "An identifier for this question or group of questions in a particular terminology such as LOINC.", 0, java.lang.Integer.MAX_VALUE, code);
        case 3242771: /*item*/  return new Property("item", "", "A particular question, question grouping or display text that is part of the questionnaire.", 0, java.lang.Integer.MAX_VALUE, item);
        default: return super.getNamedProperty(_hash, _name, _checkValid);
        }

      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 116079: /*url*/ return this.url == null ? new Base[0] : new Base[] {this.url}; // UriType
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case 351608024: /*version*/ return this.version == null ? new Base[0] : new Base[] {this.version}; // StringType
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case 110371416: /*title*/ return this.title == null ? new Base[0] : new Base[] {this.title}; // StringType
        case 1077922663: /*derivedFrom*/ return this.derivedFrom == null ? new Base[0] : this.derivedFrom.toArray(new Base[this.derivedFrom.size()]); // CanonicalType
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<PublicationStatus>
        case -404562712: /*experimental*/ return this.experimental == null ? new Base[0] : new Base[] {this.experimental}; // BooleanType
        case -603200890: /*subjectType*/ return this.subjectType == null ? new Base[0] : this.subjectType.toArray(new Base[this.subjectType.size()]); // CodeType
        case 3076014: /*date*/ return this.date == null ? new Base[0] : new Base[] {this.date}; // DateTimeType
        case 1447404028: /*publisher*/ return this.publisher == null ? new Base[0] : new Base[] {this.publisher}; // StringType
        case 951526432: /*contact*/ return this.contact == null ? new Base[0] : this.contact.toArray(new Base[this.contact.size()]); // ContactDetail
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // MarkdownType
        case -669707736: /*useContext*/ return this.useContext == null ? new Base[0] : this.useContext.toArray(new Base[this.useContext.size()]); // UsageContext
        case -507075711: /*jurisdiction*/ return this.jurisdiction == null ? new Base[0] : this.jurisdiction.toArray(new Base[this.jurisdiction.size()]); // CodeableConcept
        case -220463842: /*purpose*/ return this.purpose == null ? new Base[0] : new Base[] {this.purpose}; // MarkdownType
        case 1522889671: /*copyright*/ return this.copyright == null ? new Base[0] : new Base[] {this.copyright}; // MarkdownType
        case 223539345: /*approvalDate*/ return this.approvalDate == null ? new Base[0] : new Base[] {this.approvalDate}; // DateType
        case -1687512484: /*lastReviewDate*/ return this.lastReviewDate == null ? new Base[0] : new Base[] {this.lastReviewDate}; // DateType
        case -403934648: /*effectivePeriod*/ return this.effectivePeriod == null ? new Base[0] : new Base[] {this.effectivePeriod}; // Period
        case 3059181: /*code*/ return this.code == null ? new Base[0] : this.code.toArray(new Base[this.code.size()]); // Coding
        case 3242771: /*item*/ return this.item == null ? new Base[0] : this.item.toArray(new Base[this.item.size()]); // QuestionnaireItemComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 116079: // url
          this.url = castToUri(value); // UriType
          return value;
        case -1618432855: // identifier
          this.getIdentifier().add(castToIdentifier(value)); // Identifier
          return value;
        case 351608024: // version
          this.version = castToString(value); // StringType
          return value;
        case 3373707: // name
          this.name = castToString(value); // StringType
          return value;
        case 110371416: // title
          this.title = castToString(value); // StringType
          return value;
        case 1077922663: // derivedFrom
          this.getDerivedFrom().add(castToCanonical(value)); // CanonicalType
          return value;
        case -892481550: // status
          value = new PublicationStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<PublicationStatus>
          return value;
        case -404562712: // experimental
          this.experimental = castToBoolean(value); // BooleanType
          return value;
        case -603200890: // subjectType
          this.getSubjectType().add(castToCode(value)); // CodeType
          return value;
        case 3076014: // date
          this.date = castToDateTime(value); // DateTimeType
          return value;
        case 1447404028: // publisher
          this.publisher = castToString(value); // StringType
          return value;
        case 951526432: // contact
          this.getContact().add(castToContactDetail(value)); // ContactDetail
          return value;
        case -1724546052: // description
          this.description = castToMarkdown(value); // MarkdownType
          return value;
        case -669707736: // useContext
          this.getUseContext().add(castToUsageContext(value)); // UsageContext
          return value;
        case -507075711: // jurisdiction
          this.getJurisdiction().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -220463842: // purpose
          this.purpose = castToMarkdown(value); // MarkdownType
          return value;
        case 1522889671: // copyright
          this.copyright = castToMarkdown(value); // MarkdownType
          return value;
        case 223539345: // approvalDate
          this.approvalDate = castToDate(value); // DateType
          return value;
        case -1687512484: // lastReviewDate
          this.lastReviewDate = castToDate(value); // DateType
          return value;
        case -403934648: // effectivePeriod
          this.effectivePeriod = castToPeriod(value); // Period
          return value;
        case 3059181: // code
          this.getCode().add(castToCoding(value)); // Coding
          return value;
        case 3242771: // item
          this.getItem().add((QuestionnaireItemComponent) value); // QuestionnaireItemComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("url")) {
          this.url = castToUri(value); // UriType
        } else if (name.equals("identifier")) {
          this.getIdentifier().add(castToIdentifier(value));
        } else if (name.equals("version")) {
          this.version = castToString(value); // StringType
        } else if (name.equals("name")) {
          this.name = castToString(value); // StringType
        } else if (name.equals("title")) {
          this.title = castToString(value); // StringType
        } else if (name.equals("derivedFrom")) {
          this.getDerivedFrom().add(castToCanonical(value));
        } else if (name.equals("status")) {
          value = new PublicationStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<PublicationStatus>
        } else if (name.equals("experimental")) {
          this.experimental = castToBoolean(value); // BooleanType
        } else if (name.equals("subjectType")) {
          this.getSubjectType().add(castToCode(value));
        } else if (name.equals("date")) {
          this.date = castToDateTime(value); // DateTimeType
        } else if (name.equals("publisher")) {
          this.publisher = castToString(value); // StringType
        } else if (name.equals("contact")) {
          this.getContact().add(castToContactDetail(value));
        } else if (name.equals("description")) {
          this.description = castToMarkdown(value); // MarkdownType
        } else if (name.equals("useContext")) {
          this.getUseContext().add(castToUsageContext(value));
        } else if (name.equals("jurisdiction")) {
          this.getJurisdiction().add(castToCodeableConcept(value));
        } else if (name.equals("purpose")) {
          this.purpose = castToMarkdown(value); // MarkdownType
        } else if (name.equals("copyright")) {
          this.copyright = castToMarkdown(value); // MarkdownType
        } else if (name.equals("approvalDate")) {
          this.approvalDate = castToDate(value); // DateType
        } else if (name.equals("lastReviewDate")) {
          this.lastReviewDate = castToDate(value); // DateType
        } else if (name.equals("effectivePeriod")) {
          this.effectivePeriod = castToPeriod(value); // Period
        } else if (name.equals("code")) {
          this.getCode().add(castToCoding(value));
        } else if (name.equals("item")) {
          this.getItem().add((QuestionnaireItemComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 116079:  return getUrlElement();
        case -1618432855:  return addIdentifier(); 
        case 351608024:  return getVersionElement();
        case 3373707:  return getNameElement();
        case 110371416:  return getTitleElement();
        case 1077922663:  return addDerivedFromElement();
        case -892481550:  return getStatusElement();
        case -404562712:  return getExperimentalElement();
        case -603200890:  return addSubjectTypeElement();
        case 3076014:  return getDateElement();
        case 1447404028:  return getPublisherElement();
        case 951526432:  return addContact(); 
        case -1724546052:  return getDescriptionElement();
        case -669707736:  return addUseContext(); 
        case -507075711:  return addJurisdiction(); 
        case -220463842:  return getPurposeElement();
        case 1522889671:  return getCopyrightElement();
        case 223539345:  return getApprovalDateElement();
        case -1687512484:  return getLastReviewDateElement();
        case -403934648:  return getEffectivePeriod(); 
        case 3059181:  return addCode(); 
        case 3242771:  return addItem(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 116079: /*url*/ return new String[] {"uri"};
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case 351608024: /*version*/ return new String[] {"string"};
        case 3373707: /*name*/ return new String[] {"string"};
        case 110371416: /*title*/ return new String[] {"string"};
        case 1077922663: /*derivedFrom*/ return new String[] {"canonical"};
        case -892481550: /*status*/ return new String[] {"code"};
        case -404562712: /*experimental*/ return new String[] {"boolean"};
        case -603200890: /*subjectType*/ return new String[] {"code"};
        case 3076014: /*date*/ return new String[] {"dateTime"};
        case 1447404028: /*publisher*/ return new String[] {"string"};
        case 951526432: /*contact*/ return new String[] {"ContactDetail"};
        case -1724546052: /*description*/ return new String[] {"markdown"};
        case -669707736: /*useContext*/ return new String[] {"UsageContext"};
        case -507075711: /*jurisdiction*/ return new String[] {"CodeableConcept"};
        case -220463842: /*purpose*/ return new String[] {"markdown"};
        case 1522889671: /*copyright*/ return new String[] {"markdown"};
        case 223539345: /*approvalDate*/ return new String[] {"date"};
        case -1687512484: /*lastReviewDate*/ return new String[] {"date"};
        case -403934648: /*effectivePeriod*/ return new String[] {"Period"};
        case 3059181: /*code*/ return new String[] {"Coding"};
        case 3242771: /*item*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("url")) {
          throw new FHIRException("Cannot call addChild on a primitive type Questionnaire.url");
        }
        else if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("version")) {
          throw new FHIRException("Cannot call addChild on a primitive type Questionnaire.version");
        }
        else if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type Questionnaire.name");
        }
        else if (name.equals("title")) {
          throw new FHIRException("Cannot call addChild on a primitive type Questionnaire.title");
        }
        else if (name.equals("derivedFrom")) {
          throw new FHIRException("Cannot call addChild on a primitive type Questionnaire.derivedFrom");
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type Questionnaire.status");
        }
        else if (name.equals("experimental")) {
          throw new FHIRException("Cannot call addChild on a primitive type Questionnaire.experimental");
        }
        else if (name.equals("subjectType")) {
          throw new FHIRException("Cannot call addChild on a primitive type Questionnaire.subjectType");
        }
        else if (name.equals("date")) {
          throw new FHIRException("Cannot call addChild on a primitive type Questionnaire.date");
        }
        else if (name.equals("publisher")) {
          throw new FHIRException("Cannot call addChild on a primitive type Questionnaire.publisher");
        }
        else if (name.equals("contact")) {
          return addContact();
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type Questionnaire.description");
        }
        else if (name.equals("useContext")) {
          return addUseContext();
        }
        else if (name.equals("jurisdiction")) {
          return addJurisdiction();
        }
        else if (name.equals("purpose")) {
          throw new FHIRException("Cannot call addChild on a primitive type Questionnaire.purpose");
        }
        else if (name.equals("copyright")) {
          throw new FHIRException("Cannot call addChild on a primitive type Questionnaire.copyright");
        }
        else if (name.equals("approvalDate")) {
          throw new FHIRException("Cannot call addChild on a primitive type Questionnaire.approvalDate");
        }
        else if (name.equals("lastReviewDate")) {
          throw new FHIRException("Cannot call addChild on a primitive type Questionnaire.lastReviewDate");
        }
        else if (name.equals("effectivePeriod")) {
          this.effectivePeriod = new Period();
          return this.effectivePeriod;
        }
        else if (name.equals("code")) {
          return addCode();
        }
        else if (name.equals("item")) {
          return addItem();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "Questionnaire";

  }

      public Questionnaire copy() {
        Questionnaire dst = new Questionnaire();
        copyValues(dst);
        dst.url = url == null ? null : url.copy();
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.version = version == null ? null : version.copy();
        dst.name = name == null ? null : name.copy();
        dst.title = title == null ? null : title.copy();
        if (derivedFrom != null) {
          dst.derivedFrom = new ArrayList<CanonicalType>();
          for (CanonicalType i : derivedFrom)
            dst.derivedFrom.add(i.copy());
        };
        dst.status = status == null ? null : status.copy();
        dst.experimental = experimental == null ? null : experimental.copy();
        if (subjectType != null) {
          dst.subjectType = new ArrayList<CodeType>();
          for (CodeType i : subjectType)
            dst.subjectType.add(i.copy());
        };
        dst.date = date == null ? null : date.copy();
        dst.publisher = publisher == null ? null : publisher.copy();
        if (contact != null) {
          dst.contact = new ArrayList<ContactDetail>();
          for (ContactDetail i : contact)
            dst.contact.add(i.copy());
        };
        dst.description = description == null ? null : description.copy();
        if (useContext != null) {
          dst.useContext = new ArrayList<UsageContext>();
          for (UsageContext i : useContext)
            dst.useContext.add(i.copy());
        };
        if (jurisdiction != null) {
          dst.jurisdiction = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : jurisdiction)
            dst.jurisdiction.add(i.copy());
        };
        dst.purpose = purpose == null ? null : purpose.copy();
        dst.copyright = copyright == null ? null : copyright.copy();
        dst.approvalDate = approvalDate == null ? null : approvalDate.copy();
        dst.lastReviewDate = lastReviewDate == null ? null : lastReviewDate.copy();
        dst.effectivePeriod = effectivePeriod == null ? null : effectivePeriod.copy();
        if (code != null) {
          dst.code = new ArrayList<Coding>();
          for (Coding i : code)
            dst.code.add(i.copy());
        };
        if (item != null) {
          dst.item = new ArrayList<QuestionnaireItemComponent>();
          for (QuestionnaireItemComponent i : item)
            dst.item.add(i.copy());
        };
        return dst;
      }

      protected Questionnaire typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof Questionnaire))
          return false;
        Questionnaire o = (Questionnaire) other_;
        return compareDeep(identifier, o.identifier, true) && compareDeep(derivedFrom, o.derivedFrom, true)
           && compareDeep(subjectType, o.subjectType, true) && compareDeep(purpose, o.purpose, true) && compareDeep(copyright, o.copyright, true)
           && compareDeep(approvalDate, o.approvalDate, true) && compareDeep(lastReviewDate, o.lastReviewDate, true)
           && compareDeep(effectivePeriod, o.effectivePeriod, true) && compareDeep(code, o.code, true) && compareDeep(item, o.item, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof Questionnaire))
          return false;
        Questionnaire o = (Questionnaire) other_;
        return compareValues(subjectType, o.subjectType, true) && compareValues(purpose, o.purpose, true) && compareValues(copyright, o.copyright, true)
           && compareValues(approvalDate, o.approvalDate, true) && compareValues(lastReviewDate, o.lastReviewDate, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, derivedFrom, subjectType
          , purpose, copyright, approvalDate, lastReviewDate, effectivePeriod, code, item
          );
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Questionnaire;
   }

 /**
   * Search parameter: <b>date</b>
   * <p>
   * Description: <b>The questionnaire publication date</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Questionnaire.date</b><br>
   * </p>
   */
  @SearchParamDefinition(name="date", path="Questionnaire.date", description="The questionnaire publication date", type="date" )
  public static final String SP_DATE = "date";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>date</b>
   * <p>
   * Description: <b>The questionnaire publication date</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Questionnaire.date</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam DATE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_DATE);

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>External identifier for the questionnaire</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Questionnaire.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="Questionnaire.identifier", description="External identifier for the questionnaire", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>External identifier for the questionnaire</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Questionnaire.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>code</b>
   * <p>
   * Description: <b>A code that corresponds to one of its items in the questionnaire</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Questionnaire.item.code</b><br>
   * </p>
   */
  @SearchParamDefinition(name="code", path="Questionnaire.item.code", description="A code that corresponds to one of its items in the questionnaire", type="token" )
  public static final String SP_CODE = "code";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>code</b>
   * <p>
   * Description: <b>A code that corresponds to one of its items in the questionnaire</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Questionnaire.item.code</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CODE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CODE);

 /**
   * Search parameter: <b>context-type-value</b>
   * <p>
   * Description: <b>A use context type and value assigned to the questionnaire</b><br>
   * Type: <b>composite</b><br>
   * Path: <b></b><br>
   * </p>
   */
  @SearchParamDefinition(name="context-type-value", path="Questionnaire.useContext", description="A use context type and value assigned to the questionnaire", type="composite", compositeOf={"context-type", "context"} )
  public static final String SP_CONTEXT_TYPE_VALUE = "context-type-value";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>context-type-value</b>
   * <p>
   * Description: <b>A use context type and value assigned to the questionnaire</b><br>
   * Type: <b>composite</b><br>
   * Path: <b></b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.CompositeClientParam<ca.uhn.fhir.rest.gclient.TokenClientParam, ca.uhn.fhir.rest.gclient.TokenClientParam> CONTEXT_TYPE_VALUE = new ca.uhn.fhir.rest.gclient.CompositeClientParam<ca.uhn.fhir.rest.gclient.TokenClientParam, ca.uhn.fhir.rest.gclient.TokenClientParam>(SP_CONTEXT_TYPE_VALUE);

 /**
   * Search parameter: <b>jurisdiction</b>
   * <p>
   * Description: <b>Intended jurisdiction for the questionnaire</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Questionnaire.jurisdiction</b><br>
   * </p>
   */
  @SearchParamDefinition(name="jurisdiction", path="Questionnaire.jurisdiction", description="Intended jurisdiction for the questionnaire", type="token" )
  public static final String SP_JURISDICTION = "jurisdiction";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>jurisdiction</b>
   * <p>
   * Description: <b>Intended jurisdiction for the questionnaire</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Questionnaire.jurisdiction</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam JURISDICTION = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_JURISDICTION);

 /**
   * Search parameter: <b>description</b>
   * <p>
   * Description: <b>The description of the questionnaire</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Questionnaire.description</b><br>
   * </p>
   */
  @SearchParamDefinition(name="description", path="Questionnaire.description", description="The description of the questionnaire", type="string" )
  public static final String SP_DESCRIPTION = "description";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>description</b>
   * <p>
   * Description: <b>The description of the questionnaire</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Questionnaire.description</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam DESCRIPTION = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_DESCRIPTION);

 /**
   * Search parameter: <b>context-type</b>
   * <p>
   * Description: <b>A type of use context assigned to the questionnaire</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Questionnaire.useContext.code</b><br>
   * </p>
   */
  @SearchParamDefinition(name="context-type", path="Questionnaire.useContext.code", description="A type of use context assigned to the questionnaire", type="token" )
  public static final String SP_CONTEXT_TYPE = "context-type";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>context-type</b>
   * <p>
   * Description: <b>A type of use context assigned to the questionnaire</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Questionnaire.useContext.code</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CONTEXT_TYPE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CONTEXT_TYPE);

 /**
   * Search parameter: <b>title</b>
   * <p>
   * Description: <b>The human-friendly name of the questionnaire</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Questionnaire.title</b><br>
   * </p>
   */
  @SearchParamDefinition(name="title", path="Questionnaire.title", description="The human-friendly name of the questionnaire", type="string" )
  public static final String SP_TITLE = "title";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>title</b>
   * <p>
   * Description: <b>The human-friendly name of the questionnaire</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Questionnaire.title</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam TITLE = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_TITLE);

 /**
   * Search parameter: <b>version</b>
   * <p>
   * Description: <b>The business version of the questionnaire</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Questionnaire.version</b><br>
   * </p>
   */
  @SearchParamDefinition(name="version", path="Questionnaire.version", description="The business version of the questionnaire", type="token" )
  public static final String SP_VERSION = "version";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>version</b>
   * <p>
   * Description: <b>The business version of the questionnaire</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Questionnaire.version</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam VERSION = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_VERSION);

 /**
   * Search parameter: <b>url</b>
   * <p>
   * Description: <b>The uri that identifies the questionnaire</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>Questionnaire.url</b><br>
   * </p>
   */
  @SearchParamDefinition(name="url", path="Questionnaire.url", description="The uri that identifies the questionnaire", type="uri" )
  public static final String SP_URL = "url";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>url</b>
   * <p>
   * Description: <b>The uri that identifies the questionnaire</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>Questionnaire.url</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.UriClientParam URL = new ca.uhn.fhir.rest.gclient.UriClientParam(SP_URL);

 /**
   * Search parameter: <b>context-quantity</b>
   * <p>
   * Description: <b>A quantity- or range-valued use context assigned to the questionnaire</b><br>
   * Type: <b>quantity</b><br>
   * Path: <b>Questionnaire.useContext.valueQuantity, Questionnaire.useContext.valueRange</b><br>
   * </p>
   */
  @SearchParamDefinition(name="context-quantity", path="(Questionnaire.useContext.value as Quantity) | (Questionnaire.useContext.value as Range)", description="A quantity- or range-valued use context assigned to the questionnaire", type="quantity" )
  public static final String SP_CONTEXT_QUANTITY = "context-quantity";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>context-quantity</b>
   * <p>
   * Description: <b>A quantity- or range-valued use context assigned to the questionnaire</b><br>
   * Type: <b>quantity</b><br>
   * Path: <b>Questionnaire.useContext.valueQuantity, Questionnaire.useContext.valueRange</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.QuantityClientParam CONTEXT_QUANTITY = new ca.uhn.fhir.rest.gclient.QuantityClientParam(SP_CONTEXT_QUANTITY);

 /**
   * Search parameter: <b>effective</b>
   * <p>
   * Description: <b>The time during which the questionnaire is intended to be in use</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Questionnaire.effectivePeriod</b><br>
   * </p>
   */
  @SearchParamDefinition(name="effective", path="Questionnaire.effectivePeriod", description="The time during which the questionnaire is intended to be in use", type="date" )
  public static final String SP_EFFECTIVE = "effective";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>effective</b>
   * <p>
   * Description: <b>The time during which the questionnaire is intended to be in use</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Questionnaire.effectivePeriod</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam EFFECTIVE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_EFFECTIVE);

 /**
   * Search parameter: <b>subject-type</b>
   * <p>
   * Description: <b>Resource that can be subject of QuestionnaireResponse</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Questionnaire.subjectType</b><br>
   * </p>
   */
  @SearchParamDefinition(name="subject-type", path="Questionnaire.subjectType", description="Resource that can be subject of QuestionnaireResponse", type="token" )
  public static final String SP_SUBJECT_TYPE = "subject-type";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>subject-type</b>
   * <p>
   * Description: <b>Resource that can be subject of QuestionnaireResponse</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Questionnaire.subjectType</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam SUBJECT_TYPE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_SUBJECT_TYPE);

 /**
   * Search parameter: <b>name</b>
   * <p>
   * Description: <b>Computationally friendly name of the questionnaire</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Questionnaire.name</b><br>
   * </p>
   */
  @SearchParamDefinition(name="name", path="Questionnaire.name", description="Computationally friendly name of the questionnaire", type="string" )
  public static final String SP_NAME = "name";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>name</b>
   * <p>
   * Description: <b>Computationally friendly name of the questionnaire</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Questionnaire.name</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam NAME = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_NAME);

 /**
   * Search parameter: <b>context</b>
   * <p>
   * Description: <b>A use context assigned to the questionnaire</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Questionnaire.useContext.valueCodeableConcept</b><br>
   * </p>
   */
  @SearchParamDefinition(name="context", path="(Questionnaire.useContext.value as CodeableConcept)", description="A use context assigned to the questionnaire", type="token" )
  public static final String SP_CONTEXT = "context";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>context</b>
   * <p>
   * Description: <b>A use context assigned to the questionnaire</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Questionnaire.useContext.valueCodeableConcept</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CONTEXT = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CONTEXT);

 /**
   * Search parameter: <b>publisher</b>
   * <p>
   * Description: <b>Name of the publisher of the questionnaire</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Questionnaire.publisher</b><br>
   * </p>
   */
  @SearchParamDefinition(name="publisher", path="Questionnaire.publisher", description="Name of the publisher of the questionnaire", type="string" )
  public static final String SP_PUBLISHER = "publisher";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>publisher</b>
   * <p>
   * Description: <b>Name of the publisher of the questionnaire</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Questionnaire.publisher</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam PUBLISHER = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_PUBLISHER);

 /**
   * Search parameter: <b>definition</b>
   * <p>
   * Description: <b>ElementDefinition - details for the item</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>Questionnaire.item.definition</b><br>
   * </p>
   */
  @SearchParamDefinition(name="definition", path="Questionnaire.item.definition", description="ElementDefinition - details for the item", type="uri" )
  public static final String SP_DEFINITION = "definition";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>definition</b>
   * <p>
   * Description: <b>ElementDefinition - details for the item</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>Questionnaire.item.definition</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.UriClientParam DEFINITION = new ca.uhn.fhir.rest.gclient.UriClientParam(SP_DEFINITION);

 /**
   * Search parameter: <b>context-type-quantity</b>
   * <p>
   * Description: <b>A use context type and quantity- or range-based value assigned to the questionnaire</b><br>
   * Type: <b>composite</b><br>
   * Path: <b></b><br>
   * </p>
   */
  @SearchParamDefinition(name="context-type-quantity", path="Questionnaire.useContext", description="A use context type and quantity- or range-based value assigned to the questionnaire", type="composite", compositeOf={"context-type", "context-quantity"} )
  public static final String SP_CONTEXT_TYPE_QUANTITY = "context-type-quantity";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>context-type-quantity</b>
   * <p>
   * Description: <b>A use context type and quantity- or range-based value assigned to the questionnaire</b><br>
   * Type: <b>composite</b><br>
   * Path: <b></b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.CompositeClientParam<ca.uhn.fhir.rest.gclient.TokenClientParam, ca.uhn.fhir.rest.gclient.QuantityClientParam> CONTEXT_TYPE_QUANTITY = new ca.uhn.fhir.rest.gclient.CompositeClientParam<ca.uhn.fhir.rest.gclient.TokenClientParam, ca.uhn.fhir.rest.gclient.QuantityClientParam>(SP_CONTEXT_TYPE_QUANTITY);

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>The current status of the questionnaire</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Questionnaire.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="Questionnaire.status", description="The current status of the questionnaire", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>The current status of the questionnaire</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Questionnaire.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);


}

