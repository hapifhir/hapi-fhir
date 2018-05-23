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

// Generated on Sun, May 6, 2018 17:51-0400 for FHIR v3.4.0

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
 * A detailed description of a device, typically as part of a regulated medicinal product. It is not intended to relace the Device resource, which covers use of device instances.
 */
@ResourceDef(name="MedicinalProductDeviceSpec", profile="http://hl7.org/fhir/Profile/MedicinalProductDeviceSpec")
public class MedicinalProductDeviceSpec extends DomainResource {

    @Block()
    public static class MedicinalProductDeviceSpecMaterialComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The substance.
         */
        @Child(name = "substance", type = {CodeableConcept.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The substance", formalDefinition="The substance." )
        protected CodeableConcept substance;

        /**
         * Indicates an alternative material of the device.
         */
        @Child(name = "alternate", type = {BooleanType.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Indicates an alternative material of the device", formalDefinition="Indicates an alternative material of the device." )
        protected BooleanType alternate;

        /**
         * Whether the substance is a known or suspected allergen.
         */
        @Child(name = "allergenicIndicator", type = {BooleanType.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Whether the substance is a known or suspected allergen", formalDefinition="Whether the substance is a known or suspected allergen." )
        protected BooleanType allergenicIndicator;

        private static final long serialVersionUID = 1232736508L;

    /**
     * Constructor
     */
      public MedicinalProductDeviceSpecMaterialComponent() {
        super();
      }

    /**
     * Constructor
     */
      public MedicinalProductDeviceSpecMaterialComponent(CodeableConcept substance) {
        super();
        this.substance = substance;
      }

        /**
         * @return {@link #substance} (The substance.)
         */
        public CodeableConcept getSubstance() { 
          if (this.substance == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicinalProductDeviceSpecMaterialComponent.substance");
            else if (Configuration.doAutoCreate())
              this.substance = new CodeableConcept(); // cc
          return this.substance;
        }

        public boolean hasSubstance() { 
          return this.substance != null && !this.substance.isEmpty();
        }

        /**
         * @param value {@link #substance} (The substance.)
         */
        public MedicinalProductDeviceSpecMaterialComponent setSubstance(CodeableConcept value) { 
          this.substance = value;
          return this;
        }

        /**
         * @return {@link #alternate} (Indicates an alternative material of the device.). This is the underlying object with id, value and extensions. The accessor "getAlternate" gives direct access to the value
         */
        public BooleanType getAlternateElement() { 
          if (this.alternate == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicinalProductDeviceSpecMaterialComponent.alternate");
            else if (Configuration.doAutoCreate())
              this.alternate = new BooleanType(); // bb
          return this.alternate;
        }

        public boolean hasAlternateElement() { 
          return this.alternate != null && !this.alternate.isEmpty();
        }

        public boolean hasAlternate() { 
          return this.alternate != null && !this.alternate.isEmpty();
        }

        /**
         * @param value {@link #alternate} (Indicates an alternative material of the device.). This is the underlying object with id, value and extensions. The accessor "getAlternate" gives direct access to the value
         */
        public MedicinalProductDeviceSpecMaterialComponent setAlternateElement(BooleanType value) { 
          this.alternate = value;
          return this;
        }

        /**
         * @return Indicates an alternative material of the device.
         */
        public boolean getAlternate() { 
          return this.alternate == null || this.alternate.isEmpty() ? false : this.alternate.getValue();
        }

        /**
         * @param value Indicates an alternative material of the device.
         */
        public MedicinalProductDeviceSpecMaterialComponent setAlternate(boolean value) { 
            if (this.alternate == null)
              this.alternate = new BooleanType();
            this.alternate.setValue(value);
          return this;
        }

        /**
         * @return {@link #allergenicIndicator} (Whether the substance is a known or suspected allergen.). This is the underlying object with id, value and extensions. The accessor "getAllergenicIndicator" gives direct access to the value
         */
        public BooleanType getAllergenicIndicatorElement() { 
          if (this.allergenicIndicator == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicinalProductDeviceSpecMaterialComponent.allergenicIndicator");
            else if (Configuration.doAutoCreate())
              this.allergenicIndicator = new BooleanType(); // bb
          return this.allergenicIndicator;
        }

        public boolean hasAllergenicIndicatorElement() { 
          return this.allergenicIndicator != null && !this.allergenicIndicator.isEmpty();
        }

        public boolean hasAllergenicIndicator() { 
          return this.allergenicIndicator != null && !this.allergenicIndicator.isEmpty();
        }

        /**
         * @param value {@link #allergenicIndicator} (Whether the substance is a known or suspected allergen.). This is the underlying object with id, value and extensions. The accessor "getAllergenicIndicator" gives direct access to the value
         */
        public MedicinalProductDeviceSpecMaterialComponent setAllergenicIndicatorElement(BooleanType value) { 
          this.allergenicIndicator = value;
          return this;
        }

        /**
         * @return Whether the substance is a known or suspected allergen.
         */
        public boolean getAllergenicIndicator() { 
          return this.allergenicIndicator == null || this.allergenicIndicator.isEmpty() ? false : this.allergenicIndicator.getValue();
        }

        /**
         * @param value Whether the substance is a known or suspected allergen.
         */
        public MedicinalProductDeviceSpecMaterialComponent setAllergenicIndicator(boolean value) { 
            if (this.allergenicIndicator == null)
              this.allergenicIndicator = new BooleanType();
            this.allergenicIndicator.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("substance", "CodeableConcept", "The substance.", 0, 1, substance));
          children.add(new Property("alternate", "boolean", "Indicates an alternative material of the device.", 0, 1, alternate));
          children.add(new Property("allergenicIndicator", "boolean", "Whether the substance is a known or suspected allergen.", 0, 1, allergenicIndicator));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 530040176: /*substance*/  return new Property("substance", "CodeableConcept", "The substance.", 0, 1, substance);
          case -1408024454: /*alternate*/  return new Property("alternate", "boolean", "Indicates an alternative material of the device.", 0, 1, alternate);
          case 75406931: /*allergenicIndicator*/  return new Property("allergenicIndicator", "boolean", "Whether the substance is a known or suspected allergen.", 0, 1, allergenicIndicator);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 530040176: /*substance*/ return this.substance == null ? new Base[0] : new Base[] {this.substance}; // CodeableConcept
        case -1408024454: /*alternate*/ return this.alternate == null ? new Base[0] : new Base[] {this.alternate}; // BooleanType
        case 75406931: /*allergenicIndicator*/ return this.allergenicIndicator == null ? new Base[0] : new Base[] {this.allergenicIndicator}; // BooleanType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 530040176: // substance
          this.substance = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1408024454: // alternate
          this.alternate = castToBoolean(value); // BooleanType
          return value;
        case 75406931: // allergenicIndicator
          this.allergenicIndicator = castToBoolean(value); // BooleanType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("substance")) {
          this.substance = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("alternate")) {
          this.alternate = castToBoolean(value); // BooleanType
        } else if (name.equals("allergenicIndicator")) {
          this.allergenicIndicator = castToBoolean(value); // BooleanType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 530040176:  return getSubstance(); 
        case -1408024454:  return getAlternateElement();
        case 75406931:  return getAllergenicIndicatorElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 530040176: /*substance*/ return new String[] {"CodeableConcept"};
        case -1408024454: /*alternate*/ return new String[] {"boolean"};
        case 75406931: /*allergenicIndicator*/ return new String[] {"boolean"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("substance")) {
          this.substance = new CodeableConcept();
          return this.substance;
        }
        else if (name.equals("alternate")) {
          throw new FHIRException("Cannot call addChild on a primitive type MedicinalProductDeviceSpec.alternate");
        }
        else if (name.equals("allergenicIndicator")) {
          throw new FHIRException("Cannot call addChild on a primitive type MedicinalProductDeviceSpec.allergenicIndicator");
        }
        else
          return super.addChild(name);
      }

      public MedicinalProductDeviceSpecMaterialComponent copy() {
        MedicinalProductDeviceSpecMaterialComponent dst = new MedicinalProductDeviceSpecMaterialComponent();
        copyValues(dst);
        dst.substance = substance == null ? null : substance.copy();
        dst.alternate = alternate == null ? null : alternate.copy();
        dst.allergenicIndicator = allergenicIndicator == null ? null : allergenicIndicator.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof MedicinalProductDeviceSpecMaterialComponent))
          return false;
        MedicinalProductDeviceSpecMaterialComponent o = (MedicinalProductDeviceSpecMaterialComponent) other_;
        return compareDeep(substance, o.substance, true) && compareDeep(alternate, o.alternate, true) && compareDeep(allergenicIndicator, o.allergenicIndicator, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof MedicinalProductDeviceSpecMaterialComponent))
          return false;
        MedicinalProductDeviceSpecMaterialComponent o = (MedicinalProductDeviceSpecMaterialComponent) other_;
        return compareValues(alternate, o.alternate, true) && compareValues(allergenicIndicator, o.allergenicIndicator, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(substance, alternate, allergenicIndicator
          );
      }

  public String fhirType() {
    return "MedicinalProductDeviceSpec.material";

  }

  }

    /**
     * Business identifier.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Business identifier", formalDefinition="Business identifier." )
    protected Identifier identifier;

    /**
     * The type of device.
     */
    @Child(name = "type", type = {CodeableConcept.class}, order=1, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The type of device", formalDefinition="The type of device." )
    protected CodeableConcept type;

    /**
     * Trade name of the device, where applicable.
     */
    @Child(name = "tradeName", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Trade name of the device, where applicable", formalDefinition="Trade name of the device, where applicable." )
    protected StringType tradeName;

    /**
     * The quantity of the device present in the packaging of a medicinal product.
     */
    @Child(name = "quantity", type = {Quantity.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The quantity of the device present in the packaging of a medicinal product", formalDefinition="The quantity of the device present in the packaging of a medicinal product." )
    protected Quantity quantity;

    /**
     * Device listing number.
     */
    @Child(name = "listingNumber", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Device listing number", formalDefinition="Device listing number." )
    protected StringType listingNumber;

    /**
     * Device model or reference number.
     */
    @Child(name = "modelNumber", type = {StringType.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Device model or reference number", formalDefinition="Device model or reference number." )
    protected StringType modelNumber;

    /**
     * Whether the device is supplied as sterile.
     */
    @Child(name = "sterilityIndicator", type = {CodeableConcept.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Whether the device is supplied as sterile", formalDefinition="Whether the device is supplied as sterile." )
    protected CodeableConcept sterilityIndicator;

    /**
     * Whether the device must be sterilised before use.
     */
    @Child(name = "sterilisationRequirement", type = {CodeableConcept.class}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Whether the device must be sterilised before use", formalDefinition="Whether the device must be sterilised before use." )
    protected CodeableConcept sterilisationRequirement;

    /**
     * Usage pattern including the number of times that the device may be used.
     */
    @Child(name = "usage", type = {CodeableConcept.class}, order=8, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Usage pattern including the number of times that the device may be used", formalDefinition="Usage pattern including the number of times that the device may be used." )
    protected CodeableConcept usage;

    /**
     * A nomenclature term for the device.
     */
    @Child(name = "nomenclature", type = {CodeableConcept.class}, order=9, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="A nomenclature term for the device", formalDefinition="A nomenclature term for the device." )
    protected List<CodeableConcept> nomenclature;

    /**
     * Shelf Life and storage information.
     */
    @Child(name = "shelfLife", type = {ProductShelfLife.class}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Shelf Life and storage information", formalDefinition="Shelf Life and storage information." )
    protected List<ProductShelfLife> shelfLife;

    /**
     * Dimensions, color etc.
     */
    @Child(name = "physicalCharacteristics", type = {ProdCharacteristic.class}, order=11, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Dimensions, color etc.", formalDefinition="Dimensions, color etc." )
    protected ProdCharacteristic physicalCharacteristics;

    /**
     * Other codeable characteristics.
     */
    @Child(name = "otherCharacteristics", type = {CodeableConcept.class}, order=12, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Other codeable characteristics", formalDefinition="Other codeable characteristics." )
    protected List<CodeableConcept> otherCharacteristics;

    /**
     * Batch number or expiry date of a device.
     */
    @Child(name = "batchIdentifier", type = {Identifier.class}, order=13, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Batch number or expiry date of a device", formalDefinition="Batch number or expiry date of a device." )
    protected List<Identifier> batchIdentifier;

    /**
     * Manufacturer of this Device.
     */
    @Child(name = "manufacturer", type = {Organization.class}, order=14, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Manufacturer of this Device", formalDefinition="Manufacturer of this Device." )
    protected List<Reference> manufacturer;
    /**
     * The actual objects that are the target of the reference (Manufacturer of this Device.)
     */
    protected List<Organization> manufacturerTarget;


    /**
     * A substance used to create the material(s) of which the device is made.
     */
    @Child(name = "material", type = {}, order=15, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="A substance used to create the material(s) of which the device is made", formalDefinition="A substance used to create the material(s) of which the device is made." )
    protected List<MedicinalProductDeviceSpecMaterialComponent> material;

    private static final long serialVersionUID = 839400430L;

  /**
   * Constructor
   */
    public MedicinalProductDeviceSpec() {
      super();
    }

  /**
   * Constructor
   */
    public MedicinalProductDeviceSpec(CodeableConcept type) {
      super();
      this.type = type;
    }

    /**
     * @return {@link #identifier} (Business identifier.)
     */
    public Identifier getIdentifier() { 
      if (this.identifier == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicinalProductDeviceSpec.identifier");
        else if (Configuration.doAutoCreate())
          this.identifier = new Identifier(); // cc
      return this.identifier;
    }

    public boolean hasIdentifier() { 
      return this.identifier != null && !this.identifier.isEmpty();
    }

    /**
     * @param value {@link #identifier} (Business identifier.)
     */
    public MedicinalProductDeviceSpec setIdentifier(Identifier value) { 
      this.identifier = value;
      return this;
    }

    /**
     * @return {@link #type} (The type of device.)
     */
    public CodeableConcept getType() { 
      if (this.type == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicinalProductDeviceSpec.type");
        else if (Configuration.doAutoCreate())
          this.type = new CodeableConcept(); // cc
      return this.type;
    }

    public boolean hasType() { 
      return this.type != null && !this.type.isEmpty();
    }

    /**
     * @param value {@link #type} (The type of device.)
     */
    public MedicinalProductDeviceSpec setType(CodeableConcept value) { 
      this.type = value;
      return this;
    }

    /**
     * @return {@link #tradeName} (Trade name of the device, where applicable.). This is the underlying object with id, value and extensions. The accessor "getTradeName" gives direct access to the value
     */
    public StringType getTradeNameElement() { 
      if (this.tradeName == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicinalProductDeviceSpec.tradeName");
        else if (Configuration.doAutoCreate())
          this.tradeName = new StringType(); // bb
      return this.tradeName;
    }

    public boolean hasTradeNameElement() { 
      return this.tradeName != null && !this.tradeName.isEmpty();
    }

    public boolean hasTradeName() { 
      return this.tradeName != null && !this.tradeName.isEmpty();
    }

    /**
     * @param value {@link #tradeName} (Trade name of the device, where applicable.). This is the underlying object with id, value and extensions. The accessor "getTradeName" gives direct access to the value
     */
    public MedicinalProductDeviceSpec setTradeNameElement(StringType value) { 
      this.tradeName = value;
      return this;
    }

    /**
     * @return Trade name of the device, where applicable.
     */
    public String getTradeName() { 
      return this.tradeName == null ? null : this.tradeName.getValue();
    }

    /**
     * @param value Trade name of the device, where applicable.
     */
    public MedicinalProductDeviceSpec setTradeName(String value) { 
      if (Utilities.noString(value))
        this.tradeName = null;
      else {
        if (this.tradeName == null)
          this.tradeName = new StringType();
        this.tradeName.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #quantity} (The quantity of the device present in the packaging of a medicinal product.)
     */
    public Quantity getQuantity() { 
      if (this.quantity == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicinalProductDeviceSpec.quantity");
        else if (Configuration.doAutoCreate())
          this.quantity = new Quantity(); // cc
      return this.quantity;
    }

    public boolean hasQuantity() { 
      return this.quantity != null && !this.quantity.isEmpty();
    }

    /**
     * @param value {@link #quantity} (The quantity of the device present in the packaging of a medicinal product.)
     */
    public MedicinalProductDeviceSpec setQuantity(Quantity value) { 
      this.quantity = value;
      return this;
    }

    /**
     * @return {@link #listingNumber} (Device listing number.). This is the underlying object with id, value and extensions. The accessor "getListingNumber" gives direct access to the value
     */
    public StringType getListingNumberElement() { 
      if (this.listingNumber == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicinalProductDeviceSpec.listingNumber");
        else if (Configuration.doAutoCreate())
          this.listingNumber = new StringType(); // bb
      return this.listingNumber;
    }

    public boolean hasListingNumberElement() { 
      return this.listingNumber != null && !this.listingNumber.isEmpty();
    }

    public boolean hasListingNumber() { 
      return this.listingNumber != null && !this.listingNumber.isEmpty();
    }

    /**
     * @param value {@link #listingNumber} (Device listing number.). This is the underlying object with id, value and extensions. The accessor "getListingNumber" gives direct access to the value
     */
    public MedicinalProductDeviceSpec setListingNumberElement(StringType value) { 
      this.listingNumber = value;
      return this;
    }

    /**
     * @return Device listing number.
     */
    public String getListingNumber() { 
      return this.listingNumber == null ? null : this.listingNumber.getValue();
    }

    /**
     * @param value Device listing number.
     */
    public MedicinalProductDeviceSpec setListingNumber(String value) { 
      if (Utilities.noString(value))
        this.listingNumber = null;
      else {
        if (this.listingNumber == null)
          this.listingNumber = new StringType();
        this.listingNumber.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #modelNumber} (Device model or reference number.). This is the underlying object with id, value and extensions. The accessor "getModelNumber" gives direct access to the value
     */
    public StringType getModelNumberElement() { 
      if (this.modelNumber == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicinalProductDeviceSpec.modelNumber");
        else if (Configuration.doAutoCreate())
          this.modelNumber = new StringType(); // bb
      return this.modelNumber;
    }

    public boolean hasModelNumberElement() { 
      return this.modelNumber != null && !this.modelNumber.isEmpty();
    }

    public boolean hasModelNumber() { 
      return this.modelNumber != null && !this.modelNumber.isEmpty();
    }

    /**
     * @param value {@link #modelNumber} (Device model or reference number.). This is the underlying object with id, value and extensions. The accessor "getModelNumber" gives direct access to the value
     */
    public MedicinalProductDeviceSpec setModelNumberElement(StringType value) { 
      this.modelNumber = value;
      return this;
    }

    /**
     * @return Device model or reference number.
     */
    public String getModelNumber() { 
      return this.modelNumber == null ? null : this.modelNumber.getValue();
    }

    /**
     * @param value Device model or reference number.
     */
    public MedicinalProductDeviceSpec setModelNumber(String value) { 
      if (Utilities.noString(value))
        this.modelNumber = null;
      else {
        if (this.modelNumber == null)
          this.modelNumber = new StringType();
        this.modelNumber.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #sterilityIndicator} (Whether the device is supplied as sterile.)
     */
    public CodeableConcept getSterilityIndicator() { 
      if (this.sterilityIndicator == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicinalProductDeviceSpec.sterilityIndicator");
        else if (Configuration.doAutoCreate())
          this.sterilityIndicator = new CodeableConcept(); // cc
      return this.sterilityIndicator;
    }

    public boolean hasSterilityIndicator() { 
      return this.sterilityIndicator != null && !this.sterilityIndicator.isEmpty();
    }

    /**
     * @param value {@link #sterilityIndicator} (Whether the device is supplied as sterile.)
     */
    public MedicinalProductDeviceSpec setSterilityIndicator(CodeableConcept value) { 
      this.sterilityIndicator = value;
      return this;
    }

    /**
     * @return {@link #sterilisationRequirement} (Whether the device must be sterilised before use.)
     */
    public CodeableConcept getSterilisationRequirement() { 
      if (this.sterilisationRequirement == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicinalProductDeviceSpec.sterilisationRequirement");
        else if (Configuration.doAutoCreate())
          this.sterilisationRequirement = new CodeableConcept(); // cc
      return this.sterilisationRequirement;
    }

    public boolean hasSterilisationRequirement() { 
      return this.sterilisationRequirement != null && !this.sterilisationRequirement.isEmpty();
    }

    /**
     * @param value {@link #sterilisationRequirement} (Whether the device must be sterilised before use.)
     */
    public MedicinalProductDeviceSpec setSterilisationRequirement(CodeableConcept value) { 
      this.sterilisationRequirement = value;
      return this;
    }

    /**
     * @return {@link #usage} (Usage pattern including the number of times that the device may be used.)
     */
    public CodeableConcept getUsage() { 
      if (this.usage == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicinalProductDeviceSpec.usage");
        else if (Configuration.doAutoCreate())
          this.usage = new CodeableConcept(); // cc
      return this.usage;
    }

    public boolean hasUsage() { 
      return this.usage != null && !this.usage.isEmpty();
    }

    /**
     * @param value {@link #usage} (Usage pattern including the number of times that the device may be used.)
     */
    public MedicinalProductDeviceSpec setUsage(CodeableConcept value) { 
      this.usage = value;
      return this;
    }

    /**
     * @return {@link #nomenclature} (A nomenclature term for the device.)
     */
    public List<CodeableConcept> getNomenclature() { 
      if (this.nomenclature == null)
        this.nomenclature = new ArrayList<CodeableConcept>();
      return this.nomenclature;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicinalProductDeviceSpec setNomenclature(List<CodeableConcept> theNomenclature) { 
      this.nomenclature = theNomenclature;
      return this;
    }

    public boolean hasNomenclature() { 
      if (this.nomenclature == null)
        return false;
      for (CodeableConcept item : this.nomenclature)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addNomenclature() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.nomenclature == null)
        this.nomenclature = new ArrayList<CodeableConcept>();
      this.nomenclature.add(t);
      return t;
    }

    public MedicinalProductDeviceSpec addNomenclature(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.nomenclature == null)
        this.nomenclature = new ArrayList<CodeableConcept>();
      this.nomenclature.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #nomenclature}, creating it if it does not already exist
     */
    public CodeableConcept getNomenclatureFirstRep() { 
      if (getNomenclature().isEmpty()) {
        addNomenclature();
      }
      return getNomenclature().get(0);
    }

    /**
     * @return {@link #shelfLife} (Shelf Life and storage information.)
     */
    public List<ProductShelfLife> getShelfLife() { 
      if (this.shelfLife == null)
        this.shelfLife = new ArrayList<ProductShelfLife>();
      return this.shelfLife;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicinalProductDeviceSpec setShelfLife(List<ProductShelfLife> theShelfLife) { 
      this.shelfLife = theShelfLife;
      return this;
    }

    public boolean hasShelfLife() { 
      if (this.shelfLife == null)
        return false;
      for (ProductShelfLife item : this.shelfLife)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public ProductShelfLife addShelfLife() { //3
      ProductShelfLife t = new ProductShelfLife();
      if (this.shelfLife == null)
        this.shelfLife = new ArrayList<ProductShelfLife>();
      this.shelfLife.add(t);
      return t;
    }

    public MedicinalProductDeviceSpec addShelfLife(ProductShelfLife t) { //3
      if (t == null)
        return this;
      if (this.shelfLife == null)
        this.shelfLife = new ArrayList<ProductShelfLife>();
      this.shelfLife.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #shelfLife}, creating it if it does not already exist
     */
    public ProductShelfLife getShelfLifeFirstRep() { 
      if (getShelfLife().isEmpty()) {
        addShelfLife();
      }
      return getShelfLife().get(0);
    }

    /**
     * @return {@link #physicalCharacteristics} (Dimensions, color etc.)
     */
    public ProdCharacteristic getPhysicalCharacteristics() { 
      if (this.physicalCharacteristics == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicinalProductDeviceSpec.physicalCharacteristics");
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
    public MedicinalProductDeviceSpec setPhysicalCharacteristics(ProdCharacteristic value) { 
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
    public MedicinalProductDeviceSpec setOtherCharacteristics(List<CodeableConcept> theOtherCharacteristics) { 
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

    public MedicinalProductDeviceSpec addOtherCharacteristics(CodeableConcept t) { //3
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
     * @return {@link #batchIdentifier} (Batch number or expiry date of a device.)
     */
    public List<Identifier> getBatchIdentifier() { 
      if (this.batchIdentifier == null)
        this.batchIdentifier = new ArrayList<Identifier>();
      return this.batchIdentifier;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicinalProductDeviceSpec setBatchIdentifier(List<Identifier> theBatchIdentifier) { 
      this.batchIdentifier = theBatchIdentifier;
      return this;
    }

    public boolean hasBatchIdentifier() { 
      if (this.batchIdentifier == null)
        return false;
      for (Identifier item : this.batchIdentifier)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Identifier addBatchIdentifier() { //3
      Identifier t = new Identifier();
      if (this.batchIdentifier == null)
        this.batchIdentifier = new ArrayList<Identifier>();
      this.batchIdentifier.add(t);
      return t;
    }

    public MedicinalProductDeviceSpec addBatchIdentifier(Identifier t) { //3
      if (t == null)
        return this;
      if (this.batchIdentifier == null)
        this.batchIdentifier = new ArrayList<Identifier>();
      this.batchIdentifier.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #batchIdentifier}, creating it if it does not already exist
     */
    public Identifier getBatchIdentifierFirstRep() { 
      if (getBatchIdentifier().isEmpty()) {
        addBatchIdentifier();
      }
      return getBatchIdentifier().get(0);
    }

    /**
     * @return {@link #manufacturer} (Manufacturer of this Device.)
     */
    public List<Reference> getManufacturer() { 
      if (this.manufacturer == null)
        this.manufacturer = new ArrayList<Reference>();
      return this.manufacturer;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicinalProductDeviceSpec setManufacturer(List<Reference> theManufacturer) { 
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

    public MedicinalProductDeviceSpec addManufacturer(Reference t) { //3
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
     * @return {@link #material} (A substance used to create the material(s) of which the device is made.)
     */
    public List<MedicinalProductDeviceSpecMaterialComponent> getMaterial() { 
      if (this.material == null)
        this.material = new ArrayList<MedicinalProductDeviceSpecMaterialComponent>();
      return this.material;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicinalProductDeviceSpec setMaterial(List<MedicinalProductDeviceSpecMaterialComponent> theMaterial) { 
      this.material = theMaterial;
      return this;
    }

    public boolean hasMaterial() { 
      if (this.material == null)
        return false;
      for (MedicinalProductDeviceSpecMaterialComponent item : this.material)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public MedicinalProductDeviceSpecMaterialComponent addMaterial() { //3
      MedicinalProductDeviceSpecMaterialComponent t = new MedicinalProductDeviceSpecMaterialComponent();
      if (this.material == null)
        this.material = new ArrayList<MedicinalProductDeviceSpecMaterialComponent>();
      this.material.add(t);
      return t;
    }

    public MedicinalProductDeviceSpec addMaterial(MedicinalProductDeviceSpecMaterialComponent t) { //3
      if (t == null)
        return this;
      if (this.material == null)
        this.material = new ArrayList<MedicinalProductDeviceSpecMaterialComponent>();
      this.material.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #material}, creating it if it does not already exist
     */
    public MedicinalProductDeviceSpecMaterialComponent getMaterialFirstRep() { 
      if (getMaterial().isEmpty()) {
        addMaterial();
      }
      return getMaterial().get(0);
    }

      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("identifier", "Identifier", "Business identifier.", 0, 1, identifier));
        children.add(new Property("type", "CodeableConcept", "The type of device.", 0, 1, type));
        children.add(new Property("tradeName", "string", "Trade name of the device, where applicable.", 0, 1, tradeName));
        children.add(new Property("quantity", "Quantity", "The quantity of the device present in the packaging of a medicinal product.", 0, 1, quantity));
        children.add(new Property("listingNumber", "string", "Device listing number.", 0, 1, listingNumber));
        children.add(new Property("modelNumber", "string", "Device model or reference number.", 0, 1, modelNumber));
        children.add(new Property("sterilityIndicator", "CodeableConcept", "Whether the device is supplied as sterile.", 0, 1, sterilityIndicator));
        children.add(new Property("sterilisationRequirement", "CodeableConcept", "Whether the device must be sterilised before use.", 0, 1, sterilisationRequirement));
        children.add(new Property("usage", "CodeableConcept", "Usage pattern including the number of times that the device may be used.", 0, 1, usage));
        children.add(new Property("nomenclature", "CodeableConcept", "A nomenclature term for the device.", 0, java.lang.Integer.MAX_VALUE, nomenclature));
        children.add(new Property("shelfLife", "ProductShelfLife", "Shelf Life and storage information.", 0, java.lang.Integer.MAX_VALUE, shelfLife));
        children.add(new Property("physicalCharacteristics", "ProdCharacteristic", "Dimensions, color etc.", 0, 1, physicalCharacteristics));
        children.add(new Property("otherCharacteristics", "CodeableConcept", "Other codeable characteristics.", 0, java.lang.Integer.MAX_VALUE, otherCharacteristics));
        children.add(new Property("batchIdentifier", "Identifier", "Batch number or expiry date of a device.", 0, java.lang.Integer.MAX_VALUE, batchIdentifier));
        children.add(new Property("manufacturer", "Reference(Organization)", "Manufacturer of this Device.", 0, java.lang.Integer.MAX_VALUE, manufacturer));
        children.add(new Property("material", "", "A substance used to create the material(s) of which the device is made.", 0, java.lang.Integer.MAX_VALUE, material));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case -1618432855: /*identifier*/  return new Property("identifier", "Identifier", "Business identifier.", 0, 1, identifier);
        case 3575610: /*type*/  return new Property("type", "CodeableConcept", "The type of device.", 0, 1, type);
        case 752717327: /*tradeName*/  return new Property("tradeName", "string", "Trade name of the device, where applicable.", 0, 1, tradeName);
        case -1285004149: /*quantity*/  return new Property("quantity", "Quantity", "The quantity of the device present in the packaging of a medicinal product.", 0, 1, quantity);
        case -1571189011: /*listingNumber*/  return new Property("listingNumber", "string", "Device listing number.", 0, 1, listingNumber);
        case 346619858: /*modelNumber*/  return new Property("modelNumber", "string", "Device model or reference number.", 0, 1, modelNumber);
        case -2093506574: /*sterilityIndicator*/  return new Property("sterilityIndicator", "CodeableConcept", "Whether the device is supplied as sterile.", 0, 1, sterilityIndicator);
        case 693339497: /*sterilisationRequirement*/  return new Property("sterilisationRequirement", "CodeableConcept", "Whether the device must be sterilised before use.", 0, 1, sterilisationRequirement);
        case 111574433: /*usage*/  return new Property("usage", "CodeableConcept", "Usage pattern including the number of times that the device may be used.", 0, 1, usage);
        case 895910775: /*nomenclature*/  return new Property("nomenclature", "CodeableConcept", "A nomenclature term for the device.", 0, java.lang.Integer.MAX_VALUE, nomenclature);
        case 1796889670: /*shelfLife*/  return new Property("shelfLife", "ProductShelfLife", "Shelf Life and storage information.", 0, java.lang.Integer.MAX_VALUE, shelfLife);
        case -1599676319: /*physicalCharacteristics*/  return new Property("physicalCharacteristics", "ProdCharacteristic", "Dimensions, color etc.", 0, 1, physicalCharacteristics);
        case 722135304: /*otherCharacteristics*/  return new Property("otherCharacteristics", "CodeableConcept", "Other codeable characteristics.", 0, java.lang.Integer.MAX_VALUE, otherCharacteristics);
        case -1688395901: /*batchIdentifier*/  return new Property("batchIdentifier", "Identifier", "Batch number or expiry date of a device.", 0, java.lang.Integer.MAX_VALUE, batchIdentifier);
        case -1969347631: /*manufacturer*/  return new Property("manufacturer", "Reference(Organization)", "Manufacturer of this Device.", 0, java.lang.Integer.MAX_VALUE, manufacturer);
        case 299066663: /*material*/  return new Property("material", "", "A substance used to create the material(s) of which the device is made.", 0, java.lang.Integer.MAX_VALUE, material);
        default: return super.getNamedProperty(_hash, _name, _checkValid);
        }

      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : new Base[] {this.identifier}; // Identifier
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case 752717327: /*tradeName*/ return this.tradeName == null ? new Base[0] : new Base[] {this.tradeName}; // StringType
        case -1285004149: /*quantity*/ return this.quantity == null ? new Base[0] : new Base[] {this.quantity}; // Quantity
        case -1571189011: /*listingNumber*/ return this.listingNumber == null ? new Base[0] : new Base[] {this.listingNumber}; // StringType
        case 346619858: /*modelNumber*/ return this.modelNumber == null ? new Base[0] : new Base[] {this.modelNumber}; // StringType
        case -2093506574: /*sterilityIndicator*/ return this.sterilityIndicator == null ? new Base[0] : new Base[] {this.sterilityIndicator}; // CodeableConcept
        case 693339497: /*sterilisationRequirement*/ return this.sterilisationRequirement == null ? new Base[0] : new Base[] {this.sterilisationRequirement}; // CodeableConcept
        case 111574433: /*usage*/ return this.usage == null ? new Base[0] : new Base[] {this.usage}; // CodeableConcept
        case 895910775: /*nomenclature*/ return this.nomenclature == null ? new Base[0] : this.nomenclature.toArray(new Base[this.nomenclature.size()]); // CodeableConcept
        case 1796889670: /*shelfLife*/ return this.shelfLife == null ? new Base[0] : this.shelfLife.toArray(new Base[this.shelfLife.size()]); // ProductShelfLife
        case -1599676319: /*physicalCharacteristics*/ return this.physicalCharacteristics == null ? new Base[0] : new Base[] {this.physicalCharacteristics}; // ProdCharacteristic
        case 722135304: /*otherCharacteristics*/ return this.otherCharacteristics == null ? new Base[0] : this.otherCharacteristics.toArray(new Base[this.otherCharacteristics.size()]); // CodeableConcept
        case -1688395901: /*batchIdentifier*/ return this.batchIdentifier == null ? new Base[0] : this.batchIdentifier.toArray(new Base[this.batchIdentifier.size()]); // Identifier
        case -1969347631: /*manufacturer*/ return this.manufacturer == null ? new Base[0] : this.manufacturer.toArray(new Base[this.manufacturer.size()]); // Reference
        case 299066663: /*material*/ return this.material == null ? new Base[0] : this.material.toArray(new Base[this.material.size()]); // MedicinalProductDeviceSpecMaterialComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.identifier = castToIdentifier(value); // Identifier
          return value;
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 752717327: // tradeName
          this.tradeName = castToString(value); // StringType
          return value;
        case -1285004149: // quantity
          this.quantity = castToQuantity(value); // Quantity
          return value;
        case -1571189011: // listingNumber
          this.listingNumber = castToString(value); // StringType
          return value;
        case 346619858: // modelNumber
          this.modelNumber = castToString(value); // StringType
          return value;
        case -2093506574: // sterilityIndicator
          this.sterilityIndicator = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 693339497: // sterilisationRequirement
          this.sterilisationRequirement = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 111574433: // usage
          this.usage = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 895910775: // nomenclature
          this.getNomenclature().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 1796889670: // shelfLife
          this.getShelfLife().add(castToProductShelfLife(value)); // ProductShelfLife
          return value;
        case -1599676319: // physicalCharacteristics
          this.physicalCharacteristics = castToProdCharacteristic(value); // ProdCharacteristic
          return value;
        case 722135304: // otherCharacteristics
          this.getOtherCharacteristics().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -1688395901: // batchIdentifier
          this.getBatchIdentifier().add(castToIdentifier(value)); // Identifier
          return value;
        case -1969347631: // manufacturer
          this.getManufacturer().add(castToReference(value)); // Reference
          return value;
        case 299066663: // material
          this.getMaterial().add((MedicinalProductDeviceSpecMaterialComponent) value); // MedicinalProductDeviceSpecMaterialComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier")) {
          this.identifier = castToIdentifier(value); // Identifier
        } else if (name.equals("type")) {
          this.type = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("tradeName")) {
          this.tradeName = castToString(value); // StringType
        } else if (name.equals("quantity")) {
          this.quantity = castToQuantity(value); // Quantity
        } else if (name.equals("listingNumber")) {
          this.listingNumber = castToString(value); // StringType
        } else if (name.equals("modelNumber")) {
          this.modelNumber = castToString(value); // StringType
        } else if (name.equals("sterilityIndicator")) {
          this.sterilityIndicator = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("sterilisationRequirement")) {
          this.sterilisationRequirement = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("usage")) {
          this.usage = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("nomenclature")) {
          this.getNomenclature().add(castToCodeableConcept(value));
        } else if (name.equals("shelfLife")) {
          this.getShelfLife().add(castToProductShelfLife(value));
        } else if (name.equals("physicalCharacteristics")) {
          this.physicalCharacteristics = castToProdCharacteristic(value); // ProdCharacteristic
        } else if (name.equals("otherCharacteristics")) {
          this.getOtherCharacteristics().add(castToCodeableConcept(value));
        } else if (name.equals("batchIdentifier")) {
          this.getBatchIdentifier().add(castToIdentifier(value));
        } else if (name.equals("manufacturer")) {
          this.getManufacturer().add(castToReference(value));
        } else if (name.equals("material")) {
          this.getMaterial().add((MedicinalProductDeviceSpecMaterialComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return getIdentifier(); 
        case 3575610:  return getType(); 
        case 752717327:  return getTradeNameElement();
        case -1285004149:  return getQuantity(); 
        case -1571189011:  return getListingNumberElement();
        case 346619858:  return getModelNumberElement();
        case -2093506574:  return getSterilityIndicator(); 
        case 693339497:  return getSterilisationRequirement(); 
        case 111574433:  return getUsage(); 
        case 895910775:  return addNomenclature(); 
        case 1796889670:  return addShelfLife(); 
        case -1599676319:  return getPhysicalCharacteristics(); 
        case 722135304:  return addOtherCharacteristics(); 
        case -1688395901:  return addBatchIdentifier(); 
        case -1969347631:  return addManufacturer(); 
        case 299066663:  return addMaterial(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case 752717327: /*tradeName*/ return new String[] {"string"};
        case -1285004149: /*quantity*/ return new String[] {"Quantity"};
        case -1571189011: /*listingNumber*/ return new String[] {"string"};
        case 346619858: /*modelNumber*/ return new String[] {"string"};
        case -2093506574: /*sterilityIndicator*/ return new String[] {"CodeableConcept"};
        case 693339497: /*sterilisationRequirement*/ return new String[] {"CodeableConcept"};
        case 111574433: /*usage*/ return new String[] {"CodeableConcept"};
        case 895910775: /*nomenclature*/ return new String[] {"CodeableConcept"};
        case 1796889670: /*shelfLife*/ return new String[] {"ProductShelfLife"};
        case -1599676319: /*physicalCharacteristics*/ return new String[] {"ProdCharacteristic"};
        case 722135304: /*otherCharacteristics*/ return new String[] {"CodeableConcept"};
        case -1688395901: /*batchIdentifier*/ return new String[] {"Identifier"};
        case -1969347631: /*manufacturer*/ return new String[] {"Reference"};
        case 299066663: /*material*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          this.identifier = new Identifier();
          return this.identifier;
        }
        else if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("tradeName")) {
          throw new FHIRException("Cannot call addChild on a primitive type MedicinalProductDeviceSpec.tradeName");
        }
        else if (name.equals("quantity")) {
          this.quantity = new Quantity();
          return this.quantity;
        }
        else if (name.equals("listingNumber")) {
          throw new FHIRException("Cannot call addChild on a primitive type MedicinalProductDeviceSpec.listingNumber");
        }
        else if (name.equals("modelNumber")) {
          throw new FHIRException("Cannot call addChild on a primitive type MedicinalProductDeviceSpec.modelNumber");
        }
        else if (name.equals("sterilityIndicator")) {
          this.sterilityIndicator = new CodeableConcept();
          return this.sterilityIndicator;
        }
        else if (name.equals("sterilisationRequirement")) {
          this.sterilisationRequirement = new CodeableConcept();
          return this.sterilisationRequirement;
        }
        else if (name.equals("usage")) {
          this.usage = new CodeableConcept();
          return this.usage;
        }
        else if (name.equals("nomenclature")) {
          return addNomenclature();
        }
        else if (name.equals("shelfLife")) {
          return addShelfLife();
        }
        else if (name.equals("physicalCharacteristics")) {
          this.physicalCharacteristics = new ProdCharacteristic();
          return this.physicalCharacteristics;
        }
        else if (name.equals("otherCharacteristics")) {
          return addOtherCharacteristics();
        }
        else if (name.equals("batchIdentifier")) {
          return addBatchIdentifier();
        }
        else if (name.equals("manufacturer")) {
          return addManufacturer();
        }
        else if (name.equals("material")) {
          return addMaterial();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "MedicinalProductDeviceSpec";

  }

      public MedicinalProductDeviceSpec copy() {
        MedicinalProductDeviceSpec dst = new MedicinalProductDeviceSpec();
        copyValues(dst);
        dst.identifier = identifier == null ? null : identifier.copy();
        dst.type = type == null ? null : type.copy();
        dst.tradeName = tradeName == null ? null : tradeName.copy();
        dst.quantity = quantity == null ? null : quantity.copy();
        dst.listingNumber = listingNumber == null ? null : listingNumber.copy();
        dst.modelNumber = modelNumber == null ? null : modelNumber.copy();
        dst.sterilityIndicator = sterilityIndicator == null ? null : sterilityIndicator.copy();
        dst.sterilisationRequirement = sterilisationRequirement == null ? null : sterilisationRequirement.copy();
        dst.usage = usage == null ? null : usage.copy();
        if (nomenclature != null) {
          dst.nomenclature = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : nomenclature)
            dst.nomenclature.add(i.copy());
        };
        if (shelfLife != null) {
          dst.shelfLife = new ArrayList<ProductShelfLife>();
          for (ProductShelfLife i : shelfLife)
            dst.shelfLife.add(i.copy());
        };
        dst.physicalCharacteristics = physicalCharacteristics == null ? null : physicalCharacteristics.copy();
        if (otherCharacteristics != null) {
          dst.otherCharacteristics = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : otherCharacteristics)
            dst.otherCharacteristics.add(i.copy());
        };
        if (batchIdentifier != null) {
          dst.batchIdentifier = new ArrayList<Identifier>();
          for (Identifier i : batchIdentifier)
            dst.batchIdentifier.add(i.copy());
        };
        if (manufacturer != null) {
          dst.manufacturer = new ArrayList<Reference>();
          for (Reference i : manufacturer)
            dst.manufacturer.add(i.copy());
        };
        if (material != null) {
          dst.material = new ArrayList<MedicinalProductDeviceSpecMaterialComponent>();
          for (MedicinalProductDeviceSpecMaterialComponent i : material)
            dst.material.add(i.copy());
        };
        return dst;
      }

      protected MedicinalProductDeviceSpec typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof MedicinalProductDeviceSpec))
          return false;
        MedicinalProductDeviceSpec o = (MedicinalProductDeviceSpec) other_;
        return compareDeep(identifier, o.identifier, true) && compareDeep(type, o.type, true) && compareDeep(tradeName, o.tradeName, true)
           && compareDeep(quantity, o.quantity, true) && compareDeep(listingNumber, o.listingNumber, true)
           && compareDeep(modelNumber, o.modelNumber, true) && compareDeep(sterilityIndicator, o.sterilityIndicator, true)
           && compareDeep(sterilisationRequirement, o.sterilisationRequirement, true) && compareDeep(usage, o.usage, true)
           && compareDeep(nomenclature, o.nomenclature, true) && compareDeep(shelfLife, o.shelfLife, true)
           && compareDeep(physicalCharacteristics, o.physicalCharacteristics, true) && compareDeep(otherCharacteristics, o.otherCharacteristics, true)
           && compareDeep(batchIdentifier, o.batchIdentifier, true) && compareDeep(manufacturer, o.manufacturer, true)
           && compareDeep(material, o.material, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof MedicinalProductDeviceSpec))
          return false;
        MedicinalProductDeviceSpec o = (MedicinalProductDeviceSpec) other_;
        return compareValues(tradeName, o.tradeName, true) && compareValues(listingNumber, o.listingNumber, true)
           && compareValues(modelNumber, o.modelNumber, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, type, tradeName
          , quantity, listingNumber, modelNumber, sterilityIndicator, sterilisationRequirement
          , usage, nomenclature, shelfLife, physicalCharacteristics, otherCharacteristics, batchIdentifier
          , manufacturer, material);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.MedicinalProductDeviceSpec;
   }


}

