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

// Generated on Sun, May 6, 2018 17:51-0400 for FHIR v3.4.0

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
 * Information about a medication that is used to support knowledge.
 */
@ResourceDef(name="MedicationKnowledge", profile="http://hl7.org/fhir/Profile/MedicationKnowledge")
public class MedicationKnowledge extends DomainResource {

    public enum MedicationKnowledgeStatus {
        /**
         * The medication is available for use
         */
        ACTIVE, 
        /**
         * The medication is not available for use
         */
        INACTIVE, 
        /**
         * The medication was entered in error
         */
        ENTEREDINERROR, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static MedicationKnowledgeStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("active".equals(codeString))
          return ACTIVE;
        if ("inactive".equals(codeString))
          return INACTIVE;
        if ("entered-in-error".equals(codeString))
          return ENTEREDINERROR;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown MedicationKnowledgeStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ACTIVE: return "active";
            case INACTIVE: return "inactive";
            case ENTEREDINERROR: return "entered-in-error";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case ACTIVE: return "http://hl7.org/fhir/medicationKnowledge-status";
            case INACTIVE: return "http://hl7.org/fhir/medicationKnowledge-status";
            case ENTEREDINERROR: return "http://hl7.org/fhir/medicationKnowledge-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case ACTIVE: return "The medication is available for use";
            case INACTIVE: return "The medication is not available for use";
            case ENTEREDINERROR: return "The medication was entered in error";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ACTIVE: return "Active";
            case INACTIVE: return "Inactive";
            case ENTEREDINERROR: return "Entered in Error";
            default: return "?";
          }
        }
    }

  public static class MedicationKnowledgeStatusEnumFactory implements EnumFactory<MedicationKnowledgeStatus> {
    public MedicationKnowledgeStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("active".equals(codeString))
          return MedicationKnowledgeStatus.ACTIVE;
        if ("inactive".equals(codeString))
          return MedicationKnowledgeStatus.INACTIVE;
        if ("entered-in-error".equals(codeString))
          return MedicationKnowledgeStatus.ENTEREDINERROR;
        throw new IllegalArgumentException("Unknown MedicationKnowledgeStatus code '"+codeString+"'");
        }
        public Enumeration<MedicationKnowledgeStatus> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<MedicationKnowledgeStatus>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("active".equals(codeString))
          return new Enumeration<MedicationKnowledgeStatus>(this, MedicationKnowledgeStatus.ACTIVE);
        if ("inactive".equals(codeString))
          return new Enumeration<MedicationKnowledgeStatus>(this, MedicationKnowledgeStatus.INACTIVE);
        if ("entered-in-error".equals(codeString))
          return new Enumeration<MedicationKnowledgeStatus>(this, MedicationKnowledgeStatus.ENTEREDINERROR);
        throw new FHIRException("Unknown MedicationKnowledgeStatus code '"+codeString+"'");
        }
    public String toCode(MedicationKnowledgeStatus code) {
      if (code == MedicationKnowledgeStatus.ACTIVE)
        return "active";
      if (code == MedicationKnowledgeStatus.INACTIVE)
        return "inactive";
      if (code == MedicationKnowledgeStatus.ENTEREDINERROR)
        return "entered-in-error";
      return "?";
      }
    public String toSystem(MedicationKnowledgeStatus code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class MedicationKnowledgeMonographComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The category of documentation about the medication. (e.g. professional monograph, patient educaton monograph).
         */
        @Child(name = "type", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The category of medication document", formalDefinition="The category of documentation about the medication. (e.g. professional monograph, patient educaton monograph)." )
        protected CodeableConcept type;

        /**
         * Associated documentation about the medication.
         */
        @Child(name = "document", type = {DocumentReference.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Associated documentation about the medication", formalDefinition="Associated documentation about the medication." )
        protected Reference document;

        /**
         * The actual object that is the target of the reference (Associated documentation about the medication.)
         */
        protected DocumentReference documentTarget;

        private static final long serialVersionUID = 2046517217L;

    /**
     * Constructor
     */
      public MedicationKnowledgeMonographComponent() {
        super();
      }

        /**
         * @return {@link #type} (The category of documentation about the medication. (e.g. professional monograph, patient educaton monograph).)
         */
        public CodeableConcept getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationKnowledgeMonographComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new CodeableConcept(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (The category of documentation about the medication. (e.g. professional monograph, patient educaton monograph).)
         */
        public MedicationKnowledgeMonographComponent setType(CodeableConcept value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #document} (Associated documentation about the medication.)
         */
        public Reference getDocument() { 
          if (this.document == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationKnowledgeMonographComponent.document");
            else if (Configuration.doAutoCreate())
              this.document = new Reference(); // cc
          return this.document;
        }

        public boolean hasDocument() { 
          return this.document != null && !this.document.isEmpty();
        }

        /**
         * @param value {@link #document} (Associated documentation about the medication.)
         */
        public MedicationKnowledgeMonographComponent setDocument(Reference value) { 
          this.document = value;
          return this;
        }

        /**
         * @return {@link #document} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Associated documentation about the medication.)
         */
        public DocumentReference getDocumentTarget() { 
          if (this.documentTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationKnowledgeMonographComponent.document");
            else if (Configuration.doAutoCreate())
              this.documentTarget = new DocumentReference(); // aa
          return this.documentTarget;
        }

        /**
         * @param value {@link #document} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Associated documentation about the medication.)
         */
        public MedicationKnowledgeMonographComponent setDocumentTarget(DocumentReference value) { 
          this.documentTarget = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("type", "CodeableConcept", "The category of documentation about the medication. (e.g. professional monograph, patient educaton monograph).", 0, 1, type));
          children.add(new Property("document", "Reference(DocumentReference)", "Associated documentation about the medication.", 0, 1, document));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3575610: /*type*/  return new Property("type", "CodeableConcept", "The category of documentation about the medication. (e.g. professional monograph, patient educaton monograph).", 0, 1, type);
          case 861720859: /*document*/  return new Property("document", "Reference(DocumentReference)", "Associated documentation about the medication.", 0, 1, document);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case 861720859: /*document*/ return this.document == null ? new Base[0] : new Base[] {this.document}; // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 861720859: // document
          this.document = castToReference(value); // Reference
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type")) {
          this.type = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("document")) {
          this.document = castToReference(value); // Reference
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610:  return getType(); 
        case 861720859:  return getDocument(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case 861720859: /*document*/ return new String[] {"Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("document")) {
          this.document = new Reference();
          return this.document;
        }
        else
          return super.addChild(name);
      }

      public MedicationKnowledgeMonographComponent copy() {
        MedicationKnowledgeMonographComponent dst = new MedicationKnowledgeMonographComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.document = document == null ? null : document.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof MedicationKnowledgeMonographComponent))
          return false;
        MedicationKnowledgeMonographComponent o = (MedicationKnowledgeMonographComponent) other_;
        return compareDeep(type, o.type, true) && compareDeep(document, o.document, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof MedicationKnowledgeMonographComponent))
          return false;
        MedicationKnowledgeMonographComponent o = (MedicationKnowledgeMonographComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(type, document);
      }

  public String fhirType() {
    return "MedicationKnowledge.monograph";

  }

  }

    @Block()
    public static class MedicationKnowledgeIngredientComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The actual ingredient - either a substance (simple ingredient) or another medication.
         */
        @Child(name = "item", type = {CodeableConcept.class, Substance.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Medication(s) or substance(s) contained in the medication", formalDefinition="The actual ingredient - either a substance (simple ingredient) or another medication." )
        protected Type item;

        /**
         * Indication of whether this ingredient affects the therapeutic action of the drug.
         */
        @Child(name = "isActive", type = {BooleanType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Active ingredient indicator", formalDefinition="Indication of whether this ingredient affects the therapeutic action of the drug." )
        protected BooleanType isActive;

        /**
         * Specifies how many (or how much) of the items there are in this Medication.  For example, 250 mg per tablet.  This is expressed as a ratio where the numerator is 250mg and the denominator is 1 tablet.
         */
        @Child(name = "strength", type = {Ratio.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Quantity of ingredient present", formalDefinition="Specifies how many (or how much) of the items there are in this Medication.  For example, 250 mg per tablet.  This is expressed as a ratio where the numerator is 250mg and the denominator is 1 tablet." )
        protected Ratio strength;

        private static final long serialVersionUID = 1365103497L;

    /**
     * Constructor
     */
      public MedicationKnowledgeIngredientComponent() {
        super();
      }

    /**
     * Constructor
     */
      public MedicationKnowledgeIngredientComponent(Type item) {
        super();
        this.item = item;
      }

        /**
         * @return {@link #item} (The actual ingredient - either a substance (simple ingredient) or another medication.)
         */
        public Type getItem() { 
          return this.item;
        }

        /**
         * @return {@link #item} (The actual ingredient - either a substance (simple ingredient) or another medication.)
         */
        public CodeableConcept getItemCodeableConcept() throws FHIRException { 
          if (this.item == null)
            return null;
          if (!(this.item instanceof CodeableConcept))
            throw new FHIRException("Type mismatch: the type CodeableConcept was expected, but "+this.item.getClass().getName()+" was encountered");
          return (CodeableConcept) this.item;
        }

        public boolean hasItemCodeableConcept() { 
          return this != null && this.item instanceof CodeableConcept;
        }

        /**
         * @return {@link #item} (The actual ingredient - either a substance (simple ingredient) or another medication.)
         */
        public Reference getItemReference() throws FHIRException { 
          if (this.item == null)
            return null;
          if (!(this.item instanceof Reference))
            throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.item.getClass().getName()+" was encountered");
          return (Reference) this.item;
        }

        public boolean hasItemReference() { 
          return this != null && this.item instanceof Reference;
        }

        public boolean hasItem() { 
          return this.item != null && !this.item.isEmpty();
        }

        /**
         * @param value {@link #item} (The actual ingredient - either a substance (simple ingredient) or another medication.)
         */
        public MedicationKnowledgeIngredientComponent setItem(Type value) { 
          if (value != null && !(value instanceof CodeableConcept || value instanceof Reference))
            throw new Error("Not the right type for MedicationKnowledge.ingredient.item[x]: "+value.fhirType());
          this.item = value;
          return this;
        }

        /**
         * @return {@link #isActive} (Indication of whether this ingredient affects the therapeutic action of the drug.). This is the underlying object with id, value and extensions. The accessor "getIsActive" gives direct access to the value
         */
        public BooleanType getIsActiveElement() { 
          if (this.isActive == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationKnowledgeIngredientComponent.isActive");
            else if (Configuration.doAutoCreate())
              this.isActive = new BooleanType(); // bb
          return this.isActive;
        }

        public boolean hasIsActiveElement() { 
          return this.isActive != null && !this.isActive.isEmpty();
        }

        public boolean hasIsActive() { 
          return this.isActive != null && !this.isActive.isEmpty();
        }

        /**
         * @param value {@link #isActive} (Indication of whether this ingredient affects the therapeutic action of the drug.). This is the underlying object with id, value and extensions. The accessor "getIsActive" gives direct access to the value
         */
        public MedicationKnowledgeIngredientComponent setIsActiveElement(BooleanType value) { 
          this.isActive = value;
          return this;
        }

        /**
         * @return Indication of whether this ingredient affects the therapeutic action of the drug.
         */
        public boolean getIsActive() { 
          return this.isActive == null || this.isActive.isEmpty() ? false : this.isActive.getValue();
        }

        /**
         * @param value Indication of whether this ingredient affects the therapeutic action of the drug.
         */
        public MedicationKnowledgeIngredientComponent setIsActive(boolean value) { 
            if (this.isActive == null)
              this.isActive = new BooleanType();
            this.isActive.setValue(value);
          return this;
        }

        /**
         * @return {@link #strength} (Specifies how many (or how much) of the items there are in this Medication.  For example, 250 mg per tablet.  This is expressed as a ratio where the numerator is 250mg and the denominator is 1 tablet.)
         */
        public Ratio getStrength() { 
          if (this.strength == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationKnowledgeIngredientComponent.strength");
            else if (Configuration.doAutoCreate())
              this.strength = new Ratio(); // cc
          return this.strength;
        }

        public boolean hasStrength() { 
          return this.strength != null && !this.strength.isEmpty();
        }

        /**
         * @param value {@link #strength} (Specifies how many (or how much) of the items there are in this Medication.  For example, 250 mg per tablet.  This is expressed as a ratio where the numerator is 250mg and the denominator is 1 tablet.)
         */
        public MedicationKnowledgeIngredientComponent setStrength(Ratio value) { 
          this.strength = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("item[x]", "CodeableConcept|Reference(Substance)", "The actual ingredient - either a substance (simple ingredient) or another medication.", 0, 1, item));
          children.add(new Property("isActive", "boolean", "Indication of whether this ingredient affects the therapeutic action of the drug.", 0, 1, isActive));
          children.add(new Property("strength", "Ratio", "Specifies how many (or how much) of the items there are in this Medication.  For example, 250 mg per tablet.  This is expressed as a ratio where the numerator is 250mg and the denominator is 1 tablet.", 0, 1, strength));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 2116201613: /*item[x]*/  return new Property("item[x]", "CodeableConcept|Reference(Substance)", "The actual ingredient - either a substance (simple ingredient) or another medication.", 0, 1, item);
          case 3242771: /*item*/  return new Property("item[x]", "CodeableConcept|Reference(Substance)", "The actual ingredient - either a substance (simple ingredient) or another medication.", 0, 1, item);
          case 106644494: /*itemCodeableConcept*/  return new Property("item[x]", "CodeableConcept|Reference(Substance)", "The actual ingredient - either a substance (simple ingredient) or another medication.", 0, 1, item);
          case 1376364920: /*itemReference*/  return new Property("item[x]", "CodeableConcept|Reference(Substance)", "The actual ingredient - either a substance (simple ingredient) or another medication.", 0, 1, item);
          case -748916528: /*isActive*/  return new Property("isActive", "boolean", "Indication of whether this ingredient affects the therapeutic action of the drug.", 0, 1, isActive);
          case 1791316033: /*strength*/  return new Property("strength", "Ratio", "Specifies how many (or how much) of the items there are in this Medication.  For example, 250 mg per tablet.  This is expressed as a ratio where the numerator is 250mg and the denominator is 1 tablet.", 0, 1, strength);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3242771: /*item*/ return this.item == null ? new Base[0] : new Base[] {this.item}; // Type
        case -748916528: /*isActive*/ return this.isActive == null ? new Base[0] : new Base[] {this.isActive}; // BooleanType
        case 1791316033: /*strength*/ return this.strength == null ? new Base[0] : new Base[] {this.strength}; // Ratio
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3242771: // item
          this.item = castToType(value); // Type
          return value;
        case -748916528: // isActive
          this.isActive = castToBoolean(value); // BooleanType
          return value;
        case 1791316033: // strength
          this.strength = castToRatio(value); // Ratio
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("item[x]")) {
          this.item = castToType(value); // Type
        } else if (name.equals("isActive")) {
          this.isActive = castToBoolean(value); // BooleanType
        } else if (name.equals("strength")) {
          this.strength = castToRatio(value); // Ratio
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 2116201613:  return getItem(); 
        case 3242771:  return getItem(); 
        case -748916528:  return getIsActiveElement();
        case 1791316033:  return getStrength(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3242771: /*item*/ return new String[] {"CodeableConcept", "Reference"};
        case -748916528: /*isActive*/ return new String[] {"boolean"};
        case 1791316033: /*strength*/ return new String[] {"Ratio"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("itemCodeableConcept")) {
          this.item = new CodeableConcept();
          return this.item;
        }
        else if (name.equals("itemReference")) {
          this.item = new Reference();
          return this.item;
        }
        else if (name.equals("isActive")) {
          throw new FHIRException("Cannot call addChild on a primitive type MedicationKnowledge.isActive");
        }
        else if (name.equals("strength")) {
          this.strength = new Ratio();
          return this.strength;
        }
        else
          return super.addChild(name);
      }

      public MedicationKnowledgeIngredientComponent copy() {
        MedicationKnowledgeIngredientComponent dst = new MedicationKnowledgeIngredientComponent();
        copyValues(dst);
        dst.item = item == null ? null : item.copy();
        dst.isActive = isActive == null ? null : isActive.copy();
        dst.strength = strength == null ? null : strength.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof MedicationKnowledgeIngredientComponent))
          return false;
        MedicationKnowledgeIngredientComponent o = (MedicationKnowledgeIngredientComponent) other_;
        return compareDeep(item, o.item, true) && compareDeep(isActive, o.isActive, true) && compareDeep(strength, o.strength, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof MedicationKnowledgeIngredientComponent))
          return false;
        MedicationKnowledgeIngredientComponent o = (MedicationKnowledgeIngredientComponent) other_;
        return compareValues(isActive, o.isActive, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(item, isActive, strength
          );
      }

  public String fhirType() {
    return "MedicationKnowledge.ingredient";

  }

  }

    @Block()
    public static class MedicationKnowledgeCostComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The category of the cost information.  For example, manufacturers' cost, patient cost, claim reimbursement cost, actual acquisition cost.
         */
        @Child(name = "type", type = {StringType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The category of the cost information", formalDefinition="The category of the cost information.  For example, manufacturers' cost, patient cost, claim reimbursement cost, actual acquisition cost." )
        protected StringType type;

        /**
         * The source or owner that assigns the price to the medication.
         */
        @Child(name = "source", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The source or owner for the price information", formalDefinition="The source or owner that assigns the price to the medication." )
        protected StringType source;

        /**
         * The price of the medication.
         */
        @Child(name = "cost", type = {Money.class}, order=3, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The price of the medication", formalDefinition="The price of the medication." )
        protected Money cost;

        private static final long serialVersionUID = 1750634148L;

    /**
     * Constructor
     */
      public MedicationKnowledgeCostComponent() {
        super();
      }

    /**
     * Constructor
     */
      public MedicationKnowledgeCostComponent(StringType type, Money cost) {
        super();
        this.type = type;
        this.cost = cost;
      }

        /**
         * @return {@link #type} (The category of the cost information.  For example, manufacturers' cost, patient cost, claim reimbursement cost, actual acquisition cost.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public StringType getTypeElement() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationKnowledgeCostComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new StringType(); // bb
          return this.type;
        }

        public boolean hasTypeElement() { 
          return this.type != null && !this.type.isEmpty();
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (The category of the cost information.  For example, manufacturers' cost, patient cost, claim reimbursement cost, actual acquisition cost.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public MedicationKnowledgeCostComponent setTypeElement(StringType value) { 
          this.type = value;
          return this;
        }

        /**
         * @return The category of the cost information.  For example, manufacturers' cost, patient cost, claim reimbursement cost, actual acquisition cost.
         */
        public String getType() { 
          return this.type == null ? null : this.type.getValue();
        }

        /**
         * @param value The category of the cost information.  For example, manufacturers' cost, patient cost, claim reimbursement cost, actual acquisition cost.
         */
        public MedicationKnowledgeCostComponent setType(String value) { 
            if (this.type == null)
              this.type = new StringType();
            this.type.setValue(value);
          return this;
        }

        /**
         * @return {@link #source} (The source or owner that assigns the price to the medication.). This is the underlying object with id, value and extensions. The accessor "getSource" gives direct access to the value
         */
        public StringType getSourceElement() { 
          if (this.source == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationKnowledgeCostComponent.source");
            else if (Configuration.doAutoCreate())
              this.source = new StringType(); // bb
          return this.source;
        }

        public boolean hasSourceElement() { 
          return this.source != null && !this.source.isEmpty();
        }

        public boolean hasSource() { 
          return this.source != null && !this.source.isEmpty();
        }

        /**
         * @param value {@link #source} (The source or owner that assigns the price to the medication.). This is the underlying object with id, value and extensions. The accessor "getSource" gives direct access to the value
         */
        public MedicationKnowledgeCostComponent setSourceElement(StringType value) { 
          this.source = value;
          return this;
        }

        /**
         * @return The source or owner that assigns the price to the medication.
         */
        public String getSource() { 
          return this.source == null ? null : this.source.getValue();
        }

        /**
         * @param value The source or owner that assigns the price to the medication.
         */
        public MedicationKnowledgeCostComponent setSource(String value) { 
          if (Utilities.noString(value))
            this.source = null;
          else {
            if (this.source == null)
              this.source = new StringType();
            this.source.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #cost} (The price of the medication.)
         */
        public Money getCost() { 
          if (this.cost == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationKnowledgeCostComponent.cost");
            else if (Configuration.doAutoCreate())
              this.cost = new Money(); // cc
          return this.cost;
        }

        public boolean hasCost() { 
          return this.cost != null && !this.cost.isEmpty();
        }

        /**
         * @param value {@link #cost} (The price of the medication.)
         */
        public MedicationKnowledgeCostComponent setCost(Money value) { 
          this.cost = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("type", "string", "The category of the cost information.  For example, manufacturers' cost, patient cost, claim reimbursement cost, actual acquisition cost.", 0, 1, type));
          children.add(new Property("source", "string", "The source or owner that assigns the price to the medication.", 0, 1, source));
          children.add(new Property("cost", "Money", "The price of the medication.", 0, 1, cost));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3575610: /*type*/  return new Property("type", "string", "The category of the cost information.  For example, manufacturers' cost, patient cost, claim reimbursement cost, actual acquisition cost.", 0, 1, type);
          case -896505829: /*source*/  return new Property("source", "string", "The source or owner that assigns the price to the medication.", 0, 1, source);
          case 3059661: /*cost*/  return new Property("cost", "Money", "The price of the medication.", 0, 1, cost);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // StringType
        case -896505829: /*source*/ return this.source == null ? new Base[0] : new Base[] {this.source}; // StringType
        case 3059661: /*cost*/ return this.cost == null ? new Base[0] : new Base[] {this.cost}; // Money
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          this.type = castToString(value); // StringType
          return value;
        case -896505829: // source
          this.source = castToString(value); // StringType
          return value;
        case 3059661: // cost
          this.cost = castToMoney(value); // Money
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type")) {
          this.type = castToString(value); // StringType
        } else if (name.equals("source")) {
          this.source = castToString(value); // StringType
        } else if (name.equals("cost")) {
          this.cost = castToMoney(value); // Money
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610:  return getTypeElement();
        case -896505829:  return getSourceElement();
        case 3059661:  return getCost(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return new String[] {"string"};
        case -896505829: /*source*/ return new String[] {"string"};
        case 3059661: /*cost*/ return new String[] {"Money"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          throw new FHIRException("Cannot call addChild on a primitive type MedicationKnowledge.type");
        }
        else if (name.equals("source")) {
          throw new FHIRException("Cannot call addChild on a primitive type MedicationKnowledge.source");
        }
        else if (name.equals("cost")) {
          this.cost = new Money();
          return this.cost;
        }
        else
          return super.addChild(name);
      }

      public MedicationKnowledgeCostComponent copy() {
        MedicationKnowledgeCostComponent dst = new MedicationKnowledgeCostComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.source = source == null ? null : source.copy();
        dst.cost = cost == null ? null : cost.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof MedicationKnowledgeCostComponent))
          return false;
        MedicationKnowledgeCostComponent o = (MedicationKnowledgeCostComponent) other_;
        return compareDeep(type, o.type, true) && compareDeep(source, o.source, true) && compareDeep(cost, o.cost, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof MedicationKnowledgeCostComponent))
          return false;
        MedicationKnowledgeCostComponent o = (MedicationKnowledgeCostComponent) other_;
        return compareValues(type, o.type, true) && compareValues(source, o.source, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(type, source, cost);
      }

  public String fhirType() {
    return "MedicationKnowledge.cost";

  }

  }

    @Block()
    public static class MedicationKnowledgeMonitoringProgramComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Type of program under which the medication is monitored.
         */
        @Child(name = "type", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Type of program under which the medication is monitored", formalDefinition="Type of program under which the medication is monitored." )
        protected CodeableConcept type;

        /**
         * Name of the reviewing program.
         */
        @Child(name = "name", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Name of the reviewing program", formalDefinition="Name of the reviewing program." )
        protected StringType name;

        private static final long serialVersionUID = -280346281L;

    /**
     * Constructor
     */
      public MedicationKnowledgeMonitoringProgramComponent() {
        super();
      }

        /**
         * @return {@link #type} (Type of program under which the medication is monitored.)
         */
        public CodeableConcept getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationKnowledgeMonitoringProgramComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new CodeableConcept(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (Type of program under which the medication is monitored.)
         */
        public MedicationKnowledgeMonitoringProgramComponent setType(CodeableConcept value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #name} (Name of the reviewing program.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StringType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationKnowledgeMonitoringProgramComponent.name");
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
         * @param value {@link #name} (Name of the reviewing program.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public MedicationKnowledgeMonitoringProgramComponent setNameElement(StringType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return Name of the reviewing program.
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value Name of the reviewing program.
         */
        public MedicationKnowledgeMonitoringProgramComponent setName(String value) { 
          if (Utilities.noString(value))
            this.name = null;
          else {
            if (this.name == null)
              this.name = new StringType();
            this.name.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("type", "CodeableConcept", "Type of program under which the medication is monitored.", 0, 1, type));
          children.add(new Property("name", "string", "Name of the reviewing program.", 0, 1, name));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3575610: /*type*/  return new Property("type", "CodeableConcept", "Type of program under which the medication is monitored.", 0, 1, type);
          case 3373707: /*name*/  return new Property("name", "string", "Name of the reviewing program.", 0, 1, name);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
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
          this.name = castToString(value); // StringType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type")) {
          this.type = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("name")) {
          this.name = castToString(value); // StringType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610:  return getType(); 
        case 3373707:  return getNameElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case 3373707: /*name*/ return new String[] {"string"};
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
          throw new FHIRException("Cannot call addChild on a primitive type MedicationKnowledge.name");
        }
        else
          return super.addChild(name);
      }

      public MedicationKnowledgeMonitoringProgramComponent copy() {
        MedicationKnowledgeMonitoringProgramComponent dst = new MedicationKnowledgeMonitoringProgramComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.name = name == null ? null : name.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof MedicationKnowledgeMonitoringProgramComponent))
          return false;
        MedicationKnowledgeMonitoringProgramComponent o = (MedicationKnowledgeMonitoringProgramComponent) other_;
        return compareDeep(type, o.type, true) && compareDeep(name, o.name, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof MedicationKnowledgeMonitoringProgramComponent))
          return false;
        MedicationKnowledgeMonitoringProgramComponent o = (MedicationKnowledgeMonitoringProgramComponent) other_;
        return compareValues(name, o.name, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(type, name);
      }

  public String fhirType() {
    return "MedicationKnowledge.monitoringProgram";

  }

  }

    @Block()
    public static class MedicationKnowledgeAdministrationGuidelinesComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Dosage for the medication for the specific guidelines.
         */
        @Child(name = "dosage", type = {Dosage.class}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Dosage for the medication for the specific guidelines", formalDefinition="Dosage for the medication for the specific guidelines." )
        protected List<Dosage> dosage;

        /**
         * Indication for use that apply to the specific administration guidelines.
         */
        @Child(name = "indication", type = {CodeableConcept.class, ObservationDefinition.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Indication for use that apply to the specific administration guidelines", formalDefinition="Indication for use that apply to the specific administration guidelines." )
        protected Type indication;

        /**
         * Characteristics of the patient that are relevant to the administration guidelines (for example, height, weight,gender,  etc).
         */
        @Child(name = "patientCharacteristics", type = {}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Characteristics of the patient that are relevant to the administration guidelines", formalDefinition="Characteristics of the patient that are relevant to the administration guidelines (for example, height, weight,gender,  etc)." )
        protected List<MedicationKnowledgeAdministrationGuidelinesPatientCharacteristicsComponent> patientCharacteristics;

        private static final long serialVersionUID = -685386015L;

    /**
     * Constructor
     */
      public MedicationKnowledgeAdministrationGuidelinesComponent() {
        super();
      }

        /**
         * @return {@link #dosage} (Dosage for the medication for the specific guidelines.)
         */
        public List<Dosage> getDosage() { 
          if (this.dosage == null)
            this.dosage = new ArrayList<Dosage>();
          return this.dosage;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public MedicationKnowledgeAdministrationGuidelinesComponent setDosage(List<Dosage> theDosage) { 
          this.dosage = theDosage;
          return this;
        }

        public boolean hasDosage() { 
          if (this.dosage == null)
            return false;
          for (Dosage item : this.dosage)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public Dosage addDosage() { //3
          Dosage t = new Dosage();
          if (this.dosage == null)
            this.dosage = new ArrayList<Dosage>();
          this.dosage.add(t);
          return t;
        }

        public MedicationKnowledgeAdministrationGuidelinesComponent addDosage(Dosage t) { //3
          if (t == null)
            return this;
          if (this.dosage == null)
            this.dosage = new ArrayList<Dosage>();
          this.dosage.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #dosage}, creating it if it does not already exist
         */
        public Dosage getDosageFirstRep() { 
          if (getDosage().isEmpty()) {
            addDosage();
          }
          return getDosage().get(0);
        }

        /**
         * @return {@link #indication} (Indication for use that apply to the specific administration guidelines.)
         */
        public Type getIndication() { 
          return this.indication;
        }

        /**
         * @return {@link #indication} (Indication for use that apply to the specific administration guidelines.)
         */
        public CodeableConcept getIndicationCodeableConcept() throws FHIRException { 
          if (this.indication == null)
            return null;
          if (!(this.indication instanceof CodeableConcept))
            throw new FHIRException("Type mismatch: the type CodeableConcept was expected, but "+this.indication.getClass().getName()+" was encountered");
          return (CodeableConcept) this.indication;
        }

        public boolean hasIndicationCodeableConcept() { 
          return this != null && this.indication instanceof CodeableConcept;
        }

        /**
         * @return {@link #indication} (Indication for use that apply to the specific administration guidelines.)
         */
        public Reference getIndicationReference() throws FHIRException { 
          if (this.indication == null)
            return null;
          if (!(this.indication instanceof Reference))
            throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.indication.getClass().getName()+" was encountered");
          return (Reference) this.indication;
        }

        public boolean hasIndicationReference() { 
          return this != null && this.indication instanceof Reference;
        }

        public boolean hasIndication() { 
          return this.indication != null && !this.indication.isEmpty();
        }

        /**
         * @param value {@link #indication} (Indication for use that apply to the specific administration guidelines.)
         */
        public MedicationKnowledgeAdministrationGuidelinesComponent setIndication(Type value) { 
          if (value != null && !(value instanceof CodeableConcept || value instanceof Reference))
            throw new Error("Not the right type for MedicationKnowledge.administrationGuidelines.indication[x]: "+value.fhirType());
          this.indication = value;
          return this;
        }

        /**
         * @return {@link #patientCharacteristics} (Characteristics of the patient that are relevant to the administration guidelines (for example, height, weight,gender,  etc).)
         */
        public List<MedicationKnowledgeAdministrationGuidelinesPatientCharacteristicsComponent> getPatientCharacteristics() { 
          if (this.patientCharacteristics == null)
            this.patientCharacteristics = new ArrayList<MedicationKnowledgeAdministrationGuidelinesPatientCharacteristicsComponent>();
          return this.patientCharacteristics;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public MedicationKnowledgeAdministrationGuidelinesComponent setPatientCharacteristics(List<MedicationKnowledgeAdministrationGuidelinesPatientCharacteristicsComponent> thePatientCharacteristics) { 
          this.patientCharacteristics = thePatientCharacteristics;
          return this;
        }

        public boolean hasPatientCharacteristics() { 
          if (this.patientCharacteristics == null)
            return false;
          for (MedicationKnowledgeAdministrationGuidelinesPatientCharacteristicsComponent item : this.patientCharacteristics)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public MedicationKnowledgeAdministrationGuidelinesPatientCharacteristicsComponent addPatientCharacteristics() { //3
          MedicationKnowledgeAdministrationGuidelinesPatientCharacteristicsComponent t = new MedicationKnowledgeAdministrationGuidelinesPatientCharacteristicsComponent();
          if (this.patientCharacteristics == null)
            this.patientCharacteristics = new ArrayList<MedicationKnowledgeAdministrationGuidelinesPatientCharacteristicsComponent>();
          this.patientCharacteristics.add(t);
          return t;
        }

        public MedicationKnowledgeAdministrationGuidelinesComponent addPatientCharacteristics(MedicationKnowledgeAdministrationGuidelinesPatientCharacteristicsComponent t) { //3
          if (t == null)
            return this;
          if (this.patientCharacteristics == null)
            this.patientCharacteristics = new ArrayList<MedicationKnowledgeAdministrationGuidelinesPatientCharacteristicsComponent>();
          this.patientCharacteristics.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #patientCharacteristics}, creating it if it does not already exist
         */
        public MedicationKnowledgeAdministrationGuidelinesPatientCharacteristicsComponent getPatientCharacteristicsFirstRep() { 
          if (getPatientCharacteristics().isEmpty()) {
            addPatientCharacteristics();
          }
          return getPatientCharacteristics().get(0);
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("dosage", "Dosage", "Dosage for the medication for the specific guidelines.", 0, java.lang.Integer.MAX_VALUE, dosage));
          children.add(new Property("indication[x]", "CodeableConcept|Reference(ObservationDefinition)", "Indication for use that apply to the specific administration guidelines.", 0, 1, indication));
          children.add(new Property("patientCharacteristics", "", "Characteristics of the patient that are relevant to the administration guidelines (for example, height, weight,gender,  etc).", 0, java.lang.Integer.MAX_VALUE, patientCharacteristics));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -1326018889: /*dosage*/  return new Property("dosage", "Dosage", "Dosage for the medication for the specific guidelines.", 0, java.lang.Integer.MAX_VALUE, dosage);
          case -501208668: /*indication[x]*/  return new Property("indication[x]", "CodeableConcept|Reference(ObservationDefinition)", "Indication for use that apply to the specific administration guidelines.", 0, 1, indication);
          case -597168804: /*indication*/  return new Property("indication[x]", "CodeableConcept|Reference(ObservationDefinition)", "Indication for use that apply to the specific administration guidelines.", 0, 1, indication);
          case -1094003035: /*indicationCodeableConcept*/  return new Property("indication[x]", "CodeableConcept|Reference(ObservationDefinition)", "Indication for use that apply to the specific administration guidelines.", 0, 1, indication);
          case 803518799: /*indicationReference*/  return new Property("indication[x]", "CodeableConcept|Reference(ObservationDefinition)", "Indication for use that apply to the specific administration guidelines.", 0, 1, indication);
          case -960531341: /*patientCharacteristics*/  return new Property("patientCharacteristics", "", "Characteristics of the patient that are relevant to the administration guidelines (for example, height, weight,gender,  etc).", 0, java.lang.Integer.MAX_VALUE, patientCharacteristics);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1326018889: /*dosage*/ return this.dosage == null ? new Base[0] : this.dosage.toArray(new Base[this.dosage.size()]); // Dosage
        case -597168804: /*indication*/ return this.indication == null ? new Base[0] : new Base[] {this.indication}; // Type
        case -960531341: /*patientCharacteristics*/ return this.patientCharacteristics == null ? new Base[0] : this.patientCharacteristics.toArray(new Base[this.patientCharacteristics.size()]); // MedicationKnowledgeAdministrationGuidelinesPatientCharacteristicsComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1326018889: // dosage
          this.getDosage().add(castToDosage(value)); // Dosage
          return value;
        case -597168804: // indication
          this.indication = castToType(value); // Type
          return value;
        case -960531341: // patientCharacteristics
          this.getPatientCharacteristics().add((MedicationKnowledgeAdministrationGuidelinesPatientCharacteristicsComponent) value); // MedicationKnowledgeAdministrationGuidelinesPatientCharacteristicsComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("dosage")) {
          this.getDosage().add(castToDosage(value));
        } else if (name.equals("indication[x]")) {
          this.indication = castToType(value); // Type
        } else if (name.equals("patientCharacteristics")) {
          this.getPatientCharacteristics().add((MedicationKnowledgeAdministrationGuidelinesPatientCharacteristicsComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1326018889:  return addDosage(); 
        case -501208668:  return getIndication(); 
        case -597168804:  return getIndication(); 
        case -960531341:  return addPatientCharacteristics(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1326018889: /*dosage*/ return new String[] {"Dosage"};
        case -597168804: /*indication*/ return new String[] {"CodeableConcept", "Reference"};
        case -960531341: /*patientCharacteristics*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("dosage")) {
          return addDosage();
        }
        else if (name.equals("indicationCodeableConcept")) {
          this.indication = new CodeableConcept();
          return this.indication;
        }
        else if (name.equals("indicationReference")) {
          this.indication = new Reference();
          return this.indication;
        }
        else if (name.equals("patientCharacteristics")) {
          return addPatientCharacteristics();
        }
        else
          return super.addChild(name);
      }

      public MedicationKnowledgeAdministrationGuidelinesComponent copy() {
        MedicationKnowledgeAdministrationGuidelinesComponent dst = new MedicationKnowledgeAdministrationGuidelinesComponent();
        copyValues(dst);
        if (dosage != null) {
          dst.dosage = new ArrayList<Dosage>();
          for (Dosage i : dosage)
            dst.dosage.add(i.copy());
        };
        dst.indication = indication == null ? null : indication.copy();
        if (patientCharacteristics != null) {
          dst.patientCharacteristics = new ArrayList<MedicationKnowledgeAdministrationGuidelinesPatientCharacteristicsComponent>();
          for (MedicationKnowledgeAdministrationGuidelinesPatientCharacteristicsComponent i : patientCharacteristics)
            dst.patientCharacteristics.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof MedicationKnowledgeAdministrationGuidelinesComponent))
          return false;
        MedicationKnowledgeAdministrationGuidelinesComponent o = (MedicationKnowledgeAdministrationGuidelinesComponent) other_;
        return compareDeep(dosage, o.dosage, true) && compareDeep(indication, o.indication, true) && compareDeep(patientCharacteristics, o.patientCharacteristics, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof MedicationKnowledgeAdministrationGuidelinesComponent))
          return false;
        MedicationKnowledgeAdministrationGuidelinesComponent o = (MedicationKnowledgeAdministrationGuidelinesComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(dosage, indication, patientCharacteristics
          );
      }

  public String fhirType() {
    return "MedicationKnowledge.administrationGuidelines";

  }

  }

    @Block()
    public static class MedicationKnowledgeAdministrationGuidelinesPatientCharacteristicsComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Specific characteristic that is relevant to the administration guideline (e.g. height, weight, gender).
         */
        @Child(name = "characteristic", type = {CodeableConcept.class, SimpleQuantity.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Specific characteristic that is relevant to the administration guideline", formalDefinition="Specific characteristic that is relevant to the administration guideline (e.g. height, weight, gender)." )
        protected Type characteristic;

        /**
         * The specific characteristic (e.g. height, weight, gender, etc).
         */
        @Child(name = "value", type = {StringType.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="The specific characteristic", formalDefinition="The specific characteristic (e.g. height, weight, gender, etc)." )
        protected List<StringType> value;

        private static final long serialVersionUID = -133608297L;

    /**
     * Constructor
     */
      public MedicationKnowledgeAdministrationGuidelinesPatientCharacteristicsComponent() {
        super();
      }

    /**
     * Constructor
     */
      public MedicationKnowledgeAdministrationGuidelinesPatientCharacteristicsComponent(Type characteristic) {
        super();
        this.characteristic = characteristic;
      }

        /**
         * @return {@link #characteristic} (Specific characteristic that is relevant to the administration guideline (e.g. height, weight, gender).)
         */
        public Type getCharacteristic() { 
          return this.characteristic;
        }

        /**
         * @return {@link #characteristic} (Specific characteristic that is relevant to the administration guideline (e.g. height, weight, gender).)
         */
        public CodeableConcept getCharacteristicCodeableConcept() throws FHIRException { 
          if (this.characteristic == null)
            return null;
          if (!(this.characteristic instanceof CodeableConcept))
            throw new FHIRException("Type mismatch: the type CodeableConcept was expected, but "+this.characteristic.getClass().getName()+" was encountered");
          return (CodeableConcept) this.characteristic;
        }

        public boolean hasCharacteristicCodeableConcept() { 
          return this != null && this.characteristic instanceof CodeableConcept;
        }

        /**
         * @return {@link #characteristic} (Specific characteristic that is relevant to the administration guideline (e.g. height, weight, gender).)
         */
        public SimpleQuantity getCharacteristicSimpleQuantity() throws FHIRException { 
          if (this.characteristic == null)
            return null;
          if (!(this.characteristic instanceof SimpleQuantity))
            throw new FHIRException("Type mismatch: the type SimpleQuantity was expected, but "+this.characteristic.getClass().getName()+" was encountered");
          return (SimpleQuantity) this.characteristic;
        }

        public boolean hasCharacteristicSimpleQuantity() { 
          return this != null && this.characteristic instanceof SimpleQuantity;
        }

        public boolean hasCharacteristic() { 
          return this.characteristic != null && !this.characteristic.isEmpty();
        }

        /**
         * @param value {@link #characteristic} (Specific characteristic that is relevant to the administration guideline (e.g. height, weight, gender).)
         */
        public MedicationKnowledgeAdministrationGuidelinesPatientCharacteristicsComponent setCharacteristic(Type value) { 
          if (value != null && !(value instanceof CodeableConcept || value instanceof SimpleQuantity))
            throw new Error("Not the right type for MedicationKnowledge.administrationGuidelines.patientCharacteristics.characteristic[x]: "+value.fhirType());
          this.characteristic = value;
          return this;
        }

        /**
         * @return {@link #value} (The specific characteristic (e.g. height, weight, gender, etc).)
         */
        public List<StringType> getValue() { 
          if (this.value == null)
            this.value = new ArrayList<StringType>();
          return this.value;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public MedicationKnowledgeAdministrationGuidelinesPatientCharacteristicsComponent setValue(List<StringType> theValue) { 
          this.value = theValue;
          return this;
        }

        public boolean hasValue() { 
          if (this.value == null)
            return false;
          for (StringType item : this.value)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #value} (The specific characteristic (e.g. height, weight, gender, etc).)
         */
        public StringType addValueElement() {//2 
          StringType t = new StringType();
          if (this.value == null)
            this.value = new ArrayList<StringType>();
          this.value.add(t);
          return t;
        }

        /**
         * @param value {@link #value} (The specific characteristic (e.g. height, weight, gender, etc).)
         */
        public MedicationKnowledgeAdministrationGuidelinesPatientCharacteristicsComponent addValue(String value) { //1
          StringType t = new StringType();
          t.setValue(value);
          if (this.value == null)
            this.value = new ArrayList<StringType>();
          this.value.add(t);
          return this;
        }

        /**
         * @param value {@link #value} (The specific characteristic (e.g. height, weight, gender, etc).)
         */
        public boolean hasValue(String value) { 
          if (this.value == null)
            return false;
          for (StringType v : this.value)
            if (v.getValue().equals(value)) // string
              return true;
          return false;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("characteristic[x]", "CodeableConcept|SimpleQuantity", "Specific characteristic that is relevant to the administration guideline (e.g. height, weight, gender).", 0, 1, characteristic));
          children.add(new Property("value", "string", "The specific characteristic (e.g. height, weight, gender, etc).", 0, java.lang.Integer.MAX_VALUE, value));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -654919419: /*characteristic[x]*/  return new Property("characteristic[x]", "CodeableConcept|SimpleQuantity", "Specific characteristic that is relevant to the administration guideline (e.g. height, weight, gender).", 0, 1, characteristic);
          case 366313883: /*characteristic*/  return new Property("characteristic[x]", "CodeableConcept|SimpleQuantity", "Specific characteristic that is relevant to the administration guideline (e.g. height, weight, gender).", 0, 1, characteristic);
          case -1259840378: /*characteristicCodeableConcept*/  return new Property("characteristic[x]", "CodeableConcept|SimpleQuantity", "Specific characteristic that is relevant to the administration guideline (e.g. height, weight, gender).", 0, 1, characteristic);
          case 1947142872: /*characteristicSimpleQuantity*/  return new Property("characteristic[x]", "CodeableConcept|SimpleQuantity", "Specific characteristic that is relevant to the administration guideline (e.g. height, weight, gender).", 0, 1, characteristic);
          case 111972721: /*value*/  return new Property("value", "string", "The specific characteristic (e.g. height, weight, gender, etc).", 0, java.lang.Integer.MAX_VALUE, value);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 366313883: /*characteristic*/ return this.characteristic == null ? new Base[0] : new Base[] {this.characteristic}; // Type
        case 111972721: /*value*/ return this.value == null ? new Base[0] : this.value.toArray(new Base[this.value.size()]); // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 366313883: // characteristic
          this.characteristic = castToType(value); // Type
          return value;
        case 111972721: // value
          this.getValue().add(castToString(value)); // StringType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("characteristic[x]")) {
          this.characteristic = castToType(value); // Type
        } else if (name.equals("value")) {
          this.getValue().add(castToString(value));
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -654919419:  return getCharacteristic(); 
        case 366313883:  return getCharacteristic(); 
        case 111972721:  return addValueElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 366313883: /*characteristic*/ return new String[] {"CodeableConcept", "SimpleQuantity"};
        case 111972721: /*value*/ return new String[] {"string"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("characteristicCodeableConcept")) {
          this.characteristic = new CodeableConcept();
          return this.characteristic;
        }
        else if (name.equals("characteristicSimpleQuantity")) {
          this.characteristic = new SimpleQuantity();
          return this.characteristic;
        }
        else if (name.equals("value")) {
          throw new FHIRException("Cannot call addChild on a primitive type MedicationKnowledge.value");
        }
        else
          return super.addChild(name);
      }

      public MedicationKnowledgeAdministrationGuidelinesPatientCharacteristicsComponent copy() {
        MedicationKnowledgeAdministrationGuidelinesPatientCharacteristicsComponent dst = new MedicationKnowledgeAdministrationGuidelinesPatientCharacteristicsComponent();
        copyValues(dst);
        dst.characteristic = characteristic == null ? null : characteristic.copy();
        if (value != null) {
          dst.value = new ArrayList<StringType>();
          for (StringType i : value)
            dst.value.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof MedicationKnowledgeAdministrationGuidelinesPatientCharacteristicsComponent))
          return false;
        MedicationKnowledgeAdministrationGuidelinesPatientCharacteristicsComponent o = (MedicationKnowledgeAdministrationGuidelinesPatientCharacteristicsComponent) other_;
        return compareDeep(characteristic, o.characteristic, true) && compareDeep(value, o.value, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof MedicationKnowledgeAdministrationGuidelinesPatientCharacteristicsComponent))
          return false;
        MedicationKnowledgeAdministrationGuidelinesPatientCharacteristicsComponent o = (MedicationKnowledgeAdministrationGuidelinesPatientCharacteristicsComponent) other_;
        return compareValues(value, o.value, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(characteristic, value);
      }

  public String fhirType() {
    return "MedicationKnowledge.administrationGuidelines.patientCharacteristics";

  }

  }

    @Block()
    public static class MedicationKnowledgeMedicineClassificationComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The type of category for the medication (for example, therapeutic classification, therapeutic sub-classification).
         */
        @Child(name = "type", type = {CodeableConcept.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The type of category for the medication (for example, therapeutic classification, therapeutic sub-classification)", formalDefinition="The type of category for the medication (for example, therapeutic classification, therapeutic sub-classification)." )
        protected CodeableConcept type;

        /**
         * Specific category assigned to the medication (e.g. anti-infective, anti-hypertensive, antibiotic, etc).
         */
        @Child(name = "classification", type = {CodeableConcept.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Specific category assigned to the medication", formalDefinition="Specific category assigned to the medication (e.g. anti-infective, anti-hypertensive, antibiotic, etc)." )
        protected List<CodeableConcept> classification;

        private static final long serialVersionUID = 1562996046L;

    /**
     * Constructor
     */
      public MedicationKnowledgeMedicineClassificationComponent() {
        super();
      }

    /**
     * Constructor
     */
      public MedicationKnowledgeMedicineClassificationComponent(CodeableConcept type) {
        super();
        this.type = type;
      }

        /**
         * @return {@link #type} (The type of category for the medication (for example, therapeutic classification, therapeutic sub-classification).)
         */
        public CodeableConcept getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationKnowledgeMedicineClassificationComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new CodeableConcept(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (The type of category for the medication (for example, therapeutic classification, therapeutic sub-classification).)
         */
        public MedicationKnowledgeMedicineClassificationComponent setType(CodeableConcept value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #classification} (Specific category assigned to the medication (e.g. anti-infective, anti-hypertensive, antibiotic, etc).)
         */
        public List<CodeableConcept> getClassification() { 
          if (this.classification == null)
            this.classification = new ArrayList<CodeableConcept>();
          return this.classification;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public MedicationKnowledgeMedicineClassificationComponent setClassification(List<CodeableConcept> theClassification) { 
          this.classification = theClassification;
          return this;
        }

        public boolean hasClassification() { 
          if (this.classification == null)
            return false;
          for (CodeableConcept item : this.classification)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public CodeableConcept addClassification() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.classification == null)
            this.classification = new ArrayList<CodeableConcept>();
          this.classification.add(t);
          return t;
        }

        public MedicationKnowledgeMedicineClassificationComponent addClassification(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.classification == null)
            this.classification = new ArrayList<CodeableConcept>();
          this.classification.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #classification}, creating it if it does not already exist
         */
        public CodeableConcept getClassificationFirstRep() { 
          if (getClassification().isEmpty()) {
            addClassification();
          }
          return getClassification().get(0);
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("type", "CodeableConcept", "The type of category for the medication (for example, therapeutic classification, therapeutic sub-classification).", 0, 1, type));
          children.add(new Property("classification", "CodeableConcept", "Specific category assigned to the medication (e.g. anti-infective, anti-hypertensive, antibiotic, etc).", 0, java.lang.Integer.MAX_VALUE, classification));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3575610: /*type*/  return new Property("type", "CodeableConcept", "The type of category for the medication (for example, therapeutic classification, therapeutic sub-classification).", 0, 1, type);
          case 382350310: /*classification*/  return new Property("classification", "CodeableConcept", "Specific category assigned to the medication (e.g. anti-infective, anti-hypertensive, antibiotic, etc).", 0, java.lang.Integer.MAX_VALUE, classification);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case 382350310: /*classification*/ return this.classification == null ? new Base[0] : this.classification.toArray(new Base[this.classification.size()]); // CodeableConcept
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 382350310: // classification
          this.getClassification().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type")) {
          this.type = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("classification")) {
          this.getClassification().add(castToCodeableConcept(value));
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610:  return getType(); 
        case 382350310:  return addClassification(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case 382350310: /*classification*/ return new String[] {"CodeableConcept"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("classification")) {
          return addClassification();
        }
        else
          return super.addChild(name);
      }

      public MedicationKnowledgeMedicineClassificationComponent copy() {
        MedicationKnowledgeMedicineClassificationComponent dst = new MedicationKnowledgeMedicineClassificationComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        if (classification != null) {
          dst.classification = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : classification)
            dst.classification.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof MedicationKnowledgeMedicineClassificationComponent))
          return false;
        MedicationKnowledgeMedicineClassificationComponent o = (MedicationKnowledgeMedicineClassificationComponent) other_;
        return compareDeep(type, o.type, true) && compareDeep(classification, o.classification, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof MedicationKnowledgeMedicineClassificationComponent))
          return false;
        MedicationKnowledgeMedicineClassificationComponent o = (MedicationKnowledgeMedicineClassificationComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(type, classification);
      }

  public String fhirType() {
    return "MedicationKnowledge.medicineClassification";

  }

  }

    @Block()
    public static class MedicationKnowledgePackagingComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A code that defines the specific type of packaging that the medication can be found in (e.g. blister sleeve, tube, bottle).
         */
        @Child(name = "type", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="A code that defines the specific type of packaging that the medication can be found in", formalDefinition="A code that defines the specific type of packaging that the medication can be found in (e.g. blister sleeve, tube, bottle)." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/medicationKnowledge-package")
        protected CodeableConcept type;

        /**
         * The number of product units the package would contain if fully loaded.
         */
        @Child(name = "quantity", type = {SimpleQuantity.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The number of product units the package would contain if fully loaded", formalDefinition="The number of product units the package would contain if fully loaded." )
        protected SimpleQuantity quantity;

        private static final long serialVersionUID = -216496823L;

    /**
     * Constructor
     */
      public MedicationKnowledgePackagingComponent() {
        super();
      }

        /**
         * @return {@link #type} (A code that defines the specific type of packaging that the medication can be found in (e.g. blister sleeve, tube, bottle).)
         */
        public CodeableConcept getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationKnowledgePackagingComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new CodeableConcept(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (A code that defines the specific type of packaging that the medication can be found in (e.g. blister sleeve, tube, bottle).)
         */
        public MedicationKnowledgePackagingComponent setType(CodeableConcept value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #quantity} (The number of product units the package would contain if fully loaded.)
         */
        public SimpleQuantity getQuantity() { 
          if (this.quantity == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationKnowledgePackagingComponent.quantity");
            else if (Configuration.doAutoCreate())
              this.quantity = new SimpleQuantity(); // cc
          return this.quantity;
        }

        public boolean hasQuantity() { 
          return this.quantity != null && !this.quantity.isEmpty();
        }

        /**
         * @param value {@link #quantity} (The number of product units the package would contain if fully loaded.)
         */
        public MedicationKnowledgePackagingComponent setQuantity(SimpleQuantity value) { 
          this.quantity = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("type", "CodeableConcept", "A code that defines the specific type of packaging that the medication can be found in (e.g. blister sleeve, tube, bottle).", 0, 1, type));
          children.add(new Property("quantity", "SimpleQuantity", "The number of product units the package would contain if fully loaded.", 0, 1, quantity));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3575610: /*type*/  return new Property("type", "CodeableConcept", "A code that defines the specific type of packaging that the medication can be found in (e.g. blister sleeve, tube, bottle).", 0, 1, type);
          case -1285004149: /*quantity*/  return new Property("quantity", "SimpleQuantity", "The number of product units the package would contain if fully loaded.", 0, 1, quantity);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case -1285004149: /*quantity*/ return this.quantity == null ? new Base[0] : new Base[] {this.quantity}; // SimpleQuantity
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1285004149: // quantity
          this.quantity = castToSimpleQuantity(value); // SimpleQuantity
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type")) {
          this.type = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("quantity")) {
          this.quantity = castToSimpleQuantity(value); // SimpleQuantity
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610:  return getType(); 
        case -1285004149:  return getQuantity(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case -1285004149: /*quantity*/ return new String[] {"SimpleQuantity"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("quantity")) {
          this.quantity = new SimpleQuantity();
          return this.quantity;
        }
        else
          return super.addChild(name);
      }

      public MedicationKnowledgePackagingComponent copy() {
        MedicationKnowledgePackagingComponent dst = new MedicationKnowledgePackagingComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.quantity = quantity == null ? null : quantity.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof MedicationKnowledgePackagingComponent))
          return false;
        MedicationKnowledgePackagingComponent o = (MedicationKnowledgePackagingComponent) other_;
        return compareDeep(type, o.type, true) && compareDeep(quantity, o.quantity, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof MedicationKnowledgePackagingComponent))
          return false;
        MedicationKnowledgePackagingComponent o = (MedicationKnowledgePackagingComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(type, quantity);
      }

  public String fhirType() {
    return "MedicationKnowledge.packaging";

  }

  }

    @Block()
    public static class MedicationKnowledgeDrugCharacteristicComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A code specifying which characteristic of the medicine is being described (for example, colour, shape, imprint).
         */
        @Child(name = "type", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Code specifying the type of characteristic of medication", formalDefinition="A code specifying which characteristic of the medicine is being described (for example, colour, shape, imprint)." )
        protected CodeableConcept type;

        /**
         * Description of the characteristic.
         */
        @Child(name = "value", type = {CodeableConcept.class, StringType.class, SimpleQuantity.class, Base64BinaryType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Description of the characteristic", formalDefinition="Description of the characteristic." )
        protected Type value;

        private static final long serialVersionUID = -491121170L;

    /**
     * Constructor
     */
      public MedicationKnowledgeDrugCharacteristicComponent() {
        super();
      }

        /**
         * @return {@link #type} (A code specifying which characteristic of the medicine is being described (for example, colour, shape, imprint).)
         */
        public CodeableConcept getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationKnowledgeDrugCharacteristicComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new CodeableConcept(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (A code specifying which characteristic of the medicine is being described (for example, colour, shape, imprint).)
         */
        public MedicationKnowledgeDrugCharacteristicComponent setType(CodeableConcept value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #value} (Description of the characteristic.)
         */
        public Type getValue() { 
          return this.value;
        }

        /**
         * @return {@link #value} (Description of the characteristic.)
         */
        public CodeableConcept getValueCodeableConcept() throws FHIRException { 
          if (this.value == null)
            return null;
          if (!(this.value instanceof CodeableConcept))
            throw new FHIRException("Type mismatch: the type CodeableConcept was expected, but "+this.value.getClass().getName()+" was encountered");
          return (CodeableConcept) this.value;
        }

        public boolean hasValueCodeableConcept() { 
          return this != null && this.value instanceof CodeableConcept;
        }

        /**
         * @return {@link #value} (Description of the characteristic.)
         */
        public StringType getValueStringType() throws FHIRException { 
          if (this.value == null)
            return null;
          if (!(this.value instanceof StringType))
            throw new FHIRException("Type mismatch: the type StringType was expected, but "+this.value.getClass().getName()+" was encountered");
          return (StringType) this.value;
        }

        public boolean hasValueStringType() { 
          return this != null && this.value instanceof StringType;
        }

        /**
         * @return {@link #value} (Description of the characteristic.)
         */
        public SimpleQuantity getValueSimpleQuantity() throws FHIRException { 
          if (this.value == null)
            return null;
          if (!(this.value instanceof SimpleQuantity))
            throw new FHIRException("Type mismatch: the type SimpleQuantity was expected, but "+this.value.getClass().getName()+" was encountered");
          return (SimpleQuantity) this.value;
        }

        public boolean hasValueSimpleQuantity() { 
          return this != null && this.value instanceof SimpleQuantity;
        }

        /**
         * @return {@link #value} (Description of the characteristic.)
         */
        public Base64BinaryType getValueBase64BinaryType() throws FHIRException { 
          if (this.value == null)
            return null;
          if (!(this.value instanceof Base64BinaryType))
            throw new FHIRException("Type mismatch: the type Base64BinaryType was expected, but "+this.value.getClass().getName()+" was encountered");
          return (Base64BinaryType) this.value;
        }

        public boolean hasValueBase64BinaryType() { 
          return this != null && this.value instanceof Base64BinaryType;
        }

        public boolean hasValue() { 
          return this.value != null && !this.value.isEmpty();
        }

        /**
         * @param value {@link #value} (Description of the characteristic.)
         */
        public MedicationKnowledgeDrugCharacteristicComponent setValue(Type value) { 
          if (value != null && !(value instanceof CodeableConcept || value instanceof StringType || value instanceof SimpleQuantity || value instanceof Base64BinaryType))
            throw new Error("Not the right type for MedicationKnowledge.drugCharacteristic.value[x]: "+value.fhirType());
          this.value = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("type", "CodeableConcept", "A code specifying which characteristic of the medicine is being described (for example, colour, shape, imprint).", 0, 1, type));
          children.add(new Property("value[x]", "CodeableConcept|string|SimpleQuantity|base64Binary", "Description of the characteristic.", 0, 1, value));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3575610: /*type*/  return new Property("type", "CodeableConcept", "A code specifying which characteristic of the medicine is being described (for example, colour, shape, imprint).", 0, 1, type);
          case -1410166417: /*value[x]*/  return new Property("value[x]", "CodeableConcept|string|SimpleQuantity|base64Binary", "Description of the characteristic.", 0, 1, value);
          case 111972721: /*value*/  return new Property("value[x]", "CodeableConcept|string|SimpleQuantity|base64Binary", "Description of the characteristic.", 0, 1, value);
          case 924902896: /*valueCodeableConcept*/  return new Property("value[x]", "CodeableConcept|string|SimpleQuantity|base64Binary", "Description of the characteristic.", 0, 1, value);
          case -1424603934: /*valueString*/  return new Property("value[x]", "CodeableConcept|string|SimpleQuantity|base64Binary", "Description of the characteristic.", 0, 1, value);
          case -1723159506: /*valueSimpleQuantity*/  return new Property("value[x]", "CodeableConcept|string|SimpleQuantity|base64Binary", "Description of the characteristic.", 0, 1, value);
          case -1535024575: /*valueBase64Binary*/  return new Property("value[x]", "CodeableConcept|string|SimpleQuantity|base64Binary", "Description of the characteristic.", 0, 1, value);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case 111972721: /*value*/ return this.value == null ? new Base[0] : new Base[] {this.value}; // Type
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 111972721: // value
          this.value = castToType(value); // Type
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type")) {
          this.type = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("value[x]")) {
          this.value = castToType(value); // Type
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610:  return getType(); 
        case -1410166417:  return getValue(); 
        case 111972721:  return getValue(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case 111972721: /*value*/ return new String[] {"CodeableConcept", "string", "SimpleQuantity", "base64Binary"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("valueCodeableConcept")) {
          this.value = new CodeableConcept();
          return this.value;
        }
        else if (name.equals("valueString")) {
          this.value = new StringType();
          return this.value;
        }
        else if (name.equals("valueSimpleQuantity")) {
          this.value = new SimpleQuantity();
          return this.value;
        }
        else if (name.equals("valueBase64Binary")) {
          this.value = new Base64BinaryType();
          return this.value;
        }
        else
          return super.addChild(name);
      }

      public MedicationKnowledgeDrugCharacteristicComponent copy() {
        MedicationKnowledgeDrugCharacteristicComponent dst = new MedicationKnowledgeDrugCharacteristicComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.value = value == null ? null : value.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof MedicationKnowledgeDrugCharacteristicComponent))
          return false;
        MedicationKnowledgeDrugCharacteristicComponent o = (MedicationKnowledgeDrugCharacteristicComponent) other_;
        return compareDeep(type, o.type, true) && compareDeep(value, o.value, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof MedicationKnowledgeDrugCharacteristicComponent))
          return false;
        MedicationKnowledgeDrugCharacteristicComponent o = (MedicationKnowledgeDrugCharacteristicComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(type, value);
      }

  public String fhirType() {
    return "MedicationKnowledge.drugCharacteristic";

  }

  }

    /**
     * A code that specifies this medication, or a textual description if no code is available. Usage note: This could be a standard medication code such as a code from RxNorm, SNOMED CT, IDMP etc. It could also be a national or local formulary code, optionally with translations to other code systems.
     */
    @Child(name = "code", type = {CodeableConcept.class}, order=0, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Code that identifies this medication", formalDefinition="A code that specifies this medication, or a textual description if no code is available. Usage note: This could be a standard medication code such as a code from RxNorm, SNOMED CT, IDMP etc. It could also be a national or local formulary code, optionally with translations to other code systems." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/medication-codes")
    protected CodeableConcept code;

    /**
     * A code to indicate if the medication is in active use.
     */
    @Child(name = "status", type = {CodeType.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="active | inactive | entered-in-error", formalDefinition="A code to indicate if the medication is in active use." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/medicationKnowledge-status")
    protected Enumeration<MedicationKnowledgeStatus> status;

    /**
     * Describes the details of the manufacturer of the medication product.  This is not intended to represent the distributor of a medication product.
     */
    @Child(name = "manufacturer", type = {Organization.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Manufacturer of the item", formalDefinition="Describes the details of the manufacturer of the medication product.  This is not intended to represent the distributor of a medication product." )
    protected Reference manufacturer;

    /**
     * The actual object that is the target of the reference (Describes the details of the manufacturer of the medication product.  This is not intended to represent the distributor of a medication product.)
     */
    protected Organization manufacturerTarget;

    /**
     * Describes the form of the item.  Powder; tablets; capsule.
     */
    @Child(name = "form", type = {CodeableConcept.class}, order=3, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="powder | tablets | capsule +", formalDefinition="Describes the form of the item.  Powder; tablets; capsule." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/medication-form-codes")
    protected CodeableConcept form;

    /**
     * Specific amount of the drug in the packaged product.  For example, when specifying a product that has the same strength (For example, Insulin glargine 100 unit per mL solution for injection), this attribute provides additional clarification of the package amount (For example, 3 mL, 10mL, etc).
     */
    @Child(name = "amount", type = {SimpleQuantity.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Amount of drug in package", formalDefinition="Specific amount of the drug in the packaged product.  For example, when specifying a product that has the same strength (For example, Insulin glargine 100 unit per mL solution for injection), this attribute provides additional clarification of the package amount (For example, 3 mL, 10mL, etc)." )
    protected SimpleQuantity amount;

    /**
     * Additional names for a medication, for example, the name(s) given to a medication in different countries.  For example, acetaminophen and paracetamol or salbutamol and albuterol.
     */
    @Child(name = "synonym", type = {StringType.class}, order=5, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Additional names for a medication", formalDefinition="Additional names for a medication, for example, the name(s) given to a medication in different countries.  For example, acetaminophen and paracetamol or salbutamol and albuterol." )
    protected List<StringType> synonym;

    /**
     * Associated or related knowledge about a medication.
     */
    @Child(name = "relatedMedicationKnowledge", type = {MedicationKnowledge.class}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Associated or related medication information", formalDefinition="Associated or related knowledge about a medication." )
    protected List<Reference> relatedMedicationKnowledge;
    /**
     * The actual objects that are the target of the reference (Associated or related knowledge about a medication.)
     */
    protected List<MedicationKnowledge> relatedMedicationKnowledgeTarget;


    /**
     * Associated or related medications.  For example, if the medication is a branded product (e.g. Crestor), this is the Therapeutic Moeity (e.g. Rosuvastatin) or if this is a generic medication (e.g. Rosuvastatin), this would link to a branded product (e.g. Crestor).
     */
    @Child(name = "associatedMedication", type = {Medication.class}, order=7, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="A medication resource that is associated with this medication", formalDefinition="Associated or related medications.  For example, if the medication is a branded product (e.g. Crestor), this is the Therapeutic Moeity (e.g. Rosuvastatin) or if this is a generic medication (e.g. Rosuvastatin), this would link to a branded product (e.g. Crestor)." )
    protected Reference associatedMedication;

    /**
     * The actual object that is the target of the reference (Associated or related medications.  For example, if the medication is a branded product (e.g. Crestor), this is the Therapeutic Moeity (e.g. Rosuvastatin) or if this is a generic medication (e.g. Rosuvastatin), this would link to a branded product (e.g. Crestor).)
     */
    protected Medication associatedMedicationTarget;

    /**
     * Category of the medication or product (e.g. branded product, therapeutic moeity, generic product, innovator product, etc).
     */
    @Child(name = "productType", type = {CodeableConcept.class}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Category of the medication or product", formalDefinition="Category of the medication or product (e.g. branded product, therapeutic moeity, generic product, innovator product, etc)." )
    protected List<CodeableConcept> productType;

    /**
     * Associated documentation about the medication.
     */
    @Child(name = "monograph", type = {}, order=9, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Associated documentation about the medication", formalDefinition="Associated documentation about the medication." )
    protected List<MedicationKnowledgeMonographComponent> monograph;

    /**
     * The time required for any specified property (e.g., the concentration of a substance in the body) to decrease by half.
     */
    @Child(name = "halfLifePeriod", type = {Duration.class}, order=10, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Time required for concentration in the body to decrease by half", formalDefinition="The time required for any specified property (e.g., the concentration of a substance in the body) to decrease by half." )
    protected Duration halfLifePeriod;

    /**
     * Identifies a particular constituent of interest in the product.
     */
    @Child(name = "ingredient", type = {}, order=11, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Active or inactive ingredient", formalDefinition="Identifies a particular constituent of interest in the product." )
    protected List<MedicationKnowledgeIngredientComponent> ingredient;

    /**
     * The instructions for preparing the medication.
     */
    @Child(name = "preparationInstruction", type = {MarkdownType.class}, order=12, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="The instructions for preparing the medication", formalDefinition="The instructions for preparing the medication." )
    protected MarkdownType preparationInstruction;

    /**
     * The intended or approved route of administration.
     */
    @Child(name = "intendedRoute", type = {CodeableConcept.class}, order=13, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="The intended or approved route of administration", formalDefinition="The intended or approved route of administration." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/route-codes")
    protected List<CodeableConcept> intendedRoute;

    /**
     * The price of the medication.
     */
    @Child(name = "cost", type = {}, order=14, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="The pricing of the medication", formalDefinition="The price of the medication." )
    protected List<MedicationKnowledgeCostComponent> cost;

    /**
     * The program under which the medication is reviewed.
     */
    @Child(name = "monitoringProgram", type = {}, order=15, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Program under which a medication is reviewed", formalDefinition="The program under which the medication is reviewed." )
    protected List<MedicationKnowledgeMonitoringProgramComponent> monitoringProgram;

    /**
     * Guidelines for the administration of the medication.
     */
    @Child(name = "administrationGuidelines", type = {}, order=16, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Gudelines for administration of the medication", formalDefinition="Guidelines for the administration of the medication." )
    protected List<MedicationKnowledgeAdministrationGuidelinesComponent> administrationGuidelines;

    /**
     * Categorization of the medication within a formulary or classification system.
     */
    @Child(name = "medicineClassification", type = {}, order=17, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Categorization of the medication within a formulary or classification system", formalDefinition="Categorization of the medication within a formulary or classification system." )
    protected List<MedicationKnowledgeMedicineClassificationComponent> medicineClassification;

    /**
     * Information that only applies to packages (not products).
     */
    @Child(name = "packaging", type = {}, order=18, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Details about packaged medications", formalDefinition="Information that only applies to packages (not products)." )
    protected MedicationKnowledgePackagingComponent packaging;

    /**
     * Specifies descriptive properties of the medicine, such as color, shape, imprints, etc.
     */
    @Child(name = "drugCharacteristic", type = {}, order=19, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Specifies descriptive properties of the medicine", formalDefinition="Specifies descriptive properties of the medicine, such as color, shape, imprints, etc." )
    protected List<MedicationKnowledgeDrugCharacteristicComponent> drugCharacteristic;

    /**
     * Potential clinical issue with or between medication(s) (for example, drug-drug interaction, drug-disease contraindication, drug-allergy interaction, etc).
     */
    @Child(name = "contraindication", type = {DetectedIssue.class}, order=20, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Potential clinical issue with or between medication(s)", formalDefinition="Potential clinical issue with or between medication(s) (for example, drug-drug interaction, drug-disease contraindication, drug-allergy interaction, etc)." )
    protected List<Reference> contraindication;
    /**
     * The actual objects that are the target of the reference (Potential clinical issue with or between medication(s) (for example, drug-drug interaction, drug-disease contraindication, drug-allergy interaction, etc).)
     */
    protected List<DetectedIssue> contraindicationTarget;


    private static final long serialVersionUID = 854168093L;

  /**
   * Constructor
   */
    public MedicationKnowledge() {
      super();
    }

    /**
     * @return {@link #code} (A code that specifies this medication, or a textual description if no code is available. Usage note: This could be a standard medication code such as a code from RxNorm, SNOMED CT, IDMP etc. It could also be a national or local formulary code, optionally with translations to other code systems.)
     */
    public CodeableConcept getCode() { 
      if (this.code == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationKnowledge.code");
        else if (Configuration.doAutoCreate())
          this.code = new CodeableConcept(); // cc
      return this.code;
    }

    public boolean hasCode() { 
      return this.code != null && !this.code.isEmpty();
    }

    /**
     * @param value {@link #code} (A code that specifies this medication, or a textual description if no code is available. Usage note: This could be a standard medication code such as a code from RxNorm, SNOMED CT, IDMP etc. It could also be a national or local formulary code, optionally with translations to other code systems.)
     */
    public MedicationKnowledge setCode(CodeableConcept value) { 
      this.code = value;
      return this;
    }

    /**
     * @return {@link #status} (A code to indicate if the medication is in active use.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<MedicationKnowledgeStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationKnowledge.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<MedicationKnowledgeStatus>(new MedicationKnowledgeStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (A code to indicate if the medication is in active use.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public MedicationKnowledge setStatusElement(Enumeration<MedicationKnowledgeStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return A code to indicate if the medication is in active use.
     */
    public MedicationKnowledgeStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value A code to indicate if the medication is in active use.
     */
    public MedicationKnowledge setStatus(MedicationKnowledgeStatus value) { 
      if (value == null)
        this.status = null;
      else {
        if (this.status == null)
          this.status = new Enumeration<MedicationKnowledgeStatus>(new MedicationKnowledgeStatusEnumFactory());
        this.status.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #manufacturer} (Describes the details of the manufacturer of the medication product.  This is not intended to represent the distributor of a medication product.)
     */
    public Reference getManufacturer() { 
      if (this.manufacturer == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationKnowledge.manufacturer");
        else if (Configuration.doAutoCreate())
          this.manufacturer = new Reference(); // cc
      return this.manufacturer;
    }

    public boolean hasManufacturer() { 
      return this.manufacturer != null && !this.manufacturer.isEmpty();
    }

    /**
     * @param value {@link #manufacturer} (Describes the details of the manufacturer of the medication product.  This is not intended to represent the distributor of a medication product.)
     */
    public MedicationKnowledge setManufacturer(Reference value) { 
      this.manufacturer = value;
      return this;
    }

    /**
     * @return {@link #manufacturer} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Describes the details of the manufacturer of the medication product.  This is not intended to represent the distributor of a medication product.)
     */
    public Organization getManufacturerTarget() { 
      if (this.manufacturerTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationKnowledge.manufacturer");
        else if (Configuration.doAutoCreate())
          this.manufacturerTarget = new Organization(); // aa
      return this.manufacturerTarget;
    }

    /**
     * @param value {@link #manufacturer} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Describes the details of the manufacturer of the medication product.  This is not intended to represent the distributor of a medication product.)
     */
    public MedicationKnowledge setManufacturerTarget(Organization value) { 
      this.manufacturerTarget = value;
      return this;
    }

    /**
     * @return {@link #form} (Describes the form of the item.  Powder; tablets; capsule.)
     */
    public CodeableConcept getForm() { 
      if (this.form == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationKnowledge.form");
        else if (Configuration.doAutoCreate())
          this.form = new CodeableConcept(); // cc
      return this.form;
    }

    public boolean hasForm() { 
      return this.form != null && !this.form.isEmpty();
    }

    /**
     * @param value {@link #form} (Describes the form of the item.  Powder; tablets; capsule.)
     */
    public MedicationKnowledge setForm(CodeableConcept value) { 
      this.form = value;
      return this;
    }

    /**
     * @return {@link #amount} (Specific amount of the drug in the packaged product.  For example, when specifying a product that has the same strength (For example, Insulin glargine 100 unit per mL solution for injection), this attribute provides additional clarification of the package amount (For example, 3 mL, 10mL, etc).)
     */
    public SimpleQuantity getAmount() { 
      if (this.amount == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationKnowledge.amount");
        else if (Configuration.doAutoCreate())
          this.amount = new SimpleQuantity(); // cc
      return this.amount;
    }

    public boolean hasAmount() { 
      return this.amount != null && !this.amount.isEmpty();
    }

    /**
     * @param value {@link #amount} (Specific amount of the drug in the packaged product.  For example, when specifying a product that has the same strength (For example, Insulin glargine 100 unit per mL solution for injection), this attribute provides additional clarification of the package amount (For example, 3 mL, 10mL, etc).)
     */
    public MedicationKnowledge setAmount(SimpleQuantity value) { 
      this.amount = value;
      return this;
    }

    /**
     * @return {@link #synonym} (Additional names for a medication, for example, the name(s) given to a medication in different countries.  For example, acetaminophen and paracetamol or salbutamol and albuterol.)
     */
    public List<StringType> getSynonym() { 
      if (this.synonym == null)
        this.synonym = new ArrayList<StringType>();
      return this.synonym;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicationKnowledge setSynonym(List<StringType> theSynonym) { 
      this.synonym = theSynonym;
      return this;
    }

    public boolean hasSynonym() { 
      if (this.synonym == null)
        return false;
      for (StringType item : this.synonym)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #synonym} (Additional names for a medication, for example, the name(s) given to a medication in different countries.  For example, acetaminophen and paracetamol or salbutamol and albuterol.)
     */
    public StringType addSynonymElement() {//2 
      StringType t = new StringType();
      if (this.synonym == null)
        this.synonym = new ArrayList<StringType>();
      this.synonym.add(t);
      return t;
    }

    /**
     * @param value {@link #synonym} (Additional names for a medication, for example, the name(s) given to a medication in different countries.  For example, acetaminophen and paracetamol or salbutamol and albuterol.)
     */
    public MedicationKnowledge addSynonym(String value) { //1
      StringType t = new StringType();
      t.setValue(value);
      if (this.synonym == null)
        this.synonym = new ArrayList<StringType>();
      this.synonym.add(t);
      return this;
    }

    /**
     * @param value {@link #synonym} (Additional names for a medication, for example, the name(s) given to a medication in different countries.  For example, acetaminophen and paracetamol or salbutamol and albuterol.)
     */
    public boolean hasSynonym(String value) { 
      if (this.synonym == null)
        return false;
      for (StringType v : this.synonym)
        if (v.getValue().equals(value)) // string
          return true;
      return false;
    }

    /**
     * @return {@link #relatedMedicationKnowledge} (Associated or related knowledge about a medication.)
     */
    public List<Reference> getRelatedMedicationKnowledge() { 
      if (this.relatedMedicationKnowledge == null)
        this.relatedMedicationKnowledge = new ArrayList<Reference>();
      return this.relatedMedicationKnowledge;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicationKnowledge setRelatedMedicationKnowledge(List<Reference> theRelatedMedicationKnowledge) { 
      this.relatedMedicationKnowledge = theRelatedMedicationKnowledge;
      return this;
    }

    public boolean hasRelatedMedicationKnowledge() { 
      if (this.relatedMedicationKnowledge == null)
        return false;
      for (Reference item : this.relatedMedicationKnowledge)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addRelatedMedicationKnowledge() { //3
      Reference t = new Reference();
      if (this.relatedMedicationKnowledge == null)
        this.relatedMedicationKnowledge = new ArrayList<Reference>();
      this.relatedMedicationKnowledge.add(t);
      return t;
    }

    public MedicationKnowledge addRelatedMedicationKnowledge(Reference t) { //3
      if (t == null)
        return this;
      if (this.relatedMedicationKnowledge == null)
        this.relatedMedicationKnowledge = new ArrayList<Reference>();
      this.relatedMedicationKnowledge.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #relatedMedicationKnowledge}, creating it if it does not already exist
     */
    public Reference getRelatedMedicationKnowledgeFirstRep() { 
      if (getRelatedMedicationKnowledge().isEmpty()) {
        addRelatedMedicationKnowledge();
      }
      return getRelatedMedicationKnowledge().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<MedicationKnowledge> getRelatedMedicationKnowledgeTarget() { 
      if (this.relatedMedicationKnowledgeTarget == null)
        this.relatedMedicationKnowledgeTarget = new ArrayList<MedicationKnowledge>();
      return this.relatedMedicationKnowledgeTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public MedicationKnowledge addRelatedMedicationKnowledgeTarget() { 
      MedicationKnowledge r = new MedicationKnowledge();
      if (this.relatedMedicationKnowledgeTarget == null)
        this.relatedMedicationKnowledgeTarget = new ArrayList<MedicationKnowledge>();
      this.relatedMedicationKnowledgeTarget.add(r);
      return r;
    }

    /**
     * @return {@link #associatedMedication} (Associated or related medications.  For example, if the medication is a branded product (e.g. Crestor), this is the Therapeutic Moeity (e.g. Rosuvastatin) or if this is a generic medication (e.g. Rosuvastatin), this would link to a branded product (e.g. Crestor).)
     */
    public Reference getAssociatedMedication() { 
      if (this.associatedMedication == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationKnowledge.associatedMedication");
        else if (Configuration.doAutoCreate())
          this.associatedMedication = new Reference(); // cc
      return this.associatedMedication;
    }

    public boolean hasAssociatedMedication() { 
      return this.associatedMedication != null && !this.associatedMedication.isEmpty();
    }

    /**
     * @param value {@link #associatedMedication} (Associated or related medications.  For example, if the medication is a branded product (e.g. Crestor), this is the Therapeutic Moeity (e.g. Rosuvastatin) or if this is a generic medication (e.g. Rosuvastatin), this would link to a branded product (e.g. Crestor).)
     */
    public MedicationKnowledge setAssociatedMedication(Reference value) { 
      this.associatedMedication = value;
      return this;
    }

    /**
     * @return {@link #associatedMedication} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Associated or related medications.  For example, if the medication is a branded product (e.g. Crestor), this is the Therapeutic Moeity (e.g. Rosuvastatin) or if this is a generic medication (e.g. Rosuvastatin), this would link to a branded product (e.g. Crestor).)
     */
    public Medication getAssociatedMedicationTarget() { 
      if (this.associatedMedicationTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationKnowledge.associatedMedication");
        else if (Configuration.doAutoCreate())
          this.associatedMedicationTarget = new Medication(); // aa
      return this.associatedMedicationTarget;
    }

    /**
     * @param value {@link #associatedMedication} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Associated or related medications.  For example, if the medication is a branded product (e.g. Crestor), this is the Therapeutic Moeity (e.g. Rosuvastatin) or if this is a generic medication (e.g. Rosuvastatin), this would link to a branded product (e.g. Crestor).)
     */
    public MedicationKnowledge setAssociatedMedicationTarget(Medication value) { 
      this.associatedMedicationTarget = value;
      return this;
    }

    /**
     * @return {@link #productType} (Category of the medication or product (e.g. branded product, therapeutic moeity, generic product, innovator product, etc).)
     */
    public List<CodeableConcept> getProductType() { 
      if (this.productType == null)
        this.productType = new ArrayList<CodeableConcept>();
      return this.productType;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicationKnowledge setProductType(List<CodeableConcept> theProductType) { 
      this.productType = theProductType;
      return this;
    }

    public boolean hasProductType() { 
      if (this.productType == null)
        return false;
      for (CodeableConcept item : this.productType)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addProductType() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.productType == null)
        this.productType = new ArrayList<CodeableConcept>();
      this.productType.add(t);
      return t;
    }

    public MedicationKnowledge addProductType(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.productType == null)
        this.productType = new ArrayList<CodeableConcept>();
      this.productType.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #productType}, creating it if it does not already exist
     */
    public CodeableConcept getProductTypeFirstRep() { 
      if (getProductType().isEmpty()) {
        addProductType();
      }
      return getProductType().get(0);
    }

    /**
     * @return {@link #monograph} (Associated documentation about the medication.)
     */
    public List<MedicationKnowledgeMonographComponent> getMonograph() { 
      if (this.monograph == null)
        this.monograph = new ArrayList<MedicationKnowledgeMonographComponent>();
      return this.monograph;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicationKnowledge setMonograph(List<MedicationKnowledgeMonographComponent> theMonograph) { 
      this.monograph = theMonograph;
      return this;
    }

    public boolean hasMonograph() { 
      if (this.monograph == null)
        return false;
      for (MedicationKnowledgeMonographComponent item : this.monograph)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public MedicationKnowledgeMonographComponent addMonograph() { //3
      MedicationKnowledgeMonographComponent t = new MedicationKnowledgeMonographComponent();
      if (this.monograph == null)
        this.monograph = new ArrayList<MedicationKnowledgeMonographComponent>();
      this.monograph.add(t);
      return t;
    }

    public MedicationKnowledge addMonograph(MedicationKnowledgeMonographComponent t) { //3
      if (t == null)
        return this;
      if (this.monograph == null)
        this.monograph = new ArrayList<MedicationKnowledgeMonographComponent>();
      this.monograph.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #monograph}, creating it if it does not already exist
     */
    public MedicationKnowledgeMonographComponent getMonographFirstRep() { 
      if (getMonograph().isEmpty()) {
        addMonograph();
      }
      return getMonograph().get(0);
    }

    /**
     * @return {@link #halfLifePeriod} (The time required for any specified property (e.g., the concentration of a substance in the body) to decrease by half.)
     */
    public Duration getHalfLifePeriod() { 
      if (this.halfLifePeriod == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationKnowledge.halfLifePeriod");
        else if (Configuration.doAutoCreate())
          this.halfLifePeriod = new Duration(); // cc
      return this.halfLifePeriod;
    }

    public boolean hasHalfLifePeriod() { 
      return this.halfLifePeriod != null && !this.halfLifePeriod.isEmpty();
    }

    /**
     * @param value {@link #halfLifePeriod} (The time required for any specified property (e.g., the concentration of a substance in the body) to decrease by half.)
     */
    public MedicationKnowledge setHalfLifePeriod(Duration value) { 
      this.halfLifePeriod = value;
      return this;
    }

    /**
     * @return {@link #ingredient} (Identifies a particular constituent of interest in the product.)
     */
    public List<MedicationKnowledgeIngredientComponent> getIngredient() { 
      if (this.ingredient == null)
        this.ingredient = new ArrayList<MedicationKnowledgeIngredientComponent>();
      return this.ingredient;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicationKnowledge setIngredient(List<MedicationKnowledgeIngredientComponent> theIngredient) { 
      this.ingredient = theIngredient;
      return this;
    }

    public boolean hasIngredient() { 
      if (this.ingredient == null)
        return false;
      for (MedicationKnowledgeIngredientComponent item : this.ingredient)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public MedicationKnowledgeIngredientComponent addIngredient() { //3
      MedicationKnowledgeIngredientComponent t = new MedicationKnowledgeIngredientComponent();
      if (this.ingredient == null)
        this.ingredient = new ArrayList<MedicationKnowledgeIngredientComponent>();
      this.ingredient.add(t);
      return t;
    }

    public MedicationKnowledge addIngredient(MedicationKnowledgeIngredientComponent t) { //3
      if (t == null)
        return this;
      if (this.ingredient == null)
        this.ingredient = new ArrayList<MedicationKnowledgeIngredientComponent>();
      this.ingredient.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #ingredient}, creating it if it does not already exist
     */
    public MedicationKnowledgeIngredientComponent getIngredientFirstRep() { 
      if (getIngredient().isEmpty()) {
        addIngredient();
      }
      return getIngredient().get(0);
    }

    /**
     * @return {@link #preparationInstruction} (The instructions for preparing the medication.). This is the underlying object with id, value and extensions. The accessor "getPreparationInstruction" gives direct access to the value
     */
    public MarkdownType getPreparationInstructionElement() { 
      if (this.preparationInstruction == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationKnowledge.preparationInstruction");
        else if (Configuration.doAutoCreate())
          this.preparationInstruction = new MarkdownType(); // bb
      return this.preparationInstruction;
    }

    public boolean hasPreparationInstructionElement() { 
      return this.preparationInstruction != null && !this.preparationInstruction.isEmpty();
    }

    public boolean hasPreparationInstruction() { 
      return this.preparationInstruction != null && !this.preparationInstruction.isEmpty();
    }

    /**
     * @param value {@link #preparationInstruction} (The instructions for preparing the medication.). This is the underlying object with id, value and extensions. The accessor "getPreparationInstruction" gives direct access to the value
     */
    public MedicationKnowledge setPreparationInstructionElement(MarkdownType value) { 
      this.preparationInstruction = value;
      return this;
    }

    /**
     * @return The instructions for preparing the medication.
     */
    public String getPreparationInstruction() { 
      return this.preparationInstruction == null ? null : this.preparationInstruction.getValue();
    }

    /**
     * @param value The instructions for preparing the medication.
     */
    public MedicationKnowledge setPreparationInstruction(String value) { 
      if (value == null)
        this.preparationInstruction = null;
      else {
        if (this.preparationInstruction == null)
          this.preparationInstruction = new MarkdownType();
        this.preparationInstruction.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #intendedRoute} (The intended or approved route of administration.)
     */
    public List<CodeableConcept> getIntendedRoute() { 
      if (this.intendedRoute == null)
        this.intendedRoute = new ArrayList<CodeableConcept>();
      return this.intendedRoute;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicationKnowledge setIntendedRoute(List<CodeableConcept> theIntendedRoute) { 
      this.intendedRoute = theIntendedRoute;
      return this;
    }

    public boolean hasIntendedRoute() { 
      if (this.intendedRoute == null)
        return false;
      for (CodeableConcept item : this.intendedRoute)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addIntendedRoute() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.intendedRoute == null)
        this.intendedRoute = new ArrayList<CodeableConcept>();
      this.intendedRoute.add(t);
      return t;
    }

    public MedicationKnowledge addIntendedRoute(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.intendedRoute == null)
        this.intendedRoute = new ArrayList<CodeableConcept>();
      this.intendedRoute.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #intendedRoute}, creating it if it does not already exist
     */
    public CodeableConcept getIntendedRouteFirstRep() { 
      if (getIntendedRoute().isEmpty()) {
        addIntendedRoute();
      }
      return getIntendedRoute().get(0);
    }

    /**
     * @return {@link #cost} (The price of the medication.)
     */
    public List<MedicationKnowledgeCostComponent> getCost() { 
      if (this.cost == null)
        this.cost = new ArrayList<MedicationKnowledgeCostComponent>();
      return this.cost;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicationKnowledge setCost(List<MedicationKnowledgeCostComponent> theCost) { 
      this.cost = theCost;
      return this;
    }

    public boolean hasCost() { 
      if (this.cost == null)
        return false;
      for (MedicationKnowledgeCostComponent item : this.cost)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public MedicationKnowledgeCostComponent addCost() { //3
      MedicationKnowledgeCostComponent t = new MedicationKnowledgeCostComponent();
      if (this.cost == null)
        this.cost = new ArrayList<MedicationKnowledgeCostComponent>();
      this.cost.add(t);
      return t;
    }

    public MedicationKnowledge addCost(MedicationKnowledgeCostComponent t) { //3
      if (t == null)
        return this;
      if (this.cost == null)
        this.cost = new ArrayList<MedicationKnowledgeCostComponent>();
      this.cost.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #cost}, creating it if it does not already exist
     */
    public MedicationKnowledgeCostComponent getCostFirstRep() { 
      if (getCost().isEmpty()) {
        addCost();
      }
      return getCost().get(0);
    }

    /**
     * @return {@link #monitoringProgram} (The program under which the medication is reviewed.)
     */
    public List<MedicationKnowledgeMonitoringProgramComponent> getMonitoringProgram() { 
      if (this.monitoringProgram == null)
        this.monitoringProgram = new ArrayList<MedicationKnowledgeMonitoringProgramComponent>();
      return this.monitoringProgram;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicationKnowledge setMonitoringProgram(List<MedicationKnowledgeMonitoringProgramComponent> theMonitoringProgram) { 
      this.monitoringProgram = theMonitoringProgram;
      return this;
    }

    public boolean hasMonitoringProgram() { 
      if (this.monitoringProgram == null)
        return false;
      for (MedicationKnowledgeMonitoringProgramComponent item : this.monitoringProgram)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public MedicationKnowledgeMonitoringProgramComponent addMonitoringProgram() { //3
      MedicationKnowledgeMonitoringProgramComponent t = new MedicationKnowledgeMonitoringProgramComponent();
      if (this.monitoringProgram == null)
        this.monitoringProgram = new ArrayList<MedicationKnowledgeMonitoringProgramComponent>();
      this.monitoringProgram.add(t);
      return t;
    }

    public MedicationKnowledge addMonitoringProgram(MedicationKnowledgeMonitoringProgramComponent t) { //3
      if (t == null)
        return this;
      if (this.monitoringProgram == null)
        this.monitoringProgram = new ArrayList<MedicationKnowledgeMonitoringProgramComponent>();
      this.monitoringProgram.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #monitoringProgram}, creating it if it does not already exist
     */
    public MedicationKnowledgeMonitoringProgramComponent getMonitoringProgramFirstRep() { 
      if (getMonitoringProgram().isEmpty()) {
        addMonitoringProgram();
      }
      return getMonitoringProgram().get(0);
    }

    /**
     * @return {@link #administrationGuidelines} (Guidelines for the administration of the medication.)
     */
    public List<MedicationKnowledgeAdministrationGuidelinesComponent> getAdministrationGuidelines() { 
      if (this.administrationGuidelines == null)
        this.administrationGuidelines = new ArrayList<MedicationKnowledgeAdministrationGuidelinesComponent>();
      return this.administrationGuidelines;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicationKnowledge setAdministrationGuidelines(List<MedicationKnowledgeAdministrationGuidelinesComponent> theAdministrationGuidelines) { 
      this.administrationGuidelines = theAdministrationGuidelines;
      return this;
    }

    public boolean hasAdministrationGuidelines() { 
      if (this.administrationGuidelines == null)
        return false;
      for (MedicationKnowledgeAdministrationGuidelinesComponent item : this.administrationGuidelines)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public MedicationKnowledgeAdministrationGuidelinesComponent addAdministrationGuidelines() { //3
      MedicationKnowledgeAdministrationGuidelinesComponent t = new MedicationKnowledgeAdministrationGuidelinesComponent();
      if (this.administrationGuidelines == null)
        this.administrationGuidelines = new ArrayList<MedicationKnowledgeAdministrationGuidelinesComponent>();
      this.administrationGuidelines.add(t);
      return t;
    }

    public MedicationKnowledge addAdministrationGuidelines(MedicationKnowledgeAdministrationGuidelinesComponent t) { //3
      if (t == null)
        return this;
      if (this.administrationGuidelines == null)
        this.administrationGuidelines = new ArrayList<MedicationKnowledgeAdministrationGuidelinesComponent>();
      this.administrationGuidelines.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #administrationGuidelines}, creating it if it does not already exist
     */
    public MedicationKnowledgeAdministrationGuidelinesComponent getAdministrationGuidelinesFirstRep() { 
      if (getAdministrationGuidelines().isEmpty()) {
        addAdministrationGuidelines();
      }
      return getAdministrationGuidelines().get(0);
    }

    /**
     * @return {@link #medicineClassification} (Categorization of the medication within a formulary or classification system.)
     */
    public List<MedicationKnowledgeMedicineClassificationComponent> getMedicineClassification() { 
      if (this.medicineClassification == null)
        this.medicineClassification = new ArrayList<MedicationKnowledgeMedicineClassificationComponent>();
      return this.medicineClassification;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicationKnowledge setMedicineClassification(List<MedicationKnowledgeMedicineClassificationComponent> theMedicineClassification) { 
      this.medicineClassification = theMedicineClassification;
      return this;
    }

    public boolean hasMedicineClassification() { 
      if (this.medicineClassification == null)
        return false;
      for (MedicationKnowledgeMedicineClassificationComponent item : this.medicineClassification)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public MedicationKnowledgeMedicineClassificationComponent addMedicineClassification() { //3
      MedicationKnowledgeMedicineClassificationComponent t = new MedicationKnowledgeMedicineClassificationComponent();
      if (this.medicineClassification == null)
        this.medicineClassification = new ArrayList<MedicationKnowledgeMedicineClassificationComponent>();
      this.medicineClassification.add(t);
      return t;
    }

    public MedicationKnowledge addMedicineClassification(MedicationKnowledgeMedicineClassificationComponent t) { //3
      if (t == null)
        return this;
      if (this.medicineClassification == null)
        this.medicineClassification = new ArrayList<MedicationKnowledgeMedicineClassificationComponent>();
      this.medicineClassification.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #medicineClassification}, creating it if it does not already exist
     */
    public MedicationKnowledgeMedicineClassificationComponent getMedicineClassificationFirstRep() { 
      if (getMedicineClassification().isEmpty()) {
        addMedicineClassification();
      }
      return getMedicineClassification().get(0);
    }

    /**
     * @return {@link #packaging} (Information that only applies to packages (not products).)
     */
    public MedicationKnowledgePackagingComponent getPackaging() { 
      if (this.packaging == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationKnowledge.packaging");
        else if (Configuration.doAutoCreate())
          this.packaging = new MedicationKnowledgePackagingComponent(); // cc
      return this.packaging;
    }

    public boolean hasPackaging() { 
      return this.packaging != null && !this.packaging.isEmpty();
    }

    /**
     * @param value {@link #packaging} (Information that only applies to packages (not products).)
     */
    public MedicationKnowledge setPackaging(MedicationKnowledgePackagingComponent value) { 
      this.packaging = value;
      return this;
    }

    /**
     * @return {@link #drugCharacteristic} (Specifies descriptive properties of the medicine, such as color, shape, imprints, etc.)
     */
    public List<MedicationKnowledgeDrugCharacteristicComponent> getDrugCharacteristic() { 
      if (this.drugCharacteristic == null)
        this.drugCharacteristic = new ArrayList<MedicationKnowledgeDrugCharacteristicComponent>();
      return this.drugCharacteristic;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicationKnowledge setDrugCharacteristic(List<MedicationKnowledgeDrugCharacteristicComponent> theDrugCharacteristic) { 
      this.drugCharacteristic = theDrugCharacteristic;
      return this;
    }

    public boolean hasDrugCharacteristic() { 
      if (this.drugCharacteristic == null)
        return false;
      for (MedicationKnowledgeDrugCharacteristicComponent item : this.drugCharacteristic)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public MedicationKnowledgeDrugCharacteristicComponent addDrugCharacteristic() { //3
      MedicationKnowledgeDrugCharacteristicComponent t = new MedicationKnowledgeDrugCharacteristicComponent();
      if (this.drugCharacteristic == null)
        this.drugCharacteristic = new ArrayList<MedicationKnowledgeDrugCharacteristicComponent>();
      this.drugCharacteristic.add(t);
      return t;
    }

    public MedicationKnowledge addDrugCharacteristic(MedicationKnowledgeDrugCharacteristicComponent t) { //3
      if (t == null)
        return this;
      if (this.drugCharacteristic == null)
        this.drugCharacteristic = new ArrayList<MedicationKnowledgeDrugCharacteristicComponent>();
      this.drugCharacteristic.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #drugCharacteristic}, creating it if it does not already exist
     */
    public MedicationKnowledgeDrugCharacteristicComponent getDrugCharacteristicFirstRep() { 
      if (getDrugCharacteristic().isEmpty()) {
        addDrugCharacteristic();
      }
      return getDrugCharacteristic().get(0);
    }

    /**
     * @return {@link #contraindication} (Potential clinical issue with or between medication(s) (for example, drug-drug interaction, drug-disease contraindication, drug-allergy interaction, etc).)
     */
    public List<Reference> getContraindication() { 
      if (this.contraindication == null)
        this.contraindication = new ArrayList<Reference>();
      return this.contraindication;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicationKnowledge setContraindication(List<Reference> theContraindication) { 
      this.contraindication = theContraindication;
      return this;
    }

    public boolean hasContraindication() { 
      if (this.contraindication == null)
        return false;
      for (Reference item : this.contraindication)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addContraindication() { //3
      Reference t = new Reference();
      if (this.contraindication == null)
        this.contraindication = new ArrayList<Reference>();
      this.contraindication.add(t);
      return t;
    }

    public MedicationKnowledge addContraindication(Reference t) { //3
      if (t == null)
        return this;
      if (this.contraindication == null)
        this.contraindication = new ArrayList<Reference>();
      this.contraindication.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #contraindication}, creating it if it does not already exist
     */
    public Reference getContraindicationFirstRep() { 
      if (getContraindication().isEmpty()) {
        addContraindication();
      }
      return getContraindication().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<DetectedIssue> getContraindicationTarget() { 
      if (this.contraindicationTarget == null)
        this.contraindicationTarget = new ArrayList<DetectedIssue>();
      return this.contraindicationTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public DetectedIssue addContraindicationTarget() { 
      DetectedIssue r = new DetectedIssue();
      if (this.contraindicationTarget == null)
        this.contraindicationTarget = new ArrayList<DetectedIssue>();
      this.contraindicationTarget.add(r);
      return r;
    }

      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("code", "CodeableConcept", "A code that specifies this medication, or a textual description if no code is available. Usage note: This could be a standard medication code such as a code from RxNorm, SNOMED CT, IDMP etc. It could also be a national or local formulary code, optionally with translations to other code systems.", 0, 1, code));
        children.add(new Property("status", "code", "A code to indicate if the medication is in active use.", 0, 1, status));
        children.add(new Property("manufacturer", "Reference(Organization)", "Describes the details of the manufacturer of the medication product.  This is not intended to represent the distributor of a medication product.", 0, 1, manufacturer));
        children.add(new Property("form", "CodeableConcept", "Describes the form of the item.  Powder; tablets; capsule.", 0, 1, form));
        children.add(new Property("amount", "SimpleQuantity", "Specific amount of the drug in the packaged product.  For example, when specifying a product that has the same strength (For example, Insulin glargine 100 unit per mL solution for injection), this attribute provides additional clarification of the package amount (For example, 3 mL, 10mL, etc).", 0, 1, amount));
        children.add(new Property("synonym", "string", "Additional names for a medication, for example, the name(s) given to a medication in different countries.  For example, acetaminophen and paracetamol or salbutamol and albuterol.", 0, java.lang.Integer.MAX_VALUE, synonym));
        children.add(new Property("relatedMedicationKnowledge", "Reference(MedicationKnowledge)", "Associated or related knowledge about a medication.", 0, java.lang.Integer.MAX_VALUE, relatedMedicationKnowledge));
        children.add(new Property("associatedMedication", "Reference(Medication)", "Associated or related medications.  For example, if the medication is a branded product (e.g. Crestor), this is the Therapeutic Moeity (e.g. Rosuvastatin) or if this is a generic medication (e.g. Rosuvastatin), this would link to a branded product (e.g. Crestor).", 0, 1, associatedMedication));
        children.add(new Property("productType", "CodeableConcept", "Category of the medication or product (e.g. branded product, therapeutic moeity, generic product, innovator product, etc).", 0, java.lang.Integer.MAX_VALUE, productType));
        children.add(new Property("monograph", "", "Associated documentation about the medication.", 0, java.lang.Integer.MAX_VALUE, monograph));
        children.add(new Property("halfLifePeriod", "Duration", "The time required for any specified property (e.g., the concentration of a substance in the body) to decrease by half.", 0, 1, halfLifePeriod));
        children.add(new Property("ingredient", "", "Identifies a particular constituent of interest in the product.", 0, java.lang.Integer.MAX_VALUE, ingredient));
        children.add(new Property("preparationInstruction", "markdown", "The instructions for preparing the medication.", 0, 1, preparationInstruction));
        children.add(new Property("intendedRoute", "CodeableConcept", "The intended or approved route of administration.", 0, java.lang.Integer.MAX_VALUE, intendedRoute));
        children.add(new Property("cost", "", "The price of the medication.", 0, java.lang.Integer.MAX_VALUE, cost));
        children.add(new Property("monitoringProgram", "", "The program under which the medication is reviewed.", 0, java.lang.Integer.MAX_VALUE, monitoringProgram));
        children.add(new Property("administrationGuidelines", "", "Guidelines for the administration of the medication.", 0, java.lang.Integer.MAX_VALUE, administrationGuidelines));
        children.add(new Property("medicineClassification", "", "Categorization of the medication within a formulary or classification system.", 0, java.lang.Integer.MAX_VALUE, medicineClassification));
        children.add(new Property("packaging", "", "Information that only applies to packages (not products).", 0, 1, packaging));
        children.add(new Property("drugCharacteristic", "", "Specifies descriptive properties of the medicine, such as color, shape, imprints, etc.", 0, java.lang.Integer.MAX_VALUE, drugCharacteristic));
        children.add(new Property("contraindication", "Reference(DetectedIssue)", "Potential clinical issue with or between medication(s) (for example, drug-drug interaction, drug-disease contraindication, drug-allergy interaction, etc).", 0, java.lang.Integer.MAX_VALUE, contraindication));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case 3059181: /*code*/  return new Property("code", "CodeableConcept", "A code that specifies this medication, or a textual description if no code is available. Usage note: This could be a standard medication code such as a code from RxNorm, SNOMED CT, IDMP etc. It could also be a national or local formulary code, optionally with translations to other code systems.", 0, 1, code);
        case -892481550: /*status*/  return new Property("status", "code", "A code to indicate if the medication is in active use.", 0, 1, status);
        case -1969347631: /*manufacturer*/  return new Property("manufacturer", "Reference(Organization)", "Describes the details of the manufacturer of the medication product.  This is not intended to represent the distributor of a medication product.", 0, 1, manufacturer);
        case 3148996: /*form*/  return new Property("form", "CodeableConcept", "Describes the form of the item.  Powder; tablets; capsule.", 0, 1, form);
        case -1413853096: /*amount*/  return new Property("amount", "SimpleQuantity", "Specific amount of the drug in the packaged product.  For example, when specifying a product that has the same strength (For example, Insulin glargine 100 unit per mL solution for injection), this attribute provides additional clarification of the package amount (For example, 3 mL, 10mL, etc).", 0, 1, amount);
        case -1742128133: /*synonym*/  return new Property("synonym", "string", "Additional names for a medication, for example, the name(s) given to a medication in different countries.  For example, acetaminophen and paracetamol or salbutamol and albuterol.", 0, java.lang.Integer.MAX_VALUE, synonym);
        case 723067972: /*relatedMedicationKnowledge*/  return new Property("relatedMedicationKnowledge", "Reference(MedicationKnowledge)", "Associated or related knowledge about a medication.", 0, java.lang.Integer.MAX_VALUE, relatedMedicationKnowledge);
        case 1312779381: /*associatedMedication*/  return new Property("associatedMedication", "Reference(Medication)", "Associated or related medications.  For example, if the medication is a branded product (e.g. Crestor), this is the Therapeutic Moeity (e.g. Rosuvastatin) or if this is a generic medication (e.g. Rosuvastatin), this would link to a branded product (e.g. Crestor).", 0, 1, associatedMedication);
        case -1491615543: /*productType*/  return new Property("productType", "CodeableConcept", "Category of the medication or product (e.g. branded product, therapeutic moeity, generic product, innovator product, etc).", 0, java.lang.Integer.MAX_VALUE, productType);
        case -1442980789: /*monograph*/  return new Property("monograph", "", "Associated documentation about the medication.", 0, java.lang.Integer.MAX_VALUE, monograph);
        case -628810640: /*halfLifePeriod*/  return new Property("halfLifePeriod", "Duration", "The time required for any specified property (e.g., the concentration of a substance in the body) to decrease by half.", 0, 1, halfLifePeriod);
        case -206409263: /*ingredient*/  return new Property("ingredient", "", "Identifies a particular constituent of interest in the product.", 0, java.lang.Integer.MAX_VALUE, ingredient);
        case 1025456503: /*preparationInstruction*/  return new Property("preparationInstruction", "markdown", "The instructions for preparing the medication.", 0, 1, preparationInstruction);
        case -767798050: /*intendedRoute*/  return new Property("intendedRoute", "CodeableConcept", "The intended or approved route of administration.", 0, java.lang.Integer.MAX_VALUE, intendedRoute);
        case 3059661: /*cost*/  return new Property("cost", "", "The price of the medication.", 0, java.lang.Integer.MAX_VALUE, cost);
        case 569848092: /*monitoringProgram*/  return new Property("monitoringProgram", "", "The program under which the medication is reviewed.", 0, java.lang.Integer.MAX_VALUE, monitoringProgram);
        case 496930945: /*administrationGuidelines*/  return new Property("administrationGuidelines", "", "Guidelines for the administration of the medication.", 0, java.lang.Integer.MAX_VALUE, administrationGuidelines);
        case 1791551680: /*medicineClassification*/  return new Property("medicineClassification", "", "Categorization of the medication within a formulary or classification system.", 0, java.lang.Integer.MAX_VALUE, medicineClassification);
        case 1802065795: /*packaging*/  return new Property("packaging", "", "Information that only applies to packages (not products).", 0, 1, packaging);
        case -844126885: /*drugCharacteristic*/  return new Property("drugCharacteristic", "", "Specifies descriptive properties of the medicine, such as color, shape, imprints, etc.", 0, java.lang.Integer.MAX_VALUE, drugCharacteristic);
        case 107135229: /*contraindication*/  return new Property("contraindication", "Reference(DetectedIssue)", "Potential clinical issue with or between medication(s) (for example, drug-drug interaction, drug-disease contraindication, drug-allergy interaction, etc).", 0, java.lang.Integer.MAX_VALUE, contraindication);
        default: return super.getNamedProperty(_hash, _name, _checkValid);
        }

      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3059181: /*code*/ return this.code == null ? new Base[0] : new Base[] {this.code}; // CodeableConcept
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<MedicationKnowledgeStatus>
        case -1969347631: /*manufacturer*/ return this.manufacturer == null ? new Base[0] : new Base[] {this.manufacturer}; // Reference
        case 3148996: /*form*/ return this.form == null ? new Base[0] : new Base[] {this.form}; // CodeableConcept
        case -1413853096: /*amount*/ return this.amount == null ? new Base[0] : new Base[] {this.amount}; // SimpleQuantity
        case -1742128133: /*synonym*/ return this.synonym == null ? new Base[0] : this.synonym.toArray(new Base[this.synonym.size()]); // StringType
        case 723067972: /*relatedMedicationKnowledge*/ return this.relatedMedicationKnowledge == null ? new Base[0] : this.relatedMedicationKnowledge.toArray(new Base[this.relatedMedicationKnowledge.size()]); // Reference
        case 1312779381: /*associatedMedication*/ return this.associatedMedication == null ? new Base[0] : new Base[] {this.associatedMedication}; // Reference
        case -1491615543: /*productType*/ return this.productType == null ? new Base[0] : this.productType.toArray(new Base[this.productType.size()]); // CodeableConcept
        case -1442980789: /*monograph*/ return this.monograph == null ? new Base[0] : this.monograph.toArray(new Base[this.monograph.size()]); // MedicationKnowledgeMonographComponent
        case -628810640: /*halfLifePeriod*/ return this.halfLifePeriod == null ? new Base[0] : new Base[] {this.halfLifePeriod}; // Duration
        case -206409263: /*ingredient*/ return this.ingredient == null ? new Base[0] : this.ingredient.toArray(new Base[this.ingredient.size()]); // MedicationKnowledgeIngredientComponent
        case 1025456503: /*preparationInstruction*/ return this.preparationInstruction == null ? new Base[0] : new Base[] {this.preparationInstruction}; // MarkdownType
        case -767798050: /*intendedRoute*/ return this.intendedRoute == null ? new Base[0] : this.intendedRoute.toArray(new Base[this.intendedRoute.size()]); // CodeableConcept
        case 3059661: /*cost*/ return this.cost == null ? new Base[0] : this.cost.toArray(new Base[this.cost.size()]); // MedicationKnowledgeCostComponent
        case 569848092: /*monitoringProgram*/ return this.monitoringProgram == null ? new Base[0] : this.monitoringProgram.toArray(new Base[this.monitoringProgram.size()]); // MedicationKnowledgeMonitoringProgramComponent
        case 496930945: /*administrationGuidelines*/ return this.administrationGuidelines == null ? new Base[0] : this.administrationGuidelines.toArray(new Base[this.administrationGuidelines.size()]); // MedicationKnowledgeAdministrationGuidelinesComponent
        case 1791551680: /*medicineClassification*/ return this.medicineClassification == null ? new Base[0] : this.medicineClassification.toArray(new Base[this.medicineClassification.size()]); // MedicationKnowledgeMedicineClassificationComponent
        case 1802065795: /*packaging*/ return this.packaging == null ? new Base[0] : new Base[] {this.packaging}; // MedicationKnowledgePackagingComponent
        case -844126885: /*drugCharacteristic*/ return this.drugCharacteristic == null ? new Base[0] : this.drugCharacteristic.toArray(new Base[this.drugCharacteristic.size()]); // MedicationKnowledgeDrugCharacteristicComponent
        case 107135229: /*contraindication*/ return this.contraindication == null ? new Base[0] : this.contraindication.toArray(new Base[this.contraindication.size()]); // Reference
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
          value = new MedicationKnowledgeStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<MedicationKnowledgeStatus>
          return value;
        case -1969347631: // manufacturer
          this.manufacturer = castToReference(value); // Reference
          return value;
        case 3148996: // form
          this.form = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1413853096: // amount
          this.amount = castToSimpleQuantity(value); // SimpleQuantity
          return value;
        case -1742128133: // synonym
          this.getSynonym().add(castToString(value)); // StringType
          return value;
        case 723067972: // relatedMedicationKnowledge
          this.getRelatedMedicationKnowledge().add(castToReference(value)); // Reference
          return value;
        case 1312779381: // associatedMedication
          this.associatedMedication = castToReference(value); // Reference
          return value;
        case -1491615543: // productType
          this.getProductType().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -1442980789: // monograph
          this.getMonograph().add((MedicationKnowledgeMonographComponent) value); // MedicationKnowledgeMonographComponent
          return value;
        case -628810640: // halfLifePeriod
          this.halfLifePeriod = castToDuration(value); // Duration
          return value;
        case -206409263: // ingredient
          this.getIngredient().add((MedicationKnowledgeIngredientComponent) value); // MedicationKnowledgeIngredientComponent
          return value;
        case 1025456503: // preparationInstruction
          this.preparationInstruction = castToMarkdown(value); // MarkdownType
          return value;
        case -767798050: // intendedRoute
          this.getIntendedRoute().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 3059661: // cost
          this.getCost().add((MedicationKnowledgeCostComponent) value); // MedicationKnowledgeCostComponent
          return value;
        case 569848092: // monitoringProgram
          this.getMonitoringProgram().add((MedicationKnowledgeMonitoringProgramComponent) value); // MedicationKnowledgeMonitoringProgramComponent
          return value;
        case 496930945: // administrationGuidelines
          this.getAdministrationGuidelines().add((MedicationKnowledgeAdministrationGuidelinesComponent) value); // MedicationKnowledgeAdministrationGuidelinesComponent
          return value;
        case 1791551680: // medicineClassification
          this.getMedicineClassification().add((MedicationKnowledgeMedicineClassificationComponent) value); // MedicationKnowledgeMedicineClassificationComponent
          return value;
        case 1802065795: // packaging
          this.packaging = (MedicationKnowledgePackagingComponent) value; // MedicationKnowledgePackagingComponent
          return value;
        case -844126885: // drugCharacteristic
          this.getDrugCharacteristic().add((MedicationKnowledgeDrugCharacteristicComponent) value); // MedicationKnowledgeDrugCharacteristicComponent
          return value;
        case 107135229: // contraindication
          this.getContraindication().add(castToReference(value)); // Reference
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("code")) {
          this.code = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("status")) {
          value = new MedicationKnowledgeStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<MedicationKnowledgeStatus>
        } else if (name.equals("manufacturer")) {
          this.manufacturer = castToReference(value); // Reference
        } else if (name.equals("form")) {
          this.form = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("amount")) {
          this.amount = castToSimpleQuantity(value); // SimpleQuantity
        } else if (name.equals("synonym")) {
          this.getSynonym().add(castToString(value));
        } else if (name.equals("relatedMedicationKnowledge")) {
          this.getRelatedMedicationKnowledge().add(castToReference(value));
        } else if (name.equals("associatedMedication")) {
          this.associatedMedication = castToReference(value); // Reference
        } else if (name.equals("productType")) {
          this.getProductType().add(castToCodeableConcept(value));
        } else if (name.equals("monograph")) {
          this.getMonograph().add((MedicationKnowledgeMonographComponent) value);
        } else if (name.equals("halfLifePeriod")) {
          this.halfLifePeriod = castToDuration(value); // Duration
        } else if (name.equals("ingredient")) {
          this.getIngredient().add((MedicationKnowledgeIngredientComponent) value);
        } else if (name.equals("preparationInstruction")) {
          this.preparationInstruction = castToMarkdown(value); // MarkdownType
        } else if (name.equals("intendedRoute")) {
          this.getIntendedRoute().add(castToCodeableConcept(value));
        } else if (name.equals("cost")) {
          this.getCost().add((MedicationKnowledgeCostComponent) value);
        } else if (name.equals("monitoringProgram")) {
          this.getMonitoringProgram().add((MedicationKnowledgeMonitoringProgramComponent) value);
        } else if (name.equals("administrationGuidelines")) {
          this.getAdministrationGuidelines().add((MedicationKnowledgeAdministrationGuidelinesComponent) value);
        } else if (name.equals("medicineClassification")) {
          this.getMedicineClassification().add((MedicationKnowledgeMedicineClassificationComponent) value);
        } else if (name.equals("packaging")) {
          this.packaging = (MedicationKnowledgePackagingComponent) value; // MedicationKnowledgePackagingComponent
        } else if (name.equals("drugCharacteristic")) {
          this.getDrugCharacteristic().add((MedicationKnowledgeDrugCharacteristicComponent) value);
        } else if (name.equals("contraindication")) {
          this.getContraindication().add(castToReference(value));
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3059181:  return getCode(); 
        case -892481550:  return getStatusElement();
        case -1969347631:  return getManufacturer(); 
        case 3148996:  return getForm(); 
        case -1413853096:  return getAmount(); 
        case -1742128133:  return addSynonymElement();
        case 723067972:  return addRelatedMedicationKnowledge(); 
        case 1312779381:  return getAssociatedMedication(); 
        case -1491615543:  return addProductType(); 
        case -1442980789:  return addMonograph(); 
        case -628810640:  return getHalfLifePeriod(); 
        case -206409263:  return addIngredient(); 
        case 1025456503:  return getPreparationInstructionElement();
        case -767798050:  return addIntendedRoute(); 
        case 3059661:  return addCost(); 
        case 569848092:  return addMonitoringProgram(); 
        case 496930945:  return addAdministrationGuidelines(); 
        case 1791551680:  return addMedicineClassification(); 
        case 1802065795:  return getPackaging(); 
        case -844126885:  return addDrugCharacteristic(); 
        case 107135229:  return addContraindication(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3059181: /*code*/ return new String[] {"CodeableConcept"};
        case -892481550: /*status*/ return new String[] {"code"};
        case -1969347631: /*manufacturer*/ return new String[] {"Reference"};
        case 3148996: /*form*/ return new String[] {"CodeableConcept"};
        case -1413853096: /*amount*/ return new String[] {"SimpleQuantity"};
        case -1742128133: /*synonym*/ return new String[] {"string"};
        case 723067972: /*relatedMedicationKnowledge*/ return new String[] {"Reference"};
        case 1312779381: /*associatedMedication*/ return new String[] {"Reference"};
        case -1491615543: /*productType*/ return new String[] {"CodeableConcept"};
        case -1442980789: /*monograph*/ return new String[] {};
        case -628810640: /*halfLifePeriod*/ return new String[] {"Duration"};
        case -206409263: /*ingredient*/ return new String[] {};
        case 1025456503: /*preparationInstruction*/ return new String[] {"markdown"};
        case -767798050: /*intendedRoute*/ return new String[] {"CodeableConcept"};
        case 3059661: /*cost*/ return new String[] {};
        case 569848092: /*monitoringProgram*/ return new String[] {};
        case 496930945: /*administrationGuidelines*/ return new String[] {};
        case 1791551680: /*medicineClassification*/ return new String[] {};
        case 1802065795: /*packaging*/ return new String[] {};
        case -844126885: /*drugCharacteristic*/ return new String[] {};
        case 107135229: /*contraindication*/ return new String[] {"Reference"};
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
          throw new FHIRException("Cannot call addChild on a primitive type MedicationKnowledge.status");
        }
        else if (name.equals("manufacturer")) {
          this.manufacturer = new Reference();
          return this.manufacturer;
        }
        else if (name.equals("form")) {
          this.form = new CodeableConcept();
          return this.form;
        }
        else if (name.equals("amount")) {
          this.amount = new SimpleQuantity();
          return this.amount;
        }
        else if (name.equals("synonym")) {
          throw new FHIRException("Cannot call addChild on a primitive type MedicationKnowledge.synonym");
        }
        else if (name.equals("relatedMedicationKnowledge")) {
          return addRelatedMedicationKnowledge();
        }
        else if (name.equals("associatedMedication")) {
          this.associatedMedication = new Reference();
          return this.associatedMedication;
        }
        else if (name.equals("productType")) {
          return addProductType();
        }
        else if (name.equals("monograph")) {
          return addMonograph();
        }
        else if (name.equals("halfLifePeriod")) {
          this.halfLifePeriod = new Duration();
          return this.halfLifePeriod;
        }
        else if (name.equals("ingredient")) {
          return addIngredient();
        }
        else if (name.equals("preparationInstruction")) {
          throw new FHIRException("Cannot call addChild on a primitive type MedicationKnowledge.preparationInstruction");
        }
        else if (name.equals("intendedRoute")) {
          return addIntendedRoute();
        }
        else if (name.equals("cost")) {
          return addCost();
        }
        else if (name.equals("monitoringProgram")) {
          return addMonitoringProgram();
        }
        else if (name.equals("administrationGuidelines")) {
          return addAdministrationGuidelines();
        }
        else if (name.equals("medicineClassification")) {
          return addMedicineClassification();
        }
        else if (name.equals("packaging")) {
          this.packaging = new MedicationKnowledgePackagingComponent();
          return this.packaging;
        }
        else if (name.equals("drugCharacteristic")) {
          return addDrugCharacteristic();
        }
        else if (name.equals("contraindication")) {
          return addContraindication();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "MedicationKnowledge";

  }

      public MedicationKnowledge copy() {
        MedicationKnowledge dst = new MedicationKnowledge();
        copyValues(dst);
        dst.code = code == null ? null : code.copy();
        dst.status = status == null ? null : status.copy();
        dst.manufacturer = manufacturer == null ? null : manufacturer.copy();
        dst.form = form == null ? null : form.copy();
        dst.amount = amount == null ? null : amount.copy();
        if (synonym != null) {
          dst.synonym = new ArrayList<StringType>();
          for (StringType i : synonym)
            dst.synonym.add(i.copy());
        };
        if (relatedMedicationKnowledge != null) {
          dst.relatedMedicationKnowledge = new ArrayList<Reference>();
          for (Reference i : relatedMedicationKnowledge)
            dst.relatedMedicationKnowledge.add(i.copy());
        };
        dst.associatedMedication = associatedMedication == null ? null : associatedMedication.copy();
        if (productType != null) {
          dst.productType = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : productType)
            dst.productType.add(i.copy());
        };
        if (monograph != null) {
          dst.monograph = new ArrayList<MedicationKnowledgeMonographComponent>();
          for (MedicationKnowledgeMonographComponent i : monograph)
            dst.monograph.add(i.copy());
        };
        dst.halfLifePeriod = halfLifePeriod == null ? null : halfLifePeriod.copy();
        if (ingredient != null) {
          dst.ingredient = new ArrayList<MedicationKnowledgeIngredientComponent>();
          for (MedicationKnowledgeIngredientComponent i : ingredient)
            dst.ingredient.add(i.copy());
        };
        dst.preparationInstruction = preparationInstruction == null ? null : preparationInstruction.copy();
        if (intendedRoute != null) {
          dst.intendedRoute = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : intendedRoute)
            dst.intendedRoute.add(i.copy());
        };
        if (cost != null) {
          dst.cost = new ArrayList<MedicationKnowledgeCostComponent>();
          for (MedicationKnowledgeCostComponent i : cost)
            dst.cost.add(i.copy());
        };
        if (monitoringProgram != null) {
          dst.monitoringProgram = new ArrayList<MedicationKnowledgeMonitoringProgramComponent>();
          for (MedicationKnowledgeMonitoringProgramComponent i : monitoringProgram)
            dst.monitoringProgram.add(i.copy());
        };
        if (administrationGuidelines != null) {
          dst.administrationGuidelines = new ArrayList<MedicationKnowledgeAdministrationGuidelinesComponent>();
          for (MedicationKnowledgeAdministrationGuidelinesComponent i : administrationGuidelines)
            dst.administrationGuidelines.add(i.copy());
        };
        if (medicineClassification != null) {
          dst.medicineClassification = new ArrayList<MedicationKnowledgeMedicineClassificationComponent>();
          for (MedicationKnowledgeMedicineClassificationComponent i : medicineClassification)
            dst.medicineClassification.add(i.copy());
        };
        dst.packaging = packaging == null ? null : packaging.copy();
        if (drugCharacteristic != null) {
          dst.drugCharacteristic = new ArrayList<MedicationKnowledgeDrugCharacteristicComponent>();
          for (MedicationKnowledgeDrugCharacteristicComponent i : drugCharacteristic)
            dst.drugCharacteristic.add(i.copy());
        };
        if (contraindication != null) {
          dst.contraindication = new ArrayList<Reference>();
          for (Reference i : contraindication)
            dst.contraindication.add(i.copy());
        };
        return dst;
      }

      protected MedicationKnowledge typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof MedicationKnowledge))
          return false;
        MedicationKnowledge o = (MedicationKnowledge) other_;
        return compareDeep(code, o.code, true) && compareDeep(status, o.status, true) && compareDeep(manufacturer, o.manufacturer, true)
           && compareDeep(form, o.form, true) && compareDeep(amount, o.amount, true) && compareDeep(synonym, o.synonym, true)
           && compareDeep(relatedMedicationKnowledge, o.relatedMedicationKnowledge, true) && compareDeep(associatedMedication, o.associatedMedication, true)
           && compareDeep(productType, o.productType, true) && compareDeep(monograph, o.monograph, true) && compareDeep(halfLifePeriod, o.halfLifePeriod, true)
           && compareDeep(ingredient, o.ingredient, true) && compareDeep(preparationInstruction, o.preparationInstruction, true)
           && compareDeep(intendedRoute, o.intendedRoute, true) && compareDeep(cost, o.cost, true) && compareDeep(monitoringProgram, o.monitoringProgram, true)
           && compareDeep(administrationGuidelines, o.administrationGuidelines, true) && compareDeep(medicineClassification, o.medicineClassification, true)
           && compareDeep(packaging, o.packaging, true) && compareDeep(drugCharacteristic, o.drugCharacteristic, true)
           && compareDeep(contraindication, o.contraindication, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof MedicationKnowledge))
          return false;
        MedicationKnowledge o = (MedicationKnowledge) other_;
        return compareValues(status, o.status, true) && compareValues(synonym, o.synonym, true) && compareValues(preparationInstruction, o.preparationInstruction, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(code, status, manufacturer
          , form, amount, synonym, relatedMedicationKnowledge, associatedMedication, productType
          , monograph, halfLifePeriod, ingredient, preparationInstruction, intendedRoute, cost
          , monitoringProgram, administrationGuidelines, medicineClassification, packaging, drugCharacteristic
          , contraindication);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.MedicationKnowledge;
   }

 /**
   * Search parameter: <b>code</b>
   * <p>
   * Description: <b>Code that identifies this medication</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MedicationKnowledge.code</b><br>
   * </p>
   */
  @SearchParamDefinition(name="code", path="MedicationKnowledge.code", description="Code that identifies this medication", type="token" )
  public static final String SP_CODE = "code";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>code</b>
   * <p>
   * Description: <b>Code that identifies this medication</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MedicationKnowledge.code</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CODE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CODE);

 /**
   * Search parameter: <b>ingredient</b>
   * <p>
   * Description: <b>Medication(s) or substance(s) contained in the medication</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MedicationKnowledge.ingredient.itemReference</b><br>
   * </p>
   */
  @SearchParamDefinition(name="ingredient", path="MedicationKnowledge.ingredient.item.as(Reference)", description="Medication(s) or substance(s) contained in the medication", type="reference", target={Substance.class } )
  public static final String SP_INGREDIENT = "ingredient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>ingredient</b>
   * <p>
   * Description: <b>Medication(s) or substance(s) contained in the medication</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MedicationKnowledge.ingredient.itemReference</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam INGREDIENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_INGREDIENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>MedicationKnowledge:ingredient</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_INGREDIENT = new ca.uhn.fhir.model.api.Include("MedicationKnowledge:ingredient").toLocked();

 /**
   * Search parameter: <b>classification-type</b>
   * <p>
   * Description: <b>The type of category for the medication (for example, therapeutic classification, therapeutic sub-classification)</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MedicationKnowledge.medicineClassification.type</b><br>
   * </p>
   */
  @SearchParamDefinition(name="classification-type", path="MedicationKnowledge.medicineClassification.type", description="The type of category for the medication (for example, therapeutic classification, therapeutic sub-classification)", type="token" )
  public static final String SP_CLASSIFICATION_TYPE = "classification-type";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>classification-type</b>
   * <p>
   * Description: <b>The type of category for the medication (for example, therapeutic classification, therapeutic sub-classification)</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MedicationKnowledge.medicineClassification.type</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CLASSIFICATION_TYPE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CLASSIFICATION_TYPE);

 /**
   * Search parameter: <b>monograph-type</b>
   * <p>
   * Description: <b>The category of medication document</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MedicationKnowledge.monograph.type</b><br>
   * </p>
   */
  @SearchParamDefinition(name="monograph-type", path="MedicationKnowledge.monograph.type", description="The category of medication document", type="token" )
  public static final String SP_MONOGRAPH_TYPE = "monograph-type";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>monograph-type</b>
   * <p>
   * Description: <b>The category of medication document</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MedicationKnowledge.monograph.type</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam MONOGRAPH_TYPE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_MONOGRAPH_TYPE);

 /**
   * Search parameter: <b>classification</b>
   * <p>
   * Description: <b>Specific category assigned to the medication</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MedicationKnowledge.medicineClassification.classification</b><br>
   * </p>
   */
  @SearchParamDefinition(name="classification", path="MedicationKnowledge.medicineClassification.classification", description="Specific category assigned to the medication", type="token" )
  public static final String SP_CLASSIFICATION = "classification";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>classification</b>
   * <p>
   * Description: <b>Specific category assigned to the medication</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MedicationKnowledge.medicineClassification.classification</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CLASSIFICATION = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CLASSIFICATION);

 /**
   * Search parameter: <b>manufacturer</b>
   * <p>
   * Description: <b>Manufacturer of the item</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MedicationKnowledge.manufacturer</b><br>
   * </p>
   */
  @SearchParamDefinition(name="manufacturer", path="MedicationKnowledge.manufacturer", description="Manufacturer of the item", type="reference", target={Organization.class } )
  public static final String SP_MANUFACTURER = "manufacturer";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>manufacturer</b>
   * <p>
   * Description: <b>Manufacturer of the item</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MedicationKnowledge.manufacturer</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam MANUFACTURER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_MANUFACTURER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>MedicationKnowledge:manufacturer</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_MANUFACTURER = new ca.uhn.fhir.model.api.Include("MedicationKnowledge:manufacturer").toLocked();

 /**
   * Search parameter: <b>ingredient-code</b>
   * <p>
   * Description: <b>Medication(s) or substance(s) contained in the medication</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MedicationKnowledge.ingredient.itemCodeableConcept</b><br>
   * </p>
   */
  @SearchParamDefinition(name="ingredient-code", path="MedicationKnowledge.ingredient.item.as(CodeableConcept)", description="Medication(s) or substance(s) contained in the medication", type="token" )
  public static final String SP_INGREDIENT_CODE = "ingredient-code";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>ingredient-code</b>
   * <p>
   * Description: <b>Medication(s) or substance(s) contained in the medication</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MedicationKnowledge.ingredient.itemCodeableConcept</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam INGREDIENT_CODE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_INGREDIENT_CODE);

 /**
   * Search parameter: <b>source-cost</b>
   * <p>
   * Description: <b>The source or owner for the price information</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MedicationKnowledge.cost.source</b><br>
   * </p>
   */
  @SearchParamDefinition(name="source-cost", path="MedicationKnowledge.cost.source", description="The source or owner for the price information", type="token" )
  public static final String SP_SOURCE_COST = "source-cost";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>source-cost</b>
   * <p>
   * Description: <b>The source or owner for the price information</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MedicationKnowledge.cost.source</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam SOURCE_COST = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_SOURCE_COST);

 /**
   * Search parameter: <b>form</b>
   * <p>
   * Description: <b>powder | tablets | capsule +</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MedicationKnowledge.form</b><br>
   * </p>
   */
  @SearchParamDefinition(name="form", path="MedicationKnowledge.form", description="powder | tablets | capsule +", type="token" )
  public static final String SP_FORM = "form";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>form</b>
   * <p>
   * Description: <b>powder | tablets | capsule +</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MedicationKnowledge.form</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam FORM = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_FORM);

 /**
   * Search parameter: <b>monograph</b>
   * <p>
   * Description: <b>Associated documentation about the medication</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MedicationKnowledge.monograph.document</b><br>
   * </p>
   */
  @SearchParamDefinition(name="monograph", path="MedicationKnowledge.monograph.document", description="Associated documentation about the medication", type="reference", target={DocumentReference.class } )
  public static final String SP_MONOGRAPH = "monograph";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>monograph</b>
   * <p>
   * Description: <b>Associated documentation about the medication</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MedicationKnowledge.monograph.document</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam MONOGRAPH = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_MONOGRAPH);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>MedicationKnowledge:monograph</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_MONOGRAPH = new ca.uhn.fhir.model.api.Include("MedicationKnowledge:monograph").toLocked();

 /**
   * Search parameter: <b>monitoring-program-name</b>
   * <p>
   * Description: <b>Name of the reviewing program</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MedicationKnowledge.monitoringProgram.name</b><br>
   * </p>
   */
  @SearchParamDefinition(name="monitoring-program-name", path="MedicationKnowledge.monitoringProgram.name", description="Name of the reviewing program", type="token" )
  public static final String SP_MONITORING_PROGRAM_NAME = "monitoring-program-name";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>monitoring-program-name</b>
   * <p>
   * Description: <b>Name of the reviewing program</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MedicationKnowledge.monitoringProgram.name</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam MONITORING_PROGRAM_NAME = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_MONITORING_PROGRAM_NAME);

 /**
   * Search parameter: <b>monitoring-program-type</b>
   * <p>
   * Description: <b>Type of program under which the medication is monitored</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MedicationKnowledge.monitoringProgram.type</b><br>
   * </p>
   */
  @SearchParamDefinition(name="monitoring-program-type", path="MedicationKnowledge.monitoringProgram.type", description="Type of program under which the medication is monitored", type="token" )
  public static final String SP_MONITORING_PROGRAM_TYPE = "monitoring-program-type";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>monitoring-program-type</b>
   * <p>
   * Description: <b>Type of program under which the medication is monitored</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MedicationKnowledge.monitoringProgram.type</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam MONITORING_PROGRAM_TYPE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_MONITORING_PROGRAM_TYPE);

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>active | inactive | entered-in-error</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MedicationKnowledge.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="MedicationKnowledge.status", description="active | inactive | entered-in-error", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>active | inactive | entered-in-error</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MedicationKnowledge.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);


}

