package org.hl7.fhir.dstu3.model;

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

// Generated on Mon, Apr 17, 2017 17:38-0400 for FHIR v3.0.1

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
 * A patient's point-in-time immunization and recommendation (i.e. forecasting a patient's immunization eligibility according to a published schedule) with optional supporting justification.
 */
@ResourceDef(name="ImmunizationRecommendation", profile="http://hl7.org/fhir/Profile/ImmunizationRecommendation")
public class ImmunizationRecommendation extends DomainResource {

    @Block()
    public static class ImmunizationRecommendationRecommendationComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The date the immunization recommendation was created.
         */
        @Child(name = "date", type = {DateTimeType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Date recommendation created", formalDefinition="The date the immunization recommendation was created." )
        protected DateTimeType date;

        /**
         * Vaccine that pertains to the recommendation.
         */
        @Child(name = "vaccineCode", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Vaccine recommendation applies to", formalDefinition="Vaccine that pertains to the recommendation." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/vaccine-code")
        protected CodeableConcept vaccineCode;

        /**
         * The targeted disease for the recommendation.
         */
        @Child(name = "targetDisease", type = {CodeableConcept.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Disease to be immunized against", formalDefinition="The targeted disease for the recommendation." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/immunization-recommendation-target-disease")
        protected CodeableConcept targetDisease;

        /**
         * The next recommended dose number (e.g. dose 2 is the next recommended dose).
         */
        @Child(name = "doseNumber", type = {PositiveIntType.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Recommended dose number", formalDefinition="The next recommended dose number (e.g. dose 2 is the next recommended dose)." )
        protected PositiveIntType doseNumber;

        /**
         * Vaccine administration status.
         */
        @Child(name = "forecastStatus", type = {CodeableConcept.class}, order=5, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Vaccine administration status", formalDefinition="Vaccine administration status." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/immunization-recommendation-status")
        protected CodeableConcept forecastStatus;

        /**
         * Vaccine date recommendations.  For example, earliest date to administer, latest date to administer, etc.
         */
        @Child(name = "dateCriterion", type = {}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Dates governing proposed immunization", formalDefinition="Vaccine date recommendations.  For example, earliest date to administer, latest date to administer, etc." )
        protected List<ImmunizationRecommendationRecommendationDateCriterionComponent> dateCriterion;

        /**
         * Contains information about the protocol under which the vaccine was administered.
         */
        @Child(name = "protocol", type = {}, order=7, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Protocol used by recommendation", formalDefinition="Contains information about the protocol under which the vaccine was administered." )
        protected ImmunizationRecommendationRecommendationProtocolComponent protocol;

        /**
         * Immunization event history that supports the status and recommendation.
         */
        @Child(name = "supportingImmunization", type = {Immunization.class}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Past immunizations supporting recommendation", formalDefinition="Immunization event history that supports the status and recommendation." )
        protected List<Reference> supportingImmunization;
        /**
         * The actual objects that are the target of the reference (Immunization event history that supports the status and recommendation.)
         */
        protected List<Immunization> supportingImmunizationTarget;


        /**
         * Patient Information that supports the status and recommendation.  This includes patient observations, adverse reactions and allergy/intolerance information.
         */
        @Child(name = "supportingPatientInformation", type = {Observation.class, AllergyIntolerance.class}, order=9, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Patient observations supporting recommendation", formalDefinition="Patient Information that supports the status and recommendation.  This includes patient observations, adverse reactions and allergy/intolerance information." )
        protected List<Reference> supportingPatientInformation;
        /**
         * The actual objects that are the target of the reference (Patient Information that supports the status and recommendation.  This includes patient observations, adverse reactions and allergy/intolerance information.)
         */
        protected List<Resource> supportingPatientInformationTarget;


        private static final long serialVersionUID = 1279700888L;

    /**
     * Constructor
     */
      public ImmunizationRecommendationRecommendationComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ImmunizationRecommendationRecommendationComponent(DateTimeType date, CodeableConcept forecastStatus) {
        super();
        this.date = date;
        this.forecastStatus = forecastStatus;
      }

        /**
         * @return {@link #date} (The date the immunization recommendation was created.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
         */
        public DateTimeType getDateElement() { 
          if (this.date == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImmunizationRecommendationRecommendationComponent.date");
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
         * @param value {@link #date} (The date the immunization recommendation was created.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
         */
        public ImmunizationRecommendationRecommendationComponent setDateElement(DateTimeType value) { 
          this.date = value;
          return this;
        }

        /**
         * @return The date the immunization recommendation was created.
         */
        public Date getDate() { 
          return this.date == null ? null : this.date.getValue();
        }

        /**
         * @param value The date the immunization recommendation was created.
         */
        public ImmunizationRecommendationRecommendationComponent setDate(Date value) { 
            if (this.date == null)
              this.date = new DateTimeType();
            this.date.setValue(value);
          return this;
        }

        /**
         * @return {@link #vaccineCode} (Vaccine that pertains to the recommendation.)
         */
        public CodeableConcept getVaccineCode() { 
          if (this.vaccineCode == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImmunizationRecommendationRecommendationComponent.vaccineCode");
            else if (Configuration.doAutoCreate())
              this.vaccineCode = new CodeableConcept(); // cc
          return this.vaccineCode;
        }

        public boolean hasVaccineCode() { 
          return this.vaccineCode != null && !this.vaccineCode.isEmpty();
        }

        /**
         * @param value {@link #vaccineCode} (Vaccine that pertains to the recommendation.)
         */
        public ImmunizationRecommendationRecommendationComponent setVaccineCode(CodeableConcept value) { 
          this.vaccineCode = value;
          return this;
        }

        /**
         * @return {@link #targetDisease} (The targeted disease for the recommendation.)
         */
        public CodeableConcept getTargetDisease() { 
          if (this.targetDisease == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImmunizationRecommendationRecommendationComponent.targetDisease");
            else if (Configuration.doAutoCreate())
              this.targetDisease = new CodeableConcept(); // cc
          return this.targetDisease;
        }

        public boolean hasTargetDisease() { 
          return this.targetDisease != null && !this.targetDisease.isEmpty();
        }

        /**
         * @param value {@link #targetDisease} (The targeted disease for the recommendation.)
         */
        public ImmunizationRecommendationRecommendationComponent setTargetDisease(CodeableConcept value) { 
          this.targetDisease = value;
          return this;
        }

        /**
         * @return {@link #doseNumber} (The next recommended dose number (e.g. dose 2 is the next recommended dose).). This is the underlying object with id, value and extensions. The accessor "getDoseNumber" gives direct access to the value
         */
        public PositiveIntType getDoseNumberElement() { 
          if (this.doseNumber == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImmunizationRecommendationRecommendationComponent.doseNumber");
            else if (Configuration.doAutoCreate())
              this.doseNumber = new PositiveIntType(); // bb
          return this.doseNumber;
        }

        public boolean hasDoseNumberElement() { 
          return this.doseNumber != null && !this.doseNumber.isEmpty();
        }

        public boolean hasDoseNumber() { 
          return this.doseNumber != null && !this.doseNumber.isEmpty();
        }

        /**
         * @param value {@link #doseNumber} (The next recommended dose number (e.g. dose 2 is the next recommended dose).). This is the underlying object with id, value and extensions. The accessor "getDoseNumber" gives direct access to the value
         */
        public ImmunizationRecommendationRecommendationComponent setDoseNumberElement(PositiveIntType value) { 
          this.doseNumber = value;
          return this;
        }

        /**
         * @return The next recommended dose number (e.g. dose 2 is the next recommended dose).
         */
        public int getDoseNumber() { 
          return this.doseNumber == null || this.doseNumber.isEmpty() ? 0 : this.doseNumber.getValue();
        }

        /**
         * @param value The next recommended dose number (e.g. dose 2 is the next recommended dose).
         */
        public ImmunizationRecommendationRecommendationComponent setDoseNumber(int value) { 
            if (this.doseNumber == null)
              this.doseNumber = new PositiveIntType();
            this.doseNumber.setValue(value);
          return this;
        }

        /**
         * @return {@link #forecastStatus} (Vaccine administration status.)
         */
        public CodeableConcept getForecastStatus() { 
          if (this.forecastStatus == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImmunizationRecommendationRecommendationComponent.forecastStatus");
            else if (Configuration.doAutoCreate())
              this.forecastStatus = new CodeableConcept(); // cc
          return this.forecastStatus;
        }

        public boolean hasForecastStatus() { 
          return this.forecastStatus != null && !this.forecastStatus.isEmpty();
        }

        /**
         * @param value {@link #forecastStatus} (Vaccine administration status.)
         */
        public ImmunizationRecommendationRecommendationComponent setForecastStatus(CodeableConcept value) { 
          this.forecastStatus = value;
          return this;
        }

        /**
         * @return {@link #dateCriterion} (Vaccine date recommendations.  For example, earliest date to administer, latest date to administer, etc.)
         */
        public List<ImmunizationRecommendationRecommendationDateCriterionComponent> getDateCriterion() { 
          if (this.dateCriterion == null)
            this.dateCriterion = new ArrayList<ImmunizationRecommendationRecommendationDateCriterionComponent>();
          return this.dateCriterion;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ImmunizationRecommendationRecommendationComponent setDateCriterion(List<ImmunizationRecommendationRecommendationDateCriterionComponent> theDateCriterion) { 
          this.dateCriterion = theDateCriterion;
          return this;
        }

        public boolean hasDateCriterion() { 
          if (this.dateCriterion == null)
            return false;
          for (ImmunizationRecommendationRecommendationDateCriterionComponent item : this.dateCriterion)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public ImmunizationRecommendationRecommendationDateCriterionComponent addDateCriterion() { //3
          ImmunizationRecommendationRecommendationDateCriterionComponent t = new ImmunizationRecommendationRecommendationDateCriterionComponent();
          if (this.dateCriterion == null)
            this.dateCriterion = new ArrayList<ImmunizationRecommendationRecommendationDateCriterionComponent>();
          this.dateCriterion.add(t);
          return t;
        }

        public ImmunizationRecommendationRecommendationComponent addDateCriterion(ImmunizationRecommendationRecommendationDateCriterionComponent t) { //3
          if (t == null)
            return this;
          if (this.dateCriterion == null)
            this.dateCriterion = new ArrayList<ImmunizationRecommendationRecommendationDateCriterionComponent>();
          this.dateCriterion.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #dateCriterion}, creating it if it does not already exist
         */
        public ImmunizationRecommendationRecommendationDateCriterionComponent getDateCriterionFirstRep() { 
          if (getDateCriterion().isEmpty()) {
            addDateCriterion();
          }
          return getDateCriterion().get(0);
        }

        /**
         * @return {@link #protocol} (Contains information about the protocol under which the vaccine was administered.)
         */
        public ImmunizationRecommendationRecommendationProtocolComponent getProtocol() { 
          if (this.protocol == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImmunizationRecommendationRecommendationComponent.protocol");
            else if (Configuration.doAutoCreate())
              this.protocol = new ImmunizationRecommendationRecommendationProtocolComponent(); // cc
          return this.protocol;
        }

        public boolean hasProtocol() { 
          return this.protocol != null && !this.protocol.isEmpty();
        }

        /**
         * @param value {@link #protocol} (Contains information about the protocol under which the vaccine was administered.)
         */
        public ImmunizationRecommendationRecommendationComponent setProtocol(ImmunizationRecommendationRecommendationProtocolComponent value) { 
          this.protocol = value;
          return this;
        }

        /**
         * @return {@link #supportingImmunization} (Immunization event history that supports the status and recommendation.)
         */
        public List<Reference> getSupportingImmunization() { 
          if (this.supportingImmunization == null)
            this.supportingImmunization = new ArrayList<Reference>();
          return this.supportingImmunization;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ImmunizationRecommendationRecommendationComponent setSupportingImmunization(List<Reference> theSupportingImmunization) { 
          this.supportingImmunization = theSupportingImmunization;
          return this;
        }

        public boolean hasSupportingImmunization() { 
          if (this.supportingImmunization == null)
            return false;
          for (Reference item : this.supportingImmunization)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public Reference addSupportingImmunization() { //3
          Reference t = new Reference();
          if (this.supportingImmunization == null)
            this.supportingImmunization = new ArrayList<Reference>();
          this.supportingImmunization.add(t);
          return t;
        }

        public ImmunizationRecommendationRecommendationComponent addSupportingImmunization(Reference t) { //3
          if (t == null)
            return this;
          if (this.supportingImmunization == null)
            this.supportingImmunization = new ArrayList<Reference>();
          this.supportingImmunization.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #supportingImmunization}, creating it if it does not already exist
         */
        public Reference getSupportingImmunizationFirstRep() { 
          if (getSupportingImmunization().isEmpty()) {
            addSupportingImmunization();
          }
          return getSupportingImmunization().get(0);
        }

        /**
         * @deprecated Use Reference#setResource(IBaseResource) instead
         */
        @Deprecated
        public List<Immunization> getSupportingImmunizationTarget() { 
          if (this.supportingImmunizationTarget == null)
            this.supportingImmunizationTarget = new ArrayList<Immunization>();
          return this.supportingImmunizationTarget;
        }

        /**
         * @deprecated Use Reference#setResource(IBaseResource) instead
         */
        @Deprecated
        public Immunization addSupportingImmunizationTarget() { 
          Immunization r = new Immunization();
          if (this.supportingImmunizationTarget == null)
            this.supportingImmunizationTarget = new ArrayList<Immunization>();
          this.supportingImmunizationTarget.add(r);
          return r;
        }

        /**
         * @return {@link #supportingPatientInformation} (Patient Information that supports the status and recommendation.  This includes patient observations, adverse reactions and allergy/intolerance information.)
         */
        public List<Reference> getSupportingPatientInformation() { 
          if (this.supportingPatientInformation == null)
            this.supportingPatientInformation = new ArrayList<Reference>();
          return this.supportingPatientInformation;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ImmunizationRecommendationRecommendationComponent setSupportingPatientInformation(List<Reference> theSupportingPatientInformation) { 
          this.supportingPatientInformation = theSupportingPatientInformation;
          return this;
        }

        public boolean hasSupportingPatientInformation() { 
          if (this.supportingPatientInformation == null)
            return false;
          for (Reference item : this.supportingPatientInformation)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public Reference addSupportingPatientInformation() { //3
          Reference t = new Reference();
          if (this.supportingPatientInformation == null)
            this.supportingPatientInformation = new ArrayList<Reference>();
          this.supportingPatientInformation.add(t);
          return t;
        }

        public ImmunizationRecommendationRecommendationComponent addSupportingPatientInformation(Reference t) { //3
          if (t == null)
            return this;
          if (this.supportingPatientInformation == null)
            this.supportingPatientInformation = new ArrayList<Reference>();
          this.supportingPatientInformation.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #supportingPatientInformation}, creating it if it does not already exist
         */
        public Reference getSupportingPatientInformationFirstRep() { 
          if (getSupportingPatientInformation().isEmpty()) {
            addSupportingPatientInformation();
          }
          return getSupportingPatientInformation().get(0);
        }

        /**
         * @deprecated Use Reference#setResource(IBaseResource) instead
         */
        @Deprecated
        public List<Resource> getSupportingPatientInformationTarget() { 
          if (this.supportingPatientInformationTarget == null)
            this.supportingPatientInformationTarget = new ArrayList<Resource>();
          return this.supportingPatientInformationTarget;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("date", "dateTime", "The date the immunization recommendation was created.", 0, java.lang.Integer.MAX_VALUE, date));
          childrenList.add(new Property("vaccineCode", "CodeableConcept", "Vaccine that pertains to the recommendation.", 0, java.lang.Integer.MAX_VALUE, vaccineCode));
          childrenList.add(new Property("targetDisease", "CodeableConcept", "The targeted disease for the recommendation.", 0, java.lang.Integer.MAX_VALUE, targetDisease));
          childrenList.add(new Property("doseNumber", "positiveInt", "The next recommended dose number (e.g. dose 2 is the next recommended dose).", 0, java.lang.Integer.MAX_VALUE, doseNumber));
          childrenList.add(new Property("forecastStatus", "CodeableConcept", "Vaccine administration status.", 0, java.lang.Integer.MAX_VALUE, forecastStatus));
          childrenList.add(new Property("dateCriterion", "", "Vaccine date recommendations.  For example, earliest date to administer, latest date to administer, etc.", 0, java.lang.Integer.MAX_VALUE, dateCriterion));
          childrenList.add(new Property("protocol", "", "Contains information about the protocol under which the vaccine was administered.", 0, java.lang.Integer.MAX_VALUE, protocol));
          childrenList.add(new Property("supportingImmunization", "Reference(Immunization)", "Immunization event history that supports the status and recommendation.", 0, java.lang.Integer.MAX_VALUE, supportingImmunization));
          childrenList.add(new Property("supportingPatientInformation", "Reference(Observation|AllergyIntolerance)", "Patient Information that supports the status and recommendation.  This includes patient observations, adverse reactions and allergy/intolerance information.", 0, java.lang.Integer.MAX_VALUE, supportingPatientInformation));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3076014: /*date*/ return this.date == null ? new Base[0] : new Base[] {this.date}; // DateTimeType
        case 664556354: /*vaccineCode*/ return this.vaccineCode == null ? new Base[0] : new Base[] {this.vaccineCode}; // CodeableConcept
        case -319593813: /*targetDisease*/ return this.targetDisease == null ? new Base[0] : new Base[] {this.targetDisease}; // CodeableConcept
        case -887709242: /*doseNumber*/ return this.doseNumber == null ? new Base[0] : new Base[] {this.doseNumber}; // PositiveIntType
        case 1904598477: /*forecastStatus*/ return this.forecastStatus == null ? new Base[0] : new Base[] {this.forecastStatus}; // CodeableConcept
        case 2087518867: /*dateCriterion*/ return this.dateCriterion == null ? new Base[0] : this.dateCriterion.toArray(new Base[this.dateCriterion.size()]); // ImmunizationRecommendationRecommendationDateCriterionComponent
        case -989163880: /*protocol*/ return this.protocol == null ? new Base[0] : new Base[] {this.protocol}; // ImmunizationRecommendationRecommendationProtocolComponent
        case 1171592021: /*supportingImmunization*/ return this.supportingImmunization == null ? new Base[0] : this.supportingImmunization.toArray(new Base[this.supportingImmunization.size()]); // Reference
        case -1234160646: /*supportingPatientInformation*/ return this.supportingPatientInformation == null ? new Base[0] : this.supportingPatientInformation.toArray(new Base[this.supportingPatientInformation.size()]); // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3076014: // date
          this.date = castToDateTime(value); // DateTimeType
          return value;
        case 664556354: // vaccineCode
          this.vaccineCode = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -319593813: // targetDisease
          this.targetDisease = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -887709242: // doseNumber
          this.doseNumber = castToPositiveInt(value); // PositiveIntType
          return value;
        case 1904598477: // forecastStatus
          this.forecastStatus = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 2087518867: // dateCriterion
          this.getDateCriterion().add((ImmunizationRecommendationRecommendationDateCriterionComponent) value); // ImmunizationRecommendationRecommendationDateCriterionComponent
          return value;
        case -989163880: // protocol
          this.protocol = (ImmunizationRecommendationRecommendationProtocolComponent) value; // ImmunizationRecommendationRecommendationProtocolComponent
          return value;
        case 1171592021: // supportingImmunization
          this.getSupportingImmunization().add(castToReference(value)); // Reference
          return value;
        case -1234160646: // supportingPatientInformation
          this.getSupportingPatientInformation().add(castToReference(value)); // Reference
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("date")) {
          this.date = castToDateTime(value); // DateTimeType
        } else if (name.equals("vaccineCode")) {
          this.vaccineCode = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("targetDisease")) {
          this.targetDisease = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("doseNumber")) {
          this.doseNumber = castToPositiveInt(value); // PositiveIntType
        } else if (name.equals("forecastStatus")) {
          this.forecastStatus = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("dateCriterion")) {
          this.getDateCriterion().add((ImmunizationRecommendationRecommendationDateCriterionComponent) value);
        } else if (name.equals("protocol")) {
          this.protocol = (ImmunizationRecommendationRecommendationProtocolComponent) value; // ImmunizationRecommendationRecommendationProtocolComponent
        } else if (name.equals("supportingImmunization")) {
          this.getSupportingImmunization().add(castToReference(value));
        } else if (name.equals("supportingPatientInformation")) {
          this.getSupportingPatientInformation().add(castToReference(value));
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3076014:  return getDateElement();
        case 664556354:  return getVaccineCode(); 
        case -319593813:  return getTargetDisease(); 
        case -887709242:  return getDoseNumberElement();
        case 1904598477:  return getForecastStatus(); 
        case 2087518867:  return addDateCriterion(); 
        case -989163880:  return getProtocol(); 
        case 1171592021:  return addSupportingImmunization(); 
        case -1234160646:  return addSupportingPatientInformation(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3076014: /*date*/ return new String[] {"dateTime"};
        case 664556354: /*vaccineCode*/ return new String[] {"CodeableConcept"};
        case -319593813: /*targetDisease*/ return new String[] {"CodeableConcept"};
        case -887709242: /*doseNumber*/ return new String[] {"positiveInt"};
        case 1904598477: /*forecastStatus*/ return new String[] {"CodeableConcept"};
        case 2087518867: /*dateCriterion*/ return new String[] {};
        case -989163880: /*protocol*/ return new String[] {};
        case 1171592021: /*supportingImmunization*/ return new String[] {"Reference"};
        case -1234160646: /*supportingPatientInformation*/ return new String[] {"Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("date")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImmunizationRecommendation.date");
        }
        else if (name.equals("vaccineCode")) {
          this.vaccineCode = new CodeableConcept();
          return this.vaccineCode;
        }
        else if (name.equals("targetDisease")) {
          this.targetDisease = new CodeableConcept();
          return this.targetDisease;
        }
        else if (name.equals("doseNumber")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImmunizationRecommendation.doseNumber");
        }
        else if (name.equals("forecastStatus")) {
          this.forecastStatus = new CodeableConcept();
          return this.forecastStatus;
        }
        else if (name.equals("dateCriterion")) {
          return addDateCriterion();
        }
        else if (name.equals("protocol")) {
          this.protocol = new ImmunizationRecommendationRecommendationProtocolComponent();
          return this.protocol;
        }
        else if (name.equals("supportingImmunization")) {
          return addSupportingImmunization();
        }
        else if (name.equals("supportingPatientInformation")) {
          return addSupportingPatientInformation();
        }
        else
          return super.addChild(name);
      }

      public ImmunizationRecommendationRecommendationComponent copy() {
        ImmunizationRecommendationRecommendationComponent dst = new ImmunizationRecommendationRecommendationComponent();
        copyValues(dst);
        dst.date = date == null ? null : date.copy();
        dst.vaccineCode = vaccineCode == null ? null : vaccineCode.copy();
        dst.targetDisease = targetDisease == null ? null : targetDisease.copy();
        dst.doseNumber = doseNumber == null ? null : doseNumber.copy();
        dst.forecastStatus = forecastStatus == null ? null : forecastStatus.copy();
        if (dateCriterion != null) {
          dst.dateCriterion = new ArrayList<ImmunizationRecommendationRecommendationDateCriterionComponent>();
          for (ImmunizationRecommendationRecommendationDateCriterionComponent i : dateCriterion)
            dst.dateCriterion.add(i.copy());
        };
        dst.protocol = protocol == null ? null : protocol.copy();
        if (supportingImmunization != null) {
          dst.supportingImmunization = new ArrayList<Reference>();
          for (Reference i : supportingImmunization)
            dst.supportingImmunization.add(i.copy());
        };
        if (supportingPatientInformation != null) {
          dst.supportingPatientInformation = new ArrayList<Reference>();
          for (Reference i : supportingPatientInformation)
            dst.supportingPatientInformation.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ImmunizationRecommendationRecommendationComponent))
          return false;
        ImmunizationRecommendationRecommendationComponent o = (ImmunizationRecommendationRecommendationComponent) other;
        return compareDeep(date, o.date, true) && compareDeep(vaccineCode, o.vaccineCode, true) && compareDeep(targetDisease, o.targetDisease, true)
           && compareDeep(doseNumber, o.doseNumber, true) && compareDeep(forecastStatus, o.forecastStatus, true)
           && compareDeep(dateCriterion, o.dateCriterion, true) && compareDeep(protocol, o.protocol, true)
           && compareDeep(supportingImmunization, o.supportingImmunization, true) && compareDeep(supportingPatientInformation, o.supportingPatientInformation, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ImmunizationRecommendationRecommendationComponent))
          return false;
        ImmunizationRecommendationRecommendationComponent o = (ImmunizationRecommendationRecommendationComponent) other;
        return compareValues(date, o.date, true) && compareValues(doseNumber, o.doseNumber, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(date, vaccineCode, targetDisease
          , doseNumber, forecastStatus, dateCriterion, protocol, supportingImmunization, supportingPatientInformation
          );
      }

  public String fhirType() {
    return "ImmunizationRecommendation.recommendation";

  }

  }

    @Block()
    public static class ImmunizationRecommendationRecommendationDateCriterionComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Date classification of recommendation.  For example, earliest date to give, latest date to give, etc.
         */
        @Child(name = "code", type = {CodeableConcept.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Type of date", formalDefinition="Date classification of recommendation.  For example, earliest date to give, latest date to give, etc." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/immunization-recommendation-date-criterion")
        protected CodeableConcept code;

        /**
         * The date whose meaning is specified by dateCriterion.code.
         */
        @Child(name = "value", type = {DateTimeType.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Recommended date", formalDefinition="The date whose meaning is specified by dateCriterion.code." )
        protected DateTimeType value;

        private static final long serialVersionUID = 1036994566L;

    /**
     * Constructor
     */
      public ImmunizationRecommendationRecommendationDateCriterionComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ImmunizationRecommendationRecommendationDateCriterionComponent(CodeableConcept code, DateTimeType value) {
        super();
        this.code = code;
        this.value = value;
      }

        /**
         * @return {@link #code} (Date classification of recommendation.  For example, earliest date to give, latest date to give, etc.)
         */
        public CodeableConcept getCode() { 
          if (this.code == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImmunizationRecommendationRecommendationDateCriterionComponent.code");
            else if (Configuration.doAutoCreate())
              this.code = new CodeableConcept(); // cc
          return this.code;
        }

        public boolean hasCode() { 
          return this.code != null && !this.code.isEmpty();
        }

        /**
         * @param value {@link #code} (Date classification of recommendation.  For example, earliest date to give, latest date to give, etc.)
         */
        public ImmunizationRecommendationRecommendationDateCriterionComponent setCode(CodeableConcept value) { 
          this.code = value;
          return this;
        }

        /**
         * @return {@link #value} (The date whose meaning is specified by dateCriterion.code.). This is the underlying object with id, value and extensions. The accessor "getValue" gives direct access to the value
         */
        public DateTimeType getValueElement() { 
          if (this.value == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImmunizationRecommendationRecommendationDateCriterionComponent.value");
            else if (Configuration.doAutoCreate())
              this.value = new DateTimeType(); // bb
          return this.value;
        }

        public boolean hasValueElement() { 
          return this.value != null && !this.value.isEmpty();
        }

        public boolean hasValue() { 
          return this.value != null && !this.value.isEmpty();
        }

        /**
         * @param value {@link #value} (The date whose meaning is specified by dateCriterion.code.). This is the underlying object with id, value and extensions. The accessor "getValue" gives direct access to the value
         */
        public ImmunizationRecommendationRecommendationDateCriterionComponent setValueElement(DateTimeType value) { 
          this.value = value;
          return this;
        }

        /**
         * @return The date whose meaning is specified by dateCriterion.code.
         */
        public Date getValue() { 
          return this.value == null ? null : this.value.getValue();
        }

        /**
         * @param value The date whose meaning is specified by dateCriterion.code.
         */
        public ImmunizationRecommendationRecommendationDateCriterionComponent setValue(Date value) { 
            if (this.value == null)
              this.value = new DateTimeType();
            this.value.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("code", "CodeableConcept", "Date classification of recommendation.  For example, earliest date to give, latest date to give, etc.", 0, java.lang.Integer.MAX_VALUE, code));
          childrenList.add(new Property("value", "dateTime", "The date whose meaning is specified by dateCriterion.code.", 0, java.lang.Integer.MAX_VALUE, value));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3059181: /*code*/ return this.code == null ? new Base[0] : new Base[] {this.code}; // CodeableConcept
        case 111972721: /*value*/ return this.value == null ? new Base[0] : new Base[] {this.value}; // DateTimeType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3059181: // code
          this.code = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 111972721: // value
          this.value = castToDateTime(value); // DateTimeType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("code")) {
          this.code = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("value")) {
          this.value = castToDateTime(value); // DateTimeType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3059181:  return getCode(); 
        case 111972721:  return getValueElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3059181: /*code*/ return new String[] {"CodeableConcept"};
        case 111972721: /*value*/ return new String[] {"dateTime"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("code")) {
          this.code = new CodeableConcept();
          return this.code;
        }
        else if (name.equals("value")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImmunizationRecommendation.value");
        }
        else
          return super.addChild(name);
      }

      public ImmunizationRecommendationRecommendationDateCriterionComponent copy() {
        ImmunizationRecommendationRecommendationDateCriterionComponent dst = new ImmunizationRecommendationRecommendationDateCriterionComponent();
        copyValues(dst);
        dst.code = code == null ? null : code.copy();
        dst.value = value == null ? null : value.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ImmunizationRecommendationRecommendationDateCriterionComponent))
          return false;
        ImmunizationRecommendationRecommendationDateCriterionComponent o = (ImmunizationRecommendationRecommendationDateCriterionComponent) other;
        return compareDeep(code, o.code, true) && compareDeep(value, o.value, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ImmunizationRecommendationRecommendationDateCriterionComponent))
          return false;
        ImmunizationRecommendationRecommendationDateCriterionComponent o = (ImmunizationRecommendationRecommendationDateCriterionComponent) other;
        return compareValues(value, o.value, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(code, value);
      }

  public String fhirType() {
    return "ImmunizationRecommendation.recommendation.dateCriterion";

  }

  }

    @Block()
    public static class ImmunizationRecommendationRecommendationProtocolComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Indicates the nominal position in a series of the next dose.  This is the recommended dose number as per a specified protocol.
         */
        @Child(name = "doseSequence", type = {PositiveIntType.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Dose number within sequence", formalDefinition="Indicates the nominal position in a series of the next dose.  This is the recommended dose number as per a specified protocol." )
        protected PositiveIntType doseSequence;

        /**
         * Contains the description about the protocol under which the vaccine was administered.
         */
        @Child(name = "description", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Protocol details", formalDefinition="Contains the description about the protocol under which the vaccine was administered." )
        protected StringType description;

        /**
         * Indicates the authority who published the protocol.  For example, ACIP.
         */
        @Child(name = "authority", type = {Organization.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Who is responsible for protocol", formalDefinition="Indicates the authority who published the protocol.  For example, ACIP." )
        protected Reference authority;

        /**
         * The actual object that is the target of the reference (Indicates the authority who published the protocol.  For example, ACIP.)
         */
        protected Organization authorityTarget;

        /**
         * One possible path to achieve presumed immunity against a disease - within the context of an authority.
         */
        @Child(name = "series", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Name of vaccination series", formalDefinition="One possible path to achieve presumed immunity against a disease - within the context of an authority." )
        protected StringType series;

        private static final long serialVersionUID = 215094970L;

    /**
     * Constructor
     */
      public ImmunizationRecommendationRecommendationProtocolComponent() {
        super();
      }

        /**
         * @return {@link #doseSequence} (Indicates the nominal position in a series of the next dose.  This is the recommended dose number as per a specified protocol.). This is the underlying object with id, value and extensions. The accessor "getDoseSequence" gives direct access to the value
         */
        public PositiveIntType getDoseSequenceElement() { 
          if (this.doseSequence == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImmunizationRecommendationRecommendationProtocolComponent.doseSequence");
            else if (Configuration.doAutoCreate())
              this.doseSequence = new PositiveIntType(); // bb
          return this.doseSequence;
        }

        public boolean hasDoseSequenceElement() { 
          return this.doseSequence != null && !this.doseSequence.isEmpty();
        }

        public boolean hasDoseSequence() { 
          return this.doseSequence != null && !this.doseSequence.isEmpty();
        }

        /**
         * @param value {@link #doseSequence} (Indicates the nominal position in a series of the next dose.  This is the recommended dose number as per a specified protocol.). This is the underlying object with id, value and extensions. The accessor "getDoseSequence" gives direct access to the value
         */
        public ImmunizationRecommendationRecommendationProtocolComponent setDoseSequenceElement(PositiveIntType value) { 
          this.doseSequence = value;
          return this;
        }

        /**
         * @return Indicates the nominal position in a series of the next dose.  This is the recommended dose number as per a specified protocol.
         */
        public int getDoseSequence() { 
          return this.doseSequence == null || this.doseSequence.isEmpty() ? 0 : this.doseSequence.getValue();
        }

        /**
         * @param value Indicates the nominal position in a series of the next dose.  This is the recommended dose number as per a specified protocol.
         */
        public ImmunizationRecommendationRecommendationProtocolComponent setDoseSequence(int value) { 
            if (this.doseSequence == null)
              this.doseSequence = new PositiveIntType();
            this.doseSequence.setValue(value);
          return this;
        }

        /**
         * @return {@link #description} (Contains the description about the protocol under which the vaccine was administered.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public StringType getDescriptionElement() { 
          if (this.description == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImmunizationRecommendationRecommendationProtocolComponent.description");
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
         * @param value {@link #description} (Contains the description about the protocol under which the vaccine was administered.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public ImmunizationRecommendationRecommendationProtocolComponent setDescriptionElement(StringType value) { 
          this.description = value;
          return this;
        }

        /**
         * @return Contains the description about the protocol under which the vaccine was administered.
         */
        public String getDescription() { 
          return this.description == null ? null : this.description.getValue();
        }

        /**
         * @param value Contains the description about the protocol under which the vaccine was administered.
         */
        public ImmunizationRecommendationRecommendationProtocolComponent setDescription(String value) { 
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
         * @return {@link #authority} (Indicates the authority who published the protocol.  For example, ACIP.)
         */
        public Reference getAuthority() { 
          if (this.authority == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImmunizationRecommendationRecommendationProtocolComponent.authority");
            else if (Configuration.doAutoCreate())
              this.authority = new Reference(); // cc
          return this.authority;
        }

        public boolean hasAuthority() { 
          return this.authority != null && !this.authority.isEmpty();
        }

        /**
         * @param value {@link #authority} (Indicates the authority who published the protocol.  For example, ACIP.)
         */
        public ImmunizationRecommendationRecommendationProtocolComponent setAuthority(Reference value) { 
          this.authority = value;
          return this;
        }

        /**
         * @return {@link #authority} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Indicates the authority who published the protocol.  For example, ACIP.)
         */
        public Organization getAuthorityTarget() { 
          if (this.authorityTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImmunizationRecommendationRecommendationProtocolComponent.authority");
            else if (Configuration.doAutoCreate())
              this.authorityTarget = new Organization(); // aa
          return this.authorityTarget;
        }

        /**
         * @param value {@link #authority} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Indicates the authority who published the protocol.  For example, ACIP.)
         */
        public ImmunizationRecommendationRecommendationProtocolComponent setAuthorityTarget(Organization value) { 
          this.authorityTarget = value;
          return this;
        }

        /**
         * @return {@link #series} (One possible path to achieve presumed immunity against a disease - within the context of an authority.). This is the underlying object with id, value and extensions. The accessor "getSeries" gives direct access to the value
         */
        public StringType getSeriesElement() { 
          if (this.series == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImmunizationRecommendationRecommendationProtocolComponent.series");
            else if (Configuration.doAutoCreate())
              this.series = new StringType(); // bb
          return this.series;
        }

        public boolean hasSeriesElement() { 
          return this.series != null && !this.series.isEmpty();
        }

        public boolean hasSeries() { 
          return this.series != null && !this.series.isEmpty();
        }

        /**
         * @param value {@link #series} (One possible path to achieve presumed immunity against a disease - within the context of an authority.). This is the underlying object with id, value and extensions. The accessor "getSeries" gives direct access to the value
         */
        public ImmunizationRecommendationRecommendationProtocolComponent setSeriesElement(StringType value) { 
          this.series = value;
          return this;
        }

        /**
         * @return One possible path to achieve presumed immunity against a disease - within the context of an authority.
         */
        public String getSeries() { 
          return this.series == null ? null : this.series.getValue();
        }

        /**
         * @param value One possible path to achieve presumed immunity against a disease - within the context of an authority.
         */
        public ImmunizationRecommendationRecommendationProtocolComponent setSeries(String value) { 
          if (Utilities.noString(value))
            this.series = null;
          else {
            if (this.series == null)
              this.series = new StringType();
            this.series.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("doseSequence", "positiveInt", "Indicates the nominal position in a series of the next dose.  This is the recommended dose number as per a specified protocol.", 0, java.lang.Integer.MAX_VALUE, doseSequence));
          childrenList.add(new Property("description", "string", "Contains the description about the protocol under which the vaccine was administered.", 0, java.lang.Integer.MAX_VALUE, description));
          childrenList.add(new Property("authority", "Reference(Organization)", "Indicates the authority who published the protocol.  For example, ACIP.", 0, java.lang.Integer.MAX_VALUE, authority));
          childrenList.add(new Property("series", "string", "One possible path to achieve presumed immunity against a disease - within the context of an authority.", 0, java.lang.Integer.MAX_VALUE, series));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 550933246: /*doseSequence*/ return this.doseSequence == null ? new Base[0] : new Base[] {this.doseSequence}; // PositiveIntType
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // StringType
        case 1475610435: /*authority*/ return this.authority == null ? new Base[0] : new Base[] {this.authority}; // Reference
        case -905838985: /*series*/ return this.series == null ? new Base[0] : new Base[] {this.series}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 550933246: // doseSequence
          this.doseSequence = castToPositiveInt(value); // PositiveIntType
          return value;
        case -1724546052: // description
          this.description = castToString(value); // StringType
          return value;
        case 1475610435: // authority
          this.authority = castToReference(value); // Reference
          return value;
        case -905838985: // series
          this.series = castToString(value); // StringType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("doseSequence")) {
          this.doseSequence = castToPositiveInt(value); // PositiveIntType
        } else if (name.equals("description")) {
          this.description = castToString(value); // StringType
        } else if (name.equals("authority")) {
          this.authority = castToReference(value); // Reference
        } else if (name.equals("series")) {
          this.series = castToString(value); // StringType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 550933246:  return getDoseSequenceElement();
        case -1724546052:  return getDescriptionElement();
        case 1475610435:  return getAuthority(); 
        case -905838985:  return getSeriesElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 550933246: /*doseSequence*/ return new String[] {"positiveInt"};
        case -1724546052: /*description*/ return new String[] {"string"};
        case 1475610435: /*authority*/ return new String[] {"Reference"};
        case -905838985: /*series*/ return new String[] {"string"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("doseSequence")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImmunizationRecommendation.doseSequence");
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImmunizationRecommendation.description");
        }
        else if (name.equals("authority")) {
          this.authority = new Reference();
          return this.authority;
        }
        else if (name.equals("series")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImmunizationRecommendation.series");
        }
        else
          return super.addChild(name);
      }

      public ImmunizationRecommendationRecommendationProtocolComponent copy() {
        ImmunizationRecommendationRecommendationProtocolComponent dst = new ImmunizationRecommendationRecommendationProtocolComponent();
        copyValues(dst);
        dst.doseSequence = doseSequence == null ? null : doseSequence.copy();
        dst.description = description == null ? null : description.copy();
        dst.authority = authority == null ? null : authority.copy();
        dst.series = series == null ? null : series.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ImmunizationRecommendationRecommendationProtocolComponent))
          return false;
        ImmunizationRecommendationRecommendationProtocolComponent o = (ImmunizationRecommendationRecommendationProtocolComponent) other;
        return compareDeep(doseSequence, o.doseSequence, true) && compareDeep(description, o.description, true)
           && compareDeep(authority, o.authority, true) && compareDeep(series, o.series, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ImmunizationRecommendationRecommendationProtocolComponent))
          return false;
        ImmunizationRecommendationRecommendationProtocolComponent o = (ImmunizationRecommendationRecommendationProtocolComponent) other;
        return compareValues(doseSequence, o.doseSequence, true) && compareValues(description, o.description, true)
           && compareValues(series, o.series, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(doseSequence, description
          , authority, series);
      }

  public String fhirType() {
    return "ImmunizationRecommendation.recommendation.protocol";

  }

  }

    /**
     * A unique identifier assigned to this particular recommendation record.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Business identifier", formalDefinition="A unique identifier assigned to this particular recommendation record." )
    protected List<Identifier> identifier;

    /**
     * The patient the recommendations are for.
     */
    @Child(name = "patient", type = {Patient.class}, order=1, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Who this profile is for", formalDefinition="The patient the recommendations are for." )
    protected Reference patient;

    /**
     * The actual object that is the target of the reference (The patient the recommendations are for.)
     */
    protected Patient patientTarget;

    /**
     * Vaccine administration recommendations.
     */
    @Child(name = "recommendation", type = {}, order=2, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Vaccine administration recommendations", formalDefinition="Vaccine administration recommendations." )
    protected List<ImmunizationRecommendationRecommendationComponent> recommendation;

    private static final long serialVersionUID = 641058495L;

  /**
   * Constructor
   */
    public ImmunizationRecommendation() {
      super();
    }

  /**
   * Constructor
   */
    public ImmunizationRecommendation(Reference patient) {
      super();
      this.patient = patient;
    }

    /**
     * @return {@link #identifier} (A unique identifier assigned to this particular recommendation record.)
     */
    public List<Identifier> getIdentifier() { 
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      return this.identifier;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ImmunizationRecommendation setIdentifier(List<Identifier> theIdentifier) { 
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

    public ImmunizationRecommendation addIdentifier(Identifier t) { //3
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
     * @return {@link #patient} (The patient the recommendations are for.)
     */
    public Reference getPatient() { 
      if (this.patient == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImmunizationRecommendation.patient");
        else if (Configuration.doAutoCreate())
          this.patient = new Reference(); // cc
      return this.patient;
    }

    public boolean hasPatient() { 
      return this.patient != null && !this.patient.isEmpty();
    }

    /**
     * @param value {@link #patient} (The patient the recommendations are for.)
     */
    public ImmunizationRecommendation setPatient(Reference value) { 
      this.patient = value;
      return this;
    }

    /**
     * @return {@link #patient} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The patient the recommendations are for.)
     */
    public Patient getPatientTarget() { 
      if (this.patientTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImmunizationRecommendation.patient");
        else if (Configuration.doAutoCreate())
          this.patientTarget = new Patient(); // aa
      return this.patientTarget;
    }

    /**
     * @param value {@link #patient} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The patient the recommendations are for.)
     */
    public ImmunizationRecommendation setPatientTarget(Patient value) { 
      this.patientTarget = value;
      return this;
    }

    /**
     * @return {@link #recommendation} (Vaccine administration recommendations.)
     */
    public List<ImmunizationRecommendationRecommendationComponent> getRecommendation() { 
      if (this.recommendation == null)
        this.recommendation = new ArrayList<ImmunizationRecommendationRecommendationComponent>();
      return this.recommendation;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ImmunizationRecommendation setRecommendation(List<ImmunizationRecommendationRecommendationComponent> theRecommendation) { 
      this.recommendation = theRecommendation;
      return this;
    }

    public boolean hasRecommendation() { 
      if (this.recommendation == null)
        return false;
      for (ImmunizationRecommendationRecommendationComponent item : this.recommendation)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public ImmunizationRecommendationRecommendationComponent addRecommendation() { //3
      ImmunizationRecommendationRecommendationComponent t = new ImmunizationRecommendationRecommendationComponent();
      if (this.recommendation == null)
        this.recommendation = new ArrayList<ImmunizationRecommendationRecommendationComponent>();
      this.recommendation.add(t);
      return t;
    }

    public ImmunizationRecommendation addRecommendation(ImmunizationRecommendationRecommendationComponent t) { //3
      if (t == null)
        return this;
      if (this.recommendation == null)
        this.recommendation = new ArrayList<ImmunizationRecommendationRecommendationComponent>();
      this.recommendation.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #recommendation}, creating it if it does not already exist
     */
    public ImmunizationRecommendationRecommendationComponent getRecommendationFirstRep() { 
      if (getRecommendation().isEmpty()) {
        addRecommendation();
      }
      return getRecommendation().get(0);
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "A unique identifier assigned to this particular recommendation record.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("patient", "Reference(Patient)", "The patient the recommendations are for.", 0, java.lang.Integer.MAX_VALUE, patient));
        childrenList.add(new Property("recommendation", "", "Vaccine administration recommendations.", 0, java.lang.Integer.MAX_VALUE, recommendation));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case -791418107: /*patient*/ return this.patient == null ? new Base[0] : new Base[] {this.patient}; // Reference
        case -1028636743: /*recommendation*/ return this.recommendation == null ? new Base[0] : this.recommendation.toArray(new Base[this.recommendation.size()]); // ImmunizationRecommendationRecommendationComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.getIdentifier().add(castToIdentifier(value)); // Identifier
          return value;
        case -791418107: // patient
          this.patient = castToReference(value); // Reference
          return value;
        case -1028636743: // recommendation
          this.getRecommendation().add((ImmunizationRecommendationRecommendationComponent) value); // ImmunizationRecommendationRecommendationComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier")) {
          this.getIdentifier().add(castToIdentifier(value));
        } else if (name.equals("patient")) {
          this.patient = castToReference(value); // Reference
        } else if (name.equals("recommendation")) {
          this.getRecommendation().add((ImmunizationRecommendationRecommendationComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return addIdentifier(); 
        case -791418107:  return getPatient(); 
        case -1028636743:  return addRecommendation(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case -791418107: /*patient*/ return new String[] {"Reference"};
        case -1028636743: /*recommendation*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("patient")) {
          this.patient = new Reference();
          return this.patient;
        }
        else if (name.equals("recommendation")) {
          return addRecommendation();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "ImmunizationRecommendation";

  }

      public ImmunizationRecommendation copy() {
        ImmunizationRecommendation dst = new ImmunizationRecommendation();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.patient = patient == null ? null : patient.copy();
        if (recommendation != null) {
          dst.recommendation = new ArrayList<ImmunizationRecommendationRecommendationComponent>();
          for (ImmunizationRecommendationRecommendationComponent i : recommendation)
            dst.recommendation.add(i.copy());
        };
        return dst;
      }

      protected ImmunizationRecommendation typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ImmunizationRecommendation))
          return false;
        ImmunizationRecommendation o = (ImmunizationRecommendation) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(patient, o.patient, true) && compareDeep(recommendation, o.recommendation, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ImmunizationRecommendation))
          return false;
        ImmunizationRecommendation o = (ImmunizationRecommendation) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, patient, recommendation
          );
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.ImmunizationRecommendation;
   }

 /**
   * Search parameter: <b>date</b>
   * <p>
   * Description: <b>Date recommendation created</b><br>
   * Type: <b>date</b><br>
   * Path: <b>ImmunizationRecommendation.recommendation.date</b><br>
   * </p>
   */
  @SearchParamDefinition(name="date", path="ImmunizationRecommendation.recommendation.date", description="Date recommendation created", type="date" )
  public static final String SP_DATE = "date";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>date</b>
   * <p>
   * Description: <b>Date recommendation created</b><br>
   * Type: <b>date</b><br>
   * Path: <b>ImmunizationRecommendation.recommendation.date</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam DATE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_DATE);

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>Business identifier</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ImmunizationRecommendation.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="ImmunizationRecommendation.identifier", description="Business identifier", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>Business identifier</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ImmunizationRecommendation.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>dose-sequence</b>
   * <p>
   * Description: <b>Dose number within sequence</b><br>
   * Type: <b>number</b><br>
   * Path: <b>ImmunizationRecommendation.recommendation.protocol.doseSequence</b><br>
   * </p>
   */
  @SearchParamDefinition(name="dose-sequence", path="ImmunizationRecommendation.recommendation.protocol.doseSequence", description="Dose number within sequence", type="number" )
  public static final String SP_DOSE_SEQUENCE = "dose-sequence";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>dose-sequence</b>
   * <p>
   * Description: <b>Dose number within sequence</b><br>
   * Type: <b>number</b><br>
   * Path: <b>ImmunizationRecommendation.recommendation.protocol.doseSequence</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.NumberClientParam DOSE_SEQUENCE = new ca.uhn.fhir.rest.gclient.NumberClientParam(SP_DOSE_SEQUENCE);

 /**
   * Search parameter: <b>target-disease</b>
   * <p>
   * Description: <b>Disease to be immunized against</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ImmunizationRecommendation.recommendation.targetDisease</b><br>
   * </p>
   */
  @SearchParamDefinition(name="target-disease", path="ImmunizationRecommendation.recommendation.targetDisease", description="Disease to be immunized against", type="token" )
  public static final String SP_TARGET_DISEASE = "target-disease";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>target-disease</b>
   * <p>
   * Description: <b>Disease to be immunized against</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ImmunizationRecommendation.recommendation.targetDisease</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam TARGET_DISEASE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_TARGET_DISEASE);

 /**
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>Who this profile is for</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ImmunizationRecommendation.patient</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="ImmunizationRecommendation.patient", description="Who this profile is for", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient") }, target={Patient.class } )
  public static final String SP_PATIENT = "patient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patient</b>
   * <p>
   * Description: <b>Who this profile is for</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ImmunizationRecommendation.patient</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PATIENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PATIENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ImmunizationRecommendation:patient</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PATIENT = new ca.uhn.fhir.model.api.Include("ImmunizationRecommendation:patient").toLocked();

 /**
   * Search parameter: <b>vaccine-type</b>
   * <p>
   * Description: <b>Vaccine recommendation applies to</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ImmunizationRecommendation.recommendation.vaccineCode</b><br>
   * </p>
   */
  @SearchParamDefinition(name="vaccine-type", path="ImmunizationRecommendation.recommendation.vaccineCode", description="Vaccine recommendation applies to", type="token" )
  public static final String SP_VACCINE_TYPE = "vaccine-type";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>vaccine-type</b>
   * <p>
   * Description: <b>Vaccine recommendation applies to</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ImmunizationRecommendation.recommendation.vaccineCode</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam VACCINE_TYPE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_VACCINE_TYPE);

 /**
   * Search parameter: <b>dose-number</b>
   * <p>
   * Description: <b>Recommended dose number</b><br>
   * Type: <b>number</b><br>
   * Path: <b>ImmunizationRecommendation.recommendation.doseNumber</b><br>
   * </p>
   */
  @SearchParamDefinition(name="dose-number", path="ImmunizationRecommendation.recommendation.doseNumber", description="Recommended dose number", type="number" )
  public static final String SP_DOSE_NUMBER = "dose-number";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>dose-number</b>
   * <p>
   * Description: <b>Recommended dose number</b><br>
   * Type: <b>number</b><br>
   * Path: <b>ImmunizationRecommendation.recommendation.doseNumber</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.NumberClientParam DOSE_NUMBER = new ca.uhn.fhir.rest.gclient.NumberClientParam(SP_DOSE_NUMBER);

 /**
   * Search parameter: <b>information</b>
   * <p>
   * Description: <b>Patient observations supporting recommendation</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ImmunizationRecommendation.recommendation.supportingPatientInformation</b><br>
   * </p>
   */
  @SearchParamDefinition(name="information", path="ImmunizationRecommendation.recommendation.supportingPatientInformation", description="Patient observations supporting recommendation", type="reference", target={AllergyIntolerance.class, Observation.class } )
  public static final String SP_INFORMATION = "information";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>information</b>
   * <p>
   * Description: <b>Patient observations supporting recommendation</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ImmunizationRecommendation.recommendation.supportingPatientInformation</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam INFORMATION = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_INFORMATION);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ImmunizationRecommendation:information</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_INFORMATION = new ca.uhn.fhir.model.api.Include("ImmunizationRecommendation:information").toLocked();

 /**
   * Search parameter: <b>support</b>
   * <p>
   * Description: <b>Past immunizations supporting recommendation</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ImmunizationRecommendation.recommendation.supportingImmunization</b><br>
   * </p>
   */
  @SearchParamDefinition(name="support", path="ImmunizationRecommendation.recommendation.supportingImmunization", description="Past immunizations supporting recommendation", type="reference", target={Immunization.class } )
  public static final String SP_SUPPORT = "support";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>support</b>
   * <p>
   * Description: <b>Past immunizations supporting recommendation</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ImmunizationRecommendation.recommendation.supportingImmunization</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SUPPORT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SUPPORT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ImmunizationRecommendation:support</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SUPPORT = new ca.uhn.fhir.model.api.Include("ImmunizationRecommendation:support").toLocked();

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>Vaccine administration status</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ImmunizationRecommendation.recommendation.forecastStatus</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="ImmunizationRecommendation.recommendation.forecastStatus", description="Vaccine administration status", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>Vaccine administration status</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ImmunizationRecommendation.recommendation.forecastStatus</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);


}

