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

// Generated on Wed, Nov 11, 2015 10:54-0500 for FHIR v1.0.2
import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.instance.model.api.IBaseBackboneElement;
import org.hl7.fhir.instance.utilities.Utilities;

import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
/**
 * Describes the event of a patient consuming or otherwise being administered a medication.  This may be as simple as swallowing a tablet or it may be a long running infusion.  Related resources tie this event to the authorizing prescription, and the specific encounter between patient and health care practitioner.
 */
@ResourceDef(name="MedicationAdministration", profile="http://hl7.org/fhir/Profile/MedicationAdministration")
public class MedicationAdministration extends DomainResource {

    public enum MedicationAdministrationStatus {
        /**
         * The administration has started but has not yet completed.
         */
        INPROGRESS, 
        /**
         * Actions implied by the administration have been temporarily halted, but are expected to continue later. May also be called "suspended".
         */
        ONHOLD, 
        /**
         * All actions that are implied by the administration have occurred.
         */
        COMPLETED, 
        /**
         * The administration was entered in error and therefore nullified.
         */
        ENTEREDINERROR, 
        /**
         * Actions implied by the administration have been permanently halted, before all of them occurred.
         */
        STOPPED, 
        /**
         * added to help the parsers
         */
        NULL;
        public static MedicationAdministrationStatus fromCode(String codeString) throws Exception {
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
        throw new Exception("Unknown MedicationAdministrationStatus code '"+codeString+"'");
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
            case INPROGRESS: return "http://hl7.org/fhir/medication-admin-status";
            case ONHOLD: return "http://hl7.org/fhir/medication-admin-status";
            case COMPLETED: return "http://hl7.org/fhir/medication-admin-status";
            case ENTEREDINERROR: return "http://hl7.org/fhir/medication-admin-status";
            case STOPPED: return "http://hl7.org/fhir/medication-admin-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case INPROGRESS: return "The administration has started but has not yet completed.";
            case ONHOLD: return "Actions implied by the administration have been temporarily halted, but are expected to continue later. May also be called \"suspended\".";
            case COMPLETED: return "All actions that are implied by the administration have occurred.";
            case ENTEREDINERROR: return "The administration was entered in error and therefore nullified.";
            case STOPPED: return "Actions implied by the administration have been permanently halted, before all of them occurred.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case INPROGRESS: return "In Progress";
            case ONHOLD: return "On Hold";
            case COMPLETED: return "Completed";
            case ENTEREDINERROR: return "Entered in Error";
            case STOPPED: return "Stopped";
            default: return "?";
          }
        }
    }

  public static class MedicationAdministrationStatusEnumFactory implements EnumFactory<MedicationAdministrationStatus> {
    public MedicationAdministrationStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("in-progress".equals(codeString))
          return MedicationAdministrationStatus.INPROGRESS;
        if ("on-hold".equals(codeString))
          return MedicationAdministrationStatus.ONHOLD;
        if ("completed".equals(codeString))
          return MedicationAdministrationStatus.COMPLETED;
        if ("entered-in-error".equals(codeString))
          return MedicationAdministrationStatus.ENTEREDINERROR;
        if ("stopped".equals(codeString))
          return MedicationAdministrationStatus.STOPPED;
        throw new IllegalArgumentException("Unknown MedicationAdministrationStatus code '"+codeString+"'");
        }
    public String toCode(MedicationAdministrationStatus code) {
      if (code == MedicationAdministrationStatus.INPROGRESS)
        return "in-progress";
      if (code == MedicationAdministrationStatus.ONHOLD)
        return "on-hold";
      if (code == MedicationAdministrationStatus.COMPLETED)
        return "completed";
      if (code == MedicationAdministrationStatus.ENTEREDINERROR)
        return "entered-in-error";
      if (code == MedicationAdministrationStatus.STOPPED)
        return "stopped";
      return "?";
      }
    }

    @Block()
    public static class MedicationAdministrationDosageComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Free text dosage instructions can be used for cases where the instructions are too complex to code. When coded instructions are present, the free text instructions may still be present for display to humans taking or administering the medication.
         */
        @Child(name = "text", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Dosage Instructions", formalDefinition="Free text dosage instructions can be used for cases where the instructions are too complex to code. When coded instructions are present, the free text instructions may still be present for display to humans taking or administering the medication." )
        protected StringType text;

        /**
         * A coded specification of the anatomic site where the medication first entered the body.  For example, "left arm".
         */
        @Child(name = "site", type = {CodeableConcept.class, BodySite.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Body site administered to", formalDefinition="A coded specification of the anatomic site where the medication first entered the body.  For example, \"left arm\"." )
        protected Type site;

        /**
         * A code specifying the route or physiological path of administration of a therapeutic agent into or onto the patient.  For example, topical, intravenous, etc.
         */
        @Child(name = "route", type = {CodeableConcept.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Path of substance into body", formalDefinition="A code specifying the route or physiological path of administration of a therapeutic agent into or onto the patient.  For example, topical, intravenous, etc." )
        protected CodeableConcept route;

        /**
         * A coded value indicating the method by which the medication is intended to be or was introduced into or on the body.  This attribute will most often NOT be populated.  It is most commonly used for injections.  For example, Slow Push, Deep IV.
         */
        @Child(name = "method", type = {CodeableConcept.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="How drug was administered", formalDefinition="A coded value indicating the method by which the medication is intended to be or was introduced into or on the body.  This attribute will most often NOT be populated.  It is most commonly used for injections.  For example, Slow Push, Deep IV." )
        protected CodeableConcept method;

        /**
         * The amount of the medication given at one administration event.   Use this value when the administration is essentially an instantaneous event such as a swallowing a tablet or giving an injection.
         */
        @Child(name = "quantity", type = {SimpleQuantity.class}, order=5, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Amount administered in one dose", formalDefinition="The amount of the medication given at one administration event.   Use this value when the administration is essentially an instantaneous event such as a swallowing a tablet or giving an injection." )
        protected SimpleQuantity quantity;

        /**
         * Identifies the speed with which the medication was or will be introduced into the patient.  Typically the rate for an infusion e.g. 100 ml per 1 hour or 100 ml/hr.  May also be expressed as a rate per unit of time e.g. 500 ml per 2 hours.  Currently we do not specify a default of '1' in the denominator, but this is being discussed.  Other examples:  200 mcg/min or 200 mcg/1 minute; 1 liter/8 hours.
         */
        @Child(name = "rate", type = {Ratio.class, Range.class}, order=6, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Dose quantity per unit of time", formalDefinition="Identifies the speed with which the medication was or will be introduced into the patient.  Typically the rate for an infusion e.g. 100 ml per 1 hour or 100 ml/hr.  May also be expressed as a rate per unit of time e.g. 500 ml per 2 hours.  Currently we do not specify a default of '1' in the denominator, but this is being discussed.  Other examples:  200 mcg/min or 200 mcg/1 minute; 1 liter/8 hours." )
        protected Type rate;

        private static final long serialVersionUID = -1772198879L;

    /*
     * Constructor
     */
      public MedicationAdministrationDosageComponent() {
        super();
      }

        /**
         * @return {@link #text} (Free text dosage instructions can be used for cases where the instructions are too complex to code. When coded instructions are present, the free text instructions may still be present for display to humans taking or administering the medication.). This is the underlying object with id, value and extensions. The accessor "getText" gives direct access to the value
         */
        public StringType getTextElement() { 
          if (this.text == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationAdministrationDosageComponent.text");
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
        public MedicationAdministrationDosageComponent setTextElement(StringType value) { 
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
        public MedicationAdministrationDosageComponent setText(String value) { 
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
         * @return {@link #site} (A coded specification of the anatomic site where the medication first entered the body.  For example, "left arm".)
         */
        public Type getSite() { 
          return this.site;
        }

        /**
         * @return {@link #site} (A coded specification of the anatomic site where the medication first entered the body.  For example, "left arm".)
         */
        public CodeableConcept getSiteCodeableConcept() throws Exception { 
          if (!(this.site instanceof CodeableConcept))
            throw new Exception("Type mismatch: the type CodeableConcept was expected, but "+this.site.getClass().getName()+" was encountered");
          return (CodeableConcept) this.site;
        }

        public boolean hasSiteCodeableConcept() throws Exception { 
          return this.site instanceof CodeableConcept;
        }

        /**
         * @return {@link #site} (A coded specification of the anatomic site where the medication first entered the body.  For example, "left arm".)
         */
        public Reference getSiteReference() throws Exception { 
          if (!(this.site instanceof Reference))
            throw new Exception("Type mismatch: the type Reference was expected, but "+this.site.getClass().getName()+" was encountered");
          return (Reference) this.site;
        }

        public boolean hasSiteReference() throws Exception { 
          return this.site instanceof Reference;
        }

        public boolean hasSite() { 
          return this.site != null && !this.site.isEmpty();
        }

        /**
         * @param value {@link #site} (A coded specification of the anatomic site where the medication first entered the body.  For example, "left arm".)
         */
        public MedicationAdministrationDosageComponent setSite(Type value) { 
          this.site = value;
          return this;
        }

        /**
         * @return {@link #route} (A code specifying the route or physiological path of administration of a therapeutic agent into or onto the patient.  For example, topical, intravenous, etc.)
         */
        public CodeableConcept getRoute() { 
          if (this.route == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationAdministrationDosageComponent.route");
            else if (Configuration.doAutoCreate())
              this.route = new CodeableConcept(); // cc
          return this.route;
        }

        public boolean hasRoute() { 
          return this.route != null && !this.route.isEmpty();
        }

        /**
         * @param value {@link #route} (A code specifying the route or physiological path of administration of a therapeutic agent into or onto the patient.  For example, topical, intravenous, etc.)
         */
        public MedicationAdministrationDosageComponent setRoute(CodeableConcept value) { 
          this.route = value;
          return this;
        }

        /**
         * @return {@link #method} (A coded value indicating the method by which the medication is intended to be or was introduced into or on the body.  This attribute will most often NOT be populated.  It is most commonly used for injections.  For example, Slow Push, Deep IV.)
         */
        public CodeableConcept getMethod() { 
          if (this.method == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationAdministrationDosageComponent.method");
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
        public MedicationAdministrationDosageComponent setMethod(CodeableConcept value) { 
          this.method = value;
          return this;
        }

        /**
         * @return {@link #quantity} (The amount of the medication given at one administration event.   Use this value when the administration is essentially an instantaneous event such as a swallowing a tablet or giving an injection.)
         */
        public SimpleQuantity getQuantity() { 
          if (this.quantity == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationAdministrationDosageComponent.quantity");
            else if (Configuration.doAutoCreate())
              this.quantity = new SimpleQuantity(); // cc
          return this.quantity;
        }

        public boolean hasQuantity() { 
          return this.quantity != null && !this.quantity.isEmpty();
        }

        /**
         * @param value {@link #quantity} (The amount of the medication given at one administration event.   Use this value when the administration is essentially an instantaneous event such as a swallowing a tablet or giving an injection.)
         */
        public MedicationAdministrationDosageComponent setQuantity(SimpleQuantity value) { 
          this.quantity = value;
          return this;
        }

        /**
         * @return {@link #rate} (Identifies the speed with which the medication was or will be introduced into the patient.  Typically the rate for an infusion e.g. 100 ml per 1 hour or 100 ml/hr.  May also be expressed as a rate per unit of time e.g. 500 ml per 2 hours.  Currently we do not specify a default of '1' in the denominator, but this is being discussed.  Other examples:  200 mcg/min or 200 mcg/1 minute; 1 liter/8 hours.)
         */
        public Type getRate() { 
          return this.rate;
        }

        /**
         * @return {@link #rate} (Identifies the speed with which the medication was or will be introduced into the patient.  Typically the rate for an infusion e.g. 100 ml per 1 hour or 100 ml/hr.  May also be expressed as a rate per unit of time e.g. 500 ml per 2 hours.  Currently we do not specify a default of '1' in the denominator, but this is being discussed.  Other examples:  200 mcg/min or 200 mcg/1 minute; 1 liter/8 hours.)
         */
        public Ratio getRateRatio() throws Exception { 
          if (!(this.rate instanceof Ratio))
            throw new Exception("Type mismatch: the type Ratio was expected, but "+this.rate.getClass().getName()+" was encountered");
          return (Ratio) this.rate;
        }

        public boolean hasRateRatio() throws Exception { 
          return this.rate instanceof Ratio;
        }

        /**
         * @return {@link #rate} (Identifies the speed with which the medication was or will be introduced into the patient.  Typically the rate for an infusion e.g. 100 ml per 1 hour or 100 ml/hr.  May also be expressed as a rate per unit of time e.g. 500 ml per 2 hours.  Currently we do not specify a default of '1' in the denominator, but this is being discussed.  Other examples:  200 mcg/min or 200 mcg/1 minute; 1 liter/8 hours.)
         */
        public Range getRateRange() throws Exception { 
          if (!(this.rate instanceof Range))
            throw new Exception("Type mismatch: the type Range was expected, but "+this.rate.getClass().getName()+" was encountered");
          return (Range) this.rate;
        }

        public boolean hasRateRange() throws Exception { 
          return this.rate instanceof Range;
        }

        public boolean hasRate() { 
          return this.rate != null && !this.rate.isEmpty();
        }

        /**
         * @param value {@link #rate} (Identifies the speed with which the medication was or will be introduced into the patient.  Typically the rate for an infusion e.g. 100 ml per 1 hour or 100 ml/hr.  May also be expressed as a rate per unit of time e.g. 500 ml per 2 hours.  Currently we do not specify a default of '1' in the denominator, but this is being discussed.  Other examples:  200 mcg/min or 200 mcg/1 minute; 1 liter/8 hours.)
         */
        public MedicationAdministrationDosageComponent setRate(Type value) { 
          this.rate = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("text", "string", "Free text dosage instructions can be used for cases where the instructions are too complex to code. When coded instructions are present, the free text instructions may still be present for display to humans taking or administering the medication.", 0, java.lang.Integer.MAX_VALUE, text));
          childrenList.add(new Property("site[x]", "CodeableConcept|Reference(BodySite)", "A coded specification of the anatomic site where the medication first entered the body.  For example, \"left arm\".", 0, java.lang.Integer.MAX_VALUE, site));
          childrenList.add(new Property("route", "CodeableConcept", "A code specifying the route or physiological path of administration of a therapeutic agent into or onto the patient.  For example, topical, intravenous, etc.", 0, java.lang.Integer.MAX_VALUE, route));
          childrenList.add(new Property("method", "CodeableConcept", "A coded value indicating the method by which the medication is intended to be or was introduced into or on the body.  This attribute will most often NOT be populated.  It is most commonly used for injections.  For example, Slow Push, Deep IV.", 0, java.lang.Integer.MAX_VALUE, method));
          childrenList.add(new Property("quantity", "SimpleQuantity", "The amount of the medication given at one administration event.   Use this value when the administration is essentially an instantaneous event such as a swallowing a tablet or giving an injection.", 0, java.lang.Integer.MAX_VALUE, quantity));
          childrenList.add(new Property("rate[x]", "Ratio|Range", "Identifies the speed with which the medication was or will be introduced into the patient.  Typically the rate for an infusion e.g. 100 ml per 1 hour or 100 ml/hr.  May also be expressed as a rate per unit of time e.g. 500 ml per 2 hours.  Currently we do not specify a default of '1' in the denominator, but this is being discussed.  Other examples:  200 mcg/min or 200 mcg/1 minute; 1 liter/8 hours.", 0, java.lang.Integer.MAX_VALUE, rate));
        }

      public MedicationAdministrationDosageComponent copy() {
        MedicationAdministrationDosageComponent dst = new MedicationAdministrationDosageComponent();
        copyValues(dst);
        dst.text = text == null ? null : text.copy();
        dst.site = site == null ? null : site.copy();
        dst.route = route == null ? null : route.copy();
        dst.method = method == null ? null : method.copy();
        dst.quantity = quantity == null ? null : quantity.copy();
        dst.rate = rate == null ? null : rate.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof MedicationAdministrationDosageComponent))
          return false;
        MedicationAdministrationDosageComponent o = (MedicationAdministrationDosageComponent) other;
        return compareDeep(text, o.text, true) && compareDeep(site, o.site, true) && compareDeep(route, o.route, true)
           && compareDeep(method, o.method, true) && compareDeep(quantity, o.quantity, true) && compareDeep(rate, o.rate, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof MedicationAdministrationDosageComponent))
          return false;
        MedicationAdministrationDosageComponent o = (MedicationAdministrationDosageComponent) other;
        return compareValues(text, o.text, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (text == null || text.isEmpty()) && (site == null || site.isEmpty())
           && (route == null || route.isEmpty()) && (method == null || method.isEmpty()) && (quantity == null || quantity.isEmpty())
           && (rate == null || rate.isEmpty());
      }

  }

    /**
     * External identifier - FHIR will generate its own internal identifiers (probably URLs) which do not need to be explicitly managed by the resource.  The identifier here is one that would be used by another non-FHIR system - for example an automated medication pump would provide a record each time it operated; an administration while the patient was off the ward might be made with a different system and entered after the event.  Particularly important if these records have to be updated.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="External identifier", formalDefinition="External identifier - FHIR will generate its own internal identifiers (probably URLs) which do not need to be explicitly managed by the resource.  The identifier here is one that would be used by another non-FHIR system - for example an automated medication pump would provide a record each time it operated; an administration while the patient was off the ward might be made with a different system and entered after the event.  Particularly important if these records have to be updated." )
    protected List<Identifier> identifier;

    /**
     * Will generally be set to show that the administration has been completed.  For some long running administrations such as infusions it is possible for an administration to be started but not completed or it may be paused while some other process is under way.
     */
    @Child(name = "status", type = {CodeType.class}, order=1, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="in-progress | on-hold | completed | entered-in-error | stopped", formalDefinition="Will generally be set to show that the administration has been completed.  For some long running administrations such as infusions it is possible for an administration to be started but not completed or it may be paused while some other process is under way." )
    protected Enumeration<MedicationAdministrationStatus> status;

    /**
     * The person or animal receiving the medication.
     */
    @Child(name = "patient", type = {Patient.class}, order=2, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Who received medication", formalDefinition="The person or animal receiving the medication." )
    protected Reference patient;

    /**
     * The actual object that is the target of the reference (The person or animal receiving the medication.)
     */
    protected Patient patientTarget;

    /**
     * The individual who was responsible for giving the medication to the patient.
     */
    @Child(name = "practitioner", type = {Practitioner.class, Patient.class, RelatedPerson.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Who administered substance", formalDefinition="The individual who was responsible for giving the medication to the patient." )
    protected Reference practitioner;

    /**
     * The actual object that is the target of the reference (The individual who was responsible for giving the medication to the patient.)
     */
    protected Resource practitionerTarget;

    /**
     * The visit, admission or other contact between patient and health care provider the medication administration was performed as part of.
     */
    @Child(name = "encounter", type = {Encounter.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Encounter administered as part of", formalDefinition="The visit, admission or other contact between patient and health care provider the medication administration was performed as part of." )
    protected Reference encounter;

    /**
     * The actual object that is the target of the reference (The visit, admission or other contact between patient and health care provider the medication administration was performed as part of.)
     */
    protected Encounter encounterTarget;

    /**
     * The original request, instruction or authority to perform the administration.
     */
    @Child(name = "prescription", type = {MedicationOrder.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Order administration performed against", formalDefinition="The original request, instruction or authority to perform the administration." )
    protected Reference prescription;

    /**
     * The actual object that is the target of the reference (The original request, instruction or authority to perform the administration.)
     */
    protected MedicationOrder prescriptionTarget;

    /**
     * Set this to true if the record is saying that the medication was NOT administered.
     */
    @Child(name = "wasNotGiven", type = {BooleanType.class}, order=6, min=0, max=1, modifier=true, summary=true)
    @Description(shortDefinition="True if medication not administered", formalDefinition="Set this to true if the record is saying that the medication was NOT administered." )
    protected BooleanType wasNotGiven;

    /**
     * A code indicating why the administration was not performed.
     */
    @Child(name = "reasonNotGiven", type = {CodeableConcept.class}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Reason administration not performed", formalDefinition="A code indicating why the administration was not performed." )
    protected List<CodeableConcept> reasonNotGiven;

    /**
     * A code indicating why the medication was given.
     */
    @Child(name = "reasonGiven", type = {CodeableConcept.class}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Reason administration performed", formalDefinition="A code indicating why the medication was given." )
    protected List<CodeableConcept> reasonGiven;

    /**
     * A specific date/time or interval of time during which the administration took place (or did not take place, when the 'notGiven' attribute is true). For many administrations, such as swallowing a tablet the use of dateTime is more appropriate.
     */
    @Child(name = "effectiveTime", type = {DateTimeType.class, Period.class}, order=9, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Start and end time of administration", formalDefinition="A specific date/time or interval of time during which the administration took place (or did not take place, when the 'notGiven' attribute is true). For many administrations, such as swallowing a tablet the use of dateTime is more appropriate." )
    protected Type effectiveTime;

    /**
     * Identifies the medication that was administered. This is either a link to a resource representing the details of the medication or a simple attribute carrying a code that identifies the medication from a known list of medications.
     */
    @Child(name = "medication", type = {CodeableConcept.class, Medication.class}, order=10, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="What was administered", formalDefinition="Identifies the medication that was administered. This is either a link to a resource representing the details of the medication or a simple attribute carrying a code that identifies the medication from a known list of medications." )
    protected Type medication;

    /**
     * The device used in administering the medication to the patient.  For example, a particular infusion pump.
     */
    @Child(name = "device", type = {Device.class}, order=11, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Device used to administer", formalDefinition="The device used in administering the medication to the patient.  For example, a particular infusion pump." )
    protected List<Reference> device;
    /**
     * The actual objects that are the target of the reference (The device used in administering the medication to the patient.  For example, a particular infusion pump.)
     */
    protected List<Device> deviceTarget;


    /**
     * Extra information about the medication administration that is not conveyed by the other attributes.
     */
    @Child(name = "note", type = {StringType.class}, order=12, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Information about the administration", formalDefinition="Extra information about the medication administration that is not conveyed by the other attributes." )
    protected StringType note;

    /**
     * Describes the medication dosage information details e.g. dose, rate, site, route, etc.
     */
    @Child(name = "dosage", type = {}, order=13, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Details of how medication was taken", formalDefinition="Describes the medication dosage information details e.g. dose, rate, site, route, etc." )
    protected MedicationAdministrationDosageComponent dosage;

    private static final long serialVersionUID = -669616345L;

  /*
   * Constructor
   */
    public MedicationAdministration() {
      super();
    }

  /*
   * Constructor
   */
    public MedicationAdministration(Enumeration<MedicationAdministrationStatus> status, Reference patient, Type effectiveTime, Type medication) {
      super();
      this.status = status;
      this.patient = patient;
      this.effectiveTime = effectiveTime;
      this.medication = medication;
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
    public MedicationAdministration addIdentifier(Identifier t) { //3
      if (t == null)
        return this;
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return this;
    }

    /**
     * @return {@link #status} (Will generally be set to show that the administration has been completed.  For some long running administrations such as infusions it is possible for an administration to be started but not completed or it may be paused while some other process is under way.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<MedicationAdministrationStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationAdministration.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<MedicationAdministrationStatus>(new MedicationAdministrationStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (Will generally be set to show that the administration has been completed.  For some long running administrations such as infusions it is possible for an administration to be started but not completed or it may be paused while some other process is under way.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public MedicationAdministration setStatusElement(Enumeration<MedicationAdministrationStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return Will generally be set to show that the administration has been completed.  For some long running administrations such as infusions it is possible for an administration to be started but not completed or it may be paused while some other process is under way.
     */
    public MedicationAdministrationStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value Will generally be set to show that the administration has been completed.  For some long running administrations such as infusions it is possible for an administration to be started but not completed or it may be paused while some other process is under way.
     */
    public MedicationAdministration setStatus(MedicationAdministrationStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<MedicationAdministrationStatus>(new MedicationAdministrationStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #patient} (The person or animal receiving the medication.)
     */
    public Reference getPatient() { 
      if (this.patient == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationAdministration.patient");
        else if (Configuration.doAutoCreate())
          this.patient = new Reference(); // cc
      return this.patient;
    }

    public boolean hasPatient() { 
      return this.patient != null && !this.patient.isEmpty();
    }

    /**
     * @param value {@link #patient} (The person or animal receiving the medication.)
     */
    public MedicationAdministration setPatient(Reference value) { 
      this.patient = value;
      return this;
    }

    /**
     * @return {@link #patient} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The person or animal receiving the medication.)
     */
    public Patient getPatientTarget() { 
      if (this.patientTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationAdministration.patient");
        else if (Configuration.doAutoCreate())
          this.patientTarget = new Patient(); // aa
      return this.patientTarget;
    }

    /**
     * @param value {@link #patient} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The person or animal receiving the medication.)
     */
    public MedicationAdministration setPatientTarget(Patient value) { 
      this.patientTarget = value;
      return this;
    }

    /**
     * @return {@link #practitioner} (The individual who was responsible for giving the medication to the patient.)
     */
    public Reference getPractitioner() { 
      if (this.practitioner == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationAdministration.practitioner");
        else if (Configuration.doAutoCreate())
          this.practitioner = new Reference(); // cc
      return this.practitioner;
    }

    public boolean hasPractitioner() { 
      return this.practitioner != null && !this.practitioner.isEmpty();
    }

    /**
     * @param value {@link #practitioner} (The individual who was responsible for giving the medication to the patient.)
     */
    public MedicationAdministration setPractitioner(Reference value) { 
      this.practitioner = value;
      return this;
    }

    /**
     * @return {@link #practitioner} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The individual who was responsible for giving the medication to the patient.)
     */
    public Resource getPractitionerTarget() { 
      return this.practitionerTarget;
    }

    /**
     * @param value {@link #practitioner} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The individual who was responsible for giving the medication to the patient.)
     */
    public MedicationAdministration setPractitionerTarget(Resource value) { 
      this.practitionerTarget = value;
      return this;
    }

    /**
     * @return {@link #encounter} (The visit, admission or other contact between patient and health care provider the medication administration was performed as part of.)
     */
    public Reference getEncounter() { 
      if (this.encounter == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationAdministration.encounter");
        else if (Configuration.doAutoCreate())
          this.encounter = new Reference(); // cc
      return this.encounter;
    }

    public boolean hasEncounter() { 
      return this.encounter != null && !this.encounter.isEmpty();
    }

    /**
     * @param value {@link #encounter} (The visit, admission or other contact between patient and health care provider the medication administration was performed as part of.)
     */
    public MedicationAdministration setEncounter(Reference value) { 
      this.encounter = value;
      return this;
    }

    /**
     * @return {@link #encounter} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The visit, admission or other contact between patient and health care provider the medication administration was performed as part of.)
     */
    public Encounter getEncounterTarget() { 
      if (this.encounterTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationAdministration.encounter");
        else if (Configuration.doAutoCreate())
          this.encounterTarget = new Encounter(); // aa
      return this.encounterTarget;
    }

    /**
     * @param value {@link #encounter} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The visit, admission or other contact between patient and health care provider the medication administration was performed as part of.)
     */
    public MedicationAdministration setEncounterTarget(Encounter value) { 
      this.encounterTarget = value;
      return this;
    }

    /**
     * @return {@link #prescription} (The original request, instruction or authority to perform the administration.)
     */
    public Reference getPrescription() { 
      if (this.prescription == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationAdministration.prescription");
        else if (Configuration.doAutoCreate())
          this.prescription = new Reference(); // cc
      return this.prescription;
    }

    public boolean hasPrescription() { 
      return this.prescription != null && !this.prescription.isEmpty();
    }

    /**
     * @param value {@link #prescription} (The original request, instruction or authority to perform the administration.)
     */
    public MedicationAdministration setPrescription(Reference value) { 
      this.prescription = value;
      return this;
    }

    /**
     * @return {@link #prescription} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The original request, instruction or authority to perform the administration.)
     */
    public MedicationOrder getPrescriptionTarget() { 
      if (this.prescriptionTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationAdministration.prescription");
        else if (Configuration.doAutoCreate())
          this.prescriptionTarget = new MedicationOrder(); // aa
      return this.prescriptionTarget;
    }

    /**
     * @param value {@link #prescription} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The original request, instruction or authority to perform the administration.)
     */
    public MedicationAdministration setPrescriptionTarget(MedicationOrder value) { 
      this.prescriptionTarget = value;
      return this;
    }

    /**
     * @return {@link #wasNotGiven} (Set this to true if the record is saying that the medication was NOT administered.). This is the underlying object with id, value and extensions. The accessor "getWasNotGiven" gives direct access to the value
     */
    public BooleanType getWasNotGivenElement() { 
      if (this.wasNotGiven == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationAdministration.wasNotGiven");
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
     * @param value {@link #wasNotGiven} (Set this to true if the record is saying that the medication was NOT administered.). This is the underlying object with id, value and extensions. The accessor "getWasNotGiven" gives direct access to the value
     */
    public MedicationAdministration setWasNotGivenElement(BooleanType value) { 
      this.wasNotGiven = value;
      return this;
    }

    /**
     * @return Set this to true if the record is saying that the medication was NOT administered.
     */
    public boolean getWasNotGiven() { 
      return this.wasNotGiven == null || this.wasNotGiven.isEmpty() ? false : this.wasNotGiven.getValue();
    }

    /**
     * @param value Set this to true if the record is saying that the medication was NOT administered.
     */
    public MedicationAdministration setWasNotGiven(boolean value) { 
        if (this.wasNotGiven == null)
          this.wasNotGiven = new BooleanType();
        this.wasNotGiven.setValue(value);
      return this;
    }

    /**
     * @return {@link #reasonNotGiven} (A code indicating why the administration was not performed.)
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
     * @return {@link #reasonNotGiven} (A code indicating why the administration was not performed.)
     */
    // syntactic sugar
    public CodeableConcept addReasonNotGiven() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.reasonNotGiven == null)
        this.reasonNotGiven = new ArrayList<CodeableConcept>();
      this.reasonNotGiven.add(t);
      return t;
    }

    // syntactic sugar
    public MedicationAdministration addReasonNotGiven(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.reasonNotGiven == null)
        this.reasonNotGiven = new ArrayList<CodeableConcept>();
      this.reasonNotGiven.add(t);
      return this;
    }

    /**
     * @return {@link #reasonGiven} (A code indicating why the medication was given.)
     */
    public List<CodeableConcept> getReasonGiven() { 
      if (this.reasonGiven == null)
        this.reasonGiven = new ArrayList<CodeableConcept>();
      return this.reasonGiven;
    }

    public boolean hasReasonGiven() { 
      if (this.reasonGiven == null)
        return false;
      for (CodeableConcept item : this.reasonGiven)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #reasonGiven} (A code indicating why the medication was given.)
     */
    // syntactic sugar
    public CodeableConcept addReasonGiven() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.reasonGiven == null)
        this.reasonGiven = new ArrayList<CodeableConcept>();
      this.reasonGiven.add(t);
      return t;
    }

    // syntactic sugar
    public MedicationAdministration addReasonGiven(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.reasonGiven == null)
        this.reasonGiven = new ArrayList<CodeableConcept>();
      this.reasonGiven.add(t);
      return this;
    }

    /**
     * @return {@link #effectiveTime} (A specific date/time or interval of time during which the administration took place (or did not take place, when the 'notGiven' attribute is true). For many administrations, such as swallowing a tablet the use of dateTime is more appropriate.)
     */
    public Type getEffectiveTime() { 
      return this.effectiveTime;
    }

    /**
     * @return {@link #effectiveTime} (A specific date/time or interval of time during which the administration took place (or did not take place, when the 'notGiven' attribute is true). For many administrations, such as swallowing a tablet the use of dateTime is more appropriate.)
     */
    public DateTimeType getEffectiveTimeDateTimeType() throws Exception { 
      if (!(this.effectiveTime instanceof DateTimeType))
        throw new Exception("Type mismatch: the type DateTimeType was expected, but "+this.effectiveTime.getClass().getName()+" was encountered");
      return (DateTimeType) this.effectiveTime;
    }

    public boolean hasEffectiveTimeDateTimeType() throws Exception { 
      return this.effectiveTime instanceof DateTimeType;
    }

    /**
     * @return {@link #effectiveTime} (A specific date/time or interval of time during which the administration took place (or did not take place, when the 'notGiven' attribute is true). For many administrations, such as swallowing a tablet the use of dateTime is more appropriate.)
     */
    public Period getEffectiveTimePeriod() throws Exception { 
      if (!(this.effectiveTime instanceof Period))
        throw new Exception("Type mismatch: the type Period was expected, but "+this.effectiveTime.getClass().getName()+" was encountered");
      return (Period) this.effectiveTime;
    }

    public boolean hasEffectiveTimePeriod() throws Exception { 
      return this.effectiveTime instanceof Period;
    }

    public boolean hasEffectiveTime() { 
      return this.effectiveTime != null && !this.effectiveTime.isEmpty();
    }

    /**
     * @param value {@link #effectiveTime} (A specific date/time or interval of time during which the administration took place (or did not take place, when the 'notGiven' attribute is true). For many administrations, such as swallowing a tablet the use of dateTime is more appropriate.)
     */
    public MedicationAdministration setEffectiveTime(Type value) { 
      this.effectiveTime = value;
      return this;
    }

    /**
     * @return {@link #medication} (Identifies the medication that was administered. This is either a link to a resource representing the details of the medication or a simple attribute carrying a code that identifies the medication from a known list of medications.)
     */
    public Type getMedication() { 
      return this.medication;
    }

    /**
     * @return {@link #medication} (Identifies the medication that was administered. This is either a link to a resource representing the details of the medication or a simple attribute carrying a code that identifies the medication from a known list of medications.)
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
     * @return {@link #medication} (Identifies the medication that was administered. This is either a link to a resource representing the details of the medication or a simple attribute carrying a code that identifies the medication from a known list of medications.)
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
     * @param value {@link #medication} (Identifies the medication that was administered. This is either a link to a resource representing the details of the medication or a simple attribute carrying a code that identifies the medication from a known list of medications.)
     */
    public MedicationAdministration setMedication(Type value) { 
      this.medication = value;
      return this;
    }

    /**
     * @return {@link #device} (The device used in administering the medication to the patient.  For example, a particular infusion pump.)
     */
    public List<Reference> getDevice() { 
      if (this.device == null)
        this.device = new ArrayList<Reference>();
      return this.device;
    }

    public boolean hasDevice() { 
      if (this.device == null)
        return false;
      for (Reference item : this.device)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #device} (The device used in administering the medication to the patient.  For example, a particular infusion pump.)
     */
    // syntactic sugar
    public Reference addDevice() { //3
      Reference t = new Reference();
      if (this.device == null)
        this.device = new ArrayList<Reference>();
      this.device.add(t);
      return t;
    }

    // syntactic sugar
    public MedicationAdministration addDevice(Reference t) { //3
      if (t == null)
        return this;
      if (this.device == null)
        this.device = new ArrayList<Reference>();
      this.device.add(t);
      return this;
    }

    /**
     * @return {@link #device} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. The device used in administering the medication to the patient.  For example, a particular infusion pump.)
     */
    public List<Device> getDeviceTarget() { 
      if (this.deviceTarget == null)
        this.deviceTarget = new ArrayList<Device>();
      return this.deviceTarget;
    }

    // syntactic sugar
    /**
     * @return {@link #device} (Add an actual object that is the target of the reference. The reference library doesn't use these, but you can use this to hold the resources if you resolvethemt. The device used in administering the medication to the patient.  For example, a particular infusion pump.)
     */
    public Device addDeviceTarget() { 
      Device r = new Device();
      if (this.deviceTarget == null)
        this.deviceTarget = new ArrayList<Device>();
      this.deviceTarget.add(r);
      return r;
    }

    /**
     * @return {@link #note} (Extra information about the medication administration that is not conveyed by the other attributes.). This is the underlying object with id, value and extensions. The accessor "getNote" gives direct access to the value
     */
    public StringType getNoteElement() { 
      if (this.note == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationAdministration.note");
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
     * @param value {@link #note} (Extra information about the medication administration that is not conveyed by the other attributes.). This is the underlying object with id, value and extensions. The accessor "getNote" gives direct access to the value
     */
    public MedicationAdministration setNoteElement(StringType value) { 
      this.note = value;
      return this;
    }

    /**
     * @return Extra information about the medication administration that is not conveyed by the other attributes.
     */
    public String getNote() { 
      return this.note == null ? null : this.note.getValue();
    }

    /**
     * @param value Extra information about the medication administration that is not conveyed by the other attributes.
     */
    public MedicationAdministration setNote(String value) { 
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
     * @return {@link #dosage} (Describes the medication dosage information details e.g. dose, rate, site, route, etc.)
     */
    public MedicationAdministrationDosageComponent getDosage() { 
      if (this.dosage == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationAdministration.dosage");
        else if (Configuration.doAutoCreate())
          this.dosage = new MedicationAdministrationDosageComponent(); // cc
      return this.dosage;
    }

    public boolean hasDosage() { 
      return this.dosage != null && !this.dosage.isEmpty();
    }

    /**
     * @param value {@link #dosage} (Describes the medication dosage information details e.g. dose, rate, site, route, etc.)
     */
    public MedicationAdministration setDosage(MedicationAdministrationDosageComponent value) { 
      this.dosage = value;
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "External identifier - FHIR will generate its own internal identifiers (probably URLs) which do not need to be explicitly managed by the resource.  The identifier here is one that would be used by another non-FHIR system - for example an automated medication pump would provide a record each time it operated; an administration while the patient was off the ward might be made with a different system and entered after the event.  Particularly important if these records have to be updated.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("status", "code", "Will generally be set to show that the administration has been completed.  For some long running administrations such as infusions it is possible for an administration to be started but not completed or it may be paused while some other process is under way.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("patient", "Reference(Patient)", "The person or animal receiving the medication.", 0, java.lang.Integer.MAX_VALUE, patient));
        childrenList.add(new Property("practitioner", "Reference(Practitioner|Patient|RelatedPerson)", "The individual who was responsible for giving the medication to the patient.", 0, java.lang.Integer.MAX_VALUE, practitioner));
        childrenList.add(new Property("encounter", "Reference(Encounter)", "The visit, admission or other contact between patient and health care provider the medication administration was performed as part of.", 0, java.lang.Integer.MAX_VALUE, encounter));
        childrenList.add(new Property("prescription", "Reference(MedicationOrder)", "The original request, instruction or authority to perform the administration.", 0, java.lang.Integer.MAX_VALUE, prescription));
        childrenList.add(new Property("wasNotGiven", "boolean", "Set this to true if the record is saying that the medication was NOT administered.", 0, java.lang.Integer.MAX_VALUE, wasNotGiven));
        childrenList.add(new Property("reasonNotGiven", "CodeableConcept", "A code indicating why the administration was not performed.", 0, java.lang.Integer.MAX_VALUE, reasonNotGiven));
        childrenList.add(new Property("reasonGiven", "CodeableConcept", "A code indicating why the medication was given.", 0, java.lang.Integer.MAX_VALUE, reasonGiven));
        childrenList.add(new Property("effectiveTime[x]", "dateTime|Period", "A specific date/time or interval of time during which the administration took place (or did not take place, when the 'notGiven' attribute is true). For many administrations, such as swallowing a tablet the use of dateTime is more appropriate.", 0, java.lang.Integer.MAX_VALUE, effectiveTime));
        childrenList.add(new Property("medication[x]", "CodeableConcept|Reference(Medication)", "Identifies the medication that was administered. This is either a link to a resource representing the details of the medication or a simple attribute carrying a code that identifies the medication from a known list of medications.", 0, java.lang.Integer.MAX_VALUE, medication));
        childrenList.add(new Property("device", "Reference(Device)", "The device used in administering the medication to the patient.  For example, a particular infusion pump.", 0, java.lang.Integer.MAX_VALUE, device));
        childrenList.add(new Property("note", "string", "Extra information about the medication administration that is not conveyed by the other attributes.", 0, java.lang.Integer.MAX_VALUE, note));
        childrenList.add(new Property("dosage", "", "Describes the medication dosage information details e.g. dose, rate, site, route, etc.", 0, java.lang.Integer.MAX_VALUE, dosage));
      }

      public MedicationAdministration copy() {
        MedicationAdministration dst = new MedicationAdministration();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.status = status == null ? null : status.copy();
        dst.patient = patient == null ? null : patient.copy();
        dst.practitioner = practitioner == null ? null : practitioner.copy();
        dst.encounter = encounter == null ? null : encounter.copy();
        dst.prescription = prescription == null ? null : prescription.copy();
        dst.wasNotGiven = wasNotGiven == null ? null : wasNotGiven.copy();
        if (reasonNotGiven != null) {
          dst.reasonNotGiven = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : reasonNotGiven)
            dst.reasonNotGiven.add(i.copy());
        };
        if (reasonGiven != null) {
          dst.reasonGiven = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : reasonGiven)
            dst.reasonGiven.add(i.copy());
        };
        dst.effectiveTime = effectiveTime == null ? null : effectiveTime.copy();
        dst.medication = medication == null ? null : medication.copy();
        if (device != null) {
          dst.device = new ArrayList<Reference>();
          for (Reference i : device)
            dst.device.add(i.copy());
        };
        dst.note = note == null ? null : note.copy();
        dst.dosage = dosage == null ? null : dosage.copy();
        return dst;
      }

      protected MedicationAdministration typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof MedicationAdministration))
          return false;
        MedicationAdministration o = (MedicationAdministration) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(status, o.status, true) && compareDeep(patient, o.patient, true)
           && compareDeep(practitioner, o.practitioner, true) && compareDeep(encounter, o.encounter, true)
           && compareDeep(prescription, o.prescription, true) && compareDeep(wasNotGiven, o.wasNotGiven, true)
           && compareDeep(reasonNotGiven, o.reasonNotGiven, true) && compareDeep(reasonGiven, o.reasonGiven, true)
           && compareDeep(effectiveTime, o.effectiveTime, true) && compareDeep(medication, o.medication, true)
           && compareDeep(device, o.device, true) && compareDeep(note, o.note, true) && compareDeep(dosage, o.dosage, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof MedicationAdministration))
          return false;
        MedicationAdministration o = (MedicationAdministration) other;
        return compareValues(status, o.status, true) && compareValues(wasNotGiven, o.wasNotGiven, true) && compareValues(note, o.note, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (status == null || status.isEmpty())
           && (patient == null || patient.isEmpty()) && (practitioner == null || practitioner.isEmpty())
           && (encounter == null || encounter.isEmpty()) && (prescription == null || prescription.isEmpty())
           && (wasNotGiven == null || wasNotGiven.isEmpty()) && (reasonNotGiven == null || reasonNotGiven.isEmpty())
           && (reasonGiven == null || reasonGiven.isEmpty()) && (effectiveTime == null || effectiveTime.isEmpty())
           && (medication == null || medication.isEmpty()) && (device == null || device.isEmpty()) && (note == null || note.isEmpty())
           && (dosage == null || dosage.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.MedicationAdministration;
   }

  @SearchParamDefinition(name="identifier", path="MedicationAdministration.identifier", description="Return administrations with this external identifier", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
  @SearchParamDefinition(name="code", path="MedicationAdministration.medicationCodeableConcept", description="Return administrations of this medication code", type="token" )
  public static final String SP_CODE = "code";
  @SearchParamDefinition(name="prescription", path="MedicationAdministration.prescription", description="The identity of a prescription to list administrations from", type="reference" )
  public static final String SP_PRESCRIPTION = "prescription";
  @SearchParamDefinition(name="effectivetime", path="MedicationAdministration.effectiveTime[x]", description="Date administration happened (or did not happen)", type="date" )
  public static final String SP_EFFECTIVETIME = "effectivetime";
  @SearchParamDefinition(name="practitioner", path="MedicationAdministration.practitioner", description="Who administered substance", type="reference" )
  public static final String SP_PRACTITIONER = "practitioner";
  @SearchParamDefinition(name="patient", path="MedicationAdministration.patient", description="The identity of a patient to list administrations  for", type="reference" )
  public static final String SP_PATIENT = "patient";
  @SearchParamDefinition(name="medication", path="MedicationAdministration.medicationReference", description="Return administrations of this medication resource", type="reference" )
  public static final String SP_MEDICATION = "medication";
  @SearchParamDefinition(name="encounter", path="MedicationAdministration.encounter", description="Return administrations that share this encounter", type="reference" )
  public static final String SP_ENCOUNTER = "encounter";
  @SearchParamDefinition(name="device", path="MedicationAdministration.device", description="Return administrations with this administration device identity", type="reference" )
  public static final String SP_DEVICE = "device";
  @SearchParamDefinition(name="notgiven", path="MedicationAdministration.wasNotGiven", description="Administrations that were not made", type="token" )
  public static final String SP_NOTGIVEN = "notgiven";
  @SearchParamDefinition(name="status", path="MedicationAdministration.status", description="MedicationAdministration event status (for example one of active/paused/completed/nullified)", type="token" )
  public static final String SP_STATUS = "status";

}

