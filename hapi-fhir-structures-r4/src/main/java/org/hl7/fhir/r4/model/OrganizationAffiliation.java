package org.hl7.fhir.r4.model;

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

// Generated on Thu, Sep 13, 2018 09:04-0400 for FHIR v3.5.0

import java.util.*;

import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ChildOrder;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Block;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.exceptions.FHIRException;
/**
 * Defines an affiliation/assotiation/relationship between 2 distinct oganizations, that is not a part-of relationship/sub-division relationship.
 */
@ResourceDef(name="OrganizationAffiliation", profile="http://hl7.org/fhir/Profile/OrganizationAffiliation")
public class OrganizationAffiliation extends DomainResource {

    /**
     * Business identifiers that are specific to this role.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Business identifiers that are specific to this role", formalDefinition="Business identifiers that are specific to this role." )
    protected List<Identifier> identifier;

    /**
     * Whether this organization affiliation record is in active use.
     */
    @Child(name = "active", type = {BooleanType.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Whether this organization affiliation record is in active use", formalDefinition="Whether this organization affiliation record is in active use." )
    protected BooleanType active;

    /**
     * The period during which the participatingOrganization is affiliated with the primary organization.
     */
    @Child(name = "period", type = {Period.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The period during which the participatingOrganization is affiliated with the primary organization", formalDefinition="The period during which the participatingOrganization is affiliated with the primary organization." )
    protected Period period;

    /**
     * Organization where the role is available (primary organization/has members).
     */
    @Child(name = "organization", type = {Organization.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Organization where the role is available", formalDefinition="Organization where the role is available (primary organization/has members)." )
    protected Reference organization;

    /**
     * The actual object that is the target of the reference (Organization where the role is available (primary organization/has members).)
     */
    protected Organization organizationTarget;

    /**
     * The Participating Organization provides/performs the role(s) defined by the code to the Primary Organization (e.g. providing services or is a member of).
     */
    @Child(name = "participatingOrganization", type = {Organization.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Organization that provides/performs the role (e.g. providing services or is a member of)", formalDefinition="The Participating Organization provides/performs the role(s) defined by the code to the Primary Organization (e.g. providing services or is a member of)." )
    protected Reference participatingOrganization;

    /**
     * The actual object that is the target of the reference (The Participating Organization provides/performs the role(s) defined by the code to the Primary Organization (e.g. providing services or is a member of).)
     */
    protected Organization participatingOrganizationTarget;

    /**
     * Health insurance provider network in which the participatingOrganization provides the role's services (if defined) at the indicated locations (if defined).
     */
    @Child(name = "network", type = {Organization.class}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Health insurance provider network in which the participatingOrganization provides the role's services (if defined) at the indicated locations (if defined)", formalDefinition="Health insurance provider network in which the participatingOrganization provides the role's services (if defined) at the indicated locations (if defined)." )
    protected List<Reference> network;
    /**
     * The actual objects that are the target of the reference (Health insurance provider network in which the participatingOrganization provides the role's services (if defined) at the indicated locations (if defined).)
     */
    protected List<Organization> networkTarget;


    /**
     * Definition of the role the participatingOrganization plays in the association.
     */
    @Child(name = "code", type = {CodeableConcept.class}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Definition of the role the participatingOrganization plays", formalDefinition="Definition of the role the participatingOrganization plays in the association." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/organization-role")
    protected List<CodeableConcept> code;

    /**
     * Specific specialty of the participatingOrganization in the context of the role.
     */
    @Child(name = "specialty", type = {CodeableConcept.class}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Specific specialty of the participatingOrganization in the context of the role", formalDefinition="Specific specialty of the participatingOrganization in the context of the role." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/c80-practice-codes")
    protected List<CodeableConcept> specialty;

    /**
     * The location(s) at which the role occurs.
     */
    @Child(name = "location", type = {Location.class}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="The location(s) at which the role occurs", formalDefinition="The location(s) at which the role occurs." )
    protected List<Reference> location;
    /**
     * The actual objects that are the target of the reference (The location(s) at which the role occurs.)
     */
    protected List<Location> locationTarget;


    /**
     * Healthcare services provided through the role.
     */
    @Child(name = "healthcareService", type = {HealthcareService.class}, order=9, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Healthcare services provided through the role", formalDefinition="Healthcare services provided through the role." )
    protected List<Reference> healthcareService;
    /**
     * The actual objects that are the target of the reference (Healthcare services provided through the role.)
     */
    protected List<HealthcareService> healthcareServiceTarget;


    /**
     * Contact details at the participatingOrganization relevant to this Affiliation.
     */
    @Child(name = "telecom", type = {ContactPoint.class}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Contact details at the participatingOrganization relevant to this Affiliation", formalDefinition="Contact details at the participatingOrganization relevant to this Affiliation." )
    protected List<ContactPoint> telecom;

    /**
     * Technical endpoints providing access to services operated for this role.
     */
    @Child(name = "endpoint", type = {Endpoint.class}, order=11, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Technical endpoints providing access to services operated for this role", formalDefinition="Technical endpoints providing access to services operated for this role." )
    protected List<Reference> endpoint;
    /**
     * The actual objects that are the target of the reference (Technical endpoints providing access to services operated for this role.)
     */
    protected List<Endpoint> endpointTarget;


    private static final long serialVersionUID = -62510821L;

  /**
   * Constructor
   */
    public OrganizationAffiliation() {
      super();
    }

    /**
     * @return {@link #identifier} (Business identifiers that are specific to this role.)
     */
    public List<Identifier> getIdentifier() { 
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      return this.identifier;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public OrganizationAffiliation setIdentifier(List<Identifier> theIdentifier) { 
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

    public OrganizationAffiliation addIdentifier(Identifier t) { //3
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
     * @return {@link #active} (Whether this organization affiliation record is in active use.). This is the underlying object with id, value and extensions. The accessor "getActive" gives direct access to the value
     */
    public BooleanType getActiveElement() { 
      if (this.active == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create OrganizationAffiliation.active");
        else if (Configuration.doAutoCreate())
          this.active = new BooleanType(); // bb
      return this.active;
    }

    public boolean hasActiveElement() { 
      return this.active != null && !this.active.isEmpty();
    }

    public boolean hasActive() { 
      return this.active != null && !this.active.isEmpty();
    }

    /**
     * @param value {@link #active} (Whether this organization affiliation record is in active use.). This is the underlying object with id, value and extensions. The accessor "getActive" gives direct access to the value
     */
    public OrganizationAffiliation setActiveElement(BooleanType value) { 
      this.active = value;
      return this;
    }

    /**
     * @return Whether this organization affiliation record is in active use.
     */
    public boolean getActive() { 
      return this.active == null || this.active.isEmpty() ? false : this.active.getValue();
    }

    /**
     * @param value Whether this organization affiliation record is in active use.
     */
    public OrganizationAffiliation setActive(boolean value) { 
        if (this.active == null)
          this.active = new BooleanType();
        this.active.setValue(value);
      return this;
    }

    /**
     * @return {@link #period} (The period during which the participatingOrganization is affiliated with the primary organization.)
     */
    public Period getPeriod() { 
      if (this.period == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create OrganizationAffiliation.period");
        else if (Configuration.doAutoCreate())
          this.period = new Period(); // cc
      return this.period;
    }

    public boolean hasPeriod() { 
      return this.period != null && !this.period.isEmpty();
    }

    /**
     * @param value {@link #period} (The period during which the participatingOrganization is affiliated with the primary organization.)
     */
    public OrganizationAffiliation setPeriod(Period value) { 
      this.period = value;
      return this;
    }

    /**
     * @return {@link #organization} (Organization where the role is available (primary organization/has members).)
     */
    public Reference getOrganization() { 
      if (this.organization == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create OrganizationAffiliation.organization");
        else if (Configuration.doAutoCreate())
          this.organization = new Reference(); // cc
      return this.organization;
    }

    public boolean hasOrganization() { 
      return this.organization != null && !this.organization.isEmpty();
    }

    /**
     * @param value {@link #organization} (Organization where the role is available (primary organization/has members).)
     */
    public OrganizationAffiliation setOrganization(Reference value) { 
      this.organization = value;
      return this;
    }

    /**
     * @return {@link #organization} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Organization where the role is available (primary organization/has members).)
     */
    public Organization getOrganizationTarget() { 
      if (this.organizationTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create OrganizationAffiliation.organization");
        else if (Configuration.doAutoCreate())
          this.organizationTarget = new Organization(); // aa
      return this.organizationTarget;
    }

    /**
     * @param value {@link #organization} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Organization where the role is available (primary organization/has members).)
     */
    public OrganizationAffiliation setOrganizationTarget(Organization value) { 
      this.organizationTarget = value;
      return this;
    }

    /**
     * @return {@link #participatingOrganization} (The Participating Organization provides/performs the role(s) defined by the code to the Primary Organization (e.g. providing services or is a member of).)
     */
    public Reference getParticipatingOrganization() { 
      if (this.participatingOrganization == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create OrganizationAffiliation.participatingOrganization");
        else if (Configuration.doAutoCreate())
          this.participatingOrganization = new Reference(); // cc
      return this.participatingOrganization;
    }

    public boolean hasParticipatingOrganization() { 
      return this.participatingOrganization != null && !this.participatingOrganization.isEmpty();
    }

    /**
     * @param value {@link #participatingOrganization} (The Participating Organization provides/performs the role(s) defined by the code to the Primary Organization (e.g. providing services or is a member of).)
     */
    public OrganizationAffiliation setParticipatingOrganization(Reference value) { 
      this.participatingOrganization = value;
      return this;
    }

    /**
     * @return {@link #participatingOrganization} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The Participating Organization provides/performs the role(s) defined by the code to the Primary Organization (e.g. providing services or is a member of).)
     */
    public Organization getParticipatingOrganizationTarget() { 
      if (this.participatingOrganizationTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create OrganizationAffiliation.participatingOrganization");
        else if (Configuration.doAutoCreate())
          this.participatingOrganizationTarget = new Organization(); // aa
      return this.participatingOrganizationTarget;
    }

    /**
     * @param value {@link #participatingOrganization} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The Participating Organization provides/performs the role(s) defined by the code to the Primary Organization (e.g. providing services or is a member of).)
     */
    public OrganizationAffiliation setParticipatingOrganizationTarget(Organization value) { 
      this.participatingOrganizationTarget = value;
      return this;
    }

    /**
     * @return {@link #network} (Health insurance provider network in which the participatingOrganization provides the role's services (if defined) at the indicated locations (if defined).)
     */
    public List<Reference> getNetwork() { 
      if (this.network == null)
        this.network = new ArrayList<Reference>();
      return this.network;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public OrganizationAffiliation setNetwork(List<Reference> theNetwork) { 
      this.network = theNetwork;
      return this;
    }

    public boolean hasNetwork() { 
      if (this.network == null)
        return false;
      for (Reference item : this.network)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addNetwork() { //3
      Reference t = new Reference();
      if (this.network == null)
        this.network = new ArrayList<Reference>();
      this.network.add(t);
      return t;
    }

    public OrganizationAffiliation addNetwork(Reference t) { //3
      if (t == null)
        return this;
      if (this.network == null)
        this.network = new ArrayList<Reference>();
      this.network.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #network}, creating it if it does not already exist
     */
    public Reference getNetworkFirstRep() { 
      if (getNetwork().isEmpty()) {
        addNetwork();
      }
      return getNetwork().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Organization> getNetworkTarget() { 
      if (this.networkTarget == null)
        this.networkTarget = new ArrayList<Organization>();
      return this.networkTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public Organization addNetworkTarget() { 
      Organization r = new Organization();
      if (this.networkTarget == null)
        this.networkTarget = new ArrayList<Organization>();
      this.networkTarget.add(r);
      return r;
    }

    /**
     * @return {@link #code} (Definition of the role the participatingOrganization plays in the association.)
     */
    public List<CodeableConcept> getCode() { 
      if (this.code == null)
        this.code = new ArrayList<CodeableConcept>();
      return this.code;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public OrganizationAffiliation setCode(List<CodeableConcept> theCode) { 
      this.code = theCode;
      return this;
    }

    public boolean hasCode() { 
      if (this.code == null)
        return false;
      for (CodeableConcept item : this.code)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addCode() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.code == null)
        this.code = new ArrayList<CodeableConcept>();
      this.code.add(t);
      return t;
    }

    public OrganizationAffiliation addCode(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.code == null)
        this.code = new ArrayList<CodeableConcept>();
      this.code.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #code}, creating it if it does not already exist
     */
    public CodeableConcept getCodeFirstRep() { 
      if (getCode().isEmpty()) {
        addCode();
      }
      return getCode().get(0);
    }

    /**
     * @return {@link #specialty} (Specific specialty of the participatingOrganization in the context of the role.)
     */
    public List<CodeableConcept> getSpecialty() { 
      if (this.specialty == null)
        this.specialty = new ArrayList<CodeableConcept>();
      return this.specialty;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public OrganizationAffiliation setSpecialty(List<CodeableConcept> theSpecialty) { 
      this.specialty = theSpecialty;
      return this;
    }

    public boolean hasSpecialty() { 
      if (this.specialty == null)
        return false;
      for (CodeableConcept item : this.specialty)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addSpecialty() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.specialty == null)
        this.specialty = new ArrayList<CodeableConcept>();
      this.specialty.add(t);
      return t;
    }

    public OrganizationAffiliation addSpecialty(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.specialty == null)
        this.specialty = new ArrayList<CodeableConcept>();
      this.specialty.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #specialty}, creating it if it does not already exist
     */
    public CodeableConcept getSpecialtyFirstRep() { 
      if (getSpecialty().isEmpty()) {
        addSpecialty();
      }
      return getSpecialty().get(0);
    }

    /**
     * @return {@link #location} (The location(s) at which the role occurs.)
     */
    public List<Reference> getLocation() { 
      if (this.location == null)
        this.location = new ArrayList<Reference>();
      return this.location;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public OrganizationAffiliation setLocation(List<Reference> theLocation) { 
      this.location = theLocation;
      return this;
    }

    public boolean hasLocation() { 
      if (this.location == null)
        return false;
      for (Reference item : this.location)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addLocation() { //3
      Reference t = new Reference();
      if (this.location == null)
        this.location = new ArrayList<Reference>();
      this.location.add(t);
      return t;
    }

    public OrganizationAffiliation addLocation(Reference t) { //3
      if (t == null)
        return this;
      if (this.location == null)
        this.location = new ArrayList<Reference>();
      this.location.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #location}, creating it if it does not already exist
     */
    public Reference getLocationFirstRep() { 
      if (getLocation().isEmpty()) {
        addLocation();
      }
      return getLocation().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Location> getLocationTarget() { 
      if (this.locationTarget == null)
        this.locationTarget = new ArrayList<Location>();
      return this.locationTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public Location addLocationTarget() { 
      Location r = new Location();
      if (this.locationTarget == null)
        this.locationTarget = new ArrayList<Location>();
      this.locationTarget.add(r);
      return r;
    }

    /**
     * @return {@link #healthcareService} (Healthcare services provided through the role.)
     */
    public List<Reference> getHealthcareService() { 
      if (this.healthcareService == null)
        this.healthcareService = new ArrayList<Reference>();
      return this.healthcareService;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public OrganizationAffiliation setHealthcareService(List<Reference> theHealthcareService) { 
      this.healthcareService = theHealthcareService;
      return this;
    }

    public boolean hasHealthcareService() { 
      if (this.healthcareService == null)
        return false;
      for (Reference item : this.healthcareService)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addHealthcareService() { //3
      Reference t = new Reference();
      if (this.healthcareService == null)
        this.healthcareService = new ArrayList<Reference>();
      this.healthcareService.add(t);
      return t;
    }

    public OrganizationAffiliation addHealthcareService(Reference t) { //3
      if (t == null)
        return this;
      if (this.healthcareService == null)
        this.healthcareService = new ArrayList<Reference>();
      this.healthcareService.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #healthcareService}, creating it if it does not already exist
     */
    public Reference getHealthcareServiceFirstRep() { 
      if (getHealthcareService().isEmpty()) {
        addHealthcareService();
      }
      return getHealthcareService().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<HealthcareService> getHealthcareServiceTarget() { 
      if (this.healthcareServiceTarget == null)
        this.healthcareServiceTarget = new ArrayList<HealthcareService>();
      return this.healthcareServiceTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public HealthcareService addHealthcareServiceTarget() { 
      HealthcareService r = new HealthcareService();
      if (this.healthcareServiceTarget == null)
        this.healthcareServiceTarget = new ArrayList<HealthcareService>();
      this.healthcareServiceTarget.add(r);
      return r;
    }

    /**
     * @return {@link #telecom} (Contact details at the participatingOrganization relevant to this Affiliation.)
     */
    public List<ContactPoint> getTelecom() { 
      if (this.telecom == null)
        this.telecom = new ArrayList<ContactPoint>();
      return this.telecom;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public OrganizationAffiliation setTelecom(List<ContactPoint> theTelecom) { 
      this.telecom = theTelecom;
      return this;
    }

    public boolean hasTelecom() { 
      if (this.telecom == null)
        return false;
      for (ContactPoint item : this.telecom)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public ContactPoint addTelecom() { //3
      ContactPoint t = new ContactPoint();
      if (this.telecom == null)
        this.telecom = new ArrayList<ContactPoint>();
      this.telecom.add(t);
      return t;
    }

    public OrganizationAffiliation addTelecom(ContactPoint t) { //3
      if (t == null)
        return this;
      if (this.telecom == null)
        this.telecom = new ArrayList<ContactPoint>();
      this.telecom.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #telecom}, creating it if it does not already exist
     */
    public ContactPoint getTelecomFirstRep() { 
      if (getTelecom().isEmpty()) {
        addTelecom();
      }
      return getTelecom().get(0);
    }

    /**
     * @return {@link #endpoint} (Technical endpoints providing access to services operated for this role.)
     */
    public List<Reference> getEndpoint() { 
      if (this.endpoint == null)
        this.endpoint = new ArrayList<Reference>();
      return this.endpoint;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public OrganizationAffiliation setEndpoint(List<Reference> theEndpoint) { 
      this.endpoint = theEndpoint;
      return this;
    }

    public boolean hasEndpoint() { 
      if (this.endpoint == null)
        return false;
      for (Reference item : this.endpoint)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addEndpoint() { //3
      Reference t = new Reference();
      if (this.endpoint == null)
        this.endpoint = new ArrayList<Reference>();
      this.endpoint.add(t);
      return t;
    }

    public OrganizationAffiliation addEndpoint(Reference t) { //3
      if (t == null)
        return this;
      if (this.endpoint == null)
        this.endpoint = new ArrayList<Reference>();
      this.endpoint.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #endpoint}, creating it if it does not already exist
     */
    public Reference getEndpointFirstRep() { 
      if (getEndpoint().isEmpty()) {
        addEndpoint();
      }
      return getEndpoint().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Endpoint> getEndpointTarget() { 
      if (this.endpointTarget == null)
        this.endpointTarget = new ArrayList<Endpoint>();
      return this.endpointTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public Endpoint addEndpointTarget() { 
      Endpoint r = new Endpoint();
      if (this.endpointTarget == null)
        this.endpointTarget = new ArrayList<Endpoint>();
      this.endpointTarget.add(r);
      return r;
    }

      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("identifier", "Identifier", "Business identifiers that are specific to this role.", 0, java.lang.Integer.MAX_VALUE, identifier));
        children.add(new Property("active", "boolean", "Whether this organization affiliation record is in active use.", 0, 1, active));
        children.add(new Property("period", "Period", "The period during which the participatingOrganization is affiliated with the primary organization.", 0, 1, period));
        children.add(new Property("organization", "Reference(Organization)", "Organization where the role is available (primary organization/has members).", 0, 1, organization));
        children.add(new Property("participatingOrganization", "Reference(Organization)", "The Participating Organization provides/performs the role(s) defined by the code to the Primary Organization (e.g. providing services or is a member of).", 0, 1, participatingOrganization));
        children.add(new Property("network", "Reference(Organization)", "Health insurance provider network in which the participatingOrganization provides the role's services (if defined) at the indicated locations (if defined).", 0, java.lang.Integer.MAX_VALUE, network));
        children.add(new Property("code", "CodeableConcept", "Definition of the role the participatingOrganization plays in the association.", 0, java.lang.Integer.MAX_VALUE, code));
        children.add(new Property("specialty", "CodeableConcept", "Specific specialty of the participatingOrganization in the context of the role.", 0, java.lang.Integer.MAX_VALUE, specialty));
        children.add(new Property("location", "Reference(Location)", "The location(s) at which the role occurs.", 0, java.lang.Integer.MAX_VALUE, location));
        children.add(new Property("healthcareService", "Reference(HealthcareService)", "Healthcare services provided through the role.", 0, java.lang.Integer.MAX_VALUE, healthcareService));
        children.add(new Property("telecom", "ContactPoint", "Contact details at the participatingOrganization relevant to this Affiliation.", 0, java.lang.Integer.MAX_VALUE, telecom));
        children.add(new Property("endpoint", "Reference(Endpoint)", "Technical endpoints providing access to services operated for this role.", 0, java.lang.Integer.MAX_VALUE, endpoint));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case -1618432855: /*identifier*/  return new Property("identifier", "Identifier", "Business identifiers that are specific to this role.", 0, java.lang.Integer.MAX_VALUE, identifier);
        case -1422950650: /*active*/  return new Property("active", "boolean", "Whether this organization affiliation record is in active use.", 0, 1, active);
        case -991726143: /*period*/  return new Property("period", "Period", "The period during which the participatingOrganization is affiliated with the primary organization.", 0, 1, period);
        case 1178922291: /*organization*/  return new Property("organization", "Reference(Organization)", "Organization where the role is available (primary organization/has members).", 0, 1, organization);
        case 1593310702: /*participatingOrganization*/  return new Property("participatingOrganization", "Reference(Organization)", "The Participating Organization provides/performs the role(s) defined by the code to the Primary Organization (e.g. providing services or is a member of).", 0, 1, participatingOrganization);
        case 1843485230: /*network*/  return new Property("network", "Reference(Organization)", "Health insurance provider network in which the participatingOrganization provides the role's services (if defined) at the indicated locations (if defined).", 0, java.lang.Integer.MAX_VALUE, network);
        case 3059181: /*code*/  return new Property("code", "CodeableConcept", "Definition of the role the participatingOrganization plays in the association.", 0, java.lang.Integer.MAX_VALUE, code);
        case -1694759682: /*specialty*/  return new Property("specialty", "CodeableConcept", "Specific specialty of the participatingOrganization in the context of the role.", 0, java.lang.Integer.MAX_VALUE, specialty);
        case 1901043637: /*location*/  return new Property("location", "Reference(Location)", "The location(s) at which the role occurs.", 0, java.lang.Integer.MAX_VALUE, location);
        case 1289661064: /*healthcareService*/  return new Property("healthcareService", "Reference(HealthcareService)", "Healthcare services provided through the role.", 0, java.lang.Integer.MAX_VALUE, healthcareService);
        case -1429363305: /*telecom*/  return new Property("telecom", "ContactPoint", "Contact details at the participatingOrganization relevant to this Affiliation.", 0, java.lang.Integer.MAX_VALUE, telecom);
        case 1741102485: /*endpoint*/  return new Property("endpoint", "Reference(Endpoint)", "Technical endpoints providing access to services operated for this role.", 0, java.lang.Integer.MAX_VALUE, endpoint);
        default: return super.getNamedProperty(_hash, _name, _checkValid);
        }

      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case -1422950650: /*active*/ return this.active == null ? new Base[0] : new Base[] {this.active}; // BooleanType
        case -991726143: /*period*/ return this.period == null ? new Base[0] : new Base[] {this.period}; // Period
        case 1178922291: /*organization*/ return this.organization == null ? new Base[0] : new Base[] {this.organization}; // Reference
        case 1593310702: /*participatingOrganization*/ return this.participatingOrganization == null ? new Base[0] : new Base[] {this.participatingOrganization}; // Reference
        case 1843485230: /*network*/ return this.network == null ? new Base[0] : this.network.toArray(new Base[this.network.size()]); // Reference
        case 3059181: /*code*/ return this.code == null ? new Base[0] : this.code.toArray(new Base[this.code.size()]); // CodeableConcept
        case -1694759682: /*specialty*/ return this.specialty == null ? new Base[0] : this.specialty.toArray(new Base[this.specialty.size()]); // CodeableConcept
        case 1901043637: /*location*/ return this.location == null ? new Base[0] : this.location.toArray(new Base[this.location.size()]); // Reference
        case 1289661064: /*healthcareService*/ return this.healthcareService == null ? new Base[0] : this.healthcareService.toArray(new Base[this.healthcareService.size()]); // Reference
        case -1429363305: /*telecom*/ return this.telecom == null ? new Base[0] : this.telecom.toArray(new Base[this.telecom.size()]); // ContactPoint
        case 1741102485: /*endpoint*/ return this.endpoint == null ? new Base[0] : this.endpoint.toArray(new Base[this.endpoint.size()]); // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.getIdentifier().add(castToIdentifier(value)); // Identifier
          return value;
        case -1422950650: // active
          this.active = castToBoolean(value); // BooleanType
          return value;
        case -991726143: // period
          this.period = castToPeriod(value); // Period
          return value;
        case 1178922291: // organization
          this.organization = castToReference(value); // Reference
          return value;
        case 1593310702: // participatingOrganization
          this.participatingOrganization = castToReference(value); // Reference
          return value;
        case 1843485230: // network
          this.getNetwork().add(castToReference(value)); // Reference
          return value;
        case 3059181: // code
          this.getCode().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -1694759682: // specialty
          this.getSpecialty().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 1901043637: // location
          this.getLocation().add(castToReference(value)); // Reference
          return value;
        case 1289661064: // healthcareService
          this.getHealthcareService().add(castToReference(value)); // Reference
          return value;
        case -1429363305: // telecom
          this.getTelecom().add(castToContactPoint(value)); // ContactPoint
          return value;
        case 1741102485: // endpoint
          this.getEndpoint().add(castToReference(value)); // Reference
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier")) {
          this.getIdentifier().add(castToIdentifier(value));
        } else if (name.equals("active")) {
          this.active = castToBoolean(value); // BooleanType
        } else if (name.equals("period")) {
          this.period = castToPeriod(value); // Period
        } else if (name.equals("organization")) {
          this.organization = castToReference(value); // Reference
        } else if (name.equals("participatingOrganization")) {
          this.participatingOrganization = castToReference(value); // Reference
        } else if (name.equals("network")) {
          this.getNetwork().add(castToReference(value));
        } else if (name.equals("code")) {
          this.getCode().add(castToCodeableConcept(value));
        } else if (name.equals("specialty")) {
          this.getSpecialty().add(castToCodeableConcept(value));
        } else if (name.equals("location")) {
          this.getLocation().add(castToReference(value));
        } else if (name.equals("healthcareService")) {
          this.getHealthcareService().add(castToReference(value));
        } else if (name.equals("telecom")) {
          this.getTelecom().add(castToContactPoint(value));
        } else if (name.equals("endpoint")) {
          this.getEndpoint().add(castToReference(value));
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return addIdentifier(); 
        case -1422950650:  return getActiveElement();
        case -991726143:  return getPeriod(); 
        case 1178922291:  return getOrganization(); 
        case 1593310702:  return getParticipatingOrganization(); 
        case 1843485230:  return addNetwork(); 
        case 3059181:  return addCode(); 
        case -1694759682:  return addSpecialty(); 
        case 1901043637:  return addLocation(); 
        case 1289661064:  return addHealthcareService(); 
        case -1429363305:  return addTelecom(); 
        case 1741102485:  return addEndpoint(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case -1422950650: /*active*/ return new String[] {"boolean"};
        case -991726143: /*period*/ return new String[] {"Period"};
        case 1178922291: /*organization*/ return new String[] {"Reference"};
        case 1593310702: /*participatingOrganization*/ return new String[] {"Reference"};
        case 1843485230: /*network*/ return new String[] {"Reference"};
        case 3059181: /*code*/ return new String[] {"CodeableConcept"};
        case -1694759682: /*specialty*/ return new String[] {"CodeableConcept"};
        case 1901043637: /*location*/ return new String[] {"Reference"};
        case 1289661064: /*healthcareService*/ return new String[] {"Reference"};
        case -1429363305: /*telecom*/ return new String[] {"ContactPoint"};
        case 1741102485: /*endpoint*/ return new String[] {"Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("active")) {
          throw new FHIRException("Cannot call addChild on a primitive type OrganizationAffiliation.active");
        }
        else if (name.equals("period")) {
          this.period = new Period();
          return this.period;
        }
        else if (name.equals("organization")) {
          this.organization = new Reference();
          return this.organization;
        }
        else if (name.equals("participatingOrganization")) {
          this.participatingOrganization = new Reference();
          return this.participatingOrganization;
        }
        else if (name.equals("network")) {
          return addNetwork();
        }
        else if (name.equals("code")) {
          return addCode();
        }
        else if (name.equals("specialty")) {
          return addSpecialty();
        }
        else if (name.equals("location")) {
          return addLocation();
        }
        else if (name.equals("healthcareService")) {
          return addHealthcareService();
        }
        else if (name.equals("telecom")) {
          return addTelecom();
        }
        else if (name.equals("endpoint")) {
          return addEndpoint();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "OrganizationAffiliation";

  }

      public OrganizationAffiliation copy() {
        OrganizationAffiliation dst = new OrganizationAffiliation();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.active = active == null ? null : active.copy();
        dst.period = period == null ? null : period.copy();
        dst.organization = organization == null ? null : organization.copy();
        dst.participatingOrganization = participatingOrganization == null ? null : participatingOrganization.copy();
        if (network != null) {
          dst.network = new ArrayList<Reference>();
          for (Reference i : network)
            dst.network.add(i.copy());
        };
        if (code != null) {
          dst.code = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : code)
            dst.code.add(i.copy());
        };
        if (specialty != null) {
          dst.specialty = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : specialty)
            dst.specialty.add(i.copy());
        };
        if (location != null) {
          dst.location = new ArrayList<Reference>();
          for (Reference i : location)
            dst.location.add(i.copy());
        };
        if (healthcareService != null) {
          dst.healthcareService = new ArrayList<Reference>();
          for (Reference i : healthcareService)
            dst.healthcareService.add(i.copy());
        };
        if (telecom != null) {
          dst.telecom = new ArrayList<ContactPoint>();
          for (ContactPoint i : telecom)
            dst.telecom.add(i.copy());
        };
        if (endpoint != null) {
          dst.endpoint = new ArrayList<Reference>();
          for (Reference i : endpoint)
            dst.endpoint.add(i.copy());
        };
        return dst;
      }

      protected OrganizationAffiliation typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof OrganizationAffiliation))
          return false;
        OrganizationAffiliation o = (OrganizationAffiliation) other_;
        return compareDeep(identifier, o.identifier, true) && compareDeep(active, o.active, true) && compareDeep(period, o.period, true)
           && compareDeep(organization, o.organization, true) && compareDeep(participatingOrganization, o.participatingOrganization, true)
           && compareDeep(network, o.network, true) && compareDeep(code, o.code, true) && compareDeep(specialty, o.specialty, true)
           && compareDeep(location, o.location, true) && compareDeep(healthcareService, o.healthcareService, true)
           && compareDeep(telecom, o.telecom, true) && compareDeep(endpoint, o.endpoint, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof OrganizationAffiliation))
          return false;
        OrganizationAffiliation o = (OrganizationAffiliation) other_;
        return compareValues(active, o.active, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, active, period
          , organization, participatingOrganization, network, code, specialty, location, healthcareService
          , telecom, endpoint);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.OrganizationAffiliation;
   }

 /**
   * Search parameter: <b>date</b>
   * <p>
   * Description: <b>The period during which the participatingOrganization is affiliated with the primary organization</b><br>
   * Type: <b>date</b><br>
   * Path: <b>OrganizationAffiliation.period</b><br>
   * </p>
   */
  @SearchParamDefinition(name="date", path="OrganizationAffiliation.period", description="The period during which the participatingOrganization is affiliated with the primary organization", type="date" )
  public static final String SP_DATE = "date";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>date</b>
   * <p>
   * Description: <b>The period during which the participatingOrganization is affiliated with the primary organization</b><br>
   * Type: <b>date</b><br>
   * Path: <b>OrganizationAffiliation.period</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam DATE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_DATE);

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>An organization affiliation's Identifier</b><br>
   * Type: <b>token</b><br>
   * Path: <b>OrganizationAffiliation.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="OrganizationAffiliation.identifier", description="An organization affiliation's Identifier", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>An organization affiliation's Identifier</b><br>
   * Type: <b>token</b><br>
   * Path: <b>OrganizationAffiliation.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>specialty</b>
   * <p>
   * Description: <b>Specific specialty of the participatingOrganization in the context of the role</b><br>
   * Type: <b>token</b><br>
   * Path: <b>OrganizationAffiliation.specialty</b><br>
   * </p>
   */
  @SearchParamDefinition(name="specialty", path="OrganizationAffiliation.specialty", description="Specific specialty of the participatingOrganization in the context of the role", type="token" )
  public static final String SP_SPECIALTY = "specialty";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>specialty</b>
   * <p>
   * Description: <b>Specific specialty of the participatingOrganization in the context of the role</b><br>
   * Type: <b>token</b><br>
   * Path: <b>OrganizationAffiliation.specialty</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam SPECIALTY = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_SPECIALTY);

 /**
   * Search parameter: <b>role</b>
   * <p>
   * Description: <b>Definition of the role the participatingOrganization plays</b><br>
   * Type: <b>token</b><br>
   * Path: <b>OrganizationAffiliation.code</b><br>
   * </p>
   */
  @SearchParamDefinition(name="role", path="OrganizationAffiliation.code", description="Definition of the role the participatingOrganization plays", type="token" )
  public static final String SP_ROLE = "role";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>role</b>
   * <p>
   * Description: <b>Definition of the role the participatingOrganization plays</b><br>
   * Type: <b>token</b><br>
   * Path: <b>OrganizationAffiliation.code</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam ROLE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_ROLE);

 /**
   * Search parameter: <b>active</b>
   * <p>
   * Description: <b>Whether this organization affiliation record is in active use</b><br>
   * Type: <b>token</b><br>
   * Path: <b>OrganizationAffiliation.active</b><br>
   * </p>
   */
  @SearchParamDefinition(name="active", path="OrganizationAffiliation.active", description="Whether this organization affiliation record is in active use", type="token" )
  public static final String SP_ACTIVE = "active";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>active</b>
   * <p>
   * Description: <b>Whether this organization affiliation record is in active use</b><br>
   * Type: <b>token</b><br>
   * Path: <b>OrganizationAffiliation.active</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam ACTIVE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_ACTIVE);

 /**
   * Search parameter: <b>primary-organization</b>
   * <p>
   * Description: <b>The organization that receives the services from the participating organization</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>OrganizationAffiliation.organization</b><br>
   * </p>
   */
  @SearchParamDefinition(name="primary-organization", path="OrganizationAffiliation.organization", description="The organization that receives the services from the participating organization", type="reference", target={Organization.class } )
  public static final String SP_PRIMARY_ORGANIZATION = "primary-organization";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>primary-organization</b>
   * <p>
   * Description: <b>The organization that receives the services from the participating organization</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>OrganizationAffiliation.organization</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PRIMARY_ORGANIZATION = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PRIMARY_ORGANIZATION);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>OrganizationAffiliation:primary-organization</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PRIMARY_ORGANIZATION = new ca.uhn.fhir.model.api.Include("OrganizationAffiliation:primary-organization").toLocked();

 /**
   * Search parameter: <b>network</b>
   * <p>
   * Description: <b>Health insurance provider network in which the participatingOrganization provides the role's services (if defined) at the indicated locations (if defined)</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>OrganizationAffiliation.network</b><br>
   * </p>
   */
  @SearchParamDefinition(name="network", path="OrganizationAffiliation.network", description="Health insurance provider network in which the participatingOrganization provides the role's services (if defined) at the indicated locations (if defined)", type="reference", target={Organization.class } )
  public static final String SP_NETWORK = "network";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>network</b>
   * <p>
   * Description: <b>Health insurance provider network in which the participatingOrganization provides the role's services (if defined) at the indicated locations (if defined)</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>OrganizationAffiliation.network</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam NETWORK = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_NETWORK);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>OrganizationAffiliation:network</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_NETWORK = new ca.uhn.fhir.model.api.Include("OrganizationAffiliation:network").toLocked();

 /**
   * Search parameter: <b>endpoint</b>
   * <p>
   * Description: <b>Technical endpoints providing access to services operated for this role</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>OrganizationAffiliation.endpoint</b><br>
   * </p>
   */
  @SearchParamDefinition(name="endpoint", path="OrganizationAffiliation.endpoint", description="Technical endpoints providing access to services operated for this role", type="reference", target={Endpoint.class } )
  public static final String SP_ENDPOINT = "endpoint";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>endpoint</b>
   * <p>
   * Description: <b>Technical endpoints providing access to services operated for this role</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>OrganizationAffiliation.endpoint</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam ENDPOINT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_ENDPOINT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>OrganizationAffiliation:endpoint</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_ENDPOINT = new ca.uhn.fhir.model.api.Include("OrganizationAffiliation:endpoint").toLocked();

 /**
   * Search parameter: <b>phone</b>
   * <p>
   * Description: <b>A value in a phone contact</b><br>
   * Type: <b>token</b><br>
   * Path: <b>OrganizationAffiliation.telecom(system=phone)</b><br>
   * </p>
   */
  @SearchParamDefinition(name="phone", path="OrganizationAffiliation.telecom.where(system='phone')", description="A value in a phone contact", type="token" )
  public static final String SP_PHONE = "phone";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>phone</b>
   * <p>
   * Description: <b>A value in a phone contact</b><br>
   * Type: <b>token</b><br>
   * Path: <b>OrganizationAffiliation.telecom(system=phone)</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam PHONE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_PHONE);

 /**
   * Search parameter: <b>service</b>
   * <p>
   * Description: <b>Healthcare services provided through the role</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>OrganizationAffiliation.healthcareService</b><br>
   * </p>
   */
  @SearchParamDefinition(name="service", path="OrganizationAffiliation.healthcareService", description="Healthcare services provided through the role", type="reference", target={HealthcareService.class } )
  public static final String SP_SERVICE = "service";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>service</b>
   * <p>
   * Description: <b>Healthcare services provided through the role</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>OrganizationAffiliation.healthcareService</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SERVICE = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SERVICE);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>OrganizationAffiliation:service</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SERVICE = new ca.uhn.fhir.model.api.Include("OrganizationAffiliation:service").toLocked();

 /**
   * Search parameter: <b>participating-organization</b>
   * <p>
   * Description: <b>The organization that provides services to the primary organization</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>OrganizationAffiliation.participatingOrganization</b><br>
   * </p>
   */
  @SearchParamDefinition(name="participating-organization", path="OrganizationAffiliation.participatingOrganization", description="The organization that provides services to the primary organization", type="reference", target={Organization.class } )
  public static final String SP_PARTICIPATING_ORGANIZATION = "participating-organization";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>participating-organization</b>
   * <p>
   * Description: <b>The organization that provides services to the primary organization</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>OrganizationAffiliation.participatingOrganization</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PARTICIPATING_ORGANIZATION = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PARTICIPATING_ORGANIZATION);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>OrganizationAffiliation:participating-organization</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PARTICIPATING_ORGANIZATION = new ca.uhn.fhir.model.api.Include("OrganizationAffiliation:participating-organization").toLocked();

 /**
   * Search parameter: <b>telecom</b>
   * <p>
   * Description: <b>The value in any kind of contact</b><br>
   * Type: <b>token</b><br>
   * Path: <b>OrganizationAffiliation.telecom</b><br>
   * </p>
   */
  @SearchParamDefinition(name="telecom", path="OrganizationAffiliation.telecom", description="The value in any kind of contact", type="token" )
  public static final String SP_TELECOM = "telecom";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>telecom</b>
   * <p>
   * Description: <b>The value in any kind of contact</b><br>
   * Type: <b>token</b><br>
   * Path: <b>OrganizationAffiliation.telecom</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam TELECOM = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_TELECOM);

 /**
   * Search parameter: <b>location</b>
   * <p>
   * Description: <b>The location(s) at which the role occurs</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>OrganizationAffiliation.location</b><br>
   * </p>
   */
  @SearchParamDefinition(name="location", path="OrganizationAffiliation.location", description="The location(s) at which the role occurs", type="reference", target={Location.class } )
  public static final String SP_LOCATION = "location";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>location</b>
   * <p>
   * Description: <b>The location(s) at which the role occurs</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>OrganizationAffiliation.location</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam LOCATION = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_LOCATION);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>OrganizationAffiliation:location</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_LOCATION = new ca.uhn.fhir.model.api.Include("OrganizationAffiliation:location").toLocked();

 /**
   * Search parameter: <b>email</b>
   * <p>
   * Description: <b>A value in an email contact</b><br>
   * Type: <b>token</b><br>
   * Path: <b>OrganizationAffiliation.telecom(system=email)</b><br>
   * </p>
   */
  @SearchParamDefinition(name="email", path="OrganizationAffiliation.telecom.where(system='email')", description="A value in an email contact", type="token" )
  public static final String SP_EMAIL = "email";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>email</b>
   * <p>
   * Description: <b>A value in an email contact</b><br>
   * Type: <b>token</b><br>
   * Path: <b>OrganizationAffiliation.telecom(system=email)</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam EMAIL = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_EMAIL);


}

