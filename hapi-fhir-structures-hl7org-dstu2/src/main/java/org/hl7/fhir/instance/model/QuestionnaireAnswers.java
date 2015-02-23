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

// Generated on Wed, Feb 18, 2015 12:09-0500 for FHIR v0.4.0

import java.util.*;

import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.instance.model.annotations.ResourceDef;
import org.hl7.fhir.instance.model.annotations.SearchParamDefinition;
import org.hl7.fhir.instance.model.annotations.Block;
import org.hl7.fhir.instance.model.annotations.Child;
import org.hl7.fhir.instance.model.annotations.Description;
/**
 * A structured set of questions and their answers. The questions are ordered and grouped into coherent subsets, corresponding to the structure of the grouping of the underlying questions.
 */
@ResourceDef(name="QuestionnaireAnswers", profile="http://hl7.org/fhir/Profile/QuestionnaireAnswers")
public class QuestionnaireAnswers extends DomainResource {

    public enum QuestionnaireAnswersStatus {
        /**
         * This QuestionnaireAnswers has been partially filled out with answers, but changes or additions are still expected to be made to it.
         */
        INPROGRESS, 
        /**
         * This QuestionnaireAnswers has been filled out with answers, and the current content is regarded as definitive.
         */
        COMPLETED, 
        /**
         * This QuestionnaireAnswers has been filled out with answers, then marked as complete, yet changes or additions have been made to it afterwards.
         */
        AMENDED, 
        /**
         * added to help the parsers
         */
        NULL;
        public static QuestionnaireAnswersStatus fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("in-progress".equals(codeString))
          return INPROGRESS;
        if ("completed".equals(codeString))
          return COMPLETED;
        if ("amended".equals(codeString))
          return AMENDED;
        throw new Exception("Unknown QuestionnaireAnswersStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case INPROGRESS: return "in-progress";
            case COMPLETED: return "completed";
            case AMENDED: return "amended";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case INPROGRESS: return "";
            case COMPLETED: return "";
            case AMENDED: return "";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case INPROGRESS: return "This QuestionnaireAnswers has been partially filled out with answers, but changes or additions are still expected to be made to it.";
            case COMPLETED: return "This QuestionnaireAnswers has been filled out with answers, and the current content is regarded as definitive.";
            case AMENDED: return "This QuestionnaireAnswers has been filled out with answers, then marked as complete, yet changes or additions have been made to it afterwards.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case INPROGRESS: return "in-progress";
            case COMPLETED: return "completed";
            case AMENDED: return "amended";
            default: return "?";
          }
        }
    }

  public static class QuestionnaireAnswersStatusEnumFactory implements EnumFactory<QuestionnaireAnswersStatus> {
    public QuestionnaireAnswersStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("in-progress".equals(codeString))
          return QuestionnaireAnswersStatus.INPROGRESS;
        if ("completed".equals(codeString))
          return QuestionnaireAnswersStatus.COMPLETED;
        if ("amended".equals(codeString))
          return QuestionnaireAnswersStatus.AMENDED;
        throw new IllegalArgumentException("Unknown QuestionnaireAnswersStatus code '"+codeString+"'");
        }
    public String toCode(QuestionnaireAnswersStatus code) {
      if (code == QuestionnaireAnswersStatus.INPROGRESS)
        return "in-progress";
      if (code == QuestionnaireAnswersStatus.COMPLETED)
        return "completed";
      if (code == QuestionnaireAnswersStatus.AMENDED)
        return "amended";
      return "?";
      }
    }

    @Block()
    public static class GroupComponent extends BackboneElement {
        /**
         * Identifies the group from the Questionnaire that corresponds to this group in the QuestionnaireAnswers resource.
         */
        @Child(name="linkId", type={StringType.class}, order=1, min=0, max=1)
        @Description(shortDefinition="Corresponding group within Questionnaire", formalDefinition="Identifies the group from the Questionnaire that corresponds to this group in the QuestionnaireAnswers resource." )
        protected StringType linkId;

        /**
         * Text that is displayed above the contents of the group.
         */
        @Child(name="title", type={StringType.class}, order=2, min=0, max=1)
        @Description(shortDefinition="Name for this group", formalDefinition="Text that is displayed above the contents of the group." )
        protected StringType title;

        /**
         * Additional text for the group, used for display purposes.
         */
        @Child(name="text", type={StringType.class}, order=3, min=0, max=1)
        @Description(shortDefinition="Additional text for the group", formalDefinition="Additional text for the group, used for display purposes." )
        protected StringType text;

        /**
         * More specific subject this section's answers are about, details the subject given in QuestionnaireAnswers.
         */
        @Child(name="subject", type={}, order=4, min=0, max=1)
        @Description(shortDefinition="The subject this group's answers are about", formalDefinition="More specific subject this section's answers are about, details the subject given in QuestionnaireAnswers." )
        protected Reference subject;

        /**
         * The actual object that is the target of the reference (More specific subject this section's answers are about, details the subject given in QuestionnaireAnswers.)
         */
        protected Resource subjectTarget;

        /**
         * A sub-group within a group. The ordering of groups within this group is relevant.
         */
        @Child(name="group", type={GroupComponent.class}, order=5, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="Nested questionnaire answers group", formalDefinition="A sub-group within a group. The ordering of groups within this group is relevant." )
        protected List<GroupComponent> group;

        /**
         * Set of questions within this group. The order of questions within the group is relevant.
         */
        @Child(name="question", type={}, order=6, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="Questions in this group", formalDefinition="Set of questions within this group. The order of questions within the group is relevant." )
        protected List<QuestionComponent> question;

        private static final long serialVersionUID = -1045990435L;

      public GroupComponent() {
        super();
      }

        /**
         * @return {@link #linkId} (Identifies the group from the Questionnaire that corresponds to this group in the QuestionnaireAnswers resource.). This is the underlying object with id, value and extensions. The accessor "getLinkId" gives direct access to the value
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
         * @param value {@link #linkId} (Identifies the group from the Questionnaire that corresponds to this group in the QuestionnaireAnswers resource.). This is the underlying object with id, value and extensions. The accessor "getLinkId" gives direct access to the value
         */
        public GroupComponent setLinkIdElement(StringType value) { 
          this.linkId = value;
          return this;
        }

        /**
         * @return Identifies the group from the Questionnaire that corresponds to this group in the QuestionnaireAnswers resource.
         */
        public String getLinkId() { 
          return this.linkId == null ? null : this.linkId.getValue();
        }

        /**
         * @param value Identifies the group from the Questionnaire that corresponds to this group in the QuestionnaireAnswers resource.
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
         * @return {@link #title} (Text that is displayed above the contents of the group.). This is the underlying object with id, value and extensions. The accessor "getTitle" gives direct access to the value
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
         * @param value {@link #title} (Text that is displayed above the contents of the group.). This is the underlying object with id, value and extensions. The accessor "getTitle" gives direct access to the value
         */
        public GroupComponent setTitleElement(StringType value) { 
          this.title = value;
          return this;
        }

        /**
         * @return Text that is displayed above the contents of the group.
         */
        public String getTitle() { 
          return this.title == null ? null : this.title.getValue();
        }

        /**
         * @param value Text that is displayed above the contents of the group.
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
         * @return {@link #subject} (More specific subject this section's answers are about, details the subject given in QuestionnaireAnswers.)
         */
        public Reference getSubject() { 
          if (this.subject == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create GroupComponent.subject");
            else if (Configuration.doAutoCreate())
              this.subject = new Reference(); // cc
          return this.subject;
        }

        public boolean hasSubject() { 
          return this.subject != null && !this.subject.isEmpty();
        }

        /**
         * @param value {@link #subject} (More specific subject this section's answers are about, details the subject given in QuestionnaireAnswers.)
         */
        public GroupComponent setSubject(Reference value) { 
          this.subject = value;
          return this;
        }

        /**
         * @return {@link #subject} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (More specific subject this section's answers are about, details the subject given in QuestionnaireAnswers.)
         */
        public Resource getSubjectTarget() { 
          return this.subjectTarget;
        }

        /**
         * @param value {@link #subject} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (More specific subject this section's answers are about, details the subject given in QuestionnaireAnswers.)
         */
        public GroupComponent setSubjectTarget(Resource value) { 
          this.subjectTarget = value;
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

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("linkId", "string", "Identifies the group from the Questionnaire that corresponds to this group in the QuestionnaireAnswers resource.", 0, java.lang.Integer.MAX_VALUE, linkId));
          childrenList.add(new Property("title", "string", "Text that is displayed above the contents of the group.", 0, java.lang.Integer.MAX_VALUE, title));
          childrenList.add(new Property("text", "string", "Additional text for the group, used for display purposes.", 0, java.lang.Integer.MAX_VALUE, text));
          childrenList.add(new Property("subject", "Reference(Any)", "More specific subject this section's answers are about, details the subject given in QuestionnaireAnswers.", 0, java.lang.Integer.MAX_VALUE, subject));
          childrenList.add(new Property("group", "@QuestionnaireAnswers.group", "A sub-group within a group. The ordering of groups within this group is relevant.", 0, java.lang.Integer.MAX_VALUE, group));
          childrenList.add(new Property("question", "", "Set of questions within this group. The order of questions within the group is relevant.", 0, java.lang.Integer.MAX_VALUE, question));
        }

      public GroupComponent copy() {
        GroupComponent dst = new GroupComponent();
        copyValues(dst);
        dst.linkId = linkId == null ? null : linkId.copy();
        dst.title = title == null ? null : title.copy();
        dst.text = text == null ? null : text.copy();
        dst.subject = subject == null ? null : subject.copy();
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
        return compareDeep(linkId, o.linkId, true) && compareDeep(title, o.title, true) && compareDeep(text, o.text, true)
           && compareDeep(subject, o.subject, true) && compareDeep(group, o.group, true) && compareDeep(question, o.question, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof GroupComponent))
          return false;
        GroupComponent o = (GroupComponent) other;
        return compareValues(linkId, o.linkId, true) && compareValues(title, o.title, true) && compareValues(text, o.text, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (linkId == null || linkId.isEmpty()) && (title == null || title.isEmpty())
           && (text == null || text.isEmpty()) && (subject == null || subject.isEmpty()) && (group == null || group.isEmpty())
           && (question == null || question.isEmpty());
      }

  }

    @Block()
    public static class QuestionComponent extends BackboneElement {
        /**
         * Identifies the question from the Questionnaire that corresponds to this question in the QuestionnaireAnswers resource.
         */
        @Child(name="linkId", type={StringType.class}, order=1, min=0, max=1)
        @Description(shortDefinition="Corresponding question within Questionnaire", formalDefinition="Identifies the question from the Questionnaire that corresponds to this question in the QuestionnaireAnswers resource." )
        protected StringType linkId;

        /**
         * The actual question as shown to the user to prompt them for an answer.
         */
        @Child(name="text", type={StringType.class}, order=2, min=0, max=1)
        @Description(shortDefinition = "Text of the question as it is shown to the user", formalDefinition = "The actual question as shown to the user to prompt them for an answer.")
        protected StringType text;

        /**
         * The respondent's answer(s) to the question.
         */
        @Child(name="answer", type={}, order=3, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="The response(s) to the question", formalDefinition="The respondent's answer(s) to the question." )
        protected List<QuestionAnswerComponent> answer;

        /**
         * Nested group, containing nested question for this question. The order of groups within the question is relevant.
         */
        @Child(name="group", type={GroupComponent.class}, order=4, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="Nested questionnaire group", formalDefinition="Nested group, containing nested question for this question. The order of groups within the question is relevant." )
        protected List<GroupComponent> group;

        private static final long serialVersionUID = -564009278L;

      public QuestionComponent() {
        super();
      }

        /**
         * @return {@link #linkId} (Identifies the question from the Questionnaire that corresponds to this question in the QuestionnaireAnswers resource.). This is the underlying object with id, value and extensions. The accessor "getLinkId" gives direct access to the value
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
         * @param value {@link #linkId} (Identifies the question from the Questionnaire that corresponds to this question in the QuestionnaireAnswers resource.). This is the underlying object with id, value and extensions. The accessor "getLinkId" gives direct access to the value
         */
        public QuestionComponent setLinkIdElement(StringType value) { 
          this.linkId = value;
          return this;
        }

        /**
         * @return Identifies the question from the Questionnaire that corresponds to this question in the QuestionnaireAnswers resource.
         */
        public String getLinkId() { 
          return this.linkId == null ? null : this.linkId.getValue();
        }

        /**
         * @param value Identifies the question from the Questionnaire that corresponds to this question in the QuestionnaireAnswers resource.
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
         * @return {@link #answer} (The respondent's answer(s) to the question.)
         */
        public List<QuestionAnswerComponent> getAnswer() { 
          if (this.answer == null)
            this.answer = new ArrayList<QuestionAnswerComponent>();
          return this.answer;
        }

        public boolean hasAnswer() { 
          if (this.answer == null)
            return false;
          for (QuestionAnswerComponent item : this.answer)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #answer} (The respondent's answer(s) to the question.)
         */
    // syntactic sugar
        public QuestionAnswerComponent addAnswer() { //3
          QuestionAnswerComponent t = new QuestionAnswerComponent();
          if (this.answer == null)
            this.answer = new ArrayList<QuestionAnswerComponent>();
          this.answer.add(t);
          return t;
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

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("linkId", "string", "Identifies the question from the Questionnaire that corresponds to this question in the QuestionnaireAnswers resource.", 0, java.lang.Integer.MAX_VALUE, linkId));
          childrenList.add(new Property("text", "string", "The actual question as shown to the user to prompt them for an answer.", 0, java.lang.Integer.MAX_VALUE, text));
          childrenList.add(new Property("answer", "", "The respondent's answer(s) to the question.", 0, java.lang.Integer.MAX_VALUE, answer));
          childrenList.add(new Property("group", "@QuestionnaireAnswers.group", "Nested group, containing nested question for this question. The order of groups within the question is relevant.", 0, java.lang.Integer.MAX_VALUE, group));
        }

      public QuestionComponent copy() {
        QuestionComponent dst = new QuestionComponent();
        copyValues(dst);
        dst.linkId = linkId == null ? null : linkId.copy();
        dst.text = text == null ? null : text.copy();
        if (answer != null) {
          dst.answer = new ArrayList<QuestionAnswerComponent>();
          for (QuestionAnswerComponent i : answer)
            dst.answer.add(i.copy());
        };
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
        return compareDeep(linkId, o.linkId, true) && compareDeep(text, o.text, true) && compareDeep(answer, o.answer, true)
           && compareDeep(group, o.group, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof QuestionComponent))
          return false;
        QuestionComponent o = (QuestionComponent) other;
        return compareValues(linkId, o.linkId, true) && compareValues(text, o.text, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (linkId == null || linkId.isEmpty()) && (text == null || text.isEmpty())
           && (answer == null || answer.isEmpty()) && (group == null || group.isEmpty());
      }

  }

    @Block()
    public static class QuestionAnswerComponent extends BackboneElement {
        /**
         * The answer (or one of the answers) provided by the respondant to the question.
         */
        @Child(name = "value", type = {BooleanType.class, DecimalType.class, IntegerType.class, DateType.class, DateTimeType.class, InstantType.class, TimeType.class, StringType.class, UriType.class, Attachment.class, Coding.class, Quantity.class}, order = 1, min = 0, max = 1)
        @Description(shortDefinition = "Single-valued answer to the question", formalDefinition = "The answer (or one of the answers) provided by the respondant to the question.")
        protected Type value;

        private static final long serialVersionUID = -732981989L;

      public QuestionAnswerComponent() {
        super();
      }

        /**
         * @return {@link #value} (The answer (or one of the answers) provided by the respondant to the question.)
         */
        public Type getValue() { 
          return this.value;
        }

        /**
         * @return {@link #value} (The answer (or one of the answers) provided by the respondant to the question.)
         */
        public BooleanType getValueBooleanType() throws Exception { 
          if (!(this.value instanceof BooleanType))
            throw new Exception("Type mismatch: the type BooleanType was expected, but "+this.value.getClass().getName()+" was encountered");
          return (BooleanType) this.value;
        }

        /**
         * @return {@link #value} (The answer (or one of the answers) provided by the respondant to the question.)
         */
        public DecimalType getValueDecimalType() throws Exception { 
          if (!(this.value instanceof DecimalType))
            throw new Exception("Type mismatch: the type DecimalType was expected, but "+this.value.getClass().getName()+" was encountered");
          return (DecimalType) this.value;
        }

        /**
         * @return {@link #value} (The answer (or one of the answers) provided by the respondant to the question.)
         */
        public IntegerType getValueIntegerType() throws Exception { 
          if (!(this.value instanceof IntegerType))
            throw new Exception("Type mismatch: the type IntegerType was expected, but "+this.value.getClass().getName()+" was encountered");
          return (IntegerType) this.value;
        }

        /**
         * @return {@link #value} (The answer (or one of the answers) provided by the respondant to the question.)
         */
        public DateType getValueDateType() throws Exception { 
          if (!(this.value instanceof DateType))
            throw new Exception("Type mismatch: the type DateType was expected, but "+this.value.getClass().getName()+" was encountered");
          return (DateType) this.value;
        }

        /**
         * @return {@link #value} (The answer (or one of the answers) provided by the respondant to the question.)
         */
        public DateTimeType getValueDateTimeType() throws Exception { 
          if (!(this.value instanceof DateTimeType))
            throw new Exception("Type mismatch: the type DateTimeType was expected, but "+this.value.getClass().getName()+" was encountered");
          return (DateTimeType) this.value;
        }

        /**
         * @return {@link #value} (The answer (or one of the answers) provided by the respondant to the question.)
         */
        public InstantType getValueInstantType() throws Exception { 
          if (!(this.value instanceof InstantType))
            throw new Exception("Type mismatch: the type InstantType was expected, but "+this.value.getClass().getName()+" was encountered");
          return (InstantType) this.value;
        }

        /**
         * @return {@link #value} (The answer (or one of the answers) provided by the respondant to the question.)
         */
        public TimeType getValueTimeType() throws Exception { 
          if (!(this.value instanceof TimeType))
            throw new Exception("Type mismatch: the type TimeType was expected, but "+this.value.getClass().getName()+" was encountered");
          return (TimeType) this.value;
        }

        /**
         * @return {@link #value} (The answer (or one of the answers) provided by the respondant to the question.)
         */
        public StringType getValueStringType() throws Exception { 
          if (!(this.value instanceof StringType))
            throw new Exception("Type mismatch: the type StringType was expected, but "+this.value.getClass().getName()+" was encountered");
          return (StringType) this.value;
        }

        /**
         * @return {@link #value} (The answer (or one of the answers) provided by the respondant to the question.)
         */
        public UriType getValueUriType() throws Exception {
          if (!(this.value instanceof UriType))
            throw new Exception("Type mismatch: the type UriType was expected, but " + this.value.getClass().getName() + " was encountered");
          return (UriType) this.value;
        }

      /**
       * @return {@link #value} (The answer (or one of the answers) provided by the respondant to the question.)
       */
        public Attachment getValueAttachment() throws Exception { 
          if (!(this.value instanceof Attachment))
            throw new Exception("Type mismatch: the type Attachment was expected, but "+this.value.getClass().getName()+" was encountered");
          return (Attachment) this.value;
        }

      /**
       * @return {@link #value} (The answer (or one of the answers) provided by the respondant to the question.)
         */
        public Coding getValueCoding() throws Exception { 
          if (!(this.value instanceof Coding))
            throw new Exception("Type mismatch: the type Coding was expected, but "+this.value.getClass().getName()+" was encountered");
          return (Coding) this.value;
        }

      /**
       * @return {@link #value} (The answer (or one of the answers) provided by the respondant to the question.)
         */
        public Quantity getValueQuantity() throws Exception { 
          if (!(this.value instanceof Quantity))
            throw new Exception("Type mismatch: the type Quantity was expected, but "+this.value.getClass().getName()+" was encountered");
          return (Quantity) this.value;
        }

      /**
       * @return {@link #value} (The answer (or one of the answers) provided by the respondant to the question.)
         */
        public Reference getValueReference() throws Exception { 
          if (!(this.value instanceof Reference))
            throw new Exception("Type mismatch: the type Reference was expected, but "+this.value.getClass().getName()+" was encountered");
          return (Reference) this.value;
        }

        public boolean hasValue() { 
          return this.value != null && !this.value.isEmpty();
        }

      /**
       * @param value {@link #value} (The answer (or one of the answers) provided by the respondant to the question.)
         */
        public QuestionAnswerComponent setValue(Type value) { 
          this.value = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("value[x]", "boolean|decimal|integer|date|dateTime|instant|time|string|uri|Attachment|Coding|Quantity|Reference(Any)", "The answer (or one of the answers) provided by the respondant to the question.", 0, java.lang.Integer.MAX_VALUE, value));
        }

      public QuestionAnswerComponent copy() {
        QuestionAnswerComponent dst = new QuestionAnswerComponent();
        copyValues(dst);
        dst.value = value == null ? null : value.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof QuestionAnswerComponent))
          return false;
        QuestionAnswerComponent o = (QuestionAnswerComponent) other;
        return compareDeep(value, o.value, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof QuestionAnswerComponent))
          return false;
        QuestionAnswerComponent o = (QuestionAnswerComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (value == null || value.isEmpty());
      }

  }

    /**
     * A business identifier assigned to a particular completed (or partially completed) questionnaire.
     */
    @Child(name = "identifier", type = {Identifier.class}, order = 0, min = 0, max = 1)
    @Description(shortDefinition="Unique id for this set of answers", formalDefinition="A business identifier assigned to a particular completed (or partially completed) questionnaire." )
    protected Identifier identifier;

    /**
     * Indicates the Questionnaire resource that defines the form for which answers are being provided.
     */
    @Child(name = "questionnaire", type = {Questionnaire.class}, order = 1, min = 0, max = 1)
    @Description(shortDefinition="Form being answered", formalDefinition="Indicates the Questionnaire resource that defines the form for which answers are being provided." )
    protected Reference questionnaire;

    /**
     * The actual object that is the target of the reference (Indicates the Questionnaire resource that defines the form for which answers are being provided.)
     */
    protected Questionnaire questionnaireTarget;

    /**
     * The lifecycle status of the questionnaire answers as a whole.
     */
    @Child(name = "status", type = {CodeType.class}, order = 2, min = 1, max = 1)
    @Description(shortDefinition="in-progress | completed | amended", formalDefinition="The lifecycle status of the questionnaire answers as a whole." )
    protected Enumeration<QuestionnaireAnswersStatus> status;

    /**
     * The subject of the questionnaire answers.  This could be a patient, organization, practitioner, device, etc.  This is who/what the answers apply to, but is not necessarily the source of information.
     */
    @Child(name = "subject", type = {}, order = 3, min = 0, max = 1)
    @Description(shortDefinition="The subject of the questions", formalDefinition="The subject of the questionnaire answers.  This could be a patient, organization, practitioner, device, etc.  This is who/what the answers apply to, but is not necessarily the source of information." )
    protected Reference subject;

    /**
     * The actual object that is the target of the reference (The subject of the questionnaire answers.  This could be a patient, organization, practitioner, device, etc.  This is who/what the answers apply to, but is not necessarily the source of information.)
     */
    protected Resource subjectTarget;

    /**
     * Person who received the answers to the questions in the QuestionnaireAnswers and recorded them in the system.
     */
    @Child(name = "author", type = {Practitioner.class, Patient.class, RelatedPerson.class}, order = 4, min = 0, max = 1)
    @Description(shortDefinition="Person who received and recorded the answers", formalDefinition="Person who received the answers to the questions in the QuestionnaireAnswers and recorded them in the system." )
    protected Reference author;

    /**
     * The actual object that is the target of the reference (Person who received the answers to the questions in the QuestionnaireAnswers and recorded them in the system.)
     */
    protected Resource authorTarget;

    /**
     * The date and/or time that this version of the questionnaire answers was authored.
     */
    @Child(name = "authored", type = {DateTimeType.class}, order = 5, min = 1, max = 1)
    @Description(shortDefinition="Date this version was authored", formalDefinition="The date and/or time that this version of the questionnaire answers was authored." )
    protected DateTimeType authored;

  /**
   * The person who answered the questions about the subject.
   */
  @Child(name = "source", type = {Patient.class, Practitioner.class, RelatedPerson.class}, order = 6, min = 0, max = 1)
  @Description(shortDefinition = "The person who answered the questions", formalDefinition = "The person who answered the questions about the subject.")
    protected Reference source;

  /**
   * The actual object that is the target of the reference (The person who answered the questions about the subject.)
     */
    protected Resource sourceTarget;

    /**
     * Encounter during which this set of questionnaire answers were collected. When there were multiple encounters, this is the one considered most relevant to the context of the answers.
     */
    @Child(name = "encounter", type = {Encounter.class}, order = 7, min = 0, max = 1)
    @Description(shortDefinition="Primary encounter during which the answers were collected", formalDefinition="Encounter during which this set of questionnaire answers were collected. When there were multiple encounters, this is the one considered most relevant to the context of the answers." )
    protected Reference encounter;

    /**
     * The actual object that is the target of the reference (Encounter during which this set of questionnaire answers were collected. When there were multiple encounters, this is the one considered most relevant to the context of the answers.)
     */
    protected Encounter encounterTarget;

    /**
     * A group of questions to a possibly similarly grouped set of questions in the questionnaire answers.
     */
    @Child(name = "group", type = {}, order = 8, min = 0, max = 1)
    @Description(shortDefinition="Grouped questions", formalDefinition="A group of questions to a possibly similarly grouped set of questions in the questionnaire answers." )
    protected GroupComponent group;

    private static final long serialVersionUID = -949684393L;

    public QuestionnaireAnswers() {
      super();
    }

    public QuestionnaireAnswers(Enumeration<QuestionnaireAnswersStatus> status, DateTimeType authored) {
      super();
      this.status = status;
      this.authored = authored;
    }

    /**
     * @return {@link #identifier} (A business identifier assigned to a particular completed (or partially completed) questionnaire.)
     */
    public Identifier getIdentifier() { 
      if (this.identifier == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create QuestionnaireAnswers.identifier");
        else if (Configuration.doAutoCreate())
          this.identifier = new Identifier(); // cc
      return this.identifier;
    }

    public boolean hasIdentifier() { 
      return this.identifier != null && !this.identifier.isEmpty();
    }

    /**
     * @param value {@link #identifier} (A business identifier assigned to a particular completed (or partially completed) questionnaire.)
     */
    public QuestionnaireAnswers setIdentifier(Identifier value) { 
      this.identifier = value;
      return this;
    }

    /**
     * @return {@link #questionnaire} (Indicates the Questionnaire resource that defines the form for which answers are being provided.)
     */
    public Reference getQuestionnaire() { 
      if (this.questionnaire == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create QuestionnaireAnswers.questionnaire");
        else if (Configuration.doAutoCreate())
          this.questionnaire = new Reference(); // cc
      return this.questionnaire;
    }

    public boolean hasQuestionnaire() { 
      return this.questionnaire != null && !this.questionnaire.isEmpty();
    }

    /**
     * @param value {@link #questionnaire} (Indicates the Questionnaire resource that defines the form for which answers are being provided.)
     */
    public QuestionnaireAnswers setQuestionnaire(Reference value) { 
      this.questionnaire = value;
      return this;
    }

    /**
     * @return {@link #questionnaire} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Indicates the Questionnaire resource that defines the form for which answers are being provided.)
     */
    public Questionnaire getQuestionnaireTarget() { 
      if (this.questionnaireTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create QuestionnaireAnswers.questionnaire");
        else if (Configuration.doAutoCreate())
          this.questionnaireTarget = new Questionnaire(); // aa
      return this.questionnaireTarget;
    }

    /**
     * @param value {@link #questionnaire} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Indicates the Questionnaire resource that defines the form for which answers are being provided.)
     */
    public QuestionnaireAnswers setQuestionnaireTarget(Questionnaire value) { 
      this.questionnaireTarget = value;
      return this;
    }

    /**
     * @return {@link #status} (The lifecycle status of the questionnaire answers as a whole.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<QuestionnaireAnswersStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create QuestionnaireAnswers.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<QuestionnaireAnswersStatus>(new QuestionnaireAnswersStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (The lifecycle status of the questionnaire answers as a whole.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public QuestionnaireAnswers setStatusElement(Enumeration<QuestionnaireAnswersStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The lifecycle status of the questionnaire answers as a whole.
     */
    public QuestionnaireAnswersStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The lifecycle status of the questionnaire answers as a whole.
     */
    public QuestionnaireAnswers setStatus(QuestionnaireAnswersStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<QuestionnaireAnswersStatus>(new QuestionnaireAnswersStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #subject} (The subject of the questionnaire answers.  This could be a patient, organization, practitioner, device, etc.  This is who/what the answers apply to, but is not necessarily the source of information.)
     */
    public Reference getSubject() { 
      if (this.subject == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create QuestionnaireAnswers.subject");
        else if (Configuration.doAutoCreate())
          this.subject = new Reference(); // cc
      return this.subject;
    }

    public boolean hasSubject() { 
      return this.subject != null && !this.subject.isEmpty();
    }

    /**
     * @param value {@link #subject} (The subject of the questionnaire answers.  This could be a patient, organization, practitioner, device, etc.  This is who/what the answers apply to, but is not necessarily the source of information.)
     */
    public QuestionnaireAnswers setSubject(Reference value) { 
      this.subject = value;
      return this;
    }

    /**
     * @return {@link #subject} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The subject of the questionnaire answers.  This could be a patient, organization, practitioner, device, etc.  This is who/what the answers apply to, but is not necessarily the source of information.)
     */
    public Resource getSubjectTarget() { 
      return this.subjectTarget;
    }

    /**
     * @param value {@link #subject} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The subject of the questionnaire answers.  This could be a patient, organization, practitioner, device, etc.  This is who/what the answers apply to, but is not necessarily the source of information.)
     */
    public QuestionnaireAnswers setSubjectTarget(Resource value) { 
      this.subjectTarget = value;
      return this;
    }

    /**
     * @return {@link #author} (Person who received the answers to the questions in the QuestionnaireAnswers and recorded them in the system.)
     */
    public Reference getAuthor() { 
      if (this.author == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create QuestionnaireAnswers.author");
        else if (Configuration.doAutoCreate())
          this.author = new Reference(); // cc
      return this.author;
    }

    public boolean hasAuthor() { 
      return this.author != null && !this.author.isEmpty();
    }

    /**
     * @param value {@link #author} (Person who received the answers to the questions in the QuestionnaireAnswers and recorded them in the system.)
     */
    public QuestionnaireAnswers setAuthor(Reference value) { 
      this.author = value;
      return this;
    }

    /**
     * @return {@link #author} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Person who received the answers to the questions in the QuestionnaireAnswers and recorded them in the system.)
     */
    public Resource getAuthorTarget() { 
      return this.authorTarget;
    }

    /**
     * @param value {@link #author} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Person who received the answers to the questions in the QuestionnaireAnswers and recorded them in the system.)
     */
    public QuestionnaireAnswers setAuthorTarget(Resource value) { 
      this.authorTarget = value;
      return this;
    }

    /**
     * @return {@link #authored} (The date and/or time that this version of the questionnaire answers was authored.). This is the underlying object with id, value and extensions. The accessor "getAuthored" gives direct access to the value
     */
    public DateTimeType getAuthoredElement() { 
      if (this.authored == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create QuestionnaireAnswers.authored");
        else if (Configuration.doAutoCreate())
          this.authored = new DateTimeType(); // bb
      return this.authored;
    }

    public boolean hasAuthoredElement() { 
      return this.authored != null && !this.authored.isEmpty();
    }

    public boolean hasAuthored() { 
      return this.authored != null && !this.authored.isEmpty();
    }

    /**
     * @param value {@link #authored} (The date and/or time that this version of the questionnaire answers was authored.). This is the underlying object with id, value and extensions. The accessor "getAuthored" gives direct access to the value
     */
    public QuestionnaireAnswers setAuthoredElement(DateTimeType value) { 
      this.authored = value;
      return this;
    }

    /**
     * @return The date and/or time that this version of the questionnaire answers was authored.
     */
    public Date getAuthored() { 
      return this.authored == null ? null : this.authored.getValue();
    }

    /**
     * @param value The date and/or time that this version of the questionnaire answers was authored.
     */
    public QuestionnaireAnswers setAuthored(Date value) { 
        if (this.authored == null)
          this.authored = new DateTimeType();
        this.authored.setValue(value);
      return this;
    }

  /**
   * @return {@link #source} (The person who answered the questions about the subject.)
     */
    public Reference getSource() { 
      if (this.source == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create QuestionnaireAnswers.source");
        else if (Configuration.doAutoCreate())
          this.source = new Reference(); // cc
      return this.source;
    }

    public boolean hasSource() { 
      return this.source != null && !this.source.isEmpty();
    }

  /**
   * @param value {@link #source} (The person who answered the questions about the subject.)
     */
    public QuestionnaireAnswers setSource(Reference value) { 
      this.source = value;
      return this;
    }

  /**
   * @return {@link #source} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The person who answered the questions about the subject.)
     */
    public Resource getSourceTarget() { 
      return this.sourceTarget;
    }

  /**
   * @param value {@link #source} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The person who answered the questions about the subject.)
     */
    public QuestionnaireAnswers setSourceTarget(Resource value) { 
      this.sourceTarget = value;
      return this;
    }

    /**
     * @return {@link #encounter} (Encounter during which this set of questionnaire answers were collected. When there were multiple encounters, this is the one considered most relevant to the context of the answers.)
     */
    public Reference getEncounter() { 
      if (this.encounter == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create QuestionnaireAnswers.encounter");
        else if (Configuration.doAutoCreate())
          this.encounter = new Reference(); // cc
      return this.encounter;
    }

    public boolean hasEncounter() { 
      return this.encounter != null && !this.encounter.isEmpty();
    }

    /**
     * @param value {@link #encounter} (Encounter during which this set of questionnaire answers were collected. When there were multiple encounters, this is the one considered most relevant to the context of the answers.)
     */
    public QuestionnaireAnswers setEncounter(Reference value) { 
      this.encounter = value;
      return this;
    }

    /**
     * @return {@link #encounter} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Encounter during which this set of questionnaire answers were collected. When there were multiple encounters, this is the one considered most relevant to the context of the answers.)
     */
    public Encounter getEncounterTarget() { 
      if (this.encounterTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create QuestionnaireAnswers.encounter");
        else if (Configuration.doAutoCreate())
          this.encounterTarget = new Encounter(); // aa
      return this.encounterTarget;
    }

    /**
     * @param value {@link #encounter} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Encounter during which this set of questionnaire answers were collected. When there were multiple encounters, this is the one considered most relevant to the context of the answers.)
     */
    public QuestionnaireAnswers setEncounterTarget(Encounter value) { 
      this.encounterTarget = value;
      return this;
    }

    /**
     * @return {@link #group} (A group of questions to a possibly similarly grouped set of questions in the questionnaire answers.)
     */
    public GroupComponent getGroup() { 
      if (this.group == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create QuestionnaireAnswers.group");
        else if (Configuration.doAutoCreate())
          this.group = new GroupComponent(); // cc
      return this.group;
    }

    public boolean hasGroup() { 
      return this.group != null && !this.group.isEmpty();
    }

    /**
     * @param value {@link #group} (A group of questions to a possibly similarly grouped set of questions in the questionnaire answers.)
     */
    public QuestionnaireAnswers setGroup(GroupComponent value) { 
      this.group = value;
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "A business identifier assigned to a particular completed (or partially completed) questionnaire.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("questionnaire", "Reference(Questionnaire)", "Indicates the Questionnaire resource that defines the form for which answers are being provided.", 0, java.lang.Integer.MAX_VALUE, questionnaire));
        childrenList.add(new Property("status", "code", "The lifecycle status of the questionnaire answers as a whole.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("subject", "Reference(Any)", "The subject of the questionnaire answers.  This could be a patient, organization, practitioner, device, etc.  This is who/what the answers apply to, but is not necessarily the source of information.", 0, java.lang.Integer.MAX_VALUE, subject));
        childrenList.add(new Property("author", "Reference(Practitioner|Patient|RelatedPerson)", "Person who received the answers to the questions in the QuestionnaireAnswers and recorded them in the system.", 0, java.lang.Integer.MAX_VALUE, author));
        childrenList.add(new Property("authored", "dateTime", "The date and/or time that this version of the questionnaire answers was authored.", 0, java.lang.Integer.MAX_VALUE, authored));
        childrenList.add(new Property("source", "Reference(Patient|Practitioner|RelatedPerson)", "The person who answered the questions about the subject.", 0, java.lang.Integer.MAX_VALUE, source));
        childrenList.add(new Property("encounter", "Reference(Encounter)", "Encounter during which this set of questionnaire answers were collected. When there were multiple encounters, this is the one considered most relevant to the context of the answers.", 0, java.lang.Integer.MAX_VALUE, encounter));
        childrenList.add(new Property("group", "", "A group of questions to a possibly similarly grouped set of questions in the questionnaire answers.", 0, java.lang.Integer.MAX_VALUE, group));
      }

      public QuestionnaireAnswers copy() {
        QuestionnaireAnswers dst = new QuestionnaireAnswers();
        copyValues(dst);
        dst.identifier = identifier == null ? null : identifier.copy();
        dst.questionnaire = questionnaire == null ? null : questionnaire.copy();
        dst.status = status == null ? null : status.copy();
        dst.subject = subject == null ? null : subject.copy();
        dst.author = author == null ? null : author.copy();
        dst.authored = authored == null ? null : authored.copy();
        dst.source = source == null ? null : source.copy();
        dst.encounter = encounter == null ? null : encounter.copy();
        dst.group = group == null ? null : group.copy();
        return dst;
      }

      protected QuestionnaireAnswers typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof QuestionnaireAnswers))
          return false;
        QuestionnaireAnswers o = (QuestionnaireAnswers) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(questionnaire, o.questionnaire, true)
           && compareDeep(status, o.status, true) && compareDeep(subject, o.subject, true) && compareDeep(author, o.author, true)
           && compareDeep(authored, o.authored, true) && compareDeep(source, o.source, true) && compareDeep(encounter, o.encounter, true)
           && compareDeep(group, o.group, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof QuestionnaireAnswers))
          return false;
        QuestionnaireAnswers o = (QuestionnaireAnswers) other;
        return compareValues(status, o.status, true) && compareValues(authored, o.authored, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (questionnaire == null || questionnaire.isEmpty())
           && (status == null || status.isEmpty()) && (subject == null || subject.isEmpty()) && (author == null || author.isEmpty())
           && (authored == null || authored.isEmpty()) && (source == null || source.isEmpty()) && (encounter == null || encounter.isEmpty())
           && (group == null || group.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.QuestionnaireAnswers;
   }

  @SearchParamDefinition(name="authored", path="QuestionnaireAnswers.authored", description="When the questionnaire was authored", type="date" )
  public static final String SP_AUTHORED = "authored";
  @SearchParamDefinition(name = "questionnaire", path = "QuestionnaireAnswers.questionnaire", description = "The questionnaire the answers are provided for", type = "reference")
  public static final String SP_QUESTIONNAIRE = "questionnaire";
  @SearchParamDefinition(name="subject", path="QuestionnaireAnswers.subject", description="The subject of the questionnaire", type="reference" )
  public static final String SP_SUBJECT = "subject";
  @SearchParamDefinition(name = "author", path = "QuestionnaireAnswers.author", description = "The author of the questionnaire", type = "reference")
  public static final String SP_AUTHOR = "author";
  @SearchParamDefinition(name = "patient", path = "QuestionnaireAnswers.subject", description = "The patient that is the subject of the questionnaire", type = "reference")
  public static final String SP_PATIENT = "patient";
  @SearchParamDefinition(name="encounter", path="QuestionnaireAnswers.encounter", description="Encounter during which questionnaire was authored", type="reference" )
  public static final String SP_ENCOUNTER = "encounter";
  @SearchParamDefinition(name = "status", path = "QuestionnaireAnswers.status", description = "The status of the questionnaire answers", type = "token")
  public static final String SP_STATUS = "status";

}

