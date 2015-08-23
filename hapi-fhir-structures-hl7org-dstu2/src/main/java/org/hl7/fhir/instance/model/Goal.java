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
 * Describes the intended objective(s) of patient care, for example, weight loss, restoring an activity of daily living, etc.
 */
@ResourceDef(name="Goal", profile="http://hl7.org/fhir/Profile/Goal")
public class Goal extends DomainResource {

    public enum GoalStatus {
        /**
         * A goal is proposed for this patient
         */
        PROPOSED, 
        /**
         * A goal is planned for this patient
         */
        PLANNED, 
        /**
         * The goal is being sought but has not yet been reached.  (Also applies if goal was reached in the past but there has been regression and goal is being sought again)
         */
        INPROGRESS, 
        /**
         * The goal has been met and no further action is needed
         */
        ACHIEVED, 
        /**
         * The goal has been met, but ongoing activity is needed to sustain the goal objective
         */
        SUSTAINING, 
        /**
         * The goal is no longer being sought
         */
        CANCELLED, 
        /**
         * A proposed goal was accepted
         */
        ACCEPTED, 
        /**
         * A proposed goal was rejected
         */
        REJECTED, 
        /**
         * added to help the parsers
         */
        NULL;
        public static GoalStatus fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("proposed".equals(codeString))
          return PROPOSED;
        if ("planned".equals(codeString))
          return PLANNED;
        if ("in-progress".equals(codeString))
          return INPROGRESS;
        if ("achieved".equals(codeString))
          return ACHIEVED;
        if ("sustaining".equals(codeString))
          return SUSTAINING;
        if ("cancelled".equals(codeString))
          return CANCELLED;
        if ("accepted".equals(codeString))
          return ACCEPTED;
        if ("rejected".equals(codeString))
          return REJECTED;
        throw new Exception("Unknown GoalStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PROPOSED: return "proposed";
            case PLANNED: return "planned";
            case INPROGRESS: return "in-progress";
            case ACHIEVED: return "achieved";
            case SUSTAINING: return "sustaining";
            case CANCELLED: return "cancelled";
            case ACCEPTED: return "accepted";
            case REJECTED: return "rejected";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case PROPOSED: return "http://hl7.org/fhir/goal-status";
            case PLANNED: return "http://hl7.org/fhir/goal-status";
            case INPROGRESS: return "http://hl7.org/fhir/goal-status";
            case ACHIEVED: return "http://hl7.org/fhir/goal-status";
            case SUSTAINING: return "http://hl7.org/fhir/goal-status";
            case CANCELLED: return "http://hl7.org/fhir/goal-status";
            case ACCEPTED: return "http://hl7.org/fhir/goal-status";
            case REJECTED: return "http://hl7.org/fhir/goal-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case PROPOSED: return "A goal is proposed for this patient";
            case PLANNED: return "A goal is planned for this patient";
            case INPROGRESS: return "The goal is being sought but has not yet been reached.  (Also applies if goal was reached in the past but there has been regression and goal is being sought again)";
            case ACHIEVED: return "The goal has been met and no further action is needed";
            case SUSTAINING: return "The goal has been met, but ongoing activity is needed to sustain the goal objective";
            case CANCELLED: return "The goal is no longer being sought";
            case ACCEPTED: return "A proposed goal was accepted";
            case REJECTED: return "A proposed goal was rejected";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PROPOSED: return "Proposed";
            case PLANNED: return "Planned";
            case INPROGRESS: return "In Progress";
            case ACHIEVED: return "Achieved";
            case SUSTAINING: return "Sustaining";
            case CANCELLED: return "Cancelled";
            case ACCEPTED: return "Accepted";
            case REJECTED: return "Rejected";
            default: return "?";
          }
        }
    }

  public static class GoalStatusEnumFactory implements EnumFactory<GoalStatus> {
    public GoalStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("proposed".equals(codeString))
          return GoalStatus.PROPOSED;
        if ("planned".equals(codeString))
          return GoalStatus.PLANNED;
        if ("in-progress".equals(codeString))
          return GoalStatus.INPROGRESS;
        if ("achieved".equals(codeString))
          return GoalStatus.ACHIEVED;
        if ("sustaining".equals(codeString))
          return GoalStatus.SUSTAINING;
        if ("cancelled".equals(codeString))
          return GoalStatus.CANCELLED;
        if ("accepted".equals(codeString))
          return GoalStatus.ACCEPTED;
        if ("rejected".equals(codeString))
          return GoalStatus.REJECTED;
        throw new IllegalArgumentException("Unknown GoalStatus code '"+codeString+"'");
        }
    public String toCode(GoalStatus code) {
      if (code == GoalStatus.PROPOSED)
        return "proposed";
      if (code == GoalStatus.PLANNED)
        return "planned";
      if (code == GoalStatus.INPROGRESS)
        return "in-progress";
      if (code == GoalStatus.ACHIEVED)
        return "achieved";
      if (code == GoalStatus.SUSTAINING)
        return "sustaining";
      if (code == GoalStatus.CANCELLED)
        return "cancelled";
      if (code == GoalStatus.ACCEPTED)
        return "accepted";
      if (code == GoalStatus.REJECTED)
        return "rejected";
      return "?";
      }
    }

    @Block()
    public static class GoalOutcomeComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Details of what's changed (or not changed).
         */
        @Child(name = "result", type = {CodeableConcept.class, Observation.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Code or observation that resulted from gual", formalDefinition="Details of what's changed (or not changed)." )
        protected Type result;

        private static final long serialVersionUID = 1994317639L;

    /*
     * Constructor
     */
      public GoalOutcomeComponent() {
        super();
      }

        /**
         * @return {@link #result} (Details of what's changed (or not changed).)
         */
        public Type getResult() { 
          return this.result;
        }

        /**
         * @return {@link #result} (Details of what's changed (or not changed).)
         */
        public CodeableConcept getResultCodeableConcept() throws Exception { 
          if (!(this.result instanceof CodeableConcept))
            throw new Exception("Type mismatch: the type CodeableConcept was expected, but "+this.result.getClass().getName()+" was encountered");
          return (CodeableConcept) this.result;
        }

        public boolean hasResultCodeableConcept() throws Exception { 
          return this.result instanceof CodeableConcept;
        }

        /**
         * @return {@link #result} (Details of what's changed (or not changed).)
         */
        public Reference getResultReference() throws Exception { 
          if (!(this.result instanceof Reference))
            throw new Exception("Type mismatch: the type Reference was expected, but "+this.result.getClass().getName()+" was encountered");
          return (Reference) this.result;
        }

        public boolean hasResultReference() throws Exception { 
          return this.result instanceof Reference;
        }

        public boolean hasResult() { 
          return this.result != null && !this.result.isEmpty();
        }

        /**
         * @param value {@link #result} (Details of what's changed (or not changed).)
         */
        public GoalOutcomeComponent setResult(Type value) { 
          this.result = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("result[x]", "CodeableConcept|Reference(Observation)", "Details of what's changed (or not changed).", 0, java.lang.Integer.MAX_VALUE, result));
        }

      public GoalOutcomeComponent copy() {
        GoalOutcomeComponent dst = new GoalOutcomeComponent();
        copyValues(dst);
        dst.result = result == null ? null : result.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof GoalOutcomeComponent))
          return false;
        GoalOutcomeComponent o = (GoalOutcomeComponent) other;
        return compareDeep(result, o.result, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof GoalOutcomeComponent))
          return false;
        GoalOutcomeComponent o = (GoalOutcomeComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (result == null || result.isEmpty());
      }

  }

    /**
     * This records identifiers associated with this care plan that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation).
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="External Ids for this goal", formalDefinition="This records identifiers associated with this care plan that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)." )
    protected List<Identifier> identifier;

    /**
     * Identifies the patient/subject whose intended care is described by the plan.
     */
    @Child(name = "patient", type = {Patient.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The patient for whom this goal is intended for", formalDefinition="Identifies the patient/subject whose intended care is described by the plan." )
    protected Reference patient;

    /**
     * The actual object that is the target of the reference (Identifies the patient/subject whose intended care is described by the plan.)
     */
    protected Patient patientTarget;

    /**
     * Indicates when the goal is intended to be reached.
     */
    @Child(name = "targetDate", type = {DateType.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Reach goal on or before", formalDefinition="Indicates when the goal is intended to be reached." )
    protected DateType targetDate;

    /**
     * Human-readable description of a specific desired objective of care.
     */
    @Child(name = "description", type = {StringType.class}, order=3, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="What's the desired outcome?", formalDefinition="Human-readable description of a specific desired objective of care." )
    protected StringType description;

    /**
     * Indicates whether the goal has been reached and is still considered relevant.
     */
    @Child(name = "status", type = {CodeType.class}, order=4, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="proposed | planned | in-progress | achieved | sustaining | cancelled | accepted | rejected", formalDefinition="Indicates whether the goal has been reached and is still considered relevant." )
    protected Enumeration<GoalStatus> status;

    /**
     * Identifies when the current status.  I.e. When initially created, when achieved, when cancelled, etc.
     */
    @Child(name = "statusDate", type = {DateType.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When goal status took effect", formalDefinition="Identifies when the current status.  I.e. When initially created, when achieved, when cancelled, etc." )
    protected DateType statusDate;

    /**
     * Indicates whose goal this is - patient goal, practitioner goal, etc.
     */
    @Child(name = "author", type = {Patient.class, Practitioner.class, RelatedPerson.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Who's responsible for creating Goal?", formalDefinition="Indicates whose goal this is - patient goal, practitioner goal, etc." )
    protected Reference author;

    /**
     * The actual object that is the target of the reference (Indicates whose goal this is - patient goal, practitioner goal, etc.)
     */
    protected Resource authorTarget;

    /**
     * Identifies the level of importance associated with reaching/sustaining the goal.
     */
    @Child(name = "priority", type = {CodeableConcept.class}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="high | medium |low", formalDefinition="Identifies the level of importance associated with reaching/sustaining the goal." )
    protected CodeableConcept priority;

    /**
     * The identified conditions and other health record elements that are intended to be addressed by the goal.
     */
    @Child(name = "concern", type = {Condition.class, Observation.class, MedicationStatement.class, NutritionOrder.class, ProcedureRequest.class, RiskAssessment.class}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Health issues this goal addresses", formalDefinition="The identified conditions and other health record elements that are intended to be addressed by the goal." )
    protected List<Reference> concern;
    /**
     * The actual objects that are the target of the reference (The identified conditions and other health record elements that are intended to be addressed by the goal.)
     */
    protected List<Resource> concernTarget;


    /**
     * Any comments related to the goal.
     */
    @Child(name = "notes", type = {StringType.class}, order=9, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Comments about the goal", formalDefinition="Any comments related to the goal." )
    protected StringType notes;

    /**
     * Identifies the change (or lack of change) at the point where the goal was deepmed to be cancelled or achieved.
     */
    @Child(name = "outcome", type = {}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="What was end result of goal?", formalDefinition="Identifies the change (or lack of change) at the point where the goal was deepmed to be cancelled or achieved." )
    protected List<GoalOutcomeComponent> outcome;

    private static final long serialVersionUID = -314822558L;

  /*
   * Constructor
   */
    public Goal() {
      super();
    }

  /*
   * Constructor
   */
    public Goal(StringType description, Enumeration<GoalStatus> status) {
      super();
      this.description = description;
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
    public Goal addIdentifier(Identifier t) { //3
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
          throw new Error("Attempt to auto-create Goal.patient");
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
    public Goal setPatient(Reference value) { 
      this.patient = value;
      return this;
    }

    /**
     * @return {@link #patient} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Identifies the patient/subject whose intended care is described by the plan.)
     */
    public Patient getPatientTarget() { 
      if (this.patientTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Goal.patient");
        else if (Configuration.doAutoCreate())
          this.patientTarget = new Patient(); // aa
      return this.patientTarget;
    }

    /**
     * @param value {@link #patient} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Identifies the patient/subject whose intended care is described by the plan.)
     */
    public Goal setPatientTarget(Patient value) { 
      this.patientTarget = value;
      return this;
    }

    /**
     * @return {@link #targetDate} (Indicates when the goal is intended to be reached.). This is the underlying object with id, value and extensions. The accessor "getTargetDate" gives direct access to the value
     */
    public DateType getTargetDateElement() { 
      if (this.targetDate == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Goal.targetDate");
        else if (Configuration.doAutoCreate())
          this.targetDate = new DateType(); // bb
      return this.targetDate;
    }

    public boolean hasTargetDateElement() { 
      return this.targetDate != null && !this.targetDate.isEmpty();
    }

    public boolean hasTargetDate() { 
      return this.targetDate != null && !this.targetDate.isEmpty();
    }

    /**
     * @param value {@link #targetDate} (Indicates when the goal is intended to be reached.). This is the underlying object with id, value and extensions. The accessor "getTargetDate" gives direct access to the value
     */
    public Goal setTargetDateElement(DateType value) { 
      this.targetDate = value;
      return this;
    }

    /**
     * @return Indicates when the goal is intended to be reached.
     */
    public Date getTargetDate() { 
      return this.targetDate == null ? null : this.targetDate.getValue();
    }

    /**
     * @param value Indicates when the goal is intended to be reached.
     */
    public Goal setTargetDate(Date value) { 
      if (value == null)
        this.targetDate = null;
      else {
        if (this.targetDate == null)
          this.targetDate = new DateType();
        this.targetDate.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #description} (Human-readable description of a specific desired objective of care.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public StringType getDescriptionElement() { 
      if (this.description == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Goal.description");
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
     * @param value {@link #description} (Human-readable description of a specific desired objective of care.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public Goal setDescriptionElement(StringType value) { 
      this.description = value;
      return this;
    }

    /**
     * @return Human-readable description of a specific desired objective of care.
     */
    public String getDescription() { 
      return this.description == null ? null : this.description.getValue();
    }

    /**
     * @param value Human-readable description of a specific desired objective of care.
     */
    public Goal setDescription(String value) { 
        if (this.description == null)
          this.description = new StringType();
        this.description.setValue(value);
      return this;
    }

    /**
     * @return {@link #status} (Indicates whether the goal has been reached and is still considered relevant.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<GoalStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Goal.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<GoalStatus>(new GoalStatusEnumFactory()); // bb
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
    public Goal setStatusElement(Enumeration<GoalStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return Indicates whether the goal has been reached and is still considered relevant.
     */
    public GoalStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value Indicates whether the goal has been reached and is still considered relevant.
     */
    public Goal setStatus(GoalStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<GoalStatus>(new GoalStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #statusDate} (Identifies when the current status.  I.e. When initially created, when achieved, when cancelled, etc.). This is the underlying object with id, value and extensions. The accessor "getStatusDate" gives direct access to the value
     */
    public DateType getStatusDateElement() { 
      if (this.statusDate == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Goal.statusDate");
        else if (Configuration.doAutoCreate())
          this.statusDate = new DateType(); // bb
      return this.statusDate;
    }

    public boolean hasStatusDateElement() { 
      return this.statusDate != null && !this.statusDate.isEmpty();
    }

    public boolean hasStatusDate() { 
      return this.statusDate != null && !this.statusDate.isEmpty();
    }

    /**
     * @param value {@link #statusDate} (Identifies when the current status.  I.e. When initially created, when achieved, when cancelled, etc.). This is the underlying object with id, value and extensions. The accessor "getStatusDate" gives direct access to the value
     */
    public Goal setStatusDateElement(DateType value) { 
      this.statusDate = value;
      return this;
    }

    /**
     * @return Identifies when the current status.  I.e. When initially created, when achieved, when cancelled, etc.
     */
    public Date getStatusDate() { 
      return this.statusDate == null ? null : this.statusDate.getValue();
    }

    /**
     * @param value Identifies when the current status.  I.e. When initially created, when achieved, when cancelled, etc.
     */
    public Goal setStatusDate(Date value) { 
      if (value == null)
        this.statusDate = null;
      else {
        if (this.statusDate == null)
          this.statusDate = new DateType();
        this.statusDate.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #author} (Indicates whose goal this is - patient goal, practitioner goal, etc.)
     */
    public Reference getAuthor() { 
      if (this.author == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Goal.author");
        else if (Configuration.doAutoCreate())
          this.author = new Reference(); // cc
      return this.author;
    }

    public boolean hasAuthor() { 
      return this.author != null && !this.author.isEmpty();
    }

    /**
     * @param value {@link #author} (Indicates whose goal this is - patient goal, practitioner goal, etc.)
     */
    public Goal setAuthor(Reference value) { 
      this.author = value;
      return this;
    }

    /**
     * @return {@link #author} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Indicates whose goal this is - patient goal, practitioner goal, etc.)
     */
    public Resource getAuthorTarget() { 
      return this.authorTarget;
    }

    /**
     * @param value {@link #author} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Indicates whose goal this is - patient goal, practitioner goal, etc.)
     */
    public Goal setAuthorTarget(Resource value) { 
      this.authorTarget = value;
      return this;
    }

    /**
     * @return {@link #priority} (Identifies the level of importance associated with reaching/sustaining the goal.)
     */
    public CodeableConcept getPriority() { 
      if (this.priority == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Goal.priority");
        else if (Configuration.doAutoCreate())
          this.priority = new CodeableConcept(); // cc
      return this.priority;
    }

    public boolean hasPriority() { 
      return this.priority != null && !this.priority.isEmpty();
    }

    /**
     * @param value {@link #priority} (Identifies the level of importance associated with reaching/sustaining the goal.)
     */
    public Goal setPriority(CodeableConcept value) { 
      this.priority = value;
      return this;
    }

    /**
     * @return {@link #concern} (The identified conditions and other health record elements that are intended to be addressed by the goal.)
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
     * @return {@link #concern} (The identified conditions and other health record elements that are intended to be addressed by the goal.)
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
    public Goal addConcern(Reference t) { //3
      if (t == null)
        return this;
      if (this.concern == null)
        this.concern = new ArrayList<Reference>();
      this.concern.add(t);
      return this;
    }

    /**
     * @return {@link #concern} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. The identified conditions and other health record elements that are intended to be addressed by the goal.)
     */
    public List<Resource> getConcernTarget() { 
      if (this.concernTarget == null)
        this.concernTarget = new ArrayList<Resource>();
      return this.concernTarget;
    }

    /**
     * @return {@link #notes} (Any comments related to the goal.). This is the underlying object with id, value and extensions. The accessor "getNotes" gives direct access to the value
     */
    public StringType getNotesElement() { 
      if (this.notes == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Goal.notes");
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
    public Goal setNotesElement(StringType value) { 
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
    public Goal setNotes(String value) { 
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
     * @return {@link #outcome} (Identifies the change (or lack of change) at the point where the goal was deepmed to be cancelled or achieved.)
     */
    public List<GoalOutcomeComponent> getOutcome() { 
      if (this.outcome == null)
        this.outcome = new ArrayList<GoalOutcomeComponent>();
      return this.outcome;
    }

    public boolean hasOutcome() { 
      if (this.outcome == null)
        return false;
      for (GoalOutcomeComponent item : this.outcome)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #outcome} (Identifies the change (or lack of change) at the point where the goal was deepmed to be cancelled or achieved.)
     */
    // syntactic sugar
    public GoalOutcomeComponent addOutcome() { //3
      GoalOutcomeComponent t = new GoalOutcomeComponent();
      if (this.outcome == null)
        this.outcome = new ArrayList<GoalOutcomeComponent>();
      this.outcome.add(t);
      return t;
    }

    // syntactic sugar
    public Goal addOutcome(GoalOutcomeComponent t) { //3
      if (t == null)
        return this;
      if (this.outcome == null)
        this.outcome = new ArrayList<GoalOutcomeComponent>();
      this.outcome.add(t);
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "This records identifiers associated with this care plan that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation).", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("patient", "Reference(Patient)", "Identifies the patient/subject whose intended care is described by the plan.", 0, java.lang.Integer.MAX_VALUE, patient));
        childrenList.add(new Property("targetDate", "date", "Indicates when the goal is intended to be reached.", 0, java.lang.Integer.MAX_VALUE, targetDate));
        childrenList.add(new Property("description", "string", "Human-readable description of a specific desired objective of care.", 0, java.lang.Integer.MAX_VALUE, description));
        childrenList.add(new Property("status", "code", "Indicates whether the goal has been reached and is still considered relevant.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("statusDate", "date", "Identifies when the current status.  I.e. When initially created, when achieved, when cancelled, etc.", 0, java.lang.Integer.MAX_VALUE, statusDate));
        childrenList.add(new Property("author", "Reference(Patient|Practitioner|RelatedPerson)", "Indicates whose goal this is - patient goal, practitioner goal, etc.", 0, java.lang.Integer.MAX_VALUE, author));
        childrenList.add(new Property("priority", "CodeableConcept", "Identifies the level of importance associated with reaching/sustaining the goal.", 0, java.lang.Integer.MAX_VALUE, priority));
        childrenList.add(new Property("concern", "Reference(Condition|Observation|MedicationStatement|NutritionOrder|ProcedureRequest|RiskAssessment)", "The identified conditions and other health record elements that are intended to be addressed by the goal.", 0, java.lang.Integer.MAX_VALUE, concern));
        childrenList.add(new Property("notes", "string", "Any comments related to the goal.", 0, java.lang.Integer.MAX_VALUE, notes));
        childrenList.add(new Property("outcome", "", "Identifies the change (or lack of change) at the point where the goal was deepmed to be cancelled or achieved.", 0, java.lang.Integer.MAX_VALUE, outcome));
      }

      public Goal copy() {
        Goal dst = new Goal();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.patient = patient == null ? null : patient.copy();
        dst.targetDate = targetDate == null ? null : targetDate.copy();
        dst.description = description == null ? null : description.copy();
        dst.status = status == null ? null : status.copy();
        dst.statusDate = statusDate == null ? null : statusDate.copy();
        dst.author = author == null ? null : author.copy();
        dst.priority = priority == null ? null : priority.copy();
        if (concern != null) {
          dst.concern = new ArrayList<Reference>();
          for (Reference i : concern)
            dst.concern.add(i.copy());
        };
        dst.notes = notes == null ? null : notes.copy();
        if (outcome != null) {
          dst.outcome = new ArrayList<GoalOutcomeComponent>();
          for (GoalOutcomeComponent i : outcome)
            dst.outcome.add(i.copy());
        };
        return dst;
      }

      protected Goal typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof Goal))
          return false;
        Goal o = (Goal) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(patient, o.patient, true) && compareDeep(targetDate, o.targetDate, true)
           && compareDeep(description, o.description, true) && compareDeep(status, o.status, true) && compareDeep(statusDate, o.statusDate, true)
           && compareDeep(author, o.author, true) && compareDeep(priority, o.priority, true) && compareDeep(concern, o.concern, true)
           && compareDeep(notes, o.notes, true) && compareDeep(outcome, o.outcome, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Goal))
          return false;
        Goal o = (Goal) other;
        return compareValues(targetDate, o.targetDate, true) && compareValues(description, o.description, true)
           && compareValues(status, o.status, true) && compareValues(statusDate, o.statusDate, true) && compareValues(notes, o.notes, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (patient == null || patient.isEmpty())
           && (targetDate == null || targetDate.isEmpty()) && (description == null || description.isEmpty())
           && (status == null || status.isEmpty()) && (statusDate == null || statusDate.isEmpty()) && (author == null || author.isEmpty())
           && (priority == null || priority.isEmpty()) && (concern == null || concern.isEmpty()) && (notes == null || notes.isEmpty())
           && (outcome == null || outcome.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Goal;
   }

  @SearchParamDefinition(name="patient", path="Goal.patient", description="The patient for whom this goal is intended for", type="reference" )
  public static final String SP_PATIENT = "patient";

}

