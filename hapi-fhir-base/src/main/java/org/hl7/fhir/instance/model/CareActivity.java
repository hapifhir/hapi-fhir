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

// Generated on Sun, Dec 7, 2014 21:45-0500 for FHIR v0.3.0

import java.util.*;

import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.instance.model.annotations.ResourceDef;
import org.hl7.fhir.instance.model.annotations.SearchParamDefinition;
import org.hl7.fhir.instance.model.annotations.Block;
import org.hl7.fhir.instance.model.annotations.Child;
import org.hl7.fhir.instance.model.annotations.Description;
/**
 * Describes the intention of how one or more practitioners intend to deliver care for a particular patient for a period of time, possibly limited to care for a specific condition or set of conditions.
 */
@ResourceDef(name="CareActivity", profile="http://hl7.org/fhir/Profile/CareActivity")
public class CareActivity extends DomainResource {

    public enum CareActivityStatus implements FhirEnum {
        /**
         * Activity is planned but no action has yet been taken.
         */
        NOTSTARTED, 
        /**
         * Appointment or other booking has occurred but activity has not yet begun.
         */
        SCHEDULED, 
        /**
         * Activity has been started but is not yet complete.
         */
        INPROGRESS, 
        /**
         * Activity was started but has temporarily ceased with an expectation of resumption at a future time.
         */
        ONHOLD, 
        /**
         * The activities have been completed (more or less) as planned.
         */
        COMPLETED, 
        /**
         * The activities have been ended prior to completion (perhaps even before they were started).
         */
        CANCELLED, 
        /**
         * added to help the parsers
         */
        NULL;

      public static final CareActivityStatusEnumFactory ENUM_FACTORY = new CareActivityStatusEnumFactory();

        public static CareActivityStatus fromCode(String codeString) throws IllegalArgumentException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("not started".equals(codeString))
          return NOTSTARTED;
        if ("scheduled".equals(codeString))
          return SCHEDULED;
        if ("in progress".equals(codeString))
          return INPROGRESS;
        if ("on hold".equals(codeString))
          return ONHOLD;
        if ("completed".equals(codeString))
          return COMPLETED;
        if ("cancelled".equals(codeString))
          return CANCELLED;
        throw new IllegalArgumentException("Unknown CareActivityStatus code '"+codeString+"'");
        }
        @Override
        public String toCode() {
          switch (this) {
            case NOTSTARTED: return "not started";
            case SCHEDULED: return "scheduled";
            case INPROGRESS: return "in progress";
            case ONHOLD: return "on hold";
            case COMPLETED: return "completed";
            case CANCELLED: return "cancelled";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case NOTSTARTED: return "";
            case SCHEDULED: return "";
            case INPROGRESS: return "";
            case ONHOLD: return "";
            case COMPLETED: return "";
            case CANCELLED: return "";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case NOTSTARTED: return "Activity is planned but no action has yet been taken.";
            case SCHEDULED: return "Appointment or other booking has occurred but activity has not yet begun.";
            case INPROGRESS: return "Activity has been started but is not yet complete.";
            case ONHOLD: return "Activity was started but has temporarily ceased with an expectation of resumption at a future time.";
            case COMPLETED: return "The activities have been completed (more or less) as planned.";
            case CANCELLED: return "The activities have been ended prior to completion (perhaps even before they were started).";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case NOTSTARTED: return "not started";
            case SCHEDULED: return "scheduled";
            case INPROGRESS: return "in progress";
            case ONHOLD: return "on hold";
            case COMPLETED: return "completed";
            case CANCELLED: return "cancelled";
            default: return "?";
          }
        }
    }

  public static class CareActivityStatusEnumFactory implements EnumFactory<CareActivityStatus> {
    public CareActivityStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("not started".equals(codeString))
          return CareActivityStatus.NOTSTARTED;
        if ("scheduled".equals(codeString))
          return CareActivityStatus.SCHEDULED;
        if ("in progress".equals(codeString))
          return CareActivityStatus.INPROGRESS;
        if ("on hold".equals(codeString))
          return CareActivityStatus.ONHOLD;
        if ("completed".equals(codeString))
          return CareActivityStatus.COMPLETED;
        if ("cancelled".equals(codeString))
          return CareActivityStatus.CANCELLED;
        throw new IllegalArgumentException("Unknown CareActivityStatus code '"+codeString+"'");
        }
    public String toCode(CareActivityStatus code) throws IllegalArgumentException {
      if (code == CareActivityStatus.NOTSTARTED)
        return "not started";
      if (code == CareActivityStatus.SCHEDULED)
        return "scheduled";
      if (code == CareActivityStatus.INPROGRESS)
        return "in progress";
      if (code == CareActivityStatus.ONHOLD)
        return "on hold";
      if (code == CareActivityStatus.COMPLETED)
        return "completed";
      if (code == CareActivityStatus.CANCELLED)
        return "cancelled";
      return "?";
      }
    }

    public enum CareActivityCategory implements FhirEnum {
        /**
         * Plan for the patient to consume food of a specified nature.
         */
        DIET, 
        /**
         * Plan for the patient to consume/receive a drug, vaccine or other product.
         */
        DRUG, 
        /**
         * Plan to meet or communicate with the patient (in-patient, out-patient, phone call, etc.).
         */
        ENCOUNTER, 
        /**
         * Plan to capture information about a patient (vitals, labs, diagnostic images, etc.).
         */
        OBSERVATION, 
        /**
         * Plan to modify the patient in some way (surgery, physiotherapy, education, counseling, etc.).
         */
        PROCEDURE, 
        /**
         * Plan to provide something to the patient (medication, medical supply, etc.).
         */
        SUPPLY, 
        /**
         * Some other form of action.
         */
        OTHER, 
        /**
         * added to help the parsers
         */
        NULL;

      public static final CareActivityCategoryEnumFactory ENUM_FACTORY = new CareActivityCategoryEnumFactory();

        public static CareActivityCategory fromCode(String codeString) throws IllegalArgumentException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("diet".equals(codeString))
          return DIET;
        if ("drug".equals(codeString))
          return DRUG;
        if ("encounter".equals(codeString))
          return ENCOUNTER;
        if ("observation".equals(codeString))
          return OBSERVATION;
        if ("procedure".equals(codeString))
          return PROCEDURE;
        if ("supply".equals(codeString))
          return SUPPLY;
        if ("other".equals(codeString))
          return OTHER;
        throw new IllegalArgumentException("Unknown CareActivityCategory code '"+codeString+"'");
        }
        @Override
        public String toCode() {
          switch (this) {
            case DIET: return "diet";
            case DRUG: return "drug";
            case ENCOUNTER: return "encounter";
            case OBSERVATION: return "observation";
            case PROCEDURE: return "procedure";
            case SUPPLY: return "supply";
            case OTHER: return "other";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case DIET: return "";
            case DRUG: return "";
            case ENCOUNTER: return "";
            case OBSERVATION: return "";
            case PROCEDURE: return "";
            case SUPPLY: return "";
            case OTHER: return "";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case DIET: return "Plan for the patient to consume food of a specified nature.";
            case DRUG: return "Plan for the patient to consume/receive a drug, vaccine or other product.";
            case ENCOUNTER: return "Plan to meet or communicate with the patient (in-patient, out-patient, phone call, etc.).";
            case OBSERVATION: return "Plan to capture information about a patient (vitals, labs, diagnostic images, etc.).";
            case PROCEDURE: return "Plan to modify the patient in some way (surgery, physiotherapy, education, counseling, etc.).";
            case SUPPLY: return "Plan to provide something to the patient (medication, medical supply, etc.).";
            case OTHER: return "Some other form of action.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case DIET: return "diet";
            case DRUG: return "drug";
            case ENCOUNTER: return "encounter";
            case OBSERVATION: return "observation";
            case PROCEDURE: return "procedure";
            case SUPPLY: return "supply";
            case OTHER: return "other";
            default: return "?";
          }
        }
    }

  public static class CareActivityCategoryEnumFactory implements EnumFactory<CareActivityCategory> {
    public CareActivityCategory fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("diet".equals(codeString))
          return CareActivityCategory.DIET;
        if ("drug".equals(codeString))
          return CareActivityCategory.DRUG;
        if ("encounter".equals(codeString))
          return CareActivityCategory.ENCOUNTER;
        if ("observation".equals(codeString))
          return CareActivityCategory.OBSERVATION;
        if ("procedure".equals(codeString))
          return CareActivityCategory.PROCEDURE;
        if ("supply".equals(codeString))
          return CareActivityCategory.SUPPLY;
        if ("other".equals(codeString))
          return CareActivityCategory.OTHER;
        throw new IllegalArgumentException("Unknown CareActivityCategory code '"+codeString+"'");
        }
    public String toCode(CareActivityCategory code) throws IllegalArgumentException {
      if (code == CareActivityCategory.DIET)
        return "diet";
      if (code == CareActivityCategory.DRUG)
        return "drug";
      if (code == CareActivityCategory.ENCOUNTER)
        return "encounter";
      if (code == CareActivityCategory.OBSERVATION)
        return "observation";
      if (code == CareActivityCategory.PROCEDURE)
        return "procedure";
      if (code == CareActivityCategory.SUPPLY)
        return "supply";
      if (code == CareActivityCategory.OTHER)
        return "other";
      return "?";
      }
    }

    @Block()
    public static class CareActivitySimpleComponent extends BackboneElement {
        /**
         * High-level categorization of the type of activity in a care plan.
         */
        @Child(name="category", type={CodeType.class}, order=1, min=1, max=1)
        @Description(shortDefinition="diet | drug | encounter | observation | procedure | supply | other", formalDefinition="High-level categorization of the type of activity in a care plan." )
        protected Enumeration<CareActivityCategory> category;

        /**
         * Detailed description of the type of activity.  E.g. What lab test, what procedure, what kind of encounter.
         */
        @Child(name="code", type={CodeableConcept.class}, order=2, min=0, max=1)
        @Description(shortDefinition="Detail type of activity", formalDefinition="Detailed description of the type of activity.  E.g. What lab test, what procedure, what kind of encounter." )
        protected CodeableConcept code;

        /**
         * The period, timing or frequency upon which the described activity is to occur.
         */
        @Child(name="scheduled", type={Timing.class, Period.class, StringType.class}, order=3, min=0, max=1)
        @Description(shortDefinition="When activity is to occur", formalDefinition="The period, timing or frequency upon which the described activity is to occur." )
        protected Type scheduled;

        /**
         * Identifies the facility where the activity will occur.  E.g. home, hospital, specific clinic, etc.
         */
        @Child(name="location", type={Location.class}, order=4, min=0, max=1)
        @Description(shortDefinition="Where it should happen", formalDefinition="Identifies the facility where the activity will occur.  E.g. home, hospital, specific clinic, etc." )
        protected Reference location;

        /**
         * The actual object that is the target of the reference (Identifies the facility where the activity will occur.  E.g. home, hospital, specific clinic, etc.)
         */
        protected Location locationTarget;

        /**
         * Identifies who's expected to be involved in the activity.
         */
        @Child(name="performer", type={Practitioner.class, Organization.class, RelatedPerson.class, Patient.class}, order=5, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="Who's responsible?", formalDefinition="Identifies who's expected to be involved in the activity." )
        protected List<Reference> performer;
        /**
         * The actual objects that are the target of the reference (Identifies who's expected to be involved in the activity.)
         */
        protected List<Resource> performerTarget;


        /**
         * Identifies the food, drug or other product being consumed or supplied in the activity.
         */
        @Child(name="product", type={Medication.class, Substance.class}, order=6, min=0, max=1)
        @Description(shortDefinition="What's administered/supplied", formalDefinition="Identifies the food, drug or other product being consumed or supplied in the activity." )
        protected Reference product;

        /**
         * The actual object that is the target of the reference (Identifies the food, drug or other product being consumed or supplied in the activity.)
         */
        protected Resource productTarget;

        /**
         * Identifies the quantity expected to be consumed in a given day.
         */
        @Child(name="dailyAmount", type={Quantity.class}, order=7, min=0, max=1)
        @Description(shortDefinition="How much consumed/day?", formalDefinition="Identifies the quantity expected to be consumed in a given day." )
        protected Quantity dailyAmount;

        /**
         * Identifies the quantity expected to be supplied.
         */
        @Child(name="quantity", type={Quantity.class}, order=8, min=0, max=1)
        @Description(shortDefinition="How much is administered/supplied/consumed", formalDefinition="Identifies the quantity expected to be supplied." )
        protected Quantity quantity;

        /**
         * This provides a textual description of constraints on the activity occurrence, including relation to other activities.  It may also include objectives, pre-conditions and end-conditions.  Finally, it may convey specifics about the activity such as body site, method, route, etc.
         */
        @Child(name="details", type={StringType.class}, order=9, min=0, max=1)
        @Description(shortDefinition="Extra info on activity occurrence", formalDefinition="This provides a textual description of constraints on the activity occurrence, including relation to other activities.  It may also include objectives, pre-conditions and end-conditions.  Finally, it may convey specifics about the activity such as body site, method, route, etc." )
        protected StringType details;

        private static final long serialVersionUID = -17133504L;

      public CareActivitySimpleComponent() {
        super();
      }

      public CareActivitySimpleComponent(Enumeration<CareActivityCategory> category) {
        super();
        this.category = category;
      }

        /**
         * @return {@link #category} (High-level categorization of the type of activity in a care plan.). This is the underlying object with id, value and extensions. The accessor "getCategory" gives direct access to the value
         */
        public Enumeration<CareActivityCategory> getCategoryElement() { 
          if (this.category == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CareActivitySimpleComponent.category");
            else if (Configuration.doAutoCreate())
              this.category = new Enumeration<CareActivityCategory>();
          return this.category;
        }

        public boolean hasCategoryElement() { 
          return this.category != null && !this.category.isEmpty();
        }

        public boolean hasCategory() { 
          return this.category != null && !this.category.isEmpty();
        }

        /**
         * @param value {@link #category} (High-level categorization of the type of activity in a care plan.). This is the underlying object with id, value and extensions. The accessor "getCategory" gives direct access to the value
         */
        public CareActivitySimpleComponent setCategoryElement(Enumeration<CareActivityCategory> value) { 
          this.category = value;
          return this;
        }

        /**
         * @return High-level categorization of the type of activity in a care plan.
         */
        public CareActivityCategory getCategory() { 
          return this.category == null ? null : this.category.getValue();
        }

        /**
         * @param value High-level categorization of the type of activity in a care plan.
         */
        public CareActivitySimpleComponent setCategory(CareActivityCategory value) { 
            if (this.category == null)
              this.category = new Enumeration<CareActivityCategory>(CareActivityCategory.ENUM_FACTORY);
            this.category.setValue(value);
          return this;
        }

        /**
         * @return {@link #code} (Detailed description of the type of activity.  E.g. What lab test, what procedure, what kind of encounter.)
         */
        public CodeableConcept getCode() { 
          if (this.code == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CareActivitySimpleComponent.code");
            else if (Configuration.doAutoCreate())
              this.code = new CodeableConcept();
          return this.code;
        }

        public boolean hasCode() { 
          return this.code != null && !this.code.isEmpty();
        }

        /**
         * @param value {@link #code} (Detailed description of the type of activity.  E.g. What lab test, what procedure, what kind of encounter.)
         */
        public CareActivitySimpleComponent setCode(CodeableConcept value) { 
          this.code = value;
          return this;
        }

        /**
         * @return {@link #scheduled} (The period, timing or frequency upon which the described activity is to occur.)
         */
        public Type getScheduled() { 
          return this.scheduled;
        }

        /**
         * @return {@link #scheduled} (The period, timing or frequency upon which the described activity is to occur.)
         */
        public Timing getScheduledTiming() throws Exception { 
          if (!(this.scheduled instanceof Timing))
            throw new Exception("Type mismatch: the type Timing was expected, but "+this.scheduled.getClass().getName()+" was encountered");
          return (Timing) this.scheduled;
        }

        /**
         * @return {@link #scheduled} (The period, timing or frequency upon which the described activity is to occur.)
         */
        public Period getScheduledPeriod() throws Exception { 
          if (!(this.scheduled instanceof Period))
            throw new Exception("Type mismatch: the type Period was expected, but "+this.scheduled.getClass().getName()+" was encountered");
          return (Period) this.scheduled;
        }

        /**
         * @return {@link #scheduled} (The period, timing or frequency upon which the described activity is to occur.)
         */
        public StringType getScheduledStringType() throws Exception { 
          if (!(this.scheduled instanceof StringType))
            throw new Exception("Type mismatch: the type StringType was expected, but "+this.scheduled.getClass().getName()+" was encountered");
          return (StringType) this.scheduled;
        }

        public boolean hasScheduled() { 
          return this.scheduled != null && !this.scheduled.isEmpty();
        }

        /**
         * @param value {@link #scheduled} (The period, timing or frequency upon which the described activity is to occur.)
         */
        public CareActivitySimpleComponent setScheduled(Type value) { 
          this.scheduled = value;
          return this;
        }

        /**
         * @return {@link #location} (Identifies the facility where the activity will occur.  E.g. home, hospital, specific clinic, etc.)
         */
        public Reference getLocation() { 
          if (this.location == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CareActivitySimpleComponent.location");
            else if (Configuration.doAutoCreate())
              this.location = new Reference();
          return this.location;
        }

        public boolean hasLocation() { 
          return this.location != null && !this.location.isEmpty();
        }

        /**
         * @param value {@link #location} (Identifies the facility where the activity will occur.  E.g. home, hospital, specific clinic, etc.)
         */
        public CareActivitySimpleComponent setLocation(Reference value) { 
          this.location = value;
          return this;
        }

        /**
         * @return {@link #location} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Identifies the facility where the activity will occur.  E.g. home, hospital, specific clinic, etc.)
         */
        public Location getLocationTarget() { 
          if (this.locationTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CareActivitySimpleComponent.location");
            else if (Configuration.doAutoCreate())
              this.locationTarget = new Location();
          return this.locationTarget;
        }

        /**
         * @param value {@link #location} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Identifies the facility where the activity will occur.  E.g. home, hospital, specific clinic, etc.)
         */
        public CareActivitySimpleComponent setLocationTarget(Location value) { 
          this.locationTarget = value;
          return this;
        }

        /**
         * @return {@link #performer} (Identifies who's expected to be involved in the activity.)
         */
        public List<Reference> getPerformer() { 
          if (this.performer == null)
            this.performer = new ArrayList<Reference>();
          return this.performer;
        }

        public boolean hasPerformer() { 
          if (this.performer == null)
            return false;
          for (Reference item : this.performer)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #performer} (Identifies who's expected to be involved in the activity.)
         */
    // syntactic sugar
        public Reference addPerformer() { //3
          Reference t = new Reference();
          if (this.performer == null)
            this.performer = new ArrayList<Reference>();
          this.performer.add(t);
          return t;
        }

        /**
         * @return {@link #performer} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. Identifies who's expected to be involved in the activity.)
         */
        public List<Resource> getPerformerTarget() { 
          if (this.performerTarget == null)
            this.performerTarget = new ArrayList<Resource>();
          return this.performerTarget;
        }

        /**
         * @return {@link #product} (Identifies the food, drug or other product being consumed or supplied in the activity.)
         */
        public Reference getProduct() { 
          if (this.product == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CareActivitySimpleComponent.product");
            else if (Configuration.doAutoCreate())
              this.product = new Reference();
          return this.product;
        }

        public boolean hasProduct() { 
          return this.product != null && !this.product.isEmpty();
        }

        /**
         * @param value {@link #product} (Identifies the food, drug or other product being consumed or supplied in the activity.)
         */
        public CareActivitySimpleComponent setProduct(Reference value) { 
          this.product = value;
          return this;
        }

        /**
         * @return {@link #product} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Identifies the food, drug or other product being consumed or supplied in the activity.)
         */
        public Resource getProductTarget() { 
          return this.productTarget;
        }

        /**
         * @param value {@link #product} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Identifies the food, drug or other product being consumed or supplied in the activity.)
         */
        public CareActivitySimpleComponent setProductTarget(Resource value) { 
          this.productTarget = value;
          return this;
        }

        /**
         * @return {@link #dailyAmount} (Identifies the quantity expected to be consumed in a given day.)
         */
        public Quantity getDailyAmount() { 
          if (this.dailyAmount == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CareActivitySimpleComponent.dailyAmount");
            else if (Configuration.doAutoCreate())
              this.dailyAmount = new Quantity();
          return this.dailyAmount;
        }

        public boolean hasDailyAmount() { 
          return this.dailyAmount != null && !this.dailyAmount.isEmpty();
        }

        /**
         * @param value {@link #dailyAmount} (Identifies the quantity expected to be consumed in a given day.)
         */
        public CareActivitySimpleComponent setDailyAmount(Quantity value) { 
          this.dailyAmount = value;
          return this;
        }

        /**
         * @return {@link #quantity} (Identifies the quantity expected to be supplied.)
         */
        public Quantity getQuantity() { 
          if (this.quantity == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CareActivitySimpleComponent.quantity");
            else if (Configuration.doAutoCreate())
              this.quantity = new Quantity();
          return this.quantity;
        }

        public boolean hasQuantity() { 
          return this.quantity != null && !this.quantity.isEmpty();
        }

        /**
         * @param value {@link #quantity} (Identifies the quantity expected to be supplied.)
         */
        public CareActivitySimpleComponent setQuantity(Quantity value) { 
          this.quantity = value;
          return this;
        }

        /**
         * @return {@link #details} (This provides a textual description of constraints on the activity occurrence, including relation to other activities.  It may also include objectives, pre-conditions and end-conditions.  Finally, it may convey specifics about the activity such as body site, method, route, etc.). This is the underlying object with id, value and extensions. The accessor "getDetails" gives direct access to the value
         */
        public StringType getDetailsElement() { 
          if (this.details == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CareActivitySimpleComponent.details");
            else if (Configuration.doAutoCreate())
              this.details = new StringType();
          return this.details;
        }

        public boolean hasDetailsElement() { 
          return this.details != null && !this.details.isEmpty();
        }

        public boolean hasDetails() { 
          return this.details != null && !this.details.isEmpty();
        }

        /**
         * @param value {@link #details} (This provides a textual description of constraints on the activity occurrence, including relation to other activities.  It may also include objectives, pre-conditions and end-conditions.  Finally, it may convey specifics about the activity such as body site, method, route, etc.). This is the underlying object with id, value and extensions. The accessor "getDetails" gives direct access to the value
         */
        public CareActivitySimpleComponent setDetailsElement(StringType value) { 
          this.details = value;
          return this;
        }

        /**
         * @return This provides a textual description of constraints on the activity occurrence, including relation to other activities.  It may also include objectives, pre-conditions and end-conditions.  Finally, it may convey specifics about the activity such as body site, method, route, etc.
         */
        public String getDetails() { 
          return this.details == null ? null : this.details.getValue();
        }

        /**
         * @param value This provides a textual description of constraints on the activity occurrence, including relation to other activities.  It may also include objectives, pre-conditions and end-conditions.  Finally, it may convey specifics about the activity such as body site, method, route, etc.
         */
        public CareActivitySimpleComponent setDetails(String value) { 
          if (Utilities.noString(value))
            this.details = null;
          else {
            if (this.details == null)
              this.details = new StringType();
            this.details.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("category", "code", "High-level categorization of the type of activity in a care plan.", 0, java.lang.Integer.MAX_VALUE, category));
          childrenList.add(new Property("code", "CodeableConcept", "Detailed description of the type of activity.  E.g. What lab test, what procedure, what kind of encounter.", 0, java.lang.Integer.MAX_VALUE, code));
          childrenList.add(new Property("scheduled[x]", "Timing|Period|string", "The period, timing or frequency upon which the described activity is to occur.", 0, java.lang.Integer.MAX_VALUE, scheduled));
          childrenList.add(new Property("location", "Reference(Location)", "Identifies the facility where the activity will occur.  E.g. home, hospital, specific clinic, etc.", 0, java.lang.Integer.MAX_VALUE, location));
          childrenList.add(new Property("performer", "Reference(Practitioner|Organization|RelatedPerson|Patient)", "Identifies who's expected to be involved in the activity.", 0, java.lang.Integer.MAX_VALUE, performer));
          childrenList.add(new Property("product", "Reference(Medication|Substance)", "Identifies the food, drug or other product being consumed or supplied in the activity.", 0, java.lang.Integer.MAX_VALUE, product));
          childrenList.add(new Property("dailyAmount", "Quantity", "Identifies the quantity expected to be consumed in a given day.", 0, java.lang.Integer.MAX_VALUE, dailyAmount));
          childrenList.add(new Property("quantity", "Quantity", "Identifies the quantity expected to be supplied.", 0, java.lang.Integer.MAX_VALUE, quantity));
          childrenList.add(new Property("details", "string", "This provides a textual description of constraints on the activity occurrence, including relation to other activities.  It may also include objectives, pre-conditions and end-conditions.  Finally, it may convey specifics about the activity such as body site, method, route, etc.", 0, java.lang.Integer.MAX_VALUE, details));
        }

      public CareActivitySimpleComponent copy() {
        CareActivitySimpleComponent dst = new CareActivitySimpleComponent();
        copyValues(dst);
        dst.category = category == null ? null : category.copy();
        dst.code = code == null ? null : code.copy();
        dst.scheduled = scheduled == null ? null : scheduled.copy();
        dst.location = location == null ? null : location.copy();
        if (performer != null) {
          dst.performer = new ArrayList<Reference>();
          for (Reference i : performer)
            dst.performer.add(i.copy());
        };
        dst.product = product == null ? null : product.copy();
        dst.dailyAmount = dailyAmount == null ? null : dailyAmount.copy();
        dst.quantity = quantity == null ? null : quantity.copy();
        dst.details = details == null ? null : details.copy();
        return dst;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (category == null || category.isEmpty()) && (code == null || code.isEmpty())
           && (scheduled == null || scheduled.isEmpty()) && (location == null || location.isEmpty())
           && (performer == null || performer.isEmpty()) && (product == null || product.isEmpty()) && (dailyAmount == null || dailyAmount.isEmpty())
           && (quantity == null || quantity.isEmpty()) && (details == null || details.isEmpty());
      }

  }

    /**
     * This records identifiers associated with this care plan that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation).
     */
    @Child(name="identifier", type={Identifier.class}, order=-1, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="External Ids for this plan", formalDefinition="This records identifiers associated with this care plan that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)." )
    protected List<Identifier> identifier;

    /**
     * Identifies the patient/subject whose intended care is described by the plan.
     */
    @Child(name="patient", type={Patient.class}, order=0, min=0, max=1)
    @Description(shortDefinition="Who care plan is for", formalDefinition="Identifies the patient/subject whose intended care is described by the plan." )
    protected Reference patient;

    /**
     * The actual object that is the target of the reference (Identifies the patient/subject whose intended care is described by the plan.)
     */
    protected Patient patientTarget;

    /**
     * Internal reference that identifies the goals that this activity is intended to contribute towards meeting.
     */
    @Child(name="goal", type={Goal.class}, order=1, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Goals this activity relates to", formalDefinition="Internal reference that identifies the goals that this activity is intended to contribute towards meeting." )
    protected List<Reference> goal;
    /**
     * The actual objects that are the target of the reference (Internal reference that identifies the goals that this activity is intended to contribute towards meeting.)
     */
    protected List<Goal> goalTarget;


    /**
     * Identifies what progress is being made for the specific activity.
     */
    @Child(name="status", type={CodeType.class}, order=2, min=0, max=1)
    @Description(shortDefinition="not started | scheduled | in progress | on hold | completed | cancelled", formalDefinition="Identifies what progress is being made for the specific activity." )
    protected Enumeration<CareActivityStatus> status;

    /**
     * If true, indicates that the described activity is one that must NOT be engaged in when following the plan.
     */
    @Child(name="prohibited", type={BooleanType.class}, order=3, min=1, max=1)
    @Description(shortDefinition="Do NOT do", formalDefinition="If true, indicates that the described activity is one that must NOT be engaged in when following the plan." )
    protected BooleanType prohibited;

    /**
     * Resources that describe follow-on actions resulting from the plan, such as drug prescriptions, encounter records, appointments, etc.
     */
    @Child(name="actionResulting", type={}, order=4, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Appointments, orders, etc.", formalDefinition="Resources that describe follow-on actions resulting from the plan, such as drug prescriptions, encounter records, appointments, etc." )
    protected List<Reference> actionResulting;
    /**
     * The actual objects that are the target of the reference (Resources that describe follow-on actions resulting from the plan, such as drug prescriptions, encounter records, appointments, etc.)
     */
    protected List<Resource> actionResultingTarget;


    /**
     * Notes about the execution of the activity.
     */
    @Child(name="notes", type={StringType.class}, order=5, min=0, max=1)
    @Description(shortDefinition="Comments about the activity", formalDefinition="Notes about the execution of the activity." )
    protected StringType notes;

    /**
     * The details of the proposed activity represented in a specific resource.
     */
    @Child(name="detail", type={Procedure.class, MedicationPrescription.class, DiagnosticOrder.class, Encounter.class, Supply.class}, order=6, min=0, max=1)
    @Description(shortDefinition="Activity details defined in specific resource", formalDefinition="The details of the proposed activity represented in a specific resource." )
    protected Reference detail;

    /**
     * The actual object that is the target of the reference (The details of the proposed activity represented in a specific resource.)
     */
    protected Resource detailTarget;

    /**
     * A simple summary of details suitable for a general care plan system (e.g. form driven) that doesn't know about specific resources such as procedure etc.
     */
    @Child(name="simple", type={}, order=7, min=0, max=1)
    @Description(shortDefinition="Activity details summarised here", formalDefinition="A simple summary of details suitable for a general care plan system (e.g. form driven) that doesn't know about specific resources such as procedure etc." )
    protected CareActivitySimpleComponent simple;

    private static final long serialVersionUID = 1711521518L;

    public CareActivity() {
      super();
    }

    public CareActivity(BooleanType prohibited) {
      super();
      this.prohibited = prohibited;
    }

    /**
     * @return {@link #identifier} (This records identifiers associated with this care plan that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation).)
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
     * @return {@link #identifier} (This records identifiers associated with this care plan that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation).)
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
     * @return {@link #patient} (Identifies the patient/subject whose intended care is described by the plan.)
     */
    public Reference getPatient() { 
      if (this.patient == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CareActivity.patient");
        else if (Configuration.doAutoCreate())
          this.patient = new Reference();
      return this.patient;
    }

    public boolean hasPatient() { 
      return this.patient != null && !this.patient.isEmpty();
    }

    /**
     * @param value {@link #patient} (Identifies the patient/subject whose intended care is described by the plan.)
     */
    public CareActivity setPatient(Reference value) { 
      this.patient = value;
      return this;
    }

    /**
     * @return {@link #patient} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Identifies the patient/subject whose intended care is described by the plan.)
     */
    public Patient getPatientTarget() { 
      if (this.patientTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CareActivity.patient");
        else if (Configuration.doAutoCreate())
          this.patientTarget = new Patient();
      return this.patientTarget;
    }

    /**
     * @param value {@link #patient} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Identifies the patient/subject whose intended care is described by the plan.)
     */
    public CareActivity setPatientTarget(Patient value) { 
      this.patientTarget = value;
      return this;
    }

    /**
     * @return {@link #goal} (Internal reference that identifies the goals that this activity is intended to contribute towards meeting.)
     */
    public List<Reference> getGoal() { 
      if (this.goal == null)
        this.goal = new ArrayList<Reference>();
      return this.goal;
    }

    public boolean hasGoal() { 
      if (this.goal == null)
        return false;
      for (Reference item : this.goal)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #goal} (Internal reference that identifies the goals that this activity is intended to contribute towards meeting.)
     */
    // syntactic sugar
    public Reference addGoal() { //3
      Reference t = new Reference();
      if (this.goal == null)
        this.goal = new ArrayList<Reference>();
      this.goal.add(t);
      return t;
    }

    /**
     * @return {@link #goal} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. Internal reference that identifies the goals that this activity is intended to contribute towards meeting.)
     */
    public List<Goal> getGoalTarget() { 
      if (this.goalTarget == null)
        this.goalTarget = new ArrayList<Goal>();
      return this.goalTarget;
    }

    // syntactic sugar
    /**
     * @return {@link #goal} (Add an actual object that is the target of the reference. The reference library doesn't use these, but you can use this to hold the resources if you resolvethemt. Internal reference that identifies the goals that this activity is intended to contribute towards meeting.)
     */
    public Goal addGoalTarget() { 
      Goal r = new Goal();
      if (this.goalTarget == null)
        this.goalTarget = new ArrayList<Goal>();
      this.goalTarget.add(r);
      return r;
    }

    /**
     * @return {@link #status} (Identifies what progress is being made for the specific activity.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<CareActivityStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CareActivity.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<CareActivityStatus>();
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (Identifies what progress is being made for the specific activity.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public CareActivity setStatusElement(Enumeration<CareActivityStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return Identifies what progress is being made for the specific activity.
     */
    public CareActivityStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value Identifies what progress is being made for the specific activity.
     */
    public CareActivity setStatus(CareActivityStatus value) { 
      if (value == null)
        this.status = null;
      else {
        if (this.status == null)
          this.status = new Enumeration<CareActivityStatus>(CareActivityStatus.ENUM_FACTORY);
        this.status.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #prohibited} (If true, indicates that the described activity is one that must NOT be engaged in when following the plan.). This is the underlying object with id, value and extensions. The accessor "getProhibited" gives direct access to the value
     */
    public BooleanType getProhibitedElement() { 
      if (this.prohibited == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CareActivity.prohibited");
        else if (Configuration.doAutoCreate())
          this.prohibited = new BooleanType();
      return this.prohibited;
    }

    public boolean hasProhibitedElement() { 
      return this.prohibited != null && !this.prohibited.isEmpty();
    }

    public boolean hasProhibited() { 
      return this.prohibited != null && !this.prohibited.isEmpty();
    }

    /**
     * @param value {@link #prohibited} (If true, indicates that the described activity is one that must NOT be engaged in when following the plan.). This is the underlying object with id, value and extensions. The accessor "getProhibited" gives direct access to the value
     */
    public CareActivity setProhibitedElement(BooleanType value) { 
      this.prohibited = value;
      return this;
    }

    /**
     * @return If true, indicates that the described activity is one that must NOT be engaged in when following the plan.
     */
    public boolean getProhibited() { 
      return this.prohibited == null ? false : this.prohibited.getValue();
    }

    /**
     * @param value If true, indicates that the described activity is one that must NOT be engaged in when following the plan.
     */
    public CareActivity setProhibited(boolean value) { 
        if (this.prohibited == null)
          this.prohibited = new BooleanType();
        this.prohibited.setValue(value);
      return this;
    }

    /**
     * @return {@link #actionResulting} (Resources that describe follow-on actions resulting from the plan, such as drug prescriptions, encounter records, appointments, etc.)
     */
    public List<Reference> getActionResulting() { 
      if (this.actionResulting == null)
        this.actionResulting = new ArrayList<Reference>();
      return this.actionResulting;
    }

    public boolean hasActionResulting() { 
      if (this.actionResulting == null)
        return false;
      for (Reference item : this.actionResulting)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #actionResulting} (Resources that describe follow-on actions resulting from the plan, such as drug prescriptions, encounter records, appointments, etc.)
     */
    // syntactic sugar
    public Reference addActionResulting() { //3
      Reference t = new Reference();
      if (this.actionResulting == null)
        this.actionResulting = new ArrayList<Reference>();
      this.actionResulting.add(t);
      return t;
    }

    /**
     * @return {@link #actionResulting} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. Resources that describe follow-on actions resulting from the plan, such as drug prescriptions, encounter records, appointments, etc.)
     */
    public List<Resource> getActionResultingTarget() { 
      if (this.actionResultingTarget == null)
        this.actionResultingTarget = new ArrayList<Resource>();
      return this.actionResultingTarget;
    }

    /**
     * @return {@link #notes} (Notes about the execution of the activity.). This is the underlying object with id, value and extensions. The accessor "getNotes" gives direct access to the value
     */
    public StringType getNotesElement() { 
      if (this.notes == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CareActivity.notes");
        else if (Configuration.doAutoCreate())
          this.notes = new StringType();
      return this.notes;
    }

    public boolean hasNotesElement() { 
      return this.notes != null && !this.notes.isEmpty();
    }

    public boolean hasNotes() { 
      return this.notes != null && !this.notes.isEmpty();
    }

    /**
     * @param value {@link #notes} (Notes about the execution of the activity.). This is the underlying object with id, value and extensions. The accessor "getNotes" gives direct access to the value
     */
    public CareActivity setNotesElement(StringType value) { 
      this.notes = value;
      return this;
    }

    /**
     * @return Notes about the execution of the activity.
     */
    public String getNotes() { 
      return this.notes == null ? null : this.notes.getValue();
    }

    /**
     * @param value Notes about the execution of the activity.
     */
    public CareActivity setNotes(String value) { 
      if (Utilities.noString(value))
        this.notes = null;
      else {
        if (this.notes == null)
          this.notes = new StringType();
        this.notes.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #detail} (The details of the proposed activity represented in a specific resource.)
     */
    public Reference getDetail() { 
      if (this.detail == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CareActivity.detail");
        else if (Configuration.doAutoCreate())
          this.detail = new Reference();
      return this.detail;
    }

    public boolean hasDetail() { 
      return this.detail != null && !this.detail.isEmpty();
    }

    /**
     * @param value {@link #detail} (The details of the proposed activity represented in a specific resource.)
     */
    public CareActivity setDetail(Reference value) { 
      this.detail = value;
      return this;
    }

    /**
     * @return {@link #detail} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The details of the proposed activity represented in a specific resource.)
     */
    public Resource getDetailTarget() { 
      return this.detailTarget;
    }

    /**
     * @param value {@link #detail} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The details of the proposed activity represented in a specific resource.)
     */
    public CareActivity setDetailTarget(Resource value) { 
      this.detailTarget = value;
      return this;
    }

    /**
     * @return {@link #simple} (A simple summary of details suitable for a general care plan system (e.g. form driven) that doesn't know about specific resources such as procedure etc.)
     */
    public CareActivitySimpleComponent getSimple() { 
      if (this.simple == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CareActivity.simple");
        else if (Configuration.doAutoCreate())
          this.simple = new CareActivitySimpleComponent();
      return this.simple;
    }

    public boolean hasSimple() { 
      return this.simple != null && !this.simple.isEmpty();
    }

    /**
     * @param value {@link #simple} (A simple summary of details suitable for a general care plan system (e.g. form driven) that doesn't know about specific resources such as procedure etc.)
     */
    public CareActivity setSimple(CareActivitySimpleComponent value) { 
      this.simple = value;
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "This records identifiers associated with this care plan that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation).", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("patient", "Reference(Patient)", "Identifies the patient/subject whose intended care is described by the plan.", 0, java.lang.Integer.MAX_VALUE, patient));
        childrenList.add(new Property("goal", "Reference(Goal)", "Internal reference that identifies the goals that this activity is intended to contribute towards meeting.", 0, java.lang.Integer.MAX_VALUE, goal));
        childrenList.add(new Property("status", "code", "Identifies what progress is being made for the specific activity.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("prohibited", "boolean", "If true, indicates that the described activity is one that must NOT be engaged in when following the plan.", 0, java.lang.Integer.MAX_VALUE, prohibited));
        childrenList.add(new Property("actionResulting", "Reference(Any)", "Resources that describe follow-on actions resulting from the plan, such as drug prescriptions, encounter records, appointments, etc.", 0, java.lang.Integer.MAX_VALUE, actionResulting));
        childrenList.add(new Property("notes", "string", "Notes about the execution of the activity.", 0, java.lang.Integer.MAX_VALUE, notes));
        childrenList.add(new Property("detail", "Reference(Procedure|MedicationPrescription|DiagnosticOrder|Encounter|Supply)", "The details of the proposed activity represented in a specific resource.", 0, java.lang.Integer.MAX_VALUE, detail));
        childrenList.add(new Property("simple", "", "A simple summary of details suitable for a general care plan system (e.g. form driven) that doesn't know about specific resources such as procedure etc.", 0, java.lang.Integer.MAX_VALUE, simple));
      }

      public CareActivity copy() {
        CareActivity dst = new CareActivity();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.patient = patient == null ? null : patient.copy();
        if (goal != null) {
          dst.goal = new ArrayList<Reference>();
          for (Reference i : goal)
            dst.goal.add(i.copy());
        };
        dst.status = status == null ? null : status.copy();
        dst.prohibited = prohibited == null ? null : prohibited.copy();
        if (actionResulting != null) {
          dst.actionResulting = new ArrayList<Reference>();
          for (Reference i : actionResulting)
            dst.actionResulting.add(i.copy());
        };
        dst.notes = notes == null ? null : notes.copy();
        dst.detail = detail == null ? null : detail.copy();
        dst.simple = simple == null ? null : simple.copy();
        return dst;
      }

      protected CareActivity typedCopy() {
        return copy();
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (patient == null || patient.isEmpty())
           && (goal == null || goal.isEmpty()) && (status == null || status.isEmpty()) && (prohibited == null || prohibited.isEmpty())
           && (actionResulting == null || actionResulting.isEmpty()) && (notes == null || notes.isEmpty())
           && (detail == null || detail.isEmpty()) && (simple == null || simple.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.CareActivity;
   }

  @SearchParamDefinition(name="activitycode", path="CareActivity.simple.code", description="Detail type of activity", type="token" )
  public static final String SP_ACTIVITYCODE = "activitycode";
  @SearchParamDefinition(name="patient", path="CareActivity.patient", description="Who care plan is for", type="reference" )
  public static final String SP_PATIENT = "patient";
  @SearchParamDefinition(name="activitydetail", path="CareActivity.detail", description="Activity details defined in specific resource", type="reference" )
  public static final String SP_ACTIVITYDETAIL = "activitydetail";
  @SearchParamDefinition(name="activitydate", path="CareActivity.simple.scheduled[x]", description="Specified date occurs within period specified by CarePlan.activity.timingSchedule", type="date" )
  public static final String SP_ACTIVITYDATE = "activitydate";

}

