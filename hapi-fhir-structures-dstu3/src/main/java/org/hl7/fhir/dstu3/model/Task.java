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

// Generated on Sat, Nov 5, 2016 10:42-0400 for FHIR v1.7.0

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
 * A task to be performed.
 */
@ResourceDef(name="Task", profile="http://hl7.org/fhir/Profile/Task")
public class Task extends DomainResource {

    public enum TaskStatus {
        /**
         * The task is not yet ready to be acted upon.
         */
        DRAFT, 
        /**
         * The task is ready to be acted upon
         */
        REQUESTED, 
        /**
         * A potential performer has claimed ownership of the task and is evaluating whether to perform it
         */
        RECEIVED, 
        /**
         * The potential performer has agreed to execute the task but has not yet started work
         */
        ACCEPTED, 
        /**
         * The potential performer who claimed ownership of the task has decided not to execute it prior to performing any action.
         */
        REJECTED, 
        /**
         * Task is ready to be performed, but no action has yet been taken.  Used in place of requested/received/accepted/rejected when request assignment and acceptance is a given.
         */
        READY, 
        /**
         * Task has been started but is not yet complete.
         */
        INPROGRESS, 
        /**
         * Task has been started but work has been paused
         */
        ONHOLD, 
        /**
         * The task was attempted but could not be completed due to some error.
         */
        FAILED, 
        /**
         * The task has been completed (more or less) as requested.
         */
        COMPLETED, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static TaskStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("draft".equals(codeString))
          return DRAFT;
        if ("requested".equals(codeString))
          return REQUESTED;
        if ("received".equals(codeString))
          return RECEIVED;
        if ("accepted".equals(codeString))
          return ACCEPTED;
        if ("rejected".equals(codeString))
          return REJECTED;
        if ("ready".equals(codeString))
          return READY;
        if ("in-progress".equals(codeString))
          return INPROGRESS;
        if ("on-hold".equals(codeString))
          return ONHOLD;
        if ("failed".equals(codeString))
          return FAILED;
        if ("completed".equals(codeString))
          return COMPLETED;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown TaskStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case DRAFT: return "draft";
            case REQUESTED: return "requested";
            case RECEIVED: return "received";
            case ACCEPTED: return "accepted";
            case REJECTED: return "rejected";
            case READY: return "ready";
            case INPROGRESS: return "in-progress";
            case ONHOLD: return "on-hold";
            case FAILED: return "failed";
            case COMPLETED: return "completed";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case DRAFT: return "http://hl7.org/fhir/task-status";
            case REQUESTED: return "http://hl7.org/fhir/task-status";
            case RECEIVED: return "http://hl7.org/fhir/task-status";
            case ACCEPTED: return "http://hl7.org/fhir/task-status";
            case REJECTED: return "http://hl7.org/fhir/task-status";
            case READY: return "http://hl7.org/fhir/task-status";
            case INPROGRESS: return "http://hl7.org/fhir/task-status";
            case ONHOLD: return "http://hl7.org/fhir/task-status";
            case FAILED: return "http://hl7.org/fhir/task-status";
            case COMPLETED: return "http://hl7.org/fhir/task-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case DRAFT: return "The task is not yet ready to be acted upon.";
            case REQUESTED: return "The task is ready to be acted upon";
            case RECEIVED: return "A potential performer has claimed ownership of the task and is evaluating whether to perform it";
            case ACCEPTED: return "The potential performer has agreed to execute the task but has not yet started work";
            case REJECTED: return "The potential performer who claimed ownership of the task has decided not to execute it prior to performing any action.";
            case READY: return "Task is ready to be performed, but no action has yet been taken.  Used in place of requested/received/accepted/rejected when request assignment and acceptance is a given.";
            case INPROGRESS: return "Task has been started but is not yet complete.";
            case ONHOLD: return "Task has been started but work has been paused";
            case FAILED: return "The task was attempted but could not be completed due to some error.";
            case COMPLETED: return "The task has been completed (more or less) as requested.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case DRAFT: return "Draft";
            case REQUESTED: return "Requested";
            case RECEIVED: return "Received";
            case ACCEPTED: return "Accepted";
            case REJECTED: return "Rejected";
            case READY: return "Ready";
            case INPROGRESS: return "In Progress";
            case ONHOLD: return "On Hold";
            case FAILED: return "Failed";
            case COMPLETED: return "Completed";
            default: return "?";
          }
        }
    }

  public static class TaskStatusEnumFactory implements EnumFactory<TaskStatus> {
    public TaskStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("draft".equals(codeString))
          return TaskStatus.DRAFT;
        if ("requested".equals(codeString))
          return TaskStatus.REQUESTED;
        if ("received".equals(codeString))
          return TaskStatus.RECEIVED;
        if ("accepted".equals(codeString))
          return TaskStatus.ACCEPTED;
        if ("rejected".equals(codeString))
          return TaskStatus.REJECTED;
        if ("ready".equals(codeString))
          return TaskStatus.READY;
        if ("in-progress".equals(codeString))
          return TaskStatus.INPROGRESS;
        if ("on-hold".equals(codeString))
          return TaskStatus.ONHOLD;
        if ("failed".equals(codeString))
          return TaskStatus.FAILED;
        if ("completed".equals(codeString))
          return TaskStatus.COMPLETED;
        throw new IllegalArgumentException("Unknown TaskStatus code '"+codeString+"'");
        }
        public Enumeration<TaskStatus> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("draft".equals(codeString))
          return new Enumeration<TaskStatus>(this, TaskStatus.DRAFT);
        if ("requested".equals(codeString))
          return new Enumeration<TaskStatus>(this, TaskStatus.REQUESTED);
        if ("received".equals(codeString))
          return new Enumeration<TaskStatus>(this, TaskStatus.RECEIVED);
        if ("accepted".equals(codeString))
          return new Enumeration<TaskStatus>(this, TaskStatus.ACCEPTED);
        if ("rejected".equals(codeString))
          return new Enumeration<TaskStatus>(this, TaskStatus.REJECTED);
        if ("ready".equals(codeString))
          return new Enumeration<TaskStatus>(this, TaskStatus.READY);
        if ("in-progress".equals(codeString))
          return new Enumeration<TaskStatus>(this, TaskStatus.INPROGRESS);
        if ("on-hold".equals(codeString))
          return new Enumeration<TaskStatus>(this, TaskStatus.ONHOLD);
        if ("failed".equals(codeString))
          return new Enumeration<TaskStatus>(this, TaskStatus.FAILED);
        if ("completed".equals(codeString))
          return new Enumeration<TaskStatus>(this, TaskStatus.COMPLETED);
        throw new FHIRException("Unknown TaskStatus code '"+codeString+"'");
        }
    public String toCode(TaskStatus code) {
      if (code == TaskStatus.DRAFT)
        return "draft";
      if (code == TaskStatus.REQUESTED)
        return "requested";
      if (code == TaskStatus.RECEIVED)
        return "received";
      if (code == TaskStatus.ACCEPTED)
        return "accepted";
      if (code == TaskStatus.REJECTED)
        return "rejected";
      if (code == TaskStatus.READY)
        return "ready";
      if (code == TaskStatus.INPROGRESS)
        return "in-progress";
      if (code == TaskStatus.ONHOLD)
        return "on-hold";
      if (code == TaskStatus.FAILED)
        return "failed";
      if (code == TaskStatus.COMPLETED)
        return "completed";
      return "?";
      }
    public String toSystem(TaskStatus code) {
      return code.getSystem();
      }
    }

    public enum TaskPriority {
        /**
         * This task has low priority.
         */
        LOW, 
        /**
         * This task has normal priority.
         */
        NORMAL, 
        /**
         * This task has high priority.
         */
        HIGH, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static TaskPriority fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("low".equals(codeString))
          return LOW;
        if ("normal".equals(codeString))
          return NORMAL;
        if ("high".equals(codeString))
          return HIGH;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown TaskPriority code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case LOW: return "low";
            case NORMAL: return "normal";
            case HIGH: return "high";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case LOW: return "http://hl7.org/fhir/task-priority";
            case NORMAL: return "http://hl7.org/fhir/task-priority";
            case HIGH: return "http://hl7.org/fhir/task-priority";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case LOW: return "This task has low priority.";
            case NORMAL: return "This task has normal priority.";
            case HIGH: return "This task has high priority.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case LOW: return "Low";
            case NORMAL: return "Normal";
            case HIGH: return "High";
            default: return "?";
          }
        }
    }

  public static class TaskPriorityEnumFactory implements EnumFactory<TaskPriority> {
    public TaskPriority fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("low".equals(codeString))
          return TaskPriority.LOW;
        if ("normal".equals(codeString))
          return TaskPriority.NORMAL;
        if ("high".equals(codeString))
          return TaskPriority.HIGH;
        throw new IllegalArgumentException("Unknown TaskPriority code '"+codeString+"'");
        }
        public Enumeration<TaskPriority> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("low".equals(codeString))
          return new Enumeration<TaskPriority>(this, TaskPriority.LOW);
        if ("normal".equals(codeString))
          return new Enumeration<TaskPriority>(this, TaskPriority.NORMAL);
        if ("high".equals(codeString))
          return new Enumeration<TaskPriority>(this, TaskPriority.HIGH);
        throw new FHIRException("Unknown TaskPriority code '"+codeString+"'");
        }
    public String toCode(TaskPriority code) {
      if (code == TaskPriority.LOW)
        return "low";
      if (code == TaskPriority.NORMAL)
        return "normal";
      if (code == TaskPriority.HIGH)
        return "high";
      return "?";
      }
    public String toSystem(TaskPriority code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class TaskFulfillmentComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Indicates the number of times the requested action should occur.
         */
        @Child(name = "repetitions", type = {PositiveIntType.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="How many times to repeat", formalDefinition="Indicates the number of times the requested action should occur." )
        protected PositiveIntType repetitions;

        /**
         * Over what time-period is fulfillment sought.
         */
        @Child(name = "period", type = {Period.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Over what time-period is fulfillment sought", formalDefinition="Over what time-period is fulfillment sought." )
        protected Period period;

        /**
         * For requests that are targeted to more than on potential recipient/target, for whom is fulfillment sought?
         */
        @Child(name = "recipients", type = {Patient.class, Practitioner.class, RelatedPerson.class, Group.class, Organization.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="For whom is fulfillment sought?", formalDefinition="For requests that are targeted to more than on potential recipient/target, for whom is fulfillment sought?" )
        protected List<Reference> recipients;
        /**
         * The actual objects that are the target of the reference (For requests that are targeted to more than on potential recipient/target, for whom is fulfillment sought?)
         */
        protected List<Resource> recipientsTarget;


        private static final long serialVersionUID = -805919764L;

    /**
     * Constructor
     */
      public TaskFulfillmentComponent() {
        super();
      }

        /**
         * @return {@link #repetitions} (Indicates the number of times the requested action should occur.). This is the underlying object with id, value and extensions. The accessor "getRepetitions" gives direct access to the value
         */
        public PositiveIntType getRepetitionsElement() { 
          if (this.repetitions == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TaskFulfillmentComponent.repetitions");
            else if (Configuration.doAutoCreate())
              this.repetitions = new PositiveIntType(); // bb
          return this.repetitions;
        }

        public boolean hasRepetitionsElement() { 
          return this.repetitions != null && !this.repetitions.isEmpty();
        }

        public boolean hasRepetitions() { 
          return this.repetitions != null && !this.repetitions.isEmpty();
        }

        /**
         * @param value {@link #repetitions} (Indicates the number of times the requested action should occur.). This is the underlying object with id, value and extensions. The accessor "getRepetitions" gives direct access to the value
         */
        public TaskFulfillmentComponent setRepetitionsElement(PositiveIntType value) { 
          this.repetitions = value;
          return this;
        }

        /**
         * @return Indicates the number of times the requested action should occur.
         */
        public int getRepetitions() { 
          return this.repetitions == null || this.repetitions.isEmpty() ? 0 : this.repetitions.getValue();
        }

        /**
         * @param value Indicates the number of times the requested action should occur.
         */
        public TaskFulfillmentComponent setRepetitions(int value) { 
            if (this.repetitions == null)
              this.repetitions = new PositiveIntType();
            this.repetitions.setValue(value);
          return this;
        }

        /**
         * @return {@link #period} (Over what time-period is fulfillment sought.)
         */
        public Period getPeriod() { 
          if (this.period == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TaskFulfillmentComponent.period");
            else if (Configuration.doAutoCreate())
              this.period = new Period(); // cc
          return this.period;
        }

        public boolean hasPeriod() { 
          return this.period != null && !this.period.isEmpty();
        }

        /**
         * @param value {@link #period} (Over what time-period is fulfillment sought.)
         */
        public TaskFulfillmentComponent setPeriod(Period value) { 
          this.period = value;
          return this;
        }

        /**
         * @return {@link #recipients} (For requests that are targeted to more than on potential recipient/target, for whom is fulfillment sought?)
         */
        public List<Reference> getRecipients() { 
          if (this.recipients == null)
            this.recipients = new ArrayList<Reference>();
          return this.recipients;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public TaskFulfillmentComponent setRecipients(List<Reference> theRecipients) { 
          this.recipients = theRecipients;
          return this;
        }

        public boolean hasRecipients() { 
          if (this.recipients == null)
            return false;
          for (Reference item : this.recipients)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public Reference addRecipients() { //3
          Reference t = new Reference();
          if (this.recipients == null)
            this.recipients = new ArrayList<Reference>();
          this.recipients.add(t);
          return t;
        }

        public TaskFulfillmentComponent addRecipients(Reference t) { //3
          if (t == null)
            return this;
          if (this.recipients == null)
            this.recipients = new ArrayList<Reference>();
          this.recipients.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #recipients}, creating it if it does not already exist
         */
        public Reference getRecipientsFirstRep() { 
          if (getRecipients().isEmpty()) {
            addRecipients();
          }
          return getRecipients().get(0);
        }

        /**
         * @deprecated Use Reference#setResource(IBaseResource) instead
         */
        @Deprecated
        public List<Resource> getRecipientsTarget() { 
          if (this.recipientsTarget == null)
            this.recipientsTarget = new ArrayList<Resource>();
          return this.recipientsTarget;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("repetitions", "positiveInt", "Indicates the number of times the requested action should occur.", 0, java.lang.Integer.MAX_VALUE, repetitions));
          childrenList.add(new Property("period", "Period", "Over what time-period is fulfillment sought.", 0, java.lang.Integer.MAX_VALUE, period));
          childrenList.add(new Property("recipients", "Reference(Patient|Practitioner|RelatedPerson|Group|Organization)", "For requests that are targeted to more than on potential recipient/target, for whom is fulfillment sought?", 0, java.lang.Integer.MAX_VALUE, recipients));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 984367650: /*repetitions*/ return this.repetitions == null ? new Base[0] : new Base[] {this.repetitions}; // PositiveIntType
        case -991726143: /*period*/ return this.period == null ? new Base[0] : new Base[] {this.period}; // Period
        case -347287174: /*recipients*/ return this.recipients == null ? new Base[0] : this.recipients.toArray(new Base[this.recipients.size()]); // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 984367650: // repetitions
          this.repetitions = castToPositiveInt(value); // PositiveIntType
          break;
        case -991726143: // period
          this.period = castToPeriod(value); // Period
          break;
        case -347287174: // recipients
          this.getRecipients().add(castToReference(value)); // Reference
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("repetitions"))
          this.repetitions = castToPositiveInt(value); // PositiveIntType
        else if (name.equals("period"))
          this.period = castToPeriod(value); // Period
        else if (name.equals("recipients"))
          this.getRecipients().add(castToReference(value));
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 984367650: throw new FHIRException("Cannot make property repetitions as it is not a complex type"); // PositiveIntType
        case -991726143:  return getPeriod(); // Period
        case -347287174:  return addRecipients(); // Reference
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("repetitions")) {
          throw new FHIRException("Cannot call addChild on a primitive type Task.repetitions");
        }
        else if (name.equals("period")) {
          this.period = new Period();
          return this.period;
        }
        else if (name.equals("recipients")) {
          return addRecipients();
        }
        else
          return super.addChild(name);
      }

      public TaskFulfillmentComponent copy() {
        TaskFulfillmentComponent dst = new TaskFulfillmentComponent();
        copyValues(dst);
        dst.repetitions = repetitions == null ? null : repetitions.copy();
        dst.period = period == null ? null : period.copy();
        if (recipients != null) {
          dst.recipients = new ArrayList<Reference>();
          for (Reference i : recipients)
            dst.recipients.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof TaskFulfillmentComponent))
          return false;
        TaskFulfillmentComponent o = (TaskFulfillmentComponent) other;
        return compareDeep(repetitions, o.repetitions, true) && compareDeep(period, o.period, true) && compareDeep(recipients, o.recipients, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof TaskFulfillmentComponent))
          return false;
        TaskFulfillmentComponent o = (TaskFulfillmentComponent) other;
        return compareValues(repetitions, o.repetitions, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(repetitions, period, recipients
          );
      }

  public String fhirType() {
    return "Task.fulfillment";

  }

  }

    @Block()
    public static class ParameterComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A code or description indicating how the input is intended to be used as part of the task execution.
         */
        @Child(name = "type", type = {CodeableConcept.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Label for the input", formalDefinition="A code or description indicating how the input is intended to be used as part of the task execution." )
        protected CodeableConcept type;

        /**
         * The value of the input parameter as a basic type.
         */
        @Child(name = "value", type = {}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Input Value", formalDefinition="The value of the input parameter as a basic type." )
        protected org.hl7.fhir.dstu3.model.Type value;

        private static final long serialVersionUID = -852629026L;

    /**
     * Constructor
     */
      public ParameterComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ParameterComponent(CodeableConcept type, org.hl7.fhir.dstu3.model.Type value) {
        super();
        this.type = type;
        this.value = value;
      }

        /**
         * @return {@link #type} (A code or description indicating how the input is intended to be used as part of the task execution.)
         */
        public CodeableConcept getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ParameterComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new CodeableConcept(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (A code or description indicating how the input is intended to be used as part of the task execution.)
         */
        public ParameterComponent setType(CodeableConcept value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #value} (The value of the input parameter as a basic type.)
         */
        public org.hl7.fhir.dstu3.model.Type getValue() { 
          return this.value;
        }

        public boolean hasValue() { 
          return this.value != null && !this.value.isEmpty();
        }

        /**
         * @param value {@link #value} (The value of the input parameter as a basic type.)
         */
        public ParameterComponent setValue(org.hl7.fhir.dstu3.model.Type value) { 
          this.value = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("type", "CodeableConcept", "A code or description indicating how the input is intended to be used as part of the task execution.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("value[x]", "*", "The value of the input parameter as a basic type.", 0, java.lang.Integer.MAX_VALUE, value));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case 111972721: /*value*/ return this.value == null ? new Base[0] : new Base[] {this.value}; // org.hl7.fhir.dstu3.model.Type
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          break;
        case 111972721: // value
          this.value = castToType(value); // org.hl7.fhir.dstu3.model.Type
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type"))
          this.type = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("value[x]"))
          this.value = castToType(value); // org.hl7.fhir.dstu3.model.Type
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610:  return getType(); // CodeableConcept
        case -1410166417:  return getValue(); // org.hl7.fhir.dstu3.model.Type
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("valueBoolean")) {
          this.value = new BooleanType();
          return this.value;
        }
        else if (name.equals("valueInteger")) {
          this.value = new IntegerType();
          return this.value;
        }
        else if (name.equals("valueDecimal")) {
          this.value = new DecimalType();
          return this.value;
        }
        else if (name.equals("valueBase64Binary")) {
          this.value = new Base64BinaryType();
          return this.value;
        }
        else if (name.equals("valueInstant")) {
          this.value = new InstantType();
          return this.value;
        }
        else if (name.equals("valueString")) {
          this.value = new StringType();
          return this.value;
        }
        else if (name.equals("valueUri")) {
          this.value = new UriType();
          return this.value;
        }
        else if (name.equals("valueDate")) {
          this.value = new DateType();
          return this.value;
        }
        else if (name.equals("valueDateTime")) {
          this.value = new DateTimeType();
          return this.value;
        }
        else if (name.equals("valueTime")) {
          this.value = new TimeType();
          return this.value;
        }
        else if (name.equals("valueCode")) {
          this.value = new CodeType();
          return this.value;
        }
        else if (name.equals("valueOid")) {
          this.value = new OidType();
          return this.value;
        }
        else if (name.equals("valueId")) {
          this.value = new IdType();
          return this.value;
        }
        else if (name.equals("valueUnsignedInt")) {
          this.value = new UnsignedIntType();
          return this.value;
        }
        else if (name.equals("valuePositiveInt")) {
          this.value = new PositiveIntType();
          return this.value;
        }
        else if (name.equals("valueMarkdown")) {
          this.value = new MarkdownType();
          return this.value;
        }
        else if (name.equals("valueAnnotation")) {
          this.value = new Annotation();
          return this.value;
        }
        else if (name.equals("valueAttachment")) {
          this.value = new Attachment();
          return this.value;
        }
        else if (name.equals("valueIdentifier")) {
          this.value = new Identifier();
          return this.value;
        }
        else if (name.equals("valueCodeableConcept")) {
          this.value = new CodeableConcept();
          return this.value;
        }
        else if (name.equals("valueCoding")) {
          this.value = new Coding();
          return this.value;
        }
        else if (name.equals("valueQuantity")) {
          this.value = new Quantity();
          return this.value;
        }
        else if (name.equals("valueRange")) {
          this.value = new Range();
          return this.value;
        }
        else if (name.equals("valuePeriod")) {
          this.value = new Period();
          return this.value;
        }
        else if (name.equals("valueRatio")) {
          this.value = new Ratio();
          return this.value;
        }
        else if (name.equals("valueSampledData")) {
          this.value = new SampledData();
          return this.value;
        }
        else if (name.equals("valueSignature")) {
          this.value = new Signature();
          return this.value;
        }
        else if (name.equals("valueHumanName")) {
          this.value = new HumanName();
          return this.value;
        }
        else if (name.equals("valueAddress")) {
          this.value = new Address();
          return this.value;
        }
        else if (name.equals("valueContactPoint")) {
          this.value = new ContactPoint();
          return this.value;
        }
        else if (name.equals("valueTiming")) {
          this.value = new Timing();
          return this.value;
        }
        else if (name.equals("valueReference")) {
          this.value = new Reference();
          return this.value;
        }
        else if (name.equals("valueMeta")) {
          this.value = new Meta();
          return this.value;
        }
        else
          return super.addChild(name);
      }

      public ParameterComponent copy() {
        ParameterComponent dst = new ParameterComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.value = value == null ? null : value.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ParameterComponent))
          return false;
        ParameterComponent o = (ParameterComponent) other;
        return compareDeep(type, o.type, true) && compareDeep(value, o.value, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ParameterComponent))
          return false;
        ParameterComponent o = (ParameterComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(type, value);
      }

  public String fhirType() {
    return "Task.input";

  }

  }

    @Block()
    public static class TaskOutputComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The name of the Output parameter.
         */
        @Child(name = "type", type = {CodeableConcept.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Output Name", formalDefinition="The name of the Output parameter." )
        protected CodeableConcept type;

        /**
         * The value of the Output parameter as a basic type.
         */
        @Child(name = "value", type = {}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Output Value", formalDefinition="The value of the Output parameter as a basic type." )
        protected org.hl7.fhir.dstu3.model.Type value;

        private static final long serialVersionUID = -852629026L;

    /**
     * Constructor
     */
      public TaskOutputComponent() {
        super();
      }

    /**
     * Constructor
     */
      public TaskOutputComponent(CodeableConcept type, org.hl7.fhir.dstu3.model.Type value) {
        super();
        this.type = type;
        this.value = value;
      }

        /**
         * @return {@link #type} (The name of the Output parameter.)
         */
        public CodeableConcept getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TaskOutputComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new CodeableConcept(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (The name of the Output parameter.)
         */
        public TaskOutputComponent setType(CodeableConcept value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #value} (The value of the Output parameter as a basic type.)
         */
        public org.hl7.fhir.dstu3.model.Type getValue() { 
          return this.value;
        }

        public boolean hasValue() { 
          return this.value != null && !this.value.isEmpty();
        }

        /**
         * @param value {@link #value} (The value of the Output parameter as a basic type.)
         */
        public TaskOutputComponent setValue(org.hl7.fhir.dstu3.model.Type value) { 
          this.value = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("type", "CodeableConcept", "The name of the Output parameter.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("value[x]", "*", "The value of the Output parameter as a basic type.", 0, java.lang.Integer.MAX_VALUE, value));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case 111972721: /*value*/ return this.value == null ? new Base[0] : new Base[] {this.value}; // org.hl7.fhir.dstu3.model.Type
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          break;
        case 111972721: // value
          this.value = castToType(value); // org.hl7.fhir.dstu3.model.Type
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type"))
          this.type = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("value[x]"))
          this.value = castToType(value); // org.hl7.fhir.dstu3.model.Type
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610:  return getType(); // CodeableConcept
        case -1410166417:  return getValue(); // org.hl7.fhir.dstu3.model.Type
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("valueBoolean")) {
          this.value = new BooleanType();
          return this.value;
        }
        else if (name.equals("valueInteger")) {
          this.value = new IntegerType();
          return this.value;
        }
        else if (name.equals("valueDecimal")) {
          this.value = new DecimalType();
          return this.value;
        }
        else if (name.equals("valueBase64Binary")) {
          this.value = new Base64BinaryType();
          return this.value;
        }
        else if (name.equals("valueInstant")) {
          this.value = new InstantType();
          return this.value;
        }
        else if (name.equals("valueString")) {
          this.value = new StringType();
          return this.value;
        }
        else if (name.equals("valueUri")) {
          this.value = new UriType();
          return this.value;
        }
        else if (name.equals("valueDate")) {
          this.value = new DateType();
          return this.value;
        }
        else if (name.equals("valueDateTime")) {
          this.value = new DateTimeType();
          return this.value;
        }
        else if (name.equals("valueTime")) {
          this.value = new TimeType();
          return this.value;
        }
        else if (name.equals("valueCode")) {
          this.value = new CodeType();
          return this.value;
        }
        else if (name.equals("valueOid")) {
          this.value = new OidType();
          return this.value;
        }
        else if (name.equals("valueId")) {
          this.value = new IdType();
          return this.value;
        }
        else if (name.equals("valueUnsignedInt")) {
          this.value = new UnsignedIntType();
          return this.value;
        }
        else if (name.equals("valuePositiveInt")) {
          this.value = new PositiveIntType();
          return this.value;
        }
        else if (name.equals("valueMarkdown")) {
          this.value = new MarkdownType();
          return this.value;
        }
        else if (name.equals("valueAnnotation")) {
          this.value = new Annotation();
          return this.value;
        }
        else if (name.equals("valueAttachment")) {
          this.value = new Attachment();
          return this.value;
        }
        else if (name.equals("valueIdentifier")) {
          this.value = new Identifier();
          return this.value;
        }
        else if (name.equals("valueCodeableConcept")) {
          this.value = new CodeableConcept();
          return this.value;
        }
        else if (name.equals("valueCoding")) {
          this.value = new Coding();
          return this.value;
        }
        else if (name.equals("valueQuantity")) {
          this.value = new Quantity();
          return this.value;
        }
        else if (name.equals("valueRange")) {
          this.value = new Range();
          return this.value;
        }
        else if (name.equals("valuePeriod")) {
          this.value = new Period();
          return this.value;
        }
        else if (name.equals("valueRatio")) {
          this.value = new Ratio();
          return this.value;
        }
        else if (name.equals("valueSampledData")) {
          this.value = new SampledData();
          return this.value;
        }
        else if (name.equals("valueSignature")) {
          this.value = new Signature();
          return this.value;
        }
        else if (name.equals("valueHumanName")) {
          this.value = new HumanName();
          return this.value;
        }
        else if (name.equals("valueAddress")) {
          this.value = new Address();
          return this.value;
        }
        else if (name.equals("valueContactPoint")) {
          this.value = new ContactPoint();
          return this.value;
        }
        else if (name.equals("valueTiming")) {
          this.value = new Timing();
          return this.value;
        }
        else if (name.equals("valueReference")) {
          this.value = new Reference();
          return this.value;
        }
        else if (name.equals("valueMeta")) {
          this.value = new Meta();
          return this.value;
        }
        else
          return super.addChild(name);
      }

      public TaskOutputComponent copy() {
        TaskOutputComponent dst = new TaskOutputComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.value = value == null ? null : value.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof TaskOutputComponent))
          return false;
        TaskOutputComponent o = (TaskOutputComponent) other;
        return compareDeep(type, o.type, true) && compareDeep(value, o.value, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof TaskOutputComponent))
          return false;
        TaskOutputComponent o = (TaskOutputComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(type, value);
      }

  public String fhirType() {
    return "Task.output";

  }

  }

    /**
     * The business identifier for this task.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Task Instance Identifier", formalDefinition="The business identifier for this task." )
    protected Identifier identifier;

    /**
     * Identifies a plan, proposal or order that this task has been created in fulfillment of.
     */
    @Child(name = "basedOn", type = {Reference.class}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Request fulfilled by this task", formalDefinition="Identifies a plan, proposal or order that this task has been created in fulfillment of." )
    protected List<Reference> basedOn;
    /**
     * The actual objects that are the target of the reference (Identifies a plan, proposal or order that this task has been created in fulfillment of.)
     */
    protected List<Resource> basedOnTarget;


    /**
     * An identifier that links together multiple tasks and other requests that were created in the same context.
     */
    @Child(name = "requisition", type = {Identifier.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Requisition or grouper id", formalDefinition="An identifier that links together multiple tasks and other requests that were created in the same context." )
    protected Identifier requisition;

    /**
     * Task that this particular task is part of.
     */
    @Child(name = "parent", type = {Task.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Composite task", formalDefinition="Task that this particular task is part of." )
    protected List<Reference> parent;
    /**
     * The actual objects that are the target of the reference (Task that this particular task is part of.)
     */
    protected List<Task> parentTarget;


    /**
     * The current status of the task.
     */
    @Child(name = "status", type = {CodeType.class}, order=4, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="draft | requested | received | accepted | +", formalDefinition="The current status of the task." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/task-status")
    protected Enumeration<TaskStatus> status;

    /**
     * An explanation as to why this task is held, failed, was refused, etc.
     */
    @Child(name = "statusReason", type = {CodeableConcept.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Reason for current status", formalDefinition="An explanation as to why this task is held, failed, was refused, etc." )
    protected CodeableConcept statusReason;

    /**
     * Contains business-specific nuances of the business state.
     */
    @Child(name = "businessStatus", type = {CodeableConcept.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="E.g. \"Specimen collected\", \"IV prepped\"", formalDefinition="Contains business-specific nuances of the business state." )
    protected CodeableConcept businessStatus;

    /**
     * Indicates the "level" of actionability associated with the Task.  I.e. Is this a proposed task, a planned task, an actionable task, etc.
     */
    @Child(name = "stage", type = {CodeableConcept.class}, order=7, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="proposed | planned | actionable +", formalDefinition="Indicates the \"level\" of actionability associated with the Task.  I.e. Is this a proposed task, a planned task, an actionable task, etc." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/task-stage")
    protected CodeableConcept stage;

    /**
     * A name or code (or both) briefly describing what the task involves.
     */
    @Child(name = "code", type = {CodeableConcept.class}, order=8, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Task Type", formalDefinition="A name or code (or both) briefly describing what the task involves." )
    protected CodeableConcept code;

    /**
     * The priority of the task among other tasks of the same type.
     */
    @Child(name = "priority", type = {CodeType.class}, order=9, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="low | normal | high", formalDefinition="The priority of the task among other tasks of the same type." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/task-priority")
    protected Enumeration<TaskPriority> priority;

    /**
     * A free-text description of what is to be performed.
     */
    @Child(name = "description", type = {StringType.class}, order=10, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Human-readable explanation of task", formalDefinition="A free-text description of what is to be performed." )
    protected StringType description;

    /**
     * The request being actioned or the resource being manipulated by this task.
     */
    @Child(name = "focus", type = {Reference.class}, order=11, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="What task is acting on", formalDefinition="The request being actioned or the resource being manipulated by this task." )
    protected Reference focus;

    /**
     * The actual object that is the target of the reference (The request being actioned or the resource being manipulated by this task.)
     */
    protected Resource focusTarget;

    /**
     * The entity who benefits from the performance of the service specified in the task (e.g., the patient).
     */
    @Child(name = "for", type = {Reference.class}, order=12, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Beneficiary of the Task", formalDefinition="The entity who benefits from the performance of the service specified in the task (e.g., the patient)." )
    protected Reference for_;

    /**
     * The actual object that is the target of the reference (The entity who benefits from the performance of the service specified in the task (e.g., the patient).)
     */
    protected Resource for_Target;

    /**
     * The healthcare event  (e.g. a patient and healthcare provider interaction) during which this task was created.
     */
    @Child(name = "context", type = {Encounter.class, EpisodeOfCare.class}, order=13, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Healthcare event during which this task originated", formalDefinition="The healthcare event  (e.g. a patient and healthcare provider interaction) during which this task was created." )
    protected Reference context;

    /**
     * The actual object that is the target of the reference (The healthcare event  (e.g. a patient and healthcare provider interaction) during which this task was created.)
     */
    protected Resource contextTarget;

    /**
     * The date and time this task was created.
     */
    @Child(name = "created", type = {DateTimeType.class}, order=14, min=1, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Task Creation Date", formalDefinition="The date and time this task was created." )
    protected DateTimeType created;

    /**
     * The date and time of last modification to this task.
     */
    @Child(name = "lastModified", type = {DateTimeType.class}, order=15, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Task Last Modified Date", formalDefinition="The date and time of last modification to this task." )
    protected DateTimeType lastModified;

    /**
     * The creator of the task.
     */
    @Child(name = "requester", type = {Device.class, Organization.class, Patient.class, Practitioner.class, RelatedPerson.class}, order=16, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Task Creator", formalDefinition="The creator of the task." )
    protected Reference requester;

    /**
     * The actual object that is the target of the reference (The creator of the task.)
     */
    protected Resource requesterTarget;

    /**
     * The owner of this task.  The participant who can execute this task.
     */
    @Child(name = "owner", type = {Device.class, Organization.class, Patient.class, Practitioner.class, RelatedPerson.class}, order=17, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Task Owner", formalDefinition="The owner of this task.  The participant who can execute this task." )
    protected Reference owner;

    /**
     * The actual object that is the target of the reference (The owner of this task.  The participant who can execute this task.)
     */
    protected Resource ownerTarget;

    /**
     * The type of participant that can execute the task.
     */
    @Child(name = "performerType", type = {CodeableConcept.class}, order=18, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="requester | dispatcher | scheduler | performer | monitor | manager | acquirer | reviewer", formalDefinition="The type of participant that can execute the task." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/task-performer-type")
    protected List<CodeableConcept> performerType;

    /**
     * A description or code indicating why this task needs to be performed.
     */
    @Child(name = "reason", type = {CodeableConcept.class}, order=19, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Why task is needed", formalDefinition="A description or code indicating why this task needs to be performed." )
    protected CodeableConcept reason;

    /**
     * Free-text information captured about the task as it progresses.
     */
    @Child(name = "note", type = {Annotation.class}, order=20, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Comments made about the task", formalDefinition="Free-text information captured about the task as it progresses." )
    protected List<Annotation> note;

    /**
     * Identifies any limitations on what part of a referenced task subject request should be actioned.
     */
    @Child(name = "fulfillment", type = {}, order=21, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Constraints on fulfillment tasks", formalDefinition="Identifies any limitations on what part of a referenced task subject request should be actioned." )
    protected TaskFulfillmentComponent fulfillment;

    /**
     * A reference to a formal or informal definition of the task.  For example, a protocol, a step within a defined workflow definition, etc.
     */
    @Child(name = "definition", type = {UriType.class}, order=22, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Task Definition", formalDefinition="A reference to a formal or informal definition of the task.  For example, a protocol, a step within a defined workflow definition, etc." )
    protected UriType definition;

    /**
     * Additional information that may be needed in the execution of the task.
     */
    @Child(name = "input", type = {}, order=23, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Supporting information", formalDefinition="Additional information that may be needed in the execution of the task." )
    protected List<ParameterComponent> input;

    /**
     * Outputs produced by the Task.
     */
    @Child(name = "output", type = {}, order=24, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Task Output", formalDefinition="Outputs produced by the Task." )
    protected List<TaskOutputComponent> output;

    private static final long serialVersionUID = 564776279L;

  /**
   * Constructor
   */
    public Task() {
      super();
    }

  /**
   * Constructor
   */
    public Task(Enumeration<TaskStatus> status, CodeableConcept stage, DateTimeType created, DateTimeType lastModified, Reference requester) {
      super();
      this.status = status;
      this.stage = stage;
      this.created = created;
      this.lastModified = lastModified;
      this.requester = requester;
    }

    /**
     * @return {@link #identifier} (The business identifier for this task.)
     */
    public Identifier getIdentifier() { 
      if (this.identifier == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Task.identifier");
        else if (Configuration.doAutoCreate())
          this.identifier = new Identifier(); // cc
      return this.identifier;
    }

    public boolean hasIdentifier() { 
      return this.identifier != null && !this.identifier.isEmpty();
    }

    /**
     * @param value {@link #identifier} (The business identifier for this task.)
     */
    public Task setIdentifier(Identifier value) { 
      this.identifier = value;
      return this;
    }

    /**
     * @return {@link #basedOn} (Identifies a plan, proposal or order that this task has been created in fulfillment of.)
     */
    public List<Reference> getBasedOn() { 
      if (this.basedOn == null)
        this.basedOn = new ArrayList<Reference>();
      return this.basedOn;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Task setBasedOn(List<Reference> theBasedOn) { 
      this.basedOn = theBasedOn;
      return this;
    }

    public boolean hasBasedOn() { 
      if (this.basedOn == null)
        return false;
      for (Reference item : this.basedOn)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addBasedOn() { //3
      Reference t = new Reference();
      if (this.basedOn == null)
        this.basedOn = new ArrayList<Reference>();
      this.basedOn.add(t);
      return t;
    }

    public Task addBasedOn(Reference t) { //3
      if (t == null)
        return this;
      if (this.basedOn == null)
        this.basedOn = new ArrayList<Reference>();
      this.basedOn.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #basedOn}, creating it if it does not already exist
     */
    public Reference getBasedOnFirstRep() { 
      if (getBasedOn().isEmpty()) {
        addBasedOn();
      }
      return getBasedOn().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Resource> getBasedOnTarget() { 
      if (this.basedOnTarget == null)
        this.basedOnTarget = new ArrayList<Resource>();
      return this.basedOnTarget;
    }

    /**
     * @return {@link #requisition} (An identifier that links together multiple tasks and other requests that were created in the same context.)
     */
    public Identifier getRequisition() { 
      if (this.requisition == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Task.requisition");
        else if (Configuration.doAutoCreate())
          this.requisition = new Identifier(); // cc
      return this.requisition;
    }

    public boolean hasRequisition() { 
      return this.requisition != null && !this.requisition.isEmpty();
    }

    /**
     * @param value {@link #requisition} (An identifier that links together multiple tasks and other requests that were created in the same context.)
     */
    public Task setRequisition(Identifier value) { 
      this.requisition = value;
      return this;
    }

    /**
     * @return {@link #parent} (Task that this particular task is part of.)
     */
    public List<Reference> getParent() { 
      if (this.parent == null)
        this.parent = new ArrayList<Reference>();
      return this.parent;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Task setParent(List<Reference> theParent) { 
      this.parent = theParent;
      return this;
    }

    public boolean hasParent() { 
      if (this.parent == null)
        return false;
      for (Reference item : this.parent)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addParent() { //3
      Reference t = new Reference();
      if (this.parent == null)
        this.parent = new ArrayList<Reference>();
      this.parent.add(t);
      return t;
    }

    public Task addParent(Reference t) { //3
      if (t == null)
        return this;
      if (this.parent == null)
        this.parent = new ArrayList<Reference>();
      this.parent.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #parent}, creating it if it does not already exist
     */
    public Reference getParentFirstRep() { 
      if (getParent().isEmpty()) {
        addParent();
      }
      return getParent().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Task> getParentTarget() { 
      if (this.parentTarget == null)
        this.parentTarget = new ArrayList<Task>();
      return this.parentTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public Task addParentTarget() { 
      Task r = new Task();
      if (this.parentTarget == null)
        this.parentTarget = new ArrayList<Task>();
      this.parentTarget.add(r);
      return r;
    }

    /**
     * @return {@link #status} (The current status of the task.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<TaskStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Task.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<TaskStatus>(new TaskStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (The current status of the task.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Task setStatusElement(Enumeration<TaskStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The current status of the task.
     */
    public TaskStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The current status of the task.
     */
    public Task setStatus(TaskStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<TaskStatus>(new TaskStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #statusReason} (An explanation as to why this task is held, failed, was refused, etc.)
     */
    public CodeableConcept getStatusReason() { 
      if (this.statusReason == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Task.statusReason");
        else if (Configuration.doAutoCreate())
          this.statusReason = new CodeableConcept(); // cc
      return this.statusReason;
    }

    public boolean hasStatusReason() { 
      return this.statusReason != null && !this.statusReason.isEmpty();
    }

    /**
     * @param value {@link #statusReason} (An explanation as to why this task is held, failed, was refused, etc.)
     */
    public Task setStatusReason(CodeableConcept value) { 
      this.statusReason = value;
      return this;
    }

    /**
     * @return {@link #businessStatus} (Contains business-specific nuances of the business state.)
     */
    public CodeableConcept getBusinessStatus() { 
      if (this.businessStatus == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Task.businessStatus");
        else if (Configuration.doAutoCreate())
          this.businessStatus = new CodeableConcept(); // cc
      return this.businessStatus;
    }

    public boolean hasBusinessStatus() { 
      return this.businessStatus != null && !this.businessStatus.isEmpty();
    }

    /**
     * @param value {@link #businessStatus} (Contains business-specific nuances of the business state.)
     */
    public Task setBusinessStatus(CodeableConcept value) { 
      this.businessStatus = value;
      return this;
    }

    /**
     * @return {@link #stage} (Indicates the "level" of actionability associated with the Task.  I.e. Is this a proposed task, a planned task, an actionable task, etc.)
     */
    public CodeableConcept getStage() { 
      if (this.stage == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Task.stage");
        else if (Configuration.doAutoCreate())
          this.stage = new CodeableConcept(); // cc
      return this.stage;
    }

    public boolean hasStage() { 
      return this.stage != null && !this.stage.isEmpty();
    }

    /**
     * @param value {@link #stage} (Indicates the "level" of actionability associated with the Task.  I.e. Is this a proposed task, a planned task, an actionable task, etc.)
     */
    public Task setStage(CodeableConcept value) { 
      this.stage = value;
      return this;
    }

    /**
     * @return {@link #code} (A name or code (or both) briefly describing what the task involves.)
     */
    public CodeableConcept getCode() { 
      if (this.code == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Task.code");
        else if (Configuration.doAutoCreate())
          this.code = new CodeableConcept(); // cc
      return this.code;
    }

    public boolean hasCode() { 
      return this.code != null && !this.code.isEmpty();
    }

    /**
     * @param value {@link #code} (A name or code (or both) briefly describing what the task involves.)
     */
    public Task setCode(CodeableConcept value) { 
      this.code = value;
      return this;
    }

    /**
     * @return {@link #priority} (The priority of the task among other tasks of the same type.). This is the underlying object with id, value and extensions. The accessor "getPriority" gives direct access to the value
     */
    public Enumeration<TaskPriority> getPriorityElement() { 
      if (this.priority == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Task.priority");
        else if (Configuration.doAutoCreate())
          this.priority = new Enumeration<TaskPriority>(new TaskPriorityEnumFactory()); // bb
      return this.priority;
    }

    public boolean hasPriorityElement() { 
      return this.priority != null && !this.priority.isEmpty();
    }

    public boolean hasPriority() { 
      return this.priority != null && !this.priority.isEmpty();
    }

    /**
     * @param value {@link #priority} (The priority of the task among other tasks of the same type.). This is the underlying object with id, value and extensions. The accessor "getPriority" gives direct access to the value
     */
    public Task setPriorityElement(Enumeration<TaskPriority> value) { 
      this.priority = value;
      return this;
    }

    /**
     * @return The priority of the task among other tasks of the same type.
     */
    public TaskPriority getPriority() { 
      return this.priority == null ? null : this.priority.getValue();
    }

    /**
     * @param value The priority of the task among other tasks of the same type.
     */
    public Task setPriority(TaskPriority value) { 
      if (value == null)
        this.priority = null;
      else {
        if (this.priority == null)
          this.priority = new Enumeration<TaskPriority>(new TaskPriorityEnumFactory());
        this.priority.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #description} (A free-text description of what is to be performed.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public StringType getDescriptionElement() { 
      if (this.description == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Task.description");
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
     * @param value {@link #description} (A free-text description of what is to be performed.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public Task setDescriptionElement(StringType value) { 
      this.description = value;
      return this;
    }

    /**
     * @return A free-text description of what is to be performed.
     */
    public String getDescription() { 
      return this.description == null ? null : this.description.getValue();
    }

    /**
     * @param value A free-text description of what is to be performed.
     */
    public Task setDescription(String value) { 
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
     * @return {@link #focus} (The request being actioned or the resource being manipulated by this task.)
     */
    public Reference getFocus() { 
      if (this.focus == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Task.focus");
        else if (Configuration.doAutoCreate())
          this.focus = new Reference(); // cc
      return this.focus;
    }

    public boolean hasFocus() { 
      return this.focus != null && !this.focus.isEmpty();
    }

    /**
     * @param value {@link #focus} (The request being actioned or the resource being manipulated by this task.)
     */
    public Task setFocus(Reference value) { 
      this.focus = value;
      return this;
    }

    /**
     * @return {@link #focus} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The request being actioned or the resource being manipulated by this task.)
     */
    public Resource getFocusTarget() { 
      return this.focusTarget;
    }

    /**
     * @param value {@link #focus} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The request being actioned or the resource being manipulated by this task.)
     */
    public Task setFocusTarget(Resource value) { 
      this.focusTarget = value;
      return this;
    }

    /**
     * @return {@link #for_} (The entity who benefits from the performance of the service specified in the task (e.g., the patient).)
     */
    public Reference getFor() { 
      if (this.for_ == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Task.for_");
        else if (Configuration.doAutoCreate())
          this.for_ = new Reference(); // cc
      return this.for_;
    }

    public boolean hasFor() { 
      return this.for_ != null && !this.for_.isEmpty();
    }

    /**
     * @param value {@link #for_} (The entity who benefits from the performance of the service specified in the task (e.g., the patient).)
     */
    public Task setFor(Reference value) { 
      this.for_ = value;
      return this;
    }

    /**
     * @return {@link #for_} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The entity who benefits from the performance of the service specified in the task (e.g., the patient).)
     */
    public Resource getForTarget() { 
      return this.for_Target;
    }

    /**
     * @param value {@link #for_} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The entity who benefits from the performance of the service specified in the task (e.g., the patient).)
     */
    public Task setForTarget(Resource value) { 
      this.for_Target = value;
      return this;
    }

    /**
     * @return {@link #context} (The healthcare event  (e.g. a patient and healthcare provider interaction) during which this task was created.)
     */
    public Reference getContext() { 
      if (this.context == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Task.context");
        else if (Configuration.doAutoCreate())
          this.context = new Reference(); // cc
      return this.context;
    }

    public boolean hasContext() { 
      return this.context != null && !this.context.isEmpty();
    }

    /**
     * @param value {@link #context} (The healthcare event  (e.g. a patient and healthcare provider interaction) during which this task was created.)
     */
    public Task setContext(Reference value) { 
      this.context = value;
      return this;
    }

    /**
     * @return {@link #context} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The healthcare event  (e.g. a patient and healthcare provider interaction) during which this task was created.)
     */
    public Resource getContextTarget() { 
      return this.contextTarget;
    }

    /**
     * @param value {@link #context} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The healthcare event  (e.g. a patient and healthcare provider interaction) during which this task was created.)
     */
    public Task setContextTarget(Resource value) { 
      this.contextTarget = value;
      return this;
    }

    /**
     * @return {@link #created} (The date and time this task was created.). This is the underlying object with id, value and extensions. The accessor "getCreated" gives direct access to the value
     */
    public DateTimeType getCreatedElement() { 
      if (this.created == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Task.created");
        else if (Configuration.doAutoCreate())
          this.created = new DateTimeType(); // bb
      return this.created;
    }

    public boolean hasCreatedElement() { 
      return this.created != null && !this.created.isEmpty();
    }

    public boolean hasCreated() { 
      return this.created != null && !this.created.isEmpty();
    }

    /**
     * @param value {@link #created} (The date and time this task was created.). This is the underlying object with id, value and extensions. The accessor "getCreated" gives direct access to the value
     */
    public Task setCreatedElement(DateTimeType value) { 
      this.created = value;
      return this;
    }

    /**
     * @return The date and time this task was created.
     */
    public Date getCreated() { 
      return this.created == null ? null : this.created.getValue();
    }

    /**
     * @param value The date and time this task was created.
     */
    public Task setCreated(Date value) { 
        if (this.created == null)
          this.created = new DateTimeType();
        this.created.setValue(value);
      return this;
    }

    /**
     * @return {@link #lastModified} (The date and time of last modification to this task.). This is the underlying object with id, value and extensions. The accessor "getLastModified" gives direct access to the value
     */
    public DateTimeType getLastModifiedElement() { 
      if (this.lastModified == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Task.lastModified");
        else if (Configuration.doAutoCreate())
          this.lastModified = new DateTimeType(); // bb
      return this.lastModified;
    }

    public boolean hasLastModifiedElement() { 
      return this.lastModified != null && !this.lastModified.isEmpty();
    }

    public boolean hasLastModified() { 
      return this.lastModified != null && !this.lastModified.isEmpty();
    }

    /**
     * @param value {@link #lastModified} (The date and time of last modification to this task.). This is the underlying object with id, value and extensions. The accessor "getLastModified" gives direct access to the value
     */
    public Task setLastModifiedElement(DateTimeType value) { 
      this.lastModified = value;
      return this;
    }

    /**
     * @return The date and time of last modification to this task.
     */
    public Date getLastModified() { 
      return this.lastModified == null ? null : this.lastModified.getValue();
    }

    /**
     * @param value The date and time of last modification to this task.
     */
    public Task setLastModified(Date value) { 
        if (this.lastModified == null)
          this.lastModified = new DateTimeType();
        this.lastModified.setValue(value);
      return this;
    }

    /**
     * @return {@link #requester} (The creator of the task.)
     */
    public Reference getRequester() { 
      if (this.requester == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Task.requester");
        else if (Configuration.doAutoCreate())
          this.requester = new Reference(); // cc
      return this.requester;
    }

    public boolean hasRequester() { 
      return this.requester != null && !this.requester.isEmpty();
    }

    /**
     * @param value {@link #requester} (The creator of the task.)
     */
    public Task setRequester(Reference value) { 
      this.requester = value;
      return this;
    }

    /**
     * @return {@link #requester} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The creator of the task.)
     */
    public Resource getRequesterTarget() { 
      return this.requesterTarget;
    }

    /**
     * @param value {@link #requester} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The creator of the task.)
     */
    public Task setRequesterTarget(Resource value) { 
      this.requesterTarget = value;
      return this;
    }

    /**
     * @return {@link #owner} (The owner of this task.  The participant who can execute this task.)
     */
    public Reference getOwner() { 
      if (this.owner == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Task.owner");
        else if (Configuration.doAutoCreate())
          this.owner = new Reference(); // cc
      return this.owner;
    }

    public boolean hasOwner() { 
      return this.owner != null && !this.owner.isEmpty();
    }

    /**
     * @param value {@link #owner} (The owner of this task.  The participant who can execute this task.)
     */
    public Task setOwner(Reference value) { 
      this.owner = value;
      return this;
    }

    /**
     * @return {@link #owner} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The owner of this task.  The participant who can execute this task.)
     */
    public Resource getOwnerTarget() { 
      return this.ownerTarget;
    }

    /**
     * @param value {@link #owner} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The owner of this task.  The participant who can execute this task.)
     */
    public Task setOwnerTarget(Resource value) { 
      this.ownerTarget = value;
      return this;
    }

    /**
     * @return {@link #performerType} (The type of participant that can execute the task.)
     */
    public List<CodeableConcept> getPerformerType() { 
      if (this.performerType == null)
        this.performerType = new ArrayList<CodeableConcept>();
      return this.performerType;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Task setPerformerType(List<CodeableConcept> thePerformerType) { 
      this.performerType = thePerformerType;
      return this;
    }

    public boolean hasPerformerType() { 
      if (this.performerType == null)
        return false;
      for (CodeableConcept item : this.performerType)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addPerformerType() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.performerType == null)
        this.performerType = new ArrayList<CodeableConcept>();
      this.performerType.add(t);
      return t;
    }

    public Task addPerformerType(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.performerType == null)
        this.performerType = new ArrayList<CodeableConcept>();
      this.performerType.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #performerType}, creating it if it does not already exist
     */
    public CodeableConcept getPerformerTypeFirstRep() { 
      if (getPerformerType().isEmpty()) {
        addPerformerType();
      }
      return getPerformerType().get(0);
    }

    /**
     * @return {@link #reason} (A description or code indicating why this task needs to be performed.)
     */
    public CodeableConcept getReason() { 
      if (this.reason == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Task.reason");
        else if (Configuration.doAutoCreate())
          this.reason = new CodeableConcept(); // cc
      return this.reason;
    }

    public boolean hasReason() { 
      return this.reason != null && !this.reason.isEmpty();
    }

    /**
     * @param value {@link #reason} (A description or code indicating why this task needs to be performed.)
     */
    public Task setReason(CodeableConcept value) { 
      this.reason = value;
      return this;
    }

    /**
     * @return {@link #note} (Free-text information captured about the task as it progresses.)
     */
    public List<Annotation> getNote() { 
      if (this.note == null)
        this.note = new ArrayList<Annotation>();
      return this.note;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Task setNote(List<Annotation> theNote) { 
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

    public Task addNote(Annotation t) { //3
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
     * @return {@link #fulfillment} (Identifies any limitations on what part of a referenced task subject request should be actioned.)
     */
    public TaskFulfillmentComponent getFulfillment() { 
      if (this.fulfillment == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Task.fulfillment");
        else if (Configuration.doAutoCreate())
          this.fulfillment = new TaskFulfillmentComponent(); // cc
      return this.fulfillment;
    }

    public boolean hasFulfillment() { 
      return this.fulfillment != null && !this.fulfillment.isEmpty();
    }

    /**
     * @param value {@link #fulfillment} (Identifies any limitations on what part of a referenced task subject request should be actioned.)
     */
    public Task setFulfillment(TaskFulfillmentComponent value) { 
      this.fulfillment = value;
      return this;
    }

    /**
     * @return {@link #definition} (A reference to a formal or informal definition of the task.  For example, a protocol, a step within a defined workflow definition, etc.). This is the underlying object with id, value and extensions. The accessor "getDefinition" gives direct access to the value
     */
    public UriType getDefinitionElement() { 
      if (this.definition == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Task.definition");
        else if (Configuration.doAutoCreate())
          this.definition = new UriType(); // bb
      return this.definition;
    }

    public boolean hasDefinitionElement() { 
      return this.definition != null && !this.definition.isEmpty();
    }

    public boolean hasDefinition() { 
      return this.definition != null && !this.definition.isEmpty();
    }

    /**
     * @param value {@link #definition} (A reference to a formal or informal definition of the task.  For example, a protocol, a step within a defined workflow definition, etc.). This is the underlying object with id, value and extensions. The accessor "getDefinition" gives direct access to the value
     */
    public Task setDefinitionElement(UriType value) { 
      this.definition = value;
      return this;
    }

    /**
     * @return A reference to a formal or informal definition of the task.  For example, a protocol, a step within a defined workflow definition, etc.
     */
    public String getDefinition() { 
      return this.definition == null ? null : this.definition.getValue();
    }

    /**
     * @param value A reference to a formal or informal definition of the task.  For example, a protocol, a step within a defined workflow definition, etc.
     */
    public Task setDefinition(String value) { 
      if (Utilities.noString(value))
        this.definition = null;
      else {
        if (this.definition == null)
          this.definition = new UriType();
        this.definition.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #input} (Additional information that may be needed in the execution of the task.)
     */
    public List<ParameterComponent> getInput() { 
      if (this.input == null)
        this.input = new ArrayList<ParameterComponent>();
      return this.input;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Task setInput(List<ParameterComponent> theInput) { 
      this.input = theInput;
      return this;
    }

    public boolean hasInput() { 
      if (this.input == null)
        return false;
      for (ParameterComponent item : this.input)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public ParameterComponent addInput() { //3
      ParameterComponent t = new ParameterComponent();
      if (this.input == null)
        this.input = new ArrayList<ParameterComponent>();
      this.input.add(t);
      return t;
    }

    public Task addInput(ParameterComponent t) { //3
      if (t == null)
        return this;
      if (this.input == null)
        this.input = new ArrayList<ParameterComponent>();
      this.input.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #input}, creating it if it does not already exist
     */
    public ParameterComponent getInputFirstRep() { 
      if (getInput().isEmpty()) {
        addInput();
      }
      return getInput().get(0);
    }

    /**
     * @return {@link #output} (Outputs produced by the Task.)
     */
    public List<TaskOutputComponent> getOutput() { 
      if (this.output == null)
        this.output = new ArrayList<TaskOutputComponent>();
      return this.output;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Task setOutput(List<TaskOutputComponent> theOutput) { 
      this.output = theOutput;
      return this;
    }

    public boolean hasOutput() { 
      if (this.output == null)
        return false;
      for (TaskOutputComponent item : this.output)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public TaskOutputComponent addOutput() { //3
      TaskOutputComponent t = new TaskOutputComponent();
      if (this.output == null)
        this.output = new ArrayList<TaskOutputComponent>();
      this.output.add(t);
      return t;
    }

    public Task addOutput(TaskOutputComponent t) { //3
      if (t == null)
        return this;
      if (this.output == null)
        this.output = new ArrayList<TaskOutputComponent>();
      this.output.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #output}, creating it if it does not already exist
     */
    public TaskOutputComponent getOutputFirstRep() { 
      if (getOutput().isEmpty()) {
        addOutput();
      }
      return getOutput().get(0);
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "The business identifier for this task.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("basedOn", "Reference(Any)", "Identifies a plan, proposal or order that this task has been created in fulfillment of.", 0, java.lang.Integer.MAX_VALUE, basedOn));
        childrenList.add(new Property("requisition", "Identifier", "An identifier that links together multiple tasks and other requests that were created in the same context.", 0, java.lang.Integer.MAX_VALUE, requisition));
        childrenList.add(new Property("parent", "Reference(Task)", "Task that this particular task is part of.", 0, java.lang.Integer.MAX_VALUE, parent));
        childrenList.add(new Property("status", "code", "The current status of the task.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("statusReason", "CodeableConcept", "An explanation as to why this task is held, failed, was refused, etc.", 0, java.lang.Integer.MAX_VALUE, statusReason));
        childrenList.add(new Property("businessStatus", "CodeableConcept", "Contains business-specific nuances of the business state.", 0, java.lang.Integer.MAX_VALUE, businessStatus));
        childrenList.add(new Property("stage", "CodeableConcept", "Indicates the \"level\" of actionability associated with the Task.  I.e. Is this a proposed task, a planned task, an actionable task, etc.", 0, java.lang.Integer.MAX_VALUE, stage));
        childrenList.add(new Property("code", "CodeableConcept", "A name or code (or both) briefly describing what the task involves.", 0, java.lang.Integer.MAX_VALUE, code));
        childrenList.add(new Property("priority", "code", "The priority of the task among other tasks of the same type.", 0, java.lang.Integer.MAX_VALUE, priority));
        childrenList.add(new Property("description", "string", "A free-text description of what is to be performed.", 0, java.lang.Integer.MAX_VALUE, description));
        childrenList.add(new Property("focus", "Reference(Any)", "The request being actioned or the resource being manipulated by this task.", 0, java.lang.Integer.MAX_VALUE, focus));
        childrenList.add(new Property("for", "Reference(Any)", "The entity who benefits from the performance of the service specified in the task (e.g., the patient).", 0, java.lang.Integer.MAX_VALUE, for_));
        childrenList.add(new Property("context", "Reference(Encounter|EpisodeOfCare)", "The healthcare event  (e.g. a patient and healthcare provider interaction) during which this task was created.", 0, java.lang.Integer.MAX_VALUE, context));
        childrenList.add(new Property("created", "dateTime", "The date and time this task was created.", 0, java.lang.Integer.MAX_VALUE, created));
        childrenList.add(new Property("lastModified", "dateTime", "The date and time of last modification to this task.", 0, java.lang.Integer.MAX_VALUE, lastModified));
        childrenList.add(new Property("requester", "Reference(Device|Organization|Patient|Practitioner|RelatedPerson)", "The creator of the task.", 0, java.lang.Integer.MAX_VALUE, requester));
        childrenList.add(new Property("owner", "Reference(Device|Organization|Patient|Practitioner|RelatedPerson)", "The owner of this task.  The participant who can execute this task.", 0, java.lang.Integer.MAX_VALUE, owner));
        childrenList.add(new Property("performerType", "CodeableConcept", "The type of participant that can execute the task.", 0, java.lang.Integer.MAX_VALUE, performerType));
        childrenList.add(new Property("reason", "CodeableConcept", "A description or code indicating why this task needs to be performed.", 0, java.lang.Integer.MAX_VALUE, reason));
        childrenList.add(new Property("note", "Annotation", "Free-text information captured about the task as it progresses.", 0, java.lang.Integer.MAX_VALUE, note));
        childrenList.add(new Property("fulfillment", "", "Identifies any limitations on what part of a referenced task subject request should be actioned.", 0, java.lang.Integer.MAX_VALUE, fulfillment));
        childrenList.add(new Property("definition", "uri", "A reference to a formal or informal definition of the task.  For example, a protocol, a step within a defined workflow definition, etc.", 0, java.lang.Integer.MAX_VALUE, definition));
        childrenList.add(new Property("input", "", "Additional information that may be needed in the execution of the task.", 0, java.lang.Integer.MAX_VALUE, input));
        childrenList.add(new Property("output", "", "Outputs produced by the Task.", 0, java.lang.Integer.MAX_VALUE, output));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : new Base[] {this.identifier}; // Identifier
        case -332612366: /*basedOn*/ return this.basedOn == null ? new Base[0] : this.basedOn.toArray(new Base[this.basedOn.size()]); // Reference
        case 395923612: /*requisition*/ return this.requisition == null ? new Base[0] : new Base[] {this.requisition}; // Identifier
        case -995424086: /*parent*/ return this.parent == null ? new Base[0] : this.parent.toArray(new Base[this.parent.size()]); // Reference
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<TaskStatus>
        case 2051346646: /*statusReason*/ return this.statusReason == null ? new Base[0] : new Base[] {this.statusReason}; // CodeableConcept
        case 2008591314: /*businessStatus*/ return this.businessStatus == null ? new Base[0] : new Base[] {this.businessStatus}; // CodeableConcept
        case 109757182: /*stage*/ return this.stage == null ? new Base[0] : new Base[] {this.stage}; // CodeableConcept
        case 3059181: /*code*/ return this.code == null ? new Base[0] : new Base[] {this.code}; // CodeableConcept
        case -1165461084: /*priority*/ return this.priority == null ? new Base[0] : new Base[] {this.priority}; // Enumeration<TaskPriority>
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // StringType
        case 97604824: /*focus*/ return this.focus == null ? new Base[0] : new Base[] {this.focus}; // Reference
        case 101577: /*for*/ return this.for_ == null ? new Base[0] : new Base[] {this.for_}; // Reference
        case 951530927: /*context*/ return this.context == null ? new Base[0] : new Base[] {this.context}; // Reference
        case 1028554472: /*created*/ return this.created == null ? new Base[0] : new Base[] {this.created}; // DateTimeType
        case 1959003007: /*lastModified*/ return this.lastModified == null ? new Base[0] : new Base[] {this.lastModified}; // DateTimeType
        case 693933948: /*requester*/ return this.requester == null ? new Base[0] : new Base[] {this.requester}; // Reference
        case 106164915: /*owner*/ return this.owner == null ? new Base[0] : new Base[] {this.owner}; // Reference
        case -901444568: /*performerType*/ return this.performerType == null ? new Base[0] : this.performerType.toArray(new Base[this.performerType.size()]); // CodeableConcept
        case -934964668: /*reason*/ return this.reason == null ? new Base[0] : new Base[] {this.reason}; // CodeableConcept
        case 3387378: /*note*/ return this.note == null ? new Base[0] : this.note.toArray(new Base[this.note.size()]); // Annotation
        case 1512395230: /*fulfillment*/ return this.fulfillment == null ? new Base[0] : new Base[] {this.fulfillment}; // TaskFulfillmentComponent
        case -1014418093: /*definition*/ return this.definition == null ? new Base[0] : new Base[] {this.definition}; // UriType
        case 100358090: /*input*/ return this.input == null ? new Base[0] : this.input.toArray(new Base[this.input.size()]); // ParameterComponent
        case -1005512447: /*output*/ return this.output == null ? new Base[0] : this.output.toArray(new Base[this.output.size()]); // TaskOutputComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.identifier = castToIdentifier(value); // Identifier
          break;
        case -332612366: // basedOn
          this.getBasedOn().add(castToReference(value)); // Reference
          break;
        case 395923612: // requisition
          this.requisition = castToIdentifier(value); // Identifier
          break;
        case -995424086: // parent
          this.getParent().add(castToReference(value)); // Reference
          break;
        case -892481550: // status
          this.status = new TaskStatusEnumFactory().fromType(value); // Enumeration<TaskStatus>
          break;
        case 2051346646: // statusReason
          this.statusReason = castToCodeableConcept(value); // CodeableConcept
          break;
        case 2008591314: // businessStatus
          this.businessStatus = castToCodeableConcept(value); // CodeableConcept
          break;
        case 109757182: // stage
          this.stage = castToCodeableConcept(value); // CodeableConcept
          break;
        case 3059181: // code
          this.code = castToCodeableConcept(value); // CodeableConcept
          break;
        case -1165461084: // priority
          this.priority = new TaskPriorityEnumFactory().fromType(value); // Enumeration<TaskPriority>
          break;
        case -1724546052: // description
          this.description = castToString(value); // StringType
          break;
        case 97604824: // focus
          this.focus = castToReference(value); // Reference
          break;
        case 101577: // for
          this.for_ = castToReference(value); // Reference
          break;
        case 951530927: // context
          this.context = castToReference(value); // Reference
          break;
        case 1028554472: // created
          this.created = castToDateTime(value); // DateTimeType
          break;
        case 1959003007: // lastModified
          this.lastModified = castToDateTime(value); // DateTimeType
          break;
        case 693933948: // requester
          this.requester = castToReference(value); // Reference
          break;
        case 106164915: // owner
          this.owner = castToReference(value); // Reference
          break;
        case -901444568: // performerType
          this.getPerformerType().add(castToCodeableConcept(value)); // CodeableConcept
          break;
        case -934964668: // reason
          this.reason = castToCodeableConcept(value); // CodeableConcept
          break;
        case 3387378: // note
          this.getNote().add(castToAnnotation(value)); // Annotation
          break;
        case 1512395230: // fulfillment
          this.fulfillment = (TaskFulfillmentComponent) value; // TaskFulfillmentComponent
          break;
        case -1014418093: // definition
          this.definition = castToUri(value); // UriType
          break;
        case 100358090: // input
          this.getInput().add((ParameterComponent) value); // ParameterComponent
          break;
        case -1005512447: // output
          this.getOutput().add((TaskOutputComponent) value); // TaskOutputComponent
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier"))
          this.identifier = castToIdentifier(value); // Identifier
        else if (name.equals("basedOn"))
          this.getBasedOn().add(castToReference(value));
        else if (name.equals("requisition"))
          this.requisition = castToIdentifier(value); // Identifier
        else if (name.equals("parent"))
          this.getParent().add(castToReference(value));
        else if (name.equals("status"))
          this.status = new TaskStatusEnumFactory().fromType(value); // Enumeration<TaskStatus>
        else if (name.equals("statusReason"))
          this.statusReason = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("businessStatus"))
          this.businessStatus = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("stage"))
          this.stage = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("code"))
          this.code = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("priority"))
          this.priority = new TaskPriorityEnumFactory().fromType(value); // Enumeration<TaskPriority>
        else if (name.equals("description"))
          this.description = castToString(value); // StringType
        else if (name.equals("focus"))
          this.focus = castToReference(value); // Reference
        else if (name.equals("for"))
          this.for_ = castToReference(value); // Reference
        else if (name.equals("context"))
          this.context = castToReference(value); // Reference
        else if (name.equals("created"))
          this.created = castToDateTime(value); // DateTimeType
        else if (name.equals("lastModified"))
          this.lastModified = castToDateTime(value); // DateTimeType
        else if (name.equals("requester"))
          this.requester = castToReference(value); // Reference
        else if (name.equals("owner"))
          this.owner = castToReference(value); // Reference
        else if (name.equals("performerType"))
          this.getPerformerType().add(castToCodeableConcept(value));
        else if (name.equals("reason"))
          this.reason = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("note"))
          this.getNote().add(castToAnnotation(value));
        else if (name.equals("fulfillment"))
          this.fulfillment = (TaskFulfillmentComponent) value; // TaskFulfillmentComponent
        else if (name.equals("definition"))
          this.definition = castToUri(value); // UriType
        else if (name.equals("input"))
          this.getInput().add((ParameterComponent) value);
        else if (name.equals("output"))
          this.getOutput().add((TaskOutputComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return getIdentifier(); // Identifier
        case -332612366:  return addBasedOn(); // Reference
        case 395923612:  return getRequisition(); // Identifier
        case -995424086:  return addParent(); // Reference
        case -892481550: throw new FHIRException("Cannot make property status as it is not a complex type"); // Enumeration<TaskStatus>
        case 2051346646:  return getStatusReason(); // CodeableConcept
        case 2008591314:  return getBusinessStatus(); // CodeableConcept
        case 109757182:  return getStage(); // CodeableConcept
        case 3059181:  return getCode(); // CodeableConcept
        case -1165461084: throw new FHIRException("Cannot make property priority as it is not a complex type"); // Enumeration<TaskPriority>
        case -1724546052: throw new FHIRException("Cannot make property description as it is not a complex type"); // StringType
        case 97604824:  return getFocus(); // Reference
        case 101577:  return getFor(); // Reference
        case 951530927:  return getContext(); // Reference
        case 1028554472: throw new FHIRException("Cannot make property created as it is not a complex type"); // DateTimeType
        case 1959003007: throw new FHIRException("Cannot make property lastModified as it is not a complex type"); // DateTimeType
        case 693933948:  return getRequester(); // Reference
        case 106164915:  return getOwner(); // Reference
        case -901444568:  return addPerformerType(); // CodeableConcept
        case -934964668:  return getReason(); // CodeableConcept
        case 3387378:  return addNote(); // Annotation
        case 1512395230:  return getFulfillment(); // TaskFulfillmentComponent
        case -1014418093: throw new FHIRException("Cannot make property definition as it is not a complex type"); // UriType
        case 100358090:  return addInput(); // ParameterComponent
        case -1005512447:  return addOutput(); // TaskOutputComponent
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          this.identifier = new Identifier();
          return this.identifier;
        }
        else if (name.equals("basedOn")) {
          return addBasedOn();
        }
        else if (name.equals("requisition")) {
          this.requisition = new Identifier();
          return this.requisition;
        }
        else if (name.equals("parent")) {
          return addParent();
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type Task.status");
        }
        else if (name.equals("statusReason")) {
          this.statusReason = new CodeableConcept();
          return this.statusReason;
        }
        else if (name.equals("businessStatus")) {
          this.businessStatus = new CodeableConcept();
          return this.businessStatus;
        }
        else if (name.equals("stage")) {
          this.stage = new CodeableConcept();
          return this.stage;
        }
        else if (name.equals("code")) {
          this.code = new CodeableConcept();
          return this.code;
        }
        else if (name.equals("priority")) {
          throw new FHIRException("Cannot call addChild on a primitive type Task.priority");
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type Task.description");
        }
        else if (name.equals("focus")) {
          this.focus = new Reference();
          return this.focus;
        }
        else if (name.equals("for")) {
          this.for_ = new Reference();
          return this.for_;
        }
        else if (name.equals("context")) {
          this.context = new Reference();
          return this.context;
        }
        else if (name.equals("created")) {
          throw new FHIRException("Cannot call addChild on a primitive type Task.created");
        }
        else if (name.equals("lastModified")) {
          throw new FHIRException("Cannot call addChild on a primitive type Task.lastModified");
        }
        else if (name.equals("requester")) {
          this.requester = new Reference();
          return this.requester;
        }
        else if (name.equals("owner")) {
          this.owner = new Reference();
          return this.owner;
        }
        else if (name.equals("performerType")) {
          return addPerformerType();
        }
        else if (name.equals("reason")) {
          this.reason = new CodeableConcept();
          return this.reason;
        }
        else if (name.equals("note")) {
          return addNote();
        }
        else if (name.equals("fulfillment")) {
          this.fulfillment = new TaskFulfillmentComponent();
          return this.fulfillment;
        }
        else if (name.equals("definition")) {
          throw new FHIRException("Cannot call addChild on a primitive type Task.definition");
        }
        else if (name.equals("input")) {
          return addInput();
        }
        else if (name.equals("output")) {
          return addOutput();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "Task";

  }

      public Task copy() {
        Task dst = new Task();
        copyValues(dst);
        dst.identifier = identifier == null ? null : identifier.copy();
        if (basedOn != null) {
          dst.basedOn = new ArrayList<Reference>();
          for (Reference i : basedOn)
            dst.basedOn.add(i.copy());
        };
        dst.requisition = requisition == null ? null : requisition.copy();
        if (parent != null) {
          dst.parent = new ArrayList<Reference>();
          for (Reference i : parent)
            dst.parent.add(i.copy());
        };
        dst.status = status == null ? null : status.copy();
        dst.statusReason = statusReason == null ? null : statusReason.copy();
        dst.businessStatus = businessStatus == null ? null : businessStatus.copy();
        dst.stage = stage == null ? null : stage.copy();
        dst.code = code == null ? null : code.copy();
        dst.priority = priority == null ? null : priority.copy();
        dst.description = description == null ? null : description.copy();
        dst.focus = focus == null ? null : focus.copy();
        dst.for_ = for_ == null ? null : for_.copy();
        dst.context = context == null ? null : context.copy();
        dst.created = created == null ? null : created.copy();
        dst.lastModified = lastModified == null ? null : lastModified.copy();
        dst.requester = requester == null ? null : requester.copy();
        dst.owner = owner == null ? null : owner.copy();
        if (performerType != null) {
          dst.performerType = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : performerType)
            dst.performerType.add(i.copy());
        };
        dst.reason = reason == null ? null : reason.copy();
        if (note != null) {
          dst.note = new ArrayList<Annotation>();
          for (Annotation i : note)
            dst.note.add(i.copy());
        };
        dst.fulfillment = fulfillment == null ? null : fulfillment.copy();
        dst.definition = definition == null ? null : definition.copy();
        if (input != null) {
          dst.input = new ArrayList<ParameterComponent>();
          for (ParameterComponent i : input)
            dst.input.add(i.copy());
        };
        if (output != null) {
          dst.output = new ArrayList<TaskOutputComponent>();
          for (TaskOutputComponent i : output)
            dst.output.add(i.copy());
        };
        return dst;
      }

      protected Task typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof Task))
          return false;
        Task o = (Task) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(basedOn, o.basedOn, true) && compareDeep(requisition, o.requisition, true)
           && compareDeep(parent, o.parent, true) && compareDeep(status, o.status, true) && compareDeep(statusReason, o.statusReason, true)
           && compareDeep(businessStatus, o.businessStatus, true) && compareDeep(stage, o.stage, true) && compareDeep(code, o.code, true)
           && compareDeep(priority, o.priority, true) && compareDeep(description, o.description, true) && compareDeep(focus, o.focus, true)
           && compareDeep(for_, o.for_, true) && compareDeep(context, o.context, true) && compareDeep(created, o.created, true)
           && compareDeep(lastModified, o.lastModified, true) && compareDeep(requester, o.requester, true)
           && compareDeep(owner, o.owner, true) && compareDeep(performerType, o.performerType, true) && compareDeep(reason, o.reason, true)
           && compareDeep(note, o.note, true) && compareDeep(fulfillment, o.fulfillment, true) && compareDeep(definition, o.definition, true)
           && compareDeep(input, o.input, true) && compareDeep(output, o.output, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Task))
          return false;
        Task o = (Task) other;
        return compareValues(status, o.status, true) && compareValues(priority, o.priority, true) && compareValues(description, o.description, true)
           && compareValues(created, o.created, true) && compareValues(lastModified, o.lastModified, true) && compareValues(definition, o.definition, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, basedOn, requisition
          , parent, status, statusReason, businessStatus, stage, code, priority, description
          , focus, for_, context, created, lastModified, requester, owner, performerType
          , reason, note, fulfillment, definition, input, output);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Task;
   }

 /**
   * Search parameter: <b>owner</b>
   * <p>
   * Description: <b>Search by task owner</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Task.owner</b><br>
   * </p>
   */
  @SearchParamDefinition(name="owner", path="Task.owner", description="Search by task owner", type="reference", target={Device.class, Organization.class, Patient.class, Practitioner.class, RelatedPerson.class } )
  public static final String SP_OWNER = "owner";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>owner</b>
   * <p>
   * Description: <b>Search by task owner</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Task.owner</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam OWNER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_OWNER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Task:owner</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_OWNER = new ca.uhn.fhir.model.api.Include("Task:owner").toLocked();

 /**
   * Search parameter: <b>requester</b>
   * <p>
   * Description: <b>Search by task requester</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Task.requester</b><br>
   * </p>
   */
  @SearchParamDefinition(name="requester", path="Task.requester", description="Search by task requester", type="reference", target={Device.class, Organization.class, Patient.class, Practitioner.class, RelatedPerson.class } )
  public static final String SP_REQUESTER = "requester";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>requester</b>
   * <p>
   * Description: <b>Search by task requester</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Task.requester</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam REQUESTER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_REQUESTER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Task:requester</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_REQUESTER = new ca.uhn.fhir.model.api.Include("Task:requester").toLocked();

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>Search for a task instance by its business identifier</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Task.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="Task.identifier", description="Search for a task instance by its business identifier", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>Search for a task instance by its business identifier</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Task.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>parent</b>
   * <p>
   * Description: <b>Search by parent task</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Task.parent</b><br>
   * </p>
   */
  @SearchParamDefinition(name="parent", path="Task.parent", description="Search by parent task", type="reference", target={Task.class } )
  public static final String SP_PARENT = "parent";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>parent</b>
   * <p>
   * Description: <b>Search by parent task</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Task.parent</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PARENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PARENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Task:parent</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PARENT = new ca.uhn.fhir.model.api.Include("Task:parent").toLocked();

 /**
   * Search parameter: <b>code</b>
   * <p>
   * Description: <b>Search by task code</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Task.code</b><br>
   * </p>
   */
  @SearchParamDefinition(name="code", path="Task.code", description="Search by task code", type="token" )
  public static final String SP_CODE = "code";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>code</b>
   * <p>
   * Description: <b>Search by task code</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Task.code</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CODE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CODE);

 /**
   * Search parameter: <b>performer</b>
   * <p>
   * Description: <b>Search by recommended type of performer (e.g., Requester, Performer, Scheduler).</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Task.performerType</b><br>
   * </p>
   */
  @SearchParamDefinition(name="performer", path="Task.performerType", description="Search by recommended type of performer (e.g., Requester, Performer, Scheduler).", type="token" )
  public static final String SP_PERFORMER = "performer";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>performer</b>
   * <p>
   * Description: <b>Search by recommended type of performer (e.g., Requester, Performer, Scheduler).</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Task.performerType</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam PERFORMER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_PERFORMER);

 /**
   * Search parameter: <b>created</b>
   * <p>
   * Description: <b>Search by creation date</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Task.created</b><br>
   * </p>
   */
  @SearchParamDefinition(name="created", path="Task.created", description="Search by creation date", type="date" )
  public static final String SP_CREATED = "created";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>created</b>
   * <p>
   * Description: <b>Search by creation date</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Task.created</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam CREATED = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_CREATED);

 /**
   * Search parameter: <b>focus</b>
   * <p>
   * Description: <b>Search by task focus</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Task.focus</b><br>
   * </p>
   */
  @SearchParamDefinition(name="focus", path="Task.focus", description="Search by task focus", type="reference" )
  public static final String SP_FOCUS = "focus";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>focus</b>
   * <p>
   * Description: <b>Search by task focus</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Task.focus</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam FOCUS = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_FOCUS);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Task:focus</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_FOCUS = new ca.uhn.fhir.model.api.Include("Task:focus").toLocked();

 /**
   * Search parameter: <b>priority</b>
   * <p>
   * Description: <b>Search by task priority</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Task.priority</b><br>
   * </p>
   */
  @SearchParamDefinition(name="priority", path="Task.priority", description="Search by task priority", type="token" )
  public static final String SP_PRIORITY = "priority";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>priority</b>
   * <p>
   * Description: <b>Search by task priority</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Task.priority</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam PRIORITY = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_PRIORITY);

 /**
   * Search parameter: <b>stage</b>
   * <p>
   * Description: <b>Search by task stage</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Task.stage</b><br>
   * </p>
   */
  @SearchParamDefinition(name="stage", path="Task.stage", description="Search by task stage", type="token" )
  public static final String SP_STAGE = "stage";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>stage</b>
   * <p>
   * Description: <b>Search by task stage</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Task.stage</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STAGE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STAGE);

 /**
   * Search parameter: <b>statusreason</b>
   * <p>
   * Description: <b>Search by status reason</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Task.statusReason</b><br>
   * </p>
   */
  @SearchParamDefinition(name="statusreason", path="Task.statusReason", description="Search by status reason", type="token" )
  public static final String SP_STATUSREASON = "statusreason";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>statusreason</b>
   * <p>
   * Description: <b>Search by status reason</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Task.statusReason</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUSREASON = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUSREASON);

 /**
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>Search by patient</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Task.for</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="Task.for", description="Search by patient", type="reference", target={Patient.class } )
  public static final String SP_PATIENT = "patient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patient</b>
   * <p>
   * Description: <b>Search by patient</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Task.for</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PATIENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PATIENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Task:patient</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PATIENT = new ca.uhn.fhir.model.api.Include("Task:patient").toLocked();

 /**
   * Search parameter: <b>modified</b>
   * <p>
   * Description: <b>Search by last modification date</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Task.lastModified</b><br>
   * </p>
   */
  @SearchParamDefinition(name="modified", path="Task.lastModified", description="Search by last modification date", type="date" )
  public static final String SP_MODIFIED = "modified";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>modified</b>
   * <p>
   * Description: <b>Search by last modification date</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Task.lastModified</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam MODIFIED = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_MODIFIED);

 /**
   * Search parameter: <b>definition</b>
   * <p>
   * Description: <b>Search by task definition</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>Task.definition</b><br>
   * </p>
   */
  @SearchParamDefinition(name="definition", path="Task.definition", description="Search by task definition", type="uri" )
  public static final String SP_DEFINITION = "definition";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>definition</b>
   * <p>
   * Description: <b>Search by task definition</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>Task.definition</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.UriClientParam DEFINITION = new ca.uhn.fhir.rest.gclient.UriClientParam(SP_DEFINITION);

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>Search by task status</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Task.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="Task.status", description="Search by task status", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>Search by task status</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Task.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);


}

