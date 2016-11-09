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
import org.hl7.fhir.dstu3.model.Enumerations.*;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ChildOrder;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Block;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.exceptions.FHIRException;
/**
 * A person who is directly or indirectly involved in the provisioning of healthcare.
 */
@ResourceDef(name="Practitioner", profile="http://hl7.org/fhir/Profile/Practitioner")
public class Practitioner extends DomainResource {

    @Block()
    public static class PractitionerRoleComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The organization where the Practitioner performs the roles associated.
         */
        @Child(name = "organization", type = {Organization.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Organization where the roles are performed", formalDefinition="The organization where the Practitioner performs the roles associated." )
        protected Reference organization;

        /**
         * The actual object that is the target of the reference (The organization where the Practitioner performs the roles associated.)
         */
        protected Organization organizationTarget;

        /**
         * Roles which this practitioner is authorized to perform for the organization.
         */
        @Child(name = "code", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Roles which this practitioner may perform", formalDefinition="Roles which this practitioner is authorized to perform for the organization." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/practitioner-role")
        protected CodeableConcept code;

        /**
         * Specific specialty of the practitioner.
         */
        @Child(name = "specialty", type = {CodeableConcept.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Specific specialty of the practitioner", formalDefinition="Specific specialty of the practitioner." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/practitioner-specialty")
        protected List<CodeableConcept> specialty;

        /**
         * Business Identifiers that are specific to a role/location.
         */
        @Child(name = "identifier", type = {Identifier.class}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Business Identifiers that are specific to a role/location", formalDefinition="Business Identifiers that are specific to a role/location." )
        protected List<Identifier> identifier;

        /**
         * Contact details that are specific to the role/location/service.
         */
        @Child(name = "telecom", type = {ContactPoint.class}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Contact details that are specific to the role/location/service", formalDefinition="Contact details that are specific to the role/location/service." )
        protected List<ContactPoint> telecom;

        /**
         * The period during which the person is authorized to act as a practitioner in these role(s) for the organization.
         */
        @Child(name = "period", type = {Period.class}, order=6, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The period during which the practitioner is authorized to perform in these role(s)", formalDefinition="The period during which the person is authorized to act as a practitioner in these role(s) for the organization." )
        protected Period period;

        /**
         * The location(s) at which this practitioner provides care.
         */
        @Child(name = "location", type = {Location.class}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="The location(s) at which this practitioner provides care", formalDefinition="The location(s) at which this practitioner provides care." )
        protected List<Reference> location;
        /**
         * The actual objects that are the target of the reference (The location(s) at which this practitioner provides care.)
         */
        protected List<Location> locationTarget;


        /**
         * The list of healthcare services that this worker provides for this role's Organization/Location(s).
         */
        @Child(name = "healthcareService", type = {HealthcareService.class}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="The list of healthcare services that this worker provides for this role's Organization/Location(s)", formalDefinition="The list of healthcare services that this worker provides for this role's Organization/Location(s)." )
        protected List<Reference> healthcareService;
        /**
         * The actual objects that are the target of the reference (The list of healthcare services that this worker provides for this role's Organization/Location(s).)
         */
        protected List<HealthcareService> healthcareServiceTarget;


        /**
         * Technical endpoints providing access to services operated for the PractitonerRole.
         */
        @Child(name = "endpoint", type = {Endpoint.class}, order=9, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Technical endpoints providing access to services operated for the PractitonerRole", formalDefinition="Technical endpoints providing access to services operated for the PractitonerRole." )
        protected List<Reference> endpoint;
        /**
         * The actual objects that are the target of the reference (Technical endpoints providing access to services operated for the PractitonerRole.)
         */
        protected List<Endpoint> endpointTarget;


        private static final long serialVersionUID = 677139919L;

    /**
     * Constructor
     */
      public PractitionerRoleComponent() {
        super();
      }

        /**
         * @return {@link #organization} (The organization where the Practitioner performs the roles associated.)
         */
        public Reference getOrganization() { 
          if (this.organization == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PractitionerRoleComponent.organization");
            else if (Configuration.doAutoCreate())
              this.organization = new Reference(); // cc
          return this.organization;
        }

        public boolean hasOrganization() { 
          return this.organization != null && !this.organization.isEmpty();
        }

        /**
         * @param value {@link #organization} (The organization where the Practitioner performs the roles associated.)
         */
        public PractitionerRoleComponent setOrganization(Reference value) { 
          this.organization = value;
          return this;
        }

        /**
         * @return {@link #organization} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The organization where the Practitioner performs the roles associated.)
         */
        public Organization getOrganizationTarget() { 
          if (this.organizationTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PractitionerRoleComponent.organization");
            else if (Configuration.doAutoCreate())
              this.organizationTarget = new Organization(); // aa
          return this.organizationTarget;
        }

        /**
         * @param value {@link #organization} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The organization where the Practitioner performs the roles associated.)
         */
        public PractitionerRoleComponent setOrganizationTarget(Organization value) { 
          this.organizationTarget = value;
          return this;
        }

        /**
         * @return {@link #code} (Roles which this practitioner is authorized to perform for the organization.)
         */
        public CodeableConcept getCode() { 
          if (this.code == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PractitionerRoleComponent.code");
            else if (Configuration.doAutoCreate())
              this.code = new CodeableConcept(); // cc
          return this.code;
        }

        public boolean hasCode() { 
          return this.code != null && !this.code.isEmpty();
        }

        /**
         * @param value {@link #code} (Roles which this practitioner is authorized to perform for the organization.)
         */
        public PractitionerRoleComponent setCode(CodeableConcept value) { 
          this.code = value;
          return this;
        }

        /**
         * @return {@link #specialty} (Specific specialty of the practitioner.)
         */
        public List<CodeableConcept> getSpecialty() { 
          if (this.specialty == null)
            this.specialty = new ArrayList<CodeableConcept>();
          return this.specialty;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public PractitionerRoleComponent setSpecialty(List<CodeableConcept> theSpecialty) { 
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

        public PractitionerRoleComponent addSpecialty(CodeableConcept t) { //3
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
         * @return {@link #identifier} (Business Identifiers that are specific to a role/location.)
         */
        public List<Identifier> getIdentifier() { 
          if (this.identifier == null)
            this.identifier = new ArrayList<Identifier>();
          return this.identifier;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public PractitionerRoleComponent setIdentifier(List<Identifier> theIdentifier) { 
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

        public PractitionerRoleComponent addIdentifier(Identifier t) { //3
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
         * @return {@link #telecom} (Contact details that are specific to the role/location/service.)
         */
        public List<ContactPoint> getTelecom() { 
          if (this.telecom == null)
            this.telecom = new ArrayList<ContactPoint>();
          return this.telecom;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public PractitionerRoleComponent setTelecom(List<ContactPoint> theTelecom) { 
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

        public PractitionerRoleComponent addTelecom(ContactPoint t) { //3
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
         * @return {@link #period} (The period during which the person is authorized to act as a practitioner in these role(s) for the organization.)
         */
        public Period getPeriod() { 
          if (this.period == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PractitionerRoleComponent.period");
            else if (Configuration.doAutoCreate())
              this.period = new Period(); // cc
          return this.period;
        }

        public boolean hasPeriod() { 
          return this.period != null && !this.period.isEmpty();
        }

        /**
         * @param value {@link #period} (The period during which the person is authorized to act as a practitioner in these role(s) for the organization.)
         */
        public PractitionerRoleComponent setPeriod(Period value) { 
          this.period = value;
          return this;
        }

        /**
         * @return {@link #location} (The location(s) at which this practitioner provides care.)
         */
        public List<Reference> getLocation() { 
          if (this.location == null)
            this.location = new ArrayList<Reference>();
          return this.location;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public PractitionerRoleComponent setLocation(List<Reference> theLocation) { 
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

        public PractitionerRoleComponent addLocation(Reference t) { //3
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
         * @return {@link #healthcareService} (The list of healthcare services that this worker provides for this role's Organization/Location(s).)
         */
        public List<Reference> getHealthcareService() { 
          if (this.healthcareService == null)
            this.healthcareService = new ArrayList<Reference>();
          return this.healthcareService;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public PractitionerRoleComponent setHealthcareService(List<Reference> theHealthcareService) { 
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

        public PractitionerRoleComponent addHealthcareService(Reference t) { //3
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
         * @return {@link #endpoint} (Technical endpoints providing access to services operated for the PractitonerRole.)
         */
        public List<Reference> getEndpoint() { 
          if (this.endpoint == null)
            this.endpoint = new ArrayList<Reference>();
          return this.endpoint;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public PractitionerRoleComponent setEndpoint(List<Reference> theEndpoint) { 
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

        public PractitionerRoleComponent addEndpoint(Reference t) { //3
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

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("organization", "Reference(Organization)", "The organization where the Practitioner performs the roles associated.", 0, java.lang.Integer.MAX_VALUE, organization));
          childrenList.add(new Property("code", "CodeableConcept", "Roles which this practitioner is authorized to perform for the organization.", 0, java.lang.Integer.MAX_VALUE, code));
          childrenList.add(new Property("specialty", "CodeableConcept", "Specific specialty of the practitioner.", 0, java.lang.Integer.MAX_VALUE, specialty));
          childrenList.add(new Property("identifier", "Identifier", "Business Identifiers that are specific to a role/location.", 0, java.lang.Integer.MAX_VALUE, identifier));
          childrenList.add(new Property("telecom", "ContactPoint", "Contact details that are specific to the role/location/service.", 0, java.lang.Integer.MAX_VALUE, telecom));
          childrenList.add(new Property("period", "Period", "The period during which the person is authorized to act as a practitioner in these role(s) for the organization.", 0, java.lang.Integer.MAX_VALUE, period));
          childrenList.add(new Property("location", "Reference(Location)", "The location(s) at which this practitioner provides care.", 0, java.lang.Integer.MAX_VALUE, location));
          childrenList.add(new Property("healthcareService", "Reference(HealthcareService)", "The list of healthcare services that this worker provides for this role's Organization/Location(s).", 0, java.lang.Integer.MAX_VALUE, healthcareService));
          childrenList.add(new Property("endpoint", "Reference(Endpoint)", "Technical endpoints providing access to services operated for the PractitonerRole.", 0, java.lang.Integer.MAX_VALUE, endpoint));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 1178922291: /*organization*/ return this.organization == null ? new Base[0] : new Base[] {this.organization}; // Reference
        case 3059181: /*code*/ return this.code == null ? new Base[0] : new Base[] {this.code}; // CodeableConcept
        case -1694759682: /*specialty*/ return this.specialty == null ? new Base[0] : this.specialty.toArray(new Base[this.specialty.size()]); // CodeableConcept
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case -1429363305: /*telecom*/ return this.telecom == null ? new Base[0] : this.telecom.toArray(new Base[this.telecom.size()]); // ContactPoint
        case -991726143: /*period*/ return this.period == null ? new Base[0] : new Base[] {this.period}; // Period
        case 1901043637: /*location*/ return this.location == null ? new Base[0] : this.location.toArray(new Base[this.location.size()]); // Reference
        case 1289661064: /*healthcareService*/ return this.healthcareService == null ? new Base[0] : this.healthcareService.toArray(new Base[this.healthcareService.size()]); // Reference
        case 1741102485: /*endpoint*/ return this.endpoint == null ? new Base[0] : this.endpoint.toArray(new Base[this.endpoint.size()]); // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 1178922291: // organization
          this.organization = castToReference(value); // Reference
          break;
        case 3059181: // code
          this.code = castToCodeableConcept(value); // CodeableConcept
          break;
        case -1694759682: // specialty
          this.getSpecialty().add(castToCodeableConcept(value)); // CodeableConcept
          break;
        case -1618432855: // identifier
          this.getIdentifier().add(castToIdentifier(value)); // Identifier
          break;
        case -1429363305: // telecom
          this.getTelecom().add(castToContactPoint(value)); // ContactPoint
          break;
        case -991726143: // period
          this.period = castToPeriod(value); // Period
          break;
        case 1901043637: // location
          this.getLocation().add(castToReference(value)); // Reference
          break;
        case 1289661064: // healthcareService
          this.getHealthcareService().add(castToReference(value)); // Reference
          break;
        case 1741102485: // endpoint
          this.getEndpoint().add(castToReference(value)); // Reference
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("organization"))
          this.organization = castToReference(value); // Reference
        else if (name.equals("code"))
          this.code = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("specialty"))
          this.getSpecialty().add(castToCodeableConcept(value));
        else if (name.equals("identifier"))
          this.getIdentifier().add(castToIdentifier(value));
        else if (name.equals("telecom"))
          this.getTelecom().add(castToContactPoint(value));
        else if (name.equals("period"))
          this.period = castToPeriod(value); // Period
        else if (name.equals("location"))
          this.getLocation().add(castToReference(value));
        else if (name.equals("healthcareService"))
          this.getHealthcareService().add(castToReference(value));
        else if (name.equals("endpoint"))
          this.getEndpoint().add(castToReference(value));
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1178922291:  return getOrganization(); // Reference
        case 3059181:  return getCode(); // CodeableConcept
        case -1694759682:  return addSpecialty(); // CodeableConcept
        case -1618432855:  return addIdentifier(); // Identifier
        case -1429363305:  return addTelecom(); // ContactPoint
        case -991726143:  return getPeriod(); // Period
        case 1901043637:  return addLocation(); // Reference
        case 1289661064:  return addHealthcareService(); // Reference
        case 1741102485:  return addEndpoint(); // Reference
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("organization")) {
          this.organization = new Reference();
          return this.organization;
        }
        else if (name.equals("code")) {
          this.code = new CodeableConcept();
          return this.code;
        }
        else if (name.equals("specialty")) {
          return addSpecialty();
        }
        else if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("telecom")) {
          return addTelecom();
        }
        else if (name.equals("period")) {
          this.period = new Period();
          return this.period;
        }
        else if (name.equals("location")) {
          return addLocation();
        }
        else if (name.equals("healthcareService")) {
          return addHealthcareService();
        }
        else if (name.equals("endpoint")) {
          return addEndpoint();
        }
        else
          return super.addChild(name);
      }

      public PractitionerRoleComponent copy() {
        PractitionerRoleComponent dst = new PractitionerRoleComponent();
        copyValues(dst);
        dst.organization = organization == null ? null : organization.copy();
        dst.code = code == null ? null : code.copy();
        if (specialty != null) {
          dst.specialty = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : specialty)
            dst.specialty.add(i.copy());
        };
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        if (telecom != null) {
          dst.telecom = new ArrayList<ContactPoint>();
          for (ContactPoint i : telecom)
            dst.telecom.add(i.copy());
        };
        dst.period = period == null ? null : period.copy();
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
        if (endpoint != null) {
          dst.endpoint = new ArrayList<Reference>();
          for (Reference i : endpoint)
            dst.endpoint.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof PractitionerRoleComponent))
          return false;
        PractitionerRoleComponent o = (PractitionerRoleComponent) other;
        return compareDeep(organization, o.organization, true) && compareDeep(code, o.code, true) && compareDeep(specialty, o.specialty, true)
           && compareDeep(identifier, o.identifier, true) && compareDeep(telecom, o.telecom, true) && compareDeep(period, o.period, true)
           && compareDeep(location, o.location, true) && compareDeep(healthcareService, o.healthcareService, true)
           && compareDeep(endpoint, o.endpoint, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof PractitionerRoleComponent))
          return false;
        PractitionerRoleComponent o = (PractitionerRoleComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(organization, code, specialty
          , identifier, telecom, period, location, healthcareService, endpoint);
      }

  public String fhirType() {
    return "Practitioner.role";

  }

  }

    @Block()
    public static class PractitionerQualificationComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * An identifier that applies to this person's qualification in this role.
         */
        @Child(name = "identifier", type = {Identifier.class}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="An identifier for this qualification for the practitioner", formalDefinition="An identifier that applies to this person's qualification in this role." )
        protected List<Identifier> identifier;

        /**
         * Coded representation of the qualification.
         */
        @Child(name = "code", type = {CodeableConcept.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Coded representation of the qualification", formalDefinition="Coded representation of the qualification." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/anzsco-occupations")
        protected CodeableConcept code;

        /**
         * Period during which the qualification is valid.
         */
        @Child(name = "period", type = {Period.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Period during which the qualification is valid", formalDefinition="Period during which the qualification is valid." )
        protected Period period;

        /**
         * Organization that regulates and issues the qualification.
         */
        @Child(name = "issuer", type = {Organization.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Organization that regulates and issues the qualification", formalDefinition="Organization that regulates and issues the qualification." )
        protected Reference issuer;

        /**
         * The actual object that is the target of the reference (Organization that regulates and issues the qualification.)
         */
        protected Organization issuerTarget;

        private static final long serialVersionUID = 1095219071L;

    /**
     * Constructor
     */
      public PractitionerQualificationComponent() {
        super();
      }

    /**
     * Constructor
     */
      public PractitionerQualificationComponent(CodeableConcept code) {
        super();
        this.code = code;
      }

        /**
         * @return {@link #identifier} (An identifier that applies to this person's qualification in this role.)
         */
        public List<Identifier> getIdentifier() { 
          if (this.identifier == null)
            this.identifier = new ArrayList<Identifier>();
          return this.identifier;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public PractitionerQualificationComponent setIdentifier(List<Identifier> theIdentifier) { 
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

        public PractitionerQualificationComponent addIdentifier(Identifier t) { //3
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
         * @return {@link #code} (Coded representation of the qualification.)
         */
        public CodeableConcept getCode() { 
          if (this.code == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PractitionerQualificationComponent.code");
            else if (Configuration.doAutoCreate())
              this.code = new CodeableConcept(); // cc
          return this.code;
        }

        public boolean hasCode() { 
          return this.code != null && !this.code.isEmpty();
        }

        /**
         * @param value {@link #code} (Coded representation of the qualification.)
         */
        public PractitionerQualificationComponent setCode(CodeableConcept value) { 
          this.code = value;
          return this;
        }

        /**
         * @return {@link #period} (Period during which the qualification is valid.)
         */
        public Period getPeriod() { 
          if (this.period == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PractitionerQualificationComponent.period");
            else if (Configuration.doAutoCreate())
              this.period = new Period(); // cc
          return this.period;
        }

        public boolean hasPeriod() { 
          return this.period != null && !this.period.isEmpty();
        }

        /**
         * @param value {@link #period} (Period during which the qualification is valid.)
         */
        public PractitionerQualificationComponent setPeriod(Period value) { 
          this.period = value;
          return this;
        }

        /**
         * @return {@link #issuer} (Organization that regulates and issues the qualification.)
         */
        public Reference getIssuer() { 
          if (this.issuer == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PractitionerQualificationComponent.issuer");
            else if (Configuration.doAutoCreate())
              this.issuer = new Reference(); // cc
          return this.issuer;
        }

        public boolean hasIssuer() { 
          return this.issuer != null && !this.issuer.isEmpty();
        }

        /**
         * @param value {@link #issuer} (Organization that regulates and issues the qualification.)
         */
        public PractitionerQualificationComponent setIssuer(Reference value) { 
          this.issuer = value;
          return this;
        }

        /**
         * @return {@link #issuer} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Organization that regulates and issues the qualification.)
         */
        public Organization getIssuerTarget() { 
          if (this.issuerTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PractitionerQualificationComponent.issuer");
            else if (Configuration.doAutoCreate())
              this.issuerTarget = new Organization(); // aa
          return this.issuerTarget;
        }

        /**
         * @param value {@link #issuer} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Organization that regulates and issues the qualification.)
         */
        public PractitionerQualificationComponent setIssuerTarget(Organization value) { 
          this.issuerTarget = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("identifier", "Identifier", "An identifier that applies to this person's qualification in this role.", 0, java.lang.Integer.MAX_VALUE, identifier));
          childrenList.add(new Property("code", "CodeableConcept", "Coded representation of the qualification.", 0, java.lang.Integer.MAX_VALUE, code));
          childrenList.add(new Property("period", "Period", "Period during which the qualification is valid.", 0, java.lang.Integer.MAX_VALUE, period));
          childrenList.add(new Property("issuer", "Reference(Organization)", "Organization that regulates and issues the qualification.", 0, java.lang.Integer.MAX_VALUE, issuer));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case 3059181: /*code*/ return this.code == null ? new Base[0] : new Base[] {this.code}; // CodeableConcept
        case -991726143: /*period*/ return this.period == null ? new Base[0] : new Base[] {this.period}; // Period
        case -1179159879: /*issuer*/ return this.issuer == null ? new Base[0] : new Base[] {this.issuer}; // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.getIdentifier().add(castToIdentifier(value)); // Identifier
          break;
        case 3059181: // code
          this.code = castToCodeableConcept(value); // CodeableConcept
          break;
        case -991726143: // period
          this.period = castToPeriod(value); // Period
          break;
        case -1179159879: // issuer
          this.issuer = castToReference(value); // Reference
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier"))
          this.getIdentifier().add(castToIdentifier(value));
        else if (name.equals("code"))
          this.code = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("period"))
          this.period = castToPeriod(value); // Period
        else if (name.equals("issuer"))
          this.issuer = castToReference(value); // Reference
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return addIdentifier(); // Identifier
        case 3059181:  return getCode(); // CodeableConcept
        case -991726143:  return getPeriod(); // Period
        case -1179159879:  return getIssuer(); // Reference
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("code")) {
          this.code = new CodeableConcept();
          return this.code;
        }
        else if (name.equals("period")) {
          this.period = new Period();
          return this.period;
        }
        else if (name.equals("issuer")) {
          this.issuer = new Reference();
          return this.issuer;
        }
        else
          return super.addChild(name);
      }

      public PractitionerQualificationComponent copy() {
        PractitionerQualificationComponent dst = new PractitionerQualificationComponent();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.code = code == null ? null : code.copy();
        dst.period = period == null ? null : period.copy();
        dst.issuer = issuer == null ? null : issuer.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof PractitionerQualificationComponent))
          return false;
        PractitionerQualificationComponent o = (PractitionerQualificationComponent) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(code, o.code, true) && compareDeep(period, o.period, true)
           && compareDeep(issuer, o.issuer, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof PractitionerQualificationComponent))
          return false;
        PractitionerQualificationComponent o = (PractitionerQualificationComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, code, period
          , issuer);
      }

  public String fhirType() {
    return "Practitioner.qualification";

  }

  }

    /**
     * An identifier that applies to this person in this role.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="A identifier for the person as this agent", formalDefinition="An identifier that applies to this person in this role." )
    protected List<Identifier> identifier;

    /**
     * Whether this practitioner's record is in active use.
     */
    @Child(name = "active", type = {BooleanType.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Whether this practitioner's record is in active use", formalDefinition="Whether this practitioner's record is in active use." )
    protected BooleanType active;

    /**
     * The name(s) associated with the practitioner.
     */
    @Child(name = "name", type = {HumanName.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="The name(s) associated with the practitioner", formalDefinition="The name(s) associated with the practitioner." )
    protected List<HumanName> name;

    /**
     * A contact detail for the practitioner, e.g. a telephone number or an email address.
     */
    @Child(name = "telecom", type = {ContactPoint.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="A contact detail for the practitioner (that apply to all roles)", formalDefinition="A contact detail for the practitioner, e.g. a telephone number or an email address." )
    protected List<ContactPoint> telecom;

    /**
     * Address(es) of the practitioner that are not role specific (typically home address). Work addresses are not typically entered in this property as they are usually role dependent.
     */
    @Child(name = "address", type = {Address.class}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Address(es) of the practitioner that are not role specific (typically home address)", formalDefinition="Address(es) of the practitioner that are not role specific (typically home address). \rWork addresses are not typically entered in this property as they are usually role dependent." )
    protected List<Address> address;

    /**
     * Administrative Gender - the gender that the person is considered to have for administration and record keeping purposes.
     */
    @Child(name = "gender", type = {CodeType.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="male | female | other | unknown", formalDefinition="Administrative Gender - the gender that the person is considered to have for administration and record keeping purposes." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/administrative-gender")
    protected Enumeration<AdministrativeGender> gender;

    /**
     * The date of birth for the practitioner.
     */
    @Child(name = "birthDate", type = {DateType.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The date  on which the practitioner was born", formalDefinition="The date of birth for the practitioner." )
    protected DateType birthDate;

    /**
     * Image of the person.
     */
    @Child(name = "photo", type = {Attachment.class}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Image of the person", formalDefinition="Image of the person." )
    protected List<Attachment> photo;

    /**
     * The list of roles/organizations that the practitioner is associated with.
     */
    @Child(name = "role", type = {}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Roles/organizations the practitioner is associated with", formalDefinition="The list of roles/organizations that the practitioner is associated with." )
    protected List<PractitionerRoleComponent> role;

    /**
     * Qualifications obtained by training and certification.
     */
    @Child(name = "qualification", type = {}, order=9, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Qualifications obtained by training and certification", formalDefinition="Qualifications obtained by training and certification." )
    protected List<PractitionerQualificationComponent> qualification;

    /**
     * A language the practitioner is able to use in patient communication.
     */
    @Child(name = "communication", type = {CodeableConcept.class}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="A language the practitioner is able to use in patient communication", formalDefinition="A language the practitioner is able to use in patient communication." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/languages")
    protected List<CodeableConcept> communication;

    private static final long serialVersionUID = -983959994L;

  /**
   * Constructor
   */
    public Practitioner() {
      super();
    }

    /**
     * @return {@link #identifier} (An identifier that applies to this person in this role.)
     */
    public List<Identifier> getIdentifier() { 
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      return this.identifier;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Practitioner setIdentifier(List<Identifier> theIdentifier) { 
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

    public Practitioner addIdentifier(Identifier t) { //3
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
     * @return {@link #active} (Whether this practitioner's record is in active use.). This is the underlying object with id, value and extensions. The accessor "getActive" gives direct access to the value
     */
    public BooleanType getActiveElement() { 
      if (this.active == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Practitioner.active");
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
     * @param value {@link #active} (Whether this practitioner's record is in active use.). This is the underlying object with id, value and extensions. The accessor "getActive" gives direct access to the value
     */
    public Practitioner setActiveElement(BooleanType value) { 
      this.active = value;
      return this;
    }

    /**
     * @return Whether this practitioner's record is in active use.
     */
    public boolean getActive() { 
      return this.active == null || this.active.isEmpty() ? false : this.active.getValue();
    }

    /**
     * @param value Whether this practitioner's record is in active use.
     */
    public Practitioner setActive(boolean value) { 
        if (this.active == null)
          this.active = new BooleanType();
        this.active.setValue(value);
      return this;
    }

    /**
     * @return {@link #name} (The name(s) associated with the practitioner.)
     */
    public List<HumanName> getName() { 
      if (this.name == null)
        this.name = new ArrayList<HumanName>();
      return this.name;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Practitioner setName(List<HumanName> theName) { 
      this.name = theName;
      return this;
    }

    public boolean hasName() { 
      if (this.name == null)
        return false;
      for (HumanName item : this.name)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public HumanName addName() { //3
      HumanName t = new HumanName();
      if (this.name == null)
        this.name = new ArrayList<HumanName>();
      this.name.add(t);
      return t;
    }

    public Practitioner addName(HumanName t) { //3
      if (t == null)
        return this;
      if (this.name == null)
        this.name = new ArrayList<HumanName>();
      this.name.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #name}, creating it if it does not already exist
     */
    public HumanName getNameFirstRep() { 
      if (getName().isEmpty()) {
        addName();
      }
      return getName().get(0);
    }

    /**
     * @return {@link #telecom} (A contact detail for the practitioner, e.g. a telephone number or an email address.)
     */
    public List<ContactPoint> getTelecom() { 
      if (this.telecom == null)
        this.telecom = new ArrayList<ContactPoint>();
      return this.telecom;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Practitioner setTelecom(List<ContactPoint> theTelecom) { 
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

    public Practitioner addTelecom(ContactPoint t) { //3
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
     * @return {@link #address} (Address(es) of the practitioner that are not role specific (typically home address). Work addresses are not typically entered in this property as they are usually role dependent.)
     */
    public List<Address> getAddress() { 
      if (this.address == null)
        this.address = new ArrayList<Address>();
      return this.address;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Practitioner setAddress(List<Address> theAddress) { 
      this.address = theAddress;
      return this;
    }

    public boolean hasAddress() { 
      if (this.address == null)
        return false;
      for (Address item : this.address)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Address addAddress() { //3
      Address t = new Address();
      if (this.address == null)
        this.address = new ArrayList<Address>();
      this.address.add(t);
      return t;
    }

    public Practitioner addAddress(Address t) { //3
      if (t == null)
        return this;
      if (this.address == null)
        this.address = new ArrayList<Address>();
      this.address.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #address}, creating it if it does not already exist
     */
    public Address getAddressFirstRep() { 
      if (getAddress().isEmpty()) {
        addAddress();
      }
      return getAddress().get(0);
    }

    /**
     * @return {@link #gender} (Administrative Gender - the gender that the person is considered to have for administration and record keeping purposes.). This is the underlying object with id, value and extensions. The accessor "getGender" gives direct access to the value
     */
    public Enumeration<AdministrativeGender> getGenderElement() { 
      if (this.gender == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Practitioner.gender");
        else if (Configuration.doAutoCreate())
          this.gender = new Enumeration<AdministrativeGender>(new AdministrativeGenderEnumFactory()); // bb
      return this.gender;
    }

    public boolean hasGenderElement() { 
      return this.gender != null && !this.gender.isEmpty();
    }

    public boolean hasGender() { 
      return this.gender != null && !this.gender.isEmpty();
    }

    /**
     * @param value {@link #gender} (Administrative Gender - the gender that the person is considered to have for administration and record keeping purposes.). This is the underlying object with id, value and extensions. The accessor "getGender" gives direct access to the value
     */
    public Practitioner setGenderElement(Enumeration<AdministrativeGender> value) { 
      this.gender = value;
      return this;
    }

    /**
     * @return Administrative Gender - the gender that the person is considered to have for administration and record keeping purposes.
     */
    public AdministrativeGender getGender() { 
      return this.gender == null ? null : this.gender.getValue();
    }

    /**
     * @param value Administrative Gender - the gender that the person is considered to have for administration and record keeping purposes.
     */
    public Practitioner setGender(AdministrativeGender value) { 
      if (value == null)
        this.gender = null;
      else {
        if (this.gender == null)
          this.gender = new Enumeration<AdministrativeGender>(new AdministrativeGenderEnumFactory());
        this.gender.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #birthDate} (The date of birth for the practitioner.). This is the underlying object with id, value and extensions. The accessor "getBirthDate" gives direct access to the value
     */
    public DateType getBirthDateElement() { 
      if (this.birthDate == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Practitioner.birthDate");
        else if (Configuration.doAutoCreate())
          this.birthDate = new DateType(); // bb
      return this.birthDate;
    }

    public boolean hasBirthDateElement() { 
      return this.birthDate != null && !this.birthDate.isEmpty();
    }

    public boolean hasBirthDate() { 
      return this.birthDate != null && !this.birthDate.isEmpty();
    }

    /**
     * @param value {@link #birthDate} (The date of birth for the practitioner.). This is the underlying object with id, value and extensions. The accessor "getBirthDate" gives direct access to the value
     */
    public Practitioner setBirthDateElement(DateType value) { 
      this.birthDate = value;
      return this;
    }

    /**
     * @return The date of birth for the practitioner.
     */
    public Date getBirthDate() { 
      return this.birthDate == null ? null : this.birthDate.getValue();
    }

    /**
     * @param value The date of birth for the practitioner.
     */
    public Practitioner setBirthDate(Date value) { 
      if (value == null)
        this.birthDate = null;
      else {
        if (this.birthDate == null)
          this.birthDate = new DateType();
        this.birthDate.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #photo} (Image of the person.)
     */
    public List<Attachment> getPhoto() { 
      if (this.photo == null)
        this.photo = new ArrayList<Attachment>();
      return this.photo;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Practitioner setPhoto(List<Attachment> thePhoto) { 
      this.photo = thePhoto;
      return this;
    }

    public boolean hasPhoto() { 
      if (this.photo == null)
        return false;
      for (Attachment item : this.photo)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Attachment addPhoto() { //3
      Attachment t = new Attachment();
      if (this.photo == null)
        this.photo = new ArrayList<Attachment>();
      this.photo.add(t);
      return t;
    }

    public Practitioner addPhoto(Attachment t) { //3
      if (t == null)
        return this;
      if (this.photo == null)
        this.photo = new ArrayList<Attachment>();
      this.photo.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #photo}, creating it if it does not already exist
     */
    public Attachment getPhotoFirstRep() { 
      if (getPhoto().isEmpty()) {
        addPhoto();
      }
      return getPhoto().get(0);
    }

    /**
     * @return {@link #role} (The list of roles/organizations that the practitioner is associated with.)
     */
    public List<PractitionerRoleComponent> getRole() { 
      if (this.role == null)
        this.role = new ArrayList<PractitionerRoleComponent>();
      return this.role;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Practitioner setRole(List<PractitionerRoleComponent> theRole) { 
      this.role = theRole;
      return this;
    }

    public boolean hasRole() { 
      if (this.role == null)
        return false;
      for (PractitionerRoleComponent item : this.role)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public PractitionerRoleComponent addRole() { //3
      PractitionerRoleComponent t = new PractitionerRoleComponent();
      if (this.role == null)
        this.role = new ArrayList<PractitionerRoleComponent>();
      this.role.add(t);
      return t;
    }

    public Practitioner addRole(PractitionerRoleComponent t) { //3
      if (t == null)
        return this;
      if (this.role == null)
        this.role = new ArrayList<PractitionerRoleComponent>();
      this.role.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #role}, creating it if it does not already exist
     */
    public PractitionerRoleComponent getRoleFirstRep() { 
      if (getRole().isEmpty()) {
        addRole();
      }
      return getRole().get(0);
    }

    /**
     * @return {@link #qualification} (Qualifications obtained by training and certification.)
     */
    public List<PractitionerQualificationComponent> getQualification() { 
      if (this.qualification == null)
        this.qualification = new ArrayList<PractitionerQualificationComponent>();
      return this.qualification;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Practitioner setQualification(List<PractitionerQualificationComponent> theQualification) { 
      this.qualification = theQualification;
      return this;
    }

    public boolean hasQualification() { 
      if (this.qualification == null)
        return false;
      for (PractitionerQualificationComponent item : this.qualification)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public PractitionerQualificationComponent addQualification() { //3
      PractitionerQualificationComponent t = new PractitionerQualificationComponent();
      if (this.qualification == null)
        this.qualification = new ArrayList<PractitionerQualificationComponent>();
      this.qualification.add(t);
      return t;
    }

    public Practitioner addQualification(PractitionerQualificationComponent t) { //3
      if (t == null)
        return this;
      if (this.qualification == null)
        this.qualification = new ArrayList<PractitionerQualificationComponent>();
      this.qualification.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #qualification}, creating it if it does not already exist
     */
    public PractitionerQualificationComponent getQualificationFirstRep() { 
      if (getQualification().isEmpty()) {
        addQualification();
      }
      return getQualification().get(0);
    }

    /**
     * @return {@link #communication} (A language the practitioner is able to use in patient communication.)
     */
    public List<CodeableConcept> getCommunication() { 
      if (this.communication == null)
        this.communication = new ArrayList<CodeableConcept>();
      return this.communication;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Practitioner setCommunication(List<CodeableConcept> theCommunication) { 
      this.communication = theCommunication;
      return this;
    }

    public boolean hasCommunication() { 
      if (this.communication == null)
        return false;
      for (CodeableConcept item : this.communication)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addCommunication() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.communication == null)
        this.communication = new ArrayList<CodeableConcept>();
      this.communication.add(t);
      return t;
    }

    public Practitioner addCommunication(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.communication == null)
        this.communication = new ArrayList<CodeableConcept>();
      this.communication.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #communication}, creating it if it does not already exist
     */
    public CodeableConcept getCommunicationFirstRep() { 
      if (getCommunication().isEmpty()) {
        addCommunication();
      }
      return getCommunication().get(0);
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "An identifier that applies to this person in this role.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("active", "boolean", "Whether this practitioner's record is in active use.", 0, java.lang.Integer.MAX_VALUE, active));
        childrenList.add(new Property("name", "HumanName", "The name(s) associated with the practitioner.", 0, java.lang.Integer.MAX_VALUE, name));
        childrenList.add(new Property("telecom", "ContactPoint", "A contact detail for the practitioner, e.g. a telephone number or an email address.", 0, java.lang.Integer.MAX_VALUE, telecom));
        childrenList.add(new Property("address", "Address", "Address(es) of the practitioner that are not role specific (typically home address). \rWork addresses are not typically entered in this property as they are usually role dependent.", 0, java.lang.Integer.MAX_VALUE, address));
        childrenList.add(new Property("gender", "code", "Administrative Gender - the gender that the person is considered to have for administration and record keeping purposes.", 0, java.lang.Integer.MAX_VALUE, gender));
        childrenList.add(new Property("birthDate", "date", "The date of birth for the practitioner.", 0, java.lang.Integer.MAX_VALUE, birthDate));
        childrenList.add(new Property("photo", "Attachment", "Image of the person.", 0, java.lang.Integer.MAX_VALUE, photo));
        childrenList.add(new Property("role", "", "The list of roles/organizations that the practitioner is associated with.", 0, java.lang.Integer.MAX_VALUE, role));
        childrenList.add(new Property("qualification", "", "Qualifications obtained by training and certification.", 0, java.lang.Integer.MAX_VALUE, qualification));
        childrenList.add(new Property("communication", "CodeableConcept", "A language the practitioner is able to use in patient communication.", 0, java.lang.Integer.MAX_VALUE, communication));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case -1422950650: /*active*/ return this.active == null ? new Base[0] : new Base[] {this.active}; // BooleanType
        case 3373707: /*name*/ return this.name == null ? new Base[0] : this.name.toArray(new Base[this.name.size()]); // HumanName
        case -1429363305: /*telecom*/ return this.telecom == null ? new Base[0] : this.telecom.toArray(new Base[this.telecom.size()]); // ContactPoint
        case -1147692044: /*address*/ return this.address == null ? new Base[0] : this.address.toArray(new Base[this.address.size()]); // Address
        case -1249512767: /*gender*/ return this.gender == null ? new Base[0] : new Base[] {this.gender}; // Enumeration<AdministrativeGender>
        case -1210031859: /*birthDate*/ return this.birthDate == null ? new Base[0] : new Base[] {this.birthDate}; // DateType
        case 106642994: /*photo*/ return this.photo == null ? new Base[0] : this.photo.toArray(new Base[this.photo.size()]); // Attachment
        case 3506294: /*role*/ return this.role == null ? new Base[0] : this.role.toArray(new Base[this.role.size()]); // PractitionerRoleComponent
        case -631333393: /*qualification*/ return this.qualification == null ? new Base[0] : this.qualification.toArray(new Base[this.qualification.size()]); // PractitionerQualificationComponent
        case -1035284522: /*communication*/ return this.communication == null ? new Base[0] : this.communication.toArray(new Base[this.communication.size()]); // CodeableConcept
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.getIdentifier().add(castToIdentifier(value)); // Identifier
          break;
        case -1422950650: // active
          this.active = castToBoolean(value); // BooleanType
          break;
        case 3373707: // name
          this.getName().add(castToHumanName(value)); // HumanName
          break;
        case -1429363305: // telecom
          this.getTelecom().add(castToContactPoint(value)); // ContactPoint
          break;
        case -1147692044: // address
          this.getAddress().add(castToAddress(value)); // Address
          break;
        case -1249512767: // gender
          this.gender = new AdministrativeGenderEnumFactory().fromType(value); // Enumeration<AdministrativeGender>
          break;
        case -1210031859: // birthDate
          this.birthDate = castToDate(value); // DateType
          break;
        case 106642994: // photo
          this.getPhoto().add(castToAttachment(value)); // Attachment
          break;
        case 3506294: // role
          this.getRole().add((PractitionerRoleComponent) value); // PractitionerRoleComponent
          break;
        case -631333393: // qualification
          this.getQualification().add((PractitionerQualificationComponent) value); // PractitionerQualificationComponent
          break;
        case -1035284522: // communication
          this.getCommunication().add(castToCodeableConcept(value)); // CodeableConcept
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier"))
          this.getIdentifier().add(castToIdentifier(value));
        else if (name.equals("active"))
          this.active = castToBoolean(value); // BooleanType
        else if (name.equals("name"))
          this.getName().add(castToHumanName(value));
        else if (name.equals("telecom"))
          this.getTelecom().add(castToContactPoint(value));
        else if (name.equals("address"))
          this.getAddress().add(castToAddress(value));
        else if (name.equals("gender"))
          this.gender = new AdministrativeGenderEnumFactory().fromType(value); // Enumeration<AdministrativeGender>
        else if (name.equals("birthDate"))
          this.birthDate = castToDate(value); // DateType
        else if (name.equals("photo"))
          this.getPhoto().add(castToAttachment(value));
        else if (name.equals("role"))
          this.getRole().add((PractitionerRoleComponent) value);
        else if (name.equals("qualification"))
          this.getQualification().add((PractitionerQualificationComponent) value);
        else if (name.equals("communication"))
          this.getCommunication().add(castToCodeableConcept(value));
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return addIdentifier(); // Identifier
        case -1422950650: throw new FHIRException("Cannot make property active as it is not a complex type"); // BooleanType
        case 3373707:  return addName(); // HumanName
        case -1429363305:  return addTelecom(); // ContactPoint
        case -1147692044:  return addAddress(); // Address
        case -1249512767: throw new FHIRException("Cannot make property gender as it is not a complex type"); // Enumeration<AdministrativeGender>
        case -1210031859: throw new FHIRException("Cannot make property birthDate as it is not a complex type"); // DateType
        case 106642994:  return addPhoto(); // Attachment
        case 3506294:  return addRole(); // PractitionerRoleComponent
        case -631333393:  return addQualification(); // PractitionerQualificationComponent
        case -1035284522:  return addCommunication(); // CodeableConcept
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("active")) {
          throw new FHIRException("Cannot call addChild on a primitive type Practitioner.active");
        }
        else if (name.equals("name")) {
          return addName();
        }
        else if (name.equals("telecom")) {
          return addTelecom();
        }
        else if (name.equals("address")) {
          return addAddress();
        }
        else if (name.equals("gender")) {
          throw new FHIRException("Cannot call addChild on a primitive type Practitioner.gender");
        }
        else if (name.equals("birthDate")) {
          throw new FHIRException("Cannot call addChild on a primitive type Practitioner.birthDate");
        }
        else if (name.equals("photo")) {
          return addPhoto();
        }
        else if (name.equals("role")) {
          return addRole();
        }
        else if (name.equals("qualification")) {
          return addQualification();
        }
        else if (name.equals("communication")) {
          return addCommunication();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "Practitioner";

  }

      public Practitioner copy() {
        Practitioner dst = new Practitioner();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.active = active == null ? null : active.copy();
        if (name != null) {
          dst.name = new ArrayList<HumanName>();
          for (HumanName i : name)
            dst.name.add(i.copy());
        };
        if (telecom != null) {
          dst.telecom = new ArrayList<ContactPoint>();
          for (ContactPoint i : telecom)
            dst.telecom.add(i.copy());
        };
        if (address != null) {
          dst.address = new ArrayList<Address>();
          for (Address i : address)
            dst.address.add(i.copy());
        };
        dst.gender = gender == null ? null : gender.copy();
        dst.birthDate = birthDate == null ? null : birthDate.copy();
        if (photo != null) {
          dst.photo = new ArrayList<Attachment>();
          for (Attachment i : photo)
            dst.photo.add(i.copy());
        };
        if (role != null) {
          dst.role = new ArrayList<PractitionerRoleComponent>();
          for (PractitionerRoleComponent i : role)
            dst.role.add(i.copy());
        };
        if (qualification != null) {
          dst.qualification = new ArrayList<PractitionerQualificationComponent>();
          for (PractitionerQualificationComponent i : qualification)
            dst.qualification.add(i.copy());
        };
        if (communication != null) {
          dst.communication = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : communication)
            dst.communication.add(i.copy());
        };
        return dst;
      }

      protected Practitioner typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof Practitioner))
          return false;
        Practitioner o = (Practitioner) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(active, o.active, true) && compareDeep(name, o.name, true)
           && compareDeep(telecom, o.telecom, true) && compareDeep(address, o.address, true) && compareDeep(gender, o.gender, true)
           && compareDeep(birthDate, o.birthDate, true) && compareDeep(photo, o.photo, true) && compareDeep(role, o.role, true)
           && compareDeep(qualification, o.qualification, true) && compareDeep(communication, o.communication, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Practitioner))
          return false;
        Practitioner o = (Practitioner) other;
        return compareValues(active, o.active, true) && compareValues(gender, o.gender, true) && compareValues(birthDate, o.birthDate, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, active, name
          , telecom, address, gender, birthDate, photo, role, qualification, communication
          );
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Practitioner;
   }

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>A practitioner's Identifier</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Practitioner.identifier, Practitioner.role.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="Practitioner.identifier | Practitioner.role.identifier", description="A practitioner's Identifier", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>A practitioner's Identifier</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Practitioner.identifier, Practitioner.role.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>given</b>
   * <p>
   * Description: <b>A portion of the given name</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Practitioner.name.given</b><br>
   * </p>
   */
  @SearchParamDefinition(name="given", path="Practitioner.name.given", description="A portion of the given name", type="string" )
  public static final String SP_GIVEN = "given";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>given</b>
   * <p>
   * Description: <b>A portion of the given name</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Practitioner.name.given</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam GIVEN = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_GIVEN);

 /**
   * Search parameter: <b>specialty</b>
   * <p>
   * Description: <b>The practitioner has this specialty at an organization</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Practitioner.role.specialty</b><br>
   * </p>
   */
  @SearchParamDefinition(name="specialty", path="Practitioner.role.specialty", description="The practitioner has this specialty at an organization", type="token" )
  public static final String SP_SPECIALTY = "specialty";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>specialty</b>
   * <p>
   * Description: <b>The practitioner has this specialty at an organization</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Practitioner.role.specialty</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam SPECIALTY = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_SPECIALTY);

 /**
   * Search parameter: <b>address</b>
   * <p>
   * Description: <b>A server defined search that may match any of the string fields in the Address, including line, city, state, country, postalCode, and/or text</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Practitioner.address</b><br>
   * </p>
   */
  @SearchParamDefinition(name="address", path="Practitioner.address", description="A server defined search that may match any of the string fields in the Address, including line, city, state, country, postalCode, and/or text", type="string" )
  public static final String SP_ADDRESS = "address";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>address</b>
   * <p>
   * Description: <b>A server defined search that may match any of the string fields in the Address, including line, city, state, country, postalCode, and/or text</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Practitioner.address</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam ADDRESS = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_ADDRESS);

 /**
   * Search parameter: <b>role</b>
   * <p>
   * Description: <b>The practitioner can perform this role at for the organization</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Practitioner.role.code</b><br>
   * </p>
   */
  @SearchParamDefinition(name="role", path="Practitioner.role.code", description="The practitioner can perform this role at for the organization", type="token" )
  public static final String SP_ROLE = "role";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>role</b>
   * <p>
   * Description: <b>The practitioner can perform this role at for the organization</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Practitioner.role.code</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam ROLE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_ROLE);

 /**
   * Search parameter: <b>address-state</b>
   * <p>
   * Description: <b>A state specified in an address</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Practitioner.address.state</b><br>
   * </p>
   */
  @SearchParamDefinition(name="address-state", path="Practitioner.address.state", description="A state specified in an address", type="string" )
  public static final String SP_ADDRESS_STATE = "address-state";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>address-state</b>
   * <p>
   * Description: <b>A state specified in an address</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Practitioner.address.state</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam ADDRESS_STATE = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_ADDRESS_STATE);

 /**
   * Search parameter: <b>gender</b>
   * <p>
   * Description: <b>Gender of the practitioner</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Practitioner.gender</b><br>
   * </p>
   */
  @SearchParamDefinition(name="gender", path="Practitioner.gender", description="Gender of the practitioner", type="token" )
  public static final String SP_GENDER = "gender";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>gender</b>
   * <p>
   * Description: <b>Gender of the practitioner</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Practitioner.gender</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam GENDER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_GENDER);

 /**
   * Search parameter: <b>active</b>
   * <p>
   * Description: <b>Whether the practitioner record is active</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Practitioner.active</b><br>
   * </p>
   */
  @SearchParamDefinition(name="active", path="Practitioner.active", description="Whether the practitioner record is active", type="token" )
  public static final String SP_ACTIVE = "active";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>active</b>
   * <p>
   * Description: <b>Whether the practitioner record is active</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Practitioner.active</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam ACTIVE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_ACTIVE);

 /**
   * Search parameter: <b>address-postalcode</b>
   * <p>
   * Description: <b>A postalCode specified in an address</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Practitioner.address.postalCode</b><br>
   * </p>
   */
  @SearchParamDefinition(name="address-postalcode", path="Practitioner.address.postalCode", description="A postalCode specified in an address", type="string" )
  public static final String SP_ADDRESS_POSTALCODE = "address-postalcode";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>address-postalcode</b>
   * <p>
   * Description: <b>A postalCode specified in an address</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Practitioner.address.postalCode</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam ADDRESS_POSTALCODE = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_ADDRESS_POSTALCODE);

 /**
   * Search parameter: <b>address-country</b>
   * <p>
   * Description: <b>A country specified in an address</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Practitioner.address.country</b><br>
   * </p>
   */
  @SearchParamDefinition(name="address-country", path="Practitioner.address.country", description="A country specified in an address", type="string" )
  public static final String SP_ADDRESS_COUNTRY = "address-country";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>address-country</b>
   * <p>
   * Description: <b>A country specified in an address</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Practitioner.address.country</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam ADDRESS_COUNTRY = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_ADDRESS_COUNTRY);

 /**
   * Search parameter: <b>phonetic</b>
   * <p>
   * Description: <b>A portion of either family or given name using some kind of phonetic matching algorithm</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Practitioner.name</b><br>
   * </p>
   */
  @SearchParamDefinition(name="phonetic", path="Practitioner.name", description="A portion of either family or given name using some kind of phonetic matching algorithm", type="string" )
  public static final String SP_PHONETIC = "phonetic";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>phonetic</b>
   * <p>
   * Description: <b>A portion of either family or given name using some kind of phonetic matching algorithm</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Practitioner.name</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam PHONETIC = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_PHONETIC);

 /**
   * Search parameter: <b>phone</b>
   * <p>
   * Description: <b>A value in a phone contact</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Practitioner.telecom(system=phone), Practitioner.role.telecom(system=phone)</b><br>
   * </p>
   */
  @SearchParamDefinition(name="phone", path="Practitioner.telecom.where(system='phone') or Practitioner.role.telecom.where(system='phone')", description="A value in a phone contact", type="token" )
  public static final String SP_PHONE = "phone";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>phone</b>
   * <p>
   * Description: <b>A value in a phone contact</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Practitioner.telecom(system=phone), Practitioner.role.telecom(system=phone)</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam PHONE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_PHONE);

 /**
   * Search parameter: <b>organization</b>
   * <p>
   * Description: <b>The identity of the organization the practitioner represents / acts on behalf of</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Practitioner.role.organization</b><br>
   * </p>
   */
  @SearchParamDefinition(name="organization", path="Practitioner.role.organization", description="The identity of the organization the practitioner represents / acts on behalf of", type="reference", target={Organization.class } )
  public static final String SP_ORGANIZATION = "organization";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>organization</b>
   * <p>
   * Description: <b>The identity of the organization the practitioner represents / acts on behalf of</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Practitioner.role.organization</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam ORGANIZATION = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_ORGANIZATION);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Practitioner:organization</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_ORGANIZATION = new ca.uhn.fhir.model.api.Include("Practitioner:organization").toLocked();

 /**
   * Search parameter: <b>name</b>
   * <p>
   * Description: <b>A server defined search that may match any of the string fields in the HumanName, including family, give, prefix, suffix, suffix, and/or text</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Practitioner.name</b><br>
   * </p>
   */
  @SearchParamDefinition(name="name", path="Practitioner.name", description="A server defined search that may match any of the string fields in the HumanName, including family, give, prefix, suffix, suffix, and/or text", type="string" )
  public static final String SP_NAME = "name";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>name</b>
   * <p>
   * Description: <b>A server defined search that may match any of the string fields in the HumanName, including family, give, prefix, suffix, suffix, and/or text</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Practitioner.name</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam NAME = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_NAME);

 /**
   * Search parameter: <b>address-use</b>
   * <p>
   * Description: <b>A use code specified in an address</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Practitioner.address.use</b><br>
   * </p>
   */
  @SearchParamDefinition(name="address-use", path="Practitioner.address.use", description="A use code specified in an address", type="token" )
  public static final String SP_ADDRESS_USE = "address-use";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>address-use</b>
   * <p>
   * Description: <b>A use code specified in an address</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Practitioner.address.use</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam ADDRESS_USE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_ADDRESS_USE);

 /**
   * Search parameter: <b>telecom</b>
   * <p>
   * Description: <b>The value in any kind of contact</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Practitioner.telecom, Practitioner.role.telecom</b><br>
   * </p>
   */
  @SearchParamDefinition(name="telecom", path="Practitioner.telecom | Practitioner.role.telecom", description="The value in any kind of contact", type="token" )
  public static final String SP_TELECOM = "telecom";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>telecom</b>
   * <p>
   * Description: <b>The value in any kind of contact</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Practitioner.telecom, Practitioner.role.telecom</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam TELECOM = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_TELECOM);

 /**
   * Search parameter: <b>location</b>
   * <p>
   * Description: <b>One of the locations at which this practitioner provides care</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Practitioner.role.location</b><br>
   * </p>
   */
  @SearchParamDefinition(name="location", path="Practitioner.role.location", description="One of the locations at which this practitioner provides care", type="reference", target={Location.class } )
  public static final String SP_LOCATION = "location";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>location</b>
   * <p>
   * Description: <b>One of the locations at which this practitioner provides care</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Practitioner.role.location</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam LOCATION = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_LOCATION);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Practitioner:location</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_LOCATION = new ca.uhn.fhir.model.api.Include("Practitioner:location").toLocked();

 /**
   * Search parameter: <b>family</b>
   * <p>
   * Description: <b>A portion of the family name</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Practitioner.name.family</b><br>
   * </p>
   */
  @SearchParamDefinition(name="family", path="Practitioner.name.family", description="A portion of the family name", type="string" )
  public static final String SP_FAMILY = "family";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>family</b>
   * <p>
   * Description: <b>A portion of the family name</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Practitioner.name.family</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam FAMILY = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_FAMILY);

 /**
   * Search parameter: <b>address-city</b>
   * <p>
   * Description: <b>A city specified in an address</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Practitioner.address.city</b><br>
   * </p>
   */
  @SearchParamDefinition(name="address-city", path="Practitioner.address.city", description="A city specified in an address", type="string" )
  public static final String SP_ADDRESS_CITY = "address-city";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>address-city</b>
   * <p>
   * Description: <b>A city specified in an address</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Practitioner.address.city</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam ADDRESS_CITY = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_ADDRESS_CITY);

 /**
   * Search parameter: <b>communication</b>
   * <p>
   * Description: <b>One of the languages that the practitioner can communicate with</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Practitioner.communication</b><br>
   * </p>
   */
  @SearchParamDefinition(name="communication", path="Practitioner.communication", description="One of the languages that the practitioner can communicate with", type="token" )
  public static final String SP_COMMUNICATION = "communication";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>communication</b>
   * <p>
   * Description: <b>One of the languages that the practitioner can communicate with</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Practitioner.communication</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam COMMUNICATION = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_COMMUNICATION);

 /**
   * Search parameter: <b>email</b>
   * <p>
   * Description: <b>A value in an email contact</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Practitioner.telecom(system=email), Practitioner.role.telecom(system=email)</b><br>
   * </p>
   */
  @SearchParamDefinition(name="email", path="Practitioner.telecom.where(system='email') or Practitioner.role.telecom.where(system='email')", description="A value in an email contact", type="token" )
  public static final String SP_EMAIL = "email";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>email</b>
   * <p>
   * Description: <b>A value in an email contact</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Practitioner.telecom(system=email), Practitioner.role.telecom(system=email)</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam EMAIL = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_EMAIL);


}

