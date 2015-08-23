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
 * A request for a procedure to be performed. May be a proposal or an order.
 */
@ResourceDef(name="ProcedureRequest", profile="http://hl7.org/fhir/Profile/ProcedureRequest")
public class ProcedureRequest extends DomainResource {

    public enum ProcedureRequestStatus {
        /**
         * The request has been proposed
         */
        PROPOSED, 
        /**
         * The request is in preliminary form, prior to being requested
         */
        DRAFT, 
        /**
         * The request has been placed
         */
        REQUESTED, 
        /**
         * The receiving system has received the request but not yet decided whether it will be performed
         */
        RECEIVED, 
        /**
         * The receiving system has accepted the request, but work has not yet commenced
         */
        ACCEPTED, 
        /**
         * The work to fulfill the request is happening
         */
        INPROGRESS, 
        /**
         * The work has been complete, the report(s) released, and no further work is planned
         */
        COMPLETED, 
        /**
         * The request has been held by originating system/user request
         */
        SUSPENDED, 
        /**
         * The receiving system has declined to fulfill the request
         */
        REJECTED, 
        /**
         * The request was attempted, but due to some procedural error, it could not be completed
         */
        ABORTED, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ProcedureRequestStatus fromCode(String codeString) throws Exception {
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
        throw new Exception("Unknown ProcedureRequestStatus code '"+codeString+"'");
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
            case PROPOSED: return "The request has been proposed";
            case DRAFT: return "The request is in preliminary form, prior to being requested";
            case REQUESTED: return "The request has been placed";
            case RECEIVED: return "The receiving system has received the request but not yet decided whether it will be performed";
            case ACCEPTED: return "The receiving system has accepted the request, but work has not yet commenced";
            case INPROGRESS: return "The work to fulfill the request is happening";
            case COMPLETED: return "The work has been complete, the report(s) released, and no further work is planned";
            case SUSPENDED: return "The request has been held by originating system/user request";
            case REJECTED: return "The receiving system has declined to fulfill the request";
            case ABORTED: return "The request was attempted, but due to some procedural error, it could not be completed";
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
        public static ProcedureRequestPriority fromCode(String codeString) throws Exception {
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
        throw new Exception("Unknown ProcedureRequestPriority code '"+codeString+"'");
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
    }

    @Block()
    public static class ProcedureRequestBodySiteComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Indicates the site on the subject's body where the procedure should be performed ( i.e. the target sites).
         */
        @Child(name = "site", type = {CodeableConcept.class, BodySite.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Target body site", formalDefinition="Indicates the site on the subject's body where the procedure should be performed ( i.e. the target sites)." )
        protected Type site;

        private static final long serialVersionUID = 1429072605L;

    /*
     * Constructor
     */
      public ProcedureRequestBodySiteComponent() {
        super();
      }

    /*
     * Constructor
     */
      public ProcedureRequestBodySiteComponent(Type site) {
        super();
        this.site = site;
      }

        /**
         * @return {@link #site} (Indicates the site on the subject's body where the procedure should be performed ( i.e. the target sites).)
         */
        public Type getSite() { 
          return this.site;
        }

        /**
         * @return {@link #site} (Indicates the site on the subject's body where the procedure should be performed ( i.e. the target sites).)
         */
        public CodeableConcept getSiteCodeableConcept() throws Exception { 
          if (!(this.site instanceof CodeableConcept))
            throw new Exception("Type mismatch: the type CodeableConcept was expected, but "+this.site.getClass().getName()+" was encountered");
          return (CodeableConcept) this.site;
        }

        public boolean hasSiteCodeableConcept() throws Exception { 
          return this.site instanceof CodeableConcept;
        }

        /**
         * @return {@link #site} (Indicates the site on the subject's body where the procedure should be performed ( i.e. the target sites).)
         */
        public Reference getSiteReference() throws Exception { 
          if (!(this.site instanceof Reference))
            throw new Exception("Type mismatch: the type Reference was expected, but "+this.site.getClass().getName()+" was encountered");
          return (Reference) this.site;
        }

        public boolean hasSiteReference() throws Exception { 
          return this.site instanceof Reference;
        }

        public boolean hasSite() { 
          return this.site != null && !this.site.isEmpty();
        }

        /**
         * @param value {@link #site} (Indicates the site on the subject's body where the procedure should be performed ( i.e. the target sites).)
         */
        public ProcedureRequestBodySiteComponent setSite(Type value) { 
          this.site = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("site[x]", "CodeableConcept|Reference(BodySite)", "Indicates the site on the subject's body where the procedure should be performed ( i.e. the target sites).", 0, java.lang.Integer.MAX_VALUE, site));
        }

      public ProcedureRequestBodySiteComponent copy() {
        ProcedureRequestBodySiteComponent dst = new ProcedureRequestBodySiteComponent();
        copyValues(dst);
        dst.site = site == null ? null : site.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ProcedureRequestBodySiteComponent))
          return false;
        ProcedureRequestBodySiteComponent o = (ProcedureRequestBodySiteComponent) other;
        return compareDeep(site, o.site, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ProcedureRequestBodySiteComponent))
          return false;
        ProcedureRequestBodySiteComponent o = (ProcedureRequestBodySiteComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (site == null || site.isEmpty());
      }

  }

    /**
     * Identifiers assigned to this order by the order or by the receiver.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Identifier", formalDefinition="Identifiers assigned to this order by the order or by the receiver." )
    protected List<Identifier> identifier;

    /**
     * The patient who will receive the procedure.
     */
    @Child(name = "subject", type = {Patient.class}, order=1, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Subject", formalDefinition="The patient who will receive the procedure." )
    protected Reference subject;

    /**
     * The actual object that is the target of the reference (The patient who will receive the procedure.)
     */
    protected Patient subjectTarget;

    /**
     * The specific procedure that is ordered. Use text if the exact nature of the procedure can't be coded.
     */
    @Child(name = "type", type = {CodeableConcept.class}, order=2, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Procedure Type", formalDefinition="The specific procedure that is ordered. Use text if the exact nature of the procedure can't be coded." )
    protected CodeableConcept type;

    /**
     * Indicates the sites on the subject's body where the procedure should be performed ( i.e. the target sites).
     */
    @Child(name = "bodySite", type = {}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Target body sites", formalDefinition="Indicates the sites on the subject's body where the procedure should be performed ( i.e. the target sites)." )
    protected List<ProcedureRequestBodySiteComponent> bodySite;

    /**
     * The reason why the procedure is proposed or ordered. This procedure request may be motivated by a Condition for instance.
     */
    @Child(name = "indication", type = {CodeableConcept.class}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Indication", formalDefinition="The reason why the procedure is proposed or ordered. This procedure request may be motivated by a Condition for instance." )
    protected List<CodeableConcept> indication;

    /**
     * The timing schedule for the proposed or ordered procedure. The Schedule data type allows many different expressions, for example. "Every 8 hours"; "Three times a day"; "1/2 an hour before breakfast for 10 days from 23-Dec 2011:"; "15 Oct 2013, 17 Oct 2013 and 1 Nov 2013".
     */
    @Child(name = "timing", type = {DateTimeType.class, Period.class, Timing.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Procedure timing schedule", formalDefinition="The timing schedule for the proposed or ordered procedure. The Schedule data type allows many different expressions, for example. \"Every 8 hours\"; \"Three times a day\"; \"1/2 an hour before breakfast for 10 days from 23-Dec 2011:\"; \"15 Oct 2013, 17 Oct 2013 and 1 Nov 2013\"." )
    protected Type timing;

    /**
     * The encounter within which the procedure proposal or request was created.
     */
    @Child(name = "encounter", type = {Encounter.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Encounter", formalDefinition="The encounter within which the procedure proposal or request was created." )
    protected Reference encounter;

    /**
     * The actual object that is the target of the reference (The encounter within which the procedure proposal or request was created.)
     */
    protected Encounter encounterTarget;

    /**
     * E.g. surgeon, anaethetist, endoscopist.
     */
    @Child(name = "performer", type = {Practitioner.class, Organization.class, Patient.class, RelatedPerson.class}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Performer", formalDefinition="E.g. surgeon, anaethetist, endoscopist." )
    protected Reference performer;

    /**
     * The actual object that is the target of the reference (E.g. surgeon, anaethetist, endoscopist.)
     */
    protected Resource performerTarget;

    /**
     * The status of the order.
     */
    @Child(name = "status", type = {CodeType.class}, order=8, min=0, max=1, modifier=true, summary=true)
    @Description(shortDefinition="proposed | draft | requested | received | accepted | in-progress | completed | suspended | rejected | aborted", formalDefinition="The status of the order." )
    protected Enumeration<ProcedureRequestStatus> status;

    /**
     * Any other notes associated with this proposal or order - e.g., provider instructions.
     */
    @Child(name = "notes", type = {StringType.class}, order=9, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Notes", formalDefinition="Any other notes associated with this proposal or order - e.g., provider instructions." )
    protected List<StringType> notes;

    /**
     * If a CodeableConcept is present, it indicates the pre-condition for performing the procedure.
     */
    @Child(name = "asNeeded", type = {BooleanType.class, CodeableConcept.class}, order=10, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="PRN", formalDefinition="If a CodeableConcept is present, it indicates the pre-condition for performing the procedure." )
    protected Type asNeeded;

    /**
     * The time when the request was made.
     */
    @Child(name = "orderedOn", type = {DateTimeType.class}, order=11, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When Requested", formalDefinition="The time when the request was made." )
    protected DateTimeType orderedOn;

    /**
     * The healthcare professional responsible for proposing or ordering the procedure.
     */
    @Child(name = "orderer", type = {Practitioner.class, Patient.class, RelatedPerson.class, Device.class}, order=12, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Ordering Party", formalDefinition="The healthcare professional responsible for proposing or ordering the procedure." )
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

    private static final long serialVersionUID = -1687850759L;

  /*
   * Constructor
   */
    public ProcedureRequest() {
      super();
    }

  /*
   * Constructor
   */
    public ProcedureRequest(Reference subject, CodeableConcept type) {
      super();
      this.subject = subject;
      this.type = type;
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
     * @return {@link #subject} (The patient who will receive the procedure.)
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
     * @param value {@link #subject} (The patient who will receive the procedure.)
     */
    public ProcedureRequest setSubject(Reference value) { 
      this.subject = value;
      return this;
    }

    /**
     * @return {@link #subject} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The patient who will receive the procedure.)
     */
    public Patient getSubjectTarget() { 
      if (this.subjectTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ProcedureRequest.subject");
        else if (Configuration.doAutoCreate())
          this.subjectTarget = new Patient(); // aa
      return this.subjectTarget;
    }

    /**
     * @param value {@link #subject} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The patient who will receive the procedure.)
     */
    public ProcedureRequest setSubjectTarget(Patient value) { 
      this.subjectTarget = value;
      return this;
    }

    /**
     * @return {@link #type} (The specific procedure that is ordered. Use text if the exact nature of the procedure can't be coded.)
     */
    public CodeableConcept getType() { 
      if (this.type == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ProcedureRequest.type");
        else if (Configuration.doAutoCreate())
          this.type = new CodeableConcept(); // cc
      return this.type;
    }

    public boolean hasType() { 
      return this.type != null && !this.type.isEmpty();
    }

    /**
     * @param value {@link #type} (The specific procedure that is ordered. Use text if the exact nature of the procedure can't be coded.)
     */
    public ProcedureRequest setType(CodeableConcept value) { 
      this.type = value;
      return this;
    }

    /**
     * @return {@link #bodySite} (Indicates the sites on the subject's body where the procedure should be performed ( i.e. the target sites).)
     */
    public List<ProcedureRequestBodySiteComponent> getBodySite() { 
      if (this.bodySite == null)
        this.bodySite = new ArrayList<ProcedureRequestBodySiteComponent>();
      return this.bodySite;
    }

    public boolean hasBodySite() { 
      if (this.bodySite == null)
        return false;
      for (ProcedureRequestBodySiteComponent item : this.bodySite)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #bodySite} (Indicates the sites on the subject's body where the procedure should be performed ( i.e. the target sites).)
     */
    // syntactic sugar
    public ProcedureRequestBodySiteComponent addBodySite() { //3
      ProcedureRequestBodySiteComponent t = new ProcedureRequestBodySiteComponent();
      if (this.bodySite == null)
        this.bodySite = new ArrayList<ProcedureRequestBodySiteComponent>();
      this.bodySite.add(t);
      return t;
    }

    // syntactic sugar
    public ProcedureRequest addBodySite(ProcedureRequestBodySiteComponent t) { //3
      if (t == null)
        return this;
      if (this.bodySite == null)
        this.bodySite = new ArrayList<ProcedureRequestBodySiteComponent>();
      this.bodySite.add(t);
      return this;
    }

    /**
     * @return {@link #indication} (The reason why the procedure is proposed or ordered. This procedure request may be motivated by a Condition for instance.)
     */
    public List<CodeableConcept> getIndication() { 
      if (this.indication == null)
        this.indication = new ArrayList<CodeableConcept>();
      return this.indication;
    }

    public boolean hasIndication() { 
      if (this.indication == null)
        return false;
      for (CodeableConcept item : this.indication)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #indication} (The reason why the procedure is proposed or ordered. This procedure request may be motivated by a Condition for instance.)
     */
    // syntactic sugar
    public CodeableConcept addIndication() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.indication == null)
        this.indication = new ArrayList<CodeableConcept>();
      this.indication.add(t);
      return t;
    }

    // syntactic sugar
    public ProcedureRequest addIndication(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.indication == null)
        this.indication = new ArrayList<CodeableConcept>();
      this.indication.add(t);
      return this;
    }

    /**
     * @return {@link #timing} (The timing schedule for the proposed or ordered procedure. The Schedule data type allows many different expressions, for example. "Every 8 hours"; "Three times a day"; "1/2 an hour before breakfast for 10 days from 23-Dec 2011:"; "15 Oct 2013, 17 Oct 2013 and 1 Nov 2013".)
     */
    public Type getTiming() { 
      return this.timing;
    }

    /**
     * @return {@link #timing} (The timing schedule for the proposed or ordered procedure. The Schedule data type allows many different expressions, for example. "Every 8 hours"; "Three times a day"; "1/2 an hour before breakfast for 10 days from 23-Dec 2011:"; "15 Oct 2013, 17 Oct 2013 and 1 Nov 2013".)
     */
    public DateTimeType getTimingDateTimeType() throws Exception { 
      if (!(this.timing instanceof DateTimeType))
        throw new Exception("Type mismatch: the type DateTimeType was expected, but "+this.timing.getClass().getName()+" was encountered");
      return (DateTimeType) this.timing;
    }

    public boolean hasTimingDateTimeType() throws Exception { 
      return this.timing instanceof DateTimeType;
    }

    /**
     * @return {@link #timing} (The timing schedule for the proposed or ordered procedure. The Schedule data type allows many different expressions, for example. "Every 8 hours"; "Three times a day"; "1/2 an hour before breakfast for 10 days from 23-Dec 2011:"; "15 Oct 2013, 17 Oct 2013 and 1 Nov 2013".)
     */
    public Period getTimingPeriod() throws Exception { 
      if (!(this.timing instanceof Period))
        throw new Exception("Type mismatch: the type Period was expected, but "+this.timing.getClass().getName()+" was encountered");
      return (Period) this.timing;
    }

    public boolean hasTimingPeriod() throws Exception { 
      return this.timing instanceof Period;
    }

    /**
     * @return {@link #timing} (The timing schedule for the proposed or ordered procedure. The Schedule data type allows many different expressions, for example. "Every 8 hours"; "Three times a day"; "1/2 an hour before breakfast for 10 days from 23-Dec 2011:"; "15 Oct 2013, 17 Oct 2013 and 1 Nov 2013".)
     */
    public Timing getTimingTiming() throws Exception { 
      if (!(this.timing instanceof Timing))
        throw new Exception("Type mismatch: the type Timing was expected, but "+this.timing.getClass().getName()+" was encountered");
      return (Timing) this.timing;
    }

    public boolean hasTimingTiming() throws Exception { 
      return this.timing instanceof Timing;
    }

    public boolean hasTiming() { 
      return this.timing != null && !this.timing.isEmpty();
    }

    /**
     * @param value {@link #timing} (The timing schedule for the proposed or ordered procedure. The Schedule data type allows many different expressions, for example. "Every 8 hours"; "Three times a day"; "1/2 an hour before breakfast for 10 days from 23-Dec 2011:"; "15 Oct 2013, 17 Oct 2013 and 1 Nov 2013".)
     */
    public ProcedureRequest setTiming(Type value) { 
      this.timing = value;
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
     * @return {@link #performer} (E.g. surgeon, anaethetist, endoscopist.)
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
     * @param value {@link #performer} (E.g. surgeon, anaethetist, endoscopist.)
     */
    public ProcedureRequest setPerformer(Reference value) { 
      this.performer = value;
      return this;
    }

    /**
     * @return {@link #performer} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (E.g. surgeon, anaethetist, endoscopist.)
     */
    public Resource getPerformerTarget() { 
      return this.performerTarget;
    }

    /**
     * @param value {@link #performer} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (E.g. surgeon, anaethetist, endoscopist.)
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
     * @return {@link #notes} (Any other notes associated with this proposal or order - e.g., provider instructions.)
     */
    public List<StringType> getNotes() { 
      if (this.notes == null)
        this.notes = new ArrayList<StringType>();
      return this.notes;
    }

    public boolean hasNotes() { 
      if (this.notes == null)
        return false;
      for (StringType item : this.notes)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #notes} (Any other notes associated with this proposal or order - e.g., provider instructions.)
     */
    // syntactic sugar
    public StringType addNotesElement() {//2 
      StringType t = new StringType();
      if (this.notes == null)
        this.notes = new ArrayList<StringType>();
      this.notes.add(t);
      return t;
    }

    /**
     * @param value {@link #notes} (Any other notes associated with this proposal or order - e.g., provider instructions.)
     */
    public ProcedureRequest addNotes(String value) { //1
      StringType t = new StringType();
      t.setValue(value);
      if (this.notes == null)
        this.notes = new ArrayList<StringType>();
      this.notes.add(t);
      return this;
    }

    /**
     * @param value {@link #notes} (Any other notes associated with this proposal or order - e.g., provider instructions.)
     */
    public boolean hasNotes(String value) { 
      if (this.notes == null)
        return false;
      for (StringType v : this.notes)
        if (v.equals(value)) // string
          return true;
      return false;
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
    public BooleanType getAsNeededBooleanType() throws Exception { 
      if (!(this.asNeeded instanceof BooleanType))
        throw new Exception("Type mismatch: the type BooleanType was expected, but "+this.asNeeded.getClass().getName()+" was encountered");
      return (BooleanType) this.asNeeded;
    }

    public boolean hasAsNeededBooleanType() throws Exception { 
      return this.asNeeded instanceof BooleanType;
    }

    /**
     * @return {@link #asNeeded} (If a CodeableConcept is present, it indicates the pre-condition for performing the procedure.)
     */
    public CodeableConcept getAsNeededCodeableConcept() throws Exception { 
      if (!(this.asNeeded instanceof CodeableConcept))
        throw new Exception("Type mismatch: the type CodeableConcept was expected, but "+this.asNeeded.getClass().getName()+" was encountered");
      return (CodeableConcept) this.asNeeded;
    }

    public boolean hasAsNeededCodeableConcept() throws Exception { 
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
        childrenList.add(new Property("subject", "Reference(Patient)", "The patient who will receive the procedure.", 0, java.lang.Integer.MAX_VALUE, subject));
        childrenList.add(new Property("type", "CodeableConcept", "The specific procedure that is ordered. Use text if the exact nature of the procedure can't be coded.", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("bodySite", "", "Indicates the sites on the subject's body where the procedure should be performed ( i.e. the target sites).", 0, java.lang.Integer.MAX_VALUE, bodySite));
        childrenList.add(new Property("indication", "CodeableConcept", "The reason why the procedure is proposed or ordered. This procedure request may be motivated by a Condition for instance.", 0, java.lang.Integer.MAX_VALUE, indication));
        childrenList.add(new Property("timing[x]", "dateTime|Period|Timing", "The timing schedule for the proposed or ordered procedure. The Schedule data type allows many different expressions, for example. \"Every 8 hours\"; \"Three times a day\"; \"1/2 an hour before breakfast for 10 days from 23-Dec 2011:\"; \"15 Oct 2013, 17 Oct 2013 and 1 Nov 2013\".", 0, java.lang.Integer.MAX_VALUE, timing));
        childrenList.add(new Property("encounter", "Reference(Encounter)", "The encounter within which the procedure proposal or request was created.", 0, java.lang.Integer.MAX_VALUE, encounter));
        childrenList.add(new Property("performer", "Reference(Practitioner|Organization|Patient|RelatedPerson)", "E.g. surgeon, anaethetist, endoscopist.", 0, java.lang.Integer.MAX_VALUE, performer));
        childrenList.add(new Property("status", "code", "The status of the order.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("notes", "string", "Any other notes associated with this proposal or order - e.g., provider instructions.", 0, java.lang.Integer.MAX_VALUE, notes));
        childrenList.add(new Property("asNeeded[x]", "boolean|CodeableConcept", "If a CodeableConcept is present, it indicates the pre-condition for performing the procedure.", 0, java.lang.Integer.MAX_VALUE, asNeeded));
        childrenList.add(new Property("orderedOn", "dateTime", "The time when the request was made.", 0, java.lang.Integer.MAX_VALUE, orderedOn));
        childrenList.add(new Property("orderer", "Reference(Practitioner|Patient|RelatedPerson|Device)", "The healthcare professional responsible for proposing or ordering the procedure.", 0, java.lang.Integer.MAX_VALUE, orderer));
        childrenList.add(new Property("priority", "code", "The clinical priority associated with this order.", 0, java.lang.Integer.MAX_VALUE, priority));
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
        dst.type = type == null ? null : type.copy();
        if (bodySite != null) {
          dst.bodySite = new ArrayList<ProcedureRequestBodySiteComponent>();
          for (ProcedureRequestBodySiteComponent i : bodySite)
            dst.bodySite.add(i.copy());
        };
        if (indication != null) {
          dst.indication = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : indication)
            dst.indication.add(i.copy());
        };
        dst.timing = timing == null ? null : timing.copy();
        dst.encounter = encounter == null ? null : encounter.copy();
        dst.performer = performer == null ? null : performer.copy();
        dst.status = status == null ? null : status.copy();
        if (notes != null) {
          dst.notes = new ArrayList<StringType>();
          for (StringType i : notes)
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
        return compareDeep(identifier, o.identifier, true) && compareDeep(subject, o.subject, true) && compareDeep(type, o.type, true)
           && compareDeep(bodySite, o.bodySite, true) && compareDeep(indication, o.indication, true) && compareDeep(timing, o.timing, true)
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
        return compareValues(status, o.status, true) && compareValues(notes, o.notes, true) && compareValues(orderedOn, o.orderedOn, true)
           && compareValues(priority, o.priority, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (subject == null || subject.isEmpty())
           && (type == null || type.isEmpty()) && (bodySite == null || bodySite.isEmpty()) && (indication == null || indication.isEmpty())
           && (timing == null || timing.isEmpty()) && (encounter == null || encounter.isEmpty()) && (performer == null || performer.isEmpty())
           && (status == null || status.isEmpty()) && (notes == null || notes.isEmpty()) && (asNeeded == null || asNeeded.isEmpty())
           && (orderedOn == null || orderedOn.isEmpty()) && (orderer == null || orderer.isEmpty()) && (priority == null || priority.isEmpty())
          ;
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.ProcedureRequest;
   }

  @SearchParamDefinition(name="performer", path="ProcedureRequest.performer", description="Performer", type="reference" )
  public static final String SP_PERFORMER = "performer";
  @SearchParamDefinition(name="subject", path="ProcedureRequest.subject", description="Search by subject", type="reference" )
  public static final String SP_SUBJECT = "subject";
  @SearchParamDefinition(name="patient", path="ProcedureRequest.subject", description="Search by subject - a patient", type="reference" )
  public static final String SP_PATIENT = "patient";
  @SearchParamDefinition(name="orderer", path="ProcedureRequest.orderer", description="Ordering Party", type="reference" )
  public static final String SP_ORDERER = "orderer";
  @SearchParamDefinition(name="encounter", path="ProcedureRequest.encounter", description="Encounter", type="reference" )
  public static final String SP_ENCOUNTER = "encounter";

}

