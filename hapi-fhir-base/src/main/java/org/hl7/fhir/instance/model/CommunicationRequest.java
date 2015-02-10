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
 * A request to convey information. E.g., the CDS system proposes that an alert be sent to a responsible provider, the CDS system proposes that the public health agency be notified about a reportable condition.
 */
@ResourceDef(name="CommunicationRequest", profile="http://hl7.org/fhir/Profile/CommunicationRequest")
public class CommunicationRequest extends DomainResource {

    public enum CommunicationRequestStatus implements FhirEnum {
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

      public static final CommunicationRequestStatusEnumFactory ENUM_FACTORY = new CommunicationRequestStatusEnumFactory();

        public static CommunicationRequestStatus fromCode(String codeString) throws IllegalArgumentException {
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
        throw new IllegalArgumentException("Unknown CommunicationRequestStatus code '"+codeString+"'");
        }
        @Override
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
            case RECEIVED: return "The receiving system has received the request but not yet decided whether it will be performed.";
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

  public static class CommunicationRequestStatusEnumFactory implements EnumFactory<CommunicationRequestStatus> {
    public CommunicationRequestStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("requested".equals(codeString))
          return CommunicationRequestStatus.REQUESTED;
        if ("received".equals(codeString))
          return CommunicationRequestStatus.RECEIVED;
        if ("accepted".equals(codeString))
          return CommunicationRequestStatus.ACCEPTED;
        if ("in progress".equals(codeString))
          return CommunicationRequestStatus.INPROGRESS;
        if ("review".equals(codeString))
          return CommunicationRequestStatus.REVIEW;
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
    public String toCode(CommunicationRequestStatus code) throws IllegalArgumentException {
      if (code == CommunicationRequestStatus.REQUESTED)
        return "requested";
      if (code == CommunicationRequestStatus.RECEIVED)
        return "received";
      if (code == CommunicationRequestStatus.ACCEPTED)
        return "accepted";
      if (code == CommunicationRequestStatus.INPROGRESS)
        return "in progress";
      if (code == CommunicationRequestStatus.REVIEW)
        return "review";
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
    }

    public enum CommunicationRequestMode implements FhirEnum {
        /**
         * planned.
         */
        PLANNED, 
        /**
         * proposed.
         */
        PROPOSED, 
        /**
         * ordered.
         */
        ORDERED, 
        /**
         * added to help the parsers
         */
        NULL;

      public static final CommunicationRequestModeEnumFactory ENUM_FACTORY = new CommunicationRequestModeEnumFactory();

        public static CommunicationRequestMode fromCode(String codeString) throws IllegalArgumentException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("planned".equals(codeString))
          return PLANNED;
        if ("proposed".equals(codeString))
          return PROPOSED;
        if ("ordered".equals(codeString))
          return ORDERED;
        throw new IllegalArgumentException("Unknown CommunicationRequestMode code '"+codeString+"'");
        }
        @Override
        public String toCode() {
          switch (this) {
            case PLANNED: return "planned";
            case PROPOSED: return "proposed";
            case ORDERED: return "ordered";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case PLANNED: return "";
            case PROPOSED: return "";
            case ORDERED: return "";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case PLANNED: return "planned.";
            case PROPOSED: return "proposed.";
            case ORDERED: return "ordered.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PLANNED: return "planned";
            case PROPOSED: return "proposed";
            case ORDERED: return "ordered";
            default: return "?";
          }
        }
    }

  public static class CommunicationRequestModeEnumFactory implements EnumFactory<CommunicationRequestMode> {
    public CommunicationRequestMode fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("planned".equals(codeString))
          return CommunicationRequestMode.PLANNED;
        if ("proposed".equals(codeString))
          return CommunicationRequestMode.PROPOSED;
        if ("ordered".equals(codeString))
          return CommunicationRequestMode.ORDERED;
        throw new IllegalArgumentException("Unknown CommunicationRequestMode code '"+codeString+"'");
        }
    public String toCode(CommunicationRequestMode code) throws IllegalArgumentException {
      if (code == CommunicationRequestMode.PLANNED)
        return "planned";
      if (code == CommunicationRequestMode.PROPOSED)
        return "proposed";
      if (code == CommunicationRequestMode.ORDERED)
        return "ordered";
      return "?";
      }
    }

    @Block()
    public static class CommunicationRequestMessagePartComponent extends BackboneElement {
        /**
         * An individual message part for multi-part messages.
         */
        @Child(name="content", type={StringType.class, Attachment.class}, order=1, min=1, max=1)
        @Description(shortDefinition="Message part content", formalDefinition="An individual message part for multi-part messages." )
        protected Type content;

        private static final long serialVersionUID = -1763459053L;

      public CommunicationRequestMessagePartComponent() {
        super();
      }

      public CommunicationRequestMessagePartComponent(Type content) {
        super();
        this.content = content;
      }

        /**
         * @return {@link #content} (An individual message part for multi-part messages.)
         */
        public Type getContent() { 
          return this.content;
        }

        /**
         * @return {@link #content} (An individual message part for multi-part messages.)
         */
        public StringType getContentStringType() throws Exception { 
          if (!(this.content instanceof StringType))
            throw new Exception("Type mismatch: the type StringType was expected, but "+this.content.getClass().getName()+" was encountered");
          return (StringType) this.content;
        }

        /**
         * @return {@link #content} (An individual message part for multi-part messages.)
         */
        public Attachment getContentAttachment() throws Exception { 
          if (!(this.content instanceof Attachment))
            throw new Exception("Type mismatch: the type Attachment was expected, but "+this.content.getClass().getName()+" was encountered");
          return (Attachment) this.content;
        }

        /**
         * @return {@link #content} (An individual message part for multi-part messages.)
         */
        public Reference getContentReference() throws Exception { 
          if (!(this.content instanceof Reference))
            throw new Exception("Type mismatch: the type Reference was expected, but "+this.content.getClass().getName()+" was encountered");
          return (Reference) this.content;
        }

        public boolean hasContent() { 
          return this.content != null && !this.content.isEmpty();
        }

        /**
         * @param value {@link #content} (An individual message part for multi-part messages.)
         */
        public CommunicationRequestMessagePartComponent setContent(Type value) { 
          this.content = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("content[x]", "string|Attachment|Reference(Any)", "An individual message part for multi-part messages.", 0, java.lang.Integer.MAX_VALUE, content));
        }

      public CommunicationRequestMessagePartComponent copy() {
        CommunicationRequestMessagePartComponent dst = new CommunicationRequestMessagePartComponent();
        copyValues(dst);
        dst.content = content == null ? null : content.copy();
        return dst;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (content == null || content.isEmpty());
      }

  }

    /**
     * A unique ID of this request for reference purposes. It must be provided if user wants it returned as part of any output, otherwise it will be auto-generated, if needed, by CDS system. Does not need to be the actual ID of the source system.
     */
    @Child(name="identifier", type={Identifier.class}, order=-1, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Unique identifier", formalDefinition="A unique ID of this request for reference purposes. It must be provided if user wants it returned as part of any output, otherwise it will be auto-generated, if needed, by CDS system. Does not need to be the actual ID of the source system." )
    protected List<Identifier> identifier;

    /**
     * The type of message such as alert, notification, reminder, instruction, etc.
     */
    @Child(name="category", type={CodeableConcept.class}, order=0, min=0, max=1)
    @Description(shortDefinition="Message category", formalDefinition="The type of message such as alert, notification, reminder, instruction, etc." )
    protected CodeableConcept category;

    /**
     * The entity (e.g., person, organization, clinical information system, or device) which is the source of the communication.
     */
    @Child(name="sender", type={Patient.class, Practitioner.class, Device.class, RelatedPerson.class, Organization.class}, order=1, min=0, max=1)
    @Description(shortDefinition="Message sender", formalDefinition="The entity (e.g., person, organization, clinical information system, or device) which is the source of the communication." )
    protected Reference sender;

    /**
     * The actual object that is the target of the reference (The entity (e.g., person, organization, clinical information system, or device) which is the source of the communication.)
     */
    protected Resource senderTarget;

    /**
     * The entity (e.g., person, organization, clinical information system, or device) which is the intended target of the communication.
     */
    @Child(name="recipient", type={Patient.class, Device.class, RelatedPerson.class, Practitioner.class}, order=2, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Message recipient", formalDefinition="The entity (e.g., person, organization, clinical information system, or device) which is the intended target of the communication." )
    protected List<Reference> recipient;
    /**
     * The actual objects that are the target of the reference (The entity (e.g., person, organization, clinical information system, or device) which is the intended target of the communication.)
     */
    protected List<Resource> recipientTarget;


    /**
     * Text, attachment(s), or resource(s) to be communicated to the recipient.
     */
    @Child(name="messagePart", type={}, order=3, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Message payload", formalDefinition="Text, attachment(s), or resource(s) to be communicated to the recipient." )
    protected List<CommunicationRequestMessagePartComponent> messagePart;

    /**
     * The communication medium, e.g., email, fax.
     */
    @Child(name="medium", type={CodeableConcept.class}, order=4, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Communication medium", formalDefinition="The communication medium, e.g., email, fax." )
    protected List<CodeableConcept> medium;

    /**
     * The responsible person who authorizes this order, e.g., physician. This may be different than the author of the order statement, e.g., clerk, who may have entered the statement into the order entry application.
     */
    @Child(name="requester", type={Practitioner.class, Patient.class, RelatedPerson.class}, order=5, min=0, max=1)
    @Description(shortDefinition="Requester of communication", formalDefinition="The responsible person who authorizes this order, e.g., physician. This may be different than the author of the order statement, e.g., clerk, who may have entered the statement into the order entry application." )
    protected Reference requester;

    /**
     * The actual object that is the target of the reference (The responsible person who authorizes this order, e.g., physician. This may be different than the author of the order statement, e.g., clerk, who may have entered the statement into the order entry application.)
     */
    protected Resource requesterTarget;

    /**
     * The status of the proposal or order.
     */
    @Child(name="status", type={CodeType.class}, order=6, min=0, max=1)
    @Description(shortDefinition="requested | received | accepted | in progress | review | completed | suspended | rejected | failed", formalDefinition="The status of the proposal or order." )
    protected Enumeration<CommunicationRequestStatus> status;

    /**
     * Whether the communication is proposed, ordered, or planned.
     */
    @Child(name="mode", type={CodeType.class}, order=7, min=0, max=1)
    @Description(shortDefinition="planned | proposed | ordered", formalDefinition="Whether the communication is proposed, ordered, or planned." )
    protected Enumeration<CommunicationRequestMode> mode;

    /**
     * The encounter within which the communication request was created.
     */
    @Child(name="encounter", type={Encounter.class}, order=8, min=0, max=1)
    @Description(shortDefinition="Encounter leading to message", formalDefinition="The encounter within which the communication request was created." )
    protected Reference encounter;

    /**
     * The actual object that is the target of the reference (The encounter within which the communication request was created.)
     */
    protected Encounter encounterTarget;

    /**
     * The time when this communication is to occur.
     */
    @Child(name="scheduledTime", type={DateTimeType.class}, order=9, min=0, max=1)
    @Description(shortDefinition="When scheduled", formalDefinition="The time when this communication is to occur." )
    protected DateTimeType scheduledTime;

    /**
     * The reason or justification for the communication request.
     */
    @Child(name="indication", type={CodeableConcept.class}, order=10, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Indication for message", formalDefinition="The reason or justification for the communication request." )
    protected List<CodeableConcept> indication;

    /**
     * The time when the request was made.
     */
    @Child(name="orderedOn", type={DateTimeType.class}, order=11, min=0, max=1)
    @Description(shortDefinition="When ordered or proposed", formalDefinition="The time when the request was made." )
    protected DateTimeType orderedOn;

    /**
     * The patient who is the focus of this communication request.
     */
    @Child(name="subject", type={Patient.class}, order=12, min=1, max=1)
    @Description(shortDefinition="Focus of message", formalDefinition="The patient who is the focus of this communication request." )
    protected Reference subject;

    /**
     * The actual object that is the target of the reference (The patient who is the focus of this communication request.)
     */
    protected Patient subjectTarget;

    /**
     * Characterizes how quickly the proposed act must be initiated. Includes concepts such as stat, urgent, routine.
     */
    @Child(name="priority", type={CodeableConcept.class}, order=13, min=0, max=1)
    @Description(shortDefinition="Message urgency", formalDefinition="Characterizes how quickly the proposed act must be initiated. Includes concepts such as stat, urgent, routine." )
    protected CodeableConcept priority;

    private static final long serialVersionUID = -515307355L;

    public CommunicationRequest() {
      super();
    }

    public CommunicationRequest(Reference subject) {
      super();
      this.subject = subject;
    }

    /**
     * @return {@link #identifier} (A unique ID of this request for reference purposes. It must be provided if user wants it returned as part of any output, otherwise it will be auto-generated, if needed, by CDS system. Does not need to be the actual ID of the source system.)
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
     * @return {@link #identifier} (A unique ID of this request for reference purposes. It must be provided if user wants it returned as part of any output, otherwise it will be auto-generated, if needed, by CDS system. Does not need to be the actual ID of the source system.)
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
     * @return {@link #category} (The type of message such as alert, notification, reminder, instruction, etc.)
     */
    public CodeableConcept getCategory() { 
      if (this.category == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CommunicationRequest.category");
        else if (Configuration.doAutoCreate())
          this.category = new CodeableConcept();
      return this.category;
    }

    public boolean hasCategory() { 
      return this.category != null && !this.category.isEmpty();
    }

    /**
     * @param value {@link #category} (The type of message such as alert, notification, reminder, instruction, etc.)
     */
    public CommunicationRequest setCategory(CodeableConcept value) { 
      this.category = value;
      return this;
    }

    /**
     * @return {@link #sender} (The entity (e.g., person, organization, clinical information system, or device) which is the source of the communication.)
     */
    public Reference getSender() { 
      if (this.sender == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CommunicationRequest.sender");
        else if (Configuration.doAutoCreate())
          this.sender = new Reference();
      return this.sender;
    }

    public boolean hasSender() { 
      return this.sender != null && !this.sender.isEmpty();
    }

    /**
     * @param value {@link #sender} (The entity (e.g., person, organization, clinical information system, or device) which is the source of the communication.)
     */
    public CommunicationRequest setSender(Reference value) { 
      this.sender = value;
      return this;
    }

    /**
     * @return {@link #sender} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The entity (e.g., person, organization, clinical information system, or device) which is the source of the communication.)
     */
    public Resource getSenderTarget() { 
      return this.senderTarget;
    }

    /**
     * @param value {@link #sender} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The entity (e.g., person, organization, clinical information system, or device) which is the source of the communication.)
     */
    public CommunicationRequest setSenderTarget(Resource value) { 
      this.senderTarget = value;
      return this;
    }

    /**
     * @return {@link #recipient} (The entity (e.g., person, organization, clinical information system, or device) which is the intended target of the communication.)
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
     * @return {@link #recipient} (The entity (e.g., person, organization, clinical information system, or device) which is the intended target of the communication.)
     */
    // syntactic sugar
    public Reference addRecipient() { //3
      Reference t = new Reference();
      if (this.recipient == null)
        this.recipient = new ArrayList<Reference>();
      this.recipient.add(t);
      return t;
    }

    /**
     * @return {@link #recipient} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. The entity (e.g., person, organization, clinical information system, or device) which is the intended target of the communication.)
     */
    public List<Resource> getRecipientTarget() { 
      if (this.recipientTarget == null)
        this.recipientTarget = new ArrayList<Resource>();
      return this.recipientTarget;
    }

    /**
     * @return {@link #messagePart} (Text, attachment(s), or resource(s) to be communicated to the recipient.)
     */
    public List<CommunicationRequestMessagePartComponent> getMessagePart() { 
      if (this.messagePart == null)
        this.messagePart = new ArrayList<CommunicationRequestMessagePartComponent>();
      return this.messagePart;
    }

    public boolean hasMessagePart() { 
      if (this.messagePart == null)
        return false;
      for (CommunicationRequestMessagePartComponent item : this.messagePart)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #messagePart} (Text, attachment(s), or resource(s) to be communicated to the recipient.)
     */
    // syntactic sugar
    public CommunicationRequestMessagePartComponent addMessagePart() { //3
      CommunicationRequestMessagePartComponent t = new CommunicationRequestMessagePartComponent();
      if (this.messagePart == null)
        this.messagePart = new ArrayList<CommunicationRequestMessagePartComponent>();
      this.messagePart.add(t);
      return t;
    }

    /**
     * @return {@link #medium} (The communication medium, e.g., email, fax.)
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
     * @return {@link #medium} (The communication medium, e.g., email, fax.)
     */
    // syntactic sugar
    public CodeableConcept addMedium() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.medium == null)
        this.medium = new ArrayList<CodeableConcept>();
      this.medium.add(t);
      return t;
    }

    /**
     * @return {@link #requester} (The responsible person who authorizes this order, e.g., physician. This may be different than the author of the order statement, e.g., clerk, who may have entered the statement into the order entry application.)
     */
    public Reference getRequester() { 
      if (this.requester == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CommunicationRequest.requester");
        else if (Configuration.doAutoCreate())
          this.requester = new Reference();
      return this.requester;
    }

    public boolean hasRequester() { 
      return this.requester != null && !this.requester.isEmpty();
    }

    /**
     * @param value {@link #requester} (The responsible person who authorizes this order, e.g., physician. This may be different than the author of the order statement, e.g., clerk, who may have entered the statement into the order entry application.)
     */
    public CommunicationRequest setRequester(Reference value) { 
      this.requester = value;
      return this;
    }

    /**
     * @return {@link #requester} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The responsible person who authorizes this order, e.g., physician. This may be different than the author of the order statement, e.g., clerk, who may have entered the statement into the order entry application.)
     */
    public Resource getRequesterTarget() { 
      return this.requesterTarget;
    }

    /**
     * @param value {@link #requester} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The responsible person who authorizes this order, e.g., physician. This may be different than the author of the order statement, e.g., clerk, who may have entered the statement into the order entry application.)
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
          this.status = new Enumeration<CommunicationRequestStatus>();
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
          this.status = new Enumeration<CommunicationRequestStatus>(CommunicationRequestStatus.ENUM_FACTORY);
        this.status.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #mode} (Whether the communication is proposed, ordered, or planned.). This is the underlying object with id, value and extensions. The accessor "getMode" gives direct access to the value
     */
    public Enumeration<CommunicationRequestMode> getModeElement() { 
      if (this.mode == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CommunicationRequest.mode");
        else if (Configuration.doAutoCreate())
          this.mode = new Enumeration<CommunicationRequestMode>();
      return this.mode;
    }

    public boolean hasModeElement() { 
      return this.mode != null && !this.mode.isEmpty();
    }

    public boolean hasMode() { 
      return this.mode != null && !this.mode.isEmpty();
    }

    /**
     * @param value {@link #mode} (Whether the communication is proposed, ordered, or planned.). This is the underlying object with id, value and extensions. The accessor "getMode" gives direct access to the value
     */
    public CommunicationRequest setModeElement(Enumeration<CommunicationRequestMode> value) { 
      this.mode = value;
      return this;
    }

    /**
     * @return Whether the communication is proposed, ordered, or planned.
     */
    public CommunicationRequestMode getMode() { 
      return this.mode == null ? null : this.mode.getValue();
    }

    /**
     * @param value Whether the communication is proposed, ordered, or planned.
     */
    public CommunicationRequest setMode(CommunicationRequestMode value) { 
      if (value == null)
        this.mode = null;
      else {
        if (this.mode == null)
          this.mode = new Enumeration<CommunicationRequestMode>(CommunicationRequestMode.ENUM_FACTORY);
        this.mode.setValue(value);
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
          this.encounter = new Reference();
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
          this.encounterTarget = new Encounter();
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
     * @return {@link #scheduledTime} (The time when this communication is to occur.). This is the underlying object with id, value and extensions. The accessor "getScheduledTime" gives direct access to the value
     */
    public DateTimeType getScheduledTimeElement() { 
      if (this.scheduledTime == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CommunicationRequest.scheduledTime");
        else if (Configuration.doAutoCreate())
          this.scheduledTime = new DateTimeType();
      return this.scheduledTime;
    }

    public boolean hasScheduledTimeElement() { 
      return this.scheduledTime != null && !this.scheduledTime.isEmpty();
    }

    public boolean hasScheduledTime() { 
      return this.scheduledTime != null && !this.scheduledTime.isEmpty();
    }

    /**
     * @param value {@link #scheduledTime} (The time when this communication is to occur.). This is the underlying object with id, value and extensions. The accessor "getScheduledTime" gives direct access to the value
     */
    public CommunicationRequest setScheduledTimeElement(DateTimeType value) { 
      this.scheduledTime = value;
      return this;
    }

    /**
     * @return The time when this communication is to occur.
     */
    public Date getScheduledTime() { 
      return this.scheduledTime == null ? null : this.scheduledTime.getValue();
    }

    /**
     * @param value The time when this communication is to occur.
     */
    public CommunicationRequest setScheduledTime(Date value) { 
      if (value == null)
        this.scheduledTime = null;
      else {
        if (this.scheduledTime == null)
          this.scheduledTime = new DateTimeType();
        this.scheduledTime.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #indication} (The reason or justification for the communication request.)
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
     * @return {@link #indication} (The reason or justification for the communication request.)
     */
    // syntactic sugar
    public CodeableConcept addIndication() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.indication == null)
        this.indication = new ArrayList<CodeableConcept>();
      this.indication.add(t);
      return t;
    }

    /**
     * @return {@link #orderedOn} (The time when the request was made.). This is the underlying object with id, value and extensions. The accessor "getOrderedOn" gives direct access to the value
     */
    public DateTimeType getOrderedOnElement() { 
      if (this.orderedOn == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CommunicationRequest.orderedOn");
        else if (Configuration.doAutoCreate())
          this.orderedOn = new DateTimeType();
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
    public CommunicationRequest setOrderedOnElement(DateTimeType value) { 
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
    public CommunicationRequest setOrderedOn(Date value) { 
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
     * @return {@link #subject} (The patient who is the focus of this communication request.)
     */
    public Reference getSubject() { 
      if (this.subject == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CommunicationRequest.subject");
        else if (Configuration.doAutoCreate())
          this.subject = new Reference();
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
          this.subjectTarget = new Patient();
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
          this.priority = new CodeableConcept();
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
        childrenList.add(new Property("identifier", "Identifier", "A unique ID of this request for reference purposes. It must be provided if user wants it returned as part of any output, otherwise it will be auto-generated, if needed, by CDS system. Does not need to be the actual ID of the source system.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("category", "CodeableConcept", "The type of message such as alert, notification, reminder, instruction, etc.", 0, java.lang.Integer.MAX_VALUE, category));
        childrenList.add(new Property("sender", "Reference(Patient|Practitioner|Device|RelatedPerson|Organization)", "The entity (e.g., person, organization, clinical information system, or device) which is the source of the communication.", 0, java.lang.Integer.MAX_VALUE, sender));
        childrenList.add(new Property("recipient", "Reference(Patient|Device|RelatedPerson|Practitioner)", "The entity (e.g., person, organization, clinical information system, or device) which is the intended target of the communication.", 0, java.lang.Integer.MAX_VALUE, recipient));
        childrenList.add(new Property("messagePart", "", "Text, attachment(s), or resource(s) to be communicated to the recipient.", 0, java.lang.Integer.MAX_VALUE, messagePart));
        childrenList.add(new Property("medium", "CodeableConcept", "The communication medium, e.g., email, fax.", 0, java.lang.Integer.MAX_VALUE, medium));
        childrenList.add(new Property("requester", "Reference(Practitioner|Patient|RelatedPerson)", "The responsible person who authorizes this order, e.g., physician. This may be different than the author of the order statement, e.g., clerk, who may have entered the statement into the order entry application.", 0, java.lang.Integer.MAX_VALUE, requester));
        childrenList.add(new Property("status", "code", "The status of the proposal or order.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("mode", "code", "Whether the communication is proposed, ordered, or planned.", 0, java.lang.Integer.MAX_VALUE, mode));
        childrenList.add(new Property("encounter", "Reference(Encounter)", "The encounter within which the communication request was created.", 0, java.lang.Integer.MAX_VALUE, encounter));
        childrenList.add(new Property("scheduledTime", "dateTime", "The time when this communication is to occur.", 0, java.lang.Integer.MAX_VALUE, scheduledTime));
        childrenList.add(new Property("indication", "CodeableConcept", "The reason or justification for the communication request.", 0, java.lang.Integer.MAX_VALUE, indication));
        childrenList.add(new Property("orderedOn", "dateTime", "The time when the request was made.", 0, java.lang.Integer.MAX_VALUE, orderedOn));
        childrenList.add(new Property("subject", "Reference(Patient)", "The patient who is the focus of this communication request.", 0, java.lang.Integer.MAX_VALUE, subject));
        childrenList.add(new Property("priority", "CodeableConcept", "Characterizes how quickly the proposed act must be initiated. Includes concepts such as stat, urgent, routine.", 0, java.lang.Integer.MAX_VALUE, priority));
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
        if (messagePart != null) {
          dst.messagePart = new ArrayList<CommunicationRequestMessagePartComponent>();
          for (CommunicationRequestMessagePartComponent i : messagePart)
            dst.messagePart.add(i.copy());
        };
        if (medium != null) {
          dst.medium = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : medium)
            dst.medium.add(i.copy());
        };
        dst.requester = requester == null ? null : requester.copy();
        dst.status = status == null ? null : status.copy();
        dst.mode = mode == null ? null : mode.copy();
        dst.encounter = encounter == null ? null : encounter.copy();
        dst.scheduledTime = scheduledTime == null ? null : scheduledTime.copy();
        if (indication != null) {
          dst.indication = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : indication)
            dst.indication.add(i.copy());
        };
        dst.orderedOn = orderedOn == null ? null : orderedOn.copy();
        dst.subject = subject == null ? null : subject.copy();
        dst.priority = priority == null ? null : priority.copy();
        return dst;
      }

      protected CommunicationRequest typedCopy() {
        return copy();
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (category == null || category.isEmpty())
           && (sender == null || sender.isEmpty()) && (recipient == null || recipient.isEmpty()) && (messagePart == null || messagePart.isEmpty())
           && (medium == null || medium.isEmpty()) && (requester == null || requester.isEmpty()) && (status == null || status.isEmpty())
           && (mode == null || mode.isEmpty()) && (encounter == null || encounter.isEmpty()) && (scheduledTime == null || scheduledTime.isEmpty())
           && (indication == null || indication.isEmpty()) && (orderedOn == null || orderedOn.isEmpty())
           && (subject == null || subject.isEmpty()) && (priority == null || priority.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.CommunicationRequest;
   }

  @SearchParamDefinition(name="requester", path="CommunicationRequest.requester", description="Requester of communication", type="reference" )
  public static final String SP_REQUESTER = "requester";
  @SearchParamDefinition(name="status", path="CommunicationRequest.status", description="requested | received | accepted | in progress | review | completed | suspended | rejected | failed", type="token" )
  public static final String SP_STATUS = "status";
  @SearchParamDefinition(name="subject", path="CommunicationRequest.subject", description="Focus of message", type="reference" )
  public static final String SP_SUBJECT = "subject";
  @SearchParamDefinition(name="encounter", path="CommunicationRequest.encounter", description="Encounter leading to message", type="reference" )
  public static final String SP_ENCOUNTER = "encounter";
  @SearchParamDefinition(name="recipient", path="CommunicationRequest.recipient", description="Message recipient", type="reference" )
  public static final String SP_RECIPIENT = "recipient";
  @SearchParamDefinition(name="medium", path="CommunicationRequest.medium", description="Communication medium", type="token" )
  public static final String SP_MEDIUM = "medium";
  @SearchParamDefinition(name="mode", path="CommunicationRequest.mode", description="planned | proposed | ordered", type="token" )
  public static final String SP_MODE = "mode";
  @SearchParamDefinition(name="sender", path="CommunicationRequest.sender", description="Message sender", type="reference" )
  public static final String SP_SENDER = "sender";
  @SearchParamDefinition(name="category", path="CommunicationRequest.category", description="Message category", type="token" )
  public static final String SP_CATEGORY = "category";
  @SearchParamDefinition(name="time", path="CommunicationRequest.scheduledTime", description="When scheduled", type="date" )
  public static final String SP_TIME = "time";
  @SearchParamDefinition(name="patient", path="CommunicationRequest.subject", description="Focus of message", type="reference" )
  public static final String SP_PATIENT = "patient";
  @SearchParamDefinition(name="priority", path="CommunicationRequest.priority", description="Message urgency", type="token" )
  public static final String SP_PRIORITY = "priority";
  @SearchParamDefinition(name="ordered", path="CommunicationRequest.orderedOn", description="When ordered or proposed", type="date" )
  public static final String SP_ORDERED = "ordered";
  @SearchParamDefinition(name="identifier", path="CommunicationRequest.identifier", description="Unique identifier", type="token" )
  public static final String SP_IDENTIFIER = "identifier";

}

