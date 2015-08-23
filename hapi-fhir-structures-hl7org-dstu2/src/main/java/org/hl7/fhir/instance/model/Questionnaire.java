package org.hl7.fhir.instance.model;

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

// Generated on Sat, Aug 22, 2015 23:00-0400 for FHIR v0.5.0

import java.util.*;

import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.instance.model.Enumerations.*;
import org.hl7.fhir.instance.model.annotations.ResourceDef;
import org.hl7.fhir.instance.model.annotations.SearchParamDefinition;
import org.hl7.fhir.instance.model.annotations.Child;
import org.hl7.fhir.instance.model.annotations.Description;
import org.hl7.fhir.instance.model.annotations.Block;
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
        public static QuestionnaireStatus fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("draft".equals(codeString))
          return DRAFT;
        if ("published".equals(codeString))
          return PUBLISHED;
        if ("retired".equals(codeString))
          return RETIRED;
        throw new Exception("Unknown QuestionnaireStatus code '"+codeString+"'");
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
    public String toCode(QuestionnaireStatus code) {
      if (code == QuestionnaireStatus.DRAFT)
        return "draft";
      if (code == QuestionnaireStatus.PUBLISHED)
        return "published";
      if (code == QuestionnaireStatus.RETIRED)
        return "retired";
      return "?";
      }
    }

    public enum AnswerFormat {
        /**
         * Answer is a yes/no answer
         */
        BOOLEAN, 
        /**
         * Answer is a floating point number
         */
        DECIMAL, 
        /**
         * Answer is an integer
         */
        INTEGER, 
        /**
         * Answer is a date
         */
        DATE, 
        /**
         * Answer is a date and time
         */
        DATETIME, 
        /**
         * Answer is a system timestamp
         */
        INSTANT, 
        /**
         * Answer is a time (hour/minute/second) independent of date
         */
        TIME, 
        /**
         * Answer is a short (few words to short sentence) free-text entry
         */
        STRING, 
        /**
         * Answer is a long (potentially multi-paragram) free-text entry (still captured as a string)
         */
        TEXT, 
        /**
         * Answer is a url (website, FTP site, etc.)
         */
        URL, 
        /**
         * Answer is a Coding drawn from a list of options
         */
        CHOICE, 
        /**
         * Answer is a Coding drawn from a list of options or a free-text entry.
         */
        OPENCHOICE, 
        /**
         * Answer is binary content such as a image, PDF, etc.
         */
        ATTACHMENT, 
        /**
         * Answer is a reference to another resource (practitioner, organization, etc.)
         */
        REFERENCE, 
        /**
         * Answer is a combination of a numeric value and unit, potentially with a comparator (<, >, etc)
         */
        QUANTITY, 
        /**
         * added to help the parsers
         */
        NULL;
        public static AnswerFormat fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
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
        throw new Exception("Unknown AnswerFormat code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
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
            case BOOLEAN: return "http://hl7.org/fhir/answer-format";
            case DECIMAL: return "http://hl7.org/fhir/answer-format";
            case INTEGER: return "http://hl7.org/fhir/answer-format";
            case DATE: return "http://hl7.org/fhir/answer-format";
            case DATETIME: return "http://hl7.org/fhir/answer-format";
            case INSTANT: return "http://hl7.org/fhir/answer-format";
            case TIME: return "http://hl7.org/fhir/answer-format";
            case STRING: return "http://hl7.org/fhir/answer-format";
            case TEXT: return "http://hl7.org/fhir/answer-format";
            case URL: return "http://hl7.org/fhir/answer-format";
            case CHOICE: return "http://hl7.org/fhir/answer-format";
            case OPENCHOICE: return "http://hl7.org/fhir/answer-format";
            case ATTACHMENT: return "http://hl7.org/fhir/answer-format";
            case REFERENCE: return "http://hl7.org/fhir/answer-format";
            case QUANTITY: return "http://hl7.org/fhir/answer-format";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case BOOLEAN: return "Answer is a yes/no answer";
            case DECIMAL: return "Answer is a floating point number";
            case INTEGER: return "Answer is an integer";
            case DATE: return "Answer is a date";
            case DATETIME: return "Answer is a date and time";
            case INSTANT: return "Answer is a system timestamp";
            case TIME: return "Answer is a time (hour/minute/second) independent of date";
            case STRING: return "Answer is a short (few words to short sentence) free-text entry";
            case TEXT: return "Answer is a long (potentially multi-paragram) free-text entry (still captured as a string)";
            case URL: return "Answer is a url (website, FTP site, etc.)";
            case CHOICE: return "Answer is a Coding drawn from a list of options";
            case OPENCHOICE: return "Answer is a Coding drawn from a list of options or a free-text entry.";
            case ATTACHMENT: return "Answer is binary content such as a image, PDF, etc.";
            case REFERENCE: return "Answer is a reference to another resource (practitioner, organization, etc.)";
            case QUANTITY: return "Answer is a combination of a numeric value and unit, potentially with a comparator (<, >, etc)";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
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

  public static class AnswerFormatEnumFactory implements EnumFactory<AnswerFormat> {
    public AnswerFormat fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("boolean".equals(codeString))
          return AnswerFormat.BOOLEAN;
        if ("decimal".equals(codeString))
          return AnswerFormat.DECIMAL;
        if ("integer".equals(codeString))
          return AnswerFormat.INTEGER;
        if ("date".equals(codeString))
          return AnswerFormat.DATE;
        if ("dateTime".equals(codeString))
          return AnswerFormat.DATETIME;
        if ("instant".equals(codeString))
          return AnswerFormat.INSTANT;
        if ("time".equals(codeString))
          return AnswerFormat.TIME;
        if ("string".equals(codeString))
          return AnswerFormat.STRING;
        if ("text".equals(codeString))
          return AnswerFormat.TEXT;
        if ("url".equals(codeString))
          return AnswerFormat.URL;
        if ("choice".equals(codeString))
          return AnswerFormat.CHOICE;
        if ("open-choice".equals(codeString))
          return AnswerFormat.OPENCHOICE;
        if ("attachment".equals(codeString))
          return AnswerFormat.ATTACHMENT;
        if ("reference".equals(codeString))
          return AnswerFormat.REFERENCE;
        if ("quantity".equals(codeString))
          return AnswerFormat.QUANTITY;
        throw new IllegalArgumentException("Unknown AnswerFormat code '"+codeString+"'");
        }
    public String toCode(AnswerFormat code) {
      if (code == AnswerFormat.BOOLEAN)
        return "boolean";
      if (code == AnswerFormat.DECIMAL)
        return "decimal";
      if (code == AnswerFormat.INTEGER)
        return "integer";
      if (code == AnswerFormat.DATE)
        return "date";
      if (code == AnswerFormat.DATETIME)
        return "dateTime";
      if (code == AnswerFormat.INSTANT)
        return "instant";
      if (code == AnswerFormat.TIME)
        return "time";
      if (code == AnswerFormat.STRING)
        return "string";
      if (code == AnswerFormat.TEXT)
        return "text";
      if (code == AnswerFormat.URL)
        return "url";
      if (code == AnswerFormat.CHOICE)
        return "choice";
      if (code == AnswerFormat.OPENCHOICE)
        return "open-choice";
      if (code == AnswerFormat.ATTACHMENT)
        return "attachment";
      if (code == AnswerFormat.REFERENCE)
        return "reference";
      if (code == AnswerFormat.QUANTITY)
        return "quantity";
      return "?";
      }
    }

    @Block()
    public static class GroupComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * An identifier that is unique within the questionnaire allowing linkage to the equivalent group in a QuestionnaireResponse resource.
         */
        @Child(name = "linkId", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="To link questionnaire with questionnaire response", formalDefinition="An identifier that is unique within the questionnaire allowing linkage to the equivalent group in a QuestionnaireResponse resource." )
        protected StringType linkId;

        /**
         * The human-readable name for this section of the questionnaire.
         */
        @Child(name = "title", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Name to be displayed for group", formalDefinition="The human-readable name for this section of the questionnaire." )
        protected StringType title;

        /**
         * Identifies a how this group of questions is known in a particular terminology such as LOINC.
         */
        @Child(name = "concept", type = {Coding.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Concept that represents this section on a questionnaire", formalDefinition="Identifies a how this group of questions is known in a particular terminology such as LOINC." )
        protected List<Coding> concept;

        /**
         * Additional text for the group, used for display purposes.
         */
        @Child(name = "text", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Additional text for the group", formalDefinition="Additional text for the group, used for display purposes." )
        protected StringType text;

        /**
         * If true, indicates that the group must be present and have required questions within it answered.  If false, the group may be skipped when answering the questionnaire.
         */
        @Child(name = "required", type = {BooleanType.class}, order=5, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Must group be included in data results?", formalDefinition="If true, indicates that the group must be present and have required questions within it answered.  If false, the group may be skipped when answering the questionnaire." )
        protected BooleanType required;

        /**
         * Whether the group may occur multiple times in the instance, containing multiple sets of answers.
         */
        @Child(name = "repeats", type = {BooleanType.class}, order=6, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Whether the group may repeat", formalDefinition="Whether the group may occur multiple times in the instance, containing multiple sets of answers." )
        protected BooleanType repeats;

        /**
         * A sub-group within a group. The ordering of groups within this group is relevant.
         */
        @Child(name = "group", type = {GroupComponent.class}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Nested questionnaire group", formalDefinition="A sub-group within a group. The ordering of groups within this group is relevant." )
        protected List<GroupComponent> group;

        /**
         * Set of questions within this group. The order of questions within the group is relevant.
         */
        @Child(name = "question", type = {}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Questions in this group", formalDefinition="Set of questions within this group. The order of questions within the group is relevant." )
        protected List<QuestionComponent> question;

        private static final long serialVersionUID = 494129548L;

    /*
     * Constructor
     */
      public GroupComponent() {
        super();
      }

        /**
         * @return {@link #linkId} (An identifier that is unique within the questionnaire allowing linkage to the equivalent group in a QuestionnaireResponse resource.). This is the underlying object with id, value and extensions. The accessor "getLinkId" gives direct access to the value
         */
        public StringType getLinkIdElement() { 
          if (this.linkId == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create GroupComponent.linkId");
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
         * @param value {@link #linkId} (An identifier that is unique within the questionnaire allowing linkage to the equivalent group in a QuestionnaireResponse resource.). This is the underlying object with id, value and extensions. The accessor "getLinkId" gives direct access to the value
         */
        public GroupComponent setLinkIdElement(StringType value) { 
          this.linkId = value;
          return this;
        }

        /**
         * @return An identifier that is unique within the questionnaire allowing linkage to the equivalent group in a QuestionnaireResponse resource.
         */
        public String getLinkId() { 
          return this.linkId == null ? null : this.linkId.getValue();
        }

        /**
         * @param value An identifier that is unique within the questionnaire allowing linkage to the equivalent group in a QuestionnaireResponse resource.
         */
        public GroupComponent setLinkId(String value) { 
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
         * @return {@link #title} (The human-readable name for this section of the questionnaire.). This is the underlying object with id, value and extensions. The accessor "getTitle" gives direct access to the value
         */
        public StringType getTitleElement() { 
          if (this.title == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create GroupComponent.title");
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
         * @param value {@link #title} (The human-readable name for this section of the questionnaire.). This is the underlying object with id, value and extensions. The accessor "getTitle" gives direct access to the value
         */
        public GroupComponent setTitleElement(StringType value) { 
          this.title = value;
          return this;
        }

        /**
         * @return The human-readable name for this section of the questionnaire.
         */
        public String getTitle() { 
          return this.title == null ? null : this.title.getValue();
        }

        /**
         * @param value The human-readable name for this section of the questionnaire.
         */
        public GroupComponent setTitle(String value) { 
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
        public GroupComponent addConcept(Coding t) { //3
          if (t == null)
            return this;
          if (this.concept == null)
            this.concept = new ArrayList<Coding>();
          this.concept.add(t);
          return this;
        }

        /**
         * @return {@link #text} (Additional text for the group, used for display purposes.). This is the underlying object with id, value and extensions. The accessor "getText" gives direct access to the value
         */
        public StringType getTextElement() { 
          if (this.text == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create GroupComponent.text");
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
         * @param value {@link #text} (Additional text for the group, used for display purposes.). This is the underlying object with id, value and extensions. The accessor "getText" gives direct access to the value
         */
        public GroupComponent setTextElement(StringType value) { 
          this.text = value;
          return this;
        }

        /**
         * @return Additional text for the group, used for display purposes.
         */
        public String getText() { 
          return this.text == null ? null : this.text.getValue();
        }

        /**
         * @param value Additional text for the group, used for display purposes.
         */
        public GroupComponent setText(String value) { 
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
         * @return {@link #required} (If true, indicates that the group must be present and have required questions within it answered.  If false, the group may be skipped when answering the questionnaire.). This is the underlying object with id, value and extensions. The accessor "getRequired" gives direct access to the value
         */
        public BooleanType getRequiredElement() { 
          if (this.required == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create GroupComponent.required");
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
         * @param value {@link #required} (If true, indicates that the group must be present and have required questions within it answered.  If false, the group may be skipped when answering the questionnaire.). This is the underlying object with id, value and extensions. The accessor "getRequired" gives direct access to the value
         */
        public GroupComponent setRequiredElement(BooleanType value) { 
          this.required = value;
          return this;
        }

        /**
         * @return If true, indicates that the group must be present and have required questions within it answered.  If false, the group may be skipped when answering the questionnaire.
         */
        public boolean getRequired() { 
          return this.required == null || this.required.isEmpty() ? false : this.required.getValue();
        }

        /**
         * @param value If true, indicates that the group must be present and have required questions within it answered.  If false, the group may be skipped when answering the questionnaire.
         */
        public GroupComponent setRequired(boolean value) { 
            if (this.required == null)
              this.required = new BooleanType();
            this.required.setValue(value);
          return this;
        }

        /**
         * @return {@link #repeats} (Whether the group may occur multiple times in the instance, containing multiple sets of answers.). This is the underlying object with id, value and extensions. The accessor "getRepeats" gives direct access to the value
         */
        public BooleanType getRepeatsElement() { 
          if (this.repeats == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create GroupComponent.repeats");
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
         * @param value {@link #repeats} (Whether the group may occur multiple times in the instance, containing multiple sets of answers.). This is the underlying object with id, value and extensions. The accessor "getRepeats" gives direct access to the value
         */
        public GroupComponent setRepeatsElement(BooleanType value) { 
          this.repeats = value;
          return this;
        }

        /**
         * @return Whether the group may occur multiple times in the instance, containing multiple sets of answers.
         */
        public boolean getRepeats() { 
          return this.repeats == null || this.repeats.isEmpty() ? false : this.repeats.getValue();
        }

        /**
         * @param value Whether the group may occur multiple times in the instance, containing multiple sets of answers.
         */
        public GroupComponent setRepeats(boolean value) { 
            if (this.repeats == null)
              this.repeats = new BooleanType();
            this.repeats.setValue(value);
          return this;
        }

        /**
         * @return {@link #group} (A sub-group within a group. The ordering of groups within this group is relevant.)
         */
        public List<GroupComponent> getGroup() { 
          if (this.group == null)
            this.group = new ArrayList<GroupComponent>();
          return this.group;
        }

        public boolean hasGroup() { 
          if (this.group == null)
            return false;
          for (GroupComponent item : this.group)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #group} (A sub-group within a group. The ordering of groups within this group is relevant.)
         */
    // syntactic sugar
        public GroupComponent addGroup() { //3
          GroupComponent t = new GroupComponent();
          if (this.group == null)
            this.group = new ArrayList<GroupComponent>();
          this.group.add(t);
          return t;
        }

    // syntactic sugar
        public GroupComponent addGroup(GroupComponent t) { //3
          if (t == null)
            return this;
          if (this.group == null)
            this.group = new ArrayList<GroupComponent>();
          this.group.add(t);
          return this;
        }

        /**
         * @return {@link #question} (Set of questions within this group. The order of questions within the group is relevant.)
         */
        public List<QuestionComponent> getQuestion() { 
          if (this.question == null)
            this.question = new ArrayList<QuestionComponent>();
          return this.question;
        }

        public boolean hasQuestion() { 
          if (this.question == null)
            return false;
          for (QuestionComponent item : this.question)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #question} (Set of questions within this group. The order of questions within the group is relevant.)
         */
    // syntactic sugar
        public QuestionComponent addQuestion() { //3
          QuestionComponent t = new QuestionComponent();
          if (this.question == null)
            this.question = new ArrayList<QuestionComponent>();
          this.question.add(t);
          return t;
        }

    // syntactic sugar
        public GroupComponent addQuestion(QuestionComponent t) { //3
          if (t == null)
            return this;
          if (this.question == null)
            this.question = new ArrayList<QuestionComponent>();
          this.question.add(t);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("linkId", "string", "An identifier that is unique within the questionnaire allowing linkage to the equivalent group in a QuestionnaireResponse resource.", 0, java.lang.Integer.MAX_VALUE, linkId));
          childrenList.add(new Property("title", "string", "The human-readable name for this section of the questionnaire.", 0, java.lang.Integer.MAX_VALUE, title));
          childrenList.add(new Property("concept", "Coding", "Identifies a how this group of questions is known in a particular terminology such as LOINC.", 0, java.lang.Integer.MAX_VALUE, concept));
          childrenList.add(new Property("text", "string", "Additional text for the group, used for display purposes.", 0, java.lang.Integer.MAX_VALUE, text));
          childrenList.add(new Property("required", "boolean", "If true, indicates that the group must be present and have required questions within it answered.  If false, the group may be skipped when answering the questionnaire.", 0, java.lang.Integer.MAX_VALUE, required));
          childrenList.add(new Property("repeats", "boolean", "Whether the group may occur multiple times in the instance, containing multiple sets of answers.", 0, java.lang.Integer.MAX_VALUE, repeats));
          childrenList.add(new Property("group", "@Questionnaire.group", "A sub-group within a group. The ordering of groups within this group is relevant.", 0, java.lang.Integer.MAX_VALUE, group));
          childrenList.add(new Property("question", "", "Set of questions within this group. The order of questions within the group is relevant.", 0, java.lang.Integer.MAX_VALUE, question));
        }

      public GroupComponent copy() {
        GroupComponent dst = new GroupComponent();
        copyValues(dst);
        dst.linkId = linkId == null ? null : linkId.copy();
        dst.title = title == null ? null : title.copy();
        if (concept != null) {
          dst.concept = new ArrayList<Coding>();
          for (Coding i : concept)
            dst.concept.add(i.copy());
        };
        dst.text = text == null ? null : text.copy();
        dst.required = required == null ? null : required.copy();
        dst.repeats = repeats == null ? null : repeats.copy();
        if (group != null) {
          dst.group = new ArrayList<GroupComponent>();
          for (GroupComponent i : group)
            dst.group.add(i.copy());
        };
        if (question != null) {
          dst.question = new ArrayList<QuestionComponent>();
          for (QuestionComponent i : question)
            dst.question.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof GroupComponent))
          return false;
        GroupComponent o = (GroupComponent) other;
        return compareDeep(linkId, o.linkId, true) && compareDeep(title, o.title, true) && compareDeep(concept, o.concept, true)
           && compareDeep(text, o.text, true) && compareDeep(required, o.required, true) && compareDeep(repeats, o.repeats, true)
           && compareDeep(group, o.group, true) && compareDeep(question, o.question, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof GroupComponent))
          return false;
        GroupComponent o = (GroupComponent) other;
        return compareValues(linkId, o.linkId, true) && compareValues(title, o.title, true) && compareValues(text, o.text, true)
           && compareValues(required, o.required, true) && compareValues(repeats, o.repeats, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (linkId == null || linkId.isEmpty()) && (title == null || title.isEmpty())
           && (concept == null || concept.isEmpty()) && (text == null || text.isEmpty()) && (required == null || required.isEmpty())
           && (repeats == null || repeats.isEmpty()) && (group == null || group.isEmpty()) && (question == null || question.isEmpty())
          ;
      }

  }

    @Block()
    public static class QuestionComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * An identifier that is unique within the questionnaire allowing linkage to the equivalent group in a [[[QuestionnaireResponse]]] resource.
         */
        @Child(name = "linkId", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="To link questionnaire with questionnaire response", formalDefinition="An identifier that is unique within the questionnaire allowing linkage to the equivalent group in a [[[QuestionnaireResponse]]] resource." )
        protected StringType linkId;

        /**
         * Identifies a how this question is known in a particular terminology such as LOINC.
         */
        @Child(name = "concept", type = {Coding.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Concept that represents this question on a questionnaire", formalDefinition="Identifies a how this question is known in a particular terminology such as LOINC." )
        protected List<Coding> concept;

        /**
         * The actual question as shown to the user to prompt them for an answer.
         */
        @Child(name = "text", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Text of the question as it is shown to the user", formalDefinition="The actual question as shown to the user to prompt them for an answer." )
        protected StringType text;

        /**
         * The expected format of the answer, e.g. the type of input (string, integer) or whether a (multiple) choice is expected.
         */
        @Child(name = "type", type = {CodeType.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="boolean | decimal | integer | date | dateTime +", formalDefinition="The expected format of the answer, e.g. the type of input (string, integer) or whether a (multiple) choice is expected." )
        protected Enumeration<AnswerFormat> type;

        /**
         * If true, indicates that the question must be answered and have required groups within it also present.  If false, the question and any contained groups may be skipped when answering the questionnaire.
         */
        @Child(name = "required", type = {BooleanType.class}, order=5, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Must question be answered in data results?", formalDefinition="If true, indicates that the question must be answered and have required groups within it also present.  If false, the question and any contained groups may be skipped when answering the questionnaire." )
        protected BooleanType required;

        /**
         * If true, the question may have more than one answer.
         */
        @Child(name = "repeats", type = {BooleanType.class}, order=6, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Can question  have multiple answers?", formalDefinition="If true, the question may have more than one answer." )
        protected BooleanType repeats;

        /**
         * Reference to a valueset containing the a list of codes representing permitted answers for the question.
         */
        @Child(name = "options", type = {ValueSet.class}, order=7, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Valueset containing permitted answers", formalDefinition="Reference to a valueset containing the a list of codes representing permitted answers for the question." )
        protected Reference options;

        /**
         * The actual object that is the target of the reference (Reference to a valueset containing the a list of codes representing permitted answers for the question.)
         */
        protected ValueSet optionsTarget;

        /**
         * Nested group, containing nested question for this question. The order of groups within the question is relevant.
         */
        @Child(name = "group", type = {GroupComponent.class}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Nested questionnaire group", formalDefinition="Nested group, containing nested question for this question. The order of groups within the question is relevant." )
        protected List<GroupComponent> group;

        private static final long serialVersionUID = 1655002985L;

    /*
     * Constructor
     */
      public QuestionComponent() {
        super();
      }

        /**
         * @return {@link #linkId} (An identifier that is unique within the questionnaire allowing linkage to the equivalent group in a [[[QuestionnaireResponse]]] resource.). This is the underlying object with id, value and extensions. The accessor "getLinkId" gives direct access to the value
         */
        public StringType getLinkIdElement() { 
          if (this.linkId == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create QuestionComponent.linkId");
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
         * @param value {@link #linkId} (An identifier that is unique within the questionnaire allowing linkage to the equivalent group in a [[[QuestionnaireResponse]]] resource.). This is the underlying object with id, value and extensions. The accessor "getLinkId" gives direct access to the value
         */
        public QuestionComponent setLinkIdElement(StringType value) { 
          this.linkId = value;
          return this;
        }

        /**
         * @return An identifier that is unique within the questionnaire allowing linkage to the equivalent group in a [[[QuestionnaireResponse]]] resource.
         */
        public String getLinkId() { 
          return this.linkId == null ? null : this.linkId.getValue();
        }

        /**
         * @param value An identifier that is unique within the questionnaire allowing linkage to the equivalent group in a [[[QuestionnaireResponse]]] resource.
         */
        public QuestionComponent setLinkId(String value) { 
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
         * @return {@link #concept} (Identifies a how this question is known in a particular terminology such as LOINC.)
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
         * @return {@link #concept} (Identifies a how this question is known in a particular terminology such as LOINC.)
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
        public QuestionComponent addConcept(Coding t) { //3
          if (t == null)
            return this;
          if (this.concept == null)
            this.concept = new ArrayList<Coding>();
          this.concept.add(t);
          return this;
        }

        /**
         * @return {@link #text} (The actual question as shown to the user to prompt them for an answer.). This is the underlying object with id, value and extensions. The accessor "getText" gives direct access to the value
         */
        public StringType getTextElement() { 
          if (this.text == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create QuestionComponent.text");
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
         * @param value {@link #text} (The actual question as shown to the user to prompt them for an answer.). This is the underlying object with id, value and extensions. The accessor "getText" gives direct access to the value
         */
        public QuestionComponent setTextElement(StringType value) { 
          this.text = value;
          return this;
        }

        /**
         * @return The actual question as shown to the user to prompt them for an answer.
         */
        public String getText() { 
          return this.text == null ? null : this.text.getValue();
        }

        /**
         * @param value The actual question as shown to the user to prompt them for an answer.
         */
        public QuestionComponent setText(String value) { 
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
         * @return {@link #type} (The expected format of the answer, e.g. the type of input (string, integer) or whether a (multiple) choice is expected.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public Enumeration<AnswerFormat> getTypeElement() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create QuestionComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new Enumeration<AnswerFormat>(new AnswerFormatEnumFactory()); // bb
          return this.type;
        }

        public boolean hasTypeElement() { 
          return this.type != null && !this.type.isEmpty();
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (The expected format of the answer, e.g. the type of input (string, integer) or whether a (multiple) choice is expected.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public QuestionComponent setTypeElement(Enumeration<AnswerFormat> value) { 
          this.type = value;
          return this;
        }

        /**
         * @return The expected format of the answer, e.g. the type of input (string, integer) or whether a (multiple) choice is expected.
         */
        public AnswerFormat getType() { 
          return this.type == null ? null : this.type.getValue();
        }

        /**
         * @param value The expected format of the answer, e.g. the type of input (string, integer) or whether a (multiple) choice is expected.
         */
        public QuestionComponent setType(AnswerFormat value) { 
          if (value == null)
            this.type = null;
          else {
            if (this.type == null)
              this.type = new Enumeration<AnswerFormat>(new AnswerFormatEnumFactory());
            this.type.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #required} (If true, indicates that the question must be answered and have required groups within it also present.  If false, the question and any contained groups may be skipped when answering the questionnaire.). This is the underlying object with id, value and extensions. The accessor "getRequired" gives direct access to the value
         */
        public BooleanType getRequiredElement() { 
          if (this.required == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create QuestionComponent.required");
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
         * @param value {@link #required} (If true, indicates that the question must be answered and have required groups within it also present.  If false, the question and any contained groups may be skipped when answering the questionnaire.). This is the underlying object with id, value and extensions. The accessor "getRequired" gives direct access to the value
         */
        public QuestionComponent setRequiredElement(BooleanType value) { 
          this.required = value;
          return this;
        }

        /**
         * @return If true, indicates that the question must be answered and have required groups within it also present.  If false, the question and any contained groups may be skipped when answering the questionnaire.
         */
        public boolean getRequired() { 
          return this.required == null || this.required.isEmpty() ? false : this.required.getValue();
        }

        /**
         * @param value If true, indicates that the question must be answered and have required groups within it also present.  If false, the question and any contained groups may be skipped when answering the questionnaire.
         */
        public QuestionComponent setRequired(boolean value) { 
            if (this.required == null)
              this.required = new BooleanType();
            this.required.setValue(value);
          return this;
        }

        /**
         * @return {@link #repeats} (If true, the question may have more than one answer.). This is the underlying object with id, value and extensions. The accessor "getRepeats" gives direct access to the value
         */
        public BooleanType getRepeatsElement() { 
          if (this.repeats == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create QuestionComponent.repeats");
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
         * @param value {@link #repeats} (If true, the question may have more than one answer.). This is the underlying object with id, value and extensions. The accessor "getRepeats" gives direct access to the value
         */
        public QuestionComponent setRepeatsElement(BooleanType value) { 
          this.repeats = value;
          return this;
        }

        /**
         * @return If true, the question may have more than one answer.
         */
        public boolean getRepeats() { 
          return this.repeats == null || this.repeats.isEmpty() ? false : this.repeats.getValue();
        }

        /**
         * @param value If true, the question may have more than one answer.
         */
        public QuestionComponent setRepeats(boolean value) { 
            if (this.repeats == null)
              this.repeats = new BooleanType();
            this.repeats.setValue(value);
          return this;
        }

        /**
         * @return {@link #options} (Reference to a valueset containing the a list of codes representing permitted answers for the question.)
         */
        public Reference getOptions() { 
          if (this.options == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create QuestionComponent.options");
            else if (Configuration.doAutoCreate())
              this.options = new Reference(); // cc
          return this.options;
        }

        public boolean hasOptions() { 
          return this.options != null && !this.options.isEmpty();
        }

        /**
         * @param value {@link #options} (Reference to a valueset containing the a list of codes representing permitted answers for the question.)
         */
        public QuestionComponent setOptions(Reference value) { 
          this.options = value;
          return this;
        }

        /**
         * @return {@link #options} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Reference to a valueset containing the a list of codes representing permitted answers for the question.)
         */
        public ValueSet getOptionsTarget() { 
          if (this.optionsTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create QuestionComponent.options");
            else if (Configuration.doAutoCreate())
              this.optionsTarget = new ValueSet(); // aa
          return this.optionsTarget;
        }

        /**
         * @param value {@link #options} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Reference to a valueset containing the a list of codes representing permitted answers for the question.)
         */
        public QuestionComponent setOptionsTarget(ValueSet value) { 
          this.optionsTarget = value;
          return this;
        }

        /**
         * @return {@link #group} (Nested group, containing nested question for this question. The order of groups within the question is relevant.)
         */
        public List<GroupComponent> getGroup() { 
          if (this.group == null)
            this.group = new ArrayList<GroupComponent>();
          return this.group;
        }

        public boolean hasGroup() { 
          if (this.group == null)
            return false;
          for (GroupComponent item : this.group)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #group} (Nested group, containing nested question for this question. The order of groups within the question is relevant.)
         */
    // syntactic sugar
        public GroupComponent addGroup() { //3
          GroupComponent t = new GroupComponent();
          if (this.group == null)
            this.group = new ArrayList<GroupComponent>();
          this.group.add(t);
          return t;
        }

    // syntactic sugar
        public QuestionComponent addGroup(GroupComponent t) { //3
          if (t == null)
            return this;
          if (this.group == null)
            this.group = new ArrayList<GroupComponent>();
          this.group.add(t);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("linkId", "string", "An identifier that is unique within the questionnaire allowing linkage to the equivalent group in a [[[QuestionnaireResponse]]] resource.", 0, java.lang.Integer.MAX_VALUE, linkId));
          childrenList.add(new Property("concept", "Coding", "Identifies a how this question is known in a particular terminology such as LOINC.", 0, java.lang.Integer.MAX_VALUE, concept));
          childrenList.add(new Property("text", "string", "The actual question as shown to the user to prompt them for an answer.", 0, java.lang.Integer.MAX_VALUE, text));
          childrenList.add(new Property("type", "code", "The expected format of the answer, e.g. the type of input (string, integer) or whether a (multiple) choice is expected.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("required", "boolean", "If true, indicates that the question must be answered and have required groups within it also present.  If false, the question and any contained groups may be skipped when answering the questionnaire.", 0, java.lang.Integer.MAX_VALUE, required));
          childrenList.add(new Property("repeats", "boolean", "If true, the question may have more than one answer.", 0, java.lang.Integer.MAX_VALUE, repeats));
          childrenList.add(new Property("options", "Reference(ValueSet)", "Reference to a valueset containing the a list of codes representing permitted answers for the question.", 0, java.lang.Integer.MAX_VALUE, options));
          childrenList.add(new Property("group", "@Questionnaire.group", "Nested group, containing nested question for this question. The order of groups within the question is relevant.", 0, java.lang.Integer.MAX_VALUE, group));
        }

      public QuestionComponent copy() {
        QuestionComponent dst = new QuestionComponent();
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
        if (group != null) {
          dst.group = new ArrayList<GroupComponent>();
          for (GroupComponent i : group)
            dst.group.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof QuestionComponent))
          return false;
        QuestionComponent o = (QuestionComponent) other;
        return compareDeep(linkId, o.linkId, true) && compareDeep(concept, o.concept, true) && compareDeep(text, o.text, true)
           && compareDeep(type, o.type, true) && compareDeep(required, o.required, true) && compareDeep(repeats, o.repeats, true)
           && compareDeep(options, o.options, true) && compareDeep(group, o.group, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof QuestionComponent))
          return false;
        QuestionComponent o = (QuestionComponent) other;
        return compareValues(linkId, o.linkId, true) && compareValues(text, o.text, true) && compareValues(type, o.type, true)
           && compareValues(required, o.required, true) && compareValues(repeats, o.repeats, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (linkId == null || linkId.isEmpty()) && (concept == null || concept.isEmpty())
           && (text == null || text.isEmpty()) && (type == null || type.isEmpty()) && (required == null || required.isEmpty())
           && (repeats == null || repeats.isEmpty()) && (options == null || options.isEmpty()) && (group == null || group.isEmpty())
          ;
      }

  }

    /**
     * This records identifiers associated with this question set that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation).
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="External Ids for this questionnaire", formalDefinition="This records identifiers associated with this question set that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)." )
    protected List<Identifier> identifier;

    /**
     * The version number assigned by the publisher for business reasons.  It may remain the same when the resource is updated.
     */
    @Child(name = "version", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Logical id for this version of Questionnaire", formalDefinition="The version number assigned by the publisher for business reasons.  It may remain the same when the resource is updated." )
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
     * Identifies the types of subjects that can be the subject of the questionnaire.
     */
    @Child(name = "subjectType", type = {CodeType.class}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Resource that can be subject of QuestionnaireResponse", formalDefinition="Identifies the types of subjects that can be the subject of the questionnaire." )
    protected List<CodeType> subjectType;

    /**
     * A collection of related questions (or further groupings of questions).
     */
    @Child(name = "group", type = {}, order=7, min=1, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Grouped questions", formalDefinition="A collection of related questions (or further groupings of questions)." )
    protected GroupComponent group;

    private static final long serialVersionUID = -1348292652L;

  /*
   * Constructor
   */
    public Questionnaire() {
      super();
    }

  /*
   * Constructor
   */
    public Questionnaire(Enumeration<QuestionnaireStatus> status, GroupComponent group) {
      super();
      this.status = status;
      this.group = group;
    }

    /**
     * @return {@link #identifier} (This records identifiers associated with this question set that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation).)
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
     * @return {@link #identifier} (This records identifiers associated with this question set that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation).)
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
     * @return {@link #group} (A collection of related questions (or further groupings of questions).)
     */
    public GroupComponent getGroup() { 
      if (this.group == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Questionnaire.group");
        else if (Configuration.doAutoCreate())
          this.group = new GroupComponent(); // cc
      return this.group;
    }

    public boolean hasGroup() { 
      return this.group != null && !this.group.isEmpty();
    }

    /**
     * @param value {@link #group} (A collection of related questions (or further groupings of questions).)
     */
    public Questionnaire setGroup(GroupComponent value) { 
      this.group = value;
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "This records identifiers associated with this question set that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation).", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("version", "string", "The version number assigned by the publisher for business reasons.  It may remain the same when the resource is updated.", 0, java.lang.Integer.MAX_VALUE, version));
        childrenList.add(new Property("status", "code", "The lifecycle status of the questionnaire as a whole.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("date", "dateTime", "The date that this questionnaire was last changed.", 0, java.lang.Integer.MAX_VALUE, date));
        childrenList.add(new Property("publisher", "string", "Organization or person responsible for developing and maintaining the questionnaire.", 0, java.lang.Integer.MAX_VALUE, publisher));
        childrenList.add(new Property("telecom", "ContactPoint", "Contact details to assist a user in finding and communicating with the publisher.", 0, java.lang.Integer.MAX_VALUE, telecom));
        childrenList.add(new Property("subjectType", "code", "Identifies the types of subjects that can be the subject of the questionnaire.", 0, java.lang.Integer.MAX_VALUE, subjectType));
        childrenList.add(new Property("group", "", "A collection of related questions (or further groupings of questions).", 0, java.lang.Integer.MAX_VALUE, group));
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
        if (subjectType != null) {
          dst.subjectType = new ArrayList<CodeType>();
          for (CodeType i : subjectType)
            dst.subjectType.add(i.copy());
        };
        dst.group = group == null ? null : group.copy();
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
           && compareDeep(subjectType, o.subjectType, true) && compareDeep(group, o.group, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Questionnaire))
          return false;
        Questionnaire o = (Questionnaire) other;
        return compareValues(version, o.version, true) && compareValues(status, o.status, true) && compareValues(date, o.date, true)
           && compareValues(publisher, o.publisher, true) && compareValues(subjectType, o.subjectType, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (version == null || version.isEmpty())
           && (status == null || status.isEmpty()) && (date == null || date.isEmpty()) && (publisher == null || publisher.isEmpty())
           && (telecom == null || telecom.isEmpty()) && (subjectType == null || subjectType.isEmpty())
           && (group == null || group.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Questionnaire;
   }

  @SearchParamDefinition(name="date", path="Questionnaire.date", description="When the questionnaire was last changed", type="date" )
  public static final String SP_DATE = "date";
  @SearchParamDefinition(name="identifier", path="Questionnaire.identifier", description="An identifier for the questionnaire", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
  @SearchParamDefinition(name="code", path="Questionnaire.group.concept", description="A code that corresponds to the questionnaire or one of its groups", type="token" )
  public static final String SP_CODE = "code";
  @SearchParamDefinition(name="publisher", path="Questionnaire.publisher", description="The author of the questionnaire", type="string" )
  public static final String SP_PUBLISHER = "publisher";
  @SearchParamDefinition(name="title", path="Questionnaire.group.title", description="All or part of the name of the questionnaire (title for the root group of the questionnaire)", type="string" )
  public static final String SP_TITLE = "title";
  @SearchParamDefinition(name="version", path="Questionnaire.version", description="The business version of the questionnaire", type="string" )
  public static final String SP_VERSION = "version";
  @SearchParamDefinition(name="status", path="Questionnaire.status", description="The status of the questionnaire", type="token" )
  public static final String SP_STATUS = "status";

}

