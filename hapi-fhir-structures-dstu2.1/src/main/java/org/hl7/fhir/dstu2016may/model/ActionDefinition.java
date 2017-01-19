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
import org.hl7.fhir.instance.model.api.IBaseDatatypeElement;
import org.hl7.fhir.instance.model.api.ICompositeType;
import org.hl7.fhir.utilities.Utilities;

import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.Description;
/**
 * The definition of an action to be performed. Some aspects of the definition are specified statically, and some aspects can be specified dynamically by referencing logic defined in a library.
 */
@DatatypeDef(name="ActionDefinition")
public class ActionDefinition extends Type implements ICompositeType {

    public enum ActionRelationshipType {
        /**
         * The action must be performed before the related action
         */
        BEFORE, 
        /**
         * The action must be performed after the related action
         */
        AFTER, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ActionRelationshipType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("before".equals(codeString))
          return BEFORE;
        if ("after".equals(codeString))
          return AFTER;
        throw new FHIRException("Unknown ActionRelationshipType code '"+codeString+"'");
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
            case BEFORE: return "http://hl7.org/fhir/action-relationship-type";
            case AFTER: return "http://hl7.org/fhir/action-relationship-type";
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

  public static class ActionRelationshipTypeEnumFactory implements EnumFactory<ActionRelationshipType> {
    public ActionRelationshipType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("before".equals(codeString))
          return ActionRelationshipType.BEFORE;
        if ("after".equals(codeString))
          return ActionRelationshipType.AFTER;
        throw new IllegalArgumentException("Unknown ActionRelationshipType code '"+codeString+"'");
        }
        public Enumeration<ActionRelationshipType> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("before".equals(codeString))
          return new Enumeration<ActionRelationshipType>(this, ActionRelationshipType.BEFORE);
        if ("after".equals(codeString))
          return new Enumeration<ActionRelationshipType>(this, ActionRelationshipType.AFTER);
        throw new FHIRException("Unknown ActionRelationshipType code '"+codeString+"'");
        }
    public String toCode(ActionRelationshipType code) {
      if (code == ActionRelationshipType.BEFORE)
        return "before";
      if (code == ActionRelationshipType.AFTER)
        return "after";
      return "?";
      }
    public String toSystem(ActionRelationshipType code) {
      return code.getSystem();
      }
    }

    public enum ActionRelationshipAnchor {
        /**
         * The action relationship is anchored to the start of the related action
         */
        START, 
        /**
         * The action relationship is anchored to the end of the related action
         */
        END, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ActionRelationshipAnchor fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("start".equals(codeString))
          return START;
        if ("end".equals(codeString))
          return END;
        throw new FHIRException("Unknown ActionRelationshipAnchor code '"+codeString+"'");
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
            case START: return "http://hl7.org/fhir/action-relationship-anchor";
            case END: return "http://hl7.org/fhir/action-relationship-anchor";
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

  public static class ActionRelationshipAnchorEnumFactory implements EnumFactory<ActionRelationshipAnchor> {
    public ActionRelationshipAnchor fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("start".equals(codeString))
          return ActionRelationshipAnchor.START;
        if ("end".equals(codeString))
          return ActionRelationshipAnchor.END;
        throw new IllegalArgumentException("Unknown ActionRelationshipAnchor code '"+codeString+"'");
        }
        public Enumeration<ActionRelationshipAnchor> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("start".equals(codeString))
          return new Enumeration<ActionRelationshipAnchor>(this, ActionRelationshipAnchor.START);
        if ("end".equals(codeString))
          return new Enumeration<ActionRelationshipAnchor>(this, ActionRelationshipAnchor.END);
        throw new FHIRException("Unknown ActionRelationshipAnchor code '"+codeString+"'");
        }
    public String toCode(ActionRelationshipAnchor code) {
      if (code == ActionRelationshipAnchor.START)
        return "start";
      if (code == ActionRelationshipAnchor.END)
        return "end";
      return "?";
      }
    public String toSystem(ActionRelationshipAnchor code) {
      return code.getSystem();
      }
    }

    public enum ParticipantType {
        /**
         * The participant is the patient under evaluation
         */
        PATIENT, 
        /**
         * The participant is a practitioner involved in the patient's care
         */
        PRACTITIONER, 
        /**
         * The participant is a person related to the patient
         */
        RELATEDPERSON, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ParticipantType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("patient".equals(codeString))
          return PATIENT;
        if ("practitioner".equals(codeString))
          return PRACTITIONER;
        if ("related-person".equals(codeString))
          return RELATEDPERSON;
        throw new FHIRException("Unknown ParticipantType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PATIENT: return "patient";
            case PRACTITIONER: return "practitioner";
            case RELATEDPERSON: return "related-person";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case PATIENT: return "http://hl7.org/fhir/action-participant-type";
            case PRACTITIONER: return "http://hl7.org/fhir/action-participant-type";
            case RELATEDPERSON: return "http://hl7.org/fhir/action-participant-type";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case PATIENT: return "The participant is the patient under evaluation";
            case PRACTITIONER: return "The participant is a practitioner involved in the patient's care";
            case RELATEDPERSON: return "The participant is a person related to the patient";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PATIENT: return "Patient";
            case PRACTITIONER: return "Practitioner";
            case RELATEDPERSON: return "Related Person";
            default: return "?";
          }
        }
    }

  public static class ParticipantTypeEnumFactory implements EnumFactory<ParticipantType> {
    public ParticipantType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("patient".equals(codeString))
          return ParticipantType.PATIENT;
        if ("practitioner".equals(codeString))
          return ParticipantType.PRACTITIONER;
        if ("related-person".equals(codeString))
          return ParticipantType.RELATEDPERSON;
        throw new IllegalArgumentException("Unknown ParticipantType code '"+codeString+"'");
        }
        public Enumeration<ParticipantType> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("patient".equals(codeString))
          return new Enumeration<ParticipantType>(this, ParticipantType.PATIENT);
        if ("practitioner".equals(codeString))
          return new Enumeration<ParticipantType>(this, ParticipantType.PRACTITIONER);
        if ("related-person".equals(codeString))
          return new Enumeration<ParticipantType>(this, ParticipantType.RELATEDPERSON);
        throw new FHIRException("Unknown ParticipantType code '"+codeString+"'");
        }
    public String toCode(ParticipantType code) {
      if (code == ParticipantType.PATIENT)
        return "patient";
      if (code == ParticipantType.PRACTITIONER)
        return "practitioner";
      if (code == ParticipantType.RELATEDPERSON)
        return "related-person";
      return "?";
      }
    public String toSystem(ParticipantType code) {
      return code.getSystem();
      }
    }

    public enum ActionType {
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
         * added to help the parsers
         */
        NULL;
        public static ActionType fromCode(String codeString) throws FHIRException {
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
        throw new FHIRException("Unknown ActionType code '"+codeString+"'");
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
            case CREATE: return "http://hl7.org/fhir/action-type";
            case UPDATE: return "http://hl7.org/fhir/action-type";
            case REMOVE: return "http://hl7.org/fhir/action-type";
            case FIREEVENT: return "http://hl7.org/fhir/action-type";
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

  public static class ActionTypeEnumFactory implements EnumFactory<ActionType> {
    public ActionType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("create".equals(codeString))
          return ActionType.CREATE;
        if ("update".equals(codeString))
          return ActionType.UPDATE;
        if ("remove".equals(codeString))
          return ActionType.REMOVE;
        if ("fire-event".equals(codeString))
          return ActionType.FIREEVENT;
        throw new IllegalArgumentException("Unknown ActionType code '"+codeString+"'");
        }
        public Enumeration<ActionType> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("create".equals(codeString))
          return new Enumeration<ActionType>(this, ActionType.CREATE);
        if ("update".equals(codeString))
          return new Enumeration<ActionType>(this, ActionType.UPDATE);
        if ("remove".equals(codeString))
          return new Enumeration<ActionType>(this, ActionType.REMOVE);
        if ("fire-event".equals(codeString))
          return new Enumeration<ActionType>(this, ActionType.FIREEVENT);
        throw new FHIRException("Unknown ActionType code '"+codeString+"'");
        }
    public String toCode(ActionType code) {
      if (code == ActionType.CREATE)
        return "create";
      if (code == ActionType.UPDATE)
        return "update";
      if (code == ActionType.REMOVE)
        return "remove";
      if (code == ActionType.FIREEVENT)
        return "fire-event";
      return "?";
      }
    public String toSystem(ActionType code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class ActionDefinitionRelatedActionComponent extends Element implements IBaseDatatypeElement {
        /**
         * The unique identifier of the related action.
         */
        @Child(name = "actionIdentifier", type = {Identifier.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Identifier of the related action", formalDefinition="The unique identifier of the related action." )
        protected Identifier actionIdentifier;

        /**
         * The relationship of this action to the related action.
         */
        @Child(name = "relationship", type = {CodeType.class}, order=2, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="before | after", formalDefinition="The relationship of this action to the related action." )
        protected Enumeration<ActionRelationshipType> relationship;

        /**
         * A duration or range of durations to apply to the relationship. For example, 30-60 minutes before.
         */
        @Child(name = "offset", type = {Duration.class, Range.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Time offset for the relationship", formalDefinition="A duration or range of durations to apply to the relationship. For example, 30-60 minutes before." )
        protected Type offset;

        /**
         * An optional indicator for how the relationship is anchored to the related action. For example "before the start" or "before the end" of the related action.
         */
        @Child(name = "anchor", type = {CodeType.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="start | end", formalDefinition="An optional indicator for how the relationship is anchored to the related action. For example \"before the start\" or \"before the end\" of the related action." )
        protected Enumeration<ActionRelationshipAnchor> anchor;

        private static final long serialVersionUID = 451097227L;

    /**
     * Constructor
     */
      public ActionDefinitionRelatedActionComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ActionDefinitionRelatedActionComponent(Identifier actionIdentifier, Enumeration<ActionRelationshipType> relationship) {
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
              throw new Error("Attempt to auto-create ActionDefinitionRelatedActionComponent.actionIdentifier");
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
        public ActionDefinitionRelatedActionComponent setActionIdentifier(Identifier value) { 
          this.actionIdentifier = value;
          return this;
        }

        /**
         * @return {@link #relationship} (The relationship of this action to the related action.). This is the underlying object with id, value and extensions. The accessor "getRelationship" gives direct access to the value
         */
        public Enumeration<ActionRelationshipType> getRelationshipElement() { 
          if (this.relationship == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ActionDefinitionRelatedActionComponent.relationship");
            else if (Configuration.doAutoCreate())
              this.relationship = new Enumeration<ActionRelationshipType>(new ActionRelationshipTypeEnumFactory()); // bb
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
        public ActionDefinitionRelatedActionComponent setRelationshipElement(Enumeration<ActionRelationshipType> value) { 
          this.relationship = value;
          return this;
        }

        /**
         * @return The relationship of this action to the related action.
         */
        public ActionRelationshipType getRelationship() { 
          return this.relationship == null ? null : this.relationship.getValue();
        }

        /**
         * @param value The relationship of this action to the related action.
         */
        public ActionDefinitionRelatedActionComponent setRelationship(ActionRelationshipType value) { 
            if (this.relationship == null)
              this.relationship = new Enumeration<ActionRelationshipType>(new ActionRelationshipTypeEnumFactory());
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
        public ActionDefinitionRelatedActionComponent setOffset(Type value) { 
          this.offset = value;
          return this;
        }

        /**
         * @return {@link #anchor} (An optional indicator for how the relationship is anchored to the related action. For example "before the start" or "before the end" of the related action.). This is the underlying object with id, value and extensions. The accessor "getAnchor" gives direct access to the value
         */
        public Enumeration<ActionRelationshipAnchor> getAnchorElement() { 
          if (this.anchor == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ActionDefinitionRelatedActionComponent.anchor");
            else if (Configuration.doAutoCreate())
              this.anchor = new Enumeration<ActionRelationshipAnchor>(new ActionRelationshipAnchorEnumFactory()); // bb
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
        public ActionDefinitionRelatedActionComponent setAnchorElement(Enumeration<ActionRelationshipAnchor> value) { 
          this.anchor = value;
          return this;
        }

        /**
         * @return An optional indicator for how the relationship is anchored to the related action. For example "before the start" or "before the end" of the related action.
         */
        public ActionRelationshipAnchor getAnchor() { 
          return this.anchor == null ? null : this.anchor.getValue();
        }

        /**
         * @param value An optional indicator for how the relationship is anchored to the related action. For example "before the start" or "before the end" of the related action.
         */
        public ActionDefinitionRelatedActionComponent setAnchor(ActionRelationshipAnchor value) { 
          if (value == null)
            this.anchor = null;
          else {
            if (this.anchor == null)
              this.anchor = new Enumeration<ActionRelationshipAnchor>(new ActionRelationshipAnchorEnumFactory());
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
        case -261851592: /*relationship*/ return this.relationship == null ? new Base[0] : new Base[] {this.relationship}; // Enumeration<ActionRelationshipType>
        case -1019779949: /*offset*/ return this.offset == null ? new Base[0] : new Base[] {this.offset}; // Type
        case -1413299531: /*anchor*/ return this.anchor == null ? new Base[0] : new Base[] {this.anchor}; // Enumeration<ActionRelationshipAnchor>
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
          this.relationship = new ActionRelationshipTypeEnumFactory().fromType(value); // Enumeration<ActionRelationshipType>
          break;
        case -1019779949: // offset
          this.offset = (Type) value; // Type
          break;
        case -1413299531: // anchor
          this.anchor = new ActionRelationshipAnchorEnumFactory().fromType(value); // Enumeration<ActionRelationshipAnchor>
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("actionIdentifier"))
          this.actionIdentifier = castToIdentifier(value); // Identifier
        else if (name.equals("relationship"))
          this.relationship = new ActionRelationshipTypeEnumFactory().fromType(value); // Enumeration<ActionRelationshipType>
        else if (name.equals("offset[x]"))
          this.offset = (Type) value; // Type
        else if (name.equals("anchor"))
          this.anchor = new ActionRelationshipAnchorEnumFactory().fromType(value); // Enumeration<ActionRelationshipAnchor>
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -889046145:  return getActionIdentifier(); // Identifier
        case -261851592: throw new FHIRException("Cannot make property relationship as it is not a complex type"); // Enumeration<ActionRelationshipType>
        case -1960684787:  return getOffset(); // Type
        case -1413299531: throw new FHIRException("Cannot make property anchor as it is not a complex type"); // Enumeration<ActionRelationshipAnchor>
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
          throw new FHIRException("Cannot call addChild on a primitive type ActionDefinition.relationship");
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
          throw new FHIRException("Cannot call addChild on a primitive type ActionDefinition.anchor");
        }
        else
          return super.addChild(name);
      }

      public ActionDefinitionRelatedActionComponent copy() {
        ActionDefinitionRelatedActionComponent dst = new ActionDefinitionRelatedActionComponent();
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
        if (!(other instanceof ActionDefinitionRelatedActionComponent))
          return false;
        ActionDefinitionRelatedActionComponent o = (ActionDefinitionRelatedActionComponent) other;
        return compareDeep(actionIdentifier, o.actionIdentifier, true) && compareDeep(relationship, o.relationship, true)
           && compareDeep(offset, o.offset, true) && compareDeep(anchor, o.anchor, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ActionDefinitionRelatedActionComponent))
          return false;
        ActionDefinitionRelatedActionComponent o = (ActionDefinitionRelatedActionComponent) other;
        return compareValues(relationship, o.relationship, true) && compareValues(anchor, o.anchor, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (actionIdentifier == null || actionIdentifier.isEmpty()) && (relationship == null || relationship.isEmpty())
           && (offset == null || offset.isEmpty()) && (anchor == null || anchor.isEmpty());
      }

  public String fhirType() {
    return "ActionDefinition.relatedAction";

  }

  }

    @Block()
    public static class ActionDefinitionBehaviorComponent extends Element implements IBaseDatatypeElement {
        /**
         * The type of the behavior to be described, such as grouping, visual, or selection behaviors.
         */
        @Child(name = "type", type = {Coding.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The type of behavior (grouping, precheck, selection, cardinality, etc)", formalDefinition="The type of the behavior to be described, such as grouping, visual, or selection behaviors." )
        protected Coding type;

        /**
         * The specific behavior. The code used here is determined by the type of behavior being described. For example, the grouping behavior uses the grouping-behavior-type valueset.
         */
        @Child(name = "value", type = {Coding.class}, order=2, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Specific behavior (e.g. required, at-most-one, single, etc)", formalDefinition="The specific behavior. The code used here is determined by the type of behavior being described. For example, the grouping behavior uses the grouping-behavior-type valueset." )
        protected Coding value;

        private static final long serialVersionUID = -1054119695L;

    /**
     * Constructor
     */
      public ActionDefinitionBehaviorComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ActionDefinitionBehaviorComponent(Coding type, Coding value) {
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
              throw new Error("Attempt to auto-create ActionDefinitionBehaviorComponent.type");
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
        public ActionDefinitionBehaviorComponent setType(Coding value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #value} (The specific behavior. The code used here is determined by the type of behavior being described. For example, the grouping behavior uses the grouping-behavior-type valueset.)
         */
        public Coding getValue() { 
          if (this.value == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ActionDefinitionBehaviorComponent.value");
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
        public ActionDefinitionBehaviorComponent setValue(Coding value) { 
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

      public ActionDefinitionBehaviorComponent copy() {
        ActionDefinitionBehaviorComponent dst = new ActionDefinitionBehaviorComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.value = value == null ? null : value.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ActionDefinitionBehaviorComponent))
          return false;
        ActionDefinitionBehaviorComponent o = (ActionDefinitionBehaviorComponent) other;
        return compareDeep(type, o.type, true) && compareDeep(value, o.value, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ActionDefinitionBehaviorComponent))
          return false;
        ActionDefinitionBehaviorComponent o = (ActionDefinitionBehaviorComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (type == null || type.isEmpty()) && (value == null || value.isEmpty())
          ;
      }

  public String fhirType() {
    return "ActionDefinition.behavior";

  }

  }

    @Block()
    public static class ActionDefinitionCustomizationComponent extends Element implements IBaseDatatypeElement {
        /**
         * The path to the element to be customized. This is the path on the resource that will hold the result of the calculation defined by the expression.
         */
        @Child(name = "path", type = {StringType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The path to the element to be set dynamically", formalDefinition="The path to the element to be customized. This is the path on the resource that will hold the result of the calculation defined by the expression." )
        protected StringType path;

        /**
         * An expression specifying the value of the customized element.
         */
        @Child(name = "expression", type = {StringType.class}, order=2, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="An expression that provides the dynamic value for the customization", formalDefinition="An expression specifying the value of the customized element." )
        protected StringType expression;

        private static final long serialVersionUID = -252690483L;

    /**
     * Constructor
     */
      public ActionDefinitionCustomizationComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ActionDefinitionCustomizationComponent(StringType path, StringType expression) {
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
              throw new Error("Attempt to auto-create ActionDefinitionCustomizationComponent.path");
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
        public ActionDefinitionCustomizationComponent setPathElement(StringType value) { 
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
        public ActionDefinitionCustomizationComponent setPath(String value) { 
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
              throw new Error("Attempt to auto-create ActionDefinitionCustomizationComponent.expression");
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
        public ActionDefinitionCustomizationComponent setExpressionElement(StringType value) { 
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
        public ActionDefinitionCustomizationComponent setExpression(String value) { 
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
          throw new FHIRException("Cannot call addChild on a primitive type ActionDefinition.path");
        }
        else if (name.equals("expression")) {
          throw new FHIRException("Cannot call addChild on a primitive type ActionDefinition.expression");
        }
        else
          return super.addChild(name);
      }

      public ActionDefinitionCustomizationComponent copy() {
        ActionDefinitionCustomizationComponent dst = new ActionDefinitionCustomizationComponent();
        copyValues(dst);
        dst.path = path == null ? null : path.copy();
        dst.expression = expression == null ? null : expression.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ActionDefinitionCustomizationComponent))
          return false;
        ActionDefinitionCustomizationComponent o = (ActionDefinitionCustomizationComponent) other;
        return compareDeep(path, o.path, true) && compareDeep(expression, o.expression, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ActionDefinitionCustomizationComponent))
          return false;
        ActionDefinitionCustomizationComponent o = (ActionDefinitionCustomizationComponent) other;
        return compareValues(path, o.path, true) && compareValues(expression, o.expression, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (path == null || path.isEmpty()) && (expression == null || expression.isEmpty())
          ;
      }

  public String fhirType() {
    return "ActionDefinition.customization";

  }

  }

    /**
     * A unique identifier for the action. The identifier SHALL be unique within the container in which it appears, and MAY be universally unique.
     */
    @Child(name = "actionIdentifier", type = {Identifier.class}, order=0, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Unique identifier", formalDefinition="A unique identifier for the action. The identifier SHALL be unique within the container in which it appears, and MAY be universally unique." )
    protected Identifier actionIdentifier;

    /**
     * A user-visible label for the action.
     */
    @Child(name = "label", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="User-visible label for the action (e.g. 1. or A.)", formalDefinition="A user-visible label for the action." )
    protected StringType label;

    /**
     * The title of the action displayed to a user.
     */
    @Child(name = "title", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="User-visible title", formalDefinition="The title of the action displayed to a user." )
    protected StringType title;

    /**
     * A short description of the action used to provide a summary to display to the user.
     */
    @Child(name = "description", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Short description of the action", formalDefinition="A short description of the action used to provide a summary to display to the user." )
    protected StringType description;

    /**
     * A text equivalent of the action to be performed. This provides a human-interpretable description of the action when the definition is consumed by a system that may not be capable of interpreting it dynamically.
     */
    @Child(name = "textEquivalent", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Static text equivalent of the action, used if the dynamic aspects cannot be interpreted by the receiving system", formalDefinition="A text equivalent of the action to be performed. This provides a human-interpretable description of the action when the definition is consumed by a system that may not be capable of interpreting it dynamically." )
    protected StringType textEquivalent;

    /**
     * The concept represented by this action or its sub-actions.
     */
    @Child(name = "concept", type = {CodeableConcept.class}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="The meaning of the action or its sub-actions", formalDefinition="The concept represented by this action or its sub-actions." )
    protected List<CodeableConcept> concept;

    /**
     * The evidence grade and the sources of evidence for this action.
     */
    @Child(name = "supportingEvidence", type = {Attachment.class}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Evidence that supports taking the action", formalDefinition="The evidence grade and the sources of evidence for this action." )
    protected List<Attachment> supportingEvidence;

    /**
     * Didactic or other informational resources associated with the action that can be provided to the CDS recipient. Information resources can include inline text commentary and links to web resources.
     */
    @Child(name = "documentation", type = {Attachment.class}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Supporting documentation for the intended performer of the action", formalDefinition="Didactic or other informational resources associated with the action that can be provided to the CDS recipient. Information resources can include inline text commentary and links to web resources." )
    protected List<Attachment> documentation;

    /**
     * A relationship to another action such as "before" or "30-60 minutes after start of".
     */
    @Child(name = "relatedAction", type = {}, order=8, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Relationship to another action", formalDefinition="A relationship to another action such as \"before\" or \"30-60 minutes after start of\"." )
    protected ActionDefinitionRelatedActionComponent relatedAction;

    /**
     * The type of participant in the action.
     */
    @Child(name = "participantType", type = {CodeType.class}, order=9, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="patient | practitioner | related-person", formalDefinition="The type of participant in the action." )
    protected List<Enumeration<ParticipantType>> participantType;

    /**
     * The type of action to perform (create, update, remove).
     */
    @Child(name = "type", type = {CodeType.class}, order=10, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="create | update | remove | fire-event", formalDefinition="The type of action to perform (create, update, remove)." )
    protected Enumeration<ActionType> type;

    /**
     * A behavior associated with the action. Behaviors define how the action is to be presented and/or executed within the receiving environment.
     */
    @Child(name = "behavior", type = {}, order=11, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Defines behaviors such as selection and grouping", formalDefinition="A behavior associated with the action. Behaviors define how the action is to be presented and/or executed within the receiving environment." )
    protected List<ActionDefinitionBehaviorComponent> behavior;

    /**
     * The resource that is the target of the action (e.g. CommunicationRequest). The resource described here defines any aspects of the action that can be specified statically (i.e. are known at the time of definition).
     */
    @Child(name = "resource", type = {}, order=12, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Static portion of the action definition", formalDefinition="The resource that is the target of the action (e.g. CommunicationRequest). The resource described here defines any aspects of the action that can be specified statically (i.e. are known at the time of definition)." )
    protected Reference resource;

    /**
     * The actual object that is the target of the reference (The resource that is the target of the action (e.g. CommunicationRequest). The resource described here defines any aspects of the action that can be specified statically (i.e. are known at the time of definition).)
     */
    protected Resource resourceTarget;

    /**
     * Customizations that should be applied to the statically defined resource. For example, if the dosage of a medication must be computed based on the patient's weight, a customization would be used to specify an expression that calculated the weight, and the path on the resource that would contain the result.
     */
    @Child(name = "customization", type = {}, order=13, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Dynamic aspects of the definition", formalDefinition="Customizations that should be applied to the statically defined resource. For example, if the dosage of a medication must be computed based on the patient's weight, a customization would be used to specify an expression that calculated the weight, and the path on the resource that would contain the result." )
    protected List<ActionDefinitionCustomizationComponent> customization;

    /**
     * Sub actions that are contained within the action. The behavior of this action determines the functionality of the sub-actions. For example, a selection behavior of at-most-one indicates that of the sub-actions, at most one may be chosen as part of realizing the action definition.
     */
    @Child(name = "action", type = {ActionDefinition.class}, order=14, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="A sub-action", formalDefinition="Sub actions that are contained within the action. The behavior of this action determines the functionality of the sub-actions. For example, a selection behavior of at-most-one indicates that of the sub-actions, at most one may be chosen as part of realizing the action definition." )
    protected List<ActionDefinition> action;

    private static final long serialVersionUID = -1659761573L;

  /**
   * Constructor
   */
    public ActionDefinition() {
      super();
    }

    /**
     * @return {@link #actionIdentifier} (A unique identifier for the action. The identifier SHALL be unique within the container in which it appears, and MAY be universally unique.)
     */
    public Identifier getActionIdentifier() { 
      if (this.actionIdentifier == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ActionDefinition.actionIdentifier");
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
    public ActionDefinition setActionIdentifier(Identifier value) { 
      this.actionIdentifier = value;
      return this;
    }

    /**
     * @return {@link #label} (A user-visible label for the action.). This is the underlying object with id, value and extensions. The accessor "getLabel" gives direct access to the value
     */
    public StringType getLabelElement() { 
      if (this.label == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ActionDefinition.label");
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
    public ActionDefinition setLabelElement(StringType value) { 
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
    public ActionDefinition setLabel(String value) { 
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
          throw new Error("Attempt to auto-create ActionDefinition.title");
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
    public ActionDefinition setTitleElement(StringType value) { 
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
    public ActionDefinition setTitle(String value) { 
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
          throw new Error("Attempt to auto-create ActionDefinition.description");
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
    public ActionDefinition setDescriptionElement(StringType value) { 
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
    public ActionDefinition setDescription(String value) { 
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
          throw new Error("Attempt to auto-create ActionDefinition.textEquivalent");
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
    public ActionDefinition setTextEquivalentElement(StringType value) { 
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
    public ActionDefinition setTextEquivalent(String value) { 
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
    public ActionDefinition addConcept(CodeableConcept t) { //3
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
    public ActionDefinition addSupportingEvidence(Attachment t) { //3
      if (t == null)
        return this;
      if (this.supportingEvidence == null)
        this.supportingEvidence = new ArrayList<Attachment>();
      this.supportingEvidence.add(t);
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
    public ActionDefinition addDocumentation(Attachment t) { //3
      if (t == null)
        return this;
      if (this.documentation == null)
        this.documentation = new ArrayList<Attachment>();
      this.documentation.add(t);
      return this;
    }

    /**
     * @return {@link #relatedAction} (A relationship to another action such as "before" or "30-60 minutes after start of".)
     */
    public ActionDefinitionRelatedActionComponent getRelatedAction() { 
      if (this.relatedAction == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ActionDefinition.relatedAction");
        else if (Configuration.doAutoCreate())
          this.relatedAction = new ActionDefinitionRelatedActionComponent(); // cc
      return this.relatedAction;
    }

    public boolean hasRelatedAction() { 
      return this.relatedAction != null && !this.relatedAction.isEmpty();
    }

    /**
     * @param value {@link #relatedAction} (A relationship to another action such as "before" or "30-60 minutes after start of".)
     */
    public ActionDefinition setRelatedAction(ActionDefinitionRelatedActionComponent value) { 
      this.relatedAction = value;
      return this;
    }

    /**
     * @return {@link #participantType} (The type of participant in the action.)
     */
    public List<Enumeration<ParticipantType>> getParticipantType() { 
      if (this.participantType == null)
        this.participantType = new ArrayList<Enumeration<ParticipantType>>();
      return this.participantType;
    }

    public boolean hasParticipantType() { 
      if (this.participantType == null)
        return false;
      for (Enumeration<ParticipantType> item : this.participantType)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #participantType} (The type of participant in the action.)
     */
    // syntactic sugar
    public Enumeration<ParticipantType> addParticipantTypeElement() {//2 
      Enumeration<ParticipantType> t = new Enumeration<ParticipantType>(new ParticipantTypeEnumFactory());
      if (this.participantType == null)
        this.participantType = new ArrayList<Enumeration<ParticipantType>>();
      this.participantType.add(t);
      return t;
    }

    /**
     * @param value {@link #participantType} (The type of participant in the action.)
     */
    public ActionDefinition addParticipantType(ParticipantType value) { //1
      Enumeration<ParticipantType> t = new Enumeration<ParticipantType>(new ParticipantTypeEnumFactory());
      t.setValue(value);
      if (this.participantType == null)
        this.participantType = new ArrayList<Enumeration<ParticipantType>>();
      this.participantType.add(t);
      return this;
    }

    /**
     * @param value {@link #participantType} (The type of participant in the action.)
     */
    public boolean hasParticipantType(ParticipantType value) { 
      if (this.participantType == null)
        return false;
      for (Enumeration<ParticipantType> v : this.participantType)
        if (v.getValue().equals(value)) // code
          return true;
      return false;
    }

    /**
     * @return {@link #type} (The type of action to perform (create, update, remove).). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
     */
    public Enumeration<ActionType> getTypeElement() { 
      if (this.type == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ActionDefinition.type");
        else if (Configuration.doAutoCreate())
          this.type = new Enumeration<ActionType>(new ActionTypeEnumFactory()); // bb
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
    public ActionDefinition setTypeElement(Enumeration<ActionType> value) { 
      this.type = value;
      return this;
    }

    /**
     * @return The type of action to perform (create, update, remove).
     */
    public ActionType getType() { 
      return this.type == null ? null : this.type.getValue();
    }

    /**
     * @param value The type of action to perform (create, update, remove).
     */
    public ActionDefinition setType(ActionType value) { 
      if (value == null)
        this.type = null;
      else {
        if (this.type == null)
          this.type = new Enumeration<ActionType>(new ActionTypeEnumFactory());
        this.type.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #behavior} (A behavior associated with the action. Behaviors define how the action is to be presented and/or executed within the receiving environment.)
     */
    public List<ActionDefinitionBehaviorComponent> getBehavior() { 
      if (this.behavior == null)
        this.behavior = new ArrayList<ActionDefinitionBehaviorComponent>();
      return this.behavior;
    }

    public boolean hasBehavior() { 
      if (this.behavior == null)
        return false;
      for (ActionDefinitionBehaviorComponent item : this.behavior)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #behavior} (A behavior associated with the action. Behaviors define how the action is to be presented and/or executed within the receiving environment.)
     */
    // syntactic sugar
    public ActionDefinitionBehaviorComponent addBehavior() { //3
      ActionDefinitionBehaviorComponent t = new ActionDefinitionBehaviorComponent();
      if (this.behavior == null)
        this.behavior = new ArrayList<ActionDefinitionBehaviorComponent>();
      this.behavior.add(t);
      return t;
    }

    // syntactic sugar
    public ActionDefinition addBehavior(ActionDefinitionBehaviorComponent t) { //3
      if (t == null)
        return this;
      if (this.behavior == null)
        this.behavior = new ArrayList<ActionDefinitionBehaviorComponent>();
      this.behavior.add(t);
      return this;
    }

    /**
     * @return {@link #resource} (The resource that is the target of the action (e.g. CommunicationRequest). The resource described here defines any aspects of the action that can be specified statically (i.e. are known at the time of definition).)
     */
    public Reference getResource() { 
      if (this.resource == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ActionDefinition.resource");
        else if (Configuration.doAutoCreate())
          this.resource = new Reference(); // cc
      return this.resource;
    }

    public boolean hasResource() { 
      return this.resource != null && !this.resource.isEmpty();
    }

    /**
     * @param value {@link #resource} (The resource that is the target of the action (e.g. CommunicationRequest). The resource described here defines any aspects of the action that can be specified statically (i.e. are known at the time of definition).)
     */
    public ActionDefinition setResource(Reference value) { 
      this.resource = value;
      return this;
    }

    /**
     * @return {@link #resource} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The resource that is the target of the action (e.g. CommunicationRequest). The resource described here defines any aspects of the action that can be specified statically (i.e. are known at the time of definition).)
     */
    public Resource getResourceTarget() { 
      return this.resourceTarget;
    }

    /**
     * @param value {@link #resource} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The resource that is the target of the action (e.g. CommunicationRequest). The resource described here defines any aspects of the action that can be specified statically (i.e. are known at the time of definition).)
     */
    public ActionDefinition setResourceTarget(Resource value) { 
      this.resourceTarget = value;
      return this;
    }

    /**
     * @return {@link #customization} (Customizations that should be applied to the statically defined resource. For example, if the dosage of a medication must be computed based on the patient's weight, a customization would be used to specify an expression that calculated the weight, and the path on the resource that would contain the result.)
     */
    public List<ActionDefinitionCustomizationComponent> getCustomization() { 
      if (this.customization == null)
        this.customization = new ArrayList<ActionDefinitionCustomizationComponent>();
      return this.customization;
    }

    public boolean hasCustomization() { 
      if (this.customization == null)
        return false;
      for (ActionDefinitionCustomizationComponent item : this.customization)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #customization} (Customizations that should be applied to the statically defined resource. For example, if the dosage of a medication must be computed based on the patient's weight, a customization would be used to specify an expression that calculated the weight, and the path on the resource that would contain the result.)
     */
    // syntactic sugar
    public ActionDefinitionCustomizationComponent addCustomization() { //3
      ActionDefinitionCustomizationComponent t = new ActionDefinitionCustomizationComponent();
      if (this.customization == null)
        this.customization = new ArrayList<ActionDefinitionCustomizationComponent>();
      this.customization.add(t);
      return t;
    }

    // syntactic sugar
    public ActionDefinition addCustomization(ActionDefinitionCustomizationComponent t) { //3
      if (t == null)
        return this;
      if (this.customization == null)
        this.customization = new ArrayList<ActionDefinitionCustomizationComponent>();
      this.customization.add(t);
      return this;
    }

    /**
     * @return {@link #action} (Sub actions that are contained within the action. The behavior of this action determines the functionality of the sub-actions. For example, a selection behavior of at-most-one indicates that of the sub-actions, at most one may be chosen as part of realizing the action definition.)
     */
    public List<ActionDefinition> getAction() { 
      if (this.action == null)
        this.action = new ArrayList<ActionDefinition>();
      return this.action;
    }

    public boolean hasAction() { 
      if (this.action == null)
        return false;
      for (ActionDefinition item : this.action)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #action} (Sub actions that are contained within the action. The behavior of this action determines the functionality of the sub-actions. For example, a selection behavior of at-most-one indicates that of the sub-actions, at most one may be chosen as part of realizing the action definition.)
     */
    // syntactic sugar
    public ActionDefinition addAction() { //3
      ActionDefinition t = new ActionDefinition();
      if (this.action == null)
        this.action = new ArrayList<ActionDefinition>();
      this.action.add(t);
      return t;
    }

    // syntactic sugar
    public ActionDefinition addAction(ActionDefinition t) { //3
      if (t == null)
        return this;
      if (this.action == null)
        this.action = new ArrayList<ActionDefinition>();
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
        childrenList.add(new Property("documentation", "Attachment", "Didactic or other informational resources associated with the action that can be provided to the CDS recipient. Information resources can include inline text commentary and links to web resources.", 0, java.lang.Integer.MAX_VALUE, documentation));
        childrenList.add(new Property("relatedAction", "", "A relationship to another action such as \"before\" or \"30-60 minutes after start of\".", 0, java.lang.Integer.MAX_VALUE, relatedAction));
        childrenList.add(new Property("participantType", "code", "The type of participant in the action.", 0, java.lang.Integer.MAX_VALUE, participantType));
        childrenList.add(new Property("type", "code", "The type of action to perform (create, update, remove).", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("behavior", "", "A behavior associated with the action. Behaviors define how the action is to be presented and/or executed within the receiving environment.", 0, java.lang.Integer.MAX_VALUE, behavior));
        childrenList.add(new Property("resource", "Reference(Any)", "The resource that is the target of the action (e.g. CommunicationRequest). The resource described here defines any aspects of the action that can be specified statically (i.e. are known at the time of definition).", 0, java.lang.Integer.MAX_VALUE, resource));
        childrenList.add(new Property("customization", "", "Customizations that should be applied to the statically defined resource. For example, if the dosage of a medication must be computed based on the patient's weight, a customization would be used to specify an expression that calculated the weight, and the path on the resource that would contain the result.", 0, java.lang.Integer.MAX_VALUE, customization));
        childrenList.add(new Property("action", "ActionDefinition", "Sub actions that are contained within the action. The behavior of this action determines the functionality of the sub-actions. For example, a selection behavior of at-most-one indicates that of the sub-actions, at most one may be chosen as part of realizing the action definition.", 0, java.lang.Integer.MAX_VALUE, action));
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
        case -384107967: /*relatedAction*/ return this.relatedAction == null ? new Base[0] : new Base[] {this.relatedAction}; // ActionDefinitionRelatedActionComponent
        case 841294093: /*participantType*/ return this.participantType == null ? new Base[0] : this.participantType.toArray(new Base[this.participantType.size()]); // Enumeration<ParticipantType>
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // Enumeration<ActionType>
        case 1510912594: /*behavior*/ return this.behavior == null ? new Base[0] : this.behavior.toArray(new Base[this.behavior.size()]); // ActionDefinitionBehaviorComponent
        case -341064690: /*resource*/ return this.resource == null ? new Base[0] : new Base[] {this.resource}; // Reference
        case 1637263315: /*customization*/ return this.customization == null ? new Base[0] : this.customization.toArray(new Base[this.customization.size()]); // ActionDefinitionCustomizationComponent
        case -1422950858: /*action*/ return this.action == null ? new Base[0] : this.action.toArray(new Base[this.action.size()]); // ActionDefinition
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
        case -384107967: // relatedAction
          this.relatedAction = (ActionDefinitionRelatedActionComponent) value; // ActionDefinitionRelatedActionComponent
          break;
        case 841294093: // participantType
          this.getParticipantType().add(new ParticipantTypeEnumFactory().fromType(value)); // Enumeration<ParticipantType>
          break;
        case 3575610: // type
          this.type = new ActionTypeEnumFactory().fromType(value); // Enumeration<ActionType>
          break;
        case 1510912594: // behavior
          this.getBehavior().add((ActionDefinitionBehaviorComponent) value); // ActionDefinitionBehaviorComponent
          break;
        case -341064690: // resource
          this.resource = castToReference(value); // Reference
          break;
        case 1637263315: // customization
          this.getCustomization().add((ActionDefinitionCustomizationComponent) value); // ActionDefinitionCustomizationComponent
          break;
        case -1422950858: // action
          this.getAction().add(castToActionDefinition(value)); // ActionDefinition
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
        else if (name.equals("relatedAction"))
          this.relatedAction = (ActionDefinitionRelatedActionComponent) value; // ActionDefinitionRelatedActionComponent
        else if (name.equals("participantType"))
          this.getParticipantType().add(new ParticipantTypeEnumFactory().fromType(value));
        else if (name.equals("type"))
          this.type = new ActionTypeEnumFactory().fromType(value); // Enumeration<ActionType>
        else if (name.equals("behavior"))
          this.getBehavior().add((ActionDefinitionBehaviorComponent) value);
        else if (name.equals("resource"))
          this.resource = castToReference(value); // Reference
        else if (name.equals("customization"))
          this.getCustomization().add((ActionDefinitionCustomizationComponent) value);
        else if (name.equals("action"))
          this.getAction().add(castToActionDefinition(value));
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
        case -384107967:  return getRelatedAction(); // ActionDefinitionRelatedActionComponent
        case 841294093: throw new FHIRException("Cannot make property participantType as it is not a complex type"); // Enumeration<ParticipantType>
        case 3575610: throw new FHIRException("Cannot make property type as it is not a complex type"); // Enumeration<ActionType>
        case 1510912594:  return addBehavior(); // ActionDefinitionBehaviorComponent
        case -341064690:  return getResource(); // Reference
        case 1637263315:  return addCustomization(); // ActionDefinitionCustomizationComponent
        case -1422950858:  return addAction(); // ActionDefinition
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
          throw new FHIRException("Cannot call addChild on a primitive type ActionDefinition.label");
        }
        else if (name.equals("title")) {
          throw new FHIRException("Cannot call addChild on a primitive type ActionDefinition.title");
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type ActionDefinition.description");
        }
        else if (name.equals("textEquivalent")) {
          throw new FHIRException("Cannot call addChild on a primitive type ActionDefinition.textEquivalent");
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
        else if (name.equals("relatedAction")) {
          this.relatedAction = new ActionDefinitionRelatedActionComponent();
          return this.relatedAction;
        }
        else if (name.equals("participantType")) {
          throw new FHIRException("Cannot call addChild on a primitive type ActionDefinition.participantType");
        }
        else if (name.equals("type")) {
          throw new FHIRException("Cannot call addChild on a primitive type ActionDefinition.type");
        }
        else if (name.equals("behavior")) {
          return addBehavior();
        }
        else if (name.equals("resource")) {
          this.resource = new Reference();
          return this.resource;
        }
        else if (name.equals("customization")) {
          return addCustomization();
        }
        else if (name.equals("action")) {
          return addAction();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "ActionDefinition";

  }

      public ActionDefinition copy() {
        ActionDefinition dst = new ActionDefinition();
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
        dst.relatedAction = relatedAction == null ? null : relatedAction.copy();
        if (participantType != null) {
          dst.participantType = new ArrayList<Enumeration<ParticipantType>>();
          for (Enumeration<ParticipantType> i : participantType)
            dst.participantType.add(i.copy());
        };
        dst.type = type == null ? null : type.copy();
        if (behavior != null) {
          dst.behavior = new ArrayList<ActionDefinitionBehaviorComponent>();
          for (ActionDefinitionBehaviorComponent i : behavior)
            dst.behavior.add(i.copy());
        };
        dst.resource = resource == null ? null : resource.copy();
        if (customization != null) {
          dst.customization = new ArrayList<ActionDefinitionCustomizationComponent>();
          for (ActionDefinitionCustomizationComponent i : customization)
            dst.customization.add(i.copy());
        };
        if (action != null) {
          dst.action = new ArrayList<ActionDefinition>();
          for (ActionDefinition i : action)
            dst.action.add(i.copy());
        };
        return dst;
      }

      protected ActionDefinition typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ActionDefinition))
          return false;
        ActionDefinition o = (ActionDefinition) other;
        return compareDeep(actionIdentifier, o.actionIdentifier, true) && compareDeep(label, o.label, true)
           && compareDeep(title, o.title, true) && compareDeep(description, o.description, true) && compareDeep(textEquivalent, o.textEquivalent, true)
           && compareDeep(concept, o.concept, true) && compareDeep(supportingEvidence, o.supportingEvidence, true)
           && compareDeep(documentation, o.documentation, true) && compareDeep(relatedAction, o.relatedAction, true)
           && compareDeep(participantType, o.participantType, true) && compareDeep(type, o.type, true) && compareDeep(behavior, o.behavior, true)
           && compareDeep(resource, o.resource, true) && compareDeep(customization, o.customization, true)
           && compareDeep(action, o.action, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ActionDefinition))
          return false;
        ActionDefinition o = (ActionDefinition) other;
        return compareValues(label, o.label, true) && compareValues(title, o.title, true) && compareValues(description, o.description, true)
           && compareValues(textEquivalent, o.textEquivalent, true) && compareValues(participantType, o.participantType, true)
           && compareValues(type, o.type, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (actionIdentifier == null || actionIdentifier.isEmpty()) && (label == null || label.isEmpty())
           && (title == null || title.isEmpty()) && (description == null || description.isEmpty()) && (textEquivalent == null || textEquivalent.isEmpty())
           && (concept == null || concept.isEmpty()) && (supportingEvidence == null || supportingEvidence.isEmpty())
           && (documentation == null || documentation.isEmpty()) && (relatedAction == null || relatedAction.isEmpty())
           && (participantType == null || participantType.isEmpty()) && (type == null || type.isEmpty())
           && (behavior == null || behavior.isEmpty()) && (resource == null || resource.isEmpty()) && (customization == null || customization.isEmpty())
           && (action == null || action.isEmpty());
      }


}

