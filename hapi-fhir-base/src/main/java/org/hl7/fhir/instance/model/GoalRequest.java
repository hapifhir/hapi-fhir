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
 * Describes the intended objective(s) of carrying out the Care Plan.
 */
@ResourceDef(name="GoalRequest", profile="http://hl7.org/fhir/Profile/GoalRequest")
public class GoalRequest extends DomainResource {

    public enum GoalRequestStatus implements FhirEnum {
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

      public static final GoalRequestStatusEnumFactory ENUM_FACTORY = new GoalRequestStatusEnumFactory();

        public static GoalRequestStatus fromCode(String codeString) throws IllegalArgumentException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("in progress".equals(codeString))
          return INPROGRESS;
        if ("achieved".equals(codeString))
          return ACHIEVED;
        if ("sustaining".equals(codeString))
          return SUSTAINING;
        if ("cancelled".equals(codeString))
          return CANCELLED;
        throw new IllegalArgumentException("Unknown GoalRequestStatus code '"+codeString+"'");
        }
        @Override
        public String toCode() {
          switch (this) {
            case INPROGRESS: return "in progress";
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
            case INPROGRESS: return "in progress";
            case ACHIEVED: return "achieved";
            case SUSTAINING: return "sustaining";
            case CANCELLED: return "cancelled";
            default: return "?";
          }
        }
    }

  public static class GoalRequestStatusEnumFactory implements EnumFactory<GoalRequestStatus> {
    public GoalRequestStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("in progress".equals(codeString))
          return GoalRequestStatus.INPROGRESS;
        if ("achieved".equals(codeString))
          return GoalRequestStatus.ACHIEVED;
        if ("sustaining".equals(codeString))
          return GoalRequestStatus.SUSTAINING;
        if ("cancelled".equals(codeString))
          return GoalRequestStatus.CANCELLED;
        throw new IllegalArgumentException("Unknown GoalRequestStatus code '"+codeString+"'");
        }
    public String toCode(GoalRequestStatus code) throws IllegalArgumentException {
      if (code == GoalRequestStatus.INPROGRESS)
        return "in progress";
      if (code == GoalRequestStatus.ACHIEVED)
        return "achieved";
      if (code == GoalRequestStatus.SUSTAINING)
        return "sustaining";
      if (code == GoalRequestStatus.CANCELLED)
        return "cancelled";
      return "?";
      }
    }

    public enum GoalRequestMode implements FhirEnum {
        /**
         * planned.
         */
        PLANNED, 
        /**
         * proposed.
         */
        PROPOSED, 
        /**
         * ordered.
         */
        ORDERED, 
        /**
         * added to help the parsers
         */
        NULL;

      public static final GoalRequestModeEnumFactory ENUM_FACTORY = new GoalRequestModeEnumFactory();

        public static GoalRequestMode fromCode(String codeString) throws IllegalArgumentException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("planned".equals(codeString))
          return PLANNED;
        if ("proposed".equals(codeString))
          return PROPOSED;
        if ("ordered".equals(codeString))
          return ORDERED;
        throw new IllegalArgumentException("Unknown GoalRequestMode code '"+codeString+"'");
        }
        @Override
        public String toCode() {
          switch (this) {
            case PLANNED: return "planned";
            case PROPOSED: return "proposed";
            case ORDERED: return "ordered";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case PLANNED: return "";
            case PROPOSED: return "";
            case ORDERED: return "";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case PLANNED: return "planned.";
            case PROPOSED: return "proposed.";
            case ORDERED: return "ordered.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PLANNED: return "planned";
            case PROPOSED: return "proposed";
            case ORDERED: return "ordered";
            default: return "?";
          }
        }
    }

  public static class GoalRequestModeEnumFactory implements EnumFactory<GoalRequestMode> {
    public GoalRequestMode fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("planned".equals(codeString))
          return GoalRequestMode.PLANNED;
        if ("proposed".equals(codeString))
          return GoalRequestMode.PROPOSED;
        if ("ordered".equals(codeString))
          return GoalRequestMode.ORDERED;
        throw new IllegalArgumentException("Unknown GoalRequestMode code '"+codeString+"'");
        }
    public String toCode(GoalRequestMode code) throws IllegalArgumentException {
      if (code == GoalRequestMode.PLANNED)
        return "planned";
      if (code == GoalRequestMode.PROPOSED)
        return "proposed";
      if (code == GoalRequestMode.ORDERED)
        return "ordered";
      return "?";
      }
    }

    /**
     * This records identifiers associated with this care plan that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation).
     */
    @Child(name="identifier", type={Identifier.class}, order=-1, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="External Ids for this goal", formalDefinition="This records identifiers associated with this care plan that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)." )
    protected List<Identifier> identifier;

    /**
     * Identifies the patient/subject whose intended care is described by the plan.
     */
    @Child(name="patient", type={Patient.class}, order=0, min=0, max=1)
    @Description(shortDefinition="The patient for whom this goal is intended for", formalDefinition="Identifies the patient/subject whose intended care is described by the plan." )
    protected Reference patient;

    /**
     * The actual object that is the target of the reference (Identifies the patient/subject whose intended care is described by the plan.)
     */
    protected Patient patientTarget;

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
    @Description(shortDefinition="in progress | achieved | sustaining | cancelled", formalDefinition="Indicates whether the goal has been reached and is still considered relevant." )
    protected Enumeration<GoalRequestStatus> status;

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


    /**
     * The status of the order.
     */
    @Child(name="mode", type={CodeType.class}, order=5, min=0, max=1)
    @Description(shortDefinition="planned | proposed | ordered", formalDefinition="The status of the order." )
    protected Enumeration<GoalRequestMode> mode;

    private static final long serialVersionUID = 1698507147L;

    public GoalRequest() {
      super();
    }

    public GoalRequest(StringType description) {
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
          throw new Error("Attempt to auto-create GoalRequest.patient");
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
    public GoalRequest setPatient(Reference value) { 
      this.patient = value;
      return this;
    }

    /**
     * @return {@link #patient} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Identifies the patient/subject whose intended care is described by the plan.)
     */
    public Patient getPatientTarget() { 
      if (this.patientTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create GoalRequest.patient");
        else if (Configuration.doAutoCreate())
          this.patientTarget = new Patient();
      return this.patientTarget;
    }

    /**
     * @param value {@link #patient} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Identifies the patient/subject whose intended care is described by the plan.)
     */
    public GoalRequest setPatientTarget(Patient value) { 
      this.patientTarget = value;
      return this;
    }

    /**
     * @return {@link #description} (Human-readable description of a specific desired objective of the care plan.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public StringType getDescriptionElement() { 
      if (this.description == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create GoalRequest.description");
        else if (Configuration.doAutoCreate())
          this.description = new StringType();
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
    public GoalRequest setDescriptionElement(StringType value) { 
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
    public GoalRequest setDescription(String value) { 
        if (this.description == null)
          this.description = new StringType();
        this.description.setValue(value);
      return this;
    }

    /**
     * @return {@link #status} (Indicates whether the goal has been reached and is still considered relevant.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<GoalRequestStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create GoalRequest.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<GoalRequestStatus>();
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
    public GoalRequest setStatusElement(Enumeration<GoalRequestStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return Indicates whether the goal has been reached and is still considered relevant.
     */
    public GoalRequestStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value Indicates whether the goal has been reached and is still considered relevant.
     */
    public GoalRequest setStatus(GoalRequestStatus value) { 
      if (value == null)
        this.status = null;
      else {
        if (this.status == null)
          this.status = new Enumeration<GoalRequestStatus>(GoalRequestStatus.ENUM_FACTORY);
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
          throw new Error("Attempt to auto-create GoalRequest.notes");
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
     * @param value {@link #notes} (Any comments related to the goal.). This is the underlying object with id, value and extensions. The accessor "getNotes" gives direct access to the value
     */
    public GoalRequest setNotesElement(StringType value) { 
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
    public GoalRequest setNotes(String value) { 
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

    /**
     * @return {@link #mode} (The status of the order.). This is the underlying object with id, value and extensions. The accessor "getMode" gives direct access to the value
     */
    public Enumeration<GoalRequestMode> getModeElement() { 
      if (this.mode == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create GoalRequest.mode");
        else if (Configuration.doAutoCreate())
          this.mode = new Enumeration<GoalRequestMode>();
      return this.mode;
    }

    public boolean hasModeElement() { 
      return this.mode != null && !this.mode.isEmpty();
    }

    public boolean hasMode() { 
      return this.mode != null && !this.mode.isEmpty();
    }

    /**
     * @param value {@link #mode} (The status of the order.). This is the underlying object with id, value and extensions. The accessor "getMode" gives direct access to the value
     */
    public GoalRequest setModeElement(Enumeration<GoalRequestMode> value) { 
      this.mode = value;
      return this;
    }

    /**
     * @return The status of the order.
     */
    public GoalRequestMode getMode() { 
      return this.mode == null ? null : this.mode.getValue();
    }

    /**
     * @param value The status of the order.
     */
    public GoalRequest setMode(GoalRequestMode value) { 
      if (value == null)
        this.mode = null;
      else {
        if (this.mode == null)
          this.mode = new Enumeration<GoalRequestMode>(GoalRequestMode.ENUM_FACTORY);
        this.mode.setValue(value);
      }
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "This records identifiers associated with this care plan that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation).", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("patient", "Reference(Patient)", "Identifies the patient/subject whose intended care is described by the plan.", 0, java.lang.Integer.MAX_VALUE, patient));
        childrenList.add(new Property("description", "string", "Human-readable description of a specific desired objective of the care plan.", 0, java.lang.Integer.MAX_VALUE, description));
        childrenList.add(new Property("status", "code", "Indicates whether the goal has been reached and is still considered relevant.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("notes", "string", "Any comments related to the goal.", 0, java.lang.Integer.MAX_VALUE, notes));
        childrenList.add(new Property("concern", "Reference(Condition)", "The identified conditions that this goal relates to - the condition that caused it to be created, or that it is intended to address.", 0, java.lang.Integer.MAX_VALUE, concern));
        childrenList.add(new Property("mode", "code", "The status of the order.", 0, java.lang.Integer.MAX_VALUE, mode));
      }

      public GoalRequest copy() {
        GoalRequest dst = new GoalRequest();
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
        dst.mode = mode == null ? null : mode.copy();
        return dst;
      }

      protected GoalRequest typedCopy() {
        return copy();
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (patient == null || patient.isEmpty())
           && (description == null || description.isEmpty()) && (status == null || status.isEmpty())
           && (notes == null || notes.isEmpty()) && (concern == null || concern.isEmpty()) && (mode == null || mode.isEmpty())
          ;
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.GoalRequest;
   }

  @SearchParamDefinition(name="patient", path="GoalRequest.patient", description="The patient for whom this goal is intended for", type="reference" )
  public static final String SP_PATIENT = "patient";

}

