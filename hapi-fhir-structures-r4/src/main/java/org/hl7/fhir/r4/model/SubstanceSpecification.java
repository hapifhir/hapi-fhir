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
@ResourceDef(name="SubstanceSpecification", profile="http://hl7.org/fhir/Profile/SubstanceSpecification")
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
        @Child(name = "amount", type = {StringType.class}, order=7, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Quantitative value for this moiety", formalDefinition="Quantitative value for this moiety." )
        protected StringType amount;

        private static final long serialVersionUID = 45594592L;

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
         * @return {@link #amount} (Quantitative value for this moiety.). This is the underlying object with id, value and extensions. The accessor "getAmount" gives direct access to the value
         */
        public StringType getAmountElement() { 
          if (this.amount == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceSpecificationMoietyComponent.amount");
            else if (Configuration.doAutoCreate())
              this.amount = new StringType(); // bb
          return this.amount;
        }

        public boolean hasAmountElement() { 
          return this.amount != null && !this.amount.isEmpty();
        }

        public boolean hasAmount() { 
          return this.amount != null && !this.amount.isEmpty();
        }

        /**
         * @param value {@link #amount} (Quantitative value for this moiety.). This is the underlying object with id, value and extensions. The accessor "getAmount" gives direct access to the value
         */
        public SubstanceSpecificationMoietyComponent setAmountElement(StringType value) { 
          this.amount = value;
          return this;
        }

        /**
         * @return Quantitative value for this moiety.
         */
        public String getAmount() { 
          return this.amount == null ? null : this.amount.getValue();
        }

        /**
         * @param value Quantitative value for this moiety.
         */
        public SubstanceSpecificationMoietyComponent setAmount(String value) { 
          if (Utilities.noString(value))
            this.amount = null;
          else {
            if (this.amount == null)
              this.amount = new StringType();
            this.amount.setValue(value);
          }
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
          children.add(new Property("amount", "string", "Quantitative value for this moiety.", 0, 1, amount));
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
          case -1413853096: /*amount*/  return new Property("amount", "string", "Quantitative value for this moiety.", 0, 1, amount);
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
        case -1413853096: /*amount*/ return this.amount == null ? new Base[0] : new Base[] {this.amount}; // StringType
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
          this.amount = castToString(value); // StringType
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
        } else if (name.equals("amount")) {
          this.amount = castToString(value); // StringType
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
        case -1413853096:  return getAmountElement();
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
        case -1413853096: /*amount*/ return new String[] {"string"};
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
        else if (name.equals("amount")) {
          throw new FHIRException("Cannot call addChild on a primitive type SubstanceSpecification.amount");
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
           && compareValues(amount, o.amount, true);
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
         * Description todo.
         */
        @Child(name = "type", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Description todo", formalDefinition="Description todo." )
        protected CodeableConcept type;

        /**
         * Description todo.
         */
        @Child(name = "name", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Description todo", formalDefinition="Description todo." )
        protected CodeableConcept name;

        /**
         * A field that should be used to capture parameters that were used in the measurement of a property.
         */
        @Child(name = "parameters", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="A field that should be used to capture parameters that were used in the measurement of a property", formalDefinition="A field that should be used to capture parameters that were used in the measurement of a property." )
        protected StringType parameters;

        /**
         * Identifier for a substance upon which a defining property depends.
         */
        @Child(name = "substanceId", type = {Identifier.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Identifier for a substance upon which a defining property depends", formalDefinition="Identifier for a substance upon which a defining property depends." )
        protected Identifier substanceId;

        /**
         * Description todo.
         */
        @Child(name = "substanceName", type = {StringType.class}, order=5, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Description todo", formalDefinition="Description todo." )
        protected StringType substanceName;

        /**
         * Quantitative value for this property.
         */
        @Child(name = "amount", type = {StringType.class}, order=6, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Quantitative value for this property", formalDefinition="Quantitative value for this property." )
        protected StringType amount;

        private static final long serialVersionUID = 83235941L;

    /**
     * Constructor
     */
      public SubstanceSpecificationPropertyComponent() {
        super();
      }

        /**
         * @return {@link #type} (Description todo.)
         */
        public CodeableConcept getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceSpecificationPropertyComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new CodeableConcept(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (Description todo.)
         */
        public SubstanceSpecificationPropertyComponent setType(CodeableConcept value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #name} (Description todo.)
         */
        public CodeableConcept getName() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceSpecificationPropertyComponent.name");
            else if (Configuration.doAutoCreate())
              this.name = new CodeableConcept(); // cc
          return this.name;
        }

        public boolean hasName() { 
          return this.name != null && !this.name.isEmpty();
        }

        /**
         * @param value {@link #name} (Description todo.)
         */
        public SubstanceSpecificationPropertyComponent setName(CodeableConcept value) { 
          this.name = value;
          return this;
        }

        /**
         * @return {@link #parameters} (A field that should be used to capture parameters that were used in the measurement of a property.). This is the underlying object with id, value and extensions. The accessor "getParameters" gives direct access to the value
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
         * @param value {@link #parameters} (A field that should be used to capture parameters that were used in the measurement of a property.). This is the underlying object with id, value and extensions. The accessor "getParameters" gives direct access to the value
         */
        public SubstanceSpecificationPropertyComponent setParametersElement(StringType value) { 
          this.parameters = value;
          return this;
        }

        /**
         * @return A field that should be used to capture parameters that were used in the measurement of a property.
         */
        public String getParameters() { 
          return this.parameters == null ? null : this.parameters.getValue();
        }

        /**
         * @param value A field that should be used to capture parameters that were used in the measurement of a property.
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
         * @return {@link #substanceId} (Identifier for a substance upon which a defining property depends.)
         */
        public Identifier getSubstanceId() { 
          if (this.substanceId == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceSpecificationPropertyComponent.substanceId");
            else if (Configuration.doAutoCreate())
              this.substanceId = new Identifier(); // cc
          return this.substanceId;
        }

        public boolean hasSubstanceId() { 
          return this.substanceId != null && !this.substanceId.isEmpty();
        }

        /**
         * @param value {@link #substanceId} (Identifier for a substance upon which a defining property depends.)
         */
        public SubstanceSpecificationPropertyComponent setSubstanceId(Identifier value) { 
          this.substanceId = value;
          return this;
        }

        /**
         * @return {@link #substanceName} (Description todo.). This is the underlying object with id, value and extensions. The accessor "getSubstanceName" gives direct access to the value
         */
        public StringType getSubstanceNameElement() { 
          if (this.substanceName == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceSpecificationPropertyComponent.substanceName");
            else if (Configuration.doAutoCreate())
              this.substanceName = new StringType(); // bb
          return this.substanceName;
        }

        public boolean hasSubstanceNameElement() { 
          return this.substanceName != null && !this.substanceName.isEmpty();
        }

        public boolean hasSubstanceName() { 
          return this.substanceName != null && !this.substanceName.isEmpty();
        }

        /**
         * @param value {@link #substanceName} (Description todo.). This is the underlying object with id, value and extensions. The accessor "getSubstanceName" gives direct access to the value
         */
        public SubstanceSpecificationPropertyComponent setSubstanceNameElement(StringType value) { 
          this.substanceName = value;
          return this;
        }

        /**
         * @return Description todo.
         */
        public String getSubstanceName() { 
          return this.substanceName == null ? null : this.substanceName.getValue();
        }

        /**
         * @param value Description todo.
         */
        public SubstanceSpecificationPropertyComponent setSubstanceName(String value) { 
          if (Utilities.noString(value))
            this.substanceName = null;
          else {
            if (this.substanceName == null)
              this.substanceName = new StringType();
            this.substanceName.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #amount} (Quantitative value for this property.). This is the underlying object with id, value and extensions. The accessor "getAmount" gives direct access to the value
         */
        public StringType getAmountElement() { 
          if (this.amount == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceSpecificationPropertyComponent.amount");
            else if (Configuration.doAutoCreate())
              this.amount = new StringType(); // bb
          return this.amount;
        }

        public boolean hasAmountElement() { 
          return this.amount != null && !this.amount.isEmpty();
        }

        public boolean hasAmount() { 
          return this.amount != null && !this.amount.isEmpty();
        }

        /**
         * @param value {@link #amount} (Quantitative value for this property.). This is the underlying object with id, value and extensions. The accessor "getAmount" gives direct access to the value
         */
        public SubstanceSpecificationPropertyComponent setAmountElement(StringType value) { 
          this.amount = value;
          return this;
        }

        /**
         * @return Quantitative value for this property.
         */
        public String getAmount() { 
          return this.amount == null ? null : this.amount.getValue();
        }

        /**
         * @param value Quantitative value for this property.
         */
        public SubstanceSpecificationPropertyComponent setAmount(String value) { 
          if (Utilities.noString(value))
            this.amount = null;
          else {
            if (this.amount == null)
              this.amount = new StringType();
            this.amount.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("type", "CodeableConcept", "Description todo.", 0, 1, type));
          children.add(new Property("name", "CodeableConcept", "Description todo.", 0, 1, name));
          children.add(new Property("parameters", "string", "A field that should be used to capture parameters that were used in the measurement of a property.", 0, 1, parameters));
          children.add(new Property("substanceId", "Identifier", "Identifier for a substance upon which a defining property depends.", 0, 1, substanceId));
          children.add(new Property("substanceName", "string", "Description todo.", 0, 1, substanceName));
          children.add(new Property("amount", "string", "Quantitative value for this property.", 0, 1, amount));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3575610: /*type*/  return new Property("type", "CodeableConcept", "Description todo.", 0, 1, type);
          case 3373707: /*name*/  return new Property("name", "CodeableConcept", "Description todo.", 0, 1, name);
          case 458736106: /*parameters*/  return new Property("parameters", "string", "A field that should be used to capture parameters that were used in the measurement of a property.", 0, 1, parameters);
          case -1732496725: /*substanceId*/  return new Property("substanceId", "Identifier", "Identifier for a substance upon which a defining property depends.", 0, 1, substanceId);
          case 1518107675: /*substanceName*/  return new Property("substanceName", "string", "Description todo.", 0, 1, substanceName);
          case -1413853096: /*amount*/  return new Property("amount", "string", "Quantitative value for this property.", 0, 1, amount);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // CodeableConcept
        case 458736106: /*parameters*/ return this.parameters == null ? new Base[0] : new Base[] {this.parameters}; // StringType
        case -1732496725: /*substanceId*/ return this.substanceId == null ? new Base[0] : new Base[] {this.substanceId}; // Identifier
        case 1518107675: /*substanceName*/ return this.substanceName == null ? new Base[0] : new Base[] {this.substanceName}; // StringType
        case -1413853096: /*amount*/ return this.amount == null ? new Base[0] : new Base[] {this.amount}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 3373707: // name
          this.name = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 458736106: // parameters
          this.parameters = castToString(value); // StringType
          return value;
        case -1732496725: // substanceId
          this.substanceId = castToIdentifier(value); // Identifier
          return value;
        case 1518107675: // substanceName
          this.substanceName = castToString(value); // StringType
          return value;
        case -1413853096: // amount
          this.amount = castToString(value); // StringType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type")) {
          this.type = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("name")) {
          this.name = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("parameters")) {
          this.parameters = castToString(value); // StringType
        } else if (name.equals("substanceId")) {
          this.substanceId = castToIdentifier(value); // Identifier
        } else if (name.equals("substanceName")) {
          this.substanceName = castToString(value); // StringType
        } else if (name.equals("amount")) {
          this.amount = castToString(value); // StringType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610:  return getType(); 
        case 3373707:  return getName(); 
        case 458736106:  return getParametersElement();
        case -1732496725:  return getSubstanceId(); 
        case 1518107675:  return getSubstanceNameElement();
        case -1413853096:  return getAmountElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case 3373707: /*name*/ return new String[] {"CodeableConcept"};
        case 458736106: /*parameters*/ return new String[] {"string"};
        case -1732496725: /*substanceId*/ return new String[] {"Identifier"};
        case 1518107675: /*substanceName*/ return new String[] {"string"};
        case -1413853096: /*amount*/ return new String[] {"string"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("name")) {
          this.name = new CodeableConcept();
          return this.name;
        }
        else if (name.equals("parameters")) {
          throw new FHIRException("Cannot call addChild on a primitive type SubstanceSpecification.parameters");
        }
        else if (name.equals("substanceId")) {
          this.substanceId = new Identifier();
          return this.substanceId;
        }
        else if (name.equals("substanceName")) {
          throw new FHIRException("Cannot call addChild on a primitive type SubstanceSpecification.substanceName");
        }
        else if (name.equals("amount")) {
          throw new FHIRException("Cannot call addChild on a primitive type SubstanceSpecification.amount");
        }
        else
          return super.addChild(name);
      }

      public SubstanceSpecificationPropertyComponent copy() {
        SubstanceSpecificationPropertyComponent dst = new SubstanceSpecificationPropertyComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.name = name == null ? null : name.copy();
        dst.parameters = parameters == null ? null : parameters.copy();
        dst.substanceId = substanceId == null ? null : substanceId.copy();
        dst.substanceName = substanceName == null ? null : substanceName.copy();
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
        return compareDeep(type, o.type, true) && compareDeep(name, o.name, true) && compareDeep(parameters, o.parameters, true)
           && compareDeep(substanceId, o.substanceId, true) && compareDeep(substanceName, o.substanceName, true)
           && compareDeep(amount, o.amount, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof SubstanceSpecificationPropertyComponent))
          return false;
        SubstanceSpecificationPropertyComponent o = (SubstanceSpecificationPropertyComponent) other_;
        return compareValues(parameters, o.parameters, true) && compareValues(substanceName, o.substanceName, true)
           && compareValues(amount, o.amount, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(type, name, parameters, substanceId
          , substanceName, amount);
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
         * Specified per moiety according to the Hill system, i.e. first C, then H, then alphabetical. and each moiety separated by a dot.
         */
        @Child(name = "molecularFormulaByMoiety", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Specified per moiety according to the Hill system, i.e. first C, then H, then alphabetical. and each moiety separated by a dot", formalDefinition="Specified per moiety according to the Hill system, i.e. first C, then H, then alphabetical. and each moiety separated by a dot." )
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
        @Child(name = "referenceSource", type = {DocumentReference.class}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Supporting literature", formalDefinition="Supporting literature." )
        protected List<Reference> referenceSource;
        /**
         * The actual objects that are the target of the reference (Supporting literature.)
         */
        protected List<DocumentReference> referenceSourceTarget;


        /**
         * Molectular structural representation.
         */
        @Child(name = "structuralRepresentation", type = {}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Molectular structural representation", formalDefinition="Molectular structural representation." )
        protected List<SubstanceSpecificationStructureStructuralRepresentationComponent> structuralRepresentation;

        private static final long serialVersionUID = -2087062825L;

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
         * @return {@link #molecularFormulaByMoiety} (Specified per moiety according to the Hill system, i.e. first C, then H, then alphabetical. and each moiety separated by a dot.). This is the underlying object with id, value and extensions. The accessor "getMolecularFormulaByMoiety" gives direct access to the value
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
         * @param value {@link #molecularFormulaByMoiety} (Specified per moiety according to the Hill system, i.e. first C, then H, then alphabetical. and each moiety separated by a dot.). This is the underlying object with id, value and extensions. The accessor "getMolecularFormulaByMoiety" gives direct access to the value
         */
        public SubstanceSpecificationStructureComponent setMolecularFormulaByMoietyElement(StringType value) { 
          this.molecularFormulaByMoiety = value;
          return this;
        }

        /**
         * @return Specified per moiety according to the Hill system, i.e. first C, then H, then alphabetical. and each moiety separated by a dot.
         */
        public String getMolecularFormulaByMoiety() { 
          return this.molecularFormulaByMoiety == null ? null : this.molecularFormulaByMoiety.getValue();
        }

        /**
         * @param value Specified per moiety according to the Hill system, i.e. first C, then H, then alphabetical. and each moiety separated by a dot.
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
         * @return {@link #referenceSource} (Supporting literature.)
         */
        public List<Reference> getReferenceSource() { 
          if (this.referenceSource == null)
            this.referenceSource = new ArrayList<Reference>();
          return this.referenceSource;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public SubstanceSpecificationStructureComponent setReferenceSource(List<Reference> theReferenceSource) { 
          this.referenceSource = theReferenceSource;
          return this;
        }

        public boolean hasReferenceSource() { 
          if (this.referenceSource == null)
            return false;
          for (Reference item : this.referenceSource)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public Reference addReferenceSource() { //3
          Reference t = new Reference();
          if (this.referenceSource == null)
            this.referenceSource = new ArrayList<Reference>();
          this.referenceSource.add(t);
          return t;
        }

        public SubstanceSpecificationStructureComponent addReferenceSource(Reference t) { //3
          if (t == null)
            return this;
          if (this.referenceSource == null)
            this.referenceSource = new ArrayList<Reference>();
          this.referenceSource.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #referenceSource}, creating it if it does not already exist
         */
        public Reference getReferenceSourceFirstRep() { 
          if (getReferenceSource().isEmpty()) {
            addReferenceSource();
          }
          return getReferenceSource().get(0);
        }

        /**
         * @deprecated Use Reference#setResource(IBaseResource) instead
         */
        @Deprecated
        public List<DocumentReference> getReferenceSourceTarget() { 
          if (this.referenceSourceTarget == null)
            this.referenceSourceTarget = new ArrayList<DocumentReference>();
          return this.referenceSourceTarget;
        }

        /**
         * @deprecated Use Reference#setResource(IBaseResource) instead
         */
        @Deprecated
        public DocumentReference addReferenceSourceTarget() { 
          DocumentReference r = new DocumentReference();
          if (this.referenceSourceTarget == null)
            this.referenceSourceTarget = new ArrayList<DocumentReference>();
          this.referenceSourceTarget.add(r);
          return r;
        }

        /**
         * @return {@link #structuralRepresentation} (Molectular structural representation.)
         */
        public List<SubstanceSpecificationStructureStructuralRepresentationComponent> getStructuralRepresentation() { 
          if (this.structuralRepresentation == null)
            this.structuralRepresentation = new ArrayList<SubstanceSpecificationStructureStructuralRepresentationComponent>();
          return this.structuralRepresentation;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public SubstanceSpecificationStructureComponent setStructuralRepresentation(List<SubstanceSpecificationStructureStructuralRepresentationComponent> theStructuralRepresentation) { 
          this.structuralRepresentation = theStructuralRepresentation;
          return this;
        }

        public boolean hasStructuralRepresentation() { 
          if (this.structuralRepresentation == null)
            return false;
          for (SubstanceSpecificationStructureStructuralRepresentationComponent item : this.structuralRepresentation)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public SubstanceSpecificationStructureStructuralRepresentationComponent addStructuralRepresentation() { //3
          SubstanceSpecificationStructureStructuralRepresentationComponent t = new SubstanceSpecificationStructureStructuralRepresentationComponent();
          if (this.structuralRepresentation == null)
            this.structuralRepresentation = new ArrayList<SubstanceSpecificationStructureStructuralRepresentationComponent>();
          this.structuralRepresentation.add(t);
          return t;
        }

        public SubstanceSpecificationStructureComponent addStructuralRepresentation(SubstanceSpecificationStructureStructuralRepresentationComponent t) { //3
          if (t == null)
            return this;
          if (this.structuralRepresentation == null)
            this.structuralRepresentation = new ArrayList<SubstanceSpecificationStructureStructuralRepresentationComponent>();
          this.structuralRepresentation.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #structuralRepresentation}, creating it if it does not already exist
         */
        public SubstanceSpecificationStructureStructuralRepresentationComponent getStructuralRepresentationFirstRep() { 
          if (getStructuralRepresentation().isEmpty()) {
            addStructuralRepresentation();
          }
          return getStructuralRepresentation().get(0);
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("stereochemistry", "CodeableConcept", "Stereochemistry type.", 0, 1, stereochemistry));
          children.add(new Property("opticalActivity", "CodeableConcept", "Optical activity type.", 0, 1, opticalActivity));
          children.add(new Property("molecularFormula", "string", "Molecular formula.", 0, 1, molecularFormula));
          children.add(new Property("molecularFormulaByMoiety", "string", "Specified per moiety according to the Hill system, i.e. first C, then H, then alphabetical. and each moiety separated by a dot.", 0, 1, molecularFormulaByMoiety));
          children.add(new Property("isotope", "", "Applicable for single substances that contain a radionuclide or a non-natural isotopic ratio.", 0, java.lang.Integer.MAX_VALUE, isotope));
          children.add(new Property("molecularWeight", "@SubstanceSpecification.structure.isotope.molecularWeight", "The molecular weight or weight range (for proteins, polymers or nucleic acids).", 0, 1, molecularWeight));
          children.add(new Property("referenceSource", "Reference(DocumentReference)", "Supporting literature.", 0, java.lang.Integer.MAX_VALUE, referenceSource));
          children.add(new Property("structuralRepresentation", "", "Molectular structural representation.", 0, java.lang.Integer.MAX_VALUE, structuralRepresentation));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 263475116: /*stereochemistry*/  return new Property("stereochemistry", "CodeableConcept", "Stereochemistry type.", 0, 1, stereochemistry);
          case 1420900135: /*opticalActivity*/  return new Property("opticalActivity", "CodeableConcept", "Optical activity type.", 0, 1, opticalActivity);
          case 616660246: /*molecularFormula*/  return new Property("molecularFormula", "string", "Molecular formula.", 0, 1, molecularFormula);
          case 1315452848: /*molecularFormulaByMoiety*/  return new Property("molecularFormulaByMoiety", "string", "Specified per moiety according to the Hill system, i.e. first C, then H, then alphabetical. and each moiety separated by a dot.", 0, 1, molecularFormulaByMoiety);
          case 2097035189: /*isotope*/  return new Property("isotope", "", "Applicable for single substances that contain a radionuclide or a non-natural isotopic ratio.", 0, java.lang.Integer.MAX_VALUE, isotope);
          case 635625672: /*molecularWeight*/  return new Property("molecularWeight", "@SubstanceSpecification.structure.isotope.molecularWeight", "The molecular weight or weight range (for proteins, polymers or nucleic acids).", 0, 1, molecularWeight);
          case 882421574: /*referenceSource*/  return new Property("referenceSource", "Reference(DocumentReference)", "Supporting literature.", 0, java.lang.Integer.MAX_VALUE, referenceSource);
          case 14311178: /*structuralRepresentation*/  return new Property("structuralRepresentation", "", "Molectular structural representation.", 0, java.lang.Integer.MAX_VALUE, structuralRepresentation);
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
        case 882421574: /*referenceSource*/ return this.referenceSource == null ? new Base[0] : this.referenceSource.toArray(new Base[this.referenceSource.size()]); // Reference
        case 14311178: /*structuralRepresentation*/ return this.structuralRepresentation == null ? new Base[0] : this.structuralRepresentation.toArray(new Base[this.structuralRepresentation.size()]); // SubstanceSpecificationStructureStructuralRepresentationComponent
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
        case 882421574: // referenceSource
          this.getReferenceSource().add(castToReference(value)); // Reference
          return value;
        case 14311178: // structuralRepresentation
          this.getStructuralRepresentation().add((SubstanceSpecificationStructureStructuralRepresentationComponent) value); // SubstanceSpecificationStructureStructuralRepresentationComponent
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
        } else if (name.equals("referenceSource")) {
          this.getReferenceSource().add(castToReference(value));
        } else if (name.equals("structuralRepresentation")) {
          this.getStructuralRepresentation().add((SubstanceSpecificationStructureStructuralRepresentationComponent) value);
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
        case 882421574:  return addReferenceSource(); 
        case 14311178:  return addStructuralRepresentation(); 
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
        case 882421574: /*referenceSource*/ return new String[] {"Reference"};
        case 14311178: /*structuralRepresentation*/ return new String[] {};
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
        else if (name.equals("referenceSource")) {
          return addReferenceSource();
        }
        else if (name.equals("structuralRepresentation")) {
          return addStructuralRepresentation();
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
        if (referenceSource != null) {
          dst.referenceSource = new ArrayList<Reference>();
          for (Reference i : referenceSource)
            dst.referenceSource.add(i.copy());
        };
        if (structuralRepresentation != null) {
          dst.structuralRepresentation = new ArrayList<SubstanceSpecificationStructureStructuralRepresentationComponent>();
          for (SubstanceSpecificationStructureStructuralRepresentationComponent i : structuralRepresentation)
            dst.structuralRepresentation.add(i.copy());
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
           && compareDeep(referenceSource, o.referenceSource, true) && compareDeep(structuralRepresentation, o.structuralRepresentation, true)
          ;
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
          , molecularFormula, molecularFormulaByMoiety, isotope, molecularWeight, referenceSource
          , structuralRepresentation);
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
        @Child(name = "nuclideId", type = {Identifier.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Substance identifier for each non-natural or radioisotope", formalDefinition="Substance identifier for each non-natural or radioisotope." )
        protected Identifier nuclideId;

        /**
         * Substance name for each non-natural or radioisotope.
         */
        @Child(name = "nuclideName", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Substance name for each non-natural or radioisotope", formalDefinition="Substance name for each non-natural or radioisotope." )
        protected CodeableConcept nuclideName;

        /**
         * The type of isotopic substitution present in a single substance.
         */
        @Child(name = "substitutionType", type = {CodeableConcept.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The type of isotopic substitution present in a single substance", formalDefinition="The type of isotopic substitution present in a single substance." )
        protected CodeableConcept substitutionType;

        /**
         * Half life - for a non-natural nuclide.
         */
        @Child(name = "nuclideHalfLife", type = {Quantity.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Half life - for a non-natural nuclide", formalDefinition="Half life - for a non-natural nuclide." )
        protected Quantity nuclideHalfLife;

        /**
         * Quantitative values for this isotope.
         */
        @Child(name = "amount", type = {StringType.class}, order=5, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Quantitative values for this isotope", formalDefinition="Quantitative values for this isotope." )
        protected StringType amount;

        /**
         * The molecular weight or weight range (for proteins, polymers or nucleic acids).
         */
        @Child(name = "molecularWeight", type = {}, order=6, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The molecular weight or weight range (for proteins, polymers or nucleic acids)", formalDefinition="The molecular weight or weight range (for proteins, polymers or nucleic acids)." )
        protected SubstanceSpecificationStructureIsotopeMolecularWeightComponent molecularWeight;

        private static final long serialVersionUID = -654404311L;

    /**
     * Constructor
     */
      public SubstanceSpecificationStructureIsotopeComponent() {
        super();
      }

        /**
         * @return {@link #nuclideId} (Substance identifier for each non-natural or radioisotope.)
         */
        public Identifier getNuclideId() { 
          if (this.nuclideId == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceSpecificationStructureIsotopeComponent.nuclideId");
            else if (Configuration.doAutoCreate())
              this.nuclideId = new Identifier(); // cc
          return this.nuclideId;
        }

        public boolean hasNuclideId() { 
          return this.nuclideId != null && !this.nuclideId.isEmpty();
        }

        /**
         * @param value {@link #nuclideId} (Substance identifier for each non-natural or radioisotope.)
         */
        public SubstanceSpecificationStructureIsotopeComponent setNuclideId(Identifier value) { 
          this.nuclideId = value;
          return this;
        }

        /**
         * @return {@link #nuclideName} (Substance name for each non-natural or radioisotope.)
         */
        public CodeableConcept getNuclideName() { 
          if (this.nuclideName == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceSpecificationStructureIsotopeComponent.nuclideName");
            else if (Configuration.doAutoCreate())
              this.nuclideName = new CodeableConcept(); // cc
          return this.nuclideName;
        }

        public boolean hasNuclideName() { 
          return this.nuclideName != null && !this.nuclideName.isEmpty();
        }

        /**
         * @param value {@link #nuclideName} (Substance name for each non-natural or radioisotope.)
         */
        public SubstanceSpecificationStructureIsotopeComponent setNuclideName(CodeableConcept value) { 
          this.nuclideName = value;
          return this;
        }

        /**
         * @return {@link #substitutionType} (The type of isotopic substitution present in a single substance.)
         */
        public CodeableConcept getSubstitutionType() { 
          if (this.substitutionType == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceSpecificationStructureIsotopeComponent.substitutionType");
            else if (Configuration.doAutoCreate())
              this.substitutionType = new CodeableConcept(); // cc
          return this.substitutionType;
        }

        public boolean hasSubstitutionType() { 
          return this.substitutionType != null && !this.substitutionType.isEmpty();
        }

        /**
         * @param value {@link #substitutionType} (The type of isotopic substitution present in a single substance.)
         */
        public SubstanceSpecificationStructureIsotopeComponent setSubstitutionType(CodeableConcept value) { 
          this.substitutionType = value;
          return this;
        }

        /**
         * @return {@link #nuclideHalfLife} (Half life - for a non-natural nuclide.)
         */
        public Quantity getNuclideHalfLife() { 
          if (this.nuclideHalfLife == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceSpecificationStructureIsotopeComponent.nuclideHalfLife");
            else if (Configuration.doAutoCreate())
              this.nuclideHalfLife = new Quantity(); // cc
          return this.nuclideHalfLife;
        }

        public boolean hasNuclideHalfLife() { 
          return this.nuclideHalfLife != null && !this.nuclideHalfLife.isEmpty();
        }

        /**
         * @param value {@link #nuclideHalfLife} (Half life - for a non-natural nuclide.)
         */
        public SubstanceSpecificationStructureIsotopeComponent setNuclideHalfLife(Quantity value) { 
          this.nuclideHalfLife = value;
          return this;
        }

        /**
         * @return {@link #amount} (Quantitative values for this isotope.). This is the underlying object with id, value and extensions. The accessor "getAmount" gives direct access to the value
         */
        public StringType getAmountElement() { 
          if (this.amount == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceSpecificationStructureIsotopeComponent.amount");
            else if (Configuration.doAutoCreate())
              this.amount = new StringType(); // bb
          return this.amount;
        }

        public boolean hasAmountElement() { 
          return this.amount != null && !this.amount.isEmpty();
        }

        public boolean hasAmount() { 
          return this.amount != null && !this.amount.isEmpty();
        }

        /**
         * @param value {@link #amount} (Quantitative values for this isotope.). This is the underlying object with id, value and extensions. The accessor "getAmount" gives direct access to the value
         */
        public SubstanceSpecificationStructureIsotopeComponent setAmountElement(StringType value) { 
          this.amount = value;
          return this;
        }

        /**
         * @return Quantitative values for this isotope.
         */
        public String getAmount() { 
          return this.amount == null ? null : this.amount.getValue();
        }

        /**
         * @param value Quantitative values for this isotope.
         */
        public SubstanceSpecificationStructureIsotopeComponent setAmount(String value) { 
          if (Utilities.noString(value))
            this.amount = null;
          else {
            if (this.amount == null)
              this.amount = new StringType();
            this.amount.setValue(value);
          }
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
          children.add(new Property("nuclideId", "Identifier", "Substance identifier for each non-natural or radioisotope.", 0, 1, nuclideId));
          children.add(new Property("nuclideName", "CodeableConcept", "Substance name for each non-natural or radioisotope.", 0, 1, nuclideName));
          children.add(new Property("substitutionType", "CodeableConcept", "The type of isotopic substitution present in a single substance.", 0, 1, substitutionType));
          children.add(new Property("nuclideHalfLife", "Quantity", "Half life - for a non-natural nuclide.", 0, 1, nuclideHalfLife));
          children.add(new Property("amount", "string", "Quantitative values for this isotope.", 0, 1, amount));
          children.add(new Property("molecularWeight", "", "The molecular weight or weight range (for proteins, polymers or nucleic acids).", 0, 1, molecularWeight));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 1654398709: /*nuclideId*/  return new Property("nuclideId", "Identifier", "Substance identifier for each non-natural or radioisotope.", 0, 1, nuclideId);
          case 739409381: /*nuclideName*/  return new Property("nuclideName", "CodeableConcept", "Substance name for each non-natural or radioisotope.", 0, 1, nuclideName);
          case -1937621033: /*substitutionType*/  return new Property("substitutionType", "CodeableConcept", "The type of isotopic substitution present in a single substance.", 0, 1, substitutionType);
          case 1348294697: /*nuclideHalfLife*/  return new Property("nuclideHalfLife", "Quantity", "Half life - for a non-natural nuclide.", 0, 1, nuclideHalfLife);
          case -1413853096: /*amount*/  return new Property("amount", "string", "Quantitative values for this isotope.", 0, 1, amount);
          case 635625672: /*molecularWeight*/  return new Property("molecularWeight", "", "The molecular weight or weight range (for proteins, polymers or nucleic acids).", 0, 1, molecularWeight);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 1654398709: /*nuclideId*/ return this.nuclideId == null ? new Base[0] : new Base[] {this.nuclideId}; // Identifier
        case 739409381: /*nuclideName*/ return this.nuclideName == null ? new Base[0] : new Base[] {this.nuclideName}; // CodeableConcept
        case -1937621033: /*substitutionType*/ return this.substitutionType == null ? new Base[0] : new Base[] {this.substitutionType}; // CodeableConcept
        case 1348294697: /*nuclideHalfLife*/ return this.nuclideHalfLife == null ? new Base[0] : new Base[] {this.nuclideHalfLife}; // Quantity
        case -1413853096: /*amount*/ return this.amount == null ? new Base[0] : new Base[] {this.amount}; // StringType
        case 635625672: /*molecularWeight*/ return this.molecularWeight == null ? new Base[0] : new Base[] {this.molecularWeight}; // SubstanceSpecificationStructureIsotopeMolecularWeightComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 1654398709: // nuclideId
          this.nuclideId = castToIdentifier(value); // Identifier
          return value;
        case 739409381: // nuclideName
          this.nuclideName = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1937621033: // substitutionType
          this.substitutionType = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 1348294697: // nuclideHalfLife
          this.nuclideHalfLife = castToQuantity(value); // Quantity
          return value;
        case -1413853096: // amount
          this.amount = castToString(value); // StringType
          return value;
        case 635625672: // molecularWeight
          this.molecularWeight = (SubstanceSpecificationStructureIsotopeMolecularWeightComponent) value; // SubstanceSpecificationStructureIsotopeMolecularWeightComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("nuclideId")) {
          this.nuclideId = castToIdentifier(value); // Identifier
        } else if (name.equals("nuclideName")) {
          this.nuclideName = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("substitutionType")) {
          this.substitutionType = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("nuclideHalfLife")) {
          this.nuclideHalfLife = castToQuantity(value); // Quantity
        } else if (name.equals("amount")) {
          this.amount = castToString(value); // StringType
        } else if (name.equals("molecularWeight")) {
          this.molecularWeight = (SubstanceSpecificationStructureIsotopeMolecularWeightComponent) value; // SubstanceSpecificationStructureIsotopeMolecularWeightComponent
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1654398709:  return getNuclideId(); 
        case 739409381:  return getNuclideName(); 
        case -1937621033:  return getSubstitutionType(); 
        case 1348294697:  return getNuclideHalfLife(); 
        case -1413853096:  return getAmountElement();
        case 635625672:  return getMolecularWeight(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1654398709: /*nuclideId*/ return new String[] {"Identifier"};
        case 739409381: /*nuclideName*/ return new String[] {"CodeableConcept"};
        case -1937621033: /*substitutionType*/ return new String[] {"CodeableConcept"};
        case 1348294697: /*nuclideHalfLife*/ return new String[] {"Quantity"};
        case -1413853096: /*amount*/ return new String[] {"string"};
        case 635625672: /*molecularWeight*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("nuclideId")) {
          this.nuclideId = new Identifier();
          return this.nuclideId;
        }
        else if (name.equals("nuclideName")) {
          this.nuclideName = new CodeableConcept();
          return this.nuclideName;
        }
        else if (name.equals("substitutionType")) {
          this.substitutionType = new CodeableConcept();
          return this.substitutionType;
        }
        else if (name.equals("nuclideHalfLife")) {
          this.nuclideHalfLife = new Quantity();
          return this.nuclideHalfLife;
        }
        else if (name.equals("amount")) {
          throw new FHIRException("Cannot call addChild on a primitive type SubstanceSpecification.amount");
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
        dst.nuclideId = nuclideId == null ? null : nuclideId.copy();
        dst.nuclideName = nuclideName == null ? null : nuclideName.copy();
        dst.substitutionType = substitutionType == null ? null : substitutionType.copy();
        dst.nuclideHalfLife = nuclideHalfLife == null ? null : nuclideHalfLife.copy();
        dst.amount = amount == null ? null : amount.copy();
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
        return compareDeep(nuclideId, o.nuclideId, true) && compareDeep(nuclideName, o.nuclideName, true)
           && compareDeep(substitutionType, o.substitutionType, true) && compareDeep(nuclideHalfLife, o.nuclideHalfLife, true)
           && compareDeep(amount, o.amount, true) && compareDeep(molecularWeight, o.molecularWeight, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof SubstanceSpecificationStructureIsotopeComponent))
          return false;
        SubstanceSpecificationStructureIsotopeComponent o = (SubstanceSpecificationStructureIsotopeComponent) other_;
        return compareValues(amount, o.amount, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(nuclideId, nuclideName, substitutionType
          , nuclideHalfLife, amount, molecularWeight);
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
        @Child(name = "amount", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Used to capture quantitative values for a variety of elements. If only limits are given, the arithmetic mean would be the average. If only a single definite value for a given element is given, it would be captured in this field", formalDefinition="Used to capture quantitative values for a variety of elements. If only limits are given, the arithmetic mean would be the average. If only a single definite value for a given element is given, it would be captured in this field." )
        protected StringType amount;

        private static final long serialVersionUID = -1221185948L;

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
         * @return {@link #amount} (Used to capture quantitative values for a variety of elements. If only limits are given, the arithmetic mean would be the average. If only a single definite value for a given element is given, it would be captured in this field.). This is the underlying object with id, value and extensions. The accessor "getAmount" gives direct access to the value
         */
        public StringType getAmountElement() { 
          if (this.amount == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceSpecificationStructureIsotopeMolecularWeightComponent.amount");
            else if (Configuration.doAutoCreate())
              this.amount = new StringType(); // bb
          return this.amount;
        }

        public boolean hasAmountElement() { 
          return this.amount != null && !this.amount.isEmpty();
        }

        public boolean hasAmount() { 
          return this.amount != null && !this.amount.isEmpty();
        }

        /**
         * @param value {@link #amount} (Used to capture quantitative values for a variety of elements. If only limits are given, the arithmetic mean would be the average. If only a single definite value for a given element is given, it would be captured in this field.). This is the underlying object with id, value and extensions. The accessor "getAmount" gives direct access to the value
         */
        public SubstanceSpecificationStructureIsotopeMolecularWeightComponent setAmountElement(StringType value) { 
          this.amount = value;
          return this;
        }

        /**
         * @return Used to capture quantitative values for a variety of elements. If only limits are given, the arithmetic mean would be the average. If only a single definite value for a given element is given, it would be captured in this field.
         */
        public String getAmount() { 
          return this.amount == null ? null : this.amount.getValue();
        }

        /**
         * @param value Used to capture quantitative values for a variety of elements. If only limits are given, the arithmetic mean would be the average. If only a single definite value for a given element is given, it would be captured in this field.
         */
        public SubstanceSpecificationStructureIsotopeMolecularWeightComponent setAmount(String value) { 
          if (Utilities.noString(value))
            this.amount = null;
          else {
            if (this.amount == null)
              this.amount = new StringType();
            this.amount.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("method", "CodeableConcept", "The method by which the molecular weight was determined.", 0, 1, method));
          children.add(new Property("type", "CodeableConcept", "Type of molecular weight such as exact, average (also known as. number average), weight average.", 0, 1, type));
          children.add(new Property("amount", "string", "Used to capture quantitative values for a variety of elements. If only limits are given, the arithmetic mean would be the average. If only a single definite value for a given element is given, it would be captured in this field.", 0, 1, amount));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -1077554975: /*method*/  return new Property("method", "CodeableConcept", "The method by which the molecular weight was determined.", 0, 1, method);
          case 3575610: /*type*/  return new Property("type", "CodeableConcept", "Type of molecular weight such as exact, average (also known as. number average), weight average.", 0, 1, type);
          case -1413853096: /*amount*/  return new Property("amount", "string", "Used to capture quantitative values for a variety of elements. If only limits are given, the arithmetic mean would be the average. If only a single definite value for a given element is given, it would be captured in this field.", 0, 1, amount);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1077554975: /*method*/ return this.method == null ? new Base[0] : new Base[] {this.method}; // CodeableConcept
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case -1413853096: /*amount*/ return this.amount == null ? new Base[0] : new Base[] {this.amount}; // StringType
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
          this.amount = castToString(value); // StringType
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
          this.amount = castToString(value); // StringType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1077554975:  return getMethod(); 
        case 3575610:  return getType(); 
        case -1413853096:  return getAmountElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1077554975: /*method*/ return new String[] {"CodeableConcept"};
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case -1413853096: /*amount*/ return new String[] {"string"};
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
          throw new FHIRException("Cannot call addChild on a primitive type SubstanceSpecification.amount");
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
        return compareValues(amount, o.amount, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(method, type, amount);
      }

  public String fhirType() {
    return "SubstanceSpecification.structure.isotope.molecularWeight";

  }

  }

    @Block()
    public static class SubstanceSpecificationStructureStructuralRepresentationComponent extends BackboneElement implements IBaseBackboneElement {
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
      public SubstanceSpecificationStructureStructuralRepresentationComponent() {
        super();
      }

        /**
         * @return {@link #type} (The type of structure (e.g. Full, Partial, Representative).)
         */
        public CodeableConcept getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceSpecificationStructureStructuralRepresentationComponent.type");
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
        public SubstanceSpecificationStructureStructuralRepresentationComponent setType(CodeableConcept value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #representation} (The structural representation as text string in a format e.g. InChI, SMILES, MOLFILE, CDX.). This is the underlying object with id, value and extensions. The accessor "getRepresentation" gives direct access to the value
         */
        public StringType getRepresentationElement() { 
          if (this.representation == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceSpecificationStructureStructuralRepresentationComponent.representation");
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
        public SubstanceSpecificationStructureStructuralRepresentationComponent setRepresentationElement(StringType value) { 
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
        public SubstanceSpecificationStructureStructuralRepresentationComponent setRepresentation(String value) { 
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
              throw new Error("Attempt to auto-create SubstanceSpecificationStructureStructuralRepresentationComponent.attachment");
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
        public SubstanceSpecificationStructureStructuralRepresentationComponent setAttachment(Attachment value) { 
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

      public SubstanceSpecificationStructureStructuralRepresentationComponent copy() {
        SubstanceSpecificationStructureStructuralRepresentationComponent dst = new SubstanceSpecificationStructureStructuralRepresentationComponent();
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
        if (!(other_ instanceof SubstanceSpecificationStructureStructuralRepresentationComponent))
          return false;
        SubstanceSpecificationStructureStructuralRepresentationComponent o = (SubstanceSpecificationStructureStructuralRepresentationComponent) other_;
        return compareDeep(type, o.type, true) && compareDeep(representation, o.representation, true) && compareDeep(attachment, o.attachment, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof SubstanceSpecificationStructureStructuralRepresentationComponent))
          return false;
        SubstanceSpecificationStructureStructuralRepresentationComponent o = (SubstanceSpecificationStructureStructuralRepresentationComponent) other_;
        return compareValues(representation, o.representation, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(type, representation, attachment
          );
      }

  public String fhirType() {
    return "SubstanceSpecification.structure.structuralRepresentation";

  }

  }

    @Block()
    public static class SubstanceSpecificationSubstanceCodeComponent extends BackboneElement implements IBaseBackboneElement {
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
        @Child(name = "referenceSource", type = {StringType.class}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Supporting literature", formalDefinition="Supporting literature." )
        protected List<StringType> referenceSource;

        private static final long serialVersionUID = 1936663817L;

    /**
     * Constructor
     */
      public SubstanceSpecificationSubstanceCodeComponent() {
        super();
      }

        /**
         * @return {@link #code} (The specific code.)
         */
        public CodeableConcept getCode() { 
          if (this.code == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceSpecificationSubstanceCodeComponent.code");
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
        public SubstanceSpecificationSubstanceCodeComponent setCode(CodeableConcept value) { 
          this.code = value;
          return this;
        }

        /**
         * @return {@link #status} (Status of the code assignment.)
         */
        public CodeableConcept getStatus() { 
          if (this.status == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceSpecificationSubstanceCodeComponent.status");
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
        public SubstanceSpecificationSubstanceCodeComponent setStatus(CodeableConcept value) { 
          this.status = value;
          return this;
        }

        /**
         * @return {@link #statusDate} (The date at which the code status is changed as part of the terminology maintenance.). This is the underlying object with id, value and extensions. The accessor "getStatusDate" gives direct access to the value
         */
        public DateTimeType getStatusDateElement() { 
          if (this.statusDate == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceSpecificationSubstanceCodeComponent.statusDate");
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
        public SubstanceSpecificationSubstanceCodeComponent setStatusDateElement(DateTimeType value) { 
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
        public SubstanceSpecificationSubstanceCodeComponent setStatusDate(Date value) { 
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
              throw new Error("Attempt to auto-create SubstanceSpecificationSubstanceCodeComponent.comment");
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
        public SubstanceSpecificationSubstanceCodeComponent setCommentElement(StringType value) { 
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
        public SubstanceSpecificationSubstanceCodeComponent setComment(String value) { 
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
         * @return {@link #referenceSource} (Supporting literature.)
         */
        public List<StringType> getReferenceSource() { 
          if (this.referenceSource == null)
            this.referenceSource = new ArrayList<StringType>();
          return this.referenceSource;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public SubstanceSpecificationSubstanceCodeComponent setReferenceSource(List<StringType> theReferenceSource) { 
          this.referenceSource = theReferenceSource;
          return this;
        }

        public boolean hasReferenceSource() { 
          if (this.referenceSource == null)
            return false;
          for (StringType item : this.referenceSource)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #referenceSource} (Supporting literature.)
         */
        public StringType addReferenceSourceElement() {//2 
          StringType t = new StringType();
          if (this.referenceSource == null)
            this.referenceSource = new ArrayList<StringType>();
          this.referenceSource.add(t);
          return t;
        }

        /**
         * @param value {@link #referenceSource} (Supporting literature.)
         */
        public SubstanceSpecificationSubstanceCodeComponent addReferenceSource(String value) { //1
          StringType t = new StringType();
          t.setValue(value);
          if (this.referenceSource == null)
            this.referenceSource = new ArrayList<StringType>();
          this.referenceSource.add(t);
          return this;
        }

        /**
         * @param value {@link #referenceSource} (Supporting literature.)
         */
        public boolean hasReferenceSource(String value) { 
          if (this.referenceSource == null)
            return false;
          for (StringType v : this.referenceSource)
            if (v.getValue().equals(value)) // string
              return true;
          return false;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("code", "CodeableConcept", "The specific code.", 0, 1, code));
          children.add(new Property("status", "CodeableConcept", "Status of the code assignment.", 0, 1, status));
          children.add(new Property("statusDate", "dateTime", "The date at which the code status is changed as part of the terminology maintenance.", 0, 1, statusDate));
          children.add(new Property("comment", "string", "Any comment can be provided in this field, if necessary.", 0, 1, comment));
          children.add(new Property("referenceSource", "string", "Supporting literature.", 0, java.lang.Integer.MAX_VALUE, referenceSource));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3059181: /*code*/  return new Property("code", "CodeableConcept", "The specific code.", 0, 1, code);
          case -892481550: /*status*/  return new Property("status", "CodeableConcept", "Status of the code assignment.", 0, 1, status);
          case 247524032: /*statusDate*/  return new Property("statusDate", "dateTime", "The date at which the code status is changed as part of the terminology maintenance.", 0, 1, statusDate);
          case 950398559: /*comment*/  return new Property("comment", "string", "Any comment can be provided in this field, if necessary.", 0, 1, comment);
          case 882421574: /*referenceSource*/  return new Property("referenceSource", "string", "Supporting literature.", 0, java.lang.Integer.MAX_VALUE, referenceSource);
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
        case 882421574: /*referenceSource*/ return this.referenceSource == null ? new Base[0] : this.referenceSource.toArray(new Base[this.referenceSource.size()]); // StringType
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
        case 882421574: // referenceSource
          this.getReferenceSource().add(castToString(value)); // StringType
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
        } else if (name.equals("referenceSource")) {
          this.getReferenceSource().add(castToString(value));
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
        case 882421574:  return addReferenceSourceElement();
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
        case 882421574: /*referenceSource*/ return new String[] {"string"};
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
        else if (name.equals("referenceSource")) {
          throw new FHIRException("Cannot call addChild on a primitive type SubstanceSpecification.referenceSource");
        }
        else
          return super.addChild(name);
      }

      public SubstanceSpecificationSubstanceCodeComponent copy() {
        SubstanceSpecificationSubstanceCodeComponent dst = new SubstanceSpecificationSubstanceCodeComponent();
        copyValues(dst);
        dst.code = code == null ? null : code.copy();
        dst.status = status == null ? null : status.copy();
        dst.statusDate = statusDate == null ? null : statusDate.copy();
        dst.comment = comment == null ? null : comment.copy();
        if (referenceSource != null) {
          dst.referenceSource = new ArrayList<StringType>();
          for (StringType i : referenceSource)
            dst.referenceSource.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof SubstanceSpecificationSubstanceCodeComponent))
          return false;
        SubstanceSpecificationSubstanceCodeComponent o = (SubstanceSpecificationSubstanceCodeComponent) other_;
        return compareDeep(code, o.code, true) && compareDeep(status, o.status, true) && compareDeep(statusDate, o.statusDate, true)
           && compareDeep(comment, o.comment, true) && compareDeep(referenceSource, o.referenceSource, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof SubstanceSpecificationSubstanceCodeComponent))
          return false;
        SubstanceSpecificationSubstanceCodeComponent o = (SubstanceSpecificationSubstanceCodeComponent) other_;
        return compareValues(statusDate, o.statusDate, true) && compareValues(comment, o.comment, true) && compareValues(referenceSource, o.referenceSource, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(code, status, statusDate
          , comment, referenceSource);
      }

  public String fhirType() {
    return "SubstanceSpecification.substanceCode";

  }

  }

    @Block()
    public static class SubstanceSpecificationSubstanceNameComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The actual name.
         */
        @Child(name = "name", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The actual name", formalDefinition="The actual name." )
        protected StringType name;

        /**
         * Name type.
         */
        @Child(name = "type", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Name type", formalDefinition="Name type." )
        protected CodeableConcept type;

        /**
         * Language of the name.
         */
        @Child(name = "language", type = {CodeableConcept.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Language of the name", formalDefinition="Language of the name." )
        protected List<CodeableConcept> language;

        /**
         * The use context of this name for example if there is a different name a drug active ingredient as opposed to a food colour additive.
         */
        @Child(name = "domain", type = {CodeableConcept.class}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="The use context of this name for example if there is a different name a drug active ingredient as opposed to a food colour additive", formalDefinition="The use context of this name for example if there is a different name a drug active ingredient as opposed to a food colour additive." )
        protected List<CodeableConcept> domain;

        /**
         * The jurisdiction where this name applies.
         */
        @Child(name = "jurisdiction", type = {CodeableConcept.class}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="The jurisdiction where this name applies", formalDefinition="The jurisdiction where this name applies." )
        protected List<CodeableConcept> jurisdiction;

        /**
         * Details of the official nature of this name.
         */
        @Child(name = "officialName", type = {}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Details of the official nature of this name", formalDefinition="Details of the official nature of this name." )
        protected List<SubstanceSpecificationSubstanceNameOfficialNameComponent> officialName;

        /**
         * Supporting literature.
         */
        @Child(name = "referenceSource", type = {StringType.class}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Supporting literature", formalDefinition="Supporting literature." )
        protected List<StringType> referenceSource;

        private static final long serialVersionUID = -1690760755L;

    /**
     * Constructor
     */
      public SubstanceSpecificationSubstanceNameComponent() {
        super();
      }

        /**
         * @return {@link #name} (The actual name.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StringType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceSpecificationSubstanceNameComponent.name");
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
        public SubstanceSpecificationSubstanceNameComponent setNameElement(StringType value) { 
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
        public SubstanceSpecificationSubstanceNameComponent setName(String value) { 
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
         * @return {@link #type} (Name type.)
         */
        public CodeableConcept getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceSpecificationSubstanceNameComponent.type");
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
        public SubstanceSpecificationSubstanceNameComponent setType(CodeableConcept value) { 
          this.type = value;
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
        public SubstanceSpecificationSubstanceNameComponent setLanguage(List<CodeableConcept> theLanguage) { 
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

        public SubstanceSpecificationSubstanceNameComponent addLanguage(CodeableConcept t) { //3
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
        public SubstanceSpecificationSubstanceNameComponent setDomain(List<CodeableConcept> theDomain) { 
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

        public SubstanceSpecificationSubstanceNameComponent addDomain(CodeableConcept t) { //3
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
        public SubstanceSpecificationSubstanceNameComponent setJurisdiction(List<CodeableConcept> theJurisdiction) { 
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

        public SubstanceSpecificationSubstanceNameComponent addJurisdiction(CodeableConcept t) { //3
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
         * @return {@link #officialName} (Details of the official nature of this name.)
         */
        public List<SubstanceSpecificationSubstanceNameOfficialNameComponent> getOfficialName() { 
          if (this.officialName == null)
            this.officialName = new ArrayList<SubstanceSpecificationSubstanceNameOfficialNameComponent>();
          return this.officialName;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public SubstanceSpecificationSubstanceNameComponent setOfficialName(List<SubstanceSpecificationSubstanceNameOfficialNameComponent> theOfficialName) { 
          this.officialName = theOfficialName;
          return this;
        }

        public boolean hasOfficialName() { 
          if (this.officialName == null)
            return false;
          for (SubstanceSpecificationSubstanceNameOfficialNameComponent item : this.officialName)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public SubstanceSpecificationSubstanceNameOfficialNameComponent addOfficialName() { //3
          SubstanceSpecificationSubstanceNameOfficialNameComponent t = new SubstanceSpecificationSubstanceNameOfficialNameComponent();
          if (this.officialName == null)
            this.officialName = new ArrayList<SubstanceSpecificationSubstanceNameOfficialNameComponent>();
          this.officialName.add(t);
          return t;
        }

        public SubstanceSpecificationSubstanceNameComponent addOfficialName(SubstanceSpecificationSubstanceNameOfficialNameComponent t) { //3
          if (t == null)
            return this;
          if (this.officialName == null)
            this.officialName = new ArrayList<SubstanceSpecificationSubstanceNameOfficialNameComponent>();
          this.officialName.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #officialName}, creating it if it does not already exist
         */
        public SubstanceSpecificationSubstanceNameOfficialNameComponent getOfficialNameFirstRep() { 
          if (getOfficialName().isEmpty()) {
            addOfficialName();
          }
          return getOfficialName().get(0);
        }

        /**
         * @return {@link #referenceSource} (Supporting literature.)
         */
        public List<StringType> getReferenceSource() { 
          if (this.referenceSource == null)
            this.referenceSource = new ArrayList<StringType>();
          return this.referenceSource;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public SubstanceSpecificationSubstanceNameComponent setReferenceSource(List<StringType> theReferenceSource) { 
          this.referenceSource = theReferenceSource;
          return this;
        }

        public boolean hasReferenceSource() { 
          if (this.referenceSource == null)
            return false;
          for (StringType item : this.referenceSource)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #referenceSource} (Supporting literature.)
         */
        public StringType addReferenceSourceElement() {//2 
          StringType t = new StringType();
          if (this.referenceSource == null)
            this.referenceSource = new ArrayList<StringType>();
          this.referenceSource.add(t);
          return t;
        }

        /**
         * @param value {@link #referenceSource} (Supporting literature.)
         */
        public SubstanceSpecificationSubstanceNameComponent addReferenceSource(String value) { //1
          StringType t = new StringType();
          t.setValue(value);
          if (this.referenceSource == null)
            this.referenceSource = new ArrayList<StringType>();
          this.referenceSource.add(t);
          return this;
        }

        /**
         * @param value {@link #referenceSource} (Supporting literature.)
         */
        public boolean hasReferenceSource(String value) { 
          if (this.referenceSource == null)
            return false;
          for (StringType v : this.referenceSource)
            if (v.getValue().equals(value)) // string
              return true;
          return false;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("name", "string", "The actual name.", 0, 1, name));
          children.add(new Property("type", "CodeableConcept", "Name type.", 0, 1, type));
          children.add(new Property("language", "CodeableConcept", "Language of the name.", 0, java.lang.Integer.MAX_VALUE, language));
          children.add(new Property("domain", "CodeableConcept", "The use context of this name for example if there is a different name a drug active ingredient as opposed to a food colour additive.", 0, java.lang.Integer.MAX_VALUE, domain));
          children.add(new Property("jurisdiction", "CodeableConcept", "The jurisdiction where this name applies.", 0, java.lang.Integer.MAX_VALUE, jurisdiction));
          children.add(new Property("officialName", "", "Details of the official nature of this name.", 0, java.lang.Integer.MAX_VALUE, officialName));
          children.add(new Property("referenceSource", "string", "Supporting literature.", 0, java.lang.Integer.MAX_VALUE, referenceSource));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3373707: /*name*/  return new Property("name", "string", "The actual name.", 0, 1, name);
          case 3575610: /*type*/  return new Property("type", "CodeableConcept", "Name type.", 0, 1, type);
          case -1613589672: /*language*/  return new Property("language", "CodeableConcept", "Language of the name.", 0, java.lang.Integer.MAX_VALUE, language);
          case -1326197564: /*domain*/  return new Property("domain", "CodeableConcept", "The use context of this name for example if there is a different name a drug active ingredient as opposed to a food colour additive.", 0, java.lang.Integer.MAX_VALUE, domain);
          case -507075711: /*jurisdiction*/  return new Property("jurisdiction", "CodeableConcept", "The jurisdiction where this name applies.", 0, java.lang.Integer.MAX_VALUE, jurisdiction);
          case 1486494742: /*officialName*/  return new Property("officialName", "", "Details of the official nature of this name.", 0, java.lang.Integer.MAX_VALUE, officialName);
          case 882421574: /*referenceSource*/  return new Property("referenceSource", "string", "Supporting literature.", 0, java.lang.Integer.MAX_VALUE, referenceSource);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case -1613589672: /*language*/ return this.language == null ? new Base[0] : this.language.toArray(new Base[this.language.size()]); // CodeableConcept
        case -1326197564: /*domain*/ return this.domain == null ? new Base[0] : this.domain.toArray(new Base[this.domain.size()]); // CodeableConcept
        case -507075711: /*jurisdiction*/ return this.jurisdiction == null ? new Base[0] : this.jurisdiction.toArray(new Base[this.jurisdiction.size()]); // CodeableConcept
        case 1486494742: /*officialName*/ return this.officialName == null ? new Base[0] : this.officialName.toArray(new Base[this.officialName.size()]); // SubstanceSpecificationSubstanceNameOfficialNameComponent
        case 882421574: /*referenceSource*/ return this.referenceSource == null ? new Base[0] : this.referenceSource.toArray(new Base[this.referenceSource.size()]); // StringType
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
        case -1613589672: // language
          this.getLanguage().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -1326197564: // domain
          this.getDomain().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -507075711: // jurisdiction
          this.getJurisdiction().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 1486494742: // officialName
          this.getOfficialName().add((SubstanceSpecificationSubstanceNameOfficialNameComponent) value); // SubstanceSpecificationSubstanceNameOfficialNameComponent
          return value;
        case 882421574: // referenceSource
          this.getReferenceSource().add(castToString(value)); // StringType
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
        } else if (name.equals("language")) {
          this.getLanguage().add(castToCodeableConcept(value));
        } else if (name.equals("domain")) {
          this.getDomain().add(castToCodeableConcept(value));
        } else if (name.equals("jurisdiction")) {
          this.getJurisdiction().add(castToCodeableConcept(value));
        } else if (name.equals("officialName")) {
          this.getOfficialName().add((SubstanceSpecificationSubstanceNameOfficialNameComponent) value);
        } else if (name.equals("referenceSource")) {
          this.getReferenceSource().add(castToString(value));
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3373707:  return getNameElement();
        case 3575610:  return getType(); 
        case -1613589672:  return addLanguage(); 
        case -1326197564:  return addDomain(); 
        case -507075711:  return addJurisdiction(); 
        case 1486494742:  return addOfficialName(); 
        case 882421574:  return addReferenceSourceElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3373707: /*name*/ return new String[] {"string"};
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case -1613589672: /*language*/ return new String[] {"CodeableConcept"};
        case -1326197564: /*domain*/ return new String[] {"CodeableConcept"};
        case -507075711: /*jurisdiction*/ return new String[] {"CodeableConcept"};
        case 1486494742: /*officialName*/ return new String[] {};
        case 882421574: /*referenceSource*/ return new String[] {"string"};
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
        else if (name.equals("language")) {
          return addLanguage();
        }
        else if (name.equals("domain")) {
          return addDomain();
        }
        else if (name.equals("jurisdiction")) {
          return addJurisdiction();
        }
        else if (name.equals("officialName")) {
          return addOfficialName();
        }
        else if (name.equals("referenceSource")) {
          throw new FHIRException("Cannot call addChild on a primitive type SubstanceSpecification.referenceSource");
        }
        else
          return super.addChild(name);
      }

      public SubstanceSpecificationSubstanceNameComponent copy() {
        SubstanceSpecificationSubstanceNameComponent dst = new SubstanceSpecificationSubstanceNameComponent();
        copyValues(dst);
        dst.name = name == null ? null : name.copy();
        dst.type = type == null ? null : type.copy();
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
        if (officialName != null) {
          dst.officialName = new ArrayList<SubstanceSpecificationSubstanceNameOfficialNameComponent>();
          for (SubstanceSpecificationSubstanceNameOfficialNameComponent i : officialName)
            dst.officialName.add(i.copy());
        };
        if (referenceSource != null) {
          dst.referenceSource = new ArrayList<StringType>();
          for (StringType i : referenceSource)
            dst.referenceSource.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof SubstanceSpecificationSubstanceNameComponent))
          return false;
        SubstanceSpecificationSubstanceNameComponent o = (SubstanceSpecificationSubstanceNameComponent) other_;
        return compareDeep(name, o.name, true) && compareDeep(type, o.type, true) && compareDeep(language, o.language, true)
           && compareDeep(domain, o.domain, true) && compareDeep(jurisdiction, o.jurisdiction, true) && compareDeep(officialName, o.officialName, true)
           && compareDeep(referenceSource, o.referenceSource, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof SubstanceSpecificationSubstanceNameComponent))
          return false;
        SubstanceSpecificationSubstanceNameComponent o = (SubstanceSpecificationSubstanceNameComponent) other_;
        return compareValues(name, o.name, true) && compareValues(referenceSource, o.referenceSource, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(name, type, language, domain
          , jurisdiction, officialName, referenceSource);
      }

  public String fhirType() {
    return "SubstanceSpecification.substanceName";

  }

  }

    @Block()
    public static class SubstanceSpecificationSubstanceNameOfficialNameComponent extends BackboneElement implements IBaseBackboneElement {
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
      public SubstanceSpecificationSubstanceNameOfficialNameComponent() {
        super();
      }

        /**
         * @return {@link #authority} (Which authority uses this official name.)
         */
        public CodeableConcept getAuthority() { 
          if (this.authority == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceSpecificationSubstanceNameOfficialNameComponent.authority");
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
        public SubstanceSpecificationSubstanceNameOfficialNameComponent setAuthority(CodeableConcept value) { 
          this.authority = value;
          return this;
        }

        /**
         * @return {@link #status} (The status of the official name.)
         */
        public CodeableConcept getStatus() { 
          if (this.status == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceSpecificationSubstanceNameOfficialNameComponent.status");
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
        public SubstanceSpecificationSubstanceNameOfficialNameComponent setStatus(CodeableConcept value) { 
          this.status = value;
          return this;
        }

        /**
         * @return {@link #date} (Date of official name change.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
         */
        public DateTimeType getDateElement() { 
          if (this.date == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubstanceSpecificationSubstanceNameOfficialNameComponent.date");
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
        public SubstanceSpecificationSubstanceNameOfficialNameComponent setDateElement(DateTimeType value) { 
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
        public SubstanceSpecificationSubstanceNameOfficialNameComponent setDate(Date value) { 
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

      public SubstanceSpecificationSubstanceNameOfficialNameComponent copy() {
        SubstanceSpecificationSubstanceNameOfficialNameComponent dst = new SubstanceSpecificationSubstanceNameOfficialNameComponent();
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
        if (!(other_ instanceof SubstanceSpecificationSubstanceNameOfficialNameComponent))
          return false;
        SubstanceSpecificationSubstanceNameOfficialNameComponent o = (SubstanceSpecificationSubstanceNameOfficialNameComponent) other_;
        return compareDeep(authority, o.authority, true) && compareDeep(status, o.status, true) && compareDeep(date, o.date, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof SubstanceSpecificationSubstanceNameOfficialNameComponent))
          return false;
        SubstanceSpecificationSubstanceNameOfficialNameComponent o = (SubstanceSpecificationSubstanceNameOfficialNameComponent) other_;
        return compareValues(date, o.date, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(authority, status, date
          );
      }

  public String fhirType() {
    return "SubstanceSpecification.substanceName.officialName";

  }

  }

    /**
     * Textual comment.
     */
    @Child(name = "comment", type = {StringType.class}, order=0, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Textual comment", formalDefinition="Textual comment." )
    protected StringType comment;

    /**
     * Chemicals may be stoichiometric or non-stoichiometric.
     */
    @Child(name = "stoichiometric", type = {BooleanType.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Chemicals may be stoichiometric or non-stoichiometric", formalDefinition="Chemicals may be stoichiometric or non-stoichiometric." )
    protected BooleanType stoichiometric;

    /**
     * Identifier by which this substance is known.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Identifier by which this substance is known", formalDefinition="Identifier by which this substance is known." )
    protected Identifier identifier;

    /**
     * High level categorization, e.g. polymer or nucleic acid.
     */
    @Child(name = "type", type = {CodeableConcept.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="High level categorization, e.g. polymer or nucleic acid", formalDefinition="High level categorization, e.g. polymer or nucleic acid." )
    protected CodeableConcept type;

    /**
     * Supporting literature.
     */
    @Child(name = "referenceSource", type = {StringType.class}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Supporting literature", formalDefinition="Supporting literature." )
    protected List<StringType> referenceSource;

    /**
     * Moiety, for structural modifications.
     */
    @Child(name = "moiety", type = {}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Moiety, for structural modifications", formalDefinition="Moiety, for structural modifications." )
    protected List<SubstanceSpecificationMoietyComponent> moiety;

    /**
     * General specifications for this substance, including how it is related to other substances.
     */
    @Child(name = "property", type = {}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="General specifications for this substance, including how it is related to other substances", formalDefinition="General specifications for this substance, including how it is related to other substances." )
    protected List<SubstanceSpecificationPropertyComponent> property;

    /**
     * General information detailing this substance.
     */
    @Child(name = "referenceInformation", type = {SubstanceReferenceInformation.class}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="General information detailing this substance", formalDefinition="General information detailing this substance." )
    protected Reference referenceInformation;

    /**
     * The actual object that is the target of the reference (General information detailing this substance.)
     */
    protected SubstanceReferenceInformation referenceInformationTarget;

    /**
     * Structural information.
     */
    @Child(name = "structure", type = {}, order=8, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Structural information", formalDefinition="Structural information." )
    protected SubstanceSpecificationStructureComponent structure;

    /**
     * Codes associated with the substance.
     */
    @Child(name = "substanceCode", type = {}, order=9, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Codes associated with the substance", formalDefinition="Codes associated with the substance." )
    protected List<SubstanceSpecificationSubstanceCodeComponent> substanceCode;

    /**
     * Names applicable to this substence.
     */
    @Child(name = "substanceName", type = {}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Names applicable to this substence", formalDefinition="Names applicable to this substence." )
    protected List<SubstanceSpecificationSubstanceNameComponent> substanceName;

    /**
     * The molecular weight or weight range (for proteins, polymers or nucleic acids).
     */
    @Child(name = "molecularWeight", type = {SubstanceSpecificationStructureIsotopeMolecularWeightComponent.class}, order=11, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="The molecular weight or weight range (for proteins, polymers or nucleic acids)", formalDefinition="The molecular weight or weight range (for proteins, polymers or nucleic acids)." )
    protected List<SubstanceSpecificationStructureIsotopeMolecularWeightComponent> molecularWeight;

    /**
     * Data items specific to polymers.
     */
    @Child(name = "polymer", type = {SubstancePolymer.class}, order=12, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Data items specific to polymers", formalDefinition="Data items specific to polymers." )
    protected Reference polymer;

    /**
     * The actual object that is the target of the reference (Data items specific to polymers.)
     */
    protected SubstancePolymer polymerTarget;

    private static final long serialVersionUID = -55630281L;

  /**
   * Constructor
   */
    public SubstanceSpecification() {
      super();
    }

    /**
     * @return {@link #comment} (Textual comment.). This is the underlying object with id, value and extensions. The accessor "getComment" gives direct access to the value
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
     * @param value {@link #comment} (Textual comment.). This is the underlying object with id, value and extensions. The accessor "getComment" gives direct access to the value
     */
    public SubstanceSpecification setCommentElement(StringType value) { 
      this.comment = value;
      return this;
    }

    /**
     * @return Textual comment.
     */
    public String getComment() { 
      return this.comment == null ? null : this.comment.getValue();
    }

    /**
     * @param value Textual comment.
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
     * @return {@link #stoichiometric} (Chemicals may be stoichiometric or non-stoichiometric.). This is the underlying object with id, value and extensions. The accessor "getStoichiometric" gives direct access to the value
     */
    public BooleanType getStoichiometricElement() { 
      if (this.stoichiometric == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SubstanceSpecification.stoichiometric");
        else if (Configuration.doAutoCreate())
          this.stoichiometric = new BooleanType(); // bb
      return this.stoichiometric;
    }

    public boolean hasStoichiometricElement() { 
      return this.stoichiometric != null && !this.stoichiometric.isEmpty();
    }

    public boolean hasStoichiometric() { 
      return this.stoichiometric != null && !this.stoichiometric.isEmpty();
    }

    /**
     * @param value {@link #stoichiometric} (Chemicals may be stoichiometric or non-stoichiometric.). This is the underlying object with id, value and extensions. The accessor "getStoichiometric" gives direct access to the value
     */
    public SubstanceSpecification setStoichiometricElement(BooleanType value) { 
      this.stoichiometric = value;
      return this;
    }

    /**
     * @return Chemicals may be stoichiometric or non-stoichiometric.
     */
    public boolean getStoichiometric() { 
      return this.stoichiometric == null || this.stoichiometric.isEmpty() ? false : this.stoichiometric.getValue();
    }

    /**
     * @param value Chemicals may be stoichiometric or non-stoichiometric.
     */
    public SubstanceSpecification setStoichiometric(boolean value) { 
        if (this.stoichiometric == null)
          this.stoichiometric = new BooleanType();
        this.stoichiometric.setValue(value);
      return this;
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
     * @return {@link #referenceSource} (Supporting literature.)
     */
    public List<StringType> getReferenceSource() { 
      if (this.referenceSource == null)
        this.referenceSource = new ArrayList<StringType>();
      return this.referenceSource;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public SubstanceSpecification setReferenceSource(List<StringType> theReferenceSource) { 
      this.referenceSource = theReferenceSource;
      return this;
    }

    public boolean hasReferenceSource() { 
      if (this.referenceSource == null)
        return false;
      for (StringType item : this.referenceSource)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #referenceSource} (Supporting literature.)
     */
    public StringType addReferenceSourceElement() {//2 
      StringType t = new StringType();
      if (this.referenceSource == null)
        this.referenceSource = new ArrayList<StringType>();
      this.referenceSource.add(t);
      return t;
    }

    /**
     * @param value {@link #referenceSource} (Supporting literature.)
     */
    public SubstanceSpecification addReferenceSource(String value) { //1
      StringType t = new StringType();
      t.setValue(value);
      if (this.referenceSource == null)
        this.referenceSource = new ArrayList<StringType>();
      this.referenceSource.add(t);
      return this;
    }

    /**
     * @param value {@link #referenceSource} (Supporting literature.)
     */
    public boolean hasReferenceSource(String value) { 
      if (this.referenceSource == null)
        return false;
      for (StringType v : this.referenceSource)
        if (v.getValue().equals(value)) // string
          return true;
      return false;
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
     * @return {@link #substanceCode} (Codes associated with the substance.)
     */
    public List<SubstanceSpecificationSubstanceCodeComponent> getSubstanceCode() { 
      if (this.substanceCode == null)
        this.substanceCode = new ArrayList<SubstanceSpecificationSubstanceCodeComponent>();
      return this.substanceCode;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public SubstanceSpecification setSubstanceCode(List<SubstanceSpecificationSubstanceCodeComponent> theSubstanceCode) { 
      this.substanceCode = theSubstanceCode;
      return this;
    }

    public boolean hasSubstanceCode() { 
      if (this.substanceCode == null)
        return false;
      for (SubstanceSpecificationSubstanceCodeComponent item : this.substanceCode)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public SubstanceSpecificationSubstanceCodeComponent addSubstanceCode() { //3
      SubstanceSpecificationSubstanceCodeComponent t = new SubstanceSpecificationSubstanceCodeComponent();
      if (this.substanceCode == null)
        this.substanceCode = new ArrayList<SubstanceSpecificationSubstanceCodeComponent>();
      this.substanceCode.add(t);
      return t;
    }

    public SubstanceSpecification addSubstanceCode(SubstanceSpecificationSubstanceCodeComponent t) { //3
      if (t == null)
        return this;
      if (this.substanceCode == null)
        this.substanceCode = new ArrayList<SubstanceSpecificationSubstanceCodeComponent>();
      this.substanceCode.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #substanceCode}, creating it if it does not already exist
     */
    public SubstanceSpecificationSubstanceCodeComponent getSubstanceCodeFirstRep() { 
      if (getSubstanceCode().isEmpty()) {
        addSubstanceCode();
      }
      return getSubstanceCode().get(0);
    }

    /**
     * @return {@link #substanceName} (Names applicable to this substence.)
     */
    public List<SubstanceSpecificationSubstanceNameComponent> getSubstanceName() { 
      if (this.substanceName == null)
        this.substanceName = new ArrayList<SubstanceSpecificationSubstanceNameComponent>();
      return this.substanceName;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public SubstanceSpecification setSubstanceName(List<SubstanceSpecificationSubstanceNameComponent> theSubstanceName) { 
      this.substanceName = theSubstanceName;
      return this;
    }

    public boolean hasSubstanceName() { 
      if (this.substanceName == null)
        return false;
      for (SubstanceSpecificationSubstanceNameComponent item : this.substanceName)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public SubstanceSpecificationSubstanceNameComponent addSubstanceName() { //3
      SubstanceSpecificationSubstanceNameComponent t = new SubstanceSpecificationSubstanceNameComponent();
      if (this.substanceName == null)
        this.substanceName = new ArrayList<SubstanceSpecificationSubstanceNameComponent>();
      this.substanceName.add(t);
      return t;
    }

    public SubstanceSpecification addSubstanceName(SubstanceSpecificationSubstanceNameComponent t) { //3
      if (t == null)
        return this;
      if (this.substanceName == null)
        this.substanceName = new ArrayList<SubstanceSpecificationSubstanceNameComponent>();
      this.substanceName.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #substanceName}, creating it if it does not already exist
     */
    public SubstanceSpecificationSubstanceNameComponent getSubstanceNameFirstRep() { 
      if (getSubstanceName().isEmpty()) {
        addSubstanceName();
      }
      return getSubstanceName().get(0);
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

      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("comment", "string", "Textual comment.", 0, 1, comment));
        children.add(new Property("stoichiometric", "boolean", "Chemicals may be stoichiometric or non-stoichiometric.", 0, 1, stoichiometric));
        children.add(new Property("identifier", "Identifier", "Identifier by which this substance is known.", 0, 1, identifier));
        children.add(new Property("type", "CodeableConcept", "High level categorization, e.g. polymer or nucleic acid.", 0, 1, type));
        children.add(new Property("referenceSource", "string", "Supporting literature.", 0, java.lang.Integer.MAX_VALUE, referenceSource));
        children.add(new Property("moiety", "", "Moiety, for structural modifications.", 0, java.lang.Integer.MAX_VALUE, moiety));
        children.add(new Property("property", "", "General specifications for this substance, including how it is related to other substances.", 0, java.lang.Integer.MAX_VALUE, property));
        children.add(new Property("referenceInformation", "Reference(SubstanceReferenceInformation)", "General information detailing this substance.", 0, 1, referenceInformation));
        children.add(new Property("structure", "", "Structural information.", 0, 1, structure));
        children.add(new Property("substanceCode", "", "Codes associated with the substance.", 0, java.lang.Integer.MAX_VALUE, substanceCode));
        children.add(new Property("substanceName", "", "Names applicable to this substence.", 0, java.lang.Integer.MAX_VALUE, substanceName));
        children.add(new Property("molecularWeight", "@SubstanceSpecification.structure.isotope.molecularWeight", "The molecular weight or weight range (for proteins, polymers or nucleic acids).", 0, java.lang.Integer.MAX_VALUE, molecularWeight));
        children.add(new Property("polymer", "Reference(SubstancePolymer)", "Data items specific to polymers.", 0, 1, polymer));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case 950398559: /*comment*/  return new Property("comment", "string", "Textual comment.", 0, 1, comment);
        case -251826186: /*stoichiometric*/  return new Property("stoichiometric", "boolean", "Chemicals may be stoichiometric or non-stoichiometric.", 0, 1, stoichiometric);
        case -1618432855: /*identifier*/  return new Property("identifier", "Identifier", "Identifier by which this substance is known.", 0, 1, identifier);
        case 3575610: /*type*/  return new Property("type", "CodeableConcept", "High level categorization, e.g. polymer or nucleic acid.", 0, 1, type);
        case 882421574: /*referenceSource*/  return new Property("referenceSource", "string", "Supporting literature.", 0, java.lang.Integer.MAX_VALUE, referenceSource);
        case -1068650173: /*moiety*/  return new Property("moiety", "", "Moiety, for structural modifications.", 0, java.lang.Integer.MAX_VALUE, moiety);
        case -993141291: /*property*/  return new Property("property", "", "General specifications for this substance, including how it is related to other substances.", 0, java.lang.Integer.MAX_VALUE, property);
        case -2117930783: /*referenceInformation*/  return new Property("referenceInformation", "Reference(SubstanceReferenceInformation)", "General information detailing this substance.", 0, 1, referenceInformation);
        case 144518515: /*structure*/  return new Property("structure", "", "Structural information.", 0, 1, structure);
        case 1517793149: /*substanceCode*/  return new Property("substanceCode", "", "Codes associated with the substance.", 0, java.lang.Integer.MAX_VALUE, substanceCode);
        case 1518107675: /*substanceName*/  return new Property("substanceName", "", "Names applicable to this substence.", 0, java.lang.Integer.MAX_VALUE, substanceName);
        case 635625672: /*molecularWeight*/  return new Property("molecularWeight", "@SubstanceSpecification.structure.isotope.molecularWeight", "The molecular weight or weight range (for proteins, polymers or nucleic acids).", 0, java.lang.Integer.MAX_VALUE, molecularWeight);
        case -397514098: /*polymer*/  return new Property("polymer", "Reference(SubstancePolymer)", "Data items specific to polymers.", 0, 1, polymer);
        default: return super.getNamedProperty(_hash, _name, _checkValid);
        }

      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 950398559: /*comment*/ return this.comment == null ? new Base[0] : new Base[] {this.comment}; // StringType
        case -251826186: /*stoichiometric*/ return this.stoichiometric == null ? new Base[0] : new Base[] {this.stoichiometric}; // BooleanType
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : new Base[] {this.identifier}; // Identifier
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case 882421574: /*referenceSource*/ return this.referenceSource == null ? new Base[0] : this.referenceSource.toArray(new Base[this.referenceSource.size()]); // StringType
        case -1068650173: /*moiety*/ return this.moiety == null ? new Base[0] : this.moiety.toArray(new Base[this.moiety.size()]); // SubstanceSpecificationMoietyComponent
        case -993141291: /*property*/ return this.property == null ? new Base[0] : this.property.toArray(new Base[this.property.size()]); // SubstanceSpecificationPropertyComponent
        case -2117930783: /*referenceInformation*/ return this.referenceInformation == null ? new Base[0] : new Base[] {this.referenceInformation}; // Reference
        case 144518515: /*structure*/ return this.structure == null ? new Base[0] : new Base[] {this.structure}; // SubstanceSpecificationStructureComponent
        case 1517793149: /*substanceCode*/ return this.substanceCode == null ? new Base[0] : this.substanceCode.toArray(new Base[this.substanceCode.size()]); // SubstanceSpecificationSubstanceCodeComponent
        case 1518107675: /*substanceName*/ return this.substanceName == null ? new Base[0] : this.substanceName.toArray(new Base[this.substanceName.size()]); // SubstanceSpecificationSubstanceNameComponent
        case 635625672: /*molecularWeight*/ return this.molecularWeight == null ? new Base[0] : this.molecularWeight.toArray(new Base[this.molecularWeight.size()]); // SubstanceSpecificationStructureIsotopeMolecularWeightComponent
        case -397514098: /*polymer*/ return this.polymer == null ? new Base[0] : new Base[] {this.polymer}; // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 950398559: // comment
          this.comment = castToString(value); // StringType
          return value;
        case -251826186: // stoichiometric
          this.stoichiometric = castToBoolean(value); // BooleanType
          return value;
        case -1618432855: // identifier
          this.identifier = castToIdentifier(value); // Identifier
          return value;
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 882421574: // referenceSource
          this.getReferenceSource().add(castToString(value)); // StringType
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
        case 1517793149: // substanceCode
          this.getSubstanceCode().add((SubstanceSpecificationSubstanceCodeComponent) value); // SubstanceSpecificationSubstanceCodeComponent
          return value;
        case 1518107675: // substanceName
          this.getSubstanceName().add((SubstanceSpecificationSubstanceNameComponent) value); // SubstanceSpecificationSubstanceNameComponent
          return value;
        case 635625672: // molecularWeight
          this.getMolecularWeight().add((SubstanceSpecificationStructureIsotopeMolecularWeightComponent) value); // SubstanceSpecificationStructureIsotopeMolecularWeightComponent
          return value;
        case -397514098: // polymer
          this.polymer = castToReference(value); // Reference
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("comment")) {
          this.comment = castToString(value); // StringType
        } else if (name.equals("stoichiometric")) {
          this.stoichiometric = castToBoolean(value); // BooleanType
        } else if (name.equals("identifier")) {
          this.identifier = castToIdentifier(value); // Identifier
        } else if (name.equals("type")) {
          this.type = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("referenceSource")) {
          this.getReferenceSource().add(castToString(value));
        } else if (name.equals("moiety")) {
          this.getMoiety().add((SubstanceSpecificationMoietyComponent) value);
        } else if (name.equals("property")) {
          this.getProperty().add((SubstanceSpecificationPropertyComponent) value);
        } else if (name.equals("referenceInformation")) {
          this.referenceInformation = castToReference(value); // Reference
        } else if (name.equals("structure")) {
          this.structure = (SubstanceSpecificationStructureComponent) value; // SubstanceSpecificationStructureComponent
        } else if (name.equals("substanceCode")) {
          this.getSubstanceCode().add((SubstanceSpecificationSubstanceCodeComponent) value);
        } else if (name.equals("substanceName")) {
          this.getSubstanceName().add((SubstanceSpecificationSubstanceNameComponent) value);
        } else if (name.equals("molecularWeight")) {
          this.getMolecularWeight().add((SubstanceSpecificationStructureIsotopeMolecularWeightComponent) value);
        } else if (name.equals("polymer")) {
          this.polymer = castToReference(value); // Reference
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 950398559:  return getCommentElement();
        case -251826186:  return getStoichiometricElement();
        case -1618432855:  return getIdentifier(); 
        case 3575610:  return getType(); 
        case 882421574:  return addReferenceSourceElement();
        case -1068650173:  return addMoiety(); 
        case -993141291:  return addProperty(); 
        case -2117930783:  return getReferenceInformation(); 
        case 144518515:  return getStructure(); 
        case 1517793149:  return addSubstanceCode(); 
        case 1518107675:  return addSubstanceName(); 
        case 635625672:  return addMolecularWeight(); 
        case -397514098:  return getPolymer(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 950398559: /*comment*/ return new String[] {"string"};
        case -251826186: /*stoichiometric*/ return new String[] {"boolean"};
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case 882421574: /*referenceSource*/ return new String[] {"string"};
        case -1068650173: /*moiety*/ return new String[] {};
        case -993141291: /*property*/ return new String[] {};
        case -2117930783: /*referenceInformation*/ return new String[] {"Reference"};
        case 144518515: /*structure*/ return new String[] {};
        case 1517793149: /*substanceCode*/ return new String[] {};
        case 1518107675: /*substanceName*/ return new String[] {};
        case 635625672: /*molecularWeight*/ return new String[] {"@SubstanceSpecification.structure.isotope.molecularWeight"};
        case -397514098: /*polymer*/ return new String[] {"Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("comment")) {
          throw new FHIRException("Cannot call addChild on a primitive type SubstanceSpecification.comment");
        }
        else if (name.equals("stoichiometric")) {
          throw new FHIRException("Cannot call addChild on a primitive type SubstanceSpecification.stoichiometric");
        }
        else if (name.equals("identifier")) {
          this.identifier = new Identifier();
          return this.identifier;
        }
        else if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("referenceSource")) {
          throw new FHIRException("Cannot call addChild on a primitive type SubstanceSpecification.referenceSource");
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
        else if (name.equals("substanceCode")) {
          return addSubstanceCode();
        }
        else if (name.equals("substanceName")) {
          return addSubstanceName();
        }
        else if (name.equals("molecularWeight")) {
          return addMolecularWeight();
        }
        else if (name.equals("polymer")) {
          this.polymer = new Reference();
          return this.polymer;
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
        dst.comment = comment == null ? null : comment.copy();
        dst.stoichiometric = stoichiometric == null ? null : stoichiometric.copy();
        dst.identifier = identifier == null ? null : identifier.copy();
        dst.type = type == null ? null : type.copy();
        if (referenceSource != null) {
          dst.referenceSource = new ArrayList<StringType>();
          for (StringType i : referenceSource)
            dst.referenceSource.add(i.copy());
        };
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
        if (substanceCode != null) {
          dst.substanceCode = new ArrayList<SubstanceSpecificationSubstanceCodeComponent>();
          for (SubstanceSpecificationSubstanceCodeComponent i : substanceCode)
            dst.substanceCode.add(i.copy());
        };
        if (substanceName != null) {
          dst.substanceName = new ArrayList<SubstanceSpecificationSubstanceNameComponent>();
          for (SubstanceSpecificationSubstanceNameComponent i : substanceName)
            dst.substanceName.add(i.copy());
        };
        if (molecularWeight != null) {
          dst.molecularWeight = new ArrayList<SubstanceSpecificationStructureIsotopeMolecularWeightComponent>();
          for (SubstanceSpecificationStructureIsotopeMolecularWeightComponent i : molecularWeight)
            dst.molecularWeight.add(i.copy());
        };
        dst.polymer = polymer == null ? null : polymer.copy();
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
        return compareDeep(comment, o.comment, true) && compareDeep(stoichiometric, o.stoichiometric, true)
           && compareDeep(identifier, o.identifier, true) && compareDeep(type, o.type, true) && compareDeep(referenceSource, o.referenceSource, true)
           && compareDeep(moiety, o.moiety, true) && compareDeep(property, o.property, true) && compareDeep(referenceInformation, o.referenceInformation, true)
           && compareDeep(structure, o.structure, true) && compareDeep(substanceCode, o.substanceCode, true)
           && compareDeep(substanceName, o.substanceName, true) && compareDeep(molecularWeight, o.molecularWeight, true)
           && compareDeep(polymer, o.polymer, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof SubstanceSpecification))
          return false;
        SubstanceSpecification o = (SubstanceSpecification) other_;
        return compareValues(comment, o.comment, true) && compareValues(stoichiometric, o.stoichiometric, true)
           && compareValues(referenceSource, o.referenceSource, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(comment, stoichiometric, identifier
          , type, referenceSource, moiety, property, referenceInformation, structure, substanceCode
          , substanceName, molecularWeight, polymer);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.SubstanceSpecification;
   }


}

