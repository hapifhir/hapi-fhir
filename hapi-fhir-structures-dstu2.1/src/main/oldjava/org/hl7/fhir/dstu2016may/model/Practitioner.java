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

import org.hl7.fhir.dstu2016may.model.Enumerations.AdministrativeGender;
import org.hl7.fhir.dstu2016may.model.Enumerations.AdministrativeGenderEnumFactory;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseBackboneElement;

import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
/**
 * A person who is directly or indirectly involved in the provisioning of healthcare.
 */
@ResourceDef(name="Practitioner", profile="http://hl7.org/fhir/Profile/Practitioner")
public class Practitioner extends DomainResource {

    @Block()
    public static class PractitionerPractitionerRoleComponent extends BackboneElement implements IBaseBackboneElement {
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
        @Child(name = "role", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Roles which this practitioner may perform", formalDefinition="Roles which this practitioner is authorized to perform for the organization." )
        protected CodeableConcept role;

        /**
         * Specific specialty of the practitioner.
         */
        @Child(name = "specialty", type = {CodeableConcept.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Specific specialty of the practitioner", formalDefinition="Specific specialty of the practitioner." )
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


        private static final long serialVersionUID = -2082448551L;

    /**
     * Constructor
     */
      public PractitionerPractitionerRoleComponent() {
        super();
      }

        /**
         * @return {@link #organization} (The organization where the Practitioner performs the roles associated.)
         */
        public Reference getOrganization() { 
          if (this.organization == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PractitionerPractitionerRoleComponent.organization");
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
        public PractitionerPractitionerRoleComponent setOrganization(Reference value) { 
          this.organization = value;
          return this;
        }

        /**
         * @return {@link #organization} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The organization where the Practitioner performs the roles associated.)
         */
        public Organization getOrganizationTarget() { 
          if (this.organizationTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PractitionerPractitionerRoleComponent.organization");
            else if (Configuration.doAutoCreate())
              this.organizationTarget = new Organization(); // aa
          return this.organizationTarget;
        }

        /**
         * @param value {@link #organization} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The organization where the Practitioner performs the roles associated.)
         */
        public PractitionerPractitionerRoleComponent setOrganizationTarget(Organization value) { 
          this.organizationTarget = value;
          return this;
        }

        /**
         * @return {@link #role} (Roles which this practitioner is authorized to perform for the organization.)
         */
        public CodeableConcept getRole() { 
          if (this.role == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PractitionerPractitionerRoleComponent.role");
            else if (Configuration.doAutoCreate())
              this.role = new CodeableConcept(); // cc
          return this.role;
        }

        public boolean hasRole() { 
          return this.role != null && !this.role.isEmpty();
        }

        /**
         * @param value {@link #role} (Roles which this practitioner is authorized to perform for the organization.)
         */
        public PractitionerPractitionerRoleComponent setRole(CodeableConcept value) { 
          this.role = value;
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

        public boolean hasSpecialty() { 
          if (this.specialty == null)
            return false;
          for (CodeableConcept item : this.specialty)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #specialty} (Specific specialty of the practitioner.)
         */
    // syntactic sugar
        public CodeableConcept addSpecialty() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.specialty == null)
            this.specialty = new ArrayList<CodeableConcept>();
          this.specialty.add(t);
          return t;
        }

    // syntactic sugar
        public PractitionerPractitionerRoleComponent addSpecialty(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.specialty == null)
            this.specialty = new ArrayList<CodeableConcept>();
          this.specialty.add(t);
          return this;
        }

        /**
         * @return {@link #identifier} (Business Identifiers that are specific to a role/location.)
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
         * @return {@link #identifier} (Business Identifiers that are specific to a role/location.)
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
        public PractitionerPractitionerRoleComponent addIdentifier(Identifier t) { //3
          if (t == null)
            return this;
          if (this.identifier == null)
            this.identifier = new ArrayList<Identifier>();
          this.identifier.add(t);
          return this;
        }

        /**
         * @return {@link #telecom} (Contact details that are specific to the role/location/service.)
         */
        public List<ContactPoint> getTelecom() { 
          if (this.telecom == null)
            this.telecom = new ArrayList<ContactPoint>();
          return this.telecom;
        }

        public boolean hasTelecom() { 
          if (this.telecom == null)
            return false;
          for (ContactPoint item : this.telecom)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #telecom} (Contact details that are specific to the role/location/service.)
         */
    // syntactic sugar
        public ContactPoint addTelecom() { //3
          ContactPoint t = new ContactPoint();
          if (this.telecom == null)
            this.telecom = new ArrayList<ContactPoint>();
          this.telecom.add(t);
          return t;
        }

    // syntactic sugar
        public PractitionerPractitionerRoleComponent addTelecom(ContactPoint t) { //3
          if (t == null)
            return this;
          if (this.telecom == null)
            this.telecom = new ArrayList<ContactPoint>();
          this.telecom.add(t);
          return this;
        }

        /**
         * @return {@link #period} (The period during which the person is authorized to act as a practitioner in these role(s) for the organization.)
         */
        public Period getPeriod() { 
          if (this.period == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PractitionerPractitionerRoleComponent.period");
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
        public PractitionerPractitionerRoleComponent setPeriod(Period value) { 
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

        public boolean hasLocation() { 
          if (this.location == null)
            return false;
          for (Reference item : this.location)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #location} (The location(s) at which this practitioner provides care.)
         */
    // syntactic sugar
        public Reference addLocation() { //3
          Reference t = new Reference();
          if (this.location == null)
            this.location = new ArrayList<Reference>();
          this.location.add(t);
          return t;
        }

    // syntactic sugar
        public PractitionerPractitionerRoleComponent addLocation(Reference t) { //3
          if (t == null)
            return this;
          if (this.location == null)
            this.location = new ArrayList<Reference>();
          this.location.add(t);
          return this;
        }

        /**
         * @return {@link #location} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. The location(s) at which this practitioner provides care.)
         */
        public List<Location> getLocationTarget() { 
          if (this.locationTarget == null)
            this.locationTarget = new ArrayList<Location>();
          return this.locationTarget;
        }

    // syntactic sugar
        /**
         * @return {@link #location} (Add an actual object that is the target of the reference. The reference library doesn't use these, but you can use this to hold the resources if you resolvethemt. The location(s) at which this practitioner provides care.)
         */
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

        public boolean hasHealthcareService() { 
          if (this.healthcareService == null)
            return false;
          for (Reference item : this.healthcareService)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #healthcareService} (The list of healthcare services that this worker provides for this role's Organization/Location(s).)
         */
    // syntactic sugar
        public Reference addHealthcareService() { //3
          Reference t = new Reference();
          if (this.healthcareService == null)
            this.healthcareService = new ArrayList<Reference>();
          this.healthcareService.add(t);
          return t;
        }

    // syntactic sugar
        public PractitionerPractitionerRoleComponent addHealthcareService(Reference t) { //3
          if (t == null)
            return this;
          if (this.healthcareService == null)
            this.healthcareService = new ArrayList<Reference>();
          this.healthcareService.add(t);
          return this;
        }

        /**
         * @return {@link #healthcareService} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. The list of healthcare services that this worker provides for this role's Organization/Location(s).)
         */
        public List<HealthcareService> getHealthcareServiceTarget() { 
          if (this.healthcareServiceTarget == null)
            this.healthcareServiceTarget = new ArrayList<HealthcareService>();
          return this.healthcareServiceTarget;
        }

    // syntactic sugar
        /**
         * @return {@link #healthcareService} (Add an actual object that is the target of the reference. The reference library doesn't use these, but you can use this to hold the resources if you resolvethemt. The list of healthcare services that this worker provides for this role's Organization/Location(s).)
         */
        public HealthcareService addHealthcareServiceTarget() { 
          HealthcareService r = new HealthcareService();
          if (this.healthcareServiceTarget == null)
            this.healthcareServiceTarget = new ArrayList<HealthcareService>();
          this.healthcareServiceTarget.add(r);
          return r;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("organization", "Reference(Organization)", "The organization where the Practitioner performs the roles associated.", 0, java.lang.Integer.MAX_VALUE, organization));
          childrenList.add(new Property("role", "CodeableConcept", "Roles which this practitioner is authorized to perform for the organization.", 0, java.lang.Integer.MAX_VALUE, role));
          childrenList.add(new Property("specialty", "CodeableConcept", "Specific specialty of the practitioner.", 0, java.lang.Integer.MAX_VALUE, specialty));
          childrenList.add(new Property("identifier", "Identifier", "Business Identifiers that are specific to a role/location.", 0, java.lang.Integer.MAX_VALUE, identifier));
          childrenList.add(new Property("telecom", "ContactPoint", "Contact details that are specific to the role/location/service.", 0, java.lang.Integer.MAX_VALUE, telecom));
          childrenList.add(new Property("period", "Period", "The period during which the person is authorized to act as a practitioner in these role(s) for the organization.", 0, java.lang.Integer.MAX_VALUE, period));
          childrenList.add(new Property("location", "Reference(Location)", "The location(s) at which this practitioner provides care.", 0, java.lang.Integer.MAX_VALUE, location));
          childrenList.add(new Property("healthcareService", "Reference(HealthcareService)", "The list of healthcare services that this worker provides for this role's Organization/Location(s).", 0, java.lang.Integer.MAX_VALUE, healthcareService));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 1178922291: /*organization*/ return this.organization == null ? new Base[0] : new Base[] {this.organization}; // Reference
        case 3506294: /*role*/ return this.role == null ? new Base[0] : new Base[] {this.role}; // CodeableConcept
        case -1694759682: /*specialty*/ return this.specialty == null ? new Base[0] : this.specialty.toArray(new Base[this.specialty.size()]); // CodeableConcept
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case -1429363305: /*telecom*/ return this.telecom == null ? new Base[0] : this.telecom.toArray(new Base[this.telecom.size()]); // ContactPoint
        case -991726143: /*period*/ return this.period == null ? new Base[0] : new Base[] {this.period}; // Period
        case 1901043637: /*location*/ return this.location == null ? new Base[0] : this.location.toArray(new Base[this.location.size()]); // Reference
        case 1289661064: /*healthcareService*/ return this.healthcareService == null ? new Base[0] : this.healthcareService.toArray(new Base[this.healthcareService.size()]); // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 1178922291: // organization
          this.organization = castToReference(value); // Reference
          break;
        case 3506294: // role
          this.role = castToCodeableConcept(value); // CodeableConcept
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
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("organization"))
          this.organization = castToReference(value); // Reference
        else if (name.equals("role"))
          this.role = castToCodeableConcept(value); // CodeableConcept
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
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1178922291:  return getOrganization(); // Reference
        case 3506294:  return getRole(); // CodeableConcept
        case -1694759682:  return addSpecialty(); // CodeableConcept
        case -1618432855:  return addIdentifier(); // Identifier
        case -1429363305:  return addTelecom(); // ContactPoint
        case -991726143:  return getPeriod(); // Period
        case 1901043637:  return addLocation(); // Reference
        case 1289661064:  return addHealthcareService(); // Reference
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("organization")) {
          this.organization = new Reference();
          return this.organization;
        }
        else if (name.equals("role")) {
          this.role = new CodeableConcept();
          return this.role;
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
        else
          return super.addChild(name);
      }

      public PractitionerPractitionerRoleComponent copy() {
        PractitionerPractitionerRoleComponent dst = new PractitionerPractitionerRoleComponent();
        copyValues(dst);
        dst.organization = organization == null ? null : organization.copy();
        dst.role = role == null ? null : role.copy();
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
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof PractitionerPractitionerRoleComponent))
          return false;
        PractitionerPractitionerRoleComponent o = (PractitionerPractitionerRoleComponent) other;
        return compareDeep(organization, o.organization, true) && compareDeep(role, o.role, true) && compareDeep(specialty, o.specialty, true)
           && compareDeep(identifier, o.identifier, true) && compareDeep(telecom, o.telecom, true) && compareDeep(period, o.period, true)
           && compareDeep(location, o.location, true) && compareDeep(healthcareService, o.healthcareService, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof PractitionerPractitionerRoleComponent))
          return false;
        PractitionerPractitionerRoleComponent o = (PractitionerPractitionerRoleComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (organization == null || organization.isEmpty()) && (role == null || role.isEmpty())
           && (specialty == null || specialty.isEmpty()) && (identifier == null || identifier.isEmpty())
           && (telecom == null || telecom.isEmpty()) && (period == null || period.isEmpty()) && (location == null || location.isEmpty())
           && (healthcareService == null || healthcareService.isEmpty());
      }

  public String fhirType() {
    return "Practitioner.practitionerRole";

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

        public boolean hasIdentifier() { 
          if (this.identifier == null)
            return false;
          for (Identifier item : this.identifier)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #identifier} (An identifier that applies to this person's qualification in this role.)
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
        public PractitionerQualificationComponent addIdentifier(Identifier t) { //3
          if (t == null)
            return this;
          if (this.identifier == null)
            this.identifier = new ArrayList<Identifier>();
          this.identifier.add(t);
          return this;
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
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (code == null || code.isEmpty())
           && (period == null || period.isEmpty()) && (issuer == null || issuer.isEmpty());
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
     * Address(es) of the practitioner that are not role specific (typically home address). 
Work addresses are not typically entered in this property as they are usually role dependent.
     */
    @Child(name = "address", type = {Address.class}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Address(es) of the practitioner that are not role specific (typically home address)", formalDefinition="Address(es) of the practitioner that are not role specific (typically home address). \nWork addresses are not typically entered in this property as they are usually role dependent." )
    protected List<Address> address;

    /**
     * Administrative Gender - the gender that the person is considered to have for administration and record keeping purposes.
     */
    @Child(name = "gender", type = {CodeType.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="male | female | other | unknown", formalDefinition="Administrative Gender - the gender that the person is considered to have for administration and record keeping purposes." )
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
    @Child(name = "practitionerRole", type = {}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Roles/organizations the practitioner is associated with", formalDefinition="The list of roles/organizations that the practitioner is associated with." )
    protected List<PractitionerPractitionerRoleComponent> practitionerRole;

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
    protected List<CodeableConcept> communication;

    private static final long serialVersionUID = 2137859974L;

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

    public boolean hasIdentifier() { 
      if (this.identifier == null)
        return false;
      for (Identifier item : this.identifier)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #identifier} (An identifier that applies to this person in this role.)
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
    public Practitioner addIdentifier(Identifier t) { //3
      if (t == null)
        return this;
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return this;
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

    public boolean hasName() { 
      if (this.name == null)
        return false;
      for (HumanName item : this.name)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #name} (The name(s) associated with the practitioner.)
     */
    // syntactic sugar
    public HumanName addName() { //3
      HumanName t = new HumanName();
      if (this.name == null)
        this.name = new ArrayList<HumanName>();
      this.name.add(t);
      return t;
    }

    // syntactic sugar
    public Practitioner addName(HumanName t) { //3
      if (t == null)
        return this;
      if (this.name == null)
        this.name = new ArrayList<HumanName>();
      this.name.add(t);
      return this;
    }

    /**
     * @return {@link #telecom} (A contact detail for the practitioner, e.g. a telephone number or an email address.)
     */
    public List<ContactPoint> getTelecom() { 
      if (this.telecom == null)
        this.telecom = new ArrayList<ContactPoint>();
      return this.telecom;
    }

    public boolean hasTelecom() { 
      if (this.telecom == null)
        return false;
      for (ContactPoint item : this.telecom)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #telecom} (A contact detail for the practitioner, e.g. a telephone number or an email address.)
     */
    // syntactic sugar
    public ContactPoint addTelecom() { //3
      ContactPoint t = new ContactPoint();
      if (this.telecom == null)
        this.telecom = new ArrayList<ContactPoint>();
      this.telecom.add(t);
      return t;
    }

    // syntactic sugar
    public Practitioner addTelecom(ContactPoint t) { //3
      if (t == null)
        return this;
      if (this.telecom == null)
        this.telecom = new ArrayList<ContactPoint>();
      this.telecom.add(t);
      return this;
    }

    /**
     * @return {@link #address} (Address(es) of the practitioner that are not role specific (typically home address). 
Work addresses are not typically entered in this property as they are usually role dependent.)
     */
    public List<Address> getAddress() { 
      if (this.address == null)
        this.address = new ArrayList<Address>();
      return this.address;
    }

    public boolean hasAddress() { 
      if (this.address == null)
        return false;
      for (Address item : this.address)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #address} (Address(es) of the practitioner that are not role specific (typically home address). 
Work addresses are not typically entered in this property as they are usually role dependent.)
     */
    // syntactic sugar
    public Address addAddress() { //3
      Address t = new Address();
      if (this.address == null)
        this.address = new ArrayList<Address>();
      this.address.add(t);
      return t;
    }

    // syntactic sugar
    public Practitioner addAddress(Address t) { //3
      if (t == null)
        return this;
      if (this.address == null)
        this.address = new ArrayList<Address>();
      this.address.add(t);
      return this;
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

    public boolean hasPhoto() { 
      if (this.photo == null)
        return false;
      for (Attachment item : this.photo)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #photo} (Image of the person.)
     */
    // syntactic sugar
    public Attachment addPhoto() { //3
      Attachment t = new Attachment();
      if (this.photo == null)
        this.photo = new ArrayList<Attachment>();
      this.photo.add(t);
      return t;
    }

    // syntactic sugar
    public Practitioner addPhoto(Attachment t) { //3
      if (t == null)
        return this;
      if (this.photo == null)
        this.photo = new ArrayList<Attachment>();
      this.photo.add(t);
      return this;
    }

    /**
     * @return {@link #practitionerRole} (The list of roles/organizations that the practitioner is associated with.)
     */
    public List<PractitionerPractitionerRoleComponent> getPractitionerRole() { 
      if (this.practitionerRole == null)
        this.practitionerRole = new ArrayList<PractitionerPractitionerRoleComponent>();
      return this.practitionerRole;
    }

    public boolean hasPractitionerRole() { 
      if (this.practitionerRole == null)
        return false;
      for (PractitionerPractitionerRoleComponent item : this.practitionerRole)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #practitionerRole} (The list of roles/organizations that the practitioner is associated with.)
     */
    // syntactic sugar
    public PractitionerPractitionerRoleComponent addPractitionerRole() { //3
      PractitionerPractitionerRoleComponent t = new PractitionerPractitionerRoleComponent();
      if (this.practitionerRole == null)
        this.practitionerRole = new ArrayList<PractitionerPractitionerRoleComponent>();
      this.practitionerRole.add(t);
      return t;
    }

    // syntactic sugar
    public Practitioner addPractitionerRole(PractitionerPractitionerRoleComponent t) { //3
      if (t == null)
        return this;
      if (this.practitionerRole == null)
        this.practitionerRole = new ArrayList<PractitionerPractitionerRoleComponent>();
      this.practitionerRole.add(t);
      return this;
    }

    /**
     * @return {@link #qualification} (Qualifications obtained by training and certification.)
     */
    public List<PractitionerQualificationComponent> getQualification() { 
      if (this.qualification == null)
        this.qualification = new ArrayList<PractitionerQualificationComponent>();
      return this.qualification;
    }

    public boolean hasQualification() { 
      if (this.qualification == null)
        return false;
      for (PractitionerQualificationComponent item : this.qualification)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #qualification} (Qualifications obtained by training and certification.)
     */
    // syntactic sugar
    public PractitionerQualificationComponent addQualification() { //3
      PractitionerQualificationComponent t = new PractitionerQualificationComponent();
      if (this.qualification == null)
        this.qualification = new ArrayList<PractitionerQualificationComponent>();
      this.qualification.add(t);
      return t;
    }

    // syntactic sugar
    public Practitioner addQualification(PractitionerQualificationComponent t) { //3
      if (t == null)
        return this;
      if (this.qualification == null)
        this.qualification = new ArrayList<PractitionerQualificationComponent>();
      this.qualification.add(t);
      return this;
    }

    /**
     * @return {@link #communication} (A language the practitioner is able to use in patient communication.)
     */
    public List<CodeableConcept> getCommunication() { 
      if (this.communication == null)
        this.communication = new ArrayList<CodeableConcept>();
      return this.communication;
    }

    public boolean hasCommunication() { 
      if (this.communication == null)
        return false;
      for (CodeableConcept item : this.communication)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #communication} (A language the practitioner is able to use in patient communication.)
     */
    // syntactic sugar
    public CodeableConcept addCommunication() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.communication == null)
        this.communication = new ArrayList<CodeableConcept>();
      this.communication.add(t);
      return t;
    }

    // syntactic sugar
    public Practitioner addCommunication(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.communication == null)
        this.communication = new ArrayList<CodeableConcept>();
      this.communication.add(t);
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "An identifier that applies to this person in this role.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("active", "boolean", "Whether this practitioner's record is in active use.", 0, java.lang.Integer.MAX_VALUE, active));
        childrenList.add(new Property("name", "HumanName", "The name(s) associated with the practitioner.", 0, java.lang.Integer.MAX_VALUE, name));
        childrenList.add(new Property("telecom", "ContactPoint", "A contact detail for the practitioner, e.g. a telephone number or an email address.", 0, java.lang.Integer.MAX_VALUE, telecom));
        childrenList.add(new Property("address", "Address", "Address(es) of the practitioner that are not role specific (typically home address). \nWork addresses are not typically entered in this property as they are usually role dependent.", 0, java.lang.Integer.MAX_VALUE, address));
        childrenList.add(new Property("gender", "code", "Administrative Gender - the gender that the person is considered to have for administration and record keeping purposes.", 0, java.lang.Integer.MAX_VALUE, gender));
        childrenList.add(new Property("birthDate", "date", "The date of birth for the practitioner.", 0, java.lang.Integer.MAX_VALUE, birthDate));
        childrenList.add(new Property("photo", "Attachment", "Image of the person.", 0, java.lang.Integer.MAX_VALUE, photo));
        childrenList.add(new Property("practitionerRole", "", "The list of roles/organizations that the practitioner is associated with.", 0, java.lang.Integer.MAX_VALUE, practitionerRole));
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
        case 221717168: /*practitionerRole*/ return this.practitionerRole == null ? new Base[0] : this.practitionerRole.toArray(new Base[this.practitionerRole.size()]); // PractitionerPractitionerRoleComponent
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
        case 221717168: // practitionerRole
          this.getPractitionerRole().add((PractitionerPractitionerRoleComponent) value); // PractitionerPractitionerRoleComponent
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
        else if (name.equals("practitionerRole"))
          this.getPractitionerRole().add((PractitionerPractitionerRoleComponent) value);
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
        case 221717168:  return addPractitionerRole(); // PractitionerPractitionerRoleComponent
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
        else if (name.equals("practitionerRole")) {
          return addPractitionerRole();
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
        if (practitionerRole != null) {
          dst.practitionerRole = new ArrayList<PractitionerPractitionerRoleComponent>();
          for (PractitionerPractitionerRoleComponent i : practitionerRole)
            dst.practitionerRole.add(i.copy());
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
           && compareDeep(birthDate, o.birthDate, true) && compareDeep(photo, o.photo, true) && compareDeep(practitionerRole, o.practitionerRole, true)
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
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (active == null || active.isEmpty())
           && (name == null || name.isEmpty()) && (telecom == null || telecom.isEmpty()) && (address == null || address.isEmpty())
           && (gender == null || gender.isEmpty()) && (birthDate == null || birthDate.isEmpty()) && (photo == null || photo.isEmpty())
           && (practitionerRole == null || practitionerRole.isEmpty()) && (qualification == null || qualification.isEmpty())
           && (communication == null || communication.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Practitioner;
   }

 /**
   * Search parameter: <b>phone</b>
   * <p>
   * Description: <b>A value in a phone contact</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Practitioner.telecom(system=phone), Practitioner.practitionerRole.telecom(system=phone)</b><br>
   * </p>
   */
  @SearchParamDefinition(name="phone", path="Practitioner.telecom.where(system='phone') or Practitioner.practitionerRole.telecom.where(system='phone')", description="A value in a phone contact", type="token" )
  public static final String SP_PHONE = "phone";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>phone</b>
   * <p>
   * Description: <b>A value in a phone contact</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Practitioner.telecom(system=phone), Practitioner.practitionerRole.telecom(system=phone)</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam PHONE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_PHONE);

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
   * Search parameter: <b>location</b>
   * <p>
   * Description: <b>One of the locations at which this practitioner provides care</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Practitioner.practitionerRole.location</b><br>
   * </p>
   */
  @SearchParamDefinition(name="location", path="Practitioner.practitionerRole.location", description="One of the locations at which this practitioner provides care", type="reference" )
  public static final String SP_LOCATION = "location";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>location</b>
   * <p>
   * Description: <b>One of the locations at which this practitioner provides care</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Practitioner.practitionerRole.location</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam LOCATION = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_LOCATION);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Practitioner:location</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_LOCATION = new ca.uhn.fhir.model.api.Include("Practitioner:location").toLocked();

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
   * Search parameter: <b>organization</b>
   * <p>
   * Description: <b>The identity of the organization the practitioner represents / acts on behalf of</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Practitioner.practitionerRole.organization</b><br>
   * </p>
   */
  @SearchParamDefinition(name="organization", path="Practitioner.practitionerRole.organization", description="The identity of the organization the practitioner represents / acts on behalf of", type="reference" )
  public static final String SP_ORGANIZATION = "organization";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>organization</b>
   * <p>
   * Description: <b>The identity of the organization the practitioner represents / acts on behalf of</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Practitioner.practitionerRole.organization</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam ORGANIZATION = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_ORGANIZATION);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Practitioner:organization</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_ORGANIZATION = new ca.uhn.fhir.model.api.Include("Practitioner:organization").toLocked();

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
   * Search parameter: <b>email</b>
   * <p>
   * Description: <b>A value in an email contact</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Practitioner.telecom(system=email), Practitioner.practitionerRole.telecom(system=email)</b><br>
   * </p>
   */
  @SearchParamDefinition(name="email", path="Practitioner.telecom.where(system='email') or Practitioner.practitionerRole.telecom.where(system='email')", description="A value in an email contact", type="token" )
  public static final String SP_EMAIL = "email";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>email</b>
   * <p>
   * Description: <b>A value in an email contact</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Practitioner.telecom(system=email), Practitioner.practitionerRole.telecom(system=email)</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam EMAIL = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_EMAIL);

 /**
   * Search parameter: <b>address</b>
   * <p>
   * Description: <b>An address in any kind of address/part</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Practitioner.address</b><br>
   * </p>
   */
  @SearchParamDefinition(name="address", path="Practitioner.address", description="An address in any kind of address/part", type="string" )
  public static final String SP_ADDRESS = "address";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>address</b>
   * <p>
   * Description: <b>An address in any kind of address/part</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Practitioner.address</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam ADDRESS = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_ADDRESS);

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
   * Search parameter: <b>name</b>
   * <p>
   * Description: <b>A portion of either family or given name</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Practitioner.name</b><br>
   * </p>
   */
  @SearchParamDefinition(name="name", path="Practitioner.name", description="A portion of either family or given name", type="string" )
  public static final String SP_NAME = "name";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>name</b>
   * <p>
   * Description: <b>A portion of either family or given name</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Practitioner.name</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam NAME = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_NAME);

 /**
   * Search parameter: <b>telecom</b>
   * <p>
   * Description: <b>The value in any kind of contact</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Practitioner.telecom, Practitioner.practitionerRole.telecom</b><br>
   * </p>
   */
  @SearchParamDefinition(name="telecom", path="Practitioner.telecom | Practitioner.practitionerRole.telecom", description="The value in any kind of contact", type="token" )
  public static final String SP_TELECOM = "telecom";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>telecom</b>
   * <p>
   * Description: <b>The value in any kind of contact</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Practitioner.telecom, Practitioner.practitionerRole.telecom</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam TELECOM = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_TELECOM);

 /**
   * Search parameter: <b>role</b>
   * <p>
   * Description: <b>The practitioner can perform this role at for the organization</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Practitioner.practitionerRole.role</b><br>
   * </p>
   */
  @SearchParamDefinition(name="role", path="Practitioner.practitionerRole.role", description="The practitioner can perform this role at for the organization", type="token" )
  public static final String SP_ROLE = "role";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>role</b>
   * <p>
   * Description: <b>The practitioner can perform this role at for the organization</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Practitioner.practitionerRole.role</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam ROLE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_ROLE);

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
   * Search parameter: <b>specialty</b>
   * <p>
   * Description: <b>The practitioner has this specialty at an organization</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Practitioner.practitionerRole.specialty</b><br>
   * </p>
   */
  @SearchParamDefinition(name="specialty", path="Practitioner.practitionerRole.specialty", description="The practitioner has this specialty at an organization", type="token" )
  public static final String SP_SPECIALTY = "specialty";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>specialty</b>
   * <p>
   * Description: <b>The practitioner has this specialty at an organization</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Practitioner.practitionerRole.specialty</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam SPECIALTY = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_SPECIALTY);

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>A practitioner's Identifier</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Practitioner.identifier, Practitioner.practitionerRole.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="Practitioner.identifier | Practitioner.practitionerRole.identifier", description="A practitioner's Identifier", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>A practitioner's Identifier</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Practitioner.identifier, Practitioner.practitionerRole.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

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


}

