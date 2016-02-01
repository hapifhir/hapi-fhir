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

// Generated on Sat, Jan 30, 2016 09:18-0500 for FHIR v1.3.0

import java.util.*;

import org.hl7.fhir.utilities.Utilities;

import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Block;

import org.hl7.fhir.dstu3.exceptions.FHIRException;
import org.hl7.fhir.dstu3.model.Enumerations.*;
import org.hl7.fhir.instance.model.api.*;
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
         * added to help the parsers
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
          if (code == null || code.isEmpty())
            return null;
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
         * Question with a system timestamp answer
         */
        INSTANT, 
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
         * added to help the parsers
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
        if ("instant".equals(codeString))
          return INSTANT;
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
            case INSTANT: return "instant";
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
            case INSTANT: return "http://hl7.org/fhir/item-type";
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
            case INSTANT: return "Question with a system timestamp answer";
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
            case INSTANT: return "Instant";
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
        if ("instant".equals(codeString))
          return QuestionnaireItemType.INSTANT;
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
          if (code == null || code.isEmpty())
            return null;
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
        if ("instant".equals(codeString))
          return new Enumeration<QuestionnaireItemType>(this, QuestionnaireItemType.INSTANT);
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
      if (code == QuestionnaireItemType.INSTANT)
        return "instant";
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
        @Child(name = "linkId", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="To link questionnaire with questionnaire response", formalDefinition="An identifier that is unique within the Questionnaire allowing linkage to the equivalent item in a QuestionnaireResponse resource." )
        protected StringType linkId;

        /**
         * Identifies a how this group of questions is known in a particular terminology such as LOINC.
         */
        @Child(name = "concept", type = {Coding.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Concept that represents this item within in a questionnaire", formalDefinition="Identifies a how this group of questions is known in a particular terminology such as LOINC." )
        protected List<Coding> concept;

        /**
         * The name of a section, the text of a question or text content for a text item.
         */
        @Child(name = "text", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Primary text for the item", formalDefinition="The name of a section, the text of a question or text content for a text item." )
        protected StringType text;

        /**
         * Identifies the type of questionnaire item this is - whether text for display, a grouping of other items or a particular type of data to be captured (string, integer, coded choice, etc.).
         */
        @Child(name = "type", type = {CodeType.class}, order=4, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="group | display | boolean | decimal | integer | date | dateTime +", formalDefinition="Identifies the type of questionnaire item this is - whether text for display, a grouping of other items or a particular type of data to be captured (string, integer, coded choice, etc.)." )
        protected Enumeration<QuestionnaireItemType> type;

        /**
         * If true, indicates that the item must be present in a "completed" QuestionnaireResponse.  If false, the item may be skipped when answering the questionnaire.
         */
        @Child(name = "required", type = {BooleanType.class}, order=5, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Whether the group must be included in data results", formalDefinition="If true, indicates that the item must be present in a \"completed\" QuestionnaireResponse.  If false, the item may be skipped when answering the questionnaire." )
        protected BooleanType required;

        /**
         * Whether the item may occur multiple times in the instance, containing multiple sets of answers.
         */
        @Child(name = "repeats", type = {BooleanType.class}, order=6, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Whether the group may repeat", formalDefinition="Whether the item may occur multiple times in the instance, containing multiple sets of answers." )
        protected BooleanType repeats;

        /**
         * Reference to a value set containing a list of codes representing permitted answers for the question.
         */
        @Child(name = "options", type = {ValueSet.class}, order=7, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Valueset containing permitted answers", formalDefinition="Reference to a value set containing a list of codes representing permitted answers for the question." )
        protected Reference options;

        /**
         * The actual object that is the target of the reference (Reference to a value set containing a list of codes representing permitted answers for the question.)
         */
        protected ValueSet optionsTarget;

        /**
         * For a "choice" question, identifies one of the permitted answers for the question.
         */
        @Child(name = "option", type = {Coding.class}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Permitted answer", formalDefinition="For a \"choice\" question, identifies one of the permitted answers for the question." )
        protected List<Coding> option;

        /**
         * Allows text, questions and other groups to be nested beneath a question or group.
         */
        @Child(name = "item", type = {QuestionnaireItemComponent.class}, order=9, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Nested questionnaire items", formalDefinition="Allows text, questions and other groups to be nested beneath a question or group." )
        protected List<QuestionnaireItemComponent> item;

        private static final long serialVersionUID = 10326314L;

    /**
     * Constructor
     */
      public QuestionnaireItemComponent() {
        super();
      }

    /**
     * Constructor
     */
      public QuestionnaireItemComponent(Enumeration<QuestionnaireItemType> type) {
        super();
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
          if (Utilities.noString(value))
            this.linkId = null;
          else {
            if (this.linkId == null)
              this.linkId = new StringType();
            this.linkId.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #concept} (Identifies a how this group of questions is known in a particular terminology such as LOINC.)
         */
        public List<Coding> getConcept() { 
          if (this.concept == null)
            this.concept = new ArrayList<Coding>();
          return this.concept;
        }

        public boolean hasConcept() { 
          if (this.concept == null)
            return false;
          for (Coding item : this.concept)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #concept} (Identifies a how this group of questions is known in a particular terminology such as LOINC.)
         */
    // syntactic sugar
        public Coding addConcept() { //3
          Coding t = new Coding();
          if (this.concept == null)
            this.concept = new ArrayList<Coding>();
          this.concept.add(t);
          return t;
        }

    // syntactic sugar
        public QuestionnaireItemComponent addConcept(Coding t) { //3
          if (t == null)
            return this;
          if (this.concept == null)
            this.concept = new ArrayList<Coding>();
          this.concept.add(t);
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
        public List<Coding> getOption() { 
          if (this.option == null)
            this.option = new ArrayList<Coding>();
          return this.option;
        }

        public boolean hasOption() { 
          if (this.option == null)
            return false;
          for (Coding item : this.option)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #option} (For a "choice" question, identifies one of the permitted answers for the question.)
         */
    // syntactic sugar
        public Coding addOption() { //3
          Coding t = new Coding();
          if (this.option == null)
            this.option = new ArrayList<Coding>();
          this.option.add(t);
          return t;
        }

    // syntactic sugar
        public QuestionnaireItemComponent addOption(Coding t) { //3
          if (t == null)
            return this;
          if (this.option == null)
            this.option = new ArrayList<Coding>();
          this.option.add(t);
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

        public boolean hasItem() { 
          if (this.item == null)
            return false;
          for (QuestionnaireItemComponent item : this.item)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #item} (Allows text, questions and other groups to be nested beneath a question or group.)
         */
    // syntactic sugar
        public QuestionnaireItemComponent addItem() { //3
          QuestionnaireItemComponent t = new QuestionnaireItemComponent();
          if (this.item == null)
            this.item = new ArrayList<QuestionnaireItemComponent>();
          this.item.add(t);
          return t;
        }

    // syntactic sugar
        public QuestionnaireItemComponent addItem(QuestionnaireItemComponent t) { //3
          if (t == null)
            return this;
          if (this.item == null)
            this.item = new ArrayList<QuestionnaireItemComponent>();
          this.item.add(t);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("linkId", "string", "An identifier that is unique within the Questionnaire allowing linkage to the equivalent item in a QuestionnaireResponse resource.", 0, java.lang.Integer.MAX_VALUE, linkId));
          childrenList.add(new Property("concept", "Coding", "Identifies a how this group of questions is known in a particular terminology such as LOINC.", 0, java.lang.Integer.MAX_VALUE, concept));
          childrenList.add(new Property("text", "string", "The name of a section, the text of a question or text content for a text item.", 0, java.lang.Integer.MAX_VALUE, text));
          childrenList.add(new Property("type", "code", "Identifies the type of questionnaire item this is - whether text for display, a grouping of other items or a particular type of data to be captured (string, integer, coded choice, etc.).", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("required", "boolean", "If true, indicates that the item must be present in a \"completed\" QuestionnaireResponse.  If false, the item may be skipped when answering the questionnaire.", 0, java.lang.Integer.MAX_VALUE, required));
          childrenList.add(new Property("repeats", "boolean", "Whether the item may occur multiple times in the instance, containing multiple sets of answers.", 0, java.lang.Integer.MAX_VALUE, repeats));
          childrenList.add(new Property("options", "Reference(ValueSet)", "Reference to a value set containing a list of codes representing permitted answers for the question.", 0, java.lang.Integer.MAX_VALUE, options));
          childrenList.add(new Property("option", "Coding", "For a \"choice\" question, identifies one of the permitted answers for the question.", 0, java.lang.Integer.MAX_VALUE, option));
          childrenList.add(new Property("item", "@Questionnaire.item", "Allows text, questions and other groups to be nested beneath a question or group.", 0, java.lang.Integer.MAX_VALUE, item));
        }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("linkId"))
          this.linkId = castToString(value); // StringType
        else if (name.equals("concept"))
          this.getConcept().add(castToCoding(value));
        else if (name.equals("text"))
          this.text = castToString(value); // StringType
        else if (name.equals("type"))
          this.type = new QuestionnaireItemTypeEnumFactory().fromType(value); // Enumeration<QuestionnaireItemType>
        else if (name.equals("required"))
          this.required = castToBoolean(value); // BooleanType
        else if (name.equals("repeats"))
          this.repeats = castToBoolean(value); // BooleanType
        else if (name.equals("options"))
          this.options = castToReference(value); // Reference
        else if (name.equals("option"))
          this.getOption().add(castToCoding(value));
        else if (name.equals("item"))
          this.getItem().add((QuestionnaireItemComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("linkId")) {
          throw new FHIRException("Cannot call addChild on a primitive type Questionnaire.linkId");
        }
        else if (name.equals("concept")) {
          return addConcept();
        }
        else if (name.equals("text")) {
          throw new FHIRException("Cannot call addChild on a primitive type Questionnaire.text");
        }
        else if (name.equals("type")) {
          throw new FHIRException("Cannot call addChild on a primitive type Questionnaire.type");
        }
        else if (name.equals("required")) {
          throw new FHIRException("Cannot call addChild on a primitive type Questionnaire.required");
        }
        else if (name.equals("repeats")) {
          throw new FHIRException("Cannot call addChild on a primitive type Questionnaire.repeats");
        }
        else if (name.equals("options")) {
          this.options = new Reference();
          return this.options;
        }
        else if (name.equals("option")) {
          return addOption();
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
        if (concept != null) {
          dst.concept = new ArrayList<Coding>();
          for (Coding i : concept)
            dst.concept.add(i.copy());
        };
        dst.text = text == null ? null : text.copy();
        dst.type = type == null ? null : type.copy();
        dst.required = required == null ? null : required.copy();
        dst.repeats = repeats == null ? null : repeats.copy();
        dst.options = options == null ? null : options.copy();
        if (option != null) {
          dst.option = new ArrayList<Coding>();
          for (Coding i : option)
            dst.option.add(i.copy());
        };
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
        return compareDeep(linkId, o.linkId, true) && compareDeep(concept, o.concept, true) && compareDeep(text, o.text, true)
           && compareDeep(type, o.type, true) && compareDeep(required, o.required, true) && compareDeep(repeats, o.repeats, true)
           && compareDeep(options, o.options, true) && compareDeep(option, o.option, true) && compareDeep(item, o.item, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof QuestionnaireItemComponent))
          return false;
        QuestionnaireItemComponent o = (QuestionnaireItemComponent) other;
        return compareValues(linkId, o.linkId, true) && compareValues(text, o.text, true) && compareValues(type, o.type, true)
           && compareValues(required, o.required, true) && compareValues(repeats, o.repeats, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (linkId == null || linkId.isEmpty()) && (concept == null || concept.isEmpty())
           && (text == null || text.isEmpty()) && (type == null || type.isEmpty()) && (required == null || required.isEmpty())
           && (repeats == null || repeats.isEmpty()) && (options == null || options.isEmpty()) && (option == null || option.isEmpty())
           && (item == null || item.isEmpty());
      }

  public String fhirType() {
    return "Questionnaire.item";

  }

  }

    /**
     * This records identifiers associated with this question set that are defined by business processes and/or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation).
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="External identifiers for this questionnaire", formalDefinition="This records identifiers associated with this question set that are defined by business processes and/or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)." )
    protected List<Identifier> identifier;

    /**
     * The version number assigned by the publisher for business reasons.  It may remain the same when the resource is updated.
     */
    @Child(name = "version", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Logical identifier for this version of Questionnaire", formalDefinition="The version number assigned by the publisher for business reasons.  It may remain the same when the resource is updated." )
    protected StringType version;

    /**
     * The lifecycle status of the questionnaire as a whole.
     */
    @Child(name = "status", type = {CodeType.class}, order=2, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="draft | published | retired", formalDefinition="The lifecycle status of the questionnaire as a whole." )
    protected Enumeration<QuestionnaireStatus> status;

    /**
     * The date that this questionnaire was last changed.
     */
    @Child(name = "date", type = {DateTimeType.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Date this version was authored", formalDefinition="The date that this questionnaire was last changed." )
    protected DateTimeType date;

    /**
     * Organization or person responsible for developing and maintaining the questionnaire.
     */
    @Child(name = "publisher", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Organization/individual who designed the questionnaire", formalDefinition="Organization or person responsible for developing and maintaining the questionnaire." )
    protected StringType publisher;

    /**
     * Contact details to assist a user in finding and communicating with the publisher.
     */
    @Child(name = "telecom", type = {ContactPoint.class}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Contact information of the publisher", formalDefinition="Contact details to assist a user in finding and communicating with the publisher." )
    protected List<ContactPoint> telecom;

    /**
     * The name or label associated with this questionnaire.
     */
    @Child(name = "title", type = {StringType.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Name for the questionnaire", formalDefinition="The name or label associated with this questionnaire." )
    protected StringType title;

    /**
     * Identifies a how this question or group of questions is known in a particular terminology such as LOINC.
     */
    @Child(name = "concept", type = {Coding.class}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Concept that represents the overall questionnaire", formalDefinition="Identifies a how this question or group of questions is known in a particular terminology such as LOINC." )
    protected List<Coding> concept;

    /**
     * Identifies the types of subjects that can be the subject of the questionnaire.
     */
    @Child(name = "subjectType", type = {CodeType.class}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Resource that can be subject of QuestionnaireResponse", formalDefinition="Identifies the types of subjects that can be the subject of the questionnaire." )
    protected List<CodeType> subjectType;

    /**
     * The questions and groupings of questions that make up the questionnaire.
     */
    @Child(name = "item", type = {}, order=9, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Questions and sections within the Questionnaire", formalDefinition="The questions and groupings of questions that make up the questionnaire." )
    protected List<QuestionnaireItemComponent> item;

    private static final long serialVersionUID = 154986016L;

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
     * @return {@link #identifier} (This records identifiers associated with this question set that are defined by business processes and/or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation).)
     */
    public List<Identifier> getIdentifier() { 
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      return this.identifier;
    }

    public boolean hasIdentifier() { 
      if (this.identifier == null)
        return false;
      for (Identifier item : this.identifier)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #identifier} (This records identifiers associated with this question set that are defined by business processes and/or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation).)
     */
    // syntactic sugar
    public Identifier addIdentifier() { //3
      Identifier t = new Identifier();
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return t;
    }

    // syntactic sugar
    public Questionnaire addIdentifier(Identifier t) { //3
      if (t == null)
        return this;
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return this;
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

    public boolean hasTelecom() { 
      if (this.telecom == null)
        return false;
      for (ContactPoint item : this.telecom)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #telecom} (Contact details to assist a user in finding and communicating with the publisher.)
     */
    // syntactic sugar
    public ContactPoint addTelecom() { //3
      ContactPoint t = new ContactPoint();
      if (this.telecom == null)
        this.telecom = new ArrayList<ContactPoint>();
      this.telecom.add(t);
      return t;
    }

    // syntactic sugar
    public Questionnaire addTelecom(ContactPoint t) { //3
      if (t == null)
        return this;
      if (this.telecom == null)
        this.telecom = new ArrayList<ContactPoint>();
      this.telecom.add(t);
      return this;
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
     * @return {@link #concept} (Identifies a how this question or group of questions is known in a particular terminology such as LOINC.)
     */
    public List<Coding> getConcept() { 
      if (this.concept == null)
        this.concept = new ArrayList<Coding>();
      return this.concept;
    }

    public boolean hasConcept() { 
      if (this.concept == null)
        return false;
      for (Coding item : this.concept)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #concept} (Identifies a how this question or group of questions is known in a particular terminology such as LOINC.)
     */
    // syntactic sugar
    public Coding addConcept() { //3
      Coding t = new Coding();
      if (this.concept == null)
        this.concept = new ArrayList<Coding>();
      this.concept.add(t);
      return t;
    }

    // syntactic sugar
    public Questionnaire addConcept(Coding t) { //3
      if (t == null)
        return this;
      if (this.concept == null)
        this.concept = new ArrayList<Coding>();
      this.concept.add(t);
      return this;
    }

    /**
     * @return {@link #subjectType} (Identifies the types of subjects that can be the subject of the questionnaire.)
     */
    public List<CodeType> getSubjectType() { 
      if (this.subjectType == null)
        this.subjectType = new ArrayList<CodeType>();
      return this.subjectType;
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
    // syntactic sugar
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

    public boolean hasItem() { 
      if (this.item == null)
        return false;
      for (QuestionnaireItemComponent item : this.item)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #item} (The questions and groupings of questions that make up the questionnaire.)
     */
    // syntactic sugar
    public QuestionnaireItemComponent addItem() { //3
      QuestionnaireItemComponent t = new QuestionnaireItemComponent();
      if (this.item == null)
        this.item = new ArrayList<QuestionnaireItemComponent>();
      this.item.add(t);
      return t;
    }

    // syntactic sugar
    public Questionnaire addItem(QuestionnaireItemComponent t) { //3
      if (t == null)
        return this;
      if (this.item == null)
        this.item = new ArrayList<QuestionnaireItemComponent>();
      this.item.add(t);
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "This records identifiers associated with this question set that are defined by business processes and/or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation).", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("version", "string", "The version number assigned by the publisher for business reasons.  It may remain the same when the resource is updated.", 0, java.lang.Integer.MAX_VALUE, version));
        childrenList.add(new Property("status", "code", "The lifecycle status of the questionnaire as a whole.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("date", "dateTime", "The date that this questionnaire was last changed.", 0, java.lang.Integer.MAX_VALUE, date));
        childrenList.add(new Property("publisher", "string", "Organization or person responsible for developing and maintaining the questionnaire.", 0, java.lang.Integer.MAX_VALUE, publisher));
        childrenList.add(new Property("telecom", "ContactPoint", "Contact details to assist a user in finding and communicating with the publisher.", 0, java.lang.Integer.MAX_VALUE, telecom));
        childrenList.add(new Property("title", "string", "The name or label associated with this questionnaire.", 0, java.lang.Integer.MAX_VALUE, title));
        childrenList.add(new Property("concept", "Coding", "Identifies a how this question or group of questions is known in a particular terminology such as LOINC.", 0, java.lang.Integer.MAX_VALUE, concept));
        childrenList.add(new Property("subjectType", "code", "Identifies the types of subjects that can be the subject of the questionnaire.", 0, java.lang.Integer.MAX_VALUE, subjectType));
        childrenList.add(new Property("item", "", "The questions and groupings of questions that make up the questionnaire.", 0, java.lang.Integer.MAX_VALUE, item));
      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier"))
          this.getIdentifier().add(castToIdentifier(value));
        else if (name.equals("version"))
          this.version = castToString(value); // StringType
        else if (name.equals("status"))
          this.status = new QuestionnaireStatusEnumFactory().fromType(value); // Enumeration<QuestionnaireStatus>
        else if (name.equals("date"))
          this.date = castToDateTime(value); // DateTimeType
        else if (name.equals("publisher"))
          this.publisher = castToString(value); // StringType
        else if (name.equals("telecom"))
          this.getTelecom().add(castToContactPoint(value));
        else if (name.equals("title"))
          this.title = castToString(value); // StringType
        else if (name.equals("concept"))
          this.getConcept().add(castToCoding(value));
        else if (name.equals("subjectType"))
          this.getSubjectType().add(castToCode(value));
        else if (name.equals("item"))
          this.getItem().add((QuestionnaireItemComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
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
        else if (name.equals("title")) {
          throw new FHIRException("Cannot call addChild on a primitive type Questionnaire.title");
        }
        else if (name.equals("concept")) {
          return addConcept();
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
        dst.title = title == null ? null : title.copy();
        if (concept != null) {
          dst.concept = new ArrayList<Coding>();
          for (Coding i : concept)
            dst.concept.add(i.copy());
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
        return compareDeep(identifier, o.identifier, true) && compareDeep(version, o.version, true) && compareDeep(status, o.status, true)
           && compareDeep(date, o.date, true) && compareDeep(publisher, o.publisher, true) && compareDeep(telecom, o.telecom, true)
           && compareDeep(title, o.title, true) && compareDeep(concept, o.concept, true) && compareDeep(subjectType, o.subjectType, true)
           && compareDeep(item, o.item, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Questionnaire))
          return false;
        Questionnaire o = (Questionnaire) other;
        return compareValues(version, o.version, true) && compareValues(status, o.status, true) && compareValues(date, o.date, true)
           && compareValues(publisher, o.publisher, true) && compareValues(title, o.title, true) && compareValues(subjectType, o.subjectType, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (version == null || version.isEmpty())
           && (status == null || status.isEmpty()) && (date == null || date.isEmpty()) && (publisher == null || publisher.isEmpty())
           && (telecom == null || telecom.isEmpty()) && (title == null || title.isEmpty()) && (concept == null || concept.isEmpty())
           && (subjectType == null || subjectType.isEmpty()) && (item == null || item.isEmpty());
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
   * Path: <b>Questionnaire.item.concept</b><br>
   * </p>
   */
  @SearchParamDefinition(name="code", path="Questionnaire.item.concept", description="A code that corresponds to the questionnaire or one of its groups", type="token" )
  public static final String SP_CODE = "code";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>code</b>
   * <p>
   * Description: <b>A code that corresponds to the questionnaire or one of its groups</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Questionnaire.item.concept</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CODE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CODE);

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

