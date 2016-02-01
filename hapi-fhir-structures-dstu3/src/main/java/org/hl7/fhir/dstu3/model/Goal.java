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
 * Describes the intended objective(s) for a patient, group or organization care, for example, weight loss, restoring an activity of daily living, obtaining herd immunity via immunization, meeting a process improvement objective, etc.
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
         * A proposed goal was accepted
         */
        ACCEPTED, 
        /**
         * A proposed goal was rejected
         */
        REJECTED, 
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
         * The goal remains a long term objective but is no longer being actively pursued for a temporary period of time.
         */
        ONHOLD, 
        /**
         * The goal is no longer being sought
         */
        CANCELLED, 
        /**
         * added to help the parsers
         */
        NULL;
        public static GoalStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("proposed".equals(codeString))
          return PROPOSED;
        if ("planned".equals(codeString))
          return PLANNED;
        if ("accepted".equals(codeString))
          return ACCEPTED;
        if ("rejected".equals(codeString))
          return REJECTED;
        if ("in-progress".equals(codeString))
          return INPROGRESS;
        if ("achieved".equals(codeString))
          return ACHIEVED;
        if ("sustaining".equals(codeString))
          return SUSTAINING;
        if ("on-hold".equals(codeString))
          return ONHOLD;
        if ("cancelled".equals(codeString))
          return CANCELLED;
        throw new FHIRException("Unknown GoalStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PROPOSED: return "proposed";
            case PLANNED: return "planned";
            case ACCEPTED: return "accepted";
            case REJECTED: return "rejected";
            case INPROGRESS: return "in-progress";
            case ACHIEVED: return "achieved";
            case SUSTAINING: return "sustaining";
            case ONHOLD: return "on-hold";
            case CANCELLED: return "cancelled";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case PROPOSED: return "http://hl7.org/fhir/goal-status";
            case PLANNED: return "http://hl7.org/fhir/goal-status";
            case ACCEPTED: return "http://hl7.org/fhir/goal-status";
            case REJECTED: return "http://hl7.org/fhir/goal-status";
            case INPROGRESS: return "http://hl7.org/fhir/goal-status";
            case ACHIEVED: return "http://hl7.org/fhir/goal-status";
            case SUSTAINING: return "http://hl7.org/fhir/goal-status";
            case ONHOLD: return "http://hl7.org/fhir/goal-status";
            case CANCELLED: return "http://hl7.org/fhir/goal-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case PROPOSED: return "A goal is proposed for this patient";
            case PLANNED: return "A goal is planned for this patient";
            case ACCEPTED: return "A proposed goal was accepted";
            case REJECTED: return "A proposed goal was rejected";
            case INPROGRESS: return "The goal is being sought but has not yet been reached.  (Also applies if goal was reached in the past but there has been regression and goal is being sought again)";
            case ACHIEVED: return "The goal has been met and no further action is needed";
            case SUSTAINING: return "The goal has been met, but ongoing activity is needed to sustain the goal objective";
            case ONHOLD: return "The goal remains a long term objective but is no longer being actively pursued for a temporary period of time.";
            case CANCELLED: return "The goal is no longer being sought";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PROPOSED: return "Proposed";
            case PLANNED: return "Planned";
            case ACCEPTED: return "Accepted";
            case REJECTED: return "Rejected";
            case INPROGRESS: return "In Progress";
            case ACHIEVED: return "Achieved";
            case SUSTAINING: return "Sustaining";
            case ONHOLD: return "On Hold";
            case CANCELLED: return "Cancelled";
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
        if ("accepted".equals(codeString))
          return GoalStatus.ACCEPTED;
        if ("rejected".equals(codeString))
          return GoalStatus.REJECTED;
        if ("in-progress".equals(codeString))
          return GoalStatus.INPROGRESS;
        if ("achieved".equals(codeString))
          return GoalStatus.ACHIEVED;
        if ("sustaining".equals(codeString))
          return GoalStatus.SUSTAINING;
        if ("on-hold".equals(codeString))
          return GoalStatus.ONHOLD;
        if ("cancelled".equals(codeString))
          return GoalStatus.CANCELLED;
        throw new IllegalArgumentException("Unknown GoalStatus code '"+codeString+"'");
        }
        public Enumeration<GoalStatus> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("proposed".equals(codeString))
          return new Enumeration<GoalStatus>(this, GoalStatus.PROPOSED);
        if ("planned".equals(codeString))
          return new Enumeration<GoalStatus>(this, GoalStatus.PLANNED);
        if ("accepted".equals(codeString))
          return new Enumeration<GoalStatus>(this, GoalStatus.ACCEPTED);
        if ("rejected".equals(codeString))
          return new Enumeration<GoalStatus>(this, GoalStatus.REJECTED);
        if ("in-progress".equals(codeString))
          return new Enumeration<GoalStatus>(this, GoalStatus.INPROGRESS);
        if ("achieved".equals(codeString))
          return new Enumeration<GoalStatus>(this, GoalStatus.ACHIEVED);
        if ("sustaining".equals(codeString))
          return new Enumeration<GoalStatus>(this, GoalStatus.SUSTAINING);
        if ("on-hold".equals(codeString))
          return new Enumeration<GoalStatus>(this, GoalStatus.ONHOLD);
        if ("cancelled".equals(codeString))
          return new Enumeration<GoalStatus>(this, GoalStatus.CANCELLED);
        throw new FHIRException("Unknown GoalStatus code '"+codeString+"'");
        }
    public String toCode(GoalStatus code) {
      if (code == GoalStatus.PROPOSED)
        return "proposed";
      if (code == GoalStatus.PLANNED)
        return "planned";
      if (code == GoalStatus.ACCEPTED)
        return "accepted";
      if (code == GoalStatus.REJECTED)
        return "rejected";
      if (code == GoalStatus.INPROGRESS)
        return "in-progress";
      if (code == GoalStatus.ACHIEVED)
        return "achieved";
      if (code == GoalStatus.SUSTAINING)
        return "sustaining";
      if (code == GoalStatus.ONHOLD)
        return "on-hold";
      if (code == GoalStatus.CANCELLED)
        return "cancelled";
      return "?";
      }
    public String toSystem(GoalStatus code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class GoalOutcomeComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Details of what's changed (or not changed).
         */
        @Child(name = "result", type = {CodeableConcept.class, Observation.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Code or observation that resulted from goal", formalDefinition="Details of what's changed (or not changed)." )
        protected Type result;

        private static final long serialVersionUID = 1994317639L;

    /**
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
        public CodeableConcept getResultCodeableConcept() throws FHIRException { 
          if (!(this.result instanceof CodeableConcept))
            throw new FHIRException("Type mismatch: the type CodeableConcept was expected, but "+this.result.getClass().getName()+" was encountered");
          return (CodeableConcept) this.result;
        }

        public boolean hasResultCodeableConcept() { 
          return this.result instanceof CodeableConcept;
        }

        /**
         * @return {@link #result} (Details of what's changed (or not changed).)
         */
        public Reference getResultReference() throws FHIRException { 
          if (!(this.result instanceof Reference))
            throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.result.getClass().getName()+" was encountered");
          return (Reference) this.result;
        }

        public boolean hasResultReference() { 
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

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("result[x]"))
          this.result = (Type) value; // Type
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("resultCodeableConcept")) {
          this.result = new CodeableConcept();
          return this.result;
        }
        else if (name.equals("resultReference")) {
          this.result = new Reference();
          return this.result;
        }
        else
          return super.addChild(name);
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

  public String fhirType() {
    return "Goal.outcome";

  }

  }

    /**
     * This records identifiers associated with this care plan that are defined by business processes and/or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation).
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="External Ids for this goal", formalDefinition="This records identifiers associated with this care plan that are defined by business processes and/or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)." )
    protected List<Identifier> identifier;

    /**
     * Identifies the patient, group or organization for whom the goal is being established.
     */
    @Child(name = "subject", type = {Patient.class, Group.class, Organization.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Who this goal is intended for", formalDefinition="Identifies the patient, group or organization for whom the goal is being established." )
    protected Reference subject;

    /**
     * The actual object that is the target of the reference (Identifies the patient, group or organization for whom the goal is being established.)
     */
    protected Resource subjectTarget;

    /**
     * The date or event after which the goal should begin being pursued.
     */
    @Child(name = "start", type = {DateType.class, CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When goal pursuit begins", formalDefinition="The date or event after which the goal should begin being pursued." )
    protected Type start;

    /**
     * Indicates either the date or the duration after start by which the goal should be met.
     */
    @Child(name = "target", type = {DateType.class, Duration.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Reach goal on or before", formalDefinition="Indicates either the date or the duration after start by which the goal should be met." )
    protected Type target;

    /**
     * Indicates a category the goal falls within.
     */
    @Child(name = "category", type = {CodeableConcept.class}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="E.g. Treatment, dietary, behavioral, etc.", formalDefinition="Indicates a category the goal falls within." )
    protected List<CodeableConcept> category;

    /**
     * Human-readable description of a specific desired objective of care.
     */
    @Child(name = "description", type = {StringType.class}, order=5, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="What's the desired outcome?", formalDefinition="Human-readable description of a specific desired objective of care." )
    protected StringType description;

    /**
     * Indicates whether the goal has been reached and is still considered relevant.
     */
    @Child(name = "status", type = {CodeType.class}, order=6, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="proposed | planned | accepted | rejected | in-progress | achieved | sustaining | on-hold | cancelled", formalDefinition="Indicates whether the goal has been reached and is still considered relevant." )
    protected Enumeration<GoalStatus> status;

    /**
     * Identifies when the current status.  I.e. When initially created, when achieved, when cancelled, etc.
     */
    @Child(name = "statusDate", type = {DateType.class}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When goal status took effect", formalDefinition="Identifies when the current status.  I.e. When initially created, when achieved, when cancelled, etc." )
    protected DateType statusDate;

    /**
     * Captures the reason for the current status.
     */
    @Child(name = "statusReason", type = {CodeableConcept.class}, order=8, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Reason for current status", formalDefinition="Captures the reason for the current status." )
    protected CodeableConcept statusReason;

    /**
     * Indicates whose goal this is - patient goal, practitioner goal, etc.
     */
    @Child(name = "author", type = {Patient.class, Practitioner.class, RelatedPerson.class}, order=9, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Who's responsible for creating Goal?", formalDefinition="Indicates whose goal this is - patient goal, practitioner goal, etc." )
    protected Reference author;

    /**
     * The actual object that is the target of the reference (Indicates whose goal this is - patient goal, practitioner goal, etc.)
     */
    protected Resource authorTarget;

    /**
     * Identifies the mutually agreed level of importance associated with reaching/sustaining the goal.
     */
    @Child(name = "priority", type = {CodeableConcept.class}, order=10, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="high | medium |low", formalDefinition="Identifies the mutually agreed level of importance associated with reaching/sustaining the goal." )
    protected CodeableConcept priority;

    /**
     * The identified conditions and other health record elements that are intended to be addressed by the goal.
     */
    @Child(name = "addresses", type = {Condition.class, Observation.class, MedicationStatement.class, NutritionOrder.class, ProcedureRequest.class, RiskAssessment.class}, order=11, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Issues addressed by this goal", formalDefinition="The identified conditions and other health record elements that are intended to be addressed by the goal." )
    protected List<Reference> addresses;
    /**
     * The actual objects that are the target of the reference (The identified conditions and other health record elements that are intended to be addressed by the goal.)
     */
    protected List<Resource> addressesTarget;


    /**
     * Any comments related to the goal.
     */
    @Child(name = "note", type = {Annotation.class}, order=12, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Comments about the goal", formalDefinition="Any comments related to the goal." )
    protected List<Annotation> note;

    /**
     * Identifies the change (or lack of change) at the point where the goal was deepmed to be cancelled or achieved.
     */
    @Child(name = "outcome", type = {}, order=13, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="What was end result of goal?", formalDefinition="Identifies the change (or lack of change) at the point where the goal was deepmed to be cancelled or achieved." )
    protected List<GoalOutcomeComponent> outcome;

    private static final long serialVersionUID = 2029459056L;

  /**
   * Constructor
   */
    public Goal() {
      super();
    }

  /**
   * Constructor
   */
    public Goal(StringType description, Enumeration<GoalStatus> status) {
      super();
      this.description = description;
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
    public Goal addIdentifier(Identifier t) { //3
      if (t == null)
        return this;
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return this;
    }

    /**
     * @return {@link #subject} (Identifies the patient, group or organization for whom the goal is being established.)
     */
    public Reference getSubject() { 
      if (this.subject == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Goal.subject");
        else if (Configuration.doAutoCreate())
          this.subject = new Reference(); // cc
      return this.subject;
    }

    public boolean hasSubject() { 
      return this.subject != null && !this.subject.isEmpty();
    }

    /**
     * @param value {@link #subject} (Identifies the patient, group or organization for whom the goal is being established.)
     */
    public Goal setSubject(Reference value) { 
      this.subject = value;
      return this;
    }

    /**
     * @return {@link #subject} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Identifies the patient, group or organization for whom the goal is being established.)
     */
    public Resource getSubjectTarget() { 
      return this.subjectTarget;
    }

    /**
     * @param value {@link #subject} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Identifies the patient, group or organization for whom the goal is being established.)
     */
    public Goal setSubjectTarget(Resource value) { 
      this.subjectTarget = value;
      return this;
    }

    /**
     * @return {@link #start} (The date or event after which the goal should begin being pursued.)
     */
    public Type getStart() { 
      return this.start;
    }

    /**
     * @return {@link #start} (The date or event after which the goal should begin being pursued.)
     */
    public DateType getStartDateType() throws FHIRException { 
      if (!(this.start instanceof DateType))
        throw new FHIRException("Type mismatch: the type DateType was expected, but "+this.start.getClass().getName()+" was encountered");
      return (DateType) this.start;
    }

    public boolean hasStartDateType() { 
      return this.start instanceof DateType;
    }

    /**
     * @return {@link #start} (The date or event after which the goal should begin being pursued.)
     */
    public CodeableConcept getStartCodeableConcept() throws FHIRException { 
      if (!(this.start instanceof CodeableConcept))
        throw new FHIRException("Type mismatch: the type CodeableConcept was expected, but "+this.start.getClass().getName()+" was encountered");
      return (CodeableConcept) this.start;
    }

    public boolean hasStartCodeableConcept() { 
      return this.start instanceof CodeableConcept;
    }

    public boolean hasStart() { 
      return this.start != null && !this.start.isEmpty();
    }

    /**
     * @param value {@link #start} (The date or event after which the goal should begin being pursued.)
     */
    public Goal setStart(Type value) { 
      this.start = value;
      return this;
    }

    /**
     * @return {@link #target} (Indicates either the date or the duration after start by which the goal should be met.)
     */
    public Type getTarget() { 
      return this.target;
    }

    /**
     * @return {@link #target} (Indicates either the date or the duration after start by which the goal should be met.)
     */
    public DateType getTargetDateType() throws FHIRException { 
      if (!(this.target instanceof DateType))
        throw new FHIRException("Type mismatch: the type DateType was expected, but "+this.target.getClass().getName()+" was encountered");
      return (DateType) this.target;
    }

    public boolean hasTargetDateType() { 
      return this.target instanceof DateType;
    }

    /**
     * @return {@link #target} (Indicates either the date or the duration after start by which the goal should be met.)
     */
    public Duration getTargetDuration() throws FHIRException { 
      if (!(this.target instanceof Duration))
        throw new FHIRException("Type mismatch: the type Duration was expected, but "+this.target.getClass().getName()+" was encountered");
      return (Duration) this.target;
    }

    public boolean hasTargetDuration() { 
      return this.target instanceof Duration;
    }

    public boolean hasTarget() { 
      return this.target != null && !this.target.isEmpty();
    }

    /**
     * @param value {@link #target} (Indicates either the date or the duration after start by which the goal should be met.)
     */
    public Goal setTarget(Type value) { 
      this.target = value;
      return this;
    }

    /**
     * @return {@link #category} (Indicates a category the goal falls within.)
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
     * @return {@link #category} (Indicates a category the goal falls within.)
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
    public Goal addCategory(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.category == null)
        this.category = new ArrayList<CodeableConcept>();
      this.category.add(t);
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
     * @return {@link #statusReason} (Captures the reason for the current status.)
     */
    public CodeableConcept getStatusReason() { 
      if (this.statusReason == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Goal.statusReason");
        else if (Configuration.doAutoCreate())
          this.statusReason = new CodeableConcept(); // cc
      return this.statusReason;
    }

    public boolean hasStatusReason() { 
      return this.statusReason != null && !this.statusReason.isEmpty();
    }

    /**
     * @param value {@link #statusReason} (Captures the reason for the current status.)
     */
    public Goal setStatusReason(CodeableConcept value) { 
      this.statusReason = value;
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
     * @return {@link #priority} (Identifies the mutually agreed level of importance associated with reaching/sustaining the goal.)
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
     * @param value {@link #priority} (Identifies the mutually agreed level of importance associated with reaching/sustaining the goal.)
     */
    public Goal setPriority(CodeableConcept value) { 
      this.priority = value;
      return this;
    }

    /**
     * @return {@link #addresses} (The identified conditions and other health record elements that are intended to be addressed by the goal.)
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
     * @return {@link #addresses} (The identified conditions and other health record elements that are intended to be addressed by the goal.)
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
    public Goal addAddresses(Reference t) { //3
      if (t == null)
        return this;
      if (this.addresses == null)
        this.addresses = new ArrayList<Reference>();
      this.addresses.add(t);
      return this;
    }

    /**
     * @return {@link #addresses} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. The identified conditions and other health record elements that are intended to be addressed by the goal.)
     */
    public List<Resource> getAddressesTarget() { 
      if (this.addressesTarget == null)
        this.addressesTarget = new ArrayList<Resource>();
      return this.addressesTarget;
    }

    /**
     * @return {@link #note} (Any comments related to the goal.)
     */
    public List<Annotation> getNote() { 
      if (this.note == null)
        this.note = new ArrayList<Annotation>();
      return this.note;
    }

    public boolean hasNote() { 
      if (this.note == null)
        return false;
      for (Annotation item : this.note)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #note} (Any comments related to the goal.)
     */
    // syntactic sugar
    public Annotation addNote() { //3
      Annotation t = new Annotation();
      if (this.note == null)
        this.note = new ArrayList<Annotation>();
      this.note.add(t);
      return t;
    }

    // syntactic sugar
    public Goal addNote(Annotation t) { //3
      if (t == null)
        return this;
      if (this.note == null)
        this.note = new ArrayList<Annotation>();
      this.note.add(t);
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
        childrenList.add(new Property("identifier", "Identifier", "This records identifiers associated with this care plan that are defined by business processes and/or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation).", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("subject", "Reference(Patient|Group|Organization)", "Identifies the patient, group or organization for whom the goal is being established.", 0, java.lang.Integer.MAX_VALUE, subject));
        childrenList.add(new Property("start[x]", "date|CodeableConcept", "The date or event after which the goal should begin being pursued.", 0, java.lang.Integer.MAX_VALUE, start));
        childrenList.add(new Property("target[x]", "date|Duration", "Indicates either the date or the duration after start by which the goal should be met.", 0, java.lang.Integer.MAX_VALUE, target));
        childrenList.add(new Property("category", "CodeableConcept", "Indicates a category the goal falls within.", 0, java.lang.Integer.MAX_VALUE, category));
        childrenList.add(new Property("description", "string", "Human-readable description of a specific desired objective of care.", 0, java.lang.Integer.MAX_VALUE, description));
        childrenList.add(new Property("status", "code", "Indicates whether the goal has been reached and is still considered relevant.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("statusDate", "date", "Identifies when the current status.  I.e. When initially created, when achieved, when cancelled, etc.", 0, java.lang.Integer.MAX_VALUE, statusDate));
        childrenList.add(new Property("statusReason", "CodeableConcept", "Captures the reason for the current status.", 0, java.lang.Integer.MAX_VALUE, statusReason));
        childrenList.add(new Property("author", "Reference(Patient|Practitioner|RelatedPerson)", "Indicates whose goal this is - patient goal, practitioner goal, etc.", 0, java.lang.Integer.MAX_VALUE, author));
        childrenList.add(new Property("priority", "CodeableConcept", "Identifies the mutually agreed level of importance associated with reaching/sustaining the goal.", 0, java.lang.Integer.MAX_VALUE, priority));
        childrenList.add(new Property("addresses", "Reference(Condition|Observation|MedicationStatement|NutritionOrder|ProcedureRequest|RiskAssessment)", "The identified conditions and other health record elements that are intended to be addressed by the goal.", 0, java.lang.Integer.MAX_VALUE, addresses));
        childrenList.add(new Property("note", "Annotation", "Any comments related to the goal.", 0, java.lang.Integer.MAX_VALUE, note));
        childrenList.add(new Property("outcome", "", "Identifies the change (or lack of change) at the point where the goal was deepmed to be cancelled or achieved.", 0, java.lang.Integer.MAX_VALUE, outcome));
      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier"))
          this.getIdentifier().add(castToIdentifier(value));
        else if (name.equals("subject"))
          this.subject = castToReference(value); // Reference
        else if (name.equals("start[x]"))
          this.start = (Type) value; // Type
        else if (name.equals("target[x]"))
          this.target = (Type) value; // Type
        else if (name.equals("category"))
          this.getCategory().add(castToCodeableConcept(value));
        else if (name.equals("description"))
          this.description = castToString(value); // StringType
        else if (name.equals("status"))
          this.status = new GoalStatusEnumFactory().fromType(value); // Enumeration<GoalStatus>
        else if (name.equals("statusDate"))
          this.statusDate = castToDate(value); // DateType
        else if (name.equals("statusReason"))
          this.statusReason = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("author"))
          this.author = castToReference(value); // Reference
        else if (name.equals("priority"))
          this.priority = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("addresses"))
          this.getAddresses().add(castToReference(value));
        else if (name.equals("note"))
          this.getNote().add(castToAnnotation(value));
        else if (name.equals("outcome"))
          this.getOutcome().add((GoalOutcomeComponent) value);
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
        else if (name.equals("startDate")) {
          this.start = new DateType();
          return this.start;
        }
        else if (name.equals("startCodeableConcept")) {
          this.start = new CodeableConcept();
          return this.start;
        }
        else if (name.equals("targetDate")) {
          this.target = new DateType();
          return this.target;
        }
        else if (name.equals("targetDuration")) {
          this.target = new Duration();
          return this.target;
        }
        else if (name.equals("category")) {
          return addCategory();
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type Goal.description");
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type Goal.status");
        }
        else if (name.equals("statusDate")) {
          throw new FHIRException("Cannot call addChild on a primitive type Goal.statusDate");
        }
        else if (name.equals("statusReason")) {
          this.statusReason = new CodeableConcept();
          return this.statusReason;
        }
        else if (name.equals("author")) {
          this.author = new Reference();
          return this.author;
        }
        else if (name.equals("priority")) {
          this.priority = new CodeableConcept();
          return this.priority;
        }
        else if (name.equals("addresses")) {
          return addAddresses();
        }
        else if (name.equals("note")) {
          return addNote();
        }
        else if (name.equals("outcome")) {
          return addOutcome();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "Goal";

  }

      public Goal copy() {
        Goal dst = new Goal();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.subject = subject == null ? null : subject.copy();
        dst.start = start == null ? null : start.copy();
        dst.target = target == null ? null : target.copy();
        if (category != null) {
          dst.category = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : category)
            dst.category.add(i.copy());
        };
        dst.description = description == null ? null : description.copy();
        dst.status = status == null ? null : status.copy();
        dst.statusDate = statusDate == null ? null : statusDate.copy();
        dst.statusReason = statusReason == null ? null : statusReason.copy();
        dst.author = author == null ? null : author.copy();
        dst.priority = priority == null ? null : priority.copy();
        if (addresses != null) {
          dst.addresses = new ArrayList<Reference>();
          for (Reference i : addresses)
            dst.addresses.add(i.copy());
        };
        if (note != null) {
          dst.note = new ArrayList<Annotation>();
          for (Annotation i : note)
            dst.note.add(i.copy());
        };
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
        return compareDeep(identifier, o.identifier, true) && compareDeep(subject, o.subject, true) && compareDeep(start, o.start, true)
           && compareDeep(target, o.target, true) && compareDeep(category, o.category, true) && compareDeep(description, o.description, true)
           && compareDeep(status, o.status, true) && compareDeep(statusDate, o.statusDate, true) && compareDeep(statusReason, o.statusReason, true)
           && compareDeep(author, o.author, true) && compareDeep(priority, o.priority, true) && compareDeep(addresses, o.addresses, true)
           && compareDeep(note, o.note, true) && compareDeep(outcome, o.outcome, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Goal))
          return false;
        Goal o = (Goal) other;
        return compareValues(description, o.description, true) && compareValues(status, o.status, true) && compareValues(statusDate, o.statusDate, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (subject == null || subject.isEmpty())
           && (start == null || start.isEmpty()) && (target == null || target.isEmpty()) && (category == null || category.isEmpty())
           && (description == null || description.isEmpty()) && (status == null || status.isEmpty())
           && (statusDate == null || statusDate.isEmpty()) && (statusReason == null || statusReason.isEmpty())
           && (author == null || author.isEmpty()) && (priority == null || priority.isEmpty()) && (addresses == null || addresses.isEmpty())
           && (note == null || note.isEmpty()) && (outcome == null || outcome.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Goal;
   }

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>External Ids for this goal</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Goal.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="Goal.identifier", description="External Ids for this goal", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>External Ids for this goal</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Goal.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>Who this goal is intended for</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Goal.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="Goal.subject", description="Who this goal is intended for", type="reference" )
  public static final String SP_PATIENT = "patient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patient</b>
   * <p>
   * Description: <b>Who this goal is intended for</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Goal.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PATIENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PATIENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Goal:patient</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PATIENT = new ca.uhn.fhir.model.api.Include("Goal:patient").toLocked();

 /**
   * Search parameter: <b>subject</b>
   * <p>
   * Description: <b>Who this goal is intended for</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Goal.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="subject", path="Goal.subject", description="Who this goal is intended for", type="reference" )
  public static final String SP_SUBJECT = "subject";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>subject</b>
   * <p>
   * Description: <b>Who this goal is intended for</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Goal.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SUBJECT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SUBJECT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Goal:subject</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SUBJECT = new ca.uhn.fhir.model.api.Include("Goal:subject").toLocked();

 /**
   * Search parameter: <b>targetdate</b>
   * <p>
   * Description: <b>Reach goal on or before</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Goal.targetDate</b><br>
   * </p>
   */
  @SearchParamDefinition(name="targetdate", path="Goal.targetDate", description="Reach goal on or before", type="date" )
  public static final String SP_TARGETDATE = "targetdate";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>targetdate</b>
   * <p>
   * Description: <b>Reach goal on or before</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Goal.targetDate</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam TARGETDATE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_TARGETDATE);

 /**
   * Search parameter: <b>category</b>
   * <p>
   * Description: <b>E.g. Treatment, dietary, behavioral, etc.</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Goal.category</b><br>
   * </p>
   */
  @SearchParamDefinition(name="category", path="Goal.category", description="E.g. Treatment, dietary, behavioral, etc.", type="token" )
  public static final String SP_CATEGORY = "category";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>category</b>
   * <p>
   * Description: <b>E.g. Treatment, dietary, behavioral, etc.</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Goal.category</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CATEGORY = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CATEGORY);

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>proposed | planned | accepted | rejected | in-progress | achieved | sustaining | on-hold | cancelled</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Goal.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="Goal.status", description="proposed | planned | accepted | rejected | in-progress | achieved | sustaining | on-hold | cancelled", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>proposed | planned | accepted | rejected | in-progress | achieved | sustaining | on-hold | cancelled</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Goal.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);


}

