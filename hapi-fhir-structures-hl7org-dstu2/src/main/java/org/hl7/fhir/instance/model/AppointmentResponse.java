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
import org.hl7.fhir.instance.model.annotations.ResourceDef;
import org.hl7.fhir.instance.model.annotations.SearchParamDefinition;
import org.hl7.fhir.instance.model.annotations.Child;
import org.hl7.fhir.instance.model.annotations.Description;
import org.hl7.fhir.instance.model.annotations.Block;
import org.hl7.fhir.instance.model.api.*;
/**
 * A reply to an appointment request for a patient and/or practitioner(s), such as a confirmation or rejection.
 */
@ResourceDef(name="AppointmentResponse", profile="http://hl7.org/fhir/Profile/AppointmentResponse")
public class AppointmentResponse extends DomainResource {

    public enum ParticipantStatus {
        /**
         * The appointment participant has accepted that they can attend the appointment at the time specified in the AppointmentResponse
         */
        ACCEPTED, 
        /**
         * The appointment participant has declined the appointment
         */
        DECLINED, 
        /**
         * The appointment participant has tentatively accepted the appointment
         */
        TENTATIVE, 
        /**
         * The participant has in-process the appointment
         */
        INPROCESS, 
        /**
         * The participant has completed the appointment
         */
        COMPLETED, 
        /**
         * This is the intitial status of an appointment participant until a participant has replied. It implies that there is no commitment for the appointment
         */
        NEEDSACTION, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ParticipantStatus fromCode(String codeString) throws Exception {
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
        throw new Exception("Unknown ParticipantStatus code '"+codeString+"'");
        }
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
            case ACCEPTED: return "http://hl7.org/fhir/participantstatus";
            case DECLINED: return "http://hl7.org/fhir/participantstatus";
            case TENTATIVE: return "http://hl7.org/fhir/participantstatus";
            case INPROCESS: return "http://hl7.org/fhir/participantstatus";
            case COMPLETED: return "http://hl7.org/fhir/participantstatus";
            case NEEDSACTION: return "http://hl7.org/fhir/participantstatus";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case ACCEPTED: return "The appointment participant has accepted that they can attend the appointment at the time specified in the AppointmentResponse";
            case DECLINED: return "The appointment participant has declined the appointment";
            case TENTATIVE: return "The appointment participant has tentatively accepted the appointment";
            case INPROCESS: return "The participant has in-process the appointment";
            case COMPLETED: return "The participant has completed the appointment";
            case NEEDSACTION: return "This is the intitial status of an appointment participant until a participant has replied. It implies that there is no commitment for the appointment";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ACCEPTED: return "Accepted";
            case DECLINED: return "Declined";
            case TENTATIVE: return "Tentative";
            case INPROCESS: return "In Process";
            case COMPLETED: return "Completed";
            case NEEDSACTION: return "Needs Action";
            default: return "?";
          }
        }
    }

  public static class ParticipantStatusEnumFactory implements EnumFactory<ParticipantStatus> {
    public ParticipantStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("accepted".equals(codeString))
          return ParticipantStatus.ACCEPTED;
        if ("declined".equals(codeString))
          return ParticipantStatus.DECLINED;
        if ("tentative".equals(codeString))
          return ParticipantStatus.TENTATIVE;
        if ("in-process".equals(codeString))
          return ParticipantStatus.INPROCESS;
        if ("completed".equals(codeString))
          return ParticipantStatus.COMPLETED;
        if ("needs-action".equals(codeString))
          return ParticipantStatus.NEEDSACTION;
        throw new IllegalArgumentException("Unknown ParticipantStatus code '"+codeString+"'");
        }
    public String toCode(ParticipantStatus code) {
      if (code == ParticipantStatus.ACCEPTED)
        return "accepted";
      if (code == ParticipantStatus.DECLINED)
        return "declined";
      if (code == ParticipantStatus.TENTATIVE)
        return "tentative";
      if (code == ParticipantStatus.INPROCESS)
        return "in-process";
      if (code == ParticipantStatus.COMPLETED)
        return "completed";
      if (code == ParticipantStatus.NEEDSACTION)
        return "needs-action";
      return "?";
      }
    }

    /**
     * This records identifiers associated with this appointment response concern that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="External Ids for this item", formalDefinition="This records identifiers associated with this appointment response concern that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate." )
    protected List<Identifier> identifier;

    /**
     * Parent appointment that this response is replying to.
     */
    @Child(name = "appointment", type = {Appointment.class}, order=1, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Parent appointment that this response is replying to", formalDefinition="Parent appointment that this response is replying to." )
    protected Reference appointment;

    /**
     * The actual object that is the target of the reference (Parent appointment that this response is replying to.)
     */
    protected Appointment appointmentTarget;

    /**
     * Role of participant in the appointment.
     */
    @Child(name = "participantType", type = {CodeableConcept.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Role of participant in the appointment", formalDefinition="Role of participant in the appointment." )
    protected List<CodeableConcept> participantType;

    /**
     * A Person, Location/HealthcareService or Device that is participating in the appointment.
     */
    @Child(name = "actor", type = {Patient.class, Practitioner.class, RelatedPerson.class, Device.class, HealthcareService.class, Location.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="A Person, Location/HealthcareService or Device that is participating in the appointment", formalDefinition="A Person, Location/HealthcareService or Device that is participating in the appointment." )
    protected Reference actor;

    /**
     * The actual object that is the target of the reference (A Person, Location/HealthcareService or Device that is participating in the appointment.)
     */
    protected Resource actorTarget;

    /**
     * Participation status of the Participant. When the status is declined or tentative if the start/end times are different to the appointment, then these times should be interpreted as a requested time change. When the status is accepted, the times can either be the time of the appointment (as a confirmation of the time) or can be empty.
     */
    @Child(name = "participantStatus", type = {CodeType.class}, order=4, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="accepted | declined | tentative | in-process | completed | needs-action", formalDefinition="Participation status of the Participant. When the status is declined or tentative if the start/end times are different to the appointment, then these times should be interpreted as a requested time change. When the status is accepted, the times can either be the time of the appointment (as a confirmation of the time) or can be empty." )
    protected Enumeration<ParticipantStatus> participantStatus;

    /**
     * This comment is particularly important when the responder is declining, tentative or requesting another time to indicate the reasons why.
     */
    @Child(name = "comment", type = {StringType.class}, order=5, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Additional comments about the appointment", formalDefinition="This comment is particularly important when the responder is declining, tentative or requesting another time to indicate the reasons why." )
    protected StringType comment;

    /**
     * This may be either the same as the appointment request to confirm the details of the appointment, or alternately a new time to request a re-negotiation of the start time.
     */
    @Child(name = "start", type = {InstantType.class}, order=6, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Date/Time that the appointment is to take place, or requested new start time", formalDefinition="This may be either the same as the appointment request to confirm the details of the appointment, or alternately a new time to request a re-negotiation of the start time." )
    protected InstantType start;

    /**
     * This may be either the same as the appointment request to confirm the details of the appointment, or alternately a new time to request a re-negotiation of the end time.
     */
    @Child(name = "end", type = {InstantType.class}, order=7, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Date/Time that the appointment is to conclude, or requested new end time", formalDefinition="This may be either the same as the appointment request to confirm the details of the appointment, or alternately a new time to request a re-negotiation of the end time." )
    protected InstantType end;

    private static final long serialVersionUID = -1858624259L;

  /*
   * Constructor
   */
    public AppointmentResponse() {
      super();
    }

  /*
   * Constructor
   */
    public AppointmentResponse(Reference appointment, Enumeration<ParticipantStatus> participantStatus) {
      super();
      this.appointment = appointment;
      this.participantStatus = participantStatus;
    }

    /**
     * @return {@link #identifier} (This records identifiers associated with this appointment response concern that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate.)
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
     * @return {@link #identifier} (This records identifiers associated with this appointment response concern that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate.)
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
    public AppointmentResponse addIdentifier(Identifier t) { //3
      if (t == null)
        return this;
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return this;
    }

    /**
     * @return {@link #appointment} (Parent appointment that this response is replying to.)
     */
    public Reference getAppointment() { 
      if (this.appointment == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create AppointmentResponse.appointment");
        else if (Configuration.doAutoCreate())
          this.appointment = new Reference(); // cc
      return this.appointment;
    }

    public boolean hasAppointment() { 
      return this.appointment != null && !this.appointment.isEmpty();
    }

    /**
     * @param value {@link #appointment} (Parent appointment that this response is replying to.)
     */
    public AppointmentResponse setAppointment(Reference value) { 
      this.appointment = value;
      return this;
    }

    /**
     * @return {@link #appointment} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Parent appointment that this response is replying to.)
     */
    public Appointment getAppointmentTarget() { 
      if (this.appointmentTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create AppointmentResponse.appointment");
        else if (Configuration.doAutoCreate())
          this.appointmentTarget = new Appointment(); // aa
      return this.appointmentTarget;
    }

    /**
     * @param value {@link #appointment} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Parent appointment that this response is replying to.)
     */
    public AppointmentResponse setAppointmentTarget(Appointment value) { 
      this.appointmentTarget = value;
      return this;
    }

    /**
     * @return {@link #participantType} (Role of participant in the appointment.)
     */
    public List<CodeableConcept> getParticipantType() { 
      if (this.participantType == null)
        this.participantType = new ArrayList<CodeableConcept>();
      return this.participantType;
    }

    public boolean hasParticipantType() { 
      if (this.participantType == null)
        return false;
      for (CodeableConcept item : this.participantType)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #participantType} (Role of participant in the appointment.)
     */
    // syntactic sugar
    public CodeableConcept addParticipantType() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.participantType == null)
        this.participantType = new ArrayList<CodeableConcept>();
      this.participantType.add(t);
      return t;
    }

    // syntactic sugar
    public AppointmentResponse addParticipantType(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.participantType == null)
        this.participantType = new ArrayList<CodeableConcept>();
      this.participantType.add(t);
      return this;
    }

    /**
     * @return {@link #actor} (A Person, Location/HealthcareService or Device that is participating in the appointment.)
     */
    public Reference getActor() { 
      if (this.actor == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create AppointmentResponse.actor");
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
    public AppointmentResponse setActor(Reference value) { 
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
    public AppointmentResponse setActorTarget(Resource value) { 
      this.actorTarget = value;
      return this;
    }

    /**
     * @return {@link #participantStatus} (Participation status of the Participant. When the status is declined or tentative if the start/end times are different to the appointment, then these times should be interpreted as a requested time change. When the status is accepted, the times can either be the time of the appointment (as a confirmation of the time) or can be empty.). This is the underlying object with id, value and extensions. The accessor "getParticipantStatus" gives direct access to the value
     */
    public Enumeration<ParticipantStatus> getParticipantStatusElement() { 
      if (this.participantStatus == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create AppointmentResponse.participantStatus");
        else if (Configuration.doAutoCreate())
          this.participantStatus = new Enumeration<ParticipantStatus>(new ParticipantStatusEnumFactory()); // bb
      return this.participantStatus;
    }

    public boolean hasParticipantStatusElement() { 
      return this.participantStatus != null && !this.participantStatus.isEmpty();
    }

    public boolean hasParticipantStatus() { 
      return this.participantStatus != null && !this.participantStatus.isEmpty();
    }

    /**
     * @param value {@link #participantStatus} (Participation status of the Participant. When the status is declined or tentative if the start/end times are different to the appointment, then these times should be interpreted as a requested time change. When the status is accepted, the times can either be the time of the appointment (as a confirmation of the time) or can be empty.). This is the underlying object with id, value and extensions. The accessor "getParticipantStatus" gives direct access to the value
     */
    public AppointmentResponse setParticipantStatusElement(Enumeration<ParticipantStatus> value) { 
      this.participantStatus = value;
      return this;
    }

    /**
     * @return Participation status of the Participant. When the status is declined or tentative if the start/end times are different to the appointment, then these times should be interpreted as a requested time change. When the status is accepted, the times can either be the time of the appointment (as a confirmation of the time) or can be empty.
     */
    public ParticipantStatus getParticipantStatus() { 
      return this.participantStatus == null ? null : this.participantStatus.getValue();
    }

    /**
     * @param value Participation status of the Participant. When the status is declined or tentative if the start/end times are different to the appointment, then these times should be interpreted as a requested time change. When the status is accepted, the times can either be the time of the appointment (as a confirmation of the time) or can be empty.
     */
    public AppointmentResponse setParticipantStatus(ParticipantStatus value) { 
        if (this.participantStatus == null)
          this.participantStatus = new Enumeration<ParticipantStatus>(new ParticipantStatusEnumFactory());
        this.participantStatus.setValue(value);
      return this;
    }

    /**
     * @return {@link #comment} (This comment is particularly important when the responder is declining, tentative or requesting another time to indicate the reasons why.). This is the underlying object with id, value and extensions. The accessor "getComment" gives direct access to the value
     */
    public StringType getCommentElement() { 
      if (this.comment == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create AppointmentResponse.comment");
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
     * @param value {@link #comment} (This comment is particularly important when the responder is declining, tentative or requesting another time to indicate the reasons why.). This is the underlying object with id, value and extensions. The accessor "getComment" gives direct access to the value
     */
    public AppointmentResponse setCommentElement(StringType value) { 
      this.comment = value;
      return this;
    }

    /**
     * @return This comment is particularly important when the responder is declining, tentative or requesting another time to indicate the reasons why.
     */
    public String getComment() { 
      return this.comment == null ? null : this.comment.getValue();
    }

    /**
     * @param value This comment is particularly important when the responder is declining, tentative or requesting another time to indicate the reasons why.
     */
    public AppointmentResponse setComment(String value) { 
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
     * @return {@link #start} (This may be either the same as the appointment request to confirm the details of the appointment, or alternately a new time to request a re-negotiation of the start time.). This is the underlying object with id, value and extensions. The accessor "getStart" gives direct access to the value
     */
    public InstantType getStartElement() { 
      if (this.start == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create AppointmentResponse.start");
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
     * @param value {@link #start} (This may be either the same as the appointment request to confirm the details of the appointment, or alternately a new time to request a re-negotiation of the start time.). This is the underlying object with id, value and extensions. The accessor "getStart" gives direct access to the value
     */
    public AppointmentResponse setStartElement(InstantType value) { 
      this.start = value;
      return this;
    }

    /**
     * @return This may be either the same as the appointment request to confirm the details of the appointment, or alternately a new time to request a re-negotiation of the start time.
     */
    public Date getStart() { 
      return this.start == null ? null : this.start.getValue();
    }

    /**
     * @param value This may be either the same as the appointment request to confirm the details of the appointment, or alternately a new time to request a re-negotiation of the start time.
     */
    public AppointmentResponse setStart(Date value) { 
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
     * @return {@link #end} (This may be either the same as the appointment request to confirm the details of the appointment, or alternately a new time to request a re-negotiation of the end time.). This is the underlying object with id, value and extensions. The accessor "getEnd" gives direct access to the value
     */
    public InstantType getEndElement() { 
      if (this.end == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create AppointmentResponse.end");
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
     * @param value {@link #end} (This may be either the same as the appointment request to confirm the details of the appointment, or alternately a new time to request a re-negotiation of the end time.). This is the underlying object with id, value and extensions. The accessor "getEnd" gives direct access to the value
     */
    public AppointmentResponse setEndElement(InstantType value) { 
      this.end = value;
      return this;
    }

    /**
     * @return This may be either the same as the appointment request to confirm the details of the appointment, or alternately a new time to request a re-negotiation of the end time.
     */
    public Date getEnd() { 
      return this.end == null ? null : this.end.getValue();
    }

    /**
     * @param value This may be either the same as the appointment request to confirm the details of the appointment, or alternately a new time to request a re-negotiation of the end time.
     */
    public AppointmentResponse setEnd(Date value) { 
      if (value == null)
        this.end = null;
      else {
        if (this.end == null)
          this.end = new InstantType();
        this.end.setValue(value);
      }
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "This records identifiers associated with this appointment response concern that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("appointment", "Reference(Appointment)", "Parent appointment that this response is replying to.", 0, java.lang.Integer.MAX_VALUE, appointment));
        childrenList.add(new Property("participantType", "CodeableConcept", "Role of participant in the appointment.", 0, java.lang.Integer.MAX_VALUE, participantType));
        childrenList.add(new Property("actor", "Reference(Patient|Practitioner|RelatedPerson|Device|HealthcareService|Location)", "A Person, Location/HealthcareService or Device that is participating in the appointment.", 0, java.lang.Integer.MAX_VALUE, actor));
        childrenList.add(new Property("participantStatus", "code", "Participation status of the Participant. When the status is declined or tentative if the start/end times are different to the appointment, then these times should be interpreted as a requested time change. When the status is accepted, the times can either be the time of the appointment (as a confirmation of the time) or can be empty.", 0, java.lang.Integer.MAX_VALUE, participantStatus));
        childrenList.add(new Property("comment", "string", "This comment is particularly important when the responder is declining, tentative or requesting another time to indicate the reasons why.", 0, java.lang.Integer.MAX_VALUE, comment));
        childrenList.add(new Property("start", "instant", "This may be either the same as the appointment request to confirm the details of the appointment, or alternately a new time to request a re-negotiation of the start time.", 0, java.lang.Integer.MAX_VALUE, start));
        childrenList.add(new Property("end", "instant", "This may be either the same as the appointment request to confirm the details of the appointment, or alternately a new time to request a re-negotiation of the end time.", 0, java.lang.Integer.MAX_VALUE, end));
      }

      public AppointmentResponse copy() {
        AppointmentResponse dst = new AppointmentResponse();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.appointment = appointment == null ? null : appointment.copy();
        if (participantType != null) {
          dst.participantType = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : participantType)
            dst.participantType.add(i.copy());
        };
        dst.actor = actor == null ? null : actor.copy();
        dst.participantStatus = participantStatus == null ? null : participantStatus.copy();
        dst.comment = comment == null ? null : comment.copy();
        dst.start = start == null ? null : start.copy();
        dst.end = end == null ? null : end.copy();
        return dst;
      }

      protected AppointmentResponse typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof AppointmentResponse))
          return false;
        AppointmentResponse o = (AppointmentResponse) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(appointment, o.appointment, true)
           && compareDeep(participantType, o.participantType, true) && compareDeep(actor, o.actor, true) && compareDeep(participantStatus, o.participantStatus, true)
           && compareDeep(comment, o.comment, true) && compareDeep(start, o.start, true) && compareDeep(end, o.end, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof AppointmentResponse))
          return false;
        AppointmentResponse o = (AppointmentResponse) other;
        return compareValues(participantStatus, o.participantStatus, true) && compareValues(comment, o.comment, true)
           && compareValues(start, o.start, true) && compareValues(end, o.end, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (appointment == null || appointment.isEmpty())
           && (participantType == null || participantType.isEmpty()) && (actor == null || actor.isEmpty())
           && (participantStatus == null || participantStatus.isEmpty()) && (comment == null || comment.isEmpty())
           && (start == null || start.isEmpty()) && (end == null || end.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.AppointmentResponse;
   }

  @SearchParamDefinition(name="actor", path="AppointmentResponse.actor", description="The Person, Location/HealthcareService or Device that this appointment response replies for", type="reference" )
  public static final String SP_ACTOR = "actor";
  @SearchParamDefinition(name="partstatus", path="AppointmentResponse.participantStatus", description="The participants acceptance status for this appointment", type="token" )
  public static final String SP_PARTSTATUS = "partstatus";
  @SearchParamDefinition(name="practitioner", path="AppointmentResponse.actor", description="This Response is for this Practitioner", type="reference" )
  public static final String SP_PRACTITIONER = "practitioner";
  @SearchParamDefinition(name="patient", path="AppointmentResponse.actor", description="This Response is for this Patient", type="reference" )
  public static final String SP_PATIENT = "patient";
  @SearchParamDefinition(name="appointment", path="AppointmentResponse.appointment", description="The appointment that the response is attached to", type="reference" )
  public static final String SP_APPOINTMENT = "appointment";
  @SearchParamDefinition(name="location", path="AppointmentResponse.actor", description="This Response is for this Location", type="reference" )
  public static final String SP_LOCATION = "location";

}

