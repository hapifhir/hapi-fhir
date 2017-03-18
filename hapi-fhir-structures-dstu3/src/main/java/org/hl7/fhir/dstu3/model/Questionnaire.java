package org.hl7.fhir.dstu3.model;

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

// Generated on Sat, Mar 4, 2017 06:58-0500 for FHIR v1.9.0

import java.util.*;

import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.dstu3.model.Enumerations.*;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ChildOrder;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Block;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.exceptions.FHIRException;
/**
 * A structured set of questions intended to guide the collection of answers. The questions are ordered and grouped into coherent subsets, corresponding to the structure of the grouping of the underlying questions.
 */
@ResourceDef(name="Questionnaire", profile="http://hl7.org/fhir/Profile/Questionnaire")
public class Questionnaire extends DomainResource {

    public enum QuestionnaireStatus {
        /**
         * This Questionnaire is not ready for official use.
         */
        DRAFT, 
        /**
         * This Questionnaire is ready for use.
         */
        PUBLISHED, 
        /**
         * This Questionnaire should no longer be used to gather data.
         */
        RETIRED, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static QuestionnaireStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("draft".equals(codeString))
          return DRAFT;
        if ("published".equals(codeString))
          return PUBLISHED;
        if ("retired".equals(codeString))
          return RETIRED;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown QuestionnaireStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case DRAFT: return "draft";
            case PUBLISHED: return "published";
            case RETIRED: return "retired";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case DRAFT: return "http://hl7.org/fhir/questionnaire-status";
            case PUBLISHED: return "http://hl7.org/fhir/questionnaire-status";
            case RETIRED: return "http://hl7.org/fhir/questionnaire-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case DRAFT: return "This Questionnaire is not ready for official use.";
            case PUBLISHED: return "This Questionnaire is ready for use.";
            case RETIRED: return "This Questionnaire should no longer be used to gather data.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case DRAFT: return "Draft";
            case PUBLISHED: return "Published";
            case RETIRED: return "Retired";
            default: return "?";
          }
        }
    }

  public static class QuestionnaireStatusEnumFactory implements EnumFactory<QuestionnaireStatus> {
    public QuestionnaireStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("draft".equals(codeString))
          return QuestionnaireStatus.DRAFT;
        if ("published".equals(codeString))
          return QuestionnaireStatus.PUBLISHED;
        if ("retired".equals(codeString))
          return QuestionnaireStatus.RETIRED;
        throw new IllegalArgumentException("Unknown QuestionnaireStatus code '"+codeString+"'");
        }
        public Enumeration<QuestionnaireStatus> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<QuestionnaireStatus>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("draft".equals(codeString))
          return new Enumeration<QuestionnaireStatus>(this, QuestionnaireStatus.DRAFT);
        if ("published".equals(codeString))
          return new Enumeration<QuestionnaireStatus>(this, QuestionnaireStatus.PUBLISHED);
        if ("retired".equals(codeString))
          return new Enumeration<QuestionnaireStatus>(this, QuestionnaireStatus.RETIRED);
        throw new FHIRException("Unknown QuestionnaireStatus code '"+codeString+"'");
        }
    public String toCode(QuestionnaireStatus code) {
      if (code == QuestionnaireStatus.DRAFT)
        return "draft";
      if (code == QuestionnaireStatus.PUBLISHED)
        return "published";
      if (code == QuestionnaireStatus.RETIRED)
        return "retired";
      return "?";
      }
    public String toSystem(QuestionnaireStatus code) {
      return code.getSystem();
      }
    }

    public enum QuestionnaireItemType {
        /**
         * An item with no direct answer but which has descendant items that are questions
         */
        GROUP, 
        /**
         * Text for display that will not capture an answer or have descendants
         */
        DISPLAY, 
        /**
         * An item that defines a specific answer to be captured (and may have descendant items)
         */
        QUESTION, 
        /**
         * Question with a yes/no answer
         */
        BOOLEAN, 
        /**
         * Question with is a real number answer
         */
        DECIMAL, 
        /**
         * Question with an integer answer
         */
        INTEGER, 
        /**
         * Question with adate answer
         */
        DATE, 
        /**
         * Question with a date and time answer
         */
        DATETIME, 
        /**
         * Question with a time (hour/minute/second) answer independent of date.
         */
        TIME, 
        /**
         * Question with a short (few words to short sentence) free-text entry answer
         */
        STRING, 
        /**
         * Question with a long (potentially multi-paragraph) free-text entry (still captured as a string) answer
         */
        TEXT, 
        /**
         * Question with a url (website, FTP site, etc.) answer
         */
        URL, 
        /**
         * Question with a Coding drawn from a list of options as an answer
         */
        CHOICE, 
        /**
         * Answer is a Coding drawn from a list of options or a free-text entry captured as Coding.display
         */
        OPENCHOICE, 
        /**
         * Question with binary content such as a image, PDF, etc. as an answer
         */
        ATTACHMENT, 
        /**
         * Question with a reference to another resource (practitioner, organization, etc.) as an answer
         */
        REFERENCE, 
        /**
         * Question with a combination of a numeric value and unit, potentially with a comparator (<, >, etc.) as an answer.
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
            case GROUP: return "An item with no direct answer but which has descendant items that are questions";
            case DISPLAY: return "Text for display that will not capture an answer or have descendants";
            case QUESTION: return "An item that defines a specific answer to be captured (and may have descendant items)";
            case BOOLEAN: return "Question with a yes/no answer";
            case DECIMAL: return "Question with is a real number answer";
            case INTEGER: return "Question with an integer answer";
            case DATE: return "Question with adate answer";
            case DATETIME: return "Question with a date and time answer";
            case TIME: return "Question with a time (hour/minute/second) answer independent of date.";
            case STRING: return "Question with a short (few words to short sentence) free-text entry answer";
            case TEXT: return "Question with a long (potentially multi-paragraph) free-text entry (still captured as a string) answer";
            case URL: return "Question with a url (website, FTP site, etc.) answer";
            case CHOICE: return "Question with a Coding drawn from a list of options as an answer";
            case OPENCHOICE: return "Answer is a Coding drawn from a list of options or a free-text entry captured as Coding.display";
            case ATTACHMENT: return "Question with binary content such as a image, PDF, etc. as an answer";
            case REFERENCE: return "Question with a reference to another resource (practitioner, organization, etc.) as an answer";
            case QUANTITY: return "Question with a combination of a numeric value and unit, potentially with a comparator (<, >, etc.) as an answer.";
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

    @Block()
    public static class QuestionnaireItemComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * An identifier that is unique within the Questionnaire allowing linkage to the equivalent item in a QuestionnaireResponse resource.
         */
        @Child(name = "linkId", type = {StringType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Unique id for item in questionnaire", formalDefinition="An identifier that is unique within the Questionnaire allowing linkage to the equivalent item in a QuestionnaireResponse resource." )
        protected StringType linkId;

        /**
         * A reference to an [[[ElementDefinition]]] that provides the details for the item. If a definition is provided, then the following element values can be inferred from the definition: 

* code (ElementDefinition.code)
* type (ElementDefinition.type)
* required (ElementDefinition.min)
* repeats (ElementDefinition.max)
* maxLength (ElementDefinition.maxLength)
* options (ElementDefinition.binding)

Any information provided in these elements on a Questionnaire Item overrides the information from the definition.
         */
        @Child(name = "definition", type = {UriType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="ElementDefinition - details for the item", formalDefinition="A reference to an [[[ElementDefinition]]] that provides the details for the item. If a definition is provided, then the following element values can be inferred from the definition: \n\n* code (ElementDefinition.code)\n* type (ElementDefinition.type)\n* required (ElementDefinition.min)\n* repeats (ElementDefinition.max)\n* maxLength (ElementDefinition.maxLength)\n* options (ElementDefinition.binding)\n\nAny information provided in these elements on a Questionnaire Item overrides the information from the definition." )
        protected UriType definition;

        /**
         * A Terminology code that corresponds to this group or question (e.g. a code from LOINC, which defines many questions and answers).
         */
        @Child(name = "code", type = {Coding.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Corresponding Concept for this item in a terminology", formalDefinition="A Terminology code that corresponds to this group or question (e.g. a code from LOINC, which defines many questions and answers)." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/questionnaire-questions")
        protected List<Coding> code;

        /**
         * A short label for a particular group, question or set of display text within the questionnaire.
         */
        @Child(name = "prefix", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="E.g. \"1(a)\", \"2.5.3\"", formalDefinition="A short label for a particular group, question or set of display text within the questionnaire." )
        protected StringType prefix;

        /**
         * The name of a section, the text of a question or text content for a text item.
         */
        @Child(name = "text", type = {StringType.class}, order=5, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Primary text for the item", formalDefinition="The name of a section, the text of a question or text content for a text item." )
        protected StringType text;

        /**
         * Identifies the type of questionnaire item this is - whether text for display, a grouping of other items or a particular type of data to be captured (string, integer, coded choice, etc.).
         */
        @Child(name = "type", type = {CodeType.class}, order=6, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="group | display | boolean | decimal | integer | date | dateTime +", formalDefinition="Identifies the type of questionnaire item this is - whether text for display, a grouping of other items or a particular type of data to be captured (string, integer, coded choice, etc.)." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/item-type")
        protected Enumeration<QuestionnaireItemType> type;

        /**
         * If present, indicates that this item should only be enabled (displayed/allow answers to be captured) when the specified condition is true.
         */
        @Child(name = "enableWhen", type = {}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=true, summary=true)
        @Description(shortDefinition="Only allow data when:", formalDefinition="If present, indicates that this item should only be enabled (displayed/allow answers to be captured) when the specified condition is true." )
        protected List<QuestionnaireItemEnableWhenComponent> enableWhen;

        /**
         * If true, indicates that the item must be present in a "completed" QuestionnaireResponse.  If false, the item may be skipped when answering the questionnaire.
         */
        @Child(name = "required", type = {BooleanType.class}, order=8, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Whether the item must be included in data results", formalDefinition="If true, indicates that the item must be present in a \"completed\" QuestionnaireResponse.  If false, the item may be skipped when answering the questionnaire." )
        protected BooleanType required;

        /**
         * Whether the item may occur multiple times in the instance, containing multiple sets of answers.
         */
        @Child(name = "repeats", type = {BooleanType.class}, order=9, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Whether the item may repeat", formalDefinition="Whether the item may occur multiple times in the instance, containing multiple sets of answers." )
        protected BooleanType repeats;

        /**
         * If true, the value cannot be changed by a human respondent to the Questionnaire.
         */
        @Child(name = "readOnly", type = {BooleanType.class}, order=10, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Don't allow human editing", formalDefinition="If true, the value cannot be changed by a human respondent to the Questionnaire." )
        protected BooleanType readOnly;

        /**
         * The maximum number of characters that are permitted in the answer to be considered a "valid" QuestionnaireResponse.
         */
        @Child(name = "maxLength", type = {IntegerType.class}, order=11, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="No more than this many characters", formalDefinition="The maximum number of characters that are permitted in the answer to be considered a \"valid\" QuestionnaireResponse." )
        protected IntegerType maxLength;

        /**
         * Reference to a value set containing a list of codes representing permitted answers for the question.
         */
        @Child(name = "options", type = {ValueSet.class}, order=12, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Valueset containing permitted answers", formalDefinition="Reference to a value set containing a list of codes representing permitted answers for the question." )
        protected Reference options;

        /**
         * The actual object that is the target of the reference (Reference to a value set containing a list of codes representing permitted answers for the question.)
         */
        protected ValueSet optionsTarget;

        /**
         * For a "choice" question, identifies one of the permitted answers for the question.
         */
        @Child(name = "option", type = {}, order=13, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Permitted answer", formalDefinition="For a \"choice\" question, identifies one of the permitted answers for the question." )
        protected List<QuestionnaireItemOptionComponent> option;

        /**
         * The value that should be defaulted when rendering the questionnaire for user input.
         */
        @Child(name = "initial", type = {BooleanType.class, DecimalType.class, IntegerType.class, DateType.class, DateTimeType.class, TimeType.class, StringType.class, UriType.class, Attachment.class, Coding.class, Quantity.class, Reference.class}, order=14, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Default value when item is first rendered", formalDefinition="The value that should be defaulted when rendering the questionnaire for user input." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/questionnaire-answers")
        protected Type initial;

        /**
         * Allows text, questions and other groups to be nested beneath a question or group.
         */
        @Child(name = "item", type = {QuestionnaireItemComponent.class}, order=15, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Nested questionnaire items", formalDefinition="Allows text, questions and other groups to be nested beneath a question or group." )
        protected List<QuestionnaireItemComponent> item;

        private static final long serialVersionUID = -1997112302L;

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
         * @return {@link #definition} (A reference to an [[[ElementDefinition]]] that provides the details for the item. If a definition is provided, then the following element values can be inferred from the definition: 

* code (ElementDefinition.code)
* type (ElementDefinition.type)
* required (ElementDefinition.min)
* repeats (ElementDefinition.max)
* maxLength (ElementDefinition.maxLength)
* options (ElementDefinition.binding)

Any information provided in these elements on a Questionnaire Item overrides the information from the definition.). This is the underlying object with id, value and extensions. The accessor "getDefinition" gives direct access to the value
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
         * @param value {@link #definition} (A reference to an [[[ElementDefinition]]] that provides the details for the item. If a definition is provided, then the following element values can be inferred from the definition: 

* code (ElementDefinition.code)
* type (ElementDefinition.type)
* required (ElementDefinition.min)
* repeats (ElementDefinition.max)
* maxLength (ElementDefinition.maxLength)
* options (ElementDefinition.binding)

Any information provided in these elements on a Questionnaire Item overrides the information from the definition.). This is the underlying object with id, value and extensions. The accessor "getDefinition" gives direct access to the value
         */
        public QuestionnaireItemComponent setDefinitionElement(UriType value) { 
          this.definition = value;
          return this;
        }

        /**
         * @return A reference to an [[[ElementDefinition]]] that provides the details for the item. If a definition is provided, then the following element values can be inferred from the definition: 

* code (ElementDefinition.code)
* type (ElementDefinition.type)
* required (ElementDefinition.min)
* repeats (ElementDefinition.max)
* maxLength (ElementDefinition.maxLength)
* options (ElementDefinition.binding)

Any information provided in these elements on a Questionnaire Item overrides the information from the definition.
         */
        public String getDefinition() { 
          return this.definition == null ? null : this.definition.getValue();
        }

        /**
         * @param value A reference to an [[[ElementDefinition]]] that provides the details for the item. If a definition is provided, then the following element values can be inferred from the definition: 

* code (ElementDefinition.code)
* type (ElementDefinition.type)
* required (ElementDefinition.min)
* repeats (ElementDefinition.max)
* maxLength (ElementDefinition.maxLength)
* options (ElementDefinition.binding)

Any information provided in these elements on a Questionnaire Item overrides the information from the definition.
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
         * @return {@link #code} (A Terminology code that corresponds to this group or question (e.g. a code from LOINC, which defines many questions and answers).)
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
         * @return {@link #prefix} (A short label for a particular group, question or set of display text within the questionnaire.). This is the underlying object with id, value and extensions. The accessor "getPrefix" gives direct access to the value
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
         * @param value {@link #prefix} (A short label for a particular group, question or set of display text within the questionnaire.). This is the underlying object with id, value and extensions. The accessor "getPrefix" gives direct access to the value
         */
        public QuestionnaireItemComponent setPrefixElement(StringType value) { 
          this.prefix = value;
          return this;
        }

        /**
         * @return A short label for a particular group, question or set of display text within the questionnaire.
         */
        public String getPrefix() { 
          return this.prefix == null ? null : this.prefix.getValue();
        }

        /**
         * @param value A short label for a particular group, question or set of display text within the questionnaire.
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
         * @return {@link #text} (The name of a section, the text of a question or text content for a text item.). This is the underlying object with id, value and extensions. The accessor "getText" gives direct access to the value
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
         * @param value {@link #text} (The name of a section, the text of a question or text content for a text item.). This is the underlying object with id, value and extensions. The accessor "getText" gives direct access to the value
         */
        public QuestionnaireItemComponent setTextElement(StringType value) { 
          this.text = value;
          return this;
        }

        /**
         * @return The name of a section, the text of a question or text content for a text item.
         */
        public String getText() { 
          return this.text == null ? null : this.text.getValue();
        }

        /**
         * @param value The name of a section, the text of a question or text content for a text item.
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
         * @return {@link #type} (Identifies the type of questionnaire item this is - whether text for display, a grouping of other items or a particular type of data to be captured (string, integer, coded choice, etc.).). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
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
         * @param value {@link #type} (Identifies the type of questionnaire item this is - whether text for display, a grouping of other items or a particular type of data to be captured (string, integer, coded choice, etc.).). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public QuestionnaireItemComponent setTypeElement(Enumeration<QuestionnaireItemType> value) { 
          this.type = value;
          return this;
        }

        /**
         * @return Identifies the type of questionnaire item this is - whether text for display, a grouping of other items or a particular type of data to be captured (string, integer, coded choice, etc.).
         */
        public QuestionnaireItemType getType() { 
          return this.type == null ? null : this.type.getValue();
        }

        /**
         * @param value Identifies the type of questionnaire item this is - whether text for display, a grouping of other items or a particular type of data to be captured (string, integer, coded choice, etc.).
         */
        public QuestionnaireItemComponent setType(QuestionnaireItemType value) { 
            if (this.type == null)
              this.type = new Enumeration<QuestionnaireItemType>(new QuestionnaireItemTypeEnumFactory());
            this.type.setValue(value);
          return this;
        }

        /**
         * @return {@link #enableWhen} (If present, indicates that this item should only be enabled (displayed/allow answers to be captured) when the specified condition is true.)
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
         * @return {@link #required} (If true, indicates that the item must be present in a "completed" QuestionnaireResponse.  If false, the item may be skipped when answering the questionnaire.). This is the underlying object with id, value and extensions. The accessor "getRequired" gives direct access to the value
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
         * @param value {@link #required} (If true, indicates that the item must be present in a "completed" QuestionnaireResponse.  If false, the item may be skipped when answering the questionnaire.). This is the underlying object with id, value and extensions. The accessor "getRequired" gives direct access to the value
         */
        public QuestionnaireItemComponent setRequiredElement(BooleanType value) { 
          this.required = value;
          return this;
        }

        /**
         * @return If true, indicates that the item must be present in a "completed" QuestionnaireResponse.  If false, the item may be skipped when answering the questionnaire.
         */
        public boolean getRequired() { 
          return this.required == null || this.required.isEmpty() ? false : this.required.getValue();
        }

        /**
         * @param value If true, indicates that the item must be present in a "completed" QuestionnaireResponse.  If false, the item may be skipped when answering the questionnaire.
         */
        public QuestionnaireItemComponent setRequired(boolean value) { 
            if (this.required == null)
              this.required = new BooleanType();
            this.required.setValue(value);
          return this;
        }

        /**
         * @return {@link #repeats} (Whether the item may occur multiple times in the instance, containing multiple sets of answers.). This is the underlying object with id, value and extensions. The accessor "getRepeats" gives direct access to the value
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
         * @param value {@link #repeats} (Whether the item may occur multiple times in the instance, containing multiple sets of answers.). This is the underlying object with id, value and extensions. The accessor "getRepeats" gives direct access to the value
         */
        public QuestionnaireItemComponent setRepeatsElement(BooleanType value) { 
          this.repeats = value;
          return this;
        }

        /**
         * @return Whether the item may occur multiple times in the instance, containing multiple sets of answers.
         */
        public boolean getRepeats() { 
          return this.repeats == null || this.repeats.isEmpty() ? false : this.repeats.getValue();
        }

        /**
         * @param value Whether the item may occur multiple times in the instance, containing multiple sets of answers.
         */
        public QuestionnaireItemComponent setRepeats(boolean value) { 
            if (this.repeats == null)
              this.repeats = new BooleanType();
            this.repeats.setValue(value);
          return this;
        }

        /**
         * @return {@link #readOnly} (If true, the value cannot be changed by a human respondent to the Questionnaire.). This is the underlying object with id, value and extensions. The accessor "getReadOnly" gives direct access to the value
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
         * @param value {@link #readOnly} (If true, the value cannot be changed by a human respondent to the Questionnaire.). This is the underlying object with id, value and extensions. The accessor "getReadOnly" gives direct access to the value
         */
        public QuestionnaireItemComponent setReadOnlyElement(BooleanType value) { 
          this.readOnly = value;
          return this;
        }

        /**
         * @return If true, the value cannot be changed by a human respondent to the Questionnaire.
         */
        public boolean getReadOnly() { 
          return this.readOnly == null || this.readOnly.isEmpty() ? false : this.readOnly.getValue();
        }

        /**
         * @param value If true, the value cannot be changed by a human respondent to the Questionnaire.
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
         * @return {@link #options} (Reference to a value set containing a list of codes representing permitted answers for the question.)
         */
        public Reference getOptions() { 
          if (this.options == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create QuestionnaireItemComponent.options");
            else if (Configuration.doAutoCreate())
              this.options = new Reference(); // cc
          return this.options;
        }

        public boolean hasOptions() { 
          return this.options != null && !this.options.isEmpty();
        }

        /**
         * @param value {@link #options} (Reference to a value set containing a list of codes representing permitted answers for the question.)
         */
        public QuestionnaireItemComponent setOptions(Reference value) { 
          this.options = value;
          return this;
        }

        /**
         * @return {@link #options} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Reference to a value set containing a list of codes representing permitted answers for the question.)
         */
        public ValueSet getOptionsTarget() { 
          if (this.optionsTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create QuestionnaireItemComponent.options");
            else if (Configuration.doAutoCreate())
              this.optionsTarget = new ValueSet(); // aa
          return this.optionsTarget;
        }

        /**
         * @param value {@link #options} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Reference to a value set containing a list of codes representing permitted answers for the question.)
         */
        public QuestionnaireItemComponent setOptionsTarget(ValueSet value) { 
          this.optionsTarget = value;
          return this;
        }

        /**
         * @return {@link #option} (For a "choice" question, identifies one of the permitted answers for the question.)
         */
        public List<QuestionnaireItemOptionComponent> getOption() { 
          if (this.option == null)
            this.option = new ArrayList<QuestionnaireItemOptionComponent>();
          return this.option;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public QuestionnaireItemComponent setOption(List<QuestionnaireItemOptionComponent> theOption) { 
          this.option = theOption;
          return this;
        }

        public boolean hasOption() { 
          if (this.option == null)
            return false;
          for (QuestionnaireItemOptionComponent item : this.option)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public QuestionnaireItemOptionComponent addOption() { //3
          QuestionnaireItemOptionComponent t = new QuestionnaireItemOptionComponent();
          if (this.option == null)
            this.option = new ArrayList<QuestionnaireItemOptionComponent>();
          this.option.add(t);
          return t;
        }

        public QuestionnaireItemComponent addOption(QuestionnaireItemOptionComponent t) { //3
          if (t == null)
            return this;
          if (this.option == null)
            this.option = new ArrayList<QuestionnaireItemOptionComponent>();
          this.option.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #option}, creating it if it does not already exist
         */
        public QuestionnaireItemOptionComponent getOptionFirstRep() { 
          if (getOption().isEmpty()) {
            addOption();
          }
          return getOption().get(0);
        }

        /**
         * @return {@link #initial} (The value that should be defaulted when rendering the questionnaire for user input.)
         */
        public Type getInitial() { 
          return this.initial;
        }

        /**
         * @return {@link #initial} (The value that should be defaulted when rendering the questionnaire for user input.)
         */
        public BooleanType getInitialBooleanType() throws FHIRException { 
          if (!(this.initial instanceof BooleanType))
            throw new FHIRException("Type mismatch: the type BooleanType was expected, but "+this.initial.getClass().getName()+" was encountered");
          return (BooleanType) this.initial;
        }

        public boolean hasInitialBooleanType() { 
          return this.initial instanceof BooleanType;
        }

        /**
         * @return {@link #initial} (The value that should be defaulted when rendering the questionnaire for user input.)
         */
        public DecimalType getInitialDecimalType() throws FHIRException { 
          if (!(this.initial instanceof DecimalType))
            throw new FHIRException("Type mismatch: the type DecimalType was expected, but "+this.initial.getClass().getName()+" was encountered");
          return (DecimalType) this.initial;
        }

        public boolean hasInitialDecimalType() { 
          return this.initial instanceof DecimalType;
        }

        /**
         * @return {@link #initial} (The value that should be defaulted when rendering the questionnaire for user input.)
         */
        public IntegerType getInitialIntegerType() throws FHIRException { 
          if (!(this.initial instanceof IntegerType))
            throw new FHIRException("Type mismatch: the type IntegerType was expected, but "+this.initial.getClass().getName()+" was encountered");
          return (IntegerType) this.initial;
        }

        public boolean hasInitialIntegerType() { 
          return this.initial instanceof IntegerType;
        }

        /**
         * @return {@link #initial} (The value that should be defaulted when rendering the questionnaire for user input.)
         */
        public DateType getInitialDateType() throws FHIRException { 
          if (!(this.initial instanceof DateType))
            throw new FHIRException("Type mismatch: the type DateType was expected, but "+this.initial.getClass().getName()+" was encountered");
          return (DateType) this.initial;
        }

        public boolean hasInitialDateType() { 
          return this.initial instanceof DateType;
        }

        /**
         * @return {@link #initial} (The value that should be defaulted when rendering the questionnaire for user input.)
         */
        public DateTimeType getInitialDateTimeType() throws FHIRException { 
          if (!(this.initial instanceof DateTimeType))
            throw new FHIRException("Type mismatch: the type DateTimeType was expected, but "+this.initial.getClass().getName()+" was encountered");
          return (DateTimeType) this.initial;
        }

        public boolean hasInitialDateTimeType() { 
          return this.initial instanceof DateTimeType;
        }

        /**
         * @return {@link #initial} (The value that should be defaulted when rendering the questionnaire for user input.)
         */
        public TimeType getInitialTimeType() throws FHIRException { 
          if (!(this.initial instanceof TimeType))
            throw new FHIRException("Type mismatch: the type TimeType was expected, but "+this.initial.getClass().getName()+" was encountered");
          return (TimeType) this.initial;
        }

        public boolean hasInitialTimeType() { 
          return this.initial instanceof TimeType;
        }

        /**
         * @return {@link #initial} (The value that should be defaulted when rendering the questionnaire for user input.)
         */
        public StringType getInitialStringType() throws FHIRException { 
          if (!(this.initial instanceof StringType))
            throw new FHIRException("Type mismatch: the type StringType was expected, but "+this.initial.getClass().getName()+" was encountered");
          return (StringType) this.initial;
        }

        public boolean hasInitialStringType() { 
          return this.initial instanceof StringType;
        }

        /**
         * @return {@link #initial} (The value that should be defaulted when rendering the questionnaire for user input.)
         */
        public UriType getInitialUriType() throws FHIRException { 
          if (!(this.initial instanceof UriType))
            throw new FHIRException("Type mismatch: the type UriType was expected, but "+this.initial.getClass().getName()+" was encountered");
          return (UriType) this.initial;
        }

        public boolean hasInitialUriType() { 
          return this.initial instanceof UriType;
        }

        /**
         * @return {@link #initial} (The value that should be defaulted when rendering the questionnaire for user input.)
         */
        public Attachment getInitialAttachment() throws FHIRException { 
          if (!(this.initial instanceof Attachment))
            throw new FHIRException("Type mismatch: the type Attachment was expected, but "+this.initial.getClass().getName()+" was encountered");
          return (Attachment) this.initial;
        }

        public boolean hasInitialAttachment() { 
          return this.initial instanceof Attachment;
        }

        /**
         * @return {@link #initial} (The value that should be defaulted when rendering the questionnaire for user input.)
         */
        public Coding getInitialCoding() throws FHIRException { 
          if (!(this.initial instanceof Coding))
            throw new FHIRException("Type mismatch: the type Coding was expected, but "+this.initial.getClass().getName()+" was encountered");
          return (Coding) this.initial;
        }

        public boolean hasInitialCoding() { 
          return this.initial instanceof Coding;
        }

        /**
         * @return {@link #initial} (The value that should be defaulted when rendering the questionnaire for user input.)
         */
        public Quantity getInitialQuantity() throws FHIRException { 
          if (!(this.initial instanceof Quantity))
            throw new FHIRException("Type mismatch: the type Quantity was expected, but "+this.initial.getClass().getName()+" was encountered");
          return (Quantity) this.initial;
        }

        public boolean hasInitialQuantity() { 
          return this.initial instanceof Quantity;
        }

        /**
         * @return {@link #initial} (The value that should be defaulted when rendering the questionnaire for user input.)
         */
        public Reference getInitialReference() throws FHIRException { 
          if (!(this.initial instanceof Reference))
            throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.initial.getClass().getName()+" was encountered");
          return (Reference) this.initial;
        }

        public boolean hasInitialReference() { 
          return this.initial instanceof Reference;
        }

        public boolean hasInitial() { 
          return this.initial != null && !this.initial.isEmpty();
        }

        /**
         * @param value {@link #initial} (The value that should be defaulted when rendering the questionnaire for user input.)
         */
        public QuestionnaireItemComponent setInitial(Type value) { 
          this.initial = value;
          return this;
        }

        /**
         * @return {@link #item} (Allows text, questions and other groups to be nested beneath a question or group.)
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

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("linkId", "string", "An identifier that is unique within the Questionnaire allowing linkage to the equivalent item in a QuestionnaireResponse resource.", 0, java.lang.Integer.MAX_VALUE, linkId));
          childrenList.add(new Property("definition", "uri", "A reference to an [[[ElementDefinition]]] that provides the details for the item. If a definition is provided, then the following element values can be inferred from the definition: \n\n* code (ElementDefinition.code)\n* type (ElementDefinition.type)\n* required (ElementDefinition.min)\n* repeats (ElementDefinition.max)\n* maxLength (ElementDefinition.maxLength)\n* options (ElementDefinition.binding)\n\nAny information provided in these elements on a Questionnaire Item overrides the information from the definition.", 0, java.lang.Integer.MAX_VALUE, definition));
          childrenList.add(new Property("code", "Coding", "A Terminology code that corresponds to this group or question (e.g. a code from LOINC, which defines many questions and answers).", 0, java.lang.Integer.MAX_VALUE, code));
          childrenList.add(new Property("prefix", "string", "A short label for a particular group, question or set of display text within the questionnaire.", 0, java.lang.Integer.MAX_VALUE, prefix));
          childrenList.add(new Property("text", "string", "The name of a section, the text of a question or text content for a text item.", 0, java.lang.Integer.MAX_VALUE, text));
          childrenList.add(new Property("type", "code", "Identifies the type of questionnaire item this is - whether text for display, a grouping of other items or a particular type of data to be captured (string, integer, coded choice, etc.).", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("enableWhen", "", "If present, indicates that this item should only be enabled (displayed/allow answers to be captured) when the specified condition is true.", 0, java.lang.Integer.MAX_VALUE, enableWhen));
          childrenList.add(new Property("required", "boolean", "If true, indicates that the item must be present in a \"completed\" QuestionnaireResponse.  If false, the item may be skipped when answering the questionnaire.", 0, java.lang.Integer.MAX_VALUE, required));
          childrenList.add(new Property("repeats", "boolean", "Whether the item may occur multiple times in the instance, containing multiple sets of answers.", 0, java.lang.Integer.MAX_VALUE, repeats));
          childrenList.add(new Property("readOnly", "boolean", "If true, the value cannot be changed by a human respondent to the Questionnaire.", 0, java.lang.Integer.MAX_VALUE, readOnly));
          childrenList.add(new Property("maxLength", "integer", "The maximum number of characters that are permitted in the answer to be considered a \"valid\" QuestionnaireResponse.", 0, java.lang.Integer.MAX_VALUE, maxLength));
          childrenList.add(new Property("options", "Reference(ValueSet)", "Reference to a value set containing a list of codes representing permitted answers for the question.", 0, java.lang.Integer.MAX_VALUE, options));
          childrenList.add(new Property("option", "", "For a \"choice\" question, identifies one of the permitted answers for the question.", 0, java.lang.Integer.MAX_VALUE, option));
          childrenList.add(new Property("initial[x]", "boolean|decimal|integer|date|dateTime|time|string|uri|Attachment|Coding|Quantity|Reference(Any)", "The value that should be defaulted when rendering the questionnaire for user input.", 0, java.lang.Integer.MAX_VALUE, initial));
          childrenList.add(new Property("item", "@Questionnaire.item", "Allows text, questions and other groups to be nested beneath a question or group.", 0, java.lang.Integer.MAX_VALUE, item));
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
        case -393139297: /*required*/ return this.required == null ? new Base[0] : new Base[] {this.required}; // BooleanType
        case 1094288952: /*repeats*/ return this.repeats == null ? new Base[0] : new Base[] {this.repeats}; // BooleanType
        case -867683742: /*readOnly*/ return this.readOnly == null ? new Base[0] : new Base[] {this.readOnly}; // BooleanType
        case -791400086: /*maxLength*/ return this.maxLength == null ? new Base[0] : new Base[] {this.maxLength}; // IntegerType
        case -1249474914: /*options*/ return this.options == null ? new Base[0] : new Base[] {this.options}; // Reference
        case -1010136971: /*option*/ return this.option == null ? new Base[0] : this.option.toArray(new Base[this.option.size()]); // QuestionnaireItemOptionComponent
        case 1948342084: /*initial*/ return this.initial == null ? new Base[0] : new Base[] {this.initial}; // Type
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
        case -1249474914: // options
          this.options = castToReference(value); // Reference
          return value;
        case -1010136971: // option
          this.getOption().add((QuestionnaireItemOptionComponent) value); // QuestionnaireItemOptionComponent
          return value;
        case 1948342084: // initial
          this.initial = castToType(value); // Type
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
        } else if (name.equals("required")) {
          this.required = castToBoolean(value); // BooleanType
        } else if (name.equals("repeats")) {
          this.repeats = castToBoolean(value); // BooleanType
        } else if (name.equals("readOnly")) {
          this.readOnly = castToBoolean(value); // BooleanType
        } else if (name.equals("maxLength")) {
          this.maxLength = castToInteger(value); // IntegerType
        } else if (name.equals("options")) {
          this.options = castToReference(value); // Reference
        } else if (name.equals("option")) {
          this.getOption().add((QuestionnaireItemOptionComponent) value);
        } else if (name.equals("initial[x]")) {
          this.initial = castToType(value); // Type
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
        case -393139297:  return getRequiredElement();
        case 1094288952:  return getRepeatsElement();
        case -867683742:  return getReadOnlyElement();
        case -791400086:  return getMaxLengthElement();
        case -1249474914:  return getOptions(); 
        case -1010136971:  return addOption(); 
        case 871077564:  return getInitial(); 
        case 1948342084:  return getInitial(); 
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
        case -393139297: /*required*/ return new String[] {"boolean"};
        case 1094288952: /*repeats*/ return new String[] {"boolean"};
        case -867683742: /*readOnly*/ return new String[] {"boolean"};
        case -791400086: /*maxLength*/ return new String[] {"integer"};
        case -1249474914: /*options*/ return new String[] {"Reference"};
        case -1010136971: /*option*/ return new String[] {};
        case 1948342084: /*initial*/ return new String[] {"boolean", "decimal", "integer", "date", "dateTime", "time", "string", "uri", "Attachment", "Coding", "Quantity", "Reference"};
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
        else if (name.equals("options")) {
          this.options = new Reference();
          return this.options;
        }
        else if (name.equals("option")) {
          return addOption();
        }
        else if (name.equals("initialBoolean")) {
          this.initial = new BooleanType();
          return this.initial;
        }
        else if (name.equals("initialDecimal")) {
          this.initial = new DecimalType();
          return this.initial;
        }
        else if (name.equals("initialInteger")) {
          this.initial = new IntegerType();
          return this.initial;
        }
        else if (name.equals("initialDate")) {
          this.initial = new DateType();
          return this.initial;
        }
        else if (name.equals("initialDateTime")) {
          this.initial = new DateTimeType();
          return this.initial;
        }
        else if (name.equals("initialTime")) {
          this.initial = new TimeType();
          return this.initial;
        }
        else if (name.equals("initialString")) {
          this.initial = new StringType();
          return this.initial;
        }
        else if (name.equals("initialUri")) {
          this.initial = new UriType();
          return this.initial;
        }
        else if (name.equals("initialAttachment")) {
          this.initial = new Attachment();
          return this.initial;
        }
        else if (name.equals("initialCoding")) {
          this.initial = new Coding();
          return this.initial;
        }
        else if (name.equals("initialQuantity")) {
          this.initial = new Quantity();
          return this.initial;
        }
        else if (name.equals("initialReference")) {
          this.initial = new Reference();
          return this.initial;
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
        dst.required = required == null ? null : required.copy();
        dst.repeats = repeats == null ? null : repeats.copy();
        dst.readOnly = readOnly == null ? null : readOnly.copy();
        dst.maxLength = maxLength == null ? null : maxLength.copy();
        dst.options = options == null ? null : options.copy();
        if (option != null) {
          dst.option = new ArrayList<QuestionnaireItemOptionComponent>();
          for (QuestionnaireItemOptionComponent i : option)
            dst.option.add(i.copy());
        };
        dst.initial = initial == null ? null : initial.copy();
        if (item != null) {
          dst.item = new ArrayList<QuestionnaireItemComponent>();
          for (QuestionnaireItemComponent i : item)
            dst.item.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof QuestionnaireItemComponent))
          return false;
        QuestionnaireItemComponent o = (QuestionnaireItemComponent) other;
        return compareDeep(linkId, o.linkId, true) && compareDeep(definition, o.definition, true) && compareDeep(code, o.code, true)
           && compareDeep(prefix, o.prefix, true) && compareDeep(text, o.text, true) && compareDeep(type, o.type, true)
           && compareDeep(enableWhen, o.enableWhen, true) && compareDeep(required, o.required, true) && compareDeep(repeats, o.repeats, true)
           && compareDeep(readOnly, o.readOnly, true) && compareDeep(maxLength, o.maxLength, true) && compareDeep(options, o.options, true)
           && compareDeep(option, o.option, true) && compareDeep(initial, o.initial, true) && compareDeep(item, o.item, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof QuestionnaireItemComponent))
          return false;
        QuestionnaireItemComponent o = (QuestionnaireItemComponent) other;
        return compareValues(linkId, o.linkId, true) && compareValues(definition, o.definition, true) && compareValues(prefix, o.prefix, true)
           && compareValues(text, o.text, true) && compareValues(type, o.type, true) && compareValues(required, o.required, true)
           && compareValues(repeats, o.repeats, true) && compareValues(readOnly, o.readOnly, true) && compareValues(maxLength, o.maxLength, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(linkId, definition, code
          , prefix, text, type, enableWhen, required, repeats, readOnly, maxLength, options
          , option, initial, item);
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
         * If present, indicates that this item should be enabled only if the specified question is answered or not answered.
         */
        @Child(name = "hasAnswer", type = {BooleanType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Enable when answered or not", formalDefinition="If present, indicates that this item should be enabled only if the specified question is answered or not answered." )
        protected BooleanType hasAnswer;

        /**
         * If present, then the answer for the referenced question must match this answer for all components present in the enableWhen.answer.
         */
        @Child(name = "answer", type = {BooleanType.class, DecimalType.class, IntegerType.class, DateType.class, DateTimeType.class, TimeType.class, StringType.class, UriType.class, Attachment.class, Coding.class, Quantity.class, Reference.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Value question must have", formalDefinition="If present, then the answer for the referenced question must match this answer for all components present in the enableWhen.answer." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/questionnaire-answers")
        protected Type answer;

        private static final long serialVersionUID = -300241115L;

    /**
     * Constructor
     */
      public QuestionnaireItemEnableWhenComponent() {
        super();
      }

    /**
     * Constructor
     */
      public QuestionnaireItemEnableWhenComponent(StringType question) {
        super();
        this.question = question;
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
         * @return {@link #hasAnswer} (If present, indicates that this item should be enabled only if the specified question is answered or not answered.). This is the underlying object with id, value and extensions. The accessor "getHasAnswer" gives direct access to the value
         */
        public BooleanType getHasAnswerElement() { 
          if (this.hasAnswer == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create QuestionnaireItemEnableWhenComponent.hasAnswer");
            else if (Configuration.doAutoCreate())
              this.hasAnswer = new BooleanType(); // bb
          return this.hasAnswer;
        }

        public boolean hasHasAnswerElement() { 
          return this.hasAnswer != null && !this.hasAnswer.isEmpty();
        }

        public boolean hasHasAnswer() { 
          return this.hasAnswer != null && !this.hasAnswer.isEmpty();
        }

        /**
         * @param value {@link #hasAnswer} (If present, indicates that this item should be enabled only if the specified question is answered or not answered.). This is the underlying object with id, value and extensions. The accessor "getHasAnswer" gives direct access to the value
         */
        public QuestionnaireItemEnableWhenComponent setHasAnswerElement(BooleanType value) { 
          this.hasAnswer = value;
          return this;
        }

        /**
         * @return If present, indicates that this item should be enabled only if the specified question is answered or not answered.
         */
        public boolean getHasAnswer() { 
          return this.hasAnswer == null || this.hasAnswer.isEmpty() ? false : this.hasAnswer.getValue();
        }

        /**
         * @param value If present, indicates that this item should be enabled only if the specified question is answered or not answered.
         */
        public QuestionnaireItemEnableWhenComponent setHasAnswer(boolean value) { 
            if (this.hasAnswer == null)
              this.hasAnswer = new BooleanType();
            this.hasAnswer.setValue(value);
          return this;
        }

        /**
         * @return {@link #answer} (If present, then the answer for the referenced question must match this answer for all components present in the enableWhen.answer.)
         */
        public Type getAnswer() { 
          return this.answer;
        }

        /**
         * @return {@link #answer} (If present, then the answer for the referenced question must match this answer for all components present in the enableWhen.answer.)
         */
        public BooleanType getAnswerBooleanType() throws FHIRException { 
          if (!(this.answer instanceof BooleanType))
            throw new FHIRException("Type mismatch: the type BooleanType was expected, but "+this.answer.getClass().getName()+" was encountered");
          return (BooleanType) this.answer;
        }

        public boolean hasAnswerBooleanType() { 
          return this.answer instanceof BooleanType;
        }

        /**
         * @return {@link #answer} (If present, then the answer for the referenced question must match this answer for all components present in the enableWhen.answer.)
         */
        public DecimalType getAnswerDecimalType() throws FHIRException { 
          if (!(this.answer instanceof DecimalType))
            throw new FHIRException("Type mismatch: the type DecimalType was expected, but "+this.answer.getClass().getName()+" was encountered");
          return (DecimalType) this.answer;
        }

        public boolean hasAnswerDecimalType() { 
          return this.answer instanceof DecimalType;
        }

        /**
         * @return {@link #answer} (If present, then the answer for the referenced question must match this answer for all components present in the enableWhen.answer.)
         */
        public IntegerType getAnswerIntegerType() throws FHIRException { 
          if (!(this.answer instanceof IntegerType))
            throw new FHIRException("Type mismatch: the type IntegerType was expected, but "+this.answer.getClass().getName()+" was encountered");
          return (IntegerType) this.answer;
        }

        public boolean hasAnswerIntegerType() { 
          return this.answer instanceof IntegerType;
        }

        /**
         * @return {@link #answer} (If present, then the answer for the referenced question must match this answer for all components present in the enableWhen.answer.)
         */
        public DateType getAnswerDateType() throws FHIRException { 
          if (!(this.answer instanceof DateType))
            throw new FHIRException("Type mismatch: the type DateType was expected, but "+this.answer.getClass().getName()+" was encountered");
          return (DateType) this.answer;
        }

        public boolean hasAnswerDateType() { 
          return this.answer instanceof DateType;
        }

        /**
         * @return {@link #answer} (If present, then the answer for the referenced question must match this answer for all components present in the enableWhen.answer.)
         */
        public DateTimeType getAnswerDateTimeType() throws FHIRException { 
          if (!(this.answer instanceof DateTimeType))
            throw new FHIRException("Type mismatch: the type DateTimeType was expected, but "+this.answer.getClass().getName()+" was encountered");
          return (DateTimeType) this.answer;
        }

        public boolean hasAnswerDateTimeType() { 
          return this.answer instanceof DateTimeType;
        }

        /**
         * @return {@link #answer} (If present, then the answer for the referenced question must match this answer for all components present in the enableWhen.answer.)
         */
        public TimeType getAnswerTimeType() throws FHIRException { 
          if (!(this.answer instanceof TimeType))
            throw new FHIRException("Type mismatch: the type TimeType was expected, but "+this.answer.getClass().getName()+" was encountered");
          return (TimeType) this.answer;
        }

        public boolean hasAnswerTimeType() { 
          return this.answer instanceof TimeType;
        }

        /**
         * @return {@link #answer} (If present, then the answer for the referenced question must match this answer for all components present in the enableWhen.answer.)
         */
        public StringType getAnswerStringType() throws FHIRException { 
          if (!(this.answer instanceof StringType))
            throw new FHIRException("Type mismatch: the type StringType was expected, but "+this.answer.getClass().getName()+" was encountered");
          return (StringType) this.answer;
        }

        public boolean hasAnswerStringType() { 
          return this.answer instanceof StringType;
        }

        /**
         * @return {@link #answer} (If present, then the answer for the referenced question must match this answer for all components present in the enableWhen.answer.)
         */
        public UriType getAnswerUriType() throws FHIRException { 
          if (!(this.answer instanceof UriType))
            throw new FHIRException("Type mismatch: the type UriType was expected, but "+this.answer.getClass().getName()+" was encountered");
          return (UriType) this.answer;
        }

        public boolean hasAnswerUriType() { 
          return this.answer instanceof UriType;
        }

        /**
         * @return {@link #answer} (If present, then the answer for the referenced question must match this answer for all components present in the enableWhen.answer.)
         */
        public Attachment getAnswerAttachment() throws FHIRException { 
          if (!(this.answer instanceof Attachment))
            throw new FHIRException("Type mismatch: the type Attachment was expected, but "+this.answer.getClass().getName()+" was encountered");
          return (Attachment) this.answer;
        }

        public boolean hasAnswerAttachment() { 
          return this.answer instanceof Attachment;
        }

        /**
         * @return {@link #answer} (If present, then the answer for the referenced question must match this answer for all components present in the enableWhen.answer.)
         */
        public Coding getAnswerCoding() throws FHIRException { 
          if (!(this.answer instanceof Coding))
            throw new FHIRException("Type mismatch: the type Coding was expected, but "+this.answer.getClass().getName()+" was encountered");
          return (Coding) this.answer;
        }

        public boolean hasAnswerCoding() { 
          return this.answer instanceof Coding;
        }

        /**
         * @return {@link #answer} (If present, then the answer for the referenced question must match this answer for all components present in the enableWhen.answer.)
         */
        public Quantity getAnswerQuantity() throws FHIRException { 
          if (!(this.answer instanceof Quantity))
            throw new FHIRException("Type mismatch: the type Quantity was expected, but "+this.answer.getClass().getName()+" was encountered");
          return (Quantity) this.answer;
        }

        public boolean hasAnswerQuantity() { 
          return this.answer instanceof Quantity;
        }

        /**
         * @return {@link #answer} (If present, then the answer for the referenced question must match this answer for all components present in the enableWhen.answer.)
         */
        public Reference getAnswerReference() throws FHIRException { 
          if (!(this.answer instanceof Reference))
            throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.answer.getClass().getName()+" was encountered");
          return (Reference) this.answer;
        }

        public boolean hasAnswerReference() { 
          return this.answer instanceof Reference;
        }

        public boolean hasAnswer() { 
          return this.answer != null && !this.answer.isEmpty();
        }

        /**
         * @param value {@link #answer} (If present, then the answer for the referenced question must match this answer for all components present in the enableWhen.answer.)
         */
        public QuestionnaireItemEnableWhenComponent setAnswer(Type value) { 
          this.answer = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("question", "string", "The linkId for the question whose answer (or lack of answer) governs whether this item is enabled.", 0, java.lang.Integer.MAX_VALUE, question));
          childrenList.add(new Property("hasAnswer", "boolean", "If present, indicates that this item should be enabled only if the specified question is answered or not answered.", 0, java.lang.Integer.MAX_VALUE, hasAnswer));
          childrenList.add(new Property("answer[x]", "boolean|decimal|integer|date|dateTime|time|string|uri|Attachment|Coding|Quantity|Reference(Any)", "If present, then the answer for the referenced question must match this answer for all components present in the enableWhen.answer.", 0, java.lang.Integer.MAX_VALUE, answer));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1165870106: /*question*/ return this.question == null ? new Base[0] : new Base[] {this.question}; // StringType
        case -793058568: /*hasAnswer*/ return this.hasAnswer == null ? new Base[0] : new Base[] {this.hasAnswer}; // BooleanType
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
        case -793058568: // hasAnswer
          this.hasAnswer = castToBoolean(value); // BooleanType
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
        } else if (name.equals("hasAnswer")) {
          this.hasAnswer = castToBoolean(value); // BooleanType
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
        case -793058568:  return getHasAnswerElement();
        case 1693524994:  return getAnswer(); 
        case -1412808770:  return getAnswer(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1165870106: /*question*/ return new String[] {"string"};
        case -793058568: /*hasAnswer*/ return new String[] {"boolean"};
        case -1412808770: /*answer*/ return new String[] {"boolean", "decimal", "integer", "date", "dateTime", "time", "string", "uri", "Attachment", "Coding", "Quantity", "Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("question")) {
          throw new FHIRException("Cannot call addChild on a primitive type Questionnaire.question");
        }
        else if (name.equals("hasAnswer")) {
          throw new FHIRException("Cannot call addChild on a primitive type Questionnaire.hasAnswer");
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
        else if (name.equals("answerUri")) {
          this.answer = new UriType();
          return this.answer;
        }
        else if (name.equals("answerAttachment")) {
          this.answer = new Attachment();
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
        dst.hasAnswer = hasAnswer == null ? null : hasAnswer.copy();
        dst.answer = answer == null ? null : answer.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof QuestionnaireItemEnableWhenComponent))
          return false;
        QuestionnaireItemEnableWhenComponent o = (QuestionnaireItemEnableWhenComponent) other;
        return compareDeep(question, o.question, true) && compareDeep(hasAnswer, o.hasAnswer, true) && compareDeep(answer, o.answer, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof QuestionnaireItemEnableWhenComponent))
          return false;
        QuestionnaireItemEnableWhenComponent o = (QuestionnaireItemEnableWhenComponent) other;
        return compareValues(question, o.question, true) && compareValues(hasAnswer, o.hasAnswer, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(question, hasAnswer, answer
          );
      }

  public String fhirType() {
    return "Questionnaire.item.enableWhen";

  }

  }

    @Block()
    public static class QuestionnaireItemOptionComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Identifies a specific answer that's allowed as the answer to a question.
         */
        @Child(name = "value", type = {IntegerType.class, DateType.class, TimeType.class, StringType.class, Coding.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Answer value", formalDefinition="Identifies a specific answer that's allowed as the answer to a question." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/questionnaire-answers")
        protected Type value;

        private static final long serialVersionUID = -732981989L;

    /**
     * Constructor
     */
      public QuestionnaireItemOptionComponent() {
        super();
      }

    /**
     * Constructor
     */
      public QuestionnaireItemOptionComponent(Type value) {
        super();
        this.value = value;
      }

        /**
         * @return {@link #value} (Identifies a specific answer that's allowed as the answer to a question.)
         */
        public Type getValue() { 
          return this.value;
        }

        /**
         * @return {@link #value} (Identifies a specific answer that's allowed as the answer to a question.)
         */
        public IntegerType getValueIntegerType() throws FHIRException { 
          if (!(this.value instanceof IntegerType))
            throw new FHIRException("Type mismatch: the type IntegerType was expected, but "+this.value.getClass().getName()+" was encountered");
          return (IntegerType) this.value;
        }

        public boolean hasValueIntegerType() { 
          return this.value instanceof IntegerType;
        }

        /**
         * @return {@link #value} (Identifies a specific answer that's allowed as the answer to a question.)
         */
        public DateType getValueDateType() throws FHIRException { 
          if (!(this.value instanceof DateType))
            throw new FHIRException("Type mismatch: the type DateType was expected, but "+this.value.getClass().getName()+" was encountered");
          return (DateType) this.value;
        }

        public boolean hasValueDateType() { 
          return this.value instanceof DateType;
        }

        /**
         * @return {@link #value} (Identifies a specific answer that's allowed as the answer to a question.)
         */
        public TimeType getValueTimeType() throws FHIRException { 
          if (!(this.value instanceof TimeType))
            throw new FHIRException("Type mismatch: the type TimeType was expected, but "+this.value.getClass().getName()+" was encountered");
          return (TimeType) this.value;
        }

        public boolean hasValueTimeType() { 
          return this.value instanceof TimeType;
        }

        /**
         * @return {@link #value} (Identifies a specific answer that's allowed as the answer to a question.)
         */
        public StringType getValueStringType() throws FHIRException { 
          if (!(this.value instanceof StringType))
            throw new FHIRException("Type mismatch: the type StringType was expected, but "+this.value.getClass().getName()+" was encountered");
          return (StringType) this.value;
        }

        public boolean hasValueStringType() { 
          return this.value instanceof StringType;
        }

        /**
         * @return {@link #value} (Identifies a specific answer that's allowed as the answer to a question.)
         */
        public Coding getValueCoding() throws FHIRException { 
          if (!(this.value instanceof Coding))
            throw new FHIRException("Type mismatch: the type Coding was expected, but "+this.value.getClass().getName()+" was encountered");
          return (Coding) this.value;
        }

        public boolean hasValueCoding() { 
          return this.value instanceof Coding;
        }

        public boolean hasValue() { 
          return this.value != null && !this.value.isEmpty();
        }

        /**
         * @param value {@link #value} (Identifies a specific answer that's allowed as the answer to a question.)
         */
        public QuestionnaireItemOptionComponent setValue(Type value) { 
          this.value = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("value[x]", "integer|date|time|string|Coding", "Identifies a specific answer that's allowed as the answer to a question.", 0, java.lang.Integer.MAX_VALUE, value));
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
        case 111972721: /*value*/ return new String[] {"integer", "date", "time", "string", "Coding"};
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
        else
          return super.addChild(name);
      }

      public QuestionnaireItemOptionComponent copy() {
        QuestionnaireItemOptionComponent dst = new QuestionnaireItemOptionComponent();
        copyValues(dst);
        dst.value = value == null ? null : value.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof QuestionnaireItemOptionComponent))
          return false;
        QuestionnaireItemOptionComponent o = (QuestionnaireItemOptionComponent) other;
        return compareDeep(value, o.value, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof QuestionnaireItemOptionComponent))
          return false;
        QuestionnaireItemOptionComponent o = (QuestionnaireItemOptionComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(value);
      }

  public String fhirType() {
    return "Questionnaire.item.option";

  }

  }

    /**
     * An absolute URL that is used to identify this questionnaire when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this questionnaire is (or will be) published.
     */
    @Child(name = "url", type = {UriType.class}, order=0, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Globally unique logical identifier for  questionnaire", formalDefinition="An absolute URL that is used to identify this questionnaire when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this questionnaire is (or will be) published." )
    protected UriType url;

    /**
     * This records identifiers associated with this question set that are defined by business processes and/or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation).
     */
    @Child(name = "identifier", type = {Identifier.class}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="External identifiers for this questionnaire", formalDefinition="This records identifiers associated with this question set that are defined by business processes and/or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)." )
    protected List<Identifier> identifier;

    /**
     * The version number assigned by the publisher for business reasons.  It may remain the same when the resource is updated.
     */
    @Child(name = "version", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Logical identifier for this version of Questionnaire", formalDefinition="The version number assigned by the publisher for business reasons.  It may remain the same when the resource is updated." )
    protected StringType version;

    /**
     * The lifecycle status of the questionnaire as a whole.
     */
    @Child(name = "status", type = {CodeType.class}, order=3, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="draft | published | retired", formalDefinition="The lifecycle status of the questionnaire as a whole." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/questionnaire-status")
    protected Enumeration<QuestionnaireStatus> status;

    /**
     * The date that this questionnaire was last changed.
     */
    @Child(name = "date", type = {DateTimeType.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Date this version was authored", formalDefinition="The date that this questionnaire was last changed." )
    protected DateTimeType date;

    /**
     * Organization or person responsible for developing and maintaining the questionnaire.
     */
    @Child(name = "publisher", type = {StringType.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Organization/individual who designed the questionnaire", formalDefinition="Organization or person responsible for developing and maintaining the questionnaire." )
    protected StringType publisher;

    /**
     * Contact details to assist a user in finding and communicating with the publisher.
     */
    @Child(name = "telecom", type = {ContactPoint.class}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Contact information of the publisher", formalDefinition="Contact details to assist a user in finding and communicating with the publisher." )
    protected List<ContactPoint> telecom;

    /**
     * A code that identifies the questionnaire as falling into a particular group of like questionnaires; e.g. "Pediatric", "Admissions", "Research", "Demographic", "Opinion Survey", etc.
     */
    @Child(name = "useContext", type = {CodeableConcept.class}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Questionnaire intends to support these contexts", formalDefinition="A code that identifies the questionnaire as falling into a particular group of like questionnaires; e.g. \"Pediatric\", \"Admissions\", \"Research\", \"Demographic\", \"Opinion Survey\", etc." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/use-context")
    protected List<CodeableConcept> useContext;

    /**
     * The name or label associated with this questionnaire.
     */
    @Child(name = "title", type = {StringType.class}, order=8, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Name for the questionnaire", formalDefinition="The name or label associated with this questionnaire." )
    protected StringType title;

    /**
     * Identifies how this question or group of questions is known in a particular terminology such as LOINC.
     */
    @Child(name = "code", type = {Coding.class}, order=9, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Concept that represents the overall questionnaire", formalDefinition="Identifies how this question or group of questions is known in a particular terminology such as LOINC." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/questionnaire-questions")
    protected List<Coding> code;

    /**
     * Identifies the types of subjects that can be the subject of the questionnaire.
     */
    @Child(name = "subjectType", type = {CodeType.class}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Resource that can be subject of QuestionnaireResponse", formalDefinition="Identifies the types of subjects that can be the subject of the questionnaire." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/resource-types")
    protected List<CodeType> subjectType;

    /**
     * The questions and groupings of questions that make up the questionnaire.
     */
    @Child(name = "item", type = {}, order=11, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Questions and sections within the Questionnaire", formalDefinition="The questions and groupings of questions that make up the questionnaire." )
    protected List<QuestionnaireItemComponent> item;

    private static final long serialVersionUID = -334575587L;

  /**
   * Constructor
   */
    public Questionnaire() {
      super();
    }

  /**
   * Constructor
   */
    public Questionnaire(Enumeration<QuestionnaireStatus> status) {
      super();
      this.status = status;
    }

    /**
     * @return {@link #url} (An absolute URL that is used to identify this questionnaire when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this questionnaire is (or will be) published.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
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
     * @param value {@link #url} (An absolute URL that is used to identify this questionnaire when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this questionnaire is (or will be) published.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public Questionnaire setUrlElement(UriType value) { 
      this.url = value;
      return this;
    }

    /**
     * @return An absolute URL that is used to identify this questionnaire when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this questionnaire is (or will be) published.
     */
    public String getUrl() { 
      return this.url == null ? null : this.url.getValue();
    }

    /**
     * @param value An absolute URL that is used to identify this questionnaire when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this questionnaire is (or will be) published.
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
     * @return {@link #identifier} (This records identifiers associated with this question set that are defined by business processes and/or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation).)
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
     * @return {@link #version} (The version number assigned by the publisher for business reasons.  It may remain the same when the resource is updated.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
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
     * @param value {@link #version} (The version number assigned by the publisher for business reasons.  It may remain the same when the resource is updated.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
     */
    public Questionnaire setVersionElement(StringType value) { 
      this.version = value;
      return this;
    }

    /**
     * @return The version number assigned by the publisher for business reasons.  It may remain the same when the resource is updated.
     */
    public String getVersion() { 
      return this.version == null ? null : this.version.getValue();
    }

    /**
     * @param value The version number assigned by the publisher for business reasons.  It may remain the same when the resource is updated.
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
     * @return {@link #status} (The lifecycle status of the questionnaire as a whole.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<QuestionnaireStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Questionnaire.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<QuestionnaireStatus>(new QuestionnaireStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (The lifecycle status of the questionnaire as a whole.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Questionnaire setStatusElement(Enumeration<QuestionnaireStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The lifecycle status of the questionnaire as a whole.
     */
    public QuestionnaireStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The lifecycle status of the questionnaire as a whole.
     */
    public Questionnaire setStatus(QuestionnaireStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<QuestionnaireStatus>(new QuestionnaireStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #date} (The date that this questionnaire was last changed.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
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
     * @param value {@link #date} (The date that this questionnaire was last changed.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public Questionnaire setDateElement(DateTimeType value) { 
      this.date = value;
      return this;
    }

    /**
     * @return The date that this questionnaire was last changed.
     */
    public Date getDate() { 
      return this.date == null ? null : this.date.getValue();
    }

    /**
     * @param value The date that this questionnaire was last changed.
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
     * @return {@link #publisher} (Organization or person responsible for developing and maintaining the questionnaire.). This is the underlying object with id, value and extensions. The accessor "getPublisher" gives direct access to the value
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
     * @param value {@link #publisher} (Organization or person responsible for developing and maintaining the questionnaire.). This is the underlying object with id, value and extensions. The accessor "getPublisher" gives direct access to the value
     */
    public Questionnaire setPublisherElement(StringType value) { 
      this.publisher = value;
      return this;
    }

    /**
     * @return Organization or person responsible for developing and maintaining the questionnaire.
     */
    public String getPublisher() { 
      return this.publisher == null ? null : this.publisher.getValue();
    }

    /**
     * @param value Organization or person responsible for developing and maintaining the questionnaire.
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
     * @return {@link #telecom} (Contact details to assist a user in finding and communicating with the publisher.)
     */
    public List<ContactPoint> getTelecom() { 
      if (this.telecom == null)
        this.telecom = new ArrayList<ContactPoint>();
      return this.telecom;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Questionnaire setTelecom(List<ContactPoint> theTelecom) { 
      this.telecom = theTelecom;
      return this;
    }

    public boolean hasTelecom() { 
      if (this.telecom == null)
        return false;
      for (ContactPoint item : this.telecom)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public ContactPoint addTelecom() { //3
      ContactPoint t = new ContactPoint();
      if (this.telecom == null)
        this.telecom = new ArrayList<ContactPoint>();
      this.telecom.add(t);
      return t;
    }

    public Questionnaire addTelecom(ContactPoint t) { //3
      if (t == null)
        return this;
      if (this.telecom == null)
        this.telecom = new ArrayList<ContactPoint>();
      this.telecom.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #telecom}, creating it if it does not already exist
     */
    public ContactPoint getTelecomFirstRep() { 
      if (getTelecom().isEmpty()) {
        addTelecom();
      }
      return getTelecom().get(0);
    }

    /**
     * @return {@link #useContext} (A code that identifies the questionnaire as falling into a particular group of like questionnaires; e.g. "Pediatric", "Admissions", "Research", "Demographic", "Opinion Survey", etc.)
     */
    public List<CodeableConcept> getUseContext() { 
      if (this.useContext == null)
        this.useContext = new ArrayList<CodeableConcept>();
      return this.useContext;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Questionnaire setUseContext(List<CodeableConcept> theUseContext) { 
      this.useContext = theUseContext;
      return this;
    }

    public boolean hasUseContext() { 
      if (this.useContext == null)
        return false;
      for (CodeableConcept item : this.useContext)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addUseContext() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.useContext == null)
        this.useContext = new ArrayList<CodeableConcept>();
      this.useContext.add(t);
      return t;
    }

    public Questionnaire addUseContext(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.useContext == null)
        this.useContext = new ArrayList<CodeableConcept>();
      this.useContext.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #useContext}, creating it if it does not already exist
     */
    public CodeableConcept getUseContextFirstRep() { 
      if (getUseContext().isEmpty()) {
        addUseContext();
      }
      return getUseContext().get(0);
    }

    /**
     * @return {@link #title} (The name or label associated with this questionnaire.). This is the underlying object with id, value and extensions. The accessor "getTitle" gives direct access to the value
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
     * @param value {@link #title} (The name or label associated with this questionnaire.). This is the underlying object with id, value and extensions. The accessor "getTitle" gives direct access to the value
     */
    public Questionnaire setTitleElement(StringType value) { 
      this.title = value;
      return this;
    }

    /**
     * @return The name or label associated with this questionnaire.
     */
    public String getTitle() { 
      return this.title == null ? null : this.title.getValue();
    }

    /**
     * @param value The name or label associated with this questionnaire.
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
     * @return {@link #code} (Identifies how this question or group of questions is known in a particular terminology such as LOINC.)
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
     * @return {@link #subjectType} (Identifies the types of subjects that can be the subject of the questionnaire.)
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
     * @return {@link #subjectType} (Identifies the types of subjects that can be the subject of the questionnaire.)
     */
    public CodeType addSubjectTypeElement() {//2 
      CodeType t = new CodeType();
      if (this.subjectType == null)
        this.subjectType = new ArrayList<CodeType>();
      this.subjectType.add(t);
      return t;
    }

    /**
     * @param value {@link #subjectType} (Identifies the types of subjects that can be the subject of the questionnaire.)
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
     * @param value {@link #subjectType} (Identifies the types of subjects that can be the subject of the questionnaire.)
     */
    public boolean hasSubjectType(String value) { 
      if (this.subjectType == null)
        return false;
      for (CodeType v : this.subjectType)
        if (v.equals(value)) // code
          return true;
      return false;
    }

    /**
     * @return {@link #item} (The questions and groupings of questions that make up the questionnaire.)
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

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("url", "uri", "An absolute URL that is used to identify this questionnaire when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this questionnaire is (or will be) published.", 0, java.lang.Integer.MAX_VALUE, url));
        childrenList.add(new Property("identifier", "Identifier", "This records identifiers associated with this question set that are defined by business processes and/or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation).", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("version", "string", "The version number assigned by the publisher for business reasons.  It may remain the same when the resource is updated.", 0, java.lang.Integer.MAX_VALUE, version));
        childrenList.add(new Property("status", "code", "The lifecycle status of the questionnaire as a whole.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("date", "dateTime", "The date that this questionnaire was last changed.", 0, java.lang.Integer.MAX_VALUE, date));
        childrenList.add(new Property("publisher", "string", "Organization or person responsible for developing and maintaining the questionnaire.", 0, java.lang.Integer.MAX_VALUE, publisher));
        childrenList.add(new Property("telecom", "ContactPoint", "Contact details to assist a user in finding and communicating with the publisher.", 0, java.lang.Integer.MAX_VALUE, telecom));
        childrenList.add(new Property("useContext", "CodeableConcept", "A code that identifies the questionnaire as falling into a particular group of like questionnaires; e.g. \"Pediatric\", \"Admissions\", \"Research\", \"Demographic\", \"Opinion Survey\", etc.", 0, java.lang.Integer.MAX_VALUE, useContext));
        childrenList.add(new Property("title", "string", "The name or label associated with this questionnaire.", 0, java.lang.Integer.MAX_VALUE, title));
        childrenList.add(new Property("code", "Coding", "Identifies how this question or group of questions is known in a particular terminology such as LOINC.", 0, java.lang.Integer.MAX_VALUE, code));
        childrenList.add(new Property("subjectType", "code", "Identifies the types of subjects that can be the subject of the questionnaire.", 0, java.lang.Integer.MAX_VALUE, subjectType));
        childrenList.add(new Property("item", "", "The questions and groupings of questions that make up the questionnaire.", 0, java.lang.Integer.MAX_VALUE, item));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 116079: /*url*/ return this.url == null ? new Base[0] : new Base[] {this.url}; // UriType
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case 351608024: /*version*/ return this.version == null ? new Base[0] : new Base[] {this.version}; // StringType
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<QuestionnaireStatus>
        case 3076014: /*date*/ return this.date == null ? new Base[0] : new Base[] {this.date}; // DateTimeType
        case 1447404028: /*publisher*/ return this.publisher == null ? new Base[0] : new Base[] {this.publisher}; // StringType
        case -1429363305: /*telecom*/ return this.telecom == null ? new Base[0] : this.telecom.toArray(new Base[this.telecom.size()]); // ContactPoint
        case -669707736: /*useContext*/ return this.useContext == null ? new Base[0] : this.useContext.toArray(new Base[this.useContext.size()]); // CodeableConcept
        case 110371416: /*title*/ return this.title == null ? new Base[0] : new Base[] {this.title}; // StringType
        case 3059181: /*code*/ return this.code == null ? new Base[0] : this.code.toArray(new Base[this.code.size()]); // Coding
        case -603200890: /*subjectType*/ return this.subjectType == null ? new Base[0] : this.subjectType.toArray(new Base[this.subjectType.size()]); // CodeType
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
        case -892481550: // status
          value = new QuestionnaireStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<QuestionnaireStatus>
          return value;
        case 3076014: // date
          this.date = castToDateTime(value); // DateTimeType
          return value;
        case 1447404028: // publisher
          this.publisher = castToString(value); // StringType
          return value;
        case -1429363305: // telecom
          this.getTelecom().add(castToContactPoint(value)); // ContactPoint
          return value;
        case -669707736: // useContext
          this.getUseContext().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 110371416: // title
          this.title = castToString(value); // StringType
          return value;
        case 3059181: // code
          this.getCode().add(castToCoding(value)); // Coding
          return value;
        case -603200890: // subjectType
          this.getSubjectType().add(castToCode(value)); // CodeType
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
        } else if (name.equals("status")) {
          value = new QuestionnaireStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<QuestionnaireStatus>
        } else if (name.equals("date")) {
          this.date = castToDateTime(value); // DateTimeType
        } else if (name.equals("publisher")) {
          this.publisher = castToString(value); // StringType
        } else if (name.equals("telecom")) {
          this.getTelecom().add(castToContactPoint(value));
        } else if (name.equals("useContext")) {
          this.getUseContext().add(castToCodeableConcept(value));
        } else if (name.equals("title")) {
          this.title = castToString(value); // StringType
        } else if (name.equals("code")) {
          this.getCode().add(castToCoding(value));
        } else if (name.equals("subjectType")) {
          this.getSubjectType().add(castToCode(value));
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
        case -892481550:  return getStatusElement();
        case 3076014:  return getDateElement();
        case 1447404028:  return getPublisherElement();
        case -1429363305:  return addTelecom(); 
        case -669707736:  return addUseContext(); 
        case 110371416:  return getTitleElement();
        case 3059181:  return addCode(); 
        case -603200890:  return addSubjectTypeElement();
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
        case -892481550: /*status*/ return new String[] {"code"};
        case 3076014: /*date*/ return new String[] {"dateTime"};
        case 1447404028: /*publisher*/ return new String[] {"string"};
        case -1429363305: /*telecom*/ return new String[] {"ContactPoint"};
        case -669707736: /*useContext*/ return new String[] {"CodeableConcept"};
        case 110371416: /*title*/ return new String[] {"string"};
        case 3059181: /*code*/ return new String[] {"Coding"};
        case -603200890: /*subjectType*/ return new String[] {"code"};
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
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type Questionnaire.status");
        }
        else if (name.equals("date")) {
          throw new FHIRException("Cannot call addChild on a primitive type Questionnaire.date");
        }
        else if (name.equals("publisher")) {
          throw new FHIRException("Cannot call addChild on a primitive type Questionnaire.publisher");
        }
        else if (name.equals("telecom")) {
          return addTelecom();
        }
        else if (name.equals("useContext")) {
          return addUseContext();
        }
        else if (name.equals("title")) {
          throw new FHIRException("Cannot call addChild on a primitive type Questionnaire.title");
        }
        else if (name.equals("code")) {
          return addCode();
        }
        else if (name.equals("subjectType")) {
          throw new FHIRException("Cannot call addChild on a primitive type Questionnaire.subjectType");
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
        dst.status = status == null ? null : status.copy();
        dst.date = date == null ? null : date.copy();
        dst.publisher = publisher == null ? null : publisher.copy();
        if (telecom != null) {
          dst.telecom = new ArrayList<ContactPoint>();
          for (ContactPoint i : telecom)
            dst.telecom.add(i.copy());
        };
        if (useContext != null) {
          dst.useContext = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : useContext)
            dst.useContext.add(i.copy());
        };
        dst.title = title == null ? null : title.copy();
        if (code != null) {
          dst.code = new ArrayList<Coding>();
          for (Coding i : code)
            dst.code.add(i.copy());
        };
        if (subjectType != null) {
          dst.subjectType = new ArrayList<CodeType>();
          for (CodeType i : subjectType)
            dst.subjectType.add(i.copy());
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
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof Questionnaire))
          return false;
        Questionnaire o = (Questionnaire) other;
        return compareDeep(url, o.url, true) && compareDeep(identifier, o.identifier, true) && compareDeep(version, o.version, true)
           && compareDeep(status, o.status, true) && compareDeep(date, o.date, true) && compareDeep(publisher, o.publisher, true)
           && compareDeep(telecom, o.telecom, true) && compareDeep(useContext, o.useContext, true) && compareDeep(title, o.title, true)
           && compareDeep(code, o.code, true) && compareDeep(subjectType, o.subjectType, true) && compareDeep(item, o.item, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Questionnaire))
          return false;
        Questionnaire o = (Questionnaire) other;
        return compareValues(url, o.url, true) && compareValues(version, o.version, true) && compareValues(status, o.status, true)
           && compareValues(date, o.date, true) && compareValues(publisher, o.publisher, true) && compareValues(title, o.title, true)
           && compareValues(subjectType, o.subjectType, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(url, identifier, version
          , status, date, publisher, telecom, useContext, title, code, subjectType, item
          );
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Questionnaire;
   }

 /**
   * Search parameter: <b>date</b>
   * <p>
   * Description: <b>When the questionnaire was last changed</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Questionnaire.date</b><br>
   * </p>
   */
  @SearchParamDefinition(name="date", path="Questionnaire.date", description="When the questionnaire was last changed", type="date" )
  public static final String SP_DATE = "date";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>date</b>
   * <p>
   * Description: <b>When the questionnaire was last changed</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Questionnaire.date</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam DATE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_DATE);

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>An identifier for the questionnaire</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Questionnaire.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="Questionnaire.identifier", description="An identifier for the questionnaire", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>An identifier for the questionnaire</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Questionnaire.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>code</b>
   * <p>
   * Description: <b>A code that corresponds to the questionnaire or one of its groups</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Questionnaire.item.code</b><br>
   * </p>
   */
  @SearchParamDefinition(name="code", path="Questionnaire.item.code", description="A code that corresponds to the questionnaire or one of its groups", type="token" )
  public static final String SP_CODE = "code";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>code</b>
   * <p>
   * Description: <b>A code that corresponds to the questionnaire or one of its groups</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Questionnaire.item.code</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CODE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CODE);

 /**
   * Search parameter: <b>context</b>
   * <p>
   * Description: <b>A use context assigned to the questionnaire</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Questionnaire.useContext</b><br>
   * </p>
   */
  @SearchParamDefinition(name="context", path="Questionnaire.useContext", description="A use context assigned to the questionnaire", type="token" )
  public static final String SP_CONTEXT = "context";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>context</b>
   * <p>
   * Description: <b>A use context assigned to the questionnaire</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Questionnaire.useContext</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CONTEXT = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CONTEXT);

 /**
   * Search parameter: <b>publisher</b>
   * <p>
   * Description: <b>The author of the questionnaire</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Questionnaire.publisher</b><br>
   * </p>
   */
  @SearchParamDefinition(name="publisher", path="Questionnaire.publisher", description="The author of the questionnaire", type="string" )
  public static final String SP_PUBLISHER = "publisher";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>publisher</b>
   * <p>
   * Description: <b>The author of the questionnaire</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Questionnaire.publisher</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam PUBLISHER = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_PUBLISHER);

 /**
   * Search parameter: <b>title</b>
   * <p>
   * Description: <b>All or part of the name of the questionnaire</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Questionnaire.title</b><br>
   * </p>
   */
  @SearchParamDefinition(name="title", path="Questionnaire.title", description="All or part of the name of the questionnaire", type="string" )
  public static final String SP_TITLE = "title";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>title</b>
   * <p>
   * Description: <b>All or part of the name of the questionnaire</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Questionnaire.title</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam TITLE = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_TITLE);

 /**
   * Search parameter: <b>version</b>
   * <p>
   * Description: <b>The business version of the questionnaire</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Questionnaire.version</b><br>
   * </p>
   */
  @SearchParamDefinition(name="version", path="Questionnaire.version", description="The business version of the questionnaire", type="string" )
  public static final String SP_VERSION = "version";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>version</b>
   * <p>
   * Description: <b>The business version of the questionnaire</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Questionnaire.version</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam VERSION = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_VERSION);

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>The status of the questionnaire</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Questionnaire.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="Questionnaire.status", description="The status of the questionnaire", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>The status of the questionnaire</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Questionnaire.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);


}

