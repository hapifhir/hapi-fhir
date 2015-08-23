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
 * Demographics and other administrative information about an individual or animal receiving care or other health-related services.
 */
@ResourceDef(name="Patient", profile="http://hl7.org/fhir/Profile/Patient")
public class Patient extends DomainResource {

    public enum LinkType {
        /**
         * The patient resource containing this link must no longer be used. The link points forward to another patient resource that must be used in lieu of the patient resource that contains the link.
         */
        REPLACE, 
        /**
         * The patient resource containing this link is in use and valid but not considered the main source of information about a patient. The link points forward to another patient resource that should be consulted to retrieve additional patient information
         */
        REFER, 
        /**
         * The patient resource containing this link is in use and valid, but points to another patient resource that is known to contain data about the same person. Data in this resource might overlap or contradict information found in the other patient resource. This link does not indicate any relative importance of the resources concerned, and both should be regarded as equally valid.
         */
        SEEALSO, 
        /**
         * added to help the parsers
         */
        NULL;
        public static LinkType fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("replace".equals(codeString))
          return REPLACE;
        if ("refer".equals(codeString))
          return REFER;
        if ("seealso".equals(codeString))
          return SEEALSO;
        throw new Exception("Unknown LinkType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case REPLACE: return "replace";
            case REFER: return "refer";
            case SEEALSO: return "seealso";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case REPLACE: return "http://hl7.org/fhir/link-type";
            case REFER: return "http://hl7.org/fhir/link-type";
            case SEEALSO: return "http://hl7.org/fhir/link-type";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case REPLACE: return "The patient resource containing this link must no longer be used. The link points forward to another patient resource that must be used in lieu of the patient resource that contains the link.";
            case REFER: return "The patient resource containing this link is in use and valid but not considered the main source of information about a patient. The link points forward to another patient resource that should be consulted to retrieve additional patient information";
            case SEEALSO: return "The patient resource containing this link is in use and valid, but points to another patient resource that is known to contain data about the same person. Data in this resource might overlap or contradict information found in the other patient resource. This link does not indicate any relative importance of the resources concerned, and both should be regarded as equally valid.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case REPLACE: return "Replace";
            case REFER: return "Refer";
            case SEEALSO: return "See also";
            default: return "?";
          }
        }
    }

  public static class LinkTypeEnumFactory implements EnumFactory<LinkType> {
    public LinkType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("replace".equals(codeString))
          return LinkType.REPLACE;
        if ("refer".equals(codeString))
          return LinkType.REFER;
        if ("seealso".equals(codeString))
          return LinkType.SEEALSO;
        throw new IllegalArgumentException("Unknown LinkType code '"+codeString+"'");
        }
    public String toCode(LinkType code) {
      if (code == LinkType.REPLACE)
        return "replace";
      if (code == LinkType.REFER)
        return "refer";
      if (code == LinkType.SEEALSO)
        return "seealso";
      return "?";
      }
    }

    @Block()
    public static class ContactComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The nature of the relationship between the patient and the contact person.
         */
        @Child(name = "relationship", type = {CodeableConcept.class}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="The kind of relationship", formalDefinition="The nature of the relationship between the patient and the contact person." )
        protected List<CodeableConcept> relationship;

        /**
         * A name associated with the contact person.
         */
        @Child(name = "name", type = {HumanName.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="A name associated with the contact person", formalDefinition="A name associated with the contact person." )
        protected HumanName name;

        /**
         * A contact detail for the person, e.g. a telephone number or an email address.
         */
        @Child(name = "telecom", type = {ContactPoint.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="A contact detail for the person", formalDefinition="A contact detail for the person, e.g. a telephone number or an email address." )
        protected List<ContactPoint> telecom;

        /**
         * Address for the contact person.
         */
        @Child(name = "address", type = {Address.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Address for the contact person", formalDefinition="Address for the contact person." )
        protected Address address;

        /**
         * Administrative Gender - the gender that the contact person is considered to have for administration and record keeping purposes.
         */
        @Child(name = "gender", type = {CodeType.class}, order=5, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="male | female | other | unknown", formalDefinition="Administrative Gender - the gender that the contact person is considered to have for administration and record keeping purposes." )
        protected Enumeration<AdministrativeGender> gender;

        /**
         * Organization on behalf of which the contact is acting or for which the contact is working.
         */
        @Child(name = "organization", type = {Organization.class}, order=6, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Organization that is associated with the contact", formalDefinition="Organization on behalf of which the contact is acting or for which the contact is working." )
        protected Reference organization;

        /**
         * The actual object that is the target of the reference (Organization on behalf of which the contact is acting or for which the contact is working.)
         */
        protected Organization organizationTarget;

        /**
         * The period during which this contact person or organization is valid to be contacted relating to this patient.
         */
        @Child(name = "period", type = {Period.class}, order=7, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The period during which this contact person or organization is valid to be contacted relating to this patient", formalDefinition="The period during which this contact person or organization is valid to be contacted relating to this patient." )
        protected Period period;

        private static final long serialVersionUID = 364269017L;

    /*
     * Constructor
     */
      public ContactComponent() {
        super();
      }

        /**
         * @return {@link #relationship} (The nature of the relationship between the patient and the contact person.)
         */
        public List<CodeableConcept> getRelationship() { 
          if (this.relationship == null)
            this.relationship = new ArrayList<CodeableConcept>();
          return this.relationship;
        }

        public boolean hasRelationship() { 
          if (this.relationship == null)
            return false;
          for (CodeableConcept item : this.relationship)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #relationship} (The nature of the relationship between the patient and the contact person.)
         */
    // syntactic sugar
        public CodeableConcept addRelationship() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.relationship == null)
            this.relationship = new ArrayList<CodeableConcept>();
          this.relationship.add(t);
          return t;
        }

    // syntactic sugar
        public ContactComponent addRelationship(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.relationship == null)
            this.relationship = new ArrayList<CodeableConcept>();
          this.relationship.add(t);
          return this;
        }

        /**
         * @return {@link #name} (A name associated with the contact person.)
         */
        public HumanName getName() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ContactComponent.name");
            else if (Configuration.doAutoCreate())
              this.name = new HumanName(); // cc
          return this.name;
        }

        public boolean hasName() { 
          return this.name != null && !this.name.isEmpty();
        }

        /**
         * @param value {@link #name} (A name associated with the contact person.)
         */
        public ContactComponent setName(HumanName value) { 
          this.name = value;
          return this;
        }

        /**
         * @return {@link #telecom} (A contact detail for the person, e.g. a telephone number or an email address.)
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
         * @return {@link #telecom} (A contact detail for the person, e.g. a telephone number or an email address.)
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
        public ContactComponent addTelecom(ContactPoint t) { //3
          if (t == null)
            return this;
          if (this.telecom == null)
            this.telecom = new ArrayList<ContactPoint>();
          this.telecom.add(t);
          return this;
        }

        /**
         * @return {@link #address} (Address for the contact person.)
         */
        public Address getAddress() { 
          if (this.address == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ContactComponent.address");
            else if (Configuration.doAutoCreate())
              this.address = new Address(); // cc
          return this.address;
        }

        public boolean hasAddress() { 
          return this.address != null && !this.address.isEmpty();
        }

        /**
         * @param value {@link #address} (Address for the contact person.)
         */
        public ContactComponent setAddress(Address value) { 
          this.address = value;
          return this;
        }

        /**
         * @return {@link #gender} (Administrative Gender - the gender that the contact person is considered to have for administration and record keeping purposes.). This is the underlying object with id, value and extensions. The accessor "getGender" gives direct access to the value
         */
        public Enumeration<AdministrativeGender> getGenderElement() { 
          if (this.gender == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ContactComponent.gender");
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
         * @param value {@link #gender} (Administrative Gender - the gender that the contact person is considered to have for administration and record keeping purposes.). This is the underlying object with id, value and extensions. The accessor "getGender" gives direct access to the value
         */
        public ContactComponent setGenderElement(Enumeration<AdministrativeGender> value) { 
          this.gender = value;
          return this;
        }

        /**
         * @return Administrative Gender - the gender that the contact person is considered to have for administration and record keeping purposes.
         */
        public AdministrativeGender getGender() { 
          return this.gender == null ? null : this.gender.getValue();
        }

        /**
         * @param value Administrative Gender - the gender that the contact person is considered to have for administration and record keeping purposes.
         */
        public ContactComponent setGender(AdministrativeGender value) { 
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
         * @return {@link #organization} (Organization on behalf of which the contact is acting or for which the contact is working.)
         */
        public Reference getOrganization() { 
          if (this.organization == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ContactComponent.organization");
            else if (Configuration.doAutoCreate())
              this.organization = new Reference(); // cc
          return this.organization;
        }

        public boolean hasOrganization() { 
          return this.organization != null && !this.organization.isEmpty();
        }

        /**
         * @param value {@link #organization} (Organization on behalf of which the contact is acting or for which the contact is working.)
         */
        public ContactComponent setOrganization(Reference value) { 
          this.organization = value;
          return this;
        }

        /**
         * @return {@link #organization} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Organization on behalf of which the contact is acting or for which the contact is working.)
         */
        public Organization getOrganizationTarget() { 
          if (this.organizationTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ContactComponent.organization");
            else if (Configuration.doAutoCreate())
              this.organizationTarget = new Organization(); // aa
          return this.organizationTarget;
        }

        /**
         * @param value {@link #organization} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Organization on behalf of which the contact is acting or for which the contact is working.)
         */
        public ContactComponent setOrganizationTarget(Organization value) { 
          this.organizationTarget = value;
          return this;
        }

        /**
         * @return {@link #period} (The period during which this contact person or organization is valid to be contacted relating to this patient.)
         */
        public Period getPeriod() { 
          if (this.period == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ContactComponent.period");
            else if (Configuration.doAutoCreate())
              this.period = new Period(); // cc
          return this.period;
        }

        public boolean hasPeriod() { 
          return this.period != null && !this.period.isEmpty();
        }

        /**
         * @param value {@link #period} (The period during which this contact person or organization is valid to be contacted relating to this patient.)
         */
        public ContactComponent setPeriod(Period value) { 
          this.period = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("relationship", "CodeableConcept", "The nature of the relationship between the patient and the contact person.", 0, java.lang.Integer.MAX_VALUE, relationship));
          childrenList.add(new Property("name", "HumanName", "A name associated with the contact person.", 0, java.lang.Integer.MAX_VALUE, name));
          childrenList.add(new Property("telecom", "ContactPoint", "A contact detail for the person, e.g. a telephone number or an email address.", 0, java.lang.Integer.MAX_VALUE, telecom));
          childrenList.add(new Property("address", "Address", "Address for the contact person.", 0, java.lang.Integer.MAX_VALUE, address));
          childrenList.add(new Property("gender", "code", "Administrative Gender - the gender that the contact person is considered to have for administration and record keeping purposes.", 0, java.lang.Integer.MAX_VALUE, gender));
          childrenList.add(new Property("organization", "Reference(Organization)", "Organization on behalf of which the contact is acting or for which the contact is working.", 0, java.lang.Integer.MAX_VALUE, organization));
          childrenList.add(new Property("period", "Period", "The period during which this contact person or organization is valid to be contacted relating to this patient.", 0, java.lang.Integer.MAX_VALUE, period));
        }

      public ContactComponent copy() {
        ContactComponent dst = new ContactComponent();
        copyValues(dst);
        if (relationship != null) {
          dst.relationship = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : relationship)
            dst.relationship.add(i.copy());
        };
        dst.name = name == null ? null : name.copy();
        if (telecom != null) {
          dst.telecom = new ArrayList<ContactPoint>();
          for (ContactPoint i : telecom)
            dst.telecom.add(i.copy());
        };
        dst.address = address == null ? null : address.copy();
        dst.gender = gender == null ? null : gender.copy();
        dst.organization = organization == null ? null : organization.copy();
        dst.period = period == null ? null : period.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ContactComponent))
          return false;
        ContactComponent o = (ContactComponent) other;
        return compareDeep(relationship, o.relationship, true) && compareDeep(name, o.name, true) && compareDeep(telecom, o.telecom, true)
           && compareDeep(address, o.address, true) && compareDeep(gender, o.gender, true) && compareDeep(organization, o.organization, true)
           && compareDeep(period, o.period, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ContactComponent))
          return false;
        ContactComponent o = (ContactComponent) other;
        return compareValues(gender, o.gender, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (relationship == null || relationship.isEmpty()) && (name == null || name.isEmpty())
           && (telecom == null || telecom.isEmpty()) && (address == null || address.isEmpty()) && (gender == null || gender.isEmpty())
           && (organization == null || organization.isEmpty()) && (period == null || period.isEmpty())
          ;
      }

  }

    @Block()
    public static class AnimalComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Identifies the high level taxonomic categorization of the kind of animal.
         */
        @Child(name = "species", type = {CodeableConcept.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="E.g. Dog, Cow", formalDefinition="Identifies the high level taxonomic categorization of the kind of animal." )
        protected CodeableConcept species;

        /**
         * Identifies the detailed categorization of the kind of animal.
         */
        @Child(name = "breed", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="E.g. Poodle, Angus", formalDefinition="Identifies the detailed categorization of the kind of animal." )
        protected CodeableConcept breed;

        /**
         * Indicates the current state of the animal's reproductive organs.
         */
        @Child(name = "genderStatus", type = {CodeableConcept.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="E.g. Neutered, Intact", formalDefinition="Indicates the current state of the animal's reproductive organs." )
        protected CodeableConcept genderStatus;

        private static final long serialVersionUID = -549738382L;

    /*
     * Constructor
     */
      public AnimalComponent() {
        super();
      }

    /*
     * Constructor
     */
      public AnimalComponent(CodeableConcept species) {
        super();
        this.species = species;
      }

        /**
         * @return {@link #species} (Identifies the high level taxonomic categorization of the kind of animal.)
         */
        public CodeableConcept getSpecies() { 
          if (this.species == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AnimalComponent.species");
            else if (Configuration.doAutoCreate())
              this.species = new CodeableConcept(); // cc
          return this.species;
        }

        public boolean hasSpecies() { 
          return this.species != null && !this.species.isEmpty();
        }

        /**
         * @param value {@link #species} (Identifies the high level taxonomic categorization of the kind of animal.)
         */
        public AnimalComponent setSpecies(CodeableConcept value) { 
          this.species = value;
          return this;
        }

        /**
         * @return {@link #breed} (Identifies the detailed categorization of the kind of animal.)
         */
        public CodeableConcept getBreed() { 
          if (this.breed == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AnimalComponent.breed");
            else if (Configuration.doAutoCreate())
              this.breed = new CodeableConcept(); // cc
          return this.breed;
        }

        public boolean hasBreed() { 
          return this.breed != null && !this.breed.isEmpty();
        }

        /**
         * @param value {@link #breed} (Identifies the detailed categorization of the kind of animal.)
         */
        public AnimalComponent setBreed(CodeableConcept value) { 
          this.breed = value;
          return this;
        }

        /**
         * @return {@link #genderStatus} (Indicates the current state of the animal's reproductive organs.)
         */
        public CodeableConcept getGenderStatus() { 
          if (this.genderStatus == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AnimalComponent.genderStatus");
            else if (Configuration.doAutoCreate())
              this.genderStatus = new CodeableConcept(); // cc
          return this.genderStatus;
        }

        public boolean hasGenderStatus() { 
          return this.genderStatus != null && !this.genderStatus.isEmpty();
        }

        /**
         * @param value {@link #genderStatus} (Indicates the current state of the animal's reproductive organs.)
         */
        public AnimalComponent setGenderStatus(CodeableConcept value) { 
          this.genderStatus = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("species", "CodeableConcept", "Identifies the high level taxonomic categorization of the kind of animal.", 0, java.lang.Integer.MAX_VALUE, species));
          childrenList.add(new Property("breed", "CodeableConcept", "Identifies the detailed categorization of the kind of animal.", 0, java.lang.Integer.MAX_VALUE, breed));
          childrenList.add(new Property("genderStatus", "CodeableConcept", "Indicates the current state of the animal's reproductive organs.", 0, java.lang.Integer.MAX_VALUE, genderStatus));
        }

      public AnimalComponent copy() {
        AnimalComponent dst = new AnimalComponent();
        copyValues(dst);
        dst.species = species == null ? null : species.copy();
        dst.breed = breed == null ? null : breed.copy();
        dst.genderStatus = genderStatus == null ? null : genderStatus.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof AnimalComponent))
          return false;
        AnimalComponent o = (AnimalComponent) other;
        return compareDeep(species, o.species, true) && compareDeep(breed, o.breed, true) && compareDeep(genderStatus, o.genderStatus, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof AnimalComponent))
          return false;
        AnimalComponent o = (AnimalComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (species == null || species.isEmpty()) && (breed == null || breed.isEmpty())
           && (genderStatus == null || genderStatus.isEmpty());
      }

  }

    @Block()
    public static class PatientCommunicationComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The ISO-639-1 alpha 2 code in lower case for the language, optionally followed by a hyphen and the ISO-3166-1 alpha 2 code for the region in upper case. E.g. "en" for English, or "en-US" for American English versus "en-EN" for England English.
         */
        @Child(name = "language", type = {CodeableConcept.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The language which can be used to communicate with the patient about his or her health", formalDefinition="The ISO-639-1 alpha 2 code in lower case for the language, optionally followed by a hyphen and the ISO-3166-1 alpha 2 code for the region in upper case. E.g. \"en\" for English, or \"en-US\" for American English versus \"en-EN\" for England English." )
        protected CodeableConcept language;

        /**
         * Indicates whether or not the Patient prefers this language (over other languages he masters up a certain level).
         */
        @Child(name = "preferred", type = {BooleanType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Language preference indicator", formalDefinition="Indicates whether or not the Patient prefers this language (over other languages he masters up a certain level)." )
        protected BooleanType preferred;

        private static final long serialVersionUID = 633792918L;

    /*
     * Constructor
     */
      public PatientCommunicationComponent() {
        super();
      }

    /*
     * Constructor
     */
      public PatientCommunicationComponent(CodeableConcept language) {
        super();
        this.language = language;
      }

        /**
         * @return {@link #language} (The ISO-639-1 alpha 2 code in lower case for the language, optionally followed by a hyphen and the ISO-3166-1 alpha 2 code for the region in upper case. E.g. "en" for English, or "en-US" for American English versus "en-EN" for England English.)
         */
        public CodeableConcept getLanguage() { 
          if (this.language == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PatientCommunicationComponent.language");
            else if (Configuration.doAutoCreate())
              this.language = new CodeableConcept(); // cc
          return this.language;
        }

        public boolean hasLanguage() { 
          return this.language != null && !this.language.isEmpty();
        }

        /**
         * @param value {@link #language} (The ISO-639-1 alpha 2 code in lower case for the language, optionally followed by a hyphen and the ISO-3166-1 alpha 2 code for the region in upper case. E.g. "en" for English, or "en-US" for American English versus "en-EN" for England English.)
         */
        public PatientCommunicationComponent setLanguage(CodeableConcept value) { 
          this.language = value;
          return this;
        }

        /**
         * @return {@link #preferred} (Indicates whether or not the Patient prefers this language (over other languages he masters up a certain level).). This is the underlying object with id, value and extensions. The accessor "getPreferred" gives direct access to the value
         */
        public BooleanType getPreferredElement() { 
          if (this.preferred == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PatientCommunicationComponent.preferred");
            else if (Configuration.doAutoCreate())
              this.preferred = new BooleanType(); // bb
          return this.preferred;
        }

        public boolean hasPreferredElement() { 
          return this.preferred != null && !this.preferred.isEmpty();
        }

        public boolean hasPreferred() { 
          return this.preferred != null && !this.preferred.isEmpty();
        }

        /**
         * @param value {@link #preferred} (Indicates whether or not the Patient prefers this language (over other languages he masters up a certain level).). This is the underlying object with id, value and extensions. The accessor "getPreferred" gives direct access to the value
         */
        public PatientCommunicationComponent setPreferredElement(BooleanType value) { 
          this.preferred = value;
          return this;
        }

        /**
         * @return Indicates whether or not the Patient prefers this language (over other languages he masters up a certain level).
         */
        public boolean getPreferred() { 
          return this.preferred == null || this.preferred.isEmpty() ? false : this.preferred.getValue();
        }

        /**
         * @param value Indicates whether or not the Patient prefers this language (over other languages he masters up a certain level).
         */
        public PatientCommunicationComponent setPreferred(boolean value) { 
            if (this.preferred == null)
              this.preferred = new BooleanType();
            this.preferred.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("language", "CodeableConcept", "The ISO-639-1 alpha 2 code in lower case for the language, optionally followed by a hyphen and the ISO-3166-1 alpha 2 code for the region in upper case. E.g. \"en\" for English, or \"en-US\" for American English versus \"en-EN\" for England English.", 0, java.lang.Integer.MAX_VALUE, language));
          childrenList.add(new Property("preferred", "boolean", "Indicates whether or not the Patient prefers this language (over other languages he masters up a certain level).", 0, java.lang.Integer.MAX_VALUE, preferred));
        }

      public PatientCommunicationComponent copy() {
        PatientCommunicationComponent dst = new PatientCommunicationComponent();
        copyValues(dst);
        dst.language = language == null ? null : language.copy();
        dst.preferred = preferred == null ? null : preferred.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof PatientCommunicationComponent))
          return false;
        PatientCommunicationComponent o = (PatientCommunicationComponent) other;
        return compareDeep(language, o.language, true) && compareDeep(preferred, o.preferred, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof PatientCommunicationComponent))
          return false;
        PatientCommunicationComponent o = (PatientCommunicationComponent) other;
        return compareValues(preferred, o.preferred, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (language == null || language.isEmpty()) && (preferred == null || preferred.isEmpty())
          ;
      }

  }

    @Block()
    public static class PatientLinkComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The other patient resource that the link refers to.
         */
        @Child(name = "other", type = {Patient.class}, order=1, min=1, max=1, modifier=true, summary=false)
        @Description(shortDefinition="The other patient resource that the link refers to", formalDefinition="The other patient resource that the link refers to." )
        protected Reference other;

        /**
         * The actual object that is the target of the reference (The other patient resource that the link refers to.)
         */
        protected Patient otherTarget;

        /**
         * The type of link between this patient resource and another patient resource.
         */
        @Child(name = "type", type = {CodeType.class}, order=2, min=1, max=1, modifier=true, summary=false)
        @Description(shortDefinition="replace | refer | seealso - type of link", formalDefinition="The type of link between this patient resource and another patient resource." )
        protected Enumeration<LinkType> type;

        private static final long serialVersionUID = -1942104050L;

    /*
     * Constructor
     */
      public PatientLinkComponent() {
        super();
      }

    /*
     * Constructor
     */
      public PatientLinkComponent(Reference other, Enumeration<LinkType> type) {
        super();
        this.other = other;
        this.type = type;
      }

        /**
         * @return {@link #other} (The other patient resource that the link refers to.)
         */
        public Reference getOther() { 
          if (this.other == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PatientLinkComponent.other");
            else if (Configuration.doAutoCreate())
              this.other = new Reference(); // cc
          return this.other;
        }

        public boolean hasOther() { 
          return this.other != null && !this.other.isEmpty();
        }

        /**
         * @param value {@link #other} (The other patient resource that the link refers to.)
         */
        public PatientLinkComponent setOther(Reference value) { 
          this.other = value;
          return this;
        }

        /**
         * @return {@link #other} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The other patient resource that the link refers to.)
         */
        public Patient getOtherTarget() { 
          if (this.otherTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PatientLinkComponent.other");
            else if (Configuration.doAutoCreate())
              this.otherTarget = new Patient(); // aa
          return this.otherTarget;
        }

        /**
         * @param value {@link #other} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The other patient resource that the link refers to.)
         */
        public PatientLinkComponent setOtherTarget(Patient value) { 
          this.otherTarget = value;
          return this;
        }

        /**
         * @return {@link #type} (The type of link between this patient resource and another patient resource.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public Enumeration<LinkType> getTypeElement() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PatientLinkComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new Enumeration<LinkType>(new LinkTypeEnumFactory()); // bb
          return this.type;
        }

        public boolean hasTypeElement() { 
          return this.type != null && !this.type.isEmpty();
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (The type of link between this patient resource and another patient resource.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public PatientLinkComponent setTypeElement(Enumeration<LinkType> value) { 
          this.type = value;
          return this;
        }

        /**
         * @return The type of link between this patient resource and another patient resource.
         */
        public LinkType getType() { 
          return this.type == null ? null : this.type.getValue();
        }

        /**
         * @param value The type of link between this patient resource and another patient resource.
         */
        public PatientLinkComponent setType(LinkType value) { 
            if (this.type == null)
              this.type = new Enumeration<LinkType>(new LinkTypeEnumFactory());
            this.type.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("other", "Reference(Patient)", "The other patient resource that the link refers to.", 0, java.lang.Integer.MAX_VALUE, other));
          childrenList.add(new Property("type", "code", "The type of link between this patient resource and another patient resource.", 0, java.lang.Integer.MAX_VALUE, type));
        }

      public PatientLinkComponent copy() {
        PatientLinkComponent dst = new PatientLinkComponent();
        copyValues(dst);
        dst.other = other == null ? null : other.copy();
        dst.type = type == null ? null : type.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof PatientLinkComponent))
          return false;
        PatientLinkComponent o = (PatientLinkComponent) other;
        return compareDeep(other, o.other, true) && compareDeep(type, o.type, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof PatientLinkComponent))
          return false;
        PatientLinkComponent o = (PatientLinkComponent) other;
        return compareValues(type, o.type, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (other == null || other.isEmpty()) && (type == null || type.isEmpty())
          ;
      }

  }

    /**
     * An identifier for this patient.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="An identifier for this patient", formalDefinition="An identifier for this patient." )
    protected List<Identifier> identifier;

    /**
     * Whether this patient record is in active use.
     */
    @Child(name = "active", type = {BooleanType.class}, order=1, min=0, max=1, modifier=true, summary=true)
    @Description(shortDefinition="Whether this patient's record is in active use", formalDefinition="Whether this patient record is in active use." )
    protected BooleanType active;

    /**
     * A name associated with the individual.
     */
    @Child(name = "name", type = {HumanName.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="A name associated with the patient", formalDefinition="A name associated with the individual." )
    protected List<HumanName> name;

    /**
     * A contact detail (e.g. a telephone number or an email address) by which the individual may be contacted.
     */
    @Child(name = "telecom", type = {ContactPoint.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="A contact detail for the individual", formalDefinition="A contact detail (e.g. a telephone number or an email address) by which the individual may be contacted." )
    protected List<ContactPoint> telecom;

    /**
     * Administrative Gender - the gender that the patient is considered to have for administration and record keeping purposes.
     */
    @Child(name = "gender", type = {CodeType.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="male | female | other | unknown", formalDefinition="Administrative Gender - the gender that the patient is considered to have for administration and record keeping purposes." )
    protected Enumeration<AdministrativeGender> gender;

    /**
     * The date of birth for the individual.
     */
    @Child(name = "birthDate", type = {DateType.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The date of birth for the individual", formalDefinition="The date of birth for the individual." )
    protected DateType birthDate;

    /**
     * Indicates if the individual is deceased or not.
     */
    @Child(name = "deceased", type = {BooleanType.class, DateTimeType.class}, order=6, min=0, max=1, modifier=true, summary=true)
    @Description(shortDefinition="Indicates if the individual is deceased or not", formalDefinition="Indicates if the individual is deceased or not." )
    protected Type deceased;

    /**
     * Addresses for the individual.
     */
    @Child(name = "address", type = {Address.class}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Addresses for the individual", formalDefinition="Addresses for the individual." )
    protected List<Address> address;

    /**
     * This field contains a patient's most recent marital (civil) status.
     */
    @Child(name = "maritalStatus", type = {CodeableConcept.class}, order=8, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Marital (civil) status of a patient", formalDefinition="This field contains a patient's most recent marital (civil) status." )
    protected CodeableConcept maritalStatus;

    /**
     * Indicates whether the patient is part of a multiple or indicates the actual birth order.
     */
    @Child(name = "multipleBirth", type = {BooleanType.class, IntegerType.class}, order=9, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Whether patient is part of a multiple birth", formalDefinition="Indicates whether the patient is part of a multiple or indicates the actual birth order." )
    protected Type multipleBirth;

    /**
     * Image of the patient.
     */
    @Child(name = "photo", type = {Attachment.class}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Image of the patient", formalDefinition="Image of the patient." )
    protected List<Attachment> photo;

    /**
     * A contact party (e.g. guardian, partner, friend) for the patient.
     */
    @Child(name = "contact", type = {}, order=11, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="A contact party (e.g. guardian, partner, friend) for the patient", formalDefinition="A contact party (e.g. guardian, partner, friend) for the patient." )
    protected List<ContactComponent> contact;

    /**
     * This patient is known to be an animal.
     */
    @Child(name = "animal", type = {}, order=12, min=0, max=1, modifier=true, summary=true)
    @Description(shortDefinition="This patient is known to be an animal (non-human)", formalDefinition="This patient is known to be an animal." )
    protected AnimalComponent animal;

    /**
     * Languages which may be used to communicate with the patient about his or her health.
     */
    @Child(name = "communication", type = {}, order=13, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="A list of Languages which may be used to communicate with the patient about his or her health", formalDefinition="Languages which may be used to communicate with the patient about his or her health." )
    protected List<PatientCommunicationComponent> communication;

    /**
     * Patient's nominated care provider.
     */
    @Child(name = "careProvider", type = {Organization.class, Practitioner.class}, order=14, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Patient's nominated care provider", formalDefinition="Patient's nominated care provider." )
    protected List<Reference> careProvider;
    /**
     * The actual objects that are the target of the reference (Patient's nominated care provider.)
     */
    protected List<Resource> careProviderTarget;


    /**
     * Organization that is the custodian of the patient record.
     */
    @Child(name = "managingOrganization", type = {Organization.class}, order=15, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Organization that is the custodian of the patient record", formalDefinition="Organization that is the custodian of the patient record." )
    protected Reference managingOrganization;

    /**
     * The actual object that is the target of the reference (Organization that is the custodian of the patient record.)
     */
    protected Organization managingOrganizationTarget;

    /**
     * Link to another patient resource that concerns the same actual patient.
     */
    @Child(name = "link", type = {}, order=16, min=0, max=Child.MAX_UNLIMITED, modifier=true, summary=false)
    @Description(shortDefinition="Link to another patient resource that concerns the same actual person", formalDefinition="Link to another patient resource that concerns the same actual patient." )
    protected List<PatientLinkComponent> link;

    private static final long serialVersionUID = 2019992554L;

  /*
   * Constructor
   */
    public Patient() {
      super();
    }

    /**
     * @return {@link #identifier} (An identifier for this patient.)
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
     * @return {@link #identifier} (An identifier for this patient.)
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
    public Patient addIdentifier(Identifier t) { //3
      if (t == null)
        return this;
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return this;
    }

    /**
     * @return {@link #active} (Whether this patient record is in active use.). This is the underlying object with id, value and extensions. The accessor "getActive" gives direct access to the value
     */
    public BooleanType getActiveElement() { 
      if (this.active == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Patient.active");
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
     * @param value {@link #active} (Whether this patient record is in active use.). This is the underlying object with id, value and extensions. The accessor "getActive" gives direct access to the value
     */
    public Patient setActiveElement(BooleanType value) { 
      this.active = value;
      return this;
    }

    /**
     * @return Whether this patient record is in active use.
     */
    public boolean getActive() { 
      return this.active == null || this.active.isEmpty() ? false : this.active.getValue();
    }

    /**
     * @param value Whether this patient record is in active use.
     */
    public Patient setActive(boolean value) { 
        if (this.active == null)
          this.active = new BooleanType();
        this.active.setValue(value);
      return this;
    }

    /**
     * @return {@link #name} (A name associated with the individual.)
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
     * @return {@link #name} (A name associated with the individual.)
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
    public Patient addName(HumanName t) { //3
      if (t == null)
        return this;
      if (this.name == null)
        this.name = new ArrayList<HumanName>();
      this.name.add(t);
      return this;
    }

    /**
     * @return {@link #telecom} (A contact detail (e.g. a telephone number or an email address) by which the individual may be contacted.)
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
     * @return {@link #telecom} (A contact detail (e.g. a telephone number or an email address) by which the individual may be contacted.)
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
    public Patient addTelecom(ContactPoint t) { //3
      if (t == null)
        return this;
      if (this.telecom == null)
        this.telecom = new ArrayList<ContactPoint>();
      this.telecom.add(t);
      return this;
    }

    /**
     * @return {@link #gender} (Administrative Gender - the gender that the patient is considered to have for administration and record keeping purposes.). This is the underlying object with id, value and extensions. The accessor "getGender" gives direct access to the value
     */
    public Enumeration<AdministrativeGender> getGenderElement() { 
      if (this.gender == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Patient.gender");
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
     * @param value {@link #gender} (Administrative Gender - the gender that the patient is considered to have for administration and record keeping purposes.). This is the underlying object with id, value and extensions. The accessor "getGender" gives direct access to the value
     */
    public Patient setGenderElement(Enumeration<AdministrativeGender> value) { 
      this.gender = value;
      return this;
    }

    /**
     * @return Administrative Gender - the gender that the patient is considered to have for administration and record keeping purposes.
     */
    public AdministrativeGender getGender() { 
      return this.gender == null ? null : this.gender.getValue();
    }

    /**
     * @param value Administrative Gender - the gender that the patient is considered to have for administration and record keeping purposes.
     */
    public Patient setGender(AdministrativeGender value) { 
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
     * @return {@link #birthDate} (The date of birth for the individual.). This is the underlying object with id, value and extensions. The accessor "getBirthDate" gives direct access to the value
     */
    public DateType getBirthDateElement() { 
      if (this.birthDate == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Patient.birthDate");
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
     * @param value {@link #birthDate} (The date of birth for the individual.). This is the underlying object with id, value and extensions. The accessor "getBirthDate" gives direct access to the value
     */
    public Patient setBirthDateElement(DateType value) { 
      this.birthDate = value;
      return this;
    }

    /**
     * @return The date of birth for the individual.
     */
    public Date getBirthDate() { 
      return this.birthDate == null ? null : this.birthDate.getValue();
    }

    /**
     * @param value The date of birth for the individual.
     */
    public Patient setBirthDate(Date value) { 
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
     * @return {@link #deceased} (Indicates if the individual is deceased or not.)
     */
    public Type getDeceased() { 
      return this.deceased;
    }

    /**
     * @return {@link #deceased} (Indicates if the individual is deceased or not.)
     */
    public BooleanType getDeceasedBooleanType() throws Exception { 
      if (!(this.deceased instanceof BooleanType))
        throw new Exception("Type mismatch: the type BooleanType was expected, but "+this.deceased.getClass().getName()+" was encountered");
      return (BooleanType) this.deceased;
    }

    public boolean hasDeceasedBooleanType() throws Exception { 
      return this.deceased instanceof BooleanType;
    }

    /**
     * @return {@link #deceased} (Indicates if the individual is deceased or not.)
     */
    public DateTimeType getDeceasedDateTimeType() throws Exception { 
      if (!(this.deceased instanceof DateTimeType))
        throw new Exception("Type mismatch: the type DateTimeType was expected, but "+this.deceased.getClass().getName()+" was encountered");
      return (DateTimeType) this.deceased;
    }

    public boolean hasDeceasedDateTimeType() throws Exception { 
      return this.deceased instanceof DateTimeType;
    }

    public boolean hasDeceased() { 
      return this.deceased != null && !this.deceased.isEmpty();
    }

    /**
     * @param value {@link #deceased} (Indicates if the individual is deceased or not.)
     */
    public Patient setDeceased(Type value) { 
      this.deceased = value;
      return this;
    }

    /**
     * @return {@link #address} (Addresses for the individual.)
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
     * @return {@link #address} (Addresses for the individual.)
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
    public Patient addAddress(Address t) { //3
      if (t == null)
        return this;
      if (this.address == null)
        this.address = new ArrayList<Address>();
      this.address.add(t);
      return this;
    }

    /**
     * @return {@link #maritalStatus} (This field contains a patient's most recent marital (civil) status.)
     */
    public CodeableConcept getMaritalStatus() { 
      if (this.maritalStatus == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Patient.maritalStatus");
        else if (Configuration.doAutoCreate())
          this.maritalStatus = new CodeableConcept(); // cc
      return this.maritalStatus;
    }

    public boolean hasMaritalStatus() { 
      return this.maritalStatus != null && !this.maritalStatus.isEmpty();
    }

    /**
     * @param value {@link #maritalStatus} (This field contains a patient's most recent marital (civil) status.)
     */
    public Patient setMaritalStatus(CodeableConcept value) { 
      this.maritalStatus = value;
      return this;
    }

    /**
     * @return {@link #multipleBirth} (Indicates whether the patient is part of a multiple or indicates the actual birth order.)
     */
    public Type getMultipleBirth() { 
      return this.multipleBirth;
    }

    /**
     * @return {@link #multipleBirth} (Indicates whether the patient is part of a multiple or indicates the actual birth order.)
     */
    public BooleanType getMultipleBirthBooleanType() throws Exception { 
      if (!(this.multipleBirth instanceof BooleanType))
        throw new Exception("Type mismatch: the type BooleanType was expected, but "+this.multipleBirth.getClass().getName()+" was encountered");
      return (BooleanType) this.multipleBirth;
    }

    public boolean hasMultipleBirthBooleanType() throws Exception { 
      return this.multipleBirth instanceof BooleanType;
    }

    /**
     * @return {@link #multipleBirth} (Indicates whether the patient is part of a multiple or indicates the actual birth order.)
     */
    public IntegerType getMultipleBirthIntegerType() throws Exception { 
      if (!(this.multipleBirth instanceof IntegerType))
        throw new Exception("Type mismatch: the type IntegerType was expected, but "+this.multipleBirth.getClass().getName()+" was encountered");
      return (IntegerType) this.multipleBirth;
    }

    public boolean hasMultipleBirthIntegerType() throws Exception { 
      return this.multipleBirth instanceof IntegerType;
    }

    public boolean hasMultipleBirth() { 
      return this.multipleBirth != null && !this.multipleBirth.isEmpty();
    }

    /**
     * @param value {@link #multipleBirth} (Indicates whether the patient is part of a multiple or indicates the actual birth order.)
     */
    public Patient setMultipleBirth(Type value) { 
      this.multipleBirth = value;
      return this;
    }

    /**
     * @return {@link #photo} (Image of the patient.)
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
     * @return {@link #photo} (Image of the patient.)
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
    public Patient addPhoto(Attachment t) { //3
      if (t == null)
        return this;
      if (this.photo == null)
        this.photo = new ArrayList<Attachment>();
      this.photo.add(t);
      return this;
    }

    /**
     * @return {@link #contact} (A contact party (e.g. guardian, partner, friend) for the patient.)
     */
    public List<ContactComponent> getContact() { 
      if (this.contact == null)
        this.contact = new ArrayList<ContactComponent>();
      return this.contact;
    }

    public boolean hasContact() { 
      if (this.contact == null)
        return false;
      for (ContactComponent item : this.contact)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #contact} (A contact party (e.g. guardian, partner, friend) for the patient.)
     */
    // syntactic sugar
    public ContactComponent addContact() { //3
      ContactComponent t = new ContactComponent();
      if (this.contact == null)
        this.contact = new ArrayList<ContactComponent>();
      this.contact.add(t);
      return t;
    }

    // syntactic sugar
    public Patient addContact(ContactComponent t) { //3
      if (t == null)
        return this;
      if (this.contact == null)
        this.contact = new ArrayList<ContactComponent>();
      this.contact.add(t);
      return this;
    }

    /**
     * @return {@link #animal} (This patient is known to be an animal.)
     */
    public AnimalComponent getAnimal() { 
      if (this.animal == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Patient.animal");
        else if (Configuration.doAutoCreate())
          this.animal = new AnimalComponent(); // cc
      return this.animal;
    }

    public boolean hasAnimal() { 
      return this.animal != null && !this.animal.isEmpty();
    }

    /**
     * @param value {@link #animal} (This patient is known to be an animal.)
     */
    public Patient setAnimal(AnimalComponent value) { 
      this.animal = value;
      return this;
    }

    /**
     * @return {@link #communication} (Languages which may be used to communicate with the patient about his or her health.)
     */
    public List<PatientCommunicationComponent> getCommunication() { 
      if (this.communication == null)
        this.communication = new ArrayList<PatientCommunicationComponent>();
      return this.communication;
    }

    public boolean hasCommunication() { 
      if (this.communication == null)
        return false;
      for (PatientCommunicationComponent item : this.communication)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #communication} (Languages which may be used to communicate with the patient about his or her health.)
     */
    // syntactic sugar
    public PatientCommunicationComponent addCommunication() { //3
      PatientCommunicationComponent t = new PatientCommunicationComponent();
      if (this.communication == null)
        this.communication = new ArrayList<PatientCommunicationComponent>();
      this.communication.add(t);
      return t;
    }

    // syntactic sugar
    public Patient addCommunication(PatientCommunicationComponent t) { //3
      if (t == null)
        return this;
      if (this.communication == null)
        this.communication = new ArrayList<PatientCommunicationComponent>();
      this.communication.add(t);
      return this;
    }

    /**
     * @return {@link #careProvider} (Patient's nominated care provider.)
     */
    public List<Reference> getCareProvider() { 
      if (this.careProvider == null)
        this.careProvider = new ArrayList<Reference>();
      return this.careProvider;
    }

    public boolean hasCareProvider() { 
      if (this.careProvider == null)
        return false;
      for (Reference item : this.careProvider)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #careProvider} (Patient's nominated care provider.)
     */
    // syntactic sugar
    public Reference addCareProvider() { //3
      Reference t = new Reference();
      if (this.careProvider == null)
        this.careProvider = new ArrayList<Reference>();
      this.careProvider.add(t);
      return t;
    }

    // syntactic sugar
    public Patient addCareProvider(Reference t) { //3
      if (t == null)
        return this;
      if (this.careProvider == null)
        this.careProvider = new ArrayList<Reference>();
      this.careProvider.add(t);
      return this;
    }

    /**
     * @return {@link #careProvider} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. Patient's nominated care provider.)
     */
    public List<Resource> getCareProviderTarget() { 
      if (this.careProviderTarget == null)
        this.careProviderTarget = new ArrayList<Resource>();
      return this.careProviderTarget;
    }

    /**
     * @return {@link #managingOrganization} (Organization that is the custodian of the patient record.)
     */
    public Reference getManagingOrganization() { 
      if (this.managingOrganization == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Patient.managingOrganization");
        else if (Configuration.doAutoCreate())
          this.managingOrganization = new Reference(); // cc
      return this.managingOrganization;
    }

    public boolean hasManagingOrganization() { 
      return this.managingOrganization != null && !this.managingOrganization.isEmpty();
    }

    /**
     * @param value {@link #managingOrganization} (Organization that is the custodian of the patient record.)
     */
    public Patient setManagingOrganization(Reference value) { 
      this.managingOrganization = value;
      return this;
    }

    /**
     * @return {@link #managingOrganization} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Organization that is the custodian of the patient record.)
     */
    public Organization getManagingOrganizationTarget() { 
      if (this.managingOrganizationTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Patient.managingOrganization");
        else if (Configuration.doAutoCreate())
          this.managingOrganizationTarget = new Organization(); // aa
      return this.managingOrganizationTarget;
    }

    /**
     * @param value {@link #managingOrganization} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Organization that is the custodian of the patient record.)
     */
    public Patient setManagingOrganizationTarget(Organization value) { 
      this.managingOrganizationTarget = value;
      return this;
    }

    /**
     * @return {@link #link} (Link to another patient resource that concerns the same actual patient.)
     */
    public List<PatientLinkComponent> getLink() { 
      if (this.link == null)
        this.link = new ArrayList<PatientLinkComponent>();
      return this.link;
    }

    public boolean hasLink() { 
      if (this.link == null)
        return false;
      for (PatientLinkComponent item : this.link)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #link} (Link to another patient resource that concerns the same actual patient.)
     */
    // syntactic sugar
    public PatientLinkComponent addLink() { //3
      PatientLinkComponent t = new PatientLinkComponent();
      if (this.link == null)
        this.link = new ArrayList<PatientLinkComponent>();
      this.link.add(t);
      return t;
    }

    // syntactic sugar
    public Patient addLink(PatientLinkComponent t) { //3
      if (t == null)
        return this;
      if (this.link == null)
        this.link = new ArrayList<PatientLinkComponent>();
      this.link.add(t);
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "An identifier for this patient.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("active", "boolean", "Whether this patient record is in active use.", 0, java.lang.Integer.MAX_VALUE, active));
        childrenList.add(new Property("name", "HumanName", "A name associated with the individual.", 0, java.lang.Integer.MAX_VALUE, name));
        childrenList.add(new Property("telecom", "ContactPoint", "A contact detail (e.g. a telephone number or an email address) by which the individual may be contacted.", 0, java.lang.Integer.MAX_VALUE, telecom));
        childrenList.add(new Property("gender", "code", "Administrative Gender - the gender that the patient is considered to have for administration and record keeping purposes.", 0, java.lang.Integer.MAX_VALUE, gender));
        childrenList.add(new Property("birthDate", "date", "The date of birth for the individual.", 0, java.lang.Integer.MAX_VALUE, birthDate));
        childrenList.add(new Property("deceased[x]", "boolean|dateTime", "Indicates if the individual is deceased or not.", 0, java.lang.Integer.MAX_VALUE, deceased));
        childrenList.add(new Property("address", "Address", "Addresses for the individual.", 0, java.lang.Integer.MAX_VALUE, address));
        childrenList.add(new Property("maritalStatus", "CodeableConcept", "This field contains a patient's most recent marital (civil) status.", 0, java.lang.Integer.MAX_VALUE, maritalStatus));
        childrenList.add(new Property("multipleBirth[x]", "boolean|integer", "Indicates whether the patient is part of a multiple or indicates the actual birth order.", 0, java.lang.Integer.MAX_VALUE, multipleBirth));
        childrenList.add(new Property("photo", "Attachment", "Image of the patient.", 0, java.lang.Integer.MAX_VALUE, photo));
        childrenList.add(new Property("contact", "", "A contact party (e.g. guardian, partner, friend) for the patient.", 0, java.lang.Integer.MAX_VALUE, contact));
        childrenList.add(new Property("animal", "", "This patient is known to be an animal.", 0, java.lang.Integer.MAX_VALUE, animal));
        childrenList.add(new Property("communication", "", "Languages which may be used to communicate with the patient about his or her health.", 0, java.lang.Integer.MAX_VALUE, communication));
        childrenList.add(new Property("careProvider", "Reference(Organization|Practitioner)", "Patient's nominated care provider.", 0, java.lang.Integer.MAX_VALUE, careProvider));
        childrenList.add(new Property("managingOrganization", "Reference(Organization)", "Organization that is the custodian of the patient record.", 0, java.lang.Integer.MAX_VALUE, managingOrganization));
        childrenList.add(new Property("link", "", "Link to another patient resource that concerns the same actual patient.", 0, java.lang.Integer.MAX_VALUE, link));
      }

      public Patient copy() {
        Patient dst = new Patient();
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
        dst.gender = gender == null ? null : gender.copy();
        dst.birthDate = birthDate == null ? null : birthDate.copy();
        dst.deceased = deceased == null ? null : deceased.copy();
        if (address != null) {
          dst.address = new ArrayList<Address>();
          for (Address i : address)
            dst.address.add(i.copy());
        };
        dst.maritalStatus = maritalStatus == null ? null : maritalStatus.copy();
        dst.multipleBirth = multipleBirth == null ? null : multipleBirth.copy();
        if (photo != null) {
          dst.photo = new ArrayList<Attachment>();
          for (Attachment i : photo)
            dst.photo.add(i.copy());
        };
        if (contact != null) {
          dst.contact = new ArrayList<ContactComponent>();
          for (ContactComponent i : contact)
            dst.contact.add(i.copy());
        };
        dst.animal = animal == null ? null : animal.copy();
        if (communication != null) {
          dst.communication = new ArrayList<PatientCommunicationComponent>();
          for (PatientCommunicationComponent i : communication)
            dst.communication.add(i.copy());
        };
        if (careProvider != null) {
          dst.careProvider = new ArrayList<Reference>();
          for (Reference i : careProvider)
            dst.careProvider.add(i.copy());
        };
        dst.managingOrganization = managingOrganization == null ? null : managingOrganization.copy();
        if (link != null) {
          dst.link = new ArrayList<PatientLinkComponent>();
          for (PatientLinkComponent i : link)
            dst.link.add(i.copy());
        };
        return dst;
      }

      protected Patient typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof Patient))
          return false;
        Patient o = (Patient) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(active, o.active, true) && compareDeep(name, o.name, true)
           && compareDeep(telecom, o.telecom, true) && compareDeep(gender, o.gender, true) && compareDeep(birthDate, o.birthDate, true)
           && compareDeep(deceased, o.deceased, true) && compareDeep(address, o.address, true) && compareDeep(maritalStatus, o.maritalStatus, true)
           && compareDeep(multipleBirth, o.multipleBirth, true) && compareDeep(photo, o.photo, true) && compareDeep(contact, o.contact, true)
           && compareDeep(animal, o.animal, true) && compareDeep(communication, o.communication, true) && compareDeep(careProvider, o.careProvider, true)
           && compareDeep(managingOrganization, o.managingOrganization, true) && compareDeep(link, o.link, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Patient))
          return false;
        Patient o = (Patient) other;
        return compareValues(active, o.active, true) && compareValues(gender, o.gender, true) && compareValues(birthDate, o.birthDate, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (active == null || active.isEmpty())
           && (name == null || name.isEmpty()) && (telecom == null || telecom.isEmpty()) && (gender == null || gender.isEmpty())
           && (birthDate == null || birthDate.isEmpty()) && (deceased == null || deceased.isEmpty())
           && (address == null || address.isEmpty()) && (maritalStatus == null || maritalStatus.isEmpty())
           && (multipleBirth == null || multipleBirth.isEmpty()) && (photo == null || photo.isEmpty())
           && (contact == null || contact.isEmpty()) && (animal == null || animal.isEmpty()) && (communication == null || communication.isEmpty())
           && (careProvider == null || careProvider.isEmpty()) && (managingOrganization == null || managingOrganization.isEmpty())
           && (link == null || link.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Patient;
   }

  @SearchParamDefinition(name="birthdate", path="Patient.birthDate", description="The patient's date of birth", type="date" )
  public static final String SP_BIRTHDATE = "birthdate";
  @SearchParamDefinition(name="deceased", path="Patient.deceased[x]", description="This patient has been marked as deceased, or as a death date entered", type="token" )
  public static final String SP_DECEASED = "deceased";
  @SearchParamDefinition(name="address-state", path="Patient.address.state", description="A state specified in an address", type="string" )
  public static final String SP_ADDRESSSTATE = "address-state";
  @SearchParamDefinition(name="gender", path="Patient.gender", description="Gender of the patient", type="token" )
  public static final String SP_GENDER = "gender";
  @SearchParamDefinition(name="animal-species", path="Patient.animal.species", description="The species for animal patients", type="token" )
  public static final String SP_ANIMALSPECIES = "animal-species";
  @SearchParamDefinition(name="link", path="Patient.link.other", description="All patients linked to the given patient", type="reference" )
  public static final String SP_LINK = "link";
  @SearchParamDefinition(name="language", path="Patient.communication.language", description="Language code (irrespective of use value)", type="token" )
  public static final String SP_LANGUAGE = "language";
  @SearchParamDefinition(name="deathdate", path="Patient.deceased[x]", description="The date of death has been provided and satisfies this search value", type="date" )
  public static final String SP_DEATHDATE = "deathdate";
  @SearchParamDefinition(name="animal-breed", path="Patient.animal.breed", description="The breed for animal patients", type="token" )
  public static final String SP_ANIMALBREED = "animal-breed";
  @SearchParamDefinition(name="address-country", path="Patient.address.country", description="A country specified in an address", type="string" )
  public static final String SP_ADDRESSCOUNTRY = "address-country";
  @SearchParamDefinition(name="phonetic", path="Patient.name", description="A portion of either family or given name using some kind of phonetic matching algorithm", type="string" )
  public static final String SP_PHONETIC = "phonetic";
  @SearchParamDefinition(name="telecom", path="Patient.telecom", description="The value in any kind of telecom details of the patient", type="token" )
  public static final String SP_TELECOM = "telecom";
  @SearchParamDefinition(name="address-city", path="Patient.address.city", description="A city specified in an address", type="string" )
  public static final String SP_ADDRESSCITY = "address-city";
  @SearchParamDefinition(name="email", path="Patient.telecom(system=email)", description="A value in an email contact", type="token" )
  public static final String SP_EMAIL = "email";
  @SearchParamDefinition(name="identifier", path="Patient.identifier", description="A patient identifier", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
  @SearchParamDefinition(name="given", path="Patient.name.given", description="A portion of the given name of the patient", type="string" )
  public static final String SP_GIVEN = "given";
  @SearchParamDefinition(name="address", path="Patient.address", description="An address in any kind of address/part of the patient", type="string" )
  public static final String SP_ADDRESS = "address";
  @SearchParamDefinition(name="active", path="Patient.active", description="Whether the patient record is active", type="token" )
  public static final String SP_ACTIVE = "active";
  @SearchParamDefinition(name="address-postalcode", path="Patient.address.postalCode", description="A postalCode specified in an address", type="string" )
  public static final String SP_ADDRESSPOSTALCODE = "address-postalcode";
  @SearchParamDefinition(name="careprovider", path="Patient.careProvider", description="Patient's nominated care provider, could be a care manager, not the organization that manages the record", type="reference" )
  public static final String SP_CAREPROVIDER = "careprovider";
  @SearchParamDefinition(name="phone", path="Patient.telecom(system=phone)", description="A value in a phone contact", type="token" )
  public static final String SP_PHONE = "phone";
  @SearchParamDefinition(name="organization", path="Patient.managingOrganization", description="The organization at which this person is a patient", type="reference" )
  public static final String SP_ORGANIZATION = "organization";
  @SearchParamDefinition(name="name", path="Patient.name", description="A portion of either family or given name of the patient", type="string" )
  public static final String SP_NAME = "name";
  @SearchParamDefinition(name="address-use", path="Patient.address.use", description="A use code specified in an address", type="token" )
  public static final String SP_ADDRESSUSE = "address-use";
  @SearchParamDefinition(name="family", path="Patient.name.family", description="A portion of the family name of the patient", type="string" )
  public static final String SP_FAMILY = "family";

}

