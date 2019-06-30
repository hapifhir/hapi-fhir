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

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ChildOrder;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.Block;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.exceptions.FHIRException;
/**
 * The shelf-life and storage information for a medicinal product item or container can be described using this class.
 */
@DatatypeDef(name="ProductShelfLife")
public class ProductShelfLife extends BackboneType implements ICompositeType {

    /**
     * Unique identifier for the packaged Medicinal Product.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Unique identifier for the packaged Medicinal Product", formalDefinition="Unique identifier for the packaged Medicinal Product." )
    protected Identifier identifier;

    /**
     * This describes the shelf life, taking into account various scenarios such as shelf life of the packaged Medicinal Product itself, shelf life after transformation where necessary and shelf life after the first opening of a bottle, etc. The shelf life type shall be specified using an appropriate controlled vocabulary The controlled term and the controlled term identifier shall be specified.
     */
    @Child(name = "type", type = {CodeableConcept.class}, order=1, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="This describes the shelf life, taking into account various scenarios such as shelf life of the packaged Medicinal Product itself, shelf life after transformation where necessary and shelf life after the first opening of a bottle, etc. The shelf life type shall be specified using an appropriate controlled vocabulary The controlled term and the controlled term identifier shall be specified", formalDefinition="This describes the shelf life, taking into account various scenarios such as shelf life of the packaged Medicinal Product itself, shelf life after transformation where necessary and shelf life after the first opening of a bottle, etc. The shelf life type shall be specified using an appropriate controlled vocabulary The controlled term and the controlled term identifier shall be specified." )
    protected CodeableConcept type;

    /**
     * The shelf life time period can be specified using a numerical value for the period of time and its unit of time measurement The unit of measurement shall be specified in accordance with ISO 11240 and the resulting terminology The symbol and the symbol identifier shall be used.
     */
    @Child(name = "period", type = {Quantity.class}, order=2, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The shelf life time period can be specified using a numerical value for the period of time and its unit of time measurement The unit of measurement shall be specified in accordance with ISO 11240 and the resulting terminology The symbol and the symbol identifier shall be used", formalDefinition="The shelf life time period can be specified using a numerical value for the period of time and its unit of time measurement The unit of measurement shall be specified in accordance with ISO 11240 and the resulting terminology The symbol and the symbol identifier shall be used." )
    protected Quantity period;

    /**
     * Special precautions for storage, if any, can be specified using an appropriate controlled vocabulary The controlled term and the controlled term identifier shall be specified.
     */
    @Child(name = "specialPrecautionsForStorage", type = {CodeableConcept.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Special precautions for storage, if any, can be specified using an appropriate controlled vocabulary The controlled term and the controlled term identifier shall be specified", formalDefinition="Special precautions for storage, if any, can be specified using an appropriate controlled vocabulary The controlled term and the controlled term identifier shall be specified." )
    protected List<CodeableConcept> specialPrecautionsForStorage;

    private static final long serialVersionUID = -1561196410L;

  /**
   * Constructor
   */
    public ProductShelfLife() {
      super();
    }

  /**
   * Constructor
   */
    public ProductShelfLife(CodeableConcept type, Quantity period) {
      super();
      this.type = type;
      this.period = period;
    }

    /**
     * @return {@link #identifier} (Unique identifier for the packaged Medicinal Product.)
     */
    public Identifier getIdentifier() { 
      if (this.identifier == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ProductShelfLife.identifier");
        else if (Configuration.doAutoCreate())
          this.identifier = new Identifier(); // cc
      return this.identifier;
    }

    public boolean hasIdentifier() { 
      return this.identifier != null && !this.identifier.isEmpty();
    }

    /**
     * @param value {@link #identifier} (Unique identifier for the packaged Medicinal Product.)
     */
    public ProductShelfLife setIdentifier(Identifier value) { 
      this.identifier = value;
      return this;
    }

    /**
     * @return {@link #type} (This describes the shelf life, taking into account various scenarios such as shelf life of the packaged Medicinal Product itself, shelf life after transformation where necessary and shelf life after the first opening of a bottle, etc. The shelf life type shall be specified using an appropriate controlled vocabulary The controlled term and the controlled term identifier shall be specified.)
     */
    public CodeableConcept getType() { 
      if (this.type == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ProductShelfLife.type");
        else if (Configuration.doAutoCreate())
          this.type = new CodeableConcept(); // cc
      return this.type;
    }

    public boolean hasType() { 
      return this.type != null && !this.type.isEmpty();
    }

    /**
     * @param value {@link #type} (This describes the shelf life, taking into account various scenarios such as shelf life of the packaged Medicinal Product itself, shelf life after transformation where necessary and shelf life after the first opening of a bottle, etc. The shelf life type shall be specified using an appropriate controlled vocabulary The controlled term and the controlled term identifier shall be specified.)
     */
    public ProductShelfLife setType(CodeableConcept value) { 
      this.type = value;
      return this;
    }

    /**
     * @return {@link #period} (The shelf life time period can be specified using a numerical value for the period of time and its unit of time measurement The unit of measurement shall be specified in accordance with ISO 11240 and the resulting terminology The symbol and the symbol identifier shall be used.)
     */
    public Quantity getPeriod() { 
      if (this.period == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ProductShelfLife.period");
        else if (Configuration.doAutoCreate())
          this.period = new Quantity(); // cc
      return this.period;
    }

    public boolean hasPeriod() { 
      return this.period != null && !this.period.isEmpty();
    }

    /**
     * @param value {@link #period} (The shelf life time period can be specified using a numerical value for the period of time and its unit of time measurement The unit of measurement shall be specified in accordance with ISO 11240 and the resulting terminology The symbol and the symbol identifier shall be used.)
     */
    public ProductShelfLife setPeriod(Quantity value) { 
      this.period = value;
      return this;
    }

    /**
     * @return {@link #specialPrecautionsForStorage} (Special precautions for storage, if any, can be specified using an appropriate controlled vocabulary The controlled term and the controlled term identifier shall be specified.)
     */
    public List<CodeableConcept> getSpecialPrecautionsForStorage() { 
      if (this.specialPrecautionsForStorage == null)
        this.specialPrecautionsForStorage = new ArrayList<CodeableConcept>();
      return this.specialPrecautionsForStorage;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ProductShelfLife setSpecialPrecautionsForStorage(List<CodeableConcept> theSpecialPrecautionsForStorage) { 
      this.specialPrecautionsForStorage = theSpecialPrecautionsForStorage;
      return this;
    }

    public boolean hasSpecialPrecautionsForStorage() { 
      if (this.specialPrecautionsForStorage == null)
        return false;
      for (CodeableConcept item : this.specialPrecautionsForStorage)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addSpecialPrecautionsForStorage() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.specialPrecautionsForStorage == null)
        this.specialPrecautionsForStorage = new ArrayList<CodeableConcept>();
      this.specialPrecautionsForStorage.add(t);
      return t;
    }

    public ProductShelfLife addSpecialPrecautionsForStorage(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.specialPrecautionsForStorage == null)
        this.specialPrecautionsForStorage = new ArrayList<CodeableConcept>();
      this.specialPrecautionsForStorage.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #specialPrecautionsForStorage}, creating it if it does not already exist
     */
    public CodeableConcept getSpecialPrecautionsForStorageFirstRep() { 
      if (getSpecialPrecautionsForStorage().isEmpty()) {
        addSpecialPrecautionsForStorage();
      }
      return getSpecialPrecautionsForStorage().get(0);
    }

      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("identifier", "Identifier", "Unique identifier for the packaged Medicinal Product.", 0, 1, identifier));
        children.add(new Property("type", "CodeableConcept", "This describes the shelf life, taking into account various scenarios such as shelf life of the packaged Medicinal Product itself, shelf life after transformation where necessary and shelf life after the first opening of a bottle, etc. The shelf life type shall be specified using an appropriate controlled vocabulary The controlled term and the controlled term identifier shall be specified.", 0, 1, type));
        children.add(new Property("period", "Quantity", "The shelf life time period can be specified using a numerical value for the period of time and its unit of time measurement The unit of measurement shall be specified in accordance with ISO 11240 and the resulting terminology The symbol and the symbol identifier shall be used.", 0, 1, period));
        children.add(new Property("specialPrecautionsForStorage", "CodeableConcept", "Special precautions for storage, if any, can be specified using an appropriate controlled vocabulary The controlled term and the controlled term identifier shall be specified.", 0, java.lang.Integer.MAX_VALUE, specialPrecautionsForStorage));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case -1618432855: /*identifier*/  return new Property("identifier", "Identifier", "Unique identifier for the packaged Medicinal Product.", 0, 1, identifier);
        case 3575610: /*type*/  return new Property("type", "CodeableConcept", "This describes the shelf life, taking into account various scenarios such as shelf life of the packaged Medicinal Product itself, shelf life after transformation where necessary and shelf life after the first opening of a bottle, etc. The shelf life type shall be specified using an appropriate controlled vocabulary The controlled term and the controlled term identifier shall be specified.", 0, 1, type);
        case -991726143: /*period*/  return new Property("period", "Quantity", "The shelf life time period can be specified using a numerical value for the period of time and its unit of time measurement The unit of measurement shall be specified in accordance with ISO 11240 and the resulting terminology The symbol and the symbol identifier shall be used.", 0, 1, period);
        case 2103459492: /*specialPrecautionsForStorage*/  return new Property("specialPrecautionsForStorage", "CodeableConcept", "Special precautions for storage, if any, can be specified using an appropriate controlled vocabulary The controlled term and the controlled term identifier shall be specified.", 0, java.lang.Integer.MAX_VALUE, specialPrecautionsForStorage);
        default: return super.getNamedProperty(_hash, _name, _checkValid);
        }

      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : new Base[] {this.identifier}; // Identifier
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case -991726143: /*period*/ return this.period == null ? new Base[0] : new Base[] {this.period}; // Quantity
        case 2103459492: /*specialPrecautionsForStorage*/ return this.specialPrecautionsForStorage == null ? new Base[0] : this.specialPrecautionsForStorage.toArray(new Base[this.specialPrecautionsForStorage.size()]); // CodeableConcept
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.identifier = castToIdentifier(value); // Identifier
          return value;
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -991726143: // period
          this.period = castToQuantity(value); // Quantity
          return value;
        case 2103459492: // specialPrecautionsForStorage
          this.getSpecialPrecautionsForStorage().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier")) {
          this.identifier = castToIdentifier(value); // Identifier
        } else if (name.equals("type")) {
          this.type = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("period")) {
          this.period = castToQuantity(value); // Quantity
        } else if (name.equals("specialPrecautionsForStorage")) {
          this.getSpecialPrecautionsForStorage().add(castToCodeableConcept(value));
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return getIdentifier(); 
        case 3575610:  return getType(); 
        case -991726143:  return getPeriod(); 
        case 2103459492:  return addSpecialPrecautionsForStorage(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case -991726143: /*period*/ return new String[] {"Quantity"};
        case 2103459492: /*specialPrecautionsForStorage*/ return new String[] {"CodeableConcept"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          this.identifier = new Identifier();
          return this.identifier;
        }
        else if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("period")) {
          this.period = new Quantity();
          return this.period;
        }
        else if (name.equals("specialPrecautionsForStorage")) {
          return addSpecialPrecautionsForStorage();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "ProductShelfLife";

  }

      public ProductShelfLife copy() {
        ProductShelfLife dst = new ProductShelfLife();
        copyValues(dst);
        dst.identifier = identifier == null ? null : identifier.copy();
        dst.type = type == null ? null : type.copy();
        dst.period = period == null ? null : period.copy();
        if (specialPrecautionsForStorage != null) {
          dst.specialPrecautionsForStorage = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : specialPrecautionsForStorage)
            dst.specialPrecautionsForStorage.add(i.copy());
        };
        return dst;
      }

      protected ProductShelfLife typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ProductShelfLife))
          return false;
        ProductShelfLife o = (ProductShelfLife) other_;
        return compareDeep(identifier, o.identifier, true) && compareDeep(type, o.type, true) && compareDeep(period, o.period, true)
           && compareDeep(specialPrecautionsForStorage, o.specialPrecautionsForStorage, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ProductShelfLife))
          return false;
        ProductShelfLife o = (ProductShelfLife) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, type, period
          , specialPrecautionsForStorage);
      }


}

