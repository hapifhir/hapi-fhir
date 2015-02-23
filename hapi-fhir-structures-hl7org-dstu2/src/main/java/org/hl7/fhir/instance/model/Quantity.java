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

// Generated on Wed, Feb 18, 2015 12:09-0500 for FHIR v0.4.0

import java.util.*;
import java.math.*;

import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.instance.model.annotations.Child;
import org.hl7.fhir.instance.model.annotations.Description;
import org.hl7.fhir.instance.model.annotations.DatatypeDef;
/**
 * A measured amount (or an amount that can potentially be measured). Note that measured amounts include amounts that are not precisely quantified, including amounts involving arbitrary units and floating currencies.
 */
@DatatypeDef(name="Quantity")
public class Quantity extends Type implements ICompositeType {

    public enum QuantityComparator {
        /**
         * The actual value is less than the given value.
         */
        LESS_THAN, 
        /**
         * The actual value is less than or equal to the given value.
         */
        LESS_OR_EQUAL, 
        /**
         * The actual value is greater than or equal to the given value.
         */
        GREATER_OR_EQUAL, 
        /**
         * The actual value is greater than the given value.
         */
        GREATER_THAN, 
        /**
         * added to help the parsers
         */
        NULL;
        public static QuantityComparator fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("<".equals(codeString))
          return LESS_THAN;
        if ("<=".equals(codeString))
          return LESS_OR_EQUAL;
        if (">=".equals(codeString))
          return GREATER_OR_EQUAL;
        if (">".equals(codeString))
          return GREATER_THAN;
        throw new Exception("Unknown QuantityComparator code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case LESS_THAN: return "<";
            case LESS_OR_EQUAL: return "<=";
            case GREATER_OR_EQUAL: return ">=";
            case GREATER_THAN: return ">";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case LESS_THAN: return "";
            case LESS_OR_EQUAL: return "";
            case GREATER_OR_EQUAL: return "";
            case GREATER_THAN: return "";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case LESS_THAN: return "The actual value is less than the given value.";
            case LESS_OR_EQUAL: return "The actual value is less than or equal to the given value.";
            case GREATER_OR_EQUAL: return "The actual value is greater than or equal to the given value.";
            case GREATER_THAN: return "The actual value is greater than the given value.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case LESS_THAN: return "<";
            case LESS_OR_EQUAL: return "<=";
            case GREATER_OR_EQUAL: return ">=";
            case GREATER_THAN: return ">";
            default: return "?";
          }
        }
    }

  public static class QuantityComparatorEnumFactory implements EnumFactory<QuantityComparator> {
    public QuantityComparator fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("<".equals(codeString))
          return QuantityComparator.LESS_THAN;
        if ("<=".equals(codeString))
          return QuantityComparator.LESS_OR_EQUAL;
        if (">=".equals(codeString))
          return QuantityComparator.GREATER_OR_EQUAL;
        if (">".equals(codeString))
          return QuantityComparator.GREATER_THAN;
        throw new IllegalArgumentException("Unknown QuantityComparator code '"+codeString+"'");
        }
    public String toCode(QuantityComparator code) {
      if (code == QuantityComparator.LESS_THAN)
        return "<";
      if (code == QuantityComparator.LESS_OR_EQUAL)
        return "<=";
      if (code == QuantityComparator.GREATER_OR_EQUAL)
        return ">=";
      if (code == QuantityComparator.GREATER_THAN)
        return ">";
      return "?";
      }
    }

    /**
     * The value of the measured amount. The value includes an implicit precision in the presentation of the value.
     */
    @Child(name = "value", type = {DecimalType.class}, order = 0, min = 0, max = 1)
    @Description(shortDefinition="Numerical value (with implicit precision)", formalDefinition="The value of the measured amount. The value includes an implicit precision in the presentation of the value." )
    protected DecimalType value;

    /**
     * How the value should be understood and represented - whether the actual value is greater or less than the stated value due to measurement issues. E.g. if the comparator is "<" , then the real value is < stated value.
     */
    @Child(name = "comparator", type = {CodeType.class}, order = 1, min = 0, max = 1)
    @Description(shortDefinition="< | <= | >= | > - how to understand the value", formalDefinition="How the value should be understood and represented - whether the actual value is greater or less than the stated value due to measurement issues. E.g. if the comparator is '<' , then the real value is < stated value." )
    protected Enumeration<QuantityComparator> comparator;

    /**
     * A human-readable form of the units.
     */
    @Child(name = "units", type = {StringType.class}, order = 2, min = 0, max = 1)
    @Description(shortDefinition="Unit representation", formalDefinition="A human-readable form of the units." )
    protected StringType units;

    /**
     * The identification of the system that provides the coded form of the unit.
     */
    @Child(name = "system", type = {UriType.class}, order = 3, min = 0, max = 1)
    @Description(shortDefinition="System that defines coded unit form", formalDefinition="The identification of the system that provides the coded form of the unit." )
    protected UriType system;

    /**
     * A computer processable form of the units in some unit representation system.
     */
    @Child(name = "code", type = {CodeType.class}, order = 4, min = 0, max = 1)
    @Description(shortDefinition="Coded form of the unit", formalDefinition="A computer processable form of the units in some unit representation system." )
    protected CodeType code;

    private static final long serialVersionUID = -483422721L;

    public Quantity() {
      super();
    }

    /**
     * @return {@link #value} (The value of the measured amount. The value includes an implicit precision in the presentation of the value.). This is the underlying object with id, value and extensions. The accessor "getValue" gives direct access to the value
     */
    public DecimalType getValueElement() { 
      if (this.value == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Quantity.value");
        else if (Configuration.doAutoCreate())
          this.value = new DecimalType(); // bb
      return this.value;
    }

    public boolean hasValueElement() { 
      return this.value != null && !this.value.isEmpty();
    }

    public boolean hasValue() { 
      return this.value != null && !this.value.isEmpty();
    }

    /**
     * @param value {@link #value} (The value of the measured amount. The value includes an implicit precision in the presentation of the value.). This is the underlying object with id, value and extensions. The accessor "getValue" gives direct access to the value
     */
    public Quantity setValueElement(DecimalType value) { 
      this.value = value;
      return this;
    }

    /**
     * @return The value of the measured amount. The value includes an implicit precision in the presentation of the value.
     */
    public BigDecimal getValue() { 
      return this.value == null ? null : this.value.getValue();
    }

    /**
     * @param value The value of the measured amount. The value includes an implicit precision in the presentation of the value.
     */
    public Quantity setValue(BigDecimal value) { 
      if (value == null)
        this.value = null;
      else {
        if (this.value == null)
          this.value = new DecimalType();
        this.value.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #comparator} (How the value should be understood and represented - whether the actual value is greater or less than the stated value due to measurement issues. E.g. if the comparator is "<" , then the real value is < stated value.). This is the underlying object with id, value and extensions. The accessor "getComparator" gives direct access to the value
     */
    public Enumeration<QuantityComparator> getComparatorElement() { 
      if (this.comparator == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Quantity.comparator");
        else if (Configuration.doAutoCreate())
          this.comparator = new Enumeration<QuantityComparator>(new QuantityComparatorEnumFactory()); // bb
      return this.comparator;
    }

    public boolean hasComparatorElement() { 
      return this.comparator != null && !this.comparator.isEmpty();
    }

    public boolean hasComparator() { 
      return this.comparator != null && !this.comparator.isEmpty();
    }

    /**
     * @param value {@link #comparator} (How the value should be understood and represented - whether the actual value is greater or less than the stated value due to measurement issues. E.g. if the comparator is "<" , then the real value is < stated value.). This is the underlying object with id, value and extensions. The accessor "getComparator" gives direct access to the value
     */
    public Quantity setComparatorElement(Enumeration<QuantityComparator> value) { 
      this.comparator = value;
      return this;
    }

    /**
     * @return How the value should be understood and represented - whether the actual value is greater or less than the stated value due to measurement issues. E.g. if the comparator is "<" , then the real value is < stated value.
     */
    public QuantityComparator getComparator() { 
      return this.comparator == null ? null : this.comparator.getValue();
    }

    /**
     * @param value How the value should be understood and represented - whether the actual value is greater or less than the stated value due to measurement issues. E.g. if the comparator is "<" , then the real value is < stated value.
     */
    public Quantity setComparator(QuantityComparator value) { 
      if (value == null)
        this.comparator = null;
      else {
        if (this.comparator == null)
          this.comparator = new Enumeration<QuantityComparator>(new QuantityComparatorEnumFactory());
        this.comparator.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #units} (A human-readable form of the units.). This is the underlying object with id, value and extensions. The accessor "getUnits" gives direct access to the value
     */
    public StringType getUnitsElement() { 
      if (this.units == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Quantity.units");
        else if (Configuration.doAutoCreate())
          this.units = new StringType(); // bb
      return this.units;
    }

    public boolean hasUnitsElement() { 
      return this.units != null && !this.units.isEmpty();
    }

    public boolean hasUnits() { 
      return this.units != null && !this.units.isEmpty();
    }

    /**
     * @param value {@link #units} (A human-readable form of the units.). This is the underlying object with id, value and extensions. The accessor "getUnits" gives direct access to the value
     */
    public Quantity setUnitsElement(StringType value) { 
      this.units = value;
      return this;
    }

    /**
     * @return A human-readable form of the units.
     */
    public String getUnits() { 
      return this.units == null ? null : this.units.getValue();
    }

    /**
     * @param value A human-readable form of the units.
     */
    public Quantity setUnits(String value) { 
      if (Utilities.noString(value))
        this.units = null;
      else {
        if (this.units == null)
          this.units = new StringType();
        this.units.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #system} (The identification of the system that provides the coded form of the unit.). This is the underlying object with id, value and extensions. The accessor "getSystem" gives direct access to the value
     */
    public UriType getSystemElement() { 
      if (this.system == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Quantity.system");
        else if (Configuration.doAutoCreate())
          this.system = new UriType(); // bb
      return this.system;
    }

    public boolean hasSystemElement() { 
      return this.system != null && !this.system.isEmpty();
    }

    public boolean hasSystem() { 
      return this.system != null && !this.system.isEmpty();
    }

    /**
     * @param value {@link #system} (The identification of the system that provides the coded form of the unit.). This is the underlying object with id, value and extensions. The accessor "getSystem" gives direct access to the value
     */
    public Quantity setSystemElement(UriType value) { 
      this.system = value;
      return this;
    }

    /**
     * @return The identification of the system that provides the coded form of the unit.
     */
    public String getSystem() { 
      return this.system == null ? null : this.system.getValue();
    }

    /**
     * @param value The identification of the system that provides the coded form of the unit.
     */
    public Quantity setSystem(String value) { 
      if (Utilities.noString(value))
        this.system = null;
      else {
        if (this.system == null)
          this.system = new UriType();
        this.system.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #code} (A computer processable form of the units in some unit representation system.). This is the underlying object with id, value and extensions. The accessor "getCode" gives direct access to the value
     */
    public CodeType getCodeElement() { 
      if (this.code == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Quantity.code");
        else if (Configuration.doAutoCreate())
          this.code = new CodeType(); // bb
      return this.code;
    }

    public boolean hasCodeElement() { 
      return this.code != null && !this.code.isEmpty();
    }

    public boolean hasCode() { 
      return this.code != null && !this.code.isEmpty();
    }

    /**
     * @param value {@link #code} (A computer processable form of the units in some unit representation system.). This is the underlying object with id, value and extensions. The accessor "getCode" gives direct access to the value
     */
    public Quantity setCodeElement(CodeType value) { 
      this.code = value;
      return this;
    }

    /**
     * @return A computer processable form of the units in some unit representation system.
     */
    public String getCode() { 
      return this.code == null ? null : this.code.getValue();
    }

    /**
     * @param value A computer processable form of the units in some unit representation system.
     */
    public Quantity setCode(String value) { 
      if (Utilities.noString(value))
        this.code = null;
      else {
        if (this.code == null)
          this.code = new CodeType();
        this.code.setValue(value);
      }
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("value", "decimal", "The value of the measured amount. The value includes an implicit precision in the presentation of the value.", 0, java.lang.Integer.MAX_VALUE, value));
        childrenList.add(new Property("comparator", "code", "How the value should be understood and represented - whether the actual value is greater or less than the stated value due to measurement issues. E.g. if the comparator is '<' , then the real value is < stated value.", 0, java.lang.Integer.MAX_VALUE, comparator));
        childrenList.add(new Property("units", "string", "A human-readable form of the units.", 0, java.lang.Integer.MAX_VALUE, units));
        childrenList.add(new Property("system", "uri", "The identification of the system that provides the coded form of the unit.", 0, java.lang.Integer.MAX_VALUE, system));
        childrenList.add(new Property("code", "code", "A computer processable form of the units in some unit representation system.", 0, java.lang.Integer.MAX_VALUE, code));
      }

      public Quantity copy() {
        Quantity dst = new Quantity();
        copyValues(dst);
        dst.value = value == null ? null : value.copy();
        dst.comparator = comparator == null ? null : comparator.copy();
        dst.units = units == null ? null : units.copy();
        dst.system = system == null ? null : system.copy();
        dst.code = code == null ? null : code.copy();
        return dst;
      }

      protected Quantity typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof Quantity))
          return false;
        Quantity o = (Quantity) other;
        return compareDeep(value, o.value, true) && compareDeep(comparator, o.comparator, true) && compareDeep(units, o.units, true)
           && compareDeep(system, o.system, true) && compareDeep(code, o.code, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Quantity))
          return false;
        Quantity o = (Quantity) other;
        return compareValues(value, o.value, true) && compareValues(comparator, o.comparator, true) && compareValues(units, o.units, true)
           && compareValues(system, o.system, true) && compareValues(code, o.code, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (value == null || value.isEmpty()) && (comparator == null || comparator.isEmpty())
           && (units == null || units.isEmpty()) && (system == null || system.isEmpty()) && (code == null || code.isEmpty())
          ;
      }


}

