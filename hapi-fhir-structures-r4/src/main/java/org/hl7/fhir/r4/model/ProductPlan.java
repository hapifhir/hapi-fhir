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

// Generated on Thu, Mar 1, 2018 20:26+1100 for FHIR v3.2.0

import java.util.*;

import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.r4.model.Enumerations.*;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ChildOrder;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Block;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.exceptions.FHIRException;
/**
 * Details of a Health Insurance product/plan provided by an organization.
 */
@ResourceDef(name="ProductPlan", profile="http://hl7.org/fhir/Profile/ProductPlan")
public class ProductPlan extends DomainResource {

    @Block()
    public static class ProductPlanContactComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Indicates a purpose for which the contact can be reached.
         */
        @Child(name = "purpose", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The type of contact", formalDefinition="Indicates a purpose for which the contact can be reached." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/contactentity-type")
        protected CodeableConcept purpose;

        /**
         * A name associated with the contact.
         */
        @Child(name = "name", type = {HumanName.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="A name associated with the contact", formalDefinition="A name associated with the contact." )
        protected HumanName name;

        /**
         * A contact detail (e.g. a telephone number or an email address) by which the party may be contacted.
         */
        @Child(name = "telecom", type = {ContactPoint.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Contact details (telephone, email, etc.)  for a contact", formalDefinition="A contact detail (e.g. a telephone number or an email address) by which the party may be contacted." )
        protected List<ContactPoint> telecom;

        /**
         * Visiting or postal addresses for the contact.
         */
        @Child(name = "address", type = {Address.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Visiting or postal addresses for the contact", formalDefinition="Visiting or postal addresses for the contact." )
        protected Address address;

        private static final long serialVersionUID = 1831121305L;

    /**
     * Constructor
     */
      public ProductPlanContactComponent() {
        super();
      }

        /**
         * @return {@link #purpose} (Indicates a purpose for which the contact can be reached.)
         */
        public CodeableConcept getPurpose() { 
          if (this.purpose == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ProductPlanContactComponent.purpose");
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
        public ProductPlanContactComponent setPurpose(CodeableConcept value) { 
          this.purpose = value;
          return this;
        }

        /**
         * @return {@link #name} (A name associated with the contact.)
         */
        public HumanName getName() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ProductPlanContactComponent.name");
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
        public ProductPlanContactComponent setName(HumanName value) { 
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

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ProductPlanContactComponent setTelecom(List<ContactPoint> theTelecom) { 
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

        public ProductPlanContactComponent addTelecom(ContactPoint t) { //3
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
         * @return {@link #address} (Visiting or postal addresses for the contact.)
         */
        public Address getAddress() { 
          if (this.address == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ProductPlanContactComponent.address");
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
        public ProductPlanContactComponent setAddress(Address value) { 
          this.address = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("purpose", "CodeableConcept", "Indicates a purpose for which the contact can be reached.", 0, 1, purpose));
          children.add(new Property("name", "HumanName", "A name associated with the contact.", 0, 1, name));
          children.add(new Property("telecom", "ContactPoint", "A contact detail (e.g. a telephone number or an email address) by which the party may be contacted.", 0, java.lang.Integer.MAX_VALUE, telecom));
          children.add(new Property("address", "Address", "Visiting or postal addresses for the contact.", 0, 1, address));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -220463842: /*purpose*/  return new Property("purpose", "CodeableConcept", "Indicates a purpose for which the contact can be reached.", 0, 1, purpose);
          case 3373707: /*name*/  return new Property("name", "HumanName", "A name associated with the contact.", 0, 1, name);
          case -1429363305: /*telecom*/  return new Property("telecom", "ContactPoint", "A contact detail (e.g. a telephone number or an email address) by which the party may be contacted.", 0, java.lang.Integer.MAX_VALUE, telecom);
          case -1147692044: /*address*/  return new Property("address", "Address", "Visiting or postal addresses for the contact.", 0, 1, address);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -220463842: /*purpose*/ return this.purpose == null ? new Base[0] : new Base[] {this.purpose}; // CodeableConcept
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // HumanName
        case -1429363305: /*telecom*/ return this.telecom == null ? new Base[0] : this.telecom.toArray(new Base[this.telecom.size()]); // ContactPoint
        case -1147692044: /*address*/ return this.address == null ? new Base[0] : new Base[] {this.address}; // Address
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -220463842: // purpose
          this.purpose = castToCodeableConcept(value); // CodeableConcept
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
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("purpose")) {
          this.purpose = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("name")) {
          this.name = castToHumanName(value); // HumanName
        } else if (name.equals("telecom")) {
          this.getTelecom().add(castToContactPoint(value));
        } else if (name.equals("address")) {
          this.address = castToAddress(value); // Address
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -220463842:  return getPurpose(); 
        case 3373707:  return getName(); 
        case -1429363305:  return addTelecom(); 
        case -1147692044:  return getAddress(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -220463842: /*purpose*/ return new String[] {"CodeableConcept"};
        case 3373707: /*name*/ return new String[] {"HumanName"};
        case -1429363305: /*telecom*/ return new String[] {"ContactPoint"};
        case -1147692044: /*address*/ return new String[] {"Address"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("purpose")) {
          this.purpose = new CodeableConcept();
          return this.purpose;
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
        else
          return super.addChild(name);
      }

      public ProductPlanContactComponent copy() {
        ProductPlanContactComponent dst = new ProductPlanContactComponent();
        copyValues(dst);
        dst.purpose = purpose == null ? null : purpose.copy();
        dst.name = name == null ? null : name.copy();
        if (telecom != null) {
          dst.telecom = new ArrayList<ContactPoint>();
          for (ContactPoint i : telecom)
            dst.telecom.add(i.copy());
        };
        dst.address = address == null ? null : address.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ProductPlanContactComponent))
          return false;
        ProductPlanContactComponent o = (ProductPlanContactComponent) other_;
        return compareDeep(purpose, o.purpose, true) && compareDeep(name, o.name, true) && compareDeep(telecom, o.telecom, true)
           && compareDeep(address, o.address, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ProductPlanContactComponent))
          return false;
        ProductPlanContactComponent o = (ProductPlanContactComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(purpose, name, telecom, address
          );
      }

  public String fhirType() {
    return "ProductPlan.contact";

  }

  }

    @Block()
    public static class ProductPlanCoverageComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Type of coverage  (Medical; Dental; Mental Health; Substance Abuse; Vision; Drug; Short Term; Long Term Care; Hospice; Home Health).
         */
        @Child(name = "type", type = {CodeableConcept.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Type of coverage", formalDefinition="Type of coverage  (Medical; Dental; Mental Health; Substance Abuse; Vision; Drug; Short Term; Long Term Care; Hospice; Home Health)." )
        protected CodeableConcept type;

        /**
         * Specific benefits under this type of coverage.
         */
        @Child(name = "benefit", type = {}, order=2, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Specific benefits under this type of coverage", formalDefinition="Specific benefits under this type of coverage." )
        protected List<ProductPlanCoverageBenefitComponent> benefit;

        private static final long serialVersionUID = 1896654378L;

    /**
     * Constructor
     */
      public ProductPlanCoverageComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ProductPlanCoverageComponent(CodeableConcept type) {
        super();
        this.type = type;
      }

        /**
         * @return {@link #type} (Type of coverage  (Medical; Dental; Mental Health; Substance Abuse; Vision; Drug; Short Term; Long Term Care; Hospice; Home Health).)
         */
        public CodeableConcept getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ProductPlanCoverageComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new CodeableConcept(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (Type of coverage  (Medical; Dental; Mental Health; Substance Abuse; Vision; Drug; Short Term; Long Term Care; Hospice; Home Health).)
         */
        public ProductPlanCoverageComponent setType(CodeableConcept value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #benefit} (Specific benefits under this type of coverage.)
         */
        public List<ProductPlanCoverageBenefitComponent> getBenefit() { 
          if (this.benefit == null)
            this.benefit = new ArrayList<ProductPlanCoverageBenefitComponent>();
          return this.benefit;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ProductPlanCoverageComponent setBenefit(List<ProductPlanCoverageBenefitComponent> theBenefit) { 
          this.benefit = theBenefit;
          return this;
        }

        public boolean hasBenefit() { 
          if (this.benefit == null)
            return false;
          for (ProductPlanCoverageBenefitComponent item : this.benefit)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public ProductPlanCoverageBenefitComponent addBenefit() { //3
          ProductPlanCoverageBenefitComponent t = new ProductPlanCoverageBenefitComponent();
          if (this.benefit == null)
            this.benefit = new ArrayList<ProductPlanCoverageBenefitComponent>();
          this.benefit.add(t);
          return t;
        }

        public ProductPlanCoverageComponent addBenefit(ProductPlanCoverageBenefitComponent t) { //3
          if (t == null)
            return this;
          if (this.benefit == null)
            this.benefit = new ArrayList<ProductPlanCoverageBenefitComponent>();
          this.benefit.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #benefit}, creating it if it does not already exist
         */
        public ProductPlanCoverageBenefitComponent getBenefitFirstRep() { 
          if (getBenefit().isEmpty()) {
            addBenefit();
          }
          return getBenefit().get(0);
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("type", "CodeableConcept", "Type of coverage  (Medical; Dental; Mental Health; Substance Abuse; Vision; Drug; Short Term; Long Term Care; Hospice; Home Health).", 0, 1, type));
          children.add(new Property("benefit", "", "Specific benefits under this type of coverage.", 0, java.lang.Integer.MAX_VALUE, benefit));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3575610: /*type*/  return new Property("type", "CodeableConcept", "Type of coverage  (Medical; Dental; Mental Health; Substance Abuse; Vision; Drug; Short Term; Long Term Care; Hospice; Home Health).", 0, 1, type);
          case -222710633: /*benefit*/  return new Property("benefit", "", "Specific benefits under this type of coverage.", 0, java.lang.Integer.MAX_VALUE, benefit);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case -222710633: /*benefit*/ return this.benefit == null ? new Base[0] : this.benefit.toArray(new Base[this.benefit.size()]); // ProductPlanCoverageBenefitComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -222710633: // benefit
          this.getBenefit().add((ProductPlanCoverageBenefitComponent) value); // ProductPlanCoverageBenefitComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type")) {
          this.type = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("benefit")) {
          this.getBenefit().add((ProductPlanCoverageBenefitComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610:  return getType(); 
        case -222710633:  return addBenefit(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case -222710633: /*benefit*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("benefit")) {
          return addBenefit();
        }
        else
          return super.addChild(name);
      }

      public ProductPlanCoverageComponent copy() {
        ProductPlanCoverageComponent dst = new ProductPlanCoverageComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        if (benefit != null) {
          dst.benefit = new ArrayList<ProductPlanCoverageBenefitComponent>();
          for (ProductPlanCoverageBenefitComponent i : benefit)
            dst.benefit.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ProductPlanCoverageComponent))
          return false;
        ProductPlanCoverageComponent o = (ProductPlanCoverageComponent) other_;
        return compareDeep(type, o.type, true) && compareDeep(benefit, o.benefit, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ProductPlanCoverageComponent))
          return false;
        ProductPlanCoverageComponent o = (ProductPlanCoverageComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(type, benefit);
      }

  public String fhirType() {
    return "ProductPlan.coverage";

  }

  }

    @Block()
    public static class ProductPlanCoverageBenefitComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Type of benefit (primary care; speciality care; inpatient; outpatient).
         */
        @Child(name = "type", type = {CodeableConcept.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Type of benefit", formalDefinition="Type of benefit (primary care; speciality care; inpatient; outpatient)." )
        protected CodeableConcept type;

        /**
         * Specific benefit and related value.
         */
        @Child(name = "item", type = {}, order=2, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Specific benefit and related value", formalDefinition="Specific benefit and related value." )
        protected List<ProductPlanCoverageBenefitItemComponent> item;

        private static final long serialVersionUID = 1238381103L;

    /**
     * Constructor
     */
      public ProductPlanCoverageBenefitComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ProductPlanCoverageBenefitComponent(CodeableConcept type) {
        super();
        this.type = type;
      }

        /**
         * @return {@link #type} (Type of benefit (primary care; speciality care; inpatient; outpatient).)
         */
        public CodeableConcept getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ProductPlanCoverageBenefitComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new CodeableConcept(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (Type of benefit (primary care; speciality care; inpatient; outpatient).)
         */
        public ProductPlanCoverageBenefitComponent setType(CodeableConcept value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #item} (Specific benefit and related value.)
         */
        public List<ProductPlanCoverageBenefitItemComponent> getItem() { 
          if (this.item == null)
            this.item = new ArrayList<ProductPlanCoverageBenefitItemComponent>();
          return this.item;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ProductPlanCoverageBenefitComponent setItem(List<ProductPlanCoverageBenefitItemComponent> theItem) { 
          this.item = theItem;
          return this;
        }

        public boolean hasItem() { 
          if (this.item == null)
            return false;
          for (ProductPlanCoverageBenefitItemComponent item : this.item)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public ProductPlanCoverageBenefitItemComponent addItem() { //3
          ProductPlanCoverageBenefitItemComponent t = new ProductPlanCoverageBenefitItemComponent();
          if (this.item == null)
            this.item = new ArrayList<ProductPlanCoverageBenefitItemComponent>();
          this.item.add(t);
          return t;
        }

        public ProductPlanCoverageBenefitComponent addItem(ProductPlanCoverageBenefitItemComponent t) { //3
          if (t == null)
            return this;
          if (this.item == null)
            this.item = new ArrayList<ProductPlanCoverageBenefitItemComponent>();
          this.item.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #item}, creating it if it does not already exist
         */
        public ProductPlanCoverageBenefitItemComponent getItemFirstRep() { 
          if (getItem().isEmpty()) {
            addItem();
          }
          return getItem().get(0);
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("type", "CodeableConcept", "Type of benefit (primary care; speciality care; inpatient; outpatient).", 0, 1, type));
          children.add(new Property("item", "", "Specific benefit and related value.", 0, java.lang.Integer.MAX_VALUE, item));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3575610: /*type*/  return new Property("type", "CodeableConcept", "Type of benefit (primary care; speciality care; inpatient; outpatient).", 0, 1, type);
          case 3242771: /*item*/  return new Property("item", "", "Specific benefit and related value.", 0, java.lang.Integer.MAX_VALUE, item);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case 3242771: /*item*/ return this.item == null ? new Base[0] : this.item.toArray(new Base[this.item.size()]); // ProductPlanCoverageBenefitItemComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 3242771: // item
          this.getItem().add((ProductPlanCoverageBenefitItemComponent) value); // ProductPlanCoverageBenefitItemComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type")) {
          this.type = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("item")) {
          this.getItem().add((ProductPlanCoverageBenefitItemComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610:  return getType(); 
        case 3242771:  return addItem(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case 3242771: /*item*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("item")) {
          return addItem();
        }
        else
          return super.addChild(name);
      }

      public ProductPlanCoverageBenefitComponent copy() {
        ProductPlanCoverageBenefitComponent dst = new ProductPlanCoverageBenefitComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        if (item != null) {
          dst.item = new ArrayList<ProductPlanCoverageBenefitItemComponent>();
          for (ProductPlanCoverageBenefitItemComponent i : item)
            dst.item.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ProductPlanCoverageBenefitComponent))
          return false;
        ProductPlanCoverageBenefitComponent o = (ProductPlanCoverageBenefitComponent) other_;
        return compareDeep(type, o.type, true) && compareDeep(item, o.item, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ProductPlanCoverageBenefitComponent))
          return false;
        ProductPlanCoverageBenefitComponent o = (ProductPlanCoverageBenefitComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(type, item);
      }

  public String fhirType() {
    return "ProductPlan.coverage.benefit";

  }

  }

    @Block()
    public static class ProductPlanCoverageBenefitItemComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Coded Details of the specific benefit (days; visits).
         */
        @Child(name = "code", type = {CodeableConcept.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Coded Details of the specific benefit (days; visits)", formalDefinition="Coded Details of the specific benefit (days; visits)." )
        protected CodeableConcept code;

        /**
         * Value of the specific benefit.
         */
        @Child(name = "benefitValue", type = {Quantity.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Value of the specific benefit", formalDefinition="Value of the specific benefit." )
        protected Quantity benefitValue;

        private static final long serialVersionUID = 1339622901L;

    /**
     * Constructor
     */
      public ProductPlanCoverageBenefitItemComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ProductPlanCoverageBenefitItemComponent(CodeableConcept code) {
        super();
        this.code = code;
      }

        /**
         * @return {@link #code} (Coded Details of the specific benefit (days; visits).)
         */
        public CodeableConcept getCode() { 
          if (this.code == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ProductPlanCoverageBenefitItemComponent.code");
            else if (Configuration.doAutoCreate())
              this.code = new CodeableConcept(); // cc
          return this.code;
        }

        public boolean hasCode() { 
          return this.code != null && !this.code.isEmpty();
        }

        /**
         * @param value {@link #code} (Coded Details of the specific benefit (days; visits).)
         */
        public ProductPlanCoverageBenefitItemComponent setCode(CodeableConcept value) { 
          this.code = value;
          return this;
        }

        /**
         * @return {@link #benefitValue} (Value of the specific benefit.)
         */
        public Quantity getBenefitValue() { 
          if (this.benefitValue == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ProductPlanCoverageBenefitItemComponent.benefitValue");
            else if (Configuration.doAutoCreate())
              this.benefitValue = new Quantity(); // cc
          return this.benefitValue;
        }

        public boolean hasBenefitValue() { 
          return this.benefitValue != null && !this.benefitValue.isEmpty();
        }

        /**
         * @param value {@link #benefitValue} (Value of the specific benefit.)
         */
        public ProductPlanCoverageBenefitItemComponent setBenefitValue(Quantity value) { 
          this.benefitValue = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("code", "CodeableConcept", "Coded Details of the specific benefit (days; visits).", 0, 1, code));
          children.add(new Property("benefitValue", "Quantity", "Value of the specific benefit.", 0, 1, benefitValue));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3059181: /*code*/  return new Property("code", "CodeableConcept", "Coded Details of the specific benefit (days; visits).", 0, 1, code);
          case 130822938: /*benefitValue*/  return new Property("benefitValue", "Quantity", "Value of the specific benefit.", 0, 1, benefitValue);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3059181: /*code*/ return this.code == null ? new Base[0] : new Base[] {this.code}; // CodeableConcept
        case 130822938: /*benefitValue*/ return this.benefitValue == null ? new Base[0] : new Base[] {this.benefitValue}; // Quantity
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3059181: // code
          this.code = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 130822938: // benefitValue
          this.benefitValue = castToQuantity(value); // Quantity
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("code")) {
          this.code = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("benefitValue")) {
          this.benefitValue = castToQuantity(value); // Quantity
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3059181:  return getCode(); 
        case 130822938:  return getBenefitValue(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3059181: /*code*/ return new String[] {"CodeableConcept"};
        case 130822938: /*benefitValue*/ return new String[] {"Quantity"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("code")) {
          this.code = new CodeableConcept();
          return this.code;
        }
        else if (name.equals("benefitValue")) {
          this.benefitValue = new Quantity();
          return this.benefitValue;
        }
        else
          return super.addChild(name);
      }

      public ProductPlanCoverageBenefitItemComponent copy() {
        ProductPlanCoverageBenefitItemComponent dst = new ProductPlanCoverageBenefitItemComponent();
        copyValues(dst);
        dst.code = code == null ? null : code.copy();
        dst.benefitValue = benefitValue == null ? null : benefitValue.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ProductPlanCoverageBenefitItemComponent))
          return false;
        ProductPlanCoverageBenefitItemComponent o = (ProductPlanCoverageBenefitItemComponent) other_;
        return compareDeep(code, o.code, true) && compareDeep(benefitValue, o.benefitValue, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ProductPlanCoverageBenefitItemComponent))
          return false;
        ProductPlanCoverageBenefitItemComponent o = (ProductPlanCoverageBenefitItemComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(code, benefitValue);
      }

  public String fhirType() {
    return "ProductPlan.coverage.benefit.item";

  }

  }

    @Block()
    public static class ProductPlanPlanComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Type of plan (Platinum; Gold; Silver; Bronze; High Deductable; Low Deductable).
         */
        @Child(name = "type", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Type of plan", formalDefinition="Type of plan (Platinum; Gold; Silver; Bronze; High Deductable; Low Deductable)." )
        protected CodeableConcept type;

        /**
         * Additional descriptive content about the plan.
         */
        @Child(name = "description", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Additional descriptive content about the plan", formalDefinition="Additional descriptive content about the plan." )
        protected StringType description;

        /**
         * Plan premium.
         */
        @Child(name = "premium", type = {Money.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Plan premium", formalDefinition="Plan premium." )
        protected Money premium;

        /**
         * List of the costs associated with plan benefits.
         */
        @Child(name = "category", type = {}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="List of the costs associated with plan benefits", formalDefinition="List of the costs associated with plan benefits." )
        protected List<ProductPlanPlanCategoryComponent> category;

        private static final long serialVersionUID = -648921485L;

    /**
     * Constructor
     */
      public ProductPlanPlanComponent() {
        super();
      }

        /**
         * @return {@link #type} (Type of plan (Platinum; Gold; Silver; Bronze; High Deductable; Low Deductable).)
         */
        public CodeableConcept getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ProductPlanPlanComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new CodeableConcept(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (Type of plan (Platinum; Gold; Silver; Bronze; High Deductable; Low Deductable).)
         */
        public ProductPlanPlanComponent setType(CodeableConcept value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #description} (Additional descriptive content about the plan.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public StringType getDescriptionElement() { 
          if (this.description == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ProductPlanPlanComponent.description");
            else if (Configuration.doAutoCreate())
              this.description = new StringType(); // bb
          return this.description;
        }

        public boolean hasDescriptionElement() { 
          return this.description != null && !this.description.isEmpty();
        }

        public boolean hasDescription() { 
          return this.description != null && !this.description.isEmpty();
        }

        /**
         * @param value {@link #description} (Additional descriptive content about the plan.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public ProductPlanPlanComponent setDescriptionElement(StringType value) { 
          this.description = value;
          return this;
        }

        /**
         * @return Additional descriptive content about the plan.
         */
        public String getDescription() { 
          return this.description == null ? null : this.description.getValue();
        }

        /**
         * @param value Additional descriptive content about the plan.
         */
        public ProductPlanPlanComponent setDescription(String value) { 
          if (Utilities.noString(value))
            this.description = null;
          else {
            if (this.description == null)
              this.description = new StringType();
            this.description.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #premium} (Plan premium.)
         */
        public Money getPremium() { 
          if (this.premium == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ProductPlanPlanComponent.premium");
            else if (Configuration.doAutoCreate())
              this.premium = new Money(); // cc
          return this.premium;
        }

        public boolean hasPremium() { 
          return this.premium != null && !this.premium.isEmpty();
        }

        /**
         * @param value {@link #premium} (Plan premium.)
         */
        public ProductPlanPlanComponent setPremium(Money value) { 
          this.premium = value;
          return this;
        }

        /**
         * @return {@link #category} (List of the costs associated with plan benefits.)
         */
        public List<ProductPlanPlanCategoryComponent> getCategory() { 
          if (this.category == null)
            this.category = new ArrayList<ProductPlanPlanCategoryComponent>();
          return this.category;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ProductPlanPlanComponent setCategory(List<ProductPlanPlanCategoryComponent> theCategory) { 
          this.category = theCategory;
          return this;
        }

        public boolean hasCategory() { 
          if (this.category == null)
            return false;
          for (ProductPlanPlanCategoryComponent item : this.category)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public ProductPlanPlanCategoryComponent addCategory() { //3
          ProductPlanPlanCategoryComponent t = new ProductPlanPlanCategoryComponent();
          if (this.category == null)
            this.category = new ArrayList<ProductPlanPlanCategoryComponent>();
          this.category.add(t);
          return t;
        }

        public ProductPlanPlanComponent addCategory(ProductPlanPlanCategoryComponent t) { //3
          if (t == null)
            return this;
          if (this.category == null)
            this.category = new ArrayList<ProductPlanPlanCategoryComponent>();
          this.category.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #category}, creating it if it does not already exist
         */
        public ProductPlanPlanCategoryComponent getCategoryFirstRep() { 
          if (getCategory().isEmpty()) {
            addCategory();
          }
          return getCategory().get(0);
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("type", "CodeableConcept", "Type of plan (Platinum; Gold; Silver; Bronze; High Deductable; Low Deductable).", 0, 1, type));
          children.add(new Property("description", "string", "Additional descriptive content about the plan.", 0, 1, description));
          children.add(new Property("premium", "Money", "Plan premium.", 0, 1, premium));
          children.add(new Property("category", "", "List of the costs associated with plan benefits.", 0, java.lang.Integer.MAX_VALUE, category));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3575610: /*type*/  return new Property("type", "CodeableConcept", "Type of plan (Platinum; Gold; Silver; Bronze; High Deductable; Low Deductable).", 0, 1, type);
          case -1724546052: /*description*/  return new Property("description", "string", "Additional descriptive content about the plan.", 0, 1, description);
          case -318452137: /*premium*/  return new Property("premium", "Money", "Plan premium.", 0, 1, premium);
          case 50511102: /*category*/  return new Property("category", "", "List of the costs associated with plan benefits.", 0, java.lang.Integer.MAX_VALUE, category);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // StringType
        case -318452137: /*premium*/ return this.premium == null ? new Base[0] : new Base[] {this.premium}; // Money
        case 50511102: /*category*/ return this.category == null ? new Base[0] : this.category.toArray(new Base[this.category.size()]); // ProductPlanPlanCategoryComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1724546052: // description
          this.description = castToString(value); // StringType
          return value;
        case -318452137: // premium
          this.premium = castToMoney(value); // Money
          return value;
        case 50511102: // category
          this.getCategory().add((ProductPlanPlanCategoryComponent) value); // ProductPlanPlanCategoryComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type")) {
          this.type = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("description")) {
          this.description = castToString(value); // StringType
        } else if (name.equals("premium")) {
          this.premium = castToMoney(value); // Money
        } else if (name.equals("category")) {
          this.getCategory().add((ProductPlanPlanCategoryComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610:  return getType(); 
        case -1724546052:  return getDescriptionElement();
        case -318452137:  return getPremium(); 
        case 50511102:  return addCategory(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case -1724546052: /*description*/ return new String[] {"string"};
        case -318452137: /*premium*/ return new String[] {"Money"};
        case 50511102: /*category*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type ProductPlan.description");
        }
        else if (name.equals("premium")) {
          this.premium = new Money();
          return this.premium;
        }
        else if (name.equals("category")) {
          return addCategory();
        }
        else
          return super.addChild(name);
      }

      public ProductPlanPlanComponent copy() {
        ProductPlanPlanComponent dst = new ProductPlanPlanComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.description = description == null ? null : description.copy();
        dst.premium = premium == null ? null : premium.copy();
        if (category != null) {
          dst.category = new ArrayList<ProductPlanPlanCategoryComponent>();
          for (ProductPlanPlanCategoryComponent i : category)
            dst.category.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ProductPlanPlanComponent))
          return false;
        ProductPlanPlanComponent o = (ProductPlanPlanComponent) other_;
        return compareDeep(type, o.type, true) && compareDeep(description, o.description, true) && compareDeep(premium, o.premium, true)
           && compareDeep(category, o.category, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ProductPlanPlanComponent))
          return false;
        ProductPlanPlanComponent o = (ProductPlanPlanComponent) other_;
        return compareValues(description, o.description, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(type, description, premium
          , category);
      }

  public String fhirType() {
    return "ProductPlan.plan";

  }

  }

    @Block()
    public static class ProductPlanPlanCategoryComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * General category of benefit (Medical; Dental; Vision; Drug; Mental Health; Substance Abuse; Hospice, Home Health).
         */
        @Child(name = "code", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="General category of benefit (Medical; Dental; Vision; Drug; Mental Health; Substance Abuse; Hospice, Home Health)", formalDefinition="General category of benefit (Medical; Dental; Vision; Drug; Mental Health; Substance Abuse; Hospice, Home Health)." )
        protected CodeableConcept code;

        /**
         * List of the specific benefits under this category of benefit.
         */
        @Child(name = "benefit", type = {}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="List of the specific benefits under this category of benefit", formalDefinition="List of the specific benefits under this category of benefit." )
        protected List<ProductPlanPlanCategoryBenefitComponent> benefit;

        private static final long serialVersionUID = -1101029706L;

    /**
     * Constructor
     */
      public ProductPlanPlanCategoryComponent() {
        super();
      }

        /**
         * @return {@link #code} (General category of benefit (Medical; Dental; Vision; Drug; Mental Health; Substance Abuse; Hospice, Home Health).)
         */
        public CodeableConcept getCode() { 
          if (this.code == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ProductPlanPlanCategoryComponent.code");
            else if (Configuration.doAutoCreate())
              this.code = new CodeableConcept(); // cc
          return this.code;
        }

        public boolean hasCode() { 
          return this.code != null && !this.code.isEmpty();
        }

        /**
         * @param value {@link #code} (General category of benefit (Medical; Dental; Vision; Drug; Mental Health; Substance Abuse; Hospice, Home Health).)
         */
        public ProductPlanPlanCategoryComponent setCode(CodeableConcept value) { 
          this.code = value;
          return this;
        }

        /**
         * @return {@link #benefit} (List of the specific benefits under this category of benefit.)
         */
        public List<ProductPlanPlanCategoryBenefitComponent> getBenefit() { 
          if (this.benefit == null)
            this.benefit = new ArrayList<ProductPlanPlanCategoryBenefitComponent>();
          return this.benefit;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ProductPlanPlanCategoryComponent setBenefit(List<ProductPlanPlanCategoryBenefitComponent> theBenefit) { 
          this.benefit = theBenefit;
          return this;
        }

        public boolean hasBenefit() { 
          if (this.benefit == null)
            return false;
          for (ProductPlanPlanCategoryBenefitComponent item : this.benefit)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public ProductPlanPlanCategoryBenefitComponent addBenefit() { //3
          ProductPlanPlanCategoryBenefitComponent t = new ProductPlanPlanCategoryBenefitComponent();
          if (this.benefit == null)
            this.benefit = new ArrayList<ProductPlanPlanCategoryBenefitComponent>();
          this.benefit.add(t);
          return t;
        }

        public ProductPlanPlanCategoryComponent addBenefit(ProductPlanPlanCategoryBenefitComponent t) { //3
          if (t == null)
            return this;
          if (this.benefit == null)
            this.benefit = new ArrayList<ProductPlanPlanCategoryBenefitComponent>();
          this.benefit.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #benefit}, creating it if it does not already exist
         */
        public ProductPlanPlanCategoryBenefitComponent getBenefitFirstRep() { 
          if (getBenefit().isEmpty()) {
            addBenefit();
          }
          return getBenefit().get(0);
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("code", "CodeableConcept", "General category of benefit (Medical; Dental; Vision; Drug; Mental Health; Substance Abuse; Hospice, Home Health).", 0, 1, code));
          children.add(new Property("benefit", "", "List of the specific benefits under this category of benefit.", 0, java.lang.Integer.MAX_VALUE, benefit));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3059181: /*code*/  return new Property("code", "CodeableConcept", "General category of benefit (Medical; Dental; Vision; Drug; Mental Health; Substance Abuse; Hospice, Home Health).", 0, 1, code);
          case -222710633: /*benefit*/  return new Property("benefit", "", "List of the specific benefits under this category of benefit.", 0, java.lang.Integer.MAX_VALUE, benefit);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3059181: /*code*/ return this.code == null ? new Base[0] : new Base[] {this.code}; // CodeableConcept
        case -222710633: /*benefit*/ return this.benefit == null ? new Base[0] : this.benefit.toArray(new Base[this.benefit.size()]); // ProductPlanPlanCategoryBenefitComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3059181: // code
          this.code = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -222710633: // benefit
          this.getBenefit().add((ProductPlanPlanCategoryBenefitComponent) value); // ProductPlanPlanCategoryBenefitComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("code")) {
          this.code = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("benefit")) {
          this.getBenefit().add((ProductPlanPlanCategoryBenefitComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3059181:  return getCode(); 
        case -222710633:  return addBenefit(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3059181: /*code*/ return new String[] {"CodeableConcept"};
        case -222710633: /*benefit*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("code")) {
          this.code = new CodeableConcept();
          return this.code;
        }
        else if (name.equals("benefit")) {
          return addBenefit();
        }
        else
          return super.addChild(name);
      }

      public ProductPlanPlanCategoryComponent copy() {
        ProductPlanPlanCategoryComponent dst = new ProductPlanPlanCategoryComponent();
        copyValues(dst);
        dst.code = code == null ? null : code.copy();
        if (benefit != null) {
          dst.benefit = new ArrayList<ProductPlanPlanCategoryBenefitComponent>();
          for (ProductPlanPlanCategoryBenefitComponent i : benefit)
            dst.benefit.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ProductPlanPlanCategoryComponent))
          return false;
        ProductPlanPlanCategoryComponent o = (ProductPlanPlanCategoryComponent) other_;
        return compareDeep(code, o.code, true) && compareDeep(benefit, o.benefit, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ProductPlanPlanCategoryComponent))
          return false;
        ProductPlanPlanCategoryComponent o = (ProductPlanPlanCategoryComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(code, benefit);
      }

  public String fhirType() {
    return "ProductPlan.plan.category";

  }

  }

    @Block()
    public static class ProductPlanPlanCategoryBenefitComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Type of specific benefit (preventative; primary care office visit; speciality office visit; hospitalization; emergency room; urgent care).
         */
        @Child(name = "type", type = {CodeableConcept.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Type of specific benefit (preventative; primary care office visit; speciality office visit; hospitalization; emergency room; urgent care)", formalDefinition="Type of specific benefit (preventative; primary care office visit; speciality office visit; hospitalization; emergency room; urgent care)." )
        protected CodeableConcept type;

        /**
         * List of the costs associated with a specific benefit.
         */
        @Child(name = "cost", type = {}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="List of the costs associated with a specific benefit", formalDefinition="List of the costs associated with a specific benefit." )
        protected List<ProductPlanPlanCategoryBenefitCostComponent> cost;

        private static final long serialVersionUID = -1945596688L;

    /**
     * Constructor
     */
      public ProductPlanPlanCategoryBenefitComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ProductPlanPlanCategoryBenefitComponent(CodeableConcept type) {
        super();
        this.type = type;
      }

        /**
         * @return {@link #type} (Type of specific benefit (preventative; primary care office visit; speciality office visit; hospitalization; emergency room; urgent care).)
         */
        public CodeableConcept getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ProductPlanPlanCategoryBenefitComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new CodeableConcept(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (Type of specific benefit (preventative; primary care office visit; speciality office visit; hospitalization; emergency room; urgent care).)
         */
        public ProductPlanPlanCategoryBenefitComponent setType(CodeableConcept value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #cost} (List of the costs associated with a specific benefit.)
         */
        public List<ProductPlanPlanCategoryBenefitCostComponent> getCost() { 
          if (this.cost == null)
            this.cost = new ArrayList<ProductPlanPlanCategoryBenefitCostComponent>();
          return this.cost;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ProductPlanPlanCategoryBenefitComponent setCost(List<ProductPlanPlanCategoryBenefitCostComponent> theCost) { 
          this.cost = theCost;
          return this;
        }

        public boolean hasCost() { 
          if (this.cost == null)
            return false;
          for (ProductPlanPlanCategoryBenefitCostComponent item : this.cost)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public ProductPlanPlanCategoryBenefitCostComponent addCost() { //3
          ProductPlanPlanCategoryBenefitCostComponent t = new ProductPlanPlanCategoryBenefitCostComponent();
          if (this.cost == null)
            this.cost = new ArrayList<ProductPlanPlanCategoryBenefitCostComponent>();
          this.cost.add(t);
          return t;
        }

        public ProductPlanPlanCategoryBenefitComponent addCost(ProductPlanPlanCategoryBenefitCostComponent t) { //3
          if (t == null)
            return this;
          if (this.cost == null)
            this.cost = new ArrayList<ProductPlanPlanCategoryBenefitCostComponent>();
          this.cost.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #cost}, creating it if it does not already exist
         */
        public ProductPlanPlanCategoryBenefitCostComponent getCostFirstRep() { 
          if (getCost().isEmpty()) {
            addCost();
          }
          return getCost().get(0);
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("type", "CodeableConcept", "Type of specific benefit (preventative; primary care office visit; speciality office visit; hospitalization; emergency room; urgent care).", 0, 1, type));
          children.add(new Property("cost", "", "List of the costs associated with a specific benefit.", 0, java.lang.Integer.MAX_VALUE, cost));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3575610: /*type*/  return new Property("type", "CodeableConcept", "Type of specific benefit (preventative; primary care office visit; speciality office visit; hospitalization; emergency room; urgent care).", 0, 1, type);
          case 3059661: /*cost*/  return new Property("cost", "", "List of the costs associated with a specific benefit.", 0, java.lang.Integer.MAX_VALUE, cost);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case 3059661: /*cost*/ return this.cost == null ? new Base[0] : this.cost.toArray(new Base[this.cost.size()]); // ProductPlanPlanCategoryBenefitCostComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 3059661: // cost
          this.getCost().add((ProductPlanPlanCategoryBenefitCostComponent) value); // ProductPlanPlanCategoryBenefitCostComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type")) {
          this.type = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("cost")) {
          this.getCost().add((ProductPlanPlanCategoryBenefitCostComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610:  return getType(); 
        case 3059661:  return addCost(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case 3059661: /*cost*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("cost")) {
          return addCost();
        }
        else
          return super.addChild(name);
      }

      public ProductPlanPlanCategoryBenefitComponent copy() {
        ProductPlanPlanCategoryBenefitComponent dst = new ProductPlanPlanCategoryBenefitComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        if (cost != null) {
          dst.cost = new ArrayList<ProductPlanPlanCategoryBenefitCostComponent>();
          for (ProductPlanPlanCategoryBenefitCostComponent i : cost)
            dst.cost.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ProductPlanPlanCategoryBenefitComponent))
          return false;
        ProductPlanPlanCategoryBenefitComponent o = (ProductPlanPlanCategoryBenefitComponent) other_;
        return compareDeep(type, o.type, true) && compareDeep(cost, o.cost, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ProductPlanPlanCategoryBenefitComponent))
          return false;
        ProductPlanPlanCategoryBenefitComponent o = (ProductPlanPlanCategoryBenefitComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(type, cost);
      }

  public String fhirType() {
    return "ProductPlan.plan.category.benefit";

  }

  }

    @Block()
    public static class ProductPlanPlanCategoryBenefitCostComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Type of cost (copay; individual cap; family cap; coinsurance; deductible).
         */
        @Child(name = "type", type = {CodeableConcept.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Type of cost (copay; individual cap; family cap; coinsurance; deductible)", formalDefinition="Type of cost (copay; individual cap; family cap; coinsurance; deductible)." )
        protected CodeableConcept type;

        /**
         * Whether the cost applies to in-network or out-of-network providers (in-network; out-of-network; other).
         */
        @Child(name = "applicability", type = {Coding.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Whether the cost applies to in-network or out-of-network providers (in-network; out-of-network; other)", formalDefinition="Whether the cost applies to in-network or out-of-network providers (in-network; out-of-network; other)." )
        protected List<Coding> applicability;

        /**
         * Additional information about the cost, such as information about funding sources (e.g. HSA, HRA, FSA, RRA).
         */
        @Child(name = "qualifiers", type = {StringType.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Additional information about the cost, such as information about funding sources (e.g. HSA, HRA, FSA, RRA)", formalDefinition="Additional information about the cost, such as information about funding sources (e.g. HSA, HRA, FSA, RRA)." )
        protected List<StringType> qualifiers;

        /**
         * The actual cost value.
         */
        @Child(name = "value", type = {Quantity.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The actual cost value", formalDefinition="The actual cost value." )
        protected Quantity value;

        private static final long serialVersionUID = -1047196778L;

    /**
     * Constructor
     */
      public ProductPlanPlanCategoryBenefitCostComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ProductPlanPlanCategoryBenefitCostComponent(CodeableConcept type) {
        super();
        this.type = type;
      }

        /**
         * @return {@link #type} (Type of cost (copay; individual cap; family cap; coinsurance; deductible).)
         */
        public CodeableConcept getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ProductPlanPlanCategoryBenefitCostComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new CodeableConcept(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (Type of cost (copay; individual cap; family cap; coinsurance; deductible).)
         */
        public ProductPlanPlanCategoryBenefitCostComponent setType(CodeableConcept value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #applicability} (Whether the cost applies to in-network or out-of-network providers (in-network; out-of-network; other).)
         */
        public List<Coding> getApplicability() { 
          if (this.applicability == null)
            this.applicability = new ArrayList<Coding>();
          return this.applicability;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ProductPlanPlanCategoryBenefitCostComponent setApplicability(List<Coding> theApplicability) { 
          this.applicability = theApplicability;
          return this;
        }

        public boolean hasApplicability() { 
          if (this.applicability == null)
            return false;
          for (Coding item : this.applicability)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public Coding addApplicability() { //3
          Coding t = new Coding();
          if (this.applicability == null)
            this.applicability = new ArrayList<Coding>();
          this.applicability.add(t);
          return t;
        }

        public ProductPlanPlanCategoryBenefitCostComponent addApplicability(Coding t) { //3
          if (t == null)
            return this;
          if (this.applicability == null)
            this.applicability = new ArrayList<Coding>();
          this.applicability.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #applicability}, creating it if it does not already exist
         */
        public Coding getApplicabilityFirstRep() { 
          if (getApplicability().isEmpty()) {
            addApplicability();
          }
          return getApplicability().get(0);
        }

        /**
         * @return {@link #qualifiers} (Additional information about the cost, such as information about funding sources (e.g. HSA, HRA, FSA, RRA).)
         */
        public List<StringType> getQualifiers() { 
          if (this.qualifiers == null)
            this.qualifiers = new ArrayList<StringType>();
          return this.qualifiers;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ProductPlanPlanCategoryBenefitCostComponent setQualifiers(List<StringType> theQualifiers) { 
          this.qualifiers = theQualifiers;
          return this;
        }

        public boolean hasQualifiers() { 
          if (this.qualifiers == null)
            return false;
          for (StringType item : this.qualifiers)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #qualifiers} (Additional information about the cost, such as information about funding sources (e.g. HSA, HRA, FSA, RRA).)
         */
        public StringType addQualifiersElement() {//2 
          StringType t = new StringType();
          if (this.qualifiers == null)
            this.qualifiers = new ArrayList<StringType>();
          this.qualifiers.add(t);
          return t;
        }

        /**
         * @param value {@link #qualifiers} (Additional information about the cost, such as information about funding sources (e.g. HSA, HRA, FSA, RRA).)
         */
        public ProductPlanPlanCategoryBenefitCostComponent addQualifiers(String value) { //1
          StringType t = new StringType();
          t.setValue(value);
          if (this.qualifiers == null)
            this.qualifiers = new ArrayList<StringType>();
          this.qualifiers.add(t);
          return this;
        }

        /**
         * @param value {@link #qualifiers} (Additional information about the cost, such as information about funding sources (e.g. HSA, HRA, FSA, RRA).)
         */
        public boolean hasQualifiers(String value) { 
          if (this.qualifiers == null)
            return false;
          for (StringType v : this.qualifiers)
            if (v.getValue().equals(value)) // string
              return true;
          return false;
        }

        /**
         * @return {@link #value} (The actual cost value.)
         */
        public Quantity getValue() { 
          if (this.value == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ProductPlanPlanCategoryBenefitCostComponent.value");
            else if (Configuration.doAutoCreate())
              this.value = new Quantity(); // cc
          return this.value;
        }

        public boolean hasValue() { 
          return this.value != null && !this.value.isEmpty();
        }

        /**
         * @param value {@link #value} (The actual cost value.)
         */
        public ProductPlanPlanCategoryBenefitCostComponent setValue(Quantity value) { 
          this.value = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("type", "CodeableConcept", "Type of cost (copay; individual cap; family cap; coinsurance; deductible).", 0, 1, type));
          children.add(new Property("applicability", "Coding", "Whether the cost applies to in-network or out-of-network providers (in-network; out-of-network; other).", 0, java.lang.Integer.MAX_VALUE, applicability));
          children.add(new Property("qualifiers", "string", "Additional information about the cost, such as information about funding sources (e.g. HSA, HRA, FSA, RRA).", 0, java.lang.Integer.MAX_VALUE, qualifiers));
          children.add(new Property("value", "Quantity", "The actual cost value.", 0, 1, value));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3575610: /*type*/  return new Property("type", "CodeableConcept", "Type of cost (copay; individual cap; family cap; coinsurance; deductible).", 0, 1, type);
          case -1526770491: /*applicability*/  return new Property("applicability", "Coding", "Whether the cost applies to in-network or out-of-network providers (in-network; out-of-network; other).", 0, java.lang.Integer.MAX_VALUE, applicability);
          case -31447799: /*qualifiers*/  return new Property("qualifiers", "string", "Additional information about the cost, such as information about funding sources (e.g. HSA, HRA, FSA, RRA).", 0, java.lang.Integer.MAX_VALUE, qualifiers);
          case 111972721: /*value*/  return new Property("value", "Quantity", "The actual cost value.", 0, 1, value);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case -1526770491: /*applicability*/ return this.applicability == null ? new Base[0] : this.applicability.toArray(new Base[this.applicability.size()]); // Coding
        case -31447799: /*qualifiers*/ return this.qualifiers == null ? new Base[0] : this.qualifiers.toArray(new Base[this.qualifiers.size()]); // StringType
        case 111972721: /*value*/ return this.value == null ? new Base[0] : new Base[] {this.value}; // Quantity
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1526770491: // applicability
          this.getApplicability().add(castToCoding(value)); // Coding
          return value;
        case -31447799: // qualifiers
          this.getQualifiers().add(castToString(value)); // StringType
          return value;
        case 111972721: // value
          this.value = castToQuantity(value); // Quantity
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type")) {
          this.type = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("applicability")) {
          this.getApplicability().add(castToCoding(value));
        } else if (name.equals("qualifiers")) {
          this.getQualifiers().add(castToString(value));
        } else if (name.equals("value")) {
          this.value = castToQuantity(value); // Quantity
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610:  return getType(); 
        case -1526770491:  return addApplicability(); 
        case -31447799:  return addQualifiersElement();
        case 111972721:  return getValue(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case -1526770491: /*applicability*/ return new String[] {"Coding"};
        case -31447799: /*qualifiers*/ return new String[] {"string"};
        case 111972721: /*value*/ return new String[] {"Quantity"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("applicability")) {
          return addApplicability();
        }
        else if (name.equals("qualifiers")) {
          throw new FHIRException("Cannot call addChild on a primitive type ProductPlan.qualifiers");
        }
        else if (name.equals("value")) {
          this.value = new Quantity();
          return this.value;
        }
        else
          return super.addChild(name);
      }

      public ProductPlanPlanCategoryBenefitCostComponent copy() {
        ProductPlanPlanCategoryBenefitCostComponent dst = new ProductPlanPlanCategoryBenefitCostComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        if (applicability != null) {
          dst.applicability = new ArrayList<Coding>();
          for (Coding i : applicability)
            dst.applicability.add(i.copy());
        };
        if (qualifiers != null) {
          dst.qualifiers = new ArrayList<StringType>();
          for (StringType i : qualifiers)
            dst.qualifiers.add(i.copy());
        };
        dst.value = value == null ? null : value.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ProductPlanPlanCategoryBenefitCostComponent))
          return false;
        ProductPlanPlanCategoryBenefitCostComponent o = (ProductPlanPlanCategoryBenefitCostComponent) other_;
        return compareDeep(type, o.type, true) && compareDeep(applicability, o.applicability, true) && compareDeep(qualifiers, o.qualifiers, true)
           && compareDeep(value, o.value, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ProductPlanPlanCategoryBenefitCostComponent))
          return false;
        ProductPlanPlanCategoryBenefitCostComponent o = (ProductPlanPlanCategoryBenefitCostComponent) other_;
        return compareValues(qualifiers, o.qualifiers, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(type, applicability, qualifiers
          , value);
      }

  public String fhirType() {
    return "ProductPlan.plan.category.benefit.cost";

  }

  }

    /**
     * Identifier for the product/plan that is used to identify it across multiple disparate systems.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Identifies this product/plan  across multiple systems", formalDefinition="Identifier for the product/plan that is used to identify it across multiple disparate systems." )
    protected List<Identifier> identifier;

    /**
     * Whether the organization's record is still in active use.
     */
    @Child(name = "status", type = {CodeType.class}, order=1, min=0, max=1, modifier=true, summary=true)
    @Description(shortDefinition="draft | active | retired | unknown", formalDefinition="Whether the organization's record is still in active use." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/publication-status")
    protected Enumeration<PublicationStatus> status;

    /**
     * The type of product/plan.
     */
    @Child(name = "type", type = {CodeableConcept.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Kind of product/plan", formalDefinition="The type of product/plan." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/productplan-type")
    protected List<CodeableConcept> type;

    /**
     * Official name of the product/plan (as designated by the owner).
     */
    @Child(name = "name", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Official name", formalDefinition="Official name of the product/plan (as designated by the owner)." )
    protected StringType name;

    /**
     * A list of alternate names that the product/plan is known as, or was known as in the past.
     */
    @Child(name = "alias", type = {StringType.class}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="A list of alternate names that the product/plan is known as, or was known as in the past", formalDefinition="A list of alternate names that the product/plan is known as, or was known as in the past." )
    protected List<StringType> alias;

    /**
     * The period of time that the product is available.
     */
    @Child(name = "period", type = {Period.class}, order=5, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="The time period the product/plan is available", formalDefinition="The period of time that the product is available." )
    protected Period period;

    /**
     * Owner of the product/plan (typically a payer).
     */
    @Child(name = "ownedBy", type = {Organization.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Owner of the product/plan", formalDefinition="Owner of the product/plan (typically a payer)." )
    protected Reference ownedBy;

    /**
     * The actual object that is the target of the reference (Owner of the product/plan (typically a payer).)
     */
    protected Organization ownedByTarget;

    /**
     * Administrator of the product/plan (e.g. self-insured employer plan administered by a TPA).
     */
    @Child(name = "administeredBy", type = {Organization.class}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Administrator of the product/plan", formalDefinition="Administrator of the product/plan (e.g. self-insured employer plan administered by a TPA)." )
    protected Reference administeredBy;

    /**
     * The actual object that is the target of the reference (Administrator of the product/plan (e.g. self-insured employer plan administered by a TPA).)
     */
    protected Organization administeredByTarget;

    /**
     * An address for the organization.
     */
    @Child(name = "address", type = {Address.class}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="An address for the organization", formalDefinition="An address for the organization." )
    protected List<Address> address;

    /**
     * The geographic region in which this product/plan is available.
     */
    @Child(name = "coverageArea", type = {Location.class}, order=9, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The geographic region in which this product/plan is available", formalDefinition="The geographic region in which this product/plan is available." )
    protected Reference coverageArea;

    /**
     * The actual object that is the target of the reference (The geographic region in which this product/plan is available.)
     */
    protected Location coverageAreaTarget;

    /**
     * Contact for the product/plan for a certain purpose.
     */
    @Child(name = "contact", type = {}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Contact for the product/plan for a certain purpose", formalDefinition="Contact for the product/plan for a certain purpose." )
    protected List<ProductPlanContactComponent> contact;

    /**
     * Details about the coverage offered by the insurance product.
     */
    @Child(name = "coverage", type = {}, order=11, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Details about the coverage offered by the insurance product", formalDefinition="Details about the coverage offered by the insurance product." )
    protected List<ProductPlanCoverageComponent> coverage;

    /**
     * Details about an insurance plan.
     */
    @Child(name = "plan", type = {}, order=12, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Details about an insurance plan", formalDefinition="Details about an insurance plan." )
    protected List<ProductPlanPlanComponent> plan;

    /**
     * Technical endpoints providing access to services operated for the organization.
     */
    @Child(name = "endpoint", type = {Endpoint.class}, order=13, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Technical endpoints providing access to services operated for the organization", formalDefinition="Technical endpoints providing access to services operated for the organization." )
    protected List<Reference> endpoint;
    /**
     * The actual objects that are the target of the reference (Technical endpoints providing access to services operated for the organization.)
     */
    protected List<Endpoint> endpointTarget;


    private static final long serialVersionUID = -840176614L;

  /**
   * Constructor
   */
    public ProductPlan() {
      super();
    }

    /**
     * @return {@link #identifier} (Identifier for the product/plan that is used to identify it across multiple disparate systems.)
     */
    public List<Identifier> getIdentifier() { 
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      return this.identifier;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ProductPlan setIdentifier(List<Identifier> theIdentifier) { 
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

    public ProductPlan addIdentifier(Identifier t) { //3
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
     * @return {@link #status} (Whether the organization's record is still in active use.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<PublicationStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ProductPlan.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<PublicationStatus>(new PublicationStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (Whether the organization's record is still in active use.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public ProductPlan setStatusElement(Enumeration<PublicationStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return Whether the organization's record is still in active use.
     */
    public PublicationStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value Whether the organization's record is still in active use.
     */
    public ProductPlan setStatus(PublicationStatus value) { 
      if (value == null)
        this.status = null;
      else {
        if (this.status == null)
          this.status = new Enumeration<PublicationStatus>(new PublicationStatusEnumFactory());
        this.status.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #type} (The type of product/plan.)
     */
    public List<CodeableConcept> getType() { 
      if (this.type == null)
        this.type = new ArrayList<CodeableConcept>();
      return this.type;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ProductPlan setType(List<CodeableConcept> theType) { 
      this.type = theType;
      return this;
    }

    public boolean hasType() { 
      if (this.type == null)
        return false;
      for (CodeableConcept item : this.type)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addType() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.type == null)
        this.type = new ArrayList<CodeableConcept>();
      this.type.add(t);
      return t;
    }

    public ProductPlan addType(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.type == null)
        this.type = new ArrayList<CodeableConcept>();
      this.type.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #type}, creating it if it does not already exist
     */
    public CodeableConcept getTypeFirstRep() { 
      if (getType().isEmpty()) {
        addType();
      }
      return getType().get(0);
    }

    /**
     * @return {@link #name} (Official name of the product/plan (as designated by the owner).). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public StringType getNameElement() { 
      if (this.name == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ProductPlan.name");
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
     * @param value {@link #name} (Official name of the product/plan (as designated by the owner).). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public ProductPlan setNameElement(StringType value) { 
      this.name = value;
      return this;
    }

    /**
     * @return Official name of the product/plan (as designated by the owner).
     */
    public String getName() { 
      return this.name == null ? null : this.name.getValue();
    }

    /**
     * @param value Official name of the product/plan (as designated by the owner).
     */
    public ProductPlan setName(String value) { 
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
     * @return {@link #alias} (A list of alternate names that the product/plan is known as, or was known as in the past.)
     */
    public List<StringType> getAlias() { 
      if (this.alias == null)
        this.alias = new ArrayList<StringType>();
      return this.alias;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ProductPlan setAlias(List<StringType> theAlias) { 
      this.alias = theAlias;
      return this;
    }

    public boolean hasAlias() { 
      if (this.alias == null)
        return false;
      for (StringType item : this.alias)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #alias} (A list of alternate names that the product/plan is known as, or was known as in the past.)
     */
    public StringType addAliasElement() {//2 
      StringType t = new StringType();
      if (this.alias == null)
        this.alias = new ArrayList<StringType>();
      this.alias.add(t);
      return t;
    }

    /**
     * @param value {@link #alias} (A list of alternate names that the product/plan is known as, or was known as in the past.)
     */
    public ProductPlan addAlias(String value) { //1
      StringType t = new StringType();
      t.setValue(value);
      if (this.alias == null)
        this.alias = new ArrayList<StringType>();
      this.alias.add(t);
      return this;
    }

    /**
     * @param value {@link #alias} (A list of alternate names that the product/plan is known as, or was known as in the past.)
     */
    public boolean hasAlias(String value) { 
      if (this.alias == null)
        return false;
      for (StringType v : this.alias)
        if (v.getValue().equals(value)) // string
          return true;
      return false;
    }

    /**
     * @return {@link #period} (The period of time that the product is available.)
     */
    public Period getPeriod() { 
      if (this.period == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ProductPlan.period");
        else if (Configuration.doAutoCreate())
          this.period = new Period(); // cc
      return this.period;
    }

    public boolean hasPeriod() { 
      return this.period != null && !this.period.isEmpty();
    }

    /**
     * @param value {@link #period} (The period of time that the product is available.)
     */
    public ProductPlan setPeriod(Period value) { 
      this.period = value;
      return this;
    }

    /**
     * @return {@link #ownedBy} (Owner of the product/plan (typically a payer).)
     */
    public Reference getOwnedBy() { 
      if (this.ownedBy == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ProductPlan.ownedBy");
        else if (Configuration.doAutoCreate())
          this.ownedBy = new Reference(); // cc
      return this.ownedBy;
    }

    public boolean hasOwnedBy() { 
      return this.ownedBy != null && !this.ownedBy.isEmpty();
    }

    /**
     * @param value {@link #ownedBy} (Owner of the product/plan (typically a payer).)
     */
    public ProductPlan setOwnedBy(Reference value) { 
      this.ownedBy = value;
      return this;
    }

    /**
     * @return {@link #ownedBy} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Owner of the product/plan (typically a payer).)
     */
    public Organization getOwnedByTarget() { 
      if (this.ownedByTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ProductPlan.ownedBy");
        else if (Configuration.doAutoCreate())
          this.ownedByTarget = new Organization(); // aa
      return this.ownedByTarget;
    }

    /**
     * @param value {@link #ownedBy} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Owner of the product/plan (typically a payer).)
     */
    public ProductPlan setOwnedByTarget(Organization value) { 
      this.ownedByTarget = value;
      return this;
    }

    /**
     * @return {@link #administeredBy} (Administrator of the product/plan (e.g. self-insured employer plan administered by a TPA).)
     */
    public Reference getAdministeredBy() { 
      if (this.administeredBy == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ProductPlan.administeredBy");
        else if (Configuration.doAutoCreate())
          this.administeredBy = new Reference(); // cc
      return this.administeredBy;
    }

    public boolean hasAdministeredBy() { 
      return this.administeredBy != null && !this.administeredBy.isEmpty();
    }

    /**
     * @param value {@link #administeredBy} (Administrator of the product/plan (e.g. self-insured employer plan administered by a TPA).)
     */
    public ProductPlan setAdministeredBy(Reference value) { 
      this.administeredBy = value;
      return this;
    }

    /**
     * @return {@link #administeredBy} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Administrator of the product/plan (e.g. self-insured employer plan administered by a TPA).)
     */
    public Organization getAdministeredByTarget() { 
      if (this.administeredByTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ProductPlan.administeredBy");
        else if (Configuration.doAutoCreate())
          this.administeredByTarget = new Organization(); // aa
      return this.administeredByTarget;
    }

    /**
     * @param value {@link #administeredBy} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Administrator of the product/plan (e.g. self-insured employer plan administered by a TPA).)
     */
    public ProductPlan setAdministeredByTarget(Organization value) { 
      this.administeredByTarget = value;
      return this;
    }

    /**
     * @return {@link #address} (An address for the organization.)
     */
    public List<Address> getAddress() { 
      if (this.address == null)
        this.address = new ArrayList<Address>();
      return this.address;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ProductPlan setAddress(List<Address> theAddress) { 
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

    public ProductPlan addAddress(Address t) { //3
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
     * @return {@link #coverageArea} (The geographic region in which this product/plan is available.)
     */
    public Reference getCoverageArea() { 
      if (this.coverageArea == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ProductPlan.coverageArea");
        else if (Configuration.doAutoCreate())
          this.coverageArea = new Reference(); // cc
      return this.coverageArea;
    }

    public boolean hasCoverageArea() { 
      return this.coverageArea != null && !this.coverageArea.isEmpty();
    }

    /**
     * @param value {@link #coverageArea} (The geographic region in which this product/plan is available.)
     */
    public ProductPlan setCoverageArea(Reference value) { 
      this.coverageArea = value;
      return this;
    }

    /**
     * @return {@link #coverageArea} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The geographic region in which this product/plan is available.)
     */
    public Location getCoverageAreaTarget() { 
      if (this.coverageAreaTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ProductPlan.coverageArea");
        else if (Configuration.doAutoCreate())
          this.coverageAreaTarget = new Location(); // aa
      return this.coverageAreaTarget;
    }

    /**
     * @param value {@link #coverageArea} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The geographic region in which this product/plan is available.)
     */
    public ProductPlan setCoverageAreaTarget(Location value) { 
      this.coverageAreaTarget = value;
      return this;
    }

    /**
     * @return {@link #contact} (Contact for the product/plan for a certain purpose.)
     */
    public List<ProductPlanContactComponent> getContact() { 
      if (this.contact == null)
        this.contact = new ArrayList<ProductPlanContactComponent>();
      return this.contact;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ProductPlan setContact(List<ProductPlanContactComponent> theContact) { 
      this.contact = theContact;
      return this;
    }

    public boolean hasContact() { 
      if (this.contact == null)
        return false;
      for (ProductPlanContactComponent item : this.contact)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public ProductPlanContactComponent addContact() { //3
      ProductPlanContactComponent t = new ProductPlanContactComponent();
      if (this.contact == null)
        this.contact = new ArrayList<ProductPlanContactComponent>();
      this.contact.add(t);
      return t;
    }

    public ProductPlan addContact(ProductPlanContactComponent t) { //3
      if (t == null)
        return this;
      if (this.contact == null)
        this.contact = new ArrayList<ProductPlanContactComponent>();
      this.contact.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #contact}, creating it if it does not already exist
     */
    public ProductPlanContactComponent getContactFirstRep() { 
      if (getContact().isEmpty()) {
        addContact();
      }
      return getContact().get(0);
    }

    /**
     * @return {@link #coverage} (Details about the coverage offered by the insurance product.)
     */
    public List<ProductPlanCoverageComponent> getCoverage() { 
      if (this.coverage == null)
        this.coverage = new ArrayList<ProductPlanCoverageComponent>();
      return this.coverage;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ProductPlan setCoverage(List<ProductPlanCoverageComponent> theCoverage) { 
      this.coverage = theCoverage;
      return this;
    }

    public boolean hasCoverage() { 
      if (this.coverage == null)
        return false;
      for (ProductPlanCoverageComponent item : this.coverage)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public ProductPlanCoverageComponent addCoverage() { //3
      ProductPlanCoverageComponent t = new ProductPlanCoverageComponent();
      if (this.coverage == null)
        this.coverage = new ArrayList<ProductPlanCoverageComponent>();
      this.coverage.add(t);
      return t;
    }

    public ProductPlan addCoverage(ProductPlanCoverageComponent t) { //3
      if (t == null)
        return this;
      if (this.coverage == null)
        this.coverage = new ArrayList<ProductPlanCoverageComponent>();
      this.coverage.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #coverage}, creating it if it does not already exist
     */
    public ProductPlanCoverageComponent getCoverageFirstRep() { 
      if (getCoverage().isEmpty()) {
        addCoverage();
      }
      return getCoverage().get(0);
    }

    /**
     * @return {@link #plan} (Details about an insurance plan.)
     */
    public List<ProductPlanPlanComponent> getPlan() { 
      if (this.plan == null)
        this.plan = new ArrayList<ProductPlanPlanComponent>();
      return this.plan;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ProductPlan setPlan(List<ProductPlanPlanComponent> thePlan) { 
      this.plan = thePlan;
      return this;
    }

    public boolean hasPlan() { 
      if (this.plan == null)
        return false;
      for (ProductPlanPlanComponent item : this.plan)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public ProductPlanPlanComponent addPlan() { //3
      ProductPlanPlanComponent t = new ProductPlanPlanComponent();
      if (this.plan == null)
        this.plan = new ArrayList<ProductPlanPlanComponent>();
      this.plan.add(t);
      return t;
    }

    public ProductPlan addPlan(ProductPlanPlanComponent t) { //3
      if (t == null)
        return this;
      if (this.plan == null)
        this.plan = new ArrayList<ProductPlanPlanComponent>();
      this.plan.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #plan}, creating it if it does not already exist
     */
    public ProductPlanPlanComponent getPlanFirstRep() { 
      if (getPlan().isEmpty()) {
        addPlan();
      }
      return getPlan().get(0);
    }

    /**
     * @return {@link #endpoint} (Technical endpoints providing access to services operated for the organization.)
     */
    public List<Reference> getEndpoint() { 
      if (this.endpoint == null)
        this.endpoint = new ArrayList<Reference>();
      return this.endpoint;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ProductPlan setEndpoint(List<Reference> theEndpoint) { 
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

    public ProductPlan addEndpoint(Reference t) { //3
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
        children.add(new Property("identifier", "Identifier", "Identifier for the product/plan that is used to identify it across multiple disparate systems.", 0, java.lang.Integer.MAX_VALUE, identifier));
        children.add(new Property("status", "code", "Whether the organization's record is still in active use.", 0, 1, status));
        children.add(new Property("type", "CodeableConcept", "The type of product/plan.", 0, java.lang.Integer.MAX_VALUE, type));
        children.add(new Property("name", "string", "Official name of the product/plan (as designated by the owner).", 0, 1, name));
        children.add(new Property("alias", "string", "A list of alternate names that the product/plan is known as, or was known as in the past.", 0, java.lang.Integer.MAX_VALUE, alias));
        children.add(new Property("period", "Period", "The period of time that the product is available.", 0, 1, period));
        children.add(new Property("ownedBy", "Reference(Organization)", "Owner of the product/plan (typically a payer).", 0, 1, ownedBy));
        children.add(new Property("administeredBy", "Reference(Organization)", "Administrator of the product/plan (e.g. self-insured employer plan administered by a TPA).", 0, 1, administeredBy));
        children.add(new Property("address", "Address", "An address for the organization.", 0, java.lang.Integer.MAX_VALUE, address));
        children.add(new Property("coverageArea", "Reference(Location)", "The geographic region in which this product/plan is available.", 0, 1, coverageArea));
        children.add(new Property("contact", "", "Contact for the product/plan for a certain purpose.", 0, java.lang.Integer.MAX_VALUE, contact));
        children.add(new Property("coverage", "", "Details about the coverage offered by the insurance product.", 0, java.lang.Integer.MAX_VALUE, coverage));
        children.add(new Property("plan", "", "Details about an insurance plan.", 0, java.lang.Integer.MAX_VALUE, plan));
        children.add(new Property("endpoint", "Reference(Endpoint)", "Technical endpoints providing access to services operated for the organization.", 0, java.lang.Integer.MAX_VALUE, endpoint));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case -1618432855: /*identifier*/  return new Property("identifier", "Identifier", "Identifier for the product/plan that is used to identify it across multiple disparate systems.", 0, java.lang.Integer.MAX_VALUE, identifier);
        case -892481550: /*status*/  return new Property("status", "code", "Whether the organization's record is still in active use.", 0, 1, status);
        case 3575610: /*type*/  return new Property("type", "CodeableConcept", "The type of product/plan.", 0, java.lang.Integer.MAX_VALUE, type);
        case 3373707: /*name*/  return new Property("name", "string", "Official name of the product/plan (as designated by the owner).", 0, 1, name);
        case 92902992: /*alias*/  return new Property("alias", "string", "A list of alternate names that the product/plan is known as, or was known as in the past.", 0, java.lang.Integer.MAX_VALUE, alias);
        case -991726143: /*period*/  return new Property("period", "Period", "The period of time that the product is available.", 0, 1, period);
        case -1054743076: /*ownedBy*/  return new Property("ownedBy", "Reference(Organization)", "Owner of the product/plan (typically a payer).", 0, 1, ownedBy);
        case 898770462: /*administeredBy*/  return new Property("administeredBy", "Reference(Organization)", "Administrator of the product/plan (e.g. self-insured employer plan administered by a TPA).", 0, 1, administeredBy);
        case -1147692044: /*address*/  return new Property("address", "Address", "An address for the organization.", 0, java.lang.Integer.MAX_VALUE, address);
        case -1532328299: /*coverageArea*/  return new Property("coverageArea", "Reference(Location)", "The geographic region in which this product/plan is available.", 0, 1, coverageArea);
        case 951526432: /*contact*/  return new Property("contact", "", "Contact for the product/plan for a certain purpose.", 0, java.lang.Integer.MAX_VALUE, contact);
        case -351767064: /*coverage*/  return new Property("coverage", "", "Details about the coverage offered by the insurance product.", 0, java.lang.Integer.MAX_VALUE, coverage);
        case 3443497: /*plan*/  return new Property("plan", "", "Details about an insurance plan.", 0, java.lang.Integer.MAX_VALUE, plan);
        case 1741102485: /*endpoint*/  return new Property("endpoint", "Reference(Endpoint)", "Technical endpoints providing access to services operated for the organization.", 0, java.lang.Integer.MAX_VALUE, endpoint);
        default: return super.getNamedProperty(_hash, _name, _checkValid);
        }

      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<PublicationStatus>
        case 3575610: /*type*/ return this.type == null ? new Base[0] : this.type.toArray(new Base[this.type.size()]); // CodeableConcept
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case 92902992: /*alias*/ return this.alias == null ? new Base[0] : this.alias.toArray(new Base[this.alias.size()]); // StringType
        case -991726143: /*period*/ return this.period == null ? new Base[0] : new Base[] {this.period}; // Period
        case -1054743076: /*ownedBy*/ return this.ownedBy == null ? new Base[0] : new Base[] {this.ownedBy}; // Reference
        case 898770462: /*administeredBy*/ return this.administeredBy == null ? new Base[0] : new Base[] {this.administeredBy}; // Reference
        case -1147692044: /*address*/ return this.address == null ? new Base[0] : this.address.toArray(new Base[this.address.size()]); // Address
        case -1532328299: /*coverageArea*/ return this.coverageArea == null ? new Base[0] : new Base[] {this.coverageArea}; // Reference
        case 951526432: /*contact*/ return this.contact == null ? new Base[0] : this.contact.toArray(new Base[this.contact.size()]); // ProductPlanContactComponent
        case -351767064: /*coverage*/ return this.coverage == null ? new Base[0] : this.coverage.toArray(new Base[this.coverage.size()]); // ProductPlanCoverageComponent
        case 3443497: /*plan*/ return this.plan == null ? new Base[0] : this.plan.toArray(new Base[this.plan.size()]); // ProductPlanPlanComponent
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
        case -892481550: // status
          value = new PublicationStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<PublicationStatus>
          return value;
        case 3575610: // type
          this.getType().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 3373707: // name
          this.name = castToString(value); // StringType
          return value;
        case 92902992: // alias
          this.getAlias().add(castToString(value)); // StringType
          return value;
        case -991726143: // period
          this.period = castToPeriod(value); // Period
          return value;
        case -1054743076: // ownedBy
          this.ownedBy = castToReference(value); // Reference
          return value;
        case 898770462: // administeredBy
          this.administeredBy = castToReference(value); // Reference
          return value;
        case -1147692044: // address
          this.getAddress().add(castToAddress(value)); // Address
          return value;
        case -1532328299: // coverageArea
          this.coverageArea = castToReference(value); // Reference
          return value;
        case 951526432: // contact
          this.getContact().add((ProductPlanContactComponent) value); // ProductPlanContactComponent
          return value;
        case -351767064: // coverage
          this.getCoverage().add((ProductPlanCoverageComponent) value); // ProductPlanCoverageComponent
          return value;
        case 3443497: // plan
          this.getPlan().add((ProductPlanPlanComponent) value); // ProductPlanPlanComponent
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
        } else if (name.equals("status")) {
          value = new PublicationStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<PublicationStatus>
        } else if (name.equals("type")) {
          this.getType().add(castToCodeableConcept(value));
        } else if (name.equals("name")) {
          this.name = castToString(value); // StringType
        } else if (name.equals("alias")) {
          this.getAlias().add(castToString(value));
        } else if (name.equals("period")) {
          this.period = castToPeriod(value); // Period
        } else if (name.equals("ownedBy")) {
          this.ownedBy = castToReference(value); // Reference
        } else if (name.equals("administeredBy")) {
          this.administeredBy = castToReference(value); // Reference
        } else if (name.equals("address")) {
          this.getAddress().add(castToAddress(value));
        } else if (name.equals("coverageArea")) {
          this.coverageArea = castToReference(value); // Reference
        } else if (name.equals("contact")) {
          this.getContact().add((ProductPlanContactComponent) value);
        } else if (name.equals("coverage")) {
          this.getCoverage().add((ProductPlanCoverageComponent) value);
        } else if (name.equals("plan")) {
          this.getPlan().add((ProductPlanPlanComponent) value);
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
        case -892481550:  return getStatusElement();
        case 3575610:  return addType(); 
        case 3373707:  return getNameElement();
        case 92902992:  return addAliasElement();
        case -991726143:  return getPeriod(); 
        case -1054743076:  return getOwnedBy(); 
        case 898770462:  return getAdministeredBy(); 
        case -1147692044:  return addAddress(); 
        case -1532328299:  return getCoverageArea(); 
        case 951526432:  return addContact(); 
        case -351767064:  return addCoverage(); 
        case 3443497:  return addPlan(); 
        case 1741102485:  return addEndpoint(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case -892481550: /*status*/ return new String[] {"code"};
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case 3373707: /*name*/ return new String[] {"string"};
        case 92902992: /*alias*/ return new String[] {"string"};
        case -991726143: /*period*/ return new String[] {"Period"};
        case -1054743076: /*ownedBy*/ return new String[] {"Reference"};
        case 898770462: /*administeredBy*/ return new String[] {"Reference"};
        case -1147692044: /*address*/ return new String[] {"Address"};
        case -1532328299: /*coverageArea*/ return new String[] {"Reference"};
        case 951526432: /*contact*/ return new String[] {};
        case -351767064: /*coverage*/ return new String[] {};
        case 3443497: /*plan*/ return new String[] {};
        case 1741102485: /*endpoint*/ return new String[] {"Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type ProductPlan.status");
        }
        else if (name.equals("type")) {
          return addType();
        }
        else if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type ProductPlan.name");
        }
        else if (name.equals("alias")) {
          throw new FHIRException("Cannot call addChild on a primitive type ProductPlan.alias");
        }
        else if (name.equals("period")) {
          this.period = new Period();
          return this.period;
        }
        else if (name.equals("ownedBy")) {
          this.ownedBy = new Reference();
          return this.ownedBy;
        }
        else if (name.equals("administeredBy")) {
          this.administeredBy = new Reference();
          return this.administeredBy;
        }
        else if (name.equals("address")) {
          return addAddress();
        }
        else if (name.equals("coverageArea")) {
          this.coverageArea = new Reference();
          return this.coverageArea;
        }
        else if (name.equals("contact")) {
          return addContact();
        }
        else if (name.equals("coverage")) {
          return addCoverage();
        }
        else if (name.equals("plan")) {
          return addPlan();
        }
        else if (name.equals("endpoint")) {
          return addEndpoint();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "ProductPlan";

  }

      public ProductPlan copy() {
        ProductPlan dst = new ProductPlan();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.status = status == null ? null : status.copy();
        if (type != null) {
          dst.type = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : type)
            dst.type.add(i.copy());
        };
        dst.name = name == null ? null : name.copy();
        if (alias != null) {
          dst.alias = new ArrayList<StringType>();
          for (StringType i : alias)
            dst.alias.add(i.copy());
        };
        dst.period = period == null ? null : period.copy();
        dst.ownedBy = ownedBy == null ? null : ownedBy.copy();
        dst.administeredBy = administeredBy == null ? null : administeredBy.copy();
        if (address != null) {
          dst.address = new ArrayList<Address>();
          for (Address i : address)
            dst.address.add(i.copy());
        };
        dst.coverageArea = coverageArea == null ? null : coverageArea.copy();
        if (contact != null) {
          dst.contact = new ArrayList<ProductPlanContactComponent>();
          for (ProductPlanContactComponent i : contact)
            dst.contact.add(i.copy());
        };
        if (coverage != null) {
          dst.coverage = new ArrayList<ProductPlanCoverageComponent>();
          for (ProductPlanCoverageComponent i : coverage)
            dst.coverage.add(i.copy());
        };
        if (plan != null) {
          dst.plan = new ArrayList<ProductPlanPlanComponent>();
          for (ProductPlanPlanComponent i : plan)
            dst.plan.add(i.copy());
        };
        if (endpoint != null) {
          dst.endpoint = new ArrayList<Reference>();
          for (Reference i : endpoint)
            dst.endpoint.add(i.copy());
        };
        return dst;
      }

      protected ProductPlan typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ProductPlan))
          return false;
        ProductPlan o = (ProductPlan) other_;
        return compareDeep(identifier, o.identifier, true) && compareDeep(status, o.status, true) && compareDeep(type, o.type, true)
           && compareDeep(name, o.name, true) && compareDeep(alias, o.alias, true) && compareDeep(period, o.period, true)
           && compareDeep(ownedBy, o.ownedBy, true) && compareDeep(administeredBy, o.administeredBy, true)
           && compareDeep(address, o.address, true) && compareDeep(coverageArea, o.coverageArea, true) && compareDeep(contact, o.contact, true)
           && compareDeep(coverage, o.coverage, true) && compareDeep(plan, o.plan, true) && compareDeep(endpoint, o.endpoint, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ProductPlan))
          return false;
        ProductPlan o = (ProductPlan) other_;
        return compareValues(status, o.status, true) && compareValues(name, o.name, true) && compareValues(alias, o.alias, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, status, type
          , name, alias, period, ownedBy, administeredBy, address, coverageArea, contact
          , coverage, plan, endpoint);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.ProductPlan;
   }

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>Any identifier for the organization (not the accreditation issuer's identifier)</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ProductPlan.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="ProductPlan.identifier", description="Any identifier for the organization (not the accreditation issuer's identifier)", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>Any identifier for the organization (not the accreditation issuer's identifier)</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ProductPlan.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>address</b>
   * <p>
   * Description: <b>A server defined search that may match any of the string fields in the Address, including line, city, district, state, country, postalCode, and/or text</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ProductPlan.address</b><br>
   * </p>
   */
  @SearchParamDefinition(name="address", path="ProductPlan.address", description="A server defined search that may match any of the string fields in the Address, including line, city, district, state, country, postalCode, and/or text", type="string" )
  public static final String SP_ADDRESS = "address";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>address</b>
   * <p>
   * Description: <b>A server defined search that may match any of the string fields in the Address, including line, city, district, state, country, postalCode, and/or text</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ProductPlan.address</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam ADDRESS = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_ADDRESS);

 /**
   * Search parameter: <b>address-state</b>
   * <p>
   * Description: <b>A state specified in an address</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ProductPlan.address.state</b><br>
   * </p>
   */
  @SearchParamDefinition(name="address-state", path="ProductPlan.address.state", description="A state specified in an address", type="string" )
  public static final String SP_ADDRESS_STATE = "address-state";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>address-state</b>
   * <p>
   * Description: <b>A state specified in an address</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ProductPlan.address.state</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam ADDRESS_STATE = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_ADDRESS_STATE);

 /**
   * Search parameter: <b>owned-by</b>
   * <p>
   * Description: <b>An organization of which this organization forms a part</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ProductPlan.ownedBy</b><br>
   * </p>
   */
  @SearchParamDefinition(name="owned-by", path="ProductPlan.ownedBy", description="An organization of which this organization forms a part", type="reference", target={Organization.class } )
  public static final String SP_OWNED_BY = "owned-by";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>owned-by</b>
   * <p>
   * Description: <b>An organization of which this organization forms a part</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ProductPlan.ownedBy</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam OWNED_BY = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_OWNED_BY);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ProductPlan:owned-by</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_OWNED_BY = new ca.uhn.fhir.model.api.Include("ProductPlan:owned-by").toLocked();

 /**
   * Search parameter: <b>type</b>
   * <p>
   * Description: <b>A code for the type of organization</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ProductPlan.type</b><br>
   * </p>
   */
  @SearchParamDefinition(name="type", path="ProductPlan.type", description="A code for the type of organization", type="token" )
  public static final String SP_TYPE = "type";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>type</b>
   * <p>
   * Description: <b>A code for the type of organization</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ProductPlan.type</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam TYPE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_TYPE);

 /**
   * Search parameter: <b>address-postalcode</b>
   * <p>
   * Description: <b>A postal code specified in an address</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ProductPlan.address.postalCode</b><br>
   * </p>
   */
  @SearchParamDefinition(name="address-postalcode", path="ProductPlan.address.postalCode", description="A postal code specified in an address", type="string" )
  public static final String SP_ADDRESS_POSTALCODE = "address-postalcode";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>address-postalcode</b>
   * <p>
   * Description: <b>A postal code specified in an address</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ProductPlan.address.postalCode</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam ADDRESS_POSTALCODE = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_ADDRESS_POSTALCODE);

 /**
   * Search parameter: <b>administered-by</b>
   * <p>
   * Description: <b>Administrator of the product/plan</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ProductPlan.administeredBy</b><br>
   * </p>
   */
  @SearchParamDefinition(name="administered-by", path="ProductPlan.administeredBy", description="Administrator of the product/plan", type="reference", target={Organization.class } )
  public static final String SP_ADMINISTERED_BY = "administered-by";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>administered-by</b>
   * <p>
   * Description: <b>Administrator of the product/plan</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ProductPlan.administeredBy</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam ADMINISTERED_BY = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_ADMINISTERED_BY);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ProductPlan:administered-by</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_ADMINISTERED_BY = new ca.uhn.fhir.model.api.Include("ProductPlan:administered-by").toLocked();

 /**
   * Search parameter: <b>address-country</b>
   * <p>
   * Description: <b>A country specified in an address</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ProductPlan.address.country</b><br>
   * </p>
   */
  @SearchParamDefinition(name="address-country", path="ProductPlan.address.country", description="A country specified in an address", type="string" )
  public static final String SP_ADDRESS_COUNTRY = "address-country";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>address-country</b>
   * <p>
   * Description: <b>A country specified in an address</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ProductPlan.address.country</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam ADDRESS_COUNTRY = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_ADDRESS_COUNTRY);

 /**
   * Search parameter: <b>endpoint</b>
   * <p>
   * Description: <b>Technical endpoints providing access to services operated for the organization</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ProductPlan.endpoint</b><br>
   * </p>
   */
  @SearchParamDefinition(name="endpoint", path="ProductPlan.endpoint", description="Technical endpoints providing access to services operated for the organization", type="reference", target={Endpoint.class } )
  public static final String SP_ENDPOINT = "endpoint";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>endpoint</b>
   * <p>
   * Description: <b>Technical endpoints providing access to services operated for the organization</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ProductPlan.endpoint</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam ENDPOINT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_ENDPOINT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ProductPlan:endpoint</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_ENDPOINT = new ca.uhn.fhir.model.api.Include("ProductPlan:endpoint").toLocked();

 /**
   * Search parameter: <b>phonetic</b>
   * <p>
   * Description: <b>A portion of the organization's name using some kind of phonetic matching algorithm</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ProductPlan.name</b><br>
   * </p>
   */
  @SearchParamDefinition(name="phonetic", path="ProductPlan.name", description="A portion of the organization's name using some kind of phonetic matching algorithm", type="string" )
  public static final String SP_PHONETIC = "phonetic";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>phonetic</b>
   * <p>
   * Description: <b>A portion of the organization's name using some kind of phonetic matching algorithm</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ProductPlan.name</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam PHONETIC = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_PHONETIC);

 /**
   * Search parameter: <b>name</b>
   * <p>
   * Description: <b>A portion of the organization's name or alias</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ProductPlan.name, ProductPlan.alias</b><br>
   * </p>
   */
  @SearchParamDefinition(name="name", path="name | alias", description="A portion of the organization's name or alias", type="string" )
  public static final String SP_NAME = "name";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>name</b>
   * <p>
   * Description: <b>A portion of the organization's name or alias</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ProductPlan.name, ProductPlan.alias</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam NAME = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_NAME);

 /**
   * Search parameter: <b>address-use</b>
   * <p>
   * Description: <b>A use code specified in an address</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ProductPlan.address.use</b><br>
   * </p>
   */
  @SearchParamDefinition(name="address-use", path="ProductPlan.address.use", description="A use code specified in an address", type="token" )
  public static final String SP_ADDRESS_USE = "address-use";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>address-use</b>
   * <p>
   * Description: <b>A use code specified in an address</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ProductPlan.address.use</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam ADDRESS_USE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_ADDRESS_USE);

 /**
   * Search parameter: <b>address-city</b>
   * <p>
   * Description: <b>A city specified in an address</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ProductPlan.address.city</b><br>
   * </p>
   */
  @SearchParamDefinition(name="address-city", path="ProductPlan.address.city", description="A city specified in an address", type="string" )
  public static final String SP_ADDRESS_CITY = "address-city";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>address-city</b>
   * <p>
   * Description: <b>A city specified in an address</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ProductPlan.address.city</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam ADDRESS_CITY = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_ADDRESS_CITY);

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>Is the Organization record active</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ProductPlan.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="ProductPlan.status", description="Is the Organization record active", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>Is the Organization record active</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ProductPlan.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);


}

