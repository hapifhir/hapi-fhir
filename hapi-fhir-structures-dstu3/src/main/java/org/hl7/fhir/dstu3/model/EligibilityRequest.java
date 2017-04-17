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

// Generated on Mon, Apr 17, 2017 17:38-0400 for FHIR v3.0.1

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
 * The EligibilityRequest provides patient and insurance coverage information to an insurer for them to respond, in the form of an EligibilityResponse, with information regarding whether the stated coverage is valid and in-force and optionally to provide the insurance details of the policy.
 */
@ResourceDef(name="EligibilityRequest", profile="http://hl7.org/fhir/Profile/EligibilityRequest")
public class EligibilityRequest extends DomainResource {

    public enum EligibilityRequestStatus {
        /**
         * The instance is currently in-force.
         */
        ACTIVE, 
        /**
         * The instance is withdrawn, rescinded or reversed.
         */
        CANCELLED, 
        /**
         * A new instance the contents of which is not complete.
         */
        DRAFT, 
        /**
         * The instance was entered in error.
         */
        ENTEREDINERROR, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static EligibilityRequestStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("active".equals(codeString))
          return ACTIVE;
        if ("cancelled".equals(codeString))
          return CANCELLED;
        if ("draft".equals(codeString))
          return DRAFT;
        if ("entered-in-error".equals(codeString))
          return ENTEREDINERROR;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown EligibilityRequestStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ACTIVE: return "active";
            case CANCELLED: return "cancelled";
            case DRAFT: return "draft";
            case ENTEREDINERROR: return "entered-in-error";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case ACTIVE: return "http://hl7.org/fhir/fm-status";
            case CANCELLED: return "http://hl7.org/fhir/fm-status";
            case DRAFT: return "http://hl7.org/fhir/fm-status";
            case ENTEREDINERROR: return "http://hl7.org/fhir/fm-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case ACTIVE: return "The instance is currently in-force.";
            case CANCELLED: return "The instance is withdrawn, rescinded or reversed.";
            case DRAFT: return "A new instance the contents of which is not complete.";
            case ENTEREDINERROR: return "The instance was entered in error.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ACTIVE: return "Active";
            case CANCELLED: return "Cancelled";
            case DRAFT: return "Draft";
            case ENTEREDINERROR: return "Entered in Error";
            default: return "?";
          }
        }
    }

  public static class EligibilityRequestStatusEnumFactory implements EnumFactory<EligibilityRequestStatus> {
    public EligibilityRequestStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("active".equals(codeString))
          return EligibilityRequestStatus.ACTIVE;
        if ("cancelled".equals(codeString))
          return EligibilityRequestStatus.CANCELLED;
        if ("draft".equals(codeString))
          return EligibilityRequestStatus.DRAFT;
        if ("entered-in-error".equals(codeString))
          return EligibilityRequestStatus.ENTEREDINERROR;
        throw new IllegalArgumentException("Unknown EligibilityRequestStatus code '"+codeString+"'");
        }
        public Enumeration<EligibilityRequestStatus> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<EligibilityRequestStatus>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("active".equals(codeString))
          return new Enumeration<EligibilityRequestStatus>(this, EligibilityRequestStatus.ACTIVE);
        if ("cancelled".equals(codeString))
          return new Enumeration<EligibilityRequestStatus>(this, EligibilityRequestStatus.CANCELLED);
        if ("draft".equals(codeString))
          return new Enumeration<EligibilityRequestStatus>(this, EligibilityRequestStatus.DRAFT);
        if ("entered-in-error".equals(codeString))
          return new Enumeration<EligibilityRequestStatus>(this, EligibilityRequestStatus.ENTEREDINERROR);
        throw new FHIRException("Unknown EligibilityRequestStatus code '"+codeString+"'");
        }
    public String toCode(EligibilityRequestStatus code) {
      if (code == EligibilityRequestStatus.ACTIVE)
        return "active";
      if (code == EligibilityRequestStatus.CANCELLED)
        return "cancelled";
      if (code == EligibilityRequestStatus.DRAFT)
        return "draft";
      if (code == EligibilityRequestStatus.ENTEREDINERROR)
        return "entered-in-error";
      return "?";
      }
    public String toSystem(EligibilityRequestStatus code) {
      return code.getSystem();
      }
    }

    /**
     * The Response business identifier.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Business Identifier", formalDefinition="The Response business identifier." )
    protected List<Identifier> identifier;

    /**
     * The status of the resource instance.
     */
    @Child(name = "status", type = {CodeType.class}, order=1, min=0, max=1, modifier=true, summary=true)
    @Description(shortDefinition="active | cancelled | draft | entered-in-error", formalDefinition="The status of the resource instance." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/fm-status")
    protected Enumeration<EligibilityRequestStatus> status;

    /**
     * Immediate (STAT), best effort (NORMAL), deferred (DEFER).
     */
    @Child(name = "priority", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Desired processing priority", formalDefinition="Immediate (STAT), best effort (NORMAL), deferred (DEFER)." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/process-priority")
    protected CodeableConcept priority;

    /**
     * Patient Resource.
     */
    @Child(name = "patient", type = {Patient.class}, order=3, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="The subject of the Products and Services", formalDefinition="Patient Resource." )
    protected Reference patient;

    /**
     * The actual object that is the target of the reference (Patient Resource.)
     */
    protected Patient patientTarget;

    /**
     * The date or dates when the enclosed suite of services were performed or completed.
     */
    @Child(name = "serviced", type = {DateType.class, Period.class}, order=4, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Estimated date or dates of Service", formalDefinition="The date or dates when the enclosed suite of services were performed or completed." )
    protected Type serviced;

    /**
     * The date when this resource was created.
     */
    @Child(name = "created", type = {DateTimeType.class}, order=5, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Creation date", formalDefinition="The date when this resource was created." )
    protected DateTimeType created;

    /**
     * Person who created the invoice/claim/pre-determination or pre-authorization.
     */
    @Child(name = "enterer", type = {Practitioner.class}, order=6, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Author", formalDefinition="Person who created the invoice/claim/pre-determination or pre-authorization." )
    protected Reference enterer;

    /**
     * The actual object that is the target of the reference (Person who created the invoice/claim/pre-determination or pre-authorization.)
     */
    protected Practitioner entererTarget;

    /**
     * The practitioner who is responsible for the services rendered to the patient.
     */
    @Child(name = "provider", type = {Practitioner.class}, order=7, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Responsible practitioner", formalDefinition="The practitioner who is responsible for the services rendered to the patient." )
    protected Reference provider;

    /**
     * The actual object that is the target of the reference (The practitioner who is responsible for the services rendered to the patient.)
     */
    protected Practitioner providerTarget;

    /**
     * The organization which is responsible for the services rendered to the patient.
     */
    @Child(name = "organization", type = {Organization.class}, order=8, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Responsible organization", formalDefinition="The organization which is responsible for the services rendered to the patient." )
    protected Reference organization;

    /**
     * The actual object that is the target of the reference (The organization which is responsible for the services rendered to the patient.)
     */
    protected Organization organizationTarget;

    /**
     * The Insurer who is target  of the request.
     */
    @Child(name = "insurer", type = {Organization.class}, order=9, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Target", formalDefinition="The Insurer who is target  of the request." )
    protected Reference insurer;

    /**
     * The actual object that is the target of the reference (The Insurer who is target  of the request.)
     */
    protected Organization insurerTarget;

    /**
     * Facility where the services were provided.
     */
    @Child(name = "facility", type = {Location.class}, order=10, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Servicing Facility", formalDefinition="Facility where the services were provided." )
    protected Reference facility;

    /**
     * The actual object that is the target of the reference (Facility where the services were provided.)
     */
    protected Location facilityTarget;

    /**
     * Financial instrument by which payment information for health care.
     */
    @Child(name = "coverage", type = {Coverage.class}, order=11, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Insurance or medical plan", formalDefinition="Financial instrument by which payment information for health care." )
    protected Reference coverage;

    /**
     * The actual object that is the target of the reference (Financial instrument by which payment information for health care.)
     */
    protected Coverage coverageTarget;

    /**
     * The contract number of a business agreement which describes the terms and conditions.
     */
    @Child(name = "businessArrangement", type = {StringType.class}, order=12, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Business agreement", formalDefinition="The contract number of a business agreement which describes the terms and conditions." )
    protected StringType businessArrangement;

    /**
     * Dental, Vision, Medical, Pharmacy, Rehab etc.
     */
    @Child(name = "benefitCategory", type = {CodeableConcept.class}, order=13, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Type of services covered", formalDefinition="Dental, Vision, Medical, Pharmacy, Rehab etc." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/benefit-category")
    protected CodeableConcept benefitCategory;

    /**
     * Dental: basic, major, ortho; Vision exam, glasses, contacts; etc.
     */
    @Child(name = "benefitSubCategory", type = {CodeableConcept.class}, order=14, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Detailed services covered within the type", formalDefinition="Dental: basic, major, ortho; Vision exam, glasses, contacts; etc." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/benefit-subcategory")
    protected CodeableConcept benefitSubCategory;

    private static final long serialVersionUID = 899259023L;

  /**
   * Constructor
   */
    public EligibilityRequest() {
      super();
    }

    /**
     * @return {@link #identifier} (The Response business identifier.)
     */
    public List<Identifier> getIdentifier() { 
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      return this.identifier;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public EligibilityRequest setIdentifier(List<Identifier> theIdentifier) { 
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

    public EligibilityRequest addIdentifier(Identifier t) { //3
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
     * @return {@link #status} (The status of the resource instance.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<EligibilityRequestStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EligibilityRequest.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<EligibilityRequestStatus>(new EligibilityRequestStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (The status of the resource instance.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public EligibilityRequest setStatusElement(Enumeration<EligibilityRequestStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The status of the resource instance.
     */
    public EligibilityRequestStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The status of the resource instance.
     */
    public EligibilityRequest setStatus(EligibilityRequestStatus value) { 
      if (value == null)
        this.status = null;
      else {
        if (this.status == null)
          this.status = new Enumeration<EligibilityRequestStatus>(new EligibilityRequestStatusEnumFactory());
        this.status.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #priority} (Immediate (STAT), best effort (NORMAL), deferred (DEFER).)
     */
    public CodeableConcept getPriority() { 
      if (this.priority == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EligibilityRequest.priority");
        else if (Configuration.doAutoCreate())
          this.priority = new CodeableConcept(); // cc
      return this.priority;
    }

    public boolean hasPriority() { 
      return this.priority != null && !this.priority.isEmpty();
    }

    /**
     * @param value {@link #priority} (Immediate (STAT), best effort (NORMAL), deferred (DEFER).)
     */
    public EligibilityRequest setPriority(CodeableConcept value) { 
      this.priority = value;
      return this;
    }

    /**
     * @return {@link #patient} (Patient Resource.)
     */
    public Reference getPatient() { 
      if (this.patient == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EligibilityRequest.patient");
        else if (Configuration.doAutoCreate())
          this.patient = new Reference(); // cc
      return this.patient;
    }

    public boolean hasPatient() { 
      return this.patient != null && !this.patient.isEmpty();
    }

    /**
     * @param value {@link #patient} (Patient Resource.)
     */
    public EligibilityRequest setPatient(Reference value) { 
      this.patient = value;
      return this;
    }

    /**
     * @return {@link #patient} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Patient Resource.)
     */
    public Patient getPatientTarget() { 
      if (this.patientTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EligibilityRequest.patient");
        else if (Configuration.doAutoCreate())
          this.patientTarget = new Patient(); // aa
      return this.patientTarget;
    }

    /**
     * @param value {@link #patient} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Patient Resource.)
     */
    public EligibilityRequest setPatientTarget(Patient value) { 
      this.patientTarget = value;
      return this;
    }

    /**
     * @return {@link #serviced} (The date or dates when the enclosed suite of services were performed or completed.)
     */
    public Type getServiced() { 
      return this.serviced;
    }

    /**
     * @return {@link #serviced} (The date or dates when the enclosed suite of services were performed or completed.)
     */
    public DateType getServicedDateType() throws FHIRException { 
      if (!(this.serviced instanceof DateType))
        throw new FHIRException("Type mismatch: the type DateType was expected, but "+this.serviced.getClass().getName()+" was encountered");
      return (DateType) this.serviced;
    }

    public boolean hasServicedDateType() { 
      return this.serviced instanceof DateType;
    }

    /**
     * @return {@link #serviced} (The date or dates when the enclosed suite of services were performed or completed.)
     */
    public Period getServicedPeriod() throws FHIRException { 
      if (!(this.serviced instanceof Period))
        throw new FHIRException("Type mismatch: the type Period was expected, but "+this.serviced.getClass().getName()+" was encountered");
      return (Period) this.serviced;
    }

    public boolean hasServicedPeriod() { 
      return this.serviced instanceof Period;
    }

    public boolean hasServiced() { 
      return this.serviced != null && !this.serviced.isEmpty();
    }

    /**
     * @param value {@link #serviced} (The date or dates when the enclosed suite of services were performed or completed.)
     */
    public EligibilityRequest setServiced(Type value) { 
      this.serviced = value;
      return this;
    }

    /**
     * @return {@link #created} (The date when this resource was created.). This is the underlying object with id, value and extensions. The accessor "getCreated" gives direct access to the value
     */
    public DateTimeType getCreatedElement() { 
      if (this.created == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EligibilityRequest.created");
        else if (Configuration.doAutoCreate())
          this.created = new DateTimeType(); // bb
      return this.created;
    }

    public boolean hasCreatedElement() { 
      return this.created != null && !this.created.isEmpty();
    }

    public boolean hasCreated() { 
      return this.created != null && !this.created.isEmpty();
    }

    /**
     * @param value {@link #created} (The date when this resource was created.). This is the underlying object with id, value and extensions. The accessor "getCreated" gives direct access to the value
     */
    public EligibilityRequest setCreatedElement(DateTimeType value) { 
      this.created = value;
      return this;
    }

    /**
     * @return The date when this resource was created.
     */
    public Date getCreated() { 
      return this.created == null ? null : this.created.getValue();
    }

    /**
     * @param value The date when this resource was created.
     */
    public EligibilityRequest setCreated(Date value) { 
      if (value == null)
        this.created = null;
      else {
        if (this.created == null)
          this.created = new DateTimeType();
        this.created.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #enterer} (Person who created the invoice/claim/pre-determination or pre-authorization.)
     */
    public Reference getEnterer() { 
      if (this.enterer == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EligibilityRequest.enterer");
        else if (Configuration.doAutoCreate())
          this.enterer = new Reference(); // cc
      return this.enterer;
    }

    public boolean hasEnterer() { 
      return this.enterer != null && !this.enterer.isEmpty();
    }

    /**
     * @param value {@link #enterer} (Person who created the invoice/claim/pre-determination or pre-authorization.)
     */
    public EligibilityRequest setEnterer(Reference value) { 
      this.enterer = value;
      return this;
    }

    /**
     * @return {@link #enterer} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Person who created the invoice/claim/pre-determination or pre-authorization.)
     */
    public Practitioner getEntererTarget() { 
      if (this.entererTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EligibilityRequest.enterer");
        else if (Configuration.doAutoCreate())
          this.entererTarget = new Practitioner(); // aa
      return this.entererTarget;
    }

    /**
     * @param value {@link #enterer} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Person who created the invoice/claim/pre-determination or pre-authorization.)
     */
    public EligibilityRequest setEntererTarget(Practitioner value) { 
      this.entererTarget = value;
      return this;
    }

    /**
     * @return {@link #provider} (The practitioner who is responsible for the services rendered to the patient.)
     */
    public Reference getProvider() { 
      if (this.provider == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EligibilityRequest.provider");
        else if (Configuration.doAutoCreate())
          this.provider = new Reference(); // cc
      return this.provider;
    }

    public boolean hasProvider() { 
      return this.provider != null && !this.provider.isEmpty();
    }

    /**
     * @param value {@link #provider} (The practitioner who is responsible for the services rendered to the patient.)
     */
    public EligibilityRequest setProvider(Reference value) { 
      this.provider = value;
      return this;
    }

    /**
     * @return {@link #provider} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The practitioner who is responsible for the services rendered to the patient.)
     */
    public Practitioner getProviderTarget() { 
      if (this.providerTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EligibilityRequest.provider");
        else if (Configuration.doAutoCreate())
          this.providerTarget = new Practitioner(); // aa
      return this.providerTarget;
    }

    /**
     * @param value {@link #provider} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The practitioner who is responsible for the services rendered to the patient.)
     */
    public EligibilityRequest setProviderTarget(Practitioner value) { 
      this.providerTarget = value;
      return this;
    }

    /**
     * @return {@link #organization} (The organization which is responsible for the services rendered to the patient.)
     */
    public Reference getOrganization() { 
      if (this.organization == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EligibilityRequest.organization");
        else if (Configuration.doAutoCreate())
          this.organization = new Reference(); // cc
      return this.organization;
    }

    public boolean hasOrganization() { 
      return this.organization != null && !this.organization.isEmpty();
    }

    /**
     * @param value {@link #organization} (The organization which is responsible for the services rendered to the patient.)
     */
    public EligibilityRequest setOrganization(Reference value) { 
      this.organization = value;
      return this;
    }

    /**
     * @return {@link #organization} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The organization which is responsible for the services rendered to the patient.)
     */
    public Organization getOrganizationTarget() { 
      if (this.organizationTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EligibilityRequest.organization");
        else if (Configuration.doAutoCreate())
          this.organizationTarget = new Organization(); // aa
      return this.organizationTarget;
    }

    /**
     * @param value {@link #organization} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The organization which is responsible for the services rendered to the patient.)
     */
    public EligibilityRequest setOrganizationTarget(Organization value) { 
      this.organizationTarget = value;
      return this;
    }

    /**
     * @return {@link #insurer} (The Insurer who is target  of the request.)
     */
    public Reference getInsurer() { 
      if (this.insurer == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EligibilityRequest.insurer");
        else if (Configuration.doAutoCreate())
          this.insurer = new Reference(); // cc
      return this.insurer;
    }

    public boolean hasInsurer() { 
      return this.insurer != null && !this.insurer.isEmpty();
    }

    /**
     * @param value {@link #insurer} (The Insurer who is target  of the request.)
     */
    public EligibilityRequest setInsurer(Reference value) { 
      this.insurer = value;
      return this;
    }

    /**
     * @return {@link #insurer} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The Insurer who is target  of the request.)
     */
    public Organization getInsurerTarget() { 
      if (this.insurerTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EligibilityRequest.insurer");
        else if (Configuration.doAutoCreate())
          this.insurerTarget = new Organization(); // aa
      return this.insurerTarget;
    }

    /**
     * @param value {@link #insurer} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The Insurer who is target  of the request.)
     */
    public EligibilityRequest setInsurerTarget(Organization value) { 
      this.insurerTarget = value;
      return this;
    }

    /**
     * @return {@link #facility} (Facility where the services were provided.)
     */
    public Reference getFacility() { 
      if (this.facility == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EligibilityRequest.facility");
        else if (Configuration.doAutoCreate())
          this.facility = new Reference(); // cc
      return this.facility;
    }

    public boolean hasFacility() { 
      return this.facility != null && !this.facility.isEmpty();
    }

    /**
     * @param value {@link #facility} (Facility where the services were provided.)
     */
    public EligibilityRequest setFacility(Reference value) { 
      this.facility = value;
      return this;
    }

    /**
     * @return {@link #facility} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Facility where the services were provided.)
     */
    public Location getFacilityTarget() { 
      if (this.facilityTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EligibilityRequest.facility");
        else if (Configuration.doAutoCreate())
          this.facilityTarget = new Location(); // aa
      return this.facilityTarget;
    }

    /**
     * @param value {@link #facility} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Facility where the services were provided.)
     */
    public EligibilityRequest setFacilityTarget(Location value) { 
      this.facilityTarget = value;
      return this;
    }

    /**
     * @return {@link #coverage} (Financial instrument by which payment information for health care.)
     */
    public Reference getCoverage() { 
      if (this.coverage == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EligibilityRequest.coverage");
        else if (Configuration.doAutoCreate())
          this.coverage = new Reference(); // cc
      return this.coverage;
    }

    public boolean hasCoverage() { 
      return this.coverage != null && !this.coverage.isEmpty();
    }

    /**
     * @param value {@link #coverage} (Financial instrument by which payment information for health care.)
     */
    public EligibilityRequest setCoverage(Reference value) { 
      this.coverage = value;
      return this;
    }

    /**
     * @return {@link #coverage} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Financial instrument by which payment information for health care.)
     */
    public Coverage getCoverageTarget() { 
      if (this.coverageTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EligibilityRequest.coverage");
        else if (Configuration.doAutoCreate())
          this.coverageTarget = new Coverage(); // aa
      return this.coverageTarget;
    }

    /**
     * @param value {@link #coverage} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Financial instrument by which payment information for health care.)
     */
    public EligibilityRequest setCoverageTarget(Coverage value) { 
      this.coverageTarget = value;
      return this;
    }

    /**
     * @return {@link #businessArrangement} (The contract number of a business agreement which describes the terms and conditions.). This is the underlying object with id, value and extensions. The accessor "getBusinessArrangement" gives direct access to the value
     */
    public StringType getBusinessArrangementElement() { 
      if (this.businessArrangement == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EligibilityRequest.businessArrangement");
        else if (Configuration.doAutoCreate())
          this.businessArrangement = new StringType(); // bb
      return this.businessArrangement;
    }

    public boolean hasBusinessArrangementElement() { 
      return this.businessArrangement != null && !this.businessArrangement.isEmpty();
    }

    public boolean hasBusinessArrangement() { 
      return this.businessArrangement != null && !this.businessArrangement.isEmpty();
    }

    /**
     * @param value {@link #businessArrangement} (The contract number of a business agreement which describes the terms and conditions.). This is the underlying object with id, value and extensions. The accessor "getBusinessArrangement" gives direct access to the value
     */
    public EligibilityRequest setBusinessArrangementElement(StringType value) { 
      this.businessArrangement = value;
      return this;
    }

    /**
     * @return The contract number of a business agreement which describes the terms and conditions.
     */
    public String getBusinessArrangement() { 
      return this.businessArrangement == null ? null : this.businessArrangement.getValue();
    }

    /**
     * @param value The contract number of a business agreement which describes the terms and conditions.
     */
    public EligibilityRequest setBusinessArrangement(String value) { 
      if (Utilities.noString(value))
        this.businessArrangement = null;
      else {
        if (this.businessArrangement == null)
          this.businessArrangement = new StringType();
        this.businessArrangement.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #benefitCategory} (Dental, Vision, Medical, Pharmacy, Rehab etc.)
     */
    public CodeableConcept getBenefitCategory() { 
      if (this.benefitCategory == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EligibilityRequest.benefitCategory");
        else if (Configuration.doAutoCreate())
          this.benefitCategory = new CodeableConcept(); // cc
      return this.benefitCategory;
    }

    public boolean hasBenefitCategory() { 
      return this.benefitCategory != null && !this.benefitCategory.isEmpty();
    }

    /**
     * @param value {@link #benefitCategory} (Dental, Vision, Medical, Pharmacy, Rehab etc.)
     */
    public EligibilityRequest setBenefitCategory(CodeableConcept value) { 
      this.benefitCategory = value;
      return this;
    }

    /**
     * @return {@link #benefitSubCategory} (Dental: basic, major, ortho; Vision exam, glasses, contacts; etc.)
     */
    public CodeableConcept getBenefitSubCategory() { 
      if (this.benefitSubCategory == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EligibilityRequest.benefitSubCategory");
        else if (Configuration.doAutoCreate())
          this.benefitSubCategory = new CodeableConcept(); // cc
      return this.benefitSubCategory;
    }

    public boolean hasBenefitSubCategory() { 
      return this.benefitSubCategory != null && !this.benefitSubCategory.isEmpty();
    }

    /**
     * @param value {@link #benefitSubCategory} (Dental: basic, major, ortho; Vision exam, glasses, contacts; etc.)
     */
    public EligibilityRequest setBenefitSubCategory(CodeableConcept value) { 
      this.benefitSubCategory = value;
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "The Response business identifier.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("status", "code", "The status of the resource instance.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("priority", "CodeableConcept", "Immediate (STAT), best effort (NORMAL), deferred (DEFER).", 0, java.lang.Integer.MAX_VALUE, priority));
        childrenList.add(new Property("patient", "Reference(Patient)", "Patient Resource.", 0, java.lang.Integer.MAX_VALUE, patient));
        childrenList.add(new Property("serviced[x]", "date|Period", "The date or dates when the enclosed suite of services were performed or completed.", 0, java.lang.Integer.MAX_VALUE, serviced));
        childrenList.add(new Property("created", "dateTime", "The date when this resource was created.", 0, java.lang.Integer.MAX_VALUE, created));
        childrenList.add(new Property("enterer", "Reference(Practitioner)", "Person who created the invoice/claim/pre-determination or pre-authorization.", 0, java.lang.Integer.MAX_VALUE, enterer));
        childrenList.add(new Property("provider", "Reference(Practitioner)", "The practitioner who is responsible for the services rendered to the patient.", 0, java.lang.Integer.MAX_VALUE, provider));
        childrenList.add(new Property("organization", "Reference(Organization)", "The organization which is responsible for the services rendered to the patient.", 0, java.lang.Integer.MAX_VALUE, organization));
        childrenList.add(new Property("insurer", "Reference(Organization)", "The Insurer who is target  of the request.", 0, java.lang.Integer.MAX_VALUE, insurer));
        childrenList.add(new Property("facility", "Reference(Location)", "Facility where the services were provided.", 0, java.lang.Integer.MAX_VALUE, facility));
        childrenList.add(new Property("coverage", "Reference(Coverage)", "Financial instrument by which payment information for health care.", 0, java.lang.Integer.MAX_VALUE, coverage));
        childrenList.add(new Property("businessArrangement", "string", "The contract number of a business agreement which describes the terms and conditions.", 0, java.lang.Integer.MAX_VALUE, businessArrangement));
        childrenList.add(new Property("benefitCategory", "CodeableConcept", "Dental, Vision, Medical, Pharmacy, Rehab etc.", 0, java.lang.Integer.MAX_VALUE, benefitCategory));
        childrenList.add(new Property("benefitSubCategory", "CodeableConcept", "Dental: basic, major, ortho; Vision exam, glasses, contacts; etc.", 0, java.lang.Integer.MAX_VALUE, benefitSubCategory));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<EligibilityRequestStatus>
        case -1165461084: /*priority*/ return this.priority == null ? new Base[0] : new Base[] {this.priority}; // CodeableConcept
        case -791418107: /*patient*/ return this.patient == null ? new Base[0] : new Base[] {this.patient}; // Reference
        case 1379209295: /*serviced*/ return this.serviced == null ? new Base[0] : new Base[] {this.serviced}; // Type
        case 1028554472: /*created*/ return this.created == null ? new Base[0] : new Base[] {this.created}; // DateTimeType
        case -1591951995: /*enterer*/ return this.enterer == null ? new Base[0] : new Base[] {this.enterer}; // Reference
        case -987494927: /*provider*/ return this.provider == null ? new Base[0] : new Base[] {this.provider}; // Reference
        case 1178922291: /*organization*/ return this.organization == null ? new Base[0] : new Base[] {this.organization}; // Reference
        case 1957615864: /*insurer*/ return this.insurer == null ? new Base[0] : new Base[] {this.insurer}; // Reference
        case 501116579: /*facility*/ return this.facility == null ? new Base[0] : new Base[] {this.facility}; // Reference
        case -351767064: /*coverage*/ return this.coverage == null ? new Base[0] : new Base[] {this.coverage}; // Reference
        case 259920682: /*businessArrangement*/ return this.businessArrangement == null ? new Base[0] : new Base[] {this.businessArrangement}; // StringType
        case -1023390027: /*benefitCategory*/ return this.benefitCategory == null ? new Base[0] : new Base[] {this.benefitCategory}; // CodeableConcept
        case 1987878471: /*benefitSubCategory*/ return this.benefitSubCategory == null ? new Base[0] : new Base[] {this.benefitSubCategory}; // CodeableConcept
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.getIdentifier().add(castToIdentifier(value)); // Identifier
          return value;
        case -892481550: // status
          value = new EligibilityRequestStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<EligibilityRequestStatus>
          return value;
        case -1165461084: // priority
          this.priority = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -791418107: // patient
          this.patient = castToReference(value); // Reference
          return value;
        case 1379209295: // serviced
          this.serviced = castToType(value); // Type
          return value;
        case 1028554472: // created
          this.created = castToDateTime(value); // DateTimeType
          return value;
        case -1591951995: // enterer
          this.enterer = castToReference(value); // Reference
          return value;
        case -987494927: // provider
          this.provider = castToReference(value); // Reference
          return value;
        case 1178922291: // organization
          this.organization = castToReference(value); // Reference
          return value;
        case 1957615864: // insurer
          this.insurer = castToReference(value); // Reference
          return value;
        case 501116579: // facility
          this.facility = castToReference(value); // Reference
          return value;
        case -351767064: // coverage
          this.coverage = castToReference(value); // Reference
          return value;
        case 259920682: // businessArrangement
          this.businessArrangement = castToString(value); // StringType
          return value;
        case -1023390027: // benefitCategory
          this.benefitCategory = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 1987878471: // benefitSubCategory
          this.benefitSubCategory = castToCodeableConcept(value); // CodeableConcept
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier")) {
          this.getIdentifier().add(castToIdentifier(value));
        } else if (name.equals("status")) {
          value = new EligibilityRequestStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<EligibilityRequestStatus>
        } else if (name.equals("priority")) {
          this.priority = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("patient")) {
          this.patient = castToReference(value); // Reference
        } else if (name.equals("serviced[x]")) {
          this.serviced = castToType(value); // Type
        } else if (name.equals("created")) {
          this.created = castToDateTime(value); // DateTimeType
        } else if (name.equals("enterer")) {
          this.enterer = castToReference(value); // Reference
        } else if (name.equals("provider")) {
          this.provider = castToReference(value); // Reference
        } else if (name.equals("organization")) {
          this.organization = castToReference(value); // Reference
        } else if (name.equals("insurer")) {
          this.insurer = castToReference(value); // Reference
        } else if (name.equals("facility")) {
          this.facility = castToReference(value); // Reference
        } else if (name.equals("coverage")) {
          this.coverage = castToReference(value); // Reference
        } else if (name.equals("businessArrangement")) {
          this.businessArrangement = castToString(value); // StringType
        } else if (name.equals("benefitCategory")) {
          this.benefitCategory = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("benefitSubCategory")) {
          this.benefitSubCategory = castToCodeableConcept(value); // CodeableConcept
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return addIdentifier(); 
        case -892481550:  return getStatusElement();
        case -1165461084:  return getPriority(); 
        case -791418107:  return getPatient(); 
        case -1927922223:  return getServiced(); 
        case 1379209295:  return getServiced(); 
        case 1028554472:  return getCreatedElement();
        case -1591951995:  return getEnterer(); 
        case -987494927:  return getProvider(); 
        case 1178922291:  return getOrganization(); 
        case 1957615864:  return getInsurer(); 
        case 501116579:  return getFacility(); 
        case -351767064:  return getCoverage(); 
        case 259920682:  return getBusinessArrangementElement();
        case -1023390027:  return getBenefitCategory(); 
        case 1987878471:  return getBenefitSubCategory(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case -892481550: /*status*/ return new String[] {"code"};
        case -1165461084: /*priority*/ return new String[] {"CodeableConcept"};
        case -791418107: /*patient*/ return new String[] {"Reference"};
        case 1379209295: /*serviced*/ return new String[] {"date", "Period"};
        case 1028554472: /*created*/ return new String[] {"dateTime"};
        case -1591951995: /*enterer*/ return new String[] {"Reference"};
        case -987494927: /*provider*/ return new String[] {"Reference"};
        case 1178922291: /*organization*/ return new String[] {"Reference"};
        case 1957615864: /*insurer*/ return new String[] {"Reference"};
        case 501116579: /*facility*/ return new String[] {"Reference"};
        case -351767064: /*coverage*/ return new String[] {"Reference"};
        case 259920682: /*businessArrangement*/ return new String[] {"string"};
        case -1023390027: /*benefitCategory*/ return new String[] {"CodeableConcept"};
        case 1987878471: /*benefitSubCategory*/ return new String[] {"CodeableConcept"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type EligibilityRequest.status");
        }
        else if (name.equals("priority")) {
          this.priority = new CodeableConcept();
          return this.priority;
        }
        else if (name.equals("patient")) {
          this.patient = new Reference();
          return this.patient;
        }
        else if (name.equals("servicedDate")) {
          this.serviced = new DateType();
          return this.serviced;
        }
        else if (name.equals("servicedPeriod")) {
          this.serviced = new Period();
          return this.serviced;
        }
        else if (name.equals("created")) {
          throw new FHIRException("Cannot call addChild on a primitive type EligibilityRequest.created");
        }
        else if (name.equals("enterer")) {
          this.enterer = new Reference();
          return this.enterer;
        }
        else if (name.equals("provider")) {
          this.provider = new Reference();
          return this.provider;
        }
        else if (name.equals("organization")) {
          this.organization = new Reference();
          return this.organization;
        }
        else if (name.equals("insurer")) {
          this.insurer = new Reference();
          return this.insurer;
        }
        else if (name.equals("facility")) {
          this.facility = new Reference();
          return this.facility;
        }
        else if (name.equals("coverage")) {
          this.coverage = new Reference();
          return this.coverage;
        }
        else if (name.equals("businessArrangement")) {
          throw new FHIRException("Cannot call addChild on a primitive type EligibilityRequest.businessArrangement");
        }
        else if (name.equals("benefitCategory")) {
          this.benefitCategory = new CodeableConcept();
          return this.benefitCategory;
        }
        else if (name.equals("benefitSubCategory")) {
          this.benefitSubCategory = new CodeableConcept();
          return this.benefitSubCategory;
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "EligibilityRequest";

  }

      public EligibilityRequest copy() {
        EligibilityRequest dst = new EligibilityRequest();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.status = status == null ? null : status.copy();
        dst.priority = priority == null ? null : priority.copy();
        dst.patient = patient == null ? null : patient.copy();
        dst.serviced = serviced == null ? null : serviced.copy();
        dst.created = created == null ? null : created.copy();
        dst.enterer = enterer == null ? null : enterer.copy();
        dst.provider = provider == null ? null : provider.copy();
        dst.organization = organization == null ? null : organization.copy();
        dst.insurer = insurer == null ? null : insurer.copy();
        dst.facility = facility == null ? null : facility.copy();
        dst.coverage = coverage == null ? null : coverage.copy();
        dst.businessArrangement = businessArrangement == null ? null : businessArrangement.copy();
        dst.benefitCategory = benefitCategory == null ? null : benefitCategory.copy();
        dst.benefitSubCategory = benefitSubCategory == null ? null : benefitSubCategory.copy();
        return dst;
      }

      protected EligibilityRequest typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof EligibilityRequest))
          return false;
        EligibilityRequest o = (EligibilityRequest) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(status, o.status, true) && compareDeep(priority, o.priority, true)
           && compareDeep(patient, o.patient, true) && compareDeep(serviced, o.serviced, true) && compareDeep(created, o.created, true)
           && compareDeep(enterer, o.enterer, true) && compareDeep(provider, o.provider, true) && compareDeep(organization, o.organization, true)
           && compareDeep(insurer, o.insurer, true) && compareDeep(facility, o.facility, true) && compareDeep(coverage, o.coverage, true)
           && compareDeep(businessArrangement, o.businessArrangement, true) && compareDeep(benefitCategory, o.benefitCategory, true)
           && compareDeep(benefitSubCategory, o.benefitSubCategory, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof EligibilityRequest))
          return false;
        EligibilityRequest o = (EligibilityRequest) other;
        return compareValues(status, o.status, true) && compareValues(created, o.created, true) && compareValues(businessArrangement, o.businessArrangement, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, status, priority
          , patient, serviced, created, enterer, provider, organization, insurer, facility
          , coverage, businessArrangement, benefitCategory, benefitSubCategory);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.EligibilityRequest;
   }

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>The business identifier of the Eligibility</b><br>
   * Type: <b>token</b><br>
   * Path: <b>EligibilityRequest.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="EligibilityRequest.identifier", description="The business identifier of the Eligibility", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>The business identifier of the Eligibility</b><br>
   * Type: <b>token</b><br>
   * Path: <b>EligibilityRequest.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>provider</b>
   * <p>
   * Description: <b>The reference to the provider</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>EligibilityRequest.provider</b><br>
   * </p>
   */
  @SearchParamDefinition(name="provider", path="EligibilityRequest.provider", description="The reference to the provider", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Practitioner") }, target={Practitioner.class } )
  public static final String SP_PROVIDER = "provider";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>provider</b>
   * <p>
   * Description: <b>The reference to the provider</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>EligibilityRequest.provider</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PROVIDER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PROVIDER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>EligibilityRequest:provider</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PROVIDER = new ca.uhn.fhir.model.api.Include("EligibilityRequest:provider").toLocked();

 /**
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>The reference to the patient</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>EligibilityRequest.patient</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="EligibilityRequest.patient", description="The reference to the patient", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient") }, target={Patient.class } )
  public static final String SP_PATIENT = "patient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patient</b>
   * <p>
   * Description: <b>The reference to the patient</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>EligibilityRequest.patient</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PATIENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PATIENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>EligibilityRequest:patient</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PATIENT = new ca.uhn.fhir.model.api.Include("EligibilityRequest:patient").toLocked();

 /**
   * Search parameter: <b>created</b>
   * <p>
   * Description: <b>The creation date for the EOB</b><br>
   * Type: <b>date</b><br>
   * Path: <b>EligibilityRequest.created</b><br>
   * </p>
   */
  @SearchParamDefinition(name="created", path="EligibilityRequest.created", description="The creation date for the EOB", type="date" )
  public static final String SP_CREATED = "created";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>created</b>
   * <p>
   * Description: <b>The creation date for the EOB</b><br>
   * Type: <b>date</b><br>
   * Path: <b>EligibilityRequest.created</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam CREATED = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_CREATED);

 /**
   * Search parameter: <b>organization</b>
   * <p>
   * Description: <b>The reference to the providing organization</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>EligibilityRequest.organization</b><br>
   * </p>
   */
  @SearchParamDefinition(name="organization", path="EligibilityRequest.organization", description="The reference to the providing organization", type="reference", target={Organization.class } )
  public static final String SP_ORGANIZATION = "organization";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>organization</b>
   * <p>
   * Description: <b>The reference to the providing organization</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>EligibilityRequest.organization</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam ORGANIZATION = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_ORGANIZATION);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>EligibilityRequest:organization</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_ORGANIZATION = new ca.uhn.fhir.model.api.Include("EligibilityRequest:organization").toLocked();

 /**
   * Search parameter: <b>enterer</b>
   * <p>
   * Description: <b>The party who is responsible for the request</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>EligibilityRequest.enterer</b><br>
   * </p>
   */
  @SearchParamDefinition(name="enterer", path="EligibilityRequest.enterer", description="The party who is responsible for the request", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Practitioner") }, target={Practitioner.class } )
  public static final String SP_ENTERER = "enterer";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>enterer</b>
   * <p>
   * Description: <b>The party who is responsible for the request</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>EligibilityRequest.enterer</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam ENTERER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_ENTERER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>EligibilityRequest:enterer</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_ENTERER = new ca.uhn.fhir.model.api.Include("EligibilityRequest:enterer").toLocked();

 /**
   * Search parameter: <b>facility</b>
   * <p>
   * Description: <b>Facility responsible for the goods and services</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>EligibilityRequest.facility</b><br>
   * </p>
   */
  @SearchParamDefinition(name="facility", path="EligibilityRequest.facility", description="Facility responsible for the goods and services", type="reference", target={Location.class } )
  public static final String SP_FACILITY = "facility";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>facility</b>
   * <p>
   * Description: <b>Facility responsible for the goods and services</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>EligibilityRequest.facility</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam FACILITY = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_FACILITY);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>EligibilityRequest:facility</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_FACILITY = new ca.uhn.fhir.model.api.Include("EligibilityRequest:facility").toLocked();


}

