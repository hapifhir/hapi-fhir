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

// Generated on Wed, Jul 8, 2015 17:35-0400 for FHIR v0.5.0

import java.util.*;

import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.instance.model.annotations.ResourceDef;
import org.hl7.fhir.instance.model.annotations.SearchParamDefinition;
import org.hl7.fhir.instance.model.annotations.Child;
import org.hl7.fhir.instance.model.annotations.Description;
import org.hl7.fhir.instance.model.annotations.Block;
import org.hl7.fhir.instance.model.api.*;
/**
 * An order for both supply of the medication and the instructions for administration of the medicine to a patient.
 */
@ResourceDef(name="MedicationPrescription", profile="http://hl7.org/fhir/Profile/MedicationPrescription")
public class MedicationPrescription extends DomainResource {

    public enum MedicationPrescriptionStatus {
        /**
         * The prescription is 'actionable', but not all actions that are implied by it have occurred yet.
         */
        ACTIVE, 
        /**
         * Actions implied by the prescription have been temporarily halted, but are expected to continue later.  May also be called "suspended".
         */
        ONHOLD, 
        /**
         * All actions that are implied by the prescription have occurred (this will rarely be made explicit).
         */
        COMPLETED, 
        /**
         * The prescription was entered in error and therefore nullified.
         */
        ENTEREDINERROR, 
        /**
         * Actions implied by the prescription have been permanently halted, before all of them occurred.
         */
        STOPPED, 
        /**
         * The prescription was replaced by a newer one, which encompasses all the information in the previous one.
         */
        SUPERSEDED, 
        /**
         * The prescription is not yet 'actionable', i.e. it is a work in progress, required sign-off, need to be run through decision support.
         */
        DRAFT, 
        /**
         * added to help the parsers
         */
        NULL;
        public static MedicationPrescriptionStatus fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("active".equals(codeString))
          return ACTIVE;
        if ("on-hold".equals(codeString))
          return ONHOLD;
        if ("completed".equals(codeString))
          return COMPLETED;
        if ("entered-in-error".equals(codeString))
          return ENTEREDINERROR;
        if ("stopped".equals(codeString))
          return STOPPED;
        if ("superseded".equals(codeString))
          return SUPERSEDED;
        if ("draft".equals(codeString))
          return DRAFT;
        throw new Exception("Unknown MedicationPrescriptionStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ACTIVE: return "active";
            case ONHOLD: return "on-hold";
            case COMPLETED: return "completed";
            case ENTEREDINERROR: return "entered-in-error";
            case STOPPED: return "stopped";
            case SUPERSEDED: return "superseded";
            case DRAFT: return "draft";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case ACTIVE: return "http://hl7.org/fhir/medication-prescription-status";
            case ONHOLD: return "http://hl7.org/fhir/medication-prescription-status";
            case COMPLETED: return "http://hl7.org/fhir/medication-prescription-status";
            case ENTEREDINERROR: return "http://hl7.org/fhir/medication-prescription-status";
            case STOPPED: return "http://hl7.org/fhir/medication-prescription-status";
            case SUPERSEDED: return "http://hl7.org/fhir/medication-prescription-status";
            case DRAFT: return "http://hl7.org/fhir/medication-prescription-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case ACTIVE: return "The prescription is 'actionable', but not all actions that are implied by it have occurred yet.";
            case ONHOLD: return "Actions implied by the prescription have been temporarily halted, but are expected to continue later.  May also be called 'suspended'.";
            case COMPLETED: return "All actions that are implied by the prescription have occurred (this will rarely be made explicit).";
            case ENTEREDINERROR: return "The prescription was entered in error and therefore nullified.";
            case STOPPED: return "Actions implied by the prescription have been permanently halted, before all of them occurred.";
            case SUPERSEDED: return "The prescription was replaced by a newer one, which encompasses all the information in the previous one.";
            case DRAFT: return "The prescription is not yet 'actionable', i.e. it is a work in progress, required sign-off, need to be run through decision support.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ACTIVE: return "Active";
            case ONHOLD: return "On Hold";
            case COMPLETED: return "Completed";
            case ENTEREDINERROR: return "Entered In Error";
            case STOPPED: return "Stopped";
            case SUPERSEDED: return "Superseded";
            case DRAFT: return "Draft";
            default: return "?";
          }
        }
    }

  public static class MedicationPrescriptionStatusEnumFactory implements EnumFactory<MedicationPrescriptionStatus> {
    public MedicationPrescriptionStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("active".equals(codeString))
          return MedicationPrescriptionStatus.ACTIVE;
        if ("on-hold".equals(codeString))
          return MedicationPrescriptionStatus.ONHOLD;
        if ("completed".equals(codeString))
          return MedicationPrescriptionStatus.COMPLETED;
        if ("entered-in-error".equals(codeString))
          return MedicationPrescriptionStatus.ENTEREDINERROR;
        if ("stopped".equals(codeString))
          return MedicationPrescriptionStatus.STOPPED;
        if ("superseded".equals(codeString))
          return MedicationPrescriptionStatus.SUPERSEDED;
        if ("draft".equals(codeString))
          return MedicationPrescriptionStatus.DRAFT;
        throw new IllegalArgumentException("Unknown MedicationPrescriptionStatus code '"+codeString+"'");
        }
    public String toCode(MedicationPrescriptionStatus code) {
      if (code == MedicationPrescriptionStatus.ACTIVE)
        return "active";
      if (code == MedicationPrescriptionStatus.ONHOLD)
        return "on-hold";
      if (code == MedicationPrescriptionStatus.COMPLETED)
        return "completed";
      if (code == MedicationPrescriptionStatus.ENTEREDINERROR)
        return "entered-in-error";
      if (code == MedicationPrescriptionStatus.STOPPED)
        return "stopped";
      if (code == MedicationPrescriptionStatus.SUPERSEDED)
        return "superseded";
      if (code == MedicationPrescriptionStatus.DRAFT)
        return "draft";
      return "?";
      }
    }

    @Block()
    public static class MedicationPrescriptionDosageInstructionComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Free text dosage instructions can be used for cases where the instructions are too complex to code. When coded instructions are present, the free text instructions may still be present for display to humans taking or administering the medication.
         */
        @Child(name = "text", type = {StringType.class}, order=1, min=0, max=1)
        @Description(shortDefinition="Dosage instructions expressed as text", formalDefinition="Free text dosage instructions can be used for cases where the instructions are too complex to code. When coded instructions are present, the free text instructions may still be present for display to humans taking or administering the medication." )
        protected StringType text;

        /**
         * Additional instructions such as "Swallow with plenty of water" which may or may not be coded.
         */
        @Child(name = "additionalInstructions", type = {CodeableConcept.class}, order=2, min=0, max=1)
        @Description(shortDefinition="Supplemental instructions - e.g. 'with meals'", formalDefinition="Additional instructions such as 'Swallow with plenty of water' which may or may not be coded." )
        protected CodeableConcept additionalInstructions;

        /**
         * The timing schedule for giving the medication to the patient.  The Schedule data type allows many different expressions, for example.  "Every  8 hours"; "Three times a day"; "1/2 an hour before breakfast for 10 days from 23-Dec 2011:";  "15 Oct 2013, 17 Oct 2013 and 1 Nov 2013".
         */
        @Child(name = "scheduled", type = {DateTimeType.class, Period.class, Timing.class}, order=3, min=0, max=1)
        @Description(shortDefinition="When medication should be administered", formalDefinition="The timing schedule for giving the medication to the patient.  The Schedule data type allows many different expressions, for example.  'Every  8 hours'; 'Three times a day'; '1/2 an hour before breakfast for 10 days from 23-Dec 2011:';  '15 Oct 2013, 17 Oct 2013 and 1 Nov 2013'." )
        protected Type scheduled;

        /**
         * If set to true or if specified as a CodeableConcept, indicates that the medication is only taken when needed within the specified schedule rather than at every scheduled dose.  If a CodeableConcept is present, it indicates the pre-condition for taking the Medication.
         */
        @Child(name = "asNeeded", type = {BooleanType.class, CodeableConcept.class}, order=4, min=0, max=1)
        @Description(shortDefinition="Take 'as needed' f(or x)", formalDefinition="If set to true or if specified as a CodeableConcept, indicates that the medication is only taken when needed within the specified schedule rather than at every scheduled dose.  If a CodeableConcept is present, it indicates the pre-condition for taking the Medication." )
        protected Type asNeeded;

        /**
         * A coded specification of the anatomic site where the medication first enters the body.
         */
        @Child(name = "site", type = {CodeableConcept.class}, order=5, min=0, max=1)
        @Description(shortDefinition="Body site to administer to", formalDefinition="A coded specification of the anatomic site where the medication first enters the body." )
        protected CodeableConcept site;

        /**
         * A code specifying the route or physiological path of administration of a therapeutic agent into or onto a patient.
         */
        @Child(name = "route", type = {CodeableConcept.class}, order=6, min=0, max=1)
        @Description(shortDefinition="How drug should enter body", formalDefinition="A code specifying the route or physiological path of administration of a therapeutic agent into or onto a patient." )
        protected CodeableConcept route;

        /**
         * A coded value indicating the method by which the medication is introduced into or onto the body. Most commonly used for injections.  Examples:  Slow Push; Deep IV.Terminologies used often pre-coordinate this term with the route and or form of administration.
         */
        @Child(name = "method", type = {CodeableConcept.class}, order=7, min=0, max=1)
        @Description(shortDefinition="Technique for administering medication", formalDefinition="A coded value indicating the method by which the medication is introduced into or onto the body. Most commonly used for injections.  Examples:  Slow Push; Deep IV.\r\rTerminologies used often pre-coordinate this term with the route and or form of administration." )
        protected CodeableConcept method;

        /**
         * The amount of therapeutic or other substance given at one administration event.
         */
        @Child(name = "dose", type = {Range.class, Quantity.class}, order=8, min=0, max=1)
        @Description(shortDefinition="Amount of medication per dose", formalDefinition="The amount of therapeutic or other substance given at one administration event." )
        protected Type dose;

        /**
         * Identifies the speed with which the substance is introduced into the subject. Typically the rate for an infusion. 200ml in 2 hours.
         */
        @Child(name = "rate", type = {Ratio.class}, order=9, min=0, max=1)
        @Description(shortDefinition="Amount of medication per unit of time", formalDefinition="Identifies the speed with which the substance is introduced into the subject. Typically the rate for an infusion. 200ml in 2 hours." )
        protected Ratio rate;

        /**
         * The maximum total quantity of a therapeutic substance that may be administered to a subject over the period of time. E.g. 1000mg in 24 hours.
         */
        @Child(name = "maxDosePerPeriod", type = {Ratio.class}, order=10, min=0, max=1)
        @Description(shortDefinition="Upper limit on medication per unit of time", formalDefinition="The maximum total quantity of a therapeutic substance that may be administered to a subject over the period of time. E.g. 1000mg in 24 hours." )
        protected Ratio maxDosePerPeriod;

        private static final long serialVersionUID = -1144367815L;

    /*
     * Constructor
     */
      public MedicationPrescriptionDosageInstructionComponent() {
        super();
      }

        /**
         * @return {@link #text} (Free text dosage instructions can be used for cases where the instructions are too complex to code. When coded instructions are present, the free text instructions may still be present for display to humans taking or administering the medication.). This is the underlying object with id, value and extensions. The accessor "getText" gives direct access to the value
         */
        public StringType getTextElement() { 
          if (this.text == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationPrescriptionDosageInstructionComponent.text");
            else if (Configuration.doAutoCreate())
              this.text = new StringType(); // bb
          return this.text;
        }

        public boolean hasTextElement() { 
          return this.text != null && !this.text.isEmpty();
        }

        public boolean hasText() { 
          return this.text != null && !this.text.isEmpty();
        }

        /**
         * @param value {@link #text} (Free text dosage instructions can be used for cases where the instructions are too complex to code. When coded instructions are present, the free text instructions may still be present for display to humans taking or administering the medication.). This is the underlying object with id, value and extensions. The accessor "getText" gives direct access to the value
         */
        public MedicationPrescriptionDosageInstructionComponent setTextElement(StringType value) { 
          this.text = value;
          return this;
        }

        /**
         * @return Free text dosage instructions can be used for cases where the instructions are too complex to code. When coded instructions are present, the free text instructions may still be present for display to humans taking or administering the medication.
         */
        public String getText() { 
          return this.text == null ? null : this.text.getValue();
        }

        /**
         * @param value Free text dosage instructions can be used for cases where the instructions are too complex to code. When coded instructions are present, the free text instructions may still be present for display to humans taking or administering the medication.
         */
        public MedicationPrescriptionDosageInstructionComponent setText(String value) { 
          if (Utilities.noString(value))
            this.text = null;
          else {
            if (this.text == null)
              this.text = new StringType();
            this.text.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #additionalInstructions} (Additional instructions such as "Swallow with plenty of water" which may or may not be coded.)
         */
        public CodeableConcept getAdditionalInstructions() { 
          if (this.additionalInstructions == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationPrescriptionDosageInstructionComponent.additionalInstructions");
            else if (Configuration.doAutoCreate())
              this.additionalInstructions = new CodeableConcept(); // cc
          return this.additionalInstructions;
        }

        public boolean hasAdditionalInstructions() { 
          return this.additionalInstructions != null && !this.additionalInstructions.isEmpty();
        }

        /**
         * @param value {@link #additionalInstructions} (Additional instructions such as "Swallow with plenty of water" which may or may not be coded.)
         */
        public MedicationPrescriptionDosageInstructionComponent setAdditionalInstructions(CodeableConcept value) { 
          this.additionalInstructions = value;
          return this;
        }

        /**
         * @return {@link #scheduled} (The timing schedule for giving the medication to the patient.  The Schedule data type allows many different expressions, for example.  "Every  8 hours"; "Three times a day"; "1/2 an hour before breakfast for 10 days from 23-Dec 2011:";  "15 Oct 2013, 17 Oct 2013 and 1 Nov 2013".)
         */
        public Type getScheduled() { 
          return this.scheduled;
        }

        /**
         * @return {@link #scheduled} (The timing schedule for giving the medication to the patient.  The Schedule data type allows many different expressions, for example.  "Every  8 hours"; "Three times a day"; "1/2 an hour before breakfast for 10 days from 23-Dec 2011:";  "15 Oct 2013, 17 Oct 2013 and 1 Nov 2013".)
         */
        public DateTimeType getScheduledDateTimeType() throws Exception { 
          if (!(this.scheduled instanceof DateTimeType))
            throw new Exception("Type mismatch: the type DateTimeType was expected, but "+this.scheduled.getClass().getName()+" was encountered");
          return (DateTimeType) this.scheduled;
        }

        public boolean hasScheduledDateTimeType() throws Exception { 
          return this.scheduled instanceof DateTimeType;
        }

        /**
         * @return {@link #scheduled} (The timing schedule for giving the medication to the patient.  The Schedule data type allows many different expressions, for example.  "Every  8 hours"; "Three times a day"; "1/2 an hour before breakfast for 10 days from 23-Dec 2011:";  "15 Oct 2013, 17 Oct 2013 and 1 Nov 2013".)
         */
        public Period getScheduledPeriod() throws Exception { 
          if (!(this.scheduled instanceof Period))
            throw new Exception("Type mismatch: the type Period was expected, but "+this.scheduled.getClass().getName()+" was encountered");
          return (Period) this.scheduled;
        }

        public boolean hasScheduledPeriod() throws Exception { 
          return this.scheduled instanceof Period;
        }

        /**
         * @return {@link #scheduled} (The timing schedule for giving the medication to the patient.  The Schedule data type allows many different expressions, for example.  "Every  8 hours"; "Three times a day"; "1/2 an hour before breakfast for 10 days from 23-Dec 2011:";  "15 Oct 2013, 17 Oct 2013 and 1 Nov 2013".)
         */
        public Timing getScheduledTiming() throws Exception { 
          if (!(this.scheduled instanceof Timing))
            throw new Exception("Type mismatch: the type Timing was expected, but "+this.scheduled.getClass().getName()+" was encountered");
          return (Timing) this.scheduled;
        }

        public boolean hasScheduledTiming() throws Exception { 
          return this.scheduled instanceof Timing;
        }

        public boolean hasScheduled() { 
          return this.scheduled != null && !this.scheduled.isEmpty();
        }

        /**
         * @param value {@link #scheduled} (The timing schedule for giving the medication to the patient.  The Schedule data type allows many different expressions, for example.  "Every  8 hours"; "Three times a day"; "1/2 an hour before breakfast for 10 days from 23-Dec 2011:";  "15 Oct 2013, 17 Oct 2013 and 1 Nov 2013".)
         */
        public MedicationPrescriptionDosageInstructionComponent setScheduled(Type value) { 
          this.scheduled = value;
          return this;
        }

        /**
         * @return {@link #asNeeded} (If set to true or if specified as a CodeableConcept, indicates that the medication is only taken when needed within the specified schedule rather than at every scheduled dose.  If a CodeableConcept is present, it indicates the pre-condition for taking the Medication.)
         */
        public Type getAsNeeded() { 
          return this.asNeeded;
        }

        /**
         * @return {@link #asNeeded} (If set to true or if specified as a CodeableConcept, indicates that the medication is only taken when needed within the specified schedule rather than at every scheduled dose.  If a CodeableConcept is present, it indicates the pre-condition for taking the Medication.)
         */
        public BooleanType getAsNeededBooleanType() throws Exception { 
          if (!(this.asNeeded instanceof BooleanType))
            throw new Exception("Type mismatch: the type BooleanType was expected, but "+this.asNeeded.getClass().getName()+" was encountered");
          return (BooleanType) this.asNeeded;
        }

        public boolean hasAsNeededBooleanType() throws Exception { 
          return this.asNeeded instanceof BooleanType;
        }

        /**
         * @return {@link #asNeeded} (If set to true or if specified as a CodeableConcept, indicates that the medication is only taken when needed within the specified schedule rather than at every scheduled dose.  If a CodeableConcept is present, it indicates the pre-condition for taking the Medication.)
         */
        public CodeableConcept getAsNeededCodeableConcept() throws Exception { 
          if (!(this.asNeeded instanceof CodeableConcept))
            throw new Exception("Type mismatch: the type CodeableConcept was expected, but "+this.asNeeded.getClass().getName()+" was encountered");
          return (CodeableConcept) this.asNeeded;
        }

        public boolean hasAsNeededCodeableConcept() throws Exception { 
          return this.asNeeded instanceof CodeableConcept;
        }

        public boolean hasAsNeeded() { 
          return this.asNeeded != null && !this.asNeeded.isEmpty();
        }

        /**
         * @param value {@link #asNeeded} (If set to true or if specified as a CodeableConcept, indicates that the medication is only taken when needed within the specified schedule rather than at every scheduled dose.  If a CodeableConcept is present, it indicates the pre-condition for taking the Medication.)
         */
        public MedicationPrescriptionDosageInstructionComponent setAsNeeded(Type value) { 
          this.asNeeded = value;
          return this;
        }

        /**
         * @return {@link #site} (A coded specification of the anatomic site where the medication first enters the body.)
         */
        public CodeableConcept getSite() { 
          if (this.site == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationPrescriptionDosageInstructionComponent.site");
            else if (Configuration.doAutoCreate())
              this.site = new CodeableConcept(); // cc
          return this.site;
        }

        public boolean hasSite() { 
          return this.site != null && !this.site.isEmpty();
        }

        /**
         * @param value {@link #site} (A coded specification of the anatomic site where the medication first enters the body.)
         */
        public MedicationPrescriptionDosageInstructionComponent setSite(CodeableConcept value) { 
          this.site = value;
          return this;
        }

        /**
         * @return {@link #route} (A code specifying the route or physiological path of administration of a therapeutic agent into or onto a patient.)
         */
        public CodeableConcept getRoute() { 
          if (this.route == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationPrescriptionDosageInstructionComponent.route");
            else if (Configuration.doAutoCreate())
              this.route = new CodeableConcept(); // cc
          return this.route;
        }

        public boolean hasRoute() { 
          return this.route != null && !this.route.isEmpty();
        }

        /**
         * @param value {@link #route} (A code specifying the route or physiological path of administration of a therapeutic agent into or onto a patient.)
         */
        public MedicationPrescriptionDosageInstructionComponent setRoute(CodeableConcept value) { 
          this.route = value;
          return this;
        }

        /**
         * @return {@link #method} (A coded value indicating the method by which the medication is introduced into or onto the body. Most commonly used for injections.  Examples:  Slow Push; Deep IV.Terminologies used often pre-coordinate this term with the route and or form of administration.)
         */
        public CodeableConcept getMethod() { 
          if (this.method == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationPrescriptionDosageInstructionComponent.method");
            else if (Configuration.doAutoCreate())
              this.method = new CodeableConcept(); // cc
          return this.method;
        }

        public boolean hasMethod() { 
          return this.method != null && !this.method.isEmpty();
        }

        /**
         * @param value {@link #method} (A coded value indicating the method by which the medication is introduced into or onto the body. Most commonly used for injections.  Examples:  Slow Push; Deep IV.Terminologies used often pre-coordinate this term with the route and or form of administration.)
         */
        public MedicationPrescriptionDosageInstructionComponent setMethod(CodeableConcept value) { 
          this.method = value;
          return this;
        }

        /**
         * @return {@link #dose} (The amount of therapeutic or other substance given at one administration event.)
         */
        public Type getDose() { 
          return this.dose;
        }

        /**
         * @return {@link #dose} (The amount of therapeutic or other substance given at one administration event.)
         */
        public Range getDoseRange() throws Exception { 
          if (!(this.dose instanceof Range))
            throw new Exception("Type mismatch: the type Range was expected, but "+this.dose.getClass().getName()+" was encountered");
          return (Range) this.dose;
        }

        public boolean hasDoseRange() throws Exception { 
          return this.dose instanceof Range;
        }

        /**
         * @return {@link #dose} (The amount of therapeutic or other substance given at one administration event.)
         */
        public Quantity getDoseQuantity() throws Exception { 
          if (!(this.dose instanceof Quantity))
            throw new Exception("Type mismatch: the type Quantity was expected, but "+this.dose.getClass().getName()+" was encountered");
          return (Quantity) this.dose;
        }

        public boolean hasDoseQuantity() throws Exception { 
          return this.dose instanceof Quantity;
        }

        public boolean hasDose() { 
          return this.dose != null && !this.dose.isEmpty();
        }

        /**
         * @param value {@link #dose} (The amount of therapeutic or other substance given at one administration event.)
         */
        public MedicationPrescriptionDosageInstructionComponent setDose(Type value) { 
          this.dose = value;
          return this;
        }

        /**
         * @return {@link #rate} (Identifies the speed with which the substance is introduced into the subject. Typically the rate for an infusion. 200ml in 2 hours.)
         */
        public Ratio getRate() { 
          if (this.rate == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationPrescriptionDosageInstructionComponent.rate");
            else if (Configuration.doAutoCreate())
              this.rate = new Ratio(); // cc
          return this.rate;
        }

        public boolean hasRate() { 
          return this.rate != null && !this.rate.isEmpty();
        }

        /**
         * @param value {@link #rate} (Identifies the speed with which the substance is introduced into the subject. Typically the rate for an infusion. 200ml in 2 hours.)
         */
        public MedicationPrescriptionDosageInstructionComponent setRate(Ratio value) { 
          this.rate = value;
          return this;
        }

        /**
         * @return {@link #maxDosePerPeriod} (The maximum total quantity of a therapeutic substance that may be administered to a subject over the period of time. E.g. 1000mg in 24 hours.)
         */
        public Ratio getMaxDosePerPeriod() { 
          if (this.maxDosePerPeriod == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationPrescriptionDosageInstructionComponent.maxDosePerPeriod");
            else if (Configuration.doAutoCreate())
              this.maxDosePerPeriod = new Ratio(); // cc
          return this.maxDosePerPeriod;
        }

        public boolean hasMaxDosePerPeriod() { 
          return this.maxDosePerPeriod != null && !this.maxDosePerPeriod.isEmpty();
        }

        /**
         * @param value {@link #maxDosePerPeriod} (The maximum total quantity of a therapeutic substance that may be administered to a subject over the period of time. E.g. 1000mg in 24 hours.)
         */
        public MedicationPrescriptionDosageInstructionComponent setMaxDosePerPeriod(Ratio value) { 
          this.maxDosePerPeriod = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("text", "string", "Free text dosage instructions can be used for cases where the instructions are too complex to code. When coded instructions are present, the free text instructions may still be present for display to humans taking or administering the medication.", 0, java.lang.Integer.MAX_VALUE, text));
          childrenList.add(new Property("additionalInstructions", "CodeableConcept", "Additional instructions such as 'Swallow with plenty of water' which may or may not be coded.", 0, java.lang.Integer.MAX_VALUE, additionalInstructions));
          childrenList.add(new Property("scheduled[x]", "dateTime|Period|Timing", "The timing schedule for giving the medication to the patient.  The Schedule data type allows many different expressions, for example.  'Every  8 hours'; 'Three times a day'; '1/2 an hour before breakfast for 10 days from 23-Dec 2011:';  '15 Oct 2013, 17 Oct 2013 and 1 Nov 2013'.", 0, java.lang.Integer.MAX_VALUE, scheduled));
          childrenList.add(new Property("asNeeded[x]", "boolean|CodeableConcept", "If set to true or if specified as a CodeableConcept, indicates that the medication is only taken when needed within the specified schedule rather than at every scheduled dose.  If a CodeableConcept is present, it indicates the pre-condition for taking the Medication.", 0, java.lang.Integer.MAX_VALUE, asNeeded));
          childrenList.add(new Property("site", "CodeableConcept", "A coded specification of the anatomic site where the medication first enters the body.", 0, java.lang.Integer.MAX_VALUE, site));
          childrenList.add(new Property("route", "CodeableConcept", "A code specifying the route or physiological path of administration of a therapeutic agent into or onto a patient.", 0, java.lang.Integer.MAX_VALUE, route));
          childrenList.add(new Property("method", "CodeableConcept", "A coded value indicating the method by which the medication is introduced into or onto the body. Most commonly used for injections.  Examples:  Slow Push; Deep IV.\r\rTerminologies used often pre-coordinate this term with the route and or form of administration.", 0, java.lang.Integer.MAX_VALUE, method));
          childrenList.add(new Property("dose[x]", "Range|Quantity", "The amount of therapeutic or other substance given at one administration event.", 0, java.lang.Integer.MAX_VALUE, dose));
          childrenList.add(new Property("rate", "Ratio", "Identifies the speed with which the substance is introduced into the subject. Typically the rate for an infusion. 200ml in 2 hours.", 0, java.lang.Integer.MAX_VALUE, rate));
          childrenList.add(new Property("maxDosePerPeriod", "Ratio", "The maximum total quantity of a therapeutic substance that may be administered to a subject over the period of time. E.g. 1000mg in 24 hours.", 0, java.lang.Integer.MAX_VALUE, maxDosePerPeriod));
        }

      public MedicationPrescriptionDosageInstructionComponent copy() {
        MedicationPrescriptionDosageInstructionComponent dst = new MedicationPrescriptionDosageInstructionComponent();
        copyValues(dst);
        dst.text = text == null ? null : text.copy();
        dst.additionalInstructions = additionalInstructions == null ? null : additionalInstructions.copy();
        dst.scheduled = scheduled == null ? null : scheduled.copy();
        dst.asNeeded = asNeeded == null ? null : asNeeded.copy();
        dst.site = site == null ? null : site.copy();
        dst.route = route == null ? null : route.copy();
        dst.method = method == null ? null : method.copy();
        dst.dose = dose == null ? null : dose.copy();
        dst.rate = rate == null ? null : rate.copy();
        dst.maxDosePerPeriod = maxDosePerPeriod == null ? null : maxDosePerPeriod.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof MedicationPrescriptionDosageInstructionComponent))
          return false;
        MedicationPrescriptionDosageInstructionComponent o = (MedicationPrescriptionDosageInstructionComponent) other;
        return compareDeep(text, o.text, true) && compareDeep(additionalInstructions, o.additionalInstructions, true)
           && compareDeep(scheduled, o.scheduled, true) && compareDeep(asNeeded, o.asNeeded, true) && compareDeep(site, o.site, true)
           && compareDeep(route, o.route, true) && compareDeep(method, o.method, true) && compareDeep(dose, o.dose, true)
           && compareDeep(rate, o.rate, true) && compareDeep(maxDosePerPeriod, o.maxDosePerPeriod, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof MedicationPrescriptionDosageInstructionComponent))
          return false;
        MedicationPrescriptionDosageInstructionComponent o = (MedicationPrescriptionDosageInstructionComponent) other;
        return compareValues(text, o.text, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (text == null || text.isEmpty()) && (additionalInstructions == null || additionalInstructions.isEmpty())
           && (scheduled == null || scheduled.isEmpty()) && (asNeeded == null || asNeeded.isEmpty())
           && (site == null || site.isEmpty()) && (route == null || route.isEmpty()) && (method == null || method.isEmpty())
           && (dose == null || dose.isEmpty()) && (rate == null || rate.isEmpty()) && (maxDosePerPeriod == null || maxDosePerPeriod.isEmpty())
          ;
      }

  }

    @Block()
    public static class MedicationPrescriptionDispenseComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Identifies the medication being administered. This is a link to a resource that represents the medication which may be the details of the medication or simply an attribute carrying a code that identifies the medication from a known list of medications.
         */
        @Child(name = "medication", type = {CodeableConcept.class, Medication.class}, order=1, min=0, max=1)
        @Description(shortDefinition="Product to be supplied", formalDefinition="Identifies the medication being administered. This is a link to a resource that represents the medication which may be the details of the medication or simply an attribute carrying a code that identifies the medication from a known list of medications." )
        protected Type medication;

        /**
         * Design Comments: This indicates the validity period of a prescription (stale dating the Prescription) It reflects the prescriber perspective for the validity of the prescription. Dispenses must not be made against the prescription outside of this period. The lower-bound of the Dispensing Window signifies the earliest date that the prescription can be filled for the first time. If an upper-bound is not specified then the Prescription is open-ended or will default to a stale-date based on regulations. Rationale: Indicates when the Prescription becomes valid, and when it ceases to be a dispensable Prescription.
         */
        @Child(name = "validityPeriod", type = {Period.class}, order=2, min=0, max=1)
        @Description(shortDefinition="Time period supply is authorized for", formalDefinition="Design Comments: This indicates the validity period of a prescription (stale dating the Prescription) \rIt reflects the prescriber perspective for the validity of the prescription. Dispenses must not be made against the prescription outside of this period. The lower-bound of the Dispensing Window signifies the earliest date that the prescription can be filled for the first time. If an upper-bound is not specified then the Prescription is open-ended or will default to a stale-date based on regulations. \rRationale: Indicates when the Prescription becomes valid, and when it ceases to be a dispensable Prescription." )
        protected Period validityPeriod;

        /**
         * An integer indicating the number of repeats of the Dispense. UsageNotes: For example, the number of times the prescribed quantity is to be supplied including the initial standard fill.
         */
        @Child(name = "numberOfRepeatsAllowed", type = {PositiveIntType.class}, order=3, min=0, max=1)
        @Description(shortDefinition="# of refills authorized", formalDefinition="An integer indicating the number of repeats of the Dispense. \rUsageNotes: For example, the number of times the prescribed quantity is to be supplied including the initial standard fill." )
        protected PositiveIntType numberOfRepeatsAllowed;

        /**
         * The amount that is to be dispensed for one fill.
         */
        @Child(name = "quantity", type = {Quantity.class}, order=4, min=0, max=1)
        @Description(shortDefinition="Amount of medication to supply per dispense", formalDefinition="The amount that is to be dispensed for one fill." )
        protected Quantity quantity;

        /**
         * Identifies the period time over which the supplied product is expected to be used, or the length of time the dispense is expected to last. In some situations, this attribute may be used instead of quantity to identify the amount supplied by how long it is expected to last, rather than the physical quantity issued, e.g. 90 days supply of medication (based on an ordered dosage) When possible, it is always better to specify quantity, as this tends to be more precise. expectedSupplyDuration will always be an estimate that can be influenced by external factors.
         */
        @Child(name = "expectedSupplyDuration", type = {Duration.class}, order=5, min=0, max=1)
        @Description(shortDefinition="Days supply per dispense", formalDefinition="Identifies the period time over which the supplied product is expected to be used, or the length of time the dispense is expected to last. \rIn some situations, this attribute may be used instead of quantity to identify the amount supplied by how long it is expected to last, rather than the physical quantity issued, e.g. 90 days supply of medication (based on an ordered dosage) When possible, it is always better to specify quantity, as this tends to be more precise. expectedSupplyDuration will always be an estimate that can be influenced by external factors." )
        protected Duration expectedSupplyDuration;

        private static final long serialVersionUID = 312252966L;

    /*
     * Constructor
     */
      public MedicationPrescriptionDispenseComponent() {
        super();
      }

        /**
         * @return {@link #medication} (Identifies the medication being administered. This is a link to a resource that represents the medication which may be the details of the medication or simply an attribute carrying a code that identifies the medication from a known list of medications.)
         */
        public Type getMedication() { 
          return this.medication;
        }

        /**
         * @return {@link #medication} (Identifies the medication being administered. This is a link to a resource that represents the medication which may be the details of the medication or simply an attribute carrying a code that identifies the medication from a known list of medications.)
         */
        public CodeableConcept getMedicationCodeableConcept() throws Exception { 
          if (!(this.medication instanceof CodeableConcept))
            throw new Exception("Type mismatch: the type CodeableConcept was expected, but "+this.medication.getClass().getName()+" was encountered");
          return (CodeableConcept) this.medication;
        }

        public boolean hasMedicationCodeableConcept() throws Exception { 
          return this.medication instanceof CodeableConcept;
        }

        /**
         * @return {@link #medication} (Identifies the medication being administered. This is a link to a resource that represents the medication which may be the details of the medication or simply an attribute carrying a code that identifies the medication from a known list of medications.)
         */
        public Reference getMedicationReference() throws Exception { 
          if (!(this.medication instanceof Reference))
            throw new Exception("Type mismatch: the type Reference was expected, but "+this.medication.getClass().getName()+" was encountered");
          return (Reference) this.medication;
        }

        public boolean hasMedicationReference() throws Exception { 
          return this.medication instanceof Reference;
        }

        public boolean hasMedication() { 
          return this.medication != null && !this.medication.isEmpty();
        }

        /**
         * @param value {@link #medication} (Identifies the medication being administered. This is a link to a resource that represents the medication which may be the details of the medication or simply an attribute carrying a code that identifies the medication from a known list of medications.)
         */
        public MedicationPrescriptionDispenseComponent setMedication(Type value) { 
          this.medication = value;
          return this;
        }

        /**
         * @return {@link #validityPeriod} (Design Comments: This indicates the validity period of a prescription (stale dating the Prescription) It reflects the prescriber perspective for the validity of the prescription. Dispenses must not be made against the prescription outside of this period. The lower-bound of the Dispensing Window signifies the earliest date that the prescription can be filled for the first time. If an upper-bound is not specified then the Prescription is open-ended or will default to a stale-date based on regulations. Rationale: Indicates when the Prescription becomes valid, and when it ceases to be a dispensable Prescription.)
         */
        public Period getValidityPeriod() { 
          if (this.validityPeriod == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationPrescriptionDispenseComponent.validityPeriod");
            else if (Configuration.doAutoCreate())
              this.validityPeriod = new Period(); // cc
          return this.validityPeriod;
        }

        public boolean hasValidityPeriod() { 
          return this.validityPeriod != null && !this.validityPeriod.isEmpty();
        }

        /**
         * @param value {@link #validityPeriod} (Design Comments: This indicates the validity period of a prescription (stale dating the Prescription) It reflects the prescriber perspective for the validity of the prescription. Dispenses must not be made against the prescription outside of this period. The lower-bound of the Dispensing Window signifies the earliest date that the prescription can be filled for the first time. If an upper-bound is not specified then the Prescription is open-ended or will default to a stale-date based on regulations. Rationale: Indicates when the Prescription becomes valid, and when it ceases to be a dispensable Prescription.)
         */
        public MedicationPrescriptionDispenseComponent setValidityPeriod(Period value) { 
          this.validityPeriod = value;
          return this;
        }

        /**
         * @return {@link #numberOfRepeatsAllowed} (An integer indicating the number of repeats of the Dispense. UsageNotes: For example, the number of times the prescribed quantity is to be supplied including the initial standard fill.). This is the underlying object with id, value and extensions. The accessor "getNumberOfRepeatsAllowed" gives direct access to the value
         */
        public PositiveIntType getNumberOfRepeatsAllowedElement() { 
          if (this.numberOfRepeatsAllowed == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationPrescriptionDispenseComponent.numberOfRepeatsAllowed");
            else if (Configuration.doAutoCreate())
              this.numberOfRepeatsAllowed = new PositiveIntType(); // bb
          return this.numberOfRepeatsAllowed;
        }

        public boolean hasNumberOfRepeatsAllowedElement() { 
          return this.numberOfRepeatsAllowed != null && !this.numberOfRepeatsAllowed.isEmpty();
        }

        public boolean hasNumberOfRepeatsAllowed() { 
          return this.numberOfRepeatsAllowed != null && !this.numberOfRepeatsAllowed.isEmpty();
        }

        /**
         * @param value {@link #numberOfRepeatsAllowed} (An integer indicating the number of repeats of the Dispense. UsageNotes: For example, the number of times the prescribed quantity is to be supplied including the initial standard fill.). This is the underlying object with id, value and extensions. The accessor "getNumberOfRepeatsAllowed" gives direct access to the value
         */
        public MedicationPrescriptionDispenseComponent setNumberOfRepeatsAllowedElement(PositiveIntType value) { 
          this.numberOfRepeatsAllowed = value;
          return this;
        }

        /**
         * @return An integer indicating the number of repeats of the Dispense. UsageNotes: For example, the number of times the prescribed quantity is to be supplied including the initial standard fill.
         */
        public int getNumberOfRepeatsAllowed() { 
          return this.numberOfRepeatsAllowed == null || this.numberOfRepeatsAllowed.isEmpty() ? 0 : this.numberOfRepeatsAllowed.getValue();
        }

        /**
         * @param value An integer indicating the number of repeats of the Dispense. UsageNotes: For example, the number of times the prescribed quantity is to be supplied including the initial standard fill.
         */
        public MedicationPrescriptionDispenseComponent setNumberOfRepeatsAllowed(int value) { 
            if (this.numberOfRepeatsAllowed == null)
              this.numberOfRepeatsAllowed = new PositiveIntType();
            this.numberOfRepeatsAllowed.setValue(value);
          return this;
        }

        /**
         * @return {@link #quantity} (The amount that is to be dispensed for one fill.)
         */
        public Quantity getQuantity() { 
          if (this.quantity == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationPrescriptionDispenseComponent.quantity");
            else if (Configuration.doAutoCreate())
              this.quantity = new Quantity(); // cc
          return this.quantity;
        }

        public boolean hasQuantity() { 
          return this.quantity != null && !this.quantity.isEmpty();
        }

        /**
         * @param value {@link #quantity} (The amount that is to be dispensed for one fill.)
         */
        public MedicationPrescriptionDispenseComponent setQuantity(Quantity value) { 
          this.quantity = value;
          return this;
        }

        /**
         * @return {@link #expectedSupplyDuration} (Identifies the period time over which the supplied product is expected to be used, or the length of time the dispense is expected to last. In some situations, this attribute may be used instead of quantity to identify the amount supplied by how long it is expected to last, rather than the physical quantity issued, e.g. 90 days supply of medication (based on an ordered dosage) When possible, it is always better to specify quantity, as this tends to be more precise. expectedSupplyDuration will always be an estimate that can be influenced by external factors.)
         */
        public Duration getExpectedSupplyDuration() { 
          if (this.expectedSupplyDuration == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationPrescriptionDispenseComponent.expectedSupplyDuration");
            else if (Configuration.doAutoCreate())
              this.expectedSupplyDuration = new Duration(); // cc
          return this.expectedSupplyDuration;
        }

        public boolean hasExpectedSupplyDuration() { 
          return this.expectedSupplyDuration != null && !this.expectedSupplyDuration.isEmpty();
        }

        /**
         * @param value {@link #expectedSupplyDuration} (Identifies the period time over which the supplied product is expected to be used, or the length of time the dispense is expected to last. In some situations, this attribute may be used instead of quantity to identify the amount supplied by how long it is expected to last, rather than the physical quantity issued, e.g. 90 days supply of medication (based on an ordered dosage) When possible, it is always better to specify quantity, as this tends to be more precise. expectedSupplyDuration will always be an estimate that can be influenced by external factors.)
         */
        public MedicationPrescriptionDispenseComponent setExpectedSupplyDuration(Duration value) { 
          this.expectedSupplyDuration = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("medication[x]", "CodeableConcept|Reference(Medication)", "Identifies the medication being administered. This is a link to a resource that represents the medication which may be the details of the medication or simply an attribute carrying a code that identifies the medication from a known list of medications.", 0, java.lang.Integer.MAX_VALUE, medication));
          childrenList.add(new Property("validityPeriod", "Period", "Design Comments: This indicates the validity period of a prescription (stale dating the Prescription) \rIt reflects the prescriber perspective for the validity of the prescription. Dispenses must not be made against the prescription outside of this period. The lower-bound of the Dispensing Window signifies the earliest date that the prescription can be filled for the first time. If an upper-bound is not specified then the Prescription is open-ended or will default to a stale-date based on regulations. \rRationale: Indicates when the Prescription becomes valid, and when it ceases to be a dispensable Prescription.", 0, java.lang.Integer.MAX_VALUE, validityPeriod));
          childrenList.add(new Property("numberOfRepeatsAllowed", "positiveInt", "An integer indicating the number of repeats of the Dispense. \rUsageNotes: For example, the number of times the prescribed quantity is to be supplied including the initial standard fill.", 0, java.lang.Integer.MAX_VALUE, numberOfRepeatsAllowed));
          childrenList.add(new Property("quantity", "Quantity", "The amount that is to be dispensed for one fill.", 0, java.lang.Integer.MAX_VALUE, quantity));
          childrenList.add(new Property("expectedSupplyDuration", "Duration", "Identifies the period time over which the supplied product is expected to be used, or the length of time the dispense is expected to last. \rIn some situations, this attribute may be used instead of quantity to identify the amount supplied by how long it is expected to last, rather than the physical quantity issued, e.g. 90 days supply of medication (based on an ordered dosage) When possible, it is always better to specify quantity, as this tends to be more precise. expectedSupplyDuration will always be an estimate that can be influenced by external factors.", 0, java.lang.Integer.MAX_VALUE, expectedSupplyDuration));
        }

      public MedicationPrescriptionDispenseComponent copy() {
        MedicationPrescriptionDispenseComponent dst = new MedicationPrescriptionDispenseComponent();
        copyValues(dst);
        dst.medication = medication == null ? null : medication.copy();
        dst.validityPeriod = validityPeriod == null ? null : validityPeriod.copy();
        dst.numberOfRepeatsAllowed = numberOfRepeatsAllowed == null ? null : numberOfRepeatsAllowed.copy();
        dst.quantity = quantity == null ? null : quantity.copy();
        dst.expectedSupplyDuration = expectedSupplyDuration == null ? null : expectedSupplyDuration.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof MedicationPrescriptionDispenseComponent))
          return false;
        MedicationPrescriptionDispenseComponent o = (MedicationPrescriptionDispenseComponent) other;
        return compareDeep(medication, o.medication, true) && compareDeep(validityPeriod, o.validityPeriod, true)
           && compareDeep(numberOfRepeatsAllowed, o.numberOfRepeatsAllowed, true) && compareDeep(quantity, o.quantity, true)
           && compareDeep(expectedSupplyDuration, o.expectedSupplyDuration, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof MedicationPrescriptionDispenseComponent))
          return false;
        MedicationPrescriptionDispenseComponent o = (MedicationPrescriptionDispenseComponent) other;
        return compareValues(numberOfRepeatsAllowed, o.numberOfRepeatsAllowed, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (medication == null || medication.isEmpty()) && (validityPeriod == null || validityPeriod.isEmpty())
           && (numberOfRepeatsAllowed == null || numberOfRepeatsAllowed.isEmpty()) && (quantity == null || quantity.isEmpty())
           && (expectedSupplyDuration == null || expectedSupplyDuration.isEmpty());
      }

  }

    @Block()
    public static class MedicationPrescriptionSubstitutionComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A code signifying whether a different drug should be dispensed from what was prescribed.
         */
        @Child(name = "type", type = {CodeableConcept.class}, order=1, min=1, max=1)
        @Description(shortDefinition="generic | formulary +", formalDefinition="A code signifying whether a different drug should be dispensed from what was prescribed." )
        protected CodeableConcept type;

        /**
         * Indicates the reason for the substitution, or why substitution must or must not be performed.
         */
        @Child(name = "reason", type = {CodeableConcept.class}, order=2, min=0, max=1)
        @Description(shortDefinition="Why should substitution (not) be made", formalDefinition="Indicates the reason for the substitution, or why substitution must or must not be performed." )
        protected CodeableConcept reason;

        private static final long serialVersionUID = 1693602518L;

    /*
     * Constructor
     */
      public MedicationPrescriptionSubstitutionComponent() {
        super();
      }

    /*
     * Constructor
     */
      public MedicationPrescriptionSubstitutionComponent(CodeableConcept type) {
        super();
        this.type = type;
      }

        /**
         * @return {@link #type} (A code signifying whether a different drug should be dispensed from what was prescribed.)
         */
        public CodeableConcept getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationPrescriptionSubstitutionComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new CodeableConcept(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (A code signifying whether a different drug should be dispensed from what was prescribed.)
         */
        public MedicationPrescriptionSubstitutionComponent setType(CodeableConcept value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #reason} (Indicates the reason for the substitution, or why substitution must or must not be performed.)
         */
        public CodeableConcept getReason() { 
          if (this.reason == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationPrescriptionSubstitutionComponent.reason");
            else if (Configuration.doAutoCreate())
              this.reason = new CodeableConcept(); // cc
          return this.reason;
        }

        public boolean hasReason() { 
          return this.reason != null && !this.reason.isEmpty();
        }

        /**
         * @param value {@link #reason} (Indicates the reason for the substitution, or why substitution must or must not be performed.)
         */
        public MedicationPrescriptionSubstitutionComponent setReason(CodeableConcept value) { 
          this.reason = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("type", "CodeableConcept", "A code signifying whether a different drug should be dispensed from what was prescribed.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("reason", "CodeableConcept", "Indicates the reason for the substitution, or why substitution must or must not be performed.", 0, java.lang.Integer.MAX_VALUE, reason));
        }

      public MedicationPrescriptionSubstitutionComponent copy() {
        MedicationPrescriptionSubstitutionComponent dst = new MedicationPrescriptionSubstitutionComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.reason = reason == null ? null : reason.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof MedicationPrescriptionSubstitutionComponent))
          return false;
        MedicationPrescriptionSubstitutionComponent o = (MedicationPrescriptionSubstitutionComponent) other;
        return compareDeep(type, o.type, true) && compareDeep(reason, o.reason, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof MedicationPrescriptionSubstitutionComponent))
          return false;
        MedicationPrescriptionSubstitutionComponent o = (MedicationPrescriptionSubstitutionComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (type == null || type.isEmpty()) && (reason == null || reason.isEmpty())
          ;
      }

  }

    /**
     * External identifier - one that would be used by another non-FHIR system - for example a re-imbursement system might issue its own id for each prescription that is created.  This is particularly important where FHIR only provides part of an erntire workflow process where records have to be tracked through an entire system.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="External identifier", formalDefinition="External identifier - one that would be used by another non-FHIR system - for example a re-imbursement system might issue its own id for each prescription that is created.  This is particularly important where FHIR only provides part of an erntire workflow process where records have to be tracked through an entire system." )
    protected List<Identifier> identifier;

    /**
     * The date (and perhaps time) when the prescription was written.
     */
    @Child(name = "dateWritten", type = {DateTimeType.class}, order=1, min=0, max=1)
    @Description(shortDefinition="When prescription was authorized", formalDefinition="The date (and perhaps time) when the prescription was written." )
    protected DateTimeType dateWritten;

    /**
     * A code specifying the state of the order.  Generally this will be active or completed state.
     */
    @Child(name = "status", type = {CodeType.class}, order=2, min=0, max=1)
    @Description(shortDefinition="active | on-hold | completed | entered-in-error | stopped | superseded | draft", formalDefinition="A code specifying the state of the order.  Generally this will be active or completed state." )
    protected Enumeration<MedicationPrescriptionStatus> status;

    /**
     * A link to a resource representing the person to whom the medication will be given.
     */
    @Child(name = "patient", type = {Patient.class}, order=3, min=0, max=1)
    @Description(shortDefinition="Who prescription is for", formalDefinition="A link to a resource representing the person to whom the medication will be given." )
    protected Reference patient;

    /**
     * The actual object that is the target of the reference (A link to a resource representing the person to whom the medication will be given.)
     */
    protected Patient patientTarget;

    /**
     * The healthcare professional responsible for authorizing the prescription.
     */
    @Child(name = "prescriber", type = {Practitioner.class}, order=4, min=0, max=1)
    @Description(shortDefinition="Who ordered the medication(s)", formalDefinition="The healthcare professional responsible for authorizing the prescription." )
    protected Reference prescriber;

    /**
     * The actual object that is the target of the reference (The healthcare professional responsible for authorizing the prescription.)
     */
    protected Practitioner prescriberTarget;

    /**
     * A link to a resource that identifies the particular occurrence of contact between patient and health care provider.
     */
    @Child(name = "encounter", type = {Encounter.class}, order=5, min=0, max=1)
    @Description(shortDefinition="Created during encounter / admission / stay", formalDefinition="A link to a resource that identifies the particular occurrence of contact between patient and health care provider." )
    protected Reference encounter;

    /**
     * The actual object that is the target of the reference (A link to a resource that identifies the particular occurrence of contact between patient and health care provider.)
     */
    protected Encounter encounterTarget;

    /**
     * Can be the reason or the indication for writing the prescription.
     */
    @Child(name = "reason", type = {CodeableConcept.class, Condition.class}, order=6, min=0, max=1)
    @Description(shortDefinition="Reason or indication for writing the prescription", formalDefinition="Can be the reason or the indication for writing the prescription." )
    protected Type reason;

    /**
     * Extra information about the prescription that could not be conveyed by the other attributes.
     */
    @Child(name = "note", type = {StringType.class}, order=7, min=0, max=1)
    @Description(shortDefinition="Information about the prescription", formalDefinition="Extra information about the prescription that could not be conveyed by the other attributes." )
    protected StringType note;

    /**
     * Identifies the medication being administered. This is a link to a resource that represents the medication which may be the details of the medication or simply an attribute carrying a code that identifies the medication from a known list of medications.
     */
    @Child(name = "medication", type = {CodeableConcept.class, Medication.class}, order=8, min=1, max=1)
    @Description(shortDefinition="Medication to be taken", formalDefinition="Identifies the medication being administered. This is a link to a resource that represents the medication which may be the details of the medication or simply an attribute carrying a code that identifies the medication from a known list of medications." )
    protected Type medication;

    /**
     * Indicates how the medication is to be used by the patient.
     */
    @Child(name = "dosageInstruction", type = {}, order=9, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="How medication should be taken", formalDefinition="Indicates how the medication is to be used by the patient." )
    protected List<MedicationPrescriptionDosageInstructionComponent> dosageInstruction;

    /**
     * Deals with details of the dispense part of the order.
     */
    @Child(name = "dispense", type = {}, order=10, min=0, max=1)
    @Description(shortDefinition="Medication supply authorization", formalDefinition="Deals with details of the dispense part of the order." )
    protected MedicationPrescriptionDispenseComponent dispense;

    /**
     * Indicates whether or not substitution can or should be part of the dispense. In some cases substitution must happen, in other cases substitution must not happen, and in others it does not matter. This block explains the prescriber's intent. If nothing is specified substitution may be done.
     */
    @Child(name = "substitution", type = {}, order=11, min=0, max=1)
    @Description(shortDefinition="Any restrictions on medication substitution?", formalDefinition="Indicates whether or not substitution can or should be part of the dispense. In some cases substitution must happen, in other cases substitution must not happen, and in others it does not matter. This block explains the prescriber's intent. If nothing is specified substitution may be done." )
    protected MedicationPrescriptionSubstitutionComponent substitution;

    private static final long serialVersionUID = 1219917448L;

  /*
   * Constructor
   */
    public MedicationPrescription() {
      super();
    }

  /*
   * Constructor
   */
    public MedicationPrescription(Type medication) {
      super();
      this.medication = medication;
    }

    /**
     * @return {@link #identifier} (External identifier - one that would be used by another non-FHIR system - for example a re-imbursement system might issue its own id for each prescription that is created.  This is particularly important where FHIR only provides part of an erntire workflow process where records have to be tracked through an entire system.)
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
     * @return {@link #identifier} (External identifier - one that would be used by another non-FHIR system - for example a re-imbursement system might issue its own id for each prescription that is created.  This is particularly important where FHIR only provides part of an erntire workflow process where records have to be tracked through an entire system.)
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
    public MedicationPrescription addIdentifier(Identifier t) { //3
      if (t == null)
        return this;
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return this;
    }

    /**
     * @return {@link #dateWritten} (The date (and perhaps time) when the prescription was written.). This is the underlying object with id, value and extensions. The accessor "getDateWritten" gives direct access to the value
     */
    public DateTimeType getDateWrittenElement() { 
      if (this.dateWritten == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationPrescription.dateWritten");
        else if (Configuration.doAutoCreate())
          this.dateWritten = new DateTimeType(); // bb
      return this.dateWritten;
    }

    public boolean hasDateWrittenElement() { 
      return this.dateWritten != null && !this.dateWritten.isEmpty();
    }

    public boolean hasDateWritten() { 
      return this.dateWritten != null && !this.dateWritten.isEmpty();
    }

    /**
     * @param value {@link #dateWritten} (The date (and perhaps time) when the prescription was written.). This is the underlying object with id, value and extensions. The accessor "getDateWritten" gives direct access to the value
     */
    public MedicationPrescription setDateWrittenElement(DateTimeType value) { 
      this.dateWritten = value;
      return this;
    }

    /**
     * @return The date (and perhaps time) when the prescription was written.
     */
    public Date getDateWritten() { 
      return this.dateWritten == null ? null : this.dateWritten.getValue();
    }

    /**
     * @param value The date (and perhaps time) when the prescription was written.
     */
    public MedicationPrescription setDateWritten(Date value) { 
      if (value == null)
        this.dateWritten = null;
      else {
        if (this.dateWritten == null)
          this.dateWritten = new DateTimeType();
        this.dateWritten.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #status} (A code specifying the state of the order.  Generally this will be active or completed state.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<MedicationPrescriptionStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationPrescription.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<MedicationPrescriptionStatus>(new MedicationPrescriptionStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (A code specifying the state of the order.  Generally this will be active or completed state.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public MedicationPrescription setStatusElement(Enumeration<MedicationPrescriptionStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return A code specifying the state of the order.  Generally this will be active or completed state.
     */
    public MedicationPrescriptionStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value A code specifying the state of the order.  Generally this will be active or completed state.
     */
    public MedicationPrescription setStatus(MedicationPrescriptionStatus value) { 
      if (value == null)
        this.status = null;
      else {
        if (this.status == null)
          this.status = new Enumeration<MedicationPrescriptionStatus>(new MedicationPrescriptionStatusEnumFactory());
        this.status.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #patient} (A link to a resource representing the person to whom the medication will be given.)
     */
    public Reference getPatient() { 
      if (this.patient == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationPrescription.patient");
        else if (Configuration.doAutoCreate())
          this.patient = new Reference(); // cc
      return this.patient;
    }

    public boolean hasPatient() { 
      return this.patient != null && !this.patient.isEmpty();
    }

    /**
     * @param value {@link #patient} (A link to a resource representing the person to whom the medication will be given.)
     */
    public MedicationPrescription setPatient(Reference value) { 
      this.patient = value;
      return this;
    }

    /**
     * @return {@link #patient} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (A link to a resource representing the person to whom the medication will be given.)
     */
    public Patient getPatientTarget() { 
      if (this.patientTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationPrescription.patient");
        else if (Configuration.doAutoCreate())
          this.patientTarget = new Patient(); // aa
      return this.patientTarget;
    }

    /**
     * @param value {@link #patient} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (A link to a resource representing the person to whom the medication will be given.)
     */
    public MedicationPrescription setPatientTarget(Patient value) { 
      this.patientTarget = value;
      return this;
    }

    /**
     * @return {@link #prescriber} (The healthcare professional responsible for authorizing the prescription.)
     */
    public Reference getPrescriber() { 
      if (this.prescriber == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationPrescription.prescriber");
        else if (Configuration.doAutoCreate())
          this.prescriber = new Reference(); // cc
      return this.prescriber;
    }

    public boolean hasPrescriber() { 
      return this.prescriber != null && !this.prescriber.isEmpty();
    }

    /**
     * @param value {@link #prescriber} (The healthcare professional responsible for authorizing the prescription.)
     */
    public MedicationPrescription setPrescriber(Reference value) { 
      this.prescriber = value;
      return this;
    }

    /**
     * @return {@link #prescriber} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The healthcare professional responsible for authorizing the prescription.)
     */
    public Practitioner getPrescriberTarget() { 
      if (this.prescriberTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationPrescription.prescriber");
        else if (Configuration.doAutoCreate())
          this.prescriberTarget = new Practitioner(); // aa
      return this.prescriberTarget;
    }

    /**
     * @param value {@link #prescriber} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The healthcare professional responsible for authorizing the prescription.)
     */
    public MedicationPrescription setPrescriberTarget(Practitioner value) { 
      this.prescriberTarget = value;
      return this;
    }

    /**
     * @return {@link #encounter} (A link to a resource that identifies the particular occurrence of contact between patient and health care provider.)
     */
    public Reference getEncounter() { 
      if (this.encounter == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationPrescription.encounter");
        else if (Configuration.doAutoCreate())
          this.encounter = new Reference(); // cc
      return this.encounter;
    }

    public boolean hasEncounter() { 
      return this.encounter != null && !this.encounter.isEmpty();
    }

    /**
     * @param value {@link #encounter} (A link to a resource that identifies the particular occurrence of contact between patient and health care provider.)
     */
    public MedicationPrescription setEncounter(Reference value) { 
      this.encounter = value;
      return this;
    }

    /**
     * @return {@link #encounter} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (A link to a resource that identifies the particular occurrence of contact between patient and health care provider.)
     */
    public Encounter getEncounterTarget() { 
      if (this.encounterTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationPrescription.encounter");
        else if (Configuration.doAutoCreate())
          this.encounterTarget = new Encounter(); // aa
      return this.encounterTarget;
    }

    /**
     * @param value {@link #encounter} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (A link to a resource that identifies the particular occurrence of contact between patient and health care provider.)
     */
    public MedicationPrescription setEncounterTarget(Encounter value) { 
      this.encounterTarget = value;
      return this;
    }

    /**
     * @return {@link #reason} (Can be the reason or the indication for writing the prescription.)
     */
    public Type getReason() { 
      return this.reason;
    }

    /**
     * @return {@link #reason} (Can be the reason or the indication for writing the prescription.)
     */
    public CodeableConcept getReasonCodeableConcept() throws Exception { 
      if (!(this.reason instanceof CodeableConcept))
        throw new Exception("Type mismatch: the type CodeableConcept was expected, but "+this.reason.getClass().getName()+" was encountered");
      return (CodeableConcept) this.reason;
    }

    public boolean hasReasonCodeableConcept() throws Exception { 
      return this.reason instanceof CodeableConcept;
    }

    /**
     * @return {@link #reason} (Can be the reason or the indication for writing the prescription.)
     */
    public Reference getReasonReference() throws Exception { 
      if (!(this.reason instanceof Reference))
        throw new Exception("Type mismatch: the type Reference was expected, but "+this.reason.getClass().getName()+" was encountered");
      return (Reference) this.reason;
    }

    public boolean hasReasonReference() throws Exception { 
      return this.reason instanceof Reference;
    }

    public boolean hasReason() { 
      return this.reason != null && !this.reason.isEmpty();
    }

    /**
     * @param value {@link #reason} (Can be the reason or the indication for writing the prescription.)
     */
    public MedicationPrescription setReason(Type value) { 
      this.reason = value;
      return this;
    }

    /**
     * @return {@link #note} (Extra information about the prescription that could not be conveyed by the other attributes.). This is the underlying object with id, value and extensions. The accessor "getNote" gives direct access to the value
     */
    public StringType getNoteElement() { 
      if (this.note == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationPrescription.note");
        else if (Configuration.doAutoCreate())
          this.note = new StringType(); // bb
      return this.note;
    }

    public boolean hasNoteElement() { 
      return this.note != null && !this.note.isEmpty();
    }

    public boolean hasNote() { 
      return this.note != null && !this.note.isEmpty();
    }

    /**
     * @param value {@link #note} (Extra information about the prescription that could not be conveyed by the other attributes.). This is the underlying object with id, value and extensions. The accessor "getNote" gives direct access to the value
     */
    public MedicationPrescription setNoteElement(StringType value) { 
      this.note = value;
      return this;
    }

    /**
     * @return Extra information about the prescription that could not be conveyed by the other attributes.
     */
    public String getNote() { 
      return this.note == null ? null : this.note.getValue();
    }

    /**
     * @param value Extra information about the prescription that could not be conveyed by the other attributes.
     */
    public MedicationPrescription setNote(String value) { 
      if (Utilities.noString(value))
        this.note = null;
      else {
        if (this.note == null)
          this.note = new StringType();
        this.note.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #medication} (Identifies the medication being administered. This is a link to a resource that represents the medication which may be the details of the medication or simply an attribute carrying a code that identifies the medication from a known list of medications.)
     */
    public Type getMedication() { 
      return this.medication;
    }

    /**
     * @return {@link #medication} (Identifies the medication being administered. This is a link to a resource that represents the medication which may be the details of the medication or simply an attribute carrying a code that identifies the medication from a known list of medications.)
     */
    public CodeableConcept getMedicationCodeableConcept() throws Exception { 
      if (!(this.medication instanceof CodeableConcept))
        throw new Exception("Type mismatch: the type CodeableConcept was expected, but "+this.medication.getClass().getName()+" was encountered");
      return (CodeableConcept) this.medication;
    }

    public boolean hasMedicationCodeableConcept() throws Exception { 
      return this.medication instanceof CodeableConcept;
    }

    /**
     * @return {@link #medication} (Identifies the medication being administered. This is a link to a resource that represents the medication which may be the details of the medication or simply an attribute carrying a code that identifies the medication from a known list of medications.)
     */
    public Reference getMedicationReference() throws Exception { 
      if (!(this.medication instanceof Reference))
        throw new Exception("Type mismatch: the type Reference was expected, but "+this.medication.getClass().getName()+" was encountered");
      return (Reference) this.medication;
    }

    public boolean hasMedicationReference() throws Exception { 
      return this.medication instanceof Reference;
    }

    public boolean hasMedication() { 
      return this.medication != null && !this.medication.isEmpty();
    }

    /**
     * @param value {@link #medication} (Identifies the medication being administered. This is a link to a resource that represents the medication which may be the details of the medication or simply an attribute carrying a code that identifies the medication from a known list of medications.)
     */
    public MedicationPrescription setMedication(Type value) { 
      this.medication = value;
      return this;
    }

    /**
     * @return {@link #dosageInstruction} (Indicates how the medication is to be used by the patient.)
     */
    public List<MedicationPrescriptionDosageInstructionComponent> getDosageInstruction() { 
      if (this.dosageInstruction == null)
        this.dosageInstruction = new ArrayList<MedicationPrescriptionDosageInstructionComponent>();
      return this.dosageInstruction;
    }

    public boolean hasDosageInstruction() { 
      if (this.dosageInstruction == null)
        return false;
      for (MedicationPrescriptionDosageInstructionComponent item : this.dosageInstruction)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #dosageInstruction} (Indicates how the medication is to be used by the patient.)
     */
    // syntactic sugar
    public MedicationPrescriptionDosageInstructionComponent addDosageInstruction() { //3
      MedicationPrescriptionDosageInstructionComponent t = new MedicationPrescriptionDosageInstructionComponent();
      if (this.dosageInstruction == null)
        this.dosageInstruction = new ArrayList<MedicationPrescriptionDosageInstructionComponent>();
      this.dosageInstruction.add(t);
      return t;
    }

    // syntactic sugar
    public MedicationPrescription addDosageInstruction(MedicationPrescriptionDosageInstructionComponent t) { //3
      if (t == null)
        return this;
      if (this.dosageInstruction == null)
        this.dosageInstruction = new ArrayList<MedicationPrescriptionDosageInstructionComponent>();
      this.dosageInstruction.add(t);
      return this;
    }

    /**
     * @return {@link #dispense} (Deals with details of the dispense part of the order.)
     */
    public MedicationPrescriptionDispenseComponent getDispense() { 
      if (this.dispense == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationPrescription.dispense");
        else if (Configuration.doAutoCreate())
          this.dispense = new MedicationPrescriptionDispenseComponent(); // cc
      return this.dispense;
    }

    public boolean hasDispense() { 
      return this.dispense != null && !this.dispense.isEmpty();
    }

    /**
     * @param value {@link #dispense} (Deals with details of the dispense part of the order.)
     */
    public MedicationPrescription setDispense(MedicationPrescriptionDispenseComponent value) { 
      this.dispense = value;
      return this;
    }

    /**
     * @return {@link #substitution} (Indicates whether or not substitution can or should be part of the dispense. In some cases substitution must happen, in other cases substitution must not happen, and in others it does not matter. This block explains the prescriber's intent. If nothing is specified substitution may be done.)
     */
    public MedicationPrescriptionSubstitutionComponent getSubstitution() { 
      if (this.substitution == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationPrescription.substitution");
        else if (Configuration.doAutoCreate())
          this.substitution = new MedicationPrescriptionSubstitutionComponent(); // cc
      return this.substitution;
    }

    public boolean hasSubstitution() { 
      return this.substitution != null && !this.substitution.isEmpty();
    }

    /**
     * @param value {@link #substitution} (Indicates whether or not substitution can or should be part of the dispense. In some cases substitution must happen, in other cases substitution must not happen, and in others it does not matter. This block explains the prescriber's intent. If nothing is specified substitution may be done.)
     */
    public MedicationPrescription setSubstitution(MedicationPrescriptionSubstitutionComponent value) { 
      this.substitution = value;
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "External identifier - one that would be used by another non-FHIR system - for example a re-imbursement system might issue its own id for each prescription that is created.  This is particularly important where FHIR only provides part of an erntire workflow process where records have to be tracked through an entire system.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("dateWritten", "dateTime", "The date (and perhaps time) when the prescription was written.", 0, java.lang.Integer.MAX_VALUE, dateWritten));
        childrenList.add(new Property("status", "code", "A code specifying the state of the order.  Generally this will be active or completed state.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("patient", "Reference(Patient)", "A link to a resource representing the person to whom the medication will be given.", 0, java.lang.Integer.MAX_VALUE, patient));
        childrenList.add(new Property("prescriber", "Reference(Practitioner)", "The healthcare professional responsible for authorizing the prescription.", 0, java.lang.Integer.MAX_VALUE, prescriber));
        childrenList.add(new Property("encounter", "Reference(Encounter)", "A link to a resource that identifies the particular occurrence of contact between patient and health care provider.", 0, java.lang.Integer.MAX_VALUE, encounter));
        childrenList.add(new Property("reason[x]", "CodeableConcept|Reference(Condition)", "Can be the reason or the indication for writing the prescription.", 0, java.lang.Integer.MAX_VALUE, reason));
        childrenList.add(new Property("note", "string", "Extra information about the prescription that could not be conveyed by the other attributes.", 0, java.lang.Integer.MAX_VALUE, note));
        childrenList.add(new Property("medication[x]", "CodeableConcept|Reference(Medication)", "Identifies the medication being administered. This is a link to a resource that represents the medication which may be the details of the medication or simply an attribute carrying a code that identifies the medication from a known list of medications.", 0, java.lang.Integer.MAX_VALUE, medication));
        childrenList.add(new Property("dosageInstruction", "", "Indicates how the medication is to be used by the patient.", 0, java.lang.Integer.MAX_VALUE, dosageInstruction));
        childrenList.add(new Property("dispense", "", "Deals with details of the dispense part of the order.", 0, java.lang.Integer.MAX_VALUE, dispense));
        childrenList.add(new Property("substitution", "", "Indicates whether or not substitution can or should be part of the dispense. In some cases substitution must happen, in other cases substitution must not happen, and in others it does not matter. This block explains the prescriber's intent. If nothing is specified substitution may be done.", 0, java.lang.Integer.MAX_VALUE, substitution));
      }

      public MedicationPrescription copy() {
        MedicationPrescription dst = new MedicationPrescription();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.dateWritten = dateWritten == null ? null : dateWritten.copy();
        dst.status = status == null ? null : status.copy();
        dst.patient = patient == null ? null : patient.copy();
        dst.prescriber = prescriber == null ? null : prescriber.copy();
        dst.encounter = encounter == null ? null : encounter.copy();
        dst.reason = reason == null ? null : reason.copy();
        dst.note = note == null ? null : note.copy();
        dst.medication = medication == null ? null : medication.copy();
        if (dosageInstruction != null) {
          dst.dosageInstruction = new ArrayList<MedicationPrescriptionDosageInstructionComponent>();
          for (MedicationPrescriptionDosageInstructionComponent i : dosageInstruction)
            dst.dosageInstruction.add(i.copy());
        };
        dst.dispense = dispense == null ? null : dispense.copy();
        dst.substitution = substitution == null ? null : substitution.copy();
        return dst;
      }

      protected MedicationPrescription typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof MedicationPrescription))
          return false;
        MedicationPrescription o = (MedicationPrescription) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(dateWritten, o.dateWritten, true)
           && compareDeep(status, o.status, true) && compareDeep(patient, o.patient, true) && compareDeep(prescriber, o.prescriber, true)
           && compareDeep(encounter, o.encounter, true) && compareDeep(reason, o.reason, true) && compareDeep(note, o.note, true)
           && compareDeep(medication, o.medication, true) && compareDeep(dosageInstruction, o.dosageInstruction, true)
           && compareDeep(dispense, o.dispense, true) && compareDeep(substitution, o.substitution, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof MedicationPrescription))
          return false;
        MedicationPrescription o = (MedicationPrescription) other;
        return compareValues(dateWritten, o.dateWritten, true) && compareValues(status, o.status, true) && compareValues(note, o.note, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (dateWritten == null || dateWritten.isEmpty())
           && (status == null || status.isEmpty()) && (patient == null || patient.isEmpty()) && (prescriber == null || prescriber.isEmpty())
           && (encounter == null || encounter.isEmpty()) && (reason == null || reason.isEmpty()) && (note == null || note.isEmpty())
           && (medication == null || medication.isEmpty()) && (dosageInstruction == null || dosageInstruction.isEmpty())
           && (dispense == null || dispense.isEmpty()) && (substitution == null || substitution.isEmpty())
          ;
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.MedicationPrescription;
   }

  @SearchParamDefinition(name="prescriber", path="MedicationPrescription.prescriber", description="Who ordered the medication(s)", type="reference" )
  public static final String SP_PRESCRIBER = "prescriber";
  @SearchParamDefinition(name="identifier", path="MedicationPrescription.identifier", description="Return prescriptions with this external identity", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
  @SearchParamDefinition(name="patient", path="MedicationPrescription.patient", description="The identity of a patient to list dispenses  for", type="reference" )
  public static final String SP_PATIENT = "patient";
  @SearchParamDefinition(name="datewritten", path="MedicationPrescription.dateWritten", description="Return prescriptions written on this date", type="date" )
  public static final String SP_DATEWRITTEN = "datewritten";
  @SearchParamDefinition(name="medication", path="MedicationPrescription.medicationReference", description="Code for medicine or text in medicine name", type="reference" )
  public static final String SP_MEDICATION = "medication";
  @SearchParamDefinition(name="encounter", path="MedicationPrescription.encounter", description="Return prescriptions with this encounter identity", type="reference" )
  public static final String SP_ENCOUNTER = "encounter";
  @SearchParamDefinition(name="status", path="MedicationPrescription.status", description="Status of the prescription", type="token" )
  public static final String SP_STATUS = "status";

}

