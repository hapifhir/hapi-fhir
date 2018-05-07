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
 * Describes the intention of how one or more practitioners intend to deliver care for a particular patient, group or community for a period of time, possibly limited to care for a specific condition or set of conditions.
 */
@ResourceDef(name="CarePlan", profile="http://hl7.org/fhir/Profile/CarePlan")
public class CarePlan extends DomainResource {

    public enum CarePlanStatus {
        /**
         * The plan is in development or awaiting use but is not yet intended to be acted upon.
         */
        DRAFT, 
        /**
         * The plan is intended to be followed and used as part of patient care.
         */
        ACTIVE, 
        /**
         * The plan has been temporarily stopped but is expected to resume in the future.
         */
        SUSPENDED, 
        /**
         * The plan is no longer in use and is not expected to be followed or used in patient care.
         */
        COMPLETED, 
        /**
         * The plan was entered in error and voided.
         */
        ENTEREDINERROR, 
        /**
         * The plan has been terminated prior to reaching completion (though it may have been replaced by a new plan).
         */
        CANCELLED, 
        /**
         * The authoring system doesn't know the current state of the care plan.
         */
        UNKNOWN, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static CarePlanStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("draft".equals(codeString))
          return DRAFT;
        if ("active".equals(codeString))
          return ACTIVE;
        if ("suspended".equals(codeString))
          return SUSPENDED;
        if ("completed".equals(codeString))
          return COMPLETED;
        if ("entered-in-error".equals(codeString))
          return ENTEREDINERROR;
        if ("cancelled".equals(codeString))
          return CANCELLED;
        if ("unknown".equals(codeString))
          return UNKNOWN;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown CarePlanStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case DRAFT: return "draft";
            case ACTIVE: return "active";
            case SUSPENDED: return "suspended";
            case COMPLETED: return "completed";
            case ENTEREDINERROR: return "entered-in-error";
            case CANCELLED: return "cancelled";
            case UNKNOWN: return "unknown";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case DRAFT: return "http://hl7.org/fhir/care-plan-status";
            case ACTIVE: return "http://hl7.org/fhir/care-plan-status";
            case SUSPENDED: return "http://hl7.org/fhir/care-plan-status";
            case COMPLETED: return "http://hl7.org/fhir/care-plan-status";
            case ENTEREDINERROR: return "http://hl7.org/fhir/care-plan-status";
            case CANCELLED: return "http://hl7.org/fhir/care-plan-status";
            case UNKNOWN: return "http://hl7.org/fhir/care-plan-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case DRAFT: return "The plan is in development or awaiting use but is not yet intended to be acted upon.";
            case ACTIVE: return "The plan is intended to be followed and used as part of patient care.";
            case SUSPENDED: return "The plan has been temporarily stopped but is expected to resume in the future.";
            case COMPLETED: return "The plan is no longer in use and is not expected to be followed or used in patient care.";
            case ENTEREDINERROR: return "The plan was entered in error and voided.";
            case CANCELLED: return "The plan has been terminated prior to reaching completion (though it may have been replaced by a new plan).";
            case UNKNOWN: return "The authoring system doesn't know the current state of the care plan.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case DRAFT: return "Pending";
            case ACTIVE: return "Active";
            case SUSPENDED: return "Suspended";
            case COMPLETED: return "Completed";
            case ENTEREDINERROR: return "Entered In Error";
            case CANCELLED: return "Cancelled";
            case UNKNOWN: return "Unknown";
            default: return "?";
          }
        }
    }

  public static class CarePlanStatusEnumFactory implements EnumFactory<CarePlanStatus> {
    public CarePlanStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("draft".equals(codeString))
          return CarePlanStatus.DRAFT;
        if ("active".equals(codeString))
          return CarePlanStatus.ACTIVE;
        if ("suspended".equals(codeString))
          return CarePlanStatus.SUSPENDED;
        if ("completed".equals(codeString))
          return CarePlanStatus.COMPLETED;
        if ("entered-in-error".equals(codeString))
          return CarePlanStatus.ENTEREDINERROR;
        if ("cancelled".equals(codeString))
          return CarePlanStatus.CANCELLED;
        if ("unknown".equals(codeString))
          return CarePlanStatus.UNKNOWN;
        throw new IllegalArgumentException("Unknown CarePlanStatus code '"+codeString+"'");
        }
        public Enumeration<CarePlanStatus> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<CarePlanStatus>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("draft".equals(codeString))
          return new Enumeration<CarePlanStatus>(this, CarePlanStatus.DRAFT);
        if ("active".equals(codeString))
          return new Enumeration<CarePlanStatus>(this, CarePlanStatus.ACTIVE);
        if ("suspended".equals(codeString))
          return new Enumeration<CarePlanStatus>(this, CarePlanStatus.SUSPENDED);
        if ("completed".equals(codeString))
          return new Enumeration<CarePlanStatus>(this, CarePlanStatus.COMPLETED);
        if ("entered-in-error".equals(codeString))
          return new Enumeration<CarePlanStatus>(this, CarePlanStatus.ENTEREDINERROR);
        if ("cancelled".equals(codeString))
          return new Enumeration<CarePlanStatus>(this, CarePlanStatus.CANCELLED);
        if ("unknown".equals(codeString))
          return new Enumeration<CarePlanStatus>(this, CarePlanStatus.UNKNOWN);
        throw new FHIRException("Unknown CarePlanStatus code '"+codeString+"'");
        }
    public String toCode(CarePlanStatus code) {
      if (code == CarePlanStatus.DRAFT)
        return "draft";
      if (code == CarePlanStatus.ACTIVE)
        return "active";
      if (code == CarePlanStatus.SUSPENDED)
        return "suspended";
      if (code == CarePlanStatus.COMPLETED)
        return "completed";
      if (code == CarePlanStatus.ENTEREDINERROR)
        return "entered-in-error";
      if (code == CarePlanStatus.CANCELLED)
        return "cancelled";
      if (code == CarePlanStatus.UNKNOWN)
        return "unknown";
      return "?";
      }
    public String toSystem(CarePlanStatus code) {
      return code.getSystem();
      }
    }

    public enum CarePlanIntent {
        /**
         * The care plan is a suggestion made by someone/something that doesn't have an intention to ensure it occurs and without providing an authorization to act
         */
        PROPOSAL, 
        /**
         * The care plan represents an intention to ensure something occurs without providing an authorization for others to act
         */
        PLAN, 
        /**
         * The care plan represents a request/demand and authorization for action
         */
        ORDER, 
        /**
         * The care plan represents a component or option for a RequestGroup that establishes timing, conditionality and/or other constraints among a set of requests.

Refer to [[[RequestGroup]]] for additional information on how this status is used
         */
        OPTION, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static CarePlanIntent fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("proposal".equals(codeString))
          return PROPOSAL;
        if ("plan".equals(codeString))
          return PLAN;
        if ("order".equals(codeString))
          return ORDER;
        if ("option".equals(codeString))
          return OPTION;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown CarePlanIntent code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PROPOSAL: return "proposal";
            case PLAN: return "plan";
            case ORDER: return "order";
            case OPTION: return "option";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case PROPOSAL: return "http://hl7.org/fhir/care-plan-intent";
            case PLAN: return "http://hl7.org/fhir/care-plan-intent";
            case ORDER: return "http://hl7.org/fhir/care-plan-intent";
            case OPTION: return "http://hl7.org/fhir/care-plan-intent";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case PROPOSAL: return "The care plan is a suggestion made by someone/something that doesn't have an intention to ensure it occurs and without providing an authorization to act";
            case PLAN: return "The care plan represents an intention to ensure something occurs without providing an authorization for others to act";
            case ORDER: return "The care plan represents a request/demand and authorization for action";
            case OPTION: return "The care plan represents a component or option for a RequestGroup that establishes timing, conditionality and/or other constraints among a set of requests.\n\nRefer to [[[RequestGroup]]] for additional information on how this status is used";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PROPOSAL: return "Proposal";
            case PLAN: return "Plan";
            case ORDER: return "Order";
            case OPTION: return "Option";
            default: return "?";
          }
        }
    }

  public static class CarePlanIntentEnumFactory implements EnumFactory<CarePlanIntent> {
    public CarePlanIntent fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("proposal".equals(codeString))
          return CarePlanIntent.PROPOSAL;
        if ("plan".equals(codeString))
          return CarePlanIntent.PLAN;
        if ("order".equals(codeString))
          return CarePlanIntent.ORDER;
        if ("option".equals(codeString))
          return CarePlanIntent.OPTION;
        throw new IllegalArgumentException("Unknown CarePlanIntent code '"+codeString+"'");
        }
        public Enumeration<CarePlanIntent> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<CarePlanIntent>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("proposal".equals(codeString))
          return new Enumeration<CarePlanIntent>(this, CarePlanIntent.PROPOSAL);
        if ("plan".equals(codeString))
          return new Enumeration<CarePlanIntent>(this, CarePlanIntent.PLAN);
        if ("order".equals(codeString))
          return new Enumeration<CarePlanIntent>(this, CarePlanIntent.ORDER);
        if ("option".equals(codeString))
          return new Enumeration<CarePlanIntent>(this, CarePlanIntent.OPTION);
        throw new FHIRException("Unknown CarePlanIntent code '"+codeString+"'");
        }
    public String toCode(CarePlanIntent code) {
      if (code == CarePlanIntent.PROPOSAL)
        return "proposal";
      if (code == CarePlanIntent.PLAN)
        return "plan";
      if (code == CarePlanIntent.ORDER)
        return "order";
      if (code == CarePlanIntent.OPTION)
        return "option";
      return "?";
      }
    public String toSystem(CarePlanIntent code) {
      return code.getSystem();
      }
    }

    public enum CarePlanActivityStatus {
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
         * The authoring system doesn't know the current state of the activity.
         */
        UNKNOWN, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static CarePlanActivityStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("not-started".equals(codeString))
          return NOTSTARTED;
        if ("scheduled".equals(codeString))
          return SCHEDULED;
        if ("in-progress".equals(codeString))
          return INPROGRESS;
        if ("on-hold".equals(codeString))
          return ONHOLD;
        if ("completed".equals(codeString))
          return COMPLETED;
        if ("cancelled".equals(codeString))
          return CANCELLED;
        if ("unknown".equals(codeString))
          return UNKNOWN;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown CarePlanActivityStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case NOTSTARTED: return "not-started";
            case SCHEDULED: return "scheduled";
            case INPROGRESS: return "in-progress";
            case ONHOLD: return "on-hold";
            case COMPLETED: return "completed";
            case CANCELLED: return "cancelled";
            case UNKNOWN: return "unknown";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case NOTSTARTED: return "http://hl7.org/fhir/care-plan-activity-status";
            case SCHEDULED: return "http://hl7.org/fhir/care-plan-activity-status";
            case INPROGRESS: return "http://hl7.org/fhir/care-plan-activity-status";
            case ONHOLD: return "http://hl7.org/fhir/care-plan-activity-status";
            case COMPLETED: return "http://hl7.org/fhir/care-plan-activity-status";
            case CANCELLED: return "http://hl7.org/fhir/care-plan-activity-status";
            case UNKNOWN: return "http://hl7.org/fhir/care-plan-activity-status";
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
            case UNKNOWN: return "The authoring system doesn't know the current state of the activity.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case NOTSTARTED: return "Not Started";
            case SCHEDULED: return "Scheduled";
            case INPROGRESS: return "In Progress";
            case ONHOLD: return "On Hold";
            case COMPLETED: return "Completed";
            case CANCELLED: return "Cancelled";
            case UNKNOWN: return "Unknown";
            default: return "?";
          }
        }
    }

  public static class CarePlanActivityStatusEnumFactory implements EnumFactory<CarePlanActivityStatus> {
    public CarePlanActivityStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("not-started".equals(codeString))
          return CarePlanActivityStatus.NOTSTARTED;
        if ("scheduled".equals(codeString))
          return CarePlanActivityStatus.SCHEDULED;
        if ("in-progress".equals(codeString))
          return CarePlanActivityStatus.INPROGRESS;
        if ("on-hold".equals(codeString))
          return CarePlanActivityStatus.ONHOLD;
        if ("completed".equals(codeString))
          return CarePlanActivityStatus.COMPLETED;
        if ("cancelled".equals(codeString))
          return CarePlanActivityStatus.CANCELLED;
        if ("unknown".equals(codeString))
          return CarePlanActivityStatus.UNKNOWN;
        throw new IllegalArgumentException("Unknown CarePlanActivityStatus code '"+codeString+"'");
        }
        public Enumeration<CarePlanActivityStatus> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<CarePlanActivityStatus>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("not-started".equals(codeString))
          return new Enumeration<CarePlanActivityStatus>(this, CarePlanActivityStatus.NOTSTARTED);
        if ("scheduled".equals(codeString))
          return new Enumeration<CarePlanActivityStatus>(this, CarePlanActivityStatus.SCHEDULED);
        if ("in-progress".equals(codeString))
          return new Enumeration<CarePlanActivityStatus>(this, CarePlanActivityStatus.INPROGRESS);
        if ("on-hold".equals(codeString))
          return new Enumeration<CarePlanActivityStatus>(this, CarePlanActivityStatus.ONHOLD);
        if ("completed".equals(codeString))
          return new Enumeration<CarePlanActivityStatus>(this, CarePlanActivityStatus.COMPLETED);
        if ("cancelled".equals(codeString))
          return new Enumeration<CarePlanActivityStatus>(this, CarePlanActivityStatus.CANCELLED);
        if ("unknown".equals(codeString))
          return new Enumeration<CarePlanActivityStatus>(this, CarePlanActivityStatus.UNKNOWN);
        throw new FHIRException("Unknown CarePlanActivityStatus code '"+codeString+"'");
        }
    public String toCode(CarePlanActivityStatus code) {
      if (code == CarePlanActivityStatus.NOTSTARTED)
        return "not-started";
      if (code == CarePlanActivityStatus.SCHEDULED)
        return "scheduled";
      if (code == CarePlanActivityStatus.INPROGRESS)
        return "in-progress";
      if (code == CarePlanActivityStatus.ONHOLD)
        return "on-hold";
      if (code == CarePlanActivityStatus.COMPLETED)
        return "completed";
      if (code == CarePlanActivityStatus.CANCELLED)
        return "cancelled";
      if (code == CarePlanActivityStatus.UNKNOWN)
        return "unknown";
      return "?";
      }
    public String toSystem(CarePlanActivityStatus code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class CarePlanActivityComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Identifies the outcome at the point when the status of the activity is assessed.  For example, the outcome of an education activity could be patient understands (or not).
         */
        @Child(name = "outcomeCodeableConcept", type = {CodeableConcept.class}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Results of the activity", formalDefinition="Identifies the outcome at the point when the status of the activity is assessed.  For example, the outcome of an education activity could be patient understands (or not)." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/care-plan-activity-outcome")
        protected List<CodeableConcept> outcomeCodeableConcept;

        /**
         * Details of the outcome or action resulting from the activity.  The reference to an "event" resource, such as Procedure or Encounter or Observation, is the result/outcome of the activity itself.  The activity can be conveyed using CarePlan.activity.detail OR using the CarePlan.activity.reference (a reference to a “request” resource).
         */
        @Child(name = "outcomeReference", type = {Reference.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Appointment, Encounter, Procedure, etc.", formalDefinition="Details of the outcome or action resulting from the activity.  The reference to an \"event\" resource, such as Procedure or Encounter or Observation, is the result/outcome of the activity itself.  The activity can be conveyed using CarePlan.activity.detail OR using the CarePlan.activity.reference (a reference to a “request” resource)." )
        protected List<Reference> outcomeReference;
        /**
         * The actual objects that are the target of the reference (Details of the outcome or action resulting from the activity.  The reference to an "event" resource, such as Procedure or Encounter or Observation, is the result/outcome of the activity itself.  The activity can be conveyed using CarePlan.activity.detail OR using the CarePlan.activity.reference (a reference to a “request” resource).)
         */
        protected List<Resource> outcomeReferenceTarget;


        /**
         * Notes about the adherence/status/progress of the activity.
         */
        @Child(name = "progress", type = {Annotation.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Comments about the activity status/progress", formalDefinition="Notes about the adherence/status/progress of the activity." )
        protected List<Annotation> progress;

        /**
         * The details of the proposed activity represented in a specific resource.
         */
        @Child(name = "reference", type = {Appointment.class, CommunicationRequest.class, DeviceRequest.class, MedicationRequest.class, NutritionOrder.class, Task.class, ProcedureRequest.class, ReferralRequest.class, VisionPrescription.class, RequestGroup.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Activity details defined in specific resource", formalDefinition="The details of the proposed activity represented in a specific resource." )
        protected Reference reference;

        /**
         * The actual object that is the target of the reference (The details of the proposed activity represented in a specific resource.)
         */
        protected Resource referenceTarget;

        /**
         * A simple summary of a planned activity suitable for a general care plan system (e.g. form driven) that doesn't know about specific resources such as procedure etc.
         */
        @Child(name = "detail", type = {}, order=5, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="In-line definition of activity", formalDefinition="A simple summary of a planned activity suitable for a general care plan system (e.g. form driven) that doesn't know about specific resources such as procedure etc." )
        protected CarePlanActivityDetailComponent detail;

        private static final long serialVersionUID = -609287300L;

    /**
     * Constructor
     */
      public CarePlanActivityComponent() {
        super();
      }

        /**
         * @return {@link #outcomeCodeableConcept} (Identifies the outcome at the point when the status of the activity is assessed.  For example, the outcome of an education activity could be patient understands (or not).)
         */
        public List<CodeableConcept> getOutcomeCodeableConcept() { 
          if (this.outcomeCodeableConcept == null)
            this.outcomeCodeableConcept = new ArrayList<CodeableConcept>();
          return this.outcomeCodeableConcept;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public CarePlanActivityComponent setOutcomeCodeableConcept(List<CodeableConcept> theOutcomeCodeableConcept) { 
          this.outcomeCodeableConcept = theOutcomeCodeableConcept;
          return this;
        }

        public boolean hasOutcomeCodeableConcept() { 
          if (this.outcomeCodeableConcept == null)
            return false;
          for (CodeableConcept item : this.outcomeCodeableConcept)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public CodeableConcept addOutcomeCodeableConcept() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.outcomeCodeableConcept == null)
            this.outcomeCodeableConcept = new ArrayList<CodeableConcept>();
          this.outcomeCodeableConcept.add(t);
          return t;
        }

        public CarePlanActivityComponent addOutcomeCodeableConcept(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.outcomeCodeableConcept == null)
            this.outcomeCodeableConcept = new ArrayList<CodeableConcept>();
          this.outcomeCodeableConcept.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #outcomeCodeableConcept}, creating it if it does not already exist
         */
        public CodeableConcept getOutcomeCodeableConceptFirstRep() { 
          if (getOutcomeCodeableConcept().isEmpty()) {
            addOutcomeCodeableConcept();
          }
          return getOutcomeCodeableConcept().get(0);
        }

        /**
         * @return {@link #outcomeReference} (Details of the outcome or action resulting from the activity.  The reference to an "event" resource, such as Procedure or Encounter or Observation, is the result/outcome of the activity itself.  The activity can be conveyed using CarePlan.activity.detail OR using the CarePlan.activity.reference (a reference to a “request” resource).)
         */
        public List<Reference> getOutcomeReference() { 
          if (this.outcomeReference == null)
            this.outcomeReference = new ArrayList<Reference>();
          return this.outcomeReference;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public CarePlanActivityComponent setOutcomeReference(List<Reference> theOutcomeReference) { 
          this.outcomeReference = theOutcomeReference;
          return this;
        }

        public boolean hasOutcomeReference() { 
          if (this.outcomeReference == null)
            return false;
          for (Reference item : this.outcomeReference)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public Reference addOutcomeReference() { //3
          Reference t = new Reference();
          if (this.outcomeReference == null)
            this.outcomeReference = new ArrayList<Reference>();
          this.outcomeReference.add(t);
          return t;
        }

        public CarePlanActivityComponent addOutcomeReference(Reference t) { //3
          if (t == null)
            return this;
          if (this.outcomeReference == null)
            this.outcomeReference = new ArrayList<Reference>();
          this.outcomeReference.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #outcomeReference}, creating it if it does not already exist
         */
        public Reference getOutcomeReferenceFirstRep() { 
          if (getOutcomeReference().isEmpty()) {
            addOutcomeReference();
          }
          return getOutcomeReference().get(0);
        }

        /**
         * @deprecated Use Reference#setResource(IBaseResource) instead
         */
        @Deprecated
        public List<Resource> getOutcomeReferenceTarget() { 
          if (this.outcomeReferenceTarget == null)
            this.outcomeReferenceTarget = new ArrayList<Resource>();
          return this.outcomeReferenceTarget;
        }

        /**
         * @return {@link #progress} (Notes about the adherence/status/progress of the activity.)
         */
        public List<Annotation> getProgress() { 
          if (this.progress == null)
            this.progress = new ArrayList<Annotation>();
          return this.progress;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public CarePlanActivityComponent setProgress(List<Annotation> theProgress) { 
          this.progress = theProgress;
          return this;
        }

        public boolean hasProgress() { 
          if (this.progress == null)
            return false;
          for (Annotation item : this.progress)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public Annotation addProgress() { //3
          Annotation t = new Annotation();
          if (this.progress == null)
            this.progress = new ArrayList<Annotation>();
          this.progress.add(t);
          return t;
        }

        public CarePlanActivityComponent addProgress(Annotation t) { //3
          if (t == null)
            return this;
          if (this.progress == null)
            this.progress = new ArrayList<Annotation>();
          this.progress.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #progress}, creating it if it does not already exist
         */
        public Annotation getProgressFirstRep() { 
          if (getProgress().isEmpty()) {
            addProgress();
          }
          return getProgress().get(0);
        }

        /**
         * @return {@link #reference} (The details of the proposed activity represented in a specific resource.)
         */
        public Reference getReference() { 
          if (this.reference == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CarePlanActivityComponent.reference");
            else if (Configuration.doAutoCreate())
              this.reference = new Reference(); // cc
          return this.reference;
        }

        public boolean hasReference() { 
          return this.reference != null && !this.reference.isEmpty();
        }

        /**
         * @param value {@link #reference} (The details of the proposed activity represented in a specific resource.)
         */
        public CarePlanActivityComponent setReference(Reference value) { 
          this.reference = value;
          return this;
        }

        /**
         * @return {@link #reference} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The details of the proposed activity represented in a specific resource.)
         */
        public Resource getReferenceTarget() { 
          return this.referenceTarget;
        }

        /**
         * @param value {@link #reference} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The details of the proposed activity represented in a specific resource.)
         */
        public CarePlanActivityComponent setReferenceTarget(Resource value) { 
          this.referenceTarget = value;
          return this;
        }

        /**
         * @return {@link #detail} (A simple summary of a planned activity suitable for a general care plan system (e.g. form driven) that doesn't know about specific resources such as procedure etc.)
         */
        public CarePlanActivityDetailComponent getDetail() { 
          if (this.detail == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CarePlanActivityComponent.detail");
            else if (Configuration.doAutoCreate())
              this.detail = new CarePlanActivityDetailComponent(); // cc
          return this.detail;
        }

        public boolean hasDetail() { 
          return this.detail != null && !this.detail.isEmpty();
        }

        /**
         * @param value {@link #detail} (A simple summary of a planned activity suitable for a general care plan system (e.g. form driven) that doesn't know about specific resources such as procedure etc.)
         */
        public CarePlanActivityComponent setDetail(CarePlanActivityDetailComponent value) { 
          this.detail = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("outcomeCodeableConcept", "CodeableConcept", "Identifies the outcome at the point when the status of the activity is assessed.  For example, the outcome of an education activity could be patient understands (or not).", 0, java.lang.Integer.MAX_VALUE, outcomeCodeableConcept));
          childrenList.add(new Property("outcomeReference", "Reference(Any)", "Details of the outcome or action resulting from the activity.  The reference to an \"event\" resource, such as Procedure or Encounter or Observation, is the result/outcome of the activity itself.  The activity can be conveyed using CarePlan.activity.detail OR using the CarePlan.activity.reference (a reference to a “request” resource).", 0, java.lang.Integer.MAX_VALUE, outcomeReference));
          childrenList.add(new Property("progress", "Annotation", "Notes about the adherence/status/progress of the activity.", 0, java.lang.Integer.MAX_VALUE, progress));
          childrenList.add(new Property("reference", "Reference(Appointment|CommunicationRequest|DeviceRequest|MedicationRequest|NutritionOrder|Task|ProcedureRequest|ReferralRequest|VisionPrescription|RequestGroup)", "The details of the proposed activity represented in a specific resource.", 0, java.lang.Integer.MAX_VALUE, reference));
          childrenList.add(new Property("detail", "", "A simple summary of a planned activity suitable for a general care plan system (e.g. form driven) that doesn't know about specific resources such as procedure etc.", 0, java.lang.Integer.MAX_VALUE, detail));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -511913489: /*outcomeCodeableConcept*/ return this.outcomeCodeableConcept == null ? new Base[0] : this.outcomeCodeableConcept.toArray(new Base[this.outcomeCodeableConcept.size()]); // CodeableConcept
        case -782273511: /*outcomeReference*/ return this.outcomeReference == null ? new Base[0] : this.outcomeReference.toArray(new Base[this.outcomeReference.size()]); // Reference
        case -1001078227: /*progress*/ return this.progress == null ? new Base[0] : this.progress.toArray(new Base[this.progress.size()]); // Annotation
        case -925155509: /*reference*/ return this.reference == null ? new Base[0] : new Base[] {this.reference}; // Reference
        case -1335224239: /*detail*/ return this.detail == null ? new Base[0] : new Base[] {this.detail}; // CarePlanActivityDetailComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -511913489: // outcomeCodeableConcept
          this.getOutcomeCodeableConcept().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -782273511: // outcomeReference
          this.getOutcomeReference().add(castToReference(value)); // Reference
          return value;
        case -1001078227: // progress
          this.getProgress().add(castToAnnotation(value)); // Annotation
          return value;
        case -925155509: // reference
          this.reference = castToReference(value); // Reference
          return value;
        case -1335224239: // detail
          this.detail = (CarePlanActivityDetailComponent) value; // CarePlanActivityDetailComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("outcomeCodeableConcept")) {
          this.getOutcomeCodeableConcept().add(castToCodeableConcept(value));
        } else if (name.equals("outcomeReference")) {
          this.getOutcomeReference().add(castToReference(value));
        } else if (name.equals("progress")) {
          this.getProgress().add(castToAnnotation(value));
        } else if (name.equals("reference")) {
          this.reference = castToReference(value); // Reference
        } else if (name.equals("detail")) {
          this.detail = (CarePlanActivityDetailComponent) value; // CarePlanActivityDetailComponent
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -511913489:  return addOutcomeCodeableConcept(); 
        case -782273511:  return addOutcomeReference(); 
        case -1001078227:  return addProgress(); 
        case -925155509:  return getReference(); 
        case -1335224239:  return getDetail(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -511913489: /*outcomeCodeableConcept*/ return new String[] {"CodeableConcept"};
        case -782273511: /*outcomeReference*/ return new String[] {"Reference"};
        case -1001078227: /*progress*/ return new String[] {"Annotation"};
        case -925155509: /*reference*/ return new String[] {"Reference"};
        case -1335224239: /*detail*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("outcomeCodeableConcept")) {
          return addOutcomeCodeableConcept();
        }
        else if (name.equals("outcomeReference")) {
          return addOutcomeReference();
        }
        else if (name.equals("progress")) {
          return addProgress();
        }
        else if (name.equals("reference")) {
          this.reference = new Reference();
          return this.reference;
        }
        else if (name.equals("detail")) {
          this.detail = new CarePlanActivityDetailComponent();
          return this.detail;
        }
        else
          return super.addChild(name);
      }

      public CarePlanActivityComponent copy() {
        CarePlanActivityComponent dst = new CarePlanActivityComponent();
        copyValues(dst);
        if (outcomeCodeableConcept != null) {
          dst.outcomeCodeableConcept = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : outcomeCodeableConcept)
            dst.outcomeCodeableConcept.add(i.copy());
        };
        if (outcomeReference != null) {
          dst.outcomeReference = new ArrayList<Reference>();
          for (Reference i : outcomeReference)
            dst.outcomeReference.add(i.copy());
        };
        if (progress != null) {
          dst.progress = new ArrayList<Annotation>();
          for (Annotation i : progress)
            dst.progress.add(i.copy());
        };
        dst.reference = reference == null ? null : reference.copy();
        dst.detail = detail == null ? null : detail.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof CarePlanActivityComponent))
          return false;
        CarePlanActivityComponent o = (CarePlanActivityComponent) other;
        return compareDeep(outcomeCodeableConcept, o.outcomeCodeableConcept, true) && compareDeep(outcomeReference, o.outcomeReference, true)
           && compareDeep(progress, o.progress, true) && compareDeep(reference, o.reference, true) && compareDeep(detail, o.detail, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof CarePlanActivityComponent))
          return false;
        CarePlanActivityComponent o = (CarePlanActivityComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(outcomeCodeableConcept, outcomeReference
          , progress, reference, detail);
      }

  public String fhirType() {
    return "CarePlan.activity";

  }

  }

    @Block()
    public static class CarePlanActivityDetailComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * High-level categorization of the type of activity in a care plan.
         */
        @Child(name = "category", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="diet | drug | encounter | observation | procedure | supply | other", formalDefinition="High-level categorization of the type of activity in a care plan." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/care-plan-activity-category")
        protected CodeableConcept category;

        /**
         * Identifies the protocol, questionnaire, guideline or other specification the planned activity should be conducted in accordance with.
         */
        @Child(name = "definition", type = {PlanDefinition.class, ActivityDefinition.class, Questionnaire.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Protocol or definition", formalDefinition="Identifies the protocol, questionnaire, guideline or other specification the planned activity should be conducted in accordance with." )
        protected Reference definition;

        /**
         * The actual object that is the target of the reference (Identifies the protocol, questionnaire, guideline or other specification the planned activity should be conducted in accordance with.)
         */
        protected Resource definitionTarget;

        /**
         * Detailed description of the type of planned activity; e.g. What lab test, what procedure, what kind of encounter.
         */
        @Child(name = "code", type = {CodeableConcept.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Detail type of activity", formalDefinition="Detailed description of the type of planned activity; e.g. What lab test, what procedure, what kind of encounter." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/care-plan-activity")
        protected CodeableConcept code;

        /**
         * Provides the rationale that drove the inclusion of this particular activity as part of the plan or the reason why the activity was prohibited.
         */
        @Child(name = "reasonCode", type = {CodeableConcept.class}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Why activity should be done or why activity was prohibited", formalDefinition="Provides the rationale that drove the inclusion of this particular activity as part of the plan or the reason why the activity was prohibited." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/activity-reason")
        protected List<CodeableConcept> reasonCode;

        /**
         * Provides the health condition(s) that drove the inclusion of this particular activity as part of the plan.
         */
        @Child(name = "reasonReference", type = {Condition.class}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Condition triggering need for activity", formalDefinition="Provides the health condition(s) that drove the inclusion of this particular activity as part of the plan." )
        protected List<Reference> reasonReference;
        /**
         * The actual objects that are the target of the reference (Provides the health condition(s) that drove the inclusion of this particular activity as part of the plan.)
         */
        protected List<Condition> reasonReferenceTarget;


        /**
         * Internal reference that identifies the goals that this activity is intended to contribute towards meeting.
         */
        @Child(name = "goal", type = {Goal.class}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Goals this activity relates to", formalDefinition="Internal reference that identifies the goals that this activity is intended to contribute towards meeting." )
        protected List<Reference> goal;
        /**
         * The actual objects that are the target of the reference (Internal reference that identifies the goals that this activity is intended to contribute towards meeting.)
         */
        protected List<Goal> goalTarget;


        /**
         * Identifies what progress is being made for the specific activity.
         */
        @Child(name = "status", type = {CodeType.class}, order=7, min=1, max=1, modifier=true, summary=false)
        @Description(shortDefinition="not-started | scheduled | in-progress | on-hold | completed | cancelled | unknown", formalDefinition="Identifies what progress is being made for the specific activity." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/care-plan-activity-status")
        protected Enumeration<CarePlanActivityStatus> status;

        /**
         * Provides reason why the activity isn't yet started, is on hold, was cancelled, etc.
         */
        @Child(name = "statusReason", type = {StringType.class}, order=8, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Reason for current status", formalDefinition="Provides reason why the activity isn't yet started, is on hold, was cancelled, etc." )
        protected StringType statusReason;

        /**
         * If true, indicates that the described activity is one that must NOT be engaged in when following the plan.  If false, indicates that the described activity is one that should be engaged in when following the plan.
         */
        @Child(name = "prohibited", type = {BooleanType.class}, order=9, min=0, max=1, modifier=true, summary=false)
        @Description(shortDefinition="Do NOT do", formalDefinition="If true, indicates that the described activity is one that must NOT be engaged in when following the plan.  If false, indicates that the described activity is one that should be engaged in when following the plan." )
        protected BooleanType prohibited;

        /**
         * The period, timing or frequency upon which the described activity is to occur.
         */
        @Child(name = "scheduled", type = {Timing.class, Period.class, StringType.class}, order=10, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="When activity is to occur", formalDefinition="The period, timing or frequency upon which the described activity is to occur." )
        protected Type scheduled;

        /**
         * Identifies the facility where the activity will occur; e.g. home, hospital, specific clinic, etc.
         */
        @Child(name = "location", type = {Location.class}, order=11, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Where it should happen", formalDefinition="Identifies the facility where the activity will occur; e.g. home, hospital, specific clinic, etc." )
        protected Reference location;

        /**
         * The actual object that is the target of the reference (Identifies the facility where the activity will occur; e.g. home, hospital, specific clinic, etc.)
         */
        protected Location locationTarget;

        /**
         * Identifies who's expected to be involved in the activity.
         */
        @Child(name = "performer", type = {Practitioner.class, Organization.class, RelatedPerson.class, Patient.class, CareTeam.class}, order=12, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Who will be responsible?", formalDefinition="Identifies who's expected to be involved in the activity." )
        protected List<Reference> performer;
        /**
         * The actual objects that are the target of the reference (Identifies who's expected to be involved in the activity.)
         */
        protected List<Resource> performerTarget;


        /**
         * Identifies the food, drug or other product to be consumed or supplied in the activity.
         */
        @Child(name = "product", type = {CodeableConcept.class, Medication.class, Substance.class}, order=13, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="What is to be administered/supplied", formalDefinition="Identifies the food, drug or other product to be consumed or supplied in the activity." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/medication-codes")
        protected Type product;

        /**
         * Identifies the quantity expected to be consumed in a given day.
         */
        @Child(name = "dailyAmount", type = {SimpleQuantity.class}, order=14, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="How to consume/day?", formalDefinition="Identifies the quantity expected to be consumed in a given day." )
        protected SimpleQuantity dailyAmount;

        /**
         * Identifies the quantity expected to be supplied, administered or consumed by the subject.
         */
        @Child(name = "quantity", type = {SimpleQuantity.class}, order=15, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="How much to administer/supply/consume", formalDefinition="Identifies the quantity expected to be supplied, administered or consumed by the subject." )
        protected SimpleQuantity quantity;

        /**
         * This provides a textual description of constraints on the intended activity occurrence, including relation to other activities.  It may also include objectives, pre-conditions and end-conditions.  Finally, it may convey specifics about the activity such as body site, method, route, etc.
         */
        @Child(name = "description", type = {StringType.class}, order=16, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Extra info describing activity to perform", formalDefinition="This provides a textual description of constraints on the intended activity occurrence, including relation to other activities.  It may also include objectives, pre-conditions and end-conditions.  Finally, it may convey specifics about the activity such as body site, method, route, etc." )
        protected StringType description;

        private static final long serialVersionUID = -549984462L;

    /**
     * Constructor
     */
      public CarePlanActivityDetailComponent() {
        super();
      }

    /**
     * Constructor
     */
      public CarePlanActivityDetailComponent(Enumeration<CarePlanActivityStatus> status) {
        super();
        this.status = status;
      }

        /**
         * @return {@link #category} (High-level categorization of the type of activity in a care plan.)
         */
        public CodeableConcept getCategory() { 
          if (this.category == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CarePlanActivityDetailComponent.category");
            else if (Configuration.doAutoCreate())
              this.category = new CodeableConcept(); // cc
          return this.category;
        }

        public boolean hasCategory() { 
          return this.category != null && !this.category.isEmpty();
        }

        /**
         * @param value {@link #category} (High-level categorization of the type of activity in a care plan.)
         */
        public CarePlanActivityDetailComponent setCategory(CodeableConcept value) { 
          this.category = value;
          return this;
        }

        /**
         * @return {@link #definition} (Identifies the protocol, questionnaire, guideline or other specification the planned activity should be conducted in accordance with.)
         */
        public Reference getDefinition() { 
          if (this.definition == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CarePlanActivityDetailComponent.definition");
            else if (Configuration.doAutoCreate())
              this.definition = new Reference(); // cc
          return this.definition;
        }

        public boolean hasDefinition() { 
          return this.definition != null && !this.definition.isEmpty();
        }

        /**
         * @param value {@link #definition} (Identifies the protocol, questionnaire, guideline or other specification the planned activity should be conducted in accordance with.)
         */
        public CarePlanActivityDetailComponent setDefinition(Reference value) { 
          this.definition = value;
          return this;
        }

        /**
         * @return {@link #definition} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Identifies the protocol, questionnaire, guideline or other specification the planned activity should be conducted in accordance with.)
         */
        public Resource getDefinitionTarget() { 
          return this.definitionTarget;
        }

        /**
         * @param value {@link #definition} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Identifies the protocol, questionnaire, guideline or other specification the planned activity should be conducted in accordance with.)
         */
        public CarePlanActivityDetailComponent setDefinitionTarget(Resource value) { 
          this.definitionTarget = value;
          return this;
        }

        /**
         * @return {@link #code} (Detailed description of the type of planned activity; e.g. What lab test, what procedure, what kind of encounter.)
         */
        public CodeableConcept getCode() { 
          if (this.code == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CarePlanActivityDetailComponent.code");
            else if (Configuration.doAutoCreate())
              this.code = new CodeableConcept(); // cc
          return this.code;
        }

        public boolean hasCode() { 
          return this.code != null && !this.code.isEmpty();
        }

        /**
         * @param value {@link #code} (Detailed description of the type of planned activity; e.g. What lab test, what procedure, what kind of encounter.)
         */
        public CarePlanActivityDetailComponent setCode(CodeableConcept value) { 
          this.code = value;
          return this;
        }

        /**
         * @return {@link #reasonCode} (Provides the rationale that drove the inclusion of this particular activity as part of the plan or the reason why the activity was prohibited.)
         */
        public List<CodeableConcept> getReasonCode() { 
          if (this.reasonCode == null)
            this.reasonCode = new ArrayList<CodeableConcept>();
          return this.reasonCode;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public CarePlanActivityDetailComponent setReasonCode(List<CodeableConcept> theReasonCode) { 
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

        public CarePlanActivityDetailComponent addReasonCode(CodeableConcept t) { //3
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
         * @return {@link #reasonReference} (Provides the health condition(s) that drove the inclusion of this particular activity as part of the plan.)
         */
        public List<Reference> getReasonReference() { 
          if (this.reasonReference == null)
            this.reasonReference = new ArrayList<Reference>();
          return this.reasonReference;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public CarePlanActivityDetailComponent setReasonReference(List<Reference> theReasonReference) { 
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

        public CarePlanActivityDetailComponent addReasonReference(Reference t) { //3
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
        public List<Condition> getReasonReferenceTarget() { 
          if (this.reasonReferenceTarget == null)
            this.reasonReferenceTarget = new ArrayList<Condition>();
          return this.reasonReferenceTarget;
        }

        /**
         * @deprecated Use Reference#setResource(IBaseResource) instead
         */
        @Deprecated
        public Condition addReasonReferenceTarget() { 
          Condition r = new Condition();
          if (this.reasonReferenceTarget == null)
            this.reasonReferenceTarget = new ArrayList<Condition>();
          this.reasonReferenceTarget.add(r);
          return r;
        }

        /**
         * @return {@link #goal} (Internal reference that identifies the goals that this activity is intended to contribute towards meeting.)
         */
        public List<Reference> getGoal() { 
          if (this.goal == null)
            this.goal = new ArrayList<Reference>();
          return this.goal;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public CarePlanActivityDetailComponent setGoal(List<Reference> theGoal) { 
          this.goal = theGoal;
          return this;
        }

        public boolean hasGoal() { 
          if (this.goal == null)
            return false;
          for (Reference item : this.goal)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public Reference addGoal() { //3
          Reference t = new Reference();
          if (this.goal == null)
            this.goal = new ArrayList<Reference>();
          this.goal.add(t);
          return t;
        }

        public CarePlanActivityDetailComponent addGoal(Reference t) { //3
          if (t == null)
            return this;
          if (this.goal == null)
            this.goal = new ArrayList<Reference>();
          this.goal.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #goal}, creating it if it does not already exist
         */
        public Reference getGoalFirstRep() { 
          if (getGoal().isEmpty()) {
            addGoal();
          }
          return getGoal().get(0);
        }

        /**
         * @deprecated Use Reference#setResource(IBaseResource) instead
         */
        @Deprecated
        public List<Goal> getGoalTarget() { 
          if (this.goalTarget == null)
            this.goalTarget = new ArrayList<Goal>();
          return this.goalTarget;
        }

        /**
         * @deprecated Use Reference#setResource(IBaseResource) instead
         */
        @Deprecated
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
        public Enumeration<CarePlanActivityStatus> getStatusElement() { 
          if (this.status == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CarePlanActivityDetailComponent.status");
            else if (Configuration.doAutoCreate())
              this.status = new Enumeration<CarePlanActivityStatus>(new CarePlanActivityStatusEnumFactory()); // bb
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
        public CarePlanActivityDetailComponent setStatusElement(Enumeration<CarePlanActivityStatus> value) { 
          this.status = value;
          return this;
        }

        /**
         * @return Identifies what progress is being made for the specific activity.
         */
        public CarePlanActivityStatus getStatus() { 
          return this.status == null ? null : this.status.getValue();
        }

        /**
         * @param value Identifies what progress is being made for the specific activity.
         */
        public CarePlanActivityDetailComponent setStatus(CarePlanActivityStatus value) { 
            if (this.status == null)
              this.status = new Enumeration<CarePlanActivityStatus>(new CarePlanActivityStatusEnumFactory());
            this.status.setValue(value);
          return this;
        }

        /**
         * @return {@link #statusReason} (Provides reason why the activity isn't yet started, is on hold, was cancelled, etc.). This is the underlying object with id, value and extensions. The accessor "getStatusReason" gives direct access to the value
         */
        public StringType getStatusReasonElement() { 
          if (this.statusReason == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CarePlanActivityDetailComponent.statusReason");
            else if (Configuration.doAutoCreate())
              this.statusReason = new StringType(); // bb
          return this.statusReason;
        }

        public boolean hasStatusReasonElement() { 
          return this.statusReason != null && !this.statusReason.isEmpty();
        }

        public boolean hasStatusReason() { 
          return this.statusReason != null && !this.statusReason.isEmpty();
        }

        /**
         * @param value {@link #statusReason} (Provides reason why the activity isn't yet started, is on hold, was cancelled, etc.). This is the underlying object with id, value and extensions. The accessor "getStatusReason" gives direct access to the value
         */
        public CarePlanActivityDetailComponent setStatusReasonElement(StringType value) { 
          this.statusReason = value;
          return this;
        }

        /**
         * @return Provides reason why the activity isn't yet started, is on hold, was cancelled, etc.
         */
        public String getStatusReason() { 
          return this.statusReason == null ? null : this.statusReason.getValue();
        }

        /**
         * @param value Provides reason why the activity isn't yet started, is on hold, was cancelled, etc.
         */
        public CarePlanActivityDetailComponent setStatusReason(String value) { 
          if (Utilities.noString(value))
            this.statusReason = null;
          else {
            if (this.statusReason == null)
              this.statusReason = new StringType();
            this.statusReason.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #prohibited} (If true, indicates that the described activity is one that must NOT be engaged in when following the plan.  If false, indicates that the described activity is one that should be engaged in when following the plan.). This is the underlying object with id, value and extensions. The accessor "getProhibited" gives direct access to the value
         */
        public BooleanType getProhibitedElement() { 
          if (this.prohibited == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CarePlanActivityDetailComponent.prohibited");
            else if (Configuration.doAutoCreate())
              this.prohibited = new BooleanType(); // bb
          return this.prohibited;
        }

        public boolean hasProhibitedElement() { 
          return this.prohibited != null && !this.prohibited.isEmpty();
        }

        public boolean hasProhibited() { 
          return this.prohibited != null && !this.prohibited.isEmpty();
        }

        /**
         * @param value {@link #prohibited} (If true, indicates that the described activity is one that must NOT be engaged in when following the plan.  If false, indicates that the described activity is one that should be engaged in when following the plan.). This is the underlying object with id, value and extensions. The accessor "getProhibited" gives direct access to the value
         */
        public CarePlanActivityDetailComponent setProhibitedElement(BooleanType value) { 
          this.prohibited = value;
          return this;
        }

        /**
         * @return If true, indicates that the described activity is one that must NOT be engaged in when following the plan.  If false, indicates that the described activity is one that should be engaged in when following the plan.
         */
        public boolean getProhibited() { 
          return this.prohibited == null || this.prohibited.isEmpty() ? false : this.prohibited.getValue();
        }

        /**
         * @param value If true, indicates that the described activity is one that must NOT be engaged in when following the plan.  If false, indicates that the described activity is one that should be engaged in when following the plan.
         */
        public CarePlanActivityDetailComponent setProhibited(boolean value) { 
            if (this.prohibited == null)
              this.prohibited = new BooleanType();
            this.prohibited.setValue(value);
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
        public Timing getScheduledTiming() throws FHIRException { 
          if (!(this.scheduled instanceof Timing))
            throw new FHIRException("Type mismatch: the type Timing was expected, but "+this.scheduled.getClass().getName()+" was encountered");
          return (Timing) this.scheduled;
        }

        public boolean hasScheduledTiming() { 
          return this.scheduled instanceof Timing;
        }

        /**
         * @return {@link #scheduled} (The period, timing or frequency upon which the described activity is to occur.)
         */
        public Period getScheduledPeriod() throws FHIRException { 
          if (!(this.scheduled instanceof Period))
            throw new FHIRException("Type mismatch: the type Period was expected, but "+this.scheduled.getClass().getName()+" was encountered");
          return (Period) this.scheduled;
        }

        public boolean hasScheduledPeriod() { 
          return this.scheduled instanceof Period;
        }

        /**
         * @return {@link #scheduled} (The period, timing or frequency upon which the described activity is to occur.)
         */
        public StringType getScheduledStringType() throws FHIRException { 
          if (!(this.scheduled instanceof StringType))
            throw new FHIRException("Type mismatch: the type StringType was expected, but "+this.scheduled.getClass().getName()+" was encountered");
          return (StringType) this.scheduled;
        }

        public boolean hasScheduledStringType() { 
          return this.scheduled instanceof StringType;
        }

        public boolean hasScheduled() { 
          return this.scheduled != null && !this.scheduled.isEmpty();
        }

        /**
         * @param value {@link #scheduled} (The period, timing or frequency upon which the described activity is to occur.)
         */
        public CarePlanActivityDetailComponent setScheduled(Type value) { 
          this.scheduled = value;
          return this;
        }

        /**
         * @return {@link #location} (Identifies the facility where the activity will occur; e.g. home, hospital, specific clinic, etc.)
         */
        public Reference getLocation() { 
          if (this.location == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CarePlanActivityDetailComponent.location");
            else if (Configuration.doAutoCreate())
              this.location = new Reference(); // cc
          return this.location;
        }

        public boolean hasLocation() { 
          return this.location != null && !this.location.isEmpty();
        }

        /**
         * @param value {@link #location} (Identifies the facility where the activity will occur; e.g. home, hospital, specific clinic, etc.)
         */
        public CarePlanActivityDetailComponent setLocation(Reference value) { 
          this.location = value;
          return this;
        }

        /**
         * @return {@link #location} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Identifies the facility where the activity will occur; e.g. home, hospital, specific clinic, etc.)
         */
        public Location getLocationTarget() { 
          if (this.locationTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CarePlanActivityDetailComponent.location");
            else if (Configuration.doAutoCreate())
              this.locationTarget = new Location(); // aa
          return this.locationTarget;
        }

        /**
         * @param value {@link #location} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Identifies the facility where the activity will occur; e.g. home, hospital, specific clinic, etc.)
         */
        public CarePlanActivityDetailComponent setLocationTarget(Location value) { 
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

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public CarePlanActivityDetailComponent setPerformer(List<Reference> thePerformer) { 
          this.performer = thePerformer;
          return this;
        }

        public boolean hasPerformer() { 
          if (this.performer == null)
            return false;
          for (Reference item : this.performer)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public Reference addPerformer() { //3
          Reference t = new Reference();
          if (this.performer == null)
            this.performer = new ArrayList<Reference>();
          this.performer.add(t);
          return t;
        }

        public CarePlanActivityDetailComponent addPerformer(Reference t) { //3
          if (t == null)
            return this;
          if (this.performer == null)
            this.performer = new ArrayList<Reference>();
          this.performer.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #performer}, creating it if it does not already exist
         */
        public Reference getPerformerFirstRep() { 
          if (getPerformer().isEmpty()) {
            addPerformer();
          }
          return getPerformer().get(0);
        }

        /**
         * @deprecated Use Reference#setResource(IBaseResource) instead
         */
        @Deprecated
        public List<Resource> getPerformerTarget() { 
          if (this.performerTarget == null)
            this.performerTarget = new ArrayList<Resource>();
          return this.performerTarget;
        }

        /**
         * @return {@link #product} (Identifies the food, drug or other product to be consumed or supplied in the activity.)
         */
        public Type getProduct() { 
          return this.product;
        }

        /**
         * @return {@link #product} (Identifies the food, drug or other product to be consumed or supplied in the activity.)
         */
        public CodeableConcept getProductCodeableConcept() throws FHIRException { 
          if (!(this.product instanceof CodeableConcept))
            throw new FHIRException("Type mismatch: the type CodeableConcept was expected, but "+this.product.getClass().getName()+" was encountered");
          return (CodeableConcept) this.product;
        }

        public boolean hasProductCodeableConcept() { 
          return this.product instanceof CodeableConcept;
        }

        /**
         * @return {@link #product} (Identifies the food, drug or other product to be consumed or supplied in the activity.)
         */
        public Reference getProductReference() throws FHIRException { 
          if (!(this.product instanceof Reference))
            throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.product.getClass().getName()+" was encountered");
          return (Reference) this.product;
        }

        public boolean hasProductReference() { 
          return this.product instanceof Reference;
        }

        public boolean hasProduct() { 
          return this.product != null && !this.product.isEmpty();
        }

        /**
         * @param value {@link #product} (Identifies the food, drug or other product to be consumed or supplied in the activity.)
         */
        public CarePlanActivityDetailComponent setProduct(Type value) { 
          this.product = value;
          return this;
        }

        /**
         * @return {@link #dailyAmount} (Identifies the quantity expected to be consumed in a given day.)
         */
        public SimpleQuantity getDailyAmount() { 
          if (this.dailyAmount == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CarePlanActivityDetailComponent.dailyAmount");
            else if (Configuration.doAutoCreate())
              this.dailyAmount = new SimpleQuantity(); // cc
          return this.dailyAmount;
        }

        public boolean hasDailyAmount() { 
          return this.dailyAmount != null && !this.dailyAmount.isEmpty();
        }

        /**
         * @param value {@link #dailyAmount} (Identifies the quantity expected to be consumed in a given day.)
         */
        public CarePlanActivityDetailComponent setDailyAmount(SimpleQuantity value) { 
          this.dailyAmount = value;
          return this;
        }

        /**
         * @return {@link #quantity} (Identifies the quantity expected to be supplied, administered or consumed by the subject.)
         */
        public SimpleQuantity getQuantity() { 
          if (this.quantity == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CarePlanActivityDetailComponent.quantity");
            else if (Configuration.doAutoCreate())
              this.quantity = new SimpleQuantity(); // cc
          return this.quantity;
        }

        public boolean hasQuantity() { 
          return this.quantity != null && !this.quantity.isEmpty();
        }

        /**
         * @param value {@link #quantity} (Identifies the quantity expected to be supplied, administered or consumed by the subject.)
         */
        public CarePlanActivityDetailComponent setQuantity(SimpleQuantity value) { 
          this.quantity = value;
          return this;
        }

        /**
         * @return {@link #description} (This provides a textual description of constraints on the intended activity occurrence, including relation to other activities.  It may also include objectives, pre-conditions and end-conditions.  Finally, it may convey specifics about the activity such as body site, method, route, etc.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public StringType getDescriptionElement() { 
          if (this.description == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CarePlanActivityDetailComponent.description");
            else if (Configuration.doAutoCreate())
              this.description = new StringType(); // bb
          return this.description;
        }

        public boolean hasDescriptionElement() { 
          return this.description != null && !this.description.isEmpty();
        }

        public boolean hasDescription() { 
          return this.description != null && !this.description.isEmpty();
        }

        /**
         * @param value {@link #description} (This provides a textual description of constraints on the intended activity occurrence, including relation to other activities.  It may also include objectives, pre-conditions and end-conditions.  Finally, it may convey specifics about the activity such as body site, method, route, etc.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public CarePlanActivityDetailComponent setDescriptionElement(StringType value) { 
          this.description = value;
          return this;
        }

        /**
         * @return This provides a textual description of constraints on the intended activity occurrence, including relation to other activities.  It may also include objectives, pre-conditions and end-conditions.  Finally, it may convey specifics about the activity such as body site, method, route, etc.
         */
        public String getDescription() { 
          return this.description == null ? null : this.description.getValue();
        }

        /**
         * @param value This provides a textual description of constraints on the intended activity occurrence, including relation to other activities.  It may also include objectives, pre-conditions and end-conditions.  Finally, it may convey specifics about the activity such as body site, method, route, etc.
         */
        public CarePlanActivityDetailComponent setDescription(String value) { 
          if (Utilities.noString(value))
            this.description = null;
          else {
            if (this.description == null)
              this.description = new StringType();
            this.description.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("category", "CodeableConcept", "High-level categorization of the type of activity in a care plan.", 0, java.lang.Integer.MAX_VALUE, category));
          childrenList.add(new Property("definition", "Reference(PlanDefinition|ActivityDefinition|Questionnaire)", "Identifies the protocol, questionnaire, guideline or other specification the planned activity should be conducted in accordance with.", 0, java.lang.Integer.MAX_VALUE, definition));
          childrenList.add(new Property("code", "CodeableConcept", "Detailed description of the type of planned activity; e.g. What lab test, what procedure, what kind of encounter.", 0, java.lang.Integer.MAX_VALUE, code));
          childrenList.add(new Property("reasonCode", "CodeableConcept", "Provides the rationale that drove the inclusion of this particular activity as part of the plan or the reason why the activity was prohibited.", 0, java.lang.Integer.MAX_VALUE, reasonCode));
          childrenList.add(new Property("reasonReference", "Reference(Condition)", "Provides the health condition(s) that drove the inclusion of this particular activity as part of the plan.", 0, java.lang.Integer.MAX_VALUE, reasonReference));
          childrenList.add(new Property("goal", "Reference(Goal)", "Internal reference that identifies the goals that this activity is intended to contribute towards meeting.", 0, java.lang.Integer.MAX_VALUE, goal));
          childrenList.add(new Property("status", "code", "Identifies what progress is being made for the specific activity.", 0, java.lang.Integer.MAX_VALUE, status));
          childrenList.add(new Property("statusReason", "string", "Provides reason why the activity isn't yet started, is on hold, was cancelled, etc.", 0, java.lang.Integer.MAX_VALUE, statusReason));
          childrenList.add(new Property("prohibited", "boolean", "If true, indicates that the described activity is one that must NOT be engaged in when following the plan.  If false, indicates that the described activity is one that should be engaged in when following the plan.", 0, java.lang.Integer.MAX_VALUE, prohibited));
          childrenList.add(new Property("scheduled[x]", "Timing|Period|string", "The period, timing or frequency upon which the described activity is to occur.", 0, java.lang.Integer.MAX_VALUE, scheduled));
          childrenList.add(new Property("location", "Reference(Location)", "Identifies the facility where the activity will occur; e.g. home, hospital, specific clinic, etc.", 0, java.lang.Integer.MAX_VALUE, location));
          childrenList.add(new Property("performer", "Reference(Practitioner|Organization|RelatedPerson|Patient|CareTeam)", "Identifies who's expected to be involved in the activity.", 0, java.lang.Integer.MAX_VALUE, performer));
          childrenList.add(new Property("product[x]", "CodeableConcept|Reference(Medication|Substance)", "Identifies the food, drug or other product to be consumed or supplied in the activity.", 0, java.lang.Integer.MAX_VALUE, product));
          childrenList.add(new Property("dailyAmount", "SimpleQuantity", "Identifies the quantity expected to be consumed in a given day.", 0, java.lang.Integer.MAX_VALUE, dailyAmount));
          childrenList.add(new Property("quantity", "SimpleQuantity", "Identifies the quantity expected to be supplied, administered or consumed by the subject.", 0, java.lang.Integer.MAX_VALUE, quantity));
          childrenList.add(new Property("description", "string", "This provides a textual description of constraints on the intended activity occurrence, including relation to other activities.  It may also include objectives, pre-conditions and end-conditions.  Finally, it may convey specifics about the activity such as body site, method, route, etc.", 0, java.lang.Integer.MAX_VALUE, description));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 50511102: /*category*/ return this.category == null ? new Base[0] : new Base[] {this.category}; // CodeableConcept
        case -1014418093: /*definition*/ return this.definition == null ? new Base[0] : new Base[] {this.definition}; // Reference
        case 3059181: /*code*/ return this.code == null ? new Base[0] : new Base[] {this.code}; // CodeableConcept
        case 722137681: /*reasonCode*/ return this.reasonCode == null ? new Base[0] : this.reasonCode.toArray(new Base[this.reasonCode.size()]); // CodeableConcept
        case -1146218137: /*reasonReference*/ return this.reasonReference == null ? new Base[0] : this.reasonReference.toArray(new Base[this.reasonReference.size()]); // Reference
        case 3178259: /*goal*/ return this.goal == null ? new Base[0] : this.goal.toArray(new Base[this.goal.size()]); // Reference
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<CarePlanActivityStatus>
        case 2051346646: /*statusReason*/ return this.statusReason == null ? new Base[0] : new Base[] {this.statusReason}; // StringType
        case 663275198: /*prohibited*/ return this.prohibited == null ? new Base[0] : new Base[] {this.prohibited}; // BooleanType
        case -160710483: /*scheduled*/ return this.scheduled == null ? new Base[0] : new Base[] {this.scheduled}; // Type
        case 1901043637: /*location*/ return this.location == null ? new Base[0] : new Base[] {this.location}; // Reference
        case 481140686: /*performer*/ return this.performer == null ? new Base[0] : this.performer.toArray(new Base[this.performer.size()]); // Reference
        case -309474065: /*product*/ return this.product == null ? new Base[0] : new Base[] {this.product}; // Type
        case -768908335: /*dailyAmount*/ return this.dailyAmount == null ? new Base[0] : new Base[] {this.dailyAmount}; // SimpleQuantity
        case -1285004149: /*quantity*/ return this.quantity == null ? new Base[0] : new Base[] {this.quantity}; // SimpleQuantity
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 50511102: // category
          this.category = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1014418093: // definition
          this.definition = castToReference(value); // Reference
          return value;
        case 3059181: // code
          this.code = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 722137681: // reasonCode
          this.getReasonCode().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -1146218137: // reasonReference
          this.getReasonReference().add(castToReference(value)); // Reference
          return value;
        case 3178259: // goal
          this.getGoal().add(castToReference(value)); // Reference
          return value;
        case -892481550: // status
          value = new CarePlanActivityStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<CarePlanActivityStatus>
          return value;
        case 2051346646: // statusReason
          this.statusReason = castToString(value); // StringType
          return value;
        case 663275198: // prohibited
          this.prohibited = castToBoolean(value); // BooleanType
          return value;
        case -160710483: // scheduled
          this.scheduled = castToType(value); // Type
          return value;
        case 1901043637: // location
          this.location = castToReference(value); // Reference
          return value;
        case 481140686: // performer
          this.getPerformer().add(castToReference(value)); // Reference
          return value;
        case -309474065: // product
          this.product = castToType(value); // Type
          return value;
        case -768908335: // dailyAmount
          this.dailyAmount = castToSimpleQuantity(value); // SimpleQuantity
          return value;
        case -1285004149: // quantity
          this.quantity = castToSimpleQuantity(value); // SimpleQuantity
          return value;
        case -1724546052: // description
          this.description = castToString(value); // StringType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("category")) {
          this.category = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("definition")) {
          this.definition = castToReference(value); // Reference
        } else if (name.equals("code")) {
          this.code = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("reasonCode")) {
          this.getReasonCode().add(castToCodeableConcept(value));
        } else if (name.equals("reasonReference")) {
          this.getReasonReference().add(castToReference(value));
        } else if (name.equals("goal")) {
          this.getGoal().add(castToReference(value));
        } else if (name.equals("status")) {
          value = new CarePlanActivityStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<CarePlanActivityStatus>
        } else if (name.equals("statusReason")) {
          this.statusReason = castToString(value); // StringType
        } else if (name.equals("prohibited")) {
          this.prohibited = castToBoolean(value); // BooleanType
        } else if (name.equals("scheduled[x]")) {
          this.scheduled = castToType(value); // Type
        } else if (name.equals("location")) {
          this.location = castToReference(value); // Reference
        } else if (name.equals("performer")) {
          this.getPerformer().add(castToReference(value));
        } else if (name.equals("product[x]")) {
          this.product = castToType(value); // Type
        } else if (name.equals("dailyAmount")) {
          this.dailyAmount = castToSimpleQuantity(value); // SimpleQuantity
        } else if (name.equals("quantity")) {
          this.quantity = castToSimpleQuantity(value); // SimpleQuantity
        } else if (name.equals("description")) {
          this.description = castToString(value); // StringType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 50511102:  return getCategory(); 
        case -1014418093:  return getDefinition(); 
        case 3059181:  return getCode(); 
        case 722137681:  return addReasonCode(); 
        case -1146218137:  return addReasonReference(); 
        case 3178259:  return addGoal(); 
        case -892481550:  return getStatusElement();
        case 2051346646:  return getStatusReasonElement();
        case 663275198:  return getProhibitedElement();
        case 1162627251:  return getScheduled(); 
        case -160710483:  return getScheduled(); 
        case 1901043637:  return getLocation(); 
        case 481140686:  return addPerformer(); 
        case 1753005361:  return getProduct(); 
        case -309474065:  return getProduct(); 
        case -768908335:  return getDailyAmount(); 
        case -1285004149:  return getQuantity(); 
        case -1724546052:  return getDescriptionElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 50511102: /*category*/ return new String[] {"CodeableConcept"};
        case -1014418093: /*definition*/ return new String[] {"Reference"};
        case 3059181: /*code*/ return new String[] {"CodeableConcept"};
        case 722137681: /*reasonCode*/ return new String[] {"CodeableConcept"};
        case -1146218137: /*reasonReference*/ return new String[] {"Reference"};
        case 3178259: /*goal*/ return new String[] {"Reference"};
        case -892481550: /*status*/ return new String[] {"code"};
        case 2051346646: /*statusReason*/ return new String[] {"string"};
        case 663275198: /*prohibited*/ return new String[] {"boolean"};
        case -160710483: /*scheduled*/ return new String[] {"Timing", "Period", "string"};
        case 1901043637: /*location*/ return new String[] {"Reference"};
        case 481140686: /*performer*/ return new String[] {"Reference"};
        case -309474065: /*product*/ return new String[] {"CodeableConcept", "Reference"};
        case -768908335: /*dailyAmount*/ return new String[] {"SimpleQuantity"};
        case -1285004149: /*quantity*/ return new String[] {"SimpleQuantity"};
        case -1724546052: /*description*/ return new String[] {"string"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("category")) {
          this.category = new CodeableConcept();
          return this.category;
        }
        else if (name.equals("definition")) {
          this.definition = new Reference();
          return this.definition;
        }
        else if (name.equals("code")) {
          this.code = new CodeableConcept();
          return this.code;
        }
        else if (name.equals("reasonCode")) {
          return addReasonCode();
        }
        else if (name.equals("reasonReference")) {
          return addReasonReference();
        }
        else if (name.equals("goal")) {
          return addGoal();
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type CarePlan.status");
        }
        else if (name.equals("statusReason")) {
          throw new FHIRException("Cannot call addChild on a primitive type CarePlan.statusReason");
        }
        else if (name.equals("prohibited")) {
          throw new FHIRException("Cannot call addChild on a primitive type CarePlan.prohibited");
        }
        else if (name.equals("scheduledTiming")) {
          this.scheduled = new Timing();
          return this.scheduled;
        }
        else if (name.equals("scheduledPeriod")) {
          this.scheduled = new Period();
          return this.scheduled;
        }
        else if (name.equals("scheduledString")) {
          this.scheduled = new StringType();
          return this.scheduled;
        }
        else if (name.equals("location")) {
          this.location = new Reference();
          return this.location;
        }
        else if (name.equals("performer")) {
          return addPerformer();
        }
        else if (name.equals("productCodeableConcept")) {
          this.product = new CodeableConcept();
          return this.product;
        }
        else if (name.equals("productReference")) {
          this.product = new Reference();
          return this.product;
        }
        else if (name.equals("dailyAmount")) {
          this.dailyAmount = new SimpleQuantity();
          return this.dailyAmount;
        }
        else if (name.equals("quantity")) {
          this.quantity = new SimpleQuantity();
          return this.quantity;
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type CarePlan.description");
        }
        else
          return super.addChild(name);
      }

      public CarePlanActivityDetailComponent copy() {
        CarePlanActivityDetailComponent dst = new CarePlanActivityDetailComponent();
        copyValues(dst);
        dst.category = category == null ? null : category.copy();
        dst.definition = definition == null ? null : definition.copy();
        dst.code = code == null ? null : code.copy();
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
        if (goal != null) {
          dst.goal = new ArrayList<Reference>();
          for (Reference i : goal)
            dst.goal.add(i.copy());
        };
        dst.status = status == null ? null : status.copy();
        dst.statusReason = statusReason == null ? null : statusReason.copy();
        dst.prohibited = prohibited == null ? null : prohibited.copy();
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
        dst.description = description == null ? null : description.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof CarePlanActivityDetailComponent))
          return false;
        CarePlanActivityDetailComponent o = (CarePlanActivityDetailComponent) other;
        return compareDeep(category, o.category, true) && compareDeep(definition, o.definition, true) && compareDeep(code, o.code, true)
           && compareDeep(reasonCode, o.reasonCode, true) && compareDeep(reasonReference, o.reasonReference, true)
           && compareDeep(goal, o.goal, true) && compareDeep(status, o.status, true) && compareDeep(statusReason, o.statusReason, true)
           && compareDeep(prohibited, o.prohibited, true) && compareDeep(scheduled, o.scheduled, true) && compareDeep(location, o.location, true)
           && compareDeep(performer, o.performer, true) && compareDeep(product, o.product, true) && compareDeep(dailyAmount, o.dailyAmount, true)
           && compareDeep(quantity, o.quantity, true) && compareDeep(description, o.description, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof CarePlanActivityDetailComponent))
          return false;
        CarePlanActivityDetailComponent o = (CarePlanActivityDetailComponent) other;
        return compareValues(status, o.status, true) && compareValues(statusReason, o.statusReason, true) && compareValues(prohibited, o.prohibited, true)
           && compareValues(description, o.description, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(category, definition, code
          , reasonCode, reasonReference, goal, status, statusReason, prohibited, scheduled
          , location, performer, product, dailyAmount, quantity, description);
      }

  public String fhirType() {
    return "CarePlan.activity.detail";

  }

  }

    /**
     * This records identifiers associated with this care plan that are defined by business processes and/or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation).
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="External Ids for this plan", formalDefinition="This records identifiers associated with this care plan that are defined by business processes and/or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)." )
    protected List<Identifier> identifier;

    /**
     * Identifies the protocol, questionnaire, guideline or other specification the care plan should be conducted in accordance with.
     */
    @Child(name = "definition", type = {PlanDefinition.class, Questionnaire.class}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Protocol or definition", formalDefinition="Identifies the protocol, questionnaire, guideline or other specification the care plan should be conducted in accordance with." )
    protected List<Reference> definition;
    /**
     * The actual objects that are the target of the reference (Identifies the protocol, questionnaire, guideline or other specification the care plan should be conducted in accordance with.)
     */
    protected List<Resource> definitionTarget;


    /**
     * A care plan that is fulfilled in whole or in part by this care plan.
     */
    @Child(name = "basedOn", type = {CarePlan.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Fulfills care plan", formalDefinition="A care plan that is fulfilled in whole or in part by this care plan." )
    protected List<Reference> basedOn;
    /**
     * The actual objects that are the target of the reference (A care plan that is fulfilled in whole or in part by this care plan.)
     */
    protected List<CarePlan> basedOnTarget;


    /**
     * Completed or terminated care plan whose function is taken by this new care plan.
     */
    @Child(name = "replaces", type = {CarePlan.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="CarePlan replaced by this CarePlan", formalDefinition="Completed or terminated care plan whose function is taken by this new care plan." )
    protected List<Reference> replaces;
    /**
     * The actual objects that are the target of the reference (Completed or terminated care plan whose function is taken by this new care plan.)
     */
    protected List<CarePlan> replacesTarget;


    /**
     * A larger care plan of which this particular care plan is a component or step.
     */
    @Child(name = "partOf", type = {CarePlan.class}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Part of referenced CarePlan", formalDefinition="A larger care plan of which this particular care plan is a component or step." )
    protected List<Reference> partOf;
    /**
     * The actual objects that are the target of the reference (A larger care plan of which this particular care plan is a component or step.)
     */
    protected List<CarePlan> partOfTarget;


    /**
     * Indicates whether the plan is currently being acted upon, represents future intentions or is now a historical record.
     */
    @Child(name = "status", type = {CodeType.class}, order=5, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="draft | active | suspended | completed | entered-in-error | cancelled | unknown", formalDefinition="Indicates whether the plan is currently being acted upon, represents future intentions or is now a historical record." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/care-plan-status")
    protected Enumeration<CarePlanStatus> status;

    /**
     * Indicates the level of authority/intentionality associated with the care plan and where the care plan fits into the workflow chain.
     */
    @Child(name = "intent", type = {CodeType.class}, order=6, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="proposal | plan | order | option", formalDefinition="Indicates the level of authority/intentionality associated with the care plan and where the care plan fits into the workflow chain." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/care-plan-intent")
    protected Enumeration<CarePlanIntent> intent;

    /**
     * Identifies what "kind" of plan this is to support differentiation between multiple co-existing plans; e.g. "Home health", "psychiatric", "asthma", "disease management", "wellness plan", etc.
     */
    @Child(name = "category", type = {CodeableConcept.class}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Type of plan", formalDefinition="Identifies what \"kind\" of plan this is to support differentiation between multiple co-existing plans; e.g. \"Home health\", \"psychiatric\", \"asthma\", \"disease management\", \"wellness plan\", etc." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/care-plan-category")
    protected List<CodeableConcept> category;

    /**
     * Human-friendly name for the CarePlan.
     */
    @Child(name = "title", type = {StringType.class}, order=8, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Human-friendly name for the CarePlan", formalDefinition="Human-friendly name for the CarePlan." )
    protected StringType title;

    /**
     * A description of the scope and nature of the plan.
     */
    @Child(name = "description", type = {StringType.class}, order=9, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Summary of nature of plan", formalDefinition="A description of the scope and nature of the plan." )
    protected StringType description;

    /**
     * Identifies the patient or group whose intended care is described by the plan.
     */
    @Child(name = "subject", type = {Patient.class, Group.class}, order=10, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Who care plan is for", formalDefinition="Identifies the patient or group whose intended care is described by the plan." )
    protected Reference subject;

    /**
     * The actual object that is the target of the reference (Identifies the patient or group whose intended care is described by the plan.)
     */
    protected Resource subjectTarget;

    /**
     * Identifies the original context in which this particular CarePlan was created.
     */
    @Child(name = "context", type = {Encounter.class, EpisodeOfCare.class}, order=11, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Created in context of", formalDefinition="Identifies the original context in which this particular CarePlan was created." )
    protected Reference context;

    /**
     * The actual object that is the target of the reference (Identifies the original context in which this particular CarePlan was created.)
     */
    protected Resource contextTarget;

    /**
     * Indicates when the plan did (or is intended to) come into effect and end.
     */
    @Child(name = "period", type = {Period.class}, order=12, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Time period plan covers", formalDefinition="Indicates when the plan did (or is intended to) come into effect and end." )
    protected Period period;

    /**
     * Identifies the individual(s) or ogranization who is responsible for the content of the care plan.
     */
    @Child(name = "author", type = {Patient.class, Practitioner.class, RelatedPerson.class, Organization.class, CareTeam.class}, order=13, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Who is responsible for contents of the plan", formalDefinition="Identifies the individual(s) or ogranization who is responsible for the content of the care plan." )
    protected List<Reference> author;
    /**
     * The actual objects that are the target of the reference (Identifies the individual(s) or ogranization who is responsible for the content of the care plan.)
     */
    protected List<Resource> authorTarget;


    /**
     * Identifies all people and organizations who are expected to be involved in the care envisioned by this plan.
     */
    @Child(name = "careTeam", type = {CareTeam.class}, order=14, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Who's involved in plan?", formalDefinition="Identifies all people and organizations who are expected to be involved in the care envisioned by this plan." )
    protected List<Reference> careTeam;
    /**
     * The actual objects that are the target of the reference (Identifies all people and organizations who are expected to be involved in the care envisioned by this plan.)
     */
    protected List<CareTeam> careTeamTarget;


    /**
     * Identifies the conditions/problems/concerns/diagnoses/etc. whose management and/or mitigation are handled by this plan.
     */
    @Child(name = "addresses", type = {Condition.class}, order=15, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Health issues this plan addresses", formalDefinition="Identifies the conditions/problems/concerns/diagnoses/etc. whose management and/or mitigation are handled by this plan." )
    protected List<Reference> addresses;
    /**
     * The actual objects that are the target of the reference (Identifies the conditions/problems/concerns/diagnoses/etc. whose management and/or mitigation are handled by this plan.)
     */
    protected List<Condition> addressesTarget;


    /**
     * Identifies portions of the patient's record that specifically influenced the formation of the plan.  These might include co-morbidities, recent procedures, limitations, recent assessments, etc.
     */
    @Child(name = "supportingInfo", type = {Reference.class}, order=16, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Information considered as part of plan", formalDefinition="Identifies portions of the patient's record that specifically influenced the formation of the plan.  These might include co-morbidities, recent procedures, limitations, recent assessments, etc." )
    protected List<Reference> supportingInfo;
    /**
     * The actual objects that are the target of the reference (Identifies portions of the patient's record that specifically influenced the formation of the plan.  These might include co-morbidities, recent procedures, limitations, recent assessments, etc.)
     */
    protected List<Resource> supportingInfoTarget;


    /**
     * Describes the intended objective(s) of carrying out the care plan.
     */
    @Child(name = "goal", type = {Goal.class}, order=17, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Desired outcome of plan", formalDefinition="Describes the intended objective(s) of carrying out the care plan." )
    protected List<Reference> goal;
    /**
     * The actual objects that are the target of the reference (Describes the intended objective(s) of carrying out the care plan.)
     */
    protected List<Goal> goalTarget;


    /**
     * Identifies a planned action to occur as part of the plan.  For example, a medication to be used, lab tests to perform, self-monitoring, education, etc.
     */
    @Child(name = "activity", type = {}, order=18, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Action to occur as part of plan", formalDefinition="Identifies a planned action to occur as part of the plan.  For example, a medication to be used, lab tests to perform, self-monitoring, education, etc." )
    protected List<CarePlanActivityComponent> activity;

    /**
     * General notes about the care plan not covered elsewhere.
     */
    @Child(name = "note", type = {Annotation.class}, order=19, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Comments about the plan", formalDefinition="General notes about the care plan not covered elsewhere." )
    protected List<Annotation> note;

    private static final long serialVersionUID = 995943625L;

  /**
   * Constructor
   */
    public CarePlan() {
      super();
    }

  /**
   * Constructor
   */
    public CarePlan(Enumeration<CarePlanStatus> status, Enumeration<CarePlanIntent> intent, Reference subject) {
      super();
      this.status = status;
      this.intent = intent;
      this.subject = subject;
    }

    /**
     * @return {@link #identifier} (This records identifiers associated with this care plan that are defined by business processes and/or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation).)
     */
    public List<Identifier> getIdentifier() { 
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      return this.identifier;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public CarePlan setIdentifier(List<Identifier> theIdentifier) { 
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

    public CarePlan addIdentifier(Identifier t) { //3
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
     * @return {@link #definition} (Identifies the protocol, questionnaire, guideline or other specification the care plan should be conducted in accordance with.)
     */
    public List<Reference> getDefinition() { 
      if (this.definition == null)
        this.definition = new ArrayList<Reference>();
      return this.definition;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public CarePlan setDefinition(List<Reference> theDefinition) { 
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

    public CarePlan addDefinition(Reference t) { //3
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
     * @return {@link #basedOn} (A care plan that is fulfilled in whole or in part by this care plan.)
     */
    public List<Reference> getBasedOn() { 
      if (this.basedOn == null)
        this.basedOn = new ArrayList<Reference>();
      return this.basedOn;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public CarePlan setBasedOn(List<Reference> theBasedOn) { 
      this.basedOn = theBasedOn;
      return this;
    }

    public boolean hasBasedOn() { 
      if (this.basedOn == null)
        return false;
      for (Reference item : this.basedOn)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addBasedOn() { //3
      Reference t = new Reference();
      if (this.basedOn == null)
        this.basedOn = new ArrayList<Reference>();
      this.basedOn.add(t);
      return t;
    }

    public CarePlan addBasedOn(Reference t) { //3
      if (t == null)
        return this;
      if (this.basedOn == null)
        this.basedOn = new ArrayList<Reference>();
      this.basedOn.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #basedOn}, creating it if it does not already exist
     */
    public Reference getBasedOnFirstRep() { 
      if (getBasedOn().isEmpty()) {
        addBasedOn();
      }
      return getBasedOn().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<CarePlan> getBasedOnTarget() { 
      if (this.basedOnTarget == null)
        this.basedOnTarget = new ArrayList<CarePlan>();
      return this.basedOnTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public CarePlan addBasedOnTarget() { 
      CarePlan r = new CarePlan();
      if (this.basedOnTarget == null)
        this.basedOnTarget = new ArrayList<CarePlan>();
      this.basedOnTarget.add(r);
      return r;
    }

    /**
     * @return {@link #replaces} (Completed or terminated care plan whose function is taken by this new care plan.)
     */
    public List<Reference> getReplaces() { 
      if (this.replaces == null)
        this.replaces = new ArrayList<Reference>();
      return this.replaces;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public CarePlan setReplaces(List<Reference> theReplaces) { 
      this.replaces = theReplaces;
      return this;
    }

    public boolean hasReplaces() { 
      if (this.replaces == null)
        return false;
      for (Reference item : this.replaces)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addReplaces() { //3
      Reference t = new Reference();
      if (this.replaces == null)
        this.replaces = new ArrayList<Reference>();
      this.replaces.add(t);
      return t;
    }

    public CarePlan addReplaces(Reference t) { //3
      if (t == null)
        return this;
      if (this.replaces == null)
        this.replaces = new ArrayList<Reference>();
      this.replaces.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #replaces}, creating it if it does not already exist
     */
    public Reference getReplacesFirstRep() { 
      if (getReplaces().isEmpty()) {
        addReplaces();
      }
      return getReplaces().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<CarePlan> getReplacesTarget() { 
      if (this.replacesTarget == null)
        this.replacesTarget = new ArrayList<CarePlan>();
      return this.replacesTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public CarePlan addReplacesTarget() { 
      CarePlan r = new CarePlan();
      if (this.replacesTarget == null)
        this.replacesTarget = new ArrayList<CarePlan>();
      this.replacesTarget.add(r);
      return r;
    }

    /**
     * @return {@link #partOf} (A larger care plan of which this particular care plan is a component or step.)
     */
    public List<Reference> getPartOf() { 
      if (this.partOf == null)
        this.partOf = new ArrayList<Reference>();
      return this.partOf;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public CarePlan setPartOf(List<Reference> thePartOf) { 
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

    public CarePlan addPartOf(Reference t) { //3
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
    public List<CarePlan> getPartOfTarget() { 
      if (this.partOfTarget == null)
        this.partOfTarget = new ArrayList<CarePlan>();
      return this.partOfTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public CarePlan addPartOfTarget() { 
      CarePlan r = new CarePlan();
      if (this.partOfTarget == null)
        this.partOfTarget = new ArrayList<CarePlan>();
      this.partOfTarget.add(r);
      return r;
    }

    /**
     * @return {@link #status} (Indicates whether the plan is currently being acted upon, represents future intentions or is now a historical record.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<CarePlanStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CarePlan.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<CarePlanStatus>(new CarePlanStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (Indicates whether the plan is currently being acted upon, represents future intentions or is now a historical record.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public CarePlan setStatusElement(Enumeration<CarePlanStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return Indicates whether the plan is currently being acted upon, represents future intentions or is now a historical record.
     */
    public CarePlanStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value Indicates whether the plan is currently being acted upon, represents future intentions or is now a historical record.
     */
    public CarePlan setStatus(CarePlanStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<CarePlanStatus>(new CarePlanStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #intent} (Indicates the level of authority/intentionality associated with the care plan and where the care plan fits into the workflow chain.). This is the underlying object with id, value and extensions. The accessor "getIntent" gives direct access to the value
     */
    public Enumeration<CarePlanIntent> getIntentElement() { 
      if (this.intent == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CarePlan.intent");
        else if (Configuration.doAutoCreate())
          this.intent = new Enumeration<CarePlanIntent>(new CarePlanIntentEnumFactory()); // bb
      return this.intent;
    }

    public boolean hasIntentElement() { 
      return this.intent != null && !this.intent.isEmpty();
    }

    public boolean hasIntent() { 
      return this.intent != null && !this.intent.isEmpty();
    }

    /**
     * @param value {@link #intent} (Indicates the level of authority/intentionality associated with the care plan and where the care plan fits into the workflow chain.). This is the underlying object with id, value and extensions. The accessor "getIntent" gives direct access to the value
     */
    public CarePlan setIntentElement(Enumeration<CarePlanIntent> value) { 
      this.intent = value;
      return this;
    }

    /**
     * @return Indicates the level of authority/intentionality associated with the care plan and where the care plan fits into the workflow chain.
     */
    public CarePlanIntent getIntent() { 
      return this.intent == null ? null : this.intent.getValue();
    }

    /**
     * @param value Indicates the level of authority/intentionality associated with the care plan and where the care plan fits into the workflow chain.
     */
    public CarePlan setIntent(CarePlanIntent value) { 
        if (this.intent == null)
          this.intent = new Enumeration<CarePlanIntent>(new CarePlanIntentEnumFactory());
        this.intent.setValue(value);
      return this;
    }

    /**
     * @return {@link #category} (Identifies what "kind" of plan this is to support differentiation between multiple co-existing plans; e.g. "Home health", "psychiatric", "asthma", "disease management", "wellness plan", etc.)
     */
    public List<CodeableConcept> getCategory() { 
      if (this.category == null)
        this.category = new ArrayList<CodeableConcept>();
      return this.category;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public CarePlan setCategory(List<CodeableConcept> theCategory) { 
      this.category = theCategory;
      return this;
    }

    public boolean hasCategory() { 
      if (this.category == null)
        return false;
      for (CodeableConcept item : this.category)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addCategory() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.category == null)
        this.category = new ArrayList<CodeableConcept>();
      this.category.add(t);
      return t;
    }

    public CarePlan addCategory(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.category == null)
        this.category = new ArrayList<CodeableConcept>();
      this.category.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #category}, creating it if it does not already exist
     */
    public CodeableConcept getCategoryFirstRep() { 
      if (getCategory().isEmpty()) {
        addCategory();
      }
      return getCategory().get(0);
    }

    /**
     * @return {@link #title} (Human-friendly name for the CarePlan.). This is the underlying object with id, value and extensions. The accessor "getTitle" gives direct access to the value
     */
    public StringType getTitleElement() { 
      if (this.title == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CarePlan.title");
        else if (Configuration.doAutoCreate())
          this.title = new StringType(); // bb
      return this.title;
    }

    public boolean hasTitleElement() { 
      return this.title != null && !this.title.isEmpty();
    }

    public boolean hasTitle() { 
      return this.title != null && !this.title.isEmpty();
    }

    /**
     * @param value {@link #title} (Human-friendly name for the CarePlan.). This is the underlying object with id, value and extensions. The accessor "getTitle" gives direct access to the value
     */
    public CarePlan setTitleElement(StringType value) { 
      this.title = value;
      return this;
    }

    /**
     * @return Human-friendly name for the CarePlan.
     */
    public String getTitle() { 
      return this.title == null ? null : this.title.getValue();
    }

    /**
     * @param value Human-friendly name for the CarePlan.
     */
    public CarePlan setTitle(String value) { 
      if (Utilities.noString(value))
        this.title = null;
      else {
        if (this.title == null)
          this.title = new StringType();
        this.title.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #description} (A description of the scope and nature of the plan.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public StringType getDescriptionElement() { 
      if (this.description == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CarePlan.description");
        else if (Configuration.doAutoCreate())
          this.description = new StringType(); // bb
      return this.description;
    }

    public boolean hasDescriptionElement() { 
      return this.description != null && !this.description.isEmpty();
    }

    public boolean hasDescription() { 
      return this.description != null && !this.description.isEmpty();
    }

    /**
     * @param value {@link #description} (A description of the scope and nature of the plan.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public CarePlan setDescriptionElement(StringType value) { 
      this.description = value;
      return this;
    }

    /**
     * @return A description of the scope and nature of the plan.
     */
    public String getDescription() { 
      return this.description == null ? null : this.description.getValue();
    }

    /**
     * @param value A description of the scope and nature of the plan.
     */
    public CarePlan setDescription(String value) { 
      if (Utilities.noString(value))
        this.description = null;
      else {
        if (this.description == null)
          this.description = new StringType();
        this.description.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #subject} (Identifies the patient or group whose intended care is described by the plan.)
     */
    public Reference getSubject() { 
      if (this.subject == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CarePlan.subject");
        else if (Configuration.doAutoCreate())
          this.subject = new Reference(); // cc
      return this.subject;
    }

    public boolean hasSubject() { 
      return this.subject != null && !this.subject.isEmpty();
    }

    /**
     * @param value {@link #subject} (Identifies the patient or group whose intended care is described by the plan.)
     */
    public CarePlan setSubject(Reference value) { 
      this.subject = value;
      return this;
    }

    /**
     * @return {@link #subject} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Identifies the patient or group whose intended care is described by the plan.)
     */
    public Resource getSubjectTarget() { 
      return this.subjectTarget;
    }

    /**
     * @param value {@link #subject} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Identifies the patient or group whose intended care is described by the plan.)
     */
    public CarePlan setSubjectTarget(Resource value) { 
      this.subjectTarget = value;
      return this;
    }

    /**
     * @return {@link #context} (Identifies the original context in which this particular CarePlan was created.)
     */
    public Reference getContext() { 
      if (this.context == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CarePlan.context");
        else if (Configuration.doAutoCreate())
          this.context = new Reference(); // cc
      return this.context;
    }

    public boolean hasContext() { 
      return this.context != null && !this.context.isEmpty();
    }

    /**
     * @param value {@link #context} (Identifies the original context in which this particular CarePlan was created.)
     */
    public CarePlan setContext(Reference value) { 
      this.context = value;
      return this;
    }

    /**
     * @return {@link #context} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Identifies the original context in which this particular CarePlan was created.)
     */
    public Resource getContextTarget() { 
      return this.contextTarget;
    }

    /**
     * @param value {@link #context} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Identifies the original context in which this particular CarePlan was created.)
     */
    public CarePlan setContextTarget(Resource value) { 
      this.contextTarget = value;
      return this;
    }

    /**
     * @return {@link #period} (Indicates when the plan did (or is intended to) come into effect and end.)
     */
    public Period getPeriod() { 
      if (this.period == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CarePlan.period");
        else if (Configuration.doAutoCreate())
          this.period = new Period(); // cc
      return this.period;
    }

    public boolean hasPeriod() { 
      return this.period != null && !this.period.isEmpty();
    }

    /**
     * @param value {@link #period} (Indicates when the plan did (or is intended to) come into effect and end.)
     */
    public CarePlan setPeriod(Period value) { 
      this.period = value;
      return this;
    }

    /**
     * @return {@link #author} (Identifies the individual(s) or ogranization who is responsible for the content of the care plan.)
     */
    public List<Reference> getAuthor() { 
      if (this.author == null)
        this.author = new ArrayList<Reference>();
      return this.author;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public CarePlan setAuthor(List<Reference> theAuthor) { 
      this.author = theAuthor;
      return this;
    }

    public boolean hasAuthor() { 
      if (this.author == null)
        return false;
      for (Reference item : this.author)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addAuthor() { //3
      Reference t = new Reference();
      if (this.author == null)
        this.author = new ArrayList<Reference>();
      this.author.add(t);
      return t;
    }

    public CarePlan addAuthor(Reference t) { //3
      if (t == null)
        return this;
      if (this.author == null)
        this.author = new ArrayList<Reference>();
      this.author.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #author}, creating it if it does not already exist
     */
    public Reference getAuthorFirstRep() { 
      if (getAuthor().isEmpty()) {
        addAuthor();
      }
      return getAuthor().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Resource> getAuthorTarget() { 
      if (this.authorTarget == null)
        this.authorTarget = new ArrayList<Resource>();
      return this.authorTarget;
    }

    /**
     * @return {@link #careTeam} (Identifies all people and organizations who are expected to be involved in the care envisioned by this plan.)
     */
    public List<Reference> getCareTeam() { 
      if (this.careTeam == null)
        this.careTeam = new ArrayList<Reference>();
      return this.careTeam;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public CarePlan setCareTeam(List<Reference> theCareTeam) { 
      this.careTeam = theCareTeam;
      return this;
    }

    public boolean hasCareTeam() { 
      if (this.careTeam == null)
        return false;
      for (Reference item : this.careTeam)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addCareTeam() { //3
      Reference t = new Reference();
      if (this.careTeam == null)
        this.careTeam = new ArrayList<Reference>();
      this.careTeam.add(t);
      return t;
    }

    public CarePlan addCareTeam(Reference t) { //3
      if (t == null)
        return this;
      if (this.careTeam == null)
        this.careTeam = new ArrayList<Reference>();
      this.careTeam.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #careTeam}, creating it if it does not already exist
     */
    public Reference getCareTeamFirstRep() { 
      if (getCareTeam().isEmpty()) {
        addCareTeam();
      }
      return getCareTeam().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<CareTeam> getCareTeamTarget() { 
      if (this.careTeamTarget == null)
        this.careTeamTarget = new ArrayList<CareTeam>();
      return this.careTeamTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public CareTeam addCareTeamTarget() { 
      CareTeam r = new CareTeam();
      if (this.careTeamTarget == null)
        this.careTeamTarget = new ArrayList<CareTeam>();
      this.careTeamTarget.add(r);
      return r;
    }

    /**
     * @return {@link #addresses} (Identifies the conditions/problems/concerns/diagnoses/etc. whose management and/or mitigation are handled by this plan.)
     */
    public List<Reference> getAddresses() { 
      if (this.addresses == null)
        this.addresses = new ArrayList<Reference>();
      return this.addresses;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public CarePlan setAddresses(List<Reference> theAddresses) { 
      this.addresses = theAddresses;
      return this;
    }

    public boolean hasAddresses() { 
      if (this.addresses == null)
        return false;
      for (Reference item : this.addresses)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addAddresses() { //3
      Reference t = new Reference();
      if (this.addresses == null)
        this.addresses = new ArrayList<Reference>();
      this.addresses.add(t);
      return t;
    }

    public CarePlan addAddresses(Reference t) { //3
      if (t == null)
        return this;
      if (this.addresses == null)
        this.addresses = new ArrayList<Reference>();
      this.addresses.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #addresses}, creating it if it does not already exist
     */
    public Reference getAddressesFirstRep() { 
      if (getAddresses().isEmpty()) {
        addAddresses();
      }
      return getAddresses().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Condition> getAddressesTarget() { 
      if (this.addressesTarget == null)
        this.addressesTarget = new ArrayList<Condition>();
      return this.addressesTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public Condition addAddressesTarget() { 
      Condition r = new Condition();
      if (this.addressesTarget == null)
        this.addressesTarget = new ArrayList<Condition>();
      this.addressesTarget.add(r);
      return r;
    }

    /**
     * @return {@link #supportingInfo} (Identifies portions of the patient's record that specifically influenced the formation of the plan.  These might include co-morbidities, recent procedures, limitations, recent assessments, etc.)
     */
    public List<Reference> getSupportingInfo() { 
      if (this.supportingInfo == null)
        this.supportingInfo = new ArrayList<Reference>();
      return this.supportingInfo;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public CarePlan setSupportingInfo(List<Reference> theSupportingInfo) { 
      this.supportingInfo = theSupportingInfo;
      return this;
    }

    public boolean hasSupportingInfo() { 
      if (this.supportingInfo == null)
        return false;
      for (Reference item : this.supportingInfo)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addSupportingInfo() { //3
      Reference t = new Reference();
      if (this.supportingInfo == null)
        this.supportingInfo = new ArrayList<Reference>();
      this.supportingInfo.add(t);
      return t;
    }

    public CarePlan addSupportingInfo(Reference t) { //3
      if (t == null)
        return this;
      if (this.supportingInfo == null)
        this.supportingInfo = new ArrayList<Reference>();
      this.supportingInfo.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #supportingInfo}, creating it if it does not already exist
     */
    public Reference getSupportingInfoFirstRep() { 
      if (getSupportingInfo().isEmpty()) {
        addSupportingInfo();
      }
      return getSupportingInfo().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Resource> getSupportingInfoTarget() { 
      if (this.supportingInfoTarget == null)
        this.supportingInfoTarget = new ArrayList<Resource>();
      return this.supportingInfoTarget;
    }

    /**
     * @return {@link #goal} (Describes the intended objective(s) of carrying out the care plan.)
     */
    public List<Reference> getGoal() { 
      if (this.goal == null)
        this.goal = new ArrayList<Reference>();
      return this.goal;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public CarePlan setGoal(List<Reference> theGoal) { 
      this.goal = theGoal;
      return this;
    }

    public boolean hasGoal() { 
      if (this.goal == null)
        return false;
      for (Reference item : this.goal)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addGoal() { //3
      Reference t = new Reference();
      if (this.goal == null)
        this.goal = new ArrayList<Reference>();
      this.goal.add(t);
      return t;
    }

    public CarePlan addGoal(Reference t) { //3
      if (t == null)
        return this;
      if (this.goal == null)
        this.goal = new ArrayList<Reference>();
      this.goal.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #goal}, creating it if it does not already exist
     */
    public Reference getGoalFirstRep() { 
      if (getGoal().isEmpty()) {
        addGoal();
      }
      return getGoal().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Goal> getGoalTarget() { 
      if (this.goalTarget == null)
        this.goalTarget = new ArrayList<Goal>();
      return this.goalTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public Goal addGoalTarget() { 
      Goal r = new Goal();
      if (this.goalTarget == null)
        this.goalTarget = new ArrayList<Goal>();
      this.goalTarget.add(r);
      return r;
    }

    /**
     * @return {@link #activity} (Identifies a planned action to occur as part of the plan.  For example, a medication to be used, lab tests to perform, self-monitoring, education, etc.)
     */
    public List<CarePlanActivityComponent> getActivity() { 
      if (this.activity == null)
        this.activity = new ArrayList<CarePlanActivityComponent>();
      return this.activity;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public CarePlan setActivity(List<CarePlanActivityComponent> theActivity) { 
      this.activity = theActivity;
      return this;
    }

    public boolean hasActivity() { 
      if (this.activity == null)
        return false;
      for (CarePlanActivityComponent item : this.activity)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CarePlanActivityComponent addActivity() { //3
      CarePlanActivityComponent t = new CarePlanActivityComponent();
      if (this.activity == null)
        this.activity = new ArrayList<CarePlanActivityComponent>();
      this.activity.add(t);
      return t;
    }

    public CarePlan addActivity(CarePlanActivityComponent t) { //3
      if (t == null)
        return this;
      if (this.activity == null)
        this.activity = new ArrayList<CarePlanActivityComponent>();
      this.activity.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #activity}, creating it if it does not already exist
     */
    public CarePlanActivityComponent getActivityFirstRep() { 
      if (getActivity().isEmpty()) {
        addActivity();
      }
      return getActivity().get(0);
    }

    /**
     * @return {@link #note} (General notes about the care plan not covered elsewhere.)
     */
    public List<Annotation> getNote() { 
      if (this.note == null)
        this.note = new ArrayList<Annotation>();
      return this.note;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public CarePlan setNote(List<Annotation> theNote) { 
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

    public CarePlan addNote(Annotation t) { //3
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

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "This records identifiers associated with this care plan that are defined by business processes and/or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation).", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("definition", "Reference(PlanDefinition|Questionnaire)", "Identifies the protocol, questionnaire, guideline or other specification the care plan should be conducted in accordance with.", 0, java.lang.Integer.MAX_VALUE, definition));
        childrenList.add(new Property("basedOn", "Reference(CarePlan)", "A care plan that is fulfilled in whole or in part by this care plan.", 0, java.lang.Integer.MAX_VALUE, basedOn));
        childrenList.add(new Property("replaces", "Reference(CarePlan)", "Completed or terminated care plan whose function is taken by this new care plan.", 0, java.lang.Integer.MAX_VALUE, replaces));
        childrenList.add(new Property("partOf", "Reference(CarePlan)", "A larger care plan of which this particular care plan is a component or step.", 0, java.lang.Integer.MAX_VALUE, partOf));
        childrenList.add(new Property("status", "code", "Indicates whether the plan is currently being acted upon, represents future intentions or is now a historical record.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("intent", "code", "Indicates the level of authority/intentionality associated with the care plan and where the care plan fits into the workflow chain.", 0, java.lang.Integer.MAX_VALUE, intent));
        childrenList.add(new Property("category", "CodeableConcept", "Identifies what \"kind\" of plan this is to support differentiation between multiple co-existing plans; e.g. \"Home health\", \"psychiatric\", \"asthma\", \"disease management\", \"wellness plan\", etc.", 0, java.lang.Integer.MAX_VALUE, category));
        childrenList.add(new Property("title", "string", "Human-friendly name for the CarePlan.", 0, java.lang.Integer.MAX_VALUE, title));
        childrenList.add(new Property("description", "string", "A description of the scope and nature of the plan.", 0, java.lang.Integer.MAX_VALUE, description));
        childrenList.add(new Property("subject", "Reference(Patient|Group)", "Identifies the patient or group whose intended care is described by the plan.", 0, java.lang.Integer.MAX_VALUE, subject));
        childrenList.add(new Property("context", "Reference(Encounter|EpisodeOfCare)", "Identifies the original context in which this particular CarePlan was created.", 0, java.lang.Integer.MAX_VALUE, context));
        childrenList.add(new Property("period", "Period", "Indicates when the plan did (or is intended to) come into effect and end.", 0, java.lang.Integer.MAX_VALUE, period));
        childrenList.add(new Property("author", "Reference(Patient|Practitioner|RelatedPerson|Organization|CareTeam)", "Identifies the individual(s) or ogranization who is responsible for the content of the care plan.", 0, java.lang.Integer.MAX_VALUE, author));
        childrenList.add(new Property("careTeam", "Reference(CareTeam)", "Identifies all people and organizations who are expected to be involved in the care envisioned by this plan.", 0, java.lang.Integer.MAX_VALUE, careTeam));
        childrenList.add(new Property("addresses", "Reference(Condition)", "Identifies the conditions/problems/concerns/diagnoses/etc. whose management and/or mitigation are handled by this plan.", 0, java.lang.Integer.MAX_VALUE, addresses));
        childrenList.add(new Property("supportingInfo", "Reference(Any)", "Identifies portions of the patient's record that specifically influenced the formation of the plan.  These might include co-morbidities, recent procedures, limitations, recent assessments, etc.", 0, java.lang.Integer.MAX_VALUE, supportingInfo));
        childrenList.add(new Property("goal", "Reference(Goal)", "Describes the intended objective(s) of carrying out the care plan.", 0, java.lang.Integer.MAX_VALUE, goal));
        childrenList.add(new Property("activity", "", "Identifies a planned action to occur as part of the plan.  For example, a medication to be used, lab tests to perform, self-monitoring, education, etc.", 0, java.lang.Integer.MAX_VALUE, activity));
        childrenList.add(new Property("note", "Annotation", "General notes about the care plan not covered elsewhere.", 0, java.lang.Integer.MAX_VALUE, note));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case -1014418093: /*definition*/ return this.definition == null ? new Base[0] : this.definition.toArray(new Base[this.definition.size()]); // Reference
        case -332612366: /*basedOn*/ return this.basedOn == null ? new Base[0] : this.basedOn.toArray(new Base[this.basedOn.size()]); // Reference
        case -430332865: /*replaces*/ return this.replaces == null ? new Base[0] : this.replaces.toArray(new Base[this.replaces.size()]); // Reference
        case -995410646: /*partOf*/ return this.partOf == null ? new Base[0] : this.partOf.toArray(new Base[this.partOf.size()]); // Reference
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<CarePlanStatus>
        case -1183762788: /*intent*/ return this.intent == null ? new Base[0] : new Base[] {this.intent}; // Enumeration<CarePlanIntent>
        case 50511102: /*category*/ return this.category == null ? new Base[0] : this.category.toArray(new Base[this.category.size()]); // CodeableConcept
        case 110371416: /*title*/ return this.title == null ? new Base[0] : new Base[] {this.title}; // StringType
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // StringType
        case -1867885268: /*subject*/ return this.subject == null ? new Base[0] : new Base[] {this.subject}; // Reference
        case 951530927: /*context*/ return this.context == null ? new Base[0] : new Base[] {this.context}; // Reference
        case -991726143: /*period*/ return this.period == null ? new Base[0] : new Base[] {this.period}; // Period
        case -1406328437: /*author*/ return this.author == null ? new Base[0] : this.author.toArray(new Base[this.author.size()]); // Reference
        case -7323378: /*careTeam*/ return this.careTeam == null ? new Base[0] : this.careTeam.toArray(new Base[this.careTeam.size()]); // Reference
        case 874544034: /*addresses*/ return this.addresses == null ? new Base[0] : this.addresses.toArray(new Base[this.addresses.size()]); // Reference
        case 1922406657: /*supportingInfo*/ return this.supportingInfo == null ? new Base[0] : this.supportingInfo.toArray(new Base[this.supportingInfo.size()]); // Reference
        case 3178259: /*goal*/ return this.goal == null ? new Base[0] : this.goal.toArray(new Base[this.goal.size()]); // Reference
        case -1655966961: /*activity*/ return this.activity == null ? new Base[0] : this.activity.toArray(new Base[this.activity.size()]); // CarePlanActivityComponent
        case 3387378: /*note*/ return this.note == null ? new Base[0] : this.note.toArray(new Base[this.note.size()]); // Annotation
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
        case -332612366: // basedOn
          this.getBasedOn().add(castToReference(value)); // Reference
          return value;
        case -430332865: // replaces
          this.getReplaces().add(castToReference(value)); // Reference
          return value;
        case -995410646: // partOf
          this.getPartOf().add(castToReference(value)); // Reference
          return value;
        case -892481550: // status
          value = new CarePlanStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<CarePlanStatus>
          return value;
        case -1183762788: // intent
          value = new CarePlanIntentEnumFactory().fromType(castToCode(value));
          this.intent = (Enumeration) value; // Enumeration<CarePlanIntent>
          return value;
        case 50511102: // category
          this.getCategory().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 110371416: // title
          this.title = castToString(value); // StringType
          return value;
        case -1724546052: // description
          this.description = castToString(value); // StringType
          return value;
        case -1867885268: // subject
          this.subject = castToReference(value); // Reference
          return value;
        case 951530927: // context
          this.context = castToReference(value); // Reference
          return value;
        case -991726143: // period
          this.period = castToPeriod(value); // Period
          return value;
        case -1406328437: // author
          this.getAuthor().add(castToReference(value)); // Reference
          return value;
        case -7323378: // careTeam
          this.getCareTeam().add(castToReference(value)); // Reference
          return value;
        case 874544034: // addresses
          this.getAddresses().add(castToReference(value)); // Reference
          return value;
        case 1922406657: // supportingInfo
          this.getSupportingInfo().add(castToReference(value)); // Reference
          return value;
        case 3178259: // goal
          this.getGoal().add(castToReference(value)); // Reference
          return value;
        case -1655966961: // activity
          this.getActivity().add((CarePlanActivityComponent) value); // CarePlanActivityComponent
          return value;
        case 3387378: // note
          this.getNote().add(castToAnnotation(value)); // Annotation
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
        } else if (name.equals("basedOn")) {
          this.getBasedOn().add(castToReference(value));
        } else if (name.equals("replaces")) {
          this.getReplaces().add(castToReference(value));
        } else if (name.equals("partOf")) {
          this.getPartOf().add(castToReference(value));
        } else if (name.equals("status")) {
          value = new CarePlanStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<CarePlanStatus>
        } else if (name.equals("intent")) {
          value = new CarePlanIntentEnumFactory().fromType(castToCode(value));
          this.intent = (Enumeration) value; // Enumeration<CarePlanIntent>
        } else if (name.equals("category")) {
          this.getCategory().add(castToCodeableConcept(value));
        } else if (name.equals("title")) {
          this.title = castToString(value); // StringType
        } else if (name.equals("description")) {
          this.description = castToString(value); // StringType
        } else if (name.equals("subject")) {
          this.subject = castToReference(value); // Reference
        } else if (name.equals("context")) {
          this.context = castToReference(value); // Reference
        } else if (name.equals("period")) {
          this.period = castToPeriod(value); // Period
        } else if (name.equals("author")) {
          this.getAuthor().add(castToReference(value));
        } else if (name.equals("careTeam")) {
          this.getCareTeam().add(castToReference(value));
        } else if (name.equals("addresses")) {
          this.getAddresses().add(castToReference(value));
        } else if (name.equals("supportingInfo")) {
          this.getSupportingInfo().add(castToReference(value));
        } else if (name.equals("goal")) {
          this.getGoal().add(castToReference(value));
        } else if (name.equals("activity")) {
          this.getActivity().add((CarePlanActivityComponent) value);
        } else if (name.equals("note")) {
          this.getNote().add(castToAnnotation(value));
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return addIdentifier(); 
        case -1014418093:  return addDefinition(); 
        case -332612366:  return addBasedOn(); 
        case -430332865:  return addReplaces(); 
        case -995410646:  return addPartOf(); 
        case -892481550:  return getStatusElement();
        case -1183762788:  return getIntentElement();
        case 50511102:  return addCategory(); 
        case 110371416:  return getTitleElement();
        case -1724546052:  return getDescriptionElement();
        case -1867885268:  return getSubject(); 
        case 951530927:  return getContext(); 
        case -991726143:  return getPeriod(); 
        case -1406328437:  return addAuthor(); 
        case -7323378:  return addCareTeam(); 
        case 874544034:  return addAddresses(); 
        case 1922406657:  return addSupportingInfo(); 
        case 3178259:  return addGoal(); 
        case -1655966961:  return addActivity(); 
        case 3387378:  return addNote(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case -1014418093: /*definition*/ return new String[] {"Reference"};
        case -332612366: /*basedOn*/ return new String[] {"Reference"};
        case -430332865: /*replaces*/ return new String[] {"Reference"};
        case -995410646: /*partOf*/ return new String[] {"Reference"};
        case -892481550: /*status*/ return new String[] {"code"};
        case -1183762788: /*intent*/ return new String[] {"code"};
        case 50511102: /*category*/ return new String[] {"CodeableConcept"};
        case 110371416: /*title*/ return new String[] {"string"};
        case -1724546052: /*description*/ return new String[] {"string"};
        case -1867885268: /*subject*/ return new String[] {"Reference"};
        case 951530927: /*context*/ return new String[] {"Reference"};
        case -991726143: /*period*/ return new String[] {"Period"};
        case -1406328437: /*author*/ return new String[] {"Reference"};
        case -7323378: /*careTeam*/ return new String[] {"Reference"};
        case 874544034: /*addresses*/ return new String[] {"Reference"};
        case 1922406657: /*supportingInfo*/ return new String[] {"Reference"};
        case 3178259: /*goal*/ return new String[] {"Reference"};
        case -1655966961: /*activity*/ return new String[] {};
        case 3387378: /*note*/ return new String[] {"Annotation"};
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
        else if (name.equals("basedOn")) {
          return addBasedOn();
        }
        else if (name.equals("replaces")) {
          return addReplaces();
        }
        else if (name.equals("partOf")) {
          return addPartOf();
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type CarePlan.status");
        }
        else if (name.equals("intent")) {
          throw new FHIRException("Cannot call addChild on a primitive type CarePlan.intent");
        }
        else if (name.equals("category")) {
          return addCategory();
        }
        else if (name.equals("title")) {
          throw new FHIRException("Cannot call addChild on a primitive type CarePlan.title");
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type CarePlan.description");
        }
        else if (name.equals("subject")) {
          this.subject = new Reference();
          return this.subject;
        }
        else if (name.equals("context")) {
          this.context = new Reference();
          return this.context;
        }
        else if (name.equals("period")) {
          this.period = new Period();
          return this.period;
        }
        else if (name.equals("author")) {
          return addAuthor();
        }
        else if (name.equals("careTeam")) {
          return addCareTeam();
        }
        else if (name.equals("addresses")) {
          return addAddresses();
        }
        else if (name.equals("supportingInfo")) {
          return addSupportingInfo();
        }
        else if (name.equals("goal")) {
          return addGoal();
        }
        else if (name.equals("activity")) {
          return addActivity();
        }
        else if (name.equals("note")) {
          return addNote();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "CarePlan";

  }

      public CarePlan copy() {
        CarePlan dst = new CarePlan();
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
        if (basedOn != null) {
          dst.basedOn = new ArrayList<Reference>();
          for (Reference i : basedOn)
            dst.basedOn.add(i.copy());
        };
        if (replaces != null) {
          dst.replaces = new ArrayList<Reference>();
          for (Reference i : replaces)
            dst.replaces.add(i.copy());
        };
        if (partOf != null) {
          dst.partOf = new ArrayList<Reference>();
          for (Reference i : partOf)
            dst.partOf.add(i.copy());
        };
        dst.status = status == null ? null : status.copy();
        dst.intent = intent == null ? null : intent.copy();
        if (category != null) {
          dst.category = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : category)
            dst.category.add(i.copy());
        };
        dst.title = title == null ? null : title.copy();
        dst.description = description == null ? null : description.copy();
        dst.subject = subject == null ? null : subject.copy();
        dst.context = context == null ? null : context.copy();
        dst.period = period == null ? null : period.copy();
        if (author != null) {
          dst.author = new ArrayList<Reference>();
          for (Reference i : author)
            dst.author.add(i.copy());
        };
        if (careTeam != null) {
          dst.careTeam = new ArrayList<Reference>();
          for (Reference i : careTeam)
            dst.careTeam.add(i.copy());
        };
        if (addresses != null) {
          dst.addresses = new ArrayList<Reference>();
          for (Reference i : addresses)
            dst.addresses.add(i.copy());
        };
        if (supportingInfo != null) {
          dst.supportingInfo = new ArrayList<Reference>();
          for (Reference i : supportingInfo)
            dst.supportingInfo.add(i.copy());
        };
        if (goal != null) {
          dst.goal = new ArrayList<Reference>();
          for (Reference i : goal)
            dst.goal.add(i.copy());
        };
        if (activity != null) {
          dst.activity = new ArrayList<CarePlanActivityComponent>();
          for (CarePlanActivityComponent i : activity)
            dst.activity.add(i.copy());
        };
        if (note != null) {
          dst.note = new ArrayList<Annotation>();
          for (Annotation i : note)
            dst.note.add(i.copy());
        };
        return dst;
      }

      protected CarePlan typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof CarePlan))
          return false;
        CarePlan o = (CarePlan) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(definition, o.definition, true)
           && compareDeep(basedOn, o.basedOn, true) && compareDeep(replaces, o.replaces, true) && compareDeep(partOf, o.partOf, true)
           && compareDeep(status, o.status, true) && compareDeep(intent, o.intent, true) && compareDeep(category, o.category, true)
           && compareDeep(title, o.title, true) && compareDeep(description, o.description, true) && compareDeep(subject, o.subject, true)
           && compareDeep(context, o.context, true) && compareDeep(period, o.period, true) && compareDeep(author, o.author, true)
           && compareDeep(careTeam, o.careTeam, true) && compareDeep(addresses, o.addresses, true) && compareDeep(supportingInfo, o.supportingInfo, true)
           && compareDeep(goal, o.goal, true) && compareDeep(activity, o.activity, true) && compareDeep(note, o.note, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof CarePlan))
          return false;
        CarePlan o = (CarePlan) other;
        return compareValues(status, o.status, true) && compareValues(intent, o.intent, true) && compareValues(title, o.title, true)
           && compareValues(description, o.description, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, definition, basedOn
          , replaces, partOf, status, intent, category, title, description, subject, context
          , period, author, careTeam, addresses, supportingInfo, goal, activity, note
          );
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.CarePlan;
   }

 /**
   * Search parameter: <b>date</b>
   * <p>
   * Description: <b>Time period plan covers</b><br>
   * Type: <b>date</b><br>
   * Path: <b>CarePlan.period</b><br>
   * </p>
   */
  @SearchParamDefinition(name="date", path="CarePlan.period", description="Time period plan covers", type="date" )
  public static final String SP_DATE = "date";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>date</b>
   * <p>
   * Description: <b>Time period plan covers</b><br>
   * Type: <b>date</b><br>
   * Path: <b>CarePlan.period</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam DATE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_DATE);

 /**
   * Search parameter: <b>care-team</b>
   * <p>
   * Description: <b>Who's involved in plan?</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>CarePlan.careTeam</b><br>
   * </p>
   */
  @SearchParamDefinition(name="care-team", path="CarePlan.careTeam", description="Who's involved in plan?", type="reference", target={CareTeam.class } )
  public static final String SP_CARE_TEAM = "care-team";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>care-team</b>
   * <p>
   * Description: <b>Who's involved in plan?</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>CarePlan.careTeam</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam CARE_TEAM = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_CARE_TEAM);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>CarePlan:care-team</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_CARE_TEAM = new ca.uhn.fhir.model.api.Include("CarePlan:care-team").toLocked();

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>External Ids for this plan</b><br>
   * Type: <b>token</b><br>
   * Path: <b>CarePlan.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="CarePlan.identifier", description="External Ids for this plan", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>External Ids for this plan</b><br>
   * Type: <b>token</b><br>
   * Path: <b>CarePlan.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>performer</b>
   * <p>
   * Description: <b>Matches if the practitioner is listed as a performer in any of the "simple" activities.  (For performers of the detailed activities, chain through the activitydetail search parameter.)</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>CarePlan.activity.detail.performer</b><br>
   * </p>
   */
  @SearchParamDefinition(name="performer", path="CarePlan.activity.detail.performer", description="Matches if the practitioner is listed as a performer in any of the \"simple\" activities.  (For performers of the detailed activities, chain through the activitydetail search parameter.)", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient"), @ca.uhn.fhir.model.api.annotation.Compartment(name="Practitioner"), @ca.uhn.fhir.model.api.annotation.Compartment(name="RelatedPerson") }, target={CareTeam.class, Organization.class, Patient.class, Practitioner.class, RelatedPerson.class } )
  public static final String SP_PERFORMER = "performer";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>performer</b>
   * <p>
   * Description: <b>Matches if the practitioner is listed as a performer in any of the "simple" activities.  (For performers of the detailed activities, chain through the activitydetail search parameter.)</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>CarePlan.activity.detail.performer</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PERFORMER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PERFORMER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>CarePlan:performer</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PERFORMER = new ca.uhn.fhir.model.api.Include("CarePlan:performer").toLocked();

 /**
   * Search parameter: <b>goal</b>
   * <p>
   * Description: <b>Desired outcome of plan</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>CarePlan.goal</b><br>
   * </p>
   */
  @SearchParamDefinition(name="goal", path="CarePlan.goal", description="Desired outcome of plan", type="reference", target={Goal.class } )
  public static final String SP_GOAL = "goal";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>goal</b>
   * <p>
   * Description: <b>Desired outcome of plan</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>CarePlan.goal</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam GOAL = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_GOAL);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>CarePlan:goal</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_GOAL = new ca.uhn.fhir.model.api.Include("CarePlan:goal").toLocked();

 /**
   * Search parameter: <b>subject</b>
   * <p>
   * Description: <b>Who care plan is for</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>CarePlan.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="subject", path="CarePlan.subject", description="Who care plan is for", type="reference", target={Group.class, Patient.class } )
  public static final String SP_SUBJECT = "subject";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>subject</b>
   * <p>
   * Description: <b>Who care plan is for</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>CarePlan.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SUBJECT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SUBJECT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>CarePlan:subject</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SUBJECT = new ca.uhn.fhir.model.api.Include("CarePlan:subject").toLocked();

 /**
   * Search parameter: <b>replaces</b>
   * <p>
   * Description: <b>CarePlan replaced by this CarePlan</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>CarePlan.replaces</b><br>
   * </p>
   */
  @SearchParamDefinition(name="replaces", path="CarePlan.replaces", description="CarePlan replaced by this CarePlan", type="reference", target={CarePlan.class } )
  public static final String SP_REPLACES = "replaces";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>replaces</b>
   * <p>
   * Description: <b>CarePlan replaced by this CarePlan</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>CarePlan.replaces</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam REPLACES = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_REPLACES);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>CarePlan:replaces</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_REPLACES = new ca.uhn.fhir.model.api.Include("CarePlan:replaces").toLocked();

 /**
   * Search parameter: <b>part-of</b>
   * <p>
   * Description: <b>Part of referenced CarePlan</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>CarePlan.partOf</b><br>
   * </p>
   */
  @SearchParamDefinition(name="part-of", path="CarePlan.partOf", description="Part of referenced CarePlan", type="reference", target={CarePlan.class } )
  public static final String SP_PART_OF = "part-of";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>part-of</b>
   * <p>
   * Description: <b>Part of referenced CarePlan</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>CarePlan.partOf</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PART_OF = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PART_OF);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>CarePlan:part-of</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PART_OF = new ca.uhn.fhir.model.api.Include("CarePlan:part-of").toLocked();

 /**
   * Search parameter: <b>encounter</b>
   * <p>
   * Description: <b>Created in context of</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>CarePlan.context</b><br>
   * </p>
   */
  @SearchParamDefinition(name="encounter", path="CarePlan.context", description="Created in context of", type="reference", target={Encounter.class } )
  public static final String SP_ENCOUNTER = "encounter";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>encounter</b>
   * <p>
   * Description: <b>Created in context of</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>CarePlan.context</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam ENCOUNTER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_ENCOUNTER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>CarePlan:encounter</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_ENCOUNTER = new ca.uhn.fhir.model.api.Include("CarePlan:encounter").toLocked();

 /**
   * Search parameter: <b>intent</b>
   * <p>
   * Description: <b>proposal | plan | order | option</b><br>
   * Type: <b>token</b><br>
   * Path: <b>CarePlan.intent</b><br>
   * </p>
   */
  @SearchParamDefinition(name="intent", path="CarePlan.intent", description="proposal | plan | order | option", type="token" )
  public static final String SP_INTENT = "intent";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>intent</b>
   * <p>
   * Description: <b>proposal | plan | order | option</b><br>
   * Type: <b>token</b><br>
   * Path: <b>CarePlan.intent</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam INTENT = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_INTENT);

 /**
   * Search parameter: <b>activity-reference</b>
   * <p>
   * Description: <b>Activity details defined in specific resource</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>CarePlan.activity.reference</b><br>
   * </p>
   */
  @SearchParamDefinition(name="activity-reference", path="CarePlan.activity.reference", description="Activity details defined in specific resource", type="reference", target={Appointment.class, CommunicationRequest.class, DeviceRequest.class, MedicationRequest.class, NutritionOrder.class, ProcedureRequest.class, ReferralRequest.class, RequestGroup.class, Task.class, VisionPrescription.class } )
  public static final String SP_ACTIVITY_REFERENCE = "activity-reference";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>activity-reference</b>
   * <p>
   * Description: <b>Activity details defined in specific resource</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>CarePlan.activity.reference</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam ACTIVITY_REFERENCE = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_ACTIVITY_REFERENCE);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>CarePlan:activity-reference</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_ACTIVITY_REFERENCE = new ca.uhn.fhir.model.api.Include("CarePlan:activity-reference").toLocked();

 /**
   * Search parameter: <b>condition</b>
   * <p>
   * Description: <b>Health issues this plan addresses</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>CarePlan.addresses</b><br>
   * </p>
   */
  @SearchParamDefinition(name="condition", path="CarePlan.addresses", description="Health issues this plan addresses", type="reference", target={Condition.class } )
  public static final String SP_CONDITION = "condition";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>condition</b>
   * <p>
   * Description: <b>Health issues this plan addresses</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>CarePlan.addresses</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam CONDITION = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_CONDITION);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>CarePlan:condition</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_CONDITION = new ca.uhn.fhir.model.api.Include("CarePlan:condition").toLocked();

 /**
   * Search parameter: <b>based-on</b>
   * <p>
   * Description: <b>Fulfills care plan</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>CarePlan.basedOn</b><br>
   * </p>
   */
  @SearchParamDefinition(name="based-on", path="CarePlan.basedOn", description="Fulfills care plan", type="reference", target={CarePlan.class } )
  public static final String SP_BASED_ON = "based-on";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>based-on</b>
   * <p>
   * Description: <b>Fulfills care plan</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>CarePlan.basedOn</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam BASED_ON = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_BASED_ON);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>CarePlan:based-on</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_BASED_ON = new ca.uhn.fhir.model.api.Include("CarePlan:based-on").toLocked();

 /**
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>Who care plan is for</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>CarePlan.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="CarePlan.subject", description="Who care plan is for", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient") }, target={Patient.class } )
  public static final String SP_PATIENT = "patient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patient</b>
   * <p>
   * Description: <b>Who care plan is for</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>CarePlan.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PATIENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PATIENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>CarePlan:patient</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PATIENT = new ca.uhn.fhir.model.api.Include("CarePlan:patient").toLocked();

 /**
   * Search parameter: <b>context</b>
   * <p>
   * Description: <b>Created in context of</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>CarePlan.context</b><br>
   * </p>
   */
  @SearchParamDefinition(name="context", path="CarePlan.context", description="Created in context of", type="reference", target={Encounter.class, EpisodeOfCare.class } )
  public static final String SP_CONTEXT = "context";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>context</b>
   * <p>
   * Description: <b>Created in context of</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>CarePlan.context</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam CONTEXT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_CONTEXT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>CarePlan:context</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_CONTEXT = new ca.uhn.fhir.model.api.Include("CarePlan:context").toLocked();

 /**
   * Search parameter: <b>activity-date</b>
   * <p>
   * Description: <b>Specified date occurs within period specified by CarePlan.activity.timingSchedule</b><br>
   * Type: <b>date</b><br>
   * Path: <b>CarePlan.activity.detail.scheduled[x]</b><br>
   * </p>
   */
  @SearchParamDefinition(name="activity-date", path="CarePlan.activity.detail.scheduled", description="Specified date occurs within period specified by CarePlan.activity.timingSchedule", type="date" )
  public static final String SP_ACTIVITY_DATE = "activity-date";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>activity-date</b>
   * <p>
   * Description: <b>Specified date occurs within period specified by CarePlan.activity.timingSchedule</b><br>
   * Type: <b>date</b><br>
   * Path: <b>CarePlan.activity.detail.scheduled[x]</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam ACTIVITY_DATE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_ACTIVITY_DATE);

 /**
   * Search parameter: <b>definition</b>
   * <p>
   * Description: <b>Protocol or definition</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>CarePlan.definition</b><br>
   * </p>
   */
  @SearchParamDefinition(name="definition", path="CarePlan.definition", description="Protocol or definition", type="reference", target={PlanDefinition.class, Questionnaire.class } )
  public static final String SP_DEFINITION = "definition";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>definition</b>
   * <p>
   * Description: <b>Protocol or definition</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>CarePlan.definition</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam DEFINITION = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_DEFINITION);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>CarePlan:definition</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_DEFINITION = new ca.uhn.fhir.model.api.Include("CarePlan:definition").toLocked();

 /**
   * Search parameter: <b>category</b>
   * <p>
   * Description: <b>Type of plan</b><br>
   * Type: <b>token</b><br>
   * Path: <b>CarePlan.category</b><br>
   * </p>
   */
  @SearchParamDefinition(name="category", path="CarePlan.category", description="Type of plan", type="token" )
  public static final String SP_CATEGORY = "category";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>category</b>
   * <p>
   * Description: <b>Type of plan</b><br>
   * Type: <b>token</b><br>
   * Path: <b>CarePlan.category</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CATEGORY = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CATEGORY);

 /**
   * Search parameter: <b>activity-code</b>
   * <p>
   * Description: <b>Detail type of activity</b><br>
   * Type: <b>token</b><br>
   * Path: <b>CarePlan.activity.detail.code</b><br>
   * </p>
   */
  @SearchParamDefinition(name="activity-code", path="CarePlan.activity.detail.code", description="Detail type of activity", type="token" )
  public static final String SP_ACTIVITY_CODE = "activity-code";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>activity-code</b>
   * <p>
   * Description: <b>Detail type of activity</b><br>
   * Type: <b>token</b><br>
   * Path: <b>CarePlan.activity.detail.code</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam ACTIVITY_CODE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_ACTIVITY_CODE);

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>draft | active | suspended | completed | entered-in-error | cancelled | unknown</b><br>
   * Type: <b>token</b><br>
   * Path: <b>CarePlan.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="CarePlan.status", description="draft | active | suspended | completed | entered-in-error | cancelled | unknown", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>draft | active | suspended | completed | entered-in-error | cancelled | unknown</b><br>
   * Type: <b>token</b><br>
   * Path: <b>CarePlan.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);


}

