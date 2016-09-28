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

// Generated on Thu, Aug 25, 2016 23:04-0400 for FHIR v1.6.0
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseBackboneElement;
import org.hl7.fhir.utilities.Utilities;

import ca.uhn.fhir.model.api.annotation.*;
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
         * added to help the parsers with the generic types
         */
        NULL;
        public static MedicationAdministrationStatus fromCode(String codeString) throws FHIRException {
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
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown MedicationAdministrationStatus code '"+codeString+"'");
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
        public Enumeration<MedicationAdministrationStatus> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("in-progress".equals(codeString))
          return new Enumeration<MedicationAdministrationStatus>(this, MedicationAdministrationStatus.INPROGRESS);
        if ("on-hold".equals(codeString))
          return new Enumeration<MedicationAdministrationStatus>(this, MedicationAdministrationStatus.ONHOLD);
        if ("completed".equals(codeString))
          return new Enumeration<MedicationAdministrationStatus>(this, MedicationAdministrationStatus.COMPLETED);
        if ("entered-in-error".equals(codeString))
          return new Enumeration<MedicationAdministrationStatus>(this, MedicationAdministrationStatus.ENTEREDINERROR);
        if ("stopped".equals(codeString))
          return new Enumeration<MedicationAdministrationStatus>(this, MedicationAdministrationStatus.STOPPED);
        throw new FHIRException("Unknown MedicationAdministrationStatus code '"+codeString+"'");
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
    public String toSystem(MedicationAdministrationStatus code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class MedicationAdministrationDosageComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Free text dosage can be used for cases where the dosage administered is too complex to code. When coded dosage is present, the free text dosage may still be present for display to humans.The dosage instructions should reflect the dosage of the medication that was administered.
         */
        @Child(name = "text", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Free text dosage instructions e.g. SIG", formalDefinition="Free text dosage can be used for cases where the dosage administered is too complex to code. When coded dosage is present, the free text dosage may still be present for display to humans.\r\rThe dosage instructions should reflect the dosage of the medication that was administered." )
        protected StringType text;

        /**
         * A coded specification of the anatomic site where the medication first entered the body.  For example, "left arm".
         */
        @Child(name = "site", type = {CodeableConcept.class, BodySite.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Body site administered to", formalDefinition="A coded specification of the anatomic site where the medication first entered the body.  For example, \"left arm\"." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/approach-site-codes")
        protected Type site;

        /**
         * A code specifying the route or physiological path of administration of a therapeutic agent into or onto the patient.  For example, topical, intravenous, etc.
         */
        @Child(name = "route", type = {CodeableConcept.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Path of substance into body", formalDefinition="A code specifying the route or physiological path of administration of a therapeutic agent into or onto the patient.  For example, topical, intravenous, etc." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/route-codes")
        protected CodeableConcept route;

        /**
         * A coded value indicating the method by which the medication is intended to be or was introduced into or on the body.  This attribute will most often NOT be populated.  It is most commonly used for injections.  For example, Slow Push, Deep IV.
         */
        @Child(name = "method", type = {CodeableConcept.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="How drug was administered", formalDefinition="A coded value indicating the method by which the medication is intended to be or was introduced into or on the body.  This attribute will most often NOT be populated.  It is most commonly used for injections.  For example, Slow Push, Deep IV." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/administration-method-codes")
        protected CodeableConcept method;

        /**
         * The amount of the medication given at one administration event.   Use this value when the administration is essentially an instantaneous event such as a swallowing a tablet or giving an injection.
         */
        @Child(name = "dose", type = {SimpleQuantity.class}, order=5, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Amount of medication per dose", formalDefinition="The amount of the medication given at one administration event.   Use this value when the administration is essentially an instantaneous event such as a swallowing a tablet or giving an injection." )
        protected SimpleQuantity dose;

        /**
         * Identifies the speed with which the medication was or will be introduced into the patient.  Typically the rate for an infusion e.g. 100 ml per 1 hour or 100 ml/hr.  May also be expressed as a rate per unit of time e.g. 500 ml per 2 hours.  Other examples:  200 mcg/min or 200 mcg/1 minute; 1 liter/8 hours.
         */
        @Child(name = "rate", type = {Ratio.class, SimpleQuantity.class}, order=6, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Dose quantity per unit of time", formalDefinition="Identifies the speed with which the medication was or will be introduced into the patient.  Typically the rate for an infusion e.g. 100 ml per 1 hour or 100 ml/hr.  May also be expressed as a rate per unit of time e.g. 500 ml per 2 hours.  Other examples:  200 mcg/min or 200 mcg/1 minute; 1 liter/8 hours." )
        protected Type rate;

        private static final long serialVersionUID = -1359632689L;

    /**
     * Constructor
     */
      public MedicationAdministrationDosageComponent() {
        super();
      }

        /**
         * @return {@link #text} (Free text dosage can be used for cases where the dosage administered is too complex to code. When coded dosage is present, the free text dosage may still be present for display to humans.The dosage instructions should reflect the dosage of the medication that was administered.). This is the underlying object with id, value and extensions. The accessor "getText" gives direct access to the value
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
         * @param value {@link #text} (Free text dosage can be used for cases where the dosage administered is too complex to code. When coded dosage is present, the free text dosage may still be present for display to humans.The dosage instructions should reflect the dosage of the medication that was administered.). This is the underlying object with id, value and extensions. The accessor "getText" gives direct access to the value
         */
        public MedicationAdministrationDosageComponent setTextElement(StringType value) { 
          this.text = value;
          return this;
        }

        /**
         * @return Free text dosage can be used for cases where the dosage administered is too complex to code. When coded dosage is present, the free text dosage may still be present for display to humans.The dosage instructions should reflect the dosage of the medication that was administered.
         */
        public String getText() { 
          return this.text == null ? null : this.text.getValue();
        }

        /**
         * @param value Free text dosage can be used for cases where the dosage administered is too complex to code. When coded dosage is present, the free text dosage may still be present for display to humans.The dosage instructions should reflect the dosage of the medication that was administered.
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
        public CodeableConcept getSiteCodeableConcept() throws FHIRException { 
          if (!(this.site instanceof CodeableConcept))
            throw new FHIRException("Type mismatch: the type CodeableConcept was expected, but "+this.site.getClass().getName()+" was encountered");
          return (CodeableConcept) this.site;
        }

        public boolean hasSiteCodeableConcept() { 
          return this.site instanceof CodeableConcept;
        }

        /**
         * @return {@link #site} (A coded specification of the anatomic site where the medication first entered the body.  For example, "left arm".)
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
         * @return {@link #dose} (The amount of the medication given at one administration event.   Use this value when the administration is essentially an instantaneous event such as a swallowing a tablet or giving an injection.)
         */
        public SimpleQuantity getDose() { 
          if (this.dose == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationAdministrationDosageComponent.dose");
            else if (Configuration.doAutoCreate())
              this.dose = new SimpleQuantity(); // cc
          return this.dose;
        }

        public boolean hasDose() { 
          return this.dose != null && !this.dose.isEmpty();
        }

        /**
         * @param value {@link #dose} (The amount of the medication given at one administration event.   Use this value when the administration is essentially an instantaneous event such as a swallowing a tablet or giving an injection.)
         */
        public MedicationAdministrationDosageComponent setDose(SimpleQuantity value) { 
          this.dose = value;
          return this;
        }

        /**
         * @return {@link #rate} (Identifies the speed with which the medication was or will be introduced into the patient.  Typically the rate for an infusion e.g. 100 ml per 1 hour or 100 ml/hr.  May also be expressed as a rate per unit of time e.g. 500 ml per 2 hours.  Other examples:  200 mcg/min or 200 mcg/1 minute; 1 liter/8 hours.)
         */
        public Type getRate() { 
          return this.rate;
        }

        /**
         * @return {@link #rate} (Identifies the speed with which the medication was or will be introduced into the patient.  Typically the rate for an infusion e.g. 100 ml per 1 hour or 100 ml/hr.  May also be expressed as a rate per unit of time e.g. 500 ml per 2 hours.  Other examples:  200 mcg/min or 200 mcg/1 minute; 1 liter/8 hours.)
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
         * @return {@link #rate} (Identifies the speed with which the medication was or will be introduced into the patient.  Typically the rate for an infusion e.g. 100 ml per 1 hour or 100 ml/hr.  May also be expressed as a rate per unit of time e.g. 500 ml per 2 hours.  Other examples:  200 mcg/min or 200 mcg/1 minute; 1 liter/8 hours.)
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
         * @param value {@link #rate} (Identifies the speed with which the medication was or will be introduced into the patient.  Typically the rate for an infusion e.g. 100 ml per 1 hour or 100 ml/hr.  May also be expressed as a rate per unit of time e.g. 500 ml per 2 hours.  Other examples:  200 mcg/min or 200 mcg/1 minute; 1 liter/8 hours.)
         */
        public MedicationAdministrationDosageComponent setRate(Type value) { 
          this.rate = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("text", "string", "Free text dosage can be used for cases where the dosage administered is too complex to code. When coded dosage is present, the free text dosage may still be present for display to humans.\r\rThe dosage instructions should reflect the dosage of the medication that was administered.", 0, java.lang.Integer.MAX_VALUE, text));
          childrenList.add(new Property("site[x]", "CodeableConcept|Reference(BodySite)", "A coded specification of the anatomic site where the medication first entered the body.  For example, \"left arm\".", 0, java.lang.Integer.MAX_VALUE, site));
          childrenList.add(new Property("route", "CodeableConcept", "A code specifying the route or physiological path of administration of a therapeutic agent into or onto the patient.  For example, topical, intravenous, etc.", 0, java.lang.Integer.MAX_VALUE, route));
          childrenList.add(new Property("method", "CodeableConcept", "A coded value indicating the method by which the medication is intended to be or was introduced into or on the body.  This attribute will most often NOT be populated.  It is most commonly used for injections.  For example, Slow Push, Deep IV.", 0, java.lang.Integer.MAX_VALUE, method));
          childrenList.add(new Property("dose", "SimpleQuantity", "The amount of the medication given at one administration event.   Use this value when the administration is essentially an instantaneous event such as a swallowing a tablet or giving an injection.", 0, java.lang.Integer.MAX_VALUE, dose));
          childrenList.add(new Property("rate[x]", "Ratio|SimpleQuantity", "Identifies the speed with which the medication was or will be introduced into the patient.  Typically the rate for an infusion e.g. 100 ml per 1 hour or 100 ml/hr.  May also be expressed as a rate per unit of time e.g. 500 ml per 2 hours.  Other examples:  200 mcg/min or 200 mcg/1 minute; 1 liter/8 hours.", 0, java.lang.Integer.MAX_VALUE, rate));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3556653: /*text*/ return this.text == null ? new Base[0] : new Base[] {this.text}; // StringType
        case 3530567: /*site*/ return this.site == null ? new Base[0] : new Base[] {this.site}; // Type
        case 108704329: /*route*/ return this.route == null ? new Base[0] : new Base[] {this.route}; // CodeableConcept
        case -1077554975: /*method*/ return this.method == null ? new Base[0] : new Base[] {this.method}; // CodeableConcept
        case 3089437: /*dose*/ return this.dose == null ? new Base[0] : new Base[] {this.dose}; // SimpleQuantity
        case 3493088: /*rate*/ return this.rate == null ? new Base[0] : new Base[] {this.rate}; // Type
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3556653: // text
          this.text = castToString(value); // StringType
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
          this.dose = castToSimpleQuantity(value); // SimpleQuantity
          break;
        case 3493088: // rate
          this.rate = (Type) value; // Type
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("text"))
          this.text = castToString(value); // StringType
        else if (name.equals("site[x]"))
          this.site = (Type) value; // Type
        else if (name.equals("route"))
          this.route = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("method"))
          this.method = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("dose"))
          this.dose = castToSimpleQuantity(value); // SimpleQuantity
        else if (name.equals("rate[x]"))
          this.rate = (Type) value; // Type
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3556653: throw new FHIRException("Cannot make property text as it is not a complex type"); // StringType
        case 2099997657:  return getSite(); // Type
        case 108704329:  return getRoute(); // CodeableConcept
        case -1077554975:  return getMethod(); // CodeableConcept
        case 3089437:  return getDose(); // SimpleQuantity
        case 983460768:  return getRate(); // Type
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("text")) {
          throw new FHIRException("Cannot call addChild on a primitive type MedicationAdministration.text");
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
        else if (name.equals("dose")) {
          this.dose = new SimpleQuantity();
          return this.dose;
        }
        else if (name.equals("rateRatio")) {
          this.rate = new Ratio();
          return this.rate;
        }
        else if (name.equals("rateSimpleQuantity")) {
          this.rate = new SimpleQuantity();
          return this.rate;
        }
        else
          return super.addChild(name);
      }

      public MedicationAdministrationDosageComponent copy() {
        MedicationAdministrationDosageComponent dst = new MedicationAdministrationDosageComponent();
        copyValues(dst);
        dst.text = text == null ? null : text.copy();
        dst.site = site == null ? null : site.copy();
        dst.route = route == null ? null : route.copy();
        dst.method = method == null ? null : method.copy();
        dst.dose = dose == null ? null : dose.copy();
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
           && compareDeep(method, o.method, true) && compareDeep(dose, o.dose, true) && compareDeep(rate, o.rate, true)
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
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(text, site, route, method
          , dose, rate);
      }

  public String fhirType() {
    return "MedicationAdministration.dosage";

  }

  }

    @Block()
    public static class MedicationAdministrationEventHistoryComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The status for the event.
         */
        @Child(name = "status", type = {CodeType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="in-progress | on-hold | completed | entered-in-error | stopped", formalDefinition="The status for the event." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/medication-admin-status")
        protected Enumeration<MedicationAdministrationStatus> status;

        /**
         * The action that was taken (e.g. verify).
         */
        @Child(name = "action", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Action taken (e.g. verify)", formalDefinition="The action that was taken (e.g. verify)." )
        protected CodeableConcept action;

        /**
         * The date/time at which the event occurred.
         */
        @Child(name = "dateTime", type = {DateTimeType.class}, order=3, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The date at which the event happened", formalDefinition="The date/time at which the event occurred." )
        protected DateTimeType dateTime;

        /**
         * The person responsible for taking the action.
         */
        @Child(name = "actor", type = {Practitioner.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Who took the action", formalDefinition="The person responsible for taking the action." )
        protected Reference actor;

        /**
         * The actual object that is the target of the reference (The person responsible for taking the action.)
         */
        protected Practitioner actorTarget;

        /**
         * The reason why the action was taken.
         */
        @Child(name = "reason", type = {CodeableConcept.class}, order=5, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Reason the action was taken", formalDefinition="The reason why the action was taken." )
        protected CodeableConcept reason;

        private static final long serialVersionUID = 1371667422L;

    /**
     * Constructor
     */
      public MedicationAdministrationEventHistoryComponent() {
        super();
      }

    /**
     * Constructor
     */
      public MedicationAdministrationEventHistoryComponent(Enumeration<MedicationAdministrationStatus> status, DateTimeType dateTime) {
        super();
        this.status = status;
        this.dateTime = dateTime;
      }

        /**
         * @return {@link #status} (The status for the event.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
         */
        public Enumeration<MedicationAdministrationStatus> getStatusElement() { 
          if (this.status == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationAdministrationEventHistoryComponent.status");
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
         * @param value {@link #status} (The status for the event.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
         */
        public MedicationAdministrationEventHistoryComponent setStatusElement(Enumeration<MedicationAdministrationStatus> value) { 
          this.status = value;
          return this;
        }

        /**
         * @return The status for the event.
         */
        public MedicationAdministrationStatus getStatus() { 
          return this.status == null ? null : this.status.getValue();
        }

        /**
         * @param value The status for the event.
         */
        public MedicationAdministrationEventHistoryComponent setStatus(MedicationAdministrationStatus value) { 
            if (this.status == null)
              this.status = new Enumeration<MedicationAdministrationStatus>(new MedicationAdministrationStatusEnumFactory());
            this.status.setValue(value);
          return this;
        }

        /**
         * @return {@link #action} (The action that was taken (e.g. verify).)
         */
        public CodeableConcept getAction() { 
          if (this.action == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationAdministrationEventHistoryComponent.action");
            else if (Configuration.doAutoCreate())
              this.action = new CodeableConcept(); // cc
          return this.action;
        }

        public boolean hasAction() { 
          return this.action != null && !this.action.isEmpty();
        }

        /**
         * @param value {@link #action} (The action that was taken (e.g. verify).)
         */
        public MedicationAdministrationEventHistoryComponent setAction(CodeableConcept value) { 
          this.action = value;
          return this;
        }

        /**
         * @return {@link #dateTime} (The date/time at which the event occurred.). This is the underlying object with id, value and extensions. The accessor "getDateTime" gives direct access to the value
         */
        public DateTimeType getDateTimeElement() { 
          if (this.dateTime == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationAdministrationEventHistoryComponent.dateTime");
            else if (Configuration.doAutoCreate())
              this.dateTime = new DateTimeType(); // bb
          return this.dateTime;
        }

        public boolean hasDateTimeElement() { 
          return this.dateTime != null && !this.dateTime.isEmpty();
        }

        public boolean hasDateTime() { 
          return this.dateTime != null && !this.dateTime.isEmpty();
        }

        /**
         * @param value {@link #dateTime} (The date/time at which the event occurred.). This is the underlying object with id, value and extensions. The accessor "getDateTime" gives direct access to the value
         */
        public MedicationAdministrationEventHistoryComponent setDateTimeElement(DateTimeType value) { 
          this.dateTime = value;
          return this;
        }

        /**
         * @return The date/time at which the event occurred.
         */
        public Date getDateTime() { 
          return this.dateTime == null ? null : this.dateTime.getValue();
        }

        /**
         * @param value The date/time at which the event occurred.
         */
        public MedicationAdministrationEventHistoryComponent setDateTime(Date value) { 
            if (this.dateTime == null)
              this.dateTime = new DateTimeType();
            this.dateTime.setValue(value);
          return this;
        }

        /**
         * @return {@link #actor} (The person responsible for taking the action.)
         */
        public Reference getActor() { 
          if (this.actor == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationAdministrationEventHistoryComponent.actor");
            else if (Configuration.doAutoCreate())
              this.actor = new Reference(); // cc
          return this.actor;
        }

        public boolean hasActor() { 
          return this.actor != null && !this.actor.isEmpty();
        }

        /**
         * @param value {@link #actor} (The person responsible for taking the action.)
         */
        public MedicationAdministrationEventHistoryComponent setActor(Reference value) { 
          this.actor = value;
          return this;
        }

        /**
         * @return {@link #actor} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The person responsible for taking the action.)
         */
        public Practitioner getActorTarget() { 
          if (this.actorTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationAdministrationEventHistoryComponent.actor");
            else if (Configuration.doAutoCreate())
              this.actorTarget = new Practitioner(); // aa
          return this.actorTarget;
        }

        /**
         * @param value {@link #actor} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The person responsible for taking the action.)
         */
        public MedicationAdministrationEventHistoryComponent setActorTarget(Practitioner value) { 
          this.actorTarget = value;
          return this;
        }

        /**
         * @return {@link #reason} (The reason why the action was taken.)
         */
        public CodeableConcept getReason() { 
          if (this.reason == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationAdministrationEventHistoryComponent.reason");
            else if (Configuration.doAutoCreate())
              this.reason = new CodeableConcept(); // cc
          return this.reason;
        }

        public boolean hasReason() { 
          return this.reason != null && !this.reason.isEmpty();
        }

        /**
         * @param value {@link #reason} (The reason why the action was taken.)
         */
        public MedicationAdministrationEventHistoryComponent setReason(CodeableConcept value) { 
          this.reason = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("status", "code", "The status for the event.", 0, java.lang.Integer.MAX_VALUE, status));
          childrenList.add(new Property("action", "CodeableConcept", "The action that was taken (e.g. verify).", 0, java.lang.Integer.MAX_VALUE, action));
          childrenList.add(new Property("dateTime", "dateTime", "The date/time at which the event occurred.", 0, java.lang.Integer.MAX_VALUE, dateTime));
          childrenList.add(new Property("actor", "Reference(Practitioner)", "The person responsible for taking the action.", 0, java.lang.Integer.MAX_VALUE, actor));
          childrenList.add(new Property("reason", "CodeableConcept", "The reason why the action was taken.", 0, java.lang.Integer.MAX_VALUE, reason));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<MedicationAdministrationStatus>
        case -1422950858: /*action*/ return this.action == null ? new Base[0] : new Base[] {this.action}; // CodeableConcept
        case 1792749467: /*dateTime*/ return this.dateTime == null ? new Base[0] : new Base[] {this.dateTime}; // DateTimeType
        case 92645877: /*actor*/ return this.actor == null ? new Base[0] : new Base[] {this.actor}; // Reference
        case -934964668: /*reason*/ return this.reason == null ? new Base[0] : new Base[] {this.reason}; // CodeableConcept
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -892481550: // status
          this.status = new MedicationAdministrationStatusEnumFactory().fromType(value); // Enumeration<MedicationAdministrationStatus>
          break;
        case -1422950858: // action
          this.action = castToCodeableConcept(value); // CodeableConcept
          break;
        case 1792749467: // dateTime
          this.dateTime = castToDateTime(value); // DateTimeType
          break;
        case 92645877: // actor
          this.actor = castToReference(value); // Reference
          break;
        case -934964668: // reason
          this.reason = castToCodeableConcept(value); // CodeableConcept
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("status"))
          this.status = new MedicationAdministrationStatusEnumFactory().fromType(value); // Enumeration<MedicationAdministrationStatus>
        else if (name.equals("action"))
          this.action = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("dateTime"))
          this.dateTime = castToDateTime(value); // DateTimeType
        else if (name.equals("actor"))
          this.actor = castToReference(value); // Reference
        else if (name.equals("reason"))
          this.reason = castToCodeableConcept(value); // CodeableConcept
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -892481550: throw new FHIRException("Cannot make property status as it is not a complex type"); // Enumeration<MedicationAdministrationStatus>
        case -1422950858:  return getAction(); // CodeableConcept
        case 1792749467: throw new FHIRException("Cannot make property dateTime as it is not a complex type"); // DateTimeType
        case 92645877:  return getActor(); // Reference
        case -934964668:  return getReason(); // CodeableConcept
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type MedicationAdministration.status");
        }
        else if (name.equals("action")) {
          this.action = new CodeableConcept();
          return this.action;
        }
        else if (name.equals("dateTime")) {
          throw new FHIRException("Cannot call addChild on a primitive type MedicationAdministration.dateTime");
        }
        else if (name.equals("actor")) {
          this.actor = new Reference();
          return this.actor;
        }
        else if (name.equals("reason")) {
          this.reason = new CodeableConcept();
          return this.reason;
        }
        else
          return super.addChild(name);
      }

      public MedicationAdministrationEventHistoryComponent copy() {
        MedicationAdministrationEventHistoryComponent dst = new MedicationAdministrationEventHistoryComponent();
        copyValues(dst);
        dst.status = status == null ? null : status.copy();
        dst.action = action == null ? null : action.copy();
        dst.dateTime = dateTime == null ? null : dateTime.copy();
        dst.actor = actor == null ? null : actor.copy();
        dst.reason = reason == null ? null : reason.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof MedicationAdministrationEventHistoryComponent))
          return false;
        MedicationAdministrationEventHistoryComponent o = (MedicationAdministrationEventHistoryComponent) other;
        return compareDeep(status, o.status, true) && compareDeep(action, o.action, true) && compareDeep(dateTime, o.dateTime, true)
           && compareDeep(actor, o.actor, true) && compareDeep(reason, o.reason, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof MedicationAdministrationEventHistoryComponent))
          return false;
        MedicationAdministrationEventHistoryComponent o = (MedicationAdministrationEventHistoryComponent) other;
        return compareValues(status, o.status, true) && compareValues(dateTime, o.dateTime, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(status, action, dateTime
          , actor, reason);
      }

  public String fhirType() {
    return "MedicationAdministration.eventHistory";

  }

  }

    /**
     * External identifier - FHIR will generate its own internal identifiers (probably URLs) which do not need to be explicitly managed by the resource.  The identifier here is one that would be used by another non-FHIR system - for example an automated medication pump would provide a record each time it operated; an administration while the patient was off the ward might be made with a different system and entered after the event.  Particularly important if these records have to be updated.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="External identifier", formalDefinition="External identifier - FHIR will generate its own internal identifiers (probably URLs) which do not need to be explicitly managed by the resource.  The identifier here is one that would be used by another non-FHIR system - for example an automated medication pump would provide a record each time it operated; an administration while the patient was off the ward might be made with a different system and entered after the event.  Particularly important if these records have to be updated." )
    protected List<Identifier> identifier;

    /**
     * Will generally be set to show that the administration has been completed.  For some long running administrations such as infusions it is possible for an administration to be started but not completed or it may be paused while some other process is under way.
     */
    @Child(name = "status", type = {CodeType.class}, order=1, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="in-progress | on-hold | completed | entered-in-error | stopped", formalDefinition="Will generally be set to show that the administration has been completed.  For some long running administrations such as infusions it is possible for an administration to be started but not completed or it may be paused while some other process is under way." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/medication-admin-status")
    protected Enumeration<MedicationAdministrationStatus> status;

    /**
     * Identifies the medication that was administered. This is either a link to a resource representing the details of the medication or a simple attribute carrying a code that identifies the medication from a known list of medications.
     */
    @Child(name = "medication", type = {CodeableConcept.class, Medication.class}, order=2, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="What was administered", formalDefinition="Identifies the medication that was administered. This is either a link to a resource representing the details of the medication or a simple attribute carrying a code that identifies the medication from a known list of medications." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/medication-codes")
    protected Type medication;

    /**
     * The person or animal receiving the medication.
     */
    @Child(name = "patient", type = {Patient.class}, order=3, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Who received medication", formalDefinition="The person or animal receiving the medication." )
    protected Reference patient;

    /**
     * The actual object that is the target of the reference (The person or animal receiving the medication.)
     */
    protected Patient patientTarget;

    /**
     * The visit, admission or other contact between patient and health care provider the medication administration was performed as part of.
     */
    @Child(name = "encounter", type = {Encounter.class}, order=4, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Encounter administered as part of", formalDefinition="The visit, admission or other contact between patient and health care provider the medication administration was performed as part of." )
    protected Reference encounter;

    /**
     * The actual object that is the target of the reference (The visit, admission or other contact between patient and health care provider the medication administration was performed as part of.)
     */
    protected Encounter encounterTarget;

    /**
     * A specific date/time or interval of time during which the administration took place (or did not take place, when the 'notGiven' attribute is true). For many administrations, such as swallowing a tablet the use of dateTime is more appropriate.
     */
    @Child(name = "effectiveTime", type = {DateTimeType.class, Period.class}, order=5, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Start and end time of administration", formalDefinition="A specific date/time or interval of time during which the administration took place (or did not take place, when the 'notGiven' attribute is true). For many administrations, such as swallowing a tablet the use of dateTime is more appropriate." )
    protected Type effectiveTime;

    /**
     * The individual who was responsible for giving the medication to the patient.
     */
    @Child(name = "performer", type = {Practitioner.class, Patient.class, RelatedPerson.class}, order=6, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Who administered substance", formalDefinition="The individual who was responsible for giving the medication to the patient." )
    protected Reference performer;

    /**
     * The actual object that is the target of the reference (The individual who was responsible for giving the medication to the patient.)
     */
    protected Resource performerTarget;

    /**
     * The original request, instruction or authority to perform the administration.
     */
    @Child(name = "prescription", type = {MedicationOrder.class}, order=7, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Order administration performed against", formalDefinition="The original request, instruction or authority to perform the administration." )
    protected Reference prescription;

    /**
     * The actual object that is the target of the reference (The original request, instruction or authority to perform the administration.)
     */
    protected MedicationOrder prescriptionTarget;

    /**
     * Set this to true if the record is saying that the medication was NOT administered.
     */
    @Child(name = "wasNotGiven", type = {BooleanType.class}, order=8, min=0, max=1, modifier=true, summary=true)
    @Description(shortDefinition="True if medication not administered", formalDefinition="Set this to true if the record is saying that the medication was NOT administered." )
    protected BooleanType wasNotGiven;

    /**
     * A code indicating why the administration was not performed.
     */
    @Child(name = "reasonNotGiven", type = {CodeableConcept.class}, order=9, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Reason administration not performed", formalDefinition="A code indicating why the administration was not performed." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/reason-medication-not-given-codes")
    protected List<CodeableConcept> reasonNotGiven;

    /**
     * A code indicating why the medication was given.
     */
    @Child(name = "reasonGiven", type = {CodeableConcept.class}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Reason administration performed", formalDefinition="A code indicating why the medication was given." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/reason-medication-given-codes")
    protected List<CodeableConcept> reasonGiven;

    /**
     * The device used in administering the medication to the patient.  For example, a particular infusion pump.
     */
    @Child(name = "device", type = {Device.class}, order=11, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Device used to administer", formalDefinition="The device used in administering the medication to the patient.  For example, a particular infusion pump." )
    protected List<Reference> device;
    /**
     * The actual objects that are the target of the reference (The device used in administering the medication to the patient.  For example, a particular infusion pump.)
     */
    protected List<Device> deviceTarget;


    /**
     * Extra information about the medication administration that is not conveyed by the other attributes.
     */
    @Child(name = "note", type = {Annotation.class}, order=12, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Information about the administration", formalDefinition="Extra information about the medication administration that is not conveyed by the other attributes." )
    protected List<Annotation> note;

    /**
     * Describes the medication dosage information details e.g. dose, rate, site, route, etc.
     */
    @Child(name = "dosage", type = {}, order=13, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Details of how medication was taken", formalDefinition="Describes the medication dosage information details e.g. dose, rate, site, route, etc." )
    protected MedicationAdministrationDosageComponent dosage;

    /**
     * A summary of the events of interest that have occurred, such as when the administration was verified.
     */
    @Child(name = "eventHistory", type = {}, order=14, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="A list of events of interest in the lifecycle", formalDefinition="A summary of the events of interest that have occurred, such as when the administration was verified." )
    protected List<MedicationAdministrationEventHistoryComponent> eventHistory;

    private static final long serialVersionUID = 1979568003L;

  /**
   * Constructor
   */
    public MedicationAdministration() {
      super();
    }

  /**
   * Constructor
   */
    public MedicationAdministration(Enumeration<MedicationAdministrationStatus> status, Type medication, Reference patient, Type effectiveTime) {
      super();
      this.status = status;
      this.medication = medication;
      this.patient = patient;
      this.effectiveTime = effectiveTime;
    }

    /**
     * @return {@link #identifier} (External identifier - FHIR will generate its own internal identifiers (probably URLs) which do not need to be explicitly managed by the resource.  The identifier here is one that would be used by another non-FHIR system - for example an automated medication pump would provide a record each time it operated; an administration while the patient was off the ward might be made with a different system and entered after the event.  Particularly important if these records have to be updated.)
     */
    public List<Identifier> getIdentifier() { 
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      return this.identifier;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicationAdministration setIdentifier(List<Identifier> theIdentifier) { 
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

    public MedicationAdministration addIdentifier(Identifier t) { //3
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
     * @return {@link #medication} (Identifies the medication that was administered. This is either a link to a resource representing the details of the medication or a simple attribute carrying a code that identifies the medication from a known list of medications.)
     */
    public Type getMedication() { 
      return this.medication;
    }

    /**
     * @return {@link #medication} (Identifies the medication that was administered. This is either a link to a resource representing the details of the medication or a simple attribute carrying a code that identifies the medication from a known list of medications.)
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
     * @return {@link #medication} (Identifies the medication that was administered. This is either a link to a resource representing the details of the medication or a simple attribute carrying a code that identifies the medication from a known list of medications.)
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
     * @param value {@link #medication} (Identifies the medication that was administered. This is either a link to a resource representing the details of the medication or a simple attribute carrying a code that identifies the medication from a known list of medications.)
     */
    public MedicationAdministration setMedication(Type value) { 
      this.medication = value;
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
     * @return {@link #effectiveTime} (A specific date/time or interval of time during which the administration took place (or did not take place, when the 'notGiven' attribute is true). For many administrations, such as swallowing a tablet the use of dateTime is more appropriate.)
     */
    public Type getEffectiveTime() { 
      return this.effectiveTime;
    }

    /**
     * @return {@link #effectiveTime} (A specific date/time or interval of time during which the administration took place (or did not take place, when the 'notGiven' attribute is true). For many administrations, such as swallowing a tablet the use of dateTime is more appropriate.)
     */
    public DateTimeType getEffectiveTimeDateTimeType() throws FHIRException { 
      if (!(this.effectiveTime instanceof DateTimeType))
        throw new FHIRException("Type mismatch: the type DateTimeType was expected, but "+this.effectiveTime.getClass().getName()+" was encountered");
      return (DateTimeType) this.effectiveTime;
    }

    public boolean hasEffectiveTimeDateTimeType() { 
      return this.effectiveTime instanceof DateTimeType;
    }

    /**
     * @return {@link #effectiveTime} (A specific date/time or interval of time during which the administration took place (or did not take place, when the 'notGiven' attribute is true). For many administrations, such as swallowing a tablet the use of dateTime is more appropriate.)
     */
    public Period getEffectiveTimePeriod() throws FHIRException { 
      if (!(this.effectiveTime instanceof Period))
        throw new FHIRException("Type mismatch: the type Period was expected, but "+this.effectiveTime.getClass().getName()+" was encountered");
      return (Period) this.effectiveTime;
    }

    public boolean hasEffectiveTimePeriod() { 
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
     * @return {@link #performer} (The individual who was responsible for giving the medication to the patient.)
     */
    public Reference getPerformer() { 
      if (this.performer == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationAdministration.performer");
        else if (Configuration.doAutoCreate())
          this.performer = new Reference(); // cc
      return this.performer;
    }

    public boolean hasPerformer() { 
      return this.performer != null && !this.performer.isEmpty();
    }

    /**
     * @param value {@link #performer} (The individual who was responsible for giving the medication to the patient.)
     */
    public MedicationAdministration setPerformer(Reference value) { 
      this.performer = value;
      return this;
    }

    /**
     * @return {@link #performer} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The individual who was responsible for giving the medication to the patient.)
     */
    public Resource getPerformerTarget() { 
      return this.performerTarget;
    }

    /**
     * @param value {@link #performer} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The individual who was responsible for giving the medication to the patient.)
     */
    public MedicationAdministration setPerformerTarget(Resource value) { 
      this.performerTarget = value;
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

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicationAdministration setReasonNotGiven(List<CodeableConcept> theReasonNotGiven) { 
      this.reasonNotGiven = theReasonNotGiven;
      return this;
    }

    public boolean hasReasonNotGiven() { 
      if (this.reasonNotGiven == null)
        return false;
      for (CodeableConcept item : this.reasonNotGiven)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addReasonNotGiven() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.reasonNotGiven == null)
        this.reasonNotGiven = new ArrayList<CodeableConcept>();
      this.reasonNotGiven.add(t);
      return t;
    }

    public MedicationAdministration addReasonNotGiven(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.reasonNotGiven == null)
        this.reasonNotGiven = new ArrayList<CodeableConcept>();
      this.reasonNotGiven.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #reasonNotGiven}, creating it if it does not already exist
     */
    public CodeableConcept getReasonNotGivenFirstRep() { 
      if (getReasonNotGiven().isEmpty()) {
        addReasonNotGiven();
      }
      return getReasonNotGiven().get(0);
    }

    /**
     * @return {@link #reasonGiven} (A code indicating why the medication was given.)
     */
    public List<CodeableConcept> getReasonGiven() { 
      if (this.reasonGiven == null)
        this.reasonGiven = new ArrayList<CodeableConcept>();
      return this.reasonGiven;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicationAdministration setReasonGiven(List<CodeableConcept> theReasonGiven) { 
      this.reasonGiven = theReasonGiven;
      return this;
    }

    public boolean hasReasonGiven() { 
      if (this.reasonGiven == null)
        return false;
      for (CodeableConcept item : this.reasonGiven)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addReasonGiven() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.reasonGiven == null)
        this.reasonGiven = new ArrayList<CodeableConcept>();
      this.reasonGiven.add(t);
      return t;
    }

    public MedicationAdministration addReasonGiven(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.reasonGiven == null)
        this.reasonGiven = new ArrayList<CodeableConcept>();
      this.reasonGiven.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #reasonGiven}, creating it if it does not already exist
     */
    public CodeableConcept getReasonGivenFirstRep() { 
      if (getReasonGiven().isEmpty()) {
        addReasonGiven();
      }
      return getReasonGiven().get(0);
    }

    /**
     * @return {@link #device} (The device used in administering the medication to the patient.  For example, a particular infusion pump.)
     */
    public List<Reference> getDevice() { 
      if (this.device == null)
        this.device = new ArrayList<Reference>();
      return this.device;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicationAdministration setDevice(List<Reference> theDevice) { 
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

    public MedicationAdministration addDevice(Reference t) { //3
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
    public List<Device> getDeviceTarget() { 
      if (this.deviceTarget == null)
        this.deviceTarget = new ArrayList<Device>();
      return this.deviceTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public Device addDeviceTarget() { 
      Device r = new Device();
      if (this.deviceTarget == null)
        this.deviceTarget = new ArrayList<Device>();
      this.deviceTarget.add(r);
      return r;
    }

    /**
     * @return {@link #note} (Extra information about the medication administration that is not conveyed by the other attributes.)
     */
    public List<Annotation> getNote() { 
      if (this.note == null)
        this.note = new ArrayList<Annotation>();
      return this.note;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicationAdministration setNote(List<Annotation> theNote) { 
      this.note = theNote;
      return this;
    }

    public boolean hasNote() { 
      if (this.note == null)
        return false;
      for (Annotation item : this.note)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Annotation addNote() { //3
      Annotation t = new Annotation();
      if (this.note == null)
        this.note = new ArrayList<Annotation>();
      this.note.add(t);
      return t;
    }

    public MedicationAdministration addNote(Annotation t) { //3
      if (t == null)
        return this;
      if (this.note == null)
        this.note = new ArrayList<Annotation>();
      this.note.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #note}, creating it if it does not already exist
     */
    public Annotation getNoteFirstRep() { 
      if (getNote().isEmpty()) {
        addNote();
      }
      return getNote().get(0);
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

    /**
     * @return {@link #eventHistory} (A summary of the events of interest that have occurred, such as when the administration was verified.)
     */
    public List<MedicationAdministrationEventHistoryComponent> getEventHistory() { 
      if (this.eventHistory == null)
        this.eventHistory = new ArrayList<MedicationAdministrationEventHistoryComponent>();
      return this.eventHistory;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicationAdministration setEventHistory(List<MedicationAdministrationEventHistoryComponent> theEventHistory) { 
      this.eventHistory = theEventHistory;
      return this;
    }

    public boolean hasEventHistory() { 
      if (this.eventHistory == null)
        return false;
      for (MedicationAdministrationEventHistoryComponent item : this.eventHistory)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public MedicationAdministrationEventHistoryComponent addEventHistory() { //3
      MedicationAdministrationEventHistoryComponent t = new MedicationAdministrationEventHistoryComponent();
      if (this.eventHistory == null)
        this.eventHistory = new ArrayList<MedicationAdministrationEventHistoryComponent>();
      this.eventHistory.add(t);
      return t;
    }

    public MedicationAdministration addEventHistory(MedicationAdministrationEventHistoryComponent t) { //3
      if (t == null)
        return this;
      if (this.eventHistory == null)
        this.eventHistory = new ArrayList<MedicationAdministrationEventHistoryComponent>();
      this.eventHistory.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #eventHistory}, creating it if it does not already exist
     */
    public MedicationAdministrationEventHistoryComponent getEventHistoryFirstRep() { 
      if (getEventHistory().isEmpty()) {
        addEventHistory();
      }
      return getEventHistory().get(0);
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "External identifier - FHIR will generate its own internal identifiers (probably URLs) which do not need to be explicitly managed by the resource.  The identifier here is one that would be used by another non-FHIR system - for example an automated medication pump would provide a record each time it operated; an administration while the patient was off the ward might be made with a different system and entered after the event.  Particularly important if these records have to be updated.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("status", "code", "Will generally be set to show that the administration has been completed.  For some long running administrations such as infusions it is possible for an administration to be started but not completed or it may be paused while some other process is under way.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("medication[x]", "CodeableConcept|Reference(Medication)", "Identifies the medication that was administered. This is either a link to a resource representing the details of the medication or a simple attribute carrying a code that identifies the medication from a known list of medications.", 0, java.lang.Integer.MAX_VALUE, medication));
        childrenList.add(new Property("patient", "Reference(Patient)", "The person or animal receiving the medication.", 0, java.lang.Integer.MAX_VALUE, patient));
        childrenList.add(new Property("encounter", "Reference(Encounter)", "The visit, admission or other contact between patient and health care provider the medication administration was performed as part of.", 0, java.lang.Integer.MAX_VALUE, encounter));
        childrenList.add(new Property("effectiveTime[x]", "dateTime|Period", "A specific date/time or interval of time during which the administration took place (or did not take place, when the 'notGiven' attribute is true). For many administrations, such as swallowing a tablet the use of dateTime is more appropriate.", 0, java.lang.Integer.MAX_VALUE, effectiveTime));
        childrenList.add(new Property("performer", "Reference(Practitioner|Patient|RelatedPerson)", "The individual who was responsible for giving the medication to the patient.", 0, java.lang.Integer.MAX_VALUE, performer));
        childrenList.add(new Property("prescription", "Reference(MedicationOrder)", "The original request, instruction or authority to perform the administration.", 0, java.lang.Integer.MAX_VALUE, prescription));
        childrenList.add(new Property("wasNotGiven", "boolean", "Set this to true if the record is saying that the medication was NOT administered.", 0, java.lang.Integer.MAX_VALUE, wasNotGiven));
        childrenList.add(new Property("reasonNotGiven", "CodeableConcept", "A code indicating why the administration was not performed.", 0, java.lang.Integer.MAX_VALUE, reasonNotGiven));
        childrenList.add(new Property("reasonGiven", "CodeableConcept", "A code indicating why the medication was given.", 0, java.lang.Integer.MAX_VALUE, reasonGiven));
        childrenList.add(new Property("device", "Reference(Device)", "The device used in administering the medication to the patient.  For example, a particular infusion pump.", 0, java.lang.Integer.MAX_VALUE, device));
        childrenList.add(new Property("note", "Annotation", "Extra information about the medication administration that is not conveyed by the other attributes.", 0, java.lang.Integer.MAX_VALUE, note));
        childrenList.add(new Property("dosage", "", "Describes the medication dosage information details e.g. dose, rate, site, route, etc.", 0, java.lang.Integer.MAX_VALUE, dosage));
        childrenList.add(new Property("eventHistory", "", "A summary of the events of interest that have occurred, such as when the administration was verified.", 0, java.lang.Integer.MAX_VALUE, eventHistory));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<MedicationAdministrationStatus>
        case 1998965455: /*medication*/ return this.medication == null ? new Base[0] : new Base[] {this.medication}; // Type
        case -791418107: /*patient*/ return this.patient == null ? new Base[0] : new Base[] {this.patient}; // Reference
        case 1524132147: /*encounter*/ return this.encounter == null ? new Base[0] : new Base[] {this.encounter}; // Reference
        case -929905388: /*effectiveTime*/ return this.effectiveTime == null ? new Base[0] : new Base[] {this.effectiveTime}; // Type
        case 481140686: /*performer*/ return this.performer == null ? new Base[0] : new Base[] {this.performer}; // Reference
        case 460301338: /*prescription*/ return this.prescription == null ? new Base[0] : new Base[] {this.prescription}; // Reference
        case -1050911117: /*wasNotGiven*/ return this.wasNotGiven == null ? new Base[0] : new Base[] {this.wasNotGiven}; // BooleanType
        case 2101123790: /*reasonNotGiven*/ return this.reasonNotGiven == null ? new Base[0] : this.reasonNotGiven.toArray(new Base[this.reasonNotGiven.size()]); // CodeableConcept
        case 914964377: /*reasonGiven*/ return this.reasonGiven == null ? new Base[0] : this.reasonGiven.toArray(new Base[this.reasonGiven.size()]); // CodeableConcept
        case -1335157162: /*device*/ return this.device == null ? new Base[0] : this.device.toArray(new Base[this.device.size()]); // Reference
        case 3387378: /*note*/ return this.note == null ? new Base[0] : this.note.toArray(new Base[this.note.size()]); // Annotation
        case -1326018889: /*dosage*/ return this.dosage == null ? new Base[0] : new Base[] {this.dosage}; // MedicationAdministrationDosageComponent
        case 1835190426: /*eventHistory*/ return this.eventHistory == null ? new Base[0] : this.eventHistory.toArray(new Base[this.eventHistory.size()]); // MedicationAdministrationEventHistoryComponent
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
          this.status = new MedicationAdministrationStatusEnumFactory().fromType(value); // Enumeration<MedicationAdministrationStatus>
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
        case -929905388: // effectiveTime
          this.effectiveTime = (Type) value; // Type
          break;
        case 481140686: // performer
          this.performer = castToReference(value); // Reference
          break;
        case 460301338: // prescription
          this.prescription = castToReference(value); // Reference
          break;
        case -1050911117: // wasNotGiven
          this.wasNotGiven = castToBoolean(value); // BooleanType
          break;
        case 2101123790: // reasonNotGiven
          this.getReasonNotGiven().add(castToCodeableConcept(value)); // CodeableConcept
          break;
        case 914964377: // reasonGiven
          this.getReasonGiven().add(castToCodeableConcept(value)); // CodeableConcept
          break;
        case -1335157162: // device
          this.getDevice().add(castToReference(value)); // Reference
          break;
        case 3387378: // note
          this.getNote().add(castToAnnotation(value)); // Annotation
          break;
        case -1326018889: // dosage
          this.dosage = (MedicationAdministrationDosageComponent) value; // MedicationAdministrationDosageComponent
          break;
        case 1835190426: // eventHistory
          this.getEventHistory().add((MedicationAdministrationEventHistoryComponent) value); // MedicationAdministrationEventHistoryComponent
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier"))
          this.getIdentifier().add(castToIdentifier(value));
        else if (name.equals("status"))
          this.status = new MedicationAdministrationStatusEnumFactory().fromType(value); // Enumeration<MedicationAdministrationStatus>
        else if (name.equals("medication[x]"))
          this.medication = (Type) value; // Type
        else if (name.equals("patient"))
          this.patient = castToReference(value); // Reference
        else if (name.equals("encounter"))
          this.encounter = castToReference(value); // Reference
        else if (name.equals("effectiveTime[x]"))
          this.effectiveTime = (Type) value; // Type
        else if (name.equals("performer"))
          this.performer = castToReference(value); // Reference
        else if (name.equals("prescription"))
          this.prescription = castToReference(value); // Reference
        else if (name.equals("wasNotGiven"))
          this.wasNotGiven = castToBoolean(value); // BooleanType
        else if (name.equals("reasonNotGiven"))
          this.getReasonNotGiven().add(castToCodeableConcept(value));
        else if (name.equals("reasonGiven"))
          this.getReasonGiven().add(castToCodeableConcept(value));
        else if (name.equals("device"))
          this.getDevice().add(castToReference(value));
        else if (name.equals("note"))
          this.getNote().add(castToAnnotation(value));
        else if (name.equals("dosage"))
          this.dosage = (MedicationAdministrationDosageComponent) value; // MedicationAdministrationDosageComponent
        else if (name.equals("eventHistory"))
          this.getEventHistory().add((MedicationAdministrationEventHistoryComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return addIdentifier(); // Identifier
        case -892481550: throw new FHIRException("Cannot make property status as it is not a complex type"); // Enumeration<MedicationAdministrationStatus>
        case 1458402129:  return getMedication(); // Type
        case -791418107:  return getPatient(); // Reference
        case 1524132147:  return getEncounter(); // Reference
        case -272263444:  return getEffectiveTime(); // Type
        case 481140686:  return getPerformer(); // Reference
        case 460301338:  return getPrescription(); // Reference
        case -1050911117: throw new FHIRException("Cannot make property wasNotGiven as it is not a complex type"); // BooleanType
        case 2101123790:  return addReasonNotGiven(); // CodeableConcept
        case 914964377:  return addReasonGiven(); // CodeableConcept
        case -1335157162:  return addDevice(); // Reference
        case 3387378:  return addNote(); // Annotation
        case -1326018889:  return getDosage(); // MedicationAdministrationDosageComponent
        case 1835190426:  return addEventHistory(); // MedicationAdministrationEventHistoryComponent
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type MedicationAdministration.status");
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
        else if (name.equals("effectiveTimeDateTime")) {
          this.effectiveTime = new DateTimeType();
          return this.effectiveTime;
        }
        else if (name.equals("effectiveTimePeriod")) {
          this.effectiveTime = new Period();
          return this.effectiveTime;
        }
        else if (name.equals("performer")) {
          this.performer = new Reference();
          return this.performer;
        }
        else if (name.equals("prescription")) {
          this.prescription = new Reference();
          return this.prescription;
        }
        else if (name.equals("wasNotGiven")) {
          throw new FHIRException("Cannot call addChild on a primitive type MedicationAdministration.wasNotGiven");
        }
        else if (name.equals("reasonNotGiven")) {
          return addReasonNotGiven();
        }
        else if (name.equals("reasonGiven")) {
          return addReasonGiven();
        }
        else if (name.equals("device")) {
          return addDevice();
        }
        else if (name.equals("note")) {
          return addNote();
        }
        else if (name.equals("dosage")) {
          this.dosage = new MedicationAdministrationDosageComponent();
          return this.dosage;
        }
        else if (name.equals("eventHistory")) {
          return addEventHistory();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "MedicationAdministration";

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
        dst.medication = medication == null ? null : medication.copy();
        dst.patient = patient == null ? null : patient.copy();
        dst.encounter = encounter == null ? null : encounter.copy();
        dst.effectiveTime = effectiveTime == null ? null : effectiveTime.copy();
        dst.performer = performer == null ? null : performer.copy();
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
        if (device != null) {
          dst.device = new ArrayList<Reference>();
          for (Reference i : device)
            dst.device.add(i.copy());
        };
        if (note != null) {
          dst.note = new ArrayList<Annotation>();
          for (Annotation i : note)
            dst.note.add(i.copy());
        };
        dst.dosage = dosage == null ? null : dosage.copy();
        if (eventHistory != null) {
          dst.eventHistory = new ArrayList<MedicationAdministrationEventHistoryComponent>();
          for (MedicationAdministrationEventHistoryComponent i : eventHistory)
            dst.eventHistory.add(i.copy());
        };
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
        return compareDeep(identifier, o.identifier, true) && compareDeep(status, o.status, true) && compareDeep(medication, o.medication, true)
           && compareDeep(patient, o.patient, true) && compareDeep(encounter, o.encounter, true) && compareDeep(effectiveTime, o.effectiveTime, true)
           && compareDeep(performer, o.performer, true) && compareDeep(prescription, o.prescription, true)
           && compareDeep(wasNotGiven, o.wasNotGiven, true) && compareDeep(reasonNotGiven, o.reasonNotGiven, true)
           && compareDeep(reasonGiven, o.reasonGiven, true) && compareDeep(device, o.device, true) && compareDeep(note, o.note, true)
           && compareDeep(dosage, o.dosage, true) && compareDeep(eventHistory, o.eventHistory, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof MedicationAdministration))
          return false;
        MedicationAdministration o = (MedicationAdministration) other;
        return compareValues(status, o.status, true) && compareValues(wasNotGiven, o.wasNotGiven, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, status, medication
          , patient, encounter, effectiveTime, performer, prescription, wasNotGiven, reasonNotGiven
          , reasonGiven, device, note, dosage, eventHistory);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.MedicationAdministration;
   }

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>Return administrations with this external identifier</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MedicationAdministration.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="MedicationAdministration.identifier", description="Return administrations with this external identifier", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>Return administrations with this external identifier</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MedicationAdministration.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>code</b>
   * <p>
   * Description: <b>Return administrations of this medication code</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MedicationAdministration.medicationCodeableConcept</b><br>
   * </p>
   */
  @SearchParamDefinition(name="code", path="MedicationAdministration.medication.as(CodeableConcept)", description="Return administrations of this medication code", type="token" )
  public static final String SP_CODE = "code";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>code</b>
   * <p>
   * Description: <b>Return administrations of this medication code</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MedicationAdministration.medicationCodeableConcept</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CODE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CODE);

 /**
   * Search parameter: <b>performer</b>
   * <p>
   * Description: <b>Who administered substance</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MedicationAdministration.performer</b><br>
   * </p>
   */
  @SearchParamDefinition(name="performer", path="MedicationAdministration.performer", description="Who administered substance", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient"), @ca.uhn.fhir.model.api.annotation.Compartment(name="Practitioner"), @ca.uhn.fhir.model.api.annotation.Compartment(name="RelatedPerson") }, target={Patient.class, Practitioner.class, RelatedPerson.class } )
  public static final String SP_PERFORMER = "performer";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>performer</b>
   * <p>
   * Description: <b>Who administered substance</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MedicationAdministration.performer</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PERFORMER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PERFORMER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>MedicationAdministration:performer</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PERFORMER = new ca.uhn.fhir.model.api.Include("MedicationAdministration:performer").toLocked();

 /**
   * Search parameter: <b>prescription</b>
   * <p>
   * Description: <b>The identity of a prescription to list administrations from</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MedicationAdministration.prescription</b><br>
   * </p>
   */
  @SearchParamDefinition(name="prescription", path="MedicationAdministration.prescription", description="The identity of a prescription to list administrations from", type="reference", target={MedicationOrder.class } )
  public static final String SP_PRESCRIPTION = "prescription";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>prescription</b>
   * <p>
   * Description: <b>The identity of a prescription to list administrations from</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MedicationAdministration.prescription</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PRESCRIPTION = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PRESCRIPTION);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>MedicationAdministration:prescription</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PRESCRIPTION = new ca.uhn.fhir.model.api.Include("MedicationAdministration:prescription").toLocked();

 /**
   * Search parameter: <b>effectivetime</b>
   * <p>
   * Description: <b>Date administration happened (or did not happen)</b><br>
   * Type: <b>date</b><br>
   * Path: <b>MedicationAdministration.effectiveTime[x]</b><br>
   * </p>
   */
  @SearchParamDefinition(name="effectivetime", path="MedicationAdministration.effectiveTime", description="Date administration happened (or did not happen)", type="date" )
  public static final String SP_EFFECTIVETIME = "effectivetime";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>effectivetime</b>
   * <p>
   * Description: <b>Date administration happened (or did not happen)</b><br>
   * Type: <b>date</b><br>
   * Path: <b>MedicationAdministration.effectiveTime[x]</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam EFFECTIVETIME = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_EFFECTIVETIME);

 /**
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>The identity of a patient to list administrations  for</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MedicationAdministration.patient</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="MedicationAdministration.patient", description="The identity of a patient to list administrations  for", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient") }, target={Patient.class } )
  public static final String SP_PATIENT = "patient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patient</b>
   * <p>
   * Description: <b>The identity of a patient to list administrations  for</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MedicationAdministration.patient</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PATIENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PATIENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>MedicationAdministration:patient</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PATIENT = new ca.uhn.fhir.model.api.Include("MedicationAdministration:patient").toLocked();

 /**
   * Search parameter: <b>wasnotgiven</b>
   * <p>
   * Description: <b>Administrations that were not made</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MedicationAdministration.wasNotGiven</b><br>
   * </p>
   */
  @SearchParamDefinition(name="wasnotgiven", path="MedicationAdministration.wasNotGiven", description="Administrations that were not made", type="token" )
  public static final String SP_WASNOTGIVEN = "wasnotgiven";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>wasnotgiven</b>
   * <p>
   * Description: <b>Administrations that were not made</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MedicationAdministration.wasNotGiven</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam WASNOTGIVEN = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_WASNOTGIVEN);

 /**
   * Search parameter: <b>medication</b>
   * <p>
   * Description: <b>Return administrations of this medication resource</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MedicationAdministration.medicationReference</b><br>
   * </p>
   */
  @SearchParamDefinition(name="medication", path="MedicationAdministration.medication.as(Reference)", description="Return administrations of this medication resource", type="reference", target={Medication.class } )
  public static final String SP_MEDICATION = "medication";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>medication</b>
   * <p>
   * Description: <b>Return administrations of this medication resource</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MedicationAdministration.medicationReference</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam MEDICATION = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_MEDICATION);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>MedicationAdministration:medication</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_MEDICATION = new ca.uhn.fhir.model.api.Include("MedicationAdministration:medication").toLocked();

 /**
   * Search parameter: <b>encounter</b>
   * <p>
   * Description: <b>Return administrations that share this encounter</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MedicationAdministration.encounter</b><br>
   * </p>
   */
  @SearchParamDefinition(name="encounter", path="MedicationAdministration.encounter", description="Return administrations that share this encounter", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Encounter") }, target={Encounter.class } )
  public static final String SP_ENCOUNTER = "encounter";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>encounter</b>
   * <p>
   * Description: <b>Return administrations that share this encounter</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MedicationAdministration.encounter</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam ENCOUNTER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_ENCOUNTER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>MedicationAdministration:encounter</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_ENCOUNTER = new ca.uhn.fhir.model.api.Include("MedicationAdministration:encounter").toLocked();

 /**
   * Search parameter: <b>device</b>
   * <p>
   * Description: <b>Return administrations with this administration device identity</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MedicationAdministration.device</b><br>
   * </p>
   */
  @SearchParamDefinition(name="device", path="MedicationAdministration.device", description="Return administrations with this administration device identity", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Device") }, target={Device.class } )
  public static final String SP_DEVICE = "device";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>device</b>
   * <p>
   * Description: <b>Return administrations with this administration device identity</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MedicationAdministration.device</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam DEVICE = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_DEVICE);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>MedicationAdministration:device</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_DEVICE = new ca.uhn.fhir.model.api.Include("MedicationAdministration:device").toLocked();

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>MedicationAdministration event status (for example one of active/paused/completed/nullified)</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MedicationAdministration.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="MedicationAdministration.status", description="MedicationAdministration event status (for example one of active/paused/completed/nullified)", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>MedicationAdministration event status (for example one of active/paused/completed/nullified)</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MedicationAdministration.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);


}

