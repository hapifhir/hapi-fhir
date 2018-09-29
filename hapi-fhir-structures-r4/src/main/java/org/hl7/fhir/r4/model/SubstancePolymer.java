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

// Generated on Thu, Sep 13, 2018 09:04-0400 for FHIR v3.5.0

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
 * Todo.
 */
@ResourceDef(name="SubstancePolymer", profile="http://hl7.org/fhir/Profile/SubstancePolymer")
public class SubstancePolymer extends DomainResource {

    @Block()
    public static class SubstancePolymerMonomerSetComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Todo.
         */
        @Child(name = "ratioType", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Todo", formalDefinition="Todo." )
        protected CodeableConcept ratioType;

        /**
         * Todo.
         */
        @Child(name = "startingMaterial", type = {}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Todo", formalDefinition="Todo." )
        protected List<SubstancePolymerMonomerSetStartingMaterialComponent> startingMaterial;

        private static final long serialVersionUID = -933825014L;

    /**
     * Constructor
     */
      public SubstancePolymerMonomerSetComponent() {
        super();
      }

        /**
         * @return {@link #ratioType} (Todo.)
         */
        public CodeableConcept getRatioType() { 
          if (this.ratioType == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstancePolymerMonomerSetComponent.ratioType");
            else if (Configuration.doAutoCreate())
              this.ratioType = new CodeableConcept(); // cc
          return this.ratioType;
        }

        public boolean hasRatioType() { 
          return this.ratioType != null && !this.ratioType.isEmpty();
        }

        /**
         * @param value {@link #ratioType} (Todo.)
         */
        public SubstancePolymerMonomerSetComponent setRatioType(CodeableConcept value) { 
          this.ratioType = value;
          return this;
        }

        /**
         * @return {@link #startingMaterial} (Todo.)
         */
        public List<SubstancePolymerMonomerSetStartingMaterialComponent> getStartingMaterial() { 
          if (this.startingMaterial == null)
            this.startingMaterial = new ArrayList<SubstancePolymerMonomerSetStartingMaterialComponent>();
          return this.startingMaterial;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public SubstancePolymerMonomerSetComponent setStartingMaterial(List<SubstancePolymerMonomerSetStartingMaterialComponent> theStartingMaterial) { 
          this.startingMaterial = theStartingMaterial;
          return this;
        }

        public boolean hasStartingMaterial() { 
          if (this.startingMaterial == null)
            return false;
          for (SubstancePolymerMonomerSetStartingMaterialComponent item : this.startingMaterial)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public SubstancePolymerMonomerSetStartingMaterialComponent addStartingMaterial() { //3
          SubstancePolymerMonomerSetStartingMaterialComponent t = new SubstancePolymerMonomerSetStartingMaterialComponent();
          if (this.startingMaterial == null)
            this.startingMaterial = new ArrayList<SubstancePolymerMonomerSetStartingMaterialComponent>();
          this.startingMaterial.add(t);
          return t;
        }

        public SubstancePolymerMonomerSetComponent addStartingMaterial(SubstancePolymerMonomerSetStartingMaterialComponent t) { //3
          if (t == null)
            return this;
          if (this.startingMaterial == null)
            this.startingMaterial = new ArrayList<SubstancePolymerMonomerSetStartingMaterialComponent>();
          this.startingMaterial.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #startingMaterial}, creating it if it does not already exist
         */
        public SubstancePolymerMonomerSetStartingMaterialComponent getStartingMaterialFirstRep() { 
          if (getStartingMaterial().isEmpty()) {
            addStartingMaterial();
          }
          return getStartingMaterial().get(0);
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("ratioType", "CodeableConcept", "Todo.", 0, 1, ratioType));
          children.add(new Property("startingMaterial", "", "Todo.", 0, java.lang.Integer.MAX_VALUE, startingMaterial));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 344937957: /*ratioType*/  return new Property("ratioType", "CodeableConcept", "Todo.", 0, 1, ratioType);
          case 442919303: /*startingMaterial*/  return new Property("startingMaterial", "", "Todo.", 0, java.lang.Integer.MAX_VALUE, startingMaterial);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 344937957: /*ratioType*/ return this.ratioType == null ? new Base[0] : new Base[] {this.ratioType}; // CodeableConcept
        case 442919303: /*startingMaterial*/ return this.startingMaterial == null ? new Base[0] : this.startingMaterial.toArray(new Base[this.startingMaterial.size()]); // SubstancePolymerMonomerSetStartingMaterialComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 344937957: // ratioType
          this.ratioType = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 442919303: // startingMaterial
          this.getStartingMaterial().add((SubstancePolymerMonomerSetStartingMaterialComponent) value); // SubstancePolymerMonomerSetStartingMaterialComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("ratioType")) {
          this.ratioType = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("startingMaterial")) {
          this.getStartingMaterial().add((SubstancePolymerMonomerSetStartingMaterialComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 344937957:  return getRatioType(); 
        case 442919303:  return addStartingMaterial(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 344937957: /*ratioType*/ return new String[] {"CodeableConcept"};
        case 442919303: /*startingMaterial*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("ratioType")) {
          this.ratioType = new CodeableConcept();
          return this.ratioType;
        }
        else if (name.equals("startingMaterial")) {
          return addStartingMaterial();
        }
        else
          return super.addChild(name);
      }

      public SubstancePolymerMonomerSetComponent copy() {
        SubstancePolymerMonomerSetComponent dst = new SubstancePolymerMonomerSetComponent();
        copyValues(dst);
        dst.ratioType = ratioType == null ? null : ratioType.copy();
        if (startingMaterial != null) {
          dst.startingMaterial = new ArrayList<SubstancePolymerMonomerSetStartingMaterialComponent>();
          for (SubstancePolymerMonomerSetStartingMaterialComponent i : startingMaterial)
            dst.startingMaterial.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof SubstancePolymerMonomerSetComponent))
          return false;
        SubstancePolymerMonomerSetComponent o = (SubstancePolymerMonomerSetComponent) other_;
        return compareDeep(ratioType, o.ratioType, true) && compareDeep(startingMaterial, o.startingMaterial, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof SubstancePolymerMonomerSetComponent))
          return false;
        SubstancePolymerMonomerSetComponent o = (SubstancePolymerMonomerSetComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(ratioType, startingMaterial
          );
      }

  public String fhirType() {
    return "SubstancePolymer.monomerSet";

  }

  }

    @Block()
    public static class SubstancePolymerMonomerSetStartingMaterialComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Todo.
         */
        @Child(name = "material", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Todo", formalDefinition="Todo." )
        protected CodeableConcept material;

        /**
         * Todo.
         */
        @Child(name = "type", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Todo", formalDefinition="Todo." )
        protected CodeableConcept type;

        /**
         * Todo.
         */
        @Child(name = "isDefining", type = {BooleanType.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Todo", formalDefinition="Todo." )
        protected BooleanType isDefining;

        /**
         * Todo.
         */
        @Child(name = "amount", type = {SubstanceAmount.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Todo", formalDefinition="Todo." )
        protected SubstanceAmount amount;

        private static final long serialVersionUID = 589614045L;

    /**
     * Constructor
     */
      public SubstancePolymerMonomerSetStartingMaterialComponent() {
        super();
      }

        /**
         * @return {@link #material} (Todo.)
         */
        public CodeableConcept getMaterial() { 
          if (this.material == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstancePolymerMonomerSetStartingMaterialComponent.material");
            else if (Configuration.doAutoCreate())
              this.material = new CodeableConcept(); // cc
          return this.material;
        }

        public boolean hasMaterial() { 
          return this.material != null && !this.material.isEmpty();
        }

        /**
         * @param value {@link #material} (Todo.)
         */
        public SubstancePolymerMonomerSetStartingMaterialComponent setMaterial(CodeableConcept value) { 
          this.material = value;
          return this;
        }

        /**
         * @return {@link #type} (Todo.)
         */
        public CodeableConcept getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstancePolymerMonomerSetStartingMaterialComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new CodeableConcept(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (Todo.)
         */
        public SubstancePolymerMonomerSetStartingMaterialComponent setType(CodeableConcept value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #isDefining} (Todo.). This is the underlying object with id, value and extensions. The accessor "getIsDefining" gives direct access to the value
         */
        public BooleanType getIsDefiningElement() { 
          if (this.isDefining == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstancePolymerMonomerSetStartingMaterialComponent.isDefining");
            else if (Configuration.doAutoCreate())
              this.isDefining = new BooleanType(); // bb
          return this.isDefining;
        }

        public boolean hasIsDefiningElement() { 
          return this.isDefining != null && !this.isDefining.isEmpty();
        }

        public boolean hasIsDefining() { 
          return this.isDefining != null && !this.isDefining.isEmpty();
        }

        /**
         * @param value {@link #isDefining} (Todo.). This is the underlying object with id, value and extensions. The accessor "getIsDefining" gives direct access to the value
         */
        public SubstancePolymerMonomerSetStartingMaterialComponent setIsDefiningElement(BooleanType value) { 
          this.isDefining = value;
          return this;
        }

        /**
         * @return Todo.
         */
        public boolean getIsDefining() { 
          return this.isDefining == null || this.isDefining.isEmpty() ? false : this.isDefining.getValue();
        }

        /**
         * @param value Todo.
         */
        public SubstancePolymerMonomerSetStartingMaterialComponent setIsDefining(boolean value) { 
            if (this.isDefining == null)
              this.isDefining = new BooleanType();
            this.isDefining.setValue(value);
          return this;
        }

        /**
         * @return {@link #amount} (Todo.)
         */
        public SubstanceAmount getAmount() { 
          if (this.amount == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstancePolymerMonomerSetStartingMaterialComponent.amount");
            else if (Configuration.doAutoCreate())
              this.amount = new SubstanceAmount(); // cc
          return this.amount;
        }

        public boolean hasAmount() { 
          return this.amount != null && !this.amount.isEmpty();
        }

        /**
         * @param value {@link #amount} (Todo.)
         */
        public SubstancePolymerMonomerSetStartingMaterialComponent setAmount(SubstanceAmount value) { 
          this.amount = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("material", "CodeableConcept", "Todo.", 0, 1, material));
          children.add(new Property("type", "CodeableConcept", "Todo.", 0, 1, type));
          children.add(new Property("isDefining", "boolean", "Todo.", 0, 1, isDefining));
          children.add(new Property("amount", "SubstanceAmount", "Todo.", 0, 1, amount));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 299066663: /*material*/  return new Property("material", "CodeableConcept", "Todo.", 0, 1, material);
          case 3575610: /*type*/  return new Property("type", "CodeableConcept", "Todo.", 0, 1, type);
          case -141812990: /*isDefining*/  return new Property("isDefining", "boolean", "Todo.", 0, 1, isDefining);
          case -1413853096: /*amount*/  return new Property("amount", "SubstanceAmount", "Todo.", 0, 1, amount);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 299066663: /*material*/ return this.material == null ? new Base[0] : new Base[] {this.material}; // CodeableConcept
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case -141812990: /*isDefining*/ return this.isDefining == null ? new Base[0] : new Base[] {this.isDefining}; // BooleanType
        case -1413853096: /*amount*/ return this.amount == null ? new Base[0] : new Base[] {this.amount}; // SubstanceAmount
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 299066663: // material
          this.material = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -141812990: // isDefining
          this.isDefining = castToBoolean(value); // BooleanType
          return value;
        case -1413853096: // amount
          this.amount = castToSubstanceAmount(value); // SubstanceAmount
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("material")) {
          this.material = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("type")) {
          this.type = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("isDefining")) {
          this.isDefining = castToBoolean(value); // BooleanType
        } else if (name.equals("amount")) {
          this.amount = castToSubstanceAmount(value); // SubstanceAmount
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 299066663:  return getMaterial(); 
        case 3575610:  return getType(); 
        case -141812990:  return getIsDefiningElement();
        case -1413853096:  return getAmount(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 299066663: /*material*/ return new String[] {"CodeableConcept"};
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case -141812990: /*isDefining*/ return new String[] {"boolean"};
        case -1413853096: /*amount*/ return new String[] {"SubstanceAmount"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("material")) {
          this.material = new CodeableConcept();
          return this.material;
        }
        else if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("isDefining")) {
          throw new FHIRException("Cannot call addChild on a primitive type SubstancePolymer.isDefining");
        }
        else if (name.equals("amount")) {
          this.amount = new SubstanceAmount();
          return this.amount;
        }
        else
          return super.addChild(name);
      }

      public SubstancePolymerMonomerSetStartingMaterialComponent copy() {
        SubstancePolymerMonomerSetStartingMaterialComponent dst = new SubstancePolymerMonomerSetStartingMaterialComponent();
        copyValues(dst);
        dst.material = material == null ? null : material.copy();
        dst.type = type == null ? null : type.copy();
        dst.isDefining = isDefining == null ? null : isDefining.copy();
        dst.amount = amount == null ? null : amount.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof SubstancePolymerMonomerSetStartingMaterialComponent))
          return false;
        SubstancePolymerMonomerSetStartingMaterialComponent o = (SubstancePolymerMonomerSetStartingMaterialComponent) other_;
        return compareDeep(material, o.material, true) && compareDeep(type, o.type, true) && compareDeep(isDefining, o.isDefining, true)
           && compareDeep(amount, o.amount, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof SubstancePolymerMonomerSetStartingMaterialComponent))
          return false;
        SubstancePolymerMonomerSetStartingMaterialComponent o = (SubstancePolymerMonomerSetStartingMaterialComponent) other_;
        return compareValues(isDefining, o.isDefining, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(material, type, isDefining
          , amount);
      }

  public String fhirType() {
    return "SubstancePolymer.monomerSet.startingMaterial";

  }

  }

    @Block()
    public static class SubstancePolymerRepeatComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Todo.
         */
        @Child(name = "numberOfUnits", type = {IntegerType.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Todo", formalDefinition="Todo." )
        protected IntegerType numberOfUnits;

        /**
         * Todo.
         */
        @Child(name = "averageMolecularFormula", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Todo", formalDefinition="Todo." )
        protected StringType averageMolecularFormula;

        /**
         * Todo.
         */
        @Child(name = "repeatUnitAmountType", type = {CodeableConcept.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Todo", formalDefinition="Todo." )
        protected CodeableConcept repeatUnitAmountType;

        /**
         * Todo.
         */
        @Child(name = "repeatUnit", type = {}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Todo", formalDefinition="Todo." )
        protected List<SubstancePolymerRepeatRepeatUnitComponent> repeatUnit;

        private static final long serialVersionUID = -988147059L;

    /**
     * Constructor
     */
      public SubstancePolymerRepeatComponent() {
        super();
      }

        /**
         * @return {@link #numberOfUnits} (Todo.). This is the underlying object with id, value and extensions. The accessor "getNumberOfUnits" gives direct access to the value
         */
        public IntegerType getNumberOfUnitsElement() { 
          if (this.numberOfUnits == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstancePolymerRepeatComponent.numberOfUnits");
            else if (Configuration.doAutoCreate())
              this.numberOfUnits = new IntegerType(); // bb
          return this.numberOfUnits;
        }

        public boolean hasNumberOfUnitsElement() { 
          return this.numberOfUnits != null && !this.numberOfUnits.isEmpty();
        }

        public boolean hasNumberOfUnits() { 
          return this.numberOfUnits != null && !this.numberOfUnits.isEmpty();
        }

        /**
         * @param value {@link #numberOfUnits} (Todo.). This is the underlying object with id, value and extensions. The accessor "getNumberOfUnits" gives direct access to the value
         */
        public SubstancePolymerRepeatComponent setNumberOfUnitsElement(IntegerType value) { 
          this.numberOfUnits = value;
          return this;
        }

        /**
         * @return Todo.
         */
        public int getNumberOfUnits() { 
          return this.numberOfUnits == null || this.numberOfUnits.isEmpty() ? 0 : this.numberOfUnits.getValue();
        }

        /**
         * @param value Todo.
         */
        public SubstancePolymerRepeatComponent setNumberOfUnits(int value) { 
            if (this.numberOfUnits == null)
              this.numberOfUnits = new IntegerType();
            this.numberOfUnits.setValue(value);
          return this;
        }

        /**
         * @return {@link #averageMolecularFormula} (Todo.). This is the underlying object with id, value and extensions. The accessor "getAverageMolecularFormula" gives direct access to the value
         */
        public StringType getAverageMolecularFormulaElement() { 
          if (this.averageMolecularFormula == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstancePolymerRepeatComponent.averageMolecularFormula");
            else if (Configuration.doAutoCreate())
              this.averageMolecularFormula = new StringType(); // bb
          return this.averageMolecularFormula;
        }

        public boolean hasAverageMolecularFormulaElement() { 
          return this.averageMolecularFormula != null && !this.averageMolecularFormula.isEmpty();
        }

        public boolean hasAverageMolecularFormula() { 
          return this.averageMolecularFormula != null && !this.averageMolecularFormula.isEmpty();
        }

        /**
         * @param value {@link #averageMolecularFormula} (Todo.). This is the underlying object with id, value and extensions. The accessor "getAverageMolecularFormula" gives direct access to the value
         */
        public SubstancePolymerRepeatComponent setAverageMolecularFormulaElement(StringType value) { 
          this.averageMolecularFormula = value;
          return this;
        }

        /**
         * @return Todo.
         */
        public String getAverageMolecularFormula() { 
          return this.averageMolecularFormula == null ? null : this.averageMolecularFormula.getValue();
        }

        /**
         * @param value Todo.
         */
        public SubstancePolymerRepeatComponent setAverageMolecularFormula(String value) { 
          if (Utilities.noString(value))
            this.averageMolecularFormula = null;
          else {
            if (this.averageMolecularFormula == null)
              this.averageMolecularFormula = new StringType();
            this.averageMolecularFormula.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #repeatUnitAmountType} (Todo.)
         */
        public CodeableConcept getRepeatUnitAmountType() { 
          if (this.repeatUnitAmountType == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstancePolymerRepeatComponent.repeatUnitAmountType");
            else if (Configuration.doAutoCreate())
              this.repeatUnitAmountType = new CodeableConcept(); // cc
          return this.repeatUnitAmountType;
        }

        public boolean hasRepeatUnitAmountType() { 
          return this.repeatUnitAmountType != null && !this.repeatUnitAmountType.isEmpty();
        }

        /**
         * @param value {@link #repeatUnitAmountType} (Todo.)
         */
        public SubstancePolymerRepeatComponent setRepeatUnitAmountType(CodeableConcept value) { 
          this.repeatUnitAmountType = value;
          return this;
        }

        /**
         * @return {@link #repeatUnit} (Todo.)
         */
        public List<SubstancePolymerRepeatRepeatUnitComponent> getRepeatUnit() { 
          if (this.repeatUnit == null)
            this.repeatUnit = new ArrayList<SubstancePolymerRepeatRepeatUnitComponent>();
          return this.repeatUnit;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public SubstancePolymerRepeatComponent setRepeatUnit(List<SubstancePolymerRepeatRepeatUnitComponent> theRepeatUnit) { 
          this.repeatUnit = theRepeatUnit;
          return this;
        }

        public boolean hasRepeatUnit() { 
          if (this.repeatUnit == null)
            return false;
          for (SubstancePolymerRepeatRepeatUnitComponent item : this.repeatUnit)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public SubstancePolymerRepeatRepeatUnitComponent addRepeatUnit() { //3
          SubstancePolymerRepeatRepeatUnitComponent t = new SubstancePolymerRepeatRepeatUnitComponent();
          if (this.repeatUnit == null)
            this.repeatUnit = new ArrayList<SubstancePolymerRepeatRepeatUnitComponent>();
          this.repeatUnit.add(t);
          return t;
        }

        public SubstancePolymerRepeatComponent addRepeatUnit(SubstancePolymerRepeatRepeatUnitComponent t) { //3
          if (t == null)
            return this;
          if (this.repeatUnit == null)
            this.repeatUnit = new ArrayList<SubstancePolymerRepeatRepeatUnitComponent>();
          this.repeatUnit.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #repeatUnit}, creating it if it does not already exist
         */
        public SubstancePolymerRepeatRepeatUnitComponent getRepeatUnitFirstRep() { 
          if (getRepeatUnit().isEmpty()) {
            addRepeatUnit();
          }
          return getRepeatUnit().get(0);
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("numberOfUnits", "integer", "Todo.", 0, 1, numberOfUnits));
          children.add(new Property("averageMolecularFormula", "string", "Todo.", 0, 1, averageMolecularFormula));
          children.add(new Property("repeatUnitAmountType", "CodeableConcept", "Todo.", 0, 1, repeatUnitAmountType));
          children.add(new Property("repeatUnit", "", "Todo.", 0, java.lang.Integer.MAX_VALUE, repeatUnit));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -1321430961: /*numberOfUnits*/  return new Property("numberOfUnits", "integer", "Todo.", 0, 1, numberOfUnits);
          case 111461715: /*averageMolecularFormula*/  return new Property("averageMolecularFormula", "string", "Todo.", 0, 1, averageMolecularFormula);
          case -1994025263: /*repeatUnitAmountType*/  return new Property("repeatUnitAmountType", "CodeableConcept", "Todo.", 0, 1, repeatUnitAmountType);
          case 1159607743: /*repeatUnit*/  return new Property("repeatUnit", "", "Todo.", 0, java.lang.Integer.MAX_VALUE, repeatUnit);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1321430961: /*numberOfUnits*/ return this.numberOfUnits == null ? new Base[0] : new Base[] {this.numberOfUnits}; // IntegerType
        case 111461715: /*averageMolecularFormula*/ return this.averageMolecularFormula == null ? new Base[0] : new Base[] {this.averageMolecularFormula}; // StringType
        case -1994025263: /*repeatUnitAmountType*/ return this.repeatUnitAmountType == null ? new Base[0] : new Base[] {this.repeatUnitAmountType}; // CodeableConcept
        case 1159607743: /*repeatUnit*/ return this.repeatUnit == null ? new Base[0] : this.repeatUnit.toArray(new Base[this.repeatUnit.size()]); // SubstancePolymerRepeatRepeatUnitComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1321430961: // numberOfUnits
          this.numberOfUnits = castToInteger(value); // IntegerType
          return value;
        case 111461715: // averageMolecularFormula
          this.averageMolecularFormula = castToString(value); // StringType
          return value;
        case -1994025263: // repeatUnitAmountType
          this.repeatUnitAmountType = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 1159607743: // repeatUnit
          this.getRepeatUnit().add((SubstancePolymerRepeatRepeatUnitComponent) value); // SubstancePolymerRepeatRepeatUnitComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("numberOfUnits")) {
          this.numberOfUnits = castToInteger(value); // IntegerType
        } else if (name.equals("averageMolecularFormula")) {
          this.averageMolecularFormula = castToString(value); // StringType
        } else if (name.equals("repeatUnitAmountType")) {
          this.repeatUnitAmountType = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("repeatUnit")) {
          this.getRepeatUnit().add((SubstancePolymerRepeatRepeatUnitComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1321430961:  return getNumberOfUnitsElement();
        case 111461715:  return getAverageMolecularFormulaElement();
        case -1994025263:  return getRepeatUnitAmountType(); 
        case 1159607743:  return addRepeatUnit(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1321430961: /*numberOfUnits*/ return new String[] {"integer"};
        case 111461715: /*averageMolecularFormula*/ return new String[] {"string"};
        case -1994025263: /*repeatUnitAmountType*/ return new String[] {"CodeableConcept"};
        case 1159607743: /*repeatUnit*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("numberOfUnits")) {
          throw new FHIRException("Cannot call addChild on a primitive type SubstancePolymer.numberOfUnits");
        }
        else if (name.equals("averageMolecularFormula")) {
          throw new FHIRException("Cannot call addChild on a primitive type SubstancePolymer.averageMolecularFormula");
        }
        else if (name.equals("repeatUnitAmountType")) {
          this.repeatUnitAmountType = new CodeableConcept();
          return this.repeatUnitAmountType;
        }
        else if (name.equals("repeatUnit")) {
          return addRepeatUnit();
        }
        else
          return super.addChild(name);
      }

      public SubstancePolymerRepeatComponent copy() {
        SubstancePolymerRepeatComponent dst = new SubstancePolymerRepeatComponent();
        copyValues(dst);
        dst.numberOfUnits = numberOfUnits == null ? null : numberOfUnits.copy();
        dst.averageMolecularFormula = averageMolecularFormula == null ? null : averageMolecularFormula.copy();
        dst.repeatUnitAmountType = repeatUnitAmountType == null ? null : repeatUnitAmountType.copy();
        if (repeatUnit != null) {
          dst.repeatUnit = new ArrayList<SubstancePolymerRepeatRepeatUnitComponent>();
          for (SubstancePolymerRepeatRepeatUnitComponent i : repeatUnit)
            dst.repeatUnit.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof SubstancePolymerRepeatComponent))
          return false;
        SubstancePolymerRepeatComponent o = (SubstancePolymerRepeatComponent) other_;
        return compareDeep(numberOfUnits, o.numberOfUnits, true) && compareDeep(averageMolecularFormula, o.averageMolecularFormula, true)
           && compareDeep(repeatUnitAmountType, o.repeatUnitAmountType, true) && compareDeep(repeatUnit, o.repeatUnit, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof SubstancePolymerRepeatComponent))
          return false;
        SubstancePolymerRepeatComponent o = (SubstancePolymerRepeatComponent) other_;
        return compareValues(numberOfUnits, o.numberOfUnits, true) && compareValues(averageMolecularFormula, o.averageMolecularFormula, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(numberOfUnits, averageMolecularFormula
          , repeatUnitAmountType, repeatUnit);
      }

  public String fhirType() {
    return "SubstancePolymer.repeat";

  }

  }

    @Block()
    public static class SubstancePolymerRepeatRepeatUnitComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Todo.
         */
        @Child(name = "orientationOfPolymerisation", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Todo", formalDefinition="Todo." )
        protected CodeableConcept orientationOfPolymerisation;

        /**
         * Todo.
         */
        @Child(name = "repeatUnit", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Todo", formalDefinition="Todo." )
        protected StringType repeatUnit;

        /**
         * Todo.
         */
        @Child(name = "amount", type = {SubstanceAmount.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Todo", formalDefinition="Todo." )
        protected SubstanceAmount amount;

        /**
         * Todo.
         */
        @Child(name = "degreeOfPolymerisation", type = {}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Todo", formalDefinition="Todo." )
        protected List<SubstancePolymerRepeatRepeatUnitDegreeOfPolymerisationComponent> degreeOfPolymerisation;

        /**
         * Todo.
         */
        @Child(name = "structuralRepresentation", type = {}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Todo", formalDefinition="Todo." )
        protected List<SubstancePolymerRepeatRepeatUnitStructuralRepresentationComponent> structuralRepresentation;

        private static final long serialVersionUID = -1823741061L;

    /**
     * Constructor
     */
      public SubstancePolymerRepeatRepeatUnitComponent() {
        super();
      }

        /**
         * @return {@link #orientationOfPolymerisation} (Todo.)
         */
        public CodeableConcept getOrientationOfPolymerisation() { 
          if (this.orientationOfPolymerisation == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstancePolymerRepeatRepeatUnitComponent.orientationOfPolymerisation");
            else if (Configuration.doAutoCreate())
              this.orientationOfPolymerisation = new CodeableConcept(); // cc
          return this.orientationOfPolymerisation;
        }

        public boolean hasOrientationOfPolymerisation() { 
          return this.orientationOfPolymerisation != null && !this.orientationOfPolymerisation.isEmpty();
        }

        /**
         * @param value {@link #orientationOfPolymerisation} (Todo.)
         */
        public SubstancePolymerRepeatRepeatUnitComponent setOrientationOfPolymerisation(CodeableConcept value) { 
          this.orientationOfPolymerisation = value;
          return this;
        }

        /**
         * @return {@link #repeatUnit} (Todo.). This is the underlying object with id, value and extensions. The accessor "getRepeatUnit" gives direct access to the value
         */
        public StringType getRepeatUnitElement() { 
          if (this.repeatUnit == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstancePolymerRepeatRepeatUnitComponent.repeatUnit");
            else if (Configuration.doAutoCreate())
              this.repeatUnit = new StringType(); // bb
          return this.repeatUnit;
        }

        public boolean hasRepeatUnitElement() { 
          return this.repeatUnit != null && !this.repeatUnit.isEmpty();
        }

        public boolean hasRepeatUnit() { 
          return this.repeatUnit != null && !this.repeatUnit.isEmpty();
        }

        /**
         * @param value {@link #repeatUnit} (Todo.). This is the underlying object with id, value and extensions. The accessor "getRepeatUnit" gives direct access to the value
         */
        public SubstancePolymerRepeatRepeatUnitComponent setRepeatUnitElement(StringType value) { 
          this.repeatUnit = value;
          return this;
        }

        /**
         * @return Todo.
         */
        public String getRepeatUnit() { 
          return this.repeatUnit == null ? null : this.repeatUnit.getValue();
        }

        /**
         * @param value Todo.
         */
        public SubstancePolymerRepeatRepeatUnitComponent setRepeatUnit(String value) { 
          if (Utilities.noString(value))
            this.repeatUnit = null;
          else {
            if (this.repeatUnit == null)
              this.repeatUnit = new StringType();
            this.repeatUnit.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #amount} (Todo.)
         */
        public SubstanceAmount getAmount() { 
          if (this.amount == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstancePolymerRepeatRepeatUnitComponent.amount");
            else if (Configuration.doAutoCreate())
              this.amount = new SubstanceAmount(); // cc
          return this.amount;
        }

        public boolean hasAmount() { 
          return this.amount != null && !this.amount.isEmpty();
        }

        /**
         * @param value {@link #amount} (Todo.)
         */
        public SubstancePolymerRepeatRepeatUnitComponent setAmount(SubstanceAmount value) { 
          this.amount = value;
          return this;
        }

        /**
         * @return {@link #degreeOfPolymerisation} (Todo.)
         */
        public List<SubstancePolymerRepeatRepeatUnitDegreeOfPolymerisationComponent> getDegreeOfPolymerisation() { 
          if (this.degreeOfPolymerisation == null)
            this.degreeOfPolymerisation = new ArrayList<SubstancePolymerRepeatRepeatUnitDegreeOfPolymerisationComponent>();
          return this.degreeOfPolymerisation;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public SubstancePolymerRepeatRepeatUnitComponent setDegreeOfPolymerisation(List<SubstancePolymerRepeatRepeatUnitDegreeOfPolymerisationComponent> theDegreeOfPolymerisation) { 
          this.degreeOfPolymerisation = theDegreeOfPolymerisation;
          return this;
        }

        public boolean hasDegreeOfPolymerisation() { 
          if (this.degreeOfPolymerisation == null)
            return false;
          for (SubstancePolymerRepeatRepeatUnitDegreeOfPolymerisationComponent item : this.degreeOfPolymerisation)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public SubstancePolymerRepeatRepeatUnitDegreeOfPolymerisationComponent addDegreeOfPolymerisation() { //3
          SubstancePolymerRepeatRepeatUnitDegreeOfPolymerisationComponent t = new SubstancePolymerRepeatRepeatUnitDegreeOfPolymerisationComponent();
          if (this.degreeOfPolymerisation == null)
            this.degreeOfPolymerisation = new ArrayList<SubstancePolymerRepeatRepeatUnitDegreeOfPolymerisationComponent>();
          this.degreeOfPolymerisation.add(t);
          return t;
        }

        public SubstancePolymerRepeatRepeatUnitComponent addDegreeOfPolymerisation(SubstancePolymerRepeatRepeatUnitDegreeOfPolymerisationComponent t) { //3
          if (t == null)
            return this;
          if (this.degreeOfPolymerisation == null)
            this.degreeOfPolymerisation = new ArrayList<SubstancePolymerRepeatRepeatUnitDegreeOfPolymerisationComponent>();
          this.degreeOfPolymerisation.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #degreeOfPolymerisation}, creating it if it does not already exist
         */
        public SubstancePolymerRepeatRepeatUnitDegreeOfPolymerisationComponent getDegreeOfPolymerisationFirstRep() { 
          if (getDegreeOfPolymerisation().isEmpty()) {
            addDegreeOfPolymerisation();
          }
          return getDegreeOfPolymerisation().get(0);
        }

        /**
         * @return {@link #structuralRepresentation} (Todo.)
         */
        public List<SubstancePolymerRepeatRepeatUnitStructuralRepresentationComponent> getStructuralRepresentation() { 
          if (this.structuralRepresentation == null)
            this.structuralRepresentation = new ArrayList<SubstancePolymerRepeatRepeatUnitStructuralRepresentationComponent>();
          return this.structuralRepresentation;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public SubstancePolymerRepeatRepeatUnitComponent setStructuralRepresentation(List<SubstancePolymerRepeatRepeatUnitStructuralRepresentationComponent> theStructuralRepresentation) { 
          this.structuralRepresentation = theStructuralRepresentation;
          return this;
        }

        public boolean hasStructuralRepresentation() { 
          if (this.structuralRepresentation == null)
            return false;
          for (SubstancePolymerRepeatRepeatUnitStructuralRepresentationComponent item : this.structuralRepresentation)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public SubstancePolymerRepeatRepeatUnitStructuralRepresentationComponent addStructuralRepresentation() { //3
          SubstancePolymerRepeatRepeatUnitStructuralRepresentationComponent t = new SubstancePolymerRepeatRepeatUnitStructuralRepresentationComponent();
          if (this.structuralRepresentation == null)
            this.structuralRepresentation = new ArrayList<SubstancePolymerRepeatRepeatUnitStructuralRepresentationComponent>();
          this.structuralRepresentation.add(t);
          return t;
        }

        public SubstancePolymerRepeatRepeatUnitComponent addStructuralRepresentation(SubstancePolymerRepeatRepeatUnitStructuralRepresentationComponent t) { //3
          if (t == null)
            return this;
          if (this.structuralRepresentation == null)
            this.structuralRepresentation = new ArrayList<SubstancePolymerRepeatRepeatUnitStructuralRepresentationComponent>();
          this.structuralRepresentation.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #structuralRepresentation}, creating it if it does not already exist
         */
        public SubstancePolymerRepeatRepeatUnitStructuralRepresentationComponent getStructuralRepresentationFirstRep() { 
          if (getStructuralRepresentation().isEmpty()) {
            addStructuralRepresentation();
          }
          return getStructuralRepresentation().get(0);
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("orientationOfPolymerisation", "CodeableConcept", "Todo.", 0, 1, orientationOfPolymerisation));
          children.add(new Property("repeatUnit", "string", "Todo.", 0, 1, repeatUnit));
          children.add(new Property("amount", "SubstanceAmount", "Todo.", 0, 1, amount));
          children.add(new Property("degreeOfPolymerisation", "", "Todo.", 0, java.lang.Integer.MAX_VALUE, degreeOfPolymerisation));
          children.add(new Property("structuralRepresentation", "", "Todo.", 0, java.lang.Integer.MAX_VALUE, structuralRepresentation));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 1795817828: /*orientationOfPolymerisation*/  return new Property("orientationOfPolymerisation", "CodeableConcept", "Todo.", 0, 1, orientationOfPolymerisation);
          case 1159607743: /*repeatUnit*/  return new Property("repeatUnit", "string", "Todo.", 0, 1, repeatUnit);
          case -1413853096: /*amount*/  return new Property("amount", "SubstanceAmount", "Todo.", 0, 1, amount);
          case -159251872: /*degreeOfPolymerisation*/  return new Property("degreeOfPolymerisation", "", "Todo.", 0, java.lang.Integer.MAX_VALUE, degreeOfPolymerisation);
          case 14311178: /*structuralRepresentation*/  return new Property("structuralRepresentation", "", "Todo.", 0, java.lang.Integer.MAX_VALUE, structuralRepresentation);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 1795817828: /*orientationOfPolymerisation*/ return this.orientationOfPolymerisation == null ? new Base[0] : new Base[] {this.orientationOfPolymerisation}; // CodeableConcept
        case 1159607743: /*repeatUnit*/ return this.repeatUnit == null ? new Base[0] : new Base[] {this.repeatUnit}; // StringType
        case -1413853096: /*amount*/ return this.amount == null ? new Base[0] : new Base[] {this.amount}; // SubstanceAmount
        case -159251872: /*degreeOfPolymerisation*/ return this.degreeOfPolymerisation == null ? new Base[0] : this.degreeOfPolymerisation.toArray(new Base[this.degreeOfPolymerisation.size()]); // SubstancePolymerRepeatRepeatUnitDegreeOfPolymerisationComponent
        case 14311178: /*structuralRepresentation*/ return this.structuralRepresentation == null ? new Base[0] : this.structuralRepresentation.toArray(new Base[this.structuralRepresentation.size()]); // SubstancePolymerRepeatRepeatUnitStructuralRepresentationComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 1795817828: // orientationOfPolymerisation
          this.orientationOfPolymerisation = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 1159607743: // repeatUnit
          this.repeatUnit = castToString(value); // StringType
          return value;
        case -1413853096: // amount
          this.amount = castToSubstanceAmount(value); // SubstanceAmount
          return value;
        case -159251872: // degreeOfPolymerisation
          this.getDegreeOfPolymerisation().add((SubstancePolymerRepeatRepeatUnitDegreeOfPolymerisationComponent) value); // SubstancePolymerRepeatRepeatUnitDegreeOfPolymerisationComponent
          return value;
        case 14311178: // structuralRepresentation
          this.getStructuralRepresentation().add((SubstancePolymerRepeatRepeatUnitStructuralRepresentationComponent) value); // SubstancePolymerRepeatRepeatUnitStructuralRepresentationComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("orientationOfPolymerisation")) {
          this.orientationOfPolymerisation = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("repeatUnit")) {
          this.repeatUnit = castToString(value); // StringType
        } else if (name.equals("amount")) {
          this.amount = castToSubstanceAmount(value); // SubstanceAmount
        } else if (name.equals("degreeOfPolymerisation")) {
          this.getDegreeOfPolymerisation().add((SubstancePolymerRepeatRepeatUnitDegreeOfPolymerisationComponent) value);
        } else if (name.equals("structuralRepresentation")) {
          this.getStructuralRepresentation().add((SubstancePolymerRepeatRepeatUnitStructuralRepresentationComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1795817828:  return getOrientationOfPolymerisation(); 
        case 1159607743:  return getRepeatUnitElement();
        case -1413853096:  return getAmount(); 
        case -159251872:  return addDegreeOfPolymerisation(); 
        case 14311178:  return addStructuralRepresentation(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1795817828: /*orientationOfPolymerisation*/ return new String[] {"CodeableConcept"};
        case 1159607743: /*repeatUnit*/ return new String[] {"string"};
        case -1413853096: /*amount*/ return new String[] {"SubstanceAmount"};
        case -159251872: /*degreeOfPolymerisation*/ return new String[] {};
        case 14311178: /*structuralRepresentation*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("orientationOfPolymerisation")) {
          this.orientationOfPolymerisation = new CodeableConcept();
          return this.orientationOfPolymerisation;
        }
        else if (name.equals("repeatUnit")) {
          throw new FHIRException("Cannot call addChild on a primitive type SubstancePolymer.repeatUnit");
        }
        else if (name.equals("amount")) {
          this.amount = new SubstanceAmount();
          return this.amount;
        }
        else if (name.equals("degreeOfPolymerisation")) {
          return addDegreeOfPolymerisation();
        }
        else if (name.equals("structuralRepresentation")) {
          return addStructuralRepresentation();
        }
        else
          return super.addChild(name);
      }

      public SubstancePolymerRepeatRepeatUnitComponent copy() {
        SubstancePolymerRepeatRepeatUnitComponent dst = new SubstancePolymerRepeatRepeatUnitComponent();
        copyValues(dst);
        dst.orientationOfPolymerisation = orientationOfPolymerisation == null ? null : orientationOfPolymerisation.copy();
        dst.repeatUnit = repeatUnit == null ? null : repeatUnit.copy();
        dst.amount = amount == null ? null : amount.copy();
        if (degreeOfPolymerisation != null) {
          dst.degreeOfPolymerisation = new ArrayList<SubstancePolymerRepeatRepeatUnitDegreeOfPolymerisationComponent>();
          for (SubstancePolymerRepeatRepeatUnitDegreeOfPolymerisationComponent i : degreeOfPolymerisation)
            dst.degreeOfPolymerisation.add(i.copy());
        };
        if (structuralRepresentation != null) {
          dst.structuralRepresentation = new ArrayList<SubstancePolymerRepeatRepeatUnitStructuralRepresentationComponent>();
          for (SubstancePolymerRepeatRepeatUnitStructuralRepresentationComponent i : structuralRepresentation)
            dst.structuralRepresentation.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof SubstancePolymerRepeatRepeatUnitComponent))
          return false;
        SubstancePolymerRepeatRepeatUnitComponent o = (SubstancePolymerRepeatRepeatUnitComponent) other_;
        return compareDeep(orientationOfPolymerisation, o.orientationOfPolymerisation, true) && compareDeep(repeatUnit, o.repeatUnit, true)
           && compareDeep(amount, o.amount, true) && compareDeep(degreeOfPolymerisation, o.degreeOfPolymerisation, true)
           && compareDeep(structuralRepresentation, o.structuralRepresentation, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof SubstancePolymerRepeatRepeatUnitComponent))
          return false;
        SubstancePolymerRepeatRepeatUnitComponent o = (SubstancePolymerRepeatRepeatUnitComponent) other_;
        return compareValues(repeatUnit, o.repeatUnit, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(orientationOfPolymerisation
          , repeatUnit, amount, degreeOfPolymerisation, structuralRepresentation);
      }

  public String fhirType() {
    return "SubstancePolymer.repeat.repeatUnit";

  }

  }

    @Block()
    public static class SubstancePolymerRepeatRepeatUnitDegreeOfPolymerisationComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Todo.
         */
        @Child(name = "degree", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Todo", formalDefinition="Todo." )
        protected CodeableConcept degree;

        /**
         * Todo.
         */
        @Child(name = "amount", type = {SubstanceAmount.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Todo", formalDefinition="Todo." )
        protected SubstanceAmount amount;

        private static final long serialVersionUID = -1487452773L;

    /**
     * Constructor
     */
      public SubstancePolymerRepeatRepeatUnitDegreeOfPolymerisationComponent() {
        super();
      }

        /**
         * @return {@link #degree} (Todo.)
         */
        public CodeableConcept getDegree() { 
          if (this.degree == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstancePolymerRepeatRepeatUnitDegreeOfPolymerisationComponent.degree");
            else if (Configuration.doAutoCreate())
              this.degree = new CodeableConcept(); // cc
          return this.degree;
        }

        public boolean hasDegree() { 
          return this.degree != null && !this.degree.isEmpty();
        }

        /**
         * @param value {@link #degree} (Todo.)
         */
        public SubstancePolymerRepeatRepeatUnitDegreeOfPolymerisationComponent setDegree(CodeableConcept value) { 
          this.degree = value;
          return this;
        }

        /**
         * @return {@link #amount} (Todo.)
         */
        public SubstanceAmount getAmount() { 
          if (this.amount == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstancePolymerRepeatRepeatUnitDegreeOfPolymerisationComponent.amount");
            else if (Configuration.doAutoCreate())
              this.amount = new SubstanceAmount(); // cc
          return this.amount;
        }

        public boolean hasAmount() { 
          return this.amount != null && !this.amount.isEmpty();
        }

        /**
         * @param value {@link #amount} (Todo.)
         */
        public SubstancePolymerRepeatRepeatUnitDegreeOfPolymerisationComponent setAmount(SubstanceAmount value) { 
          this.amount = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("degree", "CodeableConcept", "Todo.", 0, 1, degree));
          children.add(new Property("amount", "SubstanceAmount", "Todo.", 0, 1, amount));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -1335595316: /*degree*/  return new Property("degree", "CodeableConcept", "Todo.", 0, 1, degree);
          case -1413853096: /*amount*/  return new Property("amount", "SubstanceAmount", "Todo.", 0, 1, amount);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1335595316: /*degree*/ return this.degree == null ? new Base[0] : new Base[] {this.degree}; // CodeableConcept
        case -1413853096: /*amount*/ return this.amount == null ? new Base[0] : new Base[] {this.amount}; // SubstanceAmount
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1335595316: // degree
          this.degree = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1413853096: // amount
          this.amount = castToSubstanceAmount(value); // SubstanceAmount
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("degree")) {
          this.degree = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("amount")) {
          this.amount = castToSubstanceAmount(value); // SubstanceAmount
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1335595316:  return getDegree(); 
        case -1413853096:  return getAmount(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1335595316: /*degree*/ return new String[] {"CodeableConcept"};
        case -1413853096: /*amount*/ return new String[] {"SubstanceAmount"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("degree")) {
          this.degree = new CodeableConcept();
          return this.degree;
        }
        else if (name.equals("amount")) {
          this.amount = new SubstanceAmount();
          return this.amount;
        }
        else
          return super.addChild(name);
      }

      public SubstancePolymerRepeatRepeatUnitDegreeOfPolymerisationComponent copy() {
        SubstancePolymerRepeatRepeatUnitDegreeOfPolymerisationComponent dst = new SubstancePolymerRepeatRepeatUnitDegreeOfPolymerisationComponent();
        copyValues(dst);
        dst.degree = degree == null ? null : degree.copy();
        dst.amount = amount == null ? null : amount.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof SubstancePolymerRepeatRepeatUnitDegreeOfPolymerisationComponent))
          return false;
        SubstancePolymerRepeatRepeatUnitDegreeOfPolymerisationComponent o = (SubstancePolymerRepeatRepeatUnitDegreeOfPolymerisationComponent) other_;
        return compareDeep(degree, o.degree, true) && compareDeep(amount, o.amount, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof SubstancePolymerRepeatRepeatUnitDegreeOfPolymerisationComponent))
          return false;
        SubstancePolymerRepeatRepeatUnitDegreeOfPolymerisationComponent o = (SubstancePolymerRepeatRepeatUnitDegreeOfPolymerisationComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(degree, amount);
      }

  public String fhirType() {
    return "SubstancePolymer.repeat.repeatUnit.degreeOfPolymerisation";

  }

  }

    @Block()
    public static class SubstancePolymerRepeatRepeatUnitStructuralRepresentationComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Todo.
         */
        @Child(name = "type", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Todo", formalDefinition="Todo." )
        protected CodeableConcept type;

        /**
         * Todo.
         */
        @Child(name = "representation", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Todo", formalDefinition="Todo." )
        protected StringType representation;

        /**
         * Todo.
         */
        @Child(name = "attachment", type = {Attachment.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Todo", formalDefinition="Todo." )
        protected Attachment attachment;

        private static final long serialVersionUID = 167954495L;

    /**
     * Constructor
     */
      public SubstancePolymerRepeatRepeatUnitStructuralRepresentationComponent() {
        super();
      }

        /**
         * @return {@link #type} (Todo.)
         */
        public CodeableConcept getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstancePolymerRepeatRepeatUnitStructuralRepresentationComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new CodeableConcept(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (Todo.)
         */
        public SubstancePolymerRepeatRepeatUnitStructuralRepresentationComponent setType(CodeableConcept value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #representation} (Todo.). This is the underlying object with id, value and extensions. The accessor "getRepresentation" gives direct access to the value
         */
        public StringType getRepresentationElement() { 
          if (this.representation == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstancePolymerRepeatRepeatUnitStructuralRepresentationComponent.representation");
            else if (Configuration.doAutoCreate())
              this.representation = new StringType(); // bb
          return this.representation;
        }

        public boolean hasRepresentationElement() { 
          return this.representation != null && !this.representation.isEmpty();
        }

        public boolean hasRepresentation() { 
          return this.representation != null && !this.representation.isEmpty();
        }

        /**
         * @param value {@link #representation} (Todo.). This is the underlying object with id, value and extensions. The accessor "getRepresentation" gives direct access to the value
         */
        public SubstancePolymerRepeatRepeatUnitStructuralRepresentationComponent setRepresentationElement(StringType value) { 
          this.representation = value;
          return this;
        }

        /**
         * @return Todo.
         */
        public String getRepresentation() { 
          return this.representation == null ? null : this.representation.getValue();
        }

        /**
         * @param value Todo.
         */
        public SubstancePolymerRepeatRepeatUnitStructuralRepresentationComponent setRepresentation(String value) { 
          if (Utilities.noString(value))
            this.representation = null;
          else {
            if (this.representation == null)
              this.representation = new StringType();
            this.representation.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #attachment} (Todo.)
         */
        public Attachment getAttachment() { 
          if (this.attachment == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstancePolymerRepeatRepeatUnitStructuralRepresentationComponent.attachment");
            else if (Configuration.doAutoCreate())
              this.attachment = new Attachment(); // cc
          return this.attachment;
        }

        public boolean hasAttachment() { 
          return this.attachment != null && !this.attachment.isEmpty();
        }

        /**
         * @param value {@link #attachment} (Todo.)
         */
        public SubstancePolymerRepeatRepeatUnitStructuralRepresentationComponent setAttachment(Attachment value) { 
          this.attachment = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("type", "CodeableConcept", "Todo.", 0, 1, type));
          children.add(new Property("representation", "string", "Todo.", 0, 1, representation));
          children.add(new Property("attachment", "Attachment", "Todo.", 0, 1, attachment));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3575610: /*type*/  return new Property("type", "CodeableConcept", "Todo.", 0, 1, type);
          case -671065907: /*representation*/  return new Property("representation", "string", "Todo.", 0, 1, representation);
          case -1963501277: /*attachment*/  return new Property("attachment", "Attachment", "Todo.", 0, 1, attachment);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case -671065907: /*representation*/ return this.representation == null ? new Base[0] : new Base[] {this.representation}; // StringType
        case -1963501277: /*attachment*/ return this.attachment == null ? new Base[0] : new Base[] {this.attachment}; // Attachment
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -671065907: // representation
          this.representation = castToString(value); // StringType
          return value;
        case -1963501277: // attachment
          this.attachment = castToAttachment(value); // Attachment
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type")) {
          this.type = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("representation")) {
          this.representation = castToString(value); // StringType
        } else if (name.equals("attachment")) {
          this.attachment = castToAttachment(value); // Attachment
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610:  return getType(); 
        case -671065907:  return getRepresentationElement();
        case -1963501277:  return getAttachment(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case -671065907: /*representation*/ return new String[] {"string"};
        case -1963501277: /*attachment*/ return new String[] {"Attachment"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("representation")) {
          throw new FHIRException("Cannot call addChild on a primitive type SubstancePolymer.representation");
        }
        else if (name.equals("attachment")) {
          this.attachment = new Attachment();
          return this.attachment;
        }
        else
          return super.addChild(name);
      }

      public SubstancePolymerRepeatRepeatUnitStructuralRepresentationComponent copy() {
        SubstancePolymerRepeatRepeatUnitStructuralRepresentationComponent dst = new SubstancePolymerRepeatRepeatUnitStructuralRepresentationComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.representation = representation == null ? null : representation.copy();
        dst.attachment = attachment == null ? null : attachment.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof SubstancePolymerRepeatRepeatUnitStructuralRepresentationComponent))
          return false;
        SubstancePolymerRepeatRepeatUnitStructuralRepresentationComponent o = (SubstancePolymerRepeatRepeatUnitStructuralRepresentationComponent) other_;
        return compareDeep(type, o.type, true) && compareDeep(representation, o.representation, true) && compareDeep(attachment, o.attachment, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof SubstancePolymerRepeatRepeatUnitStructuralRepresentationComponent))
          return false;
        SubstancePolymerRepeatRepeatUnitStructuralRepresentationComponent o = (SubstancePolymerRepeatRepeatUnitStructuralRepresentationComponent) other_;
        return compareValues(representation, o.representation, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(type, representation, attachment
          );
      }

  public String fhirType() {
    return "SubstancePolymer.repeat.repeatUnit.structuralRepresentation";

  }

  }

    /**
     * Todo.
     */
    @Child(name = "class", type = {CodeableConcept.class}, order=0, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Todo", formalDefinition="Todo." )
    protected CodeableConcept class_;

    /**
     * Todo.
     */
    @Child(name = "geometry", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Todo", formalDefinition="Todo." )
    protected CodeableConcept geometry;

    /**
     * Todo.
     */
    @Child(name = "copolymerConnectivity", type = {CodeableConcept.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Todo", formalDefinition="Todo." )
    protected List<CodeableConcept> copolymerConnectivity;

    /**
     * Todo.
     */
    @Child(name = "modification", type = {StringType.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Todo", formalDefinition="Todo." )
    protected List<StringType> modification;

    /**
     * Todo.
     */
    @Child(name = "monomerSet", type = {}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Todo", formalDefinition="Todo." )
    protected List<SubstancePolymerMonomerSetComponent> monomerSet;

    /**
     * Todo.
     */
    @Child(name = "repeat", type = {}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Todo", formalDefinition="Todo." )
    protected List<SubstancePolymerRepeatComponent> repeat;

    private static final long serialVersionUID = -58301650L;

  /**
   * Constructor
   */
    public SubstancePolymer() {
      super();
    }

    /**
     * @return {@link #class_} (Todo.)
     */
    public CodeableConcept getClass_() { 
      if (this.class_ == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SubstancePolymer.class_");
        else if (Configuration.doAutoCreate())
          this.class_ = new CodeableConcept(); // cc
      return this.class_;
    }

    public boolean hasClass_() { 
      return this.class_ != null && !this.class_.isEmpty();
    }

    /**
     * @param value {@link #class_} (Todo.)
     */
    public SubstancePolymer setClass_(CodeableConcept value) { 
      this.class_ = value;
      return this;
    }

    /**
     * @return {@link #geometry} (Todo.)
     */
    public CodeableConcept getGeometry() { 
      if (this.geometry == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SubstancePolymer.geometry");
        else if (Configuration.doAutoCreate())
          this.geometry = new CodeableConcept(); // cc
      return this.geometry;
    }

    public boolean hasGeometry() { 
      return this.geometry != null && !this.geometry.isEmpty();
    }

    /**
     * @param value {@link #geometry} (Todo.)
     */
    public SubstancePolymer setGeometry(CodeableConcept value) { 
      this.geometry = value;
      return this;
    }

    /**
     * @return {@link #copolymerConnectivity} (Todo.)
     */
    public List<CodeableConcept> getCopolymerConnectivity() { 
      if (this.copolymerConnectivity == null)
        this.copolymerConnectivity = new ArrayList<CodeableConcept>();
      return this.copolymerConnectivity;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public SubstancePolymer setCopolymerConnectivity(List<CodeableConcept> theCopolymerConnectivity) { 
      this.copolymerConnectivity = theCopolymerConnectivity;
      return this;
    }

    public boolean hasCopolymerConnectivity() { 
      if (this.copolymerConnectivity == null)
        return false;
      for (CodeableConcept item : this.copolymerConnectivity)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addCopolymerConnectivity() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.copolymerConnectivity == null)
        this.copolymerConnectivity = new ArrayList<CodeableConcept>();
      this.copolymerConnectivity.add(t);
      return t;
    }

    public SubstancePolymer addCopolymerConnectivity(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.copolymerConnectivity == null)
        this.copolymerConnectivity = new ArrayList<CodeableConcept>();
      this.copolymerConnectivity.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #copolymerConnectivity}, creating it if it does not already exist
     */
    public CodeableConcept getCopolymerConnectivityFirstRep() { 
      if (getCopolymerConnectivity().isEmpty()) {
        addCopolymerConnectivity();
      }
      return getCopolymerConnectivity().get(0);
    }

    /**
     * @return {@link #modification} (Todo.)
     */
    public List<StringType> getModification() { 
      if (this.modification == null)
        this.modification = new ArrayList<StringType>();
      return this.modification;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public SubstancePolymer setModification(List<StringType> theModification) { 
      this.modification = theModification;
      return this;
    }

    public boolean hasModification() { 
      if (this.modification == null)
        return false;
      for (StringType item : this.modification)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #modification} (Todo.)
     */
    public StringType addModificationElement() {//2 
      StringType t = new StringType();
      if (this.modification == null)
        this.modification = new ArrayList<StringType>();
      this.modification.add(t);
      return t;
    }

    /**
     * @param value {@link #modification} (Todo.)
     */
    public SubstancePolymer addModification(String value) { //1
      StringType t = new StringType();
      t.setValue(value);
      if (this.modification == null)
        this.modification = new ArrayList<StringType>();
      this.modification.add(t);
      return this;
    }

    /**
     * @param value {@link #modification} (Todo.)
     */
    public boolean hasModification(String value) { 
      if (this.modification == null)
        return false;
      for (StringType v : this.modification)
        if (v.getValue().equals(value)) // string
          return true;
      return false;
    }

    /**
     * @return {@link #monomerSet} (Todo.)
     */
    public List<SubstancePolymerMonomerSetComponent> getMonomerSet() { 
      if (this.monomerSet == null)
        this.monomerSet = new ArrayList<SubstancePolymerMonomerSetComponent>();
      return this.monomerSet;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public SubstancePolymer setMonomerSet(List<SubstancePolymerMonomerSetComponent> theMonomerSet) { 
      this.monomerSet = theMonomerSet;
      return this;
    }

    public boolean hasMonomerSet() { 
      if (this.monomerSet == null)
        return false;
      for (SubstancePolymerMonomerSetComponent item : this.monomerSet)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public SubstancePolymerMonomerSetComponent addMonomerSet() { //3
      SubstancePolymerMonomerSetComponent t = new SubstancePolymerMonomerSetComponent();
      if (this.monomerSet == null)
        this.monomerSet = new ArrayList<SubstancePolymerMonomerSetComponent>();
      this.monomerSet.add(t);
      return t;
    }

    public SubstancePolymer addMonomerSet(SubstancePolymerMonomerSetComponent t) { //3
      if (t == null)
        return this;
      if (this.monomerSet == null)
        this.monomerSet = new ArrayList<SubstancePolymerMonomerSetComponent>();
      this.monomerSet.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #monomerSet}, creating it if it does not already exist
     */
    public SubstancePolymerMonomerSetComponent getMonomerSetFirstRep() { 
      if (getMonomerSet().isEmpty()) {
        addMonomerSet();
      }
      return getMonomerSet().get(0);
    }

    /**
     * @return {@link #repeat} (Todo.)
     */
    public List<SubstancePolymerRepeatComponent> getRepeat() { 
      if (this.repeat == null)
        this.repeat = new ArrayList<SubstancePolymerRepeatComponent>();
      return this.repeat;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public SubstancePolymer setRepeat(List<SubstancePolymerRepeatComponent> theRepeat) { 
      this.repeat = theRepeat;
      return this;
    }

    public boolean hasRepeat() { 
      if (this.repeat == null)
        return false;
      for (SubstancePolymerRepeatComponent item : this.repeat)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public SubstancePolymerRepeatComponent addRepeat() { //3
      SubstancePolymerRepeatComponent t = new SubstancePolymerRepeatComponent();
      if (this.repeat == null)
        this.repeat = new ArrayList<SubstancePolymerRepeatComponent>();
      this.repeat.add(t);
      return t;
    }

    public SubstancePolymer addRepeat(SubstancePolymerRepeatComponent t) { //3
      if (t == null)
        return this;
      if (this.repeat == null)
        this.repeat = new ArrayList<SubstancePolymerRepeatComponent>();
      this.repeat.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #repeat}, creating it if it does not already exist
     */
    public SubstancePolymerRepeatComponent getRepeatFirstRep() { 
      if (getRepeat().isEmpty()) {
        addRepeat();
      }
      return getRepeat().get(0);
    }

      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("class", "CodeableConcept", "Todo.", 0, 1, class_));
        children.add(new Property("geometry", "CodeableConcept", "Todo.", 0, 1, geometry));
        children.add(new Property("copolymerConnectivity", "CodeableConcept", "Todo.", 0, java.lang.Integer.MAX_VALUE, copolymerConnectivity));
        children.add(new Property("modification", "string", "Todo.", 0, java.lang.Integer.MAX_VALUE, modification));
        children.add(new Property("monomerSet", "", "Todo.", 0, java.lang.Integer.MAX_VALUE, monomerSet));
        children.add(new Property("repeat", "", "Todo.", 0, java.lang.Integer.MAX_VALUE, repeat));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case 94742904: /*class*/  return new Property("class", "CodeableConcept", "Todo.", 0, 1, class_);
        case 1846020210: /*geometry*/  return new Property("geometry", "CodeableConcept", "Todo.", 0, 1, geometry);
        case 997107577: /*copolymerConnectivity*/  return new Property("copolymerConnectivity", "CodeableConcept", "Todo.", 0, java.lang.Integer.MAX_VALUE, copolymerConnectivity);
        case -684600932: /*modification*/  return new Property("modification", "string", "Todo.", 0, java.lang.Integer.MAX_VALUE, modification);
        case -1622483765: /*monomerSet*/  return new Property("monomerSet", "", "Todo.", 0, java.lang.Integer.MAX_VALUE, monomerSet);
        case -934531685: /*repeat*/  return new Property("repeat", "", "Todo.", 0, java.lang.Integer.MAX_VALUE, repeat);
        default: return super.getNamedProperty(_hash, _name, _checkValid);
        }

      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 94742904: /*class*/ return this.class_ == null ? new Base[0] : new Base[] {this.class_}; // CodeableConcept
        case 1846020210: /*geometry*/ return this.geometry == null ? new Base[0] : new Base[] {this.geometry}; // CodeableConcept
        case 997107577: /*copolymerConnectivity*/ return this.copolymerConnectivity == null ? new Base[0] : this.copolymerConnectivity.toArray(new Base[this.copolymerConnectivity.size()]); // CodeableConcept
        case -684600932: /*modification*/ return this.modification == null ? new Base[0] : this.modification.toArray(new Base[this.modification.size()]); // StringType
        case -1622483765: /*monomerSet*/ return this.monomerSet == null ? new Base[0] : this.monomerSet.toArray(new Base[this.monomerSet.size()]); // SubstancePolymerMonomerSetComponent
        case -934531685: /*repeat*/ return this.repeat == null ? new Base[0] : this.repeat.toArray(new Base[this.repeat.size()]); // SubstancePolymerRepeatComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 94742904: // class
          this.class_ = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 1846020210: // geometry
          this.geometry = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 997107577: // copolymerConnectivity
          this.getCopolymerConnectivity().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -684600932: // modification
          this.getModification().add(castToString(value)); // StringType
          return value;
        case -1622483765: // monomerSet
          this.getMonomerSet().add((SubstancePolymerMonomerSetComponent) value); // SubstancePolymerMonomerSetComponent
          return value;
        case -934531685: // repeat
          this.getRepeat().add((SubstancePolymerRepeatComponent) value); // SubstancePolymerRepeatComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("class")) {
          this.class_ = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("geometry")) {
          this.geometry = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("copolymerConnectivity")) {
          this.getCopolymerConnectivity().add(castToCodeableConcept(value));
        } else if (name.equals("modification")) {
          this.getModification().add(castToString(value));
        } else if (name.equals("monomerSet")) {
          this.getMonomerSet().add((SubstancePolymerMonomerSetComponent) value);
        } else if (name.equals("repeat")) {
          this.getRepeat().add((SubstancePolymerRepeatComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 94742904:  return getClass_(); 
        case 1846020210:  return getGeometry(); 
        case 997107577:  return addCopolymerConnectivity(); 
        case -684600932:  return addModificationElement();
        case -1622483765:  return addMonomerSet(); 
        case -934531685:  return addRepeat(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 94742904: /*class*/ return new String[] {"CodeableConcept"};
        case 1846020210: /*geometry*/ return new String[] {"CodeableConcept"};
        case 997107577: /*copolymerConnectivity*/ return new String[] {"CodeableConcept"};
        case -684600932: /*modification*/ return new String[] {"string"};
        case -1622483765: /*monomerSet*/ return new String[] {};
        case -934531685: /*repeat*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("class")) {
          this.class_ = new CodeableConcept();
          return this.class_;
        }
        else if (name.equals("geometry")) {
          this.geometry = new CodeableConcept();
          return this.geometry;
        }
        else if (name.equals("copolymerConnectivity")) {
          return addCopolymerConnectivity();
        }
        else if (name.equals("modification")) {
          throw new FHIRException("Cannot call addChild on a primitive type SubstancePolymer.modification");
        }
        else if (name.equals("monomerSet")) {
          return addMonomerSet();
        }
        else if (name.equals("repeat")) {
          return addRepeat();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "SubstancePolymer";

  }

      public SubstancePolymer copy() {
        SubstancePolymer dst = new SubstancePolymer();
        copyValues(dst);
        dst.class_ = class_ == null ? null : class_.copy();
        dst.geometry = geometry == null ? null : geometry.copy();
        if (copolymerConnectivity != null) {
          dst.copolymerConnectivity = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : copolymerConnectivity)
            dst.copolymerConnectivity.add(i.copy());
        };
        if (modification != null) {
          dst.modification = new ArrayList<StringType>();
          for (StringType i : modification)
            dst.modification.add(i.copy());
        };
        if (monomerSet != null) {
          dst.monomerSet = new ArrayList<SubstancePolymerMonomerSetComponent>();
          for (SubstancePolymerMonomerSetComponent i : monomerSet)
            dst.monomerSet.add(i.copy());
        };
        if (repeat != null) {
          dst.repeat = new ArrayList<SubstancePolymerRepeatComponent>();
          for (SubstancePolymerRepeatComponent i : repeat)
            dst.repeat.add(i.copy());
        };
        return dst;
      }

      protected SubstancePolymer typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof SubstancePolymer))
          return false;
        SubstancePolymer o = (SubstancePolymer) other_;
        return compareDeep(class_, o.class_, true) && compareDeep(geometry, o.geometry, true) && compareDeep(copolymerConnectivity, o.copolymerConnectivity, true)
           && compareDeep(modification, o.modification, true) && compareDeep(monomerSet, o.monomerSet, true)
           && compareDeep(repeat, o.repeat, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof SubstancePolymer))
          return false;
        SubstancePolymer o = (SubstancePolymer) other_;
        return compareValues(modification, o.modification, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(class_, geometry, copolymerConnectivity
          , modification, monomerSet, repeat);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.SubstancePolymer;
   }


}

