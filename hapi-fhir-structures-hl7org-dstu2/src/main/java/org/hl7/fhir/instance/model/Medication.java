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
import org.hl7.fhir.instance.model.annotations.ResourceDef;
import org.hl7.fhir.instance.model.annotations.SearchParamDefinition;
import org.hl7.fhir.instance.model.annotations.Child;
import org.hl7.fhir.instance.model.annotations.Description;
import org.hl7.fhir.instance.model.annotations.Block;
import org.hl7.fhir.instance.model.api.*;
/**
 * Primarily used for identification and definition of Medication, but also covers ingredients and packaging.
 */
@ResourceDef(name="Medication", profile="http://hl7.org/fhir/Profile/Medication")
public class Medication extends DomainResource {

    public enum MedicationKind {
        /**
         * The medication is a product
         */
        PRODUCT, 
        /**
         * The medication is a package - a contained group of one of more products
         */
        PACKAGE, 
        /**
         * added to help the parsers
         */
        NULL;
        public static MedicationKind fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("product".equals(codeString))
          return PRODUCT;
        if ("package".equals(codeString))
          return PACKAGE;
        throw new Exception("Unknown MedicationKind code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PRODUCT: return "product";
            case PACKAGE: return "package";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case PRODUCT: return "http://hl7.org/fhir/medication-kind";
            case PACKAGE: return "http://hl7.org/fhir/medication-kind";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case PRODUCT: return "The medication is a product";
            case PACKAGE: return "The medication is a package - a contained group of one of more products";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PRODUCT: return "Product";
            case PACKAGE: return "Package";
            default: return "?";
          }
        }
    }

  public static class MedicationKindEnumFactory implements EnumFactory<MedicationKind> {
    public MedicationKind fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("product".equals(codeString))
          return MedicationKind.PRODUCT;
        if ("package".equals(codeString))
          return MedicationKind.PACKAGE;
        throw new IllegalArgumentException("Unknown MedicationKind code '"+codeString+"'");
        }
    public String toCode(MedicationKind code) {
      if (code == MedicationKind.PRODUCT)
        return "product";
      if (code == MedicationKind.PACKAGE)
        return "package";
      return "?";
      }
    }

    @Block()
    public static class MedicationProductComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Describes the form of the item.  Powder; tablets; carton.
         */
        @Child(name = "form", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="powder | tablets | carton +", formalDefinition="Describes the form of the item.  Powder; tablets; carton." )
        protected CodeableConcept form;

        /**
         * Identifies a particular constituent of interest in the product.
         */
        @Child(name = "ingredient", type = {}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Active or inactive ingredient", formalDefinition="Identifies a particular constituent of interest in the product." )
        protected List<MedicationProductIngredientComponent> ingredient;

        /**
         * Information about a group of medication produced or packaged from one production run.
         */
        @Child(name = "batch", type = {}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="", formalDefinition="Information about a group of medication produced or packaged from one production run." )
        protected List<MedicationProductBatchComponent> batch;

        private static final long serialVersionUID = 1132853671L;

    /*
     * Constructor
     */
      public MedicationProductComponent() {
        super();
      }

        /**
         * @return {@link #form} (Describes the form of the item.  Powder; tablets; carton.)
         */
        public CodeableConcept getForm() { 
          if (this.form == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationProductComponent.form");
            else if (Configuration.doAutoCreate())
              this.form = new CodeableConcept(); // cc
          return this.form;
        }

        public boolean hasForm() { 
          return this.form != null && !this.form.isEmpty();
        }

        /**
         * @param value {@link #form} (Describes the form of the item.  Powder; tablets; carton.)
         */
        public MedicationProductComponent setForm(CodeableConcept value) { 
          this.form = value;
          return this;
        }

        /**
         * @return {@link #ingredient} (Identifies a particular constituent of interest in the product.)
         */
        public List<MedicationProductIngredientComponent> getIngredient() { 
          if (this.ingredient == null)
            this.ingredient = new ArrayList<MedicationProductIngredientComponent>();
          return this.ingredient;
        }

        public boolean hasIngredient() { 
          if (this.ingredient == null)
            return false;
          for (MedicationProductIngredientComponent item : this.ingredient)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #ingredient} (Identifies a particular constituent of interest in the product.)
         */
    // syntactic sugar
        public MedicationProductIngredientComponent addIngredient() { //3
          MedicationProductIngredientComponent t = new MedicationProductIngredientComponent();
          if (this.ingredient == null)
            this.ingredient = new ArrayList<MedicationProductIngredientComponent>();
          this.ingredient.add(t);
          return t;
        }

    // syntactic sugar
        public MedicationProductComponent addIngredient(MedicationProductIngredientComponent t) { //3
          if (t == null)
            return this;
          if (this.ingredient == null)
            this.ingredient = new ArrayList<MedicationProductIngredientComponent>();
          this.ingredient.add(t);
          return this;
        }

        /**
         * @return {@link #batch} (Information about a group of medication produced or packaged from one production run.)
         */
        public List<MedicationProductBatchComponent> getBatch() { 
          if (this.batch == null)
            this.batch = new ArrayList<MedicationProductBatchComponent>();
          return this.batch;
        }

        public boolean hasBatch() { 
          if (this.batch == null)
            return false;
          for (MedicationProductBatchComponent item : this.batch)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #batch} (Information about a group of medication produced or packaged from one production run.)
         */
    // syntactic sugar
        public MedicationProductBatchComponent addBatch() { //3
          MedicationProductBatchComponent t = new MedicationProductBatchComponent();
          if (this.batch == null)
            this.batch = new ArrayList<MedicationProductBatchComponent>();
          this.batch.add(t);
          return t;
        }

    // syntactic sugar
        public MedicationProductComponent addBatch(MedicationProductBatchComponent t) { //3
          if (t == null)
            return this;
          if (this.batch == null)
            this.batch = new ArrayList<MedicationProductBatchComponent>();
          this.batch.add(t);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("form", "CodeableConcept", "Describes the form of the item.  Powder; tablets; carton.", 0, java.lang.Integer.MAX_VALUE, form));
          childrenList.add(new Property("ingredient", "", "Identifies a particular constituent of interest in the product.", 0, java.lang.Integer.MAX_VALUE, ingredient));
          childrenList.add(new Property("batch", "", "Information about a group of medication produced or packaged from one production run.", 0, java.lang.Integer.MAX_VALUE, batch));
        }

      public MedicationProductComponent copy() {
        MedicationProductComponent dst = new MedicationProductComponent();
        copyValues(dst);
        dst.form = form == null ? null : form.copy();
        if (ingredient != null) {
          dst.ingredient = new ArrayList<MedicationProductIngredientComponent>();
          for (MedicationProductIngredientComponent i : ingredient)
            dst.ingredient.add(i.copy());
        };
        if (batch != null) {
          dst.batch = new ArrayList<MedicationProductBatchComponent>();
          for (MedicationProductBatchComponent i : batch)
            dst.batch.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof MedicationProductComponent))
          return false;
        MedicationProductComponent o = (MedicationProductComponent) other;
        return compareDeep(form, o.form, true) && compareDeep(ingredient, o.ingredient, true) && compareDeep(batch, o.batch, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof MedicationProductComponent))
          return false;
        MedicationProductComponent o = (MedicationProductComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (form == null || form.isEmpty()) && (ingredient == null || ingredient.isEmpty())
           && (batch == null || batch.isEmpty());
      }

  }

    @Block()
    public static class MedicationProductIngredientComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The actual ingredient - either a substance (simple ingredient) or another medication.
         */
        @Child(name = "item", type = {Substance.class, Medication.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The product contained", formalDefinition="The actual ingredient - either a substance (simple ingredient) or another medication." )
        protected Reference item;

        /**
         * The actual object that is the target of the reference (The actual ingredient - either a substance (simple ingredient) or another medication.)
         */
        protected Resource itemTarget;

        /**
         * Specifies how many (or how much) of the items there are in this Medication.  E.g. 250 mg per tablet.
         */
        @Child(name = "amount", type = {Ratio.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="How much ingredient in product", formalDefinition="Specifies how many (or how much) of the items there are in this Medication.  E.g. 250 mg per tablet." )
        protected Ratio amount;

        private static final long serialVersionUID = -1217232889L;

    /*
     * Constructor
     */
      public MedicationProductIngredientComponent() {
        super();
      }

    /*
     * Constructor
     */
      public MedicationProductIngredientComponent(Reference item) {
        super();
        this.item = item;
      }

        /**
         * @return {@link #item} (The actual ingredient - either a substance (simple ingredient) or another medication.)
         */
        public Reference getItem() { 
          if (this.item == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationProductIngredientComponent.item");
            else if (Configuration.doAutoCreate())
              this.item = new Reference(); // cc
          return this.item;
        }

        public boolean hasItem() { 
          return this.item != null && !this.item.isEmpty();
        }

        /**
         * @param value {@link #item} (The actual ingredient - either a substance (simple ingredient) or another medication.)
         */
        public MedicationProductIngredientComponent setItem(Reference value) { 
          this.item = value;
          return this;
        }

        /**
         * @return {@link #item} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The actual ingredient - either a substance (simple ingredient) or another medication.)
         */
        public Resource getItemTarget() { 
          return this.itemTarget;
        }

        /**
         * @param value {@link #item} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The actual ingredient - either a substance (simple ingredient) or another medication.)
         */
        public MedicationProductIngredientComponent setItemTarget(Resource value) { 
          this.itemTarget = value;
          return this;
        }

        /**
         * @return {@link #amount} (Specifies how many (or how much) of the items there are in this Medication.  E.g. 250 mg per tablet.)
         */
        public Ratio getAmount() { 
          if (this.amount == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationProductIngredientComponent.amount");
            else if (Configuration.doAutoCreate())
              this.amount = new Ratio(); // cc
          return this.amount;
        }

        public boolean hasAmount() { 
          return this.amount != null && !this.amount.isEmpty();
        }

        /**
         * @param value {@link #amount} (Specifies how many (or how much) of the items there are in this Medication.  E.g. 250 mg per tablet.)
         */
        public MedicationProductIngredientComponent setAmount(Ratio value) { 
          this.amount = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("item", "Reference(Substance|Medication)", "The actual ingredient - either a substance (simple ingredient) or another medication.", 0, java.lang.Integer.MAX_VALUE, item));
          childrenList.add(new Property("amount", "Ratio", "Specifies how many (or how much) of the items there are in this Medication.  E.g. 250 mg per tablet.", 0, java.lang.Integer.MAX_VALUE, amount));
        }

      public MedicationProductIngredientComponent copy() {
        MedicationProductIngredientComponent dst = new MedicationProductIngredientComponent();
        copyValues(dst);
        dst.item = item == null ? null : item.copy();
        dst.amount = amount == null ? null : amount.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof MedicationProductIngredientComponent))
          return false;
        MedicationProductIngredientComponent o = (MedicationProductIngredientComponent) other;
        return compareDeep(item, o.item, true) && compareDeep(amount, o.amount, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof MedicationProductIngredientComponent))
          return false;
        MedicationProductIngredientComponent o = (MedicationProductIngredientComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (item == null || item.isEmpty()) && (amount == null || amount.isEmpty())
          ;
      }

  }

    @Block()
    public static class MedicationProductBatchComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The assigned lot number of a batch of the specified product.
         */
        @Child(name = "lotNumber", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="", formalDefinition="The assigned lot number of a batch of the specified product." )
        protected StringType lotNumber;

        /**
         * When this specific batch of product will expire.
         */
        @Child(name = "expirationDate", type = {DateTimeType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="", formalDefinition="When this specific batch of product will expire." )
        protected DateTimeType expirationDate;

        private static final long serialVersionUID = 1982738755L;

    /*
     * Constructor
     */
      public MedicationProductBatchComponent() {
        super();
      }

        /**
         * @return {@link #lotNumber} (The assigned lot number of a batch of the specified product.). This is the underlying object with id, value and extensions. The accessor "getLotNumber" gives direct access to the value
         */
        public StringType getLotNumberElement() { 
          if (this.lotNumber == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationProductBatchComponent.lotNumber");
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
        public MedicationProductBatchComponent setLotNumberElement(StringType value) { 
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
        public MedicationProductBatchComponent setLotNumber(String value) { 
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
              throw new Error("Attempt to auto-create MedicationProductBatchComponent.expirationDate");
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
        public MedicationProductBatchComponent setExpirationDateElement(DateTimeType value) { 
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
        public MedicationProductBatchComponent setExpirationDate(Date value) { 
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

      public MedicationProductBatchComponent copy() {
        MedicationProductBatchComponent dst = new MedicationProductBatchComponent();
        copyValues(dst);
        dst.lotNumber = lotNumber == null ? null : lotNumber.copy();
        dst.expirationDate = expirationDate == null ? null : expirationDate.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof MedicationProductBatchComponent))
          return false;
        MedicationProductBatchComponent o = (MedicationProductBatchComponent) other;
        return compareDeep(lotNumber, o.lotNumber, true) && compareDeep(expirationDate, o.expirationDate, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof MedicationProductBatchComponent))
          return false;
        MedicationProductBatchComponent o = (MedicationProductBatchComponent) other;
        return compareValues(lotNumber, o.lotNumber, true) && compareValues(expirationDate, o.expirationDate, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (lotNumber == null || lotNumber.isEmpty()) && (expirationDate == null || expirationDate.isEmpty())
          ;
      }

  }

    @Block()
    public static class MedicationPackageComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The kind of container that this package comes as.
         */
        @Child(name = "container", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="E.g. box, vial, blister-pack", formalDefinition="The kind of container that this package comes as." )
        protected CodeableConcept container;

        /**
         * A set of components that go to make up the described item.
         */
        @Child(name = "content", type = {}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="What is  in the package?", formalDefinition="A set of components that go to make up the described item." )
        protected List<MedicationPackageContentComponent> content;

        private static final long serialVersionUID = 503772472L;

    /*
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

        public boolean hasContent() { 
          if (this.content == null)
            return false;
          for (MedicationPackageContentComponent item : this.content)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #content} (A set of components that go to make up the described item.)
         */
    // syntactic sugar
        public MedicationPackageContentComponent addContent() { //3
          MedicationPackageContentComponent t = new MedicationPackageContentComponent();
          if (this.content == null)
            this.content = new ArrayList<MedicationPackageContentComponent>();
          this.content.add(t);
          return t;
        }

    // syntactic sugar
        public MedicationPackageComponent addContent(MedicationPackageContentComponent t) { //3
          if (t == null)
            return this;
          if (this.content == null)
            this.content = new ArrayList<MedicationPackageContentComponent>();
          this.content.add(t);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("container", "CodeableConcept", "The kind of container that this package comes as.", 0, java.lang.Integer.MAX_VALUE, container));
          childrenList.add(new Property("content", "", "A set of components that go to make up the described item.", 0, java.lang.Integer.MAX_VALUE, content));
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
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof MedicationPackageComponent))
          return false;
        MedicationPackageComponent o = (MedicationPackageComponent) other;
        return compareDeep(container, o.container, true) && compareDeep(content, o.content, true);
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
        return super.isEmpty() && (container == null || container.isEmpty()) && (content == null || content.isEmpty())
          ;
      }

  }

    @Block()
    public static class MedicationPackageContentComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Identifies one of the items in the package.
         */
        @Child(name = "item", type = {Medication.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="A product in the package", formalDefinition="Identifies one of the items in the package." )
        protected Reference item;

        /**
         * The actual object that is the target of the reference (Identifies one of the items in the package.)
         */
        protected Medication itemTarget;

        /**
         * The amount of the product that is in the package.
         */
        @Child(name = "amount", type = {SimpleQuantity.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="How many are in the package?", formalDefinition="The amount of the product that is in the package." )
        protected SimpleQuantity amount;

        private static final long serialVersionUID = -1150048030L;

    /*
     * Constructor
     */
      public MedicationPackageContentComponent() {
        super();
      }

    /*
     * Constructor
     */
      public MedicationPackageContentComponent(Reference item) {
        super();
        this.item = item;
      }

        /**
         * @return {@link #item} (Identifies one of the items in the package.)
         */
        public Reference getItem() { 
          if (this.item == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationPackageContentComponent.item");
            else if (Configuration.doAutoCreate())
              this.item = new Reference(); // cc
          return this.item;
        }

        public boolean hasItem() { 
          return this.item != null && !this.item.isEmpty();
        }

        /**
         * @param value {@link #item} (Identifies one of the items in the package.)
         */
        public MedicationPackageContentComponent setItem(Reference value) { 
          this.item = value;
          return this;
        }

        /**
         * @return {@link #item} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Identifies one of the items in the package.)
         */
        public Medication getItemTarget() { 
          if (this.itemTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationPackageContentComponent.item");
            else if (Configuration.doAutoCreate())
              this.itemTarget = new Medication(); // aa
          return this.itemTarget;
        }

        /**
         * @param value {@link #item} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Identifies one of the items in the package.)
         */
        public MedicationPackageContentComponent setItemTarget(Medication value) { 
          this.itemTarget = value;
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
          childrenList.add(new Property("item", "Reference(Medication)", "Identifies one of the items in the package.", 0, java.lang.Integer.MAX_VALUE, item));
          childrenList.add(new Property("amount", "SimpleQuantity", "The amount of the product that is in the package.", 0, java.lang.Integer.MAX_VALUE, amount));
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
        return super.isEmpty() && (item == null || item.isEmpty()) && (amount == null || amount.isEmpty())
          ;
      }

  }

    /**
     * A code (or set of codes) that specify this medication, or a textual description if no code is available. Usage note: This could be a standard medication code such as a code from RxNorm, SNOMED CT, IDMP etc. It could also be a national or local formulary code, optionally with translations to other code systems.
     */
    @Child(name = "code", type = {CodeableConcept.class}, order=0, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Codes that identify this medication", formalDefinition="A code (or set of codes) that specify this medication, or a textual description if no code is available. Usage note: This could be a standard medication code such as a code from RxNorm, SNOMED CT, IDMP etc. It could also be a national or local formulary code, optionally with translations to other code systems." )
    protected CodeableConcept code;

    /**
     * Set to true if the item is attributable to a specific manufacturer.
     */
    @Child(name = "isBrand", type = {BooleanType.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="True if a brand", formalDefinition="Set to true if the item is attributable to a specific manufacturer." )
    protected BooleanType isBrand;

    /**
     * Describes the details of the manufacturer.
     */
    @Child(name = "manufacturer", type = {Organization.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Manufacturer of the item", formalDefinition="Describes the details of the manufacturer." )
    protected Reference manufacturer;

    /**
     * The actual object that is the target of the reference (Describes the details of the manufacturer.)
     */
    protected Organization manufacturerTarget;

    /**
     * Medications are either a single administrable product or a package that contains one or more products.
     */
    @Child(name = "kind", type = {CodeType.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="product | package", formalDefinition="Medications are either a single administrable product or a package that contains one or more products." )
    protected Enumeration<MedicationKind> kind;

    /**
     * Information that only applies to products (not packages).
     */
    @Child(name = "product", type = {}, order=4, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Administrable medication details", formalDefinition="Information that only applies to products (not packages)." )
    protected MedicationProductComponent product;

    /**
     * Information that only applies to packages (not products).
     */
    @Child(name = "package", type = {}, order=5, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Details about packaged medications", formalDefinition="Information that only applies to packages (not products)." )
    protected MedicationPackageComponent package_;

    private static final long serialVersionUID = -1593137297L;

  /*
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
     * @return {@link #manufacturer} (Describes the details of the manufacturer.)
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
     * @param value {@link #manufacturer} (Describes the details of the manufacturer.)
     */
    public Medication setManufacturer(Reference value) { 
      this.manufacturer = value;
      return this;
    }

    /**
     * @return {@link #manufacturer} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Describes the details of the manufacturer.)
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
     * @param value {@link #manufacturer} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Describes the details of the manufacturer.)
     */
    public Medication setManufacturerTarget(Organization value) { 
      this.manufacturerTarget = value;
      return this;
    }

    /**
     * @return {@link #kind} (Medications are either a single administrable product or a package that contains one or more products.). This is the underlying object with id, value and extensions. The accessor "getKind" gives direct access to the value
     */
    public Enumeration<MedicationKind> getKindElement() { 
      if (this.kind == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Medication.kind");
        else if (Configuration.doAutoCreate())
          this.kind = new Enumeration<MedicationKind>(new MedicationKindEnumFactory()); // bb
      return this.kind;
    }

    public boolean hasKindElement() { 
      return this.kind != null && !this.kind.isEmpty();
    }

    public boolean hasKind() { 
      return this.kind != null && !this.kind.isEmpty();
    }

    /**
     * @param value {@link #kind} (Medications are either a single administrable product or a package that contains one or more products.). This is the underlying object with id, value and extensions. The accessor "getKind" gives direct access to the value
     */
    public Medication setKindElement(Enumeration<MedicationKind> value) { 
      this.kind = value;
      return this;
    }

    /**
     * @return Medications are either a single administrable product or a package that contains one or more products.
     */
    public MedicationKind getKind() { 
      return this.kind == null ? null : this.kind.getValue();
    }

    /**
     * @param value Medications are either a single administrable product or a package that contains one or more products.
     */
    public Medication setKind(MedicationKind value) { 
      if (value == null)
        this.kind = null;
      else {
        if (this.kind == null)
          this.kind = new Enumeration<MedicationKind>(new MedicationKindEnumFactory());
        this.kind.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #product} (Information that only applies to products (not packages).)
     */
    public MedicationProductComponent getProduct() { 
      if (this.product == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Medication.product");
        else if (Configuration.doAutoCreate())
          this.product = new MedicationProductComponent(); // cc
      return this.product;
    }

    public boolean hasProduct() { 
      return this.product != null && !this.product.isEmpty();
    }

    /**
     * @param value {@link #product} (Information that only applies to products (not packages).)
     */
    public Medication setProduct(MedicationProductComponent value) { 
      this.product = value;
      return this;
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

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("code", "CodeableConcept", "A code (or set of codes) that specify this medication, or a textual description if no code is available. Usage note: This could be a standard medication code such as a code from RxNorm, SNOMED CT, IDMP etc. It could also be a national or local formulary code, optionally with translations to other code systems.", 0, java.lang.Integer.MAX_VALUE, code));
        childrenList.add(new Property("isBrand", "boolean", "Set to true if the item is attributable to a specific manufacturer.", 0, java.lang.Integer.MAX_VALUE, isBrand));
        childrenList.add(new Property("manufacturer", "Reference(Organization)", "Describes the details of the manufacturer.", 0, java.lang.Integer.MAX_VALUE, manufacturer));
        childrenList.add(new Property("kind", "code", "Medications are either a single administrable product or a package that contains one or more products.", 0, java.lang.Integer.MAX_VALUE, kind));
        childrenList.add(new Property("product", "", "Information that only applies to products (not packages).", 0, java.lang.Integer.MAX_VALUE, product));
        childrenList.add(new Property("package", "", "Information that only applies to packages (not products).", 0, java.lang.Integer.MAX_VALUE, package_));
      }

      public Medication copy() {
        Medication dst = new Medication();
        copyValues(dst);
        dst.code = code == null ? null : code.copy();
        dst.isBrand = isBrand == null ? null : isBrand.copy();
        dst.manufacturer = manufacturer == null ? null : manufacturer.copy();
        dst.kind = kind == null ? null : kind.copy();
        dst.product = product == null ? null : product.copy();
        dst.package_ = package_ == null ? null : package_.copy();
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
        return compareDeep(code, o.code, true) && compareDeep(isBrand, o.isBrand, true) && compareDeep(manufacturer, o.manufacturer, true)
           && compareDeep(kind, o.kind, true) && compareDeep(product, o.product, true) && compareDeep(package_, o.package_, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Medication))
          return false;
        Medication o = (Medication) other;
        return compareValues(isBrand, o.isBrand, true) && compareValues(kind, o.kind, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (code == null || code.isEmpty()) && (isBrand == null || isBrand.isEmpty())
           && (manufacturer == null || manufacturer.isEmpty()) && (kind == null || kind.isEmpty()) && (product == null || product.isEmpty())
           && (package_ == null || package_.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Medication;
   }

  @SearchParamDefinition(name="container", path="Medication.package.container", description="E.g. box, vial, blister-pack", type="token" )
  public static final String SP_CONTAINER = "container";
  @SearchParamDefinition(name="code", path="Medication.code", description="Codes that identify this medication", type="token" )
  public static final String SP_CODE = "code";
  @SearchParamDefinition(name="ingredient", path="Medication.product.ingredient.item", description="The product contained", type="reference" )
  public static final String SP_INGREDIENT = "ingredient";
  @SearchParamDefinition(name="form", path="Medication.product.form", description="powder | tablets | carton +", type="token" )
  public static final String SP_FORM = "form";
  @SearchParamDefinition(name="content", path="Medication.package.content.item", description="A product in the package", type="reference" )
  public static final String SP_CONTENT = "content";
  @SearchParamDefinition(name="manufacturer", path="Medication.manufacturer", description="Manufacturer of the item", type="reference" )
  public static final String SP_MANUFACTURER = "manufacturer";

}

