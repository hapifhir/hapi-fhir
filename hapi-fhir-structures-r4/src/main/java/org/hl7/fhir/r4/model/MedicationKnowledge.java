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
 * Information about a medication that is used to support knowledge.
 */
@ResourceDef(name="MedicationKnowledge", profile="http://hl7.org/fhir/Profile/MedicationKnowledge")
public class MedicationKnowledge extends DomainResource {

    public enum MedicationKnowledgeStatus {
        /**
         * The medication is available for use.
         */
        ACTIVE, 
        /**
         * The medication is not available for use.
         */
        INACTIVE, 
        /**
         * The medication was entered in error.
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
            case ACTIVE: return "The medication is available for use.";
            case INACTIVE: return "The medication is not available for use.";
            case ENTEREDINERROR: return "The medication was entered in error.";
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
    public static class MedicationKnowledgeRelatedMedicationKnowledgeComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The category of the associated medication knowledge reference.
         */
        @Child(name = "type", type = {CodeableConcept.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Category of medicationKnowledge", formalDefinition="The category of the associated medication knowledge reference." )
        protected CodeableConcept type;

        /**
         * Associated documentation about the associated medication knowledge.
         */
        @Child(name = "reference", type = {MedicationKnowledge.class}, order=2, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Associated documentation about the associated medication knowledge", formalDefinition="Associated documentation about the associated medication knowledge." )
        protected List<Reference> reference;
        /**
         * The actual objects that are the target of the reference (Associated documentation about the associated medication knowledge.)
         */
        protected List<MedicationKnowledge> referenceTarget;


        private static final long serialVersionUID = 1285880636L;

    /**
     * Constructor
     */
      public MedicationKnowledgeRelatedMedicationKnowledgeComponent() {
        super();
      }

    /**
     * Constructor
     */
      public MedicationKnowledgeRelatedMedicationKnowledgeComponent(CodeableConcept type) {
        super();
        this.type = type;
      }

        /**
         * @return {@link #type} (The category of the associated medication knowledge reference.)
         */
        public CodeableConcept getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationKnowledgeRelatedMedicationKnowledgeComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new CodeableConcept(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (The category of the associated medication knowledge reference.)
         */
        public MedicationKnowledgeRelatedMedicationKnowledgeComponent setType(CodeableConcept value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #reference} (Associated documentation about the associated medication knowledge.)
         */
        public List<Reference> getReference() { 
          if (this.reference == null)
            this.reference = new ArrayList<Reference>();
          return this.reference;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public MedicationKnowledgeRelatedMedicationKnowledgeComponent setReference(List<Reference> theReference) { 
          this.reference = theReference;
          return this;
        }

        public boolean hasReference() { 
          if (this.reference == null)
            return false;
          for (Reference item : this.reference)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public Reference addReference() { //3
          Reference t = new Reference();
          if (this.reference == null)
            this.reference = new ArrayList<Reference>();
          this.reference.add(t);
          return t;
        }

        public MedicationKnowledgeRelatedMedicationKnowledgeComponent addReference(Reference t) { //3
          if (t == null)
            return this;
          if (this.reference == null)
            this.reference = new ArrayList<Reference>();
          this.reference.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #reference}, creating it if it does not already exist
         */
        public Reference getReferenceFirstRep() { 
          if (getReference().isEmpty()) {
            addReference();
          }
          return getReference().get(0);
        }

        /**
         * @deprecated Use Reference#setResource(IBaseResource) instead
         */
        @Deprecated
        public List<MedicationKnowledge> getReferenceTarget() { 
          if (this.referenceTarget == null)
            this.referenceTarget = new ArrayList<MedicationKnowledge>();
          return this.referenceTarget;
        }

        /**
         * @deprecated Use Reference#setResource(IBaseResource) instead
         */
        @Deprecated
        public MedicationKnowledge addReferenceTarget() { 
          MedicationKnowledge r = new MedicationKnowledge();
          if (this.referenceTarget == null)
            this.referenceTarget = new ArrayList<MedicationKnowledge>();
          this.referenceTarget.add(r);
          return r;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("type", "CodeableConcept", "The category of the associated medication knowledge reference.", 0, 1, type));
          children.add(new Property("reference", "Reference(MedicationKnowledge)", "Associated documentation about the associated medication knowledge.", 0, java.lang.Integer.MAX_VALUE, reference));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3575610: /*type*/  return new Property("type", "CodeableConcept", "The category of the associated medication knowledge reference.", 0, 1, type);
          case -925155509: /*reference*/  return new Property("reference", "Reference(MedicationKnowledge)", "Associated documentation about the associated medication knowledge.", 0, java.lang.Integer.MAX_VALUE, reference);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case -925155509: /*reference*/ return this.reference == null ? new Base[0] : this.reference.toArray(new Base[this.reference.size()]); // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -925155509: // reference
          this.getReference().add(castToReference(value)); // Reference
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type")) {
          this.type = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("reference")) {
          this.getReference().add(castToReference(value));
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610:  return getType(); 
        case -925155509:  return addReference(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case -925155509: /*reference*/ return new String[] {"Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("reference")) {
          return addReference();
        }
        else
          return super.addChild(name);
      }

      public MedicationKnowledgeRelatedMedicationKnowledgeComponent copy() {
        MedicationKnowledgeRelatedMedicationKnowledgeComponent dst = new MedicationKnowledgeRelatedMedicationKnowledgeComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        if (reference != null) {
          dst.reference = new ArrayList<Reference>();
          for (Reference i : reference)
            dst.reference.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof MedicationKnowledgeRelatedMedicationKnowledgeComponent))
          return false;
        MedicationKnowledgeRelatedMedicationKnowledgeComponent o = (MedicationKnowledgeRelatedMedicationKnowledgeComponent) other_;
        return compareDeep(type, o.type, true) && compareDeep(reference, o.reference, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof MedicationKnowledgeRelatedMedicationKnowledgeComponent))
          return false;
        MedicationKnowledgeRelatedMedicationKnowledgeComponent o = (MedicationKnowledgeRelatedMedicationKnowledgeComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(type, reference);
      }

  public String fhirType() {
    return "MedicationKnowledge.relatedMedicationKnowledge";

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
        @Child(name = "source", type = {DocumentReference.class, Media.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Associated documentation about the medication", formalDefinition="Associated documentation about the medication." )
        protected Reference source;

        /**
         * The actual object that is the target of the reference (Associated documentation about the medication.)
         */
        protected Resource sourceTarget;

        private static final long serialVersionUID = 1392095381L;

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
         * @return {@link #source} (Associated documentation about the medication.)
         */
        public Reference getSource() { 
          if (this.source == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationKnowledgeMonographComponent.source");
            else if (Configuration.doAutoCreate())
              this.source = new Reference(); // cc
          return this.source;
        }

        public boolean hasSource() { 
          return this.source != null && !this.source.isEmpty();
        }

        /**
         * @param value {@link #source} (Associated documentation about the medication.)
         */
        public MedicationKnowledgeMonographComponent setSource(Reference value) { 
          this.source = value;
          return this;
        }

        /**
         * @return {@link #source} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Associated documentation about the medication.)
         */
        public Resource getSourceTarget() { 
          return this.sourceTarget;
        }

        /**
         * @param value {@link #source} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Associated documentation about the medication.)
         */
        public MedicationKnowledgeMonographComponent setSourceTarget(Resource value) { 
          this.sourceTarget = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("type", "CodeableConcept", "The category of documentation about the medication. (e.g. professional monograph, patient educaton monograph).", 0, 1, type));
          children.add(new Property("source", "Reference(DocumentReference|Media)", "Associated documentation about the medication.", 0, 1, source));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3575610: /*type*/  return new Property("type", "CodeableConcept", "The category of documentation about the medication. (e.g. professional monograph, patient educaton monograph).", 0, 1, type);
          case -896505829: /*source*/  return new Property("source", "Reference(DocumentReference|Media)", "Associated documentation about the medication.", 0, 1, source);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case -896505829: /*source*/ return this.source == null ? new Base[0] : new Base[] {this.source}; // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -896505829: // source
          this.source = castToReference(value); // Reference
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type")) {
          this.type = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("source")) {
          this.source = castToReference(value); // Reference
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610:  return getType(); 
        case -896505829:  return getSource(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case -896505829: /*source*/ return new String[] {"Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("source")) {
          this.source = new Reference();
          return this.source;
        }
        else
          return super.addChild(name);
      }

      public MedicationKnowledgeMonographComponent copy() {
        MedicationKnowledgeMonographComponent dst = new MedicationKnowledgeMonographComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.source = source == null ? null : source.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof MedicationKnowledgeMonographComponent))
          return false;
        MedicationKnowledgeMonographComponent o = (MedicationKnowledgeMonographComponent) other_;
        return compareDeep(type, o.type, true) && compareDeep(source, o.source, true);
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
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(type, source);
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
         * Indication of whether this ingredient affects the therapeutic action of the drug.
         */
        @Child(name = "isActive", type = {BooleanType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Active ingredient indicator", formalDefinition="Indication of whether this ingredient affects the therapeutic action of the drug." )
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
         * @return {@link #isActive} (Indication of whether this ingredient affects the therapeutic action of the drug.). This is the underlying object with id, value and extensions. The accessor "getIsActive" gives direct access to the value
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
         * @param value {@link #isActive} (Indication of whether this ingredient affects the therapeutic action of the drug.). This is the underlying object with id, value and extensions. The accessor "getIsActive" gives direct access to the value
         */
        public MedicationKnowledgeIngredientComponent setIsActiveElement(BooleanType value) { 
          this.isActive = value;
          return this;
        }

        /**
         * @return Indication of whether this ingredient affects the therapeutic action of the drug.
         */
        public boolean getIsActive() { 
          return this.isActive == null || this.isActive.isEmpty() ? false : this.isActive.getValue();
        }

        /**
         * @param value Indication of whether this ingredient affects the therapeutic action of the drug.
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
          children.add(new Property("isActive", "boolean", "Indication of whether this ingredient affects the therapeutic action of the drug.", 0, 1, isActive));
          children.add(new Property("strength", "Ratio", "Specifies how many (or how much) of the items there are in this Medication.  For example, 250 mg per tablet.  This is expressed as a ratio where the numerator is 250mg and the denominator is 1 tablet.", 0, 1, strength));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 2116201613: /*item[x]*/  return new Property("item[x]", "CodeableConcept|Reference(Substance)", "The actual ingredient - either a substance (simple ingredient) or another medication.", 0, 1, item);
          case 3242771: /*item*/  return new Property("item[x]", "CodeableConcept|Reference(Substance)", "The actual ingredient - either a substance (simple ingredient) or another medication.", 0, 1, item);
          case 106644494: /*itemCodeableConcept*/  return new Property("item[x]", "CodeableConcept|Reference(Substance)", "The actual ingredient - either a substance (simple ingredient) or another medication.", 0, 1, item);
          case 1376364920: /*itemReference*/  return new Property("item[x]", "CodeableConcept|Reference(Substance)", "The actual ingredient - either a substance (simple ingredient) or another medication.", 0, 1, item);
          case -748916528: /*isActive*/  return new Property("isActive", "boolean", "Indication of whether this ingredient affects the therapeutic action of the drug.", 0, 1, isActive);
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
        @Child(name = "type", type = {CodeableConcept.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The category of the cost information", formalDefinition="The category of the cost information.  For example, manufacturers' cost, patient cost, claim reimbursement cost, actual acquisition cost." )
        protected CodeableConcept type;

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

        private static final long serialVersionUID = 244671378L;

    /**
     * Constructor
     */
      public MedicationKnowledgeCostComponent() {
        super();
      }

    /**
     * Constructor
     */
      public MedicationKnowledgeCostComponent(CodeableConcept type, Money cost) {
        super();
        this.type = type;
        this.cost = cost;
      }

        /**
         * @return {@link #type} (The category of the cost information.  For example, manufacturers' cost, patient cost, claim reimbursement cost, actual acquisition cost.)
         */
        public CodeableConcept getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationKnowledgeCostComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new CodeableConcept(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (The category of the cost information.  For example, manufacturers' cost, patient cost, claim reimbursement cost, actual acquisition cost.)
         */
        public MedicationKnowledgeCostComponent setType(CodeableConcept value) { 
          this.type = value;
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
          children.add(new Property("type", "CodeableConcept", "The category of the cost information.  For example, manufacturers' cost, patient cost, claim reimbursement cost, actual acquisition cost.", 0, 1, type));
          children.add(new Property("source", "string", "The source or owner that assigns the price to the medication.", 0, 1, source));
          children.add(new Property("cost", "Money", "The price of the medication.", 0, 1, cost));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3575610: /*type*/  return new Property("type", "CodeableConcept", "The category of the cost information.  For example, manufacturers' cost, patient cost, claim reimbursement cost, actual acquisition cost.", 0, 1, type);
          case -896505829: /*source*/  return new Property("source", "string", "The source or owner that assigns the price to the medication.", 0, 1, source);
          case 3059661: /*cost*/  return new Property("cost", "Money", "The price of the medication.", 0, 1, cost);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case -896505829: /*source*/ return this.source == null ? new Base[0] : new Base[] {this.source}; // StringType
        case 3059661: /*cost*/ return this.cost == null ? new Base[0] : new Base[] {this.cost}; // Money
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
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
          this.type = castToCodeableConcept(value); // CodeableConcept
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
        case 3575610:  return getType(); 
        case -896505829:  return getSourceElement();
        case 3059661:  return getCost(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case -896505829: /*source*/ return new String[] {"string"};
        case 3059661: /*cost*/ return new String[] {"Money"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
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
        return compareValues(source, o.source, true);
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
        @Child(name = "dosage", type = {}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Dosage for the medication for the specific guidelines", formalDefinition="Dosage for the medication for the specific guidelines." )
        protected List<MedicationKnowledgeAdministrationGuidelinesDosageComponent> dosage;

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

        private static final long serialVersionUID = 1196999266L;

    /**
     * Constructor
     */
      public MedicationKnowledgeAdministrationGuidelinesComponent() {
        super();
      }

        /**
         * @return {@link #dosage} (Dosage for the medication for the specific guidelines.)
         */
        public List<MedicationKnowledgeAdministrationGuidelinesDosageComponent> getDosage() { 
          if (this.dosage == null)
            this.dosage = new ArrayList<MedicationKnowledgeAdministrationGuidelinesDosageComponent>();
          return this.dosage;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public MedicationKnowledgeAdministrationGuidelinesComponent setDosage(List<MedicationKnowledgeAdministrationGuidelinesDosageComponent> theDosage) { 
          this.dosage = theDosage;
          return this;
        }

        public boolean hasDosage() { 
          if (this.dosage == null)
            return false;
          for (MedicationKnowledgeAdministrationGuidelinesDosageComponent item : this.dosage)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public MedicationKnowledgeAdministrationGuidelinesDosageComponent addDosage() { //3
          MedicationKnowledgeAdministrationGuidelinesDosageComponent t = new MedicationKnowledgeAdministrationGuidelinesDosageComponent();
          if (this.dosage == null)
            this.dosage = new ArrayList<MedicationKnowledgeAdministrationGuidelinesDosageComponent>();
          this.dosage.add(t);
          return t;
        }

        public MedicationKnowledgeAdministrationGuidelinesComponent addDosage(MedicationKnowledgeAdministrationGuidelinesDosageComponent t) { //3
          if (t == null)
            return this;
          if (this.dosage == null)
            this.dosage = new ArrayList<MedicationKnowledgeAdministrationGuidelinesDosageComponent>();
          this.dosage.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #dosage}, creating it if it does not already exist
         */
        public MedicationKnowledgeAdministrationGuidelinesDosageComponent getDosageFirstRep() { 
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
          children.add(new Property("dosage", "", "Dosage for the medication for the specific guidelines.", 0, java.lang.Integer.MAX_VALUE, dosage));
          children.add(new Property("indication[x]", "CodeableConcept|Reference(ObservationDefinition)", "Indication for use that apply to the specific administration guidelines.", 0, 1, indication));
          children.add(new Property("patientCharacteristics", "", "Characteristics of the patient that are relevant to the administration guidelines (for example, height, weight,gender,  etc).", 0, java.lang.Integer.MAX_VALUE, patientCharacteristics));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -1326018889: /*dosage*/  return new Property("dosage", "", "Dosage for the medication for the specific guidelines.", 0, java.lang.Integer.MAX_VALUE, dosage);
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
        case -1326018889: /*dosage*/ return this.dosage == null ? new Base[0] : this.dosage.toArray(new Base[this.dosage.size()]); // MedicationKnowledgeAdministrationGuidelinesDosageComponent
        case -597168804: /*indication*/ return this.indication == null ? new Base[0] : new Base[] {this.indication}; // Type
        case -960531341: /*patientCharacteristics*/ return this.patientCharacteristics == null ? new Base[0] : this.patientCharacteristics.toArray(new Base[this.patientCharacteristics.size()]); // MedicationKnowledgeAdministrationGuidelinesPatientCharacteristicsComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1326018889: // dosage
          this.getDosage().add((MedicationKnowledgeAdministrationGuidelinesDosageComponent) value); // MedicationKnowledgeAdministrationGuidelinesDosageComponent
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
          this.getDosage().add((MedicationKnowledgeAdministrationGuidelinesDosageComponent) value);
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
        case -1326018889: /*dosage*/ return new String[] {};
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
          dst.dosage = new ArrayList<MedicationKnowledgeAdministrationGuidelinesDosageComponent>();
          for (MedicationKnowledgeAdministrationGuidelinesDosageComponent i : dosage)
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
    public static class MedicationKnowledgeAdministrationGuidelinesDosageComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The type of dosage (for example, prophylaxis, maintenance, therapeutic, etc).
         */
        @Child(name = "type", type = {CodeableConcept.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Type of dosage", formalDefinition="The type of dosage (for example, prophylaxis, maintenance, therapeutic, etc)." )
        protected CodeableConcept type;

        /**
         * Dosage for the medication for the specific guidelines.
         */
        @Child(name = "dosage", type = {Dosage.class}, order=2, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Dosage for the medication for the specific guidelines", formalDefinition="Dosage for the medication for the specific guidelines." )
        protected List<Dosage> dosage;

        private static final long serialVersionUID = 1578257961L;

    /**
     * Constructor
     */
      public MedicationKnowledgeAdministrationGuidelinesDosageComponent() {
        super();
      }

    /**
     * Constructor
     */
      public MedicationKnowledgeAdministrationGuidelinesDosageComponent(CodeableConcept type) {
        super();
        this.type = type;
      }

        /**
         * @return {@link #type} (The type of dosage (for example, prophylaxis, maintenance, therapeutic, etc).)
         */
        public CodeableConcept getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationKnowledgeAdministrationGuidelinesDosageComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new CodeableConcept(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (The type of dosage (for example, prophylaxis, maintenance, therapeutic, etc).)
         */
        public MedicationKnowledgeAdministrationGuidelinesDosageComponent setType(CodeableConcept value) { 
          this.type = value;
          return this;
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
        public MedicationKnowledgeAdministrationGuidelinesDosageComponent setDosage(List<Dosage> theDosage) { 
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

        public MedicationKnowledgeAdministrationGuidelinesDosageComponent addDosage(Dosage t) { //3
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

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("type", "CodeableConcept", "The type of dosage (for example, prophylaxis, maintenance, therapeutic, etc).", 0, 1, type));
          children.add(new Property("dosage", "Dosage", "Dosage for the medication for the specific guidelines.", 0, java.lang.Integer.MAX_VALUE, dosage));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3575610: /*type*/  return new Property("type", "CodeableConcept", "The type of dosage (for example, prophylaxis, maintenance, therapeutic, etc).", 0, 1, type);
          case -1326018889: /*dosage*/  return new Property("dosage", "Dosage", "Dosage for the medication for the specific guidelines.", 0, java.lang.Integer.MAX_VALUE, dosage);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case -1326018889: /*dosage*/ return this.dosage == null ? new Base[0] : this.dosage.toArray(new Base[this.dosage.size()]); // Dosage
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1326018889: // dosage
          this.getDosage().add(castToDosage(value)); // Dosage
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type")) {
          this.type = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("dosage")) {
          this.getDosage().add(castToDosage(value));
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610:  return getType(); 
        case -1326018889:  return addDosage(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case -1326018889: /*dosage*/ return new String[] {"Dosage"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("dosage")) {
          return addDosage();
        }
        else
          return super.addChild(name);
      }

      public MedicationKnowledgeAdministrationGuidelinesDosageComponent copy() {
        MedicationKnowledgeAdministrationGuidelinesDosageComponent dst = new MedicationKnowledgeAdministrationGuidelinesDosageComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        if (dosage != null) {
          dst.dosage = new ArrayList<Dosage>();
          for (Dosage i : dosage)
            dst.dosage.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof MedicationKnowledgeAdministrationGuidelinesDosageComponent))
          return false;
        MedicationKnowledgeAdministrationGuidelinesDosageComponent o = (MedicationKnowledgeAdministrationGuidelinesDosageComponent) other_;
        return compareDeep(type, o.type, true) && compareDeep(dosage, o.dosage, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof MedicationKnowledgeAdministrationGuidelinesDosageComponent))
          return false;
        MedicationKnowledgeAdministrationGuidelinesDosageComponent o = (MedicationKnowledgeAdministrationGuidelinesDosageComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(type, dosage);
      }

  public String fhirType() {
    return "MedicationKnowledge.administrationGuidelines.dosage";

  }

  }

    @Block()
    public static class MedicationKnowledgeAdministrationGuidelinesPatientCharacteristicsComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Specific characteristic that is relevant to the administration guideline (e.g. height, weight, gender).
         */
        @Child(name = "characteristic", type = {CodeableConcept.class, Quantity.class}, order=1, min=1, max=1, modifier=false, summary=false)
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
        public Quantity getCharacteristicQuantity() throws FHIRException { 
          if (this.characteristic == null)
            return null;
          if (!(this.characteristic instanceof Quantity))
            throw new FHIRException("Type mismatch: the type Quantity was expected, but "+this.characteristic.getClass().getName()+" was encountered");
          return (Quantity) this.characteristic;
        }

        public boolean hasCharacteristicQuantity() { 
          return this != null && this.characteristic instanceof Quantity;
        }

        public boolean hasCharacteristic() { 
          return this.characteristic != null && !this.characteristic.isEmpty();
        }

        /**
         * @param value {@link #characteristic} (Specific characteristic that is relevant to the administration guideline (e.g. height, weight, gender).)
         */
        public MedicationKnowledgeAdministrationGuidelinesPatientCharacteristicsComponent setCharacteristic(Type value) { 
          if (value != null && !(value instanceof CodeableConcept || value instanceof Quantity))
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
          case 1769373510: /*characteristicQuantity*/  return new Property("characteristic[x]", "CodeableConcept|SimpleQuantity", "Specific characteristic that is relevant to the administration guideline (e.g. height, weight, gender).", 0, 1, characteristic);
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
        else if (name.equals("characteristicQuantity")) {
          this.characteristic = new Quantity();
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
        @Child(name = "quantity", type = {Quantity.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The number of product units the package would contain if fully loaded", formalDefinition="The number of product units the package would contain if fully loaded." )
        protected Quantity quantity;

        private static final long serialVersionUID = -308052041L;

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
        public Quantity getQuantity() { 
          if (this.quantity == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationKnowledgePackagingComponent.quantity");
            else if (Configuration.doAutoCreate())
              this.quantity = new Quantity(); // cc
          return this.quantity;
        }

        public boolean hasQuantity() { 
          return this.quantity != null && !this.quantity.isEmpty();
        }

        /**
         * @param value {@link #quantity} (The number of product units the package would contain if fully loaded.)
         */
        public MedicationKnowledgePackagingComponent setQuantity(Quantity value) { 
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
        case -1285004149: /*quantity*/ return this.quantity == null ? new Base[0] : new Base[] {this.quantity}; // Quantity
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
          this.quantity = castToQuantity(value); // Quantity
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type")) {
          this.type = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("quantity")) {
          this.quantity = castToQuantity(value); // Quantity
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
          this.quantity = new Quantity();
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
        @Child(name = "value", type = {CodeableConcept.class, StringType.class, Quantity.class, Base64BinaryType.class}, order=2, min=0, max=1, modifier=false, summary=false)
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
        public Quantity getValueQuantity() throws FHIRException { 
          if (this.value == null)
            return null;
          if (!(this.value instanceof Quantity))
            throw new FHIRException("Type mismatch: the type Quantity was expected, but "+this.value.getClass().getName()+" was encountered");
          return (Quantity) this.value;
        }

        public boolean hasValueQuantity() { 
          return this != null && this.value instanceof Quantity;
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
          if (value != null && !(value instanceof CodeableConcept || value instanceof StringType || value instanceof Quantity || value instanceof Base64BinaryType))
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
          case -2029823716: /*valueQuantity*/  return new Property("value[x]", "CodeableConcept|string|SimpleQuantity|base64Binary", "Description of the characteristic.", 0, 1, value);
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
        else if (name.equals("valueQuantity")) {
          this.value = new Quantity();
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

    @Block()
    public static class MedicationKnowledgeRegulatoryComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The authority that is specifying the regulations.
         */
        @Child(name = "regulatoryAuthority", type = {Organization.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Specifies the authority of the regulation", formalDefinition="The authority that is specifying the regulations." )
        protected Reference regulatoryAuthority;

        /**
         * The actual object that is the target of the reference (The authority that is specifying the regulations.)
         */
        protected Organization regulatoryAuthorityTarget;

        /**
         * Specifies if changes are allowed when dispensing a medication from a regulatory perspective.
         */
        @Child(name = "substitution", type = {}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Specifies if changes are allowed when dispensing a medication from a regulatory perspective", formalDefinition="Specifies if changes are allowed when dispensing a medication from a regulatory perspective." )
        protected List<MedicationKnowledgeRegulatorySubstitutionComponent> substitution;

        /**
         * Specifies the schedule of a medication in jurisdiction.
         */
        @Child(name = "schedule", type = {}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Specifies the schedule of a medication in jurisdiction", formalDefinition="Specifies the schedule of a medication in jurisdiction." )
        protected List<MedicationKnowledgeRegulatoryScheduleComponent> schedule;

        /**
         * The maximum number of units of the medicaton that can be dispensed in a period.
         */
        @Child(name = "maxDispense", type = {}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The maximum number of units of the medicaton that can be dispensed in a period", formalDefinition="The maximum number of units of the medicaton that can be dispensed in a period." )
        protected MedicationKnowledgeRegulatoryMaxDispenseComponent maxDispense;

        private static final long serialVersionUID = -1252605487L;

    /**
     * Constructor
     */
      public MedicationKnowledgeRegulatoryComponent() {
        super();
      }

    /**
     * Constructor
     */
      public MedicationKnowledgeRegulatoryComponent(Reference regulatoryAuthority) {
        super();
        this.regulatoryAuthority = regulatoryAuthority;
      }

        /**
         * @return {@link #regulatoryAuthority} (The authority that is specifying the regulations.)
         */
        public Reference getRegulatoryAuthority() { 
          if (this.regulatoryAuthority == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationKnowledgeRegulatoryComponent.regulatoryAuthority");
            else if (Configuration.doAutoCreate())
              this.regulatoryAuthority = new Reference(); // cc
          return this.regulatoryAuthority;
        }

        public boolean hasRegulatoryAuthority() { 
          return this.regulatoryAuthority != null && !this.regulatoryAuthority.isEmpty();
        }

        /**
         * @param value {@link #regulatoryAuthority} (The authority that is specifying the regulations.)
         */
        public MedicationKnowledgeRegulatoryComponent setRegulatoryAuthority(Reference value) { 
          this.regulatoryAuthority = value;
          return this;
        }

        /**
         * @return {@link #regulatoryAuthority} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The authority that is specifying the regulations.)
         */
        public Organization getRegulatoryAuthorityTarget() { 
          if (this.regulatoryAuthorityTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationKnowledgeRegulatoryComponent.regulatoryAuthority");
            else if (Configuration.doAutoCreate())
              this.regulatoryAuthorityTarget = new Organization(); // aa
          return this.regulatoryAuthorityTarget;
        }

        /**
         * @param value {@link #regulatoryAuthority} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The authority that is specifying the regulations.)
         */
        public MedicationKnowledgeRegulatoryComponent setRegulatoryAuthorityTarget(Organization value) { 
          this.regulatoryAuthorityTarget = value;
          return this;
        }

        /**
         * @return {@link #substitution} (Specifies if changes are allowed when dispensing a medication from a regulatory perspective.)
         */
        public List<MedicationKnowledgeRegulatorySubstitutionComponent> getSubstitution() { 
          if (this.substitution == null)
            this.substitution = new ArrayList<MedicationKnowledgeRegulatorySubstitutionComponent>();
          return this.substitution;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public MedicationKnowledgeRegulatoryComponent setSubstitution(List<MedicationKnowledgeRegulatorySubstitutionComponent> theSubstitution) { 
          this.substitution = theSubstitution;
          return this;
        }

        public boolean hasSubstitution() { 
          if (this.substitution == null)
            return false;
          for (MedicationKnowledgeRegulatorySubstitutionComponent item : this.substitution)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public MedicationKnowledgeRegulatorySubstitutionComponent addSubstitution() { //3
          MedicationKnowledgeRegulatorySubstitutionComponent t = new MedicationKnowledgeRegulatorySubstitutionComponent();
          if (this.substitution == null)
            this.substitution = new ArrayList<MedicationKnowledgeRegulatorySubstitutionComponent>();
          this.substitution.add(t);
          return t;
        }

        public MedicationKnowledgeRegulatoryComponent addSubstitution(MedicationKnowledgeRegulatorySubstitutionComponent t) { //3
          if (t == null)
            return this;
          if (this.substitution == null)
            this.substitution = new ArrayList<MedicationKnowledgeRegulatorySubstitutionComponent>();
          this.substitution.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #substitution}, creating it if it does not already exist
         */
        public MedicationKnowledgeRegulatorySubstitutionComponent getSubstitutionFirstRep() { 
          if (getSubstitution().isEmpty()) {
            addSubstitution();
          }
          return getSubstitution().get(0);
        }

        /**
         * @return {@link #schedule} (Specifies the schedule of a medication in jurisdiction.)
         */
        public List<MedicationKnowledgeRegulatoryScheduleComponent> getSchedule() { 
          if (this.schedule == null)
            this.schedule = new ArrayList<MedicationKnowledgeRegulatoryScheduleComponent>();
          return this.schedule;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public MedicationKnowledgeRegulatoryComponent setSchedule(List<MedicationKnowledgeRegulatoryScheduleComponent> theSchedule) { 
          this.schedule = theSchedule;
          return this;
        }

        public boolean hasSchedule() { 
          if (this.schedule == null)
            return false;
          for (MedicationKnowledgeRegulatoryScheduleComponent item : this.schedule)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public MedicationKnowledgeRegulatoryScheduleComponent addSchedule() { //3
          MedicationKnowledgeRegulatoryScheduleComponent t = new MedicationKnowledgeRegulatoryScheduleComponent();
          if (this.schedule == null)
            this.schedule = new ArrayList<MedicationKnowledgeRegulatoryScheduleComponent>();
          this.schedule.add(t);
          return t;
        }

        public MedicationKnowledgeRegulatoryComponent addSchedule(MedicationKnowledgeRegulatoryScheduleComponent t) { //3
          if (t == null)
            return this;
          if (this.schedule == null)
            this.schedule = new ArrayList<MedicationKnowledgeRegulatoryScheduleComponent>();
          this.schedule.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #schedule}, creating it if it does not already exist
         */
        public MedicationKnowledgeRegulatoryScheduleComponent getScheduleFirstRep() { 
          if (getSchedule().isEmpty()) {
            addSchedule();
          }
          return getSchedule().get(0);
        }

        /**
         * @return {@link #maxDispense} (The maximum number of units of the medicaton that can be dispensed in a period.)
         */
        public MedicationKnowledgeRegulatoryMaxDispenseComponent getMaxDispense() { 
          if (this.maxDispense == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationKnowledgeRegulatoryComponent.maxDispense");
            else if (Configuration.doAutoCreate())
              this.maxDispense = new MedicationKnowledgeRegulatoryMaxDispenseComponent(); // cc
          return this.maxDispense;
        }

        public boolean hasMaxDispense() { 
          return this.maxDispense != null && !this.maxDispense.isEmpty();
        }

        /**
         * @param value {@link #maxDispense} (The maximum number of units of the medicaton that can be dispensed in a period.)
         */
        public MedicationKnowledgeRegulatoryComponent setMaxDispense(MedicationKnowledgeRegulatoryMaxDispenseComponent value) { 
          this.maxDispense = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("regulatoryAuthority", "Reference(Organization)", "The authority that is specifying the regulations.", 0, 1, regulatoryAuthority));
          children.add(new Property("substitution", "", "Specifies if changes are allowed when dispensing a medication from a regulatory perspective.", 0, java.lang.Integer.MAX_VALUE, substitution));
          children.add(new Property("schedule", "", "Specifies the schedule of a medication in jurisdiction.", 0, java.lang.Integer.MAX_VALUE, schedule));
          children.add(new Property("maxDispense", "", "The maximum number of units of the medicaton that can be dispensed in a period.", 0, 1, maxDispense));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 711233419: /*regulatoryAuthority*/  return new Property("regulatoryAuthority", "Reference(Organization)", "The authority that is specifying the regulations.", 0, 1, regulatoryAuthority);
          case 826147581: /*substitution*/  return new Property("substitution", "", "Specifies if changes are allowed when dispensing a medication from a regulatory perspective.", 0, java.lang.Integer.MAX_VALUE, substitution);
          case -697920873: /*schedule*/  return new Property("schedule", "", "Specifies the schedule of a medication in jurisdiction.", 0, java.lang.Integer.MAX_VALUE, schedule);
          case -1977784607: /*maxDispense*/  return new Property("maxDispense", "", "The maximum number of units of the medicaton that can be dispensed in a period.", 0, 1, maxDispense);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 711233419: /*regulatoryAuthority*/ return this.regulatoryAuthority == null ? new Base[0] : new Base[] {this.regulatoryAuthority}; // Reference
        case 826147581: /*substitution*/ return this.substitution == null ? new Base[0] : this.substitution.toArray(new Base[this.substitution.size()]); // MedicationKnowledgeRegulatorySubstitutionComponent
        case -697920873: /*schedule*/ return this.schedule == null ? new Base[0] : this.schedule.toArray(new Base[this.schedule.size()]); // MedicationKnowledgeRegulatoryScheduleComponent
        case -1977784607: /*maxDispense*/ return this.maxDispense == null ? new Base[0] : new Base[] {this.maxDispense}; // MedicationKnowledgeRegulatoryMaxDispenseComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 711233419: // regulatoryAuthority
          this.regulatoryAuthority = castToReference(value); // Reference
          return value;
        case 826147581: // substitution
          this.getSubstitution().add((MedicationKnowledgeRegulatorySubstitutionComponent) value); // MedicationKnowledgeRegulatorySubstitutionComponent
          return value;
        case -697920873: // schedule
          this.getSchedule().add((MedicationKnowledgeRegulatoryScheduleComponent) value); // MedicationKnowledgeRegulatoryScheduleComponent
          return value;
        case -1977784607: // maxDispense
          this.maxDispense = (MedicationKnowledgeRegulatoryMaxDispenseComponent) value; // MedicationKnowledgeRegulatoryMaxDispenseComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("regulatoryAuthority")) {
          this.regulatoryAuthority = castToReference(value); // Reference
        } else if (name.equals("substitution")) {
          this.getSubstitution().add((MedicationKnowledgeRegulatorySubstitutionComponent) value);
        } else if (name.equals("schedule")) {
          this.getSchedule().add((MedicationKnowledgeRegulatoryScheduleComponent) value);
        } else if (name.equals("maxDispense")) {
          this.maxDispense = (MedicationKnowledgeRegulatoryMaxDispenseComponent) value; // MedicationKnowledgeRegulatoryMaxDispenseComponent
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 711233419:  return getRegulatoryAuthority(); 
        case 826147581:  return addSubstitution(); 
        case -697920873:  return addSchedule(); 
        case -1977784607:  return getMaxDispense(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 711233419: /*regulatoryAuthority*/ return new String[] {"Reference"};
        case 826147581: /*substitution*/ return new String[] {};
        case -697920873: /*schedule*/ return new String[] {};
        case -1977784607: /*maxDispense*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("regulatoryAuthority")) {
          this.regulatoryAuthority = new Reference();
          return this.regulatoryAuthority;
        }
        else if (name.equals("substitution")) {
          return addSubstitution();
        }
        else if (name.equals("schedule")) {
          return addSchedule();
        }
        else if (name.equals("maxDispense")) {
          this.maxDispense = new MedicationKnowledgeRegulatoryMaxDispenseComponent();
          return this.maxDispense;
        }
        else
          return super.addChild(name);
      }

      public MedicationKnowledgeRegulatoryComponent copy() {
        MedicationKnowledgeRegulatoryComponent dst = new MedicationKnowledgeRegulatoryComponent();
        copyValues(dst);
        dst.regulatoryAuthority = regulatoryAuthority == null ? null : regulatoryAuthority.copy();
        if (substitution != null) {
          dst.substitution = new ArrayList<MedicationKnowledgeRegulatorySubstitutionComponent>();
          for (MedicationKnowledgeRegulatorySubstitutionComponent i : substitution)
            dst.substitution.add(i.copy());
        };
        if (schedule != null) {
          dst.schedule = new ArrayList<MedicationKnowledgeRegulatoryScheduleComponent>();
          for (MedicationKnowledgeRegulatoryScheduleComponent i : schedule)
            dst.schedule.add(i.copy());
        };
        dst.maxDispense = maxDispense == null ? null : maxDispense.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof MedicationKnowledgeRegulatoryComponent))
          return false;
        MedicationKnowledgeRegulatoryComponent o = (MedicationKnowledgeRegulatoryComponent) other_;
        return compareDeep(regulatoryAuthority, o.regulatoryAuthority, true) && compareDeep(substitution, o.substitution, true)
           && compareDeep(schedule, o.schedule, true) && compareDeep(maxDispense, o.maxDispense, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof MedicationKnowledgeRegulatoryComponent))
          return false;
        MedicationKnowledgeRegulatoryComponent o = (MedicationKnowledgeRegulatoryComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(regulatoryAuthority, substitution
          , schedule, maxDispense);
      }

  public String fhirType() {
    return "MedicationKnowledge.regulatory";

  }

  }

    @Block()
    public static class MedicationKnowledgeRegulatorySubstitutionComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Specifies the type of substitution allowed.
         */
        @Child(name = "type", type = {CodeableConcept.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Specifies the type of substitution allowed", formalDefinition="Specifies the type of substitution allowed." )
        protected CodeableConcept type;

        /**
         * Specifies if regulation allows for changes in the medication when dispensing.
         */
        @Child(name = "allowed", type = {BooleanType.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Specifies if regulation allows for changes in the medication when dispensing", formalDefinition="Specifies if regulation allows for changes in the medication when dispensing." )
        protected BooleanType allowed;

        private static final long serialVersionUID = 396354861L;

    /**
     * Constructor
     */
      public MedicationKnowledgeRegulatorySubstitutionComponent() {
        super();
      }

    /**
     * Constructor
     */
      public MedicationKnowledgeRegulatorySubstitutionComponent(CodeableConcept type, BooleanType allowed) {
        super();
        this.type = type;
        this.allowed = allowed;
      }

        /**
         * @return {@link #type} (Specifies the type of substitution allowed.)
         */
        public CodeableConcept getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationKnowledgeRegulatorySubstitutionComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new CodeableConcept(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (Specifies the type of substitution allowed.)
         */
        public MedicationKnowledgeRegulatorySubstitutionComponent setType(CodeableConcept value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #allowed} (Specifies if regulation allows for changes in the medication when dispensing.). This is the underlying object with id, value and extensions. The accessor "getAllowed" gives direct access to the value
         */
        public BooleanType getAllowedElement() { 
          if (this.allowed == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationKnowledgeRegulatorySubstitutionComponent.allowed");
            else if (Configuration.doAutoCreate())
              this.allowed = new BooleanType(); // bb
          return this.allowed;
        }

        public boolean hasAllowedElement() { 
          return this.allowed != null && !this.allowed.isEmpty();
        }

        public boolean hasAllowed() { 
          return this.allowed != null && !this.allowed.isEmpty();
        }

        /**
         * @param value {@link #allowed} (Specifies if regulation allows for changes in the medication when dispensing.). This is the underlying object with id, value and extensions. The accessor "getAllowed" gives direct access to the value
         */
        public MedicationKnowledgeRegulatorySubstitutionComponent setAllowedElement(BooleanType value) { 
          this.allowed = value;
          return this;
        }

        /**
         * @return Specifies if regulation allows for changes in the medication when dispensing.
         */
        public boolean getAllowed() { 
          return this.allowed == null || this.allowed.isEmpty() ? false : this.allowed.getValue();
        }

        /**
         * @param value Specifies if regulation allows for changes in the medication when dispensing.
         */
        public MedicationKnowledgeRegulatorySubstitutionComponent setAllowed(boolean value) { 
            if (this.allowed == null)
              this.allowed = new BooleanType();
            this.allowed.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("type", "CodeableConcept", "Specifies the type of substitution allowed.", 0, 1, type));
          children.add(new Property("allowed", "boolean", "Specifies if regulation allows for changes in the medication when dispensing.", 0, 1, allowed));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3575610: /*type*/  return new Property("type", "CodeableConcept", "Specifies the type of substitution allowed.", 0, 1, type);
          case -911343192: /*allowed*/  return new Property("allowed", "boolean", "Specifies if regulation allows for changes in the medication when dispensing.", 0, 1, allowed);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case -911343192: /*allowed*/ return this.allowed == null ? new Base[0] : new Base[] {this.allowed}; // BooleanType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -911343192: // allowed
          this.allowed = castToBoolean(value); // BooleanType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type")) {
          this.type = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("allowed")) {
          this.allowed = castToBoolean(value); // BooleanType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610:  return getType(); 
        case -911343192:  return getAllowedElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case -911343192: /*allowed*/ return new String[] {"boolean"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("allowed")) {
          throw new FHIRException("Cannot call addChild on a primitive type MedicationKnowledge.allowed");
        }
        else
          return super.addChild(name);
      }

      public MedicationKnowledgeRegulatorySubstitutionComponent copy() {
        MedicationKnowledgeRegulatorySubstitutionComponent dst = new MedicationKnowledgeRegulatorySubstitutionComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.allowed = allowed == null ? null : allowed.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof MedicationKnowledgeRegulatorySubstitutionComponent))
          return false;
        MedicationKnowledgeRegulatorySubstitutionComponent o = (MedicationKnowledgeRegulatorySubstitutionComponent) other_;
        return compareDeep(type, o.type, true) && compareDeep(allowed, o.allowed, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof MedicationKnowledgeRegulatorySubstitutionComponent))
          return false;
        MedicationKnowledgeRegulatorySubstitutionComponent o = (MedicationKnowledgeRegulatorySubstitutionComponent) other_;
        return compareValues(allowed, o.allowed, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(type, allowed);
      }

  public String fhirType() {
    return "MedicationKnowledge.regulatory.substitution";

  }

  }

    @Block()
    public static class MedicationKnowledgeRegulatoryScheduleComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Specifies the specific drug schedule.
         */
        @Child(name = "schedule", type = {CodeableConcept.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Specifies the specific drug schedule", formalDefinition="Specifies the specific drug schedule." )
        protected CodeableConcept schedule;

        private static final long serialVersionUID = 1955520912L;

    /**
     * Constructor
     */
      public MedicationKnowledgeRegulatoryScheduleComponent() {
        super();
      }

    /**
     * Constructor
     */
      public MedicationKnowledgeRegulatoryScheduleComponent(CodeableConcept schedule) {
        super();
        this.schedule = schedule;
      }

        /**
         * @return {@link #schedule} (Specifies the specific drug schedule.)
         */
        public CodeableConcept getSchedule() { 
          if (this.schedule == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationKnowledgeRegulatoryScheduleComponent.schedule");
            else if (Configuration.doAutoCreate())
              this.schedule = new CodeableConcept(); // cc
          return this.schedule;
        }

        public boolean hasSchedule() { 
          return this.schedule != null && !this.schedule.isEmpty();
        }

        /**
         * @param value {@link #schedule} (Specifies the specific drug schedule.)
         */
        public MedicationKnowledgeRegulatoryScheduleComponent setSchedule(CodeableConcept value) { 
          this.schedule = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("schedule", "CodeableConcept", "Specifies the specific drug schedule.", 0, 1, schedule));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -697920873: /*schedule*/  return new Property("schedule", "CodeableConcept", "Specifies the specific drug schedule.", 0, 1, schedule);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -697920873: /*schedule*/ return this.schedule == null ? new Base[0] : new Base[] {this.schedule}; // CodeableConcept
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -697920873: // schedule
          this.schedule = castToCodeableConcept(value); // CodeableConcept
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("schedule")) {
          this.schedule = castToCodeableConcept(value); // CodeableConcept
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -697920873:  return getSchedule(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -697920873: /*schedule*/ return new String[] {"CodeableConcept"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("schedule")) {
          this.schedule = new CodeableConcept();
          return this.schedule;
        }
        else
          return super.addChild(name);
      }

      public MedicationKnowledgeRegulatoryScheduleComponent copy() {
        MedicationKnowledgeRegulatoryScheduleComponent dst = new MedicationKnowledgeRegulatoryScheduleComponent();
        copyValues(dst);
        dst.schedule = schedule == null ? null : schedule.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof MedicationKnowledgeRegulatoryScheduleComponent))
          return false;
        MedicationKnowledgeRegulatoryScheduleComponent o = (MedicationKnowledgeRegulatoryScheduleComponent) other_;
        return compareDeep(schedule, o.schedule, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof MedicationKnowledgeRegulatoryScheduleComponent))
          return false;
        MedicationKnowledgeRegulatoryScheduleComponent o = (MedicationKnowledgeRegulatoryScheduleComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(schedule);
      }

  public String fhirType() {
    return "MedicationKnowledge.regulatory.schedule";

  }

  }

    @Block()
    public static class MedicationKnowledgeRegulatoryMaxDispenseComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The maximum number of units of the medicaton that can be dispensed.
         */
        @Child(name = "quantity", type = {Quantity.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The maximum number of units of the medicaton that can be dispensed", formalDefinition="The maximum number of units of the medicaton that can be dispensed." )
        protected Quantity quantity;

        /**
         * The period that applies to the maximum number of units.
         */
        @Child(name = "period", type = {Duration.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The period that applies to the maximum number of units", formalDefinition="The period that applies to the maximum number of units." )
        protected Duration period;

        private static final long serialVersionUID = -441724185L;

    /**
     * Constructor
     */
      public MedicationKnowledgeRegulatoryMaxDispenseComponent() {
        super();
      }

    /**
     * Constructor
     */
      public MedicationKnowledgeRegulatoryMaxDispenseComponent(Quantity quantity) {
        super();
        this.quantity = quantity;
      }

        /**
         * @return {@link #quantity} (The maximum number of units of the medicaton that can be dispensed.)
         */
        public Quantity getQuantity() { 
          if (this.quantity == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationKnowledgeRegulatoryMaxDispenseComponent.quantity");
            else if (Configuration.doAutoCreate())
              this.quantity = new Quantity(); // cc
          return this.quantity;
        }

        public boolean hasQuantity() { 
          return this.quantity != null && !this.quantity.isEmpty();
        }

        /**
         * @param value {@link #quantity} (The maximum number of units of the medicaton that can be dispensed.)
         */
        public MedicationKnowledgeRegulatoryMaxDispenseComponent setQuantity(Quantity value) { 
          this.quantity = value;
          return this;
        }

        /**
         * @return {@link #period} (The period that applies to the maximum number of units.)
         */
        public Duration getPeriod() { 
          if (this.period == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationKnowledgeRegulatoryMaxDispenseComponent.period");
            else if (Configuration.doAutoCreate())
              this.period = new Duration(); // cc
          return this.period;
        }

        public boolean hasPeriod() { 
          return this.period != null && !this.period.isEmpty();
        }

        /**
         * @param value {@link #period} (The period that applies to the maximum number of units.)
         */
        public MedicationKnowledgeRegulatoryMaxDispenseComponent setPeriod(Duration value) { 
          this.period = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("quantity", "SimpleQuantity", "The maximum number of units of the medicaton that can be dispensed.", 0, 1, quantity));
          children.add(new Property("period", "Duration", "The period that applies to the maximum number of units.", 0, 1, period));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -1285004149: /*quantity*/  return new Property("quantity", "SimpleQuantity", "The maximum number of units of the medicaton that can be dispensed.", 0, 1, quantity);
          case -991726143: /*period*/  return new Property("period", "Duration", "The period that applies to the maximum number of units.", 0, 1, period);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1285004149: /*quantity*/ return this.quantity == null ? new Base[0] : new Base[] {this.quantity}; // Quantity
        case -991726143: /*period*/ return this.period == null ? new Base[0] : new Base[] {this.period}; // Duration
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1285004149: // quantity
          this.quantity = castToQuantity(value); // Quantity
          return value;
        case -991726143: // period
          this.period = castToDuration(value); // Duration
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("quantity")) {
          this.quantity = castToQuantity(value); // Quantity
        } else if (name.equals("period")) {
          this.period = castToDuration(value); // Duration
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1285004149:  return getQuantity(); 
        case -991726143:  return getPeriod(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1285004149: /*quantity*/ return new String[] {"SimpleQuantity"};
        case -991726143: /*period*/ return new String[] {"Duration"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("quantity")) {
          this.quantity = new Quantity();
          return this.quantity;
        }
        else if (name.equals("period")) {
          this.period = new Duration();
          return this.period;
        }
        else
          return super.addChild(name);
      }

      public MedicationKnowledgeRegulatoryMaxDispenseComponent copy() {
        MedicationKnowledgeRegulatoryMaxDispenseComponent dst = new MedicationKnowledgeRegulatoryMaxDispenseComponent();
        copyValues(dst);
        dst.quantity = quantity == null ? null : quantity.copy();
        dst.period = period == null ? null : period.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof MedicationKnowledgeRegulatoryMaxDispenseComponent))
          return false;
        MedicationKnowledgeRegulatoryMaxDispenseComponent o = (MedicationKnowledgeRegulatoryMaxDispenseComponent) other_;
        return compareDeep(quantity, o.quantity, true) && compareDeep(period, o.period, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof MedicationKnowledgeRegulatoryMaxDispenseComponent))
          return false;
        MedicationKnowledgeRegulatoryMaxDispenseComponent o = (MedicationKnowledgeRegulatoryMaxDispenseComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(quantity, period);
      }

  public String fhirType() {
    return "MedicationKnowledge.regulatory.maxDispense";

  }

  }

    @Block()
    public static class MedicationKnowledgeKineticsComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The drug concentration measured at certain discrete points in time.
         */
        @Child(name = "areaUnderCurve", type = {Quantity.class}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="The drug concentration measured at certain discrete points in time", formalDefinition="The drug concentration measured at certain discrete points in time." )
        protected List<Quantity> areaUnderCurve;

        /**
         * The median lethal dose of a drug.
         */
        @Child(name = "lethalDose50", type = {Quantity.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="The median lethal dose of a drug", formalDefinition="The median lethal dose of a drug." )
        protected List<Quantity> lethalDose50;

        /**
         * The time required for any specified property (e.g., the concentration of a substance in the body) to decrease by half.
         */
        @Child(name = "halfLifePeriod", type = {Duration.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Time required for concentration in the body to decrease by half", formalDefinition="The time required for any specified property (e.g., the concentration of a substance in the body) to decrease by half." )
        protected Duration halfLifePeriod;

        private static final long serialVersionUID = -206244264L;

    /**
     * Constructor
     */
      public MedicationKnowledgeKineticsComponent() {
        super();
      }

        /**
         * @return {@link #areaUnderCurve} (The drug concentration measured at certain discrete points in time.)
         */
        public List<Quantity> getAreaUnderCurve() { 
          if (this.areaUnderCurve == null)
            this.areaUnderCurve = new ArrayList<Quantity>();
          return this.areaUnderCurve;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public MedicationKnowledgeKineticsComponent setAreaUnderCurve(List<Quantity> theAreaUnderCurve) { 
          this.areaUnderCurve = theAreaUnderCurve;
          return this;
        }

        public boolean hasAreaUnderCurve() { 
          if (this.areaUnderCurve == null)
            return false;
          for (Quantity item : this.areaUnderCurve)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public Quantity addAreaUnderCurve() { //3
          Quantity t = new Quantity();
          if (this.areaUnderCurve == null)
            this.areaUnderCurve = new ArrayList<Quantity>();
          this.areaUnderCurve.add(t);
          return t;
        }

        public MedicationKnowledgeKineticsComponent addAreaUnderCurve(Quantity t) { //3
          if (t == null)
            return this;
          if (this.areaUnderCurve == null)
            this.areaUnderCurve = new ArrayList<Quantity>();
          this.areaUnderCurve.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #areaUnderCurve}, creating it if it does not already exist
         */
        public Quantity getAreaUnderCurveFirstRep() { 
          if (getAreaUnderCurve().isEmpty()) {
            addAreaUnderCurve();
          }
          return getAreaUnderCurve().get(0);
        }

        /**
         * @return {@link #lethalDose50} (The median lethal dose of a drug.)
         */
        public List<Quantity> getLethalDose50() { 
          if (this.lethalDose50 == null)
            this.lethalDose50 = new ArrayList<Quantity>();
          return this.lethalDose50;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public MedicationKnowledgeKineticsComponent setLethalDose50(List<Quantity> theLethalDose50) { 
          this.lethalDose50 = theLethalDose50;
          return this;
        }

        public boolean hasLethalDose50() { 
          if (this.lethalDose50 == null)
            return false;
          for (Quantity item : this.lethalDose50)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public Quantity addLethalDose50() { //3
          Quantity t = new Quantity();
          if (this.lethalDose50 == null)
            this.lethalDose50 = new ArrayList<Quantity>();
          this.lethalDose50.add(t);
          return t;
        }

        public MedicationKnowledgeKineticsComponent addLethalDose50(Quantity t) { //3
          if (t == null)
            return this;
          if (this.lethalDose50 == null)
            this.lethalDose50 = new ArrayList<Quantity>();
          this.lethalDose50.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #lethalDose50}, creating it if it does not already exist
         */
        public Quantity getLethalDose50FirstRep() { 
          if (getLethalDose50().isEmpty()) {
            addLethalDose50();
          }
          return getLethalDose50().get(0);
        }

        /**
         * @return {@link #halfLifePeriod} (The time required for any specified property (e.g., the concentration of a substance in the body) to decrease by half.)
         */
        public Duration getHalfLifePeriod() { 
          if (this.halfLifePeriod == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationKnowledgeKineticsComponent.halfLifePeriod");
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
        public MedicationKnowledgeKineticsComponent setHalfLifePeriod(Duration value) { 
          this.halfLifePeriod = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("areaUnderCurve", "SimpleQuantity", "The drug concentration measured at certain discrete points in time.", 0, java.lang.Integer.MAX_VALUE, areaUnderCurve));
          children.add(new Property("lethalDose50", "SimpleQuantity", "The median lethal dose of a drug.", 0, java.lang.Integer.MAX_VALUE, lethalDose50));
          children.add(new Property("halfLifePeriod", "Duration", "The time required for any specified property (e.g., the concentration of a substance in the body) to decrease by half.", 0, 1, halfLifePeriod));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 1243936100: /*areaUnderCurve*/  return new Property("areaUnderCurve", "SimpleQuantity", "The drug concentration measured at certain discrete points in time.", 0, java.lang.Integer.MAX_VALUE, areaUnderCurve);
          case 302983216: /*lethalDose50*/  return new Property("lethalDose50", "SimpleQuantity", "The median lethal dose of a drug.", 0, java.lang.Integer.MAX_VALUE, lethalDose50);
          case -628810640: /*halfLifePeriod*/  return new Property("halfLifePeriod", "Duration", "The time required for any specified property (e.g., the concentration of a substance in the body) to decrease by half.", 0, 1, halfLifePeriod);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 1243936100: /*areaUnderCurve*/ return this.areaUnderCurve == null ? new Base[0] : this.areaUnderCurve.toArray(new Base[this.areaUnderCurve.size()]); // Quantity
        case 302983216: /*lethalDose50*/ return this.lethalDose50 == null ? new Base[0] : this.lethalDose50.toArray(new Base[this.lethalDose50.size()]); // Quantity
        case -628810640: /*halfLifePeriod*/ return this.halfLifePeriod == null ? new Base[0] : new Base[] {this.halfLifePeriod}; // Duration
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 1243936100: // areaUnderCurve
          this.getAreaUnderCurve().add(castToQuantity(value)); // Quantity
          return value;
        case 302983216: // lethalDose50
          this.getLethalDose50().add(castToQuantity(value)); // Quantity
          return value;
        case -628810640: // halfLifePeriod
          this.halfLifePeriod = castToDuration(value); // Duration
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("areaUnderCurve")) {
          this.getAreaUnderCurve().add(castToQuantity(value));
        } else if (name.equals("lethalDose50")) {
          this.getLethalDose50().add(castToQuantity(value));
        } else if (name.equals("halfLifePeriod")) {
          this.halfLifePeriod = castToDuration(value); // Duration
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1243936100:  return addAreaUnderCurve(); 
        case 302983216:  return addLethalDose50(); 
        case -628810640:  return getHalfLifePeriod(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1243936100: /*areaUnderCurve*/ return new String[] {"SimpleQuantity"};
        case 302983216: /*lethalDose50*/ return new String[] {"SimpleQuantity"};
        case -628810640: /*halfLifePeriod*/ return new String[] {"Duration"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("areaUnderCurve")) {
          return addAreaUnderCurve();
        }
        else if (name.equals("lethalDose50")) {
          return addLethalDose50();
        }
        else if (name.equals("halfLifePeriod")) {
          this.halfLifePeriod = new Duration();
          return this.halfLifePeriod;
        }
        else
          return super.addChild(name);
      }

      public MedicationKnowledgeKineticsComponent copy() {
        MedicationKnowledgeKineticsComponent dst = new MedicationKnowledgeKineticsComponent();
        copyValues(dst);
        if (areaUnderCurve != null) {
          dst.areaUnderCurve = new ArrayList<Quantity>();
          for (Quantity i : areaUnderCurve)
            dst.areaUnderCurve.add(i.copy());
        };
        if (lethalDose50 != null) {
          dst.lethalDose50 = new ArrayList<Quantity>();
          for (Quantity i : lethalDose50)
            dst.lethalDose50.add(i.copy());
        };
        dst.halfLifePeriod = halfLifePeriod == null ? null : halfLifePeriod.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof MedicationKnowledgeKineticsComponent))
          return false;
        MedicationKnowledgeKineticsComponent o = (MedicationKnowledgeKineticsComponent) other_;
        return compareDeep(areaUnderCurve, o.areaUnderCurve, true) && compareDeep(lethalDose50, o.lethalDose50, true)
           && compareDeep(halfLifePeriod, o.halfLifePeriod, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof MedicationKnowledgeKineticsComponent))
          return false;
        MedicationKnowledgeKineticsComponent o = (MedicationKnowledgeKineticsComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(areaUnderCurve, lethalDose50
          , halfLifePeriod);
      }

  public String fhirType() {
    return "MedicationKnowledge.kinetics";

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
    @Child(name = "status", type = {CodeType.class}, order=1, min=0, max=1, modifier=true, summary=true)
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
    @Child(name = "doseForm", type = {CodeableConcept.class}, order=3, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="powder | tablets | capsule +", formalDefinition="Describes the form of the item.  Powder; tablets; capsule." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/medication-form-codes")
    protected CodeableConcept doseForm;

    /**
     * Specific amount of the drug in the packaged product.  For example, when specifying a product that has the same strength (For example, Insulin glargine 100 unit per mL solution for injection), this attribute provides additional clarification of the package amount (For example, 3 mL, 10mL, etc).
     */
    @Child(name = "amount", type = {Quantity.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Amount of drug in package", formalDefinition="Specific amount of the drug in the packaged product.  For example, when specifying a product that has the same strength (For example, Insulin glargine 100 unit per mL solution for injection), this attribute provides additional clarification of the package amount (For example, 3 mL, 10mL, etc)." )
    protected Quantity amount;

    /**
     * Additional names for a medication, for example, the name(s) given to a medication in different countries.  For example, acetaminophen and paracetamol or salbutamol and albuterol.
     */
    @Child(name = "synonym", type = {StringType.class}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Additional names for a medication", formalDefinition="Additional names for a medication, for example, the name(s) given to a medication in different countries.  For example, acetaminophen and paracetamol or salbutamol and albuterol." )
    protected List<StringType> synonym;

    /**
     * Associated or related knowledge about a medication.
     */
    @Child(name = "relatedMedicationKnowledge", type = {}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Associated or related medication information", formalDefinition="Associated or related knowledge about a medication." )
    protected List<MedicationKnowledgeRelatedMedicationKnowledgeComponent> relatedMedicationKnowledge;

    /**
     * Associated or related medications.  For example, if the medication is a branded product (e.g. Crestor), this is the Therapeutic Moeity (e.g. Rosuvastatin) or if this is a generic medication (e.g. Rosuvastatin), this would link to a branded product (e.g. Crestor).
     */
    @Child(name = "associatedMedication", type = {Medication.class}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="A medication resource that is associated with this medication", formalDefinition="Associated or related medications.  For example, if the medication is a branded product (e.g. Crestor), this is the Therapeutic Moeity (e.g. Rosuvastatin) or if this is a generic medication (e.g. Rosuvastatin), this would link to a branded product (e.g. Crestor)." )
    protected List<Reference> associatedMedication;
    /**
     * The actual objects that are the target of the reference (Associated or related medications.  For example, if the medication is a branded product (e.g. Crestor), this is the Therapeutic Moeity (e.g. Rosuvastatin) or if this is a generic medication (e.g. Rosuvastatin), this would link to a branded product (e.g. Crestor).)
     */
    protected List<Medication> associatedMedicationTarget;


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
     * Identifies a particular constituent of interest in the product.
     */
    @Child(name = "ingredient", type = {}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Active or inactive ingredient", formalDefinition="Identifies a particular constituent of interest in the product." )
    protected List<MedicationKnowledgeIngredientComponent> ingredient;

    /**
     * The instructions for preparing the medication.
     */
    @Child(name = "preparationInstruction", type = {MarkdownType.class}, order=11, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="The instructions for preparing the medication", formalDefinition="The instructions for preparing the medication." )
    protected MarkdownType preparationInstruction;

    /**
     * The intended or approved route of administration.
     */
    @Child(name = "intendedRoute", type = {CodeableConcept.class}, order=12, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="The intended or approved route of administration", formalDefinition="The intended or approved route of administration." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/route-codes")
    protected List<CodeableConcept> intendedRoute;

    /**
     * The price of the medication.
     */
    @Child(name = "cost", type = {}, order=13, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="The pricing of the medication", formalDefinition="The price of the medication." )
    protected List<MedicationKnowledgeCostComponent> cost;

    /**
     * The program under which the medication is reviewed.
     */
    @Child(name = "monitoringProgram", type = {}, order=14, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Program under which a medication is reviewed", formalDefinition="The program under which the medication is reviewed." )
    protected List<MedicationKnowledgeMonitoringProgramComponent> monitoringProgram;

    /**
     * Guidelines for the administration of the medication.
     */
    @Child(name = "administrationGuidelines", type = {}, order=15, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Gudelines for administration of the medication", formalDefinition="Guidelines for the administration of the medication." )
    protected List<MedicationKnowledgeAdministrationGuidelinesComponent> administrationGuidelines;

    /**
     * Categorization of the medication within a formulary or classification system.
     */
    @Child(name = "medicineClassification", type = {}, order=16, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Categorization of the medication within a formulary or classification system", formalDefinition="Categorization of the medication within a formulary or classification system." )
    protected List<MedicationKnowledgeMedicineClassificationComponent> medicineClassification;

    /**
     * Information that only applies to packages (not products).
     */
    @Child(name = "packaging", type = {}, order=17, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Details about packaged medications", formalDefinition="Information that only applies to packages (not products)." )
    protected MedicationKnowledgePackagingComponent packaging;

    /**
     * Specifies descriptive properties of the medicine, such as color, shape, imprints, etc.
     */
    @Child(name = "drugCharacteristic", type = {}, order=18, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Specifies descriptive properties of the medicine", formalDefinition="Specifies descriptive properties of the medicine, such as color, shape, imprints, etc." )
    protected List<MedicationKnowledgeDrugCharacteristicComponent> drugCharacteristic;

    /**
     * Potential clinical issue with or between medication(s) (for example, drug-drug interaction, drug-disease contraindication, drug-allergy interaction, etc).
     */
    @Child(name = "contraindication", type = {DetectedIssue.class}, order=19, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Potential clinical issue with or between medication(s)", formalDefinition="Potential clinical issue with or between medication(s) (for example, drug-drug interaction, drug-disease contraindication, drug-allergy interaction, etc)." )
    protected List<Reference> contraindication;
    /**
     * The actual objects that are the target of the reference (Potential clinical issue with or between medication(s) (for example, drug-drug interaction, drug-disease contraindication, drug-allergy interaction, etc).)
     */
    protected List<DetectedIssue> contraindicationTarget;


    /**
     * Regulatory information about a medication.
     */
    @Child(name = "regulatory", type = {}, order=20, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Regulatory information about a medication", formalDefinition="Regulatory information about a medication." )
    protected List<MedicationKnowledgeRegulatoryComponent> regulatory;

    /**
     * The time course of drug absorption, distribution, metabolism and excretion of a medication from the body.
     */
    @Child(name = "kinetics", type = {}, order=21, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="The time course of drug absorption, distribution, metabolism and excretion of a medication from the body", formalDefinition="The time course of drug absorption, distribution, metabolism and excretion of a medication from the body." )
    protected List<MedicationKnowledgeKineticsComponent> kinetics;

    private static final long serialVersionUID = -1230067857L;

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
     * @return {@link #doseForm} (Describes the form of the item.  Powder; tablets; capsule.)
     */
    public CodeableConcept getDoseForm() { 
      if (this.doseForm == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationKnowledge.doseForm");
        else if (Configuration.doAutoCreate())
          this.doseForm = new CodeableConcept(); // cc
      return this.doseForm;
    }

    public boolean hasDoseForm() { 
      return this.doseForm != null && !this.doseForm.isEmpty();
    }

    /**
     * @param value {@link #doseForm} (Describes the form of the item.  Powder; tablets; capsule.)
     */
    public MedicationKnowledge setDoseForm(CodeableConcept value) { 
      this.doseForm = value;
      return this;
    }

    /**
     * @return {@link #amount} (Specific amount of the drug in the packaged product.  For example, when specifying a product that has the same strength (For example, Insulin glargine 100 unit per mL solution for injection), this attribute provides additional clarification of the package amount (For example, 3 mL, 10mL, etc).)
     */
    public Quantity getAmount() { 
      if (this.amount == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationKnowledge.amount");
        else if (Configuration.doAutoCreate())
          this.amount = new Quantity(); // cc
      return this.amount;
    }

    public boolean hasAmount() { 
      return this.amount != null && !this.amount.isEmpty();
    }

    /**
     * @param value {@link #amount} (Specific amount of the drug in the packaged product.  For example, when specifying a product that has the same strength (For example, Insulin glargine 100 unit per mL solution for injection), this attribute provides additional clarification of the package amount (For example, 3 mL, 10mL, etc).)
     */
    public MedicationKnowledge setAmount(Quantity value) { 
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
    public List<MedicationKnowledgeRelatedMedicationKnowledgeComponent> getRelatedMedicationKnowledge() { 
      if (this.relatedMedicationKnowledge == null)
        this.relatedMedicationKnowledge = new ArrayList<MedicationKnowledgeRelatedMedicationKnowledgeComponent>();
      return this.relatedMedicationKnowledge;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicationKnowledge setRelatedMedicationKnowledge(List<MedicationKnowledgeRelatedMedicationKnowledgeComponent> theRelatedMedicationKnowledge) { 
      this.relatedMedicationKnowledge = theRelatedMedicationKnowledge;
      return this;
    }

    public boolean hasRelatedMedicationKnowledge() { 
      if (this.relatedMedicationKnowledge == null)
        return false;
      for (MedicationKnowledgeRelatedMedicationKnowledgeComponent item : this.relatedMedicationKnowledge)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public MedicationKnowledgeRelatedMedicationKnowledgeComponent addRelatedMedicationKnowledge() { //3
      MedicationKnowledgeRelatedMedicationKnowledgeComponent t = new MedicationKnowledgeRelatedMedicationKnowledgeComponent();
      if (this.relatedMedicationKnowledge == null)
        this.relatedMedicationKnowledge = new ArrayList<MedicationKnowledgeRelatedMedicationKnowledgeComponent>();
      this.relatedMedicationKnowledge.add(t);
      return t;
    }

    public MedicationKnowledge addRelatedMedicationKnowledge(MedicationKnowledgeRelatedMedicationKnowledgeComponent t) { //3
      if (t == null)
        return this;
      if (this.relatedMedicationKnowledge == null)
        this.relatedMedicationKnowledge = new ArrayList<MedicationKnowledgeRelatedMedicationKnowledgeComponent>();
      this.relatedMedicationKnowledge.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #relatedMedicationKnowledge}, creating it if it does not already exist
     */
    public MedicationKnowledgeRelatedMedicationKnowledgeComponent getRelatedMedicationKnowledgeFirstRep() { 
      if (getRelatedMedicationKnowledge().isEmpty()) {
        addRelatedMedicationKnowledge();
      }
      return getRelatedMedicationKnowledge().get(0);
    }

    /**
     * @return {@link #associatedMedication} (Associated or related medications.  For example, if the medication is a branded product (e.g. Crestor), this is the Therapeutic Moeity (e.g. Rosuvastatin) or if this is a generic medication (e.g. Rosuvastatin), this would link to a branded product (e.g. Crestor).)
     */
    public List<Reference> getAssociatedMedication() { 
      if (this.associatedMedication == null)
        this.associatedMedication = new ArrayList<Reference>();
      return this.associatedMedication;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicationKnowledge setAssociatedMedication(List<Reference> theAssociatedMedication) { 
      this.associatedMedication = theAssociatedMedication;
      return this;
    }

    public boolean hasAssociatedMedication() { 
      if (this.associatedMedication == null)
        return false;
      for (Reference item : this.associatedMedication)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addAssociatedMedication() { //3
      Reference t = new Reference();
      if (this.associatedMedication == null)
        this.associatedMedication = new ArrayList<Reference>();
      this.associatedMedication.add(t);
      return t;
    }

    public MedicationKnowledge addAssociatedMedication(Reference t) { //3
      if (t == null)
        return this;
      if (this.associatedMedication == null)
        this.associatedMedication = new ArrayList<Reference>();
      this.associatedMedication.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #associatedMedication}, creating it if it does not already exist
     */
    public Reference getAssociatedMedicationFirstRep() { 
      if (getAssociatedMedication().isEmpty()) {
        addAssociatedMedication();
      }
      return getAssociatedMedication().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Medication> getAssociatedMedicationTarget() { 
      if (this.associatedMedicationTarget == null)
        this.associatedMedicationTarget = new ArrayList<Medication>();
      return this.associatedMedicationTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public Medication addAssociatedMedicationTarget() { 
      Medication r = new Medication();
      if (this.associatedMedicationTarget == null)
        this.associatedMedicationTarget = new ArrayList<Medication>();
      this.associatedMedicationTarget.add(r);
      return r;
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

    /**
     * @return {@link #regulatory} (Regulatory information about a medication.)
     */
    public List<MedicationKnowledgeRegulatoryComponent> getRegulatory() { 
      if (this.regulatory == null)
        this.regulatory = new ArrayList<MedicationKnowledgeRegulatoryComponent>();
      return this.regulatory;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicationKnowledge setRegulatory(List<MedicationKnowledgeRegulatoryComponent> theRegulatory) { 
      this.regulatory = theRegulatory;
      return this;
    }

    public boolean hasRegulatory() { 
      if (this.regulatory == null)
        return false;
      for (MedicationKnowledgeRegulatoryComponent item : this.regulatory)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public MedicationKnowledgeRegulatoryComponent addRegulatory() { //3
      MedicationKnowledgeRegulatoryComponent t = new MedicationKnowledgeRegulatoryComponent();
      if (this.regulatory == null)
        this.regulatory = new ArrayList<MedicationKnowledgeRegulatoryComponent>();
      this.regulatory.add(t);
      return t;
    }

    public MedicationKnowledge addRegulatory(MedicationKnowledgeRegulatoryComponent t) { //3
      if (t == null)
        return this;
      if (this.regulatory == null)
        this.regulatory = new ArrayList<MedicationKnowledgeRegulatoryComponent>();
      this.regulatory.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #regulatory}, creating it if it does not already exist
     */
    public MedicationKnowledgeRegulatoryComponent getRegulatoryFirstRep() { 
      if (getRegulatory().isEmpty()) {
        addRegulatory();
      }
      return getRegulatory().get(0);
    }

    /**
     * @return {@link #kinetics} (The time course of drug absorption, distribution, metabolism and excretion of a medication from the body.)
     */
    public List<MedicationKnowledgeKineticsComponent> getKinetics() { 
      if (this.kinetics == null)
        this.kinetics = new ArrayList<MedicationKnowledgeKineticsComponent>();
      return this.kinetics;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicationKnowledge setKinetics(List<MedicationKnowledgeKineticsComponent> theKinetics) { 
      this.kinetics = theKinetics;
      return this;
    }

    public boolean hasKinetics() { 
      if (this.kinetics == null)
        return false;
      for (MedicationKnowledgeKineticsComponent item : this.kinetics)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public MedicationKnowledgeKineticsComponent addKinetics() { //3
      MedicationKnowledgeKineticsComponent t = new MedicationKnowledgeKineticsComponent();
      if (this.kinetics == null)
        this.kinetics = new ArrayList<MedicationKnowledgeKineticsComponent>();
      this.kinetics.add(t);
      return t;
    }

    public MedicationKnowledge addKinetics(MedicationKnowledgeKineticsComponent t) { //3
      if (t == null)
        return this;
      if (this.kinetics == null)
        this.kinetics = new ArrayList<MedicationKnowledgeKineticsComponent>();
      this.kinetics.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #kinetics}, creating it if it does not already exist
     */
    public MedicationKnowledgeKineticsComponent getKineticsFirstRep() { 
      if (getKinetics().isEmpty()) {
        addKinetics();
      }
      return getKinetics().get(0);
    }

      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("code", "CodeableConcept", "A code that specifies this medication, or a textual description if no code is available. Usage note: This could be a standard medication code such as a code from RxNorm, SNOMED CT, IDMP etc. It could also be a national or local formulary code, optionally with translations to other code systems.", 0, 1, code));
        children.add(new Property("status", "code", "A code to indicate if the medication is in active use.", 0, 1, status));
        children.add(new Property("manufacturer", "Reference(Organization)", "Describes the details of the manufacturer of the medication product.  This is not intended to represent the distributor of a medication product.", 0, 1, manufacturer));
        children.add(new Property("doseForm", "CodeableConcept", "Describes the form of the item.  Powder; tablets; capsule.", 0, 1, doseForm));
        children.add(new Property("amount", "SimpleQuantity", "Specific amount of the drug in the packaged product.  For example, when specifying a product that has the same strength (For example, Insulin glargine 100 unit per mL solution for injection), this attribute provides additional clarification of the package amount (For example, 3 mL, 10mL, etc).", 0, 1, amount));
        children.add(new Property("synonym", "string", "Additional names for a medication, for example, the name(s) given to a medication in different countries.  For example, acetaminophen and paracetamol or salbutamol and albuterol.", 0, java.lang.Integer.MAX_VALUE, synonym));
        children.add(new Property("relatedMedicationKnowledge", "", "Associated or related knowledge about a medication.", 0, java.lang.Integer.MAX_VALUE, relatedMedicationKnowledge));
        children.add(new Property("associatedMedication", "Reference(Medication)", "Associated or related medications.  For example, if the medication is a branded product (e.g. Crestor), this is the Therapeutic Moeity (e.g. Rosuvastatin) or if this is a generic medication (e.g. Rosuvastatin), this would link to a branded product (e.g. Crestor).", 0, java.lang.Integer.MAX_VALUE, associatedMedication));
        children.add(new Property("productType", "CodeableConcept", "Category of the medication or product (e.g. branded product, therapeutic moeity, generic product, innovator product, etc).", 0, java.lang.Integer.MAX_VALUE, productType));
        children.add(new Property("monograph", "", "Associated documentation about the medication.", 0, java.lang.Integer.MAX_VALUE, monograph));
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
        children.add(new Property("regulatory", "", "Regulatory information about a medication.", 0, java.lang.Integer.MAX_VALUE, regulatory));
        children.add(new Property("kinetics", "", "The time course of drug absorption, distribution, metabolism and excretion of a medication from the body.", 0, java.lang.Integer.MAX_VALUE, kinetics));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case 3059181: /*code*/  return new Property("code", "CodeableConcept", "A code that specifies this medication, or a textual description if no code is available. Usage note: This could be a standard medication code such as a code from RxNorm, SNOMED CT, IDMP etc. It could also be a national or local formulary code, optionally with translations to other code systems.", 0, 1, code);
        case -892481550: /*status*/  return new Property("status", "code", "A code to indicate if the medication is in active use.", 0, 1, status);
        case -1969347631: /*manufacturer*/  return new Property("manufacturer", "Reference(Organization)", "Describes the details of the manufacturer of the medication product.  This is not intended to represent the distributor of a medication product.", 0, 1, manufacturer);
        case 1303858817: /*doseForm*/  return new Property("doseForm", "CodeableConcept", "Describes the form of the item.  Powder; tablets; capsule.", 0, 1, doseForm);
        case -1413853096: /*amount*/  return new Property("amount", "SimpleQuantity", "Specific amount of the drug in the packaged product.  For example, when specifying a product that has the same strength (For example, Insulin glargine 100 unit per mL solution for injection), this attribute provides additional clarification of the package amount (For example, 3 mL, 10mL, etc).", 0, 1, amount);
        case -1742128133: /*synonym*/  return new Property("synonym", "string", "Additional names for a medication, for example, the name(s) given to a medication in different countries.  For example, acetaminophen and paracetamol or salbutamol and albuterol.", 0, java.lang.Integer.MAX_VALUE, synonym);
        case 723067972: /*relatedMedicationKnowledge*/  return new Property("relatedMedicationKnowledge", "", "Associated or related knowledge about a medication.", 0, java.lang.Integer.MAX_VALUE, relatedMedicationKnowledge);
        case 1312779381: /*associatedMedication*/  return new Property("associatedMedication", "Reference(Medication)", "Associated or related medications.  For example, if the medication is a branded product (e.g. Crestor), this is the Therapeutic Moeity (e.g. Rosuvastatin) or if this is a generic medication (e.g. Rosuvastatin), this would link to a branded product (e.g. Crestor).", 0, java.lang.Integer.MAX_VALUE, associatedMedication);
        case -1491615543: /*productType*/  return new Property("productType", "CodeableConcept", "Category of the medication or product (e.g. branded product, therapeutic moeity, generic product, innovator product, etc).", 0, java.lang.Integer.MAX_VALUE, productType);
        case -1442980789: /*monograph*/  return new Property("monograph", "", "Associated documentation about the medication.", 0, java.lang.Integer.MAX_VALUE, monograph);
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
        case -27327848: /*regulatory*/  return new Property("regulatory", "", "Regulatory information about a medication.", 0, java.lang.Integer.MAX_VALUE, regulatory);
        case -553207110: /*kinetics*/  return new Property("kinetics", "", "The time course of drug absorption, distribution, metabolism and excretion of a medication from the body.", 0, java.lang.Integer.MAX_VALUE, kinetics);
        default: return super.getNamedProperty(_hash, _name, _checkValid);
        }

      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3059181: /*code*/ return this.code == null ? new Base[0] : new Base[] {this.code}; // CodeableConcept
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<MedicationKnowledgeStatus>
        case -1969347631: /*manufacturer*/ return this.manufacturer == null ? new Base[0] : new Base[] {this.manufacturer}; // Reference
        case 1303858817: /*doseForm*/ return this.doseForm == null ? new Base[0] : new Base[] {this.doseForm}; // CodeableConcept
        case -1413853096: /*amount*/ return this.amount == null ? new Base[0] : new Base[] {this.amount}; // Quantity
        case -1742128133: /*synonym*/ return this.synonym == null ? new Base[0] : this.synonym.toArray(new Base[this.synonym.size()]); // StringType
        case 723067972: /*relatedMedicationKnowledge*/ return this.relatedMedicationKnowledge == null ? new Base[0] : this.relatedMedicationKnowledge.toArray(new Base[this.relatedMedicationKnowledge.size()]); // MedicationKnowledgeRelatedMedicationKnowledgeComponent
        case 1312779381: /*associatedMedication*/ return this.associatedMedication == null ? new Base[0] : this.associatedMedication.toArray(new Base[this.associatedMedication.size()]); // Reference
        case -1491615543: /*productType*/ return this.productType == null ? new Base[0] : this.productType.toArray(new Base[this.productType.size()]); // CodeableConcept
        case -1442980789: /*monograph*/ return this.monograph == null ? new Base[0] : this.monograph.toArray(new Base[this.monograph.size()]); // MedicationKnowledgeMonographComponent
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
        case -27327848: /*regulatory*/ return this.regulatory == null ? new Base[0] : this.regulatory.toArray(new Base[this.regulatory.size()]); // MedicationKnowledgeRegulatoryComponent
        case -553207110: /*kinetics*/ return this.kinetics == null ? new Base[0] : this.kinetics.toArray(new Base[this.kinetics.size()]); // MedicationKnowledgeKineticsComponent
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
        case 1303858817: // doseForm
          this.doseForm = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1413853096: // amount
          this.amount = castToQuantity(value); // Quantity
          return value;
        case -1742128133: // synonym
          this.getSynonym().add(castToString(value)); // StringType
          return value;
        case 723067972: // relatedMedicationKnowledge
          this.getRelatedMedicationKnowledge().add((MedicationKnowledgeRelatedMedicationKnowledgeComponent) value); // MedicationKnowledgeRelatedMedicationKnowledgeComponent
          return value;
        case 1312779381: // associatedMedication
          this.getAssociatedMedication().add(castToReference(value)); // Reference
          return value;
        case -1491615543: // productType
          this.getProductType().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -1442980789: // monograph
          this.getMonograph().add((MedicationKnowledgeMonographComponent) value); // MedicationKnowledgeMonographComponent
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
        case -27327848: // regulatory
          this.getRegulatory().add((MedicationKnowledgeRegulatoryComponent) value); // MedicationKnowledgeRegulatoryComponent
          return value;
        case -553207110: // kinetics
          this.getKinetics().add((MedicationKnowledgeKineticsComponent) value); // MedicationKnowledgeKineticsComponent
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
        } else if (name.equals("doseForm")) {
          this.doseForm = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("amount")) {
          this.amount = castToQuantity(value); // Quantity
        } else if (name.equals("synonym")) {
          this.getSynonym().add(castToString(value));
        } else if (name.equals("relatedMedicationKnowledge")) {
          this.getRelatedMedicationKnowledge().add((MedicationKnowledgeRelatedMedicationKnowledgeComponent) value);
        } else if (name.equals("associatedMedication")) {
          this.getAssociatedMedication().add(castToReference(value));
        } else if (name.equals("productType")) {
          this.getProductType().add(castToCodeableConcept(value));
        } else if (name.equals("monograph")) {
          this.getMonograph().add((MedicationKnowledgeMonographComponent) value);
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
        } else if (name.equals("regulatory")) {
          this.getRegulatory().add((MedicationKnowledgeRegulatoryComponent) value);
        } else if (name.equals("kinetics")) {
          this.getKinetics().add((MedicationKnowledgeKineticsComponent) value);
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
        case 1303858817:  return getDoseForm(); 
        case -1413853096:  return getAmount(); 
        case -1742128133:  return addSynonymElement();
        case 723067972:  return addRelatedMedicationKnowledge(); 
        case 1312779381:  return addAssociatedMedication(); 
        case -1491615543:  return addProductType(); 
        case -1442980789:  return addMonograph(); 
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
        case -27327848:  return addRegulatory(); 
        case -553207110:  return addKinetics(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3059181: /*code*/ return new String[] {"CodeableConcept"};
        case -892481550: /*status*/ return new String[] {"code"};
        case -1969347631: /*manufacturer*/ return new String[] {"Reference"};
        case 1303858817: /*doseForm*/ return new String[] {"CodeableConcept"};
        case -1413853096: /*amount*/ return new String[] {"SimpleQuantity"};
        case -1742128133: /*synonym*/ return new String[] {"string"};
        case 723067972: /*relatedMedicationKnowledge*/ return new String[] {};
        case 1312779381: /*associatedMedication*/ return new String[] {"Reference"};
        case -1491615543: /*productType*/ return new String[] {"CodeableConcept"};
        case -1442980789: /*monograph*/ return new String[] {};
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
        case -27327848: /*regulatory*/ return new String[] {};
        case -553207110: /*kinetics*/ return new String[] {};
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
        else if (name.equals("doseForm")) {
          this.doseForm = new CodeableConcept();
          return this.doseForm;
        }
        else if (name.equals("amount")) {
          this.amount = new Quantity();
          return this.amount;
        }
        else if (name.equals("synonym")) {
          throw new FHIRException("Cannot call addChild on a primitive type MedicationKnowledge.synonym");
        }
        else if (name.equals("relatedMedicationKnowledge")) {
          return addRelatedMedicationKnowledge();
        }
        else if (name.equals("associatedMedication")) {
          return addAssociatedMedication();
        }
        else if (name.equals("productType")) {
          return addProductType();
        }
        else if (name.equals("monograph")) {
          return addMonograph();
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
        else if (name.equals("regulatory")) {
          return addRegulatory();
        }
        else if (name.equals("kinetics")) {
          return addKinetics();
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
        dst.doseForm = doseForm == null ? null : doseForm.copy();
        dst.amount = amount == null ? null : amount.copy();
        if (synonym != null) {
          dst.synonym = new ArrayList<StringType>();
          for (StringType i : synonym)
            dst.synonym.add(i.copy());
        };
        if (relatedMedicationKnowledge != null) {
          dst.relatedMedicationKnowledge = new ArrayList<MedicationKnowledgeRelatedMedicationKnowledgeComponent>();
          for (MedicationKnowledgeRelatedMedicationKnowledgeComponent i : relatedMedicationKnowledge)
            dst.relatedMedicationKnowledge.add(i.copy());
        };
        if (associatedMedication != null) {
          dst.associatedMedication = new ArrayList<Reference>();
          for (Reference i : associatedMedication)
            dst.associatedMedication.add(i.copy());
        };
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
        if (regulatory != null) {
          dst.regulatory = new ArrayList<MedicationKnowledgeRegulatoryComponent>();
          for (MedicationKnowledgeRegulatoryComponent i : regulatory)
            dst.regulatory.add(i.copy());
        };
        if (kinetics != null) {
          dst.kinetics = new ArrayList<MedicationKnowledgeKineticsComponent>();
          for (MedicationKnowledgeKineticsComponent i : kinetics)
            dst.kinetics.add(i.copy());
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
           && compareDeep(doseForm, o.doseForm, true) && compareDeep(amount, o.amount, true) && compareDeep(synonym, o.synonym, true)
           && compareDeep(relatedMedicationKnowledge, o.relatedMedicationKnowledge, true) && compareDeep(associatedMedication, o.associatedMedication, true)
           && compareDeep(productType, o.productType, true) && compareDeep(monograph, o.monograph, true) && compareDeep(ingredient, o.ingredient, true)
           && compareDeep(preparationInstruction, o.preparationInstruction, true) && compareDeep(intendedRoute, o.intendedRoute, true)
           && compareDeep(cost, o.cost, true) && compareDeep(monitoringProgram, o.monitoringProgram, true)
           && compareDeep(administrationGuidelines, o.administrationGuidelines, true) && compareDeep(medicineClassification, o.medicineClassification, true)
           && compareDeep(packaging, o.packaging, true) && compareDeep(drugCharacteristic, o.drugCharacteristic, true)
           && compareDeep(contraindication, o.contraindication, true) && compareDeep(regulatory, o.regulatory, true)
           && compareDeep(kinetics, o.kinetics, true);
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
          , doseForm, amount, synonym, relatedMedicationKnowledge, associatedMedication, productType
          , monograph, ingredient, preparationInstruction, intendedRoute, cost, monitoringProgram
          , administrationGuidelines, medicineClassification, packaging, drugCharacteristic, contraindication
          , regulatory, kinetics);
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
  @SearchParamDefinition(name="ingredient", path="(MedicationKnowledge.ingredient.item as Reference)", description="Medication(s) or substance(s) contained in the medication", type="reference", target={Substance.class } )
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
   * Search parameter: <b>doseform</b>
   * <p>
   * Description: <b>powder | tablets | capsule +</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MedicationKnowledge.doseForm</b><br>
   * </p>
   */
  @SearchParamDefinition(name="doseform", path="MedicationKnowledge.doseForm", description="powder | tablets | capsule +", type="token" )
  public static final String SP_DOSEFORM = "doseform";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>doseform</b>
   * <p>
   * Description: <b>powder | tablets | capsule +</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MedicationKnowledge.doseForm</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam DOSEFORM = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_DOSEFORM);

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
  @SearchParamDefinition(name="ingredient-code", path="(MedicationKnowledge.ingredient.item as CodeableConcept)", description="Medication(s) or substance(s) contained in the medication", type="token" )
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
   * Search parameter: <b>monograph</b>
   * <p>
   * Description: <b>Associated documentation about the medication</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MedicationKnowledge.monograph.source</b><br>
   * </p>
   */
  @SearchParamDefinition(name="monograph", path="MedicationKnowledge.monograph.source", description="Associated documentation about the medication", type="reference", target={DocumentReference.class, Media.class } )
  public static final String SP_MONOGRAPH = "monograph";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>monograph</b>
   * <p>
   * Description: <b>Associated documentation about the medication</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MedicationKnowledge.monograph.source</b><br>
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

