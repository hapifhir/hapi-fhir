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
 * Describes the intention of how one or more practitioners intend to deliver care for a particular patient for a period of time, possibly limited to care for a specific condition or set of conditions.
 */
@ResourceDef(name="CarePlan", profile="http://hl7.org/fhir/Profile/CarePlan")
public class CarePlan extends DomainResource {

    public enum CarePlanStatus {
        /**
         * The plan is in development or awaiting use but is not yet intended to be acted upon.
         */
        PLANNED, 
        /**
         * The plan is intended to be followed and used as part of patient care.
         */
        ACTIVE, 
        /**
         * The plan is no longer in use and is not expected to be followed or used in patient care.
         */
        COMPLETED, 
        /**
         * added to help the parsers
         */
        NULL;
        public static CarePlanStatus fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("planned".equals(codeString))
          return PLANNED;
        if ("active".equals(codeString))
          return ACTIVE;
        if ("completed".equals(codeString))
          return COMPLETED;
        throw new Exception("Unknown CarePlanStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PLANNED: return "planned";
            case ACTIVE: return "active";
            case COMPLETED: return "completed";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case PLANNED: return "";
            case ACTIVE: return "";
            case COMPLETED: return "";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case PLANNED: return "The plan is in development or awaiting use but is not yet intended to be acted upon.";
            case ACTIVE: return "The plan is intended to be followed and used as part of patient care.";
            case COMPLETED: return "The plan is no longer in use and is not expected to be followed or used in patient care.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PLANNED: return "planned";
            case ACTIVE: return "active";
            case COMPLETED: return "completed";
            default: return "?";
          }
        }
    }

  public static class CarePlanStatusEnumFactory implements EnumFactory<CarePlanStatus> {
    public CarePlanStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("planned".equals(codeString))
          return CarePlanStatus.PLANNED;
        if ("active".equals(codeString))
          return CarePlanStatus.ACTIVE;
        if ("completed".equals(codeString))
          return CarePlanStatus.COMPLETED;
        throw new IllegalArgumentException("Unknown CarePlanStatus code '"+codeString+"'");
        }
    public String toCode(CarePlanStatus code) {
      if (code == CarePlanStatus.PLANNED)
        return "planned";
      if (code == CarePlanStatus.ACTIVE)
        return "active";
      if (code == CarePlanStatus.COMPLETED)
        return "completed";
      return "?";
      }
    }

    public enum CarePlanGoalStatus {
        /**
         * The goal is being sought but has not yet been reached.  (Also applies if goal was reached in the past but there has been regression and goal is being sought again).
         */
        INPROGRESS, 
        /**
         * The goal has been met and no further action is needed.
         */
        ACHIEVED, 
        /**
         * The goal has been met, but ongoing activity is needed to sustain the goal objective.
         */
        SUSTAINING, 
        /**
         * The goal is no longer being sought.
         */
        CANCELLED, 
        /**
         * added to help the parsers
         */
        NULL;
        public static CarePlanGoalStatus fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("in-progress".equals(codeString))
          return INPROGRESS;
        if ("achieved".equals(codeString))
          return ACHIEVED;
        if ("sustaining".equals(codeString))
          return SUSTAINING;
        if ("cancelled".equals(codeString))
          return CANCELLED;
        throw new Exception("Unknown CarePlanGoalStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case INPROGRESS: return "in-progress";
            case ACHIEVED: return "achieved";
            case SUSTAINING: return "sustaining";
            case CANCELLED: return "cancelled";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case INPROGRESS: return "";
            case ACHIEVED: return "";
            case SUSTAINING: return "";
            case CANCELLED: return "";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case INPROGRESS: return "The goal is being sought but has not yet been reached.  (Also applies if goal was reached in the past but there has been regression and goal is being sought again).";
            case ACHIEVED: return "The goal has been met and no further action is needed.";
            case SUSTAINING: return "The goal has been met, but ongoing activity is needed to sustain the goal objective.";
            case CANCELLED: return "The goal is no longer being sought.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case INPROGRESS: return "in-progress";
            case ACHIEVED: return "achieved";
            case SUSTAINING: return "sustaining";
            case CANCELLED: return "cancelled";
            default: return "?";
          }
        }
    }

  public static class CarePlanGoalStatusEnumFactory implements EnumFactory<CarePlanGoalStatus> {
    public CarePlanGoalStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("in-progress".equals(codeString))
          return CarePlanGoalStatus.INPROGRESS;
        if ("achieved".equals(codeString))
          return CarePlanGoalStatus.ACHIEVED;
        if ("sustaining".equals(codeString))
          return CarePlanGoalStatus.SUSTAINING;
        if ("cancelled".equals(codeString))
          return CarePlanGoalStatus.CANCELLED;
        throw new IllegalArgumentException("Unknown CarePlanGoalStatus code '"+codeString+"'");
        }
    public String toCode(CarePlanGoalStatus code) {
      if (code == CarePlanGoalStatus.INPROGRESS)
        return "in-progress";
      if (code == CarePlanGoalStatus.ACHIEVED)
        return "achieved";
      if (code == CarePlanGoalStatus.SUSTAINING)
        return "sustaining";
      if (code == CarePlanGoalStatus.CANCELLED)
        return "cancelled";
      return "?";
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
        public static CarePlanActivityStatus fromCode(String codeString) throws Exception {
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
        throw new Exception("Unknown CarePlanActivityStatus code '"+codeString+"'");
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
            case NOTSTARTED: return "not-started";
            case SCHEDULED: return "scheduled";
            case INPROGRESS: return "in-progress";
            case ONHOLD: return "on-hold";
            case COMPLETED: return "completed";
            case CANCELLED: return "cancelled";
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
    }

    public enum CarePlanActivityCategory {
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
        public static CarePlanActivityCategory fromCode(String codeString) throws Exception {
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
        throw new Exception("Unknown CarePlanActivityCategory code '"+codeString+"'");
        }
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

  public static class CarePlanActivityCategoryEnumFactory implements EnumFactory<CarePlanActivityCategory> {
    public CarePlanActivityCategory fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("diet".equals(codeString))
          return CarePlanActivityCategory.DIET;
        if ("drug".equals(codeString))
          return CarePlanActivityCategory.DRUG;
        if ("encounter".equals(codeString))
          return CarePlanActivityCategory.ENCOUNTER;
        if ("observation".equals(codeString))
          return CarePlanActivityCategory.OBSERVATION;
        if ("procedure".equals(codeString))
          return CarePlanActivityCategory.PROCEDURE;
        if ("supply".equals(codeString))
          return CarePlanActivityCategory.SUPPLY;
        if ("other".equals(codeString))
          return CarePlanActivityCategory.OTHER;
        throw new IllegalArgumentException("Unknown CarePlanActivityCategory code '"+codeString+"'");
        }
    public String toCode(CarePlanActivityCategory code) {
      if (code == CarePlanActivityCategory.DIET)
        return "diet";
      if (code == CarePlanActivityCategory.DRUG)
        return "drug";
      if (code == CarePlanActivityCategory.ENCOUNTER)
        return "encounter";
      if (code == CarePlanActivityCategory.OBSERVATION)
        return "observation";
      if (code == CarePlanActivityCategory.PROCEDURE)
        return "procedure";
      if (code == CarePlanActivityCategory.SUPPLY)
        return "supply";
      if (code == CarePlanActivityCategory.OTHER)
        return "other";
      return "?";
      }
    }

    @Block()
    public static class CarePlanParticipantComponent extends BackboneElement {
        /**
         * Indicates specific responsibility of an individual within the care plan.  E.g. "Primary physician", "Team coordinator", "Caregiver", etc.
         */
        @Child(name="role", type={CodeableConcept.class}, order=1, min=0, max=1)
        @Description(shortDefinition="Type of involvement", formalDefinition="Indicates specific responsibility of an individual within the care plan.  E.g. 'Primary physician', 'Team coordinator', 'Caregiver', etc." )
        protected CodeableConcept role;

        /**
         * The specific person or organization who is participating/expected to participate in the care plan.
         */
        @Child(name="member", type={Practitioner.class, RelatedPerson.class, Patient.class, Organization.class}, order=2, min=1, max=1)
        @Description(shortDefinition="Who is involved", formalDefinition="The specific person or organization who is participating/expected to participate in the care plan." )
        protected Reference member;

        /**
         * The actual object that is the target of the reference (The specific person or organization who is participating/expected to participate in the care plan.)
         */
        protected Resource memberTarget;

        private static final long serialVersionUID = -466811117L;

      public CarePlanParticipantComponent() {
        super();
      }

      public CarePlanParticipantComponent(Reference member) {
        super();
        this.member = member;
      }

        /**
         * @return {@link #role} (Indicates specific responsibility of an individual within the care plan.  E.g. "Primary physician", "Team coordinator", "Caregiver", etc.)
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
         * @param value {@link #role} (Indicates specific responsibility of an individual within the care plan.  E.g. "Primary physician", "Team coordinator", "Caregiver", etc.)
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
          childrenList.add(new Property("role", "CodeableConcept", "Indicates specific responsibility of an individual within the care plan.  E.g. 'Primary physician', 'Team coordinator', 'Caregiver', etc.", 0, java.lang.Integer.MAX_VALUE, role));
          childrenList.add(new Property("member", "Reference(Practitioner|RelatedPerson|Patient|Organization)", "The specific person or organization who is participating/expected to participate in the care plan.", 0, java.lang.Integer.MAX_VALUE, member));
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

  }

    @Block()
    public static class CarePlanGoalComponent extends BackboneElement {
        /**
         * Human-readable description of a specific desired objective of the care plan.
         */
        @Child(name="description", type={StringType.class}, order=1, min=1, max=1)
        @Description(shortDefinition="What's the desired outcome?", formalDefinition="Human-readable description of a specific desired objective of the care plan." )
        protected StringType description;

        /**
         * Indicates whether the goal has been reached and is still considered relevant.
         */
        @Child(name="status", type={CodeType.class}, order=2, min=0, max=1)
        @Description(shortDefinition="in-progress | achieved | sustaining | cancelled", formalDefinition="Indicates whether the goal has been reached and is still considered relevant." )
        protected Enumeration<CarePlanGoalStatus> status;

        /**
         * Any comments related to the goal.
         */
        @Child(name="notes", type={StringType.class}, order=3, min=0, max=1)
        @Description(shortDefinition="Comments about the goal", formalDefinition="Any comments related to the goal." )
        protected StringType notes;

        /**
         * The identified conditions that this goal relates to - the condition that caused it to be created, or that it is intended to address.
         */
        @Child(name="concern", type={Condition.class}, order=4, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="Health issues this goal addresses", formalDefinition="The identified conditions that this goal relates to - the condition that caused it to be created, or that it is intended to address." )
        protected List<Reference> concern;
        /**
         * The actual objects that are the target of the reference (The identified conditions that this goal relates to - the condition that caused it to be created, or that it is intended to address.)
         */
        protected List<Condition> concernTarget;


        private static final long serialVersionUID = -1557229012L;

      public CarePlanGoalComponent() {
        super();
      }

      public CarePlanGoalComponent(StringType description) {
        super();
        this.description = description;
      }

        /**
         * @return {@link #description} (Human-readable description of a specific desired objective of the care plan.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public StringType getDescriptionElement() { 
          if (this.description == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CarePlanGoalComponent.description");
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
         * @param value {@link #description} (Human-readable description of a specific desired objective of the care plan.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public CarePlanGoalComponent setDescriptionElement(StringType value) { 
          this.description = value;
          return this;
        }

        /**
         * @return Human-readable description of a specific desired objective of the care plan.
         */
        public String getDescription() { 
          return this.description == null ? null : this.description.getValue();
        }

        /**
         * @param value Human-readable description of a specific desired objective of the care plan.
         */
        public CarePlanGoalComponent setDescription(String value) { 
            if (this.description == null)
              this.description = new StringType();
            this.description.setValue(value);
          return this;
        }

        /**
         * @return {@link #status} (Indicates whether the goal has been reached and is still considered relevant.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
         */
        public Enumeration<CarePlanGoalStatus> getStatusElement() { 
          if (this.status == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CarePlanGoalComponent.status");
            else if (Configuration.doAutoCreate())
              this.status = new Enumeration<CarePlanGoalStatus>(new CarePlanGoalStatusEnumFactory()); // bb
          return this.status;
        }

        public boolean hasStatusElement() { 
          return this.status != null && !this.status.isEmpty();
        }

        public boolean hasStatus() { 
          return this.status != null && !this.status.isEmpty();
        }

        /**
         * @param value {@link #status} (Indicates whether the goal has been reached and is still considered relevant.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
         */
        public CarePlanGoalComponent setStatusElement(Enumeration<CarePlanGoalStatus> value) { 
          this.status = value;
          return this;
        }

        /**
         * @return Indicates whether the goal has been reached and is still considered relevant.
         */
        public CarePlanGoalStatus getStatus() { 
          return this.status == null ? null : this.status.getValue();
        }

        /**
         * @param value Indicates whether the goal has been reached and is still considered relevant.
         */
        public CarePlanGoalComponent setStatus(CarePlanGoalStatus value) { 
          if (value == null)
            this.status = null;
          else {
            if (this.status == null)
              this.status = new Enumeration<CarePlanGoalStatus>(new CarePlanGoalStatusEnumFactory());
            this.status.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #notes} (Any comments related to the goal.). This is the underlying object with id, value and extensions. The accessor "getNotes" gives direct access to the value
         */
        public StringType getNotesElement() { 
          if (this.notes == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CarePlanGoalComponent.notes");
            else if (Configuration.doAutoCreate())
              this.notes = new StringType(); // bb
          return this.notes;
        }

        public boolean hasNotesElement() { 
          return this.notes != null && !this.notes.isEmpty();
        }

        public boolean hasNotes() { 
          return this.notes != null && !this.notes.isEmpty();
        }

        /**
         * @param value {@link #notes} (Any comments related to the goal.). This is the underlying object with id, value and extensions. The accessor "getNotes" gives direct access to the value
         */
        public CarePlanGoalComponent setNotesElement(StringType value) { 
          this.notes = value;
          return this;
        }

        /**
         * @return Any comments related to the goal.
         */
        public String getNotes() { 
          return this.notes == null ? null : this.notes.getValue();
        }

        /**
         * @param value Any comments related to the goal.
         */
        public CarePlanGoalComponent setNotes(String value) { 
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
         * @return {@link #concern} (The identified conditions that this goal relates to - the condition that caused it to be created, or that it is intended to address.)
         */
        public List<Reference> getConcern() { 
          if (this.concern == null)
            this.concern = new ArrayList<Reference>();
          return this.concern;
        }

        public boolean hasConcern() { 
          if (this.concern == null)
            return false;
          for (Reference item : this.concern)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #concern} (The identified conditions that this goal relates to - the condition that caused it to be created, or that it is intended to address.)
         */
    // syntactic sugar
        public Reference addConcern() { //3
          Reference t = new Reference();
          if (this.concern == null)
            this.concern = new ArrayList<Reference>();
          this.concern.add(t);
          return t;
        }

        /**
         * @return {@link #concern} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. The identified conditions that this goal relates to - the condition that caused it to be created, or that it is intended to address.)
         */
        public List<Condition> getConcernTarget() { 
          if (this.concernTarget == null)
            this.concernTarget = new ArrayList<Condition>();
          return this.concernTarget;
        }

    // syntactic sugar
        /**
         * @return {@link #concern} (Add an actual object that is the target of the reference. The reference library doesn't use these, but you can use this to hold the resources if you resolvethemt. The identified conditions that this goal relates to - the condition that caused it to be created, or that it is intended to address.)
         */
        public Condition addConcernTarget() { 
          Condition r = new Condition();
          if (this.concernTarget == null)
            this.concernTarget = new ArrayList<Condition>();
          this.concernTarget.add(r);
          return r;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("description", "string", "Human-readable description of a specific desired objective of the care plan.", 0, java.lang.Integer.MAX_VALUE, description));
          childrenList.add(new Property("status", "code", "Indicates whether the goal has been reached and is still considered relevant.", 0, java.lang.Integer.MAX_VALUE, status));
          childrenList.add(new Property("notes", "string", "Any comments related to the goal.", 0, java.lang.Integer.MAX_VALUE, notes));
          childrenList.add(new Property("concern", "Reference(Condition)", "The identified conditions that this goal relates to - the condition that caused it to be created, or that it is intended to address.", 0, java.lang.Integer.MAX_VALUE, concern));
        }

      public CarePlanGoalComponent copy() {
        CarePlanGoalComponent dst = new CarePlanGoalComponent();
        copyValues(dst);
        dst.description = description == null ? null : description.copy();
        dst.status = status == null ? null : status.copy();
        dst.notes = notes == null ? null : notes.copy();
        if (concern != null) {
          dst.concern = new ArrayList<Reference>();
          for (Reference i : concern)
            dst.concern.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof CarePlanGoalComponent))
          return false;
        CarePlanGoalComponent o = (CarePlanGoalComponent) other;
        return compareDeep(description, o.description, true) && compareDeep(status, o.status, true) && compareDeep(notes, o.notes, true)
           && compareDeep(concern, o.concern, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof CarePlanGoalComponent))
          return false;
        CarePlanGoalComponent o = (CarePlanGoalComponent) other;
        return compareValues(description, o.description, true) && compareValues(status, o.status, true) && compareValues(notes, o.notes, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (description == null || description.isEmpty()) && (status == null || status.isEmpty())
           && (notes == null || notes.isEmpty()) && (concern == null || concern.isEmpty());
      }

  }

    @Block()
    public static class CarePlanActivityComponent extends BackboneElement {
        /**
         * Internal reference that identifies the goals that this activity is intended to contribute towards meeting.
         */
        @Child(name="goal", type={UriType.class}, order=1, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="Goals this activity relates to", formalDefinition="Internal reference that identifies the goals that this activity is intended to contribute towards meeting." )
        protected List<UriType> goal;

        /**
         * Identifies what progress is being made for the specific activity.
         */
        @Child(name="status", type={CodeType.class}, order=2, min=0, max=1)
        @Description(shortDefinition="not-started | scheduled | in-progress | on-hold | completed | cancelled", formalDefinition="Identifies what progress is being made for the specific activity." )
        protected Enumeration<CarePlanActivityStatus> status;

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
        protected CarePlanActivitySimpleComponent simple;

        private static final long serialVersionUID = -1536095647L;

      public CarePlanActivityComponent() {
        super();
      }

      public CarePlanActivityComponent(BooleanType prohibited) {
        super();
        this.prohibited = prohibited;
      }

        /**
         * @return {@link #goal} (Internal reference that identifies the goals that this activity is intended to contribute towards meeting.)
         */
        public List<UriType> getGoal() { 
          if (this.goal == null)
            this.goal = new ArrayList<UriType>();
          return this.goal;
        }

        public boolean hasGoal() { 
          if (this.goal == null)
            return false;
          for (UriType item : this.goal)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #goal} (Internal reference that identifies the goals that this activity is intended to contribute towards meeting.)
         */
    // syntactic sugar
        public UriType addGoalElement() {//2 
          UriType t = new UriType();
          if (this.goal == null)
            this.goal = new ArrayList<UriType>();
          this.goal.add(t);
          return t;
        }

        /**
         * @param value {@link #goal} (Internal reference that identifies the goals that this activity is intended to contribute towards meeting.)
         */
        public CarePlanActivityComponent addGoal(String value) { //1
          UriType t = new UriType();
          t.setValue(value);
          if (this.goal == null)
            this.goal = new ArrayList<UriType>();
          this.goal.add(t);
          return this;
        }

        /**
         * @param value {@link #goal} (Internal reference that identifies the goals that this activity is intended to contribute towards meeting.)
         */
        public boolean hasGoal(String value) { 
          if (this.goal == null)
            return false;
          for (UriType v : this.goal)
            if (v.equals(value)) // uri
              return true;
          return false;
        }

        /**
         * @return {@link #status} (Identifies what progress is being made for the specific activity.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
         */
        public Enumeration<CarePlanActivityStatus> getStatusElement() { 
          if (this.status == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CarePlanActivityComponent.status");
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
        public CarePlanActivityComponent setStatusElement(Enumeration<CarePlanActivityStatus> value) { 
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
        public CarePlanActivityComponent setStatus(CarePlanActivityStatus value) { 
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
         * @return {@link #prohibited} (If true, indicates that the described activity is one that must NOT be engaged in when following the plan.). This is the underlying object with id, value and extensions. The accessor "getProhibited" gives direct access to the value
         */
        public BooleanType getProhibitedElement() { 
          if (this.prohibited == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CarePlanActivityComponent.prohibited");
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
        public CarePlanActivityComponent setProhibitedElement(BooleanType value) { 
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
        public CarePlanActivityComponent setProhibited(boolean value) { 
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
              throw new Error("Attempt to auto-create CarePlanActivityComponent.notes");
            else if (Configuration.doAutoCreate())
              this.notes = new StringType(); // bb
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
        public CarePlanActivityComponent setNotesElement(StringType value) { 
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
        public CarePlanActivityComponent setNotes(String value) { 
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
              throw new Error("Attempt to auto-create CarePlanActivityComponent.detail");
            else if (Configuration.doAutoCreate())
              this.detail = new Reference(); // cc
          return this.detail;
        }

        public boolean hasDetail() { 
          return this.detail != null && !this.detail.isEmpty();
        }

        /**
         * @param value {@link #detail} (The details of the proposed activity represented in a specific resource.)
         */
        public CarePlanActivityComponent setDetail(Reference value) { 
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
        public CarePlanActivityComponent setDetailTarget(Resource value) { 
          this.detailTarget = value;
          return this;
        }

        /**
         * @return {@link #simple} (A simple summary of details suitable for a general care plan system (e.g. form driven) that doesn't know about specific resources such as procedure etc.)
         */
        public CarePlanActivitySimpleComponent getSimple() { 
          if (this.simple == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CarePlanActivityComponent.simple");
            else if (Configuration.doAutoCreate())
              this.simple = new CarePlanActivitySimpleComponent(); // cc
          return this.simple;
        }

        public boolean hasSimple() { 
          return this.simple != null && !this.simple.isEmpty();
        }

        /**
         * @param value {@link #simple} (A simple summary of details suitable for a general care plan system (e.g. form driven) that doesn't know about specific resources such as procedure etc.)
         */
        public CarePlanActivityComponent setSimple(CarePlanActivitySimpleComponent value) { 
          this.simple = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("goal", "uri", "Internal reference that identifies the goals that this activity is intended to contribute towards meeting.", 0, java.lang.Integer.MAX_VALUE, goal));
          childrenList.add(new Property("status", "code", "Identifies what progress is being made for the specific activity.", 0, java.lang.Integer.MAX_VALUE, status));
          childrenList.add(new Property("prohibited", "boolean", "If true, indicates that the described activity is one that must NOT be engaged in when following the plan.", 0, java.lang.Integer.MAX_VALUE, prohibited));
          childrenList.add(new Property("actionResulting", "Reference(Any)", "Resources that describe follow-on actions resulting from the plan, such as drug prescriptions, encounter records, appointments, etc.", 0, java.lang.Integer.MAX_VALUE, actionResulting));
          childrenList.add(new Property("notes", "string", "Notes about the execution of the activity.", 0, java.lang.Integer.MAX_VALUE, notes));
          childrenList.add(new Property("detail", "Reference(Procedure|MedicationPrescription|DiagnosticOrder|Encounter|Supply)", "The details of the proposed activity represented in a specific resource.", 0, java.lang.Integer.MAX_VALUE, detail));
          childrenList.add(new Property("simple", "", "A simple summary of details suitable for a general care plan system (e.g. form driven) that doesn't know about specific resources such as procedure etc.", 0, java.lang.Integer.MAX_VALUE, simple));
        }

      public CarePlanActivityComponent copy() {
        CarePlanActivityComponent dst = new CarePlanActivityComponent();
        copyValues(dst);
        if (goal != null) {
          dst.goal = new ArrayList<UriType>();
          for (UriType i : goal)
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

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof CarePlanActivityComponent))
          return false;
        CarePlanActivityComponent o = (CarePlanActivityComponent) other;
        return compareDeep(goal, o.goal, true) && compareDeep(status, o.status, true) && compareDeep(prohibited, o.prohibited, true)
           && compareDeep(actionResulting, o.actionResulting, true) && compareDeep(notes, o.notes, true) && compareDeep(detail, o.detail, true)
           && compareDeep(simple, o.simple, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof CarePlanActivityComponent))
          return false;
        CarePlanActivityComponent o = (CarePlanActivityComponent) other;
        return compareValues(goal, o.goal, true) && compareValues(status, o.status, true) && compareValues(prohibited, o.prohibited, true)
           && compareValues(notes, o.notes, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (goal == null || goal.isEmpty()) && (status == null || status.isEmpty())
           && (prohibited == null || prohibited.isEmpty()) && (actionResulting == null || actionResulting.isEmpty())
           && (notes == null || notes.isEmpty()) && (detail == null || detail.isEmpty()) && (simple == null || simple.isEmpty())
          ;
      }

  }

    @Block()
    public static class CarePlanActivitySimpleComponent extends BackboneElement {
        /**
         * High-level categorization of the type of activity in a care plan.
         */
        @Child(name="category", type={CodeType.class}, order=1, min=1, max=1)
        @Description(shortDefinition="diet | drug | encounter | observation | procedure | supply | other", formalDefinition="High-level categorization of the type of activity in a care plan." )
        protected Enumeration<CarePlanActivityCategory> category;

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

        private static final long serialVersionUID = 1028930313L;

      public CarePlanActivitySimpleComponent() {
        super();
      }

      public CarePlanActivitySimpleComponent(Enumeration<CarePlanActivityCategory> category) {
        super();
        this.category = category;
      }

        /**
         * @return {@link #category} (High-level categorization of the type of activity in a care plan.). This is the underlying object with id, value and extensions. The accessor "getCategory" gives direct access to the value
         */
        public Enumeration<CarePlanActivityCategory> getCategoryElement() { 
          if (this.category == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CarePlanActivitySimpleComponent.category");
            else if (Configuration.doAutoCreate())
              this.category = new Enumeration<CarePlanActivityCategory>(new CarePlanActivityCategoryEnumFactory()); // bb
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
        public CarePlanActivitySimpleComponent setCategoryElement(Enumeration<CarePlanActivityCategory> value) { 
          this.category = value;
          return this;
        }

        /**
         * @return High-level categorization of the type of activity in a care plan.
         */
        public CarePlanActivityCategory getCategory() { 
          return this.category == null ? null : this.category.getValue();
        }

        /**
         * @param value High-level categorization of the type of activity in a care plan.
         */
        public CarePlanActivitySimpleComponent setCategory(CarePlanActivityCategory value) { 
            if (this.category == null)
              this.category = new Enumeration<CarePlanActivityCategory>(new CarePlanActivityCategoryEnumFactory());
            this.category.setValue(value);
          return this;
        }

        /**
         * @return {@link #code} (Detailed description of the type of activity.  E.g. What lab test, what procedure, what kind of encounter.)
         */
        public CodeableConcept getCode() { 
          if (this.code == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CarePlanActivitySimpleComponent.code");
            else if (Configuration.doAutoCreate())
              this.code = new CodeableConcept(); // cc
          return this.code;
        }

        public boolean hasCode() { 
          return this.code != null && !this.code.isEmpty();
        }

        /**
         * @param value {@link #code} (Detailed description of the type of activity.  E.g. What lab test, what procedure, what kind of encounter.)
         */
        public CarePlanActivitySimpleComponent setCode(CodeableConcept value) { 
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
        public CarePlanActivitySimpleComponent setScheduled(Type value) { 
          this.scheduled = value;
          return this;
        }

        /**
         * @return {@link #location} (Identifies the facility where the activity will occur.  E.g. home, hospital, specific clinic, etc.)
         */
        public Reference getLocation() { 
          if (this.location == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CarePlanActivitySimpleComponent.location");
            else if (Configuration.doAutoCreate())
              this.location = new Reference(); // cc
          return this.location;
        }

        public boolean hasLocation() { 
          return this.location != null && !this.location.isEmpty();
        }

        /**
         * @param value {@link #location} (Identifies the facility where the activity will occur.  E.g. home, hospital, specific clinic, etc.)
         */
        public CarePlanActivitySimpleComponent setLocation(Reference value) { 
          this.location = value;
          return this;
        }

        /**
         * @return {@link #location} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Identifies the facility where the activity will occur.  E.g. home, hospital, specific clinic, etc.)
         */
        public Location getLocationTarget() { 
          if (this.locationTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CarePlanActivitySimpleComponent.location");
            else if (Configuration.doAutoCreate())
              this.locationTarget = new Location(); // aa
          return this.locationTarget;
        }

        /**
         * @param value {@link #location} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Identifies the facility where the activity will occur.  E.g. home, hospital, specific clinic, etc.)
         */
        public CarePlanActivitySimpleComponent setLocationTarget(Location value) { 
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
              throw new Error("Attempt to auto-create CarePlanActivitySimpleComponent.product");
            else if (Configuration.doAutoCreate())
              this.product = new Reference(); // cc
          return this.product;
        }

        public boolean hasProduct() { 
          return this.product != null && !this.product.isEmpty();
        }

        /**
         * @param value {@link #product} (Identifies the food, drug or other product being consumed or supplied in the activity.)
         */
        public CarePlanActivitySimpleComponent setProduct(Reference value) { 
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
        public CarePlanActivitySimpleComponent setProductTarget(Resource value) { 
          this.productTarget = value;
          return this;
        }

        /**
         * @return {@link #dailyAmount} (Identifies the quantity expected to be consumed in a given day.)
         */
        public Quantity getDailyAmount() { 
          if (this.dailyAmount == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CarePlanActivitySimpleComponent.dailyAmount");
            else if (Configuration.doAutoCreate())
              this.dailyAmount = new Quantity(); // cc
          return this.dailyAmount;
        }

        public boolean hasDailyAmount() { 
          return this.dailyAmount != null && !this.dailyAmount.isEmpty();
        }

        /**
         * @param value {@link #dailyAmount} (Identifies the quantity expected to be consumed in a given day.)
         */
        public CarePlanActivitySimpleComponent setDailyAmount(Quantity value) { 
          this.dailyAmount = value;
          return this;
        }

        /**
         * @return {@link #quantity} (Identifies the quantity expected to be supplied.)
         */
        public Quantity getQuantity() { 
          if (this.quantity == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CarePlanActivitySimpleComponent.quantity");
            else if (Configuration.doAutoCreate())
              this.quantity = new Quantity(); // cc
          return this.quantity;
        }

        public boolean hasQuantity() { 
          return this.quantity != null && !this.quantity.isEmpty();
        }

        /**
         * @param value {@link #quantity} (Identifies the quantity expected to be supplied.)
         */
        public CarePlanActivitySimpleComponent setQuantity(Quantity value) { 
          this.quantity = value;
          return this;
        }

        /**
         * @return {@link #details} (This provides a textual description of constraints on the activity occurrence, including relation to other activities.  It may also include objectives, pre-conditions and end-conditions.  Finally, it may convey specifics about the activity such as body site, method, route, etc.). This is the underlying object with id, value and extensions. The accessor "getDetails" gives direct access to the value
         */
        public StringType getDetailsElement() { 
          if (this.details == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CarePlanActivitySimpleComponent.details");
            else if (Configuration.doAutoCreate())
              this.details = new StringType(); // bb
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
        public CarePlanActivitySimpleComponent setDetailsElement(StringType value) { 
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
        public CarePlanActivitySimpleComponent setDetails(String value) { 
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

      public CarePlanActivitySimpleComponent copy() {
        CarePlanActivitySimpleComponent dst = new CarePlanActivitySimpleComponent();
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

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof CarePlanActivitySimpleComponent))
          return false;
        CarePlanActivitySimpleComponent o = (CarePlanActivitySimpleComponent) other;
        return compareDeep(category, o.category, true) && compareDeep(code, o.code, true) && compareDeep(scheduled, o.scheduled, true)
           && compareDeep(location, o.location, true) && compareDeep(performer, o.performer, true) && compareDeep(product, o.product, true)
           && compareDeep(dailyAmount, o.dailyAmount, true) && compareDeep(quantity, o.quantity, true) && compareDeep(details, o.details, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof CarePlanActivitySimpleComponent))
          return false;
        CarePlanActivitySimpleComponent o = (CarePlanActivitySimpleComponent) other;
        return compareValues(category, o.category, true) && compareValues(details, o.details, true);
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
    @Child(name = "identifier", type = {Identifier.class}, order = 0, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="External Ids for this plan", formalDefinition="This records identifiers associated with this care plan that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)." )
    protected List<Identifier> identifier;

    /**
     * Identifies the patient/subject whose intended care is described by the plan.
     */
    @Child(name = "patient", type = {Patient.class}, order = 1, min = 0, max = 1)
    @Description(shortDefinition="Who care plan is for", formalDefinition="Identifies the patient/subject whose intended care is described by the plan." )
    protected Reference patient;

    /**
     * The actual object that is the target of the reference (Identifies the patient/subject whose intended care is described by the plan.)
     */
    protected Patient patientTarget;

    /**
     * Indicates whether the plan is currently being acted upon, represents future intentions or is now just historical record.
     */
    @Child(name = "status", type = {CodeType.class}, order = 2, min = 1, max = 1)
    @Description(shortDefinition="planned | active | completed", formalDefinition="Indicates whether the plan is currently being acted upon, represents future intentions or is now just historical record." )
    protected Enumeration<CarePlanStatus> status;

    /**
     * Indicates when the plan did (or is intended to) come into effect and end.
     */
    @Child(name = "period", type = {Period.class}, order = 3, min = 0, max = 1)
    @Description(shortDefinition="Time period plan covers", formalDefinition="Indicates when the plan did (or is intended to) come into effect and end." )
    protected Period period;

    /**
     * Identifies the most recent date on which the plan has been revised.
     */
    @Child(name = "modified", type = {DateTimeType.class}, order = 4, min = 0, max = 1)
    @Description(shortDefinition="When last updated", formalDefinition="Identifies the most recent date on which the plan has been revised." )
    protected DateTimeType modified;

    /**
     * Identifies the conditions/problems/concerns/diagnoses/etc. whose management and/or mitigation are handled by this plan.
     */
    @Child(name = "concern", type = {Condition.class}, order = 5, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Health issues this plan addresses", formalDefinition="Identifies the conditions/problems/concerns/diagnoses/etc. whose management and/or mitigation are handled by this plan." )
    protected List<Reference> concern;
    /**
     * The actual objects that are the target of the reference (Identifies the conditions/problems/concerns/diagnoses/etc. whose management and/or mitigation are handled by this plan.)
     */
    protected List<Condition> concernTarget;


    /**
     * Identifies all people and organizations who are expected to be involved in the care envisioned by this plan.
     */
    @Child(name = "participant", type = {}, order = 6, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Who's involved in plan?", formalDefinition="Identifies all people and organizations who are expected to be involved in the care envisioned by this plan." )
    protected List<CarePlanParticipantComponent> participant;

    /**
     * Describes the intended objective(s) of carrying out the Care Plan.
     */
    @Child(name = "goal", type = {}, order = 7, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Desired outcome of plan", formalDefinition="Describes the intended objective(s) of carrying out the Care Plan." )
    protected List<CarePlanGoalComponent> goal;

    /**
     * Identifies a planned action to occur as part of the plan.  For example, a medication to be used, lab tests to perform, self-monitoring, education, etc.
     */
    @Child(name = "activity", type = {}, order = 8, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Action to occur as part of plan", formalDefinition="Identifies a planned action to occur as part of the plan.  For example, a medication to be used, lab tests to perform, self-monitoring, education, etc." )
    protected List<CarePlanActivityComponent> activity;

    /**
     * General notes about the care plan not covered elsewhere.
     */
    @Child(name = "notes", type = {StringType.class}, order = 9, min = 0, max = 1)
    @Description(shortDefinition="Comments about the plan", formalDefinition="General notes about the care plan not covered elsewhere." )
    protected StringType notes;

    private static final long serialVersionUID = -1730021244L;

    public CarePlan() {
      super();
    }

    public CarePlan(Enumeration<CarePlanStatus> status) {
      super();
      this.status = status;
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
          throw new Error("Attempt to auto-create CarePlan.patient");
        else if (Configuration.doAutoCreate())
          this.patient = new Reference(); // cc
      return this.patient;
    }

    public boolean hasPatient() { 
      return this.patient != null && !this.patient.isEmpty();
    }

    /**
     * @param value {@link #patient} (Identifies the patient/subject whose intended care is described by the plan.)
     */
    public CarePlan setPatient(Reference value) { 
      this.patient = value;
      return this;
    }

    /**
     * @return {@link #patient} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Identifies the patient/subject whose intended care is described by the plan.)
     */
    public Patient getPatientTarget() { 
      if (this.patientTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CarePlan.patient");
        else if (Configuration.doAutoCreate())
          this.patientTarget = new Patient(); // aa
      return this.patientTarget;
    }

    /**
     * @param value {@link #patient} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Identifies the patient/subject whose intended care is described by the plan.)
     */
    public CarePlan setPatientTarget(Patient value) { 
      this.patientTarget = value;
      return this;
    }

    /**
     * @return {@link #status} (Indicates whether the plan is currently being acted upon, represents future intentions or is now just historical record.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
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
     * @param value {@link #status} (Indicates whether the plan is currently being acted upon, represents future intentions or is now just historical record.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public CarePlan setStatusElement(Enumeration<CarePlanStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return Indicates whether the plan is currently being acted upon, represents future intentions or is now just historical record.
     */
    public CarePlanStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value Indicates whether the plan is currently being acted upon, represents future intentions or is now just historical record.
     */
    public CarePlan setStatus(CarePlanStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<CarePlanStatus>(new CarePlanStatusEnumFactory());
        this.status.setValue(value);
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
     * @return {@link #concern} (Identifies the conditions/problems/concerns/diagnoses/etc. whose management and/or mitigation are handled by this plan.)
     */
    public List<Reference> getConcern() { 
      if (this.concern == null)
        this.concern = new ArrayList<Reference>();
      return this.concern;
    }

    public boolean hasConcern() { 
      if (this.concern == null)
        return false;
      for (Reference item : this.concern)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #concern} (Identifies the conditions/problems/concerns/diagnoses/etc. whose management and/or mitigation are handled by this plan.)
     */
    // syntactic sugar
    public Reference addConcern() { //3
      Reference t = new Reference();
      if (this.concern == null)
        this.concern = new ArrayList<Reference>();
      this.concern.add(t);
      return t;
    }

    /**
     * @return {@link #concern} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. Identifies the conditions/problems/concerns/diagnoses/etc. whose management and/or mitigation are handled by this plan.)
     */
    public List<Condition> getConcernTarget() { 
      if (this.concernTarget == null)
        this.concernTarget = new ArrayList<Condition>();
      return this.concernTarget;
    }

    // syntactic sugar
    /**
     * @return {@link #concern} (Add an actual object that is the target of the reference. The reference library doesn't use these, but you can use this to hold the resources if you resolvethemt. Identifies the conditions/problems/concerns/diagnoses/etc. whose management and/or mitigation are handled by this plan.)
     */
    public Condition addConcernTarget() { 
      Condition r = new Condition();
      if (this.concernTarget == null)
        this.concernTarget = new ArrayList<Condition>();
      this.concernTarget.add(r);
      return r;
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

    /**
     * @return {@link #goal} (Describes the intended objective(s) of carrying out the Care Plan.)
     */
    public List<CarePlanGoalComponent> getGoal() { 
      if (this.goal == null)
        this.goal = new ArrayList<CarePlanGoalComponent>();
      return this.goal;
    }

    public boolean hasGoal() { 
      if (this.goal == null)
        return false;
      for (CarePlanGoalComponent item : this.goal)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #goal} (Describes the intended objective(s) of carrying out the Care Plan.)
     */
    // syntactic sugar
    public CarePlanGoalComponent addGoal() { //3
      CarePlanGoalComponent t = new CarePlanGoalComponent();
      if (this.goal == null)
        this.goal = new ArrayList<CarePlanGoalComponent>();
      this.goal.add(t);
      return t;
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

    /**
     * @return {@link #notes} (General notes about the care plan not covered elsewhere.). This is the underlying object with id, value and extensions. The accessor "getNotes" gives direct access to the value
     */
    public StringType getNotesElement() { 
      if (this.notes == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CarePlan.notes");
        else if (Configuration.doAutoCreate())
          this.notes = new StringType(); // bb
      return this.notes;
    }

    public boolean hasNotesElement() { 
      return this.notes != null && !this.notes.isEmpty();
    }

    public boolean hasNotes() { 
      return this.notes != null && !this.notes.isEmpty();
    }

    /**
     * @param value {@link #notes} (General notes about the care plan not covered elsewhere.). This is the underlying object with id, value and extensions. The accessor "getNotes" gives direct access to the value
     */
    public CarePlan setNotesElement(StringType value) { 
      this.notes = value;
      return this;
    }

    /**
     * @return General notes about the care plan not covered elsewhere.
     */
    public String getNotes() { 
      return this.notes == null ? null : this.notes.getValue();
    }

    /**
     * @param value General notes about the care plan not covered elsewhere.
     */
    public CarePlan setNotes(String value) { 
      if (Utilities.noString(value))
        this.notes = null;
      else {
        if (this.notes == null)
          this.notes = new StringType();
        this.notes.setValue(value);
      }
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "This records identifiers associated with this care plan that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation).", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("patient", "Reference(Patient)", "Identifies the patient/subject whose intended care is described by the plan.", 0, java.lang.Integer.MAX_VALUE, patient));
        childrenList.add(new Property("status", "code", "Indicates whether the plan is currently being acted upon, represents future intentions or is now just historical record.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("period", "Period", "Indicates when the plan did (or is intended to) come into effect and end.", 0, java.lang.Integer.MAX_VALUE, period));
        childrenList.add(new Property("modified", "dateTime", "Identifies the most recent date on which the plan has been revised.", 0, java.lang.Integer.MAX_VALUE, modified));
        childrenList.add(new Property("concern", "Reference(Condition)", "Identifies the conditions/problems/concerns/diagnoses/etc. whose management and/or mitigation are handled by this plan.", 0, java.lang.Integer.MAX_VALUE, concern));
        childrenList.add(new Property("participant", "", "Identifies all people and organizations who are expected to be involved in the care envisioned by this plan.", 0, java.lang.Integer.MAX_VALUE, participant));
        childrenList.add(new Property("goal", "", "Describes the intended objective(s) of carrying out the Care Plan.", 0, java.lang.Integer.MAX_VALUE, goal));
        childrenList.add(new Property("activity", "", "Identifies a planned action to occur as part of the plan.  For example, a medication to be used, lab tests to perform, self-monitoring, education, etc.", 0, java.lang.Integer.MAX_VALUE, activity));
        childrenList.add(new Property("notes", "string", "General notes about the care plan not covered elsewhere.", 0, java.lang.Integer.MAX_VALUE, notes));
      }

      public CarePlan copy() {
        CarePlan dst = new CarePlan();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.patient = patient == null ? null : patient.copy();
        dst.status = status == null ? null : status.copy();
        dst.period = period == null ? null : period.copy();
        dst.modified = modified == null ? null : modified.copy();
        if (concern != null) {
          dst.concern = new ArrayList<Reference>();
          for (Reference i : concern)
            dst.concern.add(i.copy());
        };
        if (participant != null) {
          dst.participant = new ArrayList<CarePlanParticipantComponent>();
          for (CarePlanParticipantComponent i : participant)
            dst.participant.add(i.copy());
        };
        if (goal != null) {
          dst.goal = new ArrayList<CarePlanGoalComponent>();
          for (CarePlanGoalComponent i : goal)
            dst.goal.add(i.copy());
        };
        if (activity != null) {
          dst.activity = new ArrayList<CarePlanActivityComponent>();
          for (CarePlanActivityComponent i : activity)
            dst.activity.add(i.copy());
        };
        dst.notes = notes == null ? null : notes.copy();
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
        return compareDeep(identifier, o.identifier, true) && compareDeep(patient, o.patient, true) && compareDeep(status, o.status, true)
           && compareDeep(period, o.period, true) && compareDeep(modified, o.modified, true) && compareDeep(concern, o.concern, true)
           && compareDeep(participant, o.participant, true) && compareDeep(goal, o.goal, true) && compareDeep(activity, o.activity, true)
           && compareDeep(notes, o.notes, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof CarePlan))
          return false;
        CarePlan o = (CarePlan) other;
        return compareValues(status, o.status, true) && compareValues(modified, o.modified, true) && compareValues(notes, o.notes, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (patient == null || patient.isEmpty())
           && (status == null || status.isEmpty()) && (period == null || period.isEmpty()) && (modified == null || modified.isEmpty())
           && (concern == null || concern.isEmpty()) && (participant == null || participant.isEmpty())
           && (goal == null || goal.isEmpty()) && (activity == null || activity.isEmpty()) && (notes == null || notes.isEmpty())
          ;
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.CarePlan;
   }

    @SearchParamDefinition(name = "date", path = "CarePlan.period", description = "Time period plan covers", type = "date")
    public static final String SP_DATE = "date";
  @SearchParamDefinition(name="activitycode", path="CarePlan.activity.simple.code", description="Detail type of activity", type="token" )
  public static final String SP_ACTIVITYCODE = "activitycode";
  @SearchParamDefinition(name="activitydate", path="CarePlan.activity.simple.scheduled[x]", description="Specified date occurs within period specified by CarePlan.activity.timingSchedule", type="date" )
  public static final String SP_ACTIVITYDATE = "activitydate";
    @SearchParamDefinition(name = "activitydetail", path = "CarePlan.activity.detail", description = "Activity details defined in specific resource", type = "reference")
    public static final String SP_ACTIVITYDETAIL = "activitydetail";
    @SearchParamDefinition(name = "condition", path = "CarePlan.concern", description = "Health issues this plan addresses", type = "reference")
    public static final String SP_CONDITION = "condition";
    @SearchParamDefinition(name = "patient", path = "CarePlan.patient", description = "Who care plan is for", type = "reference")
    public static final String SP_PATIENT = "patient";
  @SearchParamDefinition(name="participant", path="CarePlan.participant.member", description="Who is involved", type="reference" )
  public static final String SP_PARTICIPANT = "participant";

}

