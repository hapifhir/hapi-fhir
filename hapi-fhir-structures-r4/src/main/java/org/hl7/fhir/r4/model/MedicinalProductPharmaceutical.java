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
 * A pharmaceutical product described in terms of its composition and dose form.
 */
@ResourceDef(name="MedicinalProductPharmaceutical", profile="http://hl7.org/fhir/Profile/MedicinalProductPharmaceutical")
public class MedicinalProductPharmaceutical extends DomainResource {

    @Block()
    public static class MedicinalProductPharmaceuticalCharacteristicsComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A coded characteristic.
         */
        @Child(name = "code", type = {CodeableConcept.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="A coded characteristic", formalDefinition="A coded characteristic." )
        protected CodeableConcept code;

        /**
         * The status of characteristic e.g. assigned or pending.
         */
        @Child(name = "status", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The status of characteristic e.g. assigned or pending", formalDefinition="The status of characteristic e.g. assigned or pending." )
        protected CodeableConcept status;

        private static final long serialVersionUID = 1414556635L;

    /**
     * Constructor
     */
      public MedicinalProductPharmaceuticalCharacteristicsComponent() {
        super();
      }

    /**
     * Constructor
     */
      public MedicinalProductPharmaceuticalCharacteristicsComponent(CodeableConcept code) {
        super();
        this.code = code;
      }

        /**
         * @return {@link #code} (A coded characteristic.)
         */
        public CodeableConcept getCode() { 
          if (this.code == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicinalProductPharmaceuticalCharacteristicsComponent.code");
            else if (Configuration.doAutoCreate())
              this.code = new CodeableConcept(); // cc
          return this.code;
        }

        public boolean hasCode() { 
          return this.code != null && !this.code.isEmpty();
        }

        /**
         * @param value {@link #code} (A coded characteristic.)
         */
        public MedicinalProductPharmaceuticalCharacteristicsComponent setCode(CodeableConcept value) { 
          this.code = value;
          return this;
        }

        /**
         * @return {@link #status} (The status of characteristic e.g. assigned or pending.)
         */
        public CodeableConcept getStatus() { 
          if (this.status == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicinalProductPharmaceuticalCharacteristicsComponent.status");
            else if (Configuration.doAutoCreate())
              this.status = new CodeableConcept(); // cc
          return this.status;
        }

        public boolean hasStatus() { 
          return this.status != null && !this.status.isEmpty();
        }

        /**
         * @param value {@link #status} (The status of characteristic e.g. assigned or pending.)
         */
        public MedicinalProductPharmaceuticalCharacteristicsComponent setStatus(CodeableConcept value) { 
          this.status = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("code", "CodeableConcept", "A coded characteristic.", 0, 1, code));
          children.add(new Property("status", "CodeableConcept", "The status of characteristic e.g. assigned or pending.", 0, 1, status));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3059181: /*code*/  return new Property("code", "CodeableConcept", "A coded characteristic.", 0, 1, code);
          case -892481550: /*status*/  return new Property("status", "CodeableConcept", "The status of characteristic e.g. assigned or pending.", 0, 1, status);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3059181: /*code*/ return this.code == null ? new Base[0] : new Base[] {this.code}; // CodeableConcept
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // CodeableConcept
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
          this.status = castToCodeableConcept(value); // CodeableConcept
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("code")) {
          this.code = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("status")) {
          this.status = castToCodeableConcept(value); // CodeableConcept
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3059181:  return getCode(); 
        case -892481550:  return getStatus(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3059181: /*code*/ return new String[] {"CodeableConcept"};
        case -892481550: /*status*/ return new String[] {"CodeableConcept"};
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
          this.status = new CodeableConcept();
          return this.status;
        }
        else
          return super.addChild(name);
      }

      public MedicinalProductPharmaceuticalCharacteristicsComponent copy() {
        MedicinalProductPharmaceuticalCharacteristicsComponent dst = new MedicinalProductPharmaceuticalCharacteristicsComponent();
        copyValues(dst);
        dst.code = code == null ? null : code.copy();
        dst.status = status == null ? null : status.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof MedicinalProductPharmaceuticalCharacteristicsComponent))
          return false;
        MedicinalProductPharmaceuticalCharacteristicsComponent o = (MedicinalProductPharmaceuticalCharacteristicsComponent) other_;
        return compareDeep(code, o.code, true) && compareDeep(status, o.status, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof MedicinalProductPharmaceuticalCharacteristicsComponent))
          return false;
        MedicinalProductPharmaceuticalCharacteristicsComponent o = (MedicinalProductPharmaceuticalCharacteristicsComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(code, status);
      }

  public String fhirType() {
    return "MedicinalProductPharmaceutical.characteristics";

  }

  }

    /**
     * An identifier for the pharmaceutical medicinal product.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="An identifier for the pharmaceutical medicinal product", formalDefinition="An identifier for the pharmaceutical medicinal product." )
    protected List<Identifier> identifier;

    /**
     * The administrable dose form, after necessary reconstitution.
     */
    @Child(name = "administrableDoseForm", type = {CodeableConcept.class}, order=1, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The administrable dose form, after necessary reconstitution", formalDefinition="The administrable dose form, after necessary reconstitution." )
    protected CodeableConcept administrableDoseForm;

    /**
     * Todo.
     */
    @Child(name = "unitOfPresentation", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Todo", formalDefinition="Todo." )
    protected CodeableConcept unitOfPresentation;

    /**
     * The path by which the pharmaceutical product is taken into or makes contact with the body.
     */
    @Child(name = "routeOfAdministration", type = {CodeableConcept.class}, order=3, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="The path by which the pharmaceutical product is taken into or makes contact with the body", formalDefinition="The path by which the pharmaceutical product is taken into or makes contact with the body." )
    protected List<CodeableConcept> routeOfAdministration;

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
     * Accompanying device.
     */
    @Child(name = "device", type = {StringType.class}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Accompanying device", formalDefinition="Accompanying device." )
    protected List<StringType> device;

    /**
     * Characteristics e.g. a products onset of action.
     */
    @Child(name = "characteristics", type = {}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Characteristics e.g. a products onset of action", formalDefinition="Characteristics e.g. a products onset of action." )
    protected List<MedicinalProductPharmaceuticalCharacteristicsComponent> characteristics;

    private static final long serialVersionUID = -1564698410L;

  /**
   * Constructor
   */
    public MedicinalProductPharmaceutical() {
      super();
    }

  /**
   * Constructor
   */
    public MedicinalProductPharmaceutical(CodeableConcept administrableDoseForm) {
      super();
      this.administrableDoseForm = administrableDoseForm;
    }

    /**
     * @return {@link #identifier} (An identifier for the pharmaceutical medicinal product.)
     */
    public List<Identifier> getIdentifier() { 
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      return this.identifier;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicinalProductPharmaceutical setIdentifier(List<Identifier> theIdentifier) { 
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

    public MedicinalProductPharmaceutical addIdentifier(Identifier t) { //3
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
     * @return {@link #administrableDoseForm} (The administrable dose form, after necessary reconstitution.)
     */
    public CodeableConcept getAdministrableDoseForm() { 
      if (this.administrableDoseForm == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicinalProductPharmaceutical.administrableDoseForm");
        else if (Configuration.doAutoCreate())
          this.administrableDoseForm = new CodeableConcept(); // cc
      return this.administrableDoseForm;
    }

    public boolean hasAdministrableDoseForm() { 
      return this.administrableDoseForm != null && !this.administrableDoseForm.isEmpty();
    }

    /**
     * @param value {@link #administrableDoseForm} (The administrable dose form, after necessary reconstitution.)
     */
    public MedicinalProductPharmaceutical setAdministrableDoseForm(CodeableConcept value) { 
      this.administrableDoseForm = value;
      return this;
    }

    /**
     * @return {@link #unitOfPresentation} (Todo.)
     */
    public CodeableConcept getUnitOfPresentation() { 
      if (this.unitOfPresentation == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicinalProductPharmaceutical.unitOfPresentation");
        else if (Configuration.doAutoCreate())
          this.unitOfPresentation = new CodeableConcept(); // cc
      return this.unitOfPresentation;
    }

    public boolean hasUnitOfPresentation() { 
      return this.unitOfPresentation != null && !this.unitOfPresentation.isEmpty();
    }

    /**
     * @param value {@link #unitOfPresentation} (Todo.)
     */
    public MedicinalProductPharmaceutical setUnitOfPresentation(CodeableConcept value) { 
      this.unitOfPresentation = value;
      return this;
    }

    /**
     * @return {@link #routeOfAdministration} (The path by which the pharmaceutical product is taken into or makes contact with the body.)
     */
    public List<CodeableConcept> getRouteOfAdministration() { 
      if (this.routeOfAdministration == null)
        this.routeOfAdministration = new ArrayList<CodeableConcept>();
      return this.routeOfAdministration;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicinalProductPharmaceutical setRouteOfAdministration(List<CodeableConcept> theRouteOfAdministration) { 
      this.routeOfAdministration = theRouteOfAdministration;
      return this;
    }

    public boolean hasRouteOfAdministration() { 
      if (this.routeOfAdministration == null)
        return false;
      for (CodeableConcept item : this.routeOfAdministration)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addRouteOfAdministration() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.routeOfAdministration == null)
        this.routeOfAdministration = new ArrayList<CodeableConcept>();
      this.routeOfAdministration.add(t);
      return t;
    }

    public MedicinalProductPharmaceutical addRouteOfAdministration(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.routeOfAdministration == null)
        this.routeOfAdministration = new ArrayList<CodeableConcept>();
      this.routeOfAdministration.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #routeOfAdministration}, creating it if it does not already exist
     */
    public CodeableConcept getRouteOfAdministrationFirstRep() { 
      if (getRouteOfAdministration().isEmpty()) {
        addRouteOfAdministration();
      }
      return getRouteOfAdministration().get(0);
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
    public MedicinalProductPharmaceutical setIngredient(List<Reference> theIngredient) { 
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

    public MedicinalProductPharmaceutical addIngredient(Reference t) { //3
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
     * @return {@link #device} (Accompanying device.)
     */
    public List<StringType> getDevice() { 
      if (this.device == null)
        this.device = new ArrayList<StringType>();
      return this.device;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicinalProductPharmaceutical setDevice(List<StringType> theDevice) { 
      this.device = theDevice;
      return this;
    }

    public boolean hasDevice() { 
      if (this.device == null)
        return false;
      for (StringType item : this.device)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #device} (Accompanying device.)
     */
    public StringType addDeviceElement() {//2 
      StringType t = new StringType();
      if (this.device == null)
        this.device = new ArrayList<StringType>();
      this.device.add(t);
      return t;
    }

    /**
     * @param value {@link #device} (Accompanying device.)
     */
    public MedicinalProductPharmaceutical addDevice(String value) { //1
      StringType t = new StringType();
      t.setValue(value);
      if (this.device == null)
        this.device = new ArrayList<StringType>();
      this.device.add(t);
      return this;
    }

    /**
     * @param value {@link #device} (Accompanying device.)
     */
    public boolean hasDevice(String value) { 
      if (this.device == null)
        return false;
      for (StringType v : this.device)
        if (v.getValue().equals(value)) // string
          return true;
      return false;
    }

    /**
     * @return {@link #characteristics} (Characteristics e.g. a products onset of action.)
     */
    public List<MedicinalProductPharmaceuticalCharacteristicsComponent> getCharacteristics() { 
      if (this.characteristics == null)
        this.characteristics = new ArrayList<MedicinalProductPharmaceuticalCharacteristicsComponent>();
      return this.characteristics;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicinalProductPharmaceutical setCharacteristics(List<MedicinalProductPharmaceuticalCharacteristicsComponent> theCharacteristics) { 
      this.characteristics = theCharacteristics;
      return this;
    }

    public boolean hasCharacteristics() { 
      if (this.characteristics == null)
        return false;
      for (MedicinalProductPharmaceuticalCharacteristicsComponent item : this.characteristics)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public MedicinalProductPharmaceuticalCharacteristicsComponent addCharacteristics() { //3
      MedicinalProductPharmaceuticalCharacteristicsComponent t = new MedicinalProductPharmaceuticalCharacteristicsComponent();
      if (this.characteristics == null)
        this.characteristics = new ArrayList<MedicinalProductPharmaceuticalCharacteristicsComponent>();
      this.characteristics.add(t);
      return t;
    }

    public MedicinalProductPharmaceutical addCharacteristics(MedicinalProductPharmaceuticalCharacteristicsComponent t) { //3
      if (t == null)
        return this;
      if (this.characteristics == null)
        this.characteristics = new ArrayList<MedicinalProductPharmaceuticalCharacteristicsComponent>();
      this.characteristics.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #characteristics}, creating it if it does not already exist
     */
    public MedicinalProductPharmaceuticalCharacteristicsComponent getCharacteristicsFirstRep() { 
      if (getCharacteristics().isEmpty()) {
        addCharacteristics();
      }
      return getCharacteristics().get(0);
    }

      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("identifier", "Identifier", "An identifier for the pharmaceutical medicinal product.", 0, java.lang.Integer.MAX_VALUE, identifier));
        children.add(new Property("administrableDoseForm", "CodeableConcept", "The administrable dose form, after necessary reconstitution.", 0, 1, administrableDoseForm));
        children.add(new Property("unitOfPresentation", "CodeableConcept", "Todo.", 0, 1, unitOfPresentation));
        children.add(new Property("routeOfAdministration", "CodeableConcept", "The path by which the pharmaceutical product is taken into or makes contact with the body.", 0, java.lang.Integer.MAX_VALUE, routeOfAdministration));
        children.add(new Property("ingredient", "Reference(MedicinalProductIngredient)", "Ingredient.", 0, java.lang.Integer.MAX_VALUE, ingredient));
        children.add(new Property("device", "string", "Accompanying device.", 0, java.lang.Integer.MAX_VALUE, device));
        children.add(new Property("characteristics", "", "Characteristics e.g. a products onset of action.", 0, java.lang.Integer.MAX_VALUE, characteristics));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case -1618432855: /*identifier*/  return new Property("identifier", "Identifier", "An identifier for the pharmaceutical medicinal product.", 0, java.lang.Integer.MAX_VALUE, identifier);
        case 1446105202: /*administrableDoseForm*/  return new Property("administrableDoseForm", "CodeableConcept", "The administrable dose form, after necessary reconstitution.", 0, 1, administrableDoseForm);
        case -1427765963: /*unitOfPresentation*/  return new Property("unitOfPresentation", "CodeableConcept", "Todo.", 0, 1, unitOfPresentation);
        case 1742084734: /*routeOfAdministration*/  return new Property("routeOfAdministration", "CodeableConcept", "The path by which the pharmaceutical product is taken into or makes contact with the body.", 0, java.lang.Integer.MAX_VALUE, routeOfAdministration);
        case -206409263: /*ingredient*/  return new Property("ingredient", "Reference(MedicinalProductIngredient)", "Ingredient.", 0, java.lang.Integer.MAX_VALUE, ingredient);
        case -1335157162: /*device*/  return new Property("device", "string", "Accompanying device.", 0, java.lang.Integer.MAX_VALUE, device);
        case -1529171400: /*characteristics*/  return new Property("characteristics", "", "Characteristics e.g. a products onset of action.", 0, java.lang.Integer.MAX_VALUE, characteristics);
        default: return super.getNamedProperty(_hash, _name, _checkValid);
        }

      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case 1446105202: /*administrableDoseForm*/ return this.administrableDoseForm == null ? new Base[0] : new Base[] {this.administrableDoseForm}; // CodeableConcept
        case -1427765963: /*unitOfPresentation*/ return this.unitOfPresentation == null ? new Base[0] : new Base[] {this.unitOfPresentation}; // CodeableConcept
        case 1742084734: /*routeOfAdministration*/ return this.routeOfAdministration == null ? new Base[0] : this.routeOfAdministration.toArray(new Base[this.routeOfAdministration.size()]); // CodeableConcept
        case -206409263: /*ingredient*/ return this.ingredient == null ? new Base[0] : this.ingredient.toArray(new Base[this.ingredient.size()]); // Reference
        case -1335157162: /*device*/ return this.device == null ? new Base[0] : this.device.toArray(new Base[this.device.size()]); // StringType
        case -1529171400: /*characteristics*/ return this.characteristics == null ? new Base[0] : this.characteristics.toArray(new Base[this.characteristics.size()]); // MedicinalProductPharmaceuticalCharacteristicsComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.getIdentifier().add(castToIdentifier(value)); // Identifier
          return value;
        case 1446105202: // administrableDoseForm
          this.administrableDoseForm = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1427765963: // unitOfPresentation
          this.unitOfPresentation = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 1742084734: // routeOfAdministration
          this.getRouteOfAdministration().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -206409263: // ingredient
          this.getIngredient().add(castToReference(value)); // Reference
          return value;
        case -1335157162: // device
          this.getDevice().add(castToString(value)); // StringType
          return value;
        case -1529171400: // characteristics
          this.getCharacteristics().add((MedicinalProductPharmaceuticalCharacteristicsComponent) value); // MedicinalProductPharmaceuticalCharacteristicsComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier")) {
          this.getIdentifier().add(castToIdentifier(value));
        } else if (name.equals("administrableDoseForm")) {
          this.administrableDoseForm = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("unitOfPresentation")) {
          this.unitOfPresentation = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("routeOfAdministration")) {
          this.getRouteOfAdministration().add(castToCodeableConcept(value));
        } else if (name.equals("ingredient")) {
          this.getIngredient().add(castToReference(value));
        } else if (name.equals("device")) {
          this.getDevice().add(castToString(value));
        } else if (name.equals("characteristics")) {
          this.getCharacteristics().add((MedicinalProductPharmaceuticalCharacteristicsComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return addIdentifier(); 
        case 1446105202:  return getAdministrableDoseForm(); 
        case -1427765963:  return getUnitOfPresentation(); 
        case 1742084734:  return addRouteOfAdministration(); 
        case -206409263:  return addIngredient(); 
        case -1335157162:  return addDeviceElement();
        case -1529171400:  return addCharacteristics(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case 1446105202: /*administrableDoseForm*/ return new String[] {"CodeableConcept"};
        case -1427765963: /*unitOfPresentation*/ return new String[] {"CodeableConcept"};
        case 1742084734: /*routeOfAdministration*/ return new String[] {"CodeableConcept"};
        case -206409263: /*ingredient*/ return new String[] {"Reference"};
        case -1335157162: /*device*/ return new String[] {"string"};
        case -1529171400: /*characteristics*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("administrableDoseForm")) {
          this.administrableDoseForm = new CodeableConcept();
          return this.administrableDoseForm;
        }
        else if (name.equals("unitOfPresentation")) {
          this.unitOfPresentation = new CodeableConcept();
          return this.unitOfPresentation;
        }
        else if (name.equals("routeOfAdministration")) {
          return addRouteOfAdministration();
        }
        else if (name.equals("ingredient")) {
          return addIngredient();
        }
        else if (name.equals("device")) {
          throw new FHIRException("Cannot call addChild on a primitive type MedicinalProductPharmaceutical.device");
        }
        else if (name.equals("characteristics")) {
          return addCharacteristics();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "MedicinalProductPharmaceutical";

  }

      public MedicinalProductPharmaceutical copy() {
        MedicinalProductPharmaceutical dst = new MedicinalProductPharmaceutical();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.administrableDoseForm = administrableDoseForm == null ? null : administrableDoseForm.copy();
        dst.unitOfPresentation = unitOfPresentation == null ? null : unitOfPresentation.copy();
        if (routeOfAdministration != null) {
          dst.routeOfAdministration = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : routeOfAdministration)
            dst.routeOfAdministration.add(i.copy());
        };
        if (ingredient != null) {
          dst.ingredient = new ArrayList<Reference>();
          for (Reference i : ingredient)
            dst.ingredient.add(i.copy());
        };
        if (device != null) {
          dst.device = new ArrayList<StringType>();
          for (StringType i : device)
            dst.device.add(i.copy());
        };
        if (characteristics != null) {
          dst.characteristics = new ArrayList<MedicinalProductPharmaceuticalCharacteristicsComponent>();
          for (MedicinalProductPharmaceuticalCharacteristicsComponent i : characteristics)
            dst.characteristics.add(i.copy());
        };
        return dst;
      }

      protected MedicinalProductPharmaceutical typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof MedicinalProductPharmaceutical))
          return false;
        MedicinalProductPharmaceutical o = (MedicinalProductPharmaceutical) other_;
        return compareDeep(identifier, o.identifier, true) && compareDeep(administrableDoseForm, o.administrableDoseForm, true)
           && compareDeep(unitOfPresentation, o.unitOfPresentation, true) && compareDeep(routeOfAdministration, o.routeOfAdministration, true)
           && compareDeep(ingredient, o.ingredient, true) && compareDeep(device, o.device, true) && compareDeep(characteristics, o.characteristics, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof MedicinalProductPharmaceutical))
          return false;
        MedicinalProductPharmaceutical o = (MedicinalProductPharmaceutical) other_;
        return compareValues(device, o.device, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, administrableDoseForm
          , unitOfPresentation, routeOfAdministration, ingredient, device, characteristics);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.MedicinalProductPharmaceutical;
   }


}

