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
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ChildOrder;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Block;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.exceptions.FHIRException;
/**
 * A structured set of questions and their answers. The questions are ordered and grouped into coherent subsets, corresponding to the structure of the grouping of the questionnaire being responded to.
 */
@ResourceDef(name="QuestionnaireResponse", profile="http://hl7.org/fhir/Profile/QuestionnaireResponse")
public class QuestionnaireResponse extends DomainResource {

    public enum QuestionnaireResponseStatus {
        /**
         * This QuestionnaireResponse has been partially filled out with answers, but changes or additions are still expected to be made to it.
         */
        INPROGRESS, 
        /**
         * This QuestionnaireResponse has been filled out with answers, and the current content is regarded as definitive.
         */
        COMPLETED, 
        /**
         * This QuestionnaireResponse has been filled out with answers, then marked as complete, yet changes or additions have been made to it afterwards.
         */
        AMENDED, 
        /**
         * This QuestionnaireResponse was entered in error and voided.
         */
        ENTEREDINERROR, 
        /**
         * This QuestionnaireResponse has been partially filled out with answers, but has been abandoned. It is unknown whether changes or additions are expected to be made to it.
         */
        STOPPED, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static QuestionnaireResponseStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("in-progress".equals(codeString))
          return INPROGRESS;
        if ("completed".equals(codeString))
          return COMPLETED;
        if ("amended".equals(codeString))
          return AMENDED;
        if ("entered-in-error".equals(codeString))
          return ENTEREDINERROR;
        if ("stopped".equals(codeString))
          return STOPPED;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown QuestionnaireResponseStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case INPROGRESS: return "in-progress";
            case COMPLETED: return "completed";
            case AMENDED: return "amended";
            case ENTEREDINERROR: return "entered-in-error";
            case STOPPED: return "stopped";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case INPROGRESS: return "http://hl7.org/fhir/questionnaire-answers-status";
            case COMPLETED: return "http://hl7.org/fhir/questionnaire-answers-status";
            case AMENDED: return "http://hl7.org/fhir/questionnaire-answers-status";
            case ENTEREDINERROR: return "http://hl7.org/fhir/questionnaire-answers-status";
            case STOPPED: return "http://hl7.org/fhir/questionnaire-answers-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case INPROGRESS: return "This QuestionnaireResponse has been partially filled out with answers, but changes or additions are still expected to be made to it.";
            case COMPLETED: return "This QuestionnaireResponse has been filled out with answers, and the current content is regarded as definitive.";
            case AMENDED: return "This QuestionnaireResponse has been filled out with answers, then marked as complete, yet changes or additions have been made to it afterwards.";
            case ENTEREDINERROR: return "This QuestionnaireResponse was entered in error and voided.";
            case STOPPED: return "This QuestionnaireResponse has been partially filled out with answers, but has been abandoned. It is unknown whether changes or additions are expected to be made to it.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case INPROGRESS: return "In Progress";
            case COMPLETED: return "Completed";
            case AMENDED: return "Amended";
            case ENTEREDINERROR: return "Entered in Error";
            case STOPPED: return "Stopped";
            default: return "?";
          }
        }
    }

  public static class QuestionnaireResponseStatusEnumFactory implements EnumFactory<QuestionnaireResponseStatus> {
    public QuestionnaireResponseStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("in-progress".equals(codeString))
          return QuestionnaireResponseStatus.INPROGRESS;
        if ("completed".equals(codeString))
          return QuestionnaireResponseStatus.COMPLETED;
        if ("amended".equals(codeString))
          return QuestionnaireResponseStatus.AMENDED;
        if ("entered-in-error".equals(codeString))
          return QuestionnaireResponseStatus.ENTEREDINERROR;
        if ("stopped".equals(codeString))
          return QuestionnaireResponseStatus.STOPPED;
        throw new IllegalArgumentException("Unknown QuestionnaireResponseStatus code '"+codeString+"'");
        }
        public Enumeration<QuestionnaireResponseStatus> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<QuestionnaireResponseStatus>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("in-progress".equals(codeString))
          return new Enumeration<QuestionnaireResponseStatus>(this, QuestionnaireResponseStatus.INPROGRESS);
        if ("completed".equals(codeString))
          return new Enumeration<QuestionnaireResponseStatus>(this, QuestionnaireResponseStatus.COMPLETED);
        if ("amended".equals(codeString))
          return new Enumeration<QuestionnaireResponseStatus>(this, QuestionnaireResponseStatus.AMENDED);
        if ("entered-in-error".equals(codeString))
          return new Enumeration<QuestionnaireResponseStatus>(this, QuestionnaireResponseStatus.ENTEREDINERROR);
        if ("stopped".equals(codeString))
          return new Enumeration<QuestionnaireResponseStatus>(this, QuestionnaireResponseStatus.STOPPED);
        throw new FHIRException("Unknown QuestionnaireResponseStatus code '"+codeString+"'");
        }
    public String toCode(QuestionnaireResponseStatus code) {
      if (code == QuestionnaireResponseStatus.INPROGRESS)
        return "in-progress";
      if (code == QuestionnaireResponseStatus.COMPLETED)
        return "completed";
      if (code == QuestionnaireResponseStatus.AMENDED)
        return "amended";
      if (code == QuestionnaireResponseStatus.ENTEREDINERROR)
        return "entered-in-error";
      if (code == QuestionnaireResponseStatus.STOPPED)
        return "stopped";
      return "?";
      }
    public String toSystem(QuestionnaireResponseStatus code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class QuestionnaireResponseItemComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The item from the Questionnaire that corresponds to this item in the QuestionnaireResponse resource.
         */
        @Child(name = "linkId", type = {StringType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Pointer to specific item from Questionnaire", formalDefinition="The item from the Questionnaire that corresponds to this item in the QuestionnaireResponse resource." )
        protected StringType linkId;

        /**
         * A reference to an [[[ElementDefinition]]] that provides the details for the item.
         */
        @Child(name = "definition", type = {UriType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="ElementDefinition - details for the item", formalDefinition="A reference to an [[[ElementDefinition]]] that provides the details for the item." )
        protected UriType definition;

        /**
         * Text that is displayed above the contents of the group or as the text of the question being answered.
         */
        @Child(name = "text", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Name for group or question text", formalDefinition="Text that is displayed above the contents of the group or as the text of the question being answered." )
        protected StringType text;

        /**
         * More specific subject this section's answers are about, details the subject given in QuestionnaireResponse.
         */
        @Child(name = "subject", type = {Reference.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The subject this group's answers are about", formalDefinition="More specific subject this section's answers are about, details the subject given in QuestionnaireResponse." )
        protected Reference subject;

        /**
         * The actual object that is the target of the reference (More specific subject this section's answers are about, details the subject given in QuestionnaireResponse.)
         */
        protected Resource subjectTarget;

        /**
         * The respondent's answer(s) to the question.
         */
        @Child(name = "answer", type = {}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="The response(s) to the question", formalDefinition="The respondent's answer(s) to the question." )
        protected List<QuestionnaireResponseItemAnswerComponent> answer;

        /**
         * Questions or sub-groups nested beneath a question or group.
         */
        @Child(name = "item", type = {QuestionnaireResponseItemComponent.class}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Nested questionnaire response items", formalDefinition="Questions or sub-groups nested beneath a question or group." )
        protected List<QuestionnaireResponseItemComponent> item;

        private static final long serialVersionUID = -154786340L;

    /**
     * Constructor
     */
      public QuestionnaireResponseItemComponent() {
        super();
      }

    /**
     * Constructor
     */
      public QuestionnaireResponseItemComponent(StringType linkId) {
        super();
        this.linkId = linkId;
      }

        /**
         * @return {@link #linkId} (The item from the Questionnaire that corresponds to this item in the QuestionnaireResponse resource.). This is the underlying object with id, value and extensions. The accessor "getLinkId" gives direct access to the value
         */
        public StringType getLinkIdElement() { 
          if (this.linkId == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create QuestionnaireResponseItemComponent.linkId");
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
         * @param value {@link #linkId} (The item from the Questionnaire that corresponds to this item in the QuestionnaireResponse resource.). This is the underlying object with id, value and extensions. The accessor "getLinkId" gives direct access to the value
         */
        public QuestionnaireResponseItemComponent setLinkIdElement(StringType value) { 
          this.linkId = value;
          return this;
        }

        /**
         * @return The item from the Questionnaire that corresponds to this item in the QuestionnaireResponse resource.
         */
        public String getLinkId() { 
          return this.linkId == null ? null : this.linkId.getValue();
        }

        /**
         * @param value The item from the Questionnaire that corresponds to this item in the QuestionnaireResponse resource.
         */
        public QuestionnaireResponseItemComponent setLinkId(String value) { 
            if (this.linkId == null)
              this.linkId = new StringType();
            this.linkId.setValue(value);
          return this;
        }

        /**
         * @return {@link #definition} (A reference to an [[[ElementDefinition]]] that provides the details for the item.). This is the underlying object with id, value and extensions. The accessor "getDefinition" gives direct access to the value
         */
        public UriType getDefinitionElement() { 
          if (this.definition == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create QuestionnaireResponseItemComponent.definition");
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
         * @param value {@link #definition} (A reference to an [[[ElementDefinition]]] that provides the details for the item.). This is the underlying object with id, value and extensions. The accessor "getDefinition" gives direct access to the value
         */
        public QuestionnaireResponseItemComponent setDefinitionElement(UriType value) { 
          this.definition = value;
          return this;
        }

        /**
         * @return A reference to an [[[ElementDefinition]]] that provides the details for the item.
         */
        public String getDefinition() { 
          return this.definition == null ? null : this.definition.getValue();
        }

        /**
         * @param value A reference to an [[[ElementDefinition]]] that provides the details for the item.
         */
        public QuestionnaireResponseItemComponent setDefinition(String value) { 
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
         * @return {@link #text} (Text that is displayed above the contents of the group or as the text of the question being answered.). This is the underlying object with id, value and extensions. The accessor "getText" gives direct access to the value
         */
        public StringType getTextElement() { 
          if (this.text == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create QuestionnaireResponseItemComponent.text");
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
         * @param value {@link #text} (Text that is displayed above the contents of the group or as the text of the question being answered.). This is the underlying object with id, value and extensions. The accessor "getText" gives direct access to the value
         */
        public QuestionnaireResponseItemComponent setTextElement(StringType value) { 
          this.text = value;
          return this;
        }

        /**
         * @return Text that is displayed above the contents of the group or as the text of the question being answered.
         */
        public String getText() { 
          return this.text == null ? null : this.text.getValue();
        }

        /**
         * @param value Text that is displayed above the contents of the group or as the text of the question being answered.
         */
        public QuestionnaireResponseItemComponent setText(String value) { 
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
         * @return {@link #subject} (More specific subject this section's answers are about, details the subject given in QuestionnaireResponse.)
         */
        public Reference getSubject() { 
          if (this.subject == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create QuestionnaireResponseItemComponent.subject");
            else if (Configuration.doAutoCreate())
              this.subject = new Reference(); // cc
          return this.subject;
        }

        public boolean hasSubject() { 
          return this.subject != null && !this.subject.isEmpty();
        }

        /**
         * @param value {@link #subject} (More specific subject this section's answers are about, details the subject given in QuestionnaireResponse.)
         */
        public QuestionnaireResponseItemComponent setSubject(Reference value) { 
          this.subject = value;
          return this;
        }

        /**
         * @return {@link #subject} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (More specific subject this section's answers are about, details the subject given in QuestionnaireResponse.)
         */
        public Resource getSubjectTarget() { 
          return this.subjectTarget;
        }

        /**
         * @param value {@link #subject} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (More specific subject this section's answers are about, details the subject given in QuestionnaireResponse.)
         */
        public QuestionnaireResponseItemComponent setSubjectTarget(Resource value) { 
          this.subjectTarget = value;
          return this;
        }

        /**
         * @return {@link #answer} (The respondent's answer(s) to the question.)
         */
        public List<QuestionnaireResponseItemAnswerComponent> getAnswer() { 
          if (this.answer == null)
            this.answer = new ArrayList<QuestionnaireResponseItemAnswerComponent>();
          return this.answer;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public QuestionnaireResponseItemComponent setAnswer(List<QuestionnaireResponseItemAnswerComponent> theAnswer) { 
          this.answer = theAnswer;
          return this;
        }

        public boolean hasAnswer() { 
          if (this.answer == null)
            return false;
          for (QuestionnaireResponseItemAnswerComponent item : this.answer)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public QuestionnaireResponseItemAnswerComponent addAnswer() { //3
          QuestionnaireResponseItemAnswerComponent t = new QuestionnaireResponseItemAnswerComponent();
          if (this.answer == null)
            this.answer = new ArrayList<QuestionnaireResponseItemAnswerComponent>();
          this.answer.add(t);
          return t;
        }

        public QuestionnaireResponseItemComponent addAnswer(QuestionnaireResponseItemAnswerComponent t) { //3
          if (t == null)
            return this;
          if (this.answer == null)
            this.answer = new ArrayList<QuestionnaireResponseItemAnswerComponent>();
          this.answer.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #answer}, creating it if it does not already exist
         */
        public QuestionnaireResponseItemAnswerComponent getAnswerFirstRep() { 
          if (getAnswer().isEmpty()) {
            addAnswer();
          }
          return getAnswer().get(0);
        }

        /**
         * @return {@link #item} (Questions or sub-groups nested beneath a question or group.)
         */
        public List<QuestionnaireResponseItemComponent> getItem() { 
          if (this.item == null)
            this.item = new ArrayList<QuestionnaireResponseItemComponent>();
          return this.item;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public QuestionnaireResponseItemComponent setItem(List<QuestionnaireResponseItemComponent> theItem) { 
          this.item = theItem;
          return this;
        }

        public boolean hasItem() { 
          if (this.item == null)
            return false;
          for (QuestionnaireResponseItemComponent item : this.item)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public QuestionnaireResponseItemComponent addItem() { //3
          QuestionnaireResponseItemComponent t = new QuestionnaireResponseItemComponent();
          if (this.item == null)
            this.item = new ArrayList<QuestionnaireResponseItemComponent>();
          this.item.add(t);
          return t;
        }

        public QuestionnaireResponseItemComponent addItem(QuestionnaireResponseItemComponent t) { //3
          if (t == null)
            return this;
          if (this.item == null)
            this.item = new ArrayList<QuestionnaireResponseItemComponent>();
          this.item.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #item}, creating it if it does not already exist
         */
        public QuestionnaireResponseItemComponent getItemFirstRep() { 
          if (getItem().isEmpty()) {
            addItem();
          }
          return getItem().get(0);
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("linkId", "string", "The item from the Questionnaire that corresponds to this item in the QuestionnaireResponse resource.", 0, java.lang.Integer.MAX_VALUE, linkId));
          childrenList.add(new Property("definition", "uri", "A reference to an [[[ElementDefinition]]] that provides the details for the item.", 0, java.lang.Integer.MAX_VALUE, definition));
          childrenList.add(new Property("text", "string", "Text that is displayed above the contents of the group or as the text of the question being answered.", 0, java.lang.Integer.MAX_VALUE, text));
          childrenList.add(new Property("subject", "Reference(Any)", "More specific subject this section's answers are about, details the subject given in QuestionnaireResponse.", 0, java.lang.Integer.MAX_VALUE, subject));
          childrenList.add(new Property("answer", "", "The respondent's answer(s) to the question.", 0, java.lang.Integer.MAX_VALUE, answer));
          childrenList.add(new Property("item", "@QuestionnaireResponse.item", "Questions or sub-groups nested beneath a question or group.", 0, java.lang.Integer.MAX_VALUE, item));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1102667083: /*linkId*/ return this.linkId == null ? new Base[0] : new Base[] {this.linkId}; // StringType
        case -1014418093: /*definition*/ return this.definition == null ? new Base[0] : new Base[] {this.definition}; // UriType
        case 3556653: /*text*/ return this.text == null ? new Base[0] : new Base[] {this.text}; // StringType
        case -1867885268: /*subject*/ return this.subject == null ? new Base[0] : new Base[] {this.subject}; // Reference
        case -1412808770: /*answer*/ return this.answer == null ? new Base[0] : this.answer.toArray(new Base[this.answer.size()]); // QuestionnaireResponseItemAnswerComponent
        case 3242771: /*item*/ return this.item == null ? new Base[0] : this.item.toArray(new Base[this.item.size()]); // QuestionnaireResponseItemComponent
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
        case 3556653: // text
          this.text = castToString(value); // StringType
          return value;
        case -1867885268: // subject
          this.subject = castToReference(value); // Reference
          return value;
        case -1412808770: // answer
          this.getAnswer().add((QuestionnaireResponseItemAnswerComponent) value); // QuestionnaireResponseItemAnswerComponent
          return value;
        case 3242771: // item
          this.getItem().add((QuestionnaireResponseItemComponent) value); // QuestionnaireResponseItemComponent
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
        } else if (name.equals("text")) {
          this.text = castToString(value); // StringType
        } else if (name.equals("subject")) {
          this.subject = castToReference(value); // Reference
        } else if (name.equals("answer")) {
          this.getAnswer().add((QuestionnaireResponseItemAnswerComponent) value);
        } else if (name.equals("item")) {
          this.getItem().add((QuestionnaireResponseItemComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1102667083:  return getLinkIdElement();
        case -1014418093:  return getDefinitionElement();
        case 3556653:  return getTextElement();
        case -1867885268:  return getSubject(); 
        case -1412808770:  return addAnswer(); 
        case 3242771:  return addItem(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1102667083: /*linkId*/ return new String[] {"string"};
        case -1014418093: /*definition*/ return new String[] {"uri"};
        case 3556653: /*text*/ return new String[] {"string"};
        case -1867885268: /*subject*/ return new String[] {"Reference"};
        case -1412808770: /*answer*/ return new String[] {};
        case 3242771: /*item*/ return new String[] {"@QuestionnaireResponse.item"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("linkId")) {
          throw new FHIRException("Cannot call addChild on a primitive type QuestionnaireResponse.linkId");
        }
        else if (name.equals("definition")) {
          throw new FHIRException("Cannot call addChild on a primitive type QuestionnaireResponse.definition");
        }
        else if (name.equals("text")) {
          throw new FHIRException("Cannot call addChild on a primitive type QuestionnaireResponse.text");
        }
        else if (name.equals("subject")) {
          this.subject = new Reference();
          return this.subject;
        }
        else if (name.equals("answer")) {
          return addAnswer();
        }
        else if (name.equals("item")) {
          return addItem();
        }
        else
          return super.addChild(name);
      }

      public QuestionnaireResponseItemComponent copy() {
        QuestionnaireResponseItemComponent dst = new QuestionnaireResponseItemComponent();
        copyValues(dst);
        dst.linkId = linkId == null ? null : linkId.copy();
        dst.definition = definition == null ? null : definition.copy();
        dst.text = text == null ? null : text.copy();
        dst.subject = subject == null ? null : subject.copy();
        if (answer != null) {
          dst.answer = new ArrayList<QuestionnaireResponseItemAnswerComponent>();
          for (QuestionnaireResponseItemAnswerComponent i : answer)
            dst.answer.add(i.copy());
        };
        if (item != null) {
          dst.item = new ArrayList<QuestionnaireResponseItemComponent>();
          for (QuestionnaireResponseItemComponent i : item)
            dst.item.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof QuestionnaireResponseItemComponent))
          return false;
        QuestionnaireResponseItemComponent o = (QuestionnaireResponseItemComponent) other;
        return compareDeep(linkId, o.linkId, true) && compareDeep(definition, o.definition, true) && compareDeep(text, o.text, true)
           && compareDeep(subject, o.subject, true) && compareDeep(answer, o.answer, true) && compareDeep(item, o.item, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof QuestionnaireResponseItemComponent))
          return false;
        QuestionnaireResponseItemComponent o = (QuestionnaireResponseItemComponent) other;
        return compareValues(linkId, o.linkId, true) && compareValues(definition, o.definition, true) && compareValues(text, o.text, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(linkId, definition, text
          , subject, answer, item);
      }

  public String fhirType() {
    return "QuestionnaireResponse.item";

  }

  }

    @Block()
    public static class QuestionnaireResponseItemAnswerComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The answer (or one of the answers) provided by the respondent to the question.
         */
        @Child(name = "value", type = {BooleanType.class, DecimalType.class, IntegerType.class, DateType.class, DateTimeType.class, TimeType.class, StringType.class, UriType.class, Attachment.class, Coding.class, Quantity.class, Reference.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Single-valued answer to the question", formalDefinition="The answer (or one of the answers) provided by the respondent to the question." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/questionnaire-answers")
        protected Type value;

        /**
         * Nested groups and/or questions found within this particular answer.
         */
        @Child(name = "item", type = {QuestionnaireResponseItemComponent.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Nested groups and questions", formalDefinition="Nested groups and/or questions found within this particular answer." )
        protected List<QuestionnaireResponseItemComponent> item;

        private static final long serialVersionUID = 2052422636L;

    /**
     * Constructor
     */
      public QuestionnaireResponseItemAnswerComponent() {
        super();
      }

        /**
         * @return {@link #value} (The answer (or one of the answers) provided by the respondent to the question.)
         */
        public Type getValue() { 
          return this.value;
        }

        /**
         * @return {@link #value} (The answer (or one of the answers) provided by the respondent to the question.)
         */
        public BooleanType getValueBooleanType() throws FHIRException { 
          if (!(this.value instanceof BooleanType))
            throw new FHIRException("Type mismatch: the type BooleanType was expected, but "+this.value.getClass().getName()+" was encountered");
          return (BooleanType) this.value;
        }

        public boolean hasValueBooleanType() { 
          return this.value instanceof BooleanType;
        }

        /**
         * @return {@link #value} (The answer (or one of the answers) provided by the respondent to the question.)
         */
        public DecimalType getValueDecimalType() throws FHIRException { 
          if (!(this.value instanceof DecimalType))
            throw new FHIRException("Type mismatch: the type DecimalType was expected, but "+this.value.getClass().getName()+" was encountered");
          return (DecimalType) this.value;
        }

        public boolean hasValueDecimalType() { 
          return this.value instanceof DecimalType;
        }

        /**
         * @return {@link #value} (The answer (or one of the answers) provided by the respondent to the question.)
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
         * @return {@link #value} (The answer (or one of the answers) provided by the respondent to the question.)
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
         * @return {@link #value} (The answer (or one of the answers) provided by the respondent to the question.)
         */
        public DateTimeType getValueDateTimeType() throws FHIRException { 
          if (!(this.value instanceof DateTimeType))
            throw new FHIRException("Type mismatch: the type DateTimeType was expected, but "+this.value.getClass().getName()+" was encountered");
          return (DateTimeType) this.value;
        }

        public boolean hasValueDateTimeType() { 
          return this.value instanceof DateTimeType;
        }

        /**
         * @return {@link #value} (The answer (or one of the answers) provided by the respondent to the question.)
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
         * @return {@link #value} (The answer (or one of the answers) provided by the respondent to the question.)
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
         * @return {@link #value} (The answer (or one of the answers) provided by the respondent to the question.)
         */
        public UriType getValueUriType() throws FHIRException { 
          if (!(this.value instanceof UriType))
            throw new FHIRException("Type mismatch: the type UriType was expected, but "+this.value.getClass().getName()+" was encountered");
          return (UriType) this.value;
        }

        public boolean hasValueUriType() { 
          return this.value instanceof UriType;
        }

        /**
         * @return {@link #value} (The answer (or one of the answers) provided by the respondent to the question.)
         */
        public Attachment getValueAttachment() throws FHIRException { 
          if (!(this.value instanceof Attachment))
            throw new FHIRException("Type mismatch: the type Attachment was expected, but "+this.value.getClass().getName()+" was encountered");
          return (Attachment) this.value;
        }

        public boolean hasValueAttachment() { 
          return this.value instanceof Attachment;
        }

        /**
         * @return {@link #value} (The answer (or one of the answers) provided by the respondent to the question.)
         */
        public Coding getValueCoding() throws FHIRException { 
          if (!(this.value instanceof Coding))
            throw new FHIRException("Type mismatch: the type Coding was expected, but "+this.value.getClass().getName()+" was encountered");
          return (Coding) this.value;
        }

        public boolean hasValueCoding() { 
          return this.value instanceof Coding;
        }

        /**
         * @return {@link #value} (The answer (or one of the answers) provided by the respondent to the question.)
         */
        public Quantity getValueQuantity() throws FHIRException { 
          if (!(this.value instanceof Quantity))
            throw new FHIRException("Type mismatch: the type Quantity was expected, but "+this.value.getClass().getName()+" was encountered");
          return (Quantity) this.value;
        }

        public boolean hasValueQuantity() { 
          return this.value instanceof Quantity;
        }

        /**
         * @return {@link #value} (The answer (or one of the answers) provided by the respondent to the question.)
         */
        public Reference getValueReference() throws FHIRException { 
          if (!(this.value instanceof Reference))
            throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.value.getClass().getName()+" was encountered");
          return (Reference) this.value;
        }

        public boolean hasValueReference() { 
          return this.value instanceof Reference;
        }

        public boolean hasValue() { 
          return this.value != null && !this.value.isEmpty();
        }

        /**
         * @param value {@link #value} (The answer (or one of the answers) provided by the respondent to the question.)
         */
        public QuestionnaireResponseItemAnswerComponent setValue(Type value) { 
          this.value = value;
          return this;
        }

        /**
         * @return {@link #item} (Nested groups and/or questions found within this particular answer.)
         */
        public List<QuestionnaireResponseItemComponent> getItem() { 
          if (this.item == null)
            this.item = new ArrayList<QuestionnaireResponseItemComponent>();
          return this.item;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public QuestionnaireResponseItemAnswerComponent setItem(List<QuestionnaireResponseItemComponent> theItem) { 
          this.item = theItem;
          return this;
        }

        public boolean hasItem() { 
          if (this.item == null)
            return false;
          for (QuestionnaireResponseItemComponent item : this.item)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public QuestionnaireResponseItemComponent addItem() { //3
          QuestionnaireResponseItemComponent t = new QuestionnaireResponseItemComponent();
          if (this.item == null)
            this.item = new ArrayList<QuestionnaireResponseItemComponent>();
          this.item.add(t);
          return t;
        }

        public QuestionnaireResponseItemAnswerComponent addItem(QuestionnaireResponseItemComponent t) { //3
          if (t == null)
            return this;
          if (this.item == null)
            this.item = new ArrayList<QuestionnaireResponseItemComponent>();
          this.item.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #item}, creating it if it does not already exist
         */
        public QuestionnaireResponseItemComponent getItemFirstRep() { 
          if (getItem().isEmpty()) {
            addItem();
          }
          return getItem().get(0);
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("value[x]", "boolean|decimal|integer|date|dateTime|time|string|uri|Attachment|Coding|Quantity|Reference(Any)", "The answer (or one of the answers) provided by the respondent to the question.", 0, java.lang.Integer.MAX_VALUE, value));
          childrenList.add(new Property("item", "@QuestionnaireResponse.item", "Nested groups and/or questions found within this particular answer.", 0, java.lang.Integer.MAX_VALUE, item));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 111972721: /*value*/ return this.value == null ? new Base[0] : new Base[] {this.value}; // Type
        case 3242771: /*item*/ return this.item == null ? new Base[0] : this.item.toArray(new Base[this.item.size()]); // QuestionnaireResponseItemComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 111972721: // value
          this.value = castToType(value); // Type
          return value;
        case 3242771: // item
          this.getItem().add((QuestionnaireResponseItemComponent) value); // QuestionnaireResponseItemComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("value[x]")) {
          this.value = castToType(value); // Type
        } else if (name.equals("item")) {
          this.getItem().add((QuestionnaireResponseItemComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1410166417:  return getValue(); 
        case 111972721:  return getValue(); 
        case 3242771:  return addItem(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 111972721: /*value*/ return new String[] {"boolean", "decimal", "integer", "date", "dateTime", "time", "string", "uri", "Attachment", "Coding", "Quantity", "Reference"};
        case 3242771: /*item*/ return new String[] {"@QuestionnaireResponse.item"};
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
        else if (name.equals("item")) {
          return addItem();
        }
        else
          return super.addChild(name);
      }

      public QuestionnaireResponseItemAnswerComponent copy() {
        QuestionnaireResponseItemAnswerComponent dst = new QuestionnaireResponseItemAnswerComponent();
        copyValues(dst);
        dst.value = value == null ? null : value.copy();
        if (item != null) {
          dst.item = new ArrayList<QuestionnaireResponseItemComponent>();
          for (QuestionnaireResponseItemComponent i : item)
            dst.item.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof QuestionnaireResponseItemAnswerComponent))
          return false;
        QuestionnaireResponseItemAnswerComponent o = (QuestionnaireResponseItemAnswerComponent) other;
        return compareDeep(value, o.value, true) && compareDeep(item, o.item, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof QuestionnaireResponseItemAnswerComponent))
          return false;
        QuestionnaireResponseItemAnswerComponent o = (QuestionnaireResponseItemAnswerComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(value, item);
      }

  public String fhirType() {
    return "QuestionnaireResponse.item.answer";

  }

  }

    /**
     * A business identifier assigned to a particular completed (or partially completed) questionnaire.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Unique id for this set of answers", formalDefinition="A business identifier assigned to a particular completed (or partially completed) questionnaire." )
    protected Identifier identifier;

    /**
     * The order, proposal or plan that is fulfilled in whole or in part by this QuestionnaireResponse.  For example, a ProcedureRequest seeking an intake assessment or a decision support recommendation to assess for post-partum depression.
     */
    @Child(name = "basedOn", type = {ReferralRequest.class, CarePlan.class, ProcedureRequest.class}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Request fulfilled by this QuestionnaireResponse", formalDefinition="The order, proposal or plan that is fulfilled in whole or in part by this QuestionnaireResponse.  For example, a ProcedureRequest seeking an intake assessment or a decision support recommendation to assess for post-partum depression." )
    protected List<Reference> basedOn;
    /**
     * The actual objects that are the target of the reference (The order, proposal or plan that is fulfilled in whole or in part by this QuestionnaireResponse.  For example, a ProcedureRequest seeking an intake assessment or a decision support recommendation to assess for post-partum depression.)
     */
    protected List<Resource> basedOnTarget;


    /**
     * A procedure or observation that this questionnaire was performed as part of the execution of.  For example, the surgery a checklist was executed as part of.
     */
    @Child(name = "parent", type = {Observation.class, Procedure.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Part of this action", formalDefinition="A procedure or observation that this questionnaire was performed as part of the execution of.  For example, the surgery a checklist was executed as part of." )
    protected List<Reference> parent;
    /**
     * The actual objects that are the target of the reference (A procedure or observation that this questionnaire was performed as part of the execution of.  For example, the surgery a checklist was executed as part of.)
     */
    protected List<Resource> parentTarget;


    /**
     * The Questionnaire that defines and organizes the questions for which answers are being provided.
     */
    @Child(name = "questionnaire", type = {Questionnaire.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Form being answered", formalDefinition="The Questionnaire that defines and organizes the questions for which answers are being provided." )
    protected Reference questionnaire;

    /**
     * The actual object that is the target of the reference (The Questionnaire that defines and organizes the questions for which answers are being provided.)
     */
    protected Questionnaire questionnaireTarget;

    /**
     * The position of the questionnaire response within its overall lifecycle.
     */
    @Child(name = "status", type = {CodeType.class}, order=4, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="in-progress | completed | amended | entered-in-error | stopped", formalDefinition="The position of the questionnaire response within its overall lifecycle." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/questionnaire-answers-status")
    protected Enumeration<QuestionnaireResponseStatus> status;

    /**
     * The subject of the questionnaire response.  This could be a patient, organization, practitioner, device, etc.  This is who/what the answers apply to, but is not necessarily the source of information.
     */
    @Child(name = "subject", type = {Reference.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The subject of the questions", formalDefinition="The subject of the questionnaire response.  This could be a patient, organization, practitioner, device, etc.  This is who/what the answers apply to, but is not necessarily the source of information." )
    protected Reference subject;

    /**
     * The actual object that is the target of the reference (The subject of the questionnaire response.  This could be a patient, organization, practitioner, device, etc.  This is who/what the answers apply to, but is not necessarily the source of information.)
     */
    protected Resource subjectTarget;

    /**
     * The encounter or episode of care with primary association to the questionnaire response.
     */
    @Child(name = "context", type = {Encounter.class, EpisodeOfCare.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Encounter or Episode during which questionnaire was completed", formalDefinition="The encounter or episode of care with primary association to the questionnaire response." )
    protected Reference context;

    /**
     * The actual object that is the target of the reference (The encounter or episode of care with primary association to the questionnaire response.)
     */
    protected Resource contextTarget;

    /**
     * The date and/or time that this set of answers were last changed.
     */
    @Child(name = "authored", type = {DateTimeType.class}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Date the answers were gathered", formalDefinition="The date and/or time that this set of answers were last changed." )
    protected DateTimeType authored;

    /**
     * Person who received the answers to the questions in the QuestionnaireResponse and recorded them in the system.
     */
    @Child(name = "author", type = {Device.class, Practitioner.class, Patient.class, RelatedPerson.class}, order=8, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Person who received and recorded the answers", formalDefinition="Person who received the answers to the questions in the QuestionnaireResponse and recorded them in the system." )
    protected Reference author;

    /**
     * The actual object that is the target of the reference (Person who received the answers to the questions in the QuestionnaireResponse and recorded them in the system.)
     */
    protected Resource authorTarget;

    /**
     * The person who answered the questions about the subject.
     */
    @Child(name = "source", type = {Patient.class, Practitioner.class, RelatedPerson.class}, order=9, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The person who answered the questions", formalDefinition="The person who answered the questions about the subject." )
    protected Reference source;

    /**
     * The actual object that is the target of the reference (The person who answered the questions about the subject.)
     */
    protected Resource sourceTarget;

    /**
     * A group or question item from the original questionnaire for which answers are provided.
     */
    @Child(name = "item", type = {}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Groups and questions", formalDefinition="A group or question item from the original questionnaire for which answers are provided." )
    protected List<QuestionnaireResponseItemComponent> item;

    private static final long serialVersionUID = -1559552776L;

  /**
   * Constructor
   */
    public QuestionnaireResponse() {
      super();
    }

  /**
   * Constructor
   */
    public QuestionnaireResponse(Enumeration<QuestionnaireResponseStatus> status) {
      super();
      this.status = status;
    }

    /**
     * @return {@link #identifier} (A business identifier assigned to a particular completed (or partially completed) questionnaire.)
     */
    public Identifier getIdentifier() { 
      if (this.identifier == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create QuestionnaireResponse.identifier");
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
    public QuestionnaireResponse setIdentifier(Identifier value) { 
      this.identifier = value;
      return this;
    }

    /**
     * @return {@link #basedOn} (The order, proposal or plan that is fulfilled in whole or in part by this QuestionnaireResponse.  For example, a ProcedureRequest seeking an intake assessment or a decision support recommendation to assess for post-partum depression.)
     */
    public List<Reference> getBasedOn() { 
      if (this.basedOn == null)
        this.basedOn = new ArrayList<Reference>();
      return this.basedOn;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public QuestionnaireResponse setBasedOn(List<Reference> theBasedOn) { 
      this.basedOn = theBasedOn;
      return this;
    }

    public boolean hasBasedOn() { 
      if (this.basedOn == null)
        return false;
      for (Reference item : this.basedOn)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addBasedOn() { //3
      Reference t = new Reference();
      if (this.basedOn == null)
        this.basedOn = new ArrayList<Reference>();
      this.basedOn.add(t);
      return t;
    }

    public QuestionnaireResponse addBasedOn(Reference t) { //3
      if (t == null)
        return this;
      if (this.basedOn == null)
        this.basedOn = new ArrayList<Reference>();
      this.basedOn.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #basedOn}, creating it if it does not already exist
     */
    public Reference getBasedOnFirstRep() { 
      if (getBasedOn().isEmpty()) {
        addBasedOn();
      }
      return getBasedOn().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Resource> getBasedOnTarget() { 
      if (this.basedOnTarget == null)
        this.basedOnTarget = new ArrayList<Resource>();
      return this.basedOnTarget;
    }

    /**
     * @return {@link #parent} (A procedure or observation that this questionnaire was performed as part of the execution of.  For example, the surgery a checklist was executed as part of.)
     */
    public List<Reference> getParent() { 
      if (this.parent == null)
        this.parent = new ArrayList<Reference>();
      return this.parent;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public QuestionnaireResponse setParent(List<Reference> theParent) { 
      this.parent = theParent;
      return this;
    }

    public boolean hasParent() { 
      if (this.parent == null)
        return false;
      for (Reference item : this.parent)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addParent() { //3
      Reference t = new Reference();
      if (this.parent == null)
        this.parent = new ArrayList<Reference>();
      this.parent.add(t);
      return t;
    }

    public QuestionnaireResponse addParent(Reference t) { //3
      if (t == null)
        return this;
      if (this.parent == null)
        this.parent = new ArrayList<Reference>();
      this.parent.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #parent}, creating it if it does not already exist
     */
    public Reference getParentFirstRep() { 
      if (getParent().isEmpty()) {
        addParent();
      }
      return getParent().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Resource> getParentTarget() { 
      if (this.parentTarget == null)
        this.parentTarget = new ArrayList<Resource>();
      return this.parentTarget;
    }

    /**
     * @return {@link #questionnaire} (The Questionnaire that defines and organizes the questions for which answers are being provided.)
     */
    public Reference getQuestionnaire() { 
      if (this.questionnaire == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create QuestionnaireResponse.questionnaire");
        else if (Configuration.doAutoCreate())
          this.questionnaire = new Reference(); // cc
      return this.questionnaire;
    }

    public boolean hasQuestionnaire() { 
      return this.questionnaire != null && !this.questionnaire.isEmpty();
    }

    /**
     * @param value {@link #questionnaire} (The Questionnaire that defines and organizes the questions for which answers are being provided.)
     */
    public QuestionnaireResponse setQuestionnaire(Reference value) { 
      this.questionnaire = value;
      return this;
    }

    /**
     * @return {@link #questionnaire} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The Questionnaire that defines and organizes the questions for which answers are being provided.)
     */
    public Questionnaire getQuestionnaireTarget() { 
      if (this.questionnaireTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create QuestionnaireResponse.questionnaire");
        else if (Configuration.doAutoCreate())
          this.questionnaireTarget = new Questionnaire(); // aa
      return this.questionnaireTarget;
    }

    /**
     * @param value {@link #questionnaire} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The Questionnaire that defines and organizes the questions for which answers are being provided.)
     */
    public QuestionnaireResponse setQuestionnaireTarget(Questionnaire value) { 
      this.questionnaireTarget = value;
      return this;
    }

    /**
     * @return {@link #status} (The position of the questionnaire response within its overall lifecycle.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<QuestionnaireResponseStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create QuestionnaireResponse.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<QuestionnaireResponseStatus>(new QuestionnaireResponseStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (The position of the questionnaire response within its overall lifecycle.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public QuestionnaireResponse setStatusElement(Enumeration<QuestionnaireResponseStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The position of the questionnaire response within its overall lifecycle.
     */
    public QuestionnaireResponseStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The position of the questionnaire response within its overall lifecycle.
     */
    public QuestionnaireResponse setStatus(QuestionnaireResponseStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<QuestionnaireResponseStatus>(new QuestionnaireResponseStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #subject} (The subject of the questionnaire response.  This could be a patient, organization, practitioner, device, etc.  This is who/what the answers apply to, but is not necessarily the source of information.)
     */
    public Reference getSubject() { 
      if (this.subject == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create QuestionnaireResponse.subject");
        else if (Configuration.doAutoCreate())
          this.subject = new Reference(); // cc
      return this.subject;
    }

    public boolean hasSubject() { 
      return this.subject != null && !this.subject.isEmpty();
    }

    /**
     * @param value {@link #subject} (The subject of the questionnaire response.  This could be a patient, organization, practitioner, device, etc.  This is who/what the answers apply to, but is not necessarily the source of information.)
     */
    public QuestionnaireResponse setSubject(Reference value) { 
      this.subject = value;
      return this;
    }

    /**
     * @return {@link #subject} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The subject of the questionnaire response.  This could be a patient, organization, practitioner, device, etc.  This is who/what the answers apply to, but is not necessarily the source of information.)
     */
    public Resource getSubjectTarget() { 
      return this.subjectTarget;
    }

    /**
     * @param value {@link #subject} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The subject of the questionnaire response.  This could be a patient, organization, practitioner, device, etc.  This is who/what the answers apply to, but is not necessarily the source of information.)
     */
    public QuestionnaireResponse setSubjectTarget(Resource value) { 
      this.subjectTarget = value;
      return this;
    }

    /**
     * @return {@link #context} (The encounter or episode of care with primary association to the questionnaire response.)
     */
    public Reference getContext() { 
      if (this.context == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create QuestionnaireResponse.context");
        else if (Configuration.doAutoCreate())
          this.context = new Reference(); // cc
      return this.context;
    }

    public boolean hasContext() { 
      return this.context != null && !this.context.isEmpty();
    }

    /**
     * @param value {@link #context} (The encounter or episode of care with primary association to the questionnaire response.)
     */
    public QuestionnaireResponse setContext(Reference value) { 
      this.context = value;
      return this;
    }

    /**
     * @return {@link #context} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The encounter or episode of care with primary association to the questionnaire response.)
     */
    public Resource getContextTarget() { 
      return this.contextTarget;
    }

    /**
     * @param value {@link #context} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The encounter or episode of care with primary association to the questionnaire response.)
     */
    public QuestionnaireResponse setContextTarget(Resource value) { 
      this.contextTarget = value;
      return this;
    }

    /**
     * @return {@link #authored} (The date and/or time that this set of answers were last changed.). This is the underlying object with id, value and extensions. The accessor "getAuthored" gives direct access to the value
     */
    public DateTimeType getAuthoredElement() { 
      if (this.authored == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create QuestionnaireResponse.authored");
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
     * @param value {@link #authored} (The date and/or time that this set of answers were last changed.). This is the underlying object with id, value and extensions. The accessor "getAuthored" gives direct access to the value
     */
    public QuestionnaireResponse setAuthoredElement(DateTimeType value) { 
      this.authored = value;
      return this;
    }

    /**
     * @return The date and/or time that this set of answers were last changed.
     */
    public Date getAuthored() { 
      return this.authored == null ? null : this.authored.getValue();
    }

    /**
     * @param value The date and/or time that this set of answers were last changed.
     */
    public QuestionnaireResponse setAuthored(Date value) { 
      if (value == null)
        this.authored = null;
      else {
        if (this.authored == null)
          this.authored = new DateTimeType();
        this.authored.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #author} (Person who received the answers to the questions in the QuestionnaireResponse and recorded them in the system.)
     */
    public Reference getAuthor() { 
      if (this.author == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create QuestionnaireResponse.author");
        else if (Configuration.doAutoCreate())
          this.author = new Reference(); // cc
      return this.author;
    }

    public boolean hasAuthor() { 
      return this.author != null && !this.author.isEmpty();
    }

    /**
     * @param value {@link #author} (Person who received the answers to the questions in the QuestionnaireResponse and recorded them in the system.)
     */
    public QuestionnaireResponse setAuthor(Reference value) { 
      this.author = value;
      return this;
    }

    /**
     * @return {@link #author} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Person who received the answers to the questions in the QuestionnaireResponse and recorded them in the system.)
     */
    public Resource getAuthorTarget() { 
      return this.authorTarget;
    }

    /**
     * @param value {@link #author} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Person who received the answers to the questions in the QuestionnaireResponse and recorded them in the system.)
     */
    public QuestionnaireResponse setAuthorTarget(Resource value) { 
      this.authorTarget = value;
      return this;
    }

    /**
     * @return {@link #source} (The person who answered the questions about the subject.)
     */
    public Reference getSource() { 
      if (this.source == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create QuestionnaireResponse.source");
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
    public QuestionnaireResponse setSource(Reference value) { 
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
    public QuestionnaireResponse setSourceTarget(Resource value) { 
      this.sourceTarget = value;
      return this;
    }

    /**
     * @return {@link #item} (A group or question item from the original questionnaire for which answers are provided.)
     */
    public List<QuestionnaireResponseItemComponent> getItem() { 
      if (this.item == null)
        this.item = new ArrayList<QuestionnaireResponseItemComponent>();
      return this.item;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public QuestionnaireResponse setItem(List<QuestionnaireResponseItemComponent> theItem) { 
      this.item = theItem;
      return this;
    }

    public boolean hasItem() { 
      if (this.item == null)
        return false;
      for (QuestionnaireResponseItemComponent item : this.item)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public QuestionnaireResponseItemComponent addItem() { //3
      QuestionnaireResponseItemComponent t = new QuestionnaireResponseItemComponent();
      if (this.item == null)
        this.item = new ArrayList<QuestionnaireResponseItemComponent>();
      this.item.add(t);
      return t;
    }

    public QuestionnaireResponse addItem(QuestionnaireResponseItemComponent t) { //3
      if (t == null)
        return this;
      if (this.item == null)
        this.item = new ArrayList<QuestionnaireResponseItemComponent>();
      this.item.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #item}, creating it if it does not already exist
     */
    public QuestionnaireResponseItemComponent getItemFirstRep() { 
      if (getItem().isEmpty()) {
        addItem();
      }
      return getItem().get(0);
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "A business identifier assigned to a particular completed (or partially completed) questionnaire.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("basedOn", "Reference(ReferralRequest|CarePlan|ProcedureRequest)", "The order, proposal or plan that is fulfilled in whole or in part by this QuestionnaireResponse.  For example, a ProcedureRequest seeking an intake assessment or a decision support recommendation to assess for post-partum depression.", 0, java.lang.Integer.MAX_VALUE, basedOn));
        childrenList.add(new Property("parent", "Reference(Observation|Procedure)", "A procedure or observation that this questionnaire was performed as part of the execution of.  For example, the surgery a checklist was executed as part of.", 0, java.lang.Integer.MAX_VALUE, parent));
        childrenList.add(new Property("questionnaire", "Reference(Questionnaire)", "The Questionnaire that defines and organizes the questions for which answers are being provided.", 0, java.lang.Integer.MAX_VALUE, questionnaire));
        childrenList.add(new Property("status", "code", "The position of the questionnaire response within its overall lifecycle.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("subject", "Reference(Any)", "The subject of the questionnaire response.  This could be a patient, organization, practitioner, device, etc.  This is who/what the answers apply to, but is not necessarily the source of information.", 0, java.lang.Integer.MAX_VALUE, subject));
        childrenList.add(new Property("context", "Reference(Encounter|EpisodeOfCare)", "The encounter or episode of care with primary association to the questionnaire response.", 0, java.lang.Integer.MAX_VALUE, context));
        childrenList.add(new Property("authored", "dateTime", "The date and/or time that this set of answers were last changed.", 0, java.lang.Integer.MAX_VALUE, authored));
        childrenList.add(new Property("author", "Reference(Device|Practitioner|Patient|RelatedPerson)", "Person who received the answers to the questions in the QuestionnaireResponse and recorded them in the system.", 0, java.lang.Integer.MAX_VALUE, author));
        childrenList.add(new Property("source", "Reference(Patient|Practitioner|RelatedPerson)", "The person who answered the questions about the subject.", 0, java.lang.Integer.MAX_VALUE, source));
        childrenList.add(new Property("item", "", "A group or question item from the original questionnaire for which answers are provided.", 0, java.lang.Integer.MAX_VALUE, item));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : new Base[] {this.identifier}; // Identifier
        case -332612366: /*basedOn*/ return this.basedOn == null ? new Base[0] : this.basedOn.toArray(new Base[this.basedOn.size()]); // Reference
        case -995424086: /*parent*/ return this.parent == null ? new Base[0] : this.parent.toArray(new Base[this.parent.size()]); // Reference
        case -1017049693: /*questionnaire*/ return this.questionnaire == null ? new Base[0] : new Base[] {this.questionnaire}; // Reference
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<QuestionnaireResponseStatus>
        case -1867885268: /*subject*/ return this.subject == null ? new Base[0] : new Base[] {this.subject}; // Reference
        case 951530927: /*context*/ return this.context == null ? new Base[0] : new Base[] {this.context}; // Reference
        case 1433073514: /*authored*/ return this.authored == null ? new Base[0] : new Base[] {this.authored}; // DateTimeType
        case -1406328437: /*author*/ return this.author == null ? new Base[0] : new Base[] {this.author}; // Reference
        case -896505829: /*source*/ return this.source == null ? new Base[0] : new Base[] {this.source}; // Reference
        case 3242771: /*item*/ return this.item == null ? new Base[0] : this.item.toArray(new Base[this.item.size()]); // QuestionnaireResponseItemComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.identifier = castToIdentifier(value); // Identifier
          return value;
        case -332612366: // basedOn
          this.getBasedOn().add(castToReference(value)); // Reference
          return value;
        case -995424086: // parent
          this.getParent().add(castToReference(value)); // Reference
          return value;
        case -1017049693: // questionnaire
          this.questionnaire = castToReference(value); // Reference
          return value;
        case -892481550: // status
          value = new QuestionnaireResponseStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<QuestionnaireResponseStatus>
          return value;
        case -1867885268: // subject
          this.subject = castToReference(value); // Reference
          return value;
        case 951530927: // context
          this.context = castToReference(value); // Reference
          return value;
        case 1433073514: // authored
          this.authored = castToDateTime(value); // DateTimeType
          return value;
        case -1406328437: // author
          this.author = castToReference(value); // Reference
          return value;
        case -896505829: // source
          this.source = castToReference(value); // Reference
          return value;
        case 3242771: // item
          this.getItem().add((QuestionnaireResponseItemComponent) value); // QuestionnaireResponseItemComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier")) {
          this.identifier = castToIdentifier(value); // Identifier
        } else if (name.equals("basedOn")) {
          this.getBasedOn().add(castToReference(value));
        } else if (name.equals("parent")) {
          this.getParent().add(castToReference(value));
        } else if (name.equals("questionnaire")) {
          this.questionnaire = castToReference(value); // Reference
        } else if (name.equals("status")) {
          value = new QuestionnaireResponseStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<QuestionnaireResponseStatus>
        } else if (name.equals("subject")) {
          this.subject = castToReference(value); // Reference
        } else if (name.equals("context")) {
          this.context = castToReference(value); // Reference
        } else if (name.equals("authored")) {
          this.authored = castToDateTime(value); // DateTimeType
        } else if (name.equals("author")) {
          this.author = castToReference(value); // Reference
        } else if (name.equals("source")) {
          this.source = castToReference(value); // Reference
        } else if (name.equals("item")) {
          this.getItem().add((QuestionnaireResponseItemComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return getIdentifier(); 
        case -332612366:  return addBasedOn(); 
        case -995424086:  return addParent(); 
        case -1017049693:  return getQuestionnaire(); 
        case -892481550:  return getStatusElement();
        case -1867885268:  return getSubject(); 
        case 951530927:  return getContext(); 
        case 1433073514:  return getAuthoredElement();
        case -1406328437:  return getAuthor(); 
        case -896505829:  return getSource(); 
        case 3242771:  return addItem(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case -332612366: /*basedOn*/ return new String[] {"Reference"};
        case -995424086: /*parent*/ return new String[] {"Reference"};
        case -1017049693: /*questionnaire*/ return new String[] {"Reference"};
        case -892481550: /*status*/ return new String[] {"code"};
        case -1867885268: /*subject*/ return new String[] {"Reference"};
        case 951530927: /*context*/ return new String[] {"Reference"};
        case 1433073514: /*authored*/ return new String[] {"dateTime"};
        case -1406328437: /*author*/ return new String[] {"Reference"};
        case -896505829: /*source*/ return new String[] {"Reference"};
        case 3242771: /*item*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          this.identifier = new Identifier();
          return this.identifier;
        }
        else if (name.equals("basedOn")) {
          return addBasedOn();
        }
        else if (name.equals("parent")) {
          return addParent();
        }
        else if (name.equals("questionnaire")) {
          this.questionnaire = new Reference();
          return this.questionnaire;
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type QuestionnaireResponse.status");
        }
        else if (name.equals("subject")) {
          this.subject = new Reference();
          return this.subject;
        }
        else if (name.equals("context")) {
          this.context = new Reference();
          return this.context;
        }
        else if (name.equals("authored")) {
          throw new FHIRException("Cannot call addChild on a primitive type QuestionnaireResponse.authored");
        }
        else if (name.equals("author")) {
          this.author = new Reference();
          return this.author;
        }
        else if (name.equals("source")) {
          this.source = new Reference();
          return this.source;
        }
        else if (name.equals("item")) {
          return addItem();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "QuestionnaireResponse";

  }

      public QuestionnaireResponse copy() {
        QuestionnaireResponse dst = new QuestionnaireResponse();
        copyValues(dst);
        dst.identifier = identifier == null ? null : identifier.copy();
        if (basedOn != null) {
          dst.basedOn = new ArrayList<Reference>();
          for (Reference i : basedOn)
            dst.basedOn.add(i.copy());
        };
        if (parent != null) {
          dst.parent = new ArrayList<Reference>();
          for (Reference i : parent)
            dst.parent.add(i.copy());
        };
        dst.questionnaire = questionnaire == null ? null : questionnaire.copy();
        dst.status = status == null ? null : status.copy();
        dst.subject = subject == null ? null : subject.copy();
        dst.context = context == null ? null : context.copy();
        dst.authored = authored == null ? null : authored.copy();
        dst.author = author == null ? null : author.copy();
        dst.source = source == null ? null : source.copy();
        if (item != null) {
          dst.item = new ArrayList<QuestionnaireResponseItemComponent>();
          for (QuestionnaireResponseItemComponent i : item)
            dst.item.add(i.copy());
        };
        return dst;
      }

      protected QuestionnaireResponse typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof QuestionnaireResponse))
          return false;
        QuestionnaireResponse o = (QuestionnaireResponse) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(basedOn, o.basedOn, true) && compareDeep(parent, o.parent, true)
           && compareDeep(questionnaire, o.questionnaire, true) && compareDeep(status, o.status, true) && compareDeep(subject, o.subject, true)
           && compareDeep(context, o.context, true) && compareDeep(authored, o.authored, true) && compareDeep(author, o.author, true)
           && compareDeep(source, o.source, true) && compareDeep(item, o.item, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof QuestionnaireResponse))
          return false;
        QuestionnaireResponse o = (QuestionnaireResponse) other;
        return compareValues(status, o.status, true) && compareValues(authored, o.authored, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, basedOn, parent
          , questionnaire, status, subject, context, authored, author, source, item);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.QuestionnaireResponse;
   }

 /**
   * Search parameter: <b>authored</b>
   * <p>
   * Description: <b>When the questionnaire response was last changed</b><br>
   * Type: <b>date</b><br>
   * Path: <b>QuestionnaireResponse.authored</b><br>
   * </p>
   */
  @SearchParamDefinition(name="authored", path="QuestionnaireResponse.authored", description="When the questionnaire response was last changed", type="date" )
  public static final String SP_AUTHORED = "authored";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>authored</b>
   * <p>
   * Description: <b>When the questionnaire response was last changed</b><br>
   * Type: <b>date</b><br>
   * Path: <b>QuestionnaireResponse.authored</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam AUTHORED = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_AUTHORED);

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>The unique identifier for the questionnaire response</b><br>
   * Type: <b>token</b><br>
   * Path: <b>QuestionnaireResponse.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="QuestionnaireResponse.identifier", description="The unique identifier for the questionnaire response", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>The unique identifier for the questionnaire response</b><br>
   * Type: <b>token</b><br>
   * Path: <b>QuestionnaireResponse.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>parent</b>
   * <p>
   * Description: <b>Procedure or observation this questionnaire response was performed as a part of</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>QuestionnaireResponse.parent</b><br>
   * </p>
   */
  @SearchParamDefinition(name="parent", path="QuestionnaireResponse.parent", description="Procedure or observation this questionnaire response was performed as a part of", type="reference", target={Observation.class, Procedure.class } )
  public static final String SP_PARENT = "parent";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>parent</b>
   * <p>
   * Description: <b>Procedure or observation this questionnaire response was performed as a part of</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>QuestionnaireResponse.parent</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PARENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PARENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>QuestionnaireResponse:parent</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PARENT = new ca.uhn.fhir.model.api.Include("QuestionnaireResponse:parent").toLocked();

 /**
   * Search parameter: <b>questionnaire</b>
   * <p>
   * Description: <b>The questionnaire the answers are provided for</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>QuestionnaireResponse.questionnaire</b><br>
   * </p>
   */
  @SearchParamDefinition(name="questionnaire", path="QuestionnaireResponse.questionnaire", description="The questionnaire the answers are provided for", type="reference", target={Questionnaire.class } )
  public static final String SP_QUESTIONNAIRE = "questionnaire";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>questionnaire</b>
   * <p>
   * Description: <b>The questionnaire the answers are provided for</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>QuestionnaireResponse.questionnaire</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam QUESTIONNAIRE = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_QUESTIONNAIRE);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>QuestionnaireResponse:questionnaire</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_QUESTIONNAIRE = new ca.uhn.fhir.model.api.Include("QuestionnaireResponse:questionnaire").toLocked();

 /**
   * Search parameter: <b>based-on</b>
   * <p>
   * Description: <b>Plan/proposal/order fulfilled by this questionnaire response</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>QuestionnaireResponse.basedOn</b><br>
   * </p>
   */
  @SearchParamDefinition(name="based-on", path="QuestionnaireResponse.basedOn", description="Plan/proposal/order fulfilled by this questionnaire response", type="reference", target={CarePlan.class, ProcedureRequest.class, ReferralRequest.class } )
  public static final String SP_BASED_ON = "based-on";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>based-on</b>
   * <p>
   * Description: <b>Plan/proposal/order fulfilled by this questionnaire response</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>QuestionnaireResponse.basedOn</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam BASED_ON = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_BASED_ON);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>QuestionnaireResponse:based-on</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_BASED_ON = new ca.uhn.fhir.model.api.Include("QuestionnaireResponse:based-on").toLocked();

 /**
   * Search parameter: <b>subject</b>
   * <p>
   * Description: <b>The subject of the questionnaire response</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>QuestionnaireResponse.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="subject", path="QuestionnaireResponse.subject", description="The subject of the questionnaire response", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient") } )
  public static final String SP_SUBJECT = "subject";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>subject</b>
   * <p>
   * Description: <b>The subject of the questionnaire response</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>QuestionnaireResponse.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SUBJECT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SUBJECT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>QuestionnaireResponse:subject</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SUBJECT = new ca.uhn.fhir.model.api.Include("QuestionnaireResponse:subject").toLocked();

 /**
   * Search parameter: <b>author</b>
   * <p>
   * Description: <b>The author of the questionnaire response</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>QuestionnaireResponse.author</b><br>
   * </p>
   */
  @SearchParamDefinition(name="author", path="QuestionnaireResponse.author", description="The author of the questionnaire response", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Device"), @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient"), @ca.uhn.fhir.model.api.annotation.Compartment(name="Practitioner"), @ca.uhn.fhir.model.api.annotation.Compartment(name="RelatedPerson") }, target={Device.class, Patient.class, Practitioner.class, RelatedPerson.class } )
  public static final String SP_AUTHOR = "author";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>author</b>
   * <p>
   * Description: <b>The author of the questionnaire response</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>QuestionnaireResponse.author</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam AUTHOR = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_AUTHOR);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>QuestionnaireResponse:author</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_AUTHOR = new ca.uhn.fhir.model.api.Include("QuestionnaireResponse:author").toLocked();

 /**
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>The patient that is the subject of the questionnaire response</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>QuestionnaireResponse.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="QuestionnaireResponse.subject", description="The patient that is the subject of the questionnaire response", type="reference", target={Patient.class } )
  public static final String SP_PATIENT = "patient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patient</b>
   * <p>
   * Description: <b>The patient that is the subject of the questionnaire response</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>QuestionnaireResponse.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PATIENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PATIENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>QuestionnaireResponse:patient</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PATIENT = new ca.uhn.fhir.model.api.Include("QuestionnaireResponse:patient").toLocked();

 /**
   * Search parameter: <b>context</b>
   * <p>
   * Description: <b>Encounter or episode associated with the questionnaire response</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>QuestionnaireResponse.context</b><br>
   * </p>
   */
  @SearchParamDefinition(name="context", path="QuestionnaireResponse.context", description="Encounter or episode associated with the questionnaire response", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Encounter") }, target={Encounter.class, EpisodeOfCare.class } )
  public static final String SP_CONTEXT = "context";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>context</b>
   * <p>
   * Description: <b>Encounter or episode associated with the questionnaire response</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>QuestionnaireResponse.context</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam CONTEXT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_CONTEXT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>QuestionnaireResponse:context</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_CONTEXT = new ca.uhn.fhir.model.api.Include("QuestionnaireResponse:context").toLocked();

 /**
   * Search parameter: <b>source</b>
   * <p>
   * Description: <b>The individual providing the information reflected in the questionnaire respose</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>QuestionnaireResponse.source</b><br>
   * </p>
   */
  @SearchParamDefinition(name="source", path="QuestionnaireResponse.source", description="The individual providing the information reflected in the questionnaire respose", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Practitioner"), @ca.uhn.fhir.model.api.annotation.Compartment(name="RelatedPerson") }, target={Patient.class, Practitioner.class, RelatedPerson.class } )
  public static final String SP_SOURCE = "source";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>source</b>
   * <p>
   * Description: <b>The individual providing the information reflected in the questionnaire respose</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>QuestionnaireResponse.source</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SOURCE = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SOURCE);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>QuestionnaireResponse:source</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SOURCE = new ca.uhn.fhir.model.api.Include("QuestionnaireResponse:source").toLocked();

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>The status of the questionnaire response</b><br>
   * Type: <b>token</b><br>
   * Path: <b>QuestionnaireResponse.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="QuestionnaireResponse.status", description="The status of the questionnaire response", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>The status of the questionnaire response</b><br>
   * Type: <b>token</b><br>
   * Path: <b>QuestionnaireResponse.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);


}

