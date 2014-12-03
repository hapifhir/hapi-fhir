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

// Generated on Tue, Dec 2, 2014 21:09+1100 for FHIR v0.3.0

import java.util.*;

import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.instance.model.annotations.ResourceDef;
import org.hl7.fhir.instance.model.annotations.SearchParamDefinition;
import org.hl7.fhir.instance.model.annotations.Block;
import org.hl7.fhir.instance.model.annotations.Child;
import org.hl7.fhir.instance.model.annotations.Description;
/**
 * A request for a diagnostic investigation service to be performed.
 */
@ResourceDef(name="DiagnosticOrder", profile="http://hl7.org/fhir/Profile/DiagnosticOrder")
public class DiagnosticOrder extends DomainResource {

    public enum DiagnosticOrderStatus {
        /**
         * The request has been placed.
         */
        REQUESTED, 
        /**
         * The receiving system has received the order, but not yet decided whether it will be performed.
         */
        RECEIVED, 
        /**
         * The receiving system has accepted the order, but work has not yet commenced.
         */
        ACCEPTED, 
        /**
         * The work to fulfill the order is happening.
         */
        INPROGRESS, 
        /**
         * The work is complete, and the outcomes are being reviewed for approval.
         */
        REVIEW, 
        /**
         * The work has been complete, the report(s) released, and no further work is planned.
         */
        COMPLETED, 
        /**
         * The request has been held by originating system/user request.
         */
        SUSPENDED, 
        /**
         * The receiving system has declined to fulfill the request.
         */
        REJECTED, 
        /**
         * The diagnostic investigation was attempted, but due to some procedural error, it could not be completed.
         */
        FAILED, 
        /**
         * added to help the parsers
         */
        NULL;
        public static DiagnosticOrderStatus fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("requested".equals(codeString))
          return REQUESTED;
        if ("received".equals(codeString))
          return RECEIVED;
        if ("accepted".equals(codeString))
          return ACCEPTED;
        if ("in progress".equals(codeString))
          return INPROGRESS;
        if ("review".equals(codeString))
          return REVIEW;
        if ("completed".equals(codeString))
          return COMPLETED;
        if ("suspended".equals(codeString))
          return SUSPENDED;
        if ("rejected".equals(codeString))
          return REJECTED;
        if ("failed".equals(codeString))
          return FAILED;
        throw new Exception("Unknown DiagnosticOrderStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case REQUESTED: return "requested";
            case RECEIVED: return "received";
            case ACCEPTED: return "accepted";
            case INPROGRESS: return "in progress";
            case REVIEW: return "review";
            case COMPLETED: return "completed";
            case SUSPENDED: return "suspended";
            case REJECTED: return "rejected";
            case FAILED: return "failed";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case REQUESTED: return "";
            case RECEIVED: return "";
            case ACCEPTED: return "";
            case INPROGRESS: return "";
            case REVIEW: return "";
            case COMPLETED: return "";
            case SUSPENDED: return "";
            case REJECTED: return "";
            case FAILED: return "";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case REQUESTED: return "The request has been placed.";
            case RECEIVED: return "The receiving system has received the order, but not yet decided whether it will be performed.";
            case ACCEPTED: return "The receiving system has accepted the order, but work has not yet commenced.";
            case INPROGRESS: return "The work to fulfill the order is happening.";
            case REVIEW: return "The work is complete, and the outcomes are being reviewed for approval.";
            case COMPLETED: return "The work has been complete, the report(s) released, and no further work is planned.";
            case SUSPENDED: return "The request has been held by originating system/user request.";
            case REJECTED: return "The receiving system has declined to fulfill the request.";
            case FAILED: return "The diagnostic investigation was attempted, but due to some procedural error, it could not be completed.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case REQUESTED: return "requested";
            case RECEIVED: return "received";
            case ACCEPTED: return "accepted";
            case INPROGRESS: return "in progress";
            case REVIEW: return "review";
            case COMPLETED: return "completed";
            case SUSPENDED: return "suspended";
            case REJECTED: return "rejected";
            case FAILED: return "failed";
            default: return "?";
          }
        }
    }

  public static class DiagnosticOrderStatusEnumFactory implements EnumFactory {
    public Enum<?> fromCode(String codeString) throws Exception {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("requested".equals(codeString))
          return DiagnosticOrderStatus.REQUESTED;
        if ("received".equals(codeString))
          return DiagnosticOrderStatus.RECEIVED;
        if ("accepted".equals(codeString))
          return DiagnosticOrderStatus.ACCEPTED;
        if ("in progress".equals(codeString))
          return DiagnosticOrderStatus.INPROGRESS;
        if ("review".equals(codeString))
          return DiagnosticOrderStatus.REVIEW;
        if ("completed".equals(codeString))
          return DiagnosticOrderStatus.COMPLETED;
        if ("suspended".equals(codeString))
          return DiagnosticOrderStatus.SUSPENDED;
        if ("rejected".equals(codeString))
          return DiagnosticOrderStatus.REJECTED;
        if ("failed".equals(codeString))
          return DiagnosticOrderStatus.FAILED;
        throw new Exception("Unknown DiagnosticOrderStatus code '"+codeString+"'");
        }
    public String toCode(Enum<?> code) throws Exception {
      if (code == DiagnosticOrderStatus.REQUESTED)
        return "requested";
      if (code == DiagnosticOrderStatus.RECEIVED)
        return "received";
      if (code == DiagnosticOrderStatus.ACCEPTED)
        return "accepted";
      if (code == DiagnosticOrderStatus.INPROGRESS)
        return "in progress";
      if (code == DiagnosticOrderStatus.REVIEW)
        return "review";
      if (code == DiagnosticOrderStatus.COMPLETED)
        return "completed";
      if (code == DiagnosticOrderStatus.SUSPENDED)
        return "suspended";
      if (code == DiagnosticOrderStatus.REJECTED)
        return "rejected";
      if (code == DiagnosticOrderStatus.FAILED)
        return "failed";
      return "?";
      }
    }

    public enum DiagnosticOrderPriority {
        /**
         * The order has a normal priority.
         */
        ROUTINE, 
        /**
         * The order should be urgently.
         */
        URGENT, 
        /**
         * The order is time-critical.
         */
        STAT, 
        /**
         * The order should be acted on as soon as possible.
         */
        ASAP, 
        /**
         * added to help the parsers
         */
        NULL;
        public static DiagnosticOrderPriority fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("routine".equals(codeString))
          return ROUTINE;
        if ("urgent".equals(codeString))
          return URGENT;
        if ("stat".equals(codeString))
          return STAT;
        if ("asap".equals(codeString))
          return ASAP;
        throw new Exception("Unknown DiagnosticOrderPriority code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ROUTINE: return "routine";
            case URGENT: return "urgent";
            case STAT: return "stat";
            case ASAP: return "asap";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case ROUTINE: return "";
            case URGENT: return "";
            case STAT: return "";
            case ASAP: return "";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case ROUTINE: return "The order has a normal priority.";
            case URGENT: return "The order should be urgently.";
            case STAT: return "The order is time-critical.";
            case ASAP: return "The order should be acted on as soon as possible.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ROUTINE: return "Routine";
            case URGENT: return "Urgent";
            case STAT: return "Stat";
            case ASAP: return "ASAP";
            default: return "?";
          }
        }
    }

  public static class DiagnosticOrderPriorityEnumFactory implements EnumFactory {
    public Enum<?> fromCode(String codeString) throws Exception {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("routine".equals(codeString))
          return DiagnosticOrderPriority.ROUTINE;
        if ("urgent".equals(codeString))
          return DiagnosticOrderPriority.URGENT;
        if ("stat".equals(codeString))
          return DiagnosticOrderPriority.STAT;
        if ("asap".equals(codeString))
          return DiagnosticOrderPriority.ASAP;
        throw new Exception("Unknown DiagnosticOrderPriority code '"+codeString+"'");
        }
    public String toCode(Enum<?> code) throws Exception {
      if (code == DiagnosticOrderPriority.ROUTINE)
        return "routine";
      if (code == DiagnosticOrderPriority.URGENT)
        return "urgent";
      if (code == DiagnosticOrderPriority.STAT)
        return "stat";
      if (code == DiagnosticOrderPriority.ASAP)
        return "asap";
      return "?";
      }
    }

    @Block()
    public static class DiagnosticOrderEventComponent extends BackboneElement {
        /**
         * The status for the event.
         */
        @Child(name="status", type={CodeType.class}, order=1, min=1, max=1)
        @Description(shortDefinition="requested | received | accepted | in progress | review | completed | suspended | rejected | failed", formalDefinition="The status for the event." )
        protected Enumeration<DiagnosticOrderStatus> status;

        /**
         * Additional information about the event that occurred - e.g. if the status remained unchanged.
         */
        @Child(name="description", type={CodeableConcept.class}, order=2, min=0, max=1)
        @Description(shortDefinition="More information about the event and its context", formalDefinition="Additional information about the event that occurred - e.g. if the status remained unchanged." )
        protected CodeableConcept description;

        /**
         * The date/time at which the event occurred.
         */
        @Child(name="dateTime", type={DateTimeType.class}, order=3, min=1, max=1)
        @Description(shortDefinition="The date at which the event happened", formalDefinition="The date/time at which the event occurred." )
        protected DateTimeType dateTime;

        /**
         * The person who was responsible for performing or recording the action.
         */
        @Child(name="actor", type={Practitioner.class, Device.class}, order=4, min=0, max=1)
        @Description(shortDefinition="Who recorded or did this", formalDefinition="The person who was responsible for performing or recording the action." )
        protected Reference actor;

        /**
         * The actual object that is the target of the reference (The person who was responsible for performing or recording the action.)
         */
        protected Resource actorTarget;

        private static final long serialVersionUID = -370793723L;

      public DiagnosticOrderEventComponent() {
        super();
      }

      public DiagnosticOrderEventComponent(Enumeration<DiagnosticOrderStatus> status, DateTimeType dateTime) {
        super();
        this.status = status;
        this.dateTime = dateTime;
      }

        /**
         * @return {@link #status} (The status for the event.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
         */
        public Enumeration<DiagnosticOrderStatus> getStatusElement() { 
          if (this.status == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DiagnosticOrderEventComponent.status");
            else if (Configuration.doAutoCreate())
              this.status = new Enumeration<DiagnosticOrderStatus>();
          return this.status;
        }

        public boolean hasStatusElement() { 
          return this.status != null && !this.status.isEmpty();
        }

        public boolean hasStatus() { 
          return this.status != null && !this.status.isEmpty();
        }

        /**
         * @param value {@link #status} (The status for the event.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
         */
        public DiagnosticOrderEventComponent setStatusElement(Enumeration<DiagnosticOrderStatus> value) { 
          this.status = value;
          return this;
        }

        /**
         * @return The status for the event.
         */
        public DiagnosticOrderStatus getStatus() { 
          return this.status == null ? null : this.status.getValue();
        }

        /**
         * @param value The status for the event.
         */
        public DiagnosticOrderEventComponent setStatus(DiagnosticOrderStatus value) { 
            if (this.status == null)
              this.status = new Enumeration<DiagnosticOrderStatus>();
            this.status.setValue(value);
          return this;
        }

        /**
         * @return {@link #description} (Additional information about the event that occurred - e.g. if the status remained unchanged.)
         */
        public CodeableConcept getDescription() { 
          if (this.description == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DiagnosticOrderEventComponent.description");
            else if (Configuration.doAutoCreate())
              this.description = new CodeableConcept();
          return this.description;
        }

        public boolean hasDescription() { 
          return this.description != null && !this.description.isEmpty();
        }

        /**
         * @param value {@link #description} (Additional information about the event that occurred - e.g. if the status remained unchanged.)
         */
        public DiagnosticOrderEventComponent setDescription(CodeableConcept value) { 
          this.description = value;
          return this;
        }

        /**
         * @return {@link #dateTime} (The date/time at which the event occurred.). This is the underlying object with id, value and extensions. The accessor "getDateTime" gives direct access to the value
         */
        public DateTimeType getDateTimeElement() { 
          if (this.dateTime == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DiagnosticOrderEventComponent.dateTime");
            else if (Configuration.doAutoCreate())
              this.dateTime = new DateTimeType();
          return this.dateTime;
        }

        public boolean hasDateTimeElement() { 
          return this.dateTime != null && !this.dateTime.isEmpty();
        }

        public boolean hasDateTime() { 
          return this.dateTime != null && !this.dateTime.isEmpty();
        }

        /**
         * @param value {@link #dateTime} (The date/time at which the event occurred.). This is the underlying object with id, value and extensions. The accessor "getDateTime" gives direct access to the value
         */
        public DiagnosticOrderEventComponent setDateTimeElement(DateTimeType value) { 
          this.dateTime = value;
          return this;
        }

        /**
         * @return The date/time at which the event occurred.
         */
        public DateAndTime getDateTime() { 
          return this.dateTime == null ? null : this.dateTime.getValue();
        }

        /**
         * @param value The date/time at which the event occurred.
         */
        public DiagnosticOrderEventComponent setDateTime(DateAndTime value) { 
            if (this.dateTime == null)
              this.dateTime = new DateTimeType();
            this.dateTime.setValue(value);
          return this;
        }

        /**
         * @return {@link #actor} (The person who was responsible for performing or recording the action.)
         */
        public Reference getActor() { 
          if (this.actor == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DiagnosticOrderEventComponent.actor");
            else if (Configuration.doAutoCreate())
              this.actor = new Reference();
          return this.actor;
        }

        public boolean hasActor() { 
          return this.actor != null && !this.actor.isEmpty();
        }

        /**
         * @param value {@link #actor} (The person who was responsible for performing or recording the action.)
         */
        public DiagnosticOrderEventComponent setActor(Reference value) { 
          this.actor = value;
          return this;
        }

        /**
         * @return {@link #actor} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The person who was responsible for performing or recording the action.)
         */
        public Resource getActorTarget() { 
          return this.actorTarget;
        }

        /**
         * @param value {@link #actor} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The person who was responsible for performing or recording the action.)
         */
        public DiagnosticOrderEventComponent setActorTarget(Resource value) { 
          this.actorTarget = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("status", "code", "The status for the event.", 0, java.lang.Integer.MAX_VALUE, status));
          childrenList.add(new Property("description", "CodeableConcept", "Additional information about the event that occurred - e.g. if the status remained unchanged.", 0, java.lang.Integer.MAX_VALUE, description));
          childrenList.add(new Property("dateTime", "dateTime", "The date/time at which the event occurred.", 0, java.lang.Integer.MAX_VALUE, dateTime));
          childrenList.add(new Property("actor", "Reference(Practitioner|Device)", "The person who was responsible for performing or recording the action.", 0, java.lang.Integer.MAX_VALUE, actor));
        }

      public DiagnosticOrderEventComponent copy() {
        DiagnosticOrderEventComponent dst = new DiagnosticOrderEventComponent();
        copyValues(dst);
        dst.status = status == null ? null : status.copy();
        dst.description = description == null ? null : description.copy();
        dst.dateTime = dateTime == null ? null : dateTime.copy();
        dst.actor = actor == null ? null : actor.copy();
        return dst;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (status == null || status.isEmpty()) && (description == null || description.isEmpty())
           && (dateTime == null || dateTime.isEmpty()) && (actor == null || actor.isEmpty());
      }

  }

    @Block()
    public static class DiagnosticOrderItemComponent extends BackboneElement {
        /**
         * A code that identifies a particular diagnostic investigation, or panel of investigations, that have been requested.
         */
        @Child(name="code", type={CodeableConcept.class}, order=1, min=1, max=1)
        @Description(shortDefinition="Code to indicate the item (test or panel) being ordered", formalDefinition="A code that identifies a particular diagnostic investigation, or panel of investigations, that have been requested." )
        protected CodeableConcept code;

        /**
         * If the item is related to a specific speciment.
         */
        @Child(name="specimen", type={Specimen.class}, order=2, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="If this item relates to specific specimens", formalDefinition="If the item is related to a specific speciment." )
        protected List<Reference> specimen;
        /**
         * The actual objects that are the target of the reference (If the item is related to a specific speciment.)
         */
        protected List<Specimen> specimenTarget;


        /**
         * Anatomical location where the request test should be performed.
         */
        @Child(name="bodySite", type={CodeableConcept.class}, order=3, min=0, max=1)
        @Description(shortDefinition="Location of requested test (if applicable)", formalDefinition="Anatomical location where the request test should be performed." )
        protected CodeableConcept bodySite;

        /**
         * The status of this individual item within the order.
         */
        @Child(name="status", type={CodeType.class}, order=4, min=0, max=1)
        @Description(shortDefinition="requested | received | accepted | in progress | review | completed | suspended | rejected | failed", formalDefinition="The status of this individual item within the order." )
        protected Enumeration<DiagnosticOrderStatus> status;

        /**
         * A summary of the events of interest that have occurred as this item of the request is processed.
         */
        @Child(name="event", type={DiagnosticOrderEventComponent.class}, order=5, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="Events specific to this item", formalDefinition="A summary of the events of interest that have occurred as this item of the request is processed." )
        protected List<DiagnosticOrderEventComponent> event;

        private static final long serialVersionUID = 381238192L;

      public DiagnosticOrderItemComponent() {
        super();
      }

      public DiagnosticOrderItemComponent(CodeableConcept code) {
        super();
        this.code = code;
      }

        /**
         * @return {@link #code} (A code that identifies a particular diagnostic investigation, or panel of investigations, that have been requested.)
         */
        public CodeableConcept getCode() { 
          if (this.code == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DiagnosticOrderItemComponent.code");
            else if (Configuration.doAutoCreate())
              this.code = new CodeableConcept();
          return this.code;
        }

        public boolean hasCode() { 
          return this.code != null && !this.code.isEmpty();
        }

        /**
         * @param value {@link #code} (A code that identifies a particular diagnostic investigation, or panel of investigations, that have been requested.)
         */
        public DiagnosticOrderItemComponent setCode(CodeableConcept value) { 
          this.code = value;
          return this;
        }

        /**
         * @return {@link #specimen} (If the item is related to a specific speciment.)
         */
        public List<Reference> getSpecimen() { 
          if (this.specimen == null)
            this.specimen = new ArrayList<Reference>();
          return this.specimen;
        }

        public boolean hasSpecimen() { 
          if (this.specimen == null)
            return false;
          for (Reference item : this.specimen)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #specimen} (If the item is related to a specific speciment.)
         */
    // syntactic sugar
        public Reference addSpecimen() { //3
          Reference t = new Reference();
          if (this.specimen == null)
            this.specimen = new ArrayList<Reference>();
          this.specimen.add(t);
          return t;
        }

        /**
         * @return {@link #specimen} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. If the item is related to a specific speciment.)
         */
        public List<Specimen> getSpecimenTarget() { 
          if (this.specimenTarget == null)
            this.specimenTarget = new ArrayList<Specimen>();
          return this.specimenTarget;
        }

    // syntactic sugar
        /**
         * @return {@link #specimen} (Add an actual object that is the target of the reference. The reference library doesn't use these, but you can use this to hold the resources if you resolvethemt. If the item is related to a specific speciment.)
         */
        public Specimen addSpecimenTarget() { 
          Specimen r = new Specimen();
          if (this.specimenTarget == null)
            this.specimenTarget = new ArrayList<Specimen>();
          this.specimenTarget.add(r);
          return r;
        }

        /**
         * @return {@link #bodySite} (Anatomical location where the request test should be performed.)
         */
        public CodeableConcept getBodySite() { 
          if (this.bodySite == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DiagnosticOrderItemComponent.bodySite");
            else if (Configuration.doAutoCreate())
              this.bodySite = new CodeableConcept();
          return this.bodySite;
        }

        public boolean hasBodySite() { 
          return this.bodySite != null && !this.bodySite.isEmpty();
        }

        /**
         * @param value {@link #bodySite} (Anatomical location where the request test should be performed.)
         */
        public DiagnosticOrderItemComponent setBodySite(CodeableConcept value) { 
          this.bodySite = value;
          return this;
        }

        /**
         * @return {@link #status} (The status of this individual item within the order.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
         */
        public Enumeration<DiagnosticOrderStatus> getStatusElement() { 
          if (this.status == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DiagnosticOrderItemComponent.status");
            else if (Configuration.doAutoCreate())
              this.status = new Enumeration<DiagnosticOrderStatus>();
          return this.status;
        }

        public boolean hasStatusElement() { 
          return this.status != null && !this.status.isEmpty();
        }

        public boolean hasStatus() { 
          return this.status != null && !this.status.isEmpty();
        }

        /**
         * @param value {@link #status} (The status of this individual item within the order.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
         */
        public DiagnosticOrderItemComponent setStatusElement(Enumeration<DiagnosticOrderStatus> value) { 
          this.status = value;
          return this;
        }

        /**
         * @return The status of this individual item within the order.
         */
        public DiagnosticOrderStatus getStatus() { 
          return this.status == null ? null : this.status.getValue();
        }

        /**
         * @param value The status of this individual item within the order.
         */
        public DiagnosticOrderItemComponent setStatus(DiagnosticOrderStatus value) { 
          if (value == null)
            this.status = null;
          else {
            if (this.status == null)
              this.status = new Enumeration<DiagnosticOrderStatus>();
            this.status.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #event} (A summary of the events of interest that have occurred as this item of the request is processed.)
         */
        public List<DiagnosticOrderEventComponent> getEvent() { 
          if (this.event == null)
            this.event = new ArrayList<DiagnosticOrderEventComponent>();
          return this.event;
        }

        public boolean hasEvent() { 
          if (this.event == null)
            return false;
          for (DiagnosticOrderEventComponent item : this.event)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #event} (A summary of the events of interest that have occurred as this item of the request is processed.)
         */
    // syntactic sugar
        public DiagnosticOrderEventComponent addEvent() { //3
          DiagnosticOrderEventComponent t = new DiagnosticOrderEventComponent();
          if (this.event == null)
            this.event = new ArrayList<DiagnosticOrderEventComponent>();
          this.event.add(t);
          return t;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("code", "CodeableConcept", "A code that identifies a particular diagnostic investigation, or panel of investigations, that have been requested.", 0, java.lang.Integer.MAX_VALUE, code));
          childrenList.add(new Property("specimen", "Reference(Specimen)", "If the item is related to a specific speciment.", 0, java.lang.Integer.MAX_VALUE, specimen));
          childrenList.add(new Property("bodySite", "CodeableConcept", "Anatomical location where the request test should be performed.", 0, java.lang.Integer.MAX_VALUE, bodySite));
          childrenList.add(new Property("status", "code", "The status of this individual item within the order.", 0, java.lang.Integer.MAX_VALUE, status));
          childrenList.add(new Property("event", "@DiagnosticOrder.event", "A summary of the events of interest that have occurred as this item of the request is processed.", 0, java.lang.Integer.MAX_VALUE, event));
        }

      public DiagnosticOrderItemComponent copy() {
        DiagnosticOrderItemComponent dst = new DiagnosticOrderItemComponent();
        copyValues(dst);
        dst.code = code == null ? null : code.copy();
        if (specimen != null) {
          dst.specimen = new ArrayList<Reference>();
          for (Reference i : specimen)
            dst.specimen.add(i.copy());
        };
        dst.bodySite = bodySite == null ? null : bodySite.copy();
        dst.status = status == null ? null : status.copy();
        if (event != null) {
          dst.event = new ArrayList<DiagnosticOrderEventComponent>();
          for (DiagnosticOrderEventComponent i : event)
            dst.event.add(i.copy());
        };
        return dst;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (code == null || code.isEmpty()) && (specimen == null || specimen.isEmpty())
           && (bodySite == null || bodySite.isEmpty()) && (status == null || status.isEmpty()) && (event == null || event.isEmpty())
          ;
      }

  }

    /**
     * Who or what the investigation is to be performed on. This is usually a human patient, but diagnostic tests can also be requested on animals, groups of humans or animals, devices such as dialysis machines, or even locations (typically for environmental scans).
     */
    @Child(name="subject", type={Patient.class, Group.class, Location.class, Device.class}, order=-1, min=1, max=1)
    @Description(shortDefinition="Who and/or what test is about", formalDefinition="Who or what the investigation is to be performed on. This is usually a human patient, but diagnostic tests can also be requested on animals, groups of humans or animals, devices such as dialysis machines, or even locations (typically for environmental scans)." )
    protected Reference subject;

    /**
     * The actual object that is the target of the reference (Who or what the investigation is to be performed on. This is usually a human patient, but diagnostic tests can also be requested on animals, groups of humans or animals, devices such as dialysis machines, or even locations (typically for environmental scans).)
     */
    protected Resource subjectTarget;

    /**
     * The practitioner that holds legal responsibility for ordering the investigation.
     */
    @Child(name="orderer", type={Practitioner.class}, order=0, min=0, max=1)
    @Description(shortDefinition="Who ordered the test", formalDefinition="The practitioner that holds legal responsibility for ordering the investigation." )
    protected Reference orderer;

    /**
     * The actual object that is the target of the reference (The practitioner that holds legal responsibility for ordering the investigation.)
     */
    protected Practitioner ordererTarget;

    /**
     * Identifiers assigned to this order by the order or by the receiver.
     */
    @Child(name="identifier", type={Identifier.class}, order=1, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Identifiers assigned to this order", formalDefinition="Identifiers assigned to this order by the order or by the receiver." )
    protected List<Identifier> identifier;

    /**
     * An encounter that provides additional information about the healthcare context in which this request is made.
     */
    @Child(name="encounter", type={Encounter.class}, order=2, min=0, max=1)
    @Description(shortDefinition="The encounter that this diagnostic order is associated with", formalDefinition="An encounter that provides additional information about the healthcare context in which this request is made." )
    protected Reference encounter;

    /**
     * The actual object that is the target of the reference (An encounter that provides additional information about the healthcare context in which this request is made.)
     */
    protected Encounter encounterTarget;

    /**
     * An explanation or justification for why this diagnostic investigation is being requested.
     */
    @Child(name="clinicalNotes", type={StringType.class}, order=3, min=0, max=1)
    @Description(shortDefinition="Explanation/Justification for test", formalDefinition="An explanation or justification for why this diagnostic investigation is being requested." )
    protected StringType clinicalNotes;

    /**
     * Additional clinical information about the patient or specimen that may influence test interpretations.
     */
    @Child(name="supportingInformation", type={Observation.class, Condition.class}, order=4, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Supporting observations or conditions for this request", formalDefinition="Additional clinical information about the patient or specimen that may influence test interpretations." )
    protected List<Reference> supportingInformation;
    /**
     * The actual objects that are the target of the reference (Additional clinical information about the patient or specimen that may influence test interpretations.)
     */
    protected List<Resource> supportingInformationTarget;


    /**
     * One or more specimens that the diagnostic investigation is about.
     */
    @Child(name="specimen", type={Specimen.class}, order=5, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="If the whole order relates to specific specimens", formalDefinition="One or more specimens that the diagnostic investigation is about." )
    protected List<Reference> specimen;
    /**
     * The actual objects that are the target of the reference (One or more specimens that the diagnostic investigation is about.)
     */
    protected List<Specimen> specimenTarget;


    /**
     * The status of the order.
     */
    @Child(name="status", type={CodeType.class}, order=6, min=0, max=1)
    @Description(shortDefinition="requested | received | accepted | in progress | review | completed | suspended | rejected | failed", formalDefinition="The status of the order." )
    protected Enumeration<DiagnosticOrderStatus> status;

    /**
     * The clinical priority associated with this order.
     */
    @Child(name="priority", type={CodeType.class}, order=7, min=0, max=1)
    @Description(shortDefinition="routine | urgent | stat | asap", formalDefinition="The clinical priority associated with this order." )
    protected Enumeration<DiagnosticOrderPriority> priority;

    /**
     * A summary of the events of interest that have occurred as the request is processed. E.g. when the order was made, various processing steps (specimens received), when it was completed.
     */
    @Child(name="event", type={}, order=8, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="A list of events of interest in the lifecycle", formalDefinition="A summary of the events of interest that have occurred as the request is processed. E.g. when the order was made, various processing steps (specimens received), when it was completed." )
    protected List<DiagnosticOrderEventComponent> event;

    /**
     * The specific diagnostic investigations that are requested as part of this request. Sometimes, there can only be one item per request, but in most contexts, more than one investigation can be requested.
     */
    @Child(name="item", type={}, order=9, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="The items the orderer requested", formalDefinition="The specific diagnostic investigations that are requested as part of this request. Sometimes, there can only be one item per request, but in most contexts, more than one investigation can be requested." )
    protected List<DiagnosticOrderItemComponent> item;

    private static final long serialVersionUID = 1028294242L;

    public DiagnosticOrder() {
      super();
    }

    public DiagnosticOrder(Reference subject) {
      super();
      this.subject = subject;
    }

    /**
     * @return {@link #subject} (Who or what the investigation is to be performed on. This is usually a human patient, but diagnostic tests can also be requested on animals, groups of humans or animals, devices such as dialysis machines, or even locations (typically for environmental scans).)
     */
    public Reference getSubject() { 
      if (this.subject == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DiagnosticOrder.subject");
        else if (Configuration.doAutoCreate())
          this.subject = new Reference();
      return this.subject;
    }

    public boolean hasSubject() { 
      return this.subject != null && !this.subject.isEmpty();
    }

    /**
     * @param value {@link #subject} (Who or what the investigation is to be performed on. This is usually a human patient, but diagnostic tests can also be requested on animals, groups of humans or animals, devices such as dialysis machines, or even locations (typically for environmental scans).)
     */
    public DiagnosticOrder setSubject(Reference value) { 
      this.subject = value;
      return this;
    }

    /**
     * @return {@link #subject} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Who or what the investigation is to be performed on. This is usually a human patient, but diagnostic tests can also be requested on animals, groups of humans or animals, devices such as dialysis machines, or even locations (typically for environmental scans).)
     */
    public Resource getSubjectTarget() { 
      return this.subjectTarget;
    }

    /**
     * @param value {@link #subject} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Who or what the investigation is to be performed on. This is usually a human patient, but diagnostic tests can also be requested on animals, groups of humans or animals, devices such as dialysis machines, or even locations (typically for environmental scans).)
     */
    public DiagnosticOrder setSubjectTarget(Resource value) { 
      this.subjectTarget = value;
      return this;
    }

    /**
     * @return {@link #orderer} (The practitioner that holds legal responsibility for ordering the investigation.)
     */
    public Reference getOrderer() { 
      if (this.orderer == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DiagnosticOrder.orderer");
        else if (Configuration.doAutoCreate())
          this.orderer = new Reference();
      return this.orderer;
    }

    public boolean hasOrderer() { 
      return this.orderer != null && !this.orderer.isEmpty();
    }

    /**
     * @param value {@link #orderer} (The practitioner that holds legal responsibility for ordering the investigation.)
     */
    public DiagnosticOrder setOrderer(Reference value) { 
      this.orderer = value;
      return this;
    }

    /**
     * @return {@link #orderer} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The practitioner that holds legal responsibility for ordering the investigation.)
     */
    public Practitioner getOrdererTarget() { 
      if (this.ordererTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DiagnosticOrder.orderer");
        else if (Configuration.doAutoCreate())
          this.ordererTarget = new Practitioner();
      return this.ordererTarget;
    }

    /**
     * @param value {@link #orderer} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The practitioner that holds legal responsibility for ordering the investigation.)
     */
    public DiagnosticOrder setOrdererTarget(Practitioner value) { 
      this.ordererTarget = value;
      return this;
    }

    /**
     * @return {@link #identifier} (Identifiers assigned to this order by the order or by the receiver.)
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
     * @return {@link #identifier} (Identifiers assigned to this order by the order or by the receiver.)
     */
    // syntactic sugar
    public Identifier addIdentifier() { //3
      Identifier t = new Identifier();
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return t;
    }

    /**
     * @return {@link #encounter} (An encounter that provides additional information about the healthcare context in which this request is made.)
     */
    public Reference getEncounter() { 
      if (this.encounter == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DiagnosticOrder.encounter");
        else if (Configuration.doAutoCreate())
          this.encounter = new Reference();
      return this.encounter;
    }

    public boolean hasEncounter() { 
      return this.encounter != null && !this.encounter.isEmpty();
    }

    /**
     * @param value {@link #encounter} (An encounter that provides additional information about the healthcare context in which this request is made.)
     */
    public DiagnosticOrder setEncounter(Reference value) { 
      this.encounter = value;
      return this;
    }

    /**
     * @return {@link #encounter} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (An encounter that provides additional information about the healthcare context in which this request is made.)
     */
    public Encounter getEncounterTarget() { 
      if (this.encounterTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DiagnosticOrder.encounter");
        else if (Configuration.doAutoCreate())
          this.encounterTarget = new Encounter();
      return this.encounterTarget;
    }

    /**
     * @param value {@link #encounter} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (An encounter that provides additional information about the healthcare context in which this request is made.)
     */
    public DiagnosticOrder setEncounterTarget(Encounter value) { 
      this.encounterTarget = value;
      return this;
    }

    /**
     * @return {@link #clinicalNotes} (An explanation or justification for why this diagnostic investigation is being requested.). This is the underlying object with id, value and extensions. The accessor "getClinicalNotes" gives direct access to the value
     */
    public StringType getClinicalNotesElement() { 
      if (this.clinicalNotes == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DiagnosticOrder.clinicalNotes");
        else if (Configuration.doAutoCreate())
          this.clinicalNotes = new StringType();
      return this.clinicalNotes;
    }

    public boolean hasClinicalNotesElement() { 
      return this.clinicalNotes != null && !this.clinicalNotes.isEmpty();
    }

    public boolean hasClinicalNotes() { 
      return this.clinicalNotes != null && !this.clinicalNotes.isEmpty();
    }

    /**
     * @param value {@link #clinicalNotes} (An explanation or justification for why this diagnostic investigation is being requested.). This is the underlying object with id, value and extensions. The accessor "getClinicalNotes" gives direct access to the value
     */
    public DiagnosticOrder setClinicalNotesElement(StringType value) { 
      this.clinicalNotes = value;
      return this;
    }

    /**
     * @return An explanation or justification for why this diagnostic investigation is being requested.
     */
    public String getClinicalNotes() { 
      return this.clinicalNotes == null ? null : this.clinicalNotes.getValue();
    }

    /**
     * @param value An explanation or justification for why this diagnostic investigation is being requested.
     */
    public DiagnosticOrder setClinicalNotes(String value) { 
      if (Utilities.noString(value))
        this.clinicalNotes = null;
      else {
        if (this.clinicalNotes == null)
          this.clinicalNotes = new StringType();
        this.clinicalNotes.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #supportingInformation} (Additional clinical information about the patient or specimen that may influence test interpretations.)
     */
    public List<Reference> getSupportingInformation() { 
      if (this.supportingInformation == null)
        this.supportingInformation = new ArrayList<Reference>();
      return this.supportingInformation;
    }

    public boolean hasSupportingInformation() { 
      if (this.supportingInformation == null)
        return false;
      for (Reference item : this.supportingInformation)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #supportingInformation} (Additional clinical information about the patient or specimen that may influence test interpretations.)
     */
    // syntactic sugar
    public Reference addSupportingInformation() { //3
      Reference t = new Reference();
      if (this.supportingInformation == null)
        this.supportingInformation = new ArrayList<Reference>();
      this.supportingInformation.add(t);
      return t;
    }

    /**
     * @return {@link #supportingInformation} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. Additional clinical information about the patient or specimen that may influence test interpretations.)
     */
    public List<Resource> getSupportingInformationTarget() { 
      if (this.supportingInformationTarget == null)
        this.supportingInformationTarget = new ArrayList<Resource>();
      return this.supportingInformationTarget;
    }

    /**
     * @return {@link #specimen} (One or more specimens that the diagnostic investigation is about.)
     */
    public List<Reference> getSpecimen() { 
      if (this.specimen == null)
        this.specimen = new ArrayList<Reference>();
      return this.specimen;
    }

    public boolean hasSpecimen() { 
      if (this.specimen == null)
        return false;
      for (Reference item : this.specimen)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #specimen} (One or more specimens that the diagnostic investigation is about.)
     */
    // syntactic sugar
    public Reference addSpecimen() { //3
      Reference t = new Reference();
      if (this.specimen == null)
        this.specimen = new ArrayList<Reference>();
      this.specimen.add(t);
      return t;
    }

    /**
     * @return {@link #specimen} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. One or more specimens that the diagnostic investigation is about.)
     */
    public List<Specimen> getSpecimenTarget() { 
      if (this.specimenTarget == null)
        this.specimenTarget = new ArrayList<Specimen>();
      return this.specimenTarget;
    }

    // syntactic sugar
    /**
     * @return {@link #specimen} (Add an actual object that is the target of the reference. The reference library doesn't use these, but you can use this to hold the resources if you resolvethemt. One or more specimens that the diagnostic investigation is about.)
     */
    public Specimen addSpecimenTarget() { 
      Specimen r = new Specimen();
      if (this.specimenTarget == null)
        this.specimenTarget = new ArrayList<Specimen>();
      this.specimenTarget.add(r);
      return r;
    }

    /**
     * @return {@link #status} (The status of the order.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<DiagnosticOrderStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DiagnosticOrder.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<DiagnosticOrderStatus>();
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (The status of the order.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public DiagnosticOrder setStatusElement(Enumeration<DiagnosticOrderStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The status of the order.
     */
    public DiagnosticOrderStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The status of the order.
     */
    public DiagnosticOrder setStatus(DiagnosticOrderStatus value) { 
      if (value == null)
        this.status = null;
      else {
        if (this.status == null)
          this.status = new Enumeration<DiagnosticOrderStatus>();
        this.status.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #priority} (The clinical priority associated with this order.). This is the underlying object with id, value and extensions. The accessor "getPriority" gives direct access to the value
     */
    public Enumeration<DiagnosticOrderPriority> getPriorityElement() { 
      if (this.priority == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DiagnosticOrder.priority");
        else if (Configuration.doAutoCreate())
          this.priority = new Enumeration<DiagnosticOrderPriority>();
      return this.priority;
    }

    public boolean hasPriorityElement() { 
      return this.priority != null && !this.priority.isEmpty();
    }

    public boolean hasPriority() { 
      return this.priority != null && !this.priority.isEmpty();
    }

    /**
     * @param value {@link #priority} (The clinical priority associated with this order.). This is the underlying object with id, value and extensions. The accessor "getPriority" gives direct access to the value
     */
    public DiagnosticOrder setPriorityElement(Enumeration<DiagnosticOrderPriority> value) { 
      this.priority = value;
      return this;
    }

    /**
     * @return The clinical priority associated with this order.
     */
    public DiagnosticOrderPriority getPriority() { 
      return this.priority == null ? null : this.priority.getValue();
    }

    /**
     * @param value The clinical priority associated with this order.
     */
    public DiagnosticOrder setPriority(DiagnosticOrderPriority value) { 
      if (value == null)
        this.priority = null;
      else {
        if (this.priority == null)
          this.priority = new Enumeration<DiagnosticOrderPriority>();
        this.priority.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #event} (A summary of the events of interest that have occurred as the request is processed. E.g. when the order was made, various processing steps (specimens received), when it was completed.)
     */
    public List<DiagnosticOrderEventComponent> getEvent() { 
      if (this.event == null)
        this.event = new ArrayList<DiagnosticOrderEventComponent>();
      return this.event;
    }

    public boolean hasEvent() { 
      if (this.event == null)
        return false;
      for (DiagnosticOrderEventComponent item : this.event)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #event} (A summary of the events of interest that have occurred as the request is processed. E.g. when the order was made, various processing steps (specimens received), when it was completed.)
     */
    // syntactic sugar
    public DiagnosticOrderEventComponent addEvent() { //3
      DiagnosticOrderEventComponent t = new DiagnosticOrderEventComponent();
      if (this.event == null)
        this.event = new ArrayList<DiagnosticOrderEventComponent>();
      this.event.add(t);
      return t;
    }

    /**
     * @return {@link #item} (The specific diagnostic investigations that are requested as part of this request. Sometimes, there can only be one item per request, but in most contexts, more than one investigation can be requested.)
     */
    public List<DiagnosticOrderItemComponent> getItem() { 
      if (this.item == null)
        this.item = new ArrayList<DiagnosticOrderItemComponent>();
      return this.item;
    }

    public boolean hasItem() { 
      if (this.item == null)
        return false;
      for (DiagnosticOrderItemComponent item : this.item)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #item} (The specific diagnostic investigations that are requested as part of this request. Sometimes, there can only be one item per request, but in most contexts, more than one investigation can be requested.)
     */
    // syntactic sugar
    public DiagnosticOrderItemComponent addItem() { //3
      DiagnosticOrderItemComponent t = new DiagnosticOrderItemComponent();
      if (this.item == null)
        this.item = new ArrayList<DiagnosticOrderItemComponent>();
      this.item.add(t);
      return t;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("subject", "Reference(Patient|Group|Location|Device)", "Who or what the investigation is to be performed on. This is usually a human patient, but diagnostic tests can also be requested on animals, groups of humans or animals, devices such as dialysis machines, or even locations (typically for environmental scans).", 0, java.lang.Integer.MAX_VALUE, subject));
        childrenList.add(new Property("orderer", "Reference(Practitioner)", "The practitioner that holds legal responsibility for ordering the investigation.", 0, java.lang.Integer.MAX_VALUE, orderer));
        childrenList.add(new Property("identifier", "Identifier", "Identifiers assigned to this order by the order or by the receiver.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("encounter", "Reference(Encounter)", "An encounter that provides additional information about the healthcare context in which this request is made.", 0, java.lang.Integer.MAX_VALUE, encounter));
        childrenList.add(new Property("clinicalNotes", "string", "An explanation or justification for why this diagnostic investigation is being requested.", 0, java.lang.Integer.MAX_VALUE, clinicalNotes));
        childrenList.add(new Property("supportingInformation", "Reference(Observation|Condition)", "Additional clinical information about the patient or specimen that may influence test interpretations.", 0, java.lang.Integer.MAX_VALUE, supportingInformation));
        childrenList.add(new Property("specimen", "Reference(Specimen)", "One or more specimens that the diagnostic investigation is about.", 0, java.lang.Integer.MAX_VALUE, specimen));
        childrenList.add(new Property("status", "code", "The status of the order.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("priority", "code", "The clinical priority associated with this order.", 0, java.lang.Integer.MAX_VALUE, priority));
        childrenList.add(new Property("event", "", "A summary of the events of interest that have occurred as the request is processed. E.g. when the order was made, various processing steps (specimens received), when it was completed.", 0, java.lang.Integer.MAX_VALUE, event));
        childrenList.add(new Property("item", "", "The specific diagnostic investigations that are requested as part of this request. Sometimes, there can only be one item per request, but in most contexts, more than one investigation can be requested.", 0, java.lang.Integer.MAX_VALUE, item));
      }

      public DiagnosticOrder copy() {
        DiagnosticOrder dst = new DiagnosticOrder();
        copyValues(dst);
        dst.subject = subject == null ? null : subject.copy();
        dst.orderer = orderer == null ? null : orderer.copy();
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.encounter = encounter == null ? null : encounter.copy();
        dst.clinicalNotes = clinicalNotes == null ? null : clinicalNotes.copy();
        if (supportingInformation != null) {
          dst.supportingInformation = new ArrayList<Reference>();
          for (Reference i : supportingInformation)
            dst.supportingInformation.add(i.copy());
        };
        if (specimen != null) {
          dst.specimen = new ArrayList<Reference>();
          for (Reference i : specimen)
            dst.specimen.add(i.copy());
        };
        dst.status = status == null ? null : status.copy();
        dst.priority = priority == null ? null : priority.copy();
        if (event != null) {
          dst.event = new ArrayList<DiagnosticOrderEventComponent>();
          for (DiagnosticOrderEventComponent i : event)
            dst.event.add(i.copy());
        };
        if (item != null) {
          dst.item = new ArrayList<DiagnosticOrderItemComponent>();
          for (DiagnosticOrderItemComponent i : item)
            dst.item.add(i.copy());
        };
        return dst;
      }

      protected DiagnosticOrder typedCopy() {
        return copy();
      }

      public boolean isEmpty() {
        return super.isEmpty() && (subject == null || subject.isEmpty()) && (orderer == null || orderer.isEmpty())
           && (identifier == null || identifier.isEmpty()) && (encounter == null || encounter.isEmpty())
           && (clinicalNotes == null || clinicalNotes.isEmpty()) && (supportingInformation == null || supportingInformation.isEmpty())
           && (specimen == null || specimen.isEmpty()) && (status == null || status.isEmpty()) && (priority == null || priority.isEmpty())
           && (event == null || event.isEmpty()) && (item == null || item.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.DiagnosticOrder;
   }

  @SearchParamDefinition(name="orderer", path="DiagnosticOrder.orderer", description="Who ordered the test", type="reference" )
  public static final String SP_ORDERER = "orderer";
  @SearchParamDefinition(name="status", path="DiagnosticOrder.status", description="requested | received | accepted | in progress | review | completed | suspended | rejected | failed", type="token" )
  public static final String SP_STATUS = "status";
  @SearchParamDefinition(name="subject", path="DiagnosticOrder.subject", description="Who and/or what test is about", type="reference" )
  public static final String SP_SUBJECT = "subject";
  @SearchParamDefinition(name="item-status", path="DiagnosticOrder.item.status", description="requested | received | accepted | in progress | review | completed | suspended | rejected | failed", type="token" )
  public static final String SP_ITEMSTATUS = "item-status";
  @SearchParamDefinition(name="event-status", path="DiagnosticOrder.event.status", description="requested | received | accepted | in progress | review | completed | suspended | rejected | failed", type="token" )
  public static final String SP_EVENTSTATUS = "event-status";
  @SearchParamDefinition(name="actor", path="DiagnosticOrder.event.actor|DiagnosticOrder.item.event.actor", description="Who recorded or did this", type="reference" )
  public static final String SP_ACTOR = "actor";
  @SearchParamDefinition(name="code", path="DiagnosticOrder.item.code", description="Code to indicate the item (test or panel) being ordered", type="token" )
  public static final String SP_CODE = "code";
  @SearchParamDefinition(name="encounter", path="DiagnosticOrder.encounter", description="The encounter that this diagnostic order is associated with", type="reference" )
  public static final String SP_ENCOUNTER = "encounter";
  @SearchParamDefinition(name="item-past-status", path="DiagnosticOrder.item.event.status", description="requested | received | accepted | in progress | review | completed | suspended | rejected | failed", type="token" )
  public static final String SP_ITEMPASTSTATUS = "item-past-status";
  @SearchParamDefinition(name="patient", path="DiagnosticOrder.subject", description="Who and/or what test is about", type="reference" )
  public static final String SP_PATIENT = "patient";
  @SearchParamDefinition(name="bodysite", path="DiagnosticOrder.item.bodySite", description="Location of requested test (if applicable)", type="token" )
  public static final String SP_BODYSITE = "bodysite";
  @SearchParamDefinition(name="item-date", path="DiagnosticOrder.item.event.dateTime", description="The date at which the event happened", type="date" )
  public static final String SP_ITEMDATE = "item-date";
  @SearchParamDefinition(name="specimen", path="DiagnosticOrder.specimen|DiagnosticOrder.item.specimen", description="If the whole order relates to specific specimens", type="reference" )
  public static final String SP_SPECIMEN = "specimen";
  @SearchParamDefinition(name="event-status-date", path="", description="A combination of past-status and date", type="composite" )
  public static final String SP_EVENTSTATUSDATE = "event-status-date";
  @SearchParamDefinition(name="event-date", path="DiagnosticOrder.event.dateTime", description="The date at which the event happened", type="date" )
  public static final String SP_EVENTDATE = "event-date";
  @SearchParamDefinition(name="identifier", path="DiagnosticOrder.identifier", description="Identifiers assigned to this order", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
  @SearchParamDefinition(name="item-status-date", path="", description="A combination of item-past-status and item-date", type="composite" )
  public static final String SP_ITEMSTATUSDATE = "item-status-date";

}

