package org.hl7.fhir.dstu3.model;

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

// Generated on Mon, Apr 17, 2017 17:38-0400 for FHIR v3.0.1

import java.util.*;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ChildOrder;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.Block;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.exceptions.FHIRException;
/**
 * A relationship of two Quantity values - expressed as a numerator and a denominator.
 */
@DatatypeDef(name="Ratio")
public class Ratio extends Type implements ICompositeType {

    /**
     * The value of the numerator.
     */
    @Child(name = "numerator", type = {Quantity.class}, order=0, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Numerator value", formalDefinition="The value of the numerator." )
    protected Quantity numerator;

    /**
     * The value of the denominator.
     */
    @Child(name = "denominator", type = {Quantity.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Denominator value", formalDefinition="The value of the denominator." )
    protected Quantity denominator;

    private static final long serialVersionUID = 479922563L;

  /**
   * Constructor
   */
    public Ratio() {
      super();
    }

    /**
     * @return {@link #numerator} (The value of the numerator.)
     */
    public Quantity getNumerator() { 
      if (this.numerator == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Ratio.numerator");
        else if (Configuration.doAutoCreate())
          this.numerator = new Quantity(); // cc
      return this.numerator;
    }

    public boolean hasNumerator() { 
      return this.numerator != null && !this.numerator.isEmpty();
    }

    /**
     * @param value {@link #numerator} (The value of the numerator.)
     */
    public Ratio setNumerator(Quantity value) { 
      this.numerator = value;
      return this;
    }

    /**
     * @return {@link #denominator} (The value of the denominator.)
     */
    public Quantity getDenominator() { 
      if (this.denominator == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Ratio.denominator");
        else if (Configuration.doAutoCreate())
          this.denominator = new Quantity(); // cc
      return this.denominator;
    }

    public boolean hasDenominator() { 
      return this.denominator != null && !this.denominator.isEmpty();
    }

    /**
     * @param value {@link #denominator} (The value of the denominator.)
     */
    public Ratio setDenominator(Quantity value) { 
      this.denominator = value;
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("numerator", "Quantity", "The value of the numerator.", 0, java.lang.Integer.MAX_VALUE, numerator));
        childrenList.add(new Property("denominator", "Quantity", "The value of the denominator.", 0, java.lang.Integer.MAX_VALUE, denominator));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 1747334793: /*numerator*/ return this.numerator == null ? new Base[0] : new Base[] {this.numerator}; // Quantity
        case -1983274394: /*denominator*/ return this.denominator == null ? new Base[0] : new Base[] {this.denominator}; // Quantity
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 1747334793: // numerator
          this.numerator = castToQuantity(value); // Quantity
          return value;
        case -1983274394: // denominator
          this.denominator = castToQuantity(value); // Quantity
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("numerator")) {
          this.numerator = castToQuantity(value); // Quantity
        } else if (name.equals("denominator")) {
          this.denominator = castToQuantity(value); // Quantity
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1747334793:  return getNumerator(); 
        case -1983274394:  return getDenominator(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1747334793: /*numerator*/ return new String[] {"Quantity"};
        case -1983274394: /*denominator*/ return new String[] {"Quantity"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("numerator")) {
          this.numerator = new Quantity();
          return this.numerator;
        }
        else if (name.equals("denominator")) {
          this.denominator = new Quantity();
          return this.denominator;
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "Ratio";

  }

      public Ratio copy() {
        Ratio dst = new Ratio();
        copyValues(dst);
        dst.numerator = numerator == null ? null : numerator.copy();
        dst.denominator = denominator == null ? null : denominator.copy();
        return dst;
      }

      protected Ratio typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof Ratio))
          return false;
        Ratio o = (Ratio) other;
        return compareDeep(numerator, o.numerator, true) && compareDeep(denominator, o.denominator, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Ratio))
          return false;
        Ratio o = (Ratio) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(numerator, denominator);
      }


}

