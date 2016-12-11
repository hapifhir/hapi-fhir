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
import java.util.Date;
import java.util.List;

import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseBackboneElement;

import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
/**
 * A request to convey information; e.g. the CDS system proposes that an alert be sent to a responsible provider, the CDS system proposes that the public health agency be notified about a reportable condition.
 */
@ResourceDef(name="CommunicationRequest", profile="http://hl7.org/fhir/Profile/CommunicationRequest")
public class CommunicationRequest extends DomainResource {

    public enum CommunicationRequestStatus {
        /**
         * The request has been proposed.
         */
        PROPOSED, 
        /**
         * The request has been planned.
         */
        PLANNED, 
        /**
         * The request has been placed.
         */
        REQUESTED, 
        /**
         * The receiving system has received the request but not yet decided whether it will be performed.
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
         * The work has been complete, the report(s) released, and no further work is planned.
         */
        COMPLETED, 
        /**
         * The request has been held by originating system/user request.
         */
        SUSPENDED, 
        /**
         * The receiving system has declined to fulfill the request
         */
        REJECTED, 
        /**
         * The communication was attempted, but due to some procedural error, it could not be completed.
         */
        FAILED, 
        /**
         * added to help the parsers
         */
        NULL;
        public static CommunicationRequestStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("proposed".equals(codeString))
          return PROPOSED;
        if ("planned".equals(codeString))
          return PLANNED;
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
        if ("failed".equals(codeString))
          return FAILED;
        throw new FHIRException("Unknown CommunicationRequestStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PROPOSED: return "proposed";
            case PLANNED: return "planned";
            case REQUESTED: return "requested";
            case RECEIVED: return "received";
            case ACCEPTED: return "accepted";
            case INPROGRESS: return "in-progress";
            case COMPLETED: return "completed";
            case SUSPENDED: return "suspended";
            case REJECTED: return "rejected";
            case FAILED: return "failed";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case PROPOSED: return "http://hl7.org/fhir/communication-request-status";
            case PLANNED: return "http://hl7.org/fhir/communication-request-status";
            case REQUESTED: return "http://hl7.org/fhir/communication-request-status";
            case RECEIVED: return "http://hl7.org/fhir/communication-request-status";
            case ACCEPTED: return "http://hl7.org/fhir/communication-request-status";
            case INPROGRESS: return "http://hl7.org/fhir/communication-request-status";
            case COMPLETED: return "http://hl7.org/fhir/communication-request-status";
            case SUSPENDED: return "http://hl7.org/fhir/communication-request-status";
            case REJECTED: return "http://hl7.org/fhir/communication-request-status";
            case FAILED: return "http://hl7.org/fhir/communication-request-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case PROPOSED: return "The request has been proposed.";
            case PLANNED: return "The request has been planned.";
            case REQUESTED: return "The request has been placed.";
            case RECEIVED: return "The receiving system has received the request but not yet decided whether it will be performed.";
            case ACCEPTED: return "The receiving system has accepted the order, but work has not yet commenced.";
            case INPROGRESS: return "The work to fulfill the order is happening.";
            case COMPLETED: return "The work has been complete, the report(s) released, and no further work is planned.";
            case SUSPENDED: return "The request has been held by originating system/user request.";
            case REJECTED: return "The receiving system has declined to fulfill the request";
            case FAILED: return "The communication was attempted, but due to some procedural error, it could not be completed.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PROPOSED: return "Proposed";
            case PLANNED: return "Planned";
            case REQUESTED: return "Requested";
            case RECEIVED: return "Received";
            case ACCEPTED: return "Accepted";
            case INPROGRESS: return "In Progress";
            case COMPLETED: return "Completed";
            case SUSPENDED: return "Suspended";
            case REJECTED: return "Rejected";
            case FAILED: return "Failed";
            default: return "?";
          }
        }
    }

  public static class CommunicationRequestStatusEnumFactory implements EnumFactory<CommunicationRequestStatus> {
    public CommunicationRequestStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("proposed".equals(codeString))
          return CommunicationRequestStatus.PROPOSED;
        if ("planned".equals(codeString))
          return CommunicationRequestStatus.PLANNED;
        if ("requested".equals(codeString))
          return CommunicationRequestStatus.REQUESTED;
        if ("received".equals(codeString))
          return CommunicationRequestStatus.RECEIVED;
        if ("accepted".equals(codeString))
          return CommunicationRequestStatus.ACCEPTED;
        if ("in-progress".equals(codeString))
          return CommunicationRequestStatus.INPROGRESS;
        if ("completed".equals(codeString))
          return CommunicationRequestStatus.COMPLETED;
        if ("suspended".equals(codeString))
          return CommunicationRequestStatus.SUSPENDED;
        if ("rejected".equals(codeString))
          return CommunicationRequestStatus.REJECTED;
        if ("failed".equals(codeString))
          return CommunicationRequestStatus.FAILED;
        throw new IllegalArgumentException("Unknown CommunicationRequestStatus code '"+codeString+"'");
        }
        public Enumeration<CommunicationRequestStatus> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("proposed".equals(codeString))
          return new Enumeration<CommunicationRequestStatus>(this, CommunicationRequestStatus.PROPOSED);
        if ("planned".equals(codeString))
          return new Enumeration<CommunicationRequestStatus>(this, CommunicationRequestStatus.PLANNED);
        if ("requested".equals(codeString))
          return new Enumeration<CommunicationRequestStatus>(this, CommunicationRequestStatus.REQUESTED);
        if ("received".equals(codeString))
          return new Enumeration<CommunicationRequestStatus>(this, CommunicationRequestStatus.RECEIVED);
        if ("accepted".equals(codeString))
          return new Enumeration<CommunicationRequestStatus>(this, CommunicationRequestStatus.ACCEPTED);
        if ("in-progress".equals(codeString))
          return new Enumeration<CommunicationRequestStatus>(this, CommunicationRequestStatus.INPROGRESS);
        if ("completed".equals(codeString))
          return new Enumeration<CommunicationRequestStatus>(this, CommunicationRequestStatus.COMPLETED);
        if ("suspended".equals(codeString))
          return new Enumeration<CommunicationRequestStatus>(this, CommunicationRequestStatus.SUSPENDED);
        if ("rejected".equals(codeString))
          return new Enumeration<CommunicationRequestStatus>(this, CommunicationRequestStatus.REJECTED);
        if ("failed".equals(codeString))
          return new Enumeration<CommunicationRequestStatus>(this, CommunicationRequestStatus.FAILED);
        throw new FHIRException("Unknown CommunicationRequestStatus code '"+codeString+"'");
        }
    public String toCode(CommunicationRequestStatus code) {
      if (code == CommunicationRequestStatus.PROPOSED)
        return "proposed";
      if (code == CommunicationRequestStatus.PLANNED)
        return "planned";
      if (code == CommunicationRequestStatus.REQUESTED)
        return "requested";
      if (code == CommunicationRequestStatus.RECEIVED)
        return "received";
      if (code == CommunicationRequestStatus.ACCEPTED)
        return "accepted";
      if (code == CommunicationRequestStatus.INPROGRESS)
        return "in-progress";
      if (code == CommunicationRequestStatus.COMPLETED)
        return "completed";
      if (code == CommunicationRequestStatus.SUSPENDED)
        return "suspended";
      if (code == CommunicationRequestStatus.REJECTED)
        return "rejected";
      if (code == CommunicationRequestStatus.FAILED)
        return "failed";
      return "?";
      }
    public String toSystem(CommunicationRequestStatus code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class CommunicationRequestPayloadComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The communicated content (or for multi-part communications, one portion of the communication).
         */
        @Child(name = "content", type = {StringType.class, Attachment.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Message part content", formalDefinition="The communicated content (or for multi-part communications, one portion of the communication)." )
        protected Type content;

        private static final long serialVersionUID = -1763459053L;

    /**
     * Constructor
     */
      public CommunicationRequestPayloadComponent() {
        super();
      }

    /**
     * Constructor
     */
      public CommunicationRequestPayloadComponent(Type content) {
        super();
        this.content = content;
      }

        /**
         * @return {@link #content} (The communicated content (or for multi-part communications, one portion of the communication).)
         */
        public Type getContent() { 
          return this.content;
        }

        /**
         * @return {@link #content} (The communicated content (or for multi-part communications, one portion of the communication).)
         */
        public StringType getContentStringType() throws FHIRException { 
          if (!(this.content instanceof StringType))
            throw new FHIRException("Type mismatch: the type StringType was expected, but "+this.content.getClass().getName()+" was encountered");
          return (StringType) this.content;
        }

        public boolean hasContentStringType() { 
          return this.content instanceof StringType;
        }

        /**
         * @return {@link #content} (The communicated content (or for multi-part communications, one portion of the communication).)
         */
        public Attachment getContentAttachment() throws FHIRException { 
          if (!(this.content instanceof Attachment))
            throw new FHIRException("Type mismatch: the type Attachment was expected, but "+this.content.getClass().getName()+" was encountered");
          return (Attachment) this.content;
        }

        public boolean hasContentAttachment() { 
          return this.content instanceof Attachment;
        }

        /**
         * @return {@link #content} (The communicated content (or for multi-part communications, one portion of the communication).)
         */
        public Reference getContentReference() throws FHIRException { 
          if (!(this.content instanceof Reference))
            throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.content.getClass().getName()+" was encountered");
          return (Reference) this.content;
        }

        public boolean hasContentReference() { 
          return this.content instanceof Reference;
        }

        public boolean hasContent() { 
          return this.content != null && !this.content.isEmpty();
        }

        /**
         * @param value {@link #content} (The communicated content (or for multi-part communications, one portion of the communication).)
         */
        public CommunicationRequestPayloadComponent setContent(Type value) { 
          this.content = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("content[x]", "string|Attachment|Reference(Any)", "The communicated content (or for multi-part communications, one portion of the communication).", 0, java.lang.Integer.MAX_VALUE, content));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 951530617: /*content*/ return this.content == null ? new Base[0] : new Base[] {this.content}; // Type
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 951530617: // content
          this.content = (Type) value; // Type
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("content[x]"))
          this.content = (Type) value; // Type
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 264548711:  return getContent(); // Type
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("contentString")) {
          this.content = new StringType();
          return this.content;
        }
        else if (name.equals("contentAttachment")) {
          this.content = new Attachment();
          return this.content;
        }
        else if (name.equals("contentReference")) {
          this.content = new Reference();
          return this.content;
        }
        else
          return super.addChild(name);
      }

      public CommunicationRequestPayloadComponent copy() {
        CommunicationRequestPayloadComponent dst = new CommunicationRequestPayloadComponent();
        copyValues(dst);
        dst.content = content == null ? null : content.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof CommunicationRequestPayloadComponent))
          return false;
        CommunicationRequestPayloadComponent o = (CommunicationRequestPayloadComponent) other;
        return compareDeep(content, o.content, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof CommunicationRequestPayloadComponent))
          return false;
        CommunicationRequestPayloadComponent o = (CommunicationRequestPayloadComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (content == null || content.isEmpty());
      }

  public String fhirType() {
    return "CommunicationRequest.payload";

  }

  }

    /**
     * A unique ID of this request for reference purposes. It must be provided if user wants it returned as part of any output, otherwise it will be autogenerated, if needed, by CDS system. Does not need to be the actual ID of the source system.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Unique identifier", formalDefinition="A unique ID of this request for reference purposes. It must be provided if user wants it returned as part of any output, otherwise it will be autogenerated, if needed, by CDS system. Does not need to be the actual ID of the source system." )
    protected List<Identifier> identifier;

    /**
     * The type of message to be sent such as alert, notification, reminder, instruction, etc.
     */
    @Child(name = "category", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Message category", formalDefinition="The type of message to be sent such as alert, notification, reminder, instruction, etc." )
    protected CodeableConcept category;

    /**
     * The entity (e.g. person, organization, clinical information system, or device) which is to be the source of the communication.
     */
    @Child(name = "sender", type = {Device.class, Organization.class, Patient.class, Practitioner.class, RelatedPerson.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Message sender", formalDefinition="The entity (e.g. person, organization, clinical information system, or device) which is to be the source of the communication." )
    protected Reference sender;

    /**
     * The actual object that is the target of the reference (The entity (e.g. person, organization, clinical information system, or device) which is to be the source of the communication.)
     */
    protected Resource senderTarget;

    /**
     * The entity (e.g. person, organization, clinical information system, or device) which is the intended target of the communication.
     */
    @Child(name = "recipient", type = {Device.class, Organization.class, Patient.class, Practitioner.class, RelatedPerson.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Message recipient", formalDefinition="The entity (e.g. person, organization, clinical information system, or device) which is the intended target of the communication." )
    protected List<Reference> recipient;
    /**
     * The actual objects that are the target of the reference (The entity (e.g. person, organization, clinical information system, or device) which is the intended target of the communication.)
     */
    protected List<Resource> recipientTarget;


    /**
     * Text, attachment(s), or resource(s) to be communicated to the recipient.
     */
    @Child(name = "payload", type = {}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Message payload", formalDefinition="Text, attachment(s), or resource(s) to be communicated to the recipient." )
    protected List<CommunicationRequestPayloadComponent> payload;

    /**
     * A channel that was used for this communication (e.g. email, fax).
     */
    @Child(name = "medium", type = {CodeableConcept.class}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="A channel of communication", formalDefinition="A channel that was used for this communication (e.g. email, fax)." )
    protected List<CodeableConcept> medium;

    /**
     * The responsible person who authorizes this order, e.g. physician. This may be different than the author of the order statement, e.g. clerk, who may have entered the statement into the order entry application.
     */
    @Child(name = "requester", type = {Practitioner.class, Patient.class, RelatedPerson.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="An individual who requested a communication", formalDefinition="The responsible person who authorizes this order, e.g. physician. This may be different than the author of the order statement, e.g. clerk, who may have entered the statement into the order entry application." )
    protected Reference requester;

    /**
     * The actual object that is the target of the reference (The responsible person who authorizes this order, e.g. physician. This may be different than the author of the order statement, e.g. clerk, who may have entered the statement into the order entry application.)
     */
    protected Resource requesterTarget;

    /**
     * The status of the proposal or order.
     */
    @Child(name = "status", type = {CodeType.class}, order=7, min=0, max=1, modifier=true, summary=true)
    @Description(shortDefinition="proposed | planned | requested | received | accepted | in-progress | completed | suspended | rejected | failed", formalDefinition="The status of the proposal or order." )
    protected Enumeration<CommunicationRequestStatus> status;

    /**
     * The encounter within which the communication request was created.
     */
    @Child(name = "encounter", type = {Encounter.class}, order=8, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Encounter leading to message", formalDefinition="The encounter within which the communication request was created." )
    protected Reference encounter;

    /**
     * The actual object that is the target of the reference (The encounter within which the communication request was created.)
     */
    protected Encounter encounterTarget;

    /**
     * The time when this communication is to occur.
     */
    @Child(name = "scheduled", type = {DateTimeType.class, Period.class}, order=9, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When scheduled", formalDefinition="The time when this communication is to occur." )
    protected Type scheduled;

    /**
     * The reason or justification for the communication request.
     */
    @Child(name = "reason", type = {CodeableConcept.class}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Indication for message", formalDefinition="The reason or justification for the communication request." )
    protected List<CodeableConcept> reason;

    /**
     * The time when the request was made.
     */
    @Child(name = "requestedOn", type = {DateTimeType.class}, order=11, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When ordered or proposed", formalDefinition="The time when the request was made." )
    protected DateTimeType requestedOn;

    /**
     * The patient who is the focus of this communication request.
     */
    @Child(name = "subject", type = {Patient.class}, order=12, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Focus of message", formalDefinition="The patient who is the focus of this communication request." )
    protected Reference subject;

    /**
     * The actual object that is the target of the reference (The patient who is the focus of this communication request.)
     */
    protected Patient subjectTarget;

    /**
     * Characterizes how quickly the proposed act must be initiated. Includes concepts such as stat, urgent, routine.
     */
    @Child(name = "priority", type = {CodeableConcept.class}, order=13, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Message urgency", formalDefinition="Characterizes how quickly the proposed act must be initiated. Includes concepts such as stat, urgent, routine." )
    protected CodeableConcept priority;

    private static final long serialVersionUID = 146906020L;

  /**
   * Constructor
   */
    public CommunicationRequest() {
      super();
    }

    /**
     * @return {@link #identifier} (A unique ID of this request for reference purposes. It must be provided if user wants it returned as part of any output, otherwise it will be autogenerated, if needed, by CDS system. Does not need to be the actual ID of the source system.)
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
     * @return {@link #identifier} (A unique ID of this request for reference purposes. It must be provided if user wants it returned as part of any output, otherwise it will be autogenerated, if needed, by CDS system. Does not need to be the actual ID of the source system.)
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
    public CommunicationRequest addIdentifier(Identifier t) { //3
      if (t == null)
        return this;
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return this;
    }

    /**
     * @return {@link #category} (The type of message to be sent such as alert, notification, reminder, instruction, etc.)
     */
    public CodeableConcept getCategory() { 
      if (this.category == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CommunicationRequest.category");
        else if (Configuration.doAutoCreate())
          this.category = new CodeableConcept(); // cc
      return this.category;
    }

    public boolean hasCategory() { 
      return this.category != null && !this.category.isEmpty();
    }

    /**
     * @param value {@link #category} (The type of message to be sent such as alert, notification, reminder, instruction, etc.)
     */
    public CommunicationRequest setCategory(CodeableConcept value) { 
      this.category = value;
      return this;
    }

    /**
     * @return {@link #sender} (The entity (e.g. person, organization, clinical information system, or device) which is to be the source of the communication.)
     */
    public Reference getSender() { 
      if (this.sender == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CommunicationRequest.sender");
        else if (Configuration.doAutoCreate())
          this.sender = new Reference(); // cc
      return this.sender;
    }

    public boolean hasSender() { 
      return this.sender != null && !this.sender.isEmpty();
    }

    /**
     * @param value {@link #sender} (The entity (e.g. person, organization, clinical information system, or device) which is to be the source of the communication.)
     */
    public CommunicationRequest setSender(Reference value) { 
      this.sender = value;
      return this;
    }

    /**
     * @return {@link #sender} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The entity (e.g. person, organization, clinical information system, or device) which is to be the source of the communication.)
     */
    public Resource getSenderTarget() { 
      return this.senderTarget;
    }

    /**
     * @param value {@link #sender} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The entity (e.g. person, organization, clinical information system, or device) which is to be the source of the communication.)
     */
    public CommunicationRequest setSenderTarget(Resource value) { 
      this.senderTarget = value;
      return this;
    }

    /**
     * @return {@link #recipient} (The entity (e.g. person, organization, clinical information system, or device) which is the intended target of the communication.)
     */
    public List<Reference> getRecipient() { 
      if (this.recipient == null)
        this.recipient = new ArrayList<Reference>();
      return this.recipient;
    }

    public boolean hasRecipient() { 
      if (this.recipient == null)
        return false;
      for (Reference item : this.recipient)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #recipient} (The entity (e.g. person, organization, clinical information system, or device) which is the intended target of the communication.)
     */
    // syntactic sugar
    public Reference addRecipient() { //3
      Reference t = new Reference();
      if (this.recipient == null)
        this.recipient = new ArrayList<Reference>();
      this.recipient.add(t);
      return t;
    }

    // syntactic sugar
    public CommunicationRequest addRecipient(Reference t) { //3
      if (t == null)
        return this;
      if (this.recipient == null)
        this.recipient = new ArrayList<Reference>();
      this.recipient.add(t);
      return this;
    }

    /**
     * @return {@link #recipient} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. The entity (e.g. person, organization, clinical information system, or device) which is the intended target of the communication.)
     */
    public List<Resource> getRecipientTarget() { 
      if (this.recipientTarget == null)
        this.recipientTarget = new ArrayList<Resource>();
      return this.recipientTarget;
    }

    /**
     * @return {@link #payload} (Text, attachment(s), or resource(s) to be communicated to the recipient.)
     */
    public List<CommunicationRequestPayloadComponent> getPayload() { 
      if (this.payload == null)
        this.payload = new ArrayList<CommunicationRequestPayloadComponent>();
      return this.payload;
    }

    public boolean hasPayload() { 
      if (this.payload == null)
        return false;
      for (CommunicationRequestPayloadComponent item : this.payload)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #payload} (Text, attachment(s), or resource(s) to be communicated to the recipient.)
     */
    // syntactic sugar
    public CommunicationRequestPayloadComponent addPayload() { //3
      CommunicationRequestPayloadComponent t = new CommunicationRequestPayloadComponent();
      if (this.payload == null)
        this.payload = new ArrayList<CommunicationRequestPayloadComponent>();
      this.payload.add(t);
      return t;
    }

    // syntactic sugar
    public CommunicationRequest addPayload(CommunicationRequestPayloadComponent t) { //3
      if (t == null)
        return this;
      if (this.payload == null)
        this.payload = new ArrayList<CommunicationRequestPayloadComponent>();
      this.payload.add(t);
      return this;
    }

    /**
     * @return {@link #medium} (A channel that was used for this communication (e.g. email, fax).)
     */
    public List<CodeableConcept> getMedium() { 
      if (this.medium == null)
        this.medium = new ArrayList<CodeableConcept>();
      return this.medium;
    }

    public boolean hasMedium() { 
      if (this.medium == null)
        return false;
      for (CodeableConcept item : this.medium)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #medium} (A channel that was used for this communication (e.g. email, fax).)
     */
    // syntactic sugar
    public CodeableConcept addMedium() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.medium == null)
        this.medium = new ArrayList<CodeableConcept>();
      this.medium.add(t);
      return t;
    }

    // syntactic sugar
    public CommunicationRequest addMedium(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.medium == null)
        this.medium = new ArrayList<CodeableConcept>();
      this.medium.add(t);
      return this;
    }

    /**
     * @return {@link #requester} (The responsible person who authorizes this order, e.g. physician. This may be different than the author of the order statement, e.g. clerk, who may have entered the statement into the order entry application.)
     */
    public Reference getRequester() { 
      if (this.requester == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CommunicationRequest.requester");
        else if (Configuration.doAutoCreate())
          this.requester = new Reference(); // cc
      return this.requester;
    }

    public boolean hasRequester() { 
      return this.requester != null && !this.requester.isEmpty();
    }

    /**
     * @param value {@link #requester} (The responsible person who authorizes this order, e.g. physician. This may be different than the author of the order statement, e.g. clerk, who may have entered the statement into the order entry application.)
     */
    public CommunicationRequest setRequester(Reference value) { 
      this.requester = value;
      return this;
    }

    /**
     * @return {@link #requester} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The responsible person who authorizes this order, e.g. physician. This may be different than the author of the order statement, e.g. clerk, who may have entered the statement into the order entry application.)
     */
    public Resource getRequesterTarget() { 
      return this.requesterTarget;
    }

    /**
     * @param value {@link #requester} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The responsible person who authorizes this order, e.g. physician. This may be different than the author of the order statement, e.g. clerk, who may have entered the statement into the order entry application.)
     */
    public CommunicationRequest setRequesterTarget(Resource value) { 
      this.requesterTarget = value;
      return this;
    }

    /**
     * @return {@link #status} (The status of the proposal or order.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<CommunicationRequestStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CommunicationRequest.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<CommunicationRequestStatus>(new CommunicationRequestStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (The status of the proposal or order.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public CommunicationRequest setStatusElement(Enumeration<CommunicationRequestStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The status of the proposal or order.
     */
    public CommunicationRequestStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The status of the proposal or order.
     */
    public CommunicationRequest setStatus(CommunicationRequestStatus value) { 
      if (value == null)
        this.status = null;
      else {
        if (this.status == null)
          this.status = new Enumeration<CommunicationRequestStatus>(new CommunicationRequestStatusEnumFactory());
        this.status.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #encounter} (The encounter within which the communication request was created.)
     */
    public Reference getEncounter() { 
      if (this.encounter == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CommunicationRequest.encounter");
        else if (Configuration.doAutoCreate())
          this.encounter = new Reference(); // cc
      return this.encounter;
    }

    public boolean hasEncounter() { 
      return this.encounter != null && !this.encounter.isEmpty();
    }

    /**
     * @param value {@link #encounter} (The encounter within which the communication request was created.)
     */
    public CommunicationRequest setEncounter(Reference value) { 
      this.encounter = value;
      return this;
    }

    /**
     * @return {@link #encounter} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The encounter within which the communication request was created.)
     */
    public Encounter getEncounterTarget() { 
      if (this.encounterTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CommunicationRequest.encounter");
        else if (Configuration.doAutoCreate())
          this.encounterTarget = new Encounter(); // aa
      return this.encounterTarget;
    }

    /**
     * @param value {@link #encounter} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The encounter within which the communication request was created.)
     */
    public CommunicationRequest setEncounterTarget(Encounter value) { 
      this.encounterTarget = value;
      return this;
    }

    /**
     * @return {@link #scheduled} (The time when this communication is to occur.)
     */
    public Type getScheduled() { 
      return this.scheduled;
    }

    /**
     * @return {@link #scheduled} (The time when this communication is to occur.)
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
     * @return {@link #scheduled} (The time when this communication is to occur.)
     */
    public Period getScheduledPeriod() throws FHIRException { 
      if (!(this.scheduled instanceof Period))
        throw new FHIRException("Type mismatch: the type Period was expected, but "+this.scheduled.getClass().getName()+" was encountered");
      return (Period) this.scheduled;
    }

    public boolean hasScheduledPeriod() { 
      return this.scheduled instanceof Period;
    }

    public boolean hasScheduled() { 
      return this.scheduled != null && !this.scheduled.isEmpty();
    }

    /**
     * @param value {@link #scheduled} (The time when this communication is to occur.)
     */
    public CommunicationRequest setScheduled(Type value) { 
      this.scheduled = value;
      return this;
    }

    /**
     * @return {@link #reason} (The reason or justification for the communication request.)
     */
    public List<CodeableConcept> getReason() { 
      if (this.reason == null)
        this.reason = new ArrayList<CodeableConcept>();
      return this.reason;
    }

    public boolean hasReason() { 
      if (this.reason == null)
        return false;
      for (CodeableConcept item : this.reason)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #reason} (The reason or justification for the communication request.)
     */
    // syntactic sugar
    public CodeableConcept addReason() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.reason == null)
        this.reason = new ArrayList<CodeableConcept>();
      this.reason.add(t);
      return t;
    }

    // syntactic sugar
    public CommunicationRequest addReason(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.reason == null)
        this.reason = new ArrayList<CodeableConcept>();
      this.reason.add(t);
      return this;
    }

    /**
     * @return {@link #requestedOn} (The time when the request was made.). This is the underlying object with id, value and extensions. The accessor "getRequestedOn" gives direct access to the value
     */
    public DateTimeType getRequestedOnElement() { 
      if (this.requestedOn == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CommunicationRequest.requestedOn");
        else if (Configuration.doAutoCreate())
          this.requestedOn = new DateTimeType(); // bb
      return this.requestedOn;
    }

    public boolean hasRequestedOnElement() { 
      return this.requestedOn != null && !this.requestedOn.isEmpty();
    }

    public boolean hasRequestedOn() { 
      return this.requestedOn != null && !this.requestedOn.isEmpty();
    }

    /**
     * @param value {@link #requestedOn} (The time when the request was made.). This is the underlying object with id, value and extensions. The accessor "getRequestedOn" gives direct access to the value
     */
    public CommunicationRequest setRequestedOnElement(DateTimeType value) { 
      this.requestedOn = value;
      return this;
    }

    /**
     * @return The time when the request was made.
     */
    public Date getRequestedOn() { 
      return this.requestedOn == null ? null : this.requestedOn.getValue();
    }

    /**
     * @param value The time when the request was made.
     */
    public CommunicationRequest setRequestedOn(Date value) { 
      if (value == null)
        this.requestedOn = null;
      else {
        if (this.requestedOn == null)
          this.requestedOn = new DateTimeType();
        this.requestedOn.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #subject} (The patient who is the focus of this communication request.)
     */
    public Reference getSubject() { 
      if (this.subject == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CommunicationRequest.subject");
        else if (Configuration.doAutoCreate())
          this.subject = new Reference(); // cc
      return this.subject;
    }

    public boolean hasSubject() { 
      return this.subject != null && !this.subject.isEmpty();
    }

    /**
     * @param value {@link #subject} (The patient who is the focus of this communication request.)
     */
    public CommunicationRequest setSubject(Reference value) { 
      this.subject = value;
      return this;
    }

    /**
     * @return {@link #subject} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The patient who is the focus of this communication request.)
     */
    public Patient getSubjectTarget() { 
      if (this.subjectTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CommunicationRequest.subject");
        else if (Configuration.doAutoCreate())
          this.subjectTarget = new Patient(); // aa
      return this.subjectTarget;
    }

    /**
     * @param value {@link #subject} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The patient who is the focus of this communication request.)
     */
    public CommunicationRequest setSubjectTarget(Patient value) { 
      this.subjectTarget = value;
      return this;
    }

    /**
     * @return {@link #priority} (Characterizes how quickly the proposed act must be initiated. Includes concepts such as stat, urgent, routine.)
     */
    public CodeableConcept getPriority() { 
      if (this.priority == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CommunicationRequest.priority");
        else if (Configuration.doAutoCreate())
          this.priority = new CodeableConcept(); // cc
      return this.priority;
    }

    public boolean hasPriority() { 
      return this.priority != null && !this.priority.isEmpty();
    }

    /**
     * @param value {@link #priority} (Characterizes how quickly the proposed act must be initiated. Includes concepts such as stat, urgent, routine.)
     */
    public CommunicationRequest setPriority(CodeableConcept value) { 
      this.priority = value;
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "A unique ID of this request for reference purposes. It must be provided if user wants it returned as part of any output, otherwise it will be autogenerated, if needed, by CDS system. Does not need to be the actual ID of the source system.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("category", "CodeableConcept", "The type of message to be sent such as alert, notification, reminder, instruction, etc.", 0, java.lang.Integer.MAX_VALUE, category));
        childrenList.add(new Property("sender", "Reference(Device|Organization|Patient|Practitioner|RelatedPerson)", "The entity (e.g. person, organization, clinical information system, or device) which is to be the source of the communication.", 0, java.lang.Integer.MAX_VALUE, sender));
        childrenList.add(new Property("recipient", "Reference(Device|Organization|Patient|Practitioner|RelatedPerson)", "The entity (e.g. person, organization, clinical information system, or device) which is the intended target of the communication.", 0, java.lang.Integer.MAX_VALUE, recipient));
        childrenList.add(new Property("payload", "", "Text, attachment(s), or resource(s) to be communicated to the recipient.", 0, java.lang.Integer.MAX_VALUE, payload));
        childrenList.add(new Property("medium", "CodeableConcept", "A channel that was used for this communication (e.g. email, fax).", 0, java.lang.Integer.MAX_VALUE, medium));
        childrenList.add(new Property("requester", "Reference(Practitioner|Patient|RelatedPerson)", "The responsible person who authorizes this order, e.g. physician. This may be different than the author of the order statement, e.g. clerk, who may have entered the statement into the order entry application.", 0, java.lang.Integer.MAX_VALUE, requester));
        childrenList.add(new Property("status", "code", "The status of the proposal or order.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("encounter", "Reference(Encounter)", "The encounter within which the communication request was created.", 0, java.lang.Integer.MAX_VALUE, encounter));
        childrenList.add(new Property("scheduled[x]", "dateTime|Period", "The time when this communication is to occur.", 0, java.lang.Integer.MAX_VALUE, scheduled));
        childrenList.add(new Property("reason", "CodeableConcept", "The reason or justification for the communication request.", 0, java.lang.Integer.MAX_VALUE, reason));
        childrenList.add(new Property("requestedOn", "dateTime", "The time when the request was made.", 0, java.lang.Integer.MAX_VALUE, requestedOn));
        childrenList.add(new Property("subject", "Reference(Patient)", "The patient who is the focus of this communication request.", 0, java.lang.Integer.MAX_VALUE, subject));
        childrenList.add(new Property("priority", "CodeableConcept", "Characterizes how quickly the proposed act must be initiated. Includes concepts such as stat, urgent, routine.", 0, java.lang.Integer.MAX_VALUE, priority));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case 50511102: /*category*/ return this.category == null ? new Base[0] : new Base[] {this.category}; // CodeableConcept
        case -905962955: /*sender*/ return this.sender == null ? new Base[0] : new Base[] {this.sender}; // Reference
        case 820081177: /*recipient*/ return this.recipient == null ? new Base[0] : this.recipient.toArray(new Base[this.recipient.size()]); // Reference
        case -786701938: /*payload*/ return this.payload == null ? new Base[0] : this.payload.toArray(new Base[this.payload.size()]); // CommunicationRequestPayloadComponent
        case -1078030475: /*medium*/ return this.medium == null ? new Base[0] : this.medium.toArray(new Base[this.medium.size()]); // CodeableConcept
        case 693933948: /*requester*/ return this.requester == null ? new Base[0] : new Base[] {this.requester}; // Reference
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<CommunicationRequestStatus>
        case 1524132147: /*encounter*/ return this.encounter == null ? new Base[0] : new Base[] {this.encounter}; // Reference
        case -160710483: /*scheduled*/ return this.scheduled == null ? new Base[0] : new Base[] {this.scheduled}; // Type
        case -934964668: /*reason*/ return this.reason == null ? new Base[0] : this.reason.toArray(new Base[this.reason.size()]); // CodeableConcept
        case 1150582253: /*requestedOn*/ return this.requestedOn == null ? new Base[0] : new Base[] {this.requestedOn}; // DateTimeType
        case -1867885268: /*subject*/ return this.subject == null ? new Base[0] : new Base[] {this.subject}; // Reference
        case -1165461084: /*priority*/ return this.priority == null ? new Base[0] : new Base[] {this.priority}; // CodeableConcept
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.getIdentifier().add(castToIdentifier(value)); // Identifier
          break;
        case 50511102: // category
          this.category = castToCodeableConcept(value); // CodeableConcept
          break;
        case -905962955: // sender
          this.sender = castToReference(value); // Reference
          break;
        case 820081177: // recipient
          this.getRecipient().add(castToReference(value)); // Reference
          break;
        case -786701938: // payload
          this.getPayload().add((CommunicationRequestPayloadComponent) value); // CommunicationRequestPayloadComponent
          break;
        case -1078030475: // medium
          this.getMedium().add(castToCodeableConcept(value)); // CodeableConcept
          break;
        case 693933948: // requester
          this.requester = castToReference(value); // Reference
          break;
        case -892481550: // status
          this.status = new CommunicationRequestStatusEnumFactory().fromType(value); // Enumeration<CommunicationRequestStatus>
          break;
        case 1524132147: // encounter
          this.encounter = castToReference(value); // Reference
          break;
        case -160710483: // scheduled
          this.scheduled = (Type) value; // Type
          break;
        case -934964668: // reason
          this.getReason().add(castToCodeableConcept(value)); // CodeableConcept
          break;
        case 1150582253: // requestedOn
          this.requestedOn = castToDateTime(value); // DateTimeType
          break;
        case -1867885268: // subject
          this.subject = castToReference(value); // Reference
          break;
        case -1165461084: // priority
          this.priority = castToCodeableConcept(value); // CodeableConcept
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier"))
          this.getIdentifier().add(castToIdentifier(value));
        else if (name.equals("category"))
          this.category = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("sender"))
          this.sender = castToReference(value); // Reference
        else if (name.equals("recipient"))
          this.getRecipient().add(castToReference(value));
        else if (name.equals("payload"))
          this.getPayload().add((CommunicationRequestPayloadComponent) value);
        else if (name.equals("medium"))
          this.getMedium().add(castToCodeableConcept(value));
        else if (name.equals("requester"))
          this.requester = castToReference(value); // Reference
        else if (name.equals("status"))
          this.status = new CommunicationRequestStatusEnumFactory().fromType(value); // Enumeration<CommunicationRequestStatus>
        else if (name.equals("encounter"))
          this.encounter = castToReference(value); // Reference
        else if (name.equals("scheduled[x]"))
          this.scheduled = (Type) value; // Type
        else if (name.equals("reason"))
          this.getReason().add(castToCodeableConcept(value));
        else if (name.equals("requestedOn"))
          this.requestedOn = castToDateTime(value); // DateTimeType
        else if (name.equals("subject"))
          this.subject = castToReference(value); // Reference
        else if (name.equals("priority"))
          this.priority = castToCodeableConcept(value); // CodeableConcept
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return addIdentifier(); // Identifier
        case 50511102:  return getCategory(); // CodeableConcept
        case -905962955:  return getSender(); // Reference
        case 820081177:  return addRecipient(); // Reference
        case -786701938:  return addPayload(); // CommunicationRequestPayloadComponent
        case -1078030475:  return addMedium(); // CodeableConcept
        case 693933948:  return getRequester(); // Reference
        case -892481550: throw new FHIRException("Cannot make property status as it is not a complex type"); // Enumeration<CommunicationRequestStatus>
        case 1524132147:  return getEncounter(); // Reference
        case 1162627251:  return getScheduled(); // Type
        case -934964668:  return addReason(); // CodeableConcept
        case 1150582253: throw new FHIRException("Cannot make property requestedOn as it is not a complex type"); // DateTimeType
        case -1867885268:  return getSubject(); // Reference
        case -1165461084:  return getPriority(); // CodeableConcept
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("category")) {
          this.category = new CodeableConcept();
          return this.category;
        }
        else if (name.equals("sender")) {
          this.sender = new Reference();
          return this.sender;
        }
        else if (name.equals("recipient")) {
          return addRecipient();
        }
        else if (name.equals("payload")) {
          return addPayload();
        }
        else if (name.equals("medium")) {
          return addMedium();
        }
        else if (name.equals("requester")) {
          this.requester = new Reference();
          return this.requester;
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type CommunicationRequest.status");
        }
        else if (name.equals("encounter")) {
          this.encounter = new Reference();
          return this.encounter;
        }
        else if (name.equals("scheduledDateTime")) {
          this.scheduled = new DateTimeType();
          return this.scheduled;
        }
        else if (name.equals("scheduledPeriod")) {
          this.scheduled = new Period();
          return this.scheduled;
        }
        else if (name.equals("reason")) {
          return addReason();
        }
        else if (name.equals("requestedOn")) {
          throw new FHIRException("Cannot call addChild on a primitive type CommunicationRequest.requestedOn");
        }
        else if (name.equals("subject")) {
          this.subject = new Reference();
          return this.subject;
        }
        else if (name.equals("priority")) {
          this.priority = new CodeableConcept();
          return this.priority;
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "CommunicationRequest";

  }

      public CommunicationRequest copy() {
        CommunicationRequest dst = new CommunicationRequest();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.category = category == null ? null : category.copy();
        dst.sender = sender == null ? null : sender.copy();
        if (recipient != null) {
          dst.recipient = new ArrayList<Reference>();
          for (Reference i : recipient)
            dst.recipient.add(i.copy());
        };
        if (payload != null) {
          dst.payload = new ArrayList<CommunicationRequestPayloadComponent>();
          for (CommunicationRequestPayloadComponent i : payload)
            dst.payload.add(i.copy());
        };
        if (medium != null) {
          dst.medium = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : medium)
            dst.medium.add(i.copy());
        };
        dst.requester = requester == null ? null : requester.copy();
        dst.status = status == null ? null : status.copy();
        dst.encounter = encounter == null ? null : encounter.copy();
        dst.scheduled = scheduled == null ? null : scheduled.copy();
        if (reason != null) {
          dst.reason = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : reason)
            dst.reason.add(i.copy());
        };
        dst.requestedOn = requestedOn == null ? null : requestedOn.copy();
        dst.subject = subject == null ? null : subject.copy();
        dst.priority = priority == null ? null : priority.copy();
        return dst;
      }

      protected CommunicationRequest typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof CommunicationRequest))
          return false;
        CommunicationRequest o = (CommunicationRequest) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(category, o.category, true) && compareDeep(sender, o.sender, true)
           && compareDeep(recipient, o.recipient, true) && compareDeep(payload, o.payload, true) && compareDeep(medium, o.medium, true)
           && compareDeep(requester, o.requester, true) && compareDeep(status, o.status, true) && compareDeep(encounter, o.encounter, true)
           && compareDeep(scheduled, o.scheduled, true) && compareDeep(reason, o.reason, true) && compareDeep(requestedOn, o.requestedOn, true)
           && compareDeep(subject, o.subject, true) && compareDeep(priority, o.priority, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof CommunicationRequest))
          return false;
        CommunicationRequest o = (CommunicationRequest) other;
        return compareValues(status, o.status, true) && compareValues(requestedOn, o.requestedOn, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (category == null || category.isEmpty())
           && (sender == null || sender.isEmpty()) && (recipient == null || recipient.isEmpty()) && (payload == null || payload.isEmpty())
           && (medium == null || medium.isEmpty()) && (requester == null || requester.isEmpty()) && (status == null || status.isEmpty())
           && (encounter == null || encounter.isEmpty()) && (scheduled == null || scheduled.isEmpty())
           && (reason == null || reason.isEmpty()) && (requestedOn == null || requestedOn.isEmpty())
           && (subject == null || subject.isEmpty()) && (priority == null || priority.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.CommunicationRequest;
   }

 /**
   * Search parameter: <b>sender</b>
   * <p>
   * Description: <b>Message sender</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>CommunicationRequest.sender</b><br>
   * </p>
   */
  @SearchParamDefinition(name="sender", path="CommunicationRequest.sender", description="Message sender", type="reference" )
  public static final String SP_SENDER = "sender";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>sender</b>
   * <p>
   * Description: <b>Message sender</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>CommunicationRequest.sender</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SENDER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SENDER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>CommunicationRequest:sender</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SENDER = new ca.uhn.fhir.model.api.Include("CommunicationRequest:sender").toLocked();

 /**
   * Search parameter: <b>requested</b>
   * <p>
   * Description: <b>When ordered or proposed</b><br>
   * Type: <b>date</b><br>
   * Path: <b>CommunicationRequest.requestedOn</b><br>
   * </p>
   */
  @SearchParamDefinition(name="requested", path="CommunicationRequest.requestedOn", description="When ordered or proposed", type="date" )
  public static final String SP_REQUESTED = "requested";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>requested</b>
   * <p>
   * Description: <b>When ordered or proposed</b><br>
   * Type: <b>date</b><br>
   * Path: <b>CommunicationRequest.requestedOn</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam REQUESTED = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_REQUESTED);

 /**
   * Search parameter: <b>time</b>
   * <p>
   * Description: <b>When scheduled</b><br>
   * Type: <b>date</b><br>
   * Path: <b>CommunicationRequest.scheduledDateTime</b><br>
   * </p>
   */
  @SearchParamDefinition(name="time", path="CommunicationRequest.scheduled.as(DateTime)", description="When scheduled", type="date" )
  public static final String SP_TIME = "time";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>time</b>
   * <p>
   * Description: <b>When scheduled</b><br>
   * Type: <b>date</b><br>
   * Path: <b>CommunicationRequest.scheduledDateTime</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam TIME = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_TIME);

 /**
   * Search parameter: <b>requester</b>
   * <p>
   * Description: <b>An individual who requested a communication</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>CommunicationRequest.requester</b><br>
   * </p>
   */
  @SearchParamDefinition(name="requester", path="CommunicationRequest.requester", description="An individual who requested a communication", type="reference" )
  public static final String SP_REQUESTER = "requester";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>requester</b>
   * <p>
   * Description: <b>An individual who requested a communication</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>CommunicationRequest.requester</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam REQUESTER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_REQUESTER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>CommunicationRequest:requester</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_REQUESTER = new ca.uhn.fhir.model.api.Include("CommunicationRequest:requester").toLocked();

 /**
   * Search parameter: <b>category</b>
   * <p>
   * Description: <b>Message category</b><br>
   * Type: <b>token</b><br>
   * Path: <b>CommunicationRequest.category</b><br>
   * </p>
   */
  @SearchParamDefinition(name="category", path="CommunicationRequest.category", description="Message category", type="token" )
  public static final String SP_CATEGORY = "category";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>category</b>
   * <p>
   * Description: <b>Message category</b><br>
   * Type: <b>token</b><br>
   * Path: <b>CommunicationRequest.category</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CATEGORY = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CATEGORY);

 /**
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>Focus of message</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>CommunicationRequest.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="CommunicationRequest.subject", description="Focus of message", type="reference" )
  public static final String SP_PATIENT = "patient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patient</b>
   * <p>
   * Description: <b>Focus of message</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>CommunicationRequest.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PATIENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PATIENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>CommunicationRequest:patient</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PATIENT = new ca.uhn.fhir.model.api.Include("CommunicationRequest:patient").toLocked();

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>proposed | planned | requested | received | accepted | in-progress | completed | suspended | rejected | failed</b><br>
   * Type: <b>token</b><br>
   * Path: <b>CommunicationRequest.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="CommunicationRequest.status", description="proposed | planned | requested | received | accepted | in-progress | completed | suspended | rejected | failed", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>proposed | planned | requested | received | accepted | in-progress | completed | suspended | rejected | failed</b><br>
   * Type: <b>token</b><br>
   * Path: <b>CommunicationRequest.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);

 /**
   * Search parameter: <b>priority</b>
   * <p>
   * Description: <b>Message urgency</b><br>
   * Type: <b>token</b><br>
   * Path: <b>CommunicationRequest.priority</b><br>
   * </p>
   */
  @SearchParamDefinition(name="priority", path="CommunicationRequest.priority", description="Message urgency", type="token" )
  public static final String SP_PRIORITY = "priority";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>priority</b>
   * <p>
   * Description: <b>Message urgency</b><br>
   * Type: <b>token</b><br>
   * Path: <b>CommunicationRequest.priority</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam PRIORITY = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_PRIORITY);

 /**
   * Search parameter: <b>subject</b>
   * <p>
   * Description: <b>Focus of message</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>CommunicationRequest.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="subject", path="CommunicationRequest.subject", description="Focus of message", type="reference" )
  public static final String SP_SUBJECT = "subject";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>subject</b>
   * <p>
   * Description: <b>Focus of message</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>CommunicationRequest.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SUBJECT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SUBJECT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>CommunicationRequest:subject</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SUBJECT = new ca.uhn.fhir.model.api.Include("CommunicationRequest:subject").toLocked();

 /**
   * Search parameter: <b>encounter</b>
   * <p>
   * Description: <b>Encounter leading to message</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>CommunicationRequest.encounter</b><br>
   * </p>
   */
  @SearchParamDefinition(name="encounter", path="CommunicationRequest.encounter", description="Encounter leading to message", type="reference" )
  public static final String SP_ENCOUNTER = "encounter";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>encounter</b>
   * <p>
   * Description: <b>Encounter leading to message</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>CommunicationRequest.encounter</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam ENCOUNTER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_ENCOUNTER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>CommunicationRequest:encounter</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_ENCOUNTER = new ca.uhn.fhir.model.api.Include("CommunicationRequest:encounter").toLocked();

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>Unique identifier</b><br>
   * Type: <b>token</b><br>
   * Path: <b>CommunicationRequest.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="CommunicationRequest.identifier", description="Unique identifier", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>Unique identifier</b><br>
   * Type: <b>token</b><br>
   * Path: <b>CommunicationRequest.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>medium</b>
   * <p>
   * Description: <b>A channel of communication</b><br>
   * Type: <b>token</b><br>
   * Path: <b>CommunicationRequest.medium</b><br>
   * </p>
   */
  @SearchParamDefinition(name="medium", path="CommunicationRequest.medium", description="A channel of communication", type="token" )
  public static final String SP_MEDIUM = "medium";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>medium</b>
   * <p>
   * Description: <b>A channel of communication</b><br>
   * Type: <b>token</b><br>
   * Path: <b>CommunicationRequest.medium</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam MEDIUM = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_MEDIUM);

 /**
   * Search parameter: <b>recipient</b>
   * <p>
   * Description: <b>Message recipient</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>CommunicationRequest.recipient</b><br>
   * </p>
   */
  @SearchParamDefinition(name="recipient", path="CommunicationRequest.recipient", description="Message recipient", type="reference" )
  public static final String SP_RECIPIENT = "recipient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>recipient</b>
   * <p>
   * Description: <b>Message recipient</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>CommunicationRequest.recipient</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam RECIPIENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_RECIPIENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>CommunicationRequest:recipient</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_RECIPIENT = new ca.uhn.fhir.model.api.Include("CommunicationRequest:recipient").toLocked();


}

