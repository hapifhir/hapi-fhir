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
 * An ingredient of a manufactured item or pharmaceutical product.
 */
@ResourceDef(name="MedicinalProductIngredient", profile="http://hl7.org/fhir/Profile/MedicinalProductIngredient")
public class MedicinalProductIngredient extends DomainResource {

    @Block()
    public static class MedicinalProductIngredientSpecifiedSubstanceComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The specified substance.
         */
        @Child(name = "code", type = {CodeableConcept.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The specified substance", formalDefinition="The specified substance." )
        protected CodeableConcept code;

        /**
         * The group of specified substance, e.g. group 1 to 4.
         */
        @Child(name = "group", type = {CodeableConcept.class}, order=2, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The group of specified substance, e.g. group 1 to 4", formalDefinition="The group of specified substance, e.g. group 1 to 4." )
        protected CodeableConcept group;

        /**
         * Confidentiality level of the specified substance as the ingredient.
         */
        @Child(name = "confidentiality", type = {CodeableConcept.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Confidentiality level of the specified substance as the ingredient", formalDefinition="Confidentiality level of the specified substance as the ingredient." )
        protected CodeableConcept confidentiality;

        /**
         * Quantity of the substance or specified substance present in the manufactured item or pharmaceutical product.
         */
        @Child(name = "strength", type = {}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Quantity of the substance or specified substance present in the manufactured item or pharmaceutical product", formalDefinition="Quantity of the substance or specified substance present in the manufactured item or pharmaceutical product." )
        protected List<MedicinalProductIngredientSpecifiedSubstanceStrengthComponent> strength;

        private static final long serialVersionUID = -272590200L;

    /**
     * Constructor
     */
      public MedicinalProductIngredientSpecifiedSubstanceComponent() {
        super();
      }

    /**
     * Constructor
     */
      public MedicinalProductIngredientSpecifiedSubstanceComponent(CodeableConcept code, CodeableConcept group) {
        super();
        this.code = code;
        this.group = group;
      }

        /**
         * @return {@link #code} (The specified substance.)
         */
        public CodeableConcept getCode() { 
          if (this.code == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicinalProductIngredientSpecifiedSubstanceComponent.code");
            else if (Configuration.doAutoCreate())
              this.code = new CodeableConcept(); // cc
          return this.code;
        }

        public boolean hasCode() { 
          return this.code != null && !this.code.isEmpty();
        }

        /**
         * @param value {@link #code} (The specified substance.)
         */
        public MedicinalProductIngredientSpecifiedSubstanceComponent setCode(CodeableConcept value) { 
          this.code = value;
          return this;
        }

        /**
         * @return {@link #group} (The group of specified substance, e.g. group 1 to 4.)
         */
        public CodeableConcept getGroup() { 
          if (this.group == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicinalProductIngredientSpecifiedSubstanceComponent.group");
            else if (Configuration.doAutoCreate())
              this.group = new CodeableConcept(); // cc
          return this.group;
        }

        public boolean hasGroup() { 
          return this.group != null && !this.group.isEmpty();
        }

        /**
         * @param value {@link #group} (The group of specified substance, e.g. group 1 to 4.)
         */
        public MedicinalProductIngredientSpecifiedSubstanceComponent setGroup(CodeableConcept value) { 
          this.group = value;
          return this;
        }

        /**
         * @return {@link #confidentiality} (Confidentiality level of the specified substance as the ingredient.)
         */
        public CodeableConcept getConfidentiality() { 
          if (this.confidentiality == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicinalProductIngredientSpecifiedSubstanceComponent.confidentiality");
            else if (Configuration.doAutoCreate())
              this.confidentiality = new CodeableConcept(); // cc
          return this.confidentiality;
        }

        public boolean hasConfidentiality() { 
          return this.confidentiality != null && !this.confidentiality.isEmpty();
        }

        /**
         * @param value {@link #confidentiality} (Confidentiality level of the specified substance as the ingredient.)
         */
        public MedicinalProductIngredientSpecifiedSubstanceComponent setConfidentiality(CodeableConcept value) { 
          this.confidentiality = value;
          return this;
        }

        /**
         * @return {@link #strength} (Quantity of the substance or specified substance present in the manufactured item or pharmaceutical product.)
         */
        public List<MedicinalProductIngredientSpecifiedSubstanceStrengthComponent> getStrength() { 
          if (this.strength == null)
            this.strength = new ArrayList<MedicinalProductIngredientSpecifiedSubstanceStrengthComponent>();
          return this.strength;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public MedicinalProductIngredientSpecifiedSubstanceComponent setStrength(List<MedicinalProductIngredientSpecifiedSubstanceStrengthComponent> theStrength) { 
          this.strength = theStrength;
          return this;
        }

        public boolean hasStrength() { 
          if (this.strength == null)
            return false;
          for (MedicinalProductIngredientSpecifiedSubstanceStrengthComponent item : this.strength)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public MedicinalProductIngredientSpecifiedSubstanceStrengthComponent addStrength() { //3
          MedicinalProductIngredientSpecifiedSubstanceStrengthComponent t = new MedicinalProductIngredientSpecifiedSubstanceStrengthComponent();
          if (this.strength == null)
            this.strength = new ArrayList<MedicinalProductIngredientSpecifiedSubstanceStrengthComponent>();
          this.strength.add(t);
          return t;
        }

        public MedicinalProductIngredientSpecifiedSubstanceComponent addStrength(MedicinalProductIngredientSpecifiedSubstanceStrengthComponent t) { //3
          if (t == null)
            return this;
          if (this.strength == null)
            this.strength = new ArrayList<MedicinalProductIngredientSpecifiedSubstanceStrengthComponent>();
          this.strength.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #strength}, creating it if it does not already exist
         */
        public MedicinalProductIngredientSpecifiedSubstanceStrengthComponent getStrengthFirstRep() { 
          if (getStrength().isEmpty()) {
            addStrength();
          }
          return getStrength().get(0);
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("code", "CodeableConcept", "The specified substance.", 0, 1, code));
          children.add(new Property("group", "CodeableConcept", "The group of specified substance, e.g. group 1 to 4.", 0, 1, group));
          children.add(new Property("confidentiality", "CodeableConcept", "Confidentiality level of the specified substance as the ingredient.", 0, 1, confidentiality));
          children.add(new Property("strength", "", "Quantity of the substance or specified substance present in the manufactured item or pharmaceutical product.", 0, java.lang.Integer.MAX_VALUE, strength));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3059181: /*code*/  return new Property("code", "CodeableConcept", "The specified substance.", 0, 1, code);
          case 98629247: /*group*/  return new Property("group", "CodeableConcept", "The group of specified substance, e.g. group 1 to 4.", 0, 1, group);
          case -1923018202: /*confidentiality*/  return new Property("confidentiality", "CodeableConcept", "Confidentiality level of the specified substance as the ingredient.", 0, 1, confidentiality);
          case 1791316033: /*strength*/  return new Property("strength", "", "Quantity of the substance or specified substance present in the manufactured item or pharmaceutical product.", 0, java.lang.Integer.MAX_VALUE, strength);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3059181: /*code*/ return this.code == null ? new Base[0] : new Base[] {this.code}; // CodeableConcept
        case 98629247: /*group*/ return this.group == null ? new Base[0] : new Base[] {this.group}; // CodeableConcept
        case -1923018202: /*confidentiality*/ return this.confidentiality == null ? new Base[0] : new Base[] {this.confidentiality}; // CodeableConcept
        case 1791316033: /*strength*/ return this.strength == null ? new Base[0] : this.strength.toArray(new Base[this.strength.size()]); // MedicinalProductIngredientSpecifiedSubstanceStrengthComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3059181: // code
          this.code = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 98629247: // group
          this.group = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1923018202: // confidentiality
          this.confidentiality = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 1791316033: // strength
          this.getStrength().add((MedicinalProductIngredientSpecifiedSubstanceStrengthComponent) value); // MedicinalProductIngredientSpecifiedSubstanceStrengthComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("code")) {
          this.code = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("group")) {
          this.group = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("confidentiality")) {
          this.confidentiality = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("strength")) {
          this.getStrength().add((MedicinalProductIngredientSpecifiedSubstanceStrengthComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3059181:  return getCode(); 
        case 98629247:  return getGroup(); 
        case -1923018202:  return getConfidentiality(); 
        case 1791316033:  return addStrength(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3059181: /*code*/ return new String[] {"CodeableConcept"};
        case 98629247: /*group*/ return new String[] {"CodeableConcept"};
        case -1923018202: /*confidentiality*/ return new String[] {"CodeableConcept"};
        case 1791316033: /*strength*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("code")) {
          this.code = new CodeableConcept();
          return this.code;
        }
        else if (name.equals("group")) {
          this.group = new CodeableConcept();
          return this.group;
        }
        else if (name.equals("confidentiality")) {
          this.confidentiality = new CodeableConcept();
          return this.confidentiality;
        }
        else if (name.equals("strength")) {
          return addStrength();
        }
        else
          return super.addChild(name);
      }

      public MedicinalProductIngredientSpecifiedSubstanceComponent copy() {
        MedicinalProductIngredientSpecifiedSubstanceComponent dst = new MedicinalProductIngredientSpecifiedSubstanceComponent();
        copyValues(dst);
        dst.code = code == null ? null : code.copy();
        dst.group = group == null ? null : group.copy();
        dst.confidentiality = confidentiality == null ? null : confidentiality.copy();
        if (strength != null) {
          dst.strength = new ArrayList<MedicinalProductIngredientSpecifiedSubstanceStrengthComponent>();
          for (MedicinalProductIngredientSpecifiedSubstanceStrengthComponent i : strength)
            dst.strength.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof MedicinalProductIngredientSpecifiedSubstanceComponent))
          return false;
        MedicinalProductIngredientSpecifiedSubstanceComponent o = (MedicinalProductIngredientSpecifiedSubstanceComponent) other_;
        return compareDeep(code, o.code, true) && compareDeep(group, o.group, true) && compareDeep(confidentiality, o.confidentiality, true)
           && compareDeep(strength, o.strength, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof MedicinalProductIngredientSpecifiedSubstanceComponent))
          return false;
        MedicinalProductIngredientSpecifiedSubstanceComponent o = (MedicinalProductIngredientSpecifiedSubstanceComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(code, group, confidentiality
          , strength);
      }

  public String fhirType() {
    return "MedicinalProductIngredient.specifiedSubstance";

  }

  }

    @Block()
    public static class MedicinalProductIngredientSpecifiedSubstanceStrengthComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The quantity of substance in the unit of presentation, or in the volume (or mass) of the single pharmaceutical product or manufactured item.
         */
        @Child(name = "presentation", type = {Ratio.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The quantity of substance in the unit of presentation, or in the volume (or mass) of the single pharmaceutical product or manufactured item", formalDefinition="The quantity of substance in the unit of presentation, or in the volume (or mass) of the single pharmaceutical product or manufactured item." )
        protected Ratio presentation;

        /**
         * A lower limit for the quantity of substance in the unit of presentation. For use when there is a range of strengths, this is the lower limit, with the presentation attribute becoming the upper limit.
         */
        @Child(name = "presentationLowLimit", type = {Ratio.class}, order=2, min=0, max=1, modifier=true, summary=true)
        @Description(shortDefinition="A lower limit for the quantity of substance in the unit of presentation. For use when there is a range of strengths, this is the lower limit, with the presentation attribute becoming the upper limit", formalDefinition="A lower limit for the quantity of substance in the unit of presentation. For use when there is a range of strengths, this is the lower limit, with the presentation attribute becoming the upper limit." )
        protected Ratio presentationLowLimit;

        /**
         * The strength per unitary volume (or mass).
         */
        @Child(name = "concentration", type = {Ratio.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The strength per unitary volume (or mass)", formalDefinition="The strength per unitary volume (or mass)." )
        protected Ratio concentration;

        /**
         * A lower limit for the strength per unitary volume (or mass), for when there is a range. The concentration attribute then becomes the upper limit.
         */
        @Child(name = "concentrationLowLimit", type = {Ratio.class}, order=4, min=0, max=1, modifier=true, summary=true)
        @Description(shortDefinition="A lower limit for the strength per unitary volume (or mass), for when there is a range. The concentration attribute then becomes the upper limit", formalDefinition="A lower limit for the strength per unitary volume (or mass), for when there is a range. The concentration attribute then becomes the upper limit." )
        protected Ratio concentrationLowLimit;

        /**
         * For when strength is measured at a particular point or distance.
         */
        @Child(name = "measurementPoint", type = {StringType.class}, order=5, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="For when strength is measured at a particular point or distance", formalDefinition="For when strength is measured at a particular point or distance." )
        protected StringType measurementPoint;

        /**
         * The country or countries for which the strength range applies.
         */
        @Child(name = "country", type = {CodeableConcept.class}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="The country or countries for which the strength range applies", formalDefinition="The country or countries for which the strength range applies." )
        protected List<CodeableConcept> country;

        /**
         * Strength expressed in terms of a reference substance.
         */
        @Child(name = "referenceStrength", type = {}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Strength expressed in terms of a reference substance", formalDefinition="Strength expressed in terms of a reference substance." )
        protected List<MedicinalProductIngredientSpecifiedSubstanceStrengthReferenceStrengthComponent> referenceStrength;

        private static final long serialVersionUID = 1981438822L;

    /**
     * Constructor
     */
      public MedicinalProductIngredientSpecifiedSubstanceStrengthComponent() {
        super();
      }

    /**
     * Constructor
     */
      public MedicinalProductIngredientSpecifiedSubstanceStrengthComponent(Ratio presentation) {
        super();
        this.presentation = presentation;
      }

        /**
         * @return {@link #presentation} (The quantity of substance in the unit of presentation, or in the volume (or mass) of the single pharmaceutical product or manufactured item.)
         */
        public Ratio getPresentation() { 
          if (this.presentation == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicinalProductIngredientSpecifiedSubstanceStrengthComponent.presentation");
            else if (Configuration.doAutoCreate())
              this.presentation = new Ratio(); // cc
          return this.presentation;
        }

        public boolean hasPresentation() { 
          return this.presentation != null && !this.presentation.isEmpty();
        }

        /**
         * @param value {@link #presentation} (The quantity of substance in the unit of presentation, or in the volume (or mass) of the single pharmaceutical product or manufactured item.)
         */
        public MedicinalProductIngredientSpecifiedSubstanceStrengthComponent setPresentation(Ratio value) { 
          this.presentation = value;
          return this;
        }

        /**
         * @return {@link #presentationLowLimit} (A lower limit for the quantity of substance in the unit of presentation. For use when there is a range of strengths, this is the lower limit, with the presentation attribute becoming the upper limit.)
         */
        public Ratio getPresentationLowLimit() { 
          if (this.presentationLowLimit == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicinalProductIngredientSpecifiedSubstanceStrengthComponent.presentationLowLimit");
            else if (Configuration.doAutoCreate())
              this.presentationLowLimit = new Ratio(); // cc
          return this.presentationLowLimit;
        }

        public boolean hasPresentationLowLimit() { 
          return this.presentationLowLimit != null && !this.presentationLowLimit.isEmpty();
        }

        /**
         * @param value {@link #presentationLowLimit} (A lower limit for the quantity of substance in the unit of presentation. For use when there is a range of strengths, this is the lower limit, with the presentation attribute becoming the upper limit.)
         */
        public MedicinalProductIngredientSpecifiedSubstanceStrengthComponent setPresentationLowLimit(Ratio value) { 
          this.presentationLowLimit = value;
          return this;
        }

        /**
         * @return {@link #concentration} (The strength per unitary volume (or mass).)
         */
        public Ratio getConcentration() { 
          if (this.concentration == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicinalProductIngredientSpecifiedSubstanceStrengthComponent.concentration");
            else if (Configuration.doAutoCreate())
              this.concentration = new Ratio(); // cc
          return this.concentration;
        }

        public boolean hasConcentration() { 
          return this.concentration != null && !this.concentration.isEmpty();
        }

        /**
         * @param value {@link #concentration} (The strength per unitary volume (or mass).)
         */
        public MedicinalProductIngredientSpecifiedSubstanceStrengthComponent setConcentration(Ratio value) { 
          this.concentration = value;
          return this;
        }

        /**
         * @return {@link #concentrationLowLimit} (A lower limit for the strength per unitary volume (or mass), for when there is a range. The concentration attribute then becomes the upper limit.)
         */
        public Ratio getConcentrationLowLimit() { 
          if (this.concentrationLowLimit == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicinalProductIngredientSpecifiedSubstanceStrengthComponent.concentrationLowLimit");
            else if (Configuration.doAutoCreate())
              this.concentrationLowLimit = new Ratio(); // cc
          return this.concentrationLowLimit;
        }

        public boolean hasConcentrationLowLimit() { 
          return this.concentrationLowLimit != null && !this.concentrationLowLimit.isEmpty();
        }

        /**
         * @param value {@link #concentrationLowLimit} (A lower limit for the strength per unitary volume (or mass), for when there is a range. The concentration attribute then becomes the upper limit.)
         */
        public MedicinalProductIngredientSpecifiedSubstanceStrengthComponent setConcentrationLowLimit(Ratio value) { 
          this.concentrationLowLimit = value;
          return this;
        }

        /**
         * @return {@link #measurementPoint} (For when strength is measured at a particular point or distance.). This is the underlying object with id, value and extensions. The accessor "getMeasurementPoint" gives direct access to the value
         */
        public StringType getMeasurementPointElement() { 
          if (this.measurementPoint == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicinalProductIngredientSpecifiedSubstanceStrengthComponent.measurementPoint");
            else if (Configuration.doAutoCreate())
              this.measurementPoint = new StringType(); // bb
          return this.measurementPoint;
        }

        public boolean hasMeasurementPointElement() { 
          return this.measurementPoint != null && !this.measurementPoint.isEmpty();
        }

        public boolean hasMeasurementPoint() { 
          return this.measurementPoint != null && !this.measurementPoint.isEmpty();
        }

        /**
         * @param value {@link #measurementPoint} (For when strength is measured at a particular point or distance.). This is the underlying object with id, value and extensions. The accessor "getMeasurementPoint" gives direct access to the value
         */
        public MedicinalProductIngredientSpecifiedSubstanceStrengthComponent setMeasurementPointElement(StringType value) { 
          this.measurementPoint = value;
          return this;
        }

        /**
         * @return For when strength is measured at a particular point or distance.
         */
        public String getMeasurementPoint() { 
          return this.measurementPoint == null ? null : this.measurementPoint.getValue();
        }

        /**
         * @param value For when strength is measured at a particular point or distance.
         */
        public MedicinalProductIngredientSpecifiedSubstanceStrengthComponent setMeasurementPoint(String value) { 
          if (Utilities.noString(value))
            this.measurementPoint = null;
          else {
            if (this.measurementPoint == null)
              this.measurementPoint = new StringType();
            this.measurementPoint.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #country} (The country or countries for which the strength range applies.)
         */
        public List<CodeableConcept> getCountry() { 
          if (this.country == null)
            this.country = new ArrayList<CodeableConcept>();
          return this.country;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public MedicinalProductIngredientSpecifiedSubstanceStrengthComponent setCountry(List<CodeableConcept> theCountry) { 
          this.country = theCountry;
          return this;
        }

        public boolean hasCountry() { 
          if (this.country == null)
            return false;
          for (CodeableConcept item : this.country)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public CodeableConcept addCountry() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.country == null)
            this.country = new ArrayList<CodeableConcept>();
          this.country.add(t);
          return t;
        }

        public MedicinalProductIngredientSpecifiedSubstanceStrengthComponent addCountry(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.country == null)
            this.country = new ArrayList<CodeableConcept>();
          this.country.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #country}, creating it if it does not already exist
         */
        public CodeableConcept getCountryFirstRep() { 
          if (getCountry().isEmpty()) {
            addCountry();
          }
          return getCountry().get(0);
        }

        /**
         * @return {@link #referenceStrength} (Strength expressed in terms of a reference substance.)
         */
        public List<MedicinalProductIngredientSpecifiedSubstanceStrengthReferenceStrengthComponent> getReferenceStrength() { 
          if (this.referenceStrength == null)
            this.referenceStrength = new ArrayList<MedicinalProductIngredientSpecifiedSubstanceStrengthReferenceStrengthComponent>();
          return this.referenceStrength;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public MedicinalProductIngredientSpecifiedSubstanceStrengthComponent setReferenceStrength(List<MedicinalProductIngredientSpecifiedSubstanceStrengthReferenceStrengthComponent> theReferenceStrength) { 
          this.referenceStrength = theReferenceStrength;
          return this;
        }

        public boolean hasReferenceStrength() { 
          if (this.referenceStrength == null)
            return false;
          for (MedicinalProductIngredientSpecifiedSubstanceStrengthReferenceStrengthComponent item : this.referenceStrength)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public MedicinalProductIngredientSpecifiedSubstanceStrengthReferenceStrengthComponent addReferenceStrength() { //3
          MedicinalProductIngredientSpecifiedSubstanceStrengthReferenceStrengthComponent t = new MedicinalProductIngredientSpecifiedSubstanceStrengthReferenceStrengthComponent();
          if (this.referenceStrength == null)
            this.referenceStrength = new ArrayList<MedicinalProductIngredientSpecifiedSubstanceStrengthReferenceStrengthComponent>();
          this.referenceStrength.add(t);
          return t;
        }

        public MedicinalProductIngredientSpecifiedSubstanceStrengthComponent addReferenceStrength(MedicinalProductIngredientSpecifiedSubstanceStrengthReferenceStrengthComponent t) { //3
          if (t == null)
            return this;
          if (this.referenceStrength == null)
            this.referenceStrength = new ArrayList<MedicinalProductIngredientSpecifiedSubstanceStrengthReferenceStrengthComponent>();
          this.referenceStrength.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #referenceStrength}, creating it if it does not already exist
         */
        public MedicinalProductIngredientSpecifiedSubstanceStrengthReferenceStrengthComponent getReferenceStrengthFirstRep() { 
          if (getReferenceStrength().isEmpty()) {
            addReferenceStrength();
          }
          return getReferenceStrength().get(0);
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("presentation", "Ratio", "The quantity of substance in the unit of presentation, or in the volume (or mass) of the single pharmaceutical product or manufactured item.", 0, 1, presentation));
          children.add(new Property("presentationLowLimit", "Ratio", "A lower limit for the quantity of substance in the unit of presentation. For use when there is a range of strengths, this is the lower limit, with the presentation attribute becoming the upper limit.", 0, 1, presentationLowLimit));
          children.add(new Property("concentration", "Ratio", "The strength per unitary volume (or mass).", 0, 1, concentration));
          children.add(new Property("concentrationLowLimit", "Ratio", "A lower limit for the strength per unitary volume (or mass), for when there is a range. The concentration attribute then becomes the upper limit.", 0, 1, concentrationLowLimit));
          children.add(new Property("measurementPoint", "string", "For when strength is measured at a particular point or distance.", 0, 1, measurementPoint));
          children.add(new Property("country", "CodeableConcept", "The country or countries for which the strength range applies.", 0, java.lang.Integer.MAX_VALUE, country));
          children.add(new Property("referenceStrength", "", "Strength expressed in terms of a reference substance.", 0, java.lang.Integer.MAX_VALUE, referenceStrength));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 696975130: /*presentation*/  return new Property("presentation", "Ratio", "The quantity of substance in the unit of presentation, or in the volume (or mass) of the single pharmaceutical product or manufactured item.", 0, 1, presentation);
          case -819112447: /*presentationLowLimit*/  return new Property("presentationLowLimit", "Ratio", "A lower limit for the quantity of substance in the unit of presentation. For use when there is a range of strengths, this is the lower limit, with the presentation attribute becoming the upper limit.", 0, 1, presentationLowLimit);
          case -410557331: /*concentration*/  return new Property("concentration", "Ratio", "The strength per unitary volume (or mass).", 0, 1, concentration);
          case -484132780: /*concentrationLowLimit*/  return new Property("concentrationLowLimit", "Ratio", "A lower limit for the strength per unitary volume (or mass), for when there is a range. The concentration attribute then becomes the upper limit.", 0, 1, concentrationLowLimit);
          case 235437876: /*measurementPoint*/  return new Property("measurementPoint", "string", "For when strength is measured at a particular point or distance.", 0, 1, measurementPoint);
          case 957831062: /*country*/  return new Property("country", "CodeableConcept", "The country or countries for which the strength range applies.", 0, java.lang.Integer.MAX_VALUE, country);
          case 1943566508: /*referenceStrength*/  return new Property("referenceStrength", "", "Strength expressed in terms of a reference substance.", 0, java.lang.Integer.MAX_VALUE, referenceStrength);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 696975130: /*presentation*/ return this.presentation == null ? new Base[0] : new Base[] {this.presentation}; // Ratio
        case -819112447: /*presentationLowLimit*/ return this.presentationLowLimit == null ? new Base[0] : new Base[] {this.presentationLowLimit}; // Ratio
        case -410557331: /*concentration*/ return this.concentration == null ? new Base[0] : new Base[] {this.concentration}; // Ratio
        case -484132780: /*concentrationLowLimit*/ return this.concentrationLowLimit == null ? new Base[0] : new Base[] {this.concentrationLowLimit}; // Ratio
        case 235437876: /*measurementPoint*/ return this.measurementPoint == null ? new Base[0] : new Base[] {this.measurementPoint}; // StringType
        case 957831062: /*country*/ return this.country == null ? new Base[0] : this.country.toArray(new Base[this.country.size()]); // CodeableConcept
        case 1943566508: /*referenceStrength*/ return this.referenceStrength == null ? new Base[0] : this.referenceStrength.toArray(new Base[this.referenceStrength.size()]); // MedicinalProductIngredientSpecifiedSubstanceStrengthReferenceStrengthComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 696975130: // presentation
          this.presentation = castToRatio(value); // Ratio
          return value;
        case -819112447: // presentationLowLimit
          this.presentationLowLimit = castToRatio(value); // Ratio
          return value;
        case -410557331: // concentration
          this.concentration = castToRatio(value); // Ratio
          return value;
        case -484132780: // concentrationLowLimit
          this.concentrationLowLimit = castToRatio(value); // Ratio
          return value;
        case 235437876: // measurementPoint
          this.measurementPoint = castToString(value); // StringType
          return value;
        case 957831062: // country
          this.getCountry().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 1943566508: // referenceStrength
          this.getReferenceStrength().add((MedicinalProductIngredientSpecifiedSubstanceStrengthReferenceStrengthComponent) value); // MedicinalProductIngredientSpecifiedSubstanceStrengthReferenceStrengthComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("presentation")) {
          this.presentation = castToRatio(value); // Ratio
        } else if (name.equals("presentationLowLimit")) {
          this.presentationLowLimit = castToRatio(value); // Ratio
        } else if (name.equals("concentration")) {
          this.concentration = castToRatio(value); // Ratio
        } else if (name.equals("concentrationLowLimit")) {
          this.concentrationLowLimit = castToRatio(value); // Ratio
        } else if (name.equals("measurementPoint")) {
          this.measurementPoint = castToString(value); // StringType
        } else if (name.equals("country")) {
          this.getCountry().add(castToCodeableConcept(value));
        } else if (name.equals("referenceStrength")) {
          this.getReferenceStrength().add((MedicinalProductIngredientSpecifiedSubstanceStrengthReferenceStrengthComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 696975130:  return getPresentation(); 
        case -819112447:  return getPresentationLowLimit(); 
        case -410557331:  return getConcentration(); 
        case -484132780:  return getConcentrationLowLimit(); 
        case 235437876:  return getMeasurementPointElement();
        case 957831062:  return addCountry(); 
        case 1943566508:  return addReferenceStrength(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 696975130: /*presentation*/ return new String[] {"Ratio"};
        case -819112447: /*presentationLowLimit*/ return new String[] {"Ratio"};
        case -410557331: /*concentration*/ return new String[] {"Ratio"};
        case -484132780: /*concentrationLowLimit*/ return new String[] {"Ratio"};
        case 235437876: /*measurementPoint*/ return new String[] {"string"};
        case 957831062: /*country*/ return new String[] {"CodeableConcept"};
        case 1943566508: /*referenceStrength*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("presentation")) {
          this.presentation = new Ratio();
          return this.presentation;
        }
        else if (name.equals("presentationLowLimit")) {
          this.presentationLowLimit = new Ratio();
          return this.presentationLowLimit;
        }
        else if (name.equals("concentration")) {
          this.concentration = new Ratio();
          return this.concentration;
        }
        else if (name.equals("concentrationLowLimit")) {
          this.concentrationLowLimit = new Ratio();
          return this.concentrationLowLimit;
        }
        else if (name.equals("measurementPoint")) {
          throw new FHIRException("Cannot call addChild on a primitive type MedicinalProductIngredient.measurementPoint");
        }
        else if (name.equals("country")) {
          return addCountry();
        }
        else if (name.equals("referenceStrength")) {
          return addReferenceStrength();
        }
        else
          return super.addChild(name);
      }

      public MedicinalProductIngredientSpecifiedSubstanceStrengthComponent copy() {
        MedicinalProductIngredientSpecifiedSubstanceStrengthComponent dst = new MedicinalProductIngredientSpecifiedSubstanceStrengthComponent();
        copyValues(dst);
        dst.presentation = presentation == null ? null : presentation.copy();
        dst.presentationLowLimit = presentationLowLimit == null ? null : presentationLowLimit.copy();
        dst.concentration = concentration == null ? null : concentration.copy();
        dst.concentrationLowLimit = concentrationLowLimit == null ? null : concentrationLowLimit.copy();
        dst.measurementPoint = measurementPoint == null ? null : measurementPoint.copy();
        if (country != null) {
          dst.country = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : country)
            dst.country.add(i.copy());
        };
        if (referenceStrength != null) {
          dst.referenceStrength = new ArrayList<MedicinalProductIngredientSpecifiedSubstanceStrengthReferenceStrengthComponent>();
          for (MedicinalProductIngredientSpecifiedSubstanceStrengthReferenceStrengthComponent i : referenceStrength)
            dst.referenceStrength.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof MedicinalProductIngredientSpecifiedSubstanceStrengthComponent))
          return false;
        MedicinalProductIngredientSpecifiedSubstanceStrengthComponent o = (MedicinalProductIngredientSpecifiedSubstanceStrengthComponent) other_;
        return compareDeep(presentation, o.presentation, true) && compareDeep(presentationLowLimit, o.presentationLowLimit, true)
           && compareDeep(concentration, o.concentration, true) && compareDeep(concentrationLowLimit, o.concentrationLowLimit, true)
           && compareDeep(measurementPoint, o.measurementPoint, true) && compareDeep(country, o.country, true)
           && compareDeep(referenceStrength, o.referenceStrength, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof MedicinalProductIngredientSpecifiedSubstanceStrengthComponent))
          return false;
        MedicinalProductIngredientSpecifiedSubstanceStrengthComponent o = (MedicinalProductIngredientSpecifiedSubstanceStrengthComponent) other_;
        return compareValues(measurementPoint, o.measurementPoint, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(presentation, presentationLowLimit
          , concentration, concentrationLowLimit, measurementPoint, country, referenceStrength
          );
      }

  public String fhirType() {
    return "MedicinalProductIngredient.specifiedSubstance.strength";

  }

  }

    @Block()
    public static class MedicinalProductIngredientSpecifiedSubstanceStrengthReferenceStrengthComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Relevent refrerence substance.
         */
        @Child(name = "substance", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Relevent refrerence substance", formalDefinition="Relevent refrerence substance." )
        protected CodeableConcept substance;

        /**
         * Strength expressed in terms of a reference substance.
         */
        @Child(name = "strength", type = {Ratio.class}, order=2, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Strength expressed in terms of a reference substance", formalDefinition="Strength expressed in terms of a reference substance." )
        protected Ratio strength;

        /**
         * For when strength is measured at a particular point or distance.
         */
        @Child(name = "measurementPoint", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="For when strength is measured at a particular point or distance", formalDefinition="For when strength is measured at a particular point or distance." )
        protected StringType measurementPoint;

        /**
         * The country or countries for which the strength range applies.
         */
        @Child(name = "country", type = {CodeableConcept.class}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="The country or countries for which the strength range applies", formalDefinition="The country or countries for which the strength range applies." )
        protected List<CodeableConcept> country;

        private static final long serialVersionUID = 1255052145L;

    /**
     * Constructor
     */
      public MedicinalProductIngredientSpecifiedSubstanceStrengthReferenceStrengthComponent() {
        super();
      }

    /**
     * Constructor
     */
      public MedicinalProductIngredientSpecifiedSubstanceStrengthReferenceStrengthComponent(Ratio strength) {
        super();
        this.strength = strength;
      }

        /**
         * @return {@link #substance} (Relevent refrerence substance.)
         */
        public CodeableConcept getSubstance() { 
          if (this.substance == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicinalProductIngredientSpecifiedSubstanceStrengthReferenceStrengthComponent.substance");
            else if (Configuration.doAutoCreate())
              this.substance = new CodeableConcept(); // cc
          return this.substance;
        }

        public boolean hasSubstance() { 
          return this.substance != null && !this.substance.isEmpty();
        }

        /**
         * @param value {@link #substance} (Relevent refrerence substance.)
         */
        public MedicinalProductIngredientSpecifiedSubstanceStrengthReferenceStrengthComponent setSubstance(CodeableConcept value) { 
          this.substance = value;
          return this;
        }

        /**
         * @return {@link #strength} (Strength expressed in terms of a reference substance.)
         */
        public Ratio getStrength() { 
          if (this.strength == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicinalProductIngredientSpecifiedSubstanceStrengthReferenceStrengthComponent.strength");
            else if (Configuration.doAutoCreate())
              this.strength = new Ratio(); // cc
          return this.strength;
        }

        public boolean hasStrength() { 
          return this.strength != null && !this.strength.isEmpty();
        }

        /**
         * @param value {@link #strength} (Strength expressed in terms of a reference substance.)
         */
        public MedicinalProductIngredientSpecifiedSubstanceStrengthReferenceStrengthComponent setStrength(Ratio value) { 
          this.strength = value;
          return this;
        }

        /**
         * @return {@link #measurementPoint} (For when strength is measured at a particular point or distance.). This is the underlying object with id, value and extensions. The accessor "getMeasurementPoint" gives direct access to the value
         */
        public StringType getMeasurementPointElement() { 
          if (this.measurementPoint == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicinalProductIngredientSpecifiedSubstanceStrengthReferenceStrengthComponent.measurementPoint");
            else if (Configuration.doAutoCreate())
              this.measurementPoint = new StringType(); // bb
          return this.measurementPoint;
        }

        public boolean hasMeasurementPointElement() { 
          return this.measurementPoint != null && !this.measurementPoint.isEmpty();
        }

        public boolean hasMeasurementPoint() { 
          return this.measurementPoint != null && !this.measurementPoint.isEmpty();
        }

        /**
         * @param value {@link #measurementPoint} (For when strength is measured at a particular point or distance.). This is the underlying object with id, value and extensions. The accessor "getMeasurementPoint" gives direct access to the value
         */
        public MedicinalProductIngredientSpecifiedSubstanceStrengthReferenceStrengthComponent setMeasurementPointElement(StringType value) { 
          this.measurementPoint = value;
          return this;
        }

        /**
         * @return For when strength is measured at a particular point or distance.
         */
        public String getMeasurementPoint() { 
          return this.measurementPoint == null ? null : this.measurementPoint.getValue();
        }

        /**
         * @param value For when strength is measured at a particular point or distance.
         */
        public MedicinalProductIngredientSpecifiedSubstanceStrengthReferenceStrengthComponent setMeasurementPoint(String value) { 
          if (Utilities.noString(value))
            this.measurementPoint = null;
          else {
            if (this.measurementPoint == null)
              this.measurementPoint = new StringType();
            this.measurementPoint.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #country} (The country or countries for which the strength range applies.)
         */
        public List<CodeableConcept> getCountry() { 
          if (this.country == null)
            this.country = new ArrayList<CodeableConcept>();
          return this.country;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public MedicinalProductIngredientSpecifiedSubstanceStrengthReferenceStrengthComponent setCountry(List<CodeableConcept> theCountry) { 
          this.country = theCountry;
          return this;
        }

        public boolean hasCountry() { 
          if (this.country == null)
            return false;
          for (CodeableConcept item : this.country)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public CodeableConcept addCountry() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.country == null)
            this.country = new ArrayList<CodeableConcept>();
          this.country.add(t);
          return t;
        }

        public MedicinalProductIngredientSpecifiedSubstanceStrengthReferenceStrengthComponent addCountry(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.country == null)
            this.country = new ArrayList<CodeableConcept>();
          this.country.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #country}, creating it if it does not already exist
         */
        public CodeableConcept getCountryFirstRep() { 
          if (getCountry().isEmpty()) {
            addCountry();
          }
          return getCountry().get(0);
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("substance", "CodeableConcept", "Relevent refrerence substance.", 0, 1, substance));
          children.add(new Property("strength", "Ratio", "Strength expressed in terms of a reference substance.", 0, 1, strength));
          children.add(new Property("measurementPoint", "string", "For when strength is measured at a particular point or distance.", 0, 1, measurementPoint));
          children.add(new Property("country", "CodeableConcept", "The country or countries for which the strength range applies.", 0, java.lang.Integer.MAX_VALUE, country));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 530040176: /*substance*/  return new Property("substance", "CodeableConcept", "Relevent refrerence substance.", 0, 1, substance);
          case 1791316033: /*strength*/  return new Property("strength", "Ratio", "Strength expressed in terms of a reference substance.", 0, 1, strength);
          case 235437876: /*measurementPoint*/  return new Property("measurementPoint", "string", "For when strength is measured at a particular point or distance.", 0, 1, measurementPoint);
          case 957831062: /*country*/  return new Property("country", "CodeableConcept", "The country or countries for which the strength range applies.", 0, java.lang.Integer.MAX_VALUE, country);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 530040176: /*substance*/ return this.substance == null ? new Base[0] : new Base[] {this.substance}; // CodeableConcept
        case 1791316033: /*strength*/ return this.strength == null ? new Base[0] : new Base[] {this.strength}; // Ratio
        case 235437876: /*measurementPoint*/ return this.measurementPoint == null ? new Base[0] : new Base[] {this.measurementPoint}; // StringType
        case 957831062: /*country*/ return this.country == null ? new Base[0] : this.country.toArray(new Base[this.country.size()]); // CodeableConcept
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 530040176: // substance
          this.substance = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 1791316033: // strength
          this.strength = castToRatio(value); // Ratio
          return value;
        case 235437876: // measurementPoint
          this.measurementPoint = castToString(value); // StringType
          return value;
        case 957831062: // country
          this.getCountry().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("substance")) {
          this.substance = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("strength")) {
          this.strength = castToRatio(value); // Ratio
        } else if (name.equals("measurementPoint")) {
          this.measurementPoint = castToString(value); // StringType
        } else if (name.equals("country")) {
          this.getCountry().add(castToCodeableConcept(value));
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 530040176:  return getSubstance(); 
        case 1791316033:  return getStrength(); 
        case 235437876:  return getMeasurementPointElement();
        case 957831062:  return addCountry(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 530040176: /*substance*/ return new String[] {"CodeableConcept"};
        case 1791316033: /*strength*/ return new String[] {"Ratio"};
        case 235437876: /*measurementPoint*/ return new String[] {"string"};
        case 957831062: /*country*/ return new String[] {"CodeableConcept"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("substance")) {
          this.substance = new CodeableConcept();
          return this.substance;
        }
        else if (name.equals("strength")) {
          this.strength = new Ratio();
          return this.strength;
        }
        else if (name.equals("measurementPoint")) {
          throw new FHIRException("Cannot call addChild on a primitive type MedicinalProductIngredient.measurementPoint");
        }
        else if (name.equals("country")) {
          return addCountry();
        }
        else
          return super.addChild(name);
      }

      public MedicinalProductIngredientSpecifiedSubstanceStrengthReferenceStrengthComponent copy() {
        MedicinalProductIngredientSpecifiedSubstanceStrengthReferenceStrengthComponent dst = new MedicinalProductIngredientSpecifiedSubstanceStrengthReferenceStrengthComponent();
        copyValues(dst);
        dst.substance = substance == null ? null : substance.copy();
        dst.strength = strength == null ? null : strength.copy();
        dst.measurementPoint = measurementPoint == null ? null : measurementPoint.copy();
        if (country != null) {
          dst.country = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : country)
            dst.country.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof MedicinalProductIngredientSpecifiedSubstanceStrengthReferenceStrengthComponent))
          return false;
        MedicinalProductIngredientSpecifiedSubstanceStrengthReferenceStrengthComponent o = (MedicinalProductIngredientSpecifiedSubstanceStrengthReferenceStrengthComponent) other_;
        return compareDeep(substance, o.substance, true) && compareDeep(strength, o.strength, true) && compareDeep(measurementPoint, o.measurementPoint, true)
           && compareDeep(country, o.country, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof MedicinalProductIngredientSpecifiedSubstanceStrengthReferenceStrengthComponent))
          return false;
        MedicinalProductIngredientSpecifiedSubstanceStrengthReferenceStrengthComponent o = (MedicinalProductIngredientSpecifiedSubstanceStrengthReferenceStrengthComponent) other_;
        return compareValues(measurementPoint, o.measurementPoint, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(substance, strength, measurementPoint
          , country);
      }

  public String fhirType() {
    return "MedicinalProductIngredient.specifiedSubstance.strength.referenceStrength";

  }

  }

    @Block()
    public static class MedicinalProductIngredientSubstanceComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The ingredient substance.
         */
        @Child(name = "code", type = {CodeableConcept.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The ingredient substance", formalDefinition="The ingredient substance." )
        protected CodeableConcept code;

        /**
         * Quantity of the substance or specified substance present in the manufactured item or pharmaceutical product.
         */
        @Child(name = "strength", type = {MedicinalProductIngredientSpecifiedSubstanceStrengthComponent.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Quantity of the substance or specified substance present in the manufactured item or pharmaceutical product", formalDefinition="Quantity of the substance or specified substance present in the manufactured item or pharmaceutical product." )
        protected List<MedicinalProductIngredientSpecifiedSubstanceStrengthComponent> strength;

        private static final long serialVersionUID = 1325868149L;

    /**
     * Constructor
     */
      public MedicinalProductIngredientSubstanceComponent() {
        super();
      }

    /**
     * Constructor
     */
      public MedicinalProductIngredientSubstanceComponent(CodeableConcept code) {
        super();
        this.code = code;
      }

        /**
         * @return {@link #code} (The ingredient substance.)
         */
        public CodeableConcept getCode() { 
          if (this.code == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicinalProductIngredientSubstanceComponent.code");
            else if (Configuration.doAutoCreate())
              this.code = new CodeableConcept(); // cc
          return this.code;
        }

        public boolean hasCode() { 
          return this.code != null && !this.code.isEmpty();
        }

        /**
         * @param value {@link #code} (The ingredient substance.)
         */
        public MedicinalProductIngredientSubstanceComponent setCode(CodeableConcept value) { 
          this.code = value;
          return this;
        }

        /**
         * @return {@link #strength} (Quantity of the substance or specified substance present in the manufactured item or pharmaceutical product.)
         */
        public List<MedicinalProductIngredientSpecifiedSubstanceStrengthComponent> getStrength() { 
          if (this.strength == null)
            this.strength = new ArrayList<MedicinalProductIngredientSpecifiedSubstanceStrengthComponent>();
          return this.strength;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public MedicinalProductIngredientSubstanceComponent setStrength(List<MedicinalProductIngredientSpecifiedSubstanceStrengthComponent> theStrength) { 
          this.strength = theStrength;
          return this;
        }

        public boolean hasStrength() { 
          if (this.strength == null)
            return false;
          for (MedicinalProductIngredientSpecifiedSubstanceStrengthComponent item : this.strength)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public MedicinalProductIngredientSpecifiedSubstanceStrengthComponent addStrength() { //3
          MedicinalProductIngredientSpecifiedSubstanceStrengthComponent t = new MedicinalProductIngredientSpecifiedSubstanceStrengthComponent();
          if (this.strength == null)
            this.strength = new ArrayList<MedicinalProductIngredientSpecifiedSubstanceStrengthComponent>();
          this.strength.add(t);
          return t;
        }

        public MedicinalProductIngredientSubstanceComponent addStrength(MedicinalProductIngredientSpecifiedSubstanceStrengthComponent t) { //3
          if (t == null)
            return this;
          if (this.strength == null)
            this.strength = new ArrayList<MedicinalProductIngredientSpecifiedSubstanceStrengthComponent>();
          this.strength.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #strength}, creating it if it does not already exist
         */
        public MedicinalProductIngredientSpecifiedSubstanceStrengthComponent getStrengthFirstRep() { 
          if (getStrength().isEmpty()) {
            addStrength();
          }
          return getStrength().get(0);
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("code", "CodeableConcept", "The ingredient substance.", 0, 1, code));
          children.add(new Property("strength", "@MedicinalProductIngredient.specifiedSubstance.strength", "Quantity of the substance or specified substance present in the manufactured item or pharmaceutical product.", 0, java.lang.Integer.MAX_VALUE, strength));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3059181: /*code*/  return new Property("code", "CodeableConcept", "The ingredient substance.", 0, 1, code);
          case 1791316033: /*strength*/  return new Property("strength", "@MedicinalProductIngredient.specifiedSubstance.strength", "Quantity of the substance or specified substance present in the manufactured item or pharmaceutical product.", 0, java.lang.Integer.MAX_VALUE, strength);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3059181: /*code*/ return this.code == null ? new Base[0] : new Base[] {this.code}; // CodeableConcept
        case 1791316033: /*strength*/ return this.strength == null ? new Base[0] : this.strength.toArray(new Base[this.strength.size()]); // MedicinalProductIngredientSpecifiedSubstanceStrengthComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3059181: // code
          this.code = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 1791316033: // strength
          this.getStrength().add((MedicinalProductIngredientSpecifiedSubstanceStrengthComponent) value); // MedicinalProductIngredientSpecifiedSubstanceStrengthComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("code")) {
          this.code = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("strength")) {
          this.getStrength().add((MedicinalProductIngredientSpecifiedSubstanceStrengthComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3059181:  return getCode(); 
        case 1791316033:  return addStrength(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3059181: /*code*/ return new String[] {"CodeableConcept"};
        case 1791316033: /*strength*/ return new String[] {"@MedicinalProductIngredient.specifiedSubstance.strength"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("code")) {
          this.code = new CodeableConcept();
          return this.code;
        }
        else if (name.equals("strength")) {
          return addStrength();
        }
        else
          return super.addChild(name);
      }

      public MedicinalProductIngredientSubstanceComponent copy() {
        MedicinalProductIngredientSubstanceComponent dst = new MedicinalProductIngredientSubstanceComponent();
        copyValues(dst);
        dst.code = code == null ? null : code.copy();
        if (strength != null) {
          dst.strength = new ArrayList<MedicinalProductIngredientSpecifiedSubstanceStrengthComponent>();
          for (MedicinalProductIngredientSpecifiedSubstanceStrengthComponent i : strength)
            dst.strength.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof MedicinalProductIngredientSubstanceComponent))
          return false;
        MedicinalProductIngredientSubstanceComponent o = (MedicinalProductIngredientSubstanceComponent) other_;
        return compareDeep(code, o.code, true) && compareDeep(strength, o.strength, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof MedicinalProductIngredientSubstanceComponent))
          return false;
        MedicinalProductIngredientSubstanceComponent o = (MedicinalProductIngredientSubstanceComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(code, strength);
      }

  public String fhirType() {
    return "MedicinalProductIngredient.substance";

  }

  }

    /**
     * The identifier(s) of this Ingredient that are assigned by business processes and/or used to refer to it when a direct URL reference to the resource itself is not appropriate.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Identifier for the ingredient", formalDefinition="The identifier(s) of this Ingredient that are assigned by business processes and/or used to refer to it when a direct URL reference to the resource itself is not appropriate." )
    protected Identifier identifier;

    /**
     * Ingredient role e.g. Active ingredient, excipient.
     */
    @Child(name = "role", type = {CodeableConcept.class}, order=1, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Ingredient role e.g. Active ingredient, excipient", formalDefinition="Ingredient role e.g. Active ingredient, excipient." )
    protected CodeableConcept role;

    /**
     * If the ingredient is a known or suspected allergen.
     */
    @Child(name = "allergenicIndicator", type = {BooleanType.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="If the ingredient is a known or suspected allergen", formalDefinition="If the ingredient is a known or suspected allergen." )
    protected BooleanType allergenicIndicator;

    /**
     * Manufacturer of this Ingredient.
     */
    @Child(name = "manufacturer", type = {Organization.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Manufacturer of this Ingredient", formalDefinition="Manufacturer of this Ingredient." )
    protected List<Reference> manufacturer;
    /**
     * The actual objects that are the target of the reference (Manufacturer of this Ingredient.)
     */
    protected List<Organization> manufacturerTarget;


    /**
     * A specified substance that comprises this ingredient.
     */
    @Child(name = "specifiedSubstance", type = {}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="A specified substance that comprises this ingredient", formalDefinition="A specified substance that comprises this ingredient." )
    protected List<MedicinalProductIngredientSpecifiedSubstanceComponent> specifiedSubstance;

    /**
     * The ingredient substance.
     */
    @Child(name = "substance", type = {}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The ingredient substance", formalDefinition="The ingredient substance." )
    protected MedicinalProductIngredientSubstanceComponent substance;

    private static final long serialVersionUID = -1454686641L;

  /**
   * Constructor
   */
    public MedicinalProductIngredient() {
      super();
    }

  /**
   * Constructor
   */
    public MedicinalProductIngredient(CodeableConcept role) {
      super();
      this.role = role;
    }

    /**
     * @return {@link #identifier} (The identifier(s) of this Ingredient that are assigned by business processes and/or used to refer to it when a direct URL reference to the resource itself is not appropriate.)
     */
    public Identifier getIdentifier() { 
      if (this.identifier == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicinalProductIngredient.identifier");
        else if (Configuration.doAutoCreate())
          this.identifier = new Identifier(); // cc
      return this.identifier;
    }

    public boolean hasIdentifier() { 
      return this.identifier != null && !this.identifier.isEmpty();
    }

    /**
     * @param value {@link #identifier} (The identifier(s) of this Ingredient that are assigned by business processes and/or used to refer to it when a direct URL reference to the resource itself is not appropriate.)
     */
    public MedicinalProductIngredient setIdentifier(Identifier value) { 
      this.identifier = value;
      return this;
    }

    /**
     * @return {@link #role} (Ingredient role e.g. Active ingredient, excipient.)
     */
    public CodeableConcept getRole() { 
      if (this.role == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicinalProductIngredient.role");
        else if (Configuration.doAutoCreate())
          this.role = new CodeableConcept(); // cc
      return this.role;
    }

    public boolean hasRole() { 
      return this.role != null && !this.role.isEmpty();
    }

    /**
     * @param value {@link #role} (Ingredient role e.g. Active ingredient, excipient.)
     */
    public MedicinalProductIngredient setRole(CodeableConcept value) { 
      this.role = value;
      return this;
    }

    /**
     * @return {@link #allergenicIndicator} (If the ingredient is a known or suspected allergen.). This is the underlying object with id, value and extensions. The accessor "getAllergenicIndicator" gives direct access to the value
     */
    public BooleanType getAllergenicIndicatorElement() { 
      if (this.allergenicIndicator == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicinalProductIngredient.allergenicIndicator");
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
     * @param value {@link #allergenicIndicator} (If the ingredient is a known or suspected allergen.). This is the underlying object with id, value and extensions. The accessor "getAllergenicIndicator" gives direct access to the value
     */
    public MedicinalProductIngredient setAllergenicIndicatorElement(BooleanType value) { 
      this.allergenicIndicator = value;
      return this;
    }

    /**
     * @return If the ingredient is a known or suspected allergen.
     */
    public boolean getAllergenicIndicator() { 
      return this.allergenicIndicator == null || this.allergenicIndicator.isEmpty() ? false : this.allergenicIndicator.getValue();
    }

    /**
     * @param value If the ingredient is a known or suspected allergen.
     */
    public MedicinalProductIngredient setAllergenicIndicator(boolean value) { 
        if (this.allergenicIndicator == null)
          this.allergenicIndicator = new BooleanType();
        this.allergenicIndicator.setValue(value);
      return this;
    }

    /**
     * @return {@link #manufacturer} (Manufacturer of this Ingredient.)
     */
    public List<Reference> getManufacturer() { 
      if (this.manufacturer == null)
        this.manufacturer = new ArrayList<Reference>();
      return this.manufacturer;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicinalProductIngredient setManufacturer(List<Reference> theManufacturer) { 
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

    public MedicinalProductIngredient addManufacturer(Reference t) { //3
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
     * @return {@link #specifiedSubstance} (A specified substance that comprises this ingredient.)
     */
    public List<MedicinalProductIngredientSpecifiedSubstanceComponent> getSpecifiedSubstance() { 
      if (this.specifiedSubstance == null)
        this.specifiedSubstance = new ArrayList<MedicinalProductIngredientSpecifiedSubstanceComponent>();
      return this.specifiedSubstance;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicinalProductIngredient setSpecifiedSubstance(List<MedicinalProductIngredientSpecifiedSubstanceComponent> theSpecifiedSubstance) { 
      this.specifiedSubstance = theSpecifiedSubstance;
      return this;
    }

    public boolean hasSpecifiedSubstance() { 
      if (this.specifiedSubstance == null)
        return false;
      for (MedicinalProductIngredientSpecifiedSubstanceComponent item : this.specifiedSubstance)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public MedicinalProductIngredientSpecifiedSubstanceComponent addSpecifiedSubstance() { //3
      MedicinalProductIngredientSpecifiedSubstanceComponent t = new MedicinalProductIngredientSpecifiedSubstanceComponent();
      if (this.specifiedSubstance == null)
        this.specifiedSubstance = new ArrayList<MedicinalProductIngredientSpecifiedSubstanceComponent>();
      this.specifiedSubstance.add(t);
      return t;
    }

    public MedicinalProductIngredient addSpecifiedSubstance(MedicinalProductIngredientSpecifiedSubstanceComponent t) { //3
      if (t == null)
        return this;
      if (this.specifiedSubstance == null)
        this.specifiedSubstance = new ArrayList<MedicinalProductIngredientSpecifiedSubstanceComponent>();
      this.specifiedSubstance.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #specifiedSubstance}, creating it if it does not already exist
     */
    public MedicinalProductIngredientSpecifiedSubstanceComponent getSpecifiedSubstanceFirstRep() { 
      if (getSpecifiedSubstance().isEmpty()) {
        addSpecifiedSubstance();
      }
      return getSpecifiedSubstance().get(0);
    }

    /**
     * @return {@link #substance} (The ingredient substance.)
     */
    public MedicinalProductIngredientSubstanceComponent getSubstance() { 
      if (this.substance == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicinalProductIngredient.substance");
        else if (Configuration.doAutoCreate())
          this.substance = new MedicinalProductIngredientSubstanceComponent(); // cc
      return this.substance;
    }

    public boolean hasSubstance() { 
      return this.substance != null && !this.substance.isEmpty();
    }

    /**
     * @param value {@link #substance} (The ingredient substance.)
     */
    public MedicinalProductIngredient setSubstance(MedicinalProductIngredientSubstanceComponent value) { 
      this.substance = value;
      return this;
    }

      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("identifier", "Identifier", "The identifier(s) of this Ingredient that are assigned by business processes and/or used to refer to it when a direct URL reference to the resource itself is not appropriate.", 0, 1, identifier));
        children.add(new Property("role", "CodeableConcept", "Ingredient role e.g. Active ingredient, excipient.", 0, 1, role));
        children.add(new Property("allergenicIndicator", "boolean", "If the ingredient is a known or suspected allergen.", 0, 1, allergenicIndicator));
        children.add(new Property("manufacturer", "Reference(Organization)", "Manufacturer of this Ingredient.", 0, java.lang.Integer.MAX_VALUE, manufacturer));
        children.add(new Property("specifiedSubstance", "", "A specified substance that comprises this ingredient.", 0, java.lang.Integer.MAX_VALUE, specifiedSubstance));
        children.add(new Property("substance", "", "The ingredient substance.", 0, 1, substance));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case -1618432855: /*identifier*/  return new Property("identifier", "Identifier", "The identifier(s) of this Ingredient that are assigned by business processes and/or used to refer to it when a direct URL reference to the resource itself is not appropriate.", 0, 1, identifier);
        case 3506294: /*role*/  return new Property("role", "CodeableConcept", "Ingredient role e.g. Active ingredient, excipient.", 0, 1, role);
        case 75406931: /*allergenicIndicator*/  return new Property("allergenicIndicator", "boolean", "If the ingredient is a known or suspected allergen.", 0, 1, allergenicIndicator);
        case -1969347631: /*manufacturer*/  return new Property("manufacturer", "Reference(Organization)", "Manufacturer of this Ingredient.", 0, java.lang.Integer.MAX_VALUE, manufacturer);
        case -331477600: /*specifiedSubstance*/  return new Property("specifiedSubstance", "", "A specified substance that comprises this ingredient.", 0, java.lang.Integer.MAX_VALUE, specifiedSubstance);
        case 530040176: /*substance*/  return new Property("substance", "", "The ingredient substance.", 0, 1, substance);
        default: return super.getNamedProperty(_hash, _name, _checkValid);
        }

      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : new Base[] {this.identifier}; // Identifier
        case 3506294: /*role*/ return this.role == null ? new Base[0] : new Base[] {this.role}; // CodeableConcept
        case 75406931: /*allergenicIndicator*/ return this.allergenicIndicator == null ? new Base[0] : new Base[] {this.allergenicIndicator}; // BooleanType
        case -1969347631: /*manufacturer*/ return this.manufacturer == null ? new Base[0] : this.manufacturer.toArray(new Base[this.manufacturer.size()]); // Reference
        case -331477600: /*specifiedSubstance*/ return this.specifiedSubstance == null ? new Base[0] : this.specifiedSubstance.toArray(new Base[this.specifiedSubstance.size()]); // MedicinalProductIngredientSpecifiedSubstanceComponent
        case 530040176: /*substance*/ return this.substance == null ? new Base[0] : new Base[] {this.substance}; // MedicinalProductIngredientSubstanceComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.identifier = castToIdentifier(value); // Identifier
          return value;
        case 3506294: // role
          this.role = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 75406931: // allergenicIndicator
          this.allergenicIndicator = castToBoolean(value); // BooleanType
          return value;
        case -1969347631: // manufacturer
          this.getManufacturer().add(castToReference(value)); // Reference
          return value;
        case -331477600: // specifiedSubstance
          this.getSpecifiedSubstance().add((MedicinalProductIngredientSpecifiedSubstanceComponent) value); // MedicinalProductIngredientSpecifiedSubstanceComponent
          return value;
        case 530040176: // substance
          this.substance = (MedicinalProductIngredientSubstanceComponent) value; // MedicinalProductIngredientSubstanceComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier")) {
          this.identifier = castToIdentifier(value); // Identifier
        } else if (name.equals("role")) {
          this.role = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("allergenicIndicator")) {
          this.allergenicIndicator = castToBoolean(value); // BooleanType
        } else if (name.equals("manufacturer")) {
          this.getManufacturer().add(castToReference(value));
        } else if (name.equals("specifiedSubstance")) {
          this.getSpecifiedSubstance().add((MedicinalProductIngredientSpecifiedSubstanceComponent) value);
        } else if (name.equals("substance")) {
          this.substance = (MedicinalProductIngredientSubstanceComponent) value; // MedicinalProductIngredientSubstanceComponent
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return getIdentifier(); 
        case 3506294:  return getRole(); 
        case 75406931:  return getAllergenicIndicatorElement();
        case -1969347631:  return addManufacturer(); 
        case -331477600:  return addSpecifiedSubstance(); 
        case 530040176:  return getSubstance(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case 3506294: /*role*/ return new String[] {"CodeableConcept"};
        case 75406931: /*allergenicIndicator*/ return new String[] {"boolean"};
        case -1969347631: /*manufacturer*/ return new String[] {"Reference"};
        case -331477600: /*specifiedSubstance*/ return new String[] {};
        case 530040176: /*substance*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          this.identifier = new Identifier();
          return this.identifier;
        }
        else if (name.equals("role")) {
          this.role = new CodeableConcept();
          return this.role;
        }
        else if (name.equals("allergenicIndicator")) {
          throw new FHIRException("Cannot call addChild on a primitive type MedicinalProductIngredient.allergenicIndicator");
        }
        else if (name.equals("manufacturer")) {
          return addManufacturer();
        }
        else if (name.equals("specifiedSubstance")) {
          return addSpecifiedSubstance();
        }
        else if (name.equals("substance")) {
          this.substance = new MedicinalProductIngredientSubstanceComponent();
          return this.substance;
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "MedicinalProductIngredient";

  }

      public MedicinalProductIngredient copy() {
        MedicinalProductIngredient dst = new MedicinalProductIngredient();
        copyValues(dst);
        dst.identifier = identifier == null ? null : identifier.copy();
        dst.role = role == null ? null : role.copy();
        dst.allergenicIndicator = allergenicIndicator == null ? null : allergenicIndicator.copy();
        if (manufacturer != null) {
          dst.manufacturer = new ArrayList<Reference>();
          for (Reference i : manufacturer)
            dst.manufacturer.add(i.copy());
        };
        if (specifiedSubstance != null) {
          dst.specifiedSubstance = new ArrayList<MedicinalProductIngredientSpecifiedSubstanceComponent>();
          for (MedicinalProductIngredientSpecifiedSubstanceComponent i : specifiedSubstance)
            dst.specifiedSubstance.add(i.copy());
        };
        dst.substance = substance == null ? null : substance.copy();
        return dst;
      }

      protected MedicinalProductIngredient typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof MedicinalProductIngredient))
          return false;
        MedicinalProductIngredient o = (MedicinalProductIngredient) other_;
        return compareDeep(identifier, o.identifier, true) && compareDeep(role, o.role, true) && compareDeep(allergenicIndicator, o.allergenicIndicator, true)
           && compareDeep(manufacturer, o.manufacturer, true) && compareDeep(specifiedSubstance, o.specifiedSubstance, true)
           && compareDeep(substance, o.substance, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof MedicinalProductIngredient))
          return false;
        MedicinalProductIngredient o = (MedicinalProductIngredient) other_;
        return compareValues(allergenicIndicator, o.allergenicIndicator, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, role, allergenicIndicator
          , manufacturer, specifiedSubstance, substance);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.MedicinalProductIngredient;
   }


}

