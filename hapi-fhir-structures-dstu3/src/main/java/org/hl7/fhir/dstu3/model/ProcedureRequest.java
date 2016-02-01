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
 * A request for a procedure to be performed. May be a proposal or an order.
 */
@ResourceDef(name="ProcedureRequest", profile="http://hl7.org/fhir/Profile/ProcedureRequest")
public class ProcedureRequest extends DomainResource {

    public enum ProcedureRequestStatus {
        /**
         * The request has been proposed.
         */
        PROPOSED, 
        /**
         * The request is in preliminary form, prior to being requested.
         */
        DRAFT, 
        /**
         * The request has been placed.
         */
        REQUESTED, 
        /**
         * The receiving system has received the request but not yet decided whether it will be performed.
         */
        RECEIVED, 
        /**
         * The receiving system has accepted the request, but work has not yet commenced.
         */
        ACCEPTED, 
        /**
         * The work to fulfill the request is happening.
         */
        INPROGRESS, 
        /**
         * The work has been completed, the report(s) released, and no further work is planned.
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
         * The request was attempted, but due to some procedural error, it could not be completed.
         */
        ABORTED, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ProcedureRequestStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("proposed".equals(codeString))
          return PROPOSED;
        if ("draft".equals(codeString))
          return DRAFT;
        if ("requested".equals(codeString))
          return REQUESTED;
        if ("received".equals(codeString))
          return RECEIVED;
        if ("accepted".equals(codeString))
          return ACCEPTED;
        if ("in-progress".equals(codeString))
          return INPROGRESS;
        if ("completed".equals(codeString))
          return COMPLETED;
        if ("suspended".equals(codeString))
          return SUSPENDED;
        if ("rejected".equals(codeString))
          return REJECTED;
        if ("aborted".equals(codeString))
          return ABORTED;
        throw new FHIRException("Unknown ProcedureRequestStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PROPOSED: return "proposed";
            case DRAFT: return "draft";
            case REQUESTED: return "requested";
            case RECEIVED: return "received";
            case ACCEPTED: return "accepted";
            case INPROGRESS: return "in-progress";
            case COMPLETED: return "completed";
            case SUSPENDED: return "suspended";
            case REJECTED: return "rejected";
            case ABORTED: return "aborted";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case PROPOSED: return "http://hl7.org/fhir/procedure-request-status";
            case DRAFT: return "http://hl7.org/fhir/procedure-request-status";
            case REQUESTED: return "http://hl7.org/fhir/procedure-request-status";
            case RECEIVED: return "http://hl7.org/fhir/procedure-request-status";
            case ACCEPTED: return "http://hl7.org/fhir/procedure-request-status";
            case INPROGRESS: return "http://hl7.org/fhir/procedure-request-status";
            case COMPLETED: return "http://hl7.org/fhir/procedure-request-status";
            case SUSPENDED: return "http://hl7.org/fhir/procedure-request-status";
            case REJECTED: return "http://hl7.org/fhir/procedure-request-status";
            case ABORTED: return "http://hl7.org/fhir/procedure-request-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case PROPOSED: return "The request has been proposed.";
            case DRAFT: return "The request is in preliminary form, prior to being requested.";
            case REQUESTED: return "The request has been placed.";
            case RECEIVED: return "The receiving system has received the request but not yet decided whether it will be performed.";
            case ACCEPTED: return "The receiving system has accepted the request, but work has not yet commenced.";
            case INPROGRESS: return "The work to fulfill the request is happening.";
            case COMPLETED: return "The work has been completed, the report(s) released, and no further work is planned.";
            case SUSPENDED: return "The request has been held by originating system/user request.";
            case REJECTED: return "The receiving system has declined to fulfill the request.";
            case ABORTED: return "The request was attempted, but due to some procedural error, it could not be completed.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PROPOSED: return "Proposed";
            case DRAFT: return "Draft";
            case REQUESTED: return "Requested";
            case RECEIVED: return "Received";
            case ACCEPTED: return "Accepted";
            case INPROGRESS: return "In Progress";
            case COMPLETED: return "Completed";
            case SUSPENDED: return "Suspended";
            case REJECTED: return "Rejected";
            case ABORTED: return "Aborted";
            default: return "?";
          }
        }
    }

  public static class ProcedureRequestStatusEnumFactory implements EnumFactory<ProcedureRequestStatus> {
    public ProcedureRequestStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("proposed".equals(codeString))
          return ProcedureRequestStatus.PROPOSED;
        if ("draft".equals(codeString))
          return ProcedureRequestStatus.DRAFT;
        if ("requested".equals(codeString))
          return ProcedureRequestStatus.REQUESTED;
        if ("received".equals(codeString))
          return ProcedureRequestStatus.RECEIVED;
        if ("accepted".equals(codeString))
          return ProcedureRequestStatus.ACCEPTED;
        if ("in-progress".equals(codeString))
          return ProcedureRequestStatus.INPROGRESS;
        if ("completed".equals(codeString))
          return ProcedureRequestStatus.COMPLETED;
        if ("suspended".equals(codeString))
          return ProcedureRequestStatus.SUSPENDED;
        if ("rejected".equals(codeString))
          return ProcedureRequestStatus.REJECTED;
        if ("aborted".equals(codeString))
          return ProcedureRequestStatus.ABORTED;
        throw new IllegalArgumentException("Unknown ProcedureRequestStatus code '"+codeString+"'");
        }
        public Enumeration<ProcedureRequestStatus> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("proposed".equals(codeString))
          return new Enumeration<ProcedureRequestStatus>(this, ProcedureRequestStatus.PROPOSED);
        if ("draft".equals(codeString))
          return new Enumeration<ProcedureRequestStatus>(this, ProcedureRequestStatus.DRAFT);
        if ("requested".equals(codeString))
          return new Enumeration<ProcedureRequestStatus>(this, ProcedureRequestStatus.REQUESTED);
        if ("received".equals(codeString))
          return new Enumeration<ProcedureRequestStatus>(this, ProcedureRequestStatus.RECEIVED);
        if ("accepted".equals(codeString))
          return new Enumeration<ProcedureRequestStatus>(this, ProcedureRequestStatus.ACCEPTED);
        if ("in-progress".equals(codeString))
          return new Enumeration<ProcedureRequestStatus>(this, ProcedureRequestStatus.INPROGRESS);
        if ("completed".equals(codeString))
          return new Enumeration<ProcedureRequestStatus>(this, ProcedureRequestStatus.COMPLETED);
        if ("suspended".equals(codeString))
          return new Enumeration<ProcedureRequestStatus>(this, ProcedureRequestStatus.SUSPENDED);
        if ("rejected".equals(codeString))
          return new Enumeration<ProcedureRequestStatus>(this, ProcedureRequestStatus.REJECTED);
        if ("aborted".equals(codeString))
          return new Enumeration<ProcedureRequestStatus>(this, ProcedureRequestStatus.ABORTED);
        throw new FHIRException("Unknown ProcedureRequestStatus code '"+codeString+"'");
        }
    public String toCode(ProcedureRequestStatus code) {
      if (code == ProcedureRequestStatus.PROPOSED)
        return "proposed";
      if (code == ProcedureRequestStatus.DRAFT)
        return "draft";
      if (code == ProcedureRequestStatus.REQUESTED)
        return "requested";
      if (code == ProcedureRequestStatus.RECEIVED)
        return "received";
      if (code == ProcedureRequestStatus.ACCEPTED)
        return "accepted";
      if (code == ProcedureRequestStatus.INPROGRESS)
        return "in-progress";
      if (code == ProcedureRequestStatus.COMPLETED)
        return "completed";
      if (code == ProcedureRequestStatus.SUSPENDED)
        return "suspended";
      if (code == ProcedureRequestStatus.REJECTED)
        return "rejected";
      if (code == ProcedureRequestStatus.ABORTED)
        return "aborted";
      return "?";
      }
    public String toSystem(ProcedureRequestStatus code) {
      return code.getSystem();
      }
    }

    public enum ProcedureRequestPriority {
        /**
         * The request has a normal priority.
         */
        ROUTINE, 
        /**
         * The request should be done urgently.
         */
        URGENT, 
        /**
         * The request is time-critical.
         */
        STAT, 
        /**
         * The request should be acted on as soon as possible.
         */
        ASAP, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ProcedureRequestPriority fromCode(String codeString) throws FHIRException {
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
        throw new FHIRException("Unknown ProcedureRequestPriority code '"+codeString+"'");
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
            case ROUTINE: return "http://hl7.org/fhir/procedure-request-priority";
            case URGENT: return "http://hl7.org/fhir/procedure-request-priority";
            case STAT: return "http://hl7.org/fhir/procedure-request-priority";
            case ASAP: return "http://hl7.org/fhir/procedure-request-priority";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case ROUTINE: return "The request has a normal priority.";
            case URGENT: return "The request should be done urgently.";
            case STAT: return "The request is time-critical.";
            case ASAP: return "The request should be acted on as soon as possible.";
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

  public static class ProcedureRequestPriorityEnumFactory implements EnumFactory<ProcedureRequestPriority> {
    public ProcedureRequestPriority fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("routine".equals(codeString))
          return ProcedureRequestPriority.ROUTINE;
        if ("urgent".equals(codeString))
          return ProcedureRequestPriority.URGENT;
        if ("stat".equals(codeString))
          return ProcedureRequestPriority.STAT;
        if ("asap".equals(codeString))
          return ProcedureRequestPriority.ASAP;
        throw new IllegalArgumentException("Unknown ProcedureRequestPriority code '"+codeString+"'");
        }
        public Enumeration<ProcedureRequestPriority> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("routine".equals(codeString))
          return new Enumeration<ProcedureRequestPriority>(this, ProcedureRequestPriority.ROUTINE);
        if ("urgent".equals(codeString))
          return new Enumeration<ProcedureRequestPriority>(this, ProcedureRequestPriority.URGENT);
        if ("stat".equals(codeString))
          return new Enumeration<ProcedureRequestPriority>(this, ProcedureRequestPriority.STAT);
        if ("asap".equals(codeString))
          return new Enumeration<ProcedureRequestPriority>(this, ProcedureRequestPriority.ASAP);
        throw new FHIRException("Unknown ProcedureRequestPriority code '"+codeString+"'");
        }
    public String toCode(ProcedureRequestPriority code) {
      if (code == ProcedureRequestPriority.ROUTINE)
        return "routine";
      if (code == ProcedureRequestPriority.URGENT)
        return "urgent";
      if (code == ProcedureRequestPriority.STAT)
        return "stat";
      if (code == ProcedureRequestPriority.ASAP)
        return "asap";
      return "?";
      }
    public String toSystem(ProcedureRequestPriority code) {
      return code.getSystem();
      }
    }

    /**
     * Identifiers assigned to this order by the order or by the receiver.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Unique identifier for the request", formalDefinition="Identifiers assigned to this order by the order or by the receiver." )
    protected List<Identifier> identifier;

    /**
     * The person, animal or group that should receive the procedure.
     */
    @Child(name = "subject", type = {Patient.class, Group.class}, order=1, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Who the procedure should be done to", formalDefinition="The person, animal or group that should receive the procedure." )
    protected Reference subject;

    /**
     * The actual object that is the target of the reference (The person, animal or group that should receive the procedure.)
     */
    protected Resource subjectTarget;

    /**
     * The specific procedure that is ordered. Use text if the exact nature of the procedure cannot be coded.
     */
    @Child(name = "code", type = {CodeableConcept.class}, order=2, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="What procedure to perform", formalDefinition="The specific procedure that is ordered. Use text if the exact nature of the procedure cannot be coded." )
    protected CodeableConcept code;

    /**
     * Indicates the sites on the subject's body where the procedure should be performed (I.e. the target sites).
     */
    @Child(name = "bodySite", type = {CodeableConcept.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="What part of body to perform on", formalDefinition="Indicates the sites on the subject's body where the procedure should be performed (I.e. the target sites)." )
    protected List<CodeableConcept> bodySite;

    /**
     * The reason why the procedure is being proposed or ordered. This procedure request may be motivated by a Condition for instance.
     */
    @Child(name = "reason", type = {CodeableConcept.class, Condition.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Why procedure should occur", formalDefinition="The reason why the procedure is being proposed or ordered. This procedure request may be motivated by a Condition for instance." )
    protected Type reason;

    /**
     * The timing schedule for the proposed or ordered procedure. The Schedule data type allows many different expressions.  E.g. "Every 8 hours"; "Three times a day"; "1/2 an hour before breakfast for 10 days from 23-Dec 2011:"; "15 Oct 2013, 17 Oct 2013 and 1 Nov 2013".
     */
    @Child(name = "scheduled", type = {DateTimeType.class, Period.class, Timing.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When procedure should occur", formalDefinition="The timing schedule for the proposed or ordered procedure. The Schedule data type allows many different expressions.  E.g. \"Every 8 hours\"; \"Three times a day\"; \"1/2 an hour before breakfast for 10 days from 23-Dec 2011:\"; \"15 Oct 2013, 17 Oct 2013 and 1 Nov 2013\"." )
    protected Type scheduled;

    /**
     * The encounter within which the procedure proposal or request was created.
     */
    @Child(name = "encounter", type = {Encounter.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Encounter request created during", formalDefinition="The encounter within which the procedure proposal or request was created." )
    protected Reference encounter;

    /**
     * The actual object that is the target of the reference (The encounter within which the procedure proposal or request was created.)
     */
    protected Encounter encounterTarget;

    /**
     * For example, the surgeon, anaethetist, endoscopist, etc.
     */
    @Child(name = "performer", type = {Practitioner.class, Organization.class, Patient.class, RelatedPerson.class}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Who should perform the procedure", formalDefinition="For example, the surgeon, anaethetist, endoscopist, etc." )
    protected Reference performer;

    /**
     * The actual object that is the target of the reference (For example, the surgeon, anaethetist, endoscopist, etc.)
     */
    protected Resource performerTarget;

    /**
     * The status of the order.
     */
    @Child(name = "status", type = {CodeType.class}, order=8, min=0, max=1, modifier=true, summary=true)
    @Description(shortDefinition="proposed | draft | requested | received | accepted | in-progress | completed | suspended | rejected | aborted", formalDefinition="The status of the order." )
    protected Enumeration<ProcedureRequestStatus> status;

    /**
     * Any other notes associated with this proposal or order - e.g. provider instructions.
     */
    @Child(name = "notes", type = {Annotation.class}, order=9, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Additional information about desired procedure", formalDefinition="Any other notes associated with this proposal or order - e.g. provider instructions." )
    protected List<Annotation> notes;

    /**
     * If a CodeableConcept is present, it indicates the pre-condition for performing the procedure.
     */
    @Child(name = "asNeeded", type = {BooleanType.class, CodeableConcept.class}, order=10, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Preconditions for procedure", formalDefinition="If a CodeableConcept is present, it indicates the pre-condition for performing the procedure." )
    protected Type asNeeded;

    /**
     * The time when the request was made.
     */
    @Child(name = "orderedOn", type = {DateTimeType.class}, order=11, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When request was created", formalDefinition="The time when the request was made." )
    protected DateTimeType orderedOn;

    /**
     * The healthcare professional responsible for proposing or ordering the procedure.
     */
    @Child(name = "orderer", type = {Practitioner.class, Patient.class, RelatedPerson.class, Device.class}, order=12, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Who made request", formalDefinition="The healthcare professional responsible for proposing or ordering the procedure." )
    protected Reference orderer;

    /**
     * The actual object that is the target of the reference (The healthcare professional responsible for proposing or ordering the procedure.)
     */
    protected Resource ordererTarget;

    /**
     * The clinical priority associated with this order.
     */
    @Child(name = "priority", type = {CodeType.class}, order=13, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="routine | urgent | stat | asap", formalDefinition="The clinical priority associated with this order." )
    protected Enumeration<ProcedureRequestPriority> priority;

    private static final long serialVersionUID = -916650578L;

  /**
   * Constructor
   */
    public ProcedureRequest() {
      super();
    }

  /**
   * Constructor
   */
    public ProcedureRequest(Reference subject, CodeableConcept code) {
      super();
      this.subject = subject;
      this.code = code;
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

    // syntactic sugar
    public ProcedureRequest addIdentifier(Identifier t) { //3
      if (t == null)
        return this;
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return this;
    }

    /**
     * @return {@link #subject} (The person, animal or group that should receive the procedure.)
     */
    public Reference getSubject() { 
      if (this.subject == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ProcedureRequest.subject");
        else if (Configuration.doAutoCreate())
          this.subject = new Reference(); // cc
      return this.subject;
    }

    public boolean hasSubject() { 
      return this.subject != null && !this.subject.isEmpty();
    }

    /**
     * @param value {@link #subject} (The person, animal or group that should receive the procedure.)
     */
    public ProcedureRequest setSubject(Reference value) { 
      this.subject = value;
      return this;
    }

    /**
     * @return {@link #subject} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The person, animal or group that should receive the procedure.)
     */
    public Resource getSubjectTarget() { 
      return this.subjectTarget;
    }

    /**
     * @param value {@link #subject} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The person, animal or group that should receive the procedure.)
     */
    public ProcedureRequest setSubjectTarget(Resource value) { 
      this.subjectTarget = value;
      return this;
    }

    /**
     * @return {@link #code} (The specific procedure that is ordered. Use text if the exact nature of the procedure cannot be coded.)
     */
    public CodeableConcept getCode() { 
      if (this.code == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ProcedureRequest.code");
        else if (Configuration.doAutoCreate())
          this.code = new CodeableConcept(); // cc
      return this.code;
    }

    public boolean hasCode() { 
      return this.code != null && !this.code.isEmpty();
    }

    /**
     * @param value {@link #code} (The specific procedure that is ordered. Use text if the exact nature of the procedure cannot be coded.)
     */
    public ProcedureRequest setCode(CodeableConcept value) { 
      this.code = value;
      return this;
    }

    /**
     * @return {@link #bodySite} (Indicates the sites on the subject's body where the procedure should be performed (I.e. the target sites).)
     */
    public List<CodeableConcept> getBodySite() { 
      if (this.bodySite == null)
        this.bodySite = new ArrayList<CodeableConcept>();
      return this.bodySite;
    }

    public boolean hasBodySite() { 
      if (this.bodySite == null)
        return false;
      for (CodeableConcept item : this.bodySite)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #bodySite} (Indicates the sites on the subject's body where the procedure should be performed (I.e. the target sites).)
     */
    // syntactic sugar
    public CodeableConcept addBodySite() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.bodySite == null)
        this.bodySite = new ArrayList<CodeableConcept>();
      this.bodySite.add(t);
      return t;
    }

    // syntactic sugar
    public ProcedureRequest addBodySite(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.bodySite == null)
        this.bodySite = new ArrayList<CodeableConcept>();
      this.bodySite.add(t);
      return this;
    }

    /**
     * @return {@link #reason} (The reason why the procedure is being proposed or ordered. This procedure request may be motivated by a Condition for instance.)
     */
    public Type getReason() { 
      return this.reason;
    }

    /**
     * @return {@link #reason} (The reason why the procedure is being proposed or ordered. This procedure request may be motivated by a Condition for instance.)
     */
    public CodeableConcept getReasonCodeableConcept() throws FHIRException { 
      if (!(this.reason instanceof CodeableConcept))
        throw new FHIRException("Type mismatch: the type CodeableConcept was expected, but "+this.reason.getClass().getName()+" was encountered");
      return (CodeableConcept) this.reason;
    }

    public boolean hasReasonCodeableConcept() { 
      return this.reason instanceof CodeableConcept;
    }

    /**
     * @return {@link #reason} (The reason why the procedure is being proposed or ordered. This procedure request may be motivated by a Condition for instance.)
     */
    public Reference getReasonReference() throws FHIRException { 
      if (!(this.reason instanceof Reference))
        throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.reason.getClass().getName()+" was encountered");
      return (Reference) this.reason;
    }

    public boolean hasReasonReference() { 
      return this.reason instanceof Reference;
    }

    public boolean hasReason() { 
      return this.reason != null && !this.reason.isEmpty();
    }

    /**
     * @param value {@link #reason} (The reason why the procedure is being proposed or ordered. This procedure request may be motivated by a Condition for instance.)
     */
    public ProcedureRequest setReason(Type value) { 
      this.reason = value;
      return this;
    }

    /**
     * @return {@link #scheduled} (The timing schedule for the proposed or ordered procedure. The Schedule data type allows many different expressions.  E.g. "Every 8 hours"; "Three times a day"; "1/2 an hour before breakfast for 10 days from 23-Dec 2011:"; "15 Oct 2013, 17 Oct 2013 and 1 Nov 2013".)
     */
    public Type getScheduled() { 
      return this.scheduled;
    }

    /**
     * @return {@link #scheduled} (The timing schedule for the proposed or ordered procedure. The Schedule data type allows many different expressions.  E.g. "Every 8 hours"; "Three times a day"; "1/2 an hour before breakfast for 10 days from 23-Dec 2011:"; "15 Oct 2013, 17 Oct 2013 and 1 Nov 2013".)
     */
    public DateTimeType getScheduledDateTimeType() throws FHIRException { 
      if (!(this.scheduled instanceof DateTimeType))
        throw new FHIRException("Type mismatch: the type DateTimeType was expected, but "+this.scheduled.getClass().getName()+" was encountered");
      return (DateTimeType) this.scheduled;
    }

    public boolean hasScheduledDateTimeType() { 
      return this.scheduled instanceof DateTimeType;
    }

    /**
     * @return {@link #scheduled} (The timing schedule for the proposed or ordered procedure. The Schedule data type allows many different expressions.  E.g. "Every 8 hours"; "Three times a day"; "1/2 an hour before breakfast for 10 days from 23-Dec 2011:"; "15 Oct 2013, 17 Oct 2013 and 1 Nov 2013".)
     */
    public Period getScheduledPeriod() throws FHIRException { 
      if (!(this.scheduled instanceof Period))
        throw new FHIRException("Type mismatch: the type Period was expected, but "+this.scheduled.getClass().getName()+" was encountered");
      return (Period) this.scheduled;
    }

    public boolean hasScheduledPeriod() { 
      return this.scheduled instanceof Period;
    }

    /**
     * @return {@link #scheduled} (The timing schedule for the proposed or ordered procedure. The Schedule data type allows many different expressions.  E.g. "Every 8 hours"; "Three times a day"; "1/2 an hour before breakfast for 10 days from 23-Dec 2011:"; "15 Oct 2013, 17 Oct 2013 and 1 Nov 2013".)
     */
    public Timing getScheduledTiming() throws FHIRException { 
      if (!(this.scheduled instanceof Timing))
        throw new FHIRException("Type mismatch: the type Timing was expected, but "+this.scheduled.getClass().getName()+" was encountered");
      return (Timing) this.scheduled;
    }

    public boolean hasScheduledTiming() { 
      return this.scheduled instanceof Timing;
    }

    public boolean hasScheduled() { 
      return this.scheduled != null && !this.scheduled.isEmpty();
    }

    /**
     * @param value {@link #scheduled} (The timing schedule for the proposed or ordered procedure. The Schedule data type allows many different expressions.  E.g. "Every 8 hours"; "Three times a day"; "1/2 an hour before breakfast for 10 days from 23-Dec 2011:"; "15 Oct 2013, 17 Oct 2013 and 1 Nov 2013".)
     */
    public ProcedureRequest setScheduled(Type value) { 
      this.scheduled = value;
      return this;
    }

    /**
     * @return {@link #encounter} (The encounter within which the procedure proposal or request was created.)
     */
    public Reference getEncounter() { 
      if (this.encounter == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ProcedureRequest.encounter");
        else if (Configuration.doAutoCreate())
          this.encounter = new Reference(); // cc
      return this.encounter;
    }

    public boolean hasEncounter() { 
      return this.encounter != null && !this.encounter.isEmpty();
    }

    /**
     * @param value {@link #encounter} (The encounter within which the procedure proposal or request was created.)
     */
    public ProcedureRequest setEncounter(Reference value) { 
      this.encounter = value;
      return this;
    }

    /**
     * @return {@link #encounter} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The encounter within which the procedure proposal or request was created.)
     */
    public Encounter getEncounterTarget() { 
      if (this.encounterTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ProcedureRequest.encounter");
        else if (Configuration.doAutoCreate())
          this.encounterTarget = new Encounter(); // aa
      return this.encounterTarget;
    }

    /**
     * @param value {@link #encounter} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The encounter within which the procedure proposal or request was created.)
     */
    public ProcedureRequest setEncounterTarget(Encounter value) { 
      this.encounterTarget = value;
      return this;
    }

    /**
     * @return {@link #performer} (For example, the surgeon, anaethetist, endoscopist, etc.)
     */
    public Reference getPerformer() { 
      if (this.performer == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ProcedureRequest.performer");
        else if (Configuration.doAutoCreate())
          this.performer = new Reference(); // cc
      return this.performer;
    }

    public boolean hasPerformer() { 
      return this.performer != null && !this.performer.isEmpty();
    }

    /**
     * @param value {@link #performer} (For example, the surgeon, anaethetist, endoscopist, etc.)
     */
    public ProcedureRequest setPerformer(Reference value) { 
      this.performer = value;
      return this;
    }

    /**
     * @return {@link #performer} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (For example, the surgeon, anaethetist, endoscopist, etc.)
     */
    public Resource getPerformerTarget() { 
      return this.performerTarget;
    }

    /**
     * @param value {@link #performer} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (For example, the surgeon, anaethetist, endoscopist, etc.)
     */
    public ProcedureRequest setPerformerTarget(Resource value) { 
      this.performerTarget = value;
      return this;
    }

    /**
     * @return {@link #status} (The status of the order.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<ProcedureRequestStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ProcedureRequest.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<ProcedureRequestStatus>(new ProcedureRequestStatusEnumFactory()); // bb
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
    public ProcedureRequest setStatusElement(Enumeration<ProcedureRequestStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The status of the order.
     */
    public ProcedureRequestStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The status of the order.
     */
    public ProcedureRequest setStatus(ProcedureRequestStatus value) { 
      if (value == null)
        this.status = null;
      else {
        if (this.status == null)
          this.status = new Enumeration<ProcedureRequestStatus>(new ProcedureRequestStatusEnumFactory());
        this.status.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #notes} (Any other notes associated with this proposal or order - e.g. provider instructions.)
     */
    public List<Annotation> getNotes() { 
      if (this.notes == null)
        this.notes = new ArrayList<Annotation>();
      return this.notes;
    }

    public boolean hasNotes() { 
      if (this.notes == null)
        return false;
      for (Annotation item : this.notes)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #notes} (Any other notes associated with this proposal or order - e.g. provider instructions.)
     */
    // syntactic sugar
    public Annotation addNotes() { //3
      Annotation t = new Annotation();
      if (this.notes == null)
        this.notes = new ArrayList<Annotation>();
      this.notes.add(t);
      return t;
    }

    // syntactic sugar
    public ProcedureRequest addNotes(Annotation t) { //3
      if (t == null)
        return this;
      if (this.notes == null)
        this.notes = new ArrayList<Annotation>();
      this.notes.add(t);
      return this;
    }

    /**
     * @return {@link #asNeeded} (If a CodeableConcept is present, it indicates the pre-condition for performing the procedure.)
     */
    public Type getAsNeeded() { 
      return this.asNeeded;
    }

    /**
     * @return {@link #asNeeded} (If a CodeableConcept is present, it indicates the pre-condition for performing the procedure.)
     */
    public BooleanType getAsNeededBooleanType() throws FHIRException { 
      if (!(this.asNeeded instanceof BooleanType))
        throw new FHIRException("Type mismatch: the type BooleanType was expected, but "+this.asNeeded.getClass().getName()+" was encountered");
      return (BooleanType) this.asNeeded;
    }

    public boolean hasAsNeededBooleanType() { 
      return this.asNeeded instanceof BooleanType;
    }

    /**
     * @return {@link #asNeeded} (If a CodeableConcept is present, it indicates the pre-condition for performing the procedure.)
     */
    public CodeableConcept getAsNeededCodeableConcept() throws FHIRException { 
      if (!(this.asNeeded instanceof CodeableConcept))
        throw new FHIRException("Type mismatch: the type CodeableConcept was expected, but "+this.asNeeded.getClass().getName()+" was encountered");
      return (CodeableConcept) this.asNeeded;
    }

    public boolean hasAsNeededCodeableConcept() { 
      return this.asNeeded instanceof CodeableConcept;
    }

    public boolean hasAsNeeded() { 
      return this.asNeeded != null && !this.asNeeded.isEmpty();
    }

    /**
     * @param value {@link #asNeeded} (If a CodeableConcept is present, it indicates the pre-condition for performing the procedure.)
     */
    public ProcedureRequest setAsNeeded(Type value) { 
      this.asNeeded = value;
      return this;
    }

    /**
     * @return {@link #orderedOn} (The time when the request was made.). This is the underlying object with id, value and extensions. The accessor "getOrderedOn" gives direct access to the value
     */
    public DateTimeType getOrderedOnElement() { 
      if (this.orderedOn == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ProcedureRequest.orderedOn");
        else if (Configuration.doAutoCreate())
          this.orderedOn = new DateTimeType(); // bb
      return this.orderedOn;
    }

    public boolean hasOrderedOnElement() { 
      return this.orderedOn != null && !this.orderedOn.isEmpty();
    }

    public boolean hasOrderedOn() { 
      return this.orderedOn != null && !this.orderedOn.isEmpty();
    }

    /**
     * @param value {@link #orderedOn} (The time when the request was made.). This is the underlying object with id, value and extensions. The accessor "getOrderedOn" gives direct access to the value
     */
    public ProcedureRequest setOrderedOnElement(DateTimeType value) { 
      this.orderedOn = value;
      return this;
    }

    /**
     * @return The time when the request was made.
     */
    public Date getOrderedOn() { 
      return this.orderedOn == null ? null : this.orderedOn.getValue();
    }

    /**
     * @param value The time when the request was made.
     */
    public ProcedureRequest setOrderedOn(Date value) { 
      if (value == null)
        this.orderedOn = null;
      else {
        if (this.orderedOn == null)
          this.orderedOn = new DateTimeType();
        this.orderedOn.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #orderer} (The healthcare professional responsible for proposing or ordering the procedure.)
     */
    public Reference getOrderer() { 
      if (this.orderer == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ProcedureRequest.orderer");
        else if (Configuration.doAutoCreate())
          this.orderer = new Reference(); // cc
      return this.orderer;
    }

    public boolean hasOrderer() { 
      return this.orderer != null && !this.orderer.isEmpty();
    }

    /**
     * @param value {@link #orderer} (The healthcare professional responsible for proposing or ordering the procedure.)
     */
    public ProcedureRequest setOrderer(Reference value) { 
      this.orderer = value;
      return this;
    }

    /**
     * @return {@link #orderer} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The healthcare professional responsible for proposing or ordering the procedure.)
     */
    public Resource getOrdererTarget() { 
      return this.ordererTarget;
    }

    /**
     * @param value {@link #orderer} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The healthcare professional responsible for proposing or ordering the procedure.)
     */
    public ProcedureRequest setOrdererTarget(Resource value) { 
      this.ordererTarget = value;
      return this;
    }

    /**
     * @return {@link #priority} (The clinical priority associated with this order.). This is the underlying object with id, value and extensions. The accessor "getPriority" gives direct access to the value
     */
    public Enumeration<ProcedureRequestPriority> getPriorityElement() { 
      if (this.priority == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ProcedureRequest.priority");
        else if (Configuration.doAutoCreate())
          this.priority = new Enumeration<ProcedureRequestPriority>(new ProcedureRequestPriorityEnumFactory()); // bb
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
    public ProcedureRequest setPriorityElement(Enumeration<ProcedureRequestPriority> value) { 
      this.priority = value;
      return this;
    }

    /**
     * @return The clinical priority associated with this order.
     */
    public ProcedureRequestPriority getPriority() { 
      return this.priority == null ? null : this.priority.getValue();
    }

    /**
     * @param value The clinical priority associated with this order.
     */
    public ProcedureRequest setPriority(ProcedureRequestPriority value) { 
      if (value == null)
        this.priority = null;
      else {
        if (this.priority == null)
          this.priority = new Enumeration<ProcedureRequestPriority>(new ProcedureRequestPriorityEnumFactory());
        this.priority.setValue(value);
      }
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "Identifiers assigned to this order by the order or by the receiver.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("subject", "Reference(Patient|Group)", "The person, animal or group that should receive the procedure.", 0, java.lang.Integer.MAX_VALUE, subject));
        childrenList.add(new Property("code", "CodeableConcept", "The specific procedure that is ordered. Use text if the exact nature of the procedure cannot be coded.", 0, java.lang.Integer.MAX_VALUE, code));
        childrenList.add(new Property("bodySite", "CodeableConcept", "Indicates the sites on the subject's body where the procedure should be performed (I.e. the target sites).", 0, java.lang.Integer.MAX_VALUE, bodySite));
        childrenList.add(new Property("reason[x]", "CodeableConcept|Reference(Condition)", "The reason why the procedure is being proposed or ordered. This procedure request may be motivated by a Condition for instance.", 0, java.lang.Integer.MAX_VALUE, reason));
        childrenList.add(new Property("scheduled[x]", "dateTime|Period|Timing", "The timing schedule for the proposed or ordered procedure. The Schedule data type allows many different expressions.  E.g. \"Every 8 hours\"; \"Three times a day\"; \"1/2 an hour before breakfast for 10 days from 23-Dec 2011:\"; \"15 Oct 2013, 17 Oct 2013 and 1 Nov 2013\".", 0, java.lang.Integer.MAX_VALUE, scheduled));
        childrenList.add(new Property("encounter", "Reference(Encounter)", "The encounter within which the procedure proposal or request was created.", 0, java.lang.Integer.MAX_VALUE, encounter));
        childrenList.add(new Property("performer", "Reference(Practitioner|Organization|Patient|RelatedPerson)", "For example, the surgeon, anaethetist, endoscopist, etc.", 0, java.lang.Integer.MAX_VALUE, performer));
        childrenList.add(new Property("status", "code", "The status of the order.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("notes", "Annotation", "Any other notes associated with this proposal or order - e.g. provider instructions.", 0, java.lang.Integer.MAX_VALUE, notes));
        childrenList.add(new Property("asNeeded[x]", "boolean|CodeableConcept", "If a CodeableConcept is present, it indicates the pre-condition for performing the procedure.", 0, java.lang.Integer.MAX_VALUE, asNeeded));
        childrenList.add(new Property("orderedOn", "dateTime", "The time when the request was made.", 0, java.lang.Integer.MAX_VALUE, orderedOn));
        childrenList.add(new Property("orderer", "Reference(Practitioner|Patient|RelatedPerson|Device)", "The healthcare professional responsible for proposing or ordering the procedure.", 0, java.lang.Integer.MAX_VALUE, orderer));
        childrenList.add(new Property("priority", "code", "The clinical priority associated with this order.", 0, java.lang.Integer.MAX_VALUE, priority));
      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier"))
          this.getIdentifier().add(castToIdentifier(value));
        else if (name.equals("subject"))
          this.subject = castToReference(value); // Reference
        else if (name.equals("code"))
          this.code = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("bodySite"))
          this.getBodySite().add(castToCodeableConcept(value));
        else if (name.equals("reason[x]"))
          this.reason = (Type) value; // Type
        else if (name.equals("scheduled[x]"))
          this.scheduled = (Type) value; // Type
        else if (name.equals("encounter"))
          this.encounter = castToReference(value); // Reference
        else if (name.equals("performer"))
          this.performer = castToReference(value); // Reference
        else if (name.equals("status"))
          this.status = new ProcedureRequestStatusEnumFactory().fromType(value); // Enumeration<ProcedureRequestStatus>
        else if (name.equals("notes"))
          this.getNotes().add(castToAnnotation(value));
        else if (name.equals("asNeeded[x]"))
          this.asNeeded = (Type) value; // Type
        else if (name.equals("orderedOn"))
          this.orderedOn = castToDateTime(value); // DateTimeType
        else if (name.equals("orderer"))
          this.orderer = castToReference(value); // Reference
        else if (name.equals("priority"))
          this.priority = new ProcedureRequestPriorityEnumFactory().fromType(value); // Enumeration<ProcedureRequestPriority>
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("subject")) {
          this.subject = new Reference();
          return this.subject;
        }
        else if (name.equals("code")) {
          this.code = new CodeableConcept();
          return this.code;
        }
        else if (name.equals("bodySite")) {
          return addBodySite();
        }
        else if (name.equals("reasonCodeableConcept")) {
          this.reason = new CodeableConcept();
          return this.reason;
        }
        else if (name.equals("reasonReference")) {
          this.reason = new Reference();
          return this.reason;
        }
        else if (name.equals("scheduledDateTime")) {
          this.scheduled = new DateTimeType();
          return this.scheduled;
        }
        else if (name.equals("scheduledPeriod")) {
          this.scheduled = new Period();
          return this.scheduled;
        }
        else if (name.equals("scheduledTiming")) {
          this.scheduled = new Timing();
          return this.scheduled;
        }
        else if (name.equals("encounter")) {
          this.encounter = new Reference();
          return this.encounter;
        }
        else if (name.equals("performer")) {
          this.performer = new Reference();
          return this.performer;
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type ProcedureRequest.status");
        }
        else if (name.equals("notes")) {
          return addNotes();
        }
        else if (name.equals("asNeededBoolean")) {
          this.asNeeded = new BooleanType();
          return this.asNeeded;
        }
        else if (name.equals("asNeededCodeableConcept")) {
          this.asNeeded = new CodeableConcept();
          return this.asNeeded;
        }
        else if (name.equals("orderedOn")) {
          throw new FHIRException("Cannot call addChild on a primitive type ProcedureRequest.orderedOn");
        }
        else if (name.equals("orderer")) {
          this.orderer = new Reference();
          return this.orderer;
        }
        else if (name.equals("priority")) {
          throw new FHIRException("Cannot call addChild on a primitive type ProcedureRequest.priority");
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "ProcedureRequest";

  }

      public ProcedureRequest copy() {
        ProcedureRequest dst = new ProcedureRequest();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.subject = subject == null ? null : subject.copy();
        dst.code = code == null ? null : code.copy();
        if (bodySite != null) {
          dst.bodySite = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : bodySite)
            dst.bodySite.add(i.copy());
        };
        dst.reason = reason == null ? null : reason.copy();
        dst.scheduled = scheduled == null ? null : scheduled.copy();
        dst.encounter = encounter == null ? null : encounter.copy();
        dst.performer = performer == null ? null : performer.copy();
        dst.status = status == null ? null : status.copy();
        if (notes != null) {
          dst.notes = new ArrayList<Annotation>();
          for (Annotation i : notes)
            dst.notes.add(i.copy());
        };
        dst.asNeeded = asNeeded == null ? null : asNeeded.copy();
        dst.orderedOn = orderedOn == null ? null : orderedOn.copy();
        dst.orderer = orderer == null ? null : orderer.copy();
        dst.priority = priority == null ? null : priority.copy();
        return dst;
      }

      protected ProcedureRequest typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ProcedureRequest))
          return false;
        ProcedureRequest o = (ProcedureRequest) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(subject, o.subject, true) && compareDeep(code, o.code, true)
           && compareDeep(bodySite, o.bodySite, true) && compareDeep(reason, o.reason, true) && compareDeep(scheduled, o.scheduled, true)
           && compareDeep(encounter, o.encounter, true) && compareDeep(performer, o.performer, true) && compareDeep(status, o.status, true)
           && compareDeep(notes, o.notes, true) && compareDeep(asNeeded, o.asNeeded, true) && compareDeep(orderedOn, o.orderedOn, true)
           && compareDeep(orderer, o.orderer, true) && compareDeep(priority, o.priority, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ProcedureRequest))
          return false;
        ProcedureRequest o = (ProcedureRequest) other;
        return compareValues(status, o.status, true) && compareValues(orderedOn, o.orderedOn, true) && compareValues(priority, o.priority, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (subject == null || subject.isEmpty())
           && (code == null || code.isEmpty()) && (bodySite == null || bodySite.isEmpty()) && (reason == null || reason.isEmpty())
           && (scheduled == null || scheduled.isEmpty()) && (encounter == null || encounter.isEmpty())
           && (performer == null || performer.isEmpty()) && (status == null || status.isEmpty()) && (notes == null || notes.isEmpty())
           && (asNeeded == null || asNeeded.isEmpty()) && (orderedOn == null || orderedOn.isEmpty())
           && (orderer == null || orderer.isEmpty()) && (priority == null || priority.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.ProcedureRequest;
   }

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>A unique identifier of the Procedure Request</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ProcedureRequest.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="ProcedureRequest.identifier", description="A unique identifier of the Procedure Request", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>A unique identifier of the Procedure Request</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ProcedureRequest.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>performer</b>
   * <p>
   * Description: <b>Who should perform the procedure</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ProcedureRequest.performer</b><br>
   * </p>
   */
  @SearchParamDefinition(name="performer", path="ProcedureRequest.performer", description="Who should perform the procedure", type="reference" )
  public static final String SP_PERFORMER = "performer";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>performer</b>
   * <p>
   * Description: <b>Who should perform the procedure</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ProcedureRequest.performer</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PERFORMER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PERFORMER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ProcedureRequest:performer</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PERFORMER = new ca.uhn.fhir.model.api.Include("ProcedureRequest:performer").toLocked();

 /**
   * Search parameter: <b>subject</b>
   * <p>
   * Description: <b>Search by subject</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ProcedureRequest.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="subject", path="ProcedureRequest.subject", description="Search by subject", type="reference" )
  public static final String SP_SUBJECT = "subject";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>subject</b>
   * <p>
   * Description: <b>Search by subject</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ProcedureRequest.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SUBJECT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SUBJECT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ProcedureRequest:subject</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SUBJECT = new ca.uhn.fhir.model.api.Include("ProcedureRequest:subject").toLocked();

 /**
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>Search by subject - a patient</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ProcedureRequest.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="ProcedureRequest.subject", description="Search by subject - a patient", type="reference" )
  public static final String SP_PATIENT = "patient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patient</b>
   * <p>
   * Description: <b>Search by subject - a patient</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ProcedureRequest.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PATIENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PATIENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ProcedureRequest:patient</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PATIENT = new ca.uhn.fhir.model.api.Include("ProcedureRequest:patient").toLocked();

 /**
   * Search parameter: <b>orderer</b>
   * <p>
   * Description: <b>Who made request</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ProcedureRequest.orderer</b><br>
   * </p>
   */
  @SearchParamDefinition(name="orderer", path="ProcedureRequest.orderer", description="Who made request", type="reference" )
  public static final String SP_ORDERER = "orderer";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>orderer</b>
   * <p>
   * Description: <b>Who made request</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ProcedureRequest.orderer</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam ORDERER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_ORDERER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ProcedureRequest:orderer</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_ORDERER = new ca.uhn.fhir.model.api.Include("ProcedureRequest:orderer").toLocked();

 /**
   * Search parameter: <b>encounter</b>
   * <p>
   * Description: <b>Encounter request created during</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ProcedureRequest.encounter</b><br>
   * </p>
   */
  @SearchParamDefinition(name="encounter", path="ProcedureRequest.encounter", description="Encounter request created during", type="reference" )
  public static final String SP_ENCOUNTER = "encounter";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>encounter</b>
   * <p>
   * Description: <b>Encounter request created during</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ProcedureRequest.encounter</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam ENCOUNTER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_ENCOUNTER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ProcedureRequest:encounter</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_ENCOUNTER = new ca.uhn.fhir.model.api.Include("ProcedureRequest:encounter").toLocked();


}

