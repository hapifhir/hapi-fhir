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
 * A reply to an appointment request for a patient and/or practitioner(s), such as a confirmation or rejection.
 */
@ResourceDef(name="AppointmentResponse", profile="http://hl7.org/fhir/Profile/AppointmentResponse")
public class AppointmentResponse extends DomainResource {

    public enum Participantstatus implements FhirEnum {
        /**
         * The appointment participant has accepted that they can attend the appointment at the time specified in the AppointmentResponse.
         */
        ACCEPTED, 
        /**
         * The appointment participant has declined the appointment.
         */
        DECLINED, 
        /**
         * The appointment participant has tentatively accepted the appointment.
         */
        TENTATIVE, 
        /**
         * The participant has in-process the appointment.
         */
        INPROCESS, 
        /**
         * The participant has completed the appointment.
         */
        COMPLETED, 
        /**
         * This is the intitial status of an appointment participant until a participant has replied. It implies that there is no commitment for the appointment.
         */
        NEEDSACTION, 
        /**
         * added to help the parsers
         */
        NULL;

      public static final ParticipantstatusEnumFactory ENUM_FACTORY = new ParticipantstatusEnumFactory();

        public static Participantstatus fromCode(String codeString) throws IllegalArgumentException {
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
        throw new IllegalArgumentException("Unknown Participantstatus code '"+codeString+"'");
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
            case ACCEPTED: return "The appointment participant has accepted that they can attend the appointment at the time specified in the AppointmentResponse.";
            case DECLINED: return "The appointment participant has declined the appointment.";
            case TENTATIVE: return "The appointment participant has tentatively accepted the appointment.";
            case INPROCESS: return "The participant has in-process the appointment.";
            case COMPLETED: return "The participant has completed the appointment.";
            case NEEDSACTION: return "This is the intitial status of an appointment participant until a participant has replied. It implies that there is no commitment for the appointment.";
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

  public static class ParticipantstatusEnumFactory implements EnumFactory<Participantstatus> {
    public Participantstatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("accepted".equals(codeString))
          return Participantstatus.ACCEPTED;
        if ("declined".equals(codeString))
          return Participantstatus.DECLINED;
        if ("tentative".equals(codeString))
          return Participantstatus.TENTATIVE;
        if ("in-process".equals(codeString))
          return Participantstatus.INPROCESS;
        if ("completed".equals(codeString))
          return Participantstatus.COMPLETED;
        if ("needs-action".equals(codeString))
          return Participantstatus.NEEDSACTION;
        throw new IllegalArgumentException("Unknown Participantstatus code '"+codeString+"'");
        }
    public String toCode(Participantstatus code) throws IllegalArgumentException {
      if (code == Participantstatus.ACCEPTED)
        return "accepted";
      if (code == Participantstatus.DECLINED)
        return "declined";
      if (code == Participantstatus.TENTATIVE)
        return "tentative";
      if (code == Participantstatus.INPROCESS)
        return "in-process";
      if (code == Participantstatus.COMPLETED)
        return "completed";
      if (code == Participantstatus.NEEDSACTION)
        return "needs-action";
      return "?";
      }
    }

    /**
     * This records identifiers associated with this appointment concern that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation).
     */
    @Child(name="identifier", type={Identifier.class}, order=-1, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="External Ids for this item", formalDefinition="This records identifiers associated with this appointment concern that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)." )
    protected List<Identifier> identifier;

    /**
     * Parent appointment that this response is replying to.
     */
    @Child(name="appointment", type={Appointment.class}, order=0, min=1, max=1)
    @Description(shortDefinition="Parent appointment that this response is replying to", formalDefinition="Parent appointment that this response is replying to." )
    protected Reference appointment;

    /**
     * The actual object that is the target of the reference (Parent appointment that this response is replying to.)
     */
    protected Appointment appointmentTarget;

    /**
     * Role of participant in the appointment.
     */
    @Child(name="participantType", type={CodeableConcept.class}, order=1, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Role of participant in the appointment", formalDefinition="Role of participant in the appointment." )
    protected List<CodeableConcept> participantType;

    /**
     * A Person of device that is participating in the appointment, usually Practitioner, Patient, RelatedPerson or Device.
     */
    @Child(name="individual", type={}, order=2, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="A Person of device that is participating in the appointment, usually Practitioner, Patient, RelatedPerson or Device", formalDefinition="A Person of device that is participating in the appointment, usually Practitioner, Patient, RelatedPerson or Device." )
    protected List<Reference> individual;
    /**
     * The actual objects that are the target of the reference (A Person of device that is participating in the appointment, usually Practitioner, Patient, RelatedPerson or Device.)
     */
    protected List<Resource> individualTarget;


    /**
     * Participation status of the Patient.
     */
    @Child(name="participantStatus", type={CodeType.class}, order=3, min=1, max=1)
    @Description(shortDefinition="accepted | declined | tentative | in-process | completed | needs-action", formalDefinition="Participation status of the Patient." )
    protected Enumeration<Participantstatus> participantStatus;

    /**
     * Additional comments about the appointment.
     */
    @Child(name="comment", type={StringType.class}, order=4, min=0, max=1)
    @Description(shortDefinition="Additional comments about the appointment", formalDefinition="Additional comments about the appointment." )
    protected StringType comment;

    /**
     * Date/Time that the appointment is to take place.
     */
    @Child(name="start", type={InstantType.class}, order=5, min=0, max=1)
    @Description(shortDefinition="Date/Time that the appointment is to take place", formalDefinition="Date/Time that the appointment is to take place." )
    protected InstantType start;

    /**
     * Date/Time that the appointment is to conclude.
     */
    @Child(name="end", type={InstantType.class}, order=6, min=0, max=1)
    @Description(shortDefinition="Date/Time that the appointment is to conclude", formalDefinition="Date/Time that the appointment is to conclude." )
    protected InstantType end;

    /**
     * Who recorded the appointment response.
     */
    @Child(name="lastModifiedBy", type={Practitioner.class, Patient.class, RelatedPerson.class}, order=7, min=0, max=1)
    @Description(shortDefinition="Who recorded the appointment response", formalDefinition="Who recorded the appointment response." )
    protected Reference lastModifiedBy;

    /**
     * The actual object that is the target of the reference (Who recorded the appointment response.)
     */
    protected Resource lastModifiedByTarget;

    /**
     * Date when the response was recorded or last updated.
     */
    @Child(name="lastModified", type={DateTimeType.class}, order=8, min=0, max=1)
    @Description(shortDefinition="Date when the response was recorded or last updated", formalDefinition="Date when the response was recorded or last updated." )
    protected DateTimeType lastModified;

    private static final long serialVersionUID = 1948503527L;

    public AppointmentResponse() {
      super();
    }

    public AppointmentResponse(Reference appointment, Enumeration<Participantstatus> participantStatus) {
      super();
      this.appointment = appointment;
      this.participantStatus = participantStatus;
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
     * @return {@link #appointment} (Parent appointment that this response is replying to.)
     */
    public Reference getAppointment() { 
      if (this.appointment == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create AppointmentResponse.appointment");
        else if (Configuration.doAutoCreate())
          this.appointment = new Reference();
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
          this.appointmentTarget = new Appointment();
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

    /**
     * @return {@link #individual} (A Person of device that is participating in the appointment, usually Practitioner, Patient, RelatedPerson or Device.)
     */
    public List<Reference> getIndividual() { 
      if (this.individual == null)
        this.individual = new ArrayList<Reference>();
      return this.individual;
    }

    public boolean hasIndividual() { 
      if (this.individual == null)
        return false;
      for (Reference item : this.individual)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #individual} (A Person of device that is participating in the appointment, usually Practitioner, Patient, RelatedPerson or Device.)
     */
    // syntactic sugar
    public Reference addIndividual() { //3
      Reference t = new Reference();
      if (this.individual == null)
        this.individual = new ArrayList<Reference>();
      this.individual.add(t);
      return t;
    }

    /**
     * @return {@link #individual} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. A Person of device that is participating in the appointment, usually Practitioner, Patient, RelatedPerson or Device.)
     */
    public List<Resource> getIndividualTarget() { 
      if (this.individualTarget == null)
        this.individualTarget = new ArrayList<Resource>();
      return this.individualTarget;
    }

    /**
     * @return {@link #participantStatus} (Participation status of the Patient.). This is the underlying object with id, value and extensions. The accessor "getParticipantStatus" gives direct access to the value
     */
    public Enumeration<Participantstatus> getParticipantStatusElement() { 
      if (this.participantStatus == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create AppointmentResponse.participantStatus");
        else if (Configuration.doAutoCreate())
          this.participantStatus = new Enumeration<Participantstatus>();
      return this.participantStatus;
    }

    public boolean hasParticipantStatusElement() { 
      return this.participantStatus != null && !this.participantStatus.isEmpty();
    }

    public boolean hasParticipantStatus() { 
      return this.participantStatus != null && !this.participantStatus.isEmpty();
    }

    /**
     * @param value {@link #participantStatus} (Participation status of the Patient.). This is the underlying object with id, value and extensions. The accessor "getParticipantStatus" gives direct access to the value
     */
    public AppointmentResponse setParticipantStatusElement(Enumeration<Participantstatus> value) { 
      this.participantStatus = value;
      return this;
    }

    /**
     * @return Participation status of the Patient.
     */
    public Participantstatus getParticipantStatus() { 
      return this.participantStatus == null ? null : this.participantStatus.getValue();
    }

    /**
     * @param value Participation status of the Patient.
     */
    public AppointmentResponse setParticipantStatus(Participantstatus value) { 
        if (this.participantStatus == null)
          this.participantStatus = new Enumeration<Participantstatus>(Participantstatus.ENUM_FACTORY);
        this.participantStatus.setValue(value);
      return this;
    }

    /**
     * @return {@link #comment} (Additional comments about the appointment.). This is the underlying object with id, value and extensions. The accessor "getComment" gives direct access to the value
     */
    public StringType getCommentElement() { 
      if (this.comment == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create AppointmentResponse.comment");
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
    public AppointmentResponse setCommentElement(StringType value) { 
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
     * @return {@link #start} (Date/Time that the appointment is to take place.). This is the underlying object with id, value and extensions. The accessor "getStart" gives direct access to the value
     */
    public InstantType getStartElement() { 
      if (this.start == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create AppointmentResponse.start");
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
    public AppointmentResponse setStartElement(InstantType value) { 
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
     * @return {@link #end} (Date/Time that the appointment is to conclude.). This is the underlying object with id, value and extensions. The accessor "getEnd" gives direct access to the value
     */
    public InstantType getEndElement() { 
      if (this.end == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create AppointmentResponse.end");
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
    public AppointmentResponse setEndElement(InstantType value) { 
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

    /**
     * @return {@link #lastModifiedBy} (Who recorded the appointment response.)
     */
    public Reference getLastModifiedBy() { 
      if (this.lastModifiedBy == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create AppointmentResponse.lastModifiedBy");
        else if (Configuration.doAutoCreate())
          this.lastModifiedBy = new Reference();
      return this.lastModifiedBy;
    }

    public boolean hasLastModifiedBy() { 
      return this.lastModifiedBy != null && !this.lastModifiedBy.isEmpty();
    }

    /**
     * @param value {@link #lastModifiedBy} (Who recorded the appointment response.)
     */
    public AppointmentResponse setLastModifiedBy(Reference value) { 
      this.lastModifiedBy = value;
      return this;
    }

    /**
     * @return {@link #lastModifiedBy} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Who recorded the appointment response.)
     */
    public Resource getLastModifiedByTarget() { 
      return this.lastModifiedByTarget;
    }

    /**
     * @param value {@link #lastModifiedBy} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Who recorded the appointment response.)
     */
    public AppointmentResponse setLastModifiedByTarget(Resource value) { 
      this.lastModifiedByTarget = value;
      return this;
    }

    /**
     * @return {@link #lastModified} (Date when the response was recorded or last updated.). This is the underlying object with id, value and extensions. The accessor "getLastModified" gives direct access to the value
     */
    public DateTimeType getLastModifiedElement() { 
      if (this.lastModified == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create AppointmentResponse.lastModified");
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
     * @param value {@link #lastModified} (Date when the response was recorded or last updated.). This is the underlying object with id, value and extensions. The accessor "getLastModified" gives direct access to the value
     */
    public AppointmentResponse setLastModifiedElement(DateTimeType value) { 
      this.lastModified = value;
      return this;
    }

    /**
     * @return Date when the response was recorded or last updated.
     */
    public Date getLastModified() { 
      return this.lastModified == null ? null : this.lastModified.getValue();
    }

    /**
     * @param value Date when the response was recorded or last updated.
     */
    public AppointmentResponse setLastModified(Date value) { 
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
        childrenList.add(new Property("appointment", "Reference(Appointment)", "Parent appointment that this response is replying to.", 0, java.lang.Integer.MAX_VALUE, appointment));
        childrenList.add(new Property("participantType", "CodeableConcept", "Role of participant in the appointment.", 0, java.lang.Integer.MAX_VALUE, participantType));
        childrenList.add(new Property("individual", "Reference(Any)", "A Person of device that is participating in the appointment, usually Practitioner, Patient, RelatedPerson or Device.", 0, java.lang.Integer.MAX_VALUE, individual));
        childrenList.add(new Property("participantStatus", "code", "Participation status of the Patient.", 0, java.lang.Integer.MAX_VALUE, participantStatus));
        childrenList.add(new Property("comment", "string", "Additional comments about the appointment.", 0, java.lang.Integer.MAX_VALUE, comment));
        childrenList.add(new Property("start", "instant", "Date/Time that the appointment is to take place.", 0, java.lang.Integer.MAX_VALUE, start));
        childrenList.add(new Property("end", "instant", "Date/Time that the appointment is to conclude.", 0, java.lang.Integer.MAX_VALUE, end));
        childrenList.add(new Property("lastModifiedBy", "Reference(Practitioner|Patient|RelatedPerson)", "Who recorded the appointment response.", 0, java.lang.Integer.MAX_VALUE, lastModifiedBy));
        childrenList.add(new Property("lastModified", "dateTime", "Date when the response was recorded or last updated.", 0, java.lang.Integer.MAX_VALUE, lastModified));
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
        if (individual != null) {
          dst.individual = new ArrayList<Reference>();
          for (Reference i : individual)
            dst.individual.add(i.copy());
        };
        dst.participantStatus = participantStatus == null ? null : participantStatus.copy();
        dst.comment = comment == null ? null : comment.copy();
        dst.start = start == null ? null : start.copy();
        dst.end = end == null ? null : end.copy();
        dst.lastModifiedBy = lastModifiedBy == null ? null : lastModifiedBy.copy();
        dst.lastModified = lastModified == null ? null : lastModified.copy();
        return dst;
      }

      protected AppointmentResponse typedCopy() {
        return copy();
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (appointment == null || appointment.isEmpty())
           && (participantType == null || participantType.isEmpty()) && (individual == null || individual.isEmpty())
           && (participantStatus == null || participantStatus.isEmpty()) && (comment == null || comment.isEmpty())
           && (start == null || start.isEmpty()) && (end == null || end.isEmpty()) && (lastModifiedBy == null || lastModifiedBy.isEmpty())
           && (lastModified == null || lastModified.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.AppointmentResponse;
   }

  @SearchParamDefinition(name="partstatus", path="AppointmentResponse.participantStatus", description="The overall status of the appointment", type="token" )
  public static final String SP_PARTSTATUS = "partstatus";
  @SearchParamDefinition(name="subject", path="AppointmentResponse.individual", description="The subject that the appointment response replies for", type="reference" )
  public static final String SP_SUBJECT = "subject";
  @SearchParamDefinition(name="appointment", path="AppointmentResponse.appointment", description="The appointment that the response is attached to", type="reference" )
  public static final String SP_APPOINTMENT = "appointment";

}

