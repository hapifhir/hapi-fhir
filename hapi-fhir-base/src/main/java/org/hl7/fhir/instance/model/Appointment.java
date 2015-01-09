package org.hl7.fhir.instance.model;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


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

// Generated on Sun, Dec 7, 2014 21:45-0500 for FHIR v0.3.0

import java.util.*;

import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.instance.model.annotations.ResourceDef;
import org.hl7.fhir.instance.model.annotations.SearchParamDefinition;
import org.hl7.fhir.instance.model.annotations.Block;
import org.hl7.fhir.instance.model.annotations.Child;
import org.hl7.fhir.instance.model.annotations.Description;
/**
 * A scheduled healthcare event for a patient and/or practitioner(s) where a service may take place at a specific date/time.
 */
@ResourceDef(name="Appointment", profile="http://hl7.org/fhir/Profile/Appointment")
public class Appointment extends DomainResource {

    public enum Participantrequired implements FhirEnum {
        /**
         * The participant is required to attend the appointment.
         */
        REQUIRED, 
        /**
         * The participant may optionally attend the appointment.
         */
        OPTIONAL, 
        /**
         * The participant is not required to attend the appointment (appointment is about them, not for them).
         */
        INFORMATIONONLY, 
        /**
         * added to help the parsers
         */
        NULL;

      public static final ParticipantrequiredEnumFactory ENUM_FACTORY = new ParticipantrequiredEnumFactory();

        public static Participantrequired fromCode(String codeString) throws IllegalArgumentException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("required".equals(codeString))
          return REQUIRED;
        if ("optional".equals(codeString))
          return OPTIONAL;
        if ("information-only".equals(codeString))
          return INFORMATIONONLY;
        throw new IllegalArgumentException("Unknown Participantrequired code '"+codeString+"'");
        }
        @Override
        public String toCode() {
          switch (this) {
            case REQUIRED: return "required";
            case OPTIONAL: return "optional";
            case INFORMATIONONLY: return "information-only";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case REQUIRED: return "";
            case OPTIONAL: return "";
            case INFORMATIONONLY: return "";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case REQUIRED: return "The participant is required to attend the appointment.";
            case OPTIONAL: return "The participant may optionally attend the appointment.";
            case INFORMATIONONLY: return "The participant is not required to attend the appointment (appointment is about them, not for them).";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case REQUIRED: return "required";
            case OPTIONAL: return "optional";
            case INFORMATIONONLY: return "information-only";
            default: return "?";
          }
        }
    }

  public static class ParticipantrequiredEnumFactory implements EnumFactory<Participantrequired> {
    public Participantrequired fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("required".equals(codeString))
          return Participantrequired.REQUIRED;
        if ("optional".equals(codeString))
          return Participantrequired.OPTIONAL;
        if ("information-only".equals(codeString))
          return Participantrequired.INFORMATIONONLY;
        throw new IllegalArgumentException("Unknown Participantrequired code '"+codeString+"'");
        }
    public String toCode(Participantrequired code) throws IllegalArgumentException {
      if (code == Participantrequired.REQUIRED)
        return "required";
      if (code == Participantrequired.OPTIONAL)
        return "optional";
      if (code == Participantrequired.INFORMATIONONLY)
        return "information-only";
      return "?";
      }
    }

    public enum Participationstatus implements FhirEnum {
        /**
         * The participant has accepted the appointment.
         */
        ACCEPTED, 
        /**
         * The participant has declined the appointment and will not participate in the appointment.
         */
        DECLINED, 
        /**
         * The participant has  tentatively accepted the appointment. This could be automatically created by a system and requires further processing before it can be accepted. There is no commitment that attendance will occur.
         */
        TENTATIVE, 
        /**
         * The participant has started the appointment.
         */
        INPROCESS, 
        /**
         * The participant's involvement in the appointment has been completed.
         */
        COMPLETED, 
        /**
         * The participant needs to indicate if they accept the appointment by changing this status to one of the other statuses.
         */
        NEEDSACTION, 
        /**
         * added to help the parsers
         */
        NULL;

      public static final ParticipationstatusEnumFactory ENUM_FACTORY = new ParticipationstatusEnumFactory();

        public static Participationstatus fromCode(String codeString) throws IllegalArgumentException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("accepted".equals(codeString))
          return ACCEPTED;
        if ("declined".equals(codeString))
          return DECLINED;
        if ("tentative".equals(codeString))
          return TENTATIVE;
        if ("in-process".equals(codeString))
          return INPROCESS;
        if ("completed".equals(codeString))
          return COMPLETED;
        if ("needs-action".equals(codeString))
          return NEEDSACTION;
        throw new IllegalArgumentException("Unknown Participationstatus code '"+codeString+"'");
        }
        @Override
        public String toCode() {
          switch (this) {
            case ACCEPTED: return "accepted";
            case DECLINED: return "declined";
            case TENTATIVE: return "tentative";
            case INPROCESS: return "in-process";
            case COMPLETED: return "completed";
            case NEEDSACTION: return "needs-action";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case ACCEPTED: return "";
            case DECLINED: return "";
            case TENTATIVE: return "";
            case INPROCESS: return "";
            case COMPLETED: return "";
            case NEEDSACTION: return "";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case ACCEPTED: return "The participant has accepted the appointment.";
            case DECLINED: return "The participant has declined the appointment and will not participate in the appointment.";
            case TENTATIVE: return "The participant has  tentatively accepted the appointment. This could be automatically created by a system and requires further processing before it can be accepted. There is no commitment that attendance will occur.";
            case INPROCESS: return "The participant has started the appointment.";
            case COMPLETED: return "The participant's involvement in the appointment has been completed.";
            case NEEDSACTION: return "The participant needs to indicate if they accept the appointment by changing this status to one of the other statuses.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ACCEPTED: return "accepted";
            case DECLINED: return "declined";
            case TENTATIVE: return "tentative";
            case INPROCESS: return "in-process";
            case COMPLETED: return "completed";
            case NEEDSACTION: return "needs-action";
            default: return "?";
          }
        }
    }

  public static class ParticipationstatusEnumFactory implements EnumFactory<Participationstatus> {
    public Participationstatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("accepted".equals(codeString))
          return Participationstatus.ACCEPTED;
        if ("declined".equals(codeString))
          return Participationstatus.DECLINED;
        if ("tentative".equals(codeString))
          return Participationstatus.TENTATIVE;
        if ("in-process".equals(codeString))
          return Participationstatus.INPROCESS;
        if ("completed".equals(codeString))
          return Participationstatus.COMPLETED;
        if ("needs-action".equals(codeString))
          return Participationstatus.NEEDSACTION;
        throw new IllegalArgumentException("Unknown Participationstatus code '"+codeString+"'");
        }
    public String toCode(Participationstatus code) throws IllegalArgumentException {
      if (code == Participationstatus.ACCEPTED)
        return "accepted";
      if (code == Participationstatus.DECLINED)
        return "declined";
      if (code == Participationstatus.TENTATIVE)
        return "tentative";
      if (code == Participationstatus.INPROCESS)
        return "in-process";
      if (code == Participationstatus.COMPLETED)
        return "completed";
      if (code == Participationstatus.NEEDSACTION)
        return "needs-action";
      return "?";
      }
    }

    @Block()
    public static class AppointmentParticipantComponent extends BackboneElement {
        /**
         * Role of participant in the appointment.
         */
        @Child(name="type", type={CodeableConcept.class}, order=1, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="Role of participant in the appointment", formalDefinition="Role of participant in the appointment." )
        protected List<CodeableConcept> type;

        /**
         * A Person of device that is participating in the appointment, usually Practitioner, Patient, RelatedPerson or Device.
         */
        @Child(name="actor", type={}, order=2, min=0, max=1)
        @Description(shortDefinition="A Person of device that is participating in the appointment, usually Practitioner, Patient, RelatedPerson or Device", formalDefinition="A Person of device that is participating in the appointment, usually Practitioner, Patient, RelatedPerson or Device." )
        protected Reference actor;

        /**
         * The actual object that is the target of the reference (A Person of device that is participating in the appointment, usually Practitioner, Patient, RelatedPerson or Device.)
         */
        protected Resource actorTarget;

        /**
         * Is this participant required to be present at the meeting. This covers a use-case where 2 doctors need to meet to discuss the results for a specific patient, and the patient is not required to be present.
         */
        @Child(name="required", type={CodeType.class}, order=3, min=0, max=1)
        @Description(shortDefinition="required | optional | information-only", formalDefinition="Is this participant required to be present at the meeting. This covers a use-case where 2 doctors need to meet to discuss the results for a specific patient, and the patient is not required to be present." )
        protected Enumeration<Participantrequired> required;

        /**
         * Participation status of the Patient.
         */
        @Child(name="status", type={CodeType.class}, order=4, min=1, max=1)
        @Description(shortDefinition="accepted | declined | tentative | in-process | completed | needs-action", formalDefinition="Participation status of the Patient." )
        protected Enumeration<Participationstatus> status;

        private static final long serialVersionUID = -1009855227L;

      public AppointmentParticipantComponent() {
        super();
      }

      public AppointmentParticipantComponent(Enumeration<Participationstatus> status) {
        super();
        this.status = status;
      }

        /**
         * @return {@link #type} (Role of participant in the appointment.)
         */
        public List<CodeableConcept> getType() { 
          if (this.type == null)
            this.type = new ArrayList<CodeableConcept>();
          return this.type;
        }

        public boolean hasType() { 
          if (this.type == null)
            return false;
          for (CodeableConcept item : this.type)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #type} (Role of participant in the appointment.)
         */
    // syntactic sugar
        public CodeableConcept addType() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.type == null)
            this.type = new ArrayList<CodeableConcept>();
          this.type.add(t);
          return t;
        }

        /**
         * @return {@link #actor} (A Person of device that is participating in the appointment, usually Practitioner, Patient, RelatedPerson or Device.)
         */
        public Reference getActor() { 
          if (this.actor == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AppointmentParticipantComponent.actor");
            else if (Configuration.doAutoCreate())
              this.actor = new Reference();
          return this.actor;
        }

        public boolean hasActor() { 
          return this.actor != null && !this.actor.isEmpty();
        }

        /**
         * @param value {@link #actor} (A Person of device that is participating in the appointment, usually Practitioner, Patient, RelatedPerson or Device.)
         */
        public AppointmentParticipantComponent setActor(Reference value) { 
          this.actor = value;
          return this;
        }

        /**
         * @return {@link #actor} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (A Person of device that is participating in the appointment, usually Practitioner, Patient, RelatedPerson or Device.)
         */
        public Resource getActorTarget() { 
          return this.actorTarget;
        }

        /**
         * @param value {@link #actor} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (A Person of device that is participating in the appointment, usually Practitioner, Patient, RelatedPerson or Device.)
         */
        public AppointmentParticipantComponent setActorTarget(Resource value) { 
          this.actorTarget = value;
          return this;
        }

        /**
         * @return {@link #required} (Is this participant required to be present at the meeting. This covers a use-case where 2 doctors need to meet to discuss the results for a specific patient, and the patient is not required to be present.). This is the underlying object with id, value and extensions. The accessor "getRequired" gives direct access to the value
         */
        public Enumeration<Participantrequired> getRequiredElement() { 
          if (this.required == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AppointmentParticipantComponent.required");
            else if (Configuration.doAutoCreate())
              this.required = new Enumeration<Participantrequired>();
          return this.required;
        }

        public boolean hasRequiredElement() { 
          return this.required != null && !this.required.isEmpty();
        }

        public boolean hasRequired() { 
          return this.required != null && !this.required.isEmpty();
        }

        /**
         * @param value {@link #required} (Is this participant required to be present at the meeting. This covers a use-case where 2 doctors need to meet to discuss the results for a specific patient, and the patient is not required to be present.). This is the underlying object with id, value and extensions. The accessor "getRequired" gives direct access to the value
         */
        public AppointmentParticipantComponent setRequiredElement(Enumeration<Participantrequired> value) { 
          this.required = value;
          return this;
        }

        /**
         * @return Is this participant required to be present at the meeting. This covers a use-case where 2 doctors need to meet to discuss the results for a specific patient, and the patient is not required to be present.
         */
        public Participantrequired getRequired() { 
          return this.required == null ? null : this.required.getValue();
        }

        /**
         * @param value Is this participant required to be present at the meeting. This covers a use-case where 2 doctors need to meet to discuss the results for a specific patient, and the patient is not required to be present.
         */
        public AppointmentParticipantComponent setRequired(Participantrequired value) { 
          if (value == null)
            this.required = null;
          else {
            if (this.required == null)
              this.required = new Enumeration<Participantrequired>(Participantrequired.ENUM_FACTORY);
            this.required.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #status} (Participation status of the Patient.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
         */
        public Enumeration<Participationstatus> getStatusElement() { 
          if (this.status == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AppointmentParticipantComponent.status");
            else if (Configuration.doAutoCreate())
              this.status = new Enumeration<Participationstatus>();
          return this.status;
        }

        public boolean hasStatusElement() { 
          return this.status != null && !this.status.isEmpty();
        }

        public boolean hasStatus() { 
          return this.status != null && !this.status.isEmpty();
        }

        /**
         * @param value {@link #status} (Participation status of the Patient.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
         */
        public AppointmentParticipantComponent setStatusElement(Enumeration<Participationstatus> value) { 
          this.status = value;
          return this;
        }

        /**
         * @return Participation status of the Patient.
         */
        public Participationstatus getStatus() { 
          return this.status == null ? null : this.status.getValue();
        }

        /**
         * @param value Participation status of the Patient.
         */
        public AppointmentParticipantComponent setStatus(Participationstatus value) { 
            if (this.status == null)
              this.status = new Enumeration<Participationstatus>(Participationstatus.ENUM_FACTORY);
            this.status.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("type", "CodeableConcept", "Role of participant in the appointment.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("actor", "Reference(Any)", "A Person of device that is participating in the appointment, usually Practitioner, Patient, RelatedPerson or Device.", 0, java.lang.Integer.MAX_VALUE, actor));
          childrenList.add(new Property("required", "code", "Is this participant required to be present at the meeting. This covers a use-case where 2 doctors need to meet to discuss the results for a specific patient, and the patient is not required to be present.", 0, java.lang.Integer.MAX_VALUE, required));
          childrenList.add(new Property("status", "code", "Participation status of the Patient.", 0, java.lang.Integer.MAX_VALUE, status));
        }

      public AppointmentParticipantComponent copy() {
        AppointmentParticipantComponent dst = new AppointmentParticipantComponent();
        copyValues(dst);
        if (type != null) {
          dst.type = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : type)
            dst.type.add(i.copy());
        };
        dst.actor = actor == null ? null : actor.copy();
        dst.required = required == null ? null : required.copy();
        dst.status = status == null ? null : status.copy();
        return dst;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (type == null || type.isEmpty()) && (actor == null || actor.isEmpty())
           && (required == null || required.isEmpty()) && (status == null || status.isEmpty());
      }

  }

    /**
     * This records identifiers associated with this appointment concern that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation).
     */
    @Child(name="identifier", type={Identifier.class}, order=-1, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="External Ids for this item", formalDefinition="This records identifiers associated with this appointment concern that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)." )
    protected List<Identifier> identifier;

    /**
     * The priority of the appointment. Can be used to make informed decisions if needing to re-prioritize appointments. (The iCal Standard specifies 0 as undefined, 1 as highest, 9 as lowest priority) (Need to change back to CodeableConcept).
     */
    @Child(name="priority", type={IntegerType.class}, order=0, min=0, max=1)
    @Description(shortDefinition="The priority of the appointment. Can be used to make informed decisions if needing to re-prioritize appointments. (The iCal Standard specifies 0 as undefined, 1 as highest, 9 as lowest priority) (Need to change back to CodeableConcept)", formalDefinition="The priority of the appointment. Can be used to make informed decisions if needing to re-prioritize appointments. (The iCal Standard specifies 0 as undefined, 1 as highest, 9 as lowest priority) (Need to change back to CodeableConcept)." )
    protected IntegerType priority;

    /**
     * Each of the participants has their own participation status which indicates their involvement in the process, however this status indicates the shared status.
     */
    @Child(name="status", type={CodeType.class}, order=1, min=0, max=1)
    @Description(shortDefinition="The overall status of the Appointment", formalDefinition="Each of the participants has their own participation status which indicates their involvement in the process, however this status indicates the shared status." )
    protected CodeType status;

    /**
     * The type of appointments that is being booked (ideally this would be an identifiable service - which is at a location, rather than the location itself).
     */
    @Child(name="type", type={CodeableConcept.class}, order=2, min=0, max=1)
    @Description(shortDefinition="The type of appointments that is being booked (ideally this would be an identifiable service - which is at a location, rather than the location itself)", formalDefinition="The type of appointments that is being booked (ideally this would be an identifiable service - which is at a location, rather than the location itself)." )
    protected CodeableConcept type;

    /**
     * The reason that this appointment is being scheduled, this is more clinical than administrative.
     */
    @Child(name="reason", type={CodeableConcept.class}, order=3, min=0, max=1)
    @Description(shortDefinition="The reason that this appointment is being scheduled, this is more clinical than administrative", formalDefinition="The reason that this appointment is being scheduled, this is more clinical than administrative." )
    protected CodeableConcept reason;

    /**
     * The brief description of the appointment as would be shown on a subject line in a meeting request, or appointment list. Detailed or expanded information should be put in the comment field.
     */
    @Child(name="description", type={StringType.class}, order=4, min=0, max=1)
    @Description(shortDefinition="The brief description of the appointment as would be shown on a subject line in a meeting request, or appointment list. Detailed or expanded information should be put in the comment field", formalDefinition="The brief description of the appointment as would be shown on a subject line in a meeting request, or appointment list. Detailed or expanded information should be put in the comment field." )
    protected StringType description;

    /**
     * Date/Time that the appointment is to take place.
     */
    @Child(name="start", type={InstantType.class}, order=5, min=1, max=1)
    @Description(shortDefinition="Date/Time that the appointment is to take place", formalDefinition="Date/Time that the appointment is to take place." )
    protected InstantType start;

    /**
     * Date/Time that the appointment is to conclude.
     */
    @Child(name="end", type={InstantType.class}, order=6, min=1, max=1)
    @Description(shortDefinition="Date/Time that the appointment is to conclude", formalDefinition="Date/Time that the appointment is to conclude." )
    protected InstantType end;

    /**
     * The slot that this appointment is filling. If provided then the schedule will not be provided as slots are not recursive, and the start/end values MUST be the same as from the slot.
     */
    @Child(name="slot", type={Slot.class}, order=7, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="The slot that this appointment is filling. If provided then the schedule will not be provided as slots are not recursive, and the start/end values MUST be the same as from the slot", formalDefinition="The slot that this appointment is filling. If provided then the schedule will not be provided as slots are not recursive, and the start/end values MUST be the same as from the slot." )
    protected List<Reference> slot;
    /**
     * The actual objects that are the target of the reference (The slot that this appointment is filling. If provided then the schedule will not be provided as slots are not recursive, and the start/end values MUST be the same as from the slot.)
     */
    protected List<Slot> slotTarget;


    /**
     * The primary location that this appointment is to take place.
     */
    @Child(name="location", type={Location.class}, order=8, min=0, max=1)
    @Description(shortDefinition="The primary location that this appointment is to take place", formalDefinition="The primary location that this appointment is to take place." )
    protected Reference location;

    /**
     * The actual object that is the target of the reference (The primary location that this appointment is to take place.)
     */
    protected Location locationTarget;

    /**
     * Additional comments about the appointment.
     */
    @Child(name="comment", type={StringType.class}, order=9, min=0, max=1)
    @Description(shortDefinition="Additional comments about the appointment", formalDefinition="Additional comments about the appointment." )
    protected StringType comment;

    /**
     * An Order that lead to the creation of this appointment.
     */
    @Child(name="order", type={Order.class}, order=10, min=0, max=1)
    @Description(shortDefinition="An Order that lead to the creation of this appointment", formalDefinition="An Order that lead to the creation of this appointment." )
    protected Reference order;

    /**
     * The actual object that is the target of the reference (An Order that lead to the creation of this appointment.)
     */
    protected Order orderTarget;

    /**
     * List of participants involved in the appointment.
     */
    @Child(name="participant", type={}, order=11, min=1, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="List of participants involved in the appointment", formalDefinition="List of participants involved in the appointment." )
    protected List<AppointmentParticipantComponent> participant;

    /**
     * Who recorded the appointment.
     */
    @Child(name="lastModifiedBy", type={Practitioner.class, Patient.class, RelatedPerson.class}, order=12, min=0, max=1)
    @Description(shortDefinition="Who recorded the appointment", formalDefinition="Who recorded the appointment." )
    protected Reference lastModifiedBy;

    /**
     * The actual object that is the target of the reference (Who recorded the appointment.)
     */
    protected Resource lastModifiedByTarget;

    /**
     * Date when the appointment was recorded.
     */
    @Child(name="lastModified", type={DateTimeType.class}, order=13, min=0, max=1)
    @Description(shortDefinition="Date when the appointment was recorded", formalDefinition="Date when the appointment was recorded." )
    protected DateTimeType lastModified;

    private static final long serialVersionUID = 897214982L;

    public Appointment() {
      super();
    }

    public Appointment(InstantType start, InstantType end) {
      super();
      this.start = start;
      this.end = end;
    }

    /**
     * @return {@link #identifier} (This records identifiers associated with this appointment concern that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation).)
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
     * @return {@link #identifier} (This records identifiers associated with this appointment concern that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation).)
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
     * @return {@link #priority} (The priority of the appointment. Can be used to make informed decisions if needing to re-prioritize appointments. (The iCal Standard specifies 0 as undefined, 1 as highest, 9 as lowest priority) (Need to change back to CodeableConcept).). This is the underlying object with id, value and extensions. The accessor "getPriority" gives direct access to the value
     */
    public IntegerType getPriorityElement() { 
      if (this.priority == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Appointment.priority");
        else if (Configuration.doAutoCreate())
          this.priority = new IntegerType();
      return this.priority;
    }

    public boolean hasPriorityElement() { 
      return this.priority != null && !this.priority.isEmpty();
    }

    public boolean hasPriority() { 
      return this.priority != null && !this.priority.isEmpty();
    }

    /**
     * @param value {@link #priority} (The priority of the appointment. Can be used to make informed decisions if needing to re-prioritize appointments. (The iCal Standard specifies 0 as undefined, 1 as highest, 9 as lowest priority) (Need to change back to CodeableConcept).). This is the underlying object with id, value and extensions. The accessor "getPriority" gives direct access to the value
     */
    public Appointment setPriorityElement(IntegerType value) { 
      this.priority = value;
      return this;
    }

    /**
     * @return The priority of the appointment. Can be used to make informed decisions if needing to re-prioritize appointments. (The iCal Standard specifies 0 as undefined, 1 as highest, 9 as lowest priority) (Need to change back to CodeableConcept).
     */
    public int getPriority() { 
      return this.priority == null ? null : this.priority.getValue();
    }

    /**
     * @param value The priority of the appointment. Can be used to make informed decisions if needing to re-prioritize appointments. (The iCal Standard specifies 0 as undefined, 1 as highest, 9 as lowest priority) (Need to change back to CodeableConcept).
     */
    public Appointment setPriority(int value) { 
      if (value == -1)
        this.priority = null;
      else {
        if (this.priority == null)
          this.priority = new IntegerType();
        this.priority.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #status} (Each of the participants has their own participation status which indicates their involvement in the process, however this status indicates the shared status.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public CodeType getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Appointment.status");
        else if (Configuration.doAutoCreate())
          this.status = new CodeType();
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (Each of the participants has their own participation status which indicates their involvement in the process, however this status indicates the shared status.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Appointment setStatusElement(CodeType value) { 
      this.status = value;
      return this;
    }

    /**
     * @return Each of the participants has their own participation status which indicates their involvement in the process, however this status indicates the shared status.
     */
    public String getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value Each of the participants has their own participation status which indicates their involvement in the process, however this status indicates the shared status.
     */
    public Appointment setStatus(String value) { 
      if (Utilities.noString(value))
        this.status = null;
      else {
        if (this.status == null)
          this.status = new CodeType();
        this.status.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #type} (The type of appointments that is being booked (ideally this would be an identifiable service - which is at a location, rather than the location itself).)
     */
    public CodeableConcept getType() { 
      if (this.type == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Appointment.type");
        else if (Configuration.doAutoCreate())
          this.type = new CodeableConcept();
      return this.type;
    }

    public boolean hasType() { 
      return this.type != null && !this.type.isEmpty();
    }

    /**
     * @param value {@link #type} (The type of appointments that is being booked (ideally this would be an identifiable service - which is at a location, rather than the location itself).)
     */
    public Appointment setType(CodeableConcept value) { 
      this.type = value;
      return this;
    }

    /**
     * @return {@link #reason} (The reason that this appointment is being scheduled, this is more clinical than administrative.)
     */
    public CodeableConcept getReason() { 
      if (this.reason == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Appointment.reason");
        else if (Configuration.doAutoCreate())
          this.reason = new CodeableConcept();
      return this.reason;
    }

    public boolean hasReason() { 
      return this.reason != null && !this.reason.isEmpty();
    }

    /**
     * @param value {@link #reason} (The reason that this appointment is being scheduled, this is more clinical than administrative.)
     */
    public Appointment setReason(CodeableConcept value) { 
      this.reason = value;
      return this;
    }

    /**
     * @return {@link #description} (The brief description of the appointment as would be shown on a subject line in a meeting request, or appointment list. Detailed or expanded information should be put in the comment field.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public StringType getDescriptionElement() { 
      if (this.description == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Appointment.description");
        else if (Configuration.doAutoCreate())
          this.description = new StringType();
      return this.description;
    }

    public boolean hasDescriptionElement() { 
      return this.description != null && !this.description.isEmpty();
    }

    public boolean hasDescription() { 
      return this.description != null && !this.description.isEmpty();
    }

    /**
     * @param value {@link #description} (The brief description of the appointment as would be shown on a subject line in a meeting request, or appointment list. Detailed or expanded information should be put in the comment field.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public Appointment setDescriptionElement(StringType value) { 
      this.description = value;
      return this;
    }

    /**
     * @return The brief description of the appointment as would be shown on a subject line in a meeting request, or appointment list. Detailed or expanded information should be put in the comment field.
     */
    public String getDescription() { 
      return this.description == null ? null : this.description.getValue();
    }

    /**
     * @param value The brief description of the appointment as would be shown on a subject line in a meeting request, or appointment list. Detailed or expanded information should be put in the comment field.
     */
    public Appointment setDescription(String value) { 
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
     * @return {@link #start} (Date/Time that the appointment is to take place.). This is the underlying object with id, value and extensions. The accessor "getStart" gives direct access to the value
     */
    public InstantType getStartElement() { 
      if (this.start == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Appointment.start");
        else if (Configuration.doAutoCreate())
          this.start = new InstantType();
      return this.start;
    }

    public boolean hasStartElement() { 
      return this.start != null && !this.start.isEmpty();
    }

    public boolean hasStart() { 
      return this.start != null && !this.start.isEmpty();
    }

    /**
     * @param value {@link #start} (Date/Time that the appointment is to take place.). This is the underlying object with id, value and extensions. The accessor "getStart" gives direct access to the value
     */
    public Appointment setStartElement(InstantType value) { 
      this.start = value;
      return this;
    }

    /**
     * @return Date/Time that the appointment is to take place.
     */
    public Date getStart() { 
      return this.start == null ? null : this.start.getValue();
    }

    /**
     * @param value Date/Time that the appointment is to take place.
     */
    public Appointment setStart(Date value) { 
        if (this.start == null)
          this.start = new InstantType();
        this.start.setValue(value);
      return this;
    }

    /**
     * @return {@link #end} (Date/Time that the appointment is to conclude.). This is the underlying object with id, value and extensions. The accessor "getEnd" gives direct access to the value
     */
    public InstantType getEndElement() { 
      if (this.end == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Appointment.end");
        else if (Configuration.doAutoCreate())
          this.end = new InstantType();
      return this.end;
    }

    public boolean hasEndElement() { 
      return this.end != null && !this.end.isEmpty();
    }

    public boolean hasEnd() { 
      return this.end != null && !this.end.isEmpty();
    }

    /**
     * @param value {@link #end} (Date/Time that the appointment is to conclude.). This is the underlying object with id, value and extensions. The accessor "getEnd" gives direct access to the value
     */
    public Appointment setEndElement(InstantType value) { 
      this.end = value;
      return this;
    }

    /**
     * @return Date/Time that the appointment is to conclude.
     */
    public Date getEnd() { 
      return this.end == null ? null : this.end.getValue();
    }

    /**
     * @param value Date/Time that the appointment is to conclude.
     */
    public Appointment setEnd(Date value) { 
        if (this.end == null)
          this.end = new InstantType();
        this.end.setValue(value);
      return this;
    }

    /**
     * @return {@link #slot} (The slot that this appointment is filling. If provided then the schedule will not be provided as slots are not recursive, and the start/end values MUST be the same as from the slot.)
     */
    public List<Reference> getSlot() { 
      if (this.slot == null)
        this.slot = new ArrayList<Reference>();
      return this.slot;
    }

    public boolean hasSlot() { 
      if (this.slot == null)
        return false;
      for (Reference item : this.slot)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #slot} (The slot that this appointment is filling. If provided then the schedule will not be provided as slots are not recursive, and the start/end values MUST be the same as from the slot.)
     */
    // syntactic sugar
    public Reference addSlot() { //3
      Reference t = new Reference();
      if (this.slot == null)
        this.slot = new ArrayList<Reference>();
      this.slot.add(t);
      return t;
    }

    /**
     * @return {@link #slot} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. The slot that this appointment is filling. If provided then the schedule will not be provided as slots are not recursive, and the start/end values MUST be the same as from the slot.)
     */
    public List<Slot> getSlotTarget() { 
      if (this.slotTarget == null)
        this.slotTarget = new ArrayList<Slot>();
      return this.slotTarget;
    }

    // syntactic sugar
    /**
     * @return {@link #slot} (Add an actual object that is the target of the reference. The reference library doesn't use these, but you can use this to hold the resources if you resolvethemt. The slot that this appointment is filling. If provided then the schedule will not be provided as slots are not recursive, and the start/end values MUST be the same as from the slot.)
     */
    public Slot addSlotTarget() { 
      Slot r = new Slot();
      if (this.slotTarget == null)
        this.slotTarget = new ArrayList<Slot>();
      this.slotTarget.add(r);
      return r;
    }

    /**
     * @return {@link #location} (The primary location that this appointment is to take place.)
     */
    public Reference getLocation() { 
      if (this.location == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Appointment.location");
        else if (Configuration.doAutoCreate())
          this.location = new Reference();
      return this.location;
    }

    public boolean hasLocation() { 
      return this.location != null && !this.location.isEmpty();
    }

    /**
     * @param value {@link #location} (The primary location that this appointment is to take place.)
     */
    public Appointment setLocation(Reference value) { 
      this.location = value;
      return this;
    }

    /**
     * @return {@link #location} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The primary location that this appointment is to take place.)
     */
    public Location getLocationTarget() { 
      if (this.locationTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Appointment.location");
        else if (Configuration.doAutoCreate())
          this.locationTarget = new Location();
      return this.locationTarget;
    }

    /**
     * @param value {@link #location} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The primary location that this appointment is to take place.)
     */
    public Appointment setLocationTarget(Location value) { 
      this.locationTarget = value;
      return this;
    }

    /**
     * @return {@link #comment} (Additional comments about the appointment.). This is the underlying object with id, value and extensions. The accessor "getComment" gives direct access to the value
     */
    public StringType getCommentElement() { 
      if (this.comment == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Appointment.comment");
        else if (Configuration.doAutoCreate())
          this.comment = new StringType();
      return this.comment;
    }

    public boolean hasCommentElement() { 
      return this.comment != null && !this.comment.isEmpty();
    }

    public boolean hasComment() { 
      return this.comment != null && !this.comment.isEmpty();
    }

    /**
     * @param value {@link #comment} (Additional comments about the appointment.). This is the underlying object with id, value and extensions. The accessor "getComment" gives direct access to the value
     */
    public Appointment setCommentElement(StringType value) { 
      this.comment = value;
      return this;
    }

    /**
     * @return Additional comments about the appointment.
     */
    public String getComment() { 
      return this.comment == null ? null : this.comment.getValue();
    }

    /**
     * @param value Additional comments about the appointment.
     */
    public Appointment setComment(String value) { 
      if (Utilities.noString(value))
        this.comment = null;
      else {
        if (this.comment == null)
          this.comment = new StringType();
        this.comment.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #order} (An Order that lead to the creation of this appointment.)
     */
    public Reference getOrder() { 
      if (this.order == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Appointment.order");
        else if (Configuration.doAutoCreate())
          this.order = new Reference();
      return this.order;
    }

    public boolean hasOrder() { 
      return this.order != null && !this.order.isEmpty();
    }

    /**
     * @param value {@link #order} (An Order that lead to the creation of this appointment.)
     */
    public Appointment setOrder(Reference value) { 
      this.order = value;
      return this;
    }

    /**
     * @return {@link #order} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (An Order that lead to the creation of this appointment.)
     */
    public Order getOrderTarget() { 
      if (this.orderTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Appointment.order");
        else if (Configuration.doAutoCreate())
          this.orderTarget = new Order();
      return this.orderTarget;
    }

    /**
     * @param value {@link #order} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (An Order that lead to the creation of this appointment.)
     */
    public Appointment setOrderTarget(Order value) { 
      this.orderTarget = value;
      return this;
    }

    /**
     * @return {@link #participant} (List of participants involved in the appointment.)
     */
    public List<AppointmentParticipantComponent> getParticipant() { 
      if (this.participant == null)
        this.participant = new ArrayList<AppointmentParticipantComponent>();
      return this.participant;
    }

    public boolean hasParticipant() { 
      if (this.participant == null)
        return false;
      for (AppointmentParticipantComponent item : this.participant)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #participant} (List of participants involved in the appointment.)
     */
    // syntactic sugar
    public AppointmentParticipantComponent addParticipant() { //3
      AppointmentParticipantComponent t = new AppointmentParticipantComponent();
      if (this.participant == null)
        this.participant = new ArrayList<AppointmentParticipantComponent>();
      this.participant.add(t);
      return t;
    }

    /**
     * @return {@link #lastModifiedBy} (Who recorded the appointment.)
     */
    public Reference getLastModifiedBy() { 
      if (this.lastModifiedBy == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Appointment.lastModifiedBy");
        else if (Configuration.doAutoCreate())
          this.lastModifiedBy = new Reference();
      return this.lastModifiedBy;
    }

    public boolean hasLastModifiedBy() { 
      return this.lastModifiedBy != null && !this.lastModifiedBy.isEmpty();
    }

    /**
     * @param value {@link #lastModifiedBy} (Who recorded the appointment.)
     */
    public Appointment setLastModifiedBy(Reference value) { 
      this.lastModifiedBy = value;
      return this;
    }

    /**
     * @return {@link #lastModifiedBy} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Who recorded the appointment.)
     */
    public Resource getLastModifiedByTarget() { 
      return this.lastModifiedByTarget;
    }

    /**
     * @param value {@link #lastModifiedBy} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Who recorded the appointment.)
     */
    public Appointment setLastModifiedByTarget(Resource value) { 
      this.lastModifiedByTarget = value;
      return this;
    }

    /**
     * @return {@link #lastModified} (Date when the appointment was recorded.). This is the underlying object with id, value and extensions. The accessor "getLastModified" gives direct access to the value
     */
    public DateTimeType getLastModifiedElement() { 
      if (this.lastModified == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Appointment.lastModified");
        else if (Configuration.doAutoCreate())
          this.lastModified = new DateTimeType();
      return this.lastModified;
    }

    public boolean hasLastModifiedElement() { 
      return this.lastModified != null && !this.lastModified.isEmpty();
    }

    public boolean hasLastModified() { 
      return this.lastModified != null && !this.lastModified.isEmpty();
    }

    /**
     * @param value {@link #lastModified} (Date when the appointment was recorded.). This is the underlying object with id, value and extensions. The accessor "getLastModified" gives direct access to the value
     */
    public Appointment setLastModifiedElement(DateTimeType value) { 
      this.lastModified = value;
      return this;
    }

    /**
     * @return Date when the appointment was recorded.
     */
    public Date getLastModified() { 
      return this.lastModified == null ? null : this.lastModified.getValue();
    }

    /**
     * @param value Date when the appointment was recorded.
     */
    public Appointment setLastModified(Date value) { 
      if (value == null)
        this.lastModified = null;
      else {
        if (this.lastModified == null)
          this.lastModified = new DateTimeType();
        this.lastModified.setValue(value);
      }
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "This records identifiers associated with this appointment concern that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation).", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("priority", "integer", "The priority of the appointment. Can be used to make informed decisions if needing to re-prioritize appointments. (The iCal Standard specifies 0 as undefined, 1 as highest, 9 as lowest priority) (Need to change back to CodeableConcept).", 0, java.lang.Integer.MAX_VALUE, priority));
        childrenList.add(new Property("status", "code", "Each of the participants has their own participation status which indicates their involvement in the process, however this status indicates the shared status.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("type", "CodeableConcept", "The type of appointments that is being booked (ideally this would be an identifiable service - which is at a location, rather than the location itself).", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("reason", "CodeableConcept", "The reason that this appointment is being scheduled, this is more clinical than administrative.", 0, java.lang.Integer.MAX_VALUE, reason));
        childrenList.add(new Property("description", "string", "The brief description of the appointment as would be shown on a subject line in a meeting request, or appointment list. Detailed or expanded information should be put in the comment field.", 0, java.lang.Integer.MAX_VALUE, description));
        childrenList.add(new Property("start", "instant", "Date/Time that the appointment is to take place.", 0, java.lang.Integer.MAX_VALUE, start));
        childrenList.add(new Property("end", "instant", "Date/Time that the appointment is to conclude.", 0, java.lang.Integer.MAX_VALUE, end));
        childrenList.add(new Property("slot", "Reference(Slot)", "The slot that this appointment is filling. If provided then the schedule will not be provided as slots are not recursive, and the start/end values MUST be the same as from the slot.", 0, java.lang.Integer.MAX_VALUE, slot));
        childrenList.add(new Property("location", "Reference(Location)", "The primary location that this appointment is to take place.", 0, java.lang.Integer.MAX_VALUE, location));
        childrenList.add(new Property("comment", "string", "Additional comments about the appointment.", 0, java.lang.Integer.MAX_VALUE, comment));
        childrenList.add(new Property("order", "Reference(Order)", "An Order that lead to the creation of this appointment.", 0, java.lang.Integer.MAX_VALUE, order));
        childrenList.add(new Property("participant", "", "List of participants involved in the appointment.", 0, java.lang.Integer.MAX_VALUE, participant));
        childrenList.add(new Property("lastModifiedBy", "Reference(Practitioner|Patient|RelatedPerson)", "Who recorded the appointment.", 0, java.lang.Integer.MAX_VALUE, lastModifiedBy));
        childrenList.add(new Property("lastModified", "dateTime", "Date when the appointment was recorded.", 0, java.lang.Integer.MAX_VALUE, lastModified));
      }

      public Appointment copy() {
        Appointment dst = new Appointment();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.priority = priority == null ? null : priority.copy();
        dst.status = status == null ? null : status.copy();
        dst.type = type == null ? null : type.copy();
        dst.reason = reason == null ? null : reason.copy();
        dst.description = description == null ? null : description.copy();
        dst.start = start == null ? null : start.copy();
        dst.end = end == null ? null : end.copy();
        if (slot != null) {
          dst.slot = new ArrayList<Reference>();
          for (Reference i : slot)
            dst.slot.add(i.copy());
        };
        dst.location = location == null ? null : location.copy();
        dst.comment = comment == null ? null : comment.copy();
        dst.order = order == null ? null : order.copy();
        if (participant != null) {
          dst.participant = new ArrayList<AppointmentParticipantComponent>();
          for (AppointmentParticipantComponent i : participant)
            dst.participant.add(i.copy());
        };
        dst.lastModifiedBy = lastModifiedBy == null ? null : lastModifiedBy.copy();
        dst.lastModified = lastModified == null ? null : lastModified.copy();
        return dst;
      }

      protected Appointment typedCopy() {
        return copy();
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (priority == null || priority.isEmpty())
           && (status == null || status.isEmpty()) && (type == null || type.isEmpty()) && (reason == null || reason.isEmpty())
           && (description == null || description.isEmpty()) && (start == null || start.isEmpty()) && (end == null || end.isEmpty())
           && (slot == null || slot.isEmpty()) && (location == null || location.isEmpty()) && (comment == null || comment.isEmpty())
           && (order == null || order.isEmpty()) && (participant == null || participant.isEmpty()) && (lastModifiedBy == null || lastModifiedBy.isEmpty())
           && (lastModified == null || lastModified.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Appointment;
   }

  @SearchParamDefinition(name="partstatus", path="Appointment.participant.status", description="The Participation status of the subject, or other participant on the appointment", type="token" )
  public static final String SP_PARTSTATUS = "partstatus";
  @SearchParamDefinition(name="status", path="Appointment.status", description="The overall status of the appointment", type="string" )
  public static final String SP_STATUS = "status";
  @SearchParamDefinition(name="actor", path="Appointment.participant.actor", description="Any one of the individuals participating in the appointment", type="reference" )
  public static final String SP_ACTOR = "actor";
  @SearchParamDefinition(name="date", path="Appointment.start", description="Appointment date/time.", type="date" )
  public static final String SP_DATE = "date";

}

