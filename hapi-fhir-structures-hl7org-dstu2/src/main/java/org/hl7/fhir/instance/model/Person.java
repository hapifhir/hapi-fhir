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
 * Demographics and administrative information about a person independent of a specific health-related context.
 */
@ResourceDef(name="Person", profile="http://hl7.org/fhir/Profile/Person")
public class Person extends DomainResource {

    public enum IdentityAssuranceLevel {
        /**
         * Little or no confidence in the asserted identity's accuracy.
         */
        LEVEL1, 
        /**
         * Some confidence in the asserted identity's accuracy.
         */
        LEVEL2, 
        /**
         * High confidence in the asserted identity's accuracy.
         */
        LEVEL3, 
        /**
         * Very high confidence in the asserted identity's accuracy.
         */
        LEVEL4, 
        /**
         * added to help the parsers
         */
        NULL;
        public static IdentityAssuranceLevel fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("level1".equals(codeString))
          return LEVEL1;
        if ("level2".equals(codeString))
          return LEVEL2;
        if ("level3".equals(codeString))
          return LEVEL3;
        if ("level4".equals(codeString))
          return LEVEL4;
        throw new Exception("Unknown IdentityAssuranceLevel code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case LEVEL1: return "level1";
            case LEVEL2: return "level2";
            case LEVEL3: return "level3";
            case LEVEL4: return "level4";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case LEVEL1: return "http://hl7.org/fhir/identity-assuranceLevel";
            case LEVEL2: return "http://hl7.org/fhir/identity-assuranceLevel";
            case LEVEL3: return "http://hl7.org/fhir/identity-assuranceLevel";
            case LEVEL4: return "http://hl7.org/fhir/identity-assuranceLevel";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case LEVEL1: return "Little or no confidence in the asserted identity's accuracy.";
            case LEVEL2: return "Some confidence in the asserted identity's accuracy.";
            case LEVEL3: return "High confidence in the asserted identity's accuracy.";
            case LEVEL4: return "Very high confidence in the asserted identity's accuracy.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case LEVEL1: return "Level 1";
            case LEVEL2: return "Level 2";
            case LEVEL3: return "Level 3";
            case LEVEL4: return "Level 4";
            default: return "?";
          }
        }
    }

  public static class IdentityAssuranceLevelEnumFactory implements EnumFactory<IdentityAssuranceLevel> {
    public IdentityAssuranceLevel fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("level1".equals(codeString))
          return IdentityAssuranceLevel.LEVEL1;
        if ("level2".equals(codeString))
          return IdentityAssuranceLevel.LEVEL2;
        if ("level3".equals(codeString))
          return IdentityAssuranceLevel.LEVEL3;
        if ("level4".equals(codeString))
          return IdentityAssuranceLevel.LEVEL4;
        throw new IllegalArgumentException("Unknown IdentityAssuranceLevel code '"+codeString+"'");
        }
    public String toCode(IdentityAssuranceLevel code) {
      if (code == IdentityAssuranceLevel.LEVEL1)
        return "level1";
      if (code == IdentityAssuranceLevel.LEVEL2)
        return "level2";
      if (code == IdentityAssuranceLevel.LEVEL3)
        return "level3";
      if (code == IdentityAssuranceLevel.LEVEL4)
        return "level4";
      return "?";
      }
    }

    @Block()
    public static class PersonLinkComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The resource to which this actual person is associated.
         */
        @Child(name = "target", type = {Patient.class, Practitioner.class, RelatedPerson.class, Person.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The resource to which this actual person is associated", formalDefinition="The resource to which this actual person is associated." )
        protected Reference target;

        /**
         * The actual object that is the target of the reference (The resource to which this actual person is associated.)
         */
        protected Resource targetTarget;

        /**
         * Level of assurance that this link is actually associated with the target resource.
         */
        @Child(name = "assurance", type = {CodeType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="level1 | level2 | level3 | level4", formalDefinition="Level of assurance that this link is actually associated with the target resource." )
        protected Enumeration<IdentityAssuranceLevel> assurance;

        private static final long serialVersionUID = 508763647L;

    /*
     * Constructor
     */
      public PersonLinkComponent() {
        super();
      }

    /*
     * Constructor
     */
      public PersonLinkComponent(Reference target) {
        super();
        this.target = target;
      }

        /**
         * @return {@link #target} (The resource to which this actual person is associated.)
         */
        public Reference getTarget() { 
          if (this.target == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PersonLinkComponent.target");
            else if (Configuration.doAutoCreate())
              this.target = new Reference(); // cc
          return this.target;
        }

        public boolean hasTarget() { 
          return this.target != null && !this.target.isEmpty();
        }

        /**
         * @param value {@link #target} (The resource to which this actual person is associated.)
         */
        public PersonLinkComponent setTarget(Reference value) { 
          this.target = value;
          return this;
        }

        /**
         * @return {@link #target} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The resource to which this actual person is associated.)
         */
        public Resource getTargetTarget() { 
          return this.targetTarget;
        }

        /**
         * @param value {@link #target} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The resource to which this actual person is associated.)
         */
        public PersonLinkComponent setTargetTarget(Resource value) { 
          this.targetTarget = value;
          return this;
        }

        /**
         * @return {@link #assurance} (Level of assurance that this link is actually associated with the target resource.). This is the underlying object with id, value and extensions. The accessor "getAssurance" gives direct access to the value
         */
        public Enumeration<IdentityAssuranceLevel> getAssuranceElement() { 
          if (this.assurance == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PersonLinkComponent.assurance");
            else if (Configuration.doAutoCreate())
              this.assurance = new Enumeration<IdentityAssuranceLevel>(new IdentityAssuranceLevelEnumFactory()); // bb
          return this.assurance;
        }

        public boolean hasAssuranceElement() { 
          return this.assurance != null && !this.assurance.isEmpty();
        }

        public boolean hasAssurance() { 
          return this.assurance != null && !this.assurance.isEmpty();
        }

        /**
         * @param value {@link #assurance} (Level of assurance that this link is actually associated with the target resource.). This is the underlying object with id, value and extensions. The accessor "getAssurance" gives direct access to the value
         */
        public PersonLinkComponent setAssuranceElement(Enumeration<IdentityAssuranceLevel> value) { 
          this.assurance = value;
          return this;
        }

        /**
         * @return Level of assurance that this link is actually associated with the target resource.
         */
        public IdentityAssuranceLevel getAssurance() { 
          return this.assurance == null ? null : this.assurance.getValue();
        }

        /**
         * @param value Level of assurance that this link is actually associated with the target resource.
         */
        public PersonLinkComponent setAssurance(IdentityAssuranceLevel value) { 
          if (value == null)
            this.assurance = null;
          else {
            if (this.assurance == null)
              this.assurance = new Enumeration<IdentityAssuranceLevel>(new IdentityAssuranceLevelEnumFactory());
            this.assurance.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("target", "Reference(Patient|Practitioner|RelatedPerson|Person)", "The resource to which this actual person is associated.", 0, java.lang.Integer.MAX_VALUE, target));
          childrenList.add(new Property("assurance", "code", "Level of assurance that this link is actually associated with the target resource.", 0, java.lang.Integer.MAX_VALUE, assurance));
        }

      public PersonLinkComponent copy() {
        PersonLinkComponent dst = new PersonLinkComponent();
        copyValues(dst);
        dst.target = target == null ? null : target.copy();
        dst.assurance = assurance == null ? null : assurance.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof PersonLinkComponent))
          return false;
        PersonLinkComponent o = (PersonLinkComponent) other;
        return compareDeep(target, o.target, true) && compareDeep(assurance, o.assurance, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof PersonLinkComponent))
          return false;
        PersonLinkComponent o = (PersonLinkComponent) other;
        return compareValues(assurance, o.assurance, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (target == null || target.isEmpty()) && (assurance == null || assurance.isEmpty())
          ;
      }

  }

    /**
     * Identifier for a person within a particular scope.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="A Human identifier for this person", formalDefinition="Identifier for a person within a particular scope." )
    protected List<Identifier> identifier;

    /**
     * A name associated with the person.
     */
    @Child(name = "name", type = {HumanName.class}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="A name associated with the person", formalDefinition="A name associated with the person." )
    protected List<HumanName> name;

    /**
     * A contact detail for the person, e.g. a telephone number or an email address.
     */
    @Child(name = "telecom", type = {ContactPoint.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="A contact detail for the person", formalDefinition="A contact detail for the person, e.g. a telephone number or an email address." )
    protected List<ContactPoint> telecom;

    /**
     * Administrative Gender.
     */
    @Child(name = "gender", type = {CodeType.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="male | female | other | unknown", formalDefinition="Administrative Gender." )
    protected Enumeration<AdministrativeGender> gender;

    /**
     * The birth date for the person.
     */
    @Child(name = "birthDate", type = {DateType.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The date on which the person was born", formalDefinition="The birth date for the person." )
    protected DateType birthDate;

    /**
     * One or more addresses for the person.
     */
    @Child(name = "address", type = {Address.class}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="One or more addresses for the person", formalDefinition="One or more addresses for the person." )
    protected List<Address> address;

    /**
     * An image that can be displayed as a thumbnail of the person to enhance the identification of the individual.
     */
    @Child(name = "photo", type = {Attachment.class}, order=6, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Image of the Person", formalDefinition="An image that can be displayed as a thumbnail of the person to enhance the identification of the individual." )
    protected Attachment photo;

    /**
     * The Organization that is the custodian of the person record.
     */
    @Child(name = "managingOrganization", type = {Organization.class}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The Organization that is the custodian of the person record", formalDefinition="The Organization that is the custodian of the person record." )
    protected Reference managingOrganization;

    /**
     * The actual object that is the target of the reference (The Organization that is the custodian of the person record.)
     */
    protected Organization managingOrganizationTarget;

    /**
     * Whether this person's record is in active use.
     */
    @Child(name = "active", type = {BooleanType.class}, order=8, min=0, max=1, modifier=true, summary=true)
    @Description(shortDefinition="This person's record is in active use", formalDefinition="Whether this person's record is in active use." )
    protected BooleanType active;

    /**
     * Link to a resource that concerns the same actual person.
     */
    @Child(name = "link", type = {}, order=9, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Link to a resource that concerns the same actual person", formalDefinition="Link to a resource that concerns the same actual person." )
    protected List<PersonLinkComponent> link;

    private static final long serialVersionUID = -117464654L;

  /*
   * Constructor
   */
    public Person() {
      super();
    }

    /**
     * @return {@link #identifier} (Identifier for a person within a particular scope.)
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
     * @return {@link #identifier} (Identifier for a person within a particular scope.)
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
    public Person addIdentifier(Identifier t) { //3
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
     * @return {@link #name} (A name associated with the person.)
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
    public Person addName(HumanName t) { //3
      if (t == null)
        return this;
      if (this.name == null)
        this.name = new ArrayList<HumanName>();
      this.name.add(t);
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
    public Person addTelecom(ContactPoint t) { //3
      if (t == null)
        return this;
      if (this.telecom == null)
        this.telecom = new ArrayList<ContactPoint>();
      this.telecom.add(t);
      return this;
    }

    /**
     * @return {@link #gender} (Administrative Gender.). This is the underlying object with id, value and extensions. The accessor "getGender" gives direct access to the value
     */
    public Enumeration<AdministrativeGender> getGenderElement() { 
      if (this.gender == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Person.gender");
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
     * @param value {@link #gender} (Administrative Gender.). This is the underlying object with id, value and extensions. The accessor "getGender" gives direct access to the value
     */
    public Person setGenderElement(Enumeration<AdministrativeGender> value) { 
      this.gender = value;
      return this;
    }

    /**
     * @return Administrative Gender.
     */
    public AdministrativeGender getGender() { 
      return this.gender == null ? null : this.gender.getValue();
    }

    /**
     * @param value Administrative Gender.
     */
    public Person setGender(AdministrativeGender value) { 
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
     * @return {@link #birthDate} (The birth date for the person.). This is the underlying object with id, value and extensions. The accessor "getBirthDate" gives direct access to the value
     */
    public DateType getBirthDateElement() { 
      if (this.birthDate == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Person.birthDate");
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
     * @param value {@link #birthDate} (The birth date for the person.). This is the underlying object with id, value and extensions. The accessor "getBirthDate" gives direct access to the value
     */
    public Person setBirthDateElement(DateType value) { 
      this.birthDate = value;
      return this;
    }

    /**
     * @return The birth date for the person.
     */
    public Date getBirthDate() { 
      return this.birthDate == null ? null : this.birthDate.getValue();
    }

    /**
     * @param value The birth date for the person.
     */
    public Person setBirthDate(Date value) { 
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
     * @return {@link #address} (One or more addresses for the person.)
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
     * @return {@link #address} (One or more addresses for the person.)
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
    public Person addAddress(Address t) { //3
      if (t == null)
        return this;
      if (this.address == null)
        this.address = new ArrayList<Address>();
      this.address.add(t);
      return this;
    }

    /**
     * @return {@link #photo} (An image that can be displayed as a thumbnail of the person to enhance the identification of the individual.)
     */
    public Attachment getPhoto() { 
      if (this.photo == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Person.photo");
        else if (Configuration.doAutoCreate())
          this.photo = new Attachment(); // cc
      return this.photo;
    }

    public boolean hasPhoto() { 
      return this.photo != null && !this.photo.isEmpty();
    }

    /**
     * @param value {@link #photo} (An image that can be displayed as a thumbnail of the person to enhance the identification of the individual.)
     */
    public Person setPhoto(Attachment value) { 
      this.photo = value;
      return this;
    }

    /**
     * @return {@link #managingOrganization} (The Organization that is the custodian of the person record.)
     */
    public Reference getManagingOrganization() { 
      if (this.managingOrganization == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Person.managingOrganization");
        else if (Configuration.doAutoCreate())
          this.managingOrganization = new Reference(); // cc
      return this.managingOrganization;
    }

    public boolean hasManagingOrganization() { 
      return this.managingOrganization != null && !this.managingOrganization.isEmpty();
    }

    /**
     * @param value {@link #managingOrganization} (The Organization that is the custodian of the person record.)
     */
    public Person setManagingOrganization(Reference value) { 
      this.managingOrganization = value;
      return this;
    }

    /**
     * @return {@link #managingOrganization} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The Organization that is the custodian of the person record.)
     */
    public Organization getManagingOrganizationTarget() { 
      if (this.managingOrganizationTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Person.managingOrganization");
        else if (Configuration.doAutoCreate())
          this.managingOrganizationTarget = new Organization(); // aa
      return this.managingOrganizationTarget;
    }

    /**
     * @param value {@link #managingOrganization} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The Organization that is the custodian of the person record.)
     */
    public Person setManagingOrganizationTarget(Organization value) { 
      this.managingOrganizationTarget = value;
      return this;
    }

    /**
     * @return {@link #active} (Whether this person's record is in active use.). This is the underlying object with id, value and extensions. The accessor "getActive" gives direct access to the value
     */
    public BooleanType getActiveElement() { 
      if (this.active == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Person.active");
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
     * @param value {@link #active} (Whether this person's record is in active use.). This is the underlying object with id, value and extensions. The accessor "getActive" gives direct access to the value
     */
    public Person setActiveElement(BooleanType value) { 
      this.active = value;
      return this;
    }

    /**
     * @return Whether this person's record is in active use.
     */
    public boolean getActive() { 
      return this.active == null || this.active.isEmpty() ? false : this.active.getValue();
    }

    /**
     * @param value Whether this person's record is in active use.
     */
    public Person setActive(boolean value) { 
        if (this.active == null)
          this.active = new BooleanType();
        this.active.setValue(value);
      return this;
    }

    /**
     * @return {@link #link} (Link to a resource that concerns the same actual person.)
     */
    public List<PersonLinkComponent> getLink() { 
      if (this.link == null)
        this.link = new ArrayList<PersonLinkComponent>();
      return this.link;
    }

    public boolean hasLink() { 
      if (this.link == null)
        return false;
      for (PersonLinkComponent item : this.link)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #link} (Link to a resource that concerns the same actual person.)
     */
    // syntactic sugar
    public PersonLinkComponent addLink() { //3
      PersonLinkComponent t = new PersonLinkComponent();
      if (this.link == null)
        this.link = new ArrayList<PersonLinkComponent>();
      this.link.add(t);
      return t;
    }

    // syntactic sugar
    public Person addLink(PersonLinkComponent t) { //3
      if (t == null)
        return this;
      if (this.link == null)
        this.link = new ArrayList<PersonLinkComponent>();
      this.link.add(t);
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "Identifier for a person within a particular scope.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("name", "HumanName", "A name associated with the person.", 0, java.lang.Integer.MAX_VALUE, name));
        childrenList.add(new Property("telecom", "ContactPoint", "A contact detail for the person, e.g. a telephone number or an email address.", 0, java.lang.Integer.MAX_VALUE, telecom));
        childrenList.add(new Property("gender", "code", "Administrative Gender.", 0, java.lang.Integer.MAX_VALUE, gender));
        childrenList.add(new Property("birthDate", "date", "The birth date for the person.", 0, java.lang.Integer.MAX_VALUE, birthDate));
        childrenList.add(new Property("address", "Address", "One or more addresses for the person.", 0, java.lang.Integer.MAX_VALUE, address));
        childrenList.add(new Property("photo", "Attachment", "An image that can be displayed as a thumbnail of the person to enhance the identification of the individual.", 0, java.lang.Integer.MAX_VALUE, photo));
        childrenList.add(new Property("managingOrganization", "Reference(Organization)", "The Organization that is the custodian of the person record.", 0, java.lang.Integer.MAX_VALUE, managingOrganization));
        childrenList.add(new Property("active", "boolean", "Whether this person's record is in active use.", 0, java.lang.Integer.MAX_VALUE, active));
        childrenList.add(new Property("link", "", "Link to a resource that concerns the same actual person.", 0, java.lang.Integer.MAX_VALUE, link));
      }

      public Person copy() {
        Person dst = new Person();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
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
        if (address != null) {
          dst.address = new ArrayList<Address>();
          for (Address i : address)
            dst.address.add(i.copy());
        };
        dst.photo = photo == null ? null : photo.copy();
        dst.managingOrganization = managingOrganization == null ? null : managingOrganization.copy();
        dst.active = active == null ? null : active.copy();
        if (link != null) {
          dst.link = new ArrayList<PersonLinkComponent>();
          for (PersonLinkComponent i : link)
            dst.link.add(i.copy());
        };
        return dst;
      }

      protected Person typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof Person))
          return false;
        Person o = (Person) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(name, o.name, true) && compareDeep(telecom, o.telecom, true)
           && compareDeep(gender, o.gender, true) && compareDeep(birthDate, o.birthDate, true) && compareDeep(address, o.address, true)
           && compareDeep(photo, o.photo, true) && compareDeep(managingOrganization, o.managingOrganization, true)
           && compareDeep(active, o.active, true) && compareDeep(link, o.link, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Person))
          return false;
        Person o = (Person) other;
        return compareValues(gender, o.gender, true) && compareValues(birthDate, o.birthDate, true) && compareValues(active, o.active, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (name == null || name.isEmpty())
           && (telecom == null || telecom.isEmpty()) && (gender == null || gender.isEmpty()) && (birthDate == null || birthDate.isEmpty())
           && (address == null || address.isEmpty()) && (photo == null || photo.isEmpty()) && (managingOrganization == null || managingOrganization.isEmpty())
           && (active == null || active.isEmpty()) && (link == null || link.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Person;
   }

  @SearchParamDefinition(name="identifier", path="Person.identifier", description="A person Identifier", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
  @SearchParamDefinition(name="address", path="Person.address", description="An address in any kind of address/part", type="string" )
  public static final String SP_ADDRESS = "address";
  @SearchParamDefinition(name="birthdate", path="Person.birthDate", description="The person's date of birth", type="date" )
  public static final String SP_BIRTHDATE = "birthdate";
  @SearchParamDefinition(name="address-state", path="Person.address.state", description="A state specified in an address", type="string" )
  public static final String SP_ADDRESSSTATE = "address-state";
  @SearchParamDefinition(name="gender", path="Person.gender", description="The gender of the person", type="token" )
  public static final String SP_GENDER = "gender";
  @SearchParamDefinition(name="practitioner", path="Person.link.target", description="The Person links to this Practitioner", type="reference" )
  public static final String SP_PRACTITIONER = "practitioner";
  @SearchParamDefinition(name="link", path="Person.link.target", description="Any link has this Patient, Person, RelatedPerson or Practitioner reference", type="reference" )
  public static final String SP_LINK = "link";
  @SearchParamDefinition(name="relatedperson", path="Person.link.target", description="The Person links to this RelatedPerson", type="reference" )
  public static final String SP_RELATEDPERSON = "relatedperson";
  @SearchParamDefinition(name="address-postalcode", path="Person.address.postalCode", description="A postalCode specified in an address", type="string" )
  public static final String SP_ADDRESSPOSTALCODE = "address-postalcode";
  @SearchParamDefinition(name="address-country", path="Person.address.country", description="A country specified in an address", type="string" )
  public static final String SP_ADDRESSCOUNTRY = "address-country";
  @SearchParamDefinition(name="phonetic", path="Person.name", description="A portion of name using some kind of phonetic matching algorithm", type="string" )
  public static final String SP_PHONETIC = "phonetic";
  @SearchParamDefinition(name="phone", path="Person.telecom(system=phone)", description="A value in a phone contact", type="token" )
  public static final String SP_PHONE = "phone";
  @SearchParamDefinition(name="patient", path="Person.link.target", description="The Person links to this Patient", type="reference" )
  public static final String SP_PATIENT = "patient";
  @SearchParamDefinition(name="organization", path="Person.managingOrganization", description="The organization at which this person record is being managed", type="reference" )
  public static final String SP_ORGANIZATION = "organization";
  @SearchParamDefinition(name="name", path="Person.name", description="A portion of name in any name part", type="string" )
  public static final String SP_NAME = "name";
  @SearchParamDefinition(name="address-use", path="Person.address.use", description="A use code specified in an address", type="token" )
  public static final String SP_ADDRESSUSE = "address-use";
  @SearchParamDefinition(name="telecom", path="Person.telecom", description="The value in any kind of contact", type="token" )
  public static final String SP_TELECOM = "telecom";
  @SearchParamDefinition(name="address-city", path="Person.address.city", description="A city specified in an address", type="string" )
  public static final String SP_ADDRESSCITY = "address-city";
  @SearchParamDefinition(name="email", path="Person.telecom(system=email)", description="A value in an email contact", type="token" )
  public static final String SP_EMAIL = "email";

}

