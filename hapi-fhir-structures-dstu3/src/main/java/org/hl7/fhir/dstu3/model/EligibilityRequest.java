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
import org.hl7.fhir.dstu3.model.Enumerations.*;
import org.hl7.fhir.instance.model.api.*;
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
    @Child(name = "target", type = {Organization.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Insurer", formalDefinition="The Insurer who is target  of the request." )
    protected Reference target;

    /**
     * The actual object that is the target of the reference (The Insurer who is target  of the request.)
     */
    protected Organization targetTarget;

    /**
     * The practitioner who is responsible for the services rendered to the patient.
     */
    @Child(name = "provider", type = {Practitioner.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Responsible practitioner", formalDefinition="The practitioner who is responsible for the services rendered to the patient." )
    protected Reference provider;

    /**
     * The actual object that is the target of the reference (The practitioner who is responsible for the services rendered to the patient.)
     */
    protected Practitioner providerTarget;

    /**
     * The organization which is responsible for the services rendered to the patient.
     */
    @Child(name = "organization", type = {Organization.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Responsible organization", formalDefinition="The organization which is responsible for the services rendered to the patient." )
    protected Reference organization;

    /**
     * The actual object that is the target of the reference (The organization which is responsible for the services rendered to the patient.)
     */
    protected Organization organizationTarget;

    /**
     * Immediate (STAT), best effort (NORMAL), deferred (DEFER).
     */
    @Child(name = "priority", type = {Coding.class}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Desired processing priority", formalDefinition="Immediate (STAT), best effort (NORMAL), deferred (DEFER)." )
    protected Coding priority;

    /**
     * Person who created the invoice/claim/pre-determination or pre-authorization.
     */
    @Child(name = "enterer", type = {Practitioner.class}, order=8, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Author", formalDefinition="Person who created the invoice/claim/pre-determination or pre-authorization." )
    protected Reference enterer;

    /**
     * The actual object that is the target of the reference (Person who created the invoice/claim/pre-determination or pre-authorization.)
     */
    protected Practitioner entererTarget;

    /**
     * Facility where the services were provided.
     */
    @Child(name = "facility", type = {Location.class}, order=9, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Servicing Facility", formalDefinition="Facility where the services were provided." )
    protected Reference facility;

    /**
     * The actual object that is the target of the reference (Facility where the services were provided.)
     */
    protected Location facilityTarget;

    /**
     * Patient Resource.
     */
    @Child(name = "patient", type = {Patient.class}, order=10, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The subject of the Products and Services", formalDefinition="Patient Resource." )
    protected Reference patient;

    /**
     * The actual object that is the target of the reference (Patient Resource.)
     */
    protected Patient patientTarget;

    /**
     * Financial instrument by which payment information for health care.
     */
    @Child(name = "coverage", type = {Coverage.class}, order=11, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Insurance or medical plan", formalDefinition="Financial instrument by which payment information for health care." )
    protected Reference coverage;

    /**
     * The actual object that is the target of the reference (Financial instrument by which payment information for health care.)
     */
    protected Coverage coverageTarget;

    /**
     * The contract number of a business agreement which describes the terms and conditions.
     */
    @Child(name = "businessArrangement", type = {StringType.class}, order=12, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Business agreement", formalDefinition="The contract number of a business agreement which describes the terms and conditions." )
    protected StringType businessArrangement;

    /**
     * The relationship of the patient to the subscriber.
     */
    @Child(name = "relationship", type = {Coding.class}, order=13, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Patient relationship to subscriber", formalDefinition="The relationship of the patient to the subscriber." )
    protected Coding relationship;

    /**
     * The date or dates when the enclosed suite of services were performed or completed.
     */
    @Child(name = "serviced", type = {DateType.class, Period.class}, order=14, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Estimated date or dates of Service", formalDefinition="The date or dates when the enclosed suite of services were performed or completed." )
    protected Type serviced;

    /**
     * Dental, Vision, Medical, Pharmacy, Rehab etc.
     */
    @Child(name = "benefitCategory", type = {Coding.class}, order=15, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Benefit Category", formalDefinition="Dental, Vision, Medical, Pharmacy, Rehab etc." )
    protected Coding benefitCategory;

    /**
     * Dental: basic, major, ortho; Vision exam, glasses, contacts; etc.
     */
    @Child(name = "benefitSubCategory", type = {Coding.class}, order=16, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Benefit SubCategory", formalDefinition="Dental: basic, major, ortho; Vision exam, glasses, contacts; etc." )
    protected Coding benefitSubCategory;

    private static final long serialVersionUID = 1808061326L;

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
    public Reference getTarget() { 
      if (this.target == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EligibilityRequest.target");
        else if (Configuration.doAutoCreate())
          this.target = new Reference(); // cc
      return this.target;
    }

    public boolean hasTarget() { 
      return this.target != null && !this.target.isEmpty();
    }

    /**
     * @param value {@link #target} (The Insurer who is target  of the request.)
     */
    public EligibilityRequest setTarget(Reference value) { 
      this.target = value;
      return this;
    }

    /**
     * @return {@link #target} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The Insurer who is target  of the request.)
     */
    public Organization getTargetTarget() { 
      if (this.targetTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EligibilityRequest.target");
        else if (Configuration.doAutoCreate())
          this.targetTarget = new Organization(); // aa
      return this.targetTarget;
    }

    /**
     * @param value {@link #target} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The Insurer who is target  of the request.)
     */
    public EligibilityRequest setTargetTarget(Organization value) { 
      this.targetTarget = value;
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
     * @return {@link #relationship} (The relationship of the patient to the subscriber.)
     */
    public Coding getRelationship() { 
      if (this.relationship == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EligibilityRequest.relationship");
        else if (Configuration.doAutoCreate())
          this.relationship = new Coding(); // cc
      return this.relationship;
    }

    public boolean hasRelationship() { 
      return this.relationship != null && !this.relationship.isEmpty();
    }

    /**
     * @param value {@link #relationship} (The relationship of the patient to the subscriber.)
     */
    public EligibilityRequest setRelationship(Coding value) { 
      this.relationship = value;
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
        childrenList.add(new Property("target", "Reference(Organization)", "The Insurer who is target  of the request.", 0, java.lang.Integer.MAX_VALUE, target));
        childrenList.add(new Property("provider", "Reference(Practitioner)", "The practitioner who is responsible for the services rendered to the patient.", 0, java.lang.Integer.MAX_VALUE, provider));
        childrenList.add(new Property("organization", "Reference(Organization)", "The organization which is responsible for the services rendered to the patient.", 0, java.lang.Integer.MAX_VALUE, organization));
        childrenList.add(new Property("priority", "Coding", "Immediate (STAT), best effort (NORMAL), deferred (DEFER).", 0, java.lang.Integer.MAX_VALUE, priority));
        childrenList.add(new Property("enterer", "Reference(Practitioner)", "Person who created the invoice/claim/pre-determination or pre-authorization.", 0, java.lang.Integer.MAX_VALUE, enterer));
        childrenList.add(new Property("facility", "Reference(Location)", "Facility where the services were provided.", 0, java.lang.Integer.MAX_VALUE, facility));
        childrenList.add(new Property("patient", "Reference(Patient)", "Patient Resource.", 0, java.lang.Integer.MAX_VALUE, patient));
        childrenList.add(new Property("coverage", "Reference(Coverage)", "Financial instrument by which payment information for health care.", 0, java.lang.Integer.MAX_VALUE, coverage));
        childrenList.add(new Property("businessArrangement", "string", "The contract number of a business agreement which describes the terms and conditions.", 0, java.lang.Integer.MAX_VALUE, businessArrangement));
        childrenList.add(new Property("relationship", "Coding", "The relationship of the patient to the subscriber.", 0, java.lang.Integer.MAX_VALUE, relationship));
        childrenList.add(new Property("serviced[x]", "date|Period", "The date or dates when the enclosed suite of services were performed or completed.", 0, java.lang.Integer.MAX_VALUE, serviced));
        childrenList.add(new Property("benefitCategory", "Coding", "Dental, Vision, Medical, Pharmacy, Rehab etc.", 0, java.lang.Integer.MAX_VALUE, benefitCategory));
        childrenList.add(new Property("benefitSubCategory", "Coding", "Dental: basic, major, ortho; Vision exam, glasses, contacts; etc.", 0, java.lang.Integer.MAX_VALUE, benefitSubCategory));
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
        else if (name.equals("target"))
          this.target = castToReference(value); // Reference
        else if (name.equals("provider"))
          this.provider = castToReference(value); // Reference
        else if (name.equals("organization"))
          this.organization = castToReference(value); // Reference
        else if (name.equals("priority"))
          this.priority = castToCoding(value); // Coding
        else if (name.equals("enterer"))
          this.enterer = castToReference(value); // Reference
        else if (name.equals("facility"))
          this.facility = castToReference(value); // Reference
        else if (name.equals("patient"))
          this.patient = castToReference(value); // Reference
        else if (name.equals("coverage"))
          this.coverage = castToReference(value); // Reference
        else if (name.equals("businessArrangement"))
          this.businessArrangement = castToString(value); // StringType
        else if (name.equals("relationship"))
          this.relationship = castToCoding(value); // Coding
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
        else if (name.equals("target")) {
          this.target = new Reference();
          return this.target;
        }
        else if (name.equals("provider")) {
          this.provider = new Reference();
          return this.provider;
        }
        else if (name.equals("organization")) {
          this.organization = new Reference();
          return this.organization;
        }
        else if (name.equals("priority")) {
          this.priority = new Coding();
          return this.priority;
        }
        else if (name.equals("enterer")) {
          this.enterer = new Reference();
          return this.enterer;
        }
        else if (name.equals("facility")) {
          this.facility = new Reference();
          return this.facility;
        }
        else if (name.equals("patient")) {
          this.patient = new Reference();
          return this.patient;
        }
        else if (name.equals("coverage")) {
          this.coverage = new Reference();
          return this.coverage;
        }
        else if (name.equals("businessArrangement")) {
          throw new FHIRException("Cannot call addChild on a primitive type EligibilityRequest.businessArrangement");
        }
        else if (name.equals("relationship")) {
          this.relationship = new Coding();
          return this.relationship;
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
        dst.relationship = relationship == null ? null : relationship.copy();
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
           && compareDeep(businessArrangement, o.businessArrangement, true) && compareDeep(relationship, o.relationship, true)
           && compareDeep(serviced, o.serviced, true) && compareDeep(benefitCategory, o.benefitCategory, true)
           && compareDeep(benefitSubCategory, o.benefitSubCategory, true);
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
           && (relationship == null || relationship.isEmpty()) && (serviced == null || serviced.isEmpty())
           && (benefitCategory == null || benefitCategory.isEmpty()) && (benefitSubCategory == null || benefitSubCategory.isEmpty())
          ;
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
  @SearchParamDefinition(name="provider", path="EligibilityRequest.provider", description="The reference to the provider", type="reference" )
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
  @SearchParamDefinition(name="patient", path="EligibilityRequest.patient", description="The reference to the patient", type="reference" )
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
  @SearchParamDefinition(name="organization", path="EligibilityRequest.organization", description="The reference to the providing organization", type="reference" )
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
   * Search parameter: <b>facility</b>
   * <p>
   * Description: <b>Facility responsible for the goods and services</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>EligibilityRequest.facility</b><br>
   * </p>
   */
  @SearchParamDefinition(name="facility", path="EligibilityRequest.facility", description="Facility responsible for the goods and services", type="reference" )
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

