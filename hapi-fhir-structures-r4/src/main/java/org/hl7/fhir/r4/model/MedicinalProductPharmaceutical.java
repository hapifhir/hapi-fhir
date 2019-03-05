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
 * A pharmaceutical product described in terms of its composition and dose form.
 */
@ResourceDef(name="MedicinalProductPharmaceutical", profile="http://hl7.org/fhir/StructureDefinition/MedicinalProductPharmaceutical")
public class MedicinalProductPharmaceutical extends DomainResource {

    @Block()
    public static class MedicinalProductPharmaceuticalCharacteristicsComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A coded characteristic.
         */
        @Child(name = "code", type = {CodeableConcept.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="A coded characteristic", formalDefinition="A coded characteristic." )
        protected CodeableConcept code;

        /**
         * The status of characteristic e.g. assigned or pending.
         */
        @Child(name = "status", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The status of characteristic e.g. assigned or pending", formalDefinition="The status of characteristic e.g. assigned or pending." )
        protected CodeableConcept status;

        private static final long serialVersionUID = 1414556635L;

    /**
     * Constructor
     */
      public MedicinalProductPharmaceuticalCharacteristicsComponent() {
        super();
      }

    /**
     * Constructor
     */
      public MedicinalProductPharmaceuticalCharacteristicsComponent(CodeableConcept code) {
        super();
        this.code = code;
      }

        /**
         * @return {@link #code} (A coded characteristic.)
         */
        public CodeableConcept getCode() { 
          if (this.code == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicinalProductPharmaceuticalCharacteristicsComponent.code");
            else if (Configuration.doAutoCreate())
              this.code = new CodeableConcept(); // cc
          return this.code;
        }

        public boolean hasCode() { 
          return this.code != null && !this.code.isEmpty();
        }

        /**
         * @param value {@link #code} (A coded characteristic.)
         */
        public MedicinalProductPharmaceuticalCharacteristicsComponent setCode(CodeableConcept value) { 
          this.code = value;
          return this;
        }

        /**
         * @return {@link #status} (The status of characteristic e.g. assigned or pending.)
         */
        public CodeableConcept getStatus() { 
          if (this.status == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicinalProductPharmaceuticalCharacteristicsComponent.status");
            else if (Configuration.doAutoCreate())
              this.status = new CodeableConcept(); // cc
          return this.status;
        }

        public boolean hasStatus() { 
          return this.status != null && !this.status.isEmpty();
        }

        /**
         * @param value {@link #status} (The status of characteristic e.g. assigned or pending.)
         */
        public MedicinalProductPharmaceuticalCharacteristicsComponent setStatus(CodeableConcept value) { 
          this.status = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("code", "CodeableConcept", "A coded characteristic.", 0, 1, code));
          children.add(new Property("status", "CodeableConcept", "The status of characteristic e.g. assigned or pending.", 0, 1, status));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3059181: /*code*/  return new Property("code", "CodeableConcept", "A coded characteristic.", 0, 1, code);
          case -892481550: /*status*/  return new Property("status", "CodeableConcept", "The status of characteristic e.g. assigned or pending.", 0, 1, status);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3059181: /*code*/ return this.code == null ? new Base[0] : new Base[] {this.code}; // CodeableConcept
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // CodeableConcept
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
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("code")) {
          this.code = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("status")) {
          this.status = castToCodeableConcept(value); // CodeableConcept
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3059181:  return getCode(); 
        case -892481550:  return getStatus(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3059181: /*code*/ return new String[] {"CodeableConcept"};
        case -892481550: /*status*/ return new String[] {"CodeableConcept"};
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
        else
          return super.addChild(name);
      }

      public MedicinalProductPharmaceuticalCharacteristicsComponent copy() {
        MedicinalProductPharmaceuticalCharacteristicsComponent dst = new MedicinalProductPharmaceuticalCharacteristicsComponent();
        copyValues(dst);
        dst.code = code == null ? null : code.copy();
        dst.status = status == null ? null : status.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof MedicinalProductPharmaceuticalCharacteristicsComponent))
          return false;
        MedicinalProductPharmaceuticalCharacteristicsComponent o = (MedicinalProductPharmaceuticalCharacteristicsComponent) other_;
        return compareDeep(code, o.code, true) && compareDeep(status, o.status, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof MedicinalProductPharmaceuticalCharacteristicsComponent))
          return false;
        MedicinalProductPharmaceuticalCharacteristicsComponent o = (MedicinalProductPharmaceuticalCharacteristicsComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(code, status);
      }

  public String fhirType() {
    return "MedicinalProductPharmaceutical.characteristics";

  }

  }

    @Block()
    public static class MedicinalProductPharmaceuticalRouteOfAdministrationComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Coded expression for the route.
         */
        @Child(name = "code", type = {CodeableConcept.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Coded expression for the route", formalDefinition="Coded expression for the route." )
        protected CodeableConcept code;

        /**
         * The first dose (dose quantity) administered in humans can be specified, for a product under investigation, using a numerical value and its unit of measurement.
         */
        @Child(name = "firstDose", type = {Quantity.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The first dose (dose quantity) administered in humans can be specified, for a product under investigation, using a numerical value and its unit of measurement", formalDefinition="The first dose (dose quantity) administered in humans can be specified, for a product under investigation, using a numerical value and its unit of measurement." )
        protected Quantity firstDose;

        /**
         * The maximum single dose that can be administered as per the protocol of a clinical trial can be specified using a numerical value and its unit of measurement.
         */
        @Child(name = "maxSingleDose", type = {Quantity.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The maximum single dose that can be administered as per the protocol of a clinical trial can be specified using a numerical value and its unit of measurement", formalDefinition="The maximum single dose that can be administered as per the protocol of a clinical trial can be specified using a numerical value and its unit of measurement." )
        protected Quantity maxSingleDose;

        /**
         * The maximum dose per day (maximum dose quantity to be administered in any one 24-h period) that can be administered as per the protocol referenced in the clinical trial authorisation.
         */
        @Child(name = "maxDosePerDay", type = {Quantity.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The maximum dose per day (maximum dose quantity to be administered in any one 24-h period) that can be administered as per the protocol referenced in the clinical trial authorisation", formalDefinition="The maximum dose per day (maximum dose quantity to be administered in any one 24-h period) that can be administered as per the protocol referenced in the clinical trial authorisation." )
        protected Quantity maxDosePerDay;

        /**
         * The maximum dose per treatment period that can be administered as per the protocol referenced in the clinical trial authorisation.
         */
        @Child(name = "maxDosePerTreatmentPeriod", type = {Ratio.class}, order=5, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The maximum dose per treatment period that can be administered as per the protocol referenced in the clinical trial authorisation", formalDefinition="The maximum dose per treatment period that can be administered as per the protocol referenced in the clinical trial authorisation." )
        protected Ratio maxDosePerTreatmentPeriod;

        /**
         * The maximum treatment period during which an Investigational Medicinal Product can be administered as per the protocol referenced in the clinical trial authorisation.
         */
        @Child(name = "maxTreatmentPeriod", type = {Duration.class}, order=6, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The maximum treatment period during which an Investigational Medicinal Product can be administered as per the protocol referenced in the clinical trial authorisation", formalDefinition="The maximum treatment period during which an Investigational Medicinal Product can be administered as per the protocol referenced in the clinical trial authorisation." )
        protected Duration maxTreatmentPeriod;

        /**
         * A species for which this route applies.
         */
        @Child(name = "targetSpecies", type = {}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="A species for which this route applies", formalDefinition="A species for which this route applies." )
        protected List<MedicinalProductPharmaceuticalRouteOfAdministrationTargetSpeciesComponent> targetSpecies;

        private static final long serialVersionUID = 854394783L;

    /**
     * Constructor
     */
      public MedicinalProductPharmaceuticalRouteOfAdministrationComponent() {
        super();
      }

    /**
     * Constructor
     */
      public MedicinalProductPharmaceuticalRouteOfAdministrationComponent(CodeableConcept code) {
        super();
        this.code = code;
      }

        /**
         * @return {@link #code} (Coded expression for the route.)
         */
        public CodeableConcept getCode() { 
          if (this.code == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicinalProductPharmaceuticalRouteOfAdministrationComponent.code");
            else if (Configuration.doAutoCreate())
              this.code = new CodeableConcept(); // cc
          return this.code;
        }

        public boolean hasCode() { 
          return this.code != null && !this.code.isEmpty();
        }

        /**
         * @param value {@link #code} (Coded expression for the route.)
         */
        public MedicinalProductPharmaceuticalRouteOfAdministrationComponent setCode(CodeableConcept value) { 
          this.code = value;
          return this;
        }

        /**
         * @return {@link #firstDose} (The first dose (dose quantity) administered in humans can be specified, for a product under investigation, using a numerical value and its unit of measurement.)
         */
        public Quantity getFirstDose() { 
          if (this.firstDose == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicinalProductPharmaceuticalRouteOfAdministrationComponent.firstDose");
            else if (Configuration.doAutoCreate())
              this.firstDose = new Quantity(); // cc
          return this.firstDose;
        }

        public boolean hasFirstDose() { 
          return this.firstDose != null && !this.firstDose.isEmpty();
        }

        /**
         * @param value {@link #firstDose} (The first dose (dose quantity) administered in humans can be specified, for a product under investigation, using a numerical value and its unit of measurement.)
         */
        public MedicinalProductPharmaceuticalRouteOfAdministrationComponent setFirstDose(Quantity value) { 
          this.firstDose = value;
          return this;
        }

        /**
         * @return {@link #maxSingleDose} (The maximum single dose that can be administered as per the protocol of a clinical trial can be specified using a numerical value and its unit of measurement.)
         */
        public Quantity getMaxSingleDose() { 
          if (this.maxSingleDose == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicinalProductPharmaceuticalRouteOfAdministrationComponent.maxSingleDose");
            else if (Configuration.doAutoCreate())
              this.maxSingleDose = new Quantity(); // cc
          return this.maxSingleDose;
        }

        public boolean hasMaxSingleDose() { 
          return this.maxSingleDose != null && !this.maxSingleDose.isEmpty();
        }

        /**
         * @param value {@link #maxSingleDose} (The maximum single dose that can be administered as per the protocol of a clinical trial can be specified using a numerical value and its unit of measurement.)
         */
        public MedicinalProductPharmaceuticalRouteOfAdministrationComponent setMaxSingleDose(Quantity value) { 
          this.maxSingleDose = value;
          return this;
        }

        /**
         * @return {@link #maxDosePerDay} (The maximum dose per day (maximum dose quantity to be administered in any one 24-h period) that can be administered as per the protocol referenced in the clinical trial authorisation.)
         */
        public Quantity getMaxDosePerDay() { 
          if (this.maxDosePerDay == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicinalProductPharmaceuticalRouteOfAdministrationComponent.maxDosePerDay");
            else if (Configuration.doAutoCreate())
              this.maxDosePerDay = new Quantity(); // cc
          return this.maxDosePerDay;
        }

        public boolean hasMaxDosePerDay() { 
          return this.maxDosePerDay != null && !this.maxDosePerDay.isEmpty();
        }

        /**
         * @param value {@link #maxDosePerDay} (The maximum dose per day (maximum dose quantity to be administered in any one 24-h period) that can be administered as per the protocol referenced in the clinical trial authorisation.)
         */
        public MedicinalProductPharmaceuticalRouteOfAdministrationComponent setMaxDosePerDay(Quantity value) { 
          this.maxDosePerDay = value;
          return this;
        }

        /**
         * @return {@link #maxDosePerTreatmentPeriod} (The maximum dose per treatment period that can be administered as per the protocol referenced in the clinical trial authorisation.)
         */
        public Ratio getMaxDosePerTreatmentPeriod() { 
          if (this.maxDosePerTreatmentPeriod == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicinalProductPharmaceuticalRouteOfAdministrationComponent.maxDosePerTreatmentPeriod");
            else if (Configuration.doAutoCreate())
              this.maxDosePerTreatmentPeriod = new Ratio(); // cc
          return this.maxDosePerTreatmentPeriod;
        }

        public boolean hasMaxDosePerTreatmentPeriod() { 
          return this.maxDosePerTreatmentPeriod != null && !this.maxDosePerTreatmentPeriod.isEmpty();
        }

        /**
         * @param value {@link #maxDosePerTreatmentPeriod} (The maximum dose per treatment period that can be administered as per the protocol referenced in the clinical trial authorisation.)
         */
        public MedicinalProductPharmaceuticalRouteOfAdministrationComponent setMaxDosePerTreatmentPeriod(Ratio value) { 
          this.maxDosePerTreatmentPeriod = value;
          return this;
        }

        /**
         * @return {@link #maxTreatmentPeriod} (The maximum treatment period during which an Investigational Medicinal Product can be administered as per the protocol referenced in the clinical trial authorisation.)
         */
        public Duration getMaxTreatmentPeriod() { 
          if (this.maxTreatmentPeriod == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicinalProductPharmaceuticalRouteOfAdministrationComponent.maxTreatmentPeriod");
            else if (Configuration.doAutoCreate())
              this.maxTreatmentPeriod = new Duration(); // cc
          return this.maxTreatmentPeriod;
        }

        public boolean hasMaxTreatmentPeriod() { 
          return this.maxTreatmentPeriod != null && !this.maxTreatmentPeriod.isEmpty();
        }

        /**
         * @param value {@link #maxTreatmentPeriod} (The maximum treatment period during which an Investigational Medicinal Product can be administered as per the protocol referenced in the clinical trial authorisation.)
         */
        public MedicinalProductPharmaceuticalRouteOfAdministrationComponent setMaxTreatmentPeriod(Duration value) { 
          this.maxTreatmentPeriod = value;
          return this;
        }

        /**
         * @return {@link #targetSpecies} (A species for which this route applies.)
         */
        public List<MedicinalProductPharmaceuticalRouteOfAdministrationTargetSpeciesComponent> getTargetSpecies() { 
          if (this.targetSpecies == null)
            this.targetSpecies = new ArrayList<MedicinalProductPharmaceuticalRouteOfAdministrationTargetSpeciesComponent>();
          return this.targetSpecies;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public MedicinalProductPharmaceuticalRouteOfAdministrationComponent setTargetSpecies(List<MedicinalProductPharmaceuticalRouteOfAdministrationTargetSpeciesComponent> theTargetSpecies) { 
          this.targetSpecies = theTargetSpecies;
          return this;
        }

        public boolean hasTargetSpecies() { 
          if (this.targetSpecies == null)
            return false;
          for (MedicinalProductPharmaceuticalRouteOfAdministrationTargetSpeciesComponent item : this.targetSpecies)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public MedicinalProductPharmaceuticalRouteOfAdministrationTargetSpeciesComponent addTargetSpecies() { //3
          MedicinalProductPharmaceuticalRouteOfAdministrationTargetSpeciesComponent t = new MedicinalProductPharmaceuticalRouteOfAdministrationTargetSpeciesComponent();
          if (this.targetSpecies == null)
            this.targetSpecies = new ArrayList<MedicinalProductPharmaceuticalRouteOfAdministrationTargetSpeciesComponent>();
          this.targetSpecies.add(t);
          return t;
        }

        public MedicinalProductPharmaceuticalRouteOfAdministrationComponent addTargetSpecies(MedicinalProductPharmaceuticalRouteOfAdministrationTargetSpeciesComponent t) { //3
          if (t == null)
            return this;
          if (this.targetSpecies == null)
            this.targetSpecies = new ArrayList<MedicinalProductPharmaceuticalRouteOfAdministrationTargetSpeciesComponent>();
          this.targetSpecies.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #targetSpecies}, creating it if it does not already exist
         */
        public MedicinalProductPharmaceuticalRouteOfAdministrationTargetSpeciesComponent getTargetSpeciesFirstRep() { 
          if (getTargetSpecies().isEmpty()) {
            addTargetSpecies();
          }
          return getTargetSpecies().get(0);
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("code", "CodeableConcept", "Coded expression for the route.", 0, 1, code));
          children.add(new Property("firstDose", "Quantity", "The first dose (dose quantity) administered in humans can be specified, for a product under investigation, using a numerical value and its unit of measurement.", 0, 1, firstDose));
          children.add(new Property("maxSingleDose", "Quantity", "The maximum single dose that can be administered as per the protocol of a clinical trial can be specified using a numerical value and its unit of measurement.", 0, 1, maxSingleDose));
          children.add(new Property("maxDosePerDay", "Quantity", "The maximum dose per day (maximum dose quantity to be administered in any one 24-h period) that can be administered as per the protocol referenced in the clinical trial authorisation.", 0, 1, maxDosePerDay));
          children.add(new Property("maxDosePerTreatmentPeriod", "Ratio", "The maximum dose per treatment period that can be administered as per the protocol referenced in the clinical trial authorisation.", 0, 1, maxDosePerTreatmentPeriod));
          children.add(new Property("maxTreatmentPeriod", "Duration", "The maximum treatment period during which an Investigational Medicinal Product can be administered as per the protocol referenced in the clinical trial authorisation.", 0, 1, maxTreatmentPeriod));
          children.add(new Property("targetSpecies", "", "A species for which this route applies.", 0, java.lang.Integer.MAX_VALUE, targetSpecies));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3059181: /*code*/  return new Property("code", "CodeableConcept", "Coded expression for the route.", 0, 1, code);
          case 132551405: /*firstDose*/  return new Property("firstDose", "Quantity", "The first dose (dose quantity) administered in humans can be specified, for a product under investigation, using a numerical value and its unit of measurement.", 0, 1, firstDose);
          case -259207927: /*maxSingleDose*/  return new Property("maxSingleDose", "Quantity", "The maximum single dose that can be administered as per the protocol of a clinical trial can be specified using a numerical value and its unit of measurement.", 0, 1, maxSingleDose);
          case -2017475520: /*maxDosePerDay*/  return new Property("maxDosePerDay", "Quantity", "The maximum dose per day (maximum dose quantity to be administered in any one 24-h period) that can be administered as per the protocol referenced in the clinical trial authorisation.", 0, 1, maxDosePerDay);
          case -608040195: /*maxDosePerTreatmentPeriod*/  return new Property("maxDosePerTreatmentPeriod", "Ratio", "The maximum dose per treatment period that can be administered as per the protocol referenced in the clinical trial authorisation.", 0, 1, maxDosePerTreatmentPeriod);
          case 920698453: /*maxTreatmentPeriod*/  return new Property("maxTreatmentPeriod", "Duration", "The maximum treatment period during which an Investigational Medicinal Product can be administered as per the protocol referenced in the clinical trial authorisation.", 0, 1, maxTreatmentPeriod);
          case 295481963: /*targetSpecies*/  return new Property("targetSpecies", "", "A species for which this route applies.", 0, java.lang.Integer.MAX_VALUE, targetSpecies);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3059181: /*code*/ return this.code == null ? new Base[0] : new Base[] {this.code}; // CodeableConcept
        case 132551405: /*firstDose*/ return this.firstDose == null ? new Base[0] : new Base[] {this.firstDose}; // Quantity
        case -259207927: /*maxSingleDose*/ return this.maxSingleDose == null ? new Base[0] : new Base[] {this.maxSingleDose}; // Quantity
        case -2017475520: /*maxDosePerDay*/ return this.maxDosePerDay == null ? new Base[0] : new Base[] {this.maxDosePerDay}; // Quantity
        case -608040195: /*maxDosePerTreatmentPeriod*/ return this.maxDosePerTreatmentPeriod == null ? new Base[0] : new Base[] {this.maxDosePerTreatmentPeriod}; // Ratio
        case 920698453: /*maxTreatmentPeriod*/ return this.maxTreatmentPeriod == null ? new Base[0] : new Base[] {this.maxTreatmentPeriod}; // Duration
        case 295481963: /*targetSpecies*/ return this.targetSpecies == null ? new Base[0] : this.targetSpecies.toArray(new Base[this.targetSpecies.size()]); // MedicinalProductPharmaceuticalRouteOfAdministrationTargetSpeciesComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3059181: // code
          this.code = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 132551405: // firstDose
          this.firstDose = castToQuantity(value); // Quantity
          return value;
        case -259207927: // maxSingleDose
          this.maxSingleDose = castToQuantity(value); // Quantity
          return value;
        case -2017475520: // maxDosePerDay
          this.maxDosePerDay = castToQuantity(value); // Quantity
          return value;
        case -608040195: // maxDosePerTreatmentPeriod
          this.maxDosePerTreatmentPeriod = castToRatio(value); // Ratio
          return value;
        case 920698453: // maxTreatmentPeriod
          this.maxTreatmentPeriod = castToDuration(value); // Duration
          return value;
        case 295481963: // targetSpecies
          this.getTargetSpecies().add((MedicinalProductPharmaceuticalRouteOfAdministrationTargetSpeciesComponent) value); // MedicinalProductPharmaceuticalRouteOfAdministrationTargetSpeciesComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("code")) {
          this.code = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("firstDose")) {
          this.firstDose = castToQuantity(value); // Quantity
        } else if (name.equals("maxSingleDose")) {
          this.maxSingleDose = castToQuantity(value); // Quantity
        } else if (name.equals("maxDosePerDay")) {
          this.maxDosePerDay = castToQuantity(value); // Quantity
        } else if (name.equals("maxDosePerTreatmentPeriod")) {
          this.maxDosePerTreatmentPeriod = castToRatio(value); // Ratio
        } else if (name.equals("maxTreatmentPeriod")) {
          this.maxTreatmentPeriod = castToDuration(value); // Duration
        } else if (name.equals("targetSpecies")) {
          this.getTargetSpecies().add((MedicinalProductPharmaceuticalRouteOfAdministrationTargetSpeciesComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3059181:  return getCode(); 
        case 132551405:  return getFirstDose(); 
        case -259207927:  return getMaxSingleDose(); 
        case -2017475520:  return getMaxDosePerDay(); 
        case -608040195:  return getMaxDosePerTreatmentPeriod(); 
        case 920698453:  return getMaxTreatmentPeriod(); 
        case 295481963:  return addTargetSpecies(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3059181: /*code*/ return new String[] {"CodeableConcept"};
        case 132551405: /*firstDose*/ return new String[] {"Quantity"};
        case -259207927: /*maxSingleDose*/ return new String[] {"Quantity"};
        case -2017475520: /*maxDosePerDay*/ return new String[] {"Quantity"};
        case -608040195: /*maxDosePerTreatmentPeriod*/ return new String[] {"Ratio"};
        case 920698453: /*maxTreatmentPeriod*/ return new String[] {"Duration"};
        case 295481963: /*targetSpecies*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("code")) {
          this.code = new CodeableConcept();
          return this.code;
        }
        else if (name.equals("firstDose")) {
          this.firstDose = new Quantity();
          return this.firstDose;
        }
        else if (name.equals("maxSingleDose")) {
          this.maxSingleDose = new Quantity();
          return this.maxSingleDose;
        }
        else if (name.equals("maxDosePerDay")) {
          this.maxDosePerDay = new Quantity();
          return this.maxDosePerDay;
        }
        else if (name.equals("maxDosePerTreatmentPeriod")) {
          this.maxDosePerTreatmentPeriod = new Ratio();
          return this.maxDosePerTreatmentPeriod;
        }
        else if (name.equals("maxTreatmentPeriod")) {
          this.maxTreatmentPeriod = new Duration();
          return this.maxTreatmentPeriod;
        }
        else if (name.equals("targetSpecies")) {
          return addTargetSpecies();
        }
        else
          return super.addChild(name);
      }

      public MedicinalProductPharmaceuticalRouteOfAdministrationComponent copy() {
        MedicinalProductPharmaceuticalRouteOfAdministrationComponent dst = new MedicinalProductPharmaceuticalRouteOfAdministrationComponent();
        copyValues(dst);
        dst.code = code == null ? null : code.copy();
        dst.firstDose = firstDose == null ? null : firstDose.copy();
        dst.maxSingleDose = maxSingleDose == null ? null : maxSingleDose.copy();
        dst.maxDosePerDay = maxDosePerDay == null ? null : maxDosePerDay.copy();
        dst.maxDosePerTreatmentPeriod = maxDosePerTreatmentPeriod == null ? null : maxDosePerTreatmentPeriod.copy();
        dst.maxTreatmentPeriod = maxTreatmentPeriod == null ? null : maxTreatmentPeriod.copy();
        if (targetSpecies != null) {
          dst.targetSpecies = new ArrayList<MedicinalProductPharmaceuticalRouteOfAdministrationTargetSpeciesComponent>();
          for (MedicinalProductPharmaceuticalRouteOfAdministrationTargetSpeciesComponent i : targetSpecies)
            dst.targetSpecies.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof MedicinalProductPharmaceuticalRouteOfAdministrationComponent))
          return false;
        MedicinalProductPharmaceuticalRouteOfAdministrationComponent o = (MedicinalProductPharmaceuticalRouteOfAdministrationComponent) other_;
        return compareDeep(code, o.code, true) && compareDeep(firstDose, o.firstDose, true) && compareDeep(maxSingleDose, o.maxSingleDose, true)
           && compareDeep(maxDosePerDay, o.maxDosePerDay, true) && compareDeep(maxDosePerTreatmentPeriod, o.maxDosePerTreatmentPeriod, true)
           && compareDeep(maxTreatmentPeriod, o.maxTreatmentPeriod, true) && compareDeep(targetSpecies, o.targetSpecies, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof MedicinalProductPharmaceuticalRouteOfAdministrationComponent))
          return false;
        MedicinalProductPharmaceuticalRouteOfAdministrationComponent o = (MedicinalProductPharmaceuticalRouteOfAdministrationComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(code, firstDose, maxSingleDose
          , maxDosePerDay, maxDosePerTreatmentPeriod, maxTreatmentPeriod, targetSpecies);
      }

  public String fhirType() {
    return "MedicinalProductPharmaceutical.routeOfAdministration";

  }

  }

    @Block()
    public static class MedicinalProductPharmaceuticalRouteOfAdministrationTargetSpeciesComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Coded expression for the species.
         */
        @Child(name = "code", type = {CodeableConcept.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Coded expression for the species", formalDefinition="Coded expression for the species." )
        protected CodeableConcept code;

        /**
         * A species specific time during which consumption of animal product is not appropriate.
         */
        @Child(name = "withdrawalPeriod", type = {}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="A species specific time during which consumption of animal product is not appropriate", formalDefinition="A species specific time during which consumption of animal product is not appropriate." )
        protected List<MedicinalProductPharmaceuticalRouteOfAdministrationTargetSpeciesWithdrawalPeriodComponent> withdrawalPeriod;

        private static final long serialVersionUID = -664052812L;

    /**
     * Constructor
     */
      public MedicinalProductPharmaceuticalRouteOfAdministrationTargetSpeciesComponent() {
        super();
      }

    /**
     * Constructor
     */
      public MedicinalProductPharmaceuticalRouteOfAdministrationTargetSpeciesComponent(CodeableConcept code) {
        super();
        this.code = code;
      }

        /**
         * @return {@link #code} (Coded expression for the species.)
         */
        public CodeableConcept getCode() { 
          if (this.code == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicinalProductPharmaceuticalRouteOfAdministrationTargetSpeciesComponent.code");
            else if (Configuration.doAutoCreate())
              this.code = new CodeableConcept(); // cc
          return this.code;
        }

        public boolean hasCode() { 
          return this.code != null && !this.code.isEmpty();
        }

        /**
         * @param value {@link #code} (Coded expression for the species.)
         */
        public MedicinalProductPharmaceuticalRouteOfAdministrationTargetSpeciesComponent setCode(CodeableConcept value) { 
          this.code = value;
          return this;
        }

        /**
         * @return {@link #withdrawalPeriod} (A species specific time during which consumption of animal product is not appropriate.)
         */
        public List<MedicinalProductPharmaceuticalRouteOfAdministrationTargetSpeciesWithdrawalPeriodComponent> getWithdrawalPeriod() { 
          if (this.withdrawalPeriod == null)
            this.withdrawalPeriod = new ArrayList<MedicinalProductPharmaceuticalRouteOfAdministrationTargetSpeciesWithdrawalPeriodComponent>();
          return this.withdrawalPeriod;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public MedicinalProductPharmaceuticalRouteOfAdministrationTargetSpeciesComponent setWithdrawalPeriod(List<MedicinalProductPharmaceuticalRouteOfAdministrationTargetSpeciesWithdrawalPeriodComponent> theWithdrawalPeriod) { 
          this.withdrawalPeriod = theWithdrawalPeriod;
          return this;
        }

        public boolean hasWithdrawalPeriod() { 
          if (this.withdrawalPeriod == null)
            return false;
          for (MedicinalProductPharmaceuticalRouteOfAdministrationTargetSpeciesWithdrawalPeriodComponent item : this.withdrawalPeriod)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public MedicinalProductPharmaceuticalRouteOfAdministrationTargetSpeciesWithdrawalPeriodComponent addWithdrawalPeriod() { //3
          MedicinalProductPharmaceuticalRouteOfAdministrationTargetSpeciesWithdrawalPeriodComponent t = new MedicinalProductPharmaceuticalRouteOfAdministrationTargetSpeciesWithdrawalPeriodComponent();
          if (this.withdrawalPeriod == null)
            this.withdrawalPeriod = new ArrayList<MedicinalProductPharmaceuticalRouteOfAdministrationTargetSpeciesWithdrawalPeriodComponent>();
          this.withdrawalPeriod.add(t);
          return t;
        }

        public MedicinalProductPharmaceuticalRouteOfAdministrationTargetSpeciesComponent addWithdrawalPeriod(MedicinalProductPharmaceuticalRouteOfAdministrationTargetSpeciesWithdrawalPeriodComponent t) { //3
          if (t == null)
            return this;
          if (this.withdrawalPeriod == null)
            this.withdrawalPeriod = new ArrayList<MedicinalProductPharmaceuticalRouteOfAdministrationTargetSpeciesWithdrawalPeriodComponent>();
          this.withdrawalPeriod.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #withdrawalPeriod}, creating it if it does not already exist
         */
        public MedicinalProductPharmaceuticalRouteOfAdministrationTargetSpeciesWithdrawalPeriodComponent getWithdrawalPeriodFirstRep() { 
          if (getWithdrawalPeriod().isEmpty()) {
            addWithdrawalPeriod();
          }
          return getWithdrawalPeriod().get(0);
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("code", "CodeableConcept", "Coded expression for the species.", 0, 1, code));
          children.add(new Property("withdrawalPeriod", "", "A species specific time during which consumption of animal product is not appropriate.", 0, java.lang.Integer.MAX_VALUE, withdrawalPeriod));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3059181: /*code*/  return new Property("code", "CodeableConcept", "Coded expression for the species.", 0, 1, code);
          case -98450730: /*withdrawalPeriod*/  return new Property("withdrawalPeriod", "", "A species specific time during which consumption of animal product is not appropriate.", 0, java.lang.Integer.MAX_VALUE, withdrawalPeriod);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3059181: /*code*/ return this.code == null ? new Base[0] : new Base[] {this.code}; // CodeableConcept
        case -98450730: /*withdrawalPeriod*/ return this.withdrawalPeriod == null ? new Base[0] : this.withdrawalPeriod.toArray(new Base[this.withdrawalPeriod.size()]); // MedicinalProductPharmaceuticalRouteOfAdministrationTargetSpeciesWithdrawalPeriodComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3059181: // code
          this.code = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -98450730: // withdrawalPeriod
          this.getWithdrawalPeriod().add((MedicinalProductPharmaceuticalRouteOfAdministrationTargetSpeciesWithdrawalPeriodComponent) value); // MedicinalProductPharmaceuticalRouteOfAdministrationTargetSpeciesWithdrawalPeriodComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("code")) {
          this.code = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("withdrawalPeriod")) {
          this.getWithdrawalPeriod().add((MedicinalProductPharmaceuticalRouteOfAdministrationTargetSpeciesWithdrawalPeriodComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3059181:  return getCode(); 
        case -98450730:  return addWithdrawalPeriod(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3059181: /*code*/ return new String[] {"CodeableConcept"};
        case -98450730: /*withdrawalPeriod*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("code")) {
          this.code = new CodeableConcept();
          return this.code;
        }
        else if (name.equals("withdrawalPeriod")) {
          return addWithdrawalPeriod();
        }
        else
          return super.addChild(name);
      }

      public MedicinalProductPharmaceuticalRouteOfAdministrationTargetSpeciesComponent copy() {
        MedicinalProductPharmaceuticalRouteOfAdministrationTargetSpeciesComponent dst = new MedicinalProductPharmaceuticalRouteOfAdministrationTargetSpeciesComponent();
        copyValues(dst);
        dst.code = code == null ? null : code.copy();
        if (withdrawalPeriod != null) {
          dst.withdrawalPeriod = new ArrayList<MedicinalProductPharmaceuticalRouteOfAdministrationTargetSpeciesWithdrawalPeriodComponent>();
          for (MedicinalProductPharmaceuticalRouteOfAdministrationTargetSpeciesWithdrawalPeriodComponent i : withdrawalPeriod)
            dst.withdrawalPeriod.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof MedicinalProductPharmaceuticalRouteOfAdministrationTargetSpeciesComponent))
          return false;
        MedicinalProductPharmaceuticalRouteOfAdministrationTargetSpeciesComponent o = (MedicinalProductPharmaceuticalRouteOfAdministrationTargetSpeciesComponent) other_;
        return compareDeep(code, o.code, true) && compareDeep(withdrawalPeriod, o.withdrawalPeriod, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof MedicinalProductPharmaceuticalRouteOfAdministrationTargetSpeciesComponent))
          return false;
        MedicinalProductPharmaceuticalRouteOfAdministrationTargetSpeciesComponent o = (MedicinalProductPharmaceuticalRouteOfAdministrationTargetSpeciesComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(code, withdrawalPeriod);
      }

  public String fhirType() {
    return "MedicinalProductPharmaceutical.routeOfAdministration.targetSpecies";

  }

  }

    @Block()
    public static class MedicinalProductPharmaceuticalRouteOfAdministrationTargetSpeciesWithdrawalPeriodComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Coded expression for the type of tissue for which the withdrawal period applues, e.g. meat, milk.
         */
        @Child(name = "tissue", type = {CodeableConcept.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Coded expression for the type of tissue for which the withdrawal period applues, e.g. meat, milk", formalDefinition="Coded expression for the type of tissue for which the withdrawal period applues, e.g. meat, milk." )
        protected CodeableConcept tissue;

        /**
         * A value for the time.
         */
        @Child(name = "value", type = {Quantity.class}, order=2, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="A value for the time", formalDefinition="A value for the time." )
        protected Quantity value;

        /**
         * Extra information about the withdrawal period.
         */
        @Child(name = "supportingInformation", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Extra information about the withdrawal period", formalDefinition="Extra information about the withdrawal period." )
        protected StringType supportingInformation;

        private static final long serialVersionUID = -1113691238L;

    /**
     * Constructor
     */
      public MedicinalProductPharmaceuticalRouteOfAdministrationTargetSpeciesWithdrawalPeriodComponent() {
        super();
      }

    /**
     * Constructor
     */
      public MedicinalProductPharmaceuticalRouteOfAdministrationTargetSpeciesWithdrawalPeriodComponent(CodeableConcept tissue, Quantity value) {
        super();
        this.tissue = tissue;
        this.value = value;
      }

        /**
         * @return {@link #tissue} (Coded expression for the type of tissue for which the withdrawal period applues, e.g. meat, milk.)
         */
        public CodeableConcept getTissue() { 
          if (this.tissue == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicinalProductPharmaceuticalRouteOfAdministrationTargetSpeciesWithdrawalPeriodComponent.tissue");
            else if (Configuration.doAutoCreate())
              this.tissue = new CodeableConcept(); // cc
          return this.tissue;
        }

        public boolean hasTissue() { 
          return this.tissue != null && !this.tissue.isEmpty();
        }

        /**
         * @param value {@link #tissue} (Coded expression for the type of tissue for which the withdrawal period applues, e.g. meat, milk.)
         */
        public MedicinalProductPharmaceuticalRouteOfAdministrationTargetSpeciesWithdrawalPeriodComponent setTissue(CodeableConcept value) { 
          this.tissue = value;
          return this;
        }

        /**
         * @return {@link #value} (A value for the time.)
         */
        public Quantity getValue() { 
          if (this.value == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicinalProductPharmaceuticalRouteOfAdministrationTargetSpeciesWithdrawalPeriodComponent.value");
            else if (Configuration.doAutoCreate())
              this.value = new Quantity(); // cc
          return this.value;
        }

        public boolean hasValue() { 
          return this.value != null && !this.value.isEmpty();
        }

        /**
         * @param value {@link #value} (A value for the time.)
         */
        public MedicinalProductPharmaceuticalRouteOfAdministrationTargetSpeciesWithdrawalPeriodComponent setValue(Quantity value) { 
          this.value = value;
          return this;
        }

        /**
         * @return {@link #supportingInformation} (Extra information about the withdrawal period.). This is the underlying object with id, value and extensions. The accessor "getSupportingInformation" gives direct access to the value
         */
        public StringType getSupportingInformationElement() { 
          if (this.supportingInformation == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicinalProductPharmaceuticalRouteOfAdministrationTargetSpeciesWithdrawalPeriodComponent.supportingInformation");
            else if (Configuration.doAutoCreate())
              this.supportingInformation = new StringType(); // bb
          return this.supportingInformation;
        }

        public boolean hasSupportingInformationElement() { 
          return this.supportingInformation != null && !this.supportingInformation.isEmpty();
        }

        public boolean hasSupportingInformation() { 
          return this.supportingInformation != null && !this.supportingInformation.isEmpty();
        }

        /**
         * @param value {@link #supportingInformation} (Extra information about the withdrawal period.). This is the underlying object with id, value and extensions. The accessor "getSupportingInformation" gives direct access to the value
         */
        public MedicinalProductPharmaceuticalRouteOfAdministrationTargetSpeciesWithdrawalPeriodComponent setSupportingInformationElement(StringType value) { 
          this.supportingInformation = value;
          return this;
        }

        /**
         * @return Extra information about the withdrawal period.
         */
        public String getSupportingInformation() { 
          return this.supportingInformation == null ? null : this.supportingInformation.getValue();
        }

        /**
         * @param value Extra information about the withdrawal period.
         */
        public MedicinalProductPharmaceuticalRouteOfAdministrationTargetSpeciesWithdrawalPeriodComponent setSupportingInformation(String value) { 
          if (Utilities.noString(value))
            this.supportingInformation = null;
          else {
            if (this.supportingInformation == null)
              this.supportingInformation = new StringType();
            this.supportingInformation.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("tissue", "CodeableConcept", "Coded expression for the type of tissue for which the withdrawal period applues, e.g. meat, milk.", 0, 1, tissue));
          children.add(new Property("value", "Quantity", "A value for the time.", 0, 1, value));
          children.add(new Property("supportingInformation", "string", "Extra information about the withdrawal period.", 0, 1, supportingInformation));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -873475867: /*tissue*/  return new Property("tissue", "CodeableConcept", "Coded expression for the type of tissue for which the withdrawal period applues, e.g. meat, milk.", 0, 1, tissue);
          case 111972721: /*value*/  return new Property("value", "Quantity", "A value for the time.", 0, 1, value);
          case -1248768647: /*supportingInformation*/  return new Property("supportingInformation", "string", "Extra information about the withdrawal period.", 0, 1, supportingInformation);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -873475867: /*tissue*/ return this.tissue == null ? new Base[0] : new Base[] {this.tissue}; // CodeableConcept
        case 111972721: /*value*/ return this.value == null ? new Base[0] : new Base[] {this.value}; // Quantity
        case -1248768647: /*supportingInformation*/ return this.supportingInformation == null ? new Base[0] : new Base[] {this.supportingInformation}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -873475867: // tissue
          this.tissue = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 111972721: // value
          this.value = castToQuantity(value); // Quantity
          return value;
        case -1248768647: // supportingInformation
          this.supportingInformation = castToString(value); // StringType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("tissue")) {
          this.tissue = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("value")) {
          this.value = castToQuantity(value); // Quantity
        } else if (name.equals("supportingInformation")) {
          this.supportingInformation = castToString(value); // StringType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -873475867:  return getTissue(); 
        case 111972721:  return getValue(); 
        case -1248768647:  return getSupportingInformationElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -873475867: /*tissue*/ return new String[] {"CodeableConcept"};
        case 111972721: /*value*/ return new String[] {"Quantity"};
        case -1248768647: /*supportingInformation*/ return new String[] {"string"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("tissue")) {
          this.tissue = new CodeableConcept();
          return this.tissue;
        }
        else if (name.equals("value")) {
          this.value = new Quantity();
          return this.value;
        }
        else if (name.equals("supportingInformation")) {
          throw new FHIRException("Cannot call addChild on a primitive type MedicinalProductPharmaceutical.supportingInformation");
        }
        else
          return super.addChild(name);
      }

      public MedicinalProductPharmaceuticalRouteOfAdministrationTargetSpeciesWithdrawalPeriodComponent copy() {
        MedicinalProductPharmaceuticalRouteOfAdministrationTargetSpeciesWithdrawalPeriodComponent dst = new MedicinalProductPharmaceuticalRouteOfAdministrationTargetSpeciesWithdrawalPeriodComponent();
        copyValues(dst);
        dst.tissue = tissue == null ? null : tissue.copy();
        dst.value = value == null ? null : value.copy();
        dst.supportingInformation = supportingInformation == null ? null : supportingInformation.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof MedicinalProductPharmaceuticalRouteOfAdministrationTargetSpeciesWithdrawalPeriodComponent))
          return false;
        MedicinalProductPharmaceuticalRouteOfAdministrationTargetSpeciesWithdrawalPeriodComponent o = (MedicinalProductPharmaceuticalRouteOfAdministrationTargetSpeciesWithdrawalPeriodComponent) other_;
        return compareDeep(tissue, o.tissue, true) && compareDeep(value, o.value, true) && compareDeep(supportingInformation, o.supportingInformation, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof MedicinalProductPharmaceuticalRouteOfAdministrationTargetSpeciesWithdrawalPeriodComponent))
          return false;
        MedicinalProductPharmaceuticalRouteOfAdministrationTargetSpeciesWithdrawalPeriodComponent o = (MedicinalProductPharmaceuticalRouteOfAdministrationTargetSpeciesWithdrawalPeriodComponent) other_;
        return compareValues(supportingInformation, o.supportingInformation, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(tissue, value, supportingInformation
          );
      }

  public String fhirType() {
    return "MedicinalProductPharmaceutical.routeOfAdministration.targetSpecies.withdrawalPeriod";

  }

  }

    /**
     * An identifier for the pharmaceutical medicinal product.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="An identifier for the pharmaceutical medicinal product", formalDefinition="An identifier for the pharmaceutical medicinal product." )
    protected List<Identifier> identifier;

    /**
     * The administrable dose form, after necessary reconstitution.
     */
    @Child(name = "administrableDoseForm", type = {CodeableConcept.class}, order=1, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The administrable dose form, after necessary reconstitution", formalDefinition="The administrable dose form, after necessary reconstitution." )
    protected CodeableConcept administrableDoseForm;

    /**
     * Todo.
     */
    @Child(name = "unitOfPresentation", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Todo", formalDefinition="Todo." )
    protected CodeableConcept unitOfPresentation;

    /**
     * Ingredient.
     */
    @Child(name = "ingredient", type = {MedicinalProductIngredient.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Ingredient", formalDefinition="Ingredient." )
    protected List<Reference> ingredient;
    /**
     * The actual objects that are the target of the reference (Ingredient.)
     */
    protected List<MedicinalProductIngredient> ingredientTarget;


    /**
     * Accompanying device.
     */
    @Child(name = "device", type = {DeviceDefinition.class}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Accompanying device", formalDefinition="Accompanying device." )
    protected List<Reference> device;
    /**
     * The actual objects that are the target of the reference (Accompanying device.)
     */
    protected List<DeviceDefinition> deviceTarget;


    /**
     * Characteristics e.g. a products onset of action.
     */
    @Child(name = "characteristics", type = {}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Characteristics e.g. a products onset of action", formalDefinition="Characteristics e.g. a products onset of action." )
    protected List<MedicinalProductPharmaceuticalCharacteristicsComponent> characteristics;

    /**
     * The path by which the pharmaceutical product is taken into or makes contact with the body.
     */
    @Child(name = "routeOfAdministration", type = {}, order=6, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="The path by which the pharmaceutical product is taken into or makes contact with the body", formalDefinition="The path by which the pharmaceutical product is taken into or makes contact with the body." )
    protected List<MedicinalProductPharmaceuticalRouteOfAdministrationComponent> routeOfAdministration;

    private static final long serialVersionUID = -1201548050L;

  /**
   * Constructor
   */
    public MedicinalProductPharmaceutical() {
      super();
    }

  /**
   * Constructor
   */
    public MedicinalProductPharmaceutical(CodeableConcept administrableDoseForm) {
      super();
      this.administrableDoseForm = administrableDoseForm;
    }

    /**
     * @return {@link #identifier} (An identifier for the pharmaceutical medicinal product.)
     */
    public List<Identifier> getIdentifier() { 
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      return this.identifier;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicinalProductPharmaceutical setIdentifier(List<Identifier> theIdentifier) { 
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

    public MedicinalProductPharmaceutical addIdentifier(Identifier t) { //3
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
     * @return {@link #administrableDoseForm} (The administrable dose form, after necessary reconstitution.)
     */
    public CodeableConcept getAdministrableDoseForm() { 
      if (this.administrableDoseForm == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicinalProductPharmaceutical.administrableDoseForm");
        else if (Configuration.doAutoCreate())
          this.administrableDoseForm = new CodeableConcept(); // cc
      return this.administrableDoseForm;
    }

    public boolean hasAdministrableDoseForm() { 
      return this.administrableDoseForm != null && !this.administrableDoseForm.isEmpty();
    }

    /**
     * @param value {@link #administrableDoseForm} (The administrable dose form, after necessary reconstitution.)
     */
    public MedicinalProductPharmaceutical setAdministrableDoseForm(CodeableConcept value) { 
      this.administrableDoseForm = value;
      return this;
    }

    /**
     * @return {@link #unitOfPresentation} (Todo.)
     */
    public CodeableConcept getUnitOfPresentation() { 
      if (this.unitOfPresentation == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicinalProductPharmaceutical.unitOfPresentation");
        else if (Configuration.doAutoCreate())
          this.unitOfPresentation = new CodeableConcept(); // cc
      return this.unitOfPresentation;
    }

    public boolean hasUnitOfPresentation() { 
      return this.unitOfPresentation != null && !this.unitOfPresentation.isEmpty();
    }

    /**
     * @param value {@link #unitOfPresentation} (Todo.)
     */
    public MedicinalProductPharmaceutical setUnitOfPresentation(CodeableConcept value) { 
      this.unitOfPresentation = value;
      return this;
    }

    /**
     * @return {@link #ingredient} (Ingredient.)
     */
    public List<Reference> getIngredient() { 
      if (this.ingredient == null)
        this.ingredient = new ArrayList<Reference>();
      return this.ingredient;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicinalProductPharmaceutical setIngredient(List<Reference> theIngredient) { 
      this.ingredient = theIngredient;
      return this;
    }

    public boolean hasIngredient() { 
      if (this.ingredient == null)
        return false;
      for (Reference item : this.ingredient)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addIngredient() { //3
      Reference t = new Reference();
      if (this.ingredient == null)
        this.ingredient = new ArrayList<Reference>();
      this.ingredient.add(t);
      return t;
    }

    public MedicinalProductPharmaceutical addIngredient(Reference t) { //3
      if (t == null)
        return this;
      if (this.ingredient == null)
        this.ingredient = new ArrayList<Reference>();
      this.ingredient.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #ingredient}, creating it if it does not already exist
     */
    public Reference getIngredientFirstRep() { 
      if (getIngredient().isEmpty()) {
        addIngredient();
      }
      return getIngredient().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<MedicinalProductIngredient> getIngredientTarget() { 
      if (this.ingredientTarget == null)
        this.ingredientTarget = new ArrayList<MedicinalProductIngredient>();
      return this.ingredientTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public MedicinalProductIngredient addIngredientTarget() { 
      MedicinalProductIngredient r = new MedicinalProductIngredient();
      if (this.ingredientTarget == null)
        this.ingredientTarget = new ArrayList<MedicinalProductIngredient>();
      this.ingredientTarget.add(r);
      return r;
    }

    /**
     * @return {@link #device} (Accompanying device.)
     */
    public List<Reference> getDevice() { 
      if (this.device == null)
        this.device = new ArrayList<Reference>();
      return this.device;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicinalProductPharmaceutical setDevice(List<Reference> theDevice) { 
      this.device = theDevice;
      return this;
    }

    public boolean hasDevice() { 
      if (this.device == null)
        return false;
      for (Reference item : this.device)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addDevice() { //3
      Reference t = new Reference();
      if (this.device == null)
        this.device = new ArrayList<Reference>();
      this.device.add(t);
      return t;
    }

    public MedicinalProductPharmaceutical addDevice(Reference t) { //3
      if (t == null)
        return this;
      if (this.device == null)
        this.device = new ArrayList<Reference>();
      this.device.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #device}, creating it if it does not already exist
     */
    public Reference getDeviceFirstRep() { 
      if (getDevice().isEmpty()) {
        addDevice();
      }
      return getDevice().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<DeviceDefinition> getDeviceTarget() { 
      if (this.deviceTarget == null)
        this.deviceTarget = new ArrayList<DeviceDefinition>();
      return this.deviceTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public DeviceDefinition addDeviceTarget() { 
      DeviceDefinition r = new DeviceDefinition();
      if (this.deviceTarget == null)
        this.deviceTarget = new ArrayList<DeviceDefinition>();
      this.deviceTarget.add(r);
      return r;
    }

    /**
     * @return {@link #characteristics} (Characteristics e.g. a products onset of action.)
     */
    public List<MedicinalProductPharmaceuticalCharacteristicsComponent> getCharacteristics() { 
      if (this.characteristics == null)
        this.characteristics = new ArrayList<MedicinalProductPharmaceuticalCharacteristicsComponent>();
      return this.characteristics;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicinalProductPharmaceutical setCharacteristics(List<MedicinalProductPharmaceuticalCharacteristicsComponent> theCharacteristics) { 
      this.characteristics = theCharacteristics;
      return this;
    }

    public boolean hasCharacteristics() { 
      if (this.characteristics == null)
        return false;
      for (MedicinalProductPharmaceuticalCharacteristicsComponent item : this.characteristics)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public MedicinalProductPharmaceuticalCharacteristicsComponent addCharacteristics() { //3
      MedicinalProductPharmaceuticalCharacteristicsComponent t = new MedicinalProductPharmaceuticalCharacteristicsComponent();
      if (this.characteristics == null)
        this.characteristics = new ArrayList<MedicinalProductPharmaceuticalCharacteristicsComponent>();
      this.characteristics.add(t);
      return t;
    }

    public MedicinalProductPharmaceutical addCharacteristics(MedicinalProductPharmaceuticalCharacteristicsComponent t) { //3
      if (t == null)
        return this;
      if (this.characteristics == null)
        this.characteristics = new ArrayList<MedicinalProductPharmaceuticalCharacteristicsComponent>();
      this.characteristics.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #characteristics}, creating it if it does not already exist
     */
    public MedicinalProductPharmaceuticalCharacteristicsComponent getCharacteristicsFirstRep() { 
      if (getCharacteristics().isEmpty()) {
        addCharacteristics();
      }
      return getCharacteristics().get(0);
    }

    /**
     * @return {@link #routeOfAdministration} (The path by which the pharmaceutical product is taken into or makes contact with the body.)
     */
    public List<MedicinalProductPharmaceuticalRouteOfAdministrationComponent> getRouteOfAdministration() { 
      if (this.routeOfAdministration == null)
        this.routeOfAdministration = new ArrayList<MedicinalProductPharmaceuticalRouteOfAdministrationComponent>();
      return this.routeOfAdministration;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicinalProductPharmaceutical setRouteOfAdministration(List<MedicinalProductPharmaceuticalRouteOfAdministrationComponent> theRouteOfAdministration) { 
      this.routeOfAdministration = theRouteOfAdministration;
      return this;
    }

    public boolean hasRouteOfAdministration() { 
      if (this.routeOfAdministration == null)
        return false;
      for (MedicinalProductPharmaceuticalRouteOfAdministrationComponent item : this.routeOfAdministration)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public MedicinalProductPharmaceuticalRouteOfAdministrationComponent addRouteOfAdministration() { //3
      MedicinalProductPharmaceuticalRouteOfAdministrationComponent t = new MedicinalProductPharmaceuticalRouteOfAdministrationComponent();
      if (this.routeOfAdministration == null)
        this.routeOfAdministration = new ArrayList<MedicinalProductPharmaceuticalRouteOfAdministrationComponent>();
      this.routeOfAdministration.add(t);
      return t;
    }

    public MedicinalProductPharmaceutical addRouteOfAdministration(MedicinalProductPharmaceuticalRouteOfAdministrationComponent t) { //3
      if (t == null)
        return this;
      if (this.routeOfAdministration == null)
        this.routeOfAdministration = new ArrayList<MedicinalProductPharmaceuticalRouteOfAdministrationComponent>();
      this.routeOfAdministration.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #routeOfAdministration}, creating it if it does not already exist
     */
    public MedicinalProductPharmaceuticalRouteOfAdministrationComponent getRouteOfAdministrationFirstRep() { 
      if (getRouteOfAdministration().isEmpty()) {
        addRouteOfAdministration();
      }
      return getRouteOfAdministration().get(0);
    }

      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("identifier", "Identifier", "An identifier for the pharmaceutical medicinal product.", 0, java.lang.Integer.MAX_VALUE, identifier));
        children.add(new Property("administrableDoseForm", "CodeableConcept", "The administrable dose form, after necessary reconstitution.", 0, 1, administrableDoseForm));
        children.add(new Property("unitOfPresentation", "CodeableConcept", "Todo.", 0, 1, unitOfPresentation));
        children.add(new Property("ingredient", "Reference(MedicinalProductIngredient)", "Ingredient.", 0, java.lang.Integer.MAX_VALUE, ingredient));
        children.add(new Property("device", "Reference(DeviceDefinition)", "Accompanying device.", 0, java.lang.Integer.MAX_VALUE, device));
        children.add(new Property("characteristics", "", "Characteristics e.g. a products onset of action.", 0, java.lang.Integer.MAX_VALUE, characteristics));
        children.add(new Property("routeOfAdministration", "", "The path by which the pharmaceutical product is taken into or makes contact with the body.", 0, java.lang.Integer.MAX_VALUE, routeOfAdministration));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case -1618432855: /*identifier*/  return new Property("identifier", "Identifier", "An identifier for the pharmaceutical medicinal product.", 0, java.lang.Integer.MAX_VALUE, identifier);
        case 1446105202: /*administrableDoseForm*/  return new Property("administrableDoseForm", "CodeableConcept", "The administrable dose form, after necessary reconstitution.", 0, 1, administrableDoseForm);
        case -1427765963: /*unitOfPresentation*/  return new Property("unitOfPresentation", "CodeableConcept", "Todo.", 0, 1, unitOfPresentation);
        case -206409263: /*ingredient*/  return new Property("ingredient", "Reference(MedicinalProductIngredient)", "Ingredient.", 0, java.lang.Integer.MAX_VALUE, ingredient);
        case -1335157162: /*device*/  return new Property("device", "Reference(DeviceDefinition)", "Accompanying device.", 0, java.lang.Integer.MAX_VALUE, device);
        case -1529171400: /*characteristics*/  return new Property("characteristics", "", "Characteristics e.g. a products onset of action.", 0, java.lang.Integer.MAX_VALUE, characteristics);
        case 1742084734: /*routeOfAdministration*/  return new Property("routeOfAdministration", "", "The path by which the pharmaceutical product is taken into or makes contact with the body.", 0, java.lang.Integer.MAX_VALUE, routeOfAdministration);
        default: return super.getNamedProperty(_hash, _name, _checkValid);
        }

      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case 1446105202: /*administrableDoseForm*/ return this.administrableDoseForm == null ? new Base[0] : new Base[] {this.administrableDoseForm}; // CodeableConcept
        case -1427765963: /*unitOfPresentation*/ return this.unitOfPresentation == null ? new Base[0] : new Base[] {this.unitOfPresentation}; // CodeableConcept
        case -206409263: /*ingredient*/ return this.ingredient == null ? new Base[0] : this.ingredient.toArray(new Base[this.ingredient.size()]); // Reference
        case -1335157162: /*device*/ return this.device == null ? new Base[0] : this.device.toArray(new Base[this.device.size()]); // Reference
        case -1529171400: /*characteristics*/ return this.characteristics == null ? new Base[0] : this.characteristics.toArray(new Base[this.characteristics.size()]); // MedicinalProductPharmaceuticalCharacteristicsComponent
        case 1742084734: /*routeOfAdministration*/ return this.routeOfAdministration == null ? new Base[0] : this.routeOfAdministration.toArray(new Base[this.routeOfAdministration.size()]); // MedicinalProductPharmaceuticalRouteOfAdministrationComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.getIdentifier().add(castToIdentifier(value)); // Identifier
          return value;
        case 1446105202: // administrableDoseForm
          this.administrableDoseForm = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1427765963: // unitOfPresentation
          this.unitOfPresentation = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -206409263: // ingredient
          this.getIngredient().add(castToReference(value)); // Reference
          return value;
        case -1335157162: // device
          this.getDevice().add(castToReference(value)); // Reference
          return value;
        case -1529171400: // characteristics
          this.getCharacteristics().add((MedicinalProductPharmaceuticalCharacteristicsComponent) value); // MedicinalProductPharmaceuticalCharacteristicsComponent
          return value;
        case 1742084734: // routeOfAdministration
          this.getRouteOfAdministration().add((MedicinalProductPharmaceuticalRouteOfAdministrationComponent) value); // MedicinalProductPharmaceuticalRouteOfAdministrationComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier")) {
          this.getIdentifier().add(castToIdentifier(value));
        } else if (name.equals("administrableDoseForm")) {
          this.administrableDoseForm = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("unitOfPresentation")) {
          this.unitOfPresentation = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("ingredient")) {
          this.getIngredient().add(castToReference(value));
        } else if (name.equals("device")) {
          this.getDevice().add(castToReference(value));
        } else if (name.equals("characteristics")) {
          this.getCharacteristics().add((MedicinalProductPharmaceuticalCharacteristicsComponent) value);
        } else if (name.equals("routeOfAdministration")) {
          this.getRouteOfAdministration().add((MedicinalProductPharmaceuticalRouteOfAdministrationComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return addIdentifier(); 
        case 1446105202:  return getAdministrableDoseForm(); 
        case -1427765963:  return getUnitOfPresentation(); 
        case -206409263:  return addIngredient(); 
        case -1335157162:  return addDevice(); 
        case -1529171400:  return addCharacteristics(); 
        case 1742084734:  return addRouteOfAdministration(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case 1446105202: /*administrableDoseForm*/ return new String[] {"CodeableConcept"};
        case -1427765963: /*unitOfPresentation*/ return new String[] {"CodeableConcept"};
        case -206409263: /*ingredient*/ return new String[] {"Reference"};
        case -1335157162: /*device*/ return new String[] {"Reference"};
        case -1529171400: /*characteristics*/ return new String[] {};
        case 1742084734: /*routeOfAdministration*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("administrableDoseForm")) {
          this.administrableDoseForm = new CodeableConcept();
          return this.administrableDoseForm;
        }
        else if (name.equals("unitOfPresentation")) {
          this.unitOfPresentation = new CodeableConcept();
          return this.unitOfPresentation;
        }
        else if (name.equals("ingredient")) {
          return addIngredient();
        }
        else if (name.equals("device")) {
          return addDevice();
        }
        else if (name.equals("characteristics")) {
          return addCharacteristics();
        }
        else if (name.equals("routeOfAdministration")) {
          return addRouteOfAdministration();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "MedicinalProductPharmaceutical";

  }

      public MedicinalProductPharmaceutical copy() {
        MedicinalProductPharmaceutical dst = new MedicinalProductPharmaceutical();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.administrableDoseForm = administrableDoseForm == null ? null : administrableDoseForm.copy();
        dst.unitOfPresentation = unitOfPresentation == null ? null : unitOfPresentation.copy();
        if (ingredient != null) {
          dst.ingredient = new ArrayList<Reference>();
          for (Reference i : ingredient)
            dst.ingredient.add(i.copy());
        };
        if (device != null) {
          dst.device = new ArrayList<Reference>();
          for (Reference i : device)
            dst.device.add(i.copy());
        };
        if (characteristics != null) {
          dst.characteristics = new ArrayList<MedicinalProductPharmaceuticalCharacteristicsComponent>();
          for (MedicinalProductPharmaceuticalCharacteristicsComponent i : characteristics)
            dst.characteristics.add(i.copy());
        };
        if (routeOfAdministration != null) {
          dst.routeOfAdministration = new ArrayList<MedicinalProductPharmaceuticalRouteOfAdministrationComponent>();
          for (MedicinalProductPharmaceuticalRouteOfAdministrationComponent i : routeOfAdministration)
            dst.routeOfAdministration.add(i.copy());
        };
        return dst;
      }

      protected MedicinalProductPharmaceutical typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof MedicinalProductPharmaceutical))
          return false;
        MedicinalProductPharmaceutical o = (MedicinalProductPharmaceutical) other_;
        return compareDeep(identifier, o.identifier, true) && compareDeep(administrableDoseForm, o.administrableDoseForm, true)
           && compareDeep(unitOfPresentation, o.unitOfPresentation, true) && compareDeep(ingredient, o.ingredient, true)
           && compareDeep(device, o.device, true) && compareDeep(characteristics, o.characteristics, true)
           && compareDeep(routeOfAdministration, o.routeOfAdministration, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof MedicinalProductPharmaceutical))
          return false;
        MedicinalProductPharmaceutical o = (MedicinalProductPharmaceutical) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, administrableDoseForm
          , unitOfPresentation, ingredient, device, characteristics, routeOfAdministration);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.MedicinalProductPharmaceutical;
   }

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>An identifier for the pharmaceutical medicinal product</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MedicinalProductPharmaceutical.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="MedicinalProductPharmaceutical.identifier", description="An identifier for the pharmaceutical medicinal product", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>An identifier for the pharmaceutical medicinal product</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MedicinalProductPharmaceutical.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>route</b>
   * <p>
   * Description: <b>Coded expression for the route</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MedicinalProductPharmaceutical.routeOfAdministration.code</b><br>
   * </p>
   */
  @SearchParamDefinition(name="route", path="MedicinalProductPharmaceutical.routeOfAdministration.code", description="Coded expression for the route", type="token" )
  public static final String SP_ROUTE = "route";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>route</b>
   * <p>
   * Description: <b>Coded expression for the route</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MedicinalProductPharmaceutical.routeOfAdministration.code</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam ROUTE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_ROUTE);

 /**
   * Search parameter: <b>target-species</b>
   * <p>
   * Description: <b>Coded expression for the species</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MedicinalProductPharmaceutical.routeOfAdministration.targetSpecies.code</b><br>
   * </p>
   */
  @SearchParamDefinition(name="target-species", path="MedicinalProductPharmaceutical.routeOfAdministration.targetSpecies.code", description="Coded expression for the species", type="token" )
  public static final String SP_TARGET_SPECIES = "target-species";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>target-species</b>
   * <p>
   * Description: <b>Coded expression for the species</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MedicinalProductPharmaceutical.routeOfAdministration.targetSpecies.code</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam TARGET_SPECIES = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_TARGET_SPECIES);


}

