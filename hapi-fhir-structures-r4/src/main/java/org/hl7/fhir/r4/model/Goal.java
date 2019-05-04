package org.hl7.fhir.r4.model;

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

// Generated on Thu, Dec 27, 2018 10:06-0500 for FHIR v4.0.0

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
@ResourceDef(name="Goal", profile="http://hl7.org/fhir/StructureDefinition/Goal")
public class Goal extends DomainResource {

    public enum GoalLifecycleStatus {
        /**
         * A goal is proposed for this patient.
         */
        PROPOSED, 
        /**
         * A goal is planned for this patient.
         */
        PLANNED, 
        /**
         * A proposed goal was accepted or acknowledged.
         */
        ACCEPTED, 
        /**
         * The goal is being sought actively.
         */
        ACTIVE, 
        /**
         * The goal remains a long term objective but is no longer being actively pursued for a temporary period of time.
         */
        ONHOLD, 
        /**
         * The goal is no longer being sought.
         */
        COMPLETED, 
        /**
         * The goal has been abandoned.
         */
        CANCELLED, 
        /**
         * The goal was entered in error and voided.
         */
        ENTEREDINERROR, 
        /**
         * A proposed goal was rejected.
         */
        REJECTED, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static GoalLifecycleStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("proposed".equals(codeString))
          return PROPOSED;
        if ("planned".equals(codeString))
          return PLANNED;
        if ("accepted".equals(codeString))
          return ACCEPTED;
        if ("active".equals(codeString))
          return ACTIVE;
        if ("on-hold".equals(codeString))
          return ONHOLD;
        if ("completed".equals(codeString))
          return COMPLETED;
        if ("cancelled".equals(codeString))
          return CANCELLED;
        if ("entered-in-error".equals(codeString))
          return ENTEREDINERROR;
        if ("rejected".equals(codeString))
          return REJECTED;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown GoalLifecycleStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PROPOSED: return "proposed";
            case PLANNED: return "planned";
            case ACCEPTED: return "accepted";
            case ACTIVE: return "active";
            case ONHOLD: return "on-hold";
            case COMPLETED: return "completed";
            case CANCELLED: return "cancelled";
            case ENTEREDINERROR: return "entered-in-error";
            case REJECTED: return "rejected";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case PROPOSED: return "http://hl7.org/fhir/goal-status";
            case PLANNED: return "http://hl7.org/fhir/goal-status";
            case ACCEPTED: return "http://hl7.org/fhir/goal-status";
            case ACTIVE: return "http://hl7.org/fhir/goal-status";
            case ONHOLD: return "http://hl7.org/fhir/goal-status";
            case COMPLETED: return "http://hl7.org/fhir/goal-status";
            case CANCELLED: return "http://hl7.org/fhir/goal-status";
            case ENTEREDINERROR: return "http://hl7.org/fhir/goal-status";
            case REJECTED: return "http://hl7.org/fhir/goal-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case PROPOSED: return "A goal is proposed for this patient.";
            case PLANNED: return "A goal is planned for this patient.";
            case ACCEPTED: return "A proposed goal was accepted or acknowledged.";
            case ACTIVE: return "The goal is being sought actively.";
            case ONHOLD: return "The goal remains a long term objective but is no longer being actively pursued for a temporary period of time.";
            case COMPLETED: return "The goal is no longer being sought.";
            case CANCELLED: return "The goal has been abandoned.";
            case ENTEREDINERROR: return "The goal was entered in error and voided.";
            case REJECTED: return "A proposed goal was rejected.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PROPOSED: return "Proposed";
            case PLANNED: return "Planned";
            case ACCEPTED: return "Accepted";
            case ACTIVE: return "Active";
            case ONHOLD: return "On Hold";
            case COMPLETED: return "Completed";
            case CANCELLED: return "Cancelled";
            case ENTEREDINERROR: return "Entered in Error";
            case REJECTED: return "Rejected";
            default: return "?";
          }
        }
    }

  public static class GoalLifecycleStatusEnumFactory implements EnumFactory<GoalLifecycleStatus> {
    public GoalLifecycleStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("proposed".equals(codeString))
          return GoalLifecycleStatus.PROPOSED;
        if ("planned".equals(codeString))
          return GoalLifecycleStatus.PLANNED;
        if ("accepted".equals(codeString))
          return GoalLifecycleStatus.ACCEPTED;
        if ("active".equals(codeString))
          return GoalLifecycleStatus.ACTIVE;
        if ("on-hold".equals(codeString))
          return GoalLifecycleStatus.ONHOLD;
        if ("completed".equals(codeString))
          return GoalLifecycleStatus.COMPLETED;
        if ("cancelled".equals(codeString))
          return GoalLifecycleStatus.CANCELLED;
        if ("entered-in-error".equals(codeString))
          return GoalLifecycleStatus.ENTEREDINERROR;
        if ("rejected".equals(codeString))
          return GoalLifecycleStatus.REJECTED;
        throw new IllegalArgumentException("Unknown GoalLifecycleStatus code '"+codeString+"'");
        }
        public Enumeration<GoalLifecycleStatus> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<GoalLifecycleStatus>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("proposed".equals(codeString))
          return new Enumeration<GoalLifecycleStatus>(this, GoalLifecycleStatus.PROPOSED);
        if ("planned".equals(codeString))
          return new Enumeration<GoalLifecycleStatus>(this, GoalLifecycleStatus.PLANNED);
        if ("accepted".equals(codeString))
          return new Enumeration<GoalLifecycleStatus>(this, GoalLifecycleStatus.ACCEPTED);
        if ("active".equals(codeString))
          return new Enumeration<GoalLifecycleStatus>(this, GoalLifecycleStatus.ACTIVE);
        if ("on-hold".equals(codeString))
          return new Enumeration<GoalLifecycleStatus>(this, GoalLifecycleStatus.ONHOLD);
        if ("completed".equals(codeString))
          return new Enumeration<GoalLifecycleStatus>(this, GoalLifecycleStatus.COMPLETED);
        if ("cancelled".equals(codeString))
          return new Enumeration<GoalLifecycleStatus>(this, GoalLifecycleStatus.CANCELLED);
        if ("entered-in-error".equals(codeString))
          return new Enumeration<GoalLifecycleStatus>(this, GoalLifecycleStatus.ENTEREDINERROR);
        if ("rejected".equals(codeString))
          return new Enumeration<GoalLifecycleStatus>(this, GoalLifecycleStatus.REJECTED);
        throw new FHIRException("Unknown GoalLifecycleStatus code '"+codeString+"'");
        }
    public String toCode(GoalLifecycleStatus code) {
      if (code == GoalLifecycleStatus.PROPOSED)
        return "proposed";
      if (code == GoalLifecycleStatus.PLANNED)
        return "planned";
      if (code == GoalLifecycleStatus.ACCEPTED)
        return "accepted";
      if (code == GoalLifecycleStatus.ACTIVE)
        return "active";
      if (code == GoalLifecycleStatus.ONHOLD)
        return "on-hold";
      if (code == GoalLifecycleStatus.COMPLETED)
        return "completed";
      if (code == GoalLifecycleStatus.CANCELLED)
        return "cancelled";
      if (code == GoalLifecycleStatus.ENTEREDINERROR)
        return "entered-in-error";
      if (code == GoalLifecycleStatus.REJECTED)
        return "rejected";
      return "?";
      }
    public String toSystem(GoalLifecycleStatus code) {
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
        @Child(name = "detail", type = {Quantity.class, Range.class, CodeableConcept.class, StringType.class, BooleanType.class, IntegerType.class, Ratio.class}, order=2, min=0, max=1, modifier=false, summary=true)
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
          if (this.detail == null)
            this.detail = new Quantity();
          if (!(this.detail instanceof Quantity))
            throw new FHIRException("Type mismatch: the type Quantity was expected, but "+this.detail.getClass().getName()+" was encountered");
          return (Quantity) this.detail;
        }

        public boolean hasDetailQuantity() { 
          return this != null && this.detail instanceof Quantity;
        }

        /**
         * @return {@link #detail} (The target value of the focus to be achieved to signify the fulfillment of the goal, e.g. 150 pounds, 7.0%. Either the high or low or both values of the range can be specified. When a low value is missing, it indicates that the goal is achieved at any focus value at or below the high value. Similarly, if the high value is missing, it indicates that the goal is achieved at any focus value at or above the low value.)
         */
        public Range getDetailRange() throws FHIRException { 
          if (this.detail == null)
            this.detail = new Range();
          if (!(this.detail instanceof Range))
            throw new FHIRException("Type mismatch: the type Range was expected, but "+this.detail.getClass().getName()+" was encountered");
          return (Range) this.detail;
        }

        public boolean hasDetailRange() { 
          return this != null && this.detail instanceof Range;
        }

        /**
         * @return {@link #detail} (The target value of the focus to be achieved to signify the fulfillment of the goal, e.g. 150 pounds, 7.0%. Either the high or low or both values of the range can be specified. When a low value is missing, it indicates that the goal is achieved at any focus value at or below the high value. Similarly, if the high value is missing, it indicates that the goal is achieved at any focus value at or above the low value.)
         */
        public CodeableConcept getDetailCodeableConcept() throws FHIRException { 
          if (this.detail == null)
            this.detail = new CodeableConcept();
          if (!(this.detail instanceof CodeableConcept))
            throw new FHIRException("Type mismatch: the type CodeableConcept was expected, but "+this.detail.getClass().getName()+" was encountered");
          return (CodeableConcept) this.detail;
        }

        public boolean hasDetailCodeableConcept() { 
          return this != null && this.detail instanceof CodeableConcept;
        }

        /**
         * @return {@link #detail} (The target value of the focus to be achieved to signify the fulfillment of the goal, e.g. 150 pounds, 7.0%. Either the high or low or both values of the range can be specified. When a low value is missing, it indicates that the goal is achieved at any focus value at or below the high value. Similarly, if the high value is missing, it indicates that the goal is achieved at any focus value at or above the low value.)
         */
        public StringType getDetailStringType() throws FHIRException { 
          if (this.detail == null)
            this.detail = new StringType();
          if (!(this.detail instanceof StringType))
            throw new FHIRException("Type mismatch: the type StringType was expected, but "+this.detail.getClass().getName()+" was encountered");
          return (StringType) this.detail;
        }

        public boolean hasDetailStringType() { 
          return this != null && this.detail instanceof StringType;
        }

        /**
         * @return {@link #detail} (The target value of the focus to be achieved to signify the fulfillment of the goal, e.g. 150 pounds, 7.0%. Either the high or low or both values of the range can be specified. When a low value is missing, it indicates that the goal is achieved at any focus value at or below the high value. Similarly, if the high value is missing, it indicates that the goal is achieved at any focus value at or above the low value.)
         */
        public BooleanType getDetailBooleanType() throws FHIRException { 
          if (this.detail == null)
            this.detail = new BooleanType();
          if (!(this.detail instanceof BooleanType))
            throw new FHIRException("Type mismatch: the type BooleanType was expected, but "+this.detail.getClass().getName()+" was encountered");
          return (BooleanType) this.detail;
        }

        public boolean hasDetailBooleanType() { 
          return this != null && this.detail instanceof BooleanType;
        }

        /**
         * @return {@link #detail} (The target value of the focus to be achieved to signify the fulfillment of the goal, e.g. 150 pounds, 7.0%. Either the high or low or both values of the range can be specified. When a low value is missing, it indicates that the goal is achieved at any focus value at or below the high value. Similarly, if the high value is missing, it indicates that the goal is achieved at any focus value at or above the low value.)
         */
        public IntegerType getDetailIntegerType() throws FHIRException { 
          if (this.detail == null)
            this.detail = new IntegerType();
          if (!(this.detail instanceof IntegerType))
            throw new FHIRException("Type mismatch: the type IntegerType was expected, but "+this.detail.getClass().getName()+" was encountered");
          return (IntegerType) this.detail;
        }

        public boolean hasDetailIntegerType() { 
          return this != null && this.detail instanceof IntegerType;
        }

        /**
         * @return {@link #detail} (The target value of the focus to be achieved to signify the fulfillment of the goal, e.g. 150 pounds, 7.0%. Either the high or low or both values of the range can be specified. When a low value is missing, it indicates that the goal is achieved at any focus value at or below the high value. Similarly, if the high value is missing, it indicates that the goal is achieved at any focus value at or above the low value.)
         */
        public Ratio getDetailRatio() throws FHIRException { 
          if (this.detail == null)
            this.detail = new Ratio();
          if (!(this.detail instanceof Ratio))
            throw new FHIRException("Type mismatch: the type Ratio was expected, but "+this.detail.getClass().getName()+" was encountered");
          return (Ratio) this.detail;
        }

        public boolean hasDetailRatio() { 
          return this != null && this.detail instanceof Ratio;
        }

        public boolean hasDetail() { 
          return this.detail != null && !this.detail.isEmpty();
        }

        /**
         * @param value {@link #detail} (The target value of the focus to be achieved to signify the fulfillment of the goal, e.g. 150 pounds, 7.0%. Either the high or low or both values of the range can be specified. When a low value is missing, it indicates that the goal is achieved at any focus value at or below the high value. Similarly, if the high value is missing, it indicates that the goal is achieved at any focus value at or above the low value.)
         */
        public GoalTargetComponent setDetail(Type value) { 
          if (value != null && !(value instanceof Quantity || value instanceof Range || value instanceof CodeableConcept || value instanceof StringType || value instanceof BooleanType || value instanceof IntegerType || value instanceof Ratio))
            throw new Error("Not the right type for Goal.target.detail[x]: "+value.fhirType());
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
          if (this.due == null)
            this.due = new DateType();
          if (!(this.due instanceof DateType))
            throw new FHIRException("Type mismatch: the type DateType was expected, but "+this.due.getClass().getName()+" was encountered");
          return (DateType) this.due;
        }

        public boolean hasDueDateType() { 
          return this != null && this.due instanceof DateType;
        }

        /**
         * @return {@link #due} (Indicates either the date or the duration after start by which the goal should be met.)
         */
        public Duration getDueDuration() throws FHIRException { 
          if (this.due == null)
            this.due = new Duration();
          if (!(this.due instanceof Duration))
            throw new FHIRException("Type mismatch: the type Duration was expected, but "+this.due.getClass().getName()+" was encountered");
          return (Duration) this.due;
        }

        public boolean hasDueDuration() { 
          return this != null && this.due instanceof Duration;
        }

        public boolean hasDue() { 
          return this.due != null && !this.due.isEmpty();
        }

        /**
         * @param value {@link #due} (Indicates either the date or the duration after start by which the goal should be met.)
         */
        public GoalTargetComponent setDue(Type value) { 
          if (value != null && !(value instanceof DateType || value instanceof Duration))
            throw new Error("Not the right type for Goal.target.due[x]: "+value.fhirType());
          this.due = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("measure", "CodeableConcept", "The parameter whose value is being tracked, e.g. body weight, blood pressure, or hemoglobin A1c level.", 0, 1, measure));
          children.add(new Property("detail[x]", "Quantity|Range|CodeableConcept|string|boolean|integer|Ratio", "The target value of the focus to be achieved to signify the fulfillment of the goal, e.g. 150 pounds, 7.0%. Either the high or low or both values of the range can be specified. When a low value is missing, it indicates that the goal is achieved at any focus value at or below the high value. Similarly, if the high value is missing, it indicates that the goal is achieved at any focus value at or above the low value.", 0, 1, detail));
          children.add(new Property("due[x]", "date|Duration", "Indicates either the date or the duration after start by which the goal should be met.", 0, 1, due));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 938321246: /*measure*/  return new Property("measure", "CodeableConcept", "The parameter whose value is being tracked, e.g. body weight, blood pressure, or hemoglobin A1c level.", 0, 1, measure);
          case -1973084529: /*detail[x]*/  return new Property("detail[x]", "Quantity|Range|CodeableConcept|string|boolean|integer|Ratio", "The target value of the focus to be achieved to signify the fulfillment of the goal, e.g. 150 pounds, 7.0%. Either the high or low or both values of the range can be specified. When a low value is missing, it indicates that the goal is achieved at any focus value at or below the high value. Similarly, if the high value is missing, it indicates that the goal is achieved at any focus value at or above the low value.", 0, 1, detail);
          case -1335224239: /*detail*/  return new Property("detail[x]", "Quantity|Range|CodeableConcept|string|boolean|integer|Ratio", "The target value of the focus to be achieved to signify the fulfillment of the goal, e.g. 150 pounds, 7.0%. Either the high or low or both values of the range can be specified. When a low value is missing, it indicates that the goal is achieved at any focus value at or below the high value. Similarly, if the high value is missing, it indicates that the goal is achieved at any focus value at or above the low value.", 0, 1, detail);
          case -1313079300: /*detailQuantity*/  return new Property("detail[x]", "Quantity|Range|CodeableConcept|string|boolean|integer|Ratio", "The target value of the focus to be achieved to signify the fulfillment of the goal, e.g. 150 pounds, 7.0%. Either the high or low or both values of the range can be specified. When a low value is missing, it indicates that the goal is achieved at any focus value at or below the high value. Similarly, if the high value is missing, it indicates that the goal is achieved at any focus value at or above the low value.", 0, 1, detail);
          case -2062632084: /*detailRange*/  return new Property("detail[x]", "Quantity|Range|CodeableConcept|string|boolean|integer|Ratio", "The target value of the focus to be achieved to signify the fulfillment of the goal, e.g. 150 pounds, 7.0%. Either the high or low or both values of the range can be specified. When a low value is missing, it indicates that the goal is achieved at any focus value at or below the high value. Similarly, if the high value is missing, it indicates that the goal is achieved at any focus value at or above the low value.", 0, 1, detail);
          case -175586544: /*detailCodeableConcept*/  return new Property("detail[x]", "Quantity|Range|CodeableConcept|string|boolean|integer|Ratio", "The target value of the focus to be achieved to signify the fulfillment of the goal, e.g. 150 pounds, 7.0%. Either the high or low or both values of the range can be specified. When a low value is missing, it indicates that the goal is achieved at any focus value at or below the high value. Similarly, if the high value is missing, it indicates that the goal is achieved at any focus value at or above the low value.", 0, 1, detail);
          case 529212354: /*detailString*/  return new Property("detail[x]", "Quantity|Range|CodeableConcept|string|boolean|integer|Ratio", "The target value of the focus to be achieved to signify the fulfillment of the goal, e.g. 150 pounds, 7.0%. Either the high or low or both values of the range can be specified. When a low value is missing, it indicates that the goal is achieved at any focus value at or below the high value. Similarly, if the high value is missing, it indicates that the goal is achieved at any focus value at or above the low value.", 0, 1, detail);
          case 1172184727: /*detailBoolean*/  return new Property("detail[x]", "Quantity|Range|CodeableConcept|string|boolean|integer|Ratio", "The target value of the focus to be achieved to signify the fulfillment of the goal, e.g. 150 pounds, 7.0%. Either the high or low or both values of the range can be specified. When a low value is missing, it indicates that the goal is achieved at any focus value at or below the high value. Similarly, if the high value is missing, it indicates that the goal is achieved at any focus value at or above the low value.", 0, 1, detail);
          case -1229442131: /*detailInteger*/  return new Property("detail[x]", "Quantity|Range|CodeableConcept|string|boolean|integer|Ratio", "The target value of the focus to be achieved to signify the fulfillment of the goal, e.g. 150 pounds, 7.0%. Either the high or low or both values of the range can be specified. When a low value is missing, it indicates that the goal is achieved at any focus value at or below the high value. Similarly, if the high value is missing, it indicates that the goal is achieved at any focus value at or above the low value.", 0, 1, detail);
          case -2062626246: /*detailRatio*/  return new Property("detail[x]", "Quantity|Range|CodeableConcept|string|boolean|integer|Ratio", "The target value of the focus to be achieved to signify the fulfillment of the goal, e.g. 150 pounds, 7.0%. Either the high or low or both values of the range can be specified. When a low value is missing, it indicates that the goal is achieved at any focus value at or below the high value. Similarly, if the high value is missing, it indicates that the goal is achieved at any focus value at or above the low value.", 0, 1, detail);
          case -1320900084: /*due[x]*/  return new Property("due[x]", "date|Duration", "Indicates either the date or the duration after start by which the goal should be met.", 0, 1, due);
          case 99828: /*due*/  return new Property("due[x]", "date|Duration", "Indicates either the date or the duration after start by which the goal should be met.", 0, 1, due);
          case 2001063874: /*dueDate*/  return new Property("due[x]", "date|Duration", "Indicates either the date or the duration after start by which the goal should be met.", 0, 1, due);
          case -620428376: /*dueDuration*/  return new Property("due[x]", "date|Duration", "Indicates either the date or the duration after start by which the goal should be met.", 0, 1, due);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

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
        case -1335224239: /*detail*/ return new String[] {"Quantity", "Range", "CodeableConcept", "string", "boolean", "integer", "Ratio"};
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
        else if (name.equals("detailString")) {
          this.detail = new StringType();
          return this.detail;
        }
        else if (name.equals("detailBoolean")) {
          this.detail = new BooleanType();
          return this.detail;
        }
        else if (name.equals("detailInteger")) {
          this.detail = new IntegerType();
          return this.detail;
        }
        else if (name.equals("detailRatio")) {
          this.detail = new Ratio();
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
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof GoalTargetComponent))
          return false;
        GoalTargetComponent o = (GoalTargetComponent) other_;
        return compareDeep(measure, o.measure, true) && compareDeep(detail, o.detail, true) && compareDeep(due, o.due, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof GoalTargetComponent))
          return false;
        GoalTargetComponent o = (GoalTargetComponent) other_;
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
     * Business identifiers assigned to this goal by the performer or other systems which remain constant as the resource is updated and propagates from server to server.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="External Ids for this goal", formalDefinition="Business identifiers assigned to this goal by the performer or other systems which remain constant as the resource is updated and propagates from server to server." )
    protected List<Identifier> identifier;

    /**
     * The state of the goal throughout its lifecycle.
     */
    @Child(name = "lifecycleStatus", type = {CodeType.class}, order=1, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="proposed | planned | accepted | active | on-hold | completed | cancelled | entered-in-error | rejected", formalDefinition="The state of the goal throughout its lifecycle." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/goal-status")
    protected Enumeration<GoalLifecycleStatus> lifecycleStatus;

    /**
     * Describes the progression, or lack thereof, towards the goal against the target.
     */
    @Child(name = "achievementStatus", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="in-progress | improving | worsening | no-change | achieved | sustaining | not-achieved | no-progress | not-attainable", formalDefinition="Describes the progression, or lack thereof, towards the goal against the target." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/goal-achievement")
    protected CodeableConcept achievementStatus;

    /**
     * Indicates a category the goal falls within.
     */
    @Child(name = "category", type = {CodeableConcept.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="E.g. Treatment, dietary, behavioral, etc.", formalDefinition="Indicates a category the goal falls within." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/goal-category")
    protected List<CodeableConcept> category;

    /**
     * Identifies the mutually agreed level of importance associated with reaching/sustaining the goal.
     */
    @Child(name = "priority", type = {CodeableConcept.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="high-priority | medium-priority | low-priority", formalDefinition="Identifies the mutually agreed level of importance associated with reaching/sustaining the goal." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/goal-priority")
    protected CodeableConcept priority;

    /**
     * Human-readable and/or coded description of a specific desired objective of care, such as "control blood pressure" or "negotiate an obstacle course" or "dance with child at wedding".
     */
    @Child(name = "description", type = {CodeableConcept.class}, order=5, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Code or text describing goal", formalDefinition="Human-readable and/or coded description of a specific desired objective of care, such as \"control blood pressure\" or \"negotiate an obstacle course\" or \"dance with child at wedding\"." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/clinical-findings")
    protected CodeableConcept description;

    /**
     * Identifies the patient, group or organization for whom the goal is being established.
     */
    @Child(name = "subject", type = {Patient.class, Group.class, Organization.class}, order=6, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Who this goal is intended for", formalDefinition="Identifies the patient, group or organization for whom the goal is being established." )
    protected Reference subject;

    /**
     * The actual object that is the target of the reference (Identifies the patient, group or organization for whom the goal is being established.)
     */
    protected Resource subjectTarget;

    /**
     * The date or event after which the goal should begin being pursued.
     */
    @Child(name = "start", type = {DateType.class, CodeableConcept.class}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When goal pursuit begins", formalDefinition="The date or event after which the goal should begin being pursued." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/goal-start-event")
    protected Type start;

    /**
     * Indicates what should be done by when.
     */
    @Child(name = "target", type = {}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Target outcome for the goal", formalDefinition="Indicates what should be done by when." )
    protected List<GoalTargetComponent> target;

    /**
     * Identifies when the current status.  I.e. When initially created, when achieved, when cancelled, etc.
     */
    @Child(name = "statusDate", type = {DateType.class}, order=9, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When goal status took effect", formalDefinition="Identifies when the current status.  I.e. When initially created, when achieved, when cancelled, etc." )
    protected DateType statusDate;

    /**
     * Captures the reason for the current status.
     */
    @Child(name = "statusReason", type = {StringType.class}, order=10, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Reason for current status", formalDefinition="Captures the reason for the current status." )
    protected StringType statusReason;

    /**
     * Indicates whose goal this is - patient goal, practitioner goal, etc.
     */
    @Child(name = "expressedBy", type = {Patient.class, Practitioner.class, PractitionerRole.class, RelatedPerson.class}, order=11, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Who's responsible for creating Goal?", formalDefinition="Indicates whose goal this is - patient goal, practitioner goal, etc." )
    protected Reference expressedBy;

    /**
     * The actual object that is the target of the reference (Indicates whose goal this is - patient goal, practitioner goal, etc.)
     */
    protected Resource expressedByTarget;

    /**
     * The identified conditions and other health record elements that are intended to be addressed by the goal.
     */
    @Child(name = "addresses", type = {Condition.class, Observation.class, MedicationStatement.class, NutritionOrder.class, ServiceRequest.class, RiskAssessment.class}, order=12, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Issues addressed by this goal", formalDefinition="The identified conditions and other health record elements that are intended to be addressed by the goal." )
    protected List<Reference> addresses;
    /**
     * The actual objects that are the target of the reference (The identified conditions and other health record elements that are intended to be addressed by the goal.)
     */
    protected List<Resource> addressesTarget;


    /**
     * Any comments related to the goal.
     */
    @Child(name = "note", type = {Annotation.class}, order=13, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Comments about the goal", formalDefinition="Any comments related to the goal." )
    protected List<Annotation> note;

    /**
     * Identifies the change (or lack of change) at the point when the status of the goal is assessed.
     */
    @Child(name = "outcomeCode", type = {CodeableConcept.class}, order=14, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="What result was achieved regarding the goal?", formalDefinition="Identifies the change (or lack of change) at the point when the status of the goal is assessed." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/clinical-findings")
    protected List<CodeableConcept> outcomeCode;

    /**
     * Details of what's changed (or not changed).
     */
    @Child(name = "outcomeReference", type = {Observation.class}, order=15, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Observation that resulted from goal", formalDefinition="Details of what's changed (or not changed)." )
    protected List<Reference> outcomeReference;
    /**
     * The actual objects that are the target of the reference (Details of what's changed (or not changed).)
     */
    protected List<Observation> outcomeReferenceTarget;


    private static final long serialVersionUID = -1366854797L;

  /**
   * Constructor
   */
    public Goal() {
      super();
    }

  /**
   * Constructor
   */
    public Goal(Enumeration<GoalLifecycleStatus> lifecycleStatus, CodeableConcept description, Reference subject) {
      super();
      this.lifecycleStatus = lifecycleStatus;
      this.description = description;
      this.subject = subject;
    }

    /**
     * @return {@link #identifier} (Business identifiers assigned to this goal by the performer or other systems which remain constant as the resource is updated and propagates from server to server.)
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
     * @return {@link #lifecycleStatus} (The state of the goal throughout its lifecycle.). This is the underlying object with id, value and extensions. The accessor "getLifecycleStatus" gives direct access to the value
     */
    public Enumeration<GoalLifecycleStatus> getLifecycleStatusElement() { 
      if (this.lifecycleStatus == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Goal.lifecycleStatus");
        else if (Configuration.doAutoCreate())
          this.lifecycleStatus = new Enumeration<GoalLifecycleStatus>(new GoalLifecycleStatusEnumFactory()); // bb
      return this.lifecycleStatus;
    }

    public boolean hasLifecycleStatusElement() { 
      return this.lifecycleStatus != null && !this.lifecycleStatus.isEmpty();
    }

    public boolean hasLifecycleStatus() { 
      return this.lifecycleStatus != null && !this.lifecycleStatus.isEmpty();
    }

    /**
     * @param value {@link #lifecycleStatus} (The state of the goal throughout its lifecycle.). This is the underlying object with id, value and extensions. The accessor "getLifecycleStatus" gives direct access to the value
     */
    public Goal setLifecycleStatusElement(Enumeration<GoalLifecycleStatus> value) { 
      this.lifecycleStatus = value;
      return this;
    }

    /**
     * @return The state of the goal throughout its lifecycle.
     */
    public GoalLifecycleStatus getLifecycleStatus() { 
      return this.lifecycleStatus == null ? null : this.lifecycleStatus.getValue();
    }

    /**
     * @param value The state of the goal throughout its lifecycle.
     */
    public Goal setLifecycleStatus(GoalLifecycleStatus value) { 
        if (this.lifecycleStatus == null)
          this.lifecycleStatus = new Enumeration<GoalLifecycleStatus>(new GoalLifecycleStatusEnumFactory());
        this.lifecycleStatus.setValue(value);
      return this;
    }

    /**
     * @return {@link #achievementStatus} (Describes the progression, or lack thereof, towards the goal against the target.)
     */
    public CodeableConcept getAchievementStatus() { 
      if (this.achievementStatus == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Goal.achievementStatus");
        else if (Configuration.doAutoCreate())
          this.achievementStatus = new CodeableConcept(); // cc
      return this.achievementStatus;
    }

    public boolean hasAchievementStatus() { 
      return this.achievementStatus != null && !this.achievementStatus.isEmpty();
    }

    /**
     * @param value {@link #achievementStatus} (Describes the progression, or lack thereof, towards the goal against the target.)
     */
    public Goal setAchievementStatus(CodeableConcept value) { 
      this.achievementStatus = value;
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
      if (this.start == null)
        this.start = new DateType();
      if (!(this.start instanceof DateType))
        throw new FHIRException("Type mismatch: the type DateType was expected, but "+this.start.getClass().getName()+" was encountered");
      return (DateType) this.start;
    }

    public boolean hasStartDateType() { 
      return this != null && this.start instanceof DateType;
    }

    /**
     * @return {@link #start} (The date or event after which the goal should begin being pursued.)
     */
    public CodeableConcept getStartCodeableConcept() throws FHIRException { 
      if (this.start == null)
        this.start = new CodeableConcept();
      if (!(this.start instanceof CodeableConcept))
        throw new FHIRException("Type mismatch: the type CodeableConcept was expected, but "+this.start.getClass().getName()+" was encountered");
      return (CodeableConcept) this.start;
    }

    public boolean hasStartCodeableConcept() { 
      return this != null && this.start instanceof CodeableConcept;
    }

    public boolean hasStart() { 
      return this.start != null && !this.start.isEmpty();
    }

    /**
     * @param value {@link #start} (The date or event after which the goal should begin being pursued.)
     */
    public Goal setStart(Type value) { 
      if (value != null && !(value instanceof DateType || value instanceof CodeableConcept))
        throw new Error("Not the right type for Goal.start[x]: "+value.fhirType());
      this.start = value;
      return this;
    }

    /**
     * @return {@link #target} (Indicates what should be done by when.)
     */
    public List<GoalTargetComponent> getTarget() { 
      if (this.target == null)
        this.target = new ArrayList<GoalTargetComponent>();
      return this.target;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Goal setTarget(List<GoalTargetComponent> theTarget) { 
      this.target = theTarget;
      return this;
    }

    public boolean hasTarget() { 
      if (this.target == null)
        return false;
      for (GoalTargetComponent item : this.target)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public GoalTargetComponent addTarget() { //3
      GoalTargetComponent t = new GoalTargetComponent();
      if (this.target == null)
        this.target = new ArrayList<GoalTargetComponent>();
      this.target.add(t);
      return t;
    }

    public Goal addTarget(GoalTargetComponent t) { //3
      if (t == null)
        return this;
      if (this.target == null)
        this.target = new ArrayList<GoalTargetComponent>();
      this.target.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #target}, creating it if it does not already exist
     */
    public GoalTargetComponent getTargetFirstRep() { 
      if (getTarget().isEmpty()) {
        addTarget();
      }
      return getTarget().get(0);
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

      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("identifier", "Identifier", "Business identifiers assigned to this goal by the performer or other systems which remain constant as the resource is updated and propagates from server to server.", 0, java.lang.Integer.MAX_VALUE, identifier));
        children.add(new Property("lifecycleStatus", "code", "The state of the goal throughout its lifecycle.", 0, 1, lifecycleStatus));
        children.add(new Property("achievementStatus", "CodeableConcept", "Describes the progression, or lack thereof, towards the goal against the target.", 0, 1, achievementStatus));
        children.add(new Property("category", "CodeableConcept", "Indicates a category the goal falls within.", 0, java.lang.Integer.MAX_VALUE, category));
        children.add(new Property("priority", "CodeableConcept", "Identifies the mutually agreed level of importance associated with reaching/sustaining the goal.", 0, 1, priority));
        children.add(new Property("description", "CodeableConcept", "Human-readable and/or coded description of a specific desired objective of care, such as \"control blood pressure\" or \"negotiate an obstacle course\" or \"dance with child at wedding\".", 0, 1, description));
        children.add(new Property("subject", "Reference(Patient|Group|Organization)", "Identifies the patient, group or organization for whom the goal is being established.", 0, 1, subject));
        children.add(new Property("start[x]", "date|CodeableConcept", "The date or event after which the goal should begin being pursued.", 0, 1, start));
        children.add(new Property("target", "", "Indicates what should be done by when.", 0, java.lang.Integer.MAX_VALUE, target));
        children.add(new Property("statusDate", "date", "Identifies when the current status.  I.e. When initially created, when achieved, when cancelled, etc.", 0, 1, statusDate));
        children.add(new Property("statusReason", "string", "Captures the reason for the current status.", 0, 1, statusReason));
        children.add(new Property("expressedBy", "Reference(Patient|Practitioner|PractitionerRole|RelatedPerson)", "Indicates whose goal this is - patient goal, practitioner goal, etc.", 0, 1, expressedBy));
        children.add(new Property("addresses", "Reference(Condition|Observation|MedicationStatement|NutritionOrder|ServiceRequest|RiskAssessment)", "The identified conditions and other health record elements that are intended to be addressed by the goal.", 0, java.lang.Integer.MAX_VALUE, addresses));
        children.add(new Property("note", "Annotation", "Any comments related to the goal.", 0, java.lang.Integer.MAX_VALUE, note));
        children.add(new Property("outcomeCode", "CodeableConcept", "Identifies the change (or lack of change) at the point when the status of the goal is assessed.", 0, java.lang.Integer.MAX_VALUE, outcomeCode));
        children.add(new Property("outcomeReference", "Reference(Observation)", "Details of what's changed (or not changed).", 0, java.lang.Integer.MAX_VALUE, outcomeReference));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case -1618432855: /*identifier*/  return new Property("identifier", "Identifier", "Business identifiers assigned to this goal by the performer or other systems which remain constant as the resource is updated and propagates from server to server.", 0, java.lang.Integer.MAX_VALUE, identifier);
        case 1165552636: /*lifecycleStatus*/  return new Property("lifecycleStatus", "code", "The state of the goal throughout its lifecycle.", 0, 1, lifecycleStatus);
        case 104524801: /*achievementStatus*/  return new Property("achievementStatus", "CodeableConcept", "Describes the progression, or lack thereof, towards the goal against the target.", 0, 1, achievementStatus);
        case 50511102: /*category*/  return new Property("category", "CodeableConcept", "Indicates a category the goal falls within.", 0, java.lang.Integer.MAX_VALUE, category);
        case -1165461084: /*priority*/  return new Property("priority", "CodeableConcept", "Identifies the mutually agreed level of importance associated with reaching/sustaining the goal.", 0, 1, priority);
        case -1724546052: /*description*/  return new Property("description", "CodeableConcept", "Human-readable and/or coded description of a specific desired objective of care, such as \"control blood pressure\" or \"negotiate an obstacle course\" or \"dance with child at wedding\".", 0, 1, description);
        case -1867885268: /*subject*/  return new Property("subject", "Reference(Patient|Group|Organization)", "Identifies the patient, group or organization for whom the goal is being established.", 0, 1, subject);
        case 1316793566: /*start[x]*/  return new Property("start[x]", "date|CodeableConcept", "The date or event after which the goal should begin being pursued.", 0, 1, start);
        case 109757538: /*start*/  return new Property("start[x]", "date|CodeableConcept", "The date or event after which the goal should begin being pursued.", 0, 1, start);
        case -2129778896: /*startDate*/  return new Property("start[x]", "date|CodeableConcept", "The date or event after which the goal should begin being pursued.", 0, 1, start);
        case -1758833953: /*startCodeableConcept*/  return new Property("start[x]", "date|CodeableConcept", "The date or event after which the goal should begin being pursued.", 0, 1, start);
        case -880905839: /*target*/  return new Property("target", "", "Indicates what should be done by when.", 0, java.lang.Integer.MAX_VALUE, target);
        case 247524032: /*statusDate*/  return new Property("statusDate", "date", "Identifies when the current status.  I.e. When initially created, when achieved, when cancelled, etc.", 0, 1, statusDate);
        case 2051346646: /*statusReason*/  return new Property("statusReason", "string", "Captures the reason for the current status.", 0, 1, statusReason);
        case 175423686: /*expressedBy*/  return new Property("expressedBy", "Reference(Patient|Practitioner|PractitionerRole|RelatedPerson)", "Indicates whose goal this is - patient goal, practitioner goal, etc.", 0, 1, expressedBy);
        case 874544034: /*addresses*/  return new Property("addresses", "Reference(Condition|Observation|MedicationStatement|NutritionOrder|ServiceRequest|RiskAssessment)", "The identified conditions and other health record elements that are intended to be addressed by the goal.", 0, java.lang.Integer.MAX_VALUE, addresses);
        case 3387378: /*note*/  return new Property("note", "Annotation", "Any comments related to the goal.", 0, java.lang.Integer.MAX_VALUE, note);
        case 1062482015: /*outcomeCode*/  return new Property("outcomeCode", "CodeableConcept", "Identifies the change (or lack of change) at the point when the status of the goal is assessed.", 0, java.lang.Integer.MAX_VALUE, outcomeCode);
        case -782273511: /*outcomeReference*/  return new Property("outcomeReference", "Reference(Observation)", "Details of what's changed (or not changed).", 0, java.lang.Integer.MAX_VALUE, outcomeReference);
        default: return super.getNamedProperty(_hash, _name, _checkValid);
        }

      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case 1165552636: /*lifecycleStatus*/ return this.lifecycleStatus == null ? new Base[0] : new Base[] {this.lifecycleStatus}; // Enumeration<GoalLifecycleStatus>
        case 104524801: /*achievementStatus*/ return this.achievementStatus == null ? new Base[0] : new Base[] {this.achievementStatus}; // CodeableConcept
        case 50511102: /*category*/ return this.category == null ? new Base[0] : this.category.toArray(new Base[this.category.size()]); // CodeableConcept
        case -1165461084: /*priority*/ return this.priority == null ? new Base[0] : new Base[] {this.priority}; // CodeableConcept
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // CodeableConcept
        case -1867885268: /*subject*/ return this.subject == null ? new Base[0] : new Base[] {this.subject}; // Reference
        case 109757538: /*start*/ return this.start == null ? new Base[0] : new Base[] {this.start}; // Type
        case -880905839: /*target*/ return this.target == null ? new Base[0] : this.target.toArray(new Base[this.target.size()]); // GoalTargetComponent
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
        case 1165552636: // lifecycleStatus
          value = new GoalLifecycleStatusEnumFactory().fromType(castToCode(value));
          this.lifecycleStatus = (Enumeration) value; // Enumeration<GoalLifecycleStatus>
          return value;
        case 104524801: // achievementStatus
          this.achievementStatus = castToCodeableConcept(value); // CodeableConcept
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
          this.getTarget().add((GoalTargetComponent) value); // GoalTargetComponent
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
        } else if (name.equals("lifecycleStatus")) {
          value = new GoalLifecycleStatusEnumFactory().fromType(castToCode(value));
          this.lifecycleStatus = (Enumeration) value; // Enumeration<GoalLifecycleStatus>
        } else if (name.equals("achievementStatus")) {
          this.achievementStatus = castToCodeableConcept(value); // CodeableConcept
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
          this.getTarget().add((GoalTargetComponent) value);
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
        case 1165552636:  return getLifecycleStatusElement();
        case 104524801:  return getAchievementStatus(); 
        case 50511102:  return addCategory(); 
        case -1165461084:  return getPriority(); 
        case -1724546052:  return getDescription(); 
        case -1867885268:  return getSubject(); 
        case 1316793566:  return getStart(); 
        case 109757538:  return getStart(); 
        case -880905839:  return addTarget(); 
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
        case 1165552636: /*lifecycleStatus*/ return new String[] {"code"};
        case 104524801: /*achievementStatus*/ return new String[] {"CodeableConcept"};
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
        else if (name.equals("lifecycleStatus")) {
          throw new FHIRException("Cannot call addChild on a primitive type Goal.lifecycleStatus");
        }
        else if (name.equals("achievementStatus")) {
          this.achievementStatus = new CodeableConcept();
          return this.achievementStatus;
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
          return addTarget();
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
        dst.lifecycleStatus = lifecycleStatus == null ? null : lifecycleStatus.copy();
        dst.achievementStatus = achievementStatus == null ? null : achievementStatus.copy();
        if (category != null) {
          dst.category = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : category)
            dst.category.add(i.copy());
        };
        dst.priority = priority == null ? null : priority.copy();
        dst.description = description == null ? null : description.copy();
        dst.subject = subject == null ? null : subject.copy();
        dst.start = start == null ? null : start.copy();
        if (target != null) {
          dst.target = new ArrayList<GoalTargetComponent>();
          for (GoalTargetComponent i : target)
            dst.target.add(i.copy());
        };
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
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof Goal))
          return false;
        Goal o = (Goal) other_;
        return compareDeep(identifier, o.identifier, true) && compareDeep(lifecycleStatus, o.lifecycleStatus, true)
           && compareDeep(achievementStatus, o.achievementStatus, true) && compareDeep(category, o.category, true)
           && compareDeep(priority, o.priority, true) && compareDeep(description, o.description, true) && compareDeep(subject, o.subject, true)
           && compareDeep(start, o.start, true) && compareDeep(target, o.target, true) && compareDeep(statusDate, o.statusDate, true)
           && compareDeep(statusReason, o.statusReason, true) && compareDeep(expressedBy, o.expressedBy, true)
           && compareDeep(addresses, o.addresses, true) && compareDeep(note, o.note, true) && compareDeep(outcomeCode, o.outcomeCode, true)
           && compareDeep(outcomeReference, o.outcomeReference, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof Goal))
          return false;
        Goal o = (Goal) other_;
        return compareValues(lifecycleStatus, o.lifecycleStatus, true) && compareValues(statusDate, o.statusDate, true)
           && compareValues(statusReason, o.statusReason, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, lifecycleStatus
          , achievementStatus, category, priority, description, subject, start, target, statusDate
          , statusReason, expressedBy, addresses, note, outcomeCode, outcomeReference);
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
   * Search parameter: <b>lifecycle-status</b>
   * <p>
   * Description: <b>proposed | planned | accepted | active | on-hold | completed | cancelled | entered-in-error | rejected</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Goal.lifecycleStatus</b><br>
   * </p>
   */
  @SearchParamDefinition(name="lifecycle-status", path="Goal.lifecycleStatus", description="proposed | planned | accepted | active | on-hold | completed | cancelled | entered-in-error | rejected", type="token" )
  public static final String SP_LIFECYCLE_STATUS = "lifecycle-status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>lifecycle-status</b>
   * <p>
   * Description: <b>proposed | planned | accepted | active | on-hold | completed | cancelled | entered-in-error | rejected</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Goal.lifecycleStatus</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam LIFECYCLE_STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_LIFECYCLE_STATUS);

 /**
   * Search parameter: <b>achievement-status</b>
   * <p>
   * Description: <b>in-progress | improving | worsening | no-change | achieved | sustaining | not-achieved | no-progress | not-attainable</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Goal.achievementStatus</b><br>
   * </p>
   */
  @SearchParamDefinition(name="achievement-status", path="Goal.achievementStatus", description="in-progress | improving | worsening | no-change | achieved | sustaining | not-achieved | no-progress | not-attainable", type="token" )
  public static final String SP_ACHIEVEMENT_STATUS = "achievement-status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>achievement-status</b>
   * <p>
   * Description: <b>in-progress | improving | worsening | no-change | achieved | sustaining | not-achieved | no-progress | not-attainable</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Goal.achievementStatus</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam ACHIEVEMENT_STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_ACHIEVEMENT_STATUS);

 /**
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>Who this goal is intended for</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Goal.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="Goal.subject.where(resolve() is Patient)", description="Who this goal is intended for", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient") }, target={Patient.class } )
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
  @SearchParamDefinition(name="start-date", path="(Goal.start as date)", description="When goal pursuit begins", type="date" )
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
  @SearchParamDefinition(name="target-date", path="(Goal.target.due as date)", description="Reach goal on or before", type="date" )
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


}

