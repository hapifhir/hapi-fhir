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

// Generated on Thu, Aug 25, 2016 23:04-0400 for FHIR v1.6.0
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hl7.fhir.exceptions.FHIRException;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
/**
 * A record of a request for a diagnostic investigation service to be performed.
 */
@ResourceDef(name="DiagnosticRequest", profile="http://hl7.org/fhir/Profile/DiagnosticRequest")
public class DiagnosticRequest extends DomainResource {

    public enum DiagnosticRequestStatus {
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
        public static DiagnosticRequestStatus fromCode(String codeString) throws FHIRException {
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
          throw new FHIRException("Unknown DiagnosticRequestStatus code '"+codeString+"'");
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

  public static class DiagnosticRequestStatusEnumFactory implements EnumFactory<DiagnosticRequestStatus> {
    public DiagnosticRequestStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("draft".equals(codeString))
          return DiagnosticRequestStatus.DRAFT;
        if ("active".equals(codeString))
          return DiagnosticRequestStatus.ACTIVE;
        if ("suspended".equals(codeString))
          return DiagnosticRequestStatus.SUSPENDED;
        if ("completed".equals(codeString))
          return DiagnosticRequestStatus.COMPLETED;
        if ("entered-in-error".equals(codeString))
          return DiagnosticRequestStatus.ENTEREDINERROR;
        if ("cancelled".equals(codeString))
          return DiagnosticRequestStatus.CANCELLED;
        throw new IllegalArgumentException("Unknown DiagnosticRequestStatus code '"+codeString+"'");
        }
        public Enumeration<DiagnosticRequestStatus> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("draft".equals(codeString))
          return new Enumeration<DiagnosticRequestStatus>(this, DiagnosticRequestStatus.DRAFT);
        if ("active".equals(codeString))
          return new Enumeration<DiagnosticRequestStatus>(this, DiagnosticRequestStatus.ACTIVE);
        if ("suspended".equals(codeString))
          return new Enumeration<DiagnosticRequestStatus>(this, DiagnosticRequestStatus.SUSPENDED);
        if ("completed".equals(codeString))
          return new Enumeration<DiagnosticRequestStatus>(this, DiagnosticRequestStatus.COMPLETED);
        if ("entered-in-error".equals(codeString))
          return new Enumeration<DiagnosticRequestStatus>(this, DiagnosticRequestStatus.ENTEREDINERROR);
        if ("cancelled".equals(codeString))
          return new Enumeration<DiagnosticRequestStatus>(this, DiagnosticRequestStatus.CANCELLED);
        throw new FHIRException("Unknown DiagnosticRequestStatus code '"+codeString+"'");
        }
    public String toCode(DiagnosticRequestStatus code) {
      if (code == DiagnosticRequestStatus.DRAFT)
        return "draft";
      if (code == DiagnosticRequestStatus.ACTIVE)
        return "active";
      if (code == DiagnosticRequestStatus.SUSPENDED)
        return "suspended";
      if (code == DiagnosticRequestStatus.COMPLETED)
        return "completed";
      if (code == DiagnosticRequestStatus.ENTEREDINERROR)
        return "entered-in-error";
      if (code == DiagnosticRequestStatus.CANCELLED)
        return "cancelled";
      return "?";
      }
    public String toSystem(DiagnosticRequestStatus code) {
      return code.getSystem();
      }
    }

    /**
     * Identifiers assigned to this order instance by the orderer and/or  the receiver and/or order fulfiller.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Identifiers assigned to this order", formalDefinition="Identifiers assigned to this order instance by the orderer and/or  the receiver and/or order fulfiller." )
    protected List<Identifier> identifier;

    /**
     * Protocol or definition followed by this request.
     */
    @Child(name = "definition", type = {Reference.class}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Protocol or definition", formalDefinition="Protocol or definition followed by this request." )
    protected List<Reference> definition;
    /**
     * The actual objects that are the target of the reference (Protocol or definition followed by this request.)
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
     * The status of the order.
     */
    @Child(name = "status", type = {CodeType.class}, order=5, min=0, max=1, modifier=true, summary=true)
    @Description(shortDefinition="draft | active | suspended | completed | entered-in-error | cancelled", formalDefinition="The status of the order." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/request-status")
    protected Enumeration<DiagnosticRequestStatus> status;

    /**
     * Whether the request is a proposal, plan, an original order or a reflex order.
     */
    @Child(name = "stage", type = {CodeableConcept.class}, order=6, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="proposal | plan | original-order | reflex-order", formalDefinition="Whether the request is a proposal, plan, an original order or a reflex order." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/diagnostic-request-stage")
    protected CodeableConcept stage;

    /**
     * A code that identifies a particular diagnostic investigation, or panel of investigations, that have been requested.
     */
    @Child(name = "code", type = {CodeableConcept.class}, order=7, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="What’s being requested/ordered", formalDefinition="A code that identifies a particular diagnostic investigation, or panel of investigations, that have been requested." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/diagnostic-requests")
    protected CodeableConcept code;

    /**
     * On whom or what the investigation is to be performed. This is usually a human patient, but diagnostic tests can also be requested on animals, groups of humans or animals, devices such as dialysis machines, or even locations (typically for environmental scans).
     */
    @Child(name = "subject", type = {Patient.class, Group.class, Location.class, Device.class}, order=8, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Individual the test is ordered for", formalDefinition="On whom or what the investigation is to be performed. This is usually a human patient, but diagnostic tests can also be requested on animals, groups of humans or animals, devices such as dialysis machines, or even locations (typically for environmental scans)." )
    protected Reference subject;

    /**
     * The actual object that is the target of the reference (On whom or what the investigation is to be performed. This is usually a human patient, but diagnostic tests can also be requested on animals, groups of humans or animals, devices such as dialysis machines, or even locations (typically for environmental scans).)
     */
    protected Resource subjectTarget;

    /**
     * An encounter or episode of care that provides additional information about the healthcare context in which this request is made.
     */
    @Child(name = "context", type = {Encounter.class, EpisodeOfCare.class}, order=9, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Encounter or Episode during which request was created", formalDefinition="An encounter or episode of care that provides additional information about the healthcare context in which this request is made." )
    protected Reference context;

    /**
     * The actual object that is the target of the reference (An encounter or episode of care that provides additional information about the healthcare context in which this request is made.)
     */
    protected Resource contextTarget;

    /**
     * The date/time at which the diagnostic testing should occur.
     */
    @Child(name = "occurrence", type = {DateTimeType.class, Period.class, Timing.class}, order=10, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When testing should occur", formalDefinition="The date/time at which the diagnostic testing should occur." )
    protected Type occurrence;

    /**
     * When the request transitioned to being actionable.
     */
    @Child(name = "authored", type = {DateTimeType.class}, order=11, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Date request signed", formalDefinition="When the request transitioned to being actionable." )
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
     * Desired type of performer for doing the diagnostic testing. (.
     */
    @Child(name = "performerType", type = {CodeableConcept.class}, order=13, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Performer role", formalDefinition="Desired type of performer for doing the diagnostic testing. (." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/participant-role")
    protected CodeableConcept performerType;

    /**
     * The desired perfomer for doing the diagnostic testing.
     */
    @Child(name = "performer", type = {Practitioner.class, Organization.class, Patient.class, Device.class, RelatedPerson.class}, order=14, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Requested perfomer", formalDefinition="The desired perfomer for doing the diagnostic testing." )
    protected Reference performer;

    /**
     * The actual object that is the target of the reference (The desired perfomer for doing the diagnostic testing.)
     */
    protected Resource performerTarget;

    /**
     * An explanation or justification for why this diagnostic investigation is being requested.   This is often for billing purposes.  May relate to the resources referred to in supportingInformation.
     */
    @Child(name = "reason", type = {CodeableConcept.class}, order=15, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Explanation/Justification for test", formalDefinition="An explanation or justification for why this diagnostic investigation is being requested.   This is often for billing purposes.  May relate to the resources referred to in supportingInformation." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/condition-code")
    protected List<CodeableConcept> reason;

    /**
     * Additional clinical information about the patient or specimen that may influence test interpretations.  This includes observations explicitly requested by the producer(filler) to provide context or supporting information needed to complete the order.
     */
    @Child(name = "supportingInformation", type = {Reference.class}, order=16, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Additional clinical information", formalDefinition="Additional clinical information about the patient or specimen that may influence test interpretations.  This includes observations explicitly requested by the producer(filler) to provide context or supporting information needed to complete the order." )
    protected List<Reference> supportingInformation;
    /**
     * The actual objects that are the target of the reference (Additional clinical information about the patient or specimen that may influence test interpretations.  This includes observations explicitly requested by the producer(filler) to provide context or supporting information needed to complete the order.)
     */
    protected List<Resource> supportingInformationTarget;


    /**
     * Any other notes and comments made about the service request. (e.g. "patient hates needles").
     */
    @Child(name = "note", type = {Annotation.class}, order=17, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Comments", formalDefinition="Any other notes and comments made about the service request. (e.g. \"patient hates needles\")." )
    protected List<Annotation> note;

    /**
     * Key events in the history of the request.
     */
    @Child(name = "relevantHistory", type = {Provenance.class}, order=18, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Request provenance", formalDefinition="Key events in the history of the request." )
    protected List<Reference> relevantHistory;
    /**
     * The actual objects that are the target of the reference (Key events in the history of the request.)
     */
    protected List<Provenance> relevantHistoryTarget;


    private static final long serialVersionUID = -372674491L;

  /**
   * Constructor
   */
    public DiagnosticRequest() {
      super();
    }

  /**
   * Constructor
   */
    public DiagnosticRequest(CodeableConcept stage, CodeableConcept code, Reference subject) {
      super();
      this.stage = stage;
      this.code = code;
      this.subject = subject;
    }

    /**
     * @return {@link #identifier} (Identifiers assigned to this order instance by the orderer and/or  the receiver and/or order fulfiller.)
     */
    public List<Identifier> getIdentifier() { 
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      return this.identifier;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public DiagnosticRequest setIdentifier(List<Identifier> theIdentifier) { 
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

    public DiagnosticRequest addIdentifier(Identifier t) { //3
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
     * @return {@link #definition} (Protocol or definition followed by this request.)
     */
    public List<Reference> getDefinition() { 
      if (this.definition == null)
        this.definition = new ArrayList<Reference>();
      return this.definition;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public DiagnosticRequest setDefinition(List<Reference> theDefinition) { 
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

    public DiagnosticRequest addDefinition(Reference t) { //3
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
    public DiagnosticRequest setBasedOn(List<Reference> theBasedOn) { 
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

    public DiagnosticRequest addBasedOn(Reference t) { //3
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
    public DiagnosticRequest setReplaces(List<Reference> theReplaces) { 
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

    public DiagnosticRequest addReplaces(Reference t) { //3
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
          throw new Error("Attempt to auto-create DiagnosticRequest.requisition");
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
    public DiagnosticRequest setRequisition(Identifier value) { 
      this.requisition = value;
      return this;
    }

    /**
     * @return {@link #status} (The status of the order.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<DiagnosticRequestStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DiagnosticRequest.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<DiagnosticRequestStatus>(new DiagnosticRequestStatusEnumFactory()); // bb
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
    public DiagnosticRequest setStatusElement(Enumeration<DiagnosticRequestStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The status of the order.
     */
    public DiagnosticRequestStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The status of the order.
     */
    public DiagnosticRequest setStatus(DiagnosticRequestStatus value) { 
      if (value == null)
        this.status = null;
      else {
        if (this.status == null)
          this.status = new Enumeration<DiagnosticRequestStatus>(new DiagnosticRequestStatusEnumFactory());
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
          throw new Error("Attempt to auto-create DiagnosticRequest.stage");
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
    public DiagnosticRequest setStage(CodeableConcept value) { 
      this.stage = value;
      return this;
    }

    /**
     * @return {@link #code} (A code that identifies a particular diagnostic investigation, or panel of investigations, that have been requested.)
     */
    public CodeableConcept getCode() { 
      if (this.code == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DiagnosticRequest.code");
        else if (Configuration.doAutoCreate())
          this.code = new CodeableConcept(); // cc
      return this.code;
    }

    public boolean hasCode() { 
      return this.code != null && !this.code.isEmpty();
    }

    /**
     * @param value {@link #code} (A code that identifies a particular diagnostic investigation, or panel of investigations, that have been requested.)
     */
    public DiagnosticRequest setCode(CodeableConcept value) { 
      this.code = value;
      return this;
    }

    /**
     * @return {@link #subject} (On whom or what the investigation is to be performed. This is usually a human patient, but diagnostic tests can also be requested on animals, groups of humans or animals, devices such as dialysis machines, or even locations (typically for environmental scans).)
     */
    public Reference getSubject() { 
      if (this.subject == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DiagnosticRequest.subject");
        else if (Configuration.doAutoCreate())
          this.subject = new Reference(); // cc
      return this.subject;
    }

    public boolean hasSubject() { 
      return this.subject != null && !this.subject.isEmpty();
    }

    /**
     * @param value {@link #subject} (On whom or what the investigation is to be performed. This is usually a human patient, but diagnostic tests can also be requested on animals, groups of humans or animals, devices such as dialysis machines, or even locations (typically for environmental scans).)
     */
    public DiagnosticRequest setSubject(Reference value) { 
      this.subject = value;
      return this;
    }

    /**
     * @return {@link #subject} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (On whom or what the investigation is to be performed. This is usually a human patient, but diagnostic tests can also be requested on animals, groups of humans or animals, devices such as dialysis machines, or even locations (typically for environmental scans).)
     */
    public Resource getSubjectTarget() { 
      return this.subjectTarget;
    }

    /**
     * @param value {@link #subject} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (On whom or what the investigation is to be performed. This is usually a human patient, but diagnostic tests can also be requested on animals, groups of humans or animals, devices such as dialysis machines, or even locations (typically for environmental scans).)
     */
    public DiagnosticRequest setSubjectTarget(Resource value) { 
      this.subjectTarget = value;
      return this;
    }

    /**
     * @return {@link #context} (An encounter or episode of care that provides additional information about the healthcare context in which this request is made.)
     */
    public Reference getContext() { 
      if (this.context == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DiagnosticRequest.context");
        else if (Configuration.doAutoCreate())
          this.context = new Reference(); // cc
      return this.context;
    }

    public boolean hasContext() { 
      return this.context != null && !this.context.isEmpty();
    }

    /**
     * @param value {@link #context} (An encounter or episode of care that provides additional information about the healthcare context in which this request is made.)
     */
    public DiagnosticRequest setContext(Reference value) { 
      this.context = value;
      return this;
    }

    /**
     * @return {@link #context} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (An encounter or episode of care that provides additional information about the healthcare context in which this request is made.)
     */
    public Resource getContextTarget() { 
      return this.contextTarget;
    }

    /**
     * @param value {@link #context} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (An encounter or episode of care that provides additional information about the healthcare context in which this request is made.)
     */
    public DiagnosticRequest setContextTarget(Resource value) { 
      this.contextTarget = value;
      return this;
    }

    /**
     * @return {@link #occurrence} (The date/time at which the diagnostic testing should occur.)
     */
    public Type getOccurrence() { 
      return this.occurrence;
    }

    /**
     * @return {@link #occurrence} (The date/time at which the diagnostic testing should occur.)
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
     * @return {@link #occurrence} (The date/time at which the diagnostic testing should occur.)
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
     * @return {@link #occurrence} (The date/time at which the diagnostic testing should occur.)
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
     * @param value {@link #occurrence} (The date/time at which the diagnostic testing should occur.)
     */
    public DiagnosticRequest setOccurrence(Type value) { 
      this.occurrence = value;
      return this;
    }

    /**
     * @return {@link #authored} (When the request transitioned to being actionable.). This is the underlying object with id, value and extensions. The accessor "getAuthored" gives direct access to the value
     */
    public DateTimeType getAuthoredElement() { 
      if (this.authored == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DiagnosticRequest.authored");
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
    public DiagnosticRequest setAuthoredElement(DateTimeType value) { 
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
    public DiagnosticRequest setAuthored(Date value) { 
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
          throw new Error("Attempt to auto-create DiagnosticRequest.requester");
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
    public DiagnosticRequest setRequester(Reference value) { 
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
    public DiagnosticRequest setRequesterTarget(Resource value) { 
      this.requesterTarget = value;
      return this;
    }

    /**
     * @return {@link #performerType} (Desired type of performer for doing the diagnostic testing. (.)
     */
    public CodeableConcept getPerformerType() { 
      if (this.performerType == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DiagnosticRequest.performerType");
        else if (Configuration.doAutoCreate())
          this.performerType = new CodeableConcept(); // cc
      return this.performerType;
    }

    public boolean hasPerformerType() { 
      return this.performerType != null && !this.performerType.isEmpty();
    }

    /**
     * @param value {@link #performerType} (Desired type of performer for doing the diagnostic testing. (.)
     */
    public DiagnosticRequest setPerformerType(CodeableConcept value) { 
      this.performerType = value;
      return this;
    }

    /**
     * @return {@link #performer} (The desired perfomer for doing the diagnostic testing.)
     */
    public Reference getPerformer() { 
      if (this.performer == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DiagnosticRequest.performer");
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
    public DiagnosticRequest setPerformer(Reference value) { 
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
    public DiagnosticRequest setPerformerTarget(Resource value) { 
      this.performerTarget = value;
      return this;
    }

    /**
     * @return {@link #reason} (An explanation or justification for why this diagnostic investigation is being requested.   This is often for billing purposes.  May relate to the resources referred to in supportingInformation.)
     */
    public List<CodeableConcept> getReason() { 
      if (this.reason == null)
        this.reason = new ArrayList<CodeableConcept>();
      return this.reason;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public DiagnosticRequest setReason(List<CodeableConcept> theReason) { 
      this.reason = theReason;
      return this;
    }

    public boolean hasReason() { 
      if (this.reason == null)
        return false;
      for (CodeableConcept item : this.reason)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addReason() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.reason == null)
        this.reason = new ArrayList<CodeableConcept>();
      this.reason.add(t);
      return t;
    }

    public DiagnosticRequest addReason(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.reason == null)
        this.reason = new ArrayList<CodeableConcept>();
      this.reason.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #reason}, creating it if it does not already exist
     */
    public CodeableConcept getReasonFirstRep() { 
      if (getReason().isEmpty()) {
        addReason();
      }
      return getReason().get(0);
    }

    /**
     * @return {@link #supportingInformation} (Additional clinical information about the patient or specimen that may influence test interpretations.  This includes observations explicitly requested by the producer(filler) to provide context or supporting information needed to complete the order.)
     */
    public List<Reference> getSupportingInformation() { 
      if (this.supportingInformation == null)
        this.supportingInformation = new ArrayList<Reference>();
      return this.supportingInformation;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public DiagnosticRequest setSupportingInformation(List<Reference> theSupportingInformation) { 
      this.supportingInformation = theSupportingInformation;
      return this;
    }

    public boolean hasSupportingInformation() { 
      if (this.supportingInformation == null)
        return false;
      for (Reference item : this.supportingInformation)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addSupportingInformation() { //3
      Reference t = new Reference();
      if (this.supportingInformation == null)
        this.supportingInformation = new ArrayList<Reference>();
      this.supportingInformation.add(t);
      return t;
    }

    public DiagnosticRequest addSupportingInformation(Reference t) { //3
      if (t == null)
        return this;
      if (this.supportingInformation == null)
        this.supportingInformation = new ArrayList<Reference>();
      this.supportingInformation.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #supportingInformation}, creating it if it does not already exist
     */
    public Reference getSupportingInformationFirstRep() { 
      if (getSupportingInformation().isEmpty()) {
        addSupportingInformation();
      }
      return getSupportingInformation().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Resource> getSupportingInformationTarget() { 
      if (this.supportingInformationTarget == null)
        this.supportingInformationTarget = new ArrayList<Resource>();
      return this.supportingInformationTarget;
    }

    /**
     * @return {@link #note} (Any other notes and comments made about the service request. (e.g. "patient hates needles").)
     */
    public List<Annotation> getNote() { 
      if (this.note == null)
        this.note = new ArrayList<Annotation>();
      return this.note;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public DiagnosticRequest setNote(List<Annotation> theNote) { 
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

    public DiagnosticRequest addNote(Annotation t) { //3
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
    public DiagnosticRequest setRelevantHistory(List<Reference> theRelevantHistory) { 
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

    public DiagnosticRequest addRelevantHistory(Reference t) { //3
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
        childrenList.add(new Property("identifier", "Identifier", "Identifiers assigned to this order instance by the orderer and/or  the receiver and/or order fulfiller.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("definition", "Reference(Any)", "Protocol or definition followed by this request.", 0, java.lang.Integer.MAX_VALUE, definition));
        childrenList.add(new Property("basedOn", "Reference(Any)", "Plan/proposal/order fulfilled by this request.", 0, java.lang.Integer.MAX_VALUE, basedOn));
        childrenList.add(new Property("replaces", "Reference(Any)", "The request takes the place of the referenced completed or terminated request(s).", 0, java.lang.Integer.MAX_VALUE, replaces));
        childrenList.add(new Property("requisition", "Identifier", "Composite request this is part of.", 0, java.lang.Integer.MAX_VALUE, requisition));
        childrenList.add(new Property("status", "code", "The status of the order.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("stage", "CodeableConcept", "Whether the request is a proposal, plan, an original order or a reflex order.", 0, java.lang.Integer.MAX_VALUE, stage));
        childrenList.add(new Property("code", "CodeableConcept", "A code that identifies a particular diagnostic investigation, or panel of investigations, that have been requested.", 0, java.lang.Integer.MAX_VALUE, code));
        childrenList.add(new Property("subject", "Reference(Patient|Group|Location|Device)", "On whom or what the investigation is to be performed. This is usually a human patient, but diagnostic tests can also be requested on animals, groups of humans or animals, devices such as dialysis machines, or even locations (typically for environmental scans).", 0, java.lang.Integer.MAX_VALUE, subject));
        childrenList.add(new Property("context", "Reference(Encounter|EpisodeOfCare)", "An encounter or episode of care that provides additional information about the healthcare context in which this request is made.", 0, java.lang.Integer.MAX_VALUE, context));
        childrenList.add(new Property("occurrence[x]", "dateTime|Period|Timing", "The date/time at which the diagnostic testing should occur.", 0, java.lang.Integer.MAX_VALUE, occurrence));
        childrenList.add(new Property("authored", "dateTime", "When the request transitioned to being actionable.", 0, java.lang.Integer.MAX_VALUE, authored));
        childrenList.add(new Property("requester", "Reference(Device|Practitioner|Organization)", "Who/what is requesting diagnostics.  The practitioner that holds legal responsibility for ordering the investigation.", 0, java.lang.Integer.MAX_VALUE, requester));
        childrenList.add(new Property("performerType", "CodeableConcept", "Desired type of performer for doing the diagnostic testing. (.", 0, java.lang.Integer.MAX_VALUE, performerType));
        childrenList.add(new Property("performer", "Reference(Practitioner|Organization|Patient|Device|RelatedPerson)", "The desired perfomer for doing the diagnostic testing.", 0, java.lang.Integer.MAX_VALUE, performer));
        childrenList.add(new Property("reason", "CodeableConcept", "An explanation or justification for why this diagnostic investigation is being requested.   This is often for billing purposes.  May relate to the resources referred to in supportingInformation.", 0, java.lang.Integer.MAX_VALUE, reason));
        childrenList.add(new Property("supportingInformation", "Reference(Any)", "Additional clinical information about the patient or specimen that may influence test interpretations.  This includes observations explicitly requested by the producer(filler) to provide context or supporting information needed to complete the order.", 0, java.lang.Integer.MAX_VALUE, supportingInformation));
        childrenList.add(new Property("note", "Annotation", "Any other notes and comments made about the service request. (e.g. \"patient hates needles\").", 0, java.lang.Integer.MAX_VALUE, note));
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
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<DiagnosticRequestStatus>
        case 109757182: /*stage*/ return this.stage == null ? new Base[0] : new Base[] {this.stage}; // CodeableConcept
        case 3059181: /*code*/ return this.code == null ? new Base[0] : new Base[] {this.code}; // CodeableConcept
        case -1867885268: /*subject*/ return this.subject == null ? new Base[0] : new Base[] {this.subject}; // Reference
        case 951530927: /*context*/ return this.context == null ? new Base[0] : new Base[] {this.context}; // Reference
        case 1687874001: /*occurrence*/ return this.occurrence == null ? new Base[0] : new Base[] {this.occurrence}; // Type
        case 1433073514: /*authored*/ return this.authored == null ? new Base[0] : new Base[] {this.authored}; // DateTimeType
        case 693933948: /*requester*/ return this.requester == null ? new Base[0] : new Base[] {this.requester}; // Reference
        case -901444568: /*performerType*/ return this.performerType == null ? new Base[0] : new Base[] {this.performerType}; // CodeableConcept
        case 481140686: /*performer*/ return this.performer == null ? new Base[0] : new Base[] {this.performer}; // Reference
        case -934964668: /*reason*/ return this.reason == null ? new Base[0] : this.reason.toArray(new Base[this.reason.size()]); // CodeableConcept
        case -1248768647: /*supportingInformation*/ return this.supportingInformation == null ? new Base[0] : this.supportingInformation.toArray(new Base[this.supportingInformation.size()]); // Reference
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
          this.status = new DiagnosticRequestStatusEnumFactory().fromType(value); // Enumeration<DiagnosticRequestStatus>
          break;
        case 109757182: // stage
          this.stage = castToCodeableConcept(value); // CodeableConcept
          break;
        case 3059181: // code
          this.code = castToCodeableConcept(value); // CodeableConcept
          break;
        case -1867885268: // subject
          this.subject = castToReference(value); // Reference
          break;
        case 951530927: // context
          this.context = castToReference(value); // Reference
          break;
        case 1687874001: // occurrence
          this.occurrence = (Type) value; // Type
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
        case -934964668: // reason
          this.getReason().add(castToCodeableConcept(value)); // CodeableConcept
          break;
        case -1248768647: // supportingInformation
          this.getSupportingInformation().add(castToReference(value)); // Reference
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
          this.status = new DiagnosticRequestStatusEnumFactory().fromType(value); // Enumeration<DiagnosticRequestStatus>
        else if (name.equals("stage"))
          this.stage = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("code"))
          this.code = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("subject"))
          this.subject = castToReference(value); // Reference
        else if (name.equals("context"))
          this.context = castToReference(value); // Reference
        else if (name.equals("occurrence[x]"))
          this.occurrence = (Type) value; // Type
        else if (name.equals("authored"))
          this.authored = castToDateTime(value); // DateTimeType
        else if (name.equals("requester"))
          this.requester = castToReference(value); // Reference
        else if (name.equals("performerType"))
          this.performerType = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("performer"))
          this.performer = castToReference(value); // Reference
        else if (name.equals("reason"))
          this.getReason().add(castToCodeableConcept(value));
        else if (name.equals("supportingInformation"))
          this.getSupportingInformation().add(castToReference(value));
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
        case -892481550: throw new FHIRException("Cannot make property status as it is not a complex type"); // Enumeration<DiagnosticRequestStatus>
        case 109757182:  return getStage(); // CodeableConcept
        case 3059181:  return getCode(); // CodeableConcept
        case -1867885268:  return getSubject(); // Reference
        case 951530927:  return getContext(); // Reference
        case -2022646513:  return getOccurrence(); // Type
        case 1433073514: throw new FHIRException("Cannot make property authored as it is not a complex type"); // DateTimeType
        case 693933948:  return getRequester(); // Reference
        case -901444568:  return getPerformerType(); // CodeableConcept
        case 481140686:  return getPerformer(); // Reference
        case -934964668:  return addReason(); // CodeableConcept
        case -1248768647:  return addSupportingInformation(); // Reference
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
          throw new FHIRException("Cannot call addChild on a primitive type DiagnosticRequest.status");
        }
        else if (name.equals("stage")) {
          this.stage = new CodeableConcept();
          return this.stage;
        }
        else if (name.equals("code")) {
          this.code = new CodeableConcept();
          return this.code;
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
          throw new FHIRException("Cannot call addChild on a primitive type DiagnosticRequest.authored");
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
        else if (name.equals("reason")) {
          return addReason();
        }
        else if (name.equals("supportingInformation")) {
          return addSupportingInformation();
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
    return "DiagnosticRequest";

  }

      public DiagnosticRequest copy() {
        DiagnosticRequest dst = new DiagnosticRequest();
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
        dst.code = code == null ? null : code.copy();
        dst.subject = subject == null ? null : subject.copy();
        dst.context = context == null ? null : context.copy();
        dst.occurrence = occurrence == null ? null : occurrence.copy();
        dst.authored = authored == null ? null : authored.copy();
        dst.requester = requester == null ? null : requester.copy();
        dst.performerType = performerType == null ? null : performerType.copy();
        dst.performer = performer == null ? null : performer.copy();
        if (reason != null) {
          dst.reason = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : reason)
            dst.reason.add(i.copy());
        };
        if (supportingInformation != null) {
          dst.supportingInformation = new ArrayList<Reference>();
          for (Reference i : supportingInformation)
            dst.supportingInformation.add(i.copy());
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

      protected DiagnosticRequest typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof DiagnosticRequest))
          return false;
        DiagnosticRequest o = (DiagnosticRequest) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(definition, o.definition, true)
           && compareDeep(basedOn, o.basedOn, true) && compareDeep(replaces, o.replaces, true) && compareDeep(requisition, o.requisition, true)
           && compareDeep(status, o.status, true) && compareDeep(stage, o.stage, true) && compareDeep(code, o.code, true)
           && compareDeep(subject, o.subject, true) && compareDeep(context, o.context, true) && compareDeep(occurrence, o.occurrence, true)
           && compareDeep(authored, o.authored, true) && compareDeep(requester, o.requester, true) && compareDeep(performerType, o.performerType, true)
           && compareDeep(performer, o.performer, true) && compareDeep(reason, o.reason, true) && compareDeep(supportingInformation, o.supportingInformation, true)
           && compareDeep(note, o.note, true) && compareDeep(relevantHistory, o.relevantHistory, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof DiagnosticRequest))
          return false;
        DiagnosticRequest o = (DiagnosticRequest) other;
        return compareValues(status, o.status, true) && compareValues(authored, o.authored, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, definition, basedOn
          , replaces, requisition, status, stage, code, subject, context, occurrence, authored
          , requester, performerType, performer, reason, supportingInformation, note, relevantHistory
          );
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.DiagnosticRequest;
   }

 /**
   * Search parameter: <b>requester</b>
   * <p>
   * Description: <b>Who/what is requesting service </b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DiagnosticRequest.requester</b><br>
   * </p>
   */
  @SearchParamDefinition(name="requester", path="DiagnosticRequest.requester", description="Who/what is requesting service ", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Device"), @ca.uhn.fhir.model.api.annotation.Compartment(name="Practitioner") }, target={Device.class, Organization.class, Practitioner.class } )
  public static final String SP_REQUESTER = "requester";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>requester</b>
   * <p>
   * Description: <b>Who/what is requesting service </b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DiagnosticRequest.requester</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam REQUESTER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_REQUESTER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>DiagnosticRequest:requester</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_REQUESTER = new ca.uhn.fhir.model.api.Include("DiagnosticRequest:requester").toLocked();

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>Business identifier for request/order</b><br>
   * Type: <b>token</b><br>
   * Path: <b>DiagnosticRequest.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="DiagnosticRequest.identifier", description="Business identifier for request/order", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>Business identifier for request/order</b><br>
   * Type: <b>token</b><br>
   * Path: <b>DiagnosticRequest.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>code</b>
   * <p>
   * Description: <b>What’s being requested/ordered</b><br>
   * Type: <b>token</b><br>
   * Path: <b>DiagnosticRequest.code</b><br>
   * </p>
   */
  @SearchParamDefinition(name="code", path="DiagnosticRequest.code", description="What’s being requested/ordered", type="token" )
  public static final String SP_CODE = "code";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>code</b>
   * <p>
   * Description: <b>What’s being requested/ordered</b><br>
   * Type: <b>token</b><br>
   * Path: <b>DiagnosticRequest.code</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CODE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CODE);

 /**
   * Search parameter: <b>event-date</b>
   * <p>
   * Description: <b>When service should occur</b><br>
   * Type: <b>date</b><br>
   * Path: <b>DiagnosticRequest.occurrenceDateTime, DiagnosticRequest.occurrencePeriod</b><br>
   * </p>
   */
  @SearchParamDefinition(name="event-date", path="DiagnosticRequest.occurrence.as(DateTime) | DiagnosticRequest.occurrence.as(Period)", description="When service should occur", type="date" )
  public static final String SP_EVENT_DATE = "event-date";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>event-date</b>
   * <p>
   * Description: <b>When service should occur</b><br>
   * Type: <b>date</b><br>
   * Path: <b>DiagnosticRequest.occurrenceDateTime, DiagnosticRequest.occurrencePeriod</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam EVENT_DATE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_EVENT_DATE);

 /**
   * Search parameter: <b>requisition</b>
   * <p>
   * Description: <b>Composite request this is part of</b><br>
   * Type: <b>token</b><br>
   * Path: <b>DiagnosticRequest.requisition</b><br>
   * </p>
   */
  @SearchParamDefinition(name="requisition", path="DiagnosticRequest.requisition", description="Composite request this is part of", type="token" )
  public static final String SP_REQUISITION = "requisition";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>requisition</b>
   * <p>
   * Description: <b>Composite request this is part of</b><br>
   * Type: <b>token</b><br>
   * Path: <b>DiagnosticRequest.requisition</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam REQUISITION = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_REQUISITION);

 /**
   * Search parameter: <b>replaces</b>
   * <p>
   * Description: <b>Request takes the place of referenced completed or terminated requests</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DiagnosticRequest.replaces</b><br>
   * </p>
   */
  @SearchParamDefinition(name="replaces", path="DiagnosticRequest.replaces", description="Request takes the place of referenced completed or terminated requests", type="reference" )
  public static final String SP_REPLACES = "replaces";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>replaces</b>
   * <p>
   * Description: <b>Request takes the place of referenced completed or terminated requests</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DiagnosticRequest.replaces</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam REPLACES = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_REPLACES);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>DiagnosticRequest:replaces</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_REPLACES = new ca.uhn.fhir.model.api.Include("DiagnosticRequest:replaces").toLocked();

 /**
   * Search parameter: <b>subject</b>
   * <p>
   * Description: <b>Individual the service is ordered for</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DiagnosticRequest.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="subject", path="DiagnosticRequest.subject", description="Individual the service is ordered for", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Device"), @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient") }, target={Device.class, Group.class, Location.class, Patient.class } )
  public static final String SP_SUBJECT = "subject";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>subject</b>
   * <p>
   * Description: <b>Individual the service is ordered for</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DiagnosticRequest.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SUBJECT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SUBJECT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>DiagnosticRequest:subject</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SUBJECT = new ca.uhn.fhir.model.api.Include("DiagnosticRequest:subject").toLocked();

 /**
   * Search parameter: <b>author-date</b>
   * <p>
   * Description: <b>When the request transitioned to being actionable</b><br>
   * Type: <b>date</b><br>
   * Path: <b>DiagnosticRequest.authored</b><br>
   * </p>
   */
  @SearchParamDefinition(name="author-date", path="DiagnosticRequest.authored", description="When the request transitioned to being actionable", type="date" )
  public static final String SP_AUTHOR_DATE = "author-date";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>author-date</b>
   * <p>
   * Description: <b>When the request transitioned to being actionable</b><br>
   * Type: <b>date</b><br>
   * Path: <b>DiagnosticRequest.authored</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam AUTHOR_DATE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_AUTHOR_DATE);

 /**
   * Search parameter: <b>encounter</b>
   * <p>
   * Description: <b>Encounter or Episode during which request was created</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DiagnosticRequest.context</b><br>
   * </p>
   */
  @SearchParamDefinition(name="encounter", path="DiagnosticRequest.context", description="Encounter or Episode during which request was created", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Encounter") }, target={Encounter.class, EpisodeOfCare.class } )
  public static final String SP_ENCOUNTER = "encounter";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>encounter</b>
   * <p>
   * Description: <b>Encounter or Episode during which request was created</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DiagnosticRequest.context</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam ENCOUNTER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_ENCOUNTER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>DiagnosticRequest:encounter</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_ENCOUNTER = new ca.uhn.fhir.model.api.Include("DiagnosticRequest:encounter").toLocked();

 /**
   * Search parameter: <b>based-on</b>
   * <p>
   * Description: <b>Plan/proposal/order fulfilled by this request</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DiagnosticRequest.basedOn</b><br>
   * </p>
   */
  @SearchParamDefinition(name="based-on", path="DiagnosticRequest.basedOn", description="Plan/proposal/order fulfilled by this request", type="reference" )
  public static final String SP_BASED_ON = "based-on";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>based-on</b>
   * <p>
   * Description: <b>Plan/proposal/order fulfilled by this request</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DiagnosticRequest.basedOn</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam BASED_ON = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_BASED_ON);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>DiagnosticRequest:based-on</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_BASED_ON = new ca.uhn.fhir.model.api.Include("DiagnosticRequest:based-on").toLocked();

 /**
   * Search parameter: <b>stage</b>
   * <p>
   * Description: <b>proposal | plan | original-order |reflex-order</b><br>
   * Type: <b>token</b><br>
   * Path: <b>DiagnosticRequest.stage</b><br>
   * </p>
   */
  @SearchParamDefinition(name="stage", path="DiagnosticRequest.stage", description="proposal | plan | original-order |reflex-order", type="token" )
  public static final String SP_STAGE = "stage";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>stage</b>
   * <p>
   * Description: <b>proposal | plan | original-order |reflex-order</b><br>
   * Type: <b>token</b><br>
   * Path: <b>DiagnosticRequest.stage</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STAGE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STAGE);

 /**
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>Individual the service is ordered for</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DiagnosticRequest.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="DiagnosticRequest.subject", description="Individual the service is ordered for", type="reference", target={Patient.class } )
  public static final String SP_PATIENT = "patient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patient</b>
   * <p>
   * Description: <b>Individual the service is ordered for</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DiagnosticRequest.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PATIENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PATIENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>DiagnosticRequest:patient</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PATIENT = new ca.uhn.fhir.model.api.Include("DiagnosticRequest:patient").toLocked();

 /**
   * Search parameter: <b>filler</b>
   * <p>
   * Description: <b>Desired performer for service</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DiagnosticRequest.performer</b><br>
   * </p>
   */
  @SearchParamDefinition(name="filler", path="DiagnosticRequest.performer", description="Desired performer for service", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Device"), @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient"), @ca.uhn.fhir.model.api.annotation.Compartment(name="Practitioner") }, target={Device.class, Organization.class, Patient.class, Practitioner.class, RelatedPerson.class } )
  public static final String SP_FILLER = "filler";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>filler</b>
   * <p>
   * Description: <b>Desired performer for service</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DiagnosticRequest.performer</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam FILLER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_FILLER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>DiagnosticRequest:filler</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_FILLER = new ca.uhn.fhir.model.api.Include("DiagnosticRequest:filler").toLocked();

 /**
   * Search parameter: <b>definition</b>
   * <p>
   * Description: <b>Protocol or definition followed by this request</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DiagnosticRequest.definition</b><br>
   * </p>
   */
  @SearchParamDefinition(name="definition", path="DiagnosticRequest.definition", description="Protocol or definition followed by this request", type="reference" )
  public static final String SP_DEFINITION = "definition";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>definition</b>
   * <p>
   * Description: <b>Protocol or definition followed by this request</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DiagnosticRequest.definition</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam DEFINITION = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_DEFINITION);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>DiagnosticRequest:definition</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_DEFINITION = new ca.uhn.fhir.model.api.Include("DiagnosticRequest:definition").toLocked();

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>entered-in-error | draft | active |suspended | completed </b><br>
   * Type: <b>token</b><br>
   * Path: <b>DiagnosticRequest.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="DiagnosticRequest.status", description="entered-in-error | draft | active |suspended | completed ", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>entered-in-error | draft | active |suspended | completed </b><br>
   * Type: <b>token</b><br>
   * Path: <b>DiagnosticRequest.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);


}

