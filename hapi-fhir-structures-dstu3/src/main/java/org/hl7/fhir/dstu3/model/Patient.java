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
 * Demographics and other administrative information about an individual or animal receiving care or other health-related services.
 */
@ResourceDef(name="Patient", profile="http://hl7.org/fhir/Profile/Patient")
public class Patient extends DomainResource {

    public enum LinkType {
        /**
         * The patient resource containing this link must no longer be used. The link points forward to another patient resource that must be used in lieu of the patient resource that contains this link.
         */
        REPLACEDBY, 
        /**
         * The patient resource containing this link is the current active patient record. The link points back to an inactive patient resource that has been merged into this resource, and should be consulted to retrieve additional referenced information.
         */
        REPLACES, 
        /**
         * The patient resource containing this link is in use and valid but not considered the main source of information about a patient. The link points forward to another patient resource that should be consulted to retrieve additional patient information.
         */
        REFER, 
        /**
         * The patient resource containing this link is in use and valid, but points to another patient resource that is known to contain data about the same person. Data in this resource might overlap or contradict information found in the other patient resource. This link does not indicate any relative importance of the resources concerned, and both should be regarded as equally valid.
         */
        SEEALSO, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static LinkType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("replaced-by".equals(codeString))
          return REPLACEDBY;
        if ("replaces".equals(codeString))
          return REPLACES;
        if ("refer".equals(codeString))
          return REFER;
        if ("seealso".equals(codeString))
          return SEEALSO;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown LinkType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case REPLACEDBY: return "replaced-by";
            case REPLACES: return "replaces";
            case REFER: return "refer";
            case SEEALSO: return "seealso";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case REPLACEDBY: return "http://hl7.org/fhir/link-type";
            case REPLACES: return "http://hl7.org/fhir/link-type";
            case REFER: return "http://hl7.org/fhir/link-type";
            case SEEALSO: return "http://hl7.org/fhir/link-type";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case REPLACEDBY: return "The patient resource containing this link must no longer be used. The link points forward to another patient resource that must be used in lieu of the patient resource that contains this link.";
            case REPLACES: return "The patient resource containing this link is the current active patient record. The link points back to an inactive patient resource that has been merged into this resource, and should be consulted to retrieve additional referenced information.";
            case REFER: return "The patient resource containing this link is in use and valid but not considered the main source of information about a patient. The link points forward to another patient resource that should be consulted to retrieve additional patient information.";
            case SEEALSO: return "The patient resource containing this link is in use and valid, but points to another patient resource that is known to contain data about the same person. Data in this resource might overlap or contradict information found in the other patient resource. This link does not indicate any relative importance of the resources concerned, and both should be regarded as equally valid.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case REPLACEDBY: return "Replaced-by";
            case REPLACES: return "Replaces";
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
        if ("replaced-by".equals(codeString))
          return LinkType.REPLACEDBY;
        if ("replaces".equals(codeString))
          return LinkType.REPLACES;
        if ("refer".equals(codeString))
          return LinkType.REFER;
        if ("seealso".equals(codeString))
          return LinkType.SEEALSO;
        throw new IllegalArgumentException("Unknown LinkType code '"+codeString+"'");
        }
        public Enumeration<LinkType> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<LinkType>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("replaced-by".equals(codeString))
          return new Enumeration<LinkType>(this, LinkType.REPLACEDBY);
        if ("replaces".equals(codeString))
          return new Enumeration<LinkType>(this, LinkType.REPLACES);
        if ("refer".equals(codeString))
          return new Enumeration<LinkType>(this, LinkType.REFER);
        if ("seealso".equals(codeString))
          return new Enumeration<LinkType>(this, LinkType.SEEALSO);
        throw new FHIRException("Unknown LinkType code '"+codeString+"'");
        }
    public String toCode(LinkType code) {
      if (code == LinkType.REPLACEDBY)
        return "replaced-by";
      if (code == LinkType.REPLACES)
        return "replaces";
      if (code == LinkType.REFER)
        return "refer";
      if (code == LinkType.SEEALSO)
        return "seealso";
      return "?";
      }
    public String toSystem(LinkType code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class ContactComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The nature of the relationship between the patient and the contact person.
         */
        @Child(name = "relationship", type = {CodeableConcept.class}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="The kind of relationship", formalDefinition="The nature of the relationship between the patient and the contact person." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/v2-0131")
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
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/administrative-gender")
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

    /**
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

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ContactComponent setRelationship(List<CodeableConcept> theRelationship) { 
          this.relationship = theRelationship;
          return this;
        }

        public boolean hasRelationship() { 
          if (this.relationship == null)
            return false;
          for (CodeableConcept item : this.relationship)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public CodeableConcept addRelationship() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.relationship == null)
            this.relationship = new ArrayList<CodeableConcept>();
          this.relationship.add(t);
          return t;
        }

        public ContactComponent addRelationship(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.relationship == null)
            this.relationship = new ArrayList<CodeableConcept>();
          this.relationship.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #relationship}, creating it if it does not already exist
         */
        public CodeableConcept getRelationshipFirstRep() { 
          if (getRelationship().isEmpty()) {
            addRelationship();
          }
          return getRelationship().get(0);
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

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ContactComponent setTelecom(List<ContactPoint> theTelecom) { 
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

        public ContactComponent addTelecom(ContactPoint t) { //3
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

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -261851592: /*relationship*/ return this.relationship == null ? new Base[0] : this.relationship.toArray(new Base[this.relationship.size()]); // CodeableConcept
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // HumanName
        case -1429363305: /*telecom*/ return this.telecom == null ? new Base[0] : this.telecom.toArray(new Base[this.telecom.size()]); // ContactPoint
        case -1147692044: /*address*/ return this.address == null ? new Base[0] : new Base[] {this.address}; // Address
        case -1249512767: /*gender*/ return this.gender == null ? new Base[0] : new Base[] {this.gender}; // Enumeration<AdministrativeGender>
        case 1178922291: /*organization*/ return this.organization == null ? new Base[0] : new Base[] {this.organization}; // Reference
        case -991726143: /*period*/ return this.period == null ? new Base[0] : new Base[] {this.period}; // Period
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -261851592: // relationship
          this.getRelationship().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 3373707: // name
          this.name = castToHumanName(value); // HumanName
          return value;
        case -1429363305: // telecom
          this.getTelecom().add(castToContactPoint(value)); // ContactPoint
          return value;
        case -1147692044: // address
          this.address = castToAddress(value); // Address
          return value;
        case -1249512767: // gender
          value = new AdministrativeGenderEnumFactory().fromType(castToCode(value));
          this.gender = (Enumeration) value; // Enumeration<AdministrativeGender>
          return value;
        case 1178922291: // organization
          this.organization = castToReference(value); // Reference
          return value;
        case -991726143: // period
          this.period = castToPeriod(value); // Period
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("relationship")) {
          this.getRelationship().add(castToCodeableConcept(value));
        } else if (name.equals("name")) {
          this.name = castToHumanName(value); // HumanName
        } else if (name.equals("telecom")) {
          this.getTelecom().add(castToContactPoint(value));
        } else if (name.equals("address")) {
          this.address = castToAddress(value); // Address
        } else if (name.equals("gender")) {
          value = new AdministrativeGenderEnumFactory().fromType(castToCode(value));
          this.gender = (Enumeration) value; // Enumeration<AdministrativeGender>
        } else if (name.equals("organization")) {
          this.organization = castToReference(value); // Reference
        } else if (name.equals("period")) {
          this.period = castToPeriod(value); // Period
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -261851592:  return addRelationship(); 
        case 3373707:  return getName(); 
        case -1429363305:  return addTelecom(); 
        case -1147692044:  return getAddress(); 
        case -1249512767:  return getGenderElement();
        case 1178922291:  return getOrganization(); 
        case -991726143:  return getPeriod(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -261851592: /*relationship*/ return new String[] {"CodeableConcept"};
        case 3373707: /*name*/ return new String[] {"HumanName"};
        case -1429363305: /*telecom*/ return new String[] {"ContactPoint"};
        case -1147692044: /*address*/ return new String[] {"Address"};
        case -1249512767: /*gender*/ return new String[] {"code"};
        case 1178922291: /*organization*/ return new String[] {"Reference"};
        case -991726143: /*period*/ return new String[] {"Period"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("relationship")) {
          return addRelationship();
        }
        else if (name.equals("name")) {
          this.name = new HumanName();
          return this.name;
        }
        else if (name.equals("telecom")) {
          return addTelecom();
        }
        else if (name.equals("address")) {
          this.address = new Address();
          return this.address;
        }
        else if (name.equals("gender")) {
          throw new FHIRException("Cannot call addChild on a primitive type Patient.gender");
        }
        else if (name.equals("organization")) {
          this.organization = new Reference();
          return this.organization;
        }
        else if (name.equals("period")) {
          this.period = new Period();
          return this.period;
        }
        else
          return super.addChild(name);
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
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(relationship, name, telecom
          , address, gender, organization, period);
      }

  public String fhirType() {
    return "Patient.contact";

  }

  }

    @Block()
    public static class AnimalComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Identifies the high level taxonomic categorization of the kind of animal.
         */
        @Child(name = "species", type = {CodeableConcept.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="E.g. Dog, Cow", formalDefinition="Identifies the high level taxonomic categorization of the kind of animal." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/animal-species")
        protected CodeableConcept species;

        /**
         * Identifies the detailed categorization of the kind of animal.
         */
        @Child(name = "breed", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="E.g. Poodle, Angus", formalDefinition="Identifies the detailed categorization of the kind of animal." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/animal-breeds")
        protected CodeableConcept breed;

        /**
         * Indicates the current state of the animal's reproductive organs.
         */
        @Child(name = "genderStatus", type = {CodeableConcept.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="E.g. Neutered, Intact", formalDefinition="Indicates the current state of the animal's reproductive organs." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/animal-genderstatus")
        protected CodeableConcept genderStatus;

        private static final long serialVersionUID = -549738382L;

    /**
     * Constructor
     */
      public AnimalComponent() {
        super();
      }

    /**
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

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -2008465092: /*species*/ return this.species == null ? new Base[0] : new Base[] {this.species}; // CodeableConcept
        case 94001524: /*breed*/ return this.breed == null ? new Base[0] : new Base[] {this.breed}; // CodeableConcept
        case -678569453: /*genderStatus*/ return this.genderStatus == null ? new Base[0] : new Base[] {this.genderStatus}; // CodeableConcept
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -2008465092: // species
          this.species = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 94001524: // breed
          this.breed = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -678569453: // genderStatus
          this.genderStatus = castToCodeableConcept(value); // CodeableConcept
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("species")) {
          this.species = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("breed")) {
          this.breed = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("genderStatus")) {
          this.genderStatus = castToCodeableConcept(value); // CodeableConcept
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -2008465092:  return getSpecies(); 
        case 94001524:  return getBreed(); 
        case -678569453:  return getGenderStatus(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -2008465092: /*species*/ return new String[] {"CodeableConcept"};
        case 94001524: /*breed*/ return new String[] {"CodeableConcept"};
        case -678569453: /*genderStatus*/ return new String[] {"CodeableConcept"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("species")) {
          this.species = new CodeableConcept();
          return this.species;
        }
        else if (name.equals("breed")) {
          this.breed = new CodeableConcept();
          return this.breed;
        }
        else if (name.equals("genderStatus")) {
          this.genderStatus = new CodeableConcept();
          return this.genderStatus;
        }
        else
          return super.addChild(name);
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
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(species, breed, genderStatus
          );
      }

  public String fhirType() {
    return "Patient.animal";

  }

  }

    @Block()
    public static class PatientCommunicationComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The ISO-639-1 alpha 2 code in lower case for the language, optionally followed by a hyphen and the ISO-3166-1 alpha 2 code for the region in upper case; e.g. "en" for English, or "en-US" for American English versus "en-EN" for England English.
         */
        @Child(name = "language", type = {CodeableConcept.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The language which can be used to communicate with the patient about his or her health", formalDefinition="The ISO-639-1 alpha 2 code in lower case for the language, optionally followed by a hyphen and the ISO-3166-1 alpha 2 code for the region in upper case; e.g. \"en\" for English, or \"en-US\" for American English versus \"en-EN\" for England English." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/languages")
        protected CodeableConcept language;

        /**
         * Indicates whether or not the patient prefers this language (over other languages he masters up a certain level).
         */
        @Child(name = "preferred", type = {BooleanType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Language preference indicator", formalDefinition="Indicates whether or not the patient prefers this language (over other languages he masters up a certain level)." )
        protected BooleanType preferred;

        private static final long serialVersionUID = 633792918L;

    /**
     * Constructor
     */
      public PatientCommunicationComponent() {
        super();
      }

    /**
     * Constructor
     */
      public PatientCommunicationComponent(CodeableConcept language) {
        super();
        this.language = language;
      }

        /**
         * @return {@link #language} (The ISO-639-1 alpha 2 code in lower case for the language, optionally followed by a hyphen and the ISO-3166-1 alpha 2 code for the region in upper case; e.g. "en" for English, or "en-US" for American English versus "en-EN" for England English.)
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
         * @param value {@link #language} (The ISO-639-1 alpha 2 code in lower case for the language, optionally followed by a hyphen and the ISO-3166-1 alpha 2 code for the region in upper case; e.g. "en" for English, or "en-US" for American English versus "en-EN" for England English.)
         */
        public PatientCommunicationComponent setLanguage(CodeableConcept value) { 
          this.language = value;
          return this;
        }

        /**
         * @return {@link #preferred} (Indicates whether or not the patient prefers this language (over other languages he masters up a certain level).). This is the underlying object with id, value and extensions. The accessor "getPreferred" gives direct access to the value
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
         * @param value {@link #preferred} (Indicates whether or not the patient prefers this language (over other languages he masters up a certain level).). This is the underlying object with id, value and extensions. The accessor "getPreferred" gives direct access to the value
         */
        public PatientCommunicationComponent setPreferredElement(BooleanType value) { 
          this.preferred = value;
          return this;
        }

        /**
         * @return Indicates whether or not the patient prefers this language (over other languages he masters up a certain level).
         */
        public boolean getPreferred() { 
          return this.preferred == null || this.preferred.isEmpty() ? false : this.preferred.getValue();
        }

        /**
         * @param value Indicates whether or not the patient prefers this language (over other languages he masters up a certain level).
         */
        public PatientCommunicationComponent setPreferred(boolean value) { 
            if (this.preferred == null)
              this.preferred = new BooleanType();
            this.preferred.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("language", "CodeableConcept", "The ISO-639-1 alpha 2 code in lower case for the language, optionally followed by a hyphen and the ISO-3166-1 alpha 2 code for the region in upper case; e.g. \"en\" for English, or \"en-US\" for American English versus \"en-EN\" for England English.", 0, java.lang.Integer.MAX_VALUE, language));
          childrenList.add(new Property("preferred", "boolean", "Indicates whether or not the patient prefers this language (over other languages he masters up a certain level).", 0, java.lang.Integer.MAX_VALUE, preferred));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1613589672: /*language*/ return this.language == null ? new Base[0] : new Base[] {this.language}; // CodeableConcept
        case -1294005119: /*preferred*/ return this.preferred == null ? new Base[0] : new Base[] {this.preferred}; // BooleanType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1613589672: // language
          this.language = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1294005119: // preferred
          this.preferred = castToBoolean(value); // BooleanType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("language")) {
          this.language = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("preferred")) {
          this.preferred = castToBoolean(value); // BooleanType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1613589672:  return getLanguage(); 
        case -1294005119:  return getPreferredElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1613589672: /*language*/ return new String[] {"CodeableConcept"};
        case -1294005119: /*preferred*/ return new String[] {"boolean"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("language")) {
          this.language = new CodeableConcept();
          return this.language;
        }
        else if (name.equals("preferred")) {
          throw new FHIRException("Cannot call addChild on a primitive type Patient.preferred");
        }
        else
          return super.addChild(name);
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
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(language, preferred);
      }

  public String fhirType() {
    return "Patient.communication";

  }

  }

    @Block()
    public static class PatientLinkComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The other patient resource that the link refers to.
         */
        @Child(name = "other", type = {Patient.class, RelatedPerson.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The other patient or related person resource that the link refers to", formalDefinition="The other patient resource that the link refers to." )
        protected Reference other;

        /**
         * The actual object that is the target of the reference (The other patient resource that the link refers to.)
         */
        protected Resource otherTarget;

        /**
         * The type of link between this patient resource and another patient resource.
         */
        @Child(name = "type", type = {CodeType.class}, order=2, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="replaced-by | replaces | refer | seealso - type of link", formalDefinition="The type of link between this patient resource and another patient resource." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/link-type")
        protected Enumeration<LinkType> type;

        private static final long serialVersionUID = 1083576633L;

    /**
     * Constructor
     */
      public PatientLinkComponent() {
        super();
      }

    /**
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
        public Resource getOtherTarget() { 
          return this.otherTarget;
        }

        /**
         * @param value {@link #other} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The other patient resource that the link refers to.)
         */
        public PatientLinkComponent setOtherTarget(Resource value) { 
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
          childrenList.add(new Property("other", "Reference(Patient|RelatedPerson)", "The other patient resource that the link refers to.", 0, java.lang.Integer.MAX_VALUE, other));
          childrenList.add(new Property("type", "code", "The type of link between this patient resource and another patient resource.", 0, java.lang.Integer.MAX_VALUE, type));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 106069776: /*other*/ return this.other == null ? new Base[0] : new Base[] {this.other}; // Reference
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // Enumeration<LinkType>
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 106069776: // other
          this.other = castToReference(value); // Reference
          return value;
        case 3575610: // type
          value = new LinkTypeEnumFactory().fromType(castToCode(value));
          this.type = (Enumeration) value; // Enumeration<LinkType>
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("other")) {
          this.other = castToReference(value); // Reference
        } else if (name.equals("type")) {
          value = new LinkTypeEnumFactory().fromType(castToCode(value));
          this.type = (Enumeration) value; // Enumeration<LinkType>
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 106069776:  return getOther(); 
        case 3575610:  return getTypeElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 106069776: /*other*/ return new String[] {"Reference"};
        case 3575610: /*type*/ return new String[] {"code"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("other")) {
          this.other = new Reference();
          return this.other;
        }
        else if (name.equals("type")) {
          throw new FHIRException("Cannot call addChild on a primitive type Patient.type");
        }
        else
          return super.addChild(name);
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
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(other, type);
      }

  public String fhirType() {
    return "Patient.link";

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
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/administrative-gender")
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
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/marital-status")
    protected CodeableConcept maritalStatus;

    /**
     * Indicates whether the patient is part of a multiple (bool) or indicates the actual birth order (integer).
     */
    @Child(name = "multipleBirth", type = {BooleanType.class, IntegerType.class}, order=9, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Whether patient is part of a multiple birth", formalDefinition="Indicates whether the patient is part of a multiple (bool) or indicates the actual birth order (integer)." )
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
    @Child(name = "generalPractitioner", type = {Organization.class, Practitioner.class}, order=14, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Patient's nominated primary care provider", formalDefinition="Patient's nominated care provider." )
    protected List<Reference> generalPractitioner;
    /**
     * The actual objects that are the target of the reference (Patient's nominated care provider.)
     */
    protected List<Resource> generalPractitionerTarget;


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
    @Child(name = "link", type = {}, order=16, min=0, max=Child.MAX_UNLIMITED, modifier=true, summary=true)
    @Description(shortDefinition="Link to another patient resource that concerns the same actual person", formalDefinition="Link to another patient resource that concerns the same actual patient." )
    protected List<PatientLinkComponent> link;

    private static final long serialVersionUID = -1985061666L;

  /**
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

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Patient setIdentifier(List<Identifier> theIdentifier) { 
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

    public Patient addIdentifier(Identifier t) { //3
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

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Patient setName(List<HumanName> theName) { 
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

    public Patient addName(HumanName t) { //3
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
     * @return {@link #telecom} (A contact detail (e.g. a telephone number or an email address) by which the individual may be contacted.)
     */
    public List<ContactPoint> getTelecom() { 
      if (this.telecom == null)
        this.telecom = new ArrayList<ContactPoint>();
      return this.telecom;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Patient setTelecom(List<ContactPoint> theTelecom) { 
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

    public Patient addTelecom(ContactPoint t) { //3
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
    public BooleanType getDeceasedBooleanType() throws FHIRException { 
      if (!(this.deceased instanceof BooleanType))
        throw new FHIRException("Type mismatch: the type BooleanType was expected, but "+this.deceased.getClass().getName()+" was encountered");
      return (BooleanType) this.deceased;
    }

    public boolean hasDeceasedBooleanType() { 
      return this.deceased instanceof BooleanType;
    }

    /**
     * @return {@link #deceased} (Indicates if the individual is deceased or not.)
     */
    public DateTimeType getDeceasedDateTimeType() throws FHIRException { 
      if (!(this.deceased instanceof DateTimeType))
        throw new FHIRException("Type mismatch: the type DateTimeType was expected, but "+this.deceased.getClass().getName()+" was encountered");
      return (DateTimeType) this.deceased;
    }

    public boolean hasDeceasedDateTimeType() { 
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

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Patient setAddress(List<Address> theAddress) { 
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

    public Patient addAddress(Address t) { //3
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
     * @return {@link #multipleBirth} (Indicates whether the patient is part of a multiple (bool) or indicates the actual birth order (integer).)
     */
    public Type getMultipleBirth() { 
      return this.multipleBirth;
    }

    /**
     * @return {@link #multipleBirth} (Indicates whether the patient is part of a multiple (bool) or indicates the actual birth order (integer).)
     */
    public BooleanType getMultipleBirthBooleanType() throws FHIRException { 
      if (!(this.multipleBirth instanceof BooleanType))
        throw new FHIRException("Type mismatch: the type BooleanType was expected, but "+this.multipleBirth.getClass().getName()+" was encountered");
      return (BooleanType) this.multipleBirth;
    }

    public boolean hasMultipleBirthBooleanType() { 
      return this.multipleBirth instanceof BooleanType;
    }

    /**
     * @return {@link #multipleBirth} (Indicates whether the patient is part of a multiple (bool) or indicates the actual birth order (integer).)
     */
    public IntegerType getMultipleBirthIntegerType() throws FHIRException { 
      if (!(this.multipleBirth instanceof IntegerType))
        throw new FHIRException("Type mismatch: the type IntegerType was expected, but "+this.multipleBirth.getClass().getName()+" was encountered");
      return (IntegerType) this.multipleBirth;
    }

    public boolean hasMultipleBirthIntegerType() { 
      return this.multipleBirth instanceof IntegerType;
    }

    public boolean hasMultipleBirth() { 
      return this.multipleBirth != null && !this.multipleBirth.isEmpty();
    }

    /**
     * @param value {@link #multipleBirth} (Indicates whether the patient is part of a multiple (bool) or indicates the actual birth order (integer).)
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

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Patient setPhoto(List<Attachment> thePhoto) { 
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

    public Patient addPhoto(Attachment t) { //3
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
     * @return {@link #contact} (A contact party (e.g. guardian, partner, friend) for the patient.)
     */
    public List<ContactComponent> getContact() { 
      if (this.contact == null)
        this.contact = new ArrayList<ContactComponent>();
      return this.contact;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Patient setContact(List<ContactComponent> theContact) { 
      this.contact = theContact;
      return this;
    }

    public boolean hasContact() { 
      if (this.contact == null)
        return false;
      for (ContactComponent item : this.contact)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public ContactComponent addContact() { //3
      ContactComponent t = new ContactComponent();
      if (this.contact == null)
        this.contact = new ArrayList<ContactComponent>();
      this.contact.add(t);
      return t;
    }

    public Patient addContact(ContactComponent t) { //3
      if (t == null)
        return this;
      if (this.contact == null)
        this.contact = new ArrayList<ContactComponent>();
      this.contact.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #contact}, creating it if it does not already exist
     */
    public ContactComponent getContactFirstRep() { 
      if (getContact().isEmpty()) {
        addContact();
      }
      return getContact().get(0);
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

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Patient setCommunication(List<PatientCommunicationComponent> theCommunication) { 
      this.communication = theCommunication;
      return this;
    }

    public boolean hasCommunication() { 
      if (this.communication == null)
        return false;
      for (PatientCommunicationComponent item : this.communication)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public PatientCommunicationComponent addCommunication() { //3
      PatientCommunicationComponent t = new PatientCommunicationComponent();
      if (this.communication == null)
        this.communication = new ArrayList<PatientCommunicationComponent>();
      this.communication.add(t);
      return t;
    }

    public Patient addCommunication(PatientCommunicationComponent t) { //3
      if (t == null)
        return this;
      if (this.communication == null)
        this.communication = new ArrayList<PatientCommunicationComponent>();
      this.communication.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #communication}, creating it if it does not already exist
     */
    public PatientCommunicationComponent getCommunicationFirstRep() { 
      if (getCommunication().isEmpty()) {
        addCommunication();
      }
      return getCommunication().get(0);
    }

    /**
     * @return {@link #generalPractitioner} (Patient's nominated care provider.)
     */
    public List<Reference> getGeneralPractitioner() { 
      if (this.generalPractitioner == null)
        this.generalPractitioner = new ArrayList<Reference>();
      return this.generalPractitioner;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Patient setGeneralPractitioner(List<Reference> theGeneralPractitioner) { 
      this.generalPractitioner = theGeneralPractitioner;
      return this;
    }

    public boolean hasGeneralPractitioner() { 
      if (this.generalPractitioner == null)
        return false;
      for (Reference item : this.generalPractitioner)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addGeneralPractitioner() { //3
      Reference t = new Reference();
      if (this.generalPractitioner == null)
        this.generalPractitioner = new ArrayList<Reference>();
      this.generalPractitioner.add(t);
      return t;
    }

    public Patient addGeneralPractitioner(Reference t) { //3
      if (t == null)
        return this;
      if (this.generalPractitioner == null)
        this.generalPractitioner = new ArrayList<Reference>();
      this.generalPractitioner.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #generalPractitioner}, creating it if it does not already exist
     */
    public Reference getGeneralPractitionerFirstRep() { 
      if (getGeneralPractitioner().isEmpty()) {
        addGeneralPractitioner();
      }
      return getGeneralPractitioner().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Resource> getGeneralPractitionerTarget() { 
      if (this.generalPractitionerTarget == null)
        this.generalPractitionerTarget = new ArrayList<Resource>();
      return this.generalPractitionerTarget;
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

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Patient setLink(List<PatientLinkComponent> theLink) { 
      this.link = theLink;
      return this;
    }

    public boolean hasLink() { 
      if (this.link == null)
        return false;
      for (PatientLinkComponent item : this.link)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public PatientLinkComponent addLink() { //3
      PatientLinkComponent t = new PatientLinkComponent();
      if (this.link == null)
        this.link = new ArrayList<PatientLinkComponent>();
      this.link.add(t);
      return t;
    }

    public Patient addLink(PatientLinkComponent t) { //3
      if (t == null)
        return this;
      if (this.link == null)
        this.link = new ArrayList<PatientLinkComponent>();
      this.link.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #link}, creating it if it does not already exist
     */
    public PatientLinkComponent getLinkFirstRep() { 
      if (getLink().isEmpty()) {
        addLink();
      }
      return getLink().get(0);
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
        childrenList.add(new Property("multipleBirth[x]", "boolean|integer", "Indicates whether the patient is part of a multiple (bool) or indicates the actual birth order (integer).", 0, java.lang.Integer.MAX_VALUE, multipleBirth));
        childrenList.add(new Property("photo", "Attachment", "Image of the patient.", 0, java.lang.Integer.MAX_VALUE, photo));
        childrenList.add(new Property("contact", "", "A contact party (e.g. guardian, partner, friend) for the patient.", 0, java.lang.Integer.MAX_VALUE, contact));
        childrenList.add(new Property("animal", "", "This patient is known to be an animal.", 0, java.lang.Integer.MAX_VALUE, animal));
        childrenList.add(new Property("communication", "", "Languages which may be used to communicate with the patient about his or her health.", 0, java.lang.Integer.MAX_VALUE, communication));
        childrenList.add(new Property("generalPractitioner", "Reference(Organization|Practitioner)", "Patient's nominated care provider.", 0, java.lang.Integer.MAX_VALUE, generalPractitioner));
        childrenList.add(new Property("managingOrganization", "Reference(Organization)", "Organization that is the custodian of the patient record.", 0, java.lang.Integer.MAX_VALUE, managingOrganization));
        childrenList.add(new Property("link", "", "Link to another patient resource that concerns the same actual patient.", 0, java.lang.Integer.MAX_VALUE, link));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case -1422950650: /*active*/ return this.active == null ? new Base[0] : new Base[] {this.active}; // BooleanType
        case 3373707: /*name*/ return this.name == null ? new Base[0] : this.name.toArray(new Base[this.name.size()]); // HumanName
        case -1429363305: /*telecom*/ return this.telecom == null ? new Base[0] : this.telecom.toArray(new Base[this.telecom.size()]); // ContactPoint
        case -1249512767: /*gender*/ return this.gender == null ? new Base[0] : new Base[] {this.gender}; // Enumeration<AdministrativeGender>
        case -1210031859: /*birthDate*/ return this.birthDate == null ? new Base[0] : new Base[] {this.birthDate}; // DateType
        case 561497972: /*deceased*/ return this.deceased == null ? new Base[0] : new Base[] {this.deceased}; // Type
        case -1147692044: /*address*/ return this.address == null ? new Base[0] : this.address.toArray(new Base[this.address.size()]); // Address
        case 1756919302: /*maritalStatus*/ return this.maritalStatus == null ? new Base[0] : new Base[] {this.maritalStatus}; // CodeableConcept
        case -677369713: /*multipleBirth*/ return this.multipleBirth == null ? new Base[0] : new Base[] {this.multipleBirth}; // Type
        case 106642994: /*photo*/ return this.photo == null ? new Base[0] : this.photo.toArray(new Base[this.photo.size()]); // Attachment
        case 951526432: /*contact*/ return this.contact == null ? new Base[0] : this.contact.toArray(new Base[this.contact.size()]); // ContactComponent
        case -1413116420: /*animal*/ return this.animal == null ? new Base[0] : new Base[] {this.animal}; // AnimalComponent
        case -1035284522: /*communication*/ return this.communication == null ? new Base[0] : this.communication.toArray(new Base[this.communication.size()]); // PatientCommunicationComponent
        case 1488292898: /*generalPractitioner*/ return this.generalPractitioner == null ? new Base[0] : this.generalPractitioner.toArray(new Base[this.generalPractitioner.size()]); // Reference
        case -2058947787: /*managingOrganization*/ return this.managingOrganization == null ? new Base[0] : new Base[] {this.managingOrganization}; // Reference
        case 3321850: /*link*/ return this.link == null ? new Base[0] : this.link.toArray(new Base[this.link.size()]); // PatientLinkComponent
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
        case 3373707: // name
          this.getName().add(castToHumanName(value)); // HumanName
          return value;
        case -1429363305: // telecom
          this.getTelecom().add(castToContactPoint(value)); // ContactPoint
          return value;
        case -1249512767: // gender
          value = new AdministrativeGenderEnumFactory().fromType(castToCode(value));
          this.gender = (Enumeration) value; // Enumeration<AdministrativeGender>
          return value;
        case -1210031859: // birthDate
          this.birthDate = castToDate(value); // DateType
          return value;
        case 561497972: // deceased
          this.deceased = castToType(value); // Type
          return value;
        case -1147692044: // address
          this.getAddress().add(castToAddress(value)); // Address
          return value;
        case 1756919302: // maritalStatus
          this.maritalStatus = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -677369713: // multipleBirth
          this.multipleBirth = castToType(value); // Type
          return value;
        case 106642994: // photo
          this.getPhoto().add(castToAttachment(value)); // Attachment
          return value;
        case 951526432: // contact
          this.getContact().add((ContactComponent) value); // ContactComponent
          return value;
        case -1413116420: // animal
          this.animal = (AnimalComponent) value; // AnimalComponent
          return value;
        case -1035284522: // communication
          this.getCommunication().add((PatientCommunicationComponent) value); // PatientCommunicationComponent
          return value;
        case 1488292898: // generalPractitioner
          this.getGeneralPractitioner().add(castToReference(value)); // Reference
          return value;
        case -2058947787: // managingOrganization
          this.managingOrganization = castToReference(value); // Reference
          return value;
        case 3321850: // link
          this.getLink().add((PatientLinkComponent) value); // PatientLinkComponent
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
        } else if (name.equals("name")) {
          this.getName().add(castToHumanName(value));
        } else if (name.equals("telecom")) {
          this.getTelecom().add(castToContactPoint(value));
        } else if (name.equals("gender")) {
          value = new AdministrativeGenderEnumFactory().fromType(castToCode(value));
          this.gender = (Enumeration) value; // Enumeration<AdministrativeGender>
        } else if (name.equals("birthDate")) {
          this.birthDate = castToDate(value); // DateType
        } else if (name.equals("deceased[x]")) {
          this.deceased = castToType(value); // Type
        } else if (name.equals("address")) {
          this.getAddress().add(castToAddress(value));
        } else if (name.equals("maritalStatus")) {
          this.maritalStatus = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("multipleBirth[x]")) {
          this.multipleBirth = castToType(value); // Type
        } else if (name.equals("photo")) {
          this.getPhoto().add(castToAttachment(value));
        } else if (name.equals("contact")) {
          this.getContact().add((ContactComponent) value);
        } else if (name.equals("animal")) {
          this.animal = (AnimalComponent) value; // AnimalComponent
        } else if (name.equals("communication")) {
          this.getCommunication().add((PatientCommunicationComponent) value);
        } else if (name.equals("generalPractitioner")) {
          this.getGeneralPractitioner().add(castToReference(value));
        } else if (name.equals("managingOrganization")) {
          this.managingOrganization = castToReference(value); // Reference
        } else if (name.equals("link")) {
          this.getLink().add((PatientLinkComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return addIdentifier(); 
        case -1422950650:  return getActiveElement();
        case 3373707:  return addName(); 
        case -1429363305:  return addTelecom(); 
        case -1249512767:  return getGenderElement();
        case -1210031859:  return getBirthDateElement();
        case -1311442804:  return getDeceased(); 
        case 561497972:  return getDeceased(); 
        case -1147692044:  return addAddress(); 
        case 1756919302:  return getMaritalStatus(); 
        case -1764672111:  return getMultipleBirth(); 
        case -677369713:  return getMultipleBirth(); 
        case 106642994:  return addPhoto(); 
        case 951526432:  return addContact(); 
        case -1413116420:  return getAnimal(); 
        case -1035284522:  return addCommunication(); 
        case 1488292898:  return addGeneralPractitioner(); 
        case -2058947787:  return getManagingOrganization(); 
        case 3321850:  return addLink(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case -1422950650: /*active*/ return new String[] {"boolean"};
        case 3373707: /*name*/ return new String[] {"HumanName"};
        case -1429363305: /*telecom*/ return new String[] {"ContactPoint"};
        case -1249512767: /*gender*/ return new String[] {"code"};
        case -1210031859: /*birthDate*/ return new String[] {"date"};
        case 561497972: /*deceased*/ return new String[] {"boolean", "dateTime"};
        case -1147692044: /*address*/ return new String[] {"Address"};
        case 1756919302: /*maritalStatus*/ return new String[] {"CodeableConcept"};
        case -677369713: /*multipleBirth*/ return new String[] {"boolean", "integer"};
        case 106642994: /*photo*/ return new String[] {"Attachment"};
        case 951526432: /*contact*/ return new String[] {};
        case -1413116420: /*animal*/ return new String[] {};
        case -1035284522: /*communication*/ return new String[] {};
        case 1488292898: /*generalPractitioner*/ return new String[] {"Reference"};
        case -2058947787: /*managingOrganization*/ return new String[] {"Reference"};
        case 3321850: /*link*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("active")) {
          throw new FHIRException("Cannot call addChild on a primitive type Patient.active");
        }
        else if (name.equals("name")) {
          return addName();
        }
        else if (name.equals("telecom")) {
          return addTelecom();
        }
        else if (name.equals("gender")) {
          throw new FHIRException("Cannot call addChild on a primitive type Patient.gender");
        }
        else if (name.equals("birthDate")) {
          throw new FHIRException("Cannot call addChild on a primitive type Patient.birthDate");
        }
        else if (name.equals("deceasedBoolean")) {
          this.deceased = new BooleanType();
          return this.deceased;
        }
        else if (name.equals("deceasedDateTime")) {
          this.deceased = new DateTimeType();
          return this.deceased;
        }
        else if (name.equals("address")) {
          return addAddress();
        }
        else if (name.equals("maritalStatus")) {
          this.maritalStatus = new CodeableConcept();
          return this.maritalStatus;
        }
        else if (name.equals("multipleBirthBoolean")) {
          this.multipleBirth = new BooleanType();
          return this.multipleBirth;
        }
        else if (name.equals("multipleBirthInteger")) {
          this.multipleBirth = new IntegerType();
          return this.multipleBirth;
        }
        else if (name.equals("photo")) {
          return addPhoto();
        }
        else if (name.equals("contact")) {
          return addContact();
        }
        else if (name.equals("animal")) {
          this.animal = new AnimalComponent();
          return this.animal;
        }
        else if (name.equals("communication")) {
          return addCommunication();
        }
        else if (name.equals("generalPractitioner")) {
          return addGeneralPractitioner();
        }
        else if (name.equals("managingOrganization")) {
          this.managingOrganization = new Reference();
          return this.managingOrganization;
        }
        else if (name.equals("link")) {
          return addLink();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "Patient";

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
        if (generalPractitioner != null) {
          dst.generalPractitioner = new ArrayList<Reference>();
          for (Reference i : generalPractitioner)
            dst.generalPractitioner.add(i.copy());
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
           && compareDeep(animal, o.animal, true) && compareDeep(communication, o.communication, true) && compareDeep(generalPractitioner, o.generalPractitioner, true)
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
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, active, name
          , telecom, gender, birthDate, deceased, address, maritalStatus, multipleBirth
          , photo, contact, animal, communication, generalPractitioner, managingOrganization
          , link);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Patient;
   }

 /**
   * Search parameter: <b>birthdate</b>
   * <p>
   * Description: <b>The patient's date of birth</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Patient.birthDate</b><br>
   * </p>
   */
  @SearchParamDefinition(name="birthdate", path="Patient.birthDate", description="The patient's date of birth", type="date" )
  public static final String SP_BIRTHDATE = "birthdate";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>birthdate</b>
   * <p>
   * Description: <b>The patient's date of birth</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Patient.birthDate</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam BIRTHDATE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_BIRTHDATE);

 /**
   * Search parameter: <b>deceased</b>
   * <p>
   * Description: <b>This patient has been marked as deceased, or as a death date entered</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Patient.deceased[x]</b><br>
   * </p>
   */
  @SearchParamDefinition(name="deceased", path="Patient.deceased.exists()", description="This patient has been marked as deceased, or as a death date entered", type="token" )
  public static final String SP_DECEASED = "deceased";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>deceased</b>
   * <p>
   * Description: <b>This patient has been marked as deceased, or as a death date entered</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Patient.deceased[x]</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam DECEASED = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_DECEASED);

 /**
   * Search parameter: <b>address-state</b>
   * <p>
   * Description: <b>A state specified in an address</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Patient.address.state</b><br>
   * </p>
   */
  @SearchParamDefinition(name="address-state", path="Patient.address.state", description="A state specified in an address", type="string" )
  public static final String SP_ADDRESS_STATE = "address-state";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>address-state</b>
   * <p>
   * Description: <b>A state specified in an address</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Patient.address.state</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam ADDRESS_STATE = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_ADDRESS_STATE);

 /**
   * Search parameter: <b>gender</b>
   * <p>
   * Description: <b>Gender of the patient</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Patient.gender</b><br>
   * </p>
   */
  @SearchParamDefinition(name="gender", path="Patient.gender", description="Gender of the patient", type="token" )
  public static final String SP_GENDER = "gender";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>gender</b>
   * <p>
   * Description: <b>Gender of the patient</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Patient.gender</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam GENDER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_GENDER);

 /**
   * Search parameter: <b>animal-species</b>
   * <p>
   * Description: <b>The species for animal patients</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Patient.animal.species</b><br>
   * </p>
   */
  @SearchParamDefinition(name="animal-species", path="Patient.animal.species", description="The species for animal patients", type="token" )
  public static final String SP_ANIMAL_SPECIES = "animal-species";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>animal-species</b>
   * <p>
   * Description: <b>The species for animal patients</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Patient.animal.species</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam ANIMAL_SPECIES = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_ANIMAL_SPECIES);

 /**
   * Search parameter: <b>link</b>
   * <p>
   * Description: <b>All patients linked to the given patient</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Patient.link.other</b><br>
   * </p>
   */
  @SearchParamDefinition(name="link", path="Patient.link.other", description="All patients linked to the given patient", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient"), @ca.uhn.fhir.model.api.annotation.Compartment(name="RelatedPerson") }, target={Patient.class, RelatedPerson.class } )
  public static final String SP_LINK = "link";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>link</b>
   * <p>
   * Description: <b>All patients linked to the given patient</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Patient.link.other</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam LINK = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_LINK);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Patient:link</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_LINK = new ca.uhn.fhir.model.api.Include("Patient:link").toLocked();

 /**
   * Search parameter: <b>language</b>
   * <p>
   * Description: <b>Language code (irrespective of use value)</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Patient.communication.language</b><br>
   * </p>
   */
  @SearchParamDefinition(name="language", path="Patient.communication.language", description="Language code (irrespective of use value)", type="token" )
  public static final String SP_LANGUAGE = "language";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>language</b>
   * <p>
   * Description: <b>Language code (irrespective of use value)</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Patient.communication.language</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam LANGUAGE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_LANGUAGE);

 /**
   * Search parameter: <b>animal-breed</b>
   * <p>
   * Description: <b>The breed for animal patients</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Patient.animal.breed</b><br>
   * </p>
   */
  @SearchParamDefinition(name="animal-breed", path="Patient.animal.breed", description="The breed for animal patients", type="token" )
  public static final String SP_ANIMAL_BREED = "animal-breed";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>animal-breed</b>
   * <p>
   * Description: <b>The breed for animal patients</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Patient.animal.breed</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam ANIMAL_BREED = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_ANIMAL_BREED);

 /**
   * Search parameter: <b>address-country</b>
   * <p>
   * Description: <b>A country specified in an address</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Patient.address.country</b><br>
   * </p>
   */
  @SearchParamDefinition(name="address-country", path="Patient.address.country", description="A country specified in an address", type="string" )
  public static final String SP_ADDRESS_COUNTRY = "address-country";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>address-country</b>
   * <p>
   * Description: <b>A country specified in an address</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Patient.address.country</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam ADDRESS_COUNTRY = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_ADDRESS_COUNTRY);

 /**
   * Search parameter: <b>death-date</b>
   * <p>
   * Description: <b>The date of death has been provided and satisfies this search value</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Patient.deceasedDateTime</b><br>
   * </p>
   */
  @SearchParamDefinition(name="death-date", path="Patient.deceased.as(DateTime)", description="The date of death has been provided and satisfies this search value", type="date" )
  public static final String SP_DEATH_DATE = "death-date";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>death-date</b>
   * <p>
   * Description: <b>The date of death has been provided and satisfies this search value</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Patient.deceasedDateTime</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam DEATH_DATE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_DEATH_DATE);

 /**
   * Search parameter: <b>phonetic</b>
   * <p>
   * Description: <b>A portion of either family or given name using some kind of phonetic matching algorithm</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Patient.name</b><br>
   * </p>
   */
  @SearchParamDefinition(name="phonetic", path="Patient.name", description="A portion of either family or given name using some kind of phonetic matching algorithm", type="string" )
  public static final String SP_PHONETIC = "phonetic";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>phonetic</b>
   * <p>
   * Description: <b>A portion of either family or given name using some kind of phonetic matching algorithm</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Patient.name</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam PHONETIC = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_PHONETIC);

 /**
   * Search parameter: <b>telecom</b>
   * <p>
   * Description: <b>The value in any kind of telecom details of the patient</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Patient.telecom</b><br>
   * </p>
   */
  @SearchParamDefinition(name="telecom", path="Patient.telecom", description="The value in any kind of telecom details of the patient", type="token" )
  public static final String SP_TELECOM = "telecom";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>telecom</b>
   * <p>
   * Description: <b>The value in any kind of telecom details of the patient</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Patient.telecom</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam TELECOM = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_TELECOM);

 /**
   * Search parameter: <b>address-city</b>
   * <p>
   * Description: <b>A city specified in an address</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Patient.address.city</b><br>
   * </p>
   */
  @SearchParamDefinition(name="address-city", path="Patient.address.city", description="A city specified in an address", type="string" )
  public static final String SP_ADDRESS_CITY = "address-city";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>address-city</b>
   * <p>
   * Description: <b>A city specified in an address</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Patient.address.city</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam ADDRESS_CITY = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_ADDRESS_CITY);

 /**
   * Search parameter: <b>email</b>
   * <p>
   * Description: <b>A value in an email contact</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Patient.telecom(system=email)</b><br>
   * </p>
   */
  @SearchParamDefinition(name="email", path="Patient.telecom.where(system='email')", description="A value in an email contact", type="token" )
  public static final String SP_EMAIL = "email";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>email</b>
   * <p>
   * Description: <b>A value in an email contact</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Patient.telecom(system=email)</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam EMAIL = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_EMAIL);

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>A patient identifier</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Patient.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="Patient.identifier", description="A patient identifier", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>A patient identifier</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Patient.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>given</b>
   * <p>
   * Description: <b>A portion of the given name of the patient</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Patient.name.given</b><br>
   * </p>
   */
  @SearchParamDefinition(name="given", path="Patient.name.given", description="A portion of the given name of the patient", type="string" )
  public static final String SP_GIVEN = "given";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>given</b>
   * <p>
   * Description: <b>A portion of the given name of the patient</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Patient.name.given</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam GIVEN = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_GIVEN);

 /**
   * Search parameter: <b>address</b>
   * <p>
   * Description: <b>A server defined search that may match any of the string fields in the Address, including line, city, state, country, postalCode, and/or text</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Patient.address</b><br>
   * </p>
   */
  @SearchParamDefinition(name="address", path="Patient.address", description="A server defined search that may match any of the string fields in the Address, including line, city, state, country, postalCode, and/or text", type="string" )
  public static final String SP_ADDRESS = "address";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>address</b>
   * <p>
   * Description: <b>A server defined search that may match any of the string fields in the Address, including line, city, state, country, postalCode, and/or text</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Patient.address</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam ADDRESS = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_ADDRESS);

 /**
   * Search parameter: <b>general-practitioner</b>
   * <p>
   * Description: <b>Patient's nominated general practitioner, not the organization that manages the record</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Patient.generalPractitioner</b><br>
   * </p>
   */
  @SearchParamDefinition(name="general-practitioner", path="Patient.generalPractitioner", description="Patient's nominated general practitioner, not the organization that manages the record", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Practitioner") }, target={Organization.class, Practitioner.class } )
  public static final String SP_GENERAL_PRACTITIONER = "general-practitioner";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>general-practitioner</b>
   * <p>
   * Description: <b>Patient's nominated general practitioner, not the organization that manages the record</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Patient.generalPractitioner</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam GENERAL_PRACTITIONER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_GENERAL_PRACTITIONER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Patient:general-practitioner</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_GENERAL_PRACTITIONER = new ca.uhn.fhir.model.api.Include("Patient:general-practitioner").toLocked();

 /**
   * Search parameter: <b>active</b>
   * <p>
   * Description: <b>Whether the patient record is active</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Patient.active</b><br>
   * </p>
   */
  @SearchParamDefinition(name="active", path="Patient.active", description="Whether the patient record is active", type="token" )
  public static final String SP_ACTIVE = "active";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>active</b>
   * <p>
   * Description: <b>Whether the patient record is active</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Patient.active</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam ACTIVE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_ACTIVE);

 /**
   * Search parameter: <b>address-postalcode</b>
   * <p>
   * Description: <b>A postalCode specified in an address</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Patient.address.postalCode</b><br>
   * </p>
   */
  @SearchParamDefinition(name="address-postalcode", path="Patient.address.postalCode", description="A postalCode specified in an address", type="string" )
  public static final String SP_ADDRESS_POSTALCODE = "address-postalcode";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>address-postalcode</b>
   * <p>
   * Description: <b>A postalCode specified in an address</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Patient.address.postalCode</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam ADDRESS_POSTALCODE = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_ADDRESS_POSTALCODE);

 /**
   * Search parameter: <b>phone</b>
   * <p>
   * Description: <b>A value in a phone contact</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Patient.telecom(system=phone)</b><br>
   * </p>
   */
  @SearchParamDefinition(name="phone", path="Patient.telecom.where(system='phone')", description="A value in a phone contact", type="token" )
  public static final String SP_PHONE = "phone";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>phone</b>
   * <p>
   * Description: <b>A value in a phone contact</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Patient.telecom(system=phone)</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam PHONE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_PHONE);

 /**
   * Search parameter: <b>organization</b>
   * <p>
   * Description: <b>The organization at which this person is a patient</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Patient.managingOrganization</b><br>
   * </p>
   */
  @SearchParamDefinition(name="organization", path="Patient.managingOrganization", description="The organization at which this person is a patient", type="reference", target={Organization.class } )
  public static final String SP_ORGANIZATION = "organization";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>organization</b>
   * <p>
   * Description: <b>The organization at which this person is a patient</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Patient.managingOrganization</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam ORGANIZATION = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_ORGANIZATION);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Patient:organization</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_ORGANIZATION = new ca.uhn.fhir.model.api.Include("Patient:organization").toLocked();

 /**
   * Search parameter: <b>name</b>
   * <p>
   * Description: <b>A server defined search that may match any of the string fields in the HumanName, including family, give, prefix, suffix, suffix, and/or text</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Patient.name</b><br>
   * </p>
   */
  @SearchParamDefinition(name="name", path="Patient.name", description="A server defined search that may match any of the string fields in the HumanName, including family, give, prefix, suffix, suffix, and/or text", type="string" )
  public static final String SP_NAME = "name";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>name</b>
   * <p>
   * Description: <b>A server defined search that may match any of the string fields in the HumanName, including family, give, prefix, suffix, suffix, and/or text</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Patient.name</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam NAME = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_NAME);

 /**
   * Search parameter: <b>address-use</b>
   * <p>
   * Description: <b>A use code specified in an address</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Patient.address.use</b><br>
   * </p>
   */
  @SearchParamDefinition(name="address-use", path="Patient.address.use", description="A use code specified in an address", type="token" )
  public static final String SP_ADDRESS_USE = "address-use";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>address-use</b>
   * <p>
   * Description: <b>A use code specified in an address</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Patient.address.use</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam ADDRESS_USE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_ADDRESS_USE);

 /**
   * Search parameter: <b>family</b>
   * <p>
   * Description: <b>A portion of the family name of the patient</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Patient.name.family</b><br>
   * </p>
   */
  @SearchParamDefinition(name="family", path="Patient.name.family", description="A portion of the family name of the patient", type="string" )
  public static final String SP_FAMILY = "family";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>family</b>
   * <p>
   * Description: <b>A portion of the family name of the patient</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Patient.name.family</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam FAMILY = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_FAMILY);


}

