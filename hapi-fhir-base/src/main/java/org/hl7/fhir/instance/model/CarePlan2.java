package org.hl7.fhir.instance.model;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


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
@ResourceDef(name="CarePlan2", profile="http://hl7.org/fhir/Profile/CarePlan2")
public class CarePlan2 extends DomainResource {

    public enum CarePlan2Status implements FhirEnum {
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

      public static final CarePlan2StatusEnumFactory ENUM_FACTORY = new CarePlan2StatusEnumFactory();

        public static CarePlan2Status fromCode(String codeString) throws IllegalArgumentException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("planned".equals(codeString))
          return PLANNED;
        if ("active".equals(codeString))
          return ACTIVE;
        if ("completed".equals(codeString))
          return COMPLETED;
        throw new IllegalArgumentException("Unknown CarePlan2Status code '"+codeString+"'");
        }
        @Override
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

  public static class CarePlan2StatusEnumFactory implements EnumFactory<CarePlan2Status> {
    public CarePlan2Status fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("planned".equals(codeString))
          return CarePlan2Status.PLANNED;
        if ("active".equals(codeString))
          return CarePlan2Status.ACTIVE;
        if ("completed".equals(codeString))
          return CarePlan2Status.COMPLETED;
        throw new IllegalArgumentException("Unknown CarePlan2Status code '"+codeString+"'");
        }
    public String toCode(CarePlan2Status code) throws IllegalArgumentException {
      if (code == CarePlan2Status.PLANNED)
        return "planned";
      if (code == CarePlan2Status.ACTIVE)
        return "active";
      if (code == CarePlan2Status.COMPLETED)
        return "completed";
      return "?";
      }
    }

    @Block()
    public static class CarePlan2ParticipantComponent extends BackboneElement {
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

      public CarePlan2ParticipantComponent() {
        super();
      }

      public CarePlan2ParticipantComponent(Reference member) {
        super();
        this.member = member;
      }

        /**
         * @return {@link #role} (Indicates specific responsibility of an individual within the care plan.  E.g. "Primary physician", "Team coordinator", "Caregiver", etc.)
         */
        public CodeableConcept getRole() { 
          if (this.role == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CarePlan2ParticipantComponent.role");
            else if (Configuration.doAutoCreate())
              this.role = new CodeableConcept();
          return this.role;
        }

        public boolean hasRole() { 
          return this.role != null && !this.role.isEmpty();
        }

        /**
         * @param value {@link #role} (Indicates specific responsibility of an individual within the care plan.  E.g. "Primary physician", "Team coordinator", "Caregiver", etc.)
         */
        public CarePlan2ParticipantComponent setRole(CodeableConcept value) { 
          this.role = value;
          return this;
        }

        /**
         * @return {@link #member} (The specific person or organization who is participating/expected to participate in the care plan.)
         */
        public Reference getMember() { 
          if (this.member == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CarePlan2ParticipantComponent.member");
            else if (Configuration.doAutoCreate())
              this.member = new Reference();
          return this.member;
        }

        public boolean hasMember() { 
          return this.member != null && !this.member.isEmpty();
        }

        /**
         * @param value {@link #member} (The specific person or organization who is participating/expected to participate in the care plan.)
         */
        public CarePlan2ParticipantComponent setMember(Reference value) { 
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
        public CarePlan2ParticipantComponent setMemberTarget(Resource value) { 
          this.memberTarget = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("role", "CodeableConcept", "Indicates specific responsibility of an individual within the care plan.  E.g. 'Primary physician', 'Team coordinator', 'Caregiver', etc.", 0, java.lang.Integer.MAX_VALUE, role));
          childrenList.add(new Property("member", "Reference(Practitioner|RelatedPerson|Patient|Organization)", "The specific person or organization who is participating/expected to participate in the care plan.", 0, java.lang.Integer.MAX_VALUE, member));
        }

      public CarePlan2ParticipantComponent copy() {
        CarePlan2ParticipantComponent dst = new CarePlan2ParticipantComponent();
        copyValues(dst);
        dst.role = role == null ? null : role.copy();
        dst.member = member == null ? null : member.copy();
        return dst;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (role == null || role.isEmpty()) && (member == null || member.isEmpty())
          ;
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
     * Indicates whether the plan is currently being acted upon, represents future intentions or is now just historical record.
     */
    @Child(name="status", type={CodeType.class}, order=1, min=1, max=1)
    @Description(shortDefinition="planned | active | completed", formalDefinition="Indicates whether the plan is currently being acted upon, represents future intentions or is now just historical record." )
    protected Enumeration<CarePlan2Status> status;

    /**
     * Indicates when the plan did (or is intended to) come into effect and end.
     */
    @Child(name="period", type={Period.class}, order=2, min=0, max=1)
    @Description(shortDefinition="Time period plan covers", formalDefinition="Indicates when the plan did (or is intended to) come into effect and end." )
    protected Period period;

    /**
     * Identifies the most recent date on which the plan has been revised.
     */
    @Child(name="modified", type={DateTimeType.class}, order=3, min=0, max=1)
    @Description(shortDefinition="When last updated", formalDefinition="Identifies the most recent date on which the plan has been revised." )
    protected DateTimeType modified;

    /**
     * Identifies the conditions/problems/concerns/diagnoses/etc. whose management and/or mitigation are handled by this plan.
     */
    @Child(name="concern", type={Condition.class}, order=4, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Health issues this plan addresses", formalDefinition="Identifies the conditions/problems/concerns/diagnoses/etc. whose management and/or mitigation are handled by this plan." )
    protected List<Reference> concern;
    /**
     * The actual objects that are the target of the reference (Identifies the conditions/problems/concerns/diagnoses/etc. whose management and/or mitigation are handled by this plan.)
     */
    protected List<Condition> concernTarget;


    /**
     * Identifies all people and organizations who are expected to be involved in the care envisioned by this plan.
     */
    @Child(name="participant", type={}, order=5, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Who's involved in plan?", formalDefinition="Identifies all people and organizations who are expected to be involved in the care envisioned by this plan." )
    protected List<CarePlan2ParticipantComponent> participant;

    /**
     * General notes about the care plan not covered elsewhere.
     */
    @Child(name="notes", type={StringType.class}, order=6, min=0, max=1)
    @Description(shortDefinition="Comments about the plan", formalDefinition="General notes about the care plan not covered elsewhere." )
    protected StringType notes;

    /**
     * Describes the intended objective(s) of carrying out the Care Plan.
     */
    @Child(name="goal", type={Goal.class}, order=7, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="CarePlan Goal", formalDefinition="Describes the intended objective(s) of carrying out the Care Plan." )
    protected List<Reference> goal;
    /**
     * The actual objects that are the target of the reference (Describes the intended objective(s) of carrying out the Care Plan.)
     */
    protected List<Goal> goalTarget;


    /**
     * Identifies a planned action to occur as part of the plan. For example, a medication to be used, lab tests to perform, self-monitoring, education, etc.
     */
    @Child(name="activity", type={CareActivity.class}, order=8, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="CarePlan Activity", formalDefinition="Identifies a planned action to occur as part of the plan. For example, a medication to be used, lab tests to perform, self-monitoring, education, etc." )
    protected List<Reference> activity;
    /**
     * The actual objects that are the target of the reference (Identifies a planned action to occur as part of the plan. For example, a medication to be used, lab tests to perform, self-monitoring, education, etc.)
     */
    protected List<CareActivity> activityTarget;


    private static final long serialVersionUID = -1325874459L;

    public CarePlan2() {
      super();
    }

    public CarePlan2(Enumeration<CarePlan2Status> status) {
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
          throw new Error("Attempt to auto-create CarePlan2.patient");
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
    public CarePlan2 setPatient(Reference value) { 
      this.patient = value;
      return this;
    }

    /**
     * @return {@link #patient} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Identifies the patient/subject whose intended care is described by the plan.)
     */
    public Patient getPatientTarget() { 
      if (this.patientTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CarePlan2.patient");
        else if (Configuration.doAutoCreate())
          this.patientTarget = new Patient();
      return this.patientTarget;
    }

    /**
     * @param value {@link #patient} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Identifies the patient/subject whose intended care is described by the plan.)
     */
    public CarePlan2 setPatientTarget(Patient value) { 
      this.patientTarget = value;
      return this;
    }

    /**
     * @return {@link #status} (Indicates whether the plan is currently being acted upon, represents future intentions or is now just historical record.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<CarePlan2Status> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CarePlan2.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<CarePlan2Status>();
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
    public CarePlan2 setStatusElement(Enumeration<CarePlan2Status> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return Indicates whether the plan is currently being acted upon, represents future intentions or is now just historical record.
     */
    public CarePlan2Status getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value Indicates whether the plan is currently being acted upon, represents future intentions or is now just historical record.
     */
    public CarePlan2 setStatus(CarePlan2Status value) { 
        if (this.status == null)
          this.status = new Enumeration<CarePlan2Status>(CarePlan2Status.ENUM_FACTORY);
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #period} (Indicates when the plan did (or is intended to) come into effect and end.)
     */
    public Period getPeriod() { 
      if (this.period == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CarePlan2.period");
        else if (Configuration.doAutoCreate())
          this.period = new Period();
      return this.period;
    }

    public boolean hasPeriod() { 
      return this.period != null && !this.period.isEmpty();
    }

    /**
     * @param value {@link #period} (Indicates when the plan did (or is intended to) come into effect and end.)
     */
    public CarePlan2 setPeriod(Period value) { 
      this.period = value;
      return this;
    }

    /**
     * @return {@link #modified} (Identifies the most recent date on which the plan has been revised.). This is the underlying object with id, value and extensions. The accessor "getModified" gives direct access to the value
     */
    public DateTimeType getModifiedElement() { 
      if (this.modified == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CarePlan2.modified");
        else if (Configuration.doAutoCreate())
          this.modified = new DateTimeType();
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
    public CarePlan2 setModifiedElement(DateTimeType value) { 
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
    public CarePlan2 setModified(Date value) { 
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
    public List<CarePlan2ParticipantComponent> getParticipant() { 
      if (this.participant == null)
        this.participant = new ArrayList<CarePlan2ParticipantComponent>();
      return this.participant;
    }

    public boolean hasParticipant() { 
      if (this.participant == null)
        return false;
      for (CarePlan2ParticipantComponent item : this.participant)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #participant} (Identifies all people and organizations who are expected to be involved in the care envisioned by this plan.)
     */
    // syntactic sugar
    public CarePlan2ParticipantComponent addParticipant() { //3
      CarePlan2ParticipantComponent t = new CarePlan2ParticipantComponent();
      if (this.participant == null)
        this.participant = new ArrayList<CarePlan2ParticipantComponent>();
      this.participant.add(t);
      return t;
    }

    /**
     * @return {@link #notes} (General notes about the care plan not covered elsewhere.). This is the underlying object with id, value and extensions. The accessor "getNotes" gives direct access to the value
     */
    public StringType getNotesElement() { 
      if (this.notes == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CarePlan2.notes");
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
     * @param value {@link #notes} (General notes about the care plan not covered elsewhere.). This is the underlying object with id, value and extensions. The accessor "getNotes" gives direct access to the value
     */
    public CarePlan2 setNotesElement(StringType value) { 
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
    public CarePlan2 setNotes(String value) { 
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
     * @return {@link #activity} (Identifies a planned action to occur as part of the plan. For example, a medication to be used, lab tests to perform, self-monitoring, education, etc.)
     */
    public List<Reference> getActivity() { 
      if (this.activity == null)
        this.activity = new ArrayList<Reference>();
      return this.activity;
    }

    public boolean hasActivity() { 
      if (this.activity == null)
        return false;
      for (Reference item : this.activity)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #activity} (Identifies a planned action to occur as part of the plan. For example, a medication to be used, lab tests to perform, self-monitoring, education, etc.)
     */
    // syntactic sugar
    public Reference addActivity() { //3
      Reference t = new Reference();
      if (this.activity == null)
        this.activity = new ArrayList<Reference>();
      this.activity.add(t);
      return t;
    }

    /**
     * @return {@link #activity} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. Identifies a planned action to occur as part of the plan. For example, a medication to be used, lab tests to perform, self-monitoring, education, etc.)
     */
    public List<CareActivity> getActivityTarget() { 
      if (this.activityTarget == null)
        this.activityTarget = new ArrayList<CareActivity>();
      return this.activityTarget;
    }

    // syntactic sugar
    /**
     * @return {@link #activity} (Add an actual object that is the target of the reference. The reference library doesn't use these, but you can use this to hold the resources if you resolvethemt. Identifies a planned action to occur as part of the plan. For example, a medication to be used, lab tests to perform, self-monitoring, education, etc.)
     */
    public CareActivity addActivityTarget() { 
      CareActivity r = new CareActivity();
      if (this.activityTarget == null)
        this.activityTarget = new ArrayList<CareActivity>();
      this.activityTarget.add(r);
      return r;
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
        childrenList.add(new Property("notes", "string", "General notes about the care plan not covered elsewhere.", 0, java.lang.Integer.MAX_VALUE, notes));
        childrenList.add(new Property("goal", "Reference(Goal)", "Describes the intended objective(s) of carrying out the Care Plan.", 0, java.lang.Integer.MAX_VALUE, goal));
        childrenList.add(new Property("activity", "Reference(CareActivity)", "Identifies a planned action to occur as part of the plan. For example, a medication to be used, lab tests to perform, self-monitoring, education, etc.", 0, java.lang.Integer.MAX_VALUE, activity));
      }

      public CarePlan2 copy() {
        CarePlan2 dst = new CarePlan2();
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
          dst.participant = new ArrayList<CarePlan2ParticipantComponent>();
          for (CarePlan2ParticipantComponent i : participant)
            dst.participant.add(i.copy());
        };
        dst.notes = notes == null ? null : notes.copy();
        if (goal != null) {
          dst.goal = new ArrayList<Reference>();
          for (Reference i : goal)
            dst.goal.add(i.copy());
        };
        if (activity != null) {
          dst.activity = new ArrayList<Reference>();
          for (Reference i : activity)
            dst.activity.add(i.copy());
        };
        return dst;
      }

      protected CarePlan2 typedCopy() {
        return copy();
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (patient == null || patient.isEmpty())
           && (status == null || status.isEmpty()) && (period == null || period.isEmpty()) && (modified == null || modified.isEmpty())
           && (concern == null || concern.isEmpty()) && (participant == null || participant.isEmpty())
           && (notes == null || notes.isEmpty()) && (goal == null || goal.isEmpty()) && (activity == null || activity.isEmpty())
          ;
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.CarePlan2;
   }

  @SearchParamDefinition(name="patient", path="CarePlan2.patient", description="Who care plan is for", type="reference" )
  public static final String SP_PATIENT = "patient";
  @SearchParamDefinition(name="condition", path="CarePlan2.concern", description="Health issues this plan addresses", type="reference" )
  public static final String SP_CONDITION = "condition";
  @SearchParamDefinition(name="participant", path="CarePlan2.participant.member", description="Who is involved", type="reference" )
  public static final String SP_PARTICIPANT = "participant";
  @SearchParamDefinition(name="date", path="CarePlan2.period", description="Time period plan covers", type="date" )
  public static final String SP_DATE = "date";

}

