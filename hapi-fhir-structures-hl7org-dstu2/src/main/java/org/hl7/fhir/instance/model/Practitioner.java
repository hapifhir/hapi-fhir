package org.hl7.fhir.instance.model;

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

// Generated on Sat, Aug 22, 2015 23:00-0400 for FHIR v0.5.0

import java.util.*;

import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.instance.model.Enumerations.*;
import org.hl7.fhir.instance.model.annotations.ResourceDef;
import org.hl7.fhir.instance.model.annotations.SearchParamDefinition;
import org.hl7.fhir.instance.model.annotations.Child;
import org.hl7.fhir.instance.model.annotations.Description;
import org.hl7.fhir.instance.model.annotations.Block;
import org.hl7.fhir.instance.model.api.*;
/**
 * A person who is directly or indirectly involved in the provisioning of healthcare.
 */
@ResourceDef(name="Practitioner", profile="http://hl7.org/fhir/Profile/Practitioner")
public class Practitioner extends DomainResource {

    @Block()
    public static class PractitionerPractitionerRoleComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The Organization where the Practitioner performs the roles associated.
         */
        @Child(name = "managingOrganization", type = {Organization.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The Organization where the Practitioner performs the roles associated", formalDefinition="The Organization where the Practitioner performs the roles associated." )
        protected Reference managingOrganization;

        /**
         * The actual object that is the target of the reference (The Organization where the Practitioner performs the roles associated.)
         */
        protected Organization managingOrganizationTarget;

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
         * The period during which the person is authorized to act as a practitioner in these role(s) for the organization.
         */
        @Child(name = "period", type = {Period.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The period during which the practitioner is authorized to perform in these role(s)", formalDefinition="The period during which the person is authorized to act as a practitioner in these role(s) for the organization." )
        protected Period period;

        /**
         * The location(s) at which this practitioner provides care.
         */
        @Child(name = "location", type = {Location.class}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="The location(s) at which this practitioner provides care", formalDefinition="The location(s) at which this practitioner provides care." )
        protected List<Reference> location;
        /**
         * The actual objects that are the target of the reference (The location(s) at which this practitioner provides care.)
         */
        protected List<Location> locationTarget;


        /**
         * The list of healthcare services that this worker provides for this role's Organization/Location(s).
         */
        @Child(name = "healthcareService", type = {HealthcareService.class}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="The list of healthcare services that this worker provides for this role's Organization/Location(s)", formalDefinition="The list of healthcare services that this worker provides for this role's Organization/Location(s)." )
        protected List<Reference> healthcareService;
        /**
         * The actual objects that are the target of the reference (The list of healthcare services that this worker provides for this role's Organization/Location(s).)
         */
        protected List<HealthcareService> healthcareServiceTarget;


        private static final long serialVersionUID = -2146177018L;

    /*
     * Constructor
     */
      public PractitionerPractitionerRoleComponent() {
        super();
      }

        /**
         * @return {@link #managingOrganization} (The Organization where the Practitioner performs the roles associated.)
         */
        public Reference getManagingOrganization() { 
          if (this.managingOrganization == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PractitionerPractitionerRoleComponent.managingOrganization");
            else if (Configuration.doAutoCreate())
              this.managingOrganization = new Reference(); // cc
          return this.managingOrganization;
        }

        public boolean hasManagingOrganization() { 
          return this.managingOrganization != null && !this.managingOrganization.isEmpty();
        }

        /**
         * @param value {@link #managingOrganization} (The Organization where the Practitioner performs the roles associated.)
         */
        public PractitionerPractitionerRoleComponent setManagingOrganization(Reference value) { 
          this.managingOrganization = value;
          return this;
        }

        /**
         * @return {@link #managingOrganization} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The Organization where the Practitioner performs the roles associated.)
         */
        public Organization getManagingOrganizationTarget() { 
          if (this.managingOrganizationTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PractitionerPractitionerRoleComponent.managingOrganization");
            else if (Configuration.doAutoCreate())
              this.managingOrganizationTarget = new Organization(); // aa
          return this.managingOrganizationTarget;
        }

        /**
         * @param value {@link #managingOrganization} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The Organization where the Practitioner performs the roles associated.)
         */
        public PractitionerPractitionerRoleComponent setManagingOrganizationTarget(Organization value) { 
          this.managingOrganizationTarget = value;
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
          childrenList.add(new Property("managingOrganization", "Reference(Organization)", "The Organization where the Practitioner performs the roles associated.", 0, java.lang.Integer.MAX_VALUE, managingOrganization));
          childrenList.add(new Property("role", "CodeableConcept", "Roles which this practitioner is authorized to perform for the organization.", 0, java.lang.Integer.MAX_VALUE, role));
          childrenList.add(new Property("specialty", "CodeableConcept", "Specific specialty of the practitioner.", 0, java.lang.Integer.MAX_VALUE, specialty));
          childrenList.add(new Property("period", "Period", "The period during which the person is authorized to act as a practitioner in these role(s) for the organization.", 0, java.lang.Integer.MAX_VALUE, period));
          childrenList.add(new Property("location", "Reference(Location)", "The location(s) at which this practitioner provides care.", 0, java.lang.Integer.MAX_VALUE, location));
          childrenList.add(new Property("healthcareService", "Reference(HealthcareService)", "The list of healthcare services that this worker provides for this role's Organization/Location(s).", 0, java.lang.Integer.MAX_VALUE, healthcareService));
        }

      public PractitionerPractitionerRoleComponent copy() {
        PractitionerPractitionerRoleComponent dst = new PractitionerPractitionerRoleComponent();
        copyValues(dst);
        dst.managingOrganization = managingOrganization == null ? null : managingOrganization.copy();
        dst.role = role == null ? null : role.copy();
        if (specialty != null) {
          dst.specialty = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : specialty)
            dst.specialty.add(i.copy());
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
        return compareDeep(managingOrganization, o.managingOrganization, true) && compareDeep(role, o.role, true)
           && compareDeep(specialty, o.specialty, true) && compareDeep(period, o.period, true) && compareDeep(location, o.location, true)
           && compareDeep(healthcareService, o.healthcareService, true);
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
        return super.isEmpty() && (managingOrganization == null || managingOrganization.isEmpty()) && (role == null || role.isEmpty())
           && (specialty == null || specialty.isEmpty()) && (period == null || period.isEmpty()) && (location == null || location.isEmpty())
           && (healthcareService == null || healthcareService.isEmpty());
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

    /*
     * Constructor
     */
      public PractitionerQualificationComponent() {
        super();
      }

    /*
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

  }

    /**
     * An identifier that applies to this person in this role.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="A identifier for the person as this agent", formalDefinition="An identifier that applies to this person in this role." )
    protected List<Identifier> identifier;

    /**
     * A name associated with the person.
     */
    @Child(name = "name", type = {HumanName.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="A name associated with the person", formalDefinition="A name associated with the person." )
    protected HumanName name;

    /**
     * A contact detail for the practitioner, e.g. a telephone number or an email address.
     */
    @Child(name = "telecom", type = {ContactPoint.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="A contact detail for the practitioner", formalDefinition="A contact detail for the practitioner, e.g. a telephone number or an email address." )
    protected List<ContactPoint> telecom;

    /**
     * The postal address where the practitioner can be found or visited or to which mail can be delivered.
     */
    @Child(name = "address", type = {Address.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Where practitioner can be found/visited", formalDefinition="The postal address where the practitioner can be found or visited or to which mail can be delivered." )
    protected List<Address> address;

    /**
     * Administrative Gender - the gender that the person is considered to have for administration and record keeping purposes.
     */
    @Child(name = "gender", type = {CodeType.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="male | female | other | unknown", formalDefinition="Administrative Gender - the gender that the person is considered to have for administration and record keeping purposes." )
    protected Enumeration<AdministrativeGender> gender;

    /**
     * The date of birth for the practitioner.
     */
    @Child(name = "birthDate", type = {DateType.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The date  on which the practitioner was born", formalDefinition="The date of birth for the practitioner." )
    protected DateType birthDate;

    /**
     * Image of the person.
     */
    @Child(name = "photo", type = {Attachment.class}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Image of the person", formalDefinition="Image of the person." )
    protected List<Attachment> photo;

    /**
     * The list of Roles/Organizations that the Practitioner is associated with.
     */
    @Child(name = "practitionerRole", type = {}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="The list of Roles/Organizations that the Practitioner is associated with", formalDefinition="The list of Roles/Organizations that the Practitioner is associated with." )
    protected List<PractitionerPractitionerRoleComponent> practitionerRole;

    /**
     * Qualifications obtained by training and certification.
     */
    @Child(name = "qualification", type = {}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Qualifications obtained by training and certification", formalDefinition="Qualifications obtained by training and certification." )
    protected List<PractitionerQualificationComponent> qualification;

    /**
     * A language the practitioner is able to use in patient communication.
     */
    @Child(name = "communication", type = {CodeableConcept.class}, order=9, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="A language the practitioner is able to use in patient communication", formalDefinition="A language the practitioner is able to use in patient communication." )
    protected List<CodeableConcept> communication;

    private static final long serialVersionUID = 781100268L;

  /*
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
     * @return {@link #name} (A name associated with the person.)
     */
    public HumanName getName() { 
      if (this.name == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Practitioner.name");
        else if (Configuration.doAutoCreate())
          this.name = new HumanName(); // cc
      return this.name;
    }

    public boolean hasName() { 
      return this.name != null && !this.name.isEmpty();
    }

    /**
     * @param value {@link #name} (A name associated with the person.)
     */
    public Practitioner setName(HumanName value) { 
      this.name = value;
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
     * @return {@link #address} (The postal address where the practitioner can be found or visited or to which mail can be delivered.)
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
     * @return {@link #address} (The postal address where the practitioner can be found or visited or to which mail can be delivered.)
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
     * @return {@link #practitionerRole} (The list of Roles/Organizations that the Practitioner is associated with.)
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
     * @return {@link #practitionerRole} (The list of Roles/Organizations that the Practitioner is associated with.)
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
        childrenList.add(new Property("name", "HumanName", "A name associated with the person.", 0, java.lang.Integer.MAX_VALUE, name));
        childrenList.add(new Property("telecom", "ContactPoint", "A contact detail for the practitioner, e.g. a telephone number or an email address.", 0, java.lang.Integer.MAX_VALUE, telecom));
        childrenList.add(new Property("address", "Address", "The postal address where the practitioner can be found or visited or to which mail can be delivered.", 0, java.lang.Integer.MAX_VALUE, address));
        childrenList.add(new Property("gender", "code", "Administrative Gender - the gender that the person is considered to have for administration and record keeping purposes.", 0, java.lang.Integer.MAX_VALUE, gender));
        childrenList.add(new Property("birthDate", "date", "The date of birth for the practitioner.", 0, java.lang.Integer.MAX_VALUE, birthDate));
        childrenList.add(new Property("photo", "Attachment", "Image of the person.", 0, java.lang.Integer.MAX_VALUE, photo));
        childrenList.add(new Property("practitionerRole", "", "The list of Roles/Organizations that the Practitioner is associated with.", 0, java.lang.Integer.MAX_VALUE, practitionerRole));
        childrenList.add(new Property("qualification", "", "Qualifications obtained by training and certification.", 0, java.lang.Integer.MAX_VALUE, qualification));
        childrenList.add(new Property("communication", "CodeableConcept", "A language the practitioner is able to use in patient communication.", 0, java.lang.Integer.MAX_VALUE, communication));
      }

      public Practitioner copy() {
        Practitioner dst = new Practitioner();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.name = name == null ? null : name.copy();
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
        return compareDeep(identifier, o.identifier, true) && compareDeep(name, o.name, true) && compareDeep(telecom, o.telecom, true)
           && compareDeep(address, o.address, true) && compareDeep(gender, o.gender, true) && compareDeep(birthDate, o.birthDate, true)
           && compareDeep(photo, o.photo, true) && compareDeep(practitionerRole, o.practitionerRole, true)
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
        return compareValues(gender, o.gender, true) && compareValues(birthDate, o.birthDate, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (name == null || name.isEmpty())
           && (telecom == null || telecom.isEmpty()) && (address == null || address.isEmpty()) && (gender == null || gender.isEmpty())
           && (birthDate == null || birthDate.isEmpty()) && (photo == null || photo.isEmpty()) && (practitionerRole == null || practitionerRole.isEmpty())
           && (qualification == null || qualification.isEmpty()) && (communication == null || communication.isEmpty())
          ;
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Practitioner;
   }

  @SearchParamDefinition(name="identifier", path="Practitioner.identifier", description="A practitioner's Identifier", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
  @SearchParamDefinition(name="given", path="Practitioner.name.given", description="A portion of the given name", type="string" )
  public static final String SP_GIVEN = "given";
  @SearchParamDefinition(name="specialty", path="Practitioner.practitionerRole.specialty", description="The practitioner has this specailty at an organization", type="token" )
  public static final String SP_SPECIALTY = "specialty";
  @SearchParamDefinition(name="address", path="Practitioner.address", description="An address in any kind of address/part", type="string" )
  public static final String SP_ADDRESS = "address";
  @SearchParamDefinition(name="role", path="Practitioner.practitionerRole.role", description="The practitioner can perform this role at for the organization", type="token" )
  public static final String SP_ROLE = "role";
  @SearchParamDefinition(name="address-state", path="Practitioner.address.state", description="A state specified in an address", type="string" )
  public static final String SP_ADDRESSSTATE = "address-state";
  @SearchParamDefinition(name="gender", path="Practitioner.gender", description="Gender of the practitioner", type="token" )
  public static final String SP_GENDER = "gender";
  @SearchParamDefinition(name="address-postalcode", path="Practitioner.address.postalCode", description="A postalCode specified in an address", type="string" )
  public static final String SP_ADDRESSPOSTALCODE = "address-postalcode";
  @SearchParamDefinition(name="address-country", path="Practitioner.address.country", description="A country specified in an address", type="string" )
  public static final String SP_ADDRESSCOUNTRY = "address-country";
  @SearchParamDefinition(name="phonetic", path="Practitioner.name", description="A portion of either family or given name using some kind of phonetic matching algorithm", type="string" )
  public static final String SP_PHONETIC = "phonetic";
  @SearchParamDefinition(name="phone", path="Practitioner.telecom(system=phone)", description="A value in a phone contact", type="token" )
  public static final String SP_PHONE = "phone";
  @SearchParamDefinition(name="organization", path="Practitioner.practitionerRole.managingOrganization", description="The identity of the organization the practitioner represents / acts on behalf of", type="reference" )
  public static final String SP_ORGANIZATION = "organization";
  @SearchParamDefinition(name="name", path="Practitioner.name", description="A portion of either family or given name", type="string" )
  public static final String SP_NAME = "name";
  @SearchParamDefinition(name="address-use", path="Practitioner.address.use", description="A use code specified in an address", type="token" )
  public static final String SP_ADDRESSUSE = "address-use";
  @SearchParamDefinition(name="telecom", path="Practitioner.telecom", description="The value in any kind of contact", type="token" )
  public static final String SP_TELECOM = "telecom";
  @SearchParamDefinition(name="location", path="Practitioner.practitionerRole.location", description="One of the locations at which this practitioner provides care", type="reference" )
  public static final String SP_LOCATION = "location";
  @SearchParamDefinition(name="family", path="Practitioner.name.family", description="A portion of the family name", type="string" )
  public static final String SP_FAMILY = "family";
  @SearchParamDefinition(name="address-city", path="Practitioner.address.city", description="A city specified in an address", type="string" )
  public static final String SP_ADDRESSCITY = "address-city";
  @SearchParamDefinition(name="communication", path="Practitioner.communication", description="One of the languages that the practitioner can communicate with", type="token" )
  public static final String SP_COMMUNICATION = "communication";
  @SearchParamDefinition(name="email", path="Practitioner.telecom(system=email)", description="A value in an email contact", type="token" )
  public static final String SP_EMAIL = "email";

}

