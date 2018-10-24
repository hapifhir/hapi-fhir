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
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ChildOrder;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.Block;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.exceptions.FHIRException;
/**
 * Chemical substances are a single substance type whose primary defining element is the molecular structure. Chemical substances shall be defined on the basis of their complete covalent molecular structure; the presence of a salt (counter-ion) and/or solvates (water, alcohols) is also captured. Purity, grade, physical form or particle size are not taken into account in the definition of a chemical substance or in the assignment of a Substance ID.
 */
@DatatypeDef(name="SubstanceAmount")
public class SubstanceAmount extends BackboneType implements ICompositeType {

    @Block()
    public static class SubstanceAmountReferenceRangeComponent extends Element implements IBaseDatatypeElement {
        /**
         * Lower limit possible or expected.
         */
        @Child(name = "lowLimit", type = {Quantity.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Lower limit possible or expected", formalDefinition="Lower limit possible or expected." )
        protected Quantity lowLimit;

        /**
         * Upper limit possible or expected.
         */
        @Child(name = "highLimit", type = {Quantity.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Upper limit possible or expected", formalDefinition="Upper limit possible or expected." )
        protected Quantity highLimit;

        private static final long serialVersionUID = -193230412L;

    /**
     * Constructor
     */
      public SubstanceAmountReferenceRangeComponent() {
        super();
      }

        /**
         * @return {@link #lowLimit} (Lower limit possible or expected.)
         */
        public Quantity getLowLimit() { 
          if (this.lowLimit == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceAmountReferenceRangeComponent.lowLimit");
            else if (Configuration.doAutoCreate())
              this.lowLimit = new Quantity(); // cc
          return this.lowLimit;
        }

        public boolean hasLowLimit() { 
          return this.lowLimit != null && !this.lowLimit.isEmpty();
        }

        /**
         * @param value {@link #lowLimit} (Lower limit possible or expected.)
         */
        public SubstanceAmountReferenceRangeComponent setLowLimit(Quantity value) { 
          this.lowLimit = value;
          return this;
        }

        /**
         * @return {@link #highLimit} (Upper limit possible or expected.)
         */
        public Quantity getHighLimit() { 
          if (this.highLimit == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceAmountReferenceRangeComponent.highLimit");
            else if (Configuration.doAutoCreate())
              this.highLimit = new Quantity(); // cc
          return this.highLimit;
        }

        public boolean hasHighLimit() { 
          return this.highLimit != null && !this.highLimit.isEmpty();
        }

        /**
         * @param value {@link #highLimit} (Upper limit possible or expected.)
         */
        public SubstanceAmountReferenceRangeComponent setHighLimit(Quantity value) { 
          this.highLimit = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("lowLimit", "Quantity", "Lower limit possible or expected.", 0, 1, lowLimit));
          children.add(new Property("highLimit", "Quantity", "Upper limit possible or expected.", 0, 1, highLimit));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -1841058617: /*lowLimit*/  return new Property("lowLimit", "Quantity", "Lower limit possible or expected.", 0, 1, lowLimit);
          case -710757575: /*highLimit*/  return new Property("highLimit", "Quantity", "Upper limit possible or expected.", 0, 1, highLimit);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1841058617: /*lowLimit*/ return this.lowLimit == null ? new Base[0] : new Base[] {this.lowLimit}; // Quantity
        case -710757575: /*highLimit*/ return this.highLimit == null ? new Base[0] : new Base[] {this.highLimit}; // Quantity
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1841058617: // lowLimit
          this.lowLimit = castToQuantity(value); // Quantity
          return value;
        case -710757575: // highLimit
          this.highLimit = castToQuantity(value); // Quantity
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("lowLimit")) {
          this.lowLimit = castToQuantity(value); // Quantity
        } else if (name.equals("highLimit")) {
          this.highLimit = castToQuantity(value); // Quantity
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1841058617:  return getLowLimit(); 
        case -710757575:  return getHighLimit(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1841058617: /*lowLimit*/ return new String[] {"Quantity"};
        case -710757575: /*highLimit*/ return new String[] {"Quantity"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("lowLimit")) {
          this.lowLimit = new Quantity();
          return this.lowLimit;
        }
        else if (name.equals("highLimit")) {
          this.highLimit = new Quantity();
          return this.highLimit;
        }
        else
          return super.addChild(name);
      }

      public SubstanceAmountReferenceRangeComponent copy() {
        SubstanceAmountReferenceRangeComponent dst = new SubstanceAmountReferenceRangeComponent();
        copyValues(dst);
        dst.lowLimit = lowLimit == null ? null : lowLimit.copy();
        dst.highLimit = highLimit == null ? null : highLimit.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof SubstanceAmountReferenceRangeComponent))
          return false;
        SubstanceAmountReferenceRangeComponent o = (SubstanceAmountReferenceRangeComponent) other_;
        return compareDeep(lowLimit, o.lowLimit, true) && compareDeep(highLimit, o.highLimit, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof SubstanceAmountReferenceRangeComponent))
          return false;
        SubstanceAmountReferenceRangeComponent o = (SubstanceAmountReferenceRangeComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(lowLimit, highLimit);
      }

  public String fhirType() {
    return "SubstanceAmount.referenceRange";

  }

  }

    /**
     * Used to capture quantitative values for a variety of elements. If only limits are given, the arithmetic mean would be the average. If only a single definite value for a given element is given, it would be captured in this field.
     */
    @Child(name = "amount", type = {Quantity.class, Range.class, StringType.class}, order=0, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Used to capture quantitative values for a variety of elements. If only limits are given, the arithmetic mean would be the average. If only a single definite value for a given element is given, it would be captured in this field", formalDefinition="Used to capture quantitative values for a variety of elements. If only limits are given, the arithmetic mean would be the average. If only a single definite value for a given element is given, it would be captured in this field." )
    protected Type amount;

    /**
     * Most elements that require a quantitative value will also have a field called amount type. Amount type should always be specified because the actual value of the amount is often dependent on it. EXAMPLE: In capturing the actual relative amounts of substances or molecular fragments it is essential to indicate whether the amount refers to a mole ratio or weight ratio. For any given element an effort should be made to use same the amount type for all related definitional elements.
     */
    @Child(name = "amountType", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Most elements that require a quantitative value will also have a field called amount type. Amount type should always be specified because the actual value of the amount is often dependent on it. EXAMPLE: In capturing the actual relative amounts of substances or molecular fragments it is essential to indicate whether the amount refers to a mole ratio or weight ratio. For any given element an effort should be made to use same the amount type for all related definitional elements", formalDefinition="Most elements that require a quantitative value will also have a field called amount type. Amount type should always be specified because the actual value of the amount is often dependent on it. EXAMPLE: In capturing the actual relative amounts of substances or molecular fragments it is essential to indicate whether the amount refers to a mole ratio or weight ratio. For any given element an effort should be made to use same the amount type for all related definitional elements." )
    protected CodeableConcept amountType;

    /**
     * A textual comment on a numeric value.
     */
    @Child(name = "amountText", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="A textual comment on a numeric value", formalDefinition="A textual comment on a numeric value." )
    protected StringType amountText;

    /**
     * Reference range of possible or expected values.
     */
    @Child(name = "referenceRange", type = {}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Reference range of possible or expected values", formalDefinition="Reference range of possible or expected values." )
    protected SubstanceAmountReferenceRangeComponent referenceRange;

    private static final long serialVersionUID = -174997548L;

  /**
   * Constructor
   */
    public SubstanceAmount() {
      super();
    }

    /**
     * @return {@link #amount} (Used to capture quantitative values for a variety of elements. If only limits are given, the arithmetic mean would be the average. If only a single definite value for a given element is given, it would be captured in this field.)
     */
    public Type getAmount() { 
      return this.amount;
    }

    /**
     * @return {@link #amount} (Used to capture quantitative values for a variety of elements. If only limits are given, the arithmetic mean would be the average. If only a single definite value for a given element is given, it would be captured in this field.)
     */
    public Quantity getAmountQuantity() throws FHIRException { 
      if (this.amount == null)
        return null;
      if (!(this.amount instanceof Quantity))
        throw new FHIRException("Type mismatch: the type Quantity was expected, but "+this.amount.getClass().getName()+" was encountered");
      return (Quantity) this.amount;
    }

    public boolean hasAmountQuantity() { 
      return this != null && this.amount instanceof Quantity;
    }

    /**
     * @return {@link #amount} (Used to capture quantitative values for a variety of elements. If only limits are given, the arithmetic mean would be the average. If only a single definite value for a given element is given, it would be captured in this field.)
     */
    public Range getAmountRange() throws FHIRException { 
      if (this.amount == null)
        return null;
      if (!(this.amount instanceof Range))
        throw new FHIRException("Type mismatch: the type Range was expected, but "+this.amount.getClass().getName()+" was encountered");
      return (Range) this.amount;
    }

    public boolean hasAmountRange() { 
      return this != null && this.amount instanceof Range;
    }

    /**
     * @return {@link #amount} (Used to capture quantitative values for a variety of elements. If only limits are given, the arithmetic mean would be the average. If only a single definite value for a given element is given, it would be captured in this field.)
     */
    public StringType getAmountStringType() throws FHIRException { 
      if (this.amount == null)
        return null;
      if (!(this.amount instanceof StringType))
        throw new FHIRException("Type mismatch: the type StringType was expected, but "+this.amount.getClass().getName()+" was encountered");
      return (StringType) this.amount;
    }

    public boolean hasAmountStringType() { 
      return this != null && this.amount instanceof StringType;
    }

    public boolean hasAmount() { 
      return this.amount != null && !this.amount.isEmpty();
    }

    /**
     * @param value {@link #amount} (Used to capture quantitative values for a variety of elements. If only limits are given, the arithmetic mean would be the average. If only a single definite value for a given element is given, it would be captured in this field.)
     */
    public SubstanceAmount setAmount(Type value) { 
      if (value != null && !(value instanceof Quantity || value instanceof Range || value instanceof StringType))
        throw new Error("Not the right type for SubstanceAmount.amount[x]: "+value.fhirType());
      this.amount = value;
      return this;
    }

    /**
     * @return {@link #amountType} (Most elements that require a quantitative value will also have a field called amount type. Amount type should always be specified because the actual value of the amount is often dependent on it. EXAMPLE: In capturing the actual relative amounts of substances or molecular fragments it is essential to indicate whether the amount refers to a mole ratio or weight ratio. For any given element an effort should be made to use same the amount type for all related definitional elements.)
     */
    public CodeableConcept getAmountType() { 
      if (this.amountType == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SubstanceAmount.amountType");
        else if (Configuration.doAutoCreate())
          this.amountType = new CodeableConcept(); // cc
      return this.amountType;
    }

    public boolean hasAmountType() { 
      return this.amountType != null && !this.amountType.isEmpty();
    }

    /**
     * @param value {@link #amountType} (Most elements that require a quantitative value will also have a field called amount type. Amount type should always be specified because the actual value of the amount is often dependent on it. EXAMPLE: In capturing the actual relative amounts of substances or molecular fragments it is essential to indicate whether the amount refers to a mole ratio or weight ratio. For any given element an effort should be made to use same the amount type for all related definitional elements.)
     */
    public SubstanceAmount setAmountType(CodeableConcept value) { 
      this.amountType = value;
      return this;
    }

    /**
     * @return {@link #amountText} (A textual comment on a numeric value.). This is the underlying object with id, value and extensions. The accessor "getAmountText" gives direct access to the value
     */
    public StringType getAmountTextElement() { 
      if (this.amountText == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SubstanceAmount.amountText");
        else if (Configuration.doAutoCreate())
          this.amountText = new StringType(); // bb
      return this.amountText;
    }

    public boolean hasAmountTextElement() { 
      return this.amountText != null && !this.amountText.isEmpty();
    }

    public boolean hasAmountText() { 
      return this.amountText != null && !this.amountText.isEmpty();
    }

    /**
     * @param value {@link #amountText} (A textual comment on a numeric value.). This is the underlying object with id, value and extensions. The accessor "getAmountText" gives direct access to the value
     */
    public SubstanceAmount setAmountTextElement(StringType value) { 
      this.amountText = value;
      return this;
    }

    /**
     * @return A textual comment on a numeric value.
     */
    public String getAmountText() { 
      return this.amountText == null ? null : this.amountText.getValue();
    }

    /**
     * @param value A textual comment on a numeric value.
     */
    public SubstanceAmount setAmountText(String value) { 
      if (Utilities.noString(value))
        this.amountText = null;
      else {
        if (this.amountText == null)
          this.amountText = new StringType();
        this.amountText.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #referenceRange} (Reference range of possible or expected values.)
     */
    public SubstanceAmountReferenceRangeComponent getReferenceRange() { 
      if (this.referenceRange == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SubstanceAmount.referenceRange");
        else if (Configuration.doAutoCreate())
          this.referenceRange = new SubstanceAmountReferenceRangeComponent(); // cc
      return this.referenceRange;
    }

    public boolean hasReferenceRange() { 
      return this.referenceRange != null && !this.referenceRange.isEmpty();
    }

    /**
     * @param value {@link #referenceRange} (Reference range of possible or expected values.)
     */
    public SubstanceAmount setReferenceRange(SubstanceAmountReferenceRangeComponent value) { 
      this.referenceRange = value;
      return this;
    }

      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("amount[x]", "Quantity|Range|string", "Used to capture quantitative values for a variety of elements. If only limits are given, the arithmetic mean would be the average. If only a single definite value for a given element is given, it would be captured in this field.", 0, 1, amount));
        children.add(new Property("amountType", "CodeableConcept", "Most elements that require a quantitative value will also have a field called amount type. Amount type should always be specified because the actual value of the amount is often dependent on it. EXAMPLE: In capturing the actual relative amounts of substances or molecular fragments it is essential to indicate whether the amount refers to a mole ratio or weight ratio. For any given element an effort should be made to use same the amount type for all related definitional elements.", 0, 1, amountType));
        children.add(new Property("amountText", "string", "A textual comment on a numeric value.", 0, 1, amountText));
        children.add(new Property("referenceRange", "", "Reference range of possible or expected values.", 0, 1, referenceRange));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case 646780200: /*amount[x]*/  return new Property("amount[x]", "Quantity|Range|string", "Used to capture quantitative values for a variety of elements. If only limits are given, the arithmetic mean would be the average. If only a single definite value for a given element is given, it would be captured in this field.", 0, 1, amount);
        case -1413853096: /*amount*/  return new Property("amount[x]", "Quantity|Range|string", "Used to capture quantitative values for a variety of elements. If only limits are given, the arithmetic mean would be the average. If only a single definite value for a given element is given, it would be captured in this field.", 0, 1, amount);
        case 1664303363: /*amountQuantity*/  return new Property("amount[x]", "Quantity|Range|string", "Used to capture quantitative values for a variety of elements. If only limits are given, the arithmetic mean would be the average. If only a single definite value for a given element is given, it would be captured in this field.", 0, 1, amount);
        case -1223462971: /*amountRange*/  return new Property("amount[x]", "Quantity|Range|string", "Used to capture quantitative values for a variety of elements. If only limits are given, the arithmetic mean would be the average. If only a single definite value for a given element is given, it would be captured in this field.", 0, 1, amount);
        case 773651081: /*amountString*/  return new Property("amount[x]", "Quantity|Range|string", "Used to capture quantitative values for a variety of elements. If only limits are given, the arithmetic mean would be the average. If only a single definite value for a given element is given, it would be captured in this field.", 0, 1, amount);
        case -1424857166: /*amountType*/  return new Property("amountType", "CodeableConcept", "Most elements that require a quantitative value will also have a field called amount type. Amount type should always be specified because the actual value of the amount is often dependent on it. EXAMPLE: In capturing the actual relative amounts of substances or molecular fragments it is essential to indicate whether the amount refers to a mole ratio or weight ratio. For any given element an effort should be made to use same the amount type for all related definitional elements.", 0, 1, amountType);
        case -1424876123: /*amountText*/  return new Property("amountText", "string", "A textual comment on a numeric value.", 0, 1, amountText);
        case -1912545102: /*referenceRange*/  return new Property("referenceRange", "", "Reference range of possible or expected values.", 0, 1, referenceRange);
        default: return super.getNamedProperty(_hash, _name, _checkValid);
        }

      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1413853096: /*amount*/ return this.amount == null ? new Base[0] : new Base[] {this.amount}; // Type
        case -1424857166: /*amountType*/ return this.amountType == null ? new Base[0] : new Base[] {this.amountType}; // CodeableConcept
        case -1424876123: /*amountText*/ return this.amountText == null ? new Base[0] : new Base[] {this.amountText}; // StringType
        case -1912545102: /*referenceRange*/ return this.referenceRange == null ? new Base[0] : new Base[] {this.referenceRange}; // SubstanceAmountReferenceRangeComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1413853096: // amount
          this.amount = castToType(value); // Type
          return value;
        case -1424857166: // amountType
          this.amountType = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1424876123: // amountText
          this.amountText = castToString(value); // StringType
          return value;
        case -1912545102: // referenceRange
          this.referenceRange = (SubstanceAmountReferenceRangeComponent) value; // SubstanceAmountReferenceRangeComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("amount[x]")) {
          this.amount = castToType(value); // Type
        } else if (name.equals("amountType")) {
          this.amountType = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("amountText")) {
          this.amountText = castToString(value); // StringType
        } else if (name.equals("referenceRange")) {
          this.referenceRange = (SubstanceAmountReferenceRangeComponent) value; // SubstanceAmountReferenceRangeComponent
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 646780200:  return getAmount(); 
        case -1413853096:  return getAmount(); 
        case -1424857166:  return getAmountType(); 
        case -1424876123:  return getAmountTextElement();
        case -1912545102:  return getReferenceRange(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1413853096: /*amount*/ return new String[] {"Quantity", "Range", "string"};
        case -1424857166: /*amountType*/ return new String[] {"CodeableConcept"};
        case -1424876123: /*amountText*/ return new String[] {"string"};
        case -1912545102: /*referenceRange*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("amountQuantity")) {
          this.amount = new Quantity();
          return this.amount;
        }
        else if (name.equals("amountRange")) {
          this.amount = new Range();
          return this.amount;
        }
        else if (name.equals("amountString")) {
          this.amount = new StringType();
          return this.amount;
        }
        else if (name.equals("amountType")) {
          this.amountType = new CodeableConcept();
          return this.amountType;
        }
        else if (name.equals("amountText")) {
          throw new FHIRException("Cannot call addChild on a primitive type SubstanceAmount.amountText");
        }
        else if (name.equals("referenceRange")) {
          this.referenceRange = new SubstanceAmountReferenceRangeComponent();
          return this.referenceRange;
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "SubstanceAmount";

  }

      public SubstanceAmount copy() {
        SubstanceAmount dst = new SubstanceAmount();
        copyValues(dst);
        dst.amount = amount == null ? null : amount.copy();
        dst.amountType = amountType == null ? null : amountType.copy();
        dst.amountText = amountText == null ? null : amountText.copy();
        dst.referenceRange = referenceRange == null ? null : referenceRange.copy();
        return dst;
      }

      protected SubstanceAmount typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof SubstanceAmount))
          return false;
        SubstanceAmount o = (SubstanceAmount) other_;
        return compareDeep(amount, o.amount, true) && compareDeep(amountType, o.amountType, true) && compareDeep(amountText, o.amountText, true)
           && compareDeep(referenceRange, o.referenceRange, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof SubstanceAmount))
          return false;
        SubstanceAmount o = (SubstanceAmount) other_;
        return compareValues(amountText, o.amountText, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(amount, amountType, amountText
          , referenceRange);
      }


}

