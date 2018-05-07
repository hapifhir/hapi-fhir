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
         * The authoring system does not know which of the status values currently applies for this request. Note: This concept is not to be used for "other" - one of the listed statuses is presumed to apply, it's just not known which one.
         */
        UNKNOWN, 
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
        if ("unknown".equals(codeString))
          return UNKNOWN;
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
            case UNKNOWN: return "unknown";
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
            case UNKNOWN: return "http://hl7.org/fhir/medication-admin-status";
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
            case UNKNOWN: return "The authoring system does not know which of the status values currently applies for this request. Note: This concept is not to be used for \"other\" - one of the listed statuses is presumed to apply, it's just not known which one.";
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
            case UNKNOWN: return "Unknown";
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
        if ("unknown".equals(codeString))
          return MedicationAdministrationStatus.UNKNOWN;
        throw new IllegalArgumentException("Unknown MedicationAdministrationStatus code '"+codeString+"'");
        }
        public Enumeration<MedicationAdministrationStatus> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<MedicationAdministrationStatus>(this);
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
        if ("unknown".equals(codeString))
          return new Enumeration<MedicationAdministrationStatus>(this, MedicationAdministrationStatus.UNKNOWN);
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
      if (code == MedicationAdministrationStatus.UNKNOWN)
        return "unknown";
      return "?";
      }
    public String toSystem(MedicationAdministrationStatus code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class MedicationAdministrationPerformerComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The device, practitioner, etc. who performed the action.
         */
        @Child(name = "actor", type = {Practitioner.class, Patient.class, RelatedPerson.class, Device.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Individual who was performing", formalDefinition="The device, practitioner, etc. who performed the action." )
        protected Reference actor;

        /**
         * The actual object that is the target of the reference (The device, practitioner, etc. who performed the action.)
         */
        protected Resource actorTarget;

        /**
         * The organization the device or practitioner was acting on behalf of.
         */
        @Child(name = "onBehalfOf", type = {Organization.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Organization organization was acting for", formalDefinition="The organization the device or practitioner was acting on behalf of." )
        protected Reference onBehalfOf;

        /**
         * The actual object that is the target of the reference (The organization the device or practitioner was acting on behalf of.)
         */
        protected Organization onBehalfOfTarget;

        private static final long serialVersionUID = -488386403L;

    /**
     * Constructor
     */
      public MedicationAdministrationPerformerComponent() {
        super();
      }

    /**
     * Constructor
     */
      public MedicationAdministrationPerformerComponent(Reference actor) {
        super();
        this.actor = actor;
      }

        /**
         * @return {@link #actor} (The device, practitioner, etc. who performed the action.)
         */
        public Reference getActor() { 
          if (this.actor == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationAdministrationPerformerComponent.actor");
            else if (Configuration.doAutoCreate())
              this.actor = new Reference(); // cc
          return this.actor;
        }

        public boolean hasActor() { 
          return this.actor != null && !this.actor.isEmpty();
        }

        /**
         * @param value {@link #actor} (The device, practitioner, etc. who performed the action.)
         */
        public MedicationAdministrationPerformerComponent setActor(Reference value) { 
          this.actor = value;
          return this;
        }

        /**
         * @return {@link #actor} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The device, practitioner, etc. who performed the action.)
         */
        public Resource getActorTarget() { 
          return this.actorTarget;
        }

        /**
         * @param value {@link #actor} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The device, practitioner, etc. who performed the action.)
         */
        public MedicationAdministrationPerformerComponent setActorTarget(Resource value) { 
          this.actorTarget = value;
          return this;
        }

        /**
         * @return {@link #onBehalfOf} (The organization the device or practitioner was acting on behalf of.)
         */
        public Reference getOnBehalfOf() { 
          if (this.onBehalfOf == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationAdministrationPerformerComponent.onBehalfOf");
            else if (Configuration.doAutoCreate())
              this.onBehalfOf = new Reference(); // cc
          return this.onBehalfOf;
        }

        public boolean hasOnBehalfOf() { 
          return this.onBehalfOf != null && !this.onBehalfOf.isEmpty();
        }

        /**
         * @param value {@link #onBehalfOf} (The organization the device or practitioner was acting on behalf of.)
         */
        public MedicationAdministrationPerformerComponent setOnBehalfOf(Reference value) { 
          this.onBehalfOf = value;
          return this;
        }

        /**
         * @return {@link #onBehalfOf} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The organization the device or practitioner was acting on behalf of.)
         */
        public Organization getOnBehalfOfTarget() { 
          if (this.onBehalfOfTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationAdministrationPerformerComponent.onBehalfOf");
            else if (Configuration.doAutoCreate())
              this.onBehalfOfTarget = new Organization(); // aa
          return this.onBehalfOfTarget;
        }

        /**
         * @param value {@link #onBehalfOf} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The organization the device or practitioner was acting on behalf of.)
         */
        public MedicationAdministrationPerformerComponent setOnBehalfOfTarget(Organization value) { 
          this.onBehalfOfTarget = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("actor", "Reference(Practitioner|Patient|RelatedPerson|Device)", "The device, practitioner, etc. who performed the action.", 0, java.lang.Integer.MAX_VALUE, actor));
          childrenList.add(new Property("onBehalfOf", "Reference(Organization)", "The organization the device or practitioner was acting on behalf of.", 0, java.lang.Integer.MAX_VALUE, onBehalfOf));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 92645877: /*actor*/ return this.actor == null ? new Base[0] : new Base[] {this.actor}; // Reference
        case -14402964: /*onBehalfOf*/ return this.onBehalfOf == null ? new Base[0] : new Base[] {this.onBehalfOf}; // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 92645877: // actor
          this.actor = castToReference(value); // Reference
          return value;
        case -14402964: // onBehalfOf
          this.onBehalfOf = castToReference(value); // Reference
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("actor")) {
          this.actor = castToReference(value); // Reference
        } else if (name.equals("onBehalfOf")) {
          this.onBehalfOf = castToReference(value); // Reference
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 92645877:  return getActor(); 
        case -14402964:  return getOnBehalfOf(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 92645877: /*actor*/ return new String[] {"Reference"};
        case -14402964: /*onBehalfOf*/ return new String[] {"Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("actor")) {
          this.actor = new Reference();
          return this.actor;
        }
        else if (name.equals("onBehalfOf")) {
          this.onBehalfOf = new Reference();
          return this.onBehalfOf;
        }
        else
          return super.addChild(name);
      }

      public MedicationAdministrationPerformerComponent copy() {
        MedicationAdministrationPerformerComponent dst = new MedicationAdministrationPerformerComponent();
        copyValues(dst);
        dst.actor = actor == null ? null : actor.copy();
        dst.onBehalfOf = onBehalfOf == null ? null : onBehalfOf.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof MedicationAdministrationPerformerComponent))
          return false;
        MedicationAdministrationPerformerComponent o = (MedicationAdministrationPerformerComponent) other;
        return compareDeep(actor, o.actor, true) && compareDeep(onBehalfOf, o.onBehalfOf, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof MedicationAdministrationPerformerComponent))
          return false;
        MedicationAdministrationPerformerComponent o = (MedicationAdministrationPerformerComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(actor, onBehalfOf);
      }

  public String fhirType() {
    return "MedicationAdministration.performer";

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
        @Child(name = "site", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Body site administered to", formalDefinition="A coded specification of the anatomic site where the medication first entered the body.  For example, \"left arm\"." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/approach-site-codes")
        protected CodeableConcept site;

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

        private static final long serialVersionUID = 1316915516L;

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
        public CodeableConcept getSite() { 
          if (this.site == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationAdministrationDosageComponent.site");
            else if (Configuration.doAutoCreate())
              this.site = new CodeableConcept(); // cc
          return this.site;
        }

        public boolean hasSite() { 
          return this.site != null && !this.site.isEmpty();
        }

        /**
         * @param value {@link #site} (A coded specification of the anatomic site where the medication first entered the body.  For example, "left arm".)
         */
        public MedicationAdministrationDosageComponent setSite(CodeableConcept value) { 
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
          childrenList.add(new Property("site", "CodeableConcept", "A coded specification of the anatomic site where the medication first entered the body.  For example, \"left arm\".", 0, java.lang.Integer.MAX_VALUE, site));
          childrenList.add(new Property("route", "CodeableConcept", "A code specifying the route or physiological path of administration of a therapeutic agent into or onto the patient.  For example, topical, intravenous, etc.", 0, java.lang.Integer.MAX_VALUE, route));
          childrenList.add(new Property("method", "CodeableConcept", "A coded value indicating the method by which the medication is intended to be or was introduced into or on the body.  This attribute will most often NOT be populated.  It is most commonly used for injections.  For example, Slow Push, Deep IV.", 0, java.lang.Integer.MAX_VALUE, method));
          childrenList.add(new Property("dose", "SimpleQuantity", "The amount of the medication given at one administration event.   Use this value when the administration is essentially an instantaneous event such as a swallowing a tablet or giving an injection.", 0, java.lang.Integer.MAX_VALUE, dose));
          childrenList.add(new Property("rate[x]", "Ratio|SimpleQuantity", "Identifies the speed with which the medication was or will be introduced into the patient.  Typically the rate for an infusion e.g. 100 ml per 1 hour or 100 ml/hr.  May also be expressed as a rate per unit of time e.g. 500 ml per 2 hours.  Other examples:  200 mcg/min or 200 mcg/1 minute; 1 liter/8 hours.", 0, java.lang.Integer.MAX_VALUE, rate));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3556653: /*text*/ return this.text == null ? new Base[0] : new Base[] {this.text}; // StringType
        case 3530567: /*site*/ return this.site == null ? new Base[0] : new Base[] {this.site}; // CodeableConcept
        case 108704329: /*route*/ return this.route == null ? new Base[0] : new Base[] {this.route}; // CodeableConcept
        case -1077554975: /*method*/ return this.method == null ? new Base[0] : new Base[] {this.method}; // CodeableConcept
        case 3089437: /*dose*/ return this.dose == null ? new Base[0] : new Base[] {this.dose}; // SimpleQuantity
        case 3493088: /*rate*/ return this.rate == null ? new Base[0] : new Base[] {this.rate}; // Type
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3556653: // text
          this.text = castToString(value); // StringType
          return value;
        case 3530567: // site
          this.site = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 108704329: // route
          this.route = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1077554975: // method
          this.method = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 3089437: // dose
          this.dose = castToSimpleQuantity(value); // SimpleQuantity
          return value;
        case 3493088: // rate
          this.rate = castToType(value); // Type
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("text")) {
          this.text = castToString(value); // StringType
        } else if (name.equals("site")) {
          this.site = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("route")) {
          this.route = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("method")) {
          this.method = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("dose")) {
          this.dose = castToSimpleQuantity(value); // SimpleQuantity
        } else if (name.equals("rate[x]")) {
          this.rate = castToType(value); // Type
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3556653:  return getTextElement();
        case 3530567:  return getSite(); 
        case 108704329:  return getRoute(); 
        case -1077554975:  return getMethod(); 
        case 3089437:  return getDose(); 
        case 983460768:  return getRate(); 
        case 3493088:  return getRate(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3556653: /*text*/ return new String[] {"string"};
        case 3530567: /*site*/ return new String[] {"CodeableConcept"};
        case 108704329: /*route*/ return new String[] {"CodeableConcept"};
        case -1077554975: /*method*/ return new String[] {"CodeableConcept"};
        case 3089437: /*dose*/ return new String[] {"SimpleQuantity"};
        case 3493088: /*rate*/ return new String[] {"Ratio", "SimpleQuantity"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("text")) {
          throw new FHIRException("Cannot call addChild on a primitive type MedicationAdministration.text");
        }
        else if (name.equals("site")) {
          this.site = new CodeableConcept();
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

    /**
     * External identifier - FHIR will generate its own internal identifiers (probably URLs) which do not need to be explicitly managed by the resource.  The identifier here is one that would be used by another non-FHIR system - for example an automated medication pump would provide a record each time it operated; an administration while the patient was off the ward might be made with a different system and entered after the event.  Particularly important if these records have to be updated.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="External identifier", formalDefinition="External identifier - FHIR will generate its own internal identifiers (probably URLs) which do not need to be explicitly managed by the resource.  The identifier here is one that would be used by another non-FHIR system - for example an automated medication pump would provide a record each time it operated; an administration while the patient was off the ward might be made with a different system and entered after the event.  Particularly important if these records have to be updated." )
    protected List<Identifier> identifier;

    /**
     * A protocol, guideline, orderset or other definition that was adhered to in whole or in part by this event.
     */
    @Child(name = "definition", type = {PlanDefinition.class, ActivityDefinition.class}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Instantiates protocol or definition", formalDefinition="A protocol, guideline, orderset or other definition that was adhered to in whole or in part by this event." )
    protected List<Reference> definition;
    /**
     * The actual objects that are the target of the reference (A protocol, guideline, orderset or other definition that was adhered to in whole or in part by this event.)
     */
    protected List<Resource> definitionTarget;


    /**
     * A larger event of which this particular event is a component or step.
     */
    @Child(name = "partOf", type = {MedicationAdministration.class, Procedure.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Part of referenced event", formalDefinition="A larger event of which this particular event is a component or step." )
    protected List<Reference> partOf;
    /**
     * The actual objects that are the target of the reference (A larger event of which this particular event is a component or step.)
     */
    protected List<Resource> partOfTarget;


    /**
     * Will generally be set to show that the administration has been completed.  For some long running administrations such as infusions it is possible for an administration to be started but not completed or it may be paused while some other process is under way.
     */
    @Child(name = "status", type = {CodeType.class}, order=3, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="in-progress | on-hold | completed | entered-in-error | stopped | unknown", formalDefinition="Will generally be set to show that the administration has been completed.  For some long running administrations such as infusions it is possible for an administration to be started but not completed or it may be paused while some other process is under way." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/medication-admin-status")
    protected Enumeration<MedicationAdministrationStatus> status;

    /**
     * Indicates the type of medication administration and where the medication is expected to be consumed or administered.
     */
    @Child(name = "category", type = {CodeableConcept.class}, order=4, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Type of medication usage", formalDefinition="Indicates the type of medication administration and where the medication is expected to be consumed or administered." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/medication-admin-category")
    protected CodeableConcept category;

    /**
     * Identifies the medication that was administered. This is either a link to a resource representing the details of the medication or a simple attribute carrying a code that identifies the medication from a known list of medications.
     */
    @Child(name = "medication", type = {CodeableConcept.class, Medication.class}, order=5, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="What was administered", formalDefinition="Identifies the medication that was administered. This is either a link to a resource representing the details of the medication or a simple attribute carrying a code that identifies the medication from a known list of medications." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/medication-codes")
    protected Type medication;

    /**
     * The person or animal or group receiving the medication.
     */
    @Child(name = "subject", type = {Patient.class, Group.class}, order=6, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Who received medication", formalDefinition="The person or animal or group receiving the medication." )
    protected Reference subject;

    /**
     * The actual object that is the target of the reference (The person or animal or group receiving the medication.)
     */
    protected Resource subjectTarget;

    /**
     * The visit, admission or other contact between patient and health care provider the medication administration was performed as part of.
     */
    @Child(name = "context", type = {Encounter.class, EpisodeOfCare.class}, order=7, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Encounter or Episode of Care administered as part of", formalDefinition="The visit, admission or other contact between patient and health care provider the medication administration was performed as part of." )
    protected Reference context;

    /**
     * The actual object that is the target of the reference (The visit, admission or other contact between patient and health care provider the medication administration was performed as part of.)
     */
    protected Resource contextTarget;

    /**
     * Additional information (for example, patient height and weight) that supports the administration of the medication.
     */
    @Child(name = "supportingInformation", type = {Reference.class}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Additional information to support administration", formalDefinition="Additional information (for example, patient height and weight) that supports the administration of the medication." )
    protected List<Reference> supportingInformation;
    /**
     * The actual objects that are the target of the reference (Additional information (for example, patient height and weight) that supports the administration of the medication.)
     */
    protected List<Resource> supportingInformationTarget;


    /**
     * A specific date/time or interval of time during which the administration took place (or did not take place, when the 'notGiven' attribute is true). For many administrations, such as swallowing a tablet the use of dateTime is more appropriate.
     */
    @Child(name = "effective", type = {DateTimeType.class, Period.class}, order=9, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Start and end time of administration", formalDefinition="A specific date/time or interval of time during which the administration took place (or did not take place, when the 'notGiven' attribute is true). For many administrations, such as swallowing a tablet the use of dateTime is more appropriate." )
    protected Type effective;

    /**
     * The individual who was responsible for giving the medication to the patient.
     */
    @Child(name = "performer", type = {}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Who administered substance", formalDefinition="The individual who was responsible for giving the medication to the patient." )
    protected List<MedicationAdministrationPerformerComponent> performer;

    /**
     * Set this to true if the record is saying that the medication was NOT administered.
     */
    @Child(name = "notGiven", type = {BooleanType.class}, order=11, min=0, max=1, modifier=true, summary=true)
    @Description(shortDefinition="True if medication not administered", formalDefinition="Set this to true if the record is saying that the medication was NOT administered." )
    protected BooleanType notGiven;

    /**
     * A code indicating why the administration was not performed.
     */
    @Child(name = "reasonNotGiven", type = {CodeableConcept.class}, order=12, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Reason administration not performed", formalDefinition="A code indicating why the administration was not performed." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/reason-medication-not-given-codes")
    protected List<CodeableConcept> reasonNotGiven;

    /**
     * A code indicating why the medication was given.
     */
    @Child(name = "reasonCode", type = {CodeableConcept.class}, order=13, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Reason administration performed", formalDefinition="A code indicating why the medication was given." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/reason-medication-given-codes")
    protected List<CodeableConcept> reasonCode;

    /**
     * Condition or observation that supports why the medication was administered.
     */
    @Child(name = "reasonReference", type = {Condition.class, Observation.class}, order=14, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Condition or Observation that supports why the medication was administered", formalDefinition="Condition or observation that supports why the medication was administered." )
    protected List<Reference> reasonReference;
    /**
     * The actual objects that are the target of the reference (Condition or observation that supports why the medication was administered.)
     */
    protected List<Resource> reasonReferenceTarget;


    /**
     * The original request, instruction or authority to perform the administration.
     */
    @Child(name = "prescription", type = {MedicationRequest.class}, order=15, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Request administration performed against", formalDefinition="The original request, instruction or authority to perform the administration." )
    protected Reference prescription;

    /**
     * The actual object that is the target of the reference (The original request, instruction or authority to perform the administration.)
     */
    protected MedicationRequest prescriptionTarget;

    /**
     * The device used in administering the medication to the patient.  For example, a particular infusion pump.
     */
    @Child(name = "device", type = {Device.class}, order=16, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Device used to administer", formalDefinition="The device used in administering the medication to the patient.  For example, a particular infusion pump." )
    protected List<Reference> device;
    /**
     * The actual objects that are the target of the reference (The device used in administering the medication to the patient.  For example, a particular infusion pump.)
     */
    protected List<Device> deviceTarget;


    /**
     * Extra information about the medication administration that is not conveyed by the other attributes.
     */
    @Child(name = "note", type = {Annotation.class}, order=17, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Information about the administration", formalDefinition="Extra information about the medication administration that is not conveyed by the other attributes." )
    protected List<Annotation> note;

    /**
     * Describes the medication dosage information details e.g. dose, rate, site, route, etc.
     */
    @Child(name = "dosage", type = {}, order=18, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Details of how medication was taken", formalDefinition="Describes the medication dosage information details e.g. dose, rate, site, route, etc." )
    protected MedicationAdministrationDosageComponent dosage;

    /**
     * A summary of the events of interest that have occurred, such as when the administration was verified.
     */
    @Child(name = "eventHistory", type = {Provenance.class}, order=19, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="A list of events of interest in the lifecycle", formalDefinition="A summary of the events of interest that have occurred, such as when the administration was verified." )
    protected List<Reference> eventHistory;
    /**
     * The actual objects that are the target of the reference (A summary of the events of interest that have occurred, such as when the administration was verified.)
     */
    protected List<Provenance> eventHistoryTarget;


    private static final long serialVersionUID = 673777544L;

  /**
   * Constructor
   */
    public MedicationAdministration() {
      super();
    }

  /**
   * Constructor
   */
    public MedicationAdministration(Enumeration<MedicationAdministrationStatus> status, Type medication, Reference subject, Type effective) {
      super();
      this.status = status;
      this.medication = medication;
      this.subject = subject;
      this.effective = effective;
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
     * @return {@link #definition} (A protocol, guideline, orderset or other definition that was adhered to in whole or in part by this event.)
     */
    public List<Reference> getDefinition() { 
      if (this.definition == null)
        this.definition = new ArrayList<Reference>();
      return this.definition;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicationAdministration setDefinition(List<Reference> theDefinition) { 
      this.definition = theDefinition;
      return this;
    }

    public boolean hasDefinition() { 
      if (this.definition == null)
        return false;
      for (Reference item : this.definition)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addDefinition() { //3
      Reference t = new Reference();
      if (this.definition == null)
        this.definition = new ArrayList<Reference>();
      this.definition.add(t);
      return t;
    }

    public MedicationAdministration addDefinition(Reference t) { //3
      if (t == null)
        return this;
      if (this.definition == null)
        this.definition = new ArrayList<Reference>();
      this.definition.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #definition}, creating it if it does not already exist
     */
    public Reference getDefinitionFirstRep() { 
      if (getDefinition().isEmpty()) {
        addDefinition();
      }
      return getDefinition().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Resource> getDefinitionTarget() { 
      if (this.definitionTarget == null)
        this.definitionTarget = new ArrayList<Resource>();
      return this.definitionTarget;
    }

    /**
     * @return {@link #partOf} (A larger event of which this particular event is a component or step.)
     */
    public List<Reference> getPartOf() { 
      if (this.partOf == null)
        this.partOf = new ArrayList<Reference>();
      return this.partOf;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicationAdministration setPartOf(List<Reference> thePartOf) { 
      this.partOf = thePartOf;
      return this;
    }

    public boolean hasPartOf() { 
      if (this.partOf == null)
        return false;
      for (Reference item : this.partOf)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addPartOf() { //3
      Reference t = new Reference();
      if (this.partOf == null)
        this.partOf = new ArrayList<Reference>();
      this.partOf.add(t);
      return t;
    }

    public MedicationAdministration addPartOf(Reference t) { //3
      if (t == null)
        return this;
      if (this.partOf == null)
        this.partOf = new ArrayList<Reference>();
      this.partOf.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #partOf}, creating it if it does not already exist
     */
    public Reference getPartOfFirstRep() { 
      if (getPartOf().isEmpty()) {
        addPartOf();
      }
      return getPartOf().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Resource> getPartOfTarget() { 
      if (this.partOfTarget == null)
        this.partOfTarget = new ArrayList<Resource>();
      return this.partOfTarget;
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
     * @return {@link #category} (Indicates the type of medication administration and where the medication is expected to be consumed or administered.)
     */
    public CodeableConcept getCategory() { 
      if (this.category == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationAdministration.category");
        else if (Configuration.doAutoCreate())
          this.category = new CodeableConcept(); // cc
      return this.category;
    }

    public boolean hasCategory() { 
      return this.category != null && !this.category.isEmpty();
    }

    /**
     * @param value {@link #category} (Indicates the type of medication administration and where the medication is expected to be consumed or administered.)
     */
    public MedicationAdministration setCategory(CodeableConcept value) { 
      this.category = value;
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
     * @return {@link #subject} (The person or animal or group receiving the medication.)
     */
    public Reference getSubject() { 
      if (this.subject == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationAdministration.subject");
        else if (Configuration.doAutoCreate())
          this.subject = new Reference(); // cc
      return this.subject;
    }

    public boolean hasSubject() { 
      return this.subject != null && !this.subject.isEmpty();
    }

    /**
     * @param value {@link #subject} (The person or animal or group receiving the medication.)
     */
    public MedicationAdministration setSubject(Reference value) { 
      this.subject = value;
      return this;
    }

    /**
     * @return {@link #subject} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The person or animal or group receiving the medication.)
     */
    public Resource getSubjectTarget() { 
      return this.subjectTarget;
    }

    /**
     * @param value {@link #subject} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The person or animal or group receiving the medication.)
     */
    public MedicationAdministration setSubjectTarget(Resource value) { 
      this.subjectTarget = value;
      return this;
    }

    /**
     * @return {@link #context} (The visit, admission or other contact between patient and health care provider the medication administration was performed as part of.)
     */
    public Reference getContext() { 
      if (this.context == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationAdministration.context");
        else if (Configuration.doAutoCreate())
          this.context = new Reference(); // cc
      return this.context;
    }

    public boolean hasContext() { 
      return this.context != null && !this.context.isEmpty();
    }

    /**
     * @param value {@link #context} (The visit, admission or other contact between patient and health care provider the medication administration was performed as part of.)
     */
    public MedicationAdministration setContext(Reference value) { 
      this.context = value;
      return this;
    }

    /**
     * @return {@link #context} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The visit, admission or other contact between patient and health care provider the medication administration was performed as part of.)
     */
    public Resource getContextTarget() { 
      return this.contextTarget;
    }

    /**
     * @param value {@link #context} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The visit, admission or other contact between patient and health care provider the medication administration was performed as part of.)
     */
    public MedicationAdministration setContextTarget(Resource value) { 
      this.contextTarget = value;
      return this;
    }

    /**
     * @return {@link #supportingInformation} (Additional information (for example, patient height and weight) that supports the administration of the medication.)
     */
    public List<Reference> getSupportingInformation() { 
      if (this.supportingInformation == null)
        this.supportingInformation = new ArrayList<Reference>();
      return this.supportingInformation;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicationAdministration setSupportingInformation(List<Reference> theSupportingInformation) { 
      this.supportingInformation = theSupportingInformation;
      return this;
    }

    public boolean hasSupportingInformation() { 
      if (this.supportingInformation == null)
        return false;
      for (Reference item : this.supportingInformation)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addSupportingInformation() { //3
      Reference t = new Reference();
      if (this.supportingInformation == null)
        this.supportingInformation = new ArrayList<Reference>();
      this.supportingInformation.add(t);
      return t;
    }

    public MedicationAdministration addSupportingInformation(Reference t) { //3
      if (t == null)
        return this;
      if (this.supportingInformation == null)
        this.supportingInformation = new ArrayList<Reference>();
      this.supportingInformation.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #supportingInformation}, creating it if it does not already exist
     */
    public Reference getSupportingInformationFirstRep() { 
      if (getSupportingInformation().isEmpty()) {
        addSupportingInformation();
      }
      return getSupportingInformation().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Resource> getSupportingInformationTarget() { 
      if (this.supportingInformationTarget == null)
        this.supportingInformationTarget = new ArrayList<Resource>();
      return this.supportingInformationTarget;
    }

    /**
     * @return {@link #effective} (A specific date/time or interval of time during which the administration took place (or did not take place, when the 'notGiven' attribute is true). For many administrations, such as swallowing a tablet the use of dateTime is more appropriate.)
     */
    public Type getEffective() { 
      return this.effective;
    }

    /**
     * @return {@link #effective} (A specific date/time or interval of time during which the administration took place (or did not take place, when the 'notGiven' attribute is true). For many administrations, such as swallowing a tablet the use of dateTime is more appropriate.)
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
     * @return {@link #effective} (A specific date/time or interval of time during which the administration took place (or did not take place, when the 'notGiven' attribute is true). For many administrations, such as swallowing a tablet the use of dateTime is more appropriate.)
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
     * @param value {@link #effective} (A specific date/time or interval of time during which the administration took place (or did not take place, when the 'notGiven' attribute is true). For many administrations, such as swallowing a tablet the use of dateTime is more appropriate.)
     */
    public MedicationAdministration setEffective(Type value) { 
      this.effective = value;
      return this;
    }

    /**
     * @return {@link #performer} (The individual who was responsible for giving the medication to the patient.)
     */
    public List<MedicationAdministrationPerformerComponent> getPerformer() { 
      if (this.performer == null)
        this.performer = new ArrayList<MedicationAdministrationPerformerComponent>();
      return this.performer;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicationAdministration setPerformer(List<MedicationAdministrationPerformerComponent> thePerformer) { 
      this.performer = thePerformer;
      return this;
    }

    public boolean hasPerformer() { 
      if (this.performer == null)
        return false;
      for (MedicationAdministrationPerformerComponent item : this.performer)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public MedicationAdministrationPerformerComponent addPerformer() { //3
      MedicationAdministrationPerformerComponent t = new MedicationAdministrationPerformerComponent();
      if (this.performer == null)
        this.performer = new ArrayList<MedicationAdministrationPerformerComponent>();
      this.performer.add(t);
      return t;
    }

    public MedicationAdministration addPerformer(MedicationAdministrationPerformerComponent t) { //3
      if (t == null)
        return this;
      if (this.performer == null)
        this.performer = new ArrayList<MedicationAdministrationPerformerComponent>();
      this.performer.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #performer}, creating it if it does not already exist
     */
    public MedicationAdministrationPerformerComponent getPerformerFirstRep() { 
      if (getPerformer().isEmpty()) {
        addPerformer();
      }
      return getPerformer().get(0);
    }

    /**
     * @return {@link #notGiven} (Set this to true if the record is saying that the medication was NOT administered.). This is the underlying object with id, value and extensions. The accessor "getNotGiven" gives direct access to the value
     */
    public BooleanType getNotGivenElement() { 
      if (this.notGiven == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationAdministration.notGiven");
        else if (Configuration.doAutoCreate())
          this.notGiven = new BooleanType(); // bb
      return this.notGiven;
    }

    public boolean hasNotGivenElement() { 
      return this.notGiven != null && !this.notGiven.isEmpty();
    }

    public boolean hasNotGiven() { 
      return this.notGiven != null && !this.notGiven.isEmpty();
    }

    /**
     * @param value {@link #notGiven} (Set this to true if the record is saying that the medication was NOT administered.). This is the underlying object with id, value and extensions. The accessor "getNotGiven" gives direct access to the value
     */
    public MedicationAdministration setNotGivenElement(BooleanType value) { 
      this.notGiven = value;
      return this;
    }

    /**
     * @return Set this to true if the record is saying that the medication was NOT administered.
     */
    public boolean getNotGiven() { 
      return this.notGiven == null || this.notGiven.isEmpty() ? false : this.notGiven.getValue();
    }

    /**
     * @param value Set this to true if the record is saying that the medication was NOT administered.
     */
    public MedicationAdministration setNotGiven(boolean value) { 
        if (this.notGiven == null)
          this.notGiven = new BooleanType();
        this.notGiven.setValue(value);
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
     * @return {@link #reasonCode} (A code indicating why the medication was given.)
     */
    public List<CodeableConcept> getReasonCode() { 
      if (this.reasonCode == null)
        this.reasonCode = new ArrayList<CodeableConcept>();
      return this.reasonCode;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicationAdministration setReasonCode(List<CodeableConcept> theReasonCode) { 
      this.reasonCode = theReasonCode;
      return this;
    }

    public boolean hasReasonCode() { 
      if (this.reasonCode == null)
        return false;
      for (CodeableConcept item : this.reasonCode)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addReasonCode() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.reasonCode == null)
        this.reasonCode = new ArrayList<CodeableConcept>();
      this.reasonCode.add(t);
      return t;
    }

    public MedicationAdministration addReasonCode(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.reasonCode == null)
        this.reasonCode = new ArrayList<CodeableConcept>();
      this.reasonCode.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #reasonCode}, creating it if it does not already exist
     */
    public CodeableConcept getReasonCodeFirstRep() { 
      if (getReasonCode().isEmpty()) {
        addReasonCode();
      }
      return getReasonCode().get(0);
    }

    /**
     * @return {@link #reasonReference} (Condition or observation that supports why the medication was administered.)
     */
    public List<Reference> getReasonReference() { 
      if (this.reasonReference == null)
        this.reasonReference = new ArrayList<Reference>();
      return this.reasonReference;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicationAdministration setReasonReference(List<Reference> theReasonReference) { 
      this.reasonReference = theReasonReference;
      return this;
    }

    public boolean hasReasonReference() { 
      if (this.reasonReference == null)
        return false;
      for (Reference item : this.reasonReference)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addReasonReference() { //3
      Reference t = new Reference();
      if (this.reasonReference == null)
        this.reasonReference = new ArrayList<Reference>();
      this.reasonReference.add(t);
      return t;
    }

    public MedicationAdministration addReasonReference(Reference t) { //3
      if (t == null)
        return this;
      if (this.reasonReference == null)
        this.reasonReference = new ArrayList<Reference>();
      this.reasonReference.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #reasonReference}, creating it if it does not already exist
     */
    public Reference getReasonReferenceFirstRep() { 
      if (getReasonReference().isEmpty()) {
        addReasonReference();
      }
      return getReasonReference().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Resource> getReasonReferenceTarget() { 
      if (this.reasonReferenceTarget == null)
        this.reasonReferenceTarget = new ArrayList<Resource>();
      return this.reasonReferenceTarget;
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
    public MedicationRequest getPrescriptionTarget() { 
      if (this.prescriptionTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationAdministration.prescription");
        else if (Configuration.doAutoCreate())
          this.prescriptionTarget = new MedicationRequest(); // aa
      return this.prescriptionTarget;
    }

    /**
     * @param value {@link #prescription} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The original request, instruction or authority to perform the administration.)
     */
    public MedicationAdministration setPrescriptionTarget(MedicationRequest value) { 
      this.prescriptionTarget = value;
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
    public List<Reference> getEventHistory() { 
      if (this.eventHistory == null)
        this.eventHistory = new ArrayList<Reference>();
      return this.eventHistory;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicationAdministration setEventHistory(List<Reference> theEventHistory) { 
      this.eventHistory = theEventHistory;
      return this;
    }

    public boolean hasEventHistory() { 
      if (this.eventHistory == null)
        return false;
      for (Reference item : this.eventHistory)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addEventHistory() { //3
      Reference t = new Reference();
      if (this.eventHistory == null)
        this.eventHistory = new ArrayList<Reference>();
      this.eventHistory.add(t);
      return t;
    }

    public MedicationAdministration addEventHistory(Reference t) { //3
      if (t == null)
        return this;
      if (this.eventHistory == null)
        this.eventHistory = new ArrayList<Reference>();
      this.eventHistory.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #eventHistory}, creating it if it does not already exist
     */
    public Reference getEventHistoryFirstRep() { 
      if (getEventHistory().isEmpty()) {
        addEventHistory();
      }
      return getEventHistory().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Provenance> getEventHistoryTarget() { 
      if (this.eventHistoryTarget == null)
        this.eventHistoryTarget = new ArrayList<Provenance>();
      return this.eventHistoryTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public Provenance addEventHistoryTarget() { 
      Provenance r = new Provenance();
      if (this.eventHistoryTarget == null)
        this.eventHistoryTarget = new ArrayList<Provenance>();
      this.eventHistoryTarget.add(r);
      return r;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "External identifier - FHIR will generate its own internal identifiers (probably URLs) which do not need to be explicitly managed by the resource.  The identifier here is one that would be used by another non-FHIR system - for example an automated medication pump would provide a record each time it operated; an administration while the patient was off the ward might be made with a different system and entered after the event.  Particularly important if these records have to be updated.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("definition", "Reference(PlanDefinition|ActivityDefinition)", "A protocol, guideline, orderset or other definition that was adhered to in whole or in part by this event.", 0, java.lang.Integer.MAX_VALUE, definition));
        childrenList.add(new Property("partOf", "Reference(MedicationAdministration|Procedure)", "A larger event of which this particular event is a component or step.", 0, java.lang.Integer.MAX_VALUE, partOf));
        childrenList.add(new Property("status", "code", "Will generally be set to show that the administration has been completed.  For some long running administrations such as infusions it is possible for an administration to be started but not completed or it may be paused while some other process is under way.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("category", "CodeableConcept", "Indicates the type of medication administration and where the medication is expected to be consumed or administered.", 0, java.lang.Integer.MAX_VALUE, category));
        childrenList.add(new Property("medication[x]", "CodeableConcept|Reference(Medication)", "Identifies the medication that was administered. This is either a link to a resource representing the details of the medication or a simple attribute carrying a code that identifies the medication from a known list of medications.", 0, java.lang.Integer.MAX_VALUE, medication));
        childrenList.add(new Property("subject", "Reference(Patient|Group)", "The person or animal or group receiving the medication.", 0, java.lang.Integer.MAX_VALUE, subject));
        childrenList.add(new Property("context", "Reference(Encounter|EpisodeOfCare)", "The visit, admission or other contact between patient and health care provider the medication administration was performed as part of.", 0, java.lang.Integer.MAX_VALUE, context));
        childrenList.add(new Property("supportingInformation", "Reference(Any)", "Additional information (for example, patient height and weight) that supports the administration of the medication.", 0, java.lang.Integer.MAX_VALUE, supportingInformation));
        childrenList.add(new Property("effective[x]", "dateTime|Period", "A specific date/time or interval of time during which the administration took place (or did not take place, when the 'notGiven' attribute is true). For many administrations, such as swallowing a tablet the use of dateTime is more appropriate.", 0, java.lang.Integer.MAX_VALUE, effective));
        childrenList.add(new Property("performer", "", "The individual who was responsible for giving the medication to the patient.", 0, java.lang.Integer.MAX_VALUE, performer));
        childrenList.add(new Property("notGiven", "boolean", "Set this to true if the record is saying that the medication was NOT administered.", 0, java.lang.Integer.MAX_VALUE, notGiven));
        childrenList.add(new Property("reasonNotGiven", "CodeableConcept", "A code indicating why the administration was not performed.", 0, java.lang.Integer.MAX_VALUE, reasonNotGiven));
        childrenList.add(new Property("reasonCode", "CodeableConcept", "A code indicating why the medication was given.", 0, java.lang.Integer.MAX_VALUE, reasonCode));
        childrenList.add(new Property("reasonReference", "Reference(Condition|Observation)", "Condition or observation that supports why the medication was administered.", 0, java.lang.Integer.MAX_VALUE, reasonReference));
        childrenList.add(new Property("prescription", "Reference(MedicationRequest)", "The original request, instruction or authority to perform the administration.", 0, java.lang.Integer.MAX_VALUE, prescription));
        childrenList.add(new Property("device", "Reference(Device)", "The device used in administering the medication to the patient.  For example, a particular infusion pump.", 0, java.lang.Integer.MAX_VALUE, device));
        childrenList.add(new Property("note", "Annotation", "Extra information about the medication administration that is not conveyed by the other attributes.", 0, java.lang.Integer.MAX_VALUE, note));
        childrenList.add(new Property("dosage", "", "Describes the medication dosage information details e.g. dose, rate, site, route, etc.", 0, java.lang.Integer.MAX_VALUE, dosage));
        childrenList.add(new Property("eventHistory", "Reference(Provenance)", "A summary of the events of interest that have occurred, such as when the administration was verified.", 0, java.lang.Integer.MAX_VALUE, eventHistory));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case -1014418093: /*definition*/ return this.definition == null ? new Base[0] : this.definition.toArray(new Base[this.definition.size()]); // Reference
        case -995410646: /*partOf*/ return this.partOf == null ? new Base[0] : this.partOf.toArray(new Base[this.partOf.size()]); // Reference
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<MedicationAdministrationStatus>
        case 50511102: /*category*/ return this.category == null ? new Base[0] : new Base[] {this.category}; // CodeableConcept
        case 1998965455: /*medication*/ return this.medication == null ? new Base[0] : new Base[] {this.medication}; // Type
        case -1867885268: /*subject*/ return this.subject == null ? new Base[0] : new Base[] {this.subject}; // Reference
        case 951530927: /*context*/ return this.context == null ? new Base[0] : new Base[] {this.context}; // Reference
        case -1248768647: /*supportingInformation*/ return this.supportingInformation == null ? new Base[0] : this.supportingInformation.toArray(new Base[this.supportingInformation.size()]); // Reference
        case -1468651097: /*effective*/ return this.effective == null ? new Base[0] : new Base[] {this.effective}; // Type
        case 481140686: /*performer*/ return this.performer == null ? new Base[0] : this.performer.toArray(new Base[this.performer.size()]); // MedicationAdministrationPerformerComponent
        case 1554065514: /*notGiven*/ return this.notGiven == null ? new Base[0] : new Base[] {this.notGiven}; // BooleanType
        case 2101123790: /*reasonNotGiven*/ return this.reasonNotGiven == null ? new Base[0] : this.reasonNotGiven.toArray(new Base[this.reasonNotGiven.size()]); // CodeableConcept
        case 722137681: /*reasonCode*/ return this.reasonCode == null ? new Base[0] : this.reasonCode.toArray(new Base[this.reasonCode.size()]); // CodeableConcept
        case -1146218137: /*reasonReference*/ return this.reasonReference == null ? new Base[0] : this.reasonReference.toArray(new Base[this.reasonReference.size()]); // Reference
        case 460301338: /*prescription*/ return this.prescription == null ? new Base[0] : new Base[] {this.prescription}; // Reference
        case -1335157162: /*device*/ return this.device == null ? new Base[0] : this.device.toArray(new Base[this.device.size()]); // Reference
        case 3387378: /*note*/ return this.note == null ? new Base[0] : this.note.toArray(new Base[this.note.size()]); // Annotation
        case -1326018889: /*dosage*/ return this.dosage == null ? new Base[0] : new Base[] {this.dosage}; // MedicationAdministrationDosageComponent
        case 1835190426: /*eventHistory*/ return this.eventHistory == null ? new Base[0] : this.eventHistory.toArray(new Base[this.eventHistory.size()]); // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.getIdentifier().add(castToIdentifier(value)); // Identifier
          return value;
        case -1014418093: // definition
          this.getDefinition().add(castToReference(value)); // Reference
          return value;
        case -995410646: // partOf
          this.getPartOf().add(castToReference(value)); // Reference
          return value;
        case -892481550: // status
          value = new MedicationAdministrationStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<MedicationAdministrationStatus>
          return value;
        case 50511102: // category
          this.category = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 1998965455: // medication
          this.medication = castToType(value); // Type
          return value;
        case -1867885268: // subject
          this.subject = castToReference(value); // Reference
          return value;
        case 951530927: // context
          this.context = castToReference(value); // Reference
          return value;
        case -1248768647: // supportingInformation
          this.getSupportingInformation().add(castToReference(value)); // Reference
          return value;
        case -1468651097: // effective
          this.effective = castToType(value); // Type
          return value;
        case 481140686: // performer
          this.getPerformer().add((MedicationAdministrationPerformerComponent) value); // MedicationAdministrationPerformerComponent
          return value;
        case 1554065514: // notGiven
          this.notGiven = castToBoolean(value); // BooleanType
          return value;
        case 2101123790: // reasonNotGiven
          this.getReasonNotGiven().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 722137681: // reasonCode
          this.getReasonCode().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -1146218137: // reasonReference
          this.getReasonReference().add(castToReference(value)); // Reference
          return value;
        case 460301338: // prescription
          this.prescription = castToReference(value); // Reference
          return value;
        case -1335157162: // device
          this.getDevice().add(castToReference(value)); // Reference
          return value;
        case 3387378: // note
          this.getNote().add(castToAnnotation(value)); // Annotation
          return value;
        case -1326018889: // dosage
          this.dosage = (MedicationAdministrationDosageComponent) value; // MedicationAdministrationDosageComponent
          return value;
        case 1835190426: // eventHistory
          this.getEventHistory().add(castToReference(value)); // Reference
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier")) {
          this.getIdentifier().add(castToIdentifier(value));
        } else if (name.equals("definition")) {
          this.getDefinition().add(castToReference(value));
        } else if (name.equals("partOf")) {
          this.getPartOf().add(castToReference(value));
        } else if (name.equals("status")) {
          value = new MedicationAdministrationStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<MedicationAdministrationStatus>
        } else if (name.equals("category")) {
          this.category = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("medication[x]")) {
          this.medication = castToType(value); // Type
        } else if (name.equals("subject")) {
          this.subject = castToReference(value); // Reference
        } else if (name.equals("context")) {
          this.context = castToReference(value); // Reference
        } else if (name.equals("supportingInformation")) {
          this.getSupportingInformation().add(castToReference(value));
        } else if (name.equals("effective[x]")) {
          this.effective = castToType(value); // Type
        } else if (name.equals("performer")) {
          this.getPerformer().add((MedicationAdministrationPerformerComponent) value);
        } else if (name.equals("notGiven")) {
          this.notGiven = castToBoolean(value); // BooleanType
        } else if (name.equals("reasonNotGiven")) {
          this.getReasonNotGiven().add(castToCodeableConcept(value));
        } else if (name.equals("reasonCode")) {
          this.getReasonCode().add(castToCodeableConcept(value));
        } else if (name.equals("reasonReference")) {
          this.getReasonReference().add(castToReference(value));
        } else if (name.equals("prescription")) {
          this.prescription = castToReference(value); // Reference
        } else if (name.equals("device")) {
          this.getDevice().add(castToReference(value));
        } else if (name.equals("note")) {
          this.getNote().add(castToAnnotation(value));
        } else if (name.equals("dosage")) {
          this.dosage = (MedicationAdministrationDosageComponent) value; // MedicationAdministrationDosageComponent
        } else if (name.equals("eventHistory")) {
          this.getEventHistory().add(castToReference(value));
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return addIdentifier(); 
        case -1014418093:  return addDefinition(); 
        case -995410646:  return addPartOf(); 
        case -892481550:  return getStatusElement();
        case 50511102:  return getCategory(); 
        case 1458402129:  return getMedication(); 
        case 1998965455:  return getMedication(); 
        case -1867885268:  return getSubject(); 
        case 951530927:  return getContext(); 
        case -1248768647:  return addSupportingInformation(); 
        case 247104889:  return getEffective(); 
        case -1468651097:  return getEffective(); 
        case 481140686:  return addPerformer(); 
        case 1554065514:  return getNotGivenElement();
        case 2101123790:  return addReasonNotGiven(); 
        case 722137681:  return addReasonCode(); 
        case -1146218137:  return addReasonReference(); 
        case 460301338:  return getPrescription(); 
        case -1335157162:  return addDevice(); 
        case 3387378:  return addNote(); 
        case -1326018889:  return getDosage(); 
        case 1835190426:  return addEventHistory(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case -1014418093: /*definition*/ return new String[] {"Reference"};
        case -995410646: /*partOf*/ return new String[] {"Reference"};
        case -892481550: /*status*/ return new String[] {"code"};
        case 50511102: /*category*/ return new String[] {"CodeableConcept"};
        case 1998965455: /*medication*/ return new String[] {"CodeableConcept", "Reference"};
        case -1867885268: /*subject*/ return new String[] {"Reference"};
        case 951530927: /*context*/ return new String[] {"Reference"};
        case -1248768647: /*supportingInformation*/ return new String[] {"Reference"};
        case -1468651097: /*effective*/ return new String[] {"dateTime", "Period"};
        case 481140686: /*performer*/ return new String[] {};
        case 1554065514: /*notGiven*/ return new String[] {"boolean"};
        case 2101123790: /*reasonNotGiven*/ return new String[] {"CodeableConcept"};
        case 722137681: /*reasonCode*/ return new String[] {"CodeableConcept"};
        case -1146218137: /*reasonReference*/ return new String[] {"Reference"};
        case 460301338: /*prescription*/ return new String[] {"Reference"};
        case -1335157162: /*device*/ return new String[] {"Reference"};
        case 3387378: /*note*/ return new String[] {"Annotation"};
        case -1326018889: /*dosage*/ return new String[] {};
        case 1835190426: /*eventHistory*/ return new String[] {"Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("definition")) {
          return addDefinition();
        }
        else if (name.equals("partOf")) {
          return addPartOf();
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type MedicationAdministration.status");
        }
        else if (name.equals("category")) {
          this.category = new CodeableConcept();
          return this.category;
        }
        else if (name.equals("medicationCodeableConcept")) {
          this.medication = new CodeableConcept();
          return this.medication;
        }
        else if (name.equals("medicationReference")) {
          this.medication = new Reference();
          return this.medication;
        }
        else if (name.equals("subject")) {
          this.subject = new Reference();
          return this.subject;
        }
        else if (name.equals("context")) {
          this.context = new Reference();
          return this.context;
        }
        else if (name.equals("supportingInformation")) {
          return addSupportingInformation();
        }
        else if (name.equals("effectiveDateTime")) {
          this.effective = new DateTimeType();
          return this.effective;
        }
        else if (name.equals("effectivePeriod")) {
          this.effective = new Period();
          return this.effective;
        }
        else if (name.equals("performer")) {
          return addPerformer();
        }
        else if (name.equals("notGiven")) {
          throw new FHIRException("Cannot call addChild on a primitive type MedicationAdministration.notGiven");
        }
        else if (name.equals("reasonNotGiven")) {
          return addReasonNotGiven();
        }
        else if (name.equals("reasonCode")) {
          return addReasonCode();
        }
        else if (name.equals("reasonReference")) {
          return addReasonReference();
        }
        else if (name.equals("prescription")) {
          this.prescription = new Reference();
          return this.prescription;
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
        if (definition != null) {
          dst.definition = new ArrayList<Reference>();
          for (Reference i : definition)
            dst.definition.add(i.copy());
        };
        if (partOf != null) {
          dst.partOf = new ArrayList<Reference>();
          for (Reference i : partOf)
            dst.partOf.add(i.copy());
        };
        dst.status = status == null ? null : status.copy();
        dst.category = category == null ? null : category.copy();
        dst.medication = medication == null ? null : medication.copy();
        dst.subject = subject == null ? null : subject.copy();
        dst.context = context == null ? null : context.copy();
        if (supportingInformation != null) {
          dst.supportingInformation = new ArrayList<Reference>();
          for (Reference i : supportingInformation)
            dst.supportingInformation.add(i.copy());
        };
        dst.effective = effective == null ? null : effective.copy();
        if (performer != null) {
          dst.performer = new ArrayList<MedicationAdministrationPerformerComponent>();
          for (MedicationAdministrationPerformerComponent i : performer)
            dst.performer.add(i.copy());
        };
        dst.notGiven = notGiven == null ? null : notGiven.copy();
        if (reasonNotGiven != null) {
          dst.reasonNotGiven = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : reasonNotGiven)
            dst.reasonNotGiven.add(i.copy());
        };
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
        dst.prescription = prescription == null ? null : prescription.copy();
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
          dst.eventHistory = new ArrayList<Reference>();
          for (Reference i : eventHistory)
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
        return compareDeep(identifier, o.identifier, true) && compareDeep(definition, o.definition, true)
           && compareDeep(partOf, o.partOf, true) && compareDeep(status, o.status, true) && compareDeep(category, o.category, true)
           && compareDeep(medication, o.medication, true) && compareDeep(subject, o.subject, true) && compareDeep(context, o.context, true)
           && compareDeep(supportingInformation, o.supportingInformation, true) && compareDeep(effective, o.effective, true)
           && compareDeep(performer, o.performer, true) && compareDeep(notGiven, o.notGiven, true) && compareDeep(reasonNotGiven, o.reasonNotGiven, true)
           && compareDeep(reasonCode, o.reasonCode, true) && compareDeep(reasonReference, o.reasonReference, true)
           && compareDeep(prescription, o.prescription, true) && compareDeep(device, o.device, true) && compareDeep(note, o.note, true)
           && compareDeep(dosage, o.dosage, true) && compareDeep(eventHistory, o.eventHistory, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof MedicationAdministration))
          return false;
        MedicationAdministration o = (MedicationAdministration) other;
        return compareValues(status, o.status, true) && compareValues(notGiven, o.notGiven, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, definition, partOf
          , status, category, medication, subject, context, supportingInformation, effective
          , performer, notGiven, reasonNotGiven, reasonCode, reasonReference, prescription
          , device, note, dosage, eventHistory);
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
   * Description: <b>The identify of the individual who administered the medication</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MedicationAdministration.performer.actor</b><br>
   * </p>
   */
  @SearchParamDefinition(name="performer", path="MedicationAdministration.performer.actor", description="The identify of the individual who administered the medication", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient"), @ca.uhn.fhir.model.api.annotation.Compartment(name="Practitioner"), @ca.uhn.fhir.model.api.annotation.Compartment(name="RelatedPerson") }, target={Device.class, Patient.class, Practitioner.class, RelatedPerson.class } )
  public static final String SP_PERFORMER = "performer";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>performer</b>
   * <p>
   * Description: <b>The identify of the individual who administered the medication</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MedicationAdministration.performer.actor</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PERFORMER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PERFORMER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>MedicationAdministration:performer</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PERFORMER = new ca.uhn.fhir.model.api.Include("MedicationAdministration:performer").toLocked();

 /**
   * Search parameter: <b>subject</b>
   * <p>
   * Description: <b>The identify of the individual or group to list administrations for</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MedicationAdministration.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="subject", path="MedicationAdministration.subject", description="The identify of the individual or group to list administrations for", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient") }, target={Group.class, Patient.class } )
  public static final String SP_SUBJECT = "subject";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>subject</b>
   * <p>
   * Description: <b>The identify of the individual or group to list administrations for</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MedicationAdministration.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SUBJECT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SUBJECT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>MedicationAdministration:subject</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SUBJECT = new ca.uhn.fhir.model.api.Include("MedicationAdministration:subject").toLocked();

 /**
   * Search parameter: <b>not-given</b>
   * <p>
   * Description: <b>Administrations that were not made</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MedicationAdministration.notGiven</b><br>
   * </p>
   */
  @SearchParamDefinition(name="not-given", path="MedicationAdministration.notGiven", description="Administrations that were not made", type="token" )
  public static final String SP_NOT_GIVEN = "not-given";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>not-given</b>
   * <p>
   * Description: <b>Administrations that were not made</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MedicationAdministration.notGiven</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam NOT_GIVEN = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_NOT_GIVEN);

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
   * Search parameter: <b>reason-given</b>
   * <p>
   * Description: <b>Reasons for administering the medication</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MedicationAdministration.reasonCode</b><br>
   * </p>
   */
  @SearchParamDefinition(name="reason-given", path="MedicationAdministration.reasonCode", description="Reasons for administering the medication", type="token" )
  public static final String SP_REASON_GIVEN = "reason-given";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>reason-given</b>
   * <p>
   * Description: <b>Reasons for administering the medication</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MedicationAdministration.reasonCode</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam REASON_GIVEN = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_REASON_GIVEN);

 /**
   * Search parameter: <b>prescription</b>
   * <p>
   * Description: <b>The identity of a prescription to list administrations from</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MedicationAdministration.prescription</b><br>
   * </p>
   */
  @SearchParamDefinition(name="prescription", path="MedicationAdministration.prescription", description="The identity of a prescription to list administrations from", type="reference", target={MedicationRequest.class } )
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
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>The identity of a patient to list administrations  for</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MedicationAdministration.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="MedicationAdministration.subject", description="The identity of a patient to list administrations  for", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient") }, target={Patient.class } )
  public static final String SP_PATIENT = "patient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patient</b>
   * <p>
   * Description: <b>The identity of a patient to list administrations  for</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MedicationAdministration.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PATIENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PATIENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>MedicationAdministration:patient</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PATIENT = new ca.uhn.fhir.model.api.Include("MedicationAdministration:patient").toLocked();

 /**
   * Search parameter: <b>effective-time</b>
   * <p>
   * Description: <b>Date administration happened (or did not happen)</b><br>
   * Type: <b>date</b><br>
   * Path: <b>MedicationAdministration.effective[x]</b><br>
   * </p>
   */
  @SearchParamDefinition(name="effective-time", path="MedicationAdministration.effective", description="Date administration happened (or did not happen)", type="date" )
  public static final String SP_EFFECTIVE_TIME = "effective-time";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>effective-time</b>
   * <p>
   * Description: <b>Date administration happened (or did not happen)</b><br>
   * Type: <b>date</b><br>
   * Path: <b>MedicationAdministration.effective[x]</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam EFFECTIVE_TIME = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_EFFECTIVE_TIME);

 /**
   * Search parameter: <b>context</b>
   * <p>
   * Description: <b>Return administrations that share this encounter or episode of care</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MedicationAdministration.context</b><br>
   * </p>
   */
  @SearchParamDefinition(name="context", path="MedicationAdministration.context", description="Return administrations that share this encounter or episode of care", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Encounter") }, target={Encounter.class, EpisodeOfCare.class } )
  public static final String SP_CONTEXT = "context";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>context</b>
   * <p>
   * Description: <b>Return administrations that share this encounter or episode of care</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MedicationAdministration.context</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam CONTEXT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_CONTEXT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>MedicationAdministration:context</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_CONTEXT = new ca.uhn.fhir.model.api.Include("MedicationAdministration:context").toLocked();

 /**
   * Search parameter: <b>reason-not-given</b>
   * <p>
   * Description: <b>Reasons for not administering the medication</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MedicationAdministration.reasonNotGiven</b><br>
   * </p>
   */
  @SearchParamDefinition(name="reason-not-given", path="MedicationAdministration.reasonNotGiven", description="Reasons for not administering the medication", type="token" )
  public static final String SP_REASON_NOT_GIVEN = "reason-not-given";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>reason-not-given</b>
   * <p>
   * Description: <b>Reasons for not administering the medication</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MedicationAdministration.reasonNotGiven</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam REASON_NOT_GIVEN = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_REASON_NOT_GIVEN);

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

