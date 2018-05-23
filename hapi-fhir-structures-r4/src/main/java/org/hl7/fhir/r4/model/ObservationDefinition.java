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

import java.math.*;
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
 * Set of definitional characteristics for a kind of observation or measurement produced or consumed by an orderable health care service.
 */
@ResourceDef(name="ObservationDefinition", profile="http://hl7.org/fhir/Profile/ObservationDefinition")
public class ObservationDefinition extends DomainResource {

    @Block()
    public static class ObservationDefinitionQuantitativeDetailsComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Customary unit used to report quantitative results of this observation.
         */
        @Child(name = "customaryUnit", type = {Coding.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Customary unit for quantitative results", formalDefinition="Customary unit used to report quantitative results of this observation." )
        protected Coding customaryUnit;

        /**
         * SI unit used to report quantitative results of this observation.
         */
        @Child(name = "unit", type = {Coding.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="SI unit for quantitative results", formalDefinition="SI unit used to report quantitative results of this observation." )
        protected Coding unit;

        /**
         * Factor for converting value expressed with SI unit to value expressed with customary unit.
         */
        @Child(name = "conversionFactor", type = {DecimalType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="SI to Customary unit conversion factor", formalDefinition="Factor for converting value expressed with SI unit to value expressed with customary unit." )
        protected DecimalType conversionFactor;

        /**
         * Number of digits after decimal separator when the results of this observation are of type Quantity.
         */
        @Child(name = "decimalPrecision", type = {IntegerType.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Decimal precision of observation quantitative results", formalDefinition="Number of digits after decimal separator when the results of this observation are of type Quantity." )
        protected IntegerType decimalPrecision;

        private static final long serialVersionUID = -1533265578L;

    /**
     * Constructor
     */
      public ObservationDefinitionQuantitativeDetailsComponent() {
        super();
      }

        /**
         * @return {@link #customaryUnit} (Customary unit used to report quantitative results of this observation.)
         */
        public Coding getCustomaryUnit() { 
          if (this.customaryUnit == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ObservationDefinitionQuantitativeDetailsComponent.customaryUnit");
            else if (Configuration.doAutoCreate())
              this.customaryUnit = new Coding(); // cc
          return this.customaryUnit;
        }

        public boolean hasCustomaryUnit() { 
          return this.customaryUnit != null && !this.customaryUnit.isEmpty();
        }

        /**
         * @param value {@link #customaryUnit} (Customary unit used to report quantitative results of this observation.)
         */
        public ObservationDefinitionQuantitativeDetailsComponent setCustomaryUnit(Coding value) { 
          this.customaryUnit = value;
          return this;
        }

        /**
         * @return {@link #unit} (SI unit used to report quantitative results of this observation.)
         */
        public Coding getUnit() { 
          if (this.unit == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ObservationDefinitionQuantitativeDetailsComponent.unit");
            else if (Configuration.doAutoCreate())
              this.unit = new Coding(); // cc
          return this.unit;
        }

        public boolean hasUnit() { 
          return this.unit != null && !this.unit.isEmpty();
        }

        /**
         * @param value {@link #unit} (SI unit used to report quantitative results of this observation.)
         */
        public ObservationDefinitionQuantitativeDetailsComponent setUnit(Coding value) { 
          this.unit = value;
          return this;
        }

        /**
         * @return {@link #conversionFactor} (Factor for converting value expressed with SI unit to value expressed with customary unit.). This is the underlying object with id, value and extensions. The accessor "getConversionFactor" gives direct access to the value
         */
        public DecimalType getConversionFactorElement() { 
          if (this.conversionFactor == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ObservationDefinitionQuantitativeDetailsComponent.conversionFactor");
            else if (Configuration.doAutoCreate())
              this.conversionFactor = new DecimalType(); // bb
          return this.conversionFactor;
        }

        public boolean hasConversionFactorElement() { 
          return this.conversionFactor != null && !this.conversionFactor.isEmpty();
        }

        public boolean hasConversionFactor() { 
          return this.conversionFactor != null && !this.conversionFactor.isEmpty();
        }

        /**
         * @param value {@link #conversionFactor} (Factor for converting value expressed with SI unit to value expressed with customary unit.). This is the underlying object with id, value and extensions. The accessor "getConversionFactor" gives direct access to the value
         */
        public ObservationDefinitionQuantitativeDetailsComponent setConversionFactorElement(DecimalType value) { 
          this.conversionFactor = value;
          return this;
        }

        /**
         * @return Factor for converting value expressed with SI unit to value expressed with customary unit.
         */
        public BigDecimal getConversionFactor() { 
          return this.conversionFactor == null ? null : this.conversionFactor.getValue();
        }

        /**
         * @param value Factor for converting value expressed with SI unit to value expressed with customary unit.
         */
        public ObservationDefinitionQuantitativeDetailsComponent setConversionFactor(BigDecimal value) { 
          if (value == null)
            this.conversionFactor = null;
          else {
            if (this.conversionFactor == null)
              this.conversionFactor = new DecimalType();
            this.conversionFactor.setValue(value);
          }
          return this;
        }

        /**
         * @param value Factor for converting value expressed with SI unit to value expressed with customary unit.
         */
        public ObservationDefinitionQuantitativeDetailsComponent setConversionFactor(long value) { 
              this.conversionFactor = new DecimalType();
            this.conversionFactor.setValue(value);
          return this;
        }

        /**
         * @param value Factor for converting value expressed with SI unit to value expressed with customary unit.
         */
        public ObservationDefinitionQuantitativeDetailsComponent setConversionFactor(double value) { 
              this.conversionFactor = new DecimalType();
            this.conversionFactor.setValue(value);
          return this;
        }

        /**
         * @return {@link #decimalPrecision} (Number of digits after decimal separator when the results of this observation are of type Quantity.). This is the underlying object with id, value and extensions. The accessor "getDecimalPrecision" gives direct access to the value
         */
        public IntegerType getDecimalPrecisionElement() { 
          if (this.decimalPrecision == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ObservationDefinitionQuantitativeDetailsComponent.decimalPrecision");
            else if (Configuration.doAutoCreate())
              this.decimalPrecision = new IntegerType(); // bb
          return this.decimalPrecision;
        }

        public boolean hasDecimalPrecisionElement() { 
          return this.decimalPrecision != null && !this.decimalPrecision.isEmpty();
        }

        public boolean hasDecimalPrecision() { 
          return this.decimalPrecision != null && !this.decimalPrecision.isEmpty();
        }

        /**
         * @param value {@link #decimalPrecision} (Number of digits after decimal separator when the results of this observation are of type Quantity.). This is the underlying object with id, value and extensions. The accessor "getDecimalPrecision" gives direct access to the value
         */
        public ObservationDefinitionQuantitativeDetailsComponent setDecimalPrecisionElement(IntegerType value) { 
          this.decimalPrecision = value;
          return this;
        }

        /**
         * @return Number of digits after decimal separator when the results of this observation are of type Quantity.
         */
        public int getDecimalPrecision() { 
          return this.decimalPrecision == null || this.decimalPrecision.isEmpty() ? 0 : this.decimalPrecision.getValue();
        }

        /**
         * @param value Number of digits after decimal separator when the results of this observation are of type Quantity.
         */
        public ObservationDefinitionQuantitativeDetailsComponent setDecimalPrecision(int value) { 
            if (this.decimalPrecision == null)
              this.decimalPrecision = new IntegerType();
            this.decimalPrecision.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("customaryUnit", "Coding", "Customary unit used to report quantitative results of this observation.", 0, 1, customaryUnit));
          children.add(new Property("unit", "Coding", "SI unit used to report quantitative results of this observation.", 0, 1, unit));
          children.add(new Property("conversionFactor", "decimal", "Factor for converting value expressed with SI unit to value expressed with customary unit.", 0, 1, conversionFactor));
          children.add(new Property("decimalPrecision", "integer", "Number of digits after decimal separator when the results of this observation are of type Quantity.", 0, 1, decimalPrecision));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -1375586437: /*customaryUnit*/  return new Property("customaryUnit", "Coding", "Customary unit used to report quantitative results of this observation.", 0, 1, customaryUnit);
          case 3594628: /*unit*/  return new Property("unit", "Coding", "SI unit used to report quantitative results of this observation.", 0, 1, unit);
          case 1438876165: /*conversionFactor*/  return new Property("conversionFactor", "decimal", "Factor for converting value expressed with SI unit to value expressed with customary unit.", 0, 1, conversionFactor);
          case -1564447699: /*decimalPrecision*/  return new Property("decimalPrecision", "integer", "Number of digits after decimal separator when the results of this observation are of type Quantity.", 0, 1, decimalPrecision);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1375586437: /*customaryUnit*/ return this.customaryUnit == null ? new Base[0] : new Base[] {this.customaryUnit}; // Coding
        case 3594628: /*unit*/ return this.unit == null ? new Base[0] : new Base[] {this.unit}; // Coding
        case 1438876165: /*conversionFactor*/ return this.conversionFactor == null ? new Base[0] : new Base[] {this.conversionFactor}; // DecimalType
        case -1564447699: /*decimalPrecision*/ return this.decimalPrecision == null ? new Base[0] : new Base[] {this.decimalPrecision}; // IntegerType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1375586437: // customaryUnit
          this.customaryUnit = castToCoding(value); // Coding
          return value;
        case 3594628: // unit
          this.unit = castToCoding(value); // Coding
          return value;
        case 1438876165: // conversionFactor
          this.conversionFactor = castToDecimal(value); // DecimalType
          return value;
        case -1564447699: // decimalPrecision
          this.decimalPrecision = castToInteger(value); // IntegerType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("customaryUnit")) {
          this.customaryUnit = castToCoding(value); // Coding
        } else if (name.equals("unit")) {
          this.unit = castToCoding(value); // Coding
        } else if (name.equals("conversionFactor")) {
          this.conversionFactor = castToDecimal(value); // DecimalType
        } else if (name.equals("decimalPrecision")) {
          this.decimalPrecision = castToInteger(value); // IntegerType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1375586437:  return getCustomaryUnit(); 
        case 3594628:  return getUnit(); 
        case 1438876165:  return getConversionFactorElement();
        case -1564447699:  return getDecimalPrecisionElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1375586437: /*customaryUnit*/ return new String[] {"Coding"};
        case 3594628: /*unit*/ return new String[] {"Coding"};
        case 1438876165: /*conversionFactor*/ return new String[] {"decimal"};
        case -1564447699: /*decimalPrecision*/ return new String[] {"integer"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("customaryUnit")) {
          this.customaryUnit = new Coding();
          return this.customaryUnit;
        }
        else if (name.equals("unit")) {
          this.unit = new Coding();
          return this.unit;
        }
        else if (name.equals("conversionFactor")) {
          throw new FHIRException("Cannot call addChild on a primitive type ObservationDefinition.conversionFactor");
        }
        else if (name.equals("decimalPrecision")) {
          throw new FHIRException("Cannot call addChild on a primitive type ObservationDefinition.decimalPrecision");
        }
        else
          return super.addChild(name);
      }

      public ObservationDefinitionQuantitativeDetailsComponent copy() {
        ObservationDefinitionQuantitativeDetailsComponent dst = new ObservationDefinitionQuantitativeDetailsComponent();
        copyValues(dst);
        dst.customaryUnit = customaryUnit == null ? null : customaryUnit.copy();
        dst.unit = unit == null ? null : unit.copy();
        dst.conversionFactor = conversionFactor == null ? null : conversionFactor.copy();
        dst.decimalPrecision = decimalPrecision == null ? null : decimalPrecision.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ObservationDefinitionQuantitativeDetailsComponent))
          return false;
        ObservationDefinitionQuantitativeDetailsComponent o = (ObservationDefinitionQuantitativeDetailsComponent) other_;
        return compareDeep(customaryUnit, o.customaryUnit, true) && compareDeep(unit, o.unit, true) && compareDeep(conversionFactor, o.conversionFactor, true)
           && compareDeep(decimalPrecision, o.decimalPrecision, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ObservationDefinitionQuantitativeDetailsComponent))
          return false;
        ObservationDefinitionQuantitativeDetailsComponent o = (ObservationDefinitionQuantitativeDetailsComponent) other_;
        return compareValues(conversionFactor, o.conversionFactor, true) && compareValues(decimalPrecision, o.decimalPrecision, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(customaryUnit, unit, conversionFactor
          , decimalPrecision);
      }

  public String fhirType() {
    return "ObservationDefinition.quantitativeDetails";

  }

  }

    @Block()
    public static class ObservationDefinitionQualifiedIntervalComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The category or type of interval.
         */
        @Child(name = "category", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The category or type of interval", formalDefinition="The category or type of interval." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/observation-category")
        protected CodeableConcept category;

        /**
         * The value and associated unit of the low bound (inclusive) of the reference range.
         */
        @Child(name = "range", type = {Range.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Low bound of reference range, if relevant", formalDefinition="The value and associated unit of the low bound (inclusive) of the reference range." )
        protected Range range;

        /**
         * Codes to indicate what part of the targeted reference population it applies to. For example, the normal or therapeutic range.
         */
        @Child(name = "type", type = {CodeableConcept.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Reference range qualifier", formalDefinition="Codes to indicate what part of the targeted reference population it applies to. For example, the normal or therapeutic range." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/referencerange-meaning")
        protected CodeableConcept type;

        /**
         * Codes to indicate the target population this reference range applies to.
         */
        @Child(name = "appliesTo", type = {CodeableConcept.class}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Reference range population", formalDefinition="Codes to indicate the target population this reference range applies to." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/referencerange-appliesto")
        protected List<CodeableConcept> appliesTo;

        /**
         * The age at which this reference range is applicable. This is a neonatal age (e.g. number of weeks at term) if the meaning says so.
         */
        @Child(name = "age", type = {Range.class}, order=5, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Applicable age range, if relevant", formalDefinition="The age at which this reference range is applicable. This is a neonatal age (e.g. number of weeks at term) if the meaning says so." )
        protected Range age;

        /**
         * The gestational age at which this reference range is applicable, in the context of pregnancy.
         */
        @Child(name = "gestationalAge", type = {Range.class}, order=6, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Applicable gestational age range, if relevant", formalDefinition="The gestational age at which this reference range is applicable, in the context of pregnancy." )
        protected Range gestationalAge;

        /**
         * Text based condition for which the reference range is valid.
         */
        @Child(name = "condition", type = {StringType.class}, order=7, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Condition associated with the reference range", formalDefinition="Text based condition for which the reference range is valid." )
        protected StringType condition;

        private static final long serialVersionUID = 1566527077L;

    /**
     * Constructor
     */
      public ObservationDefinitionQualifiedIntervalComponent() {
        super();
      }

        /**
         * @return {@link #category} (The category or type of interval.)
         */
        public CodeableConcept getCategory() { 
          if (this.category == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ObservationDefinitionQualifiedIntervalComponent.category");
            else if (Configuration.doAutoCreate())
              this.category = new CodeableConcept(); // cc
          return this.category;
        }

        public boolean hasCategory() { 
          return this.category != null && !this.category.isEmpty();
        }

        /**
         * @param value {@link #category} (The category or type of interval.)
         */
        public ObservationDefinitionQualifiedIntervalComponent setCategory(CodeableConcept value) { 
          this.category = value;
          return this;
        }

        /**
         * @return {@link #range} (The value and associated unit of the low bound (inclusive) of the reference range.)
         */
        public Range getRange() { 
          if (this.range == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ObservationDefinitionQualifiedIntervalComponent.range");
            else if (Configuration.doAutoCreate())
              this.range = new Range(); // cc
          return this.range;
        }

        public boolean hasRange() { 
          return this.range != null && !this.range.isEmpty();
        }

        /**
         * @param value {@link #range} (The value and associated unit of the low bound (inclusive) of the reference range.)
         */
        public ObservationDefinitionQualifiedIntervalComponent setRange(Range value) { 
          this.range = value;
          return this;
        }

        /**
         * @return {@link #type} (Codes to indicate what part of the targeted reference population it applies to. For example, the normal or therapeutic range.)
         */
        public CodeableConcept getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ObservationDefinitionQualifiedIntervalComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new CodeableConcept(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (Codes to indicate what part of the targeted reference population it applies to. For example, the normal or therapeutic range.)
         */
        public ObservationDefinitionQualifiedIntervalComponent setType(CodeableConcept value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #appliesTo} (Codes to indicate the target population this reference range applies to.)
         */
        public List<CodeableConcept> getAppliesTo() { 
          if (this.appliesTo == null)
            this.appliesTo = new ArrayList<CodeableConcept>();
          return this.appliesTo;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ObservationDefinitionQualifiedIntervalComponent setAppliesTo(List<CodeableConcept> theAppliesTo) { 
          this.appliesTo = theAppliesTo;
          return this;
        }

        public boolean hasAppliesTo() { 
          if (this.appliesTo == null)
            return false;
          for (CodeableConcept item : this.appliesTo)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public CodeableConcept addAppliesTo() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.appliesTo == null)
            this.appliesTo = new ArrayList<CodeableConcept>();
          this.appliesTo.add(t);
          return t;
        }

        public ObservationDefinitionQualifiedIntervalComponent addAppliesTo(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.appliesTo == null)
            this.appliesTo = new ArrayList<CodeableConcept>();
          this.appliesTo.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #appliesTo}, creating it if it does not already exist
         */
        public CodeableConcept getAppliesToFirstRep() { 
          if (getAppliesTo().isEmpty()) {
            addAppliesTo();
          }
          return getAppliesTo().get(0);
        }

        /**
         * @return {@link #age} (The age at which this reference range is applicable. This is a neonatal age (e.g. number of weeks at term) if the meaning says so.)
         */
        public Range getAge() { 
          if (this.age == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ObservationDefinitionQualifiedIntervalComponent.age");
            else if (Configuration.doAutoCreate())
              this.age = new Range(); // cc
          return this.age;
        }

        public boolean hasAge() { 
          return this.age != null && !this.age.isEmpty();
        }

        /**
         * @param value {@link #age} (The age at which this reference range is applicable. This is a neonatal age (e.g. number of weeks at term) if the meaning says so.)
         */
        public ObservationDefinitionQualifiedIntervalComponent setAge(Range value) { 
          this.age = value;
          return this;
        }

        /**
         * @return {@link #gestationalAge} (The gestational age at which this reference range is applicable, in the context of pregnancy.)
         */
        public Range getGestationalAge() { 
          if (this.gestationalAge == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ObservationDefinitionQualifiedIntervalComponent.gestationalAge");
            else if (Configuration.doAutoCreate())
              this.gestationalAge = new Range(); // cc
          return this.gestationalAge;
        }

        public boolean hasGestationalAge() { 
          return this.gestationalAge != null && !this.gestationalAge.isEmpty();
        }

        /**
         * @param value {@link #gestationalAge} (The gestational age at which this reference range is applicable, in the context of pregnancy.)
         */
        public ObservationDefinitionQualifiedIntervalComponent setGestationalAge(Range value) { 
          this.gestationalAge = value;
          return this;
        }

        /**
         * @return {@link #condition} (Text based condition for which the reference range is valid.). This is the underlying object with id, value and extensions. The accessor "getCondition" gives direct access to the value
         */
        public StringType getConditionElement() { 
          if (this.condition == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ObservationDefinitionQualifiedIntervalComponent.condition");
            else if (Configuration.doAutoCreate())
              this.condition = new StringType(); // bb
          return this.condition;
        }

        public boolean hasConditionElement() { 
          return this.condition != null && !this.condition.isEmpty();
        }

        public boolean hasCondition() { 
          return this.condition != null && !this.condition.isEmpty();
        }

        /**
         * @param value {@link #condition} (Text based condition for which the reference range is valid.). This is the underlying object with id, value and extensions. The accessor "getCondition" gives direct access to the value
         */
        public ObservationDefinitionQualifiedIntervalComponent setConditionElement(StringType value) { 
          this.condition = value;
          return this;
        }

        /**
         * @return Text based condition for which the reference range is valid.
         */
        public String getCondition() { 
          return this.condition == null ? null : this.condition.getValue();
        }

        /**
         * @param value Text based condition for which the reference range is valid.
         */
        public ObservationDefinitionQualifiedIntervalComponent setCondition(String value) { 
          if (Utilities.noString(value))
            this.condition = null;
          else {
            if (this.condition == null)
              this.condition = new StringType();
            this.condition.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("category", "CodeableConcept", "The category or type of interval.", 0, 1, category));
          children.add(new Property("range", "Range", "The value and associated unit of the low bound (inclusive) of the reference range.", 0, 1, range));
          children.add(new Property("type", "CodeableConcept", "Codes to indicate what part of the targeted reference population it applies to. For example, the normal or therapeutic range.", 0, 1, type));
          children.add(new Property("appliesTo", "CodeableConcept", "Codes to indicate the target population this reference range applies to.", 0, java.lang.Integer.MAX_VALUE, appliesTo));
          children.add(new Property("age", "Range", "The age at which this reference range is applicable. This is a neonatal age (e.g. number of weeks at term) if the meaning says so.", 0, 1, age));
          children.add(new Property("gestationalAge", "Range", "The gestational age at which this reference range is applicable, in the context of pregnancy.", 0, 1, gestationalAge));
          children.add(new Property("condition", "string", "Text based condition for which the reference range is valid.", 0, 1, condition));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 50511102: /*category*/  return new Property("category", "CodeableConcept", "The category or type of interval.", 0, 1, category);
          case 108280125: /*range*/  return new Property("range", "Range", "The value and associated unit of the low bound (inclusive) of the reference range.", 0, 1, range);
          case 3575610: /*type*/  return new Property("type", "CodeableConcept", "Codes to indicate what part of the targeted reference population it applies to. For example, the normal or therapeutic range.", 0, 1, type);
          case -2089924569: /*appliesTo*/  return new Property("appliesTo", "CodeableConcept", "Codes to indicate the target population this reference range applies to.", 0, java.lang.Integer.MAX_VALUE, appliesTo);
          case 96511: /*age*/  return new Property("age", "Range", "The age at which this reference range is applicable. This is a neonatal age (e.g. number of weeks at term) if the meaning says so.", 0, 1, age);
          case -241217538: /*gestationalAge*/  return new Property("gestationalAge", "Range", "The gestational age at which this reference range is applicable, in the context of pregnancy.", 0, 1, gestationalAge);
          case -861311717: /*condition*/  return new Property("condition", "string", "Text based condition for which the reference range is valid.", 0, 1, condition);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 50511102: /*category*/ return this.category == null ? new Base[0] : new Base[] {this.category}; // CodeableConcept
        case 108280125: /*range*/ return this.range == null ? new Base[0] : new Base[] {this.range}; // Range
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case -2089924569: /*appliesTo*/ return this.appliesTo == null ? new Base[0] : this.appliesTo.toArray(new Base[this.appliesTo.size()]); // CodeableConcept
        case 96511: /*age*/ return this.age == null ? new Base[0] : new Base[] {this.age}; // Range
        case -241217538: /*gestationalAge*/ return this.gestationalAge == null ? new Base[0] : new Base[] {this.gestationalAge}; // Range
        case -861311717: /*condition*/ return this.condition == null ? new Base[0] : new Base[] {this.condition}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 50511102: // category
          this.category = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 108280125: // range
          this.range = castToRange(value); // Range
          return value;
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -2089924569: // appliesTo
          this.getAppliesTo().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 96511: // age
          this.age = castToRange(value); // Range
          return value;
        case -241217538: // gestationalAge
          this.gestationalAge = castToRange(value); // Range
          return value;
        case -861311717: // condition
          this.condition = castToString(value); // StringType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("category")) {
          this.category = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("range")) {
          this.range = castToRange(value); // Range
        } else if (name.equals("type")) {
          this.type = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("appliesTo")) {
          this.getAppliesTo().add(castToCodeableConcept(value));
        } else if (name.equals("age")) {
          this.age = castToRange(value); // Range
        } else if (name.equals("gestationalAge")) {
          this.gestationalAge = castToRange(value); // Range
        } else if (name.equals("condition")) {
          this.condition = castToString(value); // StringType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 50511102:  return getCategory(); 
        case 108280125:  return getRange(); 
        case 3575610:  return getType(); 
        case -2089924569:  return addAppliesTo(); 
        case 96511:  return getAge(); 
        case -241217538:  return getGestationalAge(); 
        case -861311717:  return getConditionElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 50511102: /*category*/ return new String[] {"CodeableConcept"};
        case 108280125: /*range*/ return new String[] {"Range"};
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case -2089924569: /*appliesTo*/ return new String[] {"CodeableConcept"};
        case 96511: /*age*/ return new String[] {"Range"};
        case -241217538: /*gestationalAge*/ return new String[] {"Range"};
        case -861311717: /*condition*/ return new String[] {"string"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("category")) {
          this.category = new CodeableConcept();
          return this.category;
        }
        else if (name.equals("range")) {
          this.range = new Range();
          return this.range;
        }
        else if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("appliesTo")) {
          return addAppliesTo();
        }
        else if (name.equals("age")) {
          this.age = new Range();
          return this.age;
        }
        else if (name.equals("gestationalAge")) {
          this.gestationalAge = new Range();
          return this.gestationalAge;
        }
        else if (name.equals("condition")) {
          throw new FHIRException("Cannot call addChild on a primitive type ObservationDefinition.condition");
        }
        else
          return super.addChild(name);
      }

      public ObservationDefinitionQualifiedIntervalComponent copy() {
        ObservationDefinitionQualifiedIntervalComponent dst = new ObservationDefinitionQualifiedIntervalComponent();
        copyValues(dst);
        dst.category = category == null ? null : category.copy();
        dst.range = range == null ? null : range.copy();
        dst.type = type == null ? null : type.copy();
        if (appliesTo != null) {
          dst.appliesTo = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : appliesTo)
            dst.appliesTo.add(i.copy());
        };
        dst.age = age == null ? null : age.copy();
        dst.gestationalAge = gestationalAge == null ? null : gestationalAge.copy();
        dst.condition = condition == null ? null : condition.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ObservationDefinitionQualifiedIntervalComponent))
          return false;
        ObservationDefinitionQualifiedIntervalComponent o = (ObservationDefinitionQualifiedIntervalComponent) other_;
        return compareDeep(category, o.category, true) && compareDeep(range, o.range, true) && compareDeep(type, o.type, true)
           && compareDeep(appliesTo, o.appliesTo, true) && compareDeep(age, o.age, true) && compareDeep(gestationalAge, o.gestationalAge, true)
           && compareDeep(condition, o.condition, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ObservationDefinitionQualifiedIntervalComponent))
          return false;
        ObservationDefinitionQualifiedIntervalComponent o = (ObservationDefinitionQualifiedIntervalComponent) other_;
        return compareValues(condition, o.condition, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(category, range, type, appliesTo
          , age, gestationalAge, condition);
      }

  public String fhirType() {
    return "ObservationDefinition.qualifiedInterval";

  }

  }

    /**
     * A code that classifies the general type of observation.
     */
    @Child(name = "category", type = {Coding.class}, order=0, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Category of observation", formalDefinition="A code that classifies the general type of observation." )
    protected Coding category;

    /**
     * Describes what will be observed. Sometimes this is called the observation "name".
     */
    @Child(name = "code", type = {Coding.class}, order=1, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Type of observation (code / type)", formalDefinition="Describes what will be observed. Sometimes this is called the observation \"name\"." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/observation-codes")
    protected Coding code;

    /**
     * Data type allowed for the result of the observation.
     */
    @Child(name = "permittedDataType", type = {Coding.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Permitted data type for observation value", formalDefinition="Data type allowed for the result of the observation." )
    protected List<Coding> permittedDataType;

    /**
     * Multiple results allowed for this kind of observation.
     */
    @Child(name = "multipleResultsAllowed", type = {BooleanType.class}, order=3, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Multiple results allowed", formalDefinition="Multiple results allowed for this kind of observation." )
    protected BooleanType multipleResultsAllowed;

    /**
     * The method or technique used to perform the observation.
     */
    @Child(name = "method", type = {CodeableConcept.class}, order=4, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="The method or technique used to perform the observation", formalDefinition="The method or technique used to perform the observation." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/observation-methods")
    protected CodeableConcept method;

    /**
     * The preferred name to be used when reporting the results of this observation.
     */
    @Child(name = "preferredReportName", type = {StringType.class}, order=5, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Preferred report name", formalDefinition="The preferred name to be used when reporting the results of this observation." )
    protected StringType preferredReportName;

    /**
     * Characteristics for quantitative results of this observation.
     */
    @Child(name = "quantitativeDetails", type = {}, order=6, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Characteristics of quantitative results", formalDefinition="Characteristics for quantitative results of this observation." )
    protected ObservationDefinitionQuantitativeDetailsComponent quantitativeDetails;

    /**
     * Reference range for ordinal and continuous observations.
     */
    @Child(name = "qualifiedInterval", type = {}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Reference range for observation result", formalDefinition="Reference range for ordinal and continuous observations." )
    protected List<ObservationDefinitionQualifiedIntervalComponent> qualifiedInterval;

    /**
     * The set of valid coded results for the observation.
     */
    @Child(name = "validCodedValueSet", type = {UriType.class}, order=8, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Value set of valid coded values for the observation", formalDefinition="The set of valid coded results for the observation." )
    protected UriType validCodedValueSet;

    /**
     * The set of normal coded results for the observation.
     */
    @Child(name = "normalCodedValueSet", type = {UriType.class}, order=9, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Value set of normal coded values for the observation", formalDefinition="The set of normal coded results for the observation." )
    protected UriType normalCodedValueSet;

    /**
     * The set of abnormal coded results for the observation.
     */
    @Child(name = "abnormalCodedValueSet", type = {UriType.class}, order=10, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Value set of abnormal coded values for the observation", formalDefinition="The set of abnormal coded results for the observation." )
    protected UriType abnormalCodedValueSet;

    /**
     * The set of critical coded results for the observation.
     */
    @Child(name = "criticalCodedValueSet", type = {UriType.class}, order=11, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Value set of critical coded values for the observation", formalDefinition="The set of critical coded results for the observation." )
    protected UriType criticalCodedValueSet;

    private static final long serialVersionUID = 1678075048L;

  /**
   * Constructor
   */
    public ObservationDefinition() {
      super();
    }

  /**
   * Constructor
   */
    public ObservationDefinition(Coding code) {
      super();
      this.code = code;
    }

    /**
     * @return {@link #category} (A code that classifies the general type of observation.)
     */
    public Coding getCategory() { 
      if (this.category == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ObservationDefinition.category");
        else if (Configuration.doAutoCreate())
          this.category = new Coding(); // cc
      return this.category;
    }

    public boolean hasCategory() { 
      return this.category != null && !this.category.isEmpty();
    }

    /**
     * @param value {@link #category} (A code that classifies the general type of observation.)
     */
    public ObservationDefinition setCategory(Coding value) { 
      this.category = value;
      return this;
    }

    /**
     * @return {@link #code} (Describes what will be observed. Sometimes this is called the observation "name".)
     */
    public Coding getCode() { 
      if (this.code == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ObservationDefinition.code");
        else if (Configuration.doAutoCreate())
          this.code = new Coding(); // cc
      return this.code;
    }

    public boolean hasCode() { 
      return this.code != null && !this.code.isEmpty();
    }

    /**
     * @param value {@link #code} (Describes what will be observed. Sometimes this is called the observation "name".)
     */
    public ObservationDefinition setCode(Coding value) { 
      this.code = value;
      return this;
    }

    /**
     * @return {@link #permittedDataType} (Data type allowed for the result of the observation.)
     */
    public List<Coding> getPermittedDataType() { 
      if (this.permittedDataType == null)
        this.permittedDataType = new ArrayList<Coding>();
      return this.permittedDataType;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ObservationDefinition setPermittedDataType(List<Coding> thePermittedDataType) { 
      this.permittedDataType = thePermittedDataType;
      return this;
    }

    public boolean hasPermittedDataType() { 
      if (this.permittedDataType == null)
        return false;
      for (Coding item : this.permittedDataType)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Coding addPermittedDataType() { //3
      Coding t = new Coding();
      if (this.permittedDataType == null)
        this.permittedDataType = new ArrayList<Coding>();
      this.permittedDataType.add(t);
      return t;
    }

    public ObservationDefinition addPermittedDataType(Coding t) { //3
      if (t == null)
        return this;
      if (this.permittedDataType == null)
        this.permittedDataType = new ArrayList<Coding>();
      this.permittedDataType.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #permittedDataType}, creating it if it does not already exist
     */
    public Coding getPermittedDataTypeFirstRep() { 
      if (getPermittedDataType().isEmpty()) {
        addPermittedDataType();
      }
      return getPermittedDataType().get(0);
    }

    /**
     * @return {@link #multipleResultsAllowed} (Multiple results allowed for this kind of observation.). This is the underlying object with id, value and extensions. The accessor "getMultipleResultsAllowed" gives direct access to the value
     */
    public BooleanType getMultipleResultsAllowedElement() { 
      if (this.multipleResultsAllowed == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ObservationDefinition.multipleResultsAllowed");
        else if (Configuration.doAutoCreate())
          this.multipleResultsAllowed = new BooleanType(); // bb
      return this.multipleResultsAllowed;
    }

    public boolean hasMultipleResultsAllowedElement() { 
      return this.multipleResultsAllowed != null && !this.multipleResultsAllowed.isEmpty();
    }

    public boolean hasMultipleResultsAllowed() { 
      return this.multipleResultsAllowed != null && !this.multipleResultsAllowed.isEmpty();
    }

    /**
     * @param value {@link #multipleResultsAllowed} (Multiple results allowed for this kind of observation.). This is the underlying object with id, value and extensions. The accessor "getMultipleResultsAllowed" gives direct access to the value
     */
    public ObservationDefinition setMultipleResultsAllowedElement(BooleanType value) { 
      this.multipleResultsAllowed = value;
      return this;
    }

    /**
     * @return Multiple results allowed for this kind of observation.
     */
    public boolean getMultipleResultsAllowed() { 
      return this.multipleResultsAllowed == null || this.multipleResultsAllowed.isEmpty() ? false : this.multipleResultsAllowed.getValue();
    }

    /**
     * @param value Multiple results allowed for this kind of observation.
     */
    public ObservationDefinition setMultipleResultsAllowed(boolean value) { 
        if (this.multipleResultsAllowed == null)
          this.multipleResultsAllowed = new BooleanType();
        this.multipleResultsAllowed.setValue(value);
      return this;
    }

    /**
     * @return {@link #method} (The method or technique used to perform the observation.)
     */
    public CodeableConcept getMethod() { 
      if (this.method == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ObservationDefinition.method");
        else if (Configuration.doAutoCreate())
          this.method = new CodeableConcept(); // cc
      return this.method;
    }

    public boolean hasMethod() { 
      return this.method != null && !this.method.isEmpty();
    }

    /**
     * @param value {@link #method} (The method or technique used to perform the observation.)
     */
    public ObservationDefinition setMethod(CodeableConcept value) { 
      this.method = value;
      return this;
    }

    /**
     * @return {@link #preferredReportName} (The preferred name to be used when reporting the results of this observation.). This is the underlying object with id, value and extensions. The accessor "getPreferredReportName" gives direct access to the value
     */
    public StringType getPreferredReportNameElement() { 
      if (this.preferredReportName == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ObservationDefinition.preferredReportName");
        else if (Configuration.doAutoCreate())
          this.preferredReportName = new StringType(); // bb
      return this.preferredReportName;
    }

    public boolean hasPreferredReportNameElement() { 
      return this.preferredReportName != null && !this.preferredReportName.isEmpty();
    }

    public boolean hasPreferredReportName() { 
      return this.preferredReportName != null && !this.preferredReportName.isEmpty();
    }

    /**
     * @param value {@link #preferredReportName} (The preferred name to be used when reporting the results of this observation.). This is the underlying object with id, value and extensions. The accessor "getPreferredReportName" gives direct access to the value
     */
    public ObservationDefinition setPreferredReportNameElement(StringType value) { 
      this.preferredReportName = value;
      return this;
    }

    /**
     * @return The preferred name to be used when reporting the results of this observation.
     */
    public String getPreferredReportName() { 
      return this.preferredReportName == null ? null : this.preferredReportName.getValue();
    }

    /**
     * @param value The preferred name to be used when reporting the results of this observation.
     */
    public ObservationDefinition setPreferredReportName(String value) { 
      if (Utilities.noString(value))
        this.preferredReportName = null;
      else {
        if (this.preferredReportName == null)
          this.preferredReportName = new StringType();
        this.preferredReportName.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #quantitativeDetails} (Characteristics for quantitative results of this observation.)
     */
    public ObservationDefinitionQuantitativeDetailsComponent getQuantitativeDetails() { 
      if (this.quantitativeDetails == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ObservationDefinition.quantitativeDetails");
        else if (Configuration.doAutoCreate())
          this.quantitativeDetails = new ObservationDefinitionQuantitativeDetailsComponent(); // cc
      return this.quantitativeDetails;
    }

    public boolean hasQuantitativeDetails() { 
      return this.quantitativeDetails != null && !this.quantitativeDetails.isEmpty();
    }

    /**
     * @param value {@link #quantitativeDetails} (Characteristics for quantitative results of this observation.)
     */
    public ObservationDefinition setQuantitativeDetails(ObservationDefinitionQuantitativeDetailsComponent value) { 
      this.quantitativeDetails = value;
      return this;
    }

    /**
     * @return {@link #qualifiedInterval} (Reference range for ordinal and continuous observations.)
     */
    public List<ObservationDefinitionQualifiedIntervalComponent> getQualifiedInterval() { 
      if (this.qualifiedInterval == null)
        this.qualifiedInterval = new ArrayList<ObservationDefinitionQualifiedIntervalComponent>();
      return this.qualifiedInterval;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ObservationDefinition setQualifiedInterval(List<ObservationDefinitionQualifiedIntervalComponent> theQualifiedInterval) { 
      this.qualifiedInterval = theQualifiedInterval;
      return this;
    }

    public boolean hasQualifiedInterval() { 
      if (this.qualifiedInterval == null)
        return false;
      for (ObservationDefinitionQualifiedIntervalComponent item : this.qualifiedInterval)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public ObservationDefinitionQualifiedIntervalComponent addQualifiedInterval() { //3
      ObservationDefinitionQualifiedIntervalComponent t = new ObservationDefinitionQualifiedIntervalComponent();
      if (this.qualifiedInterval == null)
        this.qualifiedInterval = new ArrayList<ObservationDefinitionQualifiedIntervalComponent>();
      this.qualifiedInterval.add(t);
      return t;
    }

    public ObservationDefinition addQualifiedInterval(ObservationDefinitionQualifiedIntervalComponent t) { //3
      if (t == null)
        return this;
      if (this.qualifiedInterval == null)
        this.qualifiedInterval = new ArrayList<ObservationDefinitionQualifiedIntervalComponent>();
      this.qualifiedInterval.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #qualifiedInterval}, creating it if it does not already exist
     */
    public ObservationDefinitionQualifiedIntervalComponent getQualifiedIntervalFirstRep() { 
      if (getQualifiedInterval().isEmpty()) {
        addQualifiedInterval();
      }
      return getQualifiedInterval().get(0);
    }

    /**
     * @return {@link #validCodedValueSet} (The set of valid coded results for the observation.). This is the underlying object with id, value and extensions. The accessor "getValidCodedValueSet" gives direct access to the value
     */
    public UriType getValidCodedValueSetElement() { 
      if (this.validCodedValueSet == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ObservationDefinition.validCodedValueSet");
        else if (Configuration.doAutoCreate())
          this.validCodedValueSet = new UriType(); // bb
      return this.validCodedValueSet;
    }

    public boolean hasValidCodedValueSetElement() { 
      return this.validCodedValueSet != null && !this.validCodedValueSet.isEmpty();
    }

    public boolean hasValidCodedValueSet() { 
      return this.validCodedValueSet != null && !this.validCodedValueSet.isEmpty();
    }

    /**
     * @param value {@link #validCodedValueSet} (The set of valid coded results for the observation.). This is the underlying object with id, value and extensions. The accessor "getValidCodedValueSet" gives direct access to the value
     */
    public ObservationDefinition setValidCodedValueSetElement(UriType value) { 
      this.validCodedValueSet = value;
      return this;
    }

    /**
     * @return The set of valid coded results for the observation.
     */
    public String getValidCodedValueSet() { 
      return this.validCodedValueSet == null ? null : this.validCodedValueSet.getValue();
    }

    /**
     * @param value The set of valid coded results for the observation.
     */
    public ObservationDefinition setValidCodedValueSet(String value) { 
      if (Utilities.noString(value))
        this.validCodedValueSet = null;
      else {
        if (this.validCodedValueSet == null)
          this.validCodedValueSet = new UriType();
        this.validCodedValueSet.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #normalCodedValueSet} (The set of normal coded results for the observation.). This is the underlying object with id, value and extensions. The accessor "getNormalCodedValueSet" gives direct access to the value
     */
    public UriType getNormalCodedValueSetElement() { 
      if (this.normalCodedValueSet == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ObservationDefinition.normalCodedValueSet");
        else if (Configuration.doAutoCreate())
          this.normalCodedValueSet = new UriType(); // bb
      return this.normalCodedValueSet;
    }

    public boolean hasNormalCodedValueSetElement() { 
      return this.normalCodedValueSet != null && !this.normalCodedValueSet.isEmpty();
    }

    public boolean hasNormalCodedValueSet() { 
      return this.normalCodedValueSet != null && !this.normalCodedValueSet.isEmpty();
    }

    /**
     * @param value {@link #normalCodedValueSet} (The set of normal coded results for the observation.). This is the underlying object with id, value and extensions. The accessor "getNormalCodedValueSet" gives direct access to the value
     */
    public ObservationDefinition setNormalCodedValueSetElement(UriType value) { 
      this.normalCodedValueSet = value;
      return this;
    }

    /**
     * @return The set of normal coded results for the observation.
     */
    public String getNormalCodedValueSet() { 
      return this.normalCodedValueSet == null ? null : this.normalCodedValueSet.getValue();
    }

    /**
     * @param value The set of normal coded results for the observation.
     */
    public ObservationDefinition setNormalCodedValueSet(String value) { 
      if (Utilities.noString(value))
        this.normalCodedValueSet = null;
      else {
        if (this.normalCodedValueSet == null)
          this.normalCodedValueSet = new UriType();
        this.normalCodedValueSet.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #abnormalCodedValueSet} (The set of abnormal coded results for the observation.). This is the underlying object with id, value and extensions. The accessor "getAbnormalCodedValueSet" gives direct access to the value
     */
    public UriType getAbnormalCodedValueSetElement() { 
      if (this.abnormalCodedValueSet == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ObservationDefinition.abnormalCodedValueSet");
        else if (Configuration.doAutoCreate())
          this.abnormalCodedValueSet = new UriType(); // bb
      return this.abnormalCodedValueSet;
    }

    public boolean hasAbnormalCodedValueSetElement() { 
      return this.abnormalCodedValueSet != null && !this.abnormalCodedValueSet.isEmpty();
    }

    public boolean hasAbnormalCodedValueSet() { 
      return this.abnormalCodedValueSet != null && !this.abnormalCodedValueSet.isEmpty();
    }

    /**
     * @param value {@link #abnormalCodedValueSet} (The set of abnormal coded results for the observation.). This is the underlying object with id, value and extensions. The accessor "getAbnormalCodedValueSet" gives direct access to the value
     */
    public ObservationDefinition setAbnormalCodedValueSetElement(UriType value) { 
      this.abnormalCodedValueSet = value;
      return this;
    }

    /**
     * @return The set of abnormal coded results for the observation.
     */
    public String getAbnormalCodedValueSet() { 
      return this.abnormalCodedValueSet == null ? null : this.abnormalCodedValueSet.getValue();
    }

    /**
     * @param value The set of abnormal coded results for the observation.
     */
    public ObservationDefinition setAbnormalCodedValueSet(String value) { 
      if (Utilities.noString(value))
        this.abnormalCodedValueSet = null;
      else {
        if (this.abnormalCodedValueSet == null)
          this.abnormalCodedValueSet = new UriType();
        this.abnormalCodedValueSet.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #criticalCodedValueSet} (The set of critical coded results for the observation.). This is the underlying object with id, value and extensions. The accessor "getCriticalCodedValueSet" gives direct access to the value
     */
    public UriType getCriticalCodedValueSetElement() { 
      if (this.criticalCodedValueSet == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ObservationDefinition.criticalCodedValueSet");
        else if (Configuration.doAutoCreate())
          this.criticalCodedValueSet = new UriType(); // bb
      return this.criticalCodedValueSet;
    }

    public boolean hasCriticalCodedValueSetElement() { 
      return this.criticalCodedValueSet != null && !this.criticalCodedValueSet.isEmpty();
    }

    public boolean hasCriticalCodedValueSet() { 
      return this.criticalCodedValueSet != null && !this.criticalCodedValueSet.isEmpty();
    }

    /**
     * @param value {@link #criticalCodedValueSet} (The set of critical coded results for the observation.). This is the underlying object with id, value and extensions. The accessor "getCriticalCodedValueSet" gives direct access to the value
     */
    public ObservationDefinition setCriticalCodedValueSetElement(UriType value) { 
      this.criticalCodedValueSet = value;
      return this;
    }

    /**
     * @return The set of critical coded results for the observation.
     */
    public String getCriticalCodedValueSet() { 
      return this.criticalCodedValueSet == null ? null : this.criticalCodedValueSet.getValue();
    }

    /**
     * @param value The set of critical coded results for the observation.
     */
    public ObservationDefinition setCriticalCodedValueSet(String value) { 
      if (Utilities.noString(value))
        this.criticalCodedValueSet = null;
      else {
        if (this.criticalCodedValueSet == null)
          this.criticalCodedValueSet = new UriType();
        this.criticalCodedValueSet.setValue(value);
      }
      return this;
    }

      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("category", "Coding", "A code that classifies the general type of observation.", 0, 1, category));
        children.add(new Property("code", "Coding", "Describes what will be observed. Sometimes this is called the observation \"name\".", 0, 1, code));
        children.add(new Property("permittedDataType", "Coding", "Data type allowed for the result of the observation.", 0, java.lang.Integer.MAX_VALUE, permittedDataType));
        children.add(new Property("multipleResultsAllowed", "boolean", "Multiple results allowed for this kind of observation.", 0, 1, multipleResultsAllowed));
        children.add(new Property("method", "CodeableConcept", "The method or technique used to perform the observation.", 0, 1, method));
        children.add(new Property("preferredReportName", "string", "The preferred name to be used when reporting the results of this observation.", 0, 1, preferredReportName));
        children.add(new Property("quantitativeDetails", "", "Characteristics for quantitative results of this observation.", 0, 1, quantitativeDetails));
        children.add(new Property("qualifiedInterval", "", "Reference range for ordinal and continuous observations.", 0, java.lang.Integer.MAX_VALUE, qualifiedInterval));
        children.add(new Property("validCodedValueSet", "uri", "The set of valid coded results for the observation.", 0, 1, validCodedValueSet));
        children.add(new Property("normalCodedValueSet", "uri", "The set of normal coded results for the observation.", 0, 1, normalCodedValueSet));
        children.add(new Property("abnormalCodedValueSet", "uri", "The set of abnormal coded results for the observation.", 0, 1, abnormalCodedValueSet));
        children.add(new Property("criticalCodedValueSet", "uri", "The set of critical coded results for the observation.", 0, 1, criticalCodedValueSet));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case 50511102: /*category*/  return new Property("category", "Coding", "A code that classifies the general type of observation.", 0, 1, category);
        case 3059181: /*code*/  return new Property("code", "Coding", "Describes what will be observed. Sometimes this is called the observation \"name\".", 0, 1, code);
        case -99492804: /*permittedDataType*/  return new Property("permittedDataType", "Coding", "Data type allowed for the result of the observation.", 0, java.lang.Integer.MAX_VALUE, permittedDataType);
        case -2102414590: /*multipleResultsAllowed*/  return new Property("multipleResultsAllowed", "boolean", "Multiple results allowed for this kind of observation.", 0, 1, multipleResultsAllowed);
        case -1077554975: /*method*/  return new Property("method", "CodeableConcept", "The method or technique used to perform the observation.", 0, 1, method);
        case -1851030208: /*preferredReportName*/  return new Property("preferredReportName", "string", "The preferred name to be used when reporting the results of this observation.", 0, 1, preferredReportName);
        case 842150763: /*quantitativeDetails*/  return new Property("quantitativeDetails", "", "Characteristics for quantitative results of this observation.", 0, 1, quantitativeDetails);
        case 1882971521: /*qualifiedInterval*/  return new Property("qualifiedInterval", "", "Reference range for ordinal and continuous observations.", 0, java.lang.Integer.MAX_VALUE, qualifiedInterval);
        case 1374640076: /*validCodedValueSet*/  return new Property("validCodedValueSet", "uri", "The set of valid coded results for the observation.", 0, 1, validCodedValueSet);
        case -837500735: /*normalCodedValueSet*/  return new Property("normalCodedValueSet", "uri", "The set of normal coded results for the observation.", 0, 1, normalCodedValueSet);
        case 1073600256: /*abnormalCodedValueSet*/  return new Property("abnormalCodedValueSet", "uri", "The set of abnormal coded results for the observation.", 0, 1, abnormalCodedValueSet);
        case 2568457: /*criticalCodedValueSet*/  return new Property("criticalCodedValueSet", "uri", "The set of critical coded results for the observation.", 0, 1, criticalCodedValueSet);
        default: return super.getNamedProperty(_hash, _name, _checkValid);
        }

      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 50511102: /*category*/ return this.category == null ? new Base[0] : new Base[] {this.category}; // Coding
        case 3059181: /*code*/ return this.code == null ? new Base[0] : new Base[] {this.code}; // Coding
        case -99492804: /*permittedDataType*/ return this.permittedDataType == null ? new Base[0] : this.permittedDataType.toArray(new Base[this.permittedDataType.size()]); // Coding
        case -2102414590: /*multipleResultsAllowed*/ return this.multipleResultsAllowed == null ? new Base[0] : new Base[] {this.multipleResultsAllowed}; // BooleanType
        case -1077554975: /*method*/ return this.method == null ? new Base[0] : new Base[] {this.method}; // CodeableConcept
        case -1851030208: /*preferredReportName*/ return this.preferredReportName == null ? new Base[0] : new Base[] {this.preferredReportName}; // StringType
        case 842150763: /*quantitativeDetails*/ return this.quantitativeDetails == null ? new Base[0] : new Base[] {this.quantitativeDetails}; // ObservationDefinitionQuantitativeDetailsComponent
        case 1882971521: /*qualifiedInterval*/ return this.qualifiedInterval == null ? new Base[0] : this.qualifiedInterval.toArray(new Base[this.qualifiedInterval.size()]); // ObservationDefinitionQualifiedIntervalComponent
        case 1374640076: /*validCodedValueSet*/ return this.validCodedValueSet == null ? new Base[0] : new Base[] {this.validCodedValueSet}; // UriType
        case -837500735: /*normalCodedValueSet*/ return this.normalCodedValueSet == null ? new Base[0] : new Base[] {this.normalCodedValueSet}; // UriType
        case 1073600256: /*abnormalCodedValueSet*/ return this.abnormalCodedValueSet == null ? new Base[0] : new Base[] {this.abnormalCodedValueSet}; // UriType
        case 2568457: /*criticalCodedValueSet*/ return this.criticalCodedValueSet == null ? new Base[0] : new Base[] {this.criticalCodedValueSet}; // UriType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 50511102: // category
          this.category = castToCoding(value); // Coding
          return value;
        case 3059181: // code
          this.code = castToCoding(value); // Coding
          return value;
        case -99492804: // permittedDataType
          this.getPermittedDataType().add(castToCoding(value)); // Coding
          return value;
        case -2102414590: // multipleResultsAllowed
          this.multipleResultsAllowed = castToBoolean(value); // BooleanType
          return value;
        case -1077554975: // method
          this.method = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1851030208: // preferredReportName
          this.preferredReportName = castToString(value); // StringType
          return value;
        case 842150763: // quantitativeDetails
          this.quantitativeDetails = (ObservationDefinitionQuantitativeDetailsComponent) value; // ObservationDefinitionQuantitativeDetailsComponent
          return value;
        case 1882971521: // qualifiedInterval
          this.getQualifiedInterval().add((ObservationDefinitionQualifiedIntervalComponent) value); // ObservationDefinitionQualifiedIntervalComponent
          return value;
        case 1374640076: // validCodedValueSet
          this.validCodedValueSet = castToUri(value); // UriType
          return value;
        case -837500735: // normalCodedValueSet
          this.normalCodedValueSet = castToUri(value); // UriType
          return value;
        case 1073600256: // abnormalCodedValueSet
          this.abnormalCodedValueSet = castToUri(value); // UriType
          return value;
        case 2568457: // criticalCodedValueSet
          this.criticalCodedValueSet = castToUri(value); // UriType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("category")) {
          this.category = castToCoding(value); // Coding
        } else if (name.equals("code")) {
          this.code = castToCoding(value); // Coding
        } else if (name.equals("permittedDataType")) {
          this.getPermittedDataType().add(castToCoding(value));
        } else if (name.equals("multipleResultsAllowed")) {
          this.multipleResultsAllowed = castToBoolean(value); // BooleanType
        } else if (name.equals("method")) {
          this.method = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("preferredReportName")) {
          this.preferredReportName = castToString(value); // StringType
        } else if (name.equals("quantitativeDetails")) {
          this.quantitativeDetails = (ObservationDefinitionQuantitativeDetailsComponent) value; // ObservationDefinitionQuantitativeDetailsComponent
        } else if (name.equals("qualifiedInterval")) {
          this.getQualifiedInterval().add((ObservationDefinitionQualifiedIntervalComponent) value);
        } else if (name.equals("validCodedValueSet")) {
          this.validCodedValueSet = castToUri(value); // UriType
        } else if (name.equals("normalCodedValueSet")) {
          this.normalCodedValueSet = castToUri(value); // UriType
        } else if (name.equals("abnormalCodedValueSet")) {
          this.abnormalCodedValueSet = castToUri(value); // UriType
        } else if (name.equals("criticalCodedValueSet")) {
          this.criticalCodedValueSet = castToUri(value); // UriType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 50511102:  return getCategory(); 
        case 3059181:  return getCode(); 
        case -99492804:  return addPermittedDataType(); 
        case -2102414590:  return getMultipleResultsAllowedElement();
        case -1077554975:  return getMethod(); 
        case -1851030208:  return getPreferredReportNameElement();
        case 842150763:  return getQuantitativeDetails(); 
        case 1882971521:  return addQualifiedInterval(); 
        case 1374640076:  return getValidCodedValueSetElement();
        case -837500735:  return getNormalCodedValueSetElement();
        case 1073600256:  return getAbnormalCodedValueSetElement();
        case 2568457:  return getCriticalCodedValueSetElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 50511102: /*category*/ return new String[] {"Coding"};
        case 3059181: /*code*/ return new String[] {"Coding"};
        case -99492804: /*permittedDataType*/ return new String[] {"Coding"};
        case -2102414590: /*multipleResultsAllowed*/ return new String[] {"boolean"};
        case -1077554975: /*method*/ return new String[] {"CodeableConcept"};
        case -1851030208: /*preferredReportName*/ return new String[] {"string"};
        case 842150763: /*quantitativeDetails*/ return new String[] {};
        case 1882971521: /*qualifiedInterval*/ return new String[] {};
        case 1374640076: /*validCodedValueSet*/ return new String[] {"uri"};
        case -837500735: /*normalCodedValueSet*/ return new String[] {"uri"};
        case 1073600256: /*abnormalCodedValueSet*/ return new String[] {"uri"};
        case 2568457: /*criticalCodedValueSet*/ return new String[] {"uri"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("category")) {
          this.category = new Coding();
          return this.category;
        }
        else if (name.equals("code")) {
          this.code = new Coding();
          return this.code;
        }
        else if (name.equals("permittedDataType")) {
          return addPermittedDataType();
        }
        else if (name.equals("multipleResultsAllowed")) {
          throw new FHIRException("Cannot call addChild on a primitive type ObservationDefinition.multipleResultsAllowed");
        }
        else if (name.equals("method")) {
          this.method = new CodeableConcept();
          return this.method;
        }
        else if (name.equals("preferredReportName")) {
          throw new FHIRException("Cannot call addChild on a primitive type ObservationDefinition.preferredReportName");
        }
        else if (name.equals("quantitativeDetails")) {
          this.quantitativeDetails = new ObservationDefinitionQuantitativeDetailsComponent();
          return this.quantitativeDetails;
        }
        else if (name.equals("qualifiedInterval")) {
          return addQualifiedInterval();
        }
        else if (name.equals("validCodedValueSet")) {
          throw new FHIRException("Cannot call addChild on a primitive type ObservationDefinition.validCodedValueSet");
        }
        else if (name.equals("normalCodedValueSet")) {
          throw new FHIRException("Cannot call addChild on a primitive type ObservationDefinition.normalCodedValueSet");
        }
        else if (name.equals("abnormalCodedValueSet")) {
          throw new FHIRException("Cannot call addChild on a primitive type ObservationDefinition.abnormalCodedValueSet");
        }
        else if (name.equals("criticalCodedValueSet")) {
          throw new FHIRException("Cannot call addChild on a primitive type ObservationDefinition.criticalCodedValueSet");
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "ObservationDefinition";

  }

      public ObservationDefinition copy() {
        ObservationDefinition dst = new ObservationDefinition();
        copyValues(dst);
        dst.category = category == null ? null : category.copy();
        dst.code = code == null ? null : code.copy();
        if (permittedDataType != null) {
          dst.permittedDataType = new ArrayList<Coding>();
          for (Coding i : permittedDataType)
            dst.permittedDataType.add(i.copy());
        };
        dst.multipleResultsAllowed = multipleResultsAllowed == null ? null : multipleResultsAllowed.copy();
        dst.method = method == null ? null : method.copy();
        dst.preferredReportName = preferredReportName == null ? null : preferredReportName.copy();
        dst.quantitativeDetails = quantitativeDetails == null ? null : quantitativeDetails.copy();
        if (qualifiedInterval != null) {
          dst.qualifiedInterval = new ArrayList<ObservationDefinitionQualifiedIntervalComponent>();
          for (ObservationDefinitionQualifiedIntervalComponent i : qualifiedInterval)
            dst.qualifiedInterval.add(i.copy());
        };
        dst.validCodedValueSet = validCodedValueSet == null ? null : validCodedValueSet.copy();
        dst.normalCodedValueSet = normalCodedValueSet == null ? null : normalCodedValueSet.copy();
        dst.abnormalCodedValueSet = abnormalCodedValueSet == null ? null : abnormalCodedValueSet.copy();
        dst.criticalCodedValueSet = criticalCodedValueSet == null ? null : criticalCodedValueSet.copy();
        return dst;
      }

      protected ObservationDefinition typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ObservationDefinition))
          return false;
        ObservationDefinition o = (ObservationDefinition) other_;
        return compareDeep(category, o.category, true) && compareDeep(code, o.code, true) && compareDeep(permittedDataType, o.permittedDataType, true)
           && compareDeep(multipleResultsAllowed, o.multipleResultsAllowed, true) && compareDeep(method, o.method, true)
           && compareDeep(preferredReportName, o.preferredReportName, true) && compareDeep(quantitativeDetails, o.quantitativeDetails, true)
           && compareDeep(qualifiedInterval, o.qualifiedInterval, true) && compareDeep(validCodedValueSet, o.validCodedValueSet, true)
           && compareDeep(normalCodedValueSet, o.normalCodedValueSet, true) && compareDeep(abnormalCodedValueSet, o.abnormalCodedValueSet, true)
           && compareDeep(criticalCodedValueSet, o.criticalCodedValueSet, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ObservationDefinition))
          return false;
        ObservationDefinition o = (ObservationDefinition) other_;
        return compareValues(multipleResultsAllowed, o.multipleResultsAllowed, true) && compareValues(preferredReportName, o.preferredReportName, true)
           && compareValues(validCodedValueSet, o.validCodedValueSet, true) && compareValues(normalCodedValueSet, o.normalCodedValueSet, true)
           && compareValues(abnormalCodedValueSet, o.abnormalCodedValueSet, true) && compareValues(criticalCodedValueSet, o.criticalCodedValueSet, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(category, code, permittedDataType
          , multipleResultsAllowed, method, preferredReportName, quantitativeDetails, qualifiedInterval
          , validCodedValueSet, normalCodedValueSet, abnormalCodedValueSet, criticalCodedValueSet
          );
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.ObservationDefinition;
   }


}

