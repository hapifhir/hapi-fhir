package org.hl7.fhir.instance.model;

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

// Generated on Sat, Aug 22, 2015 23:00-0400 for FHIR v0.5.0

import java.util.*;

import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.instance.model.annotations.ResourceDef;
import org.hl7.fhir.instance.model.annotations.SearchParamDefinition;
import org.hl7.fhir.instance.model.annotations.Child;
import org.hl7.fhir.instance.model.annotations.Description;
import org.hl7.fhir.instance.model.annotations.Block;
import org.hl7.fhir.instance.model.api.*;
/**
 * A patient's point-of-time immunization status and recommendation with optional supporting justification.
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
        @Child(name = "vaccineType", type = {CodeableConcept.class}, order=2, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Vaccine recommendation applies to", formalDefinition="Vaccine that pertains to the recommendation." )
        protected CodeableConcept vaccineType;

        /**
         * This indicates the next recommended dose number (e.g. dose 2 is the next recommended dose).
         */
        @Child(name = "doseNumber", type = {PositiveIntType.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Recommended dose number", formalDefinition="This indicates the next recommended dose number (e.g. dose 2 is the next recommended dose)." )
        protected PositiveIntType doseNumber;

        /**
         * Vaccine administration status.
         */
        @Child(name = "forecastStatus", type = {CodeableConcept.class}, order=4, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Vaccine administration status", formalDefinition="Vaccine administration status." )
        protected CodeableConcept forecastStatus;

        /**
         * Vaccine date recommendations - e.g. earliest date to administer, latest date to administer, etc.
         */
        @Child(name = "dateCriterion", type = {}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Dates governing proposed immunization", formalDefinition="Vaccine date recommendations - e.g. earliest date to administer, latest date to administer, etc." )
        protected List<ImmunizationRecommendationRecommendationDateCriterionComponent> dateCriterion;

        /**
         * Contains information about the protocol under which the vaccine was administered.
         */
        @Child(name = "protocol", type = {}, order=6, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Protocol used by recommendation", formalDefinition="Contains information about the protocol under which the vaccine was administered." )
        protected ImmunizationRecommendationRecommendationProtocolComponent protocol;

        /**
         * Immunization event history that supports the status and recommendation.
         */
        @Child(name = "supportingImmunization", type = {Immunization.class}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Past immunizations supporting recommendation", formalDefinition="Immunization event history that supports the status and recommendation." )
        protected List<Reference> supportingImmunization;
        /**
         * The actual objects that are the target of the reference (Immunization event history that supports the status and recommendation.)
         */
        protected List<Immunization> supportingImmunizationTarget;


        /**
         * Patient Information that supports the status and recommendation.  This includes patient observations, adverse reactions and allergy/intolerance information.
         */
        @Child(name = "supportingPatientInformation", type = {Observation.class, AllergyIntolerance.class}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Patient observations supporting recommendation", formalDefinition="Patient Information that supports the status and recommendation.  This includes patient observations, adverse reactions and allergy/intolerance information." )
        protected List<Reference> supportingPatientInformation;
        /**
         * The actual objects that are the target of the reference (Patient Information that supports the status and recommendation.  This includes patient observations, adverse reactions and allergy/intolerance information.)
         */
        protected List<Resource> supportingPatientInformationTarget;


        private static final long serialVersionUID = 366360557L;

    /*
     * Constructor
     */
      public ImmunizationRecommendationRecommendationComponent() {
        super();
      }

    /*
     * Constructor
     */
      public ImmunizationRecommendationRecommendationComponent(DateTimeType date, CodeableConcept vaccineType, CodeableConcept forecastStatus) {
        super();
        this.date = date;
        this.vaccineType = vaccineType;
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
         * @return {@link #vaccineType} (Vaccine that pertains to the recommendation.)
         */
        public CodeableConcept getVaccineType() { 
          if (this.vaccineType == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImmunizationRecommendationRecommendationComponent.vaccineType");
            else if (Configuration.doAutoCreate())
              this.vaccineType = new CodeableConcept(); // cc
          return this.vaccineType;
        }

        public boolean hasVaccineType() { 
          return this.vaccineType != null && !this.vaccineType.isEmpty();
        }

        /**
         * @param value {@link #vaccineType} (Vaccine that pertains to the recommendation.)
         */
        public ImmunizationRecommendationRecommendationComponent setVaccineType(CodeableConcept value) { 
          this.vaccineType = value;
          return this;
        }

        /**
         * @return {@link #doseNumber} (This indicates the next recommended dose number (e.g. dose 2 is the next recommended dose).). This is the underlying object with id, value and extensions. The accessor "getDoseNumber" gives direct access to the value
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
         * @param value {@link #doseNumber} (This indicates the next recommended dose number (e.g. dose 2 is the next recommended dose).). This is the underlying object with id, value and extensions. The accessor "getDoseNumber" gives direct access to the value
         */
        public ImmunizationRecommendationRecommendationComponent setDoseNumberElement(PositiveIntType value) { 
          this.doseNumber = value;
          return this;
        }

        /**
         * @return This indicates the next recommended dose number (e.g. dose 2 is the next recommended dose).
         */
        public int getDoseNumber() { 
          return this.doseNumber == null || this.doseNumber.isEmpty() ? 0 : this.doseNumber.getValue();
        }

        /**
         * @param value This indicates the next recommended dose number (e.g. dose 2 is the next recommended dose).
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
         * @return {@link #dateCriterion} (Vaccine date recommendations - e.g. earliest date to administer, latest date to administer, etc.)
         */
        public List<ImmunizationRecommendationRecommendationDateCriterionComponent> getDateCriterion() { 
          if (this.dateCriterion == null)
            this.dateCriterion = new ArrayList<ImmunizationRecommendationRecommendationDateCriterionComponent>();
          return this.dateCriterion;
        }

        public boolean hasDateCriterion() { 
          if (this.dateCriterion == null)
            return false;
          for (ImmunizationRecommendationRecommendationDateCriterionComponent item : this.dateCriterion)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #dateCriterion} (Vaccine date recommendations - e.g. earliest date to administer, latest date to administer, etc.)
         */
    // syntactic sugar
        public ImmunizationRecommendationRecommendationDateCriterionComponent addDateCriterion() { //3
          ImmunizationRecommendationRecommendationDateCriterionComponent t = new ImmunizationRecommendationRecommendationDateCriterionComponent();
          if (this.dateCriterion == null)
            this.dateCriterion = new ArrayList<ImmunizationRecommendationRecommendationDateCriterionComponent>();
          this.dateCriterion.add(t);
          return t;
        }

    // syntactic sugar
        public ImmunizationRecommendationRecommendationComponent addDateCriterion(ImmunizationRecommendationRecommendationDateCriterionComponent t) { //3
          if (t == null)
            return this;
          if (this.dateCriterion == null)
            this.dateCriterion = new ArrayList<ImmunizationRecommendationRecommendationDateCriterionComponent>();
          this.dateCriterion.add(t);
          return this;
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

        public boolean hasSupportingImmunization() { 
          if (this.supportingImmunization == null)
            return false;
          for (Reference item : this.supportingImmunization)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #supportingImmunization} (Immunization event history that supports the status and recommendation.)
         */
    // syntactic sugar
        public Reference addSupportingImmunization() { //3
          Reference t = new Reference();
          if (this.supportingImmunization == null)
            this.supportingImmunization = new ArrayList<Reference>();
          this.supportingImmunization.add(t);
          return t;
        }

    // syntactic sugar
        public ImmunizationRecommendationRecommendationComponent addSupportingImmunization(Reference t) { //3
          if (t == null)
            return this;
          if (this.supportingImmunization == null)
            this.supportingImmunization = new ArrayList<Reference>();
          this.supportingImmunization.add(t);
          return this;
        }

        /**
         * @return {@link #supportingImmunization} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. Immunization event history that supports the status and recommendation.)
         */
        public List<Immunization> getSupportingImmunizationTarget() { 
          if (this.supportingImmunizationTarget == null)
            this.supportingImmunizationTarget = new ArrayList<Immunization>();
          return this.supportingImmunizationTarget;
        }

    // syntactic sugar
        /**
         * @return {@link #supportingImmunization} (Add an actual object that is the target of the reference. The reference library doesn't use these, but you can use this to hold the resources if you resolvethemt. Immunization event history that supports the status and recommendation.)
         */
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

        public boolean hasSupportingPatientInformation() { 
          if (this.supportingPatientInformation == null)
            return false;
          for (Reference item : this.supportingPatientInformation)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #supportingPatientInformation} (Patient Information that supports the status and recommendation.  This includes patient observations, adverse reactions and allergy/intolerance information.)
         */
    // syntactic sugar
        public Reference addSupportingPatientInformation() { //3
          Reference t = new Reference();
          if (this.supportingPatientInformation == null)
            this.supportingPatientInformation = new ArrayList<Reference>();
          this.supportingPatientInformation.add(t);
          return t;
        }

    // syntactic sugar
        public ImmunizationRecommendationRecommendationComponent addSupportingPatientInformation(Reference t) { //3
          if (t == null)
            return this;
          if (this.supportingPatientInformation == null)
            this.supportingPatientInformation = new ArrayList<Reference>();
          this.supportingPatientInformation.add(t);
          return this;
        }

        /**
         * @return {@link #supportingPatientInformation} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. Patient Information that supports the status and recommendation.  This includes patient observations, adverse reactions and allergy/intolerance information.)
         */
        public List<Resource> getSupportingPatientInformationTarget() { 
          if (this.supportingPatientInformationTarget == null)
            this.supportingPatientInformationTarget = new ArrayList<Resource>();
          return this.supportingPatientInformationTarget;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("date", "dateTime", "The date the immunization recommendation was created.", 0, java.lang.Integer.MAX_VALUE, date));
          childrenList.add(new Property("vaccineType", "CodeableConcept", "Vaccine that pertains to the recommendation.", 0, java.lang.Integer.MAX_VALUE, vaccineType));
          childrenList.add(new Property("doseNumber", "positiveInt", "This indicates the next recommended dose number (e.g. dose 2 is the next recommended dose).", 0, java.lang.Integer.MAX_VALUE, doseNumber));
          childrenList.add(new Property("forecastStatus", "CodeableConcept", "Vaccine administration status.", 0, java.lang.Integer.MAX_VALUE, forecastStatus));
          childrenList.add(new Property("dateCriterion", "", "Vaccine date recommendations - e.g. earliest date to administer, latest date to administer, etc.", 0, java.lang.Integer.MAX_VALUE, dateCriterion));
          childrenList.add(new Property("protocol", "", "Contains information about the protocol under which the vaccine was administered.", 0, java.lang.Integer.MAX_VALUE, protocol));
          childrenList.add(new Property("supportingImmunization", "Reference(Immunization)", "Immunization event history that supports the status and recommendation.", 0, java.lang.Integer.MAX_VALUE, supportingImmunization));
          childrenList.add(new Property("supportingPatientInformation", "Reference(Observation|AllergyIntolerance)", "Patient Information that supports the status and recommendation.  This includes patient observations, adverse reactions and allergy/intolerance information.", 0, java.lang.Integer.MAX_VALUE, supportingPatientInformation));
        }

      public ImmunizationRecommendationRecommendationComponent copy() {
        ImmunizationRecommendationRecommendationComponent dst = new ImmunizationRecommendationRecommendationComponent();
        copyValues(dst);
        dst.date = date == null ? null : date.copy();
        dst.vaccineType = vaccineType == null ? null : vaccineType.copy();
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
        return compareDeep(date, o.date, true) && compareDeep(vaccineType, o.vaccineType, true) && compareDeep(doseNumber, o.doseNumber, true)
           && compareDeep(forecastStatus, o.forecastStatus, true) && compareDeep(dateCriterion, o.dateCriterion, true)
           && compareDeep(protocol, o.protocol, true) && compareDeep(supportingImmunization, o.supportingImmunization, true)
           && compareDeep(supportingPatientInformation, o.supportingPatientInformation, true);
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
        return super.isEmpty() && (date == null || date.isEmpty()) && (vaccineType == null || vaccineType.isEmpty())
           && (doseNumber == null || doseNumber.isEmpty()) && (forecastStatus == null || forecastStatus.isEmpty())
           && (dateCriterion == null || dateCriterion.isEmpty()) && (protocol == null || protocol.isEmpty())
           && (supportingImmunization == null || supportingImmunization.isEmpty()) && (supportingPatientInformation == null || supportingPatientInformation.isEmpty())
          ;
      }

  }

    @Block()
    public static class ImmunizationRecommendationRecommendationDateCriterionComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Date classification of recommendation - e.g. earliest date to give, latest date to give, etc.
         */
        @Child(name = "code", type = {CodeableConcept.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Type of date", formalDefinition="Date classification of recommendation - e.g. earliest date to give, latest date to give, etc." )
        protected CodeableConcept code;

        /**
         * Date recommendation.
         */
        @Child(name = "value", type = {DateTimeType.class}, order=2, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Recommended date", formalDefinition="Date recommendation." )
        protected DateTimeType value;

        private static final long serialVersionUID = 1036994566L;

    /*
     * Constructor
     */
      public ImmunizationRecommendationRecommendationDateCriterionComponent() {
        super();
      }

    /*
     * Constructor
     */
      public ImmunizationRecommendationRecommendationDateCriterionComponent(CodeableConcept code, DateTimeType value) {
        super();
        this.code = code;
        this.value = value;
      }

        /**
         * @return {@link #code} (Date classification of recommendation - e.g. earliest date to give, latest date to give, etc.)
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
         * @param value {@link #code} (Date classification of recommendation - e.g. earliest date to give, latest date to give, etc.)
         */
        public ImmunizationRecommendationRecommendationDateCriterionComponent setCode(CodeableConcept value) { 
          this.code = value;
          return this;
        }

        /**
         * @return {@link #value} (Date recommendation.). This is the underlying object with id, value and extensions. The accessor "getValue" gives direct access to the value
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
         * @param value {@link #value} (Date recommendation.). This is the underlying object with id, value and extensions. The accessor "getValue" gives direct access to the value
         */
        public ImmunizationRecommendationRecommendationDateCriterionComponent setValueElement(DateTimeType value) { 
          this.value = value;
          return this;
        }

        /**
         * @return Date recommendation.
         */
        public Date getValue() { 
          return this.value == null ? null : this.value.getValue();
        }

        /**
         * @param value Date recommendation.
         */
        public ImmunizationRecommendationRecommendationDateCriterionComponent setValue(Date value) { 
            if (this.value == null)
              this.value = new DateTimeType();
            this.value.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("code", "CodeableConcept", "Date classification of recommendation - e.g. earliest date to give, latest date to give, etc.", 0, java.lang.Integer.MAX_VALUE, code));
          childrenList.add(new Property("value", "dateTime", "Date recommendation.", 0, java.lang.Integer.MAX_VALUE, value));
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
        return super.isEmpty() && (code == null || code.isEmpty()) && (value == null || value.isEmpty())
          ;
      }

  }

    @Block()
    public static class ImmunizationRecommendationRecommendationProtocolComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Indicates the nominal position in a series of the next dose.  This is the recommended dose number as per a specified protocol.
         */
        @Child(name = "doseSequence", type = {IntegerType.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Number of dose within sequence", formalDefinition="Indicates the nominal position in a series of the next dose.  This is the recommended dose number as per a specified protocol." )
        protected IntegerType doseSequence;

        /**
         * Contains the description about the protocol under which the vaccine was administered.
         */
        @Child(name = "description", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Protocol details", formalDefinition="Contains the description about the protocol under which the vaccine was administered." )
        protected StringType description;

        /**
         * Indicates the authority who published the protocol?  E.g. ACIP.
         */
        @Child(name = "authority", type = {Organization.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Who is responsible for protocol", formalDefinition="Indicates the authority who published the protocol?  E.g. ACIP." )
        protected Reference authority;

        /**
         * The actual object that is the target of the reference (Indicates the authority who published the protocol?  E.g. ACIP.)
         */
        protected Organization authorityTarget;

        /**
         * One possible path to achieve presumed immunity against a disease - within the context of an authority.
         */
        @Child(name = "series", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Name of vaccination series", formalDefinition="One possible path to achieve presumed immunity against a disease - within the context of an authority." )
        protected StringType series;

        private static final long serialVersionUID = -512702014L;

    /*
     * Constructor
     */
      public ImmunizationRecommendationRecommendationProtocolComponent() {
        super();
      }

        /**
         * @return {@link #doseSequence} (Indicates the nominal position in a series of the next dose.  This is the recommended dose number as per a specified protocol.). This is the underlying object with id, value and extensions. The accessor "getDoseSequence" gives direct access to the value
         */
        public IntegerType getDoseSequenceElement() { 
          if (this.doseSequence == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImmunizationRecommendationRecommendationProtocolComponent.doseSequence");
            else if (Configuration.doAutoCreate())
              this.doseSequence = new IntegerType(); // bb
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
        public ImmunizationRecommendationRecommendationProtocolComponent setDoseSequenceElement(IntegerType value) { 
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
              this.doseSequence = new IntegerType();
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
         * @return {@link #authority} (Indicates the authority who published the protocol?  E.g. ACIP.)
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
         * @param value {@link #authority} (Indicates the authority who published the protocol?  E.g. ACIP.)
         */
        public ImmunizationRecommendationRecommendationProtocolComponent setAuthority(Reference value) { 
          this.authority = value;
          return this;
        }

        /**
         * @return {@link #authority} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Indicates the authority who published the protocol?  E.g. ACIP.)
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
         * @param value {@link #authority} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Indicates the authority who published the protocol?  E.g. ACIP.)
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
          childrenList.add(new Property("doseSequence", "integer", "Indicates the nominal position in a series of the next dose.  This is the recommended dose number as per a specified protocol.", 0, java.lang.Integer.MAX_VALUE, doseSequence));
          childrenList.add(new Property("description", "string", "Contains the description about the protocol under which the vaccine was administered.", 0, java.lang.Integer.MAX_VALUE, description));
          childrenList.add(new Property("authority", "Reference(Organization)", "Indicates the authority who published the protocol?  E.g. ACIP.", 0, java.lang.Integer.MAX_VALUE, authority));
          childrenList.add(new Property("series", "string", "One possible path to achieve presumed immunity against a disease - within the context of an authority.", 0, java.lang.Integer.MAX_VALUE, series));
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
        return super.isEmpty() && (doseSequence == null || doseSequence.isEmpty()) && (description == null || description.isEmpty())
           && (authority == null || authority.isEmpty()) && (series == null || series.isEmpty());
      }

  }

    /**
     * A unique identifier assigned to this particular recommendation record.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Business identifier", formalDefinition="A unique identifier assigned to this particular recommendation record." )
    protected List<Identifier> identifier;

    /**
     * The patient for whom the recommendations are for.
     */
    @Child(name = "patient", type = {Patient.class}, order=1, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Who this profile is for", formalDefinition="The patient for whom the recommendations are for." )
    protected Reference patient;

    /**
     * The actual object that is the target of the reference (The patient for whom the recommendations are for.)
     */
    protected Patient patientTarget;

    /**
     * Vaccine administration recommendations.
     */
    @Child(name = "recommendation", type = {}, order=2, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Vaccine administration recommendations", formalDefinition="Vaccine administration recommendations." )
    protected List<ImmunizationRecommendationRecommendationComponent> recommendation;

    private static final long serialVersionUID = 641058495L;

  /*
   * Constructor
   */
    public ImmunizationRecommendation() {
      super();
    }

  /*
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

    public boolean hasIdentifier() { 
      if (this.identifier == null)
        return false;
      for (Identifier item : this.identifier)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #identifier} (A unique identifier assigned to this particular recommendation record.)
     */
    // syntactic sugar
    public Identifier addIdentifier() { //3
      Identifier t = new Identifier();
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return t;
    }

    // syntactic sugar
    public ImmunizationRecommendation addIdentifier(Identifier t) { //3
      if (t == null)
        return this;
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return this;
    }

    /**
     * @return {@link #patient} (The patient for whom the recommendations are for.)
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
     * @param value {@link #patient} (The patient for whom the recommendations are for.)
     */
    public ImmunizationRecommendation setPatient(Reference value) { 
      this.patient = value;
      return this;
    }

    /**
     * @return {@link #patient} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The patient for whom the recommendations are for.)
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
     * @param value {@link #patient} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The patient for whom the recommendations are for.)
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

    public boolean hasRecommendation() { 
      if (this.recommendation == null)
        return false;
      for (ImmunizationRecommendationRecommendationComponent item : this.recommendation)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #recommendation} (Vaccine administration recommendations.)
     */
    // syntactic sugar
    public ImmunizationRecommendationRecommendationComponent addRecommendation() { //3
      ImmunizationRecommendationRecommendationComponent t = new ImmunizationRecommendationRecommendationComponent();
      if (this.recommendation == null)
        this.recommendation = new ArrayList<ImmunizationRecommendationRecommendationComponent>();
      this.recommendation.add(t);
      return t;
    }

    // syntactic sugar
    public ImmunizationRecommendation addRecommendation(ImmunizationRecommendationRecommendationComponent t) { //3
      if (t == null)
        return this;
      if (this.recommendation == null)
        this.recommendation = new ArrayList<ImmunizationRecommendationRecommendationComponent>();
      this.recommendation.add(t);
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "A unique identifier assigned to this particular recommendation record.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("patient", "Reference(Patient)", "The patient for whom the recommendations are for.", 0, java.lang.Integer.MAX_VALUE, patient));
        childrenList.add(new Property("recommendation", "", "Vaccine administration recommendations.", 0, java.lang.Integer.MAX_VALUE, recommendation));
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
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (patient == null || patient.isEmpty())
           && (recommendation == null || recommendation.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.ImmunizationRecommendation;
   }

  @SearchParamDefinition(name="date", path="ImmunizationRecommendation.recommendation.date", description="Date recommendation created", type="date" )
  public static final String SP_DATE = "date";
  @SearchParamDefinition(name="identifier", path="ImmunizationRecommendation.identifier", description="Business identifier", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
  @SearchParamDefinition(name="dose-sequence", path="ImmunizationRecommendation.recommendation.protocol.doseSequence", description="Number of dose within sequence", type="token" )
  public static final String SP_DOSESEQUENCE = "dose-sequence";
  @SearchParamDefinition(name="subject", path="ImmunizationRecommendation.patient", description="Who this profile is for", type="reference" )
  public static final String SP_SUBJECT = "subject";
  @SearchParamDefinition(name="patient", path="ImmunizationRecommendation.patient", description="Who this profile is for", type="reference" )
  public static final String SP_PATIENT = "patient";
  @SearchParamDefinition(name="vaccine-type", path="ImmunizationRecommendation.recommendation.vaccineType", description="Vaccine recommendation applies to", type="token" )
  public static final String SP_VACCINETYPE = "vaccine-type";
  @SearchParamDefinition(name="dose-number", path="ImmunizationRecommendation.recommendation.doseNumber", description="Recommended dose number", type="number" )
  public static final String SP_DOSENUMBER = "dose-number";
  @SearchParamDefinition(name="information", path="ImmunizationRecommendation.recommendation.supportingPatientInformation", description="Patient observations supporting recommendation", type="reference" )
  public static final String SP_INFORMATION = "information";
  @SearchParamDefinition(name="support", path="ImmunizationRecommendation.recommendation.supportingImmunization", description="Past immunizations supporting recommendation", type="reference" )
  public static final String SP_SUPPORT = "support";
  @SearchParamDefinition(name="status", path="ImmunizationRecommendation.recommendation.forecastStatus", description="Vaccine administration status", type="token" )
  public static final String SP_STATUS = "status";

}

