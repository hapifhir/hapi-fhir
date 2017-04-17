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
         * A proposed goal was accepted or acknowledged
         */
        ACCEPTED, 
        /**
         * A goal is planned for this patient
         */
        PLANNED, 
        /**
         * The goal is being sought but has not yet been reached.  (Also applies if goal was reached in the past but there has been regression and goal is being sought again)
         */
        INPROGRESS, 
        /**
         * The goal is on schedule for the planned timelines
         */
        ONTARGET, 
        /**
         * The goal is ahead of the planned timelines
         */
        AHEADOFTARGET, 
        /**
         * The goal is behind the planned timelines
         */
        BEHINDTARGET, 
        /**
         * The goal has been met, but ongoing activity is needed to sustain the goal objective
         */
        SUSTAINING, 
        /**
         * The goal has been met and no further action is needed
         */
        ACHIEVED, 
        /**
         * The goal remains a long term objective but is no longer being actively pursued for a temporary period of time.
         */
        ONHOLD, 
        /**
         * The previously accepted goal is no longer being sought
         */
        CANCELLED, 
        /**
         * The goal was entered in error and voided.
         */
        ENTEREDINERROR, 
        /**
         * A proposed goal was rejected
         */
        REJECTED, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static GoalStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("proposed".equals(codeString))
          return PROPOSED;
        if ("accepted".equals(codeString))
          return ACCEPTED;
        if ("planned".equals(codeString))
          return PLANNED;
        if ("in-progress".equals(codeString))
          return INPROGRESS;
        if ("on-target".equals(codeString))
          return ONTARGET;
        if ("ahead-of-target".equals(codeString))
          return AHEADOFTARGET;
        if ("behind-target".equals(codeString))
          return BEHINDTARGET;
        if ("sustaining".equals(codeString))
          return SUSTAINING;
        if ("achieved".equals(codeString))
          return ACHIEVED;
        if ("on-hold".equals(codeString))
          return ONHOLD;
        if ("cancelled".equals(codeString))
          return CANCELLED;
        if ("entered-in-error".equals(codeString))
          return ENTEREDINERROR;
        if ("rejected".equals(codeString))
          return REJECTED;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown GoalStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PROPOSED: return "proposed";
            case ACCEPTED: return "accepted";
            case PLANNED: return "planned";
            case INPROGRESS: return "in-progress";
            case ONTARGET: return "on-target";
            case AHEADOFTARGET: return "ahead-of-target";
            case BEHINDTARGET: return "behind-target";
            case SUSTAINING: return "sustaining";
            case ACHIEVED: return "achieved";
            case ONHOLD: return "on-hold";
            case CANCELLED: return "cancelled";
            case ENTEREDINERROR: return "entered-in-error";
            case REJECTED: return "rejected";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case PROPOSED: return "http://hl7.org/fhir/goal-status";
            case ACCEPTED: return "http://hl7.org/fhir/goal-status";
            case PLANNED: return "http://hl7.org/fhir/goal-status";
            case INPROGRESS: return "http://hl7.org/fhir/goal-status";
            case ONTARGET: return "http://hl7.org/fhir/goal-status";
            case AHEADOFTARGET: return "http://hl7.org/fhir/goal-status";
            case BEHINDTARGET: return "http://hl7.org/fhir/goal-status";
            case SUSTAINING: return "http://hl7.org/fhir/goal-status";
            case ACHIEVED: return "http://hl7.org/fhir/goal-status";
            case ONHOLD: return "http://hl7.org/fhir/goal-status";
            case CANCELLED: return "http://hl7.org/fhir/goal-status";
            case ENTEREDINERROR: return "http://hl7.org/fhir/goal-status";
            case REJECTED: return "http://hl7.org/fhir/goal-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case PROPOSED: return "A goal is proposed for this patient";
            case ACCEPTED: return "A proposed goal was accepted or acknowledged";
            case PLANNED: return "A goal is planned for this patient";
            case INPROGRESS: return "The goal is being sought but has not yet been reached.  (Also applies if goal was reached in the past but there has been regression and goal is being sought again)";
            case ONTARGET: return "The goal is on schedule for the planned timelines";
            case AHEADOFTARGET: return "The goal is ahead of the planned timelines";
            case BEHINDTARGET: return "The goal is behind the planned timelines";
            case SUSTAINING: return "The goal has been met, but ongoing activity is needed to sustain the goal objective";
            case ACHIEVED: return "The goal has been met and no further action is needed";
            case ONHOLD: return "The goal remains a long term objective but is no longer being actively pursued for a temporary period of time.";
            case CANCELLED: return "The previously accepted goal is no longer being sought";
            case ENTEREDINERROR: return "The goal was entered in error and voided.";
            case REJECTED: return "A proposed goal was rejected";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PROPOSED: return "Proposed";
            case ACCEPTED: return "Accepted";
            case PLANNED: return "Planned";
            case INPROGRESS: return "In Progress";
            case ONTARGET: return "On Target";
            case AHEADOFTARGET: return "Ahead of Target";
            case BEHINDTARGET: return "Behind Target";
            case SUSTAINING: return "Sustaining";
            case ACHIEVED: return "Achieved";
            case ONHOLD: return "On Hold";
            case CANCELLED: return "Cancelled";
            case ENTEREDINERROR: return "Entered In Error";
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
        if ("accepted".equals(codeString))
          return GoalStatus.ACCEPTED;
        if ("planned".equals(codeString))
          return GoalStatus.PLANNED;
        if ("in-progress".equals(codeString))
          return GoalStatus.INPROGRESS;
        if ("on-target".equals(codeString))
          return GoalStatus.ONTARGET;
        if ("ahead-of-target".equals(codeString))
          return GoalStatus.AHEADOFTARGET;
        if ("behind-target".equals(codeString))
          return GoalStatus.BEHINDTARGET;
        if ("sustaining".equals(codeString))
          return GoalStatus.SUSTAINING;
        if ("achieved".equals(codeString))
          return GoalStatus.ACHIEVED;
        if ("on-hold".equals(codeString))
          return GoalStatus.ONHOLD;
        if ("cancelled".equals(codeString))
          return GoalStatus.CANCELLED;
        if ("entered-in-error".equals(codeString))
          return GoalStatus.ENTEREDINERROR;
        if ("rejected".equals(codeString))
          return GoalStatus.REJECTED;
        throw new IllegalArgumentException("Unknown GoalStatus code '"+codeString+"'");
        }
        public Enumeration<GoalStatus> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<GoalStatus>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("proposed".equals(codeString))
          return new Enumeration<GoalStatus>(this, GoalStatus.PROPOSED);
        if ("accepted".equals(codeString))
          return new Enumeration<GoalStatus>(this, GoalStatus.ACCEPTED);
        if ("planned".equals(codeString))
          return new Enumeration<GoalStatus>(this, GoalStatus.PLANNED);
        if ("in-progress".equals(codeString))
          return new Enumeration<GoalStatus>(this, GoalStatus.INPROGRESS);
        if ("on-target".equals(codeString))
          return new Enumeration<GoalStatus>(this, GoalStatus.ONTARGET);
        if ("ahead-of-target".equals(codeString))
          return new Enumeration<GoalStatus>(this, GoalStatus.AHEADOFTARGET);
        if ("behind-target".equals(codeString))
          return new Enumeration<GoalStatus>(this, GoalStatus.BEHINDTARGET);
        if ("sustaining".equals(codeString))
          return new Enumeration<GoalStatus>(this, GoalStatus.SUSTAINING);
        if ("achieved".equals(codeString))
          return new Enumeration<GoalStatus>(this, GoalStatus.ACHIEVED);
        if ("on-hold".equals(codeString))
          return new Enumeration<GoalStatus>(this, GoalStatus.ONHOLD);
        if ("cancelled".equals(codeString))
          return new Enumeration<GoalStatus>(this, GoalStatus.CANCELLED);
        if ("entered-in-error".equals(codeString))
          return new Enumeration<GoalStatus>(this, GoalStatus.ENTEREDINERROR);
        if ("rejected".equals(codeString))
          return new Enumeration<GoalStatus>(this, GoalStatus.REJECTED);
        throw new FHIRException("Unknown GoalStatus code '"+codeString+"'");
        }
    public String toCode(GoalStatus code) {
      if (code == GoalStatus.PROPOSED)
        return "proposed";
      if (code == GoalStatus.ACCEPTED)
        return "accepted";
      if (code == GoalStatus.PLANNED)
        return "planned";
      if (code == GoalStatus.INPROGRESS)
        return "in-progress";
      if (code == GoalStatus.ONTARGET)
        return "on-target";
      if (code == GoalStatus.AHEADOFTARGET)
        return "ahead-of-target";
      if (code == GoalStatus.BEHINDTARGET)
        return "behind-target";
      if (code == GoalStatus.SUSTAINING)
        return "sustaining";
      if (code == GoalStatus.ACHIEVED)
        return "achieved";
      if (code == GoalStatus.ONHOLD)
        return "on-hold";
      if (code == GoalStatus.CANCELLED)
        return "cancelled";
      if (code == GoalStatus.ENTEREDINERROR)
        return "entered-in-error";
      if (code == GoalStatus.REJECTED)
        return "rejected";
      return "?";
      }
    public String toSystem(GoalStatus code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class GoalTargetComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The parameter whose value is being tracked, e.g. body weight, blood pressure, or hemoglobin A1c level.
         */
        @Child(name = "measure", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The parameter whose value is being tracked", formalDefinition="The parameter whose value is being tracked, e.g. body weight, blood pressure, or hemoglobin A1c level." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/observation-codes")
        protected CodeableConcept measure;

        /**
         * The target value of the focus to be achieved to signify the fulfillment of the goal, e.g. 150 pounds, 7.0%. Either the high or low or both values of the range can be specified. When a low value is missing, it indicates that the goal is achieved at any focus value at or below the high value. Similarly, if the high value is missing, it indicates that the goal is achieved at any focus value at or above the low value.
         */
        @Child(name = "detail", type = {Quantity.class, Range.class, CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The target value to be achieved", formalDefinition="The target value of the focus to be achieved to signify the fulfillment of the goal, e.g. 150 pounds, 7.0%. Either the high or low or both values of the range can be specified. When a low value is missing, it indicates that the goal is achieved at any focus value at or below the high value. Similarly, if the high value is missing, it indicates that the goal is achieved at any focus value at or above the low value." )
        protected Type detail;

        /**
         * Indicates either the date or the duration after start by which the goal should be met.
         */
        @Child(name = "due", type = {DateType.class, Duration.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Reach goal on or before", formalDefinition="Indicates either the date or the duration after start by which the goal should be met." )
        protected Type due;

        private static final long serialVersionUID = -585108934L;

    /**
     * Constructor
     */
      public GoalTargetComponent() {
        super();
      }

        /**
         * @return {@link #measure} (The parameter whose value is being tracked, e.g. body weight, blood pressure, or hemoglobin A1c level.)
         */
        public CodeableConcept getMeasure() { 
          if (this.measure == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create GoalTargetComponent.measure");
            else if (Configuration.doAutoCreate())
              this.measure = new CodeableConcept(); // cc
          return this.measure;
        }

        public boolean hasMeasure() { 
          return this.measure != null && !this.measure.isEmpty();
        }

        /**
         * @param value {@link #measure} (The parameter whose value is being tracked, e.g. body weight, blood pressure, or hemoglobin A1c level.)
         */
        public GoalTargetComponent setMeasure(CodeableConcept value) { 
          this.measure = value;
          return this;
        }

        /**
         * @return {@link #detail} (The target value of the focus to be achieved to signify the fulfillment of the goal, e.g. 150 pounds, 7.0%. Either the high or low or both values of the range can be specified. When a low value is missing, it indicates that the goal is achieved at any focus value at or below the high value. Similarly, if the high value is missing, it indicates that the goal is achieved at any focus value at or above the low value.)
         */
        public Type getDetail() { 
          return this.detail;
        }

        /**
         * @return {@link #detail} (The target value of the focus to be achieved to signify the fulfillment of the goal, e.g. 150 pounds, 7.0%. Either the high or low or both values of the range can be specified. When a low value is missing, it indicates that the goal is achieved at any focus value at or below the high value. Similarly, if the high value is missing, it indicates that the goal is achieved at any focus value at or above the low value.)
         */
        public Quantity getDetailQuantity() throws FHIRException { 
          if (!(this.detail instanceof Quantity))
            throw new FHIRException("Type mismatch: the type Quantity was expected, but "+this.detail.getClass().getName()+" was encountered");
          return (Quantity) this.detail;
        }

        public boolean hasDetailQuantity() { 
          return this.detail instanceof Quantity;
        }

        /**
         * @return {@link #detail} (The target value of the focus to be achieved to signify the fulfillment of the goal, e.g. 150 pounds, 7.0%. Either the high or low or both values of the range can be specified. When a low value is missing, it indicates that the goal is achieved at any focus value at or below the high value. Similarly, if the high value is missing, it indicates that the goal is achieved at any focus value at or above the low value.)
         */
        public Range getDetailRange() throws FHIRException { 
          if (!(this.detail instanceof Range))
            throw new FHIRException("Type mismatch: the type Range was expected, but "+this.detail.getClass().getName()+" was encountered");
          return (Range) this.detail;
        }

        public boolean hasDetailRange() { 
          return this.detail instanceof Range;
        }

        /**
         * @return {@link #detail} (The target value of the focus to be achieved to signify the fulfillment of the goal, e.g. 150 pounds, 7.0%. Either the high or low or both values of the range can be specified. When a low value is missing, it indicates that the goal is achieved at any focus value at or below the high value. Similarly, if the high value is missing, it indicates that the goal is achieved at any focus value at or above the low value.)
         */
        public CodeableConcept getDetailCodeableConcept() throws FHIRException { 
          if (!(this.detail instanceof CodeableConcept))
            throw new FHIRException("Type mismatch: the type CodeableConcept was expected, but "+this.detail.getClass().getName()+" was encountered");
          return (CodeableConcept) this.detail;
        }

        public boolean hasDetailCodeableConcept() { 
          return this.detail instanceof CodeableConcept;
        }

        public boolean hasDetail() { 
          return this.detail != null && !this.detail.isEmpty();
        }

        /**
         * @param value {@link #detail} (The target value of the focus to be achieved to signify the fulfillment of the goal, e.g. 150 pounds, 7.0%. Either the high or low or both values of the range can be specified. When a low value is missing, it indicates that the goal is achieved at any focus value at or below the high value. Similarly, if the high value is missing, it indicates that the goal is achieved at any focus value at or above the low value.)
         */
        public GoalTargetComponent setDetail(Type value) { 
          this.detail = value;
          return this;
        }

        /**
         * @return {@link #due} (Indicates either the date or the duration after start by which the goal should be met.)
         */
        public Type getDue() { 
          return this.due;
        }

        /**
         * @return {@link #due} (Indicates either the date or the duration after start by which the goal should be met.)
         */
        public DateType getDueDateType() throws FHIRException { 
          if (!(this.due instanceof DateType))
            throw new FHIRException("Type mismatch: the type DateType was expected, but "+this.due.getClass().getName()+" was encountered");
          return (DateType) this.due;
        }

        public boolean hasDueDateType() { 
          return this.due instanceof DateType;
        }

        /**
         * @return {@link #due} (Indicates either the date or the duration after start by which the goal should be met.)
         */
        public Duration getDueDuration() throws FHIRException { 
          if (!(this.due instanceof Duration))
            throw new FHIRException("Type mismatch: the type Duration was expected, but "+this.due.getClass().getName()+" was encountered");
          return (Duration) this.due;
        }

        public boolean hasDueDuration() { 
          return this.due instanceof Duration;
        }

        public boolean hasDue() { 
          return this.due != null && !this.due.isEmpty();
        }

        /**
         * @param value {@link #due} (Indicates either the date or the duration after start by which the goal should be met.)
         */
        public GoalTargetComponent setDue(Type value) { 
          this.due = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("measure", "CodeableConcept", "The parameter whose value is being tracked, e.g. body weight, blood pressure, or hemoglobin A1c level.", 0, java.lang.Integer.MAX_VALUE, measure));
          childrenList.add(new Property("detail[x]", "Quantity|Range|CodeableConcept", "The target value of the focus to be achieved to signify the fulfillment of the goal, e.g. 150 pounds, 7.0%. Either the high or low or both values of the range can be specified. When a low value is missing, it indicates that the goal is achieved at any focus value at or below the high value. Similarly, if the high value is missing, it indicates that the goal is achieved at any focus value at or above the low value.", 0, java.lang.Integer.MAX_VALUE, detail));
          childrenList.add(new Property("due[x]", "date|Duration", "Indicates either the date or the duration after start by which the goal should be met.", 0, java.lang.Integer.MAX_VALUE, due));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 938321246: /*measure*/ return this.measure == null ? new Base[0] : new Base[] {this.measure}; // CodeableConcept
        case -1335224239: /*detail*/ return this.detail == null ? new Base[0] : new Base[] {this.detail}; // Type
        case 99828: /*due*/ return this.due == null ? new Base[0] : new Base[] {this.due}; // Type
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 938321246: // measure
          this.measure = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1335224239: // detail
          this.detail = castToType(value); // Type
          return value;
        case 99828: // due
          this.due = castToType(value); // Type
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("measure")) {
          this.measure = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("detail[x]")) {
          this.detail = castToType(value); // Type
        } else if (name.equals("due[x]")) {
          this.due = castToType(value); // Type
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 938321246:  return getMeasure(); 
        case -1973084529:  return getDetail(); 
        case -1335224239:  return getDetail(); 
        case -1320900084:  return getDue(); 
        case 99828:  return getDue(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 938321246: /*measure*/ return new String[] {"CodeableConcept"};
        case -1335224239: /*detail*/ return new String[] {"Quantity", "Range", "CodeableConcept"};
        case 99828: /*due*/ return new String[] {"date", "Duration"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("measure")) {
          this.measure = new CodeableConcept();
          return this.measure;
        }
        else if (name.equals("detailQuantity")) {
          this.detail = new Quantity();
          return this.detail;
        }
        else if (name.equals("detailRange")) {
          this.detail = new Range();
          return this.detail;
        }
        else if (name.equals("detailCodeableConcept")) {
          this.detail = new CodeableConcept();
          return this.detail;
        }
        else if (name.equals("dueDate")) {
          this.due = new DateType();
          return this.due;
        }
        else if (name.equals("dueDuration")) {
          this.due = new Duration();
          return this.due;
        }
        else
          return super.addChild(name);
      }

      public GoalTargetComponent copy() {
        GoalTargetComponent dst = new GoalTargetComponent();
        copyValues(dst);
        dst.measure = measure == null ? null : measure.copy();
        dst.detail = detail == null ? null : detail.copy();
        dst.due = due == null ? null : due.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof GoalTargetComponent))
          return false;
        GoalTargetComponent o = (GoalTargetComponent) other;
        return compareDeep(measure, o.measure, true) && compareDeep(detail, o.detail, true) && compareDeep(due, o.due, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof GoalTargetComponent))
          return false;
        GoalTargetComponent o = (GoalTargetComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(measure, detail, due);
      }

  public String fhirType() {
    return "Goal.target";

  }

  }

    /**
     * This records identifiers associated with this care plan that are defined by business processes and/or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation).
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="External Ids for this goal", formalDefinition="This records identifiers associated with this care plan that are defined by business processes and/or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)." )
    protected List<Identifier> identifier;

    /**
     * Indicates whether the goal has been reached and is still considered relevant.
     */
    @Child(name = "status", type = {CodeType.class}, order=1, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="proposed | accepted | planned | in-progress | on-target | ahead-of-target | behind-target | sustaining | achieved | on-hold | cancelled | entered-in-error | rejected", formalDefinition="Indicates whether the goal has been reached and is still considered relevant." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/goal-status")
    protected Enumeration<GoalStatus> status;

    /**
     * Indicates a category the goal falls within.
     */
    @Child(name = "category", type = {CodeableConcept.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="E.g. Treatment, dietary, behavioral, etc.", formalDefinition="Indicates a category the goal falls within." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/goal-category")
    protected List<CodeableConcept> category;

    /**
     * Identifies the mutually agreed level of importance associated with reaching/sustaining the goal.
     */
    @Child(name = "priority", type = {CodeableConcept.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="high-priority | medium-priority | low-priority", formalDefinition="Identifies the mutually agreed level of importance associated with reaching/sustaining the goal." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/goal-priority")
    protected CodeableConcept priority;

    /**
     * Human-readable and/or coded description of a specific desired objective of care, such as "control blood pressure" or "negotiate an obstacle course" or "dance with child at wedding".
     */
    @Child(name = "description", type = {CodeableConcept.class}, order=4, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Code or text describing goal", formalDefinition="Human-readable and/or coded description of a specific desired objective of care, such as \"control blood pressure\" or \"negotiate an obstacle course\" or \"dance with child at wedding\"." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/clinical-findings")
    protected CodeableConcept description;

    /**
     * Identifies the patient, group or organization for whom the goal is being established.
     */
    @Child(name = "subject", type = {Patient.class, Group.class, Organization.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Who this goal is intended for", formalDefinition="Identifies the patient, group or organization for whom the goal is being established." )
    protected Reference subject;

    /**
     * The actual object that is the target of the reference (Identifies the patient, group or organization for whom the goal is being established.)
     */
    protected Resource subjectTarget;

    /**
     * The date or event after which the goal should begin being pursued.
     */
    @Child(name = "start", type = {DateType.class, CodeableConcept.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When goal pursuit begins", formalDefinition="The date or event after which the goal should begin being pursued." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/goal-start-event")
    protected Type start;

    /**
     * Indicates what should be done by when.
     */
    @Child(name = "target", type = {}, order=7, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Target outcome for the goal", formalDefinition="Indicates what should be done by when." )
    protected GoalTargetComponent target;

    /**
     * Identifies when the current status.  I.e. When initially created, when achieved, when cancelled, etc.
     */
    @Child(name = "statusDate", type = {DateType.class}, order=8, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When goal status took effect", formalDefinition="Identifies when the current status.  I.e. When initially created, when achieved, when cancelled, etc." )
    protected DateType statusDate;

    /**
     * Captures the reason for the current status.
     */
    @Child(name = "statusReason", type = {StringType.class}, order=9, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Reason for current status", formalDefinition="Captures the reason for the current status." )
    protected StringType statusReason;

    /**
     * Indicates whose goal this is - patient goal, practitioner goal, etc.
     */
    @Child(name = "expressedBy", type = {Patient.class, Practitioner.class, RelatedPerson.class}, order=10, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Who's responsible for creating Goal?", formalDefinition="Indicates whose goal this is - patient goal, practitioner goal, etc." )
    protected Reference expressedBy;

    /**
     * The actual object that is the target of the reference (Indicates whose goal this is - patient goal, practitioner goal, etc.)
     */
    protected Resource expressedByTarget;

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
     * Identifies the change (or lack of change) at the point when the status of the goal is assessed.
     */
    @Child(name = "outcomeCode", type = {CodeableConcept.class}, order=13, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="What result was achieved regarding the goal?", formalDefinition="Identifies the change (or lack of change) at the point when the status of the goal is assessed." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/clinical-findings")
    protected List<CodeableConcept> outcomeCode;

    /**
     * Details of what's changed (or not changed).
     */
    @Child(name = "outcomeReference", type = {Observation.class}, order=14, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Observation that resulted from goal", formalDefinition="Details of what's changed (or not changed)." )
    protected List<Reference> outcomeReference;
    /**
     * The actual objects that are the target of the reference (Details of what's changed (or not changed).)
     */
    protected List<Observation> outcomeReferenceTarget;


    private static final long serialVersionUID = -1045412647L;

  /**
   * Constructor
   */
    public Goal() {
      super();
    }

  /**
   * Constructor
   */
    public Goal(Enumeration<GoalStatus> status, CodeableConcept description) {
      super();
      this.status = status;
      this.description = description;
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
    public Goal setIdentifier(List<Identifier> theIdentifier) { 
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

    public Goal addIdentifier(Identifier t) { //3
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
     * @return {@link #category} (Indicates a category the goal falls within.)
     */
    public List<CodeableConcept> getCategory() { 
      if (this.category == null)
        this.category = new ArrayList<CodeableConcept>();
      return this.category;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Goal setCategory(List<CodeableConcept> theCategory) { 
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

    public Goal addCategory(CodeableConcept t) { //3
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
     * @return {@link #description} (Human-readable and/or coded description of a specific desired objective of care, such as "control blood pressure" or "negotiate an obstacle course" or "dance with child at wedding".)
     */
    public CodeableConcept getDescription() { 
      if (this.description == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Goal.description");
        else if (Configuration.doAutoCreate())
          this.description = new CodeableConcept(); // cc
      return this.description;
    }

    public boolean hasDescription() { 
      return this.description != null && !this.description.isEmpty();
    }

    /**
     * @param value {@link #description} (Human-readable and/or coded description of a specific desired objective of care, such as "control blood pressure" or "negotiate an obstacle course" or "dance with child at wedding".)
     */
    public Goal setDescription(CodeableConcept value) { 
      this.description = value;
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
     * @return {@link #target} (Indicates what should be done by when.)
     */
    public GoalTargetComponent getTarget() { 
      if (this.target == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Goal.target");
        else if (Configuration.doAutoCreate())
          this.target = new GoalTargetComponent(); // cc
      return this.target;
    }

    public boolean hasTarget() { 
      return this.target != null && !this.target.isEmpty();
    }

    /**
     * @param value {@link #target} (Indicates what should be done by when.)
     */
    public Goal setTarget(GoalTargetComponent value) { 
      this.target = value;
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
     * @return {@link #statusReason} (Captures the reason for the current status.). This is the underlying object with id, value and extensions. The accessor "getStatusReason" gives direct access to the value
     */
    public StringType getStatusReasonElement() { 
      if (this.statusReason == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Goal.statusReason");
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
     * @param value {@link #statusReason} (Captures the reason for the current status.). This is the underlying object with id, value and extensions. The accessor "getStatusReason" gives direct access to the value
     */
    public Goal setStatusReasonElement(StringType value) { 
      this.statusReason = value;
      return this;
    }

    /**
     * @return Captures the reason for the current status.
     */
    public String getStatusReason() { 
      return this.statusReason == null ? null : this.statusReason.getValue();
    }

    /**
     * @param value Captures the reason for the current status.
     */
    public Goal setStatusReason(String value) { 
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
     * @return {@link #expressedBy} (Indicates whose goal this is - patient goal, practitioner goal, etc.)
     */
    public Reference getExpressedBy() { 
      if (this.expressedBy == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Goal.expressedBy");
        else if (Configuration.doAutoCreate())
          this.expressedBy = new Reference(); // cc
      return this.expressedBy;
    }

    public boolean hasExpressedBy() { 
      return this.expressedBy != null && !this.expressedBy.isEmpty();
    }

    /**
     * @param value {@link #expressedBy} (Indicates whose goal this is - patient goal, practitioner goal, etc.)
     */
    public Goal setExpressedBy(Reference value) { 
      this.expressedBy = value;
      return this;
    }

    /**
     * @return {@link #expressedBy} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Indicates whose goal this is - patient goal, practitioner goal, etc.)
     */
    public Resource getExpressedByTarget() { 
      return this.expressedByTarget;
    }

    /**
     * @param value {@link #expressedBy} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Indicates whose goal this is - patient goal, practitioner goal, etc.)
     */
    public Goal setExpressedByTarget(Resource value) { 
      this.expressedByTarget = value;
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

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Goal setAddresses(List<Reference> theAddresses) { 
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

    public Goal addAddresses(Reference t) { //3
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

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Goal setNote(List<Annotation> theNote) { 
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

    public Goal addNote(Annotation t) { //3
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

    /**
     * @return {@link #outcomeCode} (Identifies the change (or lack of change) at the point when the status of the goal is assessed.)
     */
    public List<CodeableConcept> getOutcomeCode() { 
      if (this.outcomeCode == null)
        this.outcomeCode = new ArrayList<CodeableConcept>();
      return this.outcomeCode;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Goal setOutcomeCode(List<CodeableConcept> theOutcomeCode) { 
      this.outcomeCode = theOutcomeCode;
      return this;
    }

    public boolean hasOutcomeCode() { 
      if (this.outcomeCode == null)
        return false;
      for (CodeableConcept item : this.outcomeCode)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addOutcomeCode() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.outcomeCode == null)
        this.outcomeCode = new ArrayList<CodeableConcept>();
      this.outcomeCode.add(t);
      return t;
    }

    public Goal addOutcomeCode(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.outcomeCode == null)
        this.outcomeCode = new ArrayList<CodeableConcept>();
      this.outcomeCode.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #outcomeCode}, creating it if it does not already exist
     */
    public CodeableConcept getOutcomeCodeFirstRep() { 
      if (getOutcomeCode().isEmpty()) {
        addOutcomeCode();
      }
      return getOutcomeCode().get(0);
    }

    /**
     * @return {@link #outcomeReference} (Details of what's changed (or not changed).)
     */
    public List<Reference> getOutcomeReference() { 
      if (this.outcomeReference == null)
        this.outcomeReference = new ArrayList<Reference>();
      return this.outcomeReference;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Goal setOutcomeReference(List<Reference> theOutcomeReference) { 
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

    public Goal addOutcomeReference(Reference t) { //3
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
    public List<Observation> getOutcomeReferenceTarget() { 
      if (this.outcomeReferenceTarget == null)
        this.outcomeReferenceTarget = new ArrayList<Observation>();
      return this.outcomeReferenceTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public Observation addOutcomeReferenceTarget() { 
      Observation r = new Observation();
      if (this.outcomeReferenceTarget == null)
        this.outcomeReferenceTarget = new ArrayList<Observation>();
      this.outcomeReferenceTarget.add(r);
      return r;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "This records identifiers associated with this care plan that are defined by business processes and/or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation).", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("status", "code", "Indicates whether the goal has been reached and is still considered relevant.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("category", "CodeableConcept", "Indicates a category the goal falls within.", 0, java.lang.Integer.MAX_VALUE, category));
        childrenList.add(new Property("priority", "CodeableConcept", "Identifies the mutually agreed level of importance associated with reaching/sustaining the goal.", 0, java.lang.Integer.MAX_VALUE, priority));
        childrenList.add(new Property("description", "CodeableConcept", "Human-readable and/or coded description of a specific desired objective of care, such as \"control blood pressure\" or \"negotiate an obstacle course\" or \"dance with child at wedding\".", 0, java.lang.Integer.MAX_VALUE, description));
        childrenList.add(new Property("subject", "Reference(Patient|Group|Organization)", "Identifies the patient, group or organization for whom the goal is being established.", 0, java.lang.Integer.MAX_VALUE, subject));
        childrenList.add(new Property("start[x]", "date|CodeableConcept", "The date or event after which the goal should begin being pursued.", 0, java.lang.Integer.MAX_VALUE, start));
        childrenList.add(new Property("target", "", "Indicates what should be done by when.", 0, java.lang.Integer.MAX_VALUE, target));
        childrenList.add(new Property("statusDate", "date", "Identifies when the current status.  I.e. When initially created, when achieved, when cancelled, etc.", 0, java.lang.Integer.MAX_VALUE, statusDate));
        childrenList.add(new Property("statusReason", "string", "Captures the reason for the current status.", 0, java.lang.Integer.MAX_VALUE, statusReason));
        childrenList.add(new Property("expressedBy", "Reference(Patient|Practitioner|RelatedPerson)", "Indicates whose goal this is - patient goal, practitioner goal, etc.", 0, java.lang.Integer.MAX_VALUE, expressedBy));
        childrenList.add(new Property("addresses", "Reference(Condition|Observation|MedicationStatement|NutritionOrder|ProcedureRequest|RiskAssessment)", "The identified conditions and other health record elements that are intended to be addressed by the goal.", 0, java.lang.Integer.MAX_VALUE, addresses));
        childrenList.add(new Property("note", "Annotation", "Any comments related to the goal.", 0, java.lang.Integer.MAX_VALUE, note));
        childrenList.add(new Property("outcomeCode", "CodeableConcept", "Identifies the change (or lack of change) at the point when the status of the goal is assessed.", 0, java.lang.Integer.MAX_VALUE, outcomeCode));
        childrenList.add(new Property("outcomeReference", "Reference(Observation)", "Details of what's changed (or not changed).", 0, java.lang.Integer.MAX_VALUE, outcomeReference));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<GoalStatus>
        case 50511102: /*category*/ return this.category == null ? new Base[0] : this.category.toArray(new Base[this.category.size()]); // CodeableConcept
        case -1165461084: /*priority*/ return this.priority == null ? new Base[0] : new Base[] {this.priority}; // CodeableConcept
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // CodeableConcept
        case -1867885268: /*subject*/ return this.subject == null ? new Base[0] : new Base[] {this.subject}; // Reference
        case 109757538: /*start*/ return this.start == null ? new Base[0] : new Base[] {this.start}; // Type
        case -880905839: /*target*/ return this.target == null ? new Base[0] : new Base[] {this.target}; // GoalTargetComponent
        case 247524032: /*statusDate*/ return this.statusDate == null ? new Base[0] : new Base[] {this.statusDate}; // DateType
        case 2051346646: /*statusReason*/ return this.statusReason == null ? new Base[0] : new Base[] {this.statusReason}; // StringType
        case 175423686: /*expressedBy*/ return this.expressedBy == null ? new Base[0] : new Base[] {this.expressedBy}; // Reference
        case 874544034: /*addresses*/ return this.addresses == null ? new Base[0] : this.addresses.toArray(new Base[this.addresses.size()]); // Reference
        case 3387378: /*note*/ return this.note == null ? new Base[0] : this.note.toArray(new Base[this.note.size()]); // Annotation
        case 1062482015: /*outcomeCode*/ return this.outcomeCode == null ? new Base[0] : this.outcomeCode.toArray(new Base[this.outcomeCode.size()]); // CodeableConcept
        case -782273511: /*outcomeReference*/ return this.outcomeReference == null ? new Base[0] : this.outcomeReference.toArray(new Base[this.outcomeReference.size()]); // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.getIdentifier().add(castToIdentifier(value)); // Identifier
          return value;
        case -892481550: // status
          value = new GoalStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<GoalStatus>
          return value;
        case 50511102: // category
          this.getCategory().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -1165461084: // priority
          this.priority = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1724546052: // description
          this.description = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1867885268: // subject
          this.subject = castToReference(value); // Reference
          return value;
        case 109757538: // start
          this.start = castToType(value); // Type
          return value;
        case -880905839: // target
          this.target = (GoalTargetComponent) value; // GoalTargetComponent
          return value;
        case 247524032: // statusDate
          this.statusDate = castToDate(value); // DateType
          return value;
        case 2051346646: // statusReason
          this.statusReason = castToString(value); // StringType
          return value;
        case 175423686: // expressedBy
          this.expressedBy = castToReference(value); // Reference
          return value;
        case 874544034: // addresses
          this.getAddresses().add(castToReference(value)); // Reference
          return value;
        case 3387378: // note
          this.getNote().add(castToAnnotation(value)); // Annotation
          return value;
        case 1062482015: // outcomeCode
          this.getOutcomeCode().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -782273511: // outcomeReference
          this.getOutcomeReference().add(castToReference(value)); // Reference
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier")) {
          this.getIdentifier().add(castToIdentifier(value));
        } else if (name.equals("status")) {
          value = new GoalStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<GoalStatus>
        } else if (name.equals("category")) {
          this.getCategory().add(castToCodeableConcept(value));
        } else if (name.equals("priority")) {
          this.priority = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("description")) {
          this.description = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("subject")) {
          this.subject = castToReference(value); // Reference
        } else if (name.equals("start[x]")) {
          this.start = castToType(value); // Type
        } else if (name.equals("target")) {
          this.target = (GoalTargetComponent) value; // GoalTargetComponent
        } else if (name.equals("statusDate")) {
          this.statusDate = castToDate(value); // DateType
        } else if (name.equals("statusReason")) {
          this.statusReason = castToString(value); // StringType
        } else if (name.equals("expressedBy")) {
          this.expressedBy = castToReference(value); // Reference
        } else if (name.equals("addresses")) {
          this.getAddresses().add(castToReference(value));
        } else if (name.equals("note")) {
          this.getNote().add(castToAnnotation(value));
        } else if (name.equals("outcomeCode")) {
          this.getOutcomeCode().add(castToCodeableConcept(value));
        } else if (name.equals("outcomeReference")) {
          this.getOutcomeReference().add(castToReference(value));
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return addIdentifier(); 
        case -892481550:  return getStatusElement();
        case 50511102:  return addCategory(); 
        case -1165461084:  return getPriority(); 
        case -1724546052:  return getDescription(); 
        case -1867885268:  return getSubject(); 
        case 1316793566:  return getStart(); 
        case 109757538:  return getStart(); 
        case -880905839:  return getTarget(); 
        case 247524032:  return getStatusDateElement();
        case 2051346646:  return getStatusReasonElement();
        case 175423686:  return getExpressedBy(); 
        case 874544034:  return addAddresses(); 
        case 3387378:  return addNote(); 
        case 1062482015:  return addOutcomeCode(); 
        case -782273511:  return addOutcomeReference(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case -892481550: /*status*/ return new String[] {"code"};
        case 50511102: /*category*/ return new String[] {"CodeableConcept"};
        case -1165461084: /*priority*/ return new String[] {"CodeableConcept"};
        case -1724546052: /*description*/ return new String[] {"CodeableConcept"};
        case -1867885268: /*subject*/ return new String[] {"Reference"};
        case 109757538: /*start*/ return new String[] {"date", "CodeableConcept"};
        case -880905839: /*target*/ return new String[] {};
        case 247524032: /*statusDate*/ return new String[] {"date"};
        case 2051346646: /*statusReason*/ return new String[] {"string"};
        case 175423686: /*expressedBy*/ return new String[] {"Reference"};
        case 874544034: /*addresses*/ return new String[] {"Reference"};
        case 3387378: /*note*/ return new String[] {"Annotation"};
        case 1062482015: /*outcomeCode*/ return new String[] {"CodeableConcept"};
        case -782273511: /*outcomeReference*/ return new String[] {"Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type Goal.status");
        }
        else if (name.equals("category")) {
          return addCategory();
        }
        else if (name.equals("priority")) {
          this.priority = new CodeableConcept();
          return this.priority;
        }
        else if (name.equals("description")) {
          this.description = new CodeableConcept();
          return this.description;
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
        else if (name.equals("target")) {
          this.target = new GoalTargetComponent();
          return this.target;
        }
        else if (name.equals("statusDate")) {
          throw new FHIRException("Cannot call addChild on a primitive type Goal.statusDate");
        }
        else if (name.equals("statusReason")) {
          throw new FHIRException("Cannot call addChild on a primitive type Goal.statusReason");
        }
        else if (name.equals("expressedBy")) {
          this.expressedBy = new Reference();
          return this.expressedBy;
        }
        else if (name.equals("addresses")) {
          return addAddresses();
        }
        else if (name.equals("note")) {
          return addNote();
        }
        else if (name.equals("outcomeCode")) {
          return addOutcomeCode();
        }
        else if (name.equals("outcomeReference")) {
          return addOutcomeReference();
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
        dst.status = status == null ? null : status.copy();
        if (category != null) {
          dst.category = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : category)
            dst.category.add(i.copy());
        };
        dst.priority = priority == null ? null : priority.copy();
        dst.description = description == null ? null : description.copy();
        dst.subject = subject == null ? null : subject.copy();
        dst.start = start == null ? null : start.copy();
        dst.target = target == null ? null : target.copy();
        dst.statusDate = statusDate == null ? null : statusDate.copy();
        dst.statusReason = statusReason == null ? null : statusReason.copy();
        dst.expressedBy = expressedBy == null ? null : expressedBy.copy();
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
        if (outcomeCode != null) {
          dst.outcomeCode = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : outcomeCode)
            dst.outcomeCode.add(i.copy());
        };
        if (outcomeReference != null) {
          dst.outcomeReference = new ArrayList<Reference>();
          for (Reference i : outcomeReference)
            dst.outcomeReference.add(i.copy());
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
        return compareDeep(identifier, o.identifier, true) && compareDeep(status, o.status, true) && compareDeep(category, o.category, true)
           && compareDeep(priority, o.priority, true) && compareDeep(description, o.description, true) && compareDeep(subject, o.subject, true)
           && compareDeep(start, o.start, true) && compareDeep(target, o.target, true) && compareDeep(statusDate, o.statusDate, true)
           && compareDeep(statusReason, o.statusReason, true) && compareDeep(expressedBy, o.expressedBy, true)
           && compareDeep(addresses, o.addresses, true) && compareDeep(note, o.note, true) && compareDeep(outcomeCode, o.outcomeCode, true)
           && compareDeep(outcomeReference, o.outcomeReference, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Goal))
          return false;
        Goal o = (Goal) other;
        return compareValues(status, o.status, true) && compareValues(statusDate, o.statusDate, true) && compareValues(statusReason, o.statusReason, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, status, category
          , priority, description, subject, start, target, statusDate, statusReason, expressedBy
          , addresses, note, outcomeCode, outcomeReference);
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
  @SearchParamDefinition(name="patient", path="Goal.subject", description="Who this goal is intended for", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient") }, target={Patient.class } )
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
  @SearchParamDefinition(name="subject", path="Goal.subject", description="Who this goal is intended for", type="reference", target={Group.class, Organization.class, Patient.class } )
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
   * Search parameter: <b>start-date</b>
   * <p>
   * Description: <b>When goal pursuit begins</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Goal.startDate</b><br>
   * </p>
   */
  @SearchParamDefinition(name="start-date", path="Goal.start.as(Date)", description="When goal pursuit begins", type="date" )
  public static final String SP_START_DATE = "start-date";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>start-date</b>
   * <p>
   * Description: <b>When goal pursuit begins</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Goal.startDate</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam START_DATE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_START_DATE);

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
   * Search parameter: <b>target-date</b>
   * <p>
   * Description: <b>Reach goal on or before</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Goal.target.dueDate</b><br>
   * </p>
   */
  @SearchParamDefinition(name="target-date", path="Goal.target.due.as(Date)", description="Reach goal on or before", type="date" )
  public static final String SP_TARGET_DATE = "target-date";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>target-date</b>
   * <p>
   * Description: <b>Reach goal on or before</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Goal.target.dueDate</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam TARGET_DATE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_TARGET_DATE);

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>proposed | accepted | planned | in-progress | on-target | ahead-of-target | behind-target | sustaining | achieved | on-hold | cancelled | entered-in-error | rejected</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Goal.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="Goal.status", description="proposed | accepted | planned | in-progress | on-target | ahead-of-target | behind-target | sustaining | achieved | on-hold | cancelled | entered-in-error | rejected", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>proposed | accepted | planned | in-progress | on-target | ahead-of-target | behind-target | sustaining | achieved | on-hold | cancelled | entered-in-error | rejected</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Goal.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);


}

