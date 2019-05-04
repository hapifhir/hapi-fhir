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
 * The detailed description of a substance, typically at a level beyond what is used for prescribing.
 */
@ResourceDef(name="SubstanceSpecification", profile="http://hl7.org/fhir/StructureDefinition/SubstanceSpecification")
public class SubstanceSpecification extends DomainResource {

    @Block()
    public static class SubstanceSpecificationMoietyComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Role that the moiety is playing.
         */
        @Child(name = "role", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Role that the moiety is playing", formalDefinition="Role that the moiety is playing." )
        protected CodeableConcept role;

        /**
         * Identifier by which this moiety substance is known.
         */
        @Child(name = "identifier", type = {Identifier.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Identifier by which this moiety substance is known", formalDefinition="Identifier by which this moiety substance is known." )
        protected Identifier identifier;

        /**
         * Textual name for this moiety substance.
         */
        @Child(name = "name", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Textual name for this moiety substance", formalDefinition="Textual name for this moiety substance." )
        protected StringType name;

        /**
         * Stereochemistry type.
         */
        @Child(name = "stereochemistry", type = {CodeableConcept.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Stereochemistry type", formalDefinition="Stereochemistry type." )
        protected CodeableConcept stereochemistry;

        /**
         * Optical activity type.
         */
        @Child(name = "opticalActivity", type = {CodeableConcept.class}, order=5, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Optical activity type", formalDefinition="Optical activity type." )
        protected CodeableConcept opticalActivity;

        /**
         * Molecular formula.
         */
        @Child(name = "molecularFormula", type = {StringType.class}, order=6, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Molecular formula", formalDefinition="Molecular formula." )
        protected StringType molecularFormula;

        /**
         * Quantitative value for this moiety.
         */
        @Child(name = "amount", type = {Quantity.class, StringType.class}, order=7, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Quantitative value for this moiety", formalDefinition="Quantitative value for this moiety." )
        protected Type amount;

        private static final long serialVersionUID = -505630417L;

    /**
     * Constructor
     */
      public SubstanceSpecificationMoietyComponent() {
        super();
      }

        /**
         * @return {@link #role} (Role that the moiety is playing.)
         */
        public CodeableConcept getRole() { 
          if (this.role == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceSpecificationMoietyComponent.role");
            else if (Configuration.doAutoCreate())
              this.role = new CodeableConcept(); // cc
          return this.role;
        }

        public boolean hasRole() { 
          return this.role != null && !this.role.isEmpty();
        }

        /**
         * @param value {@link #role} (Role that the moiety is playing.)
         */
        public SubstanceSpecificationMoietyComponent setRole(CodeableConcept value) { 
          this.role = value;
          return this;
        }

        /**
         * @return {@link #identifier} (Identifier by which this moiety substance is known.)
         */
        public Identifier getIdentifier() { 
          if (this.identifier == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceSpecificationMoietyComponent.identifier");
            else if (Configuration.doAutoCreate())
              this.identifier = new Identifier(); // cc
          return this.identifier;
        }

        public boolean hasIdentifier() { 
          return this.identifier != null && !this.identifier.isEmpty();
        }

        /**
         * @param value {@link #identifier} (Identifier by which this moiety substance is known.)
         */
        public SubstanceSpecificationMoietyComponent setIdentifier(Identifier value) { 
          this.identifier = value;
          return this;
        }

        /**
         * @return {@link #name} (Textual name for this moiety substance.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StringType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceSpecificationMoietyComponent.name");
            else if (Configuration.doAutoCreate())
              this.name = new StringType(); // bb
          return this.name;
        }

        public boolean hasNameElement() { 
          return this.name != null && !this.name.isEmpty();
        }

        public boolean hasName() { 
          return this.name != null && !this.name.isEmpty();
        }

        /**
         * @param value {@link #name} (Textual name for this moiety substance.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public SubstanceSpecificationMoietyComponent setNameElement(StringType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return Textual name for this moiety substance.
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value Textual name for this moiety substance.
         */
        public SubstanceSpecificationMoietyComponent setName(String value) { 
          if (Utilities.noString(value))
            this.name = null;
          else {
            if (this.name == null)
              this.name = new StringType();
            this.name.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #stereochemistry} (Stereochemistry type.)
         */
        public CodeableConcept getStereochemistry() { 
          if (this.stereochemistry == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceSpecificationMoietyComponent.stereochemistry");
            else if (Configuration.doAutoCreate())
              this.stereochemistry = new CodeableConcept(); // cc
          return this.stereochemistry;
        }

        public boolean hasStereochemistry() { 
          return this.stereochemistry != null && !this.stereochemistry.isEmpty();
        }

        /**
         * @param value {@link #stereochemistry} (Stereochemistry type.)
         */
        public SubstanceSpecificationMoietyComponent setStereochemistry(CodeableConcept value) { 
          this.stereochemistry = value;
          return this;
        }

        /**
         * @return {@link #opticalActivity} (Optical activity type.)
         */
        public CodeableConcept getOpticalActivity() { 
          if (this.opticalActivity == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceSpecificationMoietyComponent.opticalActivity");
            else if (Configuration.doAutoCreate())
              this.opticalActivity = new CodeableConcept(); // cc
          return this.opticalActivity;
        }

        public boolean hasOpticalActivity() { 
          return this.opticalActivity != null && !this.opticalActivity.isEmpty();
        }

        /**
         * @param value {@link #opticalActivity} (Optical activity type.)
         */
        public SubstanceSpecificationMoietyComponent setOpticalActivity(CodeableConcept value) { 
          this.opticalActivity = value;
          return this;
        }

        /**
         * @return {@link #molecularFormula} (Molecular formula.). This is the underlying object with id, value and extensions. The accessor "getMolecularFormula" gives direct access to the value
         */
        public StringType getMolecularFormulaElement() { 
          if (this.molecularFormula == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceSpecificationMoietyComponent.molecularFormula");
            else if (Configuration.doAutoCreate())
              this.molecularFormula = new StringType(); // bb
          return this.molecularFormula;
        }

        public boolean hasMolecularFormulaElement() { 
          return this.molecularFormula != null && !this.molecularFormula.isEmpty();
        }

        public boolean hasMolecularFormula() { 
          return this.molecularFormula != null && !this.molecularFormula.isEmpty();
        }

        /**
         * @param value {@link #molecularFormula} (Molecular formula.). This is the underlying object with id, value and extensions. The accessor "getMolecularFormula" gives direct access to the value
         */
        public SubstanceSpecificationMoietyComponent setMolecularFormulaElement(StringType value) { 
          this.molecularFormula = value;
          return this;
        }

        /**
         * @return Molecular formula.
         */
        public String getMolecularFormula() { 
          return this.molecularFormula == null ? null : this.molecularFormula.getValue();
        }

        /**
         * @param value Molecular formula.
         */
        public SubstanceSpecificationMoietyComponent setMolecularFormula(String value) { 
          if (Utilities.noString(value))
            this.molecularFormula = null;
          else {
            if (this.molecularFormula == null)
              this.molecularFormula = new StringType();
            this.molecularFormula.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #amount} (Quantitative value for this moiety.)
         */
        public Type getAmount() { 
          return this.amount;
        }

        /**
         * @return {@link #amount} (Quantitative value for this moiety.)
         */
        public Quantity getAmountQuantity() throws FHIRException { 
          if (this.amount == null)
            this.amount = new Quantity();
          if (!(this.amount instanceof Quantity))
            throw new FHIRException("Type mismatch: the type Quantity was expected, but "+this.amount.getClass().getName()+" was encountered");
          return (Quantity) this.amount;
        }

        public boolean hasAmountQuantity() { 
          return this != null && this.amount instanceof Quantity;
        }

        /**
         * @return {@link #amount} (Quantitative value for this moiety.)
         */
        public StringType getAmountStringType() throws FHIRException { 
          if (this.amount == null)
            this.amount = new StringType();
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
         * @param value {@link #amount} (Quantitative value for this moiety.)
         */
        public SubstanceSpecificationMoietyComponent setAmount(Type value) { 
          if (value != null && !(value instanceof Quantity || value instanceof StringType))
            throw new Error("Not the right type for SubstanceSpecification.moiety.amount[x]: "+value.fhirType());
          this.amount = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("role", "CodeableConcept", "Role that the moiety is playing.", 0, 1, role));
          children.add(new Property("identifier", "Identifier", "Identifier by which this moiety substance is known.", 0, 1, identifier));
          children.add(new Property("name", "string", "Textual name for this moiety substance.", 0, 1, name));
          children.add(new Property("stereochemistry", "CodeableConcept", "Stereochemistry type.", 0, 1, stereochemistry));
          children.add(new Property("opticalActivity", "CodeableConcept", "Optical activity type.", 0, 1, opticalActivity));
          children.add(new Property("molecularFormula", "string", "Molecular formula.", 0, 1, molecularFormula));
          children.add(new Property("amount[x]", "Quantity|string", "Quantitative value for this moiety.", 0, 1, amount));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3506294: /*role*/  return new Property("role", "CodeableConcept", "Role that the moiety is playing.", 0, 1, role);
          case -1618432855: /*identifier*/  return new Property("identifier", "Identifier", "Identifier by which this moiety substance is known.", 0, 1, identifier);
          case 3373707: /*name*/  return new Property("name", "string", "Textual name for this moiety substance.", 0, 1, name);
          case 263475116: /*stereochemistry*/  return new Property("stereochemistry", "CodeableConcept", "Stereochemistry type.", 0, 1, stereochemistry);
          case 1420900135: /*opticalActivity*/  return new Property("opticalActivity", "CodeableConcept", "Optical activity type.", 0, 1, opticalActivity);
          case 616660246: /*molecularFormula*/  return new Property("molecularFormula", "string", "Molecular formula.", 0, 1, molecularFormula);
          case 646780200: /*amount[x]*/  return new Property("amount[x]", "Quantity|string", "Quantitative value for this moiety.", 0, 1, amount);
          case -1413853096: /*amount*/  return new Property("amount[x]", "Quantity|string", "Quantitative value for this moiety.", 0, 1, amount);
          case 1664303363: /*amountQuantity*/  return new Property("amount[x]", "Quantity|string", "Quantitative value for this moiety.", 0, 1, amount);
          case 773651081: /*amountString*/  return new Property("amount[x]", "Quantity|string", "Quantitative value for this moiety.", 0, 1, amount);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3506294: /*role*/ return this.role == null ? new Base[0] : new Base[] {this.role}; // CodeableConcept
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : new Base[] {this.identifier}; // Identifier
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case 263475116: /*stereochemistry*/ return this.stereochemistry == null ? new Base[0] : new Base[] {this.stereochemistry}; // CodeableConcept
        case 1420900135: /*opticalActivity*/ return this.opticalActivity == null ? new Base[0] : new Base[] {this.opticalActivity}; // CodeableConcept
        case 616660246: /*molecularFormula*/ return this.molecularFormula == null ? new Base[0] : new Base[] {this.molecularFormula}; // StringType
        case -1413853096: /*amount*/ return this.amount == null ? new Base[0] : new Base[] {this.amount}; // Type
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3506294: // role
          this.role = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1618432855: // identifier
          this.identifier = castToIdentifier(value); // Identifier
          return value;
        case 3373707: // name
          this.name = castToString(value); // StringType
          return value;
        case 263475116: // stereochemistry
          this.stereochemistry = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 1420900135: // opticalActivity
          this.opticalActivity = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 616660246: // molecularFormula
          this.molecularFormula = castToString(value); // StringType
          return value;
        case -1413853096: // amount
          this.amount = castToType(value); // Type
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("role")) {
          this.role = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("identifier")) {
          this.identifier = castToIdentifier(value); // Identifier
        } else if (name.equals("name")) {
          this.name = castToString(value); // StringType
        } else if (name.equals("stereochemistry")) {
          this.stereochemistry = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("opticalActivity")) {
          this.opticalActivity = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("molecularFormula")) {
          this.molecularFormula = castToString(value); // StringType
        } else if (name.equals("amount[x]")) {
          this.amount = castToType(value); // Type
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3506294:  return getRole(); 
        case -1618432855:  return getIdentifier(); 
        case 3373707:  return getNameElement();
        case 263475116:  return getStereochemistry(); 
        case 1420900135:  return getOpticalActivity(); 
        case 616660246:  return getMolecularFormulaElement();
        case 646780200:  return getAmount(); 
        case -1413853096:  return getAmount(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3506294: /*role*/ return new String[] {"CodeableConcept"};
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case 3373707: /*name*/ return new String[] {"string"};
        case 263475116: /*stereochemistry*/ return new String[] {"CodeableConcept"};
        case 1420900135: /*opticalActivity*/ return new String[] {"CodeableConcept"};
        case 616660246: /*molecularFormula*/ return new String[] {"string"};
        case -1413853096: /*amount*/ return new String[] {"Quantity", "string"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("role")) {
          this.role = new CodeableConcept();
          return this.role;
        }
        else if (name.equals("identifier")) {
          this.identifier = new Identifier();
          return this.identifier;
        }
        else if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type SubstanceSpecification.name");
        }
        else if (name.equals("stereochemistry")) {
          this.stereochemistry = new CodeableConcept();
          return this.stereochemistry;
        }
        else if (name.equals("opticalActivity")) {
          this.opticalActivity = new CodeableConcept();
          return this.opticalActivity;
        }
        else if (name.equals("molecularFormula")) {
          throw new FHIRException("Cannot call addChild on a primitive type SubstanceSpecification.molecularFormula");
        }
        else if (name.equals("amountQuantity")) {
          this.amount = new Quantity();
          return this.amount;
        }
        else if (name.equals("amountString")) {
          this.amount = new StringType();
          return this.amount;
        }
        else
          return super.addChild(name);
      }

      public SubstanceSpecificationMoietyComponent copy() {
        SubstanceSpecificationMoietyComponent dst = new SubstanceSpecificationMoietyComponent();
        copyValues(dst);
        dst.role = role == null ? null : role.copy();
        dst.identifier = identifier == null ? null : identifier.copy();
        dst.name = name == null ? null : name.copy();
        dst.stereochemistry = stereochemistry == null ? null : stereochemistry.copy();
        dst.opticalActivity = opticalActivity == null ? null : opticalActivity.copy();
        dst.molecularFormula = molecularFormula == null ? null : molecularFormula.copy();
        dst.amount = amount == null ? null : amount.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof SubstanceSpecificationMoietyComponent))
          return false;
        SubstanceSpecificationMoietyComponent o = (SubstanceSpecificationMoietyComponent) other_;
        return compareDeep(role, o.role, true) && compareDeep(identifier, o.identifier, true) && compareDeep(name, o.name, true)
           && compareDeep(stereochemistry, o.stereochemistry, true) && compareDeep(opticalActivity, o.opticalActivity, true)
           && compareDeep(molecularFormula, o.molecularFormula, true) && compareDeep(amount, o.amount, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof SubstanceSpecificationMoietyComponent))
          return false;
        SubstanceSpecificationMoietyComponent o = (SubstanceSpecificationMoietyComponent) other_;
        return compareValues(name, o.name, true) && compareValues(molecularFormula, o.molecularFormula, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(role, identifier, name, stereochemistry
          , opticalActivity, molecularFormula, amount);
      }

  public String fhirType() {
    return "SubstanceSpecification.moiety";

  }

  }

    @Block()
    public static class SubstanceSpecificationPropertyComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A category for this property, e.g. Physical, Chemical, Enzymatic.
         */
        @Child(name = "category", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="A category for this property, e.g. Physical, Chemical, Enzymatic", formalDefinition="A category for this property, e.g. Physical, Chemical, Enzymatic." )
        protected CodeableConcept category;

        /**
         * Property type e.g. viscosity, pH, isoelectric point.
         */
        @Child(name = "code", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Property type e.g. viscosity, pH, isoelectric point", formalDefinition="Property type e.g. viscosity, pH, isoelectric point." )
        protected CodeableConcept code;

        /**
         * Parameters that were used in the measurement of a property (e.g. for viscosity: measured at 20C with a pH of 7.1).
         */
        @Child(name = "parameters", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Parameters that were used in the measurement of a property (e.g. for viscosity: measured at 20C with a pH of 7.1)", formalDefinition="Parameters that were used in the measurement of a property (e.g. for viscosity: measured at 20C with a pH of 7.1)." )
        protected StringType parameters;

        /**
         * A substance upon which a defining property depends (e.g. for solubility: in water, in alcohol).
         */
        @Child(name = "definingSubstance", type = {SubstanceSpecification.class, Substance.class, CodeableConcept.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="A substance upon which a defining property depends (e.g. for solubility: in water, in alcohol)", formalDefinition="A substance upon which a defining property depends (e.g. for solubility: in water, in alcohol)." )
        protected Type definingSubstance;

        /**
         * Quantitative value for this property.
         */
        @Child(name = "amount", type = {Quantity.class, StringType.class}, order=5, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Quantitative value for this property", formalDefinition="Quantitative value for this property." )
        protected Type amount;

        private static final long serialVersionUID = 556834916L;

    /**
     * Constructor
     */
      public SubstanceSpecificationPropertyComponent() {
        super();
      }

        /**
         * @return {@link #category} (A category for this property, e.g. Physical, Chemical, Enzymatic.)
         */
        public CodeableConcept getCategory() { 
          if (this.category == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceSpecificationPropertyComponent.category");
            else if (Configuration.doAutoCreate())
              this.category = new CodeableConcept(); // cc
          return this.category;
        }

        public boolean hasCategory() { 
          return this.category != null && !this.category.isEmpty();
        }

        /**
         * @param value {@link #category} (A category for this property, e.g. Physical, Chemical, Enzymatic.)
         */
        public SubstanceSpecificationPropertyComponent setCategory(CodeableConcept value) { 
          this.category = value;
          return this;
        }

        /**
         * @return {@link #code} (Property type e.g. viscosity, pH, isoelectric point.)
         */
        public CodeableConcept getCode() { 
          if (this.code == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceSpecificationPropertyComponent.code");
            else if (Configuration.doAutoCreate())
              this.code = new CodeableConcept(); // cc
          return this.code;
        }

        public boolean hasCode() { 
          return this.code != null && !this.code.isEmpty();
        }

        /**
         * @param value {@link #code} (Property type e.g. viscosity, pH, isoelectric point.)
         */
        public SubstanceSpecificationPropertyComponent setCode(CodeableConcept value) { 
          this.code = value;
          return this;
        }

        /**
         * @return {@link #parameters} (Parameters that were used in the measurement of a property (e.g. for viscosity: measured at 20C with a pH of 7.1).). This is the underlying object with id, value and extensions. The accessor "getParameters" gives direct access to the value
         */
        public StringType getParametersElement() { 
          if (this.parameters == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceSpecificationPropertyComponent.parameters");
            else if (Configuration.doAutoCreate())
              this.parameters = new StringType(); // bb
          return this.parameters;
        }

        public boolean hasParametersElement() { 
          return this.parameters != null && !this.parameters.isEmpty();
        }

        public boolean hasParameters() { 
          return this.parameters != null && !this.parameters.isEmpty();
        }

        /**
         * @param value {@link #parameters} (Parameters that were used in the measurement of a property (e.g. for viscosity: measured at 20C with a pH of 7.1).). This is the underlying object with id, value and extensions. The accessor "getParameters" gives direct access to the value
         */
        public SubstanceSpecificationPropertyComponent setParametersElement(StringType value) { 
          this.parameters = value;
          return this;
        }

        /**
         * @return Parameters that were used in the measurement of a property (e.g. for viscosity: measured at 20C with a pH of 7.1).
         */
        public String getParameters() { 
          return this.parameters == null ? null : this.parameters.getValue();
        }

        /**
         * @param value Parameters that were used in the measurement of a property (e.g. for viscosity: measured at 20C with a pH of 7.1).
         */
        public SubstanceSpecificationPropertyComponent setParameters(String value) { 
          if (Utilities.noString(value))
            this.parameters = null;
          else {
            if (this.parameters == null)
              this.parameters = new StringType();
            this.parameters.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #definingSubstance} (A substance upon which a defining property depends (e.g. for solubility: in water, in alcohol).)
         */
        public Type getDefiningSubstance() { 
          return this.definingSubstance;
        }

        /**
         * @return {@link #definingSubstance} (A substance upon which a defining property depends (e.g. for solubility: in water, in alcohol).)
         */
        public Reference getDefiningSubstanceReference() throws FHIRException { 
          if (this.definingSubstance == null)
            this.definingSubstance = new Reference();
          if (!(this.definingSubstance instanceof Reference))
            throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.definingSubstance.getClass().getName()+" was encountered");
          return (Reference) this.definingSubstance;
        }

        public boolean hasDefiningSubstanceReference() { 
          return this != null && this.definingSubstance instanceof Reference;
        }

        /**
         * @return {@link #definingSubstance} (A substance upon which a defining property depends (e.g. for solubility: in water, in alcohol).)
         */
        public CodeableConcept getDefiningSubstanceCodeableConcept() throws FHIRException { 
          if (this.definingSubstance == null)
            this.definingSubstance = new CodeableConcept();
          if (!(this.definingSubstance instanceof CodeableConcept))
            throw new FHIRException("Type mismatch: the type CodeableConcept was expected, but "+this.definingSubstance.getClass().getName()+" was encountered");
          return (CodeableConcept) this.definingSubstance;
        }

        public boolean hasDefiningSubstanceCodeableConcept() { 
          return this != null && this.definingSubstance instanceof CodeableConcept;
        }

        public boolean hasDefiningSubstance() { 
          return this.definingSubstance != null && !this.definingSubstance.isEmpty();
        }

        /**
         * @param value {@link #definingSubstance} (A substance upon which a defining property depends (e.g. for solubility: in water, in alcohol).)
         */
        public SubstanceSpecificationPropertyComponent setDefiningSubstance(Type value) { 
          if (value != null && !(value instanceof Reference || value instanceof CodeableConcept))
            throw new Error("Not the right type for SubstanceSpecification.property.definingSubstance[x]: "+value.fhirType());
          this.definingSubstance = value;
          return this;
        }

        /**
         * @return {@link #amount} (Quantitative value for this property.)
         */
        public Type getAmount() { 
          return this.amount;
        }

        /**
         * @return {@link #amount} (Quantitative value for this property.)
         */
        public Quantity getAmountQuantity() throws FHIRException { 
          if (this.amount == null)
            this.amount = new Quantity();
          if (!(this.amount instanceof Quantity))
            throw new FHIRException("Type mismatch: the type Quantity was expected, but "+this.amount.getClass().getName()+" was encountered");
          return (Quantity) this.amount;
        }

        public boolean hasAmountQuantity() { 
          return this != null && this.amount instanceof Quantity;
        }

        /**
         * @return {@link #amount} (Quantitative value for this property.)
         */
        public StringType getAmountStringType() throws FHIRException { 
          if (this.amount == null)
            this.amount = new StringType();
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
         * @param value {@link #amount} (Quantitative value for this property.)
         */
        public SubstanceSpecificationPropertyComponent setAmount(Type value) { 
          if (value != null && !(value instanceof Quantity || value instanceof StringType))
            throw new Error("Not the right type for SubstanceSpecification.property.amount[x]: "+value.fhirType());
          this.amount = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("category", "CodeableConcept", "A category for this property, e.g. Physical, Chemical, Enzymatic.", 0, 1, category));
          children.add(new Property("code", "CodeableConcept", "Property type e.g. viscosity, pH, isoelectric point.", 0, 1, code));
          children.add(new Property("parameters", "string", "Parameters that were used in the measurement of a property (e.g. for viscosity: measured at 20C with a pH of 7.1).", 0, 1, parameters));
          children.add(new Property("definingSubstance[x]", "Reference(SubstanceSpecification|Substance)|CodeableConcept", "A substance upon which a defining property depends (e.g. for solubility: in water, in alcohol).", 0, 1, definingSubstance));
          children.add(new Property("amount[x]", "Quantity|string", "Quantitative value for this property.", 0, 1, amount));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 50511102: /*category*/  return new Property("category", "CodeableConcept", "A category for this property, e.g. Physical, Chemical, Enzymatic.", 0, 1, category);
          case 3059181: /*code*/  return new Property("code", "CodeableConcept", "Property type e.g. viscosity, pH, isoelectric point.", 0, 1, code);
          case 458736106: /*parameters*/  return new Property("parameters", "string", "Parameters that were used in the measurement of a property (e.g. for viscosity: measured at 20C with a pH of 7.1).", 0, 1, parameters);
          case 1535270120: /*definingSubstance[x]*/  return new Property("definingSubstance[x]", "Reference(SubstanceSpecification|Substance)|CodeableConcept", "A substance upon which a defining property depends (e.g. for solubility: in water, in alcohol).", 0, 1, definingSubstance);
          case 1901076632: /*definingSubstance*/  return new Property("definingSubstance[x]", "Reference(SubstanceSpecification|Substance)|CodeableConcept", "A substance upon which a defining property depends (e.g. for solubility: in water, in alcohol).", 0, 1, definingSubstance);
          case -2101581421: /*definingSubstanceReference*/  return new Property("definingSubstance[x]", "Reference(SubstanceSpecification|Substance)|CodeableConcept", "A substance upon which a defining property depends (e.g. for solubility: in water, in alcohol).", 0, 1, definingSubstance);
          case -1438235671: /*definingSubstanceCodeableConcept*/  return new Property("definingSubstance[x]", "Reference(SubstanceSpecification|Substance)|CodeableConcept", "A substance upon which a defining property depends (e.g. for solubility: in water, in alcohol).", 0, 1, definingSubstance);
          case 646780200: /*amount[x]*/  return new Property("amount[x]", "Quantity|string", "Quantitative value for this property.", 0, 1, amount);
          case -1413853096: /*amount*/  return new Property("amount[x]", "Quantity|string", "Quantitative value for this property.", 0, 1, amount);
          case 1664303363: /*amountQuantity*/  return new Property("amount[x]", "Quantity|string", "Quantitative value for this property.", 0, 1, amount);
          case 773651081: /*amountString*/  return new Property("amount[x]", "Quantity|string", "Quantitative value for this property.", 0, 1, amount);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 50511102: /*category*/ return this.category == null ? new Base[0] : new Base[] {this.category}; // CodeableConcept
        case 3059181: /*code*/ return this.code == null ? new Base[0] : new Base[] {this.code}; // CodeableConcept
        case 458736106: /*parameters*/ return this.parameters == null ? new Base[0] : new Base[] {this.parameters}; // StringType
        case 1901076632: /*definingSubstance*/ return this.definingSubstance == null ? new Base[0] : new Base[] {this.definingSubstance}; // Type
        case -1413853096: /*amount*/ return this.amount == null ? new Base[0] : new Base[] {this.amount}; // Type
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 50511102: // category
          this.category = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 3059181: // code
          this.code = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 458736106: // parameters
          this.parameters = castToString(value); // StringType
          return value;
        case 1901076632: // definingSubstance
          this.definingSubstance = castToType(value); // Type
          return value;
        case -1413853096: // amount
          this.amount = castToType(value); // Type
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("category")) {
          this.category = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("code")) {
          this.code = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("parameters")) {
          this.parameters = castToString(value); // StringType
        } else if (name.equals("definingSubstance[x]")) {
          this.definingSubstance = castToType(value); // Type
        } else if (name.equals("amount[x]")) {
          this.amount = castToType(value); // Type
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 50511102:  return getCategory(); 
        case 3059181:  return getCode(); 
        case 458736106:  return getParametersElement();
        case 1535270120:  return getDefiningSubstance(); 
        case 1901076632:  return getDefiningSubstance(); 
        case 646780200:  return getAmount(); 
        case -1413853096:  return getAmount(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 50511102: /*category*/ return new String[] {"CodeableConcept"};
        case 3059181: /*code*/ return new String[] {"CodeableConcept"};
        case 458736106: /*parameters*/ return new String[] {"string"};
        case 1901076632: /*definingSubstance*/ return new String[] {"Reference", "CodeableConcept"};
        case -1413853096: /*amount*/ return new String[] {"Quantity", "string"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("category")) {
          this.category = new CodeableConcept();
          return this.category;
        }
        else if (name.equals("code")) {
          this.code = new CodeableConcept();
          return this.code;
        }
        else if (name.equals("parameters")) {
          throw new FHIRException("Cannot call addChild on a primitive type SubstanceSpecification.parameters");
        }
        else if (name.equals("definingSubstanceReference")) {
          this.definingSubstance = new Reference();
          return this.definingSubstance;
        }
        else if (name.equals("definingSubstanceCodeableConcept")) {
          this.definingSubstance = new CodeableConcept();
          return this.definingSubstance;
        }
        else if (name.equals("amountQuantity")) {
          this.amount = new Quantity();
          return this.amount;
        }
        else if (name.equals("amountString")) {
          this.amount = new StringType();
          return this.amount;
        }
        else
          return super.addChild(name);
      }

      public SubstanceSpecificationPropertyComponent copy() {
        SubstanceSpecificationPropertyComponent dst = new SubstanceSpecificationPropertyComponent();
        copyValues(dst);
        dst.category = category == null ? null : category.copy();
        dst.code = code == null ? null : code.copy();
        dst.parameters = parameters == null ? null : parameters.copy();
        dst.definingSubstance = definingSubstance == null ? null : definingSubstance.copy();
        dst.amount = amount == null ? null : amount.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof SubstanceSpecificationPropertyComponent))
          return false;
        SubstanceSpecificationPropertyComponent o = (SubstanceSpecificationPropertyComponent) other_;
        return compareDeep(category, o.category, true) && compareDeep(code, o.code, true) && compareDeep(parameters, o.parameters, true)
           && compareDeep(definingSubstance, o.definingSubstance, true) && compareDeep(amount, o.amount, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof SubstanceSpecificationPropertyComponent))
          return false;
        SubstanceSpecificationPropertyComponent o = (SubstanceSpecificationPropertyComponent) other_;
        return compareValues(parameters, o.parameters, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(category, code, parameters
          , definingSubstance, amount);
      }

  public String fhirType() {
    return "SubstanceSpecification.property";

  }

  }

    @Block()
    public static class SubstanceSpecificationStructureComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Stereochemistry type.
         */
        @Child(name = "stereochemistry", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Stereochemistry type", formalDefinition="Stereochemistry type." )
        protected CodeableConcept stereochemistry;

        /**
         * Optical activity type.
         */
        @Child(name = "opticalActivity", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Optical activity type", formalDefinition="Optical activity type." )
        protected CodeableConcept opticalActivity;

        /**
         * Molecular formula.
         */
        @Child(name = "molecularFormula", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Molecular formula", formalDefinition="Molecular formula." )
        protected StringType molecularFormula;

        /**
         * Specified per moiety according to the Hill system, i.e. first C, then H, then alphabetical, each moiety separated by a dot.
         */
        @Child(name = "molecularFormulaByMoiety", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Specified per moiety according to the Hill system, i.e. first C, then H, then alphabetical, each moiety separated by a dot", formalDefinition="Specified per moiety according to the Hill system, i.e. first C, then H, then alphabetical, each moiety separated by a dot." )
        protected StringType molecularFormulaByMoiety;

        /**
         * Applicable for single substances that contain a radionuclide or a non-natural isotopic ratio.
         */
        @Child(name = "isotope", type = {}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Applicable for single substances that contain a radionuclide or a non-natural isotopic ratio", formalDefinition="Applicable for single substances that contain a radionuclide or a non-natural isotopic ratio." )
        protected List<SubstanceSpecificationStructureIsotopeComponent> isotope;

        /**
         * The molecular weight or weight range (for proteins, polymers or nucleic acids).
         */
        @Child(name = "molecularWeight", type = {SubstanceSpecificationStructureIsotopeMolecularWeightComponent.class}, order=6, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The molecular weight or weight range (for proteins, polymers or nucleic acids)", formalDefinition="The molecular weight or weight range (for proteins, polymers or nucleic acids)." )
        protected SubstanceSpecificationStructureIsotopeMolecularWeightComponent molecularWeight;

        /**
         * Supporting literature.
         */
        @Child(name = "source", type = {DocumentReference.class}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Supporting literature", formalDefinition="Supporting literature." )
        protected List<Reference> source;
        /**
         * The actual objects that are the target of the reference (Supporting literature.)
         */
        protected List<DocumentReference> sourceTarget;


        /**
         * Molecular structural representation.
         */
        @Child(name = "representation", type = {}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Molecular structural representation", formalDefinition="Molecular structural representation." )
        protected List<SubstanceSpecificationStructureRepresentationComponent> representation;

        private static final long serialVersionUID = -851521497L;

    /**
     * Constructor
     */
      public SubstanceSpecificationStructureComponent() {
        super();
      }

        /**
         * @return {@link #stereochemistry} (Stereochemistry type.)
         */
        public CodeableConcept getStereochemistry() { 
          if (this.stereochemistry == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceSpecificationStructureComponent.stereochemistry");
            else if (Configuration.doAutoCreate())
              this.stereochemistry = new CodeableConcept(); // cc
          return this.stereochemistry;
        }

        public boolean hasStereochemistry() { 
          return this.stereochemistry != null && !this.stereochemistry.isEmpty();
        }

        /**
         * @param value {@link #stereochemistry} (Stereochemistry type.)
         */
        public SubstanceSpecificationStructureComponent setStereochemistry(CodeableConcept value) { 
          this.stereochemistry = value;
          return this;
        }

        /**
         * @return {@link #opticalActivity} (Optical activity type.)
         */
        public CodeableConcept getOpticalActivity() { 
          if (this.opticalActivity == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceSpecificationStructureComponent.opticalActivity");
            else if (Configuration.doAutoCreate())
              this.opticalActivity = new CodeableConcept(); // cc
          return this.opticalActivity;
        }

        public boolean hasOpticalActivity() { 
          return this.opticalActivity != null && !this.opticalActivity.isEmpty();
        }

        /**
         * @param value {@link #opticalActivity} (Optical activity type.)
         */
        public SubstanceSpecificationStructureComponent setOpticalActivity(CodeableConcept value) { 
          this.opticalActivity = value;
          return this;
        }

        /**
         * @return {@link #molecularFormula} (Molecular formula.). This is the underlying object with id, value and extensions. The accessor "getMolecularFormula" gives direct access to the value
         */
        public StringType getMolecularFormulaElement() { 
          if (this.molecularFormula == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceSpecificationStructureComponent.molecularFormula");
            else if (Configuration.doAutoCreate())
              this.molecularFormula = new StringType(); // bb
          return this.molecularFormula;
        }

        public boolean hasMolecularFormulaElement() { 
          return this.molecularFormula != null && !this.molecularFormula.isEmpty();
        }

        public boolean hasMolecularFormula() { 
          return this.molecularFormula != null && !this.molecularFormula.isEmpty();
        }

        /**
         * @param value {@link #molecularFormula} (Molecular formula.). This is the underlying object with id, value and extensions. The accessor "getMolecularFormula" gives direct access to the value
         */
        public SubstanceSpecificationStructureComponent setMolecularFormulaElement(StringType value) { 
          this.molecularFormula = value;
          return this;
        }

        /**
         * @return Molecular formula.
         */
        public String getMolecularFormula() { 
          return this.molecularFormula == null ? null : this.molecularFormula.getValue();
        }

        /**
         * @param value Molecular formula.
         */
        public SubstanceSpecificationStructureComponent setMolecularFormula(String value) { 
          if (Utilities.noString(value))
            this.molecularFormula = null;
          else {
            if (this.molecularFormula == null)
              this.molecularFormula = new StringType();
            this.molecularFormula.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #molecularFormulaByMoiety} (Specified per moiety according to the Hill system, i.e. first C, then H, then alphabetical, each moiety separated by a dot.). This is the underlying object with id, value and extensions. The accessor "getMolecularFormulaByMoiety" gives direct access to the value
         */
        public StringType getMolecularFormulaByMoietyElement() { 
          if (this.molecularFormulaByMoiety == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceSpecificationStructureComponent.molecularFormulaByMoiety");
            else if (Configuration.doAutoCreate())
              this.molecularFormulaByMoiety = new StringType(); // bb
          return this.molecularFormulaByMoiety;
        }

        public boolean hasMolecularFormulaByMoietyElement() { 
          return this.molecularFormulaByMoiety != null && !this.molecularFormulaByMoiety.isEmpty();
        }

        public boolean hasMolecularFormulaByMoiety() { 
          return this.molecularFormulaByMoiety != null && !this.molecularFormulaByMoiety.isEmpty();
        }

        /**
         * @param value {@link #molecularFormulaByMoiety} (Specified per moiety according to the Hill system, i.e. first C, then H, then alphabetical, each moiety separated by a dot.). This is the underlying object with id, value and extensions. The accessor "getMolecularFormulaByMoiety" gives direct access to the value
         */
        public SubstanceSpecificationStructureComponent setMolecularFormulaByMoietyElement(StringType value) { 
          this.molecularFormulaByMoiety = value;
          return this;
        }

        /**
         * @return Specified per moiety according to the Hill system, i.e. first C, then H, then alphabetical, each moiety separated by a dot.
         */
        public String getMolecularFormulaByMoiety() { 
          return this.molecularFormulaByMoiety == null ? null : this.molecularFormulaByMoiety.getValue();
        }

        /**
         * @param value Specified per moiety according to the Hill system, i.e. first C, then H, then alphabetical, each moiety separated by a dot.
         */
        public SubstanceSpecificationStructureComponent setMolecularFormulaByMoiety(String value) { 
          if (Utilities.noString(value))
            this.molecularFormulaByMoiety = null;
          else {
            if (this.molecularFormulaByMoiety == null)
              this.molecularFormulaByMoiety = new StringType();
            this.molecularFormulaByMoiety.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #isotope} (Applicable for single substances that contain a radionuclide or a non-natural isotopic ratio.)
         */
        public List<SubstanceSpecificationStructureIsotopeComponent> getIsotope() { 
          if (this.isotope == null)
            this.isotope = new ArrayList<SubstanceSpecificationStructureIsotopeComponent>();
          return this.isotope;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public SubstanceSpecificationStructureComponent setIsotope(List<SubstanceSpecificationStructureIsotopeComponent> theIsotope) { 
          this.isotope = theIsotope;
          return this;
        }

        public boolean hasIsotope() { 
          if (this.isotope == null)
            return false;
          for (SubstanceSpecificationStructureIsotopeComponent item : this.isotope)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public SubstanceSpecificationStructureIsotopeComponent addIsotope() { //3
          SubstanceSpecificationStructureIsotopeComponent t = new SubstanceSpecificationStructureIsotopeComponent();
          if (this.isotope == null)
            this.isotope = new ArrayList<SubstanceSpecificationStructureIsotopeComponent>();
          this.isotope.add(t);
          return t;
        }

        public SubstanceSpecificationStructureComponent addIsotope(SubstanceSpecificationStructureIsotopeComponent t) { //3
          if (t == null)
            return this;
          if (this.isotope == null)
            this.isotope = new ArrayList<SubstanceSpecificationStructureIsotopeComponent>();
          this.isotope.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #isotope}, creating it if it does not already exist
         */
        public SubstanceSpecificationStructureIsotopeComponent getIsotopeFirstRep() { 
          if (getIsotope().isEmpty()) {
            addIsotope();
          }
          return getIsotope().get(0);
        }

        /**
         * @return {@link #molecularWeight} (The molecular weight or weight range (for proteins, polymers or nucleic acids).)
         */
        public SubstanceSpecificationStructureIsotopeMolecularWeightComponent getMolecularWeight() { 
          if (this.molecularWeight == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceSpecificationStructureComponent.molecularWeight");
            else if (Configuration.doAutoCreate())
              this.molecularWeight = new SubstanceSpecificationStructureIsotopeMolecularWeightComponent(); // cc
          return this.molecularWeight;
        }

        public boolean hasMolecularWeight() { 
          return this.molecularWeight != null && !this.molecularWeight.isEmpty();
        }

        /**
         * @param value {@link #molecularWeight} (The molecular weight or weight range (for proteins, polymers or nucleic acids).)
         */
        public SubstanceSpecificationStructureComponent setMolecularWeight(SubstanceSpecificationStructureIsotopeMolecularWeightComponent value) { 
          this.molecularWeight = value;
          return this;
        }

        /**
         * @return {@link #source} (Supporting literature.)
         */
        public List<Reference> getSource() { 
          if (this.source == null)
            this.source = new ArrayList<Reference>();
          return this.source;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public SubstanceSpecificationStructureComponent setSource(List<Reference> theSource) { 
          this.source = theSource;
          return this;
        }

        public boolean hasSource() { 
          if (this.source == null)
            return false;
          for (Reference item : this.source)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public Reference addSource() { //3
          Reference t = new Reference();
          if (this.source == null)
            this.source = new ArrayList<Reference>();
          this.source.add(t);
          return t;
        }

        public SubstanceSpecificationStructureComponent addSource(Reference t) { //3
          if (t == null)
            return this;
          if (this.source == null)
            this.source = new ArrayList<Reference>();
          this.source.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #source}, creating it if it does not already exist
         */
        public Reference getSourceFirstRep() { 
          if (getSource().isEmpty()) {
            addSource();
          }
          return getSource().get(0);
        }

        /**
         * @deprecated Use Reference#setResource(IBaseResource) instead
         */
        @Deprecated
        public List<DocumentReference> getSourceTarget() { 
          if (this.sourceTarget == null)
            this.sourceTarget = new ArrayList<DocumentReference>();
          return this.sourceTarget;
        }

        /**
         * @deprecated Use Reference#setResource(IBaseResource) instead
         */
        @Deprecated
        public DocumentReference addSourceTarget() { 
          DocumentReference r = new DocumentReference();
          if (this.sourceTarget == null)
            this.sourceTarget = new ArrayList<DocumentReference>();
          this.sourceTarget.add(r);
          return r;
        }

        /**
         * @return {@link #representation} (Molecular structural representation.)
         */
        public List<SubstanceSpecificationStructureRepresentationComponent> getRepresentation() { 
          if (this.representation == null)
            this.representation = new ArrayList<SubstanceSpecificationStructureRepresentationComponent>();
          return this.representation;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public SubstanceSpecificationStructureComponent setRepresentation(List<SubstanceSpecificationStructureRepresentationComponent> theRepresentation) { 
          this.representation = theRepresentation;
          return this;
        }

        public boolean hasRepresentation() { 
          if (this.representation == null)
            return false;
          for (SubstanceSpecificationStructureRepresentationComponent item : this.representation)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public SubstanceSpecificationStructureRepresentationComponent addRepresentation() { //3
          SubstanceSpecificationStructureRepresentationComponent t = new SubstanceSpecificationStructureRepresentationComponent();
          if (this.representation == null)
            this.representation = new ArrayList<SubstanceSpecificationStructureRepresentationComponent>();
          this.representation.add(t);
          return t;
        }

        public SubstanceSpecificationStructureComponent addRepresentation(SubstanceSpecificationStructureRepresentationComponent t) { //3
          if (t == null)
            return this;
          if (this.representation == null)
            this.representation = new ArrayList<SubstanceSpecificationStructureRepresentationComponent>();
          this.representation.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #representation}, creating it if it does not already exist
         */
        public SubstanceSpecificationStructureRepresentationComponent getRepresentationFirstRep() { 
          if (getRepresentation().isEmpty()) {
            addRepresentation();
          }
          return getRepresentation().get(0);
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("stereochemistry", "CodeableConcept", "Stereochemistry type.", 0, 1, stereochemistry));
          children.add(new Property("opticalActivity", "CodeableConcept", "Optical activity type.", 0, 1, opticalActivity));
          children.add(new Property("molecularFormula", "string", "Molecular formula.", 0, 1, molecularFormula));
          children.add(new Property("molecularFormulaByMoiety", "string", "Specified per moiety according to the Hill system, i.e. first C, then H, then alphabetical, each moiety separated by a dot.", 0, 1, molecularFormulaByMoiety));
          children.add(new Property("isotope", "", "Applicable for single substances that contain a radionuclide or a non-natural isotopic ratio.", 0, java.lang.Integer.MAX_VALUE, isotope));
          children.add(new Property("molecularWeight", "@SubstanceSpecification.structure.isotope.molecularWeight", "The molecular weight or weight range (for proteins, polymers or nucleic acids).", 0, 1, molecularWeight));
          children.add(new Property("source", "Reference(DocumentReference)", "Supporting literature.", 0, java.lang.Integer.MAX_VALUE, source));
          children.add(new Property("representation", "", "Molecular structural representation.", 0, java.lang.Integer.MAX_VALUE, representation));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 263475116: /*stereochemistry*/  return new Property("stereochemistry", "CodeableConcept", "Stereochemistry type.", 0, 1, stereochemistry);
          case 1420900135: /*opticalActivity*/  return new Property("opticalActivity", "CodeableConcept", "Optical activity type.", 0, 1, opticalActivity);
          case 616660246: /*molecularFormula*/  return new Property("molecularFormula", "string", "Molecular formula.", 0, 1, molecularFormula);
          case 1315452848: /*molecularFormulaByMoiety*/  return new Property("molecularFormulaByMoiety", "string", "Specified per moiety according to the Hill system, i.e. first C, then H, then alphabetical, each moiety separated by a dot.", 0, 1, molecularFormulaByMoiety);
          case 2097035189: /*isotope*/  return new Property("isotope", "", "Applicable for single substances that contain a radionuclide or a non-natural isotopic ratio.", 0, java.lang.Integer.MAX_VALUE, isotope);
          case 635625672: /*molecularWeight*/  return new Property("molecularWeight", "@SubstanceSpecification.structure.isotope.molecularWeight", "The molecular weight or weight range (for proteins, polymers or nucleic acids).", 0, 1, molecularWeight);
          case -896505829: /*source*/  return new Property("source", "Reference(DocumentReference)", "Supporting literature.", 0, java.lang.Integer.MAX_VALUE, source);
          case -671065907: /*representation*/  return new Property("representation", "", "Molecular structural representation.", 0, java.lang.Integer.MAX_VALUE, representation);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 263475116: /*stereochemistry*/ return this.stereochemistry == null ? new Base[0] : new Base[] {this.stereochemistry}; // CodeableConcept
        case 1420900135: /*opticalActivity*/ return this.opticalActivity == null ? new Base[0] : new Base[] {this.opticalActivity}; // CodeableConcept
        case 616660246: /*molecularFormula*/ return this.molecularFormula == null ? new Base[0] : new Base[] {this.molecularFormula}; // StringType
        case 1315452848: /*molecularFormulaByMoiety*/ return this.molecularFormulaByMoiety == null ? new Base[0] : new Base[] {this.molecularFormulaByMoiety}; // StringType
        case 2097035189: /*isotope*/ return this.isotope == null ? new Base[0] : this.isotope.toArray(new Base[this.isotope.size()]); // SubstanceSpecificationStructureIsotopeComponent
        case 635625672: /*molecularWeight*/ return this.molecularWeight == null ? new Base[0] : new Base[] {this.molecularWeight}; // SubstanceSpecificationStructureIsotopeMolecularWeightComponent
        case -896505829: /*source*/ return this.source == null ? new Base[0] : this.source.toArray(new Base[this.source.size()]); // Reference
        case -671065907: /*representation*/ return this.representation == null ? new Base[0] : this.representation.toArray(new Base[this.representation.size()]); // SubstanceSpecificationStructureRepresentationComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 263475116: // stereochemistry
          this.stereochemistry = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 1420900135: // opticalActivity
          this.opticalActivity = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 616660246: // molecularFormula
          this.molecularFormula = castToString(value); // StringType
          return value;
        case 1315452848: // molecularFormulaByMoiety
          this.molecularFormulaByMoiety = castToString(value); // StringType
          return value;
        case 2097035189: // isotope
          this.getIsotope().add((SubstanceSpecificationStructureIsotopeComponent) value); // SubstanceSpecificationStructureIsotopeComponent
          return value;
        case 635625672: // molecularWeight
          this.molecularWeight = (SubstanceSpecificationStructureIsotopeMolecularWeightComponent) value; // SubstanceSpecificationStructureIsotopeMolecularWeightComponent
          return value;
        case -896505829: // source
          this.getSource().add(castToReference(value)); // Reference
          return value;
        case -671065907: // representation
          this.getRepresentation().add((SubstanceSpecificationStructureRepresentationComponent) value); // SubstanceSpecificationStructureRepresentationComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("stereochemistry")) {
          this.stereochemistry = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("opticalActivity")) {
          this.opticalActivity = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("molecularFormula")) {
          this.molecularFormula = castToString(value); // StringType
        } else if (name.equals("molecularFormulaByMoiety")) {
          this.molecularFormulaByMoiety = castToString(value); // StringType
        } else if (name.equals("isotope")) {
          this.getIsotope().add((SubstanceSpecificationStructureIsotopeComponent) value);
        } else if (name.equals("molecularWeight")) {
          this.molecularWeight = (SubstanceSpecificationStructureIsotopeMolecularWeightComponent) value; // SubstanceSpecificationStructureIsotopeMolecularWeightComponent
        } else if (name.equals("source")) {
          this.getSource().add(castToReference(value));
        } else if (name.equals("representation")) {
          this.getRepresentation().add((SubstanceSpecificationStructureRepresentationComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 263475116:  return getStereochemistry(); 
        case 1420900135:  return getOpticalActivity(); 
        case 616660246:  return getMolecularFormulaElement();
        case 1315452848:  return getMolecularFormulaByMoietyElement();
        case 2097035189:  return addIsotope(); 
        case 635625672:  return getMolecularWeight(); 
        case -896505829:  return addSource(); 
        case -671065907:  return addRepresentation(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 263475116: /*stereochemistry*/ return new String[] {"CodeableConcept"};
        case 1420900135: /*opticalActivity*/ return new String[] {"CodeableConcept"};
        case 616660246: /*molecularFormula*/ return new String[] {"string"};
        case 1315452848: /*molecularFormulaByMoiety*/ return new String[] {"string"};
        case 2097035189: /*isotope*/ return new String[] {};
        case 635625672: /*molecularWeight*/ return new String[] {"@SubstanceSpecification.structure.isotope.molecularWeight"};
        case -896505829: /*source*/ return new String[] {"Reference"};
        case -671065907: /*representation*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("stereochemistry")) {
          this.stereochemistry = new CodeableConcept();
          return this.stereochemistry;
        }
        else if (name.equals("opticalActivity")) {
          this.opticalActivity = new CodeableConcept();
          return this.opticalActivity;
        }
        else if (name.equals("molecularFormula")) {
          throw new FHIRException("Cannot call addChild on a primitive type SubstanceSpecification.molecularFormula");
        }
        else if (name.equals("molecularFormulaByMoiety")) {
          throw new FHIRException("Cannot call addChild on a primitive type SubstanceSpecification.molecularFormulaByMoiety");
        }
        else if (name.equals("isotope")) {
          return addIsotope();
        }
        else if (name.equals("molecularWeight")) {
          this.molecularWeight = new SubstanceSpecificationStructureIsotopeMolecularWeightComponent();
          return this.molecularWeight;
        }
        else if (name.equals("source")) {
          return addSource();
        }
        else if (name.equals("representation")) {
          return addRepresentation();
        }
        else
          return super.addChild(name);
      }

      public SubstanceSpecificationStructureComponent copy() {
        SubstanceSpecificationStructureComponent dst = new SubstanceSpecificationStructureComponent();
        copyValues(dst);
        dst.stereochemistry = stereochemistry == null ? null : stereochemistry.copy();
        dst.opticalActivity = opticalActivity == null ? null : opticalActivity.copy();
        dst.molecularFormula = molecularFormula == null ? null : molecularFormula.copy();
        dst.molecularFormulaByMoiety = molecularFormulaByMoiety == null ? null : molecularFormulaByMoiety.copy();
        if (isotope != null) {
          dst.isotope = new ArrayList<SubstanceSpecificationStructureIsotopeComponent>();
          for (SubstanceSpecificationStructureIsotopeComponent i : isotope)
            dst.isotope.add(i.copy());
        };
        dst.molecularWeight = molecularWeight == null ? null : molecularWeight.copy();
        if (source != null) {
          dst.source = new ArrayList<Reference>();
          for (Reference i : source)
            dst.source.add(i.copy());
        };
        if (representation != null) {
          dst.representation = new ArrayList<SubstanceSpecificationStructureRepresentationComponent>();
          for (SubstanceSpecificationStructureRepresentationComponent i : representation)
            dst.representation.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof SubstanceSpecificationStructureComponent))
          return false;
        SubstanceSpecificationStructureComponent o = (SubstanceSpecificationStructureComponent) other_;
        return compareDeep(stereochemistry, o.stereochemistry, true) && compareDeep(opticalActivity, o.opticalActivity, true)
           && compareDeep(molecularFormula, o.molecularFormula, true) && compareDeep(molecularFormulaByMoiety, o.molecularFormulaByMoiety, true)
           && compareDeep(isotope, o.isotope, true) && compareDeep(molecularWeight, o.molecularWeight, true)
           && compareDeep(source, o.source, true) && compareDeep(representation, o.representation, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof SubstanceSpecificationStructureComponent))
          return false;
        SubstanceSpecificationStructureComponent o = (SubstanceSpecificationStructureComponent) other_;
        return compareValues(molecularFormula, o.molecularFormula, true) && compareValues(molecularFormulaByMoiety, o.molecularFormulaByMoiety, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(stereochemistry, opticalActivity
          , molecularFormula, molecularFormulaByMoiety, isotope, molecularWeight, source, representation
          );
      }

  public String fhirType() {
    return "SubstanceSpecification.structure";

  }

  }

    @Block()
    public static class SubstanceSpecificationStructureIsotopeComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Substance identifier for each non-natural or radioisotope.
         */
        @Child(name = "identifier", type = {Identifier.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Substance identifier for each non-natural or radioisotope", formalDefinition="Substance identifier for each non-natural or radioisotope." )
        protected Identifier identifier;

        /**
         * Substance name for each non-natural or radioisotope.
         */
        @Child(name = "name", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Substance name for each non-natural or radioisotope", formalDefinition="Substance name for each non-natural or radioisotope." )
        protected CodeableConcept name;

        /**
         * The type of isotopic substitution present in a single substance.
         */
        @Child(name = "substitution", type = {CodeableConcept.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The type of isotopic substitution present in a single substance", formalDefinition="The type of isotopic substitution present in a single substance." )
        protected CodeableConcept substitution;

        /**
         * Half life - for a non-natural nuclide.
         */
        @Child(name = "halfLife", type = {Quantity.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Half life - for a non-natural nuclide", formalDefinition="Half life - for a non-natural nuclide." )
        protected Quantity halfLife;

        /**
         * The molecular weight or weight range (for proteins, polymers or nucleic acids).
         */
        @Child(name = "molecularWeight", type = {}, order=5, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The molecular weight or weight range (for proteins, polymers or nucleic acids)", formalDefinition="The molecular weight or weight range (for proteins, polymers or nucleic acids)." )
        protected SubstanceSpecificationStructureIsotopeMolecularWeightComponent molecularWeight;

        private static final long serialVersionUID = -531167114L;

    /**
     * Constructor
     */
      public SubstanceSpecificationStructureIsotopeComponent() {
        super();
      }

        /**
         * @return {@link #identifier} (Substance identifier for each non-natural or radioisotope.)
         */
        public Identifier getIdentifier() { 
          if (this.identifier == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceSpecificationStructureIsotopeComponent.identifier");
            else if (Configuration.doAutoCreate())
              this.identifier = new Identifier(); // cc
          return this.identifier;
        }

        public boolean hasIdentifier() { 
          return this.identifier != null && !this.identifier.isEmpty();
        }

        /**
         * @param value {@link #identifier} (Substance identifier for each non-natural or radioisotope.)
         */
        public SubstanceSpecificationStructureIsotopeComponent setIdentifier(Identifier value) { 
          this.identifier = value;
          return this;
        }

        /**
         * @return {@link #name} (Substance name for each non-natural or radioisotope.)
         */
        public CodeableConcept getName() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceSpecificationStructureIsotopeComponent.name");
            else if (Configuration.doAutoCreate())
              this.name = new CodeableConcept(); // cc
          return this.name;
        }

        public boolean hasName() { 
          return this.name != null && !this.name.isEmpty();
        }

        /**
         * @param value {@link #name} (Substance name for each non-natural or radioisotope.)
         */
        public SubstanceSpecificationStructureIsotopeComponent setName(CodeableConcept value) { 
          this.name = value;
          return this;
        }

        /**
         * @return {@link #substitution} (The type of isotopic substitution present in a single substance.)
         */
        public CodeableConcept getSubstitution() { 
          if (this.substitution == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceSpecificationStructureIsotopeComponent.substitution");
            else if (Configuration.doAutoCreate())
              this.substitution = new CodeableConcept(); // cc
          return this.substitution;
        }

        public boolean hasSubstitution() { 
          return this.substitution != null && !this.substitution.isEmpty();
        }

        /**
         * @param value {@link #substitution} (The type of isotopic substitution present in a single substance.)
         */
        public SubstanceSpecificationStructureIsotopeComponent setSubstitution(CodeableConcept value) { 
          this.substitution = value;
          return this;
        }

        /**
         * @return {@link #halfLife} (Half life - for a non-natural nuclide.)
         */
        public Quantity getHalfLife() { 
          if (this.halfLife == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceSpecificationStructureIsotopeComponent.halfLife");
            else if (Configuration.doAutoCreate())
              this.halfLife = new Quantity(); // cc
          return this.halfLife;
        }

        public boolean hasHalfLife() { 
          return this.halfLife != null && !this.halfLife.isEmpty();
        }

        /**
         * @param value {@link #halfLife} (Half life - for a non-natural nuclide.)
         */
        public SubstanceSpecificationStructureIsotopeComponent setHalfLife(Quantity value) { 
          this.halfLife = value;
          return this;
        }

        /**
         * @return {@link #molecularWeight} (The molecular weight or weight range (for proteins, polymers or nucleic acids).)
         */
        public SubstanceSpecificationStructureIsotopeMolecularWeightComponent getMolecularWeight() { 
          if (this.molecularWeight == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceSpecificationStructureIsotopeComponent.molecularWeight");
            else if (Configuration.doAutoCreate())
              this.molecularWeight = new SubstanceSpecificationStructureIsotopeMolecularWeightComponent(); // cc
          return this.molecularWeight;
        }

        public boolean hasMolecularWeight() { 
          return this.molecularWeight != null && !this.molecularWeight.isEmpty();
        }

        /**
         * @param value {@link #molecularWeight} (The molecular weight or weight range (for proteins, polymers or nucleic acids).)
         */
        public SubstanceSpecificationStructureIsotopeComponent setMolecularWeight(SubstanceSpecificationStructureIsotopeMolecularWeightComponent value) { 
          this.molecularWeight = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("identifier", "Identifier", "Substance identifier for each non-natural or radioisotope.", 0, 1, identifier));
          children.add(new Property("name", "CodeableConcept", "Substance name for each non-natural or radioisotope.", 0, 1, name));
          children.add(new Property("substitution", "CodeableConcept", "The type of isotopic substitution present in a single substance.", 0, 1, substitution));
          children.add(new Property("halfLife", "Quantity", "Half life - for a non-natural nuclide.", 0, 1, halfLife));
          children.add(new Property("molecularWeight", "", "The molecular weight or weight range (for proteins, polymers or nucleic acids).", 0, 1, molecularWeight));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -1618432855: /*identifier*/  return new Property("identifier", "Identifier", "Substance identifier for each non-natural or radioisotope.", 0, 1, identifier);
          case 3373707: /*name*/  return new Property("name", "CodeableConcept", "Substance name for each non-natural or radioisotope.", 0, 1, name);
          case 826147581: /*substitution*/  return new Property("substitution", "CodeableConcept", "The type of isotopic substitution present in a single substance.", 0, 1, substitution);
          case -54292017: /*halfLife*/  return new Property("halfLife", "Quantity", "Half life - for a non-natural nuclide.", 0, 1, halfLife);
          case 635625672: /*molecularWeight*/  return new Property("molecularWeight", "", "The molecular weight or weight range (for proteins, polymers or nucleic acids).", 0, 1, molecularWeight);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : new Base[] {this.identifier}; // Identifier
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // CodeableConcept
        case 826147581: /*substitution*/ return this.substitution == null ? new Base[0] : new Base[] {this.substitution}; // CodeableConcept
        case -54292017: /*halfLife*/ return this.halfLife == null ? new Base[0] : new Base[] {this.halfLife}; // Quantity
        case 635625672: /*molecularWeight*/ return this.molecularWeight == null ? new Base[0] : new Base[] {this.molecularWeight}; // SubstanceSpecificationStructureIsotopeMolecularWeightComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.identifier = castToIdentifier(value); // Identifier
          return value;
        case 3373707: // name
          this.name = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 826147581: // substitution
          this.substitution = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -54292017: // halfLife
          this.halfLife = castToQuantity(value); // Quantity
          return value;
        case 635625672: // molecularWeight
          this.molecularWeight = (SubstanceSpecificationStructureIsotopeMolecularWeightComponent) value; // SubstanceSpecificationStructureIsotopeMolecularWeightComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier")) {
          this.identifier = castToIdentifier(value); // Identifier
        } else if (name.equals("name")) {
          this.name = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("substitution")) {
          this.substitution = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("halfLife")) {
          this.halfLife = castToQuantity(value); // Quantity
        } else if (name.equals("molecularWeight")) {
          this.molecularWeight = (SubstanceSpecificationStructureIsotopeMolecularWeightComponent) value; // SubstanceSpecificationStructureIsotopeMolecularWeightComponent
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return getIdentifier(); 
        case 3373707:  return getName(); 
        case 826147581:  return getSubstitution(); 
        case -54292017:  return getHalfLife(); 
        case 635625672:  return getMolecularWeight(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case 3373707: /*name*/ return new String[] {"CodeableConcept"};
        case 826147581: /*substitution*/ return new String[] {"CodeableConcept"};
        case -54292017: /*halfLife*/ return new String[] {"Quantity"};
        case 635625672: /*molecularWeight*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          this.identifier = new Identifier();
          return this.identifier;
        }
        else if (name.equals("name")) {
          this.name = new CodeableConcept();
          return this.name;
        }
        else if (name.equals("substitution")) {
          this.substitution = new CodeableConcept();
          return this.substitution;
        }
        else if (name.equals("halfLife")) {
          this.halfLife = new Quantity();
          return this.halfLife;
        }
        else if (name.equals("molecularWeight")) {
          this.molecularWeight = new SubstanceSpecificationStructureIsotopeMolecularWeightComponent();
          return this.molecularWeight;
        }
        else
          return super.addChild(name);
      }

      public SubstanceSpecificationStructureIsotopeComponent copy() {
        SubstanceSpecificationStructureIsotopeComponent dst = new SubstanceSpecificationStructureIsotopeComponent();
        copyValues(dst);
        dst.identifier = identifier == null ? null : identifier.copy();
        dst.name = name == null ? null : name.copy();
        dst.substitution = substitution == null ? null : substitution.copy();
        dst.halfLife = halfLife == null ? null : halfLife.copy();
        dst.molecularWeight = molecularWeight == null ? null : molecularWeight.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof SubstanceSpecificationStructureIsotopeComponent))
          return false;
        SubstanceSpecificationStructureIsotopeComponent o = (SubstanceSpecificationStructureIsotopeComponent) other_;
        return compareDeep(identifier, o.identifier, true) && compareDeep(name, o.name, true) && compareDeep(substitution, o.substitution, true)
           && compareDeep(halfLife, o.halfLife, true) && compareDeep(molecularWeight, o.molecularWeight, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof SubstanceSpecificationStructureIsotopeComponent))
          return false;
        SubstanceSpecificationStructureIsotopeComponent o = (SubstanceSpecificationStructureIsotopeComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, name, substitution
          , halfLife, molecularWeight);
      }

  public String fhirType() {
    return "SubstanceSpecification.structure.isotope";

  }

  }

    @Block()
    public static class SubstanceSpecificationStructureIsotopeMolecularWeightComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The method by which the molecular weight was determined.
         */
        @Child(name = "method", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The method by which the molecular weight was determined", formalDefinition="The method by which the molecular weight was determined." )
        protected CodeableConcept method;

        /**
         * Type of molecular weight such as exact, average (also known as. number average), weight average.
         */
        @Child(name = "type", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Type of molecular weight such as exact, average (also known as. number average), weight average", formalDefinition="Type of molecular weight such as exact, average (also known as. number average), weight average." )
        protected CodeableConcept type;

        /**
         * Used to capture quantitative values for a variety of elements. If only limits are given, the arithmetic mean would be the average. If only a single definite value for a given element is given, it would be captured in this field.
         */
        @Child(name = "amount", type = {Quantity.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Used to capture quantitative values for a variety of elements. If only limits are given, the arithmetic mean would be the average. If only a single definite value for a given element is given, it would be captured in this field", formalDefinition="Used to capture quantitative values for a variety of elements. If only limits are given, the arithmetic mean would be the average. If only a single definite value for a given element is given, it would be captured in this field." )
        protected Quantity amount;

        private static final long serialVersionUID = 805939780L;

    /**
     * Constructor
     */
      public SubstanceSpecificationStructureIsotopeMolecularWeightComponent() {
        super();
      }

        /**
         * @return {@link #method} (The method by which the molecular weight was determined.)
         */
        public CodeableConcept getMethod() { 
          if (this.method == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceSpecificationStructureIsotopeMolecularWeightComponent.method");
            else if (Configuration.doAutoCreate())
              this.method = new CodeableConcept(); // cc
          return this.method;
        }

        public boolean hasMethod() { 
          return this.method != null && !this.method.isEmpty();
        }

        /**
         * @param value {@link #method} (The method by which the molecular weight was determined.)
         */
        public SubstanceSpecificationStructureIsotopeMolecularWeightComponent setMethod(CodeableConcept value) { 
          this.method = value;
          return this;
        }

        /**
         * @return {@link #type} (Type of molecular weight such as exact, average (also known as. number average), weight average.)
         */
        public CodeableConcept getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceSpecificationStructureIsotopeMolecularWeightComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new CodeableConcept(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (Type of molecular weight such as exact, average (also known as. number average), weight average.)
         */
        public SubstanceSpecificationStructureIsotopeMolecularWeightComponent setType(CodeableConcept value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #amount} (Used to capture quantitative values for a variety of elements. If only limits are given, the arithmetic mean would be the average. If only a single definite value for a given element is given, it would be captured in this field.)
         */
        public Quantity getAmount() { 
          if (this.amount == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceSpecificationStructureIsotopeMolecularWeightComponent.amount");
            else if (Configuration.doAutoCreate())
              this.amount = new Quantity(); // cc
          return this.amount;
        }

        public boolean hasAmount() { 
          return this.amount != null && !this.amount.isEmpty();
        }

        /**
         * @param value {@link #amount} (Used to capture quantitative values for a variety of elements. If only limits are given, the arithmetic mean would be the average. If only a single definite value for a given element is given, it would be captured in this field.)
         */
        public SubstanceSpecificationStructureIsotopeMolecularWeightComponent setAmount(Quantity value) { 
          this.amount = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("method", "CodeableConcept", "The method by which the molecular weight was determined.", 0, 1, method));
          children.add(new Property("type", "CodeableConcept", "Type of molecular weight such as exact, average (also known as. number average), weight average.", 0, 1, type));
          children.add(new Property("amount", "Quantity", "Used to capture quantitative values for a variety of elements. If only limits are given, the arithmetic mean would be the average. If only a single definite value for a given element is given, it would be captured in this field.", 0, 1, amount));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -1077554975: /*method*/  return new Property("method", "CodeableConcept", "The method by which the molecular weight was determined.", 0, 1, method);
          case 3575610: /*type*/  return new Property("type", "CodeableConcept", "Type of molecular weight such as exact, average (also known as. number average), weight average.", 0, 1, type);
          case -1413853096: /*amount*/  return new Property("amount", "Quantity", "Used to capture quantitative values for a variety of elements. If only limits are given, the arithmetic mean would be the average. If only a single definite value for a given element is given, it would be captured in this field.", 0, 1, amount);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1077554975: /*method*/ return this.method == null ? new Base[0] : new Base[] {this.method}; // CodeableConcept
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case -1413853096: /*amount*/ return this.amount == null ? new Base[0] : new Base[] {this.amount}; // Quantity
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1077554975: // method
          this.method = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1413853096: // amount
          this.amount = castToQuantity(value); // Quantity
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("method")) {
          this.method = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("type")) {
          this.type = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("amount")) {
          this.amount = castToQuantity(value); // Quantity
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1077554975:  return getMethod(); 
        case 3575610:  return getType(); 
        case -1413853096:  return getAmount(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1077554975: /*method*/ return new String[] {"CodeableConcept"};
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case -1413853096: /*amount*/ return new String[] {"Quantity"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("method")) {
          this.method = new CodeableConcept();
          return this.method;
        }
        else if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("amount")) {
          this.amount = new Quantity();
          return this.amount;
        }
        else
          return super.addChild(name);
      }

      public SubstanceSpecificationStructureIsotopeMolecularWeightComponent copy() {
        SubstanceSpecificationStructureIsotopeMolecularWeightComponent dst = new SubstanceSpecificationStructureIsotopeMolecularWeightComponent();
        copyValues(dst);
        dst.method = method == null ? null : method.copy();
        dst.type = type == null ? null : type.copy();
        dst.amount = amount == null ? null : amount.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof SubstanceSpecificationStructureIsotopeMolecularWeightComponent))
          return false;
        SubstanceSpecificationStructureIsotopeMolecularWeightComponent o = (SubstanceSpecificationStructureIsotopeMolecularWeightComponent) other_;
        return compareDeep(method, o.method, true) && compareDeep(type, o.type, true) && compareDeep(amount, o.amount, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof SubstanceSpecificationStructureIsotopeMolecularWeightComponent))
          return false;
        SubstanceSpecificationStructureIsotopeMolecularWeightComponent o = (SubstanceSpecificationStructureIsotopeMolecularWeightComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(method, type, amount);
      }

  public String fhirType() {
    return "SubstanceSpecification.structure.isotope.molecularWeight";

  }

  }

    @Block()
    public static class SubstanceSpecificationStructureRepresentationComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The type of structure (e.g. Full, Partial, Representative).
         */
        @Child(name = "type", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The type of structure (e.g. Full, Partial, Representative)", formalDefinition="The type of structure (e.g. Full, Partial, Representative)." )
        protected CodeableConcept type;

        /**
         * The structural representation as text string in a format e.g. InChI, SMILES, MOLFILE, CDX.
         */
        @Child(name = "representation", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The structural representation as text string in a format e.g. InChI, SMILES, MOLFILE, CDX", formalDefinition="The structural representation as text string in a format e.g. InChI, SMILES, MOLFILE, CDX." )
        protected StringType representation;

        /**
         * An attached file with the structural representation.
         */
        @Child(name = "attachment", type = {Attachment.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="An attached file with the structural representation", formalDefinition="An attached file with the structural representation." )
        protected Attachment attachment;

        private static final long serialVersionUID = 167954495L;

    /**
     * Constructor
     */
      public SubstanceSpecificationStructureRepresentationComponent() {
        super();
      }

        /**
         * @return {@link #type} (The type of structure (e.g. Full, Partial, Representative).)
         */
        public CodeableConcept getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceSpecificationStructureRepresentationComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new CodeableConcept(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (The type of structure (e.g. Full, Partial, Representative).)
         */
        public SubstanceSpecificationStructureRepresentationComponent setType(CodeableConcept value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #representation} (The structural representation as text string in a format e.g. InChI, SMILES, MOLFILE, CDX.). This is the underlying object with id, value and extensions. The accessor "getRepresentation" gives direct access to the value
         */
        public StringType getRepresentationElement() { 
          if (this.representation == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceSpecificationStructureRepresentationComponent.representation");
            else if (Configuration.doAutoCreate())
              this.representation = new StringType(); // bb
          return this.representation;
        }

        public boolean hasRepresentationElement() { 
          return this.representation != null && !this.representation.isEmpty();
        }

        public boolean hasRepresentation() { 
          return this.representation != null && !this.representation.isEmpty();
        }

        /**
         * @param value {@link #representation} (The structural representation as text string in a format e.g. InChI, SMILES, MOLFILE, CDX.). This is the underlying object with id, value and extensions. The accessor "getRepresentation" gives direct access to the value
         */
        public SubstanceSpecificationStructureRepresentationComponent setRepresentationElement(StringType value) { 
          this.representation = value;
          return this;
        }

        /**
         * @return The structural representation as text string in a format e.g. InChI, SMILES, MOLFILE, CDX.
         */
        public String getRepresentation() { 
          return this.representation == null ? null : this.representation.getValue();
        }

        /**
         * @param value The structural representation as text string in a format e.g. InChI, SMILES, MOLFILE, CDX.
         */
        public SubstanceSpecificationStructureRepresentationComponent setRepresentation(String value) { 
          if (Utilities.noString(value))
            this.representation = null;
          else {
            if (this.representation == null)
              this.representation = new StringType();
            this.representation.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #attachment} (An attached file with the structural representation.)
         */
        public Attachment getAttachment() { 
          if (this.attachment == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceSpecificationStructureRepresentationComponent.attachment");
            else if (Configuration.doAutoCreate())
              this.attachment = new Attachment(); // cc
          return this.attachment;
        }

        public boolean hasAttachment() { 
          return this.attachment != null && !this.attachment.isEmpty();
        }

        /**
         * @param value {@link #attachment} (An attached file with the structural representation.)
         */
        public SubstanceSpecificationStructureRepresentationComponent setAttachment(Attachment value) { 
          this.attachment = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("type", "CodeableConcept", "The type of structure (e.g. Full, Partial, Representative).", 0, 1, type));
          children.add(new Property("representation", "string", "The structural representation as text string in a format e.g. InChI, SMILES, MOLFILE, CDX.", 0, 1, representation));
          children.add(new Property("attachment", "Attachment", "An attached file with the structural representation.", 0, 1, attachment));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3575610: /*type*/  return new Property("type", "CodeableConcept", "The type of structure (e.g. Full, Partial, Representative).", 0, 1, type);
          case -671065907: /*representation*/  return new Property("representation", "string", "The structural representation as text string in a format e.g. InChI, SMILES, MOLFILE, CDX.", 0, 1, representation);
          case -1963501277: /*attachment*/  return new Property("attachment", "Attachment", "An attached file with the structural representation.", 0, 1, attachment);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case -671065907: /*representation*/ return this.representation == null ? new Base[0] : new Base[] {this.representation}; // StringType
        case -1963501277: /*attachment*/ return this.attachment == null ? new Base[0] : new Base[] {this.attachment}; // Attachment
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -671065907: // representation
          this.representation = castToString(value); // StringType
          return value;
        case -1963501277: // attachment
          this.attachment = castToAttachment(value); // Attachment
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type")) {
          this.type = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("representation")) {
          this.representation = castToString(value); // StringType
        } else if (name.equals("attachment")) {
          this.attachment = castToAttachment(value); // Attachment
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610:  return getType(); 
        case -671065907:  return getRepresentationElement();
        case -1963501277:  return getAttachment(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case -671065907: /*representation*/ return new String[] {"string"};
        case -1963501277: /*attachment*/ return new String[] {"Attachment"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("representation")) {
          throw new FHIRException("Cannot call addChild on a primitive type SubstanceSpecification.representation");
        }
        else if (name.equals("attachment")) {
          this.attachment = new Attachment();
          return this.attachment;
        }
        else
          return super.addChild(name);
      }

      public SubstanceSpecificationStructureRepresentationComponent copy() {
        SubstanceSpecificationStructureRepresentationComponent dst = new SubstanceSpecificationStructureRepresentationComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.representation = representation == null ? null : representation.copy();
        dst.attachment = attachment == null ? null : attachment.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof SubstanceSpecificationStructureRepresentationComponent))
          return false;
        SubstanceSpecificationStructureRepresentationComponent o = (SubstanceSpecificationStructureRepresentationComponent) other_;
        return compareDeep(type, o.type, true) && compareDeep(representation, o.representation, true) && compareDeep(attachment, o.attachment, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof SubstanceSpecificationStructureRepresentationComponent))
          return false;
        SubstanceSpecificationStructureRepresentationComponent o = (SubstanceSpecificationStructureRepresentationComponent) other_;
        return compareValues(representation, o.representation, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(type, representation, attachment
          );
      }

  public String fhirType() {
    return "SubstanceSpecification.structure.representation";

  }

  }

    @Block()
    public static class SubstanceSpecificationCodeComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The specific code.
         */
        @Child(name = "code", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The specific code", formalDefinition="The specific code." )
        protected CodeableConcept code;

        /**
         * Status of the code assignment.
         */
        @Child(name = "status", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Status of the code assignment", formalDefinition="Status of the code assignment." )
        protected CodeableConcept status;

        /**
         * The date at which the code status is changed as part of the terminology maintenance.
         */
        @Child(name = "statusDate", type = {DateTimeType.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The date at which the code status is changed as part of the terminology maintenance", formalDefinition="The date at which the code status is changed as part of the terminology maintenance." )
        protected DateTimeType statusDate;

        /**
         * Any comment can be provided in this field, if necessary.
         */
        @Child(name = "comment", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Any comment can be provided in this field, if necessary", formalDefinition="Any comment can be provided in this field, if necessary." )
        protected StringType comment;

        /**
         * Supporting literature.
         */
        @Child(name = "source", type = {DocumentReference.class}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Supporting literature", formalDefinition="Supporting literature." )
        protected List<Reference> source;
        /**
         * The actual objects that are the target of the reference (Supporting literature.)
         */
        protected List<DocumentReference> sourceTarget;


        private static final long serialVersionUID = -1629693460L;

    /**
     * Constructor
     */
      public SubstanceSpecificationCodeComponent() {
        super();
      }

        /**
         * @return {@link #code} (The specific code.)
         */
        public CodeableConcept getCode() { 
          if (this.code == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceSpecificationCodeComponent.code");
            else if (Configuration.doAutoCreate())
              this.code = new CodeableConcept(); // cc
          return this.code;
        }

        public boolean hasCode() { 
          return this.code != null && !this.code.isEmpty();
        }

        /**
         * @param value {@link #code} (The specific code.)
         */
        public SubstanceSpecificationCodeComponent setCode(CodeableConcept value) { 
          this.code = value;
          return this;
        }

        /**
         * @return {@link #status} (Status of the code assignment.)
         */
        public CodeableConcept getStatus() { 
          if (this.status == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceSpecificationCodeComponent.status");
            else if (Configuration.doAutoCreate())
              this.status = new CodeableConcept(); // cc
          return this.status;
        }

        public boolean hasStatus() { 
          return this.status != null && !this.status.isEmpty();
        }

        /**
         * @param value {@link #status} (Status of the code assignment.)
         */
        public SubstanceSpecificationCodeComponent setStatus(CodeableConcept value) { 
          this.status = value;
          return this;
        }

        /**
         * @return {@link #statusDate} (The date at which the code status is changed as part of the terminology maintenance.). This is the underlying object with id, value and extensions. The accessor "getStatusDate" gives direct access to the value
         */
        public DateTimeType getStatusDateElement() { 
          if (this.statusDate == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceSpecificationCodeComponent.statusDate");
            else if (Configuration.doAutoCreate())
              this.statusDate = new DateTimeType(); // bb
          return this.statusDate;
        }

        public boolean hasStatusDateElement() { 
          return this.statusDate != null && !this.statusDate.isEmpty();
        }

        public boolean hasStatusDate() { 
          return this.statusDate != null && !this.statusDate.isEmpty();
        }

        /**
         * @param value {@link #statusDate} (The date at which the code status is changed as part of the terminology maintenance.). This is the underlying object with id, value and extensions. The accessor "getStatusDate" gives direct access to the value
         */
        public SubstanceSpecificationCodeComponent setStatusDateElement(DateTimeType value) { 
          this.statusDate = value;
          return this;
        }

        /**
         * @return The date at which the code status is changed as part of the terminology maintenance.
         */
        public Date getStatusDate() { 
          return this.statusDate == null ? null : this.statusDate.getValue();
        }

        /**
         * @param value The date at which the code status is changed as part of the terminology maintenance.
         */
        public SubstanceSpecificationCodeComponent setStatusDate(Date value) { 
          if (value == null)
            this.statusDate = null;
          else {
            if (this.statusDate == null)
              this.statusDate = new DateTimeType();
            this.statusDate.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #comment} (Any comment can be provided in this field, if necessary.). This is the underlying object with id, value and extensions. The accessor "getComment" gives direct access to the value
         */
        public StringType getCommentElement() { 
          if (this.comment == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceSpecificationCodeComponent.comment");
            else if (Configuration.doAutoCreate())
              this.comment = new StringType(); // bb
          return this.comment;
        }

        public boolean hasCommentElement() { 
          return this.comment != null && !this.comment.isEmpty();
        }

        public boolean hasComment() { 
          return this.comment != null && !this.comment.isEmpty();
        }

        /**
         * @param value {@link #comment} (Any comment can be provided in this field, if necessary.). This is the underlying object with id, value and extensions. The accessor "getComment" gives direct access to the value
         */
        public SubstanceSpecificationCodeComponent setCommentElement(StringType value) { 
          this.comment = value;
          return this;
        }

        /**
         * @return Any comment can be provided in this field, if necessary.
         */
        public String getComment() { 
          return this.comment == null ? null : this.comment.getValue();
        }

        /**
         * @param value Any comment can be provided in this field, if necessary.
         */
        public SubstanceSpecificationCodeComponent setComment(String value) { 
          if (Utilities.noString(value))
            this.comment = null;
          else {
            if (this.comment == null)
              this.comment = new StringType();
            this.comment.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #source} (Supporting literature.)
         */
        public List<Reference> getSource() { 
          if (this.source == null)
            this.source = new ArrayList<Reference>();
          return this.source;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public SubstanceSpecificationCodeComponent setSource(List<Reference> theSource) { 
          this.source = theSource;
          return this;
        }

        public boolean hasSource() { 
          if (this.source == null)
            return false;
          for (Reference item : this.source)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public Reference addSource() { //3
          Reference t = new Reference();
          if (this.source == null)
            this.source = new ArrayList<Reference>();
          this.source.add(t);
          return t;
        }

        public SubstanceSpecificationCodeComponent addSource(Reference t) { //3
          if (t == null)
            return this;
          if (this.source == null)
            this.source = new ArrayList<Reference>();
          this.source.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #source}, creating it if it does not already exist
         */
        public Reference getSourceFirstRep() { 
          if (getSource().isEmpty()) {
            addSource();
          }
          return getSource().get(0);
        }

        /**
         * @deprecated Use Reference#setResource(IBaseResource) instead
         */
        @Deprecated
        public List<DocumentReference> getSourceTarget() { 
          if (this.sourceTarget == null)
            this.sourceTarget = new ArrayList<DocumentReference>();
          return this.sourceTarget;
        }

        /**
         * @deprecated Use Reference#setResource(IBaseResource) instead
         */
        @Deprecated
        public DocumentReference addSourceTarget() { 
          DocumentReference r = new DocumentReference();
          if (this.sourceTarget == null)
            this.sourceTarget = new ArrayList<DocumentReference>();
          this.sourceTarget.add(r);
          return r;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("code", "CodeableConcept", "The specific code.", 0, 1, code));
          children.add(new Property("status", "CodeableConcept", "Status of the code assignment.", 0, 1, status));
          children.add(new Property("statusDate", "dateTime", "The date at which the code status is changed as part of the terminology maintenance.", 0, 1, statusDate));
          children.add(new Property("comment", "string", "Any comment can be provided in this field, if necessary.", 0, 1, comment));
          children.add(new Property("source", "Reference(DocumentReference)", "Supporting literature.", 0, java.lang.Integer.MAX_VALUE, source));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3059181: /*code*/  return new Property("code", "CodeableConcept", "The specific code.", 0, 1, code);
          case -892481550: /*status*/  return new Property("status", "CodeableConcept", "Status of the code assignment.", 0, 1, status);
          case 247524032: /*statusDate*/  return new Property("statusDate", "dateTime", "The date at which the code status is changed as part of the terminology maintenance.", 0, 1, statusDate);
          case 950398559: /*comment*/  return new Property("comment", "string", "Any comment can be provided in this field, if necessary.", 0, 1, comment);
          case -896505829: /*source*/  return new Property("source", "Reference(DocumentReference)", "Supporting literature.", 0, java.lang.Integer.MAX_VALUE, source);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3059181: /*code*/ return this.code == null ? new Base[0] : new Base[] {this.code}; // CodeableConcept
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // CodeableConcept
        case 247524032: /*statusDate*/ return this.statusDate == null ? new Base[0] : new Base[] {this.statusDate}; // DateTimeType
        case 950398559: /*comment*/ return this.comment == null ? new Base[0] : new Base[] {this.comment}; // StringType
        case -896505829: /*source*/ return this.source == null ? new Base[0] : this.source.toArray(new Base[this.source.size()]); // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3059181: // code
          this.code = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -892481550: // status
          this.status = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 247524032: // statusDate
          this.statusDate = castToDateTime(value); // DateTimeType
          return value;
        case 950398559: // comment
          this.comment = castToString(value); // StringType
          return value;
        case -896505829: // source
          this.getSource().add(castToReference(value)); // Reference
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("code")) {
          this.code = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("status")) {
          this.status = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("statusDate")) {
          this.statusDate = castToDateTime(value); // DateTimeType
        } else if (name.equals("comment")) {
          this.comment = castToString(value); // StringType
        } else if (name.equals("source")) {
          this.getSource().add(castToReference(value));
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3059181:  return getCode(); 
        case -892481550:  return getStatus(); 
        case 247524032:  return getStatusDateElement();
        case 950398559:  return getCommentElement();
        case -896505829:  return addSource(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3059181: /*code*/ return new String[] {"CodeableConcept"};
        case -892481550: /*status*/ return new String[] {"CodeableConcept"};
        case 247524032: /*statusDate*/ return new String[] {"dateTime"};
        case 950398559: /*comment*/ return new String[] {"string"};
        case -896505829: /*source*/ return new String[] {"Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("code")) {
          this.code = new CodeableConcept();
          return this.code;
        }
        else if (name.equals("status")) {
          this.status = new CodeableConcept();
          return this.status;
        }
        else if (name.equals("statusDate")) {
          throw new FHIRException("Cannot call addChild on a primitive type SubstanceSpecification.statusDate");
        }
        else if (name.equals("comment")) {
          throw new FHIRException("Cannot call addChild on a primitive type SubstanceSpecification.comment");
        }
        else if (name.equals("source")) {
          return addSource();
        }
        else
          return super.addChild(name);
      }

      public SubstanceSpecificationCodeComponent copy() {
        SubstanceSpecificationCodeComponent dst = new SubstanceSpecificationCodeComponent();
        copyValues(dst);
        dst.code = code == null ? null : code.copy();
        dst.status = status == null ? null : status.copy();
        dst.statusDate = statusDate == null ? null : statusDate.copy();
        dst.comment = comment == null ? null : comment.copy();
        if (source != null) {
          dst.source = new ArrayList<Reference>();
          for (Reference i : source)
            dst.source.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof SubstanceSpecificationCodeComponent))
          return false;
        SubstanceSpecificationCodeComponent o = (SubstanceSpecificationCodeComponent) other_;
        return compareDeep(code, o.code, true) && compareDeep(status, o.status, true) && compareDeep(statusDate, o.statusDate, true)
           && compareDeep(comment, o.comment, true) && compareDeep(source, o.source, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof SubstanceSpecificationCodeComponent))
          return false;
        SubstanceSpecificationCodeComponent o = (SubstanceSpecificationCodeComponent) other_;
        return compareValues(statusDate, o.statusDate, true) && compareValues(comment, o.comment, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(code, status, statusDate
          , comment, source);
      }

  public String fhirType() {
    return "SubstanceSpecification.code";

  }

  }

    @Block()
    public static class SubstanceSpecificationNameComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The actual name.
         */
        @Child(name = "name", type = {StringType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The actual name", formalDefinition="The actual name." )
        protected StringType name;

        /**
         * Name type.
         */
        @Child(name = "type", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Name type", formalDefinition="Name type." )
        protected CodeableConcept type;

        /**
         * The status of the name.
         */
        @Child(name = "status", type = {CodeableConcept.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The status of the name", formalDefinition="The status of the name." )
        protected CodeableConcept status;

        /**
         * If this is the preferred name for this substance.
         */
        @Child(name = "preferred", type = {BooleanType.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="If this is the preferred name for this substance", formalDefinition="If this is the preferred name for this substance." )
        protected BooleanType preferred;

        /**
         * Language of the name.
         */
        @Child(name = "language", type = {CodeableConcept.class}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Language of the name", formalDefinition="Language of the name." )
        protected List<CodeableConcept> language;

        /**
         * The use context of this name for example if there is a different name a drug active ingredient as opposed to a food colour additive.
         */
        @Child(name = "domain", type = {CodeableConcept.class}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="The use context of this name for example if there is a different name a drug active ingredient as opposed to a food colour additive", formalDefinition="The use context of this name for example if there is a different name a drug active ingredient as opposed to a food colour additive." )
        protected List<CodeableConcept> domain;

        /**
         * The jurisdiction where this name applies.
         */
        @Child(name = "jurisdiction", type = {CodeableConcept.class}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="The jurisdiction where this name applies", formalDefinition="The jurisdiction where this name applies." )
        protected List<CodeableConcept> jurisdiction;

        /**
         * A synonym of this name.
         */
        @Child(name = "synonym", type = {SubstanceSpecificationNameComponent.class}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="A synonym of this name", formalDefinition="A synonym of this name." )
        protected List<SubstanceSpecificationNameComponent> synonym;

        /**
         * A translation for this name.
         */
        @Child(name = "translation", type = {SubstanceSpecificationNameComponent.class}, order=9, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="A translation for this name", formalDefinition="A translation for this name." )
        protected List<SubstanceSpecificationNameComponent> translation;

        /**
         * Details of the official nature of this name.
         */
        @Child(name = "official", type = {}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Details of the official nature of this name", formalDefinition="Details of the official nature of this name." )
        protected List<SubstanceSpecificationNameOfficialComponent> official;

        /**
         * Supporting literature.
         */
        @Child(name = "source", type = {DocumentReference.class}, order=11, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Supporting literature", formalDefinition="Supporting literature." )
        protected List<Reference> source;
        /**
         * The actual objects that are the target of the reference (Supporting literature.)
         */
        protected List<DocumentReference> sourceTarget;


        private static final long serialVersionUID = 1547107852L;

    /**
     * Constructor
     */
      public SubstanceSpecificationNameComponent() {
        super();
      }

    /**
     * Constructor
     */
      public SubstanceSpecificationNameComponent(StringType name) {
        super();
        this.name = name;
      }

        /**
         * @return {@link #name} (The actual name.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StringType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceSpecificationNameComponent.name");
            else if (Configuration.doAutoCreate())
              this.name = new StringType(); // bb
          return this.name;
        }

        public boolean hasNameElement() { 
          return this.name != null && !this.name.isEmpty();
        }

        public boolean hasName() { 
          return this.name != null && !this.name.isEmpty();
        }

        /**
         * @param value {@link #name} (The actual name.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public SubstanceSpecificationNameComponent setNameElement(StringType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return The actual name.
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value The actual name.
         */
        public SubstanceSpecificationNameComponent setName(String value) { 
            if (this.name == null)
              this.name = new StringType();
            this.name.setValue(value);
          return this;
        }

        /**
         * @return {@link #type} (Name type.)
         */
        public CodeableConcept getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceSpecificationNameComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new CodeableConcept(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (Name type.)
         */
        public SubstanceSpecificationNameComponent setType(CodeableConcept value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #status} (The status of the name.)
         */
        public CodeableConcept getStatus() { 
          if (this.status == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceSpecificationNameComponent.status");
            else if (Configuration.doAutoCreate())
              this.status = new CodeableConcept(); // cc
          return this.status;
        }

        public boolean hasStatus() { 
          return this.status != null && !this.status.isEmpty();
        }

        /**
         * @param value {@link #status} (The status of the name.)
         */
        public SubstanceSpecificationNameComponent setStatus(CodeableConcept value) { 
          this.status = value;
          return this;
        }

        /**
         * @return {@link #preferred} (If this is the preferred name for this substance.). This is the underlying object with id, value and extensions. The accessor "getPreferred" gives direct access to the value
         */
        public BooleanType getPreferredElement() { 
          if (this.preferred == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceSpecificationNameComponent.preferred");
            else if (Configuration.doAutoCreate())
              this.preferred = new BooleanType(); // bb
          return this.preferred;
        }

        public boolean hasPreferredElement() { 
          return this.preferred != null && !this.preferred.isEmpty();
        }

        public boolean hasPreferred() { 
          return this.preferred != null && !this.preferred.isEmpty();
        }

        /**
         * @param value {@link #preferred} (If this is the preferred name for this substance.). This is the underlying object with id, value and extensions. The accessor "getPreferred" gives direct access to the value
         */
        public SubstanceSpecificationNameComponent setPreferredElement(BooleanType value) { 
          this.preferred = value;
          return this;
        }

        /**
         * @return If this is the preferred name for this substance.
         */
        public boolean getPreferred() { 
          return this.preferred == null || this.preferred.isEmpty() ? false : this.preferred.getValue();
        }

        /**
         * @param value If this is the preferred name for this substance.
         */
        public SubstanceSpecificationNameComponent setPreferred(boolean value) { 
            if (this.preferred == null)
              this.preferred = new BooleanType();
            this.preferred.setValue(value);
          return this;
        }

        /**
         * @return {@link #language} (Language of the name.)
         */
        public List<CodeableConcept> getLanguage() { 
          if (this.language == null)
            this.language = new ArrayList<CodeableConcept>();
          return this.language;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public SubstanceSpecificationNameComponent setLanguage(List<CodeableConcept> theLanguage) { 
          this.language = theLanguage;
          return this;
        }

        public boolean hasLanguage() { 
          if (this.language == null)
            return false;
          for (CodeableConcept item : this.language)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public CodeableConcept addLanguage() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.language == null)
            this.language = new ArrayList<CodeableConcept>();
          this.language.add(t);
          return t;
        }

        public SubstanceSpecificationNameComponent addLanguage(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.language == null)
            this.language = new ArrayList<CodeableConcept>();
          this.language.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #language}, creating it if it does not already exist
         */
        public CodeableConcept getLanguageFirstRep() { 
          if (getLanguage().isEmpty()) {
            addLanguage();
          }
          return getLanguage().get(0);
        }

        /**
         * @return {@link #domain} (The use context of this name for example if there is a different name a drug active ingredient as opposed to a food colour additive.)
         */
        public List<CodeableConcept> getDomain() { 
          if (this.domain == null)
            this.domain = new ArrayList<CodeableConcept>();
          return this.domain;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public SubstanceSpecificationNameComponent setDomain(List<CodeableConcept> theDomain) { 
          this.domain = theDomain;
          return this;
        }

        public boolean hasDomain() { 
          if (this.domain == null)
            return false;
          for (CodeableConcept item : this.domain)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public CodeableConcept addDomain() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.domain == null)
            this.domain = new ArrayList<CodeableConcept>();
          this.domain.add(t);
          return t;
        }

        public SubstanceSpecificationNameComponent addDomain(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.domain == null)
            this.domain = new ArrayList<CodeableConcept>();
          this.domain.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #domain}, creating it if it does not already exist
         */
        public CodeableConcept getDomainFirstRep() { 
          if (getDomain().isEmpty()) {
            addDomain();
          }
          return getDomain().get(0);
        }

        /**
         * @return {@link #jurisdiction} (The jurisdiction where this name applies.)
         */
        public List<CodeableConcept> getJurisdiction() { 
          if (this.jurisdiction == null)
            this.jurisdiction = new ArrayList<CodeableConcept>();
          return this.jurisdiction;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public SubstanceSpecificationNameComponent setJurisdiction(List<CodeableConcept> theJurisdiction) { 
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

        public SubstanceSpecificationNameComponent addJurisdiction(CodeableConcept t) { //3
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
         * @return {@link #synonym} (A synonym of this name.)
         */
        public List<SubstanceSpecificationNameComponent> getSynonym() { 
          if (this.synonym == null)
            this.synonym = new ArrayList<SubstanceSpecificationNameComponent>();
          return this.synonym;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public SubstanceSpecificationNameComponent setSynonym(List<SubstanceSpecificationNameComponent> theSynonym) { 
          this.synonym = theSynonym;
          return this;
        }

        public boolean hasSynonym() { 
          if (this.synonym == null)
            return false;
          for (SubstanceSpecificationNameComponent item : this.synonym)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public SubstanceSpecificationNameComponent addSynonym() { //3
          SubstanceSpecificationNameComponent t = new SubstanceSpecificationNameComponent();
          if (this.synonym == null)
            this.synonym = new ArrayList<SubstanceSpecificationNameComponent>();
          this.synonym.add(t);
          return t;
        }

        public SubstanceSpecificationNameComponent addSynonym(SubstanceSpecificationNameComponent t) { //3
          if (t == null)
            return this;
          if (this.synonym == null)
            this.synonym = new ArrayList<SubstanceSpecificationNameComponent>();
          this.synonym.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #synonym}, creating it if it does not already exist
         */
        public SubstanceSpecificationNameComponent getSynonymFirstRep() { 
          if (getSynonym().isEmpty()) {
            addSynonym();
          }
          return getSynonym().get(0);
        }

        /**
         * @return {@link #translation} (A translation for this name.)
         */
        public List<SubstanceSpecificationNameComponent> getTranslation() { 
          if (this.translation == null)
            this.translation = new ArrayList<SubstanceSpecificationNameComponent>();
          return this.translation;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public SubstanceSpecificationNameComponent setTranslation(List<SubstanceSpecificationNameComponent> theTranslation) { 
          this.translation = theTranslation;
          return this;
        }

        public boolean hasTranslation() { 
          if (this.translation == null)
            return false;
          for (SubstanceSpecificationNameComponent item : this.translation)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public SubstanceSpecificationNameComponent addTranslation() { //3
          SubstanceSpecificationNameComponent t = new SubstanceSpecificationNameComponent();
          if (this.translation == null)
            this.translation = new ArrayList<SubstanceSpecificationNameComponent>();
          this.translation.add(t);
          return t;
        }

        public SubstanceSpecificationNameComponent addTranslation(SubstanceSpecificationNameComponent t) { //3
          if (t == null)
            return this;
          if (this.translation == null)
            this.translation = new ArrayList<SubstanceSpecificationNameComponent>();
          this.translation.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #translation}, creating it if it does not already exist
         */
        public SubstanceSpecificationNameComponent getTranslationFirstRep() { 
          if (getTranslation().isEmpty()) {
            addTranslation();
          }
          return getTranslation().get(0);
        }

        /**
         * @return {@link #official} (Details of the official nature of this name.)
         */
        public List<SubstanceSpecificationNameOfficialComponent> getOfficial() { 
          if (this.official == null)
            this.official = new ArrayList<SubstanceSpecificationNameOfficialComponent>();
          return this.official;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public SubstanceSpecificationNameComponent setOfficial(List<SubstanceSpecificationNameOfficialComponent> theOfficial) { 
          this.official = theOfficial;
          return this;
        }

        public boolean hasOfficial() { 
          if (this.official == null)
            return false;
          for (SubstanceSpecificationNameOfficialComponent item : this.official)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public SubstanceSpecificationNameOfficialComponent addOfficial() { //3
          SubstanceSpecificationNameOfficialComponent t = new SubstanceSpecificationNameOfficialComponent();
          if (this.official == null)
            this.official = new ArrayList<SubstanceSpecificationNameOfficialComponent>();
          this.official.add(t);
          return t;
        }

        public SubstanceSpecificationNameComponent addOfficial(SubstanceSpecificationNameOfficialComponent t) { //3
          if (t == null)
            return this;
          if (this.official == null)
            this.official = new ArrayList<SubstanceSpecificationNameOfficialComponent>();
          this.official.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #official}, creating it if it does not already exist
         */
        public SubstanceSpecificationNameOfficialComponent getOfficialFirstRep() { 
          if (getOfficial().isEmpty()) {
            addOfficial();
          }
          return getOfficial().get(0);
        }

        /**
         * @return {@link #source} (Supporting literature.)
         */
        public List<Reference> getSource() { 
          if (this.source == null)
            this.source = new ArrayList<Reference>();
          return this.source;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public SubstanceSpecificationNameComponent setSource(List<Reference> theSource) { 
          this.source = theSource;
          return this;
        }

        public boolean hasSource() { 
          if (this.source == null)
            return false;
          for (Reference item : this.source)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public Reference addSource() { //3
          Reference t = new Reference();
          if (this.source == null)
            this.source = new ArrayList<Reference>();
          this.source.add(t);
          return t;
        }

        public SubstanceSpecificationNameComponent addSource(Reference t) { //3
          if (t == null)
            return this;
          if (this.source == null)
            this.source = new ArrayList<Reference>();
          this.source.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #source}, creating it if it does not already exist
         */
        public Reference getSourceFirstRep() { 
          if (getSource().isEmpty()) {
            addSource();
          }
          return getSource().get(0);
        }

        /**
         * @deprecated Use Reference#setResource(IBaseResource) instead
         */
        @Deprecated
        public List<DocumentReference> getSourceTarget() { 
          if (this.sourceTarget == null)
            this.sourceTarget = new ArrayList<DocumentReference>();
          return this.sourceTarget;
        }

        /**
         * @deprecated Use Reference#setResource(IBaseResource) instead
         */
        @Deprecated
        public DocumentReference addSourceTarget() { 
          DocumentReference r = new DocumentReference();
          if (this.sourceTarget == null)
            this.sourceTarget = new ArrayList<DocumentReference>();
          this.sourceTarget.add(r);
          return r;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("name", "string", "The actual name.", 0, 1, name));
          children.add(new Property("type", "CodeableConcept", "Name type.", 0, 1, type));
          children.add(new Property("status", "CodeableConcept", "The status of the name.", 0, 1, status));
          children.add(new Property("preferred", "boolean", "If this is the preferred name for this substance.", 0, 1, preferred));
          children.add(new Property("language", "CodeableConcept", "Language of the name.", 0, java.lang.Integer.MAX_VALUE, language));
          children.add(new Property("domain", "CodeableConcept", "The use context of this name for example if there is a different name a drug active ingredient as opposed to a food colour additive.", 0, java.lang.Integer.MAX_VALUE, domain));
          children.add(new Property("jurisdiction", "CodeableConcept", "The jurisdiction where this name applies.", 0, java.lang.Integer.MAX_VALUE, jurisdiction));
          children.add(new Property("synonym", "@SubstanceSpecification.name", "A synonym of this name.", 0, java.lang.Integer.MAX_VALUE, synonym));
          children.add(new Property("translation", "@SubstanceSpecification.name", "A translation for this name.", 0, java.lang.Integer.MAX_VALUE, translation));
          children.add(new Property("official", "", "Details of the official nature of this name.", 0, java.lang.Integer.MAX_VALUE, official));
          children.add(new Property("source", "Reference(DocumentReference)", "Supporting literature.", 0, java.lang.Integer.MAX_VALUE, source));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3373707: /*name*/  return new Property("name", "string", "The actual name.", 0, 1, name);
          case 3575610: /*type*/  return new Property("type", "CodeableConcept", "Name type.", 0, 1, type);
          case -892481550: /*status*/  return new Property("status", "CodeableConcept", "The status of the name.", 0, 1, status);
          case -1294005119: /*preferred*/  return new Property("preferred", "boolean", "If this is the preferred name for this substance.", 0, 1, preferred);
          case -1613589672: /*language*/  return new Property("language", "CodeableConcept", "Language of the name.", 0, java.lang.Integer.MAX_VALUE, language);
          case -1326197564: /*domain*/  return new Property("domain", "CodeableConcept", "The use context of this name for example if there is a different name a drug active ingredient as opposed to a food colour additive.", 0, java.lang.Integer.MAX_VALUE, domain);
          case -507075711: /*jurisdiction*/  return new Property("jurisdiction", "CodeableConcept", "The jurisdiction where this name applies.", 0, java.lang.Integer.MAX_VALUE, jurisdiction);
          case -1742128133: /*synonym*/  return new Property("synonym", "@SubstanceSpecification.name", "A synonym of this name.", 0, java.lang.Integer.MAX_VALUE, synonym);
          case -1840647503: /*translation*/  return new Property("translation", "@SubstanceSpecification.name", "A translation for this name.", 0, java.lang.Integer.MAX_VALUE, translation);
          case -765289749: /*official*/  return new Property("official", "", "Details of the official nature of this name.", 0, java.lang.Integer.MAX_VALUE, official);
          case -896505829: /*source*/  return new Property("source", "Reference(DocumentReference)", "Supporting literature.", 0, java.lang.Integer.MAX_VALUE, source);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // CodeableConcept
        case -1294005119: /*preferred*/ return this.preferred == null ? new Base[0] : new Base[] {this.preferred}; // BooleanType
        case -1613589672: /*language*/ return this.language == null ? new Base[0] : this.language.toArray(new Base[this.language.size()]); // CodeableConcept
        case -1326197564: /*domain*/ return this.domain == null ? new Base[0] : this.domain.toArray(new Base[this.domain.size()]); // CodeableConcept
        case -507075711: /*jurisdiction*/ return this.jurisdiction == null ? new Base[0] : this.jurisdiction.toArray(new Base[this.jurisdiction.size()]); // CodeableConcept
        case -1742128133: /*synonym*/ return this.synonym == null ? new Base[0] : this.synonym.toArray(new Base[this.synonym.size()]); // SubstanceSpecificationNameComponent
        case -1840647503: /*translation*/ return this.translation == null ? new Base[0] : this.translation.toArray(new Base[this.translation.size()]); // SubstanceSpecificationNameComponent
        case -765289749: /*official*/ return this.official == null ? new Base[0] : this.official.toArray(new Base[this.official.size()]); // SubstanceSpecificationNameOfficialComponent
        case -896505829: /*source*/ return this.source == null ? new Base[0] : this.source.toArray(new Base[this.source.size()]); // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3373707: // name
          this.name = castToString(value); // StringType
          return value;
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -892481550: // status
          this.status = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1294005119: // preferred
          this.preferred = castToBoolean(value); // BooleanType
          return value;
        case -1613589672: // language
          this.getLanguage().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -1326197564: // domain
          this.getDomain().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -507075711: // jurisdiction
          this.getJurisdiction().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -1742128133: // synonym
          this.getSynonym().add((SubstanceSpecificationNameComponent) value); // SubstanceSpecificationNameComponent
          return value;
        case -1840647503: // translation
          this.getTranslation().add((SubstanceSpecificationNameComponent) value); // SubstanceSpecificationNameComponent
          return value;
        case -765289749: // official
          this.getOfficial().add((SubstanceSpecificationNameOfficialComponent) value); // SubstanceSpecificationNameOfficialComponent
          return value;
        case -896505829: // source
          this.getSource().add(castToReference(value)); // Reference
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("name")) {
          this.name = castToString(value); // StringType
        } else if (name.equals("type")) {
          this.type = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("status")) {
          this.status = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("preferred")) {
          this.preferred = castToBoolean(value); // BooleanType
        } else if (name.equals("language")) {
          this.getLanguage().add(castToCodeableConcept(value));
        } else if (name.equals("domain")) {
          this.getDomain().add(castToCodeableConcept(value));
        } else if (name.equals("jurisdiction")) {
          this.getJurisdiction().add(castToCodeableConcept(value));
        } else if (name.equals("synonym")) {
          this.getSynonym().add((SubstanceSpecificationNameComponent) value);
        } else if (name.equals("translation")) {
          this.getTranslation().add((SubstanceSpecificationNameComponent) value);
        } else if (name.equals("official")) {
          this.getOfficial().add((SubstanceSpecificationNameOfficialComponent) value);
        } else if (name.equals("source")) {
          this.getSource().add(castToReference(value));
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3373707:  return getNameElement();
        case 3575610:  return getType(); 
        case -892481550:  return getStatus(); 
        case -1294005119:  return getPreferredElement();
        case -1613589672:  return addLanguage(); 
        case -1326197564:  return addDomain(); 
        case -507075711:  return addJurisdiction(); 
        case -1742128133:  return addSynonym(); 
        case -1840647503:  return addTranslation(); 
        case -765289749:  return addOfficial(); 
        case -896505829:  return addSource(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3373707: /*name*/ return new String[] {"string"};
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case -892481550: /*status*/ return new String[] {"CodeableConcept"};
        case -1294005119: /*preferred*/ return new String[] {"boolean"};
        case -1613589672: /*language*/ return new String[] {"CodeableConcept"};
        case -1326197564: /*domain*/ return new String[] {"CodeableConcept"};
        case -507075711: /*jurisdiction*/ return new String[] {"CodeableConcept"};
        case -1742128133: /*synonym*/ return new String[] {"@SubstanceSpecification.name"};
        case -1840647503: /*translation*/ return new String[] {"@SubstanceSpecification.name"};
        case -765289749: /*official*/ return new String[] {};
        case -896505829: /*source*/ return new String[] {"Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type SubstanceSpecification.name");
        }
        else if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("status")) {
          this.status = new CodeableConcept();
          return this.status;
        }
        else if (name.equals("preferred")) {
          throw new FHIRException("Cannot call addChild on a primitive type SubstanceSpecification.preferred");
        }
        else if (name.equals("language")) {
          return addLanguage();
        }
        else if (name.equals("domain")) {
          return addDomain();
        }
        else if (name.equals("jurisdiction")) {
          return addJurisdiction();
        }
        else if (name.equals("synonym")) {
          return addSynonym();
        }
        else if (name.equals("translation")) {
          return addTranslation();
        }
        else if (name.equals("official")) {
          return addOfficial();
        }
        else if (name.equals("source")) {
          return addSource();
        }
        else
          return super.addChild(name);
      }

      public SubstanceSpecificationNameComponent copy() {
        SubstanceSpecificationNameComponent dst = new SubstanceSpecificationNameComponent();
        copyValues(dst);
        dst.name = name == null ? null : name.copy();
        dst.type = type == null ? null : type.copy();
        dst.status = status == null ? null : status.copy();
        dst.preferred = preferred == null ? null : preferred.copy();
        if (language != null) {
          dst.language = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : language)
            dst.language.add(i.copy());
        };
        if (domain != null) {
          dst.domain = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : domain)
            dst.domain.add(i.copy());
        };
        if (jurisdiction != null) {
          dst.jurisdiction = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : jurisdiction)
            dst.jurisdiction.add(i.copy());
        };
        if (synonym != null) {
          dst.synonym = new ArrayList<SubstanceSpecificationNameComponent>();
          for (SubstanceSpecificationNameComponent i : synonym)
            dst.synonym.add(i.copy());
        };
        if (translation != null) {
          dst.translation = new ArrayList<SubstanceSpecificationNameComponent>();
          for (SubstanceSpecificationNameComponent i : translation)
            dst.translation.add(i.copy());
        };
        if (official != null) {
          dst.official = new ArrayList<SubstanceSpecificationNameOfficialComponent>();
          for (SubstanceSpecificationNameOfficialComponent i : official)
            dst.official.add(i.copy());
        };
        if (source != null) {
          dst.source = new ArrayList<Reference>();
          for (Reference i : source)
            dst.source.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof SubstanceSpecificationNameComponent))
          return false;
        SubstanceSpecificationNameComponent o = (SubstanceSpecificationNameComponent) other_;
        return compareDeep(name, o.name, true) && compareDeep(type, o.type, true) && compareDeep(status, o.status, true)
           && compareDeep(preferred, o.preferred, true) && compareDeep(language, o.language, true) && compareDeep(domain, o.domain, true)
           && compareDeep(jurisdiction, o.jurisdiction, true) && compareDeep(synonym, o.synonym, true) && compareDeep(translation, o.translation, true)
           && compareDeep(official, o.official, true) && compareDeep(source, o.source, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof SubstanceSpecificationNameComponent))
          return false;
        SubstanceSpecificationNameComponent o = (SubstanceSpecificationNameComponent) other_;
        return compareValues(name, o.name, true) && compareValues(preferred, o.preferred, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(name, type, status, preferred
          , language, domain, jurisdiction, synonym, translation, official, source);
      }

  public String fhirType() {
    return "SubstanceSpecification.name";

  }

  }

    @Block()
    public static class SubstanceSpecificationNameOfficialComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Which authority uses this official name.
         */
        @Child(name = "authority", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Which authority uses this official name", formalDefinition="Which authority uses this official name." )
        protected CodeableConcept authority;

        /**
         * The status of the official name.
         */
        @Child(name = "status", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The status of the official name", formalDefinition="The status of the official name." )
        protected CodeableConcept status;

        /**
         * Date of official name change.
         */
        @Child(name = "date", type = {DateTimeType.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Date of official name change", formalDefinition="Date of official name change." )
        protected DateTimeType date;

        private static final long serialVersionUID = -2040011008L;

    /**
     * Constructor
     */
      public SubstanceSpecificationNameOfficialComponent() {
        super();
      }

        /**
         * @return {@link #authority} (Which authority uses this official name.)
         */
        public CodeableConcept getAuthority() { 
          if (this.authority == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceSpecificationNameOfficialComponent.authority");
            else if (Configuration.doAutoCreate())
              this.authority = new CodeableConcept(); // cc
          return this.authority;
        }

        public boolean hasAuthority() { 
          return this.authority != null && !this.authority.isEmpty();
        }

        /**
         * @param value {@link #authority} (Which authority uses this official name.)
         */
        public SubstanceSpecificationNameOfficialComponent setAuthority(CodeableConcept value) { 
          this.authority = value;
          return this;
        }

        /**
         * @return {@link #status} (The status of the official name.)
         */
        public CodeableConcept getStatus() { 
          if (this.status == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceSpecificationNameOfficialComponent.status");
            else if (Configuration.doAutoCreate())
              this.status = new CodeableConcept(); // cc
          return this.status;
        }

        public boolean hasStatus() { 
          return this.status != null && !this.status.isEmpty();
        }

        /**
         * @param value {@link #status} (The status of the official name.)
         */
        public SubstanceSpecificationNameOfficialComponent setStatus(CodeableConcept value) { 
          this.status = value;
          return this;
        }

        /**
         * @return {@link #date} (Date of official name change.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
         */
        public DateTimeType getDateElement() { 
          if (this.date == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceSpecificationNameOfficialComponent.date");
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
         * @param value {@link #date} (Date of official name change.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
         */
        public SubstanceSpecificationNameOfficialComponent setDateElement(DateTimeType value) { 
          this.date = value;
          return this;
        }

        /**
         * @return Date of official name change.
         */
        public Date getDate() { 
          return this.date == null ? null : this.date.getValue();
        }

        /**
         * @param value Date of official name change.
         */
        public SubstanceSpecificationNameOfficialComponent setDate(Date value) { 
          if (value == null)
            this.date = null;
          else {
            if (this.date == null)
              this.date = new DateTimeType();
            this.date.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("authority", "CodeableConcept", "Which authority uses this official name.", 0, 1, authority));
          children.add(new Property("status", "CodeableConcept", "The status of the official name.", 0, 1, status));
          children.add(new Property("date", "dateTime", "Date of official name change.", 0, 1, date));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 1475610435: /*authority*/  return new Property("authority", "CodeableConcept", "Which authority uses this official name.", 0, 1, authority);
          case -892481550: /*status*/  return new Property("status", "CodeableConcept", "The status of the official name.", 0, 1, status);
          case 3076014: /*date*/  return new Property("date", "dateTime", "Date of official name change.", 0, 1, date);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 1475610435: /*authority*/ return this.authority == null ? new Base[0] : new Base[] {this.authority}; // CodeableConcept
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // CodeableConcept
        case 3076014: /*date*/ return this.date == null ? new Base[0] : new Base[] {this.date}; // DateTimeType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 1475610435: // authority
          this.authority = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -892481550: // status
          this.status = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 3076014: // date
          this.date = castToDateTime(value); // DateTimeType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("authority")) {
          this.authority = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("status")) {
          this.status = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("date")) {
          this.date = castToDateTime(value); // DateTimeType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1475610435:  return getAuthority(); 
        case -892481550:  return getStatus(); 
        case 3076014:  return getDateElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1475610435: /*authority*/ return new String[] {"CodeableConcept"};
        case -892481550: /*status*/ return new String[] {"CodeableConcept"};
        case 3076014: /*date*/ return new String[] {"dateTime"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("authority")) {
          this.authority = new CodeableConcept();
          return this.authority;
        }
        else if (name.equals("status")) {
          this.status = new CodeableConcept();
          return this.status;
        }
        else if (name.equals("date")) {
          throw new FHIRException("Cannot call addChild on a primitive type SubstanceSpecification.date");
        }
        else
          return super.addChild(name);
      }

      public SubstanceSpecificationNameOfficialComponent copy() {
        SubstanceSpecificationNameOfficialComponent dst = new SubstanceSpecificationNameOfficialComponent();
        copyValues(dst);
        dst.authority = authority == null ? null : authority.copy();
        dst.status = status == null ? null : status.copy();
        dst.date = date == null ? null : date.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof SubstanceSpecificationNameOfficialComponent))
          return false;
        SubstanceSpecificationNameOfficialComponent o = (SubstanceSpecificationNameOfficialComponent) other_;
        return compareDeep(authority, o.authority, true) && compareDeep(status, o.status, true) && compareDeep(date, o.date, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof SubstanceSpecificationNameOfficialComponent))
          return false;
        SubstanceSpecificationNameOfficialComponent o = (SubstanceSpecificationNameOfficialComponent) other_;
        return compareValues(date, o.date, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(authority, status, date
          );
      }

  public String fhirType() {
    return "SubstanceSpecification.name.official";

  }

  }

    @Block()
    public static class SubstanceSpecificationRelationshipComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A pointer to another substance, as a resource or just a representational code.
         */
        @Child(name = "substance", type = {SubstanceSpecification.class, CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="A pointer to another substance, as a resource or just a representational code", formalDefinition="A pointer to another substance, as a resource or just a representational code." )
        protected Type substance;

        /**
         * For example "salt to parent", "active moiety", "starting material".
         */
        @Child(name = "relationship", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="For example \"salt to parent\", \"active moiety\", \"starting material\"", formalDefinition="For example \"salt to parent\", \"active moiety\", \"starting material\"." )
        protected CodeableConcept relationship;

        /**
         * For example where an enzyme strongly bonds with a particular substance, this is a defining relationship for that enzyme, out of several possible substance relationships.
         */
        @Child(name = "isDefining", type = {BooleanType.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="For example where an enzyme strongly bonds with a particular substance, this is a defining relationship for that enzyme, out of several possible substance relationships", formalDefinition="For example where an enzyme strongly bonds with a particular substance, this is a defining relationship for that enzyme, out of several possible substance relationships." )
        protected BooleanType isDefining;

        /**
         * A numeric factor for the relationship, for instance to express that the salt of a substance has some percentage of the active substance in relation to some other.
         */
        @Child(name = "amount", type = {Quantity.class, Range.class, Ratio.class, StringType.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="A numeric factor for the relationship, for instance to express that the salt of a substance has some percentage of the active substance in relation to some other", formalDefinition="A numeric factor for the relationship, for instance to express that the salt of a substance has some percentage of the active substance in relation to some other." )
        protected Type amount;

        /**
         * For use when the numeric.
         */
        @Child(name = "amountRatioLowLimit", type = {Ratio.class}, order=5, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="For use when the numeric", formalDefinition="For use when the numeric." )
        protected Ratio amountRatioLowLimit;

        /**
         * An operator for the amount, for example "average", "approximately", "less than".
         */
        @Child(name = "amountType", type = {CodeableConcept.class}, order=6, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="An operator for the amount, for example \"average\", \"approximately\", \"less than\"", formalDefinition="An operator for the amount, for example \"average\", \"approximately\", \"less than\"." )
        protected CodeableConcept amountType;

        /**
         * Supporting literature.
         */
        @Child(name = "source", type = {DocumentReference.class}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Supporting literature", formalDefinition="Supporting literature." )
        protected List<Reference> source;
        /**
         * The actual objects that are the target of the reference (Supporting literature.)
         */
        protected List<DocumentReference> sourceTarget;


        private static final long serialVersionUID = -1277419269L;

    /**
     * Constructor
     */
      public SubstanceSpecificationRelationshipComponent() {
        super();
      }

        /**
         * @return {@link #substance} (A pointer to another substance, as a resource or just a representational code.)
         */
        public Type getSubstance() { 
          return this.substance;
        }

        /**
         * @return {@link #substance} (A pointer to another substance, as a resource or just a representational code.)
         */
        public Reference getSubstanceReference() throws FHIRException { 
          if (this.substance == null)
            this.substance = new Reference();
          if (!(this.substance instanceof Reference))
            throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.substance.getClass().getName()+" was encountered");
          return (Reference) this.substance;
        }

        public boolean hasSubstanceReference() { 
          return this != null && this.substance instanceof Reference;
        }

        /**
         * @return {@link #substance} (A pointer to another substance, as a resource or just a representational code.)
         */
        public CodeableConcept getSubstanceCodeableConcept() throws FHIRException { 
          if (this.substance == null)
            this.substance = new CodeableConcept();
          if (!(this.substance instanceof CodeableConcept))
            throw new FHIRException("Type mismatch: the type CodeableConcept was expected, but "+this.substance.getClass().getName()+" was encountered");
          return (CodeableConcept) this.substance;
        }

        public boolean hasSubstanceCodeableConcept() { 
          return this != null && this.substance instanceof CodeableConcept;
        }

        public boolean hasSubstance() { 
          return this.substance != null && !this.substance.isEmpty();
        }

        /**
         * @param value {@link #substance} (A pointer to another substance, as a resource or just a representational code.)
         */
        public SubstanceSpecificationRelationshipComponent setSubstance(Type value) { 
          if (value != null && !(value instanceof Reference || value instanceof CodeableConcept))
            throw new Error("Not the right type for SubstanceSpecification.relationship.substance[x]: "+value.fhirType());
          this.substance = value;
          return this;
        }

        /**
         * @return {@link #relationship} (For example "salt to parent", "active moiety", "starting material".)
         */
        public CodeableConcept getRelationship() { 
          if (this.relationship == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceSpecificationRelationshipComponent.relationship");
            else if (Configuration.doAutoCreate())
              this.relationship = new CodeableConcept(); // cc
          return this.relationship;
        }

        public boolean hasRelationship() { 
          return this.relationship != null && !this.relationship.isEmpty();
        }

        /**
         * @param value {@link #relationship} (For example "salt to parent", "active moiety", "starting material".)
         */
        public SubstanceSpecificationRelationshipComponent setRelationship(CodeableConcept value) { 
          this.relationship = value;
          return this;
        }

        /**
         * @return {@link #isDefining} (For example where an enzyme strongly bonds with a particular substance, this is a defining relationship for that enzyme, out of several possible substance relationships.). This is the underlying object with id, value and extensions. The accessor "getIsDefining" gives direct access to the value
         */
        public BooleanType getIsDefiningElement() { 
          if (this.isDefining == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceSpecificationRelationshipComponent.isDefining");
            else if (Configuration.doAutoCreate())
              this.isDefining = new BooleanType(); // bb
          return this.isDefining;
        }

        public boolean hasIsDefiningElement() { 
          return this.isDefining != null && !this.isDefining.isEmpty();
        }

        public boolean hasIsDefining() { 
          return this.isDefining != null && !this.isDefining.isEmpty();
        }

        /**
         * @param value {@link #isDefining} (For example where an enzyme strongly bonds with a particular substance, this is a defining relationship for that enzyme, out of several possible substance relationships.). This is the underlying object with id, value and extensions. The accessor "getIsDefining" gives direct access to the value
         */
        public SubstanceSpecificationRelationshipComponent setIsDefiningElement(BooleanType value) { 
          this.isDefining = value;
          return this;
        }

        /**
         * @return For example where an enzyme strongly bonds with a particular substance, this is a defining relationship for that enzyme, out of several possible substance relationships.
         */
        public boolean getIsDefining() { 
          return this.isDefining == null || this.isDefining.isEmpty() ? false : this.isDefining.getValue();
        }

        /**
         * @param value For example where an enzyme strongly bonds with a particular substance, this is a defining relationship for that enzyme, out of several possible substance relationships.
         */
        public SubstanceSpecificationRelationshipComponent setIsDefining(boolean value) { 
            if (this.isDefining == null)
              this.isDefining = new BooleanType();
            this.isDefining.setValue(value);
          return this;
        }

        /**
         * @return {@link #amount} (A numeric factor for the relationship, for instance to express that the salt of a substance has some percentage of the active substance in relation to some other.)
         */
        public Type getAmount() { 
          return this.amount;
        }

        /**
         * @return {@link #amount} (A numeric factor for the relationship, for instance to express that the salt of a substance has some percentage of the active substance in relation to some other.)
         */
        public Quantity getAmountQuantity() throws FHIRException { 
          if (this.amount == null)
            this.amount = new Quantity();
          if (!(this.amount instanceof Quantity))
            throw new FHIRException("Type mismatch: the type Quantity was expected, but "+this.amount.getClass().getName()+" was encountered");
          return (Quantity) this.amount;
        }

        public boolean hasAmountQuantity() { 
          return this != null && this.amount instanceof Quantity;
        }

        /**
         * @return {@link #amount} (A numeric factor for the relationship, for instance to express that the salt of a substance has some percentage of the active substance in relation to some other.)
         */
        public Range getAmountRange() throws FHIRException { 
          if (this.amount == null)
            this.amount = new Range();
          if (!(this.amount instanceof Range))
            throw new FHIRException("Type mismatch: the type Range was expected, but "+this.amount.getClass().getName()+" was encountered");
          return (Range) this.amount;
        }

        public boolean hasAmountRange() { 
          return this != null && this.amount instanceof Range;
        }

        /**
         * @return {@link #amount} (A numeric factor for the relationship, for instance to express that the salt of a substance has some percentage of the active substance in relation to some other.)
         */
        public Ratio getAmountRatio() throws FHIRException { 
          if (this.amount == null)
            this.amount = new Ratio();
          if (!(this.amount instanceof Ratio))
            throw new FHIRException("Type mismatch: the type Ratio was expected, but "+this.amount.getClass().getName()+" was encountered");
          return (Ratio) this.amount;
        }

        public boolean hasAmountRatio() { 
          return this != null && this.amount instanceof Ratio;
        }

        /**
         * @return {@link #amount} (A numeric factor for the relationship, for instance to express that the salt of a substance has some percentage of the active substance in relation to some other.)
         */
        public StringType getAmountStringType() throws FHIRException { 
          if (this.amount == null)
            this.amount = new StringType();
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
         * @param value {@link #amount} (A numeric factor for the relationship, for instance to express that the salt of a substance has some percentage of the active substance in relation to some other.)
         */
        public SubstanceSpecificationRelationshipComponent setAmount(Type value) { 
          if (value != null && !(value instanceof Quantity || value instanceof Range || value instanceof Ratio || value instanceof StringType))
            throw new Error("Not the right type for SubstanceSpecification.relationship.amount[x]: "+value.fhirType());
          this.amount = value;
          return this;
        }

        /**
         * @return {@link #amountRatioLowLimit} (For use when the numeric.)
         */
        public Ratio getAmountRatioLowLimit() { 
          if (this.amountRatioLowLimit == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceSpecificationRelationshipComponent.amountRatioLowLimit");
            else if (Configuration.doAutoCreate())
              this.amountRatioLowLimit = new Ratio(); // cc
          return this.amountRatioLowLimit;
        }

        public boolean hasAmountRatioLowLimit() { 
          return this.amountRatioLowLimit != null && !this.amountRatioLowLimit.isEmpty();
        }

        /**
         * @param value {@link #amountRatioLowLimit} (For use when the numeric.)
         */
        public SubstanceSpecificationRelationshipComponent setAmountRatioLowLimit(Ratio value) { 
          this.amountRatioLowLimit = value;
          return this;
        }

        /**
         * @return {@link #amountType} (An operator for the amount, for example "average", "approximately", "less than".)
         */
        public CodeableConcept getAmountType() { 
          if (this.amountType == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceSpecificationRelationshipComponent.amountType");
            else if (Configuration.doAutoCreate())
              this.amountType = new CodeableConcept(); // cc
          return this.amountType;
        }

        public boolean hasAmountType() { 
          return this.amountType != null && !this.amountType.isEmpty();
        }

        /**
         * @param value {@link #amountType} (An operator for the amount, for example "average", "approximately", "less than".)
         */
        public SubstanceSpecificationRelationshipComponent setAmountType(CodeableConcept value) { 
          this.amountType = value;
          return this;
        }

        /**
         * @return {@link #source} (Supporting literature.)
         */
        public List<Reference> getSource() { 
          if (this.source == null)
            this.source = new ArrayList<Reference>();
          return this.source;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public SubstanceSpecificationRelationshipComponent setSource(List<Reference> theSource) { 
          this.source = theSource;
          return this;
        }

        public boolean hasSource() { 
          if (this.source == null)
            return false;
          for (Reference item : this.source)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public Reference addSource() { //3
          Reference t = new Reference();
          if (this.source == null)
            this.source = new ArrayList<Reference>();
          this.source.add(t);
          return t;
        }

        public SubstanceSpecificationRelationshipComponent addSource(Reference t) { //3
          if (t == null)
            return this;
          if (this.source == null)
            this.source = new ArrayList<Reference>();
          this.source.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #source}, creating it if it does not already exist
         */
        public Reference getSourceFirstRep() { 
          if (getSource().isEmpty()) {
            addSource();
          }
          return getSource().get(0);
        }

        /**
         * @deprecated Use Reference#setResource(IBaseResource) instead
         */
        @Deprecated
        public List<DocumentReference> getSourceTarget() { 
          if (this.sourceTarget == null)
            this.sourceTarget = new ArrayList<DocumentReference>();
          return this.sourceTarget;
        }

        /**
         * @deprecated Use Reference#setResource(IBaseResource) instead
         */
        @Deprecated
        public DocumentReference addSourceTarget() { 
          DocumentReference r = new DocumentReference();
          if (this.sourceTarget == null)
            this.sourceTarget = new ArrayList<DocumentReference>();
          this.sourceTarget.add(r);
          return r;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("substance[x]", "Reference(SubstanceSpecification)|CodeableConcept", "A pointer to another substance, as a resource or just a representational code.", 0, 1, substance));
          children.add(new Property("relationship", "CodeableConcept", "For example \"salt to parent\", \"active moiety\", \"starting material\".", 0, 1, relationship));
          children.add(new Property("isDefining", "boolean", "For example where an enzyme strongly bonds with a particular substance, this is a defining relationship for that enzyme, out of several possible substance relationships.", 0, 1, isDefining));
          children.add(new Property("amount[x]", "Quantity|Range|Ratio|string", "A numeric factor for the relationship, for instance to express that the salt of a substance has some percentage of the active substance in relation to some other.", 0, 1, amount));
          children.add(new Property("amountRatioLowLimit", "Ratio", "For use when the numeric.", 0, 1, amountRatioLowLimit));
          children.add(new Property("amountType", "CodeableConcept", "An operator for the amount, for example \"average\", \"approximately\", \"less than\".", 0, 1, amountType));
          children.add(new Property("source", "Reference(DocumentReference)", "Supporting literature.", 0, java.lang.Integer.MAX_VALUE, source));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 2127194384: /*substance[x]*/  return new Property("substance[x]", "Reference(SubstanceSpecification)|CodeableConcept", "A pointer to another substance, as a resource or just a representational code.", 0, 1, substance);
          case 530040176: /*substance*/  return new Property("substance[x]", "Reference(SubstanceSpecification)|CodeableConcept", "A pointer to another substance, as a resource or just a representational code.", 0, 1, substance);
          case 516208571: /*substanceReference*/  return new Property("substance[x]", "Reference(SubstanceSpecification)|CodeableConcept", "A pointer to another substance, as a resource or just a representational code.", 0, 1, substance);
          case -1974119407: /*substanceCodeableConcept*/  return new Property("substance[x]", "Reference(SubstanceSpecification)|CodeableConcept", "A pointer to another substance, as a resource or just a representational code.", 0, 1, substance);
          case -261851592: /*relationship*/  return new Property("relationship", "CodeableConcept", "For example \"salt to parent\", \"active moiety\", \"starting material\".", 0, 1, relationship);
          case -141812990: /*isDefining*/  return new Property("isDefining", "boolean", "For example where an enzyme strongly bonds with a particular substance, this is a defining relationship for that enzyme, out of several possible substance relationships.", 0, 1, isDefining);
          case 646780200: /*amount[x]*/  return new Property("amount[x]", "Quantity|Range|Ratio|string", "A numeric factor for the relationship, for instance to express that the salt of a substance has some percentage of the active substance in relation to some other.", 0, 1, amount);
          case -1413853096: /*amount*/  return new Property("amount[x]", "Quantity|Range|Ratio|string", "A numeric factor for the relationship, for instance to express that the salt of a substance has some percentage of the active substance in relation to some other.", 0, 1, amount);
          case 1664303363: /*amountQuantity*/  return new Property("amount[x]", "Quantity|Range|Ratio|string", "A numeric factor for the relationship, for instance to express that the salt of a substance has some percentage of the active substance in relation to some other.", 0, 1, amount);
          case -1223462971: /*amountRange*/  return new Property("amount[x]", "Quantity|Range|Ratio|string", "A numeric factor for the relationship, for instance to express that the salt of a substance has some percentage of the active substance in relation to some other.", 0, 1, amount);
          case -1223457133: /*amountRatio*/  return new Property("amount[x]", "Quantity|Range|Ratio|string", "A numeric factor for the relationship, for instance to express that the salt of a substance has some percentage of the active substance in relation to some other.", 0, 1, amount);
          case 773651081: /*amountString*/  return new Property("amount[x]", "Quantity|Range|Ratio|string", "A numeric factor for the relationship, for instance to express that the salt of a substance has some percentage of the active substance in relation to some other.", 0, 1, amount);
          case 2140623994: /*amountRatioLowLimit*/  return new Property("amountRatioLowLimit", "Ratio", "For use when the numeric.", 0, 1, amountRatioLowLimit);
          case -1424857166: /*amountType*/  return new Property("amountType", "CodeableConcept", "An operator for the amount, for example \"average\", \"approximately\", \"less than\".", 0, 1, amountType);
          case -896505829: /*source*/  return new Property("source", "Reference(DocumentReference)", "Supporting literature.", 0, java.lang.Integer.MAX_VALUE, source);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 530040176: /*substance*/ return this.substance == null ? new Base[0] : new Base[] {this.substance}; // Type
        case -261851592: /*relationship*/ return this.relationship == null ? new Base[0] : new Base[] {this.relationship}; // CodeableConcept
        case -141812990: /*isDefining*/ return this.isDefining == null ? new Base[0] : new Base[] {this.isDefining}; // BooleanType
        case -1413853096: /*amount*/ return this.amount == null ? new Base[0] : new Base[] {this.amount}; // Type
        case 2140623994: /*amountRatioLowLimit*/ return this.amountRatioLowLimit == null ? new Base[0] : new Base[] {this.amountRatioLowLimit}; // Ratio
        case -1424857166: /*amountType*/ return this.amountType == null ? new Base[0] : new Base[] {this.amountType}; // CodeableConcept
        case -896505829: /*source*/ return this.source == null ? new Base[0] : this.source.toArray(new Base[this.source.size()]); // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 530040176: // substance
          this.substance = castToType(value); // Type
          return value;
        case -261851592: // relationship
          this.relationship = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -141812990: // isDefining
          this.isDefining = castToBoolean(value); // BooleanType
          return value;
        case -1413853096: // amount
          this.amount = castToType(value); // Type
          return value;
        case 2140623994: // amountRatioLowLimit
          this.amountRatioLowLimit = castToRatio(value); // Ratio
          return value;
        case -1424857166: // amountType
          this.amountType = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -896505829: // source
          this.getSource().add(castToReference(value)); // Reference
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("substance[x]")) {
          this.substance = castToType(value); // Type
        } else if (name.equals("relationship")) {
          this.relationship = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("isDefining")) {
          this.isDefining = castToBoolean(value); // BooleanType
        } else if (name.equals("amount[x]")) {
          this.amount = castToType(value); // Type
        } else if (name.equals("amountRatioLowLimit")) {
          this.amountRatioLowLimit = castToRatio(value); // Ratio
        } else if (name.equals("amountType")) {
          this.amountType = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("source")) {
          this.getSource().add(castToReference(value));
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 2127194384:  return getSubstance(); 
        case 530040176:  return getSubstance(); 
        case -261851592:  return getRelationship(); 
        case -141812990:  return getIsDefiningElement();
        case 646780200:  return getAmount(); 
        case -1413853096:  return getAmount(); 
        case 2140623994:  return getAmountRatioLowLimit(); 
        case -1424857166:  return getAmountType(); 
        case -896505829:  return addSource(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 530040176: /*substance*/ return new String[] {"Reference", "CodeableConcept"};
        case -261851592: /*relationship*/ return new String[] {"CodeableConcept"};
        case -141812990: /*isDefining*/ return new String[] {"boolean"};
        case -1413853096: /*amount*/ return new String[] {"Quantity", "Range", "Ratio", "string"};
        case 2140623994: /*amountRatioLowLimit*/ return new String[] {"Ratio"};
        case -1424857166: /*amountType*/ return new String[] {"CodeableConcept"};
        case -896505829: /*source*/ return new String[] {"Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("substanceReference")) {
          this.substance = new Reference();
          return this.substance;
        }
        else if (name.equals("substanceCodeableConcept")) {
          this.substance = new CodeableConcept();
          return this.substance;
        }
        else if (name.equals("relationship")) {
          this.relationship = new CodeableConcept();
          return this.relationship;
        }
        else if (name.equals("isDefining")) {
          throw new FHIRException("Cannot call addChild on a primitive type SubstanceSpecification.isDefining");
        }
        else if (name.equals("amountQuantity")) {
          this.amount = new Quantity();
          return this.amount;
        }
        else if (name.equals("amountRange")) {
          this.amount = new Range();
          return this.amount;
        }
        else if (name.equals("amountRatio")) {
          this.amount = new Ratio();
          return this.amount;
        }
        else if (name.equals("amountString")) {
          this.amount = new StringType();
          return this.amount;
        }
        else if (name.equals("amountRatioLowLimit")) {
          this.amountRatioLowLimit = new Ratio();
          return this.amountRatioLowLimit;
        }
        else if (name.equals("amountType")) {
          this.amountType = new CodeableConcept();
          return this.amountType;
        }
        else if (name.equals("source")) {
          return addSource();
        }
        else
          return super.addChild(name);
      }

      public SubstanceSpecificationRelationshipComponent copy() {
        SubstanceSpecificationRelationshipComponent dst = new SubstanceSpecificationRelationshipComponent();
        copyValues(dst);
        dst.substance = substance == null ? null : substance.copy();
        dst.relationship = relationship == null ? null : relationship.copy();
        dst.isDefining = isDefining == null ? null : isDefining.copy();
        dst.amount = amount == null ? null : amount.copy();
        dst.amountRatioLowLimit = amountRatioLowLimit == null ? null : amountRatioLowLimit.copy();
        dst.amountType = amountType == null ? null : amountType.copy();
        if (source != null) {
          dst.source = new ArrayList<Reference>();
          for (Reference i : source)
            dst.source.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof SubstanceSpecificationRelationshipComponent))
          return false;
        SubstanceSpecificationRelationshipComponent o = (SubstanceSpecificationRelationshipComponent) other_;
        return compareDeep(substance, o.substance, true) && compareDeep(relationship, o.relationship, true)
           && compareDeep(isDefining, o.isDefining, true) && compareDeep(amount, o.amount, true) && compareDeep(amountRatioLowLimit, o.amountRatioLowLimit, true)
           && compareDeep(amountType, o.amountType, true) && compareDeep(source, o.source, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof SubstanceSpecificationRelationshipComponent))
          return false;
        SubstanceSpecificationRelationshipComponent o = (SubstanceSpecificationRelationshipComponent) other_;
        return compareValues(isDefining, o.isDefining, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(substance, relationship, isDefining
          , amount, amountRatioLowLimit, amountType, source);
      }

  public String fhirType() {
    return "SubstanceSpecification.relationship";

  }

  }

    /**
     * Identifier by which this substance is known.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Identifier by which this substance is known", formalDefinition="Identifier by which this substance is known." )
    protected Identifier identifier;

    /**
     * High level categorization, e.g. polymer or nucleic acid.
     */
    @Child(name = "type", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="High level categorization, e.g. polymer or nucleic acid", formalDefinition="High level categorization, e.g. polymer or nucleic acid." )
    protected CodeableConcept type;

    /**
     * Status of substance within the catalogue e.g. approved.
     */
    @Child(name = "status", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Status of substance within the catalogue e.g. approved", formalDefinition="Status of substance within the catalogue e.g. approved." )
    protected CodeableConcept status;

    /**
     * If the substance applies to only human or veterinary use.
     */
    @Child(name = "domain", type = {CodeableConcept.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="If the substance applies to only human or veterinary use", formalDefinition="If the substance applies to only human or veterinary use." )
    protected CodeableConcept domain;

    /**
     * Textual description of the substance.
     */
    @Child(name = "description", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Textual description of the substance", formalDefinition="Textual description of the substance." )
    protected StringType description;

    /**
     * Supporting literature.
     */
    @Child(name = "source", type = {DocumentReference.class}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Supporting literature", formalDefinition="Supporting literature." )
    protected List<Reference> source;
    /**
     * The actual objects that are the target of the reference (Supporting literature.)
     */
    protected List<DocumentReference> sourceTarget;


    /**
     * Textual comment about this record of a substance.
     */
    @Child(name = "comment", type = {StringType.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Textual comment about this record of a substance", formalDefinition="Textual comment about this record of a substance." )
    protected StringType comment;

    /**
     * Moiety, for structural modifications.
     */
    @Child(name = "moiety", type = {}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Moiety, for structural modifications", formalDefinition="Moiety, for structural modifications." )
    protected List<SubstanceSpecificationMoietyComponent> moiety;

    /**
     * General specifications for this substance, including how it is related to other substances.
     */
    @Child(name = "property", type = {}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="General specifications for this substance, including how it is related to other substances", formalDefinition="General specifications for this substance, including how it is related to other substances." )
    protected List<SubstanceSpecificationPropertyComponent> property;

    /**
     * General information detailing this substance.
     */
    @Child(name = "referenceInformation", type = {SubstanceReferenceInformation.class}, order=9, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="General information detailing this substance", formalDefinition="General information detailing this substance." )
    protected Reference referenceInformation;

    /**
     * The actual object that is the target of the reference (General information detailing this substance.)
     */
    protected SubstanceReferenceInformation referenceInformationTarget;

    /**
     * Structural information.
     */
    @Child(name = "structure", type = {}, order=10, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Structural information", formalDefinition="Structural information." )
    protected SubstanceSpecificationStructureComponent structure;

    /**
     * Codes associated with the substance.
     */
    @Child(name = "code", type = {}, order=11, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Codes associated with the substance", formalDefinition="Codes associated with the substance." )
    protected List<SubstanceSpecificationCodeComponent> code;

    /**
     * Names applicable to this substance.
     */
    @Child(name = "name", type = {}, order=12, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Names applicable to this substance", formalDefinition="Names applicable to this substance." )
    protected List<SubstanceSpecificationNameComponent> name;

    /**
     * The molecular weight or weight range (for proteins, polymers or nucleic acids).
     */
    @Child(name = "molecularWeight", type = {SubstanceSpecificationStructureIsotopeMolecularWeightComponent.class}, order=13, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="The molecular weight or weight range (for proteins, polymers or nucleic acids)", formalDefinition="The molecular weight or weight range (for proteins, polymers or nucleic acids)." )
    protected List<SubstanceSpecificationStructureIsotopeMolecularWeightComponent> molecularWeight;

    /**
     * A link between this substance and another, with details of the relationship.
     */
    @Child(name = "relationship", type = {}, order=14, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="A link between this substance and another, with details of the relationship", formalDefinition="A link between this substance and another, with details of the relationship." )
    protected List<SubstanceSpecificationRelationshipComponent> relationship;

    /**
     * Data items specific to nucleic acids.
     */
    @Child(name = "nucleicAcid", type = {SubstanceNucleicAcid.class}, order=15, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Data items specific to nucleic acids", formalDefinition="Data items specific to nucleic acids." )
    protected Reference nucleicAcid;

    /**
     * The actual object that is the target of the reference (Data items specific to nucleic acids.)
     */
    protected SubstanceNucleicAcid nucleicAcidTarget;

    /**
     * Data items specific to polymers.
     */
    @Child(name = "polymer", type = {SubstancePolymer.class}, order=16, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Data items specific to polymers", formalDefinition="Data items specific to polymers." )
    protected Reference polymer;

    /**
     * The actual object that is the target of the reference (Data items specific to polymers.)
     */
    protected SubstancePolymer polymerTarget;

    /**
     * Data items specific to proteins.
     */
    @Child(name = "protein", type = {SubstanceProtein.class}, order=17, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Data items specific to proteins", formalDefinition="Data items specific to proteins." )
    protected Reference protein;

    /**
     * The actual object that is the target of the reference (Data items specific to proteins.)
     */
    protected SubstanceProtein proteinTarget;

    /**
     * Material or taxonomic/anatomical source for the substance.
     */
    @Child(name = "sourceMaterial", type = {SubstanceSourceMaterial.class}, order=18, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Material or taxonomic/anatomical source for the substance", formalDefinition="Material or taxonomic/anatomical source for the substance." )
    protected Reference sourceMaterial;

    /**
     * The actual object that is the target of the reference (Material or taxonomic/anatomical source for the substance.)
     */
    protected SubstanceSourceMaterial sourceMaterialTarget;

    private static final long serialVersionUID = 1782072718L;

  /**
   * Constructor
   */
    public SubstanceSpecification() {
      super();
    }

    /**
     * @return {@link #identifier} (Identifier by which this substance is known.)
     */
    public Identifier getIdentifier() { 
      if (this.identifier == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SubstanceSpecification.identifier");
        else if (Configuration.doAutoCreate())
          this.identifier = new Identifier(); // cc
      return this.identifier;
    }

    public boolean hasIdentifier() { 
      return this.identifier != null && !this.identifier.isEmpty();
    }

    /**
     * @param value {@link #identifier} (Identifier by which this substance is known.)
     */
    public SubstanceSpecification setIdentifier(Identifier value) { 
      this.identifier = value;
      return this;
    }

    /**
     * @return {@link #type} (High level categorization, e.g. polymer or nucleic acid.)
     */
    public CodeableConcept getType() { 
      if (this.type == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SubstanceSpecification.type");
        else if (Configuration.doAutoCreate())
          this.type = new CodeableConcept(); // cc
      return this.type;
    }

    public boolean hasType() { 
      return this.type != null && !this.type.isEmpty();
    }

    /**
     * @param value {@link #type} (High level categorization, e.g. polymer or nucleic acid.)
     */
    public SubstanceSpecification setType(CodeableConcept value) { 
      this.type = value;
      return this;
    }

    /**
     * @return {@link #status} (Status of substance within the catalogue e.g. approved.)
     */
    public CodeableConcept getStatus() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SubstanceSpecification.status");
        else if (Configuration.doAutoCreate())
          this.status = new CodeableConcept(); // cc
      return this.status;
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (Status of substance within the catalogue e.g. approved.)
     */
    public SubstanceSpecification setStatus(CodeableConcept value) { 
      this.status = value;
      return this;
    }

    /**
     * @return {@link #domain} (If the substance applies to only human or veterinary use.)
     */
    public CodeableConcept getDomain() { 
      if (this.domain == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SubstanceSpecification.domain");
        else if (Configuration.doAutoCreate())
          this.domain = new CodeableConcept(); // cc
      return this.domain;
    }

    public boolean hasDomain() { 
      return this.domain != null && !this.domain.isEmpty();
    }

    /**
     * @param value {@link #domain} (If the substance applies to only human or veterinary use.)
     */
    public SubstanceSpecification setDomain(CodeableConcept value) { 
      this.domain = value;
      return this;
    }

    /**
     * @return {@link #description} (Textual description of the substance.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public StringType getDescriptionElement() { 
      if (this.description == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SubstanceSpecification.description");
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
     * @param value {@link #description} (Textual description of the substance.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public SubstanceSpecification setDescriptionElement(StringType value) { 
      this.description = value;
      return this;
    }

    /**
     * @return Textual description of the substance.
     */
    public String getDescription() { 
      return this.description == null ? null : this.description.getValue();
    }

    /**
     * @param value Textual description of the substance.
     */
    public SubstanceSpecification setDescription(String value) { 
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
     * @return {@link #source} (Supporting literature.)
     */
    public List<Reference> getSource() { 
      if (this.source == null)
        this.source = new ArrayList<Reference>();
      return this.source;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public SubstanceSpecification setSource(List<Reference> theSource) { 
      this.source = theSource;
      return this;
    }

    public boolean hasSource() { 
      if (this.source == null)
        return false;
      for (Reference item : this.source)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addSource() { //3
      Reference t = new Reference();
      if (this.source == null)
        this.source = new ArrayList<Reference>();
      this.source.add(t);
      return t;
    }

    public SubstanceSpecification addSource(Reference t) { //3
      if (t == null)
        return this;
      if (this.source == null)
        this.source = new ArrayList<Reference>();
      this.source.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #source}, creating it if it does not already exist
     */
    public Reference getSourceFirstRep() { 
      if (getSource().isEmpty()) {
        addSource();
      }
      return getSource().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<DocumentReference> getSourceTarget() { 
      if (this.sourceTarget == null)
        this.sourceTarget = new ArrayList<DocumentReference>();
      return this.sourceTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public DocumentReference addSourceTarget() { 
      DocumentReference r = new DocumentReference();
      if (this.sourceTarget == null)
        this.sourceTarget = new ArrayList<DocumentReference>();
      this.sourceTarget.add(r);
      return r;
    }

    /**
     * @return {@link #comment} (Textual comment about this record of a substance.). This is the underlying object with id, value and extensions. The accessor "getComment" gives direct access to the value
     */
    public StringType getCommentElement() { 
      if (this.comment == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SubstanceSpecification.comment");
        else if (Configuration.doAutoCreate())
          this.comment = new StringType(); // bb
      return this.comment;
    }

    public boolean hasCommentElement() { 
      return this.comment != null && !this.comment.isEmpty();
    }

    public boolean hasComment() { 
      return this.comment != null && !this.comment.isEmpty();
    }

    /**
     * @param value {@link #comment} (Textual comment about this record of a substance.). This is the underlying object with id, value and extensions. The accessor "getComment" gives direct access to the value
     */
    public SubstanceSpecification setCommentElement(StringType value) { 
      this.comment = value;
      return this;
    }

    /**
     * @return Textual comment about this record of a substance.
     */
    public String getComment() { 
      return this.comment == null ? null : this.comment.getValue();
    }

    /**
     * @param value Textual comment about this record of a substance.
     */
    public SubstanceSpecification setComment(String value) { 
      if (Utilities.noString(value))
        this.comment = null;
      else {
        if (this.comment == null)
          this.comment = new StringType();
        this.comment.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #moiety} (Moiety, for structural modifications.)
     */
    public List<SubstanceSpecificationMoietyComponent> getMoiety() { 
      if (this.moiety == null)
        this.moiety = new ArrayList<SubstanceSpecificationMoietyComponent>();
      return this.moiety;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public SubstanceSpecification setMoiety(List<SubstanceSpecificationMoietyComponent> theMoiety) { 
      this.moiety = theMoiety;
      return this;
    }

    public boolean hasMoiety() { 
      if (this.moiety == null)
        return false;
      for (SubstanceSpecificationMoietyComponent item : this.moiety)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public SubstanceSpecificationMoietyComponent addMoiety() { //3
      SubstanceSpecificationMoietyComponent t = new SubstanceSpecificationMoietyComponent();
      if (this.moiety == null)
        this.moiety = new ArrayList<SubstanceSpecificationMoietyComponent>();
      this.moiety.add(t);
      return t;
    }

    public SubstanceSpecification addMoiety(SubstanceSpecificationMoietyComponent t) { //3
      if (t == null)
        return this;
      if (this.moiety == null)
        this.moiety = new ArrayList<SubstanceSpecificationMoietyComponent>();
      this.moiety.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #moiety}, creating it if it does not already exist
     */
    public SubstanceSpecificationMoietyComponent getMoietyFirstRep() { 
      if (getMoiety().isEmpty()) {
        addMoiety();
      }
      return getMoiety().get(0);
    }

    /**
     * @return {@link #property} (General specifications for this substance, including how it is related to other substances.)
     */
    public List<SubstanceSpecificationPropertyComponent> getProperty() { 
      if (this.property == null)
        this.property = new ArrayList<SubstanceSpecificationPropertyComponent>();
      return this.property;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public SubstanceSpecification setProperty(List<SubstanceSpecificationPropertyComponent> theProperty) { 
      this.property = theProperty;
      return this;
    }

    public boolean hasProperty() { 
      if (this.property == null)
        return false;
      for (SubstanceSpecificationPropertyComponent item : this.property)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public SubstanceSpecificationPropertyComponent addProperty() { //3
      SubstanceSpecificationPropertyComponent t = new SubstanceSpecificationPropertyComponent();
      if (this.property == null)
        this.property = new ArrayList<SubstanceSpecificationPropertyComponent>();
      this.property.add(t);
      return t;
    }

    public SubstanceSpecification addProperty(SubstanceSpecificationPropertyComponent t) { //3
      if (t == null)
        return this;
      if (this.property == null)
        this.property = new ArrayList<SubstanceSpecificationPropertyComponent>();
      this.property.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #property}, creating it if it does not already exist
     */
    public SubstanceSpecificationPropertyComponent getPropertyFirstRep() { 
      if (getProperty().isEmpty()) {
        addProperty();
      }
      return getProperty().get(0);
    }

    /**
     * @return {@link #referenceInformation} (General information detailing this substance.)
     */
    public Reference getReferenceInformation() { 
      if (this.referenceInformation == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SubstanceSpecification.referenceInformation");
        else if (Configuration.doAutoCreate())
          this.referenceInformation = new Reference(); // cc
      return this.referenceInformation;
    }

    public boolean hasReferenceInformation() { 
      return this.referenceInformation != null && !this.referenceInformation.isEmpty();
    }

    /**
     * @param value {@link #referenceInformation} (General information detailing this substance.)
     */
    public SubstanceSpecification setReferenceInformation(Reference value) { 
      this.referenceInformation = value;
      return this;
    }

    /**
     * @return {@link #referenceInformation} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (General information detailing this substance.)
     */
    public SubstanceReferenceInformation getReferenceInformationTarget() { 
      if (this.referenceInformationTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SubstanceSpecification.referenceInformation");
        else if (Configuration.doAutoCreate())
          this.referenceInformationTarget = new SubstanceReferenceInformation(); // aa
      return this.referenceInformationTarget;
    }

    /**
     * @param value {@link #referenceInformation} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (General information detailing this substance.)
     */
    public SubstanceSpecification setReferenceInformationTarget(SubstanceReferenceInformation value) { 
      this.referenceInformationTarget = value;
      return this;
    }

    /**
     * @return {@link #structure} (Structural information.)
     */
    public SubstanceSpecificationStructureComponent getStructure() { 
      if (this.structure == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SubstanceSpecification.structure");
        else if (Configuration.doAutoCreate())
          this.structure = new SubstanceSpecificationStructureComponent(); // cc
      return this.structure;
    }

    public boolean hasStructure() { 
      return this.structure != null && !this.structure.isEmpty();
    }

    /**
     * @param value {@link #structure} (Structural information.)
     */
    public SubstanceSpecification setStructure(SubstanceSpecificationStructureComponent value) { 
      this.structure = value;
      return this;
    }

    /**
     * @return {@link #code} (Codes associated with the substance.)
     */
    public List<SubstanceSpecificationCodeComponent> getCode() { 
      if (this.code == null)
        this.code = new ArrayList<SubstanceSpecificationCodeComponent>();
      return this.code;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public SubstanceSpecification setCode(List<SubstanceSpecificationCodeComponent> theCode) { 
      this.code = theCode;
      return this;
    }

    public boolean hasCode() { 
      if (this.code == null)
        return false;
      for (SubstanceSpecificationCodeComponent item : this.code)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public SubstanceSpecificationCodeComponent addCode() { //3
      SubstanceSpecificationCodeComponent t = new SubstanceSpecificationCodeComponent();
      if (this.code == null)
        this.code = new ArrayList<SubstanceSpecificationCodeComponent>();
      this.code.add(t);
      return t;
    }

    public SubstanceSpecification addCode(SubstanceSpecificationCodeComponent t) { //3
      if (t == null)
        return this;
      if (this.code == null)
        this.code = new ArrayList<SubstanceSpecificationCodeComponent>();
      this.code.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #code}, creating it if it does not already exist
     */
    public SubstanceSpecificationCodeComponent getCodeFirstRep() { 
      if (getCode().isEmpty()) {
        addCode();
      }
      return getCode().get(0);
    }

    /**
     * @return {@link #name} (Names applicable to this substance.)
     */
    public List<SubstanceSpecificationNameComponent> getName() { 
      if (this.name == null)
        this.name = new ArrayList<SubstanceSpecificationNameComponent>();
      return this.name;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public SubstanceSpecification setName(List<SubstanceSpecificationNameComponent> theName) { 
      this.name = theName;
      return this;
    }

    public boolean hasName() { 
      if (this.name == null)
        return false;
      for (SubstanceSpecificationNameComponent item : this.name)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public SubstanceSpecificationNameComponent addName() { //3
      SubstanceSpecificationNameComponent t = new SubstanceSpecificationNameComponent();
      if (this.name == null)
        this.name = new ArrayList<SubstanceSpecificationNameComponent>();
      this.name.add(t);
      return t;
    }

    public SubstanceSpecification addName(SubstanceSpecificationNameComponent t) { //3
      if (t == null)
        return this;
      if (this.name == null)
        this.name = new ArrayList<SubstanceSpecificationNameComponent>();
      this.name.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #name}, creating it if it does not already exist
     */
    public SubstanceSpecificationNameComponent getNameFirstRep() { 
      if (getName().isEmpty()) {
        addName();
      }
      return getName().get(0);
    }

    /**
     * @return {@link #molecularWeight} (The molecular weight or weight range (for proteins, polymers or nucleic acids).)
     */
    public List<SubstanceSpecificationStructureIsotopeMolecularWeightComponent> getMolecularWeight() { 
      if (this.molecularWeight == null)
        this.molecularWeight = new ArrayList<SubstanceSpecificationStructureIsotopeMolecularWeightComponent>();
      return this.molecularWeight;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public SubstanceSpecification setMolecularWeight(List<SubstanceSpecificationStructureIsotopeMolecularWeightComponent> theMolecularWeight) { 
      this.molecularWeight = theMolecularWeight;
      return this;
    }

    public boolean hasMolecularWeight() { 
      if (this.molecularWeight == null)
        return false;
      for (SubstanceSpecificationStructureIsotopeMolecularWeightComponent item : this.molecularWeight)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public SubstanceSpecificationStructureIsotopeMolecularWeightComponent addMolecularWeight() { //3
      SubstanceSpecificationStructureIsotopeMolecularWeightComponent t = new SubstanceSpecificationStructureIsotopeMolecularWeightComponent();
      if (this.molecularWeight == null)
        this.molecularWeight = new ArrayList<SubstanceSpecificationStructureIsotopeMolecularWeightComponent>();
      this.molecularWeight.add(t);
      return t;
    }

    public SubstanceSpecification addMolecularWeight(SubstanceSpecificationStructureIsotopeMolecularWeightComponent t) { //3
      if (t == null)
        return this;
      if (this.molecularWeight == null)
        this.molecularWeight = new ArrayList<SubstanceSpecificationStructureIsotopeMolecularWeightComponent>();
      this.molecularWeight.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #molecularWeight}, creating it if it does not already exist
     */
    public SubstanceSpecificationStructureIsotopeMolecularWeightComponent getMolecularWeightFirstRep() { 
      if (getMolecularWeight().isEmpty()) {
        addMolecularWeight();
      }
      return getMolecularWeight().get(0);
    }

    /**
     * @return {@link #relationship} (A link between this substance and another, with details of the relationship.)
     */
    public List<SubstanceSpecificationRelationshipComponent> getRelationship() { 
      if (this.relationship == null)
        this.relationship = new ArrayList<SubstanceSpecificationRelationshipComponent>();
      return this.relationship;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public SubstanceSpecification setRelationship(List<SubstanceSpecificationRelationshipComponent> theRelationship) { 
      this.relationship = theRelationship;
      return this;
    }

    public boolean hasRelationship() { 
      if (this.relationship == null)
        return false;
      for (SubstanceSpecificationRelationshipComponent item : this.relationship)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public SubstanceSpecificationRelationshipComponent addRelationship() { //3
      SubstanceSpecificationRelationshipComponent t = new SubstanceSpecificationRelationshipComponent();
      if (this.relationship == null)
        this.relationship = new ArrayList<SubstanceSpecificationRelationshipComponent>();
      this.relationship.add(t);
      return t;
    }

    public SubstanceSpecification addRelationship(SubstanceSpecificationRelationshipComponent t) { //3
      if (t == null)
        return this;
      if (this.relationship == null)
        this.relationship = new ArrayList<SubstanceSpecificationRelationshipComponent>();
      this.relationship.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #relationship}, creating it if it does not already exist
     */
    public SubstanceSpecificationRelationshipComponent getRelationshipFirstRep() { 
      if (getRelationship().isEmpty()) {
        addRelationship();
      }
      return getRelationship().get(0);
    }

    /**
     * @return {@link #nucleicAcid} (Data items specific to nucleic acids.)
     */
    public Reference getNucleicAcid() { 
      if (this.nucleicAcid == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SubstanceSpecification.nucleicAcid");
        else if (Configuration.doAutoCreate())
          this.nucleicAcid = new Reference(); // cc
      return this.nucleicAcid;
    }

    public boolean hasNucleicAcid() { 
      return this.nucleicAcid != null && !this.nucleicAcid.isEmpty();
    }

    /**
     * @param value {@link #nucleicAcid} (Data items specific to nucleic acids.)
     */
    public SubstanceSpecification setNucleicAcid(Reference value) { 
      this.nucleicAcid = value;
      return this;
    }

    /**
     * @return {@link #nucleicAcid} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Data items specific to nucleic acids.)
     */
    public SubstanceNucleicAcid getNucleicAcidTarget() { 
      if (this.nucleicAcidTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SubstanceSpecification.nucleicAcid");
        else if (Configuration.doAutoCreate())
          this.nucleicAcidTarget = new SubstanceNucleicAcid(); // aa
      return this.nucleicAcidTarget;
    }

    /**
     * @param value {@link #nucleicAcid} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Data items specific to nucleic acids.)
     */
    public SubstanceSpecification setNucleicAcidTarget(SubstanceNucleicAcid value) { 
      this.nucleicAcidTarget = value;
      return this;
    }

    /**
     * @return {@link #polymer} (Data items specific to polymers.)
     */
    public Reference getPolymer() { 
      if (this.polymer == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SubstanceSpecification.polymer");
        else if (Configuration.doAutoCreate())
          this.polymer = new Reference(); // cc
      return this.polymer;
    }

    public boolean hasPolymer() { 
      return this.polymer != null && !this.polymer.isEmpty();
    }

    /**
     * @param value {@link #polymer} (Data items specific to polymers.)
     */
    public SubstanceSpecification setPolymer(Reference value) { 
      this.polymer = value;
      return this;
    }

    /**
     * @return {@link #polymer} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Data items specific to polymers.)
     */
    public SubstancePolymer getPolymerTarget() { 
      if (this.polymerTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SubstanceSpecification.polymer");
        else if (Configuration.doAutoCreate())
          this.polymerTarget = new SubstancePolymer(); // aa
      return this.polymerTarget;
    }

    /**
     * @param value {@link #polymer} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Data items specific to polymers.)
     */
    public SubstanceSpecification setPolymerTarget(SubstancePolymer value) { 
      this.polymerTarget = value;
      return this;
    }

    /**
     * @return {@link #protein} (Data items specific to proteins.)
     */
    public Reference getProtein() { 
      if (this.protein == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SubstanceSpecification.protein");
        else if (Configuration.doAutoCreate())
          this.protein = new Reference(); // cc
      return this.protein;
    }

    public boolean hasProtein() { 
      return this.protein != null && !this.protein.isEmpty();
    }

    /**
     * @param value {@link #protein} (Data items specific to proteins.)
     */
    public SubstanceSpecification setProtein(Reference value) { 
      this.protein = value;
      return this;
    }

    /**
     * @return {@link #protein} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Data items specific to proteins.)
     */
    public SubstanceProtein getProteinTarget() { 
      if (this.proteinTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SubstanceSpecification.protein");
        else if (Configuration.doAutoCreate())
          this.proteinTarget = new SubstanceProtein(); // aa
      return this.proteinTarget;
    }

    /**
     * @param value {@link #protein} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Data items specific to proteins.)
     */
    public SubstanceSpecification setProteinTarget(SubstanceProtein value) { 
      this.proteinTarget = value;
      return this;
    }

    /**
     * @return {@link #sourceMaterial} (Material or taxonomic/anatomical source for the substance.)
     */
    public Reference getSourceMaterial() { 
      if (this.sourceMaterial == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SubstanceSpecification.sourceMaterial");
        else if (Configuration.doAutoCreate())
          this.sourceMaterial = new Reference(); // cc
      return this.sourceMaterial;
    }

    public boolean hasSourceMaterial() { 
      return this.sourceMaterial != null && !this.sourceMaterial.isEmpty();
    }

    /**
     * @param value {@link #sourceMaterial} (Material or taxonomic/anatomical source for the substance.)
     */
    public SubstanceSpecification setSourceMaterial(Reference value) { 
      this.sourceMaterial = value;
      return this;
    }

    /**
     * @return {@link #sourceMaterial} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Material or taxonomic/anatomical source for the substance.)
     */
    public SubstanceSourceMaterial getSourceMaterialTarget() { 
      if (this.sourceMaterialTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SubstanceSpecification.sourceMaterial");
        else if (Configuration.doAutoCreate())
          this.sourceMaterialTarget = new SubstanceSourceMaterial(); // aa
      return this.sourceMaterialTarget;
    }

    /**
     * @param value {@link #sourceMaterial} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Material or taxonomic/anatomical source for the substance.)
     */
    public SubstanceSpecification setSourceMaterialTarget(SubstanceSourceMaterial value) { 
      this.sourceMaterialTarget = value;
      return this;
    }

      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("identifier", "Identifier", "Identifier by which this substance is known.", 0, 1, identifier));
        children.add(new Property("type", "CodeableConcept", "High level categorization, e.g. polymer or nucleic acid.", 0, 1, type));
        children.add(new Property("status", "CodeableConcept", "Status of substance within the catalogue e.g. approved.", 0, 1, status));
        children.add(new Property("domain", "CodeableConcept", "If the substance applies to only human or veterinary use.", 0, 1, domain));
        children.add(new Property("description", "string", "Textual description of the substance.", 0, 1, description));
        children.add(new Property("source", "Reference(DocumentReference)", "Supporting literature.", 0, java.lang.Integer.MAX_VALUE, source));
        children.add(new Property("comment", "string", "Textual comment about this record of a substance.", 0, 1, comment));
        children.add(new Property("moiety", "", "Moiety, for structural modifications.", 0, java.lang.Integer.MAX_VALUE, moiety));
        children.add(new Property("property", "", "General specifications for this substance, including how it is related to other substances.", 0, java.lang.Integer.MAX_VALUE, property));
        children.add(new Property("referenceInformation", "Reference(SubstanceReferenceInformation)", "General information detailing this substance.", 0, 1, referenceInformation));
        children.add(new Property("structure", "", "Structural information.", 0, 1, structure));
        children.add(new Property("code", "", "Codes associated with the substance.", 0, java.lang.Integer.MAX_VALUE, code));
        children.add(new Property("name", "", "Names applicable to this substance.", 0, java.lang.Integer.MAX_VALUE, name));
        children.add(new Property("molecularWeight", "@SubstanceSpecification.structure.isotope.molecularWeight", "The molecular weight or weight range (for proteins, polymers or nucleic acids).", 0, java.lang.Integer.MAX_VALUE, molecularWeight));
        children.add(new Property("relationship", "", "A link between this substance and another, with details of the relationship.", 0, java.lang.Integer.MAX_VALUE, relationship));
        children.add(new Property("nucleicAcid", "Reference(SubstanceNucleicAcid)", "Data items specific to nucleic acids.", 0, 1, nucleicAcid));
        children.add(new Property("polymer", "Reference(SubstancePolymer)", "Data items specific to polymers.", 0, 1, polymer));
        children.add(new Property("protein", "Reference(SubstanceProtein)", "Data items specific to proteins.", 0, 1, protein));
        children.add(new Property("sourceMaterial", "Reference(SubstanceSourceMaterial)", "Material or taxonomic/anatomical source for the substance.", 0, 1, sourceMaterial));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case -1618432855: /*identifier*/  return new Property("identifier", "Identifier", "Identifier by which this substance is known.", 0, 1, identifier);
        case 3575610: /*type*/  return new Property("type", "CodeableConcept", "High level categorization, e.g. polymer or nucleic acid.", 0, 1, type);
        case -892481550: /*status*/  return new Property("status", "CodeableConcept", "Status of substance within the catalogue e.g. approved.", 0, 1, status);
        case -1326197564: /*domain*/  return new Property("domain", "CodeableConcept", "If the substance applies to only human or veterinary use.", 0, 1, domain);
        case -1724546052: /*description*/  return new Property("description", "string", "Textual description of the substance.", 0, 1, description);
        case -896505829: /*source*/  return new Property("source", "Reference(DocumentReference)", "Supporting literature.", 0, java.lang.Integer.MAX_VALUE, source);
        case 950398559: /*comment*/  return new Property("comment", "string", "Textual comment about this record of a substance.", 0, 1, comment);
        case -1068650173: /*moiety*/  return new Property("moiety", "", "Moiety, for structural modifications.", 0, java.lang.Integer.MAX_VALUE, moiety);
        case -993141291: /*property*/  return new Property("property", "", "General specifications for this substance, including how it is related to other substances.", 0, java.lang.Integer.MAX_VALUE, property);
        case -2117930783: /*referenceInformation*/  return new Property("referenceInformation", "Reference(SubstanceReferenceInformation)", "General information detailing this substance.", 0, 1, referenceInformation);
        case 144518515: /*structure*/  return new Property("structure", "", "Structural information.", 0, 1, structure);
        case 3059181: /*code*/  return new Property("code", "", "Codes associated with the substance.", 0, java.lang.Integer.MAX_VALUE, code);
        case 3373707: /*name*/  return new Property("name", "", "Names applicable to this substance.", 0, java.lang.Integer.MAX_VALUE, name);
        case 635625672: /*molecularWeight*/  return new Property("molecularWeight", "@SubstanceSpecification.structure.isotope.molecularWeight", "The molecular weight or weight range (for proteins, polymers or nucleic acids).", 0, java.lang.Integer.MAX_VALUE, molecularWeight);
        case -261851592: /*relationship*/  return new Property("relationship", "", "A link between this substance and another, with details of the relationship.", 0, java.lang.Integer.MAX_VALUE, relationship);
        case 1625275180: /*nucleicAcid*/  return new Property("nucleicAcid", "Reference(SubstanceNucleicAcid)", "Data items specific to nucleic acids.", 0, 1, nucleicAcid);
        case -397514098: /*polymer*/  return new Property("polymer", "Reference(SubstancePolymer)", "Data items specific to polymers.", 0, 1, polymer);
        case -309012605: /*protein*/  return new Property("protein", "Reference(SubstanceProtein)", "Data items specific to proteins.", 0, 1, protein);
        case -1064442270: /*sourceMaterial*/  return new Property("sourceMaterial", "Reference(SubstanceSourceMaterial)", "Material or taxonomic/anatomical source for the substance.", 0, 1, sourceMaterial);
        default: return super.getNamedProperty(_hash, _name, _checkValid);
        }

      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : new Base[] {this.identifier}; // Identifier
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // CodeableConcept
        case -1326197564: /*domain*/ return this.domain == null ? new Base[0] : new Base[] {this.domain}; // CodeableConcept
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // StringType
        case -896505829: /*source*/ return this.source == null ? new Base[0] : this.source.toArray(new Base[this.source.size()]); // Reference
        case 950398559: /*comment*/ return this.comment == null ? new Base[0] : new Base[] {this.comment}; // StringType
        case -1068650173: /*moiety*/ return this.moiety == null ? new Base[0] : this.moiety.toArray(new Base[this.moiety.size()]); // SubstanceSpecificationMoietyComponent
        case -993141291: /*property*/ return this.property == null ? new Base[0] : this.property.toArray(new Base[this.property.size()]); // SubstanceSpecificationPropertyComponent
        case -2117930783: /*referenceInformation*/ return this.referenceInformation == null ? new Base[0] : new Base[] {this.referenceInformation}; // Reference
        case 144518515: /*structure*/ return this.structure == null ? new Base[0] : new Base[] {this.structure}; // SubstanceSpecificationStructureComponent
        case 3059181: /*code*/ return this.code == null ? new Base[0] : this.code.toArray(new Base[this.code.size()]); // SubstanceSpecificationCodeComponent
        case 3373707: /*name*/ return this.name == null ? new Base[0] : this.name.toArray(new Base[this.name.size()]); // SubstanceSpecificationNameComponent
        case 635625672: /*molecularWeight*/ return this.molecularWeight == null ? new Base[0] : this.molecularWeight.toArray(new Base[this.molecularWeight.size()]); // SubstanceSpecificationStructureIsotopeMolecularWeightComponent
        case -261851592: /*relationship*/ return this.relationship == null ? new Base[0] : this.relationship.toArray(new Base[this.relationship.size()]); // SubstanceSpecificationRelationshipComponent
        case 1625275180: /*nucleicAcid*/ return this.nucleicAcid == null ? new Base[0] : new Base[] {this.nucleicAcid}; // Reference
        case -397514098: /*polymer*/ return this.polymer == null ? new Base[0] : new Base[] {this.polymer}; // Reference
        case -309012605: /*protein*/ return this.protein == null ? new Base[0] : new Base[] {this.protein}; // Reference
        case -1064442270: /*sourceMaterial*/ return this.sourceMaterial == null ? new Base[0] : new Base[] {this.sourceMaterial}; // Reference
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
        case -892481550: // status
          this.status = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1326197564: // domain
          this.domain = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1724546052: // description
          this.description = castToString(value); // StringType
          return value;
        case -896505829: // source
          this.getSource().add(castToReference(value)); // Reference
          return value;
        case 950398559: // comment
          this.comment = castToString(value); // StringType
          return value;
        case -1068650173: // moiety
          this.getMoiety().add((SubstanceSpecificationMoietyComponent) value); // SubstanceSpecificationMoietyComponent
          return value;
        case -993141291: // property
          this.getProperty().add((SubstanceSpecificationPropertyComponent) value); // SubstanceSpecificationPropertyComponent
          return value;
        case -2117930783: // referenceInformation
          this.referenceInformation = castToReference(value); // Reference
          return value;
        case 144518515: // structure
          this.structure = (SubstanceSpecificationStructureComponent) value; // SubstanceSpecificationStructureComponent
          return value;
        case 3059181: // code
          this.getCode().add((SubstanceSpecificationCodeComponent) value); // SubstanceSpecificationCodeComponent
          return value;
        case 3373707: // name
          this.getName().add((SubstanceSpecificationNameComponent) value); // SubstanceSpecificationNameComponent
          return value;
        case 635625672: // molecularWeight
          this.getMolecularWeight().add((SubstanceSpecificationStructureIsotopeMolecularWeightComponent) value); // SubstanceSpecificationStructureIsotopeMolecularWeightComponent
          return value;
        case -261851592: // relationship
          this.getRelationship().add((SubstanceSpecificationRelationshipComponent) value); // SubstanceSpecificationRelationshipComponent
          return value;
        case 1625275180: // nucleicAcid
          this.nucleicAcid = castToReference(value); // Reference
          return value;
        case -397514098: // polymer
          this.polymer = castToReference(value); // Reference
          return value;
        case -309012605: // protein
          this.protein = castToReference(value); // Reference
          return value;
        case -1064442270: // sourceMaterial
          this.sourceMaterial = castToReference(value); // Reference
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
        } else if (name.equals("status")) {
          this.status = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("domain")) {
          this.domain = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("description")) {
          this.description = castToString(value); // StringType
        } else if (name.equals("source")) {
          this.getSource().add(castToReference(value));
        } else if (name.equals("comment")) {
          this.comment = castToString(value); // StringType
        } else if (name.equals("moiety")) {
          this.getMoiety().add((SubstanceSpecificationMoietyComponent) value);
        } else if (name.equals("property")) {
          this.getProperty().add((SubstanceSpecificationPropertyComponent) value);
        } else if (name.equals("referenceInformation")) {
          this.referenceInformation = castToReference(value); // Reference
        } else if (name.equals("structure")) {
          this.structure = (SubstanceSpecificationStructureComponent) value; // SubstanceSpecificationStructureComponent
        } else if (name.equals("code")) {
          this.getCode().add((SubstanceSpecificationCodeComponent) value);
        } else if (name.equals("name")) {
          this.getName().add((SubstanceSpecificationNameComponent) value);
        } else if (name.equals("molecularWeight")) {
          this.getMolecularWeight().add((SubstanceSpecificationStructureIsotopeMolecularWeightComponent) value);
        } else if (name.equals("relationship")) {
          this.getRelationship().add((SubstanceSpecificationRelationshipComponent) value);
        } else if (name.equals("nucleicAcid")) {
          this.nucleicAcid = castToReference(value); // Reference
        } else if (name.equals("polymer")) {
          this.polymer = castToReference(value); // Reference
        } else if (name.equals("protein")) {
          this.protein = castToReference(value); // Reference
        } else if (name.equals("sourceMaterial")) {
          this.sourceMaterial = castToReference(value); // Reference
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return getIdentifier(); 
        case 3575610:  return getType(); 
        case -892481550:  return getStatus(); 
        case -1326197564:  return getDomain(); 
        case -1724546052:  return getDescriptionElement();
        case -896505829:  return addSource(); 
        case 950398559:  return getCommentElement();
        case -1068650173:  return addMoiety(); 
        case -993141291:  return addProperty(); 
        case -2117930783:  return getReferenceInformation(); 
        case 144518515:  return getStructure(); 
        case 3059181:  return addCode(); 
        case 3373707:  return addName(); 
        case 635625672:  return addMolecularWeight(); 
        case -261851592:  return addRelationship(); 
        case 1625275180:  return getNucleicAcid(); 
        case -397514098:  return getPolymer(); 
        case -309012605:  return getProtein(); 
        case -1064442270:  return getSourceMaterial(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case -892481550: /*status*/ return new String[] {"CodeableConcept"};
        case -1326197564: /*domain*/ return new String[] {"CodeableConcept"};
        case -1724546052: /*description*/ return new String[] {"string"};
        case -896505829: /*source*/ return new String[] {"Reference"};
        case 950398559: /*comment*/ return new String[] {"string"};
        case -1068650173: /*moiety*/ return new String[] {};
        case -993141291: /*property*/ return new String[] {};
        case -2117930783: /*referenceInformation*/ return new String[] {"Reference"};
        case 144518515: /*structure*/ return new String[] {};
        case 3059181: /*code*/ return new String[] {};
        case 3373707: /*name*/ return new String[] {};
        case 635625672: /*molecularWeight*/ return new String[] {"@SubstanceSpecification.structure.isotope.molecularWeight"};
        case -261851592: /*relationship*/ return new String[] {};
        case 1625275180: /*nucleicAcid*/ return new String[] {"Reference"};
        case -397514098: /*polymer*/ return new String[] {"Reference"};
        case -309012605: /*protein*/ return new String[] {"Reference"};
        case -1064442270: /*sourceMaterial*/ return new String[] {"Reference"};
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
        else if (name.equals("status")) {
          this.status = new CodeableConcept();
          return this.status;
        }
        else if (name.equals("domain")) {
          this.domain = new CodeableConcept();
          return this.domain;
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type SubstanceSpecification.description");
        }
        else if (name.equals("source")) {
          return addSource();
        }
        else if (name.equals("comment")) {
          throw new FHIRException("Cannot call addChild on a primitive type SubstanceSpecification.comment");
        }
        else if (name.equals("moiety")) {
          return addMoiety();
        }
        else if (name.equals("property")) {
          return addProperty();
        }
        else if (name.equals("referenceInformation")) {
          this.referenceInformation = new Reference();
          return this.referenceInformation;
        }
        else if (name.equals("structure")) {
          this.structure = new SubstanceSpecificationStructureComponent();
          return this.structure;
        }
        else if (name.equals("code")) {
          return addCode();
        }
        else if (name.equals("name")) {
          return addName();
        }
        else if (name.equals("molecularWeight")) {
          return addMolecularWeight();
        }
        else if (name.equals("relationship")) {
          return addRelationship();
        }
        else if (name.equals("nucleicAcid")) {
          this.nucleicAcid = new Reference();
          return this.nucleicAcid;
        }
        else if (name.equals("polymer")) {
          this.polymer = new Reference();
          return this.polymer;
        }
        else if (name.equals("protein")) {
          this.protein = new Reference();
          return this.protein;
        }
        else if (name.equals("sourceMaterial")) {
          this.sourceMaterial = new Reference();
          return this.sourceMaterial;
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "SubstanceSpecification";

  }

      public SubstanceSpecification copy() {
        SubstanceSpecification dst = new SubstanceSpecification();
        copyValues(dst);
        dst.identifier = identifier == null ? null : identifier.copy();
        dst.type = type == null ? null : type.copy();
        dst.status = status == null ? null : status.copy();
        dst.domain = domain == null ? null : domain.copy();
        dst.description = description == null ? null : description.copy();
        if (source != null) {
          dst.source = new ArrayList<Reference>();
          for (Reference i : source)
            dst.source.add(i.copy());
        };
        dst.comment = comment == null ? null : comment.copy();
        if (moiety != null) {
          dst.moiety = new ArrayList<SubstanceSpecificationMoietyComponent>();
          for (SubstanceSpecificationMoietyComponent i : moiety)
            dst.moiety.add(i.copy());
        };
        if (property != null) {
          dst.property = new ArrayList<SubstanceSpecificationPropertyComponent>();
          for (SubstanceSpecificationPropertyComponent i : property)
            dst.property.add(i.copy());
        };
        dst.referenceInformation = referenceInformation == null ? null : referenceInformation.copy();
        dst.structure = structure == null ? null : structure.copy();
        if (code != null) {
          dst.code = new ArrayList<SubstanceSpecificationCodeComponent>();
          for (SubstanceSpecificationCodeComponent i : code)
            dst.code.add(i.copy());
        };
        if (name != null) {
          dst.name = new ArrayList<SubstanceSpecificationNameComponent>();
          for (SubstanceSpecificationNameComponent i : name)
            dst.name.add(i.copy());
        };
        if (molecularWeight != null) {
          dst.molecularWeight = new ArrayList<SubstanceSpecificationStructureIsotopeMolecularWeightComponent>();
          for (SubstanceSpecificationStructureIsotopeMolecularWeightComponent i : molecularWeight)
            dst.molecularWeight.add(i.copy());
        };
        if (relationship != null) {
          dst.relationship = new ArrayList<SubstanceSpecificationRelationshipComponent>();
          for (SubstanceSpecificationRelationshipComponent i : relationship)
            dst.relationship.add(i.copy());
        };
        dst.nucleicAcid = nucleicAcid == null ? null : nucleicAcid.copy();
        dst.polymer = polymer == null ? null : polymer.copy();
        dst.protein = protein == null ? null : protein.copy();
        dst.sourceMaterial = sourceMaterial == null ? null : sourceMaterial.copy();
        return dst;
      }

      protected SubstanceSpecification typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof SubstanceSpecification))
          return false;
        SubstanceSpecification o = (SubstanceSpecification) other_;
        return compareDeep(identifier, o.identifier, true) && compareDeep(type, o.type, true) && compareDeep(status, o.status, true)
           && compareDeep(domain, o.domain, true) && compareDeep(description, o.description, true) && compareDeep(source, o.source, true)
           && compareDeep(comment, o.comment, true) && compareDeep(moiety, o.moiety, true) && compareDeep(property, o.property, true)
           && compareDeep(referenceInformation, o.referenceInformation, true) && compareDeep(structure, o.structure, true)
           && compareDeep(code, o.code, true) && compareDeep(name, o.name, true) && compareDeep(molecularWeight, o.molecularWeight, true)
           && compareDeep(relationship, o.relationship, true) && compareDeep(nucleicAcid, o.nucleicAcid, true)
           && compareDeep(polymer, o.polymer, true) && compareDeep(protein, o.protein, true) && compareDeep(sourceMaterial, o.sourceMaterial, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof SubstanceSpecification))
          return false;
        SubstanceSpecification o = (SubstanceSpecification) other_;
        return compareValues(description, o.description, true) && compareValues(comment, o.comment, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, type, status
          , domain, description, source, comment, moiety, property, referenceInformation
          , structure, code, name, molecularWeight, relationship, nucleicAcid, polymer, protein
          , sourceMaterial);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.SubstanceSpecification;
   }

 /**
   * Search parameter: <b>code</b>
   * <p>
   * Description: <b>Codes associated with the substance</b><br>
   * Type: <b>token</b><br>
   * Path: <b>SubstanceSpecification.code</b><br>
   * </p>
   */
  @SearchParamDefinition(name="code", path="SubstanceSpecification.code", description="Codes associated with the substance", type="token" )
  public static final String SP_CODE = "code";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>code</b>
   * <p>
   * Description: <b>Codes associated with the substance</b><br>
   * Type: <b>token</b><br>
   * Path: <b>SubstanceSpecification.code</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CODE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CODE);


}

