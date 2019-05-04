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

import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ChildOrder;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Block;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.exceptions.FHIRException;
/**
 * Indication for the Medicinal Product.
 */
@ResourceDef(name="MedicinalProductIndication", profile="http://hl7.org/fhir/StructureDefinition/MedicinalProductIndication")
public class MedicinalProductIndication extends DomainResource {

    @Block()
    public static class MedicinalProductIndicationOtherTherapyComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The type of relationship between the medicinal product indication or contraindication and another therapy.
         */
        @Child(name = "therapyRelationshipType", type = {CodeableConcept.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The type of relationship between the medicinal product indication or contraindication and another therapy", formalDefinition="The type of relationship between the medicinal product indication or contraindication and another therapy." )
        protected CodeableConcept therapyRelationshipType;

        /**
         * Reference to a specific medication (active substance, medicinal product or class of products) as part of an indication or contraindication.
         */
        @Child(name = "medication", type = {CodeableConcept.class, MedicinalProduct.class, Medication.class, Substance.class, SubstanceSpecification.class}, order=2, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Reference to a specific medication (active substance, medicinal product or class of products) as part of an indication or contraindication", formalDefinition="Reference to a specific medication (active substance, medicinal product or class of products) as part of an indication or contraindication." )
        protected Type medication;

        private static final long serialVersionUID = 1438478115L;

    /**
     * Constructor
     */
      public MedicinalProductIndicationOtherTherapyComponent() {
        super();
      }

    /**
     * Constructor
     */
      public MedicinalProductIndicationOtherTherapyComponent(CodeableConcept therapyRelationshipType, Type medication) {
        super();
        this.therapyRelationshipType = therapyRelationshipType;
        this.medication = medication;
      }

        /**
         * @return {@link #therapyRelationshipType} (The type of relationship between the medicinal product indication or contraindication and another therapy.)
         */
        public CodeableConcept getTherapyRelationshipType() { 
          if (this.therapyRelationshipType == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicinalProductIndicationOtherTherapyComponent.therapyRelationshipType");
            else if (Configuration.doAutoCreate())
              this.therapyRelationshipType = new CodeableConcept(); // cc
          return this.therapyRelationshipType;
        }

        public boolean hasTherapyRelationshipType() { 
          return this.therapyRelationshipType != null && !this.therapyRelationshipType.isEmpty();
        }

        /**
         * @param value {@link #therapyRelationshipType} (The type of relationship between the medicinal product indication or contraindication and another therapy.)
         */
        public MedicinalProductIndicationOtherTherapyComponent setTherapyRelationshipType(CodeableConcept value) { 
          this.therapyRelationshipType = value;
          return this;
        }

        /**
         * @return {@link #medication} (Reference to a specific medication (active substance, medicinal product or class of products) as part of an indication or contraindication.)
         */
        public Type getMedication() { 
          return this.medication;
        }

        /**
         * @return {@link #medication} (Reference to a specific medication (active substance, medicinal product or class of products) as part of an indication or contraindication.)
         */
        public CodeableConcept getMedicationCodeableConcept() throws FHIRException { 
          if (this.medication == null)
            this.medication = new CodeableConcept();
          if (!(this.medication instanceof CodeableConcept))
            throw new FHIRException("Type mismatch: the type CodeableConcept was expected, but "+this.medication.getClass().getName()+" was encountered");
          return (CodeableConcept) this.medication;
        }

        public boolean hasMedicationCodeableConcept() { 
          return this != null && this.medication instanceof CodeableConcept;
        }

        /**
         * @return {@link #medication} (Reference to a specific medication (active substance, medicinal product or class of products) as part of an indication or contraindication.)
         */
        public Reference getMedicationReference() throws FHIRException { 
          if (this.medication == null)
            this.medication = new Reference();
          if (!(this.medication instanceof Reference))
            throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.medication.getClass().getName()+" was encountered");
          return (Reference) this.medication;
        }

        public boolean hasMedicationReference() { 
          return this != null && this.medication instanceof Reference;
        }

        public boolean hasMedication() { 
          return this.medication != null && !this.medication.isEmpty();
        }

        /**
         * @param value {@link #medication} (Reference to a specific medication (active substance, medicinal product or class of products) as part of an indication or contraindication.)
         */
        public MedicinalProductIndicationOtherTherapyComponent setMedication(Type value) { 
          if (value != null && !(value instanceof CodeableConcept || value instanceof Reference))
            throw new Error("Not the right type for MedicinalProductIndication.otherTherapy.medication[x]: "+value.fhirType());
          this.medication = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("therapyRelationshipType", "CodeableConcept", "The type of relationship between the medicinal product indication or contraindication and another therapy.", 0, 1, therapyRelationshipType));
          children.add(new Property("medication[x]", "CodeableConcept|Reference(MedicinalProduct|Medication|Substance|SubstanceSpecification)", "Reference to a specific medication (active substance, medicinal product or class of products) as part of an indication or contraindication.", 0, 1, medication));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -551658469: /*therapyRelationshipType*/  return new Property("therapyRelationshipType", "CodeableConcept", "The type of relationship between the medicinal product indication or contraindication and another therapy.", 0, 1, therapyRelationshipType);
          case 1458402129: /*medication[x]*/  return new Property("medication[x]", "CodeableConcept|Reference(MedicinalProduct|Medication|Substance|SubstanceSpecification)", "Reference to a specific medication (active substance, medicinal product or class of products) as part of an indication or contraindication.", 0, 1, medication);
          case 1998965455: /*medication*/  return new Property("medication[x]", "CodeableConcept|Reference(MedicinalProduct|Medication|Substance|SubstanceSpecification)", "Reference to a specific medication (active substance, medicinal product or class of products) as part of an indication or contraindication.", 0, 1, medication);
          case -209845038: /*medicationCodeableConcept*/  return new Property("medication[x]", "CodeableConcept|Reference(MedicinalProduct|Medication|Substance|SubstanceSpecification)", "Reference to a specific medication (active substance, medicinal product or class of products) as part of an indication or contraindication.", 0, 1, medication);
          case 2104315196: /*medicationReference*/  return new Property("medication[x]", "CodeableConcept|Reference(MedicinalProduct|Medication|Substance|SubstanceSpecification)", "Reference to a specific medication (active substance, medicinal product or class of products) as part of an indication or contraindication.", 0, 1, medication);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -551658469: /*therapyRelationshipType*/ return this.therapyRelationshipType == null ? new Base[0] : new Base[] {this.therapyRelationshipType}; // CodeableConcept
        case 1998965455: /*medication*/ return this.medication == null ? new Base[0] : new Base[] {this.medication}; // Type
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -551658469: // therapyRelationshipType
          this.therapyRelationshipType = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 1998965455: // medication
          this.medication = castToType(value); // Type
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("therapyRelationshipType")) {
          this.therapyRelationshipType = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("medication[x]")) {
          this.medication = castToType(value); // Type
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -551658469:  return getTherapyRelationshipType(); 
        case 1458402129:  return getMedication(); 
        case 1998965455:  return getMedication(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -551658469: /*therapyRelationshipType*/ return new String[] {"CodeableConcept"};
        case 1998965455: /*medication*/ return new String[] {"CodeableConcept", "Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("therapyRelationshipType")) {
          this.therapyRelationshipType = new CodeableConcept();
          return this.therapyRelationshipType;
        }
        else if (name.equals("medicationCodeableConcept")) {
          this.medication = new CodeableConcept();
          return this.medication;
        }
        else if (name.equals("medicationReference")) {
          this.medication = new Reference();
          return this.medication;
        }
        else
          return super.addChild(name);
      }

      public MedicinalProductIndicationOtherTherapyComponent copy() {
        MedicinalProductIndicationOtherTherapyComponent dst = new MedicinalProductIndicationOtherTherapyComponent();
        copyValues(dst);
        dst.therapyRelationshipType = therapyRelationshipType == null ? null : therapyRelationshipType.copy();
        dst.medication = medication == null ? null : medication.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof MedicinalProductIndicationOtherTherapyComponent))
          return false;
        MedicinalProductIndicationOtherTherapyComponent o = (MedicinalProductIndicationOtherTherapyComponent) other_;
        return compareDeep(therapyRelationshipType, o.therapyRelationshipType, true) && compareDeep(medication, o.medication, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof MedicinalProductIndicationOtherTherapyComponent))
          return false;
        MedicinalProductIndicationOtherTherapyComponent o = (MedicinalProductIndicationOtherTherapyComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(therapyRelationshipType, medication
          );
      }

  public String fhirType() {
    return "MedicinalProductIndication.otherTherapy";

  }

  }

    /**
     * The medication for which this is an indication.
     */
    @Child(name = "subject", type = {MedicinalProduct.class, Medication.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="The medication for which this is an indication", formalDefinition="The medication for which this is an indication." )
    protected List<Reference> subject;
    /**
     * The actual objects that are the target of the reference (The medication for which this is an indication.)
     */
    protected List<Resource> subjectTarget;


    /**
     * The disease, symptom or procedure that is the indication for treatment.
     */
    @Child(name = "diseaseSymptomProcedure", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The disease, symptom or procedure that is the indication for treatment", formalDefinition="The disease, symptom or procedure that is the indication for treatment." )
    protected CodeableConcept diseaseSymptomProcedure;

    /**
     * The status of the disease or symptom for which the indication applies.
     */
    @Child(name = "diseaseStatus", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The status of the disease or symptom for which the indication applies", formalDefinition="The status of the disease or symptom for which the indication applies." )
    protected CodeableConcept diseaseStatus;

    /**
     * Comorbidity (concurrent condition) or co-infection as part of the indication.
     */
    @Child(name = "comorbidity", type = {CodeableConcept.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Comorbidity (concurrent condition) or co-infection as part of the indication", formalDefinition="Comorbidity (concurrent condition) or co-infection as part of the indication." )
    protected List<CodeableConcept> comorbidity;

    /**
     * The intended effect, aim or strategy to be achieved by the indication.
     */
    @Child(name = "intendedEffect", type = {CodeableConcept.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The intended effect, aim or strategy to be achieved by the indication", formalDefinition="The intended effect, aim or strategy to be achieved by the indication." )
    protected CodeableConcept intendedEffect;

    /**
     * Timing or duration information as part of the indication.
     */
    @Child(name = "duration", type = {Quantity.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Timing or duration information as part of the indication", formalDefinition="Timing or duration information as part of the indication." )
    protected Quantity duration;

    /**
     * Information about the use of the medicinal product in relation to other therapies described as part of the indication.
     */
    @Child(name = "otherTherapy", type = {}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Information about the use of the medicinal product in relation to other therapies described as part of the indication", formalDefinition="Information about the use of the medicinal product in relation to other therapies described as part of the indication." )
    protected List<MedicinalProductIndicationOtherTherapyComponent> otherTherapy;

    /**
     * Describe the undesirable effects of the medicinal product.
     */
    @Child(name = "undesirableEffect", type = {MedicinalProductUndesirableEffect.class}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Describe the undesirable effects of the medicinal product", formalDefinition="Describe the undesirable effects of the medicinal product." )
    protected List<Reference> undesirableEffect;
    /**
     * The actual objects that are the target of the reference (Describe the undesirable effects of the medicinal product.)
     */
    protected List<MedicinalProductUndesirableEffect> undesirableEffectTarget;


    /**
     * The population group to which this applies.
     */
    @Child(name = "population", type = {Population.class}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="The population group to which this applies", formalDefinition="The population group to which this applies." )
    protected List<Population> population;

    private static final long serialVersionUID = 1205519664L;

  /**
   * Constructor
   */
    public MedicinalProductIndication() {
      super();
    }

    /**
     * @return {@link #subject} (The medication for which this is an indication.)
     */
    public List<Reference> getSubject() { 
      if (this.subject == null)
        this.subject = new ArrayList<Reference>();
      return this.subject;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicinalProductIndication setSubject(List<Reference> theSubject) { 
      this.subject = theSubject;
      return this;
    }

    public boolean hasSubject() { 
      if (this.subject == null)
        return false;
      for (Reference item : this.subject)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addSubject() { //3
      Reference t = new Reference();
      if (this.subject == null)
        this.subject = new ArrayList<Reference>();
      this.subject.add(t);
      return t;
    }

    public MedicinalProductIndication addSubject(Reference t) { //3
      if (t == null)
        return this;
      if (this.subject == null)
        this.subject = new ArrayList<Reference>();
      this.subject.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #subject}, creating it if it does not already exist
     */
    public Reference getSubjectFirstRep() { 
      if (getSubject().isEmpty()) {
        addSubject();
      }
      return getSubject().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Resource> getSubjectTarget() { 
      if (this.subjectTarget == null)
        this.subjectTarget = new ArrayList<Resource>();
      return this.subjectTarget;
    }

    /**
     * @return {@link #diseaseSymptomProcedure} (The disease, symptom or procedure that is the indication for treatment.)
     */
    public CodeableConcept getDiseaseSymptomProcedure() { 
      if (this.diseaseSymptomProcedure == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicinalProductIndication.diseaseSymptomProcedure");
        else if (Configuration.doAutoCreate())
          this.diseaseSymptomProcedure = new CodeableConcept(); // cc
      return this.diseaseSymptomProcedure;
    }

    public boolean hasDiseaseSymptomProcedure() { 
      return this.diseaseSymptomProcedure != null && !this.diseaseSymptomProcedure.isEmpty();
    }

    /**
     * @param value {@link #diseaseSymptomProcedure} (The disease, symptom or procedure that is the indication for treatment.)
     */
    public MedicinalProductIndication setDiseaseSymptomProcedure(CodeableConcept value) { 
      this.diseaseSymptomProcedure = value;
      return this;
    }

    /**
     * @return {@link #diseaseStatus} (The status of the disease or symptom for which the indication applies.)
     */
    public CodeableConcept getDiseaseStatus() { 
      if (this.diseaseStatus == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicinalProductIndication.diseaseStatus");
        else if (Configuration.doAutoCreate())
          this.diseaseStatus = new CodeableConcept(); // cc
      return this.diseaseStatus;
    }

    public boolean hasDiseaseStatus() { 
      return this.diseaseStatus != null && !this.diseaseStatus.isEmpty();
    }

    /**
     * @param value {@link #diseaseStatus} (The status of the disease or symptom for which the indication applies.)
     */
    public MedicinalProductIndication setDiseaseStatus(CodeableConcept value) { 
      this.diseaseStatus = value;
      return this;
    }

    /**
     * @return {@link #comorbidity} (Comorbidity (concurrent condition) or co-infection as part of the indication.)
     */
    public List<CodeableConcept> getComorbidity() { 
      if (this.comorbidity == null)
        this.comorbidity = new ArrayList<CodeableConcept>();
      return this.comorbidity;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicinalProductIndication setComorbidity(List<CodeableConcept> theComorbidity) { 
      this.comorbidity = theComorbidity;
      return this;
    }

    public boolean hasComorbidity() { 
      if (this.comorbidity == null)
        return false;
      for (CodeableConcept item : this.comorbidity)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addComorbidity() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.comorbidity == null)
        this.comorbidity = new ArrayList<CodeableConcept>();
      this.comorbidity.add(t);
      return t;
    }

    public MedicinalProductIndication addComorbidity(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.comorbidity == null)
        this.comorbidity = new ArrayList<CodeableConcept>();
      this.comorbidity.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #comorbidity}, creating it if it does not already exist
     */
    public CodeableConcept getComorbidityFirstRep() { 
      if (getComorbidity().isEmpty()) {
        addComorbidity();
      }
      return getComorbidity().get(0);
    }

    /**
     * @return {@link #intendedEffect} (The intended effect, aim or strategy to be achieved by the indication.)
     */
    public CodeableConcept getIntendedEffect() { 
      if (this.intendedEffect == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicinalProductIndication.intendedEffect");
        else if (Configuration.doAutoCreate())
          this.intendedEffect = new CodeableConcept(); // cc
      return this.intendedEffect;
    }

    public boolean hasIntendedEffect() { 
      return this.intendedEffect != null && !this.intendedEffect.isEmpty();
    }

    /**
     * @param value {@link #intendedEffect} (The intended effect, aim or strategy to be achieved by the indication.)
     */
    public MedicinalProductIndication setIntendedEffect(CodeableConcept value) { 
      this.intendedEffect = value;
      return this;
    }

    /**
     * @return {@link #duration} (Timing or duration information as part of the indication.)
     */
    public Quantity getDuration() { 
      if (this.duration == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicinalProductIndication.duration");
        else if (Configuration.doAutoCreate())
          this.duration = new Quantity(); // cc
      return this.duration;
    }

    public boolean hasDuration() { 
      return this.duration != null && !this.duration.isEmpty();
    }

    /**
     * @param value {@link #duration} (Timing or duration information as part of the indication.)
     */
    public MedicinalProductIndication setDuration(Quantity value) { 
      this.duration = value;
      return this;
    }

    /**
     * @return {@link #otherTherapy} (Information about the use of the medicinal product in relation to other therapies described as part of the indication.)
     */
    public List<MedicinalProductIndicationOtherTherapyComponent> getOtherTherapy() { 
      if (this.otherTherapy == null)
        this.otherTherapy = new ArrayList<MedicinalProductIndicationOtherTherapyComponent>();
      return this.otherTherapy;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicinalProductIndication setOtherTherapy(List<MedicinalProductIndicationOtherTherapyComponent> theOtherTherapy) { 
      this.otherTherapy = theOtherTherapy;
      return this;
    }

    public boolean hasOtherTherapy() { 
      if (this.otherTherapy == null)
        return false;
      for (MedicinalProductIndicationOtherTherapyComponent item : this.otherTherapy)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public MedicinalProductIndicationOtherTherapyComponent addOtherTherapy() { //3
      MedicinalProductIndicationOtherTherapyComponent t = new MedicinalProductIndicationOtherTherapyComponent();
      if (this.otherTherapy == null)
        this.otherTherapy = new ArrayList<MedicinalProductIndicationOtherTherapyComponent>();
      this.otherTherapy.add(t);
      return t;
    }

    public MedicinalProductIndication addOtherTherapy(MedicinalProductIndicationOtherTherapyComponent t) { //3
      if (t == null)
        return this;
      if (this.otherTherapy == null)
        this.otherTherapy = new ArrayList<MedicinalProductIndicationOtherTherapyComponent>();
      this.otherTherapy.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #otherTherapy}, creating it if it does not already exist
     */
    public MedicinalProductIndicationOtherTherapyComponent getOtherTherapyFirstRep() { 
      if (getOtherTherapy().isEmpty()) {
        addOtherTherapy();
      }
      return getOtherTherapy().get(0);
    }

    /**
     * @return {@link #undesirableEffect} (Describe the undesirable effects of the medicinal product.)
     */
    public List<Reference> getUndesirableEffect() { 
      if (this.undesirableEffect == null)
        this.undesirableEffect = new ArrayList<Reference>();
      return this.undesirableEffect;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicinalProductIndication setUndesirableEffect(List<Reference> theUndesirableEffect) { 
      this.undesirableEffect = theUndesirableEffect;
      return this;
    }

    public boolean hasUndesirableEffect() { 
      if (this.undesirableEffect == null)
        return false;
      for (Reference item : this.undesirableEffect)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addUndesirableEffect() { //3
      Reference t = new Reference();
      if (this.undesirableEffect == null)
        this.undesirableEffect = new ArrayList<Reference>();
      this.undesirableEffect.add(t);
      return t;
    }

    public MedicinalProductIndication addUndesirableEffect(Reference t) { //3
      if (t == null)
        return this;
      if (this.undesirableEffect == null)
        this.undesirableEffect = new ArrayList<Reference>();
      this.undesirableEffect.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #undesirableEffect}, creating it if it does not already exist
     */
    public Reference getUndesirableEffectFirstRep() { 
      if (getUndesirableEffect().isEmpty()) {
        addUndesirableEffect();
      }
      return getUndesirableEffect().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<MedicinalProductUndesirableEffect> getUndesirableEffectTarget() { 
      if (this.undesirableEffectTarget == null)
        this.undesirableEffectTarget = new ArrayList<MedicinalProductUndesirableEffect>();
      return this.undesirableEffectTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public MedicinalProductUndesirableEffect addUndesirableEffectTarget() { 
      MedicinalProductUndesirableEffect r = new MedicinalProductUndesirableEffect();
      if (this.undesirableEffectTarget == null)
        this.undesirableEffectTarget = new ArrayList<MedicinalProductUndesirableEffect>();
      this.undesirableEffectTarget.add(r);
      return r;
    }

    /**
     * @return {@link #population} (The population group to which this applies.)
     */
    public List<Population> getPopulation() { 
      if (this.population == null)
        this.population = new ArrayList<Population>();
      return this.population;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicinalProductIndication setPopulation(List<Population> thePopulation) { 
      this.population = thePopulation;
      return this;
    }

    public boolean hasPopulation() { 
      if (this.population == null)
        return false;
      for (Population item : this.population)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Population addPopulation() { //3
      Population t = new Population();
      if (this.population == null)
        this.population = new ArrayList<Population>();
      this.population.add(t);
      return t;
    }

    public MedicinalProductIndication addPopulation(Population t) { //3
      if (t == null)
        return this;
      if (this.population == null)
        this.population = new ArrayList<Population>();
      this.population.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #population}, creating it if it does not already exist
     */
    public Population getPopulationFirstRep() { 
      if (getPopulation().isEmpty()) {
        addPopulation();
      }
      return getPopulation().get(0);
    }

      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("subject", "Reference(MedicinalProduct|Medication)", "The medication for which this is an indication.", 0, java.lang.Integer.MAX_VALUE, subject));
        children.add(new Property("diseaseSymptomProcedure", "CodeableConcept", "The disease, symptom or procedure that is the indication for treatment.", 0, 1, diseaseSymptomProcedure));
        children.add(new Property("diseaseStatus", "CodeableConcept", "The status of the disease or symptom for which the indication applies.", 0, 1, diseaseStatus));
        children.add(new Property("comorbidity", "CodeableConcept", "Comorbidity (concurrent condition) or co-infection as part of the indication.", 0, java.lang.Integer.MAX_VALUE, comorbidity));
        children.add(new Property("intendedEffect", "CodeableConcept", "The intended effect, aim or strategy to be achieved by the indication.", 0, 1, intendedEffect));
        children.add(new Property("duration", "Quantity", "Timing or duration information as part of the indication.", 0, 1, duration));
        children.add(new Property("otherTherapy", "", "Information about the use of the medicinal product in relation to other therapies described as part of the indication.", 0, java.lang.Integer.MAX_VALUE, otherTherapy));
        children.add(new Property("undesirableEffect", "Reference(MedicinalProductUndesirableEffect)", "Describe the undesirable effects of the medicinal product.", 0, java.lang.Integer.MAX_VALUE, undesirableEffect));
        children.add(new Property("population", "Population", "The population group to which this applies.", 0, java.lang.Integer.MAX_VALUE, population));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case -1867885268: /*subject*/  return new Property("subject", "Reference(MedicinalProduct|Medication)", "The medication for which this is an indication.", 0, java.lang.Integer.MAX_VALUE, subject);
        case -1497395130: /*diseaseSymptomProcedure*/  return new Property("diseaseSymptomProcedure", "CodeableConcept", "The disease, symptom or procedure that is the indication for treatment.", 0, 1, diseaseSymptomProcedure);
        case -505503602: /*diseaseStatus*/  return new Property("diseaseStatus", "CodeableConcept", "The status of the disease or symptom for which the indication applies.", 0, 1, diseaseStatus);
        case -406395211: /*comorbidity*/  return new Property("comorbidity", "CodeableConcept", "Comorbidity (concurrent condition) or co-infection as part of the indication.", 0, java.lang.Integer.MAX_VALUE, comorbidity);
        case 1587112348: /*intendedEffect*/  return new Property("intendedEffect", "CodeableConcept", "The intended effect, aim or strategy to be achieved by the indication.", 0, 1, intendedEffect);
        case -1992012396: /*duration*/  return new Property("duration", "Quantity", "Timing or duration information as part of the indication.", 0, 1, duration);
        case -544509127: /*otherTherapy*/  return new Property("otherTherapy", "", "Information about the use of the medicinal product in relation to other therapies described as part of the indication.", 0, java.lang.Integer.MAX_VALUE, otherTherapy);
        case 444367565: /*undesirableEffect*/  return new Property("undesirableEffect", "Reference(MedicinalProductUndesirableEffect)", "Describe the undesirable effects of the medicinal product.", 0, java.lang.Integer.MAX_VALUE, undesirableEffect);
        case -2023558323: /*population*/  return new Property("population", "Population", "The population group to which this applies.", 0, java.lang.Integer.MAX_VALUE, population);
        default: return super.getNamedProperty(_hash, _name, _checkValid);
        }

      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1867885268: /*subject*/ return this.subject == null ? new Base[0] : this.subject.toArray(new Base[this.subject.size()]); // Reference
        case -1497395130: /*diseaseSymptomProcedure*/ return this.diseaseSymptomProcedure == null ? new Base[0] : new Base[] {this.diseaseSymptomProcedure}; // CodeableConcept
        case -505503602: /*diseaseStatus*/ return this.diseaseStatus == null ? new Base[0] : new Base[] {this.diseaseStatus}; // CodeableConcept
        case -406395211: /*comorbidity*/ return this.comorbidity == null ? new Base[0] : this.comorbidity.toArray(new Base[this.comorbidity.size()]); // CodeableConcept
        case 1587112348: /*intendedEffect*/ return this.intendedEffect == null ? new Base[0] : new Base[] {this.intendedEffect}; // CodeableConcept
        case -1992012396: /*duration*/ return this.duration == null ? new Base[0] : new Base[] {this.duration}; // Quantity
        case -544509127: /*otherTherapy*/ return this.otherTherapy == null ? new Base[0] : this.otherTherapy.toArray(new Base[this.otherTherapy.size()]); // MedicinalProductIndicationOtherTherapyComponent
        case 444367565: /*undesirableEffect*/ return this.undesirableEffect == null ? new Base[0] : this.undesirableEffect.toArray(new Base[this.undesirableEffect.size()]); // Reference
        case -2023558323: /*population*/ return this.population == null ? new Base[0] : this.population.toArray(new Base[this.population.size()]); // Population
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1867885268: // subject
          this.getSubject().add(castToReference(value)); // Reference
          return value;
        case -1497395130: // diseaseSymptomProcedure
          this.diseaseSymptomProcedure = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -505503602: // diseaseStatus
          this.diseaseStatus = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -406395211: // comorbidity
          this.getComorbidity().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 1587112348: // intendedEffect
          this.intendedEffect = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1992012396: // duration
          this.duration = castToQuantity(value); // Quantity
          return value;
        case -544509127: // otherTherapy
          this.getOtherTherapy().add((MedicinalProductIndicationOtherTherapyComponent) value); // MedicinalProductIndicationOtherTherapyComponent
          return value;
        case 444367565: // undesirableEffect
          this.getUndesirableEffect().add(castToReference(value)); // Reference
          return value;
        case -2023558323: // population
          this.getPopulation().add(castToPopulation(value)); // Population
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("subject")) {
          this.getSubject().add(castToReference(value));
        } else if (name.equals("diseaseSymptomProcedure")) {
          this.diseaseSymptomProcedure = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("diseaseStatus")) {
          this.diseaseStatus = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("comorbidity")) {
          this.getComorbidity().add(castToCodeableConcept(value));
        } else if (name.equals("intendedEffect")) {
          this.intendedEffect = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("duration")) {
          this.duration = castToQuantity(value); // Quantity
        } else if (name.equals("otherTherapy")) {
          this.getOtherTherapy().add((MedicinalProductIndicationOtherTherapyComponent) value);
        } else if (name.equals("undesirableEffect")) {
          this.getUndesirableEffect().add(castToReference(value));
        } else if (name.equals("population")) {
          this.getPopulation().add(castToPopulation(value));
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1867885268:  return addSubject(); 
        case -1497395130:  return getDiseaseSymptomProcedure(); 
        case -505503602:  return getDiseaseStatus(); 
        case -406395211:  return addComorbidity(); 
        case 1587112348:  return getIntendedEffect(); 
        case -1992012396:  return getDuration(); 
        case -544509127:  return addOtherTherapy(); 
        case 444367565:  return addUndesirableEffect(); 
        case -2023558323:  return addPopulation(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1867885268: /*subject*/ return new String[] {"Reference"};
        case -1497395130: /*diseaseSymptomProcedure*/ return new String[] {"CodeableConcept"};
        case -505503602: /*diseaseStatus*/ return new String[] {"CodeableConcept"};
        case -406395211: /*comorbidity*/ return new String[] {"CodeableConcept"};
        case 1587112348: /*intendedEffect*/ return new String[] {"CodeableConcept"};
        case -1992012396: /*duration*/ return new String[] {"Quantity"};
        case -544509127: /*otherTherapy*/ return new String[] {};
        case 444367565: /*undesirableEffect*/ return new String[] {"Reference"};
        case -2023558323: /*population*/ return new String[] {"Population"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("subject")) {
          return addSubject();
        }
        else if (name.equals("diseaseSymptomProcedure")) {
          this.diseaseSymptomProcedure = new CodeableConcept();
          return this.diseaseSymptomProcedure;
        }
        else if (name.equals("diseaseStatus")) {
          this.diseaseStatus = new CodeableConcept();
          return this.diseaseStatus;
        }
        else if (name.equals("comorbidity")) {
          return addComorbidity();
        }
        else if (name.equals("intendedEffect")) {
          this.intendedEffect = new CodeableConcept();
          return this.intendedEffect;
        }
        else if (name.equals("duration")) {
          this.duration = new Quantity();
          return this.duration;
        }
        else if (name.equals("otherTherapy")) {
          return addOtherTherapy();
        }
        else if (name.equals("undesirableEffect")) {
          return addUndesirableEffect();
        }
        else if (name.equals("population")) {
          return addPopulation();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "MedicinalProductIndication";

  }

      public MedicinalProductIndication copy() {
        MedicinalProductIndication dst = new MedicinalProductIndication();
        copyValues(dst);
        if (subject != null) {
          dst.subject = new ArrayList<Reference>();
          for (Reference i : subject)
            dst.subject.add(i.copy());
        };
        dst.diseaseSymptomProcedure = diseaseSymptomProcedure == null ? null : diseaseSymptomProcedure.copy();
        dst.diseaseStatus = diseaseStatus == null ? null : diseaseStatus.copy();
        if (comorbidity != null) {
          dst.comorbidity = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : comorbidity)
            dst.comorbidity.add(i.copy());
        };
        dst.intendedEffect = intendedEffect == null ? null : intendedEffect.copy();
        dst.duration = duration == null ? null : duration.copy();
        if (otherTherapy != null) {
          dst.otherTherapy = new ArrayList<MedicinalProductIndicationOtherTherapyComponent>();
          for (MedicinalProductIndicationOtherTherapyComponent i : otherTherapy)
            dst.otherTherapy.add(i.copy());
        };
        if (undesirableEffect != null) {
          dst.undesirableEffect = new ArrayList<Reference>();
          for (Reference i : undesirableEffect)
            dst.undesirableEffect.add(i.copy());
        };
        if (population != null) {
          dst.population = new ArrayList<Population>();
          for (Population i : population)
            dst.population.add(i.copy());
        };
        return dst;
      }

      protected MedicinalProductIndication typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof MedicinalProductIndication))
          return false;
        MedicinalProductIndication o = (MedicinalProductIndication) other_;
        return compareDeep(subject, o.subject, true) && compareDeep(diseaseSymptomProcedure, o.diseaseSymptomProcedure, true)
           && compareDeep(diseaseStatus, o.diseaseStatus, true) && compareDeep(comorbidity, o.comorbidity, true)
           && compareDeep(intendedEffect, o.intendedEffect, true) && compareDeep(duration, o.duration, true)
           && compareDeep(otherTherapy, o.otherTherapy, true) && compareDeep(undesirableEffect, o.undesirableEffect, true)
           && compareDeep(population, o.population, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof MedicinalProductIndication))
          return false;
        MedicinalProductIndication o = (MedicinalProductIndication) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(subject, diseaseSymptomProcedure
          , diseaseStatus, comorbidity, intendedEffect, duration, otherTherapy, undesirableEffect
          , population);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.MedicinalProductIndication;
   }

 /**
   * Search parameter: <b>subject</b>
   * <p>
   * Description: <b>The medication for which this is an indication</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MedicinalProductIndication.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="subject", path="MedicinalProductIndication.subject", description="The medication for which this is an indication", type="reference", target={Medication.class, MedicinalProduct.class } )
  public static final String SP_SUBJECT = "subject";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>subject</b>
   * <p>
   * Description: <b>The medication for which this is an indication</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MedicinalProductIndication.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SUBJECT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SUBJECT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>MedicinalProductIndication:subject</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SUBJECT = new ca.uhn.fhir.model.api.Include("MedicinalProductIndication:subject").toLocked();


}

