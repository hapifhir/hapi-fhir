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
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ChildOrder;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Block;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.exceptions.FHIRException;
/**
 * This resource is primarily used for the identification and definition of a medication. It covers the ingredients and the packaging for a medication.
 */
@ResourceDef(name="Medication", profile="http://hl7.org/fhir/Profile/Medication")
public class Medication extends DomainResource {

    public enum MedicationStatus {
        /**
         * The medication is available for use
         */
        ACTIVE, 
        /**
         * The medication is not available for use
         */
        INACTIVE, 
        /**
         * The medication was entered in error
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
            case ACTIVE: return "http://hl7.org/fhir/medication-status";
            case INACTIVE: return "http://hl7.org/fhir/medication-status";
            case ENTEREDINERROR: return "http://hl7.org/fhir/medication-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case ACTIVE: return "The medication is available for use";
            case INACTIVE: return "The medication is not available for use";
            case ENTEREDINERROR: return "The medication was entered in error";
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
         * The actual ingredient - either a substance (simple ingredient) or another medication.
         */
        @Child(name = "item", type = {CodeableConcept.class, Substance.class, Medication.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The product contained", formalDefinition="The actual ingredient - either a substance (simple ingredient) or another medication." )
        protected Type item;

        /**
         * Indication of whether this ingredient affects the therapeutic action of the drug.
         */
        @Child(name = "isActive", type = {BooleanType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Active ingredient indicator", formalDefinition="Indication of whether this ingredient affects the therapeutic action of the drug." )
        protected BooleanType isActive;

        /**
         * Specifies how many (or how much) of the items there are in this Medication.  For example, 250 mg per tablet.  This is expressed as a ratio where the numerator is 250mg and the denominator is 1 tablet.
         */
        @Child(name = "amount", type = {Ratio.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Quantity of ingredient present", formalDefinition="Specifies how many (or how much) of the items there are in this Medication.  For example, 250 mg per tablet.  This is expressed as a ratio where the numerator is 250mg and the denominator is 1 tablet." )
        protected Ratio amount;

        private static final long serialVersionUID = -1796655982L;

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
         * @return {@link #item} (The actual ingredient - either a substance (simple ingredient) or another medication.)
         */
        public Type getItem() { 
          return this.item;
        }

        /**
         * @return {@link #item} (The actual ingredient - either a substance (simple ingredient) or another medication.)
         */
        public CodeableConcept getItemCodeableConcept() throws FHIRException { 
          if (!(this.item instanceof CodeableConcept))
            throw new FHIRException("Type mismatch: the type CodeableConcept was expected, but "+this.item.getClass().getName()+" was encountered");
          return (CodeableConcept) this.item;
        }

        public boolean hasItemCodeableConcept() { 
          return this.item instanceof CodeableConcept;
        }

        /**
         * @return {@link #item} (The actual ingredient - either a substance (simple ingredient) or another medication.)
         */
        public Reference getItemReference() throws FHIRException { 
          if (!(this.item instanceof Reference))
            throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.item.getClass().getName()+" was encountered");
          return (Reference) this.item;
        }

        public boolean hasItemReference() { 
          return this.item instanceof Reference;
        }

        public boolean hasItem() { 
          return this.item != null && !this.item.isEmpty();
        }

        /**
         * @param value {@link #item} (The actual ingredient - either a substance (simple ingredient) or another medication.)
         */
        public MedicationIngredientComponent setItem(Type value) { 
          this.item = value;
          return this;
        }

        /**
         * @return {@link #isActive} (Indication of whether this ingredient affects the therapeutic action of the drug.). This is the underlying object with id, value and extensions. The accessor "getIsActive" gives direct access to the value
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
         * @param value {@link #isActive} (Indication of whether this ingredient affects the therapeutic action of the drug.). This is the underlying object with id, value and extensions. The accessor "getIsActive" gives direct access to the value
         */
        public MedicationIngredientComponent setIsActiveElement(BooleanType value) { 
          this.isActive = value;
          return this;
        }

        /**
         * @return Indication of whether this ingredient affects the therapeutic action of the drug.
         */
        public boolean getIsActive() { 
          return this.isActive == null || this.isActive.isEmpty() ? false : this.isActive.getValue();
        }

        /**
         * @param value Indication of whether this ingredient affects the therapeutic action of the drug.
         */
        public MedicationIngredientComponent setIsActive(boolean value) { 
            if (this.isActive == null)
              this.isActive = new BooleanType();
            this.isActive.setValue(value);
          return this;
        }

        /**
         * @return {@link #amount} (Specifies how many (or how much) of the items there are in this Medication.  For example, 250 mg per tablet.  This is expressed as a ratio where the numerator is 250mg and the denominator is 1 tablet.)
         */
        public Ratio getAmount() { 
          if (this.amount == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationIngredientComponent.amount");
            else if (Configuration.doAutoCreate())
              this.amount = new Ratio(); // cc
          return this.amount;
        }

        public boolean hasAmount() { 
          return this.amount != null && !this.amount.isEmpty();
        }

        /**
         * @param value {@link #amount} (Specifies how many (or how much) of the items there are in this Medication.  For example, 250 mg per tablet.  This is expressed as a ratio where the numerator is 250mg and the denominator is 1 tablet.)
         */
        public MedicationIngredientComponent setAmount(Ratio value) { 
          this.amount = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("item[x]", "CodeableConcept|Reference(Substance|Medication)", "The actual ingredient - either a substance (simple ingredient) or another medication.", 0, java.lang.Integer.MAX_VALUE, item));
          childrenList.add(new Property("isActive", "boolean", "Indication of whether this ingredient affects the therapeutic action of the drug.", 0, java.lang.Integer.MAX_VALUE, isActive));
          childrenList.add(new Property("amount", "Ratio", "Specifies how many (or how much) of the items there are in this Medication.  For example, 250 mg per tablet.  This is expressed as a ratio where the numerator is 250mg and the denominator is 1 tablet.", 0, java.lang.Integer.MAX_VALUE, amount));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3242771: /*item*/ return this.item == null ? new Base[0] : new Base[] {this.item}; // Type
        case -748916528: /*isActive*/ return this.isActive == null ? new Base[0] : new Base[] {this.isActive}; // BooleanType
        case -1413853096: /*amount*/ return this.amount == null ? new Base[0] : new Base[] {this.amount}; // Ratio
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
        case -1413853096: // amount
          this.amount = castToRatio(value); // Ratio
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
        } else if (name.equals("amount")) {
          this.amount = castToRatio(value); // Ratio
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
        case -1413853096:  return getAmount(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3242771: /*item*/ return new String[] {"CodeableConcept", "Reference"};
        case -748916528: /*isActive*/ return new String[] {"boolean"};
        case -1413853096: /*amount*/ return new String[] {"Ratio"};
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
        else if (name.equals("amount")) {
          this.amount = new Ratio();
          return this.amount;
        }
        else
          return super.addChild(name);
      }

      public MedicationIngredientComponent copy() {
        MedicationIngredientComponent dst = new MedicationIngredientComponent();
        copyValues(dst);
        dst.item = item == null ? null : item.copy();
        dst.isActive = isActive == null ? null : isActive.copy();
        dst.amount = amount == null ? null : amount.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof MedicationIngredientComponent))
          return false;
        MedicationIngredientComponent o = (MedicationIngredientComponent) other;
        return compareDeep(item, o.item, true) && compareDeep(isActive, o.isActive, true) && compareDeep(amount, o.amount, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof MedicationIngredientComponent))
          return false;
        MedicationIngredientComponent o = (MedicationIngredientComponent) other;
        return compareValues(isActive, o.isActive, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(item, isActive, amount);
      }

  public String fhirType() {
    return "Medication.ingredient";

  }

  }

    @Block()
    public static class MedicationPackageComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The kind of container that this package comes as.
         */
        @Child(name = "container", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="E.g. box, vial, blister-pack", formalDefinition="The kind of container that this package comes as." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/medication-package-form")
        protected CodeableConcept container;

        /**
         * A set of components that go to make up the described item.
         */
        @Child(name = "content", type = {}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="What is  in the package", formalDefinition="A set of components that go to make up the described item." )
        protected List<MedicationPackageContentComponent> content;

        /**
         * Information about a group of medication produced or packaged from one production run.
         */
        @Child(name = "batch", type = {}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Identifies a single production run", formalDefinition="Information about a group of medication produced or packaged from one production run." )
        protected List<MedicationPackageBatchComponent> batch;

        private static final long serialVersionUID = -255992250L;

    /**
     * Constructor
     */
      public MedicationPackageComponent() {
        super();
      }

        /**
         * @return {@link #container} (The kind of container that this package comes as.)
         */
        public CodeableConcept getContainer() { 
          if (this.container == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationPackageComponent.container");
            else if (Configuration.doAutoCreate())
              this.container = new CodeableConcept(); // cc
          return this.container;
        }

        public boolean hasContainer() { 
          return this.container != null && !this.container.isEmpty();
        }

        /**
         * @param value {@link #container} (The kind of container that this package comes as.)
         */
        public MedicationPackageComponent setContainer(CodeableConcept value) { 
          this.container = value;
          return this;
        }

        /**
         * @return {@link #content} (A set of components that go to make up the described item.)
         */
        public List<MedicationPackageContentComponent> getContent() { 
          if (this.content == null)
            this.content = new ArrayList<MedicationPackageContentComponent>();
          return this.content;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public MedicationPackageComponent setContent(List<MedicationPackageContentComponent> theContent) { 
          this.content = theContent;
          return this;
        }

        public boolean hasContent() { 
          if (this.content == null)
            return false;
          for (MedicationPackageContentComponent item : this.content)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public MedicationPackageContentComponent addContent() { //3
          MedicationPackageContentComponent t = new MedicationPackageContentComponent();
          if (this.content == null)
            this.content = new ArrayList<MedicationPackageContentComponent>();
          this.content.add(t);
          return t;
        }

        public MedicationPackageComponent addContent(MedicationPackageContentComponent t) { //3
          if (t == null)
            return this;
          if (this.content == null)
            this.content = new ArrayList<MedicationPackageContentComponent>();
          this.content.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #content}, creating it if it does not already exist
         */
        public MedicationPackageContentComponent getContentFirstRep() { 
          if (getContent().isEmpty()) {
            addContent();
          }
          return getContent().get(0);
        }

        /**
         * @return {@link #batch} (Information about a group of medication produced or packaged from one production run.)
         */
        public List<MedicationPackageBatchComponent> getBatch() { 
          if (this.batch == null)
            this.batch = new ArrayList<MedicationPackageBatchComponent>();
          return this.batch;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public MedicationPackageComponent setBatch(List<MedicationPackageBatchComponent> theBatch) { 
          this.batch = theBatch;
          return this;
        }

        public boolean hasBatch() { 
          if (this.batch == null)
            return false;
          for (MedicationPackageBatchComponent item : this.batch)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public MedicationPackageBatchComponent addBatch() { //3
          MedicationPackageBatchComponent t = new MedicationPackageBatchComponent();
          if (this.batch == null)
            this.batch = new ArrayList<MedicationPackageBatchComponent>();
          this.batch.add(t);
          return t;
        }

        public MedicationPackageComponent addBatch(MedicationPackageBatchComponent t) { //3
          if (t == null)
            return this;
          if (this.batch == null)
            this.batch = new ArrayList<MedicationPackageBatchComponent>();
          this.batch.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #batch}, creating it if it does not already exist
         */
        public MedicationPackageBatchComponent getBatchFirstRep() { 
          if (getBatch().isEmpty()) {
            addBatch();
          }
          return getBatch().get(0);
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("container", "CodeableConcept", "The kind of container that this package comes as.", 0, java.lang.Integer.MAX_VALUE, container));
          childrenList.add(new Property("content", "", "A set of components that go to make up the described item.", 0, java.lang.Integer.MAX_VALUE, content));
          childrenList.add(new Property("batch", "", "Information about a group of medication produced or packaged from one production run.", 0, java.lang.Integer.MAX_VALUE, batch));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -410956671: /*container*/ return this.container == null ? new Base[0] : new Base[] {this.container}; // CodeableConcept
        case 951530617: /*content*/ return this.content == null ? new Base[0] : this.content.toArray(new Base[this.content.size()]); // MedicationPackageContentComponent
        case 93509434: /*batch*/ return this.batch == null ? new Base[0] : this.batch.toArray(new Base[this.batch.size()]); // MedicationPackageBatchComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -410956671: // container
          this.container = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 951530617: // content
          this.getContent().add((MedicationPackageContentComponent) value); // MedicationPackageContentComponent
          return value;
        case 93509434: // batch
          this.getBatch().add((MedicationPackageBatchComponent) value); // MedicationPackageBatchComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("container")) {
          this.container = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("content")) {
          this.getContent().add((MedicationPackageContentComponent) value);
        } else if (name.equals("batch")) {
          this.getBatch().add((MedicationPackageBatchComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -410956671:  return getContainer(); 
        case 951530617:  return addContent(); 
        case 93509434:  return addBatch(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -410956671: /*container*/ return new String[] {"CodeableConcept"};
        case 951530617: /*content*/ return new String[] {};
        case 93509434: /*batch*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("container")) {
          this.container = new CodeableConcept();
          return this.container;
        }
        else if (name.equals("content")) {
          return addContent();
        }
        else if (name.equals("batch")) {
          return addBatch();
        }
        else
          return super.addChild(name);
      }

      public MedicationPackageComponent copy() {
        MedicationPackageComponent dst = new MedicationPackageComponent();
        copyValues(dst);
        dst.container = container == null ? null : container.copy();
        if (content != null) {
          dst.content = new ArrayList<MedicationPackageContentComponent>();
          for (MedicationPackageContentComponent i : content)
            dst.content.add(i.copy());
        };
        if (batch != null) {
          dst.batch = new ArrayList<MedicationPackageBatchComponent>();
          for (MedicationPackageBatchComponent i : batch)
            dst.batch.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof MedicationPackageComponent))
          return false;
        MedicationPackageComponent o = (MedicationPackageComponent) other;
        return compareDeep(container, o.container, true) && compareDeep(content, o.content, true) && compareDeep(batch, o.batch, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof MedicationPackageComponent))
          return false;
        MedicationPackageComponent o = (MedicationPackageComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(container, content, batch
          );
      }

  public String fhirType() {
    return "Medication.package";

  }

  }

    @Block()
    public static class MedicationPackageContentComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Identifies one of the items in the package.
         */
        @Child(name = "item", type = {CodeableConcept.class, Medication.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The item in the package", formalDefinition="Identifies one of the items in the package." )
        protected Type item;

        /**
         * The amount of the product that is in the package.
         */
        @Child(name = "amount", type = {SimpleQuantity.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Quantity present in the package", formalDefinition="The amount of the product that is in the package." )
        protected SimpleQuantity amount;

        private static final long serialVersionUID = 1669610080L;

    /**
     * Constructor
     */
      public MedicationPackageContentComponent() {
        super();
      }

    /**
     * Constructor
     */
      public MedicationPackageContentComponent(Type item) {
        super();
        this.item = item;
      }

        /**
         * @return {@link #item} (Identifies one of the items in the package.)
         */
        public Type getItem() { 
          return this.item;
        }

        /**
         * @return {@link #item} (Identifies one of the items in the package.)
         */
        public CodeableConcept getItemCodeableConcept() throws FHIRException { 
          if (!(this.item instanceof CodeableConcept))
            throw new FHIRException("Type mismatch: the type CodeableConcept was expected, but "+this.item.getClass().getName()+" was encountered");
          return (CodeableConcept) this.item;
        }

        public boolean hasItemCodeableConcept() { 
          return this.item instanceof CodeableConcept;
        }

        /**
         * @return {@link #item} (Identifies one of the items in the package.)
         */
        public Reference getItemReference() throws FHIRException { 
          if (!(this.item instanceof Reference))
            throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.item.getClass().getName()+" was encountered");
          return (Reference) this.item;
        }

        public boolean hasItemReference() { 
          return this.item instanceof Reference;
        }

        public boolean hasItem() { 
          return this.item != null && !this.item.isEmpty();
        }

        /**
         * @param value {@link #item} (Identifies one of the items in the package.)
         */
        public MedicationPackageContentComponent setItem(Type value) { 
          this.item = value;
          return this;
        }

        /**
         * @return {@link #amount} (The amount of the product that is in the package.)
         */
        public SimpleQuantity getAmount() { 
          if (this.amount == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationPackageContentComponent.amount");
            else if (Configuration.doAutoCreate())
              this.amount = new SimpleQuantity(); // cc
          return this.amount;
        }

        public boolean hasAmount() { 
          return this.amount != null && !this.amount.isEmpty();
        }

        /**
         * @param value {@link #amount} (The amount of the product that is in the package.)
         */
        public MedicationPackageContentComponent setAmount(SimpleQuantity value) { 
          this.amount = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("item[x]", "CodeableConcept|Reference(Medication)", "Identifies one of the items in the package.", 0, java.lang.Integer.MAX_VALUE, item));
          childrenList.add(new Property("amount", "SimpleQuantity", "The amount of the product that is in the package.", 0, java.lang.Integer.MAX_VALUE, amount));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3242771: /*item*/ return this.item == null ? new Base[0] : new Base[] {this.item}; // Type
        case -1413853096: /*amount*/ return this.amount == null ? new Base[0] : new Base[] {this.amount}; // SimpleQuantity
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3242771: // item
          this.item = castToType(value); // Type
          return value;
        case -1413853096: // amount
          this.amount = castToSimpleQuantity(value); // SimpleQuantity
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("item[x]")) {
          this.item = castToType(value); // Type
        } else if (name.equals("amount")) {
          this.amount = castToSimpleQuantity(value); // SimpleQuantity
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 2116201613:  return getItem(); 
        case 3242771:  return getItem(); 
        case -1413853096:  return getAmount(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3242771: /*item*/ return new String[] {"CodeableConcept", "Reference"};
        case -1413853096: /*amount*/ return new String[] {"SimpleQuantity"};
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
        else if (name.equals("amount")) {
          this.amount = new SimpleQuantity();
          return this.amount;
        }
        else
          return super.addChild(name);
      }

      public MedicationPackageContentComponent copy() {
        MedicationPackageContentComponent dst = new MedicationPackageContentComponent();
        copyValues(dst);
        dst.item = item == null ? null : item.copy();
        dst.amount = amount == null ? null : amount.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof MedicationPackageContentComponent))
          return false;
        MedicationPackageContentComponent o = (MedicationPackageContentComponent) other;
        return compareDeep(item, o.item, true) && compareDeep(amount, o.amount, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof MedicationPackageContentComponent))
          return false;
        MedicationPackageContentComponent o = (MedicationPackageContentComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(item, amount);
      }

  public String fhirType() {
    return "Medication.package.content";

  }

  }

    @Block()
    public static class MedicationPackageBatchComponent extends BackboneElement implements IBaseBackboneElement {
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
      public MedicationPackageBatchComponent() {
        super();
      }

        /**
         * @return {@link #lotNumber} (The assigned lot number of a batch of the specified product.). This is the underlying object with id, value and extensions. The accessor "getLotNumber" gives direct access to the value
         */
        public StringType getLotNumberElement() { 
          if (this.lotNumber == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationPackageBatchComponent.lotNumber");
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
        public MedicationPackageBatchComponent setLotNumberElement(StringType value) { 
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
        public MedicationPackageBatchComponent setLotNumber(String value) { 
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
              throw new Error("Attempt to auto-create MedicationPackageBatchComponent.expirationDate");
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
        public MedicationPackageBatchComponent setExpirationDateElement(DateTimeType value) { 
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
        public MedicationPackageBatchComponent setExpirationDate(Date value) { 
          if (value == null)
            this.expirationDate = null;
          else {
            if (this.expirationDate == null)
              this.expirationDate = new DateTimeType();
            this.expirationDate.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("lotNumber", "string", "The assigned lot number of a batch of the specified product.", 0, java.lang.Integer.MAX_VALUE, lotNumber));
          childrenList.add(new Property("expirationDate", "dateTime", "When this specific batch of product will expire.", 0, java.lang.Integer.MAX_VALUE, expirationDate));
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

      public MedicationPackageBatchComponent copy() {
        MedicationPackageBatchComponent dst = new MedicationPackageBatchComponent();
        copyValues(dst);
        dst.lotNumber = lotNumber == null ? null : lotNumber.copy();
        dst.expirationDate = expirationDate == null ? null : expirationDate.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof MedicationPackageBatchComponent))
          return false;
        MedicationPackageBatchComponent o = (MedicationPackageBatchComponent) other;
        return compareDeep(lotNumber, o.lotNumber, true) && compareDeep(expirationDate, o.expirationDate, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof MedicationPackageBatchComponent))
          return false;
        MedicationPackageBatchComponent o = (MedicationPackageBatchComponent) other;
        return compareValues(lotNumber, o.lotNumber, true) && compareValues(expirationDate, o.expirationDate, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(lotNumber, expirationDate
          );
      }

  public String fhirType() {
    return "Medication.package.batch";

  }

  }

    /**
     * A code (or set of codes) that specify this medication, or a textual description if no code is available. Usage note: This could be a standard medication code such as a code from RxNorm, SNOMED CT, IDMP etc. It could also be a national or local formulary code, optionally with translations to other code systems.
     */
    @Child(name = "code", type = {CodeableConcept.class}, order=0, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Codes that identify this medication", formalDefinition="A code (or set of codes) that specify this medication, or a textual description if no code is available. Usage note: This could be a standard medication code such as a code from RxNorm, SNOMED CT, IDMP etc. It could also be a national or local formulary code, optionally with translations to other code systems." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/medication-codes")
    protected CodeableConcept code;

    /**
     * A code to indicate if the medication is in active use.
     */
    @Child(name = "status", type = {CodeType.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="active | inactive | entered-in-error", formalDefinition="A code to indicate if the medication is in active use." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/medication-status")
    protected Enumeration<MedicationStatus> status;

    /**
     * Set to true if the item is attributable to a specific manufacturer.
     */
    @Child(name = "isBrand", type = {BooleanType.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="True if a brand", formalDefinition="Set to true if the item is attributable to a specific manufacturer." )
    protected BooleanType isBrand;

    /**
     * Set to true if the medication can be obtained without an order from a prescriber.
     */
    @Child(name = "isOverTheCounter", type = {BooleanType.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="True if medication does not require a prescription", formalDefinition="Set to true if the medication can be obtained without an order from a prescriber." )
    protected BooleanType isOverTheCounter;

    /**
     * Describes the details of the manufacturer of the medication product.  This is not intended to represent the distributor of a medication product.
     */
    @Child(name = "manufacturer", type = {Organization.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Manufacturer of the item", formalDefinition="Describes the details of the manufacturer of the medication product.  This is not intended to represent the distributor of a medication product." )
    protected Reference manufacturer;

    /**
     * The actual object that is the target of the reference (Describes the details of the manufacturer of the medication product.  This is not intended to represent the distributor of a medication product.)
     */
    protected Organization manufacturerTarget;

    /**
     * Describes the form of the item.  Powder; tablets; capsule.
     */
    @Child(name = "form", type = {CodeableConcept.class}, order=5, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="powder | tablets | capsule +", formalDefinition="Describes the form of the item.  Powder; tablets; capsule." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/medication-form-codes")
    protected CodeableConcept form;

    /**
     * Identifies a particular constituent of interest in the product.
     */
    @Child(name = "ingredient", type = {}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Active or inactive ingredient", formalDefinition="Identifies a particular constituent of interest in the product." )
    protected List<MedicationIngredientComponent> ingredient;

    /**
     * Information that only applies to packages (not products).
     */
    @Child(name = "package", type = {}, order=7, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Details about packaged medications", formalDefinition="Information that only applies to packages (not products)." )
    protected MedicationPackageComponent package_;

    /**
     * Photo(s) or graphic representation(s) of the medication.
     */
    @Child(name = "image", type = {Attachment.class}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Picture of the medication", formalDefinition="Photo(s) or graphic representation(s) of the medication." )
    protected List<Attachment> image;

    private static final long serialVersionUID = 860383645L;

  /**
   * Constructor
   */
    public Medication() {
      super();
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
     * @return {@link #isBrand} (Set to true if the item is attributable to a specific manufacturer.). This is the underlying object with id, value and extensions. The accessor "getIsBrand" gives direct access to the value
     */
    public BooleanType getIsBrandElement() { 
      if (this.isBrand == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Medication.isBrand");
        else if (Configuration.doAutoCreate())
          this.isBrand = new BooleanType(); // bb
      return this.isBrand;
    }

    public boolean hasIsBrandElement() { 
      return this.isBrand != null && !this.isBrand.isEmpty();
    }

    public boolean hasIsBrand() { 
      return this.isBrand != null && !this.isBrand.isEmpty();
    }

    /**
     * @param value {@link #isBrand} (Set to true if the item is attributable to a specific manufacturer.). This is the underlying object with id, value and extensions. The accessor "getIsBrand" gives direct access to the value
     */
    public Medication setIsBrandElement(BooleanType value) { 
      this.isBrand = value;
      return this;
    }

    /**
     * @return Set to true if the item is attributable to a specific manufacturer.
     */
    public boolean getIsBrand() { 
      return this.isBrand == null || this.isBrand.isEmpty() ? false : this.isBrand.getValue();
    }

    /**
     * @param value Set to true if the item is attributable to a specific manufacturer.
     */
    public Medication setIsBrand(boolean value) { 
        if (this.isBrand == null)
          this.isBrand = new BooleanType();
        this.isBrand.setValue(value);
      return this;
    }

    /**
     * @return {@link #isOverTheCounter} (Set to true if the medication can be obtained without an order from a prescriber.). This is the underlying object with id, value and extensions. The accessor "getIsOverTheCounter" gives direct access to the value
     */
    public BooleanType getIsOverTheCounterElement() { 
      if (this.isOverTheCounter == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Medication.isOverTheCounter");
        else if (Configuration.doAutoCreate())
          this.isOverTheCounter = new BooleanType(); // bb
      return this.isOverTheCounter;
    }

    public boolean hasIsOverTheCounterElement() { 
      return this.isOverTheCounter != null && !this.isOverTheCounter.isEmpty();
    }

    public boolean hasIsOverTheCounter() { 
      return this.isOverTheCounter != null && !this.isOverTheCounter.isEmpty();
    }

    /**
     * @param value {@link #isOverTheCounter} (Set to true if the medication can be obtained without an order from a prescriber.). This is the underlying object with id, value and extensions. The accessor "getIsOverTheCounter" gives direct access to the value
     */
    public Medication setIsOverTheCounterElement(BooleanType value) { 
      this.isOverTheCounter = value;
      return this;
    }

    /**
     * @return Set to true if the medication can be obtained without an order from a prescriber.
     */
    public boolean getIsOverTheCounter() { 
      return this.isOverTheCounter == null || this.isOverTheCounter.isEmpty() ? false : this.isOverTheCounter.getValue();
    }

    /**
     * @param value Set to true if the medication can be obtained without an order from a prescriber.
     */
    public Medication setIsOverTheCounter(boolean value) { 
        if (this.isOverTheCounter == null)
          this.isOverTheCounter = new BooleanType();
        this.isOverTheCounter.setValue(value);
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
     * @return {@link #package_} (Information that only applies to packages (not products).)
     */
    public MedicationPackageComponent getPackage() { 
      if (this.package_ == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Medication.package_");
        else if (Configuration.doAutoCreate())
          this.package_ = new MedicationPackageComponent(); // cc
      return this.package_;
    }

    public boolean hasPackage() { 
      return this.package_ != null && !this.package_.isEmpty();
    }

    /**
     * @param value {@link #package_} (Information that only applies to packages (not products).)
     */
    public Medication setPackage(MedicationPackageComponent value) { 
      this.package_ = value;
      return this;
    }

    /**
     * @return {@link #image} (Photo(s) or graphic representation(s) of the medication.)
     */
    public List<Attachment> getImage() { 
      if (this.image == null)
        this.image = new ArrayList<Attachment>();
      return this.image;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Medication setImage(List<Attachment> theImage) { 
      this.image = theImage;
      return this;
    }

    public boolean hasImage() { 
      if (this.image == null)
        return false;
      for (Attachment item : this.image)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Attachment addImage() { //3
      Attachment t = new Attachment();
      if (this.image == null)
        this.image = new ArrayList<Attachment>();
      this.image.add(t);
      return t;
    }

    public Medication addImage(Attachment t) { //3
      if (t == null)
        return this;
      if (this.image == null)
        this.image = new ArrayList<Attachment>();
      this.image.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #image}, creating it if it does not already exist
     */
    public Attachment getImageFirstRep() { 
      if (getImage().isEmpty()) {
        addImage();
      }
      return getImage().get(0);
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("code", "CodeableConcept", "A code (or set of codes) that specify this medication, or a textual description if no code is available. Usage note: This could be a standard medication code such as a code from RxNorm, SNOMED CT, IDMP etc. It could also be a national or local formulary code, optionally with translations to other code systems.", 0, java.lang.Integer.MAX_VALUE, code));
        childrenList.add(new Property("status", "code", "A code to indicate if the medication is in active use.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("isBrand", "boolean", "Set to true if the item is attributable to a specific manufacturer.", 0, java.lang.Integer.MAX_VALUE, isBrand));
        childrenList.add(new Property("isOverTheCounter", "boolean", "Set to true if the medication can be obtained without an order from a prescriber.", 0, java.lang.Integer.MAX_VALUE, isOverTheCounter));
        childrenList.add(new Property("manufacturer", "Reference(Organization)", "Describes the details of the manufacturer of the medication product.  This is not intended to represent the distributor of a medication product.", 0, java.lang.Integer.MAX_VALUE, manufacturer));
        childrenList.add(new Property("form", "CodeableConcept", "Describes the form of the item.  Powder; tablets; capsule.", 0, java.lang.Integer.MAX_VALUE, form));
        childrenList.add(new Property("ingredient", "", "Identifies a particular constituent of interest in the product.", 0, java.lang.Integer.MAX_VALUE, ingredient));
        childrenList.add(new Property("package", "", "Information that only applies to packages (not products).", 0, java.lang.Integer.MAX_VALUE, package_));
        childrenList.add(new Property("image", "Attachment", "Photo(s) or graphic representation(s) of the medication.", 0, java.lang.Integer.MAX_VALUE, image));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3059181: /*code*/ return this.code == null ? new Base[0] : new Base[] {this.code}; // CodeableConcept
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<MedicationStatus>
        case 2055403645: /*isBrand*/ return this.isBrand == null ? new Base[0] : new Base[] {this.isBrand}; // BooleanType
        case -650796023: /*isOverTheCounter*/ return this.isOverTheCounter == null ? new Base[0] : new Base[] {this.isOverTheCounter}; // BooleanType
        case -1969347631: /*manufacturer*/ return this.manufacturer == null ? new Base[0] : new Base[] {this.manufacturer}; // Reference
        case 3148996: /*form*/ return this.form == null ? new Base[0] : new Base[] {this.form}; // CodeableConcept
        case -206409263: /*ingredient*/ return this.ingredient == null ? new Base[0] : this.ingredient.toArray(new Base[this.ingredient.size()]); // MedicationIngredientComponent
        case -807062458: /*package*/ return this.package_ == null ? new Base[0] : new Base[] {this.package_}; // MedicationPackageComponent
        case 100313435: /*image*/ return this.image == null ? new Base[0] : this.image.toArray(new Base[this.image.size()]); // Attachment
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3059181: // code
          this.code = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -892481550: // status
          value = new MedicationStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<MedicationStatus>
          return value;
        case 2055403645: // isBrand
          this.isBrand = castToBoolean(value); // BooleanType
          return value;
        case -650796023: // isOverTheCounter
          this.isOverTheCounter = castToBoolean(value); // BooleanType
          return value;
        case -1969347631: // manufacturer
          this.manufacturer = castToReference(value); // Reference
          return value;
        case 3148996: // form
          this.form = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -206409263: // ingredient
          this.getIngredient().add((MedicationIngredientComponent) value); // MedicationIngredientComponent
          return value;
        case -807062458: // package
          this.package_ = (MedicationPackageComponent) value; // MedicationPackageComponent
          return value;
        case 100313435: // image
          this.getImage().add(castToAttachment(value)); // Attachment
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("code")) {
          this.code = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("status")) {
          value = new MedicationStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<MedicationStatus>
        } else if (name.equals("isBrand")) {
          this.isBrand = castToBoolean(value); // BooleanType
        } else if (name.equals("isOverTheCounter")) {
          this.isOverTheCounter = castToBoolean(value); // BooleanType
        } else if (name.equals("manufacturer")) {
          this.manufacturer = castToReference(value); // Reference
        } else if (name.equals("form")) {
          this.form = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("ingredient")) {
          this.getIngredient().add((MedicationIngredientComponent) value);
        } else if (name.equals("package")) {
          this.package_ = (MedicationPackageComponent) value; // MedicationPackageComponent
        } else if (name.equals("image")) {
          this.getImage().add(castToAttachment(value));
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3059181:  return getCode(); 
        case -892481550:  return getStatusElement();
        case 2055403645:  return getIsBrandElement();
        case -650796023:  return getIsOverTheCounterElement();
        case -1969347631:  return getManufacturer(); 
        case 3148996:  return getForm(); 
        case -206409263:  return addIngredient(); 
        case -807062458:  return getPackage(); 
        case 100313435:  return addImage(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3059181: /*code*/ return new String[] {"CodeableConcept"};
        case -892481550: /*status*/ return new String[] {"code"};
        case 2055403645: /*isBrand*/ return new String[] {"boolean"};
        case -650796023: /*isOverTheCounter*/ return new String[] {"boolean"};
        case -1969347631: /*manufacturer*/ return new String[] {"Reference"};
        case 3148996: /*form*/ return new String[] {"CodeableConcept"};
        case -206409263: /*ingredient*/ return new String[] {};
        case -807062458: /*package*/ return new String[] {};
        case 100313435: /*image*/ return new String[] {"Attachment"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("code")) {
          this.code = new CodeableConcept();
          return this.code;
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type Medication.status");
        }
        else if (name.equals("isBrand")) {
          throw new FHIRException("Cannot call addChild on a primitive type Medication.isBrand");
        }
        else if (name.equals("isOverTheCounter")) {
          throw new FHIRException("Cannot call addChild on a primitive type Medication.isOverTheCounter");
        }
        else if (name.equals("manufacturer")) {
          this.manufacturer = new Reference();
          return this.manufacturer;
        }
        else if (name.equals("form")) {
          this.form = new CodeableConcept();
          return this.form;
        }
        else if (name.equals("ingredient")) {
          return addIngredient();
        }
        else if (name.equals("package")) {
          this.package_ = new MedicationPackageComponent();
          return this.package_;
        }
        else if (name.equals("image")) {
          return addImage();
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
        dst.code = code == null ? null : code.copy();
        dst.status = status == null ? null : status.copy();
        dst.isBrand = isBrand == null ? null : isBrand.copy();
        dst.isOverTheCounter = isOverTheCounter == null ? null : isOverTheCounter.copy();
        dst.manufacturer = manufacturer == null ? null : manufacturer.copy();
        dst.form = form == null ? null : form.copy();
        if (ingredient != null) {
          dst.ingredient = new ArrayList<MedicationIngredientComponent>();
          for (MedicationIngredientComponent i : ingredient)
            dst.ingredient.add(i.copy());
        };
        dst.package_ = package_ == null ? null : package_.copy();
        if (image != null) {
          dst.image = new ArrayList<Attachment>();
          for (Attachment i : image)
            dst.image.add(i.copy());
        };
        return dst;
      }

      protected Medication typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof Medication))
          return false;
        Medication o = (Medication) other;
        return compareDeep(code, o.code, true) && compareDeep(status, o.status, true) && compareDeep(isBrand, o.isBrand, true)
           && compareDeep(isOverTheCounter, o.isOverTheCounter, true) && compareDeep(manufacturer, o.manufacturer, true)
           && compareDeep(form, o.form, true) && compareDeep(ingredient, o.ingredient, true) && compareDeep(package_, o.package_, true)
           && compareDeep(image, o.image, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Medication))
          return false;
        Medication o = (Medication) other;
        return compareValues(status, o.status, true) && compareValues(isBrand, o.isBrand, true) && compareValues(isOverTheCounter, o.isOverTheCounter, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(code, status, isBrand, isOverTheCounter
          , manufacturer, form, ingredient, package_, image);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Medication;
   }

 /**
   * Search parameter: <b>ingredient-code</b>
   * <p>
   * Description: <b>The product contained</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Medication.ingredient.itemCodeableConcept</b><br>
   * </p>
   */
  @SearchParamDefinition(name="ingredient-code", path="Medication.ingredient.item.as(CodeableConcept)", description="The product contained", type="token" )
  public static final String SP_INGREDIENT_CODE = "ingredient-code";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>ingredient-code</b>
   * <p>
   * Description: <b>The product contained</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Medication.ingredient.itemCodeableConcept</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam INGREDIENT_CODE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_INGREDIENT_CODE);

 /**
   * Search parameter: <b>container</b>
   * <p>
   * Description: <b>E.g. box, vial, blister-pack</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Medication.package.container</b><br>
   * </p>
   */
  @SearchParamDefinition(name="container", path="Medication.package.container", description="E.g. box, vial, blister-pack", type="token" )
  public static final String SP_CONTAINER = "container";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>container</b>
   * <p>
   * Description: <b>E.g. box, vial, blister-pack</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Medication.package.container</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CONTAINER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CONTAINER);

 /**
   * Search parameter: <b>package-item</b>
   * <p>
   * Description: <b>The item in the package</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Medication.package.content.itemReference</b><br>
   * </p>
   */
  @SearchParamDefinition(name="package-item", path="Medication.package.content.item.as(Reference)", description="The item in the package", type="reference", target={Medication.class } )
  public static final String SP_PACKAGE_ITEM = "package-item";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>package-item</b>
   * <p>
   * Description: <b>The item in the package</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Medication.package.content.itemReference</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PACKAGE_ITEM = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PACKAGE_ITEM);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Medication:package-item</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PACKAGE_ITEM = new ca.uhn.fhir.model.api.Include("Medication:package-item").toLocked();

 /**
   * Search parameter: <b>code</b>
   * <p>
   * Description: <b>Codes that identify this medication</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Medication.code</b><br>
   * </p>
   */
  @SearchParamDefinition(name="code", path="Medication.code", description="Codes that identify this medication", type="token" )
  public static final String SP_CODE = "code";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>code</b>
   * <p>
   * Description: <b>Codes that identify this medication</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Medication.code</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CODE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CODE);

 /**
   * Search parameter: <b>ingredient</b>
   * <p>
   * Description: <b>The product contained</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Medication.ingredient.itemReference</b><br>
   * </p>
   */
  @SearchParamDefinition(name="ingredient", path="Medication.ingredient.item.as(Reference)", description="The product contained", type="reference", target={Medication.class, Substance.class } )
  public static final String SP_INGREDIENT = "ingredient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>ingredient</b>
   * <p>
   * Description: <b>The product contained</b><br>
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
   * Description: <b>powder | tablets | capsule +</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Medication.form</b><br>
   * </p>
   */
  @SearchParamDefinition(name="form", path="Medication.form", description="powder | tablets | capsule +", type="token" )
  public static final String SP_FORM = "form";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>form</b>
   * <p>
   * Description: <b>powder | tablets | capsule +</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Medication.form</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam FORM = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_FORM);

 /**
   * Search parameter: <b>package-item-code</b>
   * <p>
   * Description: <b>The item in the package</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Medication.package.content.itemCodeableConcept</b><br>
   * </p>
   */
  @SearchParamDefinition(name="package-item-code", path="Medication.package.content.item.as(CodeableConcept)", description="The item in the package", type="token" )
  public static final String SP_PACKAGE_ITEM_CODE = "package-item-code";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>package-item-code</b>
   * <p>
   * Description: <b>The item in the package</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Medication.package.content.itemCodeableConcept</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam PACKAGE_ITEM_CODE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_PACKAGE_ITEM_CODE);

 /**
   * Search parameter: <b>manufacturer</b>
   * <p>
   * Description: <b>Manufacturer of the item</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Medication.manufacturer</b><br>
   * </p>
   */
  @SearchParamDefinition(name="manufacturer", path="Medication.manufacturer", description="Manufacturer of the item", type="reference", target={Organization.class } )
  public static final String SP_MANUFACTURER = "manufacturer";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>manufacturer</b>
   * <p>
   * Description: <b>Manufacturer of the item</b><br>
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
   * Search parameter: <b>over-the-counter</b>
   * <p>
   * Description: <b>True if medication does not require a prescription</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Medication.isOverTheCounter</b><br>
   * </p>
   */
  @SearchParamDefinition(name="over-the-counter", path="Medication.isOverTheCounter", description="True if medication does not require a prescription", type="token" )
  public static final String SP_OVER_THE_COUNTER = "over-the-counter";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>over-the-counter</b>
   * <p>
   * Description: <b>True if medication does not require a prescription</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Medication.isOverTheCounter</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam OVER_THE_COUNTER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_OVER_THE_COUNTER);

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>active | inactive | entered-in-error</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Medication.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="Medication.status", description="active | inactive | entered-in-error", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>active | inactive | entered-in-error</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Medication.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);


}

