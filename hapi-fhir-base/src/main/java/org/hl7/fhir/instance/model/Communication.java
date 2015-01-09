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
 * An occurrence of information being transmitted. E.g., an alert that was sent to a responsible provider, a public health agency was notified about a reportable condition.
 */
@ResourceDef(name="Communication", profile="http://hl7.org/fhir/Profile/Communication")
public class Communication extends DomainResource {

    public enum CommunicationStatus implements FhirEnum {
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

      public static final CommunicationStatusEnumFactory ENUM_FACTORY = new CommunicationStatusEnumFactory();

        public static CommunicationStatus fromCode(String codeString) throws IllegalArgumentException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("in progress".equals(codeString))
          return INPROGRESS;
        if ("completed".equals(codeString))
          return COMPLETED;
        if ("suspended".equals(codeString))
          return SUSPENDED;
        if ("rejected".equals(codeString))
          return REJECTED;
        if ("failed".equals(codeString))
          return FAILED;
        throw new IllegalArgumentException("Unknown CommunicationStatus code '"+codeString+"'");
        }
        @Override
        public String toCode() {
          switch (this) {
            case INPROGRESS: return "in progress";
            case COMPLETED: return "completed";
            case SUSPENDED: return "suspended";
            case REJECTED: return "rejected";
            case FAILED: return "failed";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case INPROGRESS: return "";
            case COMPLETED: return "";
            case SUSPENDED: return "";
            case REJECTED: return "";
            case FAILED: return "";
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
            case INPROGRESS: return "in progress";
            case COMPLETED: return "completed";
            case SUSPENDED: return "suspended";
            case REJECTED: return "rejected";
            case FAILED: return "failed";
            default: return "?";
          }
        }
    }

  public static class CommunicationStatusEnumFactory implements EnumFactory<CommunicationStatus> {
    public CommunicationStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("in progress".equals(codeString))
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
    public String toCode(CommunicationStatus code) throws IllegalArgumentException {
      if (code == CommunicationStatus.INPROGRESS)
        return "in progress";
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
    public static class CommunicationMessagePartComponent extends BackboneElement {
        /**
         * An individual message part for multi-part messages.
         */
        @Child(name="content", type={StringType.class, Attachment.class}, order=1, min=1, max=1)
        @Description(shortDefinition="Message part content", formalDefinition="An individual message part for multi-part messages." )
        protected Type content;

        private static final long serialVersionUID = -1763459053L;

      public CommunicationMessagePartComponent() {
        super();
      }

      public CommunicationMessagePartComponent(Type content) {
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
        public CommunicationMessagePartComponent setContent(Type value) { 
          this.content = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("content[x]", "string|Attachment|Reference(Any)", "An individual message part for multi-part messages.", 0, java.lang.Integer.MAX_VALUE, content));
        }

      public CommunicationMessagePartComponent copy() {
        CommunicationMessagePartComponent dst = new CommunicationMessagePartComponent();
        copyValues(dst);
        dst.content = content == null ? null : content.copy();
        return dst;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (content == null || content.isEmpty());
      }

  }

    /**
     * Identifiers associated with this Communication that are defined by business processes and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation).
     */
    @Child(name="identifier", type={Identifier.class}, order=-1, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Unique identifier", formalDefinition="Identifiers associated with this Communication that are defined by business processes and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)." )
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
     * The entity (e.g., person, organization, clinical information system, or device) which is the target of the communication.
     */
    @Child(name="recipient", type={Patient.class, Device.class, RelatedPerson.class, Practitioner.class}, order=2, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Message recipient", formalDefinition="The entity (e.g., person, organization, clinical information system, or device) which is the target of the communication." )
    protected List<Reference> recipient;
    /**
     * The actual objects that are the target of the reference (The entity (e.g., person, organization, clinical information system, or device) which is the target of the communication.)
     */
    protected List<Resource> recipientTarget;


    /**
     * Text, attachment(s), or resource(s) to be communicated to the recipient.
     */
    @Child(name="messagePart", type={}, order=3, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Message payload", formalDefinition="Text, attachment(s), or resource(s) to be communicated to the recipient." )
    protected List<CommunicationMessagePartComponent> messagePart;

    /**
     * The communication medium, e.g., email, fax.
     */
    @Child(name="medium", type={CodeableConcept.class}, order=4, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Communication medium", formalDefinition="The communication medium, e.g., email, fax." )
    protected List<CodeableConcept> medium;

    /**
     * The status of the transmission.
     */
    @Child(name="status", type={CodeType.class}, order=5, min=0, max=1)
    @Description(shortDefinition="in progress | completed | suspended | rejected | failed", formalDefinition="The status of the transmission." )
    protected Enumeration<CommunicationStatus> status;

    /**
     * The encounter within which the communication was sent.
     */
    @Child(name="encounter", type={Encounter.class}, order=6, min=0, max=1)
    @Description(shortDefinition="Encounter leading to message", formalDefinition="The encounter within which the communication was sent." )
    protected Reference encounter;

    /**
     * The actual object that is the target of the reference (The encounter within which the communication was sent.)
     */
    protected Encounter encounterTarget;

    /**
     * The time when this communication was sent.
     */
    @Child(name="sent", type={DateTimeType.class}, order=7, min=0, max=1)
    @Description(shortDefinition="When sent", formalDefinition="The time when this communication was sent." )
    protected DateTimeType sent;

    /**
     * The time when this communication arrived at the destination.
     */
    @Child(name="received", type={DateTimeType.class}, order=8, min=0, max=1)
    @Description(shortDefinition="When received", formalDefinition="The time when this communication arrived at the destination." )
    protected DateTimeType received;

    /**
     * The reason or justification for the communication.
     */
    @Child(name="indication", type={CodeableConcept.class}, order=9, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Indication for message", formalDefinition="The reason or justification for the communication." )
    protected List<CodeableConcept> indication;

    /**
     * The patient who is the focus of this communication.
     */
    @Child(name="subject", type={Patient.class}, order=10, min=1, max=1)
    @Description(shortDefinition="Focus of message", formalDefinition="The patient who is the focus of this communication." )
    protected Reference subject;

    /**
     * The actual object that is the target of the reference (The patient who is the focus of this communication.)
     */
    protected Patient subjectTarget;

    private static final long serialVersionUID = -817648825L;

    public Communication() {
      super();
    }

    public Communication(Reference subject) {
      super();
      this.subject = subject;
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

    /**
     * @return {@link #category} (The type of message such as alert, notification, reminder, instruction, etc.)
     */
    public CodeableConcept getCategory() { 
      if (this.category == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Communication.category");
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
    public Communication setCategory(CodeableConcept value) { 
      this.category = value;
      return this;
    }

    /**
     * @return {@link #sender} (The entity (e.g., person, organization, clinical information system, or device) which is the source of the communication.)
     */
    public Reference getSender() { 
      if (this.sender == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Communication.sender");
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
    public Communication setSender(Reference value) { 
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
    public Communication setSenderTarget(Resource value) { 
      this.senderTarget = value;
      return this;
    }

    /**
     * @return {@link #recipient} (The entity (e.g., person, organization, clinical information system, or device) which is the target of the communication.)
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
     * @return {@link #recipient} (The entity (e.g., person, organization, clinical information system, or device) which is the target of the communication.)
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
     * @return {@link #recipient} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. The entity (e.g., person, organization, clinical information system, or device) which is the target of the communication.)
     */
    public List<Resource> getRecipientTarget() { 
      if (this.recipientTarget == null)
        this.recipientTarget = new ArrayList<Resource>();
      return this.recipientTarget;
    }

    /**
     * @return {@link #messagePart} (Text, attachment(s), or resource(s) to be communicated to the recipient.)
     */
    public List<CommunicationMessagePartComponent> getMessagePart() { 
      if (this.messagePart == null)
        this.messagePart = new ArrayList<CommunicationMessagePartComponent>();
      return this.messagePart;
    }

    public boolean hasMessagePart() { 
      if (this.messagePart == null)
        return false;
      for (CommunicationMessagePartComponent item : this.messagePart)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #messagePart} (Text, attachment(s), or resource(s) to be communicated to the recipient.)
     */
    // syntactic sugar
    public CommunicationMessagePartComponent addMessagePart() { //3
      CommunicationMessagePartComponent t = new CommunicationMessagePartComponent();
      if (this.messagePart == null)
        this.messagePart = new ArrayList<CommunicationMessagePartComponent>();
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
     * @return {@link #status} (The status of the transmission.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<CommunicationStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Communication.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<CommunicationStatus>();
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
          this.status = new Enumeration<CommunicationStatus>(CommunicationStatus.ENUM_FACTORY);
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
          this.encounter = new Reference();
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
          this.encounterTarget = new Encounter();
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
          this.sent = new DateTimeType();
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
          this.received = new DateTimeType();
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
     * @return {@link #indication} (The reason or justification for the communication.)
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
     * @return {@link #indication} (The reason or justification for the communication.)
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
     * @return {@link #subject} (The patient who is the focus of this communication.)
     */
    public Reference getSubject() { 
      if (this.subject == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Communication.subject");
        else if (Configuration.doAutoCreate())
          this.subject = new Reference();
      return this.subject;
    }

    public boolean hasSubject() { 
      return this.subject != null && !this.subject.isEmpty();
    }

    /**
     * @param value {@link #subject} (The patient who is the focus of this communication.)
     */
    public Communication setSubject(Reference value) { 
      this.subject = value;
      return this;
    }

    /**
     * @return {@link #subject} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The patient who is the focus of this communication.)
     */
    public Patient getSubjectTarget() { 
      if (this.subjectTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Communication.subject");
        else if (Configuration.doAutoCreate())
          this.subjectTarget = new Patient();
      return this.subjectTarget;
    }

    /**
     * @param value {@link #subject} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The patient who is the focus of this communication.)
     */
    public Communication setSubjectTarget(Patient value) { 
      this.subjectTarget = value;
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "Identifiers associated with this Communication that are defined by business processes and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation).", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("category", "CodeableConcept", "The type of message such as alert, notification, reminder, instruction, etc.", 0, java.lang.Integer.MAX_VALUE, category));
        childrenList.add(new Property("sender", "Reference(Patient|Practitioner|Device|RelatedPerson|Organization)", "The entity (e.g., person, organization, clinical information system, or device) which is the source of the communication.", 0, java.lang.Integer.MAX_VALUE, sender));
        childrenList.add(new Property("recipient", "Reference(Patient|Device|RelatedPerson|Practitioner)", "The entity (e.g., person, organization, clinical information system, or device) which is the target of the communication.", 0, java.lang.Integer.MAX_VALUE, recipient));
        childrenList.add(new Property("messagePart", "", "Text, attachment(s), or resource(s) to be communicated to the recipient.", 0, java.lang.Integer.MAX_VALUE, messagePart));
        childrenList.add(new Property("medium", "CodeableConcept", "The communication medium, e.g., email, fax.", 0, java.lang.Integer.MAX_VALUE, medium));
        childrenList.add(new Property("status", "code", "The status of the transmission.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("encounter", "Reference(Encounter)", "The encounter within which the communication was sent.", 0, java.lang.Integer.MAX_VALUE, encounter));
        childrenList.add(new Property("sent", "dateTime", "The time when this communication was sent.", 0, java.lang.Integer.MAX_VALUE, sent));
        childrenList.add(new Property("received", "dateTime", "The time when this communication arrived at the destination.", 0, java.lang.Integer.MAX_VALUE, received));
        childrenList.add(new Property("indication", "CodeableConcept", "The reason or justification for the communication.", 0, java.lang.Integer.MAX_VALUE, indication));
        childrenList.add(new Property("subject", "Reference(Patient)", "The patient who is the focus of this communication.", 0, java.lang.Integer.MAX_VALUE, subject));
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
        if (messagePart != null) {
          dst.messagePart = new ArrayList<CommunicationMessagePartComponent>();
          for (CommunicationMessagePartComponent i : messagePart)
            dst.messagePart.add(i.copy());
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
        if (indication != null) {
          dst.indication = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : indication)
            dst.indication.add(i.copy());
        };
        dst.subject = subject == null ? null : subject.copy();
        return dst;
      }

      protected Communication typedCopy() {
        return copy();
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (category == null || category.isEmpty())
           && (sender == null || sender.isEmpty()) && (recipient == null || recipient.isEmpty()) && (messagePart == null || messagePart.isEmpty())
           && (medium == null || medium.isEmpty()) && (status == null || status.isEmpty()) && (encounter == null || encounter.isEmpty())
           && (sent == null || sent.isEmpty()) && (received == null || received.isEmpty()) && (indication == null || indication.isEmpty())
           && (subject == null || subject.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Communication;
   }

  @SearchParamDefinition(name="sender", path="Communication.sender", description="Message sender", type="reference" )
  public static final String SP_SENDER = "sender";
  @SearchParamDefinition(name="sent", path="Communication.sent", description="When sent", type="date" )
  public static final String SP_SENT = "sent";
  @SearchParamDefinition(name="category", path="Communication.category", description="Message category", type="token" )
  public static final String SP_CATEGORY = "category";
  @SearchParamDefinition(name="patient", path="Communication.subject", description="Focus of message", type="reference" )
  public static final String SP_PATIENT = "patient";
  @SearchParamDefinition(name="status", path="Communication.status", description="in progress | completed | suspended | rejected | failed", type="token" )
  public static final String SP_STATUS = "status";
  @SearchParamDefinition(name="subject", path="Communication.subject", description="Focus of message", type="reference" )
  public static final String SP_SUBJECT = "subject";
  @SearchParamDefinition(name="received", path="Communication.received", description="When received", type="date" )
  public static final String SP_RECEIVED = "received";
  @SearchParamDefinition(name="encounter", path="Communication.encounter", description="Encounter leading to message", type="reference" )
  public static final String SP_ENCOUNTER = "encounter";
  @SearchParamDefinition(name="identifier", path="Communication.identifier", description="Unique identifier", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
  @SearchParamDefinition(name="medium", path="Communication.medium", description="Communication medium", type="token" )
  public static final String SP_MEDIUM = "medium";
  @SearchParamDefinition(name="recipient", path="Communication.recipient", description="Message recipient", type="reference" )
  public static final String SP_RECIPIENT = "recipient";

}

