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
 * A record of medication being taken by a patient, or that the medication has been given to a patient where the record is the result of a report from the patient or another clinician.
 */
@ResourceDef(name="MedicationStatement", profile="http://hl7.org/fhir/Profile/MedicationStatement")
public class MedicationStatement extends DomainResource {

    public enum MedicationStatementStatus {
        /**
         * The medication is still being taken.
         */
        INPROGRESS, 
        /**
         * All actions that are implied by the statement have occurred.
         */
        COMPLETED, 
        /**
         * The statement was entered in error and therefore nullified.
         */
        ENTEREDINERROR, 
        /**
         * added to help the parsers
         */
        NULL;
        public static MedicationStatementStatus fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("in-progress".equals(codeString))
          return INPROGRESS;
        if ("completed".equals(codeString))
          return COMPLETED;
        if ("entered-in-error".equals(codeString))
          return ENTEREDINERROR;
        throw new Exception("Unknown MedicationStatementStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case INPROGRESS: return "in-progress";
            case COMPLETED: return "completed";
            case ENTEREDINERROR: return "entered-in-error";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case INPROGRESS: return "";
            case COMPLETED: return "";
            case ENTEREDINERROR: return "";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case INPROGRESS: return "The medication is still being taken.";
            case COMPLETED: return "All actions that are implied by the statement have occurred.";
            case ENTEREDINERROR: return "The statement was entered in error and therefore nullified.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case INPROGRESS: return "in-progress";
            case COMPLETED: return "completed";
            case ENTEREDINERROR: return "entered-in-error";
            default: return "?";
          }
        }
    }

  public static class MedicationStatementStatusEnumFactory implements EnumFactory<MedicationStatementStatus> {
    public MedicationStatementStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("in-progress".equals(codeString))
          return MedicationStatementStatus.INPROGRESS;
        if ("completed".equals(codeString))
          return MedicationStatementStatus.COMPLETED;
        if ("entered-in-error".equals(codeString))
          return MedicationStatementStatus.ENTEREDINERROR;
        throw new IllegalArgumentException("Unknown MedicationStatementStatus code '"+codeString+"'");
        }
    public String toCode(MedicationStatementStatus code) {
      if (code == MedicationStatementStatus.INPROGRESS)
        return "in-progress";
      if (code == MedicationStatementStatus.COMPLETED)
        return "completed";
      if (code == MedicationStatementStatus.ENTEREDINERROR)
        return "entered-in-error";
      return "?";
      }
    }

    @Block()
    public static class MedicationStatementDosageComponent extends BackboneElement {
        /**
         * Free text dosage instructions can be used for cases where the instructions are too complex to code. When coded instructions are present, the free text instructions may still be present for display to humans taking or administering the medication.
         */
        @Child(name="text", type={StringType.class}, order=1, min=0, max=1)
        @Description(shortDefinition="Dosage Instructions", formalDefinition="Free text dosage instructions can be used for cases where the instructions are too complex to code. When coded instructions are present, the free text instructions may still be present for display to humans taking or administering the medication." )
        protected StringType text;

        /**
         * The timing schedule for giving the medication to the patient.  The Schedule data type allows many different expressions, for example.  "Every  8 hours"; "Three times a day"; "1/2 an hour before breakfast for 10 days from 23-Dec 2011:";  "15 Oct 2013, 17 Oct 2013 and 1 Nov 2013".
         */
        @Child(name="schedule", type={Timing.class}, order=2, min=0, max=1)
        @Description(shortDefinition="When/how often was medication taken?", formalDefinition="The timing schedule for giving the medication to the patient.  The Schedule data type allows many different expressions, for example.  'Every  8 hours'; 'Three times a day'; '1/2 an hour before breakfast for 10 days from 23-Dec 2011:';  '15 Oct 2013, 17 Oct 2013 and 1 Nov 2013'." )
        protected Timing schedule;

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
        @Description(shortDefinition="Where on body was medication administered?", formalDefinition="A coded specification of the anatomic site where the medication first enters the body." )
        protected CodeableConcept site;

        /**
         * A code specifying the route or physiological path of administration of a therapeutic agent into or onto a subject.
         */
        @Child(name="route", type={CodeableConcept.class}, order=5, min=0, max=1)
        @Description(shortDefinition="How did the medication enter the body?", formalDefinition="A code specifying the route or physiological path of administration of a therapeutic agent into or onto a subject." )
        protected CodeableConcept route;

        /**
         * A coded value indicating the method by which the medication is introduced into or onto the body. Most commonly used for injections.  Examples:  Slow Push; Deep IV.

Terminologies used often pre-coordinate this term with the route and or form of administration.
         */
        @Child(name="method", type={CodeableConcept.class}, order=6, min=0, max=1)
        @Description(shortDefinition="Technique used to administer medication", formalDefinition="A coded value indicating the method by which the medication is introduced into or onto the body. Most commonly used for injections.  Examples:  Slow Push; Deep IV.\r\rTerminologies used often pre-coordinate this term with the route and or form of administration." )
        protected CodeableConcept method;

        /**
         * The amount of therapeutic or other substance given at one administration event.
         */
        @Child(name="quantity", type={Quantity.class}, order=7, min=0, max=1)
        @Description(shortDefinition="Amount administered in one dose", formalDefinition="The amount of therapeutic or other substance given at one administration event." )
        protected Quantity quantity;

        /**
         * Identifies the speed with which the substance is introduced into the subject. Typically the rate for an infusion. 200ml in 2 hours.
         */
        @Child(name="rate", type={Ratio.class}, order=8, min=0, max=1)
        @Description(shortDefinition="Dose quantity per unit of time", formalDefinition="Identifies the speed with which the substance is introduced into the subject. Typically the rate for an infusion. 200ml in 2 hours." )
        protected Ratio rate;

        /**
         * The maximum total quantity of a therapeutic substance that may be administered to a subject over the period of time. E.g. 1000mg in 24 hours.
         */
        @Child(name="maxDosePerPeriod", type={Ratio.class}, order=9, min=0, max=1)
        @Description(shortDefinition="Maximum dose that was consumed per unit of time", formalDefinition="The maximum total quantity of a therapeutic substance that may be administered to a subject over the period of time. E.g. 1000mg in 24 hours." )
        protected Ratio maxDosePerPeriod;

        private static final long serialVersionUID = 1729854997L;

      public MedicationStatementDosageComponent() {
        super();
      }

        /**
         * @return {@link #text} (Free text dosage instructions can be used for cases where the instructions are too complex to code. When coded instructions are present, the free text instructions may still be present for display to humans taking or administering the medication.). This is the underlying object with id, value and extensions. The accessor "getText" gives direct access to the value
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
         * @param value {@link #text} (Free text dosage instructions can be used for cases where the instructions are too complex to code. When coded instructions are present, the free text instructions may still be present for display to humans taking or administering the medication.). This is the underlying object with id, value and extensions. The accessor "getText" gives direct access to the value
         */
        public MedicationStatementDosageComponent setTextElement(StringType value) { 
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
         * @return {@link #schedule} (The timing schedule for giving the medication to the patient.  The Schedule data type allows many different expressions, for example.  "Every  8 hours"; "Three times a day"; "1/2 an hour before breakfast for 10 days from 23-Dec 2011:";  "15 Oct 2013, 17 Oct 2013 and 1 Nov 2013".)
         */
        public Timing getSchedule() { 
          if (this.schedule == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationStatementDosageComponent.schedule");
            else if (Configuration.doAutoCreate())
              this.schedule = new Timing(); // cc
          return this.schedule;
        }

        public boolean hasSchedule() { 
          return this.schedule != null && !this.schedule.isEmpty();
        }

        /**
         * @param value {@link #schedule} (The timing schedule for giving the medication to the patient.  The Schedule data type allows many different expressions, for example.  "Every  8 hours"; "Three times a day"; "1/2 an hour before breakfast for 10 days from 23-Dec 2011:";  "15 Oct 2013, 17 Oct 2013 and 1 Nov 2013".)
         */
        public MedicationStatementDosageComponent setSchedule(Timing value) { 
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
        public MedicationStatementDosageComponent setAsNeeded(Type value) { 
          this.asNeeded = value;
          return this;
        }

        /**
         * @return {@link #site} (A coded specification of the anatomic site where the medication first enters the body.)
         */
        public CodeableConcept getSite() { 
          if (this.site == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationStatementDosageComponent.site");
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
        public MedicationStatementDosageComponent setSite(CodeableConcept value) { 
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
         * @return {@link #method} (A coded value indicating the method by which the medication is introduced into or onto the body. Most commonly used for injections.  Examples:  Slow Push; Deep IV.

Terminologies used often pre-coordinate this term with the route and or form of administration.)
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
         * @param value {@link #method} (A coded value indicating the method by which the medication is introduced into or onto the body. Most commonly used for injections.  Examples:  Slow Push; Deep IV.

Terminologies used often pre-coordinate this term with the route and or form of administration.)
         */
        public MedicationStatementDosageComponent setMethod(CodeableConcept value) { 
          this.method = value;
          return this;
        }

        /**
         * @return {@link #quantity} (The amount of therapeutic or other substance given at one administration event.)
         */
        public Quantity getQuantity() { 
          if (this.quantity == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationStatementDosageComponent.quantity");
            else if (Configuration.doAutoCreate())
              this.quantity = new Quantity(); // cc
          return this.quantity;
        }

        public boolean hasQuantity() { 
          return this.quantity != null && !this.quantity.isEmpty();
        }

        /**
         * @param value {@link #quantity} (The amount of therapeutic or other substance given at one administration event.)
         */
        public MedicationStatementDosageComponent setQuantity(Quantity value) { 
          this.quantity = value;
          return this;
        }

        /**
         * @return {@link #rate} (Identifies the speed with which the substance is introduced into the subject. Typically the rate for an infusion. 200ml in 2 hours.)
         */
        public Ratio getRate() { 
          if (this.rate == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationStatementDosageComponent.rate");
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
        public MedicationStatementDosageComponent setRate(Ratio value) { 
          this.rate = value;
          return this;
        }

        /**
         * @return {@link #maxDosePerPeriod} (The maximum total quantity of a therapeutic substance that may be administered to a subject over the period of time. E.g. 1000mg in 24 hours.)
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
         * @param value {@link #maxDosePerPeriod} (The maximum total quantity of a therapeutic substance that may be administered to a subject over the period of time. E.g. 1000mg in 24 hours.)
         */
        public MedicationStatementDosageComponent setMaxDosePerPeriod(Ratio value) { 
          this.maxDosePerPeriod = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("text", "string", "Free text dosage instructions can be used for cases where the instructions are too complex to code. When coded instructions are present, the free text instructions may still be present for display to humans taking or administering the medication.", 0, java.lang.Integer.MAX_VALUE, text));
          childrenList.add(new Property("schedule", "Timing", "The timing schedule for giving the medication to the patient.  The Schedule data type allows many different expressions, for example.  'Every  8 hours'; 'Three times a day'; '1/2 an hour before breakfast for 10 days from 23-Dec 2011:';  '15 Oct 2013, 17 Oct 2013 and 1 Nov 2013'.", 0, java.lang.Integer.MAX_VALUE, schedule));
          childrenList.add(new Property("asNeeded[x]", "boolean|CodeableConcept", "If set to true or if specified as a CodeableConcept, indicates that the medication is only taken when needed within the specified schedule rather than at every scheduled dose.  If a CodeableConcept is present, it indicates the pre-condition for taking the Medication.", 0, java.lang.Integer.MAX_VALUE, asNeeded));
          childrenList.add(new Property("site", "CodeableConcept", "A coded specification of the anatomic site where the medication first enters the body.", 0, java.lang.Integer.MAX_VALUE, site));
          childrenList.add(new Property("route", "CodeableConcept", "A code specifying the route or physiological path of administration of a therapeutic agent into or onto a subject.", 0, java.lang.Integer.MAX_VALUE, route));
          childrenList.add(new Property("method", "CodeableConcept", "A coded value indicating the method by which the medication is introduced into or onto the body. Most commonly used for injections.  Examples:  Slow Push; Deep IV.\r\rTerminologies used often pre-coordinate this term with the route and or form of administration.", 0, java.lang.Integer.MAX_VALUE, method));
          childrenList.add(new Property("quantity", "Quantity", "The amount of therapeutic or other substance given at one administration event.", 0, java.lang.Integer.MAX_VALUE, quantity));
          childrenList.add(new Property("rate", "Ratio", "Identifies the speed with which the substance is introduced into the subject. Typically the rate for an infusion. 200ml in 2 hours.", 0, java.lang.Integer.MAX_VALUE, rate));
          childrenList.add(new Property("maxDosePerPeriod", "Ratio", "The maximum total quantity of a therapeutic substance that may be administered to a subject over the period of time. E.g. 1000mg in 24 hours.", 0, java.lang.Integer.MAX_VALUE, maxDosePerPeriod));
        }

      public MedicationStatementDosageComponent copy() {
        MedicationStatementDosageComponent dst = new MedicationStatementDosageComponent();
        copyValues(dst);
        dst.text = text == null ? null : text.copy();
        dst.schedule = schedule == null ? null : schedule.copy();
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
        return compareDeep(text, o.text, true) && compareDeep(schedule, o.schedule, true) && compareDeep(asNeeded, o.asNeeded, true)
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
        return super.isEmpty() && (text == null || text.isEmpty()) && (schedule == null || schedule.isEmpty())
           && (asNeeded == null || asNeeded.isEmpty()) && (site == null || site.isEmpty()) && (route == null || route.isEmpty())
           && (method == null || method.isEmpty()) && (quantity == null || quantity.isEmpty()) && (rate == null || rate.isEmpty())
           && (maxDosePerPeriod == null || maxDosePerPeriod.isEmpty());
      }

  }

    /**
     * External identifier - FHIR will generate its own internal IDs (probably URLs) which do not need to be explicitly managed by the resource.  The identifier here is one that would be used by another non-FHIR system - for example an automated medication pump would provide a record each time it operated; an administration while the patient was off the ward might be made with a different system and entered after the event.  Particularly important if these records have to be updated.
     */
    @Child(name = "identifier", type = {Identifier.class}, order = 0, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="External Identifier", formalDefinition="External identifier - FHIR will generate its own internal IDs (probably URLs) which do not need to be explicitly managed by the resource.  The identifier here is one that would be used by another non-FHIR system - for example an automated medication pump would provide a record each time it operated; an administration while the patient was off the ward might be made with a different system and entered after the event.  Particularly important if these records have to be updated." )
    protected List<Identifier> identifier;

    /**
     * The person or animal who is /was taking the medication.
     */
    @Child(name = "patient", type = {Patient.class}, order = 1, min = 0, max = 1)
    @Description(shortDefinition="Who was/is taking medication", formalDefinition="The person or animal who is /was taking the medication." )
    protected Reference patient;

    /**
     * The actual object that is the target of the reference (The person or animal who is /was taking the medication.)
     */
    protected Patient patientTarget;

    /**
     * The person who provided the information about the taking of this medication.
     */
    @Child(name = "informationSource", type = {Patient.class, Practitioner.class, RelatedPerson.class}, order = 2, min = 0, max = 1)
    @Description(shortDefinition="", formalDefinition="The person who provided the information about the taking of this medication." )
    protected Reference informationSource;

    /**
     * The actual object that is the target of the reference (The person who provided the information about the taking of this medication.)
     */
    protected Resource informationSourceTarget;

    /**
     * The date when the medication statement was asserted by the information source.
     */
    @Child(name = "dateAsserted", type = {DateTimeType.class}, order = 3, min = 0, max = 1)
    @Description(shortDefinition="When the statement was asserted?", formalDefinition="The date when the medication statement was asserted by the information source." )
    protected DateTimeType dateAsserted;

    /**
     * A code specifying the state of the statement.  Generally this will be in-progress or completed state.
     */
    @Child(name = "status", type = {CodeType.class}, order = 4, min = 1, max = 1)
    @Description(shortDefinition="in-progress | completed | entered-in-error", formalDefinition="A code specifying the state of the statement.  Generally this will be in-progress or completed state." )
    protected Enumeration<MedicationStatementStatus> status;

    /**
     * Set this to true if the record is saying that the medication was NOT taken.
     */
    @Child(name = "wasNotGiven", type = {BooleanType.class}, order = 5, min = 0, max = 1)
    @Description(shortDefinition="True if medication is/was not being taken", formalDefinition="Set this to true if the record is saying that the medication was NOT taken." )
    protected BooleanType wasNotGiven;

    /**
     * A code indicating why the medication was not taken.
     */
    @Child(name = "reasonNotGiven", type = {CodeableConcept.class}, order = 6, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="True if asserting medication was not given", formalDefinition="A code indicating why the medication was not taken." )
    protected List<CodeableConcept> reasonNotGiven;

    /**
     * A reason for why the medication is being/was taken.
     */
    @Child(name = "reasonForUse", type = {CodeableConcept.class, Condition.class}, order = 7, min = 0, max = 1)
    @Description(shortDefinition="", formalDefinition="A reason for why the medication is being/was taken." )
    protected Type reasonForUse;

    /**
     * The interval of time during which it is being asserted that the patient was taking the medication (or was not taking, when the 'wasNotGiven' attribute is true).
     */
    @Child(name = "effective", type = {DateTimeType.class, Period.class}, order = 8, min = 0, max = 1)
    @Description(shortDefinition="Over what period was medication consumed?", formalDefinition="The interval of time during which it is being asserted that the patient was taking the medication (or was not taking, when the 'wasNotGiven' attribute is true)." )
    protected Type effective;

    /**
     * Provides extra information about the medication statement that is not conveyed by the other attributes.
     */
    @Child(name = "note", type = {StringType.class}, order = 9, min = 0, max = 1)
    @Description(shortDefinition="Further information about the statement", formalDefinition="Provides extra information about the medication statement that is not conveyed by the other attributes." )
    protected StringType note;

    /**
     * Identifies the medication being administered. This is either a link to a resource representing the details of the medication or a simple attribute carrying a code that identifies the medication from a known list of medications.
     */
    @Child(name = "medication", type = {Medication.class}, order = 10, min = 0, max = 1)
    @Description(shortDefinition="What medication was taken?", formalDefinition="Identifies the medication being administered. This is either a link to a resource representing the details of the medication or a simple attribute carrying a code that identifies the medication from a known list of medications." )
    protected Reference medication;

    /**
     * The actual object that is the target of the reference (Identifies the medication being administered. This is either a link to a resource representing the details of the medication or a simple attribute carrying a code that identifies the medication from a known list of medications.)
     */
    protected Medication medicationTarget;

    /**
     * Indicates how the medication is/was used by the patient.
     */
    @Child(name = "dosage", type = {}, order = 11, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Details of how medication was taken", formalDefinition="Indicates how the medication is/was used by the patient." )
    protected List<MedicationStatementDosageComponent> dosage;

    private static final long serialVersionUID = -1525633004L;

    public MedicationStatement() {
      super();
    }

    public MedicationStatement(Enumeration<MedicationStatementStatus> status) {
      super();
      this.status = status;
    }

    /**
     * @return {@link #identifier} (External identifier - FHIR will generate its own internal IDs (probably URLs) which do not need to be explicitly managed by the resource.  The identifier here is one that would be used by another non-FHIR system - for example an automated medication pump would provide a record each time it operated; an administration while the patient was off the ward might be made with a different system and entered after the event.  Particularly important if these records have to be updated.)
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
     * @return {@link #identifier} (External identifier - FHIR will generate its own internal IDs (probably URLs) which do not need to be explicitly managed by the resource.  The identifier here is one that would be used by another non-FHIR system - for example an automated medication pump would provide a record each time it operated; an administration while the patient was off the ward might be made with a different system and entered after the event.  Particularly important if these records have to be updated.)
     */
    // syntactic sugar
    public Identifier addIdentifier() { //3
      Identifier t = new Identifier();
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return t;
    }

    /**
     * @return {@link #patient} (The person or animal who is /was taking the medication.)
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
     * @param value {@link #patient} (The person or animal who is /was taking the medication.)
     */
    public MedicationStatement setPatient(Reference value) { 
      this.patient = value;
      return this;
    }

    /**
     * @return {@link #patient} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The person or animal who is /was taking the medication.)
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
     * @param value {@link #patient} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The person or animal who is /was taking the medication.)
     */
    public MedicationStatement setPatientTarget(Patient value) { 
      this.patientTarget = value;
      return this;
    }

    /**
     * @return {@link #informationSource} (The person who provided the information about the taking of this medication.)
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
     * @param value {@link #informationSource} (The person who provided the information about the taking of this medication.)
     */
    public MedicationStatement setInformationSource(Reference value) { 
      this.informationSource = value;
      return this;
    }

    /**
     * @return {@link #informationSource} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The person who provided the information about the taking of this medication.)
     */
    public Resource getInformationSourceTarget() { 
      return this.informationSourceTarget;
    }

    /**
     * @param value {@link #informationSource} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The person who provided the information about the taking of this medication.)
     */
    public MedicationStatement setInformationSourceTarget(Resource value) { 
      this.informationSourceTarget = value;
      return this;
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
     * @return {@link #status} (A code specifying the state of the statement.  Generally this will be in-progress or completed state.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
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
     * @param value {@link #status} (A code specifying the state of the statement.  Generally this will be in-progress or completed state.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public MedicationStatement setStatusElement(Enumeration<MedicationStatementStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return A code specifying the state of the statement.  Generally this will be in-progress or completed state.
     */
    public MedicationStatementStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value A code specifying the state of the statement.  Generally this will be in-progress or completed state.
     */
    public MedicationStatement setStatus(MedicationStatementStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<MedicationStatementStatus>(new MedicationStatementStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #wasNotGiven} (Set this to true if the record is saying that the medication was NOT taken.). This is the underlying object with id, value and extensions. The accessor "getWasNotGiven" gives direct access to the value
     */
    public BooleanType getWasNotGivenElement() { 
      if (this.wasNotGiven == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationStatement.wasNotGiven");
        else if (Configuration.doAutoCreate())
          this.wasNotGiven = new BooleanType(); // bb
      return this.wasNotGiven;
    }

    public boolean hasWasNotGivenElement() { 
      return this.wasNotGiven != null && !this.wasNotGiven.isEmpty();
    }

    public boolean hasWasNotGiven() { 
      return this.wasNotGiven != null && !this.wasNotGiven.isEmpty();
    }

    /**
     * @param value {@link #wasNotGiven} (Set this to true if the record is saying that the medication was NOT taken.). This is the underlying object with id, value and extensions. The accessor "getWasNotGiven" gives direct access to the value
     */
    public MedicationStatement setWasNotGivenElement(BooleanType value) { 
      this.wasNotGiven = value;
      return this;
    }

    /**
     * @return Set this to true if the record is saying that the medication was NOT taken.
     */
    public boolean getWasNotGiven() { 
      return this.wasNotGiven == null ? false : this.wasNotGiven.getValue();
    }

    /**
     * @param value Set this to true if the record is saying that the medication was NOT taken.
     */
    public MedicationStatement setWasNotGiven(boolean value) { 
        if (this.wasNotGiven == null)
          this.wasNotGiven = new BooleanType();
        this.wasNotGiven.setValue(value);
      return this;
    }

    /**
     * @return {@link #reasonNotGiven} (A code indicating why the medication was not taken.)
     */
    public List<CodeableConcept> getReasonNotGiven() { 
      if (this.reasonNotGiven == null)
        this.reasonNotGiven = new ArrayList<CodeableConcept>();
      return this.reasonNotGiven;
    }

    public boolean hasReasonNotGiven() { 
      if (this.reasonNotGiven == null)
        return false;
      for (CodeableConcept item : this.reasonNotGiven)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #reasonNotGiven} (A code indicating why the medication was not taken.)
     */
    // syntactic sugar
    public CodeableConcept addReasonNotGiven() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.reasonNotGiven == null)
        this.reasonNotGiven = new ArrayList<CodeableConcept>();
      this.reasonNotGiven.add(t);
      return t;
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
    public CodeableConcept getReasonForUseCodeableConcept() throws Exception { 
      if (!(this.reasonForUse instanceof CodeableConcept))
        throw new Exception("Type mismatch: the type CodeableConcept was expected, but "+this.reasonForUse.getClass().getName()+" was encountered");
      return (CodeableConcept) this.reasonForUse;
    }

    /**
     * @return {@link #reasonForUse} (A reason for why the medication is being/was taken.)
     */
    public Reference getReasonForUseReference() throws Exception { 
      if (!(this.reasonForUse instanceof Reference))
        throw new Exception("Type mismatch: the type Reference was expected, but "+this.reasonForUse.getClass().getName()+" was encountered");
      return (Reference) this.reasonForUse;
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
     * @return {@link #effective} (The interval of time during which it is being asserted that the patient was taking the medication (or was not taking, when the 'wasNotGiven' attribute is true).)
     */
    public Type getEffective() { 
      return this.effective;
    }

    /**
     * @return {@link #effective} (The interval of time during which it is being asserted that the patient was taking the medication (or was not taking, when the 'wasNotGiven' attribute is true).)
     */
    public DateTimeType getEffectiveDateTimeType() throws Exception { 
      if (!(this.effective instanceof DateTimeType))
        throw new Exception("Type mismatch: the type DateTimeType was expected, but "+this.effective.getClass().getName()+" was encountered");
      return (DateTimeType) this.effective;
    }

    /**
     * @return {@link #effective} (The interval of time during which it is being asserted that the patient was taking the medication (or was not taking, when the 'wasNotGiven' attribute is true).)
     */
    public Period getEffectivePeriod() throws Exception { 
      if (!(this.effective instanceof Period))
        throw new Exception("Type mismatch: the type Period was expected, but "+this.effective.getClass().getName()+" was encountered");
      return (Period) this.effective;
    }

    public boolean hasEffective() { 
      return this.effective != null && !this.effective.isEmpty();
    }

    /**
     * @param value {@link #effective} (The interval of time during which it is being asserted that the patient was taking the medication (or was not taking, when the 'wasNotGiven' attribute is true).)
     */
    public MedicationStatement setEffective(Type value) { 
      this.effective = value;
      return this;
    }

    /**
     * @return {@link #note} (Provides extra information about the medication statement that is not conveyed by the other attributes.). This is the underlying object with id, value and extensions. The accessor "getNote" gives direct access to the value
     */
    public StringType getNoteElement() { 
      if (this.note == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationStatement.note");
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
     * @param value {@link #note} (Provides extra information about the medication statement that is not conveyed by the other attributes.). This is the underlying object with id, value and extensions. The accessor "getNote" gives direct access to the value
     */
    public MedicationStatement setNoteElement(StringType value) { 
      this.note = value;
      return this;
    }

    /**
     * @return Provides extra information about the medication statement that is not conveyed by the other attributes.
     */
    public String getNote() { 
      return this.note == null ? null : this.note.getValue();
    }

    /**
     * @param value Provides extra information about the medication statement that is not conveyed by the other attributes.
     */
    public MedicationStatement setNote(String value) { 
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
     * @return {@link #medication} (Identifies the medication being administered. This is either a link to a resource representing the details of the medication or a simple attribute carrying a code that identifies the medication from a known list of medications.)
     */
    public Reference getMedication() { 
      if (this.medication == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationStatement.medication");
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
    public MedicationStatement setMedication(Reference value) { 
      this.medication = value;
      return this;
    }

    /**
     * @return {@link #medication} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Identifies the medication being administered. This is either a link to a resource representing the details of the medication or a simple attribute carrying a code that identifies the medication from a known list of medications.)
     */
    public Medication getMedicationTarget() { 
      if (this.medicationTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationStatement.medication");
        else if (Configuration.doAutoCreate())
          this.medicationTarget = new Medication(); // aa
      return this.medicationTarget;
    }

    /**
     * @param value {@link #medication} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Identifies the medication being administered. This is either a link to a resource representing the details of the medication or a simple attribute carrying a code that identifies the medication from a known list of medications.)
     */
    public MedicationStatement setMedicationTarget(Medication value) { 
      this.medicationTarget = value;
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

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "External identifier - FHIR will generate its own internal IDs (probably URLs) which do not need to be explicitly managed by the resource.  The identifier here is one that would be used by another non-FHIR system - for example an automated medication pump would provide a record each time it operated; an administration while the patient was off the ward might be made with a different system and entered after the event.  Particularly important if these records have to be updated.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("patient", "Reference(Patient)", "The person or animal who is /was taking the medication.", 0, java.lang.Integer.MAX_VALUE, patient));
        childrenList.add(new Property("informationSource", "Reference(Patient|Practitioner|RelatedPerson)", "The person who provided the information about the taking of this medication.", 0, java.lang.Integer.MAX_VALUE, informationSource));
        childrenList.add(new Property("dateAsserted", "dateTime", "The date when the medication statement was asserted by the information source.", 0, java.lang.Integer.MAX_VALUE, dateAsserted));
        childrenList.add(new Property("status", "code", "A code specifying the state of the statement.  Generally this will be in-progress or completed state.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("wasNotGiven", "boolean", "Set this to true if the record is saying that the medication was NOT taken.", 0, java.lang.Integer.MAX_VALUE, wasNotGiven));
        childrenList.add(new Property("reasonNotGiven", "CodeableConcept", "A code indicating why the medication was not taken.", 0, java.lang.Integer.MAX_VALUE, reasonNotGiven));
        childrenList.add(new Property("reasonForUse[x]", "CodeableConcept|Reference(Condition)", "A reason for why the medication is being/was taken.", 0, java.lang.Integer.MAX_VALUE, reasonForUse));
        childrenList.add(new Property("effective[x]", "dateTime|Period", "The interval of time during which it is being asserted that the patient was taking the medication (or was not taking, when the 'wasNotGiven' attribute is true).", 0, java.lang.Integer.MAX_VALUE, effective));
        childrenList.add(new Property("note", "string", "Provides extra information about the medication statement that is not conveyed by the other attributes.", 0, java.lang.Integer.MAX_VALUE, note));
        childrenList.add(new Property("medication", "Reference(Medication)", "Identifies the medication being administered. This is either a link to a resource representing the details of the medication or a simple attribute carrying a code that identifies the medication from a known list of medications.", 0, java.lang.Integer.MAX_VALUE, medication));
        childrenList.add(new Property("dosage", "", "Indicates how the medication is/was used by the patient.", 0, java.lang.Integer.MAX_VALUE, dosage));
      }

      public MedicationStatement copy() {
        MedicationStatement dst = new MedicationStatement();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.patient = patient == null ? null : patient.copy();
        dst.informationSource = informationSource == null ? null : informationSource.copy();
        dst.dateAsserted = dateAsserted == null ? null : dateAsserted.copy();
        dst.status = status == null ? null : status.copy();
        dst.wasNotGiven = wasNotGiven == null ? null : wasNotGiven.copy();
        if (reasonNotGiven != null) {
          dst.reasonNotGiven = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : reasonNotGiven)
            dst.reasonNotGiven.add(i.copy());
        };
        dst.reasonForUse = reasonForUse == null ? null : reasonForUse.copy();
        dst.effective = effective == null ? null : effective.copy();
        dst.note = note == null ? null : note.copy();
        dst.medication = medication == null ? null : medication.copy();
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
        return compareDeep(identifier, o.identifier, true) && compareDeep(patient, o.patient, true) && compareDeep(informationSource, o.informationSource, true)
           && compareDeep(dateAsserted, o.dateAsserted, true) && compareDeep(status, o.status, true) && compareDeep(wasNotGiven, o.wasNotGiven, true)
           && compareDeep(reasonNotGiven, o.reasonNotGiven, true) && compareDeep(reasonForUse, o.reasonForUse, true)
           && compareDeep(effective, o.effective, true) && compareDeep(note, o.note, true) && compareDeep(medication, o.medication, true)
           && compareDeep(dosage, o.dosage, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof MedicationStatement))
          return false;
        MedicationStatement o = (MedicationStatement) other;
        return compareValues(dateAsserted, o.dateAsserted, true) && compareValues(status, o.status, true) && compareValues(wasNotGiven, o.wasNotGiven, true)
           && compareValues(note, o.note, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (patient == null || patient.isEmpty())
           && (informationSource == null || informationSource.isEmpty()) && (dateAsserted == null || dateAsserted.isEmpty())
           && (status == null || status.isEmpty()) && (wasNotGiven == null || wasNotGiven.isEmpty())
           && (reasonNotGiven == null || reasonNotGiven.isEmpty()) && (reasonForUse == null || reasonForUse.isEmpty())
           && (effective == null || effective.isEmpty()) && (note == null || note.isEmpty()) && (medication == null || medication.isEmpty())
           && (dosage == null || dosage.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.MedicationStatement;
   }

  @SearchParamDefinition(name="identifier", path="MedicationStatement.identifier", description="Return administrations with this external identity", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
    @SearchParamDefinition(name = "patient", path = "MedicationStatement.patient", description = "The identity of a patient to list administrations  for", type = "reference")
    public static final String SP_PATIENT = "patient";
    @SearchParamDefinition(name = "medication", path = "MedicationStatement.medication", description = "Code for medicine or text in medicine name", type = "reference")
    public static final String SP_MEDICATION = "medication";
  @SearchParamDefinition(name="effectivedate", path="MedicationStatement.effective[x]", description="Date when patient was taking (or not taking) the medication", type="date" )
  public static final String SP_EFFECTIVEDATE = "effectivedate";
    @SearchParamDefinition(name = "status", path = "MedicationStatement.status", description = "Return statements that match the given status", type = "string")
    public static final String SP_STATUS = "status";

}

