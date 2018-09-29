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
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ChildOrder;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Block;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.exceptions.FHIRException;
/**
 * A material substance originating from a biological entity intended to be transplanted or infused
into another (possibly the same) biological entity.
 */
@ResourceDef(name="BiologicallyDerivedProduct", profile="http://hl7.org/fhir/Profile/BiologicallyDerivedProduct")
public class BiologicallyDerivedProduct extends DomainResource {

    public enum BiologicallyDerivedProductCategory {
        /**
         * A collection of tissues joined in a structural unit to serve a common function.
         */
        ORGAN, 
        /**
         * An ensemble of similar cells and their extracellular matrix from the same origin that together carry out a specific function.
         */
        TISSUE, 
        /**
         * Body fluid.
         */
        FLUID, 
        /**
         * Collection of cells.
         */
        CELLS, 
        /**
         * Biological agent of unspecified type.
         */
        BIOLOGICALAGENT, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static BiologicallyDerivedProductCategory fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("organ".equals(codeString))
          return ORGAN;
        if ("tissue".equals(codeString))
          return TISSUE;
        if ("fluid".equals(codeString))
          return FLUID;
        if ("cells".equals(codeString))
          return CELLS;
        if ("biologicalAgent".equals(codeString))
          return BIOLOGICALAGENT;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown BiologicallyDerivedProductCategory code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ORGAN: return "organ";
            case TISSUE: return "tissue";
            case FLUID: return "fluid";
            case CELLS: return "cells";
            case BIOLOGICALAGENT: return "biologicalAgent";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case ORGAN: return "http://hl7.org/fhir/product-category";
            case TISSUE: return "http://hl7.org/fhir/product-category";
            case FLUID: return "http://hl7.org/fhir/product-category";
            case CELLS: return "http://hl7.org/fhir/product-category";
            case BIOLOGICALAGENT: return "http://hl7.org/fhir/product-category";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case ORGAN: return "A collection of tissues joined in a structural unit to serve a common function.";
            case TISSUE: return "An ensemble of similar cells and their extracellular matrix from the same origin that together carry out a specific function.";
            case FLUID: return "Body fluid.";
            case CELLS: return "Collection of cells.";
            case BIOLOGICALAGENT: return "Biological agent of unspecified type.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ORGAN: return "Organ";
            case TISSUE: return "Tissue";
            case FLUID: return "Fluid";
            case CELLS: return "Cells";
            case BIOLOGICALAGENT: return "BiologicalAgent";
            default: return "?";
          }
        }
    }

  public static class BiologicallyDerivedProductCategoryEnumFactory implements EnumFactory<BiologicallyDerivedProductCategory> {
    public BiologicallyDerivedProductCategory fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("organ".equals(codeString))
          return BiologicallyDerivedProductCategory.ORGAN;
        if ("tissue".equals(codeString))
          return BiologicallyDerivedProductCategory.TISSUE;
        if ("fluid".equals(codeString))
          return BiologicallyDerivedProductCategory.FLUID;
        if ("cells".equals(codeString))
          return BiologicallyDerivedProductCategory.CELLS;
        if ("biologicalAgent".equals(codeString))
          return BiologicallyDerivedProductCategory.BIOLOGICALAGENT;
        throw new IllegalArgumentException("Unknown BiologicallyDerivedProductCategory code '"+codeString+"'");
        }
        public Enumeration<BiologicallyDerivedProductCategory> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<BiologicallyDerivedProductCategory>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("organ".equals(codeString))
          return new Enumeration<BiologicallyDerivedProductCategory>(this, BiologicallyDerivedProductCategory.ORGAN);
        if ("tissue".equals(codeString))
          return new Enumeration<BiologicallyDerivedProductCategory>(this, BiologicallyDerivedProductCategory.TISSUE);
        if ("fluid".equals(codeString))
          return new Enumeration<BiologicallyDerivedProductCategory>(this, BiologicallyDerivedProductCategory.FLUID);
        if ("cells".equals(codeString))
          return new Enumeration<BiologicallyDerivedProductCategory>(this, BiologicallyDerivedProductCategory.CELLS);
        if ("biologicalAgent".equals(codeString))
          return new Enumeration<BiologicallyDerivedProductCategory>(this, BiologicallyDerivedProductCategory.BIOLOGICALAGENT);
        throw new FHIRException("Unknown BiologicallyDerivedProductCategory code '"+codeString+"'");
        }
    public String toCode(BiologicallyDerivedProductCategory code) {
      if (code == BiologicallyDerivedProductCategory.ORGAN)
        return "organ";
      if (code == BiologicallyDerivedProductCategory.TISSUE)
        return "tissue";
      if (code == BiologicallyDerivedProductCategory.FLUID)
        return "fluid";
      if (code == BiologicallyDerivedProductCategory.CELLS)
        return "cells";
      if (code == BiologicallyDerivedProductCategory.BIOLOGICALAGENT)
        return "biologicalAgent";
      return "?";
      }
    public String toSystem(BiologicallyDerivedProductCategory code) {
      return code.getSystem();
      }
    }

    public enum BiologicallyDerivedProductStatus {
        /**
         * Product is currently available for use.
         */
        AVAILABLE, 
        /**
         * Product is not currently available for use.
         */
        UNAVAILABLE, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static BiologicallyDerivedProductStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("available".equals(codeString))
          return AVAILABLE;
        if ("unavailable".equals(codeString))
          return UNAVAILABLE;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown BiologicallyDerivedProductStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case AVAILABLE: return "available";
            case UNAVAILABLE: return "unavailable";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case AVAILABLE: return "http://hl7.org/fhir/product-status";
            case UNAVAILABLE: return "http://hl7.org/fhir/product-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case AVAILABLE: return "Product is currently available for use.";
            case UNAVAILABLE: return "Product is not currently available for use.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case AVAILABLE: return "Available";
            case UNAVAILABLE: return "Unavailable";
            default: return "?";
          }
        }
    }

  public static class BiologicallyDerivedProductStatusEnumFactory implements EnumFactory<BiologicallyDerivedProductStatus> {
    public BiologicallyDerivedProductStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("available".equals(codeString))
          return BiologicallyDerivedProductStatus.AVAILABLE;
        if ("unavailable".equals(codeString))
          return BiologicallyDerivedProductStatus.UNAVAILABLE;
        throw new IllegalArgumentException("Unknown BiologicallyDerivedProductStatus code '"+codeString+"'");
        }
        public Enumeration<BiologicallyDerivedProductStatus> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<BiologicallyDerivedProductStatus>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("available".equals(codeString))
          return new Enumeration<BiologicallyDerivedProductStatus>(this, BiologicallyDerivedProductStatus.AVAILABLE);
        if ("unavailable".equals(codeString))
          return new Enumeration<BiologicallyDerivedProductStatus>(this, BiologicallyDerivedProductStatus.UNAVAILABLE);
        throw new FHIRException("Unknown BiologicallyDerivedProductStatus code '"+codeString+"'");
        }
    public String toCode(BiologicallyDerivedProductStatus code) {
      if (code == BiologicallyDerivedProductStatus.AVAILABLE)
        return "available";
      if (code == BiologicallyDerivedProductStatus.UNAVAILABLE)
        return "unavailable";
      return "?";
      }
    public String toSystem(BiologicallyDerivedProductStatus code) {
      return code.getSystem();
      }
    }

    public enum BiologicallyDerivedProductStorageScale {
        /**
         * Fahrenheit temperature scale.
         */
        FARENHEIT, 
        /**
         * Celsius or centigrade temperature scale.
         */
        CELSIUS, 
        /**
         * Kelvin absolute thermodynamic temperature scale.
         */
        KELVIN, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static BiologicallyDerivedProductStorageScale fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("farenheit".equals(codeString))
          return FARENHEIT;
        if ("celsius".equals(codeString))
          return CELSIUS;
        if ("kelvin".equals(codeString))
          return KELVIN;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown BiologicallyDerivedProductStorageScale code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case FARENHEIT: return "farenheit";
            case CELSIUS: return "celsius";
            case KELVIN: return "kelvin";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case FARENHEIT: return "http://hl7.org/fhir/product-storage-scale";
            case CELSIUS: return "http://hl7.org/fhir/product-storage-scale";
            case KELVIN: return "http://hl7.org/fhir/product-storage-scale";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case FARENHEIT: return "Fahrenheit temperature scale.";
            case CELSIUS: return "Celsius or centigrade temperature scale.";
            case KELVIN: return "Kelvin absolute thermodynamic temperature scale.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case FARENHEIT: return "Fahrenheit";
            case CELSIUS: return "Celsius";
            case KELVIN: return "Kelvin";
            default: return "?";
          }
        }
    }

  public static class BiologicallyDerivedProductStorageScaleEnumFactory implements EnumFactory<BiologicallyDerivedProductStorageScale> {
    public BiologicallyDerivedProductStorageScale fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("farenheit".equals(codeString))
          return BiologicallyDerivedProductStorageScale.FARENHEIT;
        if ("celsius".equals(codeString))
          return BiologicallyDerivedProductStorageScale.CELSIUS;
        if ("kelvin".equals(codeString))
          return BiologicallyDerivedProductStorageScale.KELVIN;
        throw new IllegalArgumentException("Unknown BiologicallyDerivedProductStorageScale code '"+codeString+"'");
        }
        public Enumeration<BiologicallyDerivedProductStorageScale> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<BiologicallyDerivedProductStorageScale>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("farenheit".equals(codeString))
          return new Enumeration<BiologicallyDerivedProductStorageScale>(this, BiologicallyDerivedProductStorageScale.FARENHEIT);
        if ("celsius".equals(codeString))
          return new Enumeration<BiologicallyDerivedProductStorageScale>(this, BiologicallyDerivedProductStorageScale.CELSIUS);
        if ("kelvin".equals(codeString))
          return new Enumeration<BiologicallyDerivedProductStorageScale>(this, BiologicallyDerivedProductStorageScale.KELVIN);
        throw new FHIRException("Unknown BiologicallyDerivedProductStorageScale code '"+codeString+"'");
        }
    public String toCode(BiologicallyDerivedProductStorageScale code) {
      if (code == BiologicallyDerivedProductStorageScale.FARENHEIT)
        return "farenheit";
      if (code == BiologicallyDerivedProductStorageScale.CELSIUS)
        return "celsius";
      if (code == BiologicallyDerivedProductStorageScale.KELVIN)
        return "kelvin";
      return "?";
      }
    public String toSystem(BiologicallyDerivedProductStorageScale code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class BiologicallyDerivedProductCollectionComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * HealthCare Professional performing collection.
         */
        @Child(name = "collector", type = {Practitioner.class, PractitionerRole.class, Patient.class, RelatedPerson.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="HealthCare Professional performing collection", formalDefinition="HealthCare Professional performing collection." )
        protected Reference collector;

        /**
         * The actual object that is the target of the reference (HealthCare Professional performing collection.)
         */
        protected Resource collectorTarget;

        /**
         * Person or entity providing product.
         */
        @Child(name = "source", type = {Patient.class, Organization.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Person or entity providing product", formalDefinition="Person or entity providing product." )
        protected Reference source;

        /**
         * The actual object that is the target of the reference (Person or entity providing product.)
         */
        protected Resource sourceTarget;

        /**
         * Time of product collection.
         */
        @Child(name = "collected", type = {DateTimeType.class, Period.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Time of product collection", formalDefinition="Time of product collection." )
        protected Type collected;

        private static final long serialVersionUID = 892130089L;

    /**
     * Constructor
     */
      public BiologicallyDerivedProductCollectionComponent() {
        super();
      }

        /**
         * @return {@link #collector} (HealthCare Professional performing collection.)
         */
        public Reference getCollector() { 
          if (this.collector == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create BiologicallyDerivedProductCollectionComponent.collector");
            else if (Configuration.doAutoCreate())
              this.collector = new Reference(); // cc
          return this.collector;
        }

        public boolean hasCollector() { 
          return this.collector != null && !this.collector.isEmpty();
        }

        /**
         * @param value {@link #collector} (HealthCare Professional performing collection.)
         */
        public BiologicallyDerivedProductCollectionComponent setCollector(Reference value) { 
          this.collector = value;
          return this;
        }

        /**
         * @return {@link #collector} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (HealthCare Professional performing collection.)
         */
        public Resource getCollectorTarget() { 
          return this.collectorTarget;
        }

        /**
         * @param value {@link #collector} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (HealthCare Professional performing collection.)
         */
        public BiologicallyDerivedProductCollectionComponent setCollectorTarget(Resource value) { 
          this.collectorTarget = value;
          return this;
        }

        /**
         * @return {@link #source} (Person or entity providing product.)
         */
        public Reference getSource() { 
          if (this.source == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create BiologicallyDerivedProductCollectionComponent.source");
            else if (Configuration.doAutoCreate())
              this.source = new Reference(); // cc
          return this.source;
        }

        public boolean hasSource() { 
          return this.source != null && !this.source.isEmpty();
        }

        /**
         * @param value {@link #source} (Person or entity providing product.)
         */
        public BiologicallyDerivedProductCollectionComponent setSource(Reference value) { 
          this.source = value;
          return this;
        }

        /**
         * @return {@link #source} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Person or entity providing product.)
         */
        public Resource getSourceTarget() { 
          return this.sourceTarget;
        }

        /**
         * @param value {@link #source} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Person or entity providing product.)
         */
        public BiologicallyDerivedProductCollectionComponent setSourceTarget(Resource value) { 
          this.sourceTarget = value;
          return this;
        }

        /**
         * @return {@link #collected} (Time of product collection.)
         */
        public Type getCollected() { 
          return this.collected;
        }

        /**
         * @return {@link #collected} (Time of product collection.)
         */
        public DateTimeType getCollectedDateTimeType() throws FHIRException { 
          if (this.collected == null)
            return null;
          if (!(this.collected instanceof DateTimeType))
            throw new FHIRException("Type mismatch: the type DateTimeType was expected, but "+this.collected.getClass().getName()+" was encountered");
          return (DateTimeType) this.collected;
        }

        public boolean hasCollectedDateTimeType() { 
          return this != null && this.collected instanceof DateTimeType;
        }

        /**
         * @return {@link #collected} (Time of product collection.)
         */
        public Period getCollectedPeriod() throws FHIRException { 
          if (this.collected == null)
            return null;
          if (!(this.collected instanceof Period))
            throw new FHIRException("Type mismatch: the type Period was expected, but "+this.collected.getClass().getName()+" was encountered");
          return (Period) this.collected;
        }

        public boolean hasCollectedPeriod() { 
          return this != null && this.collected instanceof Period;
        }

        public boolean hasCollected() { 
          return this.collected != null && !this.collected.isEmpty();
        }

        /**
         * @param value {@link #collected} (Time of product collection.)
         */
        public BiologicallyDerivedProductCollectionComponent setCollected(Type value) { 
          if (value != null && !(value instanceof DateTimeType || value instanceof Period))
            throw new Error("Not the right type for BiologicallyDerivedProduct.collection.collected[x]: "+value.fhirType());
          this.collected = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("collector", "Reference(Practitioner|PractitionerRole|Patient|RelatedPerson)", "HealthCare Professional performing collection.", 0, 1, collector));
          children.add(new Property("source", "Reference(Patient|Organization)", "Person or entity providing product.", 0, 1, source));
          children.add(new Property("collected[x]", "dateTime|Period", "Time of product collection.", 0, 1, collected));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 1883491469: /*collector*/  return new Property("collector", "Reference(Practitioner|PractitionerRole|Patient|RelatedPerson)", "HealthCare Professional performing collection.", 0, 1, collector);
          case -896505829: /*source*/  return new Property("source", "Reference(Patient|Organization)", "Person or entity providing product.", 0, 1, source);
          case 1632037015: /*collected[x]*/  return new Property("collected[x]", "dateTime|Period", "Time of product collection.", 0, 1, collected);
          case 1883491145: /*collected*/  return new Property("collected[x]", "dateTime|Period", "Time of product collection.", 0, 1, collected);
          case 2005009924: /*collectedDateTime*/  return new Property("collected[x]", "dateTime|Period", "Time of product collection.", 0, 1, collected);
          case 653185642: /*collectedPeriod*/  return new Property("collected[x]", "dateTime|Period", "Time of product collection.", 0, 1, collected);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 1883491469: /*collector*/ return this.collector == null ? new Base[0] : new Base[] {this.collector}; // Reference
        case -896505829: /*source*/ return this.source == null ? new Base[0] : new Base[] {this.source}; // Reference
        case 1883491145: /*collected*/ return this.collected == null ? new Base[0] : new Base[] {this.collected}; // Type
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 1883491469: // collector
          this.collector = castToReference(value); // Reference
          return value;
        case -896505829: // source
          this.source = castToReference(value); // Reference
          return value;
        case 1883491145: // collected
          this.collected = castToType(value); // Type
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("collector")) {
          this.collector = castToReference(value); // Reference
        } else if (name.equals("source")) {
          this.source = castToReference(value); // Reference
        } else if (name.equals("collected[x]")) {
          this.collected = castToType(value); // Type
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1883491469:  return getCollector(); 
        case -896505829:  return getSource(); 
        case 1632037015:  return getCollected(); 
        case 1883491145:  return getCollected(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1883491469: /*collector*/ return new String[] {"Reference"};
        case -896505829: /*source*/ return new String[] {"Reference"};
        case 1883491145: /*collected*/ return new String[] {"dateTime", "Period"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("collector")) {
          this.collector = new Reference();
          return this.collector;
        }
        else if (name.equals("source")) {
          this.source = new Reference();
          return this.source;
        }
        else if (name.equals("collectedDateTime")) {
          this.collected = new DateTimeType();
          return this.collected;
        }
        else if (name.equals("collectedPeriod")) {
          this.collected = new Period();
          return this.collected;
        }
        else
          return super.addChild(name);
      }

      public BiologicallyDerivedProductCollectionComponent copy() {
        BiologicallyDerivedProductCollectionComponent dst = new BiologicallyDerivedProductCollectionComponent();
        copyValues(dst);
        dst.collector = collector == null ? null : collector.copy();
        dst.source = source == null ? null : source.copy();
        dst.collected = collected == null ? null : collected.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof BiologicallyDerivedProductCollectionComponent))
          return false;
        BiologicallyDerivedProductCollectionComponent o = (BiologicallyDerivedProductCollectionComponent) other_;
        return compareDeep(collector, o.collector, true) && compareDeep(source, o.source, true) && compareDeep(collected, o.collected, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof BiologicallyDerivedProductCollectionComponent))
          return false;
        BiologicallyDerivedProductCollectionComponent o = (BiologicallyDerivedProductCollectionComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(collector, source, collected
          );
      }

  public String fhirType() {
    return "BiologicallyDerivedProduct.collection";

  }

  }

    @Block()
    public static class BiologicallyDerivedProductProcessingComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Description of of processing.
         */
        @Child(name = "description", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Description of of processing", formalDefinition="Description of of processing." )
        protected StringType description;

        /**
         * Procesing code.
         */
        @Child(name = "procedure", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Procesing code", formalDefinition="Procesing code." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/procedure-code")
        protected CodeableConcept procedure;

        /**
         * Substance added during processing.
         */
        @Child(name = "additive", type = {Substance.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Substance added during processing", formalDefinition="Substance added during processing." )
        protected Reference additive;

        /**
         * The actual object that is the target of the reference (Substance added during processing.)
         */
        protected Substance additiveTarget;

        /**
         * Time of processing.
         */
        @Child(name = "time", type = {DateTimeType.class, Period.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Time of processing", formalDefinition="Time of processing." )
        protected Type time;

        private static final long serialVersionUID = -1007041216L;

    /**
     * Constructor
     */
      public BiologicallyDerivedProductProcessingComponent() {
        super();
      }

        /**
         * @return {@link #description} (Description of of processing.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public StringType getDescriptionElement() { 
          if (this.description == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create BiologicallyDerivedProductProcessingComponent.description");
            else if (Configuration.doAutoCreate())
              this.description = new StringType(); // bb
          return this.description;
        }

        public boolean hasDescriptionElement() { 
          return this.description != null && !this.description.isEmpty();
        }

        public boolean hasDescription() { 
          return this.description != null && !this.description.isEmpty();
        }

        /**
         * @param value {@link #description} (Description of of processing.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public BiologicallyDerivedProductProcessingComponent setDescriptionElement(StringType value) { 
          this.description = value;
          return this;
        }

        /**
         * @return Description of of processing.
         */
        public String getDescription() { 
          return this.description == null ? null : this.description.getValue();
        }

        /**
         * @param value Description of of processing.
         */
        public BiologicallyDerivedProductProcessingComponent setDescription(String value) { 
          if (Utilities.noString(value))
            this.description = null;
          else {
            if (this.description == null)
              this.description = new StringType();
            this.description.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #procedure} (Procesing code.)
         */
        public CodeableConcept getProcedure() { 
          if (this.procedure == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create BiologicallyDerivedProductProcessingComponent.procedure");
            else if (Configuration.doAutoCreate())
              this.procedure = new CodeableConcept(); // cc
          return this.procedure;
        }

        public boolean hasProcedure() { 
          return this.procedure != null && !this.procedure.isEmpty();
        }

        /**
         * @param value {@link #procedure} (Procesing code.)
         */
        public BiologicallyDerivedProductProcessingComponent setProcedure(CodeableConcept value) { 
          this.procedure = value;
          return this;
        }

        /**
         * @return {@link #additive} (Substance added during processing.)
         */
        public Reference getAdditive() { 
          if (this.additive == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create BiologicallyDerivedProductProcessingComponent.additive");
            else if (Configuration.doAutoCreate())
              this.additive = new Reference(); // cc
          return this.additive;
        }

        public boolean hasAdditive() { 
          return this.additive != null && !this.additive.isEmpty();
        }

        /**
         * @param value {@link #additive} (Substance added during processing.)
         */
        public BiologicallyDerivedProductProcessingComponent setAdditive(Reference value) { 
          this.additive = value;
          return this;
        }

        /**
         * @return {@link #additive} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Substance added during processing.)
         */
        public Substance getAdditiveTarget() { 
          if (this.additiveTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create BiologicallyDerivedProductProcessingComponent.additive");
            else if (Configuration.doAutoCreate())
              this.additiveTarget = new Substance(); // aa
          return this.additiveTarget;
        }

        /**
         * @param value {@link #additive} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Substance added during processing.)
         */
        public BiologicallyDerivedProductProcessingComponent setAdditiveTarget(Substance value) { 
          this.additiveTarget = value;
          return this;
        }

        /**
         * @return {@link #time} (Time of processing.)
         */
        public Type getTime() { 
          return this.time;
        }

        /**
         * @return {@link #time} (Time of processing.)
         */
        public DateTimeType getTimeDateTimeType() throws FHIRException { 
          if (this.time == null)
            return null;
          if (!(this.time instanceof DateTimeType))
            throw new FHIRException("Type mismatch: the type DateTimeType was expected, but "+this.time.getClass().getName()+" was encountered");
          return (DateTimeType) this.time;
        }

        public boolean hasTimeDateTimeType() { 
          return this != null && this.time instanceof DateTimeType;
        }

        /**
         * @return {@link #time} (Time of processing.)
         */
        public Period getTimePeriod() throws FHIRException { 
          if (this.time == null)
            return null;
          if (!(this.time instanceof Period))
            throw new FHIRException("Type mismatch: the type Period was expected, but "+this.time.getClass().getName()+" was encountered");
          return (Period) this.time;
        }

        public boolean hasTimePeriod() { 
          return this != null && this.time instanceof Period;
        }

        public boolean hasTime() { 
          return this.time != null && !this.time.isEmpty();
        }

        /**
         * @param value {@link #time} (Time of processing.)
         */
        public BiologicallyDerivedProductProcessingComponent setTime(Type value) { 
          if (value != null && !(value instanceof DateTimeType || value instanceof Period))
            throw new Error("Not the right type for BiologicallyDerivedProduct.processing.time[x]: "+value.fhirType());
          this.time = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("description", "string", "Description of of processing.", 0, 1, description));
          children.add(new Property("procedure", "CodeableConcept", "Procesing code.", 0, 1, procedure));
          children.add(new Property("additive", "Reference(Substance)", "Substance added during processing.", 0, 1, additive));
          children.add(new Property("time[x]", "dateTime|Period", "Time of processing.", 0, 1, time));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -1724546052: /*description*/  return new Property("description", "string", "Description of of processing.", 0, 1, description);
          case -1095204141: /*procedure*/  return new Property("procedure", "CodeableConcept", "Procesing code.", 0, 1, procedure);
          case -1226589236: /*additive*/  return new Property("additive", "Reference(Substance)", "Substance added during processing.", 0, 1, additive);
          case -1313930605: /*time[x]*/  return new Property("time[x]", "dateTime|Period", "Time of processing.", 0, 1, time);
          case 3560141: /*time*/  return new Property("time[x]", "dateTime|Period", "Time of processing.", 0, 1, time);
          case 2135345544: /*timeDateTime*/  return new Property("time[x]", "dateTime|Period", "Time of processing.", 0, 1, time);
          case 693544686: /*timePeriod*/  return new Property("time[x]", "dateTime|Period", "Time of processing.", 0, 1, time);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // StringType
        case -1095204141: /*procedure*/ return this.procedure == null ? new Base[0] : new Base[] {this.procedure}; // CodeableConcept
        case -1226589236: /*additive*/ return this.additive == null ? new Base[0] : new Base[] {this.additive}; // Reference
        case 3560141: /*time*/ return this.time == null ? new Base[0] : new Base[] {this.time}; // Type
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1724546052: // description
          this.description = castToString(value); // StringType
          return value;
        case -1095204141: // procedure
          this.procedure = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1226589236: // additive
          this.additive = castToReference(value); // Reference
          return value;
        case 3560141: // time
          this.time = castToType(value); // Type
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("description")) {
          this.description = castToString(value); // StringType
        } else if (name.equals("procedure")) {
          this.procedure = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("additive")) {
          this.additive = castToReference(value); // Reference
        } else if (name.equals("time[x]")) {
          this.time = castToType(value); // Type
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1724546052:  return getDescriptionElement();
        case -1095204141:  return getProcedure(); 
        case -1226589236:  return getAdditive(); 
        case -1313930605:  return getTime(); 
        case 3560141:  return getTime(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1724546052: /*description*/ return new String[] {"string"};
        case -1095204141: /*procedure*/ return new String[] {"CodeableConcept"};
        case -1226589236: /*additive*/ return new String[] {"Reference"};
        case 3560141: /*time*/ return new String[] {"dateTime", "Period"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type BiologicallyDerivedProduct.description");
        }
        else if (name.equals("procedure")) {
          this.procedure = new CodeableConcept();
          return this.procedure;
        }
        else if (name.equals("additive")) {
          this.additive = new Reference();
          return this.additive;
        }
        else if (name.equals("timeDateTime")) {
          this.time = new DateTimeType();
          return this.time;
        }
        else if (name.equals("timePeriod")) {
          this.time = new Period();
          return this.time;
        }
        else
          return super.addChild(name);
      }

      public BiologicallyDerivedProductProcessingComponent copy() {
        BiologicallyDerivedProductProcessingComponent dst = new BiologicallyDerivedProductProcessingComponent();
        copyValues(dst);
        dst.description = description == null ? null : description.copy();
        dst.procedure = procedure == null ? null : procedure.copy();
        dst.additive = additive == null ? null : additive.copy();
        dst.time = time == null ? null : time.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof BiologicallyDerivedProductProcessingComponent))
          return false;
        BiologicallyDerivedProductProcessingComponent o = (BiologicallyDerivedProductProcessingComponent) other_;
        return compareDeep(description, o.description, true) && compareDeep(procedure, o.procedure, true)
           && compareDeep(additive, o.additive, true) && compareDeep(time, o.time, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof BiologicallyDerivedProductProcessingComponent))
          return false;
        BiologicallyDerivedProductProcessingComponent o = (BiologicallyDerivedProductProcessingComponent) other_;
        return compareValues(description, o.description, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(description, procedure, additive
          , time);
      }

  public String fhirType() {
    return "BiologicallyDerivedProduct.processing";

  }

  }

    @Block()
    public static class BiologicallyDerivedProductManipulationComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Description of manipulation.
         */
        @Child(name = "description", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Description of manipulation", formalDefinition="Description of manipulation." )
        protected StringType description;

        /**
         * Time of manipulation.
         */
        @Child(name = "time", type = {DateTimeType.class, Period.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Time of manipulation", formalDefinition="Time of manipulation." )
        protected Type time;

        private static final long serialVersionUID = 717201078L;

    /**
     * Constructor
     */
      public BiologicallyDerivedProductManipulationComponent() {
        super();
      }

        /**
         * @return {@link #description} (Description of manipulation.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public StringType getDescriptionElement() { 
          if (this.description == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create BiologicallyDerivedProductManipulationComponent.description");
            else if (Configuration.doAutoCreate())
              this.description = new StringType(); // bb
          return this.description;
        }

        public boolean hasDescriptionElement() { 
          return this.description != null && !this.description.isEmpty();
        }

        public boolean hasDescription() { 
          return this.description != null && !this.description.isEmpty();
        }

        /**
         * @param value {@link #description} (Description of manipulation.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public BiologicallyDerivedProductManipulationComponent setDescriptionElement(StringType value) { 
          this.description = value;
          return this;
        }

        /**
         * @return Description of manipulation.
         */
        public String getDescription() { 
          return this.description == null ? null : this.description.getValue();
        }

        /**
         * @param value Description of manipulation.
         */
        public BiologicallyDerivedProductManipulationComponent setDescription(String value) { 
          if (Utilities.noString(value))
            this.description = null;
          else {
            if (this.description == null)
              this.description = new StringType();
            this.description.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #time} (Time of manipulation.)
         */
        public Type getTime() { 
          return this.time;
        }

        /**
         * @return {@link #time} (Time of manipulation.)
         */
        public DateTimeType getTimeDateTimeType() throws FHIRException { 
          if (this.time == null)
            return null;
          if (!(this.time instanceof DateTimeType))
            throw new FHIRException("Type mismatch: the type DateTimeType was expected, but "+this.time.getClass().getName()+" was encountered");
          return (DateTimeType) this.time;
        }

        public boolean hasTimeDateTimeType() { 
          return this != null && this.time instanceof DateTimeType;
        }

        /**
         * @return {@link #time} (Time of manipulation.)
         */
        public Period getTimePeriod() throws FHIRException { 
          if (this.time == null)
            return null;
          if (!(this.time instanceof Period))
            throw new FHIRException("Type mismatch: the type Period was expected, but "+this.time.getClass().getName()+" was encountered");
          return (Period) this.time;
        }

        public boolean hasTimePeriod() { 
          return this != null && this.time instanceof Period;
        }

        public boolean hasTime() { 
          return this.time != null && !this.time.isEmpty();
        }

        /**
         * @param value {@link #time} (Time of manipulation.)
         */
        public BiologicallyDerivedProductManipulationComponent setTime(Type value) { 
          if (value != null && !(value instanceof DateTimeType || value instanceof Period))
            throw new Error("Not the right type for BiologicallyDerivedProduct.manipulation.time[x]: "+value.fhirType());
          this.time = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("description", "string", "Description of manipulation.", 0, 1, description));
          children.add(new Property("time[x]", "dateTime|Period", "Time of manipulation.", 0, 1, time));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -1724546052: /*description*/  return new Property("description", "string", "Description of manipulation.", 0, 1, description);
          case -1313930605: /*time[x]*/  return new Property("time[x]", "dateTime|Period", "Time of manipulation.", 0, 1, time);
          case 3560141: /*time*/  return new Property("time[x]", "dateTime|Period", "Time of manipulation.", 0, 1, time);
          case 2135345544: /*timeDateTime*/  return new Property("time[x]", "dateTime|Period", "Time of manipulation.", 0, 1, time);
          case 693544686: /*timePeriod*/  return new Property("time[x]", "dateTime|Period", "Time of manipulation.", 0, 1, time);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // StringType
        case 3560141: /*time*/ return this.time == null ? new Base[0] : new Base[] {this.time}; // Type
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1724546052: // description
          this.description = castToString(value); // StringType
          return value;
        case 3560141: // time
          this.time = castToType(value); // Type
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("description")) {
          this.description = castToString(value); // StringType
        } else if (name.equals("time[x]")) {
          this.time = castToType(value); // Type
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1724546052:  return getDescriptionElement();
        case -1313930605:  return getTime(); 
        case 3560141:  return getTime(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1724546052: /*description*/ return new String[] {"string"};
        case 3560141: /*time*/ return new String[] {"dateTime", "Period"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type BiologicallyDerivedProduct.description");
        }
        else if (name.equals("timeDateTime")) {
          this.time = new DateTimeType();
          return this.time;
        }
        else if (name.equals("timePeriod")) {
          this.time = new Period();
          return this.time;
        }
        else
          return super.addChild(name);
      }

      public BiologicallyDerivedProductManipulationComponent copy() {
        BiologicallyDerivedProductManipulationComponent dst = new BiologicallyDerivedProductManipulationComponent();
        copyValues(dst);
        dst.description = description == null ? null : description.copy();
        dst.time = time == null ? null : time.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof BiologicallyDerivedProductManipulationComponent))
          return false;
        BiologicallyDerivedProductManipulationComponent o = (BiologicallyDerivedProductManipulationComponent) other_;
        return compareDeep(description, o.description, true) && compareDeep(time, o.time, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof BiologicallyDerivedProductManipulationComponent))
          return false;
        BiologicallyDerivedProductManipulationComponent o = (BiologicallyDerivedProductManipulationComponent) other_;
        return compareValues(description, o.description, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(description, time);
      }

  public String fhirType() {
    return "BiologicallyDerivedProduct.manipulation";

  }

  }

    @Block()
    public static class BiologicallyDerivedProductStorageComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Description of storage.
         */
        @Child(name = "description", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Description of storage", formalDefinition="Description of storage." )
        protected StringType description;

        /**
         * Storage temperature.
         */
        @Child(name = "temperature", type = {DecimalType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Storage temperature", formalDefinition="Storage temperature." )
        protected DecimalType temperature;

        /**
         * Temperature scale used.
         */
        @Child(name = "scale", type = {CodeType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="farenheit | celsius | kelvin", formalDefinition="Temperature scale used." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/product-storage-scale")
        protected Enumeration<BiologicallyDerivedProductStorageScale> scale;

        /**
         * Storage timeperiod.
         */
        @Child(name = "duration", type = {Period.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Storage timeperiod", formalDefinition="Storage timeperiod." )
        protected Period duration;

        private static final long serialVersionUID = 1509141319L;

    /**
     * Constructor
     */
      public BiologicallyDerivedProductStorageComponent() {
        super();
      }

        /**
         * @return {@link #description} (Description of storage.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public StringType getDescriptionElement() { 
          if (this.description == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create BiologicallyDerivedProductStorageComponent.description");
            else if (Configuration.doAutoCreate())
              this.description = new StringType(); // bb
          return this.description;
        }

        public boolean hasDescriptionElement() { 
          return this.description != null && !this.description.isEmpty();
        }

        public boolean hasDescription() { 
          return this.description != null && !this.description.isEmpty();
        }

        /**
         * @param value {@link #description} (Description of storage.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public BiologicallyDerivedProductStorageComponent setDescriptionElement(StringType value) { 
          this.description = value;
          return this;
        }

        /**
         * @return Description of storage.
         */
        public String getDescription() { 
          return this.description == null ? null : this.description.getValue();
        }

        /**
         * @param value Description of storage.
         */
        public BiologicallyDerivedProductStorageComponent setDescription(String value) { 
          if (Utilities.noString(value))
            this.description = null;
          else {
            if (this.description == null)
              this.description = new StringType();
            this.description.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #temperature} (Storage temperature.). This is the underlying object with id, value and extensions. The accessor "getTemperature" gives direct access to the value
         */
        public DecimalType getTemperatureElement() { 
          if (this.temperature == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create BiologicallyDerivedProductStorageComponent.temperature");
            else if (Configuration.doAutoCreate())
              this.temperature = new DecimalType(); // bb
          return this.temperature;
        }

        public boolean hasTemperatureElement() { 
          return this.temperature != null && !this.temperature.isEmpty();
        }

        public boolean hasTemperature() { 
          return this.temperature != null && !this.temperature.isEmpty();
        }

        /**
         * @param value {@link #temperature} (Storage temperature.). This is the underlying object with id, value and extensions. The accessor "getTemperature" gives direct access to the value
         */
        public BiologicallyDerivedProductStorageComponent setTemperatureElement(DecimalType value) { 
          this.temperature = value;
          return this;
        }

        /**
         * @return Storage temperature.
         */
        public BigDecimal getTemperature() { 
          return this.temperature == null ? null : this.temperature.getValue();
        }

        /**
         * @param value Storage temperature.
         */
        public BiologicallyDerivedProductStorageComponent setTemperature(BigDecimal value) { 
          if (value == null)
            this.temperature = null;
          else {
            if (this.temperature == null)
              this.temperature = new DecimalType();
            this.temperature.setValue(value);
          }
          return this;
        }

        /**
         * @param value Storage temperature.
         */
        public BiologicallyDerivedProductStorageComponent setTemperature(long value) { 
              this.temperature = new DecimalType();
            this.temperature.setValue(value);
          return this;
        }

        /**
         * @param value Storage temperature.
         */
        public BiologicallyDerivedProductStorageComponent setTemperature(double value) { 
              this.temperature = new DecimalType();
            this.temperature.setValue(value);
          return this;
        }

        /**
         * @return {@link #scale} (Temperature scale used.). This is the underlying object with id, value and extensions. The accessor "getScale" gives direct access to the value
         */
        public Enumeration<BiologicallyDerivedProductStorageScale> getScaleElement() { 
          if (this.scale == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create BiologicallyDerivedProductStorageComponent.scale");
            else if (Configuration.doAutoCreate())
              this.scale = new Enumeration<BiologicallyDerivedProductStorageScale>(new BiologicallyDerivedProductStorageScaleEnumFactory()); // bb
          return this.scale;
        }

        public boolean hasScaleElement() { 
          return this.scale != null && !this.scale.isEmpty();
        }

        public boolean hasScale() { 
          return this.scale != null && !this.scale.isEmpty();
        }

        /**
         * @param value {@link #scale} (Temperature scale used.). This is the underlying object with id, value and extensions. The accessor "getScale" gives direct access to the value
         */
        public BiologicallyDerivedProductStorageComponent setScaleElement(Enumeration<BiologicallyDerivedProductStorageScale> value) { 
          this.scale = value;
          return this;
        }

        /**
         * @return Temperature scale used.
         */
        public BiologicallyDerivedProductStorageScale getScale() { 
          return this.scale == null ? null : this.scale.getValue();
        }

        /**
         * @param value Temperature scale used.
         */
        public BiologicallyDerivedProductStorageComponent setScale(BiologicallyDerivedProductStorageScale value) { 
          if (value == null)
            this.scale = null;
          else {
            if (this.scale == null)
              this.scale = new Enumeration<BiologicallyDerivedProductStorageScale>(new BiologicallyDerivedProductStorageScaleEnumFactory());
            this.scale.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #duration} (Storage timeperiod.)
         */
        public Period getDuration() { 
          if (this.duration == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create BiologicallyDerivedProductStorageComponent.duration");
            else if (Configuration.doAutoCreate())
              this.duration = new Period(); // cc
          return this.duration;
        }

        public boolean hasDuration() { 
          return this.duration != null && !this.duration.isEmpty();
        }

        /**
         * @param value {@link #duration} (Storage timeperiod.)
         */
        public BiologicallyDerivedProductStorageComponent setDuration(Period value) { 
          this.duration = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("description", "string", "Description of storage.", 0, 1, description));
          children.add(new Property("temperature", "decimal", "Storage temperature.", 0, 1, temperature));
          children.add(new Property("scale", "code", "Temperature scale used.", 0, 1, scale));
          children.add(new Property("duration", "Period", "Storage timeperiod.", 0, 1, duration));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -1724546052: /*description*/  return new Property("description", "string", "Description of storage.", 0, 1, description);
          case 321701236: /*temperature*/  return new Property("temperature", "decimal", "Storage temperature.", 0, 1, temperature);
          case 109250890: /*scale*/  return new Property("scale", "code", "Temperature scale used.", 0, 1, scale);
          case -1992012396: /*duration*/  return new Property("duration", "Period", "Storage timeperiod.", 0, 1, duration);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // StringType
        case 321701236: /*temperature*/ return this.temperature == null ? new Base[0] : new Base[] {this.temperature}; // DecimalType
        case 109250890: /*scale*/ return this.scale == null ? new Base[0] : new Base[] {this.scale}; // Enumeration<BiologicallyDerivedProductStorageScale>
        case -1992012396: /*duration*/ return this.duration == null ? new Base[0] : new Base[] {this.duration}; // Period
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1724546052: // description
          this.description = castToString(value); // StringType
          return value;
        case 321701236: // temperature
          this.temperature = castToDecimal(value); // DecimalType
          return value;
        case 109250890: // scale
          value = new BiologicallyDerivedProductStorageScaleEnumFactory().fromType(castToCode(value));
          this.scale = (Enumeration) value; // Enumeration<BiologicallyDerivedProductStorageScale>
          return value;
        case -1992012396: // duration
          this.duration = castToPeriod(value); // Period
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("description")) {
          this.description = castToString(value); // StringType
        } else if (name.equals("temperature")) {
          this.temperature = castToDecimal(value); // DecimalType
        } else if (name.equals("scale")) {
          value = new BiologicallyDerivedProductStorageScaleEnumFactory().fromType(castToCode(value));
          this.scale = (Enumeration) value; // Enumeration<BiologicallyDerivedProductStorageScale>
        } else if (name.equals("duration")) {
          this.duration = castToPeriod(value); // Period
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1724546052:  return getDescriptionElement();
        case 321701236:  return getTemperatureElement();
        case 109250890:  return getScaleElement();
        case -1992012396:  return getDuration(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1724546052: /*description*/ return new String[] {"string"};
        case 321701236: /*temperature*/ return new String[] {"decimal"};
        case 109250890: /*scale*/ return new String[] {"code"};
        case -1992012396: /*duration*/ return new String[] {"Period"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type BiologicallyDerivedProduct.description");
        }
        else if (name.equals("temperature")) {
          throw new FHIRException("Cannot call addChild on a primitive type BiologicallyDerivedProduct.temperature");
        }
        else if (name.equals("scale")) {
          throw new FHIRException("Cannot call addChild on a primitive type BiologicallyDerivedProduct.scale");
        }
        else if (name.equals("duration")) {
          this.duration = new Period();
          return this.duration;
        }
        else
          return super.addChild(name);
      }

      public BiologicallyDerivedProductStorageComponent copy() {
        BiologicallyDerivedProductStorageComponent dst = new BiologicallyDerivedProductStorageComponent();
        copyValues(dst);
        dst.description = description == null ? null : description.copy();
        dst.temperature = temperature == null ? null : temperature.copy();
        dst.scale = scale == null ? null : scale.copy();
        dst.duration = duration == null ? null : duration.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof BiologicallyDerivedProductStorageComponent))
          return false;
        BiologicallyDerivedProductStorageComponent o = (BiologicallyDerivedProductStorageComponent) other_;
        return compareDeep(description, o.description, true) && compareDeep(temperature, o.temperature, true)
           && compareDeep(scale, o.scale, true) && compareDeep(duration, o.duration, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof BiologicallyDerivedProductStorageComponent))
          return false;
        BiologicallyDerivedProductStorageComponent o = (BiologicallyDerivedProductStorageComponent) other_;
        return compareValues(description, o.description, true) && compareValues(temperature, o.temperature, true)
           && compareValues(scale, o.scale, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(description, temperature, scale
          , duration);
      }

  public String fhirType() {
    return "BiologicallyDerivedProduct.storage";

  }

  }

    /**
     * This records identifiers associated with this biologically derived product instance that are defined by business processes and/or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation).
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="External ids for this item", formalDefinition="This records identifiers associated with this biologically derived product instance that are defined by business processes and/or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)." )
    protected List<Identifier> identifier;

    /**
     * Broad category of this product.
     */
    @Child(name = "productCategory", type = {CodeType.class}, order=1, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="organ | tissue | fluid | cells | biologicalAgent", formalDefinition="Broad category of this product." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/product-category")
    protected Enumeration<BiologicallyDerivedProductCategory> productCategory;

    /**
     * A code that identifies the kind of this biologically derived product (SNOMED Ctcode).
     */
    @Child(name = "productCode", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="What this biologically derived product is", formalDefinition="A code that identifies the kind of this biologically derived product (SNOMED Ctcode)." )
    protected CodeableConcept productCode;

    /**
     * Whether the product is currently available.
     */
    @Child(name = "status", type = {CodeType.class}, order=3, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="available | unavailable", formalDefinition="Whether the product is currently available." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/product-status")
    protected Enumeration<BiologicallyDerivedProductStatus> status;

    /**
     * Procedure request to obtain this biologically derived product.
     */
    @Child(name = "request", type = {ServiceRequest.class}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Procedure request", formalDefinition="Procedure request to obtain this biologically derived product." )
    protected List<Reference> request;
    /**
     * The actual objects that are the target of the reference (Procedure request to obtain this biologically derived product.)
     */
    protected List<ServiceRequest> requestTarget;


    /**
     * Number of discrete units within this product.
     */
    @Child(name = "quantity", type = {IntegerType.class}, order=5, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="The amount of this biologically derived product", formalDefinition="Number of discrete units within this product." )
    protected IntegerType quantity;

    /**
     * Parent product (if any).
     */
    @Child(name = "parent", type = {Reference.class}, order=6, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="BiologicallyDerivedProduct parent", formalDefinition="Parent product (if any)." )
    protected Reference parent;

    /**
     * The actual object that is the target of the reference (Parent product (if any).)
     */
    protected Resource parentTarget;

    /**
     * How this product was collected.
     */
    @Child(name = "collection", type = {}, order=7, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="How this product was collected", formalDefinition="How this product was collected." )
    protected BiologicallyDerivedProductCollectionComponent collection;

    /**
     * Any processing of the product during collection.
     */
    @Child(name = "processing", type = {}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Any processing of the product during collection", formalDefinition="Any processing of the product during collection." )
    protected List<BiologicallyDerivedProductProcessingComponent> processing;

    /**
     * Any manipulation of product post-collection.
     */
    @Child(name = "manipulation", type = {}, order=9, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Any manipulation of product post-collection", formalDefinition="Any manipulation of product post-collection." )
    protected BiologicallyDerivedProductManipulationComponent manipulation;

    /**
     * Product storage.
     */
    @Child(name = "storage", type = {}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Product storage", formalDefinition="Product storage." )
    protected List<BiologicallyDerivedProductStorageComponent> storage;

    private static final long serialVersionUID = 1638250261L;

  /**
   * Constructor
   */
    public BiologicallyDerivedProduct() {
      super();
    }

    /**
     * @return {@link #identifier} (This records identifiers associated with this biologically derived product instance that are defined by business processes and/or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation).)
     */
    public List<Identifier> getIdentifier() { 
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      return this.identifier;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public BiologicallyDerivedProduct setIdentifier(List<Identifier> theIdentifier) { 
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

    public BiologicallyDerivedProduct addIdentifier(Identifier t) { //3
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
     * @return {@link #productCategory} (Broad category of this product.). This is the underlying object with id, value and extensions. The accessor "getProductCategory" gives direct access to the value
     */
    public Enumeration<BiologicallyDerivedProductCategory> getProductCategoryElement() { 
      if (this.productCategory == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create BiologicallyDerivedProduct.productCategory");
        else if (Configuration.doAutoCreate())
          this.productCategory = new Enumeration<BiologicallyDerivedProductCategory>(new BiologicallyDerivedProductCategoryEnumFactory()); // bb
      return this.productCategory;
    }

    public boolean hasProductCategoryElement() { 
      return this.productCategory != null && !this.productCategory.isEmpty();
    }

    public boolean hasProductCategory() { 
      return this.productCategory != null && !this.productCategory.isEmpty();
    }

    /**
     * @param value {@link #productCategory} (Broad category of this product.). This is the underlying object with id, value and extensions. The accessor "getProductCategory" gives direct access to the value
     */
    public BiologicallyDerivedProduct setProductCategoryElement(Enumeration<BiologicallyDerivedProductCategory> value) { 
      this.productCategory = value;
      return this;
    }

    /**
     * @return Broad category of this product.
     */
    public BiologicallyDerivedProductCategory getProductCategory() { 
      return this.productCategory == null ? null : this.productCategory.getValue();
    }

    /**
     * @param value Broad category of this product.
     */
    public BiologicallyDerivedProduct setProductCategory(BiologicallyDerivedProductCategory value) { 
      if (value == null)
        this.productCategory = null;
      else {
        if (this.productCategory == null)
          this.productCategory = new Enumeration<BiologicallyDerivedProductCategory>(new BiologicallyDerivedProductCategoryEnumFactory());
        this.productCategory.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #productCode} (A code that identifies the kind of this biologically derived product (SNOMED Ctcode).)
     */
    public CodeableConcept getProductCode() { 
      if (this.productCode == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create BiologicallyDerivedProduct.productCode");
        else if (Configuration.doAutoCreate())
          this.productCode = new CodeableConcept(); // cc
      return this.productCode;
    }

    public boolean hasProductCode() { 
      return this.productCode != null && !this.productCode.isEmpty();
    }

    /**
     * @param value {@link #productCode} (A code that identifies the kind of this biologically derived product (SNOMED Ctcode).)
     */
    public BiologicallyDerivedProduct setProductCode(CodeableConcept value) { 
      this.productCode = value;
      return this;
    }

    /**
     * @return {@link #status} (Whether the product is currently available.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<BiologicallyDerivedProductStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create BiologicallyDerivedProduct.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<BiologicallyDerivedProductStatus>(new BiologicallyDerivedProductStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (Whether the product is currently available.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public BiologicallyDerivedProduct setStatusElement(Enumeration<BiologicallyDerivedProductStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return Whether the product is currently available.
     */
    public BiologicallyDerivedProductStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value Whether the product is currently available.
     */
    public BiologicallyDerivedProduct setStatus(BiologicallyDerivedProductStatus value) { 
      if (value == null)
        this.status = null;
      else {
        if (this.status == null)
          this.status = new Enumeration<BiologicallyDerivedProductStatus>(new BiologicallyDerivedProductStatusEnumFactory());
        this.status.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #request} (Procedure request to obtain this biologically derived product.)
     */
    public List<Reference> getRequest() { 
      if (this.request == null)
        this.request = new ArrayList<Reference>();
      return this.request;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public BiologicallyDerivedProduct setRequest(List<Reference> theRequest) { 
      this.request = theRequest;
      return this;
    }

    public boolean hasRequest() { 
      if (this.request == null)
        return false;
      for (Reference item : this.request)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addRequest() { //3
      Reference t = new Reference();
      if (this.request == null)
        this.request = new ArrayList<Reference>();
      this.request.add(t);
      return t;
    }

    public BiologicallyDerivedProduct addRequest(Reference t) { //3
      if (t == null)
        return this;
      if (this.request == null)
        this.request = new ArrayList<Reference>();
      this.request.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #request}, creating it if it does not already exist
     */
    public Reference getRequestFirstRep() { 
      if (getRequest().isEmpty()) {
        addRequest();
      }
      return getRequest().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<ServiceRequest> getRequestTarget() { 
      if (this.requestTarget == null)
        this.requestTarget = new ArrayList<ServiceRequest>();
      return this.requestTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public ServiceRequest addRequestTarget() { 
      ServiceRequest r = new ServiceRequest();
      if (this.requestTarget == null)
        this.requestTarget = new ArrayList<ServiceRequest>();
      this.requestTarget.add(r);
      return r;
    }

    /**
     * @return {@link #quantity} (Number of discrete units within this product.). This is the underlying object with id, value and extensions. The accessor "getQuantity" gives direct access to the value
     */
    public IntegerType getQuantityElement() { 
      if (this.quantity == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create BiologicallyDerivedProduct.quantity");
        else if (Configuration.doAutoCreate())
          this.quantity = new IntegerType(); // bb
      return this.quantity;
    }

    public boolean hasQuantityElement() { 
      return this.quantity != null && !this.quantity.isEmpty();
    }

    public boolean hasQuantity() { 
      return this.quantity != null && !this.quantity.isEmpty();
    }

    /**
     * @param value {@link #quantity} (Number of discrete units within this product.). This is the underlying object with id, value and extensions. The accessor "getQuantity" gives direct access to the value
     */
    public BiologicallyDerivedProduct setQuantityElement(IntegerType value) { 
      this.quantity = value;
      return this;
    }

    /**
     * @return Number of discrete units within this product.
     */
    public int getQuantity() { 
      return this.quantity == null || this.quantity.isEmpty() ? 0 : this.quantity.getValue();
    }

    /**
     * @param value Number of discrete units within this product.
     */
    public BiologicallyDerivedProduct setQuantity(int value) { 
        if (this.quantity == null)
          this.quantity = new IntegerType();
        this.quantity.setValue(value);
      return this;
    }

    /**
     * @return {@link #parent} (Parent product (if any).)
     */
    public Reference getParent() { 
      if (this.parent == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create BiologicallyDerivedProduct.parent");
        else if (Configuration.doAutoCreate())
          this.parent = new Reference(); // cc
      return this.parent;
    }

    public boolean hasParent() { 
      return this.parent != null && !this.parent.isEmpty();
    }

    /**
     * @param value {@link #parent} (Parent product (if any).)
     */
    public BiologicallyDerivedProduct setParent(Reference value) { 
      this.parent = value;
      return this;
    }

    /**
     * @return {@link #parent} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Parent product (if any).)
     */
    public Resource getParentTarget() { 
      return this.parentTarget;
    }

    /**
     * @param value {@link #parent} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Parent product (if any).)
     */
    public BiologicallyDerivedProduct setParentTarget(Resource value) { 
      this.parentTarget = value;
      return this;
    }

    /**
     * @return {@link #collection} (How this product was collected.)
     */
    public BiologicallyDerivedProductCollectionComponent getCollection() { 
      if (this.collection == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create BiologicallyDerivedProduct.collection");
        else if (Configuration.doAutoCreate())
          this.collection = new BiologicallyDerivedProductCollectionComponent(); // cc
      return this.collection;
    }

    public boolean hasCollection() { 
      return this.collection != null && !this.collection.isEmpty();
    }

    /**
     * @param value {@link #collection} (How this product was collected.)
     */
    public BiologicallyDerivedProduct setCollection(BiologicallyDerivedProductCollectionComponent value) { 
      this.collection = value;
      return this;
    }

    /**
     * @return {@link #processing} (Any processing of the product during collection.)
     */
    public List<BiologicallyDerivedProductProcessingComponent> getProcessing() { 
      if (this.processing == null)
        this.processing = new ArrayList<BiologicallyDerivedProductProcessingComponent>();
      return this.processing;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public BiologicallyDerivedProduct setProcessing(List<BiologicallyDerivedProductProcessingComponent> theProcessing) { 
      this.processing = theProcessing;
      return this;
    }

    public boolean hasProcessing() { 
      if (this.processing == null)
        return false;
      for (BiologicallyDerivedProductProcessingComponent item : this.processing)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public BiologicallyDerivedProductProcessingComponent addProcessing() { //3
      BiologicallyDerivedProductProcessingComponent t = new BiologicallyDerivedProductProcessingComponent();
      if (this.processing == null)
        this.processing = new ArrayList<BiologicallyDerivedProductProcessingComponent>();
      this.processing.add(t);
      return t;
    }

    public BiologicallyDerivedProduct addProcessing(BiologicallyDerivedProductProcessingComponent t) { //3
      if (t == null)
        return this;
      if (this.processing == null)
        this.processing = new ArrayList<BiologicallyDerivedProductProcessingComponent>();
      this.processing.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #processing}, creating it if it does not already exist
     */
    public BiologicallyDerivedProductProcessingComponent getProcessingFirstRep() { 
      if (getProcessing().isEmpty()) {
        addProcessing();
      }
      return getProcessing().get(0);
    }

    /**
     * @return {@link #manipulation} (Any manipulation of product post-collection.)
     */
    public BiologicallyDerivedProductManipulationComponent getManipulation() { 
      if (this.manipulation == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create BiologicallyDerivedProduct.manipulation");
        else if (Configuration.doAutoCreate())
          this.manipulation = new BiologicallyDerivedProductManipulationComponent(); // cc
      return this.manipulation;
    }

    public boolean hasManipulation() { 
      return this.manipulation != null && !this.manipulation.isEmpty();
    }

    /**
     * @param value {@link #manipulation} (Any manipulation of product post-collection.)
     */
    public BiologicallyDerivedProduct setManipulation(BiologicallyDerivedProductManipulationComponent value) { 
      this.manipulation = value;
      return this;
    }

    /**
     * @return {@link #storage} (Product storage.)
     */
    public List<BiologicallyDerivedProductStorageComponent> getStorage() { 
      if (this.storage == null)
        this.storage = new ArrayList<BiologicallyDerivedProductStorageComponent>();
      return this.storage;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public BiologicallyDerivedProduct setStorage(List<BiologicallyDerivedProductStorageComponent> theStorage) { 
      this.storage = theStorage;
      return this;
    }

    public boolean hasStorage() { 
      if (this.storage == null)
        return false;
      for (BiologicallyDerivedProductStorageComponent item : this.storage)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public BiologicallyDerivedProductStorageComponent addStorage() { //3
      BiologicallyDerivedProductStorageComponent t = new BiologicallyDerivedProductStorageComponent();
      if (this.storage == null)
        this.storage = new ArrayList<BiologicallyDerivedProductStorageComponent>();
      this.storage.add(t);
      return t;
    }

    public BiologicallyDerivedProduct addStorage(BiologicallyDerivedProductStorageComponent t) { //3
      if (t == null)
        return this;
      if (this.storage == null)
        this.storage = new ArrayList<BiologicallyDerivedProductStorageComponent>();
      this.storage.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #storage}, creating it if it does not already exist
     */
    public BiologicallyDerivedProductStorageComponent getStorageFirstRep() { 
      if (getStorage().isEmpty()) {
        addStorage();
      }
      return getStorage().get(0);
    }

      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("identifier", "Identifier", "This records identifiers associated with this biologically derived product instance that are defined by business processes and/or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation).", 0, java.lang.Integer.MAX_VALUE, identifier));
        children.add(new Property("productCategory", "code", "Broad category of this product.", 0, 1, productCategory));
        children.add(new Property("productCode", "CodeableConcept", "A code that identifies the kind of this biologically derived product (SNOMED Ctcode).", 0, 1, productCode));
        children.add(new Property("status", "code", "Whether the product is currently available.", 0, 1, status));
        children.add(new Property("request", "Reference(ServiceRequest)", "Procedure request to obtain this biologically derived product.", 0, java.lang.Integer.MAX_VALUE, request));
        children.add(new Property("quantity", "integer", "Number of discrete units within this product.", 0, 1, quantity));
        children.add(new Property("parent", "Reference(Any)", "Parent product (if any).", 0, 1, parent));
        children.add(new Property("collection", "", "How this product was collected.", 0, 1, collection));
        children.add(new Property("processing", "", "Any processing of the product during collection.", 0, java.lang.Integer.MAX_VALUE, processing));
        children.add(new Property("manipulation", "", "Any manipulation of product post-collection.", 0, 1, manipulation));
        children.add(new Property("storage", "", "Product storage.", 0, java.lang.Integer.MAX_VALUE, storage));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case -1618432855: /*identifier*/  return new Property("identifier", "Identifier", "This records identifiers associated with this biologically derived product instance that are defined by business processes and/or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation).", 0, java.lang.Integer.MAX_VALUE, identifier);
        case 197299981: /*productCategory*/  return new Property("productCategory", "code", "Broad category of this product.", 0, 1, productCategory);
        case -1492131972: /*productCode*/  return new Property("productCode", "CodeableConcept", "A code that identifies the kind of this biologically derived product (SNOMED Ctcode).", 0, 1, productCode);
        case -892481550: /*status*/  return new Property("status", "code", "Whether the product is currently available.", 0, 1, status);
        case 1095692943: /*request*/  return new Property("request", "Reference(ServiceRequest)", "Procedure request to obtain this biologically derived product.", 0, java.lang.Integer.MAX_VALUE, request);
        case -1285004149: /*quantity*/  return new Property("quantity", "integer", "Number of discrete units within this product.", 0, 1, quantity);
        case -995424086: /*parent*/  return new Property("parent", "Reference(Any)", "Parent product (if any).", 0, 1, parent);
        case -1741312354: /*collection*/  return new Property("collection", "", "How this product was collected.", 0, 1, collection);
        case 422194963: /*processing*/  return new Property("processing", "", "Any processing of the product during collection.", 0, java.lang.Integer.MAX_VALUE, processing);
        case -696214627: /*manipulation*/  return new Property("manipulation", "", "Any manipulation of product post-collection.", 0, 1, manipulation);
        case -1884274053: /*storage*/  return new Property("storage", "", "Product storage.", 0, java.lang.Integer.MAX_VALUE, storage);
        default: return super.getNamedProperty(_hash, _name, _checkValid);
        }

      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case 197299981: /*productCategory*/ return this.productCategory == null ? new Base[0] : new Base[] {this.productCategory}; // Enumeration<BiologicallyDerivedProductCategory>
        case -1492131972: /*productCode*/ return this.productCode == null ? new Base[0] : new Base[] {this.productCode}; // CodeableConcept
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<BiologicallyDerivedProductStatus>
        case 1095692943: /*request*/ return this.request == null ? new Base[0] : this.request.toArray(new Base[this.request.size()]); // Reference
        case -1285004149: /*quantity*/ return this.quantity == null ? new Base[0] : new Base[] {this.quantity}; // IntegerType
        case -995424086: /*parent*/ return this.parent == null ? new Base[0] : new Base[] {this.parent}; // Reference
        case -1741312354: /*collection*/ return this.collection == null ? new Base[0] : new Base[] {this.collection}; // BiologicallyDerivedProductCollectionComponent
        case 422194963: /*processing*/ return this.processing == null ? new Base[0] : this.processing.toArray(new Base[this.processing.size()]); // BiologicallyDerivedProductProcessingComponent
        case -696214627: /*manipulation*/ return this.manipulation == null ? new Base[0] : new Base[] {this.manipulation}; // BiologicallyDerivedProductManipulationComponent
        case -1884274053: /*storage*/ return this.storage == null ? new Base[0] : this.storage.toArray(new Base[this.storage.size()]); // BiologicallyDerivedProductStorageComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.getIdentifier().add(castToIdentifier(value)); // Identifier
          return value;
        case 197299981: // productCategory
          value = new BiologicallyDerivedProductCategoryEnumFactory().fromType(castToCode(value));
          this.productCategory = (Enumeration) value; // Enumeration<BiologicallyDerivedProductCategory>
          return value;
        case -1492131972: // productCode
          this.productCode = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -892481550: // status
          value = new BiologicallyDerivedProductStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<BiologicallyDerivedProductStatus>
          return value;
        case 1095692943: // request
          this.getRequest().add(castToReference(value)); // Reference
          return value;
        case -1285004149: // quantity
          this.quantity = castToInteger(value); // IntegerType
          return value;
        case -995424086: // parent
          this.parent = castToReference(value); // Reference
          return value;
        case -1741312354: // collection
          this.collection = (BiologicallyDerivedProductCollectionComponent) value; // BiologicallyDerivedProductCollectionComponent
          return value;
        case 422194963: // processing
          this.getProcessing().add((BiologicallyDerivedProductProcessingComponent) value); // BiologicallyDerivedProductProcessingComponent
          return value;
        case -696214627: // manipulation
          this.manipulation = (BiologicallyDerivedProductManipulationComponent) value; // BiologicallyDerivedProductManipulationComponent
          return value;
        case -1884274053: // storage
          this.getStorage().add((BiologicallyDerivedProductStorageComponent) value); // BiologicallyDerivedProductStorageComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier")) {
          this.getIdentifier().add(castToIdentifier(value));
        } else if (name.equals("productCategory")) {
          value = new BiologicallyDerivedProductCategoryEnumFactory().fromType(castToCode(value));
          this.productCategory = (Enumeration) value; // Enumeration<BiologicallyDerivedProductCategory>
        } else if (name.equals("productCode")) {
          this.productCode = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("status")) {
          value = new BiologicallyDerivedProductStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<BiologicallyDerivedProductStatus>
        } else if (name.equals("request")) {
          this.getRequest().add(castToReference(value));
        } else if (name.equals("quantity")) {
          this.quantity = castToInteger(value); // IntegerType
        } else if (name.equals("parent")) {
          this.parent = castToReference(value); // Reference
        } else if (name.equals("collection")) {
          this.collection = (BiologicallyDerivedProductCollectionComponent) value; // BiologicallyDerivedProductCollectionComponent
        } else if (name.equals("processing")) {
          this.getProcessing().add((BiologicallyDerivedProductProcessingComponent) value);
        } else if (name.equals("manipulation")) {
          this.manipulation = (BiologicallyDerivedProductManipulationComponent) value; // BiologicallyDerivedProductManipulationComponent
        } else if (name.equals("storage")) {
          this.getStorage().add((BiologicallyDerivedProductStorageComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return addIdentifier(); 
        case 197299981:  return getProductCategoryElement();
        case -1492131972:  return getProductCode(); 
        case -892481550:  return getStatusElement();
        case 1095692943:  return addRequest(); 
        case -1285004149:  return getQuantityElement();
        case -995424086:  return getParent(); 
        case -1741312354:  return getCollection(); 
        case 422194963:  return addProcessing(); 
        case -696214627:  return getManipulation(); 
        case -1884274053:  return addStorage(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case 197299981: /*productCategory*/ return new String[] {"code"};
        case -1492131972: /*productCode*/ return new String[] {"CodeableConcept"};
        case -892481550: /*status*/ return new String[] {"code"};
        case 1095692943: /*request*/ return new String[] {"Reference"};
        case -1285004149: /*quantity*/ return new String[] {"integer"};
        case -995424086: /*parent*/ return new String[] {"Reference"};
        case -1741312354: /*collection*/ return new String[] {};
        case 422194963: /*processing*/ return new String[] {};
        case -696214627: /*manipulation*/ return new String[] {};
        case -1884274053: /*storage*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("productCategory")) {
          throw new FHIRException("Cannot call addChild on a primitive type BiologicallyDerivedProduct.productCategory");
        }
        else if (name.equals("productCode")) {
          this.productCode = new CodeableConcept();
          return this.productCode;
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type BiologicallyDerivedProduct.status");
        }
        else if (name.equals("request")) {
          return addRequest();
        }
        else if (name.equals("quantity")) {
          throw new FHIRException("Cannot call addChild on a primitive type BiologicallyDerivedProduct.quantity");
        }
        else if (name.equals("parent")) {
          this.parent = new Reference();
          return this.parent;
        }
        else if (name.equals("collection")) {
          this.collection = new BiologicallyDerivedProductCollectionComponent();
          return this.collection;
        }
        else if (name.equals("processing")) {
          return addProcessing();
        }
        else if (name.equals("manipulation")) {
          this.manipulation = new BiologicallyDerivedProductManipulationComponent();
          return this.manipulation;
        }
        else if (name.equals("storage")) {
          return addStorage();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "BiologicallyDerivedProduct";

  }

      public BiologicallyDerivedProduct copy() {
        BiologicallyDerivedProduct dst = new BiologicallyDerivedProduct();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.productCategory = productCategory == null ? null : productCategory.copy();
        dst.productCode = productCode == null ? null : productCode.copy();
        dst.status = status == null ? null : status.copy();
        if (request != null) {
          dst.request = new ArrayList<Reference>();
          for (Reference i : request)
            dst.request.add(i.copy());
        };
        dst.quantity = quantity == null ? null : quantity.copy();
        dst.parent = parent == null ? null : parent.copy();
        dst.collection = collection == null ? null : collection.copy();
        if (processing != null) {
          dst.processing = new ArrayList<BiologicallyDerivedProductProcessingComponent>();
          for (BiologicallyDerivedProductProcessingComponent i : processing)
            dst.processing.add(i.copy());
        };
        dst.manipulation = manipulation == null ? null : manipulation.copy();
        if (storage != null) {
          dst.storage = new ArrayList<BiologicallyDerivedProductStorageComponent>();
          for (BiologicallyDerivedProductStorageComponent i : storage)
            dst.storage.add(i.copy());
        };
        return dst;
      }

      protected BiologicallyDerivedProduct typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof BiologicallyDerivedProduct))
          return false;
        BiologicallyDerivedProduct o = (BiologicallyDerivedProduct) other_;
        return compareDeep(identifier, o.identifier, true) && compareDeep(productCategory, o.productCategory, true)
           && compareDeep(productCode, o.productCode, true) && compareDeep(status, o.status, true) && compareDeep(request, o.request, true)
           && compareDeep(quantity, o.quantity, true) && compareDeep(parent, o.parent, true) && compareDeep(collection, o.collection, true)
           && compareDeep(processing, o.processing, true) && compareDeep(manipulation, o.manipulation, true)
           && compareDeep(storage, o.storage, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof BiologicallyDerivedProduct))
          return false;
        BiologicallyDerivedProduct o = (BiologicallyDerivedProduct) other_;
        return compareValues(productCategory, o.productCategory, true) && compareValues(status, o.status, true)
           && compareValues(quantity, o.quantity, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, productCategory
          , productCode, status, request, quantity, parent, collection, processing, manipulation
          , storage);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.BiologicallyDerivedProduct;
   }


}

