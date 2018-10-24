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

import java.math.*;
import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.r4.model.Enumerations.*;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ChildOrder;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.Block;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.exceptions.FHIRException;
/**
 * An amount of economic utility in some recognized currency.
 */
@DatatypeDef(name="Money")
public class Money extends Type implements ICompositeType {

    /**
     * Numerical value (with implicit precision).
     */
    @Child(name = "value", type = {DecimalType.class}, order=0, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Numerical value (with implicit precision)", formalDefinition="Numerical value (with implicit precision)." )
    protected DecimalType value;

    /**
     * ISO 4217 Currency Code.
     */
    @Child(name = "currency", type = {CodeType.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="ISO 4217 Currency Code", formalDefinition="ISO 4217 Currency Code." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/currencies")
    protected CodeType currency;

    private static final long serialVersionUID = -484637938L;

  /**
   * Constructor
   */
    public Money() {
      super();
    }

    /**
     * @return {@link #value} (Numerical value (with implicit precision).). This is the underlying object with id, value and extensions. The accessor "getValue" gives direct access to the value
     */
    public DecimalType getValueElement() { 
      if (this.value == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Money.value");
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
     * @param value {@link #value} (Numerical value (with implicit precision).). This is the underlying object with id, value and extensions. The accessor "getValue" gives direct access to the value
     */
    public Money setValueElement(DecimalType value) { 
      this.value = value;
      return this;
    }

    /**
     * @return Numerical value (with implicit precision).
     */
    public BigDecimal getValue() { 
      return this.value == null ? null : this.value.getValue();
    }

    /**
     * @param value Numerical value (with implicit precision).
     */
    public Money setValue(BigDecimal value) { 
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
     * @param value Numerical value (with implicit precision).
     */
    public Money setValue(long value) { 
          this.value = new DecimalType();
        this.value.setValue(value);
      return this;
    }

    /**
     * @param value Numerical value (with implicit precision).
     */
    public Money setValue(double value) { 
          this.value = new DecimalType();
        this.value.setValue(value);
      return this;
    }

    /**
     * @return {@link #currency} (ISO 4217 Currency Code.). This is the underlying object with id, value and extensions. The accessor "getCurrency" gives direct access to the value
     */
    public CodeType getCurrencyElement() { 
      if (this.currency == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Money.currency");
        else if (Configuration.doAutoCreate())
          this.currency = new CodeType(); // bb
      return this.currency;
    }

    public boolean hasCurrencyElement() { 
      return this.currency != null && !this.currency.isEmpty();
    }

    public boolean hasCurrency() { 
      return this.currency != null && !this.currency.isEmpty();
    }

    /**
     * @param value {@link #currency} (ISO 4217 Currency Code.). This is the underlying object with id, value and extensions. The accessor "getCurrency" gives direct access to the value
     */
    public Money setCurrencyElement(CodeType value) { 
      this.currency = value;
      return this;
    }

    /**
     * @return ISO 4217 Currency Code.
     */
    public String getCurrency() { 
      return this.currency == null ? null : this.currency.getValue();
    }

    /**
     * @param value ISO 4217 Currency Code.
     */
    public Money setCurrency(String value) { 
      if (Utilities.noString(value))
        this.currency = null;
      else {
        if (this.currency == null)
          this.currency = new CodeType();
        this.currency.setValue(value);
      }
      return this;
    }

      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("value", "decimal", "Numerical value (with implicit precision).", 0, 1, value));
        children.add(new Property("currency", "code", "ISO 4217 Currency Code.", 0, 1, currency));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case 111972721: /*value*/  return new Property("value", "decimal", "Numerical value (with implicit precision).", 0, 1, value);
        case 575402001: /*currency*/  return new Property("currency", "code", "ISO 4217 Currency Code.", 0, 1, currency);
        default: return super.getNamedProperty(_hash, _name, _checkValid);
        }

      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 111972721: /*value*/ return this.value == null ? new Base[0] : new Base[] {this.value}; // DecimalType
        case 575402001: /*currency*/ return this.currency == null ? new Base[0] : new Base[] {this.currency}; // CodeType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 111972721: // value
          this.value = castToDecimal(value); // DecimalType
          return value;
        case 575402001: // currency
          this.currency = castToCode(value); // CodeType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("value")) {
          this.value = castToDecimal(value); // DecimalType
        } else if (name.equals("currency")) {
          this.currency = castToCode(value); // CodeType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 111972721:  return getValueElement();
        case 575402001:  return getCurrencyElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 111972721: /*value*/ return new String[] {"decimal"};
        case 575402001: /*currency*/ return new String[] {"code"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("value")) {
          throw new FHIRException("Cannot call addChild on a primitive type Money.value");
        }
        else if (name.equals("currency")) {
          throw new FHIRException("Cannot call addChild on a primitive type Money.currency");
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "Money";

  }

      public Money copy() {
        Money dst = new Money();
        copyValues(dst);
        dst.value = value == null ? null : value.copy();
        dst.currency = currency == null ? null : currency.copy();
        return dst;
      }

      protected Money typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof Money))
          return false;
        Money o = (Money) other_;
        return compareDeep(value, o.value, true) && compareDeep(currency, o.currency, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof Money))
          return false;
        Money o = (Money) other_;
        return compareValues(value, o.value, true) && compareValues(currency, o.currency, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(value, currency);
      }


}

