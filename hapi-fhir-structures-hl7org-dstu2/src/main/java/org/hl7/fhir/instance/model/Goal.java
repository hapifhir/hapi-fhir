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
 * Describes the intended objective(s) of the care.
 */
@ResourceDef(name="Goal", profile="http://hl7.org/fhir/Profile/Goal")
public class Goal extends DomainResource {

    public enum GoalStatus {
        /**
         * A goal is proposed for this patient.
         */
        PROPOSED, 
        /**
         * A goal is planned for this patient.
         */
        PLANNED, 
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
         * A proposed goal was accepted.
         */
        ACCEPTED, 
        /**
         * A proposed goal was rejected.
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
            case PROPOSED: return "";
            case PLANNED: return "";
            case INPROGRESS: return "";
            case ACHIEVED: return "";
            case SUSTAINING: return "";
            case CANCELLED: return "";
            case ACCEPTED: return "";
            case REJECTED: return "";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case PROPOSED: return "A goal is proposed for this patient.";
            case PLANNED: return "A goal is planned for this patient.";
            case INPROGRESS: return "The goal is being sought but has not yet been reached.  (Also applies if goal was reached in the past but there has been regression and goal is being sought again).";
            case ACHIEVED: return "The goal has been met and no further action is needed.";
            case SUSTAINING: return "The goal has been met, but ongoing activity is needed to sustain the goal objective.";
            case CANCELLED: return "The goal is no longer being sought.";
            case ACCEPTED: return "A proposed goal was accepted.";
            case REJECTED: return "A proposed goal was rejected.";
            default: return "?";
          }
        }
        public String getDisplay() {
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

    /**
     * This records identifiers associated with this care plan that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation).
     */
    @Child(name = "identifier", type = {Identifier.class}, order = 0, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="External Ids for this goal", formalDefinition="This records identifiers associated with this care plan that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)." )
    protected List<Identifier> identifier;

    /**
     * Identifies the patient/subject whose intended care is described by the plan.
     */
    @Child(name = "patient", type = {Patient.class}, order = 1, min = 0, max = 1)
    @Description(shortDefinition="The patient for whom this goal is intended for", formalDefinition="Identifies the patient/subject whose intended care is described by the plan." )
    protected Reference patient;

    /**
     * The actual object that is the target of the reference (Identifies the patient/subject whose intended care is described by the plan.)
     */
    protected Patient patientTarget;

    /**
     * Human-readable description of a specific desired objective of care.
     */
    @Child(name = "description", type = {StringType.class}, order = 2, min = 1, max = 1)
    @Description(shortDefinition="What's the desired outcome?", formalDefinition="Human-readable description of a specific desired objective of care." )
    protected StringType description;

    /**
     * Indicates whether the goal has been reached and is still considered relevant.
     */
    @Child(name = "status", type = {CodeType.class}, order = 3, min = 0, max = 1)
    @Description(shortDefinition="proposed | planned | in-progress | achieved | sustaining | cancelled | accepted | rejected", formalDefinition="Indicates whether the goal has been reached and is still considered relevant." )
    protected Enumeration<GoalStatus> status;

    /**
     * Any comments related to the goal.
     */
    @Child(name = "notes", type = {StringType.class}, order = 4, min = 0, max = 1)
    @Description(shortDefinition="Comments about the goal", formalDefinition="Any comments related to the goal." )
    protected StringType notes;

    /**
     * The identified conditions that this goal relates to - the condition that caused it to be created, or that it is intended to address.
     */
    @Child(name = "concern", type = {Condition.class}, order = 5, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Health issues this goal addresses", formalDefinition="The identified conditions that this goal relates to - the condition that caused it to be created, or that it is intended to address." )
    protected List<Reference> concern;
    /**
     * The actual objects that are the target of the reference (The identified conditions that this goal relates to - the condition that caused it to be created, or that it is intended to address.)
     */
    protected List<Condition> concernTarget;


    private static final long serialVersionUID = -73791119L;

    public Goal() {
      super();
    }

    public Goal(StringType description) {
      super();
      this.description = description;
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
      if (value == null)
        this.status = null;
      else {
        if (this.status == null)
          this.status = new Enumeration<GoalStatus>(new GoalStatusEnumFactory());
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
        childrenList.add(new Property("identifier", "Identifier", "This records identifiers associated with this care plan that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation).", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("patient", "Reference(Patient)", "Identifies the patient/subject whose intended care is described by the plan.", 0, java.lang.Integer.MAX_VALUE, patient));
        childrenList.add(new Property("description", "string", "Human-readable description of a specific desired objective of care.", 0, java.lang.Integer.MAX_VALUE, description));
        childrenList.add(new Property("status", "code", "Indicates whether the goal has been reached and is still considered relevant.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("notes", "string", "Any comments related to the goal.", 0, java.lang.Integer.MAX_VALUE, notes));
        childrenList.add(new Property("concern", "Reference(Condition)", "The identified conditions that this goal relates to - the condition that caused it to be created, or that it is intended to address.", 0, java.lang.Integer.MAX_VALUE, concern));
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
        return compareDeep(identifier, o.identifier, true) && compareDeep(patient, o.patient, true) && compareDeep(description, o.description, true)
           && compareDeep(status, o.status, true) && compareDeep(notes, o.notes, true) && compareDeep(concern, o.concern, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Goal))
          return false;
        Goal o = (Goal) other;
        return compareValues(description, o.description, true) && compareValues(status, o.status, true) && compareValues(notes, o.notes, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (patient == null || patient.isEmpty())
           && (description == null || description.isEmpty()) && (status == null || status.isEmpty())
           && (notes == null || notes.isEmpty()) && (concern == null || concern.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Goal;
   }

  @SearchParamDefinition(name="patient", path="Goal.patient", description="The patient for whom this goal is intended for", type="reference" )
  public static final String SP_PATIENT = "patient";

}

