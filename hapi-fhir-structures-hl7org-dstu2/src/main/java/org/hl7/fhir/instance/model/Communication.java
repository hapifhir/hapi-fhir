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

// Generated on Wed, Jul 13, 2016 05:32+1000 for FHIR v1.0.2
import java.util.*;

import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseBackboneElement;

import ca.uhn.fhir.model.api.annotation.*;
/**
 * An occurrence of information being transmitted; e.g. an alert that was sent to a responsible provider, a public health agency was notified about a reportable condition.
 */
@ResourceDef(name="Communication", profile="http://hl7.org/fhir/Profile/Communication")
public class Communication extends DomainResource {

    public enum CommunicationStatus {
        /**
         * The communication transmission is ongoing.
         */
        INPROGRESS, 
        /**
         * The message transmission is complete, i.e., delivered to the recipient's destination.
         */
        COMPLETED, 
        /**
         * The communication transmission has been held by originating system/user request.
         */
        SUSPENDED, 
        /**
         * The receiving system has declined to accept the message.
         */
        REJECTED, 
        /**
         * There was a failure in transmitting the message out.
         */
        FAILED, 
        /**
         * added to help the parsers
         */
        NULL;
        public static CommunicationStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
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
        throw new FHIRException("Unknown CommunicationStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
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
            case INPROGRESS: return "http://hl7.org/fhir/communication-status";
            case COMPLETED: return "http://hl7.org/fhir/communication-status";
            case SUSPENDED: return "http://hl7.org/fhir/communication-status";
            case REJECTED: return "http://hl7.org/fhir/communication-status";
            case FAILED: return "http://hl7.org/fhir/communication-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case INPROGRESS: return "The communication transmission is ongoing.";
            case COMPLETED: return "The message transmission is complete, i.e., delivered to the recipient's destination.";
            case SUSPENDED: return "The communication transmission has been held by originating system/user request.";
            case REJECTED: return "The receiving system has declined to accept the message.";
            case FAILED: return "There was a failure in transmitting the message out.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case INPROGRESS: return "In Progress";
            case COMPLETED: return "Completed";
            case SUSPENDED: return "Suspended";
            case REJECTED: return "Rejected";
            case FAILED: return "Failed";
            default: return "?";
          }
        }
    }

  public static class CommunicationStatusEnumFactory implements EnumFactory<CommunicationStatus> {
    public CommunicationStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("in-progress".equals(codeString))
          return CommunicationStatus.INPROGRESS;
        if ("completed".equals(codeString))
          return CommunicationStatus.COMPLETED;
        if ("suspended".equals(codeString))
          return CommunicationStatus.SUSPENDED;
        if ("rejected".equals(codeString))
          return CommunicationStatus.REJECTED;
        if ("failed".equals(codeString))
          return CommunicationStatus.FAILED;
        throw new IllegalArgumentException("Unknown CommunicationStatus code '"+codeString+"'");
        }
        public Enumeration<CommunicationStatus> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("in-progress".equals(codeString))
          return new Enumeration<CommunicationStatus>(this, CommunicationStatus.INPROGRESS);
        if ("completed".equals(codeString))
          return new Enumeration<CommunicationStatus>(this, CommunicationStatus.COMPLETED);
        if ("suspended".equals(codeString))
          return new Enumeration<CommunicationStatus>(this, CommunicationStatus.SUSPENDED);
        if ("rejected".equals(codeString))
          return new Enumeration<CommunicationStatus>(this, CommunicationStatus.REJECTED);
        if ("failed".equals(codeString))
          return new Enumeration<CommunicationStatus>(this, CommunicationStatus.FAILED);
        throw new FHIRException("Unknown CommunicationStatus code '"+codeString+"'");
        }
    public String toCode(CommunicationStatus code) {
      if (code == CommunicationStatus.INPROGRESS)
        return "in-progress";
      if (code == CommunicationStatus.COMPLETED)
        return "completed";
      if (code == CommunicationStatus.SUSPENDED)
        return "suspended";
      if (code == CommunicationStatus.REJECTED)
        return "rejected";
      if (code == CommunicationStatus.FAILED)
        return "failed";
      return "?";
      }
    }

    @Block()
    public static class CommunicationPayloadComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A communicated content (or for multi-part communications, one portion of the communication).
         */
        @Child(name = "content", type = {StringType.class, Attachment.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Message part content", formalDefinition="A communicated content (or for multi-part communications, one portion of the communication)." )
        protected Type content;

        private static final long serialVersionUID = -1763459053L;

    /*
     * Constructor
     */
      public CommunicationPayloadComponent() {
        super();
      }

    /*
     * Constructor
     */
      public CommunicationPayloadComponent(Type content) {
        super();
        this.content = content;
      }

        /**
         * @return {@link #content} (A communicated content (or for multi-part communications, one portion of the communication).)
         */
        public Type getContent() { 
          return this.content;
        }

        /**
         * @return {@link #content} (A communicated content (or for multi-part communications, one portion of the communication).)
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
         * @return {@link #content} (A communicated content (or for multi-part communications, one portion of the communication).)
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
         * @return {@link #content} (A communicated content (or for multi-part communications, one portion of the communication).)
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
         * @param value {@link #content} (A communicated content (or for multi-part communications, one portion of the communication).)
         */
        public CommunicationPayloadComponent setContent(Type value) { 
          this.content = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("content[x]", "string|Attachment|Reference(Any)", "A communicated content (or for multi-part communications, one portion of the communication).", 0, java.lang.Integer.MAX_VALUE, content));
        }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("content[x]"))
          this.content = (Type) value; // Type
        else
          super.setProperty(name, value);
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

      public CommunicationPayloadComponent copy() {
        CommunicationPayloadComponent dst = new CommunicationPayloadComponent();
        copyValues(dst);
        dst.content = content == null ? null : content.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof CommunicationPayloadComponent))
          return false;
        CommunicationPayloadComponent o = (CommunicationPayloadComponent) other;
        return compareDeep(content, o.content, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof CommunicationPayloadComponent))
          return false;
        CommunicationPayloadComponent o = (CommunicationPayloadComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (content == null || content.isEmpty());
      }

  public String fhirType() {
    return "Communication.payload";

  }

  }

    /**
     * Identifiers associated with this Communication that are defined by business processes and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation).
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Unique identifier", formalDefinition="Identifiers associated with this Communication that are defined by business processes and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)." )
    protected List<Identifier> identifier;

    /**
     * The type of message conveyed such as alert, notification, reminder, instruction, etc.
     */
    @Child(name = "category", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Message category", formalDefinition="The type of message conveyed such as alert, notification, reminder, instruction, etc." )
    protected CodeableConcept category;

    /**
     * The entity (e.g. person, organization, clinical information system, or device) which was the source of the communication.
     */
    @Child(name = "sender", type = {Device.class, Organization.class, Patient.class, Practitioner.class, RelatedPerson.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Message sender", formalDefinition="The entity (e.g. person, organization, clinical information system, or device) which was the source of the communication." )
    protected Reference sender;

    /**
     * The actual object that is the target of the reference (The entity (e.g. person, organization, clinical information system, or device) which was the source of the communication.)
     */
    protected Resource senderTarget;

    /**
     * The entity (e.g. person, organization, clinical information system, or device) which was the target of the communication. If receipts need to be tracked by individual, a separate resource instance will need to be created for each recipient.  Multiple recipient communications are intended where either a receipt(s) is not tracked (e.g. a mass mail-out) or is captured in aggregate (all emails confirmed received by a particular time).
     */
    @Child(name = "recipient", type = {Device.class, Organization.class, Patient.class, Practitioner.class, RelatedPerson.class, Group.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Message recipient", formalDefinition="The entity (e.g. person, organization, clinical information system, or device) which was the target of the communication. If receipts need to be tracked by individual, a separate resource instance will need to be created for each recipient.  Multiple recipient communications are intended where either a receipt(s) is not tracked (e.g. a mass mail-out) or is captured in aggregate (all emails confirmed received by a particular time)." )
    protected List<Reference> recipient;
    /**
     * The actual objects that are the target of the reference (The entity (e.g. person, organization, clinical information system, or device) which was the target of the communication. If receipts need to be tracked by individual, a separate resource instance will need to be created for each recipient.  Multiple recipient communications are intended where either a receipt(s) is not tracked (e.g. a mass mail-out) or is captured in aggregate (all emails confirmed received by a particular time).)
     */
    protected List<Resource> recipientTarget;


    /**
     * Text, attachment(s), or resource(s) that was communicated to the recipient.
     */
    @Child(name = "payload", type = {}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Message payload", formalDefinition="Text, attachment(s), or resource(s) that was communicated to the recipient." )
    protected List<CommunicationPayloadComponent> payload;

    /**
     * A channel that was used for this communication (e.g. email, fax).
     */
    @Child(name = "medium", type = {CodeableConcept.class}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="A channel of communication", formalDefinition="A channel that was used for this communication (e.g. email, fax)." )
    protected List<CodeableConcept> medium;

    /**
     * The status of the transmission.
     */
    @Child(name = "status", type = {CodeType.class}, order=6, min=0, max=1, modifier=true, summary=true)
    @Description(shortDefinition="in-progress | completed | suspended | rejected | failed", formalDefinition="The status of the transmission." )
    protected Enumeration<CommunicationStatus> status;

    /**
     * The encounter within which the communication was sent.
     */
    @Child(name = "encounter", type = {Encounter.class}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Encounter leading to message", formalDefinition="The encounter within which the communication was sent." )
    protected Reference encounter;

    /**
     * The actual object that is the target of the reference (The encounter within which the communication was sent.)
     */
    protected Encounter encounterTarget;

    /**
     * The time when this communication was sent.
     */
    @Child(name = "sent", type = {DateTimeType.class}, order=8, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When sent", formalDefinition="The time when this communication was sent." )
    protected DateTimeType sent;

    /**
     * The time when this communication arrived at the destination.
     */
    @Child(name = "received", type = {DateTimeType.class}, order=9, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When received", formalDefinition="The time when this communication arrived at the destination." )
    protected DateTimeType received;

    /**
     * The reason or justification for the communication.
     */
    @Child(name = "reason", type = {CodeableConcept.class}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Indication for message", formalDefinition="The reason or justification for the communication." )
    protected List<CodeableConcept> reason;

    /**
     * The patient who was the focus of this communication.
     */
    @Child(name = "subject", type = {Patient.class}, order=11, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Focus of message", formalDefinition="The patient who was the focus of this communication." )
    protected Reference subject;

    /**
     * The actual object that is the target of the reference (The patient who was the focus of this communication.)
     */
    protected Patient subjectTarget;

    /**
     * The communication request that was responsible for producing this communication.
     */
    @Child(name = "requestDetail", type = {CommunicationRequest.class}, order=12, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="CommunicationRequest producing this message", formalDefinition="The communication request that was responsible for producing this communication." )
    protected Reference requestDetail;

    /**
     * The actual object that is the target of the reference (The communication request that was responsible for producing this communication.)
     */
    protected CommunicationRequest requestDetailTarget;

    private static final long serialVersionUID = -1654449146L;

  /*
   * Constructor
   */
    public Communication() {
      super();
    }

    /**
     * @return {@link #identifier} (Identifiers associated with this Communication that are defined by business processes and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation).)
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
     * @return {@link #identifier} (Identifiers associated with this Communication that are defined by business processes and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation).)
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
    public Communication addIdentifier(Identifier t) { //3
      if (t == null)
        return this;
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return this;
    }

    /**
     * @return {@link #category} (The type of message conveyed such as alert, notification, reminder, instruction, etc.)
     */
    public CodeableConcept getCategory() { 
      if (this.category == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Communication.category");
        else if (Configuration.doAutoCreate())
          this.category = new CodeableConcept(); // cc
      return this.category;
    }

    public boolean hasCategory() { 
      return this.category != null && !this.category.isEmpty();
    }

    /**
     * @param value {@link #category} (The type of message conveyed such as alert, notification, reminder, instruction, etc.)
     */
    public Communication setCategory(CodeableConcept value) { 
      this.category = value;
      return this;
    }

    /**
     * @return {@link #sender} (The entity (e.g. person, organization, clinical information system, or device) which was the source of the communication.)
     */
    public Reference getSender() { 
      if (this.sender == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Communication.sender");
        else if (Configuration.doAutoCreate())
          this.sender = new Reference(); // cc
      return this.sender;
    }

    public boolean hasSender() { 
      return this.sender != null && !this.sender.isEmpty();
    }

    /**
     * @param value {@link #sender} (The entity (e.g. person, organization, clinical information system, or device) which was the source of the communication.)
     */
    public Communication setSender(Reference value) { 
      this.sender = value;
      return this;
    }

    /**
     * @return {@link #sender} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The entity (e.g. person, organization, clinical information system, or device) which was the source of the communication.)
     */
    public Resource getSenderTarget() { 
      return this.senderTarget;
    }

    /**
     * @param value {@link #sender} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The entity (e.g. person, organization, clinical information system, or device) which was the source of the communication.)
     */
    public Communication setSenderTarget(Resource value) { 
      this.senderTarget = value;
      return this;
    }

    /**
     * @return {@link #recipient} (The entity (e.g. person, organization, clinical information system, or device) which was the target of the communication. If receipts need to be tracked by individual, a separate resource instance will need to be created for each recipient.  Multiple recipient communications are intended where either a receipt(s) is not tracked (e.g. a mass mail-out) or is captured in aggregate (all emails confirmed received by a particular time).)
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
     * @return {@link #recipient} (The entity (e.g. person, organization, clinical information system, or device) which was the target of the communication. If receipts need to be tracked by individual, a separate resource instance will need to be created for each recipient.  Multiple recipient communications are intended where either a receipt(s) is not tracked (e.g. a mass mail-out) or is captured in aggregate (all emails confirmed received by a particular time).)
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
    public Communication addRecipient(Reference t) { //3
      if (t == null)
        return this;
      if (this.recipient == null)
        this.recipient = new ArrayList<Reference>();
      this.recipient.add(t);
      return this;
    }

    /**
     * @return {@link #recipient} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. The entity (e.g. person, organization, clinical information system, or device) which was the target of the communication. If receipts need to be tracked by individual, a separate resource instance will need to be created for each recipient.  Multiple recipient communications are intended where either a receipt(s) is not tracked (e.g. a mass mail-out) or is captured in aggregate (all emails confirmed received by a particular time).)
     */
    public List<Resource> getRecipientTarget() { 
      if (this.recipientTarget == null)
        this.recipientTarget = new ArrayList<Resource>();
      return this.recipientTarget;
    }

    /**
     * @return {@link #payload} (Text, attachment(s), or resource(s) that was communicated to the recipient.)
     */
    public List<CommunicationPayloadComponent> getPayload() { 
      if (this.payload == null)
        this.payload = new ArrayList<CommunicationPayloadComponent>();
      return this.payload;
    }

    public boolean hasPayload() { 
      if (this.payload == null)
        return false;
      for (CommunicationPayloadComponent item : this.payload)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #payload} (Text, attachment(s), or resource(s) that was communicated to the recipient.)
     */
    // syntactic sugar
    public CommunicationPayloadComponent addPayload() { //3
      CommunicationPayloadComponent t = new CommunicationPayloadComponent();
      if (this.payload == null)
        this.payload = new ArrayList<CommunicationPayloadComponent>();
      this.payload.add(t);
      return t;
    }

    // syntactic sugar
    public Communication addPayload(CommunicationPayloadComponent t) { //3
      if (t == null)
        return this;
      if (this.payload == null)
        this.payload = new ArrayList<CommunicationPayloadComponent>();
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
    public Communication addMedium(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.medium == null)
        this.medium = new ArrayList<CodeableConcept>();
      this.medium.add(t);
      return this;
    }

    /**
     * @return {@link #status} (The status of the transmission.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<CommunicationStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Communication.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<CommunicationStatus>(new CommunicationStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (The status of the transmission.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Communication setStatusElement(Enumeration<CommunicationStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The status of the transmission.
     */
    public CommunicationStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The status of the transmission.
     */
    public Communication setStatus(CommunicationStatus value) { 
      if (value == null)
        this.status = null;
      else {
        if (this.status == null)
          this.status = new Enumeration<CommunicationStatus>(new CommunicationStatusEnumFactory());
        this.status.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #encounter} (The encounter within which the communication was sent.)
     */
    public Reference getEncounter() { 
      if (this.encounter == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Communication.encounter");
        else if (Configuration.doAutoCreate())
          this.encounter = new Reference(); // cc
      return this.encounter;
    }

    public boolean hasEncounter() { 
      return this.encounter != null && !this.encounter.isEmpty();
    }

    /**
     * @param value {@link #encounter} (The encounter within which the communication was sent.)
     */
    public Communication setEncounter(Reference value) { 
      this.encounter = value;
      return this;
    }

    /**
     * @return {@link #encounter} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The encounter within which the communication was sent.)
     */
    public Encounter getEncounterTarget() { 
      if (this.encounterTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Communication.encounter");
        else if (Configuration.doAutoCreate())
          this.encounterTarget = new Encounter(); // aa
      return this.encounterTarget;
    }

    /**
     * @param value {@link #encounter} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The encounter within which the communication was sent.)
     */
    public Communication setEncounterTarget(Encounter value) { 
      this.encounterTarget = value;
      return this;
    }

    /**
     * @return {@link #sent} (The time when this communication was sent.). This is the underlying object with id, value and extensions. The accessor "getSent" gives direct access to the value
     */
    public DateTimeType getSentElement() { 
      if (this.sent == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Communication.sent");
        else if (Configuration.doAutoCreate())
          this.sent = new DateTimeType(); // bb
      return this.sent;
    }

    public boolean hasSentElement() { 
      return this.sent != null && !this.sent.isEmpty();
    }

    public boolean hasSent() { 
      return this.sent != null && !this.sent.isEmpty();
    }

    /**
     * @param value {@link #sent} (The time when this communication was sent.). This is the underlying object with id, value and extensions. The accessor "getSent" gives direct access to the value
     */
    public Communication setSentElement(DateTimeType value) { 
      this.sent = value;
      return this;
    }

    /**
     * @return The time when this communication was sent.
     */
    public Date getSent() { 
      return this.sent == null ? null : this.sent.getValue();
    }

    /**
     * @param value The time when this communication was sent.
     */
    public Communication setSent(Date value) { 
      if (value == null)
        this.sent = null;
      else {
        if (this.sent == null)
          this.sent = new DateTimeType();
        this.sent.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #received} (The time when this communication arrived at the destination.). This is the underlying object with id, value and extensions. The accessor "getReceived" gives direct access to the value
     */
    public DateTimeType getReceivedElement() { 
      if (this.received == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Communication.received");
        else if (Configuration.doAutoCreate())
          this.received = new DateTimeType(); // bb
      return this.received;
    }

    public boolean hasReceivedElement() { 
      return this.received != null && !this.received.isEmpty();
    }

    public boolean hasReceived() { 
      return this.received != null && !this.received.isEmpty();
    }

    /**
     * @param value {@link #received} (The time when this communication arrived at the destination.). This is the underlying object with id, value and extensions. The accessor "getReceived" gives direct access to the value
     */
    public Communication setReceivedElement(DateTimeType value) { 
      this.received = value;
      return this;
    }

    /**
     * @return The time when this communication arrived at the destination.
     */
    public Date getReceived() { 
      return this.received == null ? null : this.received.getValue();
    }

    /**
     * @param value The time when this communication arrived at the destination.
     */
    public Communication setReceived(Date value) { 
      if (value == null)
        this.received = null;
      else {
        if (this.received == null)
          this.received = new DateTimeType();
        this.received.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #reason} (The reason or justification for the communication.)
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
     * @return {@link #reason} (The reason or justification for the communication.)
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
    public Communication addReason(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.reason == null)
        this.reason = new ArrayList<CodeableConcept>();
      this.reason.add(t);
      return this;
    }

    /**
     * @return {@link #subject} (The patient who was the focus of this communication.)
     */
    public Reference getSubject() { 
      if (this.subject == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Communication.subject");
        else if (Configuration.doAutoCreate())
          this.subject = new Reference(); // cc
      return this.subject;
    }

    public boolean hasSubject() { 
      return this.subject != null && !this.subject.isEmpty();
    }

    /**
     * @param value {@link #subject} (The patient who was the focus of this communication.)
     */
    public Communication setSubject(Reference value) { 
      this.subject = value;
      return this;
    }

    /**
     * @return {@link #subject} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The patient who was the focus of this communication.)
     */
    public Patient getSubjectTarget() { 
      if (this.subjectTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Communication.subject");
        else if (Configuration.doAutoCreate())
          this.subjectTarget = new Patient(); // aa
      return this.subjectTarget;
    }

    /**
     * @param value {@link #subject} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The patient who was the focus of this communication.)
     */
    public Communication setSubjectTarget(Patient value) { 
      this.subjectTarget = value;
      return this;
    }

    /**
     * @return {@link #requestDetail} (The communication request that was responsible for producing this communication.)
     */
    public Reference getRequestDetail() { 
      if (this.requestDetail == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Communication.requestDetail");
        else if (Configuration.doAutoCreate())
          this.requestDetail = new Reference(); // cc
      return this.requestDetail;
    }

    public boolean hasRequestDetail() { 
      return this.requestDetail != null && !this.requestDetail.isEmpty();
    }

    /**
     * @param value {@link #requestDetail} (The communication request that was responsible for producing this communication.)
     */
    public Communication setRequestDetail(Reference value) { 
      this.requestDetail = value;
      return this;
    }

    /**
     * @return {@link #requestDetail} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The communication request that was responsible for producing this communication.)
     */
    public CommunicationRequest getRequestDetailTarget() { 
      if (this.requestDetailTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Communication.requestDetail");
        else if (Configuration.doAutoCreate())
          this.requestDetailTarget = new CommunicationRequest(); // aa
      return this.requestDetailTarget;
    }

    /**
     * @param value {@link #requestDetail} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The communication request that was responsible for producing this communication.)
     */
    public Communication setRequestDetailTarget(CommunicationRequest value) { 
      this.requestDetailTarget = value;
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "Identifiers associated with this Communication that are defined by business processes and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation).", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("category", "CodeableConcept", "The type of message conveyed such as alert, notification, reminder, instruction, etc.", 0, java.lang.Integer.MAX_VALUE, category));
        childrenList.add(new Property("sender", "Reference(Device|Organization|Patient|Practitioner|RelatedPerson)", "The entity (e.g. person, organization, clinical information system, or device) which was the source of the communication.", 0, java.lang.Integer.MAX_VALUE, sender));
        childrenList.add(new Property("recipient", "Reference(Device|Organization|Patient|Practitioner|RelatedPerson|Group)", "The entity (e.g. person, organization, clinical information system, or device) which was the target of the communication. If receipts need to be tracked by individual, a separate resource instance will need to be created for each recipient.  Multiple recipient communications are intended where either a receipt(s) is not tracked (e.g. a mass mail-out) or is captured in aggregate (all emails confirmed received by a particular time).", 0, java.lang.Integer.MAX_VALUE, recipient));
        childrenList.add(new Property("payload", "", "Text, attachment(s), or resource(s) that was communicated to the recipient.", 0, java.lang.Integer.MAX_VALUE, payload));
        childrenList.add(new Property("medium", "CodeableConcept", "A channel that was used for this communication (e.g. email, fax).", 0, java.lang.Integer.MAX_VALUE, medium));
        childrenList.add(new Property("status", "code", "The status of the transmission.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("encounter", "Reference(Encounter)", "The encounter within which the communication was sent.", 0, java.lang.Integer.MAX_VALUE, encounter));
        childrenList.add(new Property("sent", "dateTime", "The time when this communication was sent.", 0, java.lang.Integer.MAX_VALUE, sent));
        childrenList.add(new Property("received", "dateTime", "The time when this communication arrived at the destination.", 0, java.lang.Integer.MAX_VALUE, received));
        childrenList.add(new Property("reason", "CodeableConcept", "The reason or justification for the communication.", 0, java.lang.Integer.MAX_VALUE, reason));
        childrenList.add(new Property("subject", "Reference(Patient)", "The patient who was the focus of this communication.", 0, java.lang.Integer.MAX_VALUE, subject));
        childrenList.add(new Property("requestDetail", "Reference(CommunicationRequest)", "The communication request that was responsible for producing this communication.", 0, java.lang.Integer.MAX_VALUE, requestDetail));
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
          this.getPayload().add((CommunicationPayloadComponent) value);
        else if (name.equals("medium"))
          this.getMedium().add(castToCodeableConcept(value));
        else if (name.equals("status"))
          this.status = new CommunicationStatusEnumFactory().fromType(value); // Enumeration<CommunicationStatus>
        else if (name.equals("encounter"))
          this.encounter = castToReference(value); // Reference
        else if (name.equals("sent"))
          this.sent = castToDateTime(value); // DateTimeType
        else if (name.equals("received"))
          this.received = castToDateTime(value); // DateTimeType
        else if (name.equals("reason"))
          this.getReason().add(castToCodeableConcept(value));
        else if (name.equals("subject"))
          this.subject = castToReference(value); // Reference
        else if (name.equals("requestDetail"))
          this.requestDetail = castToReference(value); // Reference
        else
          super.setProperty(name, value);
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
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type Communication.status");
        }
        else if (name.equals("encounter")) {
          this.encounter = new Reference();
          return this.encounter;
        }
        else if (name.equals("sent")) {
          throw new FHIRException("Cannot call addChild on a primitive type Communication.sent");
        }
        else if (name.equals("received")) {
          throw new FHIRException("Cannot call addChild on a primitive type Communication.received");
        }
        else if (name.equals("reason")) {
          return addReason();
        }
        else if (name.equals("subject")) {
          this.subject = new Reference();
          return this.subject;
        }
        else if (name.equals("requestDetail")) {
          this.requestDetail = new Reference();
          return this.requestDetail;
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "Communication";

  }

      public Communication copy() {
        Communication dst = new Communication();
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
          dst.payload = new ArrayList<CommunicationPayloadComponent>();
          for (CommunicationPayloadComponent i : payload)
            dst.payload.add(i.copy());
        };
        if (medium != null) {
          dst.medium = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : medium)
            dst.medium.add(i.copy());
        };
        dst.status = status == null ? null : status.copy();
        dst.encounter = encounter == null ? null : encounter.copy();
        dst.sent = sent == null ? null : sent.copy();
        dst.received = received == null ? null : received.copy();
        if (reason != null) {
          dst.reason = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : reason)
            dst.reason.add(i.copy());
        };
        dst.subject = subject == null ? null : subject.copy();
        dst.requestDetail = requestDetail == null ? null : requestDetail.copy();
        return dst;
      }

      protected Communication typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof Communication))
          return false;
        Communication o = (Communication) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(category, o.category, true) && compareDeep(sender, o.sender, true)
           && compareDeep(recipient, o.recipient, true) && compareDeep(payload, o.payload, true) && compareDeep(medium, o.medium, true)
           && compareDeep(status, o.status, true) && compareDeep(encounter, o.encounter, true) && compareDeep(sent, o.sent, true)
           && compareDeep(received, o.received, true) && compareDeep(reason, o.reason, true) && compareDeep(subject, o.subject, true)
           && compareDeep(requestDetail, o.requestDetail, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Communication))
          return false;
        Communication o = (Communication) other;
        return compareValues(status, o.status, true) && compareValues(sent, o.sent, true) && compareValues(received, o.received, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (category == null || category.isEmpty())
           && (sender == null || sender.isEmpty()) && (recipient == null || recipient.isEmpty()) && (payload == null || payload.isEmpty())
           && (medium == null || medium.isEmpty()) && (status == null || status.isEmpty()) && (encounter == null || encounter.isEmpty())
           && (sent == null || sent.isEmpty()) && (received == null || received.isEmpty()) && (reason == null || reason.isEmpty())
           && (subject == null || subject.isEmpty()) && (requestDetail == null || requestDetail.isEmpty())
          ;
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Communication;
   }

  @SearchParamDefinition(name="identifier", path="Communication.identifier", description="Unique identifier", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
  @SearchParamDefinition(name="request", path="Communication.requestDetail", description="CommunicationRequest producing this message", type="reference" )
  public static final String SP_REQUEST = "request";
  @SearchParamDefinition(name="sender", path="Communication.sender", description="Message sender", type="reference" )
  public static final String SP_SENDER = "sender";
  @SearchParamDefinition(name="subject", path="Communication.subject", description="Focus of message", type="reference" )
  public static final String SP_SUBJECT = "subject";
  @SearchParamDefinition(name="patient", path="Communication.subject", description="Focus of message", type="reference" )
  public static final String SP_PATIENT = "patient";
  @SearchParamDefinition(name="recipient", path="Communication.recipient", description="Message recipient", type="reference" )
  public static final String SP_RECIPIENT = "recipient";
  @SearchParamDefinition(name="received", path="Communication.received", description="When received", type="date" )
  public static final String SP_RECEIVED = "received";
  @SearchParamDefinition(name="medium", path="Communication.medium", description="A channel of communication", type="token" )
  public static final String SP_MEDIUM = "medium";
  @SearchParamDefinition(name="encounter", path="Communication.encounter", description="Encounter leading to message", type="reference" )
  public static final String SP_ENCOUNTER = "encounter";
  @SearchParamDefinition(name="category", path="Communication.category", description="Message category", type="token" )
  public static final String SP_CATEGORY = "category";
  @SearchParamDefinition(name="sent", path="Communication.sent", description="When sent", type="date" )
  public static final String SP_SENT = "sent";
  @SearchParamDefinition(name="status", path="Communication.status", description="in-progress | completed | suspended | rejected | failed", type="token" )
  public static final String SP_STATUS = "status";

}

