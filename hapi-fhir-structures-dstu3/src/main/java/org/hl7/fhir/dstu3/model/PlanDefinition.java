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

// Generated on Fri, Jul 1, 2016 14:13-0400 for FHIR v1.4.0

import java.util.*;

import org.hl7.fhir.utilities.Utilities;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ChildOrder;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Block;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.dstu3.exceptions.FHIRException;
/**
 * This resource allows for the definition of an order set as a sharable, consumable, and executable artifact in support of clinical decision support.
 */
@ResourceDef(name="PlanDefinition", profile="http://hl7.org/fhir/Profile/PlanDefinition")
public class PlanDefinition extends DomainResource {

    public enum PlanActionRelationshipType {
        /**
         * The action must be performed before the related action
         */
        BEFORE, 
        /**
         * The action must be performed after the related action
         */
        AFTER, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static PlanActionRelationshipType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("before".equals(codeString))
          return BEFORE;
        if ("after".equals(codeString))
          return AFTER;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown PlanActionRelationshipType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case BEFORE: return "before";
            case AFTER: return "after";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case BEFORE: return "http://hl7.org/fhir/planaction-relationship-type";
            case AFTER: return "http://hl7.org/fhir/planaction-relationship-type";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case BEFORE: return "The action must be performed before the related action";
            case AFTER: return "The action must be performed after the related action";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case BEFORE: return "Before";
            case AFTER: return "After";
            default: return "?";
          }
        }
    }

  public static class PlanActionRelationshipTypeEnumFactory implements EnumFactory<PlanActionRelationshipType> {
    public PlanActionRelationshipType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("before".equals(codeString))
          return PlanActionRelationshipType.BEFORE;
        if ("after".equals(codeString))
          return PlanActionRelationshipType.AFTER;
        throw new IllegalArgumentException("Unknown PlanActionRelationshipType code '"+codeString+"'");
        }
        public Enumeration<PlanActionRelationshipType> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("before".equals(codeString))
          return new Enumeration<PlanActionRelationshipType>(this, PlanActionRelationshipType.BEFORE);
        if ("after".equals(codeString))
          return new Enumeration<PlanActionRelationshipType>(this, PlanActionRelationshipType.AFTER);
        throw new FHIRException("Unknown PlanActionRelationshipType code '"+codeString+"'");
        }
    public String toCode(PlanActionRelationshipType code) {
      if (code == PlanActionRelationshipType.BEFORE)
        return "before";
      if (code == PlanActionRelationshipType.AFTER)
        return "after";
      return "?";
      }
    public String toSystem(PlanActionRelationshipType code) {
      return code.getSystem();
      }
    }

    public enum PlanActionRelationshipAnchor {
        /**
         * The action relationship is anchored to the start of the related action
         */
        START, 
        /**
         * The action relationship is anchored to the end of the related action
         */
        END, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static PlanActionRelationshipAnchor fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("start".equals(codeString))
          return START;
        if ("end".equals(codeString))
          return END;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown PlanActionRelationshipAnchor code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case START: return "start";
            case END: return "end";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case START: return "http://hl7.org/fhir/planaction-relationship-anchor";
            case END: return "http://hl7.org/fhir/planaction-relationship-anchor";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case START: return "The action relationship is anchored to the start of the related action";
            case END: return "The action relationship is anchored to the end of the related action";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case START: return "Start";
            case END: return "End";
            default: return "?";
          }
        }
    }

  public static class PlanActionRelationshipAnchorEnumFactory implements EnumFactory<PlanActionRelationshipAnchor> {
    public PlanActionRelationshipAnchor fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("start".equals(codeString))
          return PlanActionRelationshipAnchor.START;
        if ("end".equals(codeString))
          return PlanActionRelationshipAnchor.END;
        throw new IllegalArgumentException("Unknown PlanActionRelationshipAnchor code '"+codeString+"'");
        }
        public Enumeration<PlanActionRelationshipAnchor> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("start".equals(codeString))
          return new Enumeration<PlanActionRelationshipAnchor>(this, PlanActionRelationshipAnchor.START);
        if ("end".equals(codeString))
          return new Enumeration<PlanActionRelationshipAnchor>(this, PlanActionRelationshipAnchor.END);
        throw new FHIRException("Unknown PlanActionRelationshipAnchor code '"+codeString+"'");
        }
    public String toCode(PlanActionRelationshipAnchor code) {
      if (code == PlanActionRelationshipAnchor.START)
        return "start";
      if (code == PlanActionRelationshipAnchor.END)
        return "end";
      return "?";
      }
    public String toSystem(PlanActionRelationshipAnchor code) {
      return code.getSystem();
      }
    }

    public enum PlanActionType {
        /**
         * The action is to create a new resource
         */
        CREATE, 
        /**
         * The action is to update an existing resource
         */
        UPDATE, 
        /**
         * The action is to remove an existing resource
         */
        REMOVE, 
        /**
         * The action is to fire a specific event
         */
        FIREEVENT, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static PlanActionType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("create".equals(codeString))
          return CREATE;
        if ("update".equals(codeString))
          return UPDATE;
        if ("remove".equals(codeString))
          return REMOVE;
        if ("fire-event".equals(codeString))
          return FIREEVENT;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown PlanActionType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case CREATE: return "create";
            case UPDATE: return "update";
            case REMOVE: return "remove";
            case FIREEVENT: return "fire-event";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case CREATE: return "http://hl7.org/fhir/planaction-type";
            case UPDATE: return "http://hl7.org/fhir/planaction-type";
            case REMOVE: return "http://hl7.org/fhir/planaction-type";
            case FIREEVENT: return "http://hl7.org/fhir/planaction-type";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case CREATE: return "The action is to create a new resource";
            case UPDATE: return "The action is to update an existing resource";
            case REMOVE: return "The action is to remove an existing resource";
            case FIREEVENT: return "The action is to fire a specific event";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case CREATE: return "Create";
            case UPDATE: return "Update";
            case REMOVE: return "Remove";
            case FIREEVENT: return "Fire Event";
            default: return "?";
          }
        }
    }

  public static class PlanActionTypeEnumFactory implements EnumFactory<PlanActionType> {
    public PlanActionType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("create".equals(codeString))
          return PlanActionType.CREATE;
        if ("update".equals(codeString))
          return PlanActionType.UPDATE;
        if ("remove".equals(codeString))
          return PlanActionType.REMOVE;
        if ("fire-event".equals(codeString))
          return PlanActionType.FIREEVENT;
        throw new IllegalArgumentException("Unknown PlanActionType code '"+codeString+"'");
        }
        public Enumeration<PlanActionType> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("create".equals(codeString))
          return new Enumeration<PlanActionType>(this, PlanActionType.CREATE);
        if ("update".equals(codeString))
          return new Enumeration<PlanActionType>(this, PlanActionType.UPDATE);
        if ("remove".equals(codeString))
          return new Enumeration<PlanActionType>(this, PlanActionType.REMOVE);
        if ("fire-event".equals(codeString))
          return new Enumeration<PlanActionType>(this, PlanActionType.FIREEVENT);
        throw new FHIRException("Unknown PlanActionType code '"+codeString+"'");
        }
    public String toCode(PlanActionType code) {
      if (code == PlanActionType.CREATE)
        return "create";
      if (code == PlanActionType.UPDATE)
        return "update";
      if (code == PlanActionType.REMOVE)
        return "remove";
      if (code == PlanActionType.FIREEVENT)
        return "fire-event";
      return "?";
      }
    public String toSystem(PlanActionType code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class PlanDefinitionActionDefinitionComponent extends BackboneElement implements IBaseBackboneElement {
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
         * Didactic or other informational resources associated with the action that can be provided to the CDS recipient. Information resources can include inline text commentary and links to web resources.
         */
        @Child(name = "documentation", type = {Attachment.class}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Supporting documentation for the intended performer of the action", formalDefinition="Didactic or other informational resources associated with the action that can be provided to the CDS recipient. Information resources can include inline text commentary and links to web resources." )
        protected List<Attachment> documentation;

        /**
         * A description of when the action should be triggered.
         */
        @Child(name = "triggerDefinition", type = {TriggerDefinition.class}, order=9, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="When the action should be triggered", formalDefinition="A description of when the action should be triggered." )
        protected TriggerDefinition triggerDefinition;

        /**
         * An expression specifying whether or not the action is applicable in a given context.
         */
        @Child(name = "condition", type = {StringType.class}, order=10, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Whether or not the action is applicable", formalDefinition="An expression specifying whether or not the action is applicable in a given context." )
        protected StringType condition;

        /**
         * A relationship to another action such as "before" or "30-60 minutes after start of".
         */
        @Child(name = "relatedAction", type = {}, order=11, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Relationship to another action", formalDefinition="A relationship to another action such as \"before\" or \"30-60 minutes after start of\"." )
        protected PlanDefinitionActionDefinitionRelatedActionComponent relatedAction;

        /**
         * The type of action to perform (create, update, remove).
         */
        @Child(name = "type", type = {CodeType.class}, order=12, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="create | update | remove | fire-event", formalDefinition="The type of action to perform (create, update, remove)." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/planaction-type")
        protected Enumeration<PlanActionType> type;

        /**
         * A behavior associated with the action. Behaviors define how the action is to be presented and/or executed within the receiving environment.
         */
        @Child(name = "behavior", type = {}, order=13, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Defines behaviors such as selection and grouping", formalDefinition="A behavior associated with the action. Behaviors define how the action is to be presented and/or executed within the receiving environment." )
        protected List<PlanDefinitionActionDefinitionBehaviorComponent> behavior;

        /**
         * The resource that is the target of the action (e.g. CommunicationRequest). The resource described here defines any aspects of the action that can be specified statically (i.e. are known at the time of definition).
         */
        @Child(name = "activityDefinition", type = {ActivityDefinition.class}, order=14, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Description of the activity to be performed", formalDefinition="The resource that is the target of the action (e.g. CommunicationRequest). The resource described here defines any aspects of the action that can be specified statically (i.e. are known at the time of definition)." )
        protected Reference activityDefinition;

        /**
         * The actual object that is the target of the reference (The resource that is the target of the action (e.g. CommunicationRequest). The resource described here defines any aspects of the action that can be specified statically (i.e. are known at the time of definition).)
         */
        protected ActivityDefinition activityDefinitionTarget;

        /**
         * Customizations that should be applied to the statically defined resource. For example, if the dosage of a medication must be computed based on the patient's weight, a customization would be used to specify an expression that calculated the weight, and the path on the resource that would contain the result.
         */
        @Child(name = "customization", type = {}, order=15, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Dynamic aspects of the definition", formalDefinition="Customizations that should be applied to the statically defined resource. For example, if the dosage of a medication must be computed based on the patient's weight, a customization would be used to specify an expression that calculated the weight, and the path on the resource that would contain the result." )
        protected List<PlanDefinitionActionDefinitionCustomizationComponent> customization;

        /**
         * Sub actions that are contained within the action. The behavior of this action determines the functionality of the sub-actions. For example, a selection behavior of at-most-one indicates that of the sub-actions, at most one may be chosen as part of realizing the action definition.
         */
        @Child(name = "actionDefinition", type = {PlanDefinitionActionDefinitionComponent.class}, order=16, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="A sub-action", formalDefinition="Sub actions that are contained within the action. The behavior of this action determines the functionality of the sub-actions. For example, a selection behavior of at-most-one indicates that of the sub-actions, at most one may be chosen as part of realizing the action definition." )
        protected List<PlanDefinitionActionDefinitionComponent> actionDefinition;

        private static final long serialVersionUID = 295965155L;

    /**
     * Constructor
     */
      public PlanDefinitionActionDefinitionComponent() {
        super();
      }

        /**
         * @return {@link #actionIdentifier} (A unique identifier for the action. The identifier SHALL be unique within the container in which it appears, and MAY be universally unique.)
         */
        public Identifier getActionIdentifier() { 
          if (this.actionIdentifier == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PlanDefinitionActionDefinitionComponent.actionIdentifier");
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
        public PlanDefinitionActionDefinitionComponent setActionIdentifier(Identifier value) { 
          this.actionIdentifier = value;
          return this;
        }

        /**
         * @return {@link #label} (A user-visible label for the action.). This is the underlying object with id, value and extensions. The accessor "getLabel" gives direct access to the value
         */
        public StringType getLabelElement() { 
          if (this.label == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PlanDefinitionActionDefinitionComponent.label");
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
        public PlanDefinitionActionDefinitionComponent setLabelElement(StringType value) { 
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
        public PlanDefinitionActionDefinitionComponent setLabel(String value) { 
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
              throw new Error("Attempt to auto-create PlanDefinitionActionDefinitionComponent.title");
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
        public PlanDefinitionActionDefinitionComponent setTitleElement(StringType value) { 
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
        public PlanDefinitionActionDefinitionComponent setTitle(String value) { 
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
              throw new Error("Attempt to auto-create PlanDefinitionActionDefinitionComponent.description");
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
        public PlanDefinitionActionDefinitionComponent setDescriptionElement(StringType value) { 
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
        public PlanDefinitionActionDefinitionComponent setDescription(String value) { 
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
              throw new Error("Attempt to auto-create PlanDefinitionActionDefinitionComponent.textEquivalent");
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
        public PlanDefinitionActionDefinitionComponent setTextEquivalentElement(StringType value) { 
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
        public PlanDefinitionActionDefinitionComponent setTextEquivalent(String value) { 
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

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public PlanDefinitionActionDefinitionComponent setConcept(List<CodeableConcept> theConcept) { 
          this.concept = theConcept;
          return this;
        }

        public boolean hasConcept() { 
          if (this.concept == null)
            return false;
          for (CodeableConcept item : this.concept)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public CodeableConcept addConcept() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.concept == null)
            this.concept = new ArrayList<CodeableConcept>();
          this.concept.add(t);
          return t;
        }

        public PlanDefinitionActionDefinitionComponent addConcept(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.concept == null)
            this.concept = new ArrayList<CodeableConcept>();
          this.concept.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #concept}, creating it if it does not already exist
         */
        public CodeableConcept getConceptFirstRep() { 
          if (getConcept().isEmpty()) {
            addConcept();
          }
          return getConcept().get(0);
        }

        /**
         * @return {@link #supportingEvidence} (The evidence grade and the sources of evidence for this action.)
         */
        public List<Attachment> getSupportingEvidence() { 
          if (this.supportingEvidence == null)
            this.supportingEvidence = new ArrayList<Attachment>();
          return this.supportingEvidence;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public PlanDefinitionActionDefinitionComponent setSupportingEvidence(List<Attachment> theSupportingEvidence) { 
          this.supportingEvidence = theSupportingEvidence;
          return this;
        }

        public boolean hasSupportingEvidence() { 
          if (this.supportingEvidence == null)
            return false;
          for (Attachment item : this.supportingEvidence)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public Attachment addSupportingEvidence() { //3
          Attachment t = new Attachment();
          if (this.supportingEvidence == null)
            this.supportingEvidence = new ArrayList<Attachment>();
          this.supportingEvidence.add(t);
          return t;
        }

        public PlanDefinitionActionDefinitionComponent addSupportingEvidence(Attachment t) { //3
          if (t == null)
            return this;
          if (this.supportingEvidence == null)
            this.supportingEvidence = new ArrayList<Attachment>();
          this.supportingEvidence.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #supportingEvidence}, creating it if it does not already exist
         */
        public Attachment getSupportingEvidenceFirstRep() { 
          if (getSupportingEvidence().isEmpty()) {
            addSupportingEvidence();
          }
          return getSupportingEvidence().get(0);
        }

        /**
         * @return {@link #documentation} (Didactic or other informational resources associated with the action that can be provided to the CDS recipient. Information resources can include inline text commentary and links to web resources.)
         */
        public List<Attachment> getDocumentation() { 
          if (this.documentation == null)
            this.documentation = new ArrayList<Attachment>();
          return this.documentation;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public PlanDefinitionActionDefinitionComponent setDocumentation(List<Attachment> theDocumentation) { 
          this.documentation = theDocumentation;
          return this;
        }

        public boolean hasDocumentation() { 
          if (this.documentation == null)
            return false;
          for (Attachment item : this.documentation)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public Attachment addDocumentation() { //3
          Attachment t = new Attachment();
          if (this.documentation == null)
            this.documentation = new ArrayList<Attachment>();
          this.documentation.add(t);
          return t;
        }

        public PlanDefinitionActionDefinitionComponent addDocumentation(Attachment t) { //3
          if (t == null)
            return this;
          if (this.documentation == null)
            this.documentation = new ArrayList<Attachment>();
          this.documentation.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #documentation}, creating it if it does not already exist
         */
        public Attachment getDocumentationFirstRep() { 
          if (getDocumentation().isEmpty()) {
            addDocumentation();
          }
          return getDocumentation().get(0);
        }

        /**
         * @return {@link #triggerDefinition} (A description of when the action should be triggered.)
         */
        public TriggerDefinition getTriggerDefinition() { 
          if (this.triggerDefinition == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PlanDefinitionActionDefinitionComponent.triggerDefinition");
            else if (Configuration.doAutoCreate())
              this.triggerDefinition = new TriggerDefinition(); // cc
          return this.triggerDefinition;
        }

        public boolean hasTriggerDefinition() { 
          return this.triggerDefinition != null && !this.triggerDefinition.isEmpty();
        }

        /**
         * @param value {@link #triggerDefinition} (A description of when the action should be triggered.)
         */
        public PlanDefinitionActionDefinitionComponent setTriggerDefinition(TriggerDefinition value) { 
          this.triggerDefinition = value;
          return this;
        }

        /**
         * @return {@link #condition} (An expression specifying whether or not the action is applicable in a given context.). This is the underlying object with id, value and extensions. The accessor "getCondition" gives direct access to the value
         */
        public StringType getConditionElement() { 
          if (this.condition == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PlanDefinitionActionDefinitionComponent.condition");
            else if (Configuration.doAutoCreate())
              this.condition = new StringType(); // bb
          return this.condition;
        }

        public boolean hasConditionElement() { 
          return this.condition != null && !this.condition.isEmpty();
        }

        public boolean hasCondition() { 
          return this.condition != null && !this.condition.isEmpty();
        }

        /**
         * @param value {@link #condition} (An expression specifying whether or not the action is applicable in a given context.). This is the underlying object with id, value and extensions. The accessor "getCondition" gives direct access to the value
         */
        public PlanDefinitionActionDefinitionComponent setConditionElement(StringType value) { 
          this.condition = value;
          return this;
        }

        /**
         * @return An expression specifying whether or not the action is applicable in a given context.
         */
        public String getCondition() { 
          return this.condition == null ? null : this.condition.getValue();
        }

        /**
         * @param value An expression specifying whether or not the action is applicable in a given context.
         */
        public PlanDefinitionActionDefinitionComponent setCondition(String value) { 
          if (Utilities.noString(value))
            this.condition = null;
          else {
            if (this.condition == null)
              this.condition = new StringType();
            this.condition.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #relatedAction} (A relationship to another action such as "before" or "30-60 minutes after start of".)
         */
        public PlanDefinitionActionDefinitionRelatedActionComponent getRelatedAction() { 
          if (this.relatedAction == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PlanDefinitionActionDefinitionComponent.relatedAction");
            else if (Configuration.doAutoCreate())
              this.relatedAction = new PlanDefinitionActionDefinitionRelatedActionComponent(); // cc
          return this.relatedAction;
        }

        public boolean hasRelatedAction() { 
          return this.relatedAction != null && !this.relatedAction.isEmpty();
        }

        /**
         * @param value {@link #relatedAction} (A relationship to another action such as "before" or "30-60 minutes after start of".)
         */
        public PlanDefinitionActionDefinitionComponent setRelatedAction(PlanDefinitionActionDefinitionRelatedActionComponent value) { 
          this.relatedAction = value;
          return this;
        }

        /**
         * @return {@link #type} (The type of action to perform (create, update, remove).). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public Enumeration<PlanActionType> getTypeElement() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PlanDefinitionActionDefinitionComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new Enumeration<PlanActionType>(new PlanActionTypeEnumFactory()); // bb
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
        public PlanDefinitionActionDefinitionComponent setTypeElement(Enumeration<PlanActionType> value) { 
          this.type = value;
          return this;
        }

        /**
         * @return The type of action to perform (create, update, remove).
         */
        public PlanActionType getType() { 
          return this.type == null ? null : this.type.getValue();
        }

        /**
         * @param value The type of action to perform (create, update, remove).
         */
        public PlanDefinitionActionDefinitionComponent setType(PlanActionType value) { 
          if (value == null)
            this.type = null;
          else {
            if (this.type == null)
              this.type = new Enumeration<PlanActionType>(new PlanActionTypeEnumFactory());
            this.type.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #behavior} (A behavior associated with the action. Behaviors define how the action is to be presented and/or executed within the receiving environment.)
         */
        public List<PlanDefinitionActionDefinitionBehaviorComponent> getBehavior() { 
          if (this.behavior == null)
            this.behavior = new ArrayList<PlanDefinitionActionDefinitionBehaviorComponent>();
          return this.behavior;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public PlanDefinitionActionDefinitionComponent setBehavior(List<PlanDefinitionActionDefinitionBehaviorComponent> theBehavior) { 
          this.behavior = theBehavior;
          return this;
        }

        public boolean hasBehavior() { 
          if (this.behavior == null)
            return false;
          for (PlanDefinitionActionDefinitionBehaviorComponent item : this.behavior)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public PlanDefinitionActionDefinitionBehaviorComponent addBehavior() { //3
          PlanDefinitionActionDefinitionBehaviorComponent t = new PlanDefinitionActionDefinitionBehaviorComponent();
          if (this.behavior == null)
            this.behavior = new ArrayList<PlanDefinitionActionDefinitionBehaviorComponent>();
          this.behavior.add(t);
          return t;
        }

        public PlanDefinitionActionDefinitionComponent addBehavior(PlanDefinitionActionDefinitionBehaviorComponent t) { //3
          if (t == null)
            return this;
          if (this.behavior == null)
            this.behavior = new ArrayList<PlanDefinitionActionDefinitionBehaviorComponent>();
          this.behavior.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #behavior}, creating it if it does not already exist
         */
        public PlanDefinitionActionDefinitionBehaviorComponent getBehaviorFirstRep() { 
          if (getBehavior().isEmpty()) {
            addBehavior();
          }
          return getBehavior().get(0);
        }

        /**
         * @return {@link #activityDefinition} (The resource that is the target of the action (e.g. CommunicationRequest). The resource described here defines any aspects of the action that can be specified statically (i.e. are known at the time of definition).)
         */
        public Reference getActivityDefinition() { 
          if (this.activityDefinition == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PlanDefinitionActionDefinitionComponent.activityDefinition");
            else if (Configuration.doAutoCreate())
              this.activityDefinition = new Reference(); // cc
          return this.activityDefinition;
        }

        public boolean hasActivityDefinition() { 
          return this.activityDefinition != null && !this.activityDefinition.isEmpty();
        }

        /**
         * @param value {@link #activityDefinition} (The resource that is the target of the action (e.g. CommunicationRequest). The resource described here defines any aspects of the action that can be specified statically (i.e. are known at the time of definition).)
         */
        public PlanDefinitionActionDefinitionComponent setActivityDefinition(Reference value) { 
          this.activityDefinition = value;
          return this;
        }

        /**
         * @return {@link #activityDefinition} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The resource that is the target of the action (e.g. CommunicationRequest). The resource described here defines any aspects of the action that can be specified statically (i.e. are known at the time of definition).)
         */
        public ActivityDefinition getActivityDefinitionTarget() { 
          if (this.activityDefinitionTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PlanDefinitionActionDefinitionComponent.activityDefinition");
            else if (Configuration.doAutoCreate())
              this.activityDefinitionTarget = new ActivityDefinition(); // aa
          return this.activityDefinitionTarget;
        }

        /**
         * @param value {@link #activityDefinition} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The resource that is the target of the action (e.g. CommunicationRequest). The resource described here defines any aspects of the action that can be specified statically (i.e. are known at the time of definition).)
         */
        public PlanDefinitionActionDefinitionComponent setActivityDefinitionTarget(ActivityDefinition value) { 
          this.activityDefinitionTarget = value;
          return this;
        }

        /**
         * @return {@link #customization} (Customizations that should be applied to the statically defined resource. For example, if the dosage of a medication must be computed based on the patient's weight, a customization would be used to specify an expression that calculated the weight, and the path on the resource that would contain the result.)
         */
        public List<PlanDefinitionActionDefinitionCustomizationComponent> getCustomization() { 
          if (this.customization == null)
            this.customization = new ArrayList<PlanDefinitionActionDefinitionCustomizationComponent>();
          return this.customization;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public PlanDefinitionActionDefinitionComponent setCustomization(List<PlanDefinitionActionDefinitionCustomizationComponent> theCustomization) { 
          this.customization = theCustomization;
          return this;
        }

        public boolean hasCustomization() { 
          if (this.customization == null)
            return false;
          for (PlanDefinitionActionDefinitionCustomizationComponent item : this.customization)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public PlanDefinitionActionDefinitionCustomizationComponent addCustomization() { //3
          PlanDefinitionActionDefinitionCustomizationComponent t = new PlanDefinitionActionDefinitionCustomizationComponent();
          if (this.customization == null)
            this.customization = new ArrayList<PlanDefinitionActionDefinitionCustomizationComponent>();
          this.customization.add(t);
          return t;
        }

        public PlanDefinitionActionDefinitionComponent addCustomization(PlanDefinitionActionDefinitionCustomizationComponent t) { //3
          if (t == null)
            return this;
          if (this.customization == null)
            this.customization = new ArrayList<PlanDefinitionActionDefinitionCustomizationComponent>();
          this.customization.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #customization}, creating it if it does not already exist
         */
        public PlanDefinitionActionDefinitionCustomizationComponent getCustomizationFirstRep() { 
          if (getCustomization().isEmpty()) {
            addCustomization();
          }
          return getCustomization().get(0);
        }

        /**
         * @return {@link #actionDefinition} (Sub actions that are contained within the action. The behavior of this action determines the functionality of the sub-actions. For example, a selection behavior of at-most-one indicates that of the sub-actions, at most one may be chosen as part of realizing the action definition.)
         */
        public List<PlanDefinitionActionDefinitionComponent> getActionDefinition() { 
          if (this.actionDefinition == null)
            this.actionDefinition = new ArrayList<PlanDefinitionActionDefinitionComponent>();
          return this.actionDefinition;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public PlanDefinitionActionDefinitionComponent setActionDefinition(List<PlanDefinitionActionDefinitionComponent> theActionDefinition) { 
          this.actionDefinition = theActionDefinition;
          return this;
        }

        public boolean hasActionDefinition() { 
          if (this.actionDefinition == null)
            return false;
          for (PlanDefinitionActionDefinitionComponent item : this.actionDefinition)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public PlanDefinitionActionDefinitionComponent addActionDefinition() { //3
          PlanDefinitionActionDefinitionComponent t = new PlanDefinitionActionDefinitionComponent();
          if (this.actionDefinition == null)
            this.actionDefinition = new ArrayList<PlanDefinitionActionDefinitionComponent>();
          this.actionDefinition.add(t);
          return t;
        }

        public PlanDefinitionActionDefinitionComponent addActionDefinition(PlanDefinitionActionDefinitionComponent t) { //3
          if (t == null)
            return this;
          if (this.actionDefinition == null)
            this.actionDefinition = new ArrayList<PlanDefinitionActionDefinitionComponent>();
          this.actionDefinition.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #actionDefinition}, creating it if it does not already exist
         */
        public PlanDefinitionActionDefinitionComponent getActionDefinitionFirstRep() { 
          if (getActionDefinition().isEmpty()) {
            addActionDefinition();
          }
          return getActionDefinition().get(0);
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
          childrenList.add(new Property("documentation", "Attachment", "Didactic or other informational resources associated with the action that can be provided to the CDS recipient. Information resources can include inline text commentary and links to web resources.", 0, java.lang.Integer.MAX_VALUE, documentation));
          childrenList.add(new Property("triggerDefinition", "TriggerDefinition", "A description of when the action should be triggered.", 0, java.lang.Integer.MAX_VALUE, triggerDefinition));
          childrenList.add(new Property("condition", "string", "An expression specifying whether or not the action is applicable in a given context.", 0, java.lang.Integer.MAX_VALUE, condition));
          childrenList.add(new Property("relatedAction", "", "A relationship to another action such as \"before\" or \"30-60 minutes after start of\".", 0, java.lang.Integer.MAX_VALUE, relatedAction));
          childrenList.add(new Property("type", "code", "The type of action to perform (create, update, remove).", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("behavior", "", "A behavior associated with the action. Behaviors define how the action is to be presented and/or executed within the receiving environment.", 0, java.lang.Integer.MAX_VALUE, behavior));
          childrenList.add(new Property("activityDefinition", "Reference(ActivityDefinition)", "The resource that is the target of the action (e.g. CommunicationRequest). The resource described here defines any aspects of the action that can be specified statically (i.e. are known at the time of definition).", 0, java.lang.Integer.MAX_VALUE, activityDefinition));
          childrenList.add(new Property("customization", "", "Customizations that should be applied to the statically defined resource. For example, if the dosage of a medication must be computed based on the patient's weight, a customization would be used to specify an expression that calculated the weight, and the path on the resource that would contain the result.", 0, java.lang.Integer.MAX_VALUE, customization));
          childrenList.add(new Property("actionDefinition", "@PlanDefinition.actionDefinition", "Sub actions that are contained within the action. The behavior of this action determines the functionality of the sub-actions. For example, a selection behavior of at-most-one indicates that of the sub-actions, at most one may be chosen as part of realizing the action definition.", 0, java.lang.Integer.MAX_VALUE, actionDefinition));
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
        case 1587405498: /*documentation*/ return this.documentation == null ? new Base[0] : this.documentation.toArray(new Base[this.documentation.size()]); // Attachment
        case 1126736171: /*triggerDefinition*/ return this.triggerDefinition == null ? new Base[0] : new Base[] {this.triggerDefinition}; // TriggerDefinition
        case -861311717: /*condition*/ return this.condition == null ? new Base[0] : new Base[] {this.condition}; // StringType
        case -384107967: /*relatedAction*/ return this.relatedAction == null ? new Base[0] : new Base[] {this.relatedAction}; // PlanDefinitionActionDefinitionRelatedActionComponent
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // Enumeration<PlanActionType>
        case 1510912594: /*behavior*/ return this.behavior == null ? new Base[0] : this.behavior.toArray(new Base[this.behavior.size()]); // PlanDefinitionActionDefinitionBehaviorComponent
        case -990265918: /*activityDefinition*/ return this.activityDefinition == null ? new Base[0] : new Base[] {this.activityDefinition}; // Reference
        case 1637263315: /*customization*/ return this.customization == null ? new Base[0] : this.customization.toArray(new Base[this.customization.size()]); // PlanDefinitionActionDefinitionCustomizationComponent
        case -285031383: /*actionDefinition*/ return this.actionDefinition == null ? new Base[0] : this.actionDefinition.toArray(new Base[this.actionDefinition.size()]); // PlanDefinitionActionDefinitionComponent
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
        case 1587405498: // documentation
          this.getDocumentation().add(castToAttachment(value)); // Attachment
          break;
        case 1126736171: // triggerDefinition
          this.triggerDefinition = castToTriggerDefinition(value); // TriggerDefinition
          break;
        case -861311717: // condition
          this.condition = castToString(value); // StringType
          break;
        case -384107967: // relatedAction
          this.relatedAction = (PlanDefinitionActionDefinitionRelatedActionComponent) value; // PlanDefinitionActionDefinitionRelatedActionComponent
          break;
        case 3575610: // type
          this.type = new PlanActionTypeEnumFactory().fromType(value); // Enumeration<PlanActionType>
          break;
        case 1510912594: // behavior
          this.getBehavior().add((PlanDefinitionActionDefinitionBehaviorComponent) value); // PlanDefinitionActionDefinitionBehaviorComponent
          break;
        case -990265918: // activityDefinition
          this.activityDefinition = castToReference(value); // Reference
          break;
        case 1637263315: // customization
          this.getCustomization().add((PlanDefinitionActionDefinitionCustomizationComponent) value); // PlanDefinitionActionDefinitionCustomizationComponent
          break;
        case -285031383: // actionDefinition
          this.getActionDefinition().add((PlanDefinitionActionDefinitionComponent) value); // PlanDefinitionActionDefinitionComponent
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
        else if (name.equals("documentation"))
          this.getDocumentation().add(castToAttachment(value));
        else if (name.equals("triggerDefinition"))
          this.triggerDefinition = castToTriggerDefinition(value); // TriggerDefinition
        else if (name.equals("condition"))
          this.condition = castToString(value); // StringType
        else if (name.equals("relatedAction"))
          this.relatedAction = (PlanDefinitionActionDefinitionRelatedActionComponent) value; // PlanDefinitionActionDefinitionRelatedActionComponent
        else if (name.equals("type"))
          this.type = new PlanActionTypeEnumFactory().fromType(value); // Enumeration<PlanActionType>
        else if (name.equals("behavior"))
          this.getBehavior().add((PlanDefinitionActionDefinitionBehaviorComponent) value);
        else if (name.equals("activityDefinition"))
          this.activityDefinition = castToReference(value); // Reference
        else if (name.equals("customization"))
          this.getCustomization().add((PlanDefinitionActionDefinitionCustomizationComponent) value);
        else if (name.equals("actionDefinition"))
          this.getActionDefinition().add((PlanDefinitionActionDefinitionComponent) value);
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
        case 1587405498:  return addDocumentation(); // Attachment
        case 1126736171:  return getTriggerDefinition(); // TriggerDefinition
        case -861311717: throw new FHIRException("Cannot make property condition as it is not a complex type"); // StringType
        case -384107967:  return getRelatedAction(); // PlanDefinitionActionDefinitionRelatedActionComponent
        case 3575610: throw new FHIRException("Cannot make property type as it is not a complex type"); // Enumeration<PlanActionType>
        case 1510912594:  return addBehavior(); // PlanDefinitionActionDefinitionBehaviorComponent
        case -990265918:  return getActivityDefinition(); // Reference
        case 1637263315:  return addCustomization(); // PlanDefinitionActionDefinitionCustomizationComponent
        case -285031383:  return addActionDefinition(); // PlanDefinitionActionDefinitionComponent
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
          throw new FHIRException("Cannot call addChild on a primitive type PlanDefinition.label");
        }
        else if (name.equals("title")) {
          throw new FHIRException("Cannot call addChild on a primitive type PlanDefinition.title");
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type PlanDefinition.description");
        }
        else if (name.equals("textEquivalent")) {
          throw new FHIRException("Cannot call addChild on a primitive type PlanDefinition.textEquivalent");
        }
        else if (name.equals("concept")) {
          return addConcept();
        }
        else if (name.equals("supportingEvidence")) {
          return addSupportingEvidence();
        }
        else if (name.equals("documentation")) {
          return addDocumentation();
        }
        else if (name.equals("triggerDefinition")) {
          this.triggerDefinition = new TriggerDefinition();
          return this.triggerDefinition;
        }
        else if (name.equals("condition")) {
          throw new FHIRException("Cannot call addChild on a primitive type PlanDefinition.condition");
        }
        else if (name.equals("relatedAction")) {
          this.relatedAction = new PlanDefinitionActionDefinitionRelatedActionComponent();
          return this.relatedAction;
        }
        else if (name.equals("type")) {
          throw new FHIRException("Cannot call addChild on a primitive type PlanDefinition.type");
        }
        else if (name.equals("behavior")) {
          return addBehavior();
        }
        else if (name.equals("activityDefinition")) {
          this.activityDefinition = new Reference();
          return this.activityDefinition;
        }
        else if (name.equals("customization")) {
          return addCustomization();
        }
        else if (name.equals("actionDefinition")) {
          return addActionDefinition();
        }
        else
          return super.addChild(name);
      }

      public PlanDefinitionActionDefinitionComponent copy() {
        PlanDefinitionActionDefinitionComponent dst = new PlanDefinitionActionDefinitionComponent();
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
        if (documentation != null) {
          dst.documentation = new ArrayList<Attachment>();
          for (Attachment i : documentation)
            dst.documentation.add(i.copy());
        };
        dst.triggerDefinition = triggerDefinition == null ? null : triggerDefinition.copy();
        dst.condition = condition == null ? null : condition.copy();
        dst.relatedAction = relatedAction == null ? null : relatedAction.copy();
        dst.type = type == null ? null : type.copy();
        if (behavior != null) {
          dst.behavior = new ArrayList<PlanDefinitionActionDefinitionBehaviorComponent>();
          for (PlanDefinitionActionDefinitionBehaviorComponent i : behavior)
            dst.behavior.add(i.copy());
        };
        dst.activityDefinition = activityDefinition == null ? null : activityDefinition.copy();
        if (customization != null) {
          dst.customization = new ArrayList<PlanDefinitionActionDefinitionCustomizationComponent>();
          for (PlanDefinitionActionDefinitionCustomizationComponent i : customization)
            dst.customization.add(i.copy());
        };
        if (actionDefinition != null) {
          dst.actionDefinition = new ArrayList<PlanDefinitionActionDefinitionComponent>();
          for (PlanDefinitionActionDefinitionComponent i : actionDefinition)
            dst.actionDefinition.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof PlanDefinitionActionDefinitionComponent))
          return false;
        PlanDefinitionActionDefinitionComponent o = (PlanDefinitionActionDefinitionComponent) other;
        return compareDeep(actionIdentifier, o.actionIdentifier, true) && compareDeep(label, o.label, true)
           && compareDeep(title, o.title, true) && compareDeep(description, o.description, true) && compareDeep(textEquivalent, o.textEquivalent, true)
           && compareDeep(concept, o.concept, true) && compareDeep(supportingEvidence, o.supportingEvidence, true)
           && compareDeep(documentation, o.documentation, true) && compareDeep(triggerDefinition, o.triggerDefinition, true)
           && compareDeep(condition, o.condition, true) && compareDeep(relatedAction, o.relatedAction, true)
           && compareDeep(type, o.type, true) && compareDeep(behavior, o.behavior, true) && compareDeep(activityDefinition, o.activityDefinition, true)
           && compareDeep(customization, o.customization, true) && compareDeep(actionDefinition, o.actionDefinition, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof PlanDefinitionActionDefinitionComponent))
          return false;
        PlanDefinitionActionDefinitionComponent o = (PlanDefinitionActionDefinitionComponent) other;
        return compareValues(label, o.label, true) && compareValues(title, o.title, true) && compareValues(description, o.description, true)
           && compareValues(textEquivalent, o.textEquivalent, true) && compareValues(condition, o.condition, true)
           && compareValues(type, o.type, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(actionIdentifier, label, title
          , description, textEquivalent, concept, supportingEvidence, documentation, triggerDefinition
          , condition, relatedAction, type, behavior, activityDefinition, customization, actionDefinition
          );
      }

  public String fhirType() {
    return "PlanDefinition.actionDefinition";

  }

  }

    @Block()
    public static class PlanDefinitionActionDefinitionRelatedActionComponent extends BackboneElement implements IBaseBackboneElement {
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
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/planaction-relationship-type")
        protected Enumeration<PlanActionRelationshipType> relationship;

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
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/planaction-relationship-anchor")
        protected Enumeration<PlanActionRelationshipAnchor> anchor;

        private static final long serialVersionUID = -2079568789L;

    /**
     * Constructor
     */
      public PlanDefinitionActionDefinitionRelatedActionComponent() {
        super();
      }

    /**
     * Constructor
     */
      public PlanDefinitionActionDefinitionRelatedActionComponent(Identifier actionIdentifier, Enumeration<PlanActionRelationshipType> relationship) {
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
              throw new Error("Attempt to auto-create PlanDefinitionActionDefinitionRelatedActionComponent.actionIdentifier");
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
        public PlanDefinitionActionDefinitionRelatedActionComponent setActionIdentifier(Identifier value) { 
          this.actionIdentifier = value;
          return this;
        }

        /**
         * @return {@link #relationship} (The relationship of this action to the related action.). This is the underlying object with id, value and extensions. The accessor "getRelationship" gives direct access to the value
         */
        public Enumeration<PlanActionRelationshipType> getRelationshipElement() { 
          if (this.relationship == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PlanDefinitionActionDefinitionRelatedActionComponent.relationship");
            else if (Configuration.doAutoCreate())
              this.relationship = new Enumeration<PlanActionRelationshipType>(new PlanActionRelationshipTypeEnumFactory()); // bb
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
        public PlanDefinitionActionDefinitionRelatedActionComponent setRelationshipElement(Enumeration<PlanActionRelationshipType> value) { 
          this.relationship = value;
          return this;
        }

        /**
         * @return The relationship of this action to the related action.
         */
        public PlanActionRelationshipType getRelationship() { 
          return this.relationship == null ? null : this.relationship.getValue();
        }

        /**
         * @param value The relationship of this action to the related action.
         */
        public PlanDefinitionActionDefinitionRelatedActionComponent setRelationship(PlanActionRelationshipType value) { 
            if (this.relationship == null)
              this.relationship = new Enumeration<PlanActionRelationshipType>(new PlanActionRelationshipTypeEnumFactory());
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
        public PlanDefinitionActionDefinitionRelatedActionComponent setOffset(Type value) { 
          this.offset = value;
          return this;
        }

        /**
         * @return {@link #anchor} (An optional indicator for how the relationship is anchored to the related action. For example "before the start" or "before the end" of the related action.). This is the underlying object with id, value and extensions. The accessor "getAnchor" gives direct access to the value
         */
        public Enumeration<PlanActionRelationshipAnchor> getAnchorElement() { 
          if (this.anchor == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PlanDefinitionActionDefinitionRelatedActionComponent.anchor");
            else if (Configuration.doAutoCreate())
              this.anchor = new Enumeration<PlanActionRelationshipAnchor>(new PlanActionRelationshipAnchorEnumFactory()); // bb
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
        public PlanDefinitionActionDefinitionRelatedActionComponent setAnchorElement(Enumeration<PlanActionRelationshipAnchor> value) { 
          this.anchor = value;
          return this;
        }

        /**
         * @return An optional indicator for how the relationship is anchored to the related action. For example "before the start" or "before the end" of the related action.
         */
        public PlanActionRelationshipAnchor getAnchor() { 
          return this.anchor == null ? null : this.anchor.getValue();
        }

        /**
         * @param value An optional indicator for how the relationship is anchored to the related action. For example "before the start" or "before the end" of the related action.
         */
        public PlanDefinitionActionDefinitionRelatedActionComponent setAnchor(PlanActionRelationshipAnchor value) { 
          if (value == null)
            this.anchor = null;
          else {
            if (this.anchor == null)
              this.anchor = new Enumeration<PlanActionRelationshipAnchor>(new PlanActionRelationshipAnchorEnumFactory());
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
        case -261851592: /*relationship*/ return this.relationship == null ? new Base[0] : new Base[] {this.relationship}; // Enumeration<PlanActionRelationshipType>
        case -1019779949: /*offset*/ return this.offset == null ? new Base[0] : new Base[] {this.offset}; // Type
        case -1413299531: /*anchor*/ return this.anchor == null ? new Base[0] : new Base[] {this.anchor}; // Enumeration<PlanActionRelationshipAnchor>
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
          this.relationship = new PlanActionRelationshipTypeEnumFactory().fromType(value); // Enumeration<PlanActionRelationshipType>
          break;
        case -1019779949: // offset
          this.offset = (Type) value; // Type
          break;
        case -1413299531: // anchor
          this.anchor = new PlanActionRelationshipAnchorEnumFactory().fromType(value); // Enumeration<PlanActionRelationshipAnchor>
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("actionIdentifier"))
          this.actionIdentifier = castToIdentifier(value); // Identifier
        else if (name.equals("relationship"))
          this.relationship = new PlanActionRelationshipTypeEnumFactory().fromType(value); // Enumeration<PlanActionRelationshipType>
        else if (name.equals("offset[x]"))
          this.offset = (Type) value; // Type
        else if (name.equals("anchor"))
          this.anchor = new PlanActionRelationshipAnchorEnumFactory().fromType(value); // Enumeration<PlanActionRelationshipAnchor>
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -889046145:  return getActionIdentifier(); // Identifier
        case -261851592: throw new FHIRException("Cannot make property relationship as it is not a complex type"); // Enumeration<PlanActionRelationshipType>
        case -1960684787:  return getOffset(); // Type
        case -1413299531: throw new FHIRException("Cannot make property anchor as it is not a complex type"); // Enumeration<PlanActionRelationshipAnchor>
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
          throw new FHIRException("Cannot call addChild on a primitive type PlanDefinition.relationship");
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
          throw new FHIRException("Cannot call addChild on a primitive type PlanDefinition.anchor");
        }
        else
          return super.addChild(name);
      }

      public PlanDefinitionActionDefinitionRelatedActionComponent copy() {
        PlanDefinitionActionDefinitionRelatedActionComponent dst = new PlanDefinitionActionDefinitionRelatedActionComponent();
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
        if (!(other instanceof PlanDefinitionActionDefinitionRelatedActionComponent))
          return false;
        PlanDefinitionActionDefinitionRelatedActionComponent o = (PlanDefinitionActionDefinitionRelatedActionComponent) other;
        return compareDeep(actionIdentifier, o.actionIdentifier, true) && compareDeep(relationship, o.relationship, true)
           && compareDeep(offset, o.offset, true) && compareDeep(anchor, o.anchor, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof PlanDefinitionActionDefinitionRelatedActionComponent))
          return false;
        PlanDefinitionActionDefinitionRelatedActionComponent o = (PlanDefinitionActionDefinitionRelatedActionComponent) other;
        return compareValues(relationship, o.relationship, true) && compareValues(anchor, o.anchor, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(actionIdentifier, relationship
          , offset, anchor);
      }

  public String fhirType() {
    return "PlanDefinition.actionDefinition.relatedAction";

  }

  }

    @Block()
    public static class PlanDefinitionActionDefinitionBehaviorComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The type of the behavior to be described, such as grouping, visual, or selection behaviors.
         */
        @Child(name = "type", type = {Coding.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The type of behavior (grouping, precheck, selection, cardinality, etc)", formalDefinition="The type of the behavior to be described, such as grouping, visual, or selection behaviors." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/planaction-behavior-type")
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
      public PlanDefinitionActionDefinitionBehaviorComponent() {
        super();
      }

    /**
     * Constructor
     */
      public PlanDefinitionActionDefinitionBehaviorComponent(Coding type, Coding value) {
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
              throw new Error("Attempt to auto-create PlanDefinitionActionDefinitionBehaviorComponent.type");
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
        public PlanDefinitionActionDefinitionBehaviorComponent setType(Coding value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #value} (The specific behavior. The code used here is determined by the type of behavior being described. For example, the grouping behavior uses the grouping-behavior-type valueset.)
         */
        public Coding getValue() { 
          if (this.value == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PlanDefinitionActionDefinitionBehaviorComponent.value");
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
        public PlanDefinitionActionDefinitionBehaviorComponent setValue(Coding value) { 
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

      public PlanDefinitionActionDefinitionBehaviorComponent copy() {
        PlanDefinitionActionDefinitionBehaviorComponent dst = new PlanDefinitionActionDefinitionBehaviorComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.value = value == null ? null : value.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof PlanDefinitionActionDefinitionBehaviorComponent))
          return false;
        PlanDefinitionActionDefinitionBehaviorComponent o = (PlanDefinitionActionDefinitionBehaviorComponent) other;
        return compareDeep(type, o.type, true) && compareDeep(value, o.value, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof PlanDefinitionActionDefinitionBehaviorComponent))
          return false;
        PlanDefinitionActionDefinitionBehaviorComponent o = (PlanDefinitionActionDefinitionBehaviorComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(type, value);
      }

  public String fhirType() {
    return "PlanDefinition.actionDefinition.behavior";

  }

  }

    @Block()
    public static class PlanDefinitionActionDefinitionCustomizationComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The path to the element to be customized. This is the path on the resource that will hold the result of the calculation defined by the expression.
         */
        @Child(name = "path", type = {StringType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The path to the element to be set dynamically", formalDefinition="The path to the element to be customized. This is the path on the resource that will hold the result of the calculation defined by the expression." )
        protected StringType path;

        /**
         * An expression specifying the value of the customized element.
         */
        @Child(name = "expression", type = {StringType.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="An expression that provides the dynamic value for the customization", formalDefinition="An expression specifying the value of the customized element." )
        protected StringType expression;

        private static final long serialVersionUID = -252690483L;

    /**
     * Constructor
     */
      public PlanDefinitionActionDefinitionCustomizationComponent() {
        super();
      }

    /**
     * Constructor
     */
      public PlanDefinitionActionDefinitionCustomizationComponent(StringType path, StringType expression) {
        super();
        this.path = path;
        this.expression = expression;
      }

        /**
         * @return {@link #path} (The path to the element to be customized. This is the path on the resource that will hold the result of the calculation defined by the expression.). This is the underlying object with id, value and extensions. The accessor "getPath" gives direct access to the value
         */
        public StringType getPathElement() { 
          if (this.path == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PlanDefinitionActionDefinitionCustomizationComponent.path");
            else if (Configuration.doAutoCreate())
              this.path = new StringType(); // bb
          return this.path;
        }

        public boolean hasPathElement() { 
          return this.path != null && !this.path.isEmpty();
        }

        public boolean hasPath() { 
          return this.path != null && !this.path.isEmpty();
        }

        /**
         * @param value {@link #path} (The path to the element to be customized. This is the path on the resource that will hold the result of the calculation defined by the expression.). This is the underlying object with id, value and extensions. The accessor "getPath" gives direct access to the value
         */
        public PlanDefinitionActionDefinitionCustomizationComponent setPathElement(StringType value) { 
          this.path = value;
          return this;
        }

        /**
         * @return The path to the element to be customized. This is the path on the resource that will hold the result of the calculation defined by the expression.
         */
        public String getPath() { 
          return this.path == null ? null : this.path.getValue();
        }

        /**
         * @param value The path to the element to be customized. This is the path on the resource that will hold the result of the calculation defined by the expression.
         */
        public PlanDefinitionActionDefinitionCustomizationComponent setPath(String value) { 
            if (this.path == null)
              this.path = new StringType();
            this.path.setValue(value);
          return this;
        }

        /**
         * @return {@link #expression} (An expression specifying the value of the customized element.). This is the underlying object with id, value and extensions. The accessor "getExpression" gives direct access to the value
         */
        public StringType getExpressionElement() { 
          if (this.expression == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PlanDefinitionActionDefinitionCustomizationComponent.expression");
            else if (Configuration.doAutoCreate())
              this.expression = new StringType(); // bb
          return this.expression;
        }

        public boolean hasExpressionElement() { 
          return this.expression != null && !this.expression.isEmpty();
        }

        public boolean hasExpression() { 
          return this.expression != null && !this.expression.isEmpty();
        }

        /**
         * @param value {@link #expression} (An expression specifying the value of the customized element.). This is the underlying object with id, value and extensions. The accessor "getExpression" gives direct access to the value
         */
        public PlanDefinitionActionDefinitionCustomizationComponent setExpressionElement(StringType value) { 
          this.expression = value;
          return this;
        }

        /**
         * @return An expression specifying the value of the customized element.
         */
        public String getExpression() { 
          return this.expression == null ? null : this.expression.getValue();
        }

        /**
         * @param value An expression specifying the value of the customized element.
         */
        public PlanDefinitionActionDefinitionCustomizationComponent setExpression(String value) { 
            if (this.expression == null)
              this.expression = new StringType();
            this.expression.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("path", "string", "The path to the element to be customized. This is the path on the resource that will hold the result of the calculation defined by the expression.", 0, java.lang.Integer.MAX_VALUE, path));
          childrenList.add(new Property("expression", "string", "An expression specifying the value of the customized element.", 0, java.lang.Integer.MAX_VALUE, expression));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3433509: /*path*/ return this.path == null ? new Base[0] : new Base[] {this.path}; // StringType
        case -1795452264: /*expression*/ return this.expression == null ? new Base[0] : new Base[] {this.expression}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3433509: // path
          this.path = castToString(value); // StringType
          break;
        case -1795452264: // expression
          this.expression = castToString(value); // StringType
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("path"))
          this.path = castToString(value); // StringType
        else if (name.equals("expression"))
          this.expression = castToString(value); // StringType
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3433509: throw new FHIRException("Cannot make property path as it is not a complex type"); // StringType
        case -1795452264: throw new FHIRException("Cannot make property expression as it is not a complex type"); // StringType
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("path")) {
          throw new FHIRException("Cannot call addChild on a primitive type PlanDefinition.path");
        }
        else if (name.equals("expression")) {
          throw new FHIRException("Cannot call addChild on a primitive type PlanDefinition.expression");
        }
        else
          return super.addChild(name);
      }

      public PlanDefinitionActionDefinitionCustomizationComponent copy() {
        PlanDefinitionActionDefinitionCustomizationComponent dst = new PlanDefinitionActionDefinitionCustomizationComponent();
        copyValues(dst);
        dst.path = path == null ? null : path.copy();
        dst.expression = expression == null ? null : expression.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof PlanDefinitionActionDefinitionCustomizationComponent))
          return false;
        PlanDefinitionActionDefinitionCustomizationComponent o = (PlanDefinitionActionDefinitionCustomizationComponent) other;
        return compareDeep(path, o.path, true) && compareDeep(expression, o.expression, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof PlanDefinitionActionDefinitionCustomizationComponent))
          return false;
        PlanDefinitionActionDefinitionCustomizationComponent o = (PlanDefinitionActionDefinitionCustomizationComponent) other;
        return compareValues(path, o.path, true) && compareValues(expression, o.expression, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(path, expression);
      }

  public String fhirType() {
    return "PlanDefinition.actionDefinition.customization";

  }

  }

    /**
     * An absolute URL that is used to identify this module when it is referenced. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this module definition is (or will be) published.
     */
    @Child(name = "url", type = {UriType.class}, order=0, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Logical URL to reference this module", formalDefinition="An absolute URL that is used to identify this module when it is referenced. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this module definition is (or will be) published." )
    protected UriType url;

    /**
     * A logical identifier for the module such as the CMS or NQF identifiers for a measure artifact.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Logical identifier(s) for the module", formalDefinition="A logical identifier for the module such as the CMS or NQF identifiers for a measure artifact." )
    protected List<Identifier> identifier;

    /**
     * The version of the module, if any. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge modules, refer to the Decision Support Service specification. Note that the version is required for non-experimental published artifact.
     */
    @Child(name = "version", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The version of the module, if any", formalDefinition="The version of the module, if any. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge modules, refer to the Decision Support Service specification. Note that the version is required for non-experimental published artifact." )
    protected StringType version;

    /**
     * A machine-friendly name for the module. This name should be usable as an identifier for the module by machine processing applications such as code generation.
     */
    @Child(name = "name", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="A machine-friendly name for the module", formalDefinition="A machine-friendly name for the module. This name should be usable as an identifier for the module by machine processing applications such as code generation." )
    protected StringType name;

    /**
     * A short, descriptive, user-friendly title for the module.
     */
    @Child(name = "title", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="A user-friendly title for the module", formalDefinition="A short, descriptive, user-friendly title for the module." )
    protected StringType title;

    /**
     * The status of the module.
     */
    @Child(name = "status", type = {CodeType.class}, order=5, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="draft | active | inactive", formalDefinition="The status of the module." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/module-metadata-status")
    protected CodeType status;

    /**
     * Determines whether the module was developed for testing purposes (or education/evaluation/marketing), and is not intended to be used in production environments.
     */
    @Child(name = "experimental", type = {BooleanType.class}, order=6, min=0, max=1, modifier=true, summary=true)
    @Description(shortDefinition="If for testing purposes, not real usage", formalDefinition="Determines whether the module was developed for testing purposes (or education/evaluation/marketing), and is not intended to be used in production environments." )
    protected BooleanType experimental;

    /**
     * A free text natural language description of the module from the consumer's perspective.
     */
    @Child(name = "description", type = {StringType.class}, order=7, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Natural language description of the module", formalDefinition="A free text natural language description of the module from the consumer's perspective." )
    protected StringType description;

    /**
     * A brief description of the purpose of the module.
     */
    @Child(name = "purpose", type = {StringType.class}, order=8, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Describes the purpose of the module", formalDefinition="A brief description of the purpose of the module." )
    protected StringType purpose;

    /**
     * A detailed description of how the module is used from a clinical perspective.
     */
    @Child(name = "usage", type = {StringType.class}, order=9, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Describes the clinical usage of the module", formalDefinition="A detailed description of how the module is used from a clinical perspective." )
    protected StringType usage;

    /**
     * The date on which the module was published.
     */
    @Child(name = "publicationDate", type = {DateType.class}, order=10, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Publication date for this version of the module", formalDefinition="The date on which the module was published." )
    protected DateType publicationDate;

    /**
     * The date on which the module content was last reviewed.
     */
    @Child(name = "lastReviewDate", type = {DateType.class}, order=11, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Last review date for the module", formalDefinition="The date on which the module content was last reviewed." )
    protected DateType lastReviewDate;

    /**
     * The period during which the module content is effective.
     */
    @Child(name = "effectivePeriod", type = {Period.class}, order=12, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="The effective date range for the module", formalDefinition="The period during which the module content is effective." )
    protected Period effectivePeriod;

    /**
     * Specifies various attributes of the patient population for whom and/or environment of care in which, the knowledge module is applicable.
     */
    @Child(name = "coverage", type = {UsageContext.class}, order=13, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Describes the context of use for this module", formalDefinition="Specifies various attributes of the patient population for whom and/or environment of care in which, the knowledge module is applicable." )
    protected List<UsageContext> coverage;

    /**
     * Clinical topics related to the content of the module.
     */
    @Child(name = "topic", type = {CodeableConcept.class}, order=14, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Descriptional topics for the module", formalDefinition="Clinical topics related to the content of the module." )
    protected List<CodeableConcept> topic;

    /**
     * A contributor to the content of the module, including authors, editors, reviewers, and endorsers.
     */
    @Child(name = "contributor", type = {Contributor.class}, order=15, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="A content contributor", formalDefinition="A contributor to the content of the module, including authors, editors, reviewers, and endorsers." )
    protected List<Contributor> contributor;

    /**
     * The name of the individual or organization that published the module (also known as the steward for the module). This information is required for non-experimental published artifacts.
     */
    @Child(name = "publisher", type = {StringType.class}, order=16, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Name of the publisher (Organization or individual)", formalDefinition="The name of the individual or organization that published the module (also known as the steward for the module). This information is required for non-experimental published artifacts." )
    protected StringType publisher;

    /**
     * Contact details to assist a user in finding and communicating with the publisher.
     */
    @Child(name = "contact", type = {ContactDetail.class}, order=17, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Contact details of the publisher", formalDefinition="Contact details to assist a user in finding and communicating with the publisher." )
    protected List<ContactDetail> contact;

    /**
     * A copyright statement relating to the module and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the module.
     */
    @Child(name = "copyright", type = {StringType.class}, order=18, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Use and/or publishing restrictions", formalDefinition="A copyright statement relating to the module and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the module." )
    protected StringType copyright;

    /**
     * Related resources such as additional documentation, justification, or bibliographic references.
     */
    @Child(name = "relatedResource", type = {RelatedResource.class}, order=19, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Related resources for the module", formalDefinition="Related resources such as additional documentation, justification, or bibliographic references." )
    protected List<RelatedResource> relatedResource;

    /**
     * A reference to a Library resource containing any formal logic used by the orderset.
     */
    @Child(name = "library", type = {Library.class}, order=20, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Logic used by the plan definition", formalDefinition="A reference to a Library resource containing any formal logic used by the orderset." )
    protected List<Reference> library;
    /**
     * The actual objects that are the target of the reference (A reference to a Library resource containing any formal logic used by the orderset.)
     */
    protected List<Library> libraryTarget;


    /**
     * An action to be taken as part of the plan.
     */
    @Child(name = "actionDefinition", type = {}, order=21, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Action defined by the plan", formalDefinition="An action to be taken as part of the plan." )
    protected List<PlanDefinitionActionDefinitionComponent> actionDefinition;

    private static final long serialVersionUID = -909451233L;

  /**
   * Constructor
   */
    public PlanDefinition() {
      super();
    }

  /**
   * Constructor
   */
    public PlanDefinition(CodeType status) {
      super();
      this.status = status;
    }

    /**
     * @return {@link #url} (An absolute URL that is used to identify this module when it is referenced. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this module definition is (or will be) published.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public UriType getUrlElement() { 
      if (this.url == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create PlanDefinition.url");
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
     * @param value {@link #url} (An absolute URL that is used to identify this module when it is referenced. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this module definition is (or will be) published.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public PlanDefinition setUrlElement(UriType value) { 
      this.url = value;
      return this;
    }

    /**
     * @return An absolute URL that is used to identify this module when it is referenced. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this module definition is (or will be) published.
     */
    public String getUrl() { 
      return this.url == null ? null : this.url.getValue();
    }

    /**
     * @param value An absolute URL that is used to identify this module when it is referenced. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this module definition is (or will be) published.
     */
    public PlanDefinition setUrl(String value) { 
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
     * @return {@link #identifier} (A logical identifier for the module such as the CMS or NQF identifiers for a measure artifact.)
     */
    public List<Identifier> getIdentifier() { 
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      return this.identifier;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public PlanDefinition setIdentifier(List<Identifier> theIdentifier) { 
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

    public PlanDefinition addIdentifier(Identifier t) { //3
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
     * @return {@link #version} (The version of the module, if any. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge modules, refer to the Decision Support Service specification. Note that the version is required for non-experimental published artifact.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
     */
    public StringType getVersionElement() { 
      if (this.version == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create PlanDefinition.version");
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
     * @param value {@link #version} (The version of the module, if any. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge modules, refer to the Decision Support Service specification. Note that the version is required for non-experimental published artifact.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
     */
    public PlanDefinition setVersionElement(StringType value) { 
      this.version = value;
      return this;
    }

    /**
     * @return The version of the module, if any. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge modules, refer to the Decision Support Service specification. Note that the version is required for non-experimental published artifact.
     */
    public String getVersion() { 
      return this.version == null ? null : this.version.getValue();
    }

    /**
     * @param value The version of the module, if any. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge modules, refer to the Decision Support Service specification. Note that the version is required for non-experimental published artifact.
     */
    public PlanDefinition setVersion(String value) { 
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
     * @return {@link #name} (A machine-friendly name for the module. This name should be usable as an identifier for the module by machine processing applications such as code generation.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public StringType getNameElement() { 
      if (this.name == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create PlanDefinition.name");
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
     * @param value {@link #name} (A machine-friendly name for the module. This name should be usable as an identifier for the module by machine processing applications such as code generation.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public PlanDefinition setNameElement(StringType value) { 
      this.name = value;
      return this;
    }

    /**
     * @return A machine-friendly name for the module. This name should be usable as an identifier for the module by machine processing applications such as code generation.
     */
    public String getName() { 
      return this.name == null ? null : this.name.getValue();
    }

    /**
     * @param value A machine-friendly name for the module. This name should be usable as an identifier for the module by machine processing applications such as code generation.
     */
    public PlanDefinition setName(String value) { 
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
     * @return {@link #title} (A short, descriptive, user-friendly title for the module.). This is the underlying object with id, value and extensions. The accessor "getTitle" gives direct access to the value
     */
    public StringType getTitleElement() { 
      if (this.title == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create PlanDefinition.title");
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
     * @param value {@link #title} (A short, descriptive, user-friendly title for the module.). This is the underlying object with id, value and extensions. The accessor "getTitle" gives direct access to the value
     */
    public PlanDefinition setTitleElement(StringType value) { 
      this.title = value;
      return this;
    }

    /**
     * @return A short, descriptive, user-friendly title for the module.
     */
    public String getTitle() { 
      return this.title == null ? null : this.title.getValue();
    }

    /**
     * @param value A short, descriptive, user-friendly title for the module.
     */
    public PlanDefinition setTitle(String value) { 
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
     * @return {@link #status} (The status of the module.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public CodeType getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create PlanDefinition.status");
        else if (Configuration.doAutoCreate())
          this.status = new CodeType(); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (The status of the module.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public PlanDefinition setStatusElement(CodeType value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The status of the module.
     */
    public String getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The status of the module.
     */
    public PlanDefinition setStatus(String value) { 
        if (this.status == null)
          this.status = new CodeType();
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #experimental} (Determines whether the module was developed for testing purposes (or education/evaluation/marketing), and is not intended to be used in production environments.). This is the underlying object with id, value and extensions. The accessor "getExperimental" gives direct access to the value
     */
    public BooleanType getExperimentalElement() { 
      if (this.experimental == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create PlanDefinition.experimental");
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
     * @param value {@link #experimental} (Determines whether the module was developed for testing purposes (or education/evaluation/marketing), and is not intended to be used in production environments.). This is the underlying object with id, value and extensions. The accessor "getExperimental" gives direct access to the value
     */
    public PlanDefinition setExperimentalElement(BooleanType value) { 
      this.experimental = value;
      return this;
    }

    /**
     * @return Determines whether the module was developed for testing purposes (or education/evaluation/marketing), and is not intended to be used in production environments.
     */
    public boolean getExperimental() { 
      return this.experimental == null || this.experimental.isEmpty() ? false : this.experimental.getValue();
    }

    /**
     * @param value Determines whether the module was developed for testing purposes (or education/evaluation/marketing), and is not intended to be used in production environments.
     */
    public PlanDefinition setExperimental(boolean value) { 
        if (this.experimental == null)
          this.experimental = new BooleanType();
        this.experimental.setValue(value);
      return this;
    }

    /**
     * @return {@link #description} (A free text natural language description of the module from the consumer's perspective.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public StringType getDescriptionElement() { 
      if (this.description == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create PlanDefinition.description");
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
     * @param value {@link #description} (A free text natural language description of the module from the consumer's perspective.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public PlanDefinition setDescriptionElement(StringType value) { 
      this.description = value;
      return this;
    }

    /**
     * @return A free text natural language description of the module from the consumer's perspective.
     */
    public String getDescription() { 
      return this.description == null ? null : this.description.getValue();
    }

    /**
     * @param value A free text natural language description of the module from the consumer's perspective.
     */
    public PlanDefinition setDescription(String value) { 
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
     * @return {@link #purpose} (A brief description of the purpose of the module.). This is the underlying object with id, value and extensions. The accessor "getPurpose" gives direct access to the value
     */
    public StringType getPurposeElement() { 
      if (this.purpose == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create PlanDefinition.purpose");
        else if (Configuration.doAutoCreate())
          this.purpose = new StringType(); // bb
      return this.purpose;
    }

    public boolean hasPurposeElement() { 
      return this.purpose != null && !this.purpose.isEmpty();
    }

    public boolean hasPurpose() { 
      return this.purpose != null && !this.purpose.isEmpty();
    }

    /**
     * @param value {@link #purpose} (A brief description of the purpose of the module.). This is the underlying object with id, value and extensions. The accessor "getPurpose" gives direct access to the value
     */
    public PlanDefinition setPurposeElement(StringType value) { 
      this.purpose = value;
      return this;
    }

    /**
     * @return A brief description of the purpose of the module.
     */
    public String getPurpose() { 
      return this.purpose == null ? null : this.purpose.getValue();
    }

    /**
     * @param value A brief description of the purpose of the module.
     */
    public PlanDefinition setPurpose(String value) { 
      if (Utilities.noString(value))
        this.purpose = null;
      else {
        if (this.purpose == null)
          this.purpose = new StringType();
        this.purpose.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #usage} (A detailed description of how the module is used from a clinical perspective.). This is the underlying object with id, value and extensions. The accessor "getUsage" gives direct access to the value
     */
    public StringType getUsageElement() { 
      if (this.usage == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create PlanDefinition.usage");
        else if (Configuration.doAutoCreate())
          this.usage = new StringType(); // bb
      return this.usage;
    }

    public boolean hasUsageElement() { 
      return this.usage != null && !this.usage.isEmpty();
    }

    public boolean hasUsage() { 
      return this.usage != null && !this.usage.isEmpty();
    }

    /**
     * @param value {@link #usage} (A detailed description of how the module is used from a clinical perspective.). This is the underlying object with id, value and extensions. The accessor "getUsage" gives direct access to the value
     */
    public PlanDefinition setUsageElement(StringType value) { 
      this.usage = value;
      return this;
    }

    /**
     * @return A detailed description of how the module is used from a clinical perspective.
     */
    public String getUsage() { 
      return this.usage == null ? null : this.usage.getValue();
    }

    /**
     * @param value A detailed description of how the module is used from a clinical perspective.
     */
    public PlanDefinition setUsage(String value) { 
      if (Utilities.noString(value))
        this.usage = null;
      else {
        if (this.usage == null)
          this.usage = new StringType();
        this.usage.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #publicationDate} (The date on which the module was published.). This is the underlying object with id, value and extensions. The accessor "getPublicationDate" gives direct access to the value
     */
    public DateType getPublicationDateElement() { 
      if (this.publicationDate == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create PlanDefinition.publicationDate");
        else if (Configuration.doAutoCreate())
          this.publicationDate = new DateType(); // bb
      return this.publicationDate;
    }

    public boolean hasPublicationDateElement() { 
      return this.publicationDate != null && !this.publicationDate.isEmpty();
    }

    public boolean hasPublicationDate() { 
      return this.publicationDate != null && !this.publicationDate.isEmpty();
    }

    /**
     * @param value {@link #publicationDate} (The date on which the module was published.). This is the underlying object with id, value and extensions. The accessor "getPublicationDate" gives direct access to the value
     */
    public PlanDefinition setPublicationDateElement(DateType value) { 
      this.publicationDate = value;
      return this;
    }

    /**
     * @return The date on which the module was published.
     */
    public Date getPublicationDate() { 
      return this.publicationDate == null ? null : this.publicationDate.getValue();
    }

    /**
     * @param value The date on which the module was published.
     */
    public PlanDefinition setPublicationDate(Date value) { 
      if (value == null)
        this.publicationDate = null;
      else {
        if (this.publicationDate == null)
          this.publicationDate = new DateType();
        this.publicationDate.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #lastReviewDate} (The date on which the module content was last reviewed.). This is the underlying object with id, value and extensions. The accessor "getLastReviewDate" gives direct access to the value
     */
    public DateType getLastReviewDateElement() { 
      if (this.lastReviewDate == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create PlanDefinition.lastReviewDate");
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
     * @param value {@link #lastReviewDate} (The date on which the module content was last reviewed.). This is the underlying object with id, value and extensions. The accessor "getLastReviewDate" gives direct access to the value
     */
    public PlanDefinition setLastReviewDateElement(DateType value) { 
      this.lastReviewDate = value;
      return this;
    }

    /**
     * @return The date on which the module content was last reviewed.
     */
    public Date getLastReviewDate() { 
      return this.lastReviewDate == null ? null : this.lastReviewDate.getValue();
    }

    /**
     * @param value The date on which the module content was last reviewed.
     */
    public PlanDefinition setLastReviewDate(Date value) { 
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
     * @return {@link #effectivePeriod} (The period during which the module content is effective.)
     */
    public Period getEffectivePeriod() { 
      if (this.effectivePeriod == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create PlanDefinition.effectivePeriod");
        else if (Configuration.doAutoCreate())
          this.effectivePeriod = new Period(); // cc
      return this.effectivePeriod;
    }

    public boolean hasEffectivePeriod() { 
      return this.effectivePeriod != null && !this.effectivePeriod.isEmpty();
    }

    /**
     * @param value {@link #effectivePeriod} (The period during which the module content is effective.)
     */
    public PlanDefinition setEffectivePeriod(Period value) { 
      this.effectivePeriod = value;
      return this;
    }

    /**
     * @return {@link #coverage} (Specifies various attributes of the patient population for whom and/or environment of care in which, the knowledge module is applicable.)
     */
    public List<UsageContext> getCoverage() { 
      if (this.coverage == null)
        this.coverage = new ArrayList<UsageContext>();
      return this.coverage;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public PlanDefinition setCoverage(List<UsageContext> theCoverage) { 
      this.coverage = theCoverage;
      return this;
    }

    public boolean hasCoverage() { 
      if (this.coverage == null)
        return false;
      for (UsageContext item : this.coverage)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public UsageContext addCoverage() { //3
      UsageContext t = new UsageContext();
      if (this.coverage == null)
        this.coverage = new ArrayList<UsageContext>();
      this.coverage.add(t);
      return t;
    }

    public PlanDefinition addCoverage(UsageContext t) { //3
      if (t == null)
        return this;
      if (this.coverage == null)
        this.coverage = new ArrayList<UsageContext>();
      this.coverage.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #coverage}, creating it if it does not already exist
     */
    public UsageContext getCoverageFirstRep() { 
      if (getCoverage().isEmpty()) {
        addCoverage();
      }
      return getCoverage().get(0);
    }

    /**
     * @return {@link #topic} (Clinical topics related to the content of the module.)
     */
    public List<CodeableConcept> getTopic() { 
      if (this.topic == null)
        this.topic = new ArrayList<CodeableConcept>();
      return this.topic;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public PlanDefinition setTopic(List<CodeableConcept> theTopic) { 
      this.topic = theTopic;
      return this;
    }

    public boolean hasTopic() { 
      if (this.topic == null)
        return false;
      for (CodeableConcept item : this.topic)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addTopic() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.topic == null)
        this.topic = new ArrayList<CodeableConcept>();
      this.topic.add(t);
      return t;
    }

    public PlanDefinition addTopic(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.topic == null)
        this.topic = new ArrayList<CodeableConcept>();
      this.topic.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #topic}, creating it if it does not already exist
     */
    public CodeableConcept getTopicFirstRep() { 
      if (getTopic().isEmpty()) {
        addTopic();
      }
      return getTopic().get(0);
    }

    /**
     * @return {@link #contributor} (A contributor to the content of the module, including authors, editors, reviewers, and endorsers.)
     */
    public List<Contributor> getContributor() { 
      if (this.contributor == null)
        this.contributor = new ArrayList<Contributor>();
      return this.contributor;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public PlanDefinition setContributor(List<Contributor> theContributor) { 
      this.contributor = theContributor;
      return this;
    }

    public boolean hasContributor() { 
      if (this.contributor == null)
        return false;
      for (Contributor item : this.contributor)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Contributor addContributor() { //3
      Contributor t = new Contributor();
      if (this.contributor == null)
        this.contributor = new ArrayList<Contributor>();
      this.contributor.add(t);
      return t;
    }

    public PlanDefinition addContributor(Contributor t) { //3
      if (t == null)
        return this;
      if (this.contributor == null)
        this.contributor = new ArrayList<Contributor>();
      this.contributor.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #contributor}, creating it if it does not already exist
     */
    public Contributor getContributorFirstRep() { 
      if (getContributor().isEmpty()) {
        addContributor();
      }
      return getContributor().get(0);
    }

    /**
     * @return {@link #publisher} (The name of the individual or organization that published the module (also known as the steward for the module). This information is required for non-experimental published artifacts.). This is the underlying object with id, value and extensions. The accessor "getPublisher" gives direct access to the value
     */
    public StringType getPublisherElement() { 
      if (this.publisher == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create PlanDefinition.publisher");
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
     * @param value {@link #publisher} (The name of the individual or organization that published the module (also known as the steward for the module). This information is required for non-experimental published artifacts.). This is the underlying object with id, value and extensions. The accessor "getPublisher" gives direct access to the value
     */
    public PlanDefinition setPublisherElement(StringType value) { 
      this.publisher = value;
      return this;
    }

    /**
     * @return The name of the individual or organization that published the module (also known as the steward for the module). This information is required for non-experimental published artifacts.
     */
    public String getPublisher() { 
      return this.publisher == null ? null : this.publisher.getValue();
    }

    /**
     * @param value The name of the individual or organization that published the module (also known as the steward for the module). This information is required for non-experimental published artifacts.
     */
    public PlanDefinition setPublisher(String value) { 
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
    public PlanDefinition setContact(List<ContactDetail> theContact) { 
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

    public PlanDefinition addContact(ContactDetail t) { //3
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
     * @return {@link #copyright} (A copyright statement relating to the module and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the module.). This is the underlying object with id, value and extensions. The accessor "getCopyright" gives direct access to the value
     */
    public StringType getCopyrightElement() { 
      if (this.copyright == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create PlanDefinition.copyright");
        else if (Configuration.doAutoCreate())
          this.copyright = new StringType(); // bb
      return this.copyright;
    }

    public boolean hasCopyrightElement() { 
      return this.copyright != null && !this.copyright.isEmpty();
    }

    public boolean hasCopyright() { 
      return this.copyright != null && !this.copyright.isEmpty();
    }

    /**
     * @param value {@link #copyright} (A copyright statement relating to the module and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the module.). This is the underlying object with id, value and extensions. The accessor "getCopyright" gives direct access to the value
     */
    public PlanDefinition setCopyrightElement(StringType value) { 
      this.copyright = value;
      return this;
    }

    /**
     * @return A copyright statement relating to the module and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the module.
     */
    public String getCopyright() { 
      return this.copyright == null ? null : this.copyright.getValue();
    }

    /**
     * @param value A copyright statement relating to the module and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the module.
     */
    public PlanDefinition setCopyright(String value) { 
      if (Utilities.noString(value))
        this.copyright = null;
      else {
        if (this.copyright == null)
          this.copyright = new StringType();
        this.copyright.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #relatedResource} (Related resources such as additional documentation, justification, or bibliographic references.)
     */
    public List<RelatedResource> getRelatedResource() { 
      if (this.relatedResource == null)
        this.relatedResource = new ArrayList<RelatedResource>();
      return this.relatedResource;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public PlanDefinition setRelatedResource(List<RelatedResource> theRelatedResource) { 
      this.relatedResource = theRelatedResource;
      return this;
    }

    public boolean hasRelatedResource() { 
      if (this.relatedResource == null)
        return false;
      for (RelatedResource item : this.relatedResource)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public RelatedResource addRelatedResource() { //3
      RelatedResource t = new RelatedResource();
      if (this.relatedResource == null)
        this.relatedResource = new ArrayList<RelatedResource>();
      this.relatedResource.add(t);
      return t;
    }

    public PlanDefinition addRelatedResource(RelatedResource t) { //3
      if (t == null)
        return this;
      if (this.relatedResource == null)
        this.relatedResource = new ArrayList<RelatedResource>();
      this.relatedResource.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #relatedResource}, creating it if it does not already exist
     */
    public RelatedResource getRelatedResourceFirstRep() { 
      if (getRelatedResource().isEmpty()) {
        addRelatedResource();
      }
      return getRelatedResource().get(0);
    }

    /**
     * @return {@link #library} (A reference to a Library resource containing any formal logic used by the orderset.)
     */
    public List<Reference> getLibrary() { 
      if (this.library == null)
        this.library = new ArrayList<Reference>();
      return this.library;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public PlanDefinition setLibrary(List<Reference> theLibrary) { 
      this.library = theLibrary;
      return this;
    }

    public boolean hasLibrary() { 
      if (this.library == null)
        return false;
      for (Reference item : this.library)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addLibrary() { //3
      Reference t = new Reference();
      if (this.library == null)
        this.library = new ArrayList<Reference>();
      this.library.add(t);
      return t;
    }

    public PlanDefinition addLibrary(Reference t) { //3
      if (t == null)
        return this;
      if (this.library == null)
        this.library = new ArrayList<Reference>();
      this.library.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #library}, creating it if it does not already exist
     */
    public Reference getLibraryFirstRep() { 
      if (getLibrary().isEmpty()) {
        addLibrary();
      }
      return getLibrary().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Library> getLibraryTarget() { 
      if (this.libraryTarget == null)
        this.libraryTarget = new ArrayList<Library>();
      return this.libraryTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public Library addLibraryTarget() { 
      Library r = new Library();
      if (this.libraryTarget == null)
        this.libraryTarget = new ArrayList<Library>();
      this.libraryTarget.add(r);
      return r;
    }

    /**
     * @return {@link #actionDefinition} (An action to be taken as part of the plan.)
     */
    public List<PlanDefinitionActionDefinitionComponent> getActionDefinition() { 
      if (this.actionDefinition == null)
        this.actionDefinition = new ArrayList<PlanDefinitionActionDefinitionComponent>();
      return this.actionDefinition;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public PlanDefinition setActionDefinition(List<PlanDefinitionActionDefinitionComponent> theActionDefinition) { 
      this.actionDefinition = theActionDefinition;
      return this;
    }

    public boolean hasActionDefinition() { 
      if (this.actionDefinition == null)
        return false;
      for (PlanDefinitionActionDefinitionComponent item : this.actionDefinition)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public PlanDefinitionActionDefinitionComponent addActionDefinition() { //3
      PlanDefinitionActionDefinitionComponent t = new PlanDefinitionActionDefinitionComponent();
      if (this.actionDefinition == null)
        this.actionDefinition = new ArrayList<PlanDefinitionActionDefinitionComponent>();
      this.actionDefinition.add(t);
      return t;
    }

    public PlanDefinition addActionDefinition(PlanDefinitionActionDefinitionComponent t) { //3
      if (t == null)
        return this;
      if (this.actionDefinition == null)
        this.actionDefinition = new ArrayList<PlanDefinitionActionDefinitionComponent>();
      this.actionDefinition.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #actionDefinition}, creating it if it does not already exist
     */
    public PlanDefinitionActionDefinitionComponent getActionDefinitionFirstRep() { 
      if (getActionDefinition().isEmpty()) {
        addActionDefinition();
      }
      return getActionDefinition().get(0);
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("url", "uri", "An absolute URL that is used to identify this module when it is referenced. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this module definition is (or will be) published.", 0, java.lang.Integer.MAX_VALUE, url));
        childrenList.add(new Property("identifier", "Identifier", "A logical identifier for the module such as the CMS or NQF identifiers for a measure artifact.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("version", "string", "The version of the module, if any. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge modules, refer to the Decision Support Service specification. Note that the version is required for non-experimental published artifact.", 0, java.lang.Integer.MAX_VALUE, version));
        childrenList.add(new Property("name", "string", "A machine-friendly name for the module. This name should be usable as an identifier for the module by machine processing applications such as code generation.", 0, java.lang.Integer.MAX_VALUE, name));
        childrenList.add(new Property("title", "string", "A short, descriptive, user-friendly title for the module.", 0, java.lang.Integer.MAX_VALUE, title));
        childrenList.add(new Property("status", "code", "The status of the module.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("experimental", "boolean", "Determines whether the module was developed for testing purposes (or education/evaluation/marketing), and is not intended to be used in production environments.", 0, java.lang.Integer.MAX_VALUE, experimental));
        childrenList.add(new Property("description", "string", "A free text natural language description of the module from the consumer's perspective.", 0, java.lang.Integer.MAX_VALUE, description));
        childrenList.add(new Property("purpose", "string", "A brief description of the purpose of the module.", 0, java.lang.Integer.MAX_VALUE, purpose));
        childrenList.add(new Property("usage", "string", "A detailed description of how the module is used from a clinical perspective.", 0, java.lang.Integer.MAX_VALUE, usage));
        childrenList.add(new Property("publicationDate", "date", "The date on which the module was published.", 0, java.lang.Integer.MAX_VALUE, publicationDate));
        childrenList.add(new Property("lastReviewDate", "date", "The date on which the module content was last reviewed.", 0, java.lang.Integer.MAX_VALUE, lastReviewDate));
        childrenList.add(new Property("effectivePeriod", "Period", "The period during which the module content is effective.", 0, java.lang.Integer.MAX_VALUE, effectivePeriod));
        childrenList.add(new Property("coverage", "UsageContext", "Specifies various attributes of the patient population for whom and/or environment of care in which, the knowledge module is applicable.", 0, java.lang.Integer.MAX_VALUE, coverage));
        childrenList.add(new Property("topic", "CodeableConcept", "Clinical topics related to the content of the module.", 0, java.lang.Integer.MAX_VALUE, topic));
        childrenList.add(new Property("contributor", "Contributor", "A contributor to the content of the module, including authors, editors, reviewers, and endorsers.", 0, java.lang.Integer.MAX_VALUE, contributor));
        childrenList.add(new Property("publisher", "string", "The name of the individual or organization that published the module (also known as the steward for the module). This information is required for non-experimental published artifacts.", 0, java.lang.Integer.MAX_VALUE, publisher));
        childrenList.add(new Property("contact", "ContactDetail", "Contact details to assist a user in finding and communicating with the publisher.", 0, java.lang.Integer.MAX_VALUE, contact));
        childrenList.add(new Property("copyright", "string", "A copyright statement relating to the module and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the module.", 0, java.lang.Integer.MAX_VALUE, copyright));
        childrenList.add(new Property("relatedResource", "RelatedResource", "Related resources such as additional documentation, justification, or bibliographic references.", 0, java.lang.Integer.MAX_VALUE, relatedResource));
        childrenList.add(new Property("library", "Reference(Library)", "A reference to a Library resource containing any formal logic used by the orderset.", 0, java.lang.Integer.MAX_VALUE, library));
        childrenList.add(new Property("actionDefinition", "", "An action to be taken as part of the plan.", 0, java.lang.Integer.MAX_VALUE, actionDefinition));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 116079: /*url*/ return this.url == null ? new Base[0] : new Base[] {this.url}; // UriType
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case 351608024: /*version*/ return this.version == null ? new Base[0] : new Base[] {this.version}; // StringType
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case 110371416: /*title*/ return this.title == null ? new Base[0] : new Base[] {this.title}; // StringType
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // CodeType
        case -404562712: /*experimental*/ return this.experimental == null ? new Base[0] : new Base[] {this.experimental}; // BooleanType
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // StringType
        case -220463842: /*purpose*/ return this.purpose == null ? new Base[0] : new Base[] {this.purpose}; // StringType
        case 111574433: /*usage*/ return this.usage == null ? new Base[0] : new Base[] {this.usage}; // StringType
        case 1470566394: /*publicationDate*/ return this.publicationDate == null ? new Base[0] : new Base[] {this.publicationDate}; // DateType
        case -1687512484: /*lastReviewDate*/ return this.lastReviewDate == null ? new Base[0] : new Base[] {this.lastReviewDate}; // DateType
        case -403934648: /*effectivePeriod*/ return this.effectivePeriod == null ? new Base[0] : new Base[] {this.effectivePeriod}; // Period
        case -351767064: /*coverage*/ return this.coverage == null ? new Base[0] : this.coverage.toArray(new Base[this.coverage.size()]); // UsageContext
        case 110546223: /*topic*/ return this.topic == null ? new Base[0] : this.topic.toArray(new Base[this.topic.size()]); // CodeableConcept
        case -1895276325: /*contributor*/ return this.contributor == null ? new Base[0] : this.contributor.toArray(new Base[this.contributor.size()]); // Contributor
        case 1447404028: /*publisher*/ return this.publisher == null ? new Base[0] : new Base[] {this.publisher}; // StringType
        case 951526432: /*contact*/ return this.contact == null ? new Base[0] : this.contact.toArray(new Base[this.contact.size()]); // ContactDetail
        case 1522889671: /*copyright*/ return this.copyright == null ? new Base[0] : new Base[] {this.copyright}; // StringType
        case 1554540889: /*relatedResource*/ return this.relatedResource == null ? new Base[0] : this.relatedResource.toArray(new Base[this.relatedResource.size()]); // RelatedResource
        case 166208699: /*library*/ return this.library == null ? new Base[0] : this.library.toArray(new Base[this.library.size()]); // Reference
        case -285031383: /*actionDefinition*/ return this.actionDefinition == null ? new Base[0] : this.actionDefinition.toArray(new Base[this.actionDefinition.size()]); // PlanDefinitionActionDefinitionComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 116079: // url
          this.url = castToUri(value); // UriType
          break;
        case -1618432855: // identifier
          this.getIdentifier().add(castToIdentifier(value)); // Identifier
          break;
        case 351608024: // version
          this.version = castToString(value); // StringType
          break;
        case 3373707: // name
          this.name = castToString(value); // StringType
          break;
        case 110371416: // title
          this.title = castToString(value); // StringType
          break;
        case -892481550: // status
          this.status = castToCode(value); // CodeType
          break;
        case -404562712: // experimental
          this.experimental = castToBoolean(value); // BooleanType
          break;
        case -1724546052: // description
          this.description = castToString(value); // StringType
          break;
        case -220463842: // purpose
          this.purpose = castToString(value); // StringType
          break;
        case 111574433: // usage
          this.usage = castToString(value); // StringType
          break;
        case 1470566394: // publicationDate
          this.publicationDate = castToDate(value); // DateType
          break;
        case -1687512484: // lastReviewDate
          this.lastReviewDate = castToDate(value); // DateType
          break;
        case -403934648: // effectivePeriod
          this.effectivePeriod = castToPeriod(value); // Period
          break;
        case -351767064: // coverage
          this.getCoverage().add(castToUsageContext(value)); // UsageContext
          break;
        case 110546223: // topic
          this.getTopic().add(castToCodeableConcept(value)); // CodeableConcept
          break;
        case -1895276325: // contributor
          this.getContributor().add(castToContributor(value)); // Contributor
          break;
        case 1447404028: // publisher
          this.publisher = castToString(value); // StringType
          break;
        case 951526432: // contact
          this.getContact().add(castToContactDetail(value)); // ContactDetail
          break;
        case 1522889671: // copyright
          this.copyright = castToString(value); // StringType
          break;
        case 1554540889: // relatedResource
          this.getRelatedResource().add(castToRelatedResource(value)); // RelatedResource
          break;
        case 166208699: // library
          this.getLibrary().add(castToReference(value)); // Reference
          break;
        case -285031383: // actionDefinition
          this.getActionDefinition().add((PlanDefinitionActionDefinitionComponent) value); // PlanDefinitionActionDefinitionComponent
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("url"))
          this.url = castToUri(value); // UriType
        else if (name.equals("identifier"))
          this.getIdentifier().add(castToIdentifier(value));
        else if (name.equals("version"))
          this.version = castToString(value); // StringType
        else if (name.equals("name"))
          this.name = castToString(value); // StringType
        else if (name.equals("title"))
          this.title = castToString(value); // StringType
        else if (name.equals("status"))
          this.status = castToCode(value); // CodeType
        else if (name.equals("experimental"))
          this.experimental = castToBoolean(value); // BooleanType
        else if (name.equals("description"))
          this.description = castToString(value); // StringType
        else if (name.equals("purpose"))
          this.purpose = castToString(value); // StringType
        else if (name.equals("usage"))
          this.usage = castToString(value); // StringType
        else if (name.equals("publicationDate"))
          this.publicationDate = castToDate(value); // DateType
        else if (name.equals("lastReviewDate"))
          this.lastReviewDate = castToDate(value); // DateType
        else if (name.equals("effectivePeriod"))
          this.effectivePeriod = castToPeriod(value); // Period
        else if (name.equals("coverage"))
          this.getCoverage().add(castToUsageContext(value));
        else if (name.equals("topic"))
          this.getTopic().add(castToCodeableConcept(value));
        else if (name.equals("contributor"))
          this.getContributor().add(castToContributor(value));
        else if (name.equals("publisher"))
          this.publisher = castToString(value); // StringType
        else if (name.equals("contact"))
          this.getContact().add(castToContactDetail(value));
        else if (name.equals("copyright"))
          this.copyright = castToString(value); // StringType
        else if (name.equals("relatedResource"))
          this.getRelatedResource().add(castToRelatedResource(value));
        else if (name.equals("library"))
          this.getLibrary().add(castToReference(value));
        else if (name.equals("actionDefinition"))
          this.getActionDefinition().add((PlanDefinitionActionDefinitionComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 116079: throw new FHIRException("Cannot make property url as it is not a complex type"); // UriType
        case -1618432855:  return addIdentifier(); // Identifier
        case 351608024: throw new FHIRException("Cannot make property version as it is not a complex type"); // StringType
        case 3373707: throw new FHIRException("Cannot make property name as it is not a complex type"); // StringType
        case 110371416: throw new FHIRException("Cannot make property title as it is not a complex type"); // StringType
        case -892481550: throw new FHIRException("Cannot make property status as it is not a complex type"); // CodeType
        case -404562712: throw new FHIRException("Cannot make property experimental as it is not a complex type"); // BooleanType
        case -1724546052: throw new FHIRException("Cannot make property description as it is not a complex type"); // StringType
        case -220463842: throw new FHIRException("Cannot make property purpose as it is not a complex type"); // StringType
        case 111574433: throw new FHIRException("Cannot make property usage as it is not a complex type"); // StringType
        case 1470566394: throw new FHIRException("Cannot make property publicationDate as it is not a complex type"); // DateType
        case -1687512484: throw new FHIRException("Cannot make property lastReviewDate as it is not a complex type"); // DateType
        case -403934648:  return getEffectivePeriod(); // Period
        case -351767064:  return addCoverage(); // UsageContext
        case 110546223:  return addTopic(); // CodeableConcept
        case -1895276325:  return addContributor(); // Contributor
        case 1447404028: throw new FHIRException("Cannot make property publisher as it is not a complex type"); // StringType
        case 951526432:  return addContact(); // ContactDetail
        case 1522889671: throw new FHIRException("Cannot make property copyright as it is not a complex type"); // StringType
        case 1554540889:  return addRelatedResource(); // RelatedResource
        case 166208699:  return addLibrary(); // Reference
        case -285031383:  return addActionDefinition(); // PlanDefinitionActionDefinitionComponent
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("url")) {
          throw new FHIRException("Cannot call addChild on a primitive type PlanDefinition.url");
        }
        else if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("version")) {
          throw new FHIRException("Cannot call addChild on a primitive type PlanDefinition.version");
        }
        else if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type PlanDefinition.name");
        }
        else if (name.equals("title")) {
          throw new FHIRException("Cannot call addChild on a primitive type PlanDefinition.title");
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type PlanDefinition.status");
        }
        else if (name.equals("experimental")) {
          throw new FHIRException("Cannot call addChild on a primitive type PlanDefinition.experimental");
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type PlanDefinition.description");
        }
        else if (name.equals("purpose")) {
          throw new FHIRException("Cannot call addChild on a primitive type PlanDefinition.purpose");
        }
        else if (name.equals("usage")) {
          throw new FHIRException("Cannot call addChild on a primitive type PlanDefinition.usage");
        }
        else if (name.equals("publicationDate")) {
          throw new FHIRException("Cannot call addChild on a primitive type PlanDefinition.publicationDate");
        }
        else if (name.equals("lastReviewDate")) {
          throw new FHIRException("Cannot call addChild on a primitive type PlanDefinition.lastReviewDate");
        }
        else if (name.equals("effectivePeriod")) {
          this.effectivePeriod = new Period();
          return this.effectivePeriod;
        }
        else if (name.equals("coverage")) {
          return addCoverage();
        }
        else if (name.equals("topic")) {
          return addTopic();
        }
        else if (name.equals("contributor")) {
          return addContributor();
        }
        else if (name.equals("publisher")) {
          throw new FHIRException("Cannot call addChild on a primitive type PlanDefinition.publisher");
        }
        else if (name.equals("contact")) {
          return addContact();
        }
        else if (name.equals("copyright")) {
          throw new FHIRException("Cannot call addChild on a primitive type PlanDefinition.copyright");
        }
        else if (name.equals("relatedResource")) {
          return addRelatedResource();
        }
        else if (name.equals("library")) {
          return addLibrary();
        }
        else if (name.equals("actionDefinition")) {
          return addActionDefinition();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "PlanDefinition";

  }

      public PlanDefinition copy() {
        PlanDefinition dst = new PlanDefinition();
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
        dst.description = description == null ? null : description.copy();
        dst.purpose = purpose == null ? null : purpose.copy();
        dst.usage = usage == null ? null : usage.copy();
        dst.publicationDate = publicationDate == null ? null : publicationDate.copy();
        dst.lastReviewDate = lastReviewDate == null ? null : lastReviewDate.copy();
        dst.effectivePeriod = effectivePeriod == null ? null : effectivePeriod.copy();
        if (coverage != null) {
          dst.coverage = new ArrayList<UsageContext>();
          for (UsageContext i : coverage)
            dst.coverage.add(i.copy());
        };
        if (topic != null) {
          dst.topic = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : topic)
            dst.topic.add(i.copy());
        };
        if (contributor != null) {
          dst.contributor = new ArrayList<Contributor>();
          for (Contributor i : contributor)
            dst.contributor.add(i.copy());
        };
        dst.publisher = publisher == null ? null : publisher.copy();
        if (contact != null) {
          dst.contact = new ArrayList<ContactDetail>();
          for (ContactDetail i : contact)
            dst.contact.add(i.copy());
        };
        dst.copyright = copyright == null ? null : copyright.copy();
        if (relatedResource != null) {
          dst.relatedResource = new ArrayList<RelatedResource>();
          for (RelatedResource i : relatedResource)
            dst.relatedResource.add(i.copy());
        };
        if (library != null) {
          dst.library = new ArrayList<Reference>();
          for (Reference i : library)
            dst.library.add(i.copy());
        };
        if (actionDefinition != null) {
          dst.actionDefinition = new ArrayList<PlanDefinitionActionDefinitionComponent>();
          for (PlanDefinitionActionDefinitionComponent i : actionDefinition)
            dst.actionDefinition.add(i.copy());
        };
        return dst;
      }

      protected PlanDefinition typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof PlanDefinition))
          return false;
        PlanDefinition o = (PlanDefinition) other;
        return compareDeep(url, o.url, true) && compareDeep(identifier, o.identifier, true) && compareDeep(version, o.version, true)
           && compareDeep(name, o.name, true) && compareDeep(title, o.title, true) && compareDeep(status, o.status, true)
           && compareDeep(experimental, o.experimental, true) && compareDeep(description, o.description, true)
           && compareDeep(purpose, o.purpose, true) && compareDeep(usage, o.usage, true) && compareDeep(publicationDate, o.publicationDate, true)
           && compareDeep(lastReviewDate, o.lastReviewDate, true) && compareDeep(effectivePeriod, o.effectivePeriod, true)
           && compareDeep(coverage, o.coverage, true) && compareDeep(topic, o.topic, true) && compareDeep(contributor, o.contributor, true)
           && compareDeep(publisher, o.publisher, true) && compareDeep(contact, o.contact, true) && compareDeep(copyright, o.copyright, true)
           && compareDeep(relatedResource, o.relatedResource, true) && compareDeep(library, o.library, true)
           && compareDeep(actionDefinition, o.actionDefinition, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof PlanDefinition))
          return false;
        PlanDefinition o = (PlanDefinition) other;
        return compareValues(url, o.url, true) && compareValues(version, o.version, true) && compareValues(name, o.name, true)
           && compareValues(title, o.title, true) && compareValues(status, o.status, true) && compareValues(experimental, o.experimental, true)
           && compareValues(description, o.description, true) && compareValues(purpose, o.purpose, true) && compareValues(usage, o.usage, true)
           && compareValues(publicationDate, o.publicationDate, true) && compareValues(lastReviewDate, o.lastReviewDate, true)
           && compareValues(publisher, o.publisher, true) && compareValues(copyright, o.copyright, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(url, identifier, version
          , name, title, status, experimental, description, purpose, usage, publicationDate
          , lastReviewDate, effectivePeriod, coverage, topic, contributor, publisher, contact
          , copyright, relatedResource, library, actionDefinition);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.PlanDefinition;
   }

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>Logical identifier for the module (e.g. CMS-143)</b><br>
   * Type: <b>token</b><br>
   * Path: <b>PlanDefinition.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="PlanDefinition.identifier", description="Logical identifier for the module (e.g. CMS-143)", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>Logical identifier for the module (e.g. CMS-143)</b><br>
   * Type: <b>token</b><br>
   * Path: <b>PlanDefinition.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>topic</b>
   * <p>
   * Description: <b>Topics associated with the module</b><br>
   * Type: <b>token</b><br>
   * Path: <b>PlanDefinition.topic</b><br>
   * </p>
   */
  @SearchParamDefinition(name="topic", path="PlanDefinition.topic", description="Topics associated with the module", type="token" )
  public static final String SP_TOPIC = "topic";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>topic</b>
   * <p>
   * Description: <b>Topics associated with the module</b><br>
   * Type: <b>token</b><br>
   * Path: <b>PlanDefinition.topic</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam TOPIC = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_TOPIC);

 /**
   * Search parameter: <b>description</b>
   * <p>
   * Description: <b>Text search against the description</b><br>
   * Type: <b>string</b><br>
   * Path: <b>PlanDefinition.description</b><br>
   * </p>
   */
  @SearchParamDefinition(name="description", path="PlanDefinition.description", description="Text search against the description", type="string" )
  public static final String SP_DESCRIPTION = "description";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>description</b>
   * <p>
   * Description: <b>Text search against the description</b><br>
   * Type: <b>string</b><br>
   * Path: <b>PlanDefinition.description</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam DESCRIPTION = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_DESCRIPTION);

 /**
   * Search parameter: <b>title</b>
   * <p>
   * Description: <b>Text search against the title</b><br>
   * Type: <b>string</b><br>
   * Path: <b>PlanDefinition.title</b><br>
   * </p>
   */
  @SearchParamDefinition(name="title", path="PlanDefinition.title", description="Text search against the title", type="string" )
  public static final String SP_TITLE = "title";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>title</b>
   * <p>
   * Description: <b>Text search against the title</b><br>
   * Type: <b>string</b><br>
   * Path: <b>PlanDefinition.title</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam TITLE = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_TITLE);

 /**
   * Search parameter: <b>version</b>
   * <p>
   * Description: <b>Version of the module (e.g. 1.0.0)</b><br>
   * Type: <b>string</b><br>
   * Path: <b>PlanDefinition.version</b><br>
   * </p>
   */
  @SearchParamDefinition(name="version", path="PlanDefinition.version", description="Version of the module (e.g. 1.0.0)", type="string" )
  public static final String SP_VERSION = "version";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>version</b>
   * <p>
   * Description: <b>Version of the module (e.g. 1.0.0)</b><br>
   * Type: <b>string</b><br>
   * Path: <b>PlanDefinition.version</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam VERSION = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_VERSION);

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>Status of the module</b><br>
   * Type: <b>token</b><br>
   * Path: <b>PlanDefinition.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="PlanDefinition.status", description="Status of the module", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>Status of the module</b><br>
   * Type: <b>token</b><br>
   * Path: <b>PlanDefinition.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);


}

