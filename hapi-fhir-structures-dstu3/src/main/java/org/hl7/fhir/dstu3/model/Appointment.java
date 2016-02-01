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
 * A booking of a healthcare event among patient(s), practitioner(s), related person(s) and/or device(s) for a specific date/time. This may result in one or more Encounter(s).
 */
@ResourceDef(name="Appointment", profile="http://hl7.org/fhir/Profile/Appointment")
public class Appointment extends DomainResource {

    public enum AppointmentStatus {
        /**
         * None of the participant(s) have finalized their acceptance of the appointment request, and the start/end time may not be set yet.
         */
        PROPOSED, 
        /**
         * Some or all of the participant(s) have not finalized their acceptance of the appointment request.
         */
        PENDING, 
        /**
         * All participant(s) have been considered and the appointment is confirmed to go ahead at the date/times specified.
         */
        BOOKED, 
        /**
         * Some of the patients have arrived.
         */
        ARRIVED, 
        /**
         * This appointment has completed and may have resulted in an encounter.
         */
        FULFILLED, 
        /**
         * The appointment has been cancelled.
         */
        CANCELLED, 
        /**
         * Some or all of the participant(s) have not/did not appear for the appointment (usually the patient).
         */
        NOSHOW, 
        /**
         * added to help the parsers
         */
        NULL;
        public static AppointmentStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("proposed".equals(codeString))
          return PROPOSED;
        if ("pending".equals(codeString))
          return PENDING;
        if ("booked".equals(codeString))
          return BOOKED;
        if ("arrived".equals(codeString))
          return ARRIVED;
        if ("fulfilled".equals(codeString))
          return FULFILLED;
        if ("cancelled".equals(codeString))
          return CANCELLED;
        if ("noshow".equals(codeString))
          return NOSHOW;
        throw new FHIRException("Unknown AppointmentStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PROPOSED: return "proposed";
            case PENDING: return "pending";
            case BOOKED: return "booked";
            case ARRIVED: return "arrived";
            case FULFILLED: return "fulfilled";
            case CANCELLED: return "cancelled";
            case NOSHOW: return "noshow";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case PROPOSED: return "http://hl7.org/fhir/appointmentstatus";
            case PENDING: return "http://hl7.org/fhir/appointmentstatus";
            case BOOKED: return "http://hl7.org/fhir/appointmentstatus";
            case ARRIVED: return "http://hl7.org/fhir/appointmentstatus";
            case FULFILLED: return "http://hl7.org/fhir/appointmentstatus";
            case CANCELLED: return "http://hl7.org/fhir/appointmentstatus";
            case NOSHOW: return "http://hl7.org/fhir/appointmentstatus";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case PROPOSED: return "None of the participant(s) have finalized their acceptance of the appointment request, and the start/end time may not be set yet.";
            case PENDING: return "Some or all of the participant(s) have not finalized their acceptance of the appointment request.";
            case BOOKED: return "All participant(s) have been considered and the appointment is confirmed to go ahead at the date/times specified.";
            case ARRIVED: return "Some of the patients have arrived.";
            case FULFILLED: return "This appointment has completed and may have resulted in an encounter.";
            case CANCELLED: return "The appointment has been cancelled.";
            case NOSHOW: return "Some or all of the participant(s) have not/did not appear for the appointment (usually the patient).";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PROPOSED: return "Proposed";
            case PENDING: return "Pending";
            case BOOKED: return "Booked";
            case ARRIVED: return "Arrived";
            case FULFILLED: return "Fulfilled";
            case CANCELLED: return "Cancelled";
            case NOSHOW: return "No Show";
            default: return "?";
          }
        }
    }

  public static class AppointmentStatusEnumFactory implements EnumFactory<AppointmentStatus> {
    public AppointmentStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("proposed".equals(codeString))
          return AppointmentStatus.PROPOSED;
        if ("pending".equals(codeString))
          return AppointmentStatus.PENDING;
        if ("booked".equals(codeString))
          return AppointmentStatus.BOOKED;
        if ("arrived".equals(codeString))
          return AppointmentStatus.ARRIVED;
        if ("fulfilled".equals(codeString))
          return AppointmentStatus.FULFILLED;
        if ("cancelled".equals(codeString))
          return AppointmentStatus.CANCELLED;
        if ("noshow".equals(codeString))
          return AppointmentStatus.NOSHOW;
        throw new IllegalArgumentException("Unknown AppointmentStatus code '"+codeString+"'");
        }
        public Enumeration<AppointmentStatus> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("proposed".equals(codeString))
          return new Enumeration<AppointmentStatus>(this, AppointmentStatus.PROPOSED);
        if ("pending".equals(codeString))
          return new Enumeration<AppointmentStatus>(this, AppointmentStatus.PENDING);
        if ("booked".equals(codeString))
          return new Enumeration<AppointmentStatus>(this, AppointmentStatus.BOOKED);
        if ("arrived".equals(codeString))
          return new Enumeration<AppointmentStatus>(this, AppointmentStatus.ARRIVED);
        if ("fulfilled".equals(codeString))
          return new Enumeration<AppointmentStatus>(this, AppointmentStatus.FULFILLED);
        if ("cancelled".equals(codeString))
          return new Enumeration<AppointmentStatus>(this, AppointmentStatus.CANCELLED);
        if ("noshow".equals(codeString))
          return new Enumeration<AppointmentStatus>(this, AppointmentStatus.NOSHOW);
        throw new FHIRException("Unknown AppointmentStatus code '"+codeString+"'");
        }
    public String toCode(AppointmentStatus code) {
      if (code == AppointmentStatus.PROPOSED)
        return "proposed";
      if (code == AppointmentStatus.PENDING)
        return "pending";
      if (code == AppointmentStatus.BOOKED)
        return "booked";
      if (code == AppointmentStatus.ARRIVED)
        return "arrived";
      if (code == AppointmentStatus.FULFILLED)
        return "fulfilled";
      if (code == AppointmentStatus.CANCELLED)
        return "cancelled";
      if (code == AppointmentStatus.NOSHOW)
        return "noshow";
      return "?";
      }
    public String toSystem(AppointmentStatus code) {
      return code.getSystem();
      }
    }

    public enum ParticipantRequired {
        /**
         * The participant is required to attend the appointment.
         */
        REQUIRED, 
        /**
         * The participant may optionally attend the appointment.
         */
        OPTIONAL, 
        /**
         * The participant is excluded from the appointment, and may not be informed of the appointment taking place. (Appointment is about them, not for them - such as 2 doctors discussing results about a patient's test).
         */
        INFORMATIONONLY, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ParticipantRequired fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("required".equals(codeString))
          return REQUIRED;
        if ("optional".equals(codeString))
          return OPTIONAL;
        if ("information-only".equals(codeString))
          return INFORMATIONONLY;
        throw new FHIRException("Unknown ParticipantRequired code '"+codeString+"'");
        }
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
            case REQUIRED: return "http://hl7.org/fhir/participantrequired";
            case OPTIONAL: return "http://hl7.org/fhir/participantrequired";
            case INFORMATIONONLY: return "http://hl7.org/fhir/participantrequired";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case REQUIRED: return "The participant is required to attend the appointment.";
            case OPTIONAL: return "The participant may optionally attend the appointment.";
            case INFORMATIONONLY: return "The participant is excluded from the appointment, and may not be informed of the appointment taking place. (Appointment is about them, not for them - such as 2 doctors discussing results about a patient's test).";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case REQUIRED: return "Required";
            case OPTIONAL: return "Optional";
            case INFORMATIONONLY: return "Information Only";
            default: return "?";
          }
        }
    }

  public static class ParticipantRequiredEnumFactory implements EnumFactory<ParticipantRequired> {
    public ParticipantRequired fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("required".equals(codeString))
          return ParticipantRequired.REQUIRED;
        if ("optional".equals(codeString))
          return ParticipantRequired.OPTIONAL;
        if ("information-only".equals(codeString))
          return ParticipantRequired.INFORMATIONONLY;
        throw new IllegalArgumentException("Unknown ParticipantRequired code '"+codeString+"'");
        }
        public Enumeration<ParticipantRequired> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("required".equals(codeString))
          return new Enumeration<ParticipantRequired>(this, ParticipantRequired.REQUIRED);
        if ("optional".equals(codeString))
          return new Enumeration<ParticipantRequired>(this, ParticipantRequired.OPTIONAL);
        if ("information-only".equals(codeString))
          return new Enumeration<ParticipantRequired>(this, ParticipantRequired.INFORMATIONONLY);
        throw new FHIRException("Unknown ParticipantRequired code '"+codeString+"'");
        }
    public String toCode(ParticipantRequired code) {
      if (code == ParticipantRequired.REQUIRED)
        return "required";
      if (code == ParticipantRequired.OPTIONAL)
        return "optional";
      if (code == ParticipantRequired.INFORMATIONONLY)
        return "information-only";
      return "?";
      }
    public String toSystem(ParticipantRequired code) {
      return code.getSystem();
      }
    }

    public enum ParticipationStatus {
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
         * The participant needs to indicate if they accept the appointment by changing this status to one of the other statuses.
         */
        NEEDSACTION, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ParticipationStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("accepted".equals(codeString))
          return ACCEPTED;
        if ("declined".equals(codeString))
          return DECLINED;
        if ("tentative".equals(codeString))
          return TENTATIVE;
        if ("needs-action".equals(codeString))
          return NEEDSACTION;
        throw new FHIRException("Unknown ParticipationStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ACCEPTED: return "accepted";
            case DECLINED: return "declined";
            case TENTATIVE: return "tentative";
            case NEEDSACTION: return "needs-action";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case ACCEPTED: return "http://hl7.org/fhir/participationstatus";
            case DECLINED: return "http://hl7.org/fhir/participationstatus";
            case TENTATIVE: return "http://hl7.org/fhir/participationstatus";
            case NEEDSACTION: return "http://hl7.org/fhir/participationstatus";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case ACCEPTED: return "The participant has accepted the appointment.";
            case DECLINED: return "The participant has declined the appointment and will not participate in the appointment.";
            case TENTATIVE: return "The participant has  tentatively accepted the appointment. This could be automatically created by a system and requires further processing before it can be accepted. There is no commitment that attendance will occur.";
            case NEEDSACTION: return "The participant needs to indicate if they accept the appointment by changing this status to one of the other statuses.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ACCEPTED: return "Accepted";
            case DECLINED: return "Declined";
            case TENTATIVE: return "Tentative";
            case NEEDSACTION: return "Needs Action";
            default: return "?";
          }
        }
    }

  public static class ParticipationStatusEnumFactory implements EnumFactory<ParticipationStatus> {
    public ParticipationStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("accepted".equals(codeString))
          return ParticipationStatus.ACCEPTED;
        if ("declined".equals(codeString))
          return ParticipationStatus.DECLINED;
        if ("tentative".equals(codeString))
          return ParticipationStatus.TENTATIVE;
        if ("needs-action".equals(codeString))
          return ParticipationStatus.NEEDSACTION;
        throw new IllegalArgumentException("Unknown ParticipationStatus code '"+codeString+"'");
        }
        public Enumeration<ParticipationStatus> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("accepted".equals(codeString))
          return new Enumeration<ParticipationStatus>(this, ParticipationStatus.ACCEPTED);
        if ("declined".equals(codeString))
          return new Enumeration<ParticipationStatus>(this, ParticipationStatus.DECLINED);
        if ("tentative".equals(codeString))
          return new Enumeration<ParticipationStatus>(this, ParticipationStatus.TENTATIVE);
        if ("needs-action".equals(codeString))
          return new Enumeration<ParticipationStatus>(this, ParticipationStatus.NEEDSACTION);
        throw new FHIRException("Unknown ParticipationStatus code '"+codeString+"'");
        }
    public String toCode(ParticipationStatus code) {
      if (code == ParticipationStatus.ACCEPTED)
        return "accepted";
      if (code == ParticipationStatus.DECLINED)
        return "declined";
      if (code == ParticipationStatus.TENTATIVE)
        return "tentative";
      if (code == ParticipationStatus.NEEDSACTION)
        return "needs-action";
      return "?";
      }
    public String toSystem(ParticipationStatus code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class AppointmentParticipantComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Role of participant in the appointment.
         */
        @Child(name = "type", type = {CodeableConcept.class}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Role of participant in the appointment", formalDefinition="Role of participant in the appointment." )
        protected List<CodeableConcept> type;

        /**
         * A Person, Location/HealthcareService or Device that is participating in the appointment.
         */
        @Child(name = "actor", type = {Patient.class, Practitioner.class, RelatedPerson.class, Device.class, HealthcareService.class, Location.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Person, Location/HealthcareService or Device", formalDefinition="A Person, Location/HealthcareService or Device that is participating in the appointment." )
        protected Reference actor;

        /**
         * The actual object that is the target of the reference (A Person, Location/HealthcareService or Device that is participating in the appointment.)
         */
        protected Resource actorTarget;

        /**
         * Is this participant required to be present at the meeting. This covers a use-case where 2 doctors need to meet to discuss the results for a specific patient, and the patient is not required to be present.
         */
        @Child(name = "required", type = {CodeType.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="required | optional | information-only", formalDefinition="Is this participant required to be present at the meeting. This covers a use-case where 2 doctors need to meet to discuss the results for a specific patient, and the patient is not required to be present." )
        protected Enumeration<ParticipantRequired> required;

        /**
         * Participation status of the Patient.
         */
        @Child(name = "status", type = {CodeType.class}, order=4, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="accepted | declined | tentative | needs-action", formalDefinition="Participation status of the Patient." )
        protected Enumeration<ParticipationStatus> status;

        private static final long serialVersionUID = -1620552507L;

    /**
     * Constructor
     */
      public AppointmentParticipantComponent() {
        super();
      }

    /**
     * Constructor
     */
      public AppointmentParticipantComponent(Enumeration<ParticipationStatus> status) {
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

    // syntactic sugar
        public AppointmentParticipantComponent addType(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.type == null)
            this.type = new ArrayList<CodeableConcept>();
          this.type.add(t);
          return this;
        }

        /**
         * @return {@link #actor} (A Person, Location/HealthcareService or Device that is participating in the appointment.)
         */
        public Reference getActor() { 
          if (this.actor == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AppointmentParticipantComponent.actor");
            else if (Configuration.doAutoCreate())
              this.actor = new Reference(); // cc
          return this.actor;
        }

        public boolean hasActor() { 
          return this.actor != null && !this.actor.isEmpty();
        }

        /**
         * @param value {@link #actor} (A Person, Location/HealthcareService or Device that is participating in the appointment.)
         */
        public AppointmentParticipantComponent setActor(Reference value) { 
          this.actor = value;
          return this;
        }

        /**
         * @return {@link #actor} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (A Person, Location/HealthcareService or Device that is participating in the appointment.)
         */
        public Resource getActorTarget() { 
          return this.actorTarget;
        }

        /**
         * @param value {@link #actor} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (A Person, Location/HealthcareService or Device that is participating in the appointment.)
         */
        public AppointmentParticipantComponent setActorTarget(Resource value) { 
          this.actorTarget = value;
          return this;
        }

        /**
         * @return {@link #required} (Is this participant required to be present at the meeting. This covers a use-case where 2 doctors need to meet to discuss the results for a specific patient, and the patient is not required to be present.). This is the underlying object with id, value and extensions. The accessor "getRequired" gives direct access to the value
         */
        public Enumeration<ParticipantRequired> getRequiredElement() { 
          if (this.required == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AppointmentParticipantComponent.required");
            else if (Configuration.doAutoCreate())
              this.required = new Enumeration<ParticipantRequired>(new ParticipantRequiredEnumFactory()); // bb
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
        public AppointmentParticipantComponent setRequiredElement(Enumeration<ParticipantRequired> value) { 
          this.required = value;
          return this;
        }

        /**
         * @return Is this participant required to be present at the meeting. This covers a use-case where 2 doctors need to meet to discuss the results for a specific patient, and the patient is not required to be present.
         */
        public ParticipantRequired getRequired() { 
          return this.required == null ? null : this.required.getValue();
        }

        /**
         * @param value Is this participant required to be present at the meeting. This covers a use-case where 2 doctors need to meet to discuss the results for a specific patient, and the patient is not required to be present.
         */
        public AppointmentParticipantComponent setRequired(ParticipantRequired value) { 
          if (value == null)
            this.required = null;
          else {
            if (this.required == null)
              this.required = new Enumeration<ParticipantRequired>(new ParticipantRequiredEnumFactory());
            this.required.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #status} (Participation status of the Patient.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
         */
        public Enumeration<ParticipationStatus> getStatusElement() { 
          if (this.status == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AppointmentParticipantComponent.status");
            else if (Configuration.doAutoCreate())
              this.status = new Enumeration<ParticipationStatus>(new ParticipationStatusEnumFactory()); // bb
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
        public AppointmentParticipantComponent setStatusElement(Enumeration<ParticipationStatus> value) { 
          this.status = value;
          return this;
        }

        /**
         * @return Participation status of the Patient.
         */
        public ParticipationStatus getStatus() { 
          return this.status == null ? null : this.status.getValue();
        }

        /**
         * @param value Participation status of the Patient.
         */
        public AppointmentParticipantComponent setStatus(ParticipationStatus value) { 
            if (this.status == null)
              this.status = new Enumeration<ParticipationStatus>(new ParticipationStatusEnumFactory());
            this.status.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("type", "CodeableConcept", "Role of participant in the appointment.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("actor", "Reference(Patient|Practitioner|RelatedPerson|Device|HealthcareService|Location)", "A Person, Location/HealthcareService or Device that is participating in the appointment.", 0, java.lang.Integer.MAX_VALUE, actor));
          childrenList.add(new Property("required", "code", "Is this participant required to be present at the meeting. This covers a use-case where 2 doctors need to meet to discuss the results for a specific patient, and the patient is not required to be present.", 0, java.lang.Integer.MAX_VALUE, required));
          childrenList.add(new Property("status", "code", "Participation status of the Patient.", 0, java.lang.Integer.MAX_VALUE, status));
        }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type"))
          this.getType().add(castToCodeableConcept(value));
        else if (name.equals("actor"))
          this.actor = castToReference(value); // Reference
        else if (name.equals("required"))
          this.required = new ParticipantRequiredEnumFactory().fromType(value); // Enumeration<ParticipantRequired>
        else if (name.equals("status"))
          this.status = new ParticipationStatusEnumFactory().fromType(value); // Enumeration<ParticipationStatus>
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          return addType();
        }
        else if (name.equals("actor")) {
          this.actor = new Reference();
          return this.actor;
        }
        else if (name.equals("required")) {
          throw new FHIRException("Cannot call addChild on a primitive type Appointment.required");
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type Appointment.status");
        }
        else
          return super.addChild(name);
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

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof AppointmentParticipantComponent))
          return false;
        AppointmentParticipantComponent o = (AppointmentParticipantComponent) other;
        return compareDeep(type, o.type, true) && compareDeep(actor, o.actor, true) && compareDeep(required, o.required, true)
           && compareDeep(status, o.status, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof AppointmentParticipantComponent))
          return false;
        AppointmentParticipantComponent o = (AppointmentParticipantComponent) other;
        return compareValues(required, o.required, true) && compareValues(status, o.status, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (type == null || type.isEmpty()) && (actor == null || actor.isEmpty())
           && (required == null || required.isEmpty()) && (status == null || status.isEmpty());
      }

  public String fhirType() {
    return "Appointment.participant";

  }

  }

    /**
     * This records identifiers associated with this appointment concern that are defined by business processes and/or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation).
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="External Ids for this item", formalDefinition="This records identifiers associated with this appointment concern that are defined by business processes and/or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)." )
    protected List<Identifier> identifier;

    /**
     * The overall status of the Appointment. Each of the participants has their own participation status which indicates their involvement in the process, however this status indicates the shared status.
     */
    @Child(name = "status", type = {CodeType.class}, order=1, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="proposed | pending | booked | arrived | fulfilled | cancelled | noshow", formalDefinition="The overall status of the Appointment. Each of the participants has their own participation status which indicates their involvement in the process, however this status indicates the shared status." )
    protected Enumeration<AppointmentStatus> status;

    /**
     * The type of appointment that is being booked (This may also be associated with participants for location, and/or a HealthcareService).
     */
    @Child(name = "type", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The type of appointment that is being booked", formalDefinition="The type of appointment that is being booked (This may also be associated with participants for location, and/or a HealthcareService)." )
    protected CodeableConcept type;

    /**
     * The reason that this appointment is being scheduled. This is more clinical than administrative.
     */
    @Child(name = "reason", type = {CodeableConcept.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Reason this appointment is scheduled", formalDefinition="The reason that this appointment is being scheduled. This is more clinical than administrative." )
    protected CodeableConcept reason;

    /**
     * The priority of the appointment. Can be used to make informed decisions if needing to re-prioritize appointments. (The iCal Standard specifies 0 as undefined, 1 as highest, 9 as lowest priority).
     */
    @Child(name = "priority", type = {UnsignedIntType.class}, order=4, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Used to make informed decisions if needing to re-prioritize", formalDefinition="The priority of the appointment. Can be used to make informed decisions if needing to re-prioritize appointments. (The iCal Standard specifies 0 as undefined, 1 as highest, 9 as lowest priority)." )
    protected UnsignedIntType priority;

    /**
     * The brief description of the appointment as would be shown on a subject line in a meeting request, or appointment list. Detailed or expanded information should be put in the comment field.
     */
    @Child(name = "description", type = {StringType.class}, order=5, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Shown on a subject line in a meeting request, or appointment list", formalDefinition="The brief description of the appointment as would be shown on a subject line in a meeting request, or appointment list. Detailed or expanded information should be put in the comment field." )
    protected StringType description;

    /**
     * Date/Time that the appointment is to take place.
     */
    @Child(name = "start", type = {InstantType.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When appointment is to take place", formalDefinition="Date/Time that the appointment is to take place." )
    protected InstantType start;

    /**
     * Date/Time that the appointment is to conclude.
     */
    @Child(name = "end", type = {InstantType.class}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When appointment is to conclude", formalDefinition="Date/Time that the appointment is to conclude." )
    protected InstantType end;

    /**
     * Number of minutes that the appointment is to take. This can be less than the duration between the start and end times (where actual time of appointment is only an estimate or is a planned appointment request).
     */
    @Child(name = "minutesDuration", type = {PositiveIntType.class}, order=8, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Can be less than start/end (e.g. estimate)", formalDefinition="Number of minutes that the appointment is to take. This can be less than the duration between the start and end times (where actual time of appointment is only an estimate or is a planned appointment request)." )
    protected PositiveIntType minutesDuration;

    /**
     * The slot that this appointment is filling. If provided then the schedule will not be provided as slots are not recursive, and the start/end values MUST be the same as from the slot.
     */
    @Child(name = "slot", type = {Slot.class}, order=9, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="If provided, then no schedule and start/end values MUST match slot", formalDefinition="The slot that this appointment is filling. If provided then the schedule will not be provided as slots are not recursive, and the start/end values MUST be the same as from the slot." )
    protected List<Reference> slot;
    /**
     * The actual objects that are the target of the reference (The slot that this appointment is filling. If provided then the schedule will not be provided as slots are not recursive, and the start/end values MUST be the same as from the slot.)
     */
    protected List<Slot> slotTarget;


    /**
     * Additional comments about the appointment.
     */
    @Child(name = "comment", type = {StringType.class}, order=10, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Additional comments", formalDefinition="Additional comments about the appointment." )
    protected StringType comment;

    /**
     * List of participants involved in the appointment.
     */
    @Child(name = "participant", type = {}, order=11, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Participants involved in appointment", formalDefinition="List of participants involved in the appointment." )
    protected List<AppointmentParticipantComponent> participant;

    private static final long serialVersionUID = -1403944125L;

  /**
   * Constructor
   */
    public Appointment() {
      super();
    }

  /**
   * Constructor
   */
    public Appointment(Enumeration<AppointmentStatus> status) {
      super();
      this.status = status;
    }

    /**
     * @return {@link #identifier} (This records identifiers associated with this appointment concern that are defined by business processes and/or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation).)
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
     * @return {@link #identifier} (This records identifiers associated with this appointment concern that are defined by business processes and/or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation).)
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
    public Appointment addIdentifier(Identifier t) { //3
      if (t == null)
        return this;
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return this;
    }

    /**
     * @return {@link #status} (The overall status of the Appointment. Each of the participants has their own participation status which indicates their involvement in the process, however this status indicates the shared status.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<AppointmentStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Appointment.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<AppointmentStatus>(new AppointmentStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (The overall status of the Appointment. Each of the participants has their own participation status which indicates their involvement in the process, however this status indicates the shared status.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Appointment setStatusElement(Enumeration<AppointmentStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The overall status of the Appointment. Each of the participants has their own participation status which indicates their involvement in the process, however this status indicates the shared status.
     */
    public AppointmentStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The overall status of the Appointment. Each of the participants has their own participation status which indicates their involvement in the process, however this status indicates the shared status.
     */
    public Appointment setStatus(AppointmentStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<AppointmentStatus>(new AppointmentStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #type} (The type of appointment that is being booked (This may also be associated with participants for location, and/or a HealthcareService).)
     */
    public CodeableConcept getType() { 
      if (this.type == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Appointment.type");
        else if (Configuration.doAutoCreate())
          this.type = new CodeableConcept(); // cc
      return this.type;
    }

    public boolean hasType() { 
      return this.type != null && !this.type.isEmpty();
    }

    /**
     * @param value {@link #type} (The type of appointment that is being booked (This may also be associated with participants for location, and/or a HealthcareService).)
     */
    public Appointment setType(CodeableConcept value) { 
      this.type = value;
      return this;
    }

    /**
     * @return {@link #reason} (The reason that this appointment is being scheduled. This is more clinical than administrative.)
     */
    public CodeableConcept getReason() { 
      if (this.reason == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Appointment.reason");
        else if (Configuration.doAutoCreate())
          this.reason = new CodeableConcept(); // cc
      return this.reason;
    }

    public boolean hasReason() { 
      return this.reason != null && !this.reason.isEmpty();
    }

    /**
     * @param value {@link #reason} (The reason that this appointment is being scheduled. This is more clinical than administrative.)
     */
    public Appointment setReason(CodeableConcept value) { 
      this.reason = value;
      return this;
    }

    /**
     * @return {@link #priority} (The priority of the appointment. Can be used to make informed decisions if needing to re-prioritize appointments. (The iCal Standard specifies 0 as undefined, 1 as highest, 9 as lowest priority).). This is the underlying object with id, value and extensions. The accessor "getPriority" gives direct access to the value
     */
    public UnsignedIntType getPriorityElement() { 
      if (this.priority == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Appointment.priority");
        else if (Configuration.doAutoCreate())
          this.priority = new UnsignedIntType(); // bb
      return this.priority;
    }

    public boolean hasPriorityElement() { 
      return this.priority != null && !this.priority.isEmpty();
    }

    public boolean hasPriority() { 
      return this.priority != null && !this.priority.isEmpty();
    }

    /**
     * @param value {@link #priority} (The priority of the appointment. Can be used to make informed decisions if needing to re-prioritize appointments. (The iCal Standard specifies 0 as undefined, 1 as highest, 9 as lowest priority).). This is the underlying object with id, value and extensions. The accessor "getPriority" gives direct access to the value
     */
    public Appointment setPriorityElement(UnsignedIntType value) { 
      this.priority = value;
      return this;
    }

    /**
     * @return The priority of the appointment. Can be used to make informed decisions if needing to re-prioritize appointments. (The iCal Standard specifies 0 as undefined, 1 as highest, 9 as lowest priority).
     */
    public int getPriority() { 
      return this.priority == null || this.priority.isEmpty() ? 0 : this.priority.getValue();
    }

    /**
     * @param value The priority of the appointment. Can be used to make informed decisions if needing to re-prioritize appointments. (The iCal Standard specifies 0 as undefined, 1 as highest, 9 as lowest priority).
     */
    public Appointment setPriority(int value) { 
        if (this.priority == null)
          this.priority = new UnsignedIntType();
        this.priority.setValue(value);
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
          this.start = new InstantType(); // bb
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
      if (value == null)
        this.start = null;
      else {
        if (this.start == null)
          this.start = new InstantType();
        this.start.setValue(value);
      }
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
          this.end = new InstantType(); // bb
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
      if (value == null)
        this.end = null;
      else {
        if (this.end == null)
          this.end = new InstantType();
        this.end.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #minutesDuration} (Number of minutes that the appointment is to take. This can be less than the duration between the start and end times (where actual time of appointment is only an estimate or is a planned appointment request).). This is the underlying object with id, value and extensions. The accessor "getMinutesDuration" gives direct access to the value
     */
    public PositiveIntType getMinutesDurationElement() { 
      if (this.minutesDuration == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Appointment.minutesDuration");
        else if (Configuration.doAutoCreate())
          this.minutesDuration = new PositiveIntType(); // bb
      return this.minutesDuration;
    }

    public boolean hasMinutesDurationElement() { 
      return this.minutesDuration != null && !this.minutesDuration.isEmpty();
    }

    public boolean hasMinutesDuration() { 
      return this.minutesDuration != null && !this.minutesDuration.isEmpty();
    }

    /**
     * @param value {@link #minutesDuration} (Number of minutes that the appointment is to take. This can be less than the duration between the start and end times (where actual time of appointment is only an estimate or is a planned appointment request).). This is the underlying object with id, value and extensions. The accessor "getMinutesDuration" gives direct access to the value
     */
    public Appointment setMinutesDurationElement(PositiveIntType value) { 
      this.minutesDuration = value;
      return this;
    }

    /**
     * @return Number of minutes that the appointment is to take. This can be less than the duration between the start and end times (where actual time of appointment is only an estimate or is a planned appointment request).
     */
    public int getMinutesDuration() { 
      return this.minutesDuration == null || this.minutesDuration.isEmpty() ? 0 : this.minutesDuration.getValue();
    }

    /**
     * @param value Number of minutes that the appointment is to take. This can be less than the duration between the start and end times (where actual time of appointment is only an estimate or is a planned appointment request).
     */
    public Appointment setMinutesDuration(int value) { 
        if (this.minutesDuration == null)
          this.minutesDuration = new PositiveIntType();
        this.minutesDuration.setValue(value);
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

    // syntactic sugar
    public Appointment addSlot(Reference t) { //3
      if (t == null)
        return this;
      if (this.slot == null)
        this.slot = new ArrayList<Reference>();
      this.slot.add(t);
      return this;
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
     * @return {@link #comment} (Additional comments about the appointment.). This is the underlying object with id, value and extensions. The accessor "getComment" gives direct access to the value
     */
    public StringType getCommentElement() { 
      if (this.comment == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Appointment.comment");
        else if (Configuration.doAutoCreate())
          this.comment = new StringType(); // bb
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

    // syntactic sugar
    public Appointment addParticipant(AppointmentParticipantComponent t) { //3
      if (t == null)
        return this;
      if (this.participant == null)
        this.participant = new ArrayList<AppointmentParticipantComponent>();
      this.participant.add(t);
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "This records identifiers associated with this appointment concern that are defined by business processes and/or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation).", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("status", "code", "The overall status of the Appointment. Each of the participants has their own participation status which indicates their involvement in the process, however this status indicates the shared status.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("type", "CodeableConcept", "The type of appointment that is being booked (This may also be associated with participants for location, and/or a HealthcareService).", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("reason", "CodeableConcept", "The reason that this appointment is being scheduled. This is more clinical than administrative.", 0, java.lang.Integer.MAX_VALUE, reason));
        childrenList.add(new Property("priority", "unsignedInt", "The priority of the appointment. Can be used to make informed decisions if needing to re-prioritize appointments. (The iCal Standard specifies 0 as undefined, 1 as highest, 9 as lowest priority).", 0, java.lang.Integer.MAX_VALUE, priority));
        childrenList.add(new Property("description", "string", "The brief description of the appointment as would be shown on a subject line in a meeting request, or appointment list. Detailed or expanded information should be put in the comment field.", 0, java.lang.Integer.MAX_VALUE, description));
        childrenList.add(new Property("start", "instant", "Date/Time that the appointment is to take place.", 0, java.lang.Integer.MAX_VALUE, start));
        childrenList.add(new Property("end", "instant", "Date/Time that the appointment is to conclude.", 0, java.lang.Integer.MAX_VALUE, end));
        childrenList.add(new Property("minutesDuration", "positiveInt", "Number of minutes that the appointment is to take. This can be less than the duration between the start and end times (where actual time of appointment is only an estimate or is a planned appointment request).", 0, java.lang.Integer.MAX_VALUE, minutesDuration));
        childrenList.add(new Property("slot", "Reference(Slot)", "The slot that this appointment is filling. If provided then the schedule will not be provided as slots are not recursive, and the start/end values MUST be the same as from the slot.", 0, java.lang.Integer.MAX_VALUE, slot));
        childrenList.add(new Property("comment", "string", "Additional comments about the appointment.", 0, java.lang.Integer.MAX_VALUE, comment));
        childrenList.add(new Property("participant", "", "List of participants involved in the appointment.", 0, java.lang.Integer.MAX_VALUE, participant));
      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier"))
          this.getIdentifier().add(castToIdentifier(value));
        else if (name.equals("status"))
          this.status = new AppointmentStatusEnumFactory().fromType(value); // Enumeration<AppointmentStatus>
        else if (name.equals("type"))
          this.type = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("reason"))
          this.reason = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("priority"))
          this.priority = castToUnsignedInt(value); // UnsignedIntType
        else if (name.equals("description"))
          this.description = castToString(value); // StringType
        else if (name.equals("start"))
          this.start = castToInstant(value); // InstantType
        else if (name.equals("end"))
          this.end = castToInstant(value); // InstantType
        else if (name.equals("minutesDuration"))
          this.minutesDuration = castToPositiveInt(value); // PositiveIntType
        else if (name.equals("slot"))
          this.getSlot().add(castToReference(value));
        else if (name.equals("comment"))
          this.comment = castToString(value); // StringType
        else if (name.equals("participant"))
          this.getParticipant().add((AppointmentParticipantComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type Appointment.status");
        }
        else if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("reason")) {
          this.reason = new CodeableConcept();
          return this.reason;
        }
        else if (name.equals("priority")) {
          throw new FHIRException("Cannot call addChild on a primitive type Appointment.priority");
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type Appointment.description");
        }
        else if (name.equals("start")) {
          throw new FHIRException("Cannot call addChild on a primitive type Appointment.start");
        }
        else if (name.equals("end")) {
          throw new FHIRException("Cannot call addChild on a primitive type Appointment.end");
        }
        else if (name.equals("minutesDuration")) {
          throw new FHIRException("Cannot call addChild on a primitive type Appointment.minutesDuration");
        }
        else if (name.equals("slot")) {
          return addSlot();
        }
        else if (name.equals("comment")) {
          throw new FHIRException("Cannot call addChild on a primitive type Appointment.comment");
        }
        else if (name.equals("participant")) {
          return addParticipant();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "Appointment";

  }

      public Appointment copy() {
        Appointment dst = new Appointment();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.status = status == null ? null : status.copy();
        dst.type = type == null ? null : type.copy();
        dst.reason = reason == null ? null : reason.copy();
        dst.priority = priority == null ? null : priority.copy();
        dst.description = description == null ? null : description.copy();
        dst.start = start == null ? null : start.copy();
        dst.end = end == null ? null : end.copy();
        dst.minutesDuration = minutesDuration == null ? null : minutesDuration.copy();
        if (slot != null) {
          dst.slot = new ArrayList<Reference>();
          for (Reference i : slot)
            dst.slot.add(i.copy());
        };
        dst.comment = comment == null ? null : comment.copy();
        if (participant != null) {
          dst.participant = new ArrayList<AppointmentParticipantComponent>();
          for (AppointmentParticipantComponent i : participant)
            dst.participant.add(i.copy());
        };
        return dst;
      }

      protected Appointment typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof Appointment))
          return false;
        Appointment o = (Appointment) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(status, o.status, true) && compareDeep(type, o.type, true)
           && compareDeep(reason, o.reason, true) && compareDeep(priority, o.priority, true) && compareDeep(description, o.description, true)
           && compareDeep(start, o.start, true) && compareDeep(end, o.end, true) && compareDeep(minutesDuration, o.minutesDuration, true)
           && compareDeep(slot, o.slot, true) && compareDeep(comment, o.comment, true) && compareDeep(participant, o.participant, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Appointment))
          return false;
        Appointment o = (Appointment) other;
        return compareValues(status, o.status, true) && compareValues(priority, o.priority, true) && compareValues(description, o.description, true)
           && compareValues(start, o.start, true) && compareValues(end, o.end, true) && compareValues(minutesDuration, o.minutesDuration, true)
           && compareValues(comment, o.comment, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (status == null || status.isEmpty())
           && (type == null || type.isEmpty()) && (reason == null || reason.isEmpty()) && (priority == null || priority.isEmpty())
           && (description == null || description.isEmpty()) && (start == null || start.isEmpty()) && (end == null || end.isEmpty())
           && (minutesDuration == null || minutesDuration.isEmpty()) && (slot == null || slot.isEmpty())
           && (comment == null || comment.isEmpty()) && (participant == null || participant.isEmpty())
          ;
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Appointment;
   }

 /**
   * Search parameter: <b>date</b>
   * <p>
   * Description: <b>Appointment date/time.</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Appointment.start</b><br>
   * </p>
   */
  @SearchParamDefinition(name="date", path="Appointment.start", description="Appointment date/time.", type="date" )
  public static final String SP_DATE = "date";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>date</b>
   * <p>
   * Description: <b>Appointment date/time.</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Appointment.start</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam DATE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_DATE);

 /**
   * Search parameter: <b>actor</b>
   * <p>
   * Description: <b>Any one of the individuals participating in the appointment</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Appointment.participant.actor</b><br>
   * </p>
   */
  @SearchParamDefinition(name="actor", path="Appointment.participant.actor", description="Any one of the individuals participating in the appointment", type="reference" )
  public static final String SP_ACTOR = "actor";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>actor</b>
   * <p>
   * Description: <b>Any one of the individuals participating in the appointment</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Appointment.participant.actor</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam ACTOR = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_ACTOR);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Appointment:actor</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_ACTOR = new ca.uhn.fhir.model.api.Include("Appointment:actor").toLocked();

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>An Identifier of the Appointment</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Appointment.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="Appointment.identifier", description="An Identifier of the Appointment", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>An Identifier of the Appointment</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Appointment.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>practitioner</b>
   * <p>
   * Description: <b>One of the individuals of the appointment is this practitioner</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Appointment.participant.actor</b><br>
   * </p>
   */
  @SearchParamDefinition(name="practitioner", path="Appointment.participant.actor", description="One of the individuals of the appointment is this practitioner", type="reference" )
  public static final String SP_PRACTITIONER = "practitioner";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>practitioner</b>
   * <p>
   * Description: <b>One of the individuals of the appointment is this practitioner</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Appointment.participant.actor</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PRACTITIONER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PRACTITIONER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Appointment:practitioner</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PRACTITIONER = new ca.uhn.fhir.model.api.Include("Appointment:practitioner").toLocked();

 /**
   * Search parameter: <b>part-status</b>
   * <p>
   * Description: <b>The Participation status of the subject, or other participant on the appointment. Can be used to locate participants that have not responded to meeting requests.</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Appointment.participant.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="part-status", path="Appointment.participant.status", description="The Participation status of the subject, or other participant on the appointment. Can be used to locate participants that have not responded to meeting requests.", type="token" )
  public static final String SP_PART_STATUS = "part-status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>part-status</b>
   * <p>
   * Description: <b>The Participation status of the subject, or other participant on the appointment. Can be used to locate participants that have not responded to meeting requests.</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Appointment.participant.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam PART_STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_PART_STATUS);

 /**
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>One of the individuals of the appointment is this patient</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Appointment.participant.actor</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="Appointment.participant.actor", description="One of the individuals of the appointment is this patient", type="reference" )
  public static final String SP_PATIENT = "patient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patient</b>
   * <p>
   * Description: <b>One of the individuals of the appointment is this patient</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Appointment.participant.actor</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PATIENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PATIENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Appointment:patient</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PATIENT = new ca.uhn.fhir.model.api.Include("Appointment:patient").toLocked();

 /**
   * Search parameter: <b>location</b>
   * <p>
   * Description: <b>This location is listed in the participants of the appointment</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Appointment.participant.actor</b><br>
   * </p>
   */
  @SearchParamDefinition(name="location", path="Appointment.participant.actor", description="This location is listed in the participants of the appointment", type="reference" )
  public static final String SP_LOCATION = "location";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>location</b>
   * <p>
   * Description: <b>This location is listed in the participants of the appointment</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Appointment.participant.actor</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam LOCATION = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_LOCATION);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Appointment:location</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_LOCATION = new ca.uhn.fhir.model.api.Include("Appointment:location").toLocked();

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>The overall status of the appointment</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Appointment.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="Appointment.status", description="The overall status of the appointment", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>The overall status of the appointment</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Appointment.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);


}

