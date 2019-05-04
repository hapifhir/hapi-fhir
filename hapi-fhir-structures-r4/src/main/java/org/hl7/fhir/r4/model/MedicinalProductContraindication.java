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
 * The clinical particulars - indications, contraindications etc. of a medicinal product, including for regulatory purposes.
 */
@ResourceDef(name="MedicinalProductContraindication", profile="http://hl7.org/fhir/StructureDefinition/MedicinalProductContraindication")
public class MedicinalProductContraindication extends DomainResource {

    @Block()
    public static class MedicinalProductContraindicationOtherTherapyComponent extends BackboneElement implements IBaseBackboneElement {
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
      public MedicinalProductContraindicationOtherTherapyComponent() {
        super();
      }

    /**
     * Constructor
     */
      public MedicinalProductContraindicationOtherTherapyComponent(CodeableConcept therapyRelationshipType, Type medication) {
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
              throw new Error("Attempt to auto-create MedicinalProductContraindicationOtherTherapyComponent.therapyRelationshipType");
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
        public MedicinalProductContraindicationOtherTherapyComponent setTherapyRelationshipType(CodeableConcept value) { 
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
        public MedicinalProductContraindicationOtherTherapyComponent setMedication(Type value) { 
          if (value != null && !(value instanceof CodeableConcept || value instanceof Reference))
            throw new Error("Not the right type for MedicinalProductContraindication.otherTherapy.medication[x]: "+value.fhirType());
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

      public MedicinalProductContraindicationOtherTherapyComponent copy() {
        MedicinalProductContraindicationOtherTherapyComponent dst = new MedicinalProductContraindicationOtherTherapyComponent();
        copyValues(dst);
        dst.therapyRelationshipType = therapyRelationshipType == null ? null : therapyRelationshipType.copy();
        dst.medication = medication == null ? null : medication.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof MedicinalProductContraindicationOtherTherapyComponent))
          return false;
        MedicinalProductContraindicationOtherTherapyComponent o = (MedicinalProductContraindicationOtherTherapyComponent) other_;
        return compareDeep(therapyRelationshipType, o.therapyRelationshipType, true) && compareDeep(medication, o.medication, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof MedicinalProductContraindicationOtherTherapyComponent))
          return false;
        MedicinalProductContraindicationOtherTherapyComponent o = (MedicinalProductContraindicationOtherTherapyComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(therapyRelationshipType, medication
          );
      }

  public String fhirType() {
    return "MedicinalProductContraindication.otherTherapy";

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
     * The disease, symptom or procedure for the contraindication.
     */
    @Child(name = "disease", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The disease, symptom or procedure for the contraindication", formalDefinition="The disease, symptom or procedure for the contraindication." )
    protected CodeableConcept disease;

    /**
     * The status of the disease or symptom for the contraindication.
     */
    @Child(name = "diseaseStatus", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The status of the disease or symptom for the contraindication", formalDefinition="The status of the disease or symptom for the contraindication." )
    protected CodeableConcept diseaseStatus;

    /**
     * A comorbidity (concurrent condition) or coinfection.
     */
    @Child(name = "comorbidity", type = {CodeableConcept.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="A comorbidity (concurrent condition) or coinfection", formalDefinition="A comorbidity (concurrent condition) or coinfection." )
    protected List<CodeableConcept> comorbidity;

    /**
     * Information about the use of the medicinal product in relation to other therapies as part of the indication.
     */
    @Child(name = "therapeuticIndication", type = {MedicinalProductIndication.class}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Information about the use of the medicinal product in relation to other therapies as part of the indication", formalDefinition="Information about the use of the medicinal product in relation to other therapies as part of the indication." )
    protected List<Reference> therapeuticIndication;
    /**
     * The actual objects that are the target of the reference (Information about the use of the medicinal product in relation to other therapies as part of the indication.)
     */
    protected List<MedicinalProductIndication> therapeuticIndicationTarget;


    /**
     * Information about the use of the medicinal product in relation to other therapies described as part of the indication.
     */
    @Child(name = "otherTherapy", type = {}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Information about the use of the medicinal product in relation to other therapies described as part of the indication", formalDefinition="Information about the use of the medicinal product in relation to other therapies described as part of the indication." )
    protected List<MedicinalProductContraindicationOtherTherapyComponent> otherTherapy;

    /**
     * The population group to which this applies.
     */
    @Child(name = "population", type = {Population.class}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="The population group to which this applies", formalDefinition="The population group to which this applies." )
    protected List<Population> population;

    private static final long serialVersionUID = -1746103034L;

  /**
   * Constructor
   */
    public MedicinalProductContraindication() {
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
    public MedicinalProductContraindication setSubject(List<Reference> theSubject) { 
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

    public MedicinalProductContraindication addSubject(Reference t) { //3
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
     * @return {@link #disease} (The disease, symptom or procedure for the contraindication.)
     */
    public CodeableConcept getDisease() { 
      if (this.disease == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicinalProductContraindication.disease");
        else if (Configuration.doAutoCreate())
          this.disease = new CodeableConcept(); // cc
      return this.disease;
    }

    public boolean hasDisease() { 
      return this.disease != null && !this.disease.isEmpty();
    }

    /**
     * @param value {@link #disease} (The disease, symptom or procedure for the contraindication.)
     */
    public MedicinalProductContraindication setDisease(CodeableConcept value) { 
      this.disease = value;
      return this;
    }

    /**
     * @return {@link #diseaseStatus} (The status of the disease or symptom for the contraindication.)
     */
    public CodeableConcept getDiseaseStatus() { 
      if (this.diseaseStatus == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicinalProductContraindication.diseaseStatus");
        else if (Configuration.doAutoCreate())
          this.diseaseStatus = new CodeableConcept(); // cc
      return this.diseaseStatus;
    }

    public boolean hasDiseaseStatus() { 
      return this.diseaseStatus != null && !this.diseaseStatus.isEmpty();
    }

    /**
     * @param value {@link #diseaseStatus} (The status of the disease or symptom for the contraindication.)
     */
    public MedicinalProductContraindication setDiseaseStatus(CodeableConcept value) { 
      this.diseaseStatus = value;
      return this;
    }

    /**
     * @return {@link #comorbidity} (A comorbidity (concurrent condition) or coinfection.)
     */
    public List<CodeableConcept> getComorbidity() { 
      if (this.comorbidity == null)
        this.comorbidity = new ArrayList<CodeableConcept>();
      return this.comorbidity;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicinalProductContraindication setComorbidity(List<CodeableConcept> theComorbidity) { 
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

    public MedicinalProductContraindication addComorbidity(CodeableConcept t) { //3
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
     * @return {@link #therapeuticIndication} (Information about the use of the medicinal product in relation to other therapies as part of the indication.)
     */
    public List<Reference> getTherapeuticIndication() { 
      if (this.therapeuticIndication == null)
        this.therapeuticIndication = new ArrayList<Reference>();
      return this.therapeuticIndication;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicinalProductContraindication setTherapeuticIndication(List<Reference> theTherapeuticIndication) { 
      this.therapeuticIndication = theTherapeuticIndication;
      return this;
    }

    public boolean hasTherapeuticIndication() { 
      if (this.therapeuticIndication == null)
        return false;
      for (Reference item : this.therapeuticIndication)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addTherapeuticIndication() { //3
      Reference t = new Reference();
      if (this.therapeuticIndication == null)
        this.therapeuticIndication = new ArrayList<Reference>();
      this.therapeuticIndication.add(t);
      return t;
    }

    public MedicinalProductContraindication addTherapeuticIndication(Reference t) { //3
      if (t == null)
        return this;
      if (this.therapeuticIndication == null)
        this.therapeuticIndication = new ArrayList<Reference>();
      this.therapeuticIndication.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #therapeuticIndication}, creating it if it does not already exist
     */
    public Reference getTherapeuticIndicationFirstRep() { 
      if (getTherapeuticIndication().isEmpty()) {
        addTherapeuticIndication();
      }
      return getTherapeuticIndication().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<MedicinalProductIndication> getTherapeuticIndicationTarget() { 
      if (this.therapeuticIndicationTarget == null)
        this.therapeuticIndicationTarget = new ArrayList<MedicinalProductIndication>();
      return this.therapeuticIndicationTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public MedicinalProductIndication addTherapeuticIndicationTarget() { 
      MedicinalProductIndication r = new MedicinalProductIndication();
      if (this.therapeuticIndicationTarget == null)
        this.therapeuticIndicationTarget = new ArrayList<MedicinalProductIndication>();
      this.therapeuticIndicationTarget.add(r);
      return r;
    }

    /**
     * @return {@link #otherTherapy} (Information about the use of the medicinal product in relation to other therapies described as part of the indication.)
     */
    public List<MedicinalProductContraindicationOtherTherapyComponent> getOtherTherapy() { 
      if (this.otherTherapy == null)
        this.otherTherapy = new ArrayList<MedicinalProductContraindicationOtherTherapyComponent>();
      return this.otherTherapy;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicinalProductContraindication setOtherTherapy(List<MedicinalProductContraindicationOtherTherapyComponent> theOtherTherapy) { 
      this.otherTherapy = theOtherTherapy;
      return this;
    }

    public boolean hasOtherTherapy() { 
      if (this.otherTherapy == null)
        return false;
      for (MedicinalProductContraindicationOtherTherapyComponent item : this.otherTherapy)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public MedicinalProductContraindicationOtherTherapyComponent addOtherTherapy() { //3
      MedicinalProductContraindicationOtherTherapyComponent t = new MedicinalProductContraindicationOtherTherapyComponent();
      if (this.otherTherapy == null)
        this.otherTherapy = new ArrayList<MedicinalProductContraindicationOtherTherapyComponent>();
      this.otherTherapy.add(t);
      return t;
    }

    public MedicinalProductContraindication addOtherTherapy(MedicinalProductContraindicationOtherTherapyComponent t) { //3
      if (t == null)
        return this;
      if (this.otherTherapy == null)
        this.otherTherapy = new ArrayList<MedicinalProductContraindicationOtherTherapyComponent>();
      this.otherTherapy.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #otherTherapy}, creating it if it does not already exist
     */
    public MedicinalProductContraindicationOtherTherapyComponent getOtherTherapyFirstRep() { 
      if (getOtherTherapy().isEmpty()) {
        addOtherTherapy();
      }
      return getOtherTherapy().get(0);
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
    public MedicinalProductContraindication setPopulation(List<Population> thePopulation) { 
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

    public MedicinalProductContraindication addPopulation(Population t) { //3
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
        children.add(new Property("disease", "CodeableConcept", "The disease, symptom or procedure for the contraindication.", 0, 1, disease));
        children.add(new Property("diseaseStatus", "CodeableConcept", "The status of the disease or symptom for the contraindication.", 0, 1, diseaseStatus));
        children.add(new Property("comorbidity", "CodeableConcept", "A comorbidity (concurrent condition) or coinfection.", 0, java.lang.Integer.MAX_VALUE, comorbidity));
        children.add(new Property("therapeuticIndication", "Reference(MedicinalProductIndication)", "Information about the use of the medicinal product in relation to other therapies as part of the indication.", 0, java.lang.Integer.MAX_VALUE, therapeuticIndication));
        children.add(new Property("otherTherapy", "", "Information about the use of the medicinal product in relation to other therapies described as part of the indication.", 0, java.lang.Integer.MAX_VALUE, otherTherapy));
        children.add(new Property("population", "Population", "The population group to which this applies.", 0, java.lang.Integer.MAX_VALUE, population));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case -1867885268: /*subject*/  return new Property("subject", "Reference(MedicinalProduct|Medication)", "The medication for which this is an indication.", 0, java.lang.Integer.MAX_VALUE, subject);
        case 1671426428: /*disease*/  return new Property("disease", "CodeableConcept", "The disease, symptom or procedure for the contraindication.", 0, 1, disease);
        case -505503602: /*diseaseStatus*/  return new Property("diseaseStatus", "CodeableConcept", "The status of the disease or symptom for the contraindication.", 0, 1, diseaseStatus);
        case -406395211: /*comorbidity*/  return new Property("comorbidity", "CodeableConcept", "A comorbidity (concurrent condition) or coinfection.", 0, java.lang.Integer.MAX_VALUE, comorbidity);
        case -1925150262: /*therapeuticIndication*/  return new Property("therapeuticIndication", "Reference(MedicinalProductIndication)", "Information about the use of the medicinal product in relation to other therapies as part of the indication.", 0, java.lang.Integer.MAX_VALUE, therapeuticIndication);
        case -544509127: /*otherTherapy*/  return new Property("otherTherapy", "", "Information about the use of the medicinal product in relation to other therapies described as part of the indication.", 0, java.lang.Integer.MAX_VALUE, otherTherapy);
        case -2023558323: /*population*/  return new Property("population", "Population", "The population group to which this applies.", 0, java.lang.Integer.MAX_VALUE, population);
        default: return super.getNamedProperty(_hash, _name, _checkValid);
        }

      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1867885268: /*subject*/ return this.subject == null ? new Base[0] : this.subject.toArray(new Base[this.subject.size()]); // Reference
        case 1671426428: /*disease*/ return this.disease == null ? new Base[0] : new Base[] {this.disease}; // CodeableConcept
        case -505503602: /*diseaseStatus*/ return this.diseaseStatus == null ? new Base[0] : new Base[] {this.diseaseStatus}; // CodeableConcept
        case -406395211: /*comorbidity*/ return this.comorbidity == null ? new Base[0] : this.comorbidity.toArray(new Base[this.comorbidity.size()]); // CodeableConcept
        case -1925150262: /*therapeuticIndication*/ return this.therapeuticIndication == null ? new Base[0] : this.therapeuticIndication.toArray(new Base[this.therapeuticIndication.size()]); // Reference
        case -544509127: /*otherTherapy*/ return this.otherTherapy == null ? new Base[0] : this.otherTherapy.toArray(new Base[this.otherTherapy.size()]); // MedicinalProductContraindicationOtherTherapyComponent
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
        case 1671426428: // disease
          this.disease = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -505503602: // diseaseStatus
          this.diseaseStatus = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -406395211: // comorbidity
          this.getComorbidity().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -1925150262: // therapeuticIndication
          this.getTherapeuticIndication().add(castToReference(value)); // Reference
          return value;
        case -544509127: // otherTherapy
          this.getOtherTherapy().add((MedicinalProductContraindicationOtherTherapyComponent) value); // MedicinalProductContraindicationOtherTherapyComponent
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
        } else if (name.equals("disease")) {
          this.disease = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("diseaseStatus")) {
          this.diseaseStatus = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("comorbidity")) {
          this.getComorbidity().add(castToCodeableConcept(value));
        } else if (name.equals("therapeuticIndication")) {
          this.getTherapeuticIndication().add(castToReference(value));
        } else if (name.equals("otherTherapy")) {
          this.getOtherTherapy().add((MedicinalProductContraindicationOtherTherapyComponent) value);
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
        case 1671426428:  return getDisease(); 
        case -505503602:  return getDiseaseStatus(); 
        case -406395211:  return addComorbidity(); 
        case -1925150262:  return addTherapeuticIndication(); 
        case -544509127:  return addOtherTherapy(); 
        case -2023558323:  return addPopulation(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1867885268: /*subject*/ return new String[] {"Reference"};
        case 1671426428: /*disease*/ return new String[] {"CodeableConcept"};
        case -505503602: /*diseaseStatus*/ return new String[] {"CodeableConcept"};
        case -406395211: /*comorbidity*/ return new String[] {"CodeableConcept"};
        case -1925150262: /*therapeuticIndication*/ return new String[] {"Reference"};
        case -544509127: /*otherTherapy*/ return new String[] {};
        case -2023558323: /*population*/ return new String[] {"Population"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("subject")) {
          return addSubject();
        }
        else if (name.equals("disease")) {
          this.disease = new CodeableConcept();
          return this.disease;
        }
        else if (name.equals("diseaseStatus")) {
          this.diseaseStatus = new CodeableConcept();
          return this.diseaseStatus;
        }
        else if (name.equals("comorbidity")) {
          return addComorbidity();
        }
        else if (name.equals("therapeuticIndication")) {
          return addTherapeuticIndication();
        }
        else if (name.equals("otherTherapy")) {
          return addOtherTherapy();
        }
        else if (name.equals("population")) {
          return addPopulation();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "MedicinalProductContraindication";

  }

      public MedicinalProductContraindication copy() {
        MedicinalProductContraindication dst = new MedicinalProductContraindication();
        copyValues(dst);
        if (subject != null) {
          dst.subject = new ArrayList<Reference>();
          for (Reference i : subject)
            dst.subject.add(i.copy());
        };
        dst.disease = disease == null ? null : disease.copy();
        dst.diseaseStatus = diseaseStatus == null ? null : diseaseStatus.copy();
        if (comorbidity != null) {
          dst.comorbidity = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : comorbidity)
            dst.comorbidity.add(i.copy());
        };
        if (therapeuticIndication != null) {
          dst.therapeuticIndication = new ArrayList<Reference>();
          for (Reference i : therapeuticIndication)
            dst.therapeuticIndication.add(i.copy());
        };
        if (otherTherapy != null) {
          dst.otherTherapy = new ArrayList<MedicinalProductContraindicationOtherTherapyComponent>();
          for (MedicinalProductContraindicationOtherTherapyComponent i : otherTherapy)
            dst.otherTherapy.add(i.copy());
        };
        if (population != null) {
          dst.population = new ArrayList<Population>();
          for (Population i : population)
            dst.population.add(i.copy());
        };
        return dst;
      }

      protected MedicinalProductContraindication typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof MedicinalProductContraindication))
          return false;
        MedicinalProductContraindication o = (MedicinalProductContraindication) other_;
        return compareDeep(subject, o.subject, true) && compareDeep(disease, o.disease, true) && compareDeep(diseaseStatus, o.diseaseStatus, true)
           && compareDeep(comorbidity, o.comorbidity, true) && compareDeep(therapeuticIndication, o.therapeuticIndication, true)
           && compareDeep(otherTherapy, o.otherTherapy, true) && compareDeep(population, o.population, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof MedicinalProductContraindication))
          return false;
        MedicinalProductContraindication o = (MedicinalProductContraindication) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(subject, disease, diseaseStatus
          , comorbidity, therapeuticIndication, otherTherapy, population);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.MedicinalProductContraindication;
   }

 /**
   * Search parameter: <b>subject</b>
   * <p>
   * Description: <b>The medication for which this is an contraindication</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MedicinalProductContraindication.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="subject", path="MedicinalProductContraindication.subject", description="The medication for which this is an contraindication", type="reference", target={Medication.class, MedicinalProduct.class } )
  public static final String SP_SUBJECT = "subject";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>subject</b>
   * <p>
   * Description: <b>The medication for which this is an contraindication</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MedicinalProductContraindication.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SUBJECT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SUBJECT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>MedicinalProductContraindication:subject</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SUBJECT = new ca.uhn.fhir.model.api.Include("MedicinalProductContraindication:subject").toLocked();


}

