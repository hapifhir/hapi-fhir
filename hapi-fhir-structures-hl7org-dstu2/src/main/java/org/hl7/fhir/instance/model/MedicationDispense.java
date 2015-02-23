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

// Generated on Wed, Feb 18, 2015 12:09-0500 for FHIR v0.4.0

import java.util.*;

import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.instance.model.annotations.ResourceDef;
import org.hl7.fhir.instance.model.annotations.SearchParamDefinition;
import org.hl7.fhir.instance.model.annotations.Block;
import org.hl7.fhir.instance.model.annotations.Child;
import org.hl7.fhir.instance.model.annotations.Description;
/**
 * Dispensing a medication to a named patient.  This includes a description of the supply provided and the instructions for administering the medication.
 */
@ResourceDef(name="MedicationDispense", profile="http://hl7.org/fhir/Profile/MedicationDispense")
public class MedicationDispense extends DomainResource {

    public enum MedicationDispenseStatus {
        /**
         * The dispense has started but has not yet completed.
         */
        INPROGRESS, 
        /**
         * Actions implied by the administration have been temporarily halted, but are expected to continue later. May also be called "suspended".
         */
        ONHOLD, 
        /**
         * All actions that are implied by the dispense have occurred.
         */
        COMPLETED, 
        /**
         * The dispense was entered in error and therefore nullified.
         */
        ENTEREDINERROR, 
        /**
         * Actions implied by the dispense have been permanently halted, before all of them occurred.
         */
        STOPPED, 
        /**
         * added to help the parsers
         */
        NULL;
        public static MedicationDispenseStatus fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("in-progress".equals(codeString))
          return INPROGRESS;
        if ("on-hold".equals(codeString))
          return ONHOLD;
        if ("completed".equals(codeString))
          return COMPLETED;
        if ("entered-in-error".equals(codeString))
          return ENTEREDINERROR;
        if ("stopped".equals(codeString))
          return STOPPED;
        throw new Exception("Unknown MedicationDispenseStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case INPROGRESS: return "in-progress";
            case ONHOLD: return "on-hold";
            case COMPLETED: return "completed";
            case ENTEREDINERROR: return "entered-in-error";
            case STOPPED: return "stopped";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case INPROGRESS: return "";
            case ONHOLD: return "";
            case COMPLETED: return "";
            case ENTEREDINERROR: return "";
            case STOPPED: return "";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case INPROGRESS: return "The dispense has started but has not yet completed.";
            case ONHOLD: return "Actions implied by the administration have been temporarily halted, but are expected to continue later. May also be called 'suspended'.";
            case COMPLETED: return "All actions that are implied by the dispense have occurred.";
            case ENTEREDINERROR: return "The dispense was entered in error and therefore nullified.";
            case STOPPED: return "Actions implied by the dispense have been permanently halted, before all of them occurred.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case INPROGRESS: return "in-progress";
            case ONHOLD: return "on-hold";
            case COMPLETED: return "completed";
            case ENTEREDINERROR: return "entered-in-error";
            case STOPPED: return "stopped";
            default: return "?";
          }
        }
    }

  public static class MedicationDispenseStatusEnumFactory implements EnumFactory<MedicationDispenseStatus> {
    public MedicationDispenseStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("in-progress".equals(codeString))
          return MedicationDispenseStatus.INPROGRESS;
        if ("on-hold".equals(codeString))
          return MedicationDispenseStatus.ONHOLD;
        if ("completed".equals(codeString))
          return MedicationDispenseStatus.COMPLETED;
        if ("entered-in-error".equals(codeString))
          return MedicationDispenseStatus.ENTEREDINERROR;
        if ("stopped".equals(codeString))
          return MedicationDispenseStatus.STOPPED;
        throw new IllegalArgumentException("Unknown MedicationDispenseStatus code '"+codeString+"'");
        }
    public String toCode(MedicationDispenseStatus code) {
      if (code == MedicationDispenseStatus.INPROGRESS)
        return "in-progress";
      if (code == MedicationDispenseStatus.ONHOLD)
        return "on-hold";
      if (code == MedicationDispenseStatus.COMPLETED)
        return "completed";
      if (code == MedicationDispenseStatus.ENTEREDINERROR)
        return "entered-in-error";
      if (code == MedicationDispenseStatus.STOPPED)
        return "stopped";
      return "?";
      }
    }

    @Block()
    public static class MedicationDispenseDosageInstructionComponent extends BackboneElement {
        /**
         * Additional instructions such as "Swallow with plenty of water" which may or may not be coded.
         */
        @Child(name="additionalInstructions", type={CodeableConcept.class}, order=1, min=0, max=1)
        @Description(shortDefinition="E.g. 'Take with food'", formalDefinition="Additional instructions such as 'Swallow with plenty of water' which may or may not be coded." )
        protected CodeableConcept additionalInstructions;

        /**
         * The timing schedule for giving the medication to the patient.  The Schedule data type allows many different expressions, for example.  "Every  8 hours"; "Three times a day"; "1/2 an hour before breakfast for 10 days from 23-Dec 2011:";  "15 Oct 2013, 17 Oct 2013 and 1 Nov 2013".
         */
        @Child(name="schedule", type={DateTimeType.class, Period.class, Timing.class}, order=2, min=0, max=1)
        @Description(shortDefinition="When medication should be administered", formalDefinition="The timing schedule for giving the medication to the patient.  The Schedule data type allows many different expressions, for example.  'Every  8 hours'; 'Three times a day'; '1/2 an hour before breakfast for 10 days from 23-Dec 2011:';  '15 Oct 2013, 17 Oct 2013 and 1 Nov 2013'." )
        protected Type schedule;

        /**
         * If set to true or if specified as a CodeableConcept, indicates that the medication is only taken when needed within the specified schedule rather than at every scheduled dose.  If a CodeableConcept is present, it indicates the pre-condition for taking the Medication.
         */
        @Child(name="asNeeded", type={BooleanType.class, CodeableConcept.class}, order=3, min=0, max=1)
        @Description(shortDefinition="Take 'as needed' f(or x)", formalDefinition="If set to true or if specified as a CodeableConcept, indicates that the medication is only taken when needed within the specified schedule rather than at every scheduled dose.  If a CodeableConcept is present, it indicates the pre-condition for taking the Medication." )
        protected Type asNeeded;

        /**
         * A coded specification of the anatomic site where the medication first enters the body.
         */
        @Child(name="site", type={CodeableConcept.class}, order=4, min=0, max=1)
        @Description(shortDefinition="Body site to administer to", formalDefinition="A coded specification of the anatomic site where the medication first enters the body." )
        protected CodeableConcept site;

        /**
         * A code specifying the route or physiological path of administration of a therapeutic agent into or onto a subject.
         */
        @Child(name="route", type={CodeableConcept.class}, order=5, min=0, max=1)
        @Description(shortDefinition="How drug should enter body", formalDefinition="A code specifying the route or physiological path of administration of a therapeutic agent into or onto a subject." )
        protected CodeableConcept route;

        /**
         * A coded value indicating the method by which the medication is introduced into or onto the body. Most commonly used for injections.  Examples:  Slow Push; Deep IV.

Terminologies used often pre-coordinate this term with the route and or form of administration.
         */
        @Child(name="method", type={CodeableConcept.class}, order=6, min=0, max=1)
        @Description(shortDefinition="Technique for administering medication", formalDefinition="A coded value indicating the method by which the medication is introduced into or onto the body. Most commonly used for injections.  Examples:  Slow Push; Deep IV.\r\rTerminologies used often pre-coordinate this term with the route and or form of administration." )
        protected CodeableConcept method;

        /**
         * The amount of therapeutic or other substance given at one administration event.
         */
        @Child(name="dose", type={Range.class, Quantity.class}, order=7, min=0, max=1)
        @Description(shortDefinition="Amount of medication per dose", formalDefinition="The amount of therapeutic or other substance given at one administration event." )
        protected Type dose;

        /**
         * Identifies the speed with which the substance is introduced into the subject. Typically the rate for an infusion. 200ml in 2 hours.
         */
        @Child(name="rate", type={Ratio.class}, order=8, min=0, max=1)
        @Description(shortDefinition="Amount of medication per unit of time", formalDefinition="Identifies the speed with which the substance is introduced into the subject. Typically the rate for an infusion. 200ml in 2 hours." )
        protected Ratio rate;

        /**
         * The maximum total quantity of a therapeutic substance that may be administered to a subject over the period of time,  e.g. 1000mg in 24 hours.
         */
        @Child(name="maxDosePerPeriod", type={Ratio.class}, order=9, min=0, max=1)
        @Description(shortDefinition="Upper limit on medication per unit of time", formalDefinition="The maximum total quantity of a therapeutic substance that may be administered to a subject over the period of time,  e.g. 1000mg in 24 hours." )
        protected Ratio maxDosePerPeriod;

        private static final long serialVersionUID = -1523433515L;

      public MedicationDispenseDosageInstructionComponent() {
        super();
      }

        /**
         * @return {@link #additionalInstructions} (Additional instructions such as "Swallow with plenty of water" which may or may not be coded.)
         */
        public CodeableConcept getAdditionalInstructions() { 
          if (this.additionalInstructions == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationDispenseDosageInstructionComponent.additionalInstructions");
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
        public MedicationDispenseDosageInstructionComponent setAdditionalInstructions(CodeableConcept value) { 
          this.additionalInstructions = value;
          return this;
        }

        /**
         * @return {@link #schedule} (The timing schedule for giving the medication to the patient.  The Schedule data type allows many different expressions, for example.  "Every  8 hours"; "Three times a day"; "1/2 an hour before breakfast for 10 days from 23-Dec 2011:";  "15 Oct 2013, 17 Oct 2013 and 1 Nov 2013".)
         */
        public Type getSchedule() { 
          return this.schedule;
        }

        /**
         * @return {@link #schedule} (The timing schedule for giving the medication to the patient.  The Schedule data type allows many different expressions, for example.  "Every  8 hours"; "Three times a day"; "1/2 an hour before breakfast for 10 days from 23-Dec 2011:";  "15 Oct 2013, 17 Oct 2013 and 1 Nov 2013".)
         */
        public DateTimeType getScheduleDateTimeType() throws Exception { 
          if (!(this.schedule instanceof DateTimeType))
            throw new Exception("Type mismatch: the type DateTimeType was expected, but "+this.schedule.getClass().getName()+" was encountered");
          return (DateTimeType) this.schedule;
        }

        /**
         * @return {@link #schedule} (The timing schedule for giving the medication to the patient.  The Schedule data type allows many different expressions, for example.  "Every  8 hours"; "Three times a day"; "1/2 an hour before breakfast for 10 days from 23-Dec 2011:";  "15 Oct 2013, 17 Oct 2013 and 1 Nov 2013".)
         */
        public Period getSchedulePeriod() throws Exception { 
          if (!(this.schedule instanceof Period))
            throw new Exception("Type mismatch: the type Period was expected, but "+this.schedule.getClass().getName()+" was encountered");
          return (Period) this.schedule;
        }

        /**
         * @return {@link #schedule} (The timing schedule for giving the medication to the patient.  The Schedule data type allows many different expressions, for example.  "Every  8 hours"; "Three times a day"; "1/2 an hour before breakfast for 10 days from 23-Dec 2011:";  "15 Oct 2013, 17 Oct 2013 and 1 Nov 2013".)
         */
        public Timing getScheduleTiming() throws Exception { 
          if (!(this.schedule instanceof Timing))
            throw new Exception("Type mismatch: the type Timing was expected, but "+this.schedule.getClass().getName()+" was encountered");
          return (Timing) this.schedule;
        }

        public boolean hasSchedule() { 
          return this.schedule != null && !this.schedule.isEmpty();
        }

        /**
         * @param value {@link #schedule} (The timing schedule for giving the medication to the patient.  The Schedule data type allows many different expressions, for example.  "Every  8 hours"; "Three times a day"; "1/2 an hour before breakfast for 10 days from 23-Dec 2011:";  "15 Oct 2013, 17 Oct 2013 and 1 Nov 2013".)
         */
        public MedicationDispenseDosageInstructionComponent setSchedule(Type value) { 
          this.schedule = value;
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

        /**
         * @return {@link #asNeeded} (If set to true or if specified as a CodeableConcept, indicates that the medication is only taken when needed within the specified schedule rather than at every scheduled dose.  If a CodeableConcept is present, it indicates the pre-condition for taking the Medication.)
         */
        public CodeableConcept getAsNeededCodeableConcept() throws Exception { 
          if (!(this.asNeeded instanceof CodeableConcept))
            throw new Exception("Type mismatch: the type CodeableConcept was expected, but "+this.asNeeded.getClass().getName()+" was encountered");
          return (CodeableConcept) this.asNeeded;
        }

        public boolean hasAsNeeded() { 
          return this.asNeeded != null && !this.asNeeded.isEmpty();
        }

        /**
         * @param value {@link #asNeeded} (If set to true or if specified as a CodeableConcept, indicates that the medication is only taken when needed within the specified schedule rather than at every scheduled dose.  If a CodeableConcept is present, it indicates the pre-condition for taking the Medication.)
         */
        public MedicationDispenseDosageInstructionComponent setAsNeeded(Type value) { 
          this.asNeeded = value;
          return this;
        }

        /**
         * @return {@link #site} (A coded specification of the anatomic site where the medication first enters the body.)
         */
        public CodeableConcept getSite() { 
          if (this.site == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationDispenseDosageInstructionComponent.site");
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
        public MedicationDispenseDosageInstructionComponent setSite(CodeableConcept value) { 
          this.site = value;
          return this;
        }

        /**
         * @return {@link #route} (A code specifying the route or physiological path of administration of a therapeutic agent into or onto a subject.)
         */
        public CodeableConcept getRoute() { 
          if (this.route == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationDispenseDosageInstructionComponent.route");
            else if (Configuration.doAutoCreate())
              this.route = new CodeableConcept(); // cc
          return this.route;
        }

        public boolean hasRoute() { 
          return this.route != null && !this.route.isEmpty();
        }

        /**
         * @param value {@link #route} (A code specifying the route or physiological path of administration of a therapeutic agent into or onto a subject.)
         */
        public MedicationDispenseDosageInstructionComponent setRoute(CodeableConcept value) { 
          this.route = value;
          return this;
        }

        /**
         * @return {@link #method} (A coded value indicating the method by which the medication is introduced into or onto the body. Most commonly used for injections.  Examples:  Slow Push; Deep IV.

Terminologies used often pre-coordinate this term with the route and or form of administration.)
         */
        public CodeableConcept getMethod() { 
          if (this.method == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationDispenseDosageInstructionComponent.method");
            else if (Configuration.doAutoCreate())
              this.method = new CodeableConcept(); // cc
          return this.method;
        }

        public boolean hasMethod() { 
          return this.method != null && !this.method.isEmpty();
        }

        /**
         * @param value {@link #method} (A coded value indicating the method by which the medication is introduced into or onto the body. Most commonly used for injections.  Examples:  Slow Push; Deep IV.

Terminologies used often pre-coordinate this term with the route and or form of administration.)
         */
        public MedicationDispenseDosageInstructionComponent setMethod(CodeableConcept value) { 
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

        /**
         * @return {@link #dose} (The amount of therapeutic or other substance given at one administration event.)
         */
        public Quantity getDoseQuantity() throws Exception { 
          if (!(this.dose instanceof Quantity))
            throw new Exception("Type mismatch: the type Quantity was expected, but "+this.dose.getClass().getName()+" was encountered");
          return (Quantity) this.dose;
        }

        public boolean hasDose() { 
          return this.dose != null && !this.dose.isEmpty();
        }

        /**
         * @param value {@link #dose} (The amount of therapeutic or other substance given at one administration event.)
         */
        public MedicationDispenseDosageInstructionComponent setDose(Type value) { 
          this.dose = value;
          return this;
        }

        /**
         * @return {@link #rate} (Identifies the speed with which the substance is introduced into the subject. Typically the rate for an infusion. 200ml in 2 hours.)
         */
        public Ratio getRate() { 
          if (this.rate == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationDispenseDosageInstructionComponent.rate");
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
        public MedicationDispenseDosageInstructionComponent setRate(Ratio value) { 
          this.rate = value;
          return this;
        }

        /**
         * @return {@link #maxDosePerPeriod} (The maximum total quantity of a therapeutic substance that may be administered to a subject over the period of time,  e.g. 1000mg in 24 hours.)
         */
        public Ratio getMaxDosePerPeriod() { 
          if (this.maxDosePerPeriod == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationDispenseDosageInstructionComponent.maxDosePerPeriod");
            else if (Configuration.doAutoCreate())
              this.maxDosePerPeriod = new Ratio(); // cc
          return this.maxDosePerPeriod;
        }

        public boolean hasMaxDosePerPeriod() { 
          return this.maxDosePerPeriod != null && !this.maxDosePerPeriod.isEmpty();
        }

        /**
         * @param value {@link #maxDosePerPeriod} (The maximum total quantity of a therapeutic substance that may be administered to a subject over the period of time,  e.g. 1000mg in 24 hours.)
         */
        public MedicationDispenseDosageInstructionComponent setMaxDosePerPeriod(Ratio value) { 
          this.maxDosePerPeriod = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("additionalInstructions", "CodeableConcept", "Additional instructions such as 'Swallow with plenty of water' which may or may not be coded.", 0, java.lang.Integer.MAX_VALUE, additionalInstructions));
          childrenList.add(new Property("schedule[x]", "dateTime|Period|Timing", "The timing schedule for giving the medication to the patient.  The Schedule data type allows many different expressions, for example.  'Every  8 hours'; 'Three times a day'; '1/2 an hour before breakfast for 10 days from 23-Dec 2011:';  '15 Oct 2013, 17 Oct 2013 and 1 Nov 2013'.", 0, java.lang.Integer.MAX_VALUE, schedule));
          childrenList.add(new Property("asNeeded[x]", "boolean|CodeableConcept", "If set to true or if specified as a CodeableConcept, indicates that the medication is only taken when needed within the specified schedule rather than at every scheduled dose.  If a CodeableConcept is present, it indicates the pre-condition for taking the Medication.", 0, java.lang.Integer.MAX_VALUE, asNeeded));
          childrenList.add(new Property("site", "CodeableConcept", "A coded specification of the anatomic site where the medication first enters the body.", 0, java.lang.Integer.MAX_VALUE, site));
          childrenList.add(new Property("route", "CodeableConcept", "A code specifying the route or physiological path of administration of a therapeutic agent into or onto a subject.", 0, java.lang.Integer.MAX_VALUE, route));
          childrenList.add(new Property("method", "CodeableConcept", "A coded value indicating the method by which the medication is introduced into or onto the body. Most commonly used for injections.  Examples:  Slow Push; Deep IV.\r\rTerminologies used often pre-coordinate this term with the route and or form of administration.", 0, java.lang.Integer.MAX_VALUE, method));
          childrenList.add(new Property("dose[x]", "Range|Quantity", "The amount of therapeutic or other substance given at one administration event.", 0, java.lang.Integer.MAX_VALUE, dose));
          childrenList.add(new Property("rate", "Ratio", "Identifies the speed with which the substance is introduced into the subject. Typically the rate for an infusion. 200ml in 2 hours.", 0, java.lang.Integer.MAX_VALUE, rate));
          childrenList.add(new Property("maxDosePerPeriod", "Ratio", "The maximum total quantity of a therapeutic substance that may be administered to a subject over the period of time,  e.g. 1000mg in 24 hours.", 0, java.lang.Integer.MAX_VALUE, maxDosePerPeriod));
        }

      public MedicationDispenseDosageInstructionComponent copy() {
        MedicationDispenseDosageInstructionComponent dst = new MedicationDispenseDosageInstructionComponent();
        copyValues(dst);
        dst.additionalInstructions = additionalInstructions == null ? null : additionalInstructions.copy();
        dst.schedule = schedule == null ? null : schedule.copy();
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
        if (!(other instanceof MedicationDispenseDosageInstructionComponent))
          return false;
        MedicationDispenseDosageInstructionComponent o = (MedicationDispenseDosageInstructionComponent) other;
        return compareDeep(additionalInstructions, o.additionalInstructions, true) && compareDeep(schedule, o.schedule, true)
           && compareDeep(asNeeded, o.asNeeded, true) && compareDeep(site, o.site, true) && compareDeep(route, o.route, true)
           && compareDeep(method, o.method, true) && compareDeep(dose, o.dose, true) && compareDeep(rate, o.rate, true)
           && compareDeep(maxDosePerPeriod, o.maxDosePerPeriod, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof MedicationDispenseDosageInstructionComponent))
          return false;
        MedicationDispenseDosageInstructionComponent o = (MedicationDispenseDosageInstructionComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (additionalInstructions == null || additionalInstructions.isEmpty())
           && (schedule == null || schedule.isEmpty()) && (asNeeded == null || asNeeded.isEmpty()) && (site == null || site.isEmpty())
           && (route == null || route.isEmpty()) && (method == null || method.isEmpty()) && (dose == null || dose.isEmpty())
           && (rate == null || rate.isEmpty()) && (maxDosePerPeriod == null || maxDosePerPeriod.isEmpty())
          ;
      }

  }

    @Block()
    public static class MedicationDispenseSubstitutionComponent extends BackboneElement {
        /**
         * A code signifying whether a different drug was dispensed from what was prescribed.
         */
        @Child(name="type", type={CodeableConcept.class}, order=1, min=1, max=1)
        @Description(shortDefinition="Type of substitiution", formalDefinition="A code signifying whether a different drug was dispensed from what was prescribed." )
        protected CodeableConcept type;

        /**
         * Indicates the reason for the substitution of (or lack of substitution) from what was prescribed.
         */
        @Child(name="reason", type={CodeableConcept.class}, order=2, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="Why was substitution made", formalDefinition="Indicates the reason for the substitution of (or lack of substitution) from what was prescribed." )
        protected List<CodeableConcept> reason;

        /**
         * The person or organization that has primary responsibility for the substitution.
         */
        @Child(name="responsibleParty", type={Practitioner.class}, order=3, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="Who is responsible for the substitution", formalDefinition="The person or organization that has primary responsibility for the substitution." )
        protected List<Reference> responsibleParty;
        /**
         * The actual objects that are the target of the reference (The person or organization that has primary responsibility for the substitution.)
         */
        protected List<Practitioner> responsiblePartyTarget;


        private static final long serialVersionUID = 1218245830L;

      public MedicationDispenseSubstitutionComponent() {
        super();
      }

      public MedicationDispenseSubstitutionComponent(CodeableConcept type) {
        super();
        this.type = type;
      }

        /**
         * @return {@link #type} (A code signifying whether a different drug was dispensed from what was prescribed.)
         */
        public CodeableConcept getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationDispenseSubstitutionComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new CodeableConcept(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (A code signifying whether a different drug was dispensed from what was prescribed.)
         */
        public MedicationDispenseSubstitutionComponent setType(CodeableConcept value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #reason} (Indicates the reason for the substitution of (or lack of substitution) from what was prescribed.)
         */
        public List<CodeableConcept> getReason() { 
          if (this.reason == null)
            this.reason = new ArrayList<CodeableConcept>();
          return this.reason;
        }

        public boolean hasReason() { 
          if (this.reason == null)
            return false;
          for (CodeableConcept item : this.reason)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #reason} (Indicates the reason for the substitution of (or lack of substitution) from what was prescribed.)
         */
    // syntactic sugar
        public CodeableConcept addReason() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.reason == null)
            this.reason = new ArrayList<CodeableConcept>();
          this.reason.add(t);
          return t;
        }

        /**
         * @return {@link #responsibleParty} (The person or organization that has primary responsibility for the substitution.)
         */
        public List<Reference> getResponsibleParty() { 
          if (this.responsibleParty == null)
            this.responsibleParty = new ArrayList<Reference>();
          return this.responsibleParty;
        }

        public boolean hasResponsibleParty() { 
          if (this.responsibleParty == null)
            return false;
          for (Reference item : this.responsibleParty)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #responsibleParty} (The person or organization that has primary responsibility for the substitution.)
         */
    // syntactic sugar
        public Reference addResponsibleParty() { //3
          Reference t = new Reference();
          if (this.responsibleParty == null)
            this.responsibleParty = new ArrayList<Reference>();
          this.responsibleParty.add(t);
          return t;
        }

        /**
         * @return {@link #responsibleParty} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. The person or organization that has primary responsibility for the substitution.)
         */
        public List<Practitioner> getResponsiblePartyTarget() { 
          if (this.responsiblePartyTarget == null)
            this.responsiblePartyTarget = new ArrayList<Practitioner>();
          return this.responsiblePartyTarget;
        }

    // syntactic sugar
        /**
         * @return {@link #responsibleParty} (Add an actual object that is the target of the reference. The reference library doesn't use these, but you can use this to hold the resources if you resolvethemt. The person or organization that has primary responsibility for the substitution.)
         */
        public Practitioner addResponsiblePartyTarget() { 
          Practitioner r = new Practitioner();
          if (this.responsiblePartyTarget == null)
            this.responsiblePartyTarget = new ArrayList<Practitioner>();
          this.responsiblePartyTarget.add(r);
          return r;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("type", "CodeableConcept", "A code signifying whether a different drug was dispensed from what was prescribed.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("reason", "CodeableConcept", "Indicates the reason for the substitution of (or lack of substitution) from what was prescribed.", 0, java.lang.Integer.MAX_VALUE, reason));
          childrenList.add(new Property("responsibleParty", "Reference(Practitioner)", "The person or organization that has primary responsibility for the substitution.", 0, java.lang.Integer.MAX_VALUE, responsibleParty));
        }

      public MedicationDispenseSubstitutionComponent copy() {
        MedicationDispenseSubstitutionComponent dst = new MedicationDispenseSubstitutionComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        if (reason != null) {
          dst.reason = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : reason)
            dst.reason.add(i.copy());
        };
        if (responsibleParty != null) {
          dst.responsibleParty = new ArrayList<Reference>();
          for (Reference i : responsibleParty)
            dst.responsibleParty.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof MedicationDispenseSubstitutionComponent))
          return false;
        MedicationDispenseSubstitutionComponent o = (MedicationDispenseSubstitutionComponent) other;
        return compareDeep(type, o.type, true) && compareDeep(reason, o.reason, true) && compareDeep(responsibleParty, o.responsibleParty, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof MedicationDispenseSubstitutionComponent))
          return false;
        MedicationDispenseSubstitutionComponent o = (MedicationDispenseSubstitutionComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (type == null || type.isEmpty()) && (reason == null || reason.isEmpty())
           && (responsibleParty == null || responsibleParty.isEmpty());
      }

  }

    /**
     * Identifier assigned by the dispensing facility - this is an identifier assigned outside FHIR.
     */
    @Child(name = "identifier", type = {Identifier.class}, order = 0, min = 0, max = 1)
    @Description(shortDefinition="External identifier", formalDefinition="Identifier assigned by the dispensing facility - this is an identifier assigned outside FHIR." )
    protected Identifier identifier;

    /**
     * A code specifying the state of the set of dispense events.
     */
    @Child(name = "status", type = {CodeType.class}, order = 1, min = 0, max = 1)
    @Description(shortDefinition="in-progress | on-hold | completed | entered-in-error | stopped", formalDefinition="A code specifying the state of the set of dispense events." )
    protected Enumeration<MedicationDispenseStatus> status;

    /**
     * A link to a resource representing the person to whom the medication will be given.
     */
    @Child(name = "patient", type = {Patient.class}, order = 2, min = 0, max = 1)
    @Description(shortDefinition="Who the dispense is for", formalDefinition="A link to a resource representing the person to whom the medication will be given." )
    protected Reference patient;

    /**
     * The actual object that is the target of the reference (A link to a resource representing the person to whom the medication will be given.)
     */
    protected Patient patientTarget;

    /**
     * The individual responsible for dispensing the medication.
     */
    @Child(name = "dispenser", type = {Practitioner.class}, order = 3, min = 0, max = 1)
    @Description(shortDefinition="Practitioner responsible for dispensing medication", formalDefinition="The individual responsible for dispensing the medication." )
    protected Reference dispenser;

    /**
     * The actual object that is the target of the reference (The individual responsible for dispensing the medication.)
     */
    protected Practitioner dispenserTarget;

    /**
     * Indicates the medication order that is being dispensed against.
     */
    @Child(name = "authorizingPrescription", type = {MedicationPrescription.class}, order = 4, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Medication order that authorizes the dispense", formalDefinition="Indicates the medication order that is being dispensed against." )
    protected List<Reference> authorizingPrescription;
    /**
     * The actual objects that are the target of the reference (Indicates the medication order that is being dispensed against.)
     */
    protected List<MedicationPrescription> authorizingPrescriptionTarget;


    /**
     * Indicates the type of dispensing event that is performed. Examples include: Trial Fill, Completion of Trial, Partial Fill, Emergency Fill, Samples, etc.
     */
    @Child(name = "type", type = {CodeableConcept.class}, order = 5, min = 0, max = 1)
    @Description(shortDefinition="Trial fill, partial fill, emergency fill, etc.", formalDefinition="Indicates the type of dispensing event that is performed. Examples include: Trial Fill, Completion of Trial, Partial Fill, Emergency Fill, Samples, etc." )
    protected CodeableConcept type;

    /**
     * The amount of medication that has been dispensed. Includes unit of measure.
     */
    @Child(name = "quantity", type = {Quantity.class}, order = 6, min = 0, max = 1)
    @Description(shortDefinition="Amount dispensed", formalDefinition="The amount of medication that has been dispensed. Includes unit of measure." )
    protected Quantity quantity;

    /**
     * The amount of medication expressed as a timing amount.
     */
    @Child(name = "daysSupply", type = {Quantity.class}, order = 7, min = 0, max = 1)
    @Description(shortDefinition="Days Supply", formalDefinition="The amount of medication expressed as a timing amount." )
    protected Quantity daysSupply;

    /**
     * Identifies the medication being administered. This is either a link to a resource representing the details of the medication or a simple attribute carrying a code that identifies the medication from a known list of medications.
     */
    @Child(name = "medication", type = {Medication.class}, order = 8, min = 0, max = 1)
    @Description(shortDefinition="What medication was supplied", formalDefinition="Identifies the medication being administered. This is either a link to a resource representing the details of the medication or a simple attribute carrying a code that identifies the medication from a known list of medications." )
    protected Reference medication;

    /**
     * The actual object that is the target of the reference (Identifies the medication being administered. This is either a link to a resource representing the details of the medication or a simple attribute carrying a code that identifies the medication from a known list of medications.)
     */
    protected Medication medicationTarget;

    /**
     * The time when the dispensed product was packaged and reviewed.
     */
    @Child(name = "whenPrepared", type = {DateTimeType.class}, order = 9, min = 0, max = 1)
    @Description(shortDefinition="Dispense processing time", formalDefinition="The time when the dispensed product was packaged and reviewed." )
    protected DateTimeType whenPrepared;

    /**
     * The time the dispensed product was provided to the patient or their representative.
     */
    @Child(name = "whenHandedOver", type = {DateTimeType.class}, order = 10, min = 0, max = 1)
    @Description(shortDefinition="Handover time", formalDefinition="The time the dispensed product was provided to the patient or their representative." )
    protected DateTimeType whenHandedOver;

    /**
     * Identification of the facility/location where the medication was shipped to, as part of the dispense event.
     */
    @Child(name = "destination", type = {Location.class}, order = 11, min = 0, max = 1)
    @Description(shortDefinition="Where the medication was sent", formalDefinition="Identification of the facility/location where the medication was shipped to, as part of the dispense event." )
    protected Reference destination;

    /**
     * The actual object that is the target of the reference (Identification of the facility/location where the medication was shipped to, as part of the dispense event.)
     */
    protected Location destinationTarget;

    /**
     * Identifies the person who picked up the medication.  This will usually be a patient or their carer, but some cases exist where it can be a healthcare professional.
     */
    @Child(name = "receiver", type = {Patient.class, Practitioner.class}, order = 12, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Who collected the medication", formalDefinition="Identifies the person who picked up the medication.  This will usually be a patient or their carer, but some cases exist where it can be a healthcare professional." )
    protected List<Reference> receiver;
    /**
     * The actual objects that are the target of the reference (Identifies the person who picked up the medication.  This will usually be a patient or their carer, but some cases exist where it can be a healthcare professional.)
     */
    protected List<Resource> receiverTarget;


    /**
     * Extra information about the dispense that could not be conveyed in the other attributes.
     */
    @Child(name = "note", type = {StringType.class}, order = 13, min = 0, max = 1)
    @Description(shortDefinition="Information about the dispense", formalDefinition="Extra information about the dispense that could not be conveyed in the other attributes." )
    protected StringType note;

    /**
     * Indicates how the medication is to be used by the patient.
     */
    @Child(name = "dosageInstruction", type = {}, order = 14, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Medicine administration instructions to the patient/carer", formalDefinition="Indicates how the medication is to be used by the patient." )
    protected List<MedicationDispenseDosageInstructionComponent> dosageInstruction;

    /**
     * Indicates whether or not substitution was made as part of the dispense.  In some cases substitution will be expected but doesn't happen, in other cases substitution is not expected but does happen.  This block explains what substitition did or did not happen and why.
     */
    @Child(name = "substitution", type = {}, order = 15, min = 0, max = 1)
    @Description(shortDefinition="Deals with substitution of one medicine for another", formalDefinition="Indicates whether or not substitution was made as part of the dispense.  In some cases substitution will be expected but doesn't happen, in other cases substitution is not expected but does happen.  This block explains what substitition did or did not happen and why." )
    protected MedicationDispenseSubstitutionComponent substitution;

    private static final long serialVersionUID = -217601399L;

    public MedicationDispense() {
      super();
    }

    /**
     * @return {@link #identifier} (Identifier assigned by the dispensing facility - this is an identifier assigned outside FHIR.)
     */
    public Identifier getIdentifier() { 
      if (this.identifier == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationDispense.identifier");
        else if (Configuration.doAutoCreate())
          this.identifier = new Identifier(); // cc
      return this.identifier;
    }

    public boolean hasIdentifier() { 
      return this.identifier != null && !this.identifier.isEmpty();
    }

    /**
     * @param value {@link #identifier} (Identifier assigned by the dispensing facility - this is an identifier assigned outside FHIR.)
     */
    public MedicationDispense setIdentifier(Identifier value) { 
      this.identifier = value;
      return this;
    }

    /**
     * @return {@link #status} (A code specifying the state of the set of dispense events.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<MedicationDispenseStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationDispense.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<MedicationDispenseStatus>(new MedicationDispenseStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (A code specifying the state of the set of dispense events.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public MedicationDispense setStatusElement(Enumeration<MedicationDispenseStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return A code specifying the state of the set of dispense events.
     */
    public MedicationDispenseStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value A code specifying the state of the set of dispense events.
     */
    public MedicationDispense setStatus(MedicationDispenseStatus value) { 
      if (value == null)
        this.status = null;
      else {
        if (this.status == null)
          this.status = new Enumeration<MedicationDispenseStatus>(new MedicationDispenseStatusEnumFactory());
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
          throw new Error("Attempt to auto-create MedicationDispense.patient");
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
    public MedicationDispense setPatient(Reference value) { 
      this.patient = value;
      return this;
    }

    /**
     * @return {@link #patient} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (A link to a resource representing the person to whom the medication will be given.)
     */
    public Patient getPatientTarget() { 
      if (this.patientTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationDispense.patient");
        else if (Configuration.doAutoCreate())
          this.patientTarget = new Patient(); // aa
      return this.patientTarget;
    }

    /**
     * @param value {@link #patient} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (A link to a resource representing the person to whom the medication will be given.)
     */
    public MedicationDispense setPatientTarget(Patient value) { 
      this.patientTarget = value;
      return this;
    }

    /**
     * @return {@link #dispenser} (The individual responsible for dispensing the medication.)
     */
    public Reference getDispenser() { 
      if (this.dispenser == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationDispense.dispenser");
        else if (Configuration.doAutoCreate())
          this.dispenser = new Reference(); // cc
      return this.dispenser;
    }

    public boolean hasDispenser() { 
      return this.dispenser != null && !this.dispenser.isEmpty();
    }

    /**
     * @param value {@link #dispenser} (The individual responsible for dispensing the medication.)
     */
    public MedicationDispense setDispenser(Reference value) { 
      this.dispenser = value;
      return this;
    }

    /**
     * @return {@link #dispenser} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The individual responsible for dispensing the medication.)
     */
    public Practitioner getDispenserTarget() { 
      if (this.dispenserTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationDispense.dispenser");
        else if (Configuration.doAutoCreate())
          this.dispenserTarget = new Practitioner(); // aa
      return this.dispenserTarget;
    }

    /**
     * @param value {@link #dispenser} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The individual responsible for dispensing the medication.)
     */
    public MedicationDispense setDispenserTarget(Practitioner value) { 
      this.dispenserTarget = value;
      return this;
    }

    /**
     * @return {@link #authorizingPrescription} (Indicates the medication order that is being dispensed against.)
     */
    public List<Reference> getAuthorizingPrescription() { 
      if (this.authorizingPrescription == null)
        this.authorizingPrescription = new ArrayList<Reference>();
      return this.authorizingPrescription;
    }

    public boolean hasAuthorizingPrescription() { 
      if (this.authorizingPrescription == null)
        return false;
      for (Reference item : this.authorizingPrescription)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #authorizingPrescription} (Indicates the medication order that is being dispensed against.)
     */
    // syntactic sugar
    public Reference addAuthorizingPrescription() { //3
      Reference t = new Reference();
      if (this.authorizingPrescription == null)
        this.authorizingPrescription = new ArrayList<Reference>();
      this.authorizingPrescription.add(t);
      return t;
    }

    /**
     * @return {@link #authorizingPrescription} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. Indicates the medication order that is being dispensed against.)
     */
    public List<MedicationPrescription> getAuthorizingPrescriptionTarget() { 
      if (this.authorizingPrescriptionTarget == null)
        this.authorizingPrescriptionTarget = new ArrayList<MedicationPrescription>();
      return this.authorizingPrescriptionTarget;
    }

    // syntactic sugar
    /**
     * @return {@link #authorizingPrescription} (Add an actual object that is the target of the reference. The reference library doesn't use these, but you can use this to hold the resources if you resolvethemt. Indicates the medication order that is being dispensed against.)
     */
    public MedicationPrescription addAuthorizingPrescriptionTarget() { 
      MedicationPrescription r = new MedicationPrescription();
      if (this.authorizingPrescriptionTarget == null)
        this.authorizingPrescriptionTarget = new ArrayList<MedicationPrescription>();
      this.authorizingPrescriptionTarget.add(r);
      return r;
    }

    /**
     * @return {@link #type} (Indicates the type of dispensing event that is performed. Examples include: Trial Fill, Completion of Trial, Partial Fill, Emergency Fill, Samples, etc.)
     */
    public CodeableConcept getType() { 
      if (this.type == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationDispense.type");
        else if (Configuration.doAutoCreate())
          this.type = new CodeableConcept(); // cc
      return this.type;
    }

    public boolean hasType() { 
      return this.type != null && !this.type.isEmpty();
    }

    /**
     * @param value {@link #type} (Indicates the type of dispensing event that is performed. Examples include: Trial Fill, Completion of Trial, Partial Fill, Emergency Fill, Samples, etc.)
     */
    public MedicationDispense setType(CodeableConcept value) { 
      this.type = value;
      return this;
    }

    /**
     * @return {@link #quantity} (The amount of medication that has been dispensed. Includes unit of measure.)
     */
    public Quantity getQuantity() { 
      if (this.quantity == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationDispense.quantity");
        else if (Configuration.doAutoCreate())
          this.quantity = new Quantity(); // cc
      return this.quantity;
    }

    public boolean hasQuantity() { 
      return this.quantity != null && !this.quantity.isEmpty();
    }

    /**
     * @param value {@link #quantity} (The amount of medication that has been dispensed. Includes unit of measure.)
     */
    public MedicationDispense setQuantity(Quantity value) { 
      this.quantity = value;
      return this;
    }

    /**
     * @return {@link #daysSupply} (The amount of medication expressed as a timing amount.)
     */
    public Quantity getDaysSupply() { 
      if (this.daysSupply == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationDispense.daysSupply");
        else if (Configuration.doAutoCreate())
          this.daysSupply = new Quantity(); // cc
      return this.daysSupply;
    }

    public boolean hasDaysSupply() { 
      return this.daysSupply != null && !this.daysSupply.isEmpty();
    }

    /**
     * @param value {@link #daysSupply} (The amount of medication expressed as a timing amount.)
     */
    public MedicationDispense setDaysSupply(Quantity value) { 
      this.daysSupply = value;
      return this;
    }

    /**
     * @return {@link #medication} (Identifies the medication being administered. This is either a link to a resource representing the details of the medication or a simple attribute carrying a code that identifies the medication from a known list of medications.)
     */
    public Reference getMedication() { 
      if (this.medication == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationDispense.medication");
        else if (Configuration.doAutoCreate())
          this.medication = new Reference(); // cc
      return this.medication;
    }

    public boolean hasMedication() { 
      return this.medication != null && !this.medication.isEmpty();
    }

    /**
     * @param value {@link #medication} (Identifies the medication being administered. This is either a link to a resource representing the details of the medication or a simple attribute carrying a code that identifies the medication from a known list of medications.)
     */
    public MedicationDispense setMedication(Reference value) { 
      this.medication = value;
      return this;
    }

    /**
     * @return {@link #medication} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Identifies the medication being administered. This is either a link to a resource representing the details of the medication or a simple attribute carrying a code that identifies the medication from a known list of medications.)
     */
    public Medication getMedicationTarget() { 
      if (this.medicationTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationDispense.medication");
        else if (Configuration.doAutoCreate())
          this.medicationTarget = new Medication(); // aa
      return this.medicationTarget;
    }

    /**
     * @param value {@link #medication} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Identifies the medication being administered. This is either a link to a resource representing the details of the medication or a simple attribute carrying a code that identifies the medication from a known list of medications.)
     */
    public MedicationDispense setMedicationTarget(Medication value) { 
      this.medicationTarget = value;
      return this;
    }

    /**
     * @return {@link #whenPrepared} (The time when the dispensed product was packaged and reviewed.). This is the underlying object with id, value and extensions. The accessor "getWhenPrepared" gives direct access to the value
     */
    public DateTimeType getWhenPreparedElement() { 
      if (this.whenPrepared == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationDispense.whenPrepared");
        else if (Configuration.doAutoCreate())
          this.whenPrepared = new DateTimeType(); // bb
      return this.whenPrepared;
    }

    public boolean hasWhenPreparedElement() { 
      return this.whenPrepared != null && !this.whenPrepared.isEmpty();
    }

    public boolean hasWhenPrepared() { 
      return this.whenPrepared != null && !this.whenPrepared.isEmpty();
    }

    /**
     * @param value {@link #whenPrepared} (The time when the dispensed product was packaged and reviewed.). This is the underlying object with id, value and extensions. The accessor "getWhenPrepared" gives direct access to the value
     */
    public MedicationDispense setWhenPreparedElement(DateTimeType value) { 
      this.whenPrepared = value;
      return this;
    }

    /**
     * @return The time when the dispensed product was packaged and reviewed.
     */
    public Date getWhenPrepared() { 
      return this.whenPrepared == null ? null : this.whenPrepared.getValue();
    }

    /**
     * @param value The time when the dispensed product was packaged and reviewed.
     */
    public MedicationDispense setWhenPrepared(Date value) { 
      if (value == null)
        this.whenPrepared = null;
      else {
        if (this.whenPrepared == null)
          this.whenPrepared = new DateTimeType();
        this.whenPrepared.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #whenHandedOver} (The time the dispensed product was provided to the patient or their representative.). This is the underlying object with id, value and extensions. The accessor "getWhenHandedOver" gives direct access to the value
     */
    public DateTimeType getWhenHandedOverElement() { 
      if (this.whenHandedOver == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationDispense.whenHandedOver");
        else if (Configuration.doAutoCreate())
          this.whenHandedOver = new DateTimeType(); // bb
      return this.whenHandedOver;
    }

    public boolean hasWhenHandedOverElement() { 
      return this.whenHandedOver != null && !this.whenHandedOver.isEmpty();
    }

    public boolean hasWhenHandedOver() { 
      return this.whenHandedOver != null && !this.whenHandedOver.isEmpty();
    }

    /**
     * @param value {@link #whenHandedOver} (The time the dispensed product was provided to the patient or their representative.). This is the underlying object with id, value and extensions. The accessor "getWhenHandedOver" gives direct access to the value
     */
    public MedicationDispense setWhenHandedOverElement(DateTimeType value) { 
      this.whenHandedOver = value;
      return this;
    }

    /**
     * @return The time the dispensed product was provided to the patient or their representative.
     */
    public Date getWhenHandedOver() { 
      return this.whenHandedOver == null ? null : this.whenHandedOver.getValue();
    }

    /**
     * @param value The time the dispensed product was provided to the patient or their representative.
     */
    public MedicationDispense setWhenHandedOver(Date value) { 
      if (value == null)
        this.whenHandedOver = null;
      else {
        if (this.whenHandedOver == null)
          this.whenHandedOver = new DateTimeType();
        this.whenHandedOver.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #destination} (Identification of the facility/location where the medication was shipped to, as part of the dispense event.)
     */
    public Reference getDestination() { 
      if (this.destination == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationDispense.destination");
        else if (Configuration.doAutoCreate())
          this.destination = new Reference(); // cc
      return this.destination;
    }

    public boolean hasDestination() { 
      return this.destination != null && !this.destination.isEmpty();
    }

    /**
     * @param value {@link #destination} (Identification of the facility/location where the medication was shipped to, as part of the dispense event.)
     */
    public MedicationDispense setDestination(Reference value) { 
      this.destination = value;
      return this;
    }

    /**
     * @return {@link #destination} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Identification of the facility/location where the medication was shipped to, as part of the dispense event.)
     */
    public Location getDestinationTarget() { 
      if (this.destinationTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationDispense.destination");
        else if (Configuration.doAutoCreate())
          this.destinationTarget = new Location(); // aa
      return this.destinationTarget;
    }

    /**
     * @param value {@link #destination} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Identification of the facility/location where the medication was shipped to, as part of the dispense event.)
     */
    public MedicationDispense setDestinationTarget(Location value) { 
      this.destinationTarget = value;
      return this;
    }

    /**
     * @return {@link #receiver} (Identifies the person who picked up the medication.  This will usually be a patient or their carer, but some cases exist where it can be a healthcare professional.)
     */
    public List<Reference> getReceiver() { 
      if (this.receiver == null)
        this.receiver = new ArrayList<Reference>();
      return this.receiver;
    }

    public boolean hasReceiver() { 
      if (this.receiver == null)
        return false;
      for (Reference item : this.receiver)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #receiver} (Identifies the person who picked up the medication.  This will usually be a patient or their carer, but some cases exist where it can be a healthcare professional.)
     */
    // syntactic sugar
    public Reference addReceiver() { //3
      Reference t = new Reference();
      if (this.receiver == null)
        this.receiver = new ArrayList<Reference>();
      this.receiver.add(t);
      return t;
    }

    /**
     * @return {@link #receiver} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. Identifies the person who picked up the medication.  This will usually be a patient or their carer, but some cases exist where it can be a healthcare professional.)
     */
    public List<Resource> getReceiverTarget() { 
      if (this.receiverTarget == null)
        this.receiverTarget = new ArrayList<Resource>();
      return this.receiverTarget;
    }

    /**
     * @return {@link #note} (Extra information about the dispense that could not be conveyed in the other attributes.). This is the underlying object with id, value and extensions. The accessor "getNote" gives direct access to the value
     */
    public StringType getNoteElement() { 
      if (this.note == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationDispense.note");
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
     * @param value {@link #note} (Extra information about the dispense that could not be conveyed in the other attributes.). This is the underlying object with id, value and extensions. The accessor "getNote" gives direct access to the value
     */
    public MedicationDispense setNoteElement(StringType value) { 
      this.note = value;
      return this;
    }

    /**
     * @return Extra information about the dispense that could not be conveyed in the other attributes.
     */
    public String getNote() { 
      return this.note == null ? null : this.note.getValue();
    }

    /**
     * @param value Extra information about the dispense that could not be conveyed in the other attributes.
     */
    public MedicationDispense setNote(String value) { 
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
     * @return {@link #dosageInstruction} (Indicates how the medication is to be used by the patient.)
     */
    public List<MedicationDispenseDosageInstructionComponent> getDosageInstruction() { 
      if (this.dosageInstruction == null)
        this.dosageInstruction = new ArrayList<MedicationDispenseDosageInstructionComponent>();
      return this.dosageInstruction;
    }

    public boolean hasDosageInstruction() { 
      if (this.dosageInstruction == null)
        return false;
      for (MedicationDispenseDosageInstructionComponent item : this.dosageInstruction)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #dosageInstruction} (Indicates how the medication is to be used by the patient.)
     */
    // syntactic sugar
    public MedicationDispenseDosageInstructionComponent addDosageInstruction() { //3
      MedicationDispenseDosageInstructionComponent t = new MedicationDispenseDosageInstructionComponent();
      if (this.dosageInstruction == null)
        this.dosageInstruction = new ArrayList<MedicationDispenseDosageInstructionComponent>();
      this.dosageInstruction.add(t);
      return t;
    }

    /**
     * @return {@link #substitution} (Indicates whether or not substitution was made as part of the dispense.  In some cases substitution will be expected but doesn't happen, in other cases substitution is not expected but does happen.  This block explains what substitition did or did not happen and why.)
     */
    public MedicationDispenseSubstitutionComponent getSubstitution() { 
      if (this.substitution == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationDispense.substitution");
        else if (Configuration.doAutoCreate())
          this.substitution = new MedicationDispenseSubstitutionComponent(); // cc
      return this.substitution;
    }

    public boolean hasSubstitution() { 
      return this.substitution != null && !this.substitution.isEmpty();
    }

    /**
     * @param value {@link #substitution} (Indicates whether or not substitution was made as part of the dispense.  In some cases substitution will be expected but doesn't happen, in other cases substitution is not expected but does happen.  This block explains what substitition did or did not happen and why.)
     */
    public MedicationDispense setSubstitution(MedicationDispenseSubstitutionComponent value) { 
      this.substitution = value;
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "Identifier assigned by the dispensing facility - this is an identifier assigned outside FHIR.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("status", "code", "A code specifying the state of the set of dispense events.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("patient", "Reference(Patient)", "A link to a resource representing the person to whom the medication will be given.", 0, java.lang.Integer.MAX_VALUE, patient));
        childrenList.add(new Property("dispenser", "Reference(Practitioner)", "The individual responsible for dispensing the medication.", 0, java.lang.Integer.MAX_VALUE, dispenser));
        childrenList.add(new Property("authorizingPrescription", "Reference(MedicationPrescription)", "Indicates the medication order that is being dispensed against.", 0, java.lang.Integer.MAX_VALUE, authorizingPrescription));
        childrenList.add(new Property("type", "CodeableConcept", "Indicates the type of dispensing event that is performed. Examples include: Trial Fill, Completion of Trial, Partial Fill, Emergency Fill, Samples, etc.", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("quantity", "Quantity", "The amount of medication that has been dispensed. Includes unit of measure.", 0, java.lang.Integer.MAX_VALUE, quantity));
        childrenList.add(new Property("daysSupply", "Quantity", "The amount of medication expressed as a timing amount.", 0, java.lang.Integer.MAX_VALUE, daysSupply));
        childrenList.add(new Property("medication", "Reference(Medication)", "Identifies the medication being administered. This is either a link to a resource representing the details of the medication or a simple attribute carrying a code that identifies the medication from a known list of medications.", 0, java.lang.Integer.MAX_VALUE, medication));
        childrenList.add(new Property("whenPrepared", "dateTime", "The time when the dispensed product was packaged and reviewed.", 0, java.lang.Integer.MAX_VALUE, whenPrepared));
        childrenList.add(new Property("whenHandedOver", "dateTime", "The time the dispensed product was provided to the patient or their representative.", 0, java.lang.Integer.MAX_VALUE, whenHandedOver));
        childrenList.add(new Property("destination", "Reference(Location)", "Identification of the facility/location where the medication was shipped to, as part of the dispense event.", 0, java.lang.Integer.MAX_VALUE, destination));
        childrenList.add(new Property("receiver", "Reference(Patient|Practitioner)", "Identifies the person who picked up the medication.  This will usually be a patient or their carer, but some cases exist where it can be a healthcare professional.", 0, java.lang.Integer.MAX_VALUE, receiver));
        childrenList.add(new Property("note", "string", "Extra information about the dispense that could not be conveyed in the other attributes.", 0, java.lang.Integer.MAX_VALUE, note));
        childrenList.add(new Property("dosageInstruction", "", "Indicates how the medication is to be used by the patient.", 0, java.lang.Integer.MAX_VALUE, dosageInstruction));
        childrenList.add(new Property("substitution", "", "Indicates whether or not substitution was made as part of the dispense.  In some cases substitution will be expected but doesn't happen, in other cases substitution is not expected but does happen.  This block explains what substitition did or did not happen and why.", 0, java.lang.Integer.MAX_VALUE, substitution));
      }

      public MedicationDispense copy() {
        MedicationDispense dst = new MedicationDispense();
        copyValues(dst);
        dst.identifier = identifier == null ? null : identifier.copy();
        dst.status = status == null ? null : status.copy();
        dst.patient = patient == null ? null : patient.copy();
        dst.dispenser = dispenser == null ? null : dispenser.copy();
        if (authorizingPrescription != null) {
          dst.authorizingPrescription = new ArrayList<Reference>();
          for (Reference i : authorizingPrescription)
            dst.authorizingPrescription.add(i.copy());
        };
        dst.type = type == null ? null : type.copy();
        dst.quantity = quantity == null ? null : quantity.copy();
        dst.daysSupply = daysSupply == null ? null : daysSupply.copy();
        dst.medication = medication == null ? null : medication.copy();
        dst.whenPrepared = whenPrepared == null ? null : whenPrepared.copy();
        dst.whenHandedOver = whenHandedOver == null ? null : whenHandedOver.copy();
        dst.destination = destination == null ? null : destination.copy();
        if (receiver != null) {
          dst.receiver = new ArrayList<Reference>();
          for (Reference i : receiver)
            dst.receiver.add(i.copy());
        };
        dst.note = note == null ? null : note.copy();
        if (dosageInstruction != null) {
          dst.dosageInstruction = new ArrayList<MedicationDispenseDosageInstructionComponent>();
          for (MedicationDispenseDosageInstructionComponent i : dosageInstruction)
            dst.dosageInstruction.add(i.copy());
        };
        dst.substitution = substitution == null ? null : substitution.copy();
        return dst;
      }

      protected MedicationDispense typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof MedicationDispense))
          return false;
        MedicationDispense o = (MedicationDispense) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(status, o.status, true) && compareDeep(patient, o.patient, true)
           && compareDeep(dispenser, o.dispenser, true) && compareDeep(authorizingPrescription, o.authorizingPrescription, true)
           && compareDeep(type, o.type, true) && compareDeep(quantity, o.quantity, true) && compareDeep(daysSupply, o.daysSupply, true)
           && compareDeep(medication, o.medication, true) && compareDeep(whenPrepared, o.whenPrepared, true)
           && compareDeep(whenHandedOver, o.whenHandedOver, true) && compareDeep(destination, o.destination, true)
           && compareDeep(receiver, o.receiver, true) && compareDeep(note, o.note, true) && compareDeep(dosageInstruction, o.dosageInstruction, true)
           && compareDeep(substitution, o.substitution, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof MedicationDispense))
          return false;
        MedicationDispense o = (MedicationDispense) other;
        return compareValues(status, o.status, true) && compareValues(whenPrepared, o.whenPrepared, true) && compareValues(whenHandedOver, o.whenHandedOver, true)
           && compareValues(note, o.note, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (status == null || status.isEmpty())
           && (patient == null || patient.isEmpty()) && (dispenser == null || dispenser.isEmpty()) && (authorizingPrescription == null || authorizingPrescription.isEmpty())
           && (type == null || type.isEmpty()) && (quantity == null || quantity.isEmpty()) && (daysSupply == null || daysSupply.isEmpty())
           && (medication == null || medication.isEmpty()) && (whenPrepared == null || whenPrepared.isEmpty())
           && (whenHandedOver == null || whenHandedOver.isEmpty()) && (destination == null || destination.isEmpty())
           && (receiver == null || receiver.isEmpty()) && (note == null || note.isEmpty()) && (dosageInstruction == null || dosageInstruction.isEmpty())
           && (substitution == null || substitution.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.MedicationDispense;
   }

    @SearchParamDefinition(name = "dispenser", path = "MedicationDispense.dispenser", description = "Return all dispenses performed by a specific indiividual", type = "reference")
    public static final String SP_DISPENSER = "dispenser";
    @SearchParamDefinition(name = "identifier", path = "MedicationDispense.identifier", description = "Return dispenses with this external identity", type = "token")
    public static final String SP_IDENTIFIER = "identifier";
  @SearchParamDefinition(name="prescription", path="MedicationDispense.authorizingPrescription", description="The identity of a prescription to list dispenses from", type="reference" )
  public static final String SP_PRESCRIPTION = "prescription";
    @SearchParamDefinition(name = "patient", path = "MedicationDispense.patient", description = "The identity of a patient to list dispenses  for", type = "reference")
    public static final String SP_PATIENT = "patient";
    @SearchParamDefinition(name = "destination", path = "MedicationDispense.destination", description = "Return dispenses that should be sent to a secific destination", type = "reference")
    public static final String SP_DESTINATION = "destination";
    @SearchParamDefinition(name = "medication", path = "MedicationDispense.medication", description = "Returns dispenses of this medicine", type = "reference")
    public static final String SP_MEDICATION = "medication";
  @SearchParamDefinition(name="responsibleparty", path="MedicationDispense.substitution.responsibleParty", description="Return all dispenses with the specified responsible party", type="reference" )
  public static final String SP_RESPONSIBLEPARTY = "responsibleparty";
  @SearchParamDefinition(name="type", path="MedicationDispense.type", description="Return all dispenses of a specific type", type="token" )
  public static final String SP_TYPE = "type";
  @SearchParamDefinition(name="whenhandedover", path="MedicationDispense.whenHandedOver", description="Date when medication handed over to patient (outpatient setting), or supplied to ward or clinic (inpatient setting)", type="date" )
  public static final String SP_WHENHANDEDOVER = "whenhandedover";
    @SearchParamDefinition(name = "whenprepared", path = "MedicationDispense.whenPrepared", description = "Date when medication prepared", type = "date")
    public static final String SP_WHENPREPARED = "whenprepared";
    @SearchParamDefinition(name = "status", path = "MedicationDispense.status", description = "Status of the dispense", type = "token")
    public static final String SP_STATUS = "status";

}

