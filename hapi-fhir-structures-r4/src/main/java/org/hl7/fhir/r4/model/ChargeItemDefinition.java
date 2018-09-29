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
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ChildOrder;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Block;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.exceptions.FHIRException;
/**
 * The ChargeItemDefinition resource provides the properties that apply to the (billing) codes necessary to calculate costs and prices. The properties may differ largely depending on type and realm, therefore this resource gives only a rough structure and requires profiling for each type of billing code system.
 */
@ResourceDef(name="ChargeItemDefinition", profile="http://hl7.org/fhir/Profile/ChargeItemDefinition")
@ChildOrder(names={"url", "identifier", "version", "title", "derivedFromUri", "partOf", "replaces", "status", "experimental", "date", "publisher", "contact", "description", "useContext", "jurisdiction", "copyright", "approvalDate", "lastReviewDate", "effectivePeriod", "code", "instance", "applicability", "propertyGroup"})
public class ChargeItemDefinition extends MetadataResource {

    public enum ChargeItemDefinitionPriceComponentType {
        /**
         * the amount is the base price used for calculating the total price before applying surcharges, discount or taxes.
         */
        BASE, 
        /**
         * the amount is a surcharge applied on the base price.
         */
        SURCHARGE, 
        /**
         * the amount is a deduction applied on the base price.
         */
        DEDUCTION, 
        /**
         * the amount is a discount applied on the base price.
         */
        DISCOUNT, 
        /**
         * the amount is the tax component of the total price.
         */
        TAX, 
        /**
         * the amount is of informational character, it has not been applied in the calculation of the total price.
         */
        INFORMATIONAL, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static ChargeItemDefinitionPriceComponentType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("base".equals(codeString))
          return BASE;
        if ("surcharge".equals(codeString))
          return SURCHARGE;
        if ("deduction".equals(codeString))
          return DEDUCTION;
        if ("discount".equals(codeString))
          return DISCOUNT;
        if ("tax".equals(codeString))
          return TAX;
        if ("informational".equals(codeString))
          return INFORMATIONAL;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown ChargeItemDefinitionPriceComponentType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case BASE: return "base";
            case SURCHARGE: return "surcharge";
            case DEDUCTION: return "deduction";
            case DISCOUNT: return "discount";
            case TAX: return "tax";
            case INFORMATIONAL: return "informational";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case BASE: return "http://hl7.org/fhir/invoice-priceComponentType";
            case SURCHARGE: return "http://hl7.org/fhir/invoice-priceComponentType";
            case DEDUCTION: return "http://hl7.org/fhir/invoice-priceComponentType";
            case DISCOUNT: return "http://hl7.org/fhir/invoice-priceComponentType";
            case TAX: return "http://hl7.org/fhir/invoice-priceComponentType";
            case INFORMATIONAL: return "http://hl7.org/fhir/invoice-priceComponentType";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case BASE: return "the amount is the base price used for calculating the total price before applying surcharges, discount or taxes.";
            case SURCHARGE: return "the amount is a surcharge applied on the base price.";
            case DEDUCTION: return "the amount is a deduction applied on the base price.";
            case DISCOUNT: return "the amount is a discount applied on the base price.";
            case TAX: return "the amount is the tax component of the total price.";
            case INFORMATIONAL: return "the amount is of informational character, it has not been applied in the calculation of the total price.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case BASE: return "base price";
            case SURCHARGE: return "surcharge";
            case DEDUCTION: return "deduction";
            case DISCOUNT: return "discount";
            case TAX: return "tax";
            case INFORMATIONAL: return "informational";
            default: return "?";
          }
        }
    }

  public static class ChargeItemDefinitionPriceComponentTypeEnumFactory implements EnumFactory<ChargeItemDefinitionPriceComponentType> {
    public ChargeItemDefinitionPriceComponentType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("base".equals(codeString))
          return ChargeItemDefinitionPriceComponentType.BASE;
        if ("surcharge".equals(codeString))
          return ChargeItemDefinitionPriceComponentType.SURCHARGE;
        if ("deduction".equals(codeString))
          return ChargeItemDefinitionPriceComponentType.DEDUCTION;
        if ("discount".equals(codeString))
          return ChargeItemDefinitionPriceComponentType.DISCOUNT;
        if ("tax".equals(codeString))
          return ChargeItemDefinitionPriceComponentType.TAX;
        if ("informational".equals(codeString))
          return ChargeItemDefinitionPriceComponentType.INFORMATIONAL;
        throw new IllegalArgumentException("Unknown ChargeItemDefinitionPriceComponentType code '"+codeString+"'");
        }
        public Enumeration<ChargeItemDefinitionPriceComponentType> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<ChargeItemDefinitionPriceComponentType>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("base".equals(codeString))
          return new Enumeration<ChargeItemDefinitionPriceComponentType>(this, ChargeItemDefinitionPriceComponentType.BASE);
        if ("surcharge".equals(codeString))
          return new Enumeration<ChargeItemDefinitionPriceComponentType>(this, ChargeItemDefinitionPriceComponentType.SURCHARGE);
        if ("deduction".equals(codeString))
          return new Enumeration<ChargeItemDefinitionPriceComponentType>(this, ChargeItemDefinitionPriceComponentType.DEDUCTION);
        if ("discount".equals(codeString))
          return new Enumeration<ChargeItemDefinitionPriceComponentType>(this, ChargeItemDefinitionPriceComponentType.DISCOUNT);
        if ("tax".equals(codeString))
          return new Enumeration<ChargeItemDefinitionPriceComponentType>(this, ChargeItemDefinitionPriceComponentType.TAX);
        if ("informational".equals(codeString))
          return new Enumeration<ChargeItemDefinitionPriceComponentType>(this, ChargeItemDefinitionPriceComponentType.INFORMATIONAL);
        throw new FHIRException("Unknown ChargeItemDefinitionPriceComponentType code '"+codeString+"'");
        }
    public String toCode(ChargeItemDefinitionPriceComponentType code) {
      if (code == ChargeItemDefinitionPriceComponentType.BASE)
        return "base";
      if (code == ChargeItemDefinitionPriceComponentType.SURCHARGE)
        return "surcharge";
      if (code == ChargeItemDefinitionPriceComponentType.DEDUCTION)
        return "deduction";
      if (code == ChargeItemDefinitionPriceComponentType.DISCOUNT)
        return "discount";
      if (code == ChargeItemDefinitionPriceComponentType.TAX)
        return "tax";
      if (code == ChargeItemDefinitionPriceComponentType.INFORMATIONAL)
        return "informational";
      return "?";
      }
    public String toSystem(ChargeItemDefinitionPriceComponentType code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class ChargeItemDefinitionApplicabilityComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A brief, natural language description of the condition that effectively communicates the intended semantics.
         */
        @Child(name = "description", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Natural language description of the condition", formalDefinition="A brief, natural language description of the condition that effectively communicates the intended semantics." )
        protected StringType description;

        /**
         * The media type of the language for the expression, e.g. "text/cql" for Clinical Query Language expressions or "text/fhirpath" for FHIRPath expressions.
         */
        @Child(name = "language", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Language of the expression", formalDefinition="The media type of the language for the expression, e.g. \"text/cql\" for Clinical Query Language expressions or \"text/fhirpath\" for FHIRPath expressions." )
        protected StringType language;

        /**
         * An expression that returns true or false, indicating whether the condition is satisfied. When using FHIRPath expressions, the %context evironment variable must be replaced at runtime with the ChargeItem resource to which this definition is applied.
         */
        @Child(name = "expression", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Boolean-valued expression", formalDefinition="An expression that returns true or false, indicating whether the condition is satisfied. When using FHIRPath expressions, the %context evironment variable must be replaced at runtime with the ChargeItem resource to which this definition is applied." )
        protected StringType expression;

        private static final long serialVersionUID = 1354288281L;

    /**
     * Constructor
     */
      public ChargeItemDefinitionApplicabilityComponent() {
        super();
      }

        /**
         * @return {@link #description} (A brief, natural language description of the condition that effectively communicates the intended semantics.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public StringType getDescriptionElement() { 
          if (this.description == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ChargeItemDefinitionApplicabilityComponent.description");
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
         * @param value {@link #description} (A brief, natural language description of the condition that effectively communicates the intended semantics.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public ChargeItemDefinitionApplicabilityComponent setDescriptionElement(StringType value) { 
          this.description = value;
          return this;
        }

        /**
         * @return A brief, natural language description of the condition that effectively communicates the intended semantics.
         */
        public String getDescription() { 
          return this.description == null ? null : this.description.getValue();
        }

        /**
         * @param value A brief, natural language description of the condition that effectively communicates the intended semantics.
         */
        public ChargeItemDefinitionApplicabilityComponent setDescription(String value) { 
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
         * @return {@link #language} (The media type of the language for the expression, e.g. "text/cql" for Clinical Query Language expressions or "text/fhirpath" for FHIRPath expressions.). This is the underlying object with id, value and extensions. The accessor "getLanguage" gives direct access to the value
         */
        public StringType getLanguageElement() { 
          if (this.language == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ChargeItemDefinitionApplicabilityComponent.language");
            else if (Configuration.doAutoCreate())
              this.language = new StringType(); // bb
          return this.language;
        }

        public boolean hasLanguageElement() { 
          return this.language != null && !this.language.isEmpty();
        }

        public boolean hasLanguage() { 
          return this.language != null && !this.language.isEmpty();
        }

        /**
         * @param value {@link #language} (The media type of the language for the expression, e.g. "text/cql" for Clinical Query Language expressions or "text/fhirpath" for FHIRPath expressions.). This is the underlying object with id, value and extensions. The accessor "getLanguage" gives direct access to the value
         */
        public ChargeItemDefinitionApplicabilityComponent setLanguageElement(StringType value) { 
          this.language = value;
          return this;
        }

        /**
         * @return The media type of the language for the expression, e.g. "text/cql" for Clinical Query Language expressions or "text/fhirpath" for FHIRPath expressions.
         */
        public String getLanguage() { 
          return this.language == null ? null : this.language.getValue();
        }

        /**
         * @param value The media type of the language for the expression, e.g. "text/cql" for Clinical Query Language expressions or "text/fhirpath" for FHIRPath expressions.
         */
        public ChargeItemDefinitionApplicabilityComponent setLanguage(String value) { 
          if (Utilities.noString(value))
            this.language = null;
          else {
            if (this.language == null)
              this.language = new StringType();
            this.language.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #expression} (An expression that returns true or false, indicating whether the condition is satisfied. When using FHIRPath expressions, the %context evironment variable must be replaced at runtime with the ChargeItem resource to which this definition is applied.). This is the underlying object with id, value and extensions. The accessor "getExpression" gives direct access to the value
         */
        public StringType getExpressionElement() { 
          if (this.expression == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ChargeItemDefinitionApplicabilityComponent.expression");
            else if (Configuration.doAutoCreate())
              this.expression = new StringType(); // bb
          return this.expression;
        }

        public boolean hasExpressionElement() { 
          return this.expression != null && !this.expression.isEmpty();
        }

        public boolean hasExpression() { 
          return this.expression != null && !this.expression.isEmpty();
        }

        /**
         * @param value {@link #expression} (An expression that returns true or false, indicating whether the condition is satisfied. When using FHIRPath expressions, the %context evironment variable must be replaced at runtime with the ChargeItem resource to which this definition is applied.). This is the underlying object with id, value and extensions. The accessor "getExpression" gives direct access to the value
         */
        public ChargeItemDefinitionApplicabilityComponent setExpressionElement(StringType value) { 
          this.expression = value;
          return this;
        }

        /**
         * @return An expression that returns true or false, indicating whether the condition is satisfied. When using FHIRPath expressions, the %context evironment variable must be replaced at runtime with the ChargeItem resource to which this definition is applied.
         */
        public String getExpression() { 
          return this.expression == null ? null : this.expression.getValue();
        }

        /**
         * @param value An expression that returns true or false, indicating whether the condition is satisfied. When using FHIRPath expressions, the %context evironment variable must be replaced at runtime with the ChargeItem resource to which this definition is applied.
         */
        public ChargeItemDefinitionApplicabilityComponent setExpression(String value) { 
          if (Utilities.noString(value))
            this.expression = null;
          else {
            if (this.expression == null)
              this.expression = new StringType();
            this.expression.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("description", "string", "A brief, natural language description of the condition that effectively communicates the intended semantics.", 0, 1, description));
          children.add(new Property("language", "string", "The media type of the language for the expression, e.g. \"text/cql\" for Clinical Query Language expressions or \"text/fhirpath\" for FHIRPath expressions.", 0, 1, language));
          children.add(new Property("expression", "string", "An expression that returns true or false, indicating whether the condition is satisfied. When using FHIRPath expressions, the %context evironment variable must be replaced at runtime with the ChargeItem resource to which this definition is applied.", 0, 1, expression));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -1724546052: /*description*/  return new Property("description", "string", "A brief, natural language description of the condition that effectively communicates the intended semantics.", 0, 1, description);
          case -1613589672: /*language*/  return new Property("language", "string", "The media type of the language for the expression, e.g. \"text/cql\" for Clinical Query Language expressions or \"text/fhirpath\" for FHIRPath expressions.", 0, 1, language);
          case -1795452264: /*expression*/  return new Property("expression", "string", "An expression that returns true or false, indicating whether the condition is satisfied. When using FHIRPath expressions, the %context evironment variable must be replaced at runtime with the ChargeItem resource to which this definition is applied.", 0, 1, expression);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // StringType
        case -1613589672: /*language*/ return this.language == null ? new Base[0] : new Base[] {this.language}; // StringType
        case -1795452264: /*expression*/ return this.expression == null ? new Base[0] : new Base[] {this.expression}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1724546052: // description
          this.description = castToString(value); // StringType
          return value;
        case -1613589672: // language
          this.language = castToString(value); // StringType
          return value;
        case -1795452264: // expression
          this.expression = castToString(value); // StringType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("description")) {
          this.description = castToString(value); // StringType
        } else if (name.equals("language")) {
          this.language = castToString(value); // StringType
        } else if (name.equals("expression")) {
          this.expression = castToString(value); // StringType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1724546052:  return getDescriptionElement();
        case -1613589672:  return getLanguageElement();
        case -1795452264:  return getExpressionElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1724546052: /*description*/ return new String[] {"string"};
        case -1613589672: /*language*/ return new String[] {"string"};
        case -1795452264: /*expression*/ return new String[] {"string"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type ChargeItemDefinition.description");
        }
        else if (name.equals("language")) {
          throw new FHIRException("Cannot call addChild on a primitive type ChargeItemDefinition.language");
        }
        else if (name.equals("expression")) {
          throw new FHIRException("Cannot call addChild on a primitive type ChargeItemDefinition.expression");
        }
        else
          return super.addChild(name);
      }

      public ChargeItemDefinitionApplicabilityComponent copy() {
        ChargeItemDefinitionApplicabilityComponent dst = new ChargeItemDefinitionApplicabilityComponent();
        copyValues(dst);
        dst.description = description == null ? null : description.copy();
        dst.language = language == null ? null : language.copy();
        dst.expression = expression == null ? null : expression.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ChargeItemDefinitionApplicabilityComponent))
          return false;
        ChargeItemDefinitionApplicabilityComponent o = (ChargeItemDefinitionApplicabilityComponent) other_;
        return compareDeep(description, o.description, true) && compareDeep(language, o.language, true)
           && compareDeep(expression, o.expression, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ChargeItemDefinitionApplicabilityComponent))
          return false;
        ChargeItemDefinitionApplicabilityComponent o = (ChargeItemDefinitionApplicabilityComponent) other_;
        return compareValues(description, o.description, true) && compareValues(language, o.language, true)
           && compareValues(expression, o.expression, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(description, language, expression
          );
      }

  public String fhirType() {
    return "ChargeItemDefinition.applicability";

  }

  }

    @Block()
    public static class ChargeItemDefinitionPropertyGroupComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Expressions that describe applicability criteria for the priceComponent.
         */
        @Child(name = "applicability", type = {ChargeItemDefinitionApplicabilityComponent.class}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Conditions under which the priceComponent is applicable", formalDefinition="Expressions that describe applicability criteria for the priceComponent." )
        protected List<ChargeItemDefinitionApplicabilityComponent> applicability;

        /**
         * The price for a ChargeItem may be calculated as a base price with surcharges/deductions that apply in certain conditions. A ChargeItemDefinition resource that defines the prices, factors and conditions that apply to a billing code is currently under developement. The priceComponent element can be used to offer transparency to the recipient of the Invoice of how the prices have been calculated.
         */
        @Child(name = "priceComponent", type = {}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Components of total line item price", formalDefinition="The price for a ChargeItem may be calculated as a base price with surcharges/deductions that apply in certain conditions. A ChargeItemDefinition resource that defines the prices, factors and conditions that apply to a billing code is currently under developement. The priceComponent element can be used to offer transparency to the recipient of the Invoice of how the prices have been calculated." )
        protected List<ChargeItemDefinitionPropertyGroupPriceComponentComponent> priceComponent;

        private static final long serialVersionUID = 1723436176L;

    /**
     * Constructor
     */
      public ChargeItemDefinitionPropertyGroupComponent() {
        super();
      }

        /**
         * @return {@link #applicability} (Expressions that describe applicability criteria for the priceComponent.)
         */
        public List<ChargeItemDefinitionApplicabilityComponent> getApplicability() { 
          if (this.applicability == null)
            this.applicability = new ArrayList<ChargeItemDefinitionApplicabilityComponent>();
          return this.applicability;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ChargeItemDefinitionPropertyGroupComponent setApplicability(List<ChargeItemDefinitionApplicabilityComponent> theApplicability) { 
          this.applicability = theApplicability;
          return this;
        }

        public boolean hasApplicability() { 
          if (this.applicability == null)
            return false;
          for (ChargeItemDefinitionApplicabilityComponent item : this.applicability)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public ChargeItemDefinitionApplicabilityComponent addApplicability() { //3
          ChargeItemDefinitionApplicabilityComponent t = new ChargeItemDefinitionApplicabilityComponent();
          if (this.applicability == null)
            this.applicability = new ArrayList<ChargeItemDefinitionApplicabilityComponent>();
          this.applicability.add(t);
          return t;
        }

        public ChargeItemDefinitionPropertyGroupComponent addApplicability(ChargeItemDefinitionApplicabilityComponent t) { //3
          if (t == null)
            return this;
          if (this.applicability == null)
            this.applicability = new ArrayList<ChargeItemDefinitionApplicabilityComponent>();
          this.applicability.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #applicability}, creating it if it does not already exist
         */
        public ChargeItemDefinitionApplicabilityComponent getApplicabilityFirstRep() { 
          if (getApplicability().isEmpty()) {
            addApplicability();
          }
          return getApplicability().get(0);
        }

        /**
         * @return {@link #priceComponent} (The price for a ChargeItem may be calculated as a base price with surcharges/deductions that apply in certain conditions. A ChargeItemDefinition resource that defines the prices, factors and conditions that apply to a billing code is currently under developement. The priceComponent element can be used to offer transparency to the recipient of the Invoice of how the prices have been calculated.)
         */
        public List<ChargeItemDefinitionPropertyGroupPriceComponentComponent> getPriceComponent() { 
          if (this.priceComponent == null)
            this.priceComponent = new ArrayList<ChargeItemDefinitionPropertyGroupPriceComponentComponent>();
          return this.priceComponent;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ChargeItemDefinitionPropertyGroupComponent setPriceComponent(List<ChargeItemDefinitionPropertyGroupPriceComponentComponent> thePriceComponent) { 
          this.priceComponent = thePriceComponent;
          return this;
        }

        public boolean hasPriceComponent() { 
          if (this.priceComponent == null)
            return false;
          for (ChargeItemDefinitionPropertyGroupPriceComponentComponent item : this.priceComponent)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public ChargeItemDefinitionPropertyGroupPriceComponentComponent addPriceComponent() { //3
          ChargeItemDefinitionPropertyGroupPriceComponentComponent t = new ChargeItemDefinitionPropertyGroupPriceComponentComponent();
          if (this.priceComponent == null)
            this.priceComponent = new ArrayList<ChargeItemDefinitionPropertyGroupPriceComponentComponent>();
          this.priceComponent.add(t);
          return t;
        }

        public ChargeItemDefinitionPropertyGroupComponent addPriceComponent(ChargeItemDefinitionPropertyGroupPriceComponentComponent t) { //3
          if (t == null)
            return this;
          if (this.priceComponent == null)
            this.priceComponent = new ArrayList<ChargeItemDefinitionPropertyGroupPriceComponentComponent>();
          this.priceComponent.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #priceComponent}, creating it if it does not already exist
         */
        public ChargeItemDefinitionPropertyGroupPriceComponentComponent getPriceComponentFirstRep() { 
          if (getPriceComponent().isEmpty()) {
            addPriceComponent();
          }
          return getPriceComponent().get(0);
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("applicability", "@ChargeItemDefinition.applicability", "Expressions that describe applicability criteria for the priceComponent.", 0, java.lang.Integer.MAX_VALUE, applicability));
          children.add(new Property("priceComponent", "", "The price for a ChargeItem may be calculated as a base price with surcharges/deductions that apply in certain conditions. A ChargeItemDefinition resource that defines the prices, factors and conditions that apply to a billing code is currently under developement. The priceComponent element can be used to offer transparency to the recipient of the Invoice of how the prices have been calculated.", 0, java.lang.Integer.MAX_VALUE, priceComponent));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -1526770491: /*applicability*/  return new Property("applicability", "@ChargeItemDefinition.applicability", "Expressions that describe applicability criteria for the priceComponent.", 0, java.lang.Integer.MAX_VALUE, applicability);
          case 1219095988: /*priceComponent*/  return new Property("priceComponent", "", "The price for a ChargeItem may be calculated as a base price with surcharges/deductions that apply in certain conditions. A ChargeItemDefinition resource that defines the prices, factors and conditions that apply to a billing code is currently under developement. The priceComponent element can be used to offer transparency to the recipient of the Invoice of how the prices have been calculated.", 0, java.lang.Integer.MAX_VALUE, priceComponent);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1526770491: /*applicability*/ return this.applicability == null ? new Base[0] : this.applicability.toArray(new Base[this.applicability.size()]); // ChargeItemDefinitionApplicabilityComponent
        case 1219095988: /*priceComponent*/ return this.priceComponent == null ? new Base[0] : this.priceComponent.toArray(new Base[this.priceComponent.size()]); // ChargeItemDefinitionPropertyGroupPriceComponentComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1526770491: // applicability
          this.getApplicability().add((ChargeItemDefinitionApplicabilityComponent) value); // ChargeItemDefinitionApplicabilityComponent
          return value;
        case 1219095988: // priceComponent
          this.getPriceComponent().add((ChargeItemDefinitionPropertyGroupPriceComponentComponent) value); // ChargeItemDefinitionPropertyGroupPriceComponentComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("applicability")) {
          this.getApplicability().add((ChargeItemDefinitionApplicabilityComponent) value);
        } else if (name.equals("priceComponent")) {
          this.getPriceComponent().add((ChargeItemDefinitionPropertyGroupPriceComponentComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1526770491:  return addApplicability(); 
        case 1219095988:  return addPriceComponent(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1526770491: /*applicability*/ return new String[] {"@ChargeItemDefinition.applicability"};
        case 1219095988: /*priceComponent*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("applicability")) {
          return addApplicability();
        }
        else if (name.equals("priceComponent")) {
          return addPriceComponent();
        }
        else
          return super.addChild(name);
      }

      public ChargeItemDefinitionPropertyGroupComponent copy() {
        ChargeItemDefinitionPropertyGroupComponent dst = new ChargeItemDefinitionPropertyGroupComponent();
        copyValues(dst);
        if (applicability != null) {
          dst.applicability = new ArrayList<ChargeItemDefinitionApplicabilityComponent>();
          for (ChargeItemDefinitionApplicabilityComponent i : applicability)
            dst.applicability.add(i.copy());
        };
        if (priceComponent != null) {
          dst.priceComponent = new ArrayList<ChargeItemDefinitionPropertyGroupPriceComponentComponent>();
          for (ChargeItemDefinitionPropertyGroupPriceComponentComponent i : priceComponent)
            dst.priceComponent.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ChargeItemDefinitionPropertyGroupComponent))
          return false;
        ChargeItemDefinitionPropertyGroupComponent o = (ChargeItemDefinitionPropertyGroupComponent) other_;
        return compareDeep(applicability, o.applicability, true) && compareDeep(priceComponent, o.priceComponent, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ChargeItemDefinitionPropertyGroupComponent))
          return false;
        ChargeItemDefinitionPropertyGroupComponent o = (ChargeItemDefinitionPropertyGroupComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(applicability, priceComponent
          );
      }

  public String fhirType() {
    return "ChargeItemDefinition.propertyGroup";

  }

  }

    @Block()
    public static class ChargeItemDefinitionPropertyGroupPriceComponentComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * This code identifies the type of the component.
         */
        @Child(name = "type", type = {CodeType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="base | surcharge | deduction | discount | tax | informational", formalDefinition="This code identifies the type of the component." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/invoice-priceComponentType")
        protected Enumeration<ChargeItemDefinitionPriceComponentType> type;

        /**
         * A code that identifies the component. Codes may be used to differentiate between kinds of taxes, surcharges, discounts etc.
         */
        @Child(name = "code", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Code identifying the specific component", formalDefinition="A code that identifies the component. Codes may be used to differentiate between kinds of taxes, surcharges, discounts etc." )
        protected CodeableConcept code;

        /**
         * The factor that has been applied on the base price for calculating this component.
         */
        @Child(name = "factor", type = {DecimalType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Factor used for calculating this component", formalDefinition="The factor that has been applied on the base price for calculating this component." )
        protected DecimalType factor;

        /**
         * The amount calculated for this component.
         */
        @Child(name = "amount", type = {Money.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Monetary amount associated with this component", formalDefinition="The amount calculated for this component." )
        protected Money amount;

        private static final long serialVersionUID = -841451335L;

    /**
     * Constructor
     */
      public ChargeItemDefinitionPropertyGroupPriceComponentComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ChargeItemDefinitionPropertyGroupPriceComponentComponent(Enumeration<ChargeItemDefinitionPriceComponentType> type) {
        super();
        this.type = type;
      }

        /**
         * @return {@link #type} (This code identifies the type of the component.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public Enumeration<ChargeItemDefinitionPriceComponentType> getTypeElement() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ChargeItemDefinitionPropertyGroupPriceComponentComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new Enumeration<ChargeItemDefinitionPriceComponentType>(new ChargeItemDefinitionPriceComponentTypeEnumFactory()); // bb
          return this.type;
        }

        public boolean hasTypeElement() { 
          return this.type != null && !this.type.isEmpty();
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (This code identifies the type of the component.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public ChargeItemDefinitionPropertyGroupPriceComponentComponent setTypeElement(Enumeration<ChargeItemDefinitionPriceComponentType> value) { 
          this.type = value;
          return this;
        }

        /**
         * @return This code identifies the type of the component.
         */
        public ChargeItemDefinitionPriceComponentType getType() { 
          return this.type == null ? null : this.type.getValue();
        }

        /**
         * @param value This code identifies the type of the component.
         */
        public ChargeItemDefinitionPropertyGroupPriceComponentComponent setType(ChargeItemDefinitionPriceComponentType value) { 
            if (this.type == null)
              this.type = new Enumeration<ChargeItemDefinitionPriceComponentType>(new ChargeItemDefinitionPriceComponentTypeEnumFactory());
            this.type.setValue(value);
          return this;
        }

        /**
         * @return {@link #code} (A code that identifies the component. Codes may be used to differentiate between kinds of taxes, surcharges, discounts etc.)
         */
        public CodeableConcept getCode() { 
          if (this.code == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ChargeItemDefinitionPropertyGroupPriceComponentComponent.code");
            else if (Configuration.doAutoCreate())
              this.code = new CodeableConcept(); // cc
          return this.code;
        }

        public boolean hasCode() { 
          return this.code != null && !this.code.isEmpty();
        }

        /**
         * @param value {@link #code} (A code that identifies the component. Codes may be used to differentiate between kinds of taxes, surcharges, discounts etc.)
         */
        public ChargeItemDefinitionPropertyGroupPriceComponentComponent setCode(CodeableConcept value) { 
          this.code = value;
          return this;
        }

        /**
         * @return {@link #factor} (The factor that has been applied on the base price for calculating this component.). This is the underlying object with id, value and extensions. The accessor "getFactor" gives direct access to the value
         */
        public DecimalType getFactorElement() { 
          if (this.factor == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ChargeItemDefinitionPropertyGroupPriceComponentComponent.factor");
            else if (Configuration.doAutoCreate())
              this.factor = new DecimalType(); // bb
          return this.factor;
        }

        public boolean hasFactorElement() { 
          return this.factor != null && !this.factor.isEmpty();
        }

        public boolean hasFactor() { 
          return this.factor != null && !this.factor.isEmpty();
        }

        /**
         * @param value {@link #factor} (The factor that has been applied on the base price for calculating this component.). This is the underlying object with id, value and extensions. The accessor "getFactor" gives direct access to the value
         */
        public ChargeItemDefinitionPropertyGroupPriceComponentComponent setFactorElement(DecimalType value) { 
          this.factor = value;
          return this;
        }

        /**
         * @return The factor that has been applied on the base price for calculating this component.
         */
        public BigDecimal getFactor() { 
          return this.factor == null ? null : this.factor.getValue();
        }

        /**
         * @param value The factor that has been applied on the base price for calculating this component.
         */
        public ChargeItemDefinitionPropertyGroupPriceComponentComponent setFactor(BigDecimal value) { 
          if (value == null)
            this.factor = null;
          else {
            if (this.factor == null)
              this.factor = new DecimalType();
            this.factor.setValue(value);
          }
          return this;
        }

        /**
         * @param value The factor that has been applied on the base price for calculating this component.
         */
        public ChargeItemDefinitionPropertyGroupPriceComponentComponent setFactor(long value) { 
              this.factor = new DecimalType();
            this.factor.setValue(value);
          return this;
        }

        /**
         * @param value The factor that has been applied on the base price for calculating this component.
         */
        public ChargeItemDefinitionPropertyGroupPriceComponentComponent setFactor(double value) { 
              this.factor = new DecimalType();
            this.factor.setValue(value);
          return this;
        }

        /**
         * @return {@link #amount} (The amount calculated for this component.)
         */
        public Money getAmount() { 
          if (this.amount == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ChargeItemDefinitionPropertyGroupPriceComponentComponent.amount");
            else if (Configuration.doAutoCreate())
              this.amount = new Money(); // cc
          return this.amount;
        }

        public boolean hasAmount() { 
          return this.amount != null && !this.amount.isEmpty();
        }

        /**
         * @param value {@link #amount} (The amount calculated for this component.)
         */
        public ChargeItemDefinitionPropertyGroupPriceComponentComponent setAmount(Money value) { 
          this.amount = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("type", "code", "This code identifies the type of the component.", 0, 1, type));
          children.add(new Property("code", "CodeableConcept", "A code that identifies the component. Codes may be used to differentiate between kinds of taxes, surcharges, discounts etc.", 0, 1, code));
          children.add(new Property("factor", "decimal", "The factor that has been applied on the base price for calculating this component.", 0, 1, factor));
          children.add(new Property("amount", "Money", "The amount calculated for this component.", 0, 1, amount));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3575610: /*type*/  return new Property("type", "code", "This code identifies the type of the component.", 0, 1, type);
          case 3059181: /*code*/  return new Property("code", "CodeableConcept", "A code that identifies the component. Codes may be used to differentiate between kinds of taxes, surcharges, discounts etc.", 0, 1, code);
          case -1282148017: /*factor*/  return new Property("factor", "decimal", "The factor that has been applied on the base price for calculating this component.", 0, 1, factor);
          case -1413853096: /*amount*/  return new Property("amount", "Money", "The amount calculated for this component.", 0, 1, amount);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // Enumeration<ChargeItemDefinitionPriceComponentType>
        case 3059181: /*code*/ return this.code == null ? new Base[0] : new Base[] {this.code}; // CodeableConcept
        case -1282148017: /*factor*/ return this.factor == null ? new Base[0] : new Base[] {this.factor}; // DecimalType
        case -1413853096: /*amount*/ return this.amount == null ? new Base[0] : new Base[] {this.amount}; // Money
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          value = new ChargeItemDefinitionPriceComponentTypeEnumFactory().fromType(castToCode(value));
          this.type = (Enumeration) value; // Enumeration<ChargeItemDefinitionPriceComponentType>
          return value;
        case 3059181: // code
          this.code = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1282148017: // factor
          this.factor = castToDecimal(value); // DecimalType
          return value;
        case -1413853096: // amount
          this.amount = castToMoney(value); // Money
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type")) {
          value = new ChargeItemDefinitionPriceComponentTypeEnumFactory().fromType(castToCode(value));
          this.type = (Enumeration) value; // Enumeration<ChargeItemDefinitionPriceComponentType>
        } else if (name.equals("code")) {
          this.code = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("factor")) {
          this.factor = castToDecimal(value); // DecimalType
        } else if (name.equals("amount")) {
          this.amount = castToMoney(value); // Money
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610:  return getTypeElement();
        case 3059181:  return getCode(); 
        case -1282148017:  return getFactorElement();
        case -1413853096:  return getAmount(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return new String[] {"code"};
        case 3059181: /*code*/ return new String[] {"CodeableConcept"};
        case -1282148017: /*factor*/ return new String[] {"decimal"};
        case -1413853096: /*amount*/ return new String[] {"Money"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          throw new FHIRException("Cannot call addChild on a primitive type ChargeItemDefinition.type");
        }
        else if (name.equals("code")) {
          this.code = new CodeableConcept();
          return this.code;
        }
        else if (name.equals("factor")) {
          throw new FHIRException("Cannot call addChild on a primitive type ChargeItemDefinition.factor");
        }
        else if (name.equals("amount")) {
          this.amount = new Money();
          return this.amount;
        }
        else
          return super.addChild(name);
      }

      public ChargeItemDefinitionPropertyGroupPriceComponentComponent copy() {
        ChargeItemDefinitionPropertyGroupPriceComponentComponent dst = new ChargeItemDefinitionPropertyGroupPriceComponentComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.code = code == null ? null : code.copy();
        dst.factor = factor == null ? null : factor.copy();
        dst.amount = amount == null ? null : amount.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ChargeItemDefinitionPropertyGroupPriceComponentComponent))
          return false;
        ChargeItemDefinitionPropertyGroupPriceComponentComponent o = (ChargeItemDefinitionPropertyGroupPriceComponentComponent) other_;
        return compareDeep(type, o.type, true) && compareDeep(code, o.code, true) && compareDeep(factor, o.factor, true)
           && compareDeep(amount, o.amount, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ChargeItemDefinitionPropertyGroupPriceComponentComponent))
          return false;
        ChargeItemDefinitionPropertyGroupPriceComponentComponent o = (ChargeItemDefinitionPropertyGroupPriceComponentComponent) other_;
        return compareValues(type, o.type, true) && compareValues(factor, o.factor, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(type, code, factor, amount
          );
      }

  public String fhirType() {
    return "ChargeItemDefinition.propertyGroup.priceComponent";

  }

  }

    /**
     * A formal identifier that is used to identify this charge item definition when it is represented in other formats, or referenced in a specification, model, design or an instance.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Additional identifier for the charge item definition", formalDefinition="A formal identifier that is used to identify this charge item definition when it is represented in other formats, or referenced in a specification, model, design or an instance." )
    protected List<Identifier> identifier;

    /**
     * The URL pointing to an externally-defined charge item definition that is adhered to in whole or in part by this definition.
     */
    @Child(name = "derivedFromUri", type = {UriType.class}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Underlying externally-defined charge item definition", formalDefinition="The URL pointing to an externally-defined charge item definition that is adhered to in whole or in part by this definition." )
    protected List<UriType> derivedFromUri;

    /**
     * A larger definition of which this particular definition is a component or step.
     */
    @Child(name = "partOf", type = {CanonicalType.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="A larger definition of which this particular definition is a component or step", formalDefinition="A larger definition of which this particular definition is a component or step." )
    protected List<CanonicalType> partOf;

    /**
     * As new versions of a protocol or guideline are defined, allows identification of what versions are replaced by a new instance.
     */
    @Child(name = "replaces", type = {CanonicalType.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Completed or terminated request(s) whose function is taken by this new request", formalDefinition="As new versions of a protocol or guideline are defined, allows identification of what versions are replaced by a new instance." )
    protected List<CanonicalType> replaces;

    /**
     * A copyright statement relating to the charge item definition and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the charge item definition.
     */
    @Child(name = "copyright", type = {MarkdownType.class}, order=4, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Use and/or publishing restrictions", formalDefinition="A copyright statement relating to the charge item definition and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the charge item definition." )
    protected MarkdownType copyright;

    /**
     * The date on which the resource content was approved by the publisher. Approval happens once when the content is officially approved for usage.
     */
    @Child(name = "approvalDate", type = {DateType.class}, order=5, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="When the charge item definition was approved by publisher", formalDefinition="The date on which the resource content was approved by the publisher. Approval happens once when the content is officially approved for usage." )
    protected DateType approvalDate;

    /**
     * The date on which the resource content was last reviewed. Review happens periodically after approval but does not change the original approval date.
     */
    @Child(name = "lastReviewDate", type = {DateType.class}, order=6, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="When the charge item definition was last reviewed", formalDefinition="The date on which the resource content was last reviewed. Review happens periodically after approval but does not change the original approval date." )
    protected DateType lastReviewDate;

    /**
     * The period during which the charge item definition content was or is planned to be in active use.
     */
    @Child(name = "effectivePeriod", type = {Period.class}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When the charge item definition is expected to be used", formalDefinition="The period during which the charge item definition content was or is planned to be in active use." )
    protected Period effectivePeriod;

    /**
     * The defined billing details in this resource pertain to the given billing code.
     */
    @Child(name = "code", type = {CodeableConcept.class}, order=8, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Billing codes or product types this definition applies to", formalDefinition="The defined billing details in this resource pertain to the given billing code." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/chargeitem-billingcodes")
    protected CodeableConcept code;

    /**
     * The defined billing details in this resource pertain to the given product instance(s).
     */
    @Child(name = "instance", type = {Medication.class, Substance.class, Device.class}, order=9, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Instances this definition applies to", formalDefinition="The defined billing details in this resource pertain to the given product instance(s)." )
    protected List<Reference> instance;
    /**
     * The actual objects that are the target of the reference (The defined billing details in this resource pertain to the given product instance(s).)
     */
    protected List<Resource> instanceTarget;


    /**
     * Expressions that describe applicability criteria for the billing code.
     */
    @Child(name = "applicability", type = {}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Whether or not the billing code is applicable", formalDefinition="Expressions that describe applicability criteria for the billing code." )
    protected List<ChargeItemDefinitionApplicabilityComponent> applicability;

    /**
     * Group of properties which are applicable under the same conditions. If no applicability rules are established for the group, then all properties always apply.
     */
    @Child(name = "propertyGroup", type = {}, order=11, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Group of properties which are applicable under the same conditions", formalDefinition="Group of properties which are applicable under the same conditions. If no applicability rules are established for the group, then all properties always apply." )
    protected List<ChargeItemDefinitionPropertyGroupComponent> propertyGroup;

    private static final long serialVersionUID = -583681330L;

  /**
   * Constructor
   */
    public ChargeItemDefinition() {
      super();
    }

  /**
   * Constructor
   */
    public ChargeItemDefinition(UriType url, Enumeration<PublicationStatus> status) {
      super();
      this.url = url;
      this.status = status;
    }

    /**
     * @return {@link #url} (An absolute URI that is used to identify this charge item definition when it is referenced in a specification, model, design or an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal address at which at which an authoritative instance of this charge item definition is (or will be) published. This URL can be the target of a canonical reference. It SHALL remain the same when the charge item definition is stored on different servers.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public UriType getUrlElement() { 
      if (this.url == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ChargeItemDefinition.url");
        else if (Configuration.doAutoCreate())
          this.url = new UriType(); // bb
      return this.url;
    }

    public boolean hasUrlElement() { 
      return this.url != null && !this.url.isEmpty();
    }

    public boolean hasUrl() { 
      return this.url != null && !this.url.isEmpty();
    }

    /**
     * @param value {@link #url} (An absolute URI that is used to identify this charge item definition when it is referenced in a specification, model, design or an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal address at which at which an authoritative instance of this charge item definition is (or will be) published. This URL can be the target of a canonical reference. It SHALL remain the same when the charge item definition is stored on different servers.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public ChargeItemDefinition setUrlElement(UriType value) { 
      this.url = value;
      return this;
    }

    /**
     * @return An absolute URI that is used to identify this charge item definition when it is referenced in a specification, model, design or an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal address at which at which an authoritative instance of this charge item definition is (or will be) published. This URL can be the target of a canonical reference. It SHALL remain the same when the charge item definition is stored on different servers.
     */
    public String getUrl() { 
      return this.url == null ? null : this.url.getValue();
    }

    /**
     * @param value An absolute URI that is used to identify this charge item definition when it is referenced in a specification, model, design or an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal address at which at which an authoritative instance of this charge item definition is (or will be) published. This URL can be the target of a canonical reference. It SHALL remain the same when the charge item definition is stored on different servers.
     */
    public ChargeItemDefinition setUrl(String value) { 
        if (this.url == null)
          this.url = new UriType();
        this.url.setValue(value);
      return this;
    }

    /**
     * @return {@link #identifier} (A formal identifier that is used to identify this charge item definition when it is represented in other formats, or referenced in a specification, model, design or an instance.)
     */
    public List<Identifier> getIdentifier() { 
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      return this.identifier;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ChargeItemDefinition setIdentifier(List<Identifier> theIdentifier) { 
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

    public ChargeItemDefinition addIdentifier(Identifier t) { //3
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
     * @return {@link #version} (The identifier that is used to identify this version of the charge item definition when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the charge item definition author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge assets, refer to the Decision Support Service specification. Note that a version is required for non-experimental active assets.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
     */
    public StringType getVersionElement() { 
      if (this.version == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ChargeItemDefinition.version");
        else if (Configuration.doAutoCreate())
          this.version = new StringType(); // bb
      return this.version;
    }

    public boolean hasVersionElement() { 
      return this.version != null && !this.version.isEmpty();
    }

    public boolean hasVersion() { 
      return this.version != null && !this.version.isEmpty();
    }

    /**
     * @param value {@link #version} (The identifier that is used to identify this version of the charge item definition when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the charge item definition author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge assets, refer to the Decision Support Service specification. Note that a version is required for non-experimental active assets.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
     */
    public ChargeItemDefinition setVersionElement(StringType value) { 
      this.version = value;
      return this;
    }

    /**
     * @return The identifier that is used to identify this version of the charge item definition when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the charge item definition author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge assets, refer to the Decision Support Service specification. Note that a version is required for non-experimental active assets.
     */
    public String getVersion() { 
      return this.version == null ? null : this.version.getValue();
    }

    /**
     * @param value The identifier that is used to identify this version of the charge item definition when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the charge item definition author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge assets, refer to the Decision Support Service specification. Note that a version is required for non-experimental active assets.
     */
    public ChargeItemDefinition setVersion(String value) { 
      if (Utilities.noString(value))
        this.version = null;
      else {
        if (this.version == null)
          this.version = new StringType();
        this.version.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #title} (A short, descriptive, user-friendly title for the charge item definition.). This is the underlying object with id, value and extensions. The accessor "getTitle" gives direct access to the value
     */
    public StringType getTitleElement() { 
      if (this.title == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ChargeItemDefinition.title");
        else if (Configuration.doAutoCreate())
          this.title = new StringType(); // bb
      return this.title;
    }

    public boolean hasTitleElement() { 
      return this.title != null && !this.title.isEmpty();
    }

    public boolean hasTitle() { 
      return this.title != null && !this.title.isEmpty();
    }

    /**
     * @param value {@link #title} (A short, descriptive, user-friendly title for the charge item definition.). This is the underlying object with id, value and extensions. The accessor "getTitle" gives direct access to the value
     */
    public ChargeItemDefinition setTitleElement(StringType value) { 
      this.title = value;
      return this;
    }

    /**
     * @return A short, descriptive, user-friendly title for the charge item definition.
     */
    public String getTitle() { 
      return this.title == null ? null : this.title.getValue();
    }

    /**
     * @param value A short, descriptive, user-friendly title for the charge item definition.
     */
    public ChargeItemDefinition setTitle(String value) { 
      if (Utilities.noString(value))
        this.title = null;
      else {
        if (this.title == null)
          this.title = new StringType();
        this.title.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #derivedFromUri} (The URL pointing to an externally-defined charge item definition that is adhered to in whole or in part by this definition.)
     */
    public List<UriType> getDerivedFromUri() { 
      if (this.derivedFromUri == null)
        this.derivedFromUri = new ArrayList<UriType>();
      return this.derivedFromUri;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ChargeItemDefinition setDerivedFromUri(List<UriType> theDerivedFromUri) { 
      this.derivedFromUri = theDerivedFromUri;
      return this;
    }

    public boolean hasDerivedFromUri() { 
      if (this.derivedFromUri == null)
        return false;
      for (UriType item : this.derivedFromUri)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #derivedFromUri} (The URL pointing to an externally-defined charge item definition that is adhered to in whole or in part by this definition.)
     */
    public UriType addDerivedFromUriElement() {//2 
      UriType t = new UriType();
      if (this.derivedFromUri == null)
        this.derivedFromUri = new ArrayList<UriType>();
      this.derivedFromUri.add(t);
      return t;
    }

    /**
     * @param value {@link #derivedFromUri} (The URL pointing to an externally-defined charge item definition that is adhered to in whole or in part by this definition.)
     */
    public ChargeItemDefinition addDerivedFromUri(String value) { //1
      UriType t = new UriType();
      t.setValue(value);
      if (this.derivedFromUri == null)
        this.derivedFromUri = new ArrayList<UriType>();
      this.derivedFromUri.add(t);
      return this;
    }

    /**
     * @param value {@link #derivedFromUri} (The URL pointing to an externally-defined charge item definition that is adhered to in whole or in part by this definition.)
     */
    public boolean hasDerivedFromUri(String value) { 
      if (this.derivedFromUri == null)
        return false;
      for (UriType v : this.derivedFromUri)
        if (v.getValue().equals(value)) // uri
          return true;
      return false;
    }

    /**
     * @return {@link #partOf} (A larger definition of which this particular definition is a component or step.)
     */
    public List<CanonicalType> getPartOf() { 
      if (this.partOf == null)
        this.partOf = new ArrayList<CanonicalType>();
      return this.partOf;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ChargeItemDefinition setPartOf(List<CanonicalType> thePartOf) { 
      this.partOf = thePartOf;
      return this;
    }

    public boolean hasPartOf() { 
      if (this.partOf == null)
        return false;
      for (CanonicalType item : this.partOf)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #partOf} (A larger definition of which this particular definition is a component or step.)
     */
    public CanonicalType addPartOfElement() {//2 
      CanonicalType t = new CanonicalType();
      if (this.partOf == null)
        this.partOf = new ArrayList<CanonicalType>();
      this.partOf.add(t);
      return t;
    }

    /**
     * @param value {@link #partOf} (A larger definition of which this particular definition is a component or step.)
     */
    public ChargeItemDefinition addPartOf(String value) { //1
      CanonicalType t = new CanonicalType();
      t.setValue(value);
      if (this.partOf == null)
        this.partOf = new ArrayList<CanonicalType>();
      this.partOf.add(t);
      return this;
    }

    /**
     * @param value {@link #partOf} (A larger definition of which this particular definition is a component or step.)
     */
    public boolean hasPartOf(String value) { 
      if (this.partOf == null)
        return false;
      for (CanonicalType v : this.partOf)
        if (v.getValue().equals(value)) // canonical(ChargeItemDefinition)
          return true;
      return false;
    }

    /**
     * @return {@link #replaces} (As new versions of a protocol or guideline are defined, allows identification of what versions are replaced by a new instance.)
     */
    public List<CanonicalType> getReplaces() { 
      if (this.replaces == null)
        this.replaces = new ArrayList<CanonicalType>();
      return this.replaces;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ChargeItemDefinition setReplaces(List<CanonicalType> theReplaces) { 
      this.replaces = theReplaces;
      return this;
    }

    public boolean hasReplaces() { 
      if (this.replaces == null)
        return false;
      for (CanonicalType item : this.replaces)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #replaces} (As new versions of a protocol or guideline are defined, allows identification of what versions are replaced by a new instance.)
     */
    public CanonicalType addReplacesElement() {//2 
      CanonicalType t = new CanonicalType();
      if (this.replaces == null)
        this.replaces = new ArrayList<CanonicalType>();
      this.replaces.add(t);
      return t;
    }

    /**
     * @param value {@link #replaces} (As new versions of a protocol or guideline are defined, allows identification of what versions are replaced by a new instance.)
     */
    public ChargeItemDefinition addReplaces(String value) { //1
      CanonicalType t = new CanonicalType();
      t.setValue(value);
      if (this.replaces == null)
        this.replaces = new ArrayList<CanonicalType>();
      this.replaces.add(t);
      return this;
    }

    /**
     * @param value {@link #replaces} (As new versions of a protocol or guideline are defined, allows identification of what versions are replaced by a new instance.)
     */
    public boolean hasReplaces(String value) { 
      if (this.replaces == null)
        return false;
      for (CanonicalType v : this.replaces)
        if (v.getValue().equals(value)) // canonical(ChargeItemDefinition)
          return true;
      return false;
    }

    /**
     * @return {@link #status} (The current state of the ChargeItemDefinition.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<PublicationStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ChargeItemDefinition.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<PublicationStatus>(new PublicationStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (The current state of the ChargeItemDefinition.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public ChargeItemDefinition setStatusElement(Enumeration<PublicationStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The current state of the ChargeItemDefinition.
     */
    public PublicationStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The current state of the ChargeItemDefinition.
     */
    public ChargeItemDefinition setStatus(PublicationStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<PublicationStatus>(new PublicationStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #experimental} (A Boolean value to indicate that this charge item definition is authored for testing purposes (or education/evaluation/marketing) and is not intended to be used for genuine usage.). This is the underlying object with id, value and extensions. The accessor "getExperimental" gives direct access to the value
     */
    public BooleanType getExperimentalElement() { 
      if (this.experimental == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ChargeItemDefinition.experimental");
        else if (Configuration.doAutoCreate())
          this.experimental = new BooleanType(); // bb
      return this.experimental;
    }

    public boolean hasExperimentalElement() { 
      return this.experimental != null && !this.experimental.isEmpty();
    }

    public boolean hasExperimental() { 
      return this.experimental != null && !this.experimental.isEmpty();
    }

    /**
     * @param value {@link #experimental} (A Boolean value to indicate that this charge item definition is authored for testing purposes (or education/evaluation/marketing) and is not intended to be used for genuine usage.). This is the underlying object with id, value and extensions. The accessor "getExperimental" gives direct access to the value
     */
    public ChargeItemDefinition setExperimentalElement(BooleanType value) { 
      this.experimental = value;
      return this;
    }

    /**
     * @return A Boolean value to indicate that this charge item definition is authored for testing purposes (or education/evaluation/marketing) and is not intended to be used for genuine usage.
     */
    public boolean getExperimental() { 
      return this.experimental == null || this.experimental.isEmpty() ? false : this.experimental.getValue();
    }

    /**
     * @param value A Boolean value to indicate that this charge item definition is authored for testing purposes (or education/evaluation/marketing) and is not intended to be used for genuine usage.
     */
    public ChargeItemDefinition setExperimental(boolean value) { 
        if (this.experimental == null)
          this.experimental = new BooleanType();
        this.experimental.setValue(value);
      return this;
    }

    /**
     * @return {@link #date} (The date  (and optionally time) when the charge item definition was published. The date must change when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the charge item definition changes.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public DateTimeType getDateElement() { 
      if (this.date == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ChargeItemDefinition.date");
        else if (Configuration.doAutoCreate())
          this.date = new DateTimeType(); // bb
      return this.date;
    }

    public boolean hasDateElement() { 
      return this.date != null && !this.date.isEmpty();
    }

    public boolean hasDate() { 
      return this.date != null && !this.date.isEmpty();
    }

    /**
     * @param value {@link #date} (The date  (and optionally time) when the charge item definition was published. The date must change when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the charge item definition changes.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public ChargeItemDefinition setDateElement(DateTimeType value) { 
      this.date = value;
      return this;
    }

    /**
     * @return The date  (and optionally time) when the charge item definition was published. The date must change when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the charge item definition changes.
     */
    public Date getDate() { 
      return this.date == null ? null : this.date.getValue();
    }

    /**
     * @param value The date  (and optionally time) when the charge item definition was published. The date must change when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the charge item definition changes.
     */
    public ChargeItemDefinition setDate(Date value) { 
      if (value == null)
        this.date = null;
      else {
        if (this.date == null)
          this.date = new DateTimeType();
        this.date.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #publisher} (The name of the organization or individual that published the charge item definition.). This is the underlying object with id, value and extensions. The accessor "getPublisher" gives direct access to the value
     */
    public StringType getPublisherElement() { 
      if (this.publisher == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ChargeItemDefinition.publisher");
        else if (Configuration.doAutoCreate())
          this.publisher = new StringType(); // bb
      return this.publisher;
    }

    public boolean hasPublisherElement() { 
      return this.publisher != null && !this.publisher.isEmpty();
    }

    public boolean hasPublisher() { 
      return this.publisher != null && !this.publisher.isEmpty();
    }

    /**
     * @param value {@link #publisher} (The name of the organization or individual that published the charge item definition.). This is the underlying object with id, value and extensions. The accessor "getPublisher" gives direct access to the value
     */
    public ChargeItemDefinition setPublisherElement(StringType value) { 
      this.publisher = value;
      return this;
    }

    /**
     * @return The name of the organization or individual that published the charge item definition.
     */
    public String getPublisher() { 
      return this.publisher == null ? null : this.publisher.getValue();
    }

    /**
     * @param value The name of the organization or individual that published the charge item definition.
     */
    public ChargeItemDefinition setPublisher(String value) { 
      if (Utilities.noString(value))
        this.publisher = null;
      else {
        if (this.publisher == null)
          this.publisher = new StringType();
        this.publisher.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #contact} (Contact details to assist a user in finding and communicating with the publisher.)
     */
    public List<ContactDetail> getContact() { 
      if (this.contact == null)
        this.contact = new ArrayList<ContactDetail>();
      return this.contact;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ChargeItemDefinition setContact(List<ContactDetail> theContact) { 
      this.contact = theContact;
      return this;
    }

    public boolean hasContact() { 
      if (this.contact == null)
        return false;
      for (ContactDetail item : this.contact)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public ContactDetail addContact() { //3
      ContactDetail t = new ContactDetail();
      if (this.contact == null)
        this.contact = new ArrayList<ContactDetail>();
      this.contact.add(t);
      return t;
    }

    public ChargeItemDefinition addContact(ContactDetail t) { //3
      if (t == null)
        return this;
      if (this.contact == null)
        this.contact = new ArrayList<ContactDetail>();
      this.contact.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #contact}, creating it if it does not already exist
     */
    public ContactDetail getContactFirstRep() { 
      if (getContact().isEmpty()) {
        addContact();
      }
      return getContact().get(0);
    }

    /**
     * @return {@link #description} (A free text natural language description of the charge item definition from a consumer's perspective.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public MarkdownType getDescriptionElement() { 
      if (this.description == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ChargeItemDefinition.description");
        else if (Configuration.doAutoCreate())
          this.description = new MarkdownType(); // bb
      return this.description;
    }

    public boolean hasDescriptionElement() { 
      return this.description != null && !this.description.isEmpty();
    }

    public boolean hasDescription() { 
      return this.description != null && !this.description.isEmpty();
    }

    /**
     * @param value {@link #description} (A free text natural language description of the charge item definition from a consumer's perspective.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public ChargeItemDefinition setDescriptionElement(MarkdownType value) { 
      this.description = value;
      return this;
    }

    /**
     * @return A free text natural language description of the charge item definition from a consumer's perspective.
     */
    public String getDescription() { 
      return this.description == null ? null : this.description.getValue();
    }

    /**
     * @param value A free text natural language description of the charge item definition from a consumer's perspective.
     */
    public ChargeItemDefinition setDescription(String value) { 
      if (value == null)
        this.description = null;
      else {
        if (this.description == null)
          this.description = new MarkdownType();
        this.description.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #useContext} (The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching for appropriate charge item definition instances.)
     */
    public List<UsageContext> getUseContext() { 
      if (this.useContext == null)
        this.useContext = new ArrayList<UsageContext>();
      return this.useContext;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ChargeItemDefinition setUseContext(List<UsageContext> theUseContext) { 
      this.useContext = theUseContext;
      return this;
    }

    public boolean hasUseContext() { 
      if (this.useContext == null)
        return false;
      for (UsageContext item : this.useContext)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public UsageContext addUseContext() { //3
      UsageContext t = new UsageContext();
      if (this.useContext == null)
        this.useContext = new ArrayList<UsageContext>();
      this.useContext.add(t);
      return t;
    }

    public ChargeItemDefinition addUseContext(UsageContext t) { //3
      if (t == null)
        return this;
      if (this.useContext == null)
        this.useContext = new ArrayList<UsageContext>();
      this.useContext.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #useContext}, creating it if it does not already exist
     */
    public UsageContext getUseContextFirstRep() { 
      if (getUseContext().isEmpty()) {
        addUseContext();
      }
      return getUseContext().get(0);
    }

    /**
     * @return {@link #jurisdiction} (A legal or geographic region in which the charge item definition is intended to be used.)
     */
    public List<CodeableConcept> getJurisdiction() { 
      if (this.jurisdiction == null)
        this.jurisdiction = new ArrayList<CodeableConcept>();
      return this.jurisdiction;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ChargeItemDefinition setJurisdiction(List<CodeableConcept> theJurisdiction) { 
      this.jurisdiction = theJurisdiction;
      return this;
    }

    public boolean hasJurisdiction() { 
      if (this.jurisdiction == null)
        return false;
      for (CodeableConcept item : this.jurisdiction)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addJurisdiction() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.jurisdiction == null)
        this.jurisdiction = new ArrayList<CodeableConcept>();
      this.jurisdiction.add(t);
      return t;
    }

    public ChargeItemDefinition addJurisdiction(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.jurisdiction == null)
        this.jurisdiction = new ArrayList<CodeableConcept>();
      this.jurisdiction.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #jurisdiction}, creating it if it does not already exist
     */
    public CodeableConcept getJurisdictionFirstRep() { 
      if (getJurisdiction().isEmpty()) {
        addJurisdiction();
      }
      return getJurisdiction().get(0);
    }

    /**
     * @return {@link #copyright} (A copyright statement relating to the charge item definition and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the charge item definition.). This is the underlying object with id, value and extensions. The accessor "getCopyright" gives direct access to the value
     */
    public MarkdownType getCopyrightElement() { 
      if (this.copyright == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ChargeItemDefinition.copyright");
        else if (Configuration.doAutoCreate())
          this.copyright = new MarkdownType(); // bb
      return this.copyright;
    }

    public boolean hasCopyrightElement() { 
      return this.copyright != null && !this.copyright.isEmpty();
    }

    public boolean hasCopyright() { 
      return this.copyright != null && !this.copyright.isEmpty();
    }

    /**
     * @param value {@link #copyright} (A copyright statement relating to the charge item definition and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the charge item definition.). This is the underlying object with id, value and extensions. The accessor "getCopyright" gives direct access to the value
     */
    public ChargeItemDefinition setCopyrightElement(MarkdownType value) { 
      this.copyright = value;
      return this;
    }

    /**
     * @return A copyright statement relating to the charge item definition and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the charge item definition.
     */
    public String getCopyright() { 
      return this.copyright == null ? null : this.copyright.getValue();
    }

    /**
     * @param value A copyright statement relating to the charge item definition and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the charge item definition.
     */
    public ChargeItemDefinition setCopyright(String value) { 
      if (value == null)
        this.copyright = null;
      else {
        if (this.copyright == null)
          this.copyright = new MarkdownType();
        this.copyright.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #approvalDate} (The date on which the resource content was approved by the publisher. Approval happens once when the content is officially approved for usage.). This is the underlying object with id, value and extensions. The accessor "getApprovalDate" gives direct access to the value
     */
    public DateType getApprovalDateElement() { 
      if (this.approvalDate == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ChargeItemDefinition.approvalDate");
        else if (Configuration.doAutoCreate())
          this.approvalDate = new DateType(); // bb
      return this.approvalDate;
    }

    public boolean hasApprovalDateElement() { 
      return this.approvalDate != null && !this.approvalDate.isEmpty();
    }

    public boolean hasApprovalDate() { 
      return this.approvalDate != null && !this.approvalDate.isEmpty();
    }

    /**
     * @param value {@link #approvalDate} (The date on which the resource content was approved by the publisher. Approval happens once when the content is officially approved for usage.). This is the underlying object with id, value and extensions. The accessor "getApprovalDate" gives direct access to the value
     */
    public ChargeItemDefinition setApprovalDateElement(DateType value) { 
      this.approvalDate = value;
      return this;
    }

    /**
     * @return The date on which the resource content was approved by the publisher. Approval happens once when the content is officially approved for usage.
     */
    public Date getApprovalDate() { 
      return this.approvalDate == null ? null : this.approvalDate.getValue();
    }

    /**
     * @param value The date on which the resource content was approved by the publisher. Approval happens once when the content is officially approved for usage.
     */
    public ChargeItemDefinition setApprovalDate(Date value) { 
      if (value == null)
        this.approvalDate = null;
      else {
        if (this.approvalDate == null)
          this.approvalDate = new DateType();
        this.approvalDate.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #lastReviewDate} (The date on which the resource content was last reviewed. Review happens periodically after approval but does not change the original approval date.). This is the underlying object with id, value and extensions. The accessor "getLastReviewDate" gives direct access to the value
     */
    public DateType getLastReviewDateElement() { 
      if (this.lastReviewDate == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ChargeItemDefinition.lastReviewDate");
        else if (Configuration.doAutoCreate())
          this.lastReviewDate = new DateType(); // bb
      return this.lastReviewDate;
    }

    public boolean hasLastReviewDateElement() { 
      return this.lastReviewDate != null && !this.lastReviewDate.isEmpty();
    }

    public boolean hasLastReviewDate() { 
      return this.lastReviewDate != null && !this.lastReviewDate.isEmpty();
    }

    /**
     * @param value {@link #lastReviewDate} (The date on which the resource content was last reviewed. Review happens periodically after approval but does not change the original approval date.). This is the underlying object with id, value and extensions. The accessor "getLastReviewDate" gives direct access to the value
     */
    public ChargeItemDefinition setLastReviewDateElement(DateType value) { 
      this.lastReviewDate = value;
      return this;
    }

    /**
     * @return The date on which the resource content was last reviewed. Review happens periodically after approval but does not change the original approval date.
     */
    public Date getLastReviewDate() { 
      return this.lastReviewDate == null ? null : this.lastReviewDate.getValue();
    }

    /**
     * @param value The date on which the resource content was last reviewed. Review happens periodically after approval but does not change the original approval date.
     */
    public ChargeItemDefinition setLastReviewDate(Date value) { 
      if (value == null)
        this.lastReviewDate = null;
      else {
        if (this.lastReviewDate == null)
          this.lastReviewDate = new DateType();
        this.lastReviewDate.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #effectivePeriod} (The period during which the charge item definition content was or is planned to be in active use.)
     */
    public Period getEffectivePeriod() { 
      if (this.effectivePeriod == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ChargeItemDefinition.effectivePeriod");
        else if (Configuration.doAutoCreate())
          this.effectivePeriod = new Period(); // cc
      return this.effectivePeriod;
    }

    public boolean hasEffectivePeriod() { 
      return this.effectivePeriod != null && !this.effectivePeriod.isEmpty();
    }

    /**
     * @param value {@link #effectivePeriod} (The period during which the charge item definition content was or is planned to be in active use.)
     */
    public ChargeItemDefinition setEffectivePeriod(Period value) { 
      this.effectivePeriod = value;
      return this;
    }

    /**
     * @return {@link #code} (The defined billing details in this resource pertain to the given billing code.)
     */
    public CodeableConcept getCode() { 
      if (this.code == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ChargeItemDefinition.code");
        else if (Configuration.doAutoCreate())
          this.code = new CodeableConcept(); // cc
      return this.code;
    }

    public boolean hasCode() { 
      return this.code != null && !this.code.isEmpty();
    }

    /**
     * @param value {@link #code} (The defined billing details in this resource pertain to the given billing code.)
     */
    public ChargeItemDefinition setCode(CodeableConcept value) { 
      this.code = value;
      return this;
    }

    /**
     * @return {@link #instance} (The defined billing details in this resource pertain to the given product instance(s).)
     */
    public List<Reference> getInstance() { 
      if (this.instance == null)
        this.instance = new ArrayList<Reference>();
      return this.instance;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ChargeItemDefinition setInstance(List<Reference> theInstance) { 
      this.instance = theInstance;
      return this;
    }

    public boolean hasInstance() { 
      if (this.instance == null)
        return false;
      for (Reference item : this.instance)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addInstance() { //3
      Reference t = new Reference();
      if (this.instance == null)
        this.instance = new ArrayList<Reference>();
      this.instance.add(t);
      return t;
    }

    public ChargeItemDefinition addInstance(Reference t) { //3
      if (t == null)
        return this;
      if (this.instance == null)
        this.instance = new ArrayList<Reference>();
      this.instance.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #instance}, creating it if it does not already exist
     */
    public Reference getInstanceFirstRep() { 
      if (getInstance().isEmpty()) {
        addInstance();
      }
      return getInstance().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Resource> getInstanceTarget() { 
      if (this.instanceTarget == null)
        this.instanceTarget = new ArrayList<Resource>();
      return this.instanceTarget;
    }

    /**
     * @return {@link #applicability} (Expressions that describe applicability criteria for the billing code.)
     */
    public List<ChargeItemDefinitionApplicabilityComponent> getApplicability() { 
      if (this.applicability == null)
        this.applicability = new ArrayList<ChargeItemDefinitionApplicabilityComponent>();
      return this.applicability;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ChargeItemDefinition setApplicability(List<ChargeItemDefinitionApplicabilityComponent> theApplicability) { 
      this.applicability = theApplicability;
      return this;
    }

    public boolean hasApplicability() { 
      if (this.applicability == null)
        return false;
      for (ChargeItemDefinitionApplicabilityComponent item : this.applicability)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public ChargeItemDefinitionApplicabilityComponent addApplicability() { //3
      ChargeItemDefinitionApplicabilityComponent t = new ChargeItemDefinitionApplicabilityComponent();
      if (this.applicability == null)
        this.applicability = new ArrayList<ChargeItemDefinitionApplicabilityComponent>();
      this.applicability.add(t);
      return t;
    }

    public ChargeItemDefinition addApplicability(ChargeItemDefinitionApplicabilityComponent t) { //3
      if (t == null)
        return this;
      if (this.applicability == null)
        this.applicability = new ArrayList<ChargeItemDefinitionApplicabilityComponent>();
      this.applicability.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #applicability}, creating it if it does not already exist
     */
    public ChargeItemDefinitionApplicabilityComponent getApplicabilityFirstRep() { 
      if (getApplicability().isEmpty()) {
        addApplicability();
      }
      return getApplicability().get(0);
    }

    /**
     * @return {@link #propertyGroup} (Group of properties which are applicable under the same conditions. If no applicability rules are established for the group, then all properties always apply.)
     */
    public List<ChargeItemDefinitionPropertyGroupComponent> getPropertyGroup() { 
      if (this.propertyGroup == null)
        this.propertyGroup = new ArrayList<ChargeItemDefinitionPropertyGroupComponent>();
      return this.propertyGroup;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ChargeItemDefinition setPropertyGroup(List<ChargeItemDefinitionPropertyGroupComponent> thePropertyGroup) { 
      this.propertyGroup = thePropertyGroup;
      return this;
    }

    public boolean hasPropertyGroup() { 
      if (this.propertyGroup == null)
        return false;
      for (ChargeItemDefinitionPropertyGroupComponent item : this.propertyGroup)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public ChargeItemDefinitionPropertyGroupComponent addPropertyGroup() { //3
      ChargeItemDefinitionPropertyGroupComponent t = new ChargeItemDefinitionPropertyGroupComponent();
      if (this.propertyGroup == null)
        this.propertyGroup = new ArrayList<ChargeItemDefinitionPropertyGroupComponent>();
      this.propertyGroup.add(t);
      return t;
    }

    public ChargeItemDefinition addPropertyGroup(ChargeItemDefinitionPropertyGroupComponent t) { //3
      if (t == null)
        return this;
      if (this.propertyGroup == null)
        this.propertyGroup = new ArrayList<ChargeItemDefinitionPropertyGroupComponent>();
      this.propertyGroup.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #propertyGroup}, creating it if it does not already exist
     */
    public ChargeItemDefinitionPropertyGroupComponent getPropertyGroupFirstRep() { 
      if (getPropertyGroup().isEmpty()) {
        addPropertyGroup();
      }
      return getPropertyGroup().get(0);
    }

      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("url", "uri", "An absolute URI that is used to identify this charge item definition when it is referenced in a specification, model, design or an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal address at which at which an authoritative instance of this charge item definition is (or will be) published. This URL can be the target of a canonical reference. It SHALL remain the same when the charge item definition is stored on different servers.", 0, 1, url));
        children.add(new Property("identifier", "Identifier", "A formal identifier that is used to identify this charge item definition when it is represented in other formats, or referenced in a specification, model, design or an instance.", 0, java.lang.Integer.MAX_VALUE, identifier));
        children.add(new Property("version", "string", "The identifier that is used to identify this version of the charge item definition when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the charge item definition author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge assets, refer to the Decision Support Service specification. Note that a version is required for non-experimental active assets.", 0, 1, version));
        children.add(new Property("title", "string", "A short, descriptive, user-friendly title for the charge item definition.", 0, 1, title));
        children.add(new Property("derivedFromUri", "uri", "The URL pointing to an externally-defined charge item definition that is adhered to in whole or in part by this definition.", 0, java.lang.Integer.MAX_VALUE, derivedFromUri));
        children.add(new Property("partOf", "canonical(ChargeItemDefinition)", "A larger definition of which this particular definition is a component or step.", 0, java.lang.Integer.MAX_VALUE, partOf));
        children.add(new Property("replaces", "canonical(ChargeItemDefinition)", "As new versions of a protocol or guideline are defined, allows identification of what versions are replaced by a new instance.", 0, java.lang.Integer.MAX_VALUE, replaces));
        children.add(new Property("status", "code", "The current state of the ChargeItemDefinition.", 0, 1, status));
        children.add(new Property("experimental", "boolean", "A Boolean value to indicate that this charge item definition is authored for testing purposes (or education/evaluation/marketing) and is not intended to be used for genuine usage.", 0, 1, experimental));
        children.add(new Property("date", "dateTime", "The date  (and optionally time) when the charge item definition was published. The date must change when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the charge item definition changes.", 0, 1, date));
        children.add(new Property("publisher", "string", "The name of the organization or individual that published the charge item definition.", 0, 1, publisher));
        children.add(new Property("contact", "ContactDetail", "Contact details to assist a user in finding and communicating with the publisher.", 0, java.lang.Integer.MAX_VALUE, contact));
        children.add(new Property("description", "markdown", "A free text natural language description of the charge item definition from a consumer's perspective.", 0, 1, description));
        children.add(new Property("useContext", "UsageContext", "The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching for appropriate charge item definition instances.", 0, java.lang.Integer.MAX_VALUE, useContext));
        children.add(new Property("jurisdiction", "CodeableConcept", "A legal or geographic region in which the charge item definition is intended to be used.", 0, java.lang.Integer.MAX_VALUE, jurisdiction));
        children.add(new Property("copyright", "markdown", "A copyright statement relating to the charge item definition and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the charge item definition.", 0, 1, copyright));
        children.add(new Property("approvalDate", "date", "The date on which the resource content was approved by the publisher. Approval happens once when the content is officially approved for usage.", 0, 1, approvalDate));
        children.add(new Property("lastReviewDate", "date", "The date on which the resource content was last reviewed. Review happens periodically after approval but does not change the original approval date.", 0, 1, lastReviewDate));
        children.add(new Property("effectivePeriod", "Period", "The period during which the charge item definition content was or is planned to be in active use.", 0, 1, effectivePeriod));
        children.add(new Property("code", "CodeableConcept", "The defined billing details in this resource pertain to the given billing code.", 0, 1, code));
        children.add(new Property("instance", "Reference(Medication|Substance|Device)", "The defined billing details in this resource pertain to the given product instance(s).", 0, java.lang.Integer.MAX_VALUE, instance));
        children.add(new Property("applicability", "", "Expressions that describe applicability criteria for the billing code.", 0, java.lang.Integer.MAX_VALUE, applicability));
        children.add(new Property("propertyGroup", "", "Group of properties which are applicable under the same conditions. If no applicability rules are established for the group, then all properties always apply.", 0, java.lang.Integer.MAX_VALUE, propertyGroup));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case 116079: /*url*/  return new Property("url", "uri", "An absolute URI that is used to identify this charge item definition when it is referenced in a specification, model, design or an instance; also called its canonical identifier. This SHOULD be globally unique and SHOULD be a literal address at which at which an authoritative instance of this charge item definition is (or will be) published. This URL can be the target of a canonical reference. It SHALL remain the same when the charge item definition is stored on different servers.", 0, 1, url);
        case -1618432855: /*identifier*/  return new Property("identifier", "Identifier", "A formal identifier that is used to identify this charge item definition when it is represented in other formats, or referenced in a specification, model, design or an instance.", 0, java.lang.Integer.MAX_VALUE, identifier);
        case 351608024: /*version*/  return new Property("version", "string", "The identifier that is used to identify this version of the charge item definition when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the charge item definition author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge assets, refer to the Decision Support Service specification. Note that a version is required for non-experimental active assets.", 0, 1, version);
        case 110371416: /*title*/  return new Property("title", "string", "A short, descriptive, user-friendly title for the charge item definition.", 0, 1, title);
        case -1076333435: /*derivedFromUri*/  return new Property("derivedFromUri", "uri", "The URL pointing to an externally-defined charge item definition that is adhered to in whole or in part by this definition.", 0, java.lang.Integer.MAX_VALUE, derivedFromUri);
        case -995410646: /*partOf*/  return new Property("partOf", "canonical(ChargeItemDefinition)", "A larger definition of which this particular definition is a component or step.", 0, java.lang.Integer.MAX_VALUE, partOf);
        case -430332865: /*replaces*/  return new Property("replaces", "canonical(ChargeItemDefinition)", "As new versions of a protocol or guideline are defined, allows identification of what versions are replaced by a new instance.", 0, java.lang.Integer.MAX_VALUE, replaces);
        case -892481550: /*status*/  return new Property("status", "code", "The current state of the ChargeItemDefinition.", 0, 1, status);
        case -404562712: /*experimental*/  return new Property("experimental", "boolean", "A Boolean value to indicate that this charge item definition is authored for testing purposes (or education/evaluation/marketing) and is not intended to be used for genuine usage.", 0, 1, experimental);
        case 3076014: /*date*/  return new Property("date", "dateTime", "The date  (and optionally time) when the charge item definition was published. The date must change when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the charge item definition changes.", 0, 1, date);
        case 1447404028: /*publisher*/  return new Property("publisher", "string", "The name of the organization or individual that published the charge item definition.", 0, 1, publisher);
        case 951526432: /*contact*/  return new Property("contact", "ContactDetail", "Contact details to assist a user in finding and communicating with the publisher.", 0, java.lang.Integer.MAX_VALUE, contact);
        case -1724546052: /*description*/  return new Property("description", "markdown", "A free text natural language description of the charge item definition from a consumer's perspective.", 0, 1, description);
        case -669707736: /*useContext*/  return new Property("useContext", "UsageContext", "The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching for appropriate charge item definition instances.", 0, java.lang.Integer.MAX_VALUE, useContext);
        case -507075711: /*jurisdiction*/  return new Property("jurisdiction", "CodeableConcept", "A legal or geographic region in which the charge item definition is intended to be used.", 0, java.lang.Integer.MAX_VALUE, jurisdiction);
        case 1522889671: /*copyright*/  return new Property("copyright", "markdown", "A copyright statement relating to the charge item definition and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the charge item definition.", 0, 1, copyright);
        case 223539345: /*approvalDate*/  return new Property("approvalDate", "date", "The date on which the resource content was approved by the publisher. Approval happens once when the content is officially approved for usage.", 0, 1, approvalDate);
        case -1687512484: /*lastReviewDate*/  return new Property("lastReviewDate", "date", "The date on which the resource content was last reviewed. Review happens periodically after approval but does not change the original approval date.", 0, 1, lastReviewDate);
        case -403934648: /*effectivePeriod*/  return new Property("effectivePeriod", "Period", "The period during which the charge item definition content was or is planned to be in active use.", 0, 1, effectivePeriod);
        case 3059181: /*code*/  return new Property("code", "CodeableConcept", "The defined billing details in this resource pertain to the given billing code.", 0, 1, code);
        case 555127957: /*instance*/  return new Property("instance", "Reference(Medication|Substance|Device)", "The defined billing details in this resource pertain to the given product instance(s).", 0, java.lang.Integer.MAX_VALUE, instance);
        case -1526770491: /*applicability*/  return new Property("applicability", "", "Expressions that describe applicability criteria for the billing code.", 0, java.lang.Integer.MAX_VALUE, applicability);
        case -1041594966: /*propertyGroup*/  return new Property("propertyGroup", "", "Group of properties which are applicable under the same conditions. If no applicability rules are established for the group, then all properties always apply.", 0, java.lang.Integer.MAX_VALUE, propertyGroup);
        default: return super.getNamedProperty(_hash, _name, _checkValid);
        }

      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 116079: /*url*/ return this.url == null ? new Base[0] : new Base[] {this.url}; // UriType
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case 351608024: /*version*/ return this.version == null ? new Base[0] : new Base[] {this.version}; // StringType
        case 110371416: /*title*/ return this.title == null ? new Base[0] : new Base[] {this.title}; // StringType
        case -1076333435: /*derivedFromUri*/ return this.derivedFromUri == null ? new Base[0] : this.derivedFromUri.toArray(new Base[this.derivedFromUri.size()]); // UriType
        case -995410646: /*partOf*/ return this.partOf == null ? new Base[0] : this.partOf.toArray(new Base[this.partOf.size()]); // CanonicalType
        case -430332865: /*replaces*/ return this.replaces == null ? new Base[0] : this.replaces.toArray(new Base[this.replaces.size()]); // CanonicalType
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<PublicationStatus>
        case -404562712: /*experimental*/ return this.experimental == null ? new Base[0] : new Base[] {this.experimental}; // BooleanType
        case 3076014: /*date*/ return this.date == null ? new Base[0] : new Base[] {this.date}; // DateTimeType
        case 1447404028: /*publisher*/ return this.publisher == null ? new Base[0] : new Base[] {this.publisher}; // StringType
        case 951526432: /*contact*/ return this.contact == null ? new Base[0] : this.contact.toArray(new Base[this.contact.size()]); // ContactDetail
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // MarkdownType
        case -669707736: /*useContext*/ return this.useContext == null ? new Base[0] : this.useContext.toArray(new Base[this.useContext.size()]); // UsageContext
        case -507075711: /*jurisdiction*/ return this.jurisdiction == null ? new Base[0] : this.jurisdiction.toArray(new Base[this.jurisdiction.size()]); // CodeableConcept
        case 1522889671: /*copyright*/ return this.copyright == null ? new Base[0] : new Base[] {this.copyright}; // MarkdownType
        case 223539345: /*approvalDate*/ return this.approvalDate == null ? new Base[0] : new Base[] {this.approvalDate}; // DateType
        case -1687512484: /*lastReviewDate*/ return this.lastReviewDate == null ? new Base[0] : new Base[] {this.lastReviewDate}; // DateType
        case -403934648: /*effectivePeriod*/ return this.effectivePeriod == null ? new Base[0] : new Base[] {this.effectivePeriod}; // Period
        case 3059181: /*code*/ return this.code == null ? new Base[0] : new Base[] {this.code}; // CodeableConcept
        case 555127957: /*instance*/ return this.instance == null ? new Base[0] : this.instance.toArray(new Base[this.instance.size()]); // Reference
        case -1526770491: /*applicability*/ return this.applicability == null ? new Base[0] : this.applicability.toArray(new Base[this.applicability.size()]); // ChargeItemDefinitionApplicabilityComponent
        case -1041594966: /*propertyGroup*/ return this.propertyGroup == null ? new Base[0] : this.propertyGroup.toArray(new Base[this.propertyGroup.size()]); // ChargeItemDefinitionPropertyGroupComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 116079: // url
          this.url = castToUri(value); // UriType
          return value;
        case -1618432855: // identifier
          this.getIdentifier().add(castToIdentifier(value)); // Identifier
          return value;
        case 351608024: // version
          this.version = castToString(value); // StringType
          return value;
        case 110371416: // title
          this.title = castToString(value); // StringType
          return value;
        case -1076333435: // derivedFromUri
          this.getDerivedFromUri().add(castToUri(value)); // UriType
          return value;
        case -995410646: // partOf
          this.getPartOf().add(castToCanonical(value)); // CanonicalType
          return value;
        case -430332865: // replaces
          this.getReplaces().add(castToCanonical(value)); // CanonicalType
          return value;
        case -892481550: // status
          value = new PublicationStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<PublicationStatus>
          return value;
        case -404562712: // experimental
          this.experimental = castToBoolean(value); // BooleanType
          return value;
        case 3076014: // date
          this.date = castToDateTime(value); // DateTimeType
          return value;
        case 1447404028: // publisher
          this.publisher = castToString(value); // StringType
          return value;
        case 951526432: // contact
          this.getContact().add(castToContactDetail(value)); // ContactDetail
          return value;
        case -1724546052: // description
          this.description = castToMarkdown(value); // MarkdownType
          return value;
        case -669707736: // useContext
          this.getUseContext().add(castToUsageContext(value)); // UsageContext
          return value;
        case -507075711: // jurisdiction
          this.getJurisdiction().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 1522889671: // copyright
          this.copyright = castToMarkdown(value); // MarkdownType
          return value;
        case 223539345: // approvalDate
          this.approvalDate = castToDate(value); // DateType
          return value;
        case -1687512484: // lastReviewDate
          this.lastReviewDate = castToDate(value); // DateType
          return value;
        case -403934648: // effectivePeriod
          this.effectivePeriod = castToPeriod(value); // Period
          return value;
        case 3059181: // code
          this.code = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 555127957: // instance
          this.getInstance().add(castToReference(value)); // Reference
          return value;
        case -1526770491: // applicability
          this.getApplicability().add((ChargeItemDefinitionApplicabilityComponent) value); // ChargeItemDefinitionApplicabilityComponent
          return value;
        case -1041594966: // propertyGroup
          this.getPropertyGroup().add((ChargeItemDefinitionPropertyGroupComponent) value); // ChargeItemDefinitionPropertyGroupComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("url")) {
          this.url = castToUri(value); // UriType
        } else if (name.equals("identifier")) {
          this.getIdentifier().add(castToIdentifier(value));
        } else if (name.equals("version")) {
          this.version = castToString(value); // StringType
        } else if (name.equals("title")) {
          this.title = castToString(value); // StringType
        } else if (name.equals("derivedFromUri")) {
          this.getDerivedFromUri().add(castToUri(value));
        } else if (name.equals("partOf")) {
          this.getPartOf().add(castToCanonical(value));
        } else if (name.equals("replaces")) {
          this.getReplaces().add(castToCanonical(value));
        } else if (name.equals("status")) {
          value = new PublicationStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<PublicationStatus>
        } else if (name.equals("experimental")) {
          this.experimental = castToBoolean(value); // BooleanType
        } else if (name.equals("date")) {
          this.date = castToDateTime(value); // DateTimeType
        } else if (name.equals("publisher")) {
          this.publisher = castToString(value); // StringType
        } else if (name.equals("contact")) {
          this.getContact().add(castToContactDetail(value));
        } else if (name.equals("description")) {
          this.description = castToMarkdown(value); // MarkdownType
        } else if (name.equals("useContext")) {
          this.getUseContext().add(castToUsageContext(value));
        } else if (name.equals("jurisdiction")) {
          this.getJurisdiction().add(castToCodeableConcept(value));
        } else if (name.equals("copyright")) {
          this.copyright = castToMarkdown(value); // MarkdownType
        } else if (name.equals("approvalDate")) {
          this.approvalDate = castToDate(value); // DateType
        } else if (name.equals("lastReviewDate")) {
          this.lastReviewDate = castToDate(value); // DateType
        } else if (name.equals("effectivePeriod")) {
          this.effectivePeriod = castToPeriod(value); // Period
        } else if (name.equals("code")) {
          this.code = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("instance")) {
          this.getInstance().add(castToReference(value));
        } else if (name.equals("applicability")) {
          this.getApplicability().add((ChargeItemDefinitionApplicabilityComponent) value);
        } else if (name.equals("propertyGroup")) {
          this.getPropertyGroup().add((ChargeItemDefinitionPropertyGroupComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 116079:  return getUrlElement();
        case -1618432855:  return addIdentifier(); 
        case 351608024:  return getVersionElement();
        case 110371416:  return getTitleElement();
        case -1076333435:  return addDerivedFromUriElement();
        case -995410646:  return addPartOfElement();
        case -430332865:  return addReplacesElement();
        case -892481550:  return getStatusElement();
        case -404562712:  return getExperimentalElement();
        case 3076014:  return getDateElement();
        case 1447404028:  return getPublisherElement();
        case 951526432:  return addContact(); 
        case -1724546052:  return getDescriptionElement();
        case -669707736:  return addUseContext(); 
        case -507075711:  return addJurisdiction(); 
        case 1522889671:  return getCopyrightElement();
        case 223539345:  return getApprovalDateElement();
        case -1687512484:  return getLastReviewDateElement();
        case -403934648:  return getEffectivePeriod(); 
        case 3059181:  return getCode(); 
        case 555127957:  return addInstance(); 
        case -1526770491:  return addApplicability(); 
        case -1041594966:  return addPropertyGroup(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 116079: /*url*/ return new String[] {"uri"};
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case 351608024: /*version*/ return new String[] {"string"};
        case 110371416: /*title*/ return new String[] {"string"};
        case -1076333435: /*derivedFromUri*/ return new String[] {"uri"};
        case -995410646: /*partOf*/ return new String[] {"canonical"};
        case -430332865: /*replaces*/ return new String[] {"canonical"};
        case -892481550: /*status*/ return new String[] {"code"};
        case -404562712: /*experimental*/ return new String[] {"boolean"};
        case 3076014: /*date*/ return new String[] {"dateTime"};
        case 1447404028: /*publisher*/ return new String[] {"string"};
        case 951526432: /*contact*/ return new String[] {"ContactDetail"};
        case -1724546052: /*description*/ return new String[] {"markdown"};
        case -669707736: /*useContext*/ return new String[] {"UsageContext"};
        case -507075711: /*jurisdiction*/ return new String[] {"CodeableConcept"};
        case 1522889671: /*copyright*/ return new String[] {"markdown"};
        case 223539345: /*approvalDate*/ return new String[] {"date"};
        case -1687512484: /*lastReviewDate*/ return new String[] {"date"};
        case -403934648: /*effectivePeriod*/ return new String[] {"Period"};
        case 3059181: /*code*/ return new String[] {"CodeableConcept"};
        case 555127957: /*instance*/ return new String[] {"Reference"};
        case -1526770491: /*applicability*/ return new String[] {};
        case -1041594966: /*propertyGroup*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("url")) {
          throw new FHIRException("Cannot call addChild on a primitive type ChargeItemDefinition.url");
        }
        else if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("version")) {
          throw new FHIRException("Cannot call addChild on a primitive type ChargeItemDefinition.version");
        }
        else if (name.equals("title")) {
          throw new FHIRException("Cannot call addChild on a primitive type ChargeItemDefinition.title");
        }
        else if (name.equals("derivedFromUri")) {
          throw new FHIRException("Cannot call addChild on a primitive type ChargeItemDefinition.derivedFromUri");
        }
        else if (name.equals("partOf")) {
          throw new FHIRException("Cannot call addChild on a primitive type ChargeItemDefinition.partOf");
        }
        else if (name.equals("replaces")) {
          throw new FHIRException("Cannot call addChild on a primitive type ChargeItemDefinition.replaces");
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type ChargeItemDefinition.status");
        }
        else if (name.equals("experimental")) {
          throw new FHIRException("Cannot call addChild on a primitive type ChargeItemDefinition.experimental");
        }
        else if (name.equals("date")) {
          throw new FHIRException("Cannot call addChild on a primitive type ChargeItemDefinition.date");
        }
        else if (name.equals("publisher")) {
          throw new FHIRException("Cannot call addChild on a primitive type ChargeItemDefinition.publisher");
        }
        else if (name.equals("contact")) {
          return addContact();
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type ChargeItemDefinition.description");
        }
        else if (name.equals("useContext")) {
          return addUseContext();
        }
        else if (name.equals("jurisdiction")) {
          return addJurisdiction();
        }
        else if (name.equals("copyright")) {
          throw new FHIRException("Cannot call addChild on a primitive type ChargeItemDefinition.copyright");
        }
        else if (name.equals("approvalDate")) {
          throw new FHIRException("Cannot call addChild on a primitive type ChargeItemDefinition.approvalDate");
        }
        else if (name.equals("lastReviewDate")) {
          throw new FHIRException("Cannot call addChild on a primitive type ChargeItemDefinition.lastReviewDate");
        }
        else if (name.equals("effectivePeriod")) {
          this.effectivePeriod = new Period();
          return this.effectivePeriod;
        }
        else if (name.equals("code")) {
          this.code = new CodeableConcept();
          return this.code;
        }
        else if (name.equals("instance")) {
          return addInstance();
        }
        else if (name.equals("applicability")) {
          return addApplicability();
        }
        else if (name.equals("propertyGroup")) {
          return addPropertyGroup();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "ChargeItemDefinition";

  }

      public ChargeItemDefinition copy() {
        ChargeItemDefinition dst = new ChargeItemDefinition();
        copyValues(dst);
        dst.url = url == null ? null : url.copy();
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.version = version == null ? null : version.copy();
        dst.title = title == null ? null : title.copy();
        if (derivedFromUri != null) {
          dst.derivedFromUri = new ArrayList<UriType>();
          for (UriType i : derivedFromUri)
            dst.derivedFromUri.add(i.copy());
        };
        if (partOf != null) {
          dst.partOf = new ArrayList<CanonicalType>();
          for (CanonicalType i : partOf)
            dst.partOf.add(i.copy());
        };
        if (replaces != null) {
          dst.replaces = new ArrayList<CanonicalType>();
          for (CanonicalType i : replaces)
            dst.replaces.add(i.copy());
        };
        dst.status = status == null ? null : status.copy();
        dst.experimental = experimental == null ? null : experimental.copy();
        dst.date = date == null ? null : date.copy();
        dst.publisher = publisher == null ? null : publisher.copy();
        if (contact != null) {
          dst.contact = new ArrayList<ContactDetail>();
          for (ContactDetail i : contact)
            dst.contact.add(i.copy());
        };
        dst.description = description == null ? null : description.copy();
        if (useContext != null) {
          dst.useContext = new ArrayList<UsageContext>();
          for (UsageContext i : useContext)
            dst.useContext.add(i.copy());
        };
        if (jurisdiction != null) {
          dst.jurisdiction = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : jurisdiction)
            dst.jurisdiction.add(i.copy());
        };
        dst.copyright = copyright == null ? null : copyright.copy();
        dst.approvalDate = approvalDate == null ? null : approvalDate.copy();
        dst.lastReviewDate = lastReviewDate == null ? null : lastReviewDate.copy();
        dst.effectivePeriod = effectivePeriod == null ? null : effectivePeriod.copy();
        dst.code = code == null ? null : code.copy();
        if (instance != null) {
          dst.instance = new ArrayList<Reference>();
          for (Reference i : instance)
            dst.instance.add(i.copy());
        };
        if (applicability != null) {
          dst.applicability = new ArrayList<ChargeItemDefinitionApplicabilityComponent>();
          for (ChargeItemDefinitionApplicabilityComponent i : applicability)
            dst.applicability.add(i.copy());
        };
        if (propertyGroup != null) {
          dst.propertyGroup = new ArrayList<ChargeItemDefinitionPropertyGroupComponent>();
          for (ChargeItemDefinitionPropertyGroupComponent i : propertyGroup)
            dst.propertyGroup.add(i.copy());
        };
        return dst;
      }

      protected ChargeItemDefinition typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ChargeItemDefinition))
          return false;
        ChargeItemDefinition o = (ChargeItemDefinition) other_;
        return compareDeep(identifier, o.identifier, true) && compareDeep(derivedFromUri, o.derivedFromUri, true)
           && compareDeep(partOf, o.partOf, true) && compareDeep(replaces, o.replaces, true) && compareDeep(copyright, o.copyright, true)
           && compareDeep(approvalDate, o.approvalDate, true) && compareDeep(lastReviewDate, o.lastReviewDate, true)
           && compareDeep(effectivePeriod, o.effectivePeriod, true) && compareDeep(code, o.code, true) && compareDeep(instance, o.instance, true)
           && compareDeep(applicability, o.applicability, true) && compareDeep(propertyGroup, o.propertyGroup, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ChargeItemDefinition))
          return false;
        ChargeItemDefinition o = (ChargeItemDefinition) other_;
        return compareValues(derivedFromUri, o.derivedFromUri, true) && compareValues(copyright, o.copyright, true)
           && compareValues(approvalDate, o.approvalDate, true) && compareValues(lastReviewDate, o.lastReviewDate, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, derivedFromUri
          , partOf, replaces, copyright, approvalDate, lastReviewDate, effectivePeriod, code
          , instance, applicability, propertyGroup);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.ChargeItemDefinition;
   }

 /**
   * Search parameter: <b>date</b>
   * <p>
   * Description: <b>The charge item definition publication date</b><br>
   * Type: <b>date</b><br>
   * Path: <b>ChargeItemDefinition.date</b><br>
   * </p>
   */
  @SearchParamDefinition(name="date", path="ChargeItemDefinition.date", description="The charge item definition publication date", type="date" )
  public static final String SP_DATE = "date";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>date</b>
   * <p>
   * Description: <b>The charge item definition publication date</b><br>
   * Type: <b>date</b><br>
   * Path: <b>ChargeItemDefinition.date</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam DATE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_DATE);

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>External identifier for the charge item definition</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ChargeItemDefinition.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="ChargeItemDefinition.identifier", description="External identifier for the charge item definition", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>External identifier for the charge item definition</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ChargeItemDefinition.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>context-type-value</b>
   * <p>
   * Description: <b>A use context type and value assigned to the charge item definition</b><br>
   * Type: <b>composite</b><br>
   * Path: <b></b><br>
   * </p>
   */
  @SearchParamDefinition(name="context-type-value", path="ChargeItemDefinition.useContext", description="A use context type and value assigned to the charge item definition", type="composite", compositeOf={"context-type", "context"} )
  public static final String SP_CONTEXT_TYPE_VALUE = "context-type-value";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>context-type-value</b>
   * <p>
   * Description: <b>A use context type and value assigned to the charge item definition</b><br>
   * Type: <b>composite</b><br>
   * Path: <b></b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.CompositeClientParam<ca.uhn.fhir.rest.gclient.TokenClientParam, ca.uhn.fhir.rest.gclient.TokenClientParam> CONTEXT_TYPE_VALUE = new ca.uhn.fhir.rest.gclient.CompositeClientParam<ca.uhn.fhir.rest.gclient.TokenClientParam, ca.uhn.fhir.rest.gclient.TokenClientParam>(SP_CONTEXT_TYPE_VALUE);

 /**
   * Search parameter: <b>jurisdiction</b>
   * <p>
   * Description: <b>Intended jurisdiction for the charge item definition</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ChargeItemDefinition.jurisdiction</b><br>
   * </p>
   */
  @SearchParamDefinition(name="jurisdiction", path="ChargeItemDefinition.jurisdiction", description="Intended jurisdiction for the charge item definition", type="token" )
  public static final String SP_JURISDICTION = "jurisdiction";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>jurisdiction</b>
   * <p>
   * Description: <b>Intended jurisdiction for the charge item definition</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ChargeItemDefinition.jurisdiction</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam JURISDICTION = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_JURISDICTION);

 /**
   * Search parameter: <b>description</b>
   * <p>
   * Description: <b>The description of the charge item definition</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ChargeItemDefinition.description</b><br>
   * </p>
   */
  @SearchParamDefinition(name="description", path="ChargeItemDefinition.description", description="The description of the charge item definition", type="string" )
  public static final String SP_DESCRIPTION = "description";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>description</b>
   * <p>
   * Description: <b>The description of the charge item definition</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ChargeItemDefinition.description</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam DESCRIPTION = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_DESCRIPTION);

 /**
   * Search parameter: <b>context-type</b>
   * <p>
   * Description: <b>A type of use context assigned to the charge item definition</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ChargeItemDefinition.useContext.code</b><br>
   * </p>
   */
  @SearchParamDefinition(name="context-type", path="ChargeItemDefinition.useContext.code", description="A type of use context assigned to the charge item definition", type="token" )
  public static final String SP_CONTEXT_TYPE = "context-type";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>context-type</b>
   * <p>
   * Description: <b>A type of use context assigned to the charge item definition</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ChargeItemDefinition.useContext.code</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CONTEXT_TYPE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CONTEXT_TYPE);

 /**
   * Search parameter: <b>title</b>
   * <p>
   * Description: <b>The human-friendly name of the charge item definition</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ChargeItemDefinition.title</b><br>
   * </p>
   */
  @SearchParamDefinition(name="title", path="ChargeItemDefinition.title", description="The human-friendly name of the charge item definition", type="string" )
  public static final String SP_TITLE = "title";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>title</b>
   * <p>
   * Description: <b>The human-friendly name of the charge item definition</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ChargeItemDefinition.title</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam TITLE = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_TITLE);

 /**
   * Search parameter: <b>version</b>
   * <p>
   * Description: <b>The business version of the charge item definition</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ChargeItemDefinition.version</b><br>
   * </p>
   */
  @SearchParamDefinition(name="version", path="ChargeItemDefinition.version", description="The business version of the charge item definition", type="token" )
  public static final String SP_VERSION = "version";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>version</b>
   * <p>
   * Description: <b>The business version of the charge item definition</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ChargeItemDefinition.version</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam VERSION = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_VERSION);

 /**
   * Search parameter: <b>url</b>
   * <p>
   * Description: <b>The uri that identifies the charge item definition</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>ChargeItemDefinition.url</b><br>
   * </p>
   */
  @SearchParamDefinition(name="url", path="ChargeItemDefinition.url", description="The uri that identifies the charge item definition", type="uri" )
  public static final String SP_URL = "url";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>url</b>
   * <p>
   * Description: <b>The uri that identifies the charge item definition</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>ChargeItemDefinition.url</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.UriClientParam URL = new ca.uhn.fhir.rest.gclient.UriClientParam(SP_URL);

 /**
   * Search parameter: <b>context-quantity</b>
   * <p>
   * Description: <b>A quantity- or range-valued use context assigned to the charge item definition</b><br>
   * Type: <b>quantity</b><br>
   * Path: <b>ChargeItemDefinition.useContext.valueQuantity, ChargeItemDefinition.useContext.valueRange</b><br>
   * </p>
   */
  @SearchParamDefinition(name="context-quantity", path="(ChargeItemDefinition.useContext.value as Quantity) | (ChargeItemDefinition.useContext.value as Range)", description="A quantity- or range-valued use context assigned to the charge item definition", type="quantity" )
  public static final String SP_CONTEXT_QUANTITY = "context-quantity";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>context-quantity</b>
   * <p>
   * Description: <b>A quantity- or range-valued use context assigned to the charge item definition</b><br>
   * Type: <b>quantity</b><br>
   * Path: <b>ChargeItemDefinition.useContext.valueQuantity, ChargeItemDefinition.useContext.valueRange</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.QuantityClientParam CONTEXT_QUANTITY = new ca.uhn.fhir.rest.gclient.QuantityClientParam(SP_CONTEXT_QUANTITY);

 /**
   * Search parameter: <b>effective</b>
   * <p>
   * Description: <b>The time during which the charge item definition is intended to be in use</b><br>
   * Type: <b>date</b><br>
   * Path: <b>ChargeItemDefinition.effectivePeriod</b><br>
   * </p>
   */
  @SearchParamDefinition(name="effective", path="ChargeItemDefinition.effectivePeriod", description="The time during which the charge item definition is intended to be in use", type="date" )
  public static final String SP_EFFECTIVE = "effective";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>effective</b>
   * <p>
   * Description: <b>The time during which the charge item definition is intended to be in use</b><br>
   * Type: <b>date</b><br>
   * Path: <b>ChargeItemDefinition.effectivePeriod</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam EFFECTIVE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_EFFECTIVE);

 /**
   * Search parameter: <b>context</b>
   * <p>
   * Description: <b>A use context assigned to the charge item definition</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ChargeItemDefinition.useContext.valueCodeableConcept</b><br>
   * </p>
   */
  @SearchParamDefinition(name="context", path="(ChargeItemDefinition.useContext.value as CodeableConcept)", description="A use context assigned to the charge item definition", type="token" )
  public static final String SP_CONTEXT = "context";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>context</b>
   * <p>
   * Description: <b>A use context assigned to the charge item definition</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ChargeItemDefinition.useContext.valueCodeableConcept</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CONTEXT = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CONTEXT);

 /**
   * Search parameter: <b>publisher</b>
   * <p>
   * Description: <b>Name of the publisher of the charge item definition</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ChargeItemDefinition.publisher</b><br>
   * </p>
   */
  @SearchParamDefinition(name="publisher", path="ChargeItemDefinition.publisher", description="Name of the publisher of the charge item definition", type="string" )
  public static final String SP_PUBLISHER = "publisher";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>publisher</b>
   * <p>
   * Description: <b>Name of the publisher of the charge item definition</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ChargeItemDefinition.publisher</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam PUBLISHER = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_PUBLISHER);

 /**
   * Search parameter: <b>context-type-quantity</b>
   * <p>
   * Description: <b>A use context type and quantity- or range-based value assigned to the charge item definition</b><br>
   * Type: <b>composite</b><br>
   * Path: <b></b><br>
   * </p>
   */
  @SearchParamDefinition(name="context-type-quantity", path="ChargeItemDefinition.useContext", description="A use context type and quantity- or range-based value assigned to the charge item definition", type="composite", compositeOf={"context-type", "context-quantity"} )
  public static final String SP_CONTEXT_TYPE_QUANTITY = "context-type-quantity";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>context-type-quantity</b>
   * <p>
   * Description: <b>A use context type and quantity- or range-based value assigned to the charge item definition</b><br>
   * Type: <b>composite</b><br>
   * Path: <b></b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.CompositeClientParam<ca.uhn.fhir.rest.gclient.TokenClientParam, ca.uhn.fhir.rest.gclient.QuantityClientParam> CONTEXT_TYPE_QUANTITY = new ca.uhn.fhir.rest.gclient.CompositeClientParam<ca.uhn.fhir.rest.gclient.TokenClientParam, ca.uhn.fhir.rest.gclient.QuantityClientParam>(SP_CONTEXT_TYPE_QUANTITY);

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>The current status of the charge item definition</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ChargeItemDefinition.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="ChargeItemDefinition.status", description="The current status of the charge item definition", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>The current status of the charge item definition</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ChargeItemDefinition.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);


}

