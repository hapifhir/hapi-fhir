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

// Generated on Sat, Aug 22, 2015 23:00-0400 for FHIR v0.5.0

import java.util.*;

import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.instance.model.annotations.ResourceDef;
import org.hl7.fhir.instance.model.annotations.SearchParamDefinition;
import org.hl7.fhir.instance.model.annotations.Child;
import org.hl7.fhir.instance.model.annotations.Description;
import org.hl7.fhir.instance.model.annotations.Block;
import org.hl7.fhir.instance.model.api.*;
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
         * The plan is intended to be followed and used as part of patient care
         */
        ACTIVE, 
        /**
         * The plan is no longer in use and is not expected to be followed or used in patient care
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
            case PLANNED: return "http://hl7.org/fhir/care-plan-status";
            case ACTIVE: return "http://hl7.org/fhir/care-plan-status";
            case COMPLETED: return "http://hl7.org/fhir/care-plan-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case PLANNED: return "The plan is in development or awaiting use but is not yet intended to be acted upon.";
            case ACTIVE: return "The plan is intended to be followed and used as part of patient care";
            case COMPLETED: return "The plan is no longer in use and is not expected to be followed or used in patient care";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PLANNED: return "Planned";
            case ACTIVE: return "Active";
            case COMPLETED: return "Completed";
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

    public enum CarePlanActivityCategory {
        /**
         * Plan for the patient to consume food of a specified nature
         */
        DIET, 
        /**
         * Plan for the patient to consume/receive a drug, vaccine or other product
         */
        DRUG, 
        /**
         * Plan to meet or communicate with the patient (in-patient, out-patient, phone call, etc.)
         */
        ENCOUNTER, 
        /**
         * Plan to capture information about a patient (vitals, labs, diagnostic images, etc.)
         */
        OBSERVATION, 
        /**
         * Plan to modify the patient in some way (surgery, physiotherapy, education, counseling, etc.)
         */
        PROCEDURE, 
        /**
         * Plan to provide something to the patient (medication, medical supply, etc.)
         */
        SUPPLY, 
        /**
         * Some other form of action
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
            case DIET: return "http://hl7.org/fhir/care-plan-activity-category";
            case DRUG: return "http://hl7.org/fhir/care-plan-activity-category";
            case ENCOUNTER: return "http://hl7.org/fhir/care-plan-activity-category";
            case OBSERVATION: return "http://hl7.org/fhir/care-plan-activity-category";
            case PROCEDURE: return "http://hl7.org/fhir/care-plan-activity-category";
            case SUPPLY: return "http://hl7.org/fhir/care-plan-activity-category";
            case OTHER: return "http://hl7.org/fhir/care-plan-activity-category";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case DIET: return "Plan for the patient to consume food of a specified nature";
            case DRUG: return "Plan for the patient to consume/receive a drug, vaccine or other product";
            case ENCOUNTER: return "Plan to meet or communicate with the patient (in-patient, out-patient, phone call, etc.)";
            case OBSERVATION: return "Plan to capture information about a patient (vitals, labs, diagnostic images, etc.)";
            case PROCEDURE: return "Plan to modify the patient in some way (surgery, physiotherapy, education, counseling, etc.)";
            case SUPPLY: return "Plan to provide something to the patient (medication, medical supply, etc.)";
            case OTHER: return "Some other form of action";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case DIET: return "Diet";
            case DRUG: return "Drug";
            case ENCOUNTER: return "Encounter";
            case OBSERVATION: return "Observation";
            case PROCEDURE: return "Procedure";
            case SUPPLY: return "Supply";
            case OTHER: return "Other";
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

    public enum CarePlanActivityStatus {
        /**
         * Activity is planned but no action has yet been taken
         */
        NOTSTARTED, 
        /**
         * Appointment or other booking has occurred but activity has not yet begun
         */
        SCHEDULED, 
        /**
         * Activity has been started but is not yet complete
         */
        INPROGRESS, 
        /**
         * Activity was started but has temporarily ceased with an expectation of resumption at a future time.
         */
        ONHOLD, 
        /**
         * The activities have been completed (more or less) as planned
         */
        COMPLETED, 
        /**
         * The activities have been ended prior to completion (perhaps even before they were started)
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
            case NOTSTARTED: return "Activity is planned but no action has yet been taken";
            case SCHEDULED: return "Appointment or other booking has occurred but activity has not yet begun";
            case INPROGRESS: return "Activity has been started but is not yet complete";
            case ONHOLD: return "Activity was started but has temporarily ceased with an expectation of resumption at a future time.";
            case COMPLETED: return "The activities have been completed (more or less) as planned";
            case CANCELLED: return "The activities have been ended prior to completion (perhaps even before they were started)";
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

    @Block()
    public static class CarePlanParticipantComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Indicates specific responsibility of an individual within the care plan.  E.g. "Primary physician", "Team coordinator", "Caregiver", etc.
         */
        @Child(name = "role", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Type of involvement", formalDefinition="Indicates specific responsibility of an individual within the care plan.  E.g. \"Primary physician\", \"Team coordinator\", \"Caregiver\", etc." )
        protected CodeableConcept role;

        /**
         * The specific person or organization who is participating/expected to participate in the care plan.
         */
        @Child(name = "member", type = {Practitioner.class, RelatedPerson.class, Patient.class, Organization.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Who is involved", formalDefinition="The specific person or organization who is participating/expected to participate in the care plan." )
        protected Reference member;

        /**
         * The actual object that is the target of the reference (The specific person or organization who is participating/expected to participate in the care plan.)
         */
        protected Resource memberTarget;

        private static final long serialVersionUID = -466811117L;

    /*
     * Constructor
     */
      public CarePlanParticipantComponent() {
        super();
      }

    /*
     * Constructor
     */
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
          childrenList.add(new Property("role", "CodeableConcept", "Indicates specific responsibility of an individual within the care plan.  E.g. \"Primary physician\", \"Team coordinator\", \"Caregiver\", etc.", 0, java.lang.Integer.MAX_VALUE, role));
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
         * Notes about the execution of the activity.
         */
        @Child(name = "notes", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Comments about the activity", formalDefinition="Notes about the execution of the activity." )
        protected StringType notes;

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

        private static final long serialVersionUID = -1011983328L;

    /*
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
          childrenList.add(new Property("notes", "string", "Notes about the execution of the activity.", 0, java.lang.Integer.MAX_VALUE, notes));
          childrenList.add(new Property("reference", "Reference(Appointment|CommunicationRequest|DeviceUseRequest|DiagnosticOrder|MedicationOrder|NutritionOrder|Order|ProcedureRequest|ProcessRequest|ReferralRequest|SupplyRequest|VisionPrescription)", "The details of the proposed activity represented in a specific resource.", 0, java.lang.Integer.MAX_VALUE, reference));
          childrenList.add(new Property("detail", "", "A simple summary of a planned activity suitable for a general care plan system (e.g. form driven) that doesn't know about specific resources such as procedure etc.", 0, java.lang.Integer.MAX_VALUE, detail));
        }

      public CarePlanActivityComponent copy() {
        CarePlanActivityComponent dst = new CarePlanActivityComponent();
        copyValues(dst);
        if (actionResulting != null) {
          dst.actionResulting = new ArrayList<Reference>();
          for (Reference i : actionResulting)
            dst.actionResulting.add(i.copy());
        };
        dst.notes = notes == null ? null : notes.copy();
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
        return compareDeep(actionResulting, o.actionResulting, true) && compareDeep(notes, o.notes, true)
           && compareDeep(reference, o.reference, true) && compareDeep(detail, o.detail, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof CarePlanActivityComponent))
          return false;
        CarePlanActivityComponent o = (CarePlanActivityComponent) other;
        return compareValues(notes, o.notes, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (actionResulting == null || actionResulting.isEmpty()) && (notes == null || notes.isEmpty())
           && (reference == null || reference.isEmpty()) && (detail == null || detail.isEmpty());
      }

  }

    @Block()
    public static class CarePlanActivityDetailComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * High-level categorization of the type of activity in a care plan.
         */
        @Child(name = "category", type = {CodeType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="diet | drug | encounter | observation | procedure | supply | other", formalDefinition="High-level categorization of the type of activity in a care plan." )
        protected Enumeration<CarePlanActivityCategory> category;

        /**
         * Detailed description of the type of planned activity.  E.g. What lab test, what procedure, what kind of encounter.
         */
        @Child(name = "code", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Detail type of activity", formalDefinition="Detailed description of the type of planned activity.  E.g. What lab test, what procedure, what kind of encounter." )
        protected CodeableConcept code;

        /**
         * Provides the health condition(s) or other rationale that drove the inclusion of this particular activity as part of the plan.
         */
        @Child(name = "reason", type = {CodeableConcept.class, Condition.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Why activity should be done", formalDefinition="Provides the health condition(s) or other rationale that drove the inclusion of this particular activity as part of the plan." )
        protected Type reason;

        /**
         * Internal reference that identifies the goals that this activity is intended to contribute towards meeting.
         */
        @Child(name = "goal", type = {Goal.class}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Goals this activity relates to", formalDefinition="Internal reference that identifies the goals that this activity is intended to contribute towards meeting." )
        protected List<Reference> goal;
        /**
         * The actual objects that are the target of the reference (Internal reference that identifies the goals that this activity is intended to contribute towards meeting.)
         */
        protected List<Goal> goalTarget;


        /**
         * Identifies what progress is being made for the specific activity.
         */
        @Child(name = "status", type = {CodeType.class}, order=5, min=0, max=1, modifier=true, summary=false)
        @Description(shortDefinition="not-started | scheduled | in-progress | on-hold | completed | cancelled", formalDefinition="Identifies what progress is being made for the specific activity." )
        protected Enumeration<CarePlanActivityStatus> status;

        /**
         * Provides reason why the activity isn't yet started, is on hold, was cancelled, etc.
         */
        @Child(name = "statusReason", type = {CodeableConcept.class}, order=6, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Reason for current status", formalDefinition="Provides reason why the activity isn't yet started, is on hold, was cancelled, etc." )
        protected CodeableConcept statusReason;

        /**
         * If true, indicates that the described activity is one that must NOT be engaged in when following the plan.
         */
        @Child(name = "prohibited", type = {BooleanType.class}, order=7, min=1, max=1, modifier=true, summary=false)
        @Description(shortDefinition="Do NOT do", formalDefinition="If true, indicates that the described activity is one that must NOT be engaged in when following the plan." )
        protected BooleanType prohibited;

        /**
         * The period, timing or frequency upon which the described activity is to occur.
         */
        @Child(name = "scheduled", type = {Timing.class, Period.class, StringType.class}, order=8, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="When activity is to occur", formalDefinition="The period, timing or frequency upon which the described activity is to occur." )
        protected Type scheduled;

        /**
         * Identifies the facility where the activity will occur.  E.g. home, hospital, specific clinic, etc.
         */
        @Child(name = "location", type = {Location.class}, order=9, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Where it should happen", formalDefinition="Identifies the facility where the activity will occur.  E.g. home, hospital, specific clinic, etc." )
        protected Reference location;

        /**
         * The actual object that is the target of the reference (Identifies the facility where the activity will occur.  E.g. home, hospital, specific clinic, etc.)
         */
        protected Location locationTarget;

        /**
         * Identifies who's expected to be involved in the activity.
         */
        @Child(name = "performer", type = {Practitioner.class, Organization.class, RelatedPerson.class, Patient.class}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Who will be responsible?", formalDefinition="Identifies who's expected to be involved in the activity." )
        protected List<Reference> performer;
        /**
         * The actual objects that are the target of the reference (Identifies who's expected to be involved in the activity.)
         */
        protected List<Resource> performerTarget;


        /**
         * Identifies the food, drug or other product to be consumed or supplied in the activity.
         */
        @Child(name = "product", type = {Medication.class, Substance.class}, order=11, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="What is to be administered/supplied", formalDefinition="Identifies the food, drug or other product to be consumed or supplied in the activity." )
        protected Reference product;

        /**
         * The actual object that is the target of the reference (Identifies the food, drug or other product to be consumed or supplied in the activity.)
         */
        protected Resource productTarget;

        /**
         * Identifies the quantity expected to be consumed in a given day.
         */
        @Child(name = "dailyAmount", type = {SimpleQuantity.class}, order=12, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="How to consume/day?", formalDefinition="Identifies the quantity expected to be consumed in a given day." )
        protected SimpleQuantity dailyAmount;

        /**
         * Identifies the quantity expected to be supplied, addministered or consumed by the subject.
         */
        @Child(name = "quantity", type = {SimpleQuantity.class}, order=13, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="How much to administer/supply/consume", formalDefinition="Identifies the quantity expected to be supplied, addministered or consumed by the subject." )
        protected SimpleQuantity quantity;

        /**
         * This provides a textual description of constraints on the intended activity occurrence, including relation to other activities.  It may also include objectives, pre-conditions and end-conditions.  Finally, it may convey specifics about the activity such as body site, method, route, etc.
         */
        @Child(name = "note", type = {StringType.class}, order=14, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Extra info on activity occurrence", formalDefinition="This provides a textual description of constraints on the intended activity occurrence, including relation to other activities.  It may also include objectives, pre-conditions and end-conditions.  Finally, it may convey specifics about the activity such as body site, method, route, etc." )
        protected StringType note;

        private static final long serialVersionUID = -369716593L;

    /*
     * Constructor
     */
      public CarePlanActivityDetailComponent() {
        super();
      }

    /*
     * Constructor
     */
      public CarePlanActivityDetailComponent(Enumeration<CarePlanActivityCategory> category, BooleanType prohibited) {
        super();
        this.category = category;
        this.prohibited = prohibited;
      }

        /**
         * @return {@link #category} (High-level categorization of the type of activity in a care plan.). This is the underlying object with id, value and extensions. The accessor "getCategory" gives direct access to the value
         */
        public Enumeration<CarePlanActivityCategory> getCategoryElement() { 
          if (this.category == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CarePlanActivityDetailComponent.category");
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
        public CarePlanActivityDetailComponent setCategoryElement(Enumeration<CarePlanActivityCategory> value) { 
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
        public CarePlanActivityDetailComponent setCategory(CarePlanActivityCategory value) { 
            if (this.category == null)
              this.category = new Enumeration<CarePlanActivityCategory>(new CarePlanActivityCategoryEnumFactory());
            this.category.setValue(value);
          return this;
        }

        /**
         * @return {@link #code} (Detailed description of the type of planned activity.  E.g. What lab test, what procedure, what kind of encounter.)
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
         * @param value {@link #code} (Detailed description of the type of planned activity.  E.g. What lab test, what procedure, what kind of encounter.)
         */
        public CarePlanActivityDetailComponent setCode(CodeableConcept value) { 
          this.code = value;
          return this;
        }

        /**
         * @return {@link #reason} (Provides the health condition(s) or other rationale that drove the inclusion of this particular activity as part of the plan.)
         */
        public Type getReason() { 
          return this.reason;
        }

        /**
         * @return {@link #reason} (Provides the health condition(s) or other rationale that drove the inclusion of this particular activity as part of the plan.)
         */
        public CodeableConcept getReasonCodeableConcept() throws Exception { 
          if (!(this.reason instanceof CodeableConcept))
            throw new Exception("Type mismatch: the type CodeableConcept was expected, but "+this.reason.getClass().getName()+" was encountered");
          return (CodeableConcept) this.reason;
        }

        public boolean hasReasonCodeableConcept() throws Exception { 
          return this.reason instanceof CodeableConcept;
        }

        /**
         * @return {@link #reason} (Provides the health condition(s) or other rationale that drove the inclusion of this particular activity as part of the plan.)
         */
        public Reference getReasonReference() throws Exception { 
          if (!(this.reason instanceof Reference))
            throw new Exception("Type mismatch: the type Reference was expected, but "+this.reason.getClass().getName()+" was encountered");
          return (Reference) this.reason;
        }

        public boolean hasReasonReference() throws Exception { 
          return this.reason instanceof Reference;
        }

        public boolean hasReason() { 
          return this.reason != null && !this.reason.isEmpty();
        }

        /**
         * @param value {@link #reason} (Provides the health condition(s) or other rationale that drove the inclusion of this particular activity as part of the plan.)
         */
        public CarePlanActivityDetailComponent setReason(Type value) { 
          this.reason = value;
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
        public Timing getScheduledTiming() throws Exception { 
          if (!(this.scheduled instanceof Timing))
            throw new Exception("Type mismatch: the type Timing was expected, but "+this.scheduled.getClass().getName()+" was encountered");
          return (Timing) this.scheduled;
        }

        public boolean hasScheduledTiming() throws Exception { 
          return this.scheduled instanceof Timing;
        }

        /**
         * @return {@link #scheduled} (The period, timing or frequency upon which the described activity is to occur.)
         */
        public Period getScheduledPeriod() throws Exception { 
          if (!(this.scheduled instanceof Period))
            throw new Exception("Type mismatch: the type Period was expected, but "+this.scheduled.getClass().getName()+" was encountered");
          return (Period) this.scheduled;
        }

        public boolean hasScheduledPeriod() throws Exception { 
          return this.scheduled instanceof Period;
        }

        /**
         * @return {@link #scheduled} (The period, timing or frequency upon which the described activity is to occur.)
         */
        public StringType getScheduledStringType() throws Exception { 
          if (!(this.scheduled instanceof StringType))
            throw new Exception("Type mismatch: the type StringType was expected, but "+this.scheduled.getClass().getName()+" was encountered");
          return (StringType) this.scheduled;
        }

        public boolean hasScheduledStringType() throws Exception { 
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
         * @return {@link #location} (Identifies the facility where the activity will occur.  E.g. home, hospital, specific clinic, etc.)
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
         * @param value {@link #location} (Identifies the facility where the activity will occur.  E.g. home, hospital, specific clinic, etc.)
         */
        public CarePlanActivityDetailComponent setLocation(Reference value) { 
          this.location = value;
          return this;
        }

        /**
         * @return {@link #location} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Identifies the facility where the activity will occur.  E.g. home, hospital, specific clinic, etc.)
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
         * @param value {@link #location} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Identifies the facility where the activity will occur.  E.g. home, hospital, specific clinic, etc.)
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
        public Reference getProduct() { 
          if (this.product == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CarePlanActivityDetailComponent.product");
            else if (Configuration.doAutoCreate())
              this.product = new Reference(); // cc
          return this.product;
        }

        public boolean hasProduct() { 
          return this.product != null && !this.product.isEmpty();
        }

        /**
         * @param value {@link #product} (Identifies the food, drug or other product to be consumed or supplied in the activity.)
         */
        public CarePlanActivityDetailComponent setProduct(Reference value) { 
          this.product = value;
          return this;
        }

        /**
         * @return {@link #product} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Identifies the food, drug or other product to be consumed or supplied in the activity.)
         */
        public Resource getProductTarget() { 
          return this.productTarget;
        }

        /**
         * @param value {@link #product} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Identifies the food, drug or other product to be consumed or supplied in the activity.)
         */
        public CarePlanActivityDetailComponent setProductTarget(Resource value) { 
          this.productTarget = value;
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
         * @return {@link #quantity} (Identifies the quantity expected to be supplied, addministered or consumed by the subject.)
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
         * @param value {@link #quantity} (Identifies the quantity expected to be supplied, addministered or consumed by the subject.)
         */
        public CarePlanActivityDetailComponent setQuantity(SimpleQuantity value) { 
          this.quantity = value;
          return this;
        }

        /**
         * @return {@link #note} (This provides a textual description of constraints on the intended activity occurrence, including relation to other activities.  It may also include objectives, pre-conditions and end-conditions.  Finally, it may convey specifics about the activity such as body site, method, route, etc.). This is the underlying object with id, value and extensions. The accessor "getNote" gives direct access to the value
         */
        public StringType getNoteElement() { 
          if (this.note == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CarePlanActivityDetailComponent.note");
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
         * @param value {@link #note} (This provides a textual description of constraints on the intended activity occurrence, including relation to other activities.  It may also include objectives, pre-conditions and end-conditions.  Finally, it may convey specifics about the activity such as body site, method, route, etc.). This is the underlying object with id, value and extensions. The accessor "getNote" gives direct access to the value
         */
        public CarePlanActivityDetailComponent setNoteElement(StringType value) { 
          this.note = value;
          return this;
        }

        /**
         * @return This provides a textual description of constraints on the intended activity occurrence, including relation to other activities.  It may also include objectives, pre-conditions and end-conditions.  Finally, it may convey specifics about the activity such as body site, method, route, etc.
         */
        public String getNote() { 
          return this.note == null ? null : this.note.getValue();
        }

        /**
         * @param value This provides a textual description of constraints on the intended activity occurrence, including relation to other activities.  It may also include objectives, pre-conditions and end-conditions.  Finally, it may convey specifics about the activity such as body site, method, route, etc.
         */
        public CarePlanActivityDetailComponent setNote(String value) { 
          if (Utilities.noString(value))
            this.note = null;
          else {
            if (this.note == null)
              this.note = new StringType();
            this.note.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("category", "code", "High-level categorization of the type of activity in a care plan.", 0, java.lang.Integer.MAX_VALUE, category));
          childrenList.add(new Property("code", "CodeableConcept", "Detailed description of the type of planned activity.  E.g. What lab test, what procedure, what kind of encounter.", 0, java.lang.Integer.MAX_VALUE, code));
          childrenList.add(new Property("reason[x]", "CodeableConcept|Reference(Condition)", "Provides the health condition(s) or other rationale that drove the inclusion of this particular activity as part of the plan.", 0, java.lang.Integer.MAX_VALUE, reason));
          childrenList.add(new Property("goal", "Reference(Goal)", "Internal reference that identifies the goals that this activity is intended to contribute towards meeting.", 0, java.lang.Integer.MAX_VALUE, goal));
          childrenList.add(new Property("status", "code", "Identifies what progress is being made for the specific activity.", 0, java.lang.Integer.MAX_VALUE, status));
          childrenList.add(new Property("statusReason", "CodeableConcept", "Provides reason why the activity isn't yet started, is on hold, was cancelled, etc.", 0, java.lang.Integer.MAX_VALUE, statusReason));
          childrenList.add(new Property("prohibited", "boolean", "If true, indicates that the described activity is one that must NOT be engaged in when following the plan.", 0, java.lang.Integer.MAX_VALUE, prohibited));
          childrenList.add(new Property("scheduled[x]", "Timing|Period|string", "The period, timing or frequency upon which the described activity is to occur.", 0, java.lang.Integer.MAX_VALUE, scheduled));
          childrenList.add(new Property("location", "Reference(Location)", "Identifies the facility where the activity will occur.  E.g. home, hospital, specific clinic, etc.", 0, java.lang.Integer.MAX_VALUE, location));
          childrenList.add(new Property("performer", "Reference(Practitioner|Organization|RelatedPerson|Patient)", "Identifies who's expected to be involved in the activity.", 0, java.lang.Integer.MAX_VALUE, performer));
          childrenList.add(new Property("product", "Reference(Medication|Substance)", "Identifies the food, drug or other product to be consumed or supplied in the activity.", 0, java.lang.Integer.MAX_VALUE, product));
          childrenList.add(new Property("dailyAmount", "SimpleQuantity", "Identifies the quantity expected to be consumed in a given day.", 0, java.lang.Integer.MAX_VALUE, dailyAmount));
          childrenList.add(new Property("quantity", "SimpleQuantity", "Identifies the quantity expected to be supplied, addministered or consumed by the subject.", 0, java.lang.Integer.MAX_VALUE, quantity));
          childrenList.add(new Property("note", "string", "This provides a textual description of constraints on the intended activity occurrence, including relation to other activities.  It may also include objectives, pre-conditions and end-conditions.  Finally, it may convey specifics about the activity such as body site, method, route, etc.", 0, java.lang.Integer.MAX_VALUE, note));
        }

      public CarePlanActivityDetailComponent copy() {
        CarePlanActivityDetailComponent dst = new CarePlanActivityDetailComponent();
        copyValues(dst);
        dst.category = category == null ? null : category.copy();
        dst.code = code == null ? null : code.copy();
        dst.reason = reason == null ? null : reason.copy();
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
        dst.note = note == null ? null : note.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof CarePlanActivityDetailComponent))
          return false;
        CarePlanActivityDetailComponent o = (CarePlanActivityDetailComponent) other;
        return compareDeep(category, o.category, true) && compareDeep(code, o.code, true) && compareDeep(reason, o.reason, true)
           && compareDeep(goal, o.goal, true) && compareDeep(status, o.status, true) && compareDeep(statusReason, o.statusReason, true)
           && compareDeep(prohibited, o.prohibited, true) && compareDeep(scheduled, o.scheduled, true) && compareDeep(location, o.location, true)
           && compareDeep(performer, o.performer, true) && compareDeep(product, o.product, true) && compareDeep(dailyAmount, o.dailyAmount, true)
           && compareDeep(quantity, o.quantity, true) && compareDeep(note, o.note, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof CarePlanActivityDetailComponent))
          return false;
        CarePlanActivityDetailComponent o = (CarePlanActivityDetailComponent) other;
        return compareValues(category, o.category, true) && compareValues(status, o.status, true) && compareValues(prohibited, o.prohibited, true)
           && compareValues(note, o.note, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (category == null || category.isEmpty()) && (code == null || code.isEmpty())
           && (reason == null || reason.isEmpty()) && (goal == null || goal.isEmpty()) && (status == null || status.isEmpty())
           && (statusReason == null || statusReason.isEmpty()) && (prohibited == null || prohibited.isEmpty())
           && (scheduled == null || scheduled.isEmpty()) && (location == null || location.isEmpty())
           && (performer == null || performer.isEmpty()) && (product == null || product.isEmpty()) && (dailyAmount == null || dailyAmount.isEmpty())
           && (quantity == null || quantity.isEmpty()) && (note == null || note.isEmpty());
      }

  }

    /**
     * This records identifiers associated with this care plan that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation).
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="External Ids for this plan", formalDefinition="This records identifiers associated with this care plan that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)." )
    protected List<Identifier> identifier;

    /**
     * Identifies the patient/subject whose intended care is described by the plan.
     */
    @Child(name = "patient", type = {Patient.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Who care plan is for", formalDefinition="Identifies the patient/subject whose intended care is described by the plan." )
    protected Reference patient;

    /**
     * The actual object that is the target of the reference (Identifies the patient/subject whose intended care is described by the plan.)
     */
    protected Patient patientTarget;

    /**
     * Indicates whether the plan is currently being acted upon, represents future intentions or is now just historical record.
     */
    @Child(name = "status", type = {CodeType.class}, order=2, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="planned | active | completed", formalDefinition="Indicates whether the plan is currently being acted upon, represents future intentions or is now just historical record." )
    protected Enumeration<CarePlanStatus> status;

    /**
     * Indicates when the plan did (or is intended to) come into effect and end.
     */
    @Child(name = "period", type = {Period.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Time period plan covers", formalDefinition="Indicates when the plan did (or is intended to) come into effect and end." )
    protected Period period;

    /**
     * Identifies the individual(s) or ogranization who is responsible for the content of the care plan.
     */
    @Child(name = "author", type = {Patient.class, Practitioner.class, RelatedPerson.class, Organization.class}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Who is responsible for plan", formalDefinition="Identifies the individual(s) or ogranization who is responsible for the content of the care plan." )
    protected List<Reference> author;
    /**
     * The actual objects that are the target of the reference (Identifies the individual(s) or ogranization who is responsible for the content of the care plan.)
     */
    protected List<Resource> authorTarget;


    /**
     * Identifies the most recent date on which the plan has been revised.
     */
    @Child(name = "modified", type = {DateTimeType.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When last updated", formalDefinition="Identifies the most recent date on which the plan has been revised." )
    protected DateTimeType modified;

    /**
     * Identifies what "kind" of plan this is to support differentiation between multiple co-existing plans.  E.g. "Home health", "psychiatric", "asthma", "disease management", etc.
     */
    @Child(name = "category", type = {CodeableConcept.class}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Type of plan", formalDefinition="Identifies what \"kind\" of plan this is to support differentiation between multiple co-existing plans.  E.g. \"Home health\", \"psychiatric\", \"asthma\", \"disease management\", etc." )
    protected List<CodeableConcept> category;

    /**
     * Identifies the conditions/problems/concerns/diagnoses/etc. whose management and/or mitigation are handled by this plan.
     */
    @Child(name = "concern", type = {Condition.class}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Health issues this plan addresses", formalDefinition="Identifies the conditions/problems/concerns/diagnoses/etc. whose management and/or mitigation are handled by this plan." )
    protected List<Reference> concern;
    /**
     * The actual objects that are the target of the reference (Identifies the conditions/problems/concerns/diagnoses/etc. whose management and/or mitigation are handled by this plan.)
     */
    protected List<Condition> concernTarget;


    /**
     * Identifies portions of the patient's record that specifically influenced the formation of the plan.  These might include co-morbidities, recent procedures, limitations, recent assessments, etc.
     */
    @Child(name = "support", type = {}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Information considered as part of plan", formalDefinition="Identifies portions of the patient's record that specifically influenced the formation of the plan.  These might include co-morbidities, recent procedures, limitations, recent assessments, etc." )
    protected List<Reference> support;
    /**
     * The actual objects that are the target of the reference (Identifies portions of the patient's record that specifically influenced the formation of the plan.  These might include co-morbidities, recent procedures, limitations, recent assessments, etc.)
     */
    protected List<Resource> supportTarget;


    /**
     * Identifies all people and organizations who are expected to be involved in the care envisioned by this plan.
     */
    @Child(name = "participant", type = {}, order=9, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Who's involved in plan?", formalDefinition="Identifies all people and organizations who are expected to be involved in the care envisioned by this plan." )
    protected List<CarePlanParticipantComponent> participant;

    /**
     * Describes the intended objective(s) of carrying out the Care Plan.
     */
    @Child(name = "goal", type = {Goal.class}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Desired outcome of plan", formalDefinition="Describes the intended objective(s) of carrying out the Care Plan." )
    protected List<Reference> goal;
    /**
     * The actual objects that are the target of the reference (Describes the intended objective(s) of carrying out the Care Plan.)
     */
    protected List<Goal> goalTarget;


    /**
     * Identifies a planned action to occur as part of the plan.  For example, a medication to be used, lab tests to perform, self-monitoring, education, etc.
     */
    @Child(name = "activity", type = {}, order=11, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Action to occur as part of plan", formalDefinition="Identifies a planned action to occur as part of the plan.  For example, a medication to be used, lab tests to perform, self-monitoring, education, etc." )
    protected List<CarePlanActivityComponent> activity;

    /**
     * General notes about the care plan not covered elsewhere.
     */
    @Child(name = "notes", type = {StringType.class}, order=12, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Comments about the plan", formalDefinition="General notes about the care plan not covered elsewhere." )
    protected StringType notes;

    private static final long serialVersionUID = -1877285959L;

  /*
   * Constructor
   */
    public CarePlan() {
      super();
    }

  /*
   * Constructor
   */
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
     * @return {@link #category} (Identifies what "kind" of plan this is to support differentiation between multiple co-existing plans.  E.g. "Home health", "psychiatric", "asthma", "disease management", etc.)
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
     * @return {@link #category} (Identifies what "kind" of plan this is to support differentiation between multiple co-existing plans.  E.g. "Home health", "psychiatric", "asthma", "disease management", etc.)
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

    // syntactic sugar
    public CarePlan addConcern(Reference t) { //3
      if (t == null)
        return this;
      if (this.concern == null)
        this.concern = new ArrayList<Reference>();
      this.concern.add(t);
      return this;
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
     * @return {@link #goal} (Describes the intended objective(s) of carrying out the Care Plan.)
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
     * @return {@link #goal} (Describes the intended objective(s) of carrying out the Care Plan.)
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
     * @return {@link #goal} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. Describes the intended objective(s) of carrying out the Care Plan.)
     */
    public List<Goal> getGoalTarget() { 
      if (this.goalTarget == null)
        this.goalTarget = new ArrayList<Goal>();
      return this.goalTarget;
    }

    // syntactic sugar
    /**
     * @return {@link #goal} (Add an actual object that is the target of the reference. The reference library doesn't use these, but you can use this to hold the resources if you resolvethemt. Describes the intended objective(s) of carrying out the Care Plan.)
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
        childrenList.add(new Property("author", "Reference(Patient|Practitioner|RelatedPerson|Organization)", "Identifies the individual(s) or ogranization who is responsible for the content of the care plan.", 0, java.lang.Integer.MAX_VALUE, author));
        childrenList.add(new Property("modified", "dateTime", "Identifies the most recent date on which the plan has been revised.", 0, java.lang.Integer.MAX_VALUE, modified));
        childrenList.add(new Property("category", "CodeableConcept", "Identifies what \"kind\" of plan this is to support differentiation between multiple co-existing plans.  E.g. \"Home health\", \"psychiatric\", \"asthma\", \"disease management\", etc.", 0, java.lang.Integer.MAX_VALUE, category));
        childrenList.add(new Property("concern", "Reference(Condition)", "Identifies the conditions/problems/concerns/diagnoses/etc. whose management and/or mitigation are handled by this plan.", 0, java.lang.Integer.MAX_VALUE, concern));
        childrenList.add(new Property("support", "Reference(Any)", "Identifies portions of the patient's record that specifically influenced the formation of the plan.  These might include co-morbidities, recent procedures, limitations, recent assessments, etc.", 0, java.lang.Integer.MAX_VALUE, support));
        childrenList.add(new Property("participant", "", "Identifies all people and organizations who are expected to be involved in the care envisioned by this plan.", 0, java.lang.Integer.MAX_VALUE, participant));
        childrenList.add(new Property("goal", "Reference(Goal)", "Describes the intended objective(s) of carrying out the Care Plan.", 0, java.lang.Integer.MAX_VALUE, goal));
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
        if (concern != null) {
          dst.concern = new ArrayList<Reference>();
          for (Reference i : concern)
            dst.concern.add(i.copy());
        };
        if (support != null) {
          dst.support = new ArrayList<Reference>();
          for (Reference i : support)
            dst.support.add(i.copy());
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
           && compareDeep(period, o.period, true) && compareDeep(author, o.author, true) && compareDeep(modified, o.modified, true)
           && compareDeep(category, o.category, true) && compareDeep(concern, o.concern, true) && compareDeep(support, o.support, true)
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
           && (status == null || status.isEmpty()) && (period == null || period.isEmpty()) && (author == null || author.isEmpty())
           && (modified == null || modified.isEmpty()) && (category == null || category.isEmpty()) && (concern == null || concern.isEmpty())
           && (support == null || support.isEmpty()) && (participant == null || participant.isEmpty())
           && (goal == null || goal.isEmpty()) && (activity == null || activity.isEmpty()) && (notes == null || notes.isEmpty())
          ;
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.CarePlan;
   }

  @SearchParamDefinition(name="date", path="CarePlan.period", description="Time period plan covers", type="date" )
  public static final String SP_DATE = "date";
  @SearchParamDefinition(name="activitycode", path="CarePlan.activity.detail.code", description="Detail type of activity", type="token" )
  public static final String SP_ACTIVITYCODE = "activitycode";
  @SearchParamDefinition(name="activitydate", path="CarePlan.activity.detail.scheduled[x]", description="Specified date occurs within period specified by CarePlan.activity.timingSchedule", type="date" )
  public static final String SP_ACTIVITYDATE = "activitydate";
  @SearchParamDefinition(name="condition", path="CarePlan.concern", description="Health issues this plan addresses", type="reference" )
  public static final String SP_CONDITION = "condition";
  @SearchParamDefinition(name="activityreference", path="CarePlan.activity.reference", description="Activity details defined in specific resource", type="reference" )
  public static final String SP_ACTIVITYREFERENCE = "activityreference";
  @SearchParamDefinition(name="performer", path="CarePlan.activity.detail.performer", description="Matches if the practitioner is listed as a performer in any of the \"simple\" activities.  (For performers of the detailed activities, chain through the activitydetail search parameter.)", type="reference" )
  public static final String SP_PERFORMER = "performer";
  @SearchParamDefinition(name="goal", path="CarePlan.goal", description="Desired outcome of plan", type="reference" )
  public static final String SP_GOAL = "goal";
  @SearchParamDefinition(name="patient", path="CarePlan.patient", description="Who care plan is for", type="reference" )
  public static final String SP_PATIENT = "patient";
  @SearchParamDefinition(name="participant", path="CarePlan.participant.member", description="Who is involved", type="reference" )
  public static final String SP_PARTICIPANT = "participant";

}

