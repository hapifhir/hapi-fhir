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
 * An order for both supply of the medication and the instructions for administration of the medication to a patient. The resource is called "MedicationOrder" rather than "MedicationPrescription" to generalize the use across inpatient and outpatient settings as well as for care plans, etc.
 */
@ResourceDef(name="MedicationOrder", profile="http://hl7.org/fhir/Profile/MedicationOrder")
public class MedicationOrder extends DomainResource {

    public enum MedicationOrderStatus {
        /**
         * The prescription is 'actionable', but not all actions that are implied by it have occurred yet.
         */
        ACTIVE, 
        /**
         * Actions implied by the prescription are to be temporarily halted, but are expected to continue later.  May also be called "suspended".
         */
        ONHOLD, 
        /**
         * All actions that are implied by the prescription have occurred.
         */
        COMPLETED, 
        /**
         * The prescription was entered in error.
         */
        ENTEREDINERROR, 
        /**
         * Actions implied by the prescription are to be permanently halted, before all of them occurred.
         */
        STOPPED, 
        /**
         * The prescription is not yet 'actionable', i.e. it is a work in progress, requires sign-off or verification, and needs to be run through decision support process.
         */
        DRAFT, 
        /**
         * added to help the parsers
         */
        NULL;
        public static MedicationOrderStatus fromCode(String codeString) throws FHIRException {
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
        if ("draft".equals(codeString))
          return DRAFT;
        throw new FHIRException("Unknown MedicationOrderStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ACTIVE: return "active";
            case ONHOLD: return "on-hold";
            case COMPLETED: return "completed";
            case ENTEREDINERROR: return "entered-in-error";
            case STOPPED: return "stopped";
            case DRAFT: return "draft";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case ACTIVE: return "http://hl7.org/fhir/medication-order-status";
            case ONHOLD: return "http://hl7.org/fhir/medication-order-status";
            case COMPLETED: return "http://hl7.org/fhir/medication-order-status";
            case ENTEREDINERROR: return "http://hl7.org/fhir/medication-order-status";
            case STOPPED: return "http://hl7.org/fhir/medication-order-status";
            case DRAFT: return "http://hl7.org/fhir/medication-order-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case ACTIVE: return "The prescription is 'actionable', but not all actions that are implied by it have occurred yet.";
            case ONHOLD: return "Actions implied by the prescription are to be temporarily halted, but are expected to continue later.  May also be called \"suspended\".";
            case COMPLETED: return "All actions that are implied by the prescription have occurred.";
            case ENTEREDINERROR: return "The prescription was entered in error.";
            case STOPPED: return "Actions implied by the prescription are to be permanently halted, before all of them occurred.";
            case DRAFT: return "The prescription is not yet 'actionable', i.e. it is a work in progress, requires sign-off or verification, and needs to be run through decision support process.";
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
            case DRAFT: return "Draft";
            default: return "?";
          }
        }
    }

  public static class MedicationOrderStatusEnumFactory implements EnumFactory<MedicationOrderStatus> {
    public MedicationOrderStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("active".equals(codeString))
          return MedicationOrderStatus.ACTIVE;
        if ("on-hold".equals(codeString))
          return MedicationOrderStatus.ONHOLD;
        if ("completed".equals(codeString))
          return MedicationOrderStatus.COMPLETED;
        if ("entered-in-error".equals(codeString))
          return MedicationOrderStatus.ENTEREDINERROR;
        if ("stopped".equals(codeString))
          return MedicationOrderStatus.STOPPED;
        if ("draft".equals(codeString))
          return MedicationOrderStatus.DRAFT;
        throw new IllegalArgumentException("Unknown MedicationOrderStatus code '"+codeString+"'");
        }
        public Enumeration<MedicationOrderStatus> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("active".equals(codeString))
          return new Enumeration<MedicationOrderStatus>(this, MedicationOrderStatus.ACTIVE);
        if ("on-hold".equals(codeString))
          return new Enumeration<MedicationOrderStatus>(this, MedicationOrderStatus.ONHOLD);
        if ("completed".equals(codeString))
          return new Enumeration<MedicationOrderStatus>(this, MedicationOrderStatus.COMPLETED);
        if ("entered-in-error".equals(codeString))
          return new Enumeration<MedicationOrderStatus>(this, MedicationOrderStatus.ENTEREDINERROR);
        if ("stopped".equals(codeString))
          return new Enumeration<MedicationOrderStatus>(this, MedicationOrderStatus.STOPPED);
        if ("draft".equals(codeString))
          return new Enumeration<MedicationOrderStatus>(this, MedicationOrderStatus.DRAFT);
        throw new FHIRException("Unknown MedicationOrderStatus code '"+codeString+"'");
        }
    public String toCode(MedicationOrderStatus code) {
      if (code == MedicationOrderStatus.ACTIVE)
        return "active";
      if (code == MedicationOrderStatus.ONHOLD)
        return "on-hold";
      if (code == MedicationOrderStatus.COMPLETED)
        return "completed";
      if (code == MedicationOrderStatus.ENTEREDINERROR)
        return "entered-in-error";
      if (code == MedicationOrderStatus.STOPPED)
        return "stopped";
      if (code == MedicationOrderStatus.DRAFT)
        return "draft";
      return "?";
      }
    public String toSystem(MedicationOrderStatus code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class MedicationOrderDosageInstructionComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Free text dosage instructions can be used for cases where the instructions are too complex to code.  The content of this attribute does not include the name or description of the medication. When coded instructions are present, the free text instructions may still be present for display to humans taking or administering the medication. It is expected that the text instructions will always be populated.  If the dosage.timing attribute is also populated, then the dosage.text should reflect the same information as the timing.
         */
        @Child(name = "text", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Free text dosage instructions e.g. SIG", formalDefinition="Free text dosage instructions can be used for cases where the instructions are too complex to code.  The content of this attribute does not include the name or description of the medication. When coded instructions are present, the free text instructions may still be present for display to humans taking or administering the medication. It is expected that the text instructions will always be populated.  If the dosage.timing attribute is also populated, then the dosage.text should reflect the same information as the timing." )
        protected StringType text;

        /**
         * Additional instructions such as "Swallow with plenty of water" which may or may not be coded.
         */
        @Child(name = "additionalInstructions", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Supplemental instructions - e.g. \"with meals\"", formalDefinition="Additional instructions such as \"Swallow with plenty of water\" which may or may not be coded." )
        protected CodeableConcept additionalInstructions;

        /**
         * The timing schedule for giving the medication to the patient. The Schedule data type allows many different expressions. For example: "Every 8 hours"; "Three times a day"; "1/2 an hour before breakfast for 10 days from 23-Dec 2011:"; "15 Oct 2013, 17 Oct 2013 and 1 Nov 2013".
         */
        @Child(name = "timing", type = {Timing.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="When medication should be administered", formalDefinition="The timing schedule for giving the medication to the patient. The Schedule data type allows many different expressions. For example: \"Every 8 hours\"; \"Three times a day\"; \"1/2 an hour before breakfast for 10 days from 23-Dec 2011:\"; \"15 Oct 2013, 17 Oct 2013 and 1 Nov 2013\"." )
        protected Timing timing;

        /**
         * Indicates whether the Medication is only taken when needed within a specific dosing schedule (Boolean option), or it indicates the precondition for taking the Medication (CodeableConcept).
         */
        @Child(name = "asNeeded", type = {BooleanType.class, CodeableConcept.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Take \"as needed\" (for x)", formalDefinition="Indicates whether the Medication is only taken when needed within a specific dosing schedule (Boolean option), or it indicates the precondition for taking the Medication (CodeableConcept)." )
        protected Type asNeeded;

        /**
         * A coded specification of the anatomic site where the medication first enters the body.
         */
        @Child(name = "site", type = {CodeableConcept.class, BodySite.class}, order=5, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Body site to administer to", formalDefinition="A coded specification of the anatomic site where the medication first enters the body." )
        protected Type site;

        /**
         * A code specifying the route or physiological path of administration of a therapeutic agent into or onto a patient's body.
         */
        @Child(name = "route", type = {CodeableConcept.class}, order=6, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="How drug should enter body", formalDefinition="A code specifying the route or physiological path of administration of a therapeutic agent into or onto a patient's body." )
        protected CodeableConcept route;

        /**
         * A coded value indicating the method by which the medication is introduced into or onto the body. Most commonly used for injections.  For examples, Slow Push; Deep IV.
         */
        @Child(name = "method", type = {CodeableConcept.class}, order=7, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Technique for administering medication", formalDefinition="A coded value indicating the method by which the medication is introduced into or onto the body. Most commonly used for injections.  For examples, Slow Push; Deep IV." )
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
        @Child(name = "rate", type = {Ratio.class, Range.class, SimpleQuantity.class}, order=9, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Amount of medication per unit of time", formalDefinition="Identifies the speed with which the medication was or will be introduced into the patient. Typically the rate for an infusion e.g. 100 ml per 1 hour or 100 ml/hr.  May also be expressed as a rate per unit of time e.g. 500 ml per 2 hours.   Currently we do not specify a default of '1' in the denominator, but this is being discussed. Other examples: 200 mcg/min or 200 mcg/1 minute; 1 liter/8 hours." )
        protected Type rate;

        /**
         * The maximum total quantity of a therapeutic substance that may be administered to a subject over the period of time.  For example, 1000mg in 24 hours.
         */
        @Child(name = "maxDosePerPeriod", type = {Ratio.class}, order=10, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Upper limit on medication per unit of time", formalDefinition="The maximum total quantity of a therapeutic substance that may be administered to a subject over the period of time.  For example, 1000mg in 24 hours." )
        protected Ratio maxDosePerPeriod;

        private static final long serialVersionUID = -1470136646L;

    /**
     * Constructor
     */
      public MedicationOrderDosageInstructionComponent() {
        super();
      }

        /**
         * @return {@link #text} (Free text dosage instructions can be used for cases where the instructions are too complex to code.  The content of this attribute does not include the name or description of the medication. When coded instructions are present, the free text instructions may still be present for display to humans taking or administering the medication. It is expected that the text instructions will always be populated.  If the dosage.timing attribute is also populated, then the dosage.text should reflect the same information as the timing.). This is the underlying object with id, value and extensions. The accessor "getText" gives direct access to the value
         */
        public StringType getTextElement() { 
          if (this.text == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationOrderDosageInstructionComponent.text");
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
         * @param value {@link #text} (Free text dosage instructions can be used for cases where the instructions are too complex to code.  The content of this attribute does not include the name or description of the medication. When coded instructions are present, the free text instructions may still be present for display to humans taking or administering the medication. It is expected that the text instructions will always be populated.  If the dosage.timing attribute is also populated, then the dosage.text should reflect the same information as the timing.). This is the underlying object with id, value and extensions. The accessor "getText" gives direct access to the value
         */
        public MedicationOrderDosageInstructionComponent setTextElement(StringType value) { 
          this.text = value;
          return this;
        }

        /**
         * @return Free text dosage instructions can be used for cases where the instructions are too complex to code.  The content of this attribute does not include the name or description of the medication. When coded instructions are present, the free text instructions may still be present for display to humans taking or administering the medication. It is expected that the text instructions will always be populated.  If the dosage.timing attribute is also populated, then the dosage.text should reflect the same information as the timing.
         */
        public String getText() { 
          return this.text == null ? null : this.text.getValue();
        }

        /**
         * @param value Free text dosage instructions can be used for cases where the instructions are too complex to code.  The content of this attribute does not include the name or description of the medication. When coded instructions are present, the free text instructions may still be present for display to humans taking or administering the medication. It is expected that the text instructions will always be populated.  If the dosage.timing attribute is also populated, then the dosage.text should reflect the same information as the timing.
         */
        public MedicationOrderDosageInstructionComponent setText(String value) { 
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
              throw new Error("Attempt to auto-create MedicationOrderDosageInstructionComponent.additionalInstructions");
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
        public MedicationOrderDosageInstructionComponent setAdditionalInstructions(CodeableConcept value) { 
          this.additionalInstructions = value;
          return this;
        }

        /**
         * @return {@link #timing} (The timing schedule for giving the medication to the patient. The Schedule data type allows many different expressions. For example: "Every 8 hours"; "Three times a day"; "1/2 an hour before breakfast for 10 days from 23-Dec 2011:"; "15 Oct 2013, 17 Oct 2013 and 1 Nov 2013".)
         */
        public Timing getTiming() { 
          if (this.timing == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationOrderDosageInstructionComponent.timing");
            else if (Configuration.doAutoCreate())
              this.timing = new Timing(); // cc
          return this.timing;
        }

        public boolean hasTiming() { 
          return this.timing != null && !this.timing.isEmpty();
        }

        /**
         * @param value {@link #timing} (The timing schedule for giving the medication to the patient. The Schedule data type allows many different expressions. For example: "Every 8 hours"; "Three times a day"; "1/2 an hour before breakfast for 10 days from 23-Dec 2011:"; "15 Oct 2013, 17 Oct 2013 and 1 Nov 2013".)
         */
        public MedicationOrderDosageInstructionComponent setTiming(Timing value) { 
          this.timing = value;
          return this;
        }

        /**
         * @return {@link #asNeeded} (Indicates whether the Medication is only taken when needed within a specific dosing schedule (Boolean option), or it indicates the precondition for taking the Medication (CodeableConcept).)
         */
        public Type getAsNeeded() { 
          return this.asNeeded;
        }

        /**
         * @return {@link #asNeeded} (Indicates whether the Medication is only taken when needed within a specific dosing schedule (Boolean option), or it indicates the precondition for taking the Medication (CodeableConcept).)
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
         * @return {@link #asNeeded} (Indicates whether the Medication is only taken when needed within a specific dosing schedule (Boolean option), or it indicates the precondition for taking the Medication (CodeableConcept).)
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
         * @param value {@link #asNeeded} (Indicates whether the Medication is only taken when needed within a specific dosing schedule (Boolean option), or it indicates the precondition for taking the Medication (CodeableConcept).)
         */
        public MedicationOrderDosageInstructionComponent setAsNeeded(Type value) { 
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
        public MedicationOrderDosageInstructionComponent setSite(Type value) { 
          this.site = value;
          return this;
        }

        /**
         * @return {@link #route} (A code specifying the route or physiological path of administration of a therapeutic agent into or onto a patient's body.)
         */
        public CodeableConcept getRoute() { 
          if (this.route == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationOrderDosageInstructionComponent.route");
            else if (Configuration.doAutoCreate())
              this.route = new CodeableConcept(); // cc
          return this.route;
        }

        public boolean hasRoute() { 
          return this.route != null && !this.route.isEmpty();
        }

        /**
         * @param value {@link #route} (A code specifying the route or physiological path of administration of a therapeutic agent into or onto a patient's body.)
         */
        public MedicationOrderDosageInstructionComponent setRoute(CodeableConcept value) { 
          this.route = value;
          return this;
        }

        /**
         * @return {@link #method} (A coded value indicating the method by which the medication is introduced into or onto the body. Most commonly used for injections.  For examples, Slow Push; Deep IV.)
         */
        public CodeableConcept getMethod() { 
          if (this.method == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationOrderDosageInstructionComponent.method");
            else if (Configuration.doAutoCreate())
              this.method = new CodeableConcept(); // cc
          return this.method;
        }

        public boolean hasMethod() { 
          return this.method != null && !this.method.isEmpty();
        }

        /**
         * @param value {@link #method} (A coded value indicating the method by which the medication is introduced into or onto the body. Most commonly used for injections.  For examples, Slow Push; Deep IV.)
         */
        public MedicationOrderDosageInstructionComponent setMethod(CodeableConcept value) { 
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
        public MedicationOrderDosageInstructionComponent setDose(Type value) { 
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

        /**
         * @return {@link #rate} (Identifies the speed with which the medication was or will be introduced into the patient. Typically the rate for an infusion e.g. 100 ml per 1 hour or 100 ml/hr.  May also be expressed as a rate per unit of time e.g. 500 ml per 2 hours.   Currently we do not specify a default of '1' in the denominator, but this is being discussed. Other examples: 200 mcg/min or 200 mcg/1 minute; 1 liter/8 hours.)
         */
        public SimpleQuantity getRateSimpleQuantity() throws FHIRException { 
          if (!(this.rate instanceof SimpleQuantity))
            throw new FHIRException("Type mismatch: the type SimpleQuantity was expected, but "+this.rate.getClass().getName()+" was encountered");
          return (SimpleQuantity) this.rate;
        }

        public boolean hasRateSimpleQuantity() { 
          return this.rate instanceof SimpleQuantity;
        }

        public boolean hasRate() { 
          return this.rate != null && !this.rate.isEmpty();
        }

        /**
         * @param value {@link #rate} (Identifies the speed with which the medication was or will be introduced into the patient. Typically the rate for an infusion e.g. 100 ml per 1 hour or 100 ml/hr.  May also be expressed as a rate per unit of time e.g. 500 ml per 2 hours.   Currently we do not specify a default of '1' in the denominator, but this is being discussed. Other examples: 200 mcg/min or 200 mcg/1 minute; 1 liter/8 hours.)
         */
        public MedicationOrderDosageInstructionComponent setRate(Type value) { 
          this.rate = value;
          return this;
        }

        /**
         * @return {@link #maxDosePerPeriod} (The maximum total quantity of a therapeutic substance that may be administered to a subject over the period of time.  For example, 1000mg in 24 hours.)
         */
        public Ratio getMaxDosePerPeriod() { 
          if (this.maxDosePerPeriod == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationOrderDosageInstructionComponent.maxDosePerPeriod");
            else if (Configuration.doAutoCreate())
              this.maxDosePerPeriod = new Ratio(); // cc
          return this.maxDosePerPeriod;
        }

        public boolean hasMaxDosePerPeriod() { 
          return this.maxDosePerPeriod != null && !this.maxDosePerPeriod.isEmpty();
        }

        /**
         * @param value {@link #maxDosePerPeriod} (The maximum total quantity of a therapeutic substance that may be administered to a subject over the period of time.  For example, 1000mg in 24 hours.)
         */
        public MedicationOrderDosageInstructionComponent setMaxDosePerPeriod(Ratio value) { 
          this.maxDosePerPeriod = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("text", "string", "Free text dosage instructions can be used for cases where the instructions are too complex to code.  The content of this attribute does not include the name or description of the medication. When coded instructions are present, the free text instructions may still be present for display to humans taking or administering the medication. It is expected that the text instructions will always be populated.  If the dosage.timing attribute is also populated, then the dosage.text should reflect the same information as the timing.", 0, java.lang.Integer.MAX_VALUE, text));
          childrenList.add(new Property("additionalInstructions", "CodeableConcept", "Additional instructions such as \"Swallow with plenty of water\" which may or may not be coded.", 0, java.lang.Integer.MAX_VALUE, additionalInstructions));
          childrenList.add(new Property("timing", "Timing", "The timing schedule for giving the medication to the patient. The Schedule data type allows many different expressions. For example: \"Every 8 hours\"; \"Three times a day\"; \"1/2 an hour before breakfast for 10 days from 23-Dec 2011:\"; \"15 Oct 2013, 17 Oct 2013 and 1 Nov 2013\".", 0, java.lang.Integer.MAX_VALUE, timing));
          childrenList.add(new Property("asNeeded[x]", "boolean|CodeableConcept", "Indicates whether the Medication is only taken when needed within a specific dosing schedule (Boolean option), or it indicates the precondition for taking the Medication (CodeableConcept).", 0, java.lang.Integer.MAX_VALUE, asNeeded));
          childrenList.add(new Property("site[x]", "CodeableConcept|Reference(BodySite)", "A coded specification of the anatomic site where the medication first enters the body.", 0, java.lang.Integer.MAX_VALUE, site));
          childrenList.add(new Property("route", "CodeableConcept", "A code specifying the route or physiological path of administration of a therapeutic agent into or onto a patient's body.", 0, java.lang.Integer.MAX_VALUE, route));
          childrenList.add(new Property("method", "CodeableConcept", "A coded value indicating the method by which the medication is introduced into or onto the body. Most commonly used for injections.  For examples, Slow Push; Deep IV.", 0, java.lang.Integer.MAX_VALUE, method));
          childrenList.add(new Property("dose[x]", "Range|SimpleQuantity", "The amount of therapeutic or other substance given at one administration event.", 0, java.lang.Integer.MAX_VALUE, dose));
          childrenList.add(new Property("rate[x]", "Ratio|Range|SimpleQuantity", "Identifies the speed with which the medication was or will be introduced into the patient. Typically the rate for an infusion e.g. 100 ml per 1 hour or 100 ml/hr.  May also be expressed as a rate per unit of time e.g. 500 ml per 2 hours.   Currently we do not specify a default of '1' in the denominator, but this is being discussed. Other examples: 200 mcg/min or 200 mcg/1 minute; 1 liter/8 hours.", 0, java.lang.Integer.MAX_VALUE, rate));
          childrenList.add(new Property("maxDosePerPeriod", "Ratio", "The maximum total quantity of a therapeutic substance that may be administered to a subject over the period of time.  For example, 1000mg in 24 hours.", 0, java.lang.Integer.MAX_VALUE, maxDosePerPeriod));
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
          throw new FHIRException("Cannot call addChild on a primitive type MedicationOrder.text");
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
        else if (name.equals("rateSimpleQuantity")) {
          this.rate = new SimpleQuantity();
          return this.rate;
        }
        else if (name.equals("maxDosePerPeriod")) {
          this.maxDosePerPeriod = new Ratio();
          return this.maxDosePerPeriod;
        }
        else
          return super.addChild(name);
      }

      public MedicationOrderDosageInstructionComponent copy() {
        MedicationOrderDosageInstructionComponent dst = new MedicationOrderDosageInstructionComponent();
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
        if (!(other instanceof MedicationOrderDosageInstructionComponent))
          return false;
        MedicationOrderDosageInstructionComponent o = (MedicationOrderDosageInstructionComponent) other;
        return compareDeep(text, o.text, true) && compareDeep(additionalInstructions, o.additionalInstructions, true)
           && compareDeep(timing, o.timing, true) && compareDeep(asNeeded, o.asNeeded, true) && compareDeep(site, o.site, true)
           && compareDeep(route, o.route, true) && compareDeep(method, o.method, true) && compareDeep(dose, o.dose, true)
           && compareDeep(rate, o.rate, true) && compareDeep(maxDosePerPeriod, o.maxDosePerPeriod, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof MedicationOrderDosageInstructionComponent))
          return false;
        MedicationOrderDosageInstructionComponent o = (MedicationOrderDosageInstructionComponent) other;
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
    return "MedicationOrder.dosageInstruction";

  }

  }

    @Block()
    public static class MedicationOrderDispenseRequestComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Identifies the medication being administered. This is a link to a resource that represents the medication which may be the details of the medication or simply an attribute carrying a code that identifies the medication from a known list of medications.
         */
        @Child(name = "medication", type = {CodeableConcept.class, Medication.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Product to be supplied", formalDefinition="Identifies the medication being administered. This is a link to a resource that represents the medication which may be the details of the medication or simply an attribute carrying a code that identifies the medication from a known list of medications." )
        protected Type medication;

        /**
         * This indicates the validity period of a prescription (stale dating the Prescription).
         */
        @Child(name = "validityPeriod", type = {Period.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Time period supply is authorized for", formalDefinition="This indicates the validity period of a prescription (stale dating the Prescription)." )
        protected Period validityPeriod;

        /**
         * An integer indicating the number of additional times (aka refills or repeats) the patient can receive the prescribed medication.   Usage Notes: This integer does NOT include the original order dispense.   This means that if an order indicates dispense 30 tablets plus  "3 repeats", then the order can be dispensed a total of 4 times and the patient can receive a total of 120 tablets.
         */
        @Child(name = "numberOfRepeatsAllowed", type = {PositiveIntType.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Number of refills authorized", formalDefinition="An integer indicating the number of additional times (aka refills or repeats) the patient can receive the prescribed medication.   Usage Notes: This integer does NOT include the original order dispense.   This means that if an order indicates dispense 30 tablets plus  \"3 repeats\", then the order can be dispensed a total of 4 times and the patient can receive a total of 120 tablets." )
        protected PositiveIntType numberOfRepeatsAllowed;

        /**
         * The amount that is to be dispensed for one fill.
         */
        @Child(name = "quantity", type = {SimpleQuantity.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Amount of medication to supply per dispense", formalDefinition="The amount that is to be dispensed for one fill." )
        protected SimpleQuantity quantity;

        /**
         * Identifies the period time over which the supplied product is expected to be used, or the length of time the dispense is expected to last.
         */
        @Child(name = "expectedSupplyDuration", type = {Duration.class}, order=5, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Number of days supply per dispense", formalDefinition="Identifies the period time over which the supplied product is expected to be used, or the length of time the dispense is expected to last." )
        protected Duration expectedSupplyDuration;

        private static final long serialVersionUID = -1690502728L;

    /**
     * Constructor
     */
      public MedicationOrderDispenseRequestComponent() {
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
        public CodeableConcept getMedicationCodeableConcept() throws FHIRException { 
          if (!(this.medication instanceof CodeableConcept))
            throw new FHIRException("Type mismatch: the type CodeableConcept was expected, but "+this.medication.getClass().getName()+" was encountered");
          return (CodeableConcept) this.medication;
        }

        public boolean hasMedicationCodeableConcept() { 
          return this.medication instanceof CodeableConcept;
        }

        /**
         * @return {@link #medication} (Identifies the medication being administered. This is a link to a resource that represents the medication which may be the details of the medication or simply an attribute carrying a code that identifies the medication from a known list of medications.)
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
         * @param value {@link #medication} (Identifies the medication being administered. This is a link to a resource that represents the medication which may be the details of the medication or simply an attribute carrying a code that identifies the medication from a known list of medications.)
         */
        public MedicationOrderDispenseRequestComponent setMedication(Type value) { 
          this.medication = value;
          return this;
        }

        /**
         * @return {@link #validityPeriod} (This indicates the validity period of a prescription (stale dating the Prescription).)
         */
        public Period getValidityPeriod() { 
          if (this.validityPeriod == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationOrderDispenseRequestComponent.validityPeriod");
            else if (Configuration.doAutoCreate())
              this.validityPeriod = new Period(); // cc
          return this.validityPeriod;
        }

        public boolean hasValidityPeriod() { 
          return this.validityPeriod != null && !this.validityPeriod.isEmpty();
        }

        /**
         * @param value {@link #validityPeriod} (This indicates the validity period of a prescription (stale dating the Prescription).)
         */
        public MedicationOrderDispenseRequestComponent setValidityPeriod(Period value) { 
          this.validityPeriod = value;
          return this;
        }

        /**
         * @return {@link #numberOfRepeatsAllowed} (An integer indicating the number of additional times (aka refills or repeats) the patient can receive the prescribed medication.   Usage Notes: This integer does NOT include the original order dispense.   This means that if an order indicates dispense 30 tablets plus  "3 repeats", then the order can be dispensed a total of 4 times and the patient can receive a total of 120 tablets.). This is the underlying object with id, value and extensions. The accessor "getNumberOfRepeatsAllowed" gives direct access to the value
         */
        public PositiveIntType getNumberOfRepeatsAllowedElement() { 
          if (this.numberOfRepeatsAllowed == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationOrderDispenseRequestComponent.numberOfRepeatsAllowed");
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
         * @param value {@link #numberOfRepeatsAllowed} (An integer indicating the number of additional times (aka refills or repeats) the patient can receive the prescribed medication.   Usage Notes: This integer does NOT include the original order dispense.   This means that if an order indicates dispense 30 tablets plus  "3 repeats", then the order can be dispensed a total of 4 times and the patient can receive a total of 120 tablets.). This is the underlying object with id, value and extensions. The accessor "getNumberOfRepeatsAllowed" gives direct access to the value
         */
        public MedicationOrderDispenseRequestComponent setNumberOfRepeatsAllowedElement(PositiveIntType value) { 
          this.numberOfRepeatsAllowed = value;
          return this;
        }

        /**
         * @return An integer indicating the number of additional times (aka refills or repeats) the patient can receive the prescribed medication.   Usage Notes: This integer does NOT include the original order dispense.   This means that if an order indicates dispense 30 tablets plus  "3 repeats", then the order can be dispensed a total of 4 times and the patient can receive a total of 120 tablets.
         */
        public int getNumberOfRepeatsAllowed() { 
          return this.numberOfRepeatsAllowed == null || this.numberOfRepeatsAllowed.isEmpty() ? 0 : this.numberOfRepeatsAllowed.getValue();
        }

        /**
         * @param value An integer indicating the number of additional times (aka refills or repeats) the patient can receive the prescribed medication.   Usage Notes: This integer does NOT include the original order dispense.   This means that if an order indicates dispense 30 tablets plus  "3 repeats", then the order can be dispensed a total of 4 times and the patient can receive a total of 120 tablets.
         */
        public MedicationOrderDispenseRequestComponent setNumberOfRepeatsAllowed(int value) { 
            if (this.numberOfRepeatsAllowed == null)
              this.numberOfRepeatsAllowed = new PositiveIntType();
            this.numberOfRepeatsAllowed.setValue(value);
          return this;
        }

        /**
         * @return {@link #quantity} (The amount that is to be dispensed for one fill.)
         */
        public SimpleQuantity getQuantity() { 
          if (this.quantity == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationOrderDispenseRequestComponent.quantity");
            else if (Configuration.doAutoCreate())
              this.quantity = new SimpleQuantity(); // cc
          return this.quantity;
        }

        public boolean hasQuantity() { 
          return this.quantity != null && !this.quantity.isEmpty();
        }

        /**
         * @param value {@link #quantity} (The amount that is to be dispensed for one fill.)
         */
        public MedicationOrderDispenseRequestComponent setQuantity(SimpleQuantity value) { 
          this.quantity = value;
          return this;
        }

        /**
         * @return {@link #expectedSupplyDuration} (Identifies the period time over which the supplied product is expected to be used, or the length of time the dispense is expected to last.)
         */
        public Duration getExpectedSupplyDuration() { 
          if (this.expectedSupplyDuration == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationOrderDispenseRequestComponent.expectedSupplyDuration");
            else if (Configuration.doAutoCreate())
              this.expectedSupplyDuration = new Duration(); // cc
          return this.expectedSupplyDuration;
        }

        public boolean hasExpectedSupplyDuration() { 
          return this.expectedSupplyDuration != null && !this.expectedSupplyDuration.isEmpty();
        }

        /**
         * @param value {@link #expectedSupplyDuration} (Identifies the period time over which the supplied product is expected to be used, or the length of time the dispense is expected to last.)
         */
        public MedicationOrderDispenseRequestComponent setExpectedSupplyDuration(Duration value) { 
          this.expectedSupplyDuration = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("medication[x]", "CodeableConcept|Reference(Medication)", "Identifies the medication being administered. This is a link to a resource that represents the medication which may be the details of the medication or simply an attribute carrying a code that identifies the medication from a known list of medications.", 0, java.lang.Integer.MAX_VALUE, medication));
          childrenList.add(new Property("validityPeriod", "Period", "This indicates the validity period of a prescription (stale dating the Prescription).", 0, java.lang.Integer.MAX_VALUE, validityPeriod));
          childrenList.add(new Property("numberOfRepeatsAllowed", "positiveInt", "An integer indicating the number of additional times (aka refills or repeats) the patient can receive the prescribed medication.   Usage Notes: This integer does NOT include the original order dispense.   This means that if an order indicates dispense 30 tablets plus  \"3 repeats\", then the order can be dispensed a total of 4 times and the patient can receive a total of 120 tablets.", 0, java.lang.Integer.MAX_VALUE, numberOfRepeatsAllowed));
          childrenList.add(new Property("quantity", "SimpleQuantity", "The amount that is to be dispensed for one fill.", 0, java.lang.Integer.MAX_VALUE, quantity));
          childrenList.add(new Property("expectedSupplyDuration", "Duration", "Identifies the period time over which the supplied product is expected to be used, or the length of time the dispense is expected to last.", 0, java.lang.Integer.MAX_VALUE, expectedSupplyDuration));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 1998965455: /*medication*/ return this.medication == null ? new Base[0] : new Base[] {this.medication}; // Type
        case -1434195053: /*validityPeriod*/ return this.validityPeriod == null ? new Base[0] : new Base[] {this.validityPeriod}; // Period
        case -239736976: /*numberOfRepeatsAllowed*/ return this.numberOfRepeatsAllowed == null ? new Base[0] : new Base[] {this.numberOfRepeatsAllowed}; // PositiveIntType
        case -1285004149: /*quantity*/ return this.quantity == null ? new Base[0] : new Base[] {this.quantity}; // SimpleQuantity
        case -1910182789: /*expectedSupplyDuration*/ return this.expectedSupplyDuration == null ? new Base[0] : new Base[] {this.expectedSupplyDuration}; // Duration
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 1998965455: // medication
          this.medication = (Type) value; // Type
          break;
        case -1434195053: // validityPeriod
          this.validityPeriod = castToPeriod(value); // Period
          break;
        case -239736976: // numberOfRepeatsAllowed
          this.numberOfRepeatsAllowed = castToPositiveInt(value); // PositiveIntType
          break;
        case -1285004149: // quantity
          this.quantity = castToSimpleQuantity(value); // SimpleQuantity
          break;
        case -1910182789: // expectedSupplyDuration
          this.expectedSupplyDuration = castToDuration(value); // Duration
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("medication[x]"))
          this.medication = (Type) value; // Type
        else if (name.equals("validityPeriod"))
          this.validityPeriod = castToPeriod(value); // Period
        else if (name.equals("numberOfRepeatsAllowed"))
          this.numberOfRepeatsAllowed = castToPositiveInt(value); // PositiveIntType
        else if (name.equals("quantity"))
          this.quantity = castToSimpleQuantity(value); // SimpleQuantity
        else if (name.equals("expectedSupplyDuration"))
          this.expectedSupplyDuration = castToDuration(value); // Duration
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1458402129:  return getMedication(); // Type
        case -1434195053:  return getValidityPeriod(); // Period
        case -239736976: throw new FHIRException("Cannot make property numberOfRepeatsAllowed as it is not a complex type"); // PositiveIntType
        case -1285004149:  return getQuantity(); // SimpleQuantity
        case -1910182789:  return getExpectedSupplyDuration(); // Duration
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("medicationCodeableConcept")) {
          this.medication = new CodeableConcept();
          return this.medication;
        }
        else if (name.equals("medicationReference")) {
          this.medication = new Reference();
          return this.medication;
        }
        else if (name.equals("validityPeriod")) {
          this.validityPeriod = new Period();
          return this.validityPeriod;
        }
        else if (name.equals("numberOfRepeatsAllowed")) {
          throw new FHIRException("Cannot call addChild on a primitive type MedicationOrder.numberOfRepeatsAllowed");
        }
        else if (name.equals("quantity")) {
          this.quantity = new SimpleQuantity();
          return this.quantity;
        }
        else if (name.equals("expectedSupplyDuration")) {
          this.expectedSupplyDuration = new Duration();
          return this.expectedSupplyDuration;
        }
        else
          return super.addChild(name);
      }

      public MedicationOrderDispenseRequestComponent copy() {
        MedicationOrderDispenseRequestComponent dst = new MedicationOrderDispenseRequestComponent();
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
        if (!(other instanceof MedicationOrderDispenseRequestComponent))
          return false;
        MedicationOrderDispenseRequestComponent o = (MedicationOrderDispenseRequestComponent) other;
        return compareDeep(medication, o.medication, true) && compareDeep(validityPeriod, o.validityPeriod, true)
           && compareDeep(numberOfRepeatsAllowed, o.numberOfRepeatsAllowed, true) && compareDeep(quantity, o.quantity, true)
           && compareDeep(expectedSupplyDuration, o.expectedSupplyDuration, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof MedicationOrderDispenseRequestComponent))
          return false;
        MedicationOrderDispenseRequestComponent o = (MedicationOrderDispenseRequestComponent) other;
        return compareValues(numberOfRepeatsAllowed, o.numberOfRepeatsAllowed, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (medication == null || medication.isEmpty()) && (validityPeriod == null || validityPeriod.isEmpty())
           && (numberOfRepeatsAllowed == null || numberOfRepeatsAllowed.isEmpty()) && (quantity == null || quantity.isEmpty())
           && (expectedSupplyDuration == null || expectedSupplyDuration.isEmpty());
      }

  public String fhirType() {
    return "MedicationOrder.dispenseRequest";

  }

  }

    @Block()
    public static class MedicationOrderSubstitutionComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A code signifying whether a different drug should be dispensed from what was prescribed.
         */
        @Child(name = "type", type = {CodeableConcept.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="generic | formulary +", formalDefinition="A code signifying whether a different drug should be dispensed from what was prescribed." )
        protected CodeableConcept type;

        /**
         * Indicates the reason for the substitution, or why substitution must or must not be performed.
         */
        @Child(name = "reason", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Why should (not) substitution be made", formalDefinition="Indicates the reason for the substitution, or why substitution must or must not be performed." )
        protected CodeableConcept reason;

        private static final long serialVersionUID = 1693602518L;

    /**
     * Constructor
     */
      public MedicationOrderSubstitutionComponent() {
        super();
      }

    /**
     * Constructor
     */
      public MedicationOrderSubstitutionComponent(CodeableConcept type) {
        super();
        this.type = type;
      }

        /**
         * @return {@link #type} (A code signifying whether a different drug should be dispensed from what was prescribed.)
         */
        public CodeableConcept getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationOrderSubstitutionComponent.type");
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
        public MedicationOrderSubstitutionComponent setType(CodeableConcept value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #reason} (Indicates the reason for the substitution, or why substitution must or must not be performed.)
         */
        public CodeableConcept getReason() { 
          if (this.reason == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationOrderSubstitutionComponent.reason");
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
        public MedicationOrderSubstitutionComponent setReason(CodeableConcept value) { 
          this.reason = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("type", "CodeableConcept", "A code signifying whether a different drug should be dispensed from what was prescribed.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("reason", "CodeableConcept", "Indicates the reason for the substitution, or why substitution must or must not be performed.", 0, java.lang.Integer.MAX_VALUE, reason));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case -934964668: /*reason*/ return this.reason == null ? new Base[0] : new Base[] {this.reason}; // CodeableConcept
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
          this.reason = castToCodeableConcept(value); // CodeableConcept
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type"))
          this.type = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("reason"))
          this.reason = castToCodeableConcept(value); // CodeableConcept
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610:  return getType(); // CodeableConcept
        case -934964668:  return getReason(); // CodeableConcept
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
          this.reason = new CodeableConcept();
          return this.reason;
        }
        else
          return super.addChild(name);
      }

      public MedicationOrderSubstitutionComponent copy() {
        MedicationOrderSubstitutionComponent dst = new MedicationOrderSubstitutionComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.reason = reason == null ? null : reason.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof MedicationOrderSubstitutionComponent))
          return false;
        MedicationOrderSubstitutionComponent o = (MedicationOrderSubstitutionComponent) other;
        return compareDeep(type, o.type, true) && compareDeep(reason, o.reason, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof MedicationOrderSubstitutionComponent))
          return false;
        MedicationOrderSubstitutionComponent o = (MedicationOrderSubstitutionComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (type == null || type.isEmpty()) && (reason == null || reason.isEmpty())
          ;
      }

  public String fhirType() {
    return "MedicationOrder.substitution";

  }

  }

    /**
     * External identifier - one that would be used by another non-FHIR system - for example a re-imbursement system might issue its own id for each prescription that is created.  This is particularly important where FHIR only provides part of an entire workflow process where records have to be tracked through an entire system.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="External identifier", formalDefinition="External identifier - one that would be used by another non-FHIR system - for example a re-imbursement system might issue its own id for each prescription that is created.  This is particularly important where FHIR only provides part of an entire workflow process where records have to be tracked through an entire system." )
    protected List<Identifier> identifier;

    /**
     * A code specifying the state of the order.  Generally this will be active or completed state.
     */
    @Child(name = "status", type = {CodeType.class}, order=1, min=0, max=1, modifier=true, summary=true)
    @Description(shortDefinition="active | on-hold | completed | entered-in-error | stopped | draft", formalDefinition="A code specifying the state of the order.  Generally this will be active or completed state." )
    protected Enumeration<MedicationOrderStatus> status;

    /**
     * Identifies the medication being administered. This is a link to a resource that represents the medication which may be the details of the medication or simply an attribute carrying a code that identifies the medication from a known list of medications.
     */
    @Child(name = "medication", type = {CodeableConcept.class, Medication.class}, order=2, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Medication to be taken", formalDefinition="Identifies the medication being administered. This is a link to a resource that represents the medication which may be the details of the medication or simply an attribute carrying a code that identifies the medication from a known list of medications." )
    protected Type medication;

    /**
     * A link to a resource representing the person to whom the medication will be given.
     */
    @Child(name = "patient", type = {Patient.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Who prescription is for", formalDefinition="A link to a resource representing the person to whom the medication will be given." )
    protected Reference patient;

    /**
     * The actual object that is the target of the reference (A link to a resource representing the person to whom the medication will be given.)
     */
    protected Patient patientTarget;

    /**
     * A link to a resource that identifies the particular occurrence of contact between patient and health care provider.
     */
    @Child(name = "encounter", type = {Encounter.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Created during encounter/admission/stay", formalDefinition="A link to a resource that identifies the particular occurrence of contact between patient and health care provider." )
    protected Reference encounter;

    /**
     * The actual object that is the target of the reference (A link to a resource that identifies the particular occurrence of contact between patient and health care provider.)
     */
    protected Encounter encounterTarget;

    /**
     * The date (and perhaps time) when the prescription was written.
     */
    @Child(name = "dateWritten", type = {DateTimeType.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When prescription was authorized", formalDefinition="The date (and perhaps time) when the prescription was written." )
    protected DateTimeType dateWritten;

    /**
     * The healthcare professional responsible for authorizing the prescription.
     */
    @Child(name = "prescriber", type = {Practitioner.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Who ordered the medication(s)", formalDefinition="The healthcare professional responsible for authorizing the prescription." )
    protected Reference prescriber;

    /**
     * The actual object that is the target of the reference (The healthcare professional responsible for authorizing the prescription.)
     */
    protected Practitioner prescriberTarget;

    /**
     * Can be the reason or the indication for writing the prescription.
     */
    @Child(name = "reasonCode", type = {CodeableConcept.class}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Reason or indication for writing the prescription", formalDefinition="Can be the reason or the indication for writing the prescription." )
    protected List<CodeableConcept> reasonCode;

    /**
     * Condition that supports why the prescription is being written.
     */
    @Child(name = "reasonReference", type = {Condition.class}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Condition that supports why the prescription is being written", formalDefinition="Condition that supports why the prescription is being written." )
    protected List<Reference> reasonReference;
    /**
     * The actual objects that are the target of the reference (Condition that supports why the prescription is being written.)
     */
    protected List<Condition> reasonReferenceTarget;


    /**
     * The date (and perhaps time) when the prescription was stopped.
     */
    @Child(name = "dateEnded", type = {DateTimeType.class}, order=9, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When prescription was stopped", formalDefinition="The date (and perhaps time) when the prescription was stopped." )
    protected DateTimeType dateEnded;

    /**
     * The reason why the prescription was stopped, if it was.
     */
    @Child(name = "reasonEnded", type = {CodeableConcept.class}, order=10, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Why prescription was stopped", formalDefinition="The reason why the prescription was stopped, if it was." )
    protected CodeableConcept reasonEnded;

    /**
     * Extra information about the prescription that could not be conveyed by the other attributes.
     */
    @Child(name = "note", type = {Annotation.class}, order=11, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Information about the prescription", formalDefinition="Extra information about the prescription that could not be conveyed by the other attributes." )
    protected List<Annotation> note;

    /**
     * Indicates how the medication is to be used by the patient.
     */
    @Child(name = "dosageInstruction", type = {}, order=12, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="How medication should be taken", formalDefinition="Indicates how the medication is to be used by the patient." )
    protected List<MedicationOrderDosageInstructionComponent> dosageInstruction;

    /**
     * Indicates the specific details for the dispense or medication supply part of a medication order (also known as a Medication Prescription).  Note that this information is NOT always sent with the order.  There may be in some settings (e.g. hospitals) institutional or system support for completing the dispense details in the pharmacy department.
     */
    @Child(name = "dispenseRequest", type = {}, order=13, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Medication supply authorization", formalDefinition="Indicates the specific details for the dispense or medication supply part of a medication order (also known as a Medication Prescription).  Note that this information is NOT always sent with the order.  There may be in some settings (e.g. hospitals) institutional or system support for completing the dispense details in the pharmacy department." )
    protected MedicationOrderDispenseRequestComponent dispenseRequest;

    /**
     * Indicates whether or not substitution can or should be part of the dispense. In some cases substitution must happen, in other cases substitution must not happen, and in others it does not matter. This block explains the prescriber's intent. If nothing is specified substitution may be done.
     */
    @Child(name = "substitution", type = {}, order=14, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Any restrictions on medication substitution", formalDefinition="Indicates whether or not substitution can or should be part of the dispense. In some cases substitution must happen, in other cases substitution must not happen, and in others it does not matter. This block explains the prescriber's intent. If nothing is specified substitution may be done." )
    protected MedicationOrderSubstitutionComponent substitution;

    /**
     * A link to a resource representing an earlier order or prescription that this order supersedes.
     */
    @Child(name = "priorPrescription", type = {MedicationOrder.class}, order=15, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="An order/prescription that this supersedes", formalDefinition="A link to a resource representing an earlier order or prescription that this order supersedes." )
    protected Reference priorPrescription;

    /**
     * The actual object that is the target of the reference (A link to a resource representing an earlier order or prescription that this order supersedes.)
     */
    protected MedicationOrder priorPrescriptionTarget;

    private static final long serialVersionUID = -1031457736L;

  /**
   * Constructor
   */
    public MedicationOrder() {
      super();
    }

  /**
   * Constructor
   */
    public MedicationOrder(Type medication) {
      super();
      this.medication = medication;
    }

    /**
     * @return {@link #identifier} (External identifier - one that would be used by another non-FHIR system - for example a re-imbursement system might issue its own id for each prescription that is created.  This is particularly important where FHIR only provides part of an entire workflow process where records have to be tracked through an entire system.)
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
     * @return {@link #identifier} (External identifier - one that would be used by another non-FHIR system - for example a re-imbursement system might issue its own id for each prescription that is created.  This is particularly important where FHIR only provides part of an entire workflow process where records have to be tracked through an entire system.)
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
    public MedicationOrder addIdentifier(Identifier t) { //3
      if (t == null)
        return this;
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return this;
    }

    /**
     * @return {@link #status} (A code specifying the state of the order.  Generally this will be active or completed state.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<MedicationOrderStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationOrder.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<MedicationOrderStatus>(new MedicationOrderStatusEnumFactory()); // bb
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
    public MedicationOrder setStatusElement(Enumeration<MedicationOrderStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return A code specifying the state of the order.  Generally this will be active or completed state.
     */
    public MedicationOrderStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value A code specifying the state of the order.  Generally this will be active or completed state.
     */
    public MedicationOrder setStatus(MedicationOrderStatus value) { 
      if (value == null)
        this.status = null;
      else {
        if (this.status == null)
          this.status = new Enumeration<MedicationOrderStatus>(new MedicationOrderStatusEnumFactory());
        this.status.setValue(value);
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
    public CodeableConcept getMedicationCodeableConcept() throws FHIRException { 
      if (!(this.medication instanceof CodeableConcept))
        throw new FHIRException("Type mismatch: the type CodeableConcept was expected, but "+this.medication.getClass().getName()+" was encountered");
      return (CodeableConcept) this.medication;
    }

    public boolean hasMedicationCodeableConcept() { 
      return this.medication instanceof CodeableConcept;
    }

    /**
     * @return {@link #medication} (Identifies the medication being administered. This is a link to a resource that represents the medication which may be the details of the medication or simply an attribute carrying a code that identifies the medication from a known list of medications.)
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
     * @param value {@link #medication} (Identifies the medication being administered. This is a link to a resource that represents the medication which may be the details of the medication or simply an attribute carrying a code that identifies the medication from a known list of medications.)
     */
    public MedicationOrder setMedication(Type value) { 
      this.medication = value;
      return this;
    }

    /**
     * @return {@link #patient} (A link to a resource representing the person to whom the medication will be given.)
     */
    public Reference getPatient() { 
      if (this.patient == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationOrder.patient");
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
    public MedicationOrder setPatient(Reference value) { 
      this.patient = value;
      return this;
    }

    /**
     * @return {@link #patient} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (A link to a resource representing the person to whom the medication will be given.)
     */
    public Patient getPatientTarget() { 
      if (this.patientTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationOrder.patient");
        else if (Configuration.doAutoCreate())
          this.patientTarget = new Patient(); // aa
      return this.patientTarget;
    }

    /**
     * @param value {@link #patient} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (A link to a resource representing the person to whom the medication will be given.)
     */
    public MedicationOrder setPatientTarget(Patient value) { 
      this.patientTarget = value;
      return this;
    }

    /**
     * @return {@link #encounter} (A link to a resource that identifies the particular occurrence of contact between patient and health care provider.)
     */
    public Reference getEncounter() { 
      if (this.encounter == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationOrder.encounter");
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
    public MedicationOrder setEncounter(Reference value) { 
      this.encounter = value;
      return this;
    }

    /**
     * @return {@link #encounter} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (A link to a resource that identifies the particular occurrence of contact between patient and health care provider.)
     */
    public Encounter getEncounterTarget() { 
      if (this.encounterTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationOrder.encounter");
        else if (Configuration.doAutoCreate())
          this.encounterTarget = new Encounter(); // aa
      return this.encounterTarget;
    }

    /**
     * @param value {@link #encounter} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (A link to a resource that identifies the particular occurrence of contact between patient and health care provider.)
     */
    public MedicationOrder setEncounterTarget(Encounter value) { 
      this.encounterTarget = value;
      return this;
    }

    /**
     * @return {@link #dateWritten} (The date (and perhaps time) when the prescription was written.). This is the underlying object with id, value and extensions. The accessor "getDateWritten" gives direct access to the value
     */
    public DateTimeType getDateWrittenElement() { 
      if (this.dateWritten == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationOrder.dateWritten");
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
    public MedicationOrder setDateWrittenElement(DateTimeType value) { 
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
    public MedicationOrder setDateWritten(Date value) { 
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
     * @return {@link #prescriber} (The healthcare professional responsible for authorizing the prescription.)
     */
    public Reference getPrescriber() { 
      if (this.prescriber == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationOrder.prescriber");
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
    public MedicationOrder setPrescriber(Reference value) { 
      this.prescriber = value;
      return this;
    }

    /**
     * @return {@link #prescriber} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The healthcare professional responsible for authorizing the prescription.)
     */
    public Practitioner getPrescriberTarget() { 
      if (this.prescriberTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationOrder.prescriber");
        else if (Configuration.doAutoCreate())
          this.prescriberTarget = new Practitioner(); // aa
      return this.prescriberTarget;
    }

    /**
     * @param value {@link #prescriber} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The healthcare professional responsible for authorizing the prescription.)
     */
    public MedicationOrder setPrescriberTarget(Practitioner value) { 
      this.prescriberTarget = value;
      return this;
    }

    /**
     * @return {@link #reasonCode} (Can be the reason or the indication for writing the prescription.)
     */
    public List<CodeableConcept> getReasonCode() { 
      if (this.reasonCode == null)
        this.reasonCode = new ArrayList<CodeableConcept>();
      return this.reasonCode;
    }

    public boolean hasReasonCode() { 
      if (this.reasonCode == null)
        return false;
      for (CodeableConcept item : this.reasonCode)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #reasonCode} (Can be the reason or the indication for writing the prescription.)
     */
    // syntactic sugar
    public CodeableConcept addReasonCode() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.reasonCode == null)
        this.reasonCode = new ArrayList<CodeableConcept>();
      this.reasonCode.add(t);
      return t;
    }

    // syntactic sugar
    public MedicationOrder addReasonCode(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.reasonCode == null)
        this.reasonCode = new ArrayList<CodeableConcept>();
      this.reasonCode.add(t);
      return this;
    }

    /**
     * @return {@link #reasonReference} (Condition that supports why the prescription is being written.)
     */
    public List<Reference> getReasonReference() { 
      if (this.reasonReference == null)
        this.reasonReference = new ArrayList<Reference>();
      return this.reasonReference;
    }

    public boolean hasReasonReference() { 
      if (this.reasonReference == null)
        return false;
      for (Reference item : this.reasonReference)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #reasonReference} (Condition that supports why the prescription is being written.)
     */
    // syntactic sugar
    public Reference addReasonReference() { //3
      Reference t = new Reference();
      if (this.reasonReference == null)
        this.reasonReference = new ArrayList<Reference>();
      this.reasonReference.add(t);
      return t;
    }

    // syntactic sugar
    public MedicationOrder addReasonReference(Reference t) { //3
      if (t == null)
        return this;
      if (this.reasonReference == null)
        this.reasonReference = new ArrayList<Reference>();
      this.reasonReference.add(t);
      return this;
    }

    /**
     * @return {@link #reasonReference} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. Condition that supports why the prescription is being written.)
     */
    public List<Condition> getReasonReferenceTarget() { 
      if (this.reasonReferenceTarget == null)
        this.reasonReferenceTarget = new ArrayList<Condition>();
      return this.reasonReferenceTarget;
    }

    // syntactic sugar
    /**
     * @return {@link #reasonReference} (Add an actual object that is the target of the reference. The reference library doesn't use these, but you can use this to hold the resources if you resolvethemt. Condition that supports why the prescription is being written.)
     */
    public Condition addReasonReferenceTarget() { 
      Condition r = new Condition();
      if (this.reasonReferenceTarget == null)
        this.reasonReferenceTarget = new ArrayList<Condition>();
      this.reasonReferenceTarget.add(r);
      return r;
    }

    /**
     * @return {@link #dateEnded} (The date (and perhaps time) when the prescription was stopped.). This is the underlying object with id, value and extensions. The accessor "getDateEnded" gives direct access to the value
     */
    public DateTimeType getDateEndedElement() { 
      if (this.dateEnded == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationOrder.dateEnded");
        else if (Configuration.doAutoCreate())
          this.dateEnded = new DateTimeType(); // bb
      return this.dateEnded;
    }

    public boolean hasDateEndedElement() { 
      return this.dateEnded != null && !this.dateEnded.isEmpty();
    }

    public boolean hasDateEnded() { 
      return this.dateEnded != null && !this.dateEnded.isEmpty();
    }

    /**
     * @param value {@link #dateEnded} (The date (and perhaps time) when the prescription was stopped.). This is the underlying object with id, value and extensions. The accessor "getDateEnded" gives direct access to the value
     */
    public MedicationOrder setDateEndedElement(DateTimeType value) { 
      this.dateEnded = value;
      return this;
    }

    /**
     * @return The date (and perhaps time) when the prescription was stopped.
     */
    public Date getDateEnded() { 
      return this.dateEnded == null ? null : this.dateEnded.getValue();
    }

    /**
     * @param value The date (and perhaps time) when the prescription was stopped.
     */
    public MedicationOrder setDateEnded(Date value) { 
      if (value == null)
        this.dateEnded = null;
      else {
        if (this.dateEnded == null)
          this.dateEnded = new DateTimeType();
        this.dateEnded.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #reasonEnded} (The reason why the prescription was stopped, if it was.)
     */
    public CodeableConcept getReasonEnded() { 
      if (this.reasonEnded == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationOrder.reasonEnded");
        else if (Configuration.doAutoCreate())
          this.reasonEnded = new CodeableConcept(); // cc
      return this.reasonEnded;
    }

    public boolean hasReasonEnded() { 
      return this.reasonEnded != null && !this.reasonEnded.isEmpty();
    }

    /**
     * @param value {@link #reasonEnded} (The reason why the prescription was stopped, if it was.)
     */
    public MedicationOrder setReasonEnded(CodeableConcept value) { 
      this.reasonEnded = value;
      return this;
    }

    /**
     * @return {@link #note} (Extra information about the prescription that could not be conveyed by the other attributes.)
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
     * @return {@link #note} (Extra information about the prescription that could not be conveyed by the other attributes.)
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
    public MedicationOrder addNote(Annotation t) { //3
      if (t == null)
        return this;
      if (this.note == null)
        this.note = new ArrayList<Annotation>();
      this.note.add(t);
      return this;
    }

    /**
     * @return {@link #dosageInstruction} (Indicates how the medication is to be used by the patient.)
     */
    public List<MedicationOrderDosageInstructionComponent> getDosageInstruction() { 
      if (this.dosageInstruction == null)
        this.dosageInstruction = new ArrayList<MedicationOrderDosageInstructionComponent>();
      return this.dosageInstruction;
    }

    public boolean hasDosageInstruction() { 
      if (this.dosageInstruction == null)
        return false;
      for (MedicationOrderDosageInstructionComponent item : this.dosageInstruction)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #dosageInstruction} (Indicates how the medication is to be used by the patient.)
     */
    // syntactic sugar
    public MedicationOrderDosageInstructionComponent addDosageInstruction() { //3
      MedicationOrderDosageInstructionComponent t = new MedicationOrderDosageInstructionComponent();
      if (this.dosageInstruction == null)
        this.dosageInstruction = new ArrayList<MedicationOrderDosageInstructionComponent>();
      this.dosageInstruction.add(t);
      return t;
    }

    // syntactic sugar
    public MedicationOrder addDosageInstruction(MedicationOrderDosageInstructionComponent t) { //3
      if (t == null)
        return this;
      if (this.dosageInstruction == null)
        this.dosageInstruction = new ArrayList<MedicationOrderDosageInstructionComponent>();
      this.dosageInstruction.add(t);
      return this;
    }

    /**
     * @return {@link #dispenseRequest} (Indicates the specific details for the dispense or medication supply part of a medication order (also known as a Medication Prescription).  Note that this information is NOT always sent with the order.  There may be in some settings (e.g. hospitals) institutional or system support for completing the dispense details in the pharmacy department.)
     */
    public MedicationOrderDispenseRequestComponent getDispenseRequest() { 
      if (this.dispenseRequest == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationOrder.dispenseRequest");
        else if (Configuration.doAutoCreate())
          this.dispenseRequest = new MedicationOrderDispenseRequestComponent(); // cc
      return this.dispenseRequest;
    }

    public boolean hasDispenseRequest() { 
      return this.dispenseRequest != null && !this.dispenseRequest.isEmpty();
    }

    /**
     * @param value {@link #dispenseRequest} (Indicates the specific details for the dispense or medication supply part of a medication order (also known as a Medication Prescription).  Note that this information is NOT always sent with the order.  There may be in some settings (e.g. hospitals) institutional or system support for completing the dispense details in the pharmacy department.)
     */
    public MedicationOrder setDispenseRequest(MedicationOrderDispenseRequestComponent value) { 
      this.dispenseRequest = value;
      return this;
    }

    /**
     * @return {@link #substitution} (Indicates whether or not substitution can or should be part of the dispense. In some cases substitution must happen, in other cases substitution must not happen, and in others it does not matter. This block explains the prescriber's intent. If nothing is specified substitution may be done.)
     */
    public MedicationOrderSubstitutionComponent getSubstitution() { 
      if (this.substitution == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationOrder.substitution");
        else if (Configuration.doAutoCreate())
          this.substitution = new MedicationOrderSubstitutionComponent(); // cc
      return this.substitution;
    }

    public boolean hasSubstitution() { 
      return this.substitution != null && !this.substitution.isEmpty();
    }

    /**
     * @param value {@link #substitution} (Indicates whether or not substitution can or should be part of the dispense. In some cases substitution must happen, in other cases substitution must not happen, and in others it does not matter. This block explains the prescriber's intent. If nothing is specified substitution may be done.)
     */
    public MedicationOrder setSubstitution(MedicationOrderSubstitutionComponent value) { 
      this.substitution = value;
      return this;
    }

    /**
     * @return {@link #priorPrescription} (A link to a resource representing an earlier order or prescription that this order supersedes.)
     */
    public Reference getPriorPrescription() { 
      if (this.priorPrescription == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationOrder.priorPrescription");
        else if (Configuration.doAutoCreate())
          this.priorPrescription = new Reference(); // cc
      return this.priorPrescription;
    }

    public boolean hasPriorPrescription() { 
      return this.priorPrescription != null && !this.priorPrescription.isEmpty();
    }

    /**
     * @param value {@link #priorPrescription} (A link to a resource representing an earlier order or prescription that this order supersedes.)
     */
    public MedicationOrder setPriorPrescription(Reference value) { 
      this.priorPrescription = value;
      return this;
    }

    /**
     * @return {@link #priorPrescription} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (A link to a resource representing an earlier order or prescription that this order supersedes.)
     */
    public MedicationOrder getPriorPrescriptionTarget() { 
      if (this.priorPrescriptionTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationOrder.priorPrescription");
        else if (Configuration.doAutoCreate())
          this.priorPrescriptionTarget = new MedicationOrder(); // aa
      return this.priorPrescriptionTarget;
    }

    /**
     * @param value {@link #priorPrescription} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (A link to a resource representing an earlier order or prescription that this order supersedes.)
     */
    public MedicationOrder setPriorPrescriptionTarget(MedicationOrder value) { 
      this.priorPrescriptionTarget = value;
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "External identifier - one that would be used by another non-FHIR system - for example a re-imbursement system might issue its own id for each prescription that is created.  This is particularly important where FHIR only provides part of an entire workflow process where records have to be tracked through an entire system.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("status", "code", "A code specifying the state of the order.  Generally this will be active or completed state.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("medication[x]", "CodeableConcept|Reference(Medication)", "Identifies the medication being administered. This is a link to a resource that represents the medication which may be the details of the medication or simply an attribute carrying a code that identifies the medication from a known list of medications.", 0, java.lang.Integer.MAX_VALUE, medication));
        childrenList.add(new Property("patient", "Reference(Patient)", "A link to a resource representing the person to whom the medication will be given.", 0, java.lang.Integer.MAX_VALUE, patient));
        childrenList.add(new Property("encounter", "Reference(Encounter)", "A link to a resource that identifies the particular occurrence of contact between patient and health care provider.", 0, java.lang.Integer.MAX_VALUE, encounter));
        childrenList.add(new Property("dateWritten", "dateTime", "The date (and perhaps time) when the prescription was written.", 0, java.lang.Integer.MAX_VALUE, dateWritten));
        childrenList.add(new Property("prescriber", "Reference(Practitioner)", "The healthcare professional responsible for authorizing the prescription.", 0, java.lang.Integer.MAX_VALUE, prescriber));
        childrenList.add(new Property("reasonCode", "CodeableConcept", "Can be the reason or the indication for writing the prescription.", 0, java.lang.Integer.MAX_VALUE, reasonCode));
        childrenList.add(new Property("reasonReference", "Reference(Condition)", "Condition that supports why the prescription is being written.", 0, java.lang.Integer.MAX_VALUE, reasonReference));
        childrenList.add(new Property("dateEnded", "dateTime", "The date (and perhaps time) when the prescription was stopped.", 0, java.lang.Integer.MAX_VALUE, dateEnded));
        childrenList.add(new Property("reasonEnded", "CodeableConcept", "The reason why the prescription was stopped, if it was.", 0, java.lang.Integer.MAX_VALUE, reasonEnded));
        childrenList.add(new Property("note", "Annotation", "Extra information about the prescription that could not be conveyed by the other attributes.", 0, java.lang.Integer.MAX_VALUE, note));
        childrenList.add(new Property("dosageInstruction", "", "Indicates how the medication is to be used by the patient.", 0, java.lang.Integer.MAX_VALUE, dosageInstruction));
        childrenList.add(new Property("dispenseRequest", "", "Indicates the specific details for the dispense or medication supply part of a medication order (also known as a Medication Prescription).  Note that this information is NOT always sent with the order.  There may be in some settings (e.g. hospitals) institutional or system support for completing the dispense details in the pharmacy department.", 0, java.lang.Integer.MAX_VALUE, dispenseRequest));
        childrenList.add(new Property("substitution", "", "Indicates whether or not substitution can or should be part of the dispense. In some cases substitution must happen, in other cases substitution must not happen, and in others it does not matter. This block explains the prescriber's intent. If nothing is specified substitution may be done.", 0, java.lang.Integer.MAX_VALUE, substitution));
        childrenList.add(new Property("priorPrescription", "Reference(MedicationOrder)", "A link to a resource representing an earlier order or prescription that this order supersedes.", 0, java.lang.Integer.MAX_VALUE, priorPrescription));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<MedicationOrderStatus>
        case 1998965455: /*medication*/ return this.medication == null ? new Base[0] : new Base[] {this.medication}; // Type
        case -791418107: /*patient*/ return this.patient == null ? new Base[0] : new Base[] {this.patient}; // Reference
        case 1524132147: /*encounter*/ return this.encounter == null ? new Base[0] : new Base[] {this.encounter}; // Reference
        case -1496880759: /*dateWritten*/ return this.dateWritten == null ? new Base[0] : new Base[] {this.dateWritten}; // DateTimeType
        case 1430631077: /*prescriber*/ return this.prescriber == null ? new Base[0] : new Base[] {this.prescriber}; // Reference
        case 722137681: /*reasonCode*/ return this.reasonCode == null ? new Base[0] : this.reasonCode.toArray(new Base[this.reasonCode.size()]); // CodeableConcept
        case -1146218137: /*reasonReference*/ return this.reasonReference == null ? new Base[0] : this.reasonReference.toArray(new Base[this.reasonReference.size()]); // Reference
        case -273053780: /*dateEnded*/ return this.dateEnded == null ? new Base[0] : new Base[] {this.dateEnded}; // DateTimeType
        case 913248982: /*reasonEnded*/ return this.reasonEnded == null ? new Base[0] : new Base[] {this.reasonEnded}; // CodeableConcept
        case 3387378: /*note*/ return this.note == null ? new Base[0] : this.note.toArray(new Base[this.note.size()]); // Annotation
        case -1201373865: /*dosageInstruction*/ return this.dosageInstruction == null ? new Base[0] : this.dosageInstruction.toArray(new Base[this.dosageInstruction.size()]); // MedicationOrderDosageInstructionComponent
        case 824620658: /*dispenseRequest*/ return this.dispenseRequest == null ? new Base[0] : new Base[] {this.dispenseRequest}; // MedicationOrderDispenseRequestComponent
        case 826147581: /*substitution*/ return this.substitution == null ? new Base[0] : new Base[] {this.substitution}; // MedicationOrderSubstitutionComponent
        case -486355964: /*priorPrescription*/ return this.priorPrescription == null ? new Base[0] : new Base[] {this.priorPrescription}; // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.getIdentifier().add(castToIdentifier(value)); // Identifier
          break;
        case -892481550: // status
          this.status = new MedicationOrderStatusEnumFactory().fromType(value); // Enumeration<MedicationOrderStatus>
          break;
        case 1998965455: // medication
          this.medication = (Type) value; // Type
          break;
        case -791418107: // patient
          this.patient = castToReference(value); // Reference
          break;
        case 1524132147: // encounter
          this.encounter = castToReference(value); // Reference
          break;
        case -1496880759: // dateWritten
          this.dateWritten = castToDateTime(value); // DateTimeType
          break;
        case 1430631077: // prescriber
          this.prescriber = castToReference(value); // Reference
          break;
        case 722137681: // reasonCode
          this.getReasonCode().add(castToCodeableConcept(value)); // CodeableConcept
          break;
        case -1146218137: // reasonReference
          this.getReasonReference().add(castToReference(value)); // Reference
          break;
        case -273053780: // dateEnded
          this.dateEnded = castToDateTime(value); // DateTimeType
          break;
        case 913248982: // reasonEnded
          this.reasonEnded = castToCodeableConcept(value); // CodeableConcept
          break;
        case 3387378: // note
          this.getNote().add(castToAnnotation(value)); // Annotation
          break;
        case -1201373865: // dosageInstruction
          this.getDosageInstruction().add((MedicationOrderDosageInstructionComponent) value); // MedicationOrderDosageInstructionComponent
          break;
        case 824620658: // dispenseRequest
          this.dispenseRequest = (MedicationOrderDispenseRequestComponent) value; // MedicationOrderDispenseRequestComponent
          break;
        case 826147581: // substitution
          this.substitution = (MedicationOrderSubstitutionComponent) value; // MedicationOrderSubstitutionComponent
          break;
        case -486355964: // priorPrescription
          this.priorPrescription = castToReference(value); // Reference
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier"))
          this.getIdentifier().add(castToIdentifier(value));
        else if (name.equals("status"))
          this.status = new MedicationOrderStatusEnumFactory().fromType(value); // Enumeration<MedicationOrderStatus>
        else if (name.equals("medication[x]"))
          this.medication = (Type) value; // Type
        else if (name.equals("patient"))
          this.patient = castToReference(value); // Reference
        else if (name.equals("encounter"))
          this.encounter = castToReference(value); // Reference
        else if (name.equals("dateWritten"))
          this.dateWritten = castToDateTime(value); // DateTimeType
        else if (name.equals("prescriber"))
          this.prescriber = castToReference(value); // Reference
        else if (name.equals("reasonCode"))
          this.getReasonCode().add(castToCodeableConcept(value));
        else if (name.equals("reasonReference"))
          this.getReasonReference().add(castToReference(value));
        else if (name.equals("dateEnded"))
          this.dateEnded = castToDateTime(value); // DateTimeType
        else if (name.equals("reasonEnded"))
          this.reasonEnded = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("note"))
          this.getNote().add(castToAnnotation(value));
        else if (name.equals("dosageInstruction"))
          this.getDosageInstruction().add((MedicationOrderDosageInstructionComponent) value);
        else if (name.equals("dispenseRequest"))
          this.dispenseRequest = (MedicationOrderDispenseRequestComponent) value; // MedicationOrderDispenseRequestComponent
        else if (name.equals("substitution"))
          this.substitution = (MedicationOrderSubstitutionComponent) value; // MedicationOrderSubstitutionComponent
        else if (name.equals("priorPrescription"))
          this.priorPrescription = castToReference(value); // Reference
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return addIdentifier(); // Identifier
        case -892481550: throw new FHIRException("Cannot make property status as it is not a complex type"); // Enumeration<MedicationOrderStatus>
        case 1458402129:  return getMedication(); // Type
        case -791418107:  return getPatient(); // Reference
        case 1524132147:  return getEncounter(); // Reference
        case -1496880759: throw new FHIRException("Cannot make property dateWritten as it is not a complex type"); // DateTimeType
        case 1430631077:  return getPrescriber(); // Reference
        case 722137681:  return addReasonCode(); // CodeableConcept
        case -1146218137:  return addReasonReference(); // Reference
        case -273053780: throw new FHIRException("Cannot make property dateEnded as it is not a complex type"); // DateTimeType
        case 913248982:  return getReasonEnded(); // CodeableConcept
        case 3387378:  return addNote(); // Annotation
        case -1201373865:  return addDosageInstruction(); // MedicationOrderDosageInstructionComponent
        case 824620658:  return getDispenseRequest(); // MedicationOrderDispenseRequestComponent
        case 826147581:  return getSubstitution(); // MedicationOrderSubstitutionComponent
        case -486355964:  return getPriorPrescription(); // Reference
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type MedicationOrder.status");
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
        else if (name.equals("encounter")) {
          this.encounter = new Reference();
          return this.encounter;
        }
        else if (name.equals("dateWritten")) {
          throw new FHIRException("Cannot call addChild on a primitive type MedicationOrder.dateWritten");
        }
        else if (name.equals("prescriber")) {
          this.prescriber = new Reference();
          return this.prescriber;
        }
        else if (name.equals("reasonCode")) {
          return addReasonCode();
        }
        else if (name.equals("reasonReference")) {
          return addReasonReference();
        }
        else if (name.equals("dateEnded")) {
          throw new FHIRException("Cannot call addChild on a primitive type MedicationOrder.dateEnded");
        }
        else if (name.equals("reasonEnded")) {
          this.reasonEnded = new CodeableConcept();
          return this.reasonEnded;
        }
        else if (name.equals("note")) {
          return addNote();
        }
        else if (name.equals("dosageInstruction")) {
          return addDosageInstruction();
        }
        else if (name.equals("dispenseRequest")) {
          this.dispenseRequest = new MedicationOrderDispenseRequestComponent();
          return this.dispenseRequest;
        }
        else if (name.equals("substitution")) {
          this.substitution = new MedicationOrderSubstitutionComponent();
          return this.substitution;
        }
        else if (name.equals("priorPrescription")) {
          this.priorPrescription = new Reference();
          return this.priorPrescription;
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "MedicationOrder";

  }

      public MedicationOrder copy() {
        MedicationOrder dst = new MedicationOrder();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.status = status == null ? null : status.copy();
        dst.medication = medication == null ? null : medication.copy();
        dst.patient = patient == null ? null : patient.copy();
        dst.encounter = encounter == null ? null : encounter.copy();
        dst.dateWritten = dateWritten == null ? null : dateWritten.copy();
        dst.prescriber = prescriber == null ? null : prescriber.copy();
        if (reasonCode != null) {
          dst.reasonCode = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : reasonCode)
            dst.reasonCode.add(i.copy());
        };
        if (reasonReference != null) {
          dst.reasonReference = new ArrayList<Reference>();
          for (Reference i : reasonReference)
            dst.reasonReference.add(i.copy());
        };
        dst.dateEnded = dateEnded == null ? null : dateEnded.copy();
        dst.reasonEnded = reasonEnded == null ? null : reasonEnded.copy();
        if (note != null) {
          dst.note = new ArrayList<Annotation>();
          for (Annotation i : note)
            dst.note.add(i.copy());
        };
        if (dosageInstruction != null) {
          dst.dosageInstruction = new ArrayList<MedicationOrderDosageInstructionComponent>();
          for (MedicationOrderDosageInstructionComponent i : dosageInstruction)
            dst.dosageInstruction.add(i.copy());
        };
        dst.dispenseRequest = dispenseRequest == null ? null : dispenseRequest.copy();
        dst.substitution = substitution == null ? null : substitution.copy();
        dst.priorPrescription = priorPrescription == null ? null : priorPrescription.copy();
        return dst;
      }

      protected MedicationOrder typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof MedicationOrder))
          return false;
        MedicationOrder o = (MedicationOrder) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(status, o.status, true) && compareDeep(medication, o.medication, true)
           && compareDeep(patient, o.patient, true) && compareDeep(encounter, o.encounter, true) && compareDeep(dateWritten, o.dateWritten, true)
           && compareDeep(prescriber, o.prescriber, true) && compareDeep(reasonCode, o.reasonCode, true) && compareDeep(reasonReference, o.reasonReference, true)
           && compareDeep(dateEnded, o.dateEnded, true) && compareDeep(reasonEnded, o.reasonEnded, true) && compareDeep(note, o.note, true)
           && compareDeep(dosageInstruction, o.dosageInstruction, true) && compareDeep(dispenseRequest, o.dispenseRequest, true)
           && compareDeep(substitution, o.substitution, true) && compareDeep(priorPrescription, o.priorPrescription, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof MedicationOrder))
          return false;
        MedicationOrder o = (MedicationOrder) other;
        return compareValues(status, o.status, true) && compareValues(dateWritten, o.dateWritten, true) && compareValues(dateEnded, o.dateEnded, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (status == null || status.isEmpty())
           && (medication == null || medication.isEmpty()) && (patient == null || patient.isEmpty())
           && (encounter == null || encounter.isEmpty()) && (dateWritten == null || dateWritten.isEmpty())
           && (prescriber == null || prescriber.isEmpty()) && (reasonCode == null || reasonCode.isEmpty())
           && (reasonReference == null || reasonReference.isEmpty()) && (dateEnded == null || dateEnded.isEmpty())
           && (reasonEnded == null || reasonEnded.isEmpty()) && (note == null || note.isEmpty()) && (dosageInstruction == null || dosageInstruction.isEmpty())
           && (dispenseRequest == null || dispenseRequest.isEmpty()) && (substitution == null || substitution.isEmpty())
           && (priorPrescription == null || priorPrescription.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.MedicationOrder;
   }

 /**
   * Search parameter: <b>medication</b>
   * <p>
   * Description: <b>Return administrations of this medication reference</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MedicationOrder.medicationReference</b><br>
   * </p>
   */
  @SearchParamDefinition(name="medication", path="MedicationOrder.medication.as(Reference)", description="Return administrations of this medication reference", type="reference" )
  public static final String SP_MEDICATION = "medication";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>medication</b>
   * <p>
   * Description: <b>Return administrations of this medication reference</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MedicationOrder.medicationReference</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam MEDICATION = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_MEDICATION);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>MedicationOrder:medication</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_MEDICATION = new ca.uhn.fhir.model.api.Include("MedicationOrder:medication").toLocked();

 /**
   * Search parameter: <b>datewritten</b>
   * <p>
   * Description: <b>Return prescriptions written on this date</b><br>
   * Type: <b>date</b><br>
   * Path: <b>MedicationOrder.dateWritten</b><br>
   * </p>
   */
  @SearchParamDefinition(name="datewritten", path="MedicationOrder.dateWritten", description="Return prescriptions written on this date", type="date" )
  public static final String SP_DATEWRITTEN = "datewritten";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>datewritten</b>
   * <p>
   * Description: <b>Return prescriptions written on this date</b><br>
   * Type: <b>date</b><br>
   * Path: <b>MedicationOrder.dateWritten</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam DATEWRITTEN = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_DATEWRITTEN);

 /**
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>The identity of a patient to list orders  for</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MedicationOrder.patient</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="MedicationOrder.patient", description="The identity of a patient to list orders  for", type="reference" )
  public static final String SP_PATIENT = "patient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patient</b>
   * <p>
   * Description: <b>The identity of a patient to list orders  for</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MedicationOrder.patient</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PATIENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PATIENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>MedicationOrder:patient</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PATIENT = new ca.uhn.fhir.model.api.Include("MedicationOrder:patient").toLocked();

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>Status of the prescription</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MedicationOrder.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="MedicationOrder.status", description="Status of the prescription", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>Status of the prescription</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MedicationOrder.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);

 /**
   * Search parameter: <b>prescriber</b>
   * <p>
   * Description: <b>Who ordered the medication(s)</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MedicationOrder.prescriber</b><br>
   * </p>
   */
  @SearchParamDefinition(name="prescriber", path="MedicationOrder.prescriber", description="Who ordered the medication(s)", type="reference" )
  public static final String SP_PRESCRIBER = "prescriber";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>prescriber</b>
   * <p>
   * Description: <b>Who ordered the medication(s)</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MedicationOrder.prescriber</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PRESCRIBER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PRESCRIBER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>MedicationOrder:prescriber</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PRESCRIBER = new ca.uhn.fhir.model.api.Include("MedicationOrder:prescriber").toLocked();

 /**
   * Search parameter: <b>code</b>
   * <p>
   * Description: <b>Return administrations of this medication code</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MedicationOrder.medicationCodeableConcept</b><br>
   * </p>
   */
  @SearchParamDefinition(name="code", path="MedicationOrder.medication.as(CodeableConcept)", description="Return administrations of this medication code", type="token" )
  public static final String SP_CODE = "code";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>code</b>
   * <p>
   * Description: <b>Return administrations of this medication code</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MedicationOrder.medicationCodeableConcept</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CODE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CODE);

 /**
   * Search parameter: <b>encounter</b>
   * <p>
   * Description: <b>Return prescriptions with this encounter identifier</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MedicationOrder.encounter</b><br>
   * </p>
   */
  @SearchParamDefinition(name="encounter", path="MedicationOrder.encounter", description="Return prescriptions with this encounter identifier", type="reference" )
  public static final String SP_ENCOUNTER = "encounter";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>encounter</b>
   * <p>
   * Description: <b>Return prescriptions with this encounter identifier</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MedicationOrder.encounter</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam ENCOUNTER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_ENCOUNTER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>MedicationOrder:encounter</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_ENCOUNTER = new ca.uhn.fhir.model.api.Include("MedicationOrder:encounter").toLocked();

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>Return prescriptions with this external identifier</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MedicationOrder.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="MedicationOrder.identifier", description="Return prescriptions with this external identifier", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>Return prescriptions with this external identifier</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MedicationOrder.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);


}

