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

import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ChildOrder;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Block;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.exceptions.FHIRException;
/**
 * The manufactured item as contained in the packaged medicinal product.
 */
@ResourceDef(name="MedicinalProductManufactured", profile="http://hl7.org/fhir/StructureDefinition/MedicinalProductManufactured")
public class MedicinalProductManufactured extends DomainResource {

    /**
     * Dose form as manufactured and before any transformation into the pharmaceutical product.
     */
    @Child(name = "manufacturedDoseForm", type = {CodeableConcept.class}, order=0, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Dose form as manufactured and before any transformation into the pharmaceutical product", formalDefinition="Dose form as manufactured and before any transformation into the pharmaceutical product." )
    protected CodeableConcept manufacturedDoseForm;

    /**
     * The “real world” units in which the quantity of the manufactured item is described.
     */
    @Child(name = "unitOfPresentation", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The “real world” units in which the quantity of the manufactured item is described", formalDefinition="The “real world” units in which the quantity of the manufactured item is described." )
    protected CodeableConcept unitOfPresentation;

    /**
     * The quantity or "count number" of the manufactured item.
     */
    @Child(name = "quantity", type = {Quantity.class}, order=2, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The quantity or \"count number\" of the manufactured item", formalDefinition="The quantity or \"count number\" of the manufactured item." )
    protected Quantity quantity;

    /**
     * Manufacturer of the item (Note that this should be named "manufacturer" but it currently causes technical issues).
     */
    @Child(name = "manufacturer", type = {Organization.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Manufacturer of the item (Note that this should be named \"manufacturer\" but it currently causes technical issues)", formalDefinition="Manufacturer of the item (Note that this should be named \"manufacturer\" but it currently causes technical issues)." )
    protected List<Reference> manufacturer;
    /**
     * The actual objects that are the target of the reference (Manufacturer of the item (Note that this should be named "manufacturer" but it currently causes technical issues).)
     */
    protected List<Organization> manufacturerTarget;


    /**
     * Ingredient.
     */
    @Child(name = "ingredient", type = {MedicinalProductIngredient.class}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Ingredient", formalDefinition="Ingredient." )
    protected List<Reference> ingredient;
    /**
     * The actual objects that are the target of the reference (Ingredient.)
     */
    protected List<MedicinalProductIngredient> ingredientTarget;


    /**
     * Dimensions, color etc.
     */
    @Child(name = "physicalCharacteristics", type = {ProdCharacteristic.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Dimensions, color etc.", formalDefinition="Dimensions, color etc." )
    protected ProdCharacteristic physicalCharacteristics;

    /**
     * Other codeable characteristics.
     */
    @Child(name = "otherCharacteristics", type = {CodeableConcept.class}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Other codeable characteristics", formalDefinition="Other codeable characteristics." )
    protected List<CodeableConcept> otherCharacteristics;

    private static final long serialVersionUID = 623073384L;

  /**
   * Constructor
   */
    public MedicinalProductManufactured() {
      super();
    }

  /**
   * Constructor
   */
    public MedicinalProductManufactured(CodeableConcept manufacturedDoseForm, Quantity quantity) {
      super();
      this.manufacturedDoseForm = manufacturedDoseForm;
      this.quantity = quantity;
    }

    /**
     * @return {@link #manufacturedDoseForm} (Dose form as manufactured and before any transformation into the pharmaceutical product.)
     */
    public CodeableConcept getManufacturedDoseForm() { 
      if (this.manufacturedDoseForm == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicinalProductManufactured.manufacturedDoseForm");
        else if (Configuration.doAutoCreate())
          this.manufacturedDoseForm = new CodeableConcept(); // cc
      return this.manufacturedDoseForm;
    }

    public boolean hasManufacturedDoseForm() { 
      return this.manufacturedDoseForm != null && !this.manufacturedDoseForm.isEmpty();
    }

    /**
     * @param value {@link #manufacturedDoseForm} (Dose form as manufactured and before any transformation into the pharmaceutical product.)
     */
    public MedicinalProductManufactured setManufacturedDoseForm(CodeableConcept value) { 
      this.manufacturedDoseForm = value;
      return this;
    }

    /**
     * @return {@link #unitOfPresentation} (The “real world” units in which the quantity of the manufactured item is described.)
     */
    public CodeableConcept getUnitOfPresentation() { 
      if (this.unitOfPresentation == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicinalProductManufactured.unitOfPresentation");
        else if (Configuration.doAutoCreate())
          this.unitOfPresentation = new CodeableConcept(); // cc
      return this.unitOfPresentation;
    }

    public boolean hasUnitOfPresentation() { 
      return this.unitOfPresentation != null && !this.unitOfPresentation.isEmpty();
    }

    /**
     * @param value {@link #unitOfPresentation} (The “real world” units in which the quantity of the manufactured item is described.)
     */
    public MedicinalProductManufactured setUnitOfPresentation(CodeableConcept value) { 
      this.unitOfPresentation = value;
      return this;
    }

    /**
     * @return {@link #quantity} (The quantity or "count number" of the manufactured item.)
     */
    public Quantity getQuantity() { 
      if (this.quantity == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicinalProductManufactured.quantity");
        else if (Configuration.doAutoCreate())
          this.quantity = new Quantity(); // cc
      return this.quantity;
    }

    public boolean hasQuantity() { 
      return this.quantity != null && !this.quantity.isEmpty();
    }

    /**
     * @param value {@link #quantity} (The quantity or "count number" of the manufactured item.)
     */
    public MedicinalProductManufactured setQuantity(Quantity value) { 
      this.quantity = value;
      return this;
    }

    /**
     * @return {@link #manufacturer} (Manufacturer of the item (Note that this should be named "manufacturer" but it currently causes technical issues).)
     */
    public List<Reference> getManufacturer() { 
      if (this.manufacturer == null)
        this.manufacturer = new ArrayList<Reference>();
      return this.manufacturer;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicinalProductManufactured setManufacturer(List<Reference> theManufacturer) { 
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

    public MedicinalProductManufactured addManufacturer(Reference t) { //3
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
     * @return {@link #ingredient} (Ingredient.)
     */
    public List<Reference> getIngredient() { 
      if (this.ingredient == null)
        this.ingredient = new ArrayList<Reference>();
      return this.ingredient;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicinalProductManufactured setIngredient(List<Reference> theIngredient) { 
      this.ingredient = theIngredient;
      return this;
    }

    public boolean hasIngredient() { 
      if (this.ingredient == null)
        return false;
      for (Reference item : this.ingredient)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addIngredient() { //3
      Reference t = new Reference();
      if (this.ingredient == null)
        this.ingredient = new ArrayList<Reference>();
      this.ingredient.add(t);
      return t;
    }

    public MedicinalProductManufactured addIngredient(Reference t) { //3
      if (t == null)
        return this;
      if (this.ingredient == null)
        this.ingredient = new ArrayList<Reference>();
      this.ingredient.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #ingredient}, creating it if it does not already exist
     */
    public Reference getIngredientFirstRep() { 
      if (getIngredient().isEmpty()) {
        addIngredient();
      }
      return getIngredient().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<MedicinalProductIngredient> getIngredientTarget() { 
      if (this.ingredientTarget == null)
        this.ingredientTarget = new ArrayList<MedicinalProductIngredient>();
      return this.ingredientTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public MedicinalProductIngredient addIngredientTarget() { 
      MedicinalProductIngredient r = new MedicinalProductIngredient();
      if (this.ingredientTarget == null)
        this.ingredientTarget = new ArrayList<MedicinalProductIngredient>();
      this.ingredientTarget.add(r);
      return r;
    }

    /**
     * @return {@link #physicalCharacteristics} (Dimensions, color etc.)
     */
    public ProdCharacteristic getPhysicalCharacteristics() { 
      if (this.physicalCharacteristics == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicinalProductManufactured.physicalCharacteristics");
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
    public MedicinalProductManufactured setPhysicalCharacteristics(ProdCharacteristic value) { 
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
    public MedicinalProductManufactured setOtherCharacteristics(List<CodeableConcept> theOtherCharacteristics) { 
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

    public MedicinalProductManufactured addOtherCharacteristics(CodeableConcept t) { //3
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

      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("manufacturedDoseForm", "CodeableConcept", "Dose form as manufactured and before any transformation into the pharmaceutical product.", 0, 1, manufacturedDoseForm));
        children.add(new Property("unitOfPresentation", "CodeableConcept", "The “real world” units in which the quantity of the manufactured item is described.", 0, 1, unitOfPresentation));
        children.add(new Property("quantity", "Quantity", "The quantity or \"count number\" of the manufactured item.", 0, 1, quantity));
        children.add(new Property("manufacturer", "Reference(Organization)", "Manufacturer of the item (Note that this should be named \"manufacturer\" but it currently causes technical issues).", 0, java.lang.Integer.MAX_VALUE, manufacturer));
        children.add(new Property("ingredient", "Reference(MedicinalProductIngredient)", "Ingredient.", 0, java.lang.Integer.MAX_VALUE, ingredient));
        children.add(new Property("physicalCharacteristics", "ProdCharacteristic", "Dimensions, color etc.", 0, 1, physicalCharacteristics));
        children.add(new Property("otherCharacteristics", "CodeableConcept", "Other codeable characteristics.", 0, java.lang.Integer.MAX_VALUE, otherCharacteristics));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case -1451400348: /*manufacturedDoseForm*/  return new Property("manufacturedDoseForm", "CodeableConcept", "Dose form as manufactured and before any transformation into the pharmaceutical product.", 0, 1, manufacturedDoseForm);
        case -1427765963: /*unitOfPresentation*/  return new Property("unitOfPresentation", "CodeableConcept", "The “real world” units in which the quantity of the manufactured item is described.", 0, 1, unitOfPresentation);
        case -1285004149: /*quantity*/  return new Property("quantity", "Quantity", "The quantity or \"count number\" of the manufactured item.", 0, 1, quantity);
        case -1969347631: /*manufacturer*/  return new Property("manufacturer", "Reference(Organization)", "Manufacturer of the item (Note that this should be named \"manufacturer\" but it currently causes technical issues).", 0, java.lang.Integer.MAX_VALUE, manufacturer);
        case -206409263: /*ingredient*/  return new Property("ingredient", "Reference(MedicinalProductIngredient)", "Ingredient.", 0, java.lang.Integer.MAX_VALUE, ingredient);
        case -1599676319: /*physicalCharacteristics*/  return new Property("physicalCharacteristics", "ProdCharacteristic", "Dimensions, color etc.", 0, 1, physicalCharacteristics);
        case 722135304: /*otherCharacteristics*/  return new Property("otherCharacteristics", "CodeableConcept", "Other codeable characteristics.", 0, java.lang.Integer.MAX_VALUE, otherCharacteristics);
        default: return super.getNamedProperty(_hash, _name, _checkValid);
        }

      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1451400348: /*manufacturedDoseForm*/ return this.manufacturedDoseForm == null ? new Base[0] : new Base[] {this.manufacturedDoseForm}; // CodeableConcept
        case -1427765963: /*unitOfPresentation*/ return this.unitOfPresentation == null ? new Base[0] : new Base[] {this.unitOfPresentation}; // CodeableConcept
        case -1285004149: /*quantity*/ return this.quantity == null ? new Base[0] : new Base[] {this.quantity}; // Quantity
        case -1969347631: /*manufacturer*/ return this.manufacturer == null ? new Base[0] : this.manufacturer.toArray(new Base[this.manufacturer.size()]); // Reference
        case -206409263: /*ingredient*/ return this.ingredient == null ? new Base[0] : this.ingredient.toArray(new Base[this.ingredient.size()]); // Reference
        case -1599676319: /*physicalCharacteristics*/ return this.physicalCharacteristics == null ? new Base[0] : new Base[] {this.physicalCharacteristics}; // ProdCharacteristic
        case 722135304: /*otherCharacteristics*/ return this.otherCharacteristics == null ? new Base[0] : this.otherCharacteristics.toArray(new Base[this.otherCharacteristics.size()]); // CodeableConcept
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1451400348: // manufacturedDoseForm
          this.manufacturedDoseForm = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1427765963: // unitOfPresentation
          this.unitOfPresentation = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1285004149: // quantity
          this.quantity = castToQuantity(value); // Quantity
          return value;
        case -1969347631: // manufacturer
          this.getManufacturer().add(castToReference(value)); // Reference
          return value;
        case -206409263: // ingredient
          this.getIngredient().add(castToReference(value)); // Reference
          return value;
        case -1599676319: // physicalCharacteristics
          this.physicalCharacteristics = castToProdCharacteristic(value); // ProdCharacteristic
          return value;
        case 722135304: // otherCharacteristics
          this.getOtherCharacteristics().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("manufacturedDoseForm")) {
          this.manufacturedDoseForm = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("unitOfPresentation")) {
          this.unitOfPresentation = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("quantity")) {
          this.quantity = castToQuantity(value); // Quantity
        } else if (name.equals("manufacturer")) {
          this.getManufacturer().add(castToReference(value));
        } else if (name.equals("ingredient")) {
          this.getIngredient().add(castToReference(value));
        } else if (name.equals("physicalCharacteristics")) {
          this.physicalCharacteristics = castToProdCharacteristic(value); // ProdCharacteristic
        } else if (name.equals("otherCharacteristics")) {
          this.getOtherCharacteristics().add(castToCodeableConcept(value));
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1451400348:  return getManufacturedDoseForm(); 
        case -1427765963:  return getUnitOfPresentation(); 
        case -1285004149:  return getQuantity(); 
        case -1969347631:  return addManufacturer(); 
        case -206409263:  return addIngredient(); 
        case -1599676319:  return getPhysicalCharacteristics(); 
        case 722135304:  return addOtherCharacteristics(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1451400348: /*manufacturedDoseForm*/ return new String[] {"CodeableConcept"};
        case -1427765963: /*unitOfPresentation*/ return new String[] {"CodeableConcept"};
        case -1285004149: /*quantity*/ return new String[] {"Quantity"};
        case -1969347631: /*manufacturer*/ return new String[] {"Reference"};
        case -206409263: /*ingredient*/ return new String[] {"Reference"};
        case -1599676319: /*physicalCharacteristics*/ return new String[] {"ProdCharacteristic"};
        case 722135304: /*otherCharacteristics*/ return new String[] {"CodeableConcept"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("manufacturedDoseForm")) {
          this.manufacturedDoseForm = new CodeableConcept();
          return this.manufacturedDoseForm;
        }
        else if (name.equals("unitOfPresentation")) {
          this.unitOfPresentation = new CodeableConcept();
          return this.unitOfPresentation;
        }
        else if (name.equals("quantity")) {
          this.quantity = new Quantity();
          return this.quantity;
        }
        else if (name.equals("manufacturer")) {
          return addManufacturer();
        }
        else if (name.equals("ingredient")) {
          return addIngredient();
        }
        else if (name.equals("physicalCharacteristics")) {
          this.physicalCharacteristics = new ProdCharacteristic();
          return this.physicalCharacteristics;
        }
        else if (name.equals("otherCharacteristics")) {
          return addOtherCharacteristics();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "MedicinalProductManufactured";

  }

      public MedicinalProductManufactured copy() {
        MedicinalProductManufactured dst = new MedicinalProductManufactured();
        copyValues(dst);
        dst.manufacturedDoseForm = manufacturedDoseForm == null ? null : manufacturedDoseForm.copy();
        dst.unitOfPresentation = unitOfPresentation == null ? null : unitOfPresentation.copy();
        dst.quantity = quantity == null ? null : quantity.copy();
        if (manufacturer != null) {
          dst.manufacturer = new ArrayList<Reference>();
          for (Reference i : manufacturer)
            dst.manufacturer.add(i.copy());
        };
        if (ingredient != null) {
          dst.ingredient = new ArrayList<Reference>();
          for (Reference i : ingredient)
            dst.ingredient.add(i.copy());
        };
        dst.physicalCharacteristics = physicalCharacteristics == null ? null : physicalCharacteristics.copy();
        if (otherCharacteristics != null) {
          dst.otherCharacteristics = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : otherCharacteristics)
            dst.otherCharacteristics.add(i.copy());
        };
        return dst;
      }

      protected MedicinalProductManufactured typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof MedicinalProductManufactured))
          return false;
        MedicinalProductManufactured o = (MedicinalProductManufactured) other_;
        return compareDeep(manufacturedDoseForm, o.manufacturedDoseForm, true) && compareDeep(unitOfPresentation, o.unitOfPresentation, true)
           && compareDeep(quantity, o.quantity, true) && compareDeep(manufacturer, o.manufacturer, true) && compareDeep(ingredient, o.ingredient, true)
           && compareDeep(physicalCharacteristics, o.physicalCharacteristics, true) && compareDeep(otherCharacteristics, o.otherCharacteristics, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof MedicinalProductManufactured))
          return false;
        MedicinalProductManufactured o = (MedicinalProductManufactured) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(manufacturedDoseForm, unitOfPresentation
          , quantity, manufacturer, ingredient, physicalCharacteristics, otherCharacteristics
          );
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.MedicinalProductManufactured;
   }


}

