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

import java.math.*;
import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.r4.model.Enumerations.*;
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
@ResourceDef(name="ObservationDefinition", profile="http://hl7.org/fhir/StructureDefinition/ObservationDefinition")
public class ObservationDefinition extends DomainResource {

    public enum ObservationDataType {
        /**
         * A measured amount.
         */
        QUANTITY, 
        /**
         * A coded concept from a reference terminology and/or text.
         */
        CODEABLECONCEPT, 
        /**
         * A sequence of Unicode characters.
         */
        STRING, 
        /**
         * true or false.
         */
        BOOLEAN, 
        /**
         * A signed integer.
         */
        INTEGER, 
        /**
         * A set of values bounded by low and high.
         */
        RANGE, 
        /**
         * A ratio of two Quantity values - a numerator and a denominator.
         */
        RATIO, 
        /**
         * A series of measurements taken by a device.
         */
        SAMPLEDDATA, 
        /**
         * A time during the day, in the format hh:mm:ss.
         */
        TIME, 
        /**
         * A date, date-time or partial date (e.g. just year or year + month) as used in human communication.
         */
        DATETIME, 
        /**
         * A time range defined by start and end date/time.
         */
        PERIOD, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static ObservationDataType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("Quantity".equals(codeString))
          return QUANTITY;
        if ("CodeableConcept".equals(codeString))
          return CODEABLECONCEPT;
        if ("string".equals(codeString))
          return STRING;
        if ("boolean".equals(codeString))
          return BOOLEAN;
        if ("integer".equals(codeString))
          return INTEGER;
        if ("Range".equals(codeString))
          return RANGE;
        if ("Ratio".equals(codeString))
          return RATIO;
        if ("SampledData".equals(codeString))
          return SAMPLEDDATA;
        if ("time".equals(codeString))
          return TIME;
        if ("dateTime".equals(codeString))
          return DATETIME;
        if ("Period".equals(codeString))
          return PERIOD;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown ObservationDataType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case QUANTITY: return "Quantity";
            case CODEABLECONCEPT: return "CodeableConcept";
            case STRING: return "string";
            case BOOLEAN: return "boolean";
            case INTEGER: return "integer";
            case RANGE: return "Range";
            case RATIO: return "Ratio";
            case SAMPLEDDATA: return "SampledData";
            case TIME: return "time";
            case DATETIME: return "dateTime";
            case PERIOD: return "Period";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case QUANTITY: return "http://hl7.org/fhir/permitted-data-type";
            case CODEABLECONCEPT: return "http://hl7.org/fhir/permitted-data-type";
            case STRING: return "http://hl7.org/fhir/permitted-data-type";
            case BOOLEAN: return "http://hl7.org/fhir/permitted-data-type";
            case INTEGER: return "http://hl7.org/fhir/permitted-data-type";
            case RANGE: return "http://hl7.org/fhir/permitted-data-type";
            case RATIO: return "http://hl7.org/fhir/permitted-data-type";
            case SAMPLEDDATA: return "http://hl7.org/fhir/permitted-data-type";
            case TIME: return "http://hl7.org/fhir/permitted-data-type";
            case DATETIME: return "http://hl7.org/fhir/permitted-data-type";
            case PERIOD: return "http://hl7.org/fhir/permitted-data-type";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case QUANTITY: return "A measured amount.";
            case CODEABLECONCEPT: return "A coded concept from a reference terminology and/or text.";
            case STRING: return "A sequence of Unicode characters.";
            case BOOLEAN: return "true or false.";
            case INTEGER: return "A signed integer.";
            case RANGE: return "A set of values bounded by low and high.";
            case RATIO: return "A ratio of two Quantity values - a numerator and a denominator.";
            case SAMPLEDDATA: return "A series of measurements taken by a device.";
            case TIME: return "A time during the day, in the format hh:mm:ss.";
            case DATETIME: return "A date, date-time or partial date (e.g. just year or year + month) as used in human communication.";
            case PERIOD: return "A time range defined by start and end date/time.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case QUANTITY: return "Quantity";
            case CODEABLECONCEPT: return "CodeableConcept";
            case STRING: return "string";
            case BOOLEAN: return "boolean";
            case INTEGER: return "integer";
            case RANGE: return "Range";
            case RATIO: return "Ratio";
            case SAMPLEDDATA: return "SampledData";
            case TIME: return "time";
            case DATETIME: return "dateTime";
            case PERIOD: return "Period";
            default: return "?";
          }
        }
    }

  public static class ObservationDataTypeEnumFactory implements EnumFactory<ObservationDataType> {
    public ObservationDataType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("Quantity".equals(codeString))
          return ObservationDataType.QUANTITY;
        if ("CodeableConcept".equals(codeString))
          return ObservationDataType.CODEABLECONCEPT;
        if ("string".equals(codeString))
          return ObservationDataType.STRING;
        if ("boolean".equals(codeString))
          return ObservationDataType.BOOLEAN;
        if ("integer".equals(codeString))
          return ObservationDataType.INTEGER;
        if ("Range".equals(codeString))
          return ObservationDataType.RANGE;
        if ("Ratio".equals(codeString))
          return ObservationDataType.RATIO;
        if ("SampledData".equals(codeString))
          return ObservationDataType.SAMPLEDDATA;
        if ("time".equals(codeString))
          return ObservationDataType.TIME;
        if ("dateTime".equals(codeString))
          return ObservationDataType.DATETIME;
        if ("Period".equals(codeString))
          return ObservationDataType.PERIOD;
        throw new IllegalArgumentException("Unknown ObservationDataType code '"+codeString+"'");
        }
        public Enumeration<ObservationDataType> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<ObservationDataType>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("Quantity".equals(codeString))
          return new Enumeration<ObservationDataType>(this, ObservationDataType.QUANTITY);
        if ("CodeableConcept".equals(codeString))
          return new Enumeration<ObservationDataType>(this, ObservationDataType.CODEABLECONCEPT);
        if ("string".equals(codeString))
          return new Enumeration<ObservationDataType>(this, ObservationDataType.STRING);
        if ("boolean".equals(codeString))
          return new Enumeration<ObservationDataType>(this, ObservationDataType.BOOLEAN);
        if ("integer".equals(codeString))
          return new Enumeration<ObservationDataType>(this, ObservationDataType.INTEGER);
        if ("Range".equals(codeString))
          return new Enumeration<ObservationDataType>(this, ObservationDataType.RANGE);
        if ("Ratio".equals(codeString))
          return new Enumeration<ObservationDataType>(this, ObservationDataType.RATIO);
        if ("SampledData".equals(codeString))
          return new Enumeration<ObservationDataType>(this, ObservationDataType.SAMPLEDDATA);
        if ("time".equals(codeString))
          return new Enumeration<ObservationDataType>(this, ObservationDataType.TIME);
        if ("dateTime".equals(codeString))
          return new Enumeration<ObservationDataType>(this, ObservationDataType.DATETIME);
        if ("Period".equals(codeString))
          return new Enumeration<ObservationDataType>(this, ObservationDataType.PERIOD);
        throw new FHIRException("Unknown ObservationDataType code '"+codeString+"'");
        }
    public String toCode(ObservationDataType code) {
      if (code == ObservationDataType.QUANTITY)
        return "Quantity";
      if (code == ObservationDataType.CODEABLECONCEPT)
        return "CodeableConcept";
      if (code == ObservationDataType.STRING)
        return "string";
      if (code == ObservationDataType.BOOLEAN)
        return "boolean";
      if (code == ObservationDataType.INTEGER)
        return "integer";
      if (code == ObservationDataType.RANGE)
        return "Range";
      if (code == ObservationDataType.RATIO)
        return "Ratio";
      if (code == ObservationDataType.SAMPLEDDATA)
        return "SampledData";
      if (code == ObservationDataType.TIME)
        return "time";
      if (code == ObservationDataType.DATETIME)
        return "dateTime";
      if (code == ObservationDataType.PERIOD)
        return "Period";
      return "?";
      }
    public String toSystem(ObservationDataType code) {
      return code.getSystem();
      }
    }

    public enum ObservationRangeCategory {
        /**
         * Reference (Normal) Range for Ordinal and Continuous Observations.
         */
        REFERENCE, 
        /**
         * Critical Range for Ordinal and Continuous Observations.
         */
        CRITICAL, 
        /**
         * Absolute Range for Ordinal and Continuous Observations. Results outside this range are not possible.
         */
        ABSOLUTE, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static ObservationRangeCategory fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("reference".equals(codeString))
          return REFERENCE;
        if ("critical".equals(codeString))
          return CRITICAL;
        if ("absolute".equals(codeString))
          return ABSOLUTE;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown ObservationRangeCategory code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case REFERENCE: return "reference";
            case CRITICAL: return "critical";
            case ABSOLUTE: return "absolute";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case REFERENCE: return "http://hl7.org/fhir/observation-range-category";
            case CRITICAL: return "http://hl7.org/fhir/observation-range-category";
            case ABSOLUTE: return "http://hl7.org/fhir/observation-range-category";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case REFERENCE: return "Reference (Normal) Range for Ordinal and Continuous Observations.";
            case CRITICAL: return "Critical Range for Ordinal and Continuous Observations.";
            case ABSOLUTE: return "Absolute Range for Ordinal and Continuous Observations. Results outside this range are not possible.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case REFERENCE: return "reference range";
            case CRITICAL: return "critical range";
            case ABSOLUTE: return "absolute range";
            default: return "?";
          }
        }
    }

  public static class ObservationRangeCategoryEnumFactory implements EnumFactory<ObservationRangeCategory> {
    public ObservationRangeCategory fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("reference".equals(codeString))
          return ObservationRangeCategory.REFERENCE;
        if ("critical".equals(codeString))
          return ObservationRangeCategory.CRITICAL;
        if ("absolute".equals(codeString))
          return ObservationRangeCategory.ABSOLUTE;
        throw new IllegalArgumentException("Unknown ObservationRangeCategory code '"+codeString+"'");
        }
        public Enumeration<ObservationRangeCategory> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<ObservationRangeCategory>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("reference".equals(codeString))
          return new Enumeration<ObservationRangeCategory>(this, ObservationRangeCategory.REFERENCE);
        if ("critical".equals(codeString))
          return new Enumeration<ObservationRangeCategory>(this, ObservationRangeCategory.CRITICAL);
        if ("absolute".equals(codeString))
          return new Enumeration<ObservationRangeCategory>(this, ObservationRangeCategory.ABSOLUTE);
        throw new FHIRException("Unknown ObservationRangeCategory code '"+codeString+"'");
        }
    public String toCode(ObservationRangeCategory code) {
      if (code == ObservationRangeCategory.REFERENCE)
        return "reference";
      if (code == ObservationRangeCategory.CRITICAL)
        return "critical";
      if (code == ObservationRangeCategory.ABSOLUTE)
        return "absolute";
      return "?";
      }
    public String toSystem(ObservationRangeCategory code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class ObservationDefinitionQuantitativeDetailsComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Customary unit used to report quantitative results of observations conforming to this ObservationDefinition.
         */
        @Child(name = "customaryUnit", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Customary unit for quantitative results", formalDefinition="Customary unit used to report quantitative results of observations conforming to this ObservationDefinition." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/ucum-units")
        protected CodeableConcept customaryUnit;

        /**
         * SI unit used to report quantitative results of observations conforming to this ObservationDefinition.
         */
        @Child(name = "unit", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="SI unit for quantitative results", formalDefinition="SI unit used to report quantitative results of observations conforming to this ObservationDefinition." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/ucum-units")
        protected CodeableConcept unit;

        /**
         * Factor for converting value expressed with SI unit to value expressed with customary unit.
         */
        @Child(name = "conversionFactor", type = {DecimalType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="SI to Customary unit conversion factor", formalDefinition="Factor for converting value expressed with SI unit to value expressed with customary unit." )
        protected DecimalType conversionFactor;

        /**
         * Number of digits after decimal separator when the results of such observations are of type Quantity.
         */
        @Child(name = "decimalPrecision", type = {IntegerType.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Decimal precision of observation quantitative results", formalDefinition="Number of digits after decimal separator when the results of such observations are of type Quantity." )
        protected IntegerType decimalPrecision;

        private static final long serialVersionUID = 1790019610L;

    /**
     * Constructor
     */
      public ObservationDefinitionQuantitativeDetailsComponent() {
        super();
      }

        /**
         * @return {@link #customaryUnit} (Customary unit used to report quantitative results of observations conforming to this ObservationDefinition.)
         */
        public CodeableConcept getCustomaryUnit() { 
          if (this.customaryUnit == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ObservationDefinitionQuantitativeDetailsComponent.customaryUnit");
            else if (Configuration.doAutoCreate())
              this.customaryUnit = new CodeableConcept(); // cc
          return this.customaryUnit;
        }

        public boolean hasCustomaryUnit() { 
          return this.customaryUnit != null && !this.customaryUnit.isEmpty();
        }

        /**
         * @param value {@link #customaryUnit} (Customary unit used to report quantitative results of observations conforming to this ObservationDefinition.)
         */
        public ObservationDefinitionQuantitativeDetailsComponent setCustomaryUnit(CodeableConcept value) { 
          this.customaryUnit = value;
          return this;
        }

        /**
         * @return {@link #unit} (SI unit used to report quantitative results of observations conforming to this ObservationDefinition.)
         */
        public CodeableConcept getUnit() { 
          if (this.unit == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ObservationDefinitionQuantitativeDetailsComponent.unit");
            else if (Configuration.doAutoCreate())
              this.unit = new CodeableConcept(); // cc
          return this.unit;
        }

        public boolean hasUnit() { 
          return this.unit != null && !this.unit.isEmpty();
        }

        /**
         * @param value {@link #unit} (SI unit used to report quantitative results of observations conforming to this ObservationDefinition.)
         */
        public ObservationDefinitionQuantitativeDetailsComponent setUnit(CodeableConcept value) { 
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
         * @return {@link #decimalPrecision} (Number of digits after decimal separator when the results of such observations are of type Quantity.). This is the underlying object with id, value and extensions. The accessor "getDecimalPrecision" gives direct access to the value
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
         * @param value {@link #decimalPrecision} (Number of digits after decimal separator when the results of such observations are of type Quantity.). This is the underlying object with id, value and extensions. The accessor "getDecimalPrecision" gives direct access to the value
         */
        public ObservationDefinitionQuantitativeDetailsComponent setDecimalPrecisionElement(IntegerType value) { 
          this.decimalPrecision = value;
          return this;
        }

        /**
         * @return Number of digits after decimal separator when the results of such observations are of type Quantity.
         */
        public int getDecimalPrecision() { 
          return this.decimalPrecision == null || this.decimalPrecision.isEmpty() ? 0 : this.decimalPrecision.getValue();
        }

        /**
         * @param value Number of digits after decimal separator when the results of such observations are of type Quantity.
         */
        public ObservationDefinitionQuantitativeDetailsComponent setDecimalPrecision(int value) { 
            if (this.decimalPrecision == null)
              this.decimalPrecision = new IntegerType();
            this.decimalPrecision.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("customaryUnit", "CodeableConcept", "Customary unit used to report quantitative results of observations conforming to this ObservationDefinition.", 0, 1, customaryUnit));
          children.add(new Property("unit", "CodeableConcept", "SI unit used to report quantitative results of observations conforming to this ObservationDefinition.", 0, 1, unit));
          children.add(new Property("conversionFactor", "decimal", "Factor for converting value expressed with SI unit to value expressed with customary unit.", 0, 1, conversionFactor));
          children.add(new Property("decimalPrecision", "integer", "Number of digits after decimal separator when the results of such observations are of type Quantity.", 0, 1, decimalPrecision));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -1375586437: /*customaryUnit*/  return new Property("customaryUnit", "CodeableConcept", "Customary unit used to report quantitative results of observations conforming to this ObservationDefinition.", 0, 1, customaryUnit);
          case 3594628: /*unit*/  return new Property("unit", "CodeableConcept", "SI unit used to report quantitative results of observations conforming to this ObservationDefinition.", 0, 1, unit);
          case 1438876165: /*conversionFactor*/  return new Property("conversionFactor", "decimal", "Factor for converting value expressed with SI unit to value expressed with customary unit.", 0, 1, conversionFactor);
          case -1564447699: /*decimalPrecision*/  return new Property("decimalPrecision", "integer", "Number of digits after decimal separator when the results of such observations are of type Quantity.", 0, 1, decimalPrecision);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1375586437: /*customaryUnit*/ return this.customaryUnit == null ? new Base[0] : new Base[] {this.customaryUnit}; // CodeableConcept
        case 3594628: /*unit*/ return this.unit == null ? new Base[0] : new Base[] {this.unit}; // CodeableConcept
        case 1438876165: /*conversionFactor*/ return this.conversionFactor == null ? new Base[0] : new Base[] {this.conversionFactor}; // DecimalType
        case -1564447699: /*decimalPrecision*/ return this.decimalPrecision == null ? new Base[0] : new Base[] {this.decimalPrecision}; // IntegerType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1375586437: // customaryUnit
          this.customaryUnit = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 3594628: // unit
          this.unit = castToCodeableConcept(value); // CodeableConcept
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
          this.customaryUnit = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("unit")) {
          this.unit = castToCodeableConcept(value); // CodeableConcept
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
        case -1375586437: /*customaryUnit*/ return new String[] {"CodeableConcept"};
        case 3594628: /*unit*/ return new String[] {"CodeableConcept"};
        case 1438876165: /*conversionFactor*/ return new String[] {"decimal"};
        case -1564447699: /*decimalPrecision*/ return new String[] {"integer"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("customaryUnit")) {
          this.customaryUnit = new CodeableConcept();
          return this.customaryUnit;
        }
        else if (name.equals("unit")) {
          this.unit = new CodeableConcept();
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
         * The category of interval of values for continuous or ordinal observations conforming to this ObservationDefinition.
         */
        @Child(name = "category", type = {CodeType.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="reference | critical | absolute", formalDefinition="The category of interval of values for continuous or ordinal observations conforming to this ObservationDefinition." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/observation-range-category")
        protected Enumeration<ObservationRangeCategory> category;

        /**
         * The low and high values determining the interval. There may be only one of the two.
         */
        @Child(name = "range", type = {Range.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The interval itself, for continuous or ordinal observations", formalDefinition="The low and high values determining the interval. There may be only one of the two." )
        protected Range range;

        /**
         * Codes to indicate the health context the range applies to. For example, the normal or therapeutic range.
         */
        @Child(name = "context", type = {CodeableConcept.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Range context qualifier", formalDefinition="Codes to indicate the health context the range applies to. For example, the normal or therapeutic range." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/referencerange-meaning")
        protected CodeableConcept context;

        /**
         * Codes to indicate the target population this reference range applies to.
         */
        @Child(name = "appliesTo", type = {CodeableConcept.class}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Targetted population of the range", formalDefinition="Codes to indicate the target population this reference range applies to." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/referencerange-appliesto")
        protected List<CodeableConcept> appliesTo;

        /**
         * Sex of the population the range applies to.
         */
        @Child(name = "gender", type = {CodeType.class}, order=5, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="male | female | other | unknown", formalDefinition="Sex of the population the range applies to." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/administrative-gender")
        protected Enumeration<AdministrativeGender> gender;

        /**
         * The age at which this reference range is applicable. This is a neonatal age (e.g. number of weeks at term) if the meaning says so.
         */
        @Child(name = "age", type = {Range.class}, order=6, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Applicable age range, if relevant", formalDefinition="The age at which this reference range is applicable. This is a neonatal age (e.g. number of weeks at term) if the meaning says so." )
        protected Range age;

        /**
         * The gestational age to which this reference range is applicable, in the context of pregnancy.
         */
        @Child(name = "gestationalAge", type = {Range.class}, order=7, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Applicable gestational age range, if relevant", formalDefinition="The gestational age to which this reference range is applicable, in the context of pregnancy." )
        protected Range gestationalAge;

        /**
         * Text based condition for which the reference range is valid.
         */
        @Child(name = "condition", type = {StringType.class}, order=8, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Condition associated with the reference range", formalDefinition="Text based condition for which the reference range is valid." )
        protected StringType condition;

        private static final long serialVersionUID = -416423468L;

    /**
     * Constructor
     */
      public ObservationDefinitionQualifiedIntervalComponent() {
        super();
      }

        /**
         * @return {@link #category} (The category of interval of values for continuous or ordinal observations conforming to this ObservationDefinition.). This is the underlying object with id, value and extensions. The accessor "getCategory" gives direct access to the value
         */
        public Enumeration<ObservationRangeCategory> getCategoryElement() { 
          if (this.category == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ObservationDefinitionQualifiedIntervalComponent.category");
            else if (Configuration.doAutoCreate())
              this.category = new Enumeration<ObservationRangeCategory>(new ObservationRangeCategoryEnumFactory()); // bb
          return this.category;
        }

        public boolean hasCategoryElement() { 
          return this.category != null && !this.category.isEmpty();
        }

        public boolean hasCategory() { 
          return this.category != null && !this.category.isEmpty();
        }

        /**
         * @param value {@link #category} (The category of interval of values for continuous or ordinal observations conforming to this ObservationDefinition.). This is the underlying object with id, value and extensions. The accessor "getCategory" gives direct access to the value
         */
        public ObservationDefinitionQualifiedIntervalComponent setCategoryElement(Enumeration<ObservationRangeCategory> value) { 
          this.category = value;
          return this;
        }

        /**
         * @return The category of interval of values for continuous or ordinal observations conforming to this ObservationDefinition.
         */
        public ObservationRangeCategory getCategory() { 
          return this.category == null ? null : this.category.getValue();
        }

        /**
         * @param value The category of interval of values for continuous or ordinal observations conforming to this ObservationDefinition.
         */
        public ObservationDefinitionQualifiedIntervalComponent setCategory(ObservationRangeCategory value) { 
          if (value == null)
            this.category = null;
          else {
            if (this.category == null)
              this.category = new Enumeration<ObservationRangeCategory>(new ObservationRangeCategoryEnumFactory());
            this.category.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #range} (The low and high values determining the interval. There may be only one of the two.)
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
         * @param value {@link #range} (The low and high values determining the interval. There may be only one of the two.)
         */
        public ObservationDefinitionQualifiedIntervalComponent setRange(Range value) { 
          this.range = value;
          return this;
        }

        /**
         * @return {@link #context} (Codes to indicate the health context the range applies to. For example, the normal or therapeutic range.)
         */
        public CodeableConcept getContext() { 
          if (this.context == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ObservationDefinitionQualifiedIntervalComponent.context");
            else if (Configuration.doAutoCreate())
              this.context = new CodeableConcept(); // cc
          return this.context;
        }

        public boolean hasContext() { 
          return this.context != null && !this.context.isEmpty();
        }

        /**
         * @param value {@link #context} (Codes to indicate the health context the range applies to. For example, the normal or therapeutic range.)
         */
        public ObservationDefinitionQualifiedIntervalComponent setContext(CodeableConcept value) { 
          this.context = value;
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
         * @return {@link #gender} (Sex of the population the range applies to.). This is the underlying object with id, value and extensions. The accessor "getGender" gives direct access to the value
         */
        public Enumeration<AdministrativeGender> getGenderElement() { 
          if (this.gender == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ObservationDefinitionQualifiedIntervalComponent.gender");
            else if (Configuration.doAutoCreate())
              this.gender = new Enumeration<AdministrativeGender>(new AdministrativeGenderEnumFactory()); // bb
          return this.gender;
        }

        public boolean hasGenderElement() { 
          return this.gender != null && !this.gender.isEmpty();
        }

        public boolean hasGender() { 
          return this.gender != null && !this.gender.isEmpty();
        }

        /**
         * @param value {@link #gender} (Sex of the population the range applies to.). This is the underlying object with id, value and extensions. The accessor "getGender" gives direct access to the value
         */
        public ObservationDefinitionQualifiedIntervalComponent setGenderElement(Enumeration<AdministrativeGender> value) { 
          this.gender = value;
          return this;
        }

        /**
         * @return Sex of the population the range applies to.
         */
        public AdministrativeGender getGender() { 
          return this.gender == null ? null : this.gender.getValue();
        }

        /**
         * @param value Sex of the population the range applies to.
         */
        public ObservationDefinitionQualifiedIntervalComponent setGender(AdministrativeGender value) { 
          if (value == null)
            this.gender = null;
          else {
            if (this.gender == null)
              this.gender = new Enumeration<AdministrativeGender>(new AdministrativeGenderEnumFactory());
            this.gender.setValue(value);
          }
          return this;
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
         * @return {@link #gestationalAge} (The gestational age to which this reference range is applicable, in the context of pregnancy.)
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
         * @param value {@link #gestationalAge} (The gestational age to which this reference range is applicable, in the context of pregnancy.)
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
          children.add(new Property("category", "code", "The category of interval of values for continuous or ordinal observations conforming to this ObservationDefinition.", 0, 1, category));
          children.add(new Property("range", "Range", "The low and high values determining the interval. There may be only one of the two.", 0, 1, range));
          children.add(new Property("context", "CodeableConcept", "Codes to indicate the health context the range applies to. For example, the normal or therapeutic range.", 0, 1, context));
          children.add(new Property("appliesTo", "CodeableConcept", "Codes to indicate the target population this reference range applies to.", 0, java.lang.Integer.MAX_VALUE, appliesTo));
          children.add(new Property("gender", "code", "Sex of the population the range applies to.", 0, 1, gender));
          children.add(new Property("age", "Range", "The age at which this reference range is applicable. This is a neonatal age (e.g. number of weeks at term) if the meaning says so.", 0, 1, age));
          children.add(new Property("gestationalAge", "Range", "The gestational age to which this reference range is applicable, in the context of pregnancy.", 0, 1, gestationalAge));
          children.add(new Property("condition", "string", "Text based condition for which the reference range is valid.", 0, 1, condition));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 50511102: /*category*/  return new Property("category", "code", "The category of interval of values for continuous or ordinal observations conforming to this ObservationDefinition.", 0, 1, category);
          case 108280125: /*range*/  return new Property("range", "Range", "The low and high values determining the interval. There may be only one of the two.", 0, 1, range);
          case 951530927: /*context*/  return new Property("context", "CodeableConcept", "Codes to indicate the health context the range applies to. For example, the normal or therapeutic range.", 0, 1, context);
          case -2089924569: /*appliesTo*/  return new Property("appliesTo", "CodeableConcept", "Codes to indicate the target population this reference range applies to.", 0, java.lang.Integer.MAX_VALUE, appliesTo);
          case -1249512767: /*gender*/  return new Property("gender", "code", "Sex of the population the range applies to.", 0, 1, gender);
          case 96511: /*age*/  return new Property("age", "Range", "The age at which this reference range is applicable. This is a neonatal age (e.g. number of weeks at term) if the meaning says so.", 0, 1, age);
          case -241217538: /*gestationalAge*/  return new Property("gestationalAge", "Range", "The gestational age to which this reference range is applicable, in the context of pregnancy.", 0, 1, gestationalAge);
          case -861311717: /*condition*/  return new Property("condition", "string", "Text based condition for which the reference range is valid.", 0, 1, condition);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 50511102: /*category*/ return this.category == null ? new Base[0] : new Base[] {this.category}; // Enumeration<ObservationRangeCategory>
        case 108280125: /*range*/ return this.range == null ? new Base[0] : new Base[] {this.range}; // Range
        case 951530927: /*context*/ return this.context == null ? new Base[0] : new Base[] {this.context}; // CodeableConcept
        case -2089924569: /*appliesTo*/ return this.appliesTo == null ? new Base[0] : this.appliesTo.toArray(new Base[this.appliesTo.size()]); // CodeableConcept
        case -1249512767: /*gender*/ return this.gender == null ? new Base[0] : new Base[] {this.gender}; // Enumeration<AdministrativeGender>
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
          value = new ObservationRangeCategoryEnumFactory().fromType(castToCode(value));
          this.category = (Enumeration) value; // Enumeration<ObservationRangeCategory>
          return value;
        case 108280125: // range
          this.range = castToRange(value); // Range
          return value;
        case 951530927: // context
          this.context = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -2089924569: // appliesTo
          this.getAppliesTo().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -1249512767: // gender
          value = new AdministrativeGenderEnumFactory().fromType(castToCode(value));
          this.gender = (Enumeration) value; // Enumeration<AdministrativeGender>
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
          value = new ObservationRangeCategoryEnumFactory().fromType(castToCode(value));
          this.category = (Enumeration) value; // Enumeration<ObservationRangeCategory>
        } else if (name.equals("range")) {
          this.range = castToRange(value); // Range
        } else if (name.equals("context")) {
          this.context = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("appliesTo")) {
          this.getAppliesTo().add(castToCodeableConcept(value));
        } else if (name.equals("gender")) {
          value = new AdministrativeGenderEnumFactory().fromType(castToCode(value));
          this.gender = (Enumeration) value; // Enumeration<AdministrativeGender>
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
        case 50511102:  return getCategoryElement();
        case 108280125:  return getRange(); 
        case 951530927:  return getContext(); 
        case -2089924569:  return addAppliesTo(); 
        case -1249512767:  return getGenderElement();
        case 96511:  return getAge(); 
        case -241217538:  return getGestationalAge(); 
        case -861311717:  return getConditionElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 50511102: /*category*/ return new String[] {"code"};
        case 108280125: /*range*/ return new String[] {"Range"};
        case 951530927: /*context*/ return new String[] {"CodeableConcept"};
        case -2089924569: /*appliesTo*/ return new String[] {"CodeableConcept"};
        case -1249512767: /*gender*/ return new String[] {"code"};
        case 96511: /*age*/ return new String[] {"Range"};
        case -241217538: /*gestationalAge*/ return new String[] {"Range"};
        case -861311717: /*condition*/ return new String[] {"string"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("category")) {
          throw new FHIRException("Cannot call addChild on a primitive type ObservationDefinition.category");
        }
        else if (name.equals("range")) {
          this.range = new Range();
          return this.range;
        }
        else if (name.equals("context")) {
          this.context = new CodeableConcept();
          return this.context;
        }
        else if (name.equals("appliesTo")) {
          return addAppliesTo();
        }
        else if (name.equals("gender")) {
          throw new FHIRException("Cannot call addChild on a primitive type ObservationDefinition.gender");
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
        dst.context = context == null ? null : context.copy();
        if (appliesTo != null) {
          dst.appliesTo = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : appliesTo)
            dst.appliesTo.add(i.copy());
        };
        dst.gender = gender == null ? null : gender.copy();
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
        return compareDeep(category, o.category, true) && compareDeep(range, o.range, true) && compareDeep(context, o.context, true)
           && compareDeep(appliesTo, o.appliesTo, true) && compareDeep(gender, o.gender, true) && compareDeep(age, o.age, true)
           && compareDeep(gestationalAge, o.gestationalAge, true) && compareDeep(condition, o.condition, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ObservationDefinitionQualifiedIntervalComponent))
          return false;
        ObservationDefinitionQualifiedIntervalComponent o = (ObservationDefinitionQualifiedIntervalComponent) other_;
        return compareValues(category, o.category, true) && compareValues(gender, o.gender, true) && compareValues(condition, o.condition, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(category, range, context
          , appliesTo, gender, age, gestationalAge, condition);
      }

  public String fhirType() {
    return "ObservationDefinition.qualifiedInterval";

  }

  }

    /**
     * A code that classifies the general type of observation.
     */
    @Child(name = "category", type = {CodeableConcept.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Category of observation", formalDefinition="A code that classifies the general type of observation." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/observation-category")
    protected List<CodeableConcept> category;

    /**
     * Describes what will be observed. Sometimes this is called the observation "name".
     */
    @Child(name = "code", type = {CodeableConcept.class}, order=1, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Type of observation (code / type)", formalDefinition="Describes what will be observed. Sometimes this is called the observation \"name\"." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/observation-codes")
    protected CodeableConcept code;

    /**
     * A unique identifier assigned to this ObservationDefinition artifact.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Business identifier for this ObservationDefinition instance", formalDefinition="A unique identifier assigned to this ObservationDefinition artifact." )
    protected List<Identifier> identifier;

    /**
     * The data types allowed for the value element of the instance observations conforming to this ObservationDefinition.
     */
    @Child(name = "permittedDataType", type = {CodeType.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Quantity | CodeableConcept | string | boolean | integer | Range | Ratio | SampledData | time | dateTime | Period", formalDefinition="The data types allowed for the value element of the instance observations conforming to this ObservationDefinition." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/permitted-data-type")
    protected List<Enumeration<ObservationDataType>> permittedDataType;

    /**
     * Multiple results allowed for observations conforming to this ObservationDefinition.
     */
    @Child(name = "multipleResultsAllowed", type = {BooleanType.class}, order=4, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Multiple results allowed", formalDefinition="Multiple results allowed for observations conforming to this ObservationDefinition." )
    protected BooleanType multipleResultsAllowed;

    /**
     * The method or technique used to perform the observation.
     */
    @Child(name = "method", type = {CodeableConcept.class}, order=5, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Method used to produce the observation", formalDefinition="The method or technique used to perform the observation." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/observation-methods")
    protected CodeableConcept method;

    /**
     * The preferred name to be used when reporting the results of observations conforming to this ObservationDefinition.
     */
    @Child(name = "preferredReportName", type = {StringType.class}, order=6, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Preferred report name", formalDefinition="The preferred name to be used when reporting the results of observations conforming to this ObservationDefinition." )
    protected StringType preferredReportName;

    /**
     * Characteristics for quantitative results of this observation.
     */
    @Child(name = "quantitativeDetails", type = {}, order=7, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Characteristics of quantitative results", formalDefinition="Characteristics for quantitative results of this observation." )
    protected ObservationDefinitionQuantitativeDetailsComponent quantitativeDetails;

    /**
     * Multiple  ranges of results qualified by different contexts for ordinal or continuous observations conforming to this ObservationDefinition.
     */
    @Child(name = "qualifiedInterval", type = {}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Qualified range for continuous and ordinal observation results", formalDefinition="Multiple  ranges of results qualified by different contexts for ordinal or continuous observations conforming to this ObservationDefinition." )
    protected List<ObservationDefinitionQualifiedIntervalComponent> qualifiedInterval;

    /**
     * The set of valid coded results for the observations  conforming to this ObservationDefinition.
     */
    @Child(name = "validCodedValueSet", type = {ValueSet.class}, order=9, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Value set of valid coded values for the observations conforming to this ObservationDefinition", formalDefinition="The set of valid coded results for the observations  conforming to this ObservationDefinition." )
    protected Reference validCodedValueSet;

    /**
     * The actual object that is the target of the reference (The set of valid coded results for the observations  conforming to this ObservationDefinition.)
     */
    protected ValueSet validCodedValueSetTarget;

    /**
     * The set of normal coded results for the observations conforming to this ObservationDefinition.
     */
    @Child(name = "normalCodedValueSet", type = {ValueSet.class}, order=10, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Value set of normal coded values for the observations conforming to this ObservationDefinition", formalDefinition="The set of normal coded results for the observations conforming to this ObservationDefinition." )
    protected Reference normalCodedValueSet;

    /**
     * The actual object that is the target of the reference (The set of normal coded results for the observations conforming to this ObservationDefinition.)
     */
    protected ValueSet normalCodedValueSetTarget;

    /**
     * The set of abnormal coded results for the observation conforming to this ObservationDefinition.
     */
    @Child(name = "abnormalCodedValueSet", type = {ValueSet.class}, order=11, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Value set of abnormal coded values for the observations conforming to this ObservationDefinition", formalDefinition="The set of abnormal coded results for the observation conforming to this ObservationDefinition." )
    protected Reference abnormalCodedValueSet;

    /**
     * The actual object that is the target of the reference (The set of abnormal coded results for the observation conforming to this ObservationDefinition.)
     */
    protected ValueSet abnormalCodedValueSetTarget;

    /**
     * The set of critical coded results for the observation conforming to this ObservationDefinition.
     */
    @Child(name = "criticalCodedValueSet", type = {ValueSet.class}, order=12, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Value set of critical coded values for the observations conforming to this ObservationDefinition", formalDefinition="The set of critical coded results for the observation conforming to this ObservationDefinition." )
    protected Reference criticalCodedValueSet;

    /**
     * The actual object that is the target of the reference (The set of critical coded results for the observation conforming to this ObservationDefinition.)
     */
    protected ValueSet criticalCodedValueSetTarget;

    private static final long serialVersionUID = 2136752757L;

  /**
   * Constructor
   */
    public ObservationDefinition() {
      super();
    }

  /**
   * Constructor
   */
    public ObservationDefinition(CodeableConcept code) {
      super();
      this.code = code;
    }

    /**
     * @return {@link #category} (A code that classifies the general type of observation.)
     */
    public List<CodeableConcept> getCategory() { 
      if (this.category == null)
        this.category = new ArrayList<CodeableConcept>();
      return this.category;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ObservationDefinition setCategory(List<CodeableConcept> theCategory) { 
      this.category = theCategory;
      return this;
    }

    public boolean hasCategory() { 
      if (this.category == null)
        return false;
      for (CodeableConcept item : this.category)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addCategory() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.category == null)
        this.category = new ArrayList<CodeableConcept>();
      this.category.add(t);
      return t;
    }

    public ObservationDefinition addCategory(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.category == null)
        this.category = new ArrayList<CodeableConcept>();
      this.category.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #category}, creating it if it does not already exist
     */
    public CodeableConcept getCategoryFirstRep() { 
      if (getCategory().isEmpty()) {
        addCategory();
      }
      return getCategory().get(0);
    }

    /**
     * @return {@link #code} (Describes what will be observed. Sometimes this is called the observation "name".)
     */
    public CodeableConcept getCode() { 
      if (this.code == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ObservationDefinition.code");
        else if (Configuration.doAutoCreate())
          this.code = new CodeableConcept(); // cc
      return this.code;
    }

    public boolean hasCode() { 
      return this.code != null && !this.code.isEmpty();
    }

    /**
     * @param value {@link #code} (Describes what will be observed. Sometimes this is called the observation "name".)
     */
    public ObservationDefinition setCode(CodeableConcept value) { 
      this.code = value;
      return this;
    }

    /**
     * @return {@link #identifier} (A unique identifier assigned to this ObservationDefinition artifact.)
     */
    public List<Identifier> getIdentifier() { 
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      return this.identifier;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ObservationDefinition setIdentifier(List<Identifier> theIdentifier) { 
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

    public ObservationDefinition addIdentifier(Identifier t) { //3
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
     * @return {@link #permittedDataType} (The data types allowed for the value element of the instance observations conforming to this ObservationDefinition.)
     */
    public List<Enumeration<ObservationDataType>> getPermittedDataType() { 
      if (this.permittedDataType == null)
        this.permittedDataType = new ArrayList<Enumeration<ObservationDataType>>();
      return this.permittedDataType;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ObservationDefinition setPermittedDataType(List<Enumeration<ObservationDataType>> thePermittedDataType) { 
      this.permittedDataType = thePermittedDataType;
      return this;
    }

    public boolean hasPermittedDataType() { 
      if (this.permittedDataType == null)
        return false;
      for (Enumeration<ObservationDataType> item : this.permittedDataType)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #permittedDataType} (The data types allowed for the value element of the instance observations conforming to this ObservationDefinition.)
     */
    public Enumeration<ObservationDataType> addPermittedDataTypeElement() {//2 
      Enumeration<ObservationDataType> t = new Enumeration<ObservationDataType>(new ObservationDataTypeEnumFactory());
      if (this.permittedDataType == null)
        this.permittedDataType = new ArrayList<Enumeration<ObservationDataType>>();
      this.permittedDataType.add(t);
      return t;
    }

    /**
     * @param value {@link #permittedDataType} (The data types allowed for the value element of the instance observations conforming to this ObservationDefinition.)
     */
    public ObservationDefinition addPermittedDataType(ObservationDataType value) { //1
      Enumeration<ObservationDataType> t = new Enumeration<ObservationDataType>(new ObservationDataTypeEnumFactory());
      t.setValue(value);
      if (this.permittedDataType == null)
        this.permittedDataType = new ArrayList<Enumeration<ObservationDataType>>();
      this.permittedDataType.add(t);
      return this;
    }

    /**
     * @param value {@link #permittedDataType} (The data types allowed for the value element of the instance observations conforming to this ObservationDefinition.)
     */
    public boolean hasPermittedDataType(ObservationDataType value) { 
      if (this.permittedDataType == null)
        return false;
      for (Enumeration<ObservationDataType> v : this.permittedDataType)
        if (v.getValue().equals(value)) // code
          return true;
      return false;
    }

    /**
     * @return {@link #multipleResultsAllowed} (Multiple results allowed for observations conforming to this ObservationDefinition.). This is the underlying object with id, value and extensions. The accessor "getMultipleResultsAllowed" gives direct access to the value
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
     * @param value {@link #multipleResultsAllowed} (Multiple results allowed for observations conforming to this ObservationDefinition.). This is the underlying object with id, value and extensions. The accessor "getMultipleResultsAllowed" gives direct access to the value
     */
    public ObservationDefinition setMultipleResultsAllowedElement(BooleanType value) { 
      this.multipleResultsAllowed = value;
      return this;
    }

    /**
     * @return Multiple results allowed for observations conforming to this ObservationDefinition.
     */
    public boolean getMultipleResultsAllowed() { 
      return this.multipleResultsAllowed == null || this.multipleResultsAllowed.isEmpty() ? false : this.multipleResultsAllowed.getValue();
    }

    /**
     * @param value Multiple results allowed for observations conforming to this ObservationDefinition.
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
     * @return {@link #preferredReportName} (The preferred name to be used when reporting the results of observations conforming to this ObservationDefinition.). This is the underlying object with id, value and extensions. The accessor "getPreferredReportName" gives direct access to the value
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
     * @param value {@link #preferredReportName} (The preferred name to be used when reporting the results of observations conforming to this ObservationDefinition.). This is the underlying object with id, value and extensions. The accessor "getPreferredReportName" gives direct access to the value
     */
    public ObservationDefinition setPreferredReportNameElement(StringType value) { 
      this.preferredReportName = value;
      return this;
    }

    /**
     * @return The preferred name to be used when reporting the results of observations conforming to this ObservationDefinition.
     */
    public String getPreferredReportName() { 
      return this.preferredReportName == null ? null : this.preferredReportName.getValue();
    }

    /**
     * @param value The preferred name to be used when reporting the results of observations conforming to this ObservationDefinition.
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
     * @return {@link #qualifiedInterval} (Multiple  ranges of results qualified by different contexts for ordinal or continuous observations conforming to this ObservationDefinition.)
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
     * @return {@link #validCodedValueSet} (The set of valid coded results for the observations  conforming to this ObservationDefinition.)
     */
    public Reference getValidCodedValueSet() { 
      if (this.validCodedValueSet == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ObservationDefinition.validCodedValueSet");
        else if (Configuration.doAutoCreate())
          this.validCodedValueSet = new Reference(); // cc
      return this.validCodedValueSet;
    }

    public boolean hasValidCodedValueSet() { 
      return this.validCodedValueSet != null && !this.validCodedValueSet.isEmpty();
    }

    /**
     * @param value {@link #validCodedValueSet} (The set of valid coded results for the observations  conforming to this ObservationDefinition.)
     */
    public ObservationDefinition setValidCodedValueSet(Reference value) { 
      this.validCodedValueSet = value;
      return this;
    }

    /**
     * @return {@link #validCodedValueSet} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The set of valid coded results for the observations  conforming to this ObservationDefinition.)
     */
    public ValueSet getValidCodedValueSetTarget() { 
      if (this.validCodedValueSetTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ObservationDefinition.validCodedValueSet");
        else if (Configuration.doAutoCreate())
          this.validCodedValueSetTarget = new ValueSet(); // aa
      return this.validCodedValueSetTarget;
    }

    /**
     * @param value {@link #validCodedValueSet} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The set of valid coded results for the observations  conforming to this ObservationDefinition.)
     */
    public ObservationDefinition setValidCodedValueSetTarget(ValueSet value) { 
      this.validCodedValueSetTarget = value;
      return this;
    }

    /**
     * @return {@link #normalCodedValueSet} (The set of normal coded results for the observations conforming to this ObservationDefinition.)
     */
    public Reference getNormalCodedValueSet() { 
      if (this.normalCodedValueSet == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ObservationDefinition.normalCodedValueSet");
        else if (Configuration.doAutoCreate())
          this.normalCodedValueSet = new Reference(); // cc
      return this.normalCodedValueSet;
    }

    public boolean hasNormalCodedValueSet() { 
      return this.normalCodedValueSet != null && !this.normalCodedValueSet.isEmpty();
    }

    /**
     * @param value {@link #normalCodedValueSet} (The set of normal coded results for the observations conforming to this ObservationDefinition.)
     */
    public ObservationDefinition setNormalCodedValueSet(Reference value) { 
      this.normalCodedValueSet = value;
      return this;
    }

    /**
     * @return {@link #normalCodedValueSet} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The set of normal coded results for the observations conforming to this ObservationDefinition.)
     */
    public ValueSet getNormalCodedValueSetTarget() { 
      if (this.normalCodedValueSetTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ObservationDefinition.normalCodedValueSet");
        else if (Configuration.doAutoCreate())
          this.normalCodedValueSetTarget = new ValueSet(); // aa
      return this.normalCodedValueSetTarget;
    }

    /**
     * @param value {@link #normalCodedValueSet} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The set of normal coded results for the observations conforming to this ObservationDefinition.)
     */
    public ObservationDefinition setNormalCodedValueSetTarget(ValueSet value) { 
      this.normalCodedValueSetTarget = value;
      return this;
    }

    /**
     * @return {@link #abnormalCodedValueSet} (The set of abnormal coded results for the observation conforming to this ObservationDefinition.)
     */
    public Reference getAbnormalCodedValueSet() { 
      if (this.abnormalCodedValueSet == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ObservationDefinition.abnormalCodedValueSet");
        else if (Configuration.doAutoCreate())
          this.abnormalCodedValueSet = new Reference(); // cc
      return this.abnormalCodedValueSet;
    }

    public boolean hasAbnormalCodedValueSet() { 
      return this.abnormalCodedValueSet != null && !this.abnormalCodedValueSet.isEmpty();
    }

    /**
     * @param value {@link #abnormalCodedValueSet} (The set of abnormal coded results for the observation conforming to this ObservationDefinition.)
     */
    public ObservationDefinition setAbnormalCodedValueSet(Reference value) { 
      this.abnormalCodedValueSet = value;
      return this;
    }

    /**
     * @return {@link #abnormalCodedValueSet} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The set of abnormal coded results for the observation conforming to this ObservationDefinition.)
     */
    public ValueSet getAbnormalCodedValueSetTarget() { 
      if (this.abnormalCodedValueSetTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ObservationDefinition.abnormalCodedValueSet");
        else if (Configuration.doAutoCreate())
          this.abnormalCodedValueSetTarget = new ValueSet(); // aa
      return this.abnormalCodedValueSetTarget;
    }

    /**
     * @param value {@link #abnormalCodedValueSet} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The set of abnormal coded results for the observation conforming to this ObservationDefinition.)
     */
    public ObservationDefinition setAbnormalCodedValueSetTarget(ValueSet value) { 
      this.abnormalCodedValueSetTarget = value;
      return this;
    }

    /**
     * @return {@link #criticalCodedValueSet} (The set of critical coded results for the observation conforming to this ObservationDefinition.)
     */
    public Reference getCriticalCodedValueSet() { 
      if (this.criticalCodedValueSet == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ObservationDefinition.criticalCodedValueSet");
        else if (Configuration.doAutoCreate())
          this.criticalCodedValueSet = new Reference(); // cc
      return this.criticalCodedValueSet;
    }

    public boolean hasCriticalCodedValueSet() { 
      return this.criticalCodedValueSet != null && !this.criticalCodedValueSet.isEmpty();
    }

    /**
     * @param value {@link #criticalCodedValueSet} (The set of critical coded results for the observation conforming to this ObservationDefinition.)
     */
    public ObservationDefinition setCriticalCodedValueSet(Reference value) { 
      this.criticalCodedValueSet = value;
      return this;
    }

    /**
     * @return {@link #criticalCodedValueSet} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The set of critical coded results for the observation conforming to this ObservationDefinition.)
     */
    public ValueSet getCriticalCodedValueSetTarget() { 
      if (this.criticalCodedValueSetTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ObservationDefinition.criticalCodedValueSet");
        else if (Configuration.doAutoCreate())
          this.criticalCodedValueSetTarget = new ValueSet(); // aa
      return this.criticalCodedValueSetTarget;
    }

    /**
     * @param value {@link #criticalCodedValueSet} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The set of critical coded results for the observation conforming to this ObservationDefinition.)
     */
    public ObservationDefinition setCriticalCodedValueSetTarget(ValueSet value) { 
      this.criticalCodedValueSetTarget = value;
      return this;
    }

      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("category", "CodeableConcept", "A code that classifies the general type of observation.", 0, java.lang.Integer.MAX_VALUE, category));
        children.add(new Property("code", "CodeableConcept", "Describes what will be observed. Sometimes this is called the observation \"name\".", 0, 1, code));
        children.add(new Property("identifier", "Identifier", "A unique identifier assigned to this ObservationDefinition artifact.", 0, java.lang.Integer.MAX_VALUE, identifier));
        children.add(new Property("permittedDataType", "code", "The data types allowed for the value element of the instance observations conforming to this ObservationDefinition.", 0, java.lang.Integer.MAX_VALUE, permittedDataType));
        children.add(new Property("multipleResultsAllowed", "boolean", "Multiple results allowed for observations conforming to this ObservationDefinition.", 0, 1, multipleResultsAllowed));
        children.add(new Property("method", "CodeableConcept", "The method or technique used to perform the observation.", 0, 1, method));
        children.add(new Property("preferredReportName", "string", "The preferred name to be used when reporting the results of observations conforming to this ObservationDefinition.", 0, 1, preferredReportName));
        children.add(new Property("quantitativeDetails", "", "Characteristics for quantitative results of this observation.", 0, 1, quantitativeDetails));
        children.add(new Property("qualifiedInterval", "", "Multiple  ranges of results qualified by different contexts for ordinal or continuous observations conforming to this ObservationDefinition.", 0, java.lang.Integer.MAX_VALUE, qualifiedInterval));
        children.add(new Property("validCodedValueSet", "Reference(ValueSet)", "The set of valid coded results for the observations  conforming to this ObservationDefinition.", 0, 1, validCodedValueSet));
        children.add(new Property("normalCodedValueSet", "Reference(ValueSet)", "The set of normal coded results for the observations conforming to this ObservationDefinition.", 0, 1, normalCodedValueSet));
        children.add(new Property("abnormalCodedValueSet", "Reference(ValueSet)", "The set of abnormal coded results for the observation conforming to this ObservationDefinition.", 0, 1, abnormalCodedValueSet));
        children.add(new Property("criticalCodedValueSet", "Reference(ValueSet)", "The set of critical coded results for the observation conforming to this ObservationDefinition.", 0, 1, criticalCodedValueSet));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case 50511102: /*category*/  return new Property("category", "CodeableConcept", "A code that classifies the general type of observation.", 0, java.lang.Integer.MAX_VALUE, category);
        case 3059181: /*code*/  return new Property("code", "CodeableConcept", "Describes what will be observed. Sometimes this is called the observation \"name\".", 0, 1, code);
        case -1618432855: /*identifier*/  return new Property("identifier", "Identifier", "A unique identifier assigned to this ObservationDefinition artifact.", 0, java.lang.Integer.MAX_VALUE, identifier);
        case -99492804: /*permittedDataType*/  return new Property("permittedDataType", "code", "The data types allowed for the value element of the instance observations conforming to this ObservationDefinition.", 0, java.lang.Integer.MAX_VALUE, permittedDataType);
        case -2102414590: /*multipleResultsAllowed*/  return new Property("multipleResultsAllowed", "boolean", "Multiple results allowed for observations conforming to this ObservationDefinition.", 0, 1, multipleResultsAllowed);
        case -1077554975: /*method*/  return new Property("method", "CodeableConcept", "The method or technique used to perform the observation.", 0, 1, method);
        case -1851030208: /*preferredReportName*/  return new Property("preferredReportName", "string", "The preferred name to be used when reporting the results of observations conforming to this ObservationDefinition.", 0, 1, preferredReportName);
        case 842150763: /*quantitativeDetails*/  return new Property("quantitativeDetails", "", "Characteristics for quantitative results of this observation.", 0, 1, quantitativeDetails);
        case 1882971521: /*qualifiedInterval*/  return new Property("qualifiedInterval", "", "Multiple  ranges of results qualified by different contexts for ordinal or continuous observations conforming to this ObservationDefinition.", 0, java.lang.Integer.MAX_VALUE, qualifiedInterval);
        case 1374640076: /*validCodedValueSet*/  return new Property("validCodedValueSet", "Reference(ValueSet)", "The set of valid coded results for the observations  conforming to this ObservationDefinition.", 0, 1, validCodedValueSet);
        case -837500735: /*normalCodedValueSet*/  return new Property("normalCodedValueSet", "Reference(ValueSet)", "The set of normal coded results for the observations conforming to this ObservationDefinition.", 0, 1, normalCodedValueSet);
        case 1073600256: /*abnormalCodedValueSet*/  return new Property("abnormalCodedValueSet", "Reference(ValueSet)", "The set of abnormal coded results for the observation conforming to this ObservationDefinition.", 0, 1, abnormalCodedValueSet);
        case 2568457: /*criticalCodedValueSet*/  return new Property("criticalCodedValueSet", "Reference(ValueSet)", "The set of critical coded results for the observation conforming to this ObservationDefinition.", 0, 1, criticalCodedValueSet);
        default: return super.getNamedProperty(_hash, _name, _checkValid);
        }

      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 50511102: /*category*/ return this.category == null ? new Base[0] : this.category.toArray(new Base[this.category.size()]); // CodeableConcept
        case 3059181: /*code*/ return this.code == null ? new Base[0] : new Base[] {this.code}; // CodeableConcept
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case -99492804: /*permittedDataType*/ return this.permittedDataType == null ? new Base[0] : this.permittedDataType.toArray(new Base[this.permittedDataType.size()]); // Enumeration<ObservationDataType>
        case -2102414590: /*multipleResultsAllowed*/ return this.multipleResultsAllowed == null ? new Base[0] : new Base[] {this.multipleResultsAllowed}; // BooleanType
        case -1077554975: /*method*/ return this.method == null ? new Base[0] : new Base[] {this.method}; // CodeableConcept
        case -1851030208: /*preferredReportName*/ return this.preferredReportName == null ? new Base[0] : new Base[] {this.preferredReportName}; // StringType
        case 842150763: /*quantitativeDetails*/ return this.quantitativeDetails == null ? new Base[0] : new Base[] {this.quantitativeDetails}; // ObservationDefinitionQuantitativeDetailsComponent
        case 1882971521: /*qualifiedInterval*/ return this.qualifiedInterval == null ? new Base[0] : this.qualifiedInterval.toArray(new Base[this.qualifiedInterval.size()]); // ObservationDefinitionQualifiedIntervalComponent
        case 1374640076: /*validCodedValueSet*/ return this.validCodedValueSet == null ? new Base[0] : new Base[] {this.validCodedValueSet}; // Reference
        case -837500735: /*normalCodedValueSet*/ return this.normalCodedValueSet == null ? new Base[0] : new Base[] {this.normalCodedValueSet}; // Reference
        case 1073600256: /*abnormalCodedValueSet*/ return this.abnormalCodedValueSet == null ? new Base[0] : new Base[] {this.abnormalCodedValueSet}; // Reference
        case 2568457: /*criticalCodedValueSet*/ return this.criticalCodedValueSet == null ? new Base[0] : new Base[] {this.criticalCodedValueSet}; // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 50511102: // category
          this.getCategory().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 3059181: // code
          this.code = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1618432855: // identifier
          this.getIdentifier().add(castToIdentifier(value)); // Identifier
          return value;
        case -99492804: // permittedDataType
          value = new ObservationDataTypeEnumFactory().fromType(castToCode(value));
          this.getPermittedDataType().add((Enumeration) value); // Enumeration<ObservationDataType>
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
          this.validCodedValueSet = castToReference(value); // Reference
          return value;
        case -837500735: // normalCodedValueSet
          this.normalCodedValueSet = castToReference(value); // Reference
          return value;
        case 1073600256: // abnormalCodedValueSet
          this.abnormalCodedValueSet = castToReference(value); // Reference
          return value;
        case 2568457: // criticalCodedValueSet
          this.criticalCodedValueSet = castToReference(value); // Reference
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("category")) {
          this.getCategory().add(castToCodeableConcept(value));
        } else if (name.equals("code")) {
          this.code = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("identifier")) {
          this.getIdentifier().add(castToIdentifier(value));
        } else if (name.equals("permittedDataType")) {
          value = new ObservationDataTypeEnumFactory().fromType(castToCode(value));
          this.getPermittedDataType().add((Enumeration) value);
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
          this.validCodedValueSet = castToReference(value); // Reference
        } else if (name.equals("normalCodedValueSet")) {
          this.normalCodedValueSet = castToReference(value); // Reference
        } else if (name.equals("abnormalCodedValueSet")) {
          this.abnormalCodedValueSet = castToReference(value); // Reference
        } else if (name.equals("criticalCodedValueSet")) {
          this.criticalCodedValueSet = castToReference(value); // Reference
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 50511102:  return addCategory(); 
        case 3059181:  return getCode(); 
        case -1618432855:  return addIdentifier(); 
        case -99492804:  return addPermittedDataTypeElement();
        case -2102414590:  return getMultipleResultsAllowedElement();
        case -1077554975:  return getMethod(); 
        case -1851030208:  return getPreferredReportNameElement();
        case 842150763:  return getQuantitativeDetails(); 
        case 1882971521:  return addQualifiedInterval(); 
        case 1374640076:  return getValidCodedValueSet(); 
        case -837500735:  return getNormalCodedValueSet(); 
        case 1073600256:  return getAbnormalCodedValueSet(); 
        case 2568457:  return getCriticalCodedValueSet(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 50511102: /*category*/ return new String[] {"CodeableConcept"};
        case 3059181: /*code*/ return new String[] {"CodeableConcept"};
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case -99492804: /*permittedDataType*/ return new String[] {"code"};
        case -2102414590: /*multipleResultsAllowed*/ return new String[] {"boolean"};
        case -1077554975: /*method*/ return new String[] {"CodeableConcept"};
        case -1851030208: /*preferredReportName*/ return new String[] {"string"};
        case 842150763: /*quantitativeDetails*/ return new String[] {};
        case 1882971521: /*qualifiedInterval*/ return new String[] {};
        case 1374640076: /*validCodedValueSet*/ return new String[] {"Reference"};
        case -837500735: /*normalCodedValueSet*/ return new String[] {"Reference"};
        case 1073600256: /*abnormalCodedValueSet*/ return new String[] {"Reference"};
        case 2568457: /*criticalCodedValueSet*/ return new String[] {"Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("category")) {
          return addCategory();
        }
        else if (name.equals("code")) {
          this.code = new CodeableConcept();
          return this.code;
        }
        else if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("permittedDataType")) {
          throw new FHIRException("Cannot call addChild on a primitive type ObservationDefinition.permittedDataType");
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
          this.validCodedValueSet = new Reference();
          return this.validCodedValueSet;
        }
        else if (name.equals("normalCodedValueSet")) {
          this.normalCodedValueSet = new Reference();
          return this.normalCodedValueSet;
        }
        else if (name.equals("abnormalCodedValueSet")) {
          this.abnormalCodedValueSet = new Reference();
          return this.abnormalCodedValueSet;
        }
        else if (name.equals("criticalCodedValueSet")) {
          this.criticalCodedValueSet = new Reference();
          return this.criticalCodedValueSet;
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
        if (category != null) {
          dst.category = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : category)
            dst.category.add(i.copy());
        };
        dst.code = code == null ? null : code.copy();
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        if (permittedDataType != null) {
          dst.permittedDataType = new ArrayList<Enumeration<ObservationDataType>>();
          for (Enumeration<ObservationDataType> i : permittedDataType)
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
        return compareDeep(category, o.category, true) && compareDeep(code, o.code, true) && compareDeep(identifier, o.identifier, true)
           && compareDeep(permittedDataType, o.permittedDataType, true) && compareDeep(multipleResultsAllowed, o.multipleResultsAllowed, true)
           && compareDeep(method, o.method, true) && compareDeep(preferredReportName, o.preferredReportName, true)
           && compareDeep(quantitativeDetails, o.quantitativeDetails, true) && compareDeep(qualifiedInterval, o.qualifiedInterval, true)
           && compareDeep(validCodedValueSet, o.validCodedValueSet, true) && compareDeep(normalCodedValueSet, o.normalCodedValueSet, true)
           && compareDeep(abnormalCodedValueSet, o.abnormalCodedValueSet, true) && compareDeep(criticalCodedValueSet, o.criticalCodedValueSet, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ObservationDefinition))
          return false;
        ObservationDefinition o = (ObservationDefinition) other_;
        return compareValues(permittedDataType, o.permittedDataType, true) && compareValues(multipleResultsAllowed, o.multipleResultsAllowed, true)
           && compareValues(preferredReportName, o.preferredReportName, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(category, code, identifier
          , permittedDataType, multipleResultsAllowed, method, preferredReportName, quantitativeDetails
          , qualifiedInterval, validCodedValueSet, normalCodedValueSet, abnormalCodedValueSet
          , criticalCodedValueSet);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.ObservationDefinition;
   }


}

