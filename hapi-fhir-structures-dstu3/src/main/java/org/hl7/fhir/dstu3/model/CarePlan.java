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

// Generated on Sat, Jan 30, 2016 09:18-0500 for FHIR v1.3.0

import java.util.*;

import org.hl7.fhir.utilities.Utilities;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Block;

import org.hl7.fhir.dstu3.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.*;
/**
 * Describes the intention of how one or more practitioners intend to deliver care for a particular patient, group or community for a period of time, possibly limited to care for a specific condition or set of conditions.
 */
@ResourceDef(name="CarePlan", profile="http://hl7.org/fhir/Profile/CarePlan")
public class CarePlan extends DomainResource {

    public enum CarePlanStatus {
        /**
         * The plan has been suggested but no commitment to it has yet been made.
         */
        PROPOSED, 
        /**
         * The plan is in development or awaiting use but is not yet intended to be acted upon.
         */
        DRAFT, 
        /**
         * The plan is intended to be followed and used as part of patient care.
         */
        ACTIVE, 
        /**
         * The plan is no longer in use and is not expected to be followed or used in patient care.
         */
        COMPLETED, 
        /**
         * The plan has been terminated prior to reaching completion (though it may have been replaced by a new plan).
         */
        CANCELLED, 
        /**
         * added to help the parsers
         */
        NULL;
        public static CarePlanStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("proposed".equals(codeString))
          return PROPOSED;
        if ("draft".equals(codeString))
          return DRAFT;
        if ("active".equals(codeString))
          return ACTIVE;
        if ("completed".equals(codeString))
          return COMPLETED;
        if ("cancelled".equals(codeString))
          return CANCELLED;
        throw new FHIRException("Unknown CarePlanStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PROPOSED: return "proposed";
            case DRAFT: return "draft";
            case ACTIVE: return "active";
            case COMPLETED: return "completed";
            case CANCELLED: return "cancelled";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case PROPOSED: return "http://hl7.org/fhir/care-plan-status";
            case DRAFT: return "http://hl7.org/fhir/care-plan-status";
            case ACTIVE: return "http://hl7.org/fhir/care-plan-status";
            case COMPLETED: return "http://hl7.org/fhir/care-plan-status";
            case CANCELLED: return "http://hl7.org/fhir/care-plan-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case PROPOSED: return "The plan has been suggested but no commitment to it has yet been made.";
            case DRAFT: return "The plan is in development or awaiting use but is not yet intended to be acted upon.";
            case ACTIVE: return "The plan is intended to be followed and used as part of patient care.";
            case COMPLETED: return "The plan is no longer in use and is not expected to be followed or used in patient care.";
            case CANCELLED: return "The plan has been terminated prior to reaching completion (though it may have been replaced by a new plan).";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PROPOSED: return "Proposed";
            case DRAFT: return "Pending";
            case ACTIVE: return "Active";
            case COMPLETED: return "Completed";
            case CANCELLED: return "Cancelled";
            default: return "?";
          }
        }
    }

  public static class CarePlanStatusEnumFactory implements EnumFactory<CarePlanStatus> {
    public CarePlanStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("proposed".equals(codeString))
          return CarePlanStatus.PROPOSED;
        if ("draft".equals(codeString))
          return CarePlanStatus.DRAFT;
        if ("active".equals(codeString))
          return CarePlanStatus.ACTIVE;
        if ("completed".equals(codeString))
          return CarePlanStatus.COMPLETED;
        if ("cancelled".equals(codeString))
          return CarePlanStatus.CANCELLED;
        throw new IllegalArgumentException("Unknown CarePlanStatus code '"+codeString+"'");
        }
        public Enumeration<CarePlanStatus> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("proposed".equals(codeString))
          return new Enumeration<CarePlanStatus>(this, CarePlanStatus.PROPOSED);
        if ("draft".equals(codeString))
          return new Enumeration<CarePlanStatus>(this, CarePlanStatus.DRAFT);
        if ("active".equals(codeString))
          return new Enumeration<CarePlanStatus>(this, CarePlanStatus.ACTIVE);
        if ("completed".equals(codeString))
          return new Enumeration<CarePlanStatus>(this, CarePlanStatus.COMPLETED);
        if ("cancelled".equals(codeString))
          return new Enumeration<CarePlanStatus>(this, CarePlanStatus.CANCELLED);
        throw new FHIRException("Unknown CarePlanStatus code '"+codeString+"'");
        }
    public String toCode(CarePlanStatus code) {
      if (code == CarePlanStatus.PROPOSED)
        return "proposed";
      if (code == CarePlanStatus.DRAFT)
        return "draft";
      if (code == CarePlanStatus.ACTIVE)
        return "active";
      if (code == CarePlanStatus.COMPLETED)
        return "completed";
      if (code == CarePlanStatus.CANCELLED)
        return "cancelled";
      return "?";
      }
    public String toSystem(CarePlanStatus code) {
      return code.getSystem();
      }
    }

    public enum CarePlanRelationship {
        /**
         * The referenced plan is considered to be part of this plan.
         */
        INCLUDES, 
        /**
         * This plan takes the places of the referenced plan.
         */
        REPLACES, 
        /**
         * This plan provides details about how to perform activities defined at a higher level by the referenced plan.
         */
        FULFILLS, 
        /**
         * added to help the parsers
         */
        NULL;
        public static CarePlanRelationship fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("includes".equals(codeString))
          return INCLUDES;
        if ("replaces".equals(codeString))
          return REPLACES;
        if ("fulfills".equals(codeString))
          return FULFILLS;
        throw new FHIRException("Unknown CarePlanRelationship code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case INCLUDES: return "includes";
            case REPLACES: return "replaces";
            case FULFILLS: return "fulfills";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case INCLUDES: return "http://hl7.org/fhir/care-plan-relationship";
            case REPLACES: return "http://hl7.org/fhir/care-plan-relationship";
            case FULFILLS: return "http://hl7.org/fhir/care-plan-relationship";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case INCLUDES: return "The referenced plan is considered to be part of this plan.";
            case REPLACES: return "This plan takes the places of the referenced plan.";
            case FULFILLS: return "This plan provides details about how to perform activities defined at a higher level by the referenced plan.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case INCLUDES: return "Includes";
            case REPLACES: return "Replaces";
            case FULFILLS: return "Fulfills";
            default: return "?";
          }
        }
    }

  public static class CarePlanRelationshipEnumFactory implements EnumFactory<CarePlanRelationship> {
    public CarePlanRelationship fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("includes".equals(codeString))
          return CarePlanRelationship.INCLUDES;
        if ("replaces".equals(codeString))
          return CarePlanRelationship.REPLACES;
        if ("fulfills".equals(codeString))
          return CarePlanRelationship.FULFILLS;
        throw new IllegalArgumentException("Unknown CarePlanRelationship code '"+codeString+"'");
        }
        public Enumeration<CarePlanRelationship> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("includes".equals(codeString))
          return new Enumeration<CarePlanRelationship>(this, CarePlanRelationship.INCLUDES);
        if ("replaces".equals(codeString))
          return new Enumeration<CarePlanRelationship>(this, CarePlanRelationship.REPLACES);
        if ("fulfills".equals(codeString))
          return new Enumeration<CarePlanRelationship>(this, CarePlanRelationship.FULFILLS);
        throw new FHIRException("Unknown CarePlanRelationship code '"+codeString+"'");
        }
    public String toCode(CarePlanRelationship code) {
      if (code == CarePlanRelationship.INCLUDES)
        return "includes";
      if (code == CarePlanRelationship.REPLACES)
        return "replaces";
      if (code == CarePlanRelationship.FULFILLS)
        return "fulfills";
      return "?";
      }
    public String toSystem(CarePlanRelationship code) {
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
         * added to help the parsers
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
            case NOTSTARTED: return "Not Started";
            case SCHEDULED: return "Scheduled";
            case INPROGRESS: return "In Progress";
            case ONHOLD: return "On Hold";
            case COMPLETED: return "Completed";
            case CANCELLED: return "Cancelled";
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
        throw new IllegalArgumentException("Unknown CarePlanActivityStatus code '"+codeString+"'");
        }
        public Enumeration<CarePlanActivityStatus> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
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
      return "?";
      }
    public String toSystem(CarePlanActivityStatus code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class CarePlanRelatedPlanComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Identifies the type of relationship this plan has to the target plan.
         */
        @Child(name = "code", type = {CodeType.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="includes | replaces | fulfills", formalDefinition="Identifies the type of relationship this plan has to the target plan." )
        protected Enumeration<CarePlanRelationship> code;

        /**
         * A reference to the plan to which a relationship is asserted.
         */
        @Child(name = "plan", type = {CarePlan.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Plan relationship exists with", formalDefinition="A reference to the plan to which a relationship is asserted." )
        protected Reference plan;

        /**
         * The actual object that is the target of the reference (A reference to the plan to which a relationship is asserted.)
         */
        protected CarePlan planTarget;

        private static final long serialVersionUID = 1875598050L;

    /**
     * Constructor
     */
      public CarePlanRelatedPlanComponent() {
        super();
      }

    /**
     * Constructor
     */
      public CarePlanRelatedPlanComponent(Reference plan) {
        super();
        this.plan = plan;
      }

        /**
         * @return {@link #code} (Identifies the type of relationship this plan has to the target plan.). This is the underlying object with id, value and extensions. The accessor "getCode" gives direct access to the value
         */
        public Enumeration<CarePlanRelationship> getCodeElement() { 
          if (this.code == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CarePlanRelatedPlanComponent.code");
            else if (Configuration.doAutoCreate())
              this.code = new Enumeration<CarePlanRelationship>(new CarePlanRelationshipEnumFactory()); // bb
          return this.code;
        }

        public boolean hasCodeElement() { 
          return this.code != null && !this.code.isEmpty();
        }

        public boolean hasCode() { 
          return this.code != null && !this.code.isEmpty();
        }

        /**
         * @param value {@link #code} (Identifies the type of relationship this plan has to the target plan.). This is the underlying object with id, value and extensions. The accessor "getCode" gives direct access to the value
         */
        public CarePlanRelatedPlanComponent setCodeElement(Enumeration<CarePlanRelationship> value) { 
          this.code = value;
          return this;
        }

        /**
         * @return Identifies the type of relationship this plan has to the target plan.
         */
        public CarePlanRelationship getCode() { 
          return this.code == null ? null : this.code.getValue();
        }

        /**
         * @param value Identifies the type of relationship this plan has to the target plan.
         */
        public CarePlanRelatedPlanComponent setCode(CarePlanRelationship value) { 
          if (value == null)
            this.code = null;
          else {
            if (this.code == null)
              this.code = new Enumeration<CarePlanRelationship>(new CarePlanRelationshipEnumFactory());
            this.code.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #plan} (A reference to the plan to which a relationship is asserted.)
         */
        public Reference getPlan() { 
          if (this.plan == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CarePlanRelatedPlanComponent.plan");
            else if (Configuration.doAutoCreate())
              this.plan = new Reference(); // cc
          return this.plan;
        }

        public boolean hasPlan() { 
          return this.plan != null && !this.plan.isEmpty();
        }

        /**
         * @param value {@link #plan} (A reference to the plan to which a relationship is asserted.)
         */
        public CarePlanRelatedPlanComponent setPlan(Reference value) { 
          this.plan = value;
          return this;
        }

        /**
         * @return {@link #plan} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (A reference to the plan to which a relationship is asserted.)
         */
        public CarePlan getPlanTarget() { 
          if (this.planTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CarePlanRelatedPlanComponent.plan");
            else if (Configuration.doAutoCreate())
              this.planTarget = new CarePlan(); // aa
          return this.planTarget;
        }

        /**
         * @param value {@link #plan} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (A reference to the plan to which a relationship is asserted.)
         */
        public CarePlanRelatedPlanComponent setPlanTarget(CarePlan value) { 
          this.planTarget = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("code", "code", "Identifies the type of relationship this plan has to the target plan.", 0, java.lang.Integer.MAX_VALUE, code));
          childrenList.add(new Property("plan", "Reference(CarePlan)", "A reference to the plan to which a relationship is asserted.", 0, java.lang.Integer.MAX_VALUE, plan));
        }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("code"))
          this.code = new CarePlanRelationshipEnumFactory().fromType(value); // Enumeration<CarePlanRelationship>
        else if (name.equals("plan"))
          this.plan = castToReference(value); // Reference
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("code")) {
          throw new FHIRException("Cannot call addChild on a primitive type CarePlan.code");
        }
        else if (name.equals("plan")) {
          this.plan = new Reference();
          return this.plan;
        }
        else
          return super.addChild(name);
      }

      public CarePlanRelatedPlanComponent copy() {
        CarePlanRelatedPlanComponent dst = new CarePlanRelatedPlanComponent();
        copyValues(dst);
        dst.code = code == null ? null : code.copy();
        dst.plan = plan == null ? null : plan.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof CarePlanRelatedPlanComponent))
          return false;
        CarePlanRelatedPlanComponent o = (CarePlanRelatedPlanComponent) other;
        return compareDeep(code, o.code, true) && compareDeep(plan, o.plan, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof CarePlanRelatedPlanComponent))
          return false;
        CarePlanRelatedPlanComponent o = (CarePlanRelatedPlanComponent) other;
        return compareValues(code, o.code, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (code == null || code.isEmpty()) && (plan == null || plan.isEmpty())
          ;
      }

  public String fhirType() {
    return "CarePlan.relatedPlan";

  }

  }

    @Block()
    public static class CarePlanParticipantComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Indicates specific responsibility of an individual within the care plan; e.g. "Primary physician", "Team coordinator", "Caregiver", etc.
         */
        @Child(name = "role", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Type of involvement", formalDefinition="Indicates specific responsibility of an individual within the care plan; e.g. \"Primary physician\", \"Team coordinator\", \"Caregiver\", etc." )
        protected CodeableConcept role;

        /**
         * The specific person or organization who is participating/expected to participate in the care plan.
         */
        @Child(name = "member", type = {Practitioner.class, RelatedPerson.class, Patient.class, Organization.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Who is involved", formalDefinition="The specific person or organization who is participating/expected to participate in the care plan." )
        protected Reference member;

        /**
         * The actual object that is the target of the reference (The specific person or organization who is participating/expected to participate in the care plan.)
         */
        protected Resource memberTarget;

        private static final long serialVersionUID = -466811117L;

    /**
     * Constructor
     */
      public CarePlanParticipantComponent() {
        super();
      }

        /**
         * @return {@link #role} (Indicates specific responsibility of an individual within the care plan; e.g. "Primary physician", "Team coordinator", "Caregiver", etc.)
         */
        public CodeableConcept getRole() { 
          if (this.role == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CarePlanParticipantComponent.role");
            else if (Configuration.doAutoCreate())
              this.role = new CodeableConcept(); // cc
          return this.role;
        }

        public boolean hasRole() { 
          return this.role != null && !this.role.isEmpty();
        }

        /**
         * @param value {@link #role} (Indicates specific responsibility of an individual within the care plan; e.g. "Primary physician", "Team coordinator", "Caregiver", etc.)
         */
        public CarePlanParticipantComponent setRole(CodeableConcept value) { 
          this.role = value;
          return this;
        }

        /**
         * @return {@link #member} (The specific person or organization who is participating/expected to participate in the care plan.)
         */
        public Reference getMember() { 
          if (this.member == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CarePlanParticipantComponent.member");
            else if (Configuration.doAutoCreate())
              this.member = new Reference(); // cc
          return this.member;
        }

        public boolean hasMember() { 
          return this.member != null && !this.member.isEmpty();
        }

        /**
         * @param value {@link #member} (The specific person or organization who is participating/expected to participate in the care plan.)
         */
        public CarePlanParticipantComponent setMember(Reference value) { 
          this.member = value;
          return this;
        }

        /**
         * @return {@link #member} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The specific person or organization who is participating/expected to participate in the care plan.)
         */
        public Resource getMemberTarget() { 
          return this.memberTarget;
        }

        /**
         * @param value {@link #member} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The specific person or organization who is participating/expected to participate in the care plan.)
         */
        public CarePlanParticipantComponent setMemberTarget(Resource value) { 
          this.memberTarget = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("role", "CodeableConcept", "Indicates specific responsibility of an individual within the care plan; e.g. \"Primary physician\", \"Team coordinator\", \"Caregiver\", etc.", 0, java.lang.Integer.MAX_VALUE, role));
          childrenList.add(new Property("member", "Reference(Practitioner|RelatedPerson|Patient|Organization)", "The specific person or organization who is participating/expected to participate in the care plan.", 0, java.lang.Integer.MAX_VALUE, member));
        }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("role"))
          this.role = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("member"))
          this.member = castToReference(value); // Reference
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("role")) {
          this.role = new CodeableConcept();
          return this.role;
        }
        else if (name.equals("member")) {
          this.member = new Reference();
          return this.member;
        }
        else
          return super.addChild(name);
      }

      public CarePlanParticipantComponent copy() {
        CarePlanParticipantComponent dst = new CarePlanParticipantComponent();
        copyValues(dst);
        dst.role = role == null ? null : role.copy();
        dst.member = member == null ? null : member.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof CarePlanParticipantComponent))
          return false;
        CarePlanParticipantComponent o = (CarePlanParticipantComponent) other;
        return compareDeep(role, o.role, true) && compareDeep(member, o.member, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof CarePlanParticipantComponent))
          return false;
        CarePlanParticipantComponent o = (CarePlanParticipantComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (role == null || role.isEmpty()) && (member == null || member.isEmpty())
          ;
      }

  public String fhirType() {
    return "CarePlan.participant";

  }

  }

    @Block()
    public static class CarePlanActivityComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Resources that describe follow-on actions resulting from the plan, such as drug prescriptions, encounter records, appointments, etc.
         */
        @Child(name = "actionResulting", type = {}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Appointments, orders, etc.", formalDefinition="Resources that describe follow-on actions resulting from the plan, such as drug prescriptions, encounter records, appointments, etc." )
        protected List<Reference> actionResulting;
        /**
         * The actual objects that are the target of the reference (Resources that describe follow-on actions resulting from the plan, such as drug prescriptions, encounter records, appointments, etc.)
         */
        protected List<Resource> actionResultingTarget;


        /**
         * Notes about the adherence/status/progress of the activity.
         */
        @Child(name = "progress", type = {Annotation.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Comments about the activity status/progress", formalDefinition="Notes about the adherence/status/progress of the activity." )
        protected List<Annotation> progress;

        /**
         * The details of the proposed activity represented in a specific resource.
         */
        @Child(name = "reference", type = {Appointment.class, CommunicationRequest.class, DeviceUseRequest.class, DiagnosticOrder.class, MedicationOrder.class, NutritionOrder.class, Order.class, ProcedureRequest.class, ProcessRequest.class, ReferralRequest.class, SupplyRequest.class, VisionPrescription.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Activity details defined in specific resource", formalDefinition="The details of the proposed activity represented in a specific resource." )
        protected Reference reference;

        /**
         * The actual object that is the target of the reference (The details of the proposed activity represented in a specific resource.)
         */
        protected Resource referenceTarget;

        /**
         * A simple summary of a planned activity suitable for a general care plan system (e.g. form driven) that doesn't know about specific resources such as procedure etc.
         */
        @Child(name = "detail", type = {}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="In-line definition of activity", formalDefinition="A simple summary of a planned activity suitable for a general care plan system (e.g. form driven) that doesn't know about specific resources such as procedure etc." )
        protected CarePlanActivityDetailComponent detail;

        private static final long serialVersionUID = 40181608L;

    /**
     * Constructor
     */
      public CarePlanActivityComponent() {
        super();
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

    // syntactic sugar
        public CarePlanActivityComponent addActionResulting(Reference t) { //3
          if (t == null)
            return this;
          if (this.actionResulting == null)
            this.actionResulting = new ArrayList<Reference>();
          this.actionResulting.add(t);
          return this;
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
         * @return {@link #progress} (Notes about the adherence/status/progress of the activity.)
         */
        public List<Annotation> getProgress() { 
          if (this.progress == null)
            this.progress = new ArrayList<Annotation>();
          return this.progress;
        }

        public boolean hasProgress() { 
          if (this.progress == null)
            return false;
          for (Annotation item : this.progress)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #progress} (Notes about the adherence/status/progress of the activity.)
         */
    // syntactic sugar
        public Annotation addProgress() { //3
          Annotation t = new Annotation();
          if (this.progress == null)
            this.progress = new ArrayList<Annotation>();
          this.progress.add(t);
          return t;
        }

    // syntactic sugar
        public CarePlanActivityComponent addProgress(Annotation t) { //3
          if (t == null)
            return this;
          if (this.progress == null)
            this.progress = new ArrayList<Annotation>();
          this.progress.add(t);
          return this;
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
          childrenList.add(new Property("actionResulting", "Reference(Any)", "Resources that describe follow-on actions resulting from the plan, such as drug prescriptions, encounter records, appointments, etc.", 0, java.lang.Integer.MAX_VALUE, actionResulting));
          childrenList.add(new Property("progress", "Annotation", "Notes about the adherence/status/progress of the activity.", 0, java.lang.Integer.MAX_VALUE, progress));
          childrenList.add(new Property("reference", "Reference(Appointment|CommunicationRequest|DeviceUseRequest|DiagnosticOrder|MedicationOrder|NutritionOrder|Order|ProcedureRequest|ProcessRequest|ReferralRequest|SupplyRequest|VisionPrescription)", "The details of the proposed activity represented in a specific resource.", 0, java.lang.Integer.MAX_VALUE, reference));
          childrenList.add(new Property("detail", "", "A simple summary of a planned activity suitable for a general care plan system (e.g. form driven) that doesn't know about specific resources such as procedure etc.", 0, java.lang.Integer.MAX_VALUE, detail));
        }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("actionResulting"))
          this.getActionResulting().add(castToReference(value));
        else if (name.equals("progress"))
          this.getProgress().add(castToAnnotation(value));
        else if (name.equals("reference"))
          this.reference = castToReference(value); // Reference
        else if (name.equals("detail"))
          this.detail = (CarePlanActivityDetailComponent) value; // CarePlanActivityDetailComponent
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("actionResulting")) {
          return addActionResulting();
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
        if (actionResulting != null) {
          dst.actionResulting = new ArrayList<Reference>();
          for (Reference i : actionResulting)
            dst.actionResulting.add(i.copy());
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
        return compareDeep(actionResulting, o.actionResulting, true) && compareDeep(progress, o.progress, true)
           && compareDeep(reference, o.reference, true) && compareDeep(detail, o.detail, true);
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
        return super.isEmpty() && (actionResulting == null || actionResulting.isEmpty()) && (progress == null || progress.isEmpty())
           && (reference == null || reference.isEmpty()) && (detail == null || detail.isEmpty());
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
        protected CodeableConcept category;

        /**
         * Detailed description of the type of planned activity; e.g. What lab test, what procedure, what kind of encounter.
         */
        @Child(name = "code", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Detail type of activity", formalDefinition="Detailed description of the type of planned activity; e.g. What lab test, what procedure, what kind of encounter." )
        protected CodeableConcept code;

        /**
         * Provides the rationale that drove the inclusion of this particular activity as part of the plan.
         */
        @Child(name = "reasonCode", type = {CodeableConcept.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Why activity should be done", formalDefinition="Provides the rationale that drove the inclusion of this particular activity as part of the plan." )
        protected List<CodeableConcept> reasonCode;

        /**
         * Provides the health condition(s) that drove the inclusion of this particular activity as part of the plan.
         */
        @Child(name = "reasonReference", type = {Condition.class}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Condition triggering need for activity", formalDefinition="Provides the health condition(s) that drove the inclusion of this particular activity as part of the plan." )
        protected List<Reference> reasonReference;
        /**
         * The actual objects that are the target of the reference (Provides the health condition(s) that drove the inclusion of this particular activity as part of the plan.)
         */
        protected List<Condition> reasonReferenceTarget;


        /**
         * Internal reference that identifies the goals that this activity is intended to contribute towards meeting.
         */
        @Child(name = "goal", type = {Goal.class}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Goals this activity relates to", formalDefinition="Internal reference that identifies the goals that this activity is intended to contribute towards meeting." )
        protected List<Reference> goal;
        /**
         * The actual objects that are the target of the reference (Internal reference that identifies the goals that this activity is intended to contribute towards meeting.)
         */
        protected List<Goal> goalTarget;


        /**
         * Identifies what progress is being made for the specific activity.
         */
        @Child(name = "status", type = {CodeType.class}, order=6, min=0, max=1, modifier=true, summary=false)
        @Description(shortDefinition="not-started | scheduled | in-progress | on-hold | completed | cancelled", formalDefinition="Identifies what progress is being made for the specific activity." )
        protected Enumeration<CarePlanActivityStatus> status;

        /**
         * Provides reason why the activity isn't yet started, is on hold, was cancelled, etc.
         */
        @Child(name = "statusReason", type = {CodeableConcept.class}, order=7, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Reason for current status", formalDefinition="Provides reason why the activity isn't yet started, is on hold, was cancelled, etc." )
        protected CodeableConcept statusReason;

        /**
         * If true, indicates that the described activity is one that must NOT be engaged in when following the plan.
         */
        @Child(name = "prohibited", type = {BooleanType.class}, order=8, min=1, max=1, modifier=true, summary=false)
        @Description(shortDefinition="Do NOT do", formalDefinition="If true, indicates that the described activity is one that must NOT be engaged in when following the plan." )
        protected BooleanType prohibited;

        /**
         * The period, timing or frequency upon which the described activity is to occur.
         */
        @Child(name = "scheduled", type = {Timing.class, Period.class, StringType.class}, order=9, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="When activity is to occur", formalDefinition="The period, timing or frequency upon which the described activity is to occur." )
        protected Type scheduled;

        /**
         * Identifies the facility where the activity will occur; e.g. home, hospital, specific clinic, etc.
         */
        @Child(name = "location", type = {Location.class}, order=10, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Where it should happen", formalDefinition="Identifies the facility where the activity will occur; e.g. home, hospital, specific clinic, etc." )
        protected Reference location;

        /**
         * The actual object that is the target of the reference (Identifies the facility where the activity will occur; e.g. home, hospital, specific clinic, etc.)
         */
        protected Location locationTarget;

        /**
         * Identifies who's expected to be involved in the activity.
         */
        @Child(name = "performer", type = {Practitioner.class, Organization.class, RelatedPerson.class, Patient.class}, order=11, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Who will be responsible?", formalDefinition="Identifies who's expected to be involved in the activity." )
        protected List<Reference> performer;
        /**
         * The actual objects that are the target of the reference (Identifies who's expected to be involved in the activity.)
         */
        protected List<Resource> performerTarget;


        /**
         * Identifies the food, drug or other product to be consumed or supplied in the activity.
         */
        @Child(name = "product", type = {CodeableConcept.class, Medication.class, Substance.class}, order=12, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="What is to be administered/supplied", formalDefinition="Identifies the food, drug or other product to be consumed or supplied in the activity." )
        protected Type product;

        /**
         * Identifies the quantity expected to be consumed in a given day.
         */
        @Child(name = "dailyAmount", type = {SimpleQuantity.class}, order=13, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="How to consume/day?", formalDefinition="Identifies the quantity expected to be consumed in a given day." )
        protected SimpleQuantity dailyAmount;

        /**
         * Identifies the quantity expected to be supplied, administered or consumed by the subject.
         */
        @Child(name = "quantity", type = {SimpleQuantity.class}, order=14, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="How much to administer/supply/consume", formalDefinition="Identifies the quantity expected to be supplied, administered or consumed by the subject." )
        protected SimpleQuantity quantity;

        /**
         * This provides a textual description of constraints on the intended activity occurrence, including relation to other activities.  It may also include objectives, pre-conditions and end-conditions.  Finally, it may convey specifics about the activity such as body site, method, route, etc.
         */
        @Child(name = "description", type = {StringType.class}, order=15, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Extra info describing activity to perform", formalDefinition="This provides a textual description of constraints on the intended activity occurrence, including relation to other activities.  It may also include objectives, pre-conditions and end-conditions.  Finally, it may convey specifics about the activity such as body site, method, route, etc." )
        protected StringType description;

        private static final long serialVersionUID = -1763965702L;

    /**
     * Constructor
     */
      public CarePlanActivityDetailComponent() {
        super();
      }

    /**
     * Constructor
     */
      public CarePlanActivityDetailComponent(BooleanType prohibited) {
        super();
        this.prohibited = prohibited;
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
         * @return {@link #reasonCode} (Provides the rationale that drove the inclusion of this particular activity as part of the plan.)
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
         * @return {@link #reasonCode} (Provides the rationale that drove the inclusion of this particular activity as part of the plan.)
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
        public CarePlanActivityDetailComponent addReasonCode(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.reasonCode == null)
            this.reasonCode = new ArrayList<CodeableConcept>();
          this.reasonCode.add(t);
          return this;
        }

        /**
         * @return {@link #reasonReference} (Provides the health condition(s) that drove the inclusion of this particular activity as part of the plan.)
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
         * @return {@link #reasonReference} (Provides the health condition(s) that drove the inclusion of this particular activity as part of the plan.)
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
        public CarePlanActivityDetailComponent addReasonReference(Reference t) { //3
          if (t == null)
            return this;
          if (this.reasonReference == null)
            this.reasonReference = new ArrayList<Reference>();
          this.reasonReference.add(t);
          return this;
        }

        /**
         * @return {@link #reasonReference} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. Provides the health condition(s) that drove the inclusion of this particular activity as part of the plan.)
         */
        public List<Condition> getReasonReferenceTarget() { 
          if (this.reasonReferenceTarget == null)
            this.reasonReferenceTarget = new ArrayList<Condition>();
          return this.reasonReferenceTarget;
        }

    // syntactic sugar
        /**
         * @return {@link #reasonReference} (Add an actual object that is the target of the reference. The reference library doesn't use these, but you can use this to hold the resources if you resolvethemt. Provides the health condition(s) that drove the inclusion of this particular activity as part of the plan.)
         */
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

    // syntactic sugar
        public CarePlanActivityDetailComponent addGoal(Reference t) { //3
          if (t == null)
            return this;
          if (this.goal == null)
            this.goal = new ArrayList<Reference>();
          this.goal.add(t);
          return this;
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
          if (value == null)
            this.status = null;
          else {
            if (this.status == null)
              this.status = new Enumeration<CarePlanActivityStatus>(new CarePlanActivityStatusEnumFactory());
            this.status.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #statusReason} (Provides reason why the activity isn't yet started, is on hold, was cancelled, etc.)
         */
        public CodeableConcept getStatusReason() { 
          if (this.statusReason == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CarePlanActivityDetailComponent.statusReason");
            else if (Configuration.doAutoCreate())
              this.statusReason = new CodeableConcept(); // cc
          return this.statusReason;
        }

        public boolean hasStatusReason() { 
          return this.statusReason != null && !this.statusReason.isEmpty();
        }

        /**
         * @param value {@link #statusReason} (Provides reason why the activity isn't yet started, is on hold, was cancelled, etc.)
         */
        public CarePlanActivityDetailComponent setStatusReason(CodeableConcept value) { 
          this.statusReason = value;
          return this;
        }

        /**
         * @return {@link #prohibited} (If true, indicates that the described activity is one that must NOT be engaged in when following the plan.). This is the underlying object with id, value and extensions. The accessor "getProhibited" gives direct access to the value
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
         * @param value {@link #prohibited} (If true, indicates that the described activity is one that must NOT be engaged in when following the plan.). This is the underlying object with id, value and extensions. The accessor "getProhibited" gives direct access to the value
         */
        public CarePlanActivityDetailComponent setProhibitedElement(BooleanType value) { 
          this.prohibited = value;
          return this;
        }

        /**
         * @return If true, indicates that the described activity is one that must NOT be engaged in when following the plan.
         */
        public boolean getProhibited() { 
          return this.prohibited == null || this.prohibited.isEmpty() ? false : this.prohibited.getValue();
        }

        /**
         * @param value If true, indicates that the described activity is one that must NOT be engaged in when following the plan.
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

    // syntactic sugar
        public CarePlanActivityDetailComponent addPerformer(Reference t) { //3
          if (t == null)
            return this;
          if (this.performer == null)
            this.performer = new ArrayList<Reference>();
          this.performer.add(t);
          return this;
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
          childrenList.add(new Property("code", "CodeableConcept", "Detailed description of the type of planned activity; e.g. What lab test, what procedure, what kind of encounter.", 0, java.lang.Integer.MAX_VALUE, code));
          childrenList.add(new Property("reasonCode", "CodeableConcept", "Provides the rationale that drove the inclusion of this particular activity as part of the plan.", 0, java.lang.Integer.MAX_VALUE, reasonCode));
          childrenList.add(new Property("reasonReference", "Reference(Condition)", "Provides the health condition(s) that drove the inclusion of this particular activity as part of the plan.", 0, java.lang.Integer.MAX_VALUE, reasonReference));
          childrenList.add(new Property("goal", "Reference(Goal)", "Internal reference that identifies the goals that this activity is intended to contribute towards meeting.", 0, java.lang.Integer.MAX_VALUE, goal));
          childrenList.add(new Property("status", "code", "Identifies what progress is being made for the specific activity.", 0, java.lang.Integer.MAX_VALUE, status));
          childrenList.add(new Property("statusReason", "CodeableConcept", "Provides reason why the activity isn't yet started, is on hold, was cancelled, etc.", 0, java.lang.Integer.MAX_VALUE, statusReason));
          childrenList.add(new Property("prohibited", "boolean", "If true, indicates that the described activity is one that must NOT be engaged in when following the plan.", 0, java.lang.Integer.MAX_VALUE, prohibited));
          childrenList.add(new Property("scheduled[x]", "Timing|Period|string", "The period, timing or frequency upon which the described activity is to occur.", 0, java.lang.Integer.MAX_VALUE, scheduled));
          childrenList.add(new Property("location", "Reference(Location)", "Identifies the facility where the activity will occur; e.g. home, hospital, specific clinic, etc.", 0, java.lang.Integer.MAX_VALUE, location));
          childrenList.add(new Property("performer", "Reference(Practitioner|Organization|RelatedPerson|Patient)", "Identifies who's expected to be involved in the activity.", 0, java.lang.Integer.MAX_VALUE, performer));
          childrenList.add(new Property("product[x]", "CodeableConcept|Reference(Medication|Substance)", "Identifies the food, drug or other product to be consumed or supplied in the activity.", 0, java.lang.Integer.MAX_VALUE, product));
          childrenList.add(new Property("dailyAmount", "SimpleQuantity", "Identifies the quantity expected to be consumed in a given day.", 0, java.lang.Integer.MAX_VALUE, dailyAmount));
          childrenList.add(new Property("quantity", "SimpleQuantity", "Identifies the quantity expected to be supplied, administered or consumed by the subject.", 0, java.lang.Integer.MAX_VALUE, quantity));
          childrenList.add(new Property("description", "string", "This provides a textual description of constraints on the intended activity occurrence, including relation to other activities.  It may also include objectives, pre-conditions and end-conditions.  Finally, it may convey specifics about the activity such as body site, method, route, etc.", 0, java.lang.Integer.MAX_VALUE, description));
        }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("category"))
          this.category = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("code"))
          this.code = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("reasonCode"))
          this.getReasonCode().add(castToCodeableConcept(value));
        else if (name.equals("reasonReference"))
          this.getReasonReference().add(castToReference(value));
        else if (name.equals("goal"))
          this.getGoal().add(castToReference(value));
        else if (name.equals("status"))
          this.status = new CarePlanActivityStatusEnumFactory().fromType(value); // Enumeration<CarePlanActivityStatus>
        else if (name.equals("statusReason"))
          this.statusReason = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("prohibited"))
          this.prohibited = castToBoolean(value); // BooleanType
        else if (name.equals("scheduled[x]"))
          this.scheduled = (Type) value; // Type
        else if (name.equals("location"))
          this.location = castToReference(value); // Reference
        else if (name.equals("performer"))
          this.getPerformer().add(castToReference(value));
        else if (name.equals("product[x]"))
          this.product = (Type) value; // Type
        else if (name.equals("dailyAmount"))
          this.dailyAmount = castToSimpleQuantity(value); // SimpleQuantity
        else if (name.equals("quantity"))
          this.quantity = castToSimpleQuantity(value); // SimpleQuantity
        else if (name.equals("description"))
          this.description = castToString(value); // StringType
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("category")) {
          this.category = new CodeableConcept();
          return this.category;
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
          this.statusReason = new CodeableConcept();
          return this.statusReason;
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
        return compareDeep(category, o.category, true) && compareDeep(code, o.code, true) && compareDeep(reasonCode, o.reasonCode, true)
           && compareDeep(reasonReference, o.reasonReference, true) && compareDeep(goal, o.goal, true) && compareDeep(status, o.status, true)
           && compareDeep(statusReason, o.statusReason, true) && compareDeep(prohibited, o.prohibited, true)
           && compareDeep(scheduled, o.scheduled, true) && compareDeep(location, o.location, true) && compareDeep(performer, o.performer, true)
           && compareDeep(product, o.product, true) && compareDeep(dailyAmount, o.dailyAmount, true) && compareDeep(quantity, o.quantity, true)
           && compareDeep(description, o.description, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof CarePlanActivityDetailComponent))
          return false;
        CarePlanActivityDetailComponent o = (CarePlanActivityDetailComponent) other;
        return compareValues(status, o.status, true) && compareValues(prohibited, o.prohibited, true) && compareValues(description, o.description, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (category == null || category.isEmpty()) && (code == null || code.isEmpty())
           && (reasonCode == null || reasonCode.isEmpty()) && (reasonReference == null || reasonReference.isEmpty())
           && (goal == null || goal.isEmpty()) && (status == null || status.isEmpty()) && (statusReason == null || statusReason.isEmpty())
           && (prohibited == null || prohibited.isEmpty()) && (scheduled == null || scheduled.isEmpty())
           && (location == null || location.isEmpty()) && (performer == null || performer.isEmpty())
           && (product == null || product.isEmpty()) && (dailyAmount == null || dailyAmount.isEmpty())
           && (quantity == null || quantity.isEmpty()) && (description == null || description.isEmpty())
          ;
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
     * Identifies the patient or group whose intended care is described by the plan.
     */
    @Child(name = "subject", type = {Patient.class, Group.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Who care plan is for", formalDefinition="Identifies the patient or group whose intended care is described by the plan." )
    protected Reference subject;

    /**
     * The actual object that is the target of the reference (Identifies the patient or group whose intended care is described by the plan.)
     */
    protected Resource subjectTarget;

    /**
     * Indicates whether the plan is currently being acted upon, represents future intentions or is now a historical record.
     */
    @Child(name = "status", type = {CodeType.class}, order=2, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="proposed | draft | active | completed | cancelled", formalDefinition="Indicates whether the plan is currently being acted upon, represents future intentions or is now a historical record." )
    protected Enumeration<CarePlanStatus> status;

    /**
     * Identifies the context in which this particular CarePlan is defined.
     */
    @Child(name = "context", type = {Encounter.class, EpisodeOfCare.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Created in context of", formalDefinition="Identifies the context in which this particular CarePlan is defined." )
    protected Reference context;

    /**
     * The actual object that is the target of the reference (Identifies the context in which this particular CarePlan is defined.)
     */
    protected Resource contextTarget;

    /**
     * Indicates when the plan did (or is intended to) come into effect and end.
     */
    @Child(name = "period", type = {Period.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Time period plan covers", formalDefinition="Indicates when the plan did (or is intended to) come into effect and end." )
    protected Period period;

    /**
     * Identifies the individual(s) or ogranization who is responsible for the content of the care plan.
     */
    @Child(name = "author", type = {Patient.class, Practitioner.class, RelatedPerson.class, Organization.class}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Who is responsible for contents of the plan", formalDefinition="Identifies the individual(s) or ogranization who is responsible for the content of the care plan." )
    protected List<Reference> author;
    /**
     * The actual objects that are the target of the reference (Identifies the individual(s) or ogranization who is responsible for the content of the care plan.)
     */
    protected List<Resource> authorTarget;


    /**
     * Identifies the most recent date on which the plan has been revised.
     */
    @Child(name = "modified", type = {DateTimeType.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When last updated", formalDefinition="Identifies the most recent date on which the plan has been revised." )
    protected DateTimeType modified;

    /**
     * Identifies what "kind" of plan this is to support differentiation between multiple co-existing plans; e.g. "Home health", "psychiatric", "asthma", "disease management", "wellness plan", etc.
     */
    @Child(name = "category", type = {CodeableConcept.class}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Type of plan", formalDefinition="Identifies what \"kind\" of plan this is to support differentiation between multiple co-existing plans; e.g. \"Home health\", \"psychiatric\", \"asthma\", \"disease management\", \"wellness plan\", etc." )
    protected List<CodeableConcept> category;

    /**
     * A description of the scope and nature of the plan.
     */
    @Child(name = "description", type = {StringType.class}, order=8, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Summary of nature of plan", formalDefinition="A description of the scope and nature of the plan." )
    protected StringType description;

    /**
     * Identifies the conditions/problems/concerns/diagnoses/etc. whose management and/or mitigation are handled by this plan.
     */
    @Child(name = "addresses", type = {Condition.class}, order=9, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Health issues this plan addresses", formalDefinition="Identifies the conditions/problems/concerns/diagnoses/etc. whose management and/or mitigation are handled by this plan." )
    protected List<Reference> addresses;
    /**
     * The actual objects that are the target of the reference (Identifies the conditions/problems/concerns/diagnoses/etc. whose management and/or mitigation are handled by this plan.)
     */
    protected List<Condition> addressesTarget;


    /**
     * Identifies portions of the patient's record that specifically influenced the formation of the plan.  These might include co-morbidities, recent procedures, limitations, recent assessments, etc.
     */
    @Child(name = "support", type = {}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Information considered as part of plan", formalDefinition="Identifies portions of the patient's record that specifically influenced the formation of the plan.  These might include co-morbidities, recent procedures, limitations, recent assessments, etc." )
    protected List<Reference> support;
    /**
     * The actual objects that are the target of the reference (Identifies portions of the patient's record that specifically influenced the formation of the plan.  These might include co-morbidities, recent procedures, limitations, recent assessments, etc.)
     */
    protected List<Resource> supportTarget;


    /**
     * Identifies CarePlans with some sort of formal relationship to the current plan.
     */
    @Child(name = "relatedPlan", type = {}, order=11, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Plans related to this one", formalDefinition="Identifies CarePlans with some sort of formal relationship to the current plan." )
    protected List<CarePlanRelatedPlanComponent> relatedPlan;

    /**
     * Identifies all people and organizations who are expected to be involved in the care envisioned by this plan.
     */
    @Child(name = "participant", type = {}, order=12, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Who's involved in plan?", formalDefinition="Identifies all people and organizations who are expected to be involved in the care envisioned by this plan." )
    protected List<CarePlanParticipantComponent> participant;

    /**
     * Describes the intended objective(s) of carrying out the care plan.
     */
    @Child(name = "goal", type = {Goal.class}, order=13, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Desired outcome of plan", formalDefinition="Describes the intended objective(s) of carrying out the care plan." )
    protected List<Reference> goal;
    /**
     * The actual objects that are the target of the reference (Describes the intended objective(s) of carrying out the care plan.)
     */
    protected List<Goal> goalTarget;


    /**
     * Identifies a planned action to occur as part of the plan.  For example, a medication to be used, lab tests to perform, self-monitoring, education, etc.
     */
    @Child(name = "activity", type = {}, order=14, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Action to occur as part of plan", formalDefinition="Identifies a planned action to occur as part of the plan.  For example, a medication to be used, lab tests to perform, self-monitoring, education, etc." )
    protected List<CarePlanActivityComponent> activity;

    /**
     * General notes about the care plan not covered elsewhere.
     */
    @Child(name = "note", type = {Annotation.class}, order=15, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Comments about the plan", formalDefinition="General notes about the care plan not covered elsewhere." )
    protected Annotation note;

    private static final long serialVersionUID = -307500543L;

  /**
   * Constructor
   */
    public CarePlan() {
      super();
    }

  /**
   * Constructor
   */
    public CarePlan(Enumeration<CarePlanStatus> status) {
      super();
      this.status = status;
    }

    /**
     * @return {@link #identifier} (This records identifiers associated with this care plan that are defined by business processes and/or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation).)
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
     * @return {@link #identifier} (This records identifiers associated with this care plan that are defined by business processes and/or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation).)
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
    public CarePlan addIdentifier(Identifier t) { //3
      if (t == null)
        return this;
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
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
     * @return {@link #context} (Identifies the context in which this particular CarePlan is defined.)
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
     * @param value {@link #context} (Identifies the context in which this particular CarePlan is defined.)
     */
    public CarePlan setContext(Reference value) { 
      this.context = value;
      return this;
    }

    /**
     * @return {@link #context} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Identifies the context in which this particular CarePlan is defined.)
     */
    public Resource getContextTarget() { 
      return this.contextTarget;
    }

    /**
     * @param value {@link #context} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Identifies the context in which this particular CarePlan is defined.)
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

    public boolean hasAuthor() { 
      if (this.author == null)
        return false;
      for (Reference item : this.author)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #author} (Identifies the individual(s) or ogranization who is responsible for the content of the care plan.)
     */
    // syntactic sugar
    public Reference addAuthor() { //3
      Reference t = new Reference();
      if (this.author == null)
        this.author = new ArrayList<Reference>();
      this.author.add(t);
      return t;
    }

    // syntactic sugar
    public CarePlan addAuthor(Reference t) { //3
      if (t == null)
        return this;
      if (this.author == null)
        this.author = new ArrayList<Reference>();
      this.author.add(t);
      return this;
    }

    /**
     * @return {@link #author} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. Identifies the individual(s) or ogranization who is responsible for the content of the care plan.)
     */
    public List<Resource> getAuthorTarget() { 
      if (this.authorTarget == null)
        this.authorTarget = new ArrayList<Resource>();
      return this.authorTarget;
    }

    /**
     * @return {@link #modified} (Identifies the most recent date on which the plan has been revised.). This is the underlying object with id, value and extensions. The accessor "getModified" gives direct access to the value
     */
    public DateTimeType getModifiedElement() { 
      if (this.modified == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CarePlan.modified");
        else if (Configuration.doAutoCreate())
          this.modified = new DateTimeType(); // bb
      return this.modified;
    }

    public boolean hasModifiedElement() { 
      return this.modified != null && !this.modified.isEmpty();
    }

    public boolean hasModified() { 
      return this.modified != null && !this.modified.isEmpty();
    }

    /**
     * @param value {@link #modified} (Identifies the most recent date on which the plan has been revised.). This is the underlying object with id, value and extensions. The accessor "getModified" gives direct access to the value
     */
    public CarePlan setModifiedElement(DateTimeType value) { 
      this.modified = value;
      return this;
    }

    /**
     * @return Identifies the most recent date on which the plan has been revised.
     */
    public Date getModified() { 
      return this.modified == null ? null : this.modified.getValue();
    }

    /**
     * @param value Identifies the most recent date on which the plan has been revised.
     */
    public CarePlan setModified(Date value) { 
      if (value == null)
        this.modified = null;
      else {
        if (this.modified == null)
          this.modified = new DateTimeType();
        this.modified.setValue(value);
      }
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

    public boolean hasCategory() { 
      if (this.category == null)
        return false;
      for (CodeableConcept item : this.category)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #category} (Identifies what "kind" of plan this is to support differentiation between multiple co-existing plans; e.g. "Home health", "psychiatric", "asthma", "disease management", "wellness plan", etc.)
     */
    // syntactic sugar
    public CodeableConcept addCategory() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.category == null)
        this.category = new ArrayList<CodeableConcept>();
      this.category.add(t);
      return t;
    }

    // syntactic sugar
    public CarePlan addCategory(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.category == null)
        this.category = new ArrayList<CodeableConcept>();
      this.category.add(t);
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
     * @return {@link #addresses} (Identifies the conditions/problems/concerns/diagnoses/etc. whose management and/or mitigation are handled by this plan.)
     */
    public List<Reference> getAddresses() { 
      if (this.addresses == null)
        this.addresses = new ArrayList<Reference>();
      return this.addresses;
    }

    public boolean hasAddresses() { 
      if (this.addresses == null)
        return false;
      for (Reference item : this.addresses)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #addresses} (Identifies the conditions/problems/concerns/diagnoses/etc. whose management and/or mitigation are handled by this plan.)
     */
    // syntactic sugar
    public Reference addAddresses() { //3
      Reference t = new Reference();
      if (this.addresses == null)
        this.addresses = new ArrayList<Reference>();
      this.addresses.add(t);
      return t;
    }

    // syntactic sugar
    public CarePlan addAddresses(Reference t) { //3
      if (t == null)
        return this;
      if (this.addresses == null)
        this.addresses = new ArrayList<Reference>();
      this.addresses.add(t);
      return this;
    }

    /**
     * @return {@link #addresses} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. Identifies the conditions/problems/concerns/diagnoses/etc. whose management and/or mitigation are handled by this plan.)
     */
    public List<Condition> getAddressesTarget() { 
      if (this.addressesTarget == null)
        this.addressesTarget = new ArrayList<Condition>();
      return this.addressesTarget;
    }

    // syntactic sugar
    /**
     * @return {@link #addresses} (Add an actual object that is the target of the reference. The reference library doesn't use these, but you can use this to hold the resources if you resolvethemt. Identifies the conditions/problems/concerns/diagnoses/etc. whose management and/or mitigation are handled by this plan.)
     */
    public Condition addAddressesTarget() { 
      Condition r = new Condition();
      if (this.addressesTarget == null)
        this.addressesTarget = new ArrayList<Condition>();
      this.addressesTarget.add(r);
      return r;
    }

    /**
     * @return {@link #support} (Identifies portions of the patient's record that specifically influenced the formation of the plan.  These might include co-morbidities, recent procedures, limitations, recent assessments, etc.)
     */
    public List<Reference> getSupport() { 
      if (this.support == null)
        this.support = new ArrayList<Reference>();
      return this.support;
    }

    public boolean hasSupport() { 
      if (this.support == null)
        return false;
      for (Reference item : this.support)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #support} (Identifies portions of the patient's record that specifically influenced the formation of the plan.  These might include co-morbidities, recent procedures, limitations, recent assessments, etc.)
     */
    // syntactic sugar
    public Reference addSupport() { //3
      Reference t = new Reference();
      if (this.support == null)
        this.support = new ArrayList<Reference>();
      this.support.add(t);
      return t;
    }

    // syntactic sugar
    public CarePlan addSupport(Reference t) { //3
      if (t == null)
        return this;
      if (this.support == null)
        this.support = new ArrayList<Reference>();
      this.support.add(t);
      return this;
    }

    /**
     * @return {@link #support} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. Identifies portions of the patient's record that specifically influenced the formation of the plan.  These might include co-morbidities, recent procedures, limitations, recent assessments, etc.)
     */
    public List<Resource> getSupportTarget() { 
      if (this.supportTarget == null)
        this.supportTarget = new ArrayList<Resource>();
      return this.supportTarget;
    }

    /**
     * @return {@link #relatedPlan} (Identifies CarePlans with some sort of formal relationship to the current plan.)
     */
    public List<CarePlanRelatedPlanComponent> getRelatedPlan() { 
      if (this.relatedPlan == null)
        this.relatedPlan = new ArrayList<CarePlanRelatedPlanComponent>();
      return this.relatedPlan;
    }

    public boolean hasRelatedPlan() { 
      if (this.relatedPlan == null)
        return false;
      for (CarePlanRelatedPlanComponent item : this.relatedPlan)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #relatedPlan} (Identifies CarePlans with some sort of formal relationship to the current plan.)
     */
    // syntactic sugar
    public CarePlanRelatedPlanComponent addRelatedPlan() { //3
      CarePlanRelatedPlanComponent t = new CarePlanRelatedPlanComponent();
      if (this.relatedPlan == null)
        this.relatedPlan = new ArrayList<CarePlanRelatedPlanComponent>();
      this.relatedPlan.add(t);
      return t;
    }

    // syntactic sugar
    public CarePlan addRelatedPlan(CarePlanRelatedPlanComponent t) { //3
      if (t == null)
        return this;
      if (this.relatedPlan == null)
        this.relatedPlan = new ArrayList<CarePlanRelatedPlanComponent>();
      this.relatedPlan.add(t);
      return this;
    }

    /**
     * @return {@link #participant} (Identifies all people and organizations who are expected to be involved in the care envisioned by this plan.)
     */
    public List<CarePlanParticipantComponent> getParticipant() { 
      if (this.participant == null)
        this.participant = new ArrayList<CarePlanParticipantComponent>();
      return this.participant;
    }

    public boolean hasParticipant() { 
      if (this.participant == null)
        return false;
      for (CarePlanParticipantComponent item : this.participant)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #participant} (Identifies all people and organizations who are expected to be involved in the care envisioned by this plan.)
     */
    // syntactic sugar
    public CarePlanParticipantComponent addParticipant() { //3
      CarePlanParticipantComponent t = new CarePlanParticipantComponent();
      if (this.participant == null)
        this.participant = new ArrayList<CarePlanParticipantComponent>();
      this.participant.add(t);
      return t;
    }

    // syntactic sugar
    public CarePlan addParticipant(CarePlanParticipantComponent t) { //3
      if (t == null)
        return this;
      if (this.participant == null)
        this.participant = new ArrayList<CarePlanParticipantComponent>();
      this.participant.add(t);
      return this;
    }

    /**
     * @return {@link #goal} (Describes the intended objective(s) of carrying out the care plan.)
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
     * @return {@link #goal} (Describes the intended objective(s) of carrying out the care plan.)
     */
    // syntactic sugar
    public Reference addGoal() { //3
      Reference t = new Reference();
      if (this.goal == null)
        this.goal = new ArrayList<Reference>();
      this.goal.add(t);
      return t;
    }

    // syntactic sugar
    public CarePlan addGoal(Reference t) { //3
      if (t == null)
        return this;
      if (this.goal == null)
        this.goal = new ArrayList<Reference>();
      this.goal.add(t);
      return this;
    }

    /**
     * @return {@link #goal} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. Describes the intended objective(s) of carrying out the care plan.)
     */
    public List<Goal> getGoalTarget() { 
      if (this.goalTarget == null)
        this.goalTarget = new ArrayList<Goal>();
      return this.goalTarget;
    }

    // syntactic sugar
    /**
     * @return {@link #goal} (Add an actual object that is the target of the reference. The reference library doesn't use these, but you can use this to hold the resources if you resolvethemt. Describes the intended objective(s) of carrying out the care plan.)
     */
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

    public boolean hasActivity() { 
      if (this.activity == null)
        return false;
      for (CarePlanActivityComponent item : this.activity)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #activity} (Identifies a planned action to occur as part of the plan.  For example, a medication to be used, lab tests to perform, self-monitoring, education, etc.)
     */
    // syntactic sugar
    public CarePlanActivityComponent addActivity() { //3
      CarePlanActivityComponent t = new CarePlanActivityComponent();
      if (this.activity == null)
        this.activity = new ArrayList<CarePlanActivityComponent>();
      this.activity.add(t);
      return t;
    }

    // syntactic sugar
    public CarePlan addActivity(CarePlanActivityComponent t) { //3
      if (t == null)
        return this;
      if (this.activity == null)
        this.activity = new ArrayList<CarePlanActivityComponent>();
      this.activity.add(t);
      return this;
    }

    /**
     * @return {@link #note} (General notes about the care plan not covered elsewhere.)
     */
    public Annotation getNote() { 
      if (this.note == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CarePlan.note");
        else if (Configuration.doAutoCreate())
          this.note = new Annotation(); // cc
      return this.note;
    }

    public boolean hasNote() { 
      return this.note != null && !this.note.isEmpty();
    }

    /**
     * @param value {@link #note} (General notes about the care plan not covered elsewhere.)
     */
    public CarePlan setNote(Annotation value) { 
      this.note = value;
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "This records identifiers associated with this care plan that are defined by business processes and/or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation).", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("subject", "Reference(Patient|Group)", "Identifies the patient or group whose intended care is described by the plan.", 0, java.lang.Integer.MAX_VALUE, subject));
        childrenList.add(new Property("status", "code", "Indicates whether the plan is currently being acted upon, represents future intentions or is now a historical record.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("context", "Reference(Encounter|EpisodeOfCare)", "Identifies the context in which this particular CarePlan is defined.", 0, java.lang.Integer.MAX_VALUE, context));
        childrenList.add(new Property("period", "Period", "Indicates when the plan did (or is intended to) come into effect and end.", 0, java.lang.Integer.MAX_VALUE, period));
        childrenList.add(new Property("author", "Reference(Patient|Practitioner|RelatedPerson|Organization)", "Identifies the individual(s) or ogranization who is responsible for the content of the care plan.", 0, java.lang.Integer.MAX_VALUE, author));
        childrenList.add(new Property("modified", "dateTime", "Identifies the most recent date on which the plan has been revised.", 0, java.lang.Integer.MAX_VALUE, modified));
        childrenList.add(new Property("category", "CodeableConcept", "Identifies what \"kind\" of plan this is to support differentiation between multiple co-existing plans; e.g. \"Home health\", \"psychiatric\", \"asthma\", \"disease management\", \"wellness plan\", etc.", 0, java.lang.Integer.MAX_VALUE, category));
        childrenList.add(new Property("description", "string", "A description of the scope and nature of the plan.", 0, java.lang.Integer.MAX_VALUE, description));
        childrenList.add(new Property("addresses", "Reference(Condition)", "Identifies the conditions/problems/concerns/diagnoses/etc. whose management and/or mitigation are handled by this plan.", 0, java.lang.Integer.MAX_VALUE, addresses));
        childrenList.add(new Property("support", "Reference(Any)", "Identifies portions of the patient's record that specifically influenced the formation of the plan.  These might include co-morbidities, recent procedures, limitations, recent assessments, etc.", 0, java.lang.Integer.MAX_VALUE, support));
        childrenList.add(new Property("relatedPlan", "", "Identifies CarePlans with some sort of formal relationship to the current plan.", 0, java.lang.Integer.MAX_VALUE, relatedPlan));
        childrenList.add(new Property("participant", "", "Identifies all people and organizations who are expected to be involved in the care envisioned by this plan.", 0, java.lang.Integer.MAX_VALUE, participant));
        childrenList.add(new Property("goal", "Reference(Goal)", "Describes the intended objective(s) of carrying out the care plan.", 0, java.lang.Integer.MAX_VALUE, goal));
        childrenList.add(new Property("activity", "", "Identifies a planned action to occur as part of the plan.  For example, a medication to be used, lab tests to perform, self-monitoring, education, etc.", 0, java.lang.Integer.MAX_VALUE, activity));
        childrenList.add(new Property("note", "Annotation", "General notes about the care plan not covered elsewhere.", 0, java.lang.Integer.MAX_VALUE, note));
      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier"))
          this.getIdentifier().add(castToIdentifier(value));
        else if (name.equals("subject"))
          this.subject = castToReference(value); // Reference
        else if (name.equals("status"))
          this.status = new CarePlanStatusEnumFactory().fromType(value); // Enumeration<CarePlanStatus>
        else if (name.equals("context"))
          this.context = castToReference(value); // Reference
        else if (name.equals("period"))
          this.period = castToPeriod(value); // Period
        else if (name.equals("author"))
          this.getAuthor().add(castToReference(value));
        else if (name.equals("modified"))
          this.modified = castToDateTime(value); // DateTimeType
        else if (name.equals("category"))
          this.getCategory().add(castToCodeableConcept(value));
        else if (name.equals("description"))
          this.description = castToString(value); // StringType
        else if (name.equals("addresses"))
          this.getAddresses().add(castToReference(value));
        else if (name.equals("support"))
          this.getSupport().add(castToReference(value));
        else if (name.equals("relatedPlan"))
          this.getRelatedPlan().add((CarePlanRelatedPlanComponent) value);
        else if (name.equals("participant"))
          this.getParticipant().add((CarePlanParticipantComponent) value);
        else if (name.equals("goal"))
          this.getGoal().add(castToReference(value));
        else if (name.equals("activity"))
          this.getActivity().add((CarePlanActivityComponent) value);
        else if (name.equals("note"))
          this.note = castToAnnotation(value); // Annotation
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("subject")) {
          this.subject = new Reference();
          return this.subject;
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type CarePlan.status");
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
        else if (name.equals("modified")) {
          throw new FHIRException("Cannot call addChild on a primitive type CarePlan.modified");
        }
        else if (name.equals("category")) {
          return addCategory();
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type CarePlan.description");
        }
        else if (name.equals("addresses")) {
          return addAddresses();
        }
        else if (name.equals("support")) {
          return addSupport();
        }
        else if (name.equals("relatedPlan")) {
          return addRelatedPlan();
        }
        else if (name.equals("participant")) {
          return addParticipant();
        }
        else if (name.equals("goal")) {
          return addGoal();
        }
        else if (name.equals("activity")) {
          return addActivity();
        }
        else if (name.equals("note")) {
          this.note = new Annotation();
          return this.note;
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
        dst.subject = subject == null ? null : subject.copy();
        dst.status = status == null ? null : status.copy();
        dst.context = context == null ? null : context.copy();
        dst.period = period == null ? null : period.copy();
        if (author != null) {
          dst.author = new ArrayList<Reference>();
          for (Reference i : author)
            dst.author.add(i.copy());
        };
        dst.modified = modified == null ? null : modified.copy();
        if (category != null) {
          dst.category = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : category)
            dst.category.add(i.copy());
        };
        dst.description = description == null ? null : description.copy();
        if (addresses != null) {
          dst.addresses = new ArrayList<Reference>();
          for (Reference i : addresses)
            dst.addresses.add(i.copy());
        };
        if (support != null) {
          dst.support = new ArrayList<Reference>();
          for (Reference i : support)
            dst.support.add(i.copy());
        };
        if (relatedPlan != null) {
          dst.relatedPlan = new ArrayList<CarePlanRelatedPlanComponent>();
          for (CarePlanRelatedPlanComponent i : relatedPlan)
            dst.relatedPlan.add(i.copy());
        };
        if (participant != null) {
          dst.participant = new ArrayList<CarePlanParticipantComponent>();
          for (CarePlanParticipantComponent i : participant)
            dst.participant.add(i.copy());
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
        dst.note = note == null ? null : note.copy();
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
        return compareDeep(identifier, o.identifier, true) && compareDeep(subject, o.subject, true) && compareDeep(status, o.status, true)
           && compareDeep(context, o.context, true) && compareDeep(period, o.period, true) && compareDeep(author, o.author, true)
           && compareDeep(modified, o.modified, true) && compareDeep(category, o.category, true) && compareDeep(description, o.description, true)
           && compareDeep(addresses, o.addresses, true) && compareDeep(support, o.support, true) && compareDeep(relatedPlan, o.relatedPlan, true)
           && compareDeep(participant, o.participant, true) && compareDeep(goal, o.goal, true) && compareDeep(activity, o.activity, true)
           && compareDeep(note, o.note, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof CarePlan))
          return false;
        CarePlan o = (CarePlan) other;
        return compareValues(status, o.status, true) && compareValues(modified, o.modified, true) && compareValues(description, o.description, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (subject == null || subject.isEmpty())
           && (status == null || status.isEmpty()) && (context == null || context.isEmpty()) && (period == null || period.isEmpty())
           && (author == null || author.isEmpty()) && (modified == null || modified.isEmpty()) && (category == null || category.isEmpty())
           && (description == null || description.isEmpty()) && (addresses == null || addresses.isEmpty())
           && (support == null || support.isEmpty()) && (relatedPlan == null || relatedPlan.isEmpty())
           && (participant == null || participant.isEmpty()) && (goal == null || goal.isEmpty()) && (activity == null || activity.isEmpty())
           && (note == null || note.isEmpty());
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
   * Search parameter: <b>activitycode</b>
   * <p>
   * Description: <b>Detail type of activity</b><br>
   * Type: <b>token</b><br>
   * Path: <b>CarePlan.activity.detail.code</b><br>
   * </p>
   */
  @SearchParamDefinition(name="activitycode", path="CarePlan.activity.detail.code", description="Detail type of activity", type="token" )
  public static final String SP_ACTIVITYCODE = "activitycode";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>activitycode</b>
   * <p>
   * Description: <b>Detail type of activity</b><br>
   * Type: <b>token</b><br>
   * Path: <b>CarePlan.activity.detail.code</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam ACTIVITYCODE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_ACTIVITYCODE);

 /**
   * Search parameter: <b>activitydate</b>
   * <p>
   * Description: <b>Specified date occurs within period specified by CarePlan.activity.timingSchedule</b><br>
   * Type: <b>date</b><br>
   * Path: <b>CarePlan.activity.detail.scheduled[x]</b><br>
   * </p>
   */
  @SearchParamDefinition(name="activitydate", path="CarePlan.activity.detail.scheduled[x]", description="Specified date occurs within period specified by CarePlan.activity.timingSchedule", type="date" )
  public static final String SP_ACTIVITYDATE = "activitydate";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>activitydate</b>
   * <p>
   * Description: <b>Specified date occurs within period specified by CarePlan.activity.timingSchedule</b><br>
   * Type: <b>date</b><br>
   * Path: <b>CarePlan.activity.detail.scheduled[x]</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam ACTIVITYDATE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_ACTIVITYDATE);

 /**
   * Search parameter: <b>activityreference</b>
   * <p>
   * Description: <b>Activity details defined in specific resource</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>CarePlan.activity.reference</b><br>
   * </p>
   */
  @SearchParamDefinition(name="activityreference", path="CarePlan.activity.reference", description="Activity details defined in specific resource", type="reference" )
  public static final String SP_ACTIVITYREFERENCE = "activityreference";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>activityreference</b>
   * <p>
   * Description: <b>Activity details defined in specific resource</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>CarePlan.activity.reference</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam ACTIVITYREFERENCE = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_ACTIVITYREFERENCE);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>CarePlan:activityreference</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_ACTIVITYREFERENCE = new ca.uhn.fhir.model.api.Include("CarePlan:activityreference").toLocked();

 /**
   * Search parameter: <b>performer</b>
   * <p>
   * Description: <b>Matches if the practitioner is listed as a performer in any of the "simple" activities.  (For performers of the detailed activities, chain through the activitydetail search parameter.)</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>CarePlan.activity.detail.performer</b><br>
   * </p>
   */
  @SearchParamDefinition(name="performer", path="CarePlan.activity.detail.performer", description="Matches if the practitioner is listed as a performer in any of the \"simple\" activities.  (For performers of the detailed activities, chain through the activitydetail search parameter.)", type="reference" )
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
  @SearchParamDefinition(name="goal", path="CarePlan.goal", description="Desired outcome of plan", type="reference" )
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
  @SearchParamDefinition(name="subject", path="CarePlan.subject", description="Who care plan is for", type="reference" )
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
   * Search parameter: <b>relatedcode</b>
   * <p>
   * Description: <b>includes | replaces | fulfills</b><br>
   * Type: <b>token</b><br>
   * Path: <b>CarePlan.relatedPlan.code</b><br>
   * </p>
   */
  @SearchParamDefinition(name="relatedcode", path="CarePlan.relatedPlan.code", description="includes | replaces | fulfills", type="token" )
  public static final String SP_RELATEDCODE = "relatedcode";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>relatedcode</b>
   * <p>
   * Description: <b>includes | replaces | fulfills</b><br>
   * Type: <b>token</b><br>
   * Path: <b>CarePlan.relatedPlan.code</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam RELATEDCODE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_RELATEDCODE);

 /**
   * Search parameter: <b>participant</b>
   * <p>
   * Description: <b>Who is involved</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>CarePlan.participant.member</b><br>
   * </p>
   */
  @SearchParamDefinition(name="participant", path="CarePlan.participant.member", description="Who is involved", type="reference" )
  public static final String SP_PARTICIPANT = "participant";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>participant</b>
   * <p>
   * Description: <b>Who is involved</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>CarePlan.participant.member</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PARTICIPANT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PARTICIPANT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>CarePlan:participant</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PARTICIPANT = new ca.uhn.fhir.model.api.Include("CarePlan:participant").toLocked();

 /**
   * Search parameter: <b>relatedplan</b>
   * <p>
   * Description: <b>Plan relationship exists with</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>CarePlan.relatedPlan.plan</b><br>
   * </p>
   */
  @SearchParamDefinition(name="relatedplan", path="CarePlan.relatedPlan.plan", description="Plan relationship exists with", type="reference" )
  public static final String SP_RELATEDPLAN = "relatedplan";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>relatedplan</b>
   * <p>
   * Description: <b>Plan relationship exists with</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>CarePlan.relatedPlan.plan</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam RELATEDPLAN = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_RELATEDPLAN);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>CarePlan:relatedplan</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_RELATEDPLAN = new ca.uhn.fhir.model.api.Include("CarePlan:relatedplan").toLocked();

 /**
   * Search parameter: <b>condition</b>
   * <p>
   * Description: <b>Health issues this plan addresses</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>CarePlan.addresses</b><br>
   * </p>
   */
  @SearchParamDefinition(name="condition", path="CarePlan.addresses", description="Health issues this plan addresses", type="reference" )
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
   * Search parameter: <b>related</b>
   * <p>
   * Description: <b>A combination of the type of relationship and the related plan</b><br>
   * Type: <b>composite</b><br>
   * Path: <b></b><br>
   * </p>
   */
  @SearchParamDefinition(name="related", path="", description="A combination of the type of relationship and the related plan", type="composite", compositeOf={"relatedcode", "relatedplan"} )
  public static final String SP_RELATED = "related";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>related</b>
   * <p>
   * Description: <b>A combination of the type of relationship and the related plan</b><br>
   * Type: <b>composite</b><br>
   * Path: <b></b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.CompositeClientParam<ca.uhn.fhir.rest.gclient.TokenClientParam, ca.uhn.fhir.rest.gclient.ReferenceClientParam> RELATED = new ca.uhn.fhir.rest.gclient.CompositeClientParam<ca.uhn.fhir.rest.gclient.TokenClientParam, ca.uhn.fhir.rest.gclient.ReferenceClientParam>(SP_RELATED);

 /**
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>Who care plan is for</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>CarePlan.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="CarePlan.subject", description="Who care plan is for", type="reference" )
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


}

