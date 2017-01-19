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
import org.hl7.fhir.utilities.Utilities;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
/**
 * This resource provides the insurance eligibility details from the insurer regarding a specified coverage and optionally some class of service.
 */
@ResourceDef(name="EligibilityRequest", profile="http://hl7.org/fhir/Profile/EligibilityRequest")
public class EligibilityRequest extends DomainResource {

    /**
     * The Response business identifier.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Business Identifier", formalDefinition="The Response business identifier." )
    protected List<Identifier> identifier;

    /**
     * The version of the style of resource contents. This should be mapped to the allowable profiles for this and supporting resources.
     */
    @Child(name = "ruleset", type = {Coding.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Resource version", formalDefinition="The version of the style of resource contents. This should be mapped to the allowable profiles for this and supporting resources." )
    protected Coding ruleset;

    /**
     * The style (standard) and version of the original material which was converted into this resource.
     */
    @Child(name = "originalRuleset", type = {Coding.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Original version", formalDefinition="The style (standard) and version of the original material which was converted into this resource." )
    protected Coding originalRuleset;

    /**
     * The date when this resource was created.
     */
    @Child(name = "created", type = {DateTimeType.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Creation date", formalDefinition="The date when this resource was created." )
    protected DateTimeType created;

    /**
     * The Insurer who is target  of the request.
     */
    @Child(name = "target", type = {Identifier.class, Organization.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Insurer", formalDefinition="The Insurer who is target  of the request." )
    protected Type target;

    /**
     * The practitioner who is responsible for the services rendered to the patient.
     */
    @Child(name = "provider", type = {Identifier.class, Practitioner.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Responsible practitioner", formalDefinition="The practitioner who is responsible for the services rendered to the patient." )
    protected Type provider;

    /**
     * The organization which is responsible for the services rendered to the patient.
     */
    @Child(name = "organization", type = {Identifier.class, Organization.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Responsible organization", formalDefinition="The organization which is responsible for the services rendered to the patient." )
    protected Type organization;

    /**
     * Immediate (STAT), best effort (NORMAL), deferred (DEFER).
     */
    @Child(name = "priority", type = {Coding.class}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Desired processing priority", formalDefinition="Immediate (STAT), best effort (NORMAL), deferred (DEFER)." )
    protected Coding priority;

    /**
     * Person who created the invoice/claim/pre-determination or pre-authorization.
     */
    @Child(name = "enterer", type = {Identifier.class, Practitioner.class}, order=8, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Author", formalDefinition="Person who created the invoice/claim/pre-determination or pre-authorization." )
    protected Type enterer;

    /**
     * Facility where the services were provided.
     */
    @Child(name = "facility", type = {Identifier.class, Location.class}, order=9, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Servicing Facility", formalDefinition="Facility where the services were provided." )
    protected Type facility;

    /**
     * Patient Resource.
     */
    @Child(name = "patient", type = {Identifier.class, Patient.class}, order=10, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The subject of the Products and Services", formalDefinition="Patient Resource." )
    protected Type patient;

    /**
     * Financial instrument by which payment information for health care.
     */
    @Child(name = "coverage", type = {Identifier.class, Coverage.class}, order=11, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Insurance or medical plan", formalDefinition="Financial instrument by which payment information for health care." )
    protected Type coverage;

    /**
     * The contract number of a business agreement which describes the terms and conditions.
     */
    @Child(name = "businessArrangement", type = {StringType.class}, order=12, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Business agreement", formalDefinition="The contract number of a business agreement which describes the terms and conditions." )
    protected StringType businessArrangement;

    /**
     * The date or dates when the enclosed suite of services were performed or completed.
     */
    @Child(name = "serviced", type = {DateType.class, Period.class}, order=13, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Estimated date or dates of Service", formalDefinition="The date or dates when the enclosed suite of services were performed or completed." )
    protected Type serviced;

    /**
     * Dental, Vision, Medical, Pharmacy, Rehab etc.
     */
    @Child(name = "benefitCategory", type = {Coding.class}, order=14, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Benefit Category", formalDefinition="Dental, Vision, Medical, Pharmacy, Rehab etc." )
    protected Coding benefitCategory;

    /**
     * Dental: basic, major, ortho; Vision exam, glasses, contacts; etc.
     */
    @Child(name = "benefitSubCategory", type = {Coding.class}, order=15, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Benefit SubCategory", formalDefinition="Dental: basic, major, ortho; Vision exam, glasses, contacts; etc." )
    protected Coding benefitSubCategory;

    private static final long serialVersionUID = 313969968L;

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

    public boolean hasIdentifier() { 
      if (this.identifier == null)
        return false;
      for (Identifier item : this.identifier)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #identifier} (The Response business identifier.)
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
    public EligibilityRequest addIdentifier(Identifier t) { //3
      if (t == null)
        return this;
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return this;
    }

    /**
     * @return {@link #ruleset} (The version of the style of resource contents. This should be mapped to the allowable profiles for this and supporting resources.)
     */
    public Coding getRuleset() { 
      if (this.ruleset == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EligibilityRequest.ruleset");
        else if (Configuration.doAutoCreate())
          this.ruleset = new Coding(); // cc
      return this.ruleset;
    }

    public boolean hasRuleset() { 
      return this.ruleset != null && !this.ruleset.isEmpty();
    }

    /**
     * @param value {@link #ruleset} (The version of the style of resource contents. This should be mapped to the allowable profiles for this and supporting resources.)
     */
    public EligibilityRequest setRuleset(Coding value) { 
      this.ruleset = value;
      return this;
    }

    /**
     * @return {@link #originalRuleset} (The style (standard) and version of the original material which was converted into this resource.)
     */
    public Coding getOriginalRuleset() { 
      if (this.originalRuleset == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EligibilityRequest.originalRuleset");
        else if (Configuration.doAutoCreate())
          this.originalRuleset = new Coding(); // cc
      return this.originalRuleset;
    }

    public boolean hasOriginalRuleset() { 
      return this.originalRuleset != null && !this.originalRuleset.isEmpty();
    }

    /**
     * @param value {@link #originalRuleset} (The style (standard) and version of the original material which was converted into this resource.)
     */
    public EligibilityRequest setOriginalRuleset(Coding value) { 
      this.originalRuleset = value;
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
     * @return {@link #target} (The Insurer who is target  of the request.)
     */
    public Type getTarget() { 
      return this.target;
    }

    /**
     * @return {@link #target} (The Insurer who is target  of the request.)
     */
    public Identifier getTargetIdentifier() throws FHIRException { 
      if (!(this.target instanceof Identifier))
        throw new FHIRException("Type mismatch: the type Identifier was expected, but "+this.target.getClass().getName()+" was encountered");
      return (Identifier) this.target;
    }

    public boolean hasTargetIdentifier() { 
      return this.target instanceof Identifier;
    }

    /**
     * @return {@link #target} (The Insurer who is target  of the request.)
     */
    public Reference getTargetReference() throws FHIRException { 
      if (!(this.target instanceof Reference))
        throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.target.getClass().getName()+" was encountered");
      return (Reference) this.target;
    }

    public boolean hasTargetReference() { 
      return this.target instanceof Reference;
    }

    public boolean hasTarget() { 
      return this.target != null && !this.target.isEmpty();
    }

    /**
     * @param value {@link #target} (The Insurer who is target  of the request.)
     */
    public EligibilityRequest setTarget(Type value) { 
      this.target = value;
      return this;
    }

    /**
     * @return {@link #provider} (The practitioner who is responsible for the services rendered to the patient.)
     */
    public Type getProvider() { 
      return this.provider;
    }

    /**
     * @return {@link #provider} (The practitioner who is responsible for the services rendered to the patient.)
     */
    public Identifier getProviderIdentifier() throws FHIRException { 
      if (!(this.provider instanceof Identifier))
        throw new FHIRException("Type mismatch: the type Identifier was expected, but "+this.provider.getClass().getName()+" was encountered");
      return (Identifier) this.provider;
    }

    public boolean hasProviderIdentifier() { 
      return this.provider instanceof Identifier;
    }

    /**
     * @return {@link #provider} (The practitioner who is responsible for the services rendered to the patient.)
     */
    public Reference getProviderReference() throws FHIRException { 
      if (!(this.provider instanceof Reference))
        throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.provider.getClass().getName()+" was encountered");
      return (Reference) this.provider;
    }

    public boolean hasProviderReference() { 
      return this.provider instanceof Reference;
    }

    public boolean hasProvider() { 
      return this.provider != null && !this.provider.isEmpty();
    }

    /**
     * @param value {@link #provider} (The practitioner who is responsible for the services rendered to the patient.)
     */
    public EligibilityRequest setProvider(Type value) { 
      this.provider = value;
      return this;
    }

    /**
     * @return {@link #organization} (The organization which is responsible for the services rendered to the patient.)
     */
    public Type getOrganization() { 
      return this.organization;
    }

    /**
     * @return {@link #organization} (The organization which is responsible for the services rendered to the patient.)
     */
    public Identifier getOrganizationIdentifier() throws FHIRException { 
      if (!(this.organization instanceof Identifier))
        throw new FHIRException("Type mismatch: the type Identifier was expected, but "+this.organization.getClass().getName()+" was encountered");
      return (Identifier) this.organization;
    }

    public boolean hasOrganizationIdentifier() { 
      return this.organization instanceof Identifier;
    }

    /**
     * @return {@link #organization} (The organization which is responsible for the services rendered to the patient.)
     */
    public Reference getOrganizationReference() throws FHIRException { 
      if (!(this.organization instanceof Reference))
        throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.organization.getClass().getName()+" was encountered");
      return (Reference) this.organization;
    }

    public boolean hasOrganizationReference() { 
      return this.organization instanceof Reference;
    }

    public boolean hasOrganization() { 
      return this.organization != null && !this.organization.isEmpty();
    }

    /**
     * @param value {@link #organization} (The organization which is responsible for the services rendered to the patient.)
     */
    public EligibilityRequest setOrganization(Type value) { 
      this.organization = value;
      return this;
    }

    /**
     * @return {@link #priority} (Immediate (STAT), best effort (NORMAL), deferred (DEFER).)
     */
    public Coding getPriority() { 
      if (this.priority == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EligibilityRequest.priority");
        else if (Configuration.doAutoCreate())
          this.priority = new Coding(); // cc
      return this.priority;
    }

    public boolean hasPriority() { 
      return this.priority != null && !this.priority.isEmpty();
    }

    /**
     * @param value {@link #priority} (Immediate (STAT), best effort (NORMAL), deferred (DEFER).)
     */
    public EligibilityRequest setPriority(Coding value) { 
      this.priority = value;
      return this;
    }

    /**
     * @return {@link #enterer} (Person who created the invoice/claim/pre-determination or pre-authorization.)
     */
    public Type getEnterer() { 
      return this.enterer;
    }

    /**
     * @return {@link #enterer} (Person who created the invoice/claim/pre-determination or pre-authorization.)
     */
    public Identifier getEntererIdentifier() throws FHIRException { 
      if (!(this.enterer instanceof Identifier))
        throw new FHIRException("Type mismatch: the type Identifier was expected, but "+this.enterer.getClass().getName()+" was encountered");
      return (Identifier) this.enterer;
    }

    public boolean hasEntererIdentifier() { 
      return this.enterer instanceof Identifier;
    }

    /**
     * @return {@link #enterer} (Person who created the invoice/claim/pre-determination or pre-authorization.)
     */
    public Reference getEntererReference() throws FHIRException { 
      if (!(this.enterer instanceof Reference))
        throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.enterer.getClass().getName()+" was encountered");
      return (Reference) this.enterer;
    }

    public boolean hasEntererReference() { 
      return this.enterer instanceof Reference;
    }

    public boolean hasEnterer() { 
      return this.enterer != null && !this.enterer.isEmpty();
    }

    /**
     * @param value {@link #enterer} (Person who created the invoice/claim/pre-determination or pre-authorization.)
     */
    public EligibilityRequest setEnterer(Type value) { 
      this.enterer = value;
      return this;
    }

    /**
     * @return {@link #facility} (Facility where the services were provided.)
     */
    public Type getFacility() { 
      return this.facility;
    }

    /**
     * @return {@link #facility} (Facility where the services were provided.)
     */
    public Identifier getFacilityIdentifier() throws FHIRException { 
      if (!(this.facility instanceof Identifier))
        throw new FHIRException("Type mismatch: the type Identifier was expected, but "+this.facility.getClass().getName()+" was encountered");
      return (Identifier) this.facility;
    }

    public boolean hasFacilityIdentifier() { 
      return this.facility instanceof Identifier;
    }

    /**
     * @return {@link #facility} (Facility where the services were provided.)
     */
    public Reference getFacilityReference() throws FHIRException { 
      if (!(this.facility instanceof Reference))
        throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.facility.getClass().getName()+" was encountered");
      return (Reference) this.facility;
    }

    public boolean hasFacilityReference() { 
      return this.facility instanceof Reference;
    }

    public boolean hasFacility() { 
      return this.facility != null && !this.facility.isEmpty();
    }

    /**
     * @param value {@link #facility} (Facility where the services were provided.)
     */
    public EligibilityRequest setFacility(Type value) { 
      this.facility = value;
      return this;
    }

    /**
     * @return {@link #patient} (Patient Resource.)
     */
    public Type getPatient() { 
      return this.patient;
    }

    /**
     * @return {@link #patient} (Patient Resource.)
     */
    public Identifier getPatientIdentifier() throws FHIRException { 
      if (!(this.patient instanceof Identifier))
        throw new FHIRException("Type mismatch: the type Identifier was expected, but "+this.patient.getClass().getName()+" was encountered");
      return (Identifier) this.patient;
    }

    public boolean hasPatientIdentifier() { 
      return this.patient instanceof Identifier;
    }

    /**
     * @return {@link #patient} (Patient Resource.)
     */
    public Reference getPatientReference() throws FHIRException { 
      if (!(this.patient instanceof Reference))
        throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.patient.getClass().getName()+" was encountered");
      return (Reference) this.patient;
    }

    public boolean hasPatientReference() { 
      return this.patient instanceof Reference;
    }

    public boolean hasPatient() { 
      return this.patient != null && !this.patient.isEmpty();
    }

    /**
     * @param value {@link #patient} (Patient Resource.)
     */
    public EligibilityRequest setPatient(Type value) { 
      this.patient = value;
      return this;
    }

    /**
     * @return {@link #coverage} (Financial instrument by which payment information for health care.)
     */
    public Type getCoverage() { 
      return this.coverage;
    }

    /**
     * @return {@link #coverage} (Financial instrument by which payment information for health care.)
     */
    public Identifier getCoverageIdentifier() throws FHIRException { 
      if (!(this.coverage instanceof Identifier))
        throw new FHIRException("Type mismatch: the type Identifier was expected, but "+this.coverage.getClass().getName()+" was encountered");
      return (Identifier) this.coverage;
    }

    public boolean hasCoverageIdentifier() { 
      return this.coverage instanceof Identifier;
    }

    /**
     * @return {@link #coverage} (Financial instrument by which payment information for health care.)
     */
    public Reference getCoverageReference() throws FHIRException { 
      if (!(this.coverage instanceof Reference))
        throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.coverage.getClass().getName()+" was encountered");
      return (Reference) this.coverage;
    }

    public boolean hasCoverageReference() { 
      return this.coverage instanceof Reference;
    }

    public boolean hasCoverage() { 
      return this.coverage != null && !this.coverage.isEmpty();
    }

    /**
     * @param value {@link #coverage} (Financial instrument by which payment information for health care.)
     */
    public EligibilityRequest setCoverage(Type value) { 
      this.coverage = value;
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
     * @return {@link #benefitCategory} (Dental, Vision, Medical, Pharmacy, Rehab etc.)
     */
    public Coding getBenefitCategory() { 
      if (this.benefitCategory == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EligibilityRequest.benefitCategory");
        else if (Configuration.doAutoCreate())
          this.benefitCategory = new Coding(); // cc
      return this.benefitCategory;
    }

    public boolean hasBenefitCategory() { 
      return this.benefitCategory != null && !this.benefitCategory.isEmpty();
    }

    /**
     * @param value {@link #benefitCategory} (Dental, Vision, Medical, Pharmacy, Rehab etc.)
     */
    public EligibilityRequest setBenefitCategory(Coding value) { 
      this.benefitCategory = value;
      return this;
    }

    /**
     * @return {@link #benefitSubCategory} (Dental: basic, major, ortho; Vision exam, glasses, contacts; etc.)
     */
    public Coding getBenefitSubCategory() { 
      if (this.benefitSubCategory == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EligibilityRequest.benefitSubCategory");
        else if (Configuration.doAutoCreate())
          this.benefitSubCategory = new Coding(); // cc
      return this.benefitSubCategory;
    }

    public boolean hasBenefitSubCategory() { 
      return this.benefitSubCategory != null && !this.benefitSubCategory.isEmpty();
    }

    /**
     * @param value {@link #benefitSubCategory} (Dental: basic, major, ortho; Vision exam, glasses, contacts; etc.)
     */
    public EligibilityRequest setBenefitSubCategory(Coding value) { 
      this.benefitSubCategory = value;
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "The Response business identifier.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("ruleset", "Coding", "The version of the style of resource contents. This should be mapped to the allowable profiles for this and supporting resources.", 0, java.lang.Integer.MAX_VALUE, ruleset));
        childrenList.add(new Property("originalRuleset", "Coding", "The style (standard) and version of the original material which was converted into this resource.", 0, java.lang.Integer.MAX_VALUE, originalRuleset));
        childrenList.add(new Property("created", "dateTime", "The date when this resource was created.", 0, java.lang.Integer.MAX_VALUE, created));
        childrenList.add(new Property("target[x]", "Identifier|Reference(Organization)", "The Insurer who is target  of the request.", 0, java.lang.Integer.MAX_VALUE, target));
        childrenList.add(new Property("provider[x]", "Identifier|Reference(Practitioner)", "The practitioner who is responsible for the services rendered to the patient.", 0, java.lang.Integer.MAX_VALUE, provider));
        childrenList.add(new Property("organization[x]", "Identifier|Reference(Organization)", "The organization which is responsible for the services rendered to the patient.", 0, java.lang.Integer.MAX_VALUE, organization));
        childrenList.add(new Property("priority", "Coding", "Immediate (STAT), best effort (NORMAL), deferred (DEFER).", 0, java.lang.Integer.MAX_VALUE, priority));
        childrenList.add(new Property("enterer[x]", "Identifier|Reference(Practitioner)", "Person who created the invoice/claim/pre-determination or pre-authorization.", 0, java.lang.Integer.MAX_VALUE, enterer));
        childrenList.add(new Property("facility[x]", "Identifier|Reference(Location)", "Facility where the services were provided.", 0, java.lang.Integer.MAX_VALUE, facility));
        childrenList.add(new Property("patient[x]", "Identifier|Reference(Patient)", "Patient Resource.", 0, java.lang.Integer.MAX_VALUE, patient));
        childrenList.add(new Property("coverage[x]", "Identifier|Reference(Coverage)", "Financial instrument by which payment information for health care.", 0, java.lang.Integer.MAX_VALUE, coverage));
        childrenList.add(new Property("businessArrangement", "string", "The contract number of a business agreement which describes the terms and conditions.", 0, java.lang.Integer.MAX_VALUE, businessArrangement));
        childrenList.add(new Property("serviced[x]", "date|Period", "The date or dates when the enclosed suite of services were performed or completed.", 0, java.lang.Integer.MAX_VALUE, serviced));
        childrenList.add(new Property("benefitCategory", "Coding", "Dental, Vision, Medical, Pharmacy, Rehab etc.", 0, java.lang.Integer.MAX_VALUE, benefitCategory));
        childrenList.add(new Property("benefitSubCategory", "Coding", "Dental: basic, major, ortho; Vision exam, glasses, contacts; etc.", 0, java.lang.Integer.MAX_VALUE, benefitSubCategory));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case 1548678118: /*ruleset*/ return this.ruleset == null ? new Base[0] : new Base[] {this.ruleset}; // Coding
        case 1089373397: /*originalRuleset*/ return this.originalRuleset == null ? new Base[0] : new Base[] {this.originalRuleset}; // Coding
        case 1028554472: /*created*/ return this.created == null ? new Base[0] : new Base[] {this.created}; // DateTimeType
        case -880905839: /*target*/ return this.target == null ? new Base[0] : new Base[] {this.target}; // Type
        case -987494927: /*provider*/ return this.provider == null ? new Base[0] : new Base[] {this.provider}; // Type
        case 1178922291: /*organization*/ return this.organization == null ? new Base[0] : new Base[] {this.organization}; // Type
        case -1165461084: /*priority*/ return this.priority == null ? new Base[0] : new Base[] {this.priority}; // Coding
        case -1591951995: /*enterer*/ return this.enterer == null ? new Base[0] : new Base[] {this.enterer}; // Type
        case 501116579: /*facility*/ return this.facility == null ? new Base[0] : new Base[] {this.facility}; // Type
        case -791418107: /*patient*/ return this.patient == null ? new Base[0] : new Base[] {this.patient}; // Type
        case -351767064: /*coverage*/ return this.coverage == null ? new Base[0] : new Base[] {this.coverage}; // Type
        case 259920682: /*businessArrangement*/ return this.businessArrangement == null ? new Base[0] : new Base[] {this.businessArrangement}; // StringType
        case 1379209295: /*serviced*/ return this.serviced == null ? new Base[0] : new Base[] {this.serviced}; // Type
        case -1023390027: /*benefitCategory*/ return this.benefitCategory == null ? new Base[0] : new Base[] {this.benefitCategory}; // Coding
        case 1987878471: /*benefitSubCategory*/ return this.benefitSubCategory == null ? new Base[0] : new Base[] {this.benefitSubCategory}; // Coding
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.getIdentifier().add(castToIdentifier(value)); // Identifier
          break;
        case 1548678118: // ruleset
          this.ruleset = castToCoding(value); // Coding
          break;
        case 1089373397: // originalRuleset
          this.originalRuleset = castToCoding(value); // Coding
          break;
        case 1028554472: // created
          this.created = castToDateTime(value); // DateTimeType
          break;
        case -880905839: // target
          this.target = (Type) value; // Type
          break;
        case -987494927: // provider
          this.provider = (Type) value; // Type
          break;
        case 1178922291: // organization
          this.organization = (Type) value; // Type
          break;
        case -1165461084: // priority
          this.priority = castToCoding(value); // Coding
          break;
        case -1591951995: // enterer
          this.enterer = (Type) value; // Type
          break;
        case 501116579: // facility
          this.facility = (Type) value; // Type
          break;
        case -791418107: // patient
          this.patient = (Type) value; // Type
          break;
        case -351767064: // coverage
          this.coverage = (Type) value; // Type
          break;
        case 259920682: // businessArrangement
          this.businessArrangement = castToString(value); // StringType
          break;
        case 1379209295: // serviced
          this.serviced = (Type) value; // Type
          break;
        case -1023390027: // benefitCategory
          this.benefitCategory = castToCoding(value); // Coding
          break;
        case 1987878471: // benefitSubCategory
          this.benefitSubCategory = castToCoding(value); // Coding
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier"))
          this.getIdentifier().add(castToIdentifier(value));
        else if (name.equals("ruleset"))
          this.ruleset = castToCoding(value); // Coding
        else if (name.equals("originalRuleset"))
          this.originalRuleset = castToCoding(value); // Coding
        else if (name.equals("created"))
          this.created = castToDateTime(value); // DateTimeType
        else if (name.equals("target[x]"))
          this.target = (Type) value; // Type
        else if (name.equals("provider[x]"))
          this.provider = (Type) value; // Type
        else if (name.equals("organization[x]"))
          this.organization = (Type) value; // Type
        else if (name.equals("priority"))
          this.priority = castToCoding(value); // Coding
        else if (name.equals("enterer[x]"))
          this.enterer = (Type) value; // Type
        else if (name.equals("facility[x]"))
          this.facility = (Type) value; // Type
        else if (name.equals("patient[x]"))
          this.patient = (Type) value; // Type
        else if (name.equals("coverage[x]"))
          this.coverage = (Type) value; // Type
        else if (name.equals("businessArrangement"))
          this.businessArrangement = castToString(value); // StringType
        else if (name.equals("serviced[x]"))
          this.serviced = (Type) value; // Type
        else if (name.equals("benefitCategory"))
          this.benefitCategory = castToCoding(value); // Coding
        else if (name.equals("benefitSubCategory"))
          this.benefitSubCategory = castToCoding(value); // Coding
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return addIdentifier(); // Identifier
        case 1548678118:  return getRuleset(); // Coding
        case 1089373397:  return getOriginalRuleset(); // Coding
        case 1028554472: throw new FHIRException("Cannot make property created as it is not a complex type"); // DateTimeType
        case -815579825:  return getTarget(); // Type
        case 2064698607:  return getProvider(); // Type
        case 1326483053:  return getOrganization(); // Type
        case -1165461084:  return getPriority(); // Coding
        case -812909349:  return getEnterer(); // Type
        case -542224643:  return getFacility(); // Type
        case -2061246629:  return getPatient(); // Type
        case 227689880:  return getCoverage(); // Type
        case 259920682: throw new FHIRException("Cannot make property businessArrangement as it is not a complex type"); // StringType
        case -1927922223:  return getServiced(); // Type
        case -1023390027:  return getBenefitCategory(); // Coding
        case 1987878471:  return getBenefitSubCategory(); // Coding
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("ruleset")) {
          this.ruleset = new Coding();
          return this.ruleset;
        }
        else if (name.equals("originalRuleset")) {
          this.originalRuleset = new Coding();
          return this.originalRuleset;
        }
        else if (name.equals("created")) {
          throw new FHIRException("Cannot call addChild on a primitive type EligibilityRequest.created");
        }
        else if (name.equals("targetIdentifier")) {
          this.target = new Identifier();
          return this.target;
        }
        else if (name.equals("targetReference")) {
          this.target = new Reference();
          return this.target;
        }
        else if (name.equals("providerIdentifier")) {
          this.provider = new Identifier();
          return this.provider;
        }
        else if (name.equals("providerReference")) {
          this.provider = new Reference();
          return this.provider;
        }
        else if (name.equals("organizationIdentifier")) {
          this.organization = new Identifier();
          return this.organization;
        }
        else if (name.equals("organizationReference")) {
          this.organization = new Reference();
          return this.organization;
        }
        else if (name.equals("priority")) {
          this.priority = new Coding();
          return this.priority;
        }
        else if (name.equals("entererIdentifier")) {
          this.enterer = new Identifier();
          return this.enterer;
        }
        else if (name.equals("entererReference")) {
          this.enterer = new Reference();
          return this.enterer;
        }
        else if (name.equals("facilityIdentifier")) {
          this.facility = new Identifier();
          return this.facility;
        }
        else if (name.equals("facilityReference")) {
          this.facility = new Reference();
          return this.facility;
        }
        else if (name.equals("patientIdentifier")) {
          this.patient = new Identifier();
          return this.patient;
        }
        else if (name.equals("patientReference")) {
          this.patient = new Reference();
          return this.patient;
        }
        else if (name.equals("coverageIdentifier")) {
          this.coverage = new Identifier();
          return this.coverage;
        }
        else if (name.equals("coverageReference")) {
          this.coverage = new Reference();
          return this.coverage;
        }
        else if (name.equals("businessArrangement")) {
          throw new FHIRException("Cannot call addChild on a primitive type EligibilityRequest.businessArrangement");
        }
        else if (name.equals("servicedDate")) {
          this.serviced = new DateType();
          return this.serviced;
        }
        else if (name.equals("servicedPeriod")) {
          this.serviced = new Period();
          return this.serviced;
        }
        else if (name.equals("benefitCategory")) {
          this.benefitCategory = new Coding();
          return this.benefitCategory;
        }
        else if (name.equals("benefitSubCategory")) {
          this.benefitSubCategory = new Coding();
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
        dst.ruleset = ruleset == null ? null : ruleset.copy();
        dst.originalRuleset = originalRuleset == null ? null : originalRuleset.copy();
        dst.created = created == null ? null : created.copy();
        dst.target = target == null ? null : target.copy();
        dst.provider = provider == null ? null : provider.copy();
        dst.organization = organization == null ? null : organization.copy();
        dst.priority = priority == null ? null : priority.copy();
        dst.enterer = enterer == null ? null : enterer.copy();
        dst.facility = facility == null ? null : facility.copy();
        dst.patient = patient == null ? null : patient.copy();
        dst.coverage = coverage == null ? null : coverage.copy();
        dst.businessArrangement = businessArrangement == null ? null : businessArrangement.copy();
        dst.serviced = serviced == null ? null : serviced.copy();
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
        return compareDeep(identifier, o.identifier, true) && compareDeep(ruleset, o.ruleset, true) && compareDeep(originalRuleset, o.originalRuleset, true)
           && compareDeep(created, o.created, true) && compareDeep(target, o.target, true) && compareDeep(provider, o.provider, true)
           && compareDeep(organization, o.organization, true) && compareDeep(priority, o.priority, true) && compareDeep(enterer, o.enterer, true)
           && compareDeep(facility, o.facility, true) && compareDeep(patient, o.patient, true) && compareDeep(coverage, o.coverage, true)
           && compareDeep(businessArrangement, o.businessArrangement, true) && compareDeep(serviced, o.serviced, true)
           && compareDeep(benefitCategory, o.benefitCategory, true) && compareDeep(benefitSubCategory, o.benefitSubCategory, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof EligibilityRequest))
          return false;
        EligibilityRequest o = (EligibilityRequest) other;
        return compareValues(created, o.created, true) && compareValues(businessArrangement, o.businessArrangement, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (ruleset == null || ruleset.isEmpty())
           && (originalRuleset == null || originalRuleset.isEmpty()) && (created == null || created.isEmpty())
           && (target == null || target.isEmpty()) && (provider == null || provider.isEmpty()) && (organization == null || organization.isEmpty())
           && (priority == null || priority.isEmpty()) && (enterer == null || enterer.isEmpty()) && (facility == null || facility.isEmpty())
           && (patient == null || patient.isEmpty()) && (coverage == null || coverage.isEmpty()) && (businessArrangement == null || businessArrangement.isEmpty())
           && (serviced == null || serviced.isEmpty()) && (benefitCategory == null || benefitCategory.isEmpty())
           && (benefitSubCategory == null || benefitSubCategory.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.EligibilityRequest;
   }

 /**
   * Search parameter: <b>patientidentifier</b>
   * <p>
   * Description: <b>The reference to the patient</b><br>
   * Type: <b>token</b><br>
   * Path: <b>EligibilityRequest.patientIdentifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patientidentifier", path="EligibilityRequest.patient.as(Identifier)", description="The reference to the patient", type="token" )
  public static final String SP_PATIENTIDENTIFIER = "patientidentifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patientidentifier</b>
   * <p>
   * Description: <b>The reference to the patient</b><br>
   * Type: <b>token</b><br>
   * Path: <b>EligibilityRequest.patientIdentifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam PATIENTIDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_PATIENTIDENTIFIER);

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
   * Search parameter: <b>facilityidentifier</b>
   * <p>
   * Description: <b>Facility responsible for the goods and services</b><br>
   * Type: <b>token</b><br>
   * Path: <b>EligibilityRequest.facilityidentifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="facilityidentifier", path="EligibilityRequest.facility.as(identifier)", description="Facility responsible for the goods and services", type="token" )
  public static final String SP_FACILITYIDENTIFIER = "facilityidentifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>facilityidentifier</b>
   * <p>
   * Description: <b>Facility responsible for the goods and services</b><br>
   * Type: <b>token</b><br>
   * Path: <b>EligibilityRequest.facilityidentifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam FACILITYIDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_FACILITYIDENTIFIER);

 /**
   * Search parameter: <b>facilityreference</b>
   * <p>
   * Description: <b>Facility responsible for the goods and services</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>EligibilityRequest.facilityReference</b><br>
   * </p>
   */
  @SearchParamDefinition(name="facilityreference", path="EligibilityRequest.facility.as(Reference)", description="Facility responsible for the goods and services", type="reference" )
  public static final String SP_FACILITYREFERENCE = "facilityreference";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>facilityreference</b>
   * <p>
   * Description: <b>Facility responsible for the goods and services</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>EligibilityRequest.facilityReference</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam FACILITYREFERENCE = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_FACILITYREFERENCE);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>EligibilityRequest:facilityreference</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_FACILITYREFERENCE = new ca.uhn.fhir.model.api.Include("EligibilityRequest:facilityreference").toLocked();

 /**
   * Search parameter: <b>providerreference</b>
   * <p>
   * Description: <b>The reference to the provider</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>EligibilityRequest.providerReference</b><br>
   * </p>
   */
  @SearchParamDefinition(name="providerreference", path="EligibilityRequest.provider.as(Reference)", description="The reference to the provider", type="reference" )
  public static final String SP_PROVIDERREFERENCE = "providerreference";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>providerreference</b>
   * <p>
   * Description: <b>The reference to the provider</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>EligibilityRequest.providerReference</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PROVIDERREFERENCE = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PROVIDERREFERENCE);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>EligibilityRequest:providerreference</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PROVIDERREFERENCE = new ca.uhn.fhir.model.api.Include("EligibilityRequest:providerreference").toLocked();

 /**
   * Search parameter: <b>organizationidentifier</b>
   * <p>
   * Description: <b>The reference to the providing organization</b><br>
   * Type: <b>token</b><br>
   * Path: <b>EligibilityRequest.organizationidentifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="organizationidentifier", path="EligibilityRequest.organization.as(identifier)", description="The reference to the providing organization", type="token" )
  public static final String SP_ORGANIZATIONIDENTIFIER = "organizationidentifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>organizationidentifier</b>
   * <p>
   * Description: <b>The reference to the providing organization</b><br>
   * Type: <b>token</b><br>
   * Path: <b>EligibilityRequest.organizationidentifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam ORGANIZATIONIDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_ORGANIZATIONIDENTIFIER);

 /**
   * Search parameter: <b>organizationreference</b>
   * <p>
   * Description: <b>The reference to the providing organization</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>EligibilityRequest.organizationReference</b><br>
   * </p>
   */
  @SearchParamDefinition(name="organizationreference", path="EligibilityRequest.organization.as(Reference)", description="The reference to the providing organization", type="reference" )
  public static final String SP_ORGANIZATIONREFERENCE = "organizationreference";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>organizationreference</b>
   * <p>
   * Description: <b>The reference to the providing organization</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>EligibilityRequest.organizationReference</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam ORGANIZATIONREFERENCE = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_ORGANIZATIONREFERENCE);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>EligibilityRequest:organizationreference</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_ORGANIZATIONREFERENCE = new ca.uhn.fhir.model.api.Include("EligibilityRequest:organizationreference").toLocked();

 /**
   * Search parameter: <b>patientreference</b>
   * <p>
   * Description: <b>The reference to the patient</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>EligibilityRequest.patientReference</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patientreference", path="EligibilityRequest.patient.as(Reference)", description="The reference to the patient", type="reference" )
  public static final String SP_PATIENTREFERENCE = "patientreference";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patientreference</b>
   * <p>
   * Description: <b>The reference to the patient</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>EligibilityRequest.patientReference</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PATIENTREFERENCE = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PATIENTREFERENCE);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>EligibilityRequest:patientreference</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PATIENTREFERENCE = new ca.uhn.fhir.model.api.Include("EligibilityRequest:patientreference").toLocked();

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
   * Search parameter: <b>provideridentifier</b>
   * <p>
   * Description: <b>The reference to the provider</b><br>
   * Type: <b>token</b><br>
   * Path: <b>EligibilityRequest.provideridentifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="provideridentifier", path="EligibilityRequest.provider.as(identifier)", description="The reference to the provider", type="token" )
  public static final String SP_PROVIDERIDENTIFIER = "provideridentifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>provideridentifier</b>
   * <p>
   * Description: <b>The reference to the provider</b><br>
   * Type: <b>token</b><br>
   * Path: <b>EligibilityRequest.provideridentifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam PROVIDERIDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_PROVIDERIDENTIFIER);


}

