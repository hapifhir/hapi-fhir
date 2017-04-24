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

// Generated on Mon, Apr 17, 2017 17:38-0400 for FHIR v3.0.1

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
 * A structured set of questions intended to guide the collection of answers from end-users. Questionnaires provide detailed control over order, presentation, phraseology and grouping to allow coherent, consistent data collection.
 */
@ResourceDef(name="Questionnaire", profile="http://hl7.org/fhir/Profile/Questionnaire")
@ChildOrder(names={"url", "identifier", "version", "name", "title", "status", "experimental", "date", "publisher", "description", "purpose", "approvalDate", "lastReviewDate", "effectivePeriod", "useContext", "jurisdiction", "contact", "copyright", "code", "subjectType", "item"})
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
         * A terminology code that corresponds to this group or question (e.g. a code from LOINC, which defines many questions and answers).
         */
        @Child(name = "code", type = {Coding.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
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
        @Child(name = "text", type = {StringType.class}, order=5, min=0, max=1, modifier=false, summary=true)
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
        @Child(name = "enableWhen", type = {}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=true, summary=true)
        @Description(shortDefinition="Only allow data when", formalDefinition="A constraint indicating that this item should only be enabled (displayed/allow answers to be captured) when the specified condition is true." )
        protected List<QuestionnaireItemEnableWhenComponent> enableWhen;

        /**
         * An indication, if true, that the item must be present in a "completed" QuestionnaireResponse.  If false, the item may be skipped when answering the questionnaire.
         */
        @Child(name = "required", type = {BooleanType.class}, order=8, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Whether the item must be included in data results", formalDefinition="An indication, if true, that the item must be present in a \"completed\" QuestionnaireResponse.  If false, the item may be skipped when answering the questionnaire." )
        protected BooleanType required;

        /**
         * An indication, if true, that the item may occur multiple times in the response, collecting multiple answers answers for questions or multiple sets of answers for groups.
         */
        @Child(name = "repeats", type = {BooleanType.class}, order=9, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Whether the item may repeat", formalDefinition="An indication, if true, that the item may occur multiple times in the response, collecting multiple answers answers for questions or multiple sets of answers for groups." )
        protected BooleanType repeats;

        /**
         * An indication, when true, that the value cannot be changed by a human respondent to the Questionnaire.
         */
        @Child(name = "readOnly", type = {BooleanType.class}, order=10, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Don't allow human editing", formalDefinition="An indication, when true, that the value cannot be changed by a human respondent to the Questionnaire." )
        protected BooleanType readOnly;

        /**
         * The maximum number of characters that are permitted in the answer to be considered a "valid" QuestionnaireResponse.
         */
        @Child(name = "maxLength", type = {IntegerType.class}, order=11, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="No more than this many characters", formalDefinition="The maximum number of characters that are permitted in the answer to be considered a \"valid\" QuestionnaireResponse." )
        protected IntegerType maxLength;

        /**
         * A reference to a value set containing a list of codes representing permitted answers for a "choice" or "open-choice" question.
         */
        @Child(name = "options", type = {ValueSet.class}, order=12, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Valueset containing permitted answers", formalDefinition="A reference to a value set containing a list of codes representing permitted answers for a \"choice\" or \"open-choice\" question." )
        protected Reference options;

        /**
         * The actual object that is the target of the reference (A reference to a value set containing a list of codes representing permitted answers for a "choice" or "open-choice" question.)
         */
        protected ValueSet optionsTarget;

        /**
         * One of the permitted answers for a "choice" or "open-choice" question.
         */
        @Child(name = "option", type = {}, order=13, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Permitted answer", formalDefinition="One of the permitted answers for a \"choice\" or \"open-choice\" question." )
        protected List<QuestionnaireItemOptionComponent> option;

        /**
         * The value that should be defaulted when initially rendering the questionnaire for user input.
         */
        @Child(name = "initial", type = {BooleanType.class, DecimalType.class, IntegerType.class, DateType.class, DateTimeType.class, TimeType.class, StringType.class, UriType.class, Attachment.class, Coding.class, Quantity.class, Reference.class}, order=14, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Default value when item is first rendered", formalDefinition="The value that should be defaulted when initially rendering the questionnaire for user input." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/questionnaire-answers")
        protected Type initial;

        /**
         * Text, questions and other groups to be nested beneath a question or group.
         */
        @Child(name = "item", type = {QuestionnaireItemComponent.class}, order=15, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Nested questionnaire items", formalDefinition="Text, questions and other groups to be nested beneath a question or group." )
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
         * @return {@link #repeats} (An indication, if true, that the item may occur multiple times in the response, collecting multiple answers answers for questions or multiple sets of answers for groups.). This is the underlying object with id, value and extensions. The accessor "getRepeats" gives direct access to the value
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
         * @param value {@link #repeats} (An indication, if true, that the item may occur multiple times in the response, collecting multiple answers answers for questions or multiple sets of answers for groups.). This is the underlying object with id, value and extensions. The accessor "getRepeats" gives direct access to the value
         */
        public QuestionnaireItemComponent setRepeatsElement(BooleanType value) { 
          this.repeats = value;
          return this;
        }

        /**
         * @return An indication, if true, that the item may occur multiple times in the response, collecting multiple answers answers for questions or multiple sets of answers for groups.
         */
        public boolean getRepeats() { 
          return this.repeats == null || this.repeats.isEmpty() ? false : this.repeats.getValue();
        }

        /**
         * @param value An indication, if true, that the item may occur multiple times in the response, collecting multiple answers answers for questions or multiple sets of answers for groups.
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
         * @return {@link #options} (A reference to a value set containing a list of codes representing permitted answers for a "choice" or "open-choice" question.)
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
         * @param value {@link #options} (A reference to a value set containing a list of codes representing permitted answers for a "choice" or "open-choice" question.)
         */
        public QuestionnaireItemComponent setOptions(Reference value) { 
          this.options = value;
          return this;
        }

        /**
         * @return {@link #options} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (A reference to a value set containing a list of codes representing permitted answers for a "choice" or "open-choice" question.)
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
         * @param value {@link #options} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (A reference to a value set containing a list of codes representing permitted answers for a "choice" or "open-choice" question.)
         */
        public QuestionnaireItemComponent setOptionsTarget(ValueSet value) { 
          this.optionsTarget = value;
          return this;
        }

        /**
         * @return {@link #option} (One of the permitted answers for a "choice" or "open-choice" question.)
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
         * @return {@link #initial} (The value that should be defaulted when initially rendering the questionnaire for user input.)
         */
        public Type getInitial() { 
          return this.initial;
        }

        /**
         * @return {@link #initial} (The value that should be defaulted when initially rendering the questionnaire for user input.)
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
         * @return {@link #initial} (The value that should be defaulted when initially rendering the questionnaire for user input.)
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
         * @return {@link #initial} (The value that should be defaulted when initially rendering the questionnaire for user input.)
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
         * @return {@link #initial} (The value that should be defaulted when initially rendering the questionnaire for user input.)
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
         * @return {@link #initial} (The value that should be defaulted when initially rendering the questionnaire for user input.)
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
         * @return {@link #initial} (The value that should be defaulted when initially rendering the questionnaire for user input.)
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
         * @return {@link #initial} (The value that should be defaulted when initially rendering the questionnaire for user input.)
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
         * @return {@link #initial} (The value that should be defaulted when initially rendering the questionnaire for user input.)
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
         * @return {@link #initial} (The value that should be defaulted when initially rendering the questionnaire for user input.)
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
         * @return {@link #initial} (The value that should be defaulted when initially rendering the questionnaire for user input.)
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
         * @return {@link #initial} (The value that should be defaulted when initially rendering the questionnaire for user input.)
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
         * @return {@link #initial} (The value that should be defaulted when initially rendering the questionnaire for user input.)
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
         * @param value {@link #initial} (The value that should be defaulted when initially rendering the questionnaire for user input.)
         */
        public QuestionnaireItemComponent setInitial(Type value) { 
          this.initial = value;
          return this;
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

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("linkId", "string", "An identifier that is unique within the Questionnaire allowing linkage to the equivalent item in a QuestionnaireResponse resource.", 0, java.lang.Integer.MAX_VALUE, linkId));
          childrenList.add(new Property("definition", "uri", "A reference to an [[[ElementDefinition]]] that provides the details for the item. If a definition is provided, then the following element values can be inferred from the definition: \n\n* code (ElementDefinition.code)\n* type (ElementDefinition.type)\n* required (ElementDefinition.min)\n* repeats (ElementDefinition.max)\n* maxLength (ElementDefinition.maxLength)\n* options (ElementDefinition.binding)\n\nAny information provided in these elements on a Questionnaire Item overrides the information from the definition.", 0, java.lang.Integer.MAX_VALUE, definition));
          childrenList.add(new Property("code", "Coding", "A terminology code that corresponds to this group or question (e.g. a code from LOINC, which defines many questions and answers).", 0, java.lang.Integer.MAX_VALUE, code));
          childrenList.add(new Property("prefix", "string", "A short label for a particular group, question or set of display text within the questionnaire used for reference by the individual completing the questionnaire.", 0, java.lang.Integer.MAX_VALUE, prefix));
          childrenList.add(new Property("text", "string", "The name of a section, the text of a question or text content for a display item.", 0, java.lang.Integer.MAX_VALUE, text));
          childrenList.add(new Property("type", "code", "The type of questionnaire item this is - whether text for display, a grouping of other items or a particular type of data to be captured (string, integer, coded choice, etc.).", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("enableWhen", "", "A constraint indicating that this item should only be enabled (displayed/allow answers to be captured) when the specified condition is true.", 0, java.lang.Integer.MAX_VALUE, enableWhen));
          childrenList.add(new Property("required", "boolean", "An indication, if true, that the item must be present in a \"completed\" QuestionnaireResponse.  If false, the item may be skipped when answering the questionnaire.", 0, java.lang.Integer.MAX_VALUE, required));
          childrenList.add(new Property("repeats", "boolean", "An indication, if true, that the item may occur multiple times in the response, collecting multiple answers answers for questions or multiple sets of answers for groups.", 0, java.lang.Integer.MAX_VALUE, repeats));
          childrenList.add(new Property("readOnly", "boolean", "An indication, when true, that the value cannot be changed by a human respondent to the Questionnaire.", 0, java.lang.Integer.MAX_VALUE, readOnly));
          childrenList.add(new Property("maxLength", "integer", "The maximum number of characters that are permitted in the answer to be considered a \"valid\" QuestionnaireResponse.", 0, java.lang.Integer.MAX_VALUE, maxLength));
          childrenList.add(new Property("options", "Reference(ValueSet)", "A reference to a value set containing a list of codes representing permitted answers for a \"choice\" or \"open-choice\" question.", 0, java.lang.Integer.MAX_VALUE, options));
          childrenList.add(new Property("option", "", "One of the permitted answers for a \"choice\" or \"open-choice\" question.", 0, java.lang.Integer.MAX_VALUE, option));
          childrenList.add(new Property("initial[x]", "boolean|decimal|integer|date|dateTime|time|string|uri|Attachment|Coding|Quantity|Reference(Any)", "The value that should be defaulted when initially rendering the questionnaire for user input.", 0, java.lang.Integer.MAX_VALUE, initial));
          childrenList.add(new Property("item", "@Questionnaire.item", "Text, questions and other groups to be nested beneath a question or group.", 0, java.lang.Integer.MAX_VALUE, item));
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
         * An indication that this item should be enabled only if the specified question is answered (hasAnswer=true) or not answered (hasAnswer=false).
         */
        @Child(name = "hasAnswer", type = {BooleanType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Enable when answered or not", formalDefinition="An indication that this item should be enabled only if the specified question is answered (hasAnswer=true) or not answered (hasAnswer=false)." )
        protected BooleanType hasAnswer;

        /**
         * An answer that the referenced question must match in order for the item to be enabled.
         */
        @Child(name = "answer", type = {BooleanType.class, DecimalType.class, IntegerType.class, DateType.class, DateTimeType.class, TimeType.class, StringType.class, UriType.class, Attachment.class, Coding.class, Quantity.class, Reference.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Value question must have", formalDefinition="An answer that the referenced question must match in order for the item to be enabled." )
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
         * @return {@link #hasAnswer} (An indication that this item should be enabled only if the specified question is answered (hasAnswer=true) or not answered (hasAnswer=false).). This is the underlying object with id, value and extensions. The accessor "getHasAnswer" gives direct access to the value
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
         * @param value {@link #hasAnswer} (An indication that this item should be enabled only if the specified question is answered (hasAnswer=true) or not answered (hasAnswer=false).). This is the underlying object with id, value and extensions. The accessor "getHasAnswer" gives direct access to the value
         */
        public QuestionnaireItemEnableWhenComponent setHasAnswerElement(BooleanType value) { 
          this.hasAnswer = value;
          return this;
        }

        /**
         * @return An indication that this item should be enabled only if the specified question is answered (hasAnswer=true) or not answered (hasAnswer=false).
         */
        public boolean getHasAnswer() { 
          return this.hasAnswer == null || this.hasAnswer.isEmpty() ? false : this.hasAnswer.getValue();
        }

        /**
         * @param value An indication that this item should be enabled only if the specified question is answered (hasAnswer=true) or not answered (hasAnswer=false).
         */
        public QuestionnaireItemEnableWhenComponent setHasAnswer(boolean value) { 
            if (this.hasAnswer == null)
              this.hasAnswer = new BooleanType();
            this.hasAnswer.setValue(value);
          return this;
        }

        /**
         * @return {@link #answer} (An answer that the referenced question must match in order for the item to be enabled.)
         */
        public Type getAnswer() { 
          return this.answer;
        }

        /**
         * @return {@link #answer} (An answer that the referenced question must match in order for the item to be enabled.)
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
         * @return {@link #answer} (An answer that the referenced question must match in order for the item to be enabled.)
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
         * @return {@link #answer} (An answer that the referenced question must match in order for the item to be enabled.)
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
         * @return {@link #answer} (An answer that the referenced question must match in order for the item to be enabled.)
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
         * @return {@link #answer} (An answer that the referenced question must match in order for the item to be enabled.)
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
         * @return {@link #answer} (An answer that the referenced question must match in order for the item to be enabled.)
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
         * @return {@link #answer} (An answer that the referenced question must match in order for the item to be enabled.)
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
         * @return {@link #answer} (An answer that the referenced question must match in order for the item to be enabled.)
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
         * @return {@link #answer} (An answer that the referenced question must match in order for the item to be enabled.)
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
         * @return {@link #answer} (An answer that the referenced question must match in order for the item to be enabled.)
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
         * @return {@link #answer} (An answer that the referenced question must match in order for the item to be enabled.)
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
         * @return {@link #answer} (An answer that the referenced question must match in order for the item to be enabled.)
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
         * @param value {@link #answer} (An answer that the referenced question must match in order for the item to be enabled.)
         */
        public QuestionnaireItemEnableWhenComponent setAnswer(Type value) { 
          this.answer = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("question", "string", "The linkId for the question whose answer (or lack of answer) governs whether this item is enabled.", 0, java.lang.Integer.MAX_VALUE, question));
          childrenList.add(new Property("hasAnswer", "boolean", "An indication that this item should be enabled only if the specified question is answered (hasAnswer=true) or not answered (hasAnswer=false).", 0, java.lang.Integer.MAX_VALUE, hasAnswer));
          childrenList.add(new Property("answer[x]", "boolean|decimal|integer|date|dateTime|time|string|uri|Attachment|Coding|Quantity|Reference(Any)", "An answer that the referenced question must match in order for the item to be enabled.", 0, java.lang.Integer.MAX_VALUE, answer));
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
         * A potential answer that's allowed as the answer to this question.
         */
        @Child(name = "value", type = {IntegerType.class, DateType.class, TimeType.class, StringType.class, Coding.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Answer value", formalDefinition="A potential answer that's allowed as the answer to this question." )
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
         * @return {@link #value} (A potential answer that's allowed as the answer to this question.)
         */
        public Type getValue() { 
          return this.value;
        }

        /**
         * @return {@link #value} (A potential answer that's allowed as the answer to this question.)
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
         * @return {@link #value} (A potential answer that's allowed as the answer to this question.)
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
         * @return {@link #value} (A potential answer that's allowed as the answer to this question.)
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
         * @return {@link #value} (A potential answer that's allowed as the answer to this question.)
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
         * @return {@link #value} (A potential answer that's allowed as the answer to this question.)
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
         * @param value {@link #value} (A potential answer that's allowed as the answer to this question.)
         */
        public QuestionnaireItemOptionComponent setValue(Type value) { 
          this.value = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("value[x]", "integer|date|time|string|Coding", "A potential answer that's allowed as the answer to this question.", 0, java.lang.Integer.MAX_VALUE, value));
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
     * A formal identifier that is used to identify this questionnaire when it is represented in other formats, or referenced in a specification, model, design or an instance.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Additional identifier for the questionnaire", formalDefinition="A formal identifier that is used to identify this questionnaire when it is represented in other formats, or referenced in a specification, model, design or an instance." )
    protected List<Identifier> identifier;

    /**
     * Explaination of why this questionnaire is needed and why it has been designed as it has.
     */
    @Child(name = "purpose", type = {MarkdownType.class}, order=1, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Why this questionnaire is defined", formalDefinition="Explaination of why this questionnaire is needed and why it has been designed as it has." )
    protected MarkdownType purpose;

    /**
     * The date on which the resource content was approved by the publisher. Approval happens once when the content is officially approved for usage.
     */
    @Child(name = "approvalDate", type = {DateType.class}, order=2, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="When the questionnaire was approved by publisher", formalDefinition="The date on which the resource content was approved by the publisher. Approval happens once when the content is officially approved for usage." )
    protected DateType approvalDate;

    /**
     * The date on which the resource content was last reviewed. Review happens periodically after approval, but doesn't change the original approval date.
     */
    @Child(name = "lastReviewDate", type = {DateType.class}, order=3, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="When the questionnaire was last reviewed", formalDefinition="The date on which the resource content was last reviewed. Review happens periodically after approval, but doesn't change the original approval date." )
    protected DateType lastReviewDate;

    /**
     * The period during which the questionnaire content was or is planned to be in active use.
     */
    @Child(name = "effectivePeriod", type = {Period.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When the questionnaire is expected to be used", formalDefinition="The period during which the questionnaire content was or is planned to be in active use." )
    protected Period effectivePeriod;

    /**
     * A copyright statement relating to the questionnaire and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the questionnaire.
     */
    @Child(name = "copyright", type = {MarkdownType.class}, order=5, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Use and/or publishing restrictions", formalDefinition="A copyright statement relating to the questionnaire and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the questionnaire." )
    protected MarkdownType copyright;

    /**
     * An identifier for this question or group of questions in a particular terminology such as LOINC.
     */
    @Child(name = "code", type = {Coding.class}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Concept that represents the overall questionnaire", formalDefinition="An identifier for this question or group of questions in a particular terminology such as LOINC." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/questionnaire-questions")
    protected List<Coding> code;

    /**
     * The types of subjects that can be the subject of responses created for the questionnaire.
     */
    @Child(name = "subjectType", type = {CodeType.class}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Resource that can be subject of QuestionnaireResponse", formalDefinition="The types of subjects that can be the subject of responses created for the questionnaire." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/resource-types")
    protected List<CodeType> subjectType;

    /**
     * A particular question, question grouping or display text that is part of the questionnaire.
     */
    @Child(name = "item", type = {}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Questions and sections within the Questionnaire", formalDefinition="A particular question, question grouping or display text that is part of the questionnaire." )
    protected List<QuestionnaireItemComponent> item;

    private static final long serialVersionUID = -1846925043L;

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
     * @return {@link #url} (An absolute URI that is used to identify this questionnaire when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this questionnaire is (or will be) published. The URL SHOULD include the major version of the questionnaire. For more information see [Technical and Business Versions](resource.html#versions).). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
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
     * @param value {@link #url} (An absolute URI that is used to identify this questionnaire when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this questionnaire is (or will be) published. The URL SHOULD include the major version of the questionnaire. For more information see [Technical and Business Versions](resource.html#versions).). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public Questionnaire setUrlElement(UriType value) { 
      this.url = value;
      return this;
    }

    /**
     * @return An absolute URI that is used to identify this questionnaire when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this questionnaire is (or will be) published. The URL SHOULD include the major version of the questionnaire. For more information see [Technical and Business Versions](resource.html#versions).
     */
    public String getUrl() { 
      return this.url == null ? null : this.url.getValue();
    }

    /**
     * @param value An absolute URI that is used to identify this questionnaire when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this questionnaire is (or will be) published. The URL SHOULD include the major version of the questionnaire. For more information see [Technical and Business Versions](resource.html#versions).
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
     * @return {@link #experimental} (A boolean value to indicate that this questionnaire is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.). This is the underlying object with id, value and extensions. The accessor "getExperimental" gives direct access to the value
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
     * @param value {@link #experimental} (A boolean value to indicate that this questionnaire is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.). This is the underlying object with id, value and extensions. The accessor "getExperimental" gives direct access to the value
     */
    public Questionnaire setExperimentalElement(BooleanType value) { 
      this.experimental = value;
      return this;
    }

    /**
     * @return A boolean value to indicate that this questionnaire is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.
     */
    public boolean getExperimental() { 
      return this.experimental == null || this.experimental.isEmpty() ? false : this.experimental.getValue();
    }

    /**
     * @param value A boolean value to indicate that this questionnaire is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.
     */
    public Questionnaire setExperimental(boolean value) { 
        if (this.experimental == null)
          this.experimental = new BooleanType();
        this.experimental.setValue(value);
      return this;
    }

    /**
     * @return {@link #date} (The date  (and optionally time) when the questionnaire was published. The date must change if and when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the questionnaire changes.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
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
     * @param value {@link #date} (The date  (and optionally time) when the questionnaire was published. The date must change if and when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the questionnaire changes.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public Questionnaire setDateElement(DateTimeType value) { 
      this.date = value;
      return this;
    }

    /**
     * @return The date  (and optionally time) when the questionnaire was published. The date must change if and when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the questionnaire changes.
     */
    public Date getDate() { 
      return this.date == null ? null : this.date.getValue();
    }

    /**
     * @param value The date  (and optionally time) when the questionnaire was published. The date must change if and when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the questionnaire changes.
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
     * @return {@link #publisher} (The name of the individual or organization that published the questionnaire.). This is the underlying object with id, value and extensions. The accessor "getPublisher" gives direct access to the value
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
     * @param value {@link #publisher} (The name of the individual or organization that published the questionnaire.). This is the underlying object with id, value and extensions. The accessor "getPublisher" gives direct access to the value
     */
    public Questionnaire setPublisherElement(StringType value) { 
      this.publisher = value;
      return this;
    }

    /**
     * @return The name of the individual or organization that published the questionnaire.
     */
    public String getPublisher() { 
      return this.publisher == null ? null : this.publisher.getValue();
    }

    /**
     * @param value The name of the individual or organization that published the questionnaire.
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
     * @return {@link #purpose} (Explaination of why this questionnaire is needed and why it has been designed as it has.). This is the underlying object with id, value and extensions. The accessor "getPurpose" gives direct access to the value
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
     * @param value {@link #purpose} (Explaination of why this questionnaire is needed and why it has been designed as it has.). This is the underlying object with id, value and extensions. The accessor "getPurpose" gives direct access to the value
     */
    public Questionnaire setPurposeElement(MarkdownType value) { 
      this.purpose = value;
      return this;
    }

    /**
     * @return Explaination of why this questionnaire is needed and why it has been designed as it has.
     */
    public String getPurpose() { 
      return this.purpose == null ? null : this.purpose.getValue();
    }

    /**
     * @param value Explaination of why this questionnaire is needed and why it has been designed as it has.
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
     * @return {@link #lastReviewDate} (The date on which the resource content was last reviewed. Review happens periodically after approval, but doesn't change the original approval date.). This is the underlying object with id, value and extensions. The accessor "getLastReviewDate" gives direct access to the value
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
     * @param value {@link #lastReviewDate} (The date on which the resource content was last reviewed. Review happens periodically after approval, but doesn't change the original approval date.). This is the underlying object with id, value and extensions. The accessor "getLastReviewDate" gives direct access to the value
     */
    public Questionnaire setLastReviewDateElement(DateType value) { 
      this.lastReviewDate = value;
      return this;
    }

    /**
     * @return The date on which the resource content was last reviewed. Review happens periodically after approval, but doesn't change the original approval date.
     */
    public Date getLastReviewDate() { 
      return this.lastReviewDate == null ? null : this.lastReviewDate.getValue();
    }

    /**
     * @param value The date on which the resource content was last reviewed. Review happens periodically after approval, but doesn't change the original approval date.
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
     * @return {@link #useContext} (The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching for appropriate questionnaire instances.)
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
        if (v.equals(value)) // code
          return true;
      return false;
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

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("url", "uri", "An absolute URI that is used to identify this questionnaire when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this questionnaire is (or will be) published. The URL SHOULD include the major version of the questionnaire. For more information see [Technical and Business Versions](resource.html#versions).", 0, java.lang.Integer.MAX_VALUE, url));
        childrenList.add(new Property("identifier", "Identifier", "A formal identifier that is used to identify this questionnaire when it is represented in other formats, or referenced in a specification, model, design or an instance.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("version", "string", "The identifier that is used to identify this version of the questionnaire when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the questionnaire author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence.", 0, java.lang.Integer.MAX_VALUE, version));
        childrenList.add(new Property("name", "string", "A natural language name identifying the questionnaire. This name should be usable as an identifier for the module by machine processing applications such as code generation.", 0, java.lang.Integer.MAX_VALUE, name));
        childrenList.add(new Property("title", "string", "A short, descriptive, user-friendly title for the questionnaire.", 0, java.lang.Integer.MAX_VALUE, title));
        childrenList.add(new Property("status", "code", "The status of this questionnaire. Enables tracking the life-cycle of the content.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("experimental", "boolean", "A boolean value to indicate that this questionnaire is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.", 0, java.lang.Integer.MAX_VALUE, experimental));
        childrenList.add(new Property("date", "dateTime", "The date  (and optionally time) when the questionnaire was published. The date must change if and when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the questionnaire changes.", 0, java.lang.Integer.MAX_VALUE, date));
        childrenList.add(new Property("publisher", "string", "The name of the individual or organization that published the questionnaire.", 0, java.lang.Integer.MAX_VALUE, publisher));
        childrenList.add(new Property("description", "markdown", "A free text natural language description of the questionnaire from a consumer's perspective.", 0, java.lang.Integer.MAX_VALUE, description));
        childrenList.add(new Property("purpose", "markdown", "Explaination of why this questionnaire is needed and why it has been designed as it has.", 0, java.lang.Integer.MAX_VALUE, purpose));
        childrenList.add(new Property("approvalDate", "date", "The date on which the resource content was approved by the publisher. Approval happens once when the content is officially approved for usage.", 0, java.lang.Integer.MAX_VALUE, approvalDate));
        childrenList.add(new Property("lastReviewDate", "date", "The date on which the resource content was last reviewed. Review happens periodically after approval, but doesn't change the original approval date.", 0, java.lang.Integer.MAX_VALUE, lastReviewDate));
        childrenList.add(new Property("effectivePeriod", "Period", "The period during which the questionnaire content was or is planned to be in active use.", 0, java.lang.Integer.MAX_VALUE, effectivePeriod));
        childrenList.add(new Property("useContext", "UsageContext", "The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching for appropriate questionnaire instances.", 0, java.lang.Integer.MAX_VALUE, useContext));
        childrenList.add(new Property("jurisdiction", "CodeableConcept", "A legal or geographic region in which the questionnaire is intended to be used.", 0, java.lang.Integer.MAX_VALUE, jurisdiction));
        childrenList.add(new Property("contact", "ContactDetail", "Contact details to assist a user in finding and communicating with the publisher.", 0, java.lang.Integer.MAX_VALUE, contact));
        childrenList.add(new Property("copyright", "markdown", "A copyright statement relating to the questionnaire and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the questionnaire.", 0, java.lang.Integer.MAX_VALUE, copyright));
        childrenList.add(new Property("code", "Coding", "An identifier for this question or group of questions in a particular terminology such as LOINC.", 0, java.lang.Integer.MAX_VALUE, code));
        childrenList.add(new Property("subjectType", "code", "The types of subjects that can be the subject of responses created for the questionnaire.", 0, java.lang.Integer.MAX_VALUE, subjectType));
        childrenList.add(new Property("item", "", "A particular question, question grouping or display text that is part of the questionnaire.", 0, java.lang.Integer.MAX_VALUE, item));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 116079: /*url*/ return this.url == null ? new Base[0] : new Base[] {this.url}; // UriType
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case 351608024: /*version*/ return this.version == null ? new Base[0] : new Base[] {this.version}; // StringType
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case 110371416: /*title*/ return this.title == null ? new Base[0] : new Base[] {this.title}; // StringType
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<PublicationStatus>
        case -404562712: /*experimental*/ return this.experimental == null ? new Base[0] : new Base[] {this.experimental}; // BooleanType
        case 3076014: /*date*/ return this.date == null ? new Base[0] : new Base[] {this.date}; // DateTimeType
        case 1447404028: /*publisher*/ return this.publisher == null ? new Base[0] : new Base[] {this.publisher}; // StringType
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // MarkdownType
        case -220463842: /*purpose*/ return this.purpose == null ? new Base[0] : new Base[] {this.purpose}; // MarkdownType
        case 223539345: /*approvalDate*/ return this.approvalDate == null ? new Base[0] : new Base[] {this.approvalDate}; // DateType
        case -1687512484: /*lastReviewDate*/ return this.lastReviewDate == null ? new Base[0] : new Base[] {this.lastReviewDate}; // DateType
        case -403934648: /*effectivePeriod*/ return this.effectivePeriod == null ? new Base[0] : new Base[] {this.effectivePeriod}; // Period
        case -669707736: /*useContext*/ return this.useContext == null ? new Base[0] : this.useContext.toArray(new Base[this.useContext.size()]); // UsageContext
        case -507075711: /*jurisdiction*/ return this.jurisdiction == null ? new Base[0] : this.jurisdiction.toArray(new Base[this.jurisdiction.size()]); // CodeableConcept
        case 951526432: /*contact*/ return this.contact == null ? new Base[0] : this.contact.toArray(new Base[this.contact.size()]); // ContactDetail
        case 1522889671: /*copyright*/ return this.copyright == null ? new Base[0] : new Base[] {this.copyright}; // MarkdownType
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
        case 3373707: // name
          this.name = castToString(value); // StringType
          return value;
        case 110371416: // title
          this.title = castToString(value); // StringType
          return value;
        case -892481550: // status
          value = new PublicationStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<PublicationStatus>
          return value;
        case -404562712: // experimental
          this.experimental = castToBoolean(value); // BooleanType
          return value;
        case 3076014: // date
          this.date = castToDateTime(value); // DateTimeType
          return value;
        case 1447404028: // publisher
          this.publisher = castToString(value); // StringType
          return value;
        case -1724546052: // description
          this.description = castToMarkdown(value); // MarkdownType
          return value;
        case -220463842: // purpose
          this.purpose = castToMarkdown(value); // MarkdownType
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
        case -669707736: // useContext
          this.getUseContext().add(castToUsageContext(value)); // UsageContext
          return value;
        case -507075711: // jurisdiction
          this.getJurisdiction().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 951526432: // contact
          this.getContact().add(castToContactDetail(value)); // ContactDetail
          return value;
        case 1522889671: // copyright
          this.copyright = castToMarkdown(value); // MarkdownType
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
        } else if (name.equals("name")) {
          this.name = castToString(value); // StringType
        } else if (name.equals("title")) {
          this.title = castToString(value); // StringType
        } else if (name.equals("status")) {
          value = new PublicationStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<PublicationStatus>
        } else if (name.equals("experimental")) {
          this.experimental = castToBoolean(value); // BooleanType
        } else if (name.equals("date")) {
          this.date = castToDateTime(value); // DateTimeType
        } else if (name.equals("publisher")) {
          this.publisher = castToString(value); // StringType
        } else if (name.equals("description")) {
          this.description = castToMarkdown(value); // MarkdownType
        } else if (name.equals("purpose")) {
          this.purpose = castToMarkdown(value); // MarkdownType
        } else if (name.equals("approvalDate")) {
          this.approvalDate = castToDate(value); // DateType
        } else if (name.equals("lastReviewDate")) {
          this.lastReviewDate = castToDate(value); // DateType
        } else if (name.equals("effectivePeriod")) {
          this.effectivePeriod = castToPeriod(value); // Period
        } else if (name.equals("useContext")) {
          this.getUseContext().add(castToUsageContext(value));
        } else if (name.equals("jurisdiction")) {
          this.getJurisdiction().add(castToCodeableConcept(value));
        } else if (name.equals("contact")) {
          this.getContact().add(castToContactDetail(value));
        } else if (name.equals("copyright")) {
          this.copyright = castToMarkdown(value); // MarkdownType
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
        case 3373707:  return getNameElement();
        case 110371416:  return getTitleElement();
        case -892481550:  return getStatusElement();
        case -404562712:  return getExperimentalElement();
        case 3076014:  return getDateElement();
        case 1447404028:  return getPublisherElement();
        case -1724546052:  return getDescriptionElement();
        case -220463842:  return getPurposeElement();
        case 223539345:  return getApprovalDateElement();
        case -1687512484:  return getLastReviewDateElement();
        case -403934648:  return getEffectivePeriod(); 
        case -669707736:  return addUseContext(); 
        case -507075711:  return addJurisdiction(); 
        case 951526432:  return addContact(); 
        case 1522889671:  return getCopyrightElement();
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
        case 3373707: /*name*/ return new String[] {"string"};
        case 110371416: /*title*/ return new String[] {"string"};
        case -892481550: /*status*/ return new String[] {"code"};
        case -404562712: /*experimental*/ return new String[] {"boolean"};
        case 3076014: /*date*/ return new String[] {"dateTime"};
        case 1447404028: /*publisher*/ return new String[] {"string"};
        case -1724546052: /*description*/ return new String[] {"markdown"};
        case -220463842: /*purpose*/ return new String[] {"markdown"};
        case 223539345: /*approvalDate*/ return new String[] {"date"};
        case -1687512484: /*lastReviewDate*/ return new String[] {"date"};
        case -403934648: /*effectivePeriod*/ return new String[] {"Period"};
        case -669707736: /*useContext*/ return new String[] {"UsageContext"};
        case -507075711: /*jurisdiction*/ return new String[] {"CodeableConcept"};
        case 951526432: /*contact*/ return new String[] {"ContactDetail"};
        case 1522889671: /*copyright*/ return new String[] {"markdown"};
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
        else if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type Questionnaire.name");
        }
        else if (name.equals("title")) {
          throw new FHIRException("Cannot call addChild on a primitive type Questionnaire.title");
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type Questionnaire.status");
        }
        else if (name.equals("experimental")) {
          throw new FHIRException("Cannot call addChild on a primitive type Questionnaire.experimental");
        }
        else if (name.equals("date")) {
          throw new FHIRException("Cannot call addChild on a primitive type Questionnaire.date");
        }
        else if (name.equals("publisher")) {
          throw new FHIRException("Cannot call addChild on a primitive type Questionnaire.publisher");
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type Questionnaire.description");
        }
        else if (name.equals("purpose")) {
          throw new FHIRException("Cannot call addChild on a primitive type Questionnaire.purpose");
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
        else if (name.equals("useContext")) {
          return addUseContext();
        }
        else if (name.equals("jurisdiction")) {
          return addJurisdiction();
        }
        else if (name.equals("contact")) {
          return addContact();
        }
        else if (name.equals("copyright")) {
          throw new FHIRException("Cannot call addChild on a primitive type Questionnaire.copyright");
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
        dst.name = name == null ? null : name.copy();
        dst.title = title == null ? null : title.copy();
        dst.status = status == null ? null : status.copy();
        dst.experimental = experimental == null ? null : experimental.copy();
        dst.date = date == null ? null : date.copy();
        dst.publisher = publisher == null ? null : publisher.copy();
        dst.description = description == null ? null : description.copy();
        dst.purpose = purpose == null ? null : purpose.copy();
        dst.approvalDate = approvalDate == null ? null : approvalDate.copy();
        dst.lastReviewDate = lastReviewDate == null ? null : lastReviewDate.copy();
        dst.effectivePeriod = effectivePeriod == null ? null : effectivePeriod.copy();
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
        if (contact != null) {
          dst.contact = new ArrayList<ContactDetail>();
          for (ContactDetail i : contact)
            dst.contact.add(i.copy());
        };
        dst.copyright = copyright == null ? null : copyright.copy();
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
        return compareDeep(identifier, o.identifier, true) && compareDeep(purpose, o.purpose, true) && compareDeep(approvalDate, o.approvalDate, true)
           && compareDeep(lastReviewDate, o.lastReviewDate, true) && compareDeep(effectivePeriod, o.effectivePeriod, true)
           && compareDeep(copyright, o.copyright, true) && compareDeep(code, o.code, true) && compareDeep(subjectType, o.subjectType, true)
           && compareDeep(item, o.item, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Questionnaire))
          return false;
        Questionnaire o = (Questionnaire) other;
        return compareValues(purpose, o.purpose, true) && compareValues(approvalDate, o.approvalDate, true)
           && compareValues(lastReviewDate, o.lastReviewDate, true) && compareValues(copyright, o.copyright, true)
           && compareValues(subjectType, o.subjectType, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, purpose, approvalDate
          , lastReviewDate, effectivePeriod, copyright, code, subjectType, item);
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

