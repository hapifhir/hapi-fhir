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

// Generated on Sat, Nov 5, 2016 10:42-0400 for FHIR v1.7.0

import java.util.*;

import org.hl7.fhir.utilities.Utilities;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ChildOrder;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Block;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.exceptions.FHIRException;
/**
 * Represents a request for a patient to employ a medical device. The device may be an implantable device, or an external assistive device, such as a walker.
 */
@ResourceDef(name="DeviceUseRequest", profile="http://hl7.org/fhir/Profile/DeviceUseRequest")
public class DeviceUseRequest extends DomainResource {

    public enum DeviceUseRequestStatus {
        /**
         * The request is in preliminary form prior to being sent.
         */
        DRAFT, 
        /**
         * The request is complete and is ready for fulfillment.
         */
        ACTIVE, 
        /**
         * The request has been held by originating system/user request.
         */
        SUSPENDED, 
        /**
         * The work has been completed, the report(s) released, and no further work is planned.
         */
        COMPLETED, 
        /**
         * The request was entered in error and voided.
         */
        ENTEREDINERROR, 
        /**
         * The request has been withdrawn.
         */
        CANCELLED, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static DeviceUseRequestStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("draft".equals(codeString))
          return DRAFT;
        if ("active".equals(codeString))
          return ACTIVE;
        if ("suspended".equals(codeString))
          return SUSPENDED;
        if ("completed".equals(codeString))
          return COMPLETED;
        if ("entered-in-error".equals(codeString))
          return ENTEREDINERROR;
        if ("cancelled".equals(codeString))
          return CANCELLED;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown DeviceUseRequestStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case DRAFT: return "draft";
            case ACTIVE: return "active";
            case SUSPENDED: return "suspended";
            case COMPLETED: return "completed";
            case ENTEREDINERROR: return "entered-in-error";
            case CANCELLED: return "cancelled";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case DRAFT: return "http://hl7.org/fhir/request-status";
            case ACTIVE: return "http://hl7.org/fhir/request-status";
            case SUSPENDED: return "http://hl7.org/fhir/request-status";
            case COMPLETED: return "http://hl7.org/fhir/request-status";
            case ENTEREDINERROR: return "http://hl7.org/fhir/request-status";
            case CANCELLED: return "http://hl7.org/fhir/request-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case DRAFT: return "The request is in preliminary form prior to being sent.";
            case ACTIVE: return "The request is complete and is ready for fulfillment.";
            case SUSPENDED: return "The request has been held by originating system/user request.";
            case COMPLETED: return "The work has been completed, the report(s) released, and no further work is planned.";
            case ENTEREDINERROR: return "The request was entered in error and voided.";
            case CANCELLED: return "The request has been withdrawn.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case DRAFT: return "Draft";
            case ACTIVE: return "Active";
            case SUSPENDED: return "Suspended";
            case COMPLETED: return "Completed";
            case ENTEREDINERROR: return "Entered in Error";
            case CANCELLED: return "Cancelled";
            default: return "?";
          }
        }
    }

  public static class DeviceUseRequestStatusEnumFactory implements EnumFactory<DeviceUseRequestStatus> {
    public DeviceUseRequestStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("draft".equals(codeString))
          return DeviceUseRequestStatus.DRAFT;
        if ("active".equals(codeString))
          return DeviceUseRequestStatus.ACTIVE;
        if ("suspended".equals(codeString))
          return DeviceUseRequestStatus.SUSPENDED;
        if ("completed".equals(codeString))
          return DeviceUseRequestStatus.COMPLETED;
        if ("entered-in-error".equals(codeString))
          return DeviceUseRequestStatus.ENTEREDINERROR;
        if ("cancelled".equals(codeString))
          return DeviceUseRequestStatus.CANCELLED;
        throw new IllegalArgumentException("Unknown DeviceUseRequestStatus code '"+codeString+"'");
        }
        public Enumeration<DeviceUseRequestStatus> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("draft".equals(codeString))
          return new Enumeration<DeviceUseRequestStatus>(this, DeviceUseRequestStatus.DRAFT);
        if ("active".equals(codeString))
          return new Enumeration<DeviceUseRequestStatus>(this, DeviceUseRequestStatus.ACTIVE);
        if ("suspended".equals(codeString))
          return new Enumeration<DeviceUseRequestStatus>(this, DeviceUseRequestStatus.SUSPENDED);
        if ("completed".equals(codeString))
          return new Enumeration<DeviceUseRequestStatus>(this, DeviceUseRequestStatus.COMPLETED);
        if ("entered-in-error".equals(codeString))
          return new Enumeration<DeviceUseRequestStatus>(this, DeviceUseRequestStatus.ENTEREDINERROR);
        if ("cancelled".equals(codeString))
          return new Enumeration<DeviceUseRequestStatus>(this, DeviceUseRequestStatus.CANCELLED);
        throw new FHIRException("Unknown DeviceUseRequestStatus code '"+codeString+"'");
        }
    public String toCode(DeviceUseRequestStatus code) {
      if (code == DeviceUseRequestStatus.DRAFT)
        return "draft";
      if (code == DeviceUseRequestStatus.ACTIVE)
        return "active";
      if (code == DeviceUseRequestStatus.SUSPENDED)
        return "suspended";
      if (code == DeviceUseRequestStatus.COMPLETED)
        return "completed";
      if (code == DeviceUseRequestStatus.ENTEREDINERROR)
        return "entered-in-error";
      if (code == DeviceUseRequestStatus.CANCELLED)
        return "cancelled";
      return "?";
      }
    public String toSystem(DeviceUseRequestStatus code) {
      return code.getSystem();
      }
    }

    /**
     * Identifiers assigned to this order by the orderer or by the receiver.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Request identifier", formalDefinition="Identifiers assigned to this order by the orderer or by the receiver." )
    protected List<Identifier> identifier;

    /**
     * Protocol or definition followed by this request. For example: The proposed act must be performed if the indicated conditions occur, e.g.., shortness of breath, SpO2 less than x%.
     */
    @Child(name = "definition", type = {Reference.class}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Protocol or definition", formalDefinition="Protocol or definition followed by this request. For example: The proposed act must be performed if the indicated conditions occur, e.g.., shortness of breath, SpO2 less than x%." )
    protected List<Reference> definition;
    /**
     * The actual objects that are the target of the reference (Protocol or definition followed by this request. For example: The proposed act must be performed if the indicated conditions occur, e.g.., shortness of breath, SpO2 less than x%.)
     */
    protected List<Resource> definitionTarget;


    /**
     * Plan/proposal/order fulfilled by this request.
     */
    @Child(name = "basedOn", type = {Reference.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="What request fulfills", formalDefinition="Plan/proposal/order fulfilled by this request." )
    protected List<Reference> basedOn;
    /**
     * The actual objects that are the target of the reference (Plan/proposal/order fulfilled by this request.)
     */
    protected List<Resource> basedOnTarget;


    /**
     * The request takes the place of the referenced completed or terminated request(s).
     */
    @Child(name = "replaces", type = {Reference.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="What request replaces", formalDefinition="The request takes the place of the referenced completed or terminated request(s)." )
    protected List<Reference> replaces;
    /**
     * The actual objects that are the target of the reference (The request takes the place of the referenced completed or terminated request(s).)
     */
    protected List<Resource> replacesTarget;


    /**
     * Composite request this is part of.
     */
    @Child(name = "requisition", type = {Identifier.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Identifier of composite request", formalDefinition="Composite request this is part of." )
    protected Identifier requisition;

    /**
     * The status of the request.
     */
    @Child(name = "status", type = {CodeType.class}, order=5, min=0, max=1, modifier=true, summary=true)
    @Description(shortDefinition="draft | active | suspended | completed | entered-in-error | cancelled", formalDefinition="The status of the request." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/request-status")
    protected Enumeration<DeviceUseRequestStatus> status;

    /**
     * Whether the request is a proposal, plan, an original order or a reflex order.
     */
    @Child(name = "stage", type = {CodeableConcept.class}, order=6, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="proposal | plan | original-order | encoded | reflex-order", formalDefinition="Whether the request is a proposal, plan, an original order or a reflex order." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/request-stage")
    protected CodeableConcept stage;

    /**
     * The details of the device to be used.
     */
    @Child(name = "device", type = {Device.class, CodeableConcept.class}, order=7, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Device requested", formalDefinition="The details of the device to be used." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/device-kind")
    protected Type device;

    /**
     * The patient who will use the device.
     */
    @Child(name = "subject", type = {Patient.class, Group.class, Location.class, Device.class}, order=8, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Focus of request", formalDefinition="The patient who will use the device." )
    protected Reference subject;

    /**
     * The actual object that is the target of the reference (The patient who will use the device.)
     */
    protected Resource subjectTarget;

    /**
     * An encounter that provides additional context in which this request is made.
     */
    @Child(name = "context", type = {Encounter.class, EpisodeOfCare.class}, order=9, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Encounter or Episode motivating request", formalDefinition="An encounter that provides additional context in which this request is made." )
    protected Reference context;

    /**
     * The actual object that is the target of the reference (An encounter that provides additional context in which this request is made.)
     */
    protected Resource contextTarget;

    /**
     * The timing schedule for the use of the device. The Schedule data type allows many different expressions, for example. "Every 8 hours"; "Three times a day"; "1/2 an hour before breakfast for 10 days from 23-Dec 2011:"; "15 Oct 2013, 17 Oct 2013 and 1 Nov 2013".
     */
    @Child(name = "occurrence", type = {DateTimeType.class, Period.class, Timing.class}, order=10, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Desired time or schedule for use", formalDefinition="The timing schedule for the use of the device. The Schedule data type allows many different expressions, for example. \"Every 8 hours\"; \"Three times a day\"; \"1/2 an hour before breakfast for 10 days from 23-Dec 2011:\"; \"15 Oct 2013, 17 Oct 2013 and 1 Nov 2013\"." )
    protected Type occurrence;

    /**
     * When the request transitioned to being actionable.
     */
    @Child(name = "authored", type = {DateTimeType.class}, order=11, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When recorded", formalDefinition="When the request transitioned to being actionable." )
    protected DateTimeType authored;

    /**
     * Who/what is requesting diagnostics.  The practitioner that holds legal responsibility for ordering the investigation.
     */
    @Child(name = "requester", type = {Device.class, Practitioner.class, Organization.class}, order=12, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Who/what is requesting diagnostics", formalDefinition="Who/what is requesting diagnostics.  The practitioner that holds legal responsibility for ordering the investigation." )
    protected Reference requester;

    /**
     * The actual object that is the target of the reference (Who/what is requesting diagnostics.  The practitioner that holds legal responsibility for ordering the investigation.)
     */
    protected Resource requesterTarget;

    /**
     * Desired type of performer for doing the diagnostic testing.
     */
    @Child(name = "performerType", type = {CodeableConcept.class}, order=13, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Fille role", formalDefinition="Desired type of performer for doing the diagnostic testing." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/participant-role")
    protected CodeableConcept performerType;

    /**
     * The desired perfomer for doing the diagnostic testing.
     */
    @Child(name = "performer", type = {Practitioner.class, Organization.class, Patient.class, Device.class, RelatedPerson.class}, order=14, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Requested Filler", formalDefinition="The desired perfomer for doing the diagnostic testing." )
    protected Reference performer;

    /**
     * The actual object that is the target of the reference (The desired perfomer for doing the diagnostic testing.)
     */
    protected Resource performerTarget;

    /**
     * Reason or justification for the use of this device.
     */
    @Child(name = "reasonCode", type = {CodeableConcept.class}, order=15, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Coded Reason for request", formalDefinition="Reason or justification for the use of this device." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/condition-code")
    protected List<CodeableConcept> reasonCode;

    /**
     * Reason or justification for the use of this device.
     */
    @Child(name = "reasonReference", type = {Reference.class}, order=16, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Linked Reason for request", formalDefinition="Reason or justification for the use of this device." )
    protected List<Reference> reasonReference;
    /**
     * The actual objects that are the target of the reference (Reason or justification for the use of this device.)
     */
    protected List<Resource> reasonReferenceTarget;


    /**
     * Additional clinical information about the patient that may influence the request fulfilment.  For example, this may includes body where on the subject's the device will be used ( i.e. the target site).
     */
    @Child(name = "supportingInfo", type = {Reference.class}, order=17, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Additional clinical information", formalDefinition="Additional clinical information about the patient that may influence the request fulfilment.  For example, this may includes body where on the subject's the device will be used ( i.e. the target site)." )
    protected List<Reference> supportingInfo;
    /**
     * The actual objects that are the target of the reference (Additional clinical information about the patient that may influence the request fulfilment.  For example, this may includes body where on the subject's the device will be used ( i.e. the target site).)
     */
    protected List<Resource> supportingInfoTarget;


    /**
     * Details about this request that were not represented at all or sufficiently in one of the attributes provided in a class. These may include for example a comment, an instruction, or a note associated with the statement.
     */
    @Child(name = "note", type = {Annotation.class}, order=18, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Notes or comments", formalDefinition="Details about this request that were not represented at all or sufficiently in one of the attributes provided in a class. These may include for example a comment, an instruction, or a note associated with the statement." )
    protected List<Annotation> note;

    /**
     * Key events in the history of the request.
     */
    @Child(name = "relevantHistory", type = {Provenance.class}, order=19, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Request provenance", formalDefinition="Key events in the history of the request." )
    protected List<Reference> relevantHistory;
    /**
     * The actual objects that are the target of the reference (Key events in the history of the request.)
     */
    protected List<Provenance> relevantHistoryTarget;


    private static final long serialVersionUID = 387661336L;

  /**
   * Constructor
   */
    public DeviceUseRequest() {
      super();
    }

  /**
   * Constructor
   */
    public DeviceUseRequest(CodeableConcept stage, Type device, Reference subject) {
      super();
      this.stage = stage;
      this.device = device;
      this.subject = subject;
    }

    /**
     * @return {@link #identifier} (Identifiers assigned to this order by the orderer or by the receiver.)
     */
    public List<Identifier> getIdentifier() { 
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      return this.identifier;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public DeviceUseRequest setIdentifier(List<Identifier> theIdentifier) { 
      this.identifier = theIdentifier;
      return this;
    }

    public boolean hasIdentifier() { 
      if (this.identifier == null)
        return false;
      for (Identifier item : this.identifier)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Identifier addIdentifier() { //3
      Identifier t = new Identifier();
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return t;
    }

    public DeviceUseRequest addIdentifier(Identifier t) { //3
      if (t == null)
        return this;
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #identifier}, creating it if it does not already exist
     */
    public Identifier getIdentifierFirstRep() { 
      if (getIdentifier().isEmpty()) {
        addIdentifier();
      }
      return getIdentifier().get(0);
    }

    /**
     * @return {@link #definition} (Protocol or definition followed by this request. For example: The proposed act must be performed if the indicated conditions occur, e.g.., shortness of breath, SpO2 less than x%.)
     */
    public List<Reference> getDefinition() { 
      if (this.definition == null)
        this.definition = new ArrayList<Reference>();
      return this.definition;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public DeviceUseRequest setDefinition(List<Reference> theDefinition) { 
      this.definition = theDefinition;
      return this;
    }

    public boolean hasDefinition() { 
      if (this.definition == null)
        return false;
      for (Reference item : this.definition)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addDefinition() { //3
      Reference t = new Reference();
      if (this.definition == null)
        this.definition = new ArrayList<Reference>();
      this.definition.add(t);
      return t;
    }

    public DeviceUseRequest addDefinition(Reference t) { //3
      if (t == null)
        return this;
      if (this.definition == null)
        this.definition = new ArrayList<Reference>();
      this.definition.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #definition}, creating it if it does not already exist
     */
    public Reference getDefinitionFirstRep() { 
      if (getDefinition().isEmpty()) {
        addDefinition();
      }
      return getDefinition().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Resource> getDefinitionTarget() { 
      if (this.definitionTarget == null)
        this.definitionTarget = new ArrayList<Resource>();
      return this.definitionTarget;
    }

    /**
     * @return {@link #basedOn} (Plan/proposal/order fulfilled by this request.)
     */
    public List<Reference> getBasedOn() { 
      if (this.basedOn == null)
        this.basedOn = new ArrayList<Reference>();
      return this.basedOn;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public DeviceUseRequest setBasedOn(List<Reference> theBasedOn) { 
      this.basedOn = theBasedOn;
      return this;
    }

    public boolean hasBasedOn() { 
      if (this.basedOn == null)
        return false;
      for (Reference item : this.basedOn)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addBasedOn() { //3
      Reference t = new Reference();
      if (this.basedOn == null)
        this.basedOn = new ArrayList<Reference>();
      this.basedOn.add(t);
      return t;
    }

    public DeviceUseRequest addBasedOn(Reference t) { //3
      if (t == null)
        return this;
      if (this.basedOn == null)
        this.basedOn = new ArrayList<Reference>();
      this.basedOn.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #basedOn}, creating it if it does not already exist
     */
    public Reference getBasedOnFirstRep() { 
      if (getBasedOn().isEmpty()) {
        addBasedOn();
      }
      return getBasedOn().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Resource> getBasedOnTarget() { 
      if (this.basedOnTarget == null)
        this.basedOnTarget = new ArrayList<Resource>();
      return this.basedOnTarget;
    }

    /**
     * @return {@link #replaces} (The request takes the place of the referenced completed or terminated request(s).)
     */
    public List<Reference> getReplaces() { 
      if (this.replaces == null)
        this.replaces = new ArrayList<Reference>();
      return this.replaces;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public DeviceUseRequest setReplaces(List<Reference> theReplaces) { 
      this.replaces = theReplaces;
      return this;
    }

    public boolean hasReplaces() { 
      if (this.replaces == null)
        return false;
      for (Reference item : this.replaces)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addReplaces() { //3
      Reference t = new Reference();
      if (this.replaces == null)
        this.replaces = new ArrayList<Reference>();
      this.replaces.add(t);
      return t;
    }

    public DeviceUseRequest addReplaces(Reference t) { //3
      if (t == null)
        return this;
      if (this.replaces == null)
        this.replaces = new ArrayList<Reference>();
      this.replaces.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #replaces}, creating it if it does not already exist
     */
    public Reference getReplacesFirstRep() { 
      if (getReplaces().isEmpty()) {
        addReplaces();
      }
      return getReplaces().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Resource> getReplacesTarget() { 
      if (this.replacesTarget == null)
        this.replacesTarget = new ArrayList<Resource>();
      return this.replacesTarget;
    }

    /**
     * @return {@link #requisition} (Composite request this is part of.)
     */
    public Identifier getRequisition() { 
      if (this.requisition == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DeviceUseRequest.requisition");
        else if (Configuration.doAutoCreate())
          this.requisition = new Identifier(); // cc
      return this.requisition;
    }

    public boolean hasRequisition() { 
      return this.requisition != null && !this.requisition.isEmpty();
    }

    /**
     * @param value {@link #requisition} (Composite request this is part of.)
     */
    public DeviceUseRequest setRequisition(Identifier value) { 
      this.requisition = value;
      return this;
    }

    /**
     * @return {@link #status} (The status of the request.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<DeviceUseRequestStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DeviceUseRequest.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<DeviceUseRequestStatus>(new DeviceUseRequestStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (The status of the request.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public DeviceUseRequest setStatusElement(Enumeration<DeviceUseRequestStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The status of the request.
     */
    public DeviceUseRequestStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The status of the request.
     */
    public DeviceUseRequest setStatus(DeviceUseRequestStatus value) { 
      if (value == null)
        this.status = null;
      else {
        if (this.status == null)
          this.status = new Enumeration<DeviceUseRequestStatus>(new DeviceUseRequestStatusEnumFactory());
        this.status.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #stage} (Whether the request is a proposal, plan, an original order or a reflex order.)
     */
    public CodeableConcept getStage() { 
      if (this.stage == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DeviceUseRequest.stage");
        else if (Configuration.doAutoCreate())
          this.stage = new CodeableConcept(); // cc
      return this.stage;
    }

    public boolean hasStage() { 
      return this.stage != null && !this.stage.isEmpty();
    }

    /**
     * @param value {@link #stage} (Whether the request is a proposal, plan, an original order or a reflex order.)
     */
    public DeviceUseRequest setStage(CodeableConcept value) { 
      this.stage = value;
      return this;
    }

    /**
     * @return {@link #device} (The details of the device to be used.)
     */
    public Type getDevice() { 
      return this.device;
    }

    /**
     * @return {@link #device} (The details of the device to be used.)
     */
    public Reference getDeviceReference() throws FHIRException { 
      if (!(this.device instanceof Reference))
        throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.device.getClass().getName()+" was encountered");
      return (Reference) this.device;
    }

    public boolean hasDeviceReference() { 
      return this.device instanceof Reference;
    }

    /**
     * @return {@link #device} (The details of the device to be used.)
     */
    public CodeableConcept getDeviceCodeableConcept() throws FHIRException { 
      if (!(this.device instanceof CodeableConcept))
        throw new FHIRException("Type mismatch: the type CodeableConcept was expected, but "+this.device.getClass().getName()+" was encountered");
      return (CodeableConcept) this.device;
    }

    public boolean hasDeviceCodeableConcept() { 
      return this.device instanceof CodeableConcept;
    }

    public boolean hasDevice() { 
      return this.device != null && !this.device.isEmpty();
    }

    /**
     * @param value {@link #device} (The details of the device to be used.)
     */
    public DeviceUseRequest setDevice(Type value) { 
      this.device = value;
      return this;
    }

    /**
     * @return {@link #subject} (The patient who will use the device.)
     */
    public Reference getSubject() { 
      if (this.subject == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DeviceUseRequest.subject");
        else if (Configuration.doAutoCreate())
          this.subject = new Reference(); // cc
      return this.subject;
    }

    public boolean hasSubject() { 
      return this.subject != null && !this.subject.isEmpty();
    }

    /**
     * @param value {@link #subject} (The patient who will use the device.)
     */
    public DeviceUseRequest setSubject(Reference value) { 
      this.subject = value;
      return this;
    }

    /**
     * @return {@link #subject} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The patient who will use the device.)
     */
    public Resource getSubjectTarget() { 
      return this.subjectTarget;
    }

    /**
     * @param value {@link #subject} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The patient who will use the device.)
     */
    public DeviceUseRequest setSubjectTarget(Resource value) { 
      this.subjectTarget = value;
      return this;
    }

    /**
     * @return {@link #context} (An encounter that provides additional context in which this request is made.)
     */
    public Reference getContext() { 
      if (this.context == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DeviceUseRequest.context");
        else if (Configuration.doAutoCreate())
          this.context = new Reference(); // cc
      return this.context;
    }

    public boolean hasContext() { 
      return this.context != null && !this.context.isEmpty();
    }

    /**
     * @param value {@link #context} (An encounter that provides additional context in which this request is made.)
     */
    public DeviceUseRequest setContext(Reference value) { 
      this.context = value;
      return this;
    }

    /**
     * @return {@link #context} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (An encounter that provides additional context in which this request is made.)
     */
    public Resource getContextTarget() { 
      return this.contextTarget;
    }

    /**
     * @param value {@link #context} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (An encounter that provides additional context in which this request is made.)
     */
    public DeviceUseRequest setContextTarget(Resource value) { 
      this.contextTarget = value;
      return this;
    }

    /**
     * @return {@link #occurrence} (The timing schedule for the use of the device. The Schedule data type allows many different expressions, for example. "Every 8 hours"; "Three times a day"; "1/2 an hour before breakfast for 10 days from 23-Dec 2011:"; "15 Oct 2013, 17 Oct 2013 and 1 Nov 2013".)
     */
    public Type getOccurrence() { 
      return this.occurrence;
    }

    /**
     * @return {@link #occurrence} (The timing schedule for the use of the device. The Schedule data type allows many different expressions, for example. "Every 8 hours"; "Three times a day"; "1/2 an hour before breakfast for 10 days from 23-Dec 2011:"; "15 Oct 2013, 17 Oct 2013 and 1 Nov 2013".)
     */
    public DateTimeType getOccurrenceDateTimeType() throws FHIRException { 
      if (!(this.occurrence instanceof DateTimeType))
        throw new FHIRException("Type mismatch: the type DateTimeType was expected, but "+this.occurrence.getClass().getName()+" was encountered");
      return (DateTimeType) this.occurrence;
    }

    public boolean hasOccurrenceDateTimeType() { 
      return this.occurrence instanceof DateTimeType;
    }

    /**
     * @return {@link #occurrence} (The timing schedule for the use of the device. The Schedule data type allows many different expressions, for example. "Every 8 hours"; "Three times a day"; "1/2 an hour before breakfast for 10 days from 23-Dec 2011:"; "15 Oct 2013, 17 Oct 2013 and 1 Nov 2013".)
     */
    public Period getOccurrencePeriod() throws FHIRException { 
      if (!(this.occurrence instanceof Period))
        throw new FHIRException("Type mismatch: the type Period was expected, but "+this.occurrence.getClass().getName()+" was encountered");
      return (Period) this.occurrence;
    }

    public boolean hasOccurrencePeriod() { 
      return this.occurrence instanceof Period;
    }

    /**
     * @return {@link #occurrence} (The timing schedule for the use of the device. The Schedule data type allows many different expressions, for example. "Every 8 hours"; "Three times a day"; "1/2 an hour before breakfast for 10 days from 23-Dec 2011:"; "15 Oct 2013, 17 Oct 2013 and 1 Nov 2013".)
     */
    public Timing getOccurrenceTiming() throws FHIRException { 
      if (!(this.occurrence instanceof Timing))
        throw new FHIRException("Type mismatch: the type Timing was expected, but "+this.occurrence.getClass().getName()+" was encountered");
      return (Timing) this.occurrence;
    }

    public boolean hasOccurrenceTiming() { 
      return this.occurrence instanceof Timing;
    }

    public boolean hasOccurrence() { 
      return this.occurrence != null && !this.occurrence.isEmpty();
    }

    /**
     * @param value {@link #occurrence} (The timing schedule for the use of the device. The Schedule data type allows many different expressions, for example. "Every 8 hours"; "Three times a day"; "1/2 an hour before breakfast for 10 days from 23-Dec 2011:"; "15 Oct 2013, 17 Oct 2013 and 1 Nov 2013".)
     */
    public DeviceUseRequest setOccurrence(Type value) { 
      this.occurrence = value;
      return this;
    }

    /**
     * @return {@link #authored} (When the request transitioned to being actionable.). This is the underlying object with id, value and extensions. The accessor "getAuthored" gives direct access to the value
     */
    public DateTimeType getAuthoredElement() { 
      if (this.authored == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DeviceUseRequest.authored");
        else if (Configuration.doAutoCreate())
          this.authored = new DateTimeType(); // bb
      return this.authored;
    }

    public boolean hasAuthoredElement() { 
      return this.authored != null && !this.authored.isEmpty();
    }

    public boolean hasAuthored() { 
      return this.authored != null && !this.authored.isEmpty();
    }

    /**
     * @param value {@link #authored} (When the request transitioned to being actionable.). This is the underlying object with id, value and extensions. The accessor "getAuthored" gives direct access to the value
     */
    public DeviceUseRequest setAuthoredElement(DateTimeType value) { 
      this.authored = value;
      return this;
    }

    /**
     * @return When the request transitioned to being actionable.
     */
    public Date getAuthored() { 
      return this.authored == null ? null : this.authored.getValue();
    }

    /**
     * @param value When the request transitioned to being actionable.
     */
    public DeviceUseRequest setAuthored(Date value) { 
      if (value == null)
        this.authored = null;
      else {
        if (this.authored == null)
          this.authored = new DateTimeType();
        this.authored.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #requester} (Who/what is requesting diagnostics.  The practitioner that holds legal responsibility for ordering the investigation.)
     */
    public Reference getRequester() { 
      if (this.requester == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DeviceUseRequest.requester");
        else if (Configuration.doAutoCreate())
          this.requester = new Reference(); // cc
      return this.requester;
    }

    public boolean hasRequester() { 
      return this.requester != null && !this.requester.isEmpty();
    }

    /**
     * @param value {@link #requester} (Who/what is requesting diagnostics.  The practitioner that holds legal responsibility for ordering the investigation.)
     */
    public DeviceUseRequest setRequester(Reference value) { 
      this.requester = value;
      return this;
    }

    /**
     * @return {@link #requester} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Who/what is requesting diagnostics.  The practitioner that holds legal responsibility for ordering the investigation.)
     */
    public Resource getRequesterTarget() { 
      return this.requesterTarget;
    }

    /**
     * @param value {@link #requester} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Who/what is requesting diagnostics.  The practitioner that holds legal responsibility for ordering the investigation.)
     */
    public DeviceUseRequest setRequesterTarget(Resource value) { 
      this.requesterTarget = value;
      return this;
    }

    /**
     * @return {@link #performerType} (Desired type of performer for doing the diagnostic testing.)
     */
    public CodeableConcept getPerformerType() { 
      if (this.performerType == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DeviceUseRequest.performerType");
        else if (Configuration.doAutoCreate())
          this.performerType = new CodeableConcept(); // cc
      return this.performerType;
    }

    public boolean hasPerformerType() { 
      return this.performerType != null && !this.performerType.isEmpty();
    }

    /**
     * @param value {@link #performerType} (Desired type of performer for doing the diagnostic testing.)
     */
    public DeviceUseRequest setPerformerType(CodeableConcept value) { 
      this.performerType = value;
      return this;
    }

    /**
     * @return {@link #performer} (The desired perfomer for doing the diagnostic testing.)
     */
    public Reference getPerformer() { 
      if (this.performer == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DeviceUseRequest.performer");
        else if (Configuration.doAutoCreate())
          this.performer = new Reference(); // cc
      return this.performer;
    }

    public boolean hasPerformer() { 
      return this.performer != null && !this.performer.isEmpty();
    }

    /**
     * @param value {@link #performer} (The desired perfomer for doing the diagnostic testing.)
     */
    public DeviceUseRequest setPerformer(Reference value) { 
      this.performer = value;
      return this;
    }

    /**
     * @return {@link #performer} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The desired perfomer for doing the diagnostic testing.)
     */
    public Resource getPerformerTarget() { 
      return this.performerTarget;
    }

    /**
     * @param value {@link #performer} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The desired perfomer for doing the diagnostic testing.)
     */
    public DeviceUseRequest setPerformerTarget(Resource value) { 
      this.performerTarget = value;
      return this;
    }

    /**
     * @return {@link #reasonCode} (Reason or justification for the use of this device.)
     */
    public List<CodeableConcept> getReasonCode() { 
      if (this.reasonCode == null)
        this.reasonCode = new ArrayList<CodeableConcept>();
      return this.reasonCode;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public DeviceUseRequest setReasonCode(List<CodeableConcept> theReasonCode) { 
      this.reasonCode = theReasonCode;
      return this;
    }

    public boolean hasReasonCode() { 
      if (this.reasonCode == null)
        return false;
      for (CodeableConcept item : this.reasonCode)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addReasonCode() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.reasonCode == null)
        this.reasonCode = new ArrayList<CodeableConcept>();
      this.reasonCode.add(t);
      return t;
    }

    public DeviceUseRequest addReasonCode(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.reasonCode == null)
        this.reasonCode = new ArrayList<CodeableConcept>();
      this.reasonCode.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #reasonCode}, creating it if it does not already exist
     */
    public CodeableConcept getReasonCodeFirstRep() { 
      if (getReasonCode().isEmpty()) {
        addReasonCode();
      }
      return getReasonCode().get(0);
    }

    /**
     * @return {@link #reasonReference} (Reason or justification for the use of this device.)
     */
    public List<Reference> getReasonReference() { 
      if (this.reasonReference == null)
        this.reasonReference = new ArrayList<Reference>();
      return this.reasonReference;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public DeviceUseRequest setReasonReference(List<Reference> theReasonReference) { 
      this.reasonReference = theReasonReference;
      return this;
    }

    public boolean hasReasonReference() { 
      if (this.reasonReference == null)
        return false;
      for (Reference item : this.reasonReference)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addReasonReference() { //3
      Reference t = new Reference();
      if (this.reasonReference == null)
        this.reasonReference = new ArrayList<Reference>();
      this.reasonReference.add(t);
      return t;
    }

    public DeviceUseRequest addReasonReference(Reference t) { //3
      if (t == null)
        return this;
      if (this.reasonReference == null)
        this.reasonReference = new ArrayList<Reference>();
      this.reasonReference.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #reasonReference}, creating it if it does not already exist
     */
    public Reference getReasonReferenceFirstRep() { 
      if (getReasonReference().isEmpty()) {
        addReasonReference();
      }
      return getReasonReference().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Resource> getReasonReferenceTarget() { 
      if (this.reasonReferenceTarget == null)
        this.reasonReferenceTarget = new ArrayList<Resource>();
      return this.reasonReferenceTarget;
    }

    /**
     * @return {@link #supportingInfo} (Additional clinical information about the patient that may influence the request fulfilment.  For example, this may includes body where on the subject's the device will be used ( i.e. the target site).)
     */
    public List<Reference> getSupportingInfo() { 
      if (this.supportingInfo == null)
        this.supportingInfo = new ArrayList<Reference>();
      return this.supportingInfo;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public DeviceUseRequest setSupportingInfo(List<Reference> theSupportingInfo) { 
      this.supportingInfo = theSupportingInfo;
      return this;
    }

    public boolean hasSupportingInfo() { 
      if (this.supportingInfo == null)
        return false;
      for (Reference item : this.supportingInfo)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addSupportingInfo() { //3
      Reference t = new Reference();
      if (this.supportingInfo == null)
        this.supportingInfo = new ArrayList<Reference>();
      this.supportingInfo.add(t);
      return t;
    }

    public DeviceUseRequest addSupportingInfo(Reference t) { //3
      if (t == null)
        return this;
      if (this.supportingInfo == null)
        this.supportingInfo = new ArrayList<Reference>();
      this.supportingInfo.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #supportingInfo}, creating it if it does not already exist
     */
    public Reference getSupportingInfoFirstRep() { 
      if (getSupportingInfo().isEmpty()) {
        addSupportingInfo();
      }
      return getSupportingInfo().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Resource> getSupportingInfoTarget() { 
      if (this.supportingInfoTarget == null)
        this.supportingInfoTarget = new ArrayList<Resource>();
      return this.supportingInfoTarget;
    }

    /**
     * @return {@link #note} (Details about this request that were not represented at all or sufficiently in one of the attributes provided in a class. These may include for example a comment, an instruction, or a note associated with the statement.)
     */
    public List<Annotation> getNote() { 
      if (this.note == null)
        this.note = new ArrayList<Annotation>();
      return this.note;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public DeviceUseRequest setNote(List<Annotation> theNote) { 
      this.note = theNote;
      return this;
    }

    public boolean hasNote() { 
      if (this.note == null)
        return false;
      for (Annotation item : this.note)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Annotation addNote() { //3
      Annotation t = new Annotation();
      if (this.note == null)
        this.note = new ArrayList<Annotation>();
      this.note.add(t);
      return t;
    }

    public DeviceUseRequest addNote(Annotation t) { //3
      if (t == null)
        return this;
      if (this.note == null)
        this.note = new ArrayList<Annotation>();
      this.note.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #note}, creating it if it does not already exist
     */
    public Annotation getNoteFirstRep() { 
      if (getNote().isEmpty()) {
        addNote();
      }
      return getNote().get(0);
    }

    /**
     * @return {@link #relevantHistory} (Key events in the history of the request.)
     */
    public List<Reference> getRelevantHistory() { 
      if (this.relevantHistory == null)
        this.relevantHistory = new ArrayList<Reference>();
      return this.relevantHistory;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public DeviceUseRequest setRelevantHistory(List<Reference> theRelevantHistory) { 
      this.relevantHistory = theRelevantHistory;
      return this;
    }

    public boolean hasRelevantHistory() { 
      if (this.relevantHistory == null)
        return false;
      for (Reference item : this.relevantHistory)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addRelevantHistory() { //3
      Reference t = new Reference();
      if (this.relevantHistory == null)
        this.relevantHistory = new ArrayList<Reference>();
      this.relevantHistory.add(t);
      return t;
    }

    public DeviceUseRequest addRelevantHistory(Reference t) { //3
      if (t == null)
        return this;
      if (this.relevantHistory == null)
        this.relevantHistory = new ArrayList<Reference>();
      this.relevantHistory.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #relevantHistory}, creating it if it does not already exist
     */
    public Reference getRelevantHistoryFirstRep() { 
      if (getRelevantHistory().isEmpty()) {
        addRelevantHistory();
      }
      return getRelevantHistory().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Provenance> getRelevantHistoryTarget() { 
      if (this.relevantHistoryTarget == null)
        this.relevantHistoryTarget = new ArrayList<Provenance>();
      return this.relevantHistoryTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public Provenance addRelevantHistoryTarget() { 
      Provenance r = new Provenance();
      if (this.relevantHistoryTarget == null)
        this.relevantHistoryTarget = new ArrayList<Provenance>();
      this.relevantHistoryTarget.add(r);
      return r;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "Identifiers assigned to this order by the orderer or by the receiver.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("definition", "Reference(Any)", "Protocol or definition followed by this request. For example: The proposed act must be performed if the indicated conditions occur, e.g.., shortness of breath, SpO2 less than x%.", 0, java.lang.Integer.MAX_VALUE, definition));
        childrenList.add(new Property("basedOn", "Reference(Any)", "Plan/proposal/order fulfilled by this request.", 0, java.lang.Integer.MAX_VALUE, basedOn));
        childrenList.add(new Property("replaces", "Reference(Any)", "The request takes the place of the referenced completed or terminated request(s).", 0, java.lang.Integer.MAX_VALUE, replaces));
        childrenList.add(new Property("requisition", "Identifier", "Composite request this is part of.", 0, java.lang.Integer.MAX_VALUE, requisition));
        childrenList.add(new Property("status", "code", "The status of the request.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("stage", "CodeableConcept", "Whether the request is a proposal, plan, an original order or a reflex order.", 0, java.lang.Integer.MAX_VALUE, stage));
        childrenList.add(new Property("device[x]", "Reference(Device)|CodeableConcept", "The details of the device to be used.", 0, java.lang.Integer.MAX_VALUE, device));
        childrenList.add(new Property("subject", "Reference(Patient|Group|Location|Device)", "The patient who will use the device.", 0, java.lang.Integer.MAX_VALUE, subject));
        childrenList.add(new Property("context", "Reference(Encounter|EpisodeOfCare)", "An encounter that provides additional context in which this request is made.", 0, java.lang.Integer.MAX_VALUE, context));
        childrenList.add(new Property("occurrence[x]", "dateTime|Period|Timing", "The timing schedule for the use of the device. The Schedule data type allows many different expressions, for example. \"Every 8 hours\"; \"Three times a day\"; \"1/2 an hour before breakfast for 10 days from 23-Dec 2011:\"; \"15 Oct 2013, 17 Oct 2013 and 1 Nov 2013\".", 0, java.lang.Integer.MAX_VALUE, occurrence));
        childrenList.add(new Property("authored", "dateTime", "When the request transitioned to being actionable.", 0, java.lang.Integer.MAX_VALUE, authored));
        childrenList.add(new Property("requester", "Reference(Device|Practitioner|Organization)", "Who/what is requesting diagnostics.  The practitioner that holds legal responsibility for ordering the investigation.", 0, java.lang.Integer.MAX_VALUE, requester));
        childrenList.add(new Property("performerType", "CodeableConcept", "Desired type of performer for doing the diagnostic testing.", 0, java.lang.Integer.MAX_VALUE, performerType));
        childrenList.add(new Property("performer", "Reference(Practitioner|Organization|Patient|Device|RelatedPerson)", "The desired perfomer for doing the diagnostic testing.", 0, java.lang.Integer.MAX_VALUE, performer));
        childrenList.add(new Property("reasonCode", "CodeableConcept", "Reason or justification for the use of this device.", 0, java.lang.Integer.MAX_VALUE, reasonCode));
        childrenList.add(new Property("reasonReference", "Reference(Any)", "Reason or justification for the use of this device.", 0, java.lang.Integer.MAX_VALUE, reasonReference));
        childrenList.add(new Property("supportingInfo", "Reference(Any)", "Additional clinical information about the patient that may influence the request fulfilment.  For example, this may includes body where on the subject's the device will be used ( i.e. the target site).", 0, java.lang.Integer.MAX_VALUE, supportingInfo));
        childrenList.add(new Property("note", "Annotation", "Details about this request that were not represented at all or sufficiently in one of the attributes provided in a class. These may include for example a comment, an instruction, or a note associated with the statement.", 0, java.lang.Integer.MAX_VALUE, note));
        childrenList.add(new Property("relevantHistory", "Reference(Provenance)", "Key events in the history of the request.", 0, java.lang.Integer.MAX_VALUE, relevantHistory));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case -1014418093: /*definition*/ return this.definition == null ? new Base[0] : this.definition.toArray(new Base[this.definition.size()]); // Reference
        case -332612366: /*basedOn*/ return this.basedOn == null ? new Base[0] : this.basedOn.toArray(new Base[this.basedOn.size()]); // Reference
        case -430332865: /*replaces*/ return this.replaces == null ? new Base[0] : this.replaces.toArray(new Base[this.replaces.size()]); // Reference
        case 395923612: /*requisition*/ return this.requisition == null ? new Base[0] : new Base[] {this.requisition}; // Identifier
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<DeviceUseRequestStatus>
        case 109757182: /*stage*/ return this.stage == null ? new Base[0] : new Base[] {this.stage}; // CodeableConcept
        case -1335157162: /*device*/ return this.device == null ? new Base[0] : new Base[] {this.device}; // Type
        case -1867885268: /*subject*/ return this.subject == null ? new Base[0] : new Base[] {this.subject}; // Reference
        case 951530927: /*context*/ return this.context == null ? new Base[0] : new Base[] {this.context}; // Reference
        case 1687874001: /*occurrence*/ return this.occurrence == null ? new Base[0] : new Base[] {this.occurrence}; // Type
        case 1433073514: /*authored*/ return this.authored == null ? new Base[0] : new Base[] {this.authored}; // DateTimeType
        case 693933948: /*requester*/ return this.requester == null ? new Base[0] : new Base[] {this.requester}; // Reference
        case -901444568: /*performerType*/ return this.performerType == null ? new Base[0] : new Base[] {this.performerType}; // CodeableConcept
        case 481140686: /*performer*/ return this.performer == null ? new Base[0] : new Base[] {this.performer}; // Reference
        case 722137681: /*reasonCode*/ return this.reasonCode == null ? new Base[0] : this.reasonCode.toArray(new Base[this.reasonCode.size()]); // CodeableConcept
        case -1146218137: /*reasonReference*/ return this.reasonReference == null ? new Base[0] : this.reasonReference.toArray(new Base[this.reasonReference.size()]); // Reference
        case 1922406657: /*supportingInfo*/ return this.supportingInfo == null ? new Base[0] : this.supportingInfo.toArray(new Base[this.supportingInfo.size()]); // Reference
        case 3387378: /*note*/ return this.note == null ? new Base[0] : this.note.toArray(new Base[this.note.size()]); // Annotation
        case 1538891575: /*relevantHistory*/ return this.relevantHistory == null ? new Base[0] : this.relevantHistory.toArray(new Base[this.relevantHistory.size()]); // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.getIdentifier().add(castToIdentifier(value)); // Identifier
          break;
        case -1014418093: // definition
          this.getDefinition().add(castToReference(value)); // Reference
          break;
        case -332612366: // basedOn
          this.getBasedOn().add(castToReference(value)); // Reference
          break;
        case -430332865: // replaces
          this.getReplaces().add(castToReference(value)); // Reference
          break;
        case 395923612: // requisition
          this.requisition = castToIdentifier(value); // Identifier
          break;
        case -892481550: // status
          this.status = new DeviceUseRequestStatusEnumFactory().fromType(value); // Enumeration<DeviceUseRequestStatus>
          break;
        case 109757182: // stage
          this.stage = castToCodeableConcept(value); // CodeableConcept
          break;
        case -1335157162: // device
          this.device = castToType(value); // Type
          break;
        case -1867885268: // subject
          this.subject = castToReference(value); // Reference
          break;
        case 951530927: // context
          this.context = castToReference(value); // Reference
          break;
        case 1687874001: // occurrence
          this.occurrence = castToType(value); // Type
          break;
        case 1433073514: // authored
          this.authored = castToDateTime(value); // DateTimeType
          break;
        case 693933948: // requester
          this.requester = castToReference(value); // Reference
          break;
        case -901444568: // performerType
          this.performerType = castToCodeableConcept(value); // CodeableConcept
          break;
        case 481140686: // performer
          this.performer = castToReference(value); // Reference
          break;
        case 722137681: // reasonCode
          this.getReasonCode().add(castToCodeableConcept(value)); // CodeableConcept
          break;
        case -1146218137: // reasonReference
          this.getReasonReference().add(castToReference(value)); // Reference
          break;
        case 1922406657: // supportingInfo
          this.getSupportingInfo().add(castToReference(value)); // Reference
          break;
        case 3387378: // note
          this.getNote().add(castToAnnotation(value)); // Annotation
          break;
        case 1538891575: // relevantHistory
          this.getRelevantHistory().add(castToReference(value)); // Reference
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier"))
          this.getIdentifier().add(castToIdentifier(value));
        else if (name.equals("definition"))
          this.getDefinition().add(castToReference(value));
        else if (name.equals("basedOn"))
          this.getBasedOn().add(castToReference(value));
        else if (name.equals("replaces"))
          this.getReplaces().add(castToReference(value));
        else if (name.equals("requisition"))
          this.requisition = castToIdentifier(value); // Identifier
        else if (name.equals("status"))
          this.status = new DeviceUseRequestStatusEnumFactory().fromType(value); // Enumeration<DeviceUseRequestStatus>
        else if (name.equals("stage"))
          this.stage = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("device[x]"))
          this.device = castToType(value); // Type
        else if (name.equals("subject"))
          this.subject = castToReference(value); // Reference
        else if (name.equals("context"))
          this.context = castToReference(value); // Reference
        else if (name.equals("occurrence[x]"))
          this.occurrence = castToType(value); // Type
        else if (name.equals("authored"))
          this.authored = castToDateTime(value); // DateTimeType
        else if (name.equals("requester"))
          this.requester = castToReference(value); // Reference
        else if (name.equals("performerType"))
          this.performerType = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("performer"))
          this.performer = castToReference(value); // Reference
        else if (name.equals("reasonCode"))
          this.getReasonCode().add(castToCodeableConcept(value));
        else if (name.equals("reasonReference"))
          this.getReasonReference().add(castToReference(value));
        else if (name.equals("supportingInfo"))
          this.getSupportingInfo().add(castToReference(value));
        else if (name.equals("note"))
          this.getNote().add(castToAnnotation(value));
        else if (name.equals("relevantHistory"))
          this.getRelevantHistory().add(castToReference(value));
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return addIdentifier(); // Identifier
        case -1014418093:  return addDefinition(); // Reference
        case -332612366:  return addBasedOn(); // Reference
        case -430332865:  return addReplaces(); // Reference
        case 395923612:  return getRequisition(); // Identifier
        case -892481550: throw new FHIRException("Cannot make property status as it is not a complex type"); // Enumeration<DeviceUseRequestStatus>
        case 109757182:  return getStage(); // CodeableConcept
        case 25206378:  return getDevice(); // Type
        case -1867885268:  return getSubject(); // Reference
        case 951530927:  return getContext(); // Reference
        case -2022646513:  return getOccurrence(); // Type
        case 1433073514: throw new FHIRException("Cannot make property authored as it is not a complex type"); // DateTimeType
        case 693933948:  return getRequester(); // Reference
        case -901444568:  return getPerformerType(); // CodeableConcept
        case 481140686:  return getPerformer(); // Reference
        case 722137681:  return addReasonCode(); // CodeableConcept
        case -1146218137:  return addReasonReference(); // Reference
        case 1922406657:  return addSupportingInfo(); // Reference
        case 3387378:  return addNote(); // Annotation
        case 1538891575:  return addRelevantHistory(); // Reference
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("definition")) {
          return addDefinition();
        }
        else if (name.equals("basedOn")) {
          return addBasedOn();
        }
        else if (name.equals("replaces")) {
          return addReplaces();
        }
        else if (name.equals("requisition")) {
          this.requisition = new Identifier();
          return this.requisition;
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type DeviceUseRequest.status");
        }
        else if (name.equals("stage")) {
          this.stage = new CodeableConcept();
          return this.stage;
        }
        else if (name.equals("deviceReference")) {
          this.device = new Reference();
          return this.device;
        }
        else if (name.equals("deviceCodeableConcept")) {
          this.device = new CodeableConcept();
          return this.device;
        }
        else if (name.equals("subject")) {
          this.subject = new Reference();
          return this.subject;
        }
        else if (name.equals("context")) {
          this.context = new Reference();
          return this.context;
        }
        else if (name.equals("occurrenceDateTime")) {
          this.occurrence = new DateTimeType();
          return this.occurrence;
        }
        else if (name.equals("occurrencePeriod")) {
          this.occurrence = new Period();
          return this.occurrence;
        }
        else if (name.equals("occurrenceTiming")) {
          this.occurrence = new Timing();
          return this.occurrence;
        }
        else if (name.equals("authored")) {
          throw new FHIRException("Cannot call addChild on a primitive type DeviceUseRequest.authored");
        }
        else if (name.equals("requester")) {
          this.requester = new Reference();
          return this.requester;
        }
        else if (name.equals("performerType")) {
          this.performerType = new CodeableConcept();
          return this.performerType;
        }
        else if (name.equals("performer")) {
          this.performer = new Reference();
          return this.performer;
        }
        else if (name.equals("reasonCode")) {
          return addReasonCode();
        }
        else if (name.equals("reasonReference")) {
          return addReasonReference();
        }
        else if (name.equals("supportingInfo")) {
          return addSupportingInfo();
        }
        else if (name.equals("note")) {
          return addNote();
        }
        else if (name.equals("relevantHistory")) {
          return addRelevantHistory();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "DeviceUseRequest";

  }

      public DeviceUseRequest copy() {
        DeviceUseRequest dst = new DeviceUseRequest();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        if (definition != null) {
          dst.definition = new ArrayList<Reference>();
          for (Reference i : definition)
            dst.definition.add(i.copy());
        };
        if (basedOn != null) {
          dst.basedOn = new ArrayList<Reference>();
          for (Reference i : basedOn)
            dst.basedOn.add(i.copy());
        };
        if (replaces != null) {
          dst.replaces = new ArrayList<Reference>();
          for (Reference i : replaces)
            dst.replaces.add(i.copy());
        };
        dst.requisition = requisition == null ? null : requisition.copy();
        dst.status = status == null ? null : status.copy();
        dst.stage = stage == null ? null : stage.copy();
        dst.device = device == null ? null : device.copy();
        dst.subject = subject == null ? null : subject.copy();
        dst.context = context == null ? null : context.copy();
        dst.occurrence = occurrence == null ? null : occurrence.copy();
        dst.authored = authored == null ? null : authored.copy();
        dst.requester = requester == null ? null : requester.copy();
        dst.performerType = performerType == null ? null : performerType.copy();
        dst.performer = performer == null ? null : performer.copy();
        if (reasonCode != null) {
          dst.reasonCode = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : reasonCode)
            dst.reasonCode.add(i.copy());
        };
        if (reasonReference != null) {
          dst.reasonReference = new ArrayList<Reference>();
          for (Reference i : reasonReference)
            dst.reasonReference.add(i.copy());
        };
        if (supportingInfo != null) {
          dst.supportingInfo = new ArrayList<Reference>();
          for (Reference i : supportingInfo)
            dst.supportingInfo.add(i.copy());
        };
        if (note != null) {
          dst.note = new ArrayList<Annotation>();
          for (Annotation i : note)
            dst.note.add(i.copy());
        };
        if (relevantHistory != null) {
          dst.relevantHistory = new ArrayList<Reference>();
          for (Reference i : relevantHistory)
            dst.relevantHistory.add(i.copy());
        };
        return dst;
      }

      protected DeviceUseRequest typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof DeviceUseRequest))
          return false;
        DeviceUseRequest o = (DeviceUseRequest) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(definition, o.definition, true)
           && compareDeep(basedOn, o.basedOn, true) && compareDeep(replaces, o.replaces, true) && compareDeep(requisition, o.requisition, true)
           && compareDeep(status, o.status, true) && compareDeep(stage, o.stage, true) && compareDeep(device, o.device, true)
           && compareDeep(subject, o.subject, true) && compareDeep(context, o.context, true) && compareDeep(occurrence, o.occurrence, true)
           && compareDeep(authored, o.authored, true) && compareDeep(requester, o.requester, true) && compareDeep(performerType, o.performerType, true)
           && compareDeep(performer, o.performer, true) && compareDeep(reasonCode, o.reasonCode, true) && compareDeep(reasonReference, o.reasonReference, true)
           && compareDeep(supportingInfo, o.supportingInfo, true) && compareDeep(note, o.note, true) && compareDeep(relevantHistory, o.relevantHistory, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof DeviceUseRequest))
          return false;
        DeviceUseRequest o = (DeviceUseRequest) other;
        return compareValues(status, o.status, true) && compareValues(authored, o.authored, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, definition, basedOn
          , replaces, requisition, status, stage, device, subject, context, occurrence
          , authored, requester, performerType, performer, reasonCode, reasonReference, supportingInfo
          , note, relevantHistory);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.DeviceUseRequest;
   }

 /**
   * Search parameter: <b>requester</b>
   * <p>
   * Description: <b>Who/what is requesting service </b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DeviceUseRequest.requester</b><br>
   * </p>
   */
  @SearchParamDefinition(name="requester", path="DeviceUseRequest.requester", description="Who/what is requesting service ", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Device"), @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient"), @ca.uhn.fhir.model.api.annotation.Compartment(name="Practitioner") }, target={Device.class, Organization.class, Practitioner.class } )
  public static final String SP_REQUESTER = "requester";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>requester</b>
   * <p>
   * Description: <b>Who/what is requesting service </b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DeviceUseRequest.requester</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam REQUESTER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_REQUESTER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>DeviceUseRequest:requester</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_REQUESTER = new ca.uhn.fhir.model.api.Include("DeviceUseRequest:requester").toLocked();

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>Business identifier for request/order</b><br>
   * Type: <b>token</b><br>
   * Path: <b>DeviceUseRequest.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="DeviceUseRequest.identifier", description="Business identifier for request/order", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>Business identifier for request/order</b><br>
   * Type: <b>token</b><br>
   * Path: <b>DeviceUseRequest.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>code</b>
   * <p>
   * Description: <b>Code for what is being requested/ordered</b><br>
   * Type: <b>token</b><br>
   * Path: <b>DeviceUseRequest.deviceCodeableConcept</b><br>
   * </p>
   */
  @SearchParamDefinition(name="code", path="DeviceUseRequest.device.as(CodeableConcept)", description="Code for what is being requested/ordered", type="token" )
  public static final String SP_CODE = "code";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>code</b>
   * <p>
   * Description: <b>Code for what is being requested/ordered</b><br>
   * Type: <b>token</b><br>
   * Path: <b>DeviceUseRequest.deviceCodeableConcept</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CODE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CODE);

 /**
   * Search parameter: <b>event-date</b>
   * <p>
   * Description: <b>When service should occur</b><br>
   * Type: <b>date</b><br>
   * Path: <b>DeviceUseRequest.occurrenceDateTime, DeviceUseRequest.occurrencePeriod</b><br>
   * </p>
   */
  @SearchParamDefinition(name="event-date", path="DeviceUseRequest.occurrence.as(DateTime) | DeviceUseRequest.occurrence.as(Period)", description="When service should occur", type="date" )
  public static final String SP_EVENT_DATE = "event-date";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>event-date</b>
   * <p>
   * Description: <b>When service should occur</b><br>
   * Type: <b>date</b><br>
   * Path: <b>DeviceUseRequest.occurrenceDateTime, DeviceUseRequest.occurrencePeriod</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam EVENT_DATE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_EVENT_DATE);

 /**
   * Search parameter: <b>requisition</b>
   * <p>
   * Description: <b>Composite request this is part of</b><br>
   * Type: <b>token</b><br>
   * Path: <b>DeviceUseRequest.requisition</b><br>
   * </p>
   */
  @SearchParamDefinition(name="requisition", path="DeviceUseRequest.requisition", description="Composite request this is part of", type="token" )
  public static final String SP_REQUISITION = "requisition";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>requisition</b>
   * <p>
   * Description: <b>Composite request this is part of</b><br>
   * Type: <b>token</b><br>
   * Path: <b>DeviceUseRequest.requisition</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam REQUISITION = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_REQUISITION);

 /**
   * Search parameter: <b>replaces</b>
   * <p>
   * Description: <b>Request takes the place of referenced completed or terminated requests</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DeviceUseRequest.replaces</b><br>
   * </p>
   */
  @SearchParamDefinition(name="replaces", path="DeviceUseRequest.replaces", description="Request takes the place of referenced completed or terminated requests", type="reference" )
  public static final String SP_REPLACES = "replaces";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>replaces</b>
   * <p>
   * Description: <b>Request takes the place of referenced completed or terminated requests</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DeviceUseRequest.replaces</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam REPLACES = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_REPLACES);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>DeviceUseRequest:replaces</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_REPLACES = new ca.uhn.fhir.model.api.Include("DeviceUseRequest:replaces").toLocked();

 /**
   * Search parameter: <b>subject</b>
   * <p>
   * Description: <b>Individual the service is ordered for</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DeviceUseRequest.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="subject", path="DeviceUseRequest.subject", description="Individual the service is ordered for", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Device"), @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient") }, target={Device.class, Group.class, Location.class, Patient.class } )
  public static final String SP_SUBJECT = "subject";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>subject</b>
   * <p>
   * Description: <b>Individual the service is ordered for</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DeviceUseRequest.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SUBJECT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SUBJECT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>DeviceUseRequest:subject</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SUBJECT = new ca.uhn.fhir.model.api.Include("DeviceUseRequest:subject").toLocked();

 /**
   * Search parameter: <b>author-date</b>
   * <p>
   * Description: <b>When the request transitioned to being actionable</b><br>
   * Type: <b>date</b><br>
   * Path: <b>DeviceUseRequest.authored</b><br>
   * </p>
   */
  @SearchParamDefinition(name="author-date", path="DeviceUseRequest.authored", description="When the request transitioned to being actionable", type="date" )
  public static final String SP_AUTHOR_DATE = "author-date";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>author-date</b>
   * <p>
   * Description: <b>When the request transitioned to being actionable</b><br>
   * Type: <b>date</b><br>
   * Path: <b>DeviceUseRequest.authored</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam AUTHOR_DATE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_AUTHOR_DATE);

 /**
   * Search parameter: <b>encounter</b>
   * <p>
   * Description: <b>Encounter or Episode during which request was created</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DeviceUseRequest.context</b><br>
   * </p>
   */
  @SearchParamDefinition(name="encounter", path="DeviceUseRequest.context", description="Encounter or Episode during which request was created", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Encounter") }, target={Encounter.class, EpisodeOfCare.class } )
  public static final String SP_ENCOUNTER = "encounter";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>encounter</b>
   * <p>
   * Description: <b>Encounter or Episode during which request was created</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DeviceUseRequest.context</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam ENCOUNTER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_ENCOUNTER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>DeviceUseRequest:encounter</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_ENCOUNTER = new ca.uhn.fhir.model.api.Include("DeviceUseRequest:encounter").toLocked();

 /**
   * Search parameter: <b>based-on</b>
   * <p>
   * Description: <b>Plan/proposal/order fulfilled by this request</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DeviceUseRequest.basedOn</b><br>
   * </p>
   */
  @SearchParamDefinition(name="based-on", path="DeviceUseRequest.basedOn", description="Plan/proposal/order fulfilled by this request", type="reference" )
  public static final String SP_BASED_ON = "based-on";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>based-on</b>
   * <p>
   * Description: <b>Plan/proposal/order fulfilled by this request</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DeviceUseRequest.basedOn</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam BASED_ON = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_BASED_ON);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>DeviceUseRequest:based-on</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_BASED_ON = new ca.uhn.fhir.model.api.Include("DeviceUseRequest:based-on").toLocked();

 /**
   * Search parameter: <b>stage</b>
   * <p>
   * Description: <b>proposal | plan | original-order |reflex-order</b><br>
   * Type: <b>token</b><br>
   * Path: <b>DeviceUseRequest.stage</b><br>
   * </p>
   */
  @SearchParamDefinition(name="stage", path="DeviceUseRequest.stage", description="proposal | plan | original-order |reflex-order", type="token" )
  public static final String SP_STAGE = "stage";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>stage</b>
   * <p>
   * Description: <b>proposal | plan | original-order |reflex-order</b><br>
   * Type: <b>token</b><br>
   * Path: <b>DeviceUseRequest.stage</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STAGE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STAGE);

 /**
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>Individual the service is ordered for</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DeviceUseRequest.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="DeviceUseRequest.subject", description="Individual the service is ordered for", type="reference", target={Patient.class } )
  public static final String SP_PATIENT = "patient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patient</b>
   * <p>
   * Description: <b>Individual the service is ordered for</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DeviceUseRequest.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PATIENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PATIENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>DeviceUseRequest:patient</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PATIENT = new ca.uhn.fhir.model.api.Include("DeviceUseRequest:patient").toLocked();

 /**
   * Search parameter: <b>filler</b>
   * <p>
   * Description: <b>Desired performer for service</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DeviceUseRequest.performer</b><br>
   * </p>
   */
  @SearchParamDefinition(name="filler", path="DeviceUseRequest.performer", description="Desired performer for service", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Device"), @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient"), @ca.uhn.fhir.model.api.annotation.Compartment(name="Practitioner") }, target={Device.class, Organization.class, Patient.class, Practitioner.class, RelatedPerson.class } )
  public static final String SP_FILLER = "filler";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>filler</b>
   * <p>
   * Description: <b>Desired performer for service</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DeviceUseRequest.performer</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam FILLER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_FILLER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>DeviceUseRequest:filler</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_FILLER = new ca.uhn.fhir.model.api.Include("DeviceUseRequest:filler").toLocked();

 /**
   * Search parameter: <b>definition</b>
   * <p>
   * Description: <b>Protocol or definition followed by this request</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DeviceUseRequest.definition</b><br>
   * </p>
   */
  @SearchParamDefinition(name="definition", path="DeviceUseRequest.definition", description="Protocol or definition followed by this request", type="reference" )
  public static final String SP_DEFINITION = "definition";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>definition</b>
   * <p>
   * Description: <b>Protocol or definition followed by this request</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DeviceUseRequest.definition</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam DEFINITION = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_DEFINITION);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>DeviceUseRequest:definition</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_DEFINITION = new ca.uhn.fhir.model.api.Include("DeviceUseRequest:definition").toLocked();

 /**
   * Search parameter: <b>device</b>
   * <p>
   * Description: <b>Reference to resource that is being requested/ordered</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DeviceUseRequest.deviceReference</b><br>
   * </p>
   */
  @SearchParamDefinition(name="device", path="DeviceUseRequest.device.as(Reference)", description="Reference to resource that is being requested/ordered", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Device") }, target={Device.class } )
  public static final String SP_DEVICE = "device";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>device</b>
   * <p>
   * Description: <b>Reference to resource that is being requested/ordered</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DeviceUseRequest.deviceReference</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam DEVICE = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_DEVICE);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>DeviceUseRequest:device</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_DEVICE = new ca.uhn.fhir.model.api.Include("DeviceUseRequest:device").toLocked();

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>entered-in-error | draft | active |suspended | completed </b><br>
   * Type: <b>token</b><br>
   * Path: <b>DeviceUseRequest.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="DeviceUseRequest.status", description="entered-in-error | draft | active |suspended | completed ", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>entered-in-error | draft | active |suspended | completed </b><br>
   * Type: <b>token</b><br>
   * Path: <b>DeviceUseRequest.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);


}

