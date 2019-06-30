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

// Generated on Thu, Dec 27, 2018 10:06-0500 for FHIR v4.0.0

import java.util.*;

import org.hl7.fhir.utilities.Utilities;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ChildOrder;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Block;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.exceptions.FHIRException;
/**
 * A medicinal product in a container or package.
 */
@ResourceDef(name="MedicinalProductPackaged", profile="http://hl7.org/fhir/StructureDefinition/MedicinalProductPackaged")
public class MedicinalProductPackaged extends DomainResource {

    @Block()
    public static class MedicinalProductPackagedBatchIdentifierComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A number appearing on the outer packaging of a specific batch.
         */
        @Child(name = "outerPackaging", type = {Identifier.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="A number appearing on the outer packaging of a specific batch", formalDefinition="A number appearing on the outer packaging of a specific batch." )
        protected Identifier outerPackaging;

        /**
         * A number appearing on the immediate packaging (and not the outer packaging).
         */
        @Child(name = "immediatePackaging", type = {Identifier.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="A number appearing on the immediate packaging (and not the outer packaging)", formalDefinition="A number appearing on the immediate packaging (and not the outer packaging)." )
        protected Identifier immediatePackaging;

        private static final long serialVersionUID = 1187365068L;

    /**
     * Constructor
     */
      public MedicinalProductPackagedBatchIdentifierComponent() {
        super();
      }

    /**
     * Constructor
     */
      public MedicinalProductPackagedBatchIdentifierComponent(Identifier outerPackaging) {
        super();
        this.outerPackaging = outerPackaging;
      }

        /**
         * @return {@link #outerPackaging} (A number appearing on the outer packaging of a specific batch.)
         */
        public Identifier getOuterPackaging() { 
          if (this.outerPackaging == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicinalProductPackagedBatchIdentifierComponent.outerPackaging");
            else if (Configuration.doAutoCreate())
              this.outerPackaging = new Identifier(); // cc
          return this.outerPackaging;
        }

        public boolean hasOuterPackaging() { 
          return this.outerPackaging != null && !this.outerPackaging.isEmpty();
        }

        /**
         * @param value {@link #outerPackaging} (A number appearing on the outer packaging of a specific batch.)
         */
        public MedicinalProductPackagedBatchIdentifierComponent setOuterPackaging(Identifier value) { 
          this.outerPackaging = value;
          return this;
        }

        /**
         * @return {@link #immediatePackaging} (A number appearing on the immediate packaging (and not the outer packaging).)
         */
        public Identifier getImmediatePackaging() { 
          if (this.immediatePackaging == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicinalProductPackagedBatchIdentifierComponent.immediatePackaging");
            else if (Configuration.doAutoCreate())
              this.immediatePackaging = new Identifier(); // cc
          return this.immediatePackaging;
        }

        public boolean hasImmediatePackaging() { 
          return this.immediatePackaging != null && !this.immediatePackaging.isEmpty();
        }

        /**
         * @param value {@link #immediatePackaging} (A number appearing on the immediate packaging (and not the outer packaging).)
         */
        public MedicinalProductPackagedBatchIdentifierComponent setImmediatePackaging(Identifier value) { 
          this.immediatePackaging = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("outerPackaging", "Identifier", "A number appearing on the outer packaging of a specific batch.", 0, 1, outerPackaging));
          children.add(new Property("immediatePackaging", "Identifier", "A number appearing on the immediate packaging (and not the outer packaging).", 0, 1, immediatePackaging));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -682249912: /*outerPackaging*/  return new Property("outerPackaging", "Identifier", "A number appearing on the outer packaging of a specific batch.", 0, 1, outerPackaging);
          case 721147602: /*immediatePackaging*/  return new Property("immediatePackaging", "Identifier", "A number appearing on the immediate packaging (and not the outer packaging).", 0, 1, immediatePackaging);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -682249912: /*outerPackaging*/ return this.outerPackaging == null ? new Base[0] : new Base[] {this.outerPackaging}; // Identifier
        case 721147602: /*immediatePackaging*/ return this.immediatePackaging == null ? new Base[0] : new Base[] {this.immediatePackaging}; // Identifier
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -682249912: // outerPackaging
          this.outerPackaging = castToIdentifier(value); // Identifier
          return value;
        case 721147602: // immediatePackaging
          this.immediatePackaging = castToIdentifier(value); // Identifier
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("outerPackaging")) {
          this.outerPackaging = castToIdentifier(value); // Identifier
        } else if (name.equals("immediatePackaging")) {
          this.immediatePackaging = castToIdentifier(value); // Identifier
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -682249912:  return getOuterPackaging(); 
        case 721147602:  return getImmediatePackaging(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -682249912: /*outerPackaging*/ return new String[] {"Identifier"};
        case 721147602: /*immediatePackaging*/ return new String[] {"Identifier"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("outerPackaging")) {
          this.outerPackaging = new Identifier();
          return this.outerPackaging;
        }
        else if (name.equals("immediatePackaging")) {
          this.immediatePackaging = new Identifier();
          return this.immediatePackaging;
        }
        else
          return super.addChild(name);
      }

      public MedicinalProductPackagedBatchIdentifierComponent copy() {
        MedicinalProductPackagedBatchIdentifierComponent dst = new MedicinalProductPackagedBatchIdentifierComponent();
        copyValues(dst);
        dst.outerPackaging = outerPackaging == null ? null : outerPackaging.copy();
        dst.immediatePackaging = immediatePackaging == null ? null : immediatePackaging.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof MedicinalProductPackagedBatchIdentifierComponent))
          return false;
        MedicinalProductPackagedBatchIdentifierComponent o = (MedicinalProductPackagedBatchIdentifierComponent) other_;
        return compareDeep(outerPackaging, o.outerPackaging, true) && compareDeep(immediatePackaging, o.immediatePackaging, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof MedicinalProductPackagedBatchIdentifierComponent))
          return false;
        MedicinalProductPackagedBatchIdentifierComponent o = (MedicinalProductPackagedBatchIdentifierComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(outerPackaging, immediatePackaging
          );
      }

  public String fhirType() {
    return "MedicinalProductPackaged.batchIdentifier";

  }

  }

    @Block()
    public static class MedicinalProductPackagedPackageItemComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Including possibly Data Carrier Identifier.
         */
        @Child(name = "identifier", type = {Identifier.class}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Including possibly Data Carrier Identifier", formalDefinition="Including possibly Data Carrier Identifier." )
        protected List<Identifier> identifier;

        /**
         * The physical type of the container of the medicine.
         */
        @Child(name = "type", type = {CodeableConcept.class}, order=2, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The physical type of the container of the medicine", formalDefinition="The physical type of the container of the medicine." )
        protected CodeableConcept type;

        /**
         * The quantity of this package in the medicinal product, at the current level of packaging. The outermost is always 1.
         */
        @Child(name = "quantity", type = {Quantity.class}, order=3, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The quantity of this package in the medicinal product, at the current level of packaging. The outermost is always 1", formalDefinition="The quantity of this package in the medicinal product, at the current level of packaging. The outermost is always 1." )
        protected Quantity quantity;

        /**
         * Material type of the package item.
         */
        @Child(name = "material", type = {CodeableConcept.class}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Material type of the package item", formalDefinition="Material type of the package item." )
        protected List<CodeableConcept> material;

        /**
         * A possible alternate material for the packaging.
         */
        @Child(name = "alternateMaterial", type = {CodeableConcept.class}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="A possible alternate material for the packaging", formalDefinition="A possible alternate material for the packaging." )
        protected List<CodeableConcept> alternateMaterial;

        /**
         * A device accompanying a medicinal product.
         */
        @Child(name = "device", type = {DeviceDefinition.class}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="A device accompanying a medicinal product", formalDefinition="A device accompanying a medicinal product." )
        protected List<Reference> device;
        /**
         * The actual objects that are the target of the reference (A device accompanying a medicinal product.)
         */
        protected List<DeviceDefinition> deviceTarget;


        /**
         * The manufactured item as contained in the packaged medicinal product.
         */
        @Child(name = "manufacturedItem", type = {MedicinalProductManufactured.class}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="The manufactured item as contained in the packaged medicinal product", formalDefinition="The manufactured item as contained in the packaged medicinal product." )
        protected List<Reference> manufacturedItem;
        /**
         * The actual objects that are the target of the reference (The manufactured item as contained in the packaged medicinal product.)
         */
        protected List<MedicinalProductManufactured> manufacturedItemTarget;


        /**
         * Allows containers within containers.
         */
        @Child(name = "packageItem", type = {MedicinalProductPackagedPackageItemComponent.class}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Allows containers within containers", formalDefinition="Allows containers within containers." )
        protected List<MedicinalProductPackagedPackageItemComponent> packageItem;

        /**
         * Dimensions, color etc.
         */
        @Child(name = "physicalCharacteristics", type = {ProdCharacteristic.class}, order=9, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Dimensions, color etc.", formalDefinition="Dimensions, color etc." )
        protected ProdCharacteristic physicalCharacteristics;

        /**
         * Other codeable characteristics.
         */
        @Child(name = "otherCharacteristics", type = {CodeableConcept.class}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Other codeable characteristics", formalDefinition="Other codeable characteristics." )
        protected List<CodeableConcept> otherCharacteristics;

        /**
         * Shelf Life and storage information.
         */
        @Child(name = "shelfLifeStorage", type = {ProductShelfLife.class}, order=11, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Shelf Life and storage information", formalDefinition="Shelf Life and storage information." )
        protected List<ProductShelfLife> shelfLifeStorage;

        /**
         * Manufacturer of this Package Item.
         */
        @Child(name = "manufacturer", type = {Organization.class}, order=12, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Manufacturer of this Package Item", formalDefinition="Manufacturer of this Package Item." )
        protected List<Reference> manufacturer;
        /**
         * The actual objects that are the target of the reference (Manufacturer of this Package Item.)
         */
        protected List<Organization> manufacturerTarget;


        private static final long serialVersionUID = 1454286533L;

    /**
     * Constructor
     */
      public MedicinalProductPackagedPackageItemComponent() {
        super();
      }

    /**
     * Constructor
     */
      public MedicinalProductPackagedPackageItemComponent(CodeableConcept type, Quantity quantity) {
        super();
        this.type = type;
        this.quantity = quantity;
      }

        /**
         * @return {@link #identifier} (Including possibly Data Carrier Identifier.)
         */
        public List<Identifier> getIdentifier() { 
          if (this.identifier == null)
            this.identifier = new ArrayList<Identifier>();
          return this.identifier;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public MedicinalProductPackagedPackageItemComponent setIdentifier(List<Identifier> theIdentifier) { 
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

        public MedicinalProductPackagedPackageItemComponent addIdentifier(Identifier t) { //3
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
         * @return {@link #type} (The physical type of the container of the medicine.)
         */
        public CodeableConcept getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicinalProductPackagedPackageItemComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new CodeableConcept(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (The physical type of the container of the medicine.)
         */
        public MedicinalProductPackagedPackageItemComponent setType(CodeableConcept value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #quantity} (The quantity of this package in the medicinal product, at the current level of packaging. The outermost is always 1.)
         */
        public Quantity getQuantity() { 
          if (this.quantity == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicinalProductPackagedPackageItemComponent.quantity");
            else if (Configuration.doAutoCreate())
              this.quantity = new Quantity(); // cc
          return this.quantity;
        }

        public boolean hasQuantity() { 
          return this.quantity != null && !this.quantity.isEmpty();
        }

        /**
         * @param value {@link #quantity} (The quantity of this package in the medicinal product, at the current level of packaging. The outermost is always 1.)
         */
        public MedicinalProductPackagedPackageItemComponent setQuantity(Quantity value) { 
          this.quantity = value;
          return this;
        }

        /**
         * @return {@link #material} (Material type of the package item.)
         */
        public List<CodeableConcept> getMaterial() { 
          if (this.material == null)
            this.material = new ArrayList<CodeableConcept>();
          return this.material;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public MedicinalProductPackagedPackageItemComponent setMaterial(List<CodeableConcept> theMaterial) { 
          this.material = theMaterial;
          return this;
        }

        public boolean hasMaterial() { 
          if (this.material == null)
            return false;
          for (CodeableConcept item : this.material)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public CodeableConcept addMaterial() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.material == null)
            this.material = new ArrayList<CodeableConcept>();
          this.material.add(t);
          return t;
        }

        public MedicinalProductPackagedPackageItemComponent addMaterial(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.material == null)
            this.material = new ArrayList<CodeableConcept>();
          this.material.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #material}, creating it if it does not already exist
         */
        public CodeableConcept getMaterialFirstRep() { 
          if (getMaterial().isEmpty()) {
            addMaterial();
          }
          return getMaterial().get(0);
        }

        /**
         * @return {@link #alternateMaterial} (A possible alternate material for the packaging.)
         */
        public List<CodeableConcept> getAlternateMaterial() { 
          if (this.alternateMaterial == null)
            this.alternateMaterial = new ArrayList<CodeableConcept>();
          return this.alternateMaterial;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public MedicinalProductPackagedPackageItemComponent setAlternateMaterial(List<CodeableConcept> theAlternateMaterial) { 
          this.alternateMaterial = theAlternateMaterial;
          return this;
        }

        public boolean hasAlternateMaterial() { 
          if (this.alternateMaterial == null)
            return false;
          for (CodeableConcept item : this.alternateMaterial)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public CodeableConcept addAlternateMaterial() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.alternateMaterial == null)
            this.alternateMaterial = new ArrayList<CodeableConcept>();
          this.alternateMaterial.add(t);
          return t;
        }

        public MedicinalProductPackagedPackageItemComponent addAlternateMaterial(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.alternateMaterial == null)
            this.alternateMaterial = new ArrayList<CodeableConcept>();
          this.alternateMaterial.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #alternateMaterial}, creating it if it does not already exist
         */
        public CodeableConcept getAlternateMaterialFirstRep() { 
          if (getAlternateMaterial().isEmpty()) {
            addAlternateMaterial();
          }
          return getAlternateMaterial().get(0);
        }

        /**
         * @return {@link #device} (A device accompanying a medicinal product.)
         */
        public List<Reference> getDevice() { 
          if (this.device == null)
            this.device = new ArrayList<Reference>();
          return this.device;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public MedicinalProductPackagedPackageItemComponent setDevice(List<Reference> theDevice) { 
          this.device = theDevice;
          return this;
        }

        public boolean hasDevice() { 
          if (this.device == null)
            return false;
          for (Reference item : this.device)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public Reference addDevice() { //3
          Reference t = new Reference();
          if (this.device == null)
            this.device = new ArrayList<Reference>();
          this.device.add(t);
          return t;
        }

        public MedicinalProductPackagedPackageItemComponent addDevice(Reference t) { //3
          if (t == null)
            return this;
          if (this.device == null)
            this.device = new ArrayList<Reference>();
          this.device.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #device}, creating it if it does not already exist
         */
        public Reference getDeviceFirstRep() { 
          if (getDevice().isEmpty()) {
            addDevice();
          }
          return getDevice().get(0);
        }

        /**
         * @deprecated Use Reference#setResource(IBaseResource) instead
         */
        @Deprecated
        public List<DeviceDefinition> getDeviceTarget() { 
          if (this.deviceTarget == null)
            this.deviceTarget = new ArrayList<DeviceDefinition>();
          return this.deviceTarget;
        }

        /**
         * @deprecated Use Reference#setResource(IBaseResource) instead
         */
        @Deprecated
        public DeviceDefinition addDeviceTarget() { 
          DeviceDefinition r = new DeviceDefinition();
          if (this.deviceTarget == null)
            this.deviceTarget = new ArrayList<DeviceDefinition>();
          this.deviceTarget.add(r);
          return r;
        }

        /**
         * @return {@link #manufacturedItem} (The manufactured item as contained in the packaged medicinal product.)
         */
        public List<Reference> getManufacturedItem() { 
          if (this.manufacturedItem == null)
            this.manufacturedItem = new ArrayList<Reference>();
          return this.manufacturedItem;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public MedicinalProductPackagedPackageItemComponent setManufacturedItem(List<Reference> theManufacturedItem) { 
          this.manufacturedItem = theManufacturedItem;
          return this;
        }

        public boolean hasManufacturedItem() { 
          if (this.manufacturedItem == null)
            return false;
          for (Reference item : this.manufacturedItem)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public Reference addManufacturedItem() { //3
          Reference t = new Reference();
          if (this.manufacturedItem == null)
            this.manufacturedItem = new ArrayList<Reference>();
          this.manufacturedItem.add(t);
          return t;
        }

        public MedicinalProductPackagedPackageItemComponent addManufacturedItem(Reference t) { //3
          if (t == null)
            return this;
          if (this.manufacturedItem == null)
            this.manufacturedItem = new ArrayList<Reference>();
          this.manufacturedItem.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #manufacturedItem}, creating it if it does not already exist
         */
        public Reference getManufacturedItemFirstRep() { 
          if (getManufacturedItem().isEmpty()) {
            addManufacturedItem();
          }
          return getManufacturedItem().get(0);
        }

        /**
         * @deprecated Use Reference#setResource(IBaseResource) instead
         */
        @Deprecated
        public List<MedicinalProductManufactured> getManufacturedItemTarget() { 
          if (this.manufacturedItemTarget == null)
            this.manufacturedItemTarget = new ArrayList<MedicinalProductManufactured>();
          return this.manufacturedItemTarget;
        }

        /**
         * @deprecated Use Reference#setResource(IBaseResource) instead
         */
        @Deprecated
        public MedicinalProductManufactured addManufacturedItemTarget() { 
          MedicinalProductManufactured r = new MedicinalProductManufactured();
          if (this.manufacturedItemTarget == null)
            this.manufacturedItemTarget = new ArrayList<MedicinalProductManufactured>();
          this.manufacturedItemTarget.add(r);
          return r;
        }

        /**
         * @return {@link #packageItem} (Allows containers within containers.)
         */
        public List<MedicinalProductPackagedPackageItemComponent> getPackageItem() { 
          if (this.packageItem == null)
            this.packageItem = new ArrayList<MedicinalProductPackagedPackageItemComponent>();
          return this.packageItem;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public MedicinalProductPackagedPackageItemComponent setPackageItem(List<MedicinalProductPackagedPackageItemComponent> thePackageItem) { 
          this.packageItem = thePackageItem;
          return this;
        }

        public boolean hasPackageItem() { 
          if (this.packageItem == null)
            return false;
          for (MedicinalProductPackagedPackageItemComponent item : this.packageItem)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public MedicinalProductPackagedPackageItemComponent addPackageItem() { //3
          MedicinalProductPackagedPackageItemComponent t = new MedicinalProductPackagedPackageItemComponent();
          if (this.packageItem == null)
            this.packageItem = new ArrayList<MedicinalProductPackagedPackageItemComponent>();
          this.packageItem.add(t);
          return t;
        }

        public MedicinalProductPackagedPackageItemComponent addPackageItem(MedicinalProductPackagedPackageItemComponent t) { //3
          if (t == null)
            return this;
          if (this.packageItem == null)
            this.packageItem = new ArrayList<MedicinalProductPackagedPackageItemComponent>();
          this.packageItem.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #packageItem}, creating it if it does not already exist
         */
        public MedicinalProductPackagedPackageItemComponent getPackageItemFirstRep() { 
          if (getPackageItem().isEmpty()) {
            addPackageItem();
          }
          return getPackageItem().get(0);
        }

        /**
         * @return {@link #physicalCharacteristics} (Dimensions, color etc.)
         */
        public ProdCharacteristic getPhysicalCharacteristics() { 
          if (this.physicalCharacteristics == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicinalProductPackagedPackageItemComponent.physicalCharacteristics");
            else if (Configuration.doAutoCreate())
              this.physicalCharacteristics = new ProdCharacteristic(); // cc
          return this.physicalCharacteristics;
        }

        public boolean hasPhysicalCharacteristics() { 
          return this.physicalCharacteristics != null && !this.physicalCharacteristics.isEmpty();
        }

        /**
         * @param value {@link #physicalCharacteristics} (Dimensions, color etc.)
         */
        public MedicinalProductPackagedPackageItemComponent setPhysicalCharacteristics(ProdCharacteristic value) { 
          this.physicalCharacteristics = value;
          return this;
        }

        /**
         * @return {@link #otherCharacteristics} (Other codeable characteristics.)
         */
        public List<CodeableConcept> getOtherCharacteristics() { 
          if (this.otherCharacteristics == null)
            this.otherCharacteristics = new ArrayList<CodeableConcept>();
          return this.otherCharacteristics;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public MedicinalProductPackagedPackageItemComponent setOtherCharacteristics(List<CodeableConcept> theOtherCharacteristics) { 
          this.otherCharacteristics = theOtherCharacteristics;
          return this;
        }

        public boolean hasOtherCharacteristics() { 
          if (this.otherCharacteristics == null)
            return false;
          for (CodeableConcept item : this.otherCharacteristics)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public CodeableConcept addOtherCharacteristics() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.otherCharacteristics == null)
            this.otherCharacteristics = new ArrayList<CodeableConcept>();
          this.otherCharacteristics.add(t);
          return t;
        }

        public MedicinalProductPackagedPackageItemComponent addOtherCharacteristics(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.otherCharacteristics == null)
            this.otherCharacteristics = new ArrayList<CodeableConcept>();
          this.otherCharacteristics.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #otherCharacteristics}, creating it if it does not already exist
         */
        public CodeableConcept getOtherCharacteristicsFirstRep() { 
          if (getOtherCharacteristics().isEmpty()) {
            addOtherCharacteristics();
          }
          return getOtherCharacteristics().get(0);
        }

        /**
         * @return {@link #shelfLifeStorage} (Shelf Life and storage information.)
         */
        public List<ProductShelfLife> getShelfLifeStorage() { 
          if (this.shelfLifeStorage == null)
            this.shelfLifeStorage = new ArrayList<ProductShelfLife>();
          return this.shelfLifeStorage;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public MedicinalProductPackagedPackageItemComponent setShelfLifeStorage(List<ProductShelfLife> theShelfLifeStorage) { 
          this.shelfLifeStorage = theShelfLifeStorage;
          return this;
        }

        public boolean hasShelfLifeStorage() { 
          if (this.shelfLifeStorage == null)
            return false;
          for (ProductShelfLife item : this.shelfLifeStorage)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public ProductShelfLife addShelfLifeStorage() { //3
          ProductShelfLife t = new ProductShelfLife();
          if (this.shelfLifeStorage == null)
            this.shelfLifeStorage = new ArrayList<ProductShelfLife>();
          this.shelfLifeStorage.add(t);
          return t;
        }

        public MedicinalProductPackagedPackageItemComponent addShelfLifeStorage(ProductShelfLife t) { //3
          if (t == null)
            return this;
          if (this.shelfLifeStorage == null)
            this.shelfLifeStorage = new ArrayList<ProductShelfLife>();
          this.shelfLifeStorage.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #shelfLifeStorage}, creating it if it does not already exist
         */
        public ProductShelfLife getShelfLifeStorageFirstRep() { 
          if (getShelfLifeStorage().isEmpty()) {
            addShelfLifeStorage();
          }
          return getShelfLifeStorage().get(0);
        }

        /**
         * @return {@link #manufacturer} (Manufacturer of this Package Item.)
         */
        public List<Reference> getManufacturer() { 
          if (this.manufacturer == null)
            this.manufacturer = new ArrayList<Reference>();
          return this.manufacturer;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public MedicinalProductPackagedPackageItemComponent setManufacturer(List<Reference> theManufacturer) { 
          this.manufacturer = theManufacturer;
          return this;
        }

        public boolean hasManufacturer() { 
          if (this.manufacturer == null)
            return false;
          for (Reference item : this.manufacturer)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public Reference addManufacturer() { //3
          Reference t = new Reference();
          if (this.manufacturer == null)
            this.manufacturer = new ArrayList<Reference>();
          this.manufacturer.add(t);
          return t;
        }

        public MedicinalProductPackagedPackageItemComponent addManufacturer(Reference t) { //3
          if (t == null)
            return this;
          if (this.manufacturer == null)
            this.manufacturer = new ArrayList<Reference>();
          this.manufacturer.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #manufacturer}, creating it if it does not already exist
         */
        public Reference getManufacturerFirstRep() { 
          if (getManufacturer().isEmpty()) {
            addManufacturer();
          }
          return getManufacturer().get(0);
        }

        /**
         * @deprecated Use Reference#setResource(IBaseResource) instead
         */
        @Deprecated
        public List<Organization> getManufacturerTarget() { 
          if (this.manufacturerTarget == null)
            this.manufacturerTarget = new ArrayList<Organization>();
          return this.manufacturerTarget;
        }

        /**
         * @deprecated Use Reference#setResource(IBaseResource) instead
         */
        @Deprecated
        public Organization addManufacturerTarget() { 
          Organization r = new Organization();
          if (this.manufacturerTarget == null)
            this.manufacturerTarget = new ArrayList<Organization>();
          this.manufacturerTarget.add(r);
          return r;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("identifier", "Identifier", "Including possibly Data Carrier Identifier.", 0, java.lang.Integer.MAX_VALUE, identifier));
          children.add(new Property("type", "CodeableConcept", "The physical type of the container of the medicine.", 0, 1, type));
          children.add(new Property("quantity", "Quantity", "The quantity of this package in the medicinal product, at the current level of packaging. The outermost is always 1.", 0, 1, quantity));
          children.add(new Property("material", "CodeableConcept", "Material type of the package item.", 0, java.lang.Integer.MAX_VALUE, material));
          children.add(new Property("alternateMaterial", "CodeableConcept", "A possible alternate material for the packaging.", 0, java.lang.Integer.MAX_VALUE, alternateMaterial));
          children.add(new Property("device", "Reference(DeviceDefinition)", "A device accompanying a medicinal product.", 0, java.lang.Integer.MAX_VALUE, device));
          children.add(new Property("manufacturedItem", "Reference(MedicinalProductManufactured)", "The manufactured item as contained in the packaged medicinal product.", 0, java.lang.Integer.MAX_VALUE, manufacturedItem));
          children.add(new Property("packageItem", "@MedicinalProductPackaged.packageItem", "Allows containers within containers.", 0, java.lang.Integer.MAX_VALUE, packageItem));
          children.add(new Property("physicalCharacteristics", "ProdCharacteristic", "Dimensions, color etc.", 0, 1, physicalCharacteristics));
          children.add(new Property("otherCharacteristics", "CodeableConcept", "Other codeable characteristics.", 0, java.lang.Integer.MAX_VALUE, otherCharacteristics));
          children.add(new Property("shelfLifeStorage", "ProductShelfLife", "Shelf Life and storage information.", 0, java.lang.Integer.MAX_VALUE, shelfLifeStorage));
          children.add(new Property("manufacturer", "Reference(Organization)", "Manufacturer of this Package Item.", 0, java.lang.Integer.MAX_VALUE, manufacturer));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -1618432855: /*identifier*/  return new Property("identifier", "Identifier", "Including possibly Data Carrier Identifier.", 0, java.lang.Integer.MAX_VALUE, identifier);
          case 3575610: /*type*/  return new Property("type", "CodeableConcept", "The physical type of the container of the medicine.", 0, 1, type);
          case -1285004149: /*quantity*/  return new Property("quantity", "Quantity", "The quantity of this package in the medicinal product, at the current level of packaging. The outermost is always 1.", 0, 1, quantity);
          case 299066663: /*material*/  return new Property("material", "CodeableConcept", "Material type of the package item.", 0, java.lang.Integer.MAX_VALUE, material);
          case -1021448255: /*alternateMaterial*/  return new Property("alternateMaterial", "CodeableConcept", "A possible alternate material for the packaging.", 0, java.lang.Integer.MAX_VALUE, alternateMaterial);
          case -1335157162: /*device*/  return new Property("device", "Reference(DeviceDefinition)", "A device accompanying a medicinal product.", 0, java.lang.Integer.MAX_VALUE, device);
          case 62093686: /*manufacturedItem*/  return new Property("manufacturedItem", "Reference(MedicinalProductManufactured)", "The manufactured item as contained in the packaged medicinal product.", 0, java.lang.Integer.MAX_VALUE, manufacturedItem);
          case 908628089: /*packageItem*/  return new Property("packageItem", "@MedicinalProductPackaged.packageItem", "Allows containers within containers.", 0, java.lang.Integer.MAX_VALUE, packageItem);
          case -1599676319: /*physicalCharacteristics*/  return new Property("physicalCharacteristics", "ProdCharacteristic", "Dimensions, color etc.", 0, 1, physicalCharacteristics);
          case 722135304: /*otherCharacteristics*/  return new Property("otherCharacteristics", "CodeableConcept", "Other codeable characteristics.", 0, java.lang.Integer.MAX_VALUE, otherCharacteristics);
          case 172049237: /*shelfLifeStorage*/  return new Property("shelfLifeStorage", "ProductShelfLife", "Shelf Life and storage information.", 0, java.lang.Integer.MAX_VALUE, shelfLifeStorage);
          case -1969347631: /*manufacturer*/  return new Property("manufacturer", "Reference(Organization)", "Manufacturer of this Package Item.", 0, java.lang.Integer.MAX_VALUE, manufacturer);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case -1285004149: /*quantity*/ return this.quantity == null ? new Base[0] : new Base[] {this.quantity}; // Quantity
        case 299066663: /*material*/ return this.material == null ? new Base[0] : this.material.toArray(new Base[this.material.size()]); // CodeableConcept
        case -1021448255: /*alternateMaterial*/ return this.alternateMaterial == null ? new Base[0] : this.alternateMaterial.toArray(new Base[this.alternateMaterial.size()]); // CodeableConcept
        case -1335157162: /*device*/ return this.device == null ? new Base[0] : this.device.toArray(new Base[this.device.size()]); // Reference
        case 62093686: /*manufacturedItem*/ return this.manufacturedItem == null ? new Base[0] : this.manufacturedItem.toArray(new Base[this.manufacturedItem.size()]); // Reference
        case 908628089: /*packageItem*/ return this.packageItem == null ? new Base[0] : this.packageItem.toArray(new Base[this.packageItem.size()]); // MedicinalProductPackagedPackageItemComponent
        case -1599676319: /*physicalCharacteristics*/ return this.physicalCharacteristics == null ? new Base[0] : new Base[] {this.physicalCharacteristics}; // ProdCharacteristic
        case 722135304: /*otherCharacteristics*/ return this.otherCharacteristics == null ? new Base[0] : this.otherCharacteristics.toArray(new Base[this.otherCharacteristics.size()]); // CodeableConcept
        case 172049237: /*shelfLifeStorage*/ return this.shelfLifeStorage == null ? new Base[0] : this.shelfLifeStorage.toArray(new Base[this.shelfLifeStorage.size()]); // ProductShelfLife
        case -1969347631: /*manufacturer*/ return this.manufacturer == null ? new Base[0] : this.manufacturer.toArray(new Base[this.manufacturer.size()]); // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.getIdentifier().add(castToIdentifier(value)); // Identifier
          return value;
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1285004149: // quantity
          this.quantity = castToQuantity(value); // Quantity
          return value;
        case 299066663: // material
          this.getMaterial().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -1021448255: // alternateMaterial
          this.getAlternateMaterial().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -1335157162: // device
          this.getDevice().add(castToReference(value)); // Reference
          return value;
        case 62093686: // manufacturedItem
          this.getManufacturedItem().add(castToReference(value)); // Reference
          return value;
        case 908628089: // packageItem
          this.getPackageItem().add((MedicinalProductPackagedPackageItemComponent) value); // MedicinalProductPackagedPackageItemComponent
          return value;
        case -1599676319: // physicalCharacteristics
          this.physicalCharacteristics = castToProdCharacteristic(value); // ProdCharacteristic
          return value;
        case 722135304: // otherCharacteristics
          this.getOtherCharacteristics().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 172049237: // shelfLifeStorage
          this.getShelfLifeStorage().add(castToProductShelfLife(value)); // ProductShelfLife
          return value;
        case -1969347631: // manufacturer
          this.getManufacturer().add(castToReference(value)); // Reference
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier")) {
          this.getIdentifier().add(castToIdentifier(value));
        } else if (name.equals("type")) {
          this.type = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("quantity")) {
          this.quantity = castToQuantity(value); // Quantity
        } else if (name.equals("material")) {
          this.getMaterial().add(castToCodeableConcept(value));
        } else if (name.equals("alternateMaterial")) {
          this.getAlternateMaterial().add(castToCodeableConcept(value));
        } else if (name.equals("device")) {
          this.getDevice().add(castToReference(value));
        } else if (name.equals("manufacturedItem")) {
          this.getManufacturedItem().add(castToReference(value));
        } else if (name.equals("packageItem")) {
          this.getPackageItem().add((MedicinalProductPackagedPackageItemComponent) value);
        } else if (name.equals("physicalCharacteristics")) {
          this.physicalCharacteristics = castToProdCharacteristic(value); // ProdCharacteristic
        } else if (name.equals("otherCharacteristics")) {
          this.getOtherCharacteristics().add(castToCodeableConcept(value));
        } else if (name.equals("shelfLifeStorage")) {
          this.getShelfLifeStorage().add(castToProductShelfLife(value));
        } else if (name.equals("manufacturer")) {
          this.getManufacturer().add(castToReference(value));
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return addIdentifier(); 
        case 3575610:  return getType(); 
        case -1285004149:  return getQuantity(); 
        case 299066663:  return addMaterial(); 
        case -1021448255:  return addAlternateMaterial(); 
        case -1335157162:  return addDevice(); 
        case 62093686:  return addManufacturedItem(); 
        case 908628089:  return addPackageItem(); 
        case -1599676319:  return getPhysicalCharacteristics(); 
        case 722135304:  return addOtherCharacteristics(); 
        case 172049237:  return addShelfLifeStorage(); 
        case -1969347631:  return addManufacturer(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case -1285004149: /*quantity*/ return new String[] {"Quantity"};
        case 299066663: /*material*/ return new String[] {"CodeableConcept"};
        case -1021448255: /*alternateMaterial*/ return new String[] {"CodeableConcept"};
        case -1335157162: /*device*/ return new String[] {"Reference"};
        case 62093686: /*manufacturedItem*/ return new String[] {"Reference"};
        case 908628089: /*packageItem*/ return new String[] {"@MedicinalProductPackaged.packageItem"};
        case -1599676319: /*physicalCharacteristics*/ return new String[] {"ProdCharacteristic"};
        case 722135304: /*otherCharacteristics*/ return new String[] {"CodeableConcept"};
        case 172049237: /*shelfLifeStorage*/ return new String[] {"ProductShelfLife"};
        case -1969347631: /*manufacturer*/ return new String[] {"Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("quantity")) {
          this.quantity = new Quantity();
          return this.quantity;
        }
        else if (name.equals("material")) {
          return addMaterial();
        }
        else if (name.equals("alternateMaterial")) {
          return addAlternateMaterial();
        }
        else if (name.equals("device")) {
          return addDevice();
        }
        else if (name.equals("manufacturedItem")) {
          return addManufacturedItem();
        }
        else if (name.equals("packageItem")) {
          return addPackageItem();
        }
        else if (name.equals("physicalCharacteristics")) {
          this.physicalCharacteristics = new ProdCharacteristic();
          return this.physicalCharacteristics;
        }
        else if (name.equals("otherCharacteristics")) {
          return addOtherCharacteristics();
        }
        else if (name.equals("shelfLifeStorage")) {
          return addShelfLifeStorage();
        }
        else if (name.equals("manufacturer")) {
          return addManufacturer();
        }
        else
          return super.addChild(name);
      }

      public MedicinalProductPackagedPackageItemComponent copy() {
        MedicinalProductPackagedPackageItemComponent dst = new MedicinalProductPackagedPackageItemComponent();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.type = type == null ? null : type.copy();
        dst.quantity = quantity == null ? null : quantity.copy();
        if (material != null) {
          dst.material = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : material)
            dst.material.add(i.copy());
        };
        if (alternateMaterial != null) {
          dst.alternateMaterial = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : alternateMaterial)
            dst.alternateMaterial.add(i.copy());
        };
        if (device != null) {
          dst.device = new ArrayList<Reference>();
          for (Reference i : device)
            dst.device.add(i.copy());
        };
        if (manufacturedItem != null) {
          dst.manufacturedItem = new ArrayList<Reference>();
          for (Reference i : manufacturedItem)
            dst.manufacturedItem.add(i.copy());
        };
        if (packageItem != null) {
          dst.packageItem = new ArrayList<MedicinalProductPackagedPackageItemComponent>();
          for (MedicinalProductPackagedPackageItemComponent i : packageItem)
            dst.packageItem.add(i.copy());
        };
        dst.physicalCharacteristics = physicalCharacteristics == null ? null : physicalCharacteristics.copy();
        if (otherCharacteristics != null) {
          dst.otherCharacteristics = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : otherCharacteristics)
            dst.otherCharacteristics.add(i.copy());
        };
        if (shelfLifeStorage != null) {
          dst.shelfLifeStorage = new ArrayList<ProductShelfLife>();
          for (ProductShelfLife i : shelfLifeStorage)
            dst.shelfLifeStorage.add(i.copy());
        };
        if (manufacturer != null) {
          dst.manufacturer = new ArrayList<Reference>();
          for (Reference i : manufacturer)
            dst.manufacturer.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof MedicinalProductPackagedPackageItemComponent))
          return false;
        MedicinalProductPackagedPackageItemComponent o = (MedicinalProductPackagedPackageItemComponent) other_;
        return compareDeep(identifier, o.identifier, true) && compareDeep(type, o.type, true) && compareDeep(quantity, o.quantity, true)
           && compareDeep(material, o.material, true) && compareDeep(alternateMaterial, o.alternateMaterial, true)
           && compareDeep(device, o.device, true) && compareDeep(manufacturedItem, o.manufacturedItem, true)
           && compareDeep(packageItem, o.packageItem, true) && compareDeep(physicalCharacteristics, o.physicalCharacteristics, true)
           && compareDeep(otherCharacteristics, o.otherCharacteristics, true) && compareDeep(shelfLifeStorage, o.shelfLifeStorage, true)
           && compareDeep(manufacturer, o.manufacturer, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof MedicinalProductPackagedPackageItemComponent))
          return false;
        MedicinalProductPackagedPackageItemComponent o = (MedicinalProductPackagedPackageItemComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, type, quantity
          , material, alternateMaterial, device, manufacturedItem, packageItem, physicalCharacteristics
          , otherCharacteristics, shelfLifeStorage, manufacturer);
      }

  public String fhirType() {
    return "MedicinalProductPackaged.packageItem";

  }

  }

    /**
     * Unique identifier.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Unique identifier", formalDefinition="Unique identifier." )
    protected List<Identifier> identifier;

    /**
     * The product with this is a pack for.
     */
    @Child(name = "subject", type = {MedicinalProduct.class}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="The product with this is a pack for", formalDefinition="The product with this is a pack for." )
    protected List<Reference> subject;
    /**
     * The actual objects that are the target of the reference (The product with this is a pack for.)
     */
    protected List<MedicinalProduct> subjectTarget;


    /**
     * Textual description.
     */
    @Child(name = "description", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Textual description", formalDefinition="Textual description." )
    protected StringType description;

    /**
     * The legal status of supply of the medicinal product as classified by the regulator.
     */
    @Child(name = "legalStatusOfSupply", type = {CodeableConcept.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The legal status of supply of the medicinal product as classified by the regulator", formalDefinition="The legal status of supply of the medicinal product as classified by the regulator." )
    protected CodeableConcept legalStatusOfSupply;

    /**
     * Marketing information.
     */
    @Child(name = "marketingStatus", type = {MarketingStatus.class}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Marketing information", formalDefinition="Marketing information." )
    protected List<MarketingStatus> marketingStatus;

    /**
     * Manufacturer of this Package Item.
     */
    @Child(name = "marketingAuthorization", type = {MedicinalProductAuthorization.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Manufacturer of this Package Item", formalDefinition="Manufacturer of this Package Item." )
    protected Reference marketingAuthorization;

    /**
     * The actual object that is the target of the reference (Manufacturer of this Package Item.)
     */
    protected MedicinalProductAuthorization marketingAuthorizationTarget;

    /**
     * Manufacturer of this Package Item.
     */
    @Child(name = "manufacturer", type = {Organization.class}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Manufacturer of this Package Item", formalDefinition="Manufacturer of this Package Item." )
    protected List<Reference> manufacturer;
    /**
     * The actual objects that are the target of the reference (Manufacturer of this Package Item.)
     */
    protected List<Organization> manufacturerTarget;


    /**
     * Batch numbering.
     */
    @Child(name = "batchIdentifier", type = {}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Batch numbering", formalDefinition="Batch numbering." )
    protected List<MedicinalProductPackagedBatchIdentifierComponent> batchIdentifier;

    /**
     * A packaging item, as a contained for medicine, possibly with other packaging items within.
     */
    @Child(name = "packageItem", type = {}, order=8, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="A packaging item, as a contained for medicine, possibly with other packaging items within", formalDefinition="A packaging item, as a contained for medicine, possibly with other packaging items within." )
    protected List<MedicinalProductPackagedPackageItemComponent> packageItem;

    private static final long serialVersionUID = -1530863773L;

  /**
   * Constructor
   */
    public MedicinalProductPackaged() {
      super();
    }

    /**
     * @return {@link #identifier} (Unique identifier.)
     */
    public List<Identifier> getIdentifier() { 
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      return this.identifier;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicinalProductPackaged setIdentifier(List<Identifier> theIdentifier) { 
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

    public MedicinalProductPackaged addIdentifier(Identifier t) { //3
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
     * @return {@link #subject} (The product with this is a pack for.)
     */
    public List<Reference> getSubject() { 
      if (this.subject == null)
        this.subject = new ArrayList<Reference>();
      return this.subject;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicinalProductPackaged setSubject(List<Reference> theSubject) { 
      this.subject = theSubject;
      return this;
    }

    public boolean hasSubject() { 
      if (this.subject == null)
        return false;
      for (Reference item : this.subject)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addSubject() { //3
      Reference t = new Reference();
      if (this.subject == null)
        this.subject = new ArrayList<Reference>();
      this.subject.add(t);
      return t;
    }

    public MedicinalProductPackaged addSubject(Reference t) { //3
      if (t == null)
        return this;
      if (this.subject == null)
        this.subject = new ArrayList<Reference>();
      this.subject.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #subject}, creating it if it does not already exist
     */
    public Reference getSubjectFirstRep() { 
      if (getSubject().isEmpty()) {
        addSubject();
      }
      return getSubject().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<MedicinalProduct> getSubjectTarget() { 
      if (this.subjectTarget == null)
        this.subjectTarget = new ArrayList<MedicinalProduct>();
      return this.subjectTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public MedicinalProduct addSubjectTarget() { 
      MedicinalProduct r = new MedicinalProduct();
      if (this.subjectTarget == null)
        this.subjectTarget = new ArrayList<MedicinalProduct>();
      this.subjectTarget.add(r);
      return r;
    }

    /**
     * @return {@link #description} (Textual description.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public StringType getDescriptionElement() { 
      if (this.description == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicinalProductPackaged.description");
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
     * @param value {@link #description} (Textual description.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public MedicinalProductPackaged setDescriptionElement(StringType value) { 
      this.description = value;
      return this;
    }

    /**
     * @return Textual description.
     */
    public String getDescription() { 
      return this.description == null ? null : this.description.getValue();
    }

    /**
     * @param value Textual description.
     */
    public MedicinalProductPackaged setDescription(String value) { 
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
     * @return {@link #legalStatusOfSupply} (The legal status of supply of the medicinal product as classified by the regulator.)
     */
    public CodeableConcept getLegalStatusOfSupply() { 
      if (this.legalStatusOfSupply == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicinalProductPackaged.legalStatusOfSupply");
        else if (Configuration.doAutoCreate())
          this.legalStatusOfSupply = new CodeableConcept(); // cc
      return this.legalStatusOfSupply;
    }

    public boolean hasLegalStatusOfSupply() { 
      return this.legalStatusOfSupply != null && !this.legalStatusOfSupply.isEmpty();
    }

    /**
     * @param value {@link #legalStatusOfSupply} (The legal status of supply of the medicinal product as classified by the regulator.)
     */
    public MedicinalProductPackaged setLegalStatusOfSupply(CodeableConcept value) { 
      this.legalStatusOfSupply = value;
      return this;
    }

    /**
     * @return {@link #marketingStatus} (Marketing information.)
     */
    public List<MarketingStatus> getMarketingStatus() { 
      if (this.marketingStatus == null)
        this.marketingStatus = new ArrayList<MarketingStatus>();
      return this.marketingStatus;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicinalProductPackaged setMarketingStatus(List<MarketingStatus> theMarketingStatus) { 
      this.marketingStatus = theMarketingStatus;
      return this;
    }

    public boolean hasMarketingStatus() { 
      if (this.marketingStatus == null)
        return false;
      for (MarketingStatus item : this.marketingStatus)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public MarketingStatus addMarketingStatus() { //3
      MarketingStatus t = new MarketingStatus();
      if (this.marketingStatus == null)
        this.marketingStatus = new ArrayList<MarketingStatus>();
      this.marketingStatus.add(t);
      return t;
    }

    public MedicinalProductPackaged addMarketingStatus(MarketingStatus t) { //3
      if (t == null)
        return this;
      if (this.marketingStatus == null)
        this.marketingStatus = new ArrayList<MarketingStatus>();
      this.marketingStatus.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #marketingStatus}, creating it if it does not already exist
     */
    public MarketingStatus getMarketingStatusFirstRep() { 
      if (getMarketingStatus().isEmpty()) {
        addMarketingStatus();
      }
      return getMarketingStatus().get(0);
    }

    /**
     * @return {@link #marketingAuthorization} (Manufacturer of this Package Item.)
     */
    public Reference getMarketingAuthorization() { 
      if (this.marketingAuthorization == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicinalProductPackaged.marketingAuthorization");
        else if (Configuration.doAutoCreate())
          this.marketingAuthorization = new Reference(); // cc
      return this.marketingAuthorization;
    }

    public boolean hasMarketingAuthorization() { 
      return this.marketingAuthorization != null && !this.marketingAuthorization.isEmpty();
    }

    /**
     * @param value {@link #marketingAuthorization} (Manufacturer of this Package Item.)
     */
    public MedicinalProductPackaged setMarketingAuthorization(Reference value) { 
      this.marketingAuthorization = value;
      return this;
    }

    /**
     * @return {@link #marketingAuthorization} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Manufacturer of this Package Item.)
     */
    public MedicinalProductAuthorization getMarketingAuthorizationTarget() { 
      if (this.marketingAuthorizationTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicinalProductPackaged.marketingAuthorization");
        else if (Configuration.doAutoCreate())
          this.marketingAuthorizationTarget = new MedicinalProductAuthorization(); // aa
      return this.marketingAuthorizationTarget;
    }

    /**
     * @param value {@link #marketingAuthorization} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Manufacturer of this Package Item.)
     */
    public MedicinalProductPackaged setMarketingAuthorizationTarget(MedicinalProductAuthorization value) { 
      this.marketingAuthorizationTarget = value;
      return this;
    }

    /**
     * @return {@link #manufacturer} (Manufacturer of this Package Item.)
     */
    public List<Reference> getManufacturer() { 
      if (this.manufacturer == null)
        this.manufacturer = new ArrayList<Reference>();
      return this.manufacturer;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicinalProductPackaged setManufacturer(List<Reference> theManufacturer) { 
      this.manufacturer = theManufacturer;
      return this;
    }

    public boolean hasManufacturer() { 
      if (this.manufacturer == null)
        return false;
      for (Reference item : this.manufacturer)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addManufacturer() { //3
      Reference t = new Reference();
      if (this.manufacturer == null)
        this.manufacturer = new ArrayList<Reference>();
      this.manufacturer.add(t);
      return t;
    }

    public MedicinalProductPackaged addManufacturer(Reference t) { //3
      if (t == null)
        return this;
      if (this.manufacturer == null)
        this.manufacturer = new ArrayList<Reference>();
      this.manufacturer.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #manufacturer}, creating it if it does not already exist
     */
    public Reference getManufacturerFirstRep() { 
      if (getManufacturer().isEmpty()) {
        addManufacturer();
      }
      return getManufacturer().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Organization> getManufacturerTarget() { 
      if (this.manufacturerTarget == null)
        this.manufacturerTarget = new ArrayList<Organization>();
      return this.manufacturerTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public Organization addManufacturerTarget() { 
      Organization r = new Organization();
      if (this.manufacturerTarget == null)
        this.manufacturerTarget = new ArrayList<Organization>();
      this.manufacturerTarget.add(r);
      return r;
    }

    /**
     * @return {@link #batchIdentifier} (Batch numbering.)
     */
    public List<MedicinalProductPackagedBatchIdentifierComponent> getBatchIdentifier() { 
      if (this.batchIdentifier == null)
        this.batchIdentifier = new ArrayList<MedicinalProductPackagedBatchIdentifierComponent>();
      return this.batchIdentifier;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicinalProductPackaged setBatchIdentifier(List<MedicinalProductPackagedBatchIdentifierComponent> theBatchIdentifier) { 
      this.batchIdentifier = theBatchIdentifier;
      return this;
    }

    public boolean hasBatchIdentifier() { 
      if (this.batchIdentifier == null)
        return false;
      for (MedicinalProductPackagedBatchIdentifierComponent item : this.batchIdentifier)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public MedicinalProductPackagedBatchIdentifierComponent addBatchIdentifier() { //3
      MedicinalProductPackagedBatchIdentifierComponent t = new MedicinalProductPackagedBatchIdentifierComponent();
      if (this.batchIdentifier == null)
        this.batchIdentifier = new ArrayList<MedicinalProductPackagedBatchIdentifierComponent>();
      this.batchIdentifier.add(t);
      return t;
    }

    public MedicinalProductPackaged addBatchIdentifier(MedicinalProductPackagedBatchIdentifierComponent t) { //3
      if (t == null)
        return this;
      if (this.batchIdentifier == null)
        this.batchIdentifier = new ArrayList<MedicinalProductPackagedBatchIdentifierComponent>();
      this.batchIdentifier.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #batchIdentifier}, creating it if it does not already exist
     */
    public MedicinalProductPackagedBatchIdentifierComponent getBatchIdentifierFirstRep() { 
      if (getBatchIdentifier().isEmpty()) {
        addBatchIdentifier();
      }
      return getBatchIdentifier().get(0);
    }

    /**
     * @return {@link #packageItem} (A packaging item, as a contained for medicine, possibly with other packaging items within.)
     */
    public List<MedicinalProductPackagedPackageItemComponent> getPackageItem() { 
      if (this.packageItem == null)
        this.packageItem = new ArrayList<MedicinalProductPackagedPackageItemComponent>();
      return this.packageItem;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicinalProductPackaged setPackageItem(List<MedicinalProductPackagedPackageItemComponent> thePackageItem) { 
      this.packageItem = thePackageItem;
      return this;
    }

    public boolean hasPackageItem() { 
      if (this.packageItem == null)
        return false;
      for (MedicinalProductPackagedPackageItemComponent item : this.packageItem)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public MedicinalProductPackagedPackageItemComponent addPackageItem() { //3
      MedicinalProductPackagedPackageItemComponent t = new MedicinalProductPackagedPackageItemComponent();
      if (this.packageItem == null)
        this.packageItem = new ArrayList<MedicinalProductPackagedPackageItemComponent>();
      this.packageItem.add(t);
      return t;
    }

    public MedicinalProductPackaged addPackageItem(MedicinalProductPackagedPackageItemComponent t) { //3
      if (t == null)
        return this;
      if (this.packageItem == null)
        this.packageItem = new ArrayList<MedicinalProductPackagedPackageItemComponent>();
      this.packageItem.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #packageItem}, creating it if it does not already exist
     */
    public MedicinalProductPackagedPackageItemComponent getPackageItemFirstRep() { 
      if (getPackageItem().isEmpty()) {
        addPackageItem();
      }
      return getPackageItem().get(0);
    }

      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("identifier", "Identifier", "Unique identifier.", 0, java.lang.Integer.MAX_VALUE, identifier));
        children.add(new Property("subject", "Reference(MedicinalProduct)", "The product with this is a pack for.", 0, java.lang.Integer.MAX_VALUE, subject));
        children.add(new Property("description", "string", "Textual description.", 0, 1, description));
        children.add(new Property("legalStatusOfSupply", "CodeableConcept", "The legal status of supply of the medicinal product as classified by the regulator.", 0, 1, legalStatusOfSupply));
        children.add(new Property("marketingStatus", "MarketingStatus", "Marketing information.", 0, java.lang.Integer.MAX_VALUE, marketingStatus));
        children.add(new Property("marketingAuthorization", "Reference(MedicinalProductAuthorization)", "Manufacturer of this Package Item.", 0, 1, marketingAuthorization));
        children.add(new Property("manufacturer", "Reference(Organization)", "Manufacturer of this Package Item.", 0, java.lang.Integer.MAX_VALUE, manufacturer));
        children.add(new Property("batchIdentifier", "", "Batch numbering.", 0, java.lang.Integer.MAX_VALUE, batchIdentifier));
        children.add(new Property("packageItem", "", "A packaging item, as a contained for medicine, possibly with other packaging items within.", 0, java.lang.Integer.MAX_VALUE, packageItem));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case -1618432855: /*identifier*/  return new Property("identifier", "Identifier", "Unique identifier.", 0, java.lang.Integer.MAX_VALUE, identifier);
        case -1867885268: /*subject*/  return new Property("subject", "Reference(MedicinalProduct)", "The product with this is a pack for.", 0, java.lang.Integer.MAX_VALUE, subject);
        case -1724546052: /*description*/  return new Property("description", "string", "Textual description.", 0, 1, description);
        case -844874031: /*legalStatusOfSupply*/  return new Property("legalStatusOfSupply", "CodeableConcept", "The legal status of supply of the medicinal product as classified by the regulator.", 0, 1, legalStatusOfSupply);
        case 70767032: /*marketingStatus*/  return new Property("marketingStatus", "MarketingStatus", "Marketing information.", 0, java.lang.Integer.MAX_VALUE, marketingStatus);
        case 571831283: /*marketingAuthorization*/  return new Property("marketingAuthorization", "Reference(MedicinalProductAuthorization)", "Manufacturer of this Package Item.", 0, 1, marketingAuthorization);
        case -1969347631: /*manufacturer*/  return new Property("manufacturer", "Reference(Organization)", "Manufacturer of this Package Item.", 0, java.lang.Integer.MAX_VALUE, manufacturer);
        case -1688395901: /*batchIdentifier*/  return new Property("batchIdentifier", "", "Batch numbering.", 0, java.lang.Integer.MAX_VALUE, batchIdentifier);
        case 908628089: /*packageItem*/  return new Property("packageItem", "", "A packaging item, as a contained for medicine, possibly with other packaging items within.", 0, java.lang.Integer.MAX_VALUE, packageItem);
        default: return super.getNamedProperty(_hash, _name, _checkValid);
        }

      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case -1867885268: /*subject*/ return this.subject == null ? new Base[0] : this.subject.toArray(new Base[this.subject.size()]); // Reference
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // StringType
        case -844874031: /*legalStatusOfSupply*/ return this.legalStatusOfSupply == null ? new Base[0] : new Base[] {this.legalStatusOfSupply}; // CodeableConcept
        case 70767032: /*marketingStatus*/ return this.marketingStatus == null ? new Base[0] : this.marketingStatus.toArray(new Base[this.marketingStatus.size()]); // MarketingStatus
        case 571831283: /*marketingAuthorization*/ return this.marketingAuthorization == null ? new Base[0] : new Base[] {this.marketingAuthorization}; // Reference
        case -1969347631: /*manufacturer*/ return this.manufacturer == null ? new Base[0] : this.manufacturer.toArray(new Base[this.manufacturer.size()]); // Reference
        case -1688395901: /*batchIdentifier*/ return this.batchIdentifier == null ? new Base[0] : this.batchIdentifier.toArray(new Base[this.batchIdentifier.size()]); // MedicinalProductPackagedBatchIdentifierComponent
        case 908628089: /*packageItem*/ return this.packageItem == null ? new Base[0] : this.packageItem.toArray(new Base[this.packageItem.size()]); // MedicinalProductPackagedPackageItemComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.getIdentifier().add(castToIdentifier(value)); // Identifier
          return value;
        case -1867885268: // subject
          this.getSubject().add(castToReference(value)); // Reference
          return value;
        case -1724546052: // description
          this.description = castToString(value); // StringType
          return value;
        case -844874031: // legalStatusOfSupply
          this.legalStatusOfSupply = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 70767032: // marketingStatus
          this.getMarketingStatus().add(castToMarketingStatus(value)); // MarketingStatus
          return value;
        case 571831283: // marketingAuthorization
          this.marketingAuthorization = castToReference(value); // Reference
          return value;
        case -1969347631: // manufacturer
          this.getManufacturer().add(castToReference(value)); // Reference
          return value;
        case -1688395901: // batchIdentifier
          this.getBatchIdentifier().add((MedicinalProductPackagedBatchIdentifierComponent) value); // MedicinalProductPackagedBatchIdentifierComponent
          return value;
        case 908628089: // packageItem
          this.getPackageItem().add((MedicinalProductPackagedPackageItemComponent) value); // MedicinalProductPackagedPackageItemComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier")) {
          this.getIdentifier().add(castToIdentifier(value));
        } else if (name.equals("subject")) {
          this.getSubject().add(castToReference(value));
        } else if (name.equals("description")) {
          this.description = castToString(value); // StringType
        } else if (name.equals("legalStatusOfSupply")) {
          this.legalStatusOfSupply = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("marketingStatus")) {
          this.getMarketingStatus().add(castToMarketingStatus(value));
        } else if (name.equals("marketingAuthorization")) {
          this.marketingAuthorization = castToReference(value); // Reference
        } else if (name.equals("manufacturer")) {
          this.getManufacturer().add(castToReference(value));
        } else if (name.equals("batchIdentifier")) {
          this.getBatchIdentifier().add((MedicinalProductPackagedBatchIdentifierComponent) value);
        } else if (name.equals("packageItem")) {
          this.getPackageItem().add((MedicinalProductPackagedPackageItemComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return addIdentifier(); 
        case -1867885268:  return addSubject(); 
        case -1724546052:  return getDescriptionElement();
        case -844874031:  return getLegalStatusOfSupply(); 
        case 70767032:  return addMarketingStatus(); 
        case 571831283:  return getMarketingAuthorization(); 
        case -1969347631:  return addManufacturer(); 
        case -1688395901:  return addBatchIdentifier(); 
        case 908628089:  return addPackageItem(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case -1867885268: /*subject*/ return new String[] {"Reference"};
        case -1724546052: /*description*/ return new String[] {"string"};
        case -844874031: /*legalStatusOfSupply*/ return new String[] {"CodeableConcept"};
        case 70767032: /*marketingStatus*/ return new String[] {"MarketingStatus"};
        case 571831283: /*marketingAuthorization*/ return new String[] {"Reference"};
        case -1969347631: /*manufacturer*/ return new String[] {"Reference"};
        case -1688395901: /*batchIdentifier*/ return new String[] {};
        case 908628089: /*packageItem*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("subject")) {
          return addSubject();
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type MedicinalProductPackaged.description");
        }
        else if (name.equals("legalStatusOfSupply")) {
          this.legalStatusOfSupply = new CodeableConcept();
          return this.legalStatusOfSupply;
        }
        else if (name.equals("marketingStatus")) {
          return addMarketingStatus();
        }
        else if (name.equals("marketingAuthorization")) {
          this.marketingAuthorization = new Reference();
          return this.marketingAuthorization;
        }
        else if (name.equals("manufacturer")) {
          return addManufacturer();
        }
        else if (name.equals("batchIdentifier")) {
          return addBatchIdentifier();
        }
        else if (name.equals("packageItem")) {
          return addPackageItem();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "MedicinalProductPackaged";

  }

      public MedicinalProductPackaged copy() {
        MedicinalProductPackaged dst = new MedicinalProductPackaged();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        if (subject != null) {
          dst.subject = new ArrayList<Reference>();
          for (Reference i : subject)
            dst.subject.add(i.copy());
        };
        dst.description = description == null ? null : description.copy();
        dst.legalStatusOfSupply = legalStatusOfSupply == null ? null : legalStatusOfSupply.copy();
        if (marketingStatus != null) {
          dst.marketingStatus = new ArrayList<MarketingStatus>();
          for (MarketingStatus i : marketingStatus)
            dst.marketingStatus.add(i.copy());
        };
        dst.marketingAuthorization = marketingAuthorization == null ? null : marketingAuthorization.copy();
        if (manufacturer != null) {
          dst.manufacturer = new ArrayList<Reference>();
          for (Reference i : manufacturer)
            dst.manufacturer.add(i.copy());
        };
        if (batchIdentifier != null) {
          dst.batchIdentifier = new ArrayList<MedicinalProductPackagedBatchIdentifierComponent>();
          for (MedicinalProductPackagedBatchIdentifierComponent i : batchIdentifier)
            dst.batchIdentifier.add(i.copy());
        };
        if (packageItem != null) {
          dst.packageItem = new ArrayList<MedicinalProductPackagedPackageItemComponent>();
          for (MedicinalProductPackagedPackageItemComponent i : packageItem)
            dst.packageItem.add(i.copy());
        };
        return dst;
      }

      protected MedicinalProductPackaged typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof MedicinalProductPackaged))
          return false;
        MedicinalProductPackaged o = (MedicinalProductPackaged) other_;
        return compareDeep(identifier, o.identifier, true) && compareDeep(subject, o.subject, true) && compareDeep(description, o.description, true)
           && compareDeep(legalStatusOfSupply, o.legalStatusOfSupply, true) && compareDeep(marketingStatus, o.marketingStatus, true)
           && compareDeep(marketingAuthorization, o.marketingAuthorization, true) && compareDeep(manufacturer, o.manufacturer, true)
           && compareDeep(batchIdentifier, o.batchIdentifier, true) && compareDeep(packageItem, o.packageItem, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof MedicinalProductPackaged))
          return false;
        MedicinalProductPackaged o = (MedicinalProductPackaged) other_;
        return compareValues(description, o.description, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, subject, description
          , legalStatusOfSupply, marketingStatus, marketingAuthorization, manufacturer, batchIdentifier
          , packageItem);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.MedicinalProductPackaged;
   }

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>Unique identifier</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MedicinalProductPackaged.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="MedicinalProductPackaged.identifier", description="Unique identifier", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>Unique identifier</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MedicinalProductPackaged.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>subject</b>
   * <p>
   * Description: <b>The product with this is a pack for</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MedicinalProductPackaged.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="subject", path="MedicinalProductPackaged.subject", description="The product with this is a pack for", type="reference", target={MedicinalProduct.class } )
  public static final String SP_SUBJECT = "subject";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>subject</b>
   * <p>
   * Description: <b>The product with this is a pack for</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MedicinalProductPackaged.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SUBJECT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SUBJECT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>MedicinalProductPackaged:subject</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SUBJECT = new ca.uhn.fhir.model.api.Include("MedicinalProductPackaged:subject").toLocked();


}

