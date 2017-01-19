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
 * A record of a medication that is being consumed by a patient.   A MedicationStatement may indicate that the patient may be taking the medication now, or has taken the medication in the past or will be taking the medication in the future.  The source of this information can be the patient, significant other (such as a family member or spouse), or a clinician.  A common scenario where this information is captured is during the history taking process during a patient visit or stay.   The medication information may come from e.g. the patient's memory, from a prescription bottle,  or from a list of medications the patient, clinician or other party maintains The primary difference between a medication statement and a medication administration is that the medication administration has complete administration information and is based on actual administration information from the person who administered the medication.  A medication statement is often, if not always, less specific.  There is no required date/time when the medication was administered, in fact we only know that a source has reported the patient is taking this medication, where details such as time, quantity, or rate or even medication product may be incomplete or missing or less precise.  As stated earlier, the medication statement information may come from the patient's memory, from a prescription bottle or from a list of medications the patient, clinician or other party maintains.  Medication administration is more formal and is not missing detailed information.
 */
@ResourceDef(name="MedicationStatement", profile="http://hl7.org/fhir/Profile/MedicationStatement")
public class MedicationStatement extends DomainResource {

    public enum MedicationStatementStatus {
        /**
         * The medication is still being taken.
         */
        ACTIVE, 
        /**
         * The medication is no longer being taken.
         */
        COMPLETED, 
        /**
         * The statement was entered in error.
         */
        ENTEREDINERROR, 
        /**
         * The medication may be taken at some time in the future.
         */
        INTENDED, 
        /**
         * added to help the parsers
         */
        NULL;
        public static MedicationStatementStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("active".equals(codeString))
          return ACTIVE;
        if ("completed".equals(codeString))
          return COMPLETED;
        if ("entered-in-error".equals(codeString))
          return ENTEREDINERROR;
        if ("intended".equals(codeString))
          return INTENDED;
        throw new FHIRException("Unknown MedicationStatementStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ACTIVE: return "active";
            case COMPLETED: return "completed";
            case ENTEREDINERROR: return "entered-in-error";
            case INTENDED: return "intended";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case ACTIVE: return "http://hl7.org/fhir/medication-statement-status";
            case COMPLETED: return "http://hl7.org/fhir/medication-statement-status";
            case ENTEREDINERROR: return "http://hl7.org/fhir/medication-statement-status";
            case INTENDED: return "http://hl7.org/fhir/medication-statement-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case ACTIVE: return "The medication is still being taken.";
            case COMPLETED: return "The medication is no longer being taken.";
            case ENTEREDINERROR: return "The statement was entered in error.";
            case INTENDED: return "The medication may be taken at some time in the future.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ACTIVE: return "Active";
            case COMPLETED: return "Completed";
            case ENTEREDINERROR: return "Entered in Error";
            case INTENDED: return "Intended";
            default: return "?";
          }
        }
    }

  public static class MedicationStatementStatusEnumFactory implements EnumFactory<MedicationStatementStatus> {
    public MedicationStatementStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("active".equals(codeString))
          return MedicationStatementStatus.ACTIVE;
        if ("completed".equals(codeString))
          return MedicationStatementStatus.COMPLETED;
        if ("entered-in-error".equals(codeString))
          return MedicationStatementStatus.ENTEREDINERROR;
        if ("intended".equals(codeString))
          return MedicationStatementStatus.INTENDED;
        throw new IllegalArgumentException("Unknown MedicationStatementStatus code '"+codeString+"'");
        }
        public Enumeration<MedicationStatementStatus> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("active".equals(codeString))
          return new Enumeration<MedicationStatementStatus>(this, MedicationStatementStatus.ACTIVE);
        if ("completed".equals(codeString))
          return new Enumeration<MedicationStatementStatus>(this, MedicationStatementStatus.COMPLETED);
        if ("entered-in-error".equals(codeString))
          return new Enumeration<MedicationStatementStatus>(this, MedicationStatementStatus.ENTEREDINERROR);
        if ("intended".equals(codeString))
          return new Enumeration<MedicationStatementStatus>(this, MedicationStatementStatus.INTENDED);
        throw new FHIRException("Unknown MedicationStatementStatus code '"+codeString+"'");
        }
    public String toCode(MedicationStatementStatus code) {
      if (code == MedicationStatementStatus.ACTIVE)
        return "active";
      if (code == MedicationStatementStatus.COMPLETED)
        return "completed";
      if (code == MedicationStatementStatus.ENTEREDINERROR)
        return "entered-in-error";
      if (code == MedicationStatementStatus.INTENDED)
        return "intended";
      return "?";
      }
    public String toSystem(MedicationStatementStatus code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class MedicationStatementDosageComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Free text dosage information as reported about a patient's medication use. When coded dosage information is present, the free text may still be present for display to humans.
         */
        @Child(name = "text", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Free text dosage instructions as reported by the information source", formalDefinition="Free text dosage information as reported about a patient's medication use. When coded dosage information is present, the free text may still be present for display to humans." )
        protected StringType text;

        /**
         * The timing schedule for giving the medication to the patient.  The Schedule data type allows many different expressions, for example.  "Every  8 hours"; "Three times a day"; "1/2 an hour before breakfast for 10 days from 23-Dec 2011:";  "15 Oct 2013, 17 Oct 2013 and 1 Nov 2013".
         */
        @Child(name = "timing", type = {Timing.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="When/how often was medication taken", formalDefinition="The timing schedule for giving the medication to the patient.  The Schedule data type allows many different expressions, for example.  \"Every  8 hours\"; \"Three times a day\"; \"1/2 an hour before breakfast for 10 days from 23-Dec 2011:\";  \"15 Oct 2013, 17 Oct 2013 and 1 Nov 2013\"." )
        protected Timing timing;

        /**
         * Indicates whether the Medication is only taken when needed within a specific dosing schedule (Boolean option), or it indicates the precondition for taking the Medication (CodeableConcept).  

Specifically if 'boolean' datatype is selected, then the following logic applies:  If set to True, this indicates that the medication is only taken when needed, within the specified schedule.
         */
        @Child(name = "asNeeded", type = {BooleanType.class, CodeableConcept.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Take \"as needed\" (for x)", formalDefinition="Indicates whether the Medication is only taken when needed within a specific dosing schedule (Boolean option), or it indicates the precondition for taking the Medication (CodeableConcept).  \n\nSpecifically if 'boolean' datatype is selected, then the following logic applies:  If set to True, this indicates that the medication is only taken when needed, within the specified schedule." )
        protected Type asNeeded;

        /**
         * A coded specification of or a reference to the anatomic site where the medication first enters the body.
         */
        @Child(name = "site", type = {CodeableConcept.class, BodySite.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Where (on body) medication is/was administered", formalDefinition="A coded specification of or a reference to the anatomic site where the medication first enters the body." )
        protected Type site;

        /**
         * A code specifying the route or physiological path of administration of a therapeutic agent into or onto a subject.
         */
        @Child(name = "route", type = {CodeableConcept.class}, order=5, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="How the medication entered the body", formalDefinition="A code specifying the route or physiological path of administration of a therapeutic agent into or onto a subject." )
        protected CodeableConcept route;

        /**
         * A coded value indicating the method by which the medication is intended to be or was introduced into or on the body.  This attribute will most often NOT be populated.  It is most commonly used for injections.  For example, Slow Push, Deep IV.
         */
        @Child(name = "method", type = {CodeableConcept.class}, order=6, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Technique used to administer medication", formalDefinition="A coded value indicating the method by which the medication is intended to be or was introduced into or on the body.  This attribute will most often NOT be populated.  It is most commonly used for injections.  For example, Slow Push, Deep IV." )
        protected CodeableConcept method;

        /**
         * The amount of therapeutic or other substance given at one administration event.
         */
        @Child(name = "quantity", type = {SimpleQuantity.class, Range.class}, order=7, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Amount administered in one dose", formalDefinition="The amount of therapeutic or other substance given at one administration event." )
        protected Type quantity;

        /**
         * Identifies the speed with which the medication was or will be introduced into the patient. Typically the rate for an infusion e.g. 100 ml per 1 hour or 100 ml/hr.  May also be expressed as a rate per unit of time e.g. 500 ml per 2 hours.   Currently we do not specify a default of '1' in the denominator, but this is being discussed. Other examples: 200 mcg/min or 200 mcg/1 minute; 1 liter/8 hours.
         */
        @Child(name = "rate", type = {Ratio.class, Range.class}, order=8, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Dose quantity per unit of time", formalDefinition="Identifies the speed with which the medication was or will be introduced into the patient. Typically the rate for an infusion e.g. 100 ml per 1 hour or 100 ml/hr.  May also be expressed as a rate per unit of time e.g. 500 ml per 2 hours.   Currently we do not specify a default of '1' in the denominator, but this is being discussed. Other examples: 200 mcg/min or 200 mcg/1 minute; 1 liter/8 hours." )
        protected Type rate;

        /**
         * The maximum total quantity of a therapeutic substance that may be administered to a subject over the period of time.  For example, 1000mg in 24 hours.
         */
        @Child(name = "maxDosePerPeriod", type = {Ratio.class}, order=9, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Maximum dose that was consumed per unit of time", formalDefinition="The maximum total quantity of a therapeutic substance that may be administered to a subject over the period of time.  For example, 1000mg in 24 hours." )
        protected Ratio maxDosePerPeriod;

        private static final long serialVersionUID = 246880733L;

    /**
     * Constructor
     */
      public MedicationStatementDosageComponent() {
        super();
      }

        /**
         * @return {@link #text} (Free text dosage information as reported about a patient's medication use. When coded dosage information is present, the free text may still be present for display to humans.). This is the underlying object with id, value and extensions. The accessor "getText" gives direct access to the value
         */
        public StringType getTextElement() { 
          if (this.text == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationStatementDosageComponent.text");
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
         * @param value {@link #text} (Free text dosage information as reported about a patient's medication use. When coded dosage information is present, the free text may still be present for display to humans.). This is the underlying object with id, value and extensions. The accessor "getText" gives direct access to the value
         */
        public MedicationStatementDosageComponent setTextElement(StringType value) { 
          this.text = value;
          return this;
        }

        /**
         * @return Free text dosage information as reported about a patient's medication use. When coded dosage information is present, the free text may still be present for display to humans.
         */
        public String getText() { 
          return this.text == null ? null : this.text.getValue();
        }

        /**
         * @param value Free text dosage information as reported about a patient's medication use. When coded dosage information is present, the free text may still be present for display to humans.
         */
        public MedicationStatementDosageComponent setText(String value) { 
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
         * @return {@link #timing} (The timing schedule for giving the medication to the patient.  The Schedule data type allows many different expressions, for example.  "Every  8 hours"; "Three times a day"; "1/2 an hour before breakfast for 10 days from 23-Dec 2011:";  "15 Oct 2013, 17 Oct 2013 and 1 Nov 2013".)
         */
        public Timing getTiming() { 
          if (this.timing == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationStatementDosageComponent.timing");
            else if (Configuration.doAutoCreate())
              this.timing = new Timing(); // cc
          return this.timing;
        }

        public boolean hasTiming() { 
          return this.timing != null && !this.timing.isEmpty();
        }

        /**
         * @param value {@link #timing} (The timing schedule for giving the medication to the patient.  The Schedule data type allows many different expressions, for example.  "Every  8 hours"; "Three times a day"; "1/2 an hour before breakfast for 10 days from 23-Dec 2011:";  "15 Oct 2013, 17 Oct 2013 and 1 Nov 2013".)
         */
        public MedicationStatementDosageComponent setTiming(Timing value) { 
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
        public MedicationStatementDosageComponent setAsNeeded(Type value) { 
          this.asNeeded = value;
          return this;
        }

        /**
         * @return {@link #site} (A coded specification of or a reference to the anatomic site where the medication first enters the body.)
         */
        public Type getSite() { 
          return this.site;
        }

        /**
         * @return {@link #site} (A coded specification of or a reference to the anatomic site where the medication first enters the body.)
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
         * @return {@link #site} (A coded specification of or a reference to the anatomic site where the medication first enters the body.)
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
         * @param value {@link #site} (A coded specification of or a reference to the anatomic site where the medication first enters the body.)
         */
        public MedicationStatementDosageComponent setSite(Type value) { 
          this.site = value;
          return this;
        }

        /**
         * @return {@link #route} (A code specifying the route or physiological path of administration of a therapeutic agent into or onto a subject.)
         */
        public CodeableConcept getRoute() { 
          if (this.route == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationStatementDosageComponent.route");
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
        public MedicationStatementDosageComponent setRoute(CodeableConcept value) { 
          this.route = value;
          return this;
        }

        /**
         * @return {@link #method} (A coded value indicating the method by which the medication is intended to be or was introduced into or on the body.  This attribute will most often NOT be populated.  It is most commonly used for injections.  For example, Slow Push, Deep IV.)
         */
        public CodeableConcept getMethod() { 
          if (this.method == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationStatementDosageComponent.method");
            else if (Configuration.doAutoCreate())
              this.method = new CodeableConcept(); // cc
          return this.method;
        }

        public boolean hasMethod() { 
          return this.method != null && !this.method.isEmpty();
        }

        /**
         * @param value {@link #method} (A coded value indicating the method by which the medication is intended to be or was introduced into or on the body.  This attribute will most often NOT be populated.  It is most commonly used for injections.  For example, Slow Push, Deep IV.)
         */
        public MedicationStatementDosageComponent setMethod(CodeableConcept value) { 
          this.method = value;
          return this;
        }

        /**
         * @return {@link #quantity} (The amount of therapeutic or other substance given at one administration event.)
         */
        public Type getQuantity() { 
          return this.quantity;
        }

        /**
         * @return {@link #quantity} (The amount of therapeutic or other substance given at one administration event.)
         */
        public SimpleQuantity getQuantitySimpleQuantity() throws FHIRException { 
          if (!(this.quantity instanceof SimpleQuantity))
            throw new FHIRException("Type mismatch: the type SimpleQuantity was expected, but "+this.quantity.getClass().getName()+" was encountered");
          return (SimpleQuantity) this.quantity;
        }

        public boolean hasQuantitySimpleQuantity() { 
          return this.quantity instanceof SimpleQuantity;
        }

        /**
         * @return {@link #quantity} (The amount of therapeutic or other substance given at one administration event.)
         */
        public Range getQuantityRange() throws FHIRException { 
          if (!(this.quantity instanceof Range))
            throw new FHIRException("Type mismatch: the type Range was expected, but "+this.quantity.getClass().getName()+" was encountered");
          return (Range) this.quantity;
        }

        public boolean hasQuantityRange() { 
          return this.quantity instanceof Range;
        }

        public boolean hasQuantity() { 
          return this.quantity != null && !this.quantity.isEmpty();
        }

        /**
         * @param value {@link #quantity} (The amount of therapeutic or other substance given at one administration event.)
         */
        public MedicationStatementDosageComponent setQuantity(Type value) { 
          this.quantity = value;
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
        public MedicationStatementDosageComponent setRate(Type value) { 
          this.rate = value;
          return this;
        }

        /**
         * @return {@link #maxDosePerPeriod} (The maximum total quantity of a therapeutic substance that may be administered to a subject over the period of time.  For example, 1000mg in 24 hours.)
         */
        public Ratio getMaxDosePerPeriod() { 
          if (this.maxDosePerPeriod == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationStatementDosageComponent.maxDosePerPeriod");
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
        public MedicationStatementDosageComponent setMaxDosePerPeriod(Ratio value) { 
          this.maxDosePerPeriod = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("text", "string", "Free text dosage information as reported about a patient's medication use. When coded dosage information is present, the free text may still be present for display to humans.", 0, java.lang.Integer.MAX_VALUE, text));
          childrenList.add(new Property("timing", "Timing", "The timing schedule for giving the medication to the patient.  The Schedule data type allows many different expressions, for example.  \"Every  8 hours\"; \"Three times a day\"; \"1/2 an hour before breakfast for 10 days from 23-Dec 2011:\";  \"15 Oct 2013, 17 Oct 2013 and 1 Nov 2013\".", 0, java.lang.Integer.MAX_VALUE, timing));
          childrenList.add(new Property("asNeeded[x]", "boolean|CodeableConcept", "Indicates whether the Medication is only taken when needed within a specific dosing schedule (Boolean option), or it indicates the precondition for taking the Medication (CodeableConcept).  \n\nSpecifically if 'boolean' datatype is selected, then the following logic applies:  If set to True, this indicates that the medication is only taken when needed, within the specified schedule.", 0, java.lang.Integer.MAX_VALUE, asNeeded));
          childrenList.add(new Property("site[x]", "CodeableConcept|Reference(BodySite)", "A coded specification of or a reference to the anatomic site where the medication first enters the body.", 0, java.lang.Integer.MAX_VALUE, site));
          childrenList.add(new Property("route", "CodeableConcept", "A code specifying the route or physiological path of administration of a therapeutic agent into or onto a subject.", 0, java.lang.Integer.MAX_VALUE, route));
          childrenList.add(new Property("method", "CodeableConcept", "A coded value indicating the method by which the medication is intended to be or was introduced into or on the body.  This attribute will most often NOT be populated.  It is most commonly used for injections.  For example, Slow Push, Deep IV.", 0, java.lang.Integer.MAX_VALUE, method));
          childrenList.add(new Property("quantity[x]", "SimpleQuantity|Range", "The amount of therapeutic or other substance given at one administration event.", 0, java.lang.Integer.MAX_VALUE, quantity));
          childrenList.add(new Property("rate[x]", "Ratio|Range", "Identifies the speed with which the medication was or will be introduced into the patient. Typically the rate for an infusion e.g. 100 ml per 1 hour or 100 ml/hr.  May also be expressed as a rate per unit of time e.g. 500 ml per 2 hours.   Currently we do not specify a default of '1' in the denominator, but this is being discussed. Other examples: 200 mcg/min or 200 mcg/1 minute; 1 liter/8 hours.", 0, java.lang.Integer.MAX_VALUE, rate));
          childrenList.add(new Property("maxDosePerPeriod", "Ratio", "The maximum total quantity of a therapeutic substance that may be administered to a subject over the period of time.  For example, 1000mg in 24 hours.", 0, java.lang.Integer.MAX_VALUE, maxDosePerPeriod));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3556653: /*text*/ return this.text == null ? new Base[0] : new Base[] {this.text}; // StringType
        case -873664438: /*timing*/ return this.timing == null ? new Base[0] : new Base[] {this.timing}; // Timing
        case -1432923513: /*asNeeded*/ return this.asNeeded == null ? new Base[0] : new Base[] {this.asNeeded}; // Type
        case 3530567: /*site*/ return this.site == null ? new Base[0] : new Base[] {this.site}; // Type
        case 108704329: /*route*/ return this.route == null ? new Base[0] : new Base[] {this.route}; // CodeableConcept
        case -1077554975: /*method*/ return this.method == null ? new Base[0] : new Base[] {this.method}; // CodeableConcept
        case -1285004149: /*quantity*/ return this.quantity == null ? new Base[0] : new Base[] {this.quantity}; // Type
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
        case -1285004149: // quantity
          this.quantity = (Type) value; // Type
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
        else if (name.equals("quantity[x]"))
          this.quantity = (Type) value; // Type
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
        case -873664438:  return getTiming(); // Timing
        case -544329575:  return getAsNeeded(); // Type
        case 2099997657:  return getSite(); // Type
        case 108704329:  return getRoute(); // CodeableConcept
        case -1077554975:  return getMethod(); // CodeableConcept
        case -515002347:  return getQuantity(); // Type
        case 983460768:  return getRate(); // Type
        case 1506263709:  return getMaxDosePerPeriod(); // Ratio
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("text")) {
          throw new FHIRException("Cannot call addChild on a primitive type MedicationStatement.text");
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
        else if (name.equals("quantitySimpleQuantity")) {
          this.quantity = new SimpleQuantity();
          return this.quantity;
        }
        else if (name.equals("quantityRange")) {
          this.quantity = new Range();
          return this.quantity;
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

      public MedicationStatementDosageComponent copy() {
        MedicationStatementDosageComponent dst = new MedicationStatementDosageComponent();
        copyValues(dst);
        dst.text = text == null ? null : text.copy();
        dst.timing = timing == null ? null : timing.copy();
        dst.asNeeded = asNeeded == null ? null : asNeeded.copy();
        dst.site = site == null ? null : site.copy();
        dst.route = route == null ? null : route.copy();
        dst.method = method == null ? null : method.copy();
        dst.quantity = quantity == null ? null : quantity.copy();
        dst.rate = rate == null ? null : rate.copy();
        dst.maxDosePerPeriod = maxDosePerPeriod == null ? null : maxDosePerPeriod.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof MedicationStatementDosageComponent))
          return false;
        MedicationStatementDosageComponent o = (MedicationStatementDosageComponent) other;
        return compareDeep(text, o.text, true) && compareDeep(timing, o.timing, true) && compareDeep(asNeeded, o.asNeeded, true)
           && compareDeep(site, o.site, true) && compareDeep(route, o.route, true) && compareDeep(method, o.method, true)
           && compareDeep(quantity, o.quantity, true) && compareDeep(rate, o.rate, true) && compareDeep(maxDosePerPeriod, o.maxDosePerPeriod, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof MedicationStatementDosageComponent))
          return false;
        MedicationStatementDosageComponent o = (MedicationStatementDosageComponent) other;
        return compareValues(text, o.text, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (text == null || text.isEmpty()) && (timing == null || timing.isEmpty())
           && (asNeeded == null || asNeeded.isEmpty()) && (site == null || site.isEmpty()) && (route == null || route.isEmpty())
           && (method == null || method.isEmpty()) && (quantity == null || quantity.isEmpty()) && (rate == null || rate.isEmpty())
           && (maxDosePerPeriod == null || maxDosePerPeriod.isEmpty());
      }

  public String fhirType() {
    return "MedicationStatement.dosage";

  }

  }

    /**
     * External identifier - FHIR will generate its own internal identifiers (probably URLs) which do not need to be explicitly managed by the resource.  The identifier here is one that would be used by another non-FHIR system - for example an automated medication pump would provide a record each time it operated; an administration while the patient was off the ward might be made with a different system and entered after the event.  Particularly important if these records have to be updated.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="External identifier", formalDefinition="External identifier - FHIR will generate its own internal identifiers (probably URLs) which do not need to be explicitly managed by the resource.  The identifier here is one that would be used by another non-FHIR system - for example an automated medication pump would provide a record each time it operated; an administration while the patient was off the ward might be made with a different system and entered after the event.  Particularly important if these records have to be updated." )
    protected List<Identifier> identifier;

    /**
     * A code representing the patient or other source's judgment about the state of the medication used that this statement is about.  Generally this will be active or completed.
     */
    @Child(name = "status", type = {CodeType.class}, order=1, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="active | completed | entered-in-error | intended", formalDefinition="A code representing the patient or other source's judgment about the state of the medication used that this statement is about.  Generally this will be active or completed." )
    protected Enumeration<MedicationStatementStatus> status;

    /**
     * Identifies the medication being administered. This is either a link to a resource representing the details of the medication or a simple attribute carrying a code that identifies the medication from a known list of medications.
     */
    @Child(name = "medication", type = {CodeableConcept.class, Medication.class}, order=2, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="What medication was taken", formalDefinition="Identifies the medication being administered. This is either a link to a resource representing the details of the medication or a simple attribute carrying a code that identifies the medication from a known list of medications." )
    protected Type medication;

    /**
     * The person or animal who is/was taking the medication.
     */
    @Child(name = "patient", type = {Patient.class}, order=3, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Who is/was taking  the medication", formalDefinition="The person or animal who is/was taking the medication." )
    protected Reference patient;

    /**
     * The actual object that is the target of the reference (The person or animal who is/was taking the medication.)
     */
    protected Patient patientTarget;

    /**
     * The interval of time during which it is being asserted that the patient was taking the medication (or was not taking, when the wasNotGiven element is true).
     */
    @Child(name = "effective", type = {DateTimeType.class, Period.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Over what period was medication consumed?", formalDefinition="The interval of time during which it is being asserted that the patient was taking the medication (or was not taking, when the wasNotGiven element is true)." )
    protected Type effective;

    /**
     * The person who provided the information about the taking of this medication.  Note:  A MedicationStatement may be derived from supportingInformation e.g claims or medicationOrder.
     */
    @Child(name = "informationSource", type = {Patient.class, Practitioner.class, RelatedPerson.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Person who provided the information about the taking of this medication", formalDefinition="The person who provided the information about the taking of this medication.  Note:  A MedicationStatement may be derived from supportingInformation e.g claims or medicationOrder." )
    protected Reference informationSource;

    /**
     * The actual object that is the target of the reference (The person who provided the information about the taking of this medication.  Note:  A MedicationStatement may be derived from supportingInformation e.g claims or medicationOrder.)
     */
    protected Resource informationSourceTarget;

    /**
     * Allows linking the MedicationStatement to the underlying MedicationOrder, or to other information that supports or is used to derive the MedicationStatement.
     */
    @Child(name = "supportingInformation", type = {}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Additional supporting information", formalDefinition="Allows linking the MedicationStatement to the underlying MedicationOrder, or to other information that supports or is used to derive the MedicationStatement." )
    protected List<Reference> supportingInformation;
    /**
     * The actual objects that are the target of the reference (Allows linking the MedicationStatement to the underlying MedicationOrder, or to other information that supports or is used to derive the MedicationStatement.)
     */
    protected List<Resource> supportingInformationTarget;


    /**
     * The date when the medication statement was asserted by the information source.
     */
    @Child(name = "dateAsserted", type = {DateTimeType.class}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When the statement was asserted?", formalDefinition="The date when the medication statement was asserted by the information source." )
    protected DateTimeType dateAsserted;

    /**
     * Set this to true if the record is saying that the medication was NOT taken.
     */
    @Child(name = "wasNotTaken", type = {BooleanType.class}, order=8, min=0, max=1, modifier=true, summary=true)
    @Description(shortDefinition="True if medication is/was not being taken", formalDefinition="Set this to true if the record is saying that the medication was NOT taken." )
    protected BooleanType wasNotTaken;

    /**
     * A code indicating why the medication was not taken.
     */
    @Child(name = "reasonNotTaken", type = {CodeableConcept.class}, order=9, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="True if asserting medication was not given", formalDefinition="A code indicating why the medication was not taken." )
    protected List<CodeableConcept> reasonNotTaken;

    /**
     * A reason for why the medication is being/was taken.
     */
    @Child(name = "reasonForUse", type = {CodeableConcept.class, Condition.class}, order=10, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="", formalDefinition="A reason for why the medication is being/was taken." )
    protected Type reasonForUse;

    /**
     * Provides extra information about the medication statement that is not conveyed by the other attributes.
     */
    @Child(name = "note", type = {Annotation.class}, order=11, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Further information about the statement", formalDefinition="Provides extra information about the medication statement that is not conveyed by the other attributes." )
    protected List<Annotation> note;

    /**
     * Indicates how the medication is/was used by the patient.
     */
    @Child(name = "dosage", type = {}, order=12, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Details of how medication was taken", formalDefinition="Indicates how the medication is/was used by the patient." )
    protected List<MedicationStatementDosageComponent> dosage;

    private static final long serialVersionUID = -425948910L;

  /**
   * Constructor
   */
    public MedicationStatement() {
      super();
    }

  /**
   * Constructor
   */
    public MedicationStatement(Enumeration<MedicationStatementStatus> status, Type medication, Reference patient) {
      super();
      this.status = status;
      this.medication = medication;
      this.patient = patient;
    }

    /**
     * @return {@link #identifier} (External identifier - FHIR will generate its own internal identifiers (probably URLs) which do not need to be explicitly managed by the resource.  The identifier here is one that would be used by another non-FHIR system - for example an automated medication pump would provide a record each time it operated; an administration while the patient was off the ward might be made with a different system and entered after the event.  Particularly important if these records have to be updated.)
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
     * @return {@link #identifier} (External identifier - FHIR will generate its own internal identifiers (probably URLs) which do not need to be explicitly managed by the resource.  The identifier here is one that would be used by another non-FHIR system - for example an automated medication pump would provide a record each time it operated; an administration while the patient was off the ward might be made with a different system and entered after the event.  Particularly important if these records have to be updated.)
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
    public MedicationStatement addIdentifier(Identifier t) { //3
      if (t == null)
        return this;
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return this;
    }

    /**
     * @return {@link #status} (A code representing the patient or other source's judgment about the state of the medication used that this statement is about.  Generally this will be active or completed.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<MedicationStatementStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationStatement.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<MedicationStatementStatus>(new MedicationStatementStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (A code representing the patient or other source's judgment about the state of the medication used that this statement is about.  Generally this will be active or completed.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public MedicationStatement setStatusElement(Enumeration<MedicationStatementStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return A code representing the patient or other source's judgment about the state of the medication used that this statement is about.  Generally this will be active or completed.
     */
    public MedicationStatementStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value A code representing the patient or other source's judgment about the state of the medication used that this statement is about.  Generally this will be active or completed.
     */
    public MedicationStatement setStatus(MedicationStatementStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<MedicationStatementStatus>(new MedicationStatementStatusEnumFactory());
        this.status.setValue(value);
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
    public MedicationStatement setMedication(Type value) { 
      this.medication = value;
      return this;
    }

    /**
     * @return {@link #patient} (The person or animal who is/was taking the medication.)
     */
    public Reference getPatient() { 
      if (this.patient == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationStatement.patient");
        else if (Configuration.doAutoCreate())
          this.patient = new Reference(); // cc
      return this.patient;
    }

    public boolean hasPatient() { 
      return this.patient != null && !this.patient.isEmpty();
    }

    /**
     * @param value {@link #patient} (The person or animal who is/was taking the medication.)
     */
    public MedicationStatement setPatient(Reference value) { 
      this.patient = value;
      return this;
    }

    /**
     * @return {@link #patient} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The person or animal who is/was taking the medication.)
     */
    public Patient getPatientTarget() { 
      if (this.patientTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationStatement.patient");
        else if (Configuration.doAutoCreate())
          this.patientTarget = new Patient(); // aa
      return this.patientTarget;
    }

    /**
     * @param value {@link #patient} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The person or animal who is/was taking the medication.)
     */
    public MedicationStatement setPatientTarget(Patient value) { 
      this.patientTarget = value;
      return this;
    }

    /**
     * @return {@link #effective} (The interval of time during which it is being asserted that the patient was taking the medication (or was not taking, when the wasNotGiven element is true).)
     */
    public Type getEffective() { 
      return this.effective;
    }

    /**
     * @return {@link #effective} (The interval of time during which it is being asserted that the patient was taking the medication (or was not taking, when the wasNotGiven element is true).)
     */
    public DateTimeType getEffectiveDateTimeType() throws FHIRException { 
      if (!(this.effective instanceof DateTimeType))
        throw new FHIRException("Type mismatch: the type DateTimeType was expected, but "+this.effective.getClass().getName()+" was encountered");
      return (DateTimeType) this.effective;
    }

    public boolean hasEffectiveDateTimeType() { 
      return this.effective instanceof DateTimeType;
    }

    /**
     * @return {@link #effective} (The interval of time during which it is being asserted that the patient was taking the medication (or was not taking, when the wasNotGiven element is true).)
     */
    public Period getEffectivePeriod() throws FHIRException { 
      if (!(this.effective instanceof Period))
        throw new FHIRException("Type mismatch: the type Period was expected, but "+this.effective.getClass().getName()+" was encountered");
      return (Period) this.effective;
    }

    public boolean hasEffectivePeriod() { 
      return this.effective instanceof Period;
    }

    public boolean hasEffective() { 
      return this.effective != null && !this.effective.isEmpty();
    }

    /**
     * @param value {@link #effective} (The interval of time during which it is being asserted that the patient was taking the medication (or was not taking, when the wasNotGiven element is true).)
     */
    public MedicationStatement setEffective(Type value) { 
      this.effective = value;
      return this;
    }

    /**
     * @return {@link #informationSource} (The person who provided the information about the taking of this medication.  Note:  A MedicationStatement may be derived from supportingInformation e.g claims or medicationOrder.)
     */
    public Reference getInformationSource() { 
      if (this.informationSource == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationStatement.informationSource");
        else if (Configuration.doAutoCreate())
          this.informationSource = new Reference(); // cc
      return this.informationSource;
    }

    public boolean hasInformationSource() { 
      return this.informationSource != null && !this.informationSource.isEmpty();
    }

    /**
     * @param value {@link #informationSource} (The person who provided the information about the taking of this medication.  Note:  A MedicationStatement may be derived from supportingInformation e.g claims or medicationOrder.)
     */
    public MedicationStatement setInformationSource(Reference value) { 
      this.informationSource = value;
      return this;
    }

    /**
     * @return {@link #informationSource} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The person who provided the information about the taking of this medication.  Note:  A MedicationStatement may be derived from supportingInformation e.g claims or medicationOrder.)
     */
    public Resource getInformationSourceTarget() { 
      return this.informationSourceTarget;
    }

    /**
     * @param value {@link #informationSource} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The person who provided the information about the taking of this medication.  Note:  A MedicationStatement may be derived from supportingInformation e.g claims or medicationOrder.)
     */
    public MedicationStatement setInformationSourceTarget(Resource value) { 
      this.informationSourceTarget = value;
      return this;
    }

    /**
     * @return {@link #supportingInformation} (Allows linking the MedicationStatement to the underlying MedicationOrder, or to other information that supports or is used to derive the MedicationStatement.)
     */
    public List<Reference> getSupportingInformation() { 
      if (this.supportingInformation == null)
        this.supportingInformation = new ArrayList<Reference>();
      return this.supportingInformation;
    }

    public boolean hasSupportingInformation() { 
      if (this.supportingInformation == null)
        return false;
      for (Reference item : this.supportingInformation)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #supportingInformation} (Allows linking the MedicationStatement to the underlying MedicationOrder, or to other information that supports or is used to derive the MedicationStatement.)
     */
    // syntactic sugar
    public Reference addSupportingInformation() { //3
      Reference t = new Reference();
      if (this.supportingInformation == null)
        this.supportingInformation = new ArrayList<Reference>();
      this.supportingInformation.add(t);
      return t;
    }

    // syntactic sugar
    public MedicationStatement addSupportingInformation(Reference t) { //3
      if (t == null)
        return this;
      if (this.supportingInformation == null)
        this.supportingInformation = new ArrayList<Reference>();
      this.supportingInformation.add(t);
      return this;
    }

    /**
     * @return {@link #supportingInformation} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. Allows linking the MedicationStatement to the underlying MedicationOrder, or to other information that supports or is used to derive the MedicationStatement.)
     */
    public List<Resource> getSupportingInformationTarget() { 
      if (this.supportingInformationTarget == null)
        this.supportingInformationTarget = new ArrayList<Resource>();
      return this.supportingInformationTarget;
    }

    /**
     * @return {@link #dateAsserted} (The date when the medication statement was asserted by the information source.). This is the underlying object with id, value and extensions. The accessor "getDateAsserted" gives direct access to the value
     */
    public DateTimeType getDateAssertedElement() { 
      if (this.dateAsserted == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationStatement.dateAsserted");
        else if (Configuration.doAutoCreate())
          this.dateAsserted = new DateTimeType(); // bb
      return this.dateAsserted;
    }

    public boolean hasDateAssertedElement() { 
      return this.dateAsserted != null && !this.dateAsserted.isEmpty();
    }

    public boolean hasDateAsserted() { 
      return this.dateAsserted != null && !this.dateAsserted.isEmpty();
    }

    /**
     * @param value {@link #dateAsserted} (The date when the medication statement was asserted by the information source.). This is the underlying object with id, value and extensions. The accessor "getDateAsserted" gives direct access to the value
     */
    public MedicationStatement setDateAssertedElement(DateTimeType value) { 
      this.dateAsserted = value;
      return this;
    }

    /**
     * @return The date when the medication statement was asserted by the information source.
     */
    public Date getDateAsserted() { 
      return this.dateAsserted == null ? null : this.dateAsserted.getValue();
    }

    /**
     * @param value The date when the medication statement was asserted by the information source.
     */
    public MedicationStatement setDateAsserted(Date value) { 
      if (value == null)
        this.dateAsserted = null;
      else {
        if (this.dateAsserted == null)
          this.dateAsserted = new DateTimeType();
        this.dateAsserted.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #wasNotTaken} (Set this to true if the record is saying that the medication was NOT taken.). This is the underlying object with id, value and extensions. The accessor "getWasNotTaken" gives direct access to the value
     */
    public BooleanType getWasNotTakenElement() { 
      if (this.wasNotTaken == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationStatement.wasNotTaken");
        else if (Configuration.doAutoCreate())
          this.wasNotTaken = new BooleanType(); // bb
      return this.wasNotTaken;
    }

    public boolean hasWasNotTakenElement() { 
      return this.wasNotTaken != null && !this.wasNotTaken.isEmpty();
    }

    public boolean hasWasNotTaken() { 
      return this.wasNotTaken != null && !this.wasNotTaken.isEmpty();
    }

    /**
     * @param value {@link #wasNotTaken} (Set this to true if the record is saying that the medication was NOT taken.). This is the underlying object with id, value and extensions. The accessor "getWasNotTaken" gives direct access to the value
     */
    public MedicationStatement setWasNotTakenElement(BooleanType value) { 
      this.wasNotTaken = value;
      return this;
    }

    /**
     * @return Set this to true if the record is saying that the medication was NOT taken.
     */
    public boolean getWasNotTaken() { 
      return this.wasNotTaken == null || this.wasNotTaken.isEmpty() ? false : this.wasNotTaken.getValue();
    }

    /**
     * @param value Set this to true if the record is saying that the medication was NOT taken.
     */
    public MedicationStatement setWasNotTaken(boolean value) { 
        if (this.wasNotTaken == null)
          this.wasNotTaken = new BooleanType();
        this.wasNotTaken.setValue(value);
      return this;
    }

    /**
     * @return {@link #reasonNotTaken} (A code indicating why the medication was not taken.)
     */
    public List<CodeableConcept> getReasonNotTaken() { 
      if (this.reasonNotTaken == null)
        this.reasonNotTaken = new ArrayList<CodeableConcept>();
      return this.reasonNotTaken;
    }

    public boolean hasReasonNotTaken() { 
      if (this.reasonNotTaken == null)
        return false;
      for (CodeableConcept item : this.reasonNotTaken)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #reasonNotTaken} (A code indicating why the medication was not taken.)
     */
    // syntactic sugar
    public CodeableConcept addReasonNotTaken() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.reasonNotTaken == null)
        this.reasonNotTaken = new ArrayList<CodeableConcept>();
      this.reasonNotTaken.add(t);
      return t;
    }

    // syntactic sugar
    public MedicationStatement addReasonNotTaken(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.reasonNotTaken == null)
        this.reasonNotTaken = new ArrayList<CodeableConcept>();
      this.reasonNotTaken.add(t);
      return this;
    }

    /**
     * @return {@link #reasonForUse} (A reason for why the medication is being/was taken.)
     */
    public Type getReasonForUse() { 
      return this.reasonForUse;
    }

    /**
     * @return {@link #reasonForUse} (A reason for why the medication is being/was taken.)
     */
    public CodeableConcept getReasonForUseCodeableConcept() throws FHIRException { 
      if (!(this.reasonForUse instanceof CodeableConcept))
        throw new FHIRException("Type mismatch: the type CodeableConcept was expected, but "+this.reasonForUse.getClass().getName()+" was encountered");
      return (CodeableConcept) this.reasonForUse;
    }

    public boolean hasReasonForUseCodeableConcept() { 
      return this.reasonForUse instanceof CodeableConcept;
    }

    /**
     * @return {@link #reasonForUse} (A reason for why the medication is being/was taken.)
     */
    public Reference getReasonForUseReference() throws FHIRException { 
      if (!(this.reasonForUse instanceof Reference))
        throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.reasonForUse.getClass().getName()+" was encountered");
      return (Reference) this.reasonForUse;
    }

    public boolean hasReasonForUseReference() { 
      return this.reasonForUse instanceof Reference;
    }

    public boolean hasReasonForUse() { 
      return this.reasonForUse != null && !this.reasonForUse.isEmpty();
    }

    /**
     * @param value {@link #reasonForUse} (A reason for why the medication is being/was taken.)
     */
    public MedicationStatement setReasonForUse(Type value) { 
      this.reasonForUse = value;
      return this;
    }

    /**
     * @return {@link #note} (Provides extra information about the medication statement that is not conveyed by the other attributes.)
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
     * @return {@link #note} (Provides extra information about the medication statement that is not conveyed by the other attributes.)
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
    public MedicationStatement addNote(Annotation t) { //3
      if (t == null)
        return this;
      if (this.note == null)
        this.note = new ArrayList<Annotation>();
      this.note.add(t);
      return this;
    }

    /**
     * @return {@link #dosage} (Indicates how the medication is/was used by the patient.)
     */
    public List<MedicationStatementDosageComponent> getDosage() { 
      if (this.dosage == null)
        this.dosage = new ArrayList<MedicationStatementDosageComponent>();
      return this.dosage;
    }

    public boolean hasDosage() { 
      if (this.dosage == null)
        return false;
      for (MedicationStatementDosageComponent item : this.dosage)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #dosage} (Indicates how the medication is/was used by the patient.)
     */
    // syntactic sugar
    public MedicationStatementDosageComponent addDosage() { //3
      MedicationStatementDosageComponent t = new MedicationStatementDosageComponent();
      if (this.dosage == null)
        this.dosage = new ArrayList<MedicationStatementDosageComponent>();
      this.dosage.add(t);
      return t;
    }

    // syntactic sugar
    public MedicationStatement addDosage(MedicationStatementDosageComponent t) { //3
      if (t == null)
        return this;
      if (this.dosage == null)
        this.dosage = new ArrayList<MedicationStatementDosageComponent>();
      this.dosage.add(t);
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "External identifier - FHIR will generate its own internal identifiers (probably URLs) which do not need to be explicitly managed by the resource.  The identifier here is one that would be used by another non-FHIR system - for example an automated medication pump would provide a record each time it operated; an administration while the patient was off the ward might be made with a different system and entered after the event.  Particularly important if these records have to be updated.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("status", "code", "A code representing the patient or other source's judgment about the state of the medication used that this statement is about.  Generally this will be active or completed.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("medication[x]", "CodeableConcept|Reference(Medication)", "Identifies the medication being administered. This is either a link to a resource representing the details of the medication or a simple attribute carrying a code that identifies the medication from a known list of medications.", 0, java.lang.Integer.MAX_VALUE, medication));
        childrenList.add(new Property("patient", "Reference(Patient)", "The person or animal who is/was taking the medication.", 0, java.lang.Integer.MAX_VALUE, patient));
        childrenList.add(new Property("effective[x]", "dateTime|Period", "The interval of time during which it is being asserted that the patient was taking the medication (or was not taking, when the wasNotGiven element is true).", 0, java.lang.Integer.MAX_VALUE, effective));
        childrenList.add(new Property("informationSource", "Reference(Patient|Practitioner|RelatedPerson)", "The person who provided the information about the taking of this medication.  Note:  A MedicationStatement may be derived from supportingInformation e.g claims or medicationOrder.", 0, java.lang.Integer.MAX_VALUE, informationSource));
        childrenList.add(new Property("supportingInformation", "Reference(Any)", "Allows linking the MedicationStatement to the underlying MedicationOrder, or to other information that supports or is used to derive the MedicationStatement.", 0, java.lang.Integer.MAX_VALUE, supportingInformation));
        childrenList.add(new Property("dateAsserted", "dateTime", "The date when the medication statement was asserted by the information source.", 0, java.lang.Integer.MAX_VALUE, dateAsserted));
        childrenList.add(new Property("wasNotTaken", "boolean", "Set this to true if the record is saying that the medication was NOT taken.", 0, java.lang.Integer.MAX_VALUE, wasNotTaken));
        childrenList.add(new Property("reasonNotTaken", "CodeableConcept", "A code indicating why the medication was not taken.", 0, java.lang.Integer.MAX_VALUE, reasonNotTaken));
        childrenList.add(new Property("reasonForUse[x]", "CodeableConcept|Reference(Condition)", "A reason for why the medication is being/was taken.", 0, java.lang.Integer.MAX_VALUE, reasonForUse));
        childrenList.add(new Property("note", "Annotation", "Provides extra information about the medication statement that is not conveyed by the other attributes.", 0, java.lang.Integer.MAX_VALUE, note));
        childrenList.add(new Property("dosage", "", "Indicates how the medication is/was used by the patient.", 0, java.lang.Integer.MAX_VALUE, dosage));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<MedicationStatementStatus>
        case 1998965455: /*medication*/ return this.medication == null ? new Base[0] : new Base[] {this.medication}; // Type
        case -791418107: /*patient*/ return this.patient == null ? new Base[0] : new Base[] {this.patient}; // Reference
        case -1468651097: /*effective*/ return this.effective == null ? new Base[0] : new Base[] {this.effective}; // Type
        case -2123220889: /*informationSource*/ return this.informationSource == null ? new Base[0] : new Base[] {this.informationSource}; // Reference
        case -1248768647: /*supportingInformation*/ return this.supportingInformation == null ? new Base[0] : this.supportingInformation.toArray(new Base[this.supportingInformation.size()]); // Reference
        case -1980855245: /*dateAsserted*/ return this.dateAsserted == null ? new Base[0] : new Base[] {this.dateAsserted}; // DateTimeType
        case -1039154243: /*wasNotTaken*/ return this.wasNotTaken == null ? new Base[0] : new Base[] {this.wasNotTaken}; // BooleanType
        case 2112880664: /*reasonNotTaken*/ return this.reasonNotTaken == null ? new Base[0] : this.reasonNotTaken.toArray(new Base[this.reasonNotTaken.size()]); // CodeableConcept
        case -1724097694: /*reasonForUse*/ return this.reasonForUse == null ? new Base[0] : new Base[] {this.reasonForUse}; // Type
        case 3387378: /*note*/ return this.note == null ? new Base[0] : this.note.toArray(new Base[this.note.size()]); // Annotation
        case -1326018889: /*dosage*/ return this.dosage == null ? new Base[0] : this.dosage.toArray(new Base[this.dosage.size()]); // MedicationStatementDosageComponent
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
          this.status = new MedicationStatementStatusEnumFactory().fromType(value); // Enumeration<MedicationStatementStatus>
          break;
        case 1998965455: // medication
          this.medication = (Type) value; // Type
          break;
        case -791418107: // patient
          this.patient = castToReference(value); // Reference
          break;
        case -1468651097: // effective
          this.effective = (Type) value; // Type
          break;
        case -2123220889: // informationSource
          this.informationSource = castToReference(value); // Reference
          break;
        case -1248768647: // supportingInformation
          this.getSupportingInformation().add(castToReference(value)); // Reference
          break;
        case -1980855245: // dateAsserted
          this.dateAsserted = castToDateTime(value); // DateTimeType
          break;
        case -1039154243: // wasNotTaken
          this.wasNotTaken = castToBoolean(value); // BooleanType
          break;
        case 2112880664: // reasonNotTaken
          this.getReasonNotTaken().add(castToCodeableConcept(value)); // CodeableConcept
          break;
        case -1724097694: // reasonForUse
          this.reasonForUse = (Type) value; // Type
          break;
        case 3387378: // note
          this.getNote().add(castToAnnotation(value)); // Annotation
          break;
        case -1326018889: // dosage
          this.getDosage().add((MedicationStatementDosageComponent) value); // MedicationStatementDosageComponent
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier"))
          this.getIdentifier().add(castToIdentifier(value));
        else if (name.equals("status"))
          this.status = new MedicationStatementStatusEnumFactory().fromType(value); // Enumeration<MedicationStatementStatus>
        else if (name.equals("medication[x]"))
          this.medication = (Type) value; // Type
        else if (name.equals("patient"))
          this.patient = castToReference(value); // Reference
        else if (name.equals("effective[x]"))
          this.effective = (Type) value; // Type
        else if (name.equals("informationSource"))
          this.informationSource = castToReference(value); // Reference
        else if (name.equals("supportingInformation"))
          this.getSupportingInformation().add(castToReference(value));
        else if (name.equals("dateAsserted"))
          this.dateAsserted = castToDateTime(value); // DateTimeType
        else if (name.equals("wasNotTaken"))
          this.wasNotTaken = castToBoolean(value); // BooleanType
        else if (name.equals("reasonNotTaken"))
          this.getReasonNotTaken().add(castToCodeableConcept(value));
        else if (name.equals("reasonForUse[x]"))
          this.reasonForUse = (Type) value; // Type
        else if (name.equals("note"))
          this.getNote().add(castToAnnotation(value));
        else if (name.equals("dosage"))
          this.getDosage().add((MedicationStatementDosageComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return addIdentifier(); // Identifier
        case -892481550: throw new FHIRException("Cannot make property status as it is not a complex type"); // Enumeration<MedicationStatementStatus>
        case 1458402129:  return getMedication(); // Type
        case -791418107:  return getPatient(); // Reference
        case 247104889:  return getEffective(); // Type
        case -2123220889:  return getInformationSource(); // Reference
        case -1248768647:  return addSupportingInformation(); // Reference
        case -1980855245: throw new FHIRException("Cannot make property dateAsserted as it is not a complex type"); // DateTimeType
        case -1039154243: throw new FHIRException("Cannot make property wasNotTaken as it is not a complex type"); // BooleanType
        case 2112880664:  return addReasonNotTaken(); // CodeableConcept
        case 919582174:  return getReasonForUse(); // Type
        case 3387378:  return addNote(); // Annotation
        case -1326018889:  return addDosage(); // MedicationStatementDosageComponent
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type MedicationStatement.status");
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
        else if (name.equals("effectiveDateTime")) {
          this.effective = new DateTimeType();
          return this.effective;
        }
        else if (name.equals("effectivePeriod")) {
          this.effective = new Period();
          return this.effective;
        }
        else if (name.equals("informationSource")) {
          this.informationSource = new Reference();
          return this.informationSource;
        }
        else if (name.equals("supportingInformation")) {
          return addSupportingInformation();
        }
        else if (name.equals("dateAsserted")) {
          throw new FHIRException("Cannot call addChild on a primitive type MedicationStatement.dateAsserted");
        }
        else if (name.equals("wasNotTaken")) {
          throw new FHIRException("Cannot call addChild on a primitive type MedicationStatement.wasNotTaken");
        }
        else if (name.equals("reasonNotTaken")) {
          return addReasonNotTaken();
        }
        else if (name.equals("reasonForUseCodeableConcept")) {
          this.reasonForUse = new CodeableConcept();
          return this.reasonForUse;
        }
        else if (name.equals("reasonForUseReference")) {
          this.reasonForUse = new Reference();
          return this.reasonForUse;
        }
        else if (name.equals("note")) {
          return addNote();
        }
        else if (name.equals("dosage")) {
          return addDosage();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "MedicationStatement";

  }

      public MedicationStatement copy() {
        MedicationStatement dst = new MedicationStatement();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.status = status == null ? null : status.copy();
        dst.medication = medication == null ? null : medication.copy();
        dst.patient = patient == null ? null : patient.copy();
        dst.effective = effective == null ? null : effective.copy();
        dst.informationSource = informationSource == null ? null : informationSource.copy();
        if (supportingInformation != null) {
          dst.supportingInformation = new ArrayList<Reference>();
          for (Reference i : supportingInformation)
            dst.supportingInformation.add(i.copy());
        };
        dst.dateAsserted = dateAsserted == null ? null : dateAsserted.copy();
        dst.wasNotTaken = wasNotTaken == null ? null : wasNotTaken.copy();
        if (reasonNotTaken != null) {
          dst.reasonNotTaken = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : reasonNotTaken)
            dst.reasonNotTaken.add(i.copy());
        };
        dst.reasonForUse = reasonForUse == null ? null : reasonForUse.copy();
        if (note != null) {
          dst.note = new ArrayList<Annotation>();
          for (Annotation i : note)
            dst.note.add(i.copy());
        };
        if (dosage != null) {
          dst.dosage = new ArrayList<MedicationStatementDosageComponent>();
          for (MedicationStatementDosageComponent i : dosage)
            dst.dosage.add(i.copy());
        };
        return dst;
      }

      protected MedicationStatement typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof MedicationStatement))
          return false;
        MedicationStatement o = (MedicationStatement) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(status, o.status, true) && compareDeep(medication, o.medication, true)
           && compareDeep(patient, o.patient, true) && compareDeep(effective, o.effective, true) && compareDeep(informationSource, o.informationSource, true)
           && compareDeep(supportingInformation, o.supportingInformation, true) && compareDeep(dateAsserted, o.dateAsserted, true)
           && compareDeep(wasNotTaken, o.wasNotTaken, true) && compareDeep(reasonNotTaken, o.reasonNotTaken, true)
           && compareDeep(reasonForUse, o.reasonForUse, true) && compareDeep(note, o.note, true) && compareDeep(dosage, o.dosage, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof MedicationStatement))
          return false;
        MedicationStatement o = (MedicationStatement) other;
        return compareValues(status, o.status, true) && compareValues(dateAsserted, o.dateAsserted, true) && compareValues(wasNotTaken, o.wasNotTaken, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (status == null || status.isEmpty())
           && (medication == null || medication.isEmpty()) && (patient == null || patient.isEmpty())
           && (effective == null || effective.isEmpty()) && (informationSource == null || informationSource.isEmpty())
           && (supportingInformation == null || supportingInformation.isEmpty()) && (dateAsserted == null || dateAsserted.isEmpty())
           && (wasNotTaken == null || wasNotTaken.isEmpty()) && (reasonNotTaken == null || reasonNotTaken.isEmpty())
           && (reasonForUse == null || reasonForUse.isEmpty()) && (note == null || note.isEmpty()) && (dosage == null || dosage.isEmpty())
          ;
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.MedicationStatement;
   }

 /**
   * Search parameter: <b>medication</b>
   * <p>
   * Description: <b>Return administrations of this medication reference</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MedicationStatement.medicationReference</b><br>
   * </p>
   */
  @SearchParamDefinition(name="medication", path="MedicationStatement.medication.as(Reference)", description="Return administrations of this medication reference", type="reference" )
  public static final String SP_MEDICATION = "medication";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>medication</b>
   * <p>
   * Description: <b>Return administrations of this medication reference</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MedicationStatement.medicationReference</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam MEDICATION = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_MEDICATION);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>MedicationStatement:medication</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_MEDICATION = new ca.uhn.fhir.model.api.Include("MedicationStatement:medication").toLocked();

 /**
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>The identity of a patient to list statements  for</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MedicationStatement.patient</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="MedicationStatement.patient", description="The identity of a patient to list statements  for", type="reference" )
  public static final String SP_PATIENT = "patient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patient</b>
   * <p>
   * Description: <b>The identity of a patient to list statements  for</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MedicationStatement.patient</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PATIENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PATIENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>MedicationStatement:patient</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PATIENT = new ca.uhn.fhir.model.api.Include("MedicationStatement:patient").toLocked();

 /**
   * Search parameter: <b>source</b>
   * <p>
   * Description: <b>Who the information in the statement came from</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MedicationStatement.informationSource</b><br>
   * </p>
   */
  @SearchParamDefinition(name="source", path="MedicationStatement.informationSource", description="Who the information in the statement came from", type="reference" )
  public static final String SP_SOURCE = "source";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>source</b>
   * <p>
   * Description: <b>Who the information in the statement came from</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MedicationStatement.informationSource</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SOURCE = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SOURCE);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>MedicationStatement:source</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SOURCE = new ca.uhn.fhir.model.api.Include("MedicationStatement:source").toLocked();

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>Return statements that match the given status</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MedicationStatement.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="MedicationStatement.status", description="Return statements that match the given status", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>Return statements that match the given status</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MedicationStatement.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);

 /**
   * Search parameter: <b>code</b>
   * <p>
   * Description: <b>Return administrations of this medication code</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MedicationStatement.medicationCodeableConcept</b><br>
   * </p>
   */
  @SearchParamDefinition(name="code", path="MedicationStatement.medication.as(CodeableConcept)", description="Return administrations of this medication code", type="token" )
  public static final String SP_CODE = "code";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>code</b>
   * <p>
   * Description: <b>Return administrations of this medication code</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MedicationStatement.medicationCodeableConcept</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CODE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CODE);

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>Return statements with this external identifier</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MedicationStatement.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="MedicationStatement.identifier", description="Return statements with this external identifier", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>Return statements with this external identifier</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MedicationStatement.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>effective</b>
   * <p>
   * Description: <b>Date when patient was taking (or not taking) the medication</b><br>
   * Type: <b>date</b><br>
   * Path: <b>MedicationStatement.effective[x]</b><br>
   * </p>
   */
  @SearchParamDefinition(name="effective", path="MedicationStatement.effective", description="Date when patient was taking (or not taking) the medication", type="date" )
  public static final String SP_EFFECTIVE = "effective";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>effective</b>
   * <p>
   * Description: <b>Date when patient was taking (or not taking) the medication</b><br>
   * Type: <b>date</b><br>
   * Path: <b>MedicationStatement.effective[x]</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam EFFECTIVE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_EFFECTIVE);


}

