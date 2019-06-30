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
@DatatypeDef(name="SubstanceMoiety")
public class SubstanceMoiety extends BackboneType implements ICompositeType {

    /**
     * The role of the moiety should be specified if there is a specific role the moiety is playing.
     */
    @Child(name = "role", type = {CodeableConcept.class}, order=0, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The role of the moiety should be specified if there is a specific role the moiety is playing", formalDefinition="The role of the moiety should be specified if there is a specific role the moiety is playing." )
    protected CodeableConcept role;

    /**
     * The unique identifier assigned to the substance representing the moiety based on the ISO 11238 substance controlled vocabulary.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The unique identifier assigned to the substance representing the moiety based on the ISO 11238 substance controlled vocabulary", formalDefinition="The unique identifier assigned to the substance representing the moiety based on the ISO 11238 substance controlled vocabulary." )
    protected Identifier identifier;

    /**
     * The name of the moiety shall be provided.
     */
    @Child(name = "name", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The name of the moiety shall be provided", formalDefinition="The name of the moiety shall be provided." )
    protected StringType name;

    /**
     * Stereochemistry shall be captured as described in 4.7.1.
     */
    @Child(name = "stereochemistry", type = {CodeableConcept.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Stereochemistry shall be captured as described in 4.7.1", formalDefinition="Stereochemistry shall be captured as described in 4.7.1." )
    protected CodeableConcept stereochemistry;

    /**
     * Optical activity shall be captured as described in 4.7.2.
     */
    @Child(name = "opticalActivity", type = {CodeableConcept.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Optical activity shall be captured as described in 4.7.2", formalDefinition="Optical activity shall be captured as described in 4.7.2." )
    protected CodeableConcept opticalActivity;

    /**
     * Molecular formula shall be captured as described in 4.7.3.
     */
    @Child(name = "molecularFormula", type = {StringType.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Molecular formula shall be captured as described in 4.7.3", formalDefinition="Molecular formula shall be captured as described in 4.7.3." )
    protected StringType molecularFormula;

    /**
     * Used to capture quantitative values for a variety of elements. If only limits are given, the arithmetic mean would be the average. If only a single definite value for a given element is given, it would be captured in this field.
     */
    @Child(name = "amount", type = {SubstanceAmount.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Used to capture quantitative values for a variety of elements. If only limits are given, the arithmetic mean would be the average. If only a single definite value for a given element is given, it would be captured in this field", formalDefinition="Used to capture quantitative values for a variety of elements. If only limits are given, the arithmetic mean would be the average. If only a single definite value for a given element is given, it would be captured in this field." )
    protected SubstanceAmount amount;

    private static final long serialVersionUID = 204575891L;

  /**
   * Constructor
   */
    public SubstanceMoiety() {
      super();
    }

    /**
     * @return {@link #role} (The role of the moiety should be specified if there is a specific role the moiety is playing.)
     */
    public CodeableConcept getRole() { 
      if (this.role == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SubstanceMoiety.role");
        else if (Configuration.doAutoCreate())
          this.role = new CodeableConcept(); // cc
      return this.role;
    }

    public boolean hasRole() { 
      return this.role != null && !this.role.isEmpty();
    }

    /**
     * @param value {@link #role} (The role of the moiety should be specified if there is a specific role the moiety is playing.)
     */
    public SubstanceMoiety setRole(CodeableConcept value) { 
      this.role = value;
      return this;
    }

    /**
     * @return {@link #identifier} (The unique identifier assigned to the substance representing the moiety based on the ISO 11238 substance controlled vocabulary.)
     */
    public Identifier getIdentifier() { 
      if (this.identifier == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SubstanceMoiety.identifier");
        else if (Configuration.doAutoCreate())
          this.identifier = new Identifier(); // cc
      return this.identifier;
    }

    public boolean hasIdentifier() { 
      return this.identifier != null && !this.identifier.isEmpty();
    }

    /**
     * @param value {@link #identifier} (The unique identifier assigned to the substance representing the moiety based on the ISO 11238 substance controlled vocabulary.)
     */
    public SubstanceMoiety setIdentifier(Identifier value) { 
      this.identifier = value;
      return this;
    }

    /**
     * @return {@link #name} (The name of the moiety shall be provided.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public StringType getNameElement() { 
      if (this.name == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SubstanceMoiety.name");
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
     * @param value {@link #name} (The name of the moiety shall be provided.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public SubstanceMoiety setNameElement(StringType value) { 
      this.name = value;
      return this;
    }

    /**
     * @return The name of the moiety shall be provided.
     */
    public String getName() { 
      return this.name == null ? null : this.name.getValue();
    }

    /**
     * @param value The name of the moiety shall be provided.
     */
    public SubstanceMoiety setName(String value) { 
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
     * @return {@link #stereochemistry} (Stereochemistry shall be captured as described in 4.7.1.)
     */
    public CodeableConcept getStereochemistry() { 
      if (this.stereochemistry == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SubstanceMoiety.stereochemistry");
        else if (Configuration.doAutoCreate())
          this.stereochemistry = new CodeableConcept(); // cc
      return this.stereochemistry;
    }

    public boolean hasStereochemistry() { 
      return this.stereochemistry != null && !this.stereochemistry.isEmpty();
    }

    /**
     * @param value {@link #stereochemistry} (Stereochemistry shall be captured as described in 4.7.1.)
     */
    public SubstanceMoiety setStereochemistry(CodeableConcept value) { 
      this.stereochemistry = value;
      return this;
    }

    /**
     * @return {@link #opticalActivity} (Optical activity shall be captured as described in 4.7.2.)
     */
    public CodeableConcept getOpticalActivity() { 
      if (this.opticalActivity == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SubstanceMoiety.opticalActivity");
        else if (Configuration.doAutoCreate())
          this.opticalActivity = new CodeableConcept(); // cc
      return this.opticalActivity;
    }

    public boolean hasOpticalActivity() { 
      return this.opticalActivity != null && !this.opticalActivity.isEmpty();
    }

    /**
     * @param value {@link #opticalActivity} (Optical activity shall be captured as described in 4.7.2.)
     */
    public SubstanceMoiety setOpticalActivity(CodeableConcept value) { 
      this.opticalActivity = value;
      return this;
    }

    /**
     * @return {@link #molecularFormula} (Molecular formula shall be captured as described in 4.7.3.). This is the underlying object with id, value and extensions. The accessor "getMolecularFormula" gives direct access to the value
     */
    public StringType getMolecularFormulaElement() { 
      if (this.molecularFormula == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SubstanceMoiety.molecularFormula");
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
     * @param value {@link #molecularFormula} (Molecular formula shall be captured as described in 4.7.3.). This is the underlying object with id, value and extensions. The accessor "getMolecularFormula" gives direct access to the value
     */
    public SubstanceMoiety setMolecularFormulaElement(StringType value) { 
      this.molecularFormula = value;
      return this;
    }

    /**
     * @return Molecular formula shall be captured as described in 4.7.3.
     */
    public String getMolecularFormula() { 
      return this.molecularFormula == null ? null : this.molecularFormula.getValue();
    }

    /**
     * @param value Molecular formula shall be captured as described in 4.7.3.
     */
    public SubstanceMoiety setMolecularFormula(String value) { 
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
     * @return {@link #amount} (Used to capture quantitative values for a variety of elements. If only limits are given, the arithmetic mean would be the average. If only a single definite value for a given element is given, it would be captured in this field.)
     */
    public SubstanceAmount getAmount() { 
      if (this.amount == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SubstanceMoiety.amount");
        else if (Configuration.doAutoCreate())
          this.amount = new SubstanceAmount(); // cc
      return this.amount;
    }

    public boolean hasAmount() { 
      return this.amount != null && !this.amount.isEmpty();
    }

    /**
     * @param value {@link #amount} (Used to capture quantitative values for a variety of elements. If only limits are given, the arithmetic mean would be the average. If only a single definite value for a given element is given, it would be captured in this field.)
     */
    public SubstanceMoiety setAmount(SubstanceAmount value) { 
      this.amount = value;
      return this;
    }

      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("role", "CodeableConcept", "The role of the moiety should be specified if there is a specific role the moiety is playing.", 0, 1, role));
        children.add(new Property("identifier", "Identifier", "The unique identifier assigned to the substance representing the moiety based on the ISO 11238 substance controlled vocabulary.", 0, 1, identifier));
        children.add(new Property("name", "string", "The name of the moiety shall be provided.", 0, 1, name));
        children.add(new Property("stereochemistry", "CodeableConcept", "Stereochemistry shall be captured as described in 4.7.1.", 0, 1, stereochemistry));
        children.add(new Property("opticalActivity", "CodeableConcept", "Optical activity shall be captured as described in 4.7.2.", 0, 1, opticalActivity));
        children.add(new Property("molecularFormula", "string", "Molecular formula shall be captured as described in 4.7.3.", 0, 1, molecularFormula));
        children.add(new Property("amount", "SubstanceAmount", "Used to capture quantitative values for a variety of elements. If only limits are given, the arithmetic mean would be the average. If only a single definite value for a given element is given, it would be captured in this field.", 0, 1, amount));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case 3506294: /*role*/  return new Property("role", "CodeableConcept", "The role of the moiety should be specified if there is a specific role the moiety is playing.", 0, 1, role);
        case -1618432855: /*identifier*/  return new Property("identifier", "Identifier", "The unique identifier assigned to the substance representing the moiety based on the ISO 11238 substance controlled vocabulary.", 0, 1, identifier);
        case 3373707: /*name*/  return new Property("name", "string", "The name of the moiety shall be provided.", 0, 1, name);
        case 263475116: /*stereochemistry*/  return new Property("stereochemistry", "CodeableConcept", "Stereochemistry shall be captured as described in 4.7.1.", 0, 1, stereochemistry);
        case 1420900135: /*opticalActivity*/  return new Property("opticalActivity", "CodeableConcept", "Optical activity shall be captured as described in 4.7.2.", 0, 1, opticalActivity);
        case 616660246: /*molecularFormula*/  return new Property("molecularFormula", "string", "Molecular formula shall be captured as described in 4.7.3.", 0, 1, molecularFormula);
        case -1413853096: /*amount*/  return new Property("amount", "SubstanceAmount", "Used to capture quantitative values for a variety of elements. If only limits are given, the arithmetic mean would be the average. If only a single definite value for a given element is given, it would be captured in this field.", 0, 1, amount);
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
        case -1413853096: /*amount*/ return this.amount == null ? new Base[0] : new Base[] {this.amount}; // SubstanceAmount
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
          this.amount = castToSubstanceAmount(value); // SubstanceAmount
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
          this.amount = castToSubstanceAmount(value); // SubstanceAmount
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
        case -1413853096: /*amount*/ return new String[] {"SubstanceAmount"};
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
          throw new FHIRException("Cannot call addChild on a primitive type SubstanceMoiety.name");
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
          throw new FHIRException("Cannot call addChild on a primitive type SubstanceMoiety.molecularFormula");
        }
        else if (name.equals("amount")) {
          this.amount = new SubstanceAmount();
          return this.amount;
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "SubstanceMoiety";

  }

      public SubstanceMoiety copy() {
        SubstanceMoiety dst = new SubstanceMoiety();
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

      protected SubstanceMoiety typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof SubstanceMoiety))
          return false;
        SubstanceMoiety o = (SubstanceMoiety) other_;
        return compareDeep(role, o.role, true) && compareDeep(identifier, o.identifier, true) && compareDeep(name, o.name, true)
           && compareDeep(stereochemistry, o.stereochemistry, true) && compareDeep(opticalActivity, o.opticalActivity, true)
           && compareDeep(molecularFormula, o.molecularFormula, true) && compareDeep(amount, o.amount, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof SubstanceMoiety))
          return false;
        SubstanceMoiety o = (SubstanceMoiety) other_;
        return compareValues(name, o.name, true) && compareValues(molecularFormula, o.molecularFormula, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(role, identifier, name, stereochemistry
          , opticalActivity, molecularFormula, amount);
      }


}

