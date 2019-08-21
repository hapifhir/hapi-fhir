package org.hl7.fhir.dstu2016may.model;

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

// Generated on Sun, May 8, 2016 03:05+1000 for FHIR v1.4.0
import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseBackboneElement;
import org.hl7.fhir.utilities.Utilities;

import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
/**
 * A guidance response is the formal response to a guidance request, including any output parameters returned by the evaluation, as well as the description of any proposed actions to be taken.
 */
@ResourceDef(name="GuidanceResponse", profile="http://hl7.org/fhir/Profile/GuidanceResponse")
public class GuidanceResponse extends DomainResource {

    public enum GuidanceResponseStatus {
        /**
         * The request was processed successfully
         */
        SUCCESS, 
        /**
         * The request was processed successfully, but more data may result in a more complete evaluation
         */
        DATAREQUESTED, 
        /**
         * The request was processed, but more data is required to complete the evaluation
         */
        DATAREQUIRED, 
        /**
         * The request is currently being processed
         */
        INPROGRESS, 
        /**
         * The request was not processed successfully
         */
        FAILURE, 
        /**
         * added to help the parsers
         */
        NULL;
        public static GuidanceResponseStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("success".equals(codeString))
          return SUCCESS;
        if ("data-requested".equals(codeString))
          return DATAREQUESTED;
        if ("data-required".equals(codeString))
          return DATAREQUIRED;
        if ("in-progress".equals(codeString))
          return INPROGRESS;
        if ("failure".equals(codeString))
          return FAILURE;
        throw new FHIRException("Unknown GuidanceResponseStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case SUCCESS: return "success";
            case DATAREQUESTED: return "data-requested";
            case DATAREQUIRED: return "data-required";
            case INPROGRESS: return "in-progress";
            case FAILURE: return "failure";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case SUCCESS: return "http://hl7.org/fhir/guidance-response-status";
            case DATAREQUESTED: return "http://hl7.org/fhir/guidance-response-status";
            case DATAREQUIRED: return "http://hl7.org/fhir/guidance-response-status";
            case INPROGRESS: return "http://hl7.org/fhir/guidance-response-status";
            case FAILURE: return "http://hl7.org/fhir/guidance-response-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case SUCCESS: return "The request was processed successfully";
            case DATAREQUESTED: return "The request was processed successfully, but more data may result in a more complete evaluation";
            case DATAREQUIRED: return "The request was processed, but more data is required to complete the evaluation";
            case INPROGRESS: return "The request is currently being processed";
            case FAILURE: return "The request was not processed successfully";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case SUCCESS: return "Success";
            case DATAREQUESTED: return "Data Requested";
            case DATAREQUIRED: return "Data Required";
            case INPROGRESS: return "In Progress";
            case FAILURE: return "Failure";
            default: return "?";
          }
        }
    }

  public static class GuidanceResponseStatusEnumFactory implements EnumFactory<GuidanceResponseStatus> {
    public GuidanceResponseStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("success".equals(codeString))
          return GuidanceResponseStatus.SUCCESS;
        if ("data-requested".equals(codeString))
          return GuidanceResponseStatus.DATAREQUESTED;
        if ("data-required".equals(codeString))
          return GuidanceResponseStatus.DATAREQUIRED;
        if ("in-progress".equals(codeString))
          return GuidanceResponseStatus.INPROGRESS;
        if ("failure".equals(codeString))
          return GuidanceResponseStatus.FAILURE;
        throw new IllegalArgumentException("Unknown GuidanceResponseStatus code '"+codeString+"'");
        }
        public Enumeration<GuidanceResponseStatus> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("success".equals(codeString))
          return new Enumeration<GuidanceResponseStatus>(this, GuidanceResponseStatus.SUCCESS);
        if ("data-requested".equals(codeString))
          return new Enumeration<GuidanceResponseStatus>(this, GuidanceResponseStatus.DATAREQUESTED);
        if ("data-required".equals(codeString))
          return new Enumeration<GuidanceResponseStatus>(this, GuidanceResponseStatus.DATAREQUIRED);
        if ("in-progress".equals(codeString))
          return new Enumeration<GuidanceResponseStatus>(this, GuidanceResponseStatus.INPROGRESS);
        if ("failure".equals(codeString))
          return new Enumeration<GuidanceResponseStatus>(this, GuidanceResponseStatus.FAILURE);
        throw new FHIRException("Unknown GuidanceResponseStatus code '"+codeString+"'");
        }
    public String toCode(GuidanceResponseStatus code) {
      if (code == GuidanceResponseStatus.SUCCESS)
        return "success";
      if (code == GuidanceResponseStatus.DATAREQUESTED)
        return "data-requested";
      if (code == GuidanceResponseStatus.DATAREQUIRED)
        return "data-required";
      if (code == GuidanceResponseStatus.INPROGRESS)
        return "in-progress";
      if (code == GuidanceResponseStatus.FAILURE)
        return "failure";
      return "?";
      }
    public String toSystem(GuidanceResponseStatus code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class GuidanceResponseActionComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A unique identifier for the action. The identifier SHALL be unique within the container in which it appears, and MAY be universally unique.
         */
        @Child(name = "actionIdentifier", type = {Identifier.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Unique identifier", formalDefinition="A unique identifier for the action. The identifier SHALL be unique within the container in which it appears, and MAY be universally unique." )
        protected Identifier actionIdentifier;

        /**
         * A user-visible label for the action.
         */
        @Child(name = "label", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="User-visible label for the action (e.g. 1. or A.)", formalDefinition="A user-visible label for the action." )
        protected StringType label;

        /**
         * The title of the action displayed to a user.
         */
        @Child(name = "title", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="User-visible title", formalDefinition="The title of the action displayed to a user." )
        protected StringType title;

        /**
         * A short description of the action used to provide a summary to display to the user.
         */
        @Child(name = "description", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Short description of the action", formalDefinition="A short description of the action used to provide a summary to display to the user." )
        protected StringType description;

        /**
         * A text equivalent of the action to be performed. This provides a human-interpretable description of the action when the definition is consumed by a system that may not be capable of interpreting it dynamically.
         */
        @Child(name = "textEquivalent", type = {StringType.class}, order=5, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Static text equivalent of the action, used if the dynamic aspects cannot be interpreted by the receiving system", formalDefinition="A text equivalent of the action to be performed. This provides a human-interpretable description of the action when the definition is consumed by a system that may not be capable of interpreting it dynamically." )
        protected StringType textEquivalent;

        /**
         * The concept represented by this action or its sub-actions.
         */
        @Child(name = "concept", type = {CodeableConcept.class}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="The meaning of the action or its sub-actions", formalDefinition="The concept represented by this action or its sub-actions." )
        protected List<CodeableConcept> concept;

        /**
         * The evidence grade and the sources of evidence for this action.
         */
        @Child(name = "supportingEvidence", type = {Attachment.class}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Evidence that supports taking the action", formalDefinition="The evidence grade and the sources of evidence for this action." )
        protected List<Attachment> supportingEvidence;

        /**
         * A relationship to another action such as "before" or "30-60 minutes after start of".
         */
        @Child(name = "relatedAction", type = {}, order=8, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Relationship to another action", formalDefinition="A relationship to another action such as \"before\" or \"30-60 minutes after start of\"." )
        protected GuidanceResponseActionRelatedActionComponent relatedAction;

        /**
         * Didactic or other informational resources associated with the action that can be provided to the CDS recipient. Information resources can include inline text commentary and links to web resources.
         */
        @Child(name = "documentation", type = {Attachment.class}, order=9, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Supporting documentation for the intended performer of the action", formalDefinition="Didactic or other informational resources associated with the action that can be provided to the CDS recipient. Information resources can include inline text commentary and links to web resources." )
        protected List<Attachment> documentation;

        /**
         * The participant in the action.
         */
        @Child(name = "participant", type = {Patient.class, Person.class, Practitioner.class, RelatedPerson.class}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Participant", formalDefinition="The participant in the action." )
        protected List<Reference> participant;
        /**
         * The actual objects that are the target of the reference (The participant in the action.)
         */
        protected List<Resource> participantTarget;


        /**
         * The type of action to perform (create, update, remove).
         */
        @Child(name = "type", type = {CodeType.class}, order=11, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="create | update | remove | fire-event", formalDefinition="The type of action to perform (create, update, remove)." )
        protected CodeType type;

        /**
         * A behavior associated with the action. Behaviors define how the action is to be presented and/or executed within the receiving environment.
         */
        @Child(name = "behavior", type = {}, order=12, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Defines behaviors such as selection and grouping", formalDefinition="A behavior associated with the action. Behaviors define how the action is to be presented and/or executed within the receiving environment." )
        protected List<GuidanceResponseActionBehaviorComponent> behavior;

        /**
         * The resource that is the target of the action (e.g. CommunicationRequest).
         */
        @Child(name = "resource", type = {}, order=13, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The target of the action", formalDefinition="The resource that is the target of the action (e.g. CommunicationRequest)." )
        protected Reference resource;

        /**
         * The actual object that is the target of the reference (The resource that is the target of the action (e.g. CommunicationRequest).)
         */
        protected Resource resourceTarget;

        /**
         * Sub actions.
         */
        @Child(name = "action", type = {GuidanceResponseActionComponent.class}, order=14, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Sub action", formalDefinition="Sub actions." )
        protected List<GuidanceResponseActionComponent> action;

        private static final long serialVersionUID = -1602697381L;

    /**
     * Constructor
     */
      public GuidanceResponseActionComponent() {
        super();
      }

        /**
         * @return {@link #actionIdentifier} (A unique identifier for the action. The identifier SHALL be unique within the container in which it appears, and MAY be universally unique.)
         */
        public Identifier getActionIdentifier() { 
          if (this.actionIdentifier == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create GuidanceResponseActionComponent.actionIdentifier");
            else if (Configuration.doAutoCreate())
              this.actionIdentifier = new Identifier(); // cc
          return this.actionIdentifier;
        }

        public boolean hasActionIdentifier() { 
          return this.actionIdentifier != null && !this.actionIdentifier.isEmpty();
        }

        /**
         * @param value {@link #actionIdentifier} (A unique identifier for the action. The identifier SHALL be unique within the container in which it appears, and MAY be universally unique.)
         */
        public GuidanceResponseActionComponent setActionIdentifier(Identifier value) { 
          this.actionIdentifier = value;
          return this;
        }

        /**
         * @return {@link #label} (A user-visible label for the action.). This is the underlying object with id, value and extensions. The accessor "getLabel" gives direct access to the value
         */
        public StringType getLabelElement() { 
          if (this.label == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create GuidanceResponseActionComponent.label");
            else if (Configuration.doAutoCreate())
              this.label = new StringType(); // bb
          return this.label;
        }

        public boolean hasLabelElement() { 
          return this.label != null && !this.label.isEmpty();
        }

        public boolean hasLabel() { 
          return this.label != null && !this.label.isEmpty();
        }

        /**
         * @param value {@link #label} (A user-visible label for the action.). This is the underlying object with id, value and extensions. The accessor "getLabel" gives direct access to the value
         */
        public GuidanceResponseActionComponent setLabelElement(StringType value) { 
          this.label = value;
          return this;
        }

        /**
         * @return A user-visible label for the action.
         */
        public String getLabel() { 
          return this.label == null ? null : this.label.getValue();
        }

        /**
         * @param value A user-visible label for the action.
         */
        public GuidanceResponseActionComponent setLabel(String value) { 
          if (Utilities.noString(value))
            this.label = null;
          else {
            if (this.label == null)
              this.label = new StringType();
            this.label.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #title} (The title of the action displayed to a user.). This is the underlying object with id, value and extensions. The accessor "getTitle" gives direct access to the value
         */
        public StringType getTitleElement() { 
          if (this.title == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create GuidanceResponseActionComponent.title");
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
         * @param value {@link #title} (The title of the action displayed to a user.). This is the underlying object with id, value and extensions. The accessor "getTitle" gives direct access to the value
         */
        public GuidanceResponseActionComponent setTitleElement(StringType value) { 
          this.title = value;
          return this;
        }

        /**
         * @return The title of the action displayed to a user.
         */
        public String getTitle() { 
          return this.title == null ? null : this.title.getValue();
        }

        /**
         * @param value The title of the action displayed to a user.
         */
        public GuidanceResponseActionComponent setTitle(String value) { 
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
         * @return {@link #description} (A short description of the action used to provide a summary to display to the user.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public StringType getDescriptionElement() { 
          if (this.description == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create GuidanceResponseActionComponent.description");
            else if (Configuration.doAutoCreate())
              this.description = new StringType(); // bb
          return this.description;
        }

        public boolean hasDescriptionElement() { 
          return this.description != null && !this.description.isEmpty();
        }

        public boolean hasDescription() { 
          return this.description != null && !this.description.isEmpty();
        }

        /**
         * @param value {@link #description} (A short description of the action used to provide a summary to display to the user.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public GuidanceResponseActionComponent setDescriptionElement(StringType value) { 
          this.description = value;
          return this;
        }

        /**
         * @return A short description of the action used to provide a summary to display to the user.
         */
        public String getDescription() { 
          return this.description == null ? null : this.description.getValue();
        }

        /**
         * @param value A short description of the action used to provide a summary to display to the user.
         */
        public GuidanceResponseActionComponent setDescription(String value) { 
          if (Utilities.noString(value))
            this.description = null;
          else {
            if (this.description == null)
              this.description = new StringType();
            this.description.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #textEquivalent} (A text equivalent of the action to be performed. This provides a human-interpretable description of the action when the definition is consumed by a system that may not be capable of interpreting it dynamically.). This is the underlying object with id, value and extensions. The accessor "getTextEquivalent" gives direct access to the value
         */
        public StringType getTextEquivalentElement() { 
          if (this.textEquivalent == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create GuidanceResponseActionComponent.textEquivalent");
            else if (Configuration.doAutoCreate())
              this.textEquivalent = new StringType(); // bb
          return this.textEquivalent;
        }

        public boolean hasTextEquivalentElement() { 
          return this.textEquivalent != null && !this.textEquivalent.isEmpty();
        }

        public boolean hasTextEquivalent() { 
          return this.textEquivalent != null && !this.textEquivalent.isEmpty();
        }

        /**
         * @param value {@link #textEquivalent} (A text equivalent of the action to be performed. This provides a human-interpretable description of the action when the definition is consumed by a system that may not be capable of interpreting it dynamically.). This is the underlying object with id, value and extensions. The accessor "getTextEquivalent" gives direct access to the value
         */
        public GuidanceResponseActionComponent setTextEquivalentElement(StringType value) { 
          this.textEquivalent = value;
          return this;
        }

        /**
         * @return A text equivalent of the action to be performed. This provides a human-interpretable description of the action when the definition is consumed by a system that may not be capable of interpreting it dynamically.
         */
        public String getTextEquivalent() { 
          return this.textEquivalent == null ? null : this.textEquivalent.getValue();
        }

        /**
         * @param value A text equivalent of the action to be performed. This provides a human-interpretable description of the action when the definition is consumed by a system that may not be capable of interpreting it dynamically.
         */
        public GuidanceResponseActionComponent setTextEquivalent(String value) { 
          if (Utilities.noString(value))
            this.textEquivalent = null;
          else {
            if (this.textEquivalent == null)
              this.textEquivalent = new StringType();
            this.textEquivalent.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #concept} (The concept represented by this action or its sub-actions.)
         */
        public List<CodeableConcept> getConcept() { 
          if (this.concept == null)
            this.concept = new ArrayList<CodeableConcept>();
          return this.concept;
        }

        public boolean hasConcept() { 
          if (this.concept == null)
            return false;
          for (CodeableConcept item : this.concept)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #concept} (The concept represented by this action or its sub-actions.)
         */
    // syntactic sugar
        public CodeableConcept addConcept() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.concept == null)
            this.concept = new ArrayList<CodeableConcept>();
          this.concept.add(t);
          return t;
        }

    // syntactic sugar
        public GuidanceResponseActionComponent addConcept(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.concept == null)
            this.concept = new ArrayList<CodeableConcept>();
          this.concept.add(t);
          return this;
        }

        /**
         * @return {@link #supportingEvidence} (The evidence grade and the sources of evidence for this action.)
         */
        public List<Attachment> getSupportingEvidence() { 
          if (this.supportingEvidence == null)
            this.supportingEvidence = new ArrayList<Attachment>();
          return this.supportingEvidence;
        }

        public boolean hasSupportingEvidence() { 
          if (this.supportingEvidence == null)
            return false;
          for (Attachment item : this.supportingEvidence)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #supportingEvidence} (The evidence grade and the sources of evidence for this action.)
         */
    // syntactic sugar
        public Attachment addSupportingEvidence() { //3
          Attachment t = new Attachment();
          if (this.supportingEvidence == null)
            this.supportingEvidence = new ArrayList<Attachment>();
          this.supportingEvidence.add(t);
          return t;
        }

    // syntactic sugar
        public GuidanceResponseActionComponent addSupportingEvidence(Attachment t) { //3
          if (t == null)
            return this;
          if (this.supportingEvidence == null)
            this.supportingEvidence = new ArrayList<Attachment>();
          this.supportingEvidence.add(t);
          return this;
        }

        /**
         * @return {@link #relatedAction} (A relationship to another action such as "before" or "30-60 minutes after start of".)
         */
        public GuidanceResponseActionRelatedActionComponent getRelatedAction() { 
          if (this.relatedAction == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create GuidanceResponseActionComponent.relatedAction");
            else if (Configuration.doAutoCreate())
              this.relatedAction = new GuidanceResponseActionRelatedActionComponent(); // cc
          return this.relatedAction;
        }

        public boolean hasRelatedAction() { 
          return this.relatedAction != null && !this.relatedAction.isEmpty();
        }

        /**
         * @param value {@link #relatedAction} (A relationship to another action such as "before" or "30-60 minutes after start of".)
         */
        public GuidanceResponseActionComponent setRelatedAction(GuidanceResponseActionRelatedActionComponent value) { 
          this.relatedAction = value;
          return this;
        }

        /**
         * @return {@link #documentation} (Didactic or other informational resources associated with the action that can be provided to the CDS recipient. Information resources can include inline text commentary and links to web resources.)
         */
        public List<Attachment> getDocumentation() { 
          if (this.documentation == null)
            this.documentation = new ArrayList<Attachment>();
          return this.documentation;
        }

        public boolean hasDocumentation() { 
          if (this.documentation == null)
            return false;
          for (Attachment item : this.documentation)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #documentation} (Didactic or other informational resources associated with the action that can be provided to the CDS recipient. Information resources can include inline text commentary and links to web resources.)
         */
    // syntactic sugar
        public Attachment addDocumentation() { //3
          Attachment t = new Attachment();
          if (this.documentation == null)
            this.documentation = new ArrayList<Attachment>();
          this.documentation.add(t);
          return t;
        }

    // syntactic sugar
        public GuidanceResponseActionComponent addDocumentation(Attachment t) { //3
          if (t == null)
            return this;
          if (this.documentation == null)
            this.documentation = new ArrayList<Attachment>();
          this.documentation.add(t);
          return this;
        }

        /**
         * @return {@link #participant} (The participant in the action.)
         */
        public List<Reference> getParticipant() { 
          if (this.participant == null)
            this.participant = new ArrayList<Reference>();
          return this.participant;
        }

        public boolean hasParticipant() { 
          if (this.participant == null)
            return false;
          for (Reference item : this.participant)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #participant} (The participant in the action.)
         */
    // syntactic sugar
        public Reference addParticipant() { //3
          Reference t = new Reference();
          if (this.participant == null)
            this.participant = new ArrayList<Reference>();
          this.participant.add(t);
          return t;
        }

    // syntactic sugar
        public GuidanceResponseActionComponent addParticipant(Reference t) { //3
          if (t == null)
            return this;
          if (this.participant == null)
            this.participant = new ArrayList<Reference>();
          this.participant.add(t);
          return this;
        }

        /**
         * @return {@link #participant} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. The participant in the action.)
         */
        public List<Resource> getParticipantTarget() { 
          if (this.participantTarget == null)
            this.participantTarget = new ArrayList<Resource>();
          return this.participantTarget;
        }

        /**
         * @return {@link #type} (The type of action to perform (create, update, remove).). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public CodeType getTypeElement() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create GuidanceResponseActionComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new CodeType(); // bb
          return this.type;
        }

        public boolean hasTypeElement() { 
          return this.type != null && !this.type.isEmpty();
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (The type of action to perform (create, update, remove).). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public GuidanceResponseActionComponent setTypeElement(CodeType value) { 
          this.type = value;
          return this;
        }

        /**
         * @return The type of action to perform (create, update, remove).
         */
        public String getType() { 
          return this.type == null ? null : this.type.getValue();
        }

        /**
         * @param value The type of action to perform (create, update, remove).
         */
        public GuidanceResponseActionComponent setType(String value) { 
          if (Utilities.noString(value))
            this.type = null;
          else {
            if (this.type == null)
              this.type = new CodeType();
            this.type.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #behavior} (A behavior associated with the action. Behaviors define how the action is to be presented and/or executed within the receiving environment.)
         */
        public List<GuidanceResponseActionBehaviorComponent> getBehavior() { 
          if (this.behavior == null)
            this.behavior = new ArrayList<GuidanceResponseActionBehaviorComponent>();
          return this.behavior;
        }

        public boolean hasBehavior() { 
          if (this.behavior == null)
            return false;
          for (GuidanceResponseActionBehaviorComponent item : this.behavior)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #behavior} (A behavior associated with the action. Behaviors define how the action is to be presented and/or executed within the receiving environment.)
         */
    // syntactic sugar
        public GuidanceResponseActionBehaviorComponent addBehavior() { //3
          GuidanceResponseActionBehaviorComponent t = new GuidanceResponseActionBehaviorComponent();
          if (this.behavior == null)
            this.behavior = new ArrayList<GuidanceResponseActionBehaviorComponent>();
          this.behavior.add(t);
          return t;
        }

    // syntactic sugar
        public GuidanceResponseActionComponent addBehavior(GuidanceResponseActionBehaviorComponent t) { //3
          if (t == null)
            return this;
          if (this.behavior == null)
            this.behavior = new ArrayList<GuidanceResponseActionBehaviorComponent>();
          this.behavior.add(t);
          return this;
        }

        /**
         * @return {@link #resource} (The resource that is the target of the action (e.g. CommunicationRequest).)
         */
        public Reference getResource() { 
          if (this.resource == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create GuidanceResponseActionComponent.resource");
            else if (Configuration.doAutoCreate())
              this.resource = new Reference(); // cc
          return this.resource;
        }

        public boolean hasResource() { 
          return this.resource != null && !this.resource.isEmpty();
        }

        /**
         * @param value {@link #resource} (The resource that is the target of the action (e.g. CommunicationRequest).)
         */
        public GuidanceResponseActionComponent setResource(Reference value) { 
          this.resource = value;
          return this;
        }

        /**
         * @return {@link #resource} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The resource that is the target of the action (e.g. CommunicationRequest).)
         */
        public Resource getResourceTarget() { 
          return this.resourceTarget;
        }

        /**
         * @param value {@link #resource} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The resource that is the target of the action (e.g. CommunicationRequest).)
         */
        public GuidanceResponseActionComponent setResourceTarget(Resource value) { 
          this.resourceTarget = value;
          return this;
        }

        /**
         * @return {@link #action} (Sub actions.)
         */
        public List<GuidanceResponseActionComponent> getAction() { 
          if (this.action == null)
            this.action = new ArrayList<GuidanceResponseActionComponent>();
          return this.action;
        }

        public boolean hasAction() { 
          if (this.action == null)
            return false;
          for (GuidanceResponseActionComponent item : this.action)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #action} (Sub actions.)
         */
    // syntactic sugar
        public GuidanceResponseActionComponent addAction() { //3
          GuidanceResponseActionComponent t = new GuidanceResponseActionComponent();
          if (this.action == null)
            this.action = new ArrayList<GuidanceResponseActionComponent>();
          this.action.add(t);
          return t;
        }

    // syntactic sugar
        public GuidanceResponseActionComponent addAction(GuidanceResponseActionComponent t) { //3
          if (t == null)
            return this;
          if (this.action == null)
            this.action = new ArrayList<GuidanceResponseActionComponent>();
          this.action.add(t);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("actionIdentifier", "Identifier", "A unique identifier for the action. The identifier SHALL be unique within the container in which it appears, and MAY be universally unique.", 0, java.lang.Integer.MAX_VALUE, actionIdentifier));
          childrenList.add(new Property("label", "string", "A user-visible label for the action.", 0, java.lang.Integer.MAX_VALUE, label));
          childrenList.add(new Property("title", "string", "The title of the action displayed to a user.", 0, java.lang.Integer.MAX_VALUE, title));
          childrenList.add(new Property("description", "string", "A short description of the action used to provide a summary to display to the user.", 0, java.lang.Integer.MAX_VALUE, description));
          childrenList.add(new Property("textEquivalent", "string", "A text equivalent of the action to be performed. This provides a human-interpretable description of the action when the definition is consumed by a system that may not be capable of interpreting it dynamically.", 0, java.lang.Integer.MAX_VALUE, textEquivalent));
          childrenList.add(new Property("concept", "CodeableConcept", "The concept represented by this action or its sub-actions.", 0, java.lang.Integer.MAX_VALUE, concept));
          childrenList.add(new Property("supportingEvidence", "Attachment", "The evidence grade and the sources of evidence for this action.", 0, java.lang.Integer.MAX_VALUE, supportingEvidence));
          childrenList.add(new Property("relatedAction", "", "A relationship to another action such as \"before\" or \"30-60 minutes after start of\".", 0, java.lang.Integer.MAX_VALUE, relatedAction));
          childrenList.add(new Property("documentation", "Attachment", "Didactic or other informational resources associated with the action that can be provided to the CDS recipient. Information resources can include inline text commentary and links to web resources.", 0, java.lang.Integer.MAX_VALUE, documentation));
          childrenList.add(new Property("participant", "Reference(Patient|Person|Practitioner|RelatedPerson)", "The participant in the action.", 0, java.lang.Integer.MAX_VALUE, participant));
          childrenList.add(new Property("type", "code", "The type of action to perform (create, update, remove).", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("behavior", "", "A behavior associated with the action. Behaviors define how the action is to be presented and/or executed within the receiving environment.", 0, java.lang.Integer.MAX_VALUE, behavior));
          childrenList.add(new Property("resource", "Reference(Any)", "The resource that is the target of the action (e.g. CommunicationRequest).", 0, java.lang.Integer.MAX_VALUE, resource));
          childrenList.add(new Property("action", "@GuidanceResponse.action", "Sub actions.", 0, java.lang.Integer.MAX_VALUE, action));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -889046145: /*actionIdentifier*/ return this.actionIdentifier == null ? new Base[0] : new Base[] {this.actionIdentifier}; // Identifier
        case 102727412: /*label*/ return this.label == null ? new Base[0] : new Base[] {this.label}; // StringType
        case 110371416: /*title*/ return this.title == null ? new Base[0] : new Base[] {this.title}; // StringType
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // StringType
        case -900391049: /*textEquivalent*/ return this.textEquivalent == null ? new Base[0] : new Base[] {this.textEquivalent}; // StringType
        case 951024232: /*concept*/ return this.concept == null ? new Base[0] : this.concept.toArray(new Base[this.concept.size()]); // CodeableConcept
        case -1735429846: /*supportingEvidence*/ return this.supportingEvidence == null ? new Base[0] : this.supportingEvidence.toArray(new Base[this.supportingEvidence.size()]); // Attachment
        case -384107967: /*relatedAction*/ return this.relatedAction == null ? new Base[0] : new Base[] {this.relatedAction}; // GuidanceResponseActionRelatedActionComponent
        case 1587405498: /*documentation*/ return this.documentation == null ? new Base[0] : this.documentation.toArray(new Base[this.documentation.size()]); // Attachment
        case 767422259: /*participant*/ return this.participant == null ? new Base[0] : this.participant.toArray(new Base[this.participant.size()]); // Reference
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeType
        case 1510912594: /*behavior*/ return this.behavior == null ? new Base[0] : this.behavior.toArray(new Base[this.behavior.size()]); // GuidanceResponseActionBehaviorComponent
        case -341064690: /*resource*/ return this.resource == null ? new Base[0] : new Base[] {this.resource}; // Reference
        case -1422950858: /*action*/ return this.action == null ? new Base[0] : this.action.toArray(new Base[this.action.size()]); // GuidanceResponseActionComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -889046145: // actionIdentifier
          this.actionIdentifier = castToIdentifier(value); // Identifier
          break;
        case 102727412: // label
          this.label = castToString(value); // StringType
          break;
        case 110371416: // title
          this.title = castToString(value); // StringType
          break;
        case -1724546052: // description
          this.description = castToString(value); // StringType
          break;
        case -900391049: // textEquivalent
          this.textEquivalent = castToString(value); // StringType
          break;
        case 951024232: // concept
          this.getConcept().add(castToCodeableConcept(value)); // CodeableConcept
          break;
        case -1735429846: // supportingEvidence
          this.getSupportingEvidence().add(castToAttachment(value)); // Attachment
          break;
        case -384107967: // relatedAction
          this.relatedAction = (GuidanceResponseActionRelatedActionComponent) value; // GuidanceResponseActionRelatedActionComponent
          break;
        case 1587405498: // documentation
          this.getDocumentation().add(castToAttachment(value)); // Attachment
          break;
        case 767422259: // participant
          this.getParticipant().add(castToReference(value)); // Reference
          break;
        case 3575610: // type
          this.type = castToCode(value); // CodeType
          break;
        case 1510912594: // behavior
          this.getBehavior().add((GuidanceResponseActionBehaviorComponent) value); // GuidanceResponseActionBehaviorComponent
          break;
        case -341064690: // resource
          this.resource = castToReference(value); // Reference
          break;
        case -1422950858: // action
          this.getAction().add((GuidanceResponseActionComponent) value); // GuidanceResponseActionComponent
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("actionIdentifier"))
          this.actionIdentifier = castToIdentifier(value); // Identifier
        else if (name.equals("label"))
          this.label = castToString(value); // StringType
        else if (name.equals("title"))
          this.title = castToString(value); // StringType
        else if (name.equals("description"))
          this.description = castToString(value); // StringType
        else if (name.equals("textEquivalent"))
          this.textEquivalent = castToString(value); // StringType
        else if (name.equals("concept"))
          this.getConcept().add(castToCodeableConcept(value));
        else if (name.equals("supportingEvidence"))
          this.getSupportingEvidence().add(castToAttachment(value));
        else if (name.equals("relatedAction"))
          this.relatedAction = (GuidanceResponseActionRelatedActionComponent) value; // GuidanceResponseActionRelatedActionComponent
        else if (name.equals("documentation"))
          this.getDocumentation().add(castToAttachment(value));
        else if (name.equals("participant"))
          this.getParticipant().add(castToReference(value));
        else if (name.equals("type"))
          this.type = castToCode(value); // CodeType
        else if (name.equals("behavior"))
          this.getBehavior().add((GuidanceResponseActionBehaviorComponent) value);
        else if (name.equals("resource"))
          this.resource = castToReference(value); // Reference
        else if (name.equals("action"))
          this.getAction().add((GuidanceResponseActionComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -889046145:  return getActionIdentifier(); // Identifier
        case 102727412: throw new FHIRException("Cannot make property label as it is not a complex type"); // StringType
        case 110371416: throw new FHIRException("Cannot make property title as it is not a complex type"); // StringType
        case -1724546052: throw new FHIRException("Cannot make property description as it is not a complex type"); // StringType
        case -900391049: throw new FHIRException("Cannot make property textEquivalent as it is not a complex type"); // StringType
        case 951024232:  return addConcept(); // CodeableConcept
        case -1735429846:  return addSupportingEvidence(); // Attachment
        case -384107967:  return getRelatedAction(); // GuidanceResponseActionRelatedActionComponent
        case 1587405498:  return addDocumentation(); // Attachment
        case 767422259:  return addParticipant(); // Reference
        case 3575610: throw new FHIRException("Cannot make property type as it is not a complex type"); // CodeType
        case 1510912594:  return addBehavior(); // GuidanceResponseActionBehaviorComponent
        case -341064690:  return getResource(); // Reference
        case -1422950858:  return addAction(); // GuidanceResponseActionComponent
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("actionIdentifier")) {
          this.actionIdentifier = new Identifier();
          return this.actionIdentifier;
        }
        else if (name.equals("label")) {
          throw new FHIRException("Cannot call addChild on a primitive type GuidanceResponse.label");
        }
        else if (name.equals("title")) {
          throw new FHIRException("Cannot call addChild on a primitive type GuidanceResponse.title");
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type GuidanceResponse.description");
        }
        else if (name.equals("textEquivalent")) {
          throw new FHIRException("Cannot call addChild on a primitive type GuidanceResponse.textEquivalent");
        }
        else if (name.equals("concept")) {
          return addConcept();
        }
        else if (name.equals("supportingEvidence")) {
          return addSupportingEvidence();
        }
        else if (name.equals("relatedAction")) {
          this.relatedAction = new GuidanceResponseActionRelatedActionComponent();
          return this.relatedAction;
        }
        else if (name.equals("documentation")) {
          return addDocumentation();
        }
        else if (name.equals("participant")) {
          return addParticipant();
        }
        else if (name.equals("type")) {
          throw new FHIRException("Cannot call addChild on a primitive type GuidanceResponse.type");
        }
        else if (name.equals("behavior")) {
          return addBehavior();
        }
        else if (name.equals("resource")) {
          this.resource = new Reference();
          return this.resource;
        }
        else if (name.equals("action")) {
          return addAction();
        }
        else
          return super.addChild(name);
      }

      public GuidanceResponseActionComponent copy() {
        GuidanceResponseActionComponent dst = new GuidanceResponseActionComponent();
        copyValues(dst);
        dst.actionIdentifier = actionIdentifier == null ? null : actionIdentifier.copy();
        dst.label = label == null ? null : label.copy();
        dst.title = title == null ? null : title.copy();
        dst.description = description == null ? null : description.copy();
        dst.textEquivalent = textEquivalent == null ? null : textEquivalent.copy();
        if (concept != null) {
          dst.concept = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : concept)
            dst.concept.add(i.copy());
        };
        if (supportingEvidence != null) {
          dst.supportingEvidence = new ArrayList<Attachment>();
          for (Attachment i : supportingEvidence)
            dst.supportingEvidence.add(i.copy());
        };
        dst.relatedAction = relatedAction == null ? null : relatedAction.copy();
        if (documentation != null) {
          dst.documentation = new ArrayList<Attachment>();
          for (Attachment i : documentation)
            dst.documentation.add(i.copy());
        };
        if (participant != null) {
          dst.participant = new ArrayList<Reference>();
          for (Reference i : participant)
            dst.participant.add(i.copy());
        };
        dst.type = type == null ? null : type.copy();
        if (behavior != null) {
          dst.behavior = new ArrayList<GuidanceResponseActionBehaviorComponent>();
          for (GuidanceResponseActionBehaviorComponent i : behavior)
            dst.behavior.add(i.copy());
        };
        dst.resource = resource == null ? null : resource.copy();
        if (action != null) {
          dst.action = new ArrayList<GuidanceResponseActionComponent>();
          for (GuidanceResponseActionComponent i : action)
            dst.action.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof GuidanceResponseActionComponent))
          return false;
        GuidanceResponseActionComponent o = (GuidanceResponseActionComponent) other;
        return compareDeep(actionIdentifier, o.actionIdentifier, true) && compareDeep(label, o.label, true)
           && compareDeep(title, o.title, true) && compareDeep(description, o.description, true) && compareDeep(textEquivalent, o.textEquivalent, true)
           && compareDeep(concept, o.concept, true) && compareDeep(supportingEvidence, o.supportingEvidence, true)
           && compareDeep(relatedAction, o.relatedAction, true) && compareDeep(documentation, o.documentation, true)
           && compareDeep(participant, o.participant, true) && compareDeep(type, o.type, true) && compareDeep(behavior, o.behavior, true)
           && compareDeep(resource, o.resource, true) && compareDeep(action, o.action, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof GuidanceResponseActionComponent))
          return false;
        GuidanceResponseActionComponent o = (GuidanceResponseActionComponent) other;
        return compareValues(label, o.label, true) && compareValues(title, o.title, true) && compareValues(description, o.description, true)
           && compareValues(textEquivalent, o.textEquivalent, true) && compareValues(type, o.type, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (actionIdentifier == null || actionIdentifier.isEmpty()) && (label == null || label.isEmpty())
           && (title == null || title.isEmpty()) && (description == null || description.isEmpty()) && (textEquivalent == null || textEquivalent.isEmpty())
           && (concept == null || concept.isEmpty()) && (supportingEvidence == null || supportingEvidence.isEmpty())
           && (relatedAction == null || relatedAction.isEmpty()) && (documentation == null || documentation.isEmpty())
           && (participant == null || participant.isEmpty()) && (type == null || type.isEmpty()) && (behavior == null || behavior.isEmpty())
           && (resource == null || resource.isEmpty()) && (action == null || action.isEmpty());
      }

  public String fhirType() {
    return "GuidanceResponse.action";

  }

  }

    @Block()
    public static class GuidanceResponseActionRelatedActionComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The unique identifier of the related action.
         */
        @Child(name = "actionIdentifier", type = {Identifier.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Identifier of the related action", formalDefinition="The unique identifier of the related action." )
        protected Identifier actionIdentifier;

        /**
         * The relationship of this action to the related action.
         */
        @Child(name = "relationship", type = {CodeType.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="before | after", formalDefinition="The relationship of this action to the related action." )
        protected CodeType relationship;

        /**
         * A duration or range of durations to apply to the relationship. For example, 30-60 minutes before.
         */
        @Child(name = "offset", type = {Duration.class, Range.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Time offset for the relationship", formalDefinition="A duration or range of durations to apply to the relationship. For example, 30-60 minutes before." )
        protected Type offset;

        /**
         * An optional indicator for how the relationship is anchored to the related action. For example "before the start" or "before the end" of the related action.
         */
        @Child(name = "anchor", type = {CodeType.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="start | end", formalDefinition="An optional indicator for how the relationship is anchored to the related action. For example \"before the start\" or \"before the end\" of the related action." )
        protected CodeType anchor;

        private static final long serialVersionUID = -1200619014L;

    /**
     * Constructor
     */
      public GuidanceResponseActionRelatedActionComponent() {
        super();
      }

    /**
     * Constructor
     */
      public GuidanceResponseActionRelatedActionComponent(Identifier actionIdentifier, CodeType relationship) {
        super();
        this.actionIdentifier = actionIdentifier;
        this.relationship = relationship;
      }

        /**
         * @return {@link #actionIdentifier} (The unique identifier of the related action.)
         */
        public Identifier getActionIdentifier() { 
          if (this.actionIdentifier == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create GuidanceResponseActionRelatedActionComponent.actionIdentifier");
            else if (Configuration.doAutoCreate())
              this.actionIdentifier = new Identifier(); // cc
          return this.actionIdentifier;
        }

        public boolean hasActionIdentifier() { 
          return this.actionIdentifier != null && !this.actionIdentifier.isEmpty();
        }

        /**
         * @param value {@link #actionIdentifier} (The unique identifier of the related action.)
         */
        public GuidanceResponseActionRelatedActionComponent setActionIdentifier(Identifier value) { 
          this.actionIdentifier = value;
          return this;
        }

        /**
         * @return {@link #relationship} (The relationship of this action to the related action.). This is the underlying object with id, value and extensions. The accessor "getRelationship" gives direct access to the value
         */
        public CodeType getRelationshipElement() { 
          if (this.relationship == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create GuidanceResponseActionRelatedActionComponent.relationship");
            else if (Configuration.doAutoCreate())
              this.relationship = new CodeType(); // bb
          return this.relationship;
        }

        public boolean hasRelationshipElement() { 
          return this.relationship != null && !this.relationship.isEmpty();
        }

        public boolean hasRelationship() { 
          return this.relationship != null && !this.relationship.isEmpty();
        }

        /**
         * @param value {@link #relationship} (The relationship of this action to the related action.). This is the underlying object with id, value and extensions. The accessor "getRelationship" gives direct access to the value
         */
        public GuidanceResponseActionRelatedActionComponent setRelationshipElement(CodeType value) { 
          this.relationship = value;
          return this;
        }

        /**
         * @return The relationship of this action to the related action.
         */
        public String getRelationship() { 
          return this.relationship == null ? null : this.relationship.getValue();
        }

        /**
         * @param value The relationship of this action to the related action.
         */
        public GuidanceResponseActionRelatedActionComponent setRelationship(String value) { 
            if (this.relationship == null)
              this.relationship = new CodeType();
            this.relationship.setValue(value);
          return this;
        }

        /**
         * @return {@link #offset} (A duration or range of durations to apply to the relationship. For example, 30-60 minutes before.)
         */
        public Type getOffset() { 
          return this.offset;
        }

        /**
         * @return {@link #offset} (A duration or range of durations to apply to the relationship. For example, 30-60 minutes before.)
         */
        public Duration getOffsetDuration() throws FHIRException { 
          if (!(this.offset instanceof Duration))
            throw new FHIRException("Type mismatch: the type Duration was expected, but "+this.offset.getClass().getName()+" was encountered");
          return (Duration) this.offset;
        }

        public boolean hasOffsetDuration() { 
          return this.offset instanceof Duration;
        }

        /**
         * @return {@link #offset} (A duration or range of durations to apply to the relationship. For example, 30-60 minutes before.)
         */
        public Range getOffsetRange() throws FHIRException { 
          if (!(this.offset instanceof Range))
            throw new FHIRException("Type mismatch: the type Range was expected, but "+this.offset.getClass().getName()+" was encountered");
          return (Range) this.offset;
        }

        public boolean hasOffsetRange() { 
          return this.offset instanceof Range;
        }

        public boolean hasOffset() { 
          return this.offset != null && !this.offset.isEmpty();
        }

        /**
         * @param value {@link #offset} (A duration or range of durations to apply to the relationship. For example, 30-60 minutes before.)
         */
        public GuidanceResponseActionRelatedActionComponent setOffset(Type value) { 
          this.offset = value;
          return this;
        }

        /**
         * @return {@link #anchor} (An optional indicator for how the relationship is anchored to the related action. For example "before the start" or "before the end" of the related action.). This is the underlying object with id, value and extensions. The accessor "getAnchor" gives direct access to the value
         */
        public CodeType getAnchorElement() { 
          if (this.anchor == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create GuidanceResponseActionRelatedActionComponent.anchor");
            else if (Configuration.doAutoCreate())
              this.anchor = new CodeType(); // bb
          return this.anchor;
        }

        public boolean hasAnchorElement() { 
          return this.anchor != null && !this.anchor.isEmpty();
        }

        public boolean hasAnchor() { 
          return this.anchor != null && !this.anchor.isEmpty();
        }

        /**
         * @param value {@link #anchor} (An optional indicator for how the relationship is anchored to the related action. For example "before the start" or "before the end" of the related action.). This is the underlying object with id, value and extensions. The accessor "getAnchor" gives direct access to the value
         */
        public GuidanceResponseActionRelatedActionComponent setAnchorElement(CodeType value) { 
          this.anchor = value;
          return this;
        }

        /**
         * @return An optional indicator for how the relationship is anchored to the related action. For example "before the start" or "before the end" of the related action.
         */
        public String getAnchor() { 
          return this.anchor == null ? null : this.anchor.getValue();
        }

        /**
         * @param value An optional indicator for how the relationship is anchored to the related action. For example "before the start" or "before the end" of the related action.
         */
        public GuidanceResponseActionRelatedActionComponent setAnchor(String value) { 
          if (Utilities.noString(value))
            this.anchor = null;
          else {
            if (this.anchor == null)
              this.anchor = new CodeType();
            this.anchor.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("actionIdentifier", "Identifier", "The unique identifier of the related action.", 0, java.lang.Integer.MAX_VALUE, actionIdentifier));
          childrenList.add(new Property("relationship", "code", "The relationship of this action to the related action.", 0, java.lang.Integer.MAX_VALUE, relationship));
          childrenList.add(new Property("offset[x]", "Duration|Range", "A duration or range of durations to apply to the relationship. For example, 30-60 minutes before.", 0, java.lang.Integer.MAX_VALUE, offset));
          childrenList.add(new Property("anchor", "code", "An optional indicator for how the relationship is anchored to the related action. For example \"before the start\" or \"before the end\" of the related action.", 0, java.lang.Integer.MAX_VALUE, anchor));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -889046145: /*actionIdentifier*/ return this.actionIdentifier == null ? new Base[0] : new Base[] {this.actionIdentifier}; // Identifier
        case -261851592: /*relationship*/ return this.relationship == null ? new Base[0] : new Base[] {this.relationship}; // CodeType
        case -1019779949: /*offset*/ return this.offset == null ? new Base[0] : new Base[] {this.offset}; // Type
        case -1413299531: /*anchor*/ return this.anchor == null ? new Base[0] : new Base[] {this.anchor}; // CodeType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -889046145: // actionIdentifier
          this.actionIdentifier = castToIdentifier(value); // Identifier
          break;
        case -261851592: // relationship
          this.relationship = castToCode(value); // CodeType
          break;
        case -1019779949: // offset
          this.offset = (Type) value; // Type
          break;
        case -1413299531: // anchor
          this.anchor = castToCode(value); // CodeType
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("actionIdentifier"))
          this.actionIdentifier = castToIdentifier(value); // Identifier
        else if (name.equals("relationship"))
          this.relationship = castToCode(value); // CodeType
        else if (name.equals("offset[x]"))
          this.offset = (Type) value; // Type
        else if (name.equals("anchor"))
          this.anchor = castToCode(value); // CodeType
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -889046145:  return getActionIdentifier(); // Identifier
        case -261851592: throw new FHIRException("Cannot make property relationship as it is not a complex type"); // CodeType
        case -1960684787:  return getOffset(); // Type
        case -1413299531: throw new FHIRException("Cannot make property anchor as it is not a complex type"); // CodeType
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("actionIdentifier")) {
          this.actionIdentifier = new Identifier();
          return this.actionIdentifier;
        }
        else if (name.equals("relationship")) {
          throw new FHIRException("Cannot call addChild on a primitive type GuidanceResponse.relationship");
        }
        else if (name.equals("offsetDuration")) {
          this.offset = new Duration();
          return this.offset;
        }
        else if (name.equals("offsetRange")) {
          this.offset = new Range();
          return this.offset;
        }
        else if (name.equals("anchor")) {
          throw new FHIRException("Cannot call addChild on a primitive type GuidanceResponse.anchor");
        }
        else
          return super.addChild(name);
      }

      public GuidanceResponseActionRelatedActionComponent copy() {
        GuidanceResponseActionRelatedActionComponent dst = new GuidanceResponseActionRelatedActionComponent();
        copyValues(dst);
        dst.actionIdentifier = actionIdentifier == null ? null : actionIdentifier.copy();
        dst.relationship = relationship == null ? null : relationship.copy();
        dst.offset = offset == null ? null : offset.copy();
        dst.anchor = anchor == null ? null : anchor.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof GuidanceResponseActionRelatedActionComponent))
          return false;
        GuidanceResponseActionRelatedActionComponent o = (GuidanceResponseActionRelatedActionComponent) other;
        return compareDeep(actionIdentifier, o.actionIdentifier, true) && compareDeep(relationship, o.relationship, true)
           && compareDeep(offset, o.offset, true) && compareDeep(anchor, o.anchor, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof GuidanceResponseActionRelatedActionComponent))
          return false;
        GuidanceResponseActionRelatedActionComponent o = (GuidanceResponseActionRelatedActionComponent) other;
        return compareValues(relationship, o.relationship, true) && compareValues(anchor, o.anchor, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (actionIdentifier == null || actionIdentifier.isEmpty()) && (relationship == null || relationship.isEmpty())
           && (offset == null || offset.isEmpty()) && (anchor == null || anchor.isEmpty());
      }

  public String fhirType() {
    return "GuidanceResponse.action.relatedAction";

  }

  }

    @Block()
    public static class GuidanceResponseActionBehaviorComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The type of the behavior to be described, such as grouping, visual, or selection behaviors.
         */
        @Child(name = "type", type = {Coding.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The type of behavior (grouping, precheck, selection, cardinality, etc)", formalDefinition="The type of the behavior to be described, such as grouping, visual, or selection behaviors." )
        protected Coding type;

        /**
         * The specific behavior. The code used here is determined by the type of behavior being described. For example, the grouping behavior uses the grouping-behavior-type valueset.
         */
        @Child(name = "value", type = {Coding.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Specific behavior (e.g. required, at-most-one, single, etc)", formalDefinition="The specific behavior. The code used here is determined by the type of behavior being described. For example, the grouping behavior uses the grouping-behavior-type valueset." )
        protected Coding value;

        private static final long serialVersionUID = -1054119695L;

    /**
     * Constructor
     */
      public GuidanceResponseActionBehaviorComponent() {
        super();
      }

    /**
     * Constructor
     */
      public GuidanceResponseActionBehaviorComponent(Coding type, Coding value) {
        super();
        this.type = type;
        this.value = value;
      }

        /**
         * @return {@link #type} (The type of the behavior to be described, such as grouping, visual, or selection behaviors.)
         */
        public Coding getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create GuidanceResponseActionBehaviorComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new Coding(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (The type of the behavior to be described, such as grouping, visual, or selection behaviors.)
         */
        public GuidanceResponseActionBehaviorComponent setType(Coding value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #value} (The specific behavior. The code used here is determined by the type of behavior being described. For example, the grouping behavior uses the grouping-behavior-type valueset.)
         */
        public Coding getValue() { 
          if (this.value == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create GuidanceResponseActionBehaviorComponent.value");
            else if (Configuration.doAutoCreate())
              this.value = new Coding(); // cc
          return this.value;
        }

        public boolean hasValue() { 
          return this.value != null && !this.value.isEmpty();
        }

        /**
         * @param value {@link #value} (The specific behavior. The code used here is determined by the type of behavior being described. For example, the grouping behavior uses the grouping-behavior-type valueset.)
         */
        public GuidanceResponseActionBehaviorComponent setValue(Coding value) { 
          this.value = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("type", "Coding", "The type of the behavior to be described, such as grouping, visual, or selection behaviors.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("value", "Coding", "The specific behavior. The code used here is determined by the type of behavior being described. For example, the grouping behavior uses the grouping-behavior-type valueset.", 0, java.lang.Integer.MAX_VALUE, value));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // Coding
        case 111972721: /*value*/ return this.value == null ? new Base[0] : new Base[] {this.value}; // Coding
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          this.type = castToCoding(value); // Coding
          break;
        case 111972721: // value
          this.value = castToCoding(value); // Coding
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type"))
          this.type = castToCoding(value); // Coding
        else if (name.equals("value"))
          this.value = castToCoding(value); // Coding
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610:  return getType(); // Coding
        case 111972721:  return getValue(); // Coding
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          this.type = new Coding();
          return this.type;
        }
        else if (name.equals("value")) {
          this.value = new Coding();
          return this.value;
        }
        else
          return super.addChild(name);
      }

      public GuidanceResponseActionBehaviorComponent copy() {
        GuidanceResponseActionBehaviorComponent dst = new GuidanceResponseActionBehaviorComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.value = value == null ? null : value.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof GuidanceResponseActionBehaviorComponent))
          return false;
        GuidanceResponseActionBehaviorComponent o = (GuidanceResponseActionBehaviorComponent) other;
        return compareDeep(type, o.type, true) && compareDeep(value, o.value, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof GuidanceResponseActionBehaviorComponent))
          return false;
        GuidanceResponseActionBehaviorComponent o = (GuidanceResponseActionBehaviorComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (type == null || type.isEmpty()) && (value == null || value.isEmpty())
          ;
      }

  public String fhirType() {
    return "GuidanceResponse.action.behavior";

  }

  }

    /**
     * The id of the request associated with this response. If an id was given as part of the request, it will be reproduced here to enable the requester to more easily identify the response in a multi-request scenario.
     */
    @Child(name = "requestId", type = {StringType.class}, order=0, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="The id of the request associated with this response, if any", formalDefinition="The id of the request associated with this response. If an id was given as part of the request, it will be reproduced here to enable the requester to more easily identify the response in a multi-request scenario." )
    protected StringType requestId;

    /**
     * A reference to the knowledge module that was invoked.
     */
    @Child(name = "module", type = {DecisionSupportServiceModule.class, DecisionSupportRule.class}, order=1, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="A reference to a knowledge module", formalDefinition="A reference to the knowledge module that was invoked." )
    protected Reference module;

    /**
     * The actual object that is the target of the reference (A reference to the knowledge module that was invoked.)
     */
    protected Resource moduleTarget;

    /**
     * The status of the response. If the evaluation is completed successfully, the status will indicate success. However, in order to complete the evaluation, the engine may require more information. In this case, the status will be data-required, and the response will contain a description of the additional required information. If the evaluation completed successfully, but the engine determines that a potentially more accurate response could be provided if more data was available, the status will be data-requested, and the response will contain a description of the additional requested information.
     */
    @Child(name = "status", type = {CodeType.class}, order=2, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="success | data-requested | data-required | in-progress | failure", formalDefinition="The status of the response. If the evaluation is completed successfully, the status will indicate success. However, in order to complete the evaluation, the engine may require more information. In this case, the status will be data-required, and the response will contain a description of the additional required information. If the evaluation completed successfully, but the engine determines that a potentially more accurate response could be provided if more data was available, the status will be data-requested, and the response will contain a description of the additional requested information." )
    protected Enumeration<GuidanceResponseStatus> status;

    /**
     * Messages resulting from the evaluation of the artifact or artifacts. As part of evaluating the request, the engine may produce informational or warning messages. These messages will be provided by this element.
     */
    @Child(name = "evaluationMessage", type = {OperationOutcome.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Messages resulting from the evaluation of the artifact or artifacts", formalDefinition="Messages resulting from the evaluation of the artifact or artifacts. As part of evaluating the request, the engine may produce informational or warning messages. These messages will be provided by this element." )
    protected List<Reference> evaluationMessage;
    /**
     * The actual objects that are the target of the reference (Messages resulting from the evaluation of the artifact or artifacts. As part of evaluating the request, the engine may produce informational or warning messages. These messages will be provided by this element.)
     */
    protected List<OperationOutcome> evaluationMessageTarget;


    /**
     * The output parameters of the evaluation, if any. Many modules will result in the return of specific resources such as procedure or communication requests that are returned as part of the operation result. However, modules may define specific outputs that would be returned as the result of the evaluation, and these would be returned in this element.
     */
    @Child(name = "outputParameters", type = {Parameters.class}, order=4, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="The output parameters of the evaluation, if any", formalDefinition="The output parameters of the evaluation, if any. Many modules will result in the return of specific resources such as procedure or communication requests that are returned as part of the operation result. However, modules may define specific outputs that would be returned as the result of the evaluation, and these would be returned in this element." )
    protected Reference outputParameters;

    /**
     * The actual object that is the target of the reference (The output parameters of the evaluation, if any. Many modules will result in the return of specific resources such as procedure or communication requests that are returned as part of the operation result. However, modules may define specific outputs that would be returned as the result of the evaluation, and these would be returned in this element.)
     */
    protected Parameters outputParametersTarget;

    /**
     * The actions, if any, produced by the evaluation of the artifact.
     */
    @Child(name = "action", type = {}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Proposed actions, if any", formalDefinition="The actions, if any, produced by the evaluation of the artifact." )
    protected List<GuidanceResponseActionComponent> action;

    /**
     * If the evaluation could not be completed due to lack of information, or additional information would potentially result in a more accurate response, this element will a description of the data required in order to proceed with the evaluation. A subsequent request to the service should include this data.
     */
    @Child(name = "dataRequirement", type = {DataRequirement.class}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Additional required data", formalDefinition="If the evaluation could not be completed due to lack of information, or additional information would potentially result in a more accurate response, this element will a description of the data required in order to proceed with the evaluation. A subsequent request to the service should include this data." )
    protected List<DataRequirement> dataRequirement;

    private static final long serialVersionUID = -918912174L;

  /**
   * Constructor
   */
    public GuidanceResponse() {
      super();
    }

  /**
   * Constructor
   */
    public GuidanceResponse(Reference module, Enumeration<GuidanceResponseStatus> status) {
      super();
      this.module = module;
      this.status = status;
    }

    /**
     * @return {@link #requestId} (The id of the request associated with this response. If an id was given as part of the request, it will be reproduced here to enable the requester to more easily identify the response in a multi-request scenario.). This is the underlying object with id, value and extensions. The accessor "getRequestId" gives direct access to the value
     */
    public StringType getRequestIdElement() { 
      if (this.requestId == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create GuidanceResponse.requestId");
        else if (Configuration.doAutoCreate())
          this.requestId = new StringType(); // bb
      return this.requestId;
    }

    public boolean hasRequestIdElement() { 
      return this.requestId != null && !this.requestId.isEmpty();
    }

    public boolean hasRequestId() { 
      return this.requestId != null && !this.requestId.isEmpty();
    }

    /**
     * @param value {@link #requestId} (The id of the request associated with this response. If an id was given as part of the request, it will be reproduced here to enable the requester to more easily identify the response in a multi-request scenario.). This is the underlying object with id, value and extensions. The accessor "getRequestId" gives direct access to the value
     */
    public GuidanceResponse setRequestIdElement(StringType value) { 
      this.requestId = value;
      return this;
    }

    /**
     * @return The id of the request associated with this response. If an id was given as part of the request, it will be reproduced here to enable the requester to more easily identify the response in a multi-request scenario.
     */
    public String getRequestId() { 
      return this.requestId == null ? null : this.requestId.getValue();
    }

    /**
     * @param value The id of the request associated with this response. If an id was given as part of the request, it will be reproduced here to enable the requester to more easily identify the response in a multi-request scenario.
     */
    public GuidanceResponse setRequestId(String value) { 
      if (Utilities.noString(value))
        this.requestId = null;
      else {
        if (this.requestId == null)
          this.requestId = new StringType();
        this.requestId.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #module} (A reference to the knowledge module that was invoked.)
     */
    public Reference getModule() { 
      if (this.module == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create GuidanceResponse.module");
        else if (Configuration.doAutoCreate())
          this.module = new Reference(); // cc
      return this.module;
    }

    public boolean hasModule() { 
      return this.module != null && !this.module.isEmpty();
    }

    /**
     * @param value {@link #module} (A reference to the knowledge module that was invoked.)
     */
    public GuidanceResponse setModule(Reference value) { 
      this.module = value;
      return this;
    }

    /**
     * @return {@link #module} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (A reference to the knowledge module that was invoked.)
     */
    public Resource getModuleTarget() { 
      return this.moduleTarget;
    }

    /**
     * @param value {@link #module} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (A reference to the knowledge module that was invoked.)
     */
    public GuidanceResponse setModuleTarget(Resource value) { 
      this.moduleTarget = value;
      return this;
    }

    /**
     * @return {@link #status} (The status of the response. If the evaluation is completed successfully, the status will indicate success. However, in order to complete the evaluation, the engine may require more information. In this case, the status will be data-required, and the response will contain a description of the additional required information. If the evaluation completed successfully, but the engine determines that a potentially more accurate response could be provided if more data was available, the status will be data-requested, and the response will contain a description of the additional requested information.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<GuidanceResponseStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create GuidanceResponse.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<GuidanceResponseStatus>(new GuidanceResponseStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (The status of the response. If the evaluation is completed successfully, the status will indicate success. However, in order to complete the evaluation, the engine may require more information. In this case, the status will be data-required, and the response will contain a description of the additional required information. If the evaluation completed successfully, but the engine determines that a potentially more accurate response could be provided if more data was available, the status will be data-requested, and the response will contain a description of the additional requested information.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public GuidanceResponse setStatusElement(Enumeration<GuidanceResponseStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The status of the response. If the evaluation is completed successfully, the status will indicate success. However, in order to complete the evaluation, the engine may require more information. In this case, the status will be data-required, and the response will contain a description of the additional required information. If the evaluation completed successfully, but the engine determines that a potentially more accurate response could be provided if more data was available, the status will be data-requested, and the response will contain a description of the additional requested information.
     */
    public GuidanceResponseStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The status of the response. If the evaluation is completed successfully, the status will indicate success. However, in order to complete the evaluation, the engine may require more information. In this case, the status will be data-required, and the response will contain a description of the additional required information. If the evaluation completed successfully, but the engine determines that a potentially more accurate response could be provided if more data was available, the status will be data-requested, and the response will contain a description of the additional requested information.
     */
    public GuidanceResponse setStatus(GuidanceResponseStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<GuidanceResponseStatus>(new GuidanceResponseStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #evaluationMessage} (Messages resulting from the evaluation of the artifact or artifacts. As part of evaluating the request, the engine may produce informational or warning messages. These messages will be provided by this element.)
     */
    public List<Reference> getEvaluationMessage() { 
      if (this.evaluationMessage == null)
        this.evaluationMessage = new ArrayList<Reference>();
      return this.evaluationMessage;
    }

    public boolean hasEvaluationMessage() { 
      if (this.evaluationMessage == null)
        return false;
      for (Reference item : this.evaluationMessage)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #evaluationMessage} (Messages resulting from the evaluation of the artifact or artifacts. As part of evaluating the request, the engine may produce informational or warning messages. These messages will be provided by this element.)
     */
    // syntactic sugar
    public Reference addEvaluationMessage() { //3
      Reference t = new Reference();
      if (this.evaluationMessage == null)
        this.evaluationMessage = new ArrayList<Reference>();
      this.evaluationMessage.add(t);
      return t;
    }

    // syntactic sugar
    public GuidanceResponse addEvaluationMessage(Reference t) { //3
      if (t == null)
        return this;
      if (this.evaluationMessage == null)
        this.evaluationMessage = new ArrayList<Reference>();
      this.evaluationMessage.add(t);
      return this;
    }

    /**
     * @return {@link #evaluationMessage} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. Messages resulting from the evaluation of the artifact or artifacts. As part of evaluating the request, the engine may produce informational or warning messages. These messages will be provided by this element.)
     */
    public List<OperationOutcome> getEvaluationMessageTarget() { 
      if (this.evaluationMessageTarget == null)
        this.evaluationMessageTarget = new ArrayList<OperationOutcome>();
      return this.evaluationMessageTarget;
    }

    // syntactic sugar
    /**
     * @return {@link #evaluationMessage} (Add an actual object that is the target of the reference. The reference library doesn't use these, but you can use this to hold the resources if you resolvethemt. Messages resulting from the evaluation of the artifact or artifacts. As part of evaluating the request, the engine may produce informational or warning messages. These messages will be provided by this element.)
     */
    public OperationOutcome addEvaluationMessageTarget() { 
      OperationOutcome r = new OperationOutcome();
      if (this.evaluationMessageTarget == null)
        this.evaluationMessageTarget = new ArrayList<OperationOutcome>();
      this.evaluationMessageTarget.add(r);
      return r;
    }

    /**
     * @return {@link #outputParameters} (The output parameters of the evaluation, if any. Many modules will result in the return of specific resources such as procedure or communication requests that are returned as part of the operation result. However, modules may define specific outputs that would be returned as the result of the evaluation, and these would be returned in this element.)
     */
    public Reference getOutputParameters() { 
      if (this.outputParameters == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create GuidanceResponse.outputParameters");
        else if (Configuration.doAutoCreate())
          this.outputParameters = new Reference(); // cc
      return this.outputParameters;
    }

    public boolean hasOutputParameters() { 
      return this.outputParameters != null && !this.outputParameters.isEmpty();
    }

    /**
     * @param value {@link #outputParameters} (The output parameters of the evaluation, if any. Many modules will result in the return of specific resources such as procedure or communication requests that are returned as part of the operation result. However, modules may define specific outputs that would be returned as the result of the evaluation, and these would be returned in this element.)
     */
    public GuidanceResponse setOutputParameters(Reference value) { 
      this.outputParameters = value;
      return this;
    }

    /**
     * @return {@link #outputParameters} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The output parameters of the evaluation, if any. Many modules will result in the return of specific resources such as procedure or communication requests that are returned as part of the operation result. However, modules may define specific outputs that would be returned as the result of the evaluation, and these would be returned in this element.)
     */
    public Parameters getOutputParametersTarget() { 
      if (this.outputParametersTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create GuidanceResponse.outputParameters");
        else if (Configuration.doAutoCreate())
          this.outputParametersTarget = new Parameters(); // aa
      return this.outputParametersTarget;
    }

    /**
     * @param value {@link #outputParameters} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The output parameters of the evaluation, if any. Many modules will result in the return of specific resources such as procedure or communication requests that are returned as part of the operation result. However, modules may define specific outputs that would be returned as the result of the evaluation, and these would be returned in this element.)
     */
    public GuidanceResponse setOutputParametersTarget(Parameters value) { 
      this.outputParametersTarget = value;
      return this;
    }

    /**
     * @return {@link #action} (The actions, if any, produced by the evaluation of the artifact.)
     */
    public List<GuidanceResponseActionComponent> getAction() { 
      if (this.action == null)
        this.action = new ArrayList<GuidanceResponseActionComponent>();
      return this.action;
    }

    public boolean hasAction() { 
      if (this.action == null)
        return false;
      for (GuidanceResponseActionComponent item : this.action)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #action} (The actions, if any, produced by the evaluation of the artifact.)
     */
    // syntactic sugar
    public GuidanceResponseActionComponent addAction() { //3
      GuidanceResponseActionComponent t = new GuidanceResponseActionComponent();
      if (this.action == null)
        this.action = new ArrayList<GuidanceResponseActionComponent>();
      this.action.add(t);
      return t;
    }

    // syntactic sugar
    public GuidanceResponse addAction(GuidanceResponseActionComponent t) { //3
      if (t == null)
        return this;
      if (this.action == null)
        this.action = new ArrayList<GuidanceResponseActionComponent>();
      this.action.add(t);
      return this;
    }

    /**
     * @return {@link #dataRequirement} (If the evaluation could not be completed due to lack of information, or additional information would potentially result in a more accurate response, this element will a description of the data required in order to proceed with the evaluation. A subsequent request to the service should include this data.)
     */
    public List<DataRequirement> getDataRequirement() { 
      if (this.dataRequirement == null)
        this.dataRequirement = new ArrayList<DataRequirement>();
      return this.dataRequirement;
    }

    public boolean hasDataRequirement() { 
      if (this.dataRequirement == null)
        return false;
      for (DataRequirement item : this.dataRequirement)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #dataRequirement} (If the evaluation could not be completed due to lack of information, or additional information would potentially result in a more accurate response, this element will a description of the data required in order to proceed with the evaluation. A subsequent request to the service should include this data.)
     */
    // syntactic sugar
    public DataRequirement addDataRequirement() { //3
      DataRequirement t = new DataRequirement();
      if (this.dataRequirement == null)
        this.dataRequirement = new ArrayList<DataRequirement>();
      this.dataRequirement.add(t);
      return t;
    }

    // syntactic sugar
    public GuidanceResponse addDataRequirement(DataRequirement t) { //3
      if (t == null)
        return this;
      if (this.dataRequirement == null)
        this.dataRequirement = new ArrayList<DataRequirement>();
      this.dataRequirement.add(t);
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("requestId", "string", "The id of the request associated with this response. If an id was given as part of the request, it will be reproduced here to enable the requester to more easily identify the response in a multi-request scenario.", 0, java.lang.Integer.MAX_VALUE, requestId));
        childrenList.add(new Property("module", "Reference(DecisionSupportServiceModule|DecisionSupportRule)", "A reference to the knowledge module that was invoked.", 0, java.lang.Integer.MAX_VALUE, module));
        childrenList.add(new Property("status", "code", "The status of the response. If the evaluation is completed successfully, the status will indicate success. However, in order to complete the evaluation, the engine may require more information. In this case, the status will be data-required, and the response will contain a description of the additional required information. If the evaluation completed successfully, but the engine determines that a potentially more accurate response could be provided if more data was available, the status will be data-requested, and the response will contain a description of the additional requested information.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("evaluationMessage", "Reference(OperationOutcome)", "Messages resulting from the evaluation of the artifact or artifacts. As part of evaluating the request, the engine may produce informational or warning messages. These messages will be provided by this element.", 0, java.lang.Integer.MAX_VALUE, evaluationMessage));
        childrenList.add(new Property("outputParameters", "Reference(Parameters)", "The output parameters of the evaluation, if any. Many modules will result in the return of specific resources such as procedure or communication requests that are returned as part of the operation result. However, modules may define specific outputs that would be returned as the result of the evaluation, and these would be returned in this element.", 0, java.lang.Integer.MAX_VALUE, outputParameters));
        childrenList.add(new Property("action", "", "The actions, if any, produced by the evaluation of the artifact.", 0, java.lang.Integer.MAX_VALUE, action));
        childrenList.add(new Property("dataRequirement", "DataRequirement", "If the evaluation could not be completed due to lack of information, or additional information would potentially result in a more accurate response, this element will a description of the data required in order to proceed with the evaluation. A subsequent request to the service should include this data.", 0, java.lang.Integer.MAX_VALUE, dataRequirement));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 693933066: /*requestId*/ return this.requestId == null ? new Base[0] : new Base[] {this.requestId}; // StringType
        case -1068784020: /*module*/ return this.module == null ? new Base[0] : new Base[] {this.module}; // Reference
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<GuidanceResponseStatus>
        case 1081619755: /*evaluationMessage*/ return this.evaluationMessage == null ? new Base[0] : this.evaluationMessage.toArray(new Base[this.evaluationMessage.size()]); // Reference
        case 525609419: /*outputParameters*/ return this.outputParameters == null ? new Base[0] : new Base[] {this.outputParameters}; // Reference
        case -1422950858: /*action*/ return this.action == null ? new Base[0] : this.action.toArray(new Base[this.action.size()]); // GuidanceResponseActionComponent
        case 629147193: /*dataRequirement*/ return this.dataRequirement == null ? new Base[0] : this.dataRequirement.toArray(new Base[this.dataRequirement.size()]); // DataRequirement
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 693933066: // requestId
          this.requestId = castToString(value); // StringType
          break;
        case -1068784020: // module
          this.module = castToReference(value); // Reference
          break;
        case -892481550: // status
          this.status = new GuidanceResponseStatusEnumFactory().fromType(value); // Enumeration<GuidanceResponseStatus>
          break;
        case 1081619755: // evaluationMessage
          this.getEvaluationMessage().add(castToReference(value)); // Reference
          break;
        case 525609419: // outputParameters
          this.outputParameters = castToReference(value); // Reference
          break;
        case -1422950858: // action
          this.getAction().add((GuidanceResponseActionComponent) value); // GuidanceResponseActionComponent
          break;
        case 629147193: // dataRequirement
          this.getDataRequirement().add(castToDataRequirement(value)); // DataRequirement
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("requestId"))
          this.requestId = castToString(value); // StringType
        else if (name.equals("module"))
          this.module = castToReference(value); // Reference
        else if (name.equals("status"))
          this.status = new GuidanceResponseStatusEnumFactory().fromType(value); // Enumeration<GuidanceResponseStatus>
        else if (name.equals("evaluationMessage"))
          this.getEvaluationMessage().add(castToReference(value));
        else if (name.equals("outputParameters"))
          this.outputParameters = castToReference(value); // Reference
        else if (name.equals("action"))
          this.getAction().add((GuidanceResponseActionComponent) value);
        else if (name.equals("dataRequirement"))
          this.getDataRequirement().add(castToDataRequirement(value));
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 693933066: throw new FHIRException("Cannot make property requestId as it is not a complex type"); // StringType
        case -1068784020:  return getModule(); // Reference
        case -892481550: throw new FHIRException("Cannot make property status as it is not a complex type"); // Enumeration<GuidanceResponseStatus>
        case 1081619755:  return addEvaluationMessage(); // Reference
        case 525609419:  return getOutputParameters(); // Reference
        case -1422950858:  return addAction(); // GuidanceResponseActionComponent
        case 629147193:  return addDataRequirement(); // DataRequirement
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("requestId")) {
          throw new FHIRException("Cannot call addChild on a primitive type GuidanceResponse.requestId");
        }
        else if (name.equals("module")) {
          this.module = new Reference();
          return this.module;
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type GuidanceResponse.status");
        }
        else if (name.equals("evaluationMessage")) {
          return addEvaluationMessage();
        }
        else if (name.equals("outputParameters")) {
          this.outputParameters = new Reference();
          return this.outputParameters;
        }
        else if (name.equals("action")) {
          return addAction();
        }
        else if (name.equals("dataRequirement")) {
          return addDataRequirement();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "GuidanceResponse";

  }

      public GuidanceResponse copy() {
        GuidanceResponse dst = new GuidanceResponse();
        copyValues(dst);
        dst.requestId = requestId == null ? null : requestId.copy();
        dst.module = module == null ? null : module.copy();
        dst.status = status == null ? null : status.copy();
        if (evaluationMessage != null) {
          dst.evaluationMessage = new ArrayList<Reference>();
          for (Reference i : evaluationMessage)
            dst.evaluationMessage.add(i.copy());
        };
        dst.outputParameters = outputParameters == null ? null : outputParameters.copy();
        if (action != null) {
          dst.action = new ArrayList<GuidanceResponseActionComponent>();
          for (GuidanceResponseActionComponent i : action)
            dst.action.add(i.copy());
        };
        if (dataRequirement != null) {
          dst.dataRequirement = new ArrayList<DataRequirement>();
          for (DataRequirement i : dataRequirement)
            dst.dataRequirement.add(i.copy());
        };
        return dst;
      }

      protected GuidanceResponse typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof GuidanceResponse))
          return false;
        GuidanceResponse o = (GuidanceResponse) other;
        return compareDeep(requestId, o.requestId, true) && compareDeep(module, o.module, true) && compareDeep(status, o.status, true)
           && compareDeep(evaluationMessage, o.evaluationMessage, true) && compareDeep(outputParameters, o.outputParameters, true)
           && compareDeep(action, o.action, true) && compareDeep(dataRequirement, o.dataRequirement, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof GuidanceResponse))
          return false;
        GuidanceResponse o = (GuidanceResponse) other;
        return compareValues(requestId, o.requestId, true) && compareValues(status, o.status, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (requestId == null || requestId.isEmpty()) && (module == null || module.isEmpty())
           && (status == null || status.isEmpty()) && (evaluationMessage == null || evaluationMessage.isEmpty())
           && (outputParameters == null || outputParameters.isEmpty()) && (action == null || action.isEmpty())
           && (dataRequirement == null || dataRequirement.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.GuidanceResponse;
   }


}

