package org.hl7.fhir.dstu2016may.model;

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

// Generated on Sun, May 8, 2016 03:05+1000 for FHIR v1.4.0
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseBackboneElement;
import org.hl7.fhir.utilities.Utilities;

import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
/**
 * Indicates that a medication product is to be or has been dispensed for a named person/patient.  This includes a description of the medication product (supply) provided and the instructions for administering the medication.  The medication dispense is the result of a pharmacy system responding to a medication order.
 */
@ResourceDef(name="MedicationDispense", profile="http://hl7.org/fhir/Profile/MedicationDispense")
public class MedicationDispense extends DomainResource {

    public enum MedicationDispenseStatus {
        /**
         * The dispense has started but has not yet completed.
         */
        INPROGRESS, 
        /**
         * Actions implied by the administration have been temporarily halted, but are expected to continue later. May also be called "suspended"
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
        public static MedicationDispenseStatus fromCode(String codeString) throws FHIRException {
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
        throw new FHIRException("Unknown MedicationDispenseStatus code '"+codeString+"'");
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
            case INPROGRESS: return "http://hl7.org/fhir/medication-dispense-status";
            case ONHOLD: return "http://hl7.org/fhir/medication-dispense-status";
            case COMPLETED: return "http://hl7.org/fhir/medication-dispense-status";
            case ENTEREDINERROR: return "http://hl7.org/fhir/medication-dispense-status";
            case STOPPED: return "http://hl7.org/fhir/medication-dispense-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case INPROGRESS: return "The dispense has started but has not yet completed.";
            case ONHOLD: return "Actions implied by the administration have been temporarily halted, but are expected to continue later. May also be called \"suspended\"";
            case COMPLETED: return "All actions that are implied by the dispense have occurred.";
            case ENTEREDINERROR: return "The dispense was entered in error and therefore nullified.";
            case STOPPED: return "Actions implied by the dispense have been permanently halted, before all of them occurred.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case INPROGRESS: return "In Progress";
            case ONHOLD: return "On Hold";
            case COMPLETED: return "Completed";
            case ENTEREDINERROR: return "Entered in-Error";
            case STOPPED: return "Stopped";
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
        public Enumeration<MedicationDispenseStatus> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("in-progress".equals(codeString))
          return new Enumeration<MedicationDispenseStatus>(this, MedicationDispenseStatus.INPROGRESS);
        if ("on-hold".equals(codeString))
          return new Enumeration<MedicationDispenseStatus>(this, MedicationDispenseStatus.ONHOLD);
        if ("completed".equals(codeString))
          return new Enumeration<MedicationDispenseStatus>(this, MedicationDispenseStatus.COMPLETED);
        if ("entered-in-error".equals(codeString))
          return new Enumeration<MedicationDispenseStatus>(this, MedicationDispenseStatus.ENTEREDINERROR);
        if ("stopped".equals(codeString))
          return new Enumeration<MedicationDispenseStatus>(this, MedicationDispenseStatus.STOPPED);
        throw new FHIRException("Unknown MedicationDispenseStatus code '"+codeString+"'");
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
    public String toSystem(MedicationDispenseStatus code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class MedicationDispenseDosageInstructionComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Free text dosage instructions can be used for cases where the instructions are too complex to code. When coded instructions are present, the free text instructions may still be present for display to humans taking or administering the medication.
         */
        @Child(name = "text", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Free text dosage instructions e.g. SIG", formalDefinition="Free text dosage instructions can be used for cases where the instructions are too complex to code. When coded instructions are present, the free text instructions may still be present for display to humans taking or administering the medication." )
        protected StringType text;

        /**
         * Additional instructions such as "Swallow with plenty of water" which may or may not be coded.
         */
        @Child(name = "additionalInstructions", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="E.g. \"Take with food\"", formalDefinition="Additional instructions such as \"Swallow with plenty of water\" which may or may not be coded." )
        protected CodeableConcept additionalInstructions;

        /**
         * The timing schedule for giving the medication to the patient.  The Schedule data type allows many different expressions.  For example, "Every  8 hours"; "Three times a day"; "1/2 an hour before breakfast for 10 days from 23-Dec 2011:";  "15 Oct 2013, 17 Oct 2013 and 1 Nov 2013".
         */
        @Child(name = "timing", type = {Timing.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="When medication should be administered", formalDefinition="The timing schedule for giving the medication to the patient.  The Schedule data type allows many different expressions.  For example, \"Every  8 hours\"; \"Three times a day\"; \"1/2 an hour before breakfast for 10 days from 23-Dec 2011:\";  \"15 Oct 2013, 17 Oct 2013 and 1 Nov 2013\"." )
        protected Timing timing;

        /**
         * Indicates whether the Medication is only taken when needed within a specific dosing schedule (Boolean option), or it indicates the precondition for taking the Medication (CodeableConcept).  

Specifically if 'boolean' datatype is selected, then the following logic applies:  If set to True, this indicates that the medication is only taken when needed, within the specified schedule.
         */
        @Child(name = "asNeeded", type = {BooleanType.class, CodeableConcept.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Take \"as needed\" f(or x)", formalDefinition="Indicates whether the Medication is only taken when needed within a specific dosing schedule (Boolean option), or it indicates the precondition for taking the Medication (CodeableConcept).  \n\nSpecifically if 'boolean' datatype is selected, then the following logic applies:  If set to True, this indicates that the medication is only taken when needed, within the specified schedule." )
        protected Type asNeeded;

        /**
         * A coded specification of the anatomic site where the medication first enters the body.
         */
        @Child(name = "site", type = {CodeableConcept.class, BodySite.class}, order=5, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Body site to administer to", formalDefinition="A coded specification of the anatomic site where the medication first enters the body." )
        protected Type site;

        /**
         * A code specifying the route or physiological path of administration of a therapeutic agent into or onto a subject.
         */
        @Child(name = "route", type = {CodeableConcept.class}, order=6, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="How drug should enter body", formalDefinition="A code specifying the route or physiological path of administration of a therapeutic agent into or onto a subject." )
        protected CodeableConcept route;

        /**
         * A coded value indicating the method by which the medication is intended to be or was introduced into or on the body.
         */
        @Child(name = "method", type = {CodeableConcept.class}, order=7, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Technique for administering medication", formalDefinition="A coded value indicating the method by which the medication is intended to be or was introduced into or on the body." )
        protected CodeableConcept method;

        /**
         * The amount of therapeutic or other substance given at one administration event.
         */
        @Child(name = "dose", type = {Range.class, SimpleQuantity.class}, order=8, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Amount of medication per dose", formalDefinition="The amount of therapeutic or other substance given at one administration event." )
        protected Type dose;

        /**
         * Identifies the speed with which the medication was or will be introduced into the patient. Typically the rate for an infusion e.g. 100 ml per 1 hour or 100 ml/hr.  May also be expressed as a rate per unit of time e.g. 500 ml per 2 hours.   Currently we do not specify a default of '1' in the denominator, but this is being discussed. Other examples: 200 mcg/min or 200 mcg/1 minute; 1 liter/8 hours.
         */
        @Child(name = "rate", type = {Ratio.class, Range.class}, order=9, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Amount of medication per unit of time", formalDefinition="Identifies the speed with which the medication was or will be introduced into the patient. Typically the rate for an infusion e.g. 100 ml per 1 hour or 100 ml/hr.  May also be expressed as a rate per unit of time e.g. 500 ml per 2 hours.   Currently we do not specify a default of '1' in the denominator, but this is being discussed. Other examples: 200 mcg/min or 200 mcg/1 minute; 1 liter/8 hours." )
        protected Type rate;

        /**
         * The maximum total quantity of a therapeutic substance that may be administered to a subject over the period of time,  e.g. 1000mg in 24 hours.
         */
        @Child(name = "maxDosePerPeriod", type = {Ratio.class}, order=10, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Upper limit on medication per unit of time", formalDefinition="The maximum total quantity of a therapeutic substance that may be administered to a subject over the period of time,  e.g. 1000mg in 24 hours." )
        protected Ratio maxDosePerPeriod;

        private static final long serialVersionUID = -1470136646L;

    /**
     * Constructor
     */
      public MedicationDispenseDosageInstructionComponent() {
        super();
      }

        /**
         * @return {@link #text} (Free text dosage instructions can be used for cases where the instructions are too complex to code. When coded instructions are present, the free text instructions may still be present for display to humans taking or administering the medication.). This is the underlying object with id, value and extensions. The accessor "getText" gives direct access to the value
         */
        public StringType getTextElement() { 
          if (this.text == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationDispenseDosageInstructionComponent.text");
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
        public MedicationDispenseDosageInstructionComponent setTextElement(StringType value) { 
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
        public MedicationDispenseDosageInstructionComponent setText(String value) { 
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
         * @return {@link #timing} (The timing schedule for giving the medication to the patient.  The Schedule data type allows many different expressions.  For example, "Every  8 hours"; "Three times a day"; "1/2 an hour before breakfast for 10 days from 23-Dec 2011:";  "15 Oct 2013, 17 Oct 2013 and 1 Nov 2013".)
         */
        public Timing getTiming() { 
          if (this.timing == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationDispenseDosageInstructionComponent.timing");
            else if (Configuration.doAutoCreate())
              this.timing = new Timing(); // cc
          return this.timing;
        }

        public boolean hasTiming() { 
          return this.timing != null && !this.timing.isEmpty();
        }

        /**
         * @param value {@link #timing} (The timing schedule for giving the medication to the patient.  The Schedule data type allows many different expressions.  For example, "Every  8 hours"; "Three times a day"; "1/2 an hour before breakfast for 10 days from 23-Dec 2011:";  "15 Oct 2013, 17 Oct 2013 and 1 Nov 2013".)
         */
        public MedicationDispenseDosageInstructionComponent setTiming(Timing value) { 
          this.timing = value;
          return this;
        }

        /**
         * @return {@link #asNeeded} (Indicates whether the Medication is only taken when needed within a specific dosing schedule (Boolean option), or it indicates the precondition for taking the Medication (CodeableConcept).  

Specifically if 'boolean' datatype is selected, then the following logic applies:  If set to True, this indicates that the medication is only taken when needed, within the specified schedule.)
         */
        public Type getAsNeeded() { 
          return this.asNeeded;
        }

        /**
         * @return {@link #asNeeded} (Indicates whether the Medication is only taken when needed within a specific dosing schedule (Boolean option), or it indicates the precondition for taking the Medication (CodeableConcept).  

Specifically if 'boolean' datatype is selected, then the following logic applies:  If set to True, this indicates that the medication is only taken when needed, within the specified schedule.)
         */
        public BooleanType getAsNeededBooleanType() throws FHIRException { 
          if (!(this.asNeeded instanceof BooleanType))
            throw new FHIRException("Type mismatch: the type BooleanType was expected, but "+this.asNeeded.getClass().getName()+" was encountered");
          return (BooleanType) this.asNeeded;
        }

        public boolean hasAsNeededBooleanType() { 
          return this.asNeeded instanceof BooleanType;
        }

        /**
         * @return {@link #asNeeded} (Indicates whether the Medication is only taken when needed within a specific dosing schedule (Boolean option), or it indicates the precondition for taking the Medication (CodeableConcept).  

Specifically if 'boolean' datatype is selected, then the following logic applies:  If set to True, this indicates that the medication is only taken when needed, within the specified schedule.)
         */
        public CodeableConcept getAsNeededCodeableConcept() throws FHIRException { 
          if (!(this.asNeeded instanceof CodeableConcept))
            throw new FHIRException("Type mismatch: the type CodeableConcept was expected, but "+this.asNeeded.getClass().getName()+" was encountered");
          return (CodeableConcept) this.asNeeded;
        }

        public boolean hasAsNeededCodeableConcept() { 
          return this.asNeeded instanceof CodeableConcept;
        }

        public boolean hasAsNeeded() { 
          return this.asNeeded != null && !this.asNeeded.isEmpty();
        }

        /**
         * @param value {@link #asNeeded} (Indicates whether the Medication is only taken when needed within a specific dosing schedule (Boolean option), or it indicates the precondition for taking the Medication (CodeableConcept).  

Specifically if 'boolean' datatype is selected, then the following logic applies:  If set to True, this indicates that the medication is only taken when needed, within the specified schedule.)
         */
        public MedicationDispenseDosageInstructionComponent setAsNeeded(Type value) { 
          this.asNeeded = value;
          return this;
        }

        /**
         * @return {@link #site} (A coded specification of the anatomic site where the medication first enters the body.)
         */
        public Type getSite() { 
          return this.site;
        }

        /**
         * @return {@link #site} (A coded specification of the anatomic site where the medication first enters the body.)
         */
        public CodeableConcept getSiteCodeableConcept() throws FHIRException { 
          if (!(this.site instanceof CodeableConcept))
            throw new FHIRException("Type mismatch: the type CodeableConcept was expected, but "+this.site.getClass().getName()+" was encountered");
          return (CodeableConcept) this.site;
        }

        public boolean hasSiteCodeableConcept() { 
          return this.site instanceof CodeableConcept;
        }

        /**
         * @return {@link #site} (A coded specification of the anatomic site where the medication first enters the body.)
         */
        public Reference getSiteReference() throws FHIRException { 
          if (!(this.site instanceof Reference))
            throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.site.getClass().getName()+" was encountered");
          return (Reference) this.site;
        }

        public boolean hasSiteReference() { 
          return this.site instanceof Reference;
        }

        public boolean hasSite() { 
          return this.site != null && !this.site.isEmpty();
        }

        /**
         * @param value {@link #site} (A coded specification of the anatomic site where the medication first enters the body.)
         */
        public MedicationDispenseDosageInstructionComponent setSite(Type value) { 
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
         * @return {@link #method} (A coded value indicating the method by which the medication is intended to be or was introduced into or on the body.)
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
         * @param value {@link #method} (A coded value indicating the method by which the medication is intended to be or was introduced into or on the body.)
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
        public Range getDoseRange() throws FHIRException { 
          if (!(this.dose instanceof Range))
            throw new FHIRException("Type mismatch: the type Range was expected, but "+this.dose.getClass().getName()+" was encountered");
          return (Range) this.dose;
        }

        public boolean hasDoseRange() { 
          return this.dose instanceof Range;
        }

        /**
         * @return {@link #dose} (The amount of therapeutic or other substance given at one administration event.)
         */
        public SimpleQuantity getDoseSimpleQuantity() throws FHIRException { 
          if (!(this.dose instanceof SimpleQuantity))
            throw new FHIRException("Type mismatch: the type SimpleQuantity was expected, but "+this.dose.getClass().getName()+" was encountered");
          return (SimpleQuantity) this.dose;
        }

        public boolean hasDoseSimpleQuantity() { 
          return this.dose instanceof SimpleQuantity;
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
         * @return {@link #rate} (Identifies the speed with which the medication was or will be introduced into the patient. Typically the rate for an infusion e.g. 100 ml per 1 hour or 100 ml/hr.  May also be expressed as a rate per unit of time e.g. 500 ml per 2 hours.   Currently we do not specify a default of '1' in the denominator, but this is being discussed. Other examples: 200 mcg/min or 200 mcg/1 minute; 1 liter/8 hours.)
         */
        public Type getRate() { 
          return this.rate;
        }

        /**
         * @return {@link #rate} (Identifies the speed with which the medication was or will be introduced into the patient. Typically the rate for an infusion e.g. 100 ml per 1 hour or 100 ml/hr.  May also be expressed as a rate per unit of time e.g. 500 ml per 2 hours.   Currently we do not specify a default of '1' in the denominator, but this is being discussed. Other examples: 200 mcg/min or 200 mcg/1 minute; 1 liter/8 hours.)
         */
        public Ratio getRateRatio() throws FHIRException { 
          if (!(this.rate instanceof Ratio))
            throw new FHIRException("Type mismatch: the type Ratio was expected, but "+this.rate.getClass().getName()+" was encountered");
          return (Ratio) this.rate;
        }

        public boolean hasRateRatio() { 
          return this.rate instanceof Ratio;
        }

        /**
         * @return {@link #rate} (Identifies the speed with which the medication was or will be introduced into the patient. Typically the rate for an infusion e.g. 100 ml per 1 hour or 100 ml/hr.  May also be expressed as a rate per unit of time e.g. 500 ml per 2 hours.   Currently we do not specify a default of '1' in the denominator, but this is being discussed. Other examples: 200 mcg/min or 200 mcg/1 minute; 1 liter/8 hours.)
         */
        public Range getRateRange() throws FHIRException { 
          if (!(this.rate instanceof Range))
            throw new FHIRException("Type mismatch: the type Range was expected, but "+this.rate.getClass().getName()+" was encountered");
          return (Range) this.rate;
        }

        public boolean hasRateRange() { 
          return this.rate instanceof Range;
        }

        public boolean hasRate() { 
          return this.rate != null && !this.rate.isEmpty();
        }

        /**
         * @param value {@link #rate} (Identifies the speed with which the medication was or will be introduced into the patient. Typically the rate for an infusion e.g. 100 ml per 1 hour or 100 ml/hr.  May also be expressed as a rate per unit of time e.g. 500 ml per 2 hours.   Currently we do not specify a default of '1' in the denominator, but this is being discussed. Other examples: 200 mcg/min or 200 mcg/1 minute; 1 liter/8 hours.)
         */
        public MedicationDispenseDosageInstructionComponent setRate(Type value) { 
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
          childrenList.add(new Property("text", "string", "Free text dosage instructions can be used for cases where the instructions are too complex to code. When coded instructions are present, the free text instructions may still be present for display to humans taking or administering the medication.", 0, java.lang.Integer.MAX_VALUE, text));
          childrenList.add(new Property("additionalInstructions", "CodeableConcept", "Additional instructions such as \"Swallow with plenty of water\" which may or may not be coded.", 0, java.lang.Integer.MAX_VALUE, additionalInstructions));
          childrenList.add(new Property("timing", "Timing", "The timing schedule for giving the medication to the patient.  The Schedule data type allows many different expressions.  For example, \"Every  8 hours\"; \"Three times a day\"; \"1/2 an hour before breakfast for 10 days from 23-Dec 2011:\";  \"15 Oct 2013, 17 Oct 2013 and 1 Nov 2013\".", 0, java.lang.Integer.MAX_VALUE, timing));
          childrenList.add(new Property("asNeeded[x]", "boolean|CodeableConcept", "Indicates whether the Medication is only taken when needed within a specific dosing schedule (Boolean option), or it indicates the precondition for taking the Medication (CodeableConcept).  \n\nSpecifically if 'boolean' datatype is selected, then the following logic applies:  If set to True, this indicates that the medication is only taken when needed, within the specified schedule.", 0, java.lang.Integer.MAX_VALUE, asNeeded));
          childrenList.add(new Property("site[x]", "CodeableConcept|Reference(BodySite)", "A coded specification of the anatomic site where the medication first enters the body.", 0, java.lang.Integer.MAX_VALUE, site));
          childrenList.add(new Property("route", "CodeableConcept", "A code specifying the route or physiological path of administration of a therapeutic agent into or onto a subject.", 0, java.lang.Integer.MAX_VALUE, route));
          childrenList.add(new Property("method", "CodeableConcept", "A coded value indicating the method by which the medication is intended to be or was introduced into or on the body.", 0, java.lang.Integer.MAX_VALUE, method));
          childrenList.add(new Property("dose[x]", "Range|SimpleQuantity", "The amount of therapeutic or other substance given at one administration event.", 0, java.lang.Integer.MAX_VALUE, dose));
          childrenList.add(new Property("rate[x]", "Ratio|Range", "Identifies the speed with which the medication was or will be introduced into the patient. Typically the rate for an infusion e.g. 100 ml per 1 hour or 100 ml/hr.  May also be expressed as a rate per unit of time e.g. 500 ml per 2 hours.   Currently we do not specify a default of '1' in the denominator, but this is being discussed. Other examples: 200 mcg/min or 200 mcg/1 minute; 1 liter/8 hours.", 0, java.lang.Integer.MAX_VALUE, rate));
          childrenList.add(new Property("maxDosePerPeriod", "Ratio", "The maximum total quantity of a therapeutic substance that may be administered to a subject over the period of time,  e.g. 1000mg in 24 hours.", 0, java.lang.Integer.MAX_VALUE, maxDosePerPeriod));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3556653: /*text*/ return this.text == null ? new Base[0] : new Base[] {this.text}; // StringType
        case -1206718612: /*additionalInstructions*/ return this.additionalInstructions == null ? new Base[0] : new Base[] {this.additionalInstructions}; // CodeableConcept
        case -873664438: /*timing*/ return this.timing == null ? new Base[0] : new Base[] {this.timing}; // Timing
        case -1432923513: /*asNeeded*/ return this.asNeeded == null ? new Base[0] : new Base[] {this.asNeeded}; // Type
        case 3530567: /*site*/ return this.site == null ? new Base[0] : new Base[] {this.site}; // Type
        case 108704329: /*route*/ return this.route == null ? new Base[0] : new Base[] {this.route}; // CodeableConcept
        case -1077554975: /*method*/ return this.method == null ? new Base[0] : new Base[] {this.method}; // CodeableConcept
        case 3089437: /*dose*/ return this.dose == null ? new Base[0] : new Base[] {this.dose}; // Type
        case 3493088: /*rate*/ return this.rate == null ? new Base[0] : new Base[] {this.rate}; // Type
        case 1506263709: /*maxDosePerPeriod*/ return this.maxDosePerPeriod == null ? new Base[0] : new Base[] {this.maxDosePerPeriod}; // Ratio
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3556653: // text
          this.text = castToString(value); // StringType
          break;
        case -1206718612: // additionalInstructions
          this.additionalInstructions = castToCodeableConcept(value); // CodeableConcept
          break;
        case -873664438: // timing
          this.timing = castToTiming(value); // Timing
          break;
        case -1432923513: // asNeeded
          this.asNeeded = (Type) value; // Type
          break;
        case 3530567: // site
          this.site = (Type) value; // Type
          break;
        case 108704329: // route
          this.route = castToCodeableConcept(value); // CodeableConcept
          break;
        case -1077554975: // method
          this.method = castToCodeableConcept(value); // CodeableConcept
          break;
        case 3089437: // dose
          this.dose = (Type) value; // Type
          break;
        case 3493088: // rate
          this.rate = (Type) value; // Type
          break;
        case 1506263709: // maxDosePerPeriod
          this.maxDosePerPeriod = castToRatio(value); // Ratio
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("text"))
          this.text = castToString(value); // StringType
        else if (name.equals("additionalInstructions"))
          this.additionalInstructions = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("timing"))
          this.timing = castToTiming(value); // Timing
        else if (name.equals("asNeeded[x]"))
          this.asNeeded = (Type) value; // Type
        else if (name.equals("site[x]"))
          this.site = (Type) value; // Type
        else if (name.equals("route"))
          this.route = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("method"))
          this.method = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("dose[x]"))
          this.dose = (Type) value; // Type
        else if (name.equals("rate[x]"))
          this.rate = (Type) value; // Type
        else if (name.equals("maxDosePerPeriod"))
          this.maxDosePerPeriod = castToRatio(value); // Ratio
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3556653: throw new FHIRException("Cannot make property text as it is not a complex type"); // StringType
        case -1206718612:  return getAdditionalInstructions(); // CodeableConcept
        case -873664438:  return getTiming(); // Timing
        case -544329575:  return getAsNeeded(); // Type
        case 2099997657:  return getSite(); // Type
        case 108704329:  return getRoute(); // CodeableConcept
        case -1077554975:  return getMethod(); // CodeableConcept
        case 1843195715:  return getDose(); // Type
        case 983460768:  return getRate(); // Type
        case 1506263709:  return getMaxDosePerPeriod(); // Ratio
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("text")) {
          throw new FHIRException("Cannot call addChild on a primitive type MedicationDispense.text");
        }
        else if (name.equals("additionalInstructions")) {
          this.additionalInstructions = new CodeableConcept();
          return this.additionalInstructions;
        }
        else if (name.equals("timing")) {
          this.timing = new Timing();
          return this.timing;
        }
        else if (name.equals("asNeededBoolean")) {
          this.asNeeded = new BooleanType();
          return this.asNeeded;
        }
        else if (name.equals("asNeededCodeableConcept")) {
          this.asNeeded = new CodeableConcept();
          return this.asNeeded;
        }
        else if (name.equals("siteCodeableConcept")) {
          this.site = new CodeableConcept();
          return this.site;
        }
        else if (name.equals("siteReference")) {
          this.site = new Reference();
          return this.site;
        }
        else if (name.equals("route")) {
          this.route = new CodeableConcept();
          return this.route;
        }
        else if (name.equals("method")) {
          this.method = new CodeableConcept();
          return this.method;
        }
        else if (name.equals("doseRange")) {
          this.dose = new Range();
          return this.dose;
        }
        else if (name.equals("doseSimpleQuantity")) {
          this.dose = new SimpleQuantity();
          return this.dose;
        }
        else if (name.equals("rateRatio")) {
          this.rate = new Ratio();
          return this.rate;
        }
        else if (name.equals("rateRange")) {
          this.rate = new Range();
          return this.rate;
        }
        else if (name.equals("maxDosePerPeriod")) {
          this.maxDosePerPeriod = new Ratio();
          return this.maxDosePerPeriod;
        }
        else
          return super.addChild(name);
      }

      public MedicationDispenseDosageInstructionComponent copy() {
        MedicationDispenseDosageInstructionComponent dst = new MedicationDispenseDosageInstructionComponent();
        copyValues(dst);
        dst.text = text == null ? null : text.copy();
        dst.additionalInstructions = additionalInstructions == null ? null : additionalInstructions.copy();
        dst.timing = timing == null ? null : timing.copy();
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
        return compareDeep(text, o.text, true) && compareDeep(additionalInstructions, o.additionalInstructions, true)
           && compareDeep(timing, o.timing, true) && compareDeep(asNeeded, o.asNeeded, true) && compareDeep(site, o.site, true)
           && compareDeep(route, o.route, true) && compareDeep(method, o.method, true) && compareDeep(dose, o.dose, true)
           && compareDeep(rate, o.rate, true) && compareDeep(maxDosePerPeriod, o.maxDosePerPeriod, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof MedicationDispenseDosageInstructionComponent))
          return false;
        MedicationDispenseDosageInstructionComponent o = (MedicationDispenseDosageInstructionComponent) other;
        return compareValues(text, o.text, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (text == null || text.isEmpty()) && (additionalInstructions == null || additionalInstructions.isEmpty())
           && (timing == null || timing.isEmpty()) && (asNeeded == null || asNeeded.isEmpty()) && (site == null || site.isEmpty())
           && (route == null || route.isEmpty()) && (method == null || method.isEmpty()) && (dose == null || dose.isEmpty())
           && (rate == null || rate.isEmpty()) && (maxDosePerPeriod == null || maxDosePerPeriod.isEmpty())
          ;
      }

  public String fhirType() {
    return "MedicationDispense.dosageInstruction";

  }

  }

    @Block()
    public static class MedicationDispenseSubstitutionComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A code signifying whether a different drug was dispensed from what was prescribed.
         */
        @Child(name = "type", type = {CodeableConcept.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Code signifying whether a different drug was dispensed from what was prescribed", formalDefinition="A code signifying whether a different drug was dispensed from what was prescribed." )
        protected CodeableConcept type;

        /**
         * Indicates the reason for the substitution of (or lack of substitution) from what was prescribed.
         */
        @Child(name = "reason", type = {CodeableConcept.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Why was substitution made", formalDefinition="Indicates the reason for the substitution of (or lack of substitution) from what was prescribed." )
        protected List<CodeableConcept> reason;

        /**
         * The person or organization that has primary responsibility for the substitution.
         */
        @Child(name = "responsibleParty", type = {Practitioner.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Who is responsible for the substitution", formalDefinition="The person or organization that has primary responsibility for the substitution." )
        protected List<Reference> responsibleParty;
        /**
         * The actual objects that are the target of the reference (The person or organization that has primary responsibility for the substitution.)
         */
        protected List<Practitioner> responsiblePartyTarget;


        private static final long serialVersionUID = 1218245830L;

    /**
     * Constructor
     */
      public MedicationDispenseSubstitutionComponent() {
        super();
      }

    /**
     * Constructor
     */
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

    // syntactic sugar
        public MedicationDispenseSubstitutionComponent addReason(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.reason == null)
            this.reason = new ArrayList<CodeableConcept>();
          this.reason.add(t);
          return this;
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

    // syntactic sugar
        public MedicationDispenseSubstitutionComponent addResponsibleParty(Reference t) { //3
          if (t == null)
            return this;
          if (this.responsibleParty == null)
            this.responsibleParty = new ArrayList<Reference>();
          this.responsibleParty.add(t);
          return this;
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

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case -934964668: /*reason*/ return this.reason == null ? new Base[0] : this.reason.toArray(new Base[this.reason.size()]); // CodeableConcept
        case 1511509392: /*responsibleParty*/ return this.responsibleParty == null ? new Base[0] : this.responsibleParty.toArray(new Base[this.responsibleParty.size()]); // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          break;
        case -934964668: // reason
          this.getReason().add(castToCodeableConcept(value)); // CodeableConcept
          break;
        case 1511509392: // responsibleParty
          this.getResponsibleParty().add(castToReference(value)); // Reference
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type"))
          this.type = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("reason"))
          this.getReason().add(castToCodeableConcept(value));
        else if (name.equals("responsibleParty"))
          this.getResponsibleParty().add(castToReference(value));
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610:  return getType(); // CodeableConcept
        case -934964668:  return addReason(); // CodeableConcept
        case 1511509392:  return addResponsibleParty(); // Reference
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("reason")) {
          return addReason();
        }
        else if (name.equals("responsibleParty")) {
          return addResponsibleParty();
        }
        else
          return super.addChild(name);
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

  public String fhirType() {
    return "MedicationDispense.substitution";

  }

  }

    /**
     * Identifier assigned by the dispensing facility - this is an identifier assigned outside FHIR.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="External identifier", formalDefinition="Identifier assigned by the dispensing facility - this is an identifier assigned outside FHIR." )
    protected Identifier identifier;

    /**
     * A code specifying the state of the set of dispense events.
     */
    @Child(name = "status", type = {CodeType.class}, order=1, min=0, max=1, modifier=true, summary=true)
    @Description(shortDefinition="in-progress | on-hold | completed | entered-in-error | stopped", formalDefinition="A code specifying the state of the set of dispense events." )
    protected Enumeration<MedicationDispenseStatus> status;

    /**
     * Identifies the medication being administered. This is either a link to a resource representing the details of the medication or a simple attribute carrying a code that identifies the medication from a known list of medications.
     */
    @Child(name = "medication", type = {CodeableConcept.class, Medication.class}, order=2, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="What medication was supplied", formalDefinition="Identifies the medication being administered. This is either a link to a resource representing the details of the medication or a simple attribute carrying a code that identifies the medication from a known list of medications." )
    protected Type medication;

    /**
     * A link to a resource representing the person to whom the medication will be given.
     */
    @Child(name = "patient", type = {Patient.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Who the dispense is for", formalDefinition="A link to a resource representing the person to whom the medication will be given." )
    protected Reference patient;

    /**
     * The actual object that is the target of the reference (A link to a resource representing the person to whom the medication will be given.)
     */
    protected Patient patientTarget;

    /**
     * The individual responsible for dispensing the medication.
     */
    @Child(name = "dispenser", type = {Practitioner.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Practitioner responsible for dispensing medication", formalDefinition="The individual responsible for dispensing the medication." )
    protected Reference dispenser;

    /**
     * The actual object that is the target of the reference (The individual responsible for dispensing the medication.)
     */
    protected Practitioner dispenserTarget;

    /**
     * Indicates the medication order that is being dispensed against.
     */
    @Child(name = "authorizingPrescription", type = {MedicationOrder.class}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Medication order that authorizes the dispense", formalDefinition="Indicates the medication order that is being dispensed against." )
    protected List<Reference> authorizingPrescription;
    /**
     * The actual objects that are the target of the reference (Indicates the medication order that is being dispensed against.)
     */
    protected List<MedicationOrder> authorizingPrescriptionTarget;


    /**
     * Indicates the type of dispensing event that is performed. For example, Trial Fill, Completion of Trial, Partial Fill, Emergency Fill, Samples, etc.
     */
    @Child(name = "type", type = {CodeableConcept.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Trial fill, partial fill, emergency fill, etc.", formalDefinition="Indicates the type of dispensing event that is performed. For example, Trial Fill, Completion of Trial, Partial Fill, Emergency Fill, Samples, etc." )
    protected CodeableConcept type;

    /**
     * The amount of medication that has been dispensed. Includes unit of measure.
     */
    @Child(name = "quantity", type = {SimpleQuantity.class}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Amount dispensed", formalDefinition="The amount of medication that has been dispensed. Includes unit of measure." )
    protected SimpleQuantity quantity;

    /**
     * The amount of medication expressed as a timing amount.
     */
    @Child(name = "daysSupply", type = {SimpleQuantity.class}, order=8, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Amount of medication expressed as a timing amount", formalDefinition="The amount of medication expressed as a timing amount." )
    protected SimpleQuantity daysSupply;

    /**
     * The time when the dispensed product was packaged and reviewed.
     */
    @Child(name = "whenPrepared", type = {DateTimeType.class}, order=9, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Dispense processing time", formalDefinition="The time when the dispensed product was packaged and reviewed." )
    protected DateTimeType whenPrepared;

    /**
     * The time the dispensed product was provided to the patient or their representative.
     */
    @Child(name = "whenHandedOver", type = {DateTimeType.class}, order=10, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When product was given out", formalDefinition="The time the dispensed product was provided to the patient or their representative." )
    protected DateTimeType whenHandedOver;

    /**
     * Identification of the facility/location where the medication was shipped to, as part of the dispense event.
     */
    @Child(name = "destination", type = {Location.class}, order=11, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Where the medication was sent", formalDefinition="Identification of the facility/location where the medication was shipped to, as part of the dispense event." )
    protected Reference destination;

    /**
     * The actual object that is the target of the reference (Identification of the facility/location where the medication was shipped to, as part of the dispense event.)
     */
    protected Location destinationTarget;

    /**
     * Identifies the person who picked up the medication.  This will usually be a patient or their caregiver, but some cases exist where it can be a healthcare professional.
     */
    @Child(name = "receiver", type = {Patient.class, Practitioner.class}, order=12, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Who collected the medication", formalDefinition="Identifies the person who picked up the medication.  This will usually be a patient or their caregiver, but some cases exist where it can be a healthcare professional." )
    protected List<Reference> receiver;
    /**
     * The actual objects that are the target of the reference (Identifies the person who picked up the medication.  This will usually be a patient or their caregiver, but some cases exist where it can be a healthcare professional.)
     */
    protected List<Resource> receiverTarget;


    /**
     * Extra information about the dispense that could not be conveyed in the other attributes.
     */
    @Child(name = "note", type = {Annotation.class}, order=13, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Information about the dispense", formalDefinition="Extra information about the dispense that could not be conveyed in the other attributes." )
    protected List<Annotation> note;

    /**
     * Indicates how the medication is to be used by the patient.  The pharmacist reviews the medication order prior to dispense and updates the dosageInstruction based on the actual product being dispensed.
     */
    @Child(name = "dosageInstruction", type = {}, order=14, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Medicine administration instructions to the patient/caregiver", formalDefinition="Indicates how the medication is to be used by the patient.  The pharmacist reviews the medication order prior to dispense and updates the dosageInstruction based on the actual product being dispensed." )
    protected List<MedicationDispenseDosageInstructionComponent> dosageInstruction;

    /**
     * Indicates whether or not substitution was made as part of the dispense.  In some cases substitution will be expected but does not happen, in other cases substitution is not expected but does happen.  This block explains what substitution did or did not happen and why.
     */
    @Child(name = "substitution", type = {}, order=15, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Deals with substitution of one medicine for another", formalDefinition="Indicates whether or not substitution was made as part of the dispense.  In some cases substitution will be expected but does not happen, in other cases substitution is not expected but does happen.  This block explains what substitution did or did not happen and why." )
    protected MedicationDispenseSubstitutionComponent substitution;

    private static final long serialVersionUID = -634238241L;

  /**
   * Constructor
   */
    public MedicationDispense() {
      super();
    }

  /**
   * Constructor
   */
    public MedicationDispense(Type medication) {
      super();
      this.medication = medication;
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
     * @return {@link #medication} (Identifies the medication being administered. This is either a link to a resource representing the details of the medication or a simple attribute carrying a code that identifies the medication from a known list of medications.)
     */
    public Type getMedication() { 
      return this.medication;
    }

    /**
     * @return {@link #medication} (Identifies the medication being administered. This is either a link to a resource representing the details of the medication or a simple attribute carrying a code that identifies the medication from a known list of medications.)
     */
    public CodeableConcept getMedicationCodeableConcept() throws FHIRException { 
      if (!(this.medication instanceof CodeableConcept))
        throw new FHIRException("Type mismatch: the type CodeableConcept was expected, but "+this.medication.getClass().getName()+" was encountered");
      return (CodeableConcept) this.medication;
    }

    public boolean hasMedicationCodeableConcept() { 
      return this.medication instanceof CodeableConcept;
    }

    /**
     * @return {@link #medication} (Identifies the medication being administered. This is either a link to a resource representing the details of the medication or a simple attribute carrying a code that identifies the medication from a known list of medications.)
     */
    public Reference getMedicationReference() throws FHIRException { 
      if (!(this.medication instanceof Reference))
        throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.medication.getClass().getName()+" was encountered");
      return (Reference) this.medication;
    }

    public boolean hasMedicationReference() { 
      return this.medication instanceof Reference;
    }

    public boolean hasMedication() { 
      return this.medication != null && !this.medication.isEmpty();
    }

    /**
     * @param value {@link #medication} (Identifies the medication being administered. This is either a link to a resource representing the details of the medication or a simple attribute carrying a code that identifies the medication from a known list of medications.)
     */
    public MedicationDispense setMedication(Type value) { 
      this.medication = value;
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

    // syntactic sugar
    public MedicationDispense addAuthorizingPrescription(Reference t) { //3
      if (t == null)
        return this;
      if (this.authorizingPrescription == null)
        this.authorizingPrescription = new ArrayList<Reference>();
      this.authorizingPrescription.add(t);
      return this;
    }

    /**
     * @return {@link #authorizingPrescription} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. Indicates the medication order that is being dispensed against.)
     */
    public List<MedicationOrder> getAuthorizingPrescriptionTarget() { 
      if (this.authorizingPrescriptionTarget == null)
        this.authorizingPrescriptionTarget = new ArrayList<MedicationOrder>();
      return this.authorizingPrescriptionTarget;
    }

    // syntactic sugar
    /**
     * @return {@link #authorizingPrescription} (Add an actual object that is the target of the reference. The reference library doesn't use these, but you can use this to hold the resources if you resolvethemt. Indicates the medication order that is being dispensed against.)
     */
    public MedicationOrder addAuthorizingPrescriptionTarget() { 
      MedicationOrder r = new MedicationOrder();
      if (this.authorizingPrescriptionTarget == null)
        this.authorizingPrescriptionTarget = new ArrayList<MedicationOrder>();
      this.authorizingPrescriptionTarget.add(r);
      return r;
    }

    /**
     * @return {@link #type} (Indicates the type of dispensing event that is performed. For example, Trial Fill, Completion of Trial, Partial Fill, Emergency Fill, Samples, etc.)
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
     * @param value {@link #type} (Indicates the type of dispensing event that is performed. For example, Trial Fill, Completion of Trial, Partial Fill, Emergency Fill, Samples, etc.)
     */
    public MedicationDispense setType(CodeableConcept value) { 
      this.type = value;
      return this;
    }

    /**
     * @return {@link #quantity} (The amount of medication that has been dispensed. Includes unit of measure.)
     */
    public SimpleQuantity getQuantity() { 
      if (this.quantity == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationDispense.quantity");
        else if (Configuration.doAutoCreate())
          this.quantity = new SimpleQuantity(); // cc
      return this.quantity;
    }

    public boolean hasQuantity() { 
      return this.quantity != null && !this.quantity.isEmpty();
    }

    /**
     * @param value {@link #quantity} (The amount of medication that has been dispensed. Includes unit of measure.)
     */
    public MedicationDispense setQuantity(SimpleQuantity value) { 
      this.quantity = value;
      return this;
    }

    /**
     * @return {@link #daysSupply} (The amount of medication expressed as a timing amount.)
     */
    public SimpleQuantity getDaysSupply() { 
      if (this.daysSupply == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationDispense.daysSupply");
        else if (Configuration.doAutoCreate())
          this.daysSupply = new SimpleQuantity(); // cc
      return this.daysSupply;
    }

    public boolean hasDaysSupply() { 
      return this.daysSupply != null && !this.daysSupply.isEmpty();
    }

    /**
     * @param value {@link #daysSupply} (The amount of medication expressed as a timing amount.)
     */
    public MedicationDispense setDaysSupply(SimpleQuantity value) { 
      this.daysSupply = value;
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
     * @return {@link #receiver} (Identifies the person who picked up the medication.  This will usually be a patient or their caregiver, but some cases exist where it can be a healthcare professional.)
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
     * @return {@link #receiver} (Identifies the person who picked up the medication.  This will usually be a patient or their caregiver, but some cases exist where it can be a healthcare professional.)
     */
    // syntactic sugar
    public Reference addReceiver() { //3
      Reference t = new Reference();
      if (this.receiver == null)
        this.receiver = new ArrayList<Reference>();
      this.receiver.add(t);
      return t;
    }

    // syntactic sugar
    public MedicationDispense addReceiver(Reference t) { //3
      if (t == null)
        return this;
      if (this.receiver == null)
        this.receiver = new ArrayList<Reference>();
      this.receiver.add(t);
      return this;
    }

    /**
     * @return {@link #receiver} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. Identifies the person who picked up the medication.  This will usually be a patient or their caregiver, but some cases exist where it can be a healthcare professional.)
     */
    public List<Resource> getReceiverTarget() { 
      if (this.receiverTarget == null)
        this.receiverTarget = new ArrayList<Resource>();
      return this.receiverTarget;
    }

    /**
     * @return {@link #note} (Extra information about the dispense that could not be conveyed in the other attributes.)
     */
    public List<Annotation> getNote() { 
      if (this.note == null)
        this.note = new ArrayList<Annotation>();
      return this.note;
    }

    public boolean hasNote() { 
      if (this.note == null)
        return false;
      for (Annotation item : this.note)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #note} (Extra information about the dispense that could not be conveyed in the other attributes.)
     */
    // syntactic sugar
    public Annotation addNote() { //3
      Annotation t = new Annotation();
      if (this.note == null)
        this.note = new ArrayList<Annotation>();
      this.note.add(t);
      return t;
    }

    // syntactic sugar
    public MedicationDispense addNote(Annotation t) { //3
      if (t == null)
        return this;
      if (this.note == null)
        this.note = new ArrayList<Annotation>();
      this.note.add(t);
      return this;
    }

    /**
     * @return {@link #dosageInstruction} (Indicates how the medication is to be used by the patient.  The pharmacist reviews the medication order prior to dispense and updates the dosageInstruction based on the actual product being dispensed.)
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
     * @return {@link #dosageInstruction} (Indicates how the medication is to be used by the patient.  The pharmacist reviews the medication order prior to dispense and updates the dosageInstruction based on the actual product being dispensed.)
     */
    // syntactic sugar
    public MedicationDispenseDosageInstructionComponent addDosageInstruction() { //3
      MedicationDispenseDosageInstructionComponent t = new MedicationDispenseDosageInstructionComponent();
      if (this.dosageInstruction == null)
        this.dosageInstruction = new ArrayList<MedicationDispenseDosageInstructionComponent>();
      this.dosageInstruction.add(t);
      return t;
    }

    // syntactic sugar
    public MedicationDispense addDosageInstruction(MedicationDispenseDosageInstructionComponent t) { //3
      if (t == null)
        return this;
      if (this.dosageInstruction == null)
        this.dosageInstruction = new ArrayList<MedicationDispenseDosageInstructionComponent>();
      this.dosageInstruction.add(t);
      return this;
    }

    /**
     * @return {@link #substitution} (Indicates whether or not substitution was made as part of the dispense.  In some cases substitution will be expected but does not happen, in other cases substitution is not expected but does happen.  This block explains what substitution did or did not happen and why.)
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
     * @param value {@link #substitution} (Indicates whether or not substitution was made as part of the dispense.  In some cases substitution will be expected but does not happen, in other cases substitution is not expected but does happen.  This block explains what substitution did or did not happen and why.)
     */
    public MedicationDispense setSubstitution(MedicationDispenseSubstitutionComponent value) { 
      this.substitution = value;
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "Identifier assigned by the dispensing facility - this is an identifier assigned outside FHIR.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("status", "code", "A code specifying the state of the set of dispense events.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("medication[x]", "CodeableConcept|Reference(Medication)", "Identifies the medication being administered. This is either a link to a resource representing the details of the medication or a simple attribute carrying a code that identifies the medication from a known list of medications.", 0, java.lang.Integer.MAX_VALUE, medication));
        childrenList.add(new Property("patient", "Reference(Patient)", "A link to a resource representing the person to whom the medication will be given.", 0, java.lang.Integer.MAX_VALUE, patient));
        childrenList.add(new Property("dispenser", "Reference(Practitioner)", "The individual responsible for dispensing the medication.", 0, java.lang.Integer.MAX_VALUE, dispenser));
        childrenList.add(new Property("authorizingPrescription", "Reference(MedicationOrder)", "Indicates the medication order that is being dispensed against.", 0, java.lang.Integer.MAX_VALUE, authorizingPrescription));
        childrenList.add(new Property("type", "CodeableConcept", "Indicates the type of dispensing event that is performed. For example, Trial Fill, Completion of Trial, Partial Fill, Emergency Fill, Samples, etc.", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("quantity", "SimpleQuantity", "The amount of medication that has been dispensed. Includes unit of measure.", 0, java.lang.Integer.MAX_VALUE, quantity));
        childrenList.add(new Property("daysSupply", "SimpleQuantity", "The amount of medication expressed as a timing amount.", 0, java.lang.Integer.MAX_VALUE, daysSupply));
        childrenList.add(new Property("whenPrepared", "dateTime", "The time when the dispensed product was packaged and reviewed.", 0, java.lang.Integer.MAX_VALUE, whenPrepared));
        childrenList.add(new Property("whenHandedOver", "dateTime", "The time the dispensed product was provided to the patient or their representative.", 0, java.lang.Integer.MAX_VALUE, whenHandedOver));
        childrenList.add(new Property("destination", "Reference(Location)", "Identification of the facility/location where the medication was shipped to, as part of the dispense event.", 0, java.lang.Integer.MAX_VALUE, destination));
        childrenList.add(new Property("receiver", "Reference(Patient|Practitioner)", "Identifies the person who picked up the medication.  This will usually be a patient or their caregiver, but some cases exist where it can be a healthcare professional.", 0, java.lang.Integer.MAX_VALUE, receiver));
        childrenList.add(new Property("note", "Annotation", "Extra information about the dispense that could not be conveyed in the other attributes.", 0, java.lang.Integer.MAX_VALUE, note));
        childrenList.add(new Property("dosageInstruction", "", "Indicates how the medication is to be used by the patient.  The pharmacist reviews the medication order prior to dispense and updates the dosageInstruction based on the actual product being dispensed.", 0, java.lang.Integer.MAX_VALUE, dosageInstruction));
        childrenList.add(new Property("substitution", "", "Indicates whether or not substitution was made as part of the dispense.  In some cases substitution will be expected but does not happen, in other cases substitution is not expected but does happen.  This block explains what substitution did or did not happen and why.", 0, java.lang.Integer.MAX_VALUE, substitution));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : new Base[] {this.identifier}; // Identifier
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<MedicationDispenseStatus>
        case 1998965455: /*medication*/ return this.medication == null ? new Base[0] : new Base[] {this.medication}; // Type
        case -791418107: /*patient*/ return this.patient == null ? new Base[0] : new Base[] {this.patient}; // Reference
        case 241511093: /*dispenser*/ return this.dispenser == null ? new Base[0] : new Base[] {this.dispenser}; // Reference
        case -1237557856: /*authorizingPrescription*/ return this.authorizingPrescription == null ? new Base[0] : this.authorizingPrescription.toArray(new Base[this.authorizingPrescription.size()]); // Reference
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case -1285004149: /*quantity*/ return this.quantity == null ? new Base[0] : new Base[] {this.quantity}; // SimpleQuantity
        case 197175334: /*daysSupply*/ return this.daysSupply == null ? new Base[0] : new Base[] {this.daysSupply}; // SimpleQuantity
        case -562837097: /*whenPrepared*/ return this.whenPrepared == null ? new Base[0] : new Base[] {this.whenPrepared}; // DateTimeType
        case -940241380: /*whenHandedOver*/ return this.whenHandedOver == null ? new Base[0] : new Base[] {this.whenHandedOver}; // DateTimeType
        case -1429847026: /*destination*/ return this.destination == null ? new Base[0] : new Base[] {this.destination}; // Reference
        case -808719889: /*receiver*/ return this.receiver == null ? new Base[0] : this.receiver.toArray(new Base[this.receiver.size()]); // Reference
        case 3387378: /*note*/ return this.note == null ? new Base[0] : this.note.toArray(new Base[this.note.size()]); // Annotation
        case -1201373865: /*dosageInstruction*/ return this.dosageInstruction == null ? new Base[0] : this.dosageInstruction.toArray(new Base[this.dosageInstruction.size()]); // MedicationDispenseDosageInstructionComponent
        case 826147581: /*substitution*/ return this.substitution == null ? new Base[0] : new Base[] {this.substitution}; // MedicationDispenseSubstitutionComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.identifier = castToIdentifier(value); // Identifier
          break;
        case -892481550: // status
          this.status = new MedicationDispenseStatusEnumFactory().fromType(value); // Enumeration<MedicationDispenseStatus>
          break;
        case 1998965455: // medication
          this.medication = (Type) value; // Type
          break;
        case -791418107: // patient
          this.patient = castToReference(value); // Reference
          break;
        case 241511093: // dispenser
          this.dispenser = castToReference(value); // Reference
          break;
        case -1237557856: // authorizingPrescription
          this.getAuthorizingPrescription().add(castToReference(value)); // Reference
          break;
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          break;
        case -1285004149: // quantity
          this.quantity = castToSimpleQuantity(value); // SimpleQuantity
          break;
        case 197175334: // daysSupply
          this.daysSupply = castToSimpleQuantity(value); // SimpleQuantity
          break;
        case -562837097: // whenPrepared
          this.whenPrepared = castToDateTime(value); // DateTimeType
          break;
        case -940241380: // whenHandedOver
          this.whenHandedOver = castToDateTime(value); // DateTimeType
          break;
        case -1429847026: // destination
          this.destination = castToReference(value); // Reference
          break;
        case -808719889: // receiver
          this.getReceiver().add(castToReference(value)); // Reference
          break;
        case 3387378: // note
          this.getNote().add(castToAnnotation(value)); // Annotation
          break;
        case -1201373865: // dosageInstruction
          this.getDosageInstruction().add((MedicationDispenseDosageInstructionComponent) value); // MedicationDispenseDosageInstructionComponent
          break;
        case 826147581: // substitution
          this.substitution = (MedicationDispenseSubstitutionComponent) value; // MedicationDispenseSubstitutionComponent
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier"))
          this.identifier = castToIdentifier(value); // Identifier
        else if (name.equals("status"))
          this.status = new MedicationDispenseStatusEnumFactory().fromType(value); // Enumeration<MedicationDispenseStatus>
        else if (name.equals("medication[x]"))
          this.medication = (Type) value; // Type
        else if (name.equals("patient"))
          this.patient = castToReference(value); // Reference
        else if (name.equals("dispenser"))
          this.dispenser = castToReference(value); // Reference
        else if (name.equals("authorizingPrescription"))
          this.getAuthorizingPrescription().add(castToReference(value));
        else if (name.equals("type"))
          this.type = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("quantity"))
          this.quantity = castToSimpleQuantity(value); // SimpleQuantity
        else if (name.equals("daysSupply"))
          this.daysSupply = castToSimpleQuantity(value); // SimpleQuantity
        else if (name.equals("whenPrepared"))
          this.whenPrepared = castToDateTime(value); // DateTimeType
        else if (name.equals("whenHandedOver"))
          this.whenHandedOver = castToDateTime(value); // DateTimeType
        else if (name.equals("destination"))
          this.destination = castToReference(value); // Reference
        else if (name.equals("receiver"))
          this.getReceiver().add(castToReference(value));
        else if (name.equals("note"))
          this.getNote().add(castToAnnotation(value));
        else if (name.equals("dosageInstruction"))
          this.getDosageInstruction().add((MedicationDispenseDosageInstructionComponent) value);
        else if (name.equals("substitution"))
          this.substitution = (MedicationDispenseSubstitutionComponent) value; // MedicationDispenseSubstitutionComponent
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return getIdentifier(); // Identifier
        case -892481550: throw new FHIRException("Cannot make property status as it is not a complex type"); // Enumeration<MedicationDispenseStatus>
        case 1458402129:  return getMedication(); // Type
        case -791418107:  return getPatient(); // Reference
        case 241511093:  return getDispenser(); // Reference
        case -1237557856:  return addAuthorizingPrescription(); // Reference
        case 3575610:  return getType(); // CodeableConcept
        case -1285004149:  return getQuantity(); // SimpleQuantity
        case 197175334:  return getDaysSupply(); // SimpleQuantity
        case -562837097: throw new FHIRException("Cannot make property whenPrepared as it is not a complex type"); // DateTimeType
        case -940241380: throw new FHIRException("Cannot make property whenHandedOver as it is not a complex type"); // DateTimeType
        case -1429847026:  return getDestination(); // Reference
        case -808719889:  return addReceiver(); // Reference
        case 3387378:  return addNote(); // Annotation
        case -1201373865:  return addDosageInstruction(); // MedicationDispenseDosageInstructionComponent
        case 826147581:  return getSubstitution(); // MedicationDispenseSubstitutionComponent
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          this.identifier = new Identifier();
          return this.identifier;
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type MedicationDispense.status");
        }
        else if (name.equals("medicationCodeableConcept")) {
          this.medication = new CodeableConcept();
          return this.medication;
        }
        else if (name.equals("medicationReference")) {
          this.medication = new Reference();
          return this.medication;
        }
        else if (name.equals("patient")) {
          this.patient = new Reference();
          return this.patient;
        }
        else if (name.equals("dispenser")) {
          this.dispenser = new Reference();
          return this.dispenser;
        }
        else if (name.equals("authorizingPrescription")) {
          return addAuthorizingPrescription();
        }
        else if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("quantity")) {
          this.quantity = new SimpleQuantity();
          return this.quantity;
        }
        else if (name.equals("daysSupply")) {
          this.daysSupply = new SimpleQuantity();
          return this.daysSupply;
        }
        else if (name.equals("whenPrepared")) {
          throw new FHIRException("Cannot call addChild on a primitive type MedicationDispense.whenPrepared");
        }
        else if (name.equals("whenHandedOver")) {
          throw new FHIRException("Cannot call addChild on a primitive type MedicationDispense.whenHandedOver");
        }
        else if (name.equals("destination")) {
          this.destination = new Reference();
          return this.destination;
        }
        else if (name.equals("receiver")) {
          return addReceiver();
        }
        else if (name.equals("note")) {
          return addNote();
        }
        else if (name.equals("dosageInstruction")) {
          return addDosageInstruction();
        }
        else if (name.equals("substitution")) {
          this.substitution = new MedicationDispenseSubstitutionComponent();
          return this.substitution;
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "MedicationDispense";

  }

      public MedicationDispense copy() {
        MedicationDispense dst = new MedicationDispense();
        copyValues(dst);
        dst.identifier = identifier == null ? null : identifier.copy();
        dst.status = status == null ? null : status.copy();
        dst.medication = medication == null ? null : medication.copy();
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
        dst.whenPrepared = whenPrepared == null ? null : whenPrepared.copy();
        dst.whenHandedOver = whenHandedOver == null ? null : whenHandedOver.copy();
        dst.destination = destination == null ? null : destination.copy();
        if (receiver != null) {
          dst.receiver = new ArrayList<Reference>();
          for (Reference i : receiver)
            dst.receiver.add(i.copy());
        };
        if (note != null) {
          dst.note = new ArrayList<Annotation>();
          for (Annotation i : note)
            dst.note.add(i.copy());
        };
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
        return compareDeep(identifier, o.identifier, true) && compareDeep(status, o.status, true) && compareDeep(medication, o.medication, true)
           && compareDeep(patient, o.patient, true) && compareDeep(dispenser, o.dispenser, true) && compareDeep(authorizingPrescription, o.authorizingPrescription, true)
           && compareDeep(type, o.type, true) && compareDeep(quantity, o.quantity, true) && compareDeep(daysSupply, o.daysSupply, true)
           && compareDeep(whenPrepared, o.whenPrepared, true) && compareDeep(whenHandedOver, o.whenHandedOver, true)
           && compareDeep(destination, o.destination, true) && compareDeep(receiver, o.receiver, true) && compareDeep(note, o.note, true)
           && compareDeep(dosageInstruction, o.dosageInstruction, true) && compareDeep(substitution, o.substitution, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof MedicationDispense))
          return false;
        MedicationDispense o = (MedicationDispense) other;
        return compareValues(status, o.status, true) && compareValues(whenPrepared, o.whenPrepared, true) && compareValues(whenHandedOver, o.whenHandedOver, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (status == null || status.isEmpty())
           && (medication == null || medication.isEmpty()) && (patient == null || patient.isEmpty())
           && (dispenser == null || dispenser.isEmpty()) && (authorizingPrescription == null || authorizingPrescription.isEmpty())
           && (type == null || type.isEmpty()) && (quantity == null || quantity.isEmpty()) && (daysSupply == null || daysSupply.isEmpty())
           && (whenPrepared == null || whenPrepared.isEmpty()) && (whenHandedOver == null || whenHandedOver.isEmpty())
           && (destination == null || destination.isEmpty()) && (receiver == null || receiver.isEmpty())
           && (note == null || note.isEmpty()) && (dosageInstruction == null || dosageInstruction.isEmpty())
           && (substitution == null || substitution.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.MedicationDispense;
   }

 /**
   * Search parameter: <b>medication</b>
   * <p>
   * Description: <b>Return dispenses of this medicine resource</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MedicationDispense.medicationReference</b><br>
   * </p>
   */
  @SearchParamDefinition(name="medication", path="MedicationDispense.medication.as(Reference)", description="Return dispenses of this medicine resource", type="reference" )
  public static final String SP_MEDICATION = "medication";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>medication</b>
   * <p>
   * Description: <b>Return dispenses of this medicine resource</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MedicationDispense.medicationReference</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam MEDICATION = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_MEDICATION);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>MedicationDispense:medication</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_MEDICATION = new ca.uhn.fhir.model.api.Include("MedicationDispense:medication").toLocked();

 /**
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>The identity of a patient to list dispenses  for</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MedicationDispense.patient</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="MedicationDispense.patient", description="The identity of a patient to list dispenses  for", type="reference" )
  public static final String SP_PATIENT = "patient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patient</b>
   * <p>
   * Description: <b>The identity of a patient to list dispenses  for</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MedicationDispense.patient</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PATIENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PATIENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>MedicationDispense:patient</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PATIENT = new ca.uhn.fhir.model.api.Include("MedicationDispense:patient").toLocked();

 /**
   * Search parameter: <b>receiver</b>
   * <p>
   * Description: <b>Who collected the medication</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MedicationDispense.receiver</b><br>
   * </p>
   */
  @SearchParamDefinition(name="receiver", path="MedicationDispense.receiver", description="Who collected the medication", type="reference" )
  public static final String SP_RECEIVER = "receiver";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>receiver</b>
   * <p>
   * Description: <b>Who collected the medication</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MedicationDispense.receiver</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam RECEIVER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_RECEIVER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>MedicationDispense:receiver</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_RECEIVER = new ca.uhn.fhir.model.api.Include("MedicationDispense:receiver").toLocked();

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>Status of the dispense</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MedicationDispense.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="MedicationDispense.status", description="Status of the dispense", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>Status of the dispense</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MedicationDispense.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);

 /**
   * Search parameter: <b>prescription</b>
   * <p>
   * Description: <b>The identity of a prescription to list dispenses from</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MedicationDispense.authorizingPrescription</b><br>
   * </p>
   */
  @SearchParamDefinition(name="prescription", path="MedicationDispense.authorizingPrescription", description="The identity of a prescription to list dispenses from", type="reference" )
  public static final String SP_PRESCRIPTION = "prescription";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>prescription</b>
   * <p>
   * Description: <b>The identity of a prescription to list dispenses from</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MedicationDispense.authorizingPrescription</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PRESCRIPTION = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PRESCRIPTION);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>MedicationDispense:prescription</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PRESCRIPTION = new ca.uhn.fhir.model.api.Include("MedicationDispense:prescription").toLocked();

 /**
   * Search parameter: <b>responsibleparty</b>
   * <p>
   * Description: <b>Return all dispenses with the specified responsible party</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MedicationDispense.substitution.responsibleParty</b><br>
   * </p>
   */
  @SearchParamDefinition(name="responsibleparty", path="MedicationDispense.substitution.responsibleParty", description="Return all dispenses with the specified responsible party", type="reference" )
  public static final String SP_RESPONSIBLEPARTY = "responsibleparty";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>responsibleparty</b>
   * <p>
   * Description: <b>Return all dispenses with the specified responsible party</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MedicationDispense.substitution.responsibleParty</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam RESPONSIBLEPARTY = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_RESPONSIBLEPARTY);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>MedicationDispense:responsibleparty</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_RESPONSIBLEPARTY = new ca.uhn.fhir.model.api.Include("MedicationDispense:responsibleparty").toLocked();

 /**
   * Search parameter: <b>dispenser</b>
   * <p>
   * Description: <b>Return all dispenses performed by a specific individual</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MedicationDispense.dispenser</b><br>
   * </p>
   */
  @SearchParamDefinition(name="dispenser", path="MedicationDispense.dispenser", description="Return all dispenses performed by a specific individual", type="reference" )
  public static final String SP_DISPENSER = "dispenser";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>dispenser</b>
   * <p>
   * Description: <b>Return all dispenses performed by a specific individual</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MedicationDispense.dispenser</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam DISPENSER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_DISPENSER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>MedicationDispense:dispenser</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_DISPENSER = new ca.uhn.fhir.model.api.Include("MedicationDispense:dispenser").toLocked();

 /**
   * Search parameter: <b>code</b>
   * <p>
   * Description: <b>Return dispenses of this medicine code</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MedicationDispense.medicationCodeableConcept</b><br>
   * </p>
   */
  @SearchParamDefinition(name="code", path="MedicationDispense.medication.as(CodeableConcept)", description="Return dispenses of this medicine code", type="token" )
  public static final String SP_CODE = "code";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>code</b>
   * <p>
   * Description: <b>Return dispenses of this medicine code</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MedicationDispense.medicationCodeableConcept</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CODE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CODE);

 /**
   * Search parameter: <b>type</b>
   * <p>
   * Description: <b>Return all dispenses of a specific type</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MedicationDispense.type</b><br>
   * </p>
   */
  @SearchParamDefinition(name="type", path="MedicationDispense.type", description="Return all dispenses of a specific type", type="token" )
  public static final String SP_TYPE = "type";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>type</b>
   * <p>
   * Description: <b>Return all dispenses of a specific type</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MedicationDispense.type</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam TYPE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_TYPE);

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>Return dispenses with this external identifier</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MedicationDispense.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="MedicationDispense.identifier", description="Return dispenses with this external identifier", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>Return dispenses with this external identifier</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MedicationDispense.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>whenprepared</b>
   * <p>
   * Description: <b>Date when medication prepared</b><br>
   * Type: <b>date</b><br>
   * Path: <b>MedicationDispense.whenPrepared</b><br>
   * </p>
   */
  @SearchParamDefinition(name="whenprepared", path="MedicationDispense.whenPrepared", description="Date when medication prepared", type="date" )
  public static final String SP_WHENPREPARED = "whenprepared";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>whenprepared</b>
   * <p>
   * Description: <b>Date when medication prepared</b><br>
   * Type: <b>date</b><br>
   * Path: <b>MedicationDispense.whenPrepared</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam WHENPREPARED = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_WHENPREPARED);

 /**
   * Search parameter: <b>whenhandedover</b>
   * <p>
   * Description: <b>Date when medication handed over to patient (outpatient setting), or supplied to ward or clinic (inpatient setting)</b><br>
   * Type: <b>date</b><br>
   * Path: <b>MedicationDispense.whenHandedOver</b><br>
   * </p>
   */
  @SearchParamDefinition(name="whenhandedover", path="MedicationDispense.whenHandedOver", description="Date when medication handed over to patient (outpatient setting), or supplied to ward or clinic (inpatient setting)", type="date" )
  public static final String SP_WHENHANDEDOVER = "whenhandedover";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>whenhandedover</b>
   * <p>
   * Description: <b>Date when medication handed over to patient (outpatient setting), or supplied to ward or clinic (inpatient setting)</b><br>
   * Type: <b>date</b><br>
   * Path: <b>MedicationDispense.whenHandedOver</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam WHENHANDEDOVER = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_WHENHANDEDOVER);

 /**
   * Search parameter: <b>destination</b>
   * <p>
   * Description: <b>Return dispenses that should be sent to a specific destination</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MedicationDispense.destination</b><br>
   * </p>
   */
  @SearchParamDefinition(name="destination", path="MedicationDispense.destination", description="Return dispenses that should be sent to a specific destination", type="reference" )
  public static final String SP_DESTINATION = "destination";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>destination</b>
   * <p>
   * Description: <b>Return dispenses that should be sent to a specific destination</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MedicationDispense.destination</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam DESTINATION = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_DESTINATION);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>MedicationDispense:destination</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_DESTINATION = new ca.uhn.fhir.model.api.Include("MedicationDispense:destination").toLocked();


}

