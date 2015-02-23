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

// Generated on Wed, Feb 18, 2015 12:09-0500 for FHIR v0.4.0

import java.util.*;

import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.instance.model.annotations.ResourceDef;
import org.hl7.fhir.instance.model.annotations.SearchParamDefinition;
import org.hl7.fhir.instance.model.annotations.Block;
import org.hl7.fhir.instance.model.annotations.Child;
import org.hl7.fhir.instance.model.annotations.Description;
/**
 * A formally or informally recognized grouping of people or organizations formed for the purpose of achieving some form of collective action.  Includes companies, institutions, corporations, departments, community groups, healthcare practice groups, etc.
 */
@ResourceDef(name="Organization", profile="http://hl7.org/fhir/Profile/Organization")
public class Organization extends DomainResource {

    public enum AdministrativeGender {
        /**
         * Male
         */
        MALE, 
        /**
         * Female
         */
        FEMALE, 
        /**
         * Other
         */
        OTHER, 
        /**
         * Unknown
         */
        UNKNOWN, 
        /**
         * added to help the parsers
         */
        NULL;
        public static AdministrativeGender fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("male".equals(codeString))
          return MALE;
        if ("female".equals(codeString))
          return FEMALE;
        if ("other".equals(codeString))
          return OTHER;
        if ("unknown".equals(codeString))
          return UNKNOWN;
        throw new Exception("Unknown AdministrativeGender code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case MALE: return "male";
            case FEMALE: return "female";
            case OTHER: return "other";
            case UNKNOWN: return "unknown";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case MALE: return "";
            case FEMALE: return "";
            case OTHER: return "";
            case UNKNOWN: return "";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case MALE: return "Male";
            case FEMALE: return "Female";
            case OTHER: return "Other";
            case UNKNOWN: return "Unknown";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case MALE: return "male";
            case FEMALE: return "female";
            case OTHER: return "other";
            case UNKNOWN: return "unknown";
            default: return "?";
          }
        }
    }

  public static class AdministrativeGenderEnumFactory implements EnumFactory<AdministrativeGender> {
    public AdministrativeGender fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("male".equals(codeString))
          return AdministrativeGender.MALE;
        if ("female".equals(codeString))
          return AdministrativeGender.FEMALE;
        if ("other".equals(codeString))
          return AdministrativeGender.OTHER;
        if ("unknown".equals(codeString))
          return AdministrativeGender.UNKNOWN;
        throw new IllegalArgumentException("Unknown AdministrativeGender code '"+codeString+"'");
        }
    public String toCode(AdministrativeGender code) {
      if (code == AdministrativeGender.MALE)
        return "male";
      if (code == AdministrativeGender.FEMALE)
        return "female";
      if (code == AdministrativeGender.OTHER)
        return "other";
      if (code == AdministrativeGender.UNKNOWN)
        return "unknown";
      return "?";
      }
    }

    @Block()
    public static class OrganizationContactComponent extends BackboneElement {
        /**
         * Indicates a purpose for which the contact can be reached.
         */
        @Child(name="purpose", type={CodeableConcept.class}, order=1, min=0, max=1)
        @Description(shortDefinition="The type of contact", formalDefinition="Indicates a purpose for which the contact can be reached." )
        protected CodeableConcept purpose;

        /**
         * A name associated with the contact.
         */
        @Child(name="name", type={HumanName.class}, order=2, min=0, max=1)
        @Description(shortDefinition="A name associated with the contact", formalDefinition="A name associated with the contact." )
        protected HumanName name;

        /**
         * A contact detail (e.g. a telephone number or an email address) by which the party may be contacted.
         */
        @Child(name="telecom", type={ContactPoint.class}, order=3, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="Contact details (telephone, email, etc)  for a contact", formalDefinition="A contact detail (e.g. a telephone number or an email address) by which the party may be contacted." )
        protected List<ContactPoint> telecom;

        /**
         * Visiting or postal addresses for the contact.
         */
        @Child(name="address", type={Address.class}, order=4, min=0, max=1)
        @Description(shortDefinition="Visiting or postal addresses for the contact", formalDefinition="Visiting or postal addresses for the contact." )
        protected Address address;

        /**
         * Administrative Gender - the gender that the person is considered to have for administration and record keeping purposes.
         */
        @Child(name="gender", type={CodeType.class}, order=5, min=0, max=1)
        @Description(shortDefinition="male | female | other | unknown", formalDefinition="Administrative Gender - the gender that the person is considered to have for administration and record keeping purposes." )
        protected Enumeration<AdministrativeGender> gender;

        private static final long serialVersionUID = -1433864122L;

      public OrganizationContactComponent() {
        super();
      }

        /**
         * @return {@link #purpose} (Indicates a purpose for which the contact can be reached.)
         */
        public CodeableConcept getPurpose() { 
          if (this.purpose == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OrganizationContactComponent.purpose");
            else if (Configuration.doAutoCreate())
              this.purpose = new CodeableConcept(); // cc
          return this.purpose;
        }

        public boolean hasPurpose() { 
          return this.purpose != null && !this.purpose.isEmpty();
        }

        /**
         * @param value {@link #purpose} (Indicates a purpose for which the contact can be reached.)
         */
        public OrganizationContactComponent setPurpose(CodeableConcept value) { 
          this.purpose = value;
          return this;
        }

        /**
         * @return {@link #name} (A name associated with the contact.)
         */
        public HumanName getName() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OrganizationContactComponent.name");
            else if (Configuration.doAutoCreate())
              this.name = new HumanName(); // cc
          return this.name;
        }

        public boolean hasName() { 
          return this.name != null && !this.name.isEmpty();
        }

        /**
         * @param value {@link #name} (A name associated with the contact.)
         */
        public OrganizationContactComponent setName(HumanName value) { 
          this.name = value;
          return this;
        }

        /**
         * @return {@link #telecom} (A contact detail (e.g. a telephone number or an email address) by which the party may be contacted.)
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
         * @return {@link #telecom} (A contact detail (e.g. a telephone number or an email address) by which the party may be contacted.)
         */
    // syntactic sugar
        public ContactPoint addTelecom() { //3
          ContactPoint t = new ContactPoint();
          if (this.telecom == null)
            this.telecom = new ArrayList<ContactPoint>();
          this.telecom.add(t);
          return t;
        }

        /**
         * @return {@link #address} (Visiting or postal addresses for the contact.)
         */
        public Address getAddress() { 
          if (this.address == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OrganizationContactComponent.address");
            else if (Configuration.doAutoCreate())
              this.address = new Address(); // cc
          return this.address;
        }

        public boolean hasAddress() { 
          return this.address != null && !this.address.isEmpty();
        }

        /**
         * @param value {@link #address} (Visiting or postal addresses for the contact.)
         */
        public OrganizationContactComponent setAddress(Address value) { 
          this.address = value;
          return this;
        }

        /**
         * @return {@link #gender} (Administrative Gender - the gender that the person is considered to have for administration and record keeping purposes.). This is the underlying object with id, value and extensions. The accessor "getGender" gives direct access to the value
         */
        public Enumeration<AdministrativeGender> getGenderElement() { 
          if (this.gender == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OrganizationContactComponent.gender");
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
        public OrganizationContactComponent setGenderElement(Enumeration<AdministrativeGender> value) { 
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
        public OrganizationContactComponent setGender(AdministrativeGender value) { 
          if (value == null)
            this.gender = null;
          else {
            if (this.gender == null)
              this.gender = new Enumeration<AdministrativeGender>(new AdministrativeGenderEnumFactory());
            this.gender.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("purpose", "CodeableConcept", "Indicates a purpose for which the contact can be reached.", 0, java.lang.Integer.MAX_VALUE, purpose));
          childrenList.add(new Property("name", "HumanName", "A name associated with the contact.", 0, java.lang.Integer.MAX_VALUE, name));
          childrenList.add(new Property("telecom", "ContactPoint", "A contact detail (e.g. a telephone number or an email address) by which the party may be contacted.", 0, java.lang.Integer.MAX_VALUE, telecom));
          childrenList.add(new Property("address", "Address", "Visiting or postal addresses for the contact.", 0, java.lang.Integer.MAX_VALUE, address));
          childrenList.add(new Property("gender", "code", "Administrative Gender - the gender that the person is considered to have for administration and record keeping purposes.", 0, java.lang.Integer.MAX_VALUE, gender));
        }

      public OrganizationContactComponent copy() {
        OrganizationContactComponent dst = new OrganizationContactComponent();
        copyValues(dst);
        dst.purpose = purpose == null ? null : purpose.copy();
        dst.name = name == null ? null : name.copy();
        if (telecom != null) {
          dst.telecom = new ArrayList<ContactPoint>();
          for (ContactPoint i : telecom)
            dst.telecom.add(i.copy());
        };
        dst.address = address == null ? null : address.copy();
        dst.gender = gender == null ? null : gender.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof OrganizationContactComponent))
          return false;
        OrganizationContactComponent o = (OrganizationContactComponent) other;
        return compareDeep(purpose, o.purpose, true) && compareDeep(name, o.name, true) && compareDeep(telecom, o.telecom, true)
           && compareDeep(address, o.address, true) && compareDeep(gender, o.gender, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof OrganizationContactComponent))
          return false;
        OrganizationContactComponent o = (OrganizationContactComponent) other;
        return compareValues(gender, o.gender, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (purpose == null || purpose.isEmpty()) && (name == null || name.isEmpty())
           && (telecom == null || telecom.isEmpty()) && (address == null || address.isEmpty()) && (gender == null || gender.isEmpty())
          ;
      }

  }

    /**
     * Identifier for the organization that is used to identify the organization across multiple disparate systems.
     */
    @Child(name = "identifier", type = {Identifier.class}, order = 0, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Identifies this organization  across multiple systems", formalDefinition="Identifier for the organization that is used to identify the organization across multiple disparate systems." )
    protected List<Identifier> identifier;

    /**
     * A name associated with the organization.
     */
    @Child(name = "name", type = {StringType.class}, order = 1, min = 0, max = 1)
    @Description(shortDefinition="Name used for the organization", formalDefinition="A name associated with the organization." )
    protected StringType name;

    /**
     * The kind of organization that this is.
     */
    @Child(name = "type", type = {CodeableConcept.class}, order = 2, min = 0, max = 1)
    @Description(shortDefinition="Kind of organization", formalDefinition="The kind of organization that this is." )
    protected CodeableConcept type;

    /**
     * A contact detail for the organization.
     */
    @Child(name = "telecom", type = {ContactPoint.class}, order = 3, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="A contact detail for the organization", formalDefinition="A contact detail for the organization." )
    protected List<ContactPoint> telecom;

    /**
     * An address for the organization.
     */
    @Child(name = "address", type = {Address.class}, order = 4, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="An address for the organization", formalDefinition="An address for the organization." )
    protected List<Address> address;

    /**
     * The organization of which this organization forms a part.
     */
    @Child(name = "partOf", type = {Organization.class}, order = 5, min = 0, max = 1)
    @Description(shortDefinition="The organization of which this organization forms a part", formalDefinition="The organization of which this organization forms a part." )
    protected Reference partOf;

    /**
     * The actual object that is the target of the reference (The organization of which this organization forms a part.)
     */
    protected Organization partOfTarget;

    /**
     * Contact for the organization for a certain purpose.
     */
    @Child(name = "contact", type = {}, order = 6, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Contact for the organization for a certain purpose", formalDefinition="Contact for the organization for a certain purpose." )
    protected List<OrganizationContactComponent> contact;

    /**
     * Location(s) the organization uses to provide services.
     */
    @Child(name = "location", type = {Location.class}, order = 7, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Location(s) the organization uses to provide services", formalDefinition="Location(s) the organization uses to provide services." )
    protected List<Reference> location;
    /**
     * The actual objects that are the target of the reference (Location(s) the organization uses to provide services.)
     */
    protected List<Location> locationTarget;


    /**
     * Whether the organization's record is still in active use.
     */
    @Child(name = "active", type = {BooleanType.class}, order = 8, min = 0, max = 1)
    @Description(shortDefinition="Whether the organization's record is still in active use", formalDefinition="Whether the organization's record is still in active use." )
    protected BooleanType active;

    private static final long serialVersionUID = -75313696L;

    public Organization() {
      super();
    }

    /**
     * @return {@link #identifier} (Identifier for the organization that is used to identify the organization across multiple disparate systems.)
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
     * @return {@link #identifier} (Identifier for the organization that is used to identify the organization across multiple disparate systems.)
     */
    // syntactic sugar
    public Identifier addIdentifier() { //3
      Identifier t = new Identifier();
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return t;
    }

    /**
     * @return {@link #name} (A name associated with the organization.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public StringType getNameElement() { 
      if (this.name == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Organization.name");
        else if (Configuration.doAutoCreate())
          this.name = new StringType(); // bb
      return this.name;
    }

    public boolean hasNameElement() { 
      return this.name != null && !this.name.isEmpty();
    }

    public boolean hasName() { 
      return this.name != null && !this.name.isEmpty();
    }

    /**
     * @param value {@link #name} (A name associated with the organization.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public Organization setNameElement(StringType value) { 
      this.name = value;
      return this;
    }

    /**
     * @return A name associated with the organization.
     */
    public String getName() { 
      return this.name == null ? null : this.name.getValue();
    }

    /**
     * @param value A name associated with the organization.
     */
    public Organization setName(String value) { 
      if (Utilities.noString(value))
        this.name = null;
      else {
        if (this.name == null)
          this.name = new StringType();
        this.name.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #type} (The kind of organization that this is.)
     */
    public CodeableConcept getType() { 
      if (this.type == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Organization.type");
        else if (Configuration.doAutoCreate())
          this.type = new CodeableConcept(); // cc
      return this.type;
    }

    public boolean hasType() { 
      return this.type != null && !this.type.isEmpty();
    }

    /**
     * @param value {@link #type} (The kind of organization that this is.)
     */
    public Organization setType(CodeableConcept value) { 
      this.type = value;
      return this;
    }

    /**
     * @return {@link #telecom} (A contact detail for the organization.)
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
     * @return {@link #telecom} (A contact detail for the organization.)
     */
    // syntactic sugar
    public ContactPoint addTelecom() { //3
      ContactPoint t = new ContactPoint();
      if (this.telecom == null)
        this.telecom = new ArrayList<ContactPoint>();
      this.telecom.add(t);
      return t;
    }

    /**
     * @return {@link #address} (An address for the organization.)
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
     * @return {@link #address} (An address for the organization.)
     */
    // syntactic sugar
    public Address addAddress() { //3
      Address t = new Address();
      if (this.address == null)
        this.address = new ArrayList<Address>();
      this.address.add(t);
      return t;
    }

    /**
     * @return {@link #partOf} (The organization of which this organization forms a part.)
     */
    public Reference getPartOf() { 
      if (this.partOf == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Organization.partOf");
        else if (Configuration.doAutoCreate())
          this.partOf = new Reference(); // cc
      return this.partOf;
    }

    public boolean hasPartOf() { 
      return this.partOf != null && !this.partOf.isEmpty();
    }

    /**
     * @param value {@link #partOf} (The organization of which this organization forms a part.)
     */
    public Organization setPartOf(Reference value) { 
      this.partOf = value;
      return this;
    }

    /**
     * @return {@link #partOf} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The organization of which this organization forms a part.)
     */
    public Organization getPartOfTarget() { 
      if (this.partOfTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Organization.partOf");
        else if (Configuration.doAutoCreate())
          this.partOfTarget = new Organization(); // aa
      return this.partOfTarget;
    }

    /**
     * @param value {@link #partOf} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The organization of which this organization forms a part.)
     */
    public Organization setPartOfTarget(Organization value) { 
      this.partOfTarget = value;
      return this;
    }

    /**
     * @return {@link #contact} (Contact for the organization for a certain purpose.)
     */
    public List<OrganizationContactComponent> getContact() { 
      if (this.contact == null)
        this.contact = new ArrayList<OrganizationContactComponent>();
      return this.contact;
    }

    public boolean hasContact() { 
      if (this.contact == null)
        return false;
      for (OrganizationContactComponent item : this.contact)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #contact} (Contact for the organization for a certain purpose.)
     */
    // syntactic sugar
    public OrganizationContactComponent addContact() { //3
      OrganizationContactComponent t = new OrganizationContactComponent();
      if (this.contact == null)
        this.contact = new ArrayList<OrganizationContactComponent>();
      this.contact.add(t);
      return t;
    }

    /**
     * @return {@link #location} (Location(s) the organization uses to provide services.)
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
     * @return {@link #location} (Location(s) the organization uses to provide services.)
     */
    // syntactic sugar
    public Reference addLocation() { //3
      Reference t = new Reference();
      if (this.location == null)
        this.location = new ArrayList<Reference>();
      this.location.add(t);
      return t;
    }

    /**
     * @return {@link #location} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. Location(s) the organization uses to provide services.)
     */
    public List<Location> getLocationTarget() { 
      if (this.locationTarget == null)
        this.locationTarget = new ArrayList<Location>();
      return this.locationTarget;
    }

    // syntactic sugar
    /**
     * @return {@link #location} (Add an actual object that is the target of the reference. The reference library doesn't use these, but you can use this to hold the resources if you resolvethemt. Location(s) the organization uses to provide services.)
     */
    public Location addLocationTarget() { 
      Location r = new Location();
      if (this.locationTarget == null)
        this.locationTarget = new ArrayList<Location>();
      this.locationTarget.add(r);
      return r;
    }

    /**
     * @return {@link #active} (Whether the organization's record is still in active use.). This is the underlying object with id, value and extensions. The accessor "getActive" gives direct access to the value
     */
    public BooleanType getActiveElement() { 
      if (this.active == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Organization.active");
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
     * @param value {@link #active} (Whether the organization's record is still in active use.). This is the underlying object with id, value and extensions. The accessor "getActive" gives direct access to the value
     */
    public Organization setActiveElement(BooleanType value) { 
      this.active = value;
      return this;
    }

    /**
     * @return Whether the organization's record is still in active use.
     */
    public boolean getActive() { 
      return this.active == null ? false : this.active.getValue();
    }

    /**
     * @param value Whether the organization's record is still in active use.
     */
    public Organization setActive(boolean value) { 
        if (this.active == null)
          this.active = new BooleanType();
        this.active.setValue(value);
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "Identifier for the organization that is used to identify the organization across multiple disparate systems.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("name", "string", "A name associated with the organization.", 0, java.lang.Integer.MAX_VALUE, name));
        childrenList.add(new Property("type", "CodeableConcept", "The kind of organization that this is.", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("telecom", "ContactPoint", "A contact detail for the organization.", 0, java.lang.Integer.MAX_VALUE, telecom));
        childrenList.add(new Property("address", "Address", "An address for the organization.", 0, java.lang.Integer.MAX_VALUE, address));
        childrenList.add(new Property("partOf", "Reference(Organization)", "The organization of which this organization forms a part.", 0, java.lang.Integer.MAX_VALUE, partOf));
        childrenList.add(new Property("contact", "", "Contact for the organization for a certain purpose.", 0, java.lang.Integer.MAX_VALUE, contact));
        childrenList.add(new Property("location", "Reference(Location)", "Location(s) the organization uses to provide services.", 0, java.lang.Integer.MAX_VALUE, location));
        childrenList.add(new Property("active", "boolean", "Whether the organization's record is still in active use.", 0, java.lang.Integer.MAX_VALUE, active));
      }

      public Organization copy() {
        Organization dst = new Organization();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.name = name == null ? null : name.copy();
        dst.type = type == null ? null : type.copy();
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
        dst.partOf = partOf == null ? null : partOf.copy();
        if (contact != null) {
          dst.contact = new ArrayList<OrganizationContactComponent>();
          for (OrganizationContactComponent i : contact)
            dst.contact.add(i.copy());
        };
        if (location != null) {
          dst.location = new ArrayList<Reference>();
          for (Reference i : location)
            dst.location.add(i.copy());
        };
        dst.active = active == null ? null : active.copy();
        return dst;
      }

      protected Organization typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof Organization))
          return false;
        Organization o = (Organization) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(name, o.name, true) && compareDeep(type, o.type, true)
           && compareDeep(telecom, o.telecom, true) && compareDeep(address, o.address, true) && compareDeep(partOf, o.partOf, true)
           && compareDeep(contact, o.contact, true) && compareDeep(location, o.location, true) && compareDeep(active, o.active, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Organization))
          return false;
        Organization o = (Organization) other;
        return compareValues(name, o.name, true) && compareValues(active, o.active, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (name == null || name.isEmpty())
           && (type == null || type.isEmpty()) && (telecom == null || telecom.isEmpty()) && (address == null || address.isEmpty())
           && (partOf == null || partOf.isEmpty()) && (contact == null || contact.isEmpty()) && (location == null || location.isEmpty())
           && (active == null || active.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Organization;
   }

  @SearchParamDefinition(name = "identifier", path = "Organization.identifier", description = "Any identifier for the organization (not the accreditation issuer's identifier)", type = "token")
  public static final String SP_IDENTIFIER = "identifier";
  @SearchParamDefinition(name="partof", path="Organization.partOf", description="Search all organizations that are part of the given organization", type="reference" )
  public static final String SP_PARTOF = "partof";
  @SearchParamDefinition(name = "phonetic", path = "", description = "A portion of the organization's name using some kind of phonetic matching algorithm", type = "string")
  public static final String SP_PHONETIC = "phonetic";
  @SearchParamDefinition(name="name", path="Organization.name", description="A portion of the organization's name", type="string" )
  public static final String SP_NAME = "name";
  @SearchParamDefinition(name="active", path="Organization.active", description="Whether the organization's record is active", type="token" )
  public static final String SP_ACTIVE = "active";
  @SearchParamDefinition(name="type", path="Organization.type", description="A code for the type of organization", type="token" )
  public static final String SP_TYPE = "type";

}

