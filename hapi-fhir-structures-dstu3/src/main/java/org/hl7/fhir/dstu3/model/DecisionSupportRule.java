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
import org.hl7.fhir.instance.model.api.*;
/**
 * This resource defines a decision support rule of the form [on Event] if Condition then Action.
 */
@ResourceDef(name="DecisionSupportRule", profile="http://hl7.org/fhir/Profile/DecisionSupportRule")
public class DecisionSupportRule extends DomainResource {

    public enum DecisionSupportRuleTriggerType {
        /**
         * The trigger occurs in response to a specific named event
         */
        NAMEDEVENT, 
        /**
         * The trigger occurs at a specific time or periodically as described by a timing or schedule
         */
        PERIODIC, 
        /**
         * The trigger occurs whenever data of a particular type is added
         */
        DATAADDED, 
        /**
         * The trigger occurs whenever data of a particular type is modified
         */
        DATAMODIFIED, 
        /**
         * The trigger occurs whenever data of a particular type is removed
         */
        DATAREMOVED, 
        /**
         * The trigger occurs whenever data of a particular type is accessed
         */
        DATAACCESSED, 
        /**
         * The trigger occurs whenever access to data of a particular type is completed
         */
        DATAACCESSENDED, 
        /**
         * added to help the parsers
         */
        NULL;
        public static DecisionSupportRuleTriggerType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("named-event".equals(codeString))
          return NAMEDEVENT;
        if ("periodic".equals(codeString))
          return PERIODIC;
        if ("data-added".equals(codeString))
          return DATAADDED;
        if ("data-modified".equals(codeString))
          return DATAMODIFIED;
        if ("data-removed".equals(codeString))
          return DATAREMOVED;
        if ("data-accessed".equals(codeString))
          return DATAACCESSED;
        if ("data-access-ended".equals(codeString))
          return DATAACCESSENDED;
        throw new FHIRException("Unknown DecisionSupportRuleTriggerType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case NAMEDEVENT: return "named-event";
            case PERIODIC: return "periodic";
            case DATAADDED: return "data-added";
            case DATAMODIFIED: return "data-modified";
            case DATAREMOVED: return "data-removed";
            case DATAACCESSED: return "data-accessed";
            case DATAACCESSENDED: return "data-access-ended";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case NAMEDEVENT: return "http://hl7.org/fhir/cds-rule-trigger-type";
            case PERIODIC: return "http://hl7.org/fhir/cds-rule-trigger-type";
            case DATAADDED: return "http://hl7.org/fhir/cds-rule-trigger-type";
            case DATAMODIFIED: return "http://hl7.org/fhir/cds-rule-trigger-type";
            case DATAREMOVED: return "http://hl7.org/fhir/cds-rule-trigger-type";
            case DATAACCESSED: return "http://hl7.org/fhir/cds-rule-trigger-type";
            case DATAACCESSENDED: return "http://hl7.org/fhir/cds-rule-trigger-type";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case NAMEDEVENT: return "The trigger occurs in response to a specific named event";
            case PERIODIC: return "The trigger occurs at a specific time or periodically as described by a timing or schedule";
            case DATAADDED: return "The trigger occurs whenever data of a particular type is added";
            case DATAMODIFIED: return "The trigger occurs whenever data of a particular type is modified";
            case DATAREMOVED: return "The trigger occurs whenever data of a particular type is removed";
            case DATAACCESSED: return "The trigger occurs whenever data of a particular type is accessed";
            case DATAACCESSENDED: return "The trigger occurs whenever access to data of a particular type is completed";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case NAMEDEVENT: return "Named Event";
            case PERIODIC: return "Periodic";
            case DATAADDED: return "Data Added";
            case DATAMODIFIED: return "Data Modified";
            case DATAREMOVED: return "Data Removed";
            case DATAACCESSED: return "Data Accessed";
            case DATAACCESSENDED: return "Data Access Ended";
            default: return "?";
          }
        }
    }

  public static class DecisionSupportRuleTriggerTypeEnumFactory implements EnumFactory<DecisionSupportRuleTriggerType> {
    public DecisionSupportRuleTriggerType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("named-event".equals(codeString))
          return DecisionSupportRuleTriggerType.NAMEDEVENT;
        if ("periodic".equals(codeString))
          return DecisionSupportRuleTriggerType.PERIODIC;
        if ("data-added".equals(codeString))
          return DecisionSupportRuleTriggerType.DATAADDED;
        if ("data-modified".equals(codeString))
          return DecisionSupportRuleTriggerType.DATAMODIFIED;
        if ("data-removed".equals(codeString))
          return DecisionSupportRuleTriggerType.DATAREMOVED;
        if ("data-accessed".equals(codeString))
          return DecisionSupportRuleTriggerType.DATAACCESSED;
        if ("data-access-ended".equals(codeString))
          return DecisionSupportRuleTriggerType.DATAACCESSENDED;
        throw new IllegalArgumentException("Unknown DecisionSupportRuleTriggerType code '"+codeString+"'");
        }
        public Enumeration<DecisionSupportRuleTriggerType> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("named-event".equals(codeString))
          return new Enumeration<DecisionSupportRuleTriggerType>(this, DecisionSupportRuleTriggerType.NAMEDEVENT);
        if ("periodic".equals(codeString))
          return new Enumeration<DecisionSupportRuleTriggerType>(this, DecisionSupportRuleTriggerType.PERIODIC);
        if ("data-added".equals(codeString))
          return new Enumeration<DecisionSupportRuleTriggerType>(this, DecisionSupportRuleTriggerType.DATAADDED);
        if ("data-modified".equals(codeString))
          return new Enumeration<DecisionSupportRuleTriggerType>(this, DecisionSupportRuleTriggerType.DATAMODIFIED);
        if ("data-removed".equals(codeString))
          return new Enumeration<DecisionSupportRuleTriggerType>(this, DecisionSupportRuleTriggerType.DATAREMOVED);
        if ("data-accessed".equals(codeString))
          return new Enumeration<DecisionSupportRuleTriggerType>(this, DecisionSupportRuleTriggerType.DATAACCESSED);
        if ("data-access-ended".equals(codeString))
          return new Enumeration<DecisionSupportRuleTriggerType>(this, DecisionSupportRuleTriggerType.DATAACCESSENDED);
        throw new FHIRException("Unknown DecisionSupportRuleTriggerType code '"+codeString+"'");
        }
    public String toCode(DecisionSupportRuleTriggerType code) {
      if (code == DecisionSupportRuleTriggerType.NAMEDEVENT)
        return "named-event";
      if (code == DecisionSupportRuleTriggerType.PERIODIC)
        return "periodic";
      if (code == DecisionSupportRuleTriggerType.DATAADDED)
        return "data-added";
      if (code == DecisionSupportRuleTriggerType.DATAMODIFIED)
        return "data-modified";
      if (code == DecisionSupportRuleTriggerType.DATAREMOVED)
        return "data-removed";
      if (code == DecisionSupportRuleTriggerType.DATAACCESSED)
        return "data-accessed";
      if (code == DecisionSupportRuleTriggerType.DATAACCESSENDED)
        return "data-access-ended";
      return "?";
      }
    public String toSystem(DecisionSupportRuleTriggerType code) {
      return code.getSystem();
      }
    }

    public enum DecisionSupportRuleParticipantType {
        /**
         * The participant is the patient under evaluation
         */
        PATIENT, 
        /**
         * The participant is a person
         */
        PERSON, 
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
        public static DecisionSupportRuleParticipantType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("patient".equals(codeString))
          return PATIENT;
        if ("person".equals(codeString))
          return PERSON;
        if ("practitioner".equals(codeString))
          return PRACTITIONER;
        if ("related-person".equals(codeString))
          return RELATEDPERSON;
        throw new FHIRException("Unknown DecisionSupportRuleParticipantType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PATIENT: return "patient";
            case PERSON: return "person";
            case PRACTITIONER: return "practitioner";
            case RELATEDPERSON: return "related-person";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case PATIENT: return "http://hl7.org/fhir/cds-rule-participant";
            case PERSON: return "http://hl7.org/fhir/cds-rule-participant";
            case PRACTITIONER: return "http://hl7.org/fhir/cds-rule-participant";
            case RELATEDPERSON: return "http://hl7.org/fhir/cds-rule-participant";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case PATIENT: return "The participant is the patient under evaluation";
            case PERSON: return "The participant is a person";
            case PRACTITIONER: return "The participant is a practitioner involved in the patient's care";
            case RELATEDPERSON: return "The participant is a person related to the patient";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PATIENT: return "Patient";
            case PERSON: return "Person";
            case PRACTITIONER: return "Practitioner";
            case RELATEDPERSON: return "Related Person";
            default: return "?";
          }
        }
    }

  public static class DecisionSupportRuleParticipantTypeEnumFactory implements EnumFactory<DecisionSupportRuleParticipantType> {
    public DecisionSupportRuleParticipantType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("patient".equals(codeString))
          return DecisionSupportRuleParticipantType.PATIENT;
        if ("person".equals(codeString))
          return DecisionSupportRuleParticipantType.PERSON;
        if ("practitioner".equals(codeString))
          return DecisionSupportRuleParticipantType.PRACTITIONER;
        if ("related-person".equals(codeString))
          return DecisionSupportRuleParticipantType.RELATEDPERSON;
        throw new IllegalArgumentException("Unknown DecisionSupportRuleParticipantType code '"+codeString+"'");
        }
        public Enumeration<DecisionSupportRuleParticipantType> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("patient".equals(codeString))
          return new Enumeration<DecisionSupportRuleParticipantType>(this, DecisionSupportRuleParticipantType.PATIENT);
        if ("person".equals(codeString))
          return new Enumeration<DecisionSupportRuleParticipantType>(this, DecisionSupportRuleParticipantType.PERSON);
        if ("practitioner".equals(codeString))
          return new Enumeration<DecisionSupportRuleParticipantType>(this, DecisionSupportRuleParticipantType.PRACTITIONER);
        if ("related-person".equals(codeString))
          return new Enumeration<DecisionSupportRuleParticipantType>(this, DecisionSupportRuleParticipantType.RELATEDPERSON);
        throw new FHIRException("Unknown DecisionSupportRuleParticipantType code '"+codeString+"'");
        }
    public String toCode(DecisionSupportRuleParticipantType code) {
      if (code == DecisionSupportRuleParticipantType.PATIENT)
        return "patient";
      if (code == DecisionSupportRuleParticipantType.PERSON)
        return "person";
      if (code == DecisionSupportRuleParticipantType.PRACTITIONER)
        return "practitioner";
      if (code == DecisionSupportRuleParticipantType.RELATEDPERSON)
        return "related-person";
      return "?";
      }
    public String toSystem(DecisionSupportRuleParticipantType code) {
      return code.getSystem();
      }
    }

    public enum DecisionSupportRuleActionType {
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
        public static DecisionSupportRuleActionType fromCode(String codeString) throws FHIRException {
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
        throw new FHIRException("Unknown DecisionSupportRuleActionType code '"+codeString+"'");
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
            case CREATE: return "http://hl7.org/fhir/cds-rule-action-type";
            case UPDATE: return "http://hl7.org/fhir/cds-rule-action-type";
            case REMOVE: return "http://hl7.org/fhir/cds-rule-action-type";
            case FIREEVENT: return "http://hl7.org/fhir/cds-rule-action-type";
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

  public static class DecisionSupportRuleActionTypeEnumFactory implements EnumFactory<DecisionSupportRuleActionType> {
    public DecisionSupportRuleActionType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("create".equals(codeString))
          return DecisionSupportRuleActionType.CREATE;
        if ("update".equals(codeString))
          return DecisionSupportRuleActionType.UPDATE;
        if ("remove".equals(codeString))
          return DecisionSupportRuleActionType.REMOVE;
        if ("fire-event".equals(codeString))
          return DecisionSupportRuleActionType.FIREEVENT;
        throw new IllegalArgumentException("Unknown DecisionSupportRuleActionType code '"+codeString+"'");
        }
        public Enumeration<DecisionSupportRuleActionType> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("create".equals(codeString))
          return new Enumeration<DecisionSupportRuleActionType>(this, DecisionSupportRuleActionType.CREATE);
        if ("update".equals(codeString))
          return new Enumeration<DecisionSupportRuleActionType>(this, DecisionSupportRuleActionType.UPDATE);
        if ("remove".equals(codeString))
          return new Enumeration<DecisionSupportRuleActionType>(this, DecisionSupportRuleActionType.REMOVE);
        if ("fire-event".equals(codeString))
          return new Enumeration<DecisionSupportRuleActionType>(this, DecisionSupportRuleActionType.FIREEVENT);
        throw new FHIRException("Unknown DecisionSupportRuleActionType code '"+codeString+"'");
        }
    public String toCode(DecisionSupportRuleActionType code) {
      if (code == DecisionSupportRuleActionType.CREATE)
        return "create";
      if (code == DecisionSupportRuleActionType.UPDATE)
        return "update";
      if (code == DecisionSupportRuleActionType.REMOVE)
        return "remove";
      if (code == DecisionSupportRuleActionType.FIREEVENT)
        return "fire-event";
      return "?";
      }
    public String toSystem(DecisionSupportRuleActionType code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class DecisionSupportRuleTriggerComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The type of triggering event.
         */
        @Child(name = "type", type = {CodeType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="named-event | periodic | data-added | data-modified | data-removed | data-accessed | data-access-ended", formalDefinition="The type of triggering event." )
        protected Enumeration<DecisionSupportRuleTriggerType> type;

        /**
         * The name of the event (if this is a named-event trigger).
         */
        @Child(name = "eventName", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="", formalDefinition="The name of the event (if this is a named-event trigger)." )
        protected StringType eventName;

        /**
         * The timing of the event (if this is a period trigger).
         */
        @Child(name = "eventTiming", type = {Timing.class, Schedule.class, DateType.class, DateTimeType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="", formalDefinition="The timing of the event (if this is a period trigger)." )
        protected Type eventTiming;

        private static final long serialVersionUID = 1233218436L;

    /**
     * Constructor
     */
      public DecisionSupportRuleTriggerComponent() {
        super();
      }

    /**
     * Constructor
     */
      public DecisionSupportRuleTriggerComponent(Enumeration<DecisionSupportRuleTriggerType> type) {
        super();
        this.type = type;
      }

        /**
         * @return {@link #type} (The type of triggering event.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public Enumeration<DecisionSupportRuleTriggerType> getTypeElement() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DecisionSupportRuleTriggerComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new Enumeration<DecisionSupportRuleTriggerType>(new DecisionSupportRuleTriggerTypeEnumFactory()); // bb
          return this.type;
        }

        public boolean hasTypeElement() { 
          return this.type != null && !this.type.isEmpty();
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (The type of triggering event.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public DecisionSupportRuleTriggerComponent setTypeElement(Enumeration<DecisionSupportRuleTriggerType> value) { 
          this.type = value;
          return this;
        }

        /**
         * @return The type of triggering event.
         */
        public DecisionSupportRuleTriggerType getType() { 
          return this.type == null ? null : this.type.getValue();
        }

        /**
         * @param value The type of triggering event.
         */
        public DecisionSupportRuleTriggerComponent setType(DecisionSupportRuleTriggerType value) { 
            if (this.type == null)
              this.type = new Enumeration<DecisionSupportRuleTriggerType>(new DecisionSupportRuleTriggerTypeEnumFactory());
            this.type.setValue(value);
          return this;
        }

        /**
         * @return {@link #eventName} (The name of the event (if this is a named-event trigger).). This is the underlying object with id, value and extensions. The accessor "getEventName" gives direct access to the value
         */
        public StringType getEventNameElement() { 
          if (this.eventName == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DecisionSupportRuleTriggerComponent.eventName");
            else if (Configuration.doAutoCreate())
              this.eventName = new StringType(); // bb
          return this.eventName;
        }

        public boolean hasEventNameElement() { 
          return this.eventName != null && !this.eventName.isEmpty();
        }

        public boolean hasEventName() { 
          return this.eventName != null && !this.eventName.isEmpty();
        }

        /**
         * @param value {@link #eventName} (The name of the event (if this is a named-event trigger).). This is the underlying object with id, value and extensions. The accessor "getEventName" gives direct access to the value
         */
        public DecisionSupportRuleTriggerComponent setEventNameElement(StringType value) { 
          this.eventName = value;
          return this;
        }

        /**
         * @return The name of the event (if this is a named-event trigger).
         */
        public String getEventName() { 
          return this.eventName == null ? null : this.eventName.getValue();
        }

        /**
         * @param value The name of the event (if this is a named-event trigger).
         */
        public DecisionSupportRuleTriggerComponent setEventName(String value) { 
          if (Utilities.noString(value))
            this.eventName = null;
          else {
            if (this.eventName == null)
              this.eventName = new StringType();
            this.eventName.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #eventTiming} (The timing of the event (if this is a period trigger).)
         */
        public Type getEventTiming() { 
          return this.eventTiming;
        }

        /**
         * @return {@link #eventTiming} (The timing of the event (if this is a period trigger).)
         */
        public Timing getEventTimingTiming() throws FHIRException { 
          if (!(this.eventTiming instanceof Timing))
            throw new FHIRException("Type mismatch: the type Timing was expected, but "+this.eventTiming.getClass().getName()+" was encountered");
          return (Timing) this.eventTiming;
        }

        public boolean hasEventTimingTiming() { 
          return this.eventTiming instanceof Timing;
        }

        /**
         * @return {@link #eventTiming} (The timing of the event (if this is a period trigger).)
         */
        public Reference getEventTimingReference() throws FHIRException { 
          if (!(this.eventTiming instanceof Reference))
            throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.eventTiming.getClass().getName()+" was encountered");
          return (Reference) this.eventTiming;
        }

        public boolean hasEventTimingReference() { 
          return this.eventTiming instanceof Reference;
        }

        /**
         * @return {@link #eventTiming} (The timing of the event (if this is a period trigger).)
         */
        public DateType getEventTimingDateType() throws FHIRException { 
          if (!(this.eventTiming instanceof DateType))
            throw new FHIRException("Type mismatch: the type DateType was expected, but "+this.eventTiming.getClass().getName()+" was encountered");
          return (DateType) this.eventTiming;
        }

        public boolean hasEventTimingDateType() { 
          return this.eventTiming instanceof DateType;
        }

        /**
         * @return {@link #eventTiming} (The timing of the event (if this is a period trigger).)
         */
        public DateTimeType getEventTimingDateTimeType() throws FHIRException { 
          if (!(this.eventTiming instanceof DateTimeType))
            throw new FHIRException("Type mismatch: the type DateTimeType was expected, but "+this.eventTiming.getClass().getName()+" was encountered");
          return (DateTimeType) this.eventTiming;
        }

        public boolean hasEventTimingDateTimeType() { 
          return this.eventTiming instanceof DateTimeType;
        }

        public boolean hasEventTiming() { 
          return this.eventTiming != null && !this.eventTiming.isEmpty();
        }

        /**
         * @param value {@link #eventTiming} (The timing of the event (if this is a period trigger).)
         */
        public DecisionSupportRuleTriggerComponent setEventTiming(Type value) { 
          this.eventTiming = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("type", "code", "The type of triggering event.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("eventName", "string", "The name of the event (if this is a named-event trigger).", 0, java.lang.Integer.MAX_VALUE, eventName));
          childrenList.add(new Property("eventTiming[x]", "Timing|Reference(Schedule)|date|dateTime", "The timing of the event (if this is a period trigger).", 0, java.lang.Integer.MAX_VALUE, eventTiming));
        }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type"))
          this.type = new DecisionSupportRuleTriggerTypeEnumFactory().fromType(value); // Enumeration<DecisionSupportRuleTriggerType>
        else if (name.equals("eventName"))
          this.eventName = castToString(value); // StringType
        else if (name.equals("eventTiming[x]"))
          this.eventTiming = (Type) value; // Type
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          throw new FHIRException("Cannot call addChild on a primitive type DecisionSupportRule.type");
        }
        else if (name.equals("eventName")) {
          throw new FHIRException("Cannot call addChild on a primitive type DecisionSupportRule.eventName");
        }
        else if (name.equals("eventTimingTiming")) {
          this.eventTiming = new Timing();
          return this.eventTiming;
        }
        else if (name.equals("eventTimingReference")) {
          this.eventTiming = new Reference();
          return this.eventTiming;
        }
        else if (name.equals("eventTimingDate")) {
          this.eventTiming = new DateType();
          return this.eventTiming;
        }
        else if (name.equals("eventTimingDateTime")) {
          this.eventTiming = new DateTimeType();
          return this.eventTiming;
        }
        else
          return super.addChild(name);
      }

      public DecisionSupportRuleTriggerComponent copy() {
        DecisionSupportRuleTriggerComponent dst = new DecisionSupportRuleTriggerComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.eventName = eventName == null ? null : eventName.copy();
        dst.eventTiming = eventTiming == null ? null : eventTiming.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof DecisionSupportRuleTriggerComponent))
          return false;
        DecisionSupportRuleTriggerComponent o = (DecisionSupportRuleTriggerComponent) other;
        return compareDeep(type, o.type, true) && compareDeep(eventName, o.eventName, true) && compareDeep(eventTiming, o.eventTiming, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof DecisionSupportRuleTriggerComponent))
          return false;
        DecisionSupportRuleTriggerComponent o = (DecisionSupportRuleTriggerComponent) other;
        return compareValues(type, o.type, true) && compareValues(eventName, o.eventName, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (type == null || type.isEmpty()) && (eventName == null || eventName.isEmpty())
           && (eventTiming == null || eventTiming.isEmpty());
      }

  public String fhirType() {
    return "DecisionSupportRule.trigger";

  }

  }

    @Block()
    public static class DecisionSupportRuleActionComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A unique identifier for the action.
         */
        @Child(name = "actionIdentifier", type = {Identifier.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="", formalDefinition="A unique identifier for the action." )
        protected Identifier actionIdentifier;

        /**
         * A user-visible number for the action.
         */
        @Child(name = "number", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="", formalDefinition="A user-visible number for the action." )
        protected StringType number;

        /**
         * Supporting evidence for the action.
         */
        @Child(name = "supportingEvidence", type = {Attachment.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="", formalDefinition="Supporting evidence for the action." )
        protected List<Attachment> supportingEvidence;

        /**
         * Supporting documentation for the action.
         */
        @Child(name = "documentation", type = {Attachment.class}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="", formalDefinition="Supporting documentation for the action." )
        protected List<Attachment> documentation;

        /**
         * The type of participant in the action.
         */
        @Child(name = "participantType", type = {CodeType.class}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="patient | person | practitioner | related-person", formalDefinition="The type of participant in the action." )
        protected List<Enumeration<DecisionSupportRuleParticipantType>> participantType;

        /**
         * The title of the action.
         */
        @Child(name = "title", type = {StringType.class}, order=6, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="", formalDefinition="The title of the action." )
        protected StringType title;

        /**
         * A short description of the action.
         */
        @Child(name = "description", type = {StringType.class}, order=7, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="", formalDefinition="A short description of the action." )
        protected StringType description;

        /**
         * A text equivalent of the action to be performed.
         */
        @Child(name = "textEquivalent", type = {StringType.class}, order=8, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="", formalDefinition="A text equivalent of the action to be performed." )
        protected StringType textEquivalent;

        /**
         * Concepts associated with the action.
         */
        @Child(name = "concept", type = {CodeableConcept.class}, order=9, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="", formalDefinition="Concepts associated with the action." )
        protected List<CodeableConcept> concept;

        /**
         * The type of action to perform (create, update, remove).
         */
        @Child(name = "type", type = {CodeType.class}, order=10, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="create | update | remove | fire-event", formalDefinition="The type of action to perform (create, update, remove)." )
        protected Enumeration<DecisionSupportRuleActionType> type;

        /**
         * The resource that is the target of the action (e.g. CommunicationRequest).
         */
        @Child(name = "resource", type = {}, order=11, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="", formalDefinition="The resource that is the target of the action (e.g. CommunicationRequest)." )
        protected Reference resource;

        /**
         * The actual object that is the target of the reference (The resource that is the target of the action (e.g. CommunicationRequest).)
         */
        protected Resource resourceTarget;

        /**
         * Customizations that should be applied to the statically defined resource.
         */
        @Child(name = "customization", type = {}, order=12, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="", formalDefinition="Customizations that should be applied to the statically defined resource." )
        protected List<DecisionSupportRuleActionCustomizationComponent> customization;

        /**
         * Sub actions.
         */
        @Child(name = "actions", type = {DecisionSupportRuleActionComponent.class}, order=13, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="", formalDefinition="Sub actions." )
        protected List<DecisionSupportRuleActionComponent> actions;

        private static final long serialVersionUID = -450005378L;

    /**
     * Constructor
     */
      public DecisionSupportRuleActionComponent() {
        super();
      }

        /**
         * @return {@link #actionIdentifier} (A unique identifier for the action.)
         */
        public Identifier getActionIdentifier() { 
          if (this.actionIdentifier == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DecisionSupportRuleActionComponent.actionIdentifier");
            else if (Configuration.doAutoCreate())
              this.actionIdentifier = new Identifier(); // cc
          return this.actionIdentifier;
        }

        public boolean hasActionIdentifier() { 
          return this.actionIdentifier != null && !this.actionIdentifier.isEmpty();
        }

        /**
         * @param value {@link #actionIdentifier} (A unique identifier for the action.)
         */
        public DecisionSupportRuleActionComponent setActionIdentifier(Identifier value) { 
          this.actionIdentifier = value;
          return this;
        }

        /**
         * @return {@link #number} (A user-visible number for the action.). This is the underlying object with id, value and extensions. The accessor "getNumber" gives direct access to the value
         */
        public StringType getNumberElement() { 
          if (this.number == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DecisionSupportRuleActionComponent.number");
            else if (Configuration.doAutoCreate())
              this.number = new StringType(); // bb
          return this.number;
        }

        public boolean hasNumberElement() { 
          return this.number != null && !this.number.isEmpty();
        }

        public boolean hasNumber() { 
          return this.number != null && !this.number.isEmpty();
        }

        /**
         * @param value {@link #number} (A user-visible number for the action.). This is the underlying object with id, value and extensions. The accessor "getNumber" gives direct access to the value
         */
        public DecisionSupportRuleActionComponent setNumberElement(StringType value) { 
          this.number = value;
          return this;
        }

        /**
         * @return A user-visible number for the action.
         */
        public String getNumber() { 
          return this.number == null ? null : this.number.getValue();
        }

        /**
         * @param value A user-visible number for the action.
         */
        public DecisionSupportRuleActionComponent setNumber(String value) { 
          if (Utilities.noString(value))
            this.number = null;
          else {
            if (this.number == null)
              this.number = new StringType();
            this.number.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #supportingEvidence} (Supporting evidence for the action.)
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
         * @return {@link #supportingEvidence} (Supporting evidence for the action.)
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
        public DecisionSupportRuleActionComponent addSupportingEvidence(Attachment t) { //3
          if (t == null)
            return this;
          if (this.supportingEvidence == null)
            this.supportingEvidence = new ArrayList<Attachment>();
          this.supportingEvidence.add(t);
          return this;
        }

        /**
         * @return {@link #documentation} (Supporting documentation for the action.)
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
         * @return {@link #documentation} (Supporting documentation for the action.)
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
        public DecisionSupportRuleActionComponent addDocumentation(Attachment t) { //3
          if (t == null)
            return this;
          if (this.documentation == null)
            this.documentation = new ArrayList<Attachment>();
          this.documentation.add(t);
          return this;
        }

        /**
         * @return {@link #participantType} (The type of participant in the action.)
         */
        public List<Enumeration<DecisionSupportRuleParticipantType>> getParticipantType() { 
          if (this.participantType == null)
            this.participantType = new ArrayList<Enumeration<DecisionSupportRuleParticipantType>>();
          return this.participantType;
        }

        public boolean hasParticipantType() { 
          if (this.participantType == null)
            return false;
          for (Enumeration<DecisionSupportRuleParticipantType> item : this.participantType)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #participantType} (The type of participant in the action.)
         */
    // syntactic sugar
        public Enumeration<DecisionSupportRuleParticipantType> addParticipantTypeElement() {//2 
          Enumeration<DecisionSupportRuleParticipantType> t = new Enumeration<DecisionSupportRuleParticipantType>(new DecisionSupportRuleParticipantTypeEnumFactory());
          if (this.participantType == null)
            this.participantType = new ArrayList<Enumeration<DecisionSupportRuleParticipantType>>();
          this.participantType.add(t);
          return t;
        }

        /**
         * @param value {@link #participantType} (The type of participant in the action.)
         */
        public DecisionSupportRuleActionComponent addParticipantType(DecisionSupportRuleParticipantType value) { //1
          Enumeration<DecisionSupportRuleParticipantType> t = new Enumeration<DecisionSupportRuleParticipantType>(new DecisionSupportRuleParticipantTypeEnumFactory());
          t.setValue(value);
          if (this.participantType == null)
            this.participantType = new ArrayList<Enumeration<DecisionSupportRuleParticipantType>>();
          this.participantType.add(t);
          return this;
        }

        /**
         * @param value {@link #participantType} (The type of participant in the action.)
         */
        public boolean hasParticipantType(DecisionSupportRuleParticipantType value) { 
          if (this.participantType == null)
            return false;
          for (Enumeration<DecisionSupportRuleParticipantType> v : this.participantType)
            if (v.equals(value)) // code
              return true;
          return false;
        }

        /**
         * @return {@link #title} (The title of the action.). This is the underlying object with id, value and extensions. The accessor "getTitle" gives direct access to the value
         */
        public StringType getTitleElement() { 
          if (this.title == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DecisionSupportRuleActionComponent.title");
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
         * @param value {@link #title} (The title of the action.). This is the underlying object with id, value and extensions. The accessor "getTitle" gives direct access to the value
         */
        public DecisionSupportRuleActionComponent setTitleElement(StringType value) { 
          this.title = value;
          return this;
        }

        /**
         * @return The title of the action.
         */
        public String getTitle() { 
          return this.title == null ? null : this.title.getValue();
        }

        /**
         * @param value The title of the action.
         */
        public DecisionSupportRuleActionComponent setTitle(String value) { 
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
         * @return {@link #description} (A short description of the action.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public StringType getDescriptionElement() { 
          if (this.description == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DecisionSupportRuleActionComponent.description");
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
         * @param value {@link #description} (A short description of the action.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public DecisionSupportRuleActionComponent setDescriptionElement(StringType value) { 
          this.description = value;
          return this;
        }

        /**
         * @return A short description of the action.
         */
        public String getDescription() { 
          return this.description == null ? null : this.description.getValue();
        }

        /**
         * @param value A short description of the action.
         */
        public DecisionSupportRuleActionComponent setDescription(String value) { 
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
         * @return {@link #textEquivalent} (A text equivalent of the action to be performed.). This is the underlying object with id, value and extensions. The accessor "getTextEquivalent" gives direct access to the value
         */
        public StringType getTextEquivalentElement() { 
          if (this.textEquivalent == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DecisionSupportRuleActionComponent.textEquivalent");
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
         * @param value {@link #textEquivalent} (A text equivalent of the action to be performed.). This is the underlying object with id, value and extensions. The accessor "getTextEquivalent" gives direct access to the value
         */
        public DecisionSupportRuleActionComponent setTextEquivalentElement(StringType value) { 
          this.textEquivalent = value;
          return this;
        }

        /**
         * @return A text equivalent of the action to be performed.
         */
        public String getTextEquivalent() { 
          return this.textEquivalent == null ? null : this.textEquivalent.getValue();
        }

        /**
         * @param value A text equivalent of the action to be performed.
         */
        public DecisionSupportRuleActionComponent setTextEquivalent(String value) { 
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
         * @return {@link #concept} (Concepts associated with the action.)
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
         * @return {@link #concept} (Concepts associated with the action.)
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
        public DecisionSupportRuleActionComponent addConcept(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.concept == null)
            this.concept = new ArrayList<CodeableConcept>();
          this.concept.add(t);
          return this;
        }

        /**
         * @return {@link #type} (The type of action to perform (create, update, remove).). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public Enumeration<DecisionSupportRuleActionType> getTypeElement() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DecisionSupportRuleActionComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new Enumeration<DecisionSupportRuleActionType>(new DecisionSupportRuleActionTypeEnumFactory()); // bb
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
        public DecisionSupportRuleActionComponent setTypeElement(Enumeration<DecisionSupportRuleActionType> value) { 
          this.type = value;
          return this;
        }

        /**
         * @return The type of action to perform (create, update, remove).
         */
        public DecisionSupportRuleActionType getType() { 
          return this.type == null ? null : this.type.getValue();
        }

        /**
         * @param value The type of action to perform (create, update, remove).
         */
        public DecisionSupportRuleActionComponent setType(DecisionSupportRuleActionType value) { 
          if (value == null)
            this.type = null;
          else {
            if (this.type == null)
              this.type = new Enumeration<DecisionSupportRuleActionType>(new DecisionSupportRuleActionTypeEnumFactory());
            this.type.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #resource} (The resource that is the target of the action (e.g. CommunicationRequest).)
         */
        public Reference getResource() { 
          if (this.resource == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DecisionSupportRuleActionComponent.resource");
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
        public DecisionSupportRuleActionComponent setResource(Reference value) { 
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
        public DecisionSupportRuleActionComponent setResourceTarget(Resource value) { 
          this.resourceTarget = value;
          return this;
        }

        /**
         * @return {@link #customization} (Customizations that should be applied to the statically defined resource.)
         */
        public List<DecisionSupportRuleActionCustomizationComponent> getCustomization() { 
          if (this.customization == null)
            this.customization = new ArrayList<DecisionSupportRuleActionCustomizationComponent>();
          return this.customization;
        }

        public boolean hasCustomization() { 
          if (this.customization == null)
            return false;
          for (DecisionSupportRuleActionCustomizationComponent item : this.customization)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #customization} (Customizations that should be applied to the statically defined resource.)
         */
    // syntactic sugar
        public DecisionSupportRuleActionCustomizationComponent addCustomization() { //3
          DecisionSupportRuleActionCustomizationComponent t = new DecisionSupportRuleActionCustomizationComponent();
          if (this.customization == null)
            this.customization = new ArrayList<DecisionSupportRuleActionCustomizationComponent>();
          this.customization.add(t);
          return t;
        }

    // syntactic sugar
        public DecisionSupportRuleActionComponent addCustomization(DecisionSupportRuleActionCustomizationComponent t) { //3
          if (t == null)
            return this;
          if (this.customization == null)
            this.customization = new ArrayList<DecisionSupportRuleActionCustomizationComponent>();
          this.customization.add(t);
          return this;
        }

        /**
         * @return {@link #actions} (Sub actions.)
         */
        public List<DecisionSupportRuleActionComponent> getActions() { 
          if (this.actions == null)
            this.actions = new ArrayList<DecisionSupportRuleActionComponent>();
          return this.actions;
        }

        public boolean hasActions() { 
          if (this.actions == null)
            return false;
          for (DecisionSupportRuleActionComponent item : this.actions)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #actions} (Sub actions.)
         */
    // syntactic sugar
        public DecisionSupportRuleActionComponent addActions() { //3
          DecisionSupportRuleActionComponent t = new DecisionSupportRuleActionComponent();
          if (this.actions == null)
            this.actions = new ArrayList<DecisionSupportRuleActionComponent>();
          this.actions.add(t);
          return t;
        }

    // syntactic sugar
        public DecisionSupportRuleActionComponent addActions(DecisionSupportRuleActionComponent t) { //3
          if (t == null)
            return this;
          if (this.actions == null)
            this.actions = new ArrayList<DecisionSupportRuleActionComponent>();
          this.actions.add(t);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("actionIdentifier", "Identifier", "A unique identifier for the action.", 0, java.lang.Integer.MAX_VALUE, actionIdentifier));
          childrenList.add(new Property("number", "string", "A user-visible number for the action.", 0, java.lang.Integer.MAX_VALUE, number));
          childrenList.add(new Property("supportingEvidence", "Attachment", "Supporting evidence for the action.", 0, java.lang.Integer.MAX_VALUE, supportingEvidence));
          childrenList.add(new Property("documentation", "Attachment", "Supporting documentation for the action.", 0, java.lang.Integer.MAX_VALUE, documentation));
          childrenList.add(new Property("participantType", "code", "The type of participant in the action.", 0, java.lang.Integer.MAX_VALUE, participantType));
          childrenList.add(new Property("title", "string", "The title of the action.", 0, java.lang.Integer.MAX_VALUE, title));
          childrenList.add(new Property("description", "string", "A short description of the action.", 0, java.lang.Integer.MAX_VALUE, description));
          childrenList.add(new Property("textEquivalent", "string", "A text equivalent of the action to be performed.", 0, java.lang.Integer.MAX_VALUE, textEquivalent));
          childrenList.add(new Property("concept", "CodeableConcept", "Concepts associated with the action.", 0, java.lang.Integer.MAX_VALUE, concept));
          childrenList.add(new Property("type", "code", "The type of action to perform (create, update, remove).", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("resource", "Reference(Any)", "The resource that is the target of the action (e.g. CommunicationRequest).", 0, java.lang.Integer.MAX_VALUE, resource));
          childrenList.add(new Property("customization", "", "Customizations that should be applied to the statically defined resource.", 0, java.lang.Integer.MAX_VALUE, customization));
          childrenList.add(new Property("actions", "@DecisionSupportRule.action", "Sub actions.", 0, java.lang.Integer.MAX_VALUE, actions));
        }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("actionIdentifier"))
          this.actionIdentifier = castToIdentifier(value); // Identifier
        else if (name.equals("number"))
          this.number = castToString(value); // StringType
        else if (name.equals("supportingEvidence"))
          this.getSupportingEvidence().add(castToAttachment(value));
        else if (name.equals("documentation"))
          this.getDocumentation().add(castToAttachment(value));
        else if (name.equals("participantType"))
          this.getParticipantType().add(new DecisionSupportRuleParticipantTypeEnumFactory().fromType(value));
        else if (name.equals("title"))
          this.title = castToString(value); // StringType
        else if (name.equals("description"))
          this.description = castToString(value); // StringType
        else if (name.equals("textEquivalent"))
          this.textEquivalent = castToString(value); // StringType
        else if (name.equals("concept"))
          this.getConcept().add(castToCodeableConcept(value));
        else if (name.equals("type"))
          this.type = new DecisionSupportRuleActionTypeEnumFactory().fromType(value); // Enumeration<DecisionSupportRuleActionType>
        else if (name.equals("resource"))
          this.resource = castToReference(value); // Reference
        else if (name.equals("customization"))
          this.getCustomization().add((DecisionSupportRuleActionCustomizationComponent) value);
        else if (name.equals("actions"))
          this.getActions().add((DecisionSupportRuleActionComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("actionIdentifier")) {
          this.actionIdentifier = new Identifier();
          return this.actionIdentifier;
        }
        else if (name.equals("number")) {
          throw new FHIRException("Cannot call addChild on a primitive type DecisionSupportRule.number");
        }
        else if (name.equals("supportingEvidence")) {
          return addSupportingEvidence();
        }
        else if (name.equals("documentation")) {
          return addDocumentation();
        }
        else if (name.equals("participantType")) {
          throw new FHIRException("Cannot call addChild on a primitive type DecisionSupportRule.participantType");
        }
        else if (name.equals("title")) {
          throw new FHIRException("Cannot call addChild on a primitive type DecisionSupportRule.title");
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type DecisionSupportRule.description");
        }
        else if (name.equals("textEquivalent")) {
          throw new FHIRException("Cannot call addChild on a primitive type DecisionSupportRule.textEquivalent");
        }
        else if (name.equals("concept")) {
          return addConcept();
        }
        else if (name.equals("type")) {
          throw new FHIRException("Cannot call addChild on a primitive type DecisionSupportRule.type");
        }
        else if (name.equals("resource")) {
          this.resource = new Reference();
          return this.resource;
        }
        else if (name.equals("customization")) {
          return addCustomization();
        }
        else if (name.equals("actions")) {
          return addActions();
        }
        else
          return super.addChild(name);
      }

      public DecisionSupportRuleActionComponent copy() {
        DecisionSupportRuleActionComponent dst = new DecisionSupportRuleActionComponent();
        copyValues(dst);
        dst.actionIdentifier = actionIdentifier == null ? null : actionIdentifier.copy();
        dst.number = number == null ? null : number.copy();
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
        if (participantType != null) {
          dst.participantType = new ArrayList<Enumeration<DecisionSupportRuleParticipantType>>();
          for (Enumeration<DecisionSupportRuleParticipantType> i : participantType)
            dst.participantType.add(i.copy());
        };
        dst.title = title == null ? null : title.copy();
        dst.description = description == null ? null : description.copy();
        dst.textEquivalent = textEquivalent == null ? null : textEquivalent.copy();
        if (concept != null) {
          dst.concept = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : concept)
            dst.concept.add(i.copy());
        };
        dst.type = type == null ? null : type.copy();
        dst.resource = resource == null ? null : resource.copy();
        if (customization != null) {
          dst.customization = new ArrayList<DecisionSupportRuleActionCustomizationComponent>();
          for (DecisionSupportRuleActionCustomizationComponent i : customization)
            dst.customization.add(i.copy());
        };
        if (actions != null) {
          dst.actions = new ArrayList<DecisionSupportRuleActionComponent>();
          for (DecisionSupportRuleActionComponent i : actions)
            dst.actions.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof DecisionSupportRuleActionComponent))
          return false;
        DecisionSupportRuleActionComponent o = (DecisionSupportRuleActionComponent) other;
        return compareDeep(actionIdentifier, o.actionIdentifier, true) && compareDeep(number, o.number, true)
           && compareDeep(supportingEvidence, o.supportingEvidence, true) && compareDeep(documentation, o.documentation, true)
           && compareDeep(participantType, o.participantType, true) && compareDeep(title, o.title, true) && compareDeep(description, o.description, true)
           && compareDeep(textEquivalent, o.textEquivalent, true) && compareDeep(concept, o.concept, true)
           && compareDeep(type, o.type, true) && compareDeep(resource, o.resource, true) && compareDeep(customization, o.customization, true)
           && compareDeep(actions, o.actions, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof DecisionSupportRuleActionComponent))
          return false;
        DecisionSupportRuleActionComponent o = (DecisionSupportRuleActionComponent) other;
        return compareValues(number, o.number, true) && compareValues(participantType, o.participantType, true)
           && compareValues(title, o.title, true) && compareValues(description, o.description, true) && compareValues(textEquivalent, o.textEquivalent, true)
           && compareValues(type, o.type, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (actionIdentifier == null || actionIdentifier.isEmpty()) && (number == null || number.isEmpty())
           && (supportingEvidence == null || supportingEvidence.isEmpty()) && (documentation == null || documentation.isEmpty())
           && (participantType == null || participantType.isEmpty()) && (title == null || title.isEmpty())
           && (description == null || description.isEmpty()) && (textEquivalent == null || textEquivalent.isEmpty())
           && (concept == null || concept.isEmpty()) && (type == null || type.isEmpty()) && (resource == null || resource.isEmpty())
           && (customization == null || customization.isEmpty()) && (actions == null || actions.isEmpty())
          ;
      }

  public String fhirType() {
    return "DecisionSupportRule.action";

  }

  }

    @Block()
    public static class DecisionSupportRuleActionCustomizationComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The path to the element to be customized.
         */
        @Child(name = "path", type = {StringType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="", formalDefinition="The path to the element to be customized." )
        protected StringType path;

        /**
         * An expression specifying the value of the customized element.
         */
        @Child(name = "expression", type = {StringType.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="", formalDefinition="An expression specifying the value of the customized element." )
        protected StringType expression;

        private static final long serialVersionUID = -252690483L;

    /**
     * Constructor
     */
      public DecisionSupportRuleActionCustomizationComponent() {
        super();
      }

    /**
     * Constructor
     */
      public DecisionSupportRuleActionCustomizationComponent(StringType path, StringType expression) {
        super();
        this.path = path;
        this.expression = expression;
      }

        /**
         * @return {@link #path} (The path to the element to be customized.). This is the underlying object with id, value and extensions. The accessor "getPath" gives direct access to the value
         */
        public StringType getPathElement() { 
          if (this.path == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DecisionSupportRuleActionCustomizationComponent.path");
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
         * @param value {@link #path} (The path to the element to be customized.). This is the underlying object with id, value and extensions. The accessor "getPath" gives direct access to the value
         */
        public DecisionSupportRuleActionCustomizationComponent setPathElement(StringType value) { 
          this.path = value;
          return this;
        }

        /**
         * @return The path to the element to be customized.
         */
        public String getPath() { 
          return this.path == null ? null : this.path.getValue();
        }

        /**
         * @param value The path to the element to be customized.
         */
        public DecisionSupportRuleActionCustomizationComponent setPath(String value) { 
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
              throw new Error("Attempt to auto-create DecisionSupportRuleActionCustomizationComponent.expression");
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
        public DecisionSupportRuleActionCustomizationComponent setExpressionElement(StringType value) { 
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
        public DecisionSupportRuleActionCustomizationComponent setExpression(String value) { 
            if (this.expression == null)
              this.expression = new StringType();
            this.expression.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("path", "string", "The path to the element to be customized.", 0, java.lang.Integer.MAX_VALUE, path));
          childrenList.add(new Property("expression", "string", "An expression specifying the value of the customized element.", 0, java.lang.Integer.MAX_VALUE, expression));
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
      public Base addChild(String name) throws FHIRException {
        if (name.equals("path")) {
          throw new FHIRException("Cannot call addChild on a primitive type DecisionSupportRule.path");
        }
        else if (name.equals("expression")) {
          throw new FHIRException("Cannot call addChild on a primitive type DecisionSupportRule.expression");
        }
        else
          return super.addChild(name);
      }

      public DecisionSupportRuleActionCustomizationComponent copy() {
        DecisionSupportRuleActionCustomizationComponent dst = new DecisionSupportRuleActionCustomizationComponent();
        copyValues(dst);
        dst.path = path == null ? null : path.copy();
        dst.expression = expression == null ? null : expression.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof DecisionSupportRuleActionCustomizationComponent))
          return false;
        DecisionSupportRuleActionCustomizationComponent o = (DecisionSupportRuleActionCustomizationComponent) other;
        return compareDeep(path, o.path, true) && compareDeep(expression, o.expression, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof DecisionSupportRuleActionCustomizationComponent))
          return false;
        DecisionSupportRuleActionCustomizationComponent o = (DecisionSupportRuleActionCustomizationComponent) other;
        return compareValues(path, o.path, true) && compareValues(expression, o.expression, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (path == null || path.isEmpty()) && (expression == null || expression.isEmpty())
          ;
      }

  public String fhirType() {
    return "DecisionSupportRule.action.customization";

  }

  }

    /**
     * A logical identifier for the module such as the CMS or NQF identifiers for a measure artifact.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Logical identifier", formalDefinition="A logical identifier for the module such as the CMS or NQF identifiers for a measure artifact." )
    protected List<Identifier> identifier;

    /**
     * The version of the module, if any. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge modules, refer to the Decision Support Service specification.
     */
    @Child(name = "version", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The version of the module, if any", formalDefinition="The version of the module, if any. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge modules, refer to the Decision Support Service specification." )
    protected StringType version;

    /**
     * A reference to a ModuleMetadata resource that provides metadata for the rule.
     */
    @Child(name = "moduleMetadata", type = {ModuleMetadata.class}, order=2, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Module information for the rule", formalDefinition="A reference to a ModuleMetadata resource that provides metadata for the rule." )
    protected Reference moduleMetadata;

    /**
     * The actual object that is the target of the reference (A reference to a ModuleMetadata resource that provides metadata for the rule.)
     */
    protected ModuleMetadata moduleMetadataTarget;

    /**
     * A reference to a Library containing the formal logic used by the rule.
     */
    @Child(name = "library", type = {Library.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="A library containing logic used by the rule", formalDefinition="A reference to a Library containing the formal logic used by the rule." )
    protected List<Reference> library;
    /**
     * The actual objects that are the target of the reference (A reference to a Library containing the formal logic used by the rule.)
     */
    protected List<Library> libraryTarget;


    /**
     * A description of a triggering event.
     */
    @Child(name = "trigger", type = {}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="", formalDefinition="A description of a triggering event." )
    protected List<DecisionSupportRuleTriggerComponent> trigger;

    /**
     * The condition for the artifact.
     */
    @Child(name = "condition", type = {StringType.class}, order=5, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="", formalDefinition="The condition for the artifact." )
    protected StringType condition;

    /**
     * The definition of the actions that should be returned by evaluation of the artifact.
     */
    @Child(name = "action", type = {}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="", formalDefinition="The definition of the actions that should be returned by evaluation of the artifact." )
    protected List<DecisionSupportRuleActionComponent> action;

    private static final long serialVersionUID = 1585395635L;

  /**
   * Constructor
   */
    public DecisionSupportRule() {
      super();
    }

    /**
     * @return {@link #identifier} (A logical identifier for the module such as the CMS or NQF identifiers for a measure artifact.)
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
     * @return {@link #identifier} (A logical identifier for the module such as the CMS or NQF identifiers for a measure artifact.)
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
    public DecisionSupportRule addIdentifier(Identifier t) { //3
      if (t == null)
        return this;
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return this;
    }

    /**
     * @return {@link #version} (The version of the module, if any. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge modules, refer to the Decision Support Service specification.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
     */
    public StringType getVersionElement() { 
      if (this.version == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DecisionSupportRule.version");
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
     * @param value {@link #version} (The version of the module, if any. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge modules, refer to the Decision Support Service specification.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
     */
    public DecisionSupportRule setVersionElement(StringType value) { 
      this.version = value;
      return this;
    }

    /**
     * @return The version of the module, if any. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge modules, refer to the Decision Support Service specification.
     */
    public String getVersion() { 
      return this.version == null ? null : this.version.getValue();
    }

    /**
     * @param value The version of the module, if any. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge modules, refer to the Decision Support Service specification.
     */
    public DecisionSupportRule setVersion(String value) { 
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
     * @return {@link #moduleMetadata} (A reference to a ModuleMetadata resource that provides metadata for the rule.)
     */
    public Reference getModuleMetadata() { 
      if (this.moduleMetadata == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DecisionSupportRule.moduleMetadata");
        else if (Configuration.doAutoCreate())
          this.moduleMetadata = new Reference(); // cc
      return this.moduleMetadata;
    }

    public boolean hasModuleMetadata() { 
      return this.moduleMetadata != null && !this.moduleMetadata.isEmpty();
    }

    /**
     * @param value {@link #moduleMetadata} (A reference to a ModuleMetadata resource that provides metadata for the rule.)
     */
    public DecisionSupportRule setModuleMetadata(Reference value) { 
      this.moduleMetadata = value;
      return this;
    }

    /**
     * @return {@link #moduleMetadata} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (A reference to a ModuleMetadata resource that provides metadata for the rule.)
     */
    public ModuleMetadata getModuleMetadataTarget() { 
      if (this.moduleMetadataTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DecisionSupportRule.moduleMetadata");
        else if (Configuration.doAutoCreate())
          this.moduleMetadataTarget = new ModuleMetadata(); // aa
      return this.moduleMetadataTarget;
    }

    /**
     * @param value {@link #moduleMetadata} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (A reference to a ModuleMetadata resource that provides metadata for the rule.)
     */
    public DecisionSupportRule setModuleMetadataTarget(ModuleMetadata value) { 
      this.moduleMetadataTarget = value;
      return this;
    }

    /**
     * @return {@link #library} (A reference to a Library containing the formal logic used by the rule.)
     */
    public List<Reference> getLibrary() { 
      if (this.library == null)
        this.library = new ArrayList<Reference>();
      return this.library;
    }

    public boolean hasLibrary() { 
      if (this.library == null)
        return false;
      for (Reference item : this.library)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #library} (A reference to a Library containing the formal logic used by the rule.)
     */
    // syntactic sugar
    public Reference addLibrary() { //3
      Reference t = new Reference();
      if (this.library == null)
        this.library = new ArrayList<Reference>();
      this.library.add(t);
      return t;
    }

    // syntactic sugar
    public DecisionSupportRule addLibrary(Reference t) { //3
      if (t == null)
        return this;
      if (this.library == null)
        this.library = new ArrayList<Reference>();
      this.library.add(t);
      return this;
    }

    /**
     * @return {@link #library} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. A reference to a Library containing the formal logic used by the rule.)
     */
    public List<Library> getLibraryTarget() { 
      if (this.libraryTarget == null)
        this.libraryTarget = new ArrayList<Library>();
      return this.libraryTarget;
    }

    // syntactic sugar
    /**
     * @return {@link #library} (Add an actual object that is the target of the reference. The reference library doesn't use these, but you can use this to hold the resources if you resolvethemt. A reference to a Library containing the formal logic used by the rule.)
     */
    public Library addLibraryTarget() { 
      Library r = new Library();
      if (this.libraryTarget == null)
        this.libraryTarget = new ArrayList<Library>();
      this.libraryTarget.add(r);
      return r;
    }

    /**
     * @return {@link #trigger} (A description of a triggering event.)
     */
    public List<DecisionSupportRuleTriggerComponent> getTrigger() { 
      if (this.trigger == null)
        this.trigger = new ArrayList<DecisionSupportRuleTriggerComponent>();
      return this.trigger;
    }

    public boolean hasTrigger() { 
      if (this.trigger == null)
        return false;
      for (DecisionSupportRuleTriggerComponent item : this.trigger)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #trigger} (A description of a triggering event.)
     */
    // syntactic sugar
    public DecisionSupportRuleTriggerComponent addTrigger() { //3
      DecisionSupportRuleTriggerComponent t = new DecisionSupportRuleTriggerComponent();
      if (this.trigger == null)
        this.trigger = new ArrayList<DecisionSupportRuleTriggerComponent>();
      this.trigger.add(t);
      return t;
    }

    // syntactic sugar
    public DecisionSupportRule addTrigger(DecisionSupportRuleTriggerComponent t) { //3
      if (t == null)
        return this;
      if (this.trigger == null)
        this.trigger = new ArrayList<DecisionSupportRuleTriggerComponent>();
      this.trigger.add(t);
      return this;
    }

    /**
     * @return {@link #condition} (The condition for the artifact.). This is the underlying object with id, value and extensions. The accessor "getCondition" gives direct access to the value
     */
    public StringType getConditionElement() { 
      if (this.condition == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DecisionSupportRule.condition");
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
     * @param value {@link #condition} (The condition for the artifact.). This is the underlying object with id, value and extensions. The accessor "getCondition" gives direct access to the value
     */
    public DecisionSupportRule setConditionElement(StringType value) { 
      this.condition = value;
      return this;
    }

    /**
     * @return The condition for the artifact.
     */
    public String getCondition() { 
      return this.condition == null ? null : this.condition.getValue();
    }

    /**
     * @param value The condition for the artifact.
     */
    public DecisionSupportRule setCondition(String value) { 
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
     * @return {@link #action} (The definition of the actions that should be returned by evaluation of the artifact.)
     */
    public List<DecisionSupportRuleActionComponent> getAction() { 
      if (this.action == null)
        this.action = new ArrayList<DecisionSupportRuleActionComponent>();
      return this.action;
    }

    public boolean hasAction() { 
      if (this.action == null)
        return false;
      for (DecisionSupportRuleActionComponent item : this.action)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #action} (The definition of the actions that should be returned by evaluation of the artifact.)
     */
    // syntactic sugar
    public DecisionSupportRuleActionComponent addAction() { //3
      DecisionSupportRuleActionComponent t = new DecisionSupportRuleActionComponent();
      if (this.action == null)
        this.action = new ArrayList<DecisionSupportRuleActionComponent>();
      this.action.add(t);
      return t;
    }

    // syntactic sugar
    public DecisionSupportRule addAction(DecisionSupportRuleActionComponent t) { //3
      if (t == null)
        return this;
      if (this.action == null)
        this.action = new ArrayList<DecisionSupportRuleActionComponent>();
      this.action.add(t);
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "A logical identifier for the module such as the CMS or NQF identifiers for a measure artifact.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("version", "string", "The version of the module, if any. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge modules, refer to the Decision Support Service specification.", 0, java.lang.Integer.MAX_VALUE, version));
        childrenList.add(new Property("moduleMetadata", "Reference(ModuleMetadata)", "A reference to a ModuleMetadata resource that provides metadata for the rule.", 0, java.lang.Integer.MAX_VALUE, moduleMetadata));
        childrenList.add(new Property("library", "Reference(Library)", "A reference to a Library containing the formal logic used by the rule.", 0, java.lang.Integer.MAX_VALUE, library));
        childrenList.add(new Property("trigger", "", "A description of a triggering event.", 0, java.lang.Integer.MAX_VALUE, trigger));
        childrenList.add(new Property("condition", "string", "The condition for the artifact.", 0, java.lang.Integer.MAX_VALUE, condition));
        childrenList.add(new Property("action", "", "The definition of the actions that should be returned by evaluation of the artifact.", 0, java.lang.Integer.MAX_VALUE, action));
      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier"))
          this.getIdentifier().add(castToIdentifier(value));
        else if (name.equals("version"))
          this.version = castToString(value); // StringType
        else if (name.equals("moduleMetadata"))
          this.moduleMetadata = castToReference(value); // Reference
        else if (name.equals("library"))
          this.getLibrary().add(castToReference(value));
        else if (name.equals("trigger"))
          this.getTrigger().add((DecisionSupportRuleTriggerComponent) value);
        else if (name.equals("condition"))
          this.condition = castToString(value); // StringType
        else if (name.equals("action"))
          this.getAction().add((DecisionSupportRuleActionComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("version")) {
          throw new FHIRException("Cannot call addChild on a primitive type DecisionSupportRule.version");
        }
        else if (name.equals("moduleMetadata")) {
          this.moduleMetadata = new Reference();
          return this.moduleMetadata;
        }
        else if (name.equals("library")) {
          return addLibrary();
        }
        else if (name.equals("trigger")) {
          return addTrigger();
        }
        else if (name.equals("condition")) {
          throw new FHIRException("Cannot call addChild on a primitive type DecisionSupportRule.condition");
        }
        else if (name.equals("action")) {
          return addAction();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "DecisionSupportRule";

  }

      public DecisionSupportRule copy() {
        DecisionSupportRule dst = new DecisionSupportRule();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.version = version == null ? null : version.copy();
        dst.moduleMetadata = moduleMetadata == null ? null : moduleMetadata.copy();
        if (library != null) {
          dst.library = new ArrayList<Reference>();
          for (Reference i : library)
            dst.library.add(i.copy());
        };
        if (trigger != null) {
          dst.trigger = new ArrayList<DecisionSupportRuleTriggerComponent>();
          for (DecisionSupportRuleTriggerComponent i : trigger)
            dst.trigger.add(i.copy());
        };
        dst.condition = condition == null ? null : condition.copy();
        if (action != null) {
          dst.action = new ArrayList<DecisionSupportRuleActionComponent>();
          for (DecisionSupportRuleActionComponent i : action)
            dst.action.add(i.copy());
        };
        return dst;
      }

      protected DecisionSupportRule typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof DecisionSupportRule))
          return false;
        DecisionSupportRule o = (DecisionSupportRule) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(version, o.version, true) && compareDeep(moduleMetadata, o.moduleMetadata, true)
           && compareDeep(library, o.library, true) && compareDeep(trigger, o.trigger, true) && compareDeep(condition, o.condition, true)
           && compareDeep(action, o.action, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof DecisionSupportRule))
          return false;
        DecisionSupportRule o = (DecisionSupportRule) other;
        return compareValues(version, o.version, true) && compareValues(condition, o.condition, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (version == null || version.isEmpty())
           && (moduleMetadata == null || moduleMetadata.isEmpty()) && (library == null || library.isEmpty())
           && (trigger == null || trigger.isEmpty()) && (condition == null || condition.isEmpty()) && (action == null || action.isEmpty())
          ;
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.DecisionSupportRule;
   }


}

