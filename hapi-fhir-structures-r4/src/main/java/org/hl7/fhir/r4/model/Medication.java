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
 * This resource is primarily used for the identification and definition of a medication for the purposes of prescribing, dispensing, and administering a medication as well as for making statements about medication use.
 */
@ResourceDef(name="Medication", profile="http://hl7.org/fhir/StructureDefinition/Medication")
public class Medication extends DomainResource {

    public enum MedicationStatus {
        /**
         * The medication is available for use.
         */
        ACTIVE, 
        /**
         * The medication is not available for use.
         */
        INACTIVE, 
        /**
         * The medication was entered in error.
         */
        ENTEREDINERROR, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static MedicationStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("active".equals(codeString))
          return ACTIVE;
        if ("inactive".equals(codeString))
          return INACTIVE;
        if ("entered-in-error".equals(codeString))
          return ENTEREDINERROR;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown MedicationStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ACTIVE: return "active";
            case INACTIVE: return "inactive";
            case ENTEREDINERROR: return "entered-in-error";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case ACTIVE: return "http://hl7.org/fhir/CodeSystem/medication-status";
            case INACTIVE: return "http://hl7.org/fhir/CodeSystem/medication-status";
            case ENTEREDINERROR: return "http://hl7.org/fhir/CodeSystem/medication-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case ACTIVE: return "The medication is available for use.";
            case INACTIVE: return "The medication is not available for use.";
            case ENTEREDINERROR: return "The medication was entered in error.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ACTIVE: return "Active";
            case INACTIVE: return "Inactive";
            case ENTEREDINERROR: return "Entered in Error";
            default: return "?";
          }
        }
    }

  public static class MedicationStatusEnumFactory implements EnumFactory<MedicationStatus> {
    public MedicationStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("active".equals(codeString))
          return MedicationStatus.ACTIVE;
        if ("inactive".equals(codeString))
          return MedicationStatus.INACTIVE;
        if ("entered-in-error".equals(codeString))
          return MedicationStatus.ENTEREDINERROR;
        throw new IllegalArgumentException("Unknown MedicationStatus code '"+codeString+"'");
        }
        public Enumeration<MedicationStatus> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<MedicationStatus>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("active".equals(codeString))
          return new Enumeration<MedicationStatus>(this, MedicationStatus.ACTIVE);
        if ("inactive".equals(codeString))
          return new Enumeration<MedicationStatus>(this, MedicationStatus.INACTIVE);
        if ("entered-in-error".equals(codeString))
          return new Enumeration<MedicationStatus>(this, MedicationStatus.ENTEREDINERROR);
        throw new FHIRException("Unknown MedicationStatus code '"+codeString+"'");
        }
    public String toCode(MedicationStatus code) {
      if (code == MedicationStatus.ACTIVE)
        return "active";
      if (code == MedicationStatus.INACTIVE)
        return "inactive";
      if (code == MedicationStatus.ENTEREDINERROR)
        return "entered-in-error";
      return "?";
      }
    public String toSystem(MedicationStatus code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class MedicationIngredientComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The actual ingredient - either a substance (simple ingredient) or another medication of a medication.
         */
        @Child(name = "item", type = {CodeableConcept.class, Substance.class, Medication.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The actual ingredient or content", formalDefinition="The actual ingredient - either a substance (simple ingredient) or another medication of a medication." )
        protected Type item;

        /**
         * Indication of whether this ingredient affects the therapeutic action of the drug.
         */
        @Child(name = "isActive", type = {BooleanType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Active ingredient indicator", formalDefinition="Indication of whether this ingredient affects the therapeutic action of the drug." )
        protected BooleanType isActive;

        /**
         * Specifies how many (or how much) of the items there are in this Medication.  For example, 250 mg per tablet.  This is expressed as a ratio where the numerator is 250mg and the denominator is 1 tablet.
         */
        @Child(name = "strength", type = {Ratio.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Quantity of ingredient present", formalDefinition="Specifies how many (or how much) of the items there are in this Medication.  For example, 250 mg per tablet.  This is expressed as a ratio where the numerator is 250mg and the denominator is 1 tablet." )
        protected Ratio strength;

        private static final long serialVersionUID = 1365103497L;

    /**
     * Constructor
     */
      public MedicationIngredientComponent() {
        super();
      }

    /**
     * Constructor
     */
      public MedicationIngredientComponent(Type item) {
        super();
        this.item = item;
      }

        /**
         * @return {@link #item} (The actual ingredient - either a substance (simple ingredient) or another medication of a medication.)
         */
        public Type getItem() { 
          return this.item;
        }

        /**
         * @return {@link #item} (The actual ingredient - either a substance (simple ingredient) or another medication of a medication.)
         */
        public CodeableConcept getItemCodeableConcept() throws FHIRException { 
          if (this.item == null)
            this.item = new CodeableConcept();
          if (!(this.item instanceof CodeableConcept))
            throw new FHIRException("Type mismatch: the type CodeableConcept was expected, but "+this.item.getClass().getName()+" was encountered");
          return (CodeableConcept) this.item;
        }

        public boolean hasItemCodeableConcept() { 
          return this != null && this.item instanceof CodeableConcept;
        }

        /**
         * @return {@link #item} (The actual ingredient - either a substance (simple ingredient) or another medication of a medication.)
         */
        public Reference getItemReference() throws FHIRException { 
          if (this.item == null)
            this.item = new Reference();
          if (!(this.item instanceof Reference))
            throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.item.getClass().getName()+" was encountered");
          return (Reference) this.item;
        }

        public boolean hasItemReference() { 
          return this != null && this.item instanceof Reference;
        }

        public boolean hasItem() { 
          return this.item != null && !this.item.isEmpty();
        }

        /**
         * @param value {@link #item} (The actual ingredient - either a substance (simple ingredient) or another medication of a medication.)
         */
        public MedicationIngredientComponent setItem(Type value) { 
          if (value != null && !(value instanceof CodeableConcept || value instanceof Reference))
            throw new Error("Not the right type for Medication.ingredient.item[x]: "+value.fhirType());
          this.item = value;
          return this;
        }

        /**
         * @return {@link #isActive} (Indication of whether this ingredient affects the therapeutic action of the drug.). This is the underlying object with id, value and extensions. The accessor "getIsActive" gives direct access to the value
         */
        public BooleanType getIsActiveElement() { 
          if (this.isActive == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationIngredientComponent.isActive");
            else if (Configuration.doAutoCreate())
              this.isActive = new BooleanType(); // bb
          return this.isActive;
        }

        public boolean hasIsActiveElement() { 
          return this.isActive != null && !this.isActive.isEmpty();
        }

        public boolean hasIsActive() { 
          return this.isActive != null && !this.isActive.isEmpty();
        }

        /**
         * @param value {@link #isActive} (Indication of whether this ingredient affects the therapeutic action of the drug.). This is the underlying object with id, value and extensions. The accessor "getIsActive" gives direct access to the value
         */
        public MedicationIngredientComponent setIsActiveElement(BooleanType value) { 
          this.isActive = value;
          return this;
        }

        /**
         * @return Indication of whether this ingredient affects the therapeutic action of the drug.
         */
        public boolean getIsActive() { 
          return this.isActive == null || this.isActive.isEmpty() ? false : this.isActive.getValue();
        }

        /**
         * @param value Indication of whether this ingredient affects the therapeutic action of the drug.
         */
        public MedicationIngredientComponent setIsActive(boolean value) { 
            if (this.isActive == null)
              this.isActive = new BooleanType();
            this.isActive.setValue(value);
          return this;
        }

        /**
         * @return {@link #strength} (Specifies how many (or how much) of the items there are in this Medication.  For example, 250 mg per tablet.  This is expressed as a ratio where the numerator is 250mg and the denominator is 1 tablet.)
         */
        public Ratio getStrength() { 
          if (this.strength == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationIngredientComponent.strength");
            else if (Configuration.doAutoCreate())
              this.strength = new Ratio(); // cc
          return this.strength;
        }

        public boolean hasStrength() { 
          return this.strength != null && !this.strength.isEmpty();
        }

        /**
         * @param value {@link #strength} (Specifies how many (or how much) of the items there are in this Medication.  For example, 250 mg per tablet.  This is expressed as a ratio where the numerator is 250mg and the denominator is 1 tablet.)
         */
        public MedicationIngredientComponent setStrength(Ratio value) { 
          this.strength = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("item[x]", "CodeableConcept|Reference(Substance|Medication)", "The actual ingredient - either a substance (simple ingredient) or another medication of a medication.", 0, 1, item));
          children.add(new Property("isActive", "boolean", "Indication of whether this ingredient affects the therapeutic action of the drug.", 0, 1, isActive));
          children.add(new Property("strength", "Ratio", "Specifies how many (or how much) of the items there are in this Medication.  For example, 250 mg per tablet.  This is expressed as a ratio where the numerator is 250mg and the denominator is 1 tablet.", 0, 1, strength));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 2116201613: /*item[x]*/  return new Property("item[x]", "CodeableConcept|Reference(Substance|Medication)", "The actual ingredient - either a substance (simple ingredient) or another medication of a medication.", 0, 1, item);
          case 3242771: /*item*/  return new Property("item[x]", "CodeableConcept|Reference(Substance|Medication)", "The actual ingredient - either a substance (simple ingredient) or another medication of a medication.", 0, 1, item);
          case 106644494: /*itemCodeableConcept*/  return new Property("item[x]", "CodeableConcept|Reference(Substance|Medication)", "The actual ingredient - either a substance (simple ingredient) or another medication of a medication.", 0, 1, item);
          case 1376364920: /*itemReference*/  return new Property("item[x]", "CodeableConcept|Reference(Substance|Medication)", "The actual ingredient - either a substance (simple ingredient) or another medication of a medication.", 0, 1, item);
          case -748916528: /*isActive*/  return new Property("isActive", "boolean", "Indication of whether this ingredient affects the therapeutic action of the drug.", 0, 1, isActive);
          case 1791316033: /*strength*/  return new Property("strength", "Ratio", "Specifies how many (or how much) of the items there are in this Medication.  For example, 250 mg per tablet.  This is expressed as a ratio where the numerator is 250mg and the denominator is 1 tablet.", 0, 1, strength);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3242771: /*item*/ return this.item == null ? new Base[0] : new Base[] {this.item}; // Type
        case -748916528: /*isActive*/ return this.isActive == null ? new Base[0] : new Base[] {this.isActive}; // BooleanType
        case 1791316033: /*strength*/ return this.strength == null ? new Base[0] : new Base[] {this.strength}; // Ratio
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3242771: // item
          this.item = castToType(value); // Type
          return value;
        case -748916528: // isActive
          this.isActive = castToBoolean(value); // BooleanType
          return value;
        case 1791316033: // strength
          this.strength = castToRatio(value); // Ratio
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("item[x]")) {
          this.item = castToType(value); // Type
        } else if (name.equals("isActive")) {
          this.isActive = castToBoolean(value); // BooleanType
        } else if (name.equals("strength")) {
          this.strength = castToRatio(value); // Ratio
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 2116201613:  return getItem(); 
        case 3242771:  return getItem(); 
        case -748916528:  return getIsActiveElement();
        case 1791316033:  return getStrength(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3242771: /*item*/ return new String[] {"CodeableConcept", "Reference"};
        case -748916528: /*isActive*/ return new String[] {"boolean"};
        case 1791316033: /*strength*/ return new String[] {"Ratio"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("itemCodeableConcept")) {
          this.item = new CodeableConcept();
          return this.item;
        }
        else if (name.equals("itemReference")) {
          this.item = new Reference();
          return this.item;
        }
        else if (name.equals("isActive")) {
          throw new FHIRException("Cannot call addChild on a primitive type Medication.isActive");
        }
        else if (name.equals("strength")) {
          this.strength = new Ratio();
          return this.strength;
        }
        else
          return super.addChild(name);
      }

      public MedicationIngredientComponent copy() {
        MedicationIngredientComponent dst = new MedicationIngredientComponent();
        copyValues(dst);
        dst.item = item == null ? null : item.copy();
        dst.isActive = isActive == null ? null : isActive.copy();
        dst.strength = strength == null ? null : strength.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof MedicationIngredientComponent))
          return false;
        MedicationIngredientComponent o = (MedicationIngredientComponent) other_;
        return compareDeep(item, o.item, true) && compareDeep(isActive, o.isActive, true) && compareDeep(strength, o.strength, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof MedicationIngredientComponent))
          return false;
        MedicationIngredientComponent o = (MedicationIngredientComponent) other_;
        return compareValues(isActive, o.isActive, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(item, isActive, strength
          );
      }

  public String fhirType() {
    return "Medication.ingredient";

  }

  }

    @Block()
    public static class MedicationBatchComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The assigned lot number of a batch of the specified product.
         */
        @Child(name = "lotNumber", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Identifier assigned to batch", formalDefinition="The assigned lot number of a batch of the specified product." )
        protected StringType lotNumber;

        /**
         * When this specific batch of product will expire.
         */
        @Child(name = "expirationDate", type = {DateTimeType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="When batch will expire", formalDefinition="When this specific batch of product will expire." )
        protected DateTimeType expirationDate;

        private static final long serialVersionUID = 1982738755L;

    /**
     * Constructor
     */
      public MedicationBatchComponent() {
        super();
      }

        /**
         * @return {@link #lotNumber} (The assigned lot number of a batch of the specified product.). This is the underlying object with id, value and extensions. The accessor "getLotNumber" gives direct access to the value
         */
        public StringType getLotNumberElement() { 
          if (this.lotNumber == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationBatchComponent.lotNumber");
            else if (Configuration.doAutoCreate())
              this.lotNumber = new StringType(); // bb
          return this.lotNumber;
        }

        public boolean hasLotNumberElement() { 
          return this.lotNumber != null && !this.lotNumber.isEmpty();
        }

        public boolean hasLotNumber() { 
          return this.lotNumber != null && !this.lotNumber.isEmpty();
        }

        /**
         * @param value {@link #lotNumber} (The assigned lot number of a batch of the specified product.). This is the underlying object with id, value and extensions. The accessor "getLotNumber" gives direct access to the value
         */
        public MedicationBatchComponent setLotNumberElement(StringType value) { 
          this.lotNumber = value;
          return this;
        }

        /**
         * @return The assigned lot number of a batch of the specified product.
         */
        public String getLotNumber() { 
          return this.lotNumber == null ? null : this.lotNumber.getValue();
        }

        /**
         * @param value The assigned lot number of a batch of the specified product.
         */
        public MedicationBatchComponent setLotNumber(String value) { 
          if (Utilities.noString(value))
            this.lotNumber = null;
          else {
            if (this.lotNumber == null)
              this.lotNumber = new StringType();
            this.lotNumber.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #expirationDate} (When this specific batch of product will expire.). This is the underlying object with id, value and extensions. The accessor "getExpirationDate" gives direct access to the value
         */
        public DateTimeType getExpirationDateElement() { 
          if (this.expirationDate == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationBatchComponent.expirationDate");
            else if (Configuration.doAutoCreate())
              this.expirationDate = new DateTimeType(); // bb
          return this.expirationDate;
        }

        public boolean hasExpirationDateElement() { 
          return this.expirationDate != null && !this.expirationDate.isEmpty();
        }

        public boolean hasExpirationDate() { 
          return this.expirationDate != null && !this.expirationDate.isEmpty();
        }

        /**
         * @param value {@link #expirationDate} (When this specific batch of product will expire.). This is the underlying object with id, value and extensions. The accessor "getExpirationDate" gives direct access to the value
         */
        public MedicationBatchComponent setExpirationDateElement(DateTimeType value) { 
          this.expirationDate = value;
          return this;
        }

        /**
         * @return When this specific batch of product will expire.
         */
        public Date getExpirationDate() { 
          return this.expirationDate == null ? null : this.expirationDate.getValue();
        }

        /**
         * @param value When this specific batch of product will expire.
         */
        public MedicationBatchComponent setExpirationDate(Date value) { 
          if (value == null)
            this.expirationDate = null;
          else {
            if (this.expirationDate == null)
              this.expirationDate = new DateTimeType();
            this.expirationDate.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("lotNumber", "string", "The assigned lot number of a batch of the specified product.", 0, 1, lotNumber));
          children.add(new Property("expirationDate", "dateTime", "When this specific batch of product will expire.", 0, 1, expirationDate));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 462547450: /*lotNumber*/  return new Property("lotNumber", "string", "The assigned lot number of a batch of the specified product.", 0, 1, lotNumber);
          case -668811523: /*expirationDate*/  return new Property("expirationDate", "dateTime", "When this specific batch of product will expire.", 0, 1, expirationDate);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 462547450: /*lotNumber*/ return this.lotNumber == null ? new Base[0] : new Base[] {this.lotNumber}; // StringType
        case -668811523: /*expirationDate*/ return this.expirationDate == null ? new Base[0] : new Base[] {this.expirationDate}; // DateTimeType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 462547450: // lotNumber
          this.lotNumber = castToString(value); // StringType
          return value;
        case -668811523: // expirationDate
          this.expirationDate = castToDateTime(value); // DateTimeType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("lotNumber")) {
          this.lotNumber = castToString(value); // StringType
        } else if (name.equals("expirationDate")) {
          this.expirationDate = castToDateTime(value); // DateTimeType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 462547450:  return getLotNumberElement();
        case -668811523:  return getExpirationDateElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 462547450: /*lotNumber*/ return new String[] {"string"};
        case -668811523: /*expirationDate*/ return new String[] {"dateTime"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("lotNumber")) {
          throw new FHIRException("Cannot call addChild on a primitive type Medication.lotNumber");
        }
        else if (name.equals("expirationDate")) {
          throw new FHIRException("Cannot call addChild on a primitive type Medication.expirationDate");
        }
        else
          return super.addChild(name);
      }

      public MedicationBatchComponent copy() {
        MedicationBatchComponent dst = new MedicationBatchComponent();
        copyValues(dst);
        dst.lotNumber = lotNumber == null ? null : lotNumber.copy();
        dst.expirationDate = expirationDate == null ? null : expirationDate.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof MedicationBatchComponent))
          return false;
        MedicationBatchComponent o = (MedicationBatchComponent) other_;
        return compareDeep(lotNumber, o.lotNumber, true) && compareDeep(expirationDate, o.expirationDate, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof MedicationBatchComponent))
          return false;
        MedicationBatchComponent o = (MedicationBatchComponent) other_;
        return compareValues(lotNumber, o.lotNumber, true) && compareValues(expirationDate, o.expirationDate, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(lotNumber, expirationDate
          );
      }

  public String fhirType() {
    return "Medication.batch";

  }

  }

    /**
     * Business identifier for this medication.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Business identifier for this medication", formalDefinition="Business identifier for this medication." )
    protected List<Identifier> identifier;

    /**
     * A code (or set of codes) that specify this medication, or a textual description if no code is available. Usage note: This could be a standard medication code such as a code from RxNorm, SNOMED CT, IDMP etc. It could also be a national or local formulary code, optionally with translations to other code systems.
     */
    @Child(name = "code", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Codes that identify this medication", formalDefinition="A code (or set of codes) that specify this medication, or a textual description if no code is available. Usage note: This could be a standard medication code such as a code from RxNorm, SNOMED CT, IDMP etc. It could also be a national or local formulary code, optionally with translations to other code systems." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/medication-codes")
    protected CodeableConcept code;

    /**
     * A code to indicate if the medication is in active use.
     */
    @Child(name = "status", type = {CodeType.class}, order=2, min=0, max=1, modifier=true, summary=true)
    @Description(shortDefinition="active | inactive | entered-in-error", formalDefinition="A code to indicate if the medication is in active use." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/medication-status")
    protected Enumeration<MedicationStatus> status;

    /**
     * Describes the details of the manufacturer of the medication product.  This is not intended to represent the distributor of a medication product.
     */
    @Child(name = "manufacturer", type = {Organization.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Manufacturer of the item", formalDefinition="Describes the details of the manufacturer of the medication product.  This is not intended to represent the distributor of a medication product." )
    protected Reference manufacturer;

    /**
     * The actual object that is the target of the reference (Describes the details of the manufacturer of the medication product.  This is not intended to represent the distributor of a medication product.)
     */
    protected Organization manufacturerTarget;

    /**
     * Describes the form of the item.  Powder; tablets; capsule.
     */
    @Child(name = "form", type = {CodeableConcept.class}, order=4, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="powder | tablets | capsule +", formalDefinition="Describes the form of the item.  Powder; tablets; capsule." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/medication-form-codes")
    protected CodeableConcept form;

    /**
     * Specific amount of the drug in the packaged product.  For example, when specifying a product that has the same strength (For example, Insulin glargine 100 unit per mL solution for injection), this attribute provides additional clarification of the package amount (For example, 3 mL, 10mL, etc.).
     */
    @Child(name = "amount", type = {Ratio.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Amount of drug in package", formalDefinition="Specific amount of the drug in the packaged product.  For example, when specifying a product that has the same strength (For example, Insulin glargine 100 unit per mL solution for injection), this attribute provides additional clarification of the package amount (For example, 3 mL, 10mL, etc.)." )
    protected Ratio amount;

    /**
     * Identifies a particular constituent of interest in the product.
     */
    @Child(name = "ingredient", type = {}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Active or inactive ingredient", formalDefinition="Identifies a particular constituent of interest in the product." )
    protected List<MedicationIngredientComponent> ingredient;

    /**
     * Information that only applies to packages (not products).
     */
    @Child(name = "batch", type = {}, order=7, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Details about packaged medications", formalDefinition="Information that only applies to packages (not products)." )
    protected MedicationBatchComponent batch;

    private static final long serialVersionUID = 781229373L;

  /**
   * Constructor
   */
    public Medication() {
      super();
    }

    /**
     * @return {@link #identifier} (Business identifier for this medication.)
     */
    public List<Identifier> getIdentifier() { 
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      return this.identifier;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Medication setIdentifier(List<Identifier> theIdentifier) { 
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

    public Medication addIdentifier(Identifier t) { //3
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
     * @return {@link #code} (A code (or set of codes) that specify this medication, or a textual description if no code is available. Usage note: This could be a standard medication code such as a code from RxNorm, SNOMED CT, IDMP etc. It could also be a national or local formulary code, optionally with translations to other code systems.)
     */
    public CodeableConcept getCode() { 
      if (this.code == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Medication.code");
        else if (Configuration.doAutoCreate())
          this.code = new CodeableConcept(); // cc
      return this.code;
    }

    public boolean hasCode() { 
      return this.code != null && !this.code.isEmpty();
    }

    /**
     * @param value {@link #code} (A code (or set of codes) that specify this medication, or a textual description if no code is available. Usage note: This could be a standard medication code such as a code from RxNorm, SNOMED CT, IDMP etc. It could also be a national or local formulary code, optionally with translations to other code systems.)
     */
    public Medication setCode(CodeableConcept value) { 
      this.code = value;
      return this;
    }

    /**
     * @return {@link #status} (A code to indicate if the medication is in active use.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<MedicationStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Medication.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<MedicationStatus>(new MedicationStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (A code to indicate if the medication is in active use.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Medication setStatusElement(Enumeration<MedicationStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return A code to indicate if the medication is in active use.
     */
    public MedicationStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value A code to indicate if the medication is in active use.
     */
    public Medication setStatus(MedicationStatus value) { 
      if (value == null)
        this.status = null;
      else {
        if (this.status == null)
          this.status = new Enumeration<MedicationStatus>(new MedicationStatusEnumFactory());
        this.status.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #manufacturer} (Describes the details of the manufacturer of the medication product.  This is not intended to represent the distributor of a medication product.)
     */
    public Reference getManufacturer() { 
      if (this.manufacturer == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Medication.manufacturer");
        else if (Configuration.doAutoCreate())
          this.manufacturer = new Reference(); // cc
      return this.manufacturer;
    }

    public boolean hasManufacturer() { 
      return this.manufacturer != null && !this.manufacturer.isEmpty();
    }

    /**
     * @param value {@link #manufacturer} (Describes the details of the manufacturer of the medication product.  This is not intended to represent the distributor of a medication product.)
     */
    public Medication setManufacturer(Reference value) { 
      this.manufacturer = value;
      return this;
    }

    /**
     * @return {@link #manufacturer} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Describes the details of the manufacturer of the medication product.  This is not intended to represent the distributor of a medication product.)
     */
    public Organization getManufacturerTarget() { 
      if (this.manufacturerTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Medication.manufacturer");
        else if (Configuration.doAutoCreate())
          this.manufacturerTarget = new Organization(); // aa
      return this.manufacturerTarget;
    }

    /**
     * @param value {@link #manufacturer} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Describes the details of the manufacturer of the medication product.  This is not intended to represent the distributor of a medication product.)
     */
    public Medication setManufacturerTarget(Organization value) { 
      this.manufacturerTarget = value;
      return this;
    }

    /**
     * @return {@link #form} (Describes the form of the item.  Powder; tablets; capsule.)
     */
    public CodeableConcept getForm() { 
      if (this.form == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Medication.form");
        else if (Configuration.doAutoCreate())
          this.form = new CodeableConcept(); // cc
      return this.form;
    }

    public boolean hasForm() { 
      return this.form != null && !this.form.isEmpty();
    }

    /**
     * @param value {@link #form} (Describes the form of the item.  Powder; tablets; capsule.)
     */
    public Medication setForm(CodeableConcept value) { 
      this.form = value;
      return this;
    }

    /**
     * @return {@link #amount} (Specific amount of the drug in the packaged product.  For example, when specifying a product that has the same strength (For example, Insulin glargine 100 unit per mL solution for injection), this attribute provides additional clarification of the package amount (For example, 3 mL, 10mL, etc.).)
     */
    public Ratio getAmount() { 
      if (this.amount == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Medication.amount");
        else if (Configuration.doAutoCreate())
          this.amount = new Ratio(); // cc
      return this.amount;
    }

    public boolean hasAmount() { 
      return this.amount != null && !this.amount.isEmpty();
    }

    /**
     * @param value {@link #amount} (Specific amount of the drug in the packaged product.  For example, when specifying a product that has the same strength (For example, Insulin glargine 100 unit per mL solution for injection), this attribute provides additional clarification of the package amount (For example, 3 mL, 10mL, etc.).)
     */
    public Medication setAmount(Ratio value) { 
      this.amount = value;
      return this;
    }

    /**
     * @return {@link #ingredient} (Identifies a particular constituent of interest in the product.)
     */
    public List<MedicationIngredientComponent> getIngredient() { 
      if (this.ingredient == null)
        this.ingredient = new ArrayList<MedicationIngredientComponent>();
      return this.ingredient;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Medication setIngredient(List<MedicationIngredientComponent> theIngredient) { 
      this.ingredient = theIngredient;
      return this;
    }

    public boolean hasIngredient() { 
      if (this.ingredient == null)
        return false;
      for (MedicationIngredientComponent item : this.ingredient)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public MedicationIngredientComponent addIngredient() { //3
      MedicationIngredientComponent t = new MedicationIngredientComponent();
      if (this.ingredient == null)
        this.ingredient = new ArrayList<MedicationIngredientComponent>();
      this.ingredient.add(t);
      return t;
    }

    public Medication addIngredient(MedicationIngredientComponent t) { //3
      if (t == null)
        return this;
      if (this.ingredient == null)
        this.ingredient = new ArrayList<MedicationIngredientComponent>();
      this.ingredient.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #ingredient}, creating it if it does not already exist
     */
    public MedicationIngredientComponent getIngredientFirstRep() { 
      if (getIngredient().isEmpty()) {
        addIngredient();
      }
      return getIngredient().get(0);
    }

    /**
     * @return {@link #batch} (Information that only applies to packages (not products).)
     */
    public MedicationBatchComponent getBatch() { 
      if (this.batch == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Medication.batch");
        else if (Configuration.doAutoCreate())
          this.batch = new MedicationBatchComponent(); // cc
      return this.batch;
    }

    public boolean hasBatch() { 
      return this.batch != null && !this.batch.isEmpty();
    }

    /**
     * @param value {@link #batch} (Information that only applies to packages (not products).)
     */
    public Medication setBatch(MedicationBatchComponent value) { 
      this.batch = value;
      return this;
    }

      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("identifier", "Identifier", "Business identifier for this medication.", 0, java.lang.Integer.MAX_VALUE, identifier));
        children.add(new Property("code", "CodeableConcept", "A code (or set of codes) that specify this medication, or a textual description if no code is available. Usage note: This could be a standard medication code such as a code from RxNorm, SNOMED CT, IDMP etc. It could also be a national or local formulary code, optionally with translations to other code systems.", 0, 1, code));
        children.add(new Property("status", "code", "A code to indicate if the medication is in active use.", 0, 1, status));
        children.add(new Property("manufacturer", "Reference(Organization)", "Describes the details of the manufacturer of the medication product.  This is not intended to represent the distributor of a medication product.", 0, 1, manufacturer));
        children.add(new Property("form", "CodeableConcept", "Describes the form of the item.  Powder; tablets; capsule.", 0, 1, form));
        children.add(new Property("amount", "Ratio", "Specific amount of the drug in the packaged product.  For example, when specifying a product that has the same strength (For example, Insulin glargine 100 unit per mL solution for injection), this attribute provides additional clarification of the package amount (For example, 3 mL, 10mL, etc.).", 0, 1, amount));
        children.add(new Property("ingredient", "", "Identifies a particular constituent of interest in the product.", 0, java.lang.Integer.MAX_VALUE, ingredient));
        children.add(new Property("batch", "", "Information that only applies to packages (not products).", 0, 1, batch));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case -1618432855: /*identifier*/  return new Property("identifier", "Identifier", "Business identifier for this medication.", 0, java.lang.Integer.MAX_VALUE, identifier);
        case 3059181: /*code*/  return new Property("code", "CodeableConcept", "A code (or set of codes) that specify this medication, or a textual description if no code is available. Usage note: This could be a standard medication code such as a code from RxNorm, SNOMED CT, IDMP etc. It could also be a national or local formulary code, optionally with translations to other code systems.", 0, 1, code);
        case -892481550: /*status*/  return new Property("status", "code", "A code to indicate if the medication is in active use.", 0, 1, status);
        case -1969347631: /*manufacturer*/  return new Property("manufacturer", "Reference(Organization)", "Describes the details of the manufacturer of the medication product.  This is not intended to represent the distributor of a medication product.", 0, 1, manufacturer);
        case 3148996: /*form*/  return new Property("form", "CodeableConcept", "Describes the form of the item.  Powder; tablets; capsule.", 0, 1, form);
        case -1413853096: /*amount*/  return new Property("amount", "Ratio", "Specific amount of the drug in the packaged product.  For example, when specifying a product that has the same strength (For example, Insulin glargine 100 unit per mL solution for injection), this attribute provides additional clarification of the package amount (For example, 3 mL, 10mL, etc.).", 0, 1, amount);
        case -206409263: /*ingredient*/  return new Property("ingredient", "", "Identifies a particular constituent of interest in the product.", 0, java.lang.Integer.MAX_VALUE, ingredient);
        case 93509434: /*batch*/  return new Property("batch", "", "Information that only applies to packages (not products).", 0, 1, batch);
        default: return super.getNamedProperty(_hash, _name, _checkValid);
        }

      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case 3059181: /*code*/ return this.code == null ? new Base[0] : new Base[] {this.code}; // CodeableConcept
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<MedicationStatus>
        case -1969347631: /*manufacturer*/ return this.manufacturer == null ? new Base[0] : new Base[] {this.manufacturer}; // Reference
        case 3148996: /*form*/ return this.form == null ? new Base[0] : new Base[] {this.form}; // CodeableConcept
        case -1413853096: /*amount*/ return this.amount == null ? new Base[0] : new Base[] {this.amount}; // Ratio
        case -206409263: /*ingredient*/ return this.ingredient == null ? new Base[0] : this.ingredient.toArray(new Base[this.ingredient.size()]); // MedicationIngredientComponent
        case 93509434: /*batch*/ return this.batch == null ? new Base[0] : new Base[] {this.batch}; // MedicationBatchComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.getIdentifier().add(castToIdentifier(value)); // Identifier
          return value;
        case 3059181: // code
          this.code = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -892481550: // status
          value = new MedicationStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<MedicationStatus>
          return value;
        case -1969347631: // manufacturer
          this.manufacturer = castToReference(value); // Reference
          return value;
        case 3148996: // form
          this.form = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1413853096: // amount
          this.amount = castToRatio(value); // Ratio
          return value;
        case -206409263: // ingredient
          this.getIngredient().add((MedicationIngredientComponent) value); // MedicationIngredientComponent
          return value;
        case 93509434: // batch
          this.batch = (MedicationBatchComponent) value; // MedicationBatchComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier")) {
          this.getIdentifier().add(castToIdentifier(value));
        } else if (name.equals("code")) {
          this.code = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("status")) {
          value = new MedicationStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<MedicationStatus>
        } else if (name.equals("manufacturer")) {
          this.manufacturer = castToReference(value); // Reference
        } else if (name.equals("form")) {
          this.form = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("amount")) {
          this.amount = castToRatio(value); // Ratio
        } else if (name.equals("ingredient")) {
          this.getIngredient().add((MedicationIngredientComponent) value);
        } else if (name.equals("batch")) {
          this.batch = (MedicationBatchComponent) value; // MedicationBatchComponent
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return addIdentifier(); 
        case 3059181:  return getCode(); 
        case -892481550:  return getStatusElement();
        case -1969347631:  return getManufacturer(); 
        case 3148996:  return getForm(); 
        case -1413853096:  return getAmount(); 
        case -206409263:  return addIngredient(); 
        case 93509434:  return getBatch(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case 3059181: /*code*/ return new String[] {"CodeableConcept"};
        case -892481550: /*status*/ return new String[] {"code"};
        case -1969347631: /*manufacturer*/ return new String[] {"Reference"};
        case 3148996: /*form*/ return new String[] {"CodeableConcept"};
        case -1413853096: /*amount*/ return new String[] {"Ratio"};
        case -206409263: /*ingredient*/ return new String[] {};
        case 93509434: /*batch*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
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
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type Medication.status");
        }
        else if (name.equals("manufacturer")) {
          this.manufacturer = new Reference();
          return this.manufacturer;
        }
        else if (name.equals("form")) {
          this.form = new CodeableConcept();
          return this.form;
        }
        else if (name.equals("amount")) {
          this.amount = new Ratio();
          return this.amount;
        }
        else if (name.equals("ingredient")) {
          return addIngredient();
        }
        else if (name.equals("batch")) {
          this.batch = new MedicationBatchComponent();
          return this.batch;
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "Medication";

  }

      public Medication copy() {
        Medication dst = new Medication();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.code = code == null ? null : code.copy();
        dst.status = status == null ? null : status.copy();
        dst.manufacturer = manufacturer == null ? null : manufacturer.copy();
        dst.form = form == null ? null : form.copy();
        dst.amount = amount == null ? null : amount.copy();
        if (ingredient != null) {
          dst.ingredient = new ArrayList<MedicationIngredientComponent>();
          for (MedicationIngredientComponent i : ingredient)
            dst.ingredient.add(i.copy());
        };
        dst.batch = batch == null ? null : batch.copy();
        return dst;
      }

      protected Medication typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof Medication))
          return false;
        Medication o = (Medication) other_;
        return compareDeep(identifier, o.identifier, true) && compareDeep(code, o.code, true) && compareDeep(status, o.status, true)
           && compareDeep(manufacturer, o.manufacturer, true) && compareDeep(form, o.form, true) && compareDeep(amount, o.amount, true)
           && compareDeep(ingredient, o.ingredient, true) && compareDeep(batch, o.batch, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof Medication))
          return false;
        Medication o = (Medication) other_;
        return compareValues(status, o.status, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, code, status
          , manufacturer, form, amount, ingredient, batch);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Medication;
   }

 /**
   * Search parameter: <b>ingredient-code</b>
   * <p>
   * Description: <b>Returns medications for this ingredient code</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Medication.ingredient.itemCodeableConcept</b><br>
   * </p>
   */
  @SearchParamDefinition(name="ingredient-code", path="(Medication.ingredient.item as CodeableConcept)", description="Returns medications for this ingredient code", type="token" )
  public static final String SP_INGREDIENT_CODE = "ingredient-code";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>ingredient-code</b>
   * <p>
   * Description: <b>Returns medications for this ingredient code</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Medication.ingredient.itemCodeableConcept</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam INGREDIENT_CODE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_INGREDIENT_CODE);

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>Returns medications with this external identifier</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Medication.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="Medication.identifier", description="Returns medications with this external identifier", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>Returns medications with this external identifier</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Medication.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>code</b>
   * <p>
   * Description: <b>Returns medications for a specific code</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Medication.code</b><br>
   * </p>
   */
  @SearchParamDefinition(name="code", path="Medication.code", description="Returns medications for a specific code", type="token" )
  public static final String SP_CODE = "code";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>code</b>
   * <p>
   * Description: <b>Returns medications for a specific code</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Medication.code</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CODE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CODE);

 /**
   * Search parameter: <b>ingredient</b>
   * <p>
   * Description: <b>Returns medications for this ingredient reference</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Medication.ingredient.itemReference</b><br>
   * </p>
   */
  @SearchParamDefinition(name="ingredient", path="(Medication.ingredient.item as Reference)", description="Returns medications for this ingredient reference", type="reference", target={Medication.class, Substance.class } )
  public static final String SP_INGREDIENT = "ingredient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>ingredient</b>
   * <p>
   * Description: <b>Returns medications for this ingredient reference</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Medication.ingredient.itemReference</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam INGREDIENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_INGREDIENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Medication:ingredient</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_INGREDIENT = new ca.uhn.fhir.model.api.Include("Medication:ingredient").toLocked();

 /**
   * Search parameter: <b>form</b>
   * <p>
   * Description: <b>Returns medications for a specific dose form</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Medication.form</b><br>
   * </p>
   */
  @SearchParamDefinition(name="form", path="Medication.form", description="Returns medications for a specific dose form", type="token" )
  public static final String SP_FORM = "form";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>form</b>
   * <p>
   * Description: <b>Returns medications for a specific dose form</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Medication.form</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam FORM = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_FORM);

 /**
   * Search parameter: <b>lot-number</b>
   * <p>
   * Description: <b>Returns medications in a batch with this lot number</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Medication.batch.lotNumber</b><br>
   * </p>
   */
  @SearchParamDefinition(name="lot-number", path="Medication.batch.lotNumber", description="Returns medications in a batch with this lot number", type="token" )
  public static final String SP_LOT_NUMBER = "lot-number";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>lot-number</b>
   * <p>
   * Description: <b>Returns medications in a batch with this lot number</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Medication.batch.lotNumber</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam LOT_NUMBER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_LOT_NUMBER);

 /**
   * Search parameter: <b>expiration-date</b>
   * <p>
   * Description: <b>Returns medications in a batch with this expiration date</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Medication.batch.expirationDate</b><br>
   * </p>
   */
  @SearchParamDefinition(name="expiration-date", path="Medication.batch.expirationDate", description="Returns medications in a batch with this expiration date", type="date" )
  public static final String SP_EXPIRATION_DATE = "expiration-date";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>expiration-date</b>
   * <p>
   * Description: <b>Returns medications in a batch with this expiration date</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Medication.batch.expirationDate</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam EXPIRATION_DATE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_EXPIRATION_DATE);

 /**
   * Search parameter: <b>manufacturer</b>
   * <p>
   * Description: <b>Returns medications made or sold for this manufacturer</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Medication.manufacturer</b><br>
   * </p>
   */
  @SearchParamDefinition(name="manufacturer", path="Medication.manufacturer", description="Returns medications made or sold for this manufacturer", type="reference", target={Organization.class } )
  public static final String SP_MANUFACTURER = "manufacturer";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>manufacturer</b>
   * <p>
   * Description: <b>Returns medications made or sold for this manufacturer</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Medication.manufacturer</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam MANUFACTURER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_MANUFACTURER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Medication:manufacturer</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_MANUFACTURER = new ca.uhn.fhir.model.api.Include("Medication:manufacturer").toLocked();

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>Returns medications for this status</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Medication.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="Medication.status", description="Returns medications for this status", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>Returns medications for this status</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Medication.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);


}

